package mnj.ont.view.backing;


import javax.faces.event.ActionEvent;

import mnj.ont.model.services.AppModuleImpl;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import oracle.jdbc.OracleTypes;

import utils.system;


public class PopUpBean {
    private RichTable matTable;
    private RichTable mytable;
    

    public PopUpBean() {
    }

    AppModuleImpl appM = getAppModuleImpl();

    public AppModuleImpl getAppModuleImpl() {
        DCBindingContainer bindingContainer =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        //BindingContext bindingContext = BindingContext.getCurrent();
        DCDataControl dc =
            bindingContainer.findDataControl("AppModuleDataControl"); // Name of application module in datacontrolBinding.cpx
        AppModuleImpl appM = (AppModuleImpl)dc.getDataProvider();
        return appM;
    }





    public void setMatTable(RichTable matTable) {
        this.matTable = matTable;
    }

    public RichTable getMatTable() {
        return matTable;
    }

//    public void selectAll(ValueChangeEvent valueChangeEvent) {
//        // Add event code here...
//        System.out.println("xdebug c1 : In selectAllChoiceBoxLN with value = " +
//                           valueChangeEvent.getNewValue());
//
//        boolean isSelected =
//            ((Boolean)valueChangeEvent.getNewValue()).booleanValue();
//        DCBindingContainer dcb = (DCBindingContainer)evaluateEL("#{bindings}");
//        DCIteratorBinding dciter = dcb.findIteratorBinding("ComponentLov1Iterator");
//
//        ViewObject vo = dciter.getViewObject();
//        //RowSetIterator it = vo.createRowSetIterator("");   
//      
//        int i = 0;
//        Row row = null;
//        vo.reset();
//        while (vo.hasNext()) {
//            if (i == 0)
//                row = vo.first();
//            else
//                row = vo.next();
//            //            System.out.println("Changing row 1: " +
//            //              row.getAttribute("Name"));
//            System.out.println("xdebug c2: Changing row 2: " +
//                               row.getAttribute("MultiSelect"));
//
//            if (isSelected)
//                row.setAttribute("MultiSelect", "Y");
//            else
//                row.setAttribute("MultiSelect", "N");
//            i++;
//        }
//        AdfFacesContext.getCurrentInstance().addPartialTarget(matTable);
//    }

//    private static Object evaluateEL(String el) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ELContext elContext = facesContext.getELContext();
//        ExpressionFactory expressionFactory =
//            facesContext.getApplication().getExpressionFactory();
//        ValueExpression exp =
//            expressionFactory.createValueExpression(elContext, el,
//                                                    Object.class);
//        return exp.getValue(elContext);
//
//
//    }
    
    
    public BindingContainer getBindings() {
           return BindingContext.getCurrent().getCurrentBindingsEntry();
       }
    public void editPopupFetchListenerNew(PopupFetchEvent popupFetchEvent) {
           
              if (popupFetchEvent.getLaunchSourceClientId().contains("cbInsert")) {
                
              }
          }
    
    public void editDialogListenerNew(DialogEvent dialogEvent) {
           if (dialogEvent.getOutcome().name().equals("ok")) {     
            FillMaterial();
             //  AdfFacesContext.getCurrentInstance().addPartialTarget(mytable);
            
                
           } else if (dialogEvent.getOutcome().name().equals("cancel")) {          
            ;
           }
       }
    
    public void editPopupCancelListenerNew(PopupCanceledEvent popupCanceledEvent) {
           
       }
    
    
    public  void FillMaterial() {
        
        BindingContext bctx = BindingContext.getCurrent();
        BindingContainer bindings = bctx.getCurrentBindingsEntry();
        OperationBinding operationBinding =
            bindings.getOperationBinding("callMatFetch");               
        //invoke method
        operationBinding.execute();
        
        
    }

    public void setMytable(RichTable mytable) {
        this.mytable = mytable;
    }

    public RichTable getMytable() {
        return mytable;
    }


    public void createPoDff(ActionEvent actionEvent) {
        // Add event code here...
        ViewObject poDffVo = appM.getMnjBomPoDffVO1();
        ViewObject headerVo = appM.getCustMnjOntBomHeaderView1();
        ViewObject autoVo=appM.getCustMnjOntBomRmlineEO_autoView1();
        String orgId =  autoVo.getCurrentRow().getAttribute("OrgId").toString();
                                                        
        String currentBomId ;
      
         currentBomId = autoVo.getCurrentRow().getAttribute("BomId").toString();
        //System.out.println("===================  currentPrNo  "+ currentPrNo);
        Row row  = poDffVo.createRow();
        row.setAttribute("BomId",currentBomId );    
        row.setAttribute("OrgId",orgId );    
        
          
    }

    private String getCurrentBomId() {
      ViewObject bomHeaderVo = appM.getCustMnjOntBomHeaderView1();
      Row currnetRow = bomHeaderVo.getCurrentRow();
      String bomId ;
      try{
          bomId =currnetRow.getAttribute("BomId").toString();
      }catch(Exception e){
          bomId="";
      }
      return bomId;
    }


    public void saveAndCreatePo(ActionEvent actionEvent) {
        // Add event code here...
        
       appM.getDBTransaction().commit();
       
        
        ViewObject poDffVo = appM.getMnjBomPoDffVO1();
        Row curentPoDffRow;
        try{
            curentPoDffRow = poDffVo.getCurrentRow();
        }catch(Exception e){
            System.out.println(" exception in   curentPoDffRow = poDffVo.getCurrentRow(); ");
            return;
        }
        
        String orgId ; 
        String userId;
        String prNo;
        try{
            orgId = curentPoDffRow.getAttribute("OrgId").toString();
            userId = curentPoDffRow.getAttribute("UserId").toString();
            prNo = curentPoDffRow.getAttribute("PrNo").toString();
        }catch(Exception e){
            System.out.println("exception in orgId = curentPoDffRow.getAttribute(\"OrgId\").toString()");
            return;
        }
        
        String stmt = "BEGIN APPS.CREATE_AUTO_PO(:1,:2,:3,:4,:5); end;";
        java.sql.CallableStatement cs =appM.getDBTransaction().createCallableStatement(stmt, 1);

        try {
            cs.registerOutParameter(1, OracleTypes.VARCHAR);
            cs.registerOutParameter(2, OracleTypes.VARCHAR);
            cs.setString(3, prNo);
            cs.setInt(4, Integer.parseInt(userId));
            cs.setInt(5, Integer.parseInt(orgId));
            cs.execute();
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
         
    }


    public void editPopUpFetchListenerPoDff(PopupFetchEvent popupFetchEvent) {
        // Add event code here...
        
       
        
    }
}

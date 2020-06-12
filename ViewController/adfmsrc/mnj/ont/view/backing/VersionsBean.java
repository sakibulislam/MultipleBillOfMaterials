package mnj.ont.view.backing;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.layout.RichPanelHeader;

import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.Number;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class VersionsBean {
    private RichInputText bomId;
    private oracle.jbo.domain.Number scopeVariable;
    private RichPanelHeader hederNo;
    private RichInputListOfValues hederno2;
    private RichOutputText showAttachText;

    public VersionsBean() {
        super();
    }

    public void setBomId(RichInputText bomId) {
        this.bomId = bomId;
    }

    public RichInputText getBomId() {
        return bomId;
    }
    
    public void versionsActionListener(ActionEvent actionEvent) {
        // Add event code here...
        //#{pageFlowScope.TestPageFlowBean.versionsActionListener}
        //#{pageFlowScope.testPageFlowBean.scopeVariable}
        System.out.println("value in bean --->"+getHederno2().getValue());
        setScopeVariable((oracle.jbo.domain.Number)getHederno2().getValue());
        System.out.println("get value-->"+getScopeVariable());
        
    }
    public void setScopeVariable(Number scopeVariable) {
        this.scopeVariable = scopeVariable;
    }

    public Number getScopeVariable() {
        return scopeVariable;
    }

    public void setHederNo(RichPanelHeader hederNo) {
        this.hederNo = hederNo;
    }

    public RichPanelHeader getHederNo() {
        return hederNo;
    }

    public void setHederno2(RichInputListOfValues hederno2) {
        this.hederno2 = hederno2;
    }

    public RichInputListOfValues getHederno2() {
        return hederno2;
    }
    
    public String callAttachment() throws IOException {
      
        String doc= null;     
        BindingContext bindingContext = BindingContext.getCurrent(); 
        DCDataControl dc = bindingContext.findDataControl("AppModuleDataControl"); //
        ApplicationModule am  = dc.getApplicationModule() ;
        ViewObject findViewObject = am.findViewObject("CustMnjOntBomHeaderView1");
        
        try {
            doc = findViewObject.getCurrentRow().getAttribute("BomNumber").toString();
        } catch (Exception e) {
            // TODO: Add catch code
            ;
        }    
      
      //.....................changing.....................//
//            String newPage =
//                "http://192.168.200.115:7003/FileUploading-ViewController-context-root/faces/view1?doc=MB&docNo="+doc;
//            // String newPage = "http://localhost:7101/PurchaseMemo-ViewController-context-root/faces/SearchPG?headerId="+getBomId().getValue();
//            FacesContext ctx = FacesContext.getCurrentInstance();
//            ExtendedRenderKitService erks =
//                Service.getService(ctx.getRenderKit(), ExtendedRenderKitService.class);
//            String url = "window.open('" + newPage + "','_self');";
//            erks.addScript(FacesContext.getCurrentInstance(), url);
//            
//            
//        FacesContext fc = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
//        response.sendRedirect("/FileUploading-ViewController-context-root/faces/view1?doc=MB&docNo="+doc);
//        fc.responseComplete();
//        
             String newPage =
                 "http://192.168.200.115:7003/FileUploading-ViewController-context-root/faces/view1?doc=CD_Invoice_No&docNo="+doc;
             // String newPage = "http://localhost:7101/PurchaseMemo-ViewController-context-root/faces/SearchPG?headerId="+getBomId().getValue();
             FacesContext ctx = FacesContext.getCurrentInstance();
             ExtendedRenderKitService erks = Service.getService(ctx.getRenderKit(), ExtendedRenderKitService.class);
             String url = "window.open('" + newPage + "','_blank','toolbar=no,location=no,menubar=no,alwaysRaised=yes,height=500,width=1100');";
             erks.addScript(FacesContext.getCurrentInstance(), url);
//..................changing END.............................////

        return null;
    }

    public void goPrAction(ActionEvent actionEvent) {
        // Add event code here...
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        HttpSession userSession = (HttpSession) ectx.getSession(false);
        userSession.setAttribute("BOMNO",String.valueOf(getHederno2().getValue()));
    }

    public void setShowAttachText(RichOutputText showAttachText) {
        this.showAttachText = showAttachText;
    }

    public RichOutputText getShowAttachText() {
        return showAttachText;
    }

    public void AttachFetchListener(PopupFetchEvent popupFetchEvent) {
        // Add event code here...
        BindingContainer bindings = getBindings();
             OperationBinding operationBinding =
                 bindings.getOperationBinding("getAttachments");         

             operationBinding.execute();
             
             if (!operationBinding.getErrors().isEmpty()) {
                 System.out.println("errore-->");
             }
            Object methodReturnValue = operationBinding.getResult();       
            showAttachText.setValue(methodReturnValue);
            AdfFacesContext.getCurrentInstance().addPartialTarget(showAttachText);
    }

    public void CancelLsitener(PopupCanceledEvent popupCanceledEvent) {
        // Add event code here...
    }
    
    public BindingContainer getBindings() {
           return BindingContext.getCurrent().getCurrentBindingsEntry();
       }

}

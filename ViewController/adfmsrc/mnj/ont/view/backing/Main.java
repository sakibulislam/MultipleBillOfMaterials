package mnj.ont.view.backing;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.validator.ValidatorException;

import javax.servlet.http.HttpSession;

import mnj.ont.model.services.AppModuleImpl;
import mnj.ont.model.views.CustMnjOntBomRmlineLinesViewRowImpl;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.faces.bi.component.pivotTable.CellFormat;
import oracle.adf.view.faces.bi.component.pivotTable.DataCellContext;
import oracle.adf.view.faces.bi.component.pivotTable.HeaderCellContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputComboboxListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.ItemEvent;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierNodeBinding;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.dss.util.QDR;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.jbo.RowSet;

import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class Main {

    private RichTable tableListener;
    private RichInputText bomId;
    private RichInputText invItemId;
    private RichTable obTab;

    private RichTable newMatTabBEan;
    private RichInputText poQty;
    private RichTable matTabBean;
    private RichTable stnTable;
    private RichInputText bookQty;
    private RichInputListOfValues bpoNo;

    private RichInputComboboxListOfValues styleName;
    private RichInputText buyerId;

    private RichTable bpOTable;
    private RichTable countryTable;
    private RichTable colorTable;
    private RichTable sizeTable;

    private RichTable lineTable;

    private ViewObject lineVo;
    private RichInputText sizeActualQtyBind;
    private RichInputText sizeWiseBomReqBind;
    private RichInputText sizeWiseProjQtyBind;
    private RichTable assignBpo_BindTB;
    private RichTable fillBPO_BindTB;

    //modification
    private RichInputText addDeductQtyBind;
    private RichInputComboboxListOfValues currency;
    private RichTable headerPending;
    private RichTable sizeBreakdownTable;
    private RichInputComboboxListOfValues purchaseType;
    private RichTable threadTable;
    private RichTable zipperTable;
    private RichTable otherTable;
    private RichShowDetailItem multieditFillFab;
    private RichPopup filterPopUp;
    private RichPopup headerVO;
    private RichTable fillFabricTable;
    private RichShowDetailItem multieditFillThread;
    private RichShowDetailItem multieditFilZipper;
    private RichShowDetailItem multieditFilOthers;
    private RichTable fillthreadTable;
    private RichTable fillZipperTable;
    private RichTable fillOtherTable;
    private RichPopup supplierpopup;

    public ApplicationModule getAppM() {
        DCBindingContainer bindingContainer =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        //BindingContext bindingContext = BindingContext.getCurrent();
        DCDataControl dc =
            bindingContainer.findDataControl("AppModuleDataControl"); // Name of application module in datacontrolBinding.cpx
        AppModuleImpl appM = (AppModuleImpl)dc.getDataProvider();
        return appM;
    }
    AppModuleImpl am = (AppModuleImpl)this.getAppM();

    ///////////////////////////////////////////////////////////

    public Main() {
        super();

        BindingContext bindingContext = BindingContext.getCurrent();
        DCDataControl dc =
            bindingContext.findDataControl("AppModuleDataControl"); //
        ApplicationModule am = dc.getApplicationModule();
        lineVo = am.findViewObject("BomLinesPRHistoryVO1");
    }

    public void fetchLinesAction(ActionEvent actionEvent) {

        oracle.jbo.domain.Number value =
            (oracle.jbo.domain.Number)actionEvent.getComponent().getAttributes().get("pSaleOrderId");
        System.out.println("VAlue of sale order in bean" + value);


    } //end of method

    public double getNumericVal(Object value) {

        try {
            if (value != null)
                return Double.parseDouble(String.valueOf(value));
            else
                return 0;
        } catch (Exception e) {

            return 0;
        }
    } //end of numeric value


    public void refreshValues() {

        //double sum = getNumericVal(getConsumtion().getValue()) + getNumericVal(getWithExtra().getValue());
        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        double consQtyVal =
            getNumericVal(r.getAttribute("UsageMoUnit").toString());


        double wastageQtyVal =
            getNumericVal(r.getAttribute("ExtraUsage").toString());
        double orderQtyVal = getNumericVal(getSizesQty());
        double reqQtyVal = 0.0;
        String type = null;
        try {
            type = r.getAttribute("PurchaseType").toString();
        } catch (Exception e) {
            type = "Non-Generic";
        }
        if (type.equals("Generic")) {
            reqQtyVal = getReqQty();
        } else {
            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * consQtyVal) * (1 + (wastageQtyVal /
                                                                         100)));
        }
        System.out.println("orderQty= " + orderQtyVal);
        System.out.println("reqQty= " + reqQtyVal);

        double alocatReqValCAL =
            getNumericVal(r.getAttribute("AlocateIntrQty").toString());
        double alocatReqValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocatReqValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());
        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        double prcntg = getNumericVal(r.getAttribute("Perntg"));
        double projPRQty = 0.0;
        double unitConvRateVal = getNumericVal(r.getAttribute("UomConvRate"));
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        double actualPrcntVal = 0.0;


        if (unitConvRateVal > 0) {
            reqQtyVal = reqQtyVal / unitConvRateVal;
            System.out.println(reqQtyVal);
        }

        reqQtyVal = MyMath.roundUp(reqQtyVal);

        try {
            projPRQty = prcntg * reqQtyVal / 100;
        } catch (Exception e) {
            // TODO: Add catch code

        }


        //        System.out.println("Actual pecent phatan wari value --->"+actualPrcntVal);
        // System.out.println("Bom round up qty ---->"+MyMath.roundUp(reqQtyVal));
        try {
            r.setAttribute("NoOfGarment",
                           new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));

            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(projPRQty +
                                                        addiQtyVal));
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number((projPRQty + addiQtyVal) *
                                                        pricePerUnitVal));
            r.setAttribute("ProjPrQty",
                           new oracle.jbo.domain.Number(projPRQty));

        } catch (SQLException e) {
            ;
        }

        String itemId = r.getAttribute("InventoryItemId").toString();
        double actualPrQtySumVal =
            getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal * 100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        double pendingPrcnt =
            MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                         100));

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));


    } //end of refreshValues methodconsExtra


    public void generalListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        double consQtyVal = getNumericVal(valueChangeEvent.getNewValue());


        double wastageQtyVal = getNumericVal(r.getAttribute("ExtraUsage"));
        double orderQtyVal = getNumericVal(getSizesQty());
        double reqQtyVal = 0.0;
        String type = null;
        try {
            type = r.getAttribute("PurchaseType").toString();
        } catch (Exception e) {
            type = "Non-Generic";
        }
        if (type.equals("Generic")) {
            reqQtyVal = getReqQty();
        } else {
            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * consQtyVal) * (1 + (wastageQtyVal /
                                                                         100)));
        }
        System.out.println("orderQty= " + orderQtyVal);
        System.out.println("reqQty= " + reqQtyVal);

        double alocatReqValCAL =
            getNumericVal(r.getAttribute("AlocateIntrQty").toString());
        double alocatReqValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocatReqValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());
        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        double prcntg = getNumericVal(r.getAttribute("Perntg"));
        double projPRQty = 0.0;
        double unitConvRateVal = getNumericVal(r.getAttribute("UomConvRate"));
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        double actualPrcntVal = 0.0;


        if (unitConvRateVal > 0) {
            reqQtyVal = reqQtyVal / unitConvRateVal;
            System.out.println(reqQtyVal);
        }

        reqQtyVal = MyMath.roundUp(reqQtyVal);

        try {
            projPRQty = prcntg * reqQtyVal / 100;
        } catch (Exception e) {
            // TODO: Add catch code

        }


        //        System.out.println("Actual pecent phatan wari value --->"+actualPrcntVal);
        // System.out.println("Bom round up qty ---->"+MyMath.roundUp(reqQtyVal));
        try {
            r.setAttribute("NoOfGarment",
                           new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));

            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(projPRQty +
                                                        addiQtyVal));
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number((projPRQty + addiQtyVal) *
                                                        pricePerUnitVal));
            r.setAttribute("ProjPrQty",
                           new oracle.jbo.domain.Number(projPRQty));

        } catch (SQLException e) {
            ;
        }

        String itemId = r.getAttribute("InventoryItemId").toString();
        double actualPrQtySumVal =
            getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal * 100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        double pendingPrcnt =
            MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                         100));

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(lineTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(threadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(zipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otherTable);

    }

    public void BookingListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        double consQtyVal = getNumericVal(r.getAttribute("UsageMoUnit"));


        double wastageQtyVal = getNumericVal(r.getAttribute("ExtraUsage"));
        double orderQtyVal = getNumericVal(getSizesQty());
        double reqQtyVal = 0.0;
        String type = null;
        try {
            type = r.getAttribute("PurchaseType").toString();
        } catch (Exception e) {
            type = "Non-Generic";
        }
        if (type.equals("Generic")) {
            reqQtyVal = getReqQty();
        } else {
            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * consQtyVal) * (1 + (wastageQtyVal /
                                                                         100)));
        }
        System.out.println("orderQty= " + orderQtyVal);
        System.out.println("reqQty= " + reqQtyVal);

        double alocatReqValCAL =
            getNumericVal(r.getAttribute("AlocateIntrQty").toString());
        double alocatReqValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocatReqValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());
        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        double prcntg = getNumericVal(valueChangeEvent.getNewValue());
        double projPRQty = 0.0;
        double unitConvRateVal = getNumericVal(r.getAttribute("UomConvRate"));
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        double actualPrcntVal = 0.0;


        if (unitConvRateVal > 0) {
            reqQtyVal = reqQtyVal / unitConvRateVal;
            System.out.println(reqQtyVal);
        }

        reqQtyVal = MyMath.roundUp(reqQtyVal);

        try {
            projPRQty = prcntg * reqQtyVal / 100;
        } catch (Exception e) {
            // TODO: Add catch code

        }


        //        System.out.println("Actual pecent phatan wari value --->"+actualPrcntVal);
        // System.out.println("Bom round up qty ---->"+MyMath.roundUp(reqQtyVal));
        try {
            r.setAttribute("NoOfGarment",
                           new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));

            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(projPRQty +
                                                        addiQtyVal));
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number((projPRQty + addiQtyVal) *
                                                        pricePerUnitVal));
            r.setAttribute("ProjPrQty",
                           new oracle.jbo.domain.Number(projPRQty));

        } catch (SQLException e) {
            ;
        }

        String itemId = r.getAttribute("InventoryItemId").toString();
        double actualPrQtySumVal =
            getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal * 100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        double pendingPrcnt =
            MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                         100));

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(lineTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(threadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(zipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otherTable);
    }

    public void wastageListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        double consQtyVal = getNumericVal(r.getAttribute("UsageMoUnit"));


        double wastageQtyVal = getNumericVal(valueChangeEvent.getNewValue());
        double orderQtyVal = getNumericVal(getSizesQty());
        double reqQtyVal = 0.0;
        String type = null;
        try {
            type = r.getAttribute("PurchaseType").toString();
        } catch (Exception e) {
            type = "Non-Generic";
        }
        if (type.equals("Generic")) {
            reqQtyVal = getReqQty();
        } else {
            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * consQtyVal) * (1 + (wastageQtyVal /
                                                                         100)));
        }
        System.out.println("orderQty= " + orderQtyVal);
        System.out.println("reqQty= " + reqQtyVal);

        double alocatReqValCAL =
            getNumericVal(r.getAttribute("AlocateIntrQty").toString());
        double alocatReqValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocatReqValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());
        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        double prcntg = getNumericVal(r.getAttribute("Perntg"));
        double projPRQty = 0.0;
        double unitConvRateVal = getNumericVal(r.getAttribute("UomConvRate"));
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        double actualPrcntVal = 0.0;


        if (unitConvRateVal > 0) {
            reqQtyVal = reqQtyVal / unitConvRateVal;
            System.out.println(reqQtyVal);
        }

        reqQtyVal = MyMath.roundUp(reqQtyVal);

        try {
            projPRQty = prcntg * reqQtyVal / 100;
        } catch (Exception e) {
            // TODO: Add catch code

        }


        //        System.out.println("Actual pecent phatan wari value --->"+actualPrcntVal);
        // System.out.println("Bom round up qty ---->"+MyMath.roundUp(reqQtyVal));
        try {
            r.setAttribute("NoOfGarment",
                           new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));

            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(projPRQty +
                                                        addiQtyVal));
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number((projPRQty + addiQtyVal) *
                                                        pricePerUnitVal));
            r.setAttribute("ProjPrQty",
                           new oracle.jbo.domain.Number(projPRQty));

        } catch (SQLException e) {
            ;
        }

        String itemId = r.getAttribute("InventoryItemId").toString();
        double actualPrQtySumVal =
            getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal * 100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        double pendingPrcnt =
            MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                         100));

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(lineTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(threadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(zipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otherTable);
    }


    public String callversion() {

        OperationBinding operationBinding = executeOperation("callPost");


        operationBinding.getParamsMap().put("hederId", getBomId().getValue());

        //invoke method
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            System.out.println("if errors-->");
            // List errors = operationBinding.getErrors();
        }
        //optional
        Object methodReturnValue = operationBinding.getResult();
        String message = "Record Successfully Posted";
        //                if (methodReturnValue != null) {
        //                    message = methodReturnValue.toString();
        //                } else {
        //                    message = "Failed! Check PR interface.";
        //                }
        showMessage(message);

        // Add event code here...
        return null;
    }

    public String generateversion() {
        callversion();
        return null;
    }

    public void setBomId(RichInputText bomId) {
        this.bomId = bomId;
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        HttpSession userSession = (HttpSession)ectx.getSession(false);
        userSession.setAttribute("bomIdS", bomId.getValue());
    }

    public RichInputText getBomId() {
        return bomId;
    }

    public String createPR() {

        OperationBinding operationBinding = executeOperation("createPR");
        operationBinding.getParamsMap().put("bomId",
                                            getBomId().getValue()); //bomLineId
        operationBinding.getParamsMap().put("bomLineId", null);
        //invoke method

        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            System.out.println("if errors-->");
            // List errors = operationBinding.getErrors();
        }


        //optional
        Object methodReturnValue = operationBinding.getResult();
        String message = null;
        if (methodReturnValue != null) {
            message = methodReturnValue.toString();
        } else {
            message = "Failed!";
        }
        System.out.println("Method return message ================>" +
                           message);
        showMessage(message);
        return null;
    }

    public String callForm() {
        String newPage =
            "http://192.168.200.106:7003/PurchaseMemo-ViewController-context-root/faces/SearchPG?headerId=" +
            getBomId().getValue();
        // String newPage = "http://localhost:7101/PurchaseMemo-ViewController-context-root/faces/SearchPG?headerId="+getBomId().getValue();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExtendedRenderKitService erks =
            Service.getService(ctx.getRenderKit(), ExtendedRenderKitService.class);
        String url = "window.open('" + newPage + "','_self');";
        erks.addScript(FacesContext.getCurrentInstance(), url);
        return null;
    }

    public String copyBOM() {

        OperationBinding operationBinding = executeOperation("CopyBOM");

        operationBinding.getParamsMap().put("bomId", getBomId().getValue());
        //invoke method
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            System.out.println("if errors-->");
            // List errors = operationBinding.getErrors();
        }
        //optional
        Object methodReturnValue = operationBinding.getResult();
        String message = null;
        if (methodReturnValue != null) {
            message =
                    " Record copied with BOM # " + methodReturnValue.toString();
        } else {
            message = "Failed!";
        }
        showMessage(message);
        return null;
    }

    //    public void linkAction(ActionEvent actionEvent) {
    //        // Add event code here...
    ////         FacesContext fctx = FacesContext.getCurrentInstance();
    ////         ExternalContext ectx = fctx.getExternalContext();
    ////         HttpSession userSession = (HttpSession) ectx.getSession(false);
    ////         userSession.setAttribute("style",getStyleName().getValue());
    //
    //    }

    public void setInvItemId(RichInputText invItemId) {
        this.invItemId = invItemId;
    }

    public RichInputText getInvItemId() {
        return invItemId;
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void editPopupFetchListener(PopupFetchEvent popupFetchEvent) {


        setWhereClause();

        if (popupFetchEvent.getLaunchSourceClientId().contains("cbInsert")) {

            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("CreateInsert");
            operationBinding.execute();
        }
    }

    public void editDialogListener(DialogEvent dialogEvent) {


        if (dialogEvent.getOutcome().name().equals("ok")) {
            callPopulate();
            resetLinesValues();

            // AdfFacesContext.getCurrentInstance().addPartialTarget(stnTable);

        }
    }

    public void editPopupCancelListener(PopupCanceledEvent popupCanceledEvent) {

    }

    public void setWhereClause() {


        OperationBinding operationBinding =
            executeOperation("setMultiSearchWherClause");
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            System.out.println("if errors-->");
            //            List errors = operationBinding.getErrors();
            //            System.out.println(@);
        }
    }

    public void callPopulate() {

        OperationBinding operationBinding =
            executeOperation("poplateMultiSearch");
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            System.out.println("if errors-->");
            // List errors = operationBinding.getErrors();
        }

    } //end of callPopulate method

    public void setObTab(RichTable obTab) {
        this.obTab = obTab;
    }

    public RichTable getObTab() {
        return obTab;
    }


    public int getSizesQty() {

        oracle.adf.view.rich.component.UIXTable table = getObTab();
        // Get the Selected Row key set iterator
        java.util.Iterator selectionIt = table.getSelectedRowKeys().iterator();
        int size = 0;
        int sizeTotal = 0;


        BindingContext bindingContext = BindingContext.getCurrent();
        DCDataControl dc =
            bindingContext.findDataControl("AppModuleDataControl"); //
        ApplicationModule am = dc.getApplicationModule();
        ViewObject findViewObject =
            am.findViewObject("CustMnjOntBomOblineDetailsView1");
        RowSetIterator it = findViewObject.createRowSetIterator("a");
        while (it.hasNext()) {
            Row r = it.next();
            try {
                size =
Integer.parseInt(r.getAttribute("SizeQuantity").toString());
            } catch (Exception e) {
                size = 0;
            }
            sizeTotal = sizeTotal + size;
            System.out.println(sizeTotal);
        }
        it.closeRowSetIterator();

        return sizeTotal;

    }


    //////////////////////////////////////New Material Code ///////////////////////////////


    public void editPopupFetchListenerFabric(PopupFetchEvent popupFetchEvent) {

        ViewObject popvo = am.getComponentLov1();
        popvo.setWhereClause("VALUE_DESCRIPTION IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit')");
        popvo.executeQuery();


        //              FilterableQueryDescriptor queryDescriptor =
        //                                                        (FilterableQueryDescriptor)getMatTabBean().getFilterModel();
        //                      if (queryDescriptor != null && queryDescriptor.getFilterCriteria() != null){
        //                        queryDescriptor.getFilterCriteria().clear();
        //                        getMatTabBean().queueEvent(new QueryEvent(getMatTabBean(),queryDescriptor));
        //                      }
        //        System.out.println("Filter Table calling .....");

    }

    public void editPopupFetchListenerThread(PopupFetchEvent popupFetchEvent) {

        ViewObject popvo = am.getComponentLov1();
        popvo.setWhereClause("VALUE_DESCRIPTION IN  ('Thread') AND SEGMENT2 >= '00035177'");
        popvo.executeQuery();


    }

    public void editPopupFetchListenerZipper(PopupFetchEvent popupFetchEvent) {

        ViewObject popvo = am.getComponentLov1();
        popvo.setWhereClause("VALUE_DESCRIPTION IN  ('Zipper','Button','Rivet')");
        popvo.executeQuery();


    }

    public void editPopupFetchListenerOthers(PopupFetchEvent popupFetchEvent) {

        ViewObject popvo = am.getComponentLov1();
        popvo.setWhereClause("VALUE_DESCRIPTION NOT IN  ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit','Thread','Zipper','Button','Rivet')");
        popvo.executeQuery();

    }

    public void editDialogListenerNew(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().name().equals("ok")) {
            System.out.println("checkingggggggggggggggggggg");
            FillMaterial();
            //  AdfFacesContext.getCurrentInstance().addPartialTarget(mytable);


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            ;
        }
    }

    public void editPopupCancelListenerNew(PopupCanceledEvent popupCanceledEvent) {

    }


    public void editDialogListenerPR(DialogEvent dialogEvent) {


        if (dialogEvent.getOutcome().name().equals("ok")) {

            createPR();
            //  AdfFacesContext.getCurrentInstance().addPartialTarget(mytable);


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            ;
        }
    }

    public void editPopupFetchLisDeliverDates(PopupFetchEvent popupFetchEvent) {


    }

    public void editDialogLisDeliveryDates(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().name().equals("ok")) {

        }
    }

    public void FillMaterial() {


        OperationBinding operationBinding = executeOperation("callMatFetch");
        operationBinding.execute();


    }

    public void setNewMatTabBEan(RichTable newMatTabBEan) {
        this.newMatTabBEan = newMatTabBEan;
    }

    public RichTable getNewMatTabBEan() {
        return newMatTabBEan;
    }

    public void selectAll(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

        BindingContext bindingContext = BindingContext.getCurrent();
        DCDataControl dc =
            bindingContext.findDataControl("AppModuleDataControl"); //
        ApplicationModule am = dc.getApplicationModule();

        ViewObject vo = am.findViewObject("popuplineVO1");

        boolean isSelected =
            ((Boolean)valueChangeEvent.getNewValue()).booleanValue();

        RowSetIterator it = vo.createRowSetIterator("a");


        int i = 0;
        Row row = null;
        it.reset();
        while (it.hasNext()) {
            if (i == 0)
                row = it.first();
            else
                row = it.next();

            //              row.getAttribute("Name"));
            if (isSelected) {
                System.out.println("Mutlti selecct true--->");
                row.setAttribute("Multiselect", true);


            } else {
                row.setAttribute("Multiselect", null);


            }
            i++;
        }
        it.closeRowSetIterator();

    }


    public void setPoQty(RichInputText poQty) {
        this.poQty = poQty;
    }

    public RichInputText getPoQty() {
        return poQty;
    }

    public void setMatTabBean(RichTable matTabBean) {
        this.matTabBean = matTabBean;
    }

    public RichTable getMatTabBean() {
        return matTabBean;
    }

    /*****Generic Method to Get BindingContainer**/
    public BindingContainer getBindingsCont() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    /**
     * Generic Method to execute operation
     * */
    public OperationBinding executeOperation(String operation) {
        OperationBinding createParam =
            getBindingsCont().getOperationBinding(operation);
        return createParam;

    }


    public void zipperFill(PopupFetchEvent popupFetchEvent) {


    }

    public void ZipperFill(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().name().equals("ok")) {

            /**********************************************/

            OperationBinding operationBinding = executeOperation("fillZipper");

            operationBinding.getParamsMap().put("bomId",
                                                getBomId().getValue());
            //invoke method
            operationBinding.execute();

            Object methodReturnValue = operationBinding.getResult();
            String message = null;
            if (methodReturnValue != null) {
                showMessage(methodReturnValue.toString());
            }


        }
    }

    public void setStnTable(RichTable stnTable) {
        this.stnTable = stnTable;
    }

    public RichTable getStnTable() {
        return stnTable;
    }

    public void setBookQty(RichInputText bookQty) {
        this.bookQty = bookQty;
    }

    public RichInputText getBookQty() {
        return bookQty;
    }


    public void setBpoNo(RichInputListOfValues bpoNo) {
        this.bpoNo = bpoNo;
    }

    public RichInputListOfValues getBpoNo() {
        return bpoNo;
    }


    public void FillThread(ActionEvent actionEvent) {
        // Add event code here...


        OperationBinding operationBinding = executeOperation("fillThread");

        operationBinding.getParamsMap().put("bpo", null);
        operationBinding.getParamsMap().put("style",
                                            getStyleName().getValue());
        //invoke method
        operationBinding.execute();

        Object methodReturnValue = operationBinding.getResult();
        String message = null;
        if (methodReturnValue != null) {
            showMessage(methodReturnValue.toString());
        }

    }


    public void displayError(ValueChangeEvent valueChangeEvent) {

        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage message =
            new FacesMessage("Allocate Qty can't be greater than 03 Status Qty");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(valueChangeEvent.getComponent().getClientId(context),
                           message);
    }

    public void merchAction(ValueChangeEvent valueChangeEvent) {
        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();
        // Add event code here...
        //  double mercQtyVal =  getNumericVal(getBookQty().getValue());
        double alocateQtyValCAL =
            getNumericVal(r.getAttribute("AllocateQty").toString());
        double alocateValCAL =
            getNumericVal(r.getAttribute("AllocateQty").toString());
        int flag = 0;
        if (alocateQtyValCAL > alocateValCAL) {

            r.setAttribute("AlocateIntrQty", new oracle.jbo.domain.Number(0));

            displayError(valueChangeEvent);
            flag++;
        }

        double alocateQtyValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocateValCGL =
            getNumericVal(r.getAttribute("AlocateCgl").toString());

        if (alocateQtyValCGL > alocateValCGL) {
            r.setAttribute("AlocateIntrCgl", new oracle.jbo.domain.Number(0));

            displayError(valueChangeEvent);
            flag++;
        }

        double alocateQtyValGFL =
            getNumericVal(r.getAttribute("AlocateGfl").toString());
        double alocateValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());

        if (alocateQtyValGFL > alocateValGFL) {
            r.setAttribute("AlocateInterGfl", new oracle.jbo.domain.Number(0));

            displayError(valueChangeEvent);
            flag++;
        }


        //        double prQtyVal = mercQtyVal - (alocateQtyValCAL+alocateQtyValCGL+alocateQtyValGFL);
        //
        //        try {
        //            if (flag==0)
        //            prQty.setValue(new oracle.jbo.domain.Number(prQtyVal));
        //        } catch (SQLException e) {
        //            ;
        //        }
        //        AdfFacesContext.getCurrentInstance().addPartialTarget(prQty);


    }

    public void showMessage(String message) {
        FacesMessage fm = new FacesMessage(message);
        fm.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, fm);
    }


    public void setStyleName(RichInputComboboxListOfValues styleName) {
        this.styleName = styleName;
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        HttpSession userSession = (HttpSession)ectx.getSession(false);
        userSession.setAttribute("style", styleName.getValue());

    }

    public RichInputComboboxListOfValues getStyleName() {
        return styleName;
    }

    public void SelectAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding = executeOperation("selectAllLines");
        operationBinding.getParamsMap().put("flag", "Y");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(stnTable);
    }

    public void DeSelectAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding = executeOperation("selectAllLines");
        operationBinding.getParamsMap().put("flag", "N");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(stnTable);
    }


    public void setBuyerId(RichInputText buyerId) {
        this.buyerId = buyerId;
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        HttpSession userSession = (HttpSession)ectx.getSession(false);
        userSession.setAttribute("buyerIdS", buyerId.getValue());

    }

    public RichInputText getBuyerId() {
        return buyerId;
    }


    public void selectBPOAll(ActionEvent actionEvent) {
        // Add event code here...
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "Y");
        operationBinding.getParamsMap().put("type", "BPO");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(bpOTable);
    }

    public void deSelectBPOAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "N");
        operationBinding.getParamsMap().put("type", "BPO");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(bpOTable);
    }

    public void selectCntryAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "Y");
        operationBinding.getParamsMap().put("type", "Country");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(countryTable);
    }

    public void deSelectCntryAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "N");
        operationBinding.getParamsMap().put("type", "Country");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(countryTable);
    }

    public void selectColorAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "Y");
        operationBinding.getParamsMap().put("type", "Color");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(colorTable);
    }

    public void deSelectColorAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "N");
        operationBinding.getParamsMap().put("type", "Color");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(colorTable);
    }

    public void selectSizeAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "Y");
        operationBinding.getParamsMap().put("type", "Size");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(sizeTable);
    }

    public void deSelectSizeAll(ActionEvent actionEvent) {
        // Add event code here...
        OperationBinding operationBinding =
            executeOperation("sizesCiritSelectDesel");
        operationBinding.getParamsMap().put("flag", "N");
        operationBinding.getParamsMap().put("type", "Size");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(sizeTable);
    }

    public void setBpOTable(RichTable bpOTable) {
        this.bpOTable = bpOTable;
    }

    public RichTable getBpOTable() {
        return bpOTable;
    }

    public void setCountryTable(RichTable countryTable) {
        this.countryTable = countryTable;
    }

    public RichTable getCountryTable() {
        return countryTable;
    }

    public void setColorTable(RichTable colorTable) {
        this.colorTable = colorTable;
    }

    public RichTable getColorTable() {
        return colorTable;
    }

    public void setSizeTable(RichTable sizeTable) {
        this.sizeTable = sizeTable;
    }

    public RichTable getSizeTable() {
        return sizeTable;
    }


    public void priceUnitAction(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        double pricePerUnitVal = getNumericVal(valueChangeEvent.getNewValue());
        //        double prQtyVal = getNumericVal(getPrQty().getValue());
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        System.out.println("additional=" +
                           getNumericVal(r.getAttribute("AdditionalQty")));
        double projPrQtyVal = getNumericVal(r.getAttribute("ProjPrQty"));
        double bomReqQtyVal = getNumericVal(r.getAttribute("NoOfGarment"));
        double prQtyVal = addiQtyVal + projPrQtyVal;
        System.out.println("prQtyVal=" + prQtyVal);
        try {
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number(prQtyVal * pricePerUnitVal));
            r.setAttribute("PrQty", new oracle.jbo.domain.Number(prQtyVal));
        } catch (SQLException e) {
        }

        double actualPrcntVal = 0.0;
        //        try {
        //            actualPrcntVal = (prQtyVal) / bomReqQtyVal * 100;
        //
        //        } catch (Exception e) {
        //            actualPrcntVal = 0.0;
        //        }
        //


        String itemId = r.getAttribute("InventoryItemId").toString();

        double actualPrQtySumVal =
            getLineQtySum("BookedQty", prQtyVal, itemId);
        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        System.out.println("Actual Qty sum-------------->" +
                           actualPrQtySumVal);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / bomReqQtyVal *
                                          100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        System.out.println("Actual PR qty sum in Bean ---------------->" +
                           actualPrcntVal);

        double pendingPrcnt = 100 - ((actualPrQtySumVal) / bomReqQtyVal * 100);

        // actualPrcntVal =getLineQtySum("ActualPrcnt",actualPrcntVal,itemId);

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(lineTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(threadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(zipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otherTable);

    }

    public void additionalListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        //        double prQtyVal = getNumericVal(getPrQty().getValue());
        double addiQtyVal = getNumericVal(valueChangeEvent.getNewValue());
        System.out.println("additional=" +
                           getNumericVal(valueChangeEvent.getNewValue()));
        double projPrQtyVal = getNumericVal(r.getAttribute("ProjPrQty"));
        double bomReqQtyVal = getNumericVal(r.getAttribute("NoOfGarment"));
        double prQtyVal = addiQtyVal + projPrQtyVal;
        System.out.println("prQtyVal=" + prQtyVal);
        try {
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number(prQtyVal * pricePerUnitVal));
            r.setAttribute("PrQty", new oracle.jbo.domain.Number(prQtyVal));
            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(prQtyVal));
        } catch (SQLException e) {
        }

        double actualPrcntVal = 0.0;
        //        try {
        //            actualPrcntVal = (prQtyVal) / bomReqQtyVal * 100;
        //
        //        } catch (Exception e) {
        //            actualPrcntVal = 0.0;
        //        }
        //


        String itemId = r.getAttribute("InventoryItemId").toString();

        double actualPrQtySumVal =
            getLineQtySum("BookedQty", prQtyVal, itemId);
        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        System.out.println("Actual Qty sum-------------->" +
                           actualPrQtySumVal);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / bomReqQtyVal *
                                          100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        System.out.println("Actual PR qty sum in Bean ---------------->" +
                           actualPrcntVal);

        double pendingPrcnt = 100 - ((actualPrQtySumVal) / bomReqQtyVal * 100);

        // actualPrcntVal =getLineQtySum("ActualPrcnt",actualPrcntVal,itemId);

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(lineTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(threadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(zipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otherTable);
    }

    public void priceCalc() {
        // Add event code here...
        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        //        double prQtyVal = getNumericVal(getPrQty().getValue());
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        System.out.println("additional=" +
                           getNumericVal(r.getAttribute("AdditionalQty")));
        double projPrQtyVal = getNumericVal(r.getAttribute("ProjPrQty"));
        double bomReqQtyVal = getNumericVal(r.getAttribute("NoOfGarment"));
        double prQtyVal = addiQtyVal + projPrQtyVal;
        System.out.println("prQtyVal=" + prQtyVal);
        try {
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number(prQtyVal * pricePerUnitVal));
            r.setAttribute("PrQty", new oracle.jbo.domain.Number(prQtyVal));
        } catch (SQLException e) {
        }

        double actualPrcntVal = 0.0;
        //        try {
        //            actualPrcntVal = (prQtyVal) / bomReqQtyVal * 100;
        //
        //        } catch (Exception e) {
        //            actualPrcntVal = 0.0;
        //        }
        //


        String itemId = r.getAttribute("InventoryItemId").toString();

        double actualPrQtySumVal =
            getLineQtySum("BookedQty", prQtyVal, itemId);
        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        System.out.println("Actual Qty sum-------------->" +
                           actualPrQtySumVal);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / bomReqQtyVal *
                                          100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        System.out.println("Actual PR qty sum in Bean ---------------->" +
                           actualPrcntVal);

        double pendingPrcnt = 100 - ((actualPrQtySumVal) / bomReqQtyVal * 100);

        // actualPrcntVal =getLineQtySum("ActualPrcnt",actualPrcntVal,itemId);

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));
    }


    public void resetLinesValues() {

        Row r = am.getCustMnjOntBomRmlineLinesView1().getCurrentRow();

        oracle.adf.view.rich.component.UIXTable table = getLineTable();
        // Get the Selected Row key set iterator
        java.util.Iterator selectionIt = table.getSelectedRowKeys().iterator();
        //        double cons = 0.0, wastage = 0.0, bomReqQty = 0.0, percnt =
        //            0.0, projPrQty = 0.0, actualPrQty, pricePerUnit = 0.0, total =
        //            0.0, reqQtyVal = 0.0, unitConvRateVal;

        /*************************Declare variables***************************/

        double cons = 0.0, actualPrcntVal = 0.0, wastageQtyVal =
            0.0, orderQtyVal = 0.0, reqQtyVal = 0.0, pricePerUnitVal =
            0.0, prcntg = 0., projPRQty = 0.00, unitConvRateVal =
            0.0, addiQtyVal = 0.0;
        String itemId = null;
        /**************************************************************/
        orderQtyVal = getNumericVal(getSizesQty());
        System.out.println("order quantity after refresh ----------->" +
                           orderQtyVal);
        while (selectionIt.hasNext()) {
            System.out.println("Seleect Row in Reset Lines Valuess------>>");
            Object rowKey = selectionIt.next();
            table.setRowKey(rowKey);
            int index = table.getRowIndex();
            FacesCtrlHierNodeBinding row =
                (FacesCtrlHierNodeBinding)table.getRowData(index);
            Row selectedRow = row.getRow();

            itemId =
                    MyMath.ChangetoString(selectedRow.getAttribute("InventoryItemId"));

            cons = MyMath.numeric3(selectedRow.getAttribute("UsageMoUnit"));
            wastageQtyVal =
                    MyMath.numeric3(selectedRow.getAttribute("ExtraUsage"));
            prcntg = MyMath.numeric3(selectedRow.getAttribute("Perntg"));
            pricePerUnitVal =
                    MyMath.numeric3(selectedRow.getAttribute("PricePerUnit"));
            unitConvRateVal =
                    MyMath.numeric3(selectedRow.getAttribute("UomConvRate"));

            addiQtyVal =
                    MyMath.numeric3(selectedRow.getAttribute("AdditionalQty"));

            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * cons) * (1 + (wastageQtyVal /
                                                                   100)));


            if (unitConvRateVal > 0) {
                reqQtyVal = reqQtyVal / unitConvRateVal;
            }

            reqQtyVal = MyMath.roundUp(reqQtyVal);

            try {
                projPRQty = prcntg * reqQtyVal / 100;
            } catch (Exception e) {
                System.out.println("Message 1---->" + e.getMessage());

            }


            //NoOfGarment
            //            selectedRow.setAttribute("NoOfGarment", reqQtyVal);
            //            selectedRow.setAttribute("BookedQty", projPRQty);
            //            selectedRow.setAttribute("ProjPrQty", projPRQty);
            //            selectedRow.setAttribute("Total", (projPRQty * pricePerUnitVal));

            double actualPrQtySumVal =
                getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);


            try {
                actualPrcntVal =
                        MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal *
                                              100);
            } catch (Exception e) {
                actualPrcntVal = 0.0;
                System.out.println("Message 2--->" + e.getMessage());
            }

            //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
            double pendingPrcnt =
                MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                             100));

            try {

                selectedRow.setAttribute("NoOfGarment",
                                         MyMath.roundUp(reqQtyVal));
                selectedRow.setAttribute("BookedQty",
                                         (projPRQty + addiQtyVal));
                selectedRow.setAttribute("ProjPrQty", projPRQty);
                selectedRow.setAttribute("ActualPrcnt", actualPrcntVal);
                selectedRow.setAttribute("PendingPrcnt", pendingPrcnt);
                selectedRow.setAttribute("AccumPrcnt", actualPrQtySumVal);
                selectedRow.setAttribute("Total",
                                         (projPRQty + addiQtyVal) * pricePerUnitVal);


            } catch (Exception e) {
                ;
            }

            try {
                r.setAttribute("NoOfGarment",
                               new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));
                r.setAttribute("PrQty",
                               new oracle.jbo.domain.Number(projPRQty +
                                                            addiQtyVal));
                r.setAttribute("Total",
                               new oracle.jbo.domain.Number((projPRQty +
                                                             addiQtyVal) *
                                                            pricePerUnitVal));
                r.setAttribute("ProjPrQty",
                               new oracle.jbo.domain.Number(projPRQty));
                r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
                r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
                r.setAttribute("AccumPrcnt",
                               MyMath.toNumber(actualPrQtySumVal));

            } catch (Exception e) {

                e.printStackTrace();
            }


        } //end of loop


    } //end of resetLinesValues


    public void setLineTable(RichTable lineTable) {
        this.lineTable = lineTable;
    }

    public RichTable getLineTable() {
        return lineTable;
    }


    public double getLineQtySum(String name, double curntVal, String itemId) {

        RowSetIterator it = lineVo.createRowSetIterator("qty");
        Row currentRow = lineVo.getCurrentRow();
        double total = curntVal;
        int inventoryItemId = Integer.parseInt(itemId);
        String itemIdCheck = null;
        while (it.hasNext()) {
            Row r = it.next();

            String itemid2 =
                r.getAttribute("InventoryItemId").toString(); //InventoryItemId
            // System.out.println(itemid2 + "item ids" + itemId);
            if (itemid2.equals(itemId)) {
                System.out.println("Booked qty count Bean----------------->" +
                                   MyMath.numeric3(r.getAttribute(name)));
                total = total + MyMath.numeric3(r.getAttribute(name));

            }
        }
        it.closeRowSetIterator();
        return total;

    } //end of method line

    public void addDeductAction(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        double actualQty = MyMath.numeric3(valueChangeEvent.getNewValue());
        double sizeWiseProjQty = MyMath.numeric(getSizeWiseProjQtyBind());
        addDeductQtyBind.setValue(MyMath.toNumber(actualQty -
                                                  sizeWiseProjQty));
        AdfFacesContext.getCurrentInstance().addPartialTarget(addDeductQtyBind);
    }


    public void setSizeActualQtyBind(RichInputText sizeActualQtyBind) {
        this.sizeActualQtyBind = sizeActualQtyBind;
    }

    public RichInputText getSizeActualQtyBind() {
        return sizeActualQtyBind;
    }

    public void setSizeWiseBomReqBind(RichInputText sizeWiseBomReqBind) {
        this.sizeWiseBomReqBind = sizeWiseBomReqBind;
    }

    public RichInputText getSizeWiseBomReqBind() {
        return sizeWiseBomReqBind;
    }

    //    public String setGetLineValues(String type, String name, double val) {
    //
    //        oracle.adf.view.rich.component.UIXTable table = getLineTable();
    //        // Get the Selected Row key set iterator
    //        java.util.Iterator selectionIt = table.getSelectedRowKeys().iterator();
    //        String value = null;
    //        while (selectionIt.hasNext()) {
    //            Object rowKey = selectionIt.next();
    //            table.setRowKey(rowKey);
    //            int index = table.getRowIndex();
    //            FacesCtrlHierNodeBinding row =
    //                (FacesCtrlHierNodeBinding)table.getRowData(index);
    //            Row selectedRow = row.getRow();
    //
    //            try {
    //                if (type.equals("GET"))
    //                    value = selectedRow.getAttribute(name).toString();
    //                else {
    //                    selectedRow.setAttribute(name, val);
    //                }
    //
    //            } catch (Exception e) {
    //                ;
    //            }
    //
    //        }
    //        return value;
    //    }

    public void setSizeWiseProjQtyBind(RichInputText sizeWiseProjQtyBind) {
        this.sizeWiseProjQtyBind = sizeWiseProjQtyBind;
    }

    public RichInputText getSizeWiseProjQtyBind() {
        return sizeWiseProjQtyBind;
    }

    public void setWhereClauseBPO() {

        /********************************/
        OperationBinding operationBinding =
            executeOperation("populateBPOLines1");
        operationBinding.execute();

    }


    public void editPopupFetchListener_BPO(PopupFetchEvent popupFetchEvent) {
        // Add event code here...
        setWhereClauseBPO();

    }

    public void editDialogListener_BPO(DialogEvent dialogEvent) {

        if (dialogEvent.getOutcome().name().equals("ok")) {
            //System.out.println("Before Fill BPO--->");
            /**************************************************/

            /**************************************************/

            FillBPO();

            AdfFacesContext.getCurrentInstance().addPartialTarget(assignBpo_BindTB);
        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            ;
        }
    }


    public void FillBPO() {
        //System.out.println("In Fill BPO--->");
        OperationBinding operationBinding = executeOperation("callBPOFetch");
        operationBinding.execute();
    }


    public void setAssignBpo_BindTB(RichTable assignBpo_BindTB) {
        this.assignBpo_BindTB = assignBpo_BindTB;
    }

    public RichTable getAssignBpo_BindTB() {
        return assignBpo_BindTB;
    }

    public void SelectAllBPO(ActionEvent actionEvent) {
        // Add event code here...
        // System.out.println("Level 1.1");

        OperationBinding operationBinding = executeOperation("selectAllBPO");
        operationBinding.getParamsMap().put("flag", "Y");
        operationBinding.execute();
        //      System.out.println("Level 1.2");

        AdfFacesContext.getCurrentInstance().addPartialTarget(fillBPO_BindTB);
    }

    public void DeSelectAllBPO(ActionEvent actionEvent) {
        // Add event code here...
        //       System.out.println("Level 1.1");

        OperationBinding operationBinding = executeOperation("selectAllBPO");
        operationBinding.getParamsMap().put("flag", "N");
        operationBinding.execute();
        //     System.out.println("Level 1.2");

        AdfFacesContext.getCurrentInstance().addPartialTarget(fillBPO_BindTB);
    }

    public void setFillBPO_BindTB(RichTable fillBPO_BindTB) {
        this.fillBPO_BindTB = fillBPO_BindTB;
    }

    public RichTable getFillBPO_BindTB() {
        return fillBPO_BindTB;
    }

    //////////////////modification/////added later


    public void setAddDeductQtyBind(RichInputText addDeductQtyBind) {
        this.addDeductQtyBind = addDeductQtyBind;
    }

    public RichInputText getAddDeductQtyBind() {
        return addDeductQtyBind;
    }

    public CellFormat getDataFormat(DataCellContext cxt) {
        CellFormat cellFormat = new CellFormat(null, null, null);
        QDR qdr = cxt.getQDR();
        //Obtain a reference to the product category column.
        Object total = qdr.getDimMember("BomRmlineId");

        if (total == null || total.toString().equals(" ") ||
            total.toString().equals("")) {
            cellFormat.setTextStyle("font-weight:bold");
            //            cellFormat.setStyle("background-color:#C0C0C0");
        }
        return cellFormat;
    }


    public CellFormat pivotHeaderFormat(HeaderCellContext cxt) {
        // Add event code here...
        if (cxt.getValue() != null) {
            return new CellFormat(null, "background-color:#EBF3FA",
                                  "font-weight:bold");
        }
        return null;
    }

    public void defaultCurrency(PhaseEvent phaseEvent) {
        // Add event code here...

        this.currency.setValue("USD");
    }

    public void setCurrency(RichInputComboboxListOfValues currency) {
        this.currency = currency;
    }

    public RichInputComboboxListOfValues getCurrency() {
        return currency;
    }


    public void fileUploaded(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        UploadedFile file = (UploadedFile)valueChangeEvent.getNewValue();
        try {
            clearSizeBreakDownTable();
            parseFile(file.getInputStream());
            AdfFacesContext.getCurrentInstance().addPartialTarget(sizeBreakdownTable);
            am.getDBTransaction().commit();
        } catch (IOException e) {
            // TODO
        }
    }

    public void clearSizeBreakDownTable() {

        am.getDBTransaction().commit();

        ViewObject vos = am.getSizeWiseDetailVO1();
        vos.setWhereClause("FLAG ='A'");
        vos.executeQuery();


        ViewObject vo = am.getSizeWiseDetailVO1();
        RowSetIterator it = vo.createRowSetIterator("aa");
        while (it.hasNext()) {
            it.next().remove();
        }
        vo.executeEmptyRowSet();
        it.closeRowSetIterator();
        //       am.getDBTransaction().

    }

    public void parseFile(java.io.InputStream file) {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(file));
        String strLine = "";
        StringTokenizer st = null;
        int lineNumber = 0, tokenNumber = 0;
        Row rw = null;

        ViewObject vo = am.getSizeWiseDetailVO1();

        //read comma separated file line by line
        try {
            while ((strLine = reader.readLine()) != null) {
                lineNumber++;
                // create a new row skip the header (header has linenumber 1)
                if (lineNumber > 1) {
                    rw = vo.createRow();
                    rw.setNewRowState(Row.STATUS_INITIALIZED);
                    vo.insertRow(rw);
                }

                //break comma separated line using ","
                st = new StringTokenizer(strLine, ",");

                double sizeProjQty = 0, sizeActualQty = 0, addDeductQty;

                while (st.hasMoreTokens()) {
                    //display csv values
                    tokenNumber++;

                    String theToken = st.nextToken();
                    System.out.println("Line # " + lineNumber + ", Token # " +
                                       tokenNumber + ", Token : " + theToken);

                    if (lineNumber > 1) {
                        // set Attribute Values
                        switch (tokenNumber) {
                        case 1:
                            rw.setAttribute("SizeVal", theToken);
                            break;
                        case 2:
                            rw.setAttribute("SizeWiseOrderQty", theToken);
                            break;
                        case 3:
                            rw.setAttribute("SizeWiseBomReqQty", theToken);
                            break;
                        case 4:
                            rw.setAttribute("BookingPercent", theToken);
                            break;
                        case 5:
                            rw.setAttribute("SizeWiseProjQty", theToken);
                            sizeProjQty = MyMath.numeric3(theToken);
                            System.out.println(sizeProjQty);

                        case 6:
                            rw.setAttribute("ActualQty", theToken);
                            sizeActualQty = MyMath.numeric3(theToken);
                            System.out.println(sizeActualQty);

                        case 7:
                            addDeductQty = sizeActualQty - sizeProjQty;
                            System.out.println(addDeductQty);
                            rw.setAttribute("AddDeduct", addDeductQty);

                        case 8:
                            rw.setAttribute("Flag", "A");

                        }
                    }
                }
                //reset token number
                tokenNumber = 0;
            }
        } catch (IOException e) {
            FacesContext fctx = FacesContext.getCurrentInstance();
            fctx.addMessage(sizeBreakdownTable.getClientId(fctx),
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                             "Content Error in Uploaded file",
                                             e.getMessage()));
        } catch (Exception e) {
            FacesContext fctx = FacesContext.getCurrentInstance();
            fctx.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data Error in Uploaded file",
                                             e.getMessage()));
        }
    }

    public void setSizeBreakdownTable(RichTable sizeBreakdownTable) {
        this.sizeBreakdownTable = sizeBreakdownTable;
    }

    public RichTable getSizeBreakdownTable() {
        return sizeBreakdownTable;
    }

    public void prPopupFetch(PopupFetchEvent popupFetchEvent) {
        // Add event code here...
        ViewObject po = am.getPOInformationVO1();
        ViewObject ho = am.getCustMnjOntBomHeaderView1();
        ViewObject ro = am.getCustMnjOntBomRmlineLinesView1();
        po.setWhereClause(null);
        po.setWhereClause("ITEM_ID = '" +
                          ro.getCurrentRow().getAttribute("InventoryItemId").toString() +
                          "' AND BOM_NO = '" +
                          ho.getCurrentRow().getAttribute("BomNumber").toString() +
                          "'");
        System.out.println("item_id" +
                           ho.getCurrentRow().getAttribute("BomNumber").toString());
        System.out.println("bop" +
                           ro.getCurrentRow().getAttribute("InventoryItemId").toString());
        po.executeQuery();
    }

    public void duplicateChecker(FacesContext facesContext,
                                 UIComponent uIComponent, Object object) {
        // Add event code here...
        if (object != null) {
            System.out.println("validation check");
            String orderType = object.toString();
            System.out.println(orderType);
            String bomid = "", styleName = "", styleNo = "", season =
                "", buyer = "";
            if (orderType.equals("Style Based")) {

                bomid =
                        am.getCustMnjOntBomHeaderView1().getCurrentRow().getAttribute("BomId").toString();

                try {
                    styleName =
                            am.getCustMnjOntBomHeaderView1().getCurrentRow().getAttribute("StyleNameNew").toString();
                } catch (Exception e) {
                    styleName = "";
                }
                try {
                    styleNo =
                            am.getCustMnjOntBomHeaderView1().getCurrentRow().getAttribute("StyleNoC").toString();
                } catch (Exception e) {
                    styleNo = "";
                }
                try {
                    season =
                            am.getCustMnjOntBomHeaderView1().getCurrentRow().getAttribute("SeasonC").toString();
                } catch (Exception e) {
                    season = "";
                }
                try {
                    buyer =
                            am.getCustMnjOntBomHeaderView1().getCurrentRow().getAttribute("BuyerId").toString();
                } catch (Exception e) {
                    buyer = "";
                }

                String query =
                    "select count(*) as isExist from CUST_MNJ_ONT_BOM where STYLE_NO_C =? and STYLE_NAME_NEW=? and SEASON_C=? and BUYER_ID=? and ORDER_TYPE like 'Style%Based%' and BOM_ID<>?";
                String msg =
                    "Duplicate Style Based Entry with same Buyer, Style and Season found !!!";
                ResultSet rs;
                try {
                    PreparedStatement createStatement =
                        am.getDBTransaction().createPreparedStatement(query,
                                                                      0);

                    createStatement.setString(1, styleNo);
                    createStatement.setString(2, styleName);
                    createStatement.setString(3, season);
                    createStatement.setString(4, buyer);
                    createStatement.setString(5, bomid);
                    rs = createStatement.executeQuery();
                    boolean check = false;
                    String result = null;
                    //rs = appM.getDBTransaction().createStatement(0).executeQuery(query);
                    if (rs.next()) {
                        result = rs.getObject(1).toString();
                        System.out.println("result = " + result);
                    }
                    if (result.equals("0")) {
                        check = true;
                    } else {
                        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                      msg,
                                                                      null));
                    }

                    rs.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    public double getReqQty() {
        ViewObject vo = am.getSizeWiseDetailVO1();
        RowSetIterator iter = vo.createRowSetIterator(null);
        double qty = 0.0;
        double temp = 0.0;
        while (iter.hasNext()) {
            Row row = iter.next();
            try {
                temp =
Double.parseDouble(row.getAttribute("SizeWiseBomReqQty").toString());
            } catch (Exception e) {
                temp = 0.0;
            }
            qty = qty + temp;
        }
        System.out.println("------" + qty);
        iter.reset();
        iter.closeRowSetIterator();
        return qty;
    }

    public void saveAll(ActionEvent actionEvent) {
        // Add event code here...
        //refreshValues();

        am.getDBTransaction().commit();
    }

    public void setPurchaseType(RichInputComboboxListOfValues purchaseType) {
        this.purchaseType = purchaseType;
    }

    public RichInputComboboxListOfValues getPurchaseType() {
        return purchaseType;
    }

    public void threadOpenListener(DisclosureEvent disclosureEvent) {
        // Add event code here...
        ViewObject prvo = am.getCustMnjOntBomRmlineLinesView1();
        prvo.setWhereClause("ITEM_PREFIX IN ('Thread')");
        prvo.executeQuery();
    }

    public void fabricOpenListener(DisclosureEvent disclosureEvent) {
        // Add event code here...

        ViewObject prvo = am.getCustMnjOntBomRmlineLinesView1();
        prvo.setWhereClause("ITEM_PREFIX IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit')");
        prvo.executeQuery();

    }

    public void zipperOpenListener(DisclosureEvent disclosureEvent) {
        // Add event code here...
        ViewObject prvo = am.getCustMnjOntBomRmlineLinesView1();
        prvo.setWhereClause("ITEM_PREFIX IN ('Zipper','Button','Rivet')");
        prvo.executeQuery();
    }

    public void othersOpenListener(DisclosureEvent disclosureEvent) {
        // Add event code here...
        ViewObject prvo = am.getCustMnjOntBomRmlineLinesView1();
        prvo.setWhereClause("ITEM_PREFIX NOT IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit','Thread','Zipper','Button','Rivet')");
        prvo.executeQuery();

    }


    public void materialSheetOpenListener(DisclosureEvent disclosureEvent) {
        // Add event code here...
        String status = null;
        String doc = null;
        ViewObject vo = am.getCustMnjOntBomHeaderView1();
        try {
            doc = vo.getCurrentRow().getAttribute("BomNumber").toString();
        } catch (Exception e) {
            // TODO: Add catch code
            ;
        }
        System.out.println("------------material sheet-------------");

        String stmt = "BEGIN :1 :=  MNJ_BOM_MATERIAL_SHEET(:2); end;";
        java.sql.CallableStatement cs =
            am.getDBTransaction().createCallableStatement(stmt, 1);

        try {
            cs.registerOutParameter(1, OracleTypes.VARCHAR);
            cs.setString(2, doc);

            cs.execute();
            status = cs.getString(1);
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewObject mvo = am.getMaterialSheetVO1();
        mvo.executeQuery();
    }

    public void setThreadTable(RichTable threadTable) {
        this.threadTable = threadTable;
    }

    public RichTable getThreadTable() {
        return threadTable;
    }

    public void setZipperTable(RichTable zipperTable) {
        this.zipperTable = zipperTable;
    }

    public RichTable getZipperTable() {
        return zipperTable;
    }

    public void setOtherTable(RichTable otherTable) {
        this.otherTable = otherTable;
    }

    public RichTable getOtherTable() {
        return otherTable;
    }

    public void doFilterMultiLineVO(ActionEvent actionEvent) {
        // Add event code here...
        System.out.println("test case 1");
        ViewObject vo = am.getmulti_Bom_Header_VO1();
        ViewObject vo2 = am.getCustMnjOntBomRmlineEO_autoView1();
        String buyer_id = null, org = null, season = null;
        try {
            buyer_id = vo.getCurrentRow().getAttribute("BuyerId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            org = vo.getCurrentRow().getAttribute("OrgId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            season = vo.getCurrentRow().getAttribute("SeasonC").toString();
        } catch (Exception e) {
            ;
        }


        vo2.setWhereClause(null);
        vo2.setWhereClause("BUYER_ID = '" + buyer_id + "' AND SEASON_C = '" +
                           season + "'AND ORG_ID = '" + org +
                           "'AND ITEM_PREFIX IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit')");
        vo2.executeQuery();
        
        //filterLineVo();
        //  AdfFacesContext.getCurrentInstance().addPartialTarget(multieditFillFab);
        //  vo2.setWhereClause("ITEM_PREFIX IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit')");
        //  vo2.executeQuery();

    }

    public void filterLineVo(DisclosureEvent disclosureEvent) {
        // Add event code here...
        System.out.println("test case 2");
        //        ViewObject vo2=am.getCustMnjOntBomRmlineEO_autoView1();
        //        vo2.setWhereClause("ITEM_PREFIX IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit')");
        //        vo2.executeQuery();
        System.out.println("test case 1");
        ViewObject vo = am.getmulti_Bom_Header_VO1();
        ViewObject vo2 = am.getCustMnjOntBomRmlineEO_autoView1();
        String buyer_id = null, org = null, season = null;
        try {
            buyer_id = vo.getCurrentRow().getAttribute("BuyerId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            org = vo.getCurrentRow().getAttribute("OrgId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            season = vo.getCurrentRow().getAttribute("SeasonC").toString();
        } catch (Exception e) {
            ;
        }


        vo2.setWhereClause(null);
        vo2.setWhereClause("BUYER_ID = '" + buyer_id + "' AND SEASON_C = '" +
                           season + "'AND ORG_ID = '" + org +
                           "'AND ITEM_PREFIX IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit')");
        vo2.executeQuery();
        System.out.println("Multi Fabric Query: " + '\n' + vo2.getQuery());

    }

    public void setMultieditFillFab(RichShowDetailItem multieditFillFab) {
        this.multieditFillFab = multieditFillFab;
    }

    public RichShowDetailItem getMultieditFillFab() {
        return multieditFillFab;
    }

    public void dofilterMultiThread(DisclosureEvent disclosureEvent) {
        // Add event code here...
        System.out.println("test case 3");
        // ViewObject vo2=am.getCustMnjOntBomRmlineEO_autoView1();
        // vo2.setWhereClause("ITEM_PREFIX IN ('Thread')");
        //  vo2.executeQuery();
        System.out.println("test case 1");
        ViewObject vo = am.getmulti_Bom_Header_VO1();
        ViewObject vo2 = am.getCustMnjOntBomRmlineEO_autoView1();
        String buyer_id = null, org = null, season = null;
        try {
            buyer_id = vo.getCurrentRow().getAttribute("BuyerId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            org = vo.getCurrentRow().getAttribute("OrgId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            season = vo.getCurrentRow().getAttribute("SeasonC").toString();
        } catch (Exception e) {
            ;
        }


        vo2.setWhereClause(null);
        vo2.setWhereClause("BUYER_ID = '" + buyer_id + "' AND SEASON_C = '" +
                           season + "'AND ORG_ID = '" + org +
                           "'AND ITEM_PREFIX IN ('Thread')");
        System.out.println("Multi Thread Query: " + '\n' + vo2.getQuery());
        vo2.executeQuery();
    }

    public void componentListenerForMultiBom(DialogEvent dialogEvent) {
        // Add event code here...

        if (dialogEvent.getOutcome().name().equals("ok")) {
            //OperationBinding operationBinding = executeOperation("popSTN"); // this will be executed later
            // department selection (Printing,Embriodery)

            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            //getFilterPopUp().show(hints); // assign department pop up will appear
            getHeaderVO().show(hints);
            //operationBinding.execute();


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            BindingContainer bindings = getBindings();

        }

    }

    public void fetchFabricForMultiBOM(PopupFetchEvent popupFetchEvent) {
        // Add event code here...
        try {
            ViewObject popvo = am.getComponentLov1();
            popvo.setWhereClause("VALUE_DESCRIPTION IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit')");
            popvo.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void setFilterPopUp(RichPopup filterPopUp) {
        this.filterPopUp = filterPopUp;
    }

    public RichPopup getFilterPopUp() {
        return filterPopUp;
    }

    public void filterViewDialog(DialogEvent dialogEvent) {
        // Add event code here...

        if (dialogEvent.getOutcome().name().equals("ok")) {
            //OperationBinding operationBinding = executeOperation("popSTN"); // this will be executed later
            // department selection (Printing,Embriodery)
            // searchHeaderVo();


            System.out.println("step---------------------------1");
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            getHeaderVO().show(hints); // assign department pop up will appear
            //operationBinding.execute();

            //  AdfFacesContext.getCurrentInstance().addPartialTarget(stnTable);
            //  AdfFacesContext.getCurrentInstance().addPartialTarget(detailTable);


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            BindingContainer bindings = getBindings();

        }
    }

    public void setHeaderVO(RichPopup headerVO) {
        this.headerVO = headerVO;
    }

    public RichPopup getHeaderVO() {
        return headerVO;
    }

    private void searchHeaderVo() {
        ViewObject ovj = am.getheaderSearchVO1();
        ViewObject searchVO = am.getfilterVO1();
        String buyer, season, unit, Org, buyerId;
        String filter = null;
        StringBuilder setVal = null;
        setVal = new StringBuilder();
        String[] array = new String[5];
        int i = 0;

        try {
            buyer = searchVO.getCurrentRow().getAttribute("Buyer").toString();
            //  String buyerwhrcls="BUYER =byer;
            array[i] = "BUYER = '" + buyer + "'";
            i++;
            //                 setVal.append(String.format("BUYER = '"+buyer+"'"));
            //                 filter=setVal.toString();
            //                 setVal=null;
        } catch (Exception e) {
            buyer = null;
        }

        try {
            season =
                    searchVO.getCurrentRow().getAttribute("Season").toString();
            array[i] = "SEASON_C = '" + season + "'";
            i++;
        } catch (Exception e) {
            season = null;
        }

        try {
            unit = searchVO.getCurrentRow().getAttribute("Unit").toString();
            array[i] = "UNIT_NAME = '" + unit + "'";
            i++;
        } catch (Exception e) {
            unit = null;
        }
        try {
            Org = searchVO.getCurrentRow().getAttribute("OrgId").toString();
            array[i] = "ORG_ID= '" + Org + "'";
            i++;
        } catch (Exception e) {
            Org = null;
        }
        try {
            buyerId =
                    searchVO.getCurrentRow().getAttribute("BuyerId").toString();
            array[i] = "BUYER_ID= '" + buyerId + "'";
            i++;
        } catch (Exception e) {
            buyerId = null;
        }


        filter = setVal.toString();
        // System.out.println(filter);
        ovj.setWhereClause(filter);
        ovj.executeQuery();


    }

    public void BOMheaderDialogListener(DialogEvent dialogEvent) {
        System.out.println("step---------------------------2");
        if (dialogEvent.getOutcome().name().equals("ok")) {

            System.out.println("step---------------------------3");

            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            getSupplierpopup().show(hints);

            // callfiltering();


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            BindingContainer bindings = getBindings();

        }


    }

    private void callfiltering() {
        ViewObject vo = am.getComponentLov1();

        RowSetIterator it = vo.createRowSetIterator("kk");
        String flag = "true";

        System.out.println("step---------------------------4");
        while (it.hasNext()) {
            Row r = it.next();
            try {
                flag = r.getAttribute("selectAttr").toString();
                System.out.println("selectAttr -->" + flag);
                if (flag != null && flag.equals("true")) {
                    System.out.println("step---------------------------5");
                    populatefromheader(r);
                }
            } catch (Exception e) {
                ;
            }
            //populateLines(r);

        }
        //doFilterAfterPopulated();
        it.closeRowSetIterator();

    }


    private void populatefromheader(Row rr) {
        ViewObject vo = am.getheaderSearchVO1();

        RowSetIterator it = vo.createRowSetIterator("kk");
        String flag = "true";
        System.out.println("step---------------------------6");

        while (it.hasNext()) {
            Row r = it.next();
            try {
                flag = r.getAttribute("SelectBom").toString();
                System.out.println("SelectBom -->" + flag);
                if (flag != null && flag.equals("true")) {

                    createline(getPopulateValue(r, "BomId"), rr,
                               getPopulateValue(r, "StyleNameC"),
                               getPopulateValue(r, "OrgId"));
                    System.out.println(" i am ready to create!!");
                }
            } catch (Exception e) {
                ;
            }
            //populateLines(r);

        }

        it.closeRowSetIterator();

    }


    public String getPopulateValue(Row r, String columnName) {

        String value = null;
        try {
            value = r.getAttribute(columnName).toString();
        } catch (Exception e) {
            ;
        }
        return value;
    }

    private void createline(String BomId, Row poprow, String style,
                            String org) throws SQLException {
        System.out.println("step---------------------------7");

        double cons = geConsVal(style);
        String item = getPopulateValue(poprow, "InventoryItemId");
        if (checkOrgAssign(poprow, org) == 0) {
            System.out.println(" inside org assign item check ---not exists");
            FacesMessage Message =
                new FacesMessage("Org is not assigned in the following item with Item ID : " +
                                 item + " !!");
            Message.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, Message);
        } else {

            System.out.println(" inside  duplicate item check ---exists");
            if (checkDuplicate(poprow, BomId) == true) {
                System.out.println(" inside duplicate check ---exists");
                FacesMessage Message =
                    new FacesMessage("This item is already tagged here !!");
                Message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, Message);
            } else {

                ViewObject v = am.getpriceSupplierProdAreaLOV1();
                String price = null, supplier = null, SupplierCode =
                    null, prod = null;
                try {
                    price =
                            v.getCurrentRow().getAttribute("UnitPrice").toString();
                } catch (Exception e) {
                    ;
                }
                try {
                    supplier =
                            v.getCurrentRow().getAttribute("Supplier").toString();
                } catch (Exception e) {
                    ;
                }
                try {
                    SupplierCode =
                            v.getCurrentRow().getAttribute("SupplierCode").toString();
                } catch (Exception e) {
                    ;
                }
                try {
                    prod =
v.getCurrentRow().getAttribute("ProdArea").toString();
                } catch (Exception e) {
                    ;
                }
                System.out.println("prod area=" + prod);

                System.out.println(" inside duplicate check ---Notexists");
                Row linerow = createMaterialLines(BomId);
                System.out.println("BOM ID=" + BomId);
                String seg1 = getPopulateValue(poprow, "Segment1");
                System.out.println("seg1" + seg1);
                String seg2 = getPopulateValue(poprow, "Segment2");
                System.out.println("seg2" + seg2);
                String seg4 = getPopulateValue(poprow, "Segment4");
                System.out.println("seg4" + seg4);

                if (getPopulateValue(poprow,
                                     "ValueDescription").equalsIgnoreCase("Thread")) {

                    linerow.setAttribute("UsageMoUnit", cons); //
                    System.out.println("Level 2   3");


                } //
                System.out.println("Level......");
                linerow.setAttribute("InventoryItemId",
                                     getPopulateValue(poprow,
                                                      "InventoryItemId"));
                System.out.println("inventory item id " +
                                   getPopulateValue(poprow,
                                                    "InventoryItemId"));
                linerow.setAttribute("ItemCode",
                                     getPopulateValue(poprow, "OrderedItem"));
                System.out.println("item code " +
                                   getPopulateValue(poprow, "OrderedItem"));
                linerow.setAttribute("ItemPrefix",
                                     getPopulateValue(poprow, "ValueDescription"));
                System.out.println("item prefix " +
                                   getPopulateValue(poprow, "ValueDescription"));
                linerow.setAttribute("MeasureUnitId",
                                     getPopulateValue(poprow, "PrimaryUnitOfMeasure"));
                System.out.println("measuredUnit " +
                                   getPopulateValue(poprow, "PrimaryUnitOfMeasure"));
                //linerow.setAttribute("ItemDesc", getPopulateValue(poprow, "Description"));
                linerow.setAttribute("LegacyCode",
                                     getPopulateValue(poprow, "LegacyCode")); //OnhandQty


                linerow.setAttribute("PricePerUnit", price);
                linerow.setAttribute("VendorId", SupplierCode);
                linerow.setAttribute("ProductionArea", prod);
                linerow.setAttribute("SupplierName", supplier);

                /**********************Onhand Qty & Alocate Qty**************************/
                linerow.setAttribute("OnhandQty1",
                                     getPopulateValue(poprow, "OnhandQty"));
                double val1 = get03StatusQty(seg1, seg2, seg4, 354);
                linerow.setAttribute("AllocateQty1", val1);

                linerow.setAttribute("OnhandCgl",
                                     getPopulateValue(poprow, "OnhandQtyCgl"));

                double val2 = get03StatusQty(seg1, seg2, seg4, 355);
                linerow.setAttribute("AlocateCgl", val2);

                linerow.setAttribute("OnhahndGfl",
                                     getPopulateValue(poprow, "OnhandQtyGfl"));

                double val3 = get03StatusQty(seg1, seg2, seg4, 356);
                linerow.setAttribute("AlocateGfl", val3);
                linerow.setAttribute("LeftOverQty", (val1 + val2 + val3));


                String prefix = getPopulateValue(poprow, "ValueDescription");
                if (prefix.equalsIgnoreCase("Thread")) {
                    linerow.setAttribute("UomConvRate",
                                         getPopulateValue(poprow,
                                                          "Threadlength")); //UomConvRate
                }
                System.out.println("Level 2   4");

                //  populateOrderLines(bomId);
                System.out.println("Level 2   5");
                String BomRmlineId =
                    linerow.getAttribute("BomRmlineId").toString();
                System.out.println("bomrmlineId " + BomRmlineId);

                //        if (!(prefix.equalsIgnoreCase("Label") ||
                //              prefix.equalsIgnoreCase("Rivet"))){
                // autoPopulateSize(null,string,BomRmlineId);
                //            System.out.println("If condition true for other prefix-=========??"+prefix);

                //  }
            }


            am.getDBTransaction().commit();
            System.out.println("step---------------------------8");

        }
    }

    public double geConsVal(String style) {

        ViewObject consVo = am.getThreadConsVO1();
        consVo.setWhereClause("STYLE ='" + style + "'");
        consVo.executeQuery();
        consVo.first();
        double consval = 0.0;
        try {
            consval =
                    Double.parseDouble(consVo.getCurrentRow().getAttribute("Cons").toString());
        } catch (Exception e) {
            // TO DO: Add catch code
            ;
        }
        System.out.println("Level 3 cons------>" + consval);

        return consval;
    }


    private Row createMaterialLines(String BomId) {
        ViewObject vo = am.getCustMnjOntBomRmlineEO_autoView1();
        Row row = vo.createRow();
        System.out.println("row=====================" + row);
        vo.insertRow(row);
        row.setNewRowState(Row.STATUS_INITIALIZED);
        row.setAttribute("BomId", BomId);
        System.out.println("BomRmlineId=" + row.getAttribute("BomRmlineId"));
        System.out.println("BomId=" + row.getAttribute("BomId"));
        // String im="CodeMaster";
        // row.setAttribute("ItemCode",im);
        // System.out.println("BomId="+row.getAttribute("ItemCode"));
        return row;
    }

    public int checkOrgAssign(Row r, String orgId) throws SQLException {

        int count = 0;

        String org = orgId;

        String item = getPopulateValue(r, "InventoryItemId");


        if (org != null) {
            //System.out.println("Check for org assignment in if--- "+org+"---"+item);
            String query =
                "SELECT COUNT(*) ITEM FROM MTL_SYSTEM_ITEMS MSI,ORG_ORGANIZATION_DEFINITIONS OOD WHERE MSI.ORGANIZATION_ID = OOD.ORGANIZATION_ID " +
                "AND OOD.OPERATING_UNIT = ? AND MSI.INVENTORY_ITEM_ID = ?";
            ResultSet resultSet = null;
            PreparedStatement createStatement =
                am.getDBTransaction().createPreparedStatement(query, 0);
            //System.out.println("Check for org assignment in if 2");
            createStatement.setString(1, org);
            createStatement.setString(2, item);
            //System.out.println("Check for org assignment in if 3");
            resultSet = createStatement.executeQuery();
            //System.out.println("Check for org assignment in if 4");

            while (resultSet.next()) {
                count = Integer.parseInt(resultSet.getString("ITEM"));
                break;
            }
            System.out.println("Item exists--" + count);
            resultSet.close();
            createStatement.close();
        }
        return count;
    }

    public boolean checkDuplicate(Row row, String BomId) throws SQLException {

        ViewObject itemvo = am.getCustMnjOntBomRmlineEO_autoView1();
        RowSetIterator it = itemvo.createRowSetIterator(null);
        System.out.println("check dupilcate id=" + BomId);


        //        while (it.hasNext()) {
        //            Row r = it.next();
        //           //System.out.println(r.getAttribute("BomId").toString());
        //            if ((r.getAttribute("ItemDesc").toString().equals(row.getAttribute("Description").toString())) && (Integer.parseInt(r.getAttribute("BomId").toString())==Integer.parseInt(BomId)) ){
        //                System.out.println("check dulpicate="+r.getAttribute("BomId").toString());
        //                return true;
        //            }
        //
        //        }
        //        return false;
        int bomid = Integer.parseInt(BomId);
        String itemCode = row.getAttribute("OrderedItem").toString();
        System.out.println("itemCode=" + itemCode);

        if (checkQuery(bomid, itemCode) == 0) {
            return false;
        } else {
            return true;
        }

    }


    public double get03StatusQty(String seg1, String seg2, String seg4,
                                 int orgId) {

        String stmt =
            "BEGIN :1 := mnj_get03_status_itemQty(:2,:3,:4, :5); end;";
        java.sql.CallableStatement cs =
            am.getDBTransaction().createCallableStatement(stmt, 1);
        double qty = 0;

        try {
            cs.registerOutParameter(1, OracleTypes.NUMBER);
            cs.setString(2, seg1);
            cs.setString(3, seg2);
            cs.setString(4, seg4);
            cs.setInt(5, orgId);
            cs.execute();
            qty = cs.getDouble(1);
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("item Qty for get3 " + qty);

        return qty;

    }

    public void generalListenerForMultiBOM(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println("enter common listener");
        Row r = am.getCustMnjOntBomRmlineEO_autoView1().getCurrentRow();

        double consQtyVal = getNumericVal(valueChangeEvent.getNewValue());


        double wastageQtyVal = getNumericVal(r.getAttribute("ExtraUsage"));
        // double orderQtyVal = getNumericVal(getSizesQty());
        double orderQtyVal =
            getSizesQtyFromAssignBPO(r.getAttribute("BomId").toString());
        double reqQtyVal = 0.0;
        String type = null;
        try {
            type = r.getAttribute("PurchaseType").toString();
        } catch (Exception e) {
            type = "Non-Generic";
        }
        if (type.equals("Generic")) {
            // reqQtyVal = getReqQty();
        } else {
            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * consQtyVal) * (1 + (wastageQtyVal /
                                                                         100)));
        }
        System.out.println("orderQty= " + orderQtyVal);
        System.out.println("reqQty= " + reqQtyVal);

        double alocatReqValCAL =
            getNumericVal(r.getAttribute("AlocateIntrQty").toString());
        double alocatReqValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocatReqValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());
        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        double prcntg = getNumericVal(r.getAttribute("Perntg"));
        double projPRQty = 0.0;
        double unitConvRateVal = getNumericVal(r.getAttribute("UomConvRate"));
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        double actualPrcntVal = 0.0;


        if (unitConvRateVal > 0) {
            reqQtyVal = reqQtyVal / unitConvRateVal;
            System.out.println(reqQtyVal);
        }

        reqQtyVal = MyMath.roundUp(reqQtyVal);

        try {
            projPRQty = prcntg * reqQtyVal / 100;
        } catch (Exception e) {
            ; // TODO: Add catch code

        }


        System.out.println("Actual pecent phatan wari value --->" +
                           actualPrcntVal);
        System.out.println("Bom round up qty ---->" +
                           MyMath.roundUp(reqQtyVal));
        try {
            r.setAttribute("NoOfGarment",
                           new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));

            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(projPRQty +
                                                        addiQtyVal));
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number((projPRQty + addiQtyVal) *
                                                        pricePerUnitVal));
            r.setAttribute("ProjPrQty",
                           new oracle.jbo.domain.Number(projPRQty));

        } catch (SQLException e) {
            ;
        }

        String itemId = r.getAttribute("InventoryItemId").toString();
        double actualPrQtySumVal =
            getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal * 100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        double pendingPrcnt =
            MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                         100));

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(fillFabricTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillthreadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillZipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillOtherTable);
    }

    private double getSizesQtyFromAssignBPO(String bomId) {
        ViewObject assignBPO = am.getAssignBPOQtyForMultiBOM1();
        //   assignBPO.setRangeStart(100);


        assignBPO.setWhereClause("BOM_ID='" + bomId + "'");
        assignBPO.executeQuery();


        double qty = 0.0, total = 0.0;

        //           Row[] rr ;
        //           rr= assignBPO.getAllRowsInRange();
        //           double qty=0.0,total=0.0;
        //
        //          System.out.println("length of r="+rr.length);
        //            for (Row row : rr) {
        //                  try {
        //                     qty =  Double.parseDouble(row.getAttribute("OrderQty").toString());
        //                   } catch (Exception e) {
        //                     qty = 0;
        //                   }
        //                   total = total + qty;
        //
        //            }


        RowSetIterator it = assignBPO.createRowSetIterator("a");
        qty = 0.0;
        total = 0.0;

        while (it.hasNext()) {
            Row r = it.next();
            try {

                qty =
Double.parseDouble(r.getAttribute("OrderQty").toString());
                System.out.println("orderQty=" + qty);
            } catch (Exception e) {
                qty = 0;
            }
            total = total + qty;

        }
        it.closeRowSetIterator();
        System.out.println(total);
        return total;

    }


    public void setFillFabricTable(RichTable fillFabricTable) {
        this.fillFabricTable = fillFabricTable;
    }

    public RichTable getFillFabricTable() {
        return fillFabricTable;
    }

    public void wastageListenerForMultiBOM(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println("enter wastagelistener===================");
        Row r = am.getCustMnjOntBomRmlineEO_autoView1().getCurrentRow();

        double consQtyVal = getNumericVal(r.getAttribute("UsageMoUnit"));


        double wastageQtyVal = getNumericVal(valueChangeEvent.getNewValue());
        //double orderQtyVal = getNumericVal(getSizesQty());
        double orderQtyVal =
            getSizesQtyFromAssignBPO(r.getAttribute("BomId").toString());
        double reqQtyVal = 0.0;
        String type = null;
        try {
            type = r.getAttribute("PurchaseType").toString();
        } catch (Exception e) {
            type = "Non-Generic";
        }
        if (type.equals("Generic")) {
            //  reqQtyVal = getReqQty();
        } else {
            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * consQtyVal) * (1 + (wastageQtyVal /
                                                                         100)));
        }
        System.out.println("orderQty= " + orderQtyVal);
        System.out.println("reqQty= " + reqQtyVal);

        double alocatReqValCAL =
            getNumericVal(r.getAttribute("AlocateIntrQty").toString());
        double alocatReqValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocatReqValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());
        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        double prcntg = getNumericVal(r.getAttribute("Perntg"));
        double projPRQty = 0.0;
        double unitConvRateVal = getNumericVal(r.getAttribute("UomConvRate"));
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        double actualPrcntVal = 0.0;


        if (unitConvRateVal > 0) {
            reqQtyVal = reqQtyVal / unitConvRateVal;
            System.out.println(reqQtyVal);
        }

        reqQtyVal = MyMath.roundUp(reqQtyVal);

        try {
            projPRQty = prcntg * reqQtyVal / 100;
        } catch (Exception e) {
            // TODO: Add catch code

        }


        //        System.out.println("Actual pecent phatan wari value --->"+actualPrcntVal);
        // System.out.println("Bom round up qty ---->"+MyMath.roundUp(reqQtyVal));
        try {
            r.setAttribute("NoOfGarment",
                           new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));

            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(projPRQty +
                                                        addiQtyVal));
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number((projPRQty + addiQtyVal) *
                                                        pricePerUnitVal));
            r.setAttribute("ProjPrQty",
                           new oracle.jbo.domain.Number(projPRQty));

        } catch (SQLException e) {
            ;
        }

        String itemId = r.getAttribute("InventoryItemId").toString();
        double actualPrQtySumVal =
            getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal * 100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        double pendingPrcnt =
            MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                         100));

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(fillFabricTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillthreadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillZipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillOtherTable);
    }

    public void bookingListenerForMulltiBOM(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println("enter booking listener==============");
        Row r = am.getCustMnjOntBomRmlineEO_autoView1().getCurrentRow();

        double consQtyVal = getNumericVal(r.getAttribute("UsageMoUnit"));


        double wastageQtyVal = getNumericVal(r.getAttribute("ExtraUsage"));
        // double orderQtyVal = getNumericVal(getSizesQty());
        double orderQtyVal =
            getSizesQtyFromAssignBPO(r.getAttribute("BomId").toString());
        double reqQtyVal = 0.0;
        String type = null;
        try {
            type = r.getAttribute("PurchaseType").toString();
        } catch (Exception e) {
            type = "Non-Generic";
        }
        if (type.equals("Generic")) {
            //reqQtyVal = getReqQty();
        } else {
            reqQtyVal =
                    Math.ceil((double)(orderQtyVal * consQtyVal) * (1 + (wastageQtyVal /
                                                                         100)));
        }
        System.out.println("orderQty= " + orderQtyVal);
        System.out.println("reqQty= " + reqQtyVal);

        double alocatReqValCAL =
            getNumericVal(r.getAttribute("AlocateIntrQty").toString());
        double alocatReqValCGL =
            getNumericVal(r.getAttribute("AlocateIntrCgl").toString());
        double alocatReqValGFL =
            getNumericVal(r.getAttribute("AlocateInterGfl").toString());
        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        double prcntg = getNumericVal(valueChangeEvent.getNewValue());
        double projPRQty = 0.0;
        double unitConvRateVal = getNumericVal(r.getAttribute("UomConvRate"));
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        double actualPrcntVal = 0.0;


        if (unitConvRateVal > 0) {
            reqQtyVal = reqQtyVal / unitConvRateVal;
            System.out.println(reqQtyVal);
        }

        reqQtyVal = MyMath.roundUp(reqQtyVal);

        try {
            projPRQty = prcntg * reqQtyVal / 100;
        } catch (Exception e) {
            // TODO: Add catch code

        }


        //        System.out.println("Actual pecent phatan wari value --->"+actualPrcntVal);
        // System.out.println("Bom round up qty ---->"+MyMath.roundUp(reqQtyVal));
        try {
            r.setAttribute("NoOfGarment",
                           new oracle.jbo.domain.Number(MyMath.roundUp(reqQtyVal)));

            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(projPRQty +
                                                        addiQtyVal));
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number((projPRQty + addiQtyVal) *
                                                        pricePerUnitVal));
            r.setAttribute("ProjPrQty",
                           new oracle.jbo.domain.Number(projPRQty));

        } catch (SQLException e) {
            ;
        }

        String itemId = r.getAttribute("InventoryItemId").toString();
        double actualPrQtySumVal =
            getLineQtySum("BookedQty", (projPRQty + addiQtyVal), itemId);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / reqQtyVal * 100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        double pendingPrcnt =
            MyMath.changeToDouble(100 - ((actualPrQtySumVal) / reqQtyVal *
                                         100));

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(fillFabricTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillthreadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillZipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillOtherTable);

    }

    public void additionalListenerForMultiBOM(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println("enter additional listener=========");
        Row r = am.getCustMnjOntBomRmlineEO_autoView1().getCurrentRow();

        double pricePerUnitVal = getNumericVal(r.getAttribute("PricePerUnit"));
        //        double prQtyVal = getNumericVal(getPrQty().getValue());
        double addiQtyVal = getNumericVal(valueChangeEvent.getNewValue());
        System.out.println("additional=" +
                           getNumericVal(valueChangeEvent.getNewValue()));
        double projPrQtyVal = getNumericVal(r.getAttribute("ProjPrQty"));
        double bomReqQtyVal = getNumericVal(r.getAttribute("NoOfGarment"));
        double prQtyVal = addiQtyVal + projPrQtyVal;
        System.out.println("prQtyVal=" + prQtyVal);
        try {
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number(prQtyVal * pricePerUnitVal));
            r.setAttribute("PrQty", new oracle.jbo.domain.Number(prQtyVal));
            r.setAttribute("BookedQty",
                           new oracle.jbo.domain.Number(prQtyVal));
        } catch (SQLException e) {
        }

        double actualPrcntVal = 0.0;
        //        try {
        //            actualPrcntVal = (prQtyVal) / bomReqQtyVal * 100;
        //
        //        } catch (Exception e) {
        //            actualPrcntVal = 0.0;
        //        }
        //


        String itemId = r.getAttribute("InventoryItemId").toString();

        double actualPrQtySumVal =
            getLineQtySum("BookedQty", prQtyVal, itemId);
        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        System.out.println("Actual Qty sum-------------->" +
                           actualPrQtySumVal);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / bomReqQtyVal *
                                          100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        System.out.println("Actual PR qty sum in Bean ---------------->" +
                           actualPrcntVal);

        double pendingPrcnt = 100 - ((actualPrQtySumVal) / bomReqQtyVal * 100);

        // actualPrcntVal =getLineQtySum("ActualPrcnt",actualPrcntVal,itemId);

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(fillFabricTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillthreadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillZipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillOtherTable);


    }

    public void priceUnitActionForMultiBOM(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println("enter price unit listener=========");
        Row r = am.getCustMnjOntBomRmlineEO_autoView1().getCurrentRow();

        double pricePerUnitVal = getNumericVal(valueChangeEvent.getNewValue());
        //        double prQtyVal = getNumericVal(getPrQty().getValue());
        System.out.println("pricePerUnitVal=========" + pricePerUnitVal);
        double addiQtyVal = getNumericVal(r.getAttribute("AdditionalQty"));
        System.out.println("additional=" +
                           getNumericVal(r.getAttribute("AdditionalQty")));
        double projPrQtyVal = getNumericVal(r.getAttribute("ProjPrQty"));
        double bomReqQtyVal = getNumericVal(r.getAttribute("NoOfGarment"));
        double prQtyVal = addiQtyVal + projPrQtyVal;
        System.out.println("prQtyVal=" + prQtyVal);
        try {
            r.setAttribute("Total",
                           new oracle.jbo.domain.Number(prQtyVal * pricePerUnitVal));
            r.setAttribute("PrQty", new oracle.jbo.domain.Number(prQtyVal));
        } catch (SQLException e) {
        }
        // System.out.println("pricePerUnitVal========="+pricePerUnitVal);
        double actualPrcntVal = 0.0;
        //        try {
        //            actualPrcntVal = (prQtyVal) / bomReqQtyVal * 100;
        //
        //        } catch (Exception e) {
        //            actualPrcntVal = 0.0;
        //        }
        //


        String itemId = r.getAttribute("InventoryItemId").toString();

        double actualPrQtySumVal =
            getLineQtySum("BookedQty", prQtyVal, itemId);
        //Pending % =100-((actualPrQtySumVal)/reqQtyVal*100)
        System.out.println("Actual Qty sum-------------->" +
                           actualPrQtySumVal);

        try {
            actualPrcntVal =
                    MyMath.changeToDouble(actualPrQtySumVal / bomReqQtyVal *
                                          100);
        } catch (Exception e) {
            actualPrcntVal = 0.0;
        }

        System.out.println("Actual PR qty sum in Bean ---------------->" +
                           actualPrcntVal);

        double pendingPrcnt = 100 - ((actualPrQtySumVal) / bomReqQtyVal * 100);

        // actualPrcntVal =getLineQtySum("ActualPrcnt",actualPrcntVal,itemId);

        r.setAttribute("ActualPrcnt", MyMath.toNumber(actualPrcntVal));
        r.setAttribute("PendingPrcnt", MyMath.toNumber(pendingPrcnt));
        r.setAttribute("AccumPrcnt", MyMath.toNumber(actualPrQtySumVal));

        AdfFacesContext.getCurrentInstance().addPartialTarget(fillFabricTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillthreadTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillZipperTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(fillOtherTable);

    }

    public void doFilterZipper(DisclosureEvent disclosureEvent) {
        // Add event code here...
        System.out.println("test case 4");
        // ViewObject vo2=am.getCustMnjOntBomRmlineEO_autoView1();
        // vo2.setWhereClause("ITEM_PREFIX IN ('Thread')");
        //  vo2.executeQuery();
        System.out.println("test case 1");
        ViewObject vo = am.getmulti_Bom_Header_VO1();
        ViewObject vo2 = am.getCustMnjOntBomRmlineEO_autoView1();
        String buyer_id = null, org = null, season = null;
        try {
            buyer_id = vo.getCurrentRow().getAttribute("BuyerId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            org = vo.getCurrentRow().getAttribute("OrgId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            season = vo.getCurrentRow().getAttribute("SeasonC").toString();
        } catch (Exception e) {
            ;
        }


        vo2.setWhereClause(null);
        vo2.setWhereClause("BUYER_ID = '" + buyer_id + "' AND SEASON_C = '" +
                           season + "'AND ORG_ID = '" + org +
                           "'AND ITEM_PREFIX IN ('Zipper','Button','Rivet')");
        vo2.executeQuery();


    }

    public void DoFilterFillOthers(DisclosureEvent disclosureEvent) {
        System.out.println("test case 5");
        // ViewObject vo2=am.getCustMnjOntBomRmlineEO_autoView1();
        // vo2.setWhereClause("ITEM_PREFIX IN ('Thread')");
        //  vo2.executeQuery();
        System.out.println("test case 1");
        ViewObject vo = am.getmulti_Bom_Header_VO1();
        ViewObject vo2 = am.getCustMnjOntBomRmlineEO_autoView1();
        String buyer_id = null, org = null, season = null;
        try {
            buyer_id = vo.getCurrentRow().getAttribute("BuyerId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            org = vo.getCurrentRow().getAttribute("OrgId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            season = vo.getCurrentRow().getAttribute("SeasonC").toString();
        } catch (Exception e) {
            ;
        }


        vo2.setWhereClause(null);
        vo2.setWhereClause("BUYER_ID = '" + buyer_id + "' AND SEASON_C = '" +
                           season + "'AND ORG_ID = '" + org +
                           "'AND ITEM_PREFIX NOT IN ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit','Thread','Zipper','Button','Rivet')");
        vo2.executeQuery();


        // Add event code here...
    }

    public void ZipperFetchlistener(PopupFetchEvent popupFetchEvent) {
        // Add event code here...
        ViewObject popvo = am.getComponentLov1();
        popvo.setWhereClause("VALUE_DESCRIPTION IN  ('Zipper','Button','Rivet')");
        popvo.executeQuery();
    }

    public void ThreadListener(PopupFetchEvent popupFetchEvent) {
        // Add event code here...

        ViewObject popvo = am.getComponentLov1();
        popvo.setWhereClause("VALUE_DESCRIPTION IN  ('Thread') AND SEGMENT2 >= '00035177'");
        popvo.executeQuery();
    }

    public void othersListener(PopupFetchEvent popupFetchEvent) {
        // Add event code here...

        ViewObject popvo = am.getComponentLov1();
        popvo.setWhereClause("VALUE_DESCRIPTION NOT IN  ('Shell-Denim','Shell-Non Denim','Pocketing','Interlining','Interlinning','Knit','Thread','Zipper','Button','Rivet')");
        popvo.executeQuery();
    }

    public void threadDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        if (dialogEvent.getOutcome().name().equals("ok")) {
            //OperationBinding operationBinding = executeOperation("popSTN"); // this will be executed later
            // department selection (Printing,Embriodery)

            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            //getFilterPopUp().show(hints); // assign department pop up will appear
            getHeaderVO().show(hints);
            //operationBinding.execute();


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            BindingContainer bindings = getBindings();

        }
    }

    public void othersDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        if (dialogEvent.getOutcome().name().equals("ok")) {
            //OperationBinding operationBinding = executeOperation("popSTN"); // this will be executed later
            // department selection (Printing,Embriodery)

            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            //getFilterPopUp().show(hints); // assign department pop up will appear
            getHeaderVO().show(hints);
            //operationBinding.execute();


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            BindingContainer bindings = getBindings();

        }
    }

    public void zipperDialoglistener(DialogEvent dialogEvent) {
        // Add event code here...
        if (dialogEvent.getOutcome().name().equals("ok")) {
            //OperationBinding operationBinding = executeOperation("popSTN"); // this will be executed later
            // department selection (Printing,Embriodery)

            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            //getFilterPopUp().show(hints); // assign department pop up will appear
            getHeaderVO().show(hints);
            //operationBinding.execute();


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            BindingContainer bindings = getBindings();

        }
    }

    public void setMultieditFillThread(RichShowDetailItem multieditFillThread) {
        this.multieditFillThread = multieditFillThread;
    }

    public RichShowDetailItem getMultieditFillThread() {
        return multieditFillThread;
    }

    public void setMultieditFilZipper(RichShowDetailItem multieditFilZipper) {
        this.multieditFilZipper = multieditFilZipper;
    }

    public RichShowDetailItem getMultieditFilZipper() {
        return multieditFilZipper;
    }

    public void setMultieditFilOthers(RichShowDetailItem multieditFilOthers) {
        this.multieditFilOthers = multieditFilOthers;
    }

    public RichShowDetailItem getMultieditFilOthers() {
        return multieditFilOthers;
    }

    private int checkQuery(int BomId, String itemCode) throws SQLException {
        int count = 0;

        //System.out.println("Check for org assignment in if--- "+org+"---"+item);
        String query =
            "SELECT COUNT(*) ITEM FROM CUST_MNJ_ONT_BOM_RMLINE BomLine WHERE BomLine.BOM_ID =? AND BomLine.ITEM_CODE = ? and BomLine.ATTRIBUTE1 is null ";
        ResultSet resultSet = null;
        PreparedStatement createStatement =
            am.getDBTransaction().createPreparedStatement(query, 0);
        //System.out.println("Check for org assignment in if 2");
        createStatement.setInt(1, BomId);
        createStatement.setString(2, itemCode);
        //System.out.println("Check for org assignment in if 3");
        resultSet = createStatement.executeQuery();
        //System.out.println("Check for org assignment in if 4");

        while (resultSet.next()) {
            count = Integer.parseInt(resultSet.getString("ITEM"));
            break;
        }
        System.out.println("Item exists--" + count);
        resultSet.close();
        createStatement.close();
        System.out.println();
        return count;


    }

    public void doFilterHeaderInfo(PopupFetchEvent popupFetchEvent) {
        // Add event code here...

        System.out.println("test case 1");
        ViewObject vo = am.getmulti_Bom_Header_VO1();
        ViewObject vo2 = am.getheaderSearchVO1();
        String buyer_id = null, org = null, season = null;
        try {
            buyer_id = vo.getCurrentRow().getAttribute("BuyerId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            org = vo.getCurrentRow().getAttribute("OrgId").toString();
        } catch (Exception e) {
            ;
        }
        try {
            season = vo.getCurrentRow().getAttribute("SeasonC").toString();
        } catch (Exception e) {
            ;
        }


        vo2.setWhereClause(null);
        vo2.setWhereClause("BUYER_ID = '" + buyer_id + "' AND SEASON_C = '" +
                           season + "'AND ORG_ID = '" + org + "'");
        vo2.executeQuery();

    }
    
    public void doPR(ActionEvent actionEvent) {
        am.getDBTransaction().commit();
        ViewObject headerVO=am.getmulti_Bom_Header_VO1();
        
        String season=null;
        int BuyerId=0;
        
        try{
            BuyerId=Integer.parseInt(headerVO.getCurrentRow().getAttribute("BuyerId").toString());
            System.out.println("-----------------------------------> buyer"+ BuyerId);
        }catch(Exception e){
            ;
        }
        
        try{
           season=headerVO.getCurrentRow().getAttribute("SeasonC").toString();
            System.out.println("-----------------------------------> season"+ season);
        }catch(Exception e){
            ;
        }
        
        // Add event code here...
        ViewObject BOMLineVO=am.getCustMnjOntBomRmlineEO_autoView1();
        String statement=null;
        String value=null;
        /**
         * checkBoxExists(vo) --> method for checking any check box ticked or not
         * checkItemStatusMatches(vo) --> method for checking each tick marked item status matches or not for Generating PR
         */
        try {
            if (checkBoxExists(BOMLineVO) != null &&
                checkItemStatusMatches(BOMLineVO).equals("true")) {
                System.out.println("enter in create pr condition.....");
                statement =  "BEGIN  MNJIT_REQ_CREATE.create_requisition(:1,:2,:3); end;";
                       CallableStatement cs = am.getDBTransaction().createCallableStatement(statement, 1);
                       try {
                           
                           cs.registerOutParameter(1, OracleTypes.VARCHAR);
                           cs.setInt(2,BuyerId);
                           cs.setString(3,season);
                           cs.execute();
                           value = cs.getString(1);
                           cs.close();

                       } catch (Exception e) {
                           value = e.getMessage();
                           System.out.println("-----------------catch------------------>done");
                       }
            } else if (checkItemStatusMatches(BOMLineVO).equals("false")) {
                showMessageWithSeverity("Found Different Item Status While Generating PR! Please Select Only 'Local' or Only 'Import' Item For PR!",
                                        "warn");
            } else if (checkBoxExists(BOMLineVO) == null) {
                showMessageWithSeverity("No Items Selected! Please Select Any Item For Generating PR!",
                                        "warn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(value != null){
            FacesMessage fm = new FacesMessage(value);
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, fm);
        }
        
        BOMLineVO.executeQuery();
        
        System.out.println("----------------------------------->done");
    }

    /**
     *
     * @param vo
     * @return true/null
     */
    public String checkBoxExists(ViewObject vo) {
        String flag = "true";
        String checkBoxValue = null;
        try {
            Row[] rowsArray = vo.getAllRowsInRange();
            for (Row eachRow : rowsArray) {
                try {
                    checkBoxValue = eachRow.getAttribute("Flag").toString();
                } catch (Exception e) {
                    checkBoxValue = "N";
                }
                if (checkBoxValue.equals("Y")) {
                    System.out.println("checkbox flag: " +
                                       eachRow.getAttribute("Flag").toString());
                    return flag;

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param vo
     * @return true/false/null
     */
    public String checkItemStatusMatches(ViewObject vo) {
        String flag = "true";
        try {

            Row[] rowsArray = vo.getAllRowsInRange();
            String itemStatusArray[] = new String[rowsArray.length];
            int idx = 0;
            String checkBoxValue = null;
            //loop for populate item status array where check box == y
            for (Row eachRow : rowsArray) {
                try {
                    checkBoxValue = eachRow.getAttribute("Flag").toString();
                } catch (Exception e) {
                    checkBoxValue = "N";
                }
                if (checkBoxValue.equals("Y")) {
                    itemStatusArray[idx] =
                            eachRow.getAttribute("ItemStatus").toString();
                    idx++;
                }
            }
            //loop for checking item status value iterating through item status array
            for (int count = 0; count < itemStatusArray.length; count++) {
                if (itemStatusArray[count] != null) {
                    System.out.println("for index: " + count + ", value: " +
                                       itemStatusArray[count]);
                    if (count > 0) {
                        if (itemStatusArray[count -
                            1].toString().equals(itemStatusArray[count].toString())) {
                            System.out.println(itemStatusArray[count - 1] +
                                               ", " + itemStatusArray[count] +
                                               " equals");
                        } else {
                            System.out.println(itemStatusArray[count - 1] +
                                               ", " + itemStatusArray[count] +
                                               " not equals");
                            flag = "false";
                            return flag;
                        }
                    }
                }
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Method for showing faces message with diffrent severity
     *
     */
    public void showMessageWithSeverity(String message, String severity) {

        FacesMessage fm = new FacesMessage(message);

        if (severity.equals("info")) {
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
        } else if (severity.equals("warn")) {
            fm.setSeverity(FacesMessage.SEVERITY_WARN);
        } else if (severity.equals("error")) {
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        }

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(context.getViewRoot().getId(), fm);
    }

    public void pOFecthListener(PopupFetchEvent popupFetchEvent) {
        // Add event code here...

        ViewObject po = am.getPOInfoVO1();
        //ViewObject ho = am.getCustMnjOntBomHeaderView1();
        ViewObject ro = am.getCustMnjOntBomRmlineEO_autoView1();
        po.setWhereClause(null);
        po.setWhereClause("ITEM_ID = '" +
                          ro.getCurrentRow().getAttribute("InventoryItemId").toString() +
                          "' AND BOM_NO = '" +
                          ro.getCurrentRow().getAttribute("BomNumber").toString() +
                          "'");
        System.out.println("bpo" +
                           ro.getCurrentRow().getAttribute("BomNumber").toString());
        System.out.println("item_id" +
                           ro.getCurrentRow().getAttribute("InventoryItemId").toString());
        po.executeQuery();
    }

    public void createPODf(ActionEvent actionEvent) {
        // Add event code here...

        ViewObject poDffVo = am.getMnjBomPoDffVO1();
        //ViewObject headerVo = appM.getCustMnjOntBomHeaderView1();
        ViewObject autoVo = am.getCustMnjOntBomRmlineEO_autoView1();
        String orgId = autoVo.getCurrentRow().getAttribute("OrgId").toString();

        String currentBomId;

        currentBomId = autoVo.getCurrentRow().getAttribute("BomId").toString();
        System.out.println("===================  currentbomid  " +
                           currentBomId);
        Row row = poDffVo.createRow();
        row.setAttribute("BomId", currentBomId);
        row.setAttribute("OrgId", orgId);

    }

    public void SaveAndGeneratePO(ActionEvent actionEvent) {

        am.getDBTransaction().commit();


        ViewObject poDffVo = am.getMnjBomPoDffVO1();
        Row curentPoDffRow;
        try {
            curentPoDffRow = poDffVo.getCurrentRow();
        } catch (Exception e) {
            System.out.println(" exception in   curentPoDffRow = poDffVo.getCurrentRow(); ");
            return;
        }

        String orgId;
        String userId;
        String prNo;
        try {
            orgId = curentPoDffRow.getAttribute("OrgId").toString();
            userId = curentPoDffRow.getAttribute("UserId").toString();
            prNo = curentPoDffRow.getAttribute("PrNo").toString();
        } catch (Exception e) {
            System.out.println("exception in orgId = curentPoDffRow.getAttribute(\"OrgId\").toString()");
            return;
        }

        String stmt = "BEGIN APPS.CREATE_AUTO_PO(:1,:2,:3,:4,:5); end;";
        java.sql.CallableStatement cs =
            am.getDBTransaction().createCallableStatement(stmt, 1);

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


        // Add event code here...
    }

    public void setFillthreadTable(RichTable fillthreadTable) {
        this.fillthreadTable = fillthreadTable;
    }

    public RichTable getFillthreadTable() {
        return fillthreadTable;
    }

    public void setFillZipperTable(RichTable fillZipperTable) {
        this.fillZipperTable = fillZipperTable;
    }

    public RichTable getFillZipperTable() {
        return fillZipperTable;
    }

    public void setFillOtherTable(RichTable fillOtherTable) {
        this.fillOtherTable = fillOtherTable;
    }

    public RichTable getFillOtherTable() {
        return fillOtherTable;
    }

    public void setSupplierpopup(RichPopup supplierpopup) {
        this.supplierpopup = supplierpopup;
    }

    public RichPopup getSupplierpopup() {
        return supplierpopup;
    }

    public void supplierpopupDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        if (dialogEvent.getOutcome().name().equals("ok")) {

            System.out.println("call filter---------------------------s-1");

            // ViewObject v=am.getprice_supplierVO1();
            //  System.out.println("price="+v.getCurrentRow().getAttribute("UnitPrice").toString());
            // System.out.println("supplier="+v.getCurrentRow().getAttribute("Supplier").toString());
            //System.out.println("SupplierCode="+v.getCurrentRow().getAttribute("SupplierCode").toString());

            callfiltering();


        } else if (dialogEvent.getOutcome().name().equals("cancel")) {
            BindingContainer bindings = getBindings();

        }


    }

    public void sltAll(ActionEvent actionEvent) {
        // Add event code here...
        ViewObject vo = am.getCustMnjOntBomRmlineEO_autoView1();
        //populatevo.executeQuery();
        String flag = "Y";
        //Row[] r = populatevo.getAllRowsInRange();
        RowSetIterator it = vo.createRowSetIterator("yy");


        while (it.hasNext()) {
            Row row = it.next();

            row.setAttribute("Flag", flag);


        }
        it.closeRowSetIterator();


    }

    public void dsltAll(ActionEvent actionEvent) {
        // Add event code here...
        ViewObject vo = am.getCustMnjOntBomRmlineEO_autoView1();
        //populatevo.executeQuery();
        String flag = "N";
        //Row[] r = populatevo.getAllRowsInRange();
        RowSetIterator it = vo.createRowSetIterator("yy");


        while (it.hasNext()) {
            Row row = it.next();

            row.setAttribute("Flag", flag);


        }
        it.closeRowSetIterator();

    }

    public void doDeleteItem(ActionEvent actionEvent) {
        // Add event code here...
        ViewObject po = am.getPOInfoVO1();
        //ViewObject ho = am.getCustMnjOntBomHeaderView1();
        ViewObject ro = am.getCustMnjOntBomRmlineEO_autoView1();
        po.setWhereClause(null);
        po.setWhereClause("ITEM_ID = '" +
                          ro.getCurrentRow().getAttribute("InventoryItemId").toString() +
                          "' AND BOM_NO = '" +
                          ro.getCurrentRow().getAttribute("BomNumber").toString() +
                          "'");
        System.out.println("bpo" +
                           ro.getCurrentRow().getAttribute("BomNumber").toString());
        System.out.println("item_id" +
                           ro.getCurrentRow().getAttribute("InventoryItemId").toString());
        po.executeQuery();
        if (po.getRowCount() == 0) {
            System.out.println("no value exist!!!");
            ro.getCurrentRow().remove();
        } else {

            System.out.println(" item ---exists");
            FacesMessage Message =
                new FacesMessage("Can not be deleted !! This Item has PR !!");
            Message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, Message);
        }
    }
}


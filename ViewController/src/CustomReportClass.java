import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.text.DecimalFormat;

import java.util.Map;

import javax.faces.context.FacesContext;

import mnj.ont.model.services.AppModuleImpl;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;

import oracle.adf.share.ADFContext;

import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import org.python.parser.ast.Str;

public class CustomReportClass {
    public CustomReportClass() {
    }

    AppModuleImpl appM = getAppModuleImpl();
            
    private AppModuleImpl getAppModuleImpl() {
        DCBindingContainer bindingContainer =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        //BindingContext bindingContext = BindingContext.getCurrent();
        DCDataControl dc =
            bindingContainer.findDataControl("AppModuleDataControl"); // Name of application module in datacontrolBinding.cpx
        AppModuleImpl appM =(AppModuleImpl)dc.getDataProvider();
        return appM;
    }
    
    /////////////////////////////////////////////////////      
            //changeable    
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("BOM");
            HSSFRow excelrow = null;
            HSSFCell excelcell = null;
            
            HSSFCellStyle tableHeaderStyle = workbook.createCellStyle();   
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            HSSFCellStyle dataStyle = workbook.createCellStyle();
            HSSFCellStyle titleHeaderStyle = workbook.createCellStyle();
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            HSSFCellStyle boldStyle = workbook.createCellStyle();
            HSSFCellStyle italicStyle = workbook.createCellStyle();
    
            HSSFFont hSSFFont1 = workbook.createFont(); //table Header
            HSSFFont hSSFFont2 = workbook.createFont(); //column header
            HSSFFont hSSFFont3 = workbook.createFont(); //title header
            HSSFFont hSSFFont4 = workbook.createFont();
            HSSFFont style = workbook.createFont();//bold font
            
            String tableHeaderFont = "Times New Roman";
            String colHeaderFont = "Times New Roman";
            Short tableHeaderFontSize = 14;//14
            Short titleHeaderFontSize = 36;//34
            Short colHeaderFontSize = 11;
            Short tableHeaderFontColor = HSSFColor.DARK_BLUE.index;
            Short titleHeaderFontColor = HSSFColor.DARK_BLUE.index;
            Short colHeaderFontColor = HSSFColor.DARK_BLUE.index;
            
            Short tableHeaderBgColor = HSSFColor.GREY_25_PERCENT.index;
            byte r1 =(byte) 222, r2 =(byte) 232, r3 =(byte) 249;
            Short colHeaderBgColor = HSSFColor.SKY_BLUE.index;
            byte r4 =(byte) 204, r5 =(byte) 229, r6 =(byte) 255;
            Short titleHeaderBgColor = HSSFColor.GREY_25_PERCENT.index;
            byte r7 =(byte) 222, r8 =(byte) 232, r9 =(byte) 249;
            //Short dataBgColor = HSSFColor.LIGHT_TURQUOISE.index;
            //byte r10 =(byte) 232, r11 =(byte) 251, r12 =(byte) 255;
            
            /////////////////////////////////////////////
            //changeable
            ViewObject headerVO = appM.getFabricCustomReportVO1();
            String lineVO = "FabricCustomReportItemVO";
            //String detailsVO = "CustMnjOntBomObline";
            
            String headerHeader = "Header Info";
            String lineHeader = "Master Fabric Booking";
            //String detailsHeader = "OB Line Info";
            
            int headerStartIndex=1;
            int headerEndIndex=6;
            int lineStartIndex=1;
            int lineEndIndex=5;
    //            int detailStartIndex=0;
    //            int detailEndIndex=11;
            
            ///////////////////////////////////////////////
            
            //changeable
            int colStart = 0;
            int rowStart = 0;
            /////////////////////////////////////////////////////
            //Unchangeable
            int xlColLine = colStart;
            //int xlColDetail = 0;
            int xlLineRow = 0;
            //int xlDetailRow = 0;
            int done = 0;
            int xlRow = rowStart;
            int xlCol = colStart+5;
            HSSFPalette palette = workbook.getCustomPalette();

            /////////////////////////////////////////////////////


        public void generateReport(FacesContext facesContext,
                                  OutputStream outputStream) {
            // Add event code here...
            ///////////////////////////////////////////////////
            palette.setColorAtIndex(tableHeaderBgColor, r1, r2, r3);
            palette.setColorAtIndex(colHeaderBgColor, r4, r5, r6);
            palette.setColorAtIndex(titleHeaderBgColor, r7, r8, r9);
            //palette.setColorAtIndex(dataBgColor, r10, r11, r12);
            
            
            headerStyle.setAlignment(CellStyle.ALIGN_CENTER);//      edit
            headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            headerStyle.setFillForegroundColor(colHeaderBgColor);
            hSSFFont1.setFontName(colHeaderFont);
            hSSFFont1.setFontHeightInPoints(colHeaderFontSize);
            hSSFFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont1.setColor(colHeaderFontColor);
           
            headerStyle.setFont(hSSFFont1);
            //style.setBorderBottom((short) 6); // double line border
            headerStyle.setBorderTop((short) 1); // single lines border
            headerStyle.setBorderBottom((short) 1); 
            headerStyle.setBorderLeft((short) 1); 
            headerStyle.setBorderRight((short) 1);
            
            dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
            //dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            //dataStyle.setFillForegroundColor(dataBgColor);
            dataStyle.setBorderBottom((short) 1);
            dataStyle.setBorderLeft((short) 1);
            dataStyle.setBorderRight((short) 1);
            
            titleHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);//centre
            titleHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            titleHeaderStyle.setFillForegroundColor(titleHeaderBgColor); 
            hSSFFont3.setFontName(tableHeaderFont);
            hSSFFont3.setFontHeightInPoints(titleHeaderFontSize);
            hSSFFont3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont3.setColor(titleHeaderFontColor);
            titleHeaderStyle.setFont(hSSFFont3);

            
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            //titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            //dataStyle.setFillForegroundColor(dataBgColor);
            
            hSSFFont4.setFontName(tableHeaderFont);
            hSSFFont4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
            hSSFFont4.setFontHeightInPoints(colHeaderFontSize);
            boldStyle.setFont(hSSFFont4);
            
            tableHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
            hSSFFont2.setFontHeightInPoints(tableHeaderFontSize);
            hSSFFont2.setFontName(tableHeaderFont);
            hSSFFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            hSSFFont2.setColor(tableHeaderFontColor);
            tableHeaderStyle.setFont(hSSFFont2);
            tableHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            tableHeaderStyle.setFillForegroundColor(tableHeaderBgColor);
            
            
            italicStyle.setAlignment(CellStyle.ALIGN_LEFT); 
            style.setFontName(tableHeaderFont);
            style.setItalic(true);
            italicStyle.setFont(style);
            
          
            /**
             * method for generating rows in worksheet
             */
            
            createExcelRow();
            worksheet.setDisplayGridlines(false);
            //////////////////////////Title/////////////////////////////
            
            /**
             * title names of excel report
             */
            
            String unitName[]={"COLUMBIA APPARELS LTD.","COLUMBIA GARMENTS LTD.","GENESIS FASHIONS LTD.","COLUMBIA WASHING PLANT LTD.","GENESIS WASHING LTD.","MATSBRO HK LTD.","NORTHERN FLOUR MILLS LTD.","GENESIS DENIM LTD."};
            int orgA[]={340,341,342,343,344,345,346,443};
            
            ViewObject head1 = appM.getCustMnjOntBomHeaderView1();
            String shiptUnit = null;
                
            shiptUnit = head1.getCurrentRow().getAttribute("ShipmentUnit").toString(); 
            System.out.println("---------------------Shipment Unit --> "+shiptUnit);
            
            if(shiptUnit.contains("CAL")) {
                shiptUnit="COLUMBIA APPARELS LTD.";
            }
            
            
            else if(shiptUnit.contains("CGL")) {
               shiptUnit="COLUMBIA GARMENTS LTD.";
           }
            else if(shiptUnit.contains("GFL")) {
                shiptUnit="GENESIS FASHIONS LTD.";
            }
            else if(shiptUnit.contains("CWPL")) {
                shiptUnit="COLUMBIA WASHING PLANT LTD.";                  
            }
            else if(shiptUnit.contains("GWL")) {
                shiptUnit="GENESIS WASHING LTD.";
            }
            else if(shiptUnit.contains("MHK")) {
                shiptUnit="MATSBRO HK LTD.";
            }
            else if(shiptUnit.contains("NFML")) {
                shiptUnit="NORTHERN FLOUR MILLS LTD.";
            }
            else if(shiptUnit.contains("GDL")) {
                shiptUnit="GENESIS DENIM LTD.";
            }
            else {
                shiptUnit=null;
            }
            
            String unit=null;
            String hro = head1.getCurrentRow().getAttribute("OrgId").toString();
            System.out.println("---------------------Req. Org ID --> "+ hro);
            int org = Integer.valueOf(hro);
            for(int i=0; i < orgA.length; i++) {
                if(org == orgA[i]) {
                    unit = unitName[i];
                    break;
                } 
            }
            
          
            
            /**
             * main title header portion
             */
            
            int end=9;
           // this.titleHeader(end,xlRow,colStart+1,"GENESIS FASHION LTD.");
            this.titleHeader(end,xlRow,colStart+1,unit); 
            //worksheet.autoSizeColumn(colStart+1);
            xlRow+=2;
            rowStart+=2;
                       
          
            
            
            
            hSSFFont1.setFontName(tableHeaderFont);
            hSSFFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            headerStyle.setFont(hSSFFont1);
            
            /**
             * subTitle part of main title header
             */
            System.out.println("Before Entering if condition req. unit org ---> " + unit);
            
            
            
            /**
             * if/else portion
             */
            
            if (unit.equals("GENESIS DENIM LTD.") || unit.equals("GENESIS FASHIONS LTD.") || unit.equals("GENESIS WASHING LTD.")){
            
            System.out.println("After Entering if condition req. unit org ---> " + unit);
            
            this.subtitle(end,xlRow,colStart,"Factory: 126/1 Kadda Nandun, Kadda Bazar, Jaydevpur, Gazipur, Bangladesh Phone : +88-2-9264020/9264024");
            
            xlRow++;
            rowStart++;
           
            this.subtitle(end,xlRow,colStart,"Head Office: 17, Mohakhali C/A, Red Crescent Concord Tower (8th, 9th, 10th, 12th & 13th Floor), Dhaka");
            headerStyle.setFont(hSSFFont1);
            xlRow++;
            rowStart++;
            
            this.subtitle(end,xlRow,colStart,"Phone: +88-09610101010, Ext: , Cell: ");
            xlRow++;
            rowStart++;
           
            this.subtitle(end,xlRow,colStart,"Email: ");
            xlRow+=3;
            rowStart+=3;
            this.subtitleLeft(end,xlRow,colStart,"The following numbers must be appeared on all related correspondances, Shipping documents, Invoices & Bills");
            xlRow+=2;
            rowStart+=2;
            
            }
            
            else if (unit.equals("COLUMBIA GARMENTS LTD.")|| unit.equals("COLUMBIA WASHING PLANT LTD.")|| unit.equals("MATSBRO HK LTD.")){
                    System.out.println("else if Condition req. unit org ---> " + unit);
                    
                    this.subtitle(end,xlRow,colStart,"Factory: Vogra, Chandana, Gazipur Sadar, Gazipur, Bangladesh Phone : +88-2-9292792-94");
                    
                    xlRow++;
                    rowStart++;
                    
                    this.subtitle(end,xlRow,colStart,"Head Office: 17, Mohakhali C/A, Red Crescent Concord Tower (8th, 9th, 10th, 12th & 13th Floor), Dhaka");
                    headerStyle.setFont(hSSFFont1);
                    xlRow++;
                    rowStart++;
                    
                    this.subtitle(end,xlRow,colStart,"Phone: +88-09610101010, Ext: , Cell: ");
                    xlRow++;
                    rowStart++;
                    
                    this.subtitle(end,xlRow,colStart,"Email: ");
                    xlRow+=3;
                    rowStart+=3;
                    this.subtitleLeft(end,xlRow,colStart,"The following numbers must be appeared on all related correspondances, Shipping documents, Invoices & Bills");
                    xlRow+=2;
                    rowStart+=2;
                }
            
            else if (unit.equals("COLUMBIA APPARELS LTD.")|| unit.equals("NORTHERN FLOUR MILLS LTD.")){
                
                    System.out.println("else if req. unit org ---> " + unit);
                    
                    this.subtitle(end,xlRow,colStart,"Factory: 228/1 Tin Sarak, Laxmipur, Jaydevpur, Gazipur, Bangladesh Phone : +88-2-9261776");
                    
                    xlRow++;
                    rowStart++;
                    
                    this.subtitle(end,xlRow,colStart,"Head Office: 17, Mohakhali C/A, Red Crescent Concord Tower (8th, 9th, 10th, 12th & 13th Floor), Dhaka");
                    headerStyle.setFont(hSSFFont1);
                    xlRow++;
                    rowStart++;
                    
                    this.subtitle(end,xlRow,colStart,"Phone: +88-09610101010, Ext: , Cell: ");
                    xlRow++;
                    rowStart++;
                    
                    this.subtitle(end,xlRow,colStart,"Email: ");
                    xlRow+=3;
                    rowStart+=3;
                    this.subtitleLeft(end,xlRow,colStart,"The following numbers must be appeared on all related correspondances, Shipping documents, Invoices & Bills");
                    xlRow+=2;
                    rowStart+=2;
                } //ends subtitle portion
            
            
            
            Row[] r = headerVO.getAllRowsInRange();
            Row row = headerVO.getCurrentRow();
            int numOfAttr = row.getAttributeCount();
            
            /**
             * Getting data of Date and Buyer for excel report
             */
            
            
            ViewObject head = appM.getFabricCustomReportVO1();                           
            String date = head.getCurrentRow().getAttribute("BomDate").toString();
            String byer = head.getCurrentRow().getAttribute("Buyer").toString();
//                                    excelcell = worksheet.getRow(11).createCell(2);
//                                                  excelcell.setCellValue(date);

           
            /**
             * Insert Logo
             */
            excelcell = worksheet.getRow(0).createCell(0);
            excelcell.setCellValue("Insert Logo"); 
         
            int infoRow = xlRow+1; // infoRow= 11 number row in excel it is 12 actually
           // this.addInfoLeftBold(1,infoRow,colStart,"Date");
           excelcell = worksheet.getRow(infoRow).createCell(colStart);
           excelcell.setCellValue("Date");
           excelcell.setCellStyle(boldStyle);
            excelcell = worksheet.getRow(infoRow).createCell(colStart+1);
            excelcell.setCellValue(date);
            //excelcell.setCellStyle(italicStyle);
            infoRow++;
            //this.addInfoLeftBold(1,infoRow,colStart,"To");
            excelcell = worksheet.getRow(infoRow).createCell(colStart);
            excelcell.setCellValue("To");
            excelcell.setCellStyle(boldStyle);
            infoRow++;
            //this.addInfoLeftBold(1,infoRow,colStart,"ATTN");
            excelcell = worksheet.getRow(infoRow).createCell(colStart);
            excelcell.setCellValue("ATTN");
            excelcell.setCellStyle(boldStyle);
            infoRow++;
            //this.addInfoLeftBold(1,infoRow,colStart,"E-Mail");
            excelcell = worksheet.getRow(infoRow).createCell(colStart);
            excelcell.setCellValue("E-Mail");
            excelcell.setCellStyle(boldStyle);
            infoRow++;
            //this.addInfoLeftBold(1,infoRow,colStart,"CC");
            excelcell = worksheet.getRow(infoRow).createCell(colStart);
            excelcell.setCellValue("CC");
            excelcell.setCellStyle(boldStyle);
            infoRow++;
            //this.addInfoLeftBold(1,infoRow,colStart,"CC E-Mail");
           // worksheet.autoSizeColumn(colStart);  
           excelcell = worksheet.getRow(infoRow).createCell(colStart);
           excelcell.setCellValue("CC E-Mail");
           excelcell.setCellStyle(boldStyle);
            infoRow++; 
            System.out.println("Info Row ---> " + infoRow);
            //this.addInfoLeftBold(1,infoRow,colStart,"Booked By");
            excelcell = worksheet.getRow(infoRow).createCell(colStart);
            excelcell.setCellValue("Booked By");
            excelcell.setCellStyle(boldStyle);
            worksheet.autoSizeColumn(colStart); 
            
            Map sessionScope = ADFContext.getCurrent().getSessionScope(); 
            String user = (String)sessionScope.get("userId");
            //String user = "6489";
            System.out.println("UserID ---->  " + user);
            
            /**
             * Booked By info insert into excel
             */
            
            ViewObject UserInfo = appM.getUserInfoVO1();
            RowSetIterator it = UserInfo.createRowSetIterator("aa");
            while (it.hasNext()) {
                Row UserInfoRow = it.next(); //next element returns
                try {

                    
                    
                    if (UserInfoRow.getAttribute("UserId").equals(user)){ //TODO
                        String UserName = (String)UserInfoRow.getAttribute("UserName");
                        
                            //String Str = new String(UserName);

                                  System.out.print("Return Value Booked By : " );
                                  String ReplacedUserName = UserName.replace('_', ' ');
                                  System.out.println(ReplacedUserName);

                                  
                        excelcell = worksheet.getRow(infoRow).createCell(colStart + 1);
                        excelcell.setCellValue(ReplacedUserName);
                        //excelcell.setCellStyle(italicStyle);
                        }
                    
                    
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
            it.closeRowSetIterator();
            
            
            
            

//            excelcell = worksheet.getRow(infoRow).createCell(colStart+1);
//            excelcell.setCellValue("Bappa");
            //excelcell.setCellStyle(italicStyle);
            
            infoRow++; //row = 18
            
            
  
        //////////////////////////////////////////////////////////////////////
            //choose between these two
            //Row wise print
            //this.HeaderTableRowWise(row); 
            //or Column wise print
            this.HeaderTableColWise(row);
            
            RowSet lineRowSet = (RowSet)row.getAttribute(lineVO);
            int countLineRow = lineRowSet.getRowCount();
            
            System.out.println("Count Line row for fabric custom report ---> " + countLineRow);
            
            if(lineRowSet.hasNext()){
                Row lineRow = lineRowSet.next();
                lineRowSet.reset();
                //-colStart
                tableHeader(xlColLine+lineEndIndex-colStart,xlLineRow,xlColLine,lineHeader,true); //Master Fabric Booking
                xlLineRow++;
                printColNames(lineStartIndex,lineEndIndex,xlLineRow,xlColLine,lineRow,true);
            }
            int sl=1;
            while (lineRowSet.hasNext()) {  
                Row lineRow = lineRowSet.next();
                xlColLine = colStart;
                xlLineRow++;
                printData(lineStartIndex,lineEndIndex,xlLineRow,xlColLine,lineRow,true,sl);
                sl++;
            }
            
            ///////////////////////////add total//////////////////////////////////
            addColValues(lineRowSet,"BookingQty",0,xlLineRow);
            xlLineRow++;
            
            
            //////////////////////////additional info//////////////////////////////////////////////////////////////
            
            /////////////////////set sizes//////////////////////
            /**
             * Set Sizes
             */
            int messageCol=3;
            int locationCol=3;
            int detailMsgCol=7;
            ///////////////////////////////////////////////////
            
            /**
             * if-else for arrange PI block
             */
            
            if (unit.equals("GENESIS DENIM LTD.") || unit.equals("GENESIS FASHIONS LTD.") ){
            xlLineRow+=3; 
            this.subtitle(1,xlLineRow,colStart,"1");
            this.subtitleLeft(messageCol,xlLineRow,colStart+1,"Please arrange PI within 2 working days against ");
//            this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,"GENESIS FASHIONS LTD.");
            this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,unit);
            
            xlLineRow++;
            this.subtitleLeft(locationCol,xlLineRow,colStart+5,"126/1 Kadda Nandun, Kadda Bazar");
            xlLineRow++;
            this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Jaydevpur, Gazipur, Bangladesh");
            xlLineRow++;
            this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Phone : +88-2-9264020/9264024");
            xlLineRow++;
            }
            else if (unit.equals("COLUMBIA GARMENTS LTD.")|| unit.equals("COLUMBIA WASHING PLANT LTD.")|| unit.equals("MATSBRO HK LTD.")){
                xlLineRow+=3; 
                this.subtitle(1,xlLineRow,colStart,"1");
                this.subtitleLeft(messageCol,xlLineRow,colStart+1,"Please arrange PI within 2 working days against ");
                //            this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,"GENESIS FASHIONS LTD.");
                this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,unit);
                
                xlLineRow++;
                this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Vogra, Chandana, Gazipur Sadar");
                xlLineRow++;
                this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Gazipur, Bangladesh");
                xlLineRow++;
                this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Phone : +88-2-9292792-94");
                xlLineRow++;
            }
            
            else if (unit.equals("COLUMBIA APPARELS LTD.")|| unit.equals("NORTHERN FLOUR MILLS LTD.")){
                    xlLineRow+=3; 
                    this.subtitle(1,xlLineRow,colStart,"1");
                    this.subtitleLeft(messageCol,xlLineRow,colStart+1,"Please arrange PI within 2 working days against ");
                    //            this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,"GENESIS FASHIONS LTD.");
                    this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,unit);
                    
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"228/1 Tin Sarak, Laxmipura");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Jaydevpur, Gazipur, Bangladesh");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Phone : +88-2-9261776");
                    xlLineRow++;
                }
            //*********end of if/else for arrange PI block******
            
           
                //TODO
                
                /**
                 * if-else for shipment/delivery to block
                 */
            
            
            if (shiptUnit.equals("COLUMBIA APPARELS LTD.")|| shiptUnit.equals("NORTHERN FLOUR MILLS LTD.")){
                this.subtitle(1,xlLineRow,colStart,"2");
                this.subtitleLeft(messageCol,xlLineRow,colStart+1,"Also arrange shipment/delivery to ");
                // this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,"GENESIS DENIM LTD.");
                this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,shiptUnit);
                
                xlLineRow++;
                this.subtitleLeft(locationCol,xlLineRow,colStart+5,"228/1, Tin Sarak, Laxmipura");
                xlLineRow++;
                this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Jaydevpur,Gazipur, Bangladesh");
                xlLineRow++;
                this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Phone : +88-2-9261776");
                xlLineRow++;
                this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Contact : Mr. Bikash/Alamgir");
                xlLineRow=xlLineRow+2;
            
            }
            
            else if (shiptUnit.equals("COLUMBIA GARMENTS LTD.")|| shiptUnit.equals("COLUMBIA WASHING PLANT LTD.")|| shiptUnit.equals("MATSBRO HK LTD.")){
                    this.subtitle(1,xlLineRow,colStart,"2");
                    this.subtitleLeft(messageCol,xlLineRow,colStart+1,"Also arrange shipment/delivery to ");
                    // this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,"GENESIS DENIM LTD.");
                    this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,shiptUnit);
                    
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Vogra, Chandana, Gazipur Sadar");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Gazipur, Bangladesh");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Phone : +88-2-9292792-94");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Contact : Mr. Hayat");
                    xlLineRow=xlLineRow+2;
                }
            
            else if (shiptUnit.equals("GENESIS DENIM LTD.") || shiptUnit.equals("GENESIS FASHIONS LTD.") || shiptUnit.equals("GENESIS WASHING LTD.") ){
                    this.subtitle(1,xlLineRow,colStart,"2");
                    this.subtitleLeft(messageCol,xlLineRow,colStart+1,"Also arrange shipment/delivery to ");
                    // this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,"GENESIS DENIM LTD.");
                    this.addInfoLeftBold(locationCol,xlLineRow,colStart+5,shiptUnit);
                    
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"126/1 Kadda Nandun, Kadda Bazar");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Jaydevpur,Gazipur, Bangladesh");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Phone : +88-2-9264020/9264024");
                    xlLineRow++;
                    this.subtitleLeft(locationCol,xlLineRow,colStart+5,"Contact : Mr. Nazrul/Rakib");
                    xlLineRow=xlLineRow+2;
                }
            //*********end of if-else for shipment/delivery to block
            
            
            this.subtitle(1,xlLineRow,colStart,"3");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"You must submit the shrinkage and inspection report when bulk fabrics are ready to ship.");
            xlLineRow++;
            ///////////////////////////////////
           // Supplier should provide soft copy of packing list in excel format with relevant information at the time of delivery.                                            
           this.subtitle(1,xlLineRow,colStart,"4");
           this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"Supplier should provide soft copy of packing list in excel format with relevant information at the time of delivery.");
           xlLineRow++;

            this.subtitle(1,xlLineRow,colStart,"5");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"Goods should meet the quality standard, chemical restrictions and other requirements of "+byer+" in all respect.");
            worksheet.autoSizeColumn(colStart+1);
            xlLineRow++;
            ///////////////////////////////////
            this.subtitle(1,xlLineRow,colStart,"6");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"Payment mode will be LC at 120 days for imported and LC at 90 days for Local goods or as agreed.");
            xlLineRow++;
            ///////////////////////////////////
            this.subtitle(1,xlLineRow,colStart,"7");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"Buyer name, style name/number, buyer order no, M&J PO No, season should be mentioned in PI.");
            xlLineRow++;
            ///////////////////////////////////
            this.subtitle(1,xlLineRow,colStart,"8");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"HS code and Country of origin should be mentioned in PI.");
            xlLineRow++;
            ///////////////////////////////////
            this.subtitle(1,xlLineRow,colStart,"9");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"Supplier/Beneficiary Bank and Bank details must be present in PI.");
            xlLineRow++;
            ///////////////////////////////////
            this.subtitle(1,xlLineRow,colStart,"10");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"In case of import with CNF/CFR delivery term, Goods value and freight cost should be mentioned seperately in PI.");
            xlLineRow++;
            ///////////////////////////////////
            this.subtitle(1,xlLineRow,colStart,"11");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"Delivery date, Delivery Port, Delivery Mode should be mentioned in PI.");
            xlLineRow++;
            ///////////////////////////////////
            this.subtitle(1,xlLineRow,colStart,"12");
            this.subtitleLeft(detailMsgCol,xlLineRow,colStart+1,"In case of Local supplier VAT registration number should be mentioned in PI.");
            xlLineRow++;
            //////////////////////////////////
            xlLineRow+=3;
            this.addInfoLeftBold(2,xlLineRow,end-2,"Best Regards"); //TODO
            /**
             * Starts Best Regards user name insert into excel cell
             */
            
            ViewObject UserInfo_best_regards = appM.getUserInfoVO1();
            RowSetIterator it_best_regards = UserInfo_best_regards.createRowSetIterator("bb");
            while (it_best_regards.hasNext()) {
                Row UserInfoRow = it_best_regards.next(); //next element returns
                try {

                   
                    
                    if (UserInfoRow.getAttribute("UserId").equals(user)){ // from String user = (String)sessionScope.get("userId");
                        String UserName = (String)UserInfoRow.getAttribute("UserName");
                        
                            System.out.print("Return Value Best Regards : " );
                            String ReplacedUserName = UserName.replace('_', ' ');
                            System.out.println(ReplacedUserName);
                        
                        excelcell = worksheet.getRow(xlLineRow+1).createCell(end-2);
                        excelcell.setCellValue(ReplacedUserName);
                            CellRangeAddress cellRange = new CellRangeAddress(
                                    xlLineRow+1, //first row (0-based)
                                    xlLineRow+1, //last row  (0-based)
                                    end-2, //first column (0-based)
                                   // xlCol + end - 1 //last column  (0-based)
                                    end-1
                            );
                            
                            worksheet.addMergedRegion(cellRange);
                            //excelcell.setCellStyle(italicStyle);
                        }
                    
                    
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
            it_best_regards.closeRowSetIterator();
            /**
             * Ends Best Regards user name insert into excel cell
             */
            worksheet.autoSizeColumn(end-2); //*******************auto size
            
//            excelcell = worksheet.getRow(xlLineRow+1).createCell(end-2);
//            excelcell.setCellValue("Bappa");
//            excelcell.setCellStyle(italicStyle);
            xlLineRow++;
            
            ///////////////////////////////additional info end////////////////////////////////////////////////////
            
            rowStart=xlLineRow;
                try {
                    //worksheet.createFreezePane(0, 1, 0, 1);
                    workbook.write(outputStream);
                    outputStream.flush();
                  //  w.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            
        }
        
        private void tableHeader(int end, int xlRow, int xlCol,String headerText,boolean slPrint){
            excelrow = (HSSFRow)worksheet.getRow(xlRow);
            if(slPrint == true){
                end++;
            }
            excelcell = excelrow.createCell(xlCol);
            excelcell.setCellValue(headerText);
            CellRangeAddress cellRange = new CellRangeAddress(
                    xlRow, //first row (0-based)
                    xlRow, //last row  (0-based)
                    xlCol, //first column (0-based)
                   // xlCol + end - 1 //last column  (0-based)
                    end+2
            );
        
            worksheet.addMergedRegion(cellRange);
            excelcell.setCellStyle(tableHeaderStyle); //Master Fabric Booking
           // HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRange, worksheet, workbook);
            //HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRange,worksheet, workbook);
            //HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRange,worksheet, workbook);
           // HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRange,worksheet, workbook);
        }
        
        private void printColNames(int start, int end, int xlRow, int xlCol,Row row,boolean slPrint){  
//            excelrow = (HSSFRow)worksheet.getRow(xlRow);
//            if(slPrint == true){
//                excelcell = excelrow.createCell(xlCol);
//                excelcell.setCellValue("Sl.");
//                excelcell.setCellStyle(headerStyle);
//                xlCol++;
//            }
//            for (String colName : row.getAttributeNames()) {
//                if (row.getAttributeIndexOf(colName) >= start &&
//                    row.getAttributeIndexOf(colName) <= end) {
//                    
//                    StringBuilder sb = new StringBuilder(colName);
//                    StringBuilder sb1 = new StringBuilder();
//                    char index;
//                    for (int i = 0; i < sb.length(); i++) {
//                        index = sb.charAt(i);
//                        if (i != 0 && Character.isUpperCase(index)) {
//                            sb1.append(" ");
//                            sb1.append(index);
//                        } else {
//                            sb1.append(index);
//                        }
//                    }
//                    
//                    colName = sb1.toString();
//                        excelcell = excelrow.createCell(xlCol);
//                        excelcell.setCellValue(colName);
//                        
//                        excelcell.setCellStyle(headerStyle);
//                        worksheet.autoSizeColumn(xlCol);
//                    
//                    xlCol += 1;
//                }
//            }
//            this.xlColLine=xlCol;
            xlRow=xlRow+1;
            
            // excelrow = (HSSFRow)worksheet.getRow(xlRow);
             excelcell = worksheet.getRow(xlRow).createCell(xlCol);
                           excelcell.setCellValue("SL.");
                           excelcell.setCellStyle(headerStyle);
            xlCol=xlCol+1;
            excelcell = worksheet.getRow(xlRow).createCell(xlCol);
            excelcell.setCellValue("SUPPLIER CODE");
            excelcell.setCellStyle(headerStyle);
            
            xlCol=xlCol+1;
            excelcell = worksheet.getRow(xlRow).createCell(xlCol);
            excelcell.setCellValue("DETAIL");
            excelcell.setCellStyle(headerStyle);
            
            xlCol=xlCol+1;
            excelcell = worksheet.getRow(xlRow).createCell(xlCol);
            excelcell.setCellValue("COLOR");
            excelcell.setCellStyle(headerStyle);
            
            xlCol=xlCol+1;
            excelcell = worksheet.getRow(xlRow).createCell(xlCol);
            excelcell.setCellValue("Booking Qty");
            excelcell.setCellStyle(headerStyle);
            
           xlCol=xlCol+1;
            
            CellRangeAddress cellRange = new CellRangeAddress(
                    xlRow, //first row (0-based)
                    xlRow, //last row  (0-based)
                    xlCol, //first column (0-based)
                   // xlCol + end - 1 //last column  (0-based)
                    end+3
            );
            worksheet.addMergedRegion(cellRange);
            
            
            HSSFCellStyle style11 = workbook.createCellStyle(); 
            for(int i = xlCol; i < 9; i++)  { //delivery date top portion border col5 to col8 test added solved
            
            style11.setBorderTop(HSSFCellStyle.BORDER_THIN);  
            style11.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style11.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style11.setBorderLeft(HSSFCellStyle.BORDER_THIN);
           
           
                   
                      
            
            style11.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style11.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style11.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style11.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            
           
           excelcell = worksheet.getRow(xlRow).createCell(i);
            excelcell.setCellStyle(style11);
//            excelcell = worksheet.getRow(xlRow-1).createCell(i);
//            excelcell.setCellStyle(style11);
            }
            //worksheet.addMergedRegion(new CellRangeAddress( xlRow, xlRow, xlCol, end));
            excelcell = worksheet.getRow(xlRow).createCell(xlCol);
            excelcell.setCellValue("Delivery Date");
            excelcell.setCellStyle(headerStyle);
                        
        }
        
        private void printData(int start, int end, int xlRow, int xlCol,Row row,boolean slPrint,int sl){
           //excelrow = (HSSFRow)worksheet.getRow(xlRow);
//            if(slPrint == true){
//                excelcell = excelrow.createCell(xlCol);
//                excelcell.setCellValue(sl);
//                excelcell.setCellStyle(dataStyle);
//               xlCol++;
//           }
            
            ViewObject header = appM.getFabricCustomReportItemVO1();
            Row[] lineRows = header.getAllRowsInRange();
            int s1=1;
            int ros=21;
            double total = 0;
            int c=5;
           for(Row lineRow:lineRows){
               excelcell = worksheet.getRow(ros).createCell(0);
               excelcell.setCellValue(String.valueOf(s1));
               excelcell.setCellStyle(dataStyle);
               s1++;
               
               excelcell = worksheet.getRow(ros).createCell(3);
               excelcell.setCellValue(lineRow.getAttribute("Color").toString());
               excelcell.setCellStyle(dataStyle);
               worksheet.autoSizeColumn(3);
               
                excelcell=worksheet.getRow(ros).createCell(1);
               try{
                excelcell.setCellValue(lineRow.getAttribute("ItemCode").toString());
               excelcell.setCellStyle(dataStyle);
               worksheet.autoSizeColumn(1);
               }catch(Exception e) {
                   ;
               }
               excelcell=worksheet.getRow(ros).createCell(2);
               try{
               excelcell.setCellValue(lineRow.getAttribute("Composition").toString());
               excelcell.setCellStyle(dataStyle);
               worksheet.autoSizeColumn(2);
               }catch(Exception e) {
                  String s=null;
                   excelcell.setCellValue(s);
                   excelcell.setCellStyle(dataStyle);
                   worksheet.autoSizeColumn(4);
               }
               excelcell=worksheet.getRow(ros).createCell(4);
               String s=null;
               try{
                   s=lineRow.getAttribute("BookingQty").toString();
                   excelcell.setCellValue(s);
                   excelcell.setCellStyle(dataStyle);
                   worksheet.autoSizeColumn(4);
                   double d1=Double.valueOf(s);           
                   total= total+d1;
               }catch(Exception e) {
                 //String  s=null;
                   excelcell.setCellValue("0");
                   excelcell.setCellStyle(dataStyle);
                   worksheet.autoSizeColumn(4);
               }
               //Delivery Date column value insert starts
               excelcell=worksheet.getRow(ros).createCell(5);
               try{
                   CellRangeAddress cellRange = new CellRangeAddress(
                           ros, //first row (0-based) 21
                           ros, //last row  (0-based) 21
                           5, //first column (0-based) 5
                          // xlCol + end - 1 //last column  (0-based)
                           end+3 //TODO 
                   );
                   worksheet.addMergedRegion(cellRange);
                   excelcell.setCellStyle(dataStyle);
                   
               excelcell.setCellValue(lineRow.getAttribute("DeliveryDate").toString());
               excelcell.setCellStyle(dataStyle);
               worksheet.autoSizeColumn(5);
               }catch(Exception e) {
                  String s2=null;
                   excelcell.setCellValue(s2);
                   
                   excelcell.setCellStyle(dataStyle);
                   worksheet.autoSizeColumn(5);
               }
              
              
              //recent
               HSSFCellStyle style11 = workbook.createCellStyle();
               for(int i = 5; i < 9; i++)  { //Delivery date column5 to coumn8
               style11.setBorderBottom(HSSFCellStyle.BORDER_THIN);
               style11.setBorderRight(HSSFCellStyle.BORDER_THIN);    //test added solved
               style11.setBottomBorderColor(IndexedColors.BLACK.getIndex());
               style11.setRightBorderColor(IndexedColors.BLACK.getIndex());
                   
               //             excelcell = worksheet.getRow(xlRow).createCell(i);
               //             excelcell.setCellStyle(style11);
                excelcell = worksheet.getRow(ros).createCell(i);
                excelcell.setCellStyle(style11);
               }
//                excelcell = worksheet.getRow(xlRow-1).createCell(c);
//                excelcell.setCellStyle(style11);

               ros++;
              // c++;
               //xlRow++;
           }
            worksheet.addMergedRegion(new CellRangeAddress(ros,ros,0,3));
            HSSFCellStyle style11 = workbook.createCellStyle();
           
            for(int i = 0; i < 9; i++)  { //TODO
                style11.setBorderTop(HSSFCellStyle.BORDER_THIN); //delivery date top border line code; test added not solved
//                style11.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                
//                style11.setBorderRight(HSSFCellStyle.BORDER_THIN); //test added not solved
//                style11.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                
                style11.setTopBorderColor(IndexedColors.BLACK.getIndex()); //test added not solved
//                style11.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//                style11.setRightBorderColor(IndexedColors.BLACK.getIndex());
//               style11.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                excelcell = worksheet.getRow(ros+1).createCell(i); //Test added not solved ros+1 to ros
                excelcell.setCellStyle(style11);
               
            }
            
            for(int i=20;i<=ros;i++)  {
                HSSFCellStyle style12 = workbook.createCellStyle();
                style12.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //style12.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style12.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                excelcell = worksheet.getRow(i).createCell(9); //delivery date column border line
                excelcell.setCellStyle(style12);
               
            }
//            excelcell = worksheet.getRow(ros+1).createCell(6);
//            excelcell.setCellStyle(style11);
            
           
           
          
            //excelcell.setCellStyle(headerStyle);
            //excelcell = excelrow.createCell(a);
            excelcell = worksheet.getRow(ros).createCell(0);// edit..........12.03
            excelcell.setCellStyle(headerStyle);
            excelcell.setCellValue("Total ");
            excelcell.setCellStyle(headerStyle);
            
            excelcell=worksheet.getRow(ros).createCell(4);
            String f=String.format("%.0f", total);
            excelcell.setCellValue(f);
            excelcell.setCellStyle(headerStyle);
            worksheet.autoSizeColumn(4);
           
            CellRangeAddress cellRange = new CellRangeAddress(
                    ros, //first row (0-based)
                    ros, //last row  (0-based)
                    5, //first column (0-based)
                   // xlCol + end - 1 //last column  (0-based)
                    end+3 //TODO
            );
            worksheet.addMergedRegion(cellRange);
//            
//            for (String colName : row.getAttributeNames()) {
//                if (row.getAttributeIndexOf(colName) >= start &&
//                    row.getAttributeIndexOf(colName) <= end) {
//                    excelcell = excelrow.createCell(xlCol);
//                    if(row.getAttribute(colName)!=null)
//                        excelcell.setCellValue(row.getAttribute(colName).toString());
//                    excelcell.setCellStyle(dataStyle);
//                    worksheet.autoSizeColumn(xlCol);            
//                    xlCol += 1;
//                }
//            }
            
           this.xlColLine=xlCol;
        }
        
        private void createExcelRow(){
            for(int i = 0;i < 500;i++){
                excelrow = (HSSFRow)worksheet.createRow(i);
            }
        }
       
        private void HeaderTableRowWise(Row row){ 
            //Row wise print
            tableHeader(headerEndIndex,xlRow,xlCol,headerHeader,false);
            xlRow ++;
            printColNames(headerStartIndex,headerEndIndex,xlRow,xlCol,row,false);
            xlRow ++;   
            xlCol = colStart;
            printData(headerStartIndex,headerEndIndex,xlRow,xlCol,row,false,0);
            
            xlColLine = colStart;
            xlLineRow = xlRow + 5;
            //xlDetailRow = xlRow + 5;
        }
        
        private void HeaderTableColWise(Row row){ 
            
            //tableHeaderColWise(xlCol+1,xlRow,xlCol,headerHeader);
            xlRow ++;
            printColNamesColWise(headerStartIndex,headerEndIndex,xlRow,xlCol,row);
            xlCol ++;   
            xlRow = rowStart+1;
            printDataColWise(headerStartIndex,headerEndIndex,xlRow,xlCol,row);
            
            xlColLine = colStart;
            xlLineRow = xlLineRow + 2;
            //xlDetailRow = xlLineRow;
        }
        
        
        private void tableHeaderColWise(int end, int xlRow, int xlCol,String headerText){
            end = 1;
            excelrow = (HSSFRow)worksheet.getRow(xlRow);
            excelcell = excelrow.createCell(xlCol);
            excelcell.setCellValue(headerText);
            CellRangeAddress cellRange = new CellRangeAddress(
                    xlRow, //first row (0-based)
                    xlRow, //last row  (0-based)
                    xlCol, //first column (0-based)
                    xlCol + end  //last column  (0-based) //TODO
            );
            worksheet.addMergedRegion(cellRange);
            excelcell.setCellStyle(tableHeaderStyle);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRange, worksheet, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRange,worksheet, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRange,worksheet, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRange,worksheet, workbook);
        }
        
        private void printColNamesColWise(int start, int end, int xlRow, int xlCol,Row row){  
            
            ViewObject spo = appM.getMaterialSheetVO1();
            String spoNum=null;
            try{
                spoNum=spo.getCurrentRow().getAttribute("SpoNo").toString(); 
                System.out.println("---------------------unit"+spoNum);
            }
            catch(Exception e) {
                spoNum=null;
            }
            
            for (String colName : row.getAttributeNames()) {
                int index1=row.getAttributeIndexOf(colName);
                if (row.getAttributeIndexOf(colName) >= start &&
                    row.getAttributeIndexOf(colName) <= end) {
                    if(index1>1)
                    {
                    excelrow = (HSSFRow)worksheet.getRow(xlRow);
                    StringBuilder sb = new StringBuilder(colName);
                    StringBuilder sb1 = new StringBuilder();
                    char index;
                    for (int i = 0; i < sb.length(); i++) {
                        index = sb.charAt(i);
                        if (i != 0 && Character.isUpperCase(index)) {
                            sb1.append(" ");
                            sb1.append(index);
                        } else {
                            sb1.append(index);
                        }
                    }
                 
                    colName = sb1.toString();
                    excelcell = excelrow.createCell(xlCol);
                    excelcell.setCellValue(colName);
                    excelcell.setCellStyle(boldStyle);
                    worksheet.autoSizeColumn(xlCol);
                    //   worksheet.setColumnBreak(5);
                    xlRow += 1;
                }
                    } 
            } //ends for loop of print Column names
            excelcell = worksheet.getRow(xlRow).createCell(xlCol);
                        excelcell.setCellValue("MJ PO/SPO"); 
            excelcell.setCellStyle(boldStyle);
            worksheet.autoSizeColumn(xlCol);
            
            
            /**
             * Starts MJ PO/SPO Inserting In Excel Cell
             */
            
            //vo of query page vo
            ViewObject queryPageVo = appM.getCustMnjOntBomHeaderView1();
            
            Row HeaderRow = queryPageVo.getCurrentRow();
            String HeaderRowStyleNo = HeaderRow.getAttribute("StyleNoC").toString();
            String HeaderRowBOMNo = HeaderRow.getAttribute("BomNumber").toString();
            System.out.println("Curret Header Row StyleNo. --> " + HeaderRowStyleNo);
            System.out.println("Curret Header Row BOMNo. --> " + HeaderRowBOMNo);
            
            
            System.out.println("@Entering SPO Insert In Excel Block!");
            ViewObject SPOAndBOMVo = appM.getSPOAndBOMVO1();
            RowSetIterator it_SPOAndBOMVo = SPOAndBOMVo.createRowSetIterator("xyz");
            int RowCount = 1;
            while(it_SPOAndBOMVo.hasNext()){
                    Row SPOAndBOMVoRow = it_SPOAndBOMVo.next();
                    System.out.println("SPOAndBOMVo Row No. --->" + RowCount);
                    
                try{
                        if (SPOAndBOMVoRow.getAttribute("BomNo").equals(HeaderRowBOMNo)){ //TODO
                                               System.out.println("@In If Condition!");
                                               String PONumber = (String)SPOAndBOMVoRow.getAttribute("PoNo");
                                               System.out.println("PONUMBER----> " + PONumber);
                                               excelcell = worksheet.getRow(xlRow).createCell(xlCol+1);
                                               excelcell.setCellValue(PONumber);
                                               //excelcell.setCellStyle(italicStyle);
                                               break; 
                        }
                }
                    
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                RowCount = RowCount + 1;
            }
            it_SPOAndBOMVo.closeRowSetIterator();
           
              // dummy PONumber            
//            String PONumber = "12456789";
//            excelcell = worksheet.getRow(xlRow).createCell(xlCol+1);
//            excelcell.setCellValue(PONumber);
//            excelcell.setCellStyle(italicStyle);
            
            /**
             * Ends SPO Inserting In Excel
             */
            
            // vo of po info
//            ViewObject POInfoPageVo = appM.getPOInformationVO1();
//            RowSetIterator it_fill_fabric_PR_info = POInfoPageVo.createRowSetIterator("aa");
//            
//            while (it_fill_fabric_PR_info.hasNext()) {
//                Row POInfoPageVoRow = it_fill_fabric_PR_info.next(); //next element returns
//                try {
//
//                    
//                    
//                    if (POInfoPageVoRow.getAttribute("BomRef").equals(HeaderRowStyleNo)){ //TODO
//                        String PONumber = (String)POInfoPageVoRow.getAttribute("PoNo");
//                        excelcell = worksheet.getRow(xlRow).createCell(xlCol+1);
//                        excelcell.setCellValue(PONumber);
//                        //excelcell.setCellStyle(italicStyle);
//                        }
//                    
//                    
//                } catch (Exception e) {
//                    System.out.println(e.getStackTrace());
//                }
//            }
//            it_fill_fabric_PR_info.closeRowSetIterator();
            
            //******************* ends of MJ PO/SPO portion ********
            System.out.println("Ends MJ PO/SPO portion");
            worksheet.autoSizeColumn(xlCol+1);

            this.xlLineRow = xlRow;
        }
        
        private void printDataColWise(int start, int end, int xlRow, int xlCol,Row row){
           
                        
                        
//                        ViewObject head = appM.getFabricCustomReportVO1();
//                        
//                       String date=head.getCurrentRow().getAttribute("BomDate").toString();
//                        excelcell = worksheet.getRow(11).createCell(2);
//                                      excelcell.setCellValue(date);
           
//           
//                int ros=11;
//            excelcell = worksheet.getRow(ros).createCell(5);
//            excelcell.setCellValue("Buyer");
//            excelcell.setCellStyle(headerStyle);
//            worksheet.autoSizeColumn(5);  
//            ros=ros+1;
//            excelcell = worksheet.getRow(ros).createCell(5);
//            excelcell.setCellValue("Season");
//            excelcell.setCellStyle(headerStyle);
//            worksheet.autoSizeColumn(5);  
//            ros=ros+1;
//            excelcell = worksheet.getRow(ros).createCell(5);
//            excelcell.setCellValue("Style Name");
//            excelcell.setCellStyle(headerStyle);
//            worksheet.autoSizeColumn(5);  
//            ros=ros+1;
//            excelcell = worksheet.getRow(ros).createCell(5);
//            excelcell.setCellValue("Style No");
//            excelcell.setCellStyle(headerStyle);
//            worksheet.autoSizeColumn(5);  
//            ros=ros+1;
//            excelcell = worksheet.getRow(ros).createCell(5);
//            excelcell.setCellValue("Brand");
//            excelcell.setCellStyle(headerStyle);
//            worksheet.autoSizeColumn(5);  
//            ros=ros+1;
//            excelcell = worksheet.getRow(ros).createCell(5);
//            excelcell.setCellValue("MJ PO/SPO");
//            excelcell.setCellStyle(headerStyle);
//            worksheet.autoSizeColumn(5);  

           
            for (String colName : row.getAttributeNames()) { //TODO
                int index=row.getAttributeIndexOf(colName);
                if (row.getAttributeIndexOf(colName) >= start &&
                    row.getAttributeIndexOf(colName) <= end) {
                    if(index>1)
                    {
                    excelrow = (HSSFRow)worksheet.getRow(xlRow);
                    excelcell = excelrow.createCell(xlCol);
                    if(row.getAttribute(colName)!=null)
                        excelcell.setCellValue(row.getAttribute(colName).toString());
                    //excelcell.setCellStyle(dataStyle); //datastyle = border appears
                    //excelcell.setCellStyle(italicStyle);
                    
                    worksheet.autoSizeColumn(xlCol);            
                    xlRow += 1;
                }
                    }
            } //ends for loop of print Column values in excel
    
            this.xlLineRow = xlRow; //row in excel = 18

        }
        
        
    private void addColValues(RowSet rowSet, String colName, int xlCol,int xlRow) {
        xlRow += 1;
        int colIndex = 0;
        rowSet.reset();
//        if (rowSet.hasNext()) {
//            Row row = rowSet.next();
//            colIndex = row.getAttributeIndexOf(colName);
//            excelrow = (HSSFRow)worksheet.getRow(xlRow);
//            int a = xlCol + colIndex - 1;
//            worksheet.addMergedRegion(new CellRangeAddress(xlRow,xlRow,0,a));
//            //excelcell.setCellStyle(headerStyle);
//            //excelcell = excelrow.createCell(a);
//            excelcell = excelrow.createCell(0);// edit..........12.03
//            excelcell.setCellStyle(headerStyle);
//            excelcell.setCellValue("Total ");
//            excelcell.setCellStyle(headerStyle);
//            //worksheet.autoSizeColumn(xlCol);
//        }
        rowSet.reset();
//        double total = 0.0;
//        while (rowSet.hasNext()) {
//            Row row = rowSet.next();
//           // String s1 = row.getAttribute(colName).toString();
//            //String s1 = (String)row.getAttribute(colName);
//           // Double d1 = Double.parseDouble(s1);
//            //double d1 = Double.valueOf(s1);
//           // total += d1;
//            String s1=row.getAttribute(colName).toString();
//            double d1=Double.valueOf(s1);
//            
//            total=total+d1;
//        }
//       // total =Double.parseDouble(new DecimalFormat("##.####").format(total));
//        excelcell = excelrow.createCell(xlCol + colIndex);
//        excelcell.setCellStyle(dataStyle);
//        
//       // excelcell.setCellValue("" + total);
//        //excelcell.setCellValue(new DecimalFormat("##.####").format(total));
//        String f=String.format("%.4f", total);
//        excelcell.setCellValue(f);
//        worksheet.autoSizeColumn(xlCol); 
    } 
    
    private void titleHeader(int end, int xlRow, int xlCol,String headerText){
        excelrow = (HSSFRow)worksheet.getRow(xlRow);
        excelcell = excelrow.createCell(xlCol);
        excelcell.setCellValue(headerText);
        CellRangeAddress cellRange = new CellRangeAddress(
                xlRow, //first row (0-based)
                xlRow, //last row  (0-based)
                xlCol, //first column (0-based)
                xlCol + end - 2 //last column  (0-based)
        );
        worksheet.addMergedRegion(cellRange);
        
        excelcell.setCellStyle(titleHeaderStyle);
    }
    
    private void subtitle(int end, int xlRow, int xlCol,String headerText){
        excelrow = (HSSFRow)worksheet.getRow(xlRow);
        excelcell = excelrow.createCell(xlCol);
        excelcell.setCellValue(headerText);
        CellRangeAddress cellRange = new CellRangeAddress(
                xlRow, //first row (0-based)
                xlRow, //last row  (0-based)
                xlCol, //first column (0-based)
                xlCol + end - 1 //last column  (0-based)
        );
        worksheet.addMergedRegion(cellRange);
        excelcell.setCellStyle(titleStyle);
    }
    
    private void subtitleLeft(int end, int xlRow, int xlCol,String headerText){
        excelrow = (HSSFRow)worksheet.getRow(xlRow);
        excelcell = excelrow.createCell(xlCol);
        excelcell.setCellValue(headerText);
        CellRangeAddress cellRange = new CellRangeAddress(
                xlRow, //first row (0-based)
                xlRow, //last row  (0-based)
                xlCol, //first column (0-based)
                xlCol + end //last column  (0-based)
        );
        worksheet.addMergedRegion(cellRange);
    }
    
    private void addInfoLeftBold(int end, int xlRow, int xlCol,String headerText){
        excelrow = (HSSFRow)worksheet.getRow(xlRow);
        excelcell = excelrow.createCell(xlCol);
        excelcell.setCellValue(headerText);
        CellRangeAddress cellRange = new CellRangeAddress(
                xlRow, //first row (0-based)
                xlRow, //last row  (0-based)
                xlCol, //first column (0-based)
                xlCol + end - 1   //last column  (0-based)
        );
        worksheet.addMergedRegion(cellRange);
        excelcell.setCellStyle(boldStyle);
    }

}

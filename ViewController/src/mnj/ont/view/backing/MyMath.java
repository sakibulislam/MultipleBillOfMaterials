package mnj.ont.view.backing;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.text.DecimalFormat;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.domain.Number;


public class MyMath {
    static DecimalFormat format = new DecimalFormat("#");  
    
        
    public MyMath() {
        super();
    }
    
    public static double round(double val){
       
       
        double rounded = 0.00;
        try{
            format.setMinimumFractionDigits(2);
            rounded = Double.parseDouble(format.format(val));
            System.out.println("Rounded Value----------->"+rounded);
        }catch(Exception e){
            ;    
        }
       return rounded; 
    }
    
    public static double roundTo4(double val){
       
       
        double rounded = 0.00;
        try{
            format.setMinimumFractionDigits(4);
            rounded = Double.parseDouble(format.format(val));
        }catch(Exception e){
            ;    
        }
       return rounded; 
    }
    
    public static double roundTo3(double val){
       
       
        double rounded = 0.00;
        try{
            format.setMinimumFractionDigits(3);
            rounded = Double.parseDouble(format.format(val));
        }catch(Exception e){
            ;    
        }
       return rounded; 
    }
    
    public static double numeric(RichInputText ob) {
        
        
        try{
        if (ob != null)
            return Double.parseDouble(String.valueOf(ob.getValue()));
        else
            return 0;
        }catch(Exception e){
            ;    
        }
        
        return 0;
    }
    
    public static double numeric2(RichOutputText ob) {
        
        
        try{
        if (ob != null)
            return Double.parseDouble(String.valueOf(ob.getValue()));
        else
            return 0;
        }catch(Exception e){
            ;    
        }
        
        return 0;
    }
    
    public static double numeric3(Object ob) {
        
             
        
        try{
        if (ob != null)
            return Double.parseDouble(String.valueOf(ob));
        else
            return 0;
        }catch(Exception e){
            ;    
        }
        
        return 0;
    }
    
    
    public static Number toNumber(double n){

        try {
            return new Number(changeToDouble(n));
        } catch (SQLException e) {
            ;
        }
        return new Number(0);
    }
    
    public static double roundUp(double n){
    
       // n = 1.6234;
        String value = String.valueOf(n);
    
        if(!value.contains(".")){
            
            return n;    
        }        
        
        char[] charArray = value.toCharArray();              
      
        if (charArray.length - (value.substring(0, value.indexOf(".")+1).length()) > 0){
            BigDecimal bdTest1 = new BigDecimal(value);
            BigDecimal another = bdTest1.setScale(0,BigDecimal.ROUND_CEILING);
            return another.doubleValue();
        }                    
        
        
        return n;
    }
    
    public static double roundDown(double n){
    
            BigDecimal bdTest1 = new BigDecimal(n);
            BigDecimal another = bdTest1.setScale(0,BigDecimal.ROUND_DOWN);
           
            return another.doubleValue();
    
        
           }
    
    public static double nvl(double a, double b){
        
        double value = 0.0;
        try{
            value = a;
        }catch (Exception e){
         return b;    
        }
        
        return a;
        
    }
    
    
    public static double changeToDouble(double n){
        
        if (Double.isNaN(n)) {
            return 0 ;    
        }
        if (Double.isInfinite(n)){
            return 0 ;
        }
        return n;
        
    }
    
    
    public static String ChangetoString(Object o){
        
        String val= null;
        
        try {
            if(o !=null)
            val = o.toString();
        } catch (Exception e) {
           val = null;
        }
        
        return val;
    }
}
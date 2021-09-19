/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoubleDatatype
/*    */   extends AbstractDatatype<Double>
/*    */ {
/*    */   public boolean isHandlingJavaType(Class<double> type) {
/* 28 */     return (type == double.class || Double.class.isAssignableFrom(type));
/*    */   }
/*    */   
/*    */   public Double valueOf(String s) throws InvalidValueException {
/* 32 */     if (s.equals("")) return null; 
/*    */     try {
/* 34 */       return Double.valueOf(Double.parseDouble(s));
/* 35 */     } catch (NumberFormatException ex) {
/* 36 */       throw new InvalidValueException("Can't convert string to number: " + s, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\DoubleDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
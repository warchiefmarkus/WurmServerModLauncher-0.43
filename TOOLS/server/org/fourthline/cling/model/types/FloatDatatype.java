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
/*    */ public class FloatDatatype
/*    */   extends AbstractDatatype<Float>
/*    */ {
/*    */   public boolean isHandlingJavaType(Class<float> type) {
/* 28 */     return (type == float.class || Float.class.isAssignableFrom(type));
/*    */   }
/*    */   
/*    */   public Float valueOf(String s) throws InvalidValueException {
/* 32 */     if (s.equals("")) return null; 
/*    */     try {
/* 34 */       return Float.valueOf(Float.parseFloat(s.trim()));
/* 35 */     } catch (NumberFormatException ex) {
/* 36 */       throw new InvalidValueException("Can't convert string to number: " + s, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\FloatDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
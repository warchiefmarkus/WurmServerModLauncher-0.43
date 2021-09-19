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
/*    */ public class ShortDatatype
/*    */   extends AbstractDatatype<Short>
/*    */ {
/*    */   public boolean isHandlingJavaType(Class<short> type) {
/* 26 */     return (type == short.class || Short.class.isAssignableFrom(type));
/*    */   }
/*    */   
/*    */   public Short valueOf(String s) throws InvalidValueException {
/* 30 */     if (s.equals("")) return null; 
/*    */     try {
/* 32 */       Short value = Short.valueOf(Short.parseShort(s.trim()));
/* 33 */       if (!isValid(value)) {
/* 34 */         throw new InvalidValueException("Not a valid short: " + s);
/*    */       }
/* 36 */       return value;
/* 37 */     } catch (NumberFormatException ex) {
/* 38 */       throw new InvalidValueException("Can't convert string to number: " + s, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\ShortDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
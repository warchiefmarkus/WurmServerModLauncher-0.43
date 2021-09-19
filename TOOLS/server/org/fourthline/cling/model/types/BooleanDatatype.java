/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public class BooleanDatatype
/*    */   extends AbstractDatatype<Boolean>
/*    */ {
/*    */   public boolean isHandlingJavaType(Class<boolean> type) {
/* 30 */     return (type == boolean.class || Boolean.class.isAssignableFrom(type));
/*    */   }
/*    */   
/*    */   public Boolean valueOf(String s) throws InvalidValueException {
/* 34 */     if (s.equals("")) return null; 
/* 35 */     if (s.equals("1") || s.toUpperCase(Locale.ROOT).equals("YES") || s.toUpperCase(Locale.ROOT).equals("TRUE"))
/* 36 */       return Boolean.valueOf(true); 
/* 37 */     if (s.equals("0") || s.toUpperCase(Locale.ROOT).equals("NO") || s.toUpperCase(Locale.ROOT).equals("FALSE")) {
/* 38 */       return Boolean.valueOf(false);
/*    */     }
/* 40 */     throw new InvalidValueException("Invalid boolean value string: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString(Boolean value) throws InvalidValueException {
/* 45 */     if (value == null) return ""; 
/* 46 */     return value.booleanValue() ? "1" : "0";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\BooleanDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
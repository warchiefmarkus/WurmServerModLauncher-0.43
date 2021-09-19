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
/*    */ public class StringDatatype
/*    */   extends AbstractDatatype<String>
/*    */ {
/*    */   public String valueOf(String s) throws InvalidValueException {
/* 27 */     if (s.equals("")) return null; 
/* 28 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\StringDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
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
/*    */ public class CustomDatatype
/*    */   extends AbstractDatatype<String>
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public CustomDatatype(String name) {
/* 26 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */   
/*    */   public String valueOf(String s) throws InvalidValueException {
/* 34 */     if (s.equals("")) return null; 
/* 35 */     return s;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 40 */     return "(" + getClass().getSimpleName() + ") '" + getName() + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\CustomDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
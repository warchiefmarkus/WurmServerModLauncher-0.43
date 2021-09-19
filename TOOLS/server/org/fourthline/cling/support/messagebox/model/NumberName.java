/*    */ package org.fourthline.cling.support.messagebox.model;
/*    */ 
/*    */ import org.fourthline.cling.support.messagebox.parser.MessageElement;
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
/*    */ public class NumberName
/*    */   implements ElementAppender
/*    */ {
/*    */   private String number;
/*    */   private String name;
/*    */   
/*    */   public NumberName(String number, String name) {
/* 29 */     this.number = number;
/* 30 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getNumber() {
/* 34 */     return this.number;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public void appendMessageElements(MessageElement parent) {
/* 42 */     ((MessageElement)parent.createChild("Number")).setContent(getNumber());
/* 43 */     ((MessageElement)parent.createChild("Name")).setContent(getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\model\NumberName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
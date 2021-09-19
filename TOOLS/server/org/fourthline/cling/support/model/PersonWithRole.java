/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import org.w3c.dom.Element;
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
/*    */ public class PersonWithRole
/*    */   extends Person
/*    */ {
/*    */   private String role;
/*    */   
/*    */   public PersonWithRole(String name) {
/* 28 */     super(name);
/*    */   }
/*    */   
/*    */   public PersonWithRole(String name, String role) {
/* 32 */     super(name);
/* 33 */     this.role = role;
/*    */   }
/*    */   
/*    */   public String getRole() {
/* 37 */     return this.role;
/*    */   }
/*    */   
/*    */   public void setOnElement(Element element) {
/* 41 */     element.setTextContent(toString());
/* 42 */     if (getRole() != null)
/* 43 */       element.setAttribute("role", getRole()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\PersonWithRole.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
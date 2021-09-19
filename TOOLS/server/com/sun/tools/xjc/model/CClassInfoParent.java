/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.JPackage;
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
/*    */ public interface CClassInfoParent
/*    */ {
/*    */   String fullName();
/*    */   
/*    */   <T> T accept(Visitor<T> paramVisitor);
/*    */   
/*    */   JPackage getOwnerPackage();
/*    */   
/*    */   public static final class Package
/*    */     implements CClassInfoParent
/*    */   {
/*    */     public final JPackage pkg;
/*    */     
/*    */     public Package(JPackage pkg) {
/* 79 */       this.pkg = pkg;
/*    */     }
/*    */     
/*    */     public String fullName() {
/* 83 */       return this.pkg.name();
/*    */     }
/*    */     
/*    */     public <T> T accept(CClassInfoParent.Visitor<T> visitor) {
/* 87 */       return visitor.onPackage(this.pkg);
/*    */     }
/*    */     
/*    */     public JPackage getOwnerPackage() {
/* 91 */       return this.pkg;
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface Visitor<T> {
/*    */     T onBean(CClassInfo param1CClassInfo);
/*    */     
/*    */     T onPackage(JPackage param1JPackage);
/*    */     
/*    */     T onElement(CElementInfo param1CElementInfo);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CClassInfoParent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
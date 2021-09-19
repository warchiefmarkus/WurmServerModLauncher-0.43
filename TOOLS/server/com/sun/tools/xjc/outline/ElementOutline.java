/*    */ package com.sun.tools.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.tools.xjc.model.CElementInfo;
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
/*    */ public abstract class ElementOutline
/*    */ {
/*    */   public final CElementInfo target;
/*    */   public final JDefinedClass implClass;
/*    */   
/*    */   public abstract Outline parent();
/*    */   
/*    */   public PackageOutline _package() {
/* 64 */     return parent().getPackageContext(this.implClass._package());
/*    */   }
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
/*    */   protected ElementOutline(CElementInfo target, JDefinedClass implClass) {
/* 80 */     this.target = target;
/* 81 */     this.implClass = implClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\ElementOutline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
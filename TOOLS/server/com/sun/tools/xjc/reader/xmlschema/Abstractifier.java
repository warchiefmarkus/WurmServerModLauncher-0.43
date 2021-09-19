/*    */ package com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CElement;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSElementDecl;
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
/*    */ class Abstractifier
/*    */   extends ClassBinderFilter
/*    */ {
/*    */   public Abstractifier(ClassBinder core) {
/* 50 */     super(core);
/*    */   }
/*    */   
/*    */   public CElement complexType(XSComplexType xs) {
/* 54 */     CElement ci = super.complexType(xs);
/* 55 */     if (ci != null && xs.isAbstract())
/* 56 */       ci.setAbstract(); 
/* 57 */     return ci;
/*    */   }
/*    */   
/*    */   public CElement elementDecl(XSElementDecl xs) {
/* 61 */     CElement ci = super.elementDecl(xs);
/* 62 */     if (ci != null && xs.isAbstract())
/* 63 */       ci.setAbstract(); 
/* 64 */     return ci;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\Abstractifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CClass;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSType;
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
/*    */ final class RestrictedComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 52 */     XSType baseType = ct.getBaseType();
/* 53 */     return (baseType != this.schemas.getAnyType() && baseType.isComplexType() && ct.getDerivationMethod() == 2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 59 */     if (this.bgmBuilder.getGlobalBinding().isRestrictionFreshType()) {
/*    */       
/* 61 */       (new FreshComplexTypeBuilder()).build(ct);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 66 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/*    */     
/* 69 */     CClass baseClass = this.selector.bindToType(baseType, (XSComponent)ct, true);
/* 70 */     assert baseClass != null;
/*    */     
/* 72 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/*    */ 
/*    */     
/* 75 */     this.builder.recordBindingMode(ct, this.builder.getBindingMode(baseType));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\RestrictedComplexTypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
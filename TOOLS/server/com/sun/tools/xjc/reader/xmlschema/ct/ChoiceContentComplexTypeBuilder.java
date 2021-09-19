/*    */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttContainer;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSModelGroup;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ import java.util.Collections;
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
/*    */ final class ChoiceContentComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 55 */     if (!this.bgmBuilder.getGlobalBinding().isChoiceContentPropertyEnabled()) {
/* 56 */       return false;
/*    */     }
/* 58 */     if (ct.getBaseType() != this.schemas.getAnyType())
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 64 */       return false;
/*    */     }
/* 66 */     XSParticle p = ct.getContentType().asParticle();
/* 67 */     if (p == null) {
/* 68 */       return false;
/*    */     }
/* 70 */     XSModelGroup mg = getTopLevelModelGroup(p);
/*    */     
/* 72 */     if (mg.getCompositor() != XSModelGroup.CHOICE) {
/* 73 */       return false;
/*    */     }
/* 75 */     if (p.isRepeated()) {
/* 76 */       return false;
/*    */     }
/* 78 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private XSModelGroup getTopLevelModelGroup(XSParticle p) {
/* 84 */     XSModelGroup mg = p.getTerm().asModelGroup();
/* 85 */     if (p.getTerm().isModelGroupDecl())
/* 86 */       mg = p.getTerm().asModelGroupDecl().getModelGroup(); 
/* 87 */     return mg;
/*    */   }
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 91 */     XSParticle p = ct.getContentType().asParticle();
/*    */     
/* 93 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */     
/* 95 */     this.bgmBuilder.getParticleBinder().build(p, Collections.singleton(p));
/*    */     
/* 97 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\ChoiceContentComplexTypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
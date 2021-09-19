/*    */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*    */ import com.sun.tools.xjc.model.CValuePropertyInfo;
/*    */ import com.sun.tools.xjc.model.TypeUse;
/*    */ import com.sun.tools.xjc.reader.RawTypeSet;
/*    */ import com.sun.tools.xjc.reader.xmlschema.RawTypeSetBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.xsom.XSAttContainer;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSContentType;
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
/*    */ final class MixedComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 55 */     XSType bt = ct.getBaseType();
/* 56 */     if (bt == this.schemas.getAnyType() && ct.isMixed()) {
/* 57 */       return true;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     if (bt.isComplexType() && !bt.asComplexType().isMixed() && ct.isMixed() && ct.getDerivationMethod() == 1 && ct.getContentType().asParticle() != null)
/*    */     {
/*    */       
/* 67 */       return true;
/*    */     }
/* 69 */     return false;
/*    */   }
/*    */   public void build(XSComplexType ct) {
/*    */     CReferencePropertyInfo cReferencePropertyInfo;
/* 73 */     XSContentType contentType = ct.getContentType();
/*    */ 
/*    */     
/* 76 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_CONTENT);
/*    */     
/* 78 */     BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/*    */ 
/*    */ 
/*    */     
/* 82 */     if (contentType.asEmpty() != null) {
/* 83 */       CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Content", false, (XSComponent)ct, (TypeUse)CBuiltinLeafInfo.STRING, null);
/*    */     } else {
/* 85 */       RawTypeSet ts = RawTypeSetBuilder.build(contentType.asParticle(), false);
/* 86 */       cReferencePropertyInfo = prop.createReferenceProperty("Content", false, (XSComponent)ct, ts, true);
/*    */     } 
/*    */     
/* 89 */     this.selector.getCurrentBean().addProperty((CPropertyInfo)cReferencePropertyInfo);
/*    */ 
/*    */     
/* 92 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\MixedComplexTypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.xjc.model.CValuePropertyInfo;
/*    */ import com.sun.tools.xjc.model.TypeUse;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.xsom.XSAttContainer;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSDeclaration;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class STDerivedComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 57 */     return ct.getBaseType().isSimpleType();
/*    */   }
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 61 */     assert ct.getDerivationMethod() == 1;
/*    */ 
/*    */     
/* 64 */     XSSimpleType baseType = ct.getBaseType().asSimpleType();
/*    */ 
/*    */     
/* 67 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */     
/* 69 */     this.simpleTypeBuilder.refererStack.push(ct);
/* 70 */     TypeUse use = this.simpleTypeBuilder.build(baseType);
/* 71 */     this.simpleTypeBuilder.refererStack.pop();
/*    */     
/* 73 */     BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/* 74 */     CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Value", false, (XSComponent)baseType, use, BGMBuilder.getName((XSDeclaration)baseType));
/* 75 */     this.selector.getCurrentBean().addProperty((CPropertyInfo)cValuePropertyInfo);
/*    */ 
/*    */     
/* 78 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\STDerivedComplexTypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
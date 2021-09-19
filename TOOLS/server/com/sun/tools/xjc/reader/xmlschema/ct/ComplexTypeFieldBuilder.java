/*     */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.BindingComponent;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ComplexTypeFieldBuilder
/*     */   extends BindingComponent
/*     */ {
/*  61 */   private final CTBuilder[] complexTypeBuilders = new CTBuilder[] { new MixedComplexTypeBuilder(), new FreshComplexTypeBuilder(), new ExtendedComplexTypeBuilder(), new RestrictedComplexTypeBuilder(), new STDerivedComplexTypeBuilder() };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private final Map<XSComplexType, ComplexTypeBindingMode> complexTypeBindingModes = new HashMap<XSComplexType, ComplexTypeBindingMode>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(XSComplexType type) {
/*  78 */     for (CTBuilder ctb : this.complexTypeBuilders) {
/*  79 */       if (ctb.isApplicable(type)) {
/*  80 */         ctb.build(type);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     assert false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordBindingMode(XSComplexType type, ComplexTypeBindingMode flag) {
/* 102 */     Object o = this.complexTypeBindingModes.put(type, flag);
/* 103 */     assert o == null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ComplexTypeBindingMode getBindingMode(XSComplexType type) {
/* 111 */     ComplexTypeBindingMode r = this.complexTypeBindingModes.get(type);
/* 112 */     assert r != null;
/* 113 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\ComplexTypeFieldBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
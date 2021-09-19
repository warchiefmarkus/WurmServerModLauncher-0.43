/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
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
/*     */ abstract class ColorBinder
/*     */   extends BindingComponent
/*     */   implements XSVisitor
/*     */ {
/*  57 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*  58 */   protected final ClassSelector selector = getClassSelector();
/*     */   
/*     */   protected final CClassInfo getCurrentBean() {
/*  61 */     return this.selector.getCurrentBean();
/*     */   }
/*     */   protected final XSComponent getCurrentRoot() {
/*  64 */     return this.selector.getCurrentRoot();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void createSimpleTypeProperty(XSSimpleType type, String propName) {
/*  69 */     BIProperty prop = BIProperty.getCustomization((XSComponent)type);
/*     */     
/*  71 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*     */     
/*  73 */     CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty(propName, false, (XSComponent)type, stb.buildDef(type), BGMBuilder.getName((XSDeclaration)type));
/*  74 */     getCurrentBean().addProperty((CPropertyInfo)cValuePropertyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void annotation(XSAnnotation xsAnnotation) {
/*  82 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public final void schema(XSSchema xsSchema) {
/*  86 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public final void facet(XSFacet xsFacet) {
/*  90 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public final void notation(XSNotation xsNotation) {
/*  94 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public final void identityConstraint(XSIdentityConstraint xsIdentityConstraint) {
/*  98 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public final void xpath(XSXPath xsxPath) {
/* 102 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ColorBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.tools.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.tools.xjc.Plugin;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.api.Mapping;
/*     */ import com.sun.tools.xjc.api.S2JJAXBModel;
/*     */ import com.sun.tools.xjc.api.TypeAndAnnotation;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.outline.PackageOutline;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
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
/*     */ final class JAXBModelImpl
/*     */   implements S2JJAXBModel
/*     */ {
/*     */   final Outline outline;
/*     */   private final Model model;
/*  75 */   private final Map<QName, Mapping> byXmlName = new HashMap<QName, Mapping>();
/*     */   
/*     */   JAXBModelImpl(Outline outline) {
/*  78 */     this.model = outline.getModel();
/*  79 */     this.outline = outline;
/*     */     
/*  81 */     for (CClassInfo ci : this.model.beans().values()) {
/*  82 */       if (!ci.isElement())
/*     */         continue; 
/*  84 */       this.byXmlName.put(ci.getElementName(), new BeanMappingImpl(this, ci));
/*     */     } 
/*  86 */     for (CElementInfo ei : this.model.getElementMappings(null).values()) {
/*  87 */       this.byXmlName.put(ei.getElementName(), new ElementMappingImpl(this, ei));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public JCodeModel generateCode(Plugin[] extensions, ErrorListener errorListener) {
/*  93 */     return this.outline.getCodeModel();
/*     */   }
/*     */   
/*     */   public List<JClass> getAllObjectFactories() {
/*  97 */     List<JClass> r = new ArrayList<JClass>();
/*  98 */     for (PackageOutline pkg : this.outline.getAllPackageContexts()) {
/*  99 */       r.add(pkg.objectFactory());
/*     */     }
/* 101 */     return r;
/*     */   }
/*     */   
/*     */   public final Mapping get(QName elementName) {
/* 105 */     return this.byXmlName.get(elementName);
/*     */   }
/*     */   
/*     */   public final Collection<? extends Mapping> getMappings() {
/* 109 */     return this.byXmlName.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeAndAnnotation getJavaType(QName xmlTypeName) {
/* 114 */     TypeUse use = (TypeUse)this.model.typeUses().get(xmlTypeName);
/* 115 */     if (use == null) return null;
/*     */     
/* 117 */     return new TypeAndAnnotationImpl(this.outline, use);
/*     */   }
/*     */   
/*     */   public final List<String> getClassList() {
/* 121 */     List<String> classList = new ArrayList<String>();
/*     */ 
/*     */     
/* 124 */     for (PackageOutline p : this.outline.getAllPackageContexts())
/* 125 */       classList.add(p.objectFactory().fullName()); 
/* 126 */     return classList;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\JAXBModelImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.tools.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.tools.xjc.api.Property;
/*     */ import com.sun.tools.xjc.api.TypeAndAnnotation;
/*     */ import com.sun.tools.xjc.model.CAdapter;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CNonElement;
/*     */ import com.sun.tools.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.model.TypeUseFactory;
/*     */ import java.util.List;
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
/*     */ final class ElementMappingImpl
/*     */   extends AbstractMappingImpl<CElementInfo>
/*     */ {
/*     */   private final TypeAndAnnotation taa;
/*     */   
/*     */   protected ElementMappingImpl(JAXBModelImpl parent, CElementInfo elementInfo) {
/*  59 */     super(parent, elementInfo);
/*     */     TypeUse typeUse;
/*  61 */     CNonElement cNonElement = this.clazz.getContentType();
/*  62 */     if (this.clazz.getProperty().isCollection())
/*  63 */       typeUse = TypeUseFactory.makeCollection((TypeUse)cNonElement); 
/*  64 */     CAdapter a = this.clazz.getProperty().getAdapter();
/*  65 */     if (a != null)
/*  66 */       typeUse = TypeUseFactory.adapt(typeUse, a); 
/*  67 */     this.taa = new TypeAndAnnotationImpl(parent.outline, typeUse);
/*     */   }
/*     */   
/*     */   public TypeAndAnnotation getType() {
/*  71 */     return this.taa;
/*     */   }
/*     */   
/*     */   public final List<Property> calcDrilldown() {
/*  75 */     CElementPropertyInfo p = this.clazz.getProperty();
/*     */     
/*  77 */     if (p.getAdapter() != null) {
/*  78 */       return null;
/*     */     }
/*  80 */     if (p.isCollection())
/*     */     {
/*  82 */       return null;
/*     */     }
/*  84 */     CTypeInfo typeClass = p.ref().get(0);
/*     */     
/*  86 */     if (!(typeClass instanceof CClassInfo))
/*     */     {
/*  88 */       return null;
/*     */     }
/*  90 */     CClassInfo ci = (CClassInfo)typeClass;
/*     */ 
/*     */     
/*  93 */     if (ci.isAbstract()) {
/*  94 */       return null;
/*     */     }
/*     */     
/*  97 */     if (!ci.isOrdered()) {
/*  98 */       return null;
/*     */     }
/* 100 */     return buildDrilldown(ci);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\ElementMappingImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
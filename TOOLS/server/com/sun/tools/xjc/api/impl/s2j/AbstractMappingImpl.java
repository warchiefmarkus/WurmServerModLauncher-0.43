/*     */ package com.sun.tools.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.tools.xjc.api.Mapping;
/*     */ import com.sun.tools.xjc.api.Property;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CElement;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ abstract class AbstractMappingImpl<InfoT extends CElement>
/*     */   implements Mapping
/*     */ {
/*     */   protected final JAXBModelImpl parent;
/*     */   protected final InfoT clazz;
/*  72 */   private List<Property> drilldown = null;
/*     */   private boolean drilldownComputed = false;
/*     */   
/*     */   protected AbstractMappingImpl(JAXBModelImpl parent, InfoT clazz) {
/*  76 */     this.parent = parent;
/*  77 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public final QName getElement() {
/*  81 */     return this.clazz.getElementName();
/*     */   }
/*     */   
/*     */   public final String getClazz() {
/*  85 */     return ((NType)this.clazz.getType()).fullName();
/*     */   }
/*     */   
/*     */   public final List<? extends Property> getWrapperStyleDrilldown() {
/*  89 */     if (!this.drilldownComputed) {
/*  90 */       this.drilldownComputed = true;
/*  91 */       this.drilldown = calcDrilldown();
/*     */     } 
/*  93 */     return this.drilldown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract List<Property> calcDrilldown();
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Property> buildDrilldown(CClassInfo typeBean) {
/*     */     List<Property> result;
/* 105 */     CClassInfo bc = typeBean.getBaseClass();
/* 106 */     if (bc != null) {
/* 107 */       result = buildDrilldown(bc);
/* 108 */       if (result == null)
/* 109 */         return null; 
/*     */     } else {
/* 111 */       result = new ArrayList<Property>();
/*     */     } 
/* 113 */     for (CPropertyInfo p : typeBean.getProperties()) {
/* 114 */       if (p instanceof CElementPropertyInfo) {
/* 115 */         CElementPropertyInfo ep = (CElementPropertyInfo)p;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 121 */         List<? extends CTypeRef> ref = ep.getTypes();
/* 122 */         if (ref.size() != 1)
/*     */         {
/* 124 */           return null;
/*     */         }
/* 126 */         result.add(createPropertyImpl((CPropertyInfo)ep, ((CTypeRef)ref.get(0)).getTagName())); continue;
/*     */       } 
/* 128 */       if (p instanceof com.sun.xml.bind.v2.model.core.ReferencePropertyInfo) {
/* 129 */         ElementAdapter fr; CReferencePropertyInfo rp = (CReferencePropertyInfo)p;
/*     */         
/* 131 */         Collection<CElement> elements = rp.getElements();
/* 132 */         if (elements.size() != 1) {
/* 133 */           return null;
/*     */         }
/* 135 */         CElement ref = elements.iterator().next();
/* 136 */         if (ref instanceof com.sun.xml.bind.v2.model.core.ClassInfo) {
/* 137 */           result.add(createPropertyImpl((CPropertyInfo)rp, ref.getElementName())); continue;
/*     */         } 
/* 139 */         CElementInfo eref = (CElementInfo)ref;
/* 140 */         if (!eref.getSubstitutionMembers().isEmpty()) {
/* 141 */           return null;
/*     */         }
/*     */ 
/*     */         
/* 145 */         if (rp.isCollection()) {
/* 146 */           fr = new ElementCollectionAdapter(this.parent.outline.getField((CPropertyInfo)rp), eref);
/*     */         } else {
/* 148 */           fr = new ElementSingleAdapter(this.parent.outline.getField((CPropertyInfo)rp), eref);
/*     */         } 
/* 150 */         result.add(new PropertyImpl(this, fr, eref.getElementName()));
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 156 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 160 */     return result;
/*     */   }
/*     */   
/*     */   private Property createPropertyImpl(CPropertyInfo p, QName tagName) {
/* 164 */     return new PropertyImpl(this, this.parent.outline.getField(p), tagName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\s2j\AbstractMappingImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
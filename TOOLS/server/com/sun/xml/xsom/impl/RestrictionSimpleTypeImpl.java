/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSVariety;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class RestrictionSimpleTypeImpl
/*     */   extends SimpleTypeImpl
/*     */   implements XSRestrictionSimpleType
/*     */ {
/*     */   private final List<XSFacet> facets;
/*     */   
/*     */   public RestrictionSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _baseType) {
/*  44 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, _baseType);
/*     */ 
/*     */ 
/*     */     
/*  48 */     this.facets = new ArrayList<XSFacet>();
/*     */   } public void addFacet(XSFacet facet) {
/*  50 */     this.facets.add(facet);
/*     */   }
/*     */   public Iterator<XSFacet> iterateDeclaredFacets() {
/*  53 */     return this.facets.iterator();
/*     */   }
/*     */   
/*     */   public Collection<? extends XSFacet> getDeclaredFacets() {
/*  57 */     return this.facets;
/*     */   }
/*     */   
/*     */   public XSFacet getDeclaredFacet(String name) {
/*  61 */     int len = this.facets.size();
/*  62 */     for (int i = 0; i < len; i++) {
/*  63 */       XSFacet f = this.facets.get(i);
/*  64 */       if (f.getName().equals(name))
/*  65 */         return f; 
/*     */     } 
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   public List<XSFacet> getDeclaredFacets(String name) {
/*  71 */     List<XSFacet> r = new ArrayList<XSFacet>();
/*  72 */     for (XSFacet f : this.facets) {
/*  73 */       if (f.getName().equals(name))
/*  74 */         r.add(f); 
/*  75 */     }  return r;
/*     */   }
/*     */   
/*     */   public XSFacet getFacet(String name) {
/*  79 */     XSFacet f = getDeclaredFacet(name);
/*  80 */     if (f != null) return f;
/*     */ 
/*     */     
/*  83 */     return getSimpleBaseType().getFacet(name);
/*     */   }
/*     */   public XSVariety getVariety() {
/*  86 */     return getSimpleBaseType().getVariety();
/*     */   }
/*     */   public XSSimpleType getPrimitiveType() {
/*  89 */     if (isPrimitive()) return this; 
/*  90 */     return getSimpleBaseType().getPrimitiveType();
/*     */   }
/*     */   
/*     */   public boolean isPrimitive() {
/*  94 */     return (getSimpleBaseType() == (getOwnerSchema().getRoot()).anySimpleType);
/*     */   }
/*     */   
/*     */   public void visit(XSSimpleTypeVisitor visitor) {
/*  98 */     visitor.restrictionSimpleType(this);
/*     */   }
/*     */   public Object apply(XSSimpleTypeFunction function) {
/* 101 */     return function.restrictionSimpleType(this);
/*     */   }
/*     */   
/* 104 */   public boolean isRestriction() { return true; } public XSRestrictionSimpleType asRestriction() {
/* 105 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\RestrictionSimpleTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
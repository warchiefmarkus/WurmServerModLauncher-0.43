/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSVariety;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.visitor.XSContentTypeFunction;
/*     */ import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SimpleTypeImpl
/*     */   extends DeclarationImpl
/*     */   implements XSSimpleType, ContentTypeImpl, Ref.SimpleType
/*     */ {
/*     */   private Ref.SimpleType baseType;
/*     */   private short redefiningCount;
/*     */   private SimpleTypeImpl redefinedBy;
/*     */   private final Set<XSVariety> finalSet;
/*     */   
/*     */   SimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _baseType) {
/*  53 */     super(_parent, _annon, _loc, _fa, _parent.getTargetNamespace(), _name, _anonymous);
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
/*  74 */     this.redefiningCount = 0;
/*     */     
/*  76 */     this.redefinedBy = null;
/*     */     this.baseType = _baseType;
/*     */     this.finalSet = finalSet;
/*  79 */   } public XSType[] listSubstitutables() { return Util.listSubstitutables((XSType)this); } public XSSimpleType getRedefinedBy() { return this.redefinedBy; }
/*     */   public void redefine(SimpleTypeImpl st) { this.baseType = st;
/*     */     st.redefinedBy = this;
/*     */     this.redefiningCount = (short)(st.redefiningCount + 1); } public int getRedefinedCount() {
/*  83 */     int i = 0;
/*  84 */     for (SimpleTypeImpl st = this.redefinedBy; st != null; st = st.redefinedBy)
/*  85 */       i++; 
/*  86 */     return i;
/*     */   }
/*     */   
/*  89 */   public XSType getBaseType() { return (XSType)this.baseType.getType(); }
/*  90 */   public XSSimpleType getSimpleBaseType() { return this.baseType.getType(); } public boolean isPrimitive() {
/*  91 */     return false;
/*     */   }
/*     */   public XSListSimpleType getBaseListType() {
/*  94 */     return getSimpleBaseType().getBaseListType();
/*     */   }
/*     */   
/*     */   public XSUnionSimpleType getBaseUnionType() {
/*  98 */     return getSimpleBaseType().getBaseUnionType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal(XSVariety v) {
/* 104 */     return this.finalSet.contains(v);
/*     */   }
/*     */   
/*     */   public final int getDerivationMethod() {
/* 108 */     return 2;
/*     */   }
/*     */   
/* 111 */   public final XSSimpleType asSimpleType() { return this; } public final XSComplexType asComplexType() {
/* 112 */     return null;
/*     */   }
/*     */   public boolean isDerivedFrom(XSType t) {
/* 115 */     SimpleTypeImpl simpleTypeImpl = this;
/*     */     while (true) {
/* 117 */       if (t == simpleTypeImpl)
/* 118 */         return true; 
/* 119 */       XSType s = simpleTypeImpl.getBaseType();
/* 120 */       if (s == simpleTypeImpl)
/* 121 */         return false; 
/* 122 */       XSType xSType1 = s;
/*     */     } 
/*     */   }
/*     */   
/* 126 */   public final boolean isSimpleType() { return true; }
/* 127 */   public final boolean isComplexType() { return false; }
/* 128 */   public final XSParticle asParticle() { return null; } public final XSContentType asEmpty() {
/* 129 */     return null;
/*     */   }
/*     */   
/* 132 */   public boolean isRestriction() { return false; }
/* 133 */   public boolean isList() { return false; }
/* 134 */   public boolean isUnion() { return false; }
/* 135 */   public XSRestrictionSimpleType asRestriction() { return null; }
/* 136 */   public XSListSimpleType asList() { return null; } public XSUnionSimpleType asUnion() {
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void visit(XSVisitor visitor) {
/* 143 */     visitor.simpleType(this);
/*     */   }
/*     */   public final void visit(XSContentTypeVisitor visitor) {
/* 146 */     visitor.simpleType(this);
/*     */   }
/*     */   public final Object apply(XSFunction function) {
/* 149 */     return function.simpleType(this);
/*     */   }
/*     */   public final Object apply(XSContentTypeFunction function) {
/* 152 */     return function.simpleType(this);
/*     */   }
/*     */   
/*     */   public XSContentType getContentType() {
/* 156 */     return this;
/*     */   } public XSSimpleType getType() {
/* 158 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\SimpleTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
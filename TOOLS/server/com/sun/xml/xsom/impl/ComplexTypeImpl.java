/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.impl.scd.Iterators;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public class ComplexTypeImpl
/*     */   extends AttributesHolder
/*     */   implements XSComplexType, Ref.ComplexType
/*     */ {
/*     */   private int derivationMethod;
/*     */   private Ref.Type baseType;
/*     */   private short redefiningCount;
/*     */   private ComplexTypeImpl redefinedBy;
/*     */   private XSElementDecl scope;
/*     */   private final boolean _abstract;
/*     */   private WildcardImpl localAttWildcard;
/*     */   private final int finalValue;
/*     */   private final int blockValue;
/*     */   private Ref.ContentType contentType;
/*     */   private XSContentType explicitContent;
/*     */   private final boolean mixed;
/*     */   
/*     */   public ComplexTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, boolean _abstract, int _derivationMethod, Ref.Type _base, int _final, int _block, boolean _mixed) {
/*  49 */     super(_parent, _annon, _loc, _fa, _name, _anonymous);
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
/* 101 */     this.redefiningCount = 0;
/*     */     
/* 103 */     this.redefinedBy = null; if (_base == null) throw new IllegalArgumentException();  this._abstract = _abstract; this.derivationMethod = _derivationMethod; this.baseType = _base; this.finalValue = _final; this.blockValue = _block; this.mixed = _mixed;
/*     */   }
/*     */   public XSComplexType asComplexType() { return this; }
/* 106 */   public boolean isDerivedFrom(XSType t) { ComplexTypeImpl complexTypeImpl = this; while (true) { if (t == complexTypeImpl) return true;  XSType s = complexTypeImpl.getBaseType(); if (s == complexTypeImpl) return false;  XSType xSType1 = s; }  } public XSSimpleType asSimpleType() { return null; } public final boolean isSimpleType() { return false; } public XSComplexType getRedefinedBy() { return this.redefinedBy; }
/*     */   public final boolean isComplexType() { return true; }
/*     */   public int getDerivationMethod() { return this.derivationMethod; }
/*     */   public XSType getBaseType() { return this.baseType.getType(); }
/* 110 */   public void redefine(ComplexTypeImpl ct) { if (this.baseType instanceof DelayedRef) { ((DelayedRef)this.baseType).redefine(ct); } else { this.baseType = ct; }  ct.redefinedBy = this; this.redefiningCount = (short)(ct.redefiningCount + 1); } public int getRedefinedCount() { int i = 0;
/* 111 */     for (ComplexTypeImpl ct = this.redefinedBy; ct != null; ct = ct.redefinedBy)
/* 112 */       i++; 
/* 113 */     return i; }
/*     */ 
/*     */   
/*     */   public XSElementDecl getScope()
/*     */   {
/* 118 */     return this.scope; } public void setScope(XSElementDecl _scope) {
/* 119 */     this.scope = _scope;
/*     */   }
/*     */   public boolean isAbstract() {
/* 122 */     return this._abstract;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWildcard(WildcardImpl wc) {
/* 129 */     this.localAttWildcard = wc;
/*     */   }
/*     */   public XSWildcard getAttributeWildcard() {
/* 132 */     WildcardImpl complete = this.localAttWildcard;
/*     */     
/* 134 */     Iterator<XSAttGroupDecl> itr = iterateAttGroups();
/* 135 */     while (itr.hasNext()) {
/* 136 */       WildcardImpl w = (WildcardImpl)((XSAttGroupDecl)itr.next()).getAttributeWildcard();
/*     */       
/* 138 */       if (w == null)
/*     */         continue; 
/* 140 */       if (complete == null) {
/* 141 */         complete = w;
/*     */         
/*     */         continue;
/*     */       } 
/* 145 */       complete = complete.union(this.ownerDocument, w);
/*     */     } 
/*     */     
/* 148 */     if (getDerivationMethod() == 2) return complete;
/*     */     
/* 150 */     WildcardImpl base = null;
/* 151 */     XSType baseType = getBaseType();
/* 152 */     if (baseType.asComplexType() != null) {
/* 153 */       base = (WildcardImpl)baseType.asComplexType().getAttributeWildcard();
/*     */     }
/* 155 */     if (complete == null) return base; 
/* 156 */     if (base == null) return complete;
/*     */     
/* 158 */     return complete.union(this.ownerDocument, base);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal(int derivationMethod) {
/* 163 */     return ((this.finalValue & derivationMethod) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSubstitutionProhibited(int method) {
/* 168 */     return ((this.blockValue & method) != 0);
/*     */   }
/*     */   
/*     */   public void setContentType(Ref.ContentType v)
/*     */   {
/* 173 */     this.contentType = v; } public XSContentType getContentType() {
/* 174 */     return this.contentType.getContentType();
/*     */   }
/*     */   
/*     */   public void setExplicitContent(XSContentType v) {
/* 178 */     this.explicitContent = v;
/*     */   } public XSContentType getExplicitContent() {
/* 180 */     return this.explicitContent;
/*     */   }
/*     */   public boolean isMixed() {
/* 183 */     return this.mixed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XSAttributeUse getAttributeUse(String nsURI, String localName) {
/* 189 */     UName name = new UName(nsURI, localName);
/*     */     
/* 191 */     if (this.prohibitedAtts.contains(name)) return null;
/*     */     
/* 193 */     XSAttributeUse o = this.attributes.get(name);
/*     */ 
/*     */     
/* 196 */     if (o == null) {
/* 197 */       Iterator<XSAttGroupDecl> itr = iterateAttGroups();
/* 198 */       while (itr.hasNext() && o == null) {
/* 199 */         o = ((XSAttGroupDecl)itr.next()).getAttributeUse(nsURI, localName);
/*     */       }
/*     */     } 
/* 202 */     if (o == null) {
/* 203 */       XSType base = getBaseType();
/* 204 */       if (base.asComplexType() != null) {
/* 205 */         o = base.asComplexType().getAttributeUse(nsURI, localName);
/*     */       }
/*     */     } 
/* 208 */     return o;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<XSAttributeUse> iterateAttributeUses() {
/* 213 */     XSComplexType baseType = getBaseType().asComplexType();
/*     */     
/* 215 */     if (baseType == null) return super.iterateAttributeUses();
/*     */     
/* 217 */     return (Iterator<XSAttributeUse>)new Iterators.Union((Iterator)new Iterators.Filter<XSAttributeUse>(baseType.iterateAttributeUses())
/*     */         {
/*     */           protected boolean matches(XSAttributeUse value) {
/* 220 */             XSAttributeDecl u = value.getDecl();
/* 221 */             UName n = new UName(u.getTargetNamespace(), u.getName());
/* 222 */             return !ComplexTypeImpl.this.prohibitedAtts.contains(n);
/*     */           }
/*     */         }super.iterateAttributeUses());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XSType[] listSubstitutables() {
/* 230 */     return Util.listSubstitutables((XSType)this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/* 236 */     visitor.complexType(this);
/*     */   }
/*     */   public <T> T apply(XSFunction<T> function) {
/* 239 */     return (T)function.complexType(this);
/*     */   }
/*     */   
/*     */   public XSComplexType getType() {
/* 243 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ComplexTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
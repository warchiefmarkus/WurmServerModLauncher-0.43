/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunctionWithParam;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
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
/*     */ public class ElementDecl
/*     */   extends DeclarationImpl
/*     */   implements XSElementDecl, Ref.Term
/*     */ {
/*     */   private XmlString defaultValue;
/*     */   private XmlString fixedValue;
/*     */   private boolean nillable;
/*     */   private boolean _abstract;
/*     */   private Ref.Type type;
/*     */   private Ref.Element substHead;
/*     */   private int substDisallowed;
/*     */   private int substExcluded;
/*     */   private final List<XSIdentityConstraint> idConstraints;
/*     */   private Set<XSElementDecl> substitutables;
/*     */   private Set<XSElementDecl> substitutablesView;
/*     */   
/*     */   public ElementDecl(PatcherManager reader, SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa, String _tns, String _name, boolean _anonymous, XmlString _defv, XmlString _fixedv, boolean _nillable, boolean _abstract, Ref.Type _type, Ref.Element _substHead, int _substDisallowed, int _substExcluded, List<IdentityConstraintImpl> idConstraints) {
/*  56 */     super(owner, _annon, _loc, fa, _tns, _name, _anonymous);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     this.substitutables = null;
/*     */ 
/*     */     
/* 124 */     this.substitutablesView = null; this.defaultValue = _defv; this.fixedValue = _fixedv; this.nillable = _nillable; this._abstract = _abstract; this.type = _type; this.substHead = _substHead; this.substDisallowed = _substDisallowed; this.substExcluded = _substExcluded; this.idConstraints = Collections.unmodifiableList((List)idConstraints); for (IdentityConstraintImpl idc : idConstraints) idc.setParent(this);  if (this.type == null) throw new IllegalArgumentException(); 
/*     */   }
/*     */   public XmlString getDefaultValue() { return this.defaultValue; }
/* 127 */   public XmlString getFixedValue() { return this.fixedValue; } public boolean isNillable() { return this.nillable; } public boolean isAbstract() { return this._abstract; } public XSType getType() { return this.type.getType(); } public Set<? extends XSElementDecl> getSubstitutables() { if (this.substitutables == null)
/*     */     {
/*     */       
/* 130 */       this.substitutables = this.substitutablesView = Collections.singleton(this);
/*     */     }
/* 132 */     return this.substitutablesView; }
/*     */   public XSElementDecl getSubstAffiliation() { if (this.substHead == null) return null;  return this.substHead.get(); }
/*     */   public boolean isSubstitutionDisallowed(int method) { return ((this.substDisallowed & method) != 0); }
/*     */   public boolean isSubstitutionExcluded(int method) { return ((this.substExcluded & method) != 0); }
/* 136 */   public List<XSIdentityConstraint> getIdentityConstraints() { return this.idConstraints; } public XSElementDecl[] listSubstitutables() { Set<? extends XSElementDecl> s = getSubstitutables(); return s.<XSElementDecl>toArray(new XSElementDecl[s.size()]); } protected void addSubstitutable(ElementDecl decl) { if (this.substitutables == null) {
/* 137 */       this.substitutables = new HashSet<XSElementDecl>();
/* 138 */       this.substitutables.add(this);
/* 139 */       this.substitutablesView = Collections.unmodifiableSet(this.substitutables);
/*     */     } 
/* 141 */     this.substitutables.add(decl); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSubstitutabilityMap() {
/* 146 */     ElementDecl parent = this;
/* 147 */     XSType type = getType();
/*     */     
/* 149 */     boolean rused = false;
/* 150 */     boolean eused = false;
/*     */     
/* 152 */     while ((parent = (ElementDecl)parent.getSubstAffiliation()) != null) {
/*     */       
/* 154 */       if (parent.isSubstitutionDisallowed(4)) {
/*     */         continue;
/*     */       }
/* 157 */       boolean rd = parent.isSubstitutionDisallowed(2);
/* 158 */       boolean ed = parent.isSubstitutionDisallowed(1);
/*     */       
/* 160 */       if ((rd && rused) || (ed && eused))
/*     */         continue; 
/* 162 */       XSType parentType = parent.getType();
/* 163 */       while (type != parentType) {
/* 164 */         if (type.getDerivationMethod() == 2) { rused = true; }
/* 165 */         else { eused = true; }
/*     */         
/* 167 */         type = type.getBaseType();
/* 168 */         if (type == null) {
/*     */           break;
/*     */         }
/* 171 */         if (type.isComplexType()) {
/* 172 */           rd |= type.asComplexType().isSubstitutionProhibited(2);
/* 173 */           ed |= type.asComplexType().isSubstitutionProhibited(1);
/*     */         } 
/*     */       } 
/*     */       
/* 177 */       if ((rd && rused) || (ed && eused)) {
/*     */         continue;
/*     */       }
/* 180 */       parent.addSubstitutable(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeSubstitutedBy(XSElementDecl e) {
/* 185 */     return getSubstitutables().contains(e);
/*     */   }
/*     */   
/* 188 */   public boolean isWildcard() { return false; }
/* 189 */   public boolean isModelGroupDecl() { return false; }
/* 190 */   public boolean isModelGroup() { return false; } public boolean isElementDecl() {
/* 191 */     return true;
/*     */   }
/* 193 */   public XSWildcard asWildcard() { return null; }
/* 194 */   public XSModelGroupDecl asModelGroupDecl() { return null; }
/* 195 */   public XSModelGroup asModelGroup() { return null; } public XSElementDecl asElementDecl() {
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/* 202 */     visitor.elementDecl(this);
/*     */   }
/*     */   public void visit(XSTermVisitor visitor) {
/* 205 */     visitor.elementDecl(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 208 */     return function.elementDecl(this);
/*     */   }
/*     */   
/*     */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 212 */     return (T)function.elementDecl(this, param);
/*     */   }
/*     */   
/*     */   public Object apply(XSFunction function) {
/* 216 */     return function.elementDecl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public XSTerm getTerm() {
/* 221 */     return (XSTerm)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ElementDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
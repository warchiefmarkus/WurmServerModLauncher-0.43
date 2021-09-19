/*     */ package 1.0.com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.DeclarationImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ public class ElementDecl
/*     */   extends DeclarationImpl
/*     */   implements XSElementDecl, Ref.Term
/*     */ {
/*     */   private String defaultValue;
/*     */   private String fixedValue;
/*     */   private boolean nillable;
/*     */   private boolean _abstract;
/*     */   private Ref.Type type;
/*     */   private Ref.Element substHead;
/*     */   private int substDisallowed;
/*     */   private int substExcluded;
/*     */   private Set substitutables;
/*     */   private Set substitutablesView;
/*     */   
/*     */   public ElementDecl(PatcherManager reader, SchemaImpl owner, AnnotationImpl _annon, Locator _loc, String _tns, String _name, boolean _anonymous, String _defv, String _fixedv, boolean _nillable, boolean _abstract, Ref.Type _type, Ref.Element _substHead, int _substDisallowed, int _substExcluded) {
/*  40 */     super(owner, _annon, _loc, _tns, _name, _anonymous);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     this.substitutables = null;
/*     */ 
/*     */     
/*  95 */     this.substitutablesView = null; this.defaultValue = _defv; this.fixedValue = _fixedv; this.nillable = _nillable; this._abstract = _abstract; this.type = _type; this.substHead = _substHead; this.substDisallowed = _substDisallowed; this.substExcluded = _substExcluded; if (this.type == null) throw new IllegalArgumentException(); 
/*     */   }
/*     */   public String getDefaultValue() { return this.defaultValue; }
/*  98 */   public String getFixedValue() { return this.fixedValue; } public boolean isNillable() { return this.nillable; } public boolean isAbstract() { return this._abstract; } public Set getSubstitutables() { if (this.substitutables == null)
/*     */     {
/*     */       
/* 101 */       this.substitutables = this.substitutablesView = Collections.singleton(this);
/*     */     }
/* 103 */     return this.substitutablesView; }
/*     */   public XSType getType() { return this.type.getType(); }
/*     */   public XSElementDecl getSubstAffiliation() { if (this.substHead == null) return null;  return this.substHead.get(); }
/*     */   public boolean isSubstitutionDisallowed(int method) { return ((this.substDisallowed & method) != 0); }
/* 107 */   public boolean isSubstitutionExcluded(int method) { return ((this.substExcluded & method) != 0); } public XSElementDecl[] listSubstitutables() { Set s = getSubstitutables(); return (XSElementDecl[])s.toArray((Object[])new XSElementDecl[s.size()]); } protected void addSubstitutable(com.sun.xml.xsom.impl.ElementDecl decl) { if (this.substitutables == null) {
/* 108 */       this.substitutables = new HashSet();
/* 109 */       this.substitutables.add(this);
/* 110 */       this.substitutablesView = Collections.unmodifiableSet(this.substitutables);
/*     */     } 
/* 112 */     this.substitutables.add(decl); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSubstitutabilityMap() {
/* 117 */     com.sun.xml.xsom.impl.ElementDecl parent = this;
/* 118 */     XSType type = getType();
/*     */     
/* 120 */     boolean rused = false;
/* 121 */     boolean eused = false;
/*     */     
/* 123 */     while ((parent = (com.sun.xml.xsom.impl.ElementDecl)parent.getSubstAffiliation()) != null) {
/*     */       
/* 125 */       if (parent.isSubstitutionDisallowed(4)) {
/*     */         continue;
/*     */       }
/* 128 */       boolean rd = parent.isSubstitutionDisallowed(2);
/* 129 */       boolean ed = parent.isSubstitutionDisallowed(1);
/*     */       
/* 131 */       if ((rd && rused) || (ed && eused))
/*     */         continue; 
/* 133 */       XSType parentType = parent.getType();
/* 134 */       while (type != parentType) {
/* 135 */         if (type.getDerivationMethod() == 2) { rused = true; }
/* 136 */         else { eused = true; }
/*     */         
/* 138 */         type = type.getBaseType();
/* 139 */         if (type == null) {
/*     */           break;
/*     */         }
/* 142 */         if (type.isComplexType()) {
/* 143 */           rd |= type.asComplexType().isSubstitutionProhibited(2);
/* 144 */           ed |= type.asComplexType().isSubstitutionProhibited(1);
/*     */         } 
/*     */       } 
/*     */       
/* 148 */       if ((rd && rused) || (ed && eused)) {
/*     */         continue;
/*     */       }
/* 151 */       parent.addSubstitutable(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeSubstitutedBy(XSElementDecl e) {
/* 156 */     return getSubstitutables().contains(e);
/*     */   }
/*     */   
/* 159 */   public boolean isWildcard() { return false; }
/* 160 */   public boolean isModelGroupDecl() { return false; }
/* 161 */   public boolean isModelGroup() { return false; } public boolean isElementDecl() {
/* 162 */     return true;
/*     */   }
/* 164 */   public XSWildcard asWildcard() { return null; }
/* 165 */   public XSModelGroupDecl asModelGroupDecl() { return null; }
/* 166 */   public XSModelGroup asModelGroup() { return null; } public XSElementDecl asElementDecl() {
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/* 173 */     visitor.elementDecl(this);
/*     */   }
/*     */   public void visit(XSTermVisitor visitor) {
/* 176 */     visitor.elementDecl(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 179 */     return function.elementDecl(this);
/*     */   }
/*     */   public Object apply(XSFunction function) {
/* 182 */     return function.elementDecl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public XSTerm getTerm() {
/* 187 */     return (XSTerm)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\ElementDecl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
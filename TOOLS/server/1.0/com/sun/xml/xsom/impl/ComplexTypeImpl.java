/*     */ package 1.0.com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.Util;
/*     */ import com.sun.xml.xsom.impl.WildcardImpl;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.util.ConcatIterator;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.Iterator;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class ComplexTypeImpl
/*     */   extends AttributesHolder implements XSComplexType, Ref.ComplexType {
/*     */   private int derivationMethod;
/*     */   private Ref.Type baseType;
/*     */   private XSElementDecl scope;
/*     */   private final boolean _abstract;
/*     */   private WildcardImpl localAttWildcard;
/*     */   private final int finalValue;
/*     */   private final int blockValue;
/*     */   private Ref.ContentType contentType;
/*     */   private XSContentType explicitContent;
/*     */   private final boolean mixed;
/*     */   
/*     */   public ComplexTypeImpl(SchemaImpl _parent, AnnotationImpl _annon, Locator _loc, String _name, boolean _anonymous, boolean _abstract, int _derivationMethod, Ref.Type _base, int _final, int _block, boolean _mixed) {
/*  40 */     super(_parent, _annon, _loc, _name, _anonymous);
/*     */     
/*  42 */     if (_base == null) {
/*  43 */       throw new IllegalArgumentException();
/*     */     }
/*  45 */     this._abstract = _abstract;
/*  46 */     this.derivationMethod = _derivationMethod;
/*  47 */     this.baseType = _base;
/*  48 */     this.finalValue = _final;
/*  49 */     this.blockValue = _block;
/*  50 */     this.mixed = _mixed;
/*     */   }
/*     */   
/*  53 */   public XSComplexType asComplexType() { return this; }
/*  54 */   public XSSimpleType asSimpleType() { return null; }
/*  55 */   public final boolean isSimpleType() { return false; } public final boolean isComplexType() {
/*  56 */     return true;
/*     */   }
/*     */   public int getDerivationMethod() {
/*  59 */     return this.derivationMethod;
/*     */   }
/*     */   public XSType getBaseType() {
/*  62 */     return this.baseType.getType();
/*     */   }
/*     */   public void redefine(com.sun.xml.xsom.impl.ComplexTypeImpl ct) {
/*  65 */     if (this.baseType instanceof DelayedRef) {
/*  66 */       ((DelayedRef)this.baseType).redefine((XSDeclaration)ct);
/*     */     } else {
/*  68 */       this.baseType = (Ref.Type)ct;
/*     */     } 
/*     */   }
/*     */   
/*  72 */   public XSElementDecl getScope() { return this.scope; } public void setScope(XSElementDecl _scope) {
/*  73 */     this.scope = _scope;
/*     */   }
/*     */   public boolean isAbstract() {
/*  76 */     return this._abstract;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWildcard(WildcardImpl wc) {
/*  83 */     this.localAttWildcard = wc;
/*     */   }
/*     */   public XSWildcard getAttributeWildcard() {
/*  86 */     WildcardImpl complete = this.localAttWildcard;
/*     */     
/*  88 */     Iterator itr = iterateAttGroups();
/*  89 */     while (itr.hasNext()) {
/*  90 */       WildcardImpl w = (WildcardImpl)((XSAttGroupDecl)itr.next()).getAttributeWildcard();
/*     */       
/*  92 */       if (w == null)
/*     */         continue; 
/*  94 */       if (complete == null) {
/*  95 */         complete = w;
/*     */         
/*     */         continue;
/*     */       } 
/*  99 */       complete = complete.union(this.ownerSchema, w);
/*     */     } 
/*     */     
/* 102 */     if (getDerivationMethod() == 2) return (XSWildcard)complete;
/*     */     
/* 104 */     WildcardImpl base = null;
/* 105 */     XSType baseType = getBaseType();
/* 106 */     if (baseType.asComplexType() != null) {
/* 107 */       base = (WildcardImpl)baseType.asComplexType().getAttributeWildcard();
/*     */     }
/* 109 */     if (complete == null) return (XSWildcard)base; 
/* 110 */     if (base == null) return (XSWildcard)complete;
/*     */     
/* 112 */     return (XSWildcard)complete.union(this.ownerSchema, base);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal(int derivationMethod) {
/* 117 */     return ((this.finalValue & derivationMethod) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSubstitutionProhibited(int method) {
/* 122 */     return ((this.blockValue & method) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentType(Ref.ContentType v) {
/* 127 */     this.contentType = v; } public XSContentType getContentType() {
/* 128 */     return this.contentType.getContentType();
/*     */   }
/*     */   
/*     */   public void setExplicitContent(XSContentType v) {
/* 132 */     this.explicitContent = v;
/*     */   } public XSContentType getExplicitContent() {
/* 134 */     return this.explicitContent;
/*     */   }
/*     */   public boolean isMixed() {
/* 137 */     return this.mixed;
/*     */   }
/*     */   
/*     */   public XSAttributeUse getAttributeUse(String nsURI, String localName) {
/* 141 */     UName name = new UName(nsURI, localName);
/*     */     
/* 143 */     if (this.prohibitedAtts.contains(name)) return null;
/*     */     
/* 145 */     XSAttributeUse o = (XSAttributeUse)this.attributes.get(name);
/*     */ 
/*     */     
/* 148 */     if (o == null) {
/* 149 */       Iterator itr = iterateAttGroups();
/* 150 */       while (itr.hasNext() && o == null) {
/* 151 */         o = ((XSAttGroupDecl)itr.next()).getAttributeUse(nsURI, localName);
/*     */       }
/*     */     } 
/* 154 */     if (o == null) {
/* 155 */       XSType base = getBaseType();
/* 156 */       if (base.asComplexType() != null) {
/* 157 */         o = base.asComplexType().getAttributeUse(nsURI, localName);
/*     */       }
/*     */     } 
/* 160 */     return o;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator iterateAttributeUses() {
/* 165 */     XSComplexType baseType = getBaseType().asComplexType();
/*     */     
/* 167 */     if (baseType == null) return super.iterateAttributeUses();
/*     */     
/* 169 */     return (Iterator)new ConcatIterator((Iterator)new Object(this, baseType.iterateAttributeUses()), super.iterateAttributeUses());
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
/*     */   public XSType[] listSubstitutables() {
/* 182 */     return Util.listSubstitutables((XSType)this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/* 188 */     visitor.complexType(this);
/*     */   }
/*     */   public Object apply(XSFunction function) {
/* 191 */     return function.complexType(this);
/*     */   }
/*     */   
/*     */   public XSType getType() {
/* 195 */     return (XSType)this; } public XSComplexType getComplexType() {
/* 196 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\ComplexTypeImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
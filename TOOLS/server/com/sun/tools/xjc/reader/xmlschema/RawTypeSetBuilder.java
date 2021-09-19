/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CAdapter;
/*     */ import com.sun.tools.xjc.model.CClass;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.CCustomizations;
/*     */ import com.sun.tools.xjc.model.CElement;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CNonElement;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.model.Multiplicity;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDom;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXSubstitutable;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
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
/*     */ public class RawTypeSetBuilder
/*     */   implements XSTermVisitor
/*     */ {
/*     */   public static RawTypeSet build(XSParticle p, boolean optional) {
/*  84 */     RawTypeSetBuilder rtsb = new RawTypeSetBuilder();
/*  85 */     rtsb.particle(p);
/*  86 */     Multiplicity mul = MultiplicityCounter.theInstance.particle(p);
/*     */     
/*  88 */     if (optional) {
/*  89 */       mul = mul.makeOptional();
/*     */     }
/*  91 */     return new RawTypeSet(rtsb.refs, mul);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private final Set<QName> elementNames = new HashSet<QName>();
/*     */   
/* 102 */   private final Set<RawTypeSet.Ref> refs = new HashSet<RawTypeSet.Ref>();
/*     */   
/* 104 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<RawTypeSet.Ref> getRefs() {
/* 113 */     return this.refs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void particle(XSParticle p) {
/* 121 */     BIDom dom = this.builder.getLocalDomCustomization(p);
/* 122 */     if (dom != null) {
/* 123 */       dom.markAsAcknowledged();
/* 124 */       this.refs.add(new WildcardRef(WildcardMode.SKIP));
/*     */     } else {
/* 126 */       p.getTerm().visit(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 131 */     this.refs.add(new WildcardRef(wc));
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 135 */     modelGroup(decl.getModelGroup());
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 139 */     for (XSParticle p : group.getChildren()) {
/* 140 */       particle(p);
/*     */     }
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 145 */     QName n = BGMBuilder.getName((XSDeclaration)decl);
/* 146 */     if (this.elementNames.add(n)) {
/* 147 */       CElement elementBean = ((ClassSelector)Ring.get(ClassSelector.class)).bindToType(decl, (XSComponent)null);
/* 148 */       if (elementBean == null) {
/* 149 */         this.refs.add(new XmlTypeRef(decl));
/*     */       
/*     */       }
/* 152 */       else if (elementBean instanceof CClass) {
/* 153 */         this.refs.add(new CClassRef(decl, (CClass)elementBean));
/*     */       } else {
/* 155 */         this.refs.add(new CElementInfoRef(decl, (CElementInfo)elementBean));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class WildcardRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     private final WildcardMode mode;
/*     */     
/*     */     WildcardRef(XSWildcard wildcard) {
/* 167 */       this.mode = getMode(wildcard);
/*     */     }
/*     */     WildcardRef(WildcardMode mode) {
/* 170 */       this.mode = mode;
/*     */     }
/*     */     
/*     */     private static WildcardMode getMode(XSWildcard wildcard) {
/* 174 */       switch (wildcard.getMode()) {
/*     */         case 1:
/* 176 */           return WildcardMode.LAX;
/*     */         case 2:
/* 178 */           return WildcardMode.STRICT;
/*     */         case 3:
/* 180 */           return WildcardMode.SKIP;
/*     */       } 
/* 182 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 188 */       throw new IllegalStateException();
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 192 */       prop.setWildcard(this.mode);
/*     */     }
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 196 */       return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 200 */       return false;
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 204 */       return ID.NONE;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class CClassRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     public final CClass target;
/*     */     public final XSElementDecl decl;
/*     */     
/*     */     CClassRef(XSElementDecl decl, CClass target) {
/* 216 */       this.decl = decl;
/* 217 */       this.target = target;
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 221 */       return new CTypeRef((CNonElement)this.target, this.decl);
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 225 */       prop.getElements().add(this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 230 */       if (this.decl.getSubstitutables().size() > 1) {
/* 231 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/* 233 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 237 */       return false;
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 241 */       return ID.NONE;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final class CElementInfoRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     public final CElementInfo target;
/*     */     public final XSElementDecl decl;
/*     */     
/*     */     CElementInfoRef(XSElementDecl decl, CElementInfo target) {
/* 253 */       this.decl = decl;
/* 254 */       this.target = target;
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 258 */       assert !this.target.isCollection();
/* 259 */       CAdapter a = this.target.getProperty().getAdapter();
/* 260 */       if (a != null && ep != null) ep.setAdapter(a);
/*     */       
/* 262 */       return new CTypeRef(this.target.getContentType(), this.decl);
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 266 */       prop.getElements().add(this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 271 */       if (this.decl.getSubstitutables().size() > 1) {
/* 272 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/* 274 */       BIXSubstitutable subst = (BIXSubstitutable)RawTypeSetBuilder.this.builder.getBindInfo((XSComponent)this.decl).get(BIXSubstitutable.class);
/* 275 */       if (subst != null) {
/* 276 */         subst.markAsAcknowledged();
/* 277 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       } 
/*     */ 
/*     */       
/* 281 */       CElementPropertyInfo p = this.target.getProperty();
/*     */ 
/*     */ 
/*     */       
/* 285 */       if ((parent.refs.size() > 1 || !parent.mul.isAtMostOnce()) && p.id() != ID.NONE)
/* 286 */         return RawTypeSet.Mode.MUST_BE_REFERENCE; 
/* 287 */       if (parent.refs.size() > 1 && p.getAdapter() != null) {
/* 288 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/* 290 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 294 */       return this.target.getProperty().isValueList();
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 298 */       return this.target.getProperty().id();
/*     */     }
/*     */     
/*     */     protected MimeType getExpectedMimeType() {
/* 302 */       return this.target.getProperty().getExpectedMimeType();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class XmlTypeRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     private final XSElementDecl decl;
/*     */     private final TypeUse target;
/*     */     
/*     */     public XmlTypeRef(XSElementDecl decl) {
/* 314 */       this.decl = decl;
/* 315 */       SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/* 316 */       stb.refererStack.push(decl);
/* 317 */       TypeUse r = ((ClassSelector)Ring.get(ClassSelector.class)).bindToType(decl.getType(), (XSComponent)decl);
/* 318 */       stb.refererStack.pop();
/* 319 */       this.target = r;
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 323 */       if (ep != null && this.target.getAdapterUse() != null)
/* 324 */         ep.setAdapter(this.target.getAdapterUse()); 
/* 325 */       return new CTypeRef(this.target.getInfo(), this.decl);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 336 */       CClassInfo scope = ((ClassSelector)Ring.get(ClassSelector.class)).getCurrentBean();
/* 337 */       Model model = (Model)Ring.get(Model.class);
/*     */       
/* 339 */       CCustomizations custs = ((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo((XSComponent)this.decl).toCustomizationList();
/*     */       
/* 341 */       if (this.target instanceof CClassInfo && ((BIGlobalBinding)Ring.get(BIGlobalBinding.class)).isSimpleMode()) {
/* 342 */         CClassInfo bean = new CClassInfo(model, (CClassInfoParent)scope, model.getNameConverter().toClassName(this.decl.getName()), this.decl.getLocator(), null, BGMBuilder.getName((XSDeclaration)this.decl), (XSComponent)this.decl, custs);
/*     */ 
/*     */ 
/*     */         
/* 346 */         bean.setBaseClass((CClass)this.target);
/* 347 */         prop.getElements().add(bean);
/*     */       } else {
/* 349 */         CElementInfo e = new CElementInfo(model, BGMBuilder.getName((XSDeclaration)this.decl), (CClassInfoParent)scope, this.target, this.decl.getDefaultValue(), this.decl, custs, this.decl.getLocator());
/*     */         
/* 351 */         prop.getElements().add(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 359 */       if ((parent.refs.size() > 1 || !parent.mul.isAtMostOnce()) && this.target.idUse() != ID.NONE)
/* 360 */         return RawTypeSet.Mode.MUST_BE_REFERENCE; 
/* 361 */       if (parent.refs.size() > 1 && this.target.getAdapterUse() != null) {
/* 362 */         return RawTypeSet.Mode.MUST_BE_REFERENCE;
/*     */       }
/*     */ 
/*     */       
/* 366 */       if (this.decl.isNillable() && parent.mul.isOptional()) {
/* 367 */         return RawTypeSet.Mode.CAN_BE_TYPEREF;
/*     */       }
/* 369 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 373 */       return this.target.isCollection();
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 377 */       return this.target.idUse();
/*     */     }
/*     */     
/*     */     protected MimeType getExpectedMimeType() {
/* 381 */       return this.target.getExpectedMimeType();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\RawTypeSetBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
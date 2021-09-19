/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.model.CClass;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.CClassRef;
/*     */ import com.sun.tools.xjc.model.CCustomizations;
/*     */ import com.sun.tools.xjc.model.CElement;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIClass;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXSubstitutable;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DefaultClassBinder
/*     */   implements ClassBinder
/*     */ {
/*  92 */   private final SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*  93 */   private final Model model = (Model)Ring.get(Model.class);
/*     */   
/*  95 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*  96 */   protected final ClassSelector selector = (ClassSelector)Ring.get(ClassSelector.class);
/*     */ 
/*     */   
/*     */   public CElement attGroupDecl(XSAttGroupDecl decl) {
/* 100 */     return allow((XSComponent)decl, decl.getName());
/*     */   }
/*     */   
/*     */   public CElement attributeDecl(XSAttributeDecl decl) {
/* 104 */     return allow((XSComponent)decl, decl.getName());
/*     */   }
/*     */   
/*     */   public CElement modelGroup(XSModelGroup mgroup) {
/* 108 */     return (CElement)never();
/*     */   }
/*     */   
/*     */   public CElement modelGroupDecl(XSModelGroupDecl decl) {
/* 112 */     return (CElement)never();
/*     */   }
/*     */   public CElement complexType(XSComplexType type) {
/*     */     String className;
/*     */     CClassInfoParent scope;
/* 117 */     CElement ci = allow((XSComponent)type, type.getName());
/* 118 */     if (ci != null) return ci;
/*     */ 
/*     */ 
/*     */     
/* 122 */     BindInfo bi = this.builder.getBindInfo((XSComponent)type);
/*     */     
/* 124 */     if (type.isGlobal()) {
/* 125 */       QName tagName = null;
/* 126 */       String str = deriveName(type);
/* 127 */       Locator loc = type.getLocator();
/*     */       
/* 129 */       if (getGlobalBinding().isSimpleMode()) {
/*     */         
/* 131 */         XSElementDecl referer = getSoleElementReferer((XSType)type);
/* 132 */         if (referer != null && isCollapsable(referer)) {
/*     */ 
/*     */ 
/*     */           
/* 136 */           tagName = BGMBuilder.getName((XSDeclaration)referer);
/* 137 */           str = deriveName((XSDeclaration)referer);
/* 138 */           loc = referer.getLocator();
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 144 */       JPackage pkg = this.selector.getPackage(type.getTargetNamespace());
/*     */       
/* 146 */       return (CElement)new CClassInfo(this.model, pkg, str, loc, getTypeName(type), tagName, (XSComponent)type, bi.toCustomizationList());
/*     */     } 
/* 148 */     XSElementDecl element = type.getScope();
/*     */     
/* 150 */     if (element.isGlobal() && isCollapsable(element)) {
/* 151 */       if (this.builder.getBindInfo((XSComponent)element).get(BIClass.class) != null)
/*     */       {
/*     */         
/* 154 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       return (CElement)new CClassInfo(this.model, this.selector.getClassScope(), deriveName((XSDeclaration)element), element.getLocator(), null, BGMBuilder.getName((XSDeclaration)element), (XSComponent)element, bi.toCustomizationList());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     CElement parentType = this.selector.isBound(element, (XSComponent)type);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     if (parentType != null && parentType instanceof CElementInfo && ((CElementInfo)parentType).hasClass()) {
/*     */ 
/*     */ 
/*     */       
/* 177 */       CElementInfo cElementInfo = (CElementInfo)parentType;
/* 178 */       className = "Type";
/*     */     }
/*     */     else {
/*     */       
/* 182 */       className = this.builder.getNameConverter().toClassName(element.getName());
/*     */       
/* 184 */       BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)type.getOwnerSchema()).get(BISchemaBinding.class);
/*     */       
/* 186 */       if (sb != null) className = sb.mangleAnonymousTypeClassName(className); 
/* 187 */       scope = this.selector.getClassScope();
/*     */     } 
/*     */     
/* 190 */     return (CElement)new CClassInfo(this.model, scope, className, type.getLocator(), null, null, (XSComponent)type, bi.toCustomizationList());
/*     */   }
/*     */ 
/*     */   
/*     */   private QName getTypeName(XSComplexType type) {
/* 195 */     if (type.getRedefinedBy() != null) {
/* 196 */       return null;
/*     */     }
/* 198 */     return BGMBuilder.getName((XSDeclaration)type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCollapsable(XSElementDecl decl) {
/* 206 */     XSType type = decl.getType();
/*     */     
/* 208 */     if (!type.isComplexType()) {
/* 209 */       return false;
/*     */     }
/* 211 */     if (decl.getSubstitutables().size() > 1 || decl.getSubstAffiliation() != null)
/*     */     {
/* 213 */       return false;
/*     */     }
/* 215 */     if (decl.isNillable())
/*     */     {
/* 217 */       return false;
/*     */     }
/* 219 */     BIXSubstitutable bixSubstitutable = (BIXSubstitutable)this.builder.getBindInfo((XSComponent)decl).get(BIXSubstitutable.class);
/* 220 */     if (bixSubstitutable != null) {
/*     */ 
/*     */       
/* 223 */       bixSubstitutable.markAsAcknowledged();
/* 224 */       return false;
/*     */     } 
/*     */     
/* 227 */     if (getGlobalBinding().isSimpleMode() && decl.isGlobal()) {
/*     */ 
/*     */       
/* 230 */       XSElementDecl referer = getSoleElementReferer(decl.getType());
/* 231 */       if (referer != null) {
/* 232 */         assert referer == decl;
/* 233 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 237 */     if (!type.isLocal() || !type.isComplexType()) {
/* 238 */       return false;
/*     */     }
/* 240 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private XSElementDecl getSoleElementReferer(@NotNull XSType t) {
/* 248 */     Set<XSComponent> referer = this.builder.getReferer(t);
/*     */     
/* 250 */     XSElementDecl sole = null;
/* 251 */     for (XSComponent r : referer) {
/* 252 */       if (r instanceof XSElementDecl) {
/* 253 */         XSElementDecl x = (XSElementDecl)r;
/* 254 */         if (!x.isGlobal()) {
/*     */           continue;
/*     */         }
/*     */         
/* 258 */         if (sole == null) { sole = x; continue; }
/* 259 */          return null;
/*     */       } 
/*     */ 
/*     */       
/* 263 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 267 */     return sole;
/*     */   }
/*     */   public CElement elementDecl(XSElementDecl decl) {
/*     */     CElementInfo cElementInfo;
/* 271 */     CElement r = allow((XSComponent)decl, decl.getName());
/*     */     
/* 273 */     if (r == null) {
/* 274 */       QName tagName = BGMBuilder.getName((XSDeclaration)decl);
/* 275 */       CCustomizations custs = this.builder.getBindInfo((XSComponent)decl).toCustomizationList();
/*     */       
/* 277 */       if (decl.isGlobal()) {
/* 278 */         if (isCollapsable(decl))
/*     */         {
/*     */           
/* 281 */           return (CElement)this.selector.bindToType(decl.getType().asComplexType(), (XSComponent)decl, true);
/*     */         }
/* 283 */         String className = null;
/* 284 */         if (getGlobalBinding().isGenerateElementClass()) {
/* 285 */           className = deriveName((XSDeclaration)decl);
/*     */         }
/*     */         
/* 288 */         CElementInfo cei = new CElementInfo(this.model, tagName, this.selector.getClassScope(), className, custs, decl.getLocator());
/*     */         
/* 290 */         this.selector.boundElements.put(decl, cei);
/*     */         
/* 292 */         this.stb.refererStack.push(decl);
/* 293 */         cei.initContentType(this.selector.bindToType(decl.getType(), (XSComponent)decl), decl, decl.getDefaultValue());
/* 294 */         this.stb.refererStack.pop();
/* 295 */         cElementInfo = cei;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 301 */     XSElementDecl top = decl.getSubstAffiliation();
/* 302 */     if (top != null) {
/* 303 */       CElement topci = this.selector.bindToType(top, (XSComponent)decl);
/*     */       
/* 305 */       if (cElementInfo instanceof CClassInfo && topci instanceof CClassInfo)
/* 306 */         ((CClassInfo)cElementInfo).setBaseClass((CClass)topci); 
/* 307 */       if (cElementInfo instanceof CElementInfo && topci instanceof CElementInfo) {
/* 308 */         cElementInfo.setSubstitutionHead((CElementInfo)topci);
/*     */       }
/*     */     } 
/* 311 */     return (CElement)cElementInfo;
/*     */   }
/*     */   public CClassInfo empty(XSContentType ct) {
/* 314 */     return null;
/*     */   }
/*     */   public CClassInfo identityConstraint(XSIdentityConstraint xsIdentityConstraint) {
/* 317 */     return never();
/*     */   }
/*     */   
/*     */   public CClassInfo xpath(XSXPath xsxPath) {
/* 321 */     return never();
/*     */   }
/*     */   
/*     */   public CClassInfo attributeUse(XSAttributeUse use) {
/* 325 */     return never();
/*     */   }
/*     */   
/*     */   public CElement simpleType(XSSimpleType type) {
/* 329 */     CElement c = allow((XSComponent)type, type.getName());
/* 330 */     if (c != null) return c;
/*     */     
/* 332 */     if (getGlobalBinding().isSimpleTypeSubstitution() && type.isGlobal()) {
/* 333 */       return (CElement)new CClassInfo(this.model, this.selector.getClassScope(), deriveName((XSDeclaration)type), type.getLocator(), BGMBuilder.getName((XSDeclaration)type), null, (XSComponent)type, null);
/*     */     }
/*     */ 
/*     */     
/* 337 */     return (CElement)never();
/*     */   }
/*     */   
/*     */   public CClassInfo particle(XSParticle particle) {
/* 341 */     return never();
/*     */   }
/*     */   
/*     */   public CClassInfo wildcard(XSWildcard wc) {
/* 345 */     return never();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CClassInfo annotation(XSAnnotation annon) {
/*     */     assert false;
/* 352 */     return null;
/*     */   }
/*     */   
/*     */   public CClassInfo notation(XSNotation not) {
/*     */     assert false;
/* 357 */     return null;
/*     */   }
/*     */   
/*     */   public CClassInfo facet(XSFacet decl) {
/*     */     assert false;
/* 362 */     return null;
/*     */   }
/*     */   public CClassInfo schema(XSSchema schema) {
/*     */     assert false;
/* 366 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CClassInfo never() {
/* 397 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private CElement allow(XSComponent component, String defaultBaseName) {
/* 413 */     BindInfo bindInfo = this.builder.getBindInfo(component);
/* 414 */     BIClass decl = (BIClass)bindInfo.get(BIClass.class);
/* 415 */     if (decl == null) return null;
/*     */     
/* 417 */     decl.markAsAcknowledged();
/*     */ 
/*     */     
/* 420 */     String ref = decl.getExistingClassRef();
/* 421 */     if (ref != null) {
/* 422 */       if (!JJavaName.isFullyQualifiedClassName(ref)) {
/* 423 */         ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(decl.getLocation(), Messages.format("ClassSelector.IncorrectClassName", new Object[] { ref }));
/*     */       }
/*     */       else {
/*     */         
/* 427 */         if (component instanceof XSComplexType)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 436 */           ((ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class)).recordBindingMode((XSComplexType)component, ComplexTypeBindingMode.NORMAL);
/*     */         }
/*     */ 
/*     */         
/* 440 */         return (CElement)new CClassRef(this.model, component, decl, bindInfo.toCustomizationList());
/*     */       } 
/*     */     }
/*     */     
/* 444 */     String clsName = decl.getClassName();
/* 445 */     if (clsName == null) {
/*     */ 
/*     */       
/* 448 */       if (defaultBaseName == null) {
/* 449 */         ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(decl.getLocation(), Messages.format("ClassSelector.ClassNameIsRequired", new Object[0]));
/*     */ 
/*     */ 
/*     */         
/* 453 */         defaultBaseName = "undefined" + component.hashCode();
/*     */       } 
/* 455 */       clsName = this.builder.deriveName(defaultBaseName, component);
/*     */     }
/* 457 */     else if (!JJavaName.isJavaIdentifier(clsName)) {
/*     */       
/* 459 */       ((ErrorReceiver)Ring.get(ErrorReceiver.class)).error(decl.getLocation(), Messages.format("ClassSelector.IncorrectClassName", new Object[] { clsName }));
/*     */ 
/*     */       
/* 462 */       clsName = "Undefined" + component.hashCode();
/*     */     } 
/*     */ 
/*     */     
/* 466 */     QName typeName = null;
/* 467 */     QName elementName = null;
/*     */     
/* 469 */     if (component instanceof XSType) {
/* 470 */       XSType t = (XSType)component;
/* 471 */       typeName = BGMBuilder.getName((XSDeclaration)t);
/*     */     } 
/*     */     
/* 474 */     if (component instanceof XSElementDecl) {
/* 475 */       XSElementDecl e = (XSElementDecl)component;
/* 476 */       elementName = BGMBuilder.getName((XSDeclaration)e);
/*     */     } 
/*     */     
/* 479 */     if (component instanceof XSElementDecl && !isCollapsable((XSElementDecl)component)) {
/* 480 */       XSElementDecl e = (XSElementDecl)component;
/*     */       
/* 482 */       CElementInfo cei = new CElementInfo(this.model, elementName, this.selector.getClassScope(), clsName, bindInfo.toCustomizationList(), decl.getLocation());
/*     */ 
/*     */       
/* 485 */       this.selector.boundElements.put(e, cei);
/*     */       
/* 487 */       this.stb.refererStack.push(component);
/* 488 */       cei.initContentType(this.selector.bindToType(e.getType(), (XSComponent)e), e, e.getDefaultValue());
/*     */ 
/*     */       
/* 491 */       this.stb.refererStack.pop();
/* 492 */       return (CElement)cei;
/*     */     } 
/*     */     
/* 495 */     CClassInfo bt = new CClassInfo(this.model, this.selector.getClassScope(), clsName, decl.getLocation(), typeName, elementName, component, bindInfo.toCustomizationList());
/*     */ 
/*     */ 
/*     */     
/* 499 */     if (decl.getJavadoc() != null) {
/* 500 */       bt.javadoc = decl.getJavadoc() + "\n\n";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 506 */     String implClass = decl.getUserSpecifiedImplClass();
/* 507 */     if (implClass != null) {
/* 508 */       bt.setUserSpecifiedImplClass(implClass);
/*     */     }
/* 510 */     return (CElement)bt;
/*     */   }
/*     */ 
/*     */   
/*     */   private BIGlobalBinding getGlobalBinding() {
/* 515 */     return this.builder.getGlobalBinding();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String deriveName(XSDeclaration comp) {
/* 523 */     return this.builder.deriveName(comp.getName(), (XSComponent)comp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String deriveName(XSComplexType comp) {
/* 532 */     String seed = this.builder.deriveName(comp.getName(), (XSComponent)comp);
/* 533 */     int cnt = comp.getRedefinedCount();
/* 534 */     for (; cnt > 0; cnt--)
/* 535 */       seed = "Original" + seed; 
/* 536 */     return seed;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\DefaultClassBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.tools.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.ClassType;
/*     */ import com.sun.codemodel.JAnnotatable;
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JEnumConstant;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JForEach;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.codemodel.fmt.JStaticJavaFile;
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.tools.xjc.AbortException;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.api.SpecVersion;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlAnyAttributeWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlEnumValueWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlEnumWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlJavaTypeAdapterWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlMimeTypeWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlRootElementWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlSeeAlsoWriter;
/*     */ import com.sun.tools.xjc.generator.annotation.spec.XmlTypeWriter;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.model.CAdapter;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.CClassRef;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.ElementOutline;
/*     */ import com.sun.tools.xjc.outline.EnumConstantOutline;
/*     */ import com.sun.tools.xjc.outline.EnumOutline;
/*     */ import com.sun.tools.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.outline.PackageOutline;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
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
/*     */ public final class BeanGenerator
/*     */   implements Outline
/*     */ {
/*     */   private final CodeModelClassFactory codeModelClassFactory;
/*     */   private final ErrorReceiver errorReceiver;
/* 127 */   private final Map<JPackage, PackageOutlineImpl> packageContexts = new HashMap<JPackage, PackageOutlineImpl>();
/*     */ 
/*     */   
/* 130 */   private final Map<CClassInfo, ClassOutlineImpl> classes = new HashMap<CClassInfo, ClassOutlineImpl>();
/*     */ 
/*     */   
/* 133 */   private final Map<CEnumLeafInfo, EnumOutline> enums = new HashMap<CEnumLeafInfo, EnumOutline>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private final Map<Class, JClass> generatedRuntime = (Map)new HashMap<Class<?>, JClass>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Model model;
/*     */ 
/*     */   
/*     */   private final JCodeModel codeModel;
/*     */ 
/*     */   
/* 148 */   private final Map<CPropertyInfo, FieldOutline> fields = new HashMap<CPropertyInfo, FieldOutline>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   final Map<CElementInfo, ElementOutlineImpl> elements = new HashMap<CElementInfo, ElementOutlineImpl>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CClassInfoParent.Visitor<JClassContainer> exposedContainerBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CClassInfoParent.Visitor<JClassContainer> implContainerBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Outline generate(Model model, ErrorReceiver _errorReceiver) {
/*     */     try {
/* 174 */       return new BeanGenerator(model, _errorReceiver);
/* 175 */     } catch (AbortException e) {
/* 176 */       return null;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateClassList() {
/*     */     try {
/*     */       StringBuilder buf;
/* 256 */       JDefinedClass jc = this.codeModel.rootPackage()._class("JAXBDebug");
/* 257 */       JMethod m = jc.method(17, JAXBContext.class, "createContext");
/* 258 */       JVar $classLoader = m.param(ClassLoader.class, "classLoader");
/* 259 */       m._throws(JAXBException.class);
/* 260 */       JInvocation inv = this.codeModel.ref(JAXBContext.class).staticInvoke("newInstance");
/* 261 */       m.body()._return((JExpression)inv);
/*     */       
/* 263 */       switch (this.model.strategy) {
/*     */         
/*     */         case ID:
/* 266 */           buf = new StringBuilder();
/* 267 */           for (PackageOutlineImpl po : this.packageContexts.values()) {
/* 268 */             if (buf.length() > 0) buf.append(':'); 
/* 269 */             buf.append(po._package().name());
/*     */           } 
/* 271 */           inv.arg(buf.toString()).arg((JExpression)$classLoader);
/*     */           return;
/*     */         
/*     */         case IDREF:
/* 275 */           for (ClassOutlineImpl cc : getClasses())
/* 276 */             inv.arg(cc.implRef.dotclass()); 
/* 277 */           for (PackageOutlineImpl po : this.packageContexts.values())
/* 278 */             inv.arg(po.objectFactory().dotclass()); 
/*     */           return;
/*     */       } 
/* 281 */       throw new IllegalStateException();
/*     */     }
/* 283 */     catch (JClassAlreadyExistsException e) {
/* 284 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model getModel() {
/* 292 */     return this.model;
/*     */   }
/*     */   
/*     */   public JCodeModel getCodeModel() {
/* 296 */     return this.codeModel;
/*     */   }
/*     */   
/*     */   public JClassContainer getContainer(CClassInfoParent parent, Aspect aspect) {
/*     */     CClassInfoParent.Visitor<JClassContainer> v;
/* 301 */     switch (aspect) {
/*     */       case ID:
/* 303 */         v = this.exposedContainerBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 312 */         return (JClassContainer)parent.accept(v);case IDREF: v = this.implContainerBuilder; return (JClassContainer)parent.accept(v);
/*     */     } 
/*     */     assert false;
/*     */     throw new IllegalStateException(); } public final JType resolve(CTypeRef ref, Aspect a) {
/* 316 */     return ((NType)ref.getTarget().getType()).toType(this, a);
/*     */   }
/*     */   
/* 319 */   private BeanGenerator(Model _model, ErrorReceiver _errorReceiver) { this.exposedContainerBuilder = new CClassInfoParent.Visitor<JClassContainer>()
/*     */       {
/*     */         public JClassContainer onBean(CClassInfo bean) {
/* 322 */           return (JClassContainer)(BeanGenerator.this.getClazz(bean)).ref;
/*     */         }
/*     */ 
/*     */         
/*     */         public JClassContainer onElement(CElementInfo element) {
/* 327 */           return (JClassContainer)(BeanGenerator.this.getElement(element)).implClass;
/*     */         }
/*     */         
/*     */         public JClassContainer onPackage(JPackage pkg) {
/* 331 */           return (JClassContainer)BeanGenerator.this.model.strategy.getPackage(pkg, Aspect.EXPOSED);
/*     */         }
/*     */       };
/*     */     
/* 335 */     this.implContainerBuilder = new CClassInfoParent.Visitor<JClassContainer>()
/*     */       {
/*     */         public JClassContainer onBean(CClassInfo bean) {
/* 338 */           return (JClassContainer)(BeanGenerator.this.getClazz(bean)).implClass;
/*     */         }
/*     */         
/*     */         public JClassContainer onElement(CElementInfo element) {
/* 342 */           return (JClassContainer)(BeanGenerator.this.getElement(element)).implClass;
/*     */         }
/*     */         
/*     */         public JClassContainer onPackage(JPackage pkg) {
/* 346 */           return (JClassContainer)BeanGenerator.this.model.strategy.getPackage(pkg, Aspect.IMPLEMENTATION); } }; this.model = _model; this.codeModel = this.model.codeModel; this.errorReceiver = _errorReceiver; this.codeModelClassFactory = new CodeModelClassFactory(this.errorReceiver); for (CEnumLeafInfo p : this.model.enums().values())
/*     */       this.enums.put(p, generateEnumDef(p));  JPackage[] packages = getUsedPackages(Aspect.EXPOSED); for (JPackage pkg : packages)
/*     */       getPackageContext(pkg);  for (CClassInfo bean : this.model.beans().values())
/*     */       getClazz(bean);  for (PackageOutlineImpl p : this.packageContexts.values())
/*     */       p.calcDefaultValues();  JClass OBJECT = this.codeModel.ref(Object.class); for (ClassOutlineImpl cc : getClasses()) {
/*     */       CClassInfo superClass = cc.target.getBaseClass(); if (superClass != null) {
/*     */         this.model.strategy._extends(cc, getClazz(superClass)); continue;
/*     */       }  CClassRef refSuperClass = cc.target.getRefBaseClass(); if (refSuperClass != null) {
/*     */         cc.implClass._extends(refSuperClass.toType(this, Aspect.EXPOSED)); continue;
/*     */       }  if (this.model.rootClass != null && cc.implClass._extends().equals(OBJECT))
/*     */         cc.implClass._extends(this.model.rootClass);  if (this.model.rootInterface != null)
/*     */         cc.ref._implements(this.model.rootInterface); 
/*     */     }  for (ClassOutlineImpl co : getClasses())
/*     */       generateClassBody(co); 
/*     */     for (EnumOutline eo : this.enums.values())
/*     */       generateEnumBody(eo); 
/*     */     for (CElementInfo ei : this.model.getAllElements())
/*     */       getPackageContext(ei._package()).objectFactoryGenerator().populate(ei); 
/*     */     if (this.model.options.debugMode)
/* 365 */       generateClassList();  } public final JPackage[] getUsedPackages(Aspect aspect) { Set<JPackage> s = new TreeSet<JPackage>();
/*     */     
/* 367 */     for (CClassInfo bean : this.model.beans().values()) {
/* 368 */       JClassContainer cont = getContainer(bean.parent(), aspect);
/* 369 */       if (cont.isPackage()) {
/* 370 */         s.add((JPackage)cont);
/*     */       }
/*     */     } 
/* 373 */     for (CElementInfo e : this.model.getElementMappings(null).values())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 378 */       s.add(e._package());
/*     */     }
/*     */     
/* 381 */     return s.<JPackage>toArray(new JPackage[s.size()]); }
/*     */   
/*     */   public ErrorReceiver getErrorReceiver() {
/* 384 */     return this.errorReceiver;
/*     */   } public CodeModelClassFactory getClassFactory() {
/* 386 */     return this.codeModelClassFactory;
/*     */   }
/*     */   public PackageOutlineImpl getPackageContext(JPackage p) {
/* 389 */     PackageOutlineImpl r = this.packageContexts.get(p);
/* 390 */     if (r == null) {
/* 391 */       r = new PackageOutlineImpl(this, this.model, p);
/* 392 */       this.packageContexts.put(p, r);
/*     */     } 
/* 394 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassOutlineImpl generateClassDef(CClassInfo bean) {
/*     */     JDefinedClass jDefinedClass;
/* 402 */     ImplStructureStrategy.Result r = this.model.strategy.createClasses(this, bean);
/*     */ 
/*     */     
/* 405 */     if (bean.getUserSpecifiedImplClass() != null) {
/*     */       JDefinedClass jDefinedClass1;
/*     */       
/*     */       try {
/* 409 */         jDefinedClass1 = this.codeModel._class(bean.getUserSpecifiedImplClass());
/*     */         
/* 411 */         jDefinedClass1.hide();
/* 412 */       } catch (JClassAlreadyExistsException e) {
/*     */         
/* 414 */         jDefinedClass1 = e.getExistingClass();
/*     */       } 
/* 416 */       jDefinedClass1._extends((JClass)r.implementation);
/* 417 */       jDefinedClass = jDefinedClass1;
/*     */     } else {
/* 419 */       jDefinedClass = r.implementation;
/*     */     } 
/* 421 */     return new ClassOutlineImpl(this, bean, r.exposed, r.implementation, (JClass)jDefinedClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ClassOutlineImpl> getClasses() {
/* 427 */     assert this.model.beans().size() == this.classes.size();
/* 428 */     return this.classes.values();
/*     */   }
/*     */   
/*     */   public ClassOutlineImpl getClazz(CClassInfo bean) {
/* 432 */     ClassOutlineImpl r = this.classes.get(bean);
/* 433 */     if (r == null)
/* 434 */       this.classes.put(bean, r = generateClassDef(bean)); 
/* 435 */     return r;
/*     */   }
/*     */   
/*     */   public ElementOutlineImpl getElement(CElementInfo ei) {
/* 439 */     ElementOutlineImpl def = this.elements.get(ei);
/* 440 */     if (def == null && ei.hasClass())
/*     */     {
/* 442 */       def = new ElementOutlineImpl(this, ei);
/*     */     }
/* 444 */     return def;
/*     */   }
/*     */   
/*     */   public EnumOutline getEnum(CEnumLeafInfo eli) {
/* 448 */     return this.enums.get(eli);
/*     */   }
/*     */   
/*     */   public Collection<EnumOutline> getEnums() {
/* 452 */     return this.enums.values();
/*     */   }
/*     */   
/*     */   public Iterable<? extends PackageOutline> getAllPackageContexts() {
/* 456 */     return this.packageContexts.values();
/*     */   }
/*     */   
/*     */   public FieldOutline getField(CPropertyInfo prop) {
/* 460 */     return this.fields.get(prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateClassBody(ClassOutlineImpl cc) {
/* 468 */     CClassInfo target = cc.target;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 477 */     if (this.model.serializable) {
/* 478 */       cc.implClass._implements(Serializable.class);
/* 479 */       if (this.model.serialVersionUID != null) {
/* 480 */         cc.implClass.field(28, (JType)this.codeModel.LONG, "serialVersionUID", JExpr.lit(this.model.serialVersionUID.longValue()));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 489 */     String mostUsedNamespaceURI = cc._package().getMostUsedNamespaceURI();
/*     */ 
/*     */ 
/*     */     
/* 493 */     XmlTypeWriter xtw = (XmlTypeWriter)cc.implClass.annotate2(XmlTypeWriter.class);
/* 494 */     writeTypeName(cc.target.getTypeName(), xtw, mostUsedNamespaceURI);
/*     */     
/* 496 */     if (this.model.options.target.isLaterThan(SpecVersion.V2_1)) {
/*     */       
/* 498 */       Iterator<CClassInfo> subclasses = cc.target.listSubclasses();
/* 499 */       if (subclasses.hasNext()) {
/* 500 */         XmlSeeAlsoWriter saw = (XmlSeeAlsoWriter)cc.implClass.annotate2(XmlSeeAlsoWriter.class);
/* 501 */         while (subclasses.hasNext()) {
/* 502 */           CClassInfo s = subclasses.next();
/* 503 */           saw.value((JType)(getClazz(s)).implRef);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 508 */     if (target.isElement()) {
/* 509 */       String namespaceURI = target.getElementName().getNamespaceURI();
/* 510 */       String localPart = target.getElementName().getLocalPart();
/*     */ 
/*     */ 
/*     */       
/* 514 */       XmlRootElementWriter xrew = (XmlRootElementWriter)cc.implClass.annotate2(XmlRootElementWriter.class);
/* 515 */       xrew.name(localPart);
/* 516 */       if (!namespaceURI.equals(mostUsedNamespaceURI)) {
/* 517 */         xrew.namespace(namespaceURI);
/*     */       }
/*     */     } 
/* 520 */     if (target.isOrdered()) {
/* 521 */       for (CPropertyInfo p : target.getProperties()) {
/* 522 */         if (!(p instanceof com.sun.tools.xjc.model.CAttributePropertyInfo)) {
/* 523 */           xtw.propOrder(p.getName(false));
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 528 */       xtw.getAnnotationUse().paramArray("propOrder");
/*     */     } 
/*     */     
/* 531 */     for (CPropertyInfo prop : target.getProperties()) {
/* 532 */       generateFieldDecl(cc, prop);
/*     */     }
/*     */     
/* 535 */     if (target.declaresAttributeWildcard()) {
/* 536 */       generateAttributeWildcard(cc);
/*     */     }
/*     */ 
/*     */     
/* 540 */     cc.ref.javadoc().append(target.javadoc);
/*     */     
/* 542 */     cc._package().objectFactoryGenerator().populate(cc);
/*     */   }
/*     */   
/*     */   private void writeTypeName(QName typeName, XmlTypeWriter xtw, String mostUsedNamespaceURI) {
/* 546 */     if (typeName == null) {
/* 547 */       xtw.name("");
/*     */     } else {
/* 549 */       xtw.name(typeName.getLocalPart());
/* 550 */       String typeNameURI = typeName.getNamespaceURI();
/* 551 */       if (!typeNameURI.equals(mostUsedNamespaceURI)) {
/* 552 */         xtw.namespace(typeNameURI);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateAttributeWildcard(ClassOutlineImpl cc) {
/* 560 */     String FIELD_NAME = "otherAttributes";
/* 561 */     String METHOD_SEED = this.model.getNameConverter().toClassName(FIELD_NAME);
/*     */     
/* 563 */     JClass mapType = this.codeModel.ref(Map.class).narrow(new Class[] { QName.class, String.class });
/* 564 */     JClass mapImpl = this.codeModel.ref(HashMap.class).narrow(new Class[] { QName.class, String.class });
/*     */ 
/*     */ 
/*     */     
/* 568 */     JFieldVar $ref = cc.implClass.field(4, (JType)mapType, FIELD_NAME, (JExpression)JExpr._new(mapImpl));
/*     */     
/* 570 */     $ref.annotate2(XmlAnyAttributeWriter.class);
/*     */     
/* 572 */     MethodWriter writer = cc.createMethodWriter();
/*     */     
/* 574 */     JMethod $get = writer.declareMethod((JType)mapType, "get" + METHOD_SEED);
/* 575 */     $get.javadoc().append("Gets a map that contains attributes that aren't bound to any typed property on this class.\n\n<p>\nthe map is keyed by the name of the attribute and \nthe value is the string value of the attribute.\n\nthe map returned by this method is live, and you can add new attribute\nby updating the map directly. Because of this design, there's no setter.\n");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 583 */     $get.javadoc().addReturn().append("always non-null");
/*     */     
/* 585 */     $get.body()._return((JExpression)$ref);
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
/*     */   private EnumOutline generateEnumDef(CEnumLeafInfo e) {
/* 597 */     JDefinedClass type = getClassFactory().createClass(getContainer(e.parent, Aspect.EXPOSED), e.shortName, e.getLocator(), ClassType.ENUM);
/*     */     
/* 599 */     type.javadoc().append(e.javadoc);
/*     */     
/* 601 */     return new EnumOutline(e, type) {
/*     */         @NotNull
/*     */         public Outline parent() {
/* 604 */           return BeanGenerator.this;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private void generateEnumBody(EnumOutline eo) {
/* 610 */     JDefinedClass type = eo.clazz;
/* 611 */     CEnumLeafInfo e = eo.target;
/*     */     
/* 613 */     XmlTypeWriter xtw = (XmlTypeWriter)type.annotate2(XmlTypeWriter.class);
/* 614 */     writeTypeName(e.getTypeName(), xtw, eo._package().getMostUsedNamespaceURI());
/*     */ 
/*     */     
/* 617 */     JCodeModel codeModel = this.model.codeModel;
/*     */ 
/*     */     
/* 620 */     JType baseExposedType = e.base.toType(this, Aspect.EXPOSED).unboxify();
/* 621 */     JType baseImplType = e.base.toType(this, Aspect.IMPLEMENTATION).unboxify();
/*     */ 
/*     */     
/* 624 */     XmlEnumWriter xew = (XmlEnumWriter)type.annotate2(XmlEnumWriter.class);
/* 625 */     xew.value(baseExposedType);
/*     */ 
/*     */     
/* 628 */     boolean needsValue = e.needsValueField();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 634 */     Set<String> enumFieldNames = new HashSet<String>();
/*     */     
/* 636 */     for (CEnumConstant mem : e.members) {
/* 637 */       String constName = mem.getName();
/*     */       
/* 639 */       if (!JJavaName.isJavaIdentifier(constName))
/*     */       {
/* 641 */         getErrorReceiver().error(e.getLocator(), Messages.ERR_UNUSABLE_NAME.format(new Object[] { mem.getLexicalValue(), constName }));
/*     */       }
/*     */ 
/*     */       
/* 645 */       if (!enumFieldNames.add(constName)) {
/* 646 */         getErrorReceiver().error(e.getLocator(), Messages.ERR_NAME_COLLISION.format(new Object[] { constName }));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 651 */       JEnumConstant constRef = type.enumConstant(constName);
/* 652 */       if (needsValue) {
/* 653 */         constRef.arg(e.base.createConstant(this, new XmlString(mem.getLexicalValue())));
/*     */       }
/* 655 */       if (!mem.getLexicalValue().equals(constName)) {
/* 656 */         ((XmlEnumValueWriter)constRef.annotate2(XmlEnumValueWriter.class)).value(mem.getLexicalValue());
/*     */       }
/*     */       
/* 659 */       if (mem.javadoc != null) {
/* 660 */         constRef.javadoc().append(mem.javadoc);
/*     */       }
/* 662 */       eo.constants.add(new EnumConstantOutline(mem, constRef) {
/*     */           
/*     */           });
/*     */     } 
/* 666 */     if (needsValue) {
/*     */       JInvocation jInvocation1, jInvocation2;
/*     */       
/* 669 */       JFieldVar $value = type.field(12, baseExposedType, "value");
/*     */ 
/*     */ 
/*     */       
/* 673 */       type.method(1, baseExposedType, "value").body()._return((JExpression)$value);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 680 */       JMethod m = type.constructor(0);
/* 681 */       m.body().assign((JAssignmentTarget)$value, (JExpression)m.param(baseImplType, "v"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 693 */       m = type.method(17, (JType)type, "fromValue");
/* 694 */       JVar $v = m.param(baseExposedType, "v");
/* 695 */       JForEach fe = m.body().forEach((JType)type, "c", (JExpression)type.staticInvoke("values"));
/*     */       
/* 697 */       if (baseExposedType.isPrimitive()) {
/* 698 */         JExpression eq = fe.var().ref((JVar)$value).eq((JExpression)$v);
/*     */       } else {
/* 700 */         jInvocation1 = fe.var().ref((JVar)$value).invoke("equals").arg((JExpression)$v);
/*     */       } 
/* 702 */       fe.body()._if((JExpression)jInvocation1)._then()._return((JExpression)fe.var());
/*     */       
/* 704 */       JInvocation ex = JExpr._new(codeModel.ref(IllegalArgumentException.class));
/*     */ 
/*     */       
/* 707 */       if (baseExposedType.isPrimitive()) {
/* 708 */         jInvocation2 = codeModel.ref(String.class).staticInvoke("valueOf").arg((JExpression)$v);
/*     */       }
/* 710 */       else if (baseExposedType == codeModel.ref(String.class)) {
/* 711 */         JVar jVar = $v;
/*     */       } else {
/* 713 */         jInvocation2 = $v.invoke("toString");
/*     */       } 
/* 715 */       m.body()._throw((JExpression)ex.arg((JExpression)jInvocation2));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 720 */       type.method(1, String.class, "value").body()._return((JExpression)JExpr.invoke("name"));
/*     */ 
/*     */ 
/*     */       
/* 724 */       JMethod m = type.method(17, (JType)type, "fromValue");
/* 725 */       m.body()._return((JExpression)JExpr.invoke("valueOf").arg((JExpression)m.param(String.class, "v")));
/*     */     } 
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
/*     */   private FieldOutline generateFieldDecl(ClassOutlineImpl cc, CPropertyInfo prop) {
/* 739 */     FieldRenderer fr = prop.realization;
/* 740 */     if (fr == null)
/*     */     {
/* 742 */       fr = this.model.options.getFieldRendererFactory().getDefault();
/*     */     }
/* 744 */     FieldOutline field = fr.generate(cc, prop);
/* 745 */     this.fields.put(prop, field);
/*     */ 
/*     */     
/* 748 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void generateAdapterIfNecessary(CPropertyInfo prop, JAnnotatable field) {
/* 757 */     CAdapter adapter = prop.getAdapter();
/* 758 */     if (adapter != null) {
/* 759 */       if (adapter.getAdapterIfKnown() == SwaRefAdapter.class) {
/* 760 */         field.annotate(XmlAttachmentRef.class);
/*     */       }
/*     */       else {
/*     */         
/* 764 */         XmlJavaTypeAdapterWriter xjtw = (XmlJavaTypeAdapterWriter)field.annotate2(XmlJavaTypeAdapterWriter.class);
/* 765 */         xjtw.value((JType)((NClass)adapter.adapterType).toType(this, Aspect.EXPOSED));
/*     */       } 
/*     */     }
/*     */     
/* 769 */     switch (prop.id()) {
/*     */       case ID:
/* 771 */         field.annotate(XmlID.class);
/*     */         break;
/*     */       case IDREF:
/* 774 */         field.annotate(XmlIDREF.class);
/*     */         break;
/*     */     } 
/*     */     
/* 778 */     if (prop.getExpectedMimeType() != null)
/* 779 */       ((XmlMimeTypeWriter)field.annotate2(XmlMimeTypeWriter.class)).value(prop.getExpectedMimeType().toString()); 
/*     */   }
/*     */   
/*     */   public final JClass addRuntime(Class clazz) {
/* 783 */     JClass g = this.generatedRuntime.get(clazz);
/* 784 */     if (g == null) {
/*     */       
/* 786 */       JPackage implPkg = getUsedPackages(Aspect.IMPLEMENTATION)[0].subPackage("runtime");
/* 787 */       g = generateStaticClass(clazz, implPkg);
/* 788 */       this.generatedRuntime.put(clazz, g);
/*     */     } 
/* 790 */     return g;
/*     */   }
/*     */   
/*     */   public JClass generateStaticClass(Class src, JPackage out) {
/* 794 */     String shortName = getShortName(src.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 800 */     URL res = src.getResource(shortName + ".java");
/* 801 */     if (res == null)
/* 802 */       res = src.getResource(shortName + ".java_"); 
/* 803 */     if (res == null) {
/* 804 */       throw new InternalError("Unable to load source code of " + src.getName() + " as a resource");
/*     */     }
/* 806 */     JStaticJavaFile sjf = new JStaticJavaFile(out, shortName, res, null);
/* 807 */     out.addResourceFile((JResourceFile)sjf);
/* 808 */     return sjf.getJClass();
/*     */   }
/*     */   
/*     */   private String getShortName(String name) {
/* 812 */     return name.substring(name.lastIndexOf('.') + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\BeanGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
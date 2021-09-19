/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import com.sun.codemodel.fmt.JTextFile;
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.reader.ModelChecker;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDom;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISerializable;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.util.XSFinder;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerFactory;
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
/*     */ public class BGMBuilder
/*     */   extends BindingComponent
/*     */ {
/*     */   public final boolean inExtensionMode;
/*     */   public final String defaultPackage1;
/*     */   public final String defaultPackage2;
/*     */   
/*     */   public static Model build(XSSchemaSet _schemas, JCodeModel codeModel, ErrorReceiver _errorReceiver, Options opts) {
/* 101 */     Ring old = Ring.begin();
/*     */     try {
/* 103 */       ErrorReceiverFilter ef = new ErrorReceiverFilter((ErrorListener)_errorReceiver);
/*     */       
/* 105 */       Ring.add(XSSchemaSet.class, _schemas);
/* 106 */       Ring.add(codeModel);
/* 107 */       Model model = new Model(opts, codeModel, null, opts.classNameAllocator, _schemas);
/* 108 */       Ring.add(model);
/* 109 */       Ring.add(ErrorReceiver.class, ef);
/* 110 */       Ring.add(CodeModelClassFactory.class, new CodeModelClassFactory((ErrorReceiver)ef));
/*     */       
/* 112 */       BGMBuilder builder = new BGMBuilder(opts.defaultPackage, opts.defaultPackage2, opts.isExtensionMode(), opts.getFieldRendererFactory());
/*     */       
/* 114 */       builder._build();
/*     */       
/* 116 */       if (ef.hadError()) return null; 
/* 117 */       return model;
/*     */     } finally {
/* 119 */       Ring.end(old);
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
/* 142 */   private final BindGreen green = (BindGreen)Ring.get(BindGreen.class);
/* 143 */   private final BindPurple purple = (BindPurple)Ring.get(BindPurple.class);
/*     */   
/* 145 */   public final Model model = (Model)Ring.get(Model.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public final FieldRendererFactory fieldRendererFactory;
/*     */ 
/*     */   
/*     */   private RefererFinder refFinder;
/*     */ 
/*     */   
/*     */   private BIGlobalBinding globalBinding;
/*     */ 
/*     */   
/*     */   private ParticleBinder particleBinder;
/*     */ 
/*     */   
/*     */   private final BindInfo emptyBindInfo;
/*     */ 
/*     */   
/*     */   private final Map<XSComponent, BindInfo> externalBindInfos;
/*     */ 
/*     */   
/*     */   private final XSFinder toPurple;
/*     */ 
/*     */   
/*     */   private Transformer identityTransformer;
/*     */ 
/*     */ 
/*     */   
/*     */   private void _build() {
/* 175 */     buildContents();
/* 176 */     getClassSelector().executeTasks();
/*     */ 
/*     */ 
/*     */     
/* 180 */     ((UnusedCustomizationChecker)Ring.get(UnusedCustomizationChecker.class)).run();
/*     */     
/* 182 */     ((ModelChecker)Ring.get(ModelChecker.class)).check();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void promoteGlobalBindings() {
/* 189 */     XSSchemaSet schemas = (XSSchemaSet)Ring.get(XSSchemaSet.class);
/*     */     
/* 191 */     for (XSSchema s : schemas.getSchemas()) {
/* 192 */       BindInfo bi = getBindInfo((XSComponent)s);
/*     */ 
/*     */       
/* 195 */       this.model.getCustomizations().addAll((Collection)bi.toCustomizationList());
/*     */       
/* 197 */       BIGlobalBinding gb = (BIGlobalBinding)bi.get(BIGlobalBinding.class);
/* 198 */       if (gb == null) {
/*     */         continue;
/*     */       }
/* 201 */       if (this.globalBinding == null) {
/* 202 */         this.globalBinding = gb;
/* 203 */         this.globalBinding.markAsAcknowledged();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 208 */       gb.markAsAcknowledged();
/* 209 */       getErrorReporter().error(gb.getLocation(), "ERR_MULTIPLE_GLOBAL_BINDINGS", new Object[0]);
/*     */       
/* 211 */       getErrorReporter().error(this.globalBinding.getLocation(), "ERR_MULTIPLE_GLOBAL_BINDINGS_OTHER", new Object[0]);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 216 */     if (this.globalBinding == null) {
/*     */ 
/*     */       
/* 219 */       this.globalBinding = new BIGlobalBinding();
/* 220 */       BindInfo big = new BindInfo();
/* 221 */       big.addDecl((BIDeclaration)this.globalBinding);
/* 222 */       big.setOwner(this, null);
/*     */     } 
/*     */ 
/*     */     
/* 226 */     this.model.strategy = this.globalBinding.getCodeGenerationStrategy();
/* 227 */     this.model.rootClass = (JClass)this.globalBinding.getSuperClass();
/* 228 */     this.model.rootInterface = (JClass)this.globalBinding.getSuperInterface();
/*     */     
/* 230 */     this.particleBinder = this.globalBinding.isSimpleMode() ? new ExpressionParticleBinder() : new DefaultParticleBinder();
/*     */ 
/*     */     
/* 233 */     BISerializable serial = this.globalBinding.getSerializable();
/* 234 */     if (serial != null) {
/* 235 */       this.model.serializable = true;
/* 236 */       this.model.serialVersionUID = serial.uid;
/*     */     } 
/*     */ 
/*     */     
/* 240 */     if (this.globalBinding.nameConverter != null) {
/* 241 */       this.model.setNameConverter(this.globalBinding.nameConverter);
/*     */     }
/*     */     
/* 244 */     this.globalBinding.dispatchGlobalConversions(schemas);
/*     */     
/* 246 */     this.globalBinding.errorCheck();
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
/*     */   @NotNull
/*     */   public BIGlobalBinding getGlobalBinding() {
/* 260 */     return this.globalBinding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public ParticleBinder getParticleBinder() {
/* 268 */     return this.particleBinder;
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
/*     */   public NameConverter getNameConverter() {
/* 282 */     return this.model.getNameConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildContents() {
/* 290 */     ClassSelector cs = getClassSelector();
/* 291 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*     */     
/* 293 */     for (XSSchema s : ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getSchemas()) {
/* 294 */       BISchemaBinding sb = (BISchemaBinding)getBindInfo((XSComponent)s).get(BISchemaBinding.class);
/*     */       
/* 296 */       if (sb != null && !sb.map) {
/* 297 */         sb.markAsAcknowledged();
/*     */         
/*     */         continue;
/*     */       } 
/* 301 */       getClassSelector().pushClassScope((CClassInfoParent)new CClassInfoParent.Package(getClassSelector().getPackage(s.getTargetNamespace())));
/*     */ 
/*     */       
/* 304 */       checkMultipleSchemaBindings(s);
/* 305 */       processPackageJavadoc(s);
/* 306 */       populate(s.getAttGroupDecls(), s);
/* 307 */       populate(s.getAttributeDecls(), s);
/* 308 */       populate(s.getElementDecls(), s);
/* 309 */       populate(s.getModelGroupDecls(), s);
/*     */ 
/*     */       
/* 312 */       for (XSType t : s.getTypes().values()) {
/* 313 */         stb.refererStack.push(t);
/* 314 */         this.model.typeUses().put(getName((XSDeclaration)t), cs.bindToType(t, (XSComponent)s));
/* 315 */         stb.refererStack.pop();
/*     */       } 
/*     */       
/* 318 */       getClassSelector().popClassScope();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkMultipleSchemaBindings(XSSchema schema) {
/* 324 */     ArrayList<Locator> locations = new ArrayList<Locator>();
/*     */     
/* 326 */     BindInfo bi = getBindInfo((XSComponent)schema);
/* 327 */     for (BIDeclaration bid : bi) {
/* 328 */       if (bid.getName() == BISchemaBinding.NAME)
/* 329 */         locations.add(bid.getLocation()); 
/*     */     } 
/* 331 */     if (locations.size() <= 1) {
/*     */       return;
/*     */     }
/* 334 */     getErrorReporter().error(locations.get(0), "BGMBuilder.MultipleSchemaBindings", new Object[] { schema.getTargetNamespace() });
/*     */ 
/*     */     
/* 337 */     for (int i = 1; i < locations.size(); i++) {
/* 338 */       getErrorReporter().error(locations.get(i), "BGMBuilder.MultipleSchemaBindings.Location", new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void populate(Map<String, ? extends XSComponent> col, XSSchema schema) {
/* 347 */     ClassSelector cs = getClassSelector();
/* 348 */     for (XSComponent sc : col.values()) {
/* 349 */       cs.bindToType(sc, (XSComponent)schema);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processPackageJavadoc(XSSchema s) {
/* 358 */     BISchemaBinding cust = (BISchemaBinding)getBindInfo((XSComponent)s).get(BISchemaBinding.class);
/* 359 */     if (cust == null)
/*     */       return; 
/* 361 */     cust.markAsAcknowledged();
/* 362 */     if (cust.getJavadoc() == null) {
/*     */       return;
/*     */     }
/* 365 */     JTextFile html = new JTextFile("package.html");
/* 366 */     html.setContents(cust.getJavadoc());
/* 367 */     getClassSelector().getPackage(s.getTargetNamespace()).addResourceFile((JResourceFile)html);
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
/*     */   public BindInfo getOrCreateBindInfo(XSComponent schemaComponent) {
/* 385 */     BindInfo bi = _getBindInfoReadOnly(schemaComponent);
/* 386 */     if (bi != null) return bi;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 391 */     bi = new BindInfo();
/* 392 */     bi.setOwner(this, schemaComponent);
/* 393 */     this.externalBindInfos.put(schemaComponent, bi);
/* 394 */     return bi;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BGMBuilder(String defaultPackage1, String defaultPackage2, boolean _inExtensionMode, FieldRendererFactory fieldRendererFactory)
/*     */   {
/* 401 */     this.emptyBindInfo = new BindInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 444 */     this.externalBindInfos = new HashMap<XSComponent, BindInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 471 */     this.toPurple = new XSFinder()
/*     */       {
/*     */         public Boolean attributeUse(XSAttributeUse use) {
/* 474 */           return Boolean.valueOf(true);
/*     */         }
/*     */ 
/*     */         
/*     */         public Boolean simpleType(XSSimpleType xsSimpleType) {
/* 479 */           return Boolean.valueOf(true);
/*     */         }
/*     */ 
/*     */         
/*     */         public Boolean wildcard(XSWildcard xsWildcard)
/*     */         {
/* 485 */           return Boolean.valueOf(true); }
/*     */       }; this.inExtensionMode = _inExtensionMode; this.defaultPackage1 = defaultPackage1; this.defaultPackage2 = defaultPackage2; this.fieldRendererFactory = fieldRendererFactory; DatatypeConverter.setDatatypeConverter(DatatypeConverterImpl.theInstance); promoteGlobalBindings();
/*     */   } public BindInfo getBindInfo(XSComponent schemaComponent) { BindInfo bi = _getBindInfoReadOnly(schemaComponent); if (bi != null)
/*     */       return bi;  return this.emptyBindInfo; } private BindInfo _getBindInfoReadOnly(XSComponent schemaComponent) { BindInfo bi = this.externalBindInfos.get(schemaComponent); if (bi != null)
/*     */       return bi;  XSAnnotation annon = schemaComponent.getAnnotation(); if (annon != null) {
/*     */       bi = (BindInfo)annon.getAnnotation(); if (bi != null) {
/*     */         if (bi.getOwner() == null)
/*     */           bi.setOwner(this, schemaComponent);  return bi;
/*     */       } 
/*     */     } 
/* 495 */     return null; } public void ying(XSComponent sc, @Nullable XSComponent referer) { if (((Boolean)sc.apply((XSFunction)this.toPurple)).booleanValue() == true || getClassSelector().bindToType(sc, referer) != null) {
/* 496 */       sc.visit(this.purple);
/*     */     } else {
/* 498 */       sc.visit(this.green);
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer getIdentityTransformer() {
/*     */     try {
/* 508 */       if (this.identityTransformer == null)
/* 509 */         this.identityTransformer = TransformerFactory.newInstance().newTransformer(); 
/* 510 */       return this.identityTransformer;
/* 511 */     } catch (TransformerConfigurationException e) {
/* 512 */       throw new Error(e);
/*     */     }  } protected final BIDom getLocalDomCustomization(XSParticle p) { BIDom dom = (BIDom)getBindInfo((XSComponent)p).get(BIDom.class); if (dom != null)
/*     */       return dom;  dom = (BIDom)getBindInfo((XSComponent)p.getTerm()).get(BIDom.class); if (dom != null)
/*     */       return dom;  XSTerm t = p.getTerm(); if (t.isElementDecl())
/*     */       return (BIDom)getBindInfo((XSComponent)t.asElementDecl().getType()).get(BIDom.class); 
/*     */     if (t.isModelGroupDecl())
/*     */       return (BIDom)getBindInfo((XSComponent)t.asModelGroupDecl().getModelGroup()).get(BIDom.class); 
/*     */     return null; }
/* 520 */   public Set<XSComponent> getReferer(XSType c) { if (this.refFinder == null) {
/* 521 */       this.refFinder = new RefererFinder();
/* 522 */       this.refFinder.schemaSet((XSSchemaSet)Ring.get(XSSchemaSet.class));
/*     */     } 
/* 524 */     return this.refFinder.getReferer((XSComponent)c); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QName getName(XSDeclaration decl) {
/* 533 */     String local = decl.getName();
/* 534 */     if (local == null) return null; 
/* 535 */     return new QName(decl.getTargetNamespace(), local);
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
/*     */   public String deriveName(String name, XSComponent comp) {
/* 552 */     XSSchema owner = comp.getOwnerSchema();
/*     */     
/* 554 */     name = getNameConverter().toClassName(name);
/*     */     
/* 556 */     if (owner != null) {
/* 557 */       BISchemaBinding sb = (BISchemaBinding)getBindInfo((XSComponent)owner).get(BISchemaBinding.class);
/*     */       
/* 559 */       if (sb != null) name = sb.mangleClassName(name, comp);
/*     */     
/*     */     } 
/* 562 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\BGMBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
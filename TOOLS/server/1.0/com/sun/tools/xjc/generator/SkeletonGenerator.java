/*     */ package 1.0.com.sun.tools.xjc.generator;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.codemodel.fmt.JStaticFile;
/*     */ import com.sun.codemodel.fmt.JStaticJavaFile;
/*     */ import com.sun.msv.grammar.NameClassAndExpression;
/*     */ import com.sun.tools.xjc.AbortException;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.LookupTableBuilder;
/*     */ import com.sun.tools.xjc.generator.LookupTableCache;
/*     */ import com.sun.tools.xjc.generator.LookupTableFactory;
/*     */ import com.sun.tools.xjc.generator.LookupTableInterner;
/*     */ import com.sun.tools.xjc.generator.Messages;
/*     */ import com.sun.tools.xjc.generator.MethodWriter;
/*     */ import com.sun.tools.xjc.generator.PackageContext;
/*     */ import com.sun.tools.xjc.generator.XmlNameStoreAlgorithm;
/*     */ import com.sun.tools.xjc.generator.cls.ImplStructureStrategy;
/*     */ import com.sun.tools.xjc.generator.cls.PararellStructureStrategy;
/*     */ import com.sun.tools.xjc.generator.field.DefaultFieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.bind.JAXBObject;
/*     */ import com.sun.xml.bind.RIElement;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkeletonGenerator
/*     */   implements GeneratorContext
/*     */ {
/*     */   private final CodeModelClassFactory codeModelClassFactory;
/*     */   private final ErrorReceiver errorReceiver;
/*     */   private final Options opts;
/*  67 */   private final Map packageContexts = new HashMap();
/*     */ 
/*     */   
/*  70 */   private final Map classContexts = new HashMap();
/*     */ 
/*     */   
/*     */   private final AnnotatedGrammar grammar;
/*     */ 
/*     */   
/*     */   private final JCodeModel codeModel;
/*     */ 
/*     */   
/*  79 */   private final Map runtimeClasses = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private final Map fields = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final LookupTableBuilder lookupTableBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GeneratorContext generate(AnnotatedGrammar grammar, Options opt, ErrorReceiver _errorReceiver) {
/*     */     try {
/* 110 */       return new com.sun.tools.xjc.generator.SkeletonGenerator(grammar, opt, _errorReceiver);
/* 111 */     } catch (AbortException e) {
/* 112 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SkeletonGenerator(AnnotatedGrammar _grammar, Options opt, ErrorReceiver _errorReceiver) {
/* 120 */     this.grammar = _grammar;
/* 121 */     this.opts = opt;
/* 122 */     this.codeModel = this.grammar.codeModel;
/* 123 */     this.errorReceiver = _errorReceiver;
/* 124 */     this.codeModelClassFactory = new CodeModelClassFactory(this.errorReceiver);
/*     */ 
/*     */     
/* 127 */     populateTransducers(this.grammar);
/*     */ 
/*     */     
/* 130 */     generateStaticRuntime();
/*     */     
/* 132 */     JPackage[] packages = this.grammar.getUsedPackages();
/*     */     
/* 134 */     if (packages.length != 0) {
/* 135 */       this.lookupTableBuilder = (LookupTableBuilder)new LookupTableCache((LookupTableBuilder)new LookupTableInterner((LookupTableBuilder)new LookupTableFactory(packages[0].subPackage("impl"))));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 140 */       this.lookupTableBuilder = null;
/*     */     } 
/*     */     
/* 143 */     for (int i = 0; i < packages.length; i++) {
/* 144 */       JPackage pkg = packages[i];
/* 145 */       this.packageContexts.put(pkg, new PackageContext(this, this.grammar, opt, pkg));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 150 */     ClassItem[] items = this.grammar.getClasses();
/*     */ 
/*     */     
/* 153 */     PararellStructureStrategy pararellStructureStrategy = new PararellStructureStrategy(this.codeModelClassFactory); int j;
/* 154 */     for (j = 0; j < items.length; j++) {
/* 155 */       this.classContexts.put(items[j], new ClassContext(this, (ImplStructureStrategy)pararellStructureStrategy, items[j]));
/*     */     }
/*     */     
/* 158 */     for (j = 0; j < items.length; j++) {
/* 159 */       generateClass(getClassContext(items[j]));
/*     */     }
/*     */     
/* 162 */     for (j = 0; j < items.length; j++) {
/* 163 */       ClassContext cc = getClassContext(items[j]);
/*     */ 
/*     */       
/* 166 */       ClassItem superClass = cc.target.getSuperClass();
/* 167 */       if (superClass != null) {
/*     */         
/* 169 */         cc.implClass._extends((getClassContext(superClass)).implRef);
/*     */       
/*     */       }
/* 172 */       else if (this.grammar.rootClass != null) {
/* 173 */         cc.implClass._extends(this.grammar.rootClass);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 178 */       FieldUse[] fus = items[j].getDeclaredFieldUses();
/* 179 */       for (int k = 0; k < fus.length; k++) {
/* 180 */         if (fus[k].isDelegated()) {
/* 181 */           generateDelegation((items[j]).locator, cc.implClass, (JClass)(fus[k]).type, getField(fus[k]));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedGrammar getGrammar() {
/* 190 */     return this.grammar;
/*     */   }
/*     */   
/*     */   public JCodeModel getCodeModel() {
/* 194 */     return this.codeModel;
/*     */   }
/*     */   
/*     */   public LookupTableBuilder getLookupTableBuilder() {
/* 198 */     return this.lookupTableBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPackage getRuntimePackage() {
/* 209 */     if (this.opts.runtimePackage != null)
/*     */     {
/* 211 */       return this.codeModel._package(this.opts.runtimePackage);
/*     */     }
/* 213 */     JPackage[] pkgs = this.grammar.getUsedPackages();
/* 214 */     if (pkgs.length == 0) return null;
/*     */     
/* 216 */     JPackage pkg = pkgs[0];
/* 217 */     if (pkg.name().startsWith("org.w3") && pkgs.length > 1) {
/* 218 */       pkg = this.grammar.getUsedPackages()[1];
/*     */     }
/* 220 */     return pkg.subPackage("impl.runtime");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateStaticRuntime() {
/* 230 */     if (!this.opts.generateRuntime) {
/*     */       return;
/*     */     }
/* 233 */     JPackage pkg = getRuntimePackage();
/* 234 */     String prefix = "com/sun/tools/xjc/runtime/";
/*     */     
/* 236 */     if (pkg == null)
/*     */       return; 
/* 238 */     BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("com/sun/tools/xjc/runtime/filelist")));
/*     */     
/*     */     try {
/*     */       String line;
/* 242 */       while ((line = r.readLine()) != null) {
/* 243 */         if (line.startsWith("#"))
/*     */           continue; 
/* 245 */         String name = line.substring(12);
/* 246 */         boolean forU = (line.charAt(2) == 'x');
/* 247 */         boolean forW = (line.charAt(4) == 'x');
/* 248 */         boolean forM = (line.charAt(6) == 'x');
/* 249 */         boolean forV = (line.charAt(8) == 'x');
/* 250 */         boolean must = (line.charAt(10) == 'x');
/*     */         
/* 252 */         if (must || (forU && this.opts.generateUnmarshallingCode) || (forW && this.opts.generateValidatingUnmarshallingCode) || (forM && this.opts.generateMarshallingCode) || (forV && this.opts.generateValidationCode))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 258 */           if (name.endsWith(".java")) {
/* 259 */             String className = name.substring(0, name.length() - 5);
/* 260 */             Class cls = Class.forName("com/sun/tools/xjc/runtime/".replace('/', '.') + className);
/*     */             
/* 262 */             addRuntime(cls); continue;
/*     */           } 
/* 264 */           JStaticFile s = new JStaticFile("com/sun/tools/xjc/runtime/" + name);
/* 265 */           pkg.addResourceFile((JResourceFile)s);
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 271 */     catch (IOException e) {
/* 272 */       e.printStackTrace();
/* 273 */       throw new JAXBAssertionError();
/* 274 */     } catch (ClassNotFoundException e) {
/* 275 */       e.printStackTrace();
/* 276 */       throw new JAXBAssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   public JClass getRuntime(Class clazz) {
/* 281 */     JClass r = (JClass)this.runtimeClasses.get(clazz);
/* 282 */     if (r != null) return r; 
/* 283 */     return addRuntime(clazz);
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
/*     */   private JClass addRuntime(Class runtimeClass) {
/* 297 */     JPackage pkg = getRuntimePackage();
/* 298 */     String shortName = getShortName(runtimeClass.getName());
/*     */     
/* 300 */     if (!pkg.hasResourceFile(shortName + ".java")) {
/* 301 */       URL res = runtimeClass.getResource(shortName + ".java");
/* 302 */       if (res == null) {
/* 303 */         throw new JAXBAssertionError("Unable to load source code of " + runtimeClass.getName() + " as a resource");
/*     */       }
/* 305 */       JStaticJavaFile sjf = new JStaticJavaFile(pkg, shortName, res, (JStaticJavaFile.LineFilter)new PreProcessor(this, null));
/*     */       
/* 307 */       if (this.opts.generateRuntime)
/* 308 */         pkg.addResourceFile((JResourceFile)sjf); 
/* 309 */       this.runtimeClasses.put(runtimeClass, sjf.getJClass());
/*     */     } 
/*     */     
/* 312 */     return getRuntime(runtimeClass);
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
/*     */   private String getShortName(String name) {
/* 330 */     return name.substring(name.lastIndexOf('.') + 1);
/*     */   }
/*     */   public ErrorReceiver getErrorReceiver() {
/* 333 */     return this.errorReceiver;
/*     */   } public CodeModelClassFactory getClassFactory() {
/* 335 */     return this.codeModelClassFactory;
/*     */   }
/*     */   public PackageContext getPackageContext(JPackage p) {
/* 338 */     return (PackageContext)this.packageContexts.get(p);
/*     */   }
/*     */   
/*     */   public ClassContext getClassContext(ClassItem ci) {
/* 342 */     return (ClassContext)this.classContexts.get(ci);
/*     */   }
/*     */   
/*     */   public PackageContext[] getAllPackageContexts() {
/* 346 */     return (PackageContext[])this.packageContexts.values().toArray((Object[])new PackageContext[this.packageContexts.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldRenderer getField(FieldUse fu) {
/* 352 */     return (FieldRenderer)this.fields.get(fu);
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
/*     */   private void generateClass(ClassContext cc) {
/* 368 */     if (this.grammar.serialVersionUID != null) {
/* 369 */       cc.implClass._implements(Serializable.class);
/* 370 */       cc.implClass.field(28, (JType)this.codeModel.LONG, "serialVersionUID", JExpr.lit(this.grammar.serialVersionUID.longValue()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 378 */     if (cc.target.exp instanceof NameClassAndExpression) {
/*     */       
/* 380 */       XmlNameStoreAlgorithm nsa = XmlNameStoreAlgorithm.get((NameClassAndExpression)cc.target.exp);
/*     */       
/* 382 */       nsa.populate(cc);
/*     */       
/* 384 */       if (cc.target.exp instanceof com.sun.msv.grammar.ElementExp) {
/*     */         
/* 386 */         cc.implClass._implements(RIElement.class);
/*     */         
/* 388 */         cc.implClass.method(1, String.class, "____jaxb_ri____getNamespaceURI").body()._return(nsa.getNamespaceURI());
/*     */         
/* 390 */         cc.implClass.method(1, String.class, "____jaxb_ri____getLocalName").body()._return(nsa.getLocalPart());
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     cc.implClass._implements(JAXBObject.class);
/*     */ 
/*     */     
/* 397 */     FieldUse[] fus = cc.target.getDeclaredFieldUses();
/* 398 */     for (int j = 0; j < fus.length; j++) {
/* 399 */       generateFieldDecl(cc, fus[j]);
/*     */     }
/* 401 */     if (cc.target.hasGetContentMethod) {
/* 402 */       generateChoiceContentField(cc);
/*     */     }
/*     */     
/* 405 */     cc._package.objectFactoryGenerator.populate(cc);
/*     */ 
/*     */     
/* 408 */     cc._package.versionGenerator.generateVersionReference(cc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateChoiceContentField(ClassContext cc) {
/* 415 */     FieldUse[] fus = cc.target.getDeclaredFieldUses();
/*     */ 
/*     */     
/* 418 */     JType[] types = new JType[fus.length];
/* 419 */     for (int i = 0; i < fus.length; i++) {
/* 420 */       FieldRenderer fr = getField(fus[i]);
/* 421 */       types[i] = (JType)fr.getValueType();
/*     */     } 
/* 423 */     JType returnType = TypeUtil.getCommonBaseType(this.codeModel, types);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 428 */     MethodWriter helper = cc.createMethodWriter();
/* 429 */     JMethod $get = helper.declareMethod(returnType, "getContent");
/*     */     
/* 431 */     for (int j = 0; j < fus.length; j++) {
/* 432 */       FieldRenderer fr = getField(fus[j]);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 437 */       JBlock then = $get.body()._if(fr.hasSetValue())._then();
/* 438 */       then._return(fr.getValue());
/*     */     } 
/*     */     
/* 441 */     $get.body()._return(JExpr._null());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 447 */     JMethod $isSet = helper.declareMethod((JType)this.codeModel.BOOLEAN, "isSetContent");
/* 448 */     JExpression exp = JExpr.FALSE;
/* 449 */     for (int k = 0; k < fus.length; k++) {
/* 450 */       exp = exp.cor(getField(fus[k]).hasSetValue());
/*     */     }
/* 452 */     $isSet.body()._return(exp);
/*     */ 
/*     */ 
/*     */     
/* 456 */     JMethod $unset = helper.declareMethod((JType)this.codeModel.VOID, "unsetContent"); int m;
/* 457 */     for (m = 0; m < fus.length; m++) {
/* 458 */       getField(fus[m]).unsetValues($unset.body());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 463 */     for (m = 0; m < fus.length; m++) {
/* 464 */       FieldRenderer fr1 = getField(fus[m]);
/* 465 */       for (int n = 0; n < fus.length; n++) {
/* 466 */         if (m != n) {
/* 467 */           FieldRenderer fr2 = getField(fus[n]);
/* 468 */           fr2.unsetValues(fr1.getOnSetEventHandler());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateDelegation(Locator errorSource, JDefinedClass impl, JClass _intf, FieldRenderer fr) {
/* 476 */     JDefinedClass intf = (JDefinedClass)_intf;
/*     */     
/* 478 */     for (Iterator iterator = intf._implements(); iterator.hasNext();) {
/* 479 */       generateDelegation(errorSource, impl, iterator.next(), fr);
/*     */     }
/*     */     
/* 482 */     for (Iterator itr = intf.methods(); itr.hasNext(); ) {
/* 483 */       JMethod m = itr.next();
/*     */ 
/*     */       
/* 486 */       if (impl.getMethod(m.name(), m.listParamTypes()) != null) {
/* 487 */         this.errorReceiver.error(errorSource, Messages.format("SkeletonGenerator.MethodCollision", m.name(), impl.fullName(), intf.fullName()));
/*     */       }
/*     */ 
/*     */       
/* 491 */       JMethod n = impl.method(1, m.type(), m.name());
/* 492 */       JVar[] mp = m.listParams();
/*     */       
/* 494 */       JInvocation inv = fr.getValue().invoke(m);
/*     */       
/* 496 */       if (m.type() == this.codeModel.VOID) {
/* 497 */         n.body().add((JStatement)inv);
/*     */       } else {
/* 499 */         n.body()._return((JExpression)inv);
/*     */       } 
/* 501 */       for (int j = 0; j < mp.length; j++) {
/* 502 */         inv.arg((JExpression)n.param(mp[j].type(), mp[j].name()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateTransducers(AnnotatedGrammar grammar) {
/* 514 */     PrimitiveItem[] pis = grammar.getPrimitives();
/* 515 */     for (int i = 0; i < pis.length; i++) {
/* 516 */       (pis[i]).xducer.populate(grammar, this);
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
/*     */   private FieldRenderer generateFieldDecl(ClassContext cc, FieldUse fu) {
/*     */     DefaultFieldRendererFactory defaultFieldRendererFactory;
/* 535 */     FieldRendererFactory frf = fu.getRealization();
/* 536 */     if (frf == null)
/*     */     {
/* 538 */       defaultFieldRendererFactory = new DefaultFieldRendererFactory(this.codeModel);
/*     */     }
/* 540 */     FieldRenderer field = defaultFieldRendererFactory.create(cc, fu);
/* 541 */     field.generate();
/* 542 */     this.fields.put(fu, field);
/*     */     
/* 544 */     return field;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\SkeletonGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
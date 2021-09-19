/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import com.sun.codemodel.fmt.JTextFile;
/*     */ import com.sun.msv.datatype.xsd.QnameType;
/*     */ import com.sun.msv.datatype.xsd.QnameValueType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorExpression;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.grammar.xmlschema.OccurrenceExp;
/*     */ import com.sun.msv.util.StringPair;
/*     */ import com.sun.tools.xjc.AbortException;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.IgnoreItem;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.tools.xjc.reader.PackageTracker;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.reader.annotator.DatatypeSimplifier;
/*     */ import com.sun.tools.xjc.reader.annotator.FieldCollisionChecker;
/*     */ import com.sun.tools.xjc.reader.annotator.HierarchyAnnotator;
/*     */ import com.sun.tools.xjc.reader.annotator.RelationNormalizer;
/*     */ import com.sun.tools.xjc.reader.annotator.SymbolSpaceTypeAssigner;
/*     */ import com.sun.tools.xjc.reader.xmlschema.AlternativeParticleBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.DefaultParticleBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ErrorReporter;
/*     */ import com.sun.tools.xjc.reader.xmlschema.FieldBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ParticleBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.SimpleTypeBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.TypeBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.UnusedCustomizationChecker;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXSerializable;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXSuperClass;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassSelector;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*     */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BGMBuilder
/*     */   implements AnnotatorController
/*     */ {
/*     */   public final ClassSelector selector;
/*     */   public final TypeBuilder typeBuilder;
/*     */   public final FieldBuilder fieldBuilder;
/*     */   public final ParticleBinder particleBinder;
/*     */   public final ComplexTypeFieldBuilder complexTypeBuilder;
/*     */   
/*     */   public static AnnotatedGrammar build(XSSchemaSet schemas, JCodeModel codeModel, ErrorReceiver errorReceiver, String defPackage, boolean inExtensionMode) {
/*  82 */     ErrorReceiverFilter erFilter = new ErrorReceiverFilter(errorReceiver);
/*     */     try {
/*  84 */       AnnotatedGrammar grammar = (new com.sun.tools.xjc.reader.xmlschema.BGMBuilder(schemas, codeModel, (ErrorReceiver)erFilter, defPackage, inExtensionMode))._build(schemas);
/*  85 */       if (erFilter.hadError()) return null; 
/*  86 */       return grammar;
/*  87 */     } catch (AbortException e) {
/*  88 */       return null;
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
/* 107 */   public final Set particlesWithGlobalElementSkip = new HashSet(); public final boolean inExtensionMode; private BIGlobalBinding globalBinding; private NameConverter nameConverter;
/*     */   public final XSSchemaSet schemas;
/*     */   private final BindInfo emptyBindInfo;
/*     */   private final Map externalBindInfos;
/*     */   public final ErrorReporter errorReporter;
/*     */   public final ErrorReceiver errorReceiver;
/*     */   public final ExpressionPool pool;
/*     */   public final AnnotatedGrammar grammar;
/*     */   public final SimpleTypeBuilder simpleTypeBuilder;
/*     */   private final Expression xsiTypeExp;
/*     */   private final Map substitutionGroupCache;
/*     */   
/*     */   private AnnotatedGrammar _build(XSSchemaSet schemas) {
/* 120 */     buildContents();
/* 121 */     buildTopLevelExp();
/* 122 */     this.selector.executeTasks();
/*     */ 
/*     */     
/* 125 */     reportUnusedCustomizations();
/*     */ 
/*     */     
/* 128 */     if (!this.errorReporter.hadError()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       this.grammar.visit((ExpressionVisitorExpression)new DatatypeSimplifier(this.grammar.getPool()));
/* 136 */       HierarchyAnnotator.annotate(this.grammar, this);
/* 137 */       SymbolSpaceTypeAssigner.assign(this.grammar, this);
/* 138 */       FieldCollisionChecker.check(this.grammar, this);
/* 139 */       RelationNormalizer.normalize(this.grammar, this);
/*     */     } 
/*     */     
/* 142 */     return this.grammar;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void promoteGlobalBindings() {
/* 148 */     for (Iterator itr = this.schemas.iterateSchema(); itr.hasNext(); ) {
/* 149 */       XSSchema s = itr.next();
/* 150 */       BindInfo bi = getBindInfo((XSComponent)s);
/*     */       
/* 152 */       BIGlobalBinding gb = (BIGlobalBinding)bi.get(BIGlobalBinding.NAME);
/*     */       
/* 154 */       if (gb != null && this.globalBinding == null) {
/* 155 */         this.globalBinding = gb;
/* 156 */         this.globalBinding.markAsAcknowledged();
/*     */       } 
/*     */     } 
/*     */     
/* 160 */     if (this.globalBinding == null) {
/*     */ 
/*     */       
/* 163 */       this.globalBinding = new BIGlobalBinding(this.grammar.codeModel);
/* 164 */       BindInfo big = new BindInfo(null);
/* 165 */       big.addDecl((BIDeclaration)this.globalBinding);
/* 166 */       big.setOwner(this, null);
/*     */     } 
/*     */ 
/*     */     
/* 170 */     BIXSuperClass root = this.globalBinding.getSuperClassExtension();
/* 171 */     if (root != null) {
/* 172 */       this.grammar.rootClass = (JClass)root.getRootClass();
/*     */     }
/* 174 */     BIXSerializable serial = this.globalBinding.getSerializableExtension();
/* 175 */     if (serial != null) {
/* 176 */       this.grammar.serialVersionUID = new Long(serial.getUID());
/*     */     }
/*     */     
/* 179 */     this.nameConverter = this.globalBinding.getNameConverter();
/*     */ 
/*     */     
/* 182 */     this.globalBinding.dispatchGlobalConversions(this.schemas);
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
/*     */   public BIGlobalBinding getGlobalBinding() {
/* 198 */     return this.globalBinding;
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
/*     */   public NameConverter getNameConverter() {
/* 213 */     return this.nameConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildContents() {
/* 221 */     for (Iterator itr = this.schemas.iterateSchema(); itr.hasNext(); ) {
/* 222 */       XSSchema s = itr.next();
/* 223 */       if (s.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/*     */         continue;
/*     */       }
/* 226 */       checkMultipleSchemaBindings(s);
/* 227 */       processPackageJavadoc(s);
/* 228 */       populate(s.iterateAttGroupDecls());
/* 229 */       populate(s.iterateAttributeDecls());
/* 230 */       populate(s.iterateComplexTypes());
/* 231 */       populate(s.iterateElementDecls());
/* 232 */       populate(s.iterateModelGroupDecls());
/* 233 */       populate(s.iterateSimpleTypes());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkMultipleSchemaBindings(XSSchema schema) {
/* 239 */     ArrayList locations = new ArrayList();
/*     */     
/* 241 */     BindInfo bi = getBindInfo((XSComponent)schema); int i;
/* 242 */     for (i = 0; i < bi.size(); i++) {
/* 243 */       if (bi.get(i).getName() == BISchemaBinding.NAME)
/* 244 */         locations.add(bi.get(i).getLocation()); 
/*     */     } 
/* 246 */     if (locations.size() <= 1) {
/*     */       return;
/*     */     }
/* 249 */     this.errorReporter.error(locations.get(0), "BGMBuilder.MultipleSchemaBindings", schema.getTargetNamespace());
/*     */ 
/*     */     
/* 252 */     for (i = 1; i < locations.size(); i++) {
/* 253 */       this.errorReporter.error(locations.get(i), "BGMBuilder.MultipleSchemaBindings.Location");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void populate(Iterator itr) {
/* 262 */     while (itr.hasNext()) {
/* 263 */       XSComponent sc = itr.next();
/* 264 */       this.selector.bindToType(sc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processPackageJavadoc(XSSchema s) {
/* 274 */     BISchemaBinding cust = (BISchemaBinding)getBindInfo((XSComponent)s).get(BISchemaBinding.NAME);
/* 275 */     if (cust == null)
/*     */       return; 
/* 277 */     if (cust.getJavadoc() == null) {
/*     */       return;
/*     */     }
/* 280 */     JTextFile html = new JTextFile("package.html");
/* 281 */     html.setContents(cust.getJavadoc());
/* 282 */     this.selector.getPackage(s.getTargetNamespace()).addResourceFile((JResourceFile)html);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildTopLevelExp() {
/* 289 */     Expression top = Expression.nullSet;
/*     */     
/* 291 */     Iterator itr = this.schemas.iterateElementDecls();
/* 292 */     while (itr.hasNext()) {
/* 293 */       XSElementDecl decl = itr.next();
/*     */       
/* 295 */       TypeItem ti = this.selector.bindToType(decl);
/* 296 */       if (ti instanceof com.sun.tools.xjc.grammar.ClassItem) {
/* 297 */         top = this.pool.createChoice(top, (Expression)ti);
/*     */       }
/*     */     } 
/* 300 */     if (top == Expression.nullSet)
/*     */     {
/* 302 */       this.errorReporter.warning(null, "BGMBuilder.NoGlobalElement", null);
/*     */     }
/* 304 */     this.grammar.exp = top;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportUnusedCustomizations() {
/* 312 */     (new UnusedCustomizationChecker(this)).run();
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
/*     */   public BindInfo getBindInfo(XSComponent schemaComponent) {
/*     */     BindInfo bi = _getBindInfoReadOnly(schemaComponent);
/*     */     if (bi != null) {
/*     */       return bi;
/*     */     }
/*     */     return this.emptyBindInfo;
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
/*     */   public BindInfo getOrCreateBindInfo(XSComponent schemaComponent) {
/* 359 */     BindInfo bi = _getBindInfoReadOnly(schemaComponent);
/* 360 */     if (bi != null) return bi;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 365 */     bi = new BindInfo(null);
/* 366 */     bi.setOwner(this, schemaComponent);
/* 367 */     this.externalBindInfos.put(schemaComponent, bi);
/* 368 */     return bi;
/*     */   } private BindInfo _getBindInfoReadOnly(XSComponent schemaComponent) { BindInfo bi = (BindInfo)this.externalBindInfos.get(schemaComponent); if (bi != null)
/*     */       return bi;  XSAnnotation annon = schemaComponent.getAnnotation(); if (annon != null) { bi = (BindInfo)annon.getAnnotation(); if (bi != null) {
/*     */         if (bi.getOwner() == null)
/*     */           bi.setOwner(this, schemaComponent);  return bi;
/*     */       }  }
/* 374 */      return null; } public BGMBuilder(XSSchemaSet _schemas, JCodeModel codeModel, ErrorReceiver _errorReceiver, String defaultPackage, boolean _inExtensionMode) { this.emptyBindInfo = new BindInfo(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 419 */     this.externalBindInfos = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 428 */     this.pool = new ExpressionPool();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 531 */     this.xsiTypeExp = this.pool.createOptional(this.pool.createAttribute((NameClass)new SimpleNameClass("http://www.w3.org/2001/XMLSchema-instance", "type"), this.pool.createData((XSDatatype)QnameType.theInstance)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 554 */     this.substitutionGroupCache = new HashMap(); this.schemas = _schemas; this.inExtensionMode = _inExtensionMode; this.grammar = new AnnotatedGrammar(Expression.nullSet, this.pool, codeModel); this.errorReceiver = _errorReceiver;
/*     */     this.errorReporter = new ErrorReporter(_errorReceiver);
/*     */     this.simpleTypeBuilder = new SimpleTypeBuilder(this);
/*     */     this.typeBuilder = new TypeBuilder(this);
/*     */     this.fieldBuilder = new FieldBuilder(this);
/*     */     this.complexTypeBuilder = new ComplexTypeFieldBuilder(this);
/*     */     promoteGlobalBindings();
/*     */     this.selector = new ClassSelector(this, defaultPackage);
/*     */     if (getGlobalBinding().isModelGroupBinding()) {
/*     */       this.particleBinder = (ParticleBinder)new AlternativeParticleBinder(this);
/*     */     } else {
/*     */       this.particleBinder = (ParticleBinder)new DefaultParticleBinder(this);
/*     */     }  } public Expression processMinMax(Expression item, XSParticle p) { return processMinMax(item, p.getMinOccurs(), p.getMaxOccurs()); }
/* 567 */   public Expression getSubstitionGroupList(XSElementDecl e) { Expression exp = (Expression)this.substitutionGroupCache.get(e);
/*     */     
/* 569 */     if (exp == null) {
/* 570 */       Set group = e.getSubstitutables();
/* 571 */       exp = Expression.nullSet;
/*     */       
/* 573 */       for (Iterator itr = group.iterator(); itr.hasNext(); ) {
/* 574 */         XSElementDecl decl = itr.next();
/* 575 */         if (decl == e || 
/* 576 */           decl.isAbstract())
/* 577 */           continue;  exp = this.pool.createChoice(exp, (Expression)this.selector.bindToType(decl));
/*     */       } 
/*     */       
/* 580 */       this.substitutionGroupCache.put(e, exp);
/*     */     } 
/*     */     
/* 583 */     return exp; } public Expression processMinMax(Expression item, int min, int max) { Expression exp = Expression.epsilon; for (int i = 0; i < min; i++)
/*     */       exp = this.pool.createSequence(item, exp);  if (max == -1) {
/*     */       if (min == 1)
/*     */         return this.pool.createOneOrMore(item);  Expression expression = this.pool.createSequence(exp, this.pool.createZeroOrMore(item)); if (min <= 1)
/*     */         return expression;  return (Expression)new OccurrenceExp(expression, max, min, item);
/*     */     } 
/*     */     if (max == 0)
/*     */       return Expression.nullSet; 
/*     */     Expression tmp = Expression.epsilon;
/*     */     for (int j = min; j < max; j++)
/*     */       tmp = this.pool.createOptional(this.pool.createSequence(item, tmp)); 
/*     */     Expression exactExp = this.pool.createSequence(exp, tmp);
/*     */     if (max == 1)
/*     */       return exactExp; 
/*     */     return (Expression)new OccurrenceExp(exactExp, max, min, item); }
/* 598 */   public final Expression getTypeSubstitutionList(XSComplexType ct, boolean strict) { if (!this.inExtensionMode)
/*     */     {
/* 600 */       return Expression.nullSet;
/*     */     }
/*     */     
/* 603 */     Expression exp = Expression.nullSet;
/*     */     
/* 605 */     XSType[] group = ct.listSubstitutables();
/*     */     
/* 607 */     for (int i = 0; i < group.length; i++) {
/* 608 */       if (!strict || !group[i].asComplexType().isAbstract())
/*     */       {
/* 610 */         exp = this.pool.createChoice(this.pool.createSequence(this.pool.createAttribute((NameClass)new SimpleNameClass("http://www.w3.org/2001/XMLSchema-instance", "type"), (Expression)new IgnoreItem(this.pool.createValue((Datatype)QnameType.theInstance, new StringPair("http://www.w3.org/2001/XMLSchema", "qname"), new QnameValueType(group[i].getTargetNamespace(), group[i].getName())), null)), this.selector.bindToType((XSComponent)group[i])), exp);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 623 */     return exp; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void _assert(boolean b) {
/* 635 */     if (!b) throw new JAXBAssertionError(); 
/*     */   }
/*     */   protected Expression applyRecursively(XSModelGroup mg, ParticleHandler f) { Expression[] exp = new Expression[mg.getSize()]; for (int i = 0; i < exp.length; i++)
/*     */       exp[i] = (Expression)f.particle(mg.getChild(i));  if (mg.getCompositor() == XSModelGroup.SEQUENCE) { Expression r = Expression.epsilon; for (int j = 0; j < exp.length; j++)
/*     */         r = this.pool.createSequence(r, exp[j]);  return r; }
/*     */      if (mg.getCompositor() == XSModelGroup.ALL) { Expression r = Expression.epsilon; for (int j = 0; j < exp.length; j++)
/*     */         r = this.pool.createInterleave(r, exp[j]);  return r; }
/*     */      if (mg.getCompositor() == XSModelGroup.CHOICE) {
/*     */       Expression r = Expression.nullSet; for (int j = 0; j < exp.length; j++)
/*     */         r = this.pool.createChoice(r, exp[j]);  return r;
/* 645 */     }  _assert(false); return null; } public Expression createXsiTypeExp(XSElementDecl decl) { return (Expression)new IgnoreItem(this.xsiTypeExp, decl.getLocator()); } public PackageTracker getPackageTracker() { throw new JAXBAssertionError(); }
/*     */ 
/*     */   
/*     */   public void reportError(Expression[] locations, String msg) {
/* 649 */     reportError(new Locator[0], msg);
/*     */   }
/*     */   
/*     */   public void reportError(Locator[] locations, String msg) {
/* 653 */     Locator loc = null;
/* 654 */     if (locations.length != 0) loc = locations[0];
/*     */     
/* 656 */     this.errorReceiver.error(loc, msg);
/*     */   }
/*     */   public ErrorReceiver getErrorReceiver() {
/* 659 */     return this.errorReceiver;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\BGMBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
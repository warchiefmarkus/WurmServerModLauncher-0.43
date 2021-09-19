/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*     */ 
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDocComment;
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.util.JavadocEscapeWriter;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.msv.util.LightStack;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.reader.Util;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.JClassFactory;
/*     */ import com.sun.tools.xjc.reader.xmlschema.PrefixedJClassFactoryImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.AGMFragmentBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.DOMBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.DefaultClassBinder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.JClassFactoryImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.Messages;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.ModelGroupBindingClassBinder;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.xjc.util.Util;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.impl.util.SchemaWriter;
/*     */ import com.sun.xml.xsom.util.ComponentNameFunction;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
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
/*     */ public class ClassSelector
/*     */ {
/*     */   private final String defaultPackageName;
/*     */   public final BGMBuilder builder;
/*     */   protected final AGMFragmentBuilder agmFragmentBuilder;
/*     */   public final CodeModelClassFactory codeModelClassFactory;
/*     */   private final Map bindMap;
/*     */   private final LightStack bindQueue;
/*     */   private final ClassBinder classBinder;
/*     */   private final DOMBinder domBinder;
/*     */   private final Stack classFactories;
/*     */   private Set reportedAbstractComplexTypes;
/*     */   
/*     */   public ClassSelector(BGMBuilder _builder, String defaultPackage) {
/*     */     ModelGroupBindingClassBinder modelGroupBindingClassBinder;
/*  76 */     this.bindMap = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.bindQueue = new LightStack();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     this.classFactories = new Stack();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     this.reportedAbstractComplexTypes = null;
/*     */     this.builder = _builder;
/*     */     this.agmFragmentBuilder = new AGMFragmentBuilder(_builder);
/*     */     this.codeModelClassFactory = new CodeModelClassFactory(this.builder.getErrorReceiver());
/*     */     this.domBinder = new DOMBinder(this);
/*     */     this.defaultPackageName = defaultPackage;
/*     */     DefaultClassBinder defaultClassBinder = new DefaultClassBinder(this);
/*     */     if (this.builder.getGlobalBinding().isModelGroupBinding())
/*     */       modelGroupBindingClassBinder = new ModelGroupBindingClassBinder(this, (ClassBinder)defaultClassBinder); 
/*     */     this.classBinder = (ClassBinder)modelGroupBindingClassBinder;
/*     */     this.classFactories.push(null);
/*     */   } public final JClassFactory getClassFactory() { return this.classFactories.peek(); } public final void pushClassFactory(JClassFactory clsFctry) { this.classFactories.push(clsFctry); } public final void popClassFactory() {
/*     */     this.classFactories.pop();
/*     */   }
/*     */   public ClassItem bindToType(XSComplexType ct) {
/*     */     return _bindToClass((XSComponent)ct, true);
/*     */   }
/* 266 */   private void checkAbstractComplexType(XSElementDecl decl) { if (this.builder.inExtensionMode) {
/*     */       return;
/*     */     }
/*     */     
/* 270 */     XSType t = decl.getType();
/* 271 */     if (t.isComplexType() && t.asComplexType().isAbstract()) {
/*     */ 
/*     */ 
/*     */       
/* 275 */       if (this.reportedAbstractComplexTypes == null) {
/* 276 */         this.reportedAbstractComplexTypes = new HashSet();
/*     */       }
/* 278 */       if (this.reportedAbstractComplexTypes.add(t)) {
/*     */         
/* 280 */         this.builder.errorReceiver.error(t.getLocator(), Messages.format("ClassSelector.AbstractComplexType", t.getName()));
/*     */         
/* 282 */         this.builder.errorReceiver.error(decl.getLocator(), Messages.format("ClassSelector.AbstractComplexType.SourceLocation"));
/*     */       } 
/*     */     }  }
/*     */ 
/*     */   
/*     */   public TypeItem bindToType(XSElementDecl e) {
/*     */     TypeItem t = this.domBinder.bind(e);
/*     */     if (t != null)
/*     */       return t; 
/*     */     return (TypeItem)_bindToClass((XSComponent)e, false);
/*     */   }
/*     */   public Expression bindToType(XSComponent sc) {
/*     */     Expression t = this.domBinder.bind(sc);
/*     */     if (t != null)
/*     */       return t; 
/*     */     return (Expression)_bindToClass(sc, false);
/*     */   }
/* 299 */   private boolean needValueConstructor(XSComponent sc) { if (!(sc instanceof XSElementDecl)) return false;
/*     */     
/* 301 */     XSElementDecl decl = (XSElementDecl)sc;
/* 302 */     if (!decl.getType().isSimpleType()) return false;
/*     */     
/* 304 */     return true; }
/*     */   private ClassItem _bindToClass(XSComponent sc, boolean cannotBeDelayed) { if (!this.bindMap.containsKey(sc)) { if (sc instanceof XSElementDecl) checkAbstractComplexType((XSElementDecl)sc);  boolean isGlobal = false; if (sc instanceof XSDeclaration) { isGlobal = ((XSDeclaration)sc).isGlobal(); if (isGlobal) pushClassFactory((JClassFactory)new JClassFactoryImpl(this, (JClassContainer)getPackage(((XSDeclaration)sc).getTargetNamespace())));  }  ClassItem ci = (ClassItem)sc.apply((XSFunction)this.classBinder); if (isGlobal) popClassFactory();  if (ci == null) return null;  queueBuild(sc, ci); }  Binding bind = (Binding)this.bindMap.get(sc); if (cannotBeDelayed) bind.build();  return Binding.access$200(bind); }
/*     */   public void executeTasks() { while (this.bindQueue.size() != 0)
/* 307 */       ((Binding)this.bindQueue.pop()).build();  } private static final String[] reservedClassNames = new String[] { "ObjectFactory" };
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueBuild(XSComponent sc, ClassItem ci) {
/* 312 */     Binding b = new Binding(this, sc, ci);
/* 313 */     this.bindQueue.push(b);
/* 314 */     Object o = this.bindMap.put(sc, b);
/* 315 */     _assert((o == null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void build(XSComponent sc, ClassItem ci) {
/* 322 */     _assert((Binding.access$200((Binding)this.bindMap.get(sc)) == ci));
/*     */     
/* 324 */     for (int i = 0; i < reservedClassNames.length; i++) {
/* 325 */       if (ci.getTypeAsDefined().name().equals(reservedClassNames[i])) {
/* 326 */         this.builder.errorReceiver.error(sc.getLocator(), Messages.format("ClassSelector.ReservedClassName", reservedClassNames[i]));
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 333 */     addSchemaFragmentJavadoc(ci.getTypeAsDefined().javadoc(), sc);
/*     */ 
/*     */ 
/*     */     
/* 337 */     if (Util.getSystemProperty(getClass(), "nestedInterface") != null) {
/* 338 */       pushClassFactory((JClassFactory)new PrefixedJClassFactoryImpl(this.builder, ci.getTypeAsDefined()));
/*     */     } else {
/*     */       
/* 341 */       pushClassFactory((JClassFactory)new JClassFactoryImpl(this, (JClassContainer)ci.getTypeAsDefined()));
/*     */     } 
/* 343 */     ci.exp = this.builder.fieldBuilder.build(sc);
/*     */     
/* 345 */     ci.agm.exp = this.agmFragmentBuilder.build(sc, ci);
/*     */     
/* 347 */     popClassFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 352 */     BIProperty prop = (BIProperty)this.builder.getBindInfo(sc).get(BIProperty.NAME);
/* 353 */     if (prop != null) prop.markAsAcknowledged();
/*     */ 
/*     */ 
/*     */     
/* 357 */     if (ci.hasGetContentMethod) {
/* 358 */       ci.exp.visit((ExpressionVisitorVoid)new Object(this));
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
/*     */   private void addSchemaFragmentJavadoc(JDocComment javadoc, XSComponent sc) {
/* 380 */     BindInfo bi = this.builder.getBindInfo(sc);
/* 381 */     String doc = bi.getDocumentation();
/*     */     
/* 383 */     if (doc != null && bi.hasTitleInDocumentation()) {
/* 384 */       javadoc.appendComment(doc);
/* 385 */       javadoc.appendComment("\n");
/*     */     } 
/*     */     
/* 388 */     StringWriter out = new StringWriter();
/* 389 */     SchemaWriter sw = new SchemaWriter((Writer)new JavadocEscapeWriter(out));
/* 390 */     sc.visit((XSVisitor)sw);
/*     */     
/* 392 */     Locator loc = sc.getLocator();
/* 393 */     String fileName = null;
/* 394 */     if (loc != null) {
/* 395 */       fileName = loc.getPublicId();
/* 396 */       if (fileName == null)
/* 397 */         fileName = loc.getSystemId(); 
/*     */     } 
/* 399 */     if (fileName == null) fileName = "";
/*     */     
/* 401 */     String lineNumber = Messages.format("ClassSelector.JavadocLineUnknown");
/* 402 */     if (loc != null && loc.getLineNumber() != -1) {
/* 403 */       lineNumber = String.valueOf(loc.getLineNumber());
/*     */     }
/* 405 */     String componentName = (String)sc.apply((XSFunction)new ComponentNameFunction());
/*     */     
/* 407 */     javadoc.appendComment(Messages.format("ClassSelector.JavadocHeading", componentName, fileName, lineNumber));
/*     */ 
/*     */ 
/*     */     
/* 411 */     if (doc != null && !bi.hasTitleInDocumentation()) {
/* 412 */       javadoc.appendComment("\n");
/* 413 */       javadoc.appendComment(doc);
/* 414 */       javadoc.appendComment("\n");
/*     */     } 
/*     */     
/* 417 */     javadoc.appendComment("\n<p>\n<pre>\n");
/* 418 */     javadoc.appendComment(out.getBuffer().toString());
/* 419 */     javadoc.appendComment("</pre>");
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
/* 441 */   private static Set checkedPackageNames = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage getPackage(String targetNamespace) {
/* 452 */     XSSchema s = this.builder.schemas.getSchema(targetNamespace);
/*     */     
/* 454 */     BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)s).get(BISchemaBinding.NAME);
/*     */ 
/*     */     
/* 457 */     String name = null;
/*     */ 
/*     */     
/* 460 */     if (this.defaultPackageName != null) {
/* 461 */       name = this.defaultPackageName;
/*     */     }
/*     */     
/* 464 */     if (name == null && sb != null && sb.getPackageName() != null) {
/* 465 */       name = sb.getPackageName();
/*     */     }
/*     */     
/* 468 */     if (name == null) {
/* 469 */       name = Util.getPackageNameFromNamespaceURI(targetNamespace, this.builder.getNameConverter());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 474 */     if (name == null) {
/* 475 */       name = "generated";
/*     */     }
/*     */ 
/*     */     
/* 479 */     if (checkedPackageNames.add(name))
/*     */     {
/* 481 */       if (!JJavaName.isJavaPackageName(name))
/*     */       {
/*     */ 
/*     */         
/* 485 */         this.builder.errorReceiver.error(s.getLocator(), Messages.format("ClassSelector.IncorrectPackageName", targetNamespace, name));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 490 */     return this.builder.grammar.codeModel._package(name);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void _assert(boolean b) {
/* 495 */     if (!b)
/* 496 */       throw new JAXBAssertionError(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\ClassSelector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
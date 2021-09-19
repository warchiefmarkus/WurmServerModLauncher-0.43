/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.util.JavadocEscapeWriter;
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.CClass;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.CElement;
/*     */ import com.sun.tools.xjc.model.CElementInfo;
/*     */ import com.sun.tools.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.LocalScoping;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassSelector
/*     */   extends BindingComponent
/*     */ {
/*  90 */   private final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   private final Map<XSComponent, Binding> bindMap = new HashMap<XSComponent, Binding>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   final Map<XSComponent, CElementInfo> boundElements = new HashMap<XSComponent, CElementInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   private final Stack<Binding> bindQueue = new Stack<Binding>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private final Set<CClassInfo> built = new HashSet<CClassInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassBinder classBinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   private final Stack<CClassInfoParent> classScopes = new Stack<CClassInfoParent>();
/*     */ 
/*     */   
/*     */   private XSComponent currentRoot;
/*     */ 
/*     */   
/*     */   private CClassInfo currentBean;
/*     */ 
/*     */   
/*     */   private final class Binding
/*     */   {
/*     */     private final XSComponent sc;
/*     */     
/*     */     private final CTypeInfo bean;
/*     */ 
/*     */     
/*     */     public Binding(XSComponent sc, CTypeInfo bean) {
/* 151 */       this.sc = sc;
/* 152 */       this.bean = bean;
/*     */     }
/*     */     
/*     */     void build() {
/* 156 */       if (!(this.bean instanceof CClassInfo)) {
/*     */         return;
/*     */       }
/* 159 */       CClassInfo bean = (CClassInfo)this.bean;
/*     */       
/* 161 */       if (!ClassSelector.this.built.add(bean)) {
/*     */         return;
/*     */       }
/* 164 */       for (String reservedClassName : ClassSelector.reservedClassNames) {
/* 165 */         if (bean.getName().equals(reservedClassName)) {
/* 166 */           ClassSelector.this.getErrorReporter().error(this.sc.getLocator(), "ClassSelector.ReservedClassName", new Object[] { reservedClassName });
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 175 */       if (ClassSelector.this.needValueConstructor(this.sc))
/*     */       {
/*     */         
/* 178 */         bean.addConstructor(new String[] { "value" });
/*     */       }
/*     */       
/* 181 */       if (bean.javadoc == null) {
/* 182 */         ClassSelector.this.addSchemaFragmentJavadoc(bean, this.sc);
/*     */       }
/*     */       
/* 185 */       if (ClassSelector.this.builder.getGlobalBinding().getFlattenClasses() == LocalScoping.NESTED) {
/* 186 */         ClassSelector.this.pushClassScope((CClassInfoParent)bean);
/*     */       } else {
/* 188 */         ClassSelector.this.pushClassScope(bean.parent());
/* 189 */       }  XSComponent oldRoot = ClassSelector.this.currentRoot;
/* 190 */       CClassInfo oldBean = ClassSelector.this.currentBean;
/* 191 */       ClassSelector.this.currentRoot = this.sc;
/* 192 */       ClassSelector.this.currentBean = bean;
/* 193 */       this.sc.visit((XSVisitor)Ring.get(BindRed.class));
/* 194 */       ClassSelector.this.currentBean = oldBean;
/* 195 */       ClassSelector.this.currentRoot = oldRoot;
/* 196 */       ClassSelector.this.popClassScope();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 201 */       BIProperty prop = (BIProperty)ClassSelector.this.builder.getBindInfo(this.sc).get(BIProperty.class);
/* 202 */       if (prop != null) prop.markAsAcknowledged();
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassSelector() {
/* 209 */     this.classBinder = new Abstractifier(new DefaultClassBinder());
/* 210 */     Ring.add(ClassBinder.class, this.classBinder);
/*     */     
/* 212 */     this.classScopes.push((CClassInfoParent)null);
/*     */     
/* 214 */     XSComplexType anyType = ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getComplexType("http://www.w3.org/2001/XMLSchema", "anyType");
/* 215 */     this.bindMap.put(anyType, new Binding((XSComponent)anyType, (CTypeInfo)CBuiltinLeafInfo.ANYTYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   public final CClassInfoParent getClassScope() {
/* 220 */     assert !this.classScopes.isEmpty();
/* 221 */     return this.classScopes.peek();
/*     */   }
/*     */   
/*     */   public final void pushClassScope(CClassInfoParent clsFctry) {
/* 225 */     assert clsFctry != null;
/* 226 */     this.classScopes.push(clsFctry);
/*     */   }
/*     */   
/*     */   public final void popClassScope() {
/* 230 */     this.classScopes.pop();
/*     */   }
/*     */   
/*     */   public XSComponent getCurrentRoot() {
/* 234 */     return this.currentRoot;
/*     */   }
/*     */   
/*     */   public CClassInfo getCurrentBean() {
/* 238 */     return this.currentBean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CElement isBound(XSElementDecl x, XSComponent referer) {
/* 245 */     CElementInfo r = this.boundElements.get(x);
/* 246 */     if (r != null)
/* 247 */       return (CElement)r; 
/* 248 */     return bindToType(x, referer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CTypeInfo bindToType(XSComponent sc, XSComponent referer) {
/* 257 */     return _bindToClass(sc, referer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CElement bindToType(XSElementDecl e, XSComponent referer) {
/* 267 */     return (CElement)_bindToClass((XSComponent)e, referer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CClass bindToType(XSComplexType t, XSComponent referer, boolean cannotBeDelayed) {
/* 275 */     return (CClass)_bindToClass((XSComponent)t, referer, cannotBeDelayed);
/*     */   }
/*     */   
/*     */   public TypeUse bindToType(XSType t, XSComponent referer) {
/* 279 */     if (t instanceof XSSimpleType) {
/* 280 */       return ((SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class)).build((XSSimpleType)t);
/*     */     }
/* 282 */     return (TypeUse)_bindToClass((XSComponent)t, referer, false);
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
/*     */   CTypeInfo _bindToClass(@NotNull XSComponent sc, XSComponent referer, boolean cannotBeDelayed) {
/* 301 */     if (!this.bindMap.containsKey(sc)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 306 */       boolean isGlobal = false;
/* 307 */       if (sc instanceof XSDeclaration) {
/* 308 */         isGlobal = ((XSDeclaration)sc).isGlobal();
/* 309 */         if (isGlobal) {
/* 310 */           pushClassScope((CClassInfoParent)new CClassInfoParent.Package(getPackage(((XSDeclaration)sc).getTargetNamespace())));
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 315 */       CElement bean = (CElement)sc.apply(this.classBinder);
/*     */       
/* 317 */       if (isGlobal) {
/* 318 */         popClassScope();
/*     */       }
/* 320 */       if (bean == null) {
/* 321 */         return null;
/*     */       }
/*     */       
/* 324 */       if (bean instanceof CClassInfo) {
/* 325 */         XSSchema os = sc.getOwnerSchema();
/* 326 */         BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)os).get(BISchemaBinding.class);
/* 327 */         if (sb != null && !sb.map) {
/*     */           
/* 329 */           getErrorReporter().error(sc.getLocator(), "ERR_REFERENCE_TO_NONEXPORTED_CLASS", new Object[] { sc.apply((XSFunction)new ComponentNameFunction()) });
/*     */           
/* 331 */           getErrorReporter().error(sb.getLocation(), "ERR_REFERENCE_TO_NONEXPORTED_CLASS_MAP_FALSE", new Object[] { os.getTargetNamespace() });
/*     */           
/* 333 */           if (referer != null) {
/* 334 */             getErrorReporter().error(referer.getLocator(), "ERR_REFERENCE_TO_NONEXPORTED_CLASS_REFERER", new Object[] { referer.apply((XSFunction)new ComponentNameFunction()) });
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 340 */       queueBuild(sc, bean);
/*     */     } 
/*     */     
/* 343 */     Binding bind = this.bindMap.get(sc);
/* 344 */     if (cannotBeDelayed) {
/* 345 */       bind.build();
/*     */     }
/* 347 */     return bind.bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void executeTasks() {
/* 354 */     while (this.bindQueue.size() != 0) {
/* 355 */       ((Binding)this.bindQueue.pop()).build();
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
/*     */   private boolean needValueConstructor(XSComponent sc) {
/* 370 */     if (!(sc instanceof XSElementDecl)) return false;
/*     */     
/* 372 */     XSElementDecl decl = (XSElementDecl)sc;
/* 373 */     if (!decl.getType().isSimpleType()) return false;
/*     */     
/* 375 */     return true;
/*     */   }
/*     */   
/* 378 */   private static final String[] reservedClassNames = new String[] { "ObjectFactory" };
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueBuild(XSComponent sc, CElement bean) {
/* 383 */     Binding b = new Binding(sc, (CTypeInfo)bean);
/* 384 */     this.bindQueue.push(b);
/* 385 */     Binding old = this.bindMap.put(sc, b);
/* 386 */     assert old == null || old.bean == bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addSchemaFragmentJavadoc(CClassInfo bean, XSComponent sc) {
/* 396 */     String doc = this.builder.getBindInfo(sc).getDocumentation();
/* 397 */     if (doc != null) {
/* 398 */       append(bean, doc);
/*     */     }
/*     */     
/* 401 */     Locator loc = sc.getLocator();
/* 402 */     String fileName = null;
/* 403 */     if (loc != null) {
/* 404 */       fileName = loc.getPublicId();
/* 405 */       if (fileName == null)
/* 406 */         fileName = loc.getSystemId(); 
/*     */     } 
/* 408 */     if (fileName == null) fileName = "";
/*     */     
/* 410 */     String lineNumber = Messages.format("ClassSelector.JavadocLineUnknown", new Object[0]);
/* 411 */     if (loc != null && loc.getLineNumber() != -1) {
/* 412 */       lineNumber = String.valueOf(loc.getLineNumber());
/*     */     }
/* 414 */     String componentName = (String)sc.apply((XSFunction)new ComponentNameFunction());
/* 415 */     String jdoc = Messages.format("ClassSelector.JavadocHeading", new Object[] { componentName, fileName, lineNumber });
/* 416 */     append(bean, jdoc);
/*     */ 
/*     */     
/* 419 */     StringWriter out = new StringWriter();
/* 420 */     out.write("<pre>\n");
/* 421 */     SchemaWriter sw = new SchemaWriter((Writer)new JavadocEscapeWriter(out));
/* 422 */     sc.visit((XSVisitor)sw);
/* 423 */     out.write("</pre>");
/* 424 */     append(bean, out.toString());
/*     */   }
/*     */   
/*     */   private void append(CClassInfo bean, String doc) {
/* 428 */     if (bean.javadoc == null) {
/* 429 */       bean.javadoc = doc + '\n';
/*     */     } else {
/* 431 */       bean.javadoc += '\n' + doc + '\n';
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 441 */   private static Set<String> checkedPackageNames = new HashSet<String>();
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
/* 452 */     XSSchema s = ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getSchema(targetNamespace);
/*     */     
/* 454 */     BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)s).get(BISchemaBinding.class);
/*     */     
/* 456 */     if (sb != null) sb.markAsAcknowledged();
/*     */     
/* 458 */     String name = null;
/*     */ 
/*     */     
/* 461 */     if (this.builder.defaultPackage1 != null) {
/* 462 */       name = this.builder.defaultPackage1;
/*     */     }
/*     */     
/* 465 */     if (name == null && sb != null && sb.getPackageName() != null) {
/* 466 */       name = sb.getPackageName();
/*     */     }
/*     */     
/* 469 */     if (name == null && this.builder.defaultPackage2 != null) {
/* 470 */       name = this.builder.defaultPackage2;
/*     */     }
/*     */     
/* 473 */     if (name == null) {
/* 474 */       name = this.builder.getNameConverter().toPackageName(targetNamespace);
/*     */     }
/*     */ 
/*     */     
/* 478 */     if (name == null) {
/* 479 */       name = "generated";
/*     */     }
/*     */ 
/*     */     
/* 483 */     if (checkedPackageNames.add(name))
/*     */     {
/* 485 */       if (!JJavaName.isJavaPackageName(name))
/*     */       {
/*     */ 
/*     */         
/* 489 */         getErrorReporter().error(s.getLocator(), "ClassSelector.IncorrectPackageName", new Object[] { targetNamespace, name });
/*     */       }
/*     */     }
/*     */     
/* 493 */     return ((JCodeModel)Ring.get(JCodeModel.class))._package(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ClassSelector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
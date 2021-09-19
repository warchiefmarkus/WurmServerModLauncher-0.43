/*     */ package 1.0.com.sun.codemodel;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDeclaration;
/*     */ import com.sun.codemodel.JDocComment;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JMods;
/*     */ import com.sun.codemodel.JNestedClass;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class JDefinedClass extends JClass implements JDeclaration, JClassContainer {
/*  25 */   private String name = null;
/*     */ 
/*     */   
/*     */   private final boolean isInterface;
/*     */ 
/*     */   
/*     */   private JMods mods;
/*     */ 
/*     */   
/*     */   private JClass superClass;
/*     */ 
/*     */   
/*  37 */   private final List interfaces = new ArrayList();
/*     */ 
/*     */   
/*  40 */   private final List fields = new ArrayList();
/*     */ 
/*     */   
/*  43 */   private final Map fieldsByName = new HashMap();
/*     */ 
/*     */   
/*  46 */   private JBlock init = null;
/*     */ 
/*     */   
/*  49 */   private JDocComment jdoc = null;
/*     */ 
/*     */   
/*  52 */   private final List constructors = new ArrayList();
/*     */ 
/*     */   
/*  55 */   private final List methods = new ArrayList();
/*     */ 
/*     */   
/*  58 */   private final Map classes = new TreeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map upperCaseClassMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hideFile = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object metadata;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String directBlock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JDefinedClass(int mods, String name, boolean isInterface, JCodeModel owner) {
/*  96 */     super(owner);
/*     */     
/*  98 */     if (JCodeModel.isCaseSensitiveFileSystem) {
/*  99 */       this.upperCaseClassMap = null;
/*     */     } else {
/* 101 */       this.upperCaseClassMap = new HashMap();
/*     */     } 
/* 103 */     if (name.trim().length() == 0) {
/* 104 */       throw new IllegalArgumentException("JClass name empty");
/*     */     }
/* 106 */     if (!Character.isJavaIdentifierStart(name.charAt(0))) {
/* 107 */       String msg = "JClass name " + name + " contains illegal character" + " for beginning of identifier: " + name.charAt(0);
/*     */       
/* 109 */       throw new IllegalArgumentException(msg);
/*     */     } 
/* 111 */     for (int i = 1; i < name.length(); i++) {
/* 112 */       if (!Character.isJavaIdentifierPart(name.charAt(i))) {
/* 113 */         String msg = "JClass name " + name + " contains illegal character " + name.charAt(i);
/*     */         
/* 115 */         throw new IllegalArgumentException(msg);
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     this.mods = isInterface ? JMods.forInterface(mods) : JMods.forClass(mods);
/*     */ 
/*     */     
/* 122 */     this.name = name;
/* 123 */     this.isInterface = isInterface;
/*     */ 
/*     */     
/* 126 */     if (!isInterface) {
/* 127 */       this.superClass = owner().ref(Object.class);
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
/*     */   public com.sun.codemodel.JDefinedClass _extends(JClass superClass) {
/* 139 */     if (isInterface()) {
/* 140 */       throw new IllegalArgumentException("unable to set the super class for an interface");
/*     */     }
/* 142 */     if (superClass == null) {
/* 143 */       throw new NullPointerException();
/*     */     }
/* 145 */     this.superClass = superClass;
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public com.sun.codemodel.JDefinedClass _extends(Class superClass) {
/* 150 */     return _extends(owner().ref(superClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass _extends() {
/* 158 */     return this.superClass;
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
/*     */   public com.sun.codemodel.JDefinedClass _implements(JClass iface) {
/* 170 */     this.interfaces.add(iface);
/* 171 */     return this;
/*     */   }
/*     */   
/*     */   public com.sun.codemodel.JDefinedClass _implements(Class iface) {
/* 175 */     return _implements(owner().ref(iface));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator _implements() {
/* 183 */     return this.interfaces.iterator();
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
/*     */   public String name() {
/* 196 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/* 200 */     return this.isInterface;
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
/*     */   public JFieldVar field(int mods, JType type, String name) {
/* 218 */     return field(mods, type, name, null);
/*     */   }
/*     */   
/*     */   public JFieldVar field(int mods, Class type, String name) {
/* 222 */     return field(mods, (JType)owner().ref(type), name);
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
/*     */   public JFieldVar field(int mods, JType type, String name, JExpression init) {
/* 240 */     JFieldVar f = new JFieldVar(JMods.forField(mods), type, name, init);
/* 241 */     this.fields.add(f);
/*     */     
/* 243 */     JFieldVar existing = (JFieldVar)this.fieldsByName.get(name);
/* 244 */     if (existing != null)
/* 245 */       this.fields.remove(existing); 
/* 246 */     this.fieldsByName.put(name, f);
/*     */     
/* 248 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   public JFieldVar field(int mods, Class type, String name, JExpression init) {
/* 253 */     return field(mods, (JType)owner().ref(type), name, init);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator fields() {
/* 260 */     return this.fields.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock init() {
/* 270 */     if (this.init == null) this.init = new JBlock(); 
/* 271 */     return this.init;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod constructor(int mods) {
/* 281 */     JMethod c = new JMethod(mods, this);
/* 282 */     this.constructors.add(c);
/* 283 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator constructors() {
/* 290 */     return this.constructors.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod getConstructor(JType[] argTypes) {
/* 301 */     for (Iterator itr = this.constructors.iterator(); itr.hasNext(); ) {
/* 302 */       JMethod m = itr.next();
/*     */       
/* 304 */       if (m.hasSignature(argTypes))
/* 305 */         return m; 
/*     */     } 
/* 307 */     return null;
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
/*     */   public JMethod method(int mods, JType type, String name) {
/* 326 */     JMethod m = new JMethod(this, mods, type, name);
/* 327 */     this.methods.add(m);
/* 328 */     return m;
/*     */   }
/*     */   
/*     */   public JMethod method(int mods, Class type, String name) {
/* 332 */     return method(mods, (JType)owner().ref(type), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator methods() {
/* 339 */     return this.methods.iterator();
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
/*     */   public JMethod getMethod(String name, JType[] argTypes) {
/* 351 */     for (Iterator itr = this.methods.iterator(); itr.hasNext(); ) {
/* 352 */       JMethod m = itr.next();
/* 353 */       if (!m.name().equals(name)) {
/*     */         continue;
/*     */       }
/* 356 */       if (m.hasSignature(argTypes))
/* 357 */         return m; 
/*     */     } 
/* 359 */     return null;
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
/*     */   public com.sun.codemodel.JDefinedClass _class(int mods, String name) throws JClassAlreadyExistsException {
/* 374 */     return _class(mods, name, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDefinedClass _class(int mods, String name, boolean isInterface) throws JClassAlreadyExistsException {
/* 380 */     if (this.classes.containsKey(name)) {
/* 381 */       throw new JClassAlreadyExistsException((com.sun.codemodel.JDefinedClass)this.classes.get(name));
/*     */     }
/*     */     
/* 384 */     JNestedClass jNestedClass = new JNestedClass(this, mods, name, isInterface);
/* 385 */     if (this.upperCaseClassMap != null) {
/* 386 */       com.sun.codemodel.JDefinedClass dc = (com.sun.codemodel.JDefinedClass)this.upperCaseClassMap.get(name.toUpperCase());
/* 387 */       if (dc != null)
/* 388 */         throw new JClassAlreadyExistsException(dc); 
/* 389 */       this.upperCaseClassMap.put(name.toUpperCase(), jNestedClass);
/*     */     } 
/* 391 */     this.classes.put(name, jNestedClass);
/* 392 */     return (com.sun.codemodel.JDefinedClass)jNestedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDefinedClass _class(String name) throws JClassAlreadyExistsException {
/* 400 */     return _class(1, name);
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
/*     */   public com.sun.codemodel.JDefinedClass _interface(int mods, String name) throws JClassAlreadyExistsException {
/* 415 */     return _class(mods, name, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JDefinedClass _interface(String name) throws JClassAlreadyExistsException {
/* 422 */     return _interface(1, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 432 */     if (this.jdoc == null)
/* 433 */       this.jdoc = new JDocComment(); 
/* 434 */     return this.jdoc;
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
/*     */   public void hide() {
/* 446 */     this.hideFile = true;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/* 450 */     return this.hideFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Iterator classes() {
/* 459 */     return this.classes.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass[] listClasses() {
/* 466 */     return (JClass[])this.classes.values().toArray((Object[])new JClass[this.classes.values().size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass outer() {
/* 474 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void declare(JFormatter f) {
/* 480 */     if (this.jdoc != null) {
/* 481 */       f.nl().g((JGenerable)this.jdoc);
/*     */     }
/* 483 */     f.g((JGenerable)this.mods).p(this.isInterface ? "interface" : "class").p(this.name);
/*     */     
/* 485 */     if (this.superClass != null && this.superClass != owner().ref(Object.class))
/*     */     {
/* 487 */       f.nl().i().p("extends").g((JGenerable)this.superClass).nl().o();
/*     */     }
/* 489 */     if (!this.interfaces.isEmpty()) {
/* 490 */       boolean first = true;
/* 491 */       if (this.superClass == null) f.nl(); 
/* 492 */       f.i().p(this.isInterface ? "extends" : "implements");
/* 493 */       for (Iterator i = this.interfaces.iterator(); i.hasNext(); ) {
/* 494 */         if (!first) f.p(','); 
/* 495 */         f.g((JGenerable)i.next());
/* 496 */         first = false;
/*     */       } 
/* 498 */       f.nl().o();
/*     */     } 
/* 500 */     declareBody(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void declareBody(JFormatter f) {
/* 507 */     f.p('{').nl().nl().i();
/* 508 */     for (Iterator iterator = this.fields.iterator(); iterator.hasNext();) {
/* 509 */       f.d((JDeclaration)iterator.next());
/*     */     }
/* 511 */     if (this.init != null)
/* 512 */       f.nl().p("static").s((JStatement)this.init); 
/* 513 */     for (Iterator iterator2 = this.constructors.iterator(); iterator2.hasNext();) {
/* 514 */       f.nl().d((JDeclaration)iterator2.next());
/*     */     }
/* 516 */     for (Iterator iterator1 = this.methods.iterator(); iterator1.hasNext();) {
/* 517 */       f.nl().d((JDeclaration)iterator1.next());
/*     */     }
/* 519 */     for (Iterator i = this.classes.values().iterator(); i.hasNext();) {
/* 520 */       f.nl().d(i.next());
/*     */     }
/* 522 */     if (this.directBlock != null)
/* 523 */       f.p(this.directBlock); 
/* 524 */     f.nl().o().p('}').nl();
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 528 */     f.p(fullName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void direct(String string) {
/* 539 */     if (this.directBlock == null) {
/* 540 */       this.directBlock = string;
/*     */     } else {
/* 542 */       this.directBlock += string;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JDefinedClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
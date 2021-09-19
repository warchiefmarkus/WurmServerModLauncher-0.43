/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDefinedClass
/*     */   extends JClass
/*     */   implements JDeclaration, JClassContainer, JGenerifiable, JAnnotatable
/*     */ {
/*  52 */   private String name = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private JMods mods;
/*     */ 
/*     */   
/*     */   private JClass superClass;
/*     */ 
/*     */   
/*  62 */   private final Set<JClass> interfaces = new TreeSet<JClass>();
/*     */ 
/*     */   
/*  65 */   final Map<String, JFieldVar> fields = new LinkedHashMap<String, JFieldVar>();
/*     */ 
/*     */   
/*  68 */   private JBlock init = null;
/*     */ 
/*     */   
/*  71 */   private JDocComment jdoc = null;
/*     */ 
/*     */   
/*  74 */   private final List<JMethod> constructors = new ArrayList<JMethod>();
/*     */ 
/*     */   
/*  77 */   private final List<JMethod> methods = new ArrayList<JMethod>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, JDefinedClass> classes;
/*     */ 
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
/*     */   
/*     */   public Object metadata;
/*     */ 
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
/* 116 */   private JClassContainer outer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassType classType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   private final Map<String, JEnumConstant> enumConstantsByName = new LinkedHashMap<String, JEnumConstant>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   private final JGenerifiableImpl generifiable = new JGenerifiableImpl() {
/*     */       protected JCodeModel owner() {
/* 149 */         return JDefinedClass.this.owner();
/*     */       }
/*     */     };
/*     */   
/*     */   JDefinedClass(JClassContainer parent, int mods, String name, ClassType classTypeval) {
/* 154 */     this(mods, name, parent, parent.owner(), classTypeval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JDefinedClass(JCodeModel owner, int mods, String name) {
/* 164 */     this(mods, name, (JClassContainer)null, owner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JDefinedClass(int mods, String name, JClassContainer parent, JCodeModel owner) {
/* 172 */     this(mods, name, parent, owner, ClassType.CLASS);
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
/*     */   private JDefinedClass(int mods, String name, JClassContainer parent, JCodeModel owner, ClassType classTypeVal) {
/* 190 */     super(owner);
/*     */     
/* 192 */     if (name != null) {
/* 193 */       if (name.trim().length() == 0) {
/* 194 */         throw new IllegalArgumentException("JClass name empty");
/*     */       }
/* 196 */       if (!Character.isJavaIdentifierStart(name.charAt(0))) {
/* 197 */         String msg = "JClass name " + name + " contains illegal character" + " for beginning of identifier: " + name.charAt(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 203 */         throw new IllegalArgumentException(msg);
/*     */       } 
/* 205 */       for (int i = 1; i < name.length(); i++) {
/* 206 */         if (!Character.isJavaIdentifierPart(name.charAt(i))) {
/* 207 */           String msg = "JClass name " + name + " contains illegal character " + name.charAt(i);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 212 */           throw new IllegalArgumentException(msg);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 217 */     this.classType = classTypeVal;
/* 218 */     if (isInterface()) {
/* 219 */       this.mods = JMods.forInterface(mods);
/*     */     } else {
/* 221 */       this.mods = JMods.forClass(mods);
/*     */     } 
/* 223 */     this.name = name;
/*     */     
/* 225 */     this.outer = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAnonymous() {
/* 232 */     return (this.name == null);
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
/*     */   public JDefinedClass _extends(JClass superClass) {
/* 244 */     if (this.classType == ClassType.INTERFACE)
/* 245 */       throw new IllegalArgumentException("unable to set the super class for an interface"); 
/* 246 */     if (superClass == null) {
/* 247 */       throw new NullPointerException();
/*     */     }
/* 249 */     for (JClass o = superClass.outer(); o != null; o = o.outer()) {
/* 250 */       if (this == o) {
/* 251 */         throw new IllegalArgumentException("Illegal class inheritance loop.  Outer class " + this.name + " may not subclass from inner class: " + o.name());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 256 */     this.superClass = superClass;
/* 257 */     return this;
/*     */   }
/*     */   
/*     */   public JDefinedClass _extends(Class superClass) {
/* 261 */     return _extends(owner().ref(superClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass _extends() {
/* 268 */     if (this.superClass == null)
/* 269 */       this.superClass = owner().ref(Object.class); 
/* 270 */     return this.superClass;
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
/*     */   public JDefinedClass _implements(JClass iface) {
/* 282 */     this.interfaces.add(iface);
/* 283 */     return this;
/*     */   }
/*     */   
/*     */   public JDefinedClass _implements(Class iface) {
/* 287 */     return _implements(owner().ref(iface));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JClass> _implements() {
/* 295 */     return this.interfaces.iterator();
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
/* 308 */     return this.name;
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
/*     */   public JEnumConstant enumConstant(String name) {
/* 322 */     JEnumConstant ec = this.enumConstantsByName.get(name);
/* 323 */     if (null == ec) {
/* 324 */       ec = new JEnumConstant(this, name);
/* 325 */       this.enumConstantsByName.put(name, ec);
/*     */     } 
/* 327 */     return ec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String fullName() {
/* 334 */     if (this.outer instanceof JDefinedClass) {
/* 335 */       return ((JDefinedClass)this.outer).fullName() + '.' + name();
/*     */     }
/* 337 */     JPackage p = _package();
/* 338 */     if (p.isUnnamed()) {
/* 339 */       return name();
/*     */     }
/* 341 */     return p.name() + '.' + name();
/*     */   }
/*     */   
/*     */   public String binaryName() {
/* 345 */     if (this.outer instanceof JDefinedClass) {
/* 346 */       return ((JDefinedClass)this.outer).binaryName() + '$' + name();
/*     */     }
/* 348 */     return fullName();
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/* 352 */     return (this.classType == ClassType.INTERFACE);
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 356 */     return this.mods.isAbstract();
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
/* 374 */     return field(mods, type, name, (JExpression)null);
/*     */   }
/*     */   
/*     */   public JFieldVar field(int mods, Class type, String name) {
/* 378 */     return field(mods, owner()._ref(type), name);
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
/*     */   public JFieldVar field(int mods, JType type, String name, JExpression init) {
/* 400 */     JFieldVar f = new JFieldVar(this, JMods.forField(mods), type, name, init);
/*     */     
/* 402 */     if (this.fields.put(name, f) != null) {
/* 403 */       throw new IllegalArgumentException("trying to create the same field twice: " + name);
/*     */     }
/* 405 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnnotationTypeDeclaration() {
/* 413 */     return (this.classType == ClassType.ANNOTATION_TYPE_DECL);
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
/*     */   public JDefinedClass _annotationTypeDeclaration(String name) throws JClassAlreadyExistsException {
/* 429 */     return _class(1, name, ClassType.ANNOTATION_TYPE_DECL);
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
/*     */   public JDefinedClass _enum(String name) throws JClassAlreadyExistsException {
/* 443 */     return _class(1, name, ClassType.ENUM);
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
/*     */   public JDefinedClass _enum(int mods, String name) throws JClassAlreadyExistsException {
/* 459 */     return _class(mods, name, ClassType.ENUM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassType getClassType() {
/* 467 */     return this.classType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFieldVar field(int mods, Class type, String name, JExpression init) {
/* 475 */     return field(mods, owner()._ref(type), name, init);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, JFieldVar> fields() {
/* 485 */     return Collections.unmodifiableMap(this.fields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeField(JFieldVar field) {
/* 495 */     if (this.fields.remove(field.name()) != field) {
/* 496 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock init() {
/* 506 */     if (this.init == null)
/* 507 */       this.init = new JBlock(); 
/* 508 */     return this.init;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod constructor(int mods) {
/* 518 */     JMethod c = new JMethod(mods, this);
/* 519 */     this.constructors.add(c);
/* 520 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator constructors() {
/* 527 */     return this.constructors.iterator();
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
/* 538 */     for (JMethod m : this.constructors) {
/* 539 */       if (m.hasSignature(argTypes))
/* 540 */         return m; 
/*     */     } 
/* 542 */     return null;
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
/* 561 */     JMethod m = new JMethod(this, mods, type, name);
/* 562 */     this.methods.add(m);
/* 563 */     return m;
/*     */   }
/*     */   
/*     */   public JMethod method(int mods, Class type, String name) {
/* 567 */     return method(mods, owner()._ref(type), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<JMethod> methods() {
/* 574 */     return this.methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod getMethod(String name, JType[] argTypes) {
/* 585 */     for (JMethod m : this.methods) {
/* 586 */       if (!m.name().equals(name)) {
/*     */         continue;
/*     */       }
/* 589 */       if (m.hasSignature(argTypes))
/* 590 */         return m; 
/*     */     } 
/* 592 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isClass() {
/* 596 */     return true;
/*     */   }
/*     */   public boolean isPackage() {
/* 599 */     return false;
/*     */   } public JPackage getPackage() {
/* 601 */     return parentContainer().getPackage();
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
/*     */   public JDefinedClass _class(int mods, String name) throws JClassAlreadyExistsException {
/* 616 */     return _class(mods, name, ClassType.CLASS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, boolean isInterface) throws JClassAlreadyExistsException {
/* 625 */     return _class(mods, name, isInterface ? ClassType.INTERFACE : ClassType.CLASS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, ClassType classTypeVal) throws JClassAlreadyExistsException {
/*     */     String NAME;
/* 632 */     if (JCodeModel.isCaseSensitiveFileSystem) {
/* 633 */       NAME = name.toUpperCase();
/*     */     } else {
/* 635 */       NAME = name;
/*     */     } 
/* 637 */     if (getClasses().containsKey(NAME)) {
/* 638 */       throw new JClassAlreadyExistsException((JDefinedClass)getClasses().get(NAME));
/*     */     }
/*     */     
/* 641 */     JDefinedClass c = new JDefinedClass(this, mods, name, classTypeVal);
/* 642 */     getClasses().put(NAME, c);
/* 643 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String name) throws JClassAlreadyExistsException {
/* 652 */     return _class(1, name);
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
/*     */   public JDefinedClass _interface(int mods, String name) throws JClassAlreadyExistsException {
/* 668 */     return _class(mods, name, ClassType.INTERFACE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _interface(String name) throws JClassAlreadyExistsException {
/* 676 */     return _interface(1, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 686 */     if (this.jdoc == null)
/* 687 */       this.jdoc = new JDocComment(owner()); 
/* 688 */     return this.jdoc;
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
/* 700 */     this.hideFile = true;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/* 704 */     return this.hideFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Iterator<JDefinedClass> classes() {
/* 712 */     if (this.classes == null) {
/* 713 */       return Collections.<JDefinedClass>emptyList().iterator();
/*     */     }
/* 715 */     return this.classes.values().iterator();
/*     */   }
/*     */   
/*     */   private Map<String, JDefinedClass> getClasses() {
/* 719 */     if (this.classes == null)
/* 720 */       this.classes = new TreeMap<String, JDefinedClass>(); 
/* 721 */     return this.classes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass[] listClasses() {
/* 729 */     if (this.classes == null) {
/* 730 */       return new JClass[0];
/*     */     }
/* 732 */     return (JClass[])this.classes.values().toArray((Object[])new JClass[this.classes.values().size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public JClass outer() {
/* 737 */     if (this.outer.isClass()) {
/* 738 */       return (JClass)this.outer;
/*     */     }
/* 740 */     return null;
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 744 */     if (this.jdoc != null) {
/* 745 */       f.nl().g(this.jdoc);
/*     */     }
/* 747 */     if (this.annotations != null) {
/* 748 */       for (JAnnotationUse annotation : this.annotations) {
/* 749 */         f.g(annotation).nl();
/*     */       }
/*     */     }
/* 752 */     f.g(this.mods).p(this.classType.declarationToken).id(this.name).d(this.generifiable);
/*     */     
/* 754 */     if (this.superClass != null && this.superClass != owner().ref(Object.class)) {
/* 755 */       f.nl().i().p("extends").g(this.superClass).nl().o();
/*     */     }
/* 757 */     if (!this.interfaces.isEmpty()) {
/* 758 */       if (this.superClass == null)
/* 759 */         f.nl(); 
/* 760 */       f.i().p((this.classType == ClassType.INTERFACE) ? "extends" : "implements");
/* 761 */       f.g((Collection)this.interfaces);
/* 762 */       f.nl().o();
/*     */     } 
/* 764 */     declareBody(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void declareBody(JFormatter f) {
/* 771 */     f.p('{').nl().nl().i();
/* 772 */     boolean first = true;
/*     */     
/* 774 */     if (!this.enumConstantsByName.isEmpty()) {
/* 775 */       for (JEnumConstant c : this.enumConstantsByName.values()) {
/* 776 */         if (!first) f.p(',').nl(); 
/* 777 */         f.d(c);
/* 778 */         first = false;
/*     */       } 
/* 780 */       f.p(';').nl();
/*     */     } 
/*     */     
/* 783 */     for (JFieldVar field : this.fields.values())
/* 784 */       f.d(field); 
/* 785 */     if (this.init != null)
/* 786 */       f.nl().p("static").s(this.init); 
/* 787 */     for (JMethod m : this.constructors) {
/* 788 */       f.nl().d(m);
/*     */     }
/* 790 */     for (JMethod m : this.methods) {
/* 791 */       f.nl().d(m);
/*     */     }
/* 793 */     if (this.classes != null) {
/* 794 */       for (JDefinedClass dc : this.classes.values()) {
/* 795 */         f.nl().d(dc);
/*     */       }
/*     */     }
/* 798 */     if (this.directBlock != null)
/* 799 */       f.p(this.directBlock); 
/* 800 */     f.nl().o().p('}').nl();
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
/* 811 */     if (this.directBlock == null) {
/* 812 */       this.directBlock = string;
/*     */     } else {
/* 814 */       this.directBlock += string;
/*     */     } 
/*     */   }
/*     */   public final JPackage _package() {
/* 818 */     JClassContainer p = this.outer;
/* 819 */     while (!(p instanceof JPackage))
/* 820 */       p = p.parentContainer(); 
/* 821 */     return (JPackage)p;
/*     */   }
/*     */   
/*     */   public final JClassContainer parentContainer() {
/* 825 */     return this.outer;
/*     */   }
/*     */   
/*     */   public JTypeVar generify(String name) {
/* 829 */     return this.generifiable.generify(name);
/*     */   }
/*     */   public JTypeVar generify(String name, Class bound) {
/* 832 */     return this.generifiable.generify(name, bound);
/*     */   }
/*     */   public JTypeVar generify(String name, JClass bound) {
/* 835 */     return this.generifiable.generify(name, bound);
/*     */   }
/*     */   
/*     */   public JTypeVar[] typeParams() {
/* 839 */     return this.generifiable.typeParams();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 845 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 853 */     return annotate(owner().ref(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 861 */     if (this.annotations == null)
/* 862 */       this.annotations = new ArrayList<JAnnotationUse>(); 
/* 863 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 864 */     this.annotations.add(a);
/* 865 */     return a;
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 869 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JDefinedClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
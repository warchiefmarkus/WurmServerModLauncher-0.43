/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JPackage
/*     */   implements JDeclaration, JGenerable, JClassContainer, JAnnotatable, Comparable<JPackage>
/*     */ {
/*     */   private String name;
/*     */   private final JCodeModel owner;
/*  57 */   private final Map<String, JDefinedClass> classes = new TreeMap<String, JDefinedClass>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private final Set<JResourceFile> resources = new HashSet<JResourceFile>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, JDefinedClass> upperCaseClassMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private JDocComment jdoc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JPackage(String name, JCodeModel cw) {
/*  94 */     this.owner = cw;
/*  95 */     if (name.equals(".")) {
/*  96 */       String msg = "Package name . is not allowed";
/*  97 */       throw new IllegalArgumentException(msg);
/*     */     } 
/*     */     
/* 100 */     if (JCodeModel.isCaseSensitiveFileSystem) {
/* 101 */       this.upperCaseClassMap = null;
/*     */     } else {
/* 103 */       this.upperCaseClassMap = new HashMap<String, JDefinedClass>();
/*     */     } 
/* 105 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public JClassContainer parentContainer() {
/* 110 */     return parent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage parent() {
/* 117 */     if (this.name.length() == 0) return null;
/*     */     
/* 119 */     int idx = this.name.lastIndexOf('.');
/* 120 */     return this.owner._package(this.name.substring(0, idx));
/*     */   }
/*     */   
/* 123 */   public boolean isClass() { return false; }
/* 124 */   public boolean isPackage() { return true; } public JPackage getPackage() {
/* 125 */     return this;
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
/*     */   public JDefinedClass _class(int mods, String name) throws JClassAlreadyExistsException {
/* 142 */     return _class(mods, name, ClassType.CLASS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, boolean isInterface) throws JClassAlreadyExistsException {
/* 150 */     return _class(mods, name, isInterface ? ClassType.INTERFACE : ClassType.CLASS);
/*     */   }
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, ClassType classTypeVal) throws JClassAlreadyExistsException {
/* 154 */     if (this.classes.containsKey(name)) {
/* 155 */       throw new JClassAlreadyExistsException((JDefinedClass)this.classes.get(name));
/*     */     }
/*     */     
/* 158 */     JDefinedClass c = new JDefinedClass(this, mods, name, classTypeVal);
/*     */     
/* 160 */     if (this.upperCaseClassMap != null) {
/* 161 */       JDefinedClass dc = this.upperCaseClassMap.get(name.toUpperCase());
/* 162 */       if (dc != null)
/* 163 */         throw new JClassAlreadyExistsException(dc); 
/* 164 */       this.upperCaseClassMap.put(name.toUpperCase(), c);
/*     */     } 
/* 166 */     this.classes.put(name, c);
/* 167 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String name) throws JClassAlreadyExistsException {
/* 175 */     return _class(1, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _getClass(String name) {
/* 185 */     if (this.classes.containsKey(name)) {
/* 186 */       return this.classes.get(name);
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(JPackage that) {
/* 195 */     return this.name.compareTo(that.name);
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
/*     */   public JDefinedClass _interface(int mods, String name) throws JClassAlreadyExistsException {
/* 210 */     return _class(mods, name, ClassType.INTERFACE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _interface(String name) throws JClassAlreadyExistsException {
/* 217 */     return _interface(1, name);
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
/*     */   public JDefinedClass _annotationTypeDeclaration(String name) throws JClassAlreadyExistsException {
/* 231 */     return _class(1, name, ClassType.ANNOTATION_TYPE_DECL);
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
/* 245 */     return _class(1, name, ClassType.ENUM);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JResourceFile addResourceFile(JResourceFile rsrc) {
/* 251 */     this.resources.add(rsrc);
/* 252 */     return rsrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasResourceFile(String name) {
/* 259 */     for (JResourceFile r : this.resources) {
/* 260 */       if (r.name().equals(name))
/* 261 */         return true; 
/* 262 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator propertyFiles() {
/* 269 */     return this.resources.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 279 */     if (this.jdoc == null)
/* 280 */       this.jdoc = new JDocComment(owner()); 
/* 281 */     return this.jdoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(JClass c) {
/* 288 */     if (c._package() != this) {
/* 289 */       throw new IllegalArgumentException("the specified class is not a member of this package, or it is a referenced class");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 294 */     this.classes.remove(c.name());
/* 295 */     if (this.upperCaseClassMap != null) {
/* 296 */       this.upperCaseClassMap.remove(c.name().toUpperCase());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass ref(String name) throws ClassNotFoundException {
/* 303 */     if (name.indexOf('.') >= 0) {
/* 304 */       throw new IllegalArgumentException("JClass name contains '.': " + name);
/*     */     }
/* 306 */     String n = "";
/* 307 */     if (!isUnnamed())
/* 308 */       n = this.name + '.'; 
/* 309 */     n = n + name;
/*     */     
/* 311 */     return this.owner.ref(Class.forName(n));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage subPackage(String pkg) {
/* 318 */     if (isUnnamed()) return owner()._package(pkg); 
/* 319 */     return owner()._package(this.name + '.' + pkg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JDefinedClass> classes() {
/* 327 */     return this.classes.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefined(String classLocalName) {
/* 334 */     Iterator<JDefinedClass> itr = classes();
/* 335 */     while (itr.hasNext()) {
/* 336 */       if (((JClass)itr.next()).name().equals(classLocalName)) {
/* 337 */         return true;
/*     */       }
/*     */     } 
/* 340 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isUnnamed() {
/* 346 */     return (this.name.length() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 357 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final JCodeModel owner() {
/* 363 */     return this.owner;
/*     */   }
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 367 */     if (isUnnamed())
/* 368 */       throw new IllegalArgumentException("the root package cannot be annotated"); 
/* 369 */     if (this.annotations == null)
/* 370 */       this.annotations = new ArrayList<JAnnotationUse>(); 
/* 371 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 372 */     this.annotations.add(a);
/* 373 */     return a;
/*     */   }
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 377 */     return annotate(this.owner.ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 381 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   File toPath(File dir) {
/* 389 */     if (this.name == null) return dir; 
/* 390 */     return new File(dir, this.name.replace('.', File.separatorChar));
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 394 */     if (this.name.length() != 0)
/* 395 */       f.p("package").p(this.name).p(';').nl(); 
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 399 */     f.p(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void build(CodeWriter src, CodeWriter res) throws IOException {
/* 406 */     for (JDefinedClass c : this.classes.values()) {
/* 407 */       if (c.isHidden()) {
/*     */         continue;
/*     */       }
/* 410 */       JFormatter f = createJavaSourceFileWriter(src, c.name());
/* 411 */       f.write(c);
/* 412 */       f.close();
/*     */     } 
/*     */ 
/*     */     
/* 416 */     if (this.annotations != null || this.jdoc != null) {
/* 417 */       JFormatter f = createJavaSourceFileWriter(src, "package-info");
/*     */       
/* 419 */       if (this.jdoc != null) {
/* 420 */         f.g(this.jdoc);
/*     */       }
/*     */       
/* 423 */       if (this.annotations != null)
/* 424 */         for (JAnnotationUse a : this.annotations) {
/* 425 */           f.g(a).nl();
/*     */         } 
/* 427 */       f.d(this);
/*     */       
/* 429 */       f.close();
/*     */     } 
/*     */ 
/*     */     
/* 433 */     for (JResourceFile rsrc : this.resources) {
/* 434 */       CodeWriter cw = rsrc.isResource() ? res : src;
/* 435 */       OutputStream os = new BufferedOutputStream(cw.openBinary(this, rsrc.name()));
/* 436 */       rsrc.build(os);
/* 437 */       os.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   int countArtifacts() {
/* 442 */     int r = 0;
/* 443 */     for (JDefinedClass c : this.classes.values()) {
/* 444 */       if (c.isHidden())
/*     */         continue; 
/* 446 */       r++;
/*     */     } 
/*     */     
/* 449 */     if (this.annotations != null || this.jdoc != null) {
/* 450 */       r++;
/*     */     }
/*     */     
/* 453 */     r += this.resources.size();
/*     */     
/* 455 */     return r;
/*     */   }
/*     */   
/*     */   private JFormatter createJavaSourceFileWriter(CodeWriter src, String className) throws IOException {
/* 459 */     Writer bw = new BufferedWriter(src.openSource(this, className + ".java"));
/* 460 */     return new JFormatter(new PrintWriter(bw));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JPackage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
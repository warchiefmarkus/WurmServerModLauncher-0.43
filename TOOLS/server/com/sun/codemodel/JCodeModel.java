/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.writer.FileCodeWriter;
/*     */ import com.sun.codemodel.writer.ProgressCodeWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JCodeModel
/*     */ {
/*  78 */   private HashMap<String, JPackage> packages = new HashMap<String, JPackage>();
/*     */ 
/*     */   
/*  81 */   private final HashMap<Class, JReferencedClass> refClasses = new HashMap<Class<?>, JReferencedClass>();
/*     */ 
/*     */ 
/*     */   
/*  85 */   public final JNullType NULL = new JNullType(this);
/*     */   
/*  87 */   public final JPrimitiveType VOID = new JPrimitiveType(this, "void", Void.class);
/*  88 */   public final JPrimitiveType BOOLEAN = new JPrimitiveType(this, "boolean", Boolean.class);
/*  89 */   public final JPrimitiveType BYTE = new JPrimitiveType(this, "byte", Byte.class);
/*  90 */   public final JPrimitiveType SHORT = new JPrimitiveType(this, "short", Short.class);
/*  91 */   public final JPrimitiveType CHAR = new JPrimitiveType(this, "char", Character.class);
/*  92 */   public final JPrimitiveType INT = new JPrimitiveType(this, "int", Integer.class);
/*  93 */   public final JPrimitiveType FLOAT = new JPrimitiveType(this, "float", Float.class);
/*  94 */   public final JPrimitiveType LONG = new JPrimitiveType(this, "long", Long.class);
/*  95 */   public final JPrimitiveType DOUBLE = new JPrimitiveType(this, "double", Double.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected static final boolean isCaseSensitiveFileSystem = getFileSystemCaseSensitivity();
/*     */   
/*     */   private JClass wildcard;
/*     */   
/*     */   private static boolean getFileSystemCaseSensitivity() {
/*     */     try {
/* 107 */       if (System.getProperty("com.sun.codemodel.FileSystemCaseSensitive") != null)
/* 108 */         return true; 
/* 109 */     } catch (Exception e) {}
/*     */ 
/*     */     
/* 112 */     return (File.separatorChar == '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Map<Class, Class> primitiveToBox;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Map<Class, Class> boxToPrimitive;
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage _package(String name) {
/* 127 */     JPackage p = this.packages.get(name);
/* 128 */     if (p == null) {
/* 129 */       p = new JPackage(name, this);
/* 130 */       this.packages.put(name, p);
/*     */     } 
/* 132 */     return p;
/*     */   }
/*     */   
/*     */   public final JPackage rootPackage() {
/* 136 */     return _package("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JPackage> packages() {
/* 144 */     return this.packages.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String fullyqualifiedName) throws JClassAlreadyExistsException {
/* 154 */     return _class(fullyqualifiedName, ClassType.CLASS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass directClass(String name) {
/* 165 */     return new JDirectClass(this, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String fullyqualifiedName, ClassType t) throws JClassAlreadyExistsException {
/* 175 */     int idx = fullyqualifiedName.lastIndexOf('.');
/* 176 */     if (idx < 0) return rootPackage()._class(fullyqualifiedName);
/*     */     
/* 178 */     return _package(fullyqualifiedName.substring(0, idx))._class(1, fullyqualifiedName.substring(idx + 1), t);
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
/*     */   public JDefinedClass _getClass(String fullyQualifiedName) {
/* 190 */     int idx = fullyQualifiedName.lastIndexOf('.');
/* 191 */     if (idx < 0) return rootPackage()._getClass(fullyQualifiedName);
/*     */     
/* 193 */     return _package(fullyQualifiedName.substring(0, idx))._getClass(fullyQualifiedName.substring(idx + 1));
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
/*     */   public JDefinedClass newAnonymousClass(JClass baseType) {
/* 205 */     return new JAnonymousClass(baseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass anonymousClass(JClass baseType) {
/* 212 */     return new JAnonymousClass(baseType);
/*     */   }
/*     */   
/*     */   public JDefinedClass anonymousClass(Class baseType) {
/* 216 */     return anonymousClass(ref(baseType));
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
/*     */   public void build(File destDir, PrintStream status) throws IOException {
/* 229 */     build(destDir, destDir, status);
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
/*     */   public void build(File srcDir, File resourceDir, PrintStream status) throws IOException {
/*     */     ProgressCodeWriter progressCodeWriter1, progressCodeWriter2;
/* 244 */     FileCodeWriter fileCodeWriter1 = new FileCodeWriter(srcDir);
/* 245 */     FileCodeWriter fileCodeWriter2 = new FileCodeWriter(resourceDir);
/* 246 */     if (status != null) {
/* 247 */       progressCodeWriter1 = new ProgressCodeWriter((CodeWriter)fileCodeWriter1, status);
/* 248 */       progressCodeWriter2 = new ProgressCodeWriter((CodeWriter)fileCodeWriter2, status);
/*     */     } 
/* 250 */     build((CodeWriter)progressCodeWriter1, (CodeWriter)progressCodeWriter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(File destDir) throws IOException {
/* 257 */     build(destDir, System.out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(File srcDir, File resourceDir) throws IOException {
/* 264 */     build(srcDir, resourceDir, System.out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(CodeWriter out) throws IOException {
/* 271 */     build(out, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(CodeWriter source, CodeWriter resource) throws IOException {
/* 278 */     JPackage[] pkgs = (JPackage[])this.packages.values().toArray((Object[])new JPackage[this.packages.size()]);
/*     */     
/* 280 */     for (JPackage pkg : pkgs)
/* 281 */       pkg.build(source, resource); 
/* 282 */     source.close();
/* 283 */     resource.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int countArtifacts() {
/* 291 */     int r = 0;
/* 292 */     JPackage[] pkgs = (JPackage[])this.packages.values().toArray((Object[])new JPackage[this.packages.size()]);
/*     */     
/* 294 */     for (JPackage pkg : pkgs)
/* 295 */       r += pkg.countArtifacts(); 
/* 296 */     return r;
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
/*     */   public JClass ref(Class clazz) {
/* 309 */     JReferencedClass jrc = this.refClasses.get(clazz);
/* 310 */     if (jrc == null) {
/* 311 */       if (clazz.isPrimitive())
/* 312 */         throw new IllegalArgumentException(clazz + " is a primitive"); 
/* 313 */       if (clazz.isArray()) {
/* 314 */         return new JArrayClass(this, _ref(clazz.getComponentType()));
/*     */       }
/* 316 */       jrc = new JReferencedClass(clazz);
/* 317 */       this.refClasses.put(clazz, jrc);
/*     */     } 
/*     */     
/* 320 */     return jrc;
/*     */   }
/*     */   
/*     */   public JType _ref(Class c) {
/* 324 */     if (c.isPrimitive()) {
/* 325 */       return JType.parse(this, c.getName());
/*     */     }
/* 327 */     return ref(c);
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
/*     */   public JClass ref(String fullyQualifiedClassName) {
/*     */     try {
/* 342 */       return ref(Thread.currentThread().getContextClassLoader().loadClass(fullyQualifiedClassName));
/* 343 */     } catch (ClassNotFoundException e) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 348 */         return ref(Class.forName(fullyQualifiedClassName));
/* 349 */       } catch (ClassNotFoundException e1) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 354 */         return new JDirectClass(this, fullyQualifiedClassName);
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
/*     */   
/*     */   public JClass wildcard() {
/* 367 */     if (this.wildcard == null)
/* 368 */       this.wildcard = ref(Object.class).wildcard(); 
/* 369 */     return this.wildcard;
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
/*     */   public JType parseType(String name) throws ClassNotFoundException {
/* 383 */     if (name.endsWith("[]")) {
/* 384 */       return parseType(name.substring(0, name.length() - 2)).array();
/*     */     }
/*     */     
/*     */     try {
/* 388 */       return JType.parse(this, name);
/* 389 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 394 */       return (new TypeNameParser(name)).parseTypeName();
/*     */     } 
/*     */   }
/*     */   
/*     */   private final class TypeNameParser {
/*     */     private final String s;
/*     */     
/*     */     public TypeNameParser(String s) {
/* 402 */       this.s = s;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int idx;
/*     */ 
/*     */ 
/*     */     
/*     */     JClass parseTypeName() throws ClassNotFoundException {
/* 412 */       int start = this.idx;
/*     */       
/* 414 */       if (this.s.charAt(this.idx) == '?') {
/*     */         
/* 416 */         this.idx++;
/* 417 */         ws();
/* 418 */         String head = this.s.substring(this.idx);
/* 419 */         if (head.startsWith("extends")) {
/* 420 */           this.idx += 7;
/* 421 */           ws();
/* 422 */           return parseTypeName().wildcard();
/*     */         } 
/* 424 */         if (head.startsWith("super")) {
/* 425 */           throw new UnsupportedOperationException("? super T not implemented");
/*     */         }
/*     */         
/* 428 */         throw new IllegalArgumentException("only extends/super can follow ?, but found " + this.s.substring(this.idx));
/*     */       } 
/*     */ 
/*     */       
/* 432 */       while (this.idx < this.s.length()) {
/* 433 */         char ch = this.s.charAt(this.idx);
/* 434 */         if (Character.isJavaIdentifierStart(ch) || Character.isJavaIdentifierPart(ch) || ch == '.')
/*     */         {
/*     */           
/* 437 */           this.idx++;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 442 */       JClass clazz = JCodeModel.this.ref(this.s.substring(start, this.idx));
/*     */       
/* 444 */       return parseSuffix(clazz);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private JClass parseSuffix(JClass clazz) throws ClassNotFoundException {
/* 452 */       if (this.idx == this.s.length()) {
/* 453 */         return clazz;
/*     */       }
/* 455 */       char ch = this.s.charAt(this.idx);
/*     */       
/* 457 */       if (ch == '<') {
/* 458 */         return parseSuffix(parseArguments(clazz));
/*     */       }
/* 460 */       if (ch == '[') {
/* 461 */         if (this.s.charAt(this.idx + 1) == ']') {
/* 462 */           this.idx += 2;
/* 463 */           return parseSuffix(clazz.array());
/*     */         } 
/* 465 */         throw new IllegalArgumentException("Expected ']' but found " + this.s.substring(this.idx + 1));
/*     */       } 
/*     */       
/* 468 */       return clazz;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void ws() {
/* 475 */       while (Character.isWhitespace(this.s.charAt(this.idx)) && this.idx < this.s.length()) {
/* 476 */         this.idx++;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private JClass parseArguments(JClass rawType) throws ClassNotFoundException {
/* 485 */       if (this.s.charAt(this.idx) != '<')
/* 486 */         throw new IllegalArgumentException(); 
/* 487 */       this.idx++;
/*     */       
/* 489 */       List<JClass> args = new ArrayList<JClass>();
/*     */       
/*     */       while (true) {
/* 492 */         args.add(parseTypeName());
/* 493 */         if (this.idx == this.s.length())
/* 494 */           throw new IllegalArgumentException("Missing '>' in " + this.s); 
/* 495 */         char ch = this.s.charAt(this.idx);
/* 496 */         if (ch == '>') {
/* 497 */           return rawType.narrow(args.<JClass>toArray(new JClass[args.size()]));
/*     */         }
/* 499 */         if (ch != ',')
/* 500 */           throw new IllegalArgumentException(this.s); 
/* 501 */         this.idx++;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class JReferencedClass
/*     */     extends JClass
/*     */     implements JDeclaration
/*     */   {
/*     */     private final Class _class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     JReferencedClass(Class _clazz) {
/* 523 */       super(JCodeModel.this);
/* 524 */       this._class = _clazz;
/* 525 */       assert !this._class.isArray();
/*     */     }
/*     */     
/*     */     public String name() {
/* 529 */       return this._class.getSimpleName().replace('$', '.');
/*     */     }
/*     */     
/*     */     public String fullName() {
/* 533 */       return this._class.getName().replace('$', '.');
/*     */     }
/*     */     
/*     */     public String binaryName() {
/* 537 */       return this._class.getName();
/*     */     }
/*     */     
/*     */     public JClass outer() {
/* 541 */       Class<?> p = this._class.getDeclaringClass();
/* 542 */       if (p == null) return null; 
/* 543 */       return JCodeModel.this.ref(p);
/*     */     }
/*     */     
/*     */     public JPackage _package() {
/* 547 */       String name = fullName();
/*     */ 
/*     */       
/* 550 */       if (name.indexOf('[') != -1) {
/* 551 */         return JCodeModel.this._package("");
/*     */       }
/*     */       
/* 554 */       int idx = name.lastIndexOf('.');
/* 555 */       if (idx < 0) {
/* 556 */         return JCodeModel.this._package("");
/*     */       }
/* 558 */       return JCodeModel.this._package(name.substring(0, idx));
/*     */     }
/*     */     
/*     */     public JClass _extends() {
/* 562 */       Class sp = this._class.getSuperclass();
/* 563 */       if (sp == null) {
/* 564 */         if (isInterface())
/* 565 */           return owner().ref(Object.class); 
/* 566 */         return null;
/*     */       } 
/* 568 */       return JCodeModel.this.ref(sp);
/*     */     }
/*     */     
/*     */     public Iterator<JClass> _implements() {
/* 572 */       final Class[] interfaces = this._class.getInterfaces();
/* 573 */       return new Iterator<JClass>() {
/* 574 */           private int idx = 0;
/*     */           public boolean hasNext() {
/* 576 */             return (this.idx < interfaces.length);
/*     */           }
/*     */           public JClass next() {
/* 579 */             return JCodeModel.this.ref(interfaces[this.idx++]);
/*     */           }
/*     */           public void remove() {
/* 582 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean isInterface() {
/* 588 */       return this._class.isInterface();
/*     */     }
/*     */     
/*     */     public boolean isAbstract() {
/* 592 */       return Modifier.isAbstract(this._class.getModifiers());
/*     */     }
/*     */     
/*     */     public JPrimitiveType getPrimitiveType() {
/* 596 */       Class v = JCodeModel.boxToPrimitive.get(this._class);
/* 597 */       if (v != null) {
/* 598 */         return JType.parse(JCodeModel.this, v.getName());
/*     */       }
/* 600 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isArray() {
/* 604 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void declare(JFormatter f) {}
/*     */ 
/*     */     
/*     */     public JTypeVar[] typeParams() {
/* 612 */       return super.typeParams();
/*     */     }
/*     */ 
/*     */     
/*     */     protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 617 */       return this;
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
/*     */   static {
/* 632 */     Map<Class<?>, Class<?>> m1 = new HashMap<Class<?>, Class<?>>();
/* 633 */     Map<Class<?>, Class<?>> m2 = new HashMap<Class<?>, Class<?>>();
/*     */     
/* 635 */     m1.put(Boolean.class, boolean.class);
/* 636 */     m1.put(Byte.class, byte.class);
/* 637 */     m1.put(Character.class, char.class);
/* 638 */     m1.put(Double.class, double.class);
/* 639 */     m1.put(Float.class, float.class);
/* 640 */     m1.put(Integer.class, int.class);
/* 641 */     m1.put(Long.class, long.class);
/* 642 */     m1.put(Short.class, short.class);
/* 643 */     m1.put(Void.class, void.class);
/*     */     
/* 645 */     for (Map.Entry<Class<?>, Class<?>> e : m1.entrySet()) {
/* 646 */       m2.put(e.getValue(), e.getKey());
/*     */     }
/* 648 */     boxToPrimitive = Collections.unmodifiableMap(m1);
/* 649 */     primitiveToBox = Collections.unmodifiableMap(m2);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JCodeModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
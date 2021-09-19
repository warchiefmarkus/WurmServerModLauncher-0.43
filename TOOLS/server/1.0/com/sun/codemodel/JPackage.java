/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.CodeWriter;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDeclaration;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JPackageMemberClass;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import com.sun.codemodel.util.UnicodeEscapeWriter;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ public final class JPackage
/*     */   implements JDeclaration, JGenerable, JClassContainer
/*     */ {
/*     */   private String name;
/*     */   private final JCodeModel owner;
/*  45 */   private final Map classes = new TreeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final Set resources = new HashSet();
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JPackage(String name, JCodeModel cw) {
/*  72 */     this.owner = cw;
/*  73 */     if (name.equals(".")) {
/*  74 */       String msg = "JPackage name . is not allowed";
/*  75 */       throw new IllegalArgumentException(msg);
/*     */     } 
/*     */     
/*  78 */     int dots = 1;
/*  79 */     for (int i = 0; i < name.length(); i++) {
/*  80 */       char c = name.charAt(i);
/*  81 */       if (c == '.') {
/*  82 */         dots++;
/*     */       } else {
/*     */         
/*  85 */         if (dots > 1) {
/*  86 */           String msg = "JPackage name " + name + " missing identifier";
/*  87 */           throw new IllegalArgumentException(msg);
/*  88 */         }  if (dots == 1 && !Character.isJavaIdentifierStart(c)) {
/*  89 */           String msg = "JPackage name " + name + " contains illegal " + "character for beginning of identifier: " + c;
/*     */           
/*  91 */           throw new IllegalArgumentException(msg);
/*  92 */         }  if (!Character.isJavaIdentifierPart(c)) {
/*  93 */           String msg = "JPackage name " + name + "contains illegal " + "character: " + c;
/*  94 */           throw new IllegalArgumentException(msg);
/*     */         } 
/*  96 */         dots = 0;
/*     */       } 
/*  98 */     }  if (!name.trim().equals("") && dots != 0) {
/*  99 */       String msg = "JPackage name not allowed to end with .";
/* 100 */       throw new IllegalArgumentException(msg);
/*     */     } 
/*     */     
/* 103 */     if (JCodeModel.isCaseSensitiveFileSystem) {
/* 104 */       this.upperCaseClassMap = null;
/*     */     } else {
/* 106 */       this.upperCaseClassMap = new HashMap();
/*     */     } 
/* 108 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public JClassContainer parentContainer() {
/* 113 */     return parent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JPackage parent() {
/* 120 */     if (this.name.length() == 0) return null;
/*     */     
/* 122 */     int idx = this.name.lastIndexOf('.');
/* 123 */     return this.owner._package(this.name.substring(0, idx));
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
/*     */   public JDefinedClass _class(int mods, String name) throws JClassAlreadyExistsException {
/* 141 */     return _class(mods, name, false);
/*     */   }
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, boolean isInterface) throws JClassAlreadyExistsException {
/* 145 */     if (this.classes.containsKey(name)) {
/* 146 */       throw new JClassAlreadyExistsException((JDefinedClass)this.classes.get(name));
/*     */     }
/*     */     
/* 149 */     JPackageMemberClass jPackageMemberClass = new JPackageMemberClass(this, mods, name, isInterface);
/*     */     
/* 151 */     if (this.upperCaseClassMap != null) {
/* 152 */       JDefinedClass dc = (JDefinedClass)this.upperCaseClassMap.get(name.toUpperCase());
/* 153 */       if (dc != null)
/* 154 */         throw new JClassAlreadyExistsException(dc); 
/* 155 */       this.upperCaseClassMap.put(name.toUpperCase(), jPackageMemberClass);
/*     */     } 
/* 157 */     this.classes.put(name, jPackageMemberClass);
/* 158 */     return (JDefinedClass)jPackageMemberClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String name) throws JClassAlreadyExistsException {
/* 166 */     return _class(1, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _getClass(String name) {
/* 176 */     if (this.classes.containsKey(name)) {
/* 177 */       return (JDefinedClass)this.classes.get(name);
/*     */     }
/* 179 */     return null;
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
/* 195 */     return _class(mods, name, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _interface(String name) throws JClassAlreadyExistsException {
/* 202 */     return _interface(1, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JResourceFile addResourceFile(JResourceFile rsrc) {
/* 209 */     this.resources.add(rsrc);
/* 210 */     return rsrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasResourceFile(String name) {
/* 217 */     for (Iterator itr = this.resources.iterator(); itr.hasNext(); ) {
/* 218 */       JResourceFile r = itr.next();
/* 219 */       if (r.name().equals(name))
/* 220 */         return true; 
/*     */     } 
/* 222 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator propertyFiles() {
/* 229 */     return this.resources.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(JClass c) {
/* 236 */     if (c._package() != this) {
/* 237 */       throw new IllegalArgumentException("the specified class is not a member of this package, or it is a referenced class");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 242 */     this.classes.remove(c.name());
/* 243 */     if (this.upperCaseClassMap != null) {
/* 244 */       this.upperCaseClassMap.remove(c.name().toUpperCase());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass ref(String name) {
/* 251 */     if (name.indexOf('.') >= 0) {
/* 252 */       throw new IllegalArgumentException("JClass name contains '.': " + name);
/*     */     }
/* 254 */     String n = "";
/* 255 */     if (!isUnnamed())
/* 256 */       n = this.name + "."; 
/* 257 */     n = n + name;
/*     */     
/*     */     try {
/* 260 */       return this.owner.ref(Class.forName(n));
/* 261 */     } catch (ClassNotFoundException e) {
/* 262 */       throw new NoClassDefFoundError(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JPackage subPackage(String pkg) {
/* 270 */     if (isUnnamed()) return owner()._package(pkg); 
/* 271 */     return owner()._package(this.name + "." + pkg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator classes() {
/* 279 */     return this.classes.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefined(String classLocalName) {
/* 286 */     Iterator itr = classes();
/* 287 */     while (itr.hasNext()) {
/* 288 */       if (((JClass)itr.next()).name().equals(classLocalName)) {
/* 289 */         return true;
/*     */       }
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isUnnamed() {
/* 298 */     return (this.name.length() == 0);
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
/* 309 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final JCodeModel owner() {
/* 315 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   File toPath(File dir) {
/* 321 */     if (this.name == null) return dir; 
/* 322 */     return new File(dir, this.name.replace('.', File.separatorChar));
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 326 */     if (this.name.length() != 0)
/* 327 */       f.p("package").p(this.name).p(';').nl(); 
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 331 */     f.p(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void build(CodeWriter out) throws IOException {
/* 337 */     for (Iterator iterator = this.classes.values().iterator(); iterator.hasNext(); ) {
/* 338 */       JPackageMemberClass c = iterator.next();
/*     */       
/* 340 */       if (c.isHidden()) {
/*     */         continue;
/*     */       }
/* 343 */       Object object = new BufferedWriter(new OutputStreamWriter(out.open(this, c.name() + ".java")));
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 348 */         object = new Object(this, (Writer)object);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 359 */       catch (Throwable t) {
/* 360 */         object = new UnicodeEscapeWriter((Writer)object);
/*     */       } 
/*     */       
/* 363 */       JFormatter f = new JFormatter(new PrintWriter((Writer)object));
/* 364 */       c.declare(f);
/* 365 */       f.close();
/*     */     } 
/*     */     
/* 368 */     for (Iterator i = this.resources.iterator(); i.hasNext(); ) {
/* 369 */       JResourceFile rsrc = i.next();
/*     */       
/* 371 */       OutputStream os = new BufferedOutputStream(out.open(this, rsrc.name()));
/* 372 */       rsrc.build(os);
/* 373 */       os.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JPackage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
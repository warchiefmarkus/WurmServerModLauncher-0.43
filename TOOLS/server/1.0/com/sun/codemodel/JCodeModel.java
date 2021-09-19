/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.CodeWriter;
/*     */ import com.sun.codemodel.JAnonymousClass;
/*     */ import com.sun.codemodel.JArrayClass;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JNullType;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.writer.FileCodeWriter;
/*     */ import com.sun.codemodel.writer.ProgressCodeWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public final class JCodeModel
/*     */ {
/*  23 */   private HashMap packages = new HashMap();
/*     */ 
/*     */   
/*  26 */   private final HashMap refClasses = new HashMap();
/*     */ 
/*     */ 
/*     */   
/*  30 */   public final JNullType NULL = new JNullType(this);
/*     */   
/*  32 */   public final JPrimitiveType VOID = new JPrimitiveType(this, "void", Void.class);
/*  33 */   public final JPrimitiveType BOOLEAN = new JPrimitiveType(this, "boolean", Boolean.class);
/*  34 */   public final JPrimitiveType BYTE = new JPrimitiveType(this, "byte", Byte.class);
/*  35 */   public final JPrimitiveType SHORT = new JPrimitiveType(this, "short", Short.class);
/*  36 */   public final JPrimitiveType CHAR = new JPrimitiveType(this, "char", Character.class);
/*  37 */   public final JPrimitiveType INT = new JPrimitiveType(this, "int", Integer.class);
/*  38 */   public final JPrimitiveType FLOAT = new JPrimitiveType(this, "float", Float.class);
/*  39 */   public final JPrimitiveType LONG = new JPrimitiveType(this, "long", Long.class);
/*  40 */   public final JPrimitiveType DOUBLE = new JPrimitiveType(this, "double", Double.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   protected static final boolean isCaseSensitiveFileSystem = getFileSystemCaseSensitivity();
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean getFileSystemCaseSensitivity() {
/*     */     try {
/*  52 */       if (System.getProperty("com.sun.codemodel.FileSystemCaseSensitive") != null)
/*  53 */         return true; 
/*  54 */     } catch (Exception e) {}
/*     */ 
/*     */     
/*  57 */     return (File.separatorChar == '/');
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
/*     */   public JPackage _package(String name) {
/*  72 */     JPackage p = (JPackage)this.packages.get(name);
/*  73 */     if (p == null) {
/*  74 */       p = new JPackage(name, this);
/*  75 */       this.packages.put(name, p);
/*     */     } 
/*  77 */     return p;
/*     */   }
/*     */   
/*     */   public final JPackage rootPackage() {
/*  81 */     return _package("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator packages() {
/*  89 */     return this.packages.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String fullyqualifiedName) throws JClassAlreadyExistsException {
/*  99 */     int idx = fullyqualifiedName.lastIndexOf('.');
/* 100 */     if (idx < 0) return rootPackage()._class(fullyqualifiedName);
/*     */     
/* 102 */     return _package(fullyqualifiedName.substring(0, idx))._class(fullyqualifiedName.substring(idx + 1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _getClass(String fullyQualifiedName) {
/* 113 */     int idx = fullyQualifiedName.lastIndexOf('.');
/* 114 */     if (idx < 0) return rootPackage()._getClass(fullyQualifiedName);
/*     */     
/* 116 */     return _package(fullyQualifiedName.substring(0, idx))._getClass(fullyQualifiedName.substring(idx + 1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass newAnonymousClass(JClass baseType) {
/* 124 */     return (JDefinedClass)new JAnonymousClass(baseType, this);
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
/*     */     ProgressCodeWriter progressCodeWriter;
/* 138 */     FileCodeWriter fileCodeWriter = new FileCodeWriter(destDir);
/* 139 */     if (status != null) {
/* 140 */       progressCodeWriter = new ProgressCodeWriter((CodeWriter)fileCodeWriter, status);
/*     */     }
/* 142 */     build((CodeWriter)progressCodeWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(File destDir) throws IOException {
/* 149 */     build(destDir, System.out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(CodeWriter out) throws IOException {
/* 156 */     for (Iterator i = this.packages.values().iterator(); i.hasNext();)
/* 157 */       ((JPackage)i.next()).build(out); 
/* 158 */     out.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass ref(Class clazz) {
/* 167 */     JReferencedClass jrc = (JReferencedClass)this.refClasses.get(clazz);
/* 168 */     if (jrc == null) {
/* 169 */       if (clazz.isArray()) {
/* 170 */         return (JClass)new JArrayClass(this, (JType)ref(clazz.getComponentType()));
/*     */       }
/* 172 */       jrc = new JReferencedClass(this, clazz);
/* 173 */       this.refClasses.put(clazz, jrc);
/*     */     } 
/*     */     
/* 176 */     return (JClass)jrc;
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
/*     */   public JClass ref(String fullyQualifiedClassName) throws ClassNotFoundException {
/*     */     try {
/* 189 */       return ref(Thread.currentThread().getContextClassLoader().loadClass(fullyQualifiedClassName));
/* 190 */     } catch (ClassNotFoundException e) {
/*     */       
/* 192 */       return ref(Class.forName(fullyQualifiedClassName));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JCodeModel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.codemodel.fmt;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JResourceFile;
/*     */ import com.sun.codemodel.JTypeVar;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.text.ParseException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JStaticJavaFile
/*     */   extends JResourceFile
/*     */ {
/*     */   private final JPackage pkg;
/*     */   private final String className;
/*     */   private final URL source;
/*     */   private final JStaticClass clazz;
/*     */   private final LineFilter filter;
/*     */   
/*     */   public JStaticJavaFile(JPackage _pkg, String className, String _resourceName) {
/*  71 */     this(_pkg, className, JStaticJavaFile.class.getClassLoader().getResource(_resourceName), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JStaticJavaFile(JPackage _pkg, String _className, URL _source, LineFilter _filter) {
/*  76 */     super(_className + ".java");
/*  77 */     if (_source == null) throw new NullPointerException(); 
/*  78 */     this.pkg = _pkg;
/*  79 */     this.clazz = new JStaticClass();
/*  80 */     this.className = _className;
/*  81 */     this.source = _source;
/*  82 */     this.filter = _filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass getJClass() {
/*  89 */     return this.clazz;
/*     */   }
/*     */   
/*     */   protected boolean isResource() {
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   protected void build(OutputStream os) throws IOException {
/*  97 */     InputStream is = this.source.openStream();
/*     */     
/*  99 */     BufferedReader r = new BufferedReader(new InputStreamReader(is));
/* 100 */     PrintWriter w = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
/* 101 */     LineFilter filter = createLineFilter();
/* 102 */     int lineNumber = 1;
/*     */     
/*     */     try {
/*     */       String line;
/* 106 */       while ((line = r.readLine()) != null) {
/* 107 */         line = filter.process(line);
/* 108 */         if (line != null)
/* 109 */           w.println(line); 
/* 110 */         lineNumber++;
/*     */       } 
/* 112 */     } catch (ParseException e) {
/* 113 */       throw new IOException("unable to process " + this.source + " line:" + lineNumber + "\n" + e.getMessage());
/*     */     } 
/*     */     
/* 116 */     w.close();
/* 117 */     r.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LineFilter createLineFilter() {
/* 128 */     LineFilter f = new LineFilter() {
/*     */         public String process(String line) {
/* 130 */           if (!line.startsWith("package ")) return line;
/*     */ 
/*     */           
/* 133 */           if (JStaticJavaFile.this.pkg.isUnnamed()) {
/* 134 */             return null;
/*     */           }
/* 136 */           return "package " + JStaticJavaFile.this.pkg.name() + ";";
/*     */         }
/*     */       };
/* 139 */     if (this.filter != null) {
/* 140 */       return new ChainFilter(this.filter, f);
/*     */     }
/* 142 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface LineFilter
/*     */   {
/*     */     String process(String param1String) throws ParseException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ChainFilter
/*     */     implements LineFilter
/*     */   {
/*     */     private final JStaticJavaFile.LineFilter first;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final JStaticJavaFile.LineFilter second;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ChainFilter(JStaticJavaFile.LineFilter first, JStaticJavaFile.LineFilter second) {
/* 173 */       this.first = first;
/* 174 */       this.second = second;
/*     */     }
/*     */     public String process(String line) throws ParseException {
/* 177 */       line = this.first.process(line);
/* 178 */       if (line == null) return null; 
/* 179 */       return this.second.process(line);
/*     */     }
/*     */   }
/*     */   
/*     */   private class JStaticClass
/*     */     extends JClass
/*     */   {
/*     */     private final JTypeVar[] typeParams;
/*     */     
/*     */     JStaticClass() {
/* 189 */       super(JStaticJavaFile.this.pkg.owner());
/*     */       
/* 191 */       this.typeParams = new JTypeVar[0];
/*     */     }
/*     */     
/*     */     public String name() {
/* 195 */       return JStaticJavaFile.this.className;
/*     */     }
/*     */     
/*     */     public String fullName() {
/* 199 */       if (JStaticJavaFile.this.pkg.isUnnamed()) {
/* 200 */         return JStaticJavaFile.this.className;
/*     */       }
/* 202 */       return JStaticJavaFile.this.pkg.name() + '.' + JStaticJavaFile.this.className;
/*     */     }
/*     */     
/*     */     public JPackage _package() {
/* 206 */       return JStaticJavaFile.this.pkg;
/*     */     }
/*     */     
/*     */     public JClass _extends() {
/* 210 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Iterator _implements() {
/* 214 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean isInterface() {
/* 218 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean isAbstract() {
/* 222 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public JTypeVar[] typeParams() {
/* 226 */       return this.typeParams;
/*     */     }
/*     */     
/*     */     protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 230 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\fmt\JStaticJavaFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
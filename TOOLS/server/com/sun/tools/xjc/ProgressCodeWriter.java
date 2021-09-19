/*    */ package com.sun.tools.xjc;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.codemodel.writer.FilterCodeWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ProgressCodeWriter
/*    */   extends FilterCodeWriter
/*    */ {
/*    */   private int current;
/*    */   private final int totalFileCount;
/*    */   private final XJCListener progress;
/*    */   
/*    */   public ProgressCodeWriter(CodeWriter output, XJCListener progress, int totalFileCount) {
/* 57 */     super(output);
/* 58 */     this.progress = progress;
/* 59 */     this.totalFileCount = totalFileCount;
/* 60 */     if (progress == null) {
/* 61 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 67 */     report(pkg, fileName);
/* 68 */     return super.openSource(pkg, fileName);
/*    */   }
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 72 */     report(pkg, fileName);
/* 73 */     return super.openBinary(pkg, fileName);
/*    */   }
/*    */   
/*    */   private void report(JPackage pkg, String fileName) {
/* 77 */     String name = pkg.name().replace('.', File.separatorChar);
/* 78 */     if (name.length() != 0) name = name + File.separatorChar; 
/* 79 */     name = name + fileName;
/*    */     
/* 81 */     if (this.progress.isCanceled())
/* 82 */       throw new AbortException(); 
/* 83 */     this.progress.generatedFile(name, this.current++, this.totalFileCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ProgressCodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
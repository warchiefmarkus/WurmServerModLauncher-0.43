/*    */ package com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
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
/*    */ public class ProgressCodeWriter
/*    */   extends FilterCodeWriter
/*    */ {
/*    */   private final PrintStream progress;
/*    */   
/*    */   public ProgressCodeWriter(CodeWriter output, PrintStream progress) {
/* 40 */     super(output);
/* 41 */     this.progress = progress;
/* 42 */     if (progress == null) {
/* 43 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 49 */     report(pkg, fileName);
/* 50 */     return super.openBinary(pkg, fileName);
/*    */   }
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 54 */     report(pkg, fileName);
/* 55 */     return super.openSource(pkg, fileName);
/*    */   }
/*    */   
/*    */   private void report(JPackage pkg, String fileName) {
/* 59 */     if (pkg.isUnnamed()) { this.progress.println(fileName); }
/*    */     else
/* 61 */     { this.progress.println(pkg.name().replace('.', File.separatorChar) + File.separatorChar + fileName); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\writer\ProgressCodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
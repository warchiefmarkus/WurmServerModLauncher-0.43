/*    */ package com.sun.tools.xjc.api.util;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.mirror.apt.Filer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FilerCodeWriter
/*    */   extends CodeWriter
/*    */ {
/*    */   private final Filer filer;
/*    */   
/*    */   public FilerCodeWriter(Filer filer) {
/* 61 */     this.filer = filer;
/*    */   }
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/*    */     Filer.Location loc;
/* 66 */     if (fileName.endsWith(".java")) {
/*    */ 
/*    */       
/* 69 */       loc = Filer.Location.SOURCE_TREE;
/*    */     } else {
/*    */       
/* 72 */       loc = Filer.Location.CLASS_TREE;
/*    */     } 
/* 74 */     return this.filer.createBinaryFile(loc, pkg.name(), new File(fileName));
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 79 */     if (pkg.isUnnamed()) {
/* 80 */       name = fileName;
/*    */     } else {
/* 82 */       name = pkg.name() + '.' + fileName;
/*    */     } 
/* 84 */     String name = name.substring(0, name.length() - 5);
/*    */     
/* 86 */     return this.filer.createSourceFile(name);
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ap\\util\FilerCodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class FileCodeWriter
/*    */   extends CodeWriter
/*    */ {
/*    */   private final File target;
/*    */   private final boolean readOnly;
/* 47 */   private final Set<File> readonlyFiles = new HashSet<File>();
/*    */   
/*    */   public FileCodeWriter(File target) throws IOException {
/* 50 */     this(target, false);
/*    */   }
/*    */   
/*    */   public FileCodeWriter(File target, boolean readOnly) throws IOException {
/* 54 */     this.target = target;
/* 55 */     this.readOnly = readOnly;
/* 56 */     if (!target.exists() || !target.isDirectory()) {
/* 57 */       throw new IOException(target + ": non-existent directory");
/*    */     }
/*    */   }
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 62 */     return new FileOutputStream(getFile(pkg, fileName));
/*    */   }
/*    */   
/*    */   protected File getFile(JPackage pkg, String fileName) throws IOException {
/*    */     File dir;
/* 67 */     if (pkg.isUnnamed()) {
/* 68 */       dir = this.target;
/*    */     } else {
/* 70 */       dir = new File(this.target, toDirName(pkg));
/*    */     } 
/* 72 */     if (!dir.exists()) dir.mkdirs();
/*    */     
/* 74 */     File fn = new File(dir, fileName);
/*    */     
/* 76 */     if (fn.exists() && 
/* 77 */       !fn.delete()) {
/* 78 */       throw new IOException(fn + ": Can't delete previous version");
/*    */     }
/*    */ 
/*    */     
/* 82 */     if (this.readOnly) this.readonlyFiles.add(fn); 
/* 83 */     return fn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 88 */     for (File f : this.readonlyFiles) {
/* 89 */       f.setReadOnly();
/*    */     }
/*    */   }
/*    */   
/*    */   private static String toDirName(JPackage pkg) {
/* 94 */     return pkg.name().replace('.', File.separatorChar);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\writer\FileCodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipOutputStream;
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
/*    */ public class ZipCodeWriter
/*    */   extends CodeWriter
/*    */ {
/*    */   private final ZipOutputStream zip;
/*    */   private final OutputStream filter;
/*    */   
/*    */   public ZipCodeWriter(OutputStream target) {
/* 43 */     this.zip = new ZipOutputStream(target);
/*    */     
/* 45 */     this.filter = new FilterOutputStream(this.zip)
/*    */       {
/*    */         public void close() {}
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 55 */     String name = fileName;
/* 56 */     if (!pkg.isUnnamed()) name = toDirName(pkg) + name;
/*    */     
/* 58 */     this.zip.putNextEntry(new ZipEntry(name));
/* 59 */     return this.filter;
/*    */   }
/*    */ 
/*    */   
/*    */   private static String toDirName(JPackage pkg) {
/* 64 */     return pkg.name().replace('.', '/') + '/';
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 68 */     this.zip.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\writer\ZipCodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
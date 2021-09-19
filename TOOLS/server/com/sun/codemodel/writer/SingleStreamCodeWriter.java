/*    */ package com.sun.codemodel.writer;
/*    */ 
/*    */ import com.sun.codemodel.CodeWriter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
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
/*    */ public class SingleStreamCodeWriter
/*    */   extends CodeWriter
/*    */ {
/*    */   private final PrintStream out;
/*    */   
/*    */   public SingleStreamCodeWriter(OutputStream os) {
/* 49 */     this.out = new PrintStream(os);
/*    */   }
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 53 */     String pkgName = pkg.name();
/* 54 */     if (pkgName.length() != 0) pkgName = pkgName + '.';
/*    */     
/* 56 */     this.out.println("-----------------------------------" + pkgName + fileName + "-----------------------------------");
/*    */ 
/*    */ 
/*    */     
/* 60 */     return new FilterOutputStream(this.out)
/*    */       {
/*    */         public void close() {}
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 68 */     this.out.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\writer\SingleStreamCodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
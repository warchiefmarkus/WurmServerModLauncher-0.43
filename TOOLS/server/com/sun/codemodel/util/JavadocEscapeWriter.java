/*    */ package com.sun.codemodel.util;
/*    */ 
/*    */ import java.io.FilterWriter;
/*    */ import java.io.IOException;
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
/*    */ public class JavadocEscapeWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   public JavadocEscapeWriter(Writer next) {
/* 49 */     super(next);
/*    */   }
/*    */   
/*    */   public void write(int ch) throws IOException {
/* 53 */     if (ch == 60) {
/* 54 */       this.out.write("&lt;");
/*    */     }
/* 56 */     else if (ch == 38) {
/* 57 */       this.out.write("&amp;");
/*    */     } else {
/* 59 */       this.out.write(ch);
/*    */     } 
/*    */   }
/*    */   public void write(char[] buf, int off, int len) throws IOException {
/* 63 */     for (int i = 0; i < len; i++)
/* 64 */       write(buf[off + i]); 
/*    */   }
/*    */   
/*    */   public void write(char[] buf) throws IOException {
/* 68 */     write(buf, 0, buf.length);
/*    */   }
/*    */   
/*    */   public void write(String buf, int off, int len) throws IOException {
/* 72 */     write(buf.toCharArray(), off, len);
/*    */   }
/*    */   
/*    */   public void write(String buf) throws IOException {
/* 76 */     write(buf.toCharArray(), 0, buf.length());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemode\\util\JavadocEscapeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
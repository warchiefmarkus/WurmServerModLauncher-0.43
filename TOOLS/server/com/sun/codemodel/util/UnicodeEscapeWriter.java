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
/*    */ public class UnicodeEscapeWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   public UnicodeEscapeWriter(Writer next) {
/* 39 */     super(next);
/*    */   }
/*    */   
/*    */   public final void write(int ch) throws IOException {
/* 43 */     if (!requireEscaping(ch)) { this.out.write(ch); }
/*    */     else
/*    */     
/* 46 */     { this.out.write("\\u");
/* 47 */       String s = Integer.toHexString(ch);
/* 48 */       for (int i = s.length(); i < 4; i++)
/* 49 */         this.out.write(48); 
/* 50 */       this.out.write(s); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean requireEscaping(int ch) {
/* 59 */     if (ch >= 128) return true;
/*    */ 
/*    */     
/* 62 */     if (ch < 32 && " \t\r\n".indexOf(ch) == -1) return true;
/*    */     
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   public final void write(char[] buf, int off, int len) throws IOException {
/* 68 */     for (int i = 0; i < len; i++)
/* 69 */       write(buf[off + i]); 
/*    */   }
/*    */   
/*    */   public final void write(char[] buf) throws IOException {
/* 73 */     write(buf, 0, buf.length);
/*    */   }
/*    */   
/*    */   public final void write(String buf, int off, int len) throws IOException {
/* 77 */     write(buf.toCharArray(), off, len);
/*    */   }
/*    */   
/*    */   public final void write(String buf) throws IOException {
/* 81 */     write(buf.toCharArray(), 0, buf.length());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemode\\util\UnicodeEscapeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
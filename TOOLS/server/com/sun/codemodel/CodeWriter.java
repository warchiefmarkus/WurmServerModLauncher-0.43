/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.util.EncoderFactory;
/*    */ import com.sun.codemodel.util.UnicodeEscapeWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.Writer;
/*    */ import java.nio.charset.CharsetEncoder;
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
/*    */ public abstract class CodeWriter
/*    */ {
/*    */   public abstract OutputStream openBinary(JPackage paramJPackage, String paramString) throws IOException;
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 72 */     final OutputStreamWriter bw = new OutputStreamWriter(openBinary(pkg, fileName));
/*    */ 
/*    */     
/*    */     try {
/* 76 */       return (Writer)new UnicodeEscapeWriter(bw)
/*    */         {
/*    */           
/* 79 */           private final CharsetEncoder encoder = EncoderFactory.createEncoder(bw.getEncoding());
/*    */           
/*    */           protected boolean requireEscaping(int ch) {
/* 82 */             if (ch < 32 && " \t\r\n".indexOf(ch) == -1) return true;
/*    */             
/* 84 */             if (ch < 128) return false;
/*    */             
/* 86 */             return !this.encoder.canEncode((char)ch);
/*    */           }
/*    */         };
/* 89 */     } catch (Throwable t) {
/* 90 */       return (Writer)new UnicodeEscapeWriter(bw);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void close() throws IOException;
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\CodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
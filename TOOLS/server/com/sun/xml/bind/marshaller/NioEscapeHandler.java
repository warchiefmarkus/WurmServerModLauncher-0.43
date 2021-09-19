/*    */ package com.sun.xml.bind.marshaller;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.nio.charset.Charset;
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
/*    */ public class NioEscapeHandler
/*    */   implements CharacterEscapeHandler
/*    */ {
/*    */   private final CharsetEncoder encoder;
/*    */   
/*    */   public NioEscapeHandler(String charsetName) {
/* 70 */     this.encoder = Charset.forName(charsetName).newEncoder();
/*    */   }
/*    */   
/*    */   public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
/* 74 */     int limit = start + length;
/* 75 */     for (int i = start; i < limit; i++) {
/* 76 */       switch (ch[i]) {
/*    */         case '&':
/* 78 */           out.write("&amp;");
/*    */           break;
/*    */         case '<':
/* 81 */           out.write("&lt;");
/*    */           break;
/*    */         case '>':
/* 84 */           out.write("&gt;");
/*    */           break;
/*    */         case '"':
/* 87 */           if (isAttVal) {
/* 88 */             out.write("&quot;"); break;
/*    */           } 
/* 90 */           out.write(34);
/*    */           break;
/*    */         
/*    */         default:
/* 94 */           if (this.encoder.canEncode(ch[i])) {
/* 95 */             out.write(ch[i]); break;
/*    */           } 
/* 97 */           out.write("&#");
/* 98 */           out.write(Integer.toString(ch[i]));
/* 99 */           out.write(59);
/*    */           break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\marshaller\NioEscapeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
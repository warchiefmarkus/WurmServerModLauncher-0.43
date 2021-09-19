/*    */ package com.sun.xml.bind.marshaller;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DumbEscapeHandler
/*    */   implements CharacterEscapeHandler
/*    */ {
/* 55 */   public static final CharacterEscapeHandler theInstance = new DumbEscapeHandler();
/*    */   
/*    */   public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
/* 58 */     int limit = start + length;
/* 59 */     for (int i = start; i < limit; i++) {
/* 60 */       switch (ch[i]) {
/*    */         case '&':
/* 62 */           out.write("&amp;");
/*    */           break;
/*    */         case '<':
/* 65 */           out.write("&lt;");
/*    */           break;
/*    */         case '>':
/* 68 */           out.write("&gt;");
/*    */           break;
/*    */         case '"':
/* 71 */           if (isAttVal) {
/* 72 */             out.write("&quot;"); break;
/*    */           } 
/* 74 */           out.write(34);
/*    */           break;
/*    */         
/*    */         default:
/* 78 */           if (ch[i] > '') {
/* 79 */             out.write("&#");
/* 80 */             out.write(Integer.toString(ch[i]));
/* 81 */             out.write(59); break;
/*    */           } 
/* 83 */           out.write(ch[i]);
/*    */           break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\marshaller\DumbEscapeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
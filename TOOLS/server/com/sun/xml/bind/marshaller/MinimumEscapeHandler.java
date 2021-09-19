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
/*    */ 
/*    */ public class MinimumEscapeHandler
/*    */   implements CharacterEscapeHandler
/*    */ {
/* 56 */   public static final CharacterEscapeHandler theInstance = new MinimumEscapeHandler();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
/* 62 */     int limit = start + length;
/* 63 */     for (int i = start; i < limit; i++) {
/* 64 */       char c = ch[i];
/* 65 */       if (c == '&' || c == '<' || c == '>' || (c == '"' && isAttVal)) {
/* 66 */         if (i != start)
/* 67 */           out.write(ch, start, i - start); 
/* 68 */         start = i + 1;
/* 69 */         switch (ch[i]) {
/*    */           case '&':
/* 71 */             out.write("&amp;");
/*    */             break;
/*    */           case '<':
/* 74 */             out.write("&lt;");
/*    */             break;
/*    */           case '>':
/* 77 */             out.write("&gt;");
/*    */             break;
/*    */           case '"':
/* 80 */             out.write("&quot;");
/*    */             break;
/*    */         } 
/*    */       
/*    */       } 
/*    */     } 
/* 86 */     if (start != limit)
/* 87 */       out.write(ch, start, limit - start); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\marshaller\MinimumEscapeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
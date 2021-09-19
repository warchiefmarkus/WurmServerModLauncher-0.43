/*    */ package com.sun.xml.txw2.output;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DumbEscapeHandler
/*    */   implements CharacterEscapeHandler
/*    */ {
/* 59 */   public static final CharacterEscapeHandler theInstance = new DumbEscapeHandler();
/*    */   
/*    */   public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
/* 62 */     int limit = start + length;
/* 63 */     for (int i = start; i < limit; i++) {
/* 64 */       switch (ch[i]) {
/*    */         case '&':
/* 66 */           out.write("&amp;");
/*    */           break;
/*    */         case '<':
/* 69 */           out.write("&lt;");
/*    */           break;
/*    */         case '>':
/* 72 */           out.write("&gt;");
/*    */           break;
/*    */         case '"':
/* 75 */           if (isAttVal) {
/* 76 */             out.write("&quot;"); break;
/*    */           } 
/* 78 */           out.write(34);
/*    */           break;
/*    */         
/*    */         default:
/* 82 */           if (ch[i] > '') {
/* 83 */             out.write("&#");
/* 84 */             out.write(Integer.toString(ch[i]));
/* 85 */             out.write(59); break;
/*    */           } 
/* 87 */           out.write(ch[i]);
/*    */           break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\DumbEscapeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
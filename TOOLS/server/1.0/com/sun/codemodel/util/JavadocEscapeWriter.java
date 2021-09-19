/*    */ package 1.0.com.sun.codemodel.util;
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
/*    */ public class JavadocEscapeWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   public JavadocEscapeWriter(Writer next) {
/* 34 */     super(next);
/*    */   }
/*    */   
/*    */   public void write(int ch) throws IOException {
/* 38 */     if (ch == 60) {
/* 39 */       this.out.write("&lt;");
/*    */     }
/* 41 */     else if (ch == 38) {
/* 42 */       this.out.write("&amp;");
/*    */     } else {
/* 44 */       this.out.write(ch);
/*    */     } 
/*    */   }
/*    */   public void write(char[] buf, int off, int len) throws IOException {
/* 48 */     for (int i = 0; i < len; i++)
/* 49 */       write(buf[off + i]); 
/*    */   }
/*    */   
/*    */   public void write(char[] buf) throws IOException {
/* 53 */     write(buf, 0, buf.length);
/*    */   }
/*    */   
/*    */   public void write(String buf, int off, int len) throws IOException {
/* 57 */     write(buf.toCharArray(), off, len);
/*    */   }
/*    */   
/*    */   public void write(String buf) throws IOException {
/* 61 */     write(buf.toCharArray(), 0, buf.length());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemode\\util\JavadocEscapeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
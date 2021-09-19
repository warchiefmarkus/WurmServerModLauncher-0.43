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
/*    */ public class UnicodeEscapeWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   public UnicodeEscapeWriter(Writer next) {
/* 24 */     super(next);
/*    */   }
/*    */   
/*    */   public final void write(int ch) throws IOException {
/* 28 */     if (!requireEscaping(ch)) { this.out.write(ch); }
/*    */     else
/*    */     
/* 31 */     { this.out.write("\\u");
/* 32 */       String s = Integer.toHexString(ch);
/* 33 */       for (int i = s.length(); i < 4; i++)
/* 34 */         this.out.write(48); 
/* 35 */       this.out.write(s); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean requireEscaping(int ch) {
/* 44 */     if (ch >= 128) return true;
/*    */ 
/*    */     
/* 47 */     if (ch < 32 && " \t\r\n".indexOf(ch) == -1) return true;
/*    */     
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   public final void write(char[] buf, int off, int len) throws IOException {
/* 53 */     for (int i = 0; i < len; i++)
/* 54 */       write(buf[off + i]); 
/*    */   }
/*    */   
/*    */   public final void write(char[] buf) throws IOException {
/* 58 */     write(buf, 0, buf.length);
/*    */   }
/*    */   
/*    */   public final void write(String buf, int off, int len) throws IOException {
/* 62 */     write(buf.toCharArray(), off, len);
/*    */   }
/*    */   
/*    */   public final void write(String buf) throws IOException {
/* 66 */     write(buf.toCharArray(), 0, buf.length());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemode\\util\UnicodeEscapeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
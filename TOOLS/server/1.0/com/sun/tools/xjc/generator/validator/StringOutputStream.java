/*    */ package 1.0.com.sun.tools.xjc.generator.validator;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private final Writer writer;
/*    */   
/*    */   public StringOutputStream(Writer _writer) {
/* 17 */     this.writer = _writer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(int ch) throws IOException {
/* 23 */     this.writer.write(ch);
/*    */   }
/*    */   
/*    */   public void write(byte[] data) throws IOException {
/* 27 */     write(data, 0, data.length);
/*    */   }
/*    */   
/*    */   public void write(byte[] data, int start, int len) throws IOException {
/* 31 */     char[] buf = new char[len];
/*    */     
/* 33 */     for (int i = 0; i < len; i++)
/* 34 */       buf[i] = (char)(data[i + start] & 0xFF); 
/* 35 */     this.writer.write(buf);
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 39 */     this.writer.close();
/*    */   }
/*    */   
/*    */   public void flush() throws IOException {
/* 43 */     this.writer.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\validator\StringOutputStream.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
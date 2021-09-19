/*    */ package 1.0.com.sun.codemodel.fmt;
/*    */ 
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JBinaryFile
/*    */   extends JResourceFile
/*    */ {
/* 22 */   private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */   
/*    */   public JBinaryFile(String name) {
/* 25 */     super(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream getDataStore() {
/* 35 */     return this.baos;
/*    */   }
/*    */   
/*    */   public void build(OutputStream os) throws IOException {
/* 39 */     os.write(this.baos.toByteArray());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\fmt\JBinaryFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package 1.0.com.sun.codemodel.fmt;
/*    */ 
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import java.io.DataInputStream;
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
/*    */ public final class JStaticFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   private final String resourceName;
/*    */   
/*    */   public JStaticFile(String _resourceName) {
/* 25 */     this(com.sun.codemodel.fmt.JStaticFile.class.getClassLoader(), _resourceName);
/*    */   }
/*    */   
/*    */   public JStaticFile(ClassLoader _classLoader, String _resourceName) {
/* 29 */     super(_resourceName.substring(_resourceName.lastIndexOf('/') + 1));
/* 30 */     this.classLoader = _classLoader;
/* 31 */     this.resourceName = _resourceName;
/*    */   }
/*    */   
/*    */   protected void build(OutputStream os) throws IOException {
/* 35 */     DataInputStream dis = new DataInputStream(this.classLoader.getResourceAsStream(this.resourceName));
/*    */     
/* 37 */     byte[] buf = new byte[256];
/*    */     int sz;
/* 39 */     while ((sz = dis.read(buf)) > 0) {
/* 40 */       os.write(buf, 0, sz);
/*    */     }
/* 42 */     dis.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\fmt\JStaticFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
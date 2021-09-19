/*    */ package 1.0.com.sun.codemodel.fmt;
/*    */ 
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JPropertyFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private final Properties data;
/*    */   
/*    */   public JPropertyFile(String name) {
/* 18 */     super(name);
/*    */ 
/*    */     
/* 21 */     this.data = new Properties();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(String key, String value) {
/* 29 */     this.data.put(key, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void build(OutputStream out) throws IOException {
/* 37 */     this.data.store(out, (String)null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\fmt\JPropertyFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
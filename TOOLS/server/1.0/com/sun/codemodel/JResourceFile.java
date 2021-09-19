/*    */ package 1.0.com.sun.codemodel;
/*    */ 
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
/*    */ public abstract class JResourceFile
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   protected JResourceFile(String name) {
/* 19 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String name() {
/* 26 */     return this.name;
/*    */   }
/*    */   
/*    */   protected abstract void build(OutputStream paramOutputStream) throws IOException;
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JResourceFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
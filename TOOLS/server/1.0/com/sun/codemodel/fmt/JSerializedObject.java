/*    */ package 1.0.com.sun.codemodel.fmt;
/*    */ 
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JSerializedObject
/*    */   extends JResourceFile
/*    */ {
/*    */   private final Object obj;
/*    */   
/*    */   public JSerializedObject(String name, Object obj) throws IOException {
/* 28 */     super(name);
/* 29 */     this.obj = obj;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void build(OutputStream os) throws IOException {
/* 37 */     ObjectOutputStream oos = new ObjectOutputStream(os);
/* 38 */     oos.writeObject(this.obj);
/* 39 */     oos.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\fmt\JSerializedObject.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
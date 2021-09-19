/*    */ package com.sun.codemodel.fmt;
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
/* 43 */     super(name);
/* 44 */     this.obj = obj;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void build(OutputStream os) throws IOException {
/* 52 */     ObjectOutputStream oos = new ObjectOutputStream(os);
/* 53 */     oos.writeObject(this.obj);
/* 54 */     oos.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\fmt\JSerializedObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
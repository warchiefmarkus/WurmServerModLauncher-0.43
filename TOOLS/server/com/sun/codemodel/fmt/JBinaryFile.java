/*    */ package com.sun.codemodel.fmt;
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
/*    */ public final class JBinaryFile
/*    */   extends JResourceFile
/*    */ {
/* 37 */   private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */   
/*    */   public JBinaryFile(String name) {
/* 40 */     super(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream getDataStore() {
/* 50 */     return this.baos;
/*    */   }
/*    */   
/*    */   public void build(OutputStream os) throws IOException {
/* 54 */     os.write(this.baos.toByteArray());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\fmt\JBinaryFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
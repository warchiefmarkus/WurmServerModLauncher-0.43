/*    */ package com.sun.jnlp;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class MeteredFileOutputStream
/*    */   extends FileOutputStream
/*    */ {
/* 18 */   static String _message = null;
/*    */   private FileContentsImpl _contents;
/* 20 */   private long _written = 0L;
/*    */ 
/*    */   
/*    */   MeteredFileOutputStream(File paramFile, boolean paramBoolean, FileContentsImpl paramFileContentsImpl) throws FileNotFoundException {
/* 24 */     super(paramFile.getAbsolutePath(), paramBoolean);
/* 25 */     this._contents = paramFileContentsImpl;
/* 26 */     this._written = paramFile.length();
/*    */ 
/*    */     
/* 29 */     if (_message == null) {
/* 30 */       _message = ResourceManager.getString("APIImpl.persistence.filesizemessage");
/*    */     }
/*    */   }
/*    */   
/*    */   public void write(int paramInt) throws IOException {
/* 35 */     checkWrite(1);
/* 36 */     super.write(paramInt);
/* 37 */     this._written++;
/*    */   }
/*    */   
/*    */   public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/* 41 */     checkWrite(paramInt2);
/* 42 */     super.write(paramArrayOfbyte, paramInt1, paramInt2);
/* 43 */     this._written += paramInt2;
/*    */   }
/*    */   
/*    */   public void write(byte[] paramArrayOfbyte) throws IOException {
/* 47 */     write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*    */   }
/*    */   
/*    */   private void checkWrite(int paramInt) throws IOException {
/* 51 */     if (this._written + paramInt > this._contents.getMaxLength())
/* 52 */       throw new IOException(_message); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\MeteredFileOutputStream.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
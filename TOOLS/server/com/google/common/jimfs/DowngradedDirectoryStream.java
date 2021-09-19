/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.DirectoryStream;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SecureDirectoryStream;
/*    */ import java.util.Iterator;
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
/*    */ final class DowngradedDirectoryStream
/*    */   implements DirectoryStream<Path>
/*    */ {
/*    */   private final SecureDirectoryStream<Path> secureDirectoryStream;
/*    */   
/*    */   DowngradedDirectoryStream(SecureDirectoryStream<Path> secureDirectoryStream) {
/* 38 */     this.secureDirectoryStream = (SecureDirectoryStream<Path>)Preconditions.checkNotNull(secureDirectoryStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Path> iterator() {
/* 43 */     return this.secureDirectoryStream.iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 48 */     this.secureDirectoryStream.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\DowngradedDirectoryStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.wurmonline.shared.util;
/*    */ 
/*    */ import java.io.InputStream;
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
/*    */ public final class StreamUtilities
/*    */ {
/*    */   public static void closeInputStreamIgnoreExceptions(InputStream aInputStream) {
/* 55 */     if (aInputStream != null) {
/*    */       
/*    */       try {
/*    */         
/* 59 */         aInputStream.close();
/*    */       }
/* 61 */       catch (Exception exception) {}
/*    */     }
/*    */   }
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
/*    */   public static void closeOutputStreamIgnoreExceptions(OutputStream aOutputStream) {
/* 79 */     if (aOutputStream != null)
/*    */       
/*    */       try {
/*    */         
/* 83 */         aOutputStream.close();
/*    */       }
/* 85 */       catch (Exception exception) {} 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\StreamUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
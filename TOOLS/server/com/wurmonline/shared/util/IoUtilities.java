/*    */ package com.wurmonline.shared.util;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
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
/*    */ public final class IoUtilities
/*    */ {
/*    */   public static void closeClosable(Closeable closableObject) {
/* 49 */     if (closableObject != null) {
/*    */       
/*    */       try {
/*    */         
/* 53 */         closableObject.close();
/*    */       }
/* 55 */       catch (IOException iOException) {}
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
/*    */   public static void closeHttpURLConnection(HttpURLConnection httpURLConnection) {
/* 71 */     if (httpURLConnection != null)
/*    */       
/*    */       try {
/*    */         
/* 75 */         httpURLConnection.disconnect();
/*    */       }
/* 77 */       catch (Exception exception) {} 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\IoUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
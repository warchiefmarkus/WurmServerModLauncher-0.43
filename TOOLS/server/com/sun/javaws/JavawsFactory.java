/*    */ package com.sun.javaws;
/*    */ 
/*    */ import com.sun.javaws.net.BasicDownloadLayer;
/*    */ import com.sun.javaws.net.BasicNetworkLayer;
/*    */ import com.sun.javaws.net.HttpDownload;
/*    */ import com.sun.javaws.net.HttpRequest;
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
/*    */ public class JavawsFactory
/*    */ {
/* 25 */   private static HttpRequest _httpRequestImpl = (HttpRequest)new BasicNetworkLayer();
/* 26 */   private static HttpDownload _httpDownloadImpl = (HttpDownload)new BasicDownloadLayer(_httpRequestImpl);
/*    */ 
/*    */   
/*    */   public static HttpRequest getHttpRequestImpl() {
/* 30 */     return _httpRequestImpl;
/*    */   } public static HttpDownload getHttpDownloadImpl() {
/* 32 */     return _httpDownloadImpl;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\JavawsFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
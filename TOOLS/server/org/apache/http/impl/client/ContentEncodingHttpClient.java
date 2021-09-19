/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.HttpResponseInterceptor;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.client.protocol.RequestAcceptEncoding;
/*    */ import org.apache.http.client.protocol.ResponseContentEncoding;
/*    */ import org.apache.http.conn.ClientConnectionManager;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.protocol.BasicHttpProcessor;
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
/*    */ @Deprecated
/*    */ @ThreadSafe
/*    */ public class ContentEncodingHttpClient
/*    */   extends DefaultHttpClient
/*    */ {
/*    */   public ContentEncodingHttpClient(ClientConnectionManager conman, HttpParams params) {
/* 62 */     super(conman, params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ContentEncodingHttpClient(HttpParams params) {
/* 69 */     this(null, params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ContentEncodingHttpClient() {
/* 76 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected BasicHttpProcessor createHttpProcessor() {
/* 84 */     BasicHttpProcessor result = super.createHttpProcessor();
/*    */     
/* 86 */     result.addRequestInterceptor((HttpRequestInterceptor)new RequestAcceptEncoding());
/* 87 */     result.addResponseInterceptor((HttpResponseInterceptor)new ResponseContentEncoding());
/*    */     
/* 89 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\ContentEncodingHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
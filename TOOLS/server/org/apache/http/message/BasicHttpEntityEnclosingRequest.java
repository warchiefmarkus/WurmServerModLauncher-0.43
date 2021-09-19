/*    */ package org.apache.http.message;
/*    */ 
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.RequestLine;
/*    */ import org.apache.http.annotation.NotThreadSafe;
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
/*    */ @NotThreadSafe
/*    */ public class BasicHttpEntityEnclosingRequest
/*    */   extends BasicHttpRequest
/*    */   implements HttpEntityEnclosingRequest
/*    */ {
/*    */   private HttpEntity entity;
/*    */   
/*    */   public BasicHttpEntityEnclosingRequest(String method, String uri) {
/* 50 */     super(method, uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public BasicHttpEntityEnclosingRequest(String method, String uri, ProtocolVersion ver) {
/* 55 */     super(method, uri, ver);
/*    */   }
/*    */   
/*    */   public BasicHttpEntityEnclosingRequest(RequestLine requestline) {
/* 59 */     super(requestline);
/*    */   }
/*    */   
/*    */   public HttpEntity getEntity() {
/* 63 */     return this.entity;
/*    */   }
/*    */   
/*    */   public void setEntity(HttpEntity entity) {
/* 67 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public boolean expectContinue() {
/* 71 */     Header expect = getFirstHeader("Expect");
/* 72 */     return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHttpEntityEnclosingRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
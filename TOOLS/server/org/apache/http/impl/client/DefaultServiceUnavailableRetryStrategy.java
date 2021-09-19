/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ @Immutable
/*    */ public class DefaultServiceUnavailableRetryStrategy
/*    */   implements ServiceUnavailableRetryStrategy
/*    */ {
/*    */   private final int maxRetries;
/*    */   private final long retryInterval;
/*    */   
/*    */   public DefaultServiceUnavailableRetryStrategy(int maxRetries, int retryInterval) {
/* 60 */     if (maxRetries < 1) {
/* 61 */       throw new IllegalArgumentException("MaxRetries must be greater than 1");
/*    */     }
/* 63 */     if (retryInterval < 1) {
/* 64 */       throw new IllegalArgumentException("Retry interval must be greater than 1");
/*    */     }
/* 66 */     this.maxRetries = maxRetries;
/* 67 */     this.retryInterval = retryInterval;
/*    */   }
/*    */   
/*    */   public DefaultServiceUnavailableRetryStrategy() {
/* 71 */     this(1, 1000);
/*    */   }
/*    */   
/*    */   public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
/* 75 */     return (executionCount <= this.maxRetries && response.getStatusLine().getStatusCode() == 503);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getRetryInterval() {
/* 80 */     return this.retryInterval;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\DefaultServiceUnavailableRetryStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.HttpTransportMetrics;
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
/*    */ public class HttpTransportMetricsImpl
/*    */   implements HttpTransportMetrics
/*    */ {
/* 41 */   private long bytesTransferred = 0L;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getBytesTransferred() {
/* 48 */     return this.bytesTransferred;
/*    */   }
/*    */   
/*    */   public void setBytesTransferred(long count) {
/* 52 */     this.bytesTransferred = count;
/*    */   }
/*    */   
/*    */   public void incrementBytesTransferred(long count) {
/* 56 */     this.bytesTransferred += count;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 60 */     this.bytesTransferred = 0L;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\HttpTransportMetricsImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
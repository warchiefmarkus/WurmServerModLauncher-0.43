/*    */ package org.fourthline.cling.transport.impl.jetty;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import org.fourthline.cling.transport.spi.AbstractStreamClientConfiguration;
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
/*    */ public class StreamClientConfigurationImpl
/*    */   extends AbstractStreamClientConfiguration
/*    */ {
/*    */   public StreamClientConfigurationImpl(ExecutorService timeoutExecutorService) {
/* 30 */     super(timeoutExecutorService);
/*    */   }
/*    */   
/*    */   public StreamClientConfigurationImpl(ExecutorService timeoutExecutorService, int timeoutSeconds) {
/* 34 */     super(timeoutExecutorService, timeoutSeconds);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRequestRetryCount() {
/* 41 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\jetty\StreamClientConfigurationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
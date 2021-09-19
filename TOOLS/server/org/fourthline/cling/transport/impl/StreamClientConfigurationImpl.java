/*    */ package org.fourthline.cling.transport.impl;
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
/*    */   private boolean usePersistentConnections = false;
/*    */   
/*    */   public StreamClientConfigurationImpl(ExecutorService timeoutExecutorService) {
/* 32 */     super(timeoutExecutorService);
/*    */   }
/*    */   
/*    */   public StreamClientConfigurationImpl(ExecutorService timeoutExecutorService, int timeoutSeconds) {
/* 36 */     super(timeoutExecutorService, timeoutSeconds);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isUsePersistentConnections() {
/* 43 */     return this.usePersistentConnections;
/*    */   }
/*    */   
/*    */   public void setUsePersistentConnections(boolean usePersistentConnections) {
/* 47 */     this.usePersistentConnections = usePersistentConnections;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\StreamClientConfigurationImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
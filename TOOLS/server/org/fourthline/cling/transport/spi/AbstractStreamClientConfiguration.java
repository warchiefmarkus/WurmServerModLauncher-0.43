/*    */ package org.fourthline.cling.transport.spi;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import org.fourthline.cling.model.ServerClientTokens;
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
/*    */ public abstract class AbstractStreamClientConfiguration
/*    */   implements StreamClientConfiguration
/*    */ {
/*    */   protected ExecutorService requestExecutorService;
/* 27 */   protected int timeoutSeconds = 60;
/* 28 */   protected int logWarningSeconds = 5;
/*    */   
/*    */   protected AbstractStreamClientConfiguration(ExecutorService requestExecutorService) {
/* 31 */     this.requestExecutorService = requestExecutorService;
/*    */   }
/*    */   
/*    */   protected AbstractStreamClientConfiguration(ExecutorService requestExecutorService, int timeoutSeconds) {
/* 35 */     this.requestExecutorService = requestExecutorService;
/* 36 */     this.timeoutSeconds = timeoutSeconds;
/*    */   }
/*    */   
/*    */   protected AbstractStreamClientConfiguration(ExecutorService requestExecutorService, int timeoutSeconds, int logWarningSeconds) {
/* 40 */     this.requestExecutorService = requestExecutorService;
/* 41 */     this.timeoutSeconds = timeoutSeconds;
/* 42 */     this.logWarningSeconds = logWarningSeconds;
/*    */   }
/*    */   
/*    */   public ExecutorService getRequestExecutorService() {
/* 46 */     return this.requestExecutorService;
/*    */   }
/*    */   
/*    */   public void setRequestExecutorService(ExecutorService requestExecutorService) {
/* 50 */     this.requestExecutorService = requestExecutorService;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTimeoutSeconds() {
/* 57 */     return this.timeoutSeconds;
/*    */   }
/*    */   
/*    */   public void setTimeoutSeconds(int timeoutSeconds) {
/* 61 */     this.timeoutSeconds = timeoutSeconds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLogWarningSeconds() {
/* 68 */     return this.logWarningSeconds;
/*    */   }
/*    */   
/*    */   public void setLogWarningSeconds(int logWarningSeconds) {
/* 72 */     this.logWarningSeconds = logWarningSeconds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserAgentValue(int majorVersion, int minorVersion) {
/* 79 */     return (new ServerClientTokens(majorVersion, minorVersion)).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\AbstractStreamClientConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
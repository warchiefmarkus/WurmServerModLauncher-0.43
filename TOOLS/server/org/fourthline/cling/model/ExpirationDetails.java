/*    */ package org.fourthline.cling.model;
/*    */ 
/*    */ import java.util.Date;
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
/*    */ public class ExpirationDetails
/*    */ {
/*    */   public static final int UNLIMITED_AGE = 0;
/* 27 */   private int maxAgeSeconds = 0;
/* 28 */   private long lastRefreshTimestampSeconds = getCurrentTimestampSeconds();
/*    */ 
/*    */   
/*    */   public ExpirationDetails() {}
/*    */   
/*    */   public ExpirationDetails(int maxAgeSeconds) {
/* 34 */     this.maxAgeSeconds = maxAgeSeconds;
/*    */   }
/*    */   
/*    */   public int getMaxAgeSeconds() {
/* 38 */     return this.maxAgeSeconds;
/*    */   }
/*    */   
/*    */   public long getLastRefreshTimestampSeconds() {
/* 42 */     return this.lastRefreshTimestampSeconds;
/*    */   }
/*    */   
/*    */   public void setLastRefreshTimestampSeconds(long lastRefreshTimestampSeconds) {
/* 46 */     this.lastRefreshTimestampSeconds = lastRefreshTimestampSeconds;
/*    */   }
/*    */   
/*    */   public void stampLastRefresh() {
/* 50 */     setLastRefreshTimestampSeconds(getCurrentTimestampSeconds());
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 54 */     return hasExpired(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasExpired(boolean halfTime) {
/* 63 */     return (this.maxAgeSeconds != 0 && this.lastRefreshTimestampSeconds + (this.maxAgeSeconds / (halfTime ? 2 : 1)) < 
/* 64 */       getCurrentTimestampSeconds());
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSecondsUntilExpiration() {
/* 69 */     return (this.maxAgeSeconds == 0) ? 2147483647L : (this.lastRefreshTimestampSeconds + this.maxAgeSeconds - 
/*    */       
/* 71 */       getCurrentTimestampSeconds());
/*    */   }
/*    */   
/*    */   protected long getCurrentTimestampSeconds() {
/* 75 */     return (new Date()).getTime() / 1000L;
/*    */   }
/*    */ 
/*    */   
/* 79 */   private static String simpleName = ExpirationDetails.class.getSimpleName();
/*    */   
/*    */   public String toString() {
/* 82 */     return "(" + simpleName + ")" + " MAX AGE: " + this.maxAgeSeconds;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\ExpirationDetails.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
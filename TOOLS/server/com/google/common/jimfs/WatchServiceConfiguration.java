/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public abstract class WatchServiceConfiguration
/*    */ {
/* 37 */   static final WatchServiceConfiguration DEFAULT = polling(5L, TimeUnit.SECONDS);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static WatchServiceConfiguration polling(long interval, TimeUnit timeUnit) {
/* 45 */     return new PollingConfig(interval, timeUnit);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   abstract AbstractWatchService newWatchService(FileSystemView paramFileSystemView, PathService paramPathService);
/*    */ 
/*    */ 
/*    */   
/*    */   private static final class PollingConfig
/*    */     extends WatchServiceConfiguration
/*    */   {
/*    */     private final long interval;
/*    */ 
/*    */     
/*    */     private final TimeUnit timeUnit;
/*    */ 
/*    */ 
/*    */     
/*    */     private PollingConfig(long interval, TimeUnit timeUnit) {
/* 66 */       Preconditions.checkArgument((interval > 0L), "interval (%s) must be positive", new Object[] { Long.valueOf(interval) });
/* 67 */       this.interval = interval;
/* 68 */       this.timeUnit = (TimeUnit)Preconditions.checkNotNull(timeUnit);
/*    */     }
/*    */ 
/*    */     
/*    */     AbstractWatchService newWatchService(FileSystemView view, PathService pathService) {
/* 73 */       return new PollingWatchService(view, pathService, view.state(), this.interval, this.timeUnit);
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 78 */       return "WatchServiceConfiguration.polling(" + this.interval + ", " + this.timeUnit + ")";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\WatchServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.fourthline.cling.registry;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public class RegistryMaintainer
/*    */   implements Runnable
/*    */ {
/* 28 */   private static Logger log = Logger.getLogger(RegistryMaintainer.class.getName());
/*    */   
/*    */   private final RegistryImpl registry;
/*    */   
/*    */   private final int sleepIntervalMillis;
/*    */   private volatile boolean stopped = false;
/*    */   
/*    */   public RegistryMaintainer(RegistryImpl registry, int sleepIntervalMillis) {
/* 36 */     this.registry = registry;
/* 37 */     this.sleepIntervalMillis = sleepIntervalMillis;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 41 */     if (log.isLoggable(Level.FINE))
/* 42 */       log.fine("Setting stopped status on thread"); 
/* 43 */     this.stopped = true;
/*    */   }
/*    */   
/*    */   public void run() {
/* 47 */     this.stopped = false;
/* 48 */     if (log.isLoggable(Level.FINE))
/* 49 */       log.fine("Running registry maintenance loop every milliseconds: " + this.sleepIntervalMillis); 
/* 50 */     while (!this.stopped) {
/*    */       
/*    */       try {
/* 53 */         this.registry.maintain();
/* 54 */         Thread.sleep(this.sleepIntervalMillis);
/* 55 */       } catch (InterruptedException ex) {
/* 56 */         this.stopped = true;
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     log.fine("Stopped status on thread received, ending maintenance loop");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\RegistryMaintainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.fourthline.cling.protocol;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.transport.RouterException;
/*    */ import org.seamless.util.Exceptions;
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
/*    */ public abstract class SendingAsync
/*    */   implements Runnable
/*    */ {
/* 40 */   private static final Logger log = Logger.getLogger(UpnpService.class.getName());
/*    */   
/*    */   private final UpnpService upnpService;
/*    */   
/*    */   protected SendingAsync(UpnpService upnpService) {
/* 45 */     this.upnpService = upnpService;
/*    */   }
/*    */   
/*    */   public UpnpService getUpnpService() {
/* 49 */     return this.upnpService;
/*    */   }
/*    */   
/*    */   public void run() {
/*    */     try {
/* 54 */       execute();
/* 55 */     } catch (Exception ex) {
/* 56 */       Throwable cause = Exceptions.unwrap(ex);
/* 57 */       if (cause instanceof InterruptedException) {
/* 58 */         log.log(Level.INFO, "Interrupted protocol '" + getClass().getSimpleName() + "': " + ex, cause);
/*    */       } else {
/* 60 */         throw new RuntimeException("Fatal error while executing protocol '" + 
/* 61 */             getClass().getSimpleName() + "': " + ex, ex);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void execute() throws RouterException;
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "(" + getClass().getSimpleName() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\SendingAsync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
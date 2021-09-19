/*    */ package org.fourthline.cling.protocol.async;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.UpnpService;
/*    */ import org.fourthline.cling.model.meta.LocalDevice;
/*    */ import org.fourthline.cling.model.types.NotificationSubtype;
/*    */ import org.fourthline.cling.transport.RouterException;
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
/*    */ public class SendingNotificationAlive
/*    */   extends SendingNotification
/*    */ {
/* 32 */   private static final Logger log = Logger.getLogger(SendingNotification.class.getName());
/*    */   
/*    */   public SendingNotificationAlive(UpnpService upnpService, LocalDevice device) {
/* 35 */     super(upnpService, device);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void execute() throws RouterException {
/* 40 */     log.fine("Sending alive messages (" + getBulkRepeat() + " times) for: " + getDevice());
/* 41 */     super.execute();
/*    */   }
/*    */   
/*    */   protected NotificationSubtype getNotificationSubtype() {
/* 45 */     return NotificationSubtype.ALIVE;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\async\SendingNotificationAlive.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
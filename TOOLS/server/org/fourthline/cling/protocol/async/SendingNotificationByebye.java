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
/*    */ public class SendingNotificationByebye
/*    */   extends SendingNotification
/*    */ {
/* 32 */   private static final Logger log = Logger.getLogger(SendingNotification.class.getName());
/*    */   
/*    */   public SendingNotificationByebye(UpnpService upnpService, LocalDevice device) {
/* 35 */     super(upnpService, device);
/*    */   }
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
/*    */   protected void execute() throws RouterException {
/* 49 */     log.fine("Sending byebye messages (" + getBulkRepeat() + " times) for: " + getDevice());
/* 50 */     super.execute();
/*    */   }
/*    */   
/*    */   protected NotificationSubtype getNotificationSubtype() {
/* 54 */     return NotificationSubtype.BYEBYE;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\async\SendingNotificationByebye.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
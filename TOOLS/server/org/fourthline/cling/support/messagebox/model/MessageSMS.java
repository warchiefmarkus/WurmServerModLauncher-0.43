/*    */ package org.fourthline.cling.support.messagebox.model;
/*    */ 
/*    */ import org.fourthline.cling.support.messagebox.parser.MessageElement;
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
/*    */ public class MessageSMS
/*    */   extends Message
/*    */ {
/*    */   private final DateTime receiveTime;
/*    */   private final NumberName receiver;
/*    */   private final NumberName sender;
/*    */   private final String body;
/*    */   
/*    */   public MessageSMS(NumberName receiver, NumberName sender, String body) {
/* 33 */     this(new DateTime(), receiver, sender, body);
/*    */   }
/*    */   
/*    */   public MessageSMS(DateTime receiveTime, NumberName receiver, NumberName sender, String body) {
/* 37 */     this(Message.DisplayType.MAXIMUM, receiveTime, receiver, sender, body);
/*    */   }
/*    */   
/*    */   public MessageSMS(Message.DisplayType displayType, DateTime receiveTime, NumberName receiver, NumberName sender, String body) {
/* 41 */     super(Message.Category.SMS, displayType);
/* 42 */     this.receiveTime = receiveTime;
/* 43 */     this.receiver = receiver;
/* 44 */     this.sender = sender;
/* 45 */     this.body = body;
/*    */   }
/*    */   
/*    */   public DateTime getReceiveTime() {
/* 49 */     return this.receiveTime;
/*    */   }
/*    */   
/*    */   public NumberName getReceiver() {
/* 53 */     return this.receiver;
/*    */   }
/*    */   
/*    */   public NumberName getSender() {
/* 57 */     return this.sender;
/*    */   }
/*    */   
/*    */   public String getBody() {
/* 61 */     return this.body;
/*    */   }
/*    */   
/*    */   public void appendMessageElements(MessageElement parent) {
/* 65 */     getReceiveTime().appendMessageElements((MessageElement)parent.createChild("ReceiveTime"));
/* 66 */     getReceiver().appendMessageElements((MessageElement)parent.createChild("Receiver"));
/* 67 */     getSender().appendMessageElements((MessageElement)parent.createChild("Sender"));
/* 68 */     ((MessageElement)parent.createChild("Body")).setContent(getBody());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\model\MessageSMS.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
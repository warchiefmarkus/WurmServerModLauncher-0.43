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
/*    */ public class MessageScheduleReminder
/*    */   extends Message
/*    */ {
/*    */   private final DateTime startTime;
/*    */   private final NumberName owner;
/*    */   private final String subject;
/*    */   private final DateTime endTime;
/*    */   private final String location;
/*    */   private final String body;
/*    */   
/*    */   public MessageScheduleReminder(DateTime startTime, NumberName owner, String subject, DateTime endTime, String location, String body) {
/* 34 */     this(Message.DisplayType.MAXIMUM, startTime, owner, subject, endTime, location, body);
/*    */   }
/*    */ 
/*    */   
/*    */   public MessageScheduleReminder(Message.DisplayType displayType, DateTime startTime, NumberName owner, String subject, DateTime endTime, String location, String body) {
/* 39 */     super(Message.Category.SCHEDULE_REMINDER, displayType);
/* 40 */     this.startTime = startTime;
/* 41 */     this.owner = owner;
/* 42 */     this.subject = subject;
/* 43 */     this.endTime = endTime;
/* 44 */     this.location = location;
/* 45 */     this.body = body;
/*    */   }
/*    */   
/*    */   public DateTime getStartTime() {
/* 49 */     return this.startTime;
/*    */   }
/*    */   
/*    */   public NumberName getOwner() {
/* 53 */     return this.owner;
/*    */   }
/*    */   
/*    */   public String getSubject() {
/* 57 */     return this.subject;
/*    */   }
/*    */   
/*    */   public DateTime getEndTime() {
/* 61 */     return this.endTime;
/*    */   }
/*    */   
/*    */   public String getLocation() {
/* 65 */     return this.location;
/*    */   }
/*    */   
/*    */   public String getBody() {
/* 69 */     return this.body;
/*    */   }
/*    */   
/*    */   public void appendMessageElements(MessageElement parent) {
/* 73 */     getStartTime().appendMessageElements((MessageElement)parent.createChild("StartTime"));
/* 74 */     getOwner().appendMessageElements((MessageElement)parent.createChild("Owner"));
/* 75 */     ((MessageElement)parent.createChild("Subject")).setContent(getSubject());
/* 76 */     getEndTime().appendMessageElements((MessageElement)parent.createChild("EndTime"));
/* 77 */     ((MessageElement)parent.createChild("Location")).setContent(getLocation());
/* 78 */     ((MessageElement)parent.createChild("Body")).setContent(getBody());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\model\MessageScheduleReminder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
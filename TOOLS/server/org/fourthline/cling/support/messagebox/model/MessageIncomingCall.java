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
/*    */ public class MessageIncomingCall
/*    */   extends Message
/*    */ {
/*    */   private final DateTime callTime;
/*    */   private final NumberName callee;
/*    */   private final NumberName caller;
/*    */   
/*    */   public MessageIncomingCall(NumberName callee, NumberName caller) {
/* 30 */     this(new DateTime(), callee, caller);
/*    */   }
/*    */   
/*    */   public MessageIncomingCall(DateTime callTime, NumberName callee, NumberName caller) {
/* 34 */     this(Message.DisplayType.MAXIMUM, callTime, callee, caller);
/*    */   }
/*    */   
/*    */   public MessageIncomingCall(Message.DisplayType displayType, DateTime callTime, NumberName callee, NumberName caller) {
/* 38 */     super(Message.Category.INCOMING_CALL, displayType);
/* 39 */     this.callTime = callTime;
/* 40 */     this.callee = callee;
/* 41 */     this.caller = caller;
/*    */   }
/*    */   
/*    */   public DateTime getCallTime() {
/* 45 */     return this.callTime;
/*    */   }
/*    */   
/*    */   public NumberName getCallee() {
/* 49 */     return this.callee;
/*    */   }
/*    */   
/*    */   public NumberName getCaller() {
/* 53 */     return this.caller;
/*    */   }
/*    */   
/*    */   public void appendMessageElements(MessageElement parent) {
/* 57 */     getCallTime().appendMessageElements((MessageElement)parent.createChild("CallTime"));
/* 58 */     getCallee().appendMessageElements((MessageElement)parent.createChild("Callee"));
/* 59 */     getCaller().appendMessageElements((MessageElement)parent.createChild("Caller"));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\model\MessageIncomingCall.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
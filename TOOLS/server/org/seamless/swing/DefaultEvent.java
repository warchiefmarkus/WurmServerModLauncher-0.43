/*    */ package org.seamless.swing;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class DefaultEvent<PAYLOAD>
/*    */   implements Event
/*    */ {
/*    */   PAYLOAD payload;
/* 30 */   Set<Controller> firedInControllers = new HashSet<Controller>();
/*    */   
/*    */   public DefaultEvent() {}
/*    */   
/*    */   public DefaultEvent(PAYLOAD payload) {
/* 35 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public PAYLOAD getPayload() {
/* 39 */     return this.payload;
/*    */   }
/*    */   
/*    */   public void setPayload(PAYLOAD payload) {
/* 43 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public void addFiredInController(Controller seenController) {
/* 47 */     this.firedInControllers.add(seenController);
/*    */   }
/*    */   
/*    */   public boolean alreadyFired(Controller controller) {
/* 51 */     return this.firedInControllers.contains(controller);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\DefaultEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package javax.xml.bind.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.ValidationEvent;
/*    */ import javax.xml.bind.ValidationEventHandler;
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
/*    */ public class ValidationEventCollector
/*    */   implements ValidationEventHandler
/*    */ {
/* 32 */   private final List<ValidationEvent> events = new ArrayList<ValidationEvent>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ValidationEvent[] getEvents() {
/* 43 */     return this.events.<ValidationEvent>toArray(new ValidationEvent[this.events.size()]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() {
/* 50 */     this.events.clear();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasEvents() {
/* 61 */     return !this.events.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean handleEvent(ValidationEvent event) {
/* 65 */     this.events.add(event);
/*    */     
/* 67 */     boolean retVal = true;
/* 68 */     switch (event.getSeverity())
/*    */     { case 0:
/* 70 */         retVal = true;
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
/* 85 */         return retVal;case 1: retVal = true; return retVal;case 2: retVal = false; return retVal; }  _assert(false, Messages.format("ValidationEventCollector.UnrecognizedSeverity", Integer.valueOf(event.getSeverity()))); return retVal;
/*    */   }
/*    */   
/*    */   private static void _assert(boolean b, String msg) {
/* 89 */     if (!b)
/* 90 */       throw new InternalError(msg); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bin\\util\ValidationEventCollector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
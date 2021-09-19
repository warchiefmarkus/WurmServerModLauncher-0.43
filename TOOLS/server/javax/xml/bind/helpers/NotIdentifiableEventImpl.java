/*    */ package javax.xml.bind.helpers;
/*    */ 
/*    */ import javax.xml.bind.NotIdentifiableEvent;
/*    */ import javax.xml.bind.ValidationEventLocator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NotIdentifiableEventImpl
/*    */   extends ValidationEventImpl
/*    */   implements NotIdentifiableEvent
/*    */ {
/*    */   public NotIdentifiableEventImpl(int _severity, String _message, ValidationEventLocator _locator) {
/* 44 */     super(_severity, _message, _locator);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NotIdentifiableEventImpl(int _severity, String _message, ValidationEventLocator _locator, Throwable _linkedException) {
/* 63 */     super(_severity, _message, _locator, _linkedException);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\helpers\NotIdentifiableEventImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
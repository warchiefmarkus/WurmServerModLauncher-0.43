/*    */ package javax.xml.bind.helpers;
/*    */ 
/*    */ import javax.xml.bind.PrintConversionEvent;
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
/*    */ 
/*    */ public class PrintConversionEventImpl
/*    */   extends ValidationEventImpl
/*    */   implements PrintConversionEvent
/*    */ {
/*    */   public PrintConversionEventImpl(int _severity, String _message, ValidationEventLocator _locator) {
/* 45 */     super(_severity, _message, _locator);
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
/*    */   public PrintConversionEventImpl(int _severity, String _message, ValidationEventLocator _locator, Throwable _linkedException) {
/* 64 */     super(_severity, _message, _locator, _linkedException);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\helpers\PrintConversionEventImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
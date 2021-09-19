/*    */ package javax.xml.bind;
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
/*    */ public class PropertyException
/*    */   extends JAXBException
/*    */ {
/*    */   public PropertyException(String message) {
/* 30 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyException(String message, String errorCode) {
/* 41 */     super(message, errorCode);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyException(Throwable exception) {
/* 51 */     super(exception);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyException(String message, Throwable exception) {
/* 62 */     super(message, exception);
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
/*    */   public PropertyException(String message, String errorCode, Throwable exception) {
/* 77 */     super(message, errorCode, exception);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyException(String name, Object value) {
/* 88 */     super(Messages.format("PropertyException.NameValue", name, value.toString()));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\PropertyException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
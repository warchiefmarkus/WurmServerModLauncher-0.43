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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationException
/*    */   extends JAXBException
/*    */ {
/*    */   public ValidationException(String message) {
/* 33 */     this(message, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ValidationException(String message, String errorCode) {
/* 44 */     this(message, errorCode, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ValidationException(Throwable exception) {
/* 54 */     this(null, null, exception);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ValidationException(String message, Throwable exception) {
/* 65 */     this(message, null, exception);
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
/*    */   public ValidationException(String message, String errorCode, Throwable exception) {
/* 77 */     super(message, errorCode, exception);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\ValidationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
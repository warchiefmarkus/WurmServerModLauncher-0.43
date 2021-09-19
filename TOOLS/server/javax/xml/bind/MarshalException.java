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
/*    */ public class MarshalException
/*    */   extends JAXBException
/*    */ {
/*    */   public MarshalException(String message) {
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
/*    */   public MarshalException(String message, String errorCode) {
/* 44 */     this(message, errorCode, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MarshalException(Throwable exception) {
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
/*    */   public MarshalException(String message, Throwable exception) {
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
/*    */   public MarshalException(String message, String errorCode, Throwable exception) {
/* 77 */     super(message, errorCode, exception);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\MarshalException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
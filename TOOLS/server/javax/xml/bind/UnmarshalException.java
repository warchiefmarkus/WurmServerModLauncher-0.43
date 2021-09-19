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
/*    */ 
/*    */ 
/*    */ public class UnmarshalException
/*    */   extends JAXBException
/*    */ {
/*    */   public UnmarshalException(String message) {
/* 35 */     this(message, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmarshalException(String message, String errorCode) {
/* 46 */     this(message, errorCode, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmarshalException(Throwable exception) {
/* 56 */     this(null, null, exception);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmarshalException(String message, Throwable exception) {
/* 67 */     this(message, null, exception);
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
/*    */   public UnmarshalException(String message, String errorCode, Throwable exception) {
/* 79 */     super(message, errorCode, exception);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\UnmarshalException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
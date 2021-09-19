/*    */ package oauth.signpost.exception;
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
/*    */ public class OAuthCommunicationException
/*    */   extends OAuthException
/*    */ {
/*    */   private String responseBody;
/*    */   
/*    */   public OAuthCommunicationException(Exception cause) {
/* 23 */     super("Communication with the service provider failed: " + cause.getLocalizedMessage(), cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public OAuthCommunicationException(String message, String responseBody) {
/* 28 */     super(message);
/* 29 */     this.responseBody = responseBody;
/*    */   }
/*    */   
/*    */   public String getResponseBody() {
/* 33 */     return this.responseBody;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\exception\OAuthCommunicationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
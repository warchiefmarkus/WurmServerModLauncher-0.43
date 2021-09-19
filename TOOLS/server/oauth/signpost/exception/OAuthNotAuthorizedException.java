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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OAuthNotAuthorizedException
/*    */   extends OAuthException
/*    */ {
/*    */   private static final String ERROR = "Authorization failed (server replied with a 401). This can happen if the consumer key was not correct or the signatures did not match.";
/*    */   private String responseBody;
/*    */   
/*    */   public OAuthNotAuthorizedException() {
/* 27 */     super("Authorization failed (server replied with a 401). This can happen if the consumer key was not correct or the signatures did not match.");
/*    */   }
/*    */   
/*    */   public OAuthNotAuthorizedException(String responseBody) {
/* 31 */     super("Authorization failed (server replied with a 401). This can happen if the consumer key was not correct or the signatures did not match.");
/* 32 */     this.responseBody = responseBody;
/*    */   }
/*    */   
/*    */   public String getResponseBody() {
/* 36 */     return this.responseBody;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\exception\OAuthNotAuthorizedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
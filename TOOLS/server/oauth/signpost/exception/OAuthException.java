/*    */ package oauth.signpost.exception;
/*    */ 
/*    */ public abstract class OAuthException
/*    */   extends Exception
/*    */ {
/*    */   public OAuthException(String message) {
/*  7 */     super(message);
/*    */   }
/*    */   
/*    */   public OAuthException(Throwable cause) {
/* 11 */     super(cause);
/*    */   }
/*    */   
/*    */   public OAuthException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\exception\OAuthException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
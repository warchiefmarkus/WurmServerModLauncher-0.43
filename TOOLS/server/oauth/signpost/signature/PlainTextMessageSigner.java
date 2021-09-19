/*    */ package oauth.signpost.signature;
/*    */ 
/*    */ import oauth.signpost.OAuth;
/*    */ import oauth.signpost.exception.OAuthMessageSignerException;
/*    */ import oauth.signpost.http.HttpParameters;
/*    */ import oauth.signpost.http.HttpRequest;
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
/*    */ public class PlainTextMessageSigner
/*    */   extends OAuthMessageSigner
/*    */ {
/*    */   public String getSignatureMethod() {
/* 27 */     return "PLAINTEXT";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String sign(HttpRequest request, HttpParameters requestParams) throws OAuthMessageSignerException {
/* 33 */     return OAuth.percentEncode(getConsumerSecret()) + '&' + OAuth.percentEncode(getTokenSecret());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\signature\PlainTextMessageSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
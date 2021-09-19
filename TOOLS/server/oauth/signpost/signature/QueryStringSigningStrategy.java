/*    */ package oauth.signpost.signature;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import oauth.signpost.OAuth;
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
/*    */ public class QueryStringSigningStrategy
/*    */   implements SigningStrategy
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public String writeSignature(String signature, HttpRequest request, HttpParameters requestParameters) {
/* 26 */     HttpParameters oauthParams = requestParameters.getOAuthParameters();
/* 27 */     oauthParams.put("oauth_signature", signature, true);
/*    */     
/* 29 */     Iterator<String> iter = oauthParams.keySet().iterator();
/*    */ 
/*    */     
/* 32 */     String firstKey = iter.next();
/* 33 */     StringBuilder sb = new StringBuilder(OAuth.addQueryString(request.getRequestUrl(), oauthParams.getAsQueryString(firstKey)));
/*    */ 
/*    */     
/* 36 */     while (iter.hasNext()) {
/* 37 */       sb.append("&");
/* 38 */       String key = iter.next();
/* 39 */       sb.append(oauthParams.getAsQueryString(key));
/*    */     } 
/*    */     
/* 42 */     String signedUrl = sb.toString();
/*    */     
/* 44 */     request.setRequestUrl(signedUrl);
/*    */     
/* 46 */     return signedUrl;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\signature\QueryStringSigningStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
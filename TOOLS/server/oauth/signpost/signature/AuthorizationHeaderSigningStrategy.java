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
/*    */ public class AuthorizationHeaderSigningStrategy
/*    */   implements SigningStrategy
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public String writeSignature(String signature, HttpRequest request, HttpParameters requestParameters) {
/* 20 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 22 */     sb.append("OAuth ");
/*    */ 
/*    */     
/* 25 */     if (requestParameters.containsKey("realm")) {
/* 26 */       sb.append(requestParameters.getAsHeaderElement("realm"));
/* 27 */       sb.append(", ");
/*    */     } 
/*    */ 
/*    */     
/* 31 */     HttpParameters oauthParams = requestParameters.getOAuthParameters();
/* 32 */     oauthParams.put("oauth_signature", signature, true);
/*    */     
/* 34 */     Iterator<String> iter = oauthParams.keySet().iterator();
/* 35 */     while (iter.hasNext()) {
/* 36 */       String key = iter.next();
/* 37 */       sb.append(oauthParams.getAsHeaderElement(key));
/* 38 */       if (iter.hasNext()) {
/* 39 */         sb.append(", ");
/*    */       }
/*    */     } 
/*    */     
/* 43 */     String header = sb.toString();
/* 44 */     OAuth.debugOut("Auth Header", header);
/* 45 */     request.setHeader("Authorization", header);
/*    */     
/* 47 */     return header;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\signature\AuthorizationHeaderSigningStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
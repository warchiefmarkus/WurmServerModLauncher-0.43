/*    */ package oauth.signpost.signature;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.security.GeneralSecurityException;
/*    */ import javax.crypto.Mac;
/*    */ import javax.crypto.SecretKey;
/*    */ import javax.crypto.spec.SecretKeySpec;
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
/*    */ 
/*    */ 
/*    */ public class HmacSha1MessageSigner
/*    */   extends OAuthMessageSigner
/*    */ {
/*    */   private static final String MAC_NAME = "HmacSHA1";
/*    */   
/*    */   public String getSignatureMethod() {
/* 36 */     return "HMAC-SHA1";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String sign(HttpRequest request, HttpParameters requestParams) throws OAuthMessageSignerException {
/*    */     try {
/* 43 */       String keyString = OAuth.percentEncode(getConsumerSecret()) + '&' + OAuth.percentEncode(getTokenSecret());
/*    */       
/* 45 */       byte[] keyBytes = keyString.getBytes("UTF-8");
/*    */       
/* 47 */       SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");
/* 48 */       Mac mac = Mac.getInstance("HmacSHA1");
/* 49 */       mac.init(key);
/*    */       
/* 51 */       String sbs = (new SignatureBaseString(request, requestParams)).generate();
/* 52 */       OAuth.debugOut("SBS", sbs);
/* 53 */       byte[] text = sbs.getBytes("UTF-8");
/*    */       
/* 55 */       return base64Encode(mac.doFinal(text)).trim();
/* 56 */     } catch (GeneralSecurityException e) {
/* 57 */       throw new OAuthMessageSignerException(e);
/* 58 */     } catch (UnsupportedEncodingException e) {
/* 59 */       throw new OAuthMessageSignerException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\signature\HmacSha1MessageSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
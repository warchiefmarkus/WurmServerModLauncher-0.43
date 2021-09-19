/*    */ package winterwell.jtwitter.ecosystem;
/*    */ 
/*    */ import winterwell.json.JSONException;
/*    */ import winterwell.json.JSONObject;
/*    */ import winterwell.jtwitter.InternalUtils;
/*    */ import winterwell.jtwitter.Twitter;
/*    */ import winterwell.jtwitter.TwitterException;
/*    */ import winterwell.jtwitter.URLConnectionHttpClient;
/*    */ import winterwell.jtwitter.User;
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
/*    */ public class ThirdParty
/*    */ {
/*    */   private Twitter.IHttpClient client;
/*    */   
/*    */   public ThirdParty() {
/* 30 */     this((Twitter.IHttpClient)new URLConnectionHttpClient());
/*    */   }
/*    */   
/*    */   public ThirdParty(Twitter.IHttpClient client) {
/* 34 */     this.client = client;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getInfochimpTrustRank(User user, String apiKey) {
/* 44 */     String json = this.client.getPage(
/* 45 */         "http://api.infochimps.com/soc/net/tw/trstrank.json", 
/* 46 */         InternalUtils.asMap(new Object[] { "screen_name", user.screenName, "apikey", 
/* 47 */             apiKey }, ), false);
/*    */     try {
/* 49 */       JSONObject results = new JSONObject(json);
/* 50 */       Double score = Double.valueOf(results.getDouble("trstrank"));
/* 51 */       return score.doubleValue();
/* 52 */     } catch (JSONException e) {
/* 53 */       throw new TwitterException.Parsing(json, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\ThirdParty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
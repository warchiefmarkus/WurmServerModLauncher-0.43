/*    */ package winterwell.jtwitter.ecosystem;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Map;
/*    */ import winterwell.json.JSONException;
/*    */ import winterwell.json.JSONObject;
/*    */ import winterwell.jtwitter.InternalUtils;
/*    */ import winterwell.jtwitter.Twitter;
/*    */ import winterwell.jtwitter.TwitterException;
/*    */ import winterwell.jtwitter.URLConnectionHttpClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TwitterCounter
/*    */ {
/*    */   final String apiKey;
/* 20 */   Twitter.IHttpClient client = (Twitter.IHttpClient)new URLConnectionHttpClient();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Twitter.IHttpClient getClient() {
/* 27 */     return this.client;
/*    */   }
/*    */   
/*    */   public TwitterCounter(String twitterCounterApiKey) {
/* 31 */     this.apiKey = twitterCounterApiKey;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TwitterCounterStats getStats(Number twitterUserId) {
/* 37 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "twitter_id", twitterUserId, "apikey", this.apiKey });
/*    */ 
/*    */     
/* 40 */     String json = this.client.getPage("http://api.twittercounter.com/", vars, false);
/*    */     try {
/* 42 */       JSONObject jo = new JSONObject(json);
/* 43 */       return new TwitterCounterStats(jo);
/* 44 */     } catch (JSONException e) {
/* 45 */       throw new TwitterException.Parsing(json, e);
/* 46 */     } catch (ParseException e) {
/* 47 */       throw new TwitterException.Parsing(json, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\TwitterCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
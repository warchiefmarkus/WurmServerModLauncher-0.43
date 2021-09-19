/*    */ package winterwell.jtwitter.ecosystem;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public class PeerIndex
/*    */ {
/*    */   final String API_KEY;
/*    */   Twitter.IHttpClient client;
/*    */   
/*    */   public PeerIndex(String apiKey) {
/* 32 */     this.client = (Twitter.IHttpClient)new URLConnectionHttpClient();
/*    */     this.API_KEY = apiKey;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PeerIndexProfile getProfile(User user) {
/* 41 */     Map vars = InternalUtils.asMap(new Object[] { (user.screenName == null) ? "twitter_screen_name" : "twitter_id", (user.screenName == null) ? user.id : user.screenName, "api_key", this.API_KEY });
/*    */ 
/*    */     
/* 44 */     String json = this.client.getPage("https://api.peerindex.com/1/actor/basic.json", 
/* 45 */         vars, false);
/*    */     try {
/* 47 */       JSONObject jo = new JSONObject(json);
/* 48 */       return new PeerIndexProfile(jo);
/* 49 */     } catch (JSONException e) {
/* 50 */       throw new TwitterException.Parsing(json, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\PeerIndex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
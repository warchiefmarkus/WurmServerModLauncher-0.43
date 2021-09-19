/*    */ package winterwell.jtwitter.ecosystem;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import winterwell.json.JSONArray;
/*    */ import winterwell.json.JSONObject;
/*    */ import winterwell.jtwitter.InternalUtils;
/*    */ import winterwell.jtwitter.Twitter;
/*    */ import winterwell.jtwitter.URLConnectionHttpClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Klout
/*    */ {
/*    */   final String API_KEY;
/*    */   Twitter.IHttpClient client;
/*    */   
/*    */   public Klout(String apiKey) {
/* 25 */     this.client = (Twitter.IHttpClient)new URLConnectionHttpClient();
/*    */     this.API_KEY = apiKey;
/*    */   } public Map<String, Double> getScore(String... userNames) {
/* 28 */     String unames = InternalUtils.join(userNames);
/* 29 */     Map vars = InternalUtils.asMap(new Object[] { "key", this.API_KEY, "users", unames });
/* 30 */     String json = this.client.getPage("http://api.klout.com/1/klout.json", vars, false);
/* 31 */     JSONObject jo = new JSONObject(json);
/* 32 */     JSONArray users = jo.getJSONArray("users");
/* 33 */     Map<String, Double> scores = new HashMap<String, Double>(users.length());
/* 34 */     for (int i = 0, n = users.length(); i < n; i++) {
/* 35 */       JSONObject u = users.getJSONObject(i);
/* 36 */       scores.put(u.getString("twitter_screen_name"), Double.valueOf(u.getDouble("kscore")));
/*    */     } 
/* 38 */     return scores;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\Klout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package winterwell.jtwitter;
/*    */ 
/*    */ import java.util.Map;
/*    */ import winterwell.json.JSONObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Twitter_Analytics
/*    */ {
/*    */   private Twitter.IHttpClient http;
/*    */   
/*    */   Twitter_Analytics(Twitter.IHttpClient http) {
/* 31 */     this.http = http;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getUrlCount(String url) {
/* 41 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "url", url });
/* 42 */     String json = this.http.getPage("http://urls.api.twitter.com/1/urls/count.json", 
/* 43 */         vars, false);
/* 44 */     JSONObject jo = new JSONObject(json);
/* 45 */     return jo.getInt("count");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Twitter_Analytics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
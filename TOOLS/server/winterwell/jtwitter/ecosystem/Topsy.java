/*    */ package winterwell.jtwitter.ecosystem;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Topsy
/*    */ {
/*    */   public static final class UrlInfo
/*    */   {
/*    */     public final String title;
/*    */     public final int linkCount;
/*    */     public final String desc;
/*    */     public final String url;
/*    */     
/*    */     public UrlInfo(JSONObject resp) throws JSONException {
/* 33 */       this.url = resp.getString("url");
/* 34 */       this.title = resp.getString("title");
/* 35 */       this.linkCount = resp.getInt("trackback_total");
/* 36 */       this.desc = resp.getString("description");
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 41 */       return String.valueOf(this.url) + " " + this.linkCount + " " + this.title;
/*    */     }
/*    */   }
/*    */   
/* 45 */   private Twitter.IHttpClient client = (Twitter.IHttpClient)new URLConnectionHttpClient();
/*    */ 
/*    */   
/*    */   private String apikey;
/*    */ 
/*    */   
/*    */   public Topsy() {}
/*    */ 
/*    */   
/*    */   public Topsy(String apiKey) {
/* 55 */     this.apikey = apiKey;
/*    */   }
/*    */   
/*    */   public UrlInfo getUrlInfo(String url) {
/* 59 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "url", url });
/* 60 */     if (this.apikey != null) vars.put("apikey", this.apikey); 
/* 61 */     String json = this.client.getPage("http://otter.topsy.com/urlinfo.json", vars, false);
/*    */     try {
/* 63 */       JSONObject jo = new JSONObject(json);
/* 64 */       JSONObject resp = jo.getJSONObject("response");
/* 65 */       return new UrlInfo(resp);
/* 66 */     } catch (JSONException e) {
/* 67 */       throw new TwitterException.Parsing(json, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\Topsy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
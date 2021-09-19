/*    */ package winterwell.jtwitter.ecosystem;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import winterwell.json.JSONArray;
/*    */ import winterwell.json.JSONException;
/*    */ import winterwell.json.JSONObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PeerIndexProfile
/*    */ {
/*    */   public final int peerIndex;
/*    */   public final int authority;
/*    */   public final String twitterScreenName;
/*    */   public final List<String> topics;
/*    */   public final int audience;
/*    */   public final int activity;
/*    */   public final String slug;
/*    */   public final String url;
/*    */   public final String name;
/*    */   
/*    */   public String toString() {
/* 29 */     return "PeerIndexProfile[" + this.twitterScreenName + " " + this.peerIndex + " " + 
/* 30 */       this.authority + ":" + this.audience + ":" + this.activity + " " + this.topics + "]";
/*    */   }
/*    */   
/*    */   PeerIndexProfile(JSONObject jo) throws JSONException {
/* 34 */     this.peerIndex = jo.getInt("peerindex");
/* 35 */     this.authority = jo.getInt("authority");
/* 36 */     this.audience = jo.getInt("audience");
/* 37 */     this.activity = jo.getInt("activity");
/* 38 */     this.twitterScreenName = jo.getString("twitter");
/* 39 */     this.topics = new ArrayList<String>();
/* 40 */     JSONArray _topics = jo.getJSONArray("topics");
/* 41 */     for (int i = 0; i < _topics.length(); i++) {
/* 42 */       this.topics.add((String)_topics.get(i));
/*    */     }
/* 44 */     this.slug = jo.getString("slug");
/* 45 */     this.url = jo.getString("url");
/* 46 */     this.name = jo.optString("name");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\PeerIndexProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
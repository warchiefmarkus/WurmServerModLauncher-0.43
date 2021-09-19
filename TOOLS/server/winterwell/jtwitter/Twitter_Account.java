/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import winterwell.json.JSONArray;
/*     */ import winterwell.json.JSONException;
/*     */ import winterwell.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Twitter_Account
/*     */ {
/*     */   public enum KAccessLevel
/*     */   {
/*  25 */     NONE,
/*     */     
/*  27 */     READ_ONLY,
/*     */     
/*  29 */     READ_WRITE,
/*     */     
/*  31 */     READ_WRITE_DM;
/*     */   }
/*     */   
/*     */   public static class Search
/*     */   {
/*     */     private Date createdAt;
/*     */     private Long id;
/*     */     private String query;
/*     */     
/*     */     public Search(Long id, Date createdAt, String query) {
/*  41 */       this.id = id;
/*  42 */       this.createdAt = createdAt;
/*  43 */       this.query = query;
/*     */     }
/*     */     
/*     */     public Date getCreatedAt() {
/*  47 */       return this.createdAt;
/*     */     }
/*     */     
/*     */     public Long getId() {
/*  51 */       return this.id;
/*     */     }
/*     */     
/*     */     public String getText() {
/*  55 */       return this.query;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  60 */   public static String COLOR_BG = "profile_background_color";
/*     */   
/*  62 */   public static String COLOR_LINK = "profile_link_color";
/*     */   
/*  64 */   public static String COLOR_SIDEBAR_BORDER = "profile_sidebar_border_color";
/*  65 */   public static String COLOR_SIDEBAR_FILL = "profile_sidebar_fill_color";
/*  66 */   public static String COLOR_TEXT = "profile_text_color";
/*     */   private KAccessLevel accessLevel;
/*     */   final Twitter jtwit;
/*     */   
/*     */   public Twitter_Account(Twitter jtwit) {
/*  71 */     assert jtwit.getHttpClient().canAuthenticate() : jtwit;
/*  72 */     this.jtwit = jtwit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, RateLimit> getRateLimits() {
/*  82 */     return ((URLConnectionHttpClient)this.jtwit.getHttpClient()).updateRateLimits();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Search createSavedSearch(String query) {
/*  94 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "saved_searches/create.json";
/*  95 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "query", query });
/*  96 */     String json = this.jtwit.getHttpClient().post(url, vars, true);
/*     */     try {
/*  98 */       return makeSearch(new JSONObject(json));
/*  99 */     } catch (JSONException e) {
/* 100 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Search destroySavedSearch(Long id) {
/* 112 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "saved_searches/destroy/" + id + 
/* 113 */       ".json";
/* 114 */     String json = this.jtwit.getHttpClient().post(url, null, true);
/*     */     try {
/* 116 */       return makeSearch(new JSONObject(json));
/* 117 */     } catch (JSONException e) {
/* 118 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KAccessLevel getAccessLevel() {
/* 129 */     if (this.accessLevel != null)
/* 130 */       return this.accessLevel; 
/*     */     try {
/* 132 */       verifyCredentials();
/* 133 */       return this.accessLevel;
/* 134 */     } catch (E401 e) {
/* 135 */       return KAccessLevel.NONE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Search> getSavedSearches() {
/* 144 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "saved_searches.json";
/* 145 */     String json = this.jtwit.getHttpClient().getPage(url, null, true);
/*     */     try {
/* 147 */       JSONArray ja = new JSONArray(json);
/* 148 */       List<Search> searches = new ArrayList<Search>();
/* 149 */       for (int i = 0; i < ja.length(); i++) {
/* 150 */         JSONObject jo = ja.getJSONObject(i);
/* 151 */         Search search = makeSearch(jo);
/* 152 */         searches.add(search);
/*     */       } 
/* 154 */       return searches;
/* 155 */     } catch (JSONException e) {
/* 156 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Search makeSearch(JSONObject jo) throws JSONException {
/* 169 */     Date createdAt = InternalUtils.parseDate(jo
/* 170 */         .getString("created_at"));
/* 171 */     Long id = Long.valueOf(jo.getLong("id"));
/* 172 */     String query = jo.getString("query");
/* 173 */     Search search = new Search(id, createdAt, query);
/* 174 */     return search;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User setProfile(String name, String url, String location, String description) {
/* 199 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "name", name, "url", url, "location", location, "description", description });
/* 200 */     String apiUrl = String.valueOf(this.jtwit.TWITTER_URL) + "/account/update_profile.json";
/* 201 */     String json = this.jtwit.getHttpClient().post(apiUrl, vars, true);
/* 202 */     return InternalUtils.user(json);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User setProfileColors(Map<String, String> colorName2hexCode) {
/* 215 */     assert colorName2hexCode.size() != 0;
/* 216 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/account/update_profile_colors.json";
/* 217 */     String json = this.jtwit.getHttpClient().post(url, colorName2hexCode, true);
/* 218 */     return InternalUtils.user(json);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 223 */     return "TwitterAccount[" + this.jtwit.getScreenName() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User verifyCredentials() throws TwitterException.E401 {
/* 238 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/account/verify_credentials.json";
/* 239 */     String json = this.jtwit.getHttpClient().getPage(url, null, true);
/*     */     
/* 241 */     Twitter.IHttpClient client = this.jtwit.getHttpClient();
/* 242 */     String al = client.getHeader("X-Access-Level");
/* 243 */     if (al != null) {
/* 244 */       if ("read".equals(al)) {
/* 245 */         this.accessLevel = KAccessLevel.READ_ONLY;
/*     */       }
/* 247 */       if ("read-write".equals(al)) {
/* 248 */         this.accessLevel = KAccessLevel.READ_WRITE;
/*     */       }
/* 250 */       if ("read-write-directmessages".equals(al)) {
/* 251 */         this.accessLevel = KAccessLevel.READ_WRITE_DM;
/*     */       }
/*     */     } 
/* 254 */     User self = InternalUtils.user(json);
/*     */     
/* 256 */     this.jtwit.self = self;
/* 257 */     return self;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Twitter_Account.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
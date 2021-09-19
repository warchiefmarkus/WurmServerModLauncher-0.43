/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.util.Date;
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
/*     */ public final class RateLimit
/*     */ {
/*     */   public static final String RES_STREAM_USER = "/stream/user";
/*     */   public static final String RES_STREAM_KEYWORD = "/stream/keyword";
/*     */   public static final String RES_USERS_BULK_SHOW = "/users/lookup";
/*     */   public static final String RES_USERS_SHOW1 = "/users/show";
/*     */   public static final String RES_USER_TIMELINE = "/statuses/user_timeline";
/*     */   public static final String RES_SEARCH = "/search/tweets";
/*     */   public static final String RES_STATUS_SHOW = "/statuses/show";
/*     */   public static final String RES_USERS_SEARCH = "/users/search";
/*     */   public static final String RES_FRIENDSHIPS_SHOW = "/friendships/show";
/*     */   public static final String RES_TRENDS = "/trends/place";
/*     */   public static final String RES_LISTS_SHOW = "/lists/show";
/*     */   private String limit;
/*     */   private String remaining;
/*     */   private String reset;
/*     */   
/*     */   public RateLimit(String limit, String remaining, String reset) {
/*  44 */     this.limit = limit;
/*  45 */     this.remaining = remaining;
/*  46 */     this.reset = reset;
/*     */   }
/*     */   
/*     */   RateLimit(JSONObject jrl) {
/*  50 */     this(jrl.getString("limit"), jrl.getString("remaining"), jrl.getString("reset"));
/*     */   }
/*     */   
/*     */   public int getLimit() {
/*  54 */     return Integer.valueOf(this.limit).intValue();
/*     */   }
/*     */   
/*     */   public int getRemaining() {
/*  58 */     return Integer.valueOf(this.remaining).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getReset() {
/*  65 */     return InternalUtils.parseDate(this.reset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOutOfDate() {
/*  73 */     return (getReset().getTime() < System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  78 */     return this.remaining;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void waitForReset() {
/*  87 */     Long r = Long.valueOf(this.reset);
/*  88 */     long now = System.currentTimeMillis();
/*  89 */     long wait = r.longValue() - now;
/*  90 */     if (wait < 0L)
/*     */       return; 
/*     */     try {
/*  93 */       Thread.sleep(wait);
/*  94 */     } catch (InterruptedException e) {
/*     */       
/*  96 */       throw new TwitterException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getResource(String url) {
/* 107 */     if (!url.startsWith("https://api.twitter.com/1.1")) {
/* 108 */       return null;
/*     */     }
/* 110 */     int s = "https://api.twitter.com/1.1".length();
/* 111 */     int e = url.indexOf(".json", s);
/* 112 */     if (e == -1) return null; 
/* 113 */     int e1 = url.indexOf("/", s + 1);
/* 114 */     if (e1 == -1 || e1 > e) {
/* 115 */       return url.substring(s, e);
/*     */     }
/* 117 */     int e2 = url.indexOf("/", e1 + 1);
/* 118 */     if (e2 == -1 || e2 > e) {
/* 119 */       return url.substring(s, e);
/*     */     }
/* 121 */     return url.substring(s, e2);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\RateLimit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
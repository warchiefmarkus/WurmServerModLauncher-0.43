/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.net.HttpURLConnection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TwitterStream
/*     */   extends AStream
/*     */ {
/*     */   public enum KMethod
/*     */   {
/*  21 */     filter,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  26 */     firehose,
/*     */ 
/*     */     
/*  29 */     links,
/*     */ 
/*     */     
/*  32 */     retweet,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  40 */     sample;
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
/*  54 */   public static int MAX_KEYWORDS = 400;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MAX_KEYWORD_LENGTH = 60;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MAX_USERS = 5000;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   static Map<String, AStream> user2stream = new ConcurrentHashMap<String, AStream>();
/*     */   
/*     */   private List<Long> follow;
/*     */   
/*     */   private List<double[]> locns;
/*     */   
/*  77 */   KMethod method = KMethod.sample;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> track;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TwitterStream(Twitter jtwit) {
/*  88 */     super(jtwit);
/*     */   }
/*     */ 
/*     */   
/*     */   HttpURLConnection connect2() throws Exception {
/*  93 */     connect3_rateLimit();
/*     */     
/*  95 */     String url = "https://stream.twitter.com/1.1/statuses/" + this.method + ".json";
/*  96 */     Map<String, String> vars = new HashMap<String, String>();
/*  97 */     if (this.follow != null && this.follow.size() != 0) {
/*  98 */       vars.put("follow", InternalUtils.join(this.follow, 0, 2147483647));
/*     */     }
/* 100 */     if (this.track != null && this.track.size() != 0) {
/* 101 */       vars.put("track", InternalUtils.join(this.track, 0, 2147483647));
/*     */     }
/*     */     
/* 104 */     if (vars.isEmpty() && this.method == KMethod.filter) {
/* 105 */       throw new IllegalStateException("No filters set for " + this);
/*     */     }
/* 107 */     vars.put("delimited", "length");
/*     */     
/* 109 */     HttpURLConnection con = this.client.post2_connect(url, vars);
/* 110 */     return con;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void connect3_rateLimit() {
/* 118 */     if (this.jtwit.getScreenName() == null)
/*     */       return; 
/* 120 */     AStream s = user2stream.get(this.jtwit.getScreenName());
/* 121 */     if (s != null && s.isConnected()) {
/* 122 */       throw new TwitterException.TooManyLogins(
/* 123 */           "One account, one stream (running: " + 
/* 124 */           s + 
/* 125 */           "; trying to run" + 
/* 126 */           this + 
/* 127 */           ").\n\tBut streams OR their filter parameters, so one stream can do a lot.");
/*     */     }
/*     */     
/* 130 */     if (user2stream.size() > 500)
/*     */     {
/* 132 */       user2stream = new ConcurrentHashMap<String, AStream>();
/*     */     }
/* 134 */     user2stream.put(this.jtwit.getScreenName(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   void fillInOutages2(Twitter jtwit2, AStream.Outage outage) {
/* 139 */     if (this.method != KMethod.filter) {
/* 140 */       throw new UnsupportedOperationException();
/*     */     }
/* 142 */     if (this.track != null) {
/* 143 */       for (String keyword : this.track) {
/* 144 */         List<Status> msgs = this.jtwit.search(keyword);
/* 145 */         for (Status status : msgs) {
/* 146 */           if (this.tweets.contains(status)) {
/*     */             continue;
/*     */           }
/* 149 */           this.tweets.add(status);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 155 */     if (this.follow != null) {
/* 156 */       for (Long user : this.follow) {
/* 157 */         List<Status> msgs = this.jtwit.getUserTimeline(user);
/* 158 */         for (Status status : msgs) {
/* 159 */           if (this.tweets.contains(status)) {
/*     */             continue;
/*     */           }
/* 162 */           this.tweets.add(status);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 167 */     if (this.locns != null && !this.locns.isEmpty()) {
/* 168 */       throw new UnsupportedOperationException("TODO");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getTrackKeywords() {
/* 175 */     return this.track;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFollowUsers(List<Long> userIds) throws IllegalArgumentException {
/* 183 */     this.method = KMethod.filter;
/* 184 */     if (userIds != null && userIds.size() > 5000) {
/* 185 */       throw new IllegalArgumentException("Track upto 5000 users - not " + userIds.size());
/*     */     }
/* 187 */     this.follow = userIds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Long> getFollowUsers() {
/* 194 */     return this.follow;
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
/*     */   @Deprecated
/*     */   public void setLocation(List<double[]> boundingBoxes) {
/* 211 */     this.method = KMethod.filter;
/* 212 */     this.locns = boundingBoxes;
/* 213 */     throw new RuntimeException("TODO! Not implemented yet (sorry)");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setMethod(KMethod method) {
/* 223 */     this.method = method;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrackKeywords(List<String> keywords) {
/* 251 */     if (keywords.size() > MAX_KEYWORDS) {
/* 252 */       throw new IllegalArgumentException("Too many tracked terms: " + keywords.size() + " (" + MAX_KEYWORDS + " limit)");
/*     */     }
/*     */     
/* 255 */     for (String kw : keywords) {
/* 256 */       if (kw.length() > 60) {
/* 257 */         throw new IllegalArgumentException("Track term too long: " + kw + " (60 char limit)");
/*     */       }
/*     */     } 
/*     */     
/* 261 */     this.track = keywords;
/* 262 */     this.method = KMethod.filter;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 267 */     StringBuilder sb = new StringBuilder("TwitterStream");
/* 268 */     sb.append("[" + this.method);
/* 269 */     if (this.track != null) {
/* 270 */       sb.append(" track:" + InternalUtils.join(this.track, 0, 5));
/*     */     }
/* 272 */     if (this.follow != null && this.follow.size() > 0) {
/* 273 */       sb.append(" follow:" + InternalUtils.join(this.follow, 0, 5));
/*     */     }
/* 275 */     if (this.locns != null) {
/* 276 */       sb.append(" in:" + InternalUtils.join(this.locns, 0, 5));
/*     */     }
/* 278 */     sb.append(" by:" + this.jtwit.getScreenNameIfKnown());
/* 279 */     sb.append("]");
/* 280 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setListenersOnly(boolean listenersOnly) {
/* 290 */     this.listenersOnly = listenersOnly;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\TwitterStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
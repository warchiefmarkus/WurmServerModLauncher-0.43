/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserStream
/*     */   extends AStream
/*     */ {
/*     */   boolean withFollowings;
/*     */   
/*     */   public UserStream(Twitter jtwit) {
/*  55 */     super(jtwit);
/*     */   }
/*     */ 
/*     */   
/*     */   HttpURLConnection connect2() throws IOException {
/*  60 */     connect3_rateLimit();
/*     */     
/*  62 */     String url = "https://userstream.twitter.com/2/user.json?delimited=length";
/*     */     
/*  64 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "with", this.withFollowings ? "followings" : "user" });
/*  65 */     HttpURLConnection con = this.client.connect(url, vars, true);
/*  66 */     return con;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void connect3_rateLimit() {
/*  74 */     if (this.jtwit.getScreenName() == null)
/*     */       return; 
/*  76 */     AStream s = user2stream.get(this.jtwit.getScreenName());
/*  77 */     if (s != null && s.isConnected()) {
/*  78 */       throw new TwitterException.TooManyLogins("One account, one UserStream");
/*     */     }
/*     */     
/*  81 */     if (user2stream.size() > 500)
/*     */     {
/*  83 */       user2stream = new ConcurrentHashMap<String, AStream>();
/*     */     }
/*  85 */     user2stream.put(this.jtwit.getScreenName(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   static Map<String, AStream> user2stream = new ConcurrentHashMap<String, AStream>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void fillInOutages2(Twitter jtwit2, AStream.Outage outage) throws UnsupportedOperationException, TwitterException {
/* 101 */     if (this.withFollowings)
/*     */     {
/* 103 */       throw new UnsupportedOperationException("TODO");
/*     */     }
/* 105 */     List<Status> mentions = jtwit2.getMentions();
/* 106 */     for (Status status : mentions) {
/* 107 */       if (this.tweets.contains(status)) {
/*     */         continue;
/*     */       }
/* 110 */       this.tweets.add(status);
/*     */     } 
/*     */     
/* 113 */     List<Status> updates = jtwit2.getUserTimeline(jtwit2.getScreenName());
/* 114 */     for (Status status : updates) {
/* 115 */       if (this.tweets.contains(status)) {
/*     */         continue;
/*     */       }
/* 118 */       this.tweets.add(status);
/*     */     } 
/* 120 */     List<Message> dms = jtwit2.getDirectMessages();
/* 121 */     for (Twitter.ITweet dm : dms) {
/* 122 */       if (this.tweets.contains(dm)) {
/*     */         continue;
/*     */       }
/* 125 */       this.tweets.add(dm);
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
/*     */   public Collection<Long> getFriends() {
/* 137 */     return this.friends;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWithFollowings(boolean withFollowings) {
/* 145 */     assert !isConnected();
/* 146 */     this.withFollowings = withFollowings;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     StringBuilder sb = new StringBuilder("UserStream");
/* 152 */     sb.append("[" + this.jtwit.getScreenNameIfKnown());
/* 153 */     if (this.withFollowings) sb.append(" +followings"); 
/* 154 */     sb.append("]");
/* 155 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\UserStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
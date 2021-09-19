/*     */ package winterwell.jtwitter.ecosystem;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import winterwell.jtwitter.InternalUtils;
/*     */ import winterwell.jtwitter.Status;
/*     */ import winterwell.jtwitter.Twitter;
/*     */ import winterwell.jtwitter.TwitterException;
/*     */ import winterwell.jtwitter.URLConnectionHttpClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TwitLonger
/*     */ {
/*     */   Twitter.IHttpClient http;
/*     */   private Twitter jtwit;
/*     */   private String twitlongerApiKey;
/*     */   private String twitlongerAppName;
/*     */   
/*     */   public TwitLonger() {
/*  24 */     this.http = (Twitter.IHttpClient)new URLConnectionHttpClient();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TwitLonger(Twitter jtwitter, String twitlongerApiKey, String twitlongerAppName) {
/*  34 */     this.twitlongerApiKey = twitlongerApiKey;
/*  35 */     this.twitlongerAppName = twitlongerAppName;
/*  36 */     this.http = jtwitter.getHttpClient();
/*  37 */     this.jtwit = jtwitter;
/*  38 */     if (twitlongerApiKey == null || twitlongerAppName == null) {
/*  39 */       throw new IllegalStateException("Incomplete Twitlonger api details");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Status updateLongStatus(String message, Number inReplyToStatusId) {
/*  59 */     assert this.twitlongerApiKey != null : "Wrong constructor used -- you must supply an api-key to post";
/*  60 */     if (message.length() < 141) {
/*  61 */       throw new IllegalArgumentException("Message too short (" + 
/*  62 */           inReplyToStatusId + 
/*  63 */           " chars). Just post a normal Twitter status. ");
/*     */     }
/*  65 */     String url = "http://www.twitlonger.com/api_post";
/*     */ 
/*     */ 
/*     */     
/*  69 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "application", this.twitlongerAppName, "api_key", this.twitlongerApiKey, "username", this.jtwit.getScreenName(), "message", message });
/*  70 */     if (inReplyToStatusId != null && inReplyToStatusId.doubleValue() != 0.0D) {
/*  71 */       vars.put("in_reply", inReplyToStatusId.toString());
/*     */     }
/*     */     
/*  74 */     String response = this.http.post(url, vars, false);
/*  75 */     Matcher m = contentTag.matcher(response);
/*  76 */     boolean ok = m.find();
/*  77 */     if (!ok) {
/*  78 */       throw new TwitterException.TwitLongerException(
/*  79 */           "TwitLonger call failed", response);
/*     */     }
/*  81 */     String shortMsg = m.group(1).trim();
/*     */ 
/*     */     
/*  84 */     Status s = this.jtwit.updateStatus(shortMsg, inReplyToStatusId);
/*     */     
/*  86 */     m = idTag.matcher(response);
/*  87 */     ok = m.find();
/*  88 */     if (!ok)
/*     */     {
/*  90 */       return s;
/*     */     }
/*  92 */     String id = m.group(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 100 */       url = "http://www.twitlonger.com/api_set_id";
/* 101 */       vars.remove("message");
/* 102 */       vars.remove("in_reply");
/* 103 */       vars.remove("username");
/* 104 */       vars.put("message_id", id);
/* 105 */       vars.put("twitter_id", s.getId());
/* 106 */       this.http.post(url, vars, false);
/* 107 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   static final Pattern contentTag = Pattern.compile(
/* 120 */       "<content>(.+?)<\\/content>", 32);
/*     */   
/* 122 */   static final Pattern idTag = Pattern.compile("<id>(.+?)<\\/id>", 
/* 123 */       32);
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
/*     */   public String getLongStatus(Status truncatedStatus) {
/* 136 */     int i = truncatedStatus.text.indexOf("http://tl.gd/");
/* 137 */     if (i == -1)
/* 138 */       return truncatedStatus.text; 
/* 139 */     String id = truncatedStatus.text.substring(i + 13).trim();
/* 140 */     String response = this.http.getPage("http://www.twitlonger.com/api_read/" + 
/* 141 */         id, null, false);
/* 142 */     Matcher m = contentTag.matcher(response);
/* 143 */     boolean ok = m.find();
/* 144 */     if (!ok)
/* 145 */       throw new TwitterException.TwitLongerException(
/* 146 */           "TwitLonger call failed", response); 
/* 147 */     String longMsg = m.group(1).trim();
/* 148 */     return longMsg;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\ecosystem\TwitLonger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
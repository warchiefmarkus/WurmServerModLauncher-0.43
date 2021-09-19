/*      */ package winterwell.jtwitter;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigInteger;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Matcher;
/*      */ import winterwell.json.JSONArray;
/*      */ import winterwell.json.JSONException;
/*      */ import winterwell.json.JSONObject;
/*      */ import winterwell.jtwitter.ecosystem.TwitLonger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Twitter
/*      */   implements Serializable
/*      */ {
/*      */   public static boolean CASE_SENSITIVE_SCREENNAMES;
/*      */   
/*      */   public static Map<String, Double> getAPIStatus() throws Exception {
/*  118 */     HashMap<String, Double> map = new HashMap<String, Double>();
/*      */ 
/*      */     
/*  121 */     String json = null;
/*      */     try {
/*  123 */       URLConnectionHttpClient client = new URLConnectionHttpClient();
/*  124 */       json = client.getPage("https://api.io.watchmouse.com/synth/current/39657/folder/7617/?fields=info;cur;24h.uptime", null, false);
/*  125 */       JSONObject jobj = new JSONObject(json);
/*  126 */       JSONArray jarr = jobj.getJSONArray("result");
/*  127 */       for (int i = 0; i < jarr.length(); i++) {
/*  128 */         JSONObject jo = jarr.getJSONObject(i);
/*  129 */         String name = jo.getJSONObject("info").getString("name");
/*  130 */         JSONObject h24 = jo.getJSONObject("24h");
/*  131 */         double value = h24.getDouble("uptime");
/*  132 */         map.put(name, Double.valueOf(value));
/*      */       } 
/*  134 */       return map;
/*  135 */     } catch (JSONException e) {
/*  136 */       throw new TwitterException.Parsing(json, e);
/*  137 */     } catch (Exception e) {
/*  138 */       return map;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface ICallback
/*      */   {
/*      */     boolean process(List<Status> param1List);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface IHttpClient
/*      */   {
/*      */     boolean canAuthenticate();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     HttpURLConnection connect(String param1String, Map<String, String> param1Map, boolean param1Boolean) throws IOException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     IHttpClient copy();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getHeader(String param1String);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getPage(String param1String, Map<String, String> param1Map, boolean param1Boolean) throws TwitterException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     RateLimit getRateLimit(Twitter.KRequestType param1KRequestType);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Map<String, RateLimit> getRateLimits();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String post(String param1String, Map<String, String> param1Map, boolean param1Boolean) throws TwitterException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     HttpURLConnection post2_connect(String param1String, Map<String, String> param1Map) throws Exception;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setTimeout(int param1Int);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isRetryOnError();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setRetryOnError(boolean param1Boolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface ITweet
/*      */     extends Serializable
/*      */   {
/*      */     Date getCreatedAt();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     BigInteger getId();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getLocation();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     List<String> getMentions();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Place getPlace();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getText();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     List<Twitter.TweetEntity> getTweetEntities(Twitter.KEntityType param1KEntityType);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     User getUser();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getDisplayText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum KEntityType
/*      */   {
/*  361 */     hashtags, urls, user_mentions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum KRequestType
/*      */   {
/*  371 */     NORMAL("/statuses/user_timeline"),
/*  372 */     SEARCH("/search/tweets"),
/*  373 */     SEARCH_USERS(
/*  374 */       "/users/search"),
/*  375 */     SHOW_USER("/users/show"),
/*  376 */     UPLOAD_MEDIA("Media"),
/*  377 */     STREAM_KEYWORD(""),
/*  378 */     STREAM_USER("");
/*      */ 
/*      */     
/*      */     final String rateLimit;
/*      */ 
/*      */ 
/*      */     
/*      */     KRequestType(String rateLimit) {
/*  386 */       this.rateLimit = rateLimit;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class TweetEntity
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     final String display;
/*      */     
/*      */     public final int end;
/*      */     
/*      */     public final int start;
/*      */     
/*      */     private final Twitter.ITweet tweet;
/*      */     
/*      */     public final Twitter.KEntityType type;
/*      */ 
/*      */     
/*      */     static ArrayList<TweetEntity> parse(Twitter.ITweet tweet, String rawText, Twitter.KEntityType type, JSONObject jsonEntities) throws JSONException {
/*  410 */       assert type != null && tweet != null && rawText != null && jsonEntities != null : 
/*  411 */         tweet + "\t" + rawText + "\t" + type + "\t" + jsonEntities;
/*      */       try {
/*  413 */         JSONArray arr = jsonEntities.optJSONArray(type.toString());
/*      */         
/*  415 */         if (arr == null || arr.length() == 0) {
/*  416 */           return null;
/*      */         }
/*  418 */         ArrayList<TweetEntity> list = new ArrayList<TweetEntity>(
/*  419 */             arr.length());
/*  420 */         for (int i = 0; i < arr.length(); i++) {
/*  421 */           JSONObject obj = arr.getJSONObject(i);
/*  422 */           TweetEntity te = new TweetEntity(tweet, rawText, type, obj, list);
/*  423 */           list.add(te);
/*      */         } 
/*  425 */         return list;
/*  426 */       } catch (Throwable e) {
/*      */         
/*  428 */         return null;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TweetEntity(Twitter.ITweet tweet, String rawText, Twitter.KEntityType type, JSONObject obj, ArrayList<TweetEntity> previous) throws JSONException {
/*      */       Object eu;
/*  457 */       this.tweet = tweet;
/*  458 */       this.type = type;
/*  459 */       switch (type) {
/*      */         case urls:
/*  461 */           eu = obj.opt("expanded_url");
/*  462 */           this.display = JSONObject.NULL.equals(eu) ? null : (String)eu;
/*      */           break;
/*      */         case user_mentions:
/*  465 */           this.display = obj.getString("name");
/*      */           break;
/*      */         default:
/*  468 */           this.display = null;
/*      */           break;
/*      */       } 
/*      */       
/*  472 */       JSONArray indices = obj.getJSONArray("indices");
/*  473 */       int _start = indices.getInt(0);
/*  474 */       int _end = indices.getInt(1);
/*  475 */       assert _start >= 0 && _end >= _start : obj;
/*      */       
/*  477 */       String text = tweet.getText();
/*  478 */       if (rawText.regionMatches(_start, text, _start, _end - _start)) {
/*      */         
/*  480 */         this.start = _start; this.end = _end;
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  489 */       _end = Math.min(_end, rawText.length());
/*  490 */       _start = Math.min(_start, _end);
/*  491 */       if (_start == _end) {
/*      */         Matcher m;
/*  493 */         switch (type) {
/*      */ 
/*      */           
/*      */           case urls:
/*  497 */             m = Regex.VALID_URL.matcher(text);
/*  498 */             if (m.find()) {
/*  499 */               this.start = m.start();
/*  500 */               this.end = m.end();
/*      */               return;
/*      */             } 
/*      */             break;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  508 */         this.end = Math.min(_end, text.length());
/*  509 */         this.start = Math.min(_start, this.end);
/*      */         
/*      */         return;
/*      */       } 
/*  513 */       String entityText = rawText.substring(_start, _end);
/*      */       
/*  515 */       int from = 0;
/*  516 */       for (TweetEntity prev : previous) {
/*  517 */         if (tweet.getText().regionMatches(prev.start, entityText, 0, entityText.length())) {
/*  518 */           from = prev.end;
/*      */         }
/*      */       } 
/*      */       
/*  522 */       int i = text.indexOf(entityText, from);
/*  523 */       if (i == -1) {
/*      */         
/*  525 */         entityText = InternalUtils.unencode(entityText);
/*  526 */         i = text.indexOf(entityText);
/*  527 */         if (i == -1) i = _start; 
/*      */       } 
/*  529 */       this.start = i;
/*  530 */       this.end = this.start + _end - _start;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TweetEntity(Twitter.ITweet tweet, Twitter.KEntityType type, int start, int end, String display) {
/*  537 */       this.tweet = tweet;
/*  538 */       this.end = end;
/*  539 */       this.start = start;
/*  540 */       this.type = type;
/*  541 */       this.display = display;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String displayVersion() {
/*  549 */       return (this.display == null) ? toString() : this.display;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  562 */       String text = this.tweet.getText();
/*  563 */       int e = Math.min(this.end, text.length());
/*  564 */       int s = Math.min(this.start, e);
/*  565 */       return text.substring(s, e);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean CHECK_TWEET_LENGTH = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  599 */   public static int LINK_LENGTH = 22;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  607 */   public static int MEDIA_LENGTH = 23;
/*      */   
/*  609 */   public static long PHOTO_SIZE_LIMIT = 3145728L;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String SEARCH_MIXED = "mixed";
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String SEARCH_POPULAR = "popular";
/*      */ 
/*      */   
/*      */   public static final String SEARCH_RECENT = "recent";
/*      */ 
/*      */   
/*      */   private static final long serialVersionUID = 1L;
/*      */ 
/*      */   
/*      */   public static final String version = "2.8.7";
/*      */ 
/*      */   
/*      */   public static final int MAX_CHARS = 140;
/*      */ 
/*      */   
/*      */   static final String API_VERSION = "1.1";
/*      */ 
/*      */   
/*      */   static final String DEFAULT_TWITTER_URL = "https://api.twitter.com/1.1";
/*      */ 
/*      */   
/*      */   public static boolean WORRIED_ABOUT_TWITTER = false;
/*      */ 
/*      */   
/*      */   Integer count;
/*      */ 
/*      */   
/*      */   private String geocode;
/*      */ 
/*      */   
/*      */   final IHttpClient http;
/*      */ 
/*      */ 
/*      */   
/*      */   public static User getUser(String screenName, List<User> users) {
/*  652 */     assert screenName != null && users != null;
/*  653 */     for (User user : users) {
/*  654 */       if (screenName.equals(user.screenName))
/*  655 */         return user; 
/*      */     } 
/*  657 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/*  670 */     if (args.length == 3) {
/*  671 */       Twitter tw = new Twitter(args[0], args[1]);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  678 */       Status s = tw.setStatus(args[2]);
/*  679 */       System.out.println(s);
/*      */       return;
/*      */     } 
/*  682 */     System.out.println("Java interface for Twitter");
/*  683 */     System.out.println("--------------------------");
/*  684 */     System.out.println("Version 2.8.7");
/*  685 */     System.out.println("Released under LGPL by Winterwell Associates Ltd.");
/*  686 */     System.out
/*  687 */       .println("See source code, JavaDoc, or http://winterwell.com for details on how to use.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean includeRTs = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String lang;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  710 */   private int maxResults = -1;
/*      */ 
/*      */   
/*      */   private double[] myLatLong;
/*      */ 
/*      */   
/*      */   private String name;
/*      */ 
/*      */   
/*      */   private String resultType;
/*      */ 
/*      */   
/*      */   User self;
/*      */ 
/*      */   
/*      */   private Date sinceDate;
/*      */ 
/*      */   
/*      */   private Number sinceId;
/*      */ 
/*      */   
/*  731 */   private String sourceApp = "jtwitterlib";
/*      */ 
/*      */ 
/*      */   
/*      */   boolean tweetEntities = true;
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   private transient String twitlongerApiKey;
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   private transient String twitlongerAppName;
/*      */ 
/*      */   
/*  748 */   String TWITTER_URL = "https://api.twitter.com/1.1";
/*      */ 
/*      */   
/*      */   private Date untilDate;
/*      */ 
/*      */   
/*      */   private BigInteger untilId;
/*      */ 
/*      */   
/*      */   private Long placeId;
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMyPlace(Long placeId) {
/*  762 */     this.placeId = placeId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Twitter() {
/*  772 */     this((String)null, new URLConnectionHttpClient());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Twitter(String name, IHttpClient client) {
/*  784 */     this.name = name;
/*  785 */     this.http = client;
/*  786 */     assert client != null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Twitter(String screenName, String password) {
/*  804 */     this(screenName, new URLConnectionHttpClient(screenName, password));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Twitter(Twitter jtwit) {
/*  814 */     this(jtwit.getScreenName(), jtwit.http.copy());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Twitter_Account account() {
/*  821 */     return new Twitter_Account(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Twitter_Analytics analytics() {
/*  828 */     return new Twitter_Analytics(this.http);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Map<String, String> addStandardishParameters(Map<String, String> vars) {
/*  840 */     if (this.sinceId != null) {
/*  841 */       vars.put("since_id", this.sinceId.toString());
/*      */     }
/*  843 */     if (this.untilId != null) {
/*  844 */       vars.put("max_id", this.untilId.toString());
/*      */     }
/*  846 */     if (this.count != null) {
/*  847 */       vars.put("count", this.count.toString());
/*      */     }
/*  849 */     if (this.tweetEntities) {
/*  850 */       vars.put("include_entities", "1");
/*      */     } else {
/*  852 */       vars.put("include_entities", "0");
/*      */     } 
/*  854 */     if (!this.includeRTs) {
/*  855 */       vars.put("include_rts", "0");
/*      */     }
/*  857 */     return vars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User befriend(String username) throws TwitterException {
/*  871 */     return follow(username);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User breakFriendship(String username) {
/*  881 */     return stopFollowing(username);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<User> bulkShow(List<String> screenNames) {
/*  888 */     return users().show(screenNames);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<User> bulkShowById(List<? extends Number> userIds) {
/*  895 */     return users().showById(userIds);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T extends ITweet> List<T> dateFilter(List<T> list) {
/*  909 */     if (this.sinceDate == null && this.untilDate == null)
/*  910 */       return list; 
/*  911 */     ArrayList<T> filtered = new ArrayList<T>(list.size());
/*  912 */     for (ITweet iTweet : list) {
/*      */       
/*  914 */       if (iTweet.getCreatedAt() == null) {
/*  915 */         filtered.add((T)iTweet);
/*      */         continue;
/*      */       } 
/*  918 */       if (this.untilDate != null && this.untilDate.before(iTweet.getCreatedAt())) {
/*      */         continue;
/*      */       }
/*  921 */       if (this.sinceDate != null && this.sinceDate.after(iTweet.getCreatedAt())) {
/*      */         continue;
/*      */       }
/*      */       
/*  925 */       filtered.add((T)iTweet);
/*      */     } 
/*  927 */     return filtered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy(ITweet tweet) throws TwitterException {
/*  935 */     if (tweet instanceof Status) {
/*  936 */       destroyStatus(tweet.getId());
/*      */     } else {
/*  938 */       destroyMessage((Message)tweet);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void destroyMessage(Message dm) {
/*  948 */     String page = post(String.valueOf(this.TWITTER_URL) + "/direct_messages/destroy/" + dm.id + 
/*  949 */         ".json", null, true);
/*  950 */     assert page != null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroyMessage(Number id) {
/*  960 */     String page = post(String.valueOf(this.TWITTER_URL) + "/direct_messages/destroy/" + id + 
/*  961 */         ".json", null, true);
/*  962 */     assert page != null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroyStatus(Number id) throws TwitterException {
/*  972 */     String page = post(String.valueOf(this.TWITTER_URL) + "/statuses/destroy/" + id + ".json", 
/*  973 */         null, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  978 */     flush();
/*  979 */     assert page != null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void destroyStatus(Status status) throws TwitterException {
/*  992 */     destroyStatus(status.getId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean enoughResults(List list) {
/* 1003 */     return (this.maxResults != -1 && list.size() >= this.maxResults);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void flush() {
/* 1009 */     this.http.getPage("https://twitter.com/" + this.name, null, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User follow(String username) throws TwitterException {
/* 1017 */     return users().follow(username);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1022 */     return (this.name == null) ? "Twitter" : ("Twitter[" + this.name + "]");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User follow(User user) {
/* 1030 */     return follow(user.screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Twitter_Geo geo() {
/* 1038 */     return new Twitter_Geo(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Message> getDirectMessages() {
/* 1047 */     return getMessages(String.valueOf(this.TWITTER_URL) + "/direct_messages.json", 
/* 1048 */         standardishParameters());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Message> getDirectMessagesSent() {
/* 1055 */     return getMessages(String.valueOf(this.TWITTER_URL) + "/direct_messages/sent.json", 
/* 1056 */         standardishParameters());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getFavorites() {
/* 1064 */     return getFavorites(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getFavorites(String screenName) {
/* 1075 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName });
/* 1076 */     return getStatuses(String.valueOf(this.TWITTER_URL) + "/favorites/list.json", 
/* 1077 */         addStandardishParameters(vars), this.http.canAuthenticate());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<Number> getFollowerIDs() throws TwitterException {
/* 1085 */     return users().getFollowerIDs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<Number> getFollowerIDs(String screenName) throws TwitterException {
/* 1094 */     return users().getFollowerIDs(screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<User> getFollowers() throws TwitterException {
/* 1102 */     return users().getFollowers();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<User> getFollowers(String username) throws TwitterException {
/* 1110 */     return users().getFollowers(username);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<Number> getFriendIDs() throws TwitterException {
/* 1118 */     return users().getFriendIDs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<Number> getFriendIDs(String screenName) throws TwitterException {
/* 1126 */     return users().getFriendIDs(screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<User> getFriends() throws TwitterException {
/* 1134 */     return users().getFriends();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<User> getFriends(String username) throws TwitterException {
/* 1142 */     return users().getFriends(username);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<Status> getFriendsTimeline() throws TwitterException {
/* 1153 */     return getHomeTimeline();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getHomeTimeline() throws TwitterException {
/* 1161 */     assert this.http.canAuthenticate();
/* 1162 */     return getStatuses(String.valueOf(this.TWITTER_URL) + "/statuses/home_timeline.json", 
/* 1163 */         standardishParameters(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IHttpClient getHttpClient() {
/* 1171 */     return this.http;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TwitterList> getLists() {
/* 1178 */     return getLists(this.name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TwitterList> getListsAll(User user) {
/* 1189 */     assert user != null || this.http.canAuthenticate() : "No authenticating user";
/*      */     try {
/* 1191 */       String url = String.valueOf(this.TWITTER_URL) + "/lists/all.json";
/* 1192 */       Map<String, String> vars = (user.screenName == null) ? 
/* 1193 */         InternalUtils.asMap(new Object[] { "user_id", user.id
/* 1194 */           }) : InternalUtils.asMap(new Object[] { "screen_name", user.screenName });
/* 1195 */       String listsJson = this.http.getPage(url, vars, this.http.canAuthenticate());
/* 1196 */       JSONObject wrapper = new JSONObject(listsJson);
/* 1197 */       JSONArray jarr = (JSONArray)wrapper.get("lists");
/* 1198 */       List<TwitterList> lists = new ArrayList<TwitterList>();
/* 1199 */       for (int i = 0; i < jarr.length(); i++) {
/* 1200 */         JSONObject li = jarr.getJSONObject(i);
/* 1201 */         TwitterList twList = new TwitterList(li, this);
/* 1202 */         lists.add(twList);
/*      */       } 
/* 1204 */       return lists;
/* 1205 */     } catch (JSONException e) {
/* 1206 */       throw new TwitterException.Parsing(null, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TwitterList> getLists(String screenName) {
/* 1215 */     assert screenName != null;
/*      */     try {
/* 1217 */       String url = String.valueOf(this.TWITTER_URL) + "/lists/list.json";
/*      */       
/* 1219 */       Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName });
/* 1220 */       String listsJson = this.http.getPage(url, vars, true);
/*      */       
/* 1222 */       JSONArray jarr = new JSONArray(listsJson);
/* 1223 */       List<TwitterList> lists = new ArrayList<TwitterList>();
/* 1224 */       for (int i = 0; i < jarr.length(); i++) {
/* 1225 */         JSONObject li = jarr.getJSONObject(i);
/* 1226 */         TwitterList twList = new TwitterList(li, this);
/* 1227 */         lists.add(twList);
/*      */       } 
/* 1229 */       return lists;
/* 1230 */     } catch (JSONException e) {
/* 1231 */       throw new TwitterException.Parsing(null, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TwitterList> getListsContaining(String screenName, boolean filterToOwned) {
/* 1244 */     assert screenName != null;
/*      */     try {
/* 1246 */       String url = String.valueOf(this.TWITTER_URL) + "/lists/memberships.json";
/*      */       
/* 1248 */       Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName });
/* 1249 */       if (filterToOwned) {
/* 1250 */         assert this.http.canAuthenticate();
/* 1251 */         vars.put("filter_to_owned_lists", "1");
/*      */       } 
/* 1253 */       String listsJson = this.http.getPage(url, vars, this.http.canAuthenticate());
/* 1254 */       JSONObject wrapper = new JSONObject(listsJson);
/* 1255 */       JSONArray jarr = (JSONArray)wrapper.get("lists");
/* 1256 */       List<TwitterList> lists = new ArrayList<TwitterList>();
/* 1257 */       for (int i = 0; i < jarr.length(); i++) {
/* 1258 */         JSONObject li = jarr.getJSONObject(i);
/* 1259 */         TwitterList twList = new TwitterList(li, this);
/* 1260 */         lists.add(twList);
/*      */       } 
/* 1262 */       return lists;
/* 1263 */     } catch (JSONException e) {
/* 1264 */       throw new TwitterException.Parsing(null, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TwitterList> getListsContainingMe() {
/* 1275 */     return getListsContaining(this.name, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLongStatus(Status truncatedStatus) {
/* 1288 */     TwitLonger tl = new TwitLonger();
/* 1289 */     return tl.getLongStatus(truncatedStatus);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxResults() {
/* 1296 */     return this.maxResults;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getMentions() {
/* 1312 */     return getStatuses(String.valueOf(this.TWITTER_URL) + "/statuses/mentions_timeline.json", 
/* 1313 */         standardishParameters(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Message> getMessages(String url, Map<String, String> var) {
/* 1326 */     if (this.maxResults < 1) {
/* 1327 */       List<Message> list = Message.getMessages(this.http.getPage(url, var, 
/* 1328 */             true));
/* 1329 */       list = dateFilter(list);
/* 1330 */       return list;
/*      */     } 
/*      */ 
/*      */     
/* 1334 */     BigInteger maxId = this.untilId;
/* 1335 */     List<Message> msgs = new ArrayList<Message>();
/* 1336 */     while (msgs.size() <= this.maxResults) {
/* 1337 */       String p = this.http.getPage(url, var, true);
/* 1338 */       List<Message> nextpage = Message.getMessages(p);
/*      */       
/* 1340 */       maxId = InternalUtils.getMinId(maxId, (List)nextpage);
/*      */       
/* 1342 */       nextpage = dateFilter(nextpage);
/* 1343 */       msgs.addAll(nextpage);
/*      */       
/* 1345 */       if (nextpage.size() < 20) {
/*      */         break;
/*      */       }
/*      */       
/* 1349 */       var.put("max_id", maxId.toString());
/*      */     } 
/* 1351 */     return msgs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RateLimit getRateLimit(KRequestType reqType) {
/* 1372 */     return this.http.getRateLimit(reqType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRateLimitStatus() {
/* 1394 */     RateLimit rl = ((URLConnectionHttpClient)this.http).updateRateLimits().get(KRequestType.NORMAL.rateLimit);
/* 1395 */     return (rl == null) ? 90 : rl.getRemaining();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getReplies() throws TwitterException {
/* 1416 */     return getMentions();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<User> getRetweeters(Status tweet) {
/* 1429 */     String url = String.valueOf(this.TWITTER_URL) + "/statuses/retweets/" + tweet.id + 
/* 1430 */       ".json";
/* 1431 */     Map<String, String> vars = addStandardishParameters(new HashMap<String, String>());
/* 1432 */     String json = this.http.getPage(url, vars, this.http.canAuthenticate());
/* 1433 */     List<Status> ss = Status.getStatuses(json);
/* 1434 */     List<User> users = new ArrayList<User>(ss.size());
/* 1435 */     for (Status status : ss) {
/* 1436 */       users.add(status.getUser());
/*      */     }
/* 1438 */     return users;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getRetweets(Status tweet) {
/* 1447 */     String url = String.valueOf(this.TWITTER_URL) + "/statuses/retweets/" + tweet.id + ".json";
/* 1448 */     Map<String, String> vars = addStandardishParameters(new HashMap<String, String>());
/* 1449 */     String json = this.http.getPage(url, vars, true);
/* 1450 */     List<Status> newStyle = Status.getStatuses(json);
/*      */     
/*      */     try {
/* 1453 */       StringBuilder sq = new StringBuilder();
/* 1454 */       sq.append("\"RT @" + tweet.getUser().getScreenName() + ": ");
/* 1455 */       if (sq.length() + tweet.text.length() + 1 > 140) {
/* 1456 */         int i = tweet.text.lastIndexOf(' ', 140 - sq.length() - 1);
/* 1457 */         String words = tweet.text.substring(0, i);
/* 1458 */         sq.append(words);
/*      */       } else {
/* 1460 */         sq.append(tweet.text);
/*      */       } 
/* 1462 */       sq.append('"');
/* 1463 */       List<Status> oldStyle = search(sq.toString());
/*      */       
/* 1465 */       newStyle.addAll(oldStyle);
/* 1466 */       Collections.sort(newStyle, InternalUtils.NEWEST_FIRST);
/* 1467 */       return newStyle;
/* 1468 */     } catch (TwitterException e) {
/*      */       
/* 1470 */       return newStyle;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getRetweetsByMe() {
/* 1483 */     List<Status> myTweets = getUserTimeline();
/* 1484 */     List<Status> retweets = new ArrayList<Status>();
/* 1485 */     for (Status status : myTweets) {
/* 1486 */       if (status.getOriginal() != null && status.getText().startsWith("RT")) {
/* 1487 */         retweets.add(status);
/*      */       }
/*      */     } 
/* 1490 */     return retweets;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getRetweetsOfMe() {
/* 1498 */     String url = String.valueOf(this.TWITTER_URL) + "/statuses/retweets_of_me.json";
/* 1499 */     Map<String, String> vars = addStandardishParameters(new HashMap<String, String>());
/* 1500 */     String json = this.http.getPage(url, vars, true);
/* 1501 */     return Status.getStatuses(json);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getScreenName() {
/* 1511 */     if (this.name != null) {
/* 1512 */       return this.name;
/*      */     }
/* 1514 */     getSelf();
/* 1515 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getScreenNameIfKnown() {
/* 1525 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, String> getSearchParams(String searchTerm, Integer rpp) {
/* 1536 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "count", rpp, "q", searchTerm });
/* 1537 */     if (this.sinceId != null) {
/* 1538 */       vars.put("since_id", this.sinceId.toString());
/*      */     }
/* 1540 */     if (this.untilId != null)
/*      */     {
/*      */       
/* 1543 */       vars.put("max_id", this.untilId.toString());
/*      */     }
/*      */ 
/*      */     
/* 1547 */     if (this.untilDate != null) {
/* 1548 */       vars.put("until", InternalUtils.df.format(this.untilDate));
/*      */     }
/* 1550 */     if (this.lang != null) {
/* 1551 */       vars.put("lang", this.lang);
/*      */     }
/* 1553 */     if (this.geocode != null) {
/* 1554 */       vars.put("geocode", this.geocode);
/*      */     }
/* 1556 */     if (this.resultType != null) {
/* 1557 */       vars.put("result_type", this.resultType);
/*      */     }
/* 1559 */     addStandardishParameters(vars);
/* 1560 */     return vars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public User getSelf() {
/* 1569 */     if (this.self != null)
/* 1570 */       return this.self; 
/* 1571 */     if (!this.http.canAuthenticate()) {
/* 1572 */       if (this.name != null) {
/*      */         
/* 1574 */         this.self = new User(this.name);
/* 1575 */         return this.self;
/*      */       } 
/* 1577 */       return null;
/*      */     } 
/* 1579 */     account().verifyCredentials();
/* 1580 */     this.name = this.self.getScreenName();
/* 1581 */     return this.self;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status getStatus() throws TwitterException {
/* 1595 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "count", Integer.valueOf(6) });
/* 1596 */     String json = this.http.getPage(
/* 1597 */         String.valueOf(this.TWITTER_URL) + "/statuses/user_timeline.json", vars, true);
/* 1598 */     List<Status> statuses = Status.getStatuses(json);
/* 1599 */     if (statuses.size() == 0)
/* 1600 */       return null; 
/* 1601 */     return statuses.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status getStatus(Number id) throws TwitterException {
/* 1612 */     boolean auth = InternalUtils.authoriseIn11(this);
/* 1613 */     Map<String, String> vars = this.tweetEntities ? InternalUtils.asMap(new Object[] { "include_entities", "1"
/* 1614 */         }) : null;
/* 1615 */     String json = this.http.getPage(String.valueOf(this.TWITTER_URL) + "/statuses/show/" + id + 
/* 1616 */         ".json", vars, auth);
/*      */     try {
/* 1618 */       return new Status(new JSONObject(json), null);
/* 1619 */     } catch (JSONException e) {
/* 1620 */       throw new TwitterException.Parsing(json, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status getStatus(String username) throws TwitterException {
/* 1631 */     assert username != null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1636 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "id", username, "count", Integer.valueOf(6) });
/* 1637 */     String json = this.http.getPage(
/* 1638 */         String.valueOf(this.TWITTER_URL) + "/statuses/user_timeline.json", vars, this.http.canAuthenticate());
/* 1639 */     List<Status> statuses = Status.getStatuses(json);
/* 1640 */     if (statuses.size() == 0)
/* 1641 */       return null; 
/* 1642 */     return statuses.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<Status> getStatuses(String url, Map<String, String> var, boolean authenticate) {
/* 1657 */     if (this.maxResults < 1) {
/*      */       
/*      */       try {
/* 1660 */         list = Status.getStatuses(this.http.getPage(url, var, 
/* 1661 */               authenticate));
/* 1662 */       } catch (Parsing pex) {
/*      */ 
/*      */         
/* 1665 */         if (this.http.isRetryOnError()) {
/* 1666 */           InternalUtils.sleep(250L);
/* 1667 */           String json = this.http.getPage(url, var, authenticate);
/* 1668 */           list = Status.getStatuses(json);
/*      */         } else {
/* 1670 */           throw pex;
/*      */         } 
/*      */       } 
/* 1673 */       List<Status> list = dateFilter(list);
/* 1674 */       return list;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1680 */     BigInteger maxId = this.untilId;
/* 1681 */     List<Status> msgs = new ArrayList<Status>();
/* 1682 */     while (msgs.size() <= this.maxResults) {
/*      */       List<Status> nextpage;
/*      */       try {
/* 1685 */         String json = this.http.getPage(url, var, authenticate);
/* 1686 */         nextpage = Status.getStatuses(json);
/* 1687 */       } catch (Parsing pex) {
/*      */ 
/*      */         
/* 1690 */         if (this.http.isRetryOnError()) {
/* 1691 */           InternalUtils.sleep(250L);
/* 1692 */           String json = this.http.getPage(url, var, authenticate);
/* 1693 */           nextpage = Status.getStatuses(json);
/*      */         } else {
/* 1695 */           throw pex;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1701 */       if (nextpage.size() == 0) {
/*      */         break;
/*      */       }
/*      */       
/* 1705 */       maxId = InternalUtils.getMinId(maxId, (List)nextpage);
/*      */       
/* 1707 */       msgs.addAll(dateFilter(nextpage));
/* 1708 */       var.put("max_id", maxId.toString());
/*      */     } 
/* 1710 */     return msgs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getTrends() {
/* 1717 */     return getTrends(Integer.valueOf(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getTrends(Number woeid) {
/* 1728 */     String jsonTrends = this.http.getPage(String.valueOf(this.TWITTER_URL) + "/trends/place.json", 
/* 1729 */         InternalUtils.asMap(new Object[] { "id", woeid }, ), true);
/*      */     try {
/* 1731 */       JSONArray jarr = new JSONArray(jsonTrends);
/* 1732 */       JSONObject json1 = jarr.getJSONObject(0);
/* 1733 */       JSONArray json2 = json1.getJSONArray("trends");
/* 1734 */       List<String> trends = new ArrayList<String>();
/* 1735 */       for (int i = 0; i < json2.length(); i++) {
/* 1736 */         JSONObject ti = json2.getJSONObject(i);
/* 1737 */         String t = ti.getString("name");
/* 1738 */         trends.add(t);
/*      */       } 
/* 1740 */       return trends;
/* 1741 */     } catch (JSONException e) {
/* 1742 */       throw new TwitterException.Parsing(jsonTrends, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getUntilDate() {
/* 1750 */     return this.untilDate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User getUser(long userId) {
/* 1758 */     return show(Long.valueOf(userId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User getUser(String screenName) {
/* 1766 */     return show(screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getUserTimeline() throws TwitterException {
/* 1774 */     return getStatuses(String.valueOf(this.TWITTER_URL) + "/statuses/user_timeline.json", 
/* 1775 */         standardishParameters(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getUserTimeline(Long userId) throws TwitterException {
/* 1786 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "user_id", userId });
/* 1787 */     addStandardishParameters(vars);
/*      */     
/* 1789 */     boolean authenticate = this.http.canAuthenticate();
/*      */     try {
/* 1791 */       return getStatuses(String.valueOf(this.TWITTER_URL) + "/statuses/user_timeline.json", 
/* 1792 */           vars, authenticate);
/* 1793 */     } catch (E401 e) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1798 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getUserTimeline(String screenName) throws TwitterException {
/* 1831 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName });
/* 1832 */     addStandardishParameters(vars);
/*      */     
/* 1834 */     boolean authenticate = this.http.canAuthenticate();
/*      */     try {
/* 1836 */       return getStatuses(String.valueOf(this.TWITTER_URL) + "/statuses/user_timeline.json", 
/* 1837 */           vars, authenticate);
/* 1838 */     } catch (E404 e) {
/* 1839 */       throw new TwitterException.E404("Twitter does not return any information for " + screenName + 
/* 1840 */           ". They may have been deleted long ago.");
/* 1841 */     } catch (E401 e) {
/*      */ 
/*      */       
/* 1844 */       isSuspended(screenName);
/* 1845 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> getUserTimelineWithRetweets(String screenName) throws TwitterException {
/* 1874 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName, "include_rts", "1" });
/* 1875 */     addStandardishParameters(vars);
/*      */     
/* 1877 */     boolean authenticate = this.http.canAuthenticate();
/*      */     try {
/* 1879 */       return getStatuses(String.valueOf(this.TWITTER_URL) + "/statuses/user_timeline.json", 
/* 1880 */           vars, authenticate);
/* 1881 */     } catch (E401 e) {
/* 1882 */       isSuspended(screenName);
/* 1883 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean isFollower(String userB) {
/* 1892 */     return isFollower(userB, this.name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFollower(String followerScreenName, String followedScreenName) {
/* 1901 */     return users().isFollower(followerScreenName, followedScreenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean isFollowing(String userB) {
/* 1909 */     return isFollower(this.name, userB);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean isFollowing(User user) {
/* 1917 */     return isFollowing(user.screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRateLimited(KRequestType reqType, int minCalls) {
/* 1934 */     RateLimit rl = getRateLimit(reqType);
/*      */     
/* 1936 */     if (rl == null) {
/* 1937 */       return false;
/*      */     }
/*      */     
/* 1940 */     if (rl.getRemaining() >= minCalls) {
/* 1941 */       return false;
/*      */     }
/* 1943 */     if (rl.isOutOfDate()) {
/* 1944 */       return false;
/*      */     }
/* 1946 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void isSuspended(String screenName) throws TwitterException.SuspendedUser {
/* 1957 */     show(screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTwitlongerSetup() {
/* 1969 */     return (this.twitlongerApiKey != null && this.twitlongerAppName != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isValidLogin() {
/* 1979 */     if (!this.http.canAuthenticate())
/* 1980 */       return false; 
/*      */     try {
/* 1982 */       Twitter_Account ta = new Twitter_Account(this);
/* 1983 */       User u = ta.verifyCredentials();
/* 1984 */       return true;
/* 1985 */     } catch (E403 e) {
/* 1986 */       return false;
/* 1987 */     } catch (E401 e) {
/* 1988 */       return false;
/* 1989 */     } catch (TwitterException e) {
/* 1990 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String post(String uri, Map<String, String> vars, boolean authenticate) throws TwitterException {
/* 1999 */     String page = this.http.post(uri, vars, authenticate);
/* 2000 */     return page;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reportSpam(String screenName) {
/* 2009 */     this.http.getPage(String.valueOf(this.TWITTER_URL) + "/version/report_spam.json", 
/* 2010 */         InternalUtils.asMap(new Object[] { "screen_name", screenName }, ), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status retweet(Status tweet) {
/*      */     try {
/* 2024 */       String result = post(
/* 2025 */           String.valueOf(this.TWITTER_URL) + "/statuses/retweet/" + tweet.getId() + 
/* 2026 */           ".json", null, true);
/* 2027 */       return new Status(new JSONObject(result), null);
/*      */     
/*      */     }
/* 2030 */     catch (E403 e) {
/* 2031 */       List<Status> rts = getRetweetsByMe();
/* 2032 */       for (Status rt : rts) {
/* 2033 */         if (tweet.equals(rt.getOriginal()))
/* 2034 */           throw new TwitterException.Repetition(rt.getText()); 
/*      */       } 
/* 2036 */       throw e;
/* 2037 */     } catch (JSONException e) {
/* 2038 */       throw new TwitterException.Parsing(null, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> search(String searchTerm) {
/* 2048 */     return search(searchTerm, null, 100);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Status> search(String searchTerm, ICallback callback, int rpp) {
/*      */     Map<String, String> vars;
/* 2091 */     if (rpp > 100 && this.maxResults < rpp) {
/* 2092 */       throw new IllegalArgumentException(
/* 2093 */           "You need to switch on paging to fetch more than 100 search results. First call setMaxResults() to raise the limit above " + 
/* 2094 */           rpp);
/*      */     }
/* 2096 */     if (searchTerm.length() > 1000) {
/* 2097 */       throw new TwitterException.E406("Search query too long: " + searchTerm);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2102 */     if (this.maxResults < 100 && this.maxResults > 0) {
/*      */       
/* 2104 */       vars = getSearchParams(searchTerm, Integer.valueOf(this.maxResults));
/*      */     } else {
/* 2106 */       vars = getSearchParams(searchTerm, Integer.valueOf(rpp));
/*      */     } 
/*      */ 
/*      */     
/* 2110 */     List<Status> allResults = new ArrayList<Status>(Math.max(this.maxResults, 
/* 2111 */           rpp));
/* 2112 */     String url = String.valueOf(this.TWITTER_URL) + "/search/tweets.json";
/* 2113 */     BigInteger maxId = this.untilId;
/*      */     do {
/* 2115 */       vars.put("max_id", maxId);
/*      */       
/*      */       try {
/* 2118 */         String json = this.http.getPage(url, vars, true);
/* 2119 */         stati = Status.getStatusesFromSearch(this, json);
/* 2120 */       } catch (Parsing pex) {
/*      */ 
/*      */         
/* 2123 */         if (this.http.isRetryOnError()) {
/* 2124 */           InternalUtils.sleep(250L);
/* 2125 */           String json = this.http.getPage(url, vars, true);
/* 2126 */           stati = Status.getStatusesFromSearch(this, json);
/*      */         } else {
/* 2128 */           throw pex;
/*      */         } 
/* 2130 */       } catch (E403 ex) {
/*      */         
/* 2132 */         if (ex.getMessage() != null && ex.getMessage().startsWith("code 195:")) {
/* 2133 */           throw new TwitterException.E406("Search too long/complex: " + ex.getMessage());
/*      */         }
/* 2135 */         throw ex;
/*      */       } 
/* 2137 */       int numResults = stati.size();
/*      */       
/* 2139 */       maxId = InternalUtils.getMinId(maxId, (List)stati);
/*      */       
/* 2141 */       List<Status> stati = dateFilter(stati);
/* 2142 */       allResults.addAll(stati);
/* 2143 */       if (callback != null)
/*      */       {
/* 2145 */         if (callback.process(stati)) {
/*      */           break;
/*      */         }
/*      */       }
/* 2149 */       if ((rpp == 100 && numResults < 70) || numResults < rpp) {
/*      */         break;
/*      */       }
/* 2152 */     } while (allResults.size() < this.maxResults);
/* 2153 */     return allResults;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<User> searchUsers(String searchTerm) {
/* 2197 */     return users().searchUsers(searchTerm);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Message sendMessage(String recipient, String text) throws TwitterException {
/* 2215 */     assert recipient != null && text != null : String.valueOf(recipient) + " " + text;
/* 2216 */     assert !text.startsWith("d " + recipient) : String.valueOf(recipient) + " " + text;
/* 2217 */     assert !recipient.startsWith("@") : String.valueOf(recipient) + " " + text;
/* 2218 */     if (text.length() > 140) {
/* 2219 */       throw new IllegalArgumentException("Message is too long.");
/*      */     }
/* 2221 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "user", recipient, "text", text });
/* 2222 */     if (this.tweetEntities) {
/* 2223 */       vars.put("include_entities", "1");
/*      */     }
/* 2225 */     String result = null;
/*      */     
/*      */     try {
/* 2228 */       result = post(String.valueOf(this.TWITTER_URL) + "/direct_messages/new.json", vars, true);
/*      */       
/* 2230 */       return new Message(new JSONObject(result));
/* 2231 */     } catch (JSONException e) {
/* 2232 */       throw new TwitterException.Parsing(result, e);
/* 2233 */     } catch (E404 e) {
/*      */       
/* 2235 */       throw new TwitterException.E404(String.valueOf(e.getMessage()) + " with recipient=" + 
/* 2236 */           recipient + ", text=" + text);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAPIRootUrl(String url) {
/* 2251 */     assert url.startsWith("http://") || url.startsWith("https://") : url;
/* 2252 */     assert !url.endsWith("/") : "Please remove the trailing / from " + url;
/* 2253 */     this.TWITTER_URL = url;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCount(Integer count) {
/* 2265 */     this.count = count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status setFavorite(Status status, boolean isFavorite) {
/*      */     try {
/* 2276 */       String uri = isFavorite ? (String.valueOf(this.TWITTER_URL) + "/favorites/create.json") : (
/* 2277 */         String.valueOf(this.TWITTER_URL) + "/favorites/destroy.json");
/* 2278 */       String json = this.http.post(uri, InternalUtils.asMap(new Object[] { "id", status.id }, ), true);
/* 2279 */       return new Status(new JSONObject(json), null);
/* 2280 */     } catch (E403 e) {
/*      */       
/* 2282 */       if (e.getMessage() != null && 
/* 2283 */         e.getMessage().contains("already favorited")) {
/* 2284 */         return null;
/*      */       }
/*      */       
/* 2287 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIncludeRTs(boolean includeRTs) {
/* 2298 */     this.includeRTs = includeRTs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIncludeTweetEntities(boolean tweetEntities) {
/* 2310 */     this.tweetEntities = tweetEntities;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLanguage(String language) {
/* 2324 */     this.lang = language;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxResults(int maxResults) {
/* 2338 */     assert maxResults != 0;
/* 2339 */     this.maxResults = maxResults;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMyLocation(double[] latitudeLongitude) {
/* 2361 */     this.myLatLong = latitudeLongitude;
/* 2362 */     if (this.myLatLong == null)
/*      */       return; 
/* 2364 */     if (Math.abs(this.myLatLong[0]) > 90.0D)
/* 2365 */       throw new IllegalArgumentException(String.valueOf(this.myLatLong[0]) + 
/* 2366 */           " is not within +/- 90"); 
/* 2367 */     if (Math.abs(this.myLatLong[1]) > 180.0D) {
/* 2368 */       throw new IllegalArgumentException(String.valueOf(this.myLatLong[1]) + 
/* 2369 */           " is not within +/- 180");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSearchLocation(double latitude, double longitude, String radius) {
/* 2395 */     assert radius.endsWith("mi") || radius.endsWith("km") : radius;
/* 2396 */     this.geocode = String.valueOf((float)latitude) + "," + (float)longitude + "," + radius;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSearchLocation() {
/* 2403 */     return this.geocode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSearchResultType(String resultType) {
/* 2417 */     this.resultType = resultType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setSinceDate(Date sinceDate) {
/* 2435 */     this.sinceDate = sinceDate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSinceId(Number statusId) {
/* 2452 */     this.sinceId = statusId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSource(String sourceApp) {
/* 2467 */     this.sourceApp = sourceApp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status setStatus(String statusText) throws TwitterException {
/* 2482 */     return updateStatus(statusText);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setUntilDate(Date untilDate) {
/* 2494 */     this.untilDate = untilDate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUntilId(Number untilId) {
/* 2504 */     if (untilId == null) {
/* 2505 */       this.untilId = null;
/*      */       return;
/*      */     } 
/* 2508 */     if (untilId instanceof BigInteger) {
/* 2509 */       this.untilId = (BigInteger)untilId;
/*      */       return;
/*      */     } 
/* 2512 */     this.untilId = BigInteger.valueOf(untilId.longValue());
/*      */   }
/*      */   
/*      */   public BigInteger getUntilId() {
/* 2516 */     return this.untilId;
/*      */   }
/*      */   
/*      */   public Number getSinceId() {
/* 2520 */     return this.sinceId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setupTwitlonger(String twitlongerAppName, String twitlongerApiKey) {
/* 2538 */     this.twitlongerAppName = twitlongerAppName;
/* 2539 */     this.twitlongerApiKey = twitlongerApiKey;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User show(Number userId) {
/* 2549 */     return users().show(userId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User show(String screenName) throws TwitterException, TwitterException.SuspendedUser {
/* 2560 */     return users().show(screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> splitMessage(String longStatus) {
/* 2572 */     if (longStatus.length() <= 140) {
/* 2573 */       return Collections.singletonList(longStatus);
/*      */     }
/* 2575 */     List<String> sections = new ArrayList<String>(4);
/* 2576 */     StringBuilder tweet = new StringBuilder(140);
/* 2577 */     String[] words = longStatus.split("\\s+"); byte b; int i; String[] arrayOfString1;
/* 2578 */     for (i = (arrayOfString1 = words).length, b = 0; b < i; ) { String w = arrayOfString1[b];
/*      */ 
/*      */ 
/*      */       
/* 2582 */       if (tweet.length() + w.length() + 1 > 140) {
/*      */         
/* 2584 */         tweet.append("...");
/* 2585 */         sections.add(tweet.toString());
/* 2586 */         tweet = new StringBuilder(140);
/* 2587 */         tweet.append(w);
/*      */       } else {
/* 2589 */         if (tweet.length() != 0) {
/* 2590 */           tweet.append(" ");
/*      */         }
/* 2592 */         tweet.append(w);
/*      */       } 
/*      */       b++; }
/*      */     
/* 2596 */     if (tweet.length() != 0) {
/* 2597 */       sections.add(tweet.toString());
/*      */     }
/* 2599 */     return sections;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, String> standardishParameters() {
/* 2607 */     return addStandardishParameters(new HashMap<String, String>());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User stopFollowing(String username) {
/* 2617 */     return users().stopFollowing(username);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public User stopFollowing(User user) {
/* 2627 */     return stopFollowing(user.screenName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updateConfiguration() {
/* 2635 */     String json = this.http.getPage(String.valueOf(this.TWITTER_URL) + "/help/configuration.json", 
/* 2636 */         null, true);
/* 2637 */     boolean change = false;
/*      */     try {
/* 2639 */       JSONObject jo = new JSONObject(json);
/*      */       
/* 2641 */       int len = jo.getInt("short_url_length");
/* 2642 */       if (len != LINK_LENGTH) change = true; 
/* 2643 */       LINK_LENGTH = len;
/*      */ 
/*      */ 
/*      */       
/* 2647 */       len = jo.getInt("characters_reserved_per_media");
/* 2648 */       if (len != MEDIA_LENGTH) change = true; 
/* 2649 */       MEDIA_LENGTH = len;
/*      */ 
/*      */       
/* 2652 */       long lmt = jo.getLong("photo_size_limit");
/* 2653 */       if (lmt != PHOTO_SIZE_LIMIT) change = true; 
/* 2654 */       PHOTO_SIZE_LIMIT = lmt;
/*      */ 
/*      */ 
/*      */       
/* 2658 */       return change;
/* 2659 */     } catch (JSONException e) {
/* 2660 */       throw new TwitterException.Parsing(json, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status updateLongStatus(String message, Number inReplyToStatusId) {
/* 2682 */     TwitLonger tl = new TwitLonger(this, this.twitlongerApiKey, this.twitlongerAppName);
/* 2683 */     return tl.updateLongStatus(message, inReplyToStatusId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status updateStatus(String statusText) {
/* 2694 */     return updateStatus(statusText, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int countCharacters(String statusText) {
/* 2709 */     int shortLength = statusText.length();
/* 2710 */     Matcher m = Regex.VALID_URL.matcher(statusText);
/* 2711 */     while (m.find()) {
/* 2712 */       shortLength += LINK_LENGTH - m.group().length();
/*      */       
/* 2714 */       if (m.group().startsWith("https")) {
/* 2715 */         shortLength++;
/*      */       }
/*      */     } 
/* 2718 */     return shortLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status updateStatus(String statusText, Number inReplyToStatusId) throws TwitterException {
/* 2753 */     Map<String, String> vars = updateStatus2_vars(statusText, inReplyToStatusId, false);
/* 2754 */     String result = this.http.post(String.valueOf(this.TWITTER_URL) + "/statuses/update.json", vars, 
/* 2755 */         true);
/*      */     try {
/* 2757 */       Status s = new Status(new JSONObject(result), null);
/* 2758 */       s = updateStatus2_safetyCheck(statusText, s);
/* 2759 */       return s;
/* 2760 */     } catch (JSONException e) {
/* 2761 */       throw new TwitterException.Parsing(result, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, String> updateStatus2_vars(String statusText, Number inReplyToStatusId, boolean withMedia) {
/* 2774 */     int max = withMedia ? (140 - MEDIA_LENGTH) : 140;
/* 2775 */     if (statusText.length() > max && 
/* 2776 */       this.TWITTER_URL.contains("twitter") && 
/* 2777 */       CHECK_TWEET_LENGTH) {
/*      */       
/* 2779 */       int shortLength = countCharacters(statusText);
/* 2780 */       if (shortLength > max) {
/*      */         
/* 2782 */         if (statusText.startsWith("RT")) {
/* 2783 */           throw new IllegalArgumentException(
/* 2784 */               "Status text must be 140 characters or less -- use Twitter.retweet() to do new-style retweets which can be a bit longer: " + 
/* 2785 */               statusText.length() + " " + statusText);
/*      */         }
/* 2787 */         if (withMedia) {
/* 2788 */           throw new IllegalArgumentException(
/* 2789 */               "Status-with-media text must be " + max + " characters or less: " + 
/* 2790 */               statusText.length() + " " + statusText);
/*      */         }
/* 2792 */         throw new IllegalArgumentException(
/* 2793 */             "Status text must be 140 characters or less: " + 
/* 2794 */             statusText.length() + " " + statusText);
/*      */       } 
/*      */     } 
/*      */     
/* 2798 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "status", statusText });
/* 2799 */     if (this.tweetEntities) vars.put("include_entities", "1");
/*      */ 
/*      */     
/* 2802 */     if (this.myLatLong != null) {
/* 2803 */       vars.put("lat", Double.toString(this.myLatLong[0]));
/* 2804 */       vars.put("long", Double.toString(this.myLatLong[1]));
/*      */     } 
/* 2806 */     if (this.placeId != null) {
/* 2807 */       vars.put("place_id", Long.toString(this.placeId.longValue()));
/*      */     }
/*      */     
/* 2810 */     if (this.sourceApp != null) {
/* 2811 */       vars.put("source", this.sourceApp);
/*      */     }
/* 2813 */     if (inReplyToStatusId != null) {
/*      */       
/* 2815 */       double v = inReplyToStatusId.doubleValue();
/* 2816 */       assert v != 0.0D && v != -1.0D;
/* 2817 */       vars.put("in_reply_to_status_id", inReplyToStatusId.toString());
/*      */     } 
/* 2819 */     return vars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Status updateStatus2_safetyCheck(String statusText, Status s) {
/* 2834 */     String st = statusText.toLowerCase();
/* 2835 */     if (st.startsWith("dm ") || st.startsWith("d ")) {
/* 2836 */       return null;
/*      */     }
/* 2838 */     if (!WORRIED_ABOUT_TWITTER) {
/* 2839 */       return s;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2847 */     String targetText = statusText.trim();
/* 2848 */     String returnedStatusText = s.text.trim();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2853 */     targetText = InternalUtils.stripUrls(targetText);
/* 2854 */     returnedStatusText = InternalUtils.stripUrls(returnedStatusText);
/* 2855 */     if (returnedStatusText.equals(targetText))
/*      */     {
/* 2857 */       return s;
/*      */     }
/*      */     try {
/* 2860 */       Thread.sleep(500L);
/* 2861 */     } catch (InterruptedException interruptedException) {}
/*      */ 
/*      */     
/* 2864 */     Status s2 = getStatus();
/* 2865 */     if (s2 != null) {
/* 2866 */       returnedStatusText = InternalUtils.stripUrls(s2.text.trim());
/* 2867 */       if (targetText.equals(returnedStatusText)) {
/* 2868 */         return s2;
/*      */       }
/*      */     } 
/* 2871 */     throw new TwitterException.Unexplained(
/* 2872 */         "Unexplained failure for tweet: expected \"" + statusText + 
/* 2873 */         "\" but got " + s2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Status updateStatusWithMedia(String statusText, BigInteger inReplyToStatusId, File mediaFile) {
/* 2890 */     if (mediaFile == null || !mediaFile.isFile()) {
/* 2891 */       throw new IllegalArgumentException("Invalid file: " + mediaFile);
/*      */     }
/* 2893 */     Map<String, String> vars = updateStatus2_vars(statusText, inReplyToStatusId, true);
/* 2894 */     vars.put("media[]", mediaFile);
/*      */ 
/*      */     
/* 2897 */     String result = null;
/*      */     
/*      */     try {
/* 2900 */       String url = String.valueOf(this.TWITTER_URL) + "/statuses/update_with_media.json";
/* 2901 */       result = ((OAuthSignpostClient)this.http).postMultipartForm(url, vars);
/* 2902 */       Status s = new Status(new JSONObject(result), null);
/* 2903 */       return s;
/* 2904 */     } catch (E403 e) {
/*      */       
/* 2906 */       Status s = getStatus();
/* 2907 */       if (s != null && s.getText().equals(statusText))
/* 2908 */         throw new TwitterException.Repetition(s.getText()); 
/* 2909 */       throw e;
/* 2910 */     } catch (JSONException e) {
/* 2911 */       throw new TwitterException.Parsing(result, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Twitter_Users users() {
/* 2919 */     return new Twitter_Users(this);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Twitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Twitter_Users
/*     */ {
/*     */   private final Twitter.IHttpClient http;
/*     */   private final Twitter jtwit;
/*     */   
/*     */   Twitter_Users(Twitter jtwit) {
/*  33 */     this.jtwit = jtwit;
/*  34 */     this.http = jtwit.getHttpClient();
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
/*     */   public User block(String screenName) {
/*  48 */     HashMap<Object, Object> vars = new HashMap<Object, Object>();
/*  49 */     vars.put("screen_name", screenName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     String json = this.http.post(String.valueOf(this.jtwit.TWITTER_URL) + "/blocks/create.json", 
/*  55 */         (Map)vars, true);
/*  56 */     return InternalUtils.user(json);
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
/*     */   List<User> bulkShow2(String apiMethod, Class<String> stringOrNumber, Collection<?> screenNamesOrIds) {
/*  74 */     boolean auth = InternalUtils.authoriseIn11(this.jtwit);
/*  75 */     int batchSize = 100;
/*  76 */     ArrayList<User> users = new ArrayList<User>(screenNamesOrIds.size());
/*  77 */     List _screenNamesOrIds = (screenNamesOrIds instanceof List) ? (List)screenNamesOrIds : 
/*  78 */       new ArrayList(screenNamesOrIds);
/*  79 */     for (int i = 0; i < _screenNamesOrIds.size(); i += batchSize) {
/*  80 */       int last = i + batchSize;
/*  81 */       String names = InternalUtils.join(_screenNamesOrIds, i, last);
/*  82 */       String var = (stringOrNumber == String.class) ? "screen_name" : 
/*  83 */         "user_id";
/*  84 */       Map<String, String> vars = InternalUtils.asMap(new Object[] { var, names });
/*     */       try {
/*  86 */         String json = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + apiMethod, vars, auth);
/*  87 */         List<User> usersi = User.getUsers(json);
/*  88 */         users.addAll(usersi);
/*  89 */       } catch (E404 e404) {
/*     */ 
/*     */       
/*  92 */       } catch (TwitterException e) {
/*     */ 
/*     */ 
/*     */         
/*  96 */         if (users.size() == 0)
/*  97 */           throw e; 
/*  98 */         e.printStackTrace();
/*     */         break;
/*     */       } 
/*     */     } 
/* 102 */     return users;
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
/*     */   public User follow(String username) throws TwitterException {
/* 118 */     if (username == null)
/* 119 */       throw new NullPointerException(); 
/* 120 */     if (username.equals(this.jtwit.getScreenName()))
/* 121 */       throw new IllegalArgumentException("follow yourself makes no sense"); 
/* 122 */     String page = null;
/*     */     
/*     */     try {
/* 125 */       Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", username });
/* 126 */       page = this.http.post(String.valueOf(this.jtwit.TWITTER_URL) + "/friendships/create.json", 
/* 127 */           vars, true);
/*     */ 
/*     */       
/* 130 */       return new User(new JSONObject(page), null);
/* 131 */     } catch (SuspendedUser e) {
/* 132 */       throw e;
/* 133 */     } catch (Repetition e) {
/* 134 */       return null;
/* 135 */     } catch (E403 e) {
/*     */       
/*     */       try {
/* 138 */         if (isFollowing(username))
/* 139 */           return null; 
/* 140 */       } catch (TwitterException twitterException) {}
/*     */ 
/*     */       
/* 143 */       throw e;
/* 144 */     } catch (JSONException e) {
/* 145 */       throw new TwitterException.Parsing(page, e);
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
/*     */   public User follow(User user) {
/* 158 */     return follow(user.screenName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Number> getBlockedIds() {
/* 167 */     String json = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + 
/* 168 */         "/blocks/ids.json", null, true);
/*     */     try {
/* 170 */       JSONArray arr = json.startsWith("[") ? new JSONArray(json) : (
/* 171 */         new JSONObject(json)).getJSONArray("ids");
/* 172 */       List<Number> ids = new ArrayList<Number>(arr.length());
/* 173 */       for (int i = 0, n = arr.length(); i < n; i++) {
/* 174 */         ids.add(Long.valueOf(arr.getLong(i)));
/*     */       }
/* 176 */       return ids;
/* 177 */     } catch (JSONException e) {
/* 178 */       throw new TwitterException.Parsing(json, e);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Number> getFollowerIDs() throws TwitterException {
/* 201 */     return getUserIDs(String.valueOf(this.jtwit.TWITTER_URL) + "/followers/ids.json", null, null);
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
/*     */   public List<Number> getFollowerIDs(String screenName) throws TwitterException {
/* 215 */     return getUserIDs(String.valueOf(this.jtwit.TWITTER_URL) + "/followers/ids.json", screenName, null);
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
/*     */   public List<Number> getFollowerIDs(long userId) throws TwitterException {
/* 227 */     return getUserIDs(String.valueOf(this.jtwit.TWITTER_URL) + "/followers/ids.json", null, Long.valueOf(userId));
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
/*     */   @Deprecated
/*     */   public List<User> getFollowers() throws TwitterException {
/* 240 */     List<Number> ids = getFollowerIDs();
/* 241 */     return getTweeps2(ids);
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
/*     */   public List<User> getFollowers(String username) throws TwitterException {
/* 257 */     List<Number> ids = getFollowerIDs(username);
/* 258 */     return getTweeps2(ids);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Number> getFriendIDs() throws TwitterException {
/* 268 */     return getUserIDs(String.valueOf(this.jtwit.TWITTER_URL) + "/friends/ids.json", null, null);
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
/*     */   public List<Number> getFriendIDs(String screenName) throws TwitterException {
/* 284 */     return getUserIDs(String.valueOf(this.jtwit.TWITTER_URL) + "/friends/ids.json", screenName, null);
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
/*     */   public List<Number> getFriendIDs(long userId) throws TwitterException {
/* 296 */     return getUserIDs(String.valueOf(this.jtwit.TWITTER_URL) + "/friends/ids.json", null, Long.valueOf(userId));
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
/*     */   @Deprecated
/*     */   public List<User> getFriends() throws TwitterException {
/* 316 */     List<Number> ids = getFriendIDs();
/* 317 */     return getTweeps2(ids);
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
/*     */   public List<User> getFriends(String username) throws TwitterException {
/* 333 */     List<Number> ids = getFriendIDs(username);
/* 334 */     return getTweeps2(ids);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<User> getTweeps2(List<Number> ids) {
/* 341 */     if (ids.size() > 100) {
/* 342 */       ids = ids.subList(0, 100);
/*     */     }
/* 344 */     List<User> users = showById(ids);
/* 345 */     return users;
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
/*     */   public List<User> getRelationshipInfo(List<String> screenNames) {
/* 360 */     if (screenNames.size() == 0)
/* 361 */       return Collections.EMPTY_LIST; 
/* 362 */     List<User> users = bulkShow2("/friendships/lookup.json", String.class, 
/* 363 */         screenNames);
/* 364 */     return users;
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
/*     */   public List<User> getRelationshipInfoById(List<? extends Number> userIDs) {
/* 379 */     if (userIDs.size() == 0)
/* 380 */       return Collections.EMPTY_LIST; 
/* 381 */     List<User> users = bulkShow2("/friendships/lookup.json", Number.class, 
/* 382 */         userIDs);
/* 383 */     return users;
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
/*     */   public User getUser(long userId) {
/* 396 */     return show(Long.valueOf(userId));
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
/*     */   public User getUser(String screenName) {
/* 408 */     return show(screenName);
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
/*     */   private List<Number> getUserIDs(String url, String screenName, Long userId) {
/* 423 */     Long cursor = Long.valueOf(-1L);
/* 424 */     List<Number> ids = new ArrayList<Number>();
/* 425 */     if (screenName != null && userId != null) throw new IllegalArgumentException("cannot use both screen_name and user_id when fetching user_ids");
/*     */     
/* 427 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName, "user_id", userId });
/* 428 */     while (cursor.longValue() != 0L && !this.jtwit.enoughResults(ids)) {
/* 429 */       vars.put("cursor", String.valueOf(cursor));
/* 430 */       String json = this.http.getPage(url, vars, this.http.canAuthenticate());
/*     */       
/*     */       try {
/*     */         JSONArray jarr;
/* 434 */         if (json.charAt(0) == '[') {
/* 435 */           jarr = new JSONArray(json);
/* 436 */           cursor = Long.valueOf(0L);
/*     */         } else {
/* 438 */           JSONObject jobj = new JSONObject(json);
/* 439 */           jarr = (JSONArray)jobj.get("ids");
/* 440 */           cursor = new Long(jobj.getString("next_cursor"));
/*     */         } 
/* 442 */         for (int i = 0; i < jarr.length(); i++) {
/* 443 */           ids.add(Long.valueOf(jarr.getLong(i)));
/*     */         }
/* 445 */         if (jarr.length() == 0) {
/*     */           break;
/*     */         }
/*     */       }
/* 449 */       catch (JSONException e) {
/* 450 */         throw new TwitterException.Parsing(json, e);
/*     */       } 
/*     */     } 
/* 453 */     return ids;
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
/*     */   private List<User> getUsers(String url, String screenName) {
/* 466 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName });
/* 467 */     List<User> users = new ArrayList<User>();
/* 468 */     Long cursor = Long.valueOf(-1L);
/* 469 */     while (cursor.longValue() != 0L && !this.jtwit.enoughResults(users)) {
/* 470 */       vars.put("cursor", cursor.toString());
/*     */       
/*     */       try {
/* 473 */         JSONObject jobj = new JSONObject(this.http.getPage(url, vars, 
/* 474 */               this.http.canAuthenticate()));
/* 475 */         users.addAll(User.getUsers(jobj.getString("users")));
/* 476 */         cursor = new Long(jobj.getString("next_cursor"));
/* 477 */       } catch (JSONException e) {
/* 478 */         throw new TwitterException.Parsing(null, e);
/*     */       } 
/*     */     } 
/* 481 */     return users;
/*     */   }
/*     */   
/*     */   public boolean isBlocked(Long userId) {
/*     */     try {
/* 486 */       HashMap<Object, Object> vars = new HashMap<Object, Object>();
/* 487 */       vars.put("user_id", Long.toString(userId.longValue()));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 492 */       String json = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + 
/* 493 */           "/blocks/exists.json", (Map)vars, true);
/* 494 */       return true;
/* 495 */     } catch (E404 e) {
/* 496 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBlocked(String screenName) {
/*     */     try {
/* 502 */       HashMap<Object, Object> vars = new HashMap<Object, Object>();
/* 503 */       vars.put("screen_name", screenName);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 508 */       String json = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + 
/* 509 */           "/blocks/exists.json", (Map)vars, true);
/* 510 */       return true;
/* 511 */     } catch (E404 e) {
/* 512 */       return false;
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
/*     */   public boolean isFollower(String userB) {
/* 524 */     return isFollower(userB, this.jtwit.getScreenName());
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
/*     */   public boolean isFollower(String followerScreenName, String followedScreenName) {
/* 541 */     assert followerScreenName != null && followedScreenName != null;
/*     */ 
/*     */     
/*     */     try {
/* 545 */       Map<String, String> vars = InternalUtils.asMap(new Object[] { "source_screen_name", followerScreenName, "target_screen_name", followedScreenName });
/* 546 */       String page = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + 
/* 547 */           "/friendships/show.json", vars, this.http.canAuthenticate());
/* 548 */       JSONObject jo = new JSONObject(page);
/* 549 */       JSONObject trgt = jo.getJSONObject("relationship").getJSONObject("target");
/* 550 */       boolean fby = trgt.getBoolean("followed_by");
/* 551 */       return fby;
/* 552 */     } catch (E403 e) {
/* 553 */       if (e instanceof TwitterException.SuspendedUser) {
/* 554 */         throw e;
/*     */       }
/*     */ 
/*     */       
/* 558 */       String whoFirst = followedScreenName.equals(this.jtwit.getScreenName()) ? followerScreenName : 
/* 559 */         followedScreenName;
/*     */       
/*     */       try {
/* 562 */         show(whoFirst);
/* 563 */         String whoSecond = whoFirst.equals(followedScreenName) ? followerScreenName : 
/* 564 */           followedScreenName;
/* 565 */         if (whoSecond.equals(this.jtwit.getScreenName()))
/* 566 */           throw e; 
/* 567 */         show(whoSecond);
/* 568 */       } catch (RateLimit rateLimit) {}
/*     */ 
/*     */ 
/*     */       
/* 572 */       throw e;
/* 573 */     } catch (TwitterException e) {
/*     */       
/* 575 */       if (e.getMessage() != null && 
/* 576 */         e.getMessage().contains(
/* 577 */           "Two user ids or screen_names must be supplied"))
/* 578 */         throw new TwitterException("WTF? inputs: follower=" + 
/* 579 */             followerScreenName + ", followed=" + 
/* 580 */             followedScreenName + ", call-by=" + 
/* 581 */             this.jtwit.getScreenName() + "; " + e.getMessage()); 
/* 582 */       throw e;
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
/*     */   public boolean isFollowing(String userB) {
/* 594 */     return isFollower(this.jtwit.getScreenName(), userB);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFollowing(User user) {
/* 603 */     return isFollowing(user.screenName);
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
/*     */   public User leaveNotifications(String screenName) {
/* 619 */     return setNotifications(screenName, Boolean.valueOf(false), null);
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
/*     */   public User setNotifications(String screenName, Boolean device, Boolean retweets) {
/* 631 */     if (device == null && retweets == null) {
/* 632 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 636 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName, "device", device, "retweets", retweets });
/* 637 */     String page = this.http.post(String.valueOf(this.jtwit.TWITTER_URL) + 
/* 638 */         "/friendships/update.json", vars, true);
/*     */     try {
/* 640 */       JSONObject jo = (new JSONObject(page)).getJSONObject("relationship").getJSONObject("target");
/* 641 */       return new User(jo, null);
/* 642 */     } catch (JSONException e) {
/* 643 */       throw new TwitterException.Parsing(page, e);
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
/*     */   public User notify(String username) {
/* 657 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", username });
/* 658 */     String page = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + 
/* 659 */         "/notifications/follow.json", vars, true);
/*     */     try {
/* 661 */       return new User(new JSONObject(page), null);
/* 662 */     } catch (JSONException e) {
/* 663 */       throw new TwitterException.Parsing(page, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public User reportSpammer(String screenName) {
/* 668 */     HashMap<Object, Object> vars = new HashMap<Object, Object>();
/* 669 */     vars.put("screen_name", screenName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 674 */     String json = this.http.post(String.valueOf(this.jtwit.TWITTER_URL) + "/report_spam.json", (Map)vars, 
/* 675 */         true);
/* 676 */     return InternalUtils.user(json);
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
/*     */   public List<User> searchUsers(String searchTerm) {
/* 696 */     return searchUsers(searchTerm, 0);
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
/*     */   public List<User> searchUsers(String searchTerm, int page) {
/* 709 */     assert searchTerm != null;
/* 710 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "q", searchTerm });
/*     */     
/* 712 */     if (page > 1) {
/* 713 */       vars.put("page", Integer.toString(page));
/*     */     }
/* 715 */     if (this.jtwit.count != null && this.jtwit.count.intValue() < 20) {
/* 716 */       vars.put("per_page", String.valueOf(this.jtwit.count));
/*     */     }
/*     */     
/* 719 */     String json = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + "/users/search.json", 
/* 720 */         vars, true);
/* 721 */     List<User> users = User.getUsers(json);
/* 722 */     return users;
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
/*     */   public List<User> show(Collection<String> screenNames) {
/* 739 */     if (screenNames.size() == 0)
/* 740 */       return Collections.EMPTY_LIST; 
/* 741 */     return bulkShow2("/users/lookup.json", String.class, screenNames);
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
/*     */   public User show(Number userId) {
/* 755 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "user_id", userId.toString() });
/* 756 */     String json = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + "/users/show.json", 
/* 757 */         vars, this.http.canAuthenticate());
/*     */     try {
/* 759 */       User user = new User(new JSONObject(json), null);
/* 760 */       return user;
/* 761 */     } catch (JSONException e) {
/* 762 */       throw new TwitterException.Parsing(json, e);
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
/*     */   public User show(String screenName) throws TwitterException, TwitterException.SuspendedUser {
/* 779 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", screenName });
/*     */     
/* 781 */     String json = "";
/*     */     try {
/* 783 */       json = this.http.getPage(String.valueOf(this.jtwit.TWITTER_URL) + "/users/show.json", 
/* 784 */           vars, this.http.canAuthenticate());
/*     */     }
/* 786 */     catch (Exception e) {
/*     */       
/* 788 */       throw new TwitterException.E404("User " + screenName + 
/* 789 */           " does not seem to exist, their user account may have been removed from the service");
/*     */     } 
/*     */     
/* 792 */     if (json.length() == 0)
/* 793 */       throw new TwitterException.E404(String.valueOf(screenName) + 
/* 794 */           " does not seem to exist"); 
/*     */     try {
/* 796 */       User user = new User(new JSONObject(json), null);
/* 797 */       return user;
/* 798 */     } catch (JSONException e) {
/* 799 */       throw new TwitterException.Parsing(json, e);
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
/*     */   public List<User> showById(Collection<? extends Number> userIds) {
/* 812 */     if (userIds.size() == 0)
/* 813 */       return Collections.EMPTY_LIST; 
/* 814 */     return bulkShow2("/users/lookup.json", Number.class, userIds);
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
/*     */   public User stopFollowing(String username) {
/*     */     String page;
/*     */     try {
/* 831 */       Map<String, String> vars = InternalUtils.asMap(new Object[] { "screen_name", username });
/* 832 */       page = this.jtwit.http.post(String.valueOf(this.jtwit.TWITTER_URL) + 
/* 833 */           "/friendships/destroy.json", vars, true);
/*     */ 
/*     */     
/*     */     }
/* 837 */     catch (TwitterException e) {
/*     */       
/* 839 */       if (e.getMessage() != null && 
/* 840 */         e.getMessage().contains("not friends")) {
/* 841 */         return null;
/*     */       }
/* 843 */       throw e;
/*     */     } 
/*     */     
/*     */     try {
/* 847 */       User user = new User(new JSONObject(page), null);
/* 848 */       return user;
/* 849 */     } catch (JSONException e) {
/* 850 */       throw new TwitterException.Parsing(page, e);
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
/*     */   public User stopFollowing(User user) {
/* 862 */     return stopFollowing(user.screenName);
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
/*     */   public User unblock(String screenName) {
/* 875 */     HashMap<Object, Object> vars = new HashMap<Object, Object>();
/* 876 */     vars.put("screen_name", screenName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 881 */     String json = this.http.post(String.valueOf(this.jtwit.TWITTER_URL) + "/blocks/destroy.json", 
/* 882 */         (Map)vars, true);
/* 883 */     return InternalUtils.user(json);
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
/*     */   public boolean userExists(String screenName) {
/*     */     try {
/* 898 */       show(screenName);
/* 899 */     } catch (SuspendedUser e) {
/* 900 */       return false;
/* 901 */     } catch (E404 e) {
/* 902 */       return false;
/*     */     } 
/* 904 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Twitter_Users.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
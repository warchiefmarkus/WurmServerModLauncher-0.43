/*     */ package winterwell.jtwitter;
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import winterwell.json.JSONArray;
/*     */ import winterwell.json.JSONException;
/*     */ import winterwell.json.JSONObject;
/*     */ 
/*     */ public final class User implements Serializable {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public final Date createdAt;
/*     */   public final String description;
/*     */   public final int favoritesCount;
/*     */   private final Boolean followedByYou;
/*     */   public int followersCount;
/*     */   private final Boolean followingYou;
/*     */   public final boolean followRequestSent;
/*     */   public final int friendsCount;
/*     */   public final Long id;
/*     */   String lang;
/*     */   public final int listedCount;
/*     */   public final String location;
/*     */   public final String name;
/*     */   public final boolean notifications;
/*     */   private Place place;
/*     */   
/*     */   static List<User> getUsers(String json) throws TwitterException {
/*  29 */     if (json.trim().equals(""))
/*  30 */       return Collections.emptyList(); 
/*     */     try {
/*  32 */       JSONArray arr = new JSONArray(json);
/*  33 */       return getUsers2(arr);
/*  34 */     } catch (JSONException e) {
/*  35 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */   public final String profileBackgroundColor; public final URI profileBackgroundImageUrl; public final boolean profileBackgroundTile; public URI profileImageUrl; public final String profileLinkColor; public final String profileSidebarBorderColor; public final String profileSidebarFillColor; public final String profileTextColor; public final boolean protectedUser; public final String screenName; public final Status status; public final int statusesCount; public final String timezone; public final double timezoneOffSet; public final boolean verified; public final URI website;
/*     */   static List<User> getUsers2(JSONArray arr) throws JSONException {
/*  40 */     List<User> users = new ArrayList<User>();
/*  41 */     for (int i = 0; i < arr.length(); i++) {
/*  42 */       JSONObject obj = arr.getJSONObject(i);
/*  43 */       User u = new User(obj, null);
/*  44 */       users.add(u);
/*     */     } 
/*  46 */     return users;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLang() {
/*  82 */     return this.lang;
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
/*     */   User(JSONObject obj, Status status) throws TwitterException {
/*     */     try {
/* 163 */       this.id = Long.valueOf(obj.getLong("id"));
/* 164 */       this.name = InternalUtils.unencode(InternalUtils.jsonGet("name", obj));
/* 165 */       String sn = InternalUtils.jsonGet("screen_name", obj);
/* 166 */       this.screenName = Twitter.CASE_SENSITIVE_SCREENNAMES ? sn : sn.toLowerCase();
/*     */       
/* 168 */       Object _locn = Status.jsonGetLocn(obj);
/* 169 */       this.location = (_locn == null) ? null : _locn.toString();
/* 170 */       if (_locn instanceof Place) {
/* 171 */         this.place = (Place)_locn;
/*     */       }
/*     */       
/* 174 */       this.lang = InternalUtils.jsonGet("lang", obj);
/*     */       
/* 176 */       this.description = InternalUtils.unencode(InternalUtils.jsonGet(
/* 177 */             "description", obj));
/* 178 */       String img = InternalUtils.jsonGet("profile_image_url", obj);
/* 179 */       this.profileImageUrl = (img == null) ? null : InternalUtils.URI(img);
/* 180 */       String url = InternalUtils.jsonGet("url", obj);
/* 181 */       this.website = (url == null) ? null : InternalUtils.URI(url);
/* 182 */       this.protectedUser = obj.optBoolean("protected");
/* 183 */       this.followersCount = obj.optInt("followers_count");
/* 184 */       this.profileBackgroundColor = InternalUtils.jsonGet(
/* 185 */           "profile_background_color", obj);
/* 186 */       this.profileLinkColor = InternalUtils.jsonGet("profile_link_color", obj);
/* 187 */       this.profileTextColor = InternalUtils.jsonGet("profile_text_color", obj);
/* 188 */       this.profileSidebarFillColor = InternalUtils.jsonGet(
/* 189 */           "profile_sidebar_fill_color", obj);
/* 190 */       this.profileSidebarBorderColor = InternalUtils.jsonGet(
/* 191 */           "profile_sidebar_border_color", obj);
/* 192 */       this.friendsCount = obj.optInt("friends_count");
/*     */       
/* 194 */       String c = InternalUtils.jsonGet("created_at", obj);
/* 195 */       this.createdAt = (c == null) ? null : InternalUtils.parseDate(c);
/*     */ 
/*     */ 
/*     */       
/* 199 */       this.favoritesCount = obj.optInt("favourites_count");
/* 200 */       String utcOffSet = InternalUtils.jsonGet("utc_offset", obj);
/* 201 */       this.timezoneOffSet = (utcOffSet == null) ? 0.0D : 
/* 202 */         Double.parseDouble(utcOffSet);
/* 203 */       this.timezone = InternalUtils.jsonGet("time_zone", obj);
/* 204 */       img = InternalUtils.jsonGet("profile_background_image_url", obj);
/* 205 */       this.profileBackgroundImageUrl = (img == null) ? null : 
/* 206 */         InternalUtils.URI(img);
/* 207 */       this.profileBackgroundTile = obj.optBoolean("profile_background_tile");
/* 208 */       this.statusesCount = obj.optInt("statuses_count");
/* 209 */       this.notifications = obj.optBoolean("notifications");
/* 210 */       this.verified = obj.optBoolean("verified");
/*     */       
/* 212 */       Object _cons = obj.opt("connections");
/* 213 */       if (_cons instanceof JSONArray) {
/* 214 */         JSONArray cons = (JSONArray)_cons;
/* 215 */         boolean _following = false, _followedBy = false, _followRequested = false;
/* 216 */         for (int i = 0, n = cons.length(); i < n; i++) {
/* 217 */           String ci = cons.getString(i);
/* 218 */           if ("following".equals(ci)) {
/* 219 */             _following = true;
/* 220 */           } else if ("followed_by".equals(ci)) {
/* 221 */             _followedBy = true;
/* 222 */           } else if ("following_requested".equals(ci)) {
/* 223 */             _followRequested = true;
/*     */           } 
/*     */         } 
/* 226 */         this.followedByYou = Boolean.valueOf(_following);
/* 227 */         this.followingYou = Boolean.valueOf(_followedBy);
/* 228 */         this.followRequestSent = _followRequested;
/*     */       } else {
/* 230 */         this.followedByYou = InternalUtils.getOptBoolean(obj, "following");
/*     */ 
/*     */         
/* 233 */         this.followingYou = InternalUtils.getOptBoolean(obj, "followed_by");
/* 234 */         this.followRequestSent = obj.optBoolean("follow_request_sent");
/*     */       } 
/*     */       
/* 237 */       this.listedCount = obj.optInt("listed_count", -1);
/*     */       
/* 239 */       if (status == null) {
/* 240 */         JSONObject s = obj.optJSONObject("status");
/* 241 */         this.status = (s == null) ? null : new Status(s, this);
/*     */       } else {
/* 243 */         this.status = status;
/*     */       } 
/* 245 */     } catch (JSONException e) {
/* 246 */       throw new TwitterException.Parsing(String.valueOf(obj), e);
/* 247 */     } catch (NullPointerException e) {
/* 248 */       throw new TwitterException(e + " from <" + obj + ">, <" + status + 
/* 249 */           ">\n\t" + e.getStackTrace()[0] + "\n\t" + 
/* 250 */           e.getStackTrace()[1]);
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
/*     */   public User(String screenName) {
/* 268 */     this(screenName, (Number)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   User(String screenName, Number id) {
/* 275 */     this.id = (id == null) ? null : Long.valueOf(id.longValue());
/* 276 */     this.name = null;
/* 277 */     if (screenName != null && !Twitter.CASE_SENSITIVE_SCREENNAMES) {
/* 278 */       screenName = screenName.toLowerCase();
/*     */     }
/* 280 */     this.screenName = screenName;
/* 281 */     this.status = null;
/* 282 */     this.location = null;
/* 283 */     this.description = null;
/* 284 */     this.profileImageUrl = null;
/* 285 */     this.website = null;
/* 286 */     this.protectedUser = false;
/* 287 */     this.followersCount = 0;
/* 288 */     this.profileBackgroundColor = null;
/* 289 */     this.profileLinkColor = null;
/* 290 */     this.profileTextColor = null;
/* 291 */     this.profileSidebarFillColor = null;
/* 292 */     this.profileSidebarBorderColor = null;
/* 293 */     this.friendsCount = 0;
/* 294 */     this.createdAt = null;
/* 295 */     this.favoritesCount = 0;
/* 296 */     this.timezoneOffSet = -1.0D;
/* 297 */     this.timezone = null;
/* 298 */     this.profileBackgroundImageUrl = null;
/* 299 */     this.profileBackgroundTile = false;
/* 300 */     this.statusesCount = 0;
/* 301 */     this.notifications = false;
/* 302 */     this.verified = false;
/* 303 */     this.followedByYou = null;
/* 304 */     this.followingYou = null;
/* 305 */     this.followRequestSent = false;
/* 306 */     this.listedCount = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 311 */     if (this == other)
/* 312 */       return true; 
/* 313 */     if (other.getClass() != User.class)
/* 314 */       return false; 
/* 315 */     User ou = (User)other;
/*     */     
/* 317 */     if (this.screenName != null && ou.screenName != null) {
/* 318 */       return this.screenName.equals(ou.screenName);
/*     */     }
/* 320 */     if (this.id != null && ou.id != null) {
/* 321 */       return (this.id == ou.id);
/*     */     }
/* 323 */     return false;
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
/*     */   public Date getCreatedAt() {
/* 336 */     return this.createdAt;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 340 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFavoritesCount() {
/* 349 */     return this.favoritesCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFollowersCount() {
/* 358 */     return this.followersCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFriendsCount() {
/* 367 */     return this.friendsCount;
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
/*     */   public Long getId() {
/* 379 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 387 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 396 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Place getPlace() {
/* 404 */     return this.place;
/*     */   }
/*     */   
/*     */   public String getProfileBackgroundColor() {
/* 408 */     return this.profileBackgroundColor;
/*     */   }
/*     */   
/*     */   public URI getProfileBackgroundImageUrl() {
/* 412 */     return this.profileBackgroundImageUrl;
/*     */   }
/*     */   
/*     */   public URI getProfileImageUrl() {
/* 416 */     return this.profileImageUrl;
/*     */   }
/*     */   
/*     */   public String getProfileLinkColor() {
/* 420 */     return this.profileLinkColor;
/*     */   }
/*     */   
/*     */   public String getProfileSidebarBorderColor() {
/* 424 */     return this.profileSidebarBorderColor;
/*     */   }
/*     */   
/*     */   public String getProfileSidebarFillColor() {
/* 428 */     return this.profileSidebarFillColor;
/*     */   }
/*     */   
/*     */   public String getProfileTextColor() {
/* 432 */     return this.profileTextColor;
/*     */   }
/*     */   
/*     */   public boolean getProtectedUser() {
/* 436 */     return this.protectedUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScreenName() {
/* 441 */     return this.screenName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Status getStatus() {
/* 449 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusesCount() {
/* 458 */     return this.statusesCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTimezone() {
/* 465 */     return this.timezone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTimezoneOffSet() {
/* 474 */     return this.timezoneOffSet;
/*     */   }
/*     */   
/*     */   public URI getWebsite() {
/* 478 */     return this.website;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 484 */     return this.screenName.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDummyObject() {
/* 494 */     return (this.name == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isFollowedByYou() {
/* 504 */     return this.followedByYou;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isFollowingYou() {
/* 514 */     return this.followingYou;
/*     */   }
/*     */   
/*     */   public boolean isNotifications() {
/* 518 */     return this.notifications;
/*     */   }
/*     */   
/*     */   public boolean isProfileBackgroundTile() {
/* 522 */     return this.profileBackgroundTile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProtectedUser() {
/* 529 */     return this.protectedUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVerified() {
/* 537 */     return this.verified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 545 */     return this.screenName;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\User.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
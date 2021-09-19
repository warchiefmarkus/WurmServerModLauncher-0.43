/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public final class Status
/*     */   implements Twitter.ITweet
/*     */ {
/*  30 */   static final Pattern AT_YOU_SIR = Pattern.compile("@(\\w+)");
/*     */   
/*     */   private static final String FAKE = "fake";
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public final Date createdAt;
/*     */   private EnumMap<Twitter.KEntityType, List<Twitter.TweetEntity>> entities;
/*     */   private boolean favorited;
/*     */   public final BigInteger id;
/*     */   public final BigInteger inReplyToStatusId;
/*     */   private String location;
/*     */   
/*     */   static List<Status> getStatuses(String json) throws TwitterException {
/*  44 */     if (json.trim().equals(""))
/*  45 */       return Collections.emptyList(); 
/*     */     try {
/*  47 */       List<Status> tweets = new ArrayList<Status>();
/*  48 */       JSONArray arr = new JSONArray(json);
/*  49 */       for (int i = 0; i < arr.length(); i++) {
/*  50 */         Object ai = arr.get(i);
/*  51 */         if (!JSONObject.NULL.equals(ai)) {
/*     */ 
/*     */           
/*  54 */           JSONObject obj = (JSONObject)ai;
/*  55 */           Status tweet = new Status(obj, null);
/*  56 */           tweets.add(tweet);
/*     */         } 
/*  58 */       }  return tweets;
/*  59 */     } catch (JSONException e) {
/*     */       
/*  61 */       if (json.startsWith("<")) {
/*  62 */         throw new TwitterException.E50X(InternalUtils.stripTags(json));
/*     */       }
/*  64 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Status original;
/*     */   private Place place;
/*     */   public final int retweetCount;
/*     */   boolean sensitive;
/*     */   public final String source;
/*     */   public final String text;
/*     */   public final User user;
/*     */   private String lang;
/*     */   
/*     */   static List<Status> getStatusesFromSearch(Twitter tw, String json) {
/*     */     try {
/*  80 */       JSONObject searchResults = new JSONObject(json);
/*  81 */       List<Status> users = new ArrayList<Status>();
/*  82 */       JSONArray arr = searchResults.getJSONArray("statuses");
/*  83 */       for (int i = 0; i < arr.length(); i++) {
/*  84 */         JSONObject obj = arr.getJSONObject(i);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  89 */         Status s = new Status(obj, null);
/*  90 */         users.add(s);
/*     */       } 
/*  92 */       return users;
/*  93 */     } catch (JSONException e) {
/*  94 */       throw new TwitterException.Parsing(json, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object jsonGetLocn(JSONObject object) throws JSONException {
/* 104 */     String _location = InternalUtils.jsonGet("location", object);
/*     */     
/* 106 */     if (_location != null && _location.length() == 0) {
/* 107 */       _location = null;
/*     */     }
/* 109 */     JSONObject _place = object.optJSONObject("place");
/* 110 */     if (_location != null) {
/*     */       
/* 112 */       Matcher m = InternalUtils.latLongLocn.matcher(_location);
/* 113 */       if (m.matches()) {
/* 114 */         _location = String.valueOf(m.group(2)) + "," + m.group(3);
/*     */       }
/* 116 */       return _location;
/*     */     } 
/*     */ 
/*     */     
/* 120 */     if (_place != null) {
/* 121 */       Place place = new Place(_place);
/* 122 */       return place;
/*     */     } 
/* 124 */     JSONObject geo = object.optJSONObject("geo");
/* 125 */     if (geo != null && geo != JSONObject.NULL) {
/* 126 */       JSONArray latLong = geo.getJSONArray("coordinates");
/* 127 */       _location = latLong.get(0) + "," + latLong.get(1);
/*     */     } 
/*     */     
/* 130 */     return _location;
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
/*     */   public String getLang() {
/* 203 */     return this.lang;
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
/*     */   Status(JSONObject object, User user) throws TwitterException {
/*     */     try {
/* 227 */       String _id = object.optString("id_str");
/* 228 */       this.id = new BigInteger((_id == "") ? object.get("id").toString() : _id);
/*     */       
/* 230 */       JSONObject retweeted = object.optJSONObject("retweeted_status");
/* 231 */       if (retweeted != null) {
/* 232 */         this.original = new Status(retweeted, null);
/*     */       }
/*     */ 
/*     */       
/* 236 */       String _rawtext = InternalUtils.jsonGet("text", object);
/* 237 */       String _text = _rawtext;
/*     */       
/* 239 */       boolean truncated = object.optBoolean("truncated");
/* 240 */       if (!truncated && this.original != null) {
/* 241 */         truncated = !(!_text.endsWith("…") && !_text.endsWith("..."));
/*     */       }
/* 243 */       String rtStart = null;
/* 244 */       if (truncated && this.original != null && _text.startsWith("RT ")) {
/* 245 */         rtStart = "RT @" + this.original.getUser() + ": ";
/* 246 */         _text = String.valueOf(rtStart) + this.original.getText();
/*     */       } else {
/* 248 */         _text = InternalUtils.unencode(_text);
/*     */       } 
/* 250 */       this.text = _text;
/*     */ 
/*     */       
/* 253 */       String c = InternalUtils.jsonGet("created_at", object);
/* 254 */       this.createdAt = InternalUtils.parseDate(c);
/*     */ 
/*     */       
/* 257 */       String src = InternalUtils.jsonGet("source", object);
/* 258 */       this.source = (src != null && src.contains("&lt;")) ? InternalUtils.unencode(src) : src;
/*     */       
/* 260 */       String irt = InternalUtils.jsonGet("in_reply_to_status_id", object);
/* 261 */       if (irt == null || irt.length() == 0) {
/*     */ 
/*     */         
/* 264 */         this.inReplyToStatusId = (this.original == null) ? null : this.original.getId();
/*     */       } else {
/* 266 */         this.inReplyToStatusId = new BigInteger(irt);
/*     */       } 
/* 268 */       this.favorited = object.optBoolean("favorited");
/*     */ 
/*     */       
/* 271 */       if (user != null) {
/* 272 */         this.user = user;
/*     */       } else {
/* 274 */         JSONObject jsonUser = object.optJSONObject("user");
/*     */ 
/*     */         
/* 277 */         if (jsonUser == null) {
/* 278 */           this.user = null;
/* 279 */         } else if (jsonUser.opt("screen_name") == null) {
/*     */ 
/*     */ 
/*     */           
/* 283 */           String _uid = jsonUser.optString("id_str");
/* 284 */           BigInteger userId = new BigInteger((_uid == "") ? object.get(
/* 285 */                 "id").toString() : _uid);
/* 286 */           this.user = new User(null, userId);
/*     */         } else {
/*     */           
/* 289 */           this.user = new User(jsonUser, this);
/*     */         } 
/*     */       } 
/*     */       
/* 293 */       Object _locn = jsonGetLocn(object);
/* 294 */       this.location = (_locn == null) ? null : _locn.toString();
/* 295 */       if (_locn instanceof Place) {
/* 296 */         this.place = (Place)_locn;
/*     */       }
/*     */       
/* 299 */       String _lang = object.optString("lang");
/* 300 */       this.lang = "und".equals(_lang) ? null : _lang;
/*     */       
/* 302 */       this.retweetCount = object.optInt("retweet_count", -1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 308 */       JSONObject jsonEntities = object.optJSONObject("entities");
/*     */       
/* 310 */       if (jsonEntities != null) {
/* 311 */         this.entities = new EnumMap<Twitter.KEntityType, List<Twitter.TweetEntity>>(
/* 312 */             Twitter.KEntityType.class);
/* 313 */         setupEntities(_rawtext, rtStart, jsonEntities);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 323 */       this.sensitive = object.optBoolean("possibly_sensitive");
/* 324 */     } catch (JSONException e) {
/* 325 */       throw new TwitterException.Parsing(null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupEntities(String _rawtext, String rtStart, JSONObject jsonEntities) {
/* 331 */     if (rtStart != null) {
/*     */       
/* 333 */       int rt = rtStart.length(); byte b1; int j; Twitter.KEntityType[] arrayOfKEntityType1;
/* 334 */       for (j = (arrayOfKEntityType1 = Twitter.KEntityType.values()).length, b1 = 0; b1 < j; ) { Twitter.KEntityType type = arrayOfKEntityType1[b1];
/* 335 */         List<Twitter.TweetEntity> es = this.original.getTweetEntities(type);
/* 336 */         if (es != null) {
/* 337 */           ArrayList<Twitter.TweetEntity> rtEs = new ArrayList(es.size());
/* 338 */           for (Twitter.TweetEntity e : es) {
/* 339 */             Twitter.TweetEntity rte = new Twitter.TweetEntity(this, e.type, 
/*     */                 
/* 341 */                 Math.min(rt + e.start, this.text.length()), Math.min(rt + e.end, this.text.length()), e.display);
/* 342 */             rtEs.add(rte);
/*     */           } 
/* 344 */           this.entities.put(type, rtEs);
/*     */         }  b1++; }
/*     */        return;
/*     */     }  byte b; int i;
/*     */     Twitter.KEntityType[] arrayOfKEntityType;
/* 349 */     for (i = (arrayOfKEntityType = Twitter.KEntityType.values()).length, b = 0; b < i; ) { Twitter.KEntityType type = arrayOfKEntityType[b];
/* 350 */       List<Twitter.TweetEntity> es = Twitter.TweetEntity.parse(this, _rawtext, type, 
/* 351 */           jsonEntities);
/* 352 */       this.entities.put(type, es);
/*     */       b++; }
/*     */   
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
/*     */   @Deprecated
/*     */   public Status(User user, String text, Number id, Date createdAt) {
/* 375 */     this.text = text;
/* 376 */     this.user = user;
/* 377 */     this.createdAt = createdAt;
/* 378 */     this.id = (id == null) ? null : (
/* 379 */       (id instanceof BigInteger) ? (BigInteger)id : new BigInteger(
/* 380 */         id.toString()));
/* 381 */     this.inReplyToStatusId = null;
/* 382 */     this.source = "fake";
/* 383 */     this.retweetCount = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 391 */     if (this == obj)
/* 392 */       return true; 
/* 393 */     if (obj == null)
/* 394 */       return false; 
/* 395 */     if (getClass() != obj.getClass())
/* 396 */       return false; 
/* 397 */     Status other = (Status)obj;
/* 398 */     return this.id.equals(other.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreatedAt() {
/* 403 */     return this.createdAt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getId() {
/* 411 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 416 */     return this.location;
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
/*     */   public List<String> getMentions() {
/* 429 */     Matcher m = AT_YOU_SIR.matcher(this.text);
/* 430 */     List<String> list = new ArrayList<String>(2);
/* 431 */     while (m.find()) {
/*     */       
/* 433 */       if (m.start() != 0 && 
/* 434 */         Character.isLetterOrDigit(this.text.charAt(m.start() - 1))) {
/*     */         continue;
/*     */       }
/* 437 */       String mention = m.group(1);
/*     */       
/* 439 */       if (!Twitter.CASE_SENSITIVE_SCREENNAMES) {
/* 440 */         mention = mention.toLowerCase();
/*     */       }
/* 442 */       list.add(mention);
/*     */     } 
/* 444 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Status getOriginal() {
/* 452 */     return this.original;
/*     */   }
/*     */ 
/*     */   
/*     */   public Place getPlace() {
/* 457 */     return this.place;
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
/*     */   public String getSource() {
/* 469 */     return InternalUtils.stripTags(this.source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/* 476 */     return this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Twitter.TweetEntity> getTweetEntities(Twitter.KEntityType type) {
/* 481 */     return (this.entities == null) ? null : this.entities.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public User getUser() {
/* 486 */     return this.user;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 491 */     return this.id.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFavorite() {
/* 498 */     return this.favorited;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSensitive() {
/* 509 */     return this.sensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 519 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayText() {
/* 529 */     return getDisplayText2(this);
/*     */   }
/*     */   
/*     */   static String getDisplayText2(Twitter.ITweet tweet) {
/* 533 */     List<Twitter.TweetEntity> es = tweet.getTweetEntities(Twitter.KEntityType.urls);
/* 534 */     String _text = tweet.getText();
/* 535 */     if (es == null || es.size() == 0)
/*     */     {
/* 537 */       return _text;
/*     */     }
/* 539 */     StringBuilder sb = new StringBuilder(200);
/* 540 */     int i = 0;
/* 541 */     for (Twitter.TweetEntity entity : es) {
/* 542 */       sb.append(_text.substring(i, entity.start));
/* 543 */       sb.append(entity.displayVersion());
/* 544 */       i = entity.end;
/*     */     } 
/* 546 */     if (i < _text.length()) {
/* 547 */       sb.append(_text.substring(i));
/*     */     }
/* 549 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Status.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TwitterList
/*     */   extends AbstractList<User>
/*     */ {
/*     */   private boolean _private;
/*     */   
/*     */   public static TwitterList get(String ownerScreenName, String slug, Twitter jtwit) {
/*  56 */     return new TwitterList(ownerScreenName, slug, jtwit);
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
/*     */   public static TwitterList get(Number id, Twitter jtwit) {
/*  71 */     return new TwitterList(id, jtwit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private long cursor = -1L;
/*     */ 
/*     */   
/*     */   private String description;
/*     */ 
/*     */   
/*     */   private final Twitter.IHttpClient http;
/*     */ 
/*     */   
/*     */   private Number id;
/*     */   
/*     */   private final Twitter jtwit;
/*     */   
/*  92 */   private int memberCount = -1;
/*     */ 
/*     */   
/*     */   private String name;
/*     */ 
/*     */   
/*     */   private User owner;
/*     */ 
/*     */   
/*     */   private String slug;
/*     */   
/*     */   private int subscriberCount;
/*     */   
/* 105 */   private final List<User> users = new ArrayList<User>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TwitterList(JSONObject json, Twitter jtwit) throws JSONException {
/* 115 */     this.jtwit = jtwit;
/* 116 */     this.http = jtwit.getHttpClient();
/* 117 */     init2(json);
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
/*     */   @Deprecated
/*     */   public TwitterList(String ownerScreenName, String slug, Twitter jtwit) {
/* 144 */     assert ownerScreenName != null && slug != null && jtwit != null;
/* 145 */     this.jtwit = jtwit;
/* 146 */     this.owner = new User(ownerScreenName);
/* 147 */     this.name = slug;
/* 148 */     this.slug = slug;
/* 149 */     this.http = jtwit.getHttpClient();
/* 150 */     init();
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
/*     */   public TwitterList(String listName, Twitter jtwit, boolean isPublic, String description) {
/* 167 */     assert listName != null && jtwit != null;
/* 168 */     this.jtwit = jtwit;
/* 169 */     String ownerScreenName = jtwit.getScreenName();
/* 170 */     assert ownerScreenName != null;
/* 171 */     this.name = listName;
/* 172 */     this.slug = listName;
/* 173 */     this.http = jtwit.getHttpClient();
/*     */     
/* 175 */     String url = String.valueOf(jtwit.TWITTER_URL) + "/lists/create.json";
/*     */ 
/*     */     
/* 178 */     Map<String, String> vars = InternalUtils.asMap(new Object[] { "name", listName, "mode", isPublic ? "public" : "private", "description", description });
/* 179 */     String json = this.http.post(url, vars, true);
/*     */     try {
/* 181 */       JSONObject jobj = new JSONObject(json);
/* 182 */       init2(jobj);
/* 183 */     } catch (JSONException e) {
/* 184 */       throw new TwitterException("Could not parse response: " + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TwitterList(Number id, Twitter jtwit) {
/* 189 */     assert id != null && jtwit != null;
/* 190 */     this.jtwit = jtwit;
/* 191 */     this.id = id;
/* 192 */     this.http = jtwit.getHttpClient();
/* 193 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(User user) {
/* 203 */     if (this.users.contains(user))
/* 204 */       return false; 
/* 205 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/members/create.json";
/* 206 */     Map<String, String> map = getListVars();
/* 207 */     map.put("screen_name", user.screenName);
/* 208 */     String json = this.http.post(url, map, true);
/*     */     
/*     */     try {
/* 211 */       JSONObject jobj = new JSONObject(json);
/* 212 */       this.memberCount = jobj.getInt("member_count");
/*     */       
/* 214 */       this.users.add(user);
/* 215 */       return true;
/* 216 */     } catch (JSONException e) {
/* 217 */       throw new TwitterException("Could not parse response: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends User> newUsers) {
/* 223 */     List<User> newUsersList = new ArrayList<User>(newUsers);
/* 224 */     newUsersList.removeAll(this.users);
/* 225 */     if (newUsersList.size() == 0)
/* 226 */       return false; 
/* 227 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/members/create_all.json";
/* 228 */     Map<String, String> map = getListVars();
/* 229 */     int batchSize = 100;
/* 230 */     for (int i = 0; i < this.users.size(); i += batchSize) {
/* 231 */       int last = i + batchSize;
/* 232 */       String names = InternalUtils.join(newUsersList, i, last);
/* 233 */       map.put("screen_name", names);
/* 234 */       String json = this.http.post(url, map, true);
/*     */       
/*     */       try {
/* 237 */         JSONObject jobj = new JSONObject(json);
/* 238 */         this.memberCount = jobj.getInt("member_count");
/* 239 */       } catch (JSONException e) {
/* 240 */         throw new TwitterException("Could not parse response: " + e);
/*     */       } 
/*     */     } 
/*     */     
/* 244 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 254 */     String URL = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/destroy.json";
/* 255 */     this.http.post(URL, getListVars(), true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public User get(int index) {
/* 261 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/members.json";
/* 262 */     Map<String, String> vars = getListVars();
/* 263 */     while (this.users.size() < index + 1 && this.cursor != 0L) {
/* 264 */       vars.put("cursor", Long.toString(this.cursor));
/* 265 */       String json = this.http.getPage(url, vars, true);
/*     */       try {
/* 267 */         JSONObject jobj = new JSONObject(json);
/* 268 */         JSONArray jarr = (JSONArray)jobj.get("users");
/* 269 */         List<User> users1page = User.getUsers(jarr.toString());
/* 270 */         this.users.addAll(users1page);
/* 271 */         this.cursor = (new Long(jobj.getString("next_cursor"))).longValue();
/* 272 */       } catch (JSONException e) {
/* 273 */         throw new TwitterException("Could not parse user list" + e);
/*     */       } 
/*     */     } 
/* 276 */     return this.users.get(index);
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 280 */     init();
/* 281 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> getListVars() {
/* 288 */     Map<Object, Object> vars = new HashMap<Object, Object>();
/* 289 */     if (this.id != null) {
/* 290 */       vars.put("list_id", this.id);
/* 291 */       return (Map)vars;
/*     */     } 
/* 293 */     vars.put("owner_screen_name", this.owner.screenName);
/* 294 */     vars.put("slug", this.slug);
/* 295 */     return (Map)vars;
/*     */   }
/*     */   
/*     */   public Number getId() {
/* 299 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 303 */     return this.name;
/*     */   }
/*     */   
/*     */   public User getOwner() {
/* 307 */     return this.owner;
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
/*     */   public List<Status> getStatuses() throws TwitterException {
/* 320 */     Map<String, String> vars = getListVars();
/*     */     
/* 322 */     this.jtwit.addStandardishParameters(vars);
/*     */     
/* 324 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/statuses.json";
/* 325 */     return this.jtwit.getStatuses(url, vars, true);
/*     */   }
/*     */   
/*     */   public int getSubscriberCount() {
/* 329 */     init();
/* 330 */     return this.subscriberCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<User> getSubscribers() {
/* 338 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/subscribers.json";
/* 339 */     Map<String, String> vars = getListVars();
/* 340 */     String json = this.http.getPage(url, vars, true);
/*     */     try {
/* 342 */       JSONObject jobj = new JSONObject(json);
/* 343 */       JSONArray jsonUsers = jobj.getJSONArray("users");
/* 344 */       return User.getUsers2(jsonUsers);
/* 345 */     } catch (JSONException e) {
/* 346 */       throw new TwitterException("Could not parse response: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 354 */     if (this.memberCount != -1)
/*     */       return; 
/* 356 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/show.json";
/* 357 */     Map<String, String> vars = getListVars();
/* 358 */     String json = this.http.getPage(url, vars, true);
/*     */     try {
/* 360 */       JSONObject jobj = new JSONObject(json);
/* 361 */       init2(jobj);
/* 362 */     } catch (JSONException e) {
/* 363 */       throw new TwitterException("Could not parse response: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void init2(JSONObject jobj) throws JSONException {
/* 369 */     this.memberCount = jobj.getInt("member_count");
/* 370 */     this.subscriberCount = jobj.getInt("subscriber_count");
/* 371 */     this.name = jobj.getString("name");
/* 372 */     this.slug = jobj.getString("slug");
/* 373 */     this.id = Long.valueOf(jobj.getLong("id"));
/* 374 */     this._private = "private".equals(jobj.optString("mode"));
/* 375 */     this.description = jobj.optString("description");
/* 376 */     JSONObject user = jobj.getJSONObject("user");
/* 377 */     this.owner = new User(user, null);
/*     */   }
/*     */   
/*     */   public boolean isPrivate() {
/* 381 */     init();
/* 382 */     return this._private;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/*     */     try {
/* 393 */       User user = (User)o;
/* 394 */       String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/members/destroy.json";
/* 395 */       Map<String, String> map = getListVars();
/* 396 */       map.put("screen_name", user.screenName);
/* 397 */       String json = this.http.post(url, map, true);
/*     */       
/* 399 */       JSONObject jobj = new JSONObject(json);
/* 400 */       this.memberCount = jobj.getInt("member_count");
/*     */       
/* 402 */       this.users.remove(user);
/* 403 */       return true;
/* 404 */     } catch (JSONException e) {
/* 405 */       throw new TwitterException("Could not parse response: " + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 410 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/update.json";
/* 411 */     Map<String, String> vars = getListVars();
/* 412 */     vars.put("description", description);
/* 413 */     String json = this.http.getPage(url, vars, true);
/*     */     try {
/* 415 */       JSONObject jobj = new JSONObject(json);
/* 416 */       init2(jobj);
/* 417 */     } catch (JSONException e) {
/* 418 */       throw new TwitterException("Could not parse response: " + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setPrivate(boolean isPrivate) {
/* 423 */     String url = String.valueOf(this.jtwit.TWITTER_URL) + "/lists/update.json";
/* 424 */     Map<String, String> vars = getListVars();
/* 425 */     vars.put("mode", isPrivate ? "private" : "public");
/* 426 */     String json = this.http.getPage(url, vars, true);
/*     */     try {
/* 428 */       JSONObject jobj = new JSONObject(json);
/* 429 */       init2(jobj);
/* 430 */     } catch (JSONException e) {
/* 431 */       throw new TwitterException("Could not parse response: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 437 */     init();
/* 438 */     return this.memberCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 443 */     return String.valueOf(getClass().getSimpleName()) + "[" + this.owner + "." + this.name + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\TwitterList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
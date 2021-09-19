/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TwitterEvent
/*     */ {
/*     */   public final Date createdAt;
/*     */   public final User source;
/*     */   public final User target;
/*     */   private Object targetObject;
/*     */   public final String type;
/*     */   
/*     */   TwitterEvent(Date createdAt, User source, String type, User target, Object targetObject) {
/*  62 */     this.createdAt = createdAt;
/*  63 */     this.source = source;
/*  64 */     this.type = type;
/*  65 */     this.target = target;
/*  66 */     this.targetObject = targetObject;
/*     */   }
/*     */   
/*     */   public TwitterEvent(JSONObject jo, Twitter jtwit) throws JSONException {
/*  70 */     this.type = jo.getString("event");
/*  71 */     this.target = new User(jo.getJSONObject("target"), null);
/*  72 */     this.source = new User(jo.getJSONObject("source"), null);
/*  73 */     this.createdAt = InternalUtils.parseDate(jo.getString("created_at"));
/*     */     
/*  75 */     JSONObject to = jo.optJSONObject("target_object");
/*  76 */     if (to == null)
/*     */       return; 
/*  78 */     if (to.has("member_count")) {
/*  79 */       this.targetObject = new TwitterList(to, jtwit);
/*     */       return;
/*     */     } 
/*     */     try {
/*  83 */       this.targetObject = new Status(to, null);
/*  84 */     } catch (Exception ex) {
/*  85 */       this.targetObject = to;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Date getCreatedAt() {
/*  90 */     return this.createdAt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User getSource() {
/*  97 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User getTarget() {
/* 104 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTargetObject() {
/* 113 */     return this.targetObject;
/*     */   }
/*     */   
/*     */   public String getType() {
/* 117 */     return this.type;
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
/*     */   public boolean is(String type) {
/* 130 */     return this.type.equals(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     return this.source + " " + this.type + " " + this.target + " " + getTargetObject();
/*     */   }
/*     */   
/*     */   public static interface Type {
/*     */     public static final String ADDED_TO_LIST = "list_member_added";
/*     */     public static final String FAVORITE = "favorite";
/*     */     public static final String FOLLOW = "follow";
/*     */     public static final String LIST_CREATED = "list_created";
/*     */     public static final String REMOVED_FROM_LIST = "list_member_removed";
/*     */     public static final String UNFAVORITE = "unfavorite";
/*     */     public static final String USER_UPDATE = "user_update";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\TwitterEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
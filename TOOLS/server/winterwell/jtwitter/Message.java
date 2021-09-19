/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import winterwell.json.JSONArray;
/*     */ import winterwell.json.JSONException;
/*     */ import winterwell.json.JSONObject;
/*     */ 
/*     */ 
/*     */ public final class Message
/*     */   implements Twitter.ITweet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Date createdAt;
/*     */   private EnumMap<Twitter.KEntityType, List<Twitter.TweetEntity>> entities;
/*     */   public final Number id;
/*     */   public Number inReplyToMessageId;
/*     */   private String location;
/*     */   private Place place;
/*     */   private final User recipient;
/*     */   private final User sender;
/*     */   public final String text;
/*     */   
/*     */   public String getDisplayText() {
/*  29 */     return Status.getDisplayText2(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static List<Message> getMessages(String json) throws TwitterException {
/*  39 */     if (json.trim().equals(""))
/*  40 */       return Collections.emptyList(); 
/*     */     try {
/*  42 */       List<Message> msgs = new ArrayList<Message>();
/*  43 */       JSONArray arr = new JSONArray(json);
/*  44 */       for (int i = 0; i < arr.length(); i++) {
/*  45 */         JSONObject obj = arr.getJSONObject(i);
/*  46 */         Message u = new Message(obj);
/*  47 */         msgs.add(u);
/*     */       } 
/*  49 */       return msgs;
/*  50 */     } catch (JSONException e) {
/*  51 */       throw new TwitterException.Parsing(json, e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Message(User dummyUser, Number id) {
/*  80 */     this.sender = dummyUser;
/*  81 */     this.id = id;
/*  82 */     this.recipient = null;
/*  83 */     this.createdAt = null;
/*  84 */     this.text = null;
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
/*     */   Message(JSONObject obj) throws JSONException, TwitterException {
/*  96 */     this.id = Long.valueOf(obj.getLong("id"));
/*  97 */     String _text = obj.getString("text");
/*  98 */     this.text = InternalUtils.unencode(_text);
/*  99 */     String c = InternalUtils.jsonGet("created_at", obj);
/* 100 */     this.createdAt = InternalUtils.parseDate(c);
/* 101 */     this.sender = new User(obj.getJSONObject("sender"), null);
/*     */     
/* 103 */     Object recip = obj.opt("recipient");
/* 104 */     if (recip instanceof JSONObject) {
/*     */       
/* 106 */       this.recipient = new User((JSONObject)recip, null);
/*     */     } else {
/* 108 */       this.recipient = null;
/*     */     } 
/* 110 */     JSONObject jsonEntities = obj.optJSONObject("entities");
/* 111 */     if (jsonEntities != null) {
/*     */       
/* 113 */       this.entities = new EnumMap<Twitter.KEntityType, List<Twitter.TweetEntity>>(
/* 114 */           Twitter.KEntityType.class); byte b; int i; Twitter.KEntityType[] arrayOfKEntityType;
/* 115 */       for (i = (arrayOfKEntityType = Twitter.KEntityType.values()).length, b = 0; b < i; ) { Twitter.KEntityType type = arrayOfKEntityType[b];
/* 116 */         List<Twitter.TweetEntity> es = Twitter.TweetEntity.parse(this, _text, type, 
/* 117 */             jsonEntities);
/* 118 */         this.entities.put(type, es);
/*     */         b++; }
/*     */     
/*     */     } 
/* 122 */     Object _locn = Status.jsonGetLocn(obj);
/* 123 */     this.location = (_locn == null) ? null : _locn.toString();
/* 124 */     if (_locn instanceof Place) {
/* 125 */       this.place = (Place)_locn;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 134 */     if (this == obj)
/* 135 */       return true; 
/* 136 */     if (obj == null)
/* 137 */       return false; 
/* 138 */     if (getClass() != obj.getClass())
/* 139 */       return false; 
/* 140 */     Message other = (Message)obj;
/* 141 */     return this.id.equals(other.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreatedAt() {
/* 146 */     return this.createdAt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getId() {
/* 154 */     if (this.id instanceof Long) {
/* 155 */       return BigInteger.valueOf(this.id.longValue());
/*     */     }
/* 157 */     return (BigInteger)this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 162 */     return this.location;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getMentions() {
/* 167 */     return Collections.singletonList(this.recipient.screenName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Place getPlace() {
/* 172 */     return this.place;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User getRecipient() {
/* 179 */     return this.recipient;
/*     */   }
/*     */   
/*     */   public User getSender() {
/* 183 */     return this.sender;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText() {
/* 188 */     return this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Twitter.TweetEntity> getTweetEntities(Twitter.KEntityType type) {
/* 193 */     return (this.entities == null) ? null : this.entities.get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User getUser() {
/* 201 */     return getSender();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 206 */     return this.id.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     return this.text;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Message.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
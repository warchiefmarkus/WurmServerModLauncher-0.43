/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Random;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AStream
/*     */   implements Closeable
/*     */ {
/*     */   public static interface IListen
/*     */   {
/*     */     boolean processEvent(TwitterEvent param1TwitterEvent);
/*     */     
/*     */     boolean processSystemEvent(Object[] param1ArrayOfObject);
/*     */     
/*     */     boolean processTweet(Twitter.ITweet param1ITweet);
/*     */   }
/*     */   
/*     */   public static final class Outage
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     public final BigInteger sinceId;
/*     */     public final long untilTime;
/*     */     
/*     */     public Outage(BigInteger sinceId, long untilTime) {
/*  95 */       this.sinceId = sinceId;
/*  96 */       this.untilTime = untilTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 101 */       return "Outage[id:" + this.sinceId + " to time:" + this.untilTime + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static int MAX_BUFFER = 10000;
/*     */   
/*     */   private static final int MAX_WAIT_SECONDS = 900;
/*     */   
/*     */   boolean autoReconnect;
/*     */   
/*     */   final Twitter.IHttpClient client;
/*     */   
/*     */   static int forgetIfFull(List incoming) {
/* 117 */     if (incoming.size() < MAX_BUFFER)
/* 118 */       return 0; 
/* 119 */     int chop = MAX_BUFFER / 10;
/* 120 */     for (int i = 0; i < chop; i++) {
/* 121 */       incoming.remove(0);
/*     */     }
/* 123 */     return chop;
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
/*     */   static Object read3_parse(JSONObject jo, Twitter jtwitr) throws JSONException {
/* 136 */     if (jo.has("text")) {
/* 137 */       Status tweet = new Status(jo, null);
/* 138 */       return tweet;
/*     */     } 
/*     */     
/* 141 */     if (jo.has("direct_message")) {
/* 142 */       Message dm = new Message(jo.getJSONObject("direct_message"));
/* 143 */       return dm;
/*     */     } 
/*     */ 
/*     */     
/* 147 */     String eventType = jo.optString("event");
/* 148 */     if (eventType != "") {
/* 149 */       TwitterEvent event = new TwitterEvent(jo, jtwitr);
/* 150 */       return event;
/*     */     } 
/*     */     
/* 153 */     JSONObject del = jo.optJSONObject("delete");
/* 154 */     if (del != null) {
/* 155 */       Twitter.ITweet deadTweet; boolean isDM = false;
/* 156 */       JSONObject s = del.optJSONObject("status");
/* 157 */       if (s == null) {
/* 158 */         s = del.getJSONObject("direct_message");
/* 159 */         isDM = true;
/*     */       } 
/* 161 */       BigInteger id = new BigInteger(s.getString("id_str"));
/* 162 */       BigInteger userId = new BigInteger(s.getString("user_id"));
/*     */       
/* 164 */       User dummyUser = new User(null, userId);
/* 165 */       if (isDM) {
/* 166 */         deadTweet = new Message(dummyUser, id);
/*     */       } else {
/* 168 */         deadTweet = new Status(dummyUser, null, id, null);
/*     */       } 
/* 170 */       return new Object[] { "delete", deadTweet, userId };
/*     */     } 
/*     */     
/* 173 */     JSONObject limit = jo.optJSONObject("limit");
/* 174 */     if (limit != null) {
/* 175 */       int cnt = limit.optInt("track");
/* 176 */       if (cnt == 0) {
/* 177 */         System.out.println(jo);
/*     */       }
/* 179 */       return new Object[] { "limit", Integer.valueOf(cnt) };
/*     */     } 
/*     */     
/* 182 */     JSONObject disconnect = jo.optJSONObject("disconnect");
/* 183 */     if (disconnect != null) {
/* 184 */       return new Object[] { "disconnect", disconnect };
/*     */     }
/*     */     
/* 187 */     System.out.println(jo);
/* 188 */     return new Object[] { "unknown", jo };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   List<TwitterEvent> events = new ArrayList<TwitterEvent>();
/*     */ 
/*     */ 
/*     */   
/*     */   boolean fillInFollows = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private int forgotten;
/*     */ 
/*     */   
/*     */   List<Long> friends;
/*     */ 
/*     */   
/*     */   final Twitter jtwit;
/*     */ 
/*     */   
/* 212 */   private BigInteger lastId = BigInteger.ZERO;
/*     */   
/* 214 */   final List<IListen> listeners = new ArrayList<IListen>(0);
/*     */   
/* 216 */   final List<Outage> outages = Collections.synchronizedList(new ArrayList<Outage>());
/*     */   
/*     */   int previousCount;
/*     */   
/*     */   StreamGobbler readThread;
/*     */   
/*     */   InputStream stream;
/*     */   
/* 224 */   List<Object[]> sysEvents = new ArrayList();
/*     */   
/* 226 */   List<Twitter.ITweet> tweets = new ArrayList<Twitter.ITweet>();
/*     */ 
/*     */ 
/*     */   
/*     */   boolean listenersOnly;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AStream(Twitter jtwit) {
/* 236 */     this.client = jtwit.getHttpClient();
/* 237 */     this.jtwit = jtwit;
/*     */ 
/*     */     
/* 240 */     this.client.setTimeout(91000);
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
/*     */   public void addListener(IListen listener) {
/* 254 */     synchronized (this.listeners) {
/*     */       
/* 256 */       this.listeners.remove(listener);
/*     */       
/* 258 */       this.listeners.add(0, listener);
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
/*     */   public void addOutage(Outage outage) {
/* 272 */     for (int i = 0; i < this.outages.size(); i++) {
/* 273 */       Outage o = this.outages.get(i);
/* 274 */       if (o.sinceId.compareTo(outage.sinceId) > 0) {
/*     */         
/* 276 */         this.outages.add(i, outage);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 281 */     this.outages.add(outage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 288 */     this.outages.clear();
/* 289 */     popEvents();
/* 290 */     popSystemEvents();
/* 291 */     popTweets();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 302 */     if (this.readThread != null && Thread.currentThread() != this.readThread) {
/* 303 */       this.readThread.pleaseStop();
/*     */       
/* 305 */       if (this.readThread.isAlive()) {
/*     */         try {
/* 307 */           Thread.sleep(100L);
/* 308 */         } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */         
/* 311 */         this.readThread.interrupt();
/*     */       } 
/* 313 */       this.readThread = null;
/*     */     } 
/* 315 */     InternalUtils.close(this.stream);
/* 316 */     this.stream = null;
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
/*     */   public synchronized void connect() throws TwitterException {
/* 331 */     if (isConnected()) {
/*     */       return;
/*     */     }
/* 334 */     close();
/*     */     
/* 336 */     assert this.readThread == null || this.readThread.stream == this : this;
/*     */     
/* 338 */     HttpURLConnection con = null;
/*     */     
/*     */     try {
/* 341 */       con = connect2();
/* 342 */       this.stream = con.getInputStream();
/* 343 */       if (this.readThread == null) {
/* 344 */         this.readThread = new StreamGobbler(this);
/* 345 */         this.readThread.setName("Gobble:" + toString());
/* 346 */         this.readThread.start();
/*     */       } else {
/*     */         
/* 349 */         assert Thread.currentThread() == this.readThread : this;
/* 350 */         assert this.readThread.stream == this : this.readThread;
/*     */       } 
/*     */       
/* 353 */       if (isConnected())
/*     */         return; 
/* 355 */       Thread.sleep(10L);
/* 356 */       if (!isConnected()) {
/* 357 */         throw new TwitterException(this.readThread.ex);
/*     */       }
/* 359 */     } catch (Exception e) {
/* 360 */       if (e instanceof TwitterException) {
/* 361 */         throw (TwitterException)e;
/*     */       }
/*     */ 
/*     */       
/* 365 */       throw new TwitterException(e);
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
/*     */   abstract HttpURLConnection connect2() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Exception fillInOutages() throws UnsupportedOperationException {
/* 390 */     if (this.outages.size() == 0)
/* 391 */       return null; 
/* 392 */     Outage[] outs = this.outages.<Outage>toArray(new Outage[0]);
/*     */     
/* 394 */     Twitter jtwit2 = new Twitter(this.jtwit);
/* 395 */     Exception ex = null; byte b; int i; Outage[] arrayOfOutage1;
/* 396 */     for (i = (arrayOfOutage1 = outs).length, b = 0; b < i; ) { Outage outage = arrayOfOutage1[b];
/*     */       
/* 398 */       if (System.currentTimeMillis() - outage.untilTime >= 60000L) {
/*     */ 
/*     */         
/* 401 */         boolean ok = this.outages.remove(outage);
/* 402 */         if (ok)
/*     */           try {
/* 404 */             jtwit2.setSinceId(outage.sinceId);
/* 405 */             jtwit2.setUntilDate(new Date(outage.untilTime));
/* 406 */             jtwit2.setMaxResults(100000);
/*     */             
/* 408 */             fillInOutages2(jtwit2, outage);
/*     */           }
/* 410 */           catch (Throwable e) {
/*     */             
/* 412 */             this.outages.add(outage);
/* 413 */             if (e instanceof Exception)
/* 414 */               ex = (Exception)e; 
/*     */           }  
/*     */       }  b++; }
/*     */     
/* 418 */     return ex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void fillInOutages2(Twitter paramTwitter, Outage paramOutage);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 432 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<TwitterEvent> getEvents() {
/* 439 */     read();
/* 440 */     return this.events;
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
/*     */   public final int getForgotten() {
/* 453 */     return this.forgotten;
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
/*     */   public final List<Outage> getOutages() {
/* 465 */     return this.outages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<Object[]> getSystemEvents() {
/* 473 */     read();
/* 474 */     return this.sysEvents;
/*     */   }
/*     */   
/*     */   public final List<Twitter.ITweet> getTweets() {
/* 478 */     read();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 486 */     return this.tweets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAlive() {
/* 494 */     if (isConnected())
/* 495 */       return true; 
/* 496 */     if (!this.autoReconnect) {
/* 497 */       return false;
/*     */     }
/* 499 */     return (this.readThread != null && this.readThread.isAlive() && 
/* 500 */       !this.readThread.stopFlag);
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
/*     */   public final boolean isConnected() {
/* 512 */     if (this.readThread != null && this.readThread.isAlive() && 
/* 513 */       this.readThread.ex == null)
/*     */     {
/* 515 */       if (!this.readThread.stopFlag) {
/*     */         return true;
/*     */       }
/*     */     }
/*     */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<TwitterEvent> popEvents() {
/* 526 */     List<TwitterEvent> evs = getEvents();
/* 527 */     this.events = new ArrayList<TwitterEvent>();
/* 528 */     return evs;
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
/*     */   public final List<Object[]> popSystemEvents() {
/* 545 */     List<Object[]> evs = getSystemEvents();
/* 546 */     this.sysEvents = new ArrayList();
/* 547 */     return evs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<Twitter.ITweet> popTweets() {
/* 557 */     List<Twitter.ITweet> ts = getTweets();
/*     */     
/* 559 */     this.tweets = new ArrayList<Twitter.ITweet>();
/* 560 */     return ts;
/*     */   }
/*     */   
/*     */   private final void read() {
/* 564 */     if (this.readThread != null) {
/* 565 */       String[] jsons = this.readThread.popJsons(); byte b; int i; String[] arrayOfString1;
/* 566 */       for (i = (arrayOfString1 = jsons).length, b = 0; b < i; ) { String json = arrayOfString1[b];
/*     */         try {
/* 568 */           read2(json);
/* 569 */         } catch (JSONException e) {
/* 570 */           throw new TwitterException.Parsing(json, e);
/*     */         }  b++; }
/*     */     
/*     */     } 
/* 574 */     if (isConnected()) {
/*     */       return;
/*     */     }
/*     */     
/* 578 */     if (this.readThread != null && this.readThread.stopFlag) {
/*     */       return;
/*     */     }
/* 581 */     Exception ex = (this.readThread == null) ? null : this.readThread.ex;
/*     */     
/* 583 */     close();
/*     */     
/* 585 */     if (!this.autoReconnect) {
/* 586 */       if (ex instanceof TwitterException) throw (TwitterException)ex; 
/* 587 */       throw new TwitterException(ex);
/*     */     } 
/*     */     
/* 590 */     reconnect();
/*     */   }
/*     */   
/*     */   private void read2(String json) throws JSONException {
/* 594 */     JSONObject jobj = new JSONObject(json);
/*     */ 
/*     */     
/* 597 */     JSONArray _friends = jobj.optJSONArray("friends");
/* 598 */     if (_friends != null) {
/* 599 */       read3_friends(_friends);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 604 */     Object object = read3_parse(jobj, this.jtwit);
/*     */ 
/*     */     
/* 607 */     if (object instanceof Twitter.ITweet) {
/* 608 */       Twitter.ITweet tweet = (Twitter.ITweet)object;
/*     */ 
/*     */       
/* 611 */       if (this.tweets.contains(tweet))
/*     */         return; 
/* 613 */       this.tweets.add(tweet);
/*     */ 
/*     */       
/* 616 */       if (tweet instanceof Status) {
/* 617 */         BigInteger id = ((Status)tweet).id;
/* 618 */         if (id.compareTo(this.lastId) > 0) {
/* 619 */           this.lastId = id;
/*     */         }
/*     */       } 
/* 622 */       this.forgotten += forgetIfFull(this.tweets);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 627 */     if (object instanceof TwitterEvent) {
/* 628 */       TwitterEvent event = (TwitterEvent)object;
/* 629 */       this.events.add(event);
/* 630 */       this.forgotten += forgetIfFull(this.events);
/*     */       
/*     */       return;
/*     */     } 
/* 634 */     if (object instanceof Object[]) {
/* 635 */       Object[] sysEvent = (Object[])object;
/*     */ 
/*     */       
/* 638 */       if ("delete".equals(sysEvent[0]))
/* 639 */       { Twitter.ITweet deadTweet = (Twitter.ITweet)sysEvent[1];
/*     */         
/* 641 */         boolean pruned = this.tweets.remove(deadTweet);
/* 642 */         if (pruned)
/* 643 */           return;  } else if ("limit".equals(sysEvent[0]))
/*     */       
/* 645 */       { Integer cnt = (Integer)sysEvent[1];
/* 646 */         this.forgotten += cnt.intValue(); }
/*     */ 
/*     */       
/* 649 */       this.sysEvents.add(sysEvent);
/* 650 */       this.forgotten += forgetIfFull(this.sysEvents);
/*     */       
/*     */       return;
/*     */     } 
/* 654 */     System.out.println(jobj);
/*     */   }
/*     */   
/*     */   private void read3_friends(JSONArray _friends) throws JSONException {
/* 658 */     List<Long> oldFriends = this.friends;
/* 659 */     this.friends = new ArrayList<Long>(_friends.length());
/* 660 */     for (int i = 0, n = _friends.length(); i < n; i++) {
/* 661 */       long fi = _friends.getLong(i);
/* 662 */       this.friends.add(Long.valueOf(fi));
/*     */     } 
/* 664 */     if (oldFriends == null || !this.fillInFollows) {
/*     */       return;
/*     */     }
/*     */     
/* 668 */     HashSet<Long> friends2 = new HashSet<Long>(this.friends);
/* 669 */     friends2.removeAll(oldFriends);
/* 670 */     if (friends2.size() == 0)
/*     */       return; 
/* 672 */     Twitter_Users tu = new Twitter_Users(this.jtwit);
/* 673 */     List<User> newFriends = tu.showById((Collection)friends2);
/* 674 */     User you = this.jtwit.getSelf();
/* 675 */     for (User nf : newFriends) {
/* 676 */       TwitterEvent e = new TwitterEvent(new Date(), you, 
/* 677 */           "follow", nf, null);
/* 678 */       this.events.add(e);
/*     */     } 
/* 680 */     this.forgotten += forgetIfFull(this.events);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void reconnect() {
/* 691 */     long now = System.currentTimeMillis();
/* 692 */     reconnect2();
/* 693 */     long dt = System.currentTimeMillis() - now;
/* 694 */     addSysEvent(new Object[] { "reconnect", Long.valueOf(dt) });
/*     */ 
/*     */ 
/*     */     
/* 698 */     if (this.lastId != BigInteger.ZERO) {
/* 699 */       this.outages.add(new Outage(this.lastId, System.currentTimeMillis()));
/*     */       
/* 701 */       if (this.outages.size() > 100000) {
/* 702 */         for (int i = 0; i < 1000; i++) {
/* 703 */           this.outages.remove(0);
/*     */         }
/*     */         
/* 706 */         this.forgotten += 10000;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addSysEvent(Object[] sysEvent) {
/* 717 */     this.sysEvents.add(sysEvent);
/* 718 */     if (this.listeners.size() == 0)
/* 719 */       return;  synchronized (this.listeners) {
/*     */       try {
/* 721 */         for (IListen listener : this.listeners) {
/* 722 */           boolean carryOn = listener.processSystemEvent(sysEvent);
/*     */           
/* 724 */           if (!carryOn) {
/*     */             break;
/*     */           }
/*     */         } 
/* 728 */       } catch (Exception e) {
/*     */         
/* 730 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void reconnect2() {
/*     */     try {
/* 739 */       connect();
/*     */       return;
/* 741 */     } catch (E40X e) {
/*     */       
/* 743 */       throw e;
/* 744 */     } catch (Exception e) {
/*     */       
/* 746 */       System.out.println(e);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 753 */       int wait = 20 + (new Random()).nextInt(40);
/* 754 */       int waited = 0;
/* 755 */       while (waited < 900) {
/*     */         try {
/* 757 */           Thread.sleep((wait * 1000));
/* 758 */           waited += wait;
/* 759 */           if (wait < 300) {
/* 760 */             wait *= 2;
/*     */           }
/* 762 */           connect();
/*     */           
/*     */           return;
/* 765 */         } catch (E40X e40X) {
/* 766 */           throw e40X;
/* 767 */         } catch (Exception exception) {
/*     */           
/* 769 */           System.out.println(exception);
/*     */         } 
/*     */       } 
/* 772 */       throw new TwitterException.E50X("Could not connect to streaming server");
/*     */     } 
/*     */   }
/*     */   synchronized void reconnectFromGobblerThread() {
/* 776 */     assert Thread.currentThread() == this.readThread || this.readThread == null : this;
/* 777 */     if (isConnected())
/*     */       return; 
/* 779 */     reconnect();
/*     */   }
/*     */   
/*     */   public boolean removeListener(IListen listener) {
/* 783 */     synchronized (this.listeners) {
/* 784 */       return this.listeners.remove(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoReconnect(boolean yes) {
/* 794 */     this.autoReconnect = yes;
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
/*     */   public void setPreviousCount(int previousCount) {
/* 811 */     this.previousCount = previousCount;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\AStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
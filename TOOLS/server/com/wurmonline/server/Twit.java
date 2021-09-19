/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ import winterwell.jtwitter.OAuthSignpostClient;
/*     */ import winterwell.jtwitter.Twitter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Twit
/*     */ {
/*  34 */   private static final Logger logger = Logger.getLogger(Twit.class.getName());
/*     */   private final String sender;
/*     */   private final String twit;
/*     */   private final String consumerKey;
/*     */   private final String consumerSecret;
/*     */   private final String oauthToken;
/*     */   private final String oauthTokenSecret;
/*     */   private final boolean isVillage;
/*  42 */   private static final Twit[] emptyTwits = new Twit[0];
/*     */   @GuardedBy("TWITS_RW_LOCK")
/*  44 */   private static final List<Twit> twits = new LinkedList<>();
/*  45 */   private static final ReentrantReadWriteLock TWITS_RW_LOCK = new ReentrantReadWriteLock();
/*  46 */   private static final TwitterThread twitterThread = new TwitterThread();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Twit(String senderName, String toTwit, String consumerKeyToUse, String consumerSecretToUse, String applicationToken, String applicationSecret, boolean _isVillage) {
/*  54 */     this.sender = senderName;
/*  55 */     this.twit = toTwit.substring(0, Math.min(toTwit.length(), 279));
/*  56 */     this.consumerKey = consumerKeyToUse;
/*  57 */     this.consumerSecret = consumerSecretToUse;
/*  58 */     this.oauthToken = applicationToken;
/*  59 */     this.oauthTokenSecret = applicationSecret;
/*  60 */     this.isVillage = _isVillage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Twit[] getTwitsArray() {
/*     */     try {
/*  67 */       TWITS_RW_LOCK.writeLock().lock();
/*  68 */       if (twits.size() > 0)
/*     */       {
/*  70 */         Twit[] toReturn = new Twit[twits.size()];
/*  71 */         int x = 0;
/*  72 */         for (ListIterator<Twit> it = twits.listIterator(); it.hasNext(); ) {
/*     */           
/*  74 */           toReturn[x] = it.next();
/*  75 */           x++;
/*     */         } 
/*  77 */         return toReturn;
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/*  82 */       TWITS_RW_LOCK.writeLock().unlock();
/*     */     } 
/*  84 */     return emptyTwits;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void removeTwit(Twit twit) {
/*     */     try {
/*  91 */       TWITS_RW_LOCK.writeLock().lock();
/*  92 */       twits.remove(twit);
/*     */     }
/*     */     finally {
/*     */       
/*  96 */       TWITS_RW_LOCK.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void pollTwits() {
/* 105 */     Twit[] twitarr = getTwitsArray();
/* 106 */     if (twitarr.length > 0)
/*     */     {
/* 108 */       for (int y = 0; y < twitarr.length; y++) {
/*     */ 
/*     */         
/*     */         try {
/* 112 */           twitJTwitter(twitarr[y]);
/* 113 */           removeTwit(twitarr[y]);
/*     */         }
/* 115 */         catch (Exception ex) {
/*     */           
/* 117 */           if (ex.getMessage().startsWith("Already tweeted!") || ex.getMessage().startsWith("Forbidden") || ex
/* 118 */             .getMessage().startsWith("Unauthorized") || ex.getMessage().startsWith("Invalid")) {
/*     */             
/* 120 */             logger.log(Level.INFO, "Removed duplicate or unauthorized " + (twitarr[y]).twit);
/* 121 */             removeTwit(twitarr[y]);
/*     */           }
/* 123 */           else if ((twitarr[y]).isVillage) {
/*     */             
/* 125 */             logger.log(Level.INFO, "Twitting failed for village " + ex.getMessage() + " Removing.");
/* 126 */             removeTwit(twitarr[y]);
/*     */           }
/*     */           else {
/*     */             
/* 130 */             if ((twitarr[y]).twit == null || (twitarr[y]).twit.length() == 0)
/* 131 */               removeTwit(twitarr[y]); 
/* 132 */             logger.log(Level.INFO, "Twitting failed for server " + ex.getMessage() + ". Trying later.");
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void twit(Twit twit) {
/* 181 */     if (twit != null) {
/*     */       
/*     */       try {
/*     */         
/* 185 */         TWITS_RW_LOCK.writeLock().lock();
/* 186 */         twits.add(twit);
/*     */       }
/*     */       finally {
/*     */         
/* 190 */         TWITS_RW_LOCK.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void twitJTwitter(Twit twit) {
/* 197 */     logger.log(Level.INFO, "creating oauthClient for " + twit.twit);
/*     */     
/* 199 */     OAuthSignpostClient oauthClient = new OAuthSignpostClient(twit.consumerKey, twit.consumerSecret, twit.oauthToken, twit.oauthTokenSecret);
/*     */ 
/*     */     
/* 202 */     logger.log(Level.INFO, "creating twitter for " + twit.twit);
/* 203 */     Twitter twitter = new Twitter(twit.sender, (Twitter.IHttpClient)oauthClient);
/*     */     
/* 205 */     twitter.setStatus(twit.twit);
/* 206 */     logger.log(Level.INFO, "done sending twit " + twit.twit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final TwitterThread getTwitterThread() {
/* 212 */     return twitterThread;
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
/*     */   private static class TwitterThread
/*     */     implements Runnable
/*     */   {
/*     */     public void run() {
/*     */       try {
/* 243 */         long start = System.nanoTime();
/*     */         
/* 245 */         Twit.pollTwits();
/*     */         
/* 247 */         float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 248 */         if (lElapsedTime > (float)Constants.lagThreshold)
/*     */         {
/* 250 */           Twit.logger.info("Finished calling Twit.pollTwits(), which took " + lElapsedTime + " millis.");
/*     */         
/*     */         }
/*     */       }
/* 254 */       catch (RuntimeException e) {
/*     */         
/* 256 */         Twit.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorService while calling Twit.pollTwits()", e);
/* 257 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Twit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
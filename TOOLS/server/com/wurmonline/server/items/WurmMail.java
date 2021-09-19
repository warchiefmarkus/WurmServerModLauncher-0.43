/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.questions.MailSendConfirmQuestion;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WurmMail
/*     */   implements TimeConstants, MiscConstants, CounterTypes, Comparable<WurmMail>
/*     */ {
/*     */   public static final byte MAIL_TYPE_PREPAID = 0;
/*     */   public static final byte MAIL_TYPE_CASHONDELIVERY = 1;
/*     */   public final byte type;
/*     */   public final long itemId;
/*     */   public long sender;
/*     */   public long receiver;
/*     */   public final long price;
/*     */   public final long sent;
/*     */   public long expiration;
/*     */   public final int sourceserver;
/*     */   public boolean rejected = false;
/*  60 */   private static final ConcurrentHashMap<Long, Set<WurmMail>> mails = new ConcurrentHashMap<>();
/*  61 */   private static final ConcurrentHashMap<Long, Long> mailsByItemId = new ConcurrentHashMap<>();
/*     */   
/*  63 */   private static final Logger logger = Logger.getLogger(WurmMail.class.getName());
/*     */   
/*     */   private static final String GET_ALL_MAIL = "SELECT * FROM MAIL";
/*     */   
/*     */   private static final String DELETE_MAIL = "DELETE FROM MAIL WHERE ITEMID=?";
/*     */   
/*     */   private static final String SAVE_MAIL = "INSERT INTO MAIL (ITEMID,TYPE,SENDER,RECEIVER,PRICE,SENT,EXPIRATION,SOURCESERVER,RETURNED ) VALUES(?,?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String UPDATE_MAIL = "UPDATE MAIL SET TYPE=?,SENDER=?,RECEIVER=?,PRICE=?,SENT=?,EXPIRATION=?,RETURNED=? WHERE ITEMID=?";
/*     */   public static final int maxNumberMailsToDisplay = 100;
/*     */   
/*     */   public WurmMail(byte _type, long _itemid, long _sender, long _receiver, long _price, long _sent, long _expiration, int _sourceserver, boolean _rejected, boolean loading) {
/*  75 */     this.type = _type;
/*  76 */     this.itemId = _itemid;
/*  77 */     this.sender = _sender;
/*  78 */     this.receiver = _receiver;
/*  79 */     this.price = _price;
/*  80 */     this.sent = _sent;
/*  81 */     this.expiration = _expiration;
/*  82 */     this.rejected = _rejected;
/*  83 */     this.sourceserver = _sourceserver;
/*  84 */     if (loading) {
/*  85 */       addWurmMail(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final void addWurmMail(WurmMail mail) {
/*  90 */     if (!mail.isExpired() || mail.isRejected()) {
/*     */       
/*  92 */       if (mail.isRejected() && mail.isExpired())
/*     */       {
/*     */         
/*  95 */         deleteMail(mail.getItemId());
/*     */       }
/*     */       else
/*     */       {
/*  99 */         getMailsFor(mail.receiver).add(mail);
/* 100 */         mailsByItemId.put(new Long(mail.itemId), new Long(mail.receiver));
/*     */       }
/*     */     
/* 103 */     } else if (mail.sourceserver == Servers.localServer.id && !mail.rejected) {
/*     */ 
/*     */       
/* 106 */       long oldRec = mail.receiver;
/* 107 */       mail.receiver = mail.sender;
/* 108 */       mail.sender = oldRec;
/* 109 */       mail.expiration = System.currentTimeMillis() + 1209600000L;
/* 110 */       mail.rejected = true;
/* 111 */       mail.update();
/* 112 */       getMailsFor(mail.receiver).add(mail);
/* 113 */       mailsByItemId.put(new Long(mail.itemId), new Long(mail.receiver));
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
/*     */   public static final void poll() {
/* 141 */     Set<WurmMail> toDelete = new HashSet<>();
/* 142 */     for (Set<WurmMail> mailset : mails.values()) {
/*     */       
/* 144 */       for (WurmMail m : mailset) {
/*     */         
/* 146 */         if (m.isExpired()) {
/*     */           
/* 148 */           logger.log(Level.INFO, "Checking expired WurmMail " + m.itemId);
/*     */           
/* 150 */           if (m.sourceserver != Servers.localServer.id && !m.isRejected()) {
/*     */             
/* 152 */             logger.log(Level.INFO, "Trying to return expired WurmMail " + m.itemId);
/* 153 */             int targetServer = m.sourceserver;
/*     */             
/*     */             try {
/* 156 */               Item toReturn = Items.getItem(m.getItemId());
/* 157 */               if (Servers.getServerWithId(targetServer).isAvailable(5, true)) {
/*     */                 
/* 159 */                 logger.log(Level.INFO, "Returning to " + targetServer);
/*     */ 
/*     */                 
/* 162 */                 m.expiration = System.currentTimeMillis() + 1209600000L;
/* 163 */                 m.setRejected(true);
/* 164 */                 long sender = m.receiver;
/* 165 */                 m.receiver = m.sender;
/* 166 */                 m.sender = sender;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 173 */                 Set<WurmMail> oneMail = new HashSet<>();
/* 174 */                 oneMail.add(m);
/* 175 */                 Item[] itemarr = { toReturn };
/*     */ 
/*     */                 
/* 178 */                 if (!MailSendConfirmQuestion.sendMailSetToServer(m.receiver, null, targetServer, oneMail, m.sender, itemarr))
/*     */                 {
/* 180 */                   toDelete.add(m);
/*     */                 }
/*     */               } 
/*     */               
/*     */               continue;
/* 185 */             } catch (NoSuchItemException nsi) {
/*     */               
/* 187 */               toDelete.add(m);
/*     */               break;
/*     */             } 
/*     */           } 
/* 191 */           if (m.isRejected()) {
/*     */ 
/*     */             
/* 194 */             logger.log(Level.INFO, "Deleting expired rejected mail " + m
/* 195 */                 .getItemId() + " sent to " + m.getReceiver() + " from " + m
/* 196 */                 .getSender());
/* 197 */             toDelete.add(m);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 202 */     for (WurmMail deleted : toDelete) {
/*     */       
/* 204 */       Items.destroyItem(deleted.getItemId());
/* 205 */       removeMail(deleted.getItemId());
/* 206 */       logger.log(Level.INFO, "Deleted WurmMail " + deleted.getItemId());
/*     */     } 
/* 208 */     toDelete.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isItemInMail(long itemId) {
/* 213 */     return (mailsByItemId.get(Long.valueOf(itemId)) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRejected() {
/* 223 */     return this.rejected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRejected(boolean aRejected) {
/* 234 */     this.rejected = aRejected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getType() {
/* 244 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getItemId() {
/* 254 */     return this.itemId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSender() {
/* 264 */     return this.sender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getReceiver() {
/* 274 */     return this.receiver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPrice() {
/* 284 */     return this.price;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSent() {
/* 294 */     return this.sent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExpiration() {
/* 304 */     return this.expiration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSourceserver() {
/* 314 */     return this.sourceserver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*     */     try {
/* 321 */       Item item = Items.getItem(this.itemId);
/* 322 */       return item.getName();
/*     */     }
/* 324 */     catch (NoSuchItemException e) {
/*     */       
/* 326 */       return "UnKnown";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExpired() {
/* 332 */     return (this.expiration < System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<WurmMail> getWaitingMailFor(long receiverid) {
/* 337 */     Set<WurmMail> set = mails.get(new Long(receiverid));
/* 338 */     Set<WurmMail> toReturn = new HashSet<>();
/* 339 */     if (set != null)
/*     */     {
/* 341 */       for (WurmMail mail : set) {
/*     */         
/* 343 */         if (mail.sent < System.currentTimeMillis())
/* 344 */           toReturn.add(mail); 
/*     */       } 
/*     */     }
/* 347 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<WurmMail> getMailsFor(long receiverid) {
/* 352 */     Set<WurmMail> set = mails.get(new Long(receiverid));
/* 353 */     if (set == null) {
/*     */       
/* 355 */       set = new HashSet<>();
/* 356 */       mails.put(new Long(receiverid), set);
/*     */     } 
/* 358 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<WurmMail> getSentMailsFor(long receiverid, int maxNumbers) {
/* 363 */     Set<WurmMail> set = getMailsFor(receiverid);
/* 364 */     Set<WurmMail> toReturn = new HashSet<>();
/* 365 */     int nums = 0;
/* 366 */     for (WurmMail toAdd : set) {
/*     */       
/* 368 */       if (toAdd.sent < System.currentTimeMillis()) {
/*     */         
/* 370 */         toReturn.add(toAdd);
/* 371 */         nums++;
/*     */       } 
/* 373 */       if (nums >= maxNumbers)
/*     */         break; 
/*     */     } 
/* 376 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<WurmMail> getMailsSendBy(long senderid) {
/* 381 */     Set<WurmMail> sent = new HashSet<>();
/* 382 */     for (Set<WurmMail> mailset : mails.values()) {
/*     */       
/* 384 */       for (WurmMail m : mailset) {
/*     */         
/* 386 */         if (m.sender == senderid)
/* 387 */           sent.add(m); 
/*     */       } 
/*     */     } 
/* 390 */     return sent;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final long getReceiverForItem(long itemId) {
/* 395 */     Long receiver = mailsByItemId.get(new Long(itemId));
/* 396 */     if (receiver == null)
/* 397 */       return -10L; 
/* 398 */     return receiver.longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final WurmMail getWurmMailForItem(long itemId) {
/* 403 */     Long receiver = mailsByItemId.get(new Long(itemId));
/* 404 */     if (receiver == null)
/* 405 */       return null; 
/* 406 */     Set<WurmMail> set = getMailsFor(receiver.longValue());
/* 407 */     if (set != null)
/*     */     {
/* 409 */       for (WurmMail m : set) {
/*     */         
/* 411 */         if (m.itemId == itemId)
/*     */         {
/* 413 */           return m;
/*     */         }
/*     */       } 
/*     */     }
/* 417 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final WurmMail[] getAllMail() {
/* 422 */     Set<WurmMail> sent = new HashSet<>();
/* 423 */     for (Set<WurmMail> mailset : mails.values()) {
/*     */       
/* 425 */       for (WurmMail m : mailset)
/*     */       {
/* 427 */         sent.add(m);
/*     */       }
/*     */     } 
/* 430 */     return sent.<WurmMail>toArray(new WurmMail[sent.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void removeMail(long itemId) {
/* 435 */     Long receiver = mailsByItemId.get(new Long(itemId));
/* 436 */     if (receiver != null) {
/*     */       
/* 438 */       Set<WurmMail> set = getMailsFor(receiver.longValue());
/* 439 */       if (set != null) {
/*     */         
/* 441 */         WurmMail toRemove = null;
/* 442 */         for (WurmMail m : set) {
/*     */           
/* 444 */           if (m.itemId == itemId) {
/*     */             
/* 446 */             toRemove = m;
/*     */             break;
/*     */           } 
/*     */         } 
/* 450 */         if (toRemove != null)
/*     */         {
/* 452 */           set.remove(toRemove);
/*     */         }
/*     */       } 
/* 455 */       mailsByItemId.remove(new Long(itemId));
/* 456 */       deleteMail(itemId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllMails() throws IOException {
/* 462 */     long start = System.nanoTime();
/* 463 */     int loadedMails = 0;
/* 464 */     Connection dbcon = null;
/* 465 */     PreparedStatement ps = null;
/* 466 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 469 */       dbcon = DbConnector.getItemDbCon();
/* 470 */       ps = dbcon.prepareStatement("SELECT * FROM MAIL");
/* 471 */       rs = ps.executeQuery();
/* 472 */       while (rs.next())
/*     */       {
/* 474 */         new WurmMail(rs.getByte("TYPE"), rs.getLong("ITEMID"), rs.getLong("SENDER"), rs.getLong("RECEIVER"), rs
/* 475 */             .getLong("PRICE"), rs.getLong("SENT"), rs.getLong("EXPIRATION"), rs.getInt("SOURCESERVER"), rs
/* 476 */             .getBoolean("RETURNED"), true);
/* 477 */         loadedMails++;
/*     */       }
/*     */     
/* 480 */     } catch (SQLException sqex) {
/*     */       
/* 482 */       logger.log(Level.WARNING, "Problem loading Mails from database due to " + sqex.getMessage(), sqex);
/* 483 */       throw new IOException(sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 487 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 488 */       DbConnector.returnConnection(dbcon);
/* 489 */       long end = System.nanoTime();
/* 490 */       logger.info("Loaded " + loadedMails + " Mails from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void deleteMail(long itemid) {
/* 497 */     Connection dbcon = null;
/* 498 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 501 */       dbcon = DbConnector.getItemDbCon();
/* 502 */       ps = dbcon.prepareStatement("DELETE FROM MAIL WHERE ITEMID=?");
/* 503 */       ps.setLong(1, itemid);
/* 504 */       ps.executeUpdate();
/*     */     }
/* 506 */     catch (SQLException sqex) {
/*     */       
/* 508 */       logger.log(Level.WARNING, itemid + " : " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 512 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 513 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void createInDatabase() {
/* 519 */     Connection dbcon = null;
/* 520 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 523 */       dbcon = DbConnector.getItemDbCon();
/* 524 */       ps = dbcon.prepareStatement("INSERT INTO MAIL (ITEMID,TYPE,SENDER,RECEIVER,PRICE,SENT,EXPIRATION,SOURCESERVER,RETURNED ) VALUES(?,?,?,?,?,?,?,?,?)");
/* 525 */       ps.setLong(1, this.itemId);
/* 526 */       ps.setByte(2, this.type);
/* 527 */       ps.setLong(3, this.sender);
/* 528 */       ps.setLong(4, this.receiver);
/* 529 */       ps.setLong(5, this.price);
/* 530 */       ps.setLong(6, this.sent);
/* 531 */       ps.setLong(7, this.expiration);
/* 532 */       ps.setInt(8, this.sourceserver);
/* 533 */       ps.setBoolean(9, this.rejected);
/* 534 */       ps.executeUpdate();
/*     */     }
/* 536 */     catch (SQLException sqex) {
/*     */       
/* 538 */       logger.log(Level.WARNING, this.itemId + " : " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 542 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 543 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void update() {
/* 552 */     Connection dbcon = null;
/* 553 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 556 */       dbcon = DbConnector.getItemDbCon();
/* 557 */       ps = dbcon.prepareStatement("UPDATE MAIL SET TYPE=?,SENDER=?,RECEIVER=?,PRICE=?,SENT=?,EXPIRATION=?,RETURNED=? WHERE ITEMID=?");
/* 558 */       ps.setByte(1, this.type);
/* 559 */       ps.setLong(2, this.sender);
/* 560 */       ps.setLong(3, this.receiver);
/* 561 */       ps.setLong(4, this.price);
/* 562 */       ps.setLong(5, this.sent);
/* 563 */       ps.setLong(6, this.expiration);
/* 564 */       ps.setBoolean(7, this.rejected);
/* 565 */       ps.setLong(8, this.itemId);
/* 566 */       ps.executeUpdate();
/*     */     }
/* 568 */     catch (SQLException sqex) {
/*     */       
/* 570 */       logger.log(Level.WARNING, this.itemId + " : " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 574 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 575 */       DbConnector.returnConnection(dbcon);
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
/*     */   public int compareTo(WurmMail otherWurmMail) {
/* 587 */     return getName().compareTo(otherWurmMail.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\WurmMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
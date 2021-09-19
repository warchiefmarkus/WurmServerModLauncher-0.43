/*     */ package com.wurmonline.server.banks;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.concurrency.Pollable;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Bank
/*     */   implements MiscConstants, TimeConstants, Pollable
/*     */ {
/*     */   public static final String VERSION = "$Revision: 1.2 $";
/*     */   private static final String DELETESLOT = "DELETE FROM BANKS_ITEMS WHERE ITEMID=?";
/*     */   public final long owner;
/*     */   private final long lastPolled;
/*     */   public final long id;
/*  58 */   public long startedMoving = -10L;
/*     */ 
/*     */ 
/*     */   
/*     */   public final int size;
/*     */ 
/*     */   
/*     */   public final BankSlot[] slots;
/*     */ 
/*     */   
/*  68 */   public int currentVillage = -10;
/*  69 */   public int targetVillage = -10;
/*     */ 
/*     */   
/*     */   public boolean open = false;
/*     */ 
/*     */   
/*     */   private static final String CREATE = "INSERT INTO BANKS (WURMID,OWNER,LASTPOLLED,STARTEDMOVE,SIZE,CURRENTVILLAGE,TARGETVILLAGE) VALUES(?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String LOADITEMS = "SELECT * FROM BANKS_ITEMS WHERE BANKID=?";
/*     */   
/*     */   private static final String MOVEINFO = "UPDATE BANKS SET STARTEDMOVE=?,TARGETVILLAGE=?,CURRENTVILLAGE=? WHERE WURMID=?";
/*     */   
/*  81 */   private static final Logger logger = Logger.getLogger(Bank.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Bank(long aOwnerId, int aSize, int aVillage) {
/*  96 */     this.owner = aOwnerId;
/*  97 */     this.size = aSize;
/*  98 */     this.currentVillage = aVillage;
/*  99 */     this.lastPolled = System.currentTimeMillis();
/* 100 */     this.id = WurmId.getNextBankId();
/* 101 */     this.slots = new BankSlot[aSize];
/* 102 */     save();
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
/*     */   public Bank(long aId, long aOwnerId, int aSize, long aLastPolled, long aStartedMoving, int aCurrentVillage, int aTargetVillage) {
/* 126 */     this.owner = aOwnerId;
/* 127 */     this.size = aSize;
/* 128 */     this.id = aId;
/* 129 */     this.startedMoving = aStartedMoving;
/* 130 */     this.lastPolled = aLastPolled;
/* 131 */     this.currentVillage = aCurrentVillage;
/* 132 */     this.targetVillage = aTargetVillage;
/* 133 */     this.slots = new BankSlot[aSize];
/* 134 */     loadAllItems();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() throws BankUnavailableException {
/* 143 */     if (this.startedMoving > 0L) {
/*     */       
/* 145 */       Village target = null;
/*     */       
/*     */       try {
/* 148 */         target = Villages.getVillage(this.targetVillage);
/*     */       }
/* 150 */       catch (NoSuchVillageException noSuchVillageException) {}
/*     */ 
/*     */ 
/*     */       
/* 154 */       if (target == null) {
/* 155 */         throw new BankUnavailableException("The bank account is put on hold. Please select a new village.");
/*     */       }
/*     */       
/* 158 */       long now = System.currentTimeMillis();
/* 159 */       if (this.startedMoving + 86400000L < now) {
/*     */         
/* 161 */         poll(now);
/*     */       } else {
/*     */         
/* 164 */         throw new BankUnavailableException("The bank account is moving to " + target.getName() + ". It will arrive in approximately " + 
/* 165 */             Server.getTimeFor(this.startedMoving + 86400000L - now) + ".");
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     this.open = true;
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
/*     */   public void pollItems(long now) {
/* 181 */     for (int x = 0; x < this.slots.length; x++) {
/*     */       
/* 183 */       if (this.slots[x] != null) {
/*     */         
/* 185 */         if ((this.slots[x]).item.isFood())
/* 186 */           (this.slots[x]).item.setDamage((this.slots[x]).item.getDamage() + 10.0F); 
/* 187 */         (this.slots[x]).item.lastMaintained = now;
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
/*     */   public void poll(long now) {
/* 205 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 207 */       logger.finest("Bank polling now: " + now + ", id: " + this.id);
/*     */     }
/* 209 */     if (this.startedMoving > 0L && this.startedMoving + 86400000L < now)
/*     */     {
/* 211 */       stopMoving();
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
/*     */   public boolean removeItem(Item item) {
/* 224 */     for (int x = 0; x < this.slots.length; x++) {
/*     */       
/* 226 */       if (this.slots[x] != null && (this.slots[x]).item == item) {
/*     */         
/* 228 */         this.slots[x].delete();
/* 229 */         this.slots[x] = null;
/* 230 */         item.setBanked(false);
/* 231 */         return true;
/*     */       } 
/*     */     } 
/* 234 */     return false;
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
/*     */   public boolean addItem(Item item) {
/* 250 */     for (int x = 0; x < this.slots.length; x++) {
/*     */       
/* 252 */       if (this.slots[x] == null) {
/*     */         
/* 254 */         this.slots[x] = new BankSlot(item, this.id, false, System.currentTimeMillis(), true);
/* 255 */         item.setBanked(true);
/* 256 */         if (item.isCoin())
/*     */         {
/* 258 */           Server.getInstance().transaction(item.getWurmId(), item.lastOwner, this.id, "Banked", item.getValue());
/*     */         }
/* 260 */         return true;
/*     */       } 
/*     */     } 
/* 263 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void save() {
/* 273 */     Connection dbcon = null;
/* 274 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 277 */       dbcon = DbConnector.getEconomyDbCon();
/* 278 */       ps = dbcon.prepareStatement("INSERT INTO BANKS (WURMID,OWNER,LASTPOLLED,STARTEDMOVE,SIZE,CURRENTVILLAGE,TARGETVILLAGE) VALUES(?,?,?,?,?,?,?)");
/* 279 */       ps.setLong(1, this.id);
/* 280 */       ps.setLong(2, this.owner);
/* 281 */       ps.setLong(3, this.lastPolled);
/* 282 */       ps.setLong(4, this.startedMoving);
/* 283 */       ps.setInt(5, this.size);
/* 284 */       ps.setInt(6, this.currentVillage);
/* 285 */       ps.setInt(7, this.targetVillage);
/* 286 */       ps.executeUpdate();
/*     */     }
/* 288 */     catch (SQLException sqx) {
/*     */       
/* 290 */       logger.log(Level.WARNING, "Failed to create bank account for owner " + this.owner + ", SqlState: " + sqx.getSQLState() + ", ErrorCode: " + sqx
/* 291 */           .getErrorCode(), sqx);
/* 292 */       Exception lNext = sqx.getNextException();
/* 293 */       if (lNext != null)
/*     */       {
/* 295 */         logger.log(Level.WARNING, "Failed to create bank account for owner " + this.owner + ", Next Exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 300 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 301 */       DbConnector.returnConnection(dbcon);
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
/*     */   public Village getCurrentVillage() throws BankUnavailableException {
/* 315 */     Village village = null;
/*     */     
/*     */     try {
/* 318 */       village = Villages.getVillage(this.currentVillage);
/*     */     }
/* 320 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 322 */       throw new BankUnavailableException("The bank account is currently not located in a village.");
/*     */     } 
/* 324 */     return village;
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
/*     */   public boolean startMoving(int aTargetVillage) {
/* 338 */     if (!this.open) {
/*     */       
/* 340 */       this.targetVillage = aTargetVillage;
/* 341 */       this.currentVillage = -10;
/* 342 */       this.startedMoving = System.currentTimeMillis();
/* 343 */       setMoveInfo();
/* 344 */       return true;
/*     */     } 
/* 346 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopMoving() {
/* 356 */     this.currentVillage = this.targetVillage;
/* 357 */     this.targetVillage = -10;
/* 358 */     this.startedMoving = -10L;
/* 359 */     setMoveInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setMoveInfo() {
/* 369 */     Connection dbcon = null;
/* 370 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 373 */       dbcon = DbConnector.getEconomyDbCon();
/* 374 */       ps = dbcon.prepareStatement("UPDATE BANKS SET STARTEDMOVE=?,TARGETVILLAGE=?,CURRENTVILLAGE=? WHERE WURMID=?");
/* 375 */       ps.setLong(1, this.startedMoving);
/* 376 */       ps.setInt(2, this.targetVillage);
/* 377 */       ps.setInt(3, this.currentVillage);
/*     */       
/* 379 */       ps.setLong(4, this.id);
/* 380 */       ps.executeUpdate();
/*     */     }
/* 382 */     catch (SQLException sqx) {
/*     */       
/* 384 */       logger.log(Level.WARNING, "Failed to set move info for bank account with owner " + this.owner + ", SqlState: " + sqx
/* 385 */           .getSQLState() + ", ErrorCode: " + sqx
/* 386 */           .getErrorCode(), sqx);
/* 387 */       Exception lNext = sqx.getNextException();
/* 388 */       if (lNext != null)
/*     */       {
/* 390 */         logger.log(Level.WARNING, "Failed to set move info for bank account with owner " + this.owner + ", Next Exception", lNext);
/*     */       
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 396 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 397 */       DbConnector.returnConnection(dbcon);
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
/*     */   private void loadAllItems() {
/* 410 */     Connection dbcon = null;
/* 411 */     PreparedStatement ps = null;
/* 412 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 415 */       dbcon = DbConnector.getEconomyDbCon();
/* 416 */       ps = dbcon.prepareStatement("SELECT * FROM BANKS_ITEMS WHERE BANKID=?");
/* 417 */       ps.setLong(1, this.id);
/* 418 */       rs = ps.executeQuery();
/* 419 */       long itemid = -10L;
/* 420 */       int x = 0;
/* 421 */       while (rs.next()) {
/*     */         
/*     */         try
/*     */         {
/* 425 */           itemid = rs.getLong("ITEMID");
/* 426 */           long inserted = rs.getLong("INSERTED");
/* 427 */           boolean stasis = rs.getBoolean("STASIS");
/* 428 */           Item item = Items.getItem(itemid);
/* 429 */           if (x < this.size) {
/* 430 */             this.slots[x] = new BankSlot(item, this.id, stasis, inserted, false);
/*     */           } else {
/* 432 */             logger.log(Level.WARNING, "Bank account with owner " + this.owner + " has too many items.");
/* 433 */           }  x++;
/*     */         }
/* 435 */         catch (NoSuchItemException nsi)
/*     */         {
/* 437 */           deleteSlot(itemid);
/* 438 */           logger.log(Level.WARNING, itemid + " not found:" + nsi.getMessage() + ". Deleting bank slot.");
/*     */         }
/*     */       
/*     */       } 
/* 442 */     } catch (SQLException sqx) {
/*     */       
/* 444 */       logger.log(Level.WARNING, "Failed to load bank items, SqlState: " + sqx
/* 445 */           .getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 446 */       Exception lNext = sqx.getNextException();
/* 447 */       if (lNext != null)
/*     */       {
/* 449 */         logger.log(Level.WARNING, "Failed to load bank items, Next Exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 454 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 455 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteSlot(long wurmid) {
/* 462 */     Connection dbcon = null;
/* 463 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 466 */       dbcon = DbConnector.getEconomyDbCon();
/* 467 */       ps = dbcon.prepareStatement("DELETE FROM BANKS_ITEMS WHERE ITEMID=?");
/* 468 */       ps.setLong(1, wurmid);
/* 469 */       ps.executeUpdate();
/*     */     }
/* 471 */     catch (SQLException sqx) {
/*     */       
/* 473 */       logger.log(Level.WARNING, "Failed to delete bankslot for bank " + this.id + ", SqlState: " + sqx.getSQLState() + ", ErrorCode: " + sqx
/* 474 */           .getErrorCode(), sqx);
/* 475 */       Exception lNext = sqx.getNextException();
/* 476 */       if (lNext != null)
/*     */       {
/* 478 */         logger.log(Level.WARNING, "Failed to delete bankslot for bank " + this.id + ", next exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 483 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 484 */       DbConnector.returnConnection(dbcon);
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
/*     */   public String toString() {
/* 530 */     return "Bank [id: " + this.id + ", owner: " + this.owner + ", currentVillage: " + this.currentVillage + ", targetVillage: " + this.targetVillage + ", open: " + this.open + ", lastPolled: " + this.lastPolled + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\banks\Bank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
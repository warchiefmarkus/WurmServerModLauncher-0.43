/*     */ package com.wurmonline.server.banks;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
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
/*     */ public final class BankSlot
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final String VERSION = "$Revision: 1.2 $";
/*     */   public Item item;
/*     */   public boolean stasis;
/*  44 */   public long inserted = -10L;
/*     */ 
/*     */   
/*     */   private final long bank;
/*     */ 
/*     */   
/*     */   private static final String CREATE = "INSERT INTO BANKS_ITEMS (ITEMID,BANKID,INSERTED,STASIS) VALUES(?,?,?,?)";
/*     */   
/*     */   private static final String DELETESLOT = "DELETE FROM BANKS_ITEMS WHERE ITEMID=?";
/*     */   
/*  54 */   private static final Logger logger = Logger.getLogger(BankSlot.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BankSlot(Item aItem, long aBank, boolean aStas, long aInserted, boolean aCreate) {
/*  71 */     this.item = aItem;
/*  72 */     this.bank = aBank;
/*  73 */     this.stasis = aStas;
/*  74 */     this.inserted = aInserted;
/*  75 */     if (aCreate) {
/*  76 */       create();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void create() {
/*  86 */     Connection dbcon = null;
/*  87 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  90 */       dbcon = DbConnector.getEconomyDbCon();
/*  91 */       ps = dbcon.prepareStatement("INSERT INTO BANKS_ITEMS (ITEMID,BANKID,INSERTED,STASIS) VALUES(?,?,?,?)");
/*  92 */       ps.setLong(1, this.item.getWurmId());
/*  93 */       ps.setLong(2, this.bank);
/*  94 */       ps.setLong(3, System.currentTimeMillis());
/*  95 */       ps.setBoolean(4, this.stasis);
/*  96 */       ps.executeUpdate();
/*     */     }
/*  98 */     catch (SQLException sqx) {
/*     */       
/* 100 */       logger.log(Level.WARNING, "Failed to create bank slot for item " + this.item.getWurmId() + ", SqlState: " + sqx.getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 101 */       Exception lNext = sqx.getNextException();
/* 102 */       if (lNext != null)
/*     */       {
/* 104 */         logger.log(Level.WARNING, "Failed to create bank slot for item " + this.item.getWurmId() + ", next exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 109 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 110 */       DbConnector.returnConnection(dbcon);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void delete() {
/* 161 */     Connection dbcon = null;
/* 162 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 165 */       dbcon = DbConnector.getEconomyDbCon();
/* 166 */       ps = dbcon.prepareStatement("DELETE FROM BANKS_ITEMS WHERE ITEMID=?");
/* 167 */       ps.setLong(1, this.item.getWurmId());
/* 168 */       ps.executeUpdate();
/*     */     }
/* 170 */     catch (SQLException sqx) {
/*     */       
/* 172 */       logger.log(Level.WARNING, "Failed to delete bankslot for bank " + this.bank + ", SqlState: " + sqx.getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 173 */       Exception lNext = sqx.getNextException();
/* 174 */       if (lNext != null)
/*     */       {
/* 176 */         logger.log(Level.WARNING, "Failed to delete bankslot for bank " + this.bank + ", next exception", lNext);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 181 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 182 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItem() {
/* 193 */     return this.item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(Item aItem) {
/* 204 */     this.item = aItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInserted() {
/* 214 */     return this.inserted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInserted(long aInserted) {
/* 225 */     this.inserted = aInserted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStasis() {
/* 235 */     return this.stasis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getBank() {
/* 245 */     return this.bank;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\banks\BankSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.endgames;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTypes;
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
/*     */ public final class EndGameItem
/*     */   implements MiscConstants, ItemTypes
/*     */ {
/*     */   private static final String DELETE = "DELETE FROM ENDGAMEITEMS WHERE WURMID=?";
/*     */   private static final String CREATE = "INSERT INTO ENDGAMEITEMS (WURMID,TYPE,HOLY) VALUES(?,?,?)";
/*     */   private static final String SETLASTMOVED = "UPDATE ENDGAMEITEMS SET LASTMOVED=? WHERE WURMID=?";
/*  44 */   private static final Logger logger = Logger.getLogger(EndGameItem.class.getName());
/*     */   private final long wurmid;
/*     */   private final Item item;
/*     */   private final boolean holy;
/*     */   private final short type;
/*  49 */   long lastMoved = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EndGameItem(Item aItem, boolean aHoly, short aType, boolean aCreate) {
/*  61 */     this.item = aItem;
/*  62 */     this.holy = aHoly;
/*  63 */     this.type = aType;
/*  64 */     this.wurmid = aItem.getWurmId();
/*  65 */     if (aCreate) {
/*  66 */       create();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInWorld() {
/*  75 */     return (this.item.getZoneId() != -10 || this.item.getOwnerId() != -10L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void create() {
/*  83 */     Connection dbcon = null;
/*  84 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  87 */       dbcon = DbConnector.getItemDbCon();
/*  88 */       ps = dbcon.prepareStatement("INSERT INTO ENDGAMEITEMS (WURMID,TYPE,HOLY) VALUES(?,?,?)");
/*  89 */       ps.setLong(1, this.wurmid);
/*  90 */       ps.setShort(2, this.type);
/*  91 */       ps.setBoolean(3, this.holy);
/*  92 */       ps.executeUpdate();
/*     */     }
/*  94 */     catch (SQLException sqx) {
/*     */       
/*  96 */       logger.log(Level.WARNING, "Failed to create endgameitem " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 100 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 101 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void destroy() {
/* 110 */     delete();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastMoved() {
/* 115 */     return this.lastMoved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void delete() {
/* 123 */     Connection dbcon = null;
/* 124 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 127 */       dbcon = DbConnector.getItemDbCon();
/* 128 */       ps = dbcon.prepareStatement("DELETE FROM ENDGAMEITEMS WHERE WURMID=?");
/* 129 */       ps.setLong(1, this.wurmid);
/* 130 */       ps.executeUpdate();
/*     */     }
/* 132 */     catch (SQLException sqx) {
/*     */       
/* 134 */       logger.log(Level.WARNING, "Failed to delete endgameitem " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 138 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 139 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void delete(long wurmId) {
/* 149 */     Connection dbcon = null;
/* 150 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 153 */       dbcon = DbConnector.getItemDbCon();
/* 154 */       ps = dbcon.prepareStatement("DELETE FROM ENDGAMEITEMS WHERE WURMID=?");
/* 155 */       ps.setLong(1, wurmId);
/* 156 */       ps.executeUpdate();
/*     */     }
/* 158 */     catch (SQLException sqx) {
/*     */       
/* 160 */       logger.log(Level.WARNING, "Failed to delete endgameitem " + wurmId + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 164 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 165 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastMoved(long lastm) {
/* 176 */     Connection dbcon = null;
/* 177 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 180 */       this.lastMoved = lastm;
/* 181 */       dbcon = DbConnector.getItemDbCon();
/* 182 */       ps = dbcon.prepareStatement("UPDATE ENDGAMEITEMS SET LASTMOVED=? WHERE WURMID=?");
/* 183 */       ps.setLong(1, this.lastMoved);
/* 184 */       ps.setLong(2, this.wurmid);
/* 185 */       ps.executeUpdate();
/*     */     }
/* 187 */     catch (SQLException sqx) {
/*     */       
/* 189 */       logger.log(Level.WARNING, "Failed to set last moved " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 193 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 194 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWurmid() {
/* 205 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getItem() {
/* 215 */     return this.item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHoly() {
/* 225 */     return this.holy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getType() {
/* 235 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 246 */     return "EndGameItem [ID: " + this.wurmid + ", holy: " + this.holy + ", type: " + this.type + ", inWorld: " + isInWorld() + ", item: " + this.item + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\endgames\EndGameItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
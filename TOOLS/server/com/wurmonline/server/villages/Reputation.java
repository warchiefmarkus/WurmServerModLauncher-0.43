/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
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
/*     */ public final class Reputation
/*     */   implements MiscConstants, Comparable<Reputation>
/*     */ {
/*     */   private static final String DELETEREPUTATION = "DELETE FROM REPUTATION WHERE WURMID=? AND VILLAGEID=?";
/*     */   private static final String UPDATEREPUTATION = "UPDATE REPUTATION SET REPUTATION=?,PERMANENT=? WHERE WURMID=? AND VILLAGEID=?";
/*     */   private static final String CREATEREPUTATION = "INSERT INTO REPUTATION (REPUTATION,PERMANENT, WURMID,VILLAGEID) VALUES(?,?,?,?)";
/*     */   private final long wurmid;
/*     */   private final int villageId;
/*     */   private boolean permanent = false;
/*  52 */   private byte value = 0;
/*     */   
/*  54 */   private static final Logger logger = Logger.getLogger(Reputation.class.getName());
/*     */   
/*     */   private final boolean guest;
/*     */   
/*     */   Reputation(long wurmId, int village, boolean perma, int val, boolean isGuest, boolean loading) {
/*  59 */     this.wurmid = wurmId;
/*  60 */     this.villageId = village;
/*  61 */     this.permanent = perma;
/*  62 */     if (val > 100) {
/*  63 */       val = 100;
/*  64 */     } else if (val < -100) {
/*  65 */       val = -100;
/*  66 */     }  this.value = (byte)val;
/*  67 */     this.guest = isGuest;
/*  68 */     if (!loading) {
/*  69 */       create();
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
/*     */   public int compareTo(Reputation otherReputation) {
/*     */     try {
/*  83 */       return getNameFor().compareTo(otherReputation.getNameFor());
/*     */     }
/*  85 */     catch (NoSuchPlayerException e) {
/*     */       
/*  87 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*  97 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGuest() {
/* 106 */     return this.guest;
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
/*     */   void setValue(int val, boolean override) {
/* 118 */     if (val > 100) {
/* 119 */       val = 100;
/* 120 */     } else if (val < -100) {
/* 121 */       val = -100;
/* 122 */     }  if (!this.permanent || override) {
/* 123 */       this.value = (byte)val;
/*     */     }
/* 125 */     if (this.value == 0) {
/* 126 */       delete();
/*     */     } else {
/* 128 */       update();
/*     */     } 
/*     */   }
/*     */   
/*     */   public long getWurmId() {
/* 133 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */   
/*     */   public Creature getCreature() {
/* 138 */     Creature toReturn = null;
/*     */     
/*     */     try {
/* 141 */       toReturn = Server.getInstance().getCreature(this.wurmid);
/*     */     }
/* 143 */     catch (NoSuchPlayerException noSuchPlayerException) {
/*     */ 
/*     */     
/*     */     }
/* 147 */     catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */ 
/*     */     
/* 151 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNameFor() throws NoSuchPlayerException {
/* 156 */     String name = "Unknown";
/* 157 */     if (this.guest)
/* 158 */       name = name + " guest"; 
/* 159 */     if (WurmId.getType(this.wurmid) == 0) {
/*     */ 
/*     */       
/*     */       try {
/* 163 */         name = Players.getInstance().getNameFor(this.wurmid);
/*     */       }
/* 165 */       catch (IOException iox) {
/*     */         
/* 167 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/* 168 */         name = "";
/*     */       } 
/*     */     } else {
/*     */       
/* 172 */       name = name + " creature";
/* 173 */     }  return name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Village getVillage() {
/* 178 */     Village toReturn = null;
/*     */     
/*     */     try {
/* 181 */       toReturn = Villages.getVillage(this.villageId);
/*     */     }
/* 183 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 185 */       logger.log(Level.WARNING, "No village for reputation with wurmid " + this.wurmid + " and villageid " + this.villageId, (Throwable)nsv);
/*     */     } 
/* 187 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPermanent() {
/* 196 */     return this.permanent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPermanent(boolean perma) {
/* 205 */     this.permanent = perma;
/* 206 */     update();
/*     */   }
/*     */ 
/*     */   
/*     */   void modify(int val) {
/* 211 */     if (val != 0)
/*     */     {
/* 213 */       if (!this.permanent) {
/*     */         
/* 215 */         this.value = (byte)(this.value + val);
/* 216 */         if (this.value > 100) {
/* 217 */           this.value = 100;
/* 218 */         } else if (this.value < -100) {
/* 219 */           this.value = -100;
/* 220 */         }  if (this.value == 0) {
/*     */           
/* 222 */           delete();
/*     */         }
/*     */         else {
/*     */           
/* 226 */           update();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void delete() {
/* 234 */     Connection dbcon = null;
/* 235 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 238 */       dbcon = DbConnector.getZonesDbCon();
/* 239 */       ps = dbcon.prepareStatement("DELETE FROM REPUTATION WHERE WURMID=? AND VILLAGEID=?");
/* 240 */       ps.setLong(1, this.wurmid);
/* 241 */       ps.setInt(2, this.villageId);
/* 242 */       ps.executeUpdate();
/*     */     }
/* 244 */     catch (SQLException sqx) {
/*     */       
/* 246 */       logger.log(Level.WARNING, "Failed to delete reputation for wurmid=" + this.wurmid + ", village with id=" + this.villageId, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 250 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 251 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void create() {
/* 257 */     if (!this.guest) {
/*     */       
/* 259 */       Connection dbcon = null;
/* 260 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 263 */         dbcon = DbConnector.getZonesDbCon();
/* 264 */         ps = dbcon.prepareStatement("INSERT INTO REPUTATION (REPUTATION,PERMANENT, WURMID,VILLAGEID) VALUES(?,?,?,?)");
/* 265 */         ps.setByte(1, this.value);
/* 266 */         ps.setBoolean(2, this.permanent);
/*     */         
/* 268 */         ps.setLong(3, this.wurmid);
/* 269 */         ps.setInt(4, this.villageId);
/*     */         
/* 271 */         ps.executeUpdate();
/*     */       }
/* 273 */       catch (SQLException sqx) {
/*     */         
/* 275 */         logger.log(Level.WARNING, "Failed to create reputation for wurmid=" + this.wurmid + ", village with id=" + this.villageId, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 279 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 280 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void update() {
/* 287 */     if (!this.guest) {
/*     */       
/* 289 */       Connection dbcon = null;
/* 290 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 293 */         dbcon = DbConnector.getZonesDbCon();
/* 294 */         ps = dbcon.prepareStatement("UPDATE REPUTATION SET REPUTATION=?,PERMANENT=? WHERE WURMID=? AND VILLAGEID=?");
/* 295 */         ps.setByte(1, this.value);
/* 296 */         ps.setBoolean(2, this.permanent);
/*     */         
/* 298 */         ps.setLong(3, this.wurmid);
/* 299 */         ps.setInt(4, this.villageId);
/* 300 */         ps.executeUpdate();
/*     */       }
/* 302 */       catch (SQLException sqx) {
/*     */         
/* 304 */         logger.log(Level.WARNING, "Failed to update reputation for wurmid=" + this.wurmid + ", village with id=" + this.villageId, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 308 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 309 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\Reputation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
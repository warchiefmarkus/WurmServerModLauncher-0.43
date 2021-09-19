/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ListIterator;
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
/*     */ public final class DbGuardPlan
/*     */   extends GuardPlan
/*     */   implements CounterTypes
/*     */ {
/*  47 */   private static final Logger logger = Logger.getLogger(DbGuardPlan.class.getName());
/*     */   
/*     */   private static final String CREATE_GUARDPLAN = "INSERT INTO GUARDPLAN (VILLAGEID,  TYPE, LASTPAYED,MONEYLEFT, GUARDS) VALUES(?,?,?,?,?)";
/*     */   private static final String CHANGE_PLAN = "UPDATE GUARDPLAN SET LASTPAYED=?,TYPE=?,MONEYLEFT=?, GUARDS=? WHERE VILLAGEID=?";
/*     */   private static final String LOAD_PLAN = "SELECT * FROM GUARDPLAN WHERE VILLAGEID=?";
/*     */   private static final String DELETE_GUARDPLAN = "DELETE FROM GUARDPLAN WHERE VILLAGEID=?";
/*     */   private static final String ADD_RETURNEDGUARD = "INSERT INTO RETURNEDGUARDS (VILLAGEID, CREATUREID ) VALUES(?,?)";
/*     */   private static final String DELETE_RETURNEDGUARD = "DELETE FROM RETURNEDGUARDS WHERE CREATUREID=?";
/*     */   private static final String LOAD_RETURNEDGUARDS = "SELECT CREATUREID FROM RETURNEDGUARDS WHERE VILLAGEID=?";
/*     */   private static final String ADD_PAYMENT = "INSERT INTO GUARDPLANPAYMENTS (VILLAGEID, CREATUREID,MONEY,PAYED ) VALUES(?,?,?,?)";
/*     */   private static final String SET_LAST_DRAINED = "UPDATE GUARDPLAN SET LASTDRAINED=?, MONEYLEFT=? WHERE VILLAGEID=?";
/*     */   private static final String SET_DRAINMOD = "UPDATE GUARDPLAN SET DRAINMOD=? WHERE VILLAGEID=?";
/*     */   
/*     */   DbGuardPlan(int aType, int aVillageId) {
/*  61 */     super(aType, aVillageId);
/*     */   }
/*     */ 
/*     */   
/*     */   DbGuardPlan(int aVillageId) {
/*  66 */     super(aVillageId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void load() {
/*  72 */     Connection dbcon = null;
/*  73 */     PreparedStatement ps = null;
/*  74 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  77 */       dbcon = DbConnector.getZonesDbCon();
/*  78 */       ps = dbcon.prepareStatement("SELECT * FROM GUARDPLAN WHERE VILLAGEID=?");
/*  79 */       ps.setInt(1, this.villageId);
/*  80 */       rs = ps.executeQuery();
/*  81 */       boolean found = false;
/*  82 */       if (rs.next()) {
/*     */         
/*  84 */         found = true;
/*  85 */         this.type = rs.getInt("TYPE");
/*  86 */         this.lastChangedPlan = rs.getLong("LASTPAYED");
/*  87 */         this.moneyLeft = rs.getLong("MONEYLEFT");
/*  88 */         this.lastDrained = rs.getLong("LASTDRAINED");
/*  89 */         this.drainModifier = rs.getFloat("DRAINMOD");
/*  90 */         this.hiredGuardNumber = rs.getInt("GUARDS");
/*  91 */         loadReturnedGuards();
/*     */       } 
/*  93 */       if (!found)
/*     */       {
/*  95 */         this.type = 1;
/*  96 */         create();
/*     */       }
/*     */     
/*  99 */     } catch (SQLException sqx) {
/*     */       
/* 101 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 105 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 106 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void create() {
/* 113 */     this.lastChangedPlan = 0L;
/* 114 */     this.moneyLeft = 0L;
/* 115 */     Connection dbcon = null;
/* 116 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 119 */       dbcon = DbConnector.getZonesDbCon();
/* 120 */       ps = dbcon.prepareStatement("INSERT INTO GUARDPLAN (VILLAGEID,  TYPE, LASTPAYED,MONEYLEFT, GUARDS) VALUES(?,?,?,?,?)");
/* 121 */       ps.setInt(1, this.villageId);
/* 122 */       ps.setInt(2, this.type);
/* 123 */       ps.setLong(3, this.lastChangedPlan);
/* 124 */       ps.setLong(4, this.moneyLeft);
/* 125 */       ps.setInt(5, this.hiredGuardNumber);
/* 126 */       ps.executeUpdate();
/*     */     }
/* 128 */     catch (SQLException sqx) {
/*     */       
/* 130 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 134 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 135 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateGuardPlan(int aType, long aMoneyLeft, int newNumberOfHiredGuards) {
/* 142 */     this.type = aType;
/* 143 */     this.moneyLeft = aMoneyLeft;
/*     */     
/* 145 */     this.hiredGuardNumber = newNumberOfHiredGuards;
/* 146 */     Connection dbcon = null;
/* 147 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 150 */       dbcon = DbConnector.getZonesDbCon();
/* 151 */       ps = dbcon.prepareStatement("UPDATE GUARDPLAN SET LASTPAYED=?,TYPE=?,MONEYLEFT=?, GUARDS=? WHERE VILLAGEID=?");
/* 152 */       ps.setLong(1, this.lastChangedPlan);
/* 153 */       ps.setInt(2, aType);
/* 154 */       ps.setLong(3, aMoneyLeft);
/* 155 */       ps.setInt(4, this.hiredGuardNumber);
/* 156 */       ps.setInt(5, this.villageId);
/* 157 */       ps.executeUpdate();
/*     */     }
/* 159 */     catch (SQLException sqx) {
/*     */       
/* 161 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 165 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 166 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void drainGuardPlan(long aMoneyLeft) {
/* 173 */     this.moneyLeft = aMoneyLeft;
/* 174 */     this.lastDrained = System.currentTimeMillis();
/* 175 */     Connection dbcon = null;
/* 176 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 179 */       dbcon = DbConnector.getZonesDbCon();
/* 180 */       ps = dbcon.prepareStatement("UPDATE GUARDPLAN SET LASTDRAINED=?, MONEYLEFT=? WHERE VILLAGEID=?");
/* 181 */       ps.setLong(1, this.lastDrained);
/* 182 */       ps.setLong(2, aMoneyLeft);
/* 183 */       ps.setInt(3, this.villageId);
/* 184 */       ps.executeUpdate();
/*     */     }
/* 186 */     catch (SQLException sqx) {
/*     */       
/* 188 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 192 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 193 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void saveDrainMod() {
/* 200 */     Connection dbcon = null;
/* 201 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 204 */       dbcon = DbConnector.getZonesDbCon();
/* 205 */       ps = dbcon.prepareStatement("UPDATE GUARDPLAN SET DRAINMOD=? WHERE VILLAGEID=?");
/* 206 */       ps.setFloat(1, this.drainModifier);
/* 207 */       ps.setInt(2, this.villageId);
/* 208 */       ps.executeUpdate();
/*     */     }
/* 210 */     catch (SQLException sqx) {
/*     */       
/* 212 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 216 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 217 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void delete() {
/* 224 */     Connection dbcon = null;
/* 225 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 228 */       dbcon = DbConnector.getZonesDbCon();
/* 229 */       ps = dbcon.prepareStatement("DELETE FROM GUARDPLAN WHERE VILLAGEID=?");
/* 230 */       ps.setInt(1, this.villageId);
/* 231 */       ps.executeUpdate();
/*     */     }
/* 233 */     catch (SQLException sqx) {
/*     */       
/* 235 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 239 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 240 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 242 */     loadReturnedGuards();
/* 243 */     deleteReturnedGuards();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void deleteReturnedGuards() {
/* 249 */     if (this.freeGuards.size() > 0)
/*     */     {
/* 251 */       for (ListIterator<Creature> it = this.freeGuards.listIterator(); it.hasNext(); ) {
/*     */         
/* 253 */         Creature guard = it.next();
/* 254 */         removeReturnedGuard(guard.getWurmId());
/* 255 */         guard.destroy();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void addReturnedGuard(long guardId) {
/* 263 */     Connection dbcon = null;
/* 264 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 267 */       dbcon = DbConnector.getZonesDbCon();
/* 268 */       ps = dbcon.prepareStatement("INSERT INTO RETURNEDGUARDS (VILLAGEID, CREATUREID ) VALUES(?,?)");
/* 269 */       ps.setInt(1, this.villageId);
/* 270 */       ps.setLong(2, guardId);
/* 271 */       ps.executeUpdate();
/*     */     }
/* 273 */     catch (SQLException sqx) {
/*     */       
/* 275 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 279 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 280 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void removeReturnedGuard(long guardId) {
/* 287 */     Connection dbcon = null;
/* 288 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 291 */       dbcon = DbConnector.getZonesDbCon();
/* 292 */       ps = dbcon.prepareStatement("DELETE FROM RETURNEDGUARDS WHERE CREATUREID=?");
/* 293 */       ps.setLong(1, guardId);
/* 294 */       ps.executeUpdate();
/*     */     }
/* 296 */     catch (SQLException sqx) {
/*     */       
/* 298 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 302 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 303 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadReturnedGuards() {
/* 309 */     Connection dbcon = null;
/* 310 */     PreparedStatement ps = null;
/* 311 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 314 */       dbcon = DbConnector.getZonesDbCon();
/* 315 */       ps = dbcon.prepareStatement("SELECT CREATUREID FROM RETURNEDGUARDS WHERE VILLAGEID=?");
/* 316 */       ps.setInt(1, this.villageId);
/* 317 */       rs = ps.executeQuery();
/* 318 */       while (rs.next()) {
/*     */         
/* 320 */         long cid = rs.getLong("CREATUREID");
/*     */         
/*     */         try {
/* 323 */           Creature guard = Creatures.getInstance().getCreature(cid);
/* 324 */           this.freeGuards.add(guard);
/*     */         }
/* 326 */         catch (NoSuchCreatureException nsc) {
/*     */           
/* 328 */           logger.log(Level.WARNING, "Failed to retrieve creature " + cid, (Throwable)nsc);
/*     */         }
/*     */       
/*     */       } 
/* 332 */     } catch (SQLException sqx) {
/*     */       
/* 334 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 338 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 339 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPayment(String creatureName, long creatureId, long money) {
/* 346 */     Connection dbcon = null;
/* 347 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 350 */       dbcon = DbConnector.getZonesDbCon();
/* 351 */       ps = dbcon.prepareStatement("INSERT INTO GUARDPLANPAYMENTS (VILLAGEID, CREATUREID,MONEY,PAYED ) VALUES(?,?,?,?)");
/* 352 */       ps.setInt(1, this.villageId);
/* 353 */       ps.setLong(2, creatureId);
/* 354 */       ps.setLong(3, money);
/* 355 */       ps.setLong(4, WurmCalendar.currentTime);
/* 356 */       ps.executeUpdate();
/*     */     }
/* 358 */     catch (SQLException sqx) {
/*     */       
/* 360 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 364 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 365 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 369 */       if (WurmId.getType(creatureId) == 0) {
/* 370 */         getVillage().addHistory(creatureName, "added " + 
/* 371 */             Economy.getEconomy().getChangeFor(money).getChangeString() + " to upkeep");
/*     */       }
/* 373 */     } catch (NoSuchVillageException nsv) {
/*     */       
/* 375 */       logger.log(Level.WARNING, creatureName + " tried to add " + money + " irons to nonexistant village with id " + this.villageId, (Throwable)nsv);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DbGuardPlan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
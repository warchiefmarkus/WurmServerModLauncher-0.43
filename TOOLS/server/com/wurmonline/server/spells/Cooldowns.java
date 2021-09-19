/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class Cooldowns
/*     */   implements TimeConstants
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(Cooldowns.class.getName());
/*     */   
/*     */   private static final String loadCooldowns = "SELECT * FROM COOLDOWNS";
/*     */   
/*     */   private static final String deleteCooldownsFor = "DELETE FROM COOLDOWNS WHERE OWNERID=?";
/*     */   private static final String createCooldown = "INSERT INTO COOLDOWNS (OWNERID,SPELLID,AVAILABLE) VALUES(?,?,?)";
/*     */   private static final String updateCooldown = "UPDATE COOLDOWNS SET AVAILABLE=? WHERE OWNERID=? AND SPELLID=?";
/*  43 */   public final Map<Integer, Long> cooldowns = new HashMap<>();
/*  44 */   private static final Map<Long, Cooldowns> allCooldowns = new HashMap<>();
/*     */   
/*     */   private final long ownerid;
/*     */   
/*     */   private Cooldowns(long _ownerid) {
/*  49 */     this.ownerid = _ownerid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Cooldowns getCooldownsFor(long creatureId, boolean create) {
/*  54 */     Cooldowns cd = allCooldowns.get(Long.valueOf(creatureId));
/*  55 */     if (create && cd == null) {
/*     */       
/*  57 */       cd = new Cooldowns(creatureId);
/*  58 */       allCooldowns.put(Long.valueOf(creatureId), cd);
/*     */     } 
/*  60 */     return cd;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCooldown(int spellid, long availableAt, boolean loading) {
/*  65 */     boolean update = this.cooldowns.containsKey(Integer.valueOf(spellid));
/*  66 */     this.cooldowns.put(Integer.valueOf(spellid), Long.valueOf(availableAt));
/*  67 */     if (!loading)
/*     */     {
/*     */       
/*  70 */       if (System.currentTimeMillis() - availableAt > 600000L)
/*     */       {
/*  72 */         if (update) {
/*  73 */           updateToDisk(spellid, availableAt);
/*     */         } else {
/*  75 */           saveToDisk(spellid, availableAt);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public long isAvaibleAt(int spellid) {
/*  82 */     Integer tocheck = Integer.valueOf(spellid);
/*  83 */     if (this.cooldowns.containsKey(tocheck))
/*  84 */       return ((Long)this.cooldowns.get(tocheck)).longValue(); 
/*  85 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveToDisk(int spellid, long availableAt) {
/*  90 */     Connection dbcon = null;
/*  91 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  94 */       dbcon = DbConnector.getPlayerDbCon();
/*     */       
/*  96 */       ps = dbcon.prepareStatement("INSERT INTO COOLDOWNS (OWNERID,SPELLID,AVAILABLE) VALUES(?,?,?)");
/*  97 */       ps.setLong(1, this.ownerid);
/*  98 */       ps.setInt(2, spellid);
/*  99 */       ps.setLong(3, availableAt);
/* 100 */       ps.executeUpdate();
/*     */     }
/* 102 */     catch (SQLException sqex) {
/*     */       
/* 104 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 108 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 109 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateToDisk(int spellid, long availableAt) {
/* 115 */     Connection dbcon = null;
/* 116 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 119 */       dbcon = DbConnector.getPlayerDbCon();
/*     */       
/* 121 */       ps = dbcon.prepareStatement("UPDATE COOLDOWNS SET AVAILABLE=? WHERE OWNERID=? AND SPELLID=?");
/* 122 */       ps.setLong(1, availableAt);
/* 123 */       ps.setLong(2, this.ownerid);
/* 124 */       ps.setInt(3, spellid);
/* 125 */       ps.executeUpdate();
/*     */     }
/* 127 */     catch (SQLException sqex) {
/*     */       
/* 129 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 133 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 134 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void deleteCooldownsFor(long ownerId) {
/* 140 */     Connection dbcon = null;
/* 141 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 144 */       dbcon = DbConnector.getPlayerDbCon();
/* 145 */       ps = dbcon.prepareStatement("DELETE FROM COOLDOWNS WHERE OWNERID=?");
/* 146 */       ps.setLong(1, ownerId);
/* 147 */       ps.executeUpdate();
/*     */     }
/* 149 */     catch (SQLException sqex) {
/*     */       
/* 151 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 155 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 156 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 158 */     allCooldowns.remove(Long.valueOf(ownerId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllCooldowns() {
/* 163 */     logger.log(Level.INFO, "Loading all cooldowns.");
/* 164 */     long start = System.nanoTime();
/* 165 */     Connection dbcon = null;
/* 166 */     PreparedStatement ps = null;
/* 167 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 170 */       dbcon = DbConnector.getPlayerDbCon();
/* 171 */       ps = dbcon.prepareStatement("SELECT * FROM COOLDOWNS");
/* 172 */       rs = ps.executeQuery();
/* 173 */       while (rs.next())
/*     */       {
/* 175 */         long ownerId = rs.getLong("OWNERID");
/* 176 */         Cooldowns cd = getCooldownsFor(ownerId, false);
/* 177 */         if (cd == null)
/* 178 */           cd = new Cooldowns(ownerId); 
/* 179 */         cd.addCooldown(rs.getInt("SPELLID"), rs.getLong("AVAILABLE"), true);
/* 180 */         allCooldowns.put(Long.valueOf(ownerId), cd);
/*     */       }
/*     */     
/* 183 */     } catch (SQLException sqx) {
/*     */       
/* 185 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 189 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 190 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 192 */       long end = System.nanoTime();
/* 193 */       logger.info("Loaded cooldowns from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Cooldowns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
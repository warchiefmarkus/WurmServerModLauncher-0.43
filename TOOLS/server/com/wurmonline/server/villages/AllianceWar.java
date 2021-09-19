/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
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
/*     */ public class AllianceWar
/*     */   implements TimeConstants
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(AllianceWar.class.getName());
/*     */   
/*     */   private final int aggressor;
/*     */   
/*     */   private final int defender;
/*     */   
/*     */   private final long declarationTime;
/*     */   
/*     */   private long warStartedTime;
/*     */   
/*     */   private long peaceDeclaredTime;
/*     */   
/*     */   private static final String LOAD_ALL = "SELECT * FROM ALLIANCEWARS";
/*     */   private static final String INSERTWAR = "INSERT INTO ALLIANCEWARS (ALLIANCEONE,ALLIANCETWO,TIMEDECLARED) VALUES (?,?,?)";
/*     */   private static final String DELETEWAR = "DELETE FROM ALLIANCEWARS WHERE ALLIANCEONE=? AND ALLIANCETWO=?";
/*     */   private static final String SETWARSTARTED = "UPDATE ALLIANCEWARS SET TIMESTARTED=? WHERE ALLIANCEONE=? AND ALLIANCETWO=?";
/*     */   private static final String SETPEACEWANTED = "UPDATE ALLIANCEWARS SET TIMEPEACE=? WHERE ALLIANCEONE=? AND ALLIANCETWO=?";
/*     */   public static final long TIME_UNTIL_PEACE = 345600000L;
/*     */   
/*     */   public AllianceWar(int aggressorId, int defenderId) {
/*  59 */     this.aggressor = aggressorId;
/*  60 */     this.defender = defenderId;
/*  61 */     this.declarationTime = System.currentTimeMillis();
/*     */     
/*  63 */     PvPAlliance one = PvPAlliance.getPvPAlliance(this.aggressor);
/*  64 */     if (one != null) {
/*     */       
/*  66 */       PvPAlliance two = PvPAlliance.getPvPAlliance(this.defender);
/*  67 */       if (two != null) {
/*     */         
/*  69 */         one.addAllianceWar(this);
/*  70 */         two.addAllianceWar(this);
/*  71 */         create();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AllianceWar(int aggressorId, int defenderId, long declared, long started, long peacetime) {
/*  82 */     this.aggressor = aggressorId;
/*  83 */     this.defender = defenderId;
/*  84 */     this.declarationTime = declared;
/*  85 */     this.warStartedTime = started;
/*  86 */     this.peaceDeclaredTime = peacetime;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAll() {
/*  91 */     logger.log(Level.INFO, "Loading all alliance wars.");
/*  92 */     long start = System.nanoTime();
/*  93 */     Connection dbcon = null;
/*  94 */     PreparedStatement ps = null;
/*  95 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  98 */       dbcon = DbConnector.getZonesDbCon();
/*  99 */       ps = dbcon.prepareStatement("SELECT * FROM ALLIANCEWARS");
/* 100 */       rs = ps.executeQuery();
/* 101 */       while (rs.next()) {
/*     */         
/* 103 */         int allianceOne = rs.getInt("ALLIANCEONE");
/* 104 */         int allianceTwo = rs.getInt("ALLIANCETWO");
/* 105 */         long declared = rs.getLong("TIMEDECLARED");
/* 106 */         long started = rs.getLong("TIMESTARTED");
/* 107 */         long peace = rs.getLong("TIMEPEACE");
/* 108 */         PvPAlliance one = PvPAlliance.getPvPAlliance(allianceOne);
/* 109 */         if (one != null) {
/*     */           
/* 111 */           PvPAlliance two = PvPAlliance.getPvPAlliance(allianceTwo);
/* 112 */           if (two != null)
/*     */           {
/* 114 */             AllianceWar war = new AllianceWar(allianceOne, allianceTwo, declared, started, peace);
/* 115 */             one.addAllianceWar(war);
/* 116 */             two.addAllianceWar(war);
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 121 */     } catch (SQLException sqx) {
/*     */       
/* 123 */       logger.log(Level.WARNING, "Failed to load pvp alliance wars " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 127 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 128 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 130 */       long end = System.nanoTime();
/* 131 */       logger.info("Loaded alliance wars from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void delete() {
/* 138 */     PvPAlliance one = PvPAlliance.getPvPAlliance(this.aggressor);
/* 139 */     if (one != null)
/* 140 */       one.removeWar(this); 
/* 141 */     PvPAlliance two = PvPAlliance.getPvPAlliance(this.defender);
/* 142 */     if (two != null) {
/* 143 */       two.removeWar(this);
/*     */     }
/* 145 */     Connection dbcon = null;
/* 146 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 149 */       dbcon = DbConnector.getZonesDbCon();
/* 150 */       ps = dbcon.prepareStatement("DELETE FROM ALLIANCEWARS WHERE ALLIANCEONE=? AND ALLIANCETWO=?");
/* 151 */       ps.setInt(1, this.aggressor);
/* 152 */       ps.setInt(2, this.defender);
/* 153 */       ps.executeUpdate();
/*     */     }
/* 155 */     catch (SQLException sqx) {
/*     */       
/* 157 */       logger.log(Level.WARNING, "Failed to insert pvp alliance war " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 161 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 162 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final void create() {
/* 168 */     Connection dbcon = null;
/* 169 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 172 */       dbcon = DbConnector.getZonesDbCon();
/* 173 */       ps = dbcon.prepareStatement("INSERT INTO ALLIANCEWARS (ALLIANCEONE,ALLIANCETWO,TIMEDECLARED) VALUES (?,?,?)");
/* 174 */       ps.setInt(1, this.aggressor);
/* 175 */       ps.setInt(2, this.defender);
/* 176 */       ps.setLong(3, this.declarationTime);
/* 177 */       ps.executeUpdate();
/*     */     }
/* 179 */     catch (SQLException sqx) {
/*     */       
/* 181 */       logger.log(Level.WARNING, "Failed to insert pvp alliance war " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 185 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 186 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasBeenAccepted() {
/* 192 */     return (this.warStartedTime > 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isActive() {
/* 197 */     return (hasBeenAccepted() && !hasEnded());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasEnded() {
/* 202 */     return (System.currentTimeMillis() > this.peaceDeclaredTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAggressor() {
/* 212 */     return this.aggressor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefender() {
/* 222 */     return this.defender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDeclarationTime() {
/* 232 */     return this.declarationTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWarStartedTime() {
/* 242 */     return this.warStartedTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWarStartedTime() {
/* 250 */     this.warStartedTime = System.currentTimeMillis();
/* 251 */     Connection dbcon = null;
/* 252 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 255 */       dbcon = DbConnector.getZonesDbCon();
/* 256 */       ps = dbcon.prepareStatement("UPDATE ALLIANCEWARS SET TIMESTARTED=? WHERE ALLIANCEONE=? AND ALLIANCETWO=?");
/* 257 */       ps.setLong(1, this.warStartedTime);
/* 258 */       ps.setInt(2, this.aggressor);
/* 259 */       ps.setInt(3, this.defender);
/* 260 */       ps.executeUpdate();
/*     */     }
/* 262 */     catch (SQLException sqx) {
/*     */       
/* 264 */       logger.log(Level.WARNING, "Failed to set war started in pvp alliance war " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 268 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 269 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPeaceDeclaredTime() {
/* 280 */     return this.peaceDeclaredTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPeaceDeclaredTime() {
/* 289 */     this.peaceDeclaredTime = System.currentTimeMillis() + 345600000L;
/* 290 */     Connection dbcon = null;
/* 291 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 294 */       dbcon = DbConnector.getZonesDbCon();
/* 295 */       ps = dbcon.prepareStatement("UPDATE ALLIANCEWARS SET TIMEPEACE=? WHERE ALLIANCEONE=? AND ALLIANCETWO=?");
/* 296 */       ps.setLong(1, this.peaceDeclaredTime);
/* 297 */       ps.setInt(2, this.aggressor);
/* 298 */       ps.setInt(3, this.defender);
/* 299 */       ps.executeUpdate();
/*     */     }
/* 301 */     catch (SQLException sqx) {
/*     */       
/* 303 */       logger.log(Level.WARNING, "Failed to set peace declared in pvp alliance war " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 307 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 308 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\AllianceWar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
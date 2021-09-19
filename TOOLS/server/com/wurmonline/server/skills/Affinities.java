/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Affinities
/*     */ {
/*  40 */   private static Logger logger = Logger.getLogger(Affinities.class.getName());
/*     */   private static final String updatePlayerAffinity = "update AFFINITIES set NUMBER=? where WURMID=? AND SKILL=?";
/*     */   private static final String createPlayerAffinity = "INSERT INTO AFFINITIES (WURMID,SKILL,NUMBER) VALUES(?,?,?)";
/*     */   private static final String deletePlayerAffinity = "DELETE FROM AFFINITIES WHERE WURMID=? AND SKILL=?";
/*     */   private static final String loadAllAffinities = "SELECT * FROM AFFINITIES WHERE NUMBER>0";
/*     */   private static final String deleteAllPlayerAffinity = "DELETE FROM AFFINITIES WHERE WURMID=?";
/*  46 */   private static Map<Long, Set<Affinity>> affinities = new HashMap<>();
/*  47 */   private static Affinity toRemove = null;
/*     */   private static boolean found = false;
/*  49 */   private static final Affinity[] emptyAffs = new Affinity[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadAffinities() {
/*  63 */     long start = System.nanoTime();
/*  64 */     int loadedAffinities = 0;
/*  65 */     Connection dbcon = null;
/*  66 */     PreparedStatement ps = null;
/*  67 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  70 */       affinities = new HashMap<>();
/*  71 */       dbcon = DbConnector.getPlayerDbCon();
/*  72 */       ps = dbcon.prepareStatement("SELECT * FROM AFFINITIES WHERE NUMBER>0");
/*  73 */       rs = ps.executeQuery();
/*  74 */       while (rs.next())
/*     */       {
/*  76 */         setAffinity(rs.getLong("WURMID"), rs.getInt("SKILL"), rs.getByte("NUMBER"), true);
/*  77 */         loadedAffinities++;
/*     */       }
/*     */     
/*  80 */     } catch (SQLException sqx) {
/*     */       
/*  82 */       logger.log(Level.WARNING, "Failed to load affinities!", sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  86 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  87 */       DbConnector.returnConnection(dbcon);
/*  88 */       long end = System.nanoTime();
/*  89 */       logger.info("Loaded " + loadedAffinities + " affinities from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setAffinity(long playerid, int skillnumber, int value, boolean loading) {
/*  96 */     found = false;
/*  97 */     Long idl = Long.valueOf(playerid);
/*  98 */     Set<Affinity> affs = affinities.get(idl);
/*  99 */     if (affs == null) {
/*     */       
/* 101 */       affs = new HashSet<>();
/* 102 */       affinities.put(idl, affs);
/*     */     } 
/* 104 */     for (Affinity a : affs) {
/*     */       
/* 106 */       if (a.skillNumber == skillnumber) {
/*     */         
/* 108 */         found = true;
/* 109 */         a.number = Math.min(5, value);
/* 110 */         if (!loading) {
/*     */           
/* 112 */           setAffinityForSkill(playerid, a.skillNumber, a.number);
/* 113 */           updateAffinity(playerid, a.skillNumber, a.number);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 119 */     value = Math.min(5, value);
/* 120 */     if (!found && !loading) {
/*     */       
/* 122 */       createAffinity(playerid, skillnumber, value);
/* 123 */       setAffinityForSkill(playerid, skillnumber, value);
/*     */     } 
/*     */     
/* 126 */     affs.add(new Affinity(skillnumber, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void decreaseAffinity(long playerid, int skillnum, int value) {
/* 131 */     Long idl = Long.valueOf(playerid);
/* 132 */     Set<Affinity> affs = affinities.get(idl);
/* 133 */     toRemove = null;
/* 134 */     if (affs == null) {
/*     */       
/* 136 */       logger.log(Level.WARNING, "Affinities not found when removing from " + playerid);
/*     */       return;
/*     */     } 
/* 139 */     for (Iterator<Affinity> it = affs.iterator(); it.hasNext(); ) {
/*     */       
/* 141 */       Affinity a = it.next();
/* 142 */       if (a.skillNumber == skillnum) {
/*     */         
/* 144 */         a.number -= value;
/* 145 */         setAffinityForSkill(playerid, skillnum, Math.max(0, a.number));
/* 146 */         if (a.number <= 0)
/* 147 */           toRemove = a; 
/*     */         break;
/*     */       } 
/*     */     } 
/* 151 */     if (toRemove != null) {
/*     */       
/* 153 */       affs.remove(toRemove);
/* 154 */       deleteAffinity(playerid, skillnum);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setAffinityForSkill(long playerid, int skillid, int number) {
/*     */     try {
/* 162 */       Player p = Players.getInstance().getPlayer(playerid);
/* 163 */       if (p != null) {
/*     */         
/*     */         try {
/*     */           
/* 167 */           Skill s = p.getSkills().getSkill(skillid);
/* 168 */           s.setAffinity(number);
/*     */         }
/* 170 */         catch (NoSuchSkillException noSuchSkillException) {}
/*     */ 
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 176 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void deleteAffinity(long playerid, int skillnum) {
/* 184 */     Connection dbcon = null;
/* 185 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 188 */       dbcon = DbConnector.getPlayerDbCon();
/* 189 */       ps = dbcon.prepareStatement("DELETE FROM AFFINITIES WHERE WURMID=? AND SKILL=?");
/* 190 */       ps.setLong(1, playerid);
/* 191 */       ps.setInt(2, skillnum);
/* 192 */       ps.executeUpdate();
/*     */     }
/* 194 */     catch (SQLException sqx) {
/*     */       
/* 196 */       logger.log(Level.WARNING, "Failed to delete affinity " + skillnum + " for " + playerid, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 200 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 201 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteAllPlayerAffinity(long playerid) {
/* 207 */     Set<Affinity> set = affinities.get(Long.valueOf(playerid));
/* 208 */     if (set != null)
/* 209 */       set.clear(); 
/* 210 */     Connection dbcon = null;
/* 211 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 214 */       dbcon = DbConnector.getPlayerDbCon();
/* 215 */       ps = dbcon.prepareStatement("DELETE FROM AFFINITIES WHERE WURMID=?");
/* 216 */       ps.setLong(1, playerid);
/* 217 */       ps.executeUpdate();
/*     */     }
/* 219 */     catch (SQLException sqx) {
/*     */       
/* 221 */       logger.log(Level.WARNING, "Failed to delete affinities for " + playerid, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 225 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 226 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void updateAffinity(long playerid, int skillnum, int number) {
/* 232 */     Connection dbcon = null;
/* 233 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 236 */       dbcon = DbConnector.getPlayerDbCon();
/* 237 */       ps = dbcon.prepareStatement("update AFFINITIES set NUMBER=? where WURMID=? AND SKILL=?");
/* 238 */       ps.setByte(1, (byte)number);
/* 239 */       ps.setLong(2, playerid);
/* 240 */       ps.setInt(3, skillnum);
/*     */       
/* 242 */       ps.executeUpdate();
/*     */     }
/* 244 */     catch (SQLException sqx) {
/*     */       
/* 246 */       logger.log(Level.WARNING, "Failed to update affinity " + skillnum + " for " + playerid, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 250 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 251 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void createAffinity(long playerid, int skillnum, int number) {
/* 257 */     Connection dbcon = null;
/* 258 */     PreparedStatement ps = null;
/*     */ 
/*     */     
/*     */     try {
/* 262 */       dbcon = DbConnector.getPlayerDbCon();
/* 263 */       ps = dbcon.prepareStatement("INSERT INTO AFFINITIES (WURMID,SKILL,NUMBER) VALUES(?,?,?)");
/* 264 */       ps.setLong(1, playerid);
/* 265 */       ps.setInt(2, skillnum);
/* 266 */       ps.setByte(3, (byte)number);
/*     */       
/* 268 */       ps.executeUpdate();
/*     */     }
/* 270 */     catch (SQLException sqx) {
/*     */       
/* 272 */       logger.log(Level.WARNING, "Failed to create affinity " + skillnum + " for " + playerid + " nums=" + number, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 276 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 277 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Affinity[] getAffinities(long playerid) {
/* 284 */     Set<Affinity> affs = affinities.get(Long.valueOf(playerid));
/* 285 */     if (affs == null || affs.isEmpty()) {
/* 286 */       return emptyAffs;
/*     */     }
/* 288 */     return affs.<Affinity>toArray(new Affinity[affs.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\Affinities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
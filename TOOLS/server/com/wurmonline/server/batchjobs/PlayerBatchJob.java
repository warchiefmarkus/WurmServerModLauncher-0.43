/*     */ package com.wurmonline.server.batchjobs;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.intra.IntraServerConnection;
/*     */ import com.wurmonline.server.items.BodyDbStrings;
/*     */ import com.wurmonline.server.items.CoinDbStrings;
/*     */ import com.wurmonline.server.items.ItemDbStrings;
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
/*     */ public final class PlayerBatchJob
/*     */   implements TimeConstants, MiscConstants
/*     */ {
/*     */   private static final String deleteSkills = "DELETE FROM SKILLS WHERE OWNER=?";
/*     */   private static final String deletePlayer = "DELETE FROM PLAYERS WHERE WURMID=?";
/*  49 */   private static final String getPlayers = "SELECT WURMID FROM PLAYERS WHERE LASTLOGOUT<" + (
/*  50 */     System.currentTimeMillis() - 2419200000L);
/*     */   
/*     */   private static final String getAllPlayers = "SELECT WURMID,PASSWORD FROM WURMPLAYERS2.PLAYERS";
/*     */   
/*     */   private static final String getAllReimbs = "SELECT * FROM REIMB WHERE CREATED=0";
/*     */   
/*     */   private static final String insertPassword = "UPDATE PLAYERS SET PASSWORD=? WHERE WURMID=?";
/*     */   private static final String getChampions = "SELECT WURMID FROM PLAYERS WHERE REALDEATH>0 AND REALDEATH<5";
/*     */   private static final String revertChampFaithFavor = "UPDATE PLAYERS SET FAITH=50, FAVOR=50, REALDEATH=0,PRIEST=1 WHERE WURMID=?";
/*     */   private static final String updateChampSkillStepOne = "UPDATE SKILLS SET VALUE=VALUE-50, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=?";
/*     */   private static final String updateChampSkillStepTwo = "UPDATE SKILLS SET VALUE=10, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=? AND VALUE<10";
/*     */   private static final String updateChampSkillStatOne = "UPDATE SKILLS SET VALUE=VALUE-5, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=?";
/*     */   private static final String selectChanneling = "SELECT * FROM SKILLS WHERE OWNER=? AND NUMBER=10067";
/*     */   private static final String updateChanneling = "UPDATE SKILLS SET VALUE=?, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=10067";
/*     */   private static final String reimburseFatigue = "UPDATE PLAYERS SET FATIGUE=?,LASTFATIGUE=?,SLEEP=LEAST(36000,SLEEP+?)";
/*  65 */   private static Logger logger = Logger.getLogger(PlayerBatchJob.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String updateReimb = "UPDATE REIMB SET CREATED=1 WHERE NAME=?";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void monthlyPrune() {
/*     */     try {
/*  79 */       Connection pdbcon = DbConnector.getPlayerDbCon();
/*  80 */       Connection idbcon = DbConnector.getItemDbCon();
/*     */       
/*  82 */       PreparedStatement ps = pdbcon.prepareStatement(getPlayers);
/*  83 */       ResultSet rs = ps.executeQuery();
/*  84 */       while (rs.next()) {
/*     */         
/*  86 */         long owner = rs.getLong("WURMID");
/*  87 */         PreparedStatement ps2 = idbcon.prepareStatement(ItemDbStrings.getInstance().deleteByOwnerId());
/*  88 */         ps2.setLong(1, owner);
/*  89 */         ps2.executeUpdate();
/*  90 */         ps2.close();
/*     */         
/*  92 */         PreparedStatement ps3 = pdbcon.prepareStatement("DELETE FROM SKILLS WHERE OWNER=?");
/*  93 */         ps3.setLong(1, owner);
/*  94 */         ps3.executeUpdate();
/*  95 */         ps3.close();
/*  96 */         PreparedStatement ps4 = pdbcon.prepareStatement("DELETE FROM PLAYERS WHERE WURMID=?");
/*  97 */         ps4.setLong(1, owner);
/*  98 */         ps4.executeUpdate();
/*  99 */         ps4.close();
/* 100 */         PreparedStatement ps5 = idbcon.prepareStatement(BodyDbStrings.getInstance().deleteByOwnerId());
/* 101 */         ps5.setLong(1, owner);
/* 102 */         ps5.executeUpdate();
/* 103 */         ps5.close();
/* 104 */         PreparedStatement ps6 = idbcon.prepareStatement(CoinDbStrings.getInstance().deleteByOwnerId());
/* 105 */         ps6.setLong(1, owner);
/* 106 */         ps6.executeUpdate();
/* 107 */         ps6.close();
/*     */       } 
/* 109 */       rs.close();
/* 110 */       ps.close();
/*     */     }
/* 112 */     catch (SQLException sqx) {
/*     */       
/* 114 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void reimburseFatigue() {
/* 120 */     logger.log(Level.INFO, "Wurm crashed. Reimbursing fatigue for all players.");
/*     */     
/*     */     try {
/* 123 */       Connection pdbcon = DbConnector.getPlayerDbCon();
/* 124 */       PreparedStatement ps = pdbcon.prepareStatement("UPDATE PLAYERS SET FATIGUE=?,LASTFATIGUE=?,SLEEP=LEAST(36000,SLEEP+?)");
/* 125 */       ps.setLong(1, 43200L);
/* 126 */       ps.setLong(2, System.currentTimeMillis());
/* 127 */       ps.setLong(3, 18000L);
/* 128 */       ps.executeUpdate();
/* 129 */       ps.close();
/*     */     }
/* 131 */     catch (SQLException sqx) {
/*     */       
/* 133 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void fixPasswords() {
/* 139 */     logger.log(Level.INFO, "Fixing passwords.");
/*     */     
/*     */     try {
/* 142 */       long wurmid = -1L;
/* 143 */       String password = "";
/* 144 */       Connection pdbcon = DbConnector.getPlayerDbCon();
/* 145 */       PreparedStatement ps = pdbcon.prepareStatement("SELECT WURMID,PASSWORD FROM WURMPLAYERS2.PLAYERS");
/* 146 */       ResultSet rs = ps.executeQuery();
/* 147 */       int nums = 0;
/* 148 */       while (rs.next()) {
/*     */         
/* 150 */         wurmid = rs.getLong("WURMID");
/* 151 */         password = rs.getString("PASSWORD");
/* 152 */         PreparedStatement ps2 = pdbcon.prepareStatement("UPDATE PLAYERS SET PASSWORD=? WHERE WURMID=?");
/* 153 */         ps2.setString(1, password);
/* 154 */         ps2.setLong(2, wurmid);
/* 155 */         ps2.executeUpdate();
/* 156 */         ps2.close();
/* 157 */         nums++;
/*     */       } 
/* 159 */       rs.close();
/* 160 */       ps.close();
/* 161 */       logger.log(Level.INFO, "Fixed " + nums + " passwords.");
/*     */     }
/* 163 */     catch (SQLException sqx) {
/*     */       
/* 165 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void createReimbursedAccs() {
/*     */     try {
/* 173 */       Connection pdbcon = DbConnector.getPlayerDbCon();
/* 174 */       PreparedStatement ps = pdbcon.prepareStatement("SELECT * FROM REIMB WHERE CREATED=0");
/* 175 */       ResultSet rs = ps.executeQuery();
/* 176 */       int nums = 0;
/* 177 */       boolean wild = false;
/* 178 */       int serverId = 2;
/* 179 */       byte kingdom = 1;
/* 180 */       String name = "";
/* 181 */       String password = "";
/* 182 */       String email = "";
/* 183 */       String challengePhrase = "";
/* 184 */       String answer = "";
/* 185 */       byte power = 0;
/* 186 */       byte gender = 0;
/* 187 */       while (rs.next()) {
/*     */         
/* 189 */         serverId = 2;
/* 190 */         name = rs.getString("NAME");
/* 191 */         email = rs.getString("EMAIL");
/* 192 */         password = rs.getString("PASSWORD");
/* 193 */         if (password == null || password.length() == 0)
/* 194 */           password = name + "kjhoiu1131"; 
/* 195 */         challengePhrase = rs.getString("PWQUESTION");
/* 196 */         if (challengePhrase == null || challengePhrase.length() == 0)
/* 197 */           challengePhrase = ""; 
/* 198 */         answer = rs.getString("PWANSWER");
/* 199 */         if (answer == null || answer.length() == 0)
/* 200 */           answer = ""; 
/* 201 */         wild = rs.getBoolean("WILD");
/* 202 */         power = rs.getByte("POWER");
/* 203 */         kingdom = rs.getByte("KINGDOM");
/* 204 */         gender = rs.getByte("GENDER");
/* 205 */         if (wild)
/* 206 */           serverId = 3; 
/* 207 */         ServerEntry toCreateOn = Servers.getServerWithId(serverId);
/* 208 */         if (toCreateOn != null) {
/*     */ 
/*     */ 
/*     */           
/* 212 */           int tilex = toCreateOn.SPAWNPOINTJENNX;
/* 213 */           int tiley = toCreateOn.SPAWNPOINTJENNY;
/*     */           
/* 215 */           if (kingdom == 3) {
/*     */             
/* 217 */             tilex = toCreateOn.SPAWNPOINTLIBX;
/* 218 */             tiley = toCreateOn.SPAWNPOINTLIBY;
/*     */           } 
/* 220 */           LoginServerWebConnection lsw = new LoginServerWebConnection(serverId);
/*     */           
/*     */           try {
/* 223 */             logger.log(Level.INFO, "Creating " + name + " on server " + serverId);
/* 224 */             byte[] playerData = lsw.createAndReturnPlayer(name, password, challengePhrase, answer, email, kingdom, power, Server.rand
/* 225 */                 .nextLong(), gender, true, true, true);
/*     */             
/* 227 */             long wurmId = IntraServerConnection.savePlayerToDisk(playerData, tilex, tiley, true, true);
/* 228 */             nums++;
/* 229 */             PreparedStatement ps2 = pdbcon.prepareStatement("UPDATE REIMB SET CREATED=1 WHERE NAME=?");
/* 230 */             ps2.setString(1, name);
/* 231 */             ps2.executeUpdate();
/* 232 */             ps2.close();
/*     */           }
/* 234 */           catch (Exception ex) {
/*     */             
/* 236 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */           } 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 243 */         logger.log(Level.WARNING, "Failed to create player " + name + ": The desired server " + serverId + " does not exist.");
/*     */       } 
/*     */ 
/*     */       
/* 247 */       rs.close();
/* 248 */       ps.close();
/* 249 */       logger.log(Level.INFO, "Created " + nums + " players.");
/*     */     }
/* 251 */     catch (SQLException sqx) {
/*     */       
/* 253 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void removeChampions() {
/*     */     try {
/* 261 */       Connection pdbcon = DbConnector.getPlayerDbCon();
/*     */       
/* 263 */       PreparedStatement ps = pdbcon.prepareStatement("SELECT WURMID FROM PLAYERS WHERE REALDEATH>0 AND REALDEATH<5");
/* 264 */       ResultSet rs = ps.executeQuery();
/* 265 */       while (rs.next()) {
/*     */         
/* 267 */         long owner = rs.getLong("WURMID");
/* 268 */         PreparedStatement ps2 = pdbcon.prepareStatement("UPDATE PLAYERS SET FAITH=50, FAVOR=50, REALDEATH=0,PRIEST=1 WHERE WURMID=?");
/* 269 */         ps2.setLong(1, owner);
/* 270 */         ps2.executeUpdate();
/* 271 */         ps2.close();
/*     */         
/* 273 */         updateChampSkill(10066, owner, pdbcon);
/* 274 */         updateChampSkill(10068, owner, pdbcon);
/* 275 */         updateChampStat(104, owner, pdbcon);
/* 276 */         updateChampStat(102, owner, pdbcon);
/* 277 */         updateChampStat(103, owner, pdbcon);
/* 278 */         updateChampStat(100, owner, pdbcon);
/* 279 */         updateChampStat(101, owner, pdbcon);
/* 280 */         updateChampStat(106, owner, pdbcon);
/* 281 */         updateChampStat(105, owner, pdbcon);
/* 282 */         fixChanneling(owner, pdbcon);
/*     */       } 
/* 284 */       rs.close();
/* 285 */       ps.close();
/*     */     }
/* 287 */     catch (SQLException sqx) {
/*     */       
/* 289 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void updateChampSkill(int number, long wurmid, Connection dbcon) {
/*     */     try {
/* 297 */       PreparedStatement ps = dbcon.prepareStatement("UPDATE SKILLS SET VALUE=VALUE-50, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=?");
/* 298 */       ps.setLong(1, wurmid);
/* 299 */       ps.setInt(2, number);
/* 300 */       ps.executeUpdate();
/* 301 */       ps.close();
/*     */       
/* 303 */       PreparedStatement ps2 = dbcon.prepareStatement("UPDATE SKILLS SET VALUE=10, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=? AND VALUE<10");
/* 304 */       ps2.setLong(1, wurmid);
/* 305 */       ps2.setInt(2, number);
/* 306 */       ps2.executeUpdate();
/* 307 */       ps2.close();
/*     */     }
/* 309 */     catch (SQLException sqx) {
/*     */       
/* 311 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void updateChampStat(int number, long wurmid, Connection dbcon) {
/*     */     try {
/* 319 */       PreparedStatement ps = dbcon.prepareStatement("UPDATE SKILLS SET VALUE=VALUE-5, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=?");
/* 320 */       ps.setLong(1, wurmid);
/* 321 */       ps.setInt(2, number);
/* 322 */       ps.executeUpdate();
/* 323 */       ps.close();
/*     */     }
/* 325 */     catch (SQLException sqx) {
/*     */       
/* 327 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void fixChanneling(long wurmid, Connection dbcon) {
/*     */     try {
/* 335 */       PreparedStatement ps = dbcon.prepareStatement("SELECT * FROM SKILLS WHERE OWNER=? AND NUMBER=10067");
/* 336 */       ps.setLong(1, wurmid);
/* 337 */       ResultSet rs = ps.executeQuery();
/* 338 */       if (rs.next())
/*     */       {
/* 340 */         float value = rs.getFloat("VALUE");
/* 341 */         ps.close();
/* 342 */         rs.close();
/*     */         
/* 344 */         float newValue = value - 50.0F;
/* 345 */         if (value > 80.0F) {
/*     */           
/* 347 */           newValue += (value - 80.0F) * 2.0F;
/* 348 */           if (value > 85.0F)
/* 349 */             newValue += (value - 85.0F) * 2.0F; 
/* 350 */           if (value > 90.0F)
/* 351 */             newValue += (value - 90.0F) * 2.0F; 
/*     */         } 
/* 353 */         PreparedStatement ps2 = dbcon.prepareStatement("UPDATE SKILLS SET VALUE=?, MINVALUE=VALUE WHERE OWNER=? AND NUMBER=10067");
/* 354 */         ps2.setFloat(1, newValue);
/* 355 */         ps2.setLong(2, wurmid);
/* 356 */         ps2.executeUpdate();
/* 357 */         ps2.close();
/*     */       }
/*     */     
/* 360 */     } catch (SQLException sqx) {
/*     */       
/* 362 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\batchjobs\PlayerBatchJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
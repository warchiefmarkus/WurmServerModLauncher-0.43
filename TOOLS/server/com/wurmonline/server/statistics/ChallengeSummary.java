/*     */ package com.wurmonline.server.statistics;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentSkipListSet;
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
/*     */ public class ChallengeSummary
/*     */   implements MiscConstants
/*     */ {
/*     */   private static final String loadAllScores = "SELECT * FROM CHALLENGE";
/*     */   private static final String insertScore = "INSERT INTO CHALLENGE(LASTUPDATED,WURMID,ROUND,TYPE,POINTS,LASTPOINTS) VALUES (?,?,?,?,?,?)";
/*     */   private static final String updateScore = "UPDATE CHALLENGE SET LASTUPDATED=?,POINTS=?,LASTPOINTS=? WHERE WURMID=? AND ROUND=? AND TYPE=?";
/*  59 */   private static final Logger logger = Logger.getLogger(ChallengeSummary.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static final Map<Long, ChallengeSummary> allScores = new ConcurrentHashMap<>();
/*     */   
/*     */   private static boolean isDirty = false;
/*     */   
/*     */   private final long wid;
/*     */   private final String name;
/*  70 */   private static final ChallengeScore[] topScores = new ChallengeScore[(ChallengePointEnum.ChallengePoint.getTypes()).length];
/*  71 */   private static final String[] topScorers = new String[(ChallengePointEnum.ChallengePoint.getTypes()).length];
/*     */ 
/*     */ 
/*     */   
/*  75 */   private final ConcurrentHashMap<Integer, ChallengeRound> privateRounds = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*     */   static final String start = "<TABLE id=\"gameDataTable\">\n\t\t<TR class=\"gameDataTopTenTR\">\n\t\t\t<TH>Name</TH>\n\t\t\t<TH>Points</TH>\n\t\t\t<TH>Last points</TH>\n\t\t\t<TH>Date</TH>\n\t\t</TR>\n\t\t";
/*     */ 
/*     */   
/*     */   private static final String header = "<!DOCTYPE html><HTML>\n\t<HEAD>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><TITLE>Wurm Online Challenge Standings</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t";
/*     */ 
/*     */   
/*     */   private static final String rootdir = "/var/www/challenge/";
/*     */   
/*  86 */   private static String headerFilename = "/var/www/challenge/main" + ChallengePointEnum.ChallengeScenario.current.getNum() + ".html";
/*     */   
/*     */   private static final String mainHeader = "<!DOCTYPE html><HTML>\n\t<HEAD>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><TITLE>Wurm Online Challenge Standings</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t";
/*     */   
/*     */   static final String headerStart = "<TABLE id=\"gameDataTable\">\n\t\t<TR>\n\t\t\t<TH>Challenge</TH>\n\t\t\t<TH>Leader</TH>\n\t\t\t<TH>Points</TH>\n\t\t\t<TH>Last Points</TH>\n\t\t\t<TH>Date</TH>\n\t\t</TR>\n\t\t";
/*     */   
/*     */   private static final String tablefooter = "</TABLE>\n\n";
/*     */   private static final String pagefooter = "</BODY>\n</HTML>";
/*     */   private boolean fileExists = false;
/*  95 */   private String filename = "";
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean writing = false;
/*     */ 
/*     */   
/*     */   static final String hscpStart = "<TABLE id=\"gameDataTable\">\n\t\t<TR>\n\t\t\t<TH>Rank</TH>\n\t\t\t<TH>Name</TH>\n\t\t\t<TH>Points</TH>\n\t\t\t<TH>Last Points</TH>\n\t\t\t<TH>Date</TH>\n\t\t</TR>\n\t\t";
/*     */ 
/*     */ 
/*     */   
/*     */   public ChallengeSummary(long wurmId, String playerName) {
/* 107 */     this.wid = wurmId;
/* 108 */     this.name = playerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addToScore(PlayerInfo pid, int scoreType, float added) {
/* 113 */     if (pid.getPower() > 0)
/*     */       return; 
/* 115 */     if (added != 0.0F) {
/*     */       
/* 117 */       boolean newScore = false;
/* 118 */       ChallengeSummary summary = getSummary(pid.wurmId);
/* 119 */       if (summary == null) {
/*     */         
/* 121 */         summary = new ChallengeSummary(pid.wurmId, pid.getName());
/* 122 */         addChallengeSummary(summary);
/*     */       } 
/* 124 */       ChallengeRound round = summary.getPrivateChallengeRound(ChallengePointEnum.ChallengeScenario.current.getNum());
/* 125 */       if (round == null) {
/*     */         
/* 127 */         round = new ChallengeRound(ChallengePointEnum.ChallengeScenario.current.getNum());
/* 128 */         summary.addPrivateChallengeRound(round);
/*     */       } 
/* 130 */       ChallengeScore scoreObj = round.getCurrentScoreForType(scoreType);
/* 131 */       if (scoreObj == null) {
/*     */         
/* 133 */         scoreObj = new ChallengeScore(scoreType, added, System.currentTimeMillis(), added);
/* 134 */         newScore = true;
/*     */       }
/*     */       else {
/*     */         
/* 138 */         scoreObj.setPoints(scoreObj.getPoints() + added);
/* 139 */         scoreObj.setLastPoints(added);
/*     */       } 
/* 141 */       round.setScore(scoreObj);
/* 142 */       ChallengePointEnum.ChallengePoint.fromInt(scoreType).setDirty(true);
/* 143 */       if (newScore) {
/*     */         
/* 145 */         createScore(pid.wurmId, ChallengePointEnum.ChallengeScenario.current.getNum(), scoreObj);
/*     */       } else {
/*     */         
/* 148 */         updateScore(pid.wurmId, ChallengePointEnum.ChallengeScenario.current.getNum(), scoreObj);
/* 149 */       }  if (checkIfTopScore(scoreObj, pid)) {
/*     */         
/*     */         try {
/*     */           
/* 153 */           Player player = Players.getInstance().getPlayer(pid.wurmId);
/* 154 */           player.getCommunicator().sendSafeServerMessage("New High Score: " + 
/* 155 */               ChallengePointEnum.ChallengePoint.fromInt(scoreType).getName() + " " + scoreObj.getPoints() + "!");
/*     */         
/*     */         }
/* 158 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 165 */       if (scoreType == ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype()) {
/* 166 */         summary.saveCurrentPersonalHtmlPage();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final boolean checkIfTopScore(ChallengeScore score, PlayerInfo pinf) {
/* 172 */     if (score.getType() != 0) {
/*     */       
/* 174 */       if (topScores[score.getType()] == null && score.getPoints() > 0.0F) {
/*     */         
/* 176 */         topScores[score.getType()] = score;
/* 177 */         topScorers[score.getType()] = pinf.getName();
/* 178 */         return true;
/*     */       } 
/* 180 */       if (score.getPoints() > 0.0F && score
/* 181 */         .getPoints() > topScores[score.getType()].getPoints()) {
/*     */         
/* 183 */         topScores[score.getType()] = score;
/* 184 */         topScorers[score.getType()] = pinf.getName();
/* 185 */         return true;
/*     */       } 
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addScoreFromLoad(PlayerInfo pid, int roundNumber, ChallengeScore score) {
/* 193 */     ChallengeSummary summary = getSummary(pid.wurmId);
/* 194 */     if (summary == null) {
/*     */       
/* 196 */       summary = new ChallengeSummary(pid.wurmId, pid.getName());
/* 197 */       addChallengeSummary(summary);
/*     */     } 
/* 199 */     ChallengeRound round = summary.getPrivateChallengeRound(roundNumber);
/* 200 */     if (round == null) {
/*     */       
/* 202 */       round = new ChallengeRound(roundNumber);
/* 203 */       summary.addPrivateChallengeRound(round);
/*     */     } 
/* 205 */     round.setScore(score);
/* 206 */     checkIfTopScore(score, pid);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addChallengeSummary(ChallengeSummary summary) {
/* 211 */     allScores.put(Long.valueOf(summary.getPlayerId()), summary);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final ChallengeSummary getSummary(long playerId) {
/* 216 */     return allScores.get(Long.valueOf(playerId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final ChallengeRound getRoundSummary(long playerId, int round) {
/* 221 */     ChallengeSummary summary = allScores.get(Long.valueOf(playerId));
/* 222 */     if (summary != null)
/*     */     {
/* 224 */       return summary.getPrivateChallengeRound(round);
/*     */     }
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ChallengeRound getPrivateChallengeRound(int round) {
/* 231 */     return this.privateRounds.get(Integer.valueOf(round));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addPrivateChallengeRound(ChallengeRound round) {
/* 236 */     this.privateRounds.put(Integer.valueOf(round.getRound()), round);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getPlayerId() {
/* 241 */     return this.wid;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getPlayerName() {
/* 246 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadLocalChallengeScores() {
/* 251 */     if (Servers.localServer.isChallengeServer()) {
/*     */       
/* 253 */       Connection dbcon = null;
/* 254 */       PreparedStatement ps = null;
/* 255 */       ResultSet rs = null;
/* 256 */       int loadedScores = 0;
/* 257 */       long lStart = System.nanoTime();
/*     */ 
/*     */       
/*     */       try {
/* 261 */         dbcon = DbConnector.getLoginDbCon();
/* 262 */         ps = dbcon.prepareStatement("SELECT * FROM CHALLENGE");
/* 263 */         rs = ps.executeQuery();
/*     */         
/* 265 */         while (rs.next())
/*     */         {
/* 267 */           long wurmid = rs.getLong("WURMID");
/* 268 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/* 269 */           int round = rs.getInt("ROUND");
/* 270 */           float points = rs.getFloat("POINTS");
/* 271 */           int scoreType = rs.getInt("TYPE");
/* 272 */           long lastUpdated = rs.getLong("LASTUPDATED");
/* 273 */           long lastAdded = rs.getLong("LASTPOINTS");
/* 274 */           if (pinf != null)
/*     */           {
/* 276 */             addScoreFromLoad(pinf, round, new ChallengeScore(scoreType, points, lastUpdated, (float)lastAdded));
/*     */           }
/* 278 */           loadedScores++;
/*     */         }
/*     */       
/* 281 */       } catch (SQLException sqx) {
/*     */         
/* 283 */         logger.log(Level.WARNING, "Failed to load scores, SqlState: " + sqx
/* 284 */             .getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 285 */         Exception lNext = sqx.getNextException();
/* 286 */         if (lNext != null)
/*     */         {
/* 288 */           logger.log(Level.WARNING, "Failed to load scores, Next Exception", lNext);
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/* 293 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 294 */         DbConnector.returnConnection(dbcon);
/*     */         
/* 296 */         long end = System.nanoTime();
/* 297 */         logger.info("Loaded " + loadedScores + " challenge scores from database took " + ((float)(end - lStart) / 1000000.0F) + " ms");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void createScore(long pid, int round, ChallengeScore score) {
/*     */     try {
/* 307 */       if (Servers.localServer.isChallengeServer()) {
/*     */         
/* 309 */         Connection dbcon = null;
/* 310 */         PreparedStatement ps = null;
/* 311 */         ResultSet rs = null;
/*     */         
/*     */         try {
/* 314 */           dbcon = DbConnector.getLoginDbCon();
/* 315 */           ps = dbcon.prepareStatement("INSERT INTO CHALLENGE(LASTUPDATED,WURMID,ROUND,TYPE,POINTS,LASTPOINTS) VALUES (?,?,?,?,?,?)");
/* 316 */           ps.setLong(1, score.getLastUpdated());
/* 317 */           ps.setLong(2, pid);
/* 318 */           ps.setInt(3, round);
/* 319 */           ps.setInt(4, score.getType());
/* 320 */           ps.setFloat(5, score.getPoints());
/* 321 */           ps.setFloat(6, score.getLastPoints());
/* 322 */           ps.execute();
/*     */         }
/* 324 */         catch (SQLException sqx) {
/*     */           
/* 326 */           logger.log(Level.WARNING, "Failed to save score " + pid + "," + round + "," + score
/*     */               
/* 328 */               .getPoints() + ", SqlState: " + sqx
/* 329 */               .getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 330 */           Exception lNext = sqx.getNextException();
/* 331 */           if (lNext != null)
/*     */           {
/* 333 */             logger.log(Level.WARNING, "Failed to save scores, Next Exception", lNext);
/*     */           }
/*     */         }
/*     */         finally {
/*     */           
/* 338 */           DbUtilities.closeDatabaseObjects(ps, rs);
/* 339 */           DbConnector.returnConnection(dbcon);
/*     */         }
/*     */       
/*     */       } 
/* 343 */     } catch (Exception ex) {
/*     */       
/* 345 */       logger.log(Level.WARNING, "Exception saving challenge score " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void updateScore(long pid, int round, ChallengeScore score) {
/*     */     try {
/* 353 */       if (Servers.localServer.isChallengeServer())
/*     */       {
/* 355 */         Connection dbcon = null;
/* 356 */         PreparedStatement ps = null;
/* 357 */         ResultSet rs = null;
/*     */         
/*     */         try {
/* 360 */           dbcon = DbConnector.getLoginDbCon();
/* 361 */           ps = dbcon.prepareStatement("UPDATE CHALLENGE SET LASTUPDATED=?,POINTS=?,LASTPOINTS=? WHERE WURMID=? AND ROUND=? AND TYPE=?");
/* 362 */           ps.setLong(1, score.getLastUpdated());
/* 363 */           ps.setFloat(2, score.getPoints());
/* 364 */           ps.setFloat(3, score.getLastPoints());
/* 365 */           ps.setLong(4, pid);
/* 366 */           ps.setInt(5, round);
/* 367 */           ps.setInt(6, score.getType());
/* 368 */           ps.executeUpdate();
/*     */         }
/* 370 */         catch (SQLException sqx) {
/*     */           
/* 372 */           logger.log(Level.WARNING, "Failed to save score " + pid + "," + round + "," + score
/*     */               
/* 374 */               .getPoints() + ", SqlState: " + sqx
/* 375 */               .getSQLState() + ", ErrorCode: " + sqx.getErrorCode(), sqx);
/* 376 */           Exception lNext = sqx.getNextException();
/* 377 */           if (lNext != null)
/*     */           {
/* 379 */             logger.log(Level.WARNING, "Failed to load scores, Next Exception", lNext);
/*     */           }
/*     */         }
/*     */         finally {
/*     */           
/* 384 */           DbUtilities.closeDatabaseObjects(ps, rs);
/* 385 */           DbConnector.returnConnection(dbcon);
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 390 */     } catch (Exception ex) {
/*     */       
/* 392 */       logger.log(Level.WARNING, "Exception " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final File createFile() {
/* 398 */     if (!this.fileExists) {
/*     */       
/* 400 */       String dir = "/var/www/challenge/" + this.name.substring(0, 1) + File.separator;
/*     */       
/* 402 */       File dirFile = new File(dir.toLowerCase());
/* 403 */       if (!dirFile.exists())
/* 404 */         dirFile.mkdirs(); 
/* 405 */       this.fileExists = true;
/* 406 */       this.filename = dir.toLowerCase() + this.name.toLowerCase() + ".html";
/*     */     } 
/* 408 */     return new File(this.filename);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveCurrentPersonalHtmlPage() {
/* 413 */     isDirty = true;
/* 414 */     (new Thread()
/*     */       {
/*     */ 
/*     */         
/*     */         public void run()
/*     */         {
/* 420 */           Writer output = null;
/*     */           
/*     */           try {
/* 423 */             File aFile = ChallengeSummary.this.createFile();
/*     */ 
/*     */             
/* 426 */             output = new BufferedWriter(new FileWriter(aFile));
/* 427 */             output.write("<!DOCTYPE html><HTML>\n\t<HEAD>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><TITLE>Wurm Online Challenge Standings</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t");
/* 428 */             output.write("<H1>Summary for " + ChallengeSummary.this.name + "</H1>\n\t<br>");
/*     */             
/* 430 */             for (ChallengePointEnum.ChallengeScenario scenario : ChallengePointEnum.ChallengeScenario.getScenarios()) {
/*     */               
/* 432 */               if (scenario.getNum() > 0) {
/*     */                 
/* 434 */                 ChallengeRound summary = ChallengeSummary.this.getPrivateChallengeRound(scenario.getNum());
/* 435 */                 if (summary != null) {
/*     */                   
/* 437 */                   output.write("<img src=\"" + summary.getRoundIcon() + "\" alt=\"round icon\"/><p><a href=\"../main" + summary
/* 438 */                       .getRound() + ".html\">" + summary.getRoundName() + "</a></p>\n\t");
/*     */ 
/*     */ 
/*     */                   
/*     */                   try {
/* 443 */                     output.write("<TABLE id=\"gameDataTable\">\n\t\t<TR class=\"gameDataTopTenTR\">\n\t\t\t<TH>Name</TH>\n\t\t\t<TH>Points</TH>\n\t\t\t<TH>Last points</TH>\n\t\t\t<TH>Date</TH>\n\t\t</TR>\n\t\t");
/*     */                   }
/* 445 */                   catch (IOException iox) {
/*     */                     
/* 447 */                     ChallengeSummary.logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */                   } 
/*     */                   
/* 450 */                   for (ChallengeScore score : summary.getScores()) {
/*     */                     
/* 452 */                     if (score.getType() != 0)
/*     */                     {
/* 454 */                       output.write("<TR class=\"gameDataTopTenTR\">\n\t\t\t<TD class=\"gameDataTopTenTDName\">" + 
/* 455 */                           ChallengePointEnum.ChallengePoint.fromInt(score.getType()).getName() + "</TD>\n\t\t\t<TD class=\"gameDataTopTenTDValue\">" + score
/*     */                           
/* 457 */                           .getPoints() + "</TD>\n\t\t\t<TD>" + score
/*     */                           
/* 459 */                           .getLastPoints() + "</TD>\n\t\t\t<TD>" + new Date(score
/*     */                             
/* 461 */                             .getLastUpdated()) + "</TD>\n\t\t</TR>\n\t\t");
/*     */                     }
/*     */                   } 
/*     */                   
/* 465 */                   output.write("</TABLE>\n\n");
/*     */                 } 
/*     */               } 
/*     */             } 
/* 469 */             output.write("</BODY>\n</HTML>");
/*     */           }
/* 471 */           catch (IOException iox) {
/*     */             
/* 473 */             ChallengeSummary.logger.log(Level.WARNING, "Failed to close html file for " + ChallengeSummary.this.name, iox);
/*     */           } finally {
/*     */ 
/*     */             
/*     */             try {
/*     */ 
/*     */               
/* 480 */               if (output != null) {
/* 481 */                 output.close();
/*     */               }
/* 483 */             } catch (IOException iOException) {}
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 488 */       }).start();
/*     */   }
/*     */ 
/*     */   
/*     */   private static final File createHeaderFile() {
/* 493 */     return new File(headerFilename);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final String getHighScoreUrl(int pointType) {
/* 498 */     return ChallengePointEnum.ChallengePoint.fromInt(pointType).getName().replace(" ", "").trim().toLowerCase() + ChallengePointEnum.ChallengeScenario.current
/* 499 */       .getNum() + ".html";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String getPlayerHomePageUrl(String playerName) {
/* 505 */     return playerName.substring(0, 1).toLowerCase() + "/" + playerName
/* 506 */       .toLowerCase() + ".html";
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void saveCurrentGlobalHtmlPage() {
/* 511 */     if (isDirty) {
/*     */       
/* 513 */       isDirty = false;
/* 514 */       if (!writing) {
/*     */         
/* 516 */         writing = true;
/* 517 */         (new Thread()
/*     */           {
/*     */             
/*     */             public void run()
/*     */             {
/* 522 */               Writer output = null;
/*     */               
/*     */               try {
/* 525 */                 File aFile = ChallengeSummary.createHeaderFile();
/*     */ 
/*     */                 
/* 528 */                 output = new BufferedWriter(new FileWriter(aFile));
/* 529 */                 output.write("<!DOCTYPE html><HTML>\n\t<HEAD>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><TITLE>Wurm Online Challenge Standings</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t");
/* 530 */                 output.write("<H1>Summary for " + ChallengePointEnum.ChallengeScenario.current.getName() + "</H1>\n\t<br>");
/* 531 */                 output.write("<img src=\"" + ChallengePointEnum.ChallengeScenario.current.getUrl() + "\" alt=\"round icon\"/><p>" + ChallengePointEnum.ChallengeScenario.current
/* 532 */                     .getDesc() + "</p>\n\t");
/*     */                 
/*     */                 try {
/* 535 */                   output.write("<TABLE id=\"gameDataTable\">\n\t\t<TR>\n\t\t\t<TH>Challenge</TH>\n\t\t\t<TH>Leader</TH>\n\t\t\t<TH>Points</TH>\n\t\t\t<TH>Last Points</TH>\n\t\t\t<TH>Date</TH>\n\t\t</TR>\n\t\t");
/*     */                 }
/* 537 */                 catch (IOException iox) {
/*     */                   
/* 539 */                   ChallengeSummary.logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */                 } 
/* 541 */                 for (int x = 0; x < ChallengeSummary.topScores.length; x++) {
/*     */                   
/* 543 */                   if (ChallengeSummary.topScores[x] != null) {
/*     */                     
/* 545 */                     String scorerUrl = ChallengeSummary.getPlayerHomePageUrl(ChallengeSummary.topScorers[x]);
/* 546 */                     output.write("<TR class=\"gameDataTopTenTR\">\n\t\t\t<TD class=\"gameDataTopTenTDName\"><a href=\"" + ChallengeSummary
/* 547 */                         .getHighScoreUrl(ChallengeSummary.topScores[x].getType()) + "\">" + 
/* 548 */                         ChallengePointEnum.ChallengePoint.fromInt(ChallengeSummary.topScores[x].getType()).getName() + "</a></TD>\n\t\t\t<TD class=\"gameDataTopTenTDName\"><a href=\"" + scorerUrl
/*     */                         
/* 550 */                         .toLowerCase() + "\">" + ChallengeSummary.topScorers[x] + "</a></TD>\n\t\t\t<TD class=\"gameDataTopTenTDValue\">" + ChallengeSummary
/*     */                         
/* 552 */                         .topScores[x].getPoints() + "</TD>\n\t\t\n\t\t<TR>\n\t\t\t<TH>" + ChallengeSummary
/*     */                         
/* 554 */                         .topScores[x].getLastPoints() + "</TH>\n\t\t\t<TH>" + new Date(ChallengeSummary
/*     */                           
/* 556 */                           .topScores[x].getLastUpdated()) + "</TH>\n\t\t</TR>\n\t\t");
/*     */                   } 
/*     */                 } 
/*     */                 
/* 560 */                 output.write("</TABLE>\n\n");
/* 561 */                 output.write("</BODY>\n</HTML>");
/* 562 */                 for (ChallengePointEnum.ChallengePoint point : ChallengePointEnum.ChallengePoint.getTypes()) {
/*     */                   
/* 564 */                   if (point.getEnumtype() > 0)
/*     */                   {
/* 566 */                     if (point.isDirty()) {
/* 567 */                       ChallengeSummary.createHighScorePage(point.getEnumtype());
/*     */                     }
/*     */                   }
/*     */                 } 
/* 571 */               } catch (IOException iox) {
/*     */                 
/* 573 */                 ChallengeSummary.logger.log(Level.WARNING, "Failed to close html file for main page", iox);
/*     */               } finally {
/*     */ 
/*     */                 
/*     */                 try {
/*     */ 
/*     */                   
/* 580 */                   if (output != null) {
/* 581 */                     output.close();
/*     */                   }
/* 583 */                 } catch (IOException iOException) {}
/*     */               } 
/*     */ 
/*     */               
/* 587 */               ChallengeSummary.writing = false;
/*     */             }
/* 589 */           }).start();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void createHighScorePage(int scoreType) {
/* 598 */     String fileName = "/var/www/challenge/" + getHighScoreUrl(scoreType);
/* 599 */     File aFile = new File(fileName);
/* 600 */     Writer output = null;
/*     */     
/*     */     try {
/* 603 */       ChallengePointEnum.ChallengePoint point = ChallengePointEnum.ChallengePoint.fromInt(scoreType);
/*     */ 
/*     */       
/* 606 */       output = new BufferedWriter(new FileWriter(aFile));
/* 607 */       output.write("<!DOCTYPE html><HTML>\n\t<HEAD>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><TITLE>Wurm Online Challenge Standings</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t");
/* 608 */       output.write("<H1>Summary for " + point.getName() + "</H1>\n\t<br>");
/* 609 */       output.write("<img src=\"" + ChallengePointEnum.ChallengeScenario.current.getUrl() + "\" alt=\"round icon\"/><p><a href=\"main" + ChallengePointEnum.ChallengeScenario.current
/* 610 */           .getNum() + ".html\">" + ChallengePointEnum.ChallengeScenario.current
/* 611 */           .getName() + "</a></p>\n\t");
/*     */ 
/*     */       
/*     */       try {
/* 615 */         output.write("<TABLE id=\"gameDataTable\">\n\t\t<TR>\n\t\t\t<TH>Rank</TH>\n\t\t\t<TH>Name</TH>\n\t\t\t<TH>Points</TH>\n\t\t\t<TH>Last Points</TH>\n\t\t\t<TH>Date</TH>\n\t\t</TR>\n\t\t");
/*     */       }
/* 617 */       catch (IOException iox) {
/*     */         
/* 619 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */       } 
/* 621 */       ConcurrentSkipListSet<ScoreNamePair> scores = new ConcurrentSkipListSet<>();
/* 622 */       for (ChallengeSummary summary : allScores.values()) {
/*     */         
/* 624 */         ChallengeRound round = summary.getPrivateChallengeRound(ChallengePointEnum.ChallengeScenario.current.getNum());
/* 625 */         if (round != null) {
/*     */           
/* 627 */           ChallengeScore[] scoreArr = round.getScores();
/* 628 */           for (ChallengeScore score : scoreArr) {
/*     */             
/* 630 */             if (score.getType() == scoreType && score.getPoints() > 0.0F) {
/*     */               
/* 632 */               scores.add(new ScoreNamePair(summary.getPlayerName(), score));
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 638 */       ScoreNamePair[] topScoreArr = (ScoreNamePair[])scores.toArray((Object[])new ScoreNamePair[scores.size()]);
/* 639 */       Arrays.sort((Object[])topScoreArr);
/* 640 */       for (int x = 0; x < topScoreArr.length; x++) {
/*     */         
/* 642 */         if (topScoreArr[x] != null) {
/*     */           
/* 644 */           String scorerUrl = getPlayerHomePageUrl((topScoreArr[x]).name);
/* 645 */           output.write("<TR class=\"gameDataTopTenTR\">\n\t\t\t<TD class=\"gameDataTopTenTDValue\">" + (x + 1) + "</TD>\n\t\t\t<TD class=\"gameDataTopTenTDName\"><a href=\"" + scorerUrl + "\">" + (topScoreArr[x]).name + "</a></TD>\n\t\t\t<TD class=\"gameDataTopTenTDValue\">" + (topScoreArr[x]).score
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 650 */               .getPoints() + "</TD>\n\t\t\n\t\t<TR>\n\t\t\t<TH>" + (topScoreArr[x]).score
/*     */               
/* 652 */               .getLastPoints() + "</TH>\n\t\t\t<TH>" + new Date((topScoreArr[x]).score
/*     */                 
/* 654 */                 .getLastUpdated()) + "</TH>\n\t\t</TR>\n\t\t");
/*     */         } 
/*     */       } 
/*     */       
/* 658 */       output.write("</TABLE>\n\n");
/* 659 */       output.write("</BODY>\n</HTML>");
/*     */     }
/* 661 */     catch (IOException iox) {
/*     */       
/* 663 */       logger.log(Level.WARNING, "Failed to close html file for main page", iox);
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 670 */         if (output != null) {
/* 671 */           output.close();
/*     */         }
/* 673 */       } catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 677 */     ChallengePointEnum.ChallengePoint.fromInt(scoreType).setDirty(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\statistics\ChallengeSummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
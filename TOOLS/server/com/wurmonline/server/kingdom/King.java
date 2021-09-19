/*      */ package com.wurmonline.server.kingdom;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class King
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*      */   private static final String CREATE_KING_ERA = "insert into KING_ERA ( ERA,KINGDOM,KINGDOMNAME, KINGID,KINGSNAME,GENDER,STARTTIME,STARTWURMTIME,STARTLANDPERCENT, CURRENTLANDPERCENT,      NEXTCHALLENGE,CURRENT) VALUES (?,?,?,?,?,?,?,?,?,?,  ?,1)";
/*      */   private static final String UPDATE_KING_ERA = "UPDATE KING_ERA SET KINGSNAME=?,GENDER=?,ENDTIME=?,ENDWURMTIME=?, CURRENTLANDPERCENT=?, CAPITAL=?, CURRENT=?,KINGDOM=? WHERE ERA=?";
/*      */   private static final String UPDATE_LEVELSKILLED = "UPDATE KING_ERA SET LEVELSKILLED=? WHERE ERA=?";
/*      */   private static final String UPDATE_LEVELSLOST = "UPDATE KING_ERA SET LEVELSLOST=? WHERE ERA=?";
/*      */   private static final String UPDATE_APPOINTMENTS = "UPDATE KING_ERA SET APPOINTMENTS=? WHERE ERA=?";
/*      */   private static final String GET_ALL_KING_ERA = "select * FROM KING_ERA";
/*      */   private static final String UPDATE_CHALLENGES = "UPDATE KING_ERA SET NEXTCHALLENGE=?,DECLINEDCHALLENGES=?,ACCEPTDATE=?,CHALLENGEDATE=? WHERE ERA=?";
/*   72 */   public String kingdomName = "unknown kingdom";
/*      */   
/*   74 */   private static Logger logger = Logger.getLogger(King.class.getName());
/*      */   
/*   76 */   public static int currentEra = 0;
/*      */   
/*   78 */   public int era = 0;
/*      */   
/*   80 */   public String kingName = "";
/*      */   
/*   82 */   public long kingid = -10L;
/*      */   
/*   84 */   private long startTime = 0L;
/*      */   
/*   86 */   private long endTime = 0L;
/*      */   
/*   88 */   public long startWurmTime = 0L;
/*      */   
/*   90 */   public long endWurmTime = 0L;
/*      */   
/*   92 */   public float startLand = 0.0F;
/*      */   
/*   94 */   public float currentLand = 0.0F;
/*      */   
/*   96 */   public int appointed = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  102 */   public int levelskilled = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   public int levelslost = 0;
/*      */   
/*      */   public boolean current = false;
/*      */   
/*  112 */   public byte kingdom = 0;
/*      */   
/*  114 */   private long nextChallenge = 0L;
/*      */   
/*  116 */   private int declinedChallenges = 0;
/*      */   
/*  118 */   private long challengeDate = 0L;
/*      */   
/*  120 */   private long acceptDate = 0L;
/*      */   
/*  122 */   public byte gender = 0;
/*      */   
/*  124 */   public String capital = "";
/*      */   
/*  126 */   private String rulerMaleTitle = "Grand Prince";
/*      */   
/*  128 */   private String rulerFemaleTitle = "Grand Princess";
/*      */   
/*  130 */   private static King kingJenn = null;
/*      */   
/*  132 */   private static King kingMolRehan = null;
/*      */   
/*  134 */   private static King kingHots = null;
/*      */   
/*  136 */   private Appointments appointments = null;
/*      */   
/*  138 */   public static final Map<Integer, King> eras = new HashMap<>();
/*      */   
/*  140 */   public static final Map<Long, Integer> challenges = new HashMap<>();
/*      */   
/*  142 */   private static final int challengesRequired = Servers.isThisATestServer() ? 3 : 10;
/*      */   
/*  144 */   private static final int votesRequired = Servers.isThisATestServer() ? 1 : 10;
/*      */   
/*  146 */   private static final Set<King> kings = new HashSet<>();
/*      */   
/*  148 */   private static final long challengeFactor = Servers.isThisATestServer() ? 60000L : 604800000L;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float landPercentRequiredForBonus = 2.0F;
/*      */ 
/*      */ 
/*      */   
/*      */   long lastCapital;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addKing(King king) {
/*  162 */     eras.put(Integer.valueOf(king.era), king);
/*  163 */     logger.log(Level.INFO, "Loading kings, adding " + king.kingName);
/*  164 */     if (king.current) {
/*      */       
/*  166 */       if (king.kingdom == 1) {
/*      */         
/*  168 */         logger.log(Level.INFO, "Setting current jenn king: " + king.kingName);
/*  169 */         kingJenn = king;
/*      */       }
/*  171 */       else if (king.kingdom == 2) {
/*      */         
/*  173 */         logger.log(Level.INFO, "Setting current mol rehan king: " + king.kingName);
/*  174 */         kingMolRehan = king;
/*      */       }
/*  176 */       else if (king.kingdom == 3) {
/*      */         
/*  178 */         logger.log(Level.INFO, "Setting current hots king: " + king.kingName);
/*  179 */         kingHots = king;
/*      */       } 
/*  181 */       kings.add(king);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static King getKing(byte _kingdom) {
/*  187 */     if (_kingdom == 1)
/*      */     {
/*  189 */       return kingJenn;
/*      */     }
/*  191 */     if (_kingdom == 2)
/*      */     {
/*  193 */       return kingMolRehan;
/*      */     }
/*  195 */     if (_kingdom == 3)
/*      */     {
/*  197 */       return kingHots;
/*      */     }
/*  199 */     for (King k : kings) {
/*      */       
/*  201 */       if (k.kingdom == _kingdom && k.current)
/*  202 */         return k; 
/*      */     } 
/*  204 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isKing(long wurmid, byte kingdom) {
/*  209 */     King k = getKing(kingdom);
/*  210 */     if (k != null)
/*  211 */       return (k.kingid == wurmid); 
/*  212 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void purgeKing(byte _kingdom) {
/*  217 */     Zones.calculateZones(true);
/*  218 */     if (_kingdom == 1) {
/*      */       
/*  220 */       if (kingJenn != null) {
/*      */         
/*  222 */         kingJenn.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  223 */         switchCurrent(kingJenn);
/*      */       } 
/*  225 */       kingJenn = null;
/*      */ 
/*      */       
/*  228 */       new Appointments(-1, (byte)1, true);
/*      */     }
/*  230 */     else if (_kingdom == 2) {
/*      */       
/*  232 */       if (kingMolRehan != null) {
/*      */         
/*  234 */         kingMolRehan.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  235 */         switchCurrent(kingMolRehan);
/*      */       } 
/*  237 */       kingMolRehan = null;
/*      */ 
/*      */       
/*  240 */       new Appointments(-2, (byte)2, true);
/*      */     }
/*  242 */     else if (_kingdom == 3) {
/*      */       
/*  244 */       if (kingHots != null) {
/*      */         
/*  246 */         kingHots.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  247 */         switchCurrent(kingHots);
/*      */       } 
/*  249 */       kingHots = null;
/*      */ 
/*      */       
/*  252 */       new Appointments(-3, (byte)3, true);
/*      */     }
/*      */     else {
/*      */       
/*  256 */       King[] kingarr = getKings();
/*  257 */       for (King k : kingarr) {
/*      */         
/*  259 */         if (k.kingdom == _kingdom) {
/*      */           
/*  261 */           k.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  262 */           switchCurrent(k);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pollKings() {
/*  270 */     King[] kingarr = getKings();
/*  271 */     for (King k : kingarr)
/*      */     {
/*  273 */       k.poll();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final King[] getKings() {
/*  279 */     return kings.<King>toArray(new King[kings.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   private void poll() {
/*  284 */     if (System.currentTimeMillis() - this.appointments.lastChecked > 604800000L) {
/*      */       
/*  286 */       this.appointments.resetAppointments(this.kingdom);
/*      */ 
/*      */       
/*  289 */       Kingdom k = Kingdoms.getKingdom(this.kingdom);
/*  290 */       if (k.isCustomKingdom()) {
/*      */         
/*  292 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(this.kingid);
/*  293 */         if (pinf != null)
/*      */         {
/*  295 */           if (System.currentTimeMillis() - pinf.lastLogout > 2419200000L && 
/*  296 */             System.currentTimeMillis() - pinf.lastLogin > 2419200000L)
/*      */           {
/*      */             
/*  299 */             Items.deleteRoyalItemForKingdom(this.kingdom, true, false);
/*  300 */             logger.log(Level.INFO, this.kingName + " has not logged in for a month. A new king for " + this.kingdomName + " will be found.");
/*      */             
/*  302 */             purgeKing(this.kingdom);
/*      */           }
/*      */         
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/*  309 */       PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(this.kingid);
/*  310 */       if (pinf != null)
/*      */       {
/*  312 */         if (pinf.currentServer == Servers.localServer.id)
/*      */         {
/*  314 */           if (!pinf.isPaying()) {
/*      */             
/*  316 */             Kingdom k = Kingdoms.getKingdom(this.kingdom);
/*  317 */             if (!k.isCustomKingdom()) {
/*      */               
/*  319 */               Items.deleteRoyalItemForKingdom(this.kingdom, true, true);
/*  320 */               logger.log(Level.INFO, this.kingName + " is no longer premium. Deleted the regalia.");
/*  321 */               purgeKing(this.kingdom);
/*      */               return;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/*  327 */       Zones.calculateZones(false);
/*  328 */       float oldland = this.currentLand;
/*  329 */       this.currentLand = Zones.getPercentLandForKingdom(this.kingdom);
/*  330 */       if (oldland != this.currentLand) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  338 */         logger.log(Level.INFO, "Saving " + this.kingName + " because new land is " + this.currentLand + " compared to " + oldland);
/*  339 */         save();
/*      */       } 
/*  341 */       if (hasFailedToRespondToChallenge()) {
/*      */ 
/*      */         
/*  344 */         HistoryManager.addHistory(this.kingName, "decided not to respond to a challenge.");
/*  345 */         Server.getInstance().broadCastAlert(this.kingName + " has decided not to respond to a challenge.");
/*      */         
/*  347 */         logger.log(Level.INFO, this.kingName + " did not respond to a challenge.");
/*  348 */         setChallengeDeclined();
/*  349 */         if (hasFailedAllChallenges()) {
/*      */           
/*  351 */           HistoryManager.addHistory(this.kingName, "may now be voted away from the throne within one week at the duelling stone.");
/*      */           
/*  353 */           Server.getInstance().broadCastAlert(
/*  354 */               getFullTitle() + " may now be voted away from the throne within one week at the duelling stone.");
/*      */           
/*  356 */           logger.log(Level.INFO, this.kingName + " may now be voted away.");
/*      */         } 
/*      */       } 
/*  359 */       if (hasFailedAllChallenges())
/*      */       {
/*  361 */         if (getVotesNeeded() == 0) {
/*      */           
/*  363 */           removeByVote();
/*      */         }
/*  365 */         else if (getNextChallenge() < System.currentTimeMillis()) {
/*      */           
/*  367 */           PlayerInfoFactory.resetVotesForKingdom(this.kingdom);
/*  368 */           this.declinedChallenges = 0;
/*  369 */           updateChallenges();
/*  370 */           HistoryManager.addHistory(this.kingName, "was not voted away from the throne this time. The " + 
/*  371 */               getRulerTitle() + " remains on the throne of " + this.kingdomName + ".");
/*      */           
/*  373 */           Server.getInstance().broadCastNormal(this.kingName + " was not voted away from the throne this time. The " + 
/*  374 */               getRulerTitle() + " remains on the throne of " + this.kingdomName + ".");
/*      */           
/*  376 */           logger.log(Level.INFO, this.kingName + " may no longer be voted away.");
/*      */         } 
/*      */       }
/*  379 */       if (this.acceptDate > 0L)
/*      */       {
/*  381 */         if (System.currentTimeMillis() > this.acceptDate) {
/*      */           
/*      */           try {
/*      */             
/*  385 */             Player p = Players.getInstance().getPlayer(this.kingid);
/*  386 */             if (p.isInOwnDuelRing()) {
/*      */               
/*  388 */               if (Servers.isThisATestServer()) {
/*      */                 
/*  390 */                 if (System.currentTimeMillis() - getChallengeAcceptedDate() > 300000L)
/*      */                 {
/*  392 */                   passedChallenge();
/*      */                 }
/*      */               }
/*  395 */               else if (System.currentTimeMillis() - this.acceptDate > 1800000L) {
/*      */                 
/*  397 */                 passedChallenge();
/*      */               } 
/*  399 */               p.getCommunicator().sendAlertServerMessage("Unseen eyes watch you.");
/*      */             } else {
/*      */               
/*  402 */               setFailedChallenge();
/*      */             } 
/*  404 */           } catch (NoSuchPlayerException nsp) {
/*      */             
/*  406 */             setFailedChallenge();
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeByVote() {
/*  415 */     HistoryManager.addHistory(this.kingName, "has been voted away from the throne by the people of " + this.kingdomName + "!");
/*  416 */     Server.getInstance().broadCastAlert(
/*  417 */         getFullTitle() + " has been voted away from the throne by the people of " + this.kingdomName + "!");
/*  418 */     Items.deleteRoyalItemForKingdom(this.kingdom, true, true);
/*  419 */     purgeKing(this.kingdom);
/*  420 */     logger.log(Level.INFO, this.kingName + " has been voted away.");
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeByFailChallenge() {
/*  425 */     HistoryManager.addHistory(this.kingName, "has failed the challenge by the people of " + this.kingdomName + "!");
/*  426 */     Server.getInstance()
/*  427 */       .broadCastNormal(getFullTitle() + " has failed the challenge by the people of " + this.kingdomName + "!");
/*  428 */     Items.deleteRoyalItemForKingdom(this.kingdom, true, true);
/*  429 */     purgeKing(this.kingdom);
/*  430 */     logger.log(Level.INFO, this.kingName + " has failed the challenge.");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setRulerName(King king) {
/*  435 */     king.rulerMaleTitle = getRulerTitle(true, king.kingdom);
/*  436 */     king.rulerFemaleTitle = getRulerTitle(false, king.kingdom);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getRulerTitle() {
/*  441 */     if (this.gender == 1)
/*  442 */       return this.rulerFemaleTitle; 
/*  443 */     return this.rulerMaleTitle;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getRulerTitle(boolean male, byte kingdom) {
/*  448 */     if (kingdom == 1) {
/*      */       
/*  450 */       if (male)
/*  451 */         return "Grand Prince"; 
/*  452 */       return "Grand Princess";
/*      */     } 
/*  454 */     if (kingdom == 2) {
/*      */       
/*  456 */       if (male)
/*  457 */         return "Chancellor"; 
/*  458 */       return "Chancellor";
/*      */     } 
/*  460 */     if (kingdom == 3) {
/*      */       
/*  462 */       if (male)
/*  463 */         return "Emperor"; 
/*  464 */       return "Empress";
/*      */     } 
/*      */ 
/*      */     
/*  468 */     if (male)
/*  469 */       return "Chief"; 
/*  470 */     return "Chieftain";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadAllEra() {
/*  476 */     logger.log(Level.INFO, "Loading all kingdom eras.");
/*  477 */     long start = System.nanoTime();
/*  478 */     Connection dbcon = null;
/*  479 */     PreparedStatement ps = null;
/*  480 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  483 */       dbcon = DbConnector.getZonesDbCon();
/*  484 */       ps = dbcon.prepareStatement("select * FROM KING_ERA");
/*  485 */       rs = ps.executeQuery();
/*  486 */       while (rs.next())
/*      */       {
/*  488 */         King k = new King();
/*  489 */         k.era = rs.getInt("ERA");
/*  490 */         k.kingdom = rs.getByte("KINGDOM");
/*  491 */         k.current = rs.getBoolean("CURRENT");
/*  492 */         if (k.era > currentEra)
/*  493 */           currentEra = k.era; 
/*  494 */         k.kingName = rs.getString("KINGSNAME");
/*  495 */         k.gender = rs.getByte("GENDER");
/*  496 */         k.startLand = rs.getFloat("STARTLANDPERCENT");
/*  497 */         k.startTime = rs.getLong("STARTTIME");
/*  498 */         k.endTime = rs.getLong("ENDTIME");
/*  499 */         k.startWurmTime = rs.getLong("STARTWURMTIME");
/*  500 */         k.endWurmTime = rs.getLong("ENDWURMTIME");
/*  501 */         k.currentLand = rs.getFloat("CURRENTLANDPERCENT");
/*  502 */         k.appointed = rs.getInt("APPOINTMENTS");
/*  503 */         k.levelskilled = rs.getInt("LEVELSKILLED");
/*  504 */         k.levelslost = rs.getInt("LEVELSLOST");
/*  505 */         k.capital = rs.getString("CAPITAL");
/*  506 */         k.kingid = rs.getLong("KINGID");
/*  507 */         k.appointed = rs.getInt("APPOINTMENTS");
/*  508 */         k.nextChallenge = rs.getLong("NEXTCHALLENGE");
/*  509 */         k.declinedChallenges = rs.getInt("DECLINEDCHALLENGES");
/*  510 */         k.acceptDate = rs.getLong("ACCEPTDATE");
/*  511 */         k.challengeDate = rs.getLong("CHALLENGEDATE");
/*  512 */         k.kingdomName = rs.getString("KINGDOMNAME");
/*  513 */         byte template = k.kingdom;
/*      */         
/*  515 */         Kingdom kingd = Kingdoms.getKingdom(k.kingdom);
/*  516 */         if (kingd != null) {
/*      */           
/*  518 */           template = kingd.getTemplate();
/*  519 */           logger.log(Level.INFO, "Template for " + k.kingdom + "=" + template + " (" + kingd.getId() + ")");
/*      */         } 
/*  521 */         k.appointments = new Appointments(k.era, template, k.current);
/*  522 */         setRulerName(k);
/*  523 */         addKing(k);
/*      */       }
/*      */     
/*  526 */     } catch (SQLException sqex) {
/*      */       
/*  528 */       logger.log(Level.WARNING, "Failed to load kingdom eras: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  532 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  533 */       DbConnector.returnConnection(dbcon);
/*      */       
/*  535 */       long end = System.nanoTime();
/*  536 */       logger.info("Loaded kingdom eras from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  541 */     if (Appointments.jenn == null)
/*  542 */       new Appointments(-1, (byte)1, true); 
/*  543 */     if (Appointments.hots == null)
/*  544 */       new Appointments(-3, (byte)3, true); 
/*  545 */     if (Appointments.molr == null) {
/*  546 */       new Appointments(-2, (byte)2, true);
/*      */     }
/*  548 */     if (Appointments.none == null) {
/*  549 */       new Appointments(-5, (byte)0, true);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void setToNoKingdom(byte oldKingdom) {
/*  554 */     for (King k : eras.values()) {
/*  555 */       if (k.kingdom == oldKingdom) {
/*      */         
/*  557 */         k.kingdom = 0;
/*  558 */         k.save();
/*      */       } 
/*  560 */     }  for (King k : kings) {
/*  561 */       if (k.kingdom == oldKingdom) {
/*      */         
/*  563 */         k.kingdom = 0;
/*  564 */         k.save();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Appointments getCurrentAppointments(byte kingdom) {
/*  570 */     King k = getKing(kingdom);
/*  571 */     if (k != null && k.current)
/*      */     {
/*  573 */       return Appointments.getAppointments(k.era);
/*      */     }
/*      */ 
/*      */     
/*  577 */     Kingdom kingd = Kingdoms.getKingdom(kingdom);
/*  578 */     if (kingd != null)
/*      */     {
/*      */       
/*  581 */       return Appointments.getCurrentAppointments(kingd.getTemplate());
/*      */     }
/*      */     
/*  584 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void abdicate(boolean isOnSurface, boolean destroyItems) {
/*  589 */     Items.deleteRoyalItemForKingdom(this.kingdom, isOnSurface, destroyItems);
/*  590 */     purgeKing(this.kingdom);
/*      */   }
/*      */ 
/*      */   
/*      */   public static King createKing(byte _kingdom, String kingname, long kingwurmid, byte kinggender) {
/*  595 */     King k = new King();
/*      */     
/*  597 */     k.era = ++currentEra;
/*  598 */     k.kingdom = _kingdom;
/*  599 */     k.kingid = kingwurmid;
/*  600 */     k.kingName = kingname;
/*  601 */     k.gender = kinggender;
/*  602 */     k.startTime = System.currentTimeMillis();
/*  603 */     k.startWurmTime = WurmCalendar.currentTime;
/*  604 */     k.nextChallenge = System.currentTimeMillis() + challengeFactor;
/*  605 */     k.kingdomName = Kingdoms.getNameFor(_kingdom);
/*  606 */     Zones.calculateZones(true);
/*  607 */     k.startLand = Zones.getPercentLandForKingdom(_kingdom);
/*  608 */     boolean foundCapital = false;
/*      */     
/*      */     try {
/*  611 */       Player p = Players.getInstance().getPlayer(kingwurmid);
/*  612 */       p.achievement(321);
/*  613 */       if (p.getCitizenVillage() != null)
/*      */       {
/*  615 */         foundCapital = true;
/*  616 */         k.setCapital(p.getCitizenVillage().getName(), true);
/*      */       }
/*      */     
/*  619 */     } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */     
/*  623 */     if (_kingdom == 1) {
/*      */       
/*  625 */       if (kingJenn != null) {
/*      */         
/*  627 */         kingJenn.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  628 */         switchCurrent(kingJenn);
/*      */       } 
/*  630 */       kingJenn = k;
/*      */     }
/*  632 */     else if (_kingdom == 2) {
/*      */       
/*  634 */       if (kingMolRehan != null) {
/*      */         
/*  636 */         kingMolRehan.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  637 */         switchCurrent(kingMolRehan);
/*      */       } 
/*  639 */       kingMolRehan = k;
/*      */     }
/*  641 */     else if (_kingdom == 3) {
/*      */       
/*  643 */       if (kingHots != null) {
/*      */         
/*  645 */         kingHots.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  646 */         switchCurrent(kingHots);
/*      */       } 
/*  648 */       kingHots = k;
/*      */     }
/*      */     else {
/*      */       
/*  652 */       King oldKing = getKing(_kingdom);
/*  653 */       if (oldKing != null) {
/*      */         
/*  655 */         oldKing.currentLand = Zones.getPercentLandForKingdom(_kingdom);
/*  656 */         logger.log(Level.INFO, "Found old king " + oldKing.kingName + " when creating new.");
/*  657 */         switchCurrent(oldKing);
/*  658 */         if (!foundCapital)
/*  659 */           k.setCapital(oldKing.capital, true); 
/*      */       } 
/*      */     } 
/*  662 */     k.currentLand = k.startLand;
/*  663 */     k.current = true;
/*  664 */     k.create();
/*      */     
/*  666 */     byte template = k.kingdom;
/*  667 */     Kingdom kingd = Kingdoms.getKingdomOrNull(k.kingdom);
/*  668 */     if (kingd != null) {
/*      */       
/*  670 */       template = kingd.getTemplate();
/*  671 */       logger.log(Level.INFO, "Using " + Kingdoms.getNameFor(template) + " for " + kingd.getName());
/*      */     } 
/*  673 */     k.appointments = new Appointments(k.era, template, k.current);
/*  674 */     setRulerName(k);
/*  675 */     addKing(k);
/*  676 */     HistoryManager.addHistory(k.kingName, "is appointed new " + k.getRulerTitle() + " of " + k.kingdomName);
/*  677 */     Items.transferRegaliaForKingdom(_kingdom, kingwurmid);
/*  678 */     pollKings();
/*  679 */     return k;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void switchCurrent(King oldking) {
/*  684 */     oldking.endTime = System.currentTimeMillis();
/*  685 */     oldking.endWurmTime = WurmCalendar.currentTime;
/*  686 */     oldking.current = false;
/*  687 */     HistoryManager.addHistory(oldking.kingName, "no longer is the " + oldking.getRulerTitle() + " of " + oldking.kingdomName);
/*      */     
/*  689 */     Server.getInstance().broadCastNormal(oldking.kingName + " no longer is the " + oldking
/*  690 */         .getRulerTitle() + " of " + oldking.kingdomName);
/*  691 */     oldking.save();
/*      */     
/*  693 */     kings.remove(oldking);
/*      */     
/*  695 */     PlayerInfoFactory.resetVotesForKingdom(oldking.kingdom);
/*      */   }
/*      */ 
/*      */   
/*      */   private void create() {
/*  700 */     Connection dbcon = null;
/*  701 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  704 */       dbcon = DbConnector.getZonesDbCon();
/*  705 */       ps = dbcon.prepareStatement("insert into KING_ERA ( ERA,KINGDOM,KINGDOMNAME, KINGID,KINGSNAME,GENDER,STARTTIME,STARTWURMTIME,STARTLANDPERCENT, CURRENTLANDPERCENT,      NEXTCHALLENGE,CURRENT) VALUES (?,?,?,?,?,?,?,?,?,?,  ?,1)");
/*  706 */       ps.setInt(1, this.era);
/*  707 */       ps.setByte(2, this.kingdom);
/*  708 */       ps.setString(3, this.kingdomName);
/*  709 */       ps.setLong(4, this.kingid);
/*  710 */       ps.setString(5, this.kingName);
/*  711 */       ps.setByte(6, this.gender);
/*  712 */       ps.setLong(7, this.startTime);
/*  713 */       ps.setLong(8, this.startWurmTime);
/*  714 */       ps.setFloat(9, this.startLand);
/*  715 */       ps.setFloat(10, this.currentLand);
/*  716 */       ps.setLong(11, this.nextChallenge);
/*  717 */       ps.executeUpdate();
/*      */     }
/*  719 */     catch (SQLException sqex) {
/*      */       
/*  721 */       logger.log(Level.WARNING, "Failed to create kingdom for era " + this.era + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  725 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  726 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void save() {
/*  735 */     Connection dbcon = null;
/*  736 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  739 */       dbcon = DbConnector.getZonesDbCon();
/*  740 */       ps = dbcon.prepareStatement("UPDATE KING_ERA SET KINGSNAME=?,GENDER=?,ENDTIME=?,ENDWURMTIME=?, CURRENTLANDPERCENT=?, CAPITAL=?, CURRENT=?,KINGDOM=? WHERE ERA=?");
/*  741 */       ps.setString(1, this.kingName);
/*  742 */       ps.setByte(2, this.gender);
/*  743 */       ps.setLong(3, this.endTime);
/*  744 */       ps.setLong(4, this.endWurmTime);
/*  745 */       ps.setFloat(5, this.currentLand);
/*  746 */       ps.setString(6, this.capital);
/*  747 */       ps.setBoolean(7, this.current);
/*  748 */       ps.setByte(8, this.kingdom);
/*  749 */       ps.setInt(9, this.era);
/*  750 */       ps.executeUpdate();
/*      */     }
/*  752 */     catch (SQLException sqex) {
/*      */       
/*  754 */       logger.log(Level.WARNING, "Failed to save kingdom for era " + this.era + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  758 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  759 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */   private King() {
/*  763 */     this.lastCapital = System.currentTimeMillis();
/*      */     if (logger.isLoggable(Level.FINER))
/*      */       logger.finer("Creating new King"); 
/*      */   } public final boolean setCapital(String newcapital, boolean forced) {
/*  767 */     if (System.currentTimeMillis() - this.lastCapital > 21600000L || forced || Servers.isThisATestServer()) {
/*      */       
/*  769 */       this.capital = newcapital;
/*  770 */       this.lastCapital = System.currentTimeMillis();
/*  771 */       save();
/*  772 */       return true;
/*      */     } 
/*  774 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setGender(byte newgender) {
/*  784 */     this.gender = newgender;
/*  785 */     save();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addAppointment(Appointment app) {
/*  790 */     this.appointed += app.getLevel();
/*  791 */     Connection dbcon = null;
/*  792 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  795 */       dbcon = DbConnector.getZonesDbCon();
/*  796 */       ps = dbcon.prepareStatement("UPDATE KING_ERA SET APPOINTMENTS=? WHERE ERA=?");
/*  797 */       ps.setInt(1, this.appointed);
/*  798 */       ps.setInt(2, this.era);
/*  799 */       ps.executeUpdate();
/*      */     }
/*  801 */     catch (SQLException sqex) {
/*      */       
/*  803 */       logger.log(Level.WARNING, "Failed to update appointed: " + this.appointed + " for era " + this.era + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  807 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  808 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void resetNextChallenge(long nextTime) {
/*  818 */     this.nextChallenge = nextTime;
/*  819 */     challenges.clear();
/*  820 */     updateChallenges();
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getNextChallenge() {
/*  825 */     return this.nextChallenge;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setChallengeDate() {
/*  830 */     this.challengeDate = System.currentTimeMillis();
/*  831 */     updateChallenges();
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getChallengeDate() {
/*  836 */     return this.challengeDate;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setChallengeAccepted(long date) {
/*  841 */     this.acceptDate = date;
/*  842 */     this.challengeDate = 0L;
/*  843 */     resetNextChallenge(this.acceptDate + challengeFactor * (3 - this.declinedChallenges));
/*  844 */     updateChallenges();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setChallengeDeclined() {
/*  849 */     resetNextChallenge(System.currentTimeMillis() + challengeFactor);
/*  850 */     this.challengeDate = 0L;
/*  851 */     this.declinedChallenges++;
/*  852 */     updateChallenges();
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getChallengeAcceptedDate() {
/*  857 */     return this.acceptDate;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getDeclinedChallengesNumber() {
/*  862 */     return this.declinedChallenges;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void passedChallenge() {
/*  867 */     HistoryManager.addHistory(this.kingName, "passed the challenge put forth by the people of " + this.kingdomName + "!");
/*  868 */     Server.getInstance().broadCastNormal(
/*  869 */         getFullTitle() + " passed the challenge put forth by the people of " + this.kingdomName + "!");
/*  870 */     this.acceptDate = 0L;
/*  871 */     this.challengeDate = 0L;
/*  872 */     updateChallenges();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setFailedChallenge() {
/*  877 */     if (!hasFailedAllChallenges()) {
/*      */       
/*  879 */       HistoryManager.addHistory(this.kingName, "failed the challenge put forth by the people of " + this.kingdomName + " and may now be voted away from the throne.");
/*      */       
/*  881 */       Message mess = new Message(null, (byte)10, Kingdoms.getChatNameFor(this.kingdom), "<" + this.kingName + "> has failed the challenge and may now be voted away from the throne.");
/*      */ 
/*      */       
/*  884 */       Player[] playarr = Players.getInstance().getPlayers();
/*      */       
/*  886 */       byte windowKingdom = this.kingdom;
/*      */       
/*  888 */       for (Player lElement : playarr) {
/*      */         
/*  890 */         if (windowKingdom == lElement.getKingdomId() || lElement.getPower() > 0)
/*  891 */           lElement.getCommunicator().sendMessage(mess); 
/*      */       } 
/*  893 */       resetNextChallenge(System.currentTimeMillis() + challengeFactor);
/*  894 */       this.acceptDate = 0L;
/*  895 */       this.challengeDate = 0L;
/*  896 */       this.declinedChallenges = 3;
/*  897 */       updateChallenges();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean mayBeChallenged() {
/*  903 */     return (System.currentTimeMillis() - this.challengeDate > challengeFactor && System.currentTimeMillis() > getNextChallenge());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasFailedToRespondToChallenge() {
/*  908 */     return (this.challengeDate != 0L && System.currentTimeMillis() - this.challengeDate > challengeFactor);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasFailedAllChallenges() {
/*  913 */     return (this.declinedChallenges >= 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getVotes() {
/*  918 */     return PlayerInfoFactory.getVotesForKingdom(this.kingdom);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getVotesNeeded() {
/*  923 */     return Math.max(0, votesRequired - getVotes());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasBeenChallenged() {
/*  928 */     int challengesCast = 0;
/*  929 */     for (Integer i : challenges.values()) {
/*      */       
/*  931 */       if (i.intValue() == this.era)
/*  932 */         challengesCast++; 
/*      */     } 
/*  934 */     return (challengesCast >= challengesRequired);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean addChallenge(Creature challenger) {
/*  939 */     if (challenger.getKingdomId() == this.kingdom) {
/*      */       
/*  941 */       if (Servers.isThisATestServer()) {
/*      */         
/*  943 */         boolean bool = hasBeenChallenged();
/*  944 */         challenges.put(Long.valueOf(Server.rand.nextLong()), Integer.valueOf(this.era));
/*  945 */         if (hasBeenChallenged() != bool)
/*  946 */           setChallengeDate(); 
/*  947 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  951 */       if (challenges.containsKey(Long.valueOf(challenger.getWurmId())))
/*      */       {
/*      */         
/*  954 */         return false;
/*      */       }
/*  956 */       boolean wasChallenged = hasBeenChallenged();
/*  957 */       challenges.put(Long.valueOf(challenger.getWurmId()), Integer.valueOf(this.era));
/*      */       
/*  959 */       if (hasBeenChallenged() != wasChallenged)
/*  960 */         setChallengeDate(); 
/*  961 */       return true;
/*      */     } 
/*      */     
/*  964 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getChallengeSize() {
/*  969 */     return challenges.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateChallenges() {
/*  974 */     Connection dbcon = null;
/*  975 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  978 */       dbcon = DbConnector.getZonesDbCon();
/*  979 */       ps = dbcon.prepareStatement("UPDATE KING_ERA SET NEXTCHALLENGE=?,DECLINEDCHALLENGES=?,ACCEPTDATE=?,CHALLENGEDATE=? WHERE ERA=?");
/*  980 */       ps.setLong(1, this.nextChallenge);
/*  981 */       ps.setLong(2, this.declinedChallenges);
/*  982 */       ps.setLong(3, this.acceptDate);
/*  983 */       ps.setLong(4, this.challengeDate);
/*  984 */       ps.setInt(5, this.era);
/*  985 */       ps.executeUpdate();
/*      */     }
/*  987 */     catch (SQLException sqex) {
/*      */       
/*  989 */       logger.log(Level.WARNING, "Failed to update challenges: for era " + this.era + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  993 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  994 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addLevelsLost(int lost) {
/* 1000 */     this.levelslost += lost;
/* 1001 */     logger.log(Level.INFO, this.kingName + " adding " + lost + " levels lost to " + this.levelslost + " for kingdom " + 
/*      */ 
/*      */         
/* 1004 */         Kingdoms.getChatNameFor(this.kingdom) + " era " + this.era);
/* 1005 */     Connection dbcon = null;
/* 1006 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1009 */       dbcon = DbConnector.getZonesDbCon();
/* 1010 */       ps = dbcon.prepareStatement("UPDATE KING_ERA SET LEVELSLOST=? WHERE ERA=?");
/* 1011 */       ps.setInt(1, this.levelslost);
/* 1012 */       ps.setInt(2, this.era);
/* 1013 */       ps.executeUpdate();
/*      */     }
/* 1015 */     catch (SQLException sqex) {
/*      */       
/* 1017 */       logger.log(Level.WARNING, "Failed to update for era " + this.era + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1021 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1022 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addLevelsKilled(int killed, String name, int worth) {
/* 1028 */     this.levelskilled += killed;
/* 1029 */     logger.log(Level.INFO, this.kingName + " killed " + name + " worth " + worth + " adding " + killed + " levels killed to " + this.levelskilled + " for kingdom " + 
/*      */         
/* 1031 */         Kingdoms.getChatNameFor(this.kingdom) + " era " + this.era);
/* 1032 */     Connection dbcon = null;
/* 1033 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1036 */       dbcon = DbConnector.getZonesDbCon();
/* 1037 */       ps = dbcon.prepareStatement("UPDATE KING_ERA SET LEVELSKILLED=? WHERE ERA=?");
/* 1038 */       ps.setInt(1, this.levelskilled);
/* 1039 */       ps.setInt(2, this.era);
/* 1040 */       ps.executeUpdate();
/*      */     }
/* 1042 */     catch (SQLException sqex) {
/*      */       
/* 1044 */       logger.log(Level.WARNING, "Failed to update for era " + this.era + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1048 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1049 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public float getLandSuccessPercent() {
/* 1055 */     if (this.startLand == 0.0F)
/* 1056 */       this.startLand = this.currentLand; 
/* 1057 */     if (this.startLand == 0.0F)
/* 1058 */       return 100.0F; 
/* 1059 */     return this.currentLand / this.startLand * 100.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAppointedSuccessPercent() {
/* 1064 */     if (this.levelskilled == 0 && this.levelslost == 0)
/* 1065 */       return 100.0F; 
/* 1066 */     if (this.levelslost < 20 && this.levelskilled < 20)
/* 1067 */       return 100.0F; 
/* 1068 */     if (this.levelslost == 0 && this.levelskilled != 0)
/* 1069 */       return (100 + this.levelskilled); 
/* 1070 */     if (this.levelslost != 0 && this.levelskilled == 0)
/* 1071 */       return (100 - this.levelslost); 
/* 1072 */     return this.levelskilled / this.levelslost * 100.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   private String getSuccessTitle() {
/* 1077 */     float successPercentSinceStart = getLandSuccessPercent();
/* 1078 */     if (successPercentSinceStart < 100.0F) {
/*      */       
/* 1080 */       if (successPercentSinceStart < 10.0F)
/* 1081 */         return "the Traitor"; 
/* 1082 */       if (successPercentSinceStart < 20.0F)
/* 1083 */         return "the Tragic"; 
/* 1084 */       if (successPercentSinceStart < 30.0F)
/* 1085 */         return "the Joke"; 
/* 1086 */       if (successPercentSinceStart < 50.0F)
/* 1087 */         return "the Imbecile"; 
/* 1088 */       if (successPercentSinceStart < 70.0F)
/* 1089 */         return "the Failed"; 
/* 1090 */       if (successPercentSinceStart < 90.0F) {
/* 1091 */         return "the Stupid";
/*      */       }
/* 1093 */       return "the Acceptable";
/*      */     } 
/*      */ 
/*      */     
/* 1097 */     if (successPercentSinceStart < 110.0F)
/* 1098 */       return "the Acceptable"; 
/* 1099 */     if (successPercentSinceStart < 120.0F)
/* 1100 */       return "the Lucky"; 
/* 1101 */     if (successPercentSinceStart < 130.0F)
/* 1102 */       return "the Conquering"; 
/* 1103 */     if (successPercentSinceStart < 140.0F)
/* 1104 */       return "the Strong"; 
/* 1105 */     if (successPercentSinceStart < 150.0F)
/* 1106 */       return "the Impressive"; 
/* 1107 */     if (successPercentSinceStart < 180.0F)
/* 1108 */       return "the Great"; 
/* 1109 */     if (successPercentSinceStart < 200.0F)
/* 1110 */       return "the Fantastic"; 
/* 1111 */     if (successPercentSinceStart < 400.0F) {
/* 1112 */       return "the Magnificent";
/*      */     }
/* 1114 */     return "the Divine";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String getAppointmentSuccess() {
/* 1120 */     float successPercentSinceStart = getAppointedSuccessPercent();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1138 */     if (successPercentSinceStart < 110.0F)
/* 1139 */       return ""; 
/* 1140 */     if (successPercentSinceStart < 120.0F)
/* 1141 */       return ""; 
/* 1142 */     if (successPercentSinceStart < 150.0F)
/* 1143 */       return " Warrior"; 
/* 1144 */     if (successPercentSinceStart < 180.0F)
/* 1145 */       return " Defender"; 
/* 1146 */     if (successPercentSinceStart < 200.0F)
/* 1147 */       return " Statesman"; 
/* 1148 */     if (successPercentSinceStart < 400.0F) {
/* 1149 */       return " Saviour";
/*      */     }
/* 1151 */     return " Holiness";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFullTitle() {
/* 1157 */     return getRulerTitle() + " " + this.kingName + " " + getSuccessTitle() + getAppointmentSuccess();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOfficial(int officeId, long wurmid, byte kingdom) {
/* 1162 */     King tempKing = getKing(kingdom);
/* 1163 */     if (tempKing != null)
/*      */     {
/* 1165 */       if (tempKing.appointments != null)
/*      */       {
/* 1167 */         return (tempKing.appointments.officials[officeId - 1500] == wurmid);
/*      */       }
/*      */     }
/* 1170 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Creature getOfficial(byte _kingdom, int officeId) {
/* 1175 */     King tempKing = getKing(_kingdom);
/* 1176 */     if (tempKing != null)
/*      */     {
/* 1178 */       if (tempKing.appointments != null) {
/*      */         
/* 1180 */         long wurmid = tempKing.appointments.officials[officeId - 1500];
/*      */         
/*      */         try {
/* 1183 */           Player p = Players.getInstance().getPlayer(wurmid);
/* 1184 */           return (Creature)p;
/*      */         }
/* 1186 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1192 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\kingdom\King.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
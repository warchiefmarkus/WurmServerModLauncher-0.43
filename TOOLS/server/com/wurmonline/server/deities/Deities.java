/*      */ package com.wurmonline.server.deities;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
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
/*      */ public final class Deities
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*   55 */   private static final Map<Integer, Deity> deities = new HashMap<>();
/*      */   
/*      */   private static final String LOAD_DEITIES = "SELECT * FROM DEITIES";
/*      */   
/*      */   private static final String CALCULATE_FAITHS = "SELECT DEITY,FAITH, KINGDOM, PAYMENTEXPIRE FROM PLAYERS WHERE POWER=0 AND LASTLOGOUT>?";
/*      */   
/*   61 */   private static final Map<String, Integer> valreiStatuses = new HashMap<>();
/*   62 */   private static final Map<Integer, Integer> valreiPositions = new HashMap<>();
/*   63 */   private static final Map<Integer, String> valreiNames = new HashMap<>();
/*      */ 
/*      */   
/*   66 */   private static Logger logger = Logger.getLogger(Deities.class.getName());
/*      */   
/*      */   public static final int DEITY_NONE = 0;
/*      */   
/*      */   public static final int DEITY_FO = 1;
/*      */   
/*      */   public static final int DEITY_MAGRANON = 2;
/*      */   
/*      */   public static final int DEITY_VYNORA = 3;
/*      */   
/*      */   public static final int DEITY_LIBILA = 4;
/*      */   
/*      */   public static final int DEITY_WURM = 5;
/*      */   
/*      */   public static final int DEITY_NOGUMP = 6;
/*      */   
/*      */   public static final int DEITY_WALNUT = 7;
/*      */   
/*      */   public static final int DEITY_PHARMAKOS = 8;
/*      */   
/*      */   public static final int DEITY_JACKAL = 9;
/*      */   
/*      */   public static final int DEITY_DEATHCRAWLER = 10;
/*      */   
/*      */   public static final int DEITY_SCAVENGER = 11;
/*      */   
/*      */   public static final int DEITY_GIANT = 12;
/*      */   
/*      */   public static final int DEITY_RESERVED = 100;
/*      */   
/*      */   public static final int DEITY_TOSIEK = 31;
/*      */   
/*      */   public static final int DEITY_NAHJO = 32;
/*      */   
/*      */   public static final int DEITY_NATHAN = 33;
/*      */   
/*      */   public static final int DEITY_PAAWEELR = 34;
/*      */   public static final int DEITY_SMEAGAIN = 35;
/*      */   public static final int DEITY_GARY = 36;
/*  105 */   public static int maxDeityNum = 100;
/*      */   
/*      */   public static final float THEFTMOD = -0.25F;
/*      */   
/*  109 */   public static float faithPlayers = 0.0F;
/*      */   
/*      */   public static final int FAVORNEEDEDFORRITUALS = 100000;
/*      */ 
/*      */   
/*      */   static {
/*      */     try {
/*  116 */       logger.log(Level.INFO, "Loading deities ");
/*  117 */       loadDeities();
/*      */     }
/*  119 */     catch (IOException iox) {
/*      */       
/*  121 */       logger.log(Level.WARNING, "Failed to load deities!", iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isOkOnFreedom(int deityNum, byte kingdomId) {
/*  134 */     if (kingdomId == 4)
/*      */     {
/*  136 */       return (deityNum == 1 || deityNum == 3 || deityNum == 2);
/*      */     }
/*  138 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte getFavoredKingdom(int deityNum) {
/*  144 */     if (deityNum == 1 || deityNum == 3)
/*      */     {
/*  146 */       return 1;
/*      */     }
/*  148 */     if (deityNum == 2)
/*      */     {
/*  150 */       return 2;
/*      */     }
/*  152 */     if (deityNum == 4)
/*      */     {
/*  154 */       return 3;
/*      */     }
/*  156 */     Deity d = getDeity(deityNum);
/*  157 */     if (d != null) {
/*  158 */       return d.getFavoredKingdom();
/*      */     }
/*  160 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void rollBefriendPassive(Deity deity, Random rand) {
/*  167 */     if (deity.isHateGod()) {
/*  168 */       deity.setBefriendMonster((rand.nextInt(2) == 0));
/*  169 */     } else if (deity.isForestGod()) {
/*  170 */       deity.setBefriendCreature((rand.nextInt(2) == 0));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  175 */       deity.setBefriendCreature((rand.nextInt(5) == 0));
/*  176 */       if (!deity.isBefriendCreature()) {
/*  177 */         deity.setBefriendMonster((rand.nextInt(10) == 0));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void rollWarriorPassive(Deity deity, Random rand) {
/*  189 */     if (deity.isMountainGod()) {
/*  190 */       deity.setWarrior((rand.nextInt(2) == 0));
/*  191 */     } else if (deity.isHateGod()) {
/*  192 */       deity.setDeathItemProtector((rand.nextInt(2) == 0));
/*      */     } 
/*      */     
/*  195 */     if (deity.isWarrior() || deity.isDeathItemProtector()) {
/*      */       return;
/*      */     }
/*      */     
/*  199 */     deity.setWarrior((rand.nextInt(4) == 0));
/*  200 */     if (!deity.isWarrior()) {
/*      */       
/*  202 */       deity.setDeathItemProtector((rand.nextInt(4) == 0));
/*  203 */       if (!deity.isDeathItemProtector())
/*      */       {
/*  205 */         deity.setDeathProtector((rand.nextInt(3) == 0));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  210 */     if (deity.isWarrior() || deity.isDeathItemProtector() || deity.isDeathProtector()) {
/*      */       return;
/*      */     }
/*      */     
/*  214 */     deity.setDeathProtector(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void rollHealingLearningPassive(Deity deity, Random rand) {
/*  222 */     if (deity.isWarrior() || deity.isDeathItemProtector()) {
/*      */       
/*  224 */       deity.setLearner((rand.nextInt(3) == 0));
/*      */       
/*      */       return;
/*      */     } 
/*  228 */     if (!deity.isHateGod()) {
/*      */ 
/*      */       
/*  231 */       if (deity.isForestGod()) {
/*  232 */         deity.setHealer((rand.nextInt(3) > 0));
/*      */       } else {
/*  234 */         deity.setHealer((rand.nextInt(3) == 0));
/*      */       } 
/*      */       
/*  237 */       if (deity.isHealer()) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  243 */     deity.setLearner(true);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void initializeDemigodPassives(Deity deity) {
/*  248 */     Random rand = deity.initializeAndGetRand();
/*      */ 
/*      */     
/*  251 */     int template = 1 + rand.nextInt(4);
/*      */ 
/*      */ 
/*      */     
/*  255 */     if (deity.getNumber() == 31 || deity.getNumber() == 33 || deity.getNumber() == 36)
/*  256 */       template = 4; 
/*  257 */     if (deity.getNumber() == 32)
/*      */     {
/*      */ 
/*      */       
/*  261 */       template = 1; } 
/*  262 */     if (deity.getNumber() == 34)
/*      */     {
/*  264 */       template = 3; } 
/*  265 */     if (deity.getNumber() == 35)
/*      */     {
/*  267 */       template = 2;
/*      */     }
/*  269 */     deity.setTemplateDeity(template);
/*      */ 
/*      */     
/*  272 */     if (template == 1) {
/*      */       
/*  274 */       deity.setForestGod(true);
/*  275 */       deity.setClothAffinity(true);
/*      */     }
/*  277 */     else if (template == 2) {
/*      */       
/*  279 */       deity.setMountainGod(true);
/*  280 */       deity.setMetalAffinity(true);
/*      */     }
/*  282 */     else if (template == 3) {
/*      */       
/*  284 */       deity.setWaterGod(true);
/*  285 */       deity.setClayAffinity(true);
/*      */     }
/*  287 */     else if (template == 4) {
/*      */       
/*  289 */       deity.setHateGod(true);
/*  290 */       deity.setMeatAffinity(true);
/*      */     } 
/*      */ 
/*      */     
/*  294 */     rollBefriendPassive(deity, rand);
/*  295 */     rollWarriorPassive(deity, rand);
/*  296 */     rollHealingLearningPassive(deity, rand);
/*      */ 
/*      */     
/*  299 */     if (deity.isHateGod()) {
/*      */       
/*  301 */       deity.setAllowsButchering(true);
/*  302 */       deity.setFavorRegenerator((rand.nextInt(2) == 0));
/*      */     }
/*      */     else {
/*      */       
/*  306 */       deity.setAllowsButchering((rand.nextInt(5) == 0));
/*  307 */       deity.setFavorRegenerator((rand.nextInt(4) == 0));
/*      */     } 
/*      */ 
/*      */     
/*  311 */     if (deity.isForestGod()) {
/*      */       
/*  313 */       deity.setFoodAffinity((rand.nextInt(2) == 0));
/*  314 */       deity.setStaminaBonus((rand.nextInt(2) == 0));
/*  315 */       deity.setFoodBonus((rand.nextInt(2) == 0));
/*      */     }
/*      */     else {
/*      */       
/*  319 */       deity.setFoodAffinity((rand.nextInt(4) == 0));
/*  320 */       deity.setStaminaBonus((rand.nextInt(4) == 0));
/*  321 */       deity.setFoodBonus((rand.nextInt(4) == 0));
/*      */     } 
/*      */ 
/*      */     
/*  325 */     if (deity.isWaterGod()) {
/*      */       
/*  327 */       deity.setRoadProtector((rand.nextInt(2) == 0));
/*  328 */       deity.setItemProtector((rand.nextInt(2) == 0));
/*  329 */       deity.setRepairer((rand.nextInt(2) == 0));
/*  330 */       deity.setBuildWallBonus(rand.nextInt(10));
/*  331 */       deity.setWoodAffinity((rand.nextInt(2) == 0));
/*      */     }
/*      */     else {
/*      */       
/*  335 */       deity.setRoadProtector((rand.nextInt(4) == 0));
/*  336 */       deity.setItemProtector((rand.nextInt(4) == 0));
/*  337 */       deity.setRepairer((rand.nextInt(4) == 0));
/*  338 */       deity.setBuildWallBonus((5 - rand.nextInt(10)));
/*  339 */       deity.setWoodAffinity((rand.nextInt(4) == 0));
/*      */     } 
/*      */ 
/*      */     
/*  343 */     if (deity.isHealer()) {
/*  344 */       deity.alignment = 100;
/*  345 */     } else if (deity.isHateGod()) {
/*  346 */       deity.alignment = -100;
/*      */     } 
/*  348 */     if (deity.getNumber() == 35) {
/*  349 */       deity.setDeathItemProtector(true);
/*      */     }
/*  351 */     if (deity.getNumber() == 32) {
/*  352 */       deity.setMeatAffinity(true);
/*      */     }
/*  354 */     createHumanConvertStrings(deity);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addDeity(Deity deity) {
/*  359 */     if (deity.number == 1) {
/*      */ 
/*      */       
/*  362 */       deity.setTemplateDeity(1);
/*  363 */       deity.setForestGod(true);
/*      */ 
/*      */       
/*  366 */       deity.setClothAffinity(true);
/*  367 */       deity.setFoodAffinity(true);
/*      */ 
/*      */       
/*  370 */       deity.setBefriendCreature(true);
/*  371 */       deity.setStaminaBonus(true);
/*  372 */       deity.setFoodBonus(true);
/*  373 */       deity.setHealer(true);
/*  374 */       createFoConvertStrings(deity);
/*      */     }
/*  376 */     else if (deity.number == 2) {
/*      */ 
/*      */       
/*  379 */       deity.setTemplateDeity(2);
/*  380 */       deity.setMountainGod(true);
/*      */ 
/*      */       
/*  383 */       deity.setMetalAffinity(true);
/*      */ 
/*      */       
/*  386 */       deity.setWarrior(true);
/*  387 */       deity.setDeathProtector(true);
/*  388 */       deity.setDeathItemProtector(true);
/*  389 */       createMagranonConvertStrings(deity);
/*      */     }
/*  391 */     else if (deity.number == 3) {
/*      */ 
/*      */       
/*  394 */       deity.setTemplateDeity(3);
/*  395 */       deity.setWaterGod(true);
/*      */ 
/*      */       
/*  398 */       deity.setClayAffinity(true);
/*  399 */       deity.setWoodAffinity(true);
/*      */ 
/*      */       
/*  402 */       deity.setRoadProtector(true);
/*  403 */       deity.setItemProtector(true);
/*  404 */       deity.setRepairer(true);
/*  405 */       deity.setLearner(true);
/*  406 */       deity.setBuildWallBonus(20.0F);
/*  407 */       createVynoraConvertStrings(deity);
/*      */     }
/*  409 */     else if (deity.number == 4) {
/*      */ 
/*      */       
/*  412 */       deity.setTemplateDeity(4);
/*  413 */       deity.setHateGod(true);
/*      */ 
/*      */       
/*  416 */       deity.setMeatAffinity(true);
/*      */ 
/*      */       
/*  419 */       deity.setFavorRegenerator(true);
/*  420 */       deity.setBefriendMonster(true);
/*  421 */       deity.setDeathProtector(true);
/*  422 */       deity.setAllowsButchering(true);
/*  423 */       createLibilaConvertStrings(deity);
/*      */     }
/*  425 */     else if (deity.isCustomDeity()) {
/*      */ 
/*      */ 
/*      */       
/*  429 */       initializeDemigodPassives(deity);
/*  430 */       createHumanConvertStrings(deity);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  440 */       Random rand = deity.initializeAndGetRand();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  446 */       deity.setMeatAffinity((rand.nextInt(3) == 0));
/*  447 */       deity.setHateGod((rand.nextInt(3) == 0 || deity.alignment < 0));
/*  448 */       deity.setAllowsButchering((rand.nextInt(3) == 0 || deity.isHateGod()));
/*  449 */       deity.setRoadProtector((rand.nextInt(3) == 0));
/*  450 */       deity.setItemProtector((rand.nextInt(3) == 0));
/*      */       
/*  452 */       deity.setWarrior((rand.nextInt(3) == 0));
/*  453 */       deity.setDeathProtector((rand.nextInt(3) == 0));
/*  454 */       deity.setDeathItemProtector((rand.nextInt(3) == 0));
/*  455 */       deity.setMetalAffinity((rand.nextInt(3) == 0));
/*  456 */       deity.setMountainGod((rand.nextInt(3) == 0 || deity.number == 32));
/*      */       
/*  458 */       deity.setRepairer((rand.nextInt(3) == 0));
/*  459 */       deity.setLearner((rand.nextInt(3) == 0));
/*  460 */       deity.setBuildWallBonus(rand.nextInt(10));
/*  461 */       deity.setWoodAffinity((rand.nextInt(3) == 0));
/*  462 */       deity.setWaterGod((rand.nextInt(3) == 0 || deity.number == 32));
/*      */       
/*  464 */       deity.setBefriendCreature((rand.nextInt(3) == 0));
/*  465 */       deity.setStaminaBonus((rand.nextInt(3) == 0));
/*  466 */       deity.setFoodBonus((rand.nextInt(3) == 0));
/*  467 */       if (!deity.isHateGod())
/*  468 */         deity.setHealer((rand.nextInt(3) == 0)); 
/*  469 */       deity.setClayAffinity((rand.nextInt(3) == 0));
/*  470 */       deity.setClothAffinity((rand.nextInt(3) == 0));
/*  471 */       deity.setFoodAffinity((rand.nextInt(3) == 0));
/*      */ 
/*      */       
/*  474 */       if (!deity.isHateGod() && !deity.isWaterGod() && !deity.isMountainGod()) {
/*  475 */         deity.setForestGod((rand.nextInt(8) < 2));
/*      */       }
/*  477 */       if (deity.isHealer()) {
/*  478 */         deity.alignment = 100;
/*  479 */       } else if (deity.isHateGod()) {
/*  480 */         deity.alignment = -100;
/*  481 */       }  createHumanConvertStrings(deity);
/*      */     } 
/*  483 */     deities.put(Integer.valueOf(deity.number), deity);
/*      */   }
/*      */ 
/*      */   
/*      */   static void removeDeity(int number) {
/*  488 */     deities.remove(Integer.valueOf(number));
/*      */   }
/*      */ 
/*      */   
/*      */   public static Deity getDeity(int number) {
/*  493 */     return deities.get(Integer.valueOf(number));
/*      */   }
/*      */ 
/*      */   
/*      */   private static void resetDeityFollowers() {
/*  498 */     for (Deity d : deities.values())
/*      */     {
/*  500 */       d.setActiveFollowers(0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Deity[] getDeities() {
/*  510 */     return (Deity[])deities.values().toArray((Object[])new Deity[deities.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<Integer, String> getEntities() {
/*  519 */     return valreiNames;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getEntityNumber(String name) {
/*  524 */     for (Map.Entry<Integer, String> entry : valreiNames.entrySet()) {
/*      */       
/*  526 */       if (((String)entry.getValue()).equalsIgnoreCase(name))
/*  527 */         return ((Integer)entry.getKey()).intValue(); 
/*      */     } 
/*  529 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Deity translateDeityForEntity(int entityNumber) {
/*  534 */     String entityName = valreiNames.get(Integer.valueOf(entityNumber));
/*  535 */     if (entityName != null)
/*      */     {
/*  537 */       for (Deity d : deities.values()) {
/*      */         
/*  539 */         if (d.getName().equalsIgnoreCase(entityName))
/*  540 */           return d; 
/*      */       } 
/*      */     }
/*  543 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int translateEntityForDeity(int deityNumber) {
/*  548 */     Deity d = getDeity(deityNumber);
/*  549 */     if (d != null)
/*  550 */       for (Map.Entry<Integer, String> entry : valreiNames.entrySet()) {
/*      */         
/*  552 */         if (((String)entry.getValue()).equalsIgnoreCase(d.getName()))
/*  553 */           return ((Integer)entry.getKey()).intValue(); 
/*      */       }  
/*  555 */     return -1;
/*      */   }
/*      */   
/*      */   public static final void addEntity(int number, String name) {
/*  559 */     valreiNames.put(Integer.valueOf(number), name);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isNameOkay(String aName) {
/*  564 */     String lName = aName.toLowerCase();
/*  565 */     for (Deity d : deities.values()) {
/*      */       
/*  567 */       if (d.getNumber() < 100)
/*      */       {
/*  569 */         if (lName.equals(d.name.toLowerCase()))
/*      */         {
/*  571 */           return false;
/*      */         }
/*      */       }
/*      */     } 
/*  575 */     if (lName.equals("jackal") || lName.equals("valrej") || lName.equals("valrei") || lName.equals("seris") || lName
/*  576 */       .equals("sol") || lName.equals("upkeep") || lName.equals("system") || lName.equals("village") || lName
/*  577 */       .equals("team") || lName.equals("local") || lName.equals("combat") || lName.equals("friends") || lName
/*  578 */       .equals("nogump") || lName.equals("uttacha") || lName.equals("pharmakos") || lName.equals("walnut"))
/*  579 */       return false; 
/*  580 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void calculateFaiths() {
/*  590 */     Connection dbcon = null;
/*  591 */     PreparedStatement ps = null;
/*  592 */     ResultSet rs = null;
/*  593 */     resetDeityFollowers();
/*      */     
/*      */     try {
/*  596 */       dbcon = DbConnector.getPlayerDbCon();
/*  597 */       ps = dbcon.prepareStatement("SELECT DEITY,FAITH, KINGDOM, PAYMENTEXPIRE FROM PLAYERS WHERE POWER=0 AND LASTLOGOUT>?");
/*  598 */       ps.setLong(1, System.currentTimeMillis() - 604800000L);
/*  599 */       rs = ps.executeQuery();
/*  600 */       faithPlayers = 0.0F;
/*  601 */       Map<Integer, Float> faithMap = new HashMap<>();
/*  602 */       Kingdoms.activePremiumHots = 0;
/*  603 */       Kingdoms.activePremiumJenn = 0;
/*  604 */       Kingdoms.activePremiumMolr = 0;
/*      */       
/*  606 */       while (rs.next()) {
/*      */         
/*  608 */         byte d = rs.getByte("DEITY");
/*      */         
/*  610 */         if (d > 0) {
/*      */           
/*  612 */           float faith = rs.getFloat("FAITH");
/*  613 */           Float f = faithMap.get(Integer.valueOf(d));
/*  614 */           if (f == null) {
/*      */             
/*  616 */             f = new Float(faith);
/*  617 */             faithMap.put(Integer.valueOf(d), f);
/*      */           } else {
/*      */             
/*  620 */             f = new Float(f.floatValue() + faith);
/*  621 */           }  Deity deity = deities.get(Integer.valueOf(d));
/*  622 */           if (deity != null)
/*      */           {
/*  624 */             deity.setActiveFollowers(deity.getActiveFollowers() + 1);
/*      */           }
/*  626 */           faithPlayers++;
/*      */         } 
/*  628 */         byte kdom = rs.getByte("KINGDOM");
/*  629 */         if (kdom == 1) {
/*  630 */           Kingdoms.activePremiumJenn++;
/*  631 */         } else if (kdom == 3) {
/*  632 */           Kingdoms.activePremiumHots++;
/*  633 */         } else if (kdom == 2) {
/*  634 */           Kingdoms.activePremiumMolr++;
/*  635 */         }  (Kingdoms.getKingdom(kdom)).activePremiums++;
/*  636 */         (Kingdoms.getKingdom(kdom)).countedAtleastOnce = true;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  642 */         for (Map.Entry<Integer, Float> me : faithMap.entrySet())
/*      */         {
/*  644 */           Deity deity = getDeity(((Integer)me.getKey()).intValue());
/*  645 */           if (deity != null && ((Float)me.getValue()).floatValue() > 0.0F) {
/*      */             
/*  647 */             deity.setFaith((((Float)me.getValue()).floatValue() / faithPlayers));
/*  648 */             if (deity.getNumber() < 0 || deity.getNumber() > 4)
/*      */             {
/*  650 */               deity.setMaxKingdom(); } 
/*      */             continue;
/*      */           } 
/*  653 */           if (deity != null) {
/*  654 */             deity.setFaith(0.0D);
/*      */           
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*  660 */       catch (IOException iox) {
/*      */         
/*  662 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       }
/*      */     
/*  665 */     } catch (SQLException sqx) {
/*      */       
/*  667 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  671 */       DbConnector.returnConnection(dbcon);
/*  672 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/*      */ 
/*      */     
/*  676 */     Kingdoms.checkIfDisbandKingdom();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean mayDestroyAltars() {
/*  683 */     return WurmCalendar.mayDestroyHugeAltars();
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void loadDeities() throws IOException {
/*  688 */     Connection dbcon = null;
/*  689 */     PreparedStatement ps = null;
/*  690 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  693 */       dbcon = DbConnector.getDeityDbCon();
/*  694 */       ps = dbcon.prepareStatement("SELECT * FROM DEITIES");
/*  695 */       rs = ps.executeQuery();
/*  696 */       int found = 0;
/*  697 */       while (rs.next()) {
/*      */         
/*  699 */         int number = rs.getByte("ID");
/*  700 */         if (number > maxDeityNum)
/*  701 */           maxDeityNum = number; 
/*  702 */         String name = rs.getString("NAME");
/*  703 */         byte sex = rs.getByte("SEX");
/*  704 */         double faith = rs.getDouble("FAITH");
/*  705 */         int favor = rs.getInt("FAVOR");
/*  706 */         byte alignment = rs.getByte("ALIGNMENT");
/*  707 */         byte power = rs.getByte("POWER");
/*  708 */         int holyitem = rs.getInt("HOLYITEM");
/*  709 */         float attack = rs.getFloat("ATTACK");
/*  710 */         float vitality = rs.getFloat("VITALITY");
/*      */         
/*  712 */         DbDeity deity = new DbDeity(number, name, alignment, sex, power, faith, holyitem, favor, attack, vitality, false);
/*      */         
/*  714 */         addDeity(deity);
/*  715 */         if (number == 1 || number == 3) {
/*  716 */           deity.setFavoredKingdom((byte)1);
/*  717 */         } else if (number == 2) {
/*  718 */           deity.setFavoredKingdom((byte)2);
/*  719 */         } else if (number == 4) {
/*  720 */           deity.setFavoredKingdom((byte)3);
/*  721 */         }  found++;
/*      */       } 
/*  723 */       if (found == 0)
/*      */       {
/*  725 */         createBasicDeities();
/*      */       }
/*      */     }
/*  728 */     catch (SQLException sqx) {
/*      */       
/*  730 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*  731 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  735 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  736 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getNextDeityNum() {
/*  742 */     return ++maxDeityNum;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Deity ascend(int newid, String name, long wurmid, byte sex, byte power, float attack, float vitality) {
/*  748 */     if (newid > maxDeityNum)
/*  749 */       maxDeityNum = newid; 
/*  750 */     int newNum = newid;
/*  751 */     Random rand = new Random(wurmid);
/*  752 */     DbDeity deity = new DbDeity(newNum, name, (byte)0, sex, power, 0.0D, 505 + rand.nextInt(4), 0, attack, vitality, true);
/*      */     
/*  754 */     addDeity(deity);
/*      */ 
/*      */     
/*      */     try {
/*  758 */       deity.save();
/*      */     }
/*  760 */     catch (IOException iox) {
/*      */       
/*  762 */       logger.log(Level.WARNING, "Failed to save " + deity.name, iox);
/*  763 */       return null;
/*      */     } 
/*  765 */     return deity;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Deity getRandomHateDeity() {
/*  770 */     boolean exists = false;
/*  771 */     for (Deity d : deities.values()) {
/*  772 */       if (d.isHateGod() && d.number != 4)
/*  773 */         exists = true; 
/*  774 */     }  if (exists) {
/*      */       
/*  776 */       LinkedList<Deity> toRet = new LinkedList<>();
/*  777 */       for (Deity d : deities.values()) {
/*      */         
/*  779 */         if (d.isHateGod() && d.number != 4)
/*  780 */           toRet.add(d); 
/*      */       } 
/*  782 */       return toRet.get(Server.rand.nextInt(toRet.size()));
/*      */     } 
/*      */     
/*  785 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Deity getRandomNonHateDeity() {
/*  790 */     boolean exists = false;
/*  791 */     for (Deity d : deities.values()) {
/*  792 */       if (!d.isHateGod() && d.number > 100)
/*  793 */         exists = true; 
/*  794 */     }  if (exists) {
/*      */       
/*  796 */       LinkedList<Deity> toRet = new LinkedList<>();
/*  797 */       for (Deity d : deities.values()) {
/*      */         
/*  799 */         if (!d.isHateGod() && d.number > 100)
/*  800 */           toRet.add(d); 
/*      */       } 
/*  802 */       return toRet.get(Server.rand.nextInt(toRet.size()));
/*      */     } 
/*      */     
/*  805 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void createBasicDeities() {
/*  810 */     DbDeity deity = new DbDeity(1, "Fo", (byte)100, (byte)0, (byte)5, 0.0D, 505, 0, 7.0F, 5.0F, true);
/*  811 */     addDeity(deity);
/*  812 */     deity.setFavoredKingdom((byte)1);
/*      */     
/*      */     try {
/*  815 */       deity.save();
/*      */     }
/*  817 */     catch (IOException iox) {
/*      */       
/*  819 */       logger.log(Level.WARNING, "Failed to save " + deity.name, iox);
/*      */     } 
/*  821 */     deity = new DbDeity(2, "Magranon", (byte)70, (byte)0, (byte)4, 0.0D, 507, 0, 6.0F, 6.0F, true);
/*      */     
/*  823 */     addDeity(deity);
/*  824 */     deity.setFavoredKingdom((byte)2);
/*      */     
/*      */     try {
/*  827 */       deity.save();
/*      */     }
/*  829 */     catch (IOException iox) {
/*      */       
/*  831 */       logger.log(Level.WARNING, "Failed to save " + deity.name, iox);
/*      */     } 
/*  833 */     deity = new DbDeity(3, "Vynora", (byte)70, (byte)1, (byte)4, 0.0D, 508, 0, 5.0F, 7.0F, true);
/*      */     
/*  835 */     addDeity(deity);
/*  836 */     deity.setFavoredKingdom((byte)1);
/*      */ 
/*      */     
/*      */     try {
/*  840 */       deity.save();
/*      */     }
/*  842 */     catch (IOException iox) {
/*      */       
/*  844 */       logger.log(Level.WARNING, "Failed to save " + deity.name, iox);
/*      */     } 
/*  846 */     deity = new DbDeity(4, "Libila", (byte)-100, (byte)1, (byte)4, 0.0D, 506, 0, 6.0F, 6.0F, true);
/*      */     
/*  848 */     addDeity(deity);
/*  849 */     deity.setFavoredKingdom((byte)3);
/*      */     
/*      */     try {
/*  852 */       deity.save();
/*      */     }
/*  854 */     catch (IOException iox) {
/*      */       
/*  856 */       logger.log(Level.WARNING, "Failed to save " + deity.name, iox);
/*      */     } 
/*      */   }
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
/*      */   private static void createHumanConvertStrings(Deity deity) {
/*  879 */     String[] conv1 = { deity.name + " is here to grant strength and guidance.", "We are all seekers here in these foreign lands,", "We are all threatened by the ancient powers,", "And it is easy to stumble in this darkness never to rise again.", deity.getCapHeSheItString() + " will lead you through the marshes and caverns, and " + deity.getHeSheItString() + " will hold your hand when you falter.", deity.isHealer() ? ("Follow " + deity.getHimHerItString() + " and " + deity.getHeSheItString() + " will keep you safe and healthy.") : ("Trust " + deity.getHimHerItString() + " in the darkness and sorrow."), deity.isHateGod() ? "Together we will crush our enemies and rule here in eternity." : "There is a blessed land we want to show you.", deity.isHateGod() ? ("Listen carefully and you will hear the thunder of " + deity.getHisHerItsString() + " armies!") : "Join us in freedom and follow us in peaceful bliss!", "The path leads on to victory and a new dawn for humankind.", deity.isHateGod() ? "Will you join us or be crushed like a flea?" : "Will you join us?" };
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
/*  891 */     String[] alt1 = { deity.isHateGod() ? "Mortal," : "Dear friend,", "I have transcended to a higher state of being.", "I am now among the Immortals.", "I have become the epitome of " + (deity.isHateGod() ? "darkness." : (deity.isHealer() ? "love." : "light.")), "Trust me when I promise you a path to strength and glory.", deity.isHateGod() ? "I will lead the way and you will follow." : "I will walk beside you in eternal friendship.", deity.isHateGod() ? "Together we will slay our enemies in the sleep and tear their children apart." : "I will support you when you stagger, and keep your children safe.", "Nothing will stop us and one day we will meet on the Western Spurs and drink " + (deity.isHateGod() ? "our enemies' blood." : "honey and wine!"), "Let us grow together and conquer the forbidden lands where our souls rule in eternity!", "Are you ready to join us?" };
/*      */ 
/*      */     
/*  894 */     deity.convertText1 = conv1;
/*  895 */     deity.altarConvertText1 = alt1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createFoConvertStrings(Deity fo) {
/*  900 */     String[] conv1 = { "Fo!", "His creations surround you. His love is everywhere around you.", "He is the father of all things. He created the world out of love and passion.", "To embrace Fo is to embrace all living things around you.", "All and everyone is equal, but different.", "We are all dirt. We are all gems. We just come in different shapes and colors.", "To create more and love all that is already created is to love Fo.", "To passionately strike down at those who aim to destroy these creations is to love Fo.", "To strive after beauty and harmony with nature is to love Fo.", "If you love Fo, Fo loves you!" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  909 */     String[] alt1 = { "I am Fo.", "I am the Silence and the Trees. The sprout is my symbol.", "Silent and lonely I lingered in darkness.", "Look around you. I created this of love and loneliness.", "You are all like, but different.", "You are all dirt. You are all gems. You just come in different shapes and colors.", "Do my bidding. Let all things grow into the splendor they may possess.", "Strive after beauty and harmony with nature. Let your soul become a lustrous gem.", "With the same passion by which I once created all this, strike down at those who aim to destroy these creations.", "Love me. Let me love you." };
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
/*  921 */     fo.convertText1 = conv1;
/*  922 */     fo.altarConvertText1 = alt1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createMagranonConvertStrings(Deity magranon) {
/*  927 */     String[] conv1 = { "Is your goal in life to achieve riches? To achieve freedom?", "Who is stopping you? You are. Who will help you? Magranon will!", "We, the followers of Magranon will stand at the top of the world one day and sing!", "Together we will strive to rule the world. We will conquer all evil, build fantastic houses and live rich and glorious lives in them.", "What is knowledge for if you do not use it? What use is compassion if you are hungry?", "What are the alternatives?", "Say yes to yourself! Say 'I will!' Your world will change, and you will change the world!", "There are obstacles. People and forces will oppose us. Who will want to deny us all we strive for.", "That force must be utterly defeated! No victory will be possible unless we cleanse the world of that evil.", "Join our ranks. Help yourself reach the top!" };
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
/*  939 */     String[] alt1 = { "Listen to the words of Magranon:", "What are you? Could you not be more?", "I am the Fire and the Mountain. A sword is my symbol.", "I will help you rule the world. You will conquer all evil, and live a rich and glorious life.", "One day you will stand at the top of the world and sing!", "What is knowledge for if you do not use it? What use is compassion if you are hungry?", "Paths leading endlessly into the mist! What matters is power!", "First, power over self. Then, power over others.", "Say yes to yourself! Say 'I will!' Your world will change, and you will change the world!", "Let me help. Together, nothing can stop us!" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  949 */     magranon.convertText1 = conv1;
/*  950 */     magranon.altarConvertText1 = alt1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createVynoraConvertStrings(Deity vynora) {
/*  955 */     String[] conv1 = { "What is this?", "Have you ever asked yourself that question?", "Vynora, our godess and guide, will help you seek the answer to that ancient riddle, just as we help her in her quest to know everything.", "Many secrets has she gathered, and we who call ourselves Seekers will be the first ones to learn.", "What is a Man? What is a Woman? What lies in darkness of the Void? Questions need answers!", "Seekers strive after excellence. We seek the truth in all things. We will go anywhere in our attempts to find it.", "True knowledge also brings us power. Power over self, but also power over others.", "Our gathered experience tells us that the best for all is to use that knowledge with care.", "Therefore most of us are peaceful and strive after a calm and orderly way to gather knowledge.", "Welcome to join the followers of Vynora!" };
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
/*  967 */     String[] alt1 = { "Seeker!", "I am the Water and the Wind. A bowl is my symbol.", "Have you ever asked yourself the Questions?", "I will help you find the answer to the Ancient Riddles, if you help me.", "My knowledge is vast, but I need to know the last parts! We all must know!", "What are you? What lingers in the darkness of the Void?", "Seek excellence. Seek the truth in all things. Go anywhere in your attempts to find it.", "True knowledge also brings you power.", "Exercise that power with care, or it will hurt you like the snake who bites its tail.", "Too many secrets are hidden by the other gods, and none will they reveal. This cannot be.", "Flow with me!" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  976 */     vynora.convertText1 = conv1;
/*  977 */     vynora.altarConvertText1 = alt1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createLibilaConvertStrings(Deity libila) {
/*  982 */     String[] conv1 = { "Look at you. Pitiful creature.", "You seek the powers of the Whisperer? They may be available.", "Know that I personally will not help you much. But I am bound to tell you that she will.", "To tell the truth - She rewards me for recruiting you.", "People say much about us. They think we lie. And yes we do. We lie about a lot of things, but not about the truth.", "Truth is everything will end. Truth is some of us will be rewarded greatly by Her some day.", "Libila will not accept her having been betrayed by the other gods. Your contract with her is to help her, and she will help you.", "Her aim is to gain control here, and your goal is to stop the others from gaining it instead.", "You are expected and required to use all effective means available: Terror, deception, torture, death, sacrifices.", "Are you ready to join the Horde of the Summoned? Know that if you choose not to, you are against us!" };
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
/*  994 */     String[] alt1 = { "Look at you. Pitiful creature.", "You seek the powers of the Whisperer? They may be available.", "I am the Hate and the Deceit, but know that I will help you much.", "Know that I want revenge for the betrayal by the others. This is why the scythe is my symbol.", "Become my tool and my weapon. Let me sharpen you, and let me run you through the heart of my enemies.", "For this I will reward you greatly. You will be given powers beyond normal mortal possibilities.", "Exact my revenge anywhere, anytime and anyhow. Make it painful and frightening.", "Together, let us enter the Forbidden Lands. We are all in our right to do so!", "Let us grow together, and throw our enemies into the void!", "Are you ready to join the Horde of the Summoned? Know that if you choose not to, you are against me!" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1004 */     libila.convertText1 = conv1;
/* 1005 */     libila.altarConvertText1 = alt1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean acceptsNewChampions(int deityNumber) {
/* 1010 */     int nums = PlayerInfoFactory.getNumberOfChamps(deityNumber);
/* 1011 */     if (deityNumber == 1)
/* 1012 */       nums += PlayerInfoFactory.getNumberOfChamps(3); 
/* 1013 */     if (deityNumber == 3)
/* 1014 */       nums += PlayerInfoFactory.getNumberOfChamps(1); 
/* 1015 */     if (deityNumber == 4) {
/* 1016 */       return (nums < 200);
/*      */     }
/* 1018 */     return (nums < 200);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void clearValreiPositions() {
/* 1023 */     valreiPositions.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean hasValreiPositions() {
/* 1028 */     return (valreiPositions.size() > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addPosition(int deityId, int hexPosition) {
/* 1033 */     valreiPositions.put(Integer.valueOf(deityId), Integer.valueOf(hexPosition));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Integer getPosition(int deityId) {
/* 1038 */     return valreiPositions.get(Integer.valueOf(deityId));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void clearValreiStatuses() {
/* 1043 */     valreiStatuses.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void addStatus(String status, int deityId) {
/* 1049 */     valreiStatuses.put(status, Integer.valueOf(deityId));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getEntityName(int deityId) {
/* 1054 */     switch (deityId) {
/*      */       
/*      */       case 7:
/* 1057 */         return "Walnut";
/*      */       case 10:
/* 1059 */         return "The Deathcrawler";
/*      */       case 8:
/* 1061 */         return "Pharmakos";
/*      */       case 6:
/* 1063 */         return "Nogump";
/*      */       case 11:
/* 1065 */         return "The Scavenger";
/*      */       case 12:
/* 1067 */         return "The Dirtmaw Giant";
/*      */       case 9:
/* 1069 */         return "Jackal";
/*      */     } 
/* 1071 */     String n = valreiNames.get(Integer.valueOf(deityId));
/* 1072 */     if (n == null)
/* 1073 */       n = ""; 
/* 1074 */     return n;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getDeityName(int deityId) {
/* 1080 */     Deity d = getDeity(deityId);
/* 1081 */     if (d != null) {
/* 1082 */       return d.getName();
/*      */     }
/* 1084 */     switch (deityId) {
/*      */       
/*      */       case 7:
/* 1087 */         return "Walnut";
/*      */       case 10:
/* 1089 */         return "The Deathcrawler";
/*      */       case 8:
/* 1091 */         return "Pharmakos";
/*      */       case 6:
/* 1093 */         return "Nogump";
/*      */       case 11:
/* 1095 */         return "The Scavenger";
/*      */       case 12:
/* 1097 */         return "The Dirtmaw Giant";
/*      */       case 9:
/* 1099 */         return "Jackal";
/*      */     } 
/* 1101 */     String n = valreiNames.get(Integer.valueOf(deityId));
/* 1102 */     if (n == null)
/* 1103 */       n = ""; 
/* 1104 */     return n;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getRandomStatusFor(int deityId) {
/* 1110 */     List<String> availStatuses = new ArrayList<>();
/* 1111 */     for (Map.Entry<String, Integer> status : valreiStatuses.entrySet()) {
/*      */       
/* 1113 */       if (((Integer)status.getValue()).intValue() == deityId)
/*      */       {
/* 1115 */         availStatuses.add(status.getKey());
/*      */       }
/*      */     } 
/* 1118 */     if (availStatuses.size() > 0) {
/*      */       
/* 1120 */       int num = Server.rand.nextInt(availStatuses.size());
/* 1121 */       return availStatuses.get(num);
/*      */     } 
/* 1123 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean hasValreiStatuses() {
/* 1128 */     return (valreiStatuses.size() > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getRandomStatus() {
/* 1133 */     if (valreiStatuses.size() > 0) {
/*      */       
/* 1135 */       int num = Server.rand.nextInt(valreiStatuses.size());
/*      */       
/* 1137 */       int x = 0;
/* 1138 */       for (Map.Entry<String, Integer> status : valreiStatuses.entrySet()) {
/*      */         
/* 1140 */         if (x >= num) {
/* 1141 */           return status.getKey();
/*      */         }
/* 1143 */         x++;
/*      */       } 
/*      */     } 
/* 1146 */     return "";
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\deities\Deities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
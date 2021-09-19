/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateCreator;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.statistics.ChallengePointEnum;
/*      */ import com.wurmonline.server.statistics.ChallengeSummary;
/*      */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public final class Achievements
/*      */   implements CounterTypes, MiscConstants, TimeConstants, CreatureTemplateIds
/*      */ {
/*   69 */   private static final Logger logger = Logger.getLogger(Achievements.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   74 */   private static final Map<Long, Achievements> achievements = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */   
/*   78 */   private final Map<Integer, Achievement> achievementsMap = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   83 */   private final Set<AchievementTemplate> personalGoalsSet = new HashSet<>();
/*      */ 
/*      */ 
/*      */   
/*   87 */   private static final Achievement[] emptyArray = new Achievement[0];
/*      */ 
/*      */   
/*      */   private static final String loadAllAchievements = "SELECT * FROM ACHIEVEMENTS";
/*      */ 
/*      */   
/*      */   private static final String deleteAllAchievementsForPlayer = "DELETE FROM ACHIEVEMENTS WHERE PLAYER=?";
/*      */ 
/*      */   
/*      */   private final long wurmId;
/*      */ 
/*      */ 
/*      */   
/*      */   public Achievements(long holderId) {
/*  101 */     this.wurmId = holderId;
/*      */   }
/*      */ 
/*      */   
/*      */   public Achievements(long holderId, boolean createGoals) {
/*  106 */     this(holderId);
/*      */     
/*  108 */     if (createGoals) {
/*  109 */       generatePersonalGoals(holderId);
/*      */     }
/*      */   }
/*      */   
/*      */   private final long getWurmId() {
/*  114 */     return this.wurmId;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean hasAchievement(long wurmId, int achievementId) {
/*  119 */     Achievements ach = getAchievementObject(wurmId);
/*  120 */     if (ach == null) {
/*  121 */       return false;
/*      */     }
/*  123 */     if (ach.getAchievement(achievementId) == null) {
/*  124 */       return false;
/*      */     }
/*  126 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Set<AchievementTemplate> getOldPersonalGoals(long wurmId) {
/*  131 */     Set<AchievementTemplate> initialSet = new HashSet<>();
/*      */ 
/*      */     
/*  134 */     initialSet.add(Achievement.getTemplate(141));
/*      */     
/*  136 */     initialSet.add(Achievement.getTemplate(237));
/*      */     
/*  138 */     initialSet.add(Achievement.getTemplate(171));
/*      */     
/*  140 */     initialSet.add(Achievement.getTemplate(70));
/*      */     
/*  142 */     initialSet.add(Achievement.getTemplate(57));
/*      */     
/*  144 */     Random rand = new Random(wurmId);
/*  145 */     while (initialSet.size() < 7) {
/*      */       
/*  147 */       AchievementTemplate originalAch = Achievement.getRandomPersonalDiamondAchievement(rand);
/*  148 */       initialSet.add(originalAch);
/*      */     } 
/*  150 */     while (initialSet.size() < 9) {
/*      */       
/*  152 */       AchievementTemplate originalAch = Achievement.getRandomPersonalGoldAchievement(rand);
/*  153 */       initialSet.add(originalAch);
/*      */     } 
/*  155 */     while (initialSet.size() < 20) {
/*      */       
/*  157 */       AchievementTemplate originalAch = Achievement.getRandomPersonalSilverAchievement(rand);
/*  158 */       initialSet.add(originalAch);
/*      */     } 
/*      */     
/*  161 */     return initialSet;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void generatePersonalGoals(long playerId) {
/*  166 */     if (!canStillWinTheGame()) {
/*      */       return;
/*      */     }
/*  169 */     Set<AchievementTemplate> initialSet = new HashSet<>();
/*  170 */     Set<AchievementTemplate> completedAch = new HashSet<>();
/*  171 */     for (Achievement t : getAchievements(playerId)) {
/*  172 */       completedAch.add(t.getTemplate());
/*      */     }
/*      */     
/*  175 */     initialSet.add(Achievement.getTemplate(141));
/*      */     
/*  177 */     initialSet.add(Achievement.getTemplate(237));
/*      */     
/*  179 */     initialSet.add(Achievement.getTemplate(171));
/*      */     
/*  181 */     initialSet.add(Achievement.getTemplate(70));
/*      */     
/*  183 */     initialSet.add(Achievement.getTemplate(57));
/*      */     
/*  185 */     Random rand = new Random(playerId);
/*  186 */     while (initialSet.size() < 7) {
/*      */       
/*  188 */       AchievementTemplate originalAch = Achievement.getRandomPersonalDiamondAchievement(rand);
/*  189 */       initialSet.add(originalAch);
/*      */     } 
/*  191 */     while (initialSet.size() < 9) {
/*      */       
/*  193 */       AchievementTemplate originalAch = Achievement.getRandomPersonalGoldAchievement(rand);
/*  194 */       initialSet.add(originalAch);
/*      */     } 
/*  196 */     while (initialSet.size() < 20) {
/*      */       
/*  198 */       AchievementTemplate originalAch = Achievement.getRandomPersonalSilverAchievement(rand);
/*  199 */       initialSet.add(originalAch);
/*      */     } 
/*      */     
/*  202 */     this.personalGoalsSet.clear();
/*  203 */     for (AchievementTemplate t : initialSet) {
/*      */       
/*  205 */       int count = 1;
/*  206 */       if (completedAch.contains(t)) {
/*      */         
/*  208 */         this.personalGoalsSet.add(t); continue;
/*      */       } 
/*  210 */       if (t.getNumber() >= 300 && t.getType() == 5) {
/*      */         
/*  212 */         AchievementTemplate newAch = Achievement.getRandomPersonalDiamondAchievement(new Random(playerId + count++));
/*  213 */         while (newAch.getNumber() >= 300 || initialSet.contains(newAch) || this.personalGoalsSet.contains(newAch)) {
/*  214 */           newAch = Achievement.getRandomPersonalDiamondAchievement(new Random(playerId + count++));
/*      */         }
/*  216 */         this.personalGoalsSet.add(newAch); continue;
/*      */       } 
/*  218 */       if (AchievementGenerator.isRerollablePersonalGoal(t.getNumber())) {
/*      */         
/*  220 */         if (t.getType() == 4) {
/*      */           
/*  222 */           AchievementTemplate newAch = Achievement.getRandomPersonalGoldAchievement(new Random(playerId + count++));
/*  223 */           while (AchievementGenerator.isRerollablePersonalGoal(newAch.getNumber()) || initialSet
/*  224 */             .contains(newAch) || this.personalGoalsSet.contains(newAch)) {
/*  225 */             newAch = Achievement.getRandomPersonalGoldAchievement(new Random(playerId + count++));
/*      */           }
/*  227 */           this.personalGoalsSet.add(newAch); continue;
/*      */         } 
/*  229 */         if (t.getType() == 3) {
/*      */           
/*  231 */           AchievementTemplate newAch = Achievement.getRandomPersonalSilverAchievement(new Random(playerId + count++));
/*  232 */           while (AchievementGenerator.isRerollablePersonalGoal(newAch.getNumber()) || initialSet
/*  233 */             .contains(newAch) || this.personalGoalsSet.contains(newAch)) {
/*  234 */             newAch = Achievement.getRandomPersonalSilverAchievement(new Random(playerId + count++));
/*      */           }
/*  236 */           this.personalGoalsSet.add(newAch);
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/*  241 */       this.personalGoalsSet.add(t);
/*      */     } 
/*      */ 
/*      */     
/*  245 */     AchievementTemplate toRemove = null;
/*  246 */     for (AchievementTemplate t : this.personalGoalsSet) {
/*      */       
/*  248 */       if (t.getNumber() == 298) {
/*      */         
/*  250 */         if (completedAch.contains(t))
/*      */           continue; 
/*  252 */         toRemove = t;
/*      */       } 
/*      */     } 
/*  255 */     if (toRemove != null) {
/*      */       
/*  257 */       this.personalGoalsSet.remove(toRemove);
/*  258 */       this.personalGoalsSet.add(Achievement.getTemplate(486));
/*      */     } 
/*      */     
/*  261 */     this.personalGoalsSet.add(Achievement.getTemplate(344));
/*      */   }
/*      */ 
/*      */   
/*      */   private final void generatePersonalUndeadGoals() {
/*  266 */     this.personalGoalsSet.clear();
/*  267 */     this.personalGoalsSet.add(Achievement.getTemplate(338));
/*  268 */     this.personalGoalsSet.add(Achievement.getTemplate(340));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<AchievementTemplate> getPersonalGoals() {
/*  273 */     return this.personalGoalsSet;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isPersonalGoal(AchievementTemplate template) {
/*  278 */     if (canStillWinTheGame()) {
/*  279 */       return this.personalGoalsSet.contains(template);
/*      */     }
/*  281 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasMetAllPersonalGoals() {
/*  286 */     if (!canStillWinTheGame()) {
/*  287 */       return false;
/*      */     }
/*  289 */     for (AchievementTemplate template : this.personalGoalsSet) {
/*      */       
/*  291 */       Achievement a = getAchievement(template.getNumber());
/*  292 */       if (a == null)
/*  293 */         return false; 
/*      */     } 
/*  295 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addAchievement(Achievement achievement, boolean createGoals) {
/*  300 */     Achievements personalAchieves = achievements.get(Long.valueOf(achievement.getHolder()));
/*  301 */     if (personalAchieves == null) {
/*      */       
/*  303 */       personalAchieves = new Achievements(achievement.getHolder(), createGoals);
/*  304 */       achievements.put(Long.valueOf(achievement.getHolder()), personalAchieves);
/*      */     } 
/*  306 */     personalAchieves.achievementsMap.put(Integer.valueOf(achievement.getAchievement()), achievement);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Achievement[] getAchievements(long creatureId) {
/*  311 */     Achievements personalSet = achievements.get(Long.valueOf(creatureId));
/*  312 */     if (personalSet == null || personalSet.achievementsMap.isEmpty())
/*  313 */       return emptyArray; 
/*  314 */     return (Achievement[])personalSet.achievementsMap.values().toArray(
/*  315 */         (Object[])new Achievement[personalSet.achievementsMap.values().size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Achievements getAchievementObject(long creatureId) {
/*  320 */     Achievements personalAchieves = achievements.get(Long.valueOf(creatureId));
/*  321 */     if (personalAchieves == null) {
/*      */       
/*  323 */       personalAchieves = new Achievements(creatureId, true);
/*  324 */       achievements.put(Long.valueOf(creatureId), personalAchieves);
/*      */     } 
/*  326 */     return personalAchieves;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Set<AchievementTemplate> getPersonalGoals(long creatureId, boolean isUndead) {
/*  331 */     Achievements personalAchieves = achievements.get(Long.valueOf(creatureId));
/*  332 */     if (personalAchieves == null) {
/*      */       
/*  334 */       personalAchieves = new Achievements(creatureId, true);
/*  335 */       achievements.put(Long.valueOf(creatureId), personalAchieves);
/*      */     } 
/*  337 */     if (isUndead) {
/*      */       
/*  339 */       if (personalAchieves.getPersonalGoals().size() > 2)
/*  340 */         personalAchieves.generatePersonalUndeadGoals(); 
/*  341 */       return personalAchieves.getPersonalGoals();
/*      */     } 
/*  343 */     return personalAchieves.getPersonalGoals();
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void awardKarma(AchievementTemplate template, Creature creature) {
/*  348 */     switch (template.getType()) {
/*      */       
/*      */       case 3:
/*  351 */         creature.setKarma(creature.getKarma() + 100);
/*  352 */         creature.getCommunicator().sendSafeServerMessage("You have received 100 karma for '" + template
/*  353 */             .getRequirement() + "'.");
/*      */         break;
/*      */       case 4:
/*  356 */         creature.setKarma(creature.getKarma() + 500);
/*  357 */         creature.getCommunicator().sendSafeServerMessage("You have received 500 karma for '" + template
/*  358 */             .getRequirement() + "'.");
/*      */         break;
/*      */       case 5:
/*  361 */         creature.setKarma(creature.getKarma() + 1000);
/*  362 */         creature.getCommunicator().sendSafeServerMessage("You have received 1000 karma for '" + template
/*  363 */             .getRequirement() + "'.");
/*      */         break;
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
/*      */   public Achievement getAchievement(int achievement) {
/*  378 */     return this.achievementsMap.get(Integer.valueOf(achievement));
/*      */   }
/*      */ 
/*      */   
/*      */   private final void setWinnerEffects(Creature p) {
/*  383 */     if (!canStillWinTheGame()) {
/*      */       return;
/*      */     }
/*  386 */     if (!p.hasFlag(6)) {
/*      */       
/*  388 */       p.setFlag(6, true);
/*  389 */       p.achievement(326);
/*      */ 
/*      */       
/*      */       try {
/*  393 */         int itemTemplateId = 795 + Server.rand.nextInt(16);
/*  394 */         if (Server.rand.nextInt(100) == 0)
/*  395 */           itemTemplateId = 465; 
/*  396 */         Item i = ItemFactory.createItem(itemTemplateId, (80 + Server.rand.nextInt(20)), "");
/*  397 */         if (i.getTemplateId() == 465)
/*      */         {
/*      */           
/*  400 */           i.setData1(CreatureTemplateCreator.getRandomDragonOrDrakeId());
/*      */         }
/*      */         
/*  403 */         p.getInventory().insertItem(i);
/*  404 */         p.addTitle(Titles.Title.Winner);
/*      */ 
/*      */         
/*  407 */         HistoryManager.addHistory(p.getName(), "has Won The Game and receives the " + i.getName() + "!");
/*      */       }
/*  409 */       catch (Exception nsi) {
/*      */         
/*  411 */         logger.log(Level.WARNING, p.getName() + " " + nsi.getMessage(), nsi);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void setWinnerEffectsOffline(PlayerInfo pInf) {
/*  418 */     if (!pInf.isFlagSet(6)) {
/*      */       
/*  420 */       pInf.setFlag(6, true);
/*  421 */       triggerAchievement(pInf.wurmId, 326);
/*      */ 
/*      */       
/*      */       try {
/*  425 */         int itemTemplateId = 795 + Server.rand.nextInt(16);
/*  426 */         if (Server.rand.nextInt(100) == 0)
/*  427 */           itemTemplateId = 465; 
/*  428 */         Item i = ItemFactory.createItem(itemTemplateId, (80 + Server.rand.nextInt(20)), "");
/*  429 */         if (i.getTemplateId() == 465)
/*      */         {
/*      */           
/*  432 */           i.setData1(CreatureTemplateCreator.getRandomDragonOrDrakeId());
/*      */         }
/*      */         
/*  435 */         long inventory = DbCreatureStatus.getInventoryIdFor(pInf.wurmId);
/*  436 */         i.setParentId(inventory, true);
/*  437 */         i.setOwnerId(pInf.wurmId);
/*      */         
/*  439 */         pInf.addTitle(Titles.Title.Winner);
/*      */         
/*  441 */         HistoryManager.addHistory(pInf.getName(), "has Won The Game and receives the " + i.getName() + "!");
/*      */       }
/*  443 */       catch (Exception nsi) {
/*      */         
/*  445 */         logger.log(Level.WARNING, pInf.getName() + " " + nsi.getMessage(), nsi);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void triggerAchievement(long creatureId, int achievementId, int counterModifier) {
/*  452 */     if (WurmId.getType(creatureId) != 0) {
/*      */       return;
/*      */     }
/*      */     
/*  456 */     Achievements personalAchieves = achievements.get(Long.valueOf(creatureId));
/*  457 */     if (personalAchieves == null) {
/*      */       
/*  459 */       personalAchieves = new Achievements(creatureId, true);
/*  460 */       achievements.put(Long.valueOf(creatureId), personalAchieves);
/*      */     } 
/*  462 */     Achievement achieved = personalAchieves.getAchievement(achievementId);
/*  463 */     if (achieved == null) {
/*      */ 
/*      */       
/*  466 */       achieved = new Achievement(achievementId, new Timestamp(System.currentTimeMillis()), creatureId, counterModifier, -1);
/*      */       
/*  468 */       PlayerJournal.achievementTriggered(creatureId, achievementId);
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
/*  481 */       achieved.create(false);
/*  482 */       if (!achieved.isInVisible()) {
/*      */         
/*      */         try {
/*      */           
/*  486 */           Player p = Players.getInstance().getPlayer(creatureId);
/*  487 */           achieved.sendNewAchievement(p);
/*  488 */           if (achievementId == 369)
/*  489 */             p.addTitle(Titles.Title.Knigt); 
/*  490 */           if (achievementId == 367) {
/*      */             
/*  492 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(creatureId);
/*  493 */             if (pinf != null) {
/*      */               
/*  495 */               ChallengeSummary.addToScore(pinf, ChallengePointEnum.ChallengePoint.TREASURE_CHESTS.getEnumtype(), 1.0F);
/*  496 */               ChallengeSummary.addToScore(pinf, ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), 5.0F);
/*      */             } 
/*      */           } 
/*  499 */           if (personalAchieves.isPersonalGoal(achieved.getTemplate()))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  510 */             achieved.sendUpdatePersonalGoal(p);
/*  511 */             awardKarma(achieved.getTemplate(), p);
/*      */             
/*  513 */             if (canStillWinTheGame())
/*      */             {
/*  515 */               if (personalAchieves.hasMetAllPersonalGoals())
/*      */               {
/*  517 */                 personalAchieves.setWinnerEffects(p);
/*      */               }
/*      */             }
/*      */           }
/*      */         
/*  522 */         } catch (NoSuchPlayerException nsc) {
/*      */           
/*  524 */           PlayerInfo pInf = PlayerInfoFactory.getPlayerInfoWithWurmId(creatureId);
/*  525 */           if (pInf != null) {
/*      */             
/*  527 */             if (achievementId == 369)
/*  528 */               pInf.addTitle(Titles.Title.Knigt); 
/*  529 */             if (achievementId == 367) {
/*      */               
/*  531 */               ChallengeSummary.addToScore(pInf, ChallengePointEnum.ChallengePoint.TREASURE_CHESTS.getEnumtype(), 1.0F);
/*  532 */               ChallengeSummary.addToScore(pInf, ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), 5.0F);
/*      */             } 
/*  534 */             if (personalAchieves.isPersonalGoal(achieved.getTemplate())) {
/*      */               
/*  536 */               switch (achieved.getTemplate().getType()) {
/*      */                 
/*      */                 case 3:
/*  539 */                   pInf.setKarma(pInf.getKarma() + 100);
/*      */                   break;
/*      */                 case 4:
/*  542 */                   pInf.setKarma(pInf.getKarma() + 500);
/*      */                   break;
/*      */                 case 5:
/*  545 */                   pInf.setKarma(pInf.getKarma() + 1000);
/*      */                   break;
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  551 */               if (canStillWinTheGame())
/*      */               {
/*  553 */                 if (personalAchieves.hasMetAllPersonalGoals())
/*      */                 {
/*  555 */                   personalAchieves.setWinnerEffectsOffline(pInf);
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*  562 */       triggerAchievements(creatureId, achieved, achievementId, personalAchieves, achieved.getTriggeredAchievements());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  570 */     else if (!achieved.isOneTimer()) {
/*      */       
/*  572 */       int[] triggered = achieved.setCounter(achieved.getCounter() + counterModifier);
/*  573 */       if (!achieved.isInVisible()) {
/*      */         
/*      */         try {
/*      */           
/*  577 */           Player p = Players.getInstance().getPlayer(creatureId);
/*  578 */           achieved.sendUpdateAchievement(p);
/*  579 */           if (achievementId == 369)
/*  580 */             p.addTitle(Titles.Title.Knigt); 
/*  581 */           if (achievementId == 367) {
/*      */             
/*  583 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(creatureId);
/*  584 */             if (pinf != null)
/*      */             {
/*  586 */               ChallengeSummary.addToScore(pinf, ChallengePointEnum.ChallengePoint.TREASURE_CHESTS.getEnumtype(), 1.0F);
/*  587 */               ChallengeSummary.addToScore(pinf, ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), 5.0F);
/*      */             }
/*      */           
/*      */           } 
/*  591 */         } catch (NoSuchPlayerException nsc) {
/*      */           
/*  593 */           PlayerInfo pInf = PlayerInfoFactory.getPlayerInfoWithWurmId(creatureId);
/*  594 */           if (pInf != null) {
/*      */             
/*  596 */             if (achievementId == 369)
/*  597 */               pInf.addTitle(Titles.Title.Knigt); 
/*  598 */             if (achievementId == 367) {
/*      */               
/*  600 */               ChallengeSummary.addToScore(pInf, ChallengePointEnum.ChallengePoint.TREASURE_CHESTS.getEnumtype(), 1.0F);
/*  601 */               ChallengeSummary.addToScore(pInf, ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), 5.0F);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*  606 */       triggerAchievements(creatureId, achieved, achievementId, personalAchieves, triggered);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void triggerAchievement(long creatureId, int achievementId) {
/*  613 */     if (WurmId.getType(creatureId) != 0) {
/*      */       return;
/*      */     }
/*      */     
/*  617 */     triggerAchievement(creatureId, achievementId, 1);
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
/*      */   public static void sendAchievementList(Creature creature) {
/*  720 */     Achievement[] lAchievements = getAchievements(creature.getWurmId());
/*  721 */     creature.getCommunicator().sendAchievementList(lAchievements);
/*  722 */     sendPersonalGoalsList(creature);
/*      */     
/*  724 */     if (creature.isPlayer()) {
/*      */       
/*  726 */       PlayerTutorial.sendTutorialList((Player)creature);
/*  727 */       PlayerJournal.sendPersonalJournal((Player)creature);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void awardPremiumAchievements(Creature creature, int totalMonths) {
/*  733 */     ArrayList<Integer> achievementsList = new ArrayList<>();
/*  734 */     for (Achievement a : getAchievements(creature.getWurmId())) {
/*  735 */       achievementsList.add(Integer.valueOf(a.getAchievement()));
/*      */     }
/*  737 */     if (totalMonths >= 1 && !achievementsList.contains(Integer.valueOf(343)))
/*  738 */       creature.achievement(343); 
/*  739 */     if (totalMonths >= 3 && !achievementsList.contains(Integer.valueOf(344)))
/*  740 */       creature.achievement(344); 
/*  741 */     if (totalMonths >= 6 && !achievementsList.contains(Integer.valueOf(345)))
/*  742 */       creature.achievement(345); 
/*  743 */     if (totalMonths >= 9 && !achievementsList.contains(Integer.valueOf(346)))
/*  744 */       creature.achievement(346); 
/*  745 */     if (totalMonths >= 13 && !achievementsList.contains(Integer.valueOf(347)))
/*  746 */       creature.achievement(347); 
/*  747 */     if (totalMonths >= 16 && !achievementsList.contains(Integer.valueOf(348)))
/*  748 */       creature.achievement(348); 
/*  749 */     if (totalMonths >= 20 && !achievementsList.contains(Integer.valueOf(349)))
/*  750 */       creature.achievement(349); 
/*  751 */     if (totalMonths >= 26 && !achievementsList.contains(Integer.valueOf(350)))
/*  752 */       creature.achievement(350); 
/*  753 */     if (totalMonths >= 36 && !achievementsList.contains(Integer.valueOf(351)))
/*  754 */       creature.achievement(351); 
/*  755 */     if (totalMonths >= 48 && !achievementsList.contains(Integer.valueOf(352)))
/*  756 */       creature.achievement(352); 
/*  757 */     if (totalMonths >= 60 && !achievementsList.contains(Integer.valueOf(353)))
/*  758 */       creature.achievement(353); 
/*  759 */     if (totalMonths >= 80 && !achievementsList.contains(Integer.valueOf(354)))
/*  760 */       creature.achievement(354); 
/*  761 */     if (totalMonths >= 120 && !achievementsList.contains(Integer.valueOf(355))) {
/*  762 */       creature.achievement(355);
/*      */     }
/*  764 */     creature.setFlag(61, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sendPersonalGoalsList(Creature creature) {
/*  770 */     if (!canStillWinTheGame()) {
/*      */       return;
/*      */     }
/*  773 */     Achievements pachievements = getAchievementObject(creature.getWurmId());
/*      */     
/*  775 */     pachievements.generatePersonalGoals(creature.getWurmId());
/*      */     
/*  777 */     Set<AchievementTemplate> pset = getPersonalGoals(creature.getWurmId(), creature.isUndead());
/*  778 */     Map<AchievementTemplate, Boolean> goals = new ConcurrentHashMap<>();
/*  779 */     boolean awardTut = false;
/*  780 */     for (AchievementTemplate template : pset) {
/*      */       
/*  782 */       Achievement a = pachievements.getAchievement(template.getNumber());
/*  783 */       goals.put(template, Boolean.valueOf((a != null)));
/*  784 */       if (a != null) {
/*      */         
/*  786 */         if (!creature.hasFlag(5))
/*      */         {
/*  788 */           awardKarma(template, creature); } 
/*      */         continue;
/*      */       } 
/*  791 */       if (template.getNumber() == 141)
/*      */       {
/*  793 */         if (!Servers.localServer.LOGINSERVER)
/*  794 */           awardTut = true; 
/*      */       }
/*      */     } 
/*  797 */     if (awardTut) {
/*  798 */       creature.achievement(141);
/*      */     }
/*      */ 
/*      */     
/*  802 */     if (creature.isPlayer() && !creature.hasFlag(61)) {
/*      */       
/*  804 */       PlayerInfo player = PlayerInfoFactory.getPlayerInfoWithWurmId(creature.getWurmId());
/*  805 */       if (player != null && player.awards != null) {
/*  806 */         awardPremiumAchievements(creature, player.awards.getMonthsPaidSinceReset());
/*      */       }
/*      */     } 
/*  809 */     if (!creature.hasFlag(6) && pachievements.hasMetAllPersonalGoals())
/*      */     {
/*  811 */       if (canStillWinTheGame())
/*      */       {
/*  813 */         pachievements.setWinnerEffects(creature);
/*      */       }
/*      */     }
/*  816 */     if (!creature.hasFlag(5))
/*      */     {
/*  818 */       creature.setFlag(5, true);
/*      */     }
/*  820 */     creature.getCommunicator().sendPersonalGoalsList(goals);
/*  821 */     if (creature.getPlayingTime() > 7200000L && creature.getPlayingTime() < 21600000L) {
/*  822 */       creature.getCommunicator().sendShowPersonalGoalWindow(true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void triggerAchievements(long creatureId, Achievement achieved, int achievementId, Achievements personalAchieves, int[] triggered) {
/*  829 */     for (int number : triggered) {
/*      */ 
/*      */ 
/*      */       
/*  833 */       if (number != achievementId) {
/*      */ 
/*      */ 
/*      */         
/*  837 */         Achievement old = personalAchieves.getAchievement(number);
/*  838 */         if (old != null) {
/*      */           
/*  840 */           if (!old.isOneTimer())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  851 */             int required = old.getTriggerOnCounter() * (old.getCounter() + 1);
/*  852 */             if (achieved.getCounter() >= required)
/*      */             {
/*  854 */               int numTimes = achieved.getCounter() / old.getTriggerOnCounter() - old.getCounter();
/*      */               
/*  856 */               if (numTimes > 0) {
/*  857 */                 triggerAchievement(creatureId, old.getAchievement(), numTimes);
/*      */               
/*      */               }
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  867 */           AchievementTemplate template = Achievement.getTemplate(number);
/*  868 */           if (template.getTriggerOnCounter() == 1)
/*      */           {
/*  870 */             if ((template.getRequiredAchievements()).length <= 1)
/*      */             {
/*  872 */               logger.log(Level.WARNING, "Achievement " + number + " has trigger on 1. Usually not good unless it's a meta achievement since it means the triggering achievement immediately gives another achievement.");
/*      */             }
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  883 */           if ((template.getTriggerOnCounter() > 0 && achieved.getCounter() >= template.getTriggerOnCounter()) || (template
/*  884 */             .getTriggerOnCounter() < 0 && achieved.getCounter() <= template
/*  885 */             .getTriggerOnCounter())) {
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
/*  896 */             boolean trigger = true;
/*  897 */             int[] required = template.getRequiredAchievements();
/*  898 */             for (int req : required) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  905 */               if (req != achievementId) {
/*      */                 
/*  907 */                 Achievement existingReq = personalAchieves.getAchievement(req);
/*  908 */                 if (existingReq == null || existingReq.getCounter() < template.getTriggerOnCounter())
/*      */                 {
/*      */ 
/*      */ 
/*      */                   
/*  913 */                   trigger = false;
/*      */                 }
/*      */               } 
/*      */             } 
/*  917 */             int numTimes = achieved.getCounter() / template.getTriggerOnCounter();
/*      */             
/*  919 */             if (trigger && numTimes > 0)
/*      */             {
/*  921 */               triggerAchievement(creatureId, template.getNumber(), numTimes);
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/*  926 */         PlayerJournal.subAchievementCounterTick(creatureId, number);
/*      */       } else {
/*      */         
/*  929 */         logger.log(Level.WARNING, "Achievement " + achievementId + " has itself as trigger: " + number);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void loadAllAchievements() throws IOException {
/*  935 */     long start = System.nanoTime();
/*  936 */     int loadedAchievements = 0;
/*  937 */     Connection dbcon = null;
/*  938 */     PreparedStatement ps = null;
/*  939 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  942 */       dbcon = DbConnector.getPlayerDbCon();
/*  943 */       ps = dbcon.prepareStatement("SELECT * FROM ACHIEVEMENTS");
/*  944 */       rs = ps.executeQuery();
/*  945 */       while (rs.next())
/*      */       {
/*  947 */         Timestamp st = new Timestamp(System.currentTimeMillis());
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  952 */           st = new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(rs.getString("ADATE")).getTime());
/*      */         }
/*  954 */         catch (Exception ex) {
/*      */           
/*  956 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */         } 
/*  958 */         addAchievement(new Achievement(rs.getInt("ACHIEVEMENT"), st, rs.getLong("PLAYER"), rs
/*  959 */               .getInt("COUNTER"), rs.getInt("ID")), false);
/*  960 */         loadedAchievements++;
/*      */       }
/*      */     
/*  963 */     } catch (SQLException sqex) {
/*      */       
/*  965 */       logger.log(Level.WARNING, "Failed to load achievements due to " + sqex.getMessage(), sqex);
/*  966 */       throw new IOException("Failed to load achievements", sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  970 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  971 */       DbConnector.returnConnection(dbcon);
/*  972 */       long end = System.nanoTime();
/*  973 */       logger.info("Loaded " + loadedAchievements + " achievements from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */     } 
/*      */ 
/*      */     
/*  977 */     if (canStillWinTheGame()) {
/*  978 */       generateAllPersonalGoals();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void generateAllPersonalGoals() {
/*  983 */     long start = System.nanoTime();
/*  984 */     int count = 0;
/*  985 */     for (Achievements a : achievements.values()) {
/*      */       
/*  987 */       a.generatePersonalGoals(a.getWurmId());
/*  988 */       count++;
/*      */     } 
/*  990 */     long end = System.nanoTime();
/*  991 */     logger.info("Generated " + count + " personal goals, took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void deleteAllAchievements(long playerId) throws IOException {
/*  996 */     Connection dbcon = null;
/*  997 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1000 */       dbcon = DbConnector.getPlayerDbCon();
/* 1001 */       ps = dbcon.prepareStatement("DELETE FROM ACHIEVEMENTS WHERE PLAYER=?");
/* 1002 */       ps.setLong(1, playerId);
/* 1003 */       ps.executeUpdate();
/*      */     }
/* 1005 */     catch (SQLException sqex) {
/*      */       
/* 1007 */       logger.log(Level.WARNING, "Failed to delete achievements for " + playerId + ' ' + sqex.getMessage(), sqex);
/* 1008 */       throw new IOException("Failed to delete achievements for " + playerId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1012 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1013 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean canStillWinTheGame() {
/* 1020 */     return WurmCalendar.nowIsBefore(0, 1, 1, 1, 2019);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Achievements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerJournal
/*     */   implements AchievementList
/*     */ {
/*  26 */   protected static final Logger logger = Logger.getLogger(PlayerJournal.class.getName());
/*     */   
/*     */   public static final byte JOUR_TIER_TUT = 0;
/*     */   
/*     */   public static final byte JOUR_TIER_BASIC1 = 1;
/*     */   
/*     */   public static final byte JOUR_TIER_BASIC2 = 2;
/*     */   
/*     */   public static final byte JOUR_TIER_BASIC3 = 3;
/*     */   
/*     */   public static final byte JOUR_TIER_INTERMEDIATE1 = 4;
/*     */   
/*     */   public static final byte JOUR_TIER_INTERMEDIATE2 = 5;
/*     */   
/*     */   public static final byte JOUR_TIER_INTERMEDIATE3 = 6;
/*     */   public static final byte JOUR_TIER_EXPERT1 = 7;
/*     */   public static final byte JOUR_TIER_EXPERT2 = 8;
/*     */   public static final byte JOUR_TIER_EXPERT3 = 9;
/*     */   public static final byte JOUR_TIER_PRIEST1 = 10;
/*     */   public static final byte JOUR_TIER_PRIEST2 = 11;
/*     */   public static final byte JOUR_TIER_PRIEST3 = 12;
/*  47 */   private static final HashMap<Byte, JournalTier> allTiers = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  54 */     JournalTier tutorialTier = new JournalTier((byte)0, "First Steps", (byte)-1, (byte)1, 6, 64, new int[] { 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, Features.Feature.HIGHWAYS.isEnabled() ? 524 : 576 });
/*  55 */     tutorialTier.setReward(new JournalReward("Apprentice Title & 1hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/*  60 */             p.addTitle(Titles.Title.Journal_T0);
/*  61 */             p.getSaveFile().addToSleep(3600);
/*     */           }
/*     */         });
/*     */     
/*  65 */     JournalTier basicTier1 = new JournalTier((byte)1, "Finding the Path", (byte)0, (byte)2, 5, 65, new int[] { 525, 526, 529, 530, 531, 532, 533, 534, 535, 536, 537 });
/*     */ 
/*     */     
/*  68 */     basicTier1.setReward(new JournalReward("Learned Title & 1hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/*  73 */             p.addTitle(Titles.Title.Journal_T1);
/*  74 */             p.getSaveFile().addToSleep(3600);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  79 */     JournalTier basicTier2 = new JournalTier((byte)2, "Gathering Stride", (byte)1, (byte)3, 7, 66, new int[] { 538, 539, 540, 541, 542, 231, 543, 544, 545, 546, 301, 547, 548, 549 });
/*     */ 
/*     */ 
/*     */     
/*  83 */     basicTier2.setReward(new JournalReward("Experienced Title & 2hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/*  88 */             p.addTitle(Titles.Title.Journal_T2);
/*  89 */             p.getSaveFile().addToSleep(7200);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  94 */     JournalTier basicTier3 = new JournalTier((byte)3, "Paved with Stone", (byte)2, (byte)4, 7, 67, new int[] { 550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563 });
/*     */ 
/*     */     
/*  97 */     basicTier3.setReward(new JournalReward("Skilled Title & 2hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/* 102 */             p.addTitle(Titles.Title.Journal_T3);
/* 103 */             p.getSaveFile().addToSleep(7200);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 108 */     JournalTier intermediateTier1 = new JournalTier((byte)4, "On the Highway", (byte)3, (byte)5, 6, 68, new int[] { 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575 });
/*     */ 
/*     */     
/* 111 */     intermediateTier1.setReward(new JournalReward("Accomplished Title, Glimmersteel or Adamantine Lump & 3hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/* 116 */             p.addTitle(Titles.Title.Journal_T4);
/* 117 */             p.getSaveFile().addToSleep(10800);
/*     */             
/*     */             try {
/* 120 */               int templateId = 698;
/* 121 */               if (Server.rand.nextBoolean())
/* 122 */                 templateId = 694; 
/* 123 */               Item lump = ItemFactory.createItem(templateId, Server.rand.nextFloat() * 50.0F + 40.0F, p.getName());
/* 124 */               p.getInventory().insertItem(lump, true);
/*     */             }
/* 126 */             catch (FailedException|com.wurmonline.server.items.NoSuchTemplateException failedException) {}
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     JournalTier intermediateTier2 = new JournalTier((byte)5, "Picking up Speed", (byte)4, (byte)6, 6, 69, new int[] { 586, 588, 578, 581, 580, 579, 584, 583, 587, 582, 585, 577 });
/*     */ 
/*     */     
/* 137 */     intermediateTier2.setReward(new JournalReward("Proficient Title, Choice of Affinity & 3hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/* 142 */             p.addTitle(Titles.Title.Journal_T5);
/* 143 */             p.getSaveFile().addToSleep(10800);
/*     */             
/*     */             try {
/* 146 */               Item token = ItemFactory.createItem(1438, 80.0F, p.getName());
/* 147 */               p.getInventory().insertItem(token, true);
/*     */             }
/* 149 */             catch (FailedException|com.wurmonline.server.items.NoSuchTemplateException failedException) {}
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     JournalTier intermediateTier3 = new JournalTier((byte)6, "The Winding Road", (byte)5, (byte)7, 6, 70, new int[] { 590, 591, 594, 589, 592, 596, 597, 600, 599, 595, 593, 598 });
/*     */ 
/*     */     
/* 160 */     intermediateTier3.setReward(new JournalReward("Talented Title, Increased Max Sleep Bonus & 6hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/* 165 */             p.addTitle(Titles.Title.Journal_T6);
/* 166 */             p.setFlag(77, true);
/* 167 */             p.getSaveFile().addToSleep(21600);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 172 */     JournalTier priestTier1 = new JournalTier((byte)10, "Dedication", (byte)-1, (byte)11, 6, 78, new int[] { 604, 606, 609, 608, 614, 615, 605, 607, 612, 611, 610, 613 })
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean isVisible(long playerId)
/*     */         {
/* 179 */           Optional<PlayerInfo> playerInfo = PlayerInfoFactory.getPlayerInfoOptional(playerId);
/* 180 */           return (playerInfo.isPresent() && ((PlayerInfo)playerInfo.get()).isPriest);
/*     */         }
/*     */       };
/* 183 */     priestTier1.setReward(new JournalReward("Blessed Title, 2h Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/* 188 */             p.addTitle(Titles.Title.Journal_P1);
/* 189 */             p.getSaveFile().addToSleep(7200);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 194 */     JournalTier priestTier2 = new JournalTier((byte)11, "Approbation", (byte)10, (byte)12, 6, 79, new int[] { 617, 619, 627, 616, 625, 623, 622, 621, 626, 618, 624, 620 });
/*     */ 
/*     */     
/* 197 */     priestTier2.setReward(new JournalReward("Angelic Title, Increased Max Faith Gains Per Day & 3hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/* 202 */             p.addTitle(Titles.Title.Journal_P2);
/* 203 */             p.setFlag(81, true);
/* 204 */             p.getSaveFile().addToSleep(10800);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 209 */     JournalTier priestTier3 = new JournalTier((byte)12, "Benediction", (byte)11, (byte)-1, 5, 80, new int[] { 628, 630, 635, 634, 636, 632, 633, 631, 637, 629 });
/*     */ 
/*     */     
/* 212 */     priestTier3.setReward(new JournalReward("Divine Title, +5 Power to Spell Casts & 5hr Sleep Bonus")
/*     */         {
/*     */           
/*     */           public void runReward(Player p)
/*     */           {
/* 217 */             p.addTitle(Titles.Title.Journal_P3);
/* 218 */             p.setFlag(82, true);
/* 219 */             p.getSaveFile().addToSleep(18000);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 224 */     allTiers.put(Byte.valueOf((byte)0), tutorialTier);
/* 225 */     allTiers.put(Byte.valueOf((byte)1), basicTier1);
/* 226 */     allTiers.put(Byte.valueOf((byte)2), basicTier2);
/* 227 */     allTiers.put(Byte.valueOf((byte)3), basicTier3);
/* 228 */     allTiers.put(Byte.valueOf((byte)4), intermediateTier1);
/* 229 */     allTiers.put(Byte.valueOf((byte)5), intermediateTier2);
/* 230 */     allTiers.put(Byte.valueOf((byte)6), intermediateTier3);
/*     */     
/* 232 */     allTiers.put(Byte.valueOf((byte)10), priestTier1);
/* 233 */     allTiers.put(Byte.valueOf((byte)11), priestTier2);
/* 234 */     allTiers.put(Byte.valueOf((byte)12), priestTier3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static HashMap<Byte, JournalTier> getAllTiers() {
/* 239 */     return allTiers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void achievementTriggered(long playerId, int achievementId) {
/* 245 */     Optional<JournalTier> theTier = allTiers.values().stream().filter(t -> t.containsAchievement(achievementId)).findFirst();
/*     */     
/* 247 */     if (theTier.isPresent() && ((JournalTier)theTier.get()).isVisible(playerId)) {
/*     */ 
/*     */       
/* 250 */       Optional<Player> p = Players.getInstance().getPlayerOptional(playerId);
/* 251 */       p.ifPresent(ply -> {
/*     */             ply.getCommunicator().sendPersonalJournalAchvUpdate(((JournalTier)theTier.get()).getTierId(), achievementId, true);
/*     */             
/*     */             if (((JournalTier)theTier.get()).shouldUnlockNextTier(playerId)) {
/*     */               JournalTier nextTier = ((JournalTier)theTier.get()).getNextTier();
/*     */               
/*     */               sendTierUnlock(ply, nextTier);
/*     */             } 
/*     */           });
/* 260 */       if (((JournalTier)theTier.get()).shouldUnlockReward(playerId)) {
/*     */         
/* 262 */         ((JournalTier)theTier.get()).awardReward(playerId);
/* 263 */         p.ifPresent(ply -> ply.getCommunicator().sendPersonalJournalTierUpdate(((JournalTier)theTier.get()).getTierId(), true, ((JournalTier)theTier.get()).getRewardString()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void subAchievementCounterTick(long playerId, int subAchievementId) {
/* 271 */     Optional<JournalTier> subAchTier = allTiers.values().stream().filter(t -> t.containsAchievement(subAchievementId)).findFirst();
/*     */     
/* 273 */     if (subAchTier.isPresent() && ((JournalTier)subAchTier.get()).isVisible(playerId)) {
/*     */       
/* 275 */       Optional<Player> p = Players.getInstance().getPlayerOptional(playerId);
/* 276 */       p.ifPresent(ply -> ply.getCommunicator().sendPersonalJournalAchvUpdate(((JournalTier)subAchTier.get()).getTierId(), subAchievementId, (Achievements.getAchievementObject(playerId).getAchievement(subAchievementId) != null)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendTierUnlock(@Nullable Player p, @Nullable JournalTier toUnlock) {
/* 285 */     if (p == null || toUnlock == null) {
/*     */       return;
/*     */     }
/* 288 */     Achievements ach = Achievements.getAchievementObject(p.getWurmId());
/* 289 */     int[] achievementIds = toUnlock.getAchievementList().stream().mapToInt(i -> i.intValue()).toArray();
/* 290 */     boolean[] achievementsCompleted = new boolean[achievementIds.length];
/* 291 */     for (int i = 0; i < achievementIds.length; i++) {
/*     */       
/* 293 */       if (ach != null) {
/* 294 */         achievementsCompleted[i] = (ach.getAchievement(achievementIds[i]) != null);
/*     */       } else {
/* 296 */         achievementsCompleted[i] = false;
/*     */       } 
/*     */     } 
/* 299 */     p.getCommunicator().sendSafeServerMessage("Congratulations, you have now unlocked " + toUnlock.getTierName() + " in your journal.", (byte)2);
/* 300 */     p.getCommunicator().sendPersonalJournalTier(toUnlock.getTierId(), toUnlock.getTierName(), toUnlock.getRewardString(), (toUnlock
/* 301 */         .isRewardUnlocked(p.getWurmId()) || toUnlock.hasBeenAwarded(p.getWurmId())), achievementIds, achievementsCompleted);
/*     */     
/* 303 */     if (toUnlock.isNextTierUnlocked(p.getWurmId())) {
/*     */       
/* 305 */       if (toUnlock.isRewardUnlocked(p.getWurmId()) && !toUnlock.hasBeenAwarded(p.getWurmId())) {
/* 306 */         toUnlock.awardReward(p.getWurmId());
/*     */       }
/* 308 */       sendTierUnlock(p, toUnlock.getNextTier());
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
/*     */   public static void sendPersonalJournal(@Nullable Player plr) {
/* 320 */     if (plr == null)
/*     */       return; 
/* 322 */     for (JournalTier tier : allTiers.values()) {
/*     */       
/* 324 */       if (!tier.isVisible(plr.getWurmId())) {
/*     */         continue;
/*     */       }
/* 327 */       if (tier.isRewardUnlocked(plr.getWurmId()) && !tier.hasBeenAwarded(plr.getWurmId())) {
/* 328 */         tier.awardReward(plr.getWurmId());
/*     */       }
/* 330 */       Achievements ach = Achievements.getAchievementObject(plr.getWurmId());
/* 331 */       int[] achievementIds = tier.getAchievementList().stream().mapToInt(i -> i.intValue()).toArray();
/* 332 */       boolean[] achievementsCompleted = new boolean[achievementIds.length];
/* 333 */       for (int i = 0; i < achievementIds.length; i++) {
/*     */         
/* 335 */         if (ach != null) {
/* 336 */           achievementsCompleted[i] = (ach.getAchievement(achievementIds[i]) != null);
/*     */         } else {
/* 338 */           achievementsCompleted[i] = false;
/*     */         } 
/*     */       } 
/* 341 */       plr.getCommunicator().sendPersonalJournalTier(tier.getTierId(), tier.getTierName(), tier.getRewardString(), (tier
/* 342 */           .isRewardUnlocked(plr.getWurmId()) || tier.hasBeenAwarded(plr.getWurmId())), achievementIds, achievementsCompleted);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sendJournalInfoBML(@Nullable Player p, long targetId) {
/* 348 */     if (p == null) {
/*     */       return;
/*     */     }
/* 351 */     if (targetId == -10L) {
/*     */       return;
/*     */     }
/* 354 */     BMLBuilder toSend = BMLBuilder.createVertArrayNode(false);
/* 355 */     Achievements ach = Achievements.getAchievementObject(targetId);
/* 356 */     for (JournalTier tier : allTiers.values()) {
/*     */       
/* 358 */       int[] achievementIds = tier.getAchievementList().stream().mapToInt(i -> i.intValue()).toArray();
/* 359 */       boolean[] achievementsCompleted = new boolean[achievementIds.length]; int i;
/* 360 */       for (i = 0; i < achievementIds.length; i++) {
/*     */         
/* 362 */         if (ach != null) {
/* 363 */           achievementsCompleted[i] = (ach.getAchievement(achievementIds[i]) != null);
/*     */         } else {
/* 365 */           achievementsCompleted[i] = false;
/*     */         } 
/*     */       } 
/* 368 */       toSend.addLabel(tier.getTierName(), tier.getRewardString(), BMLBuilder.TextType.BOLD, tier.isRewardUnlocked(targetId) ? (
/* 369 */           tier.hasBeenAwarded(targetId) ? Color.GREEN : Color.YELLOW) : (tier.isVisible(targetId) ? Color.LIGHT_GRAY : Color.GRAY));
/* 370 */       for (i = 0; i < achievementIds.length; i++) {
/*     */         
/* 372 */         AchievementTemplate t = Achievement.getTemplate(achievementIds[i]);
/* 373 */         if (t == null) {
/*     */           
/* 375 */           logger.warning("AchievementTemplate for ID# " + achievementIds[i] + " is null");
/*     */         } else {
/*     */           
/* 378 */           toSend.addLabel("  - " + t.getName(), achievementsCompleted[i] ? t.getDescription() : t.getRequirement(), null, achievementsCompleted[i] ? Color.GREEN : (
/* 379 */               tier.isVisible(targetId) ? Color.LIGHT_GRAY : Color.GRAY));
/*     */         } 
/*     */       } 
/*     */     } 
/* 383 */     toSend.addText("\r\n\r\nKey:", null, BMLBuilder.TextType.BOLD, Color.WHITE);
/* 384 */     toSend.addText("Not visible to player", null, null, Color.GRAY);
/* 385 */     toSend.addText("Visible to player, incomplete", null, null, Color.LIGHT_GRAY);
/* 386 */     toSend.addText("Completed", null, null, Color.GREEN);
/* 387 */     toSend.addText("Completed but unrewarded (should reward on login)", null, null, Color.YELLOW, 300, 40);
/*     */     
/* 389 */     p.getCommunicator().sendBml(300, 500, true, true, BMLBuilder.createBMLBorderPanel(null, null, 
/* 390 */           BMLBuilder.createScrollPanelNode(true, false).addString(toSend.toString()), null, null).toString(), 200, 200, 200, "Journal Info: " + 
/* 391 */         PlayerInfoFactory.getPlayerName(targetId));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerJournal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
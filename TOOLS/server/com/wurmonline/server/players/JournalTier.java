/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JournalTier
/*     */ {
/*     */   private final ArrayList<Integer> achievementList;
/*     */   private final byte tierId;
/*     */   private final String tierName;
/*     */   private final byte lastTierId;
/*     */   private final byte nextTierId;
/*     */   private final int unlockNextNeeded;
/*     */   private final int rewardFlagId;
/*  19 */   private JournalReward reward = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public JournalTier(byte tierId, String tierName, byte lastTierId, byte nextTierId, int unlockNextNeeded, int rewardFlag, int... achievements) {
/*  24 */     this.tierId = tierId;
/*  25 */     this.tierName = tierName;
/*  26 */     this.lastTierId = lastTierId;
/*  27 */     this.nextTierId = nextTierId;
/*  28 */     this.unlockNextNeeded = unlockNextNeeded;
/*  29 */     this.rewardFlagId = rewardFlag;
/*     */     
/*  31 */     this.achievementList = new ArrayList<>();
/*  32 */     for (int i : achievements) {
/*  33 */       this.achievementList.add(Integer.valueOf(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public byte getTierId() {
/*  38 */     return this.tierId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTierName() {
/*  43 */     return this.tierName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAchievement(int achievementId) {
/*  48 */     return this.achievementList.contains(Integer.valueOf(achievementId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVisible(long playerId) {
/*  58 */     if (this.lastTierId < 0) {
/*  59 */       return true;
/*     */     }
/*  61 */     return ((JournalTier)PlayerJournal.getAllTiers().get(Byte.valueOf(this.lastTierId))).isNextTierUnlocked(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNextTierUnlocked(long playerId) {
/*  72 */     if (!isVisible(playerId)) {
/*  73 */       return false;
/*     */     }
/*  75 */     Achievement[] achieves = Achievements.getAchievements(playerId);
/*  76 */     int countCompleted = 0;
/*  77 */     for (Achievement a : achieves) {
/*  78 */       if (this.achievementList.contains(Integer.valueOf(a.getTemplate().getNumber())))
/*  79 */         countCompleted++; 
/*     */     } 
/*  81 */     return (countCompleted >= this.unlockNextNeeded);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldUnlockNextTier(long playerId) {
/*  86 */     if (!isVisible(playerId)) {
/*  87 */       return false;
/*     */     }
/*  89 */     Achievement[] achieves = Achievements.getAchievements(playerId);
/*  90 */     int countCompleted = 0;
/*  91 */     for (Achievement a : achieves) {
/*  92 */       if (this.achievementList.contains(Integer.valueOf(a.getTemplate().getNumber())))
/*  93 */         countCompleted++; 
/*     */     } 
/*  95 */     return (countCompleted + 1 == this.unlockNextNeeded);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRewardUnlocked(long playerId) {
/* 106 */     if (!isVisible(playerId)) {
/* 107 */       return false;
/*     */     }
/* 109 */     Achievement[] achieves = Achievements.getAchievements(playerId);
/* 110 */     int countCompleted = 0;
/* 111 */     for (Achievement a : achieves) {
/* 112 */       if (this.achievementList.contains(Integer.valueOf(a.getTemplate().getNumber())))
/* 113 */         countCompleted++; 
/*     */     } 
/* 115 */     return (countCompleted >= this.achievementList.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBeenAwarded(long playerId) {
/* 126 */     Player p = Players.getInstance().getPlayerOrNull(playerId);
/* 127 */     if (p != null) {
/*     */       
/* 129 */       if (p.hasFlag(getRewardFlag())) {
/* 130 */         return true;
/*     */       }
/*     */     } else {
/*     */       
/* 134 */       PlayerInfo pInf = PlayerInfoFactory.getPlayerInfoWithWurmId(playerId);
/* 135 */       if (pInf != null && pInf.isFlagSet(getRewardFlag())) {
/* 136 */         return true;
/*     */       }
/*     */     } 
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldUnlockReward(long playerId) {
/* 144 */     if (!isVisible(playerId)) {
/* 145 */       return false;
/*     */     }
/* 147 */     if (hasBeenAwarded(playerId)) {
/* 148 */       return false;
/*     */     }
/* 150 */     Achievement[] achieves = Achievements.getAchievements(playerId);
/* 151 */     int countCompleted = 0;
/* 152 */     for (Achievement a : achieves) {
/* 153 */       if (this.achievementList.contains(Integer.valueOf(a.getTemplate().getNumber())))
/* 154 */         countCompleted++; 
/*     */     } 
/* 156 */     return (countCompleted + 1 == this.achievementList.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNextUnlockCount() {
/* 161 */     return this.unlockNextNeeded;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getLastTierId() {
/* 166 */     return this.lastTierId;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getNextTierId() {
/* 171 */     return this.nextTierId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalAchievements() {
/* 176 */     return this.achievementList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<Integer> getAchievementList() {
/* 181 */     return this.achievementList;
/*     */   }
/*     */ 
/*     */   
/*     */   public JournalTier getNextTier() {
/* 186 */     return PlayerJournal.getAllTiers().get(Byte.valueOf(this.nextTierId));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRewardFlag() {
/* 191 */     return this.rewardFlagId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReward(JournalReward jr) {
/* 196 */     this.reward = jr;
/*     */   }
/*     */ 
/*     */   
/*     */   public void awardReward(long playerId) {
/* 201 */     Player p = Players.getInstance().getPlayerOrNull(playerId);
/* 202 */     if (p == null) {
/*     */       return;
/*     */     }
/* 205 */     if (this.reward != null) {
/*     */       
/* 207 */       this.reward.runReward(p);
/* 208 */       p.setFlag(getRewardFlag(), true);
/* 209 */       p.getCommunicator().sendSafeServerMessage("Congratulations, you fully completed " + getTierName() + " and earned the reward: " + this.reward
/* 210 */           .getRewardDesc(), (byte)2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRewardString() {
/* 216 */     if (this.reward != null) {
/* 217 */       return this.reward.getRewardDesc();
/*     */     }
/* 219 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\JournalTier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
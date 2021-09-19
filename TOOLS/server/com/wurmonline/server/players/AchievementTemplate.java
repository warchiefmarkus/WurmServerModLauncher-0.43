/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
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
/*     */ public final class AchievementTemplate
/*     */   implements MiscConstants
/*     */ {
/*  34 */   private static final Logger logger = Logger.getLogger(AchievementTemplate.class.getName());
/*     */   
/*     */   public static final String CREATOR_SYSTEM = "System";
/*     */   private final int number;
/*     */   private String name;
/*  39 */   private String creator = "System";
/*  40 */   private String description = "";
/*     */   
/*     */   private boolean isForCooking = false;
/*     */   private boolean isInLiters = false;
/*     */   private boolean isPersonalGoal = false;
/*  45 */   private String requirement = "";
/*     */   
/*     */   private boolean playSoundOnUpdate = false;
/*     */   private final boolean invisible;
/*  49 */   private static int nextAchievementId = Servers.localServer.id * 100000;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean oneTimer = false;
/*     */ 
/*     */   
/*  56 */   private int onTriggerOnCounter = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private byte type = 3;
/*     */ 
/*     */ 
/*     */   
/*  65 */   private int[] achievementsTriggered = EMPTY_INT_ARRAY;
/*     */ 
/*     */ 
/*     */   
/*  69 */   private int[] requiredAchievements = EMPTY_INT_ARRAY;
/*     */ 
/*     */   
/*  72 */   private int[] triggeredByAchievements = EMPTY_INT_ARRAY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AchievementTemplate(int identity, String achName, boolean isInvisible) {
/*  79 */     this.number = identity;
/*  80 */     this.name = achName;
/*  81 */     this.invisible = isInvisible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AchievementTemplate(int identity, String achName, boolean isInvisible, String requirementString) {
/*  90 */     this.number = identity;
/*  91 */     this.name = achName;
/*  92 */     this.invisible = isInvisible;
/*     */     
/*  94 */     this.isPersonalGoal = (identity < 335);
/*  95 */     this.requirement = requirementString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AchievementTemplate(int identity, String achName, boolean isInvisible, int triggerOn, byte achievementType, boolean playUpdateSound, boolean isOneTimer) {
/* 104 */     this.number = identity;
/* 105 */     this.name = achName;
/* 106 */     this.invisible = isInvisible;
/* 107 */     this.onTriggerOnCounter = triggerOn;
/* 108 */     this.type = achievementType;
/* 109 */     this.playSoundOnUpdate = playUpdateSound;
/* 110 */     this.oneTimer = isOneTimer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AchievementTemplate(int identity, String achName, boolean isInvisible, int triggerOn, byte achievementType, boolean playUpdateSound, boolean isOneTimer, String requirementString) {
/* 119 */     this.number = identity;
/* 120 */     this.name = achName;
/* 121 */     this.invisible = isInvisible;
/* 122 */     this.onTriggerOnCounter = triggerOn;
/* 123 */     this.type = achievementType;
/* 124 */     this.playSoundOnUpdate = playUpdateSound;
/* 125 */     this.oneTimer = isOneTimer;
/*     */     
/* 127 */     this.isPersonalGoal = (identity < 335);
/* 128 */     this.requirement = requirementString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AchievementTemplate(int identity, String achName, boolean isInvisible, int triggerOn, String myDescription, String creatorName, boolean playUpdateSound, boolean loaded) {
/* 137 */     this.number = identity;
/* 138 */     this.name = achName;
/* 139 */     this.invisible = isInvisible;
/* 140 */     this.description = myDescription;
/* 141 */     this.onTriggerOnCounter = triggerOn;
/* 142 */     this.type = 2;
/* 143 */     this.creator = creatorName;
/* 144 */     this.playSoundOnUpdate = playUpdateSound;
/* 145 */     this.oneTimer = true;
/* 146 */     nextAchievementId = Math.max(nextAchievementId, this.number + 1);
/* 147 */     if (!loaded)
/*     */     {
/* 149 */       AchievementGenerator.insertAchievementTemplate(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete() {
/* 155 */     AchievementGenerator.deleteAchievementTemplate(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNextAchievementId() {
/* 165 */     return nextAchievementId;
/*     */   }
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
/*     */   public int[] getAchievementsTriggered() {
/* 205 */     return this.achievementsTriggered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAchievementsTriggered(int[] aAchievementsTriggered) {
/* 216 */     this.achievementsTriggered = aAchievementsTriggered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getRequiredAchievements() {
/* 226 */     return this.requiredAchievements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredAchievements(int[] aRequiredAchievements) {
/* 237 */     this.requiredAchievements = aRequiredAchievements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 247 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescription(String aDescription) {
/* 258 */     this.description = aDescription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForCooking() {
/* 267 */     return this.isForCooking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsForCooking(boolean isForCooking) {
/* 276 */     this.isForCooking = isForCooking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInLiters() {
/* 285 */     return this.isInLiters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsInLiters(boolean isInLiters) {
/* 294 */     this.isInLiters = isInLiters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInvisible() {
/* 304 */     return this.invisible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumber() {
/* 314 */     return this.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 324 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String aName) {
/* 335 */     this.name = aName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTriggerOnCounter() {
/* 345 */     return this.onTriggerOnCounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTriggerOnCounter(int triggerOnCounter) {
/* 356 */     this.onTriggerOnCounter = triggerOnCounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreator() {
/* 366 */     return this.creator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreator(String aCreator) {
/* 377 */     this.creator = aCreator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getType() {
/* 387 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(byte aType) {
/* 398 */     this.type = aType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPlaySoundOnUpdate() {
/* 408 */     return this.playSoundOnUpdate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaySoundOnUpdate(boolean aPlaySoundOnUpdate) {
/* 419 */     this.playSoundOnUpdate = aPlaySoundOnUpdate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOneTimer() {
/* 429 */     return this.oneTimer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOneTimer(boolean aOneTimer) {
/* 440 */     this.oneTimer = aOneTimer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequirement() {
/* 450 */     return this.requirement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPersonalGoal() {
/* 460 */     return this.isPersonalGoal;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTriggeredByAchievement(int achievement) {
/* 465 */     if (this.triggeredByAchievements.length > 0) {
/*     */       
/* 467 */       int[] newList = new int[this.triggeredByAchievements.length + 1];
/* 468 */       System.arraycopy(this.triggeredByAchievements, 0, newList, 0, this.triggeredByAchievements.length);
/* 469 */       newList[newList.length - 1] = achievement;
/*     */       
/* 471 */       this.triggeredByAchievements = newList;
/*     */     } else {
/*     */       
/* 474 */       this.triggeredByAchievements = new int[] { achievement };
/*     */     } 
/*     */   }
/*     */   
/*     */   public int[] getTriggeredByAchievements() {
/* 479 */     return this.triggeredByAchievements;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getProgressFor(long wurmId) {
/* 484 */     if (Achievements.getAchievementObject(wurmId).getAchievement(this.number) != null) {
/* 485 */       return 1.0F;
/*     */     }
/* 487 */     if (this.triggeredByAchievements.length == 0 || getTriggerOnCounter() == 0) {
/* 488 */       return 0.0F;
/*     */     }
/* 490 */     float totalCount = 0.0F;
/* 491 */     for (int i : this.triggeredByAchievements) {
/*     */       
/* 493 */       Achievement a = Achievements.getAchievementObject(wurmId).getAchievement(i);
/* 494 */       if (a != null)
/*     */       {
/*     */         
/* 497 */         totalCount += a.getCounter();
/*     */       }
/*     */     } 
/* 500 */     return Math.max(0.0F, Math.min(1.0F, totalCount / getTriggerOnCounter()));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\AchievementTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
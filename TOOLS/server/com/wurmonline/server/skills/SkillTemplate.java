/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkillTemplate
/*     */   implements TimeConstants, Comparable<SkillTemplate>
/*     */ {
/*  37 */   private long decayTime = 86400000L;
/*     */ 
/*     */   
/*     */   @Nonnull
/*  41 */   private int[] dependencies = MiscConstants.EMPTY_INT_ARRAY;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   String name = "Unknown skill";
/*     */ 
/*     */ 
/*     */   
/*  50 */   private float difficulty = 1.0F;
/*     */   
/*     */   private final int number;
/*     */   
/*     */   private final short type;
/*     */   
/*     */   boolean fightSkill = false;
/*     */   
/*     */   boolean thieverySkill = false;
/*     */   
/*     */   boolean ignoresEnemies = false;
/*     */   
/*     */   boolean isPriestSlowskillgain = false;
/*  63 */   long tickTime = 0L;
/*     */   
/*     */   public static final long TICKTIME_ZERO = 0L;
/*     */   
/*     */   public static final long TICKTIME_FIVE = 300000L;
/*     */   
/*     */   public static final long TICKTIME_ONE = 60000L;
/*     */   public static final long TICKTIME_TEN = 600000L;
/*     */   public static final long TICKTIME_TWENTY = 1200000L;
/*     */   public static final long TICKTIME_HOUR = 3600000L;
/*  73 */   private final float difficultyDivider = Servers.localServer.isChallengeServer() ? 50.0F : 1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SkillTemplate(int aNumber, String aName, float aDifficulty, @Nonnull int[] aDependencies, long aDecayTime, short aType) {
/*  81 */     this.number = aNumber;
/*  82 */     this.name = aName;
/*  83 */     this.difficulty = aDifficulty / this.difficultyDivider;
/*  84 */     this.dependencies = aDependencies;
/*  85 */     this.decayTime = Math.max(aDecayTime, 1L);
/*  86 */     this.type = aType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SkillTemplate(int aNumber, String aName, float aDifficulty, @Nonnull int[] aDependencies, long aDecayTime, short aType, boolean aFightingSkill, boolean aIgnoreEnemy) {
/*  93 */     this(aNumber, aName, aDifficulty, aDependencies, aDecayTime, aType);
/*  94 */     this.fightSkill = aFightingSkill;
/*  95 */     this.ignoresEnemies = aIgnoreEnemy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SkillTemplate(int aNumber, String aName, float aDifficulty, @Nonnull int[] aDependencies, long aDecayTime, short aType, boolean aThieverySkill, long _tickTime) {
/* 102 */     this(aNumber, aName, aDifficulty, aDependencies, aDecayTime, aType);
/* 103 */     this.thieverySkill = aThieverySkill;
/* 104 */     if (this.thieverySkill)
/* 105 */       this.ignoresEnemies = true; 
/* 106 */     this.tickTime = _tickTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 116 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   int[] getDependencies() {
/* 127 */     return this.dependencies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDifficulty() {
/* 138 */     return this.difficulty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDifficulty(float newDifficulty) {
/* 148 */     this.difficulty = newDifficulty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDecayTime() {
/* 159 */     return this.decayTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumber() {
/* 169 */     return this.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMission() {
/* 178 */     return ((this.number >= 10001 && this.number <= 10040) || this.number == 1005);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getType() {
/* 187 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(SkillTemplate aTemplate) {
/* 198 */     return getName().compareTo(aTemplate.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTickTime() {
/* 203 */     return this.tickTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSlowForPriests() {
/* 212 */     return this.isPriestSlowskillgain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsSlowForPriests(boolean isSlow) {
/* 221 */     this.isPriestSlowskillgain = isSlow;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\SkillTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import edu.umd.cs.findbugs.annotations.NonNull;
/*     */ import java.util.ArrayList;
/*     */ import java.util.logging.Logger;
/*     */ import javafx.util.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LootTableUtilities
/*     */ {
/*  16 */   private static final Logger logger = Logger.getLogger(LootTableUtilities.class.getName());
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
/*     */   public static long getRandomLoot(@NonNull ArrayList<Pair<Short, Long>> lootTable, long maxValue, long minValueCap, long maxValueCap, int minRerolls, int maxRerolls, float chanceToReroll, float rrChanceDecreasePerRoll, long aimRollsTowardsValue) {
/*     */     long rolledValue;
/*  56 */     if (lootTable == null) {
/*     */       
/*  58 */       logger.severe("Loot table was null");
/*  59 */       return -1L;
/*     */     } 
/*  61 */     if (maxValue <= 0L) {
/*     */       
/*  63 */       logger.severe("maxValue was less than or equal to 0, maxValue=" + maxValue);
/*  64 */       return -1L;
/*     */     } 
/*  66 */     if (minValueCap > maxValue || minValueCap > maxValueCap) {
/*     */       
/*  68 */       logger.severe("Min value cap is an unreasonable number. minValueCap=" + minValueCap + ", maxValue=" + maxValue + ", maxValueCap=" + maxValueCap);
/*     */       
/*  70 */       return -1L;
/*     */     } 
/*  72 */     if (maxValueCap > maxValue) {
/*     */       
/*  74 */       logger.severe("Max value cap is larger than the max value. maxValueCap=" + maxValueCap + ", maxValue=" + maxValue);
/*     */       
/*  76 */       return -1L;
/*     */     } 
/*  78 */     if (minRerolls > maxRerolls) {
/*     */       
/*  80 */       logger.severe("minRerolls larger than maxRerolls. minRerolls=" + minRerolls + ", maxRerolls=" + maxRerolls);
/*  81 */       return -1L;
/*     */     } 
/*  83 */     if (chanceToReroll > 1.0D || chanceToReroll < 0.0D) {
/*     */       
/*  85 */       logger.severe("chance to reroll is not a reasonable value. chanceToReroll=" + chanceToReroll);
/*  86 */       return -1L;
/*     */     } 
/*     */ 
/*     */     
/*  90 */     long[] candidateValues = new long[maxRerolls];
/*  91 */     int timesRerolled = 0;
/*  92 */     long actualMinValueCap = (minValueCap < 0L) ? 0L : minValueCap;
/*  93 */     float currentChanceToReroll = chanceToReroll + rrChanceDecreasePerRoll;
/*  94 */     boolean hasRerolled = false;
/*     */ 
/*     */     
/*     */     do {
/*     */       long probationalValue;
/*     */ 
/*     */       
/* 101 */       if (!hasRerolled) {
/* 102 */         hasRerolled = true;
/*     */       } else {
/* 104 */         timesRerolled++;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       do {
/* 110 */         probationalValue = Server.rand.nextLong();
/* 111 */       } while (probationalValue <= maxValue && probationalValue > actualMinValueCap && probationalValue <= maxValueCap);
/*     */ 
/*     */ 
/*     */       
/* 115 */       candidateValues[timesRerolled] = probationalValue;
/*     */ 
/*     */     
/*     */     }
/* 119 */     while (timesRerolled < minRerolls || (Server.rand
/* 120 */       .nextFloat() < currentChanceToReroll - rrChanceDecreasePerRoll && timesRerolled < maxRerolls));
/*     */ 
/*     */     
/* 123 */     if (aimRollsTowardsValue < 0L) {
/*     */       
/* 125 */       long value = 0L;
/*     */       
/* 127 */       for (int i = 0; i <= timesRerolled; i++) {
/*     */         
/* 129 */         if (candidateValues[i] > value) {
/* 130 */           value = candidateValues[i];
/*     */         }
/*     */       } 
/* 133 */       rolledValue = value;
/*     */     }
/*     */     else {
/*     */       
/* 137 */       long value = 0L;
/* 138 */       long distance = Long.MAX_VALUE;
/*     */       
/* 140 */       for (int i = 0; i <= timesRerolled; i++) {
/*     */         
/* 142 */         if (candidateValues[i] == aimRollsTowardsValue) {
/* 143 */           return aimRollsTowardsValue;
/*     */         }
/* 145 */         long temp = Math.abs(candidateValues[i] - aimRollsTowardsValue);
/*     */         
/* 147 */         if (temp < distance) {
/*     */           
/* 149 */           distance = temp;
/* 150 */           value = candidateValues[i];
/*     */         } 
/*     */       } 
/*     */       
/* 154 */       rolledValue = value;
/*     */     } 
/*     */ 
/*     */     
/* 158 */     return rolledValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\LootTableUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
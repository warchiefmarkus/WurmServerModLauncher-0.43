/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ public final class EncounterType
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(EncounterType.class.getName());
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
/*  47 */   private final LinkedList<Integer> chances = new LinkedList<>();
/*  48 */   private final LinkedList<Encounter> encounters = new LinkedList<>();
/*  49 */   private int sumchance = 0;
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Encounter NULL_ENCOUNTER = new Encounter(); private final byte tiletype; public static final byte ELEVATION_GROUND = 0; public static final byte ELEVATION_WATER = 1; public static final byte ELEVATION_DEEP_WATER = 2; static {
/*  54 */     NULL_ENCOUNTER.addType(-10, 0);
/*     */   }
/*     */   public static final byte ELEVATION_FLYING = 3; public static final byte ELEVATION_FLYING_HIGH = 4; public static final byte ELEVATION_BEACH = 5; public static final byte ELEVATION_CAVES = -1; private final byte elev;
/*     */   
/*     */   public EncounterType(byte aTiletype, byte aElevation) {
/*  59 */     this.tiletype = aTiletype;
/*  60 */     this.elev = aElevation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEncounter(Encounter enc, int chance) {
/*  65 */     this.chances.addLast(Integer.valueOf(chance + this.sumchance));
/*  66 */     this.encounters.addLast(enc);
/*  67 */     this.sumchance += chance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Encounter getRandomEncounter(Creature loggerCret) {
/*  73 */     assert this.sumchance > 0 : "sumchance was 0, which means that no Encounters have been added to this EncounterType - " + this;
/*     */ 
/*     */     
/*  76 */     loggerCret.getCommunicator().sendNormalServerMessage("Sumchance=" + this.sumchance + " for elevation " + this.elev);
/*  77 */     if (this.sumchance > 0) {
/*     */       
/*  79 */       int rand = Server.rand.nextInt(this.sumchance) + 1;
/*  80 */       loggerCret.getCommunicator().sendNormalServerMessage("Rand=" + rand);
/*  81 */       for (int x = 0; x < this.chances.size(); x++) {
/*     */         
/*  83 */         Integer ii = this.chances.get(x);
/*  84 */         loggerCret.getCommunicator().sendNormalServerMessage("Chance integer=" + ii + " for " + ((Encounter)this.encounters
/*  85 */             .get(x)).getTypes());
/*  86 */         if (rand <= ii.intValue())
/*     */         {
/*  88 */           loggerCret.getCommunicator().sendNormalServerMessage("Returning " + x);
/*     */           
/*  90 */           return this.encounters.get(x);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/*  96 */       logger.warning("sumchance was 0, which means that no Encounters have been added to this EncounterType - " + this);
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   Encounter getRandomEncounter() {
/* 103 */     assert this.sumchance > 0 : "sumchance was 0, which means that no Encounters have been added to this EncounterType - " + this;
/*     */ 
/*     */ 
/*     */     
/* 107 */     if (this.sumchance > 0) {
/*     */       
/* 109 */       int rand = Server.rand.nextInt(this.sumchance) + 1;
/* 110 */       for (int x = 0; x < this.chances.size(); x++) {
/*     */         
/* 112 */         Integer ii = this.chances.get(x);
/* 113 */         if (rand <= ii.intValue()) {
/* 114 */           return this.encounters.get(x);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 119 */       logger.warning("sumchance was 0, which means that no Encounters have been added to this EncounterType - " + this);
/*     */     } 
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getTiletype() {
/* 131 */     return this.tiletype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getElev() {
/* 141 */     return this.elev;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfEncounters() {
/* 151 */     return this.encounters.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSumchance() {
/* 161 */     return this.sumchance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     return "EncounterType [tiletype=" + this.tiletype + ", elev=" + this.elev + ", encounters=" + getNumberOfEncounters() + ", sumchance=" + this.sumchance + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\EncounterType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
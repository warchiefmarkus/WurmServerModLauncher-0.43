/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum EpicMissionEnum
/*     */ {
/*  10 */   BUILDSTRUCTURE_SP((byte)101, 1, 3, 4, 4, true, false, true, true, false, 250, 250, 0, 60, false, true, new String[] { "creation", "building" }),
/*  11 */   BUILDSTRUCTURE_TO((byte)102, 1, 4, 5, 5, true, false, true, true, false, 250, 500, 0, 60, false, true, new String[] { "creation", "building" }),
/*  12 */   BUILDSTRUCTURE_SG((byte)103, 1, 5, 6, 6, true, false, true, true, false, 250, 750, 0, 60, false, true, new String[] { "creation", "building"
/*     */     }),
/*  14 */   RITUALMS_FRIENDLY((byte)104, 4, 1, 7, 4, true, false, true, true, false, 0, 500, 30, 45, false, true, new String[] { "proximity", "humility" }),
/*  15 */   RITUALMS_ENEMY((byte)105, 4, 1, 7, 5, false, true, true, false, true, 0, 500, 30, 45, false, true, new String[] { "proximity", "humility"
/*     */     }),
/*  17 */   CUTTREE_FRIENDLY((byte)106, 4, 1, 5, 5, true, false, true, true, false, 500, 0, 30, 30, false, true, new String[] { "revenge", "entrapment" }),
/*  18 */   CUTTREE_ENEMY((byte)107, 4, 1, 6, 6, false, true, true, false, true, 750, 0, 30, 30, false, true, new String[] { "revenge", "entrapment"
/*     */     }),
/*  20 */   RITUALGT((byte)108, 4, 1, 5, 5, false, true, true, false, true, 0, 300, 30, 30, false, true, new String[] { "submission", "danger"
/*     */     }),
/*  22 */   SACMISSIONITEMS((byte)109, 5, 1, 7, 7, true, true, true, true, false, 100, 500, 20, 30, true, false, new String[] { "wealth", "sacrifice"
/*     */     }),
/*  24 */   SACITEMS((byte)110, 5, 1, 7, 7, true, false, true, true, false, 0, 500, 20, 30, true, false, new String[] { "wealth", "sacrifice"
/*     */     }),
/*  26 */   CREATEITEMS((byte)111, 6, 1, 7, 7, true, false, true, true, false, 0, 500, 20, 30, true, false, new String[] { "construction", "thought"
/*     */     }),
/*  28 */   GIVEITEMS_FRIENDLY((byte)112, 4, 1, 6, 4, true, false, true, true, false, 100, 500, 20, 20, true, false, new String[] { "gifts", "concession" }),
/*  29 */   GIVEITEMS_ENEMY((byte)113, 3, 3, 7, 5, true, true, true, true, false, 150, 600, 30, 40, true, false, new String[] { "gifts", "concession"
/*     */     }),
/*  31 */   SLAYCREATURE_PASSIVE((byte)114, 4, 1, 3, 3, true, false, true, true, false, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason" }),
/*  32 */   SLAYCREATURE_HOSTILELOW((byte)115, 5, 2, 6, 6, true, true, true, true, true, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason" }),
/*  33 */   SLAYCREATURE_HOSTILEHIGH((byte)116, 4, 5, 7, 7, true, true, true, true, true, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason"
/*     */     }),
/*  35 */   SLAYTRAITOR_PASSIVE((byte)117, 4, 1, 3, 3, true, false, true, true, false, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason" }),
/*  36 */   SLAYTRAITOR_HOSTILELOW((byte)118, 5, 2, 5, 5, true, true, true, true, true, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason" }),
/*  37 */   SLAYTRAITOR_HOSTILEHIGH((byte)119, 4, 5, 7, 7, true, true, true, true, true, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason"
/*     */     }),
/*  39 */   DESTROYGT((byte)120, 3, 3, 6, 6, false, true, true, false, true, 750, 0, 30, 30, false, true, new String[] { "destruction", "devastation"
/*     */     }),
/*  41 */   SACCREATURE_PASSIVE((byte)121, 3, 1, 3, 3, true, false, true, true, false, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason" }),
/*  42 */   SACCREATURE_HOSTILELOW((byte)122, 4, 4, 5, 5, true, true, true, true, true, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason" }),
/*  43 */   SACCREATURE_HOSTILEHIGH((byte)123, 3, 6, 7, 7, true, true, true, true, true, 0, 500, 15, 30, true, false, new String[] { "annihilation", "treason"
/*     */     }),
/*  45 */   SLAYTOWERGUARDS((byte)124, 3, 1, 7, 7, false, true, true, false, true, 0, 500, 15, 30, true, false, new String[] { "cleansing", "attack" }); private byte missionType; private int missionChance; private int minDifficulty; private int maxDifficulty; private boolean friendlyTerritory; private boolean enemyTerritory;
/*     */   private boolean battlegroundServer;
/*     */   private boolean homeServer;
/*     */   
/*     */   public static EpicMissionEnum getRandomMission(int difficulty, boolean battlegroundServer, boolean homeServer, boolean enemyHomeServer) {
/*  50 */     int totalChance = 0;
/*  51 */     for (EpicMissionEnum f : values()) {
/*     */       
/*  53 */       if (f.minDifficulty <= difficulty && f.maxDifficulty >= difficulty)
/*     */       {
/*  55 */         if ((battlegroundServer && f.battlegroundServer) || (homeServer && f.homeServer) || (enemyHomeServer && f.enemyHomeServer))
/*     */         {
/*     */           
/*  58 */           totalChance += f.getMissionChance();
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/*  63 */     if (totalChance == 0) {
/*  64 */       return null;
/*     */     }
/*  66 */     int winningVal = Server.rand.nextInt(totalChance);
/*  67 */     int thisVal = 0;
/*  68 */     for (EpicMissionEnum f : values()) {
/*     */       
/*  70 */       if (f.minDifficulty <= difficulty && f.maxDifficulty >= difficulty)
/*     */       {
/*  72 */         if ((battlegroundServer && f.battlegroundServer) || (homeServer && f.homeServer) || (enemyHomeServer && f.enemyHomeServer)) {
/*     */ 
/*     */           
/*  75 */           if (thisVal + f.getMissionChance() > winningVal) {
/*  76 */             return f;
/*     */           }
/*  78 */           thisVal += f.getMissionChance();
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  83 */     return null;
/*     */   }
/*     */   private boolean enemyHomeServer; private int baseKarma; private int karmaBonusDiffMult; private int baseSleep; private int sleepBonusDiffMult; private boolean isKarmaMultProgress; private boolean isSleepMultNearby; private String[] missionNames;
/*     */   
/*     */   public static EpicMissionEnum getMissionForType(byte missionType) {
/*  88 */     for (EpicMissionEnum f : values()) {
/*     */       
/*  90 */       if (f.getMissionType() == missionType) {
/*  91 */         return f;
/*     */       }
/*     */     } 
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMissionItem(EpicMissionEnum mission) {
/* 105 */     switch (mission.getMissionType()) {
/*     */       
/*     */       case 109:
/*     */       case 110:
/*     */       case 111:
/*     */       case 112:
/*     */       case 113:
/* 112 */         return true;
/*     */     } 
/*     */     
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNumReqItemEffected(EpicMissionEnum mission) {
/* 126 */     switch (mission.getMissionType()) {
/*     */       
/*     */       case 110:
/*     */       case 111:
/* 130 */         return true;
/*     */     } 
/*     */     
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMissionCreature(EpicMissionEnum mission) {
/* 144 */     switch (mission.getMissionType()) {
/*     */       
/*     */       case 114:
/*     */       case 115:
/*     */       case 116:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
/*     */       case 121:
/*     */       case 122:
/*     */       case 123:
/*     */       case 124:
/* 156 */         return true;
/*     */     } 
/*     */     
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMissionKarmaGivenOnKill(EpicMissionEnum mission) {
/* 170 */     switch (mission.getMissionType()) {
/*     */       
/*     */       case 114:
/*     */       case 115:
/*     */       case 116:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
/*     */       case 124:
/* 179 */         return true;
/*     */     } 
/*     */     
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isKarmaSplitNearby(EpicMissionEnum mission) {
/* 187 */     switch (mission.getMissionType()) {
/*     */       
/*     */       case 101:
/*     */       case 102:
/*     */       case 103:
/* 192 */         return true;
/*     */     } 
/*     */     
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isRitualMission(EpicMissionEnum mission) {
/* 200 */     switch (mission.getMissionType()) {
/*     */       
/*     */       case 104:
/*     */       case 105:
/*     */       case 108:
/* 205 */         return true;
/*     */     } 
/*     */     
/* 208 */     return false;
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
/*     */   EpicMissionEnum(byte missionType, int missionChance, int minDifficulty, int maxDifficulty, int maxDifficultyPvp, boolean friendlyTerritory, boolean enemyTerritory, boolean battlegroundServer, boolean homeServer, boolean enemyHomeServer, int baseKarma, int karmaBonusDiffMult, int baseSleep, int sleepBonusDiffMult, boolean isKarmaMultProgress, boolean isSleepMultNearby, String... missionNames) {
/* 240 */     this.missionType = missionType;
/* 241 */     this.missionChance = missionChance;
/* 242 */     this.minDifficulty = minDifficulty;
/* 243 */     this.maxDifficulty = maxDifficulty;
/* 244 */     if (Servers.localServer.PVPSERVER)
/* 245 */       this.maxDifficulty = maxDifficultyPvp; 
/* 246 */     this.friendlyTerritory = friendlyTerritory;
/* 247 */     this.enemyTerritory = enemyTerritory;
/* 248 */     this.battlegroundServer = battlegroundServer;
/* 249 */     this.homeServer = homeServer;
/* 250 */     this.enemyHomeServer = enemyHomeServer;
/* 251 */     this.baseKarma = baseKarma;
/* 252 */     this.karmaBonusDiffMult = karmaBonusDiffMult;
/* 253 */     this.baseSleep = baseSleep;
/* 254 */     this.sleepBonusDiffMult = sleepBonusDiffMult;
/* 255 */     this.isKarmaMultProgress = isKarmaMultProgress;
/* 256 */     this.isSleepMultNearby = isSleepMultNearby;
/* 257 */     this.missionNames = missionNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getMissionType() {
/* 262 */     return this.missionType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMissionChance() {
/* 267 */     return this.missionChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinDifficulty() {
/* 272 */     return this.minDifficulty;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxDifficulty() {
/* 277 */     return this.maxDifficulty;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFriendlyTerritory() {
/* 282 */     return this.friendlyTerritory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnemyTerritory() {
/* 287 */     return this.enemyTerritory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBattlegroundServer() {
/* 292 */     return this.battlegroundServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHomeServer() {
/* 297 */     return this.homeServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnemyHomeServer() {
/* 302 */     return this.enemyHomeServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseKarma() {
/* 307 */     return this.baseKarma;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getKarmaBonusDiffMult() {
/* 312 */     return this.karmaBonusDiffMult;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseSleep() {
/* 317 */     return this.baseSleep;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSleepBonusDiffMult() {
/* 322 */     return this.sleepBonusDiffMult;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKarmaMultProgress() {
/* 327 */     return this.isKarmaMultProgress;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSleepMultNearby() {
/* 332 */     return this.isSleepMultNearby;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMissionNames() {
/* 337 */     return this.missionNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRandomMissionName() {
/* 342 */     return this.missionNames[Server.rand.nextInt(this.missionNames.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   public static final long getTimeReductionForMission(byte missionType, int missionDifficulty) {
/* 347 */     long toReturn = 14400000L;
/* 348 */     toReturn += 7200000L * missionDifficulty;
/* 349 */     return toReturn;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicMissionEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.deities.DbRitual;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RiteEvent
/*     */ {
/*  18 */   protected static Logger logger = Logger.getLogger(RiteEvent.class.getName());
/*     */   
/*     */   protected static final int MAXIMUM_RITES_CAST = 2147483647;
/*     */   
/*  22 */   protected static HashMap<Integer, RiteEvent> riteEvents = new HashMap<>();
/*  23 */   protected static int lastRiteId = 0;
/*  24 */   public static int lastClaimId = -1;
/*     */   
/*  26 */   protected ArrayList<Long> claimedReward = new ArrayList<>();
/*     */ 
/*     */   
/*     */   protected int id;
/*     */ 
/*     */   
/*     */   protected long casterId;
/*     */ 
/*     */   
/*     */   protected int spellId;
/*     */   
/*     */   protected int deityNum;
/*     */   
/*     */   protected int templateDeity;
/*     */   
/*     */   protected long castTime;
/*     */   
/*     */   protected long duration;
/*     */   
/*     */   protected long expiration;
/*     */ 
/*     */   
/*     */   public RiteEvent(int id, long casterId, int spellId, int deityNum, long castTime, long duration) {
/*  49 */     this.casterId = casterId;
/*  50 */     this.spellId = spellId;
/*  51 */     this.deityNum = deityNum;
/*  52 */     Deity baseDeity = Deities.getDeity(deityNum);
/*  53 */     if (baseDeity != null) {
/*     */       
/*  55 */       this.templateDeity = baseDeity.getTemplateDeity();
/*     */     }
/*     */     else {
/*     */       
/*  59 */       logger.warning(String.format("No template deity found for deity with ID %d when creating a RiteEvent.", new Object[] { Integer.valueOf(deityNum) }));
/*  60 */       this.templateDeity = deityNum;
/*     */     } 
/*  62 */     this.castTime = castTime;
/*  63 */     this.duration = duration;
/*  64 */     this.expiration = castTime + duration;
/*     */ 
/*     */     
/*  67 */     if (id < 0) {
/*     */       
/*  69 */       for (int i = lastRiteId; i < Integer.MAX_VALUE; i++) {
/*     */ 
/*     */         
/*  72 */         RiteEvent result = riteEvents.putIfAbsent(Integer.valueOf(i), this);
/*  73 */         if (result == null) {
/*     */ 
/*     */           
/*  76 */           this.id = i;
/*  77 */           lastRiteId = i;
/*     */           break;
/*     */         } 
/*     */       } 
/*  81 */       DbRitual.createRiteEvent(this);
/*     */     }
/*     */     else {
/*     */       
/*  85 */       this.id = id;
/*  86 */       riteEvents.put(Integer.valueOf(id), this);
/*  87 */       if (id > lastRiteId) {
/*  88 */         lastRiteId = id;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/*  97 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCasterId() {
/* 105 */     return this.casterId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSpellId() {
/* 113 */     return this.spellId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDeityNum() {
/* 121 */     return this.deityNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCastTime() {
/* 129 */     return this.castTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDuration() {
/* 137 */     return this.duration;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addRitualClaim(int id, long playerId, int ritualCastsId, long claimTime) {
/* 142 */     RiteEvent event = riteEvents.get(Integer.valueOf(ritualCastsId));
/* 143 */     if (event == null) {
/*     */       
/* 145 */       logger.warning(String.format("Could not load Ritual Claim for player %d because RiteEvent %d does not exist.", new Object[] {
/* 146 */               Long.valueOf(playerId), Integer.valueOf(ritualCastsId)
/*     */             }));
/*     */       
/*     */       return;
/*     */     } 
/* 151 */     event.claimedReward.add(Long.valueOf(playerId));
/*     */ 
/*     */     
/* 154 */     if (lastClaimId < id) {
/* 155 */       lastClaimId = id;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RiteOfSpringEvent
/*     */     extends RiteEvent
/*     */   {
/*     */     public RiteOfSpringEvent(int id, long casterId, int spellId, int deityNum, long castTime, long duration) {
/* 163 */       super(id, casterId, spellId, deityNum, castTime, duration);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean claimRiteReward(Player player) {
/* 168 */       if (!super.claimRiteReward(player))
/*     */       {
/*     */         
/* 171 */         return false;
/*     */       }
/*     */       
/* 174 */       player.getCommunicator().sendSafeServerMessage("You feel enlightened!", (byte)2);
/* 175 */       awardBasicBonuses(player, player.getMindLogical());
/* 176 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class RiteOfTheSunEvent
/*     */     extends RiteEvent
/*     */   {
/*     */     public RiteOfTheSunEvent(int id, long casterId, int spellId, int deityNum, long castTime, long duration) {
/* 185 */       super(id, casterId, spellId, deityNum, castTime, duration);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean claimRiteReward(Player player) {
/* 190 */       if (!super.claimRiteReward(player))
/*     */       {
/*     */         
/* 193 */         return false;
/*     */       }
/*     */       
/* 196 */       player.getCommunicator().sendSafeServerMessage("You feel a sudden surge of energy!", (byte)2);
/* 197 */       awardBasicBonuses(player, player.getStaminaSkill());
/* 198 */       player.getBody().healFully();
/* 199 */       float nut = (80 + Server.rand.nextInt(19)) / 100.0F;
/* 200 */       player.getStatus().refresh(nut, false);
/* 201 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class RiteOfCropEvent
/*     */     extends RiteEvent
/*     */   {
/*     */     public RiteOfCropEvent(int id, long casterId, int spellId, int deityNum, long castTime, long duration) {
/* 210 */       super(id, casterId, spellId, deityNum, castTime, duration);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean claimRiteReward(Player player) {
/* 215 */       if (!super.claimRiteReward(player))
/*     */       {
/*     */         
/* 218 */         return false;
/*     */       }
/*     */       
/* 221 */       player.getCommunicator().sendSafeServerMessage("You feel a wave of warmth!", (byte)2);
/* 222 */       awardBasicBonuses(player, player.getSoulDepth());
/* 223 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class RiteOfDeathEvent
/*     */     extends RiteEvent
/*     */   {
/*     */     public RiteOfDeathEvent(int id, long casterId, int spellId, int deityNum, long castTime, long duration) {
/* 232 */       super(id, casterId, spellId, deityNum, castTime, duration);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean claimRiteReward(Player player) {
/* 237 */       if (!super.claimRiteReward(player))
/*     */       {
/*     */         
/* 240 */         return false;
/*     */       }
/*     */       
/* 243 */       player.getCommunicator().sendSafeServerMessage("You feel a sudden surge of power!", (byte)2);
/* 244 */       awardBasicBonuses(player, player.getSoulStrength());
/* 245 */       return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void createGenericRiteEvent(int id, long casterId, int spellId, int deityNum, long castTime, long duration) {
/* 263 */     switch (spellId) {
/*     */       
/*     */       case 403:
/* 266 */         new RiteOfSpringEvent(id, casterId, spellId, deityNum, castTime, duration);
/*     */         break;
/*     */       case 402:
/* 269 */         new RiteOfDeathEvent(id, casterId, spellId, deityNum, castTime, duration);
/*     */         break;
/*     */       case 401:
/* 272 */         new RiteOfTheSunEvent(id, casterId, spellId, deityNum, castTime, duration);
/*     */         break;
/*     */       case 400:
/* 275 */         new RiteOfCropEvent(id, casterId, spellId, deityNum, castTime, duration);
/*     */         break;
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
/*     */   
/*     */   public static boolean isActive(int spellid) {
/* 289 */     for (RiteEvent event : riteEvents.values()) {
/*     */ 
/*     */       
/* 292 */       if (spellid == 400 && !(event instanceof RiteOfCropEvent))
/*     */         continue; 
/* 294 */       if (spellid == 403 && !(event instanceof RiteOfSpringEvent))
/*     */         continue; 
/* 296 */       if (spellid == 402 && !(event instanceof RiteOfDeathEvent))
/*     */         continue; 
/* 298 */       if (spellid == 401 && !(event instanceof RiteOfTheSunEvent)) {
/*     */         continue;
/*     */       }
/*     */       
/* 302 */       if (event.expiration > System.currentTimeMillis())
/*     */       {
/*     */         
/* 305 */         return true;
/*     */       }
/*     */     } 
/* 308 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void awardBasicBonuses(Player player, Skill skill) {
/* 319 */     double currentKnowledge = skill.getKnowledge();
/* 320 */     double bonus = (100.0D - currentKnowledge) * 0.002D;
/* 321 */     skill.setKnowledge(currentKnowledge + bonus, false);
/* 322 */     player.getSaveFile().addToSleep(18000);
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
/*     */   public boolean claimRiteReward(Player player) {
/* 334 */     if (player.getDeity().getTemplateDeity() != this.templateDeity)
/*     */     {
/*     */       
/* 337 */       return false;
/*     */     }
/* 339 */     if (this.expiration < System.currentTimeMillis())
/*     */     {
/*     */       
/* 342 */       return false;
/*     */     }
/* 344 */     if (this.claimedReward.contains(Long.valueOf(player.getWurmId())))
/*     */     {
/*     */       
/* 347 */       return false;
/*     */     }
/*     */     
/* 350 */     this.claimedReward.add(Long.valueOf(player.getWurmId()));
/* 351 */     lastClaimId++;
/* 352 */     DbRitual.createRiteClaim(lastClaimId, player.getWurmId(), this.id, System.currentTimeMillis());
/* 353 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkRiteRewards(Player player) {
/* 363 */     for (RiteEvent event : riteEvents.values())
/*     */     {
/* 365 */       event.claimRiteReward(player);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RiteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
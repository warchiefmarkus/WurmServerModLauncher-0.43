/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.math.BigInteger;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
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
/*     */ public class AffinitiesTimed
/*     */ {
/*  51 */   private static final Logger logger = Logger.getLogger(AffinitiesTimed.class.getName());
/*     */   
/*  53 */   private static final Map<Long, AffinitiesTimed> playerTimedAffinities = new ConcurrentHashMap<>();
/*     */   
/*     */   private static final String GET_ALL_PLAYER_TIMED_AFFINITIES = "SELECT * FROM AFFINITIESTIMED";
/*     */   
/*     */   private static final String CREATE_PLAYER_TIMED_AFFINITY = "INSERT INTO AFFINITIESTIMED (PLAYERID,SKILL,EXPIRATION) VALUES (?,?,?)";
/*     */   private static final String UPDATE_PLAYER_TIMED_AFFINITY = "UPDATE AFFINITIESTIMED SET EXPIRATION=? WHERE PLAYERID=? AND SKILL=?";
/*     */   private static final String DELETE_PLAYER_TIMED_AFFINITIES = "DELETE FROM AFFINITIESTIMED WHERE PLAYERID=?";
/*     */   private static final String DELETE_PLAYER_SKILL_TIMED_AFFINITIES = "DELETE FROM AFFINITIESTIMED WHERE PLAYERID=? AND Skill=?";
/*     */   private final long wurmId;
/*  62 */   private final Map<Integer, Long> timedAffinities = new ConcurrentHashMap<>();
/*  63 */   private final Map<Integer, Integer> updateAffinities = new ConcurrentHashMap<>();
/*  64 */   private int lastSkillId = -1;
/*  65 */   private long lastTime = -1L;
/*     */ 
/*     */   
/*     */   public AffinitiesTimed(long playerId) {
/*  69 */     this.wurmId = playerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/*  78 */     return this.wurmId;
/*     */   }
/*     */ 
/*     */   
/*     */   int getLastSkillId() {
/*  83 */     return this.lastSkillId;
/*     */   }
/*     */ 
/*     */   
/*     */   long getLastTime() {
/*  88 */     return this.lastTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void put(int skill, long expires) {
/*  98 */     this.timedAffinities.put(Integer.valueOf(skill), Long.valueOf(expires));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Long getExpires(int skill) {
/* 109 */     return this.timedAffinities.get(Integer.valueOf(skill));
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
/*     */   public boolean add(int skill, long duration) {
/* 121 */     boolean toReturn = false;
/* 122 */     Long expires = getExpires(skill);
/* 123 */     long newExpires = 0L;
/* 124 */     if (expires == null) {
/*     */ 
/*     */       
/* 127 */       newExpires = WurmCalendar.getCurrentTime() + duration * 10L;
/* 128 */       toReturn = true;
/* 129 */       this.updateAffinities.put(Integer.valueOf(skill), Integer.valueOf(skill));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 134 */       newExpires = expires.longValue() + duration;
/* 135 */       this.updateAffinities.put(Integer.valueOf(skill), Integer.valueOf(skill));
/*     */     } 
/* 137 */     this.timedAffinities.put(Integer.valueOf(skill), Long.valueOf(newExpires));
/* 138 */     this.lastSkillId = skill;
/* 139 */     this.lastTime = WurmCalendar.getCurrentTime();
/* 140 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(int skill) {
/* 149 */     dbRemoveTimedAffinity(this.wurmId, skill);
/* 150 */     this.timedAffinities.remove(Integer.valueOf(skill));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void pollTimeAffinities(Creature creature) {
/* 156 */     for (Map.Entry<Integer, Long> entry : this.timedAffinities.entrySet()) {
/*     */       
/* 158 */       int skillId = ((Integer)entry.getKey()).intValue();
/* 159 */       long expires = ((Long)entry.getValue()).longValue();
/* 160 */       if (expires < WurmCalendar.getCurrentTime())
/*     */       {
/*     */         
/* 163 */         sendRemoveTimedAffinity(creature, skillId);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 169 */     for (Integer skill : this.updateAffinities.values()) {
/*     */       
/* 171 */       int skillId = skill.intValue();
/* 172 */       Long expires = this.timedAffinities.get(skill);
/* 173 */       if (expires == null) {
/*     */         
/* 175 */         this.updateAffinities.remove(skill);
/*     */         
/*     */         continue;
/*     */       } 
/* 179 */       if (skillId != this.lastSkillId) {
/*     */         
/* 181 */         this.updateAffinities.remove(skill);
/*     */         
/* 183 */         dbSaveTimedAffinity(this.wurmId, skillId, expires.longValue(), true); continue;
/*     */       } 
/* 185 */       if (WurmCalendar.getCurrentTime() > this.lastTime + 50L) {
/*     */         
/* 187 */         this.lastSkillId = -1;
/*     */         
/* 189 */         this.updateAffinities.remove(skill);
/* 190 */         dbSaveTimedAffinity(this.wurmId, skillId, expires.longValue(), true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEmpty() {
/* 198 */     return this.timedAffinities.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendTimedAffinities(Creature creature) {
/* 203 */     for (Map.Entry<Integer, Long> entry : this.timedAffinities.entrySet()) {
/*     */       
/* 205 */       if (((Long)entry.getValue()).longValue() > WurmCalendar.getCurrentTime())
/*     */       {
/* 207 */         sendTimedAffinity(creature, ((Integer)entry.getKey()).intValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendTimedAffinity(Creature creature, int skillNum) {
/* 215 */     long id = makeId(skillNum);
/*     */     
/* 217 */     Long expires = getExpires(skillNum);
/* 218 */     if (expires != null) {
/*     */       
/* 220 */       int dur = (int)((float)(expires.longValue() - WurmCalendar.getCurrentTime()) / 8.0F);
/*     */       
/* 222 */       if (dur > 0)
/*     */       {
/* 224 */         creature.getCommunicator().sendAddStatusEffect(id, SpellEffectsEnum.SKILL_TIMED_AFFINITY, dur, 
/* 225 */             SkillSystem.getNameFor(skillNum));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRemoveTimedAffinities(Creature creature) {
/* 232 */     for (Map.Entry<Integer, Long> entry : this.timedAffinities.entrySet())
/*     */     {
/* 234 */       sendRemoveTimedAffinity(creature, ((Integer)entry.getKey()).intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRemoveTimedAffinity(Creature creature, int skillNum) {
/* 240 */     creature.getCommunicator().sendRemoveFromStatusEffectBar(makeId(skillNum));
/* 241 */     remove(skillNum);
/*     */   }
/*     */ 
/*     */   
/*     */   private long makeId(int skillNum) {
/* 246 */     long sid = BigInteger.valueOf(skillNum).shiftLeft(32).longValue() + 18L;
/* 247 */     return SpellEffectsEnum.SKILL_TIMED_AFFINITY.createId(sid);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void poll(Creature creature) {
/* 252 */     AffinitiesTimed at = getTimedAffinitiesByPlayer(creature.getWurmId(), false);
/* 253 */     if (at != null)
/*     */     {
/* 255 */       at.pollTimeAffinities(creature);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendTimedAffinitiesFor(Creature creature) {
/* 264 */     AffinitiesTimed at = getTimedAffinitiesByPlayer(creature.getWurmId(), false);
/* 265 */     if (at != null)
/*     */     {
/* 267 */       at.sendTimedAffinities(creature);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static SkillTemplate getTimedAffinitySkill(Creature creature, Item item) {
/* 273 */     if (!creature.isPlayer()) {
/* 274 */       return null;
/*     */     }
/* 276 */     long playerId = creature.getWurmId();
/* 277 */     int ibonus = item.getBonus();
/* 278 */     if (ibonus == -1)
/* 279 */       return null; 
/* 280 */     if (Server.getInstance().isPS() || creature.hasFlag(53)) {
/*     */       
/* 282 */       Random affinityRandom = new Random();
/* 283 */       affinityRandom.setSeed(creature.getWurmId());
/* 284 */       ibonus += affinityRandom.nextInt(SkillSystem.getNumberOfSkillTemplates());
/* 285 */       ibonus %= SkillSystem.getNumberOfSkillTemplates();
/*     */     }
/*     */     else {
/*     */       
/* 289 */       ibonus = (int)(ibonus + (playerId & 0xFFL));
/* 290 */       ibonus = (int)(ibonus + (playerId >>> 8L & 0xFFL));
/* 291 */       ibonus = (int)(ibonus + (playerId >>> 16L & 0xFFL));
/* 292 */       ibonus = (int)(ibonus + (playerId >>> 24L & 0xFFL));
/* 293 */       ibonus = (int)(ibonus + (playerId >>> 32L & 0xFFL));
/* 294 */       ibonus = (int)(ibonus + (playerId >>> 40L & 0xFFL));
/* 295 */       ibonus = (int)(ibonus + (playerId >>> 48L & 0xFFL));
/* 296 */       ibonus = (int)(ibonus + (playerId >>> 56L & 0xFFL));
/* 297 */       ibonus = (ibonus & 0xFF) % SkillSystem.getNumberOfSkillTemplates();
/*     */     } 
/*     */     
/* 300 */     return SkillSystem.getSkillTemplateByIndex(ibonus);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addTimedAffinityFromBonus(Creature creature, int weight, Item item) {
/* 311 */     if (!creature.isPlayer())
/*     */       return; 
/* 313 */     int ibonus = item.getBonus();
/*     */     
/* 315 */     if (ibonus == -1)
/*     */       return; 
/* 317 */     long playerId = creature.getWurmId();
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
/* 329 */     SkillTemplate skillTemplate = getTimedAffinitySkill(creature, item);
/* 330 */     if (skillTemplate == null)
/*     */       return; 
/* 332 */     int skillId = skillTemplate.getNumber();
/*     */ 
/*     */     
/* 335 */     float rarityMod = 1.0F + (item.getRarity() * item.getRarity()) * 0.1F;
/* 336 */     int duration = (int)(weight * item.getCurrentQualityLevel() * rarityMod * item.getFoodComplexity());
/* 337 */     AffinitiesTimed at = getTimedAffinitiesByPlayer(playerId, true);
/*     */ 
/*     */     
/* 340 */     boolean sendMessage = (at.getLastSkillId() != skillId || WurmCalendar.getCurrentTime() > at.getLastTime() + 50L);
/*     */     
/* 342 */     at.add(skillId, duration);
/* 343 */     if (sendMessage)
/*     */     {
/*     */       
/* 346 */       creature.getCommunicator().sendNormalServerMessage("You suddenly realise that you have more of an insight about " + skillTemplate
/* 347 */           .getName().toLowerCase() + "!", (byte)2);
/*     */     }
/*     */     
/* 350 */     at.sendTimedAffinity(creature, skillTemplate.getNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTimedAffinity(long playerId, int skill) {
/* 359 */     AffinitiesTimed at = getTimedAffinitiesByPlayer(playerId, false);
/* 360 */     if (at != null) {
/*     */       
/* 362 */       Long expires = at.getExpires(skill);
/* 363 */       if (expires == null) {
/* 364 */         at.remove(skill);
/*     */       }
/*     */       else {
/*     */         
/* 368 */         if (expires.longValue() > WurmCalendar.getCurrentTime()) {
/* 369 */           return true;
/*     */         }
/* 371 */         at.remove(skill);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 376 */     return false;
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
/*     */   @Nullable
/*     */   public static final AffinitiesTimed getTimedAffinitiesByPlayer(long playerId, boolean autoCreate) {
/* 389 */     AffinitiesTimed at = playerTimedAffinities.get(Long.valueOf(playerId));
/* 390 */     if (at == null && autoCreate) {
/*     */       
/* 392 */       at = new AffinitiesTimed(playerId);
/* 393 */       playerTimedAffinities.put(Long.valueOf(playerId), at);
/*     */     } 
/* 395 */     return at;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteTimedAffinitiesForPlayer(long playerId) {
/* 404 */     dbRemovePlayerTimedAffinities(playerId);
/* 405 */     playerTimedAffinities.remove(Long.valueOf(playerId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeTimedAffinitiesForPlayer(Creature creature) {
/* 410 */     AffinitiesTimed at = getTimedAffinitiesByPlayer(creature.getWurmId(), false);
/* 411 */     if (at != null)
/*     */     {
/* 413 */       at.sendRemoveTimedAffinities(creature);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int loadAllPlayerTimedAffinities() {
/* 422 */     logger.info("Loading all Player Timed Affinities");
/* 423 */     long start = System.nanoTime();
/* 424 */     int count = 0;
/* 425 */     Connection dbcon = null;
/* 426 */     PreparedStatement ps = null;
/* 427 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 430 */       dbcon = DbConnector.getPlayerDbCon();
/* 431 */       ps = dbcon.prepareStatement("SELECT * FROM AFFINITIESTIMED");
/* 432 */       rs = ps.executeQuery();
/* 433 */       while (rs.next())
/*     */       {
/* 435 */         count++;
/* 436 */         long playerId = rs.getLong("PLAYERID");
/* 437 */         int skill = rs.getInt("SKILL");
/* 438 */         long expires = rs.getLong("EXPIRATION");
/*     */         
/* 440 */         AffinitiesTimed at = getTimedAffinitiesByPlayer(playerId, true);
/* 441 */         at.put(skill, expires);
/*     */       }
/*     */     
/* 444 */     } catch (SQLException sqex) {
/*     */       
/* 446 */       logger.log(Level.WARNING, "Failed to load all player timed affinities: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 450 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 451 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/* 454 */     logger.log(Level.INFO, "Number of player timed affinities=" + count + ".");
/*     */     
/* 456 */     logger.log(Level.INFO, "Player timed affinities loaded. That took " + (
/* 457 */         (float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/* 458 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbSaveTimedAffinity(long playerId, int skill, long expires, boolean update) {
/* 463 */     Connection dbcon = null;
/* 464 */     PreparedStatement ps = null;
/* 465 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 468 */       dbcon = DbConnector.getPlayerDbCon();
/* 469 */       if (update) {
/*     */         
/* 471 */         ps = dbcon.prepareStatement("UPDATE AFFINITIESTIMED SET EXPIRATION=? WHERE PLAYERID=? AND SKILL=?");
/* 472 */         ps.setLong(1, expires);
/* 473 */         ps.setLong(2, playerId);
/* 474 */         ps.setInt(3, skill);
/* 475 */         int did = ps.executeUpdate();
/* 476 */         if (did > 0) {
/*     */           return;
/*     */         }
/* 479 */         DbUtilities.closeDatabaseObjects(ps, rs);
/*     */       } 
/* 481 */       ps = dbcon.prepareStatement("INSERT INTO AFFINITIESTIMED (PLAYERID,SKILL,EXPIRATION) VALUES (?,?,?)");
/* 482 */       ps.setLong(1, playerId);
/* 483 */       ps.setInt(2, skill);
/* 484 */       ps.setLong(3, expires);
/* 485 */       ps.executeUpdate();
/*     */     }
/* 487 */     catch (SQLException sqex) {
/*     */       
/* 489 */       logger.log(Level.WARNING, "Failed to save player (" + playerId + ") skill (" + skill + ") timed affinities: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 493 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 494 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dbRemovePlayerTimedAffinities(long playerId) {
/* 504 */     Connection dbcon = null;
/* 505 */     PreparedStatement ps = null;
/* 506 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 509 */       dbcon = DbConnector.getPlayerDbCon();
/* 510 */       ps = dbcon.prepareStatement("DELETE FROM AFFINITIESTIMED WHERE PLAYERID=?");
/* 511 */       ps.setLong(1, playerId);
/* 512 */       ps.executeUpdate();
/*     */     }
/* 514 */     catch (SQLException sqex) {
/*     */       
/* 516 */       logger.log(Level.WARNING, "Failed to remove player (" + playerId + ") timed affiniies: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 520 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 521 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbRemoveTimedAffinity(long playerId, int skill) {
/* 527 */     Connection dbcon = null;
/* 528 */     PreparedStatement ps = null;
/* 529 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 532 */       dbcon = DbConnector.getPlayerDbCon();
/* 533 */       ps = dbcon.prepareStatement("DELETE FROM AFFINITIESTIMED WHERE PLAYERID=? AND Skill=?");
/* 534 */       ps.setLong(1, playerId);
/* 535 */       ps.setInt(2, skill);
/* 536 */       ps.executeUpdate();
/*     */     }
/* 538 */     catch (SQLException sqex) {
/*     */       
/* 540 */       logger.log(Level.WARNING, "Failed to remove player (" + playerId + ")  skill (" + skill + ") timed affinity: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 544 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 545 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\AffinitiesTimed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.questions.MissionManager;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
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
/*     */ public final class TriggerEffects
/*     */   implements CounterTypes
/*     */ {
/*  44 */   private static Logger logger = Logger.getLogger(TriggerEffects.class.getName());
/*     */   
/*     */   public static final String LOADALLEFFECTS = "SELECT * FROM TRIGGEREFFECTS";
/*     */   
/*  48 */   private static final Map<Integer, TriggerEffect> effects = new ConcurrentHashMap<>();
/*     */   
/*     */   public static final int SHOW_ALL = 0;
/*     */   
/*     */   public static final int SHOW_LINKED = 1;
/*     */   
/*     */   public static final int SHOW_UNLINKED = 2;
/*     */   
/*     */   static {
/*     */     try {
/*  58 */       loadAllTriggerEffects();
/*     */     }
/*  60 */     catch (Exception ex) {
/*     */       
/*  62 */       logger.log(Level.WARNING, "Problems loading all Trigger Effects", ex);
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
/*     */   public static void addTriggerEffect(TriggerEffect effect) {
/*  76 */     effects.put(Integer.valueOf(effect.getId()), effect);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumEffects() {
/*  81 */     return effects.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public static TriggerEffect[] getAllEffects() {
/*  86 */     return (TriggerEffect[])effects.values().toArray((Object[])new TriggerEffect[effects.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static TriggerEffect[] getEffectsForTrigger(int triggerId, boolean incInactive) {
/*  91 */     return Triggers2Effects.getEffectsForTrigger(triggerId, incInactive);
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
/*     */   public static TriggerEffect[] getEffectsForMission(int missionId) {
/* 106 */     Set<TriggerEffect> effs = new HashSet<>();
/* 107 */     for (TriggerEffect effect : effects.values()) {
/*     */       
/* 109 */       if (effect.getMissionId() == missionId)
/* 110 */         effs.add(effect); 
/*     */     } 
/* 112 */     return effs.<TriggerEffect>toArray(new TriggerEffect[effs.size()]);
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
/*     */   public static TriggerEffect[] getFilteredEffects(MissionTrigger[] trigs, Creature creature, int linked, boolean incInactive, boolean dontListMine, boolean listMineOnly, long listForUser, boolean showAll) {
/* 132 */     Set<TriggerEffect> effs = new HashSet<>();
/* 133 */     for (TriggerEffect effect : effects.values()) {
/*     */       
/* 135 */       boolean found = showAll;
/* 136 */       if (!found)
/*     */       {
/* 138 */         for (MissionTrigger trig : trigs) {
/*     */           
/* 140 */           if (Triggers2Effects.hasLink(trig.getId(), effect.getId())) {
/*     */             
/* 142 */             found = true;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 147 */       if (found)
/*     */       {
/* 149 */         if (canShow(effect, creature, linked, incInactive, dontListMine, listMineOnly, listForUser))
/* 150 */           effs.add(effect); 
/*     */       }
/*     */     } 
/* 153 */     return effs.<TriggerEffect>toArray(new TriggerEffect[effs.size()]);
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
/*     */   public static TriggerEffect[] getFilteredEffects(MissionTrigger[] trigs, Creature creature, int linked, boolean incInactive, boolean dontListMine, boolean listMineOnly, long listForUser, int missionId) {
/* 165 */     Set<TriggerEffect> effs = new HashSet<>();
/* 166 */     for (TriggerEffect effect : effects.values()) {
/*     */       
/* 168 */       if (effect.getMissionId() == missionId) {
/*     */         
/* 170 */         boolean found = false;
/* 171 */         for (MissionTrigger trig : trigs) {
/*     */           
/* 173 */           if (Triggers2Effects.hasLink(trig.getId(), effect.getId())) {
/*     */             
/* 175 */             found = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 179 */         if (!found)
/*     */         {
/* 181 */           if (canShow(effect, creature, linked, incInactive, dontListMine, listMineOnly, listForUser))
/* 182 */             effs.add(effect); 
/*     */         }
/*     */       } 
/*     */     } 
/* 186 */     return effs.<TriggerEffect>toArray(new TriggerEffect[effs.size()]);
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
/*     */   public static TriggerEffect[] getFilteredEffects(Creature creature, int linked, boolean incInactive, boolean dontListMine, boolean listMineOnly, long listForUser) {
/* 202 */     Set<TriggerEffect> effs = new HashSet<>();
/* 203 */     for (TriggerEffect effect : effects.values()) {
/*     */       
/* 205 */       if (canShow(effect, creature, linked, incInactive, dontListMine, listMineOnly, listForUser))
/* 206 */         effs.add(effect); 
/*     */     } 
/* 208 */     return effs.<TriggerEffect>toArray(new TriggerEffect[effs.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canShow(TriggerEffect effect, Creature creature, int linked, boolean incInactive, boolean dontListMine, boolean listMineOnly, long listForUser) {
/* 214 */     boolean own = (effect.getOwnerId() == creature.getWurmId());
/* 215 */     boolean show = (creature.getPower() > 0 || own);
/* 216 */     boolean userMatch = (effect.getOwnerId() == listForUser);
/* 217 */     if (own) {
/*     */       
/* 219 */       if (dontListMine) {
/* 220 */         show = false;
/*     */       }
/* 222 */     } else if (listMineOnly) {
/*     */       
/* 224 */       show = false;
/* 225 */       if (listForUser != -10L && userMatch) {
/* 226 */         show = true;
/*     */       }
/* 228 */     } else if (listForUser != -10L) {
/*     */       
/* 230 */       show = false;
/* 231 */       if (userMatch)
/* 232 */         show = true; 
/*     */     } 
/* 234 */     if (effect.getCreatorType() == 2 && creature.getPower() < MissionManager.CAN_SEE_EPIC_MISSIONS)
/* 235 */       show = false; 
/* 236 */     if (show)
/*     */     {
/* 238 */       switch (linked) {
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 243 */           show = (effect.getMissionId() != 0);
/*     */           break;
/*     */         case 2:
/* 246 */           show = (effect.getMissionId() == 0);
/*     */           break;
/*     */       } 
/*     */     }
/* 250 */     return show;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean removeEffect(int id) {
/* 255 */     return (effects.remove(Integer.valueOf(id)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static TriggerEffect getTriggerEffect(int id) {
/* 260 */     return effects.get(Integer.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void destroyEffectsForTrigger(int triggerId) {
/* 266 */     TriggerEffect[] tes = Triggers2Effects.getEffectsForTrigger(triggerId, true);
/* 267 */     for (TriggerEffect mt : tes) {
/*     */ 
/*     */       
/* 270 */       MissionTrigger trig = MissionTriggers.getTriggerWithId(triggerId);
/* 271 */       if (trig != null)
/*     */       {
/* 273 */         if ((mt.destroysTarget() || WurmId.getType(trig.getTarget()) == 1) && mt
/* 274 */           .getCreatorType() != 3) {
/* 275 */           mt.destroyTarget(trig.getTarget());
/*     */         }
/*     */       }
/* 278 */       if (tes.length == 1) {
/*     */         
/* 280 */         removeEffect(mt.getId());
/* 281 */         mt.destroy();
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadAllTriggerEffects() {
/* 307 */     Connection dbcon = null;
/* 308 */     PreparedStatement ps = null;
/* 309 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 312 */       dbcon = DbConnector.getPlayerDbCon();
/* 313 */       ps = dbcon.prepareStatement("SELECT * FROM TRIGGEREFFECTS");
/* 314 */       rs = ps.executeQuery();
/* 315 */       int mid = -10;
/* 316 */       while (rs.next())
/*     */       {
/* 318 */         mid = rs.getInt("ID");
/* 319 */         TriggerEffect m = new TriggerEffect();
/* 320 */         m.setId(mid);
/* 321 */         m.setName(rs.getString("NAME"));
/* 322 */         m.setDescription(rs.getString("DESCRIPTION"));
/* 323 */         m.setRewardItem(rs.getInt("REWARDITEM"));
/* 324 */         m.setRewardNumbers(rs.getInt("REWARDITEMNUMBERS"));
/* 325 */         m.setRewardQl(rs.getInt("REWARDQUALITY"));
/* 326 */         m.setRewardByteValue(rs.getByte("REWARDBYTE"));
/* 327 */         m.setExistingItemReward(rs.getLong("EXISTINGREWARDITEMID"));
/* 328 */         m.setRewardTargetContainerId(rs.getLong("REWARDTARGETCONTAINERID"));
/* 329 */         m.setRewardSkillNum(rs.getInt("REWARDSKILLNUM"));
/* 330 */         m.setRewardSkillVal(rs.getFloat("REWARDSKILLVAL"));
/* 331 */         m.setSpecialEffect(rs.getInt("SPECIALEFFECTID"));
/* 332 */         int triggerId = rs.getInt("TRIGGERID");
/*     */         
/* 334 */         if (triggerId > 0) {
/* 335 */           Triggers2Effects.addLink(triggerId, mid, false);
/*     */         }
/* 337 */         m.setSoundName(rs.getString("SOUND"));
/* 338 */         m.setTextDisplayed(rs.getString("TEXT"));
/* 339 */         m.setTopText(rs.getString("TOP"));
/* 340 */         m.setMission(rs.getInt("MISSION"));
/* 341 */         m.setMissionStateChange(rs.getFloat("MISSIONSTATECHANGE"));
/* 342 */         m.setInactive(rs.getBoolean("INACTIVE"));
/* 343 */         m.setLastModifierName(rs.getString("LASTMODIFIER"));
/* 344 */         m.setDestroysTarget(rs.getBoolean("DESTROYTARGET"));
/* 345 */         m.setCreatorName(rs.getString("CREATOR"));
/* 346 */         m.setCreatedDate(rs.getString("CREATEDDATE"));
/* 347 */         m.setLastModifierName(rs.getString("LASTMODIFIER"));
/* 348 */         Timestamp st = new Timestamp(System.currentTimeMillis());
/*     */         
/*     */         try {
/* 351 */           st = new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(rs.getString("LASTMODIFIEDDATE")).getTime());
/*     */         }
/* 353 */         catch (Exception ex) {
/*     */           
/* 355 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */         } 
/* 357 */         m.setLastModifiedDate(st);
/* 358 */         m.setCreatorType(rs.getByte("CREATORTYPE"));
/* 359 */         m.setOwnerId(rs.getLong("CREATORID"));
/* 360 */         m.setItemMaterial(rs.getByte("ITEMMATERIAL"));
/* 361 */         m.setNewbieItem(rs.getBoolean("NEWBIE"));
/*     */         
/* 363 */         m.setModifyTileX(rs.getInt("MODIFYTILEX"));
/* 364 */         m.setModifyTileY(rs.getInt("MODIFYTILEY"));
/* 365 */         m.setNewTileType(rs.getInt("NEWTILETYPE"));
/* 366 */         m.setNewTileData(rs.getByte("NEWTILEDATA"));
/* 367 */         m.setSpawnTileX(rs.getInt("SPAWNTILEX"));
/* 368 */         m.setSpawnTileY(rs.getInt("SPAWNTILEY"));
/* 369 */         m.setCreatureSpawn(rs.getInt("CREATURESPAWN"));
/* 370 */         m.setCreatureAge(rs.getInt("CREATUREAGE"));
/* 371 */         m.setCreatureType(rs.getByte("CREATURE_TYPE"));
/* 372 */         m.setCreatureName(rs.getString("CREATURE_NAME"));
/* 373 */         m.setTeleportX(rs.getInt("TELEPORTX"));
/* 374 */         m.setTeleportY(rs.getInt("TELEPORTY"));
/* 375 */         m.setTeleportLayer(rs.getInt("TELEPORTLAYER"));
/* 376 */         m.setMissionToActivate(rs.getInt("MISSIONACTIVATED"));
/* 377 */         m.setMissionToDeActivate(rs.getInt("MISSIONDEACTIVATED"));
/*     */         
/* 379 */         m.setWindowSizeX(rs.getInt("WSZX"));
/* 380 */         m.setWindowSizeY(rs.getInt("WSZY"));
/* 381 */         m.setStartSkillgain(rs.getBoolean("STARTSKILLGAIN"));
/* 382 */         m.setStopSkillgain(rs.getBoolean("STOPSKILLGAIN"));
/* 383 */         m.setDestroyInventory(rs.getBoolean("DESTROYITEMS"));
/* 384 */         addTriggerEffect(m);
/*     */       }
/*     */     
/* 387 */     } catch (SQLException sqx) {
/*     */       
/* 389 */       logger.log(Level.WARNING, sqx.getMessage());
/*     */     }
/*     */     finally {
/*     */       
/* 393 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 394 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\TriggerEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
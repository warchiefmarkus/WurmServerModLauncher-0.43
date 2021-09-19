/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class MissionTargets
/*     */   implements CounterTypes
/*     */ {
/*  37 */   private static final Map<Long, MissionTarget> missionTargets = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  41 */   private static final Logger logger = Logger.getLogger(MissionTargets.class.getName());
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
/*     */   public static void destroyMissionTarget(long missionTarget, boolean destroyTriggers) {
/*  61 */     MissionTarget m = missionTargets.remove(Long.valueOf(missionTarget));
/*  62 */     if (m != null && destroyTriggers)
/*     */     {
/*  64 */       m.destroy();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isMissionTarget(long potentialTarget) {
/*  70 */     return missionTargets.containsKey(Long.valueOf(potentialTarget));
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTarget getMissionTargetFor(long potentialTarget) {
/*  75 */     return missionTargets.get(Long.valueOf(potentialTarget));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Long[] getTargetIds() {
/*  80 */     return (Long[])missionTargets.keySet().toArray((Object[])new Long[missionTargets.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean destroyStructureTargets(long structureId, @Nullable String possibleCreatorName) {
/*  85 */     boolean found = false;
/*  86 */     Long[] targs = getTargetIds();
/*  87 */     for (Long tid : targs) {
/*     */       
/*  89 */       if (tid != null) {
/*     */         
/*  91 */         long targetId = tid.longValue();
/*  92 */         if (WurmId.getType(targetId) == 5) {
/*     */           
/*  94 */           Wall w = Wall.getWall(targetId);
/*  95 */           if (w != null)
/*     */           {
/*  97 */             if (w.getStructureId() == structureId) {
/*     */               
/*  99 */               MissionTarget mt = getMissionTargetFor(targetId);
/* 100 */               if (mt != null) {
/*     */                 
/* 102 */                 MissionTrigger[] mits = mt.getMissionTriggers();
/* 103 */                 for (MissionTrigger missionT : mits) {
/*     */                   
/* 105 */                   if (possibleCreatorName == null || missionT
/* 106 */                     .getCreatorName().toLowerCase().equals(possibleCreatorName)) {
/*     */                     
/* 108 */                     found = true;
/* 109 */                     missionT.destroy();
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 118 */     return found;
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
/*     */   public static void addMissionTrigger(MissionTrigger trigger) {
/* 151 */     MissionTarget mt = getMissionTargetFor(trigger.getTarget());
/* 152 */     if (mt == null && trigger.getTarget() > 0L) {
/*     */       
/* 154 */       mt = new MissionTarget(trigger.getTarget());
/* 155 */       missionTargets.put(Long.valueOf(trigger.getTarget()), mt);
/*     */     } 
/* 157 */     if (mt != null) {
/* 158 */       mt.addMissionTrigger(trigger);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeMissionTrigger(MissionTrigger trigger, boolean destroyAllTriggers) {
/* 167 */     if (trigger != null) {
/*     */       
/* 169 */       MissionTarget mt = getMissionTargetFor(trigger.getTarget());
/*     */       
/* 171 */       if (mt != null) {
/*     */         
/* 173 */         mt.removeMissionTrigger(trigger);
/* 174 */         if (mt.getNumTriggers() == 0)
/* 175 */           destroyMissionTarget(mt.getId(), destroyAllTriggers); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\MissionTargets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
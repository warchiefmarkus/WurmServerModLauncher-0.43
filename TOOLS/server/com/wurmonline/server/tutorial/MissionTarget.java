/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public final class MissionTarget
/*     */ {
/*  31 */   private final Set<MissionTrigger> missionTriggers = new HashSet<>();
/*     */ 
/*     */ 
/*     */   
/*  35 */   private static final Logger logger = Logger.getLogger(MissionTarget.class.getName());
/*     */ 
/*     */   
/*     */   private final long id;
/*     */ 
/*     */ 
/*     */   
/*     */   MissionTarget(long targetId) {
/*  43 */     this.id = targetId;
/*     */   }
/*     */ 
/*     */   
/*     */   long getId() {
/*  48 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addMissionTrigger(MissionTrigger missionReqs) {
/*  56 */     if (missionReqs != null) {
/*  57 */       this.missionTriggers.add(missionReqs);
/*     */     }
/*     */   }
/*     */   
/*     */   void removeMissionTrigger(MissionTrigger missionReqs) {
/*  62 */     this.missionTriggers.remove(missionReqs);
/*     */   }
/*     */ 
/*     */   
/*     */   int getNumTriggers() {
/*  67 */     return this.missionTriggers.size();
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
/*     */   private MissionTrigger getMissionTrigger(int mission, int state, boolean checkActive) {
/*  85 */     for (MissionTrigger mr : this.missionTriggers) {
/*     */       
/*  87 */       if (mr.getMissionRequired() == mission && mr.isTriggered(state, checkActive))
/*     */       {
/*  89 */         return mr;
/*     */       }
/*     */     } 
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MissionTrigger[] getMissionTriggers() {
/*  97 */     return this.missionTriggers.<MissionTrigger>toArray(new MissionTrigger[this.missionTriggers.size()]);
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
/*     */   boolean isMissionFulfilled(int mission, byte state) {
/* 109 */     for (MissionTrigger mr : this.missionTriggers) {
/*     */       
/* 111 */       if (mr.getMissionRequired() == mission && mr.getStateRequired() == state)
/* 112 */         return true; 
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void destroy() {
/* 122 */     MissionTriggers.destroyTriggersForTarget(this.id);
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
/* 133 */     return "MissionTarget [id=" + this.id + ", missionTriggers=" + this.missionTriggers + ", numTriggers=" + getNumTriggers() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\MissionTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
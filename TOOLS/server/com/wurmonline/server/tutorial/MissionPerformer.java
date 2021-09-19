/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.epic.EpicMission;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class MissionPerformer
/*     */   implements MiscConstants
/*     */ {
/*  37 */   private final Map<Integer, MissionPerformed> missionsPerformed = new HashMap<>();
/*     */   
/*  39 */   private static final Logger logger = Logger.getLogger(MissionPerformer.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private final long wurmid;
/*     */ 
/*     */ 
/*     */   
/*     */   public MissionPerformer(long performerId) {
/*  48 */     this.wurmid = performerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWurmId() {
/*  53 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */   
/*     */   void addMissionPerformed(MissionPerformed mp) {
/*  58 */     this.missionsPerformed.put(Integer.valueOf(mp.getMissionId()), mp);
/*     */   }
/*     */ 
/*     */   
/*     */   public MissionPerformed getMission(int mission) {
/*  63 */     return this.missionsPerformed.get(Integer.valueOf(mission));
/*     */   }
/*     */ 
/*     */   
/*     */   public MissionPerformed[] getAllMissionsPerformed() {
/*  68 */     return (MissionPerformed[])this.missionsPerformed.values().toArray((Object[])new MissionPerformed[this.missionsPerformed.values().size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public MissionPerformed getMissionByWurmId(long wurmId) {
/*  73 */     for (MissionPerformed mp : this.missionsPerformed.values()) {
/*     */       
/*  75 */       if (mp.getWurmId() == wurmId)
/*  76 */         return mp; 
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isMissionCompleted(int mission) {
/*  83 */     return isMissionPerformed(mission, 100.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isMissionPerformed(int mission, float state) {
/*  88 */     MissionPerformed m = this.missionsPerformed.get(Integer.valueOf(mission));
/*  89 */     if (m != null)
/*  90 */       return (m.getState() >= state); 
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendEpicMissionsPerformed(Creature creature, Communicator comm) {
/*  96 */     EpicMission[] em = null;
/*  97 */     if (!Servers.localServer.PVPSERVER) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       em = EpicServerStatus.getCurrentEpicMissions();
/* 105 */       for (int x = 0; x < em.length; x++)
/*     */       {
/* 107 */         if (em[x].isCurrent()) {
/* 108 */           sendEpicMission(em[x], comm);
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 115 */       em = EpicServerStatus.getCurrentEpicMissions();
/* 116 */       if (em != null)
/*     */       {
/* 118 */         for (int x = 0; x < em.length; x++)
/*     */         {
/* 120 */           sendEpicMissionPvPServer(em[x], creature, comm);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void sendEpicMissionPvPServer(EpicMission mission, Creature creature, Communicator comm) {
/* 129 */     Mission mis = Missions.getMissionWithId(mission.getMissionId());
/* 130 */     if (mis != null && (!mis.isHidden() || comm.player.getPower() >= 2))
/*     */     {
/* 132 */       if (Deities.getFavoredKingdom(mission.getEpicEntityId()) == creature.getKingdomTemplateId()) {
/*     */         
/* 134 */         String name = "Everyone: " + mis.getName();
/* 135 */         if (comm.player.getPower() >= 2)
/* 136 */           name = "(" + mis.getId() + ") " + name; 
/* 137 */         if (mis.isHidden())
/* 138 */           name = "[hidden] " + name; 
/* 139 */         comm.sendMissionState(-mission.getMissionId(), name, mis.getInstruction(), mis
/* 140 */             .getMissionCreatorName(), mission
/* 141 */             .getMissionProgress(), mis.getLastModifiedAsLong(), mission.getEndTime(), mission
/* 142 */             .getExpireTime(), false, mis
/* 143 */             .getDifficulty(), mis.getRewards());
/*     */       }
/*     */       else {
/*     */         
/* 147 */         String name = "Enemy: " + mis.getName();
/* 148 */         if (comm.player.getPower() >= 2)
/* 149 */           name = "(" + mis.getId() + ") " + name; 
/* 150 */         if (mis.isHidden())
/* 151 */           name = "[hidden] " + name; 
/* 152 */         comm.sendMissionState(-mission.getMissionId(), name, mis.getInstruction(), mis
/* 153 */             .getMissionCreatorName(), mission
/* 154 */             .getMissionProgress(), mis.getLastModifiedAsLong(), mission.getEndTime(), mission
/* 155 */             .getExpireTime(), false, mis
/* 156 */             .getDifficulty(), mis.getRewards());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendEpicMission(EpicMission mission, Communicator comm) {
/* 163 */     Mission mis = Missions.getMissionWithId(mission.getMissionId());
/* 164 */     if (mis != null && (!mis.isHidden() || comm.player.getPower() >= 2)) {
/*     */       
/* 166 */       String name = "Everyone: " + mis.getName();
/* 167 */       if (comm.player.getPower() >= 2)
/* 168 */         name = "(" + mis.getId() + ") " + name; 
/* 169 */       if (mis.isHidden()) {
/* 170 */         name = "[hidden] " + name;
/*     */       }
/*     */       
/* 173 */       comm.sendMissionState(-mission.getMissionId(), name, mis.getInstruction(), mis
/* 174 */           .getMissionCreatorName(), mission
/* 175 */           .getMissionProgress(), mis.getLastModifiedAsLong(), mission.getEndTime(), mission
/* 176 */           .getExpireTime(), false, mis
/* 177 */           .getDifficulty(), mis.getRewards());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendAllMissionPerformed(Communicator comm) {
/* 183 */     if (comm != null) {
/*     */       
/* 185 */       for (MissionPerformed mp : this.missionsPerformed.values())
/*     */       {
/*     */         
/* 188 */         if ((mp.getState() <= -1.0F || mp.getState() >= 100.0F) && mp
/* 189 */           .getEndTime() < System.currentTimeMillis() - 7257600000L) {
/*     */           continue;
/*     */         }
/* 192 */         if (!mp.isInactivated())
/*     */         {
/* 194 */           Mission mis = Missions.getMissionWithId(mp.getMissionId());
/* 195 */           if (mis != null) {
/*     */             
/* 197 */             if (!mis.isHidden() || comm.player.getPower() >= 2) {
/*     */               
/* 199 */               String name = mis.getName();
/* 200 */               if (comm.player.getPower() >= 2)
/* 201 */                 name = "(" + mis.getId() + ") " + name; 
/* 202 */               if (mis.isHidden())
/* 203 */                 name = "[hidden] " + name; 
/* 204 */               comm.sendMissionState(mp.getWurmId(), name, mis.getInstruction(), mis.getMissionCreatorName(), mp
/* 205 */                   .getState(), mp.getStartTime(), mp.getEndTime(), mp
/* 206 */                   .getFinishTimeAsLong(mis.getMaxTimeSeconds()), mis.hasSecondChance(), mis
/* 207 */                   .getDifficulty(), mis.getRewards());
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/* 212 */           comm.sendMissionState(mp.getWurmId(), "Unknown", "N/A", "N/A", mp.getState(), mp.getStartTime(), mp
/* 213 */               .getEndTime(), 0L, false, (byte)-10, "Unknown");
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 222 */       logger.warning("Could not send Mission state as Communicator was null, MissionPerformer: " + this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void sendUpdatePerformer(MissionPerformed mp) {
/*     */     try {
/* 230 */       Creature perf = Server.getInstance().getCreature(this.wurmid);
/* 231 */       if (mp.isInactivated()) {
/* 232 */         perf.getCommunicator().sendRemoveMissionState(mp.getWurmId());
/*     */       } else {
/*     */         
/* 235 */         Mission mis = Missions.getMissionWithId(mp.getMissionId());
/* 236 */         String name = "Unknown";
/* 237 */         String instruction = "N/A";
/* 238 */         String creatorName = "N/A";
/* 239 */         long maxTime = 0L;
/* 240 */         boolean mayBeRestarted = false;
/* 241 */         byte difficulty = -10;
/* 242 */         String rewards = "Unknown";
/* 243 */         if (mis != null) {
/*     */           
/* 245 */           if (mis.isHidden() && perf.getPower() == 0)
/*     */             return; 
/* 247 */           name = mis.getName();
/* 248 */           if (perf.getPower() >= 2)
/* 249 */             name = "(" + mis.getId() + ") " + name; 
/* 250 */           if (mis.isHidden())
/* 251 */             name = "[hidden] " + name; 
/* 252 */           instruction = mis.getInstruction();
/* 253 */           creatorName = mis.getMissionCreatorName();
/* 254 */           maxTime = mp.getFinishTimeAsLong(mis.getMaxTimeSeconds());
/* 255 */           mayBeRestarted = mis.hasSecondChance();
/* 256 */           difficulty = mis.getDifficulty();
/* 257 */           rewards = mis.getRewards();
/*     */         } 
/* 259 */         perf.getCommunicator().sendMissionState(mp.getWurmId(), name, instruction, creatorName, mp.getState(), mp
/* 260 */             .getStartTime(), mp.getEndTime(), maxTime, mayBeRestarted, difficulty, rewards);
/*     */       }
/*     */     
/* 263 */     } catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */     
/*     */     }
/* 267 */     catch (NoSuchPlayerException noSuchPlayerException) {}
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
/*     */   public String toString() {
/* 281 */     return "MissionPerformer [Number of missions performed=" + this.missionsPerformed.size() + ", performer wurmid=" + this.wurmid + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\MissionPerformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
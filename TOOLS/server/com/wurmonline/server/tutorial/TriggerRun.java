/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.Actions;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.epic.EpicMission;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.questions.SimplePopup;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ final class TriggerRun
/*     */   implements MiscConstants
/*     */ {
/*  40 */   private static Logger logger = Logger.getLogger(TriggerRun.class.getName());
/*     */   private boolean done = false;
/*     */   private boolean triggered = false;
/*     */   private boolean openedDoor = false;
/*  44 */   private MissionTrigger lastTrigger = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void run(Creature performer, MissionTrigger[] trigs, int counter) {
/*  53 */     Set<Integer> doneActions = new HashSet<>();
/*  54 */     this.done = true;
/*  55 */     int maxSeconds = 0;
/*  56 */     if (trigs.length > 0) {
/*     */       
/*  58 */       MissionPerformer mp = MissionPerformed.getMissionPerformer(performer.getWurmId());
/*  59 */       performer.sendToLoggers("Found " + trigs.length + " triggers", (byte)2);
/*  60 */       for (int x = 0; x < trigs.length; x++) {
/*     */         
/*  62 */         if (!trigs[x].isInactive())
/*     */         {
/*  64 */           if (!doneActions.contains(Integer.valueOf(trigs[x].getOnActionPerformed()))) {
/*     */             
/*  66 */             performer.sendToLoggers("Checking " + trigs[x].getName(), (byte)2);
/*  67 */             if (maxSeconds < trigs[x].getSeconds())
/*  68 */               maxSeconds = trigs[x].getSeconds(); 
/*  69 */             boolean skip = false;
/*  70 */             EpicMission mis = EpicServerStatus.getEpicMissionForMission(trigs[x].getMissionRequired());
/*     */             
/*  72 */             if (mis != null) {
/*     */               
/*  74 */               if (mis.isCompleted() || !mis.isCurrent()) {
/*     */                 
/*  76 */                 skip = true;
/*     */                 
/*  78 */                 performer.sendToLoggers("Skipping " + trigs[x].getName() + " because it is not active", (byte)2);
/*     */               } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  85 */               if (!Servers.isThisAChaosServer() && Servers.localServer.PVPSERVER)
/*     */               {
/*  87 */                 if (Deities.getFavoredKingdom(mis.getEpicEntityId()) != performer.getKingdomTemplateId()) {
/*     */                   
/*  89 */                   skip = true;
/*     */                   
/*  91 */                   performer.sendToLoggers("Skipping " + trigs[x].getName() + " because of kingdom.", (byte)2);
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/*  96 */             if (trigs[x].getCreatorType() == 2)
/*     */             {
/*  98 */               if (mis != null)
/*     */               {
/* 100 */                 if (mis.isCompleted()) {
/*     */                   
/* 102 */                   logger.log(Level.INFO, "This code shouldn't be REACHED really since MissionPerformers are set to FINISHED at completion.");
/*     */                   
/* 104 */                   if (mp != null) {
/*     */                     
/* 106 */                     MissionPerformed mperf = mp.getMission(trigs[x].getMissionRequired());
/* 107 */                     if (mperf != null)
/*     */                     {
/* 109 */                       if (mperf.isStarted())
/*     */                       {
/* 111 */                         mperf.setState(100.0F, performer.getWurmId());
/*     */                       }
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             }
/* 118 */             if (!skip) {
/*     */               
/* 120 */               if (maxSeconds > 0) {
/* 121 */                 this.done = false;
/*     */               }
/* 123 */               if (maxSeconds > 0 && counter == 0)
/*     */               {
/* 125 */                 if (x == trigs.length - 1)
/* 126 */                   performer.sendActionControl("activating", true, maxSeconds * 10); 
/*     */               }
/* 128 */               if (trigs[x].getSeconds() == counter || (counter == 1 && trigs[x].getSeconds() == 0)) {
/*     */                 
/* 130 */                 Mission mission = Missions.getMissionWithId(trigs[x].getMissionRequired());
/* 131 */                 if (mission != null && !mission.isInactive()) {
/*     */                   
/* 133 */                   performer.sendToLoggers("Triggered " + trigs[x].getSeconds() + " counter=" + counter, (byte)2);
/*     */                   
/* 135 */                   boolean sendStartPopup = false;
/*     */                   
/* 137 */                   if (trigs[x].getStateRequired() == 0.0F)
/*     */                   {
/* 139 */                     if (mp == null) {
/*     */                       
/* 141 */                       mp = MissionPerformed.startNewMission(trigs[x].getMissionRequired(), performer
/* 142 */                           .getWurmId(), 1.0F);
/* 143 */                       sendStartPopup = true;
/* 144 */                       performer.sendToLoggers("Starting mission. No existing performer.", (byte)2);
/*     */                     }
/*     */                     else {
/*     */                       
/* 148 */                       MissionPerformed mperf = mp.getMission(trigs[x].getMissionRequired());
/* 149 */                       if (mperf == null) {
/*     */                         
/* 151 */                         MissionPerformed.startNewMission(trigs[x].getMissionRequired(), performer
/* 152 */                             .getWurmId(), 1.0F);
/* 153 */                         sendStartPopup = true;
/* 154 */                         performer
/* 155 */                           .sendToLoggers("Starting mission. No existing mission.", (byte)2);
/*     */                       } 
/*     */                     } 
/*     */                   }
/* 159 */                   if (mp != null)
/*     */                   {
/* 161 */                     performer.sendToLoggers("Checking for performer state. Trigger is done=" + this.done, (byte)2);
/*     */                     
/* 163 */                     runEffect(performer, mp, trigs[x].getMissionRequired(), trigs[x], sendStartPopup, doneActions);
/*     */                   }
/*     */                 
/*     */                 } else {
/*     */                   
/* 168 */                   performer.sendToLoggers("Mission is inactive or null. Trigger is done=" + this.done, (byte)2);
/*     */                 } 
/*     */               } 
/* 171 */               if (x == trigs.length - 1)
/*     */               {
/* 173 */                 if (counter == maxSeconds) {
/* 174 */                   this.done = true;
/*     */                 }
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void runEffect(Creature performer, MissionPerformer mp, int missionId, MissionTrigger trigger, boolean sendStartPopup, Set<Integer> doneActions) {
/* 186 */     if (!performer.isPlayer())
/*     */       return; 
/* 188 */     MissionPerformed mperf = mp.getMission(missionId);
/*     */     
/* 190 */     Mission mission = Missions.getMissionWithId(missionId);
/*     */     
/* 192 */     if (mperf != null && (mperf.isStarted() || mperf.isFailed()))
/*     */     {
/* 194 */       if (mission != null) {
/*     */ 
/*     */         
/* 197 */         boolean secondChance = ((mperf.isFailed() && mission.hasSecondChance()) || (mperf.isCompleted() && mission.mayBeRestarted()));
/* 198 */         if (secondChance)
/*     */         {
/* 200 */           if (trigger.getStateRequired() == 0.0F) {
/*     */             
/* 202 */             mperf.setState(1.0F, performer.getWurmId());
/* 203 */             sendStartPopup = true;
/*     */           } 
/*     */         }
/* 206 */         if (!mperf.isFailed())
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 211 */           performer.sendToLoggers("Mission " + mission.getName() + " is not failed or may be restarted", (byte)2);
/*     */ 
/*     */           
/* 214 */           boolean epicMissionTrigger = false;
/* 215 */           boolean setEpicFinished = false;
/* 216 */           EpicMission mis = EpicServerStatus.getEpicMissionForMission(missionId);
/*     */           
/* 218 */           if (mis != null)
/*     */           {
/* 220 */             if (mis.getMissionProgress() < 100.0F)
/*     */             {
/* 222 */               if (mperf.getState() != 100.0F) {
/*     */                 
/* 224 */                 epicMissionTrigger = true;
/* 225 */                 setEpicFinished = !Actions.isMultipleRunEpicAction(trigger.getOnActionPerformed());
/* 226 */                 performer.sendToLoggers("Mission " + mission.getName() + " is epicmissiontrigger. Not finished " + setEpicFinished, (byte)2);
/*     */               } 
/*     */             }
/*     */           }
/*     */           
/* 231 */           if (mission.getMaxTimeSeconds() > 0 && 
/* 232 */             System.currentTimeMillis() > mperf.getFinishTimeAsLong(mission.getMaxTimeSeconds())) {
/*     */             
/* 234 */             mperf.setState(-1.0F, performer.getWurmId());
/* 235 */             String miss = Server.getTimeFor(System.currentTimeMillis() - mperf
/* 236 */                 .getFinishTimeAsLong(mission.getMaxTimeSeconds()));
/*     */             
/* 238 */             SimplePopup pop = new SimplePopup(performer, "Mission failed", "You failed " + mission.getName() + ". You are " + miss + " late.");
/*     */             
/* 240 */             pop.sendQuestion();
/*     */           }
/* 242 */           else if ((epicMissionTrigger && trigger.getOnActionPerformed() != 47) || trigger
/* 243 */             .isTriggered(mperf.getState(), true) || (sendStartPopup && mperf
/* 244 */             .getState() == 1.0F && trigger.getStateRequired() == 0.0F)) {
/*     */             
/* 246 */             performer.sendToLoggers("Proper state achieved for mission " + trigger.getMissionRequired(), (byte)2);
/*     */             
/* 248 */             TriggerEffect[] eff = TriggerEffects.getEffectsForTrigger(trigger.getId(), true);
/* 249 */             for (int e = 0; e < eff.length; e++) {
/*     */               
/* 251 */               eff[e].effect(performer, mperf, trigger.getTarget(), setEpicFinished, 
/* 252 */                   (trigger.getOnActionPerformed() != 142));
/* 253 */               doneActions.add(Integer.valueOf(trigger.getOnActionPerformed()));
/* 254 */               this.triggered = true;
/* 255 */               if (eff[e].getSpecialEffectId() == 1) {
/* 256 */                 this.openedDoor = true;
/*     */               }
/*     */             } 
/*     */             
/* 260 */             if (this.triggered && trigger.getOnActionPerformed() == 47) {
/*     */               
/* 262 */               this.lastTrigger = trigger;
/*     */               
/*     */               return;
/*     */             } 
/* 266 */           } else if (epicMissionTrigger && trigger.getOnActionPerformed() == 47) {
/*     */ 
/*     */             
/* 269 */             SimplePopup pop = new SimplePopup(performer, "Already done", "No effect" + mission.getName() + ". You have already shown your respect.");
/* 270 */             pop.sendQuestion();
/*     */           } 
/* 272 */           if (sendStartPopup)
/*     */           {
/* 274 */             if (mission.getInstruction() != null && mission.getInstruction().length() > 0)
/*     */             {
/* 276 */               if (mperf.getState() != 100.0F) {
/*     */ 
/*     */                 
/* 279 */                 SimplePopup pop = new SimplePopup(performer, "Mission start", mission.getInstruction());
/* 280 */                 pop.sendQuestion();
/*     */               } else {
/*     */                 
/* 283 */                 performer.sendToLoggers("Not sending mission start popup for mission " + trigger
/* 284 */                     .getMissionRequired() + " since it is already finished.", (byte)2);
/*     */               }
/*     */             
/*     */             }
/*     */           }
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 293 */         trigger.setInactive(true);
/* 294 */         trigger.update();
/* 295 */         if (performer.getPower() > 0) {
/* 296 */           performer.getCommunicator().sendNormalServerMessage("The trigger " + trigger
/* 297 */               .getName() + " was made inactive because it was not connected to any mission.");
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isDone() {
/* 309 */     return this.done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isTriggered() {
/* 317 */     return this.triggered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isOpenedDoor() {
/* 325 */     return this.openedDoor;
/*     */   }
/*     */ 
/*     */   
/*     */   MissionTrigger getLastTrigger() {
/* 330 */     return this.lastTrigger;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\TriggerRun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ final class PlanetBehaviour
/*     */   extends Behaviour
/*     */   implements MiscConstants
/*     */ {
/*     */   PlanetBehaviour() {
/*  41 */     super((short)36);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item object, int planetId) {
/*  52 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  53 */     toReturn.addAll(super.getBehavioursFor(performer, object, planetId));
/*  54 */     if (performer instanceof com.wurmonline.server.players.Player && object.getTemplateId() == 903)
/*  55 */       toReturn.add(Actions.actionEntrys[118]); 
/*  56 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int planetId) {
/*  67 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  68 */     toReturn.addAll(super.getBehavioursFor(performer, planetId));
/*  69 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action action, Creature performer, int planetId, short num, float counter) {
/*  80 */     if (num == 1)
/*     */     {
/*  82 */       if (planetId == 0) {
/*     */         
/*  84 */         performer.getCommunicator().sendNormalServerMessage("You see Jackal, stealer of souls and flowing with the blood of the damned.");
/*     */       }
/*  86 */       else if (planetId == 1) {
/*     */         
/*  88 */         performer.getCommunicator().sendNormalServerMessage("You see Valrei, home of the Gods.");
/*     */       }
/*  90 */       else if (planetId == 2) {
/*     */         
/*  92 */         performer.getCommunicator().sendNormalServerMessage("You see Seris, home of the Dead.");
/*     */       }
/*  94 */       else if (planetId == 3) {
/*     */         
/*  96 */         performer.getCommunicator().sendNormalServerMessage("You see Haven. Rumours have it that this is where Golden Valley lies.");
/*     */       }
/*  98 */       else if (planetId == 99) {
/*     */         
/* 100 */         performer.getCommunicator().sendNormalServerMessage("You see Sol, home of the demons.");
/*     */       } 
/*     */     }
/* 103 */     return true;
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
/*     */   public boolean action(Action action, Creature performer, Item source, int planetId, short num, float counter) {
/* 115 */     if (num == 1)
/* 116 */       return action(action, performer, planetId, num, counter); 
/* 117 */     if (num == 118) {
/*     */       
/* 119 */       boolean done = false;
/* 120 */       if (!performer.isOnSurface()) {
/*     */         
/* 122 */         performer.getCommunicator().sendNormalServerMessage("You must be on the surface for astral navigation.");
/* 123 */         done = true;
/*     */       }
/*     */       else {
/*     */         
/* 127 */         boolean insta = (Servers.localServer.testServer && performer.getPower() > 1);
/*     */         
/* 129 */         int timePerStage = insta ? 2 : 10;
/* 130 */         int time = timePerStage * 30;
/* 131 */         if (counter == 1.0F) {
/*     */           
/* 133 */           action.setTimeLeft(time);
/* 134 */           performer.sendActionControl(Actions.actionEntrys[118].getVerbString(), true, time);
/* 135 */           performer.getCommunicator().sendNormalServerMessage("You carefully place the dioptra on its tripod in front of you.");
/* 136 */           Server.getInstance().broadCastAction(performer.getName() + " starts to work out where they are.", performer, 5);
/* 137 */           action.setTickCount(0);
/*     */         } else {
/*     */           
/* 140 */           time = action.getTimeLeft();
/*     */         } 
/* 142 */         if (action.currentSecond() % timePerStage == 0) {
/*     */           
/* 144 */           action.incTickCount();
/* 145 */           String pMsg = "";
/* 146 */           String bMsg = "";
/* 147 */           switch (action.getTickCount()) {
/*     */             
/*     */             case 1:
/* 150 */               pMsg = "You make sure the dioptra is level.";
/* 151 */               bMsg = performer.getName() + " levels a dioptra.";
/*     */               break;
/*     */             
/*     */             case 2:
/* 155 */               pMsg = "You line up the dioptra with " + getName(planetId) + ".";
/* 156 */               bMsg = performer.getName() + " points the dioptra.";
/*     */               break;
/*     */             
/*     */             case 3:
/* 160 */               pMsg = "You work out you are in the " + EpicServerStatus.getAreaString(performer.getTileX(), performer.getTileY()) + ".";
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 166 */           if (pMsg.length() > 0)
/* 167 */             performer.getCommunicator().sendNormalServerMessage(pMsg); 
/* 168 */           if (bMsg.length() > 0) {
/* 169 */             Server.getInstance().broadCastAction(bMsg, performer, 5);
/*     */           }
/* 171 */           if (action.getTickCount() >= 3 || done) {
/*     */             
/* 173 */             performer.getCommunicator().sendNormalServerMessage("You pack up the dioptra.");
/* 174 */             Server.getInstance().broadCastAction(performer.getName() + " packs up a dioptra.", performer, 5);
/* 175 */             done = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 179 */       return done;
/*     */     } 
/* 181 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static final String getName(int planetId) {
/* 186 */     if (planetId == 0)
/*     */     {
/* 188 */       return "Jackal";
/*     */     }
/* 190 */     if (planetId == 1)
/*     */     {
/* 192 */       return "Valrei";
/*     */     }
/* 194 */     if (planetId == 2)
/*     */     {
/* 196 */       return "Seris";
/*     */     }
/* 198 */     if (planetId == 3)
/*     */     {
/* 200 */       return "Haven";
/*     */     }
/* 202 */     if (planetId == 99)
/*     */     {
/* 204 */       return "Sol";
/*     */     }
/* 206 */     return "unknown";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\PlanetBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.questions.EntityMoveQuestion;
/*     */ import com.wurmonline.server.questions.SelectSpellQuestion;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ class DomainItemBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*     */   DomainItemBehaviour() {
/*  43 */     super((short)33);
/*     */   }
/*     */ 
/*     */   
/*     */   DomainItemBehaviour(short type) {
/*  48 */     super(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  59 */     List<ActionEntry> toReturn = new ArrayList<>();
/*  60 */     toReturn.addAll(super.getBehavioursFor(performer, target));
/*  61 */     boolean reachable = true;
/*  62 */     if (target.getOwnerId() == -10L) {
/*     */       
/*  64 */       reachable = false;
/*  65 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*     */         
/*  67 */         BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  68 */         if (result == null)
/*     */         {
/*  70 */           reachable = true;
/*     */         }
/*     */       } 
/*     */     } 
/*  74 */     if (reachable) {
/*     */       
/*  76 */       if (Servers.localServer.EPIC)
/*     */       {
/*  78 */         if (performer.getDeity() != null)
/*     */         {
/*  80 */           toReturn.add(Actions.actionEntrys[610]);
/*     */         }
/*     */       }
/*  83 */       toReturn.add(Actions.actionEntrys[141]);
/*     */       
/*  85 */       if (performer.getFaith() >= 10.0F)
/*     */       {
/*  87 */         if (target.getBless() != null) {
/*     */           
/*  89 */           toReturn.add(Actions.actionEntrys[142]);
/*  90 */           toReturn.add(Actions.actionEntrys[143]);
/*  91 */           if (performer.isPriest()) {
/*  92 */             toReturn.add(Actions.actionEntrys[452]);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*  97 */     return toReturn;
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
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/* 109 */     List<ActionEntry> toReturn = new ArrayList<>();
/* 110 */     toReturn.addAll(super.getBehavioursFor(performer, source, target));
/* 111 */     boolean reachable = true;
/* 112 */     if (target.getOwnerId() == -10L) {
/*     */       
/* 114 */       reachable = false;
/* 115 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F)) {
/*     */         
/* 117 */         BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/* 118 */         if (result == null)
/*     */         {
/* 120 */           reachable = true;
/*     */         }
/*     */       } 
/*     */     } 
/* 124 */     if (reachable) {
/*     */       
/* 126 */       if (Servers.localServer.EPIC)
/*     */       {
/* 128 */         if (performer.getDeity() != null)
/*     */         {
/* 130 */           toReturn.add(Actions.actionEntrys[610]);
/*     */         }
/*     */       }
/* 133 */       toReturn.add(Actions.actionEntrys[141]);
/*     */       
/* 135 */       if (performer.getFaith() >= 10.0F) {
/*     */         
/* 137 */         if (target.getBless() != null) {
/*     */           
/* 139 */           toReturn.add(Actions.actionEntrys[142]);
/* 140 */           toReturn.add(Actions.actionEntrys[143]);
/*     */         } 
/* 142 */         if (performer.isPriest()) {
/*     */           
/* 144 */           toReturn.add(Actions.actionEntrys[452]);
/* 145 */           if (source.isHolyItem()) {
/* 146 */             toReturn.add(Actions.actionEntrys[216]);
/*     */           }
/*     */         } 
/*     */       } 
/* 150 */       if (source.isRechargeable())
/*     */       {
/* 152 */         toReturn.add(Actions.actionEntrys[370]);
/*     */       }
/*     */     } 
/* 155 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 166 */     boolean done = true;
/* 167 */     if (action == 141) {
/* 168 */       done = MethodsReligion.pray(act, performer, target, counter);
/* 169 */     } else if (action == 142) {
/*     */       
/* 171 */       if (performer.getFaith() >= 10.0F) {
/* 172 */         done = MethodsReligion.sacrifice(act, performer, target);
/*     */       } else {
/* 174 */         done = true;
/*     */       } 
/* 176 */     } else if (action == 143) {
/*     */       
/* 178 */       if (performer.getFaith() >= 10.0F) {
/* 179 */         done = MethodsReligion.desecrate(act, performer, null, target);
/*     */       } else {
/* 181 */         done = true;
/*     */       } 
/* 183 */     } else if (action == 452) {
/*     */       
/* 185 */       if (performer.isPriest())
/*     */       {
/* 187 */         done = true;
/*     */         
/* 189 */         SelectSpellQuestion spq = new SelectSpellQuestion(performer, "Spell set", "These are your available spells", target.getWurmId());
/* 190 */         spq.sendQuestion();
/*     */       }
/*     */     
/* 193 */     } else if (action == 610) {
/*     */       
/* 195 */       done = true;
/* 196 */       if (Servers.localServer.EPIC)
/*     */       {
/* 198 */         if (performer.getDeity() != null) {
/*     */           
/* 200 */           EntityMoveQuestion emq = new EntityMoveQuestion(performer);
/* 201 */           emq.sendQuestion();
/*     */         } 
/*     */       }
/*     */     } else {
/*     */       
/* 206 */       done = super.action(act, performer, target, action, counter);
/*     */     } 
/* 208 */     return done;
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
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 221 */     boolean done = false;
/* 222 */     boolean reachable = true;
/*     */ 
/*     */     
/* 225 */     if (action == 141) {
/* 226 */       done = action(act, performer, target, action, counter);
/* 227 */     } else if (action == 142) {
/*     */       
/* 229 */       if (performer.getFaith() >= 10.0F) {
/* 230 */         done = MethodsReligion.sacrifice(act, performer, target);
/*     */       } else {
/* 232 */         done = true;
/*     */       } 
/* 234 */     } else if (action == 143) {
/*     */       
/* 236 */       if (performer.getFaith() >= 10.0F) {
/* 237 */         done = MethodsReligion.desecrate(act, performer, source, target);
/*     */       } else {
/* 239 */         done = true;
/*     */       } 
/* 241 */     } else if (action == 216) {
/*     */       
/* 243 */       done = true;
/* 244 */       if (performer.isPriest() && source.isHolyItem())
/*     */       {
/* 246 */         if (target.getParentId() != -10L) {
/*     */           
/* 248 */           performer.getCommunicator().sendNormalServerMessage("The altar needs to be on the ground to be used.");
/*     */         }
/*     */         else {
/*     */           
/* 252 */           done = MethodsReligion.holdSermon(performer, target, source, act, counter);
/*     */         } 
/*     */       }
/* 255 */     } else if (action == 370 && source.isRechargeable()) {
/*     */       
/* 257 */       if (target.getParentId() != -10L) {
/*     */         
/* 259 */         performer.getCommunicator().sendNormalServerMessage("The altar needs to be on the ground to be used.");
/*     */       }
/*     */       else {
/*     */         
/* 263 */         done = MethodsReligion.sendRechargeQuestion(performer, source);
/*     */       } 
/* 265 */     } else if (action == 452) {
/* 266 */       done = action(act, performer, target, action, counter);
/* 267 */     } else if (action == 610) {
/*     */       
/* 269 */       done = true;
/* 270 */       if (Servers.localServer.EPIC)
/*     */       {
/* 272 */         if (performer.getDeity() != null) {
/*     */           
/* 274 */           EntityMoveQuestion emq = new EntityMoveQuestion(performer);
/* 275 */           emq.sendQuestion();
/*     */         } 
/*     */       }
/*     */     } else {
/*     */       
/* 280 */       done = super.action(act, performer, source, target, action, counter);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 285 */     return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\DomainItemBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
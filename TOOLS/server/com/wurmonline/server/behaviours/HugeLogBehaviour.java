/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HugeLogBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*     */   HugeLogBehaviour() {
/*  42 */     super((short)37);
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
/*  54 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  55 */     toReturn.addAll(super.getBehavioursFor(performer, source, target));
/*  56 */     boolean reachable = false;
/*  57 */     if (target.getOwnerId() == -10L) {
/*     */       
/*  59 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F))
/*     */       {
/*  61 */         BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  62 */         if (result == null)
/*     */         {
/*  64 */           reachable = true;
/*     */         }
/*     */       }
/*     */     
/*  68 */     } else if (target.getOwnerId() == performer.getWurmId()) {
/*     */       
/*  70 */       reachable = true;
/*     */     } 
/*  72 */     if (reachable)
/*     */     {
/*  74 */       if (source.isWeaponAxe() || source.getTemplateId() == 24)
/*  75 */         toReturn.add(Actions.actionEntrys[97]); 
/*     */     }
/*  77 */     return toReturn;
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
/*  90 */     boolean done = false;
/*  91 */     boolean reachable = false;
/*  92 */     if (target.getOwnerId() == -10L) {
/*     */       
/*  94 */       if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F))
/*     */       {
/*  96 */         reachable = true;
/*     */       }
/*     */     }
/*  99 */     else if (target.getOwnerId() == performer.getWurmId()) {
/*     */       
/* 101 */       reachable = true;
/*     */     } 
/* 103 */     if (reachable) {
/*     */       
/* 105 */       if (action == 97) {
/* 106 */         done = MethodsItems.chop(act, performer, source, target, counter);
/*     */       } else {
/* 108 */         done = super.action(act, performer, source, target, action, counter);
/*     */       } 
/*     */     } else {
/* 111 */       done = super.action(act, performer, source, target, action, counter);
/* 112 */     }  return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\HugeLogBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
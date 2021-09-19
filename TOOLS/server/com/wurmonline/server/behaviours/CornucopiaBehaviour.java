/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import java.util.List;
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
/*     */ final class CornucopiaBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(CornucopiaBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   CornucopiaBehaviour() {
/*  37 */     super((short)30);
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
/*  48 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  49 */     if (WurmPermissions.mayCreateItems(performer)) {
/*  50 */       toReturn.add(Actions.actionEntrys[148]);
/*     */     } else {
/*  52 */       logger.warning(performer.getName() + " tried to use a Cornucopia but their power was only " + performer.getPower());
/*  53 */     }  return toReturn;
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
/*  65 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  66 */     if (WurmPermissions.mayCreateItems(performer)) {
/*  67 */       toReturn.add(Actions.actionEntrys[148]);
/*     */     } else {
/*  69 */       logger.warning(performer.getName() + " tried to use a Cornucopia but their power was only " + performer.getPower());
/*  70 */     }  return toReturn;
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
/*  83 */     boolean done = true;
/*  84 */     if (action == 148) {
/*     */       
/*  86 */       done = true;
/*  87 */       if (WurmPermissions.mayCreateItems(performer)) {
/*  88 */         Methods.sendCreateQuestion(performer, source);
/*     */       } else {
/*  90 */         logger.warning(performer.getName() + " tried to use a Cornucopia but their power was only " + performer
/*  91 */             .getPower());
/*     */       } 
/*     */     } else {
/*  94 */       done = super.action(act, performer, source, target, action, counter);
/*  95 */     }  return done;
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
/* 106 */     boolean done = true;
/* 107 */     if (action == 148) {
/*     */       
/* 109 */       done = true;
/* 110 */       if (WurmPermissions.mayCreateItems(performer)) {
/* 111 */         Methods.sendCreateQuestion(performer, target);
/*     */       } else {
/* 113 */         logger.warning(performer.getName() + " tried to use a Cornucopia but their power was only " + performer
/* 114 */             .getPower());
/*     */       } 
/*     */     } else {
/* 117 */       done = super.action(act, performer, target, action, counter);
/* 118 */     }  return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\CornucopiaBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
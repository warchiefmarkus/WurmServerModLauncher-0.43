/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.questions.TraderManagementQuestion;
/*     */ import com.wurmonline.server.questions.TraderRentalQuestion;
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
/*     */ final class TraderBookBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*     */   TraderBookBehaviour() {
/*  42 */     super((short)29);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  48 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  49 */     if (target.getTemplateId() != 299 || performer.isOnSurface())
/*  50 */       toReturn.add(Actions.actionEntrys[85]); 
/*  51 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  57 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  58 */     if (target.getTemplateId() != 299 || performer.isOnSurface())
/*  59 */       toReturn.add(Actions.actionEntrys[85]); 
/*  60 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  66 */     boolean done = true;
/*  67 */     if (action == 85) {
/*     */       
/*  69 */       if (target.getTemplateId() == 300) {
/*     */ 
/*     */         
/*  72 */         if (target.getData() == -1L)
/*     */         {
/*  74 */           if (!Methods.isActionAllowed(performer, action)) {
/*  75 */             return true;
/*     */           }
/*     */         }
/*  78 */         TraderManagementQuestion tq = new TraderManagementQuestion(performer, "Managing merchant.", "Set the options you prefer:", target.getWurmId());
/*  79 */         tq.sendQuestion();
/*     */       }
/*  81 */       else if (target.getTemplateId() == 299 && performer.isOnSurface()) {
/*     */         
/*  83 */         if (!Features.Feature.BLOCKED_TRADERS.isEnabled()) {
/*     */           
/*  85 */           if (target.getData() == -1L)
/*     */           {
/*  87 */             if (!Methods.isActionAllowed(performer, action)) {
/*  88 */               return true;
/*     */             }
/*     */           }
/*  91 */           TraderRentalQuestion tq = new TraderRentalQuestion(performer, "Managing trader.", "Set the options you prefer:", target.getWurmId());
/*  92 */           tq.sendQuestion();
/*     */         } else {
/*     */           
/*  95 */           performer.getCommunicator().sendSafeServerMessage("Trader contracts are disabled on this server");
/*     */         } 
/*     */       } 
/*     */     } else {
/*  99 */       done = super.action(act, performer, target, action, counter);
/* 100 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 107 */     boolean done = true;
/* 108 */     if (action == 85) {
/*     */       
/* 110 */       if (target.getTemplateId() == 300) {
/*     */ 
/*     */         
/* 113 */         TraderManagementQuestion tq = new TraderManagementQuestion(performer, "Managing merchant", "Personal merchant management", target.getWurmId());
/* 114 */         tq.sendQuestion();
/*     */       }
/* 116 */       else if (target.getTemplateId() == 299 && performer.isOnSurface()) {
/*     */         
/* 118 */         if (!Features.Feature.BLOCKED_TRADERS.isEnabled()) {
/*     */ 
/*     */           
/* 121 */           TraderRentalQuestion tq = new TraderRentalQuestion(performer, "Managing trader", "Normal trader management", target.getWurmId());
/* 122 */           tq.sendQuestion();
/*     */         } else {
/*     */           
/* 125 */           performer.getCommunicator().sendSafeServerMessage("Trader contracts are disabled on this server");
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 130 */       done = super.action(act, performer, source, target, action, counter);
/* 131 */     }  return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TraderBookBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Delivery;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.questions.WagonerDeliveriesQuestion;
/*     */ import com.wurmonline.server.questions.WagonerSetupDeliveryQuestion;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ final class WagonerContainerBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(WagonerContainerBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   WagonerContainerBehaviour() {
/*  45 */     super((short)61);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  51 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  52 */     toReturn.addAll(getBehavioursForWagonerContainer(performer, null, target));
/*  53 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  59 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  60 */     toReturn.addAll(getBehavioursForWagonerContainer(performer, source, target));
/*  61 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  67 */     boolean[] ans = wagonerContainerActions(act, performer, null, target, action, counter);
/*  68 */     if (ans[0])
/*  69 */       return ans[1]; 
/*  70 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  77 */     boolean[] ans = wagonerContainerActions(act, performer, source, target, action, counter);
/*  78 */     if (ans[0])
/*  79 */       return ans[1]; 
/*  80 */     return super.action(act, performer, source, target, action, counter);
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
/*     */   private List<ActionEntry> getBehavioursForWagonerContainer(Creature performer, @Nullable Item source, Item container) {
/*  93 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/*  95 */     if (Features.Feature.WAGONER.isEnabled()) {
/*     */       
/*  97 */       if (container.isPlanted() && !container.isSealedByPlayer() && !container.isEmpty(false))
/*     */       {
/*     */         
/* 100 */         toReturn.add(Actions.actionEntrys[915]);
/*     */       }
/* 102 */       if (container.isSealedByPlayer()) {
/*     */ 
/*     */         
/* 105 */         Delivery delivery = Delivery.canViewDelivery(container, performer);
/* 106 */         if (delivery != null) {
/* 107 */           toReturn.add(Actions.actionEntrys[918]);
/*     */         }
/* 109 */         if (Delivery.canUnSealContainer(container, performer)) {
/* 110 */           toReturn.add(Actions.actionEntrys[740]);
/*     */         }
/*     */       } 
/*     */     } 
/* 114 */     return toReturn;
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
/*     */   public boolean[] wagonerContainerActions(Action act, Creature performer, @Nullable Item source, Item container, short action, float counter) {
/* 130 */     if (Features.Feature.WAGONER.isEnabled()) {
/*     */       
/* 132 */       if (action == 915 && container.isPlanted() && !container.isSealedByPlayer() && !container.isEmpty(false)) {
/*     */ 
/*     */         
/* 135 */         WagonerSetupDeliveryQuestion wsdq = new WagonerSetupDeliveryQuestion(performer, container);
/* 136 */         wsdq.sendQuestion();
/* 137 */         return new boolean[] { true, true };
/*     */       } 
/* 139 */       Delivery delivery = Delivery.canViewDelivery(container, performer);
/* 140 */       if (delivery != null && action == 918 && container.isSealedByPlayer()) {
/*     */ 
/*     */         
/* 143 */         WagonerDeliveriesQuestion wdq = new WagonerDeliveriesQuestion(performer, delivery.getDeliveryId(), false);
/* 144 */         wdq.sendQuestion2();
/* 145 */         return new boolean[] { true, true };
/*     */       } 
/* 147 */       if (Delivery.canUnSealContainer(container, performer))
/*     */       {
/* 149 */         container.setIsSealedByPlayer(false);
/*     */       }
/*     */     } 
/* 152 */     return new boolean[] { false, false };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WagonerContainerBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
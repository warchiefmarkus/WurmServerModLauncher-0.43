/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.NoSuchStructureException;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Structures;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WritBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(WritBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   WritBehaviour() {
/*  47 */     super((short)21);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  53 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  54 */     toReturn.add(Actions.actionEntrys[62]);
/*  55 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  61 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  62 */     toReturn.add(Actions.actionEntrys[62]);
/*  63 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  69 */     boolean done = true;
/*  70 */     if (action == 1) {
/*     */       
/*  72 */       if (target.getTemplateId() == 166) {
/*     */         try
/*     */         {
/*     */           
/*  76 */           Structure structure = Structures.getStructureForWrit(target.getWurmId());
/*  77 */           structure.setWritid(-10L, true);
/*  78 */           performer.getCommunicator().sendNormalServerMessage("The new permissions system does not use writs. Deleting.");
/*  79 */           Items.destroyItem(target.getWurmId());
/*     */         }
/*  81 */         catch (NoSuchStructureException nss)
/*     */         {
/*  83 */           performer.getCommunicator().sendNormalServerMessage("The structure for this writ does no exist. Deleting.");
/*  84 */           Items.destroyItem(target.getWurmId());
/*     */         }
/*     */       
/*     */       }
/*  88 */     } else if (action == 62) {
/*     */       
/*  90 */       if (target.getOwnerId() == performer.getWurmId()) {
/*     */         
/*     */         try {
/*     */           
/*  94 */           Structure structure = Structures.getStructureForWrit(target.getWurmId());
/*  95 */           structure.setWritid(-10L, true);
/*  96 */           performer.getCommunicator().sendNormalServerMessage("The new permissions system does not use writs. Deleting.");
/*  97 */           Items.destroyItem(target.getWurmId());
/*     */         }
/*  99 */         catch (NoSuchStructureException nss) {
/*     */           
/* 101 */           performer.getCommunicator().sendNormalServerMessage("The structure for this writ does no exist. Destroying.");
/*     */           
/* 103 */           Items.destroyItem(target.getWurmId());
/*     */         } 
/*     */       }
/*     */     } else {
/*     */       
/* 108 */       done = super.action(act, performer, target, action, counter);
/* 109 */     }  return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WritBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
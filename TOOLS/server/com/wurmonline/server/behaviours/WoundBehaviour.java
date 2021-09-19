/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.server.spells.Spells;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import java.util.LinkedList;
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
/*     */ final class WoundBehaviour
/*     */   extends Behaviour
/*     */   implements ItemMaterials, MiscConstants
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(Wound.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   WoundBehaviour() {
/*  49 */     super((short)27);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Wound target) {
/*  55 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  56 */     toReturn.addAll(super.getBehavioursFor(performer, target));
/*  57 */     if (performer.getCultist() != null && performer.getCultist().mayCleanWounds())
/*     */     {
/*  59 */       if (!target.isDrownWound())
/*  60 */         toReturn.add(Actions.actionEntrys[395]); 
/*     */     }
/*  62 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Wound target) {
/*  68 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  69 */     toReturn.addAll(super.getBehavioursFor(performer, source, target));
/*  70 */     if (!target.isDrownWound()) {
/*     */       
/*  72 */       if (source.getMaterial() == 17 && !target.isInternal()) {
/*     */         
/*  74 */         toReturn.add(Actions.actionEntrys[196]);
/*     */       }
/*  76 */       else if (source.isHealingSalve()) {
/*     */         
/*  78 */         if (target.isInternal() || target.isBruise() || target.isPoison())
/*  79 */           toReturn.add(Actions.actionEntrys[196]); 
/*     */       } 
/*  81 */       if (source.getTemplateId() == 481) {
/*     */         
/*  83 */         if (target.getType() != 10) {
/*  84 */           toReturn.add(Actions.actionEntrys[284]);
/*     */         }
/*  86 */       } else if (source.getTemplateId() == 128) {
/*     */         
/*  88 */         if (target.getType() == 10)
/*  89 */           toReturn.add(Actions.actionEntrys[284]); 
/*     */       } 
/*     */     } 
/*  92 */     if (performer.getDeity() != null)
/*     */     {
/*  94 */       if (source.isHolyItem(performer.getDeity()))
/*     */       {
/*  96 */         if (performer.isPriest() || performer.getPower() > 0) {
/*     */           
/*  98 */           float faith = performer.getFaith();
/*  99 */           Spell[] spells = performer.getDeity().getSpellsTargettingWounds((int)faith);
/*     */           
/* 101 */           if (spells.length > 0) {
/*     */             
/* 103 */             toReturn.add(new ActionEntry((short)-spells.length, "Spells", "spells"));
/* 104 */             for (int x = 0; x < spells.length; x++)
/* 105 */               toReturn.add(Actions.actionEntrys[(spells[x]).number]); 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/* 110 */     if (performer.getCultist() != null && performer.getCultist().mayCleanWounds())
/*     */     {
/* 112 */       if (!target.isDrownWound())
/* 113 */         toReturn.add(Actions.actionEntrys[395]); 
/*     */     }
/* 115 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Wound target, short action, float counter) {
/* 121 */     boolean done = true;
/* 122 */     Creature owner = target.getCreature();
/* 123 */     if (owner != null)
/*     */     {
/* 125 */       if (!performer.isWithinDistanceTo(owner.getPosX(), owner.getPosY(), owner.getPositionZ() + owner.getAltOffZ(), 8.0F))
/*     */       {
/* 127 */         return true;
/*     */       }
/*     */     }
/* 130 */     if (action == 1) {
/*     */       
/* 132 */       performer.getCommunicator().sendNormalServerMessage("You see " + target
/* 133 */           .getWoundString() + " at the " + performer
/* 134 */           .getBody().getWoundLocationString(target.getLocation()) + ".");
/* 135 */       if (target.getHealEff() > 0) {
/* 136 */         performer.getCommunicator().sendNormalServerMessage("It is covered with some healing plants (" + target
/* 137 */             .getHealEff() + ").");
/*     */       }
/* 139 */     } else if (action == 395) {
/*     */       
/* 141 */       if (target.getInfectionSeverity() <= 0.0F && target.getPoisonSeverity() <= 0.0F) {
/*     */         
/* 143 */         performer.getCommunicator().sendNormalServerMessage("The wound is not dirty or infected.");
/*     */       }
/* 145 */       else if (target.getInfectionSeverity() <= 0.0F && target.isInternal()) {
/*     */         
/* 147 */         performer.getCommunicator().sendNormalServerMessage("That wound is internal.");
/*     */       }
/* 149 */       else if (Methods.isActionAllowed(performer, (short)384) && performer
/* 150 */         .getCultist() != null && performer.getCultist().mayCleanWounds()) {
/*     */         
/* 152 */         target.setInfectionSeverity(0.0F);
/* 153 */         target.setPoisonSeverity(0.0F);
/* 154 */         String targetName = target.getCreature().getNameWithGenus();
/* 155 */         if (target.getCreature().isPlayer())
/* 156 */           targetName = target.getCreature().getName(); 
/* 157 */         if (target.getCreature() == performer) {
/* 158 */           Server.getInstance().broadCastAction(performer
/* 159 */               .getName() + " sucks on " + performer.getHisHerItsString() + " wounds.", performer, 5);
/*     */         } else {
/*     */           
/* 162 */           target.getCreature().getCommunicator()
/* 163 */             .sendNormalServerMessage(performer.getName() + " sucks on your wounds.");
/* 164 */           Server.getInstance().broadCastAction(performer.getName() + " sucks on " + targetName + "'s wounds.", performer, target
/* 165 */               .getCreature(), 5);
/*     */         } 
/* 167 */         performer.getCommunicator().sendNormalServerMessage("You gleefully clean the wound.");
/* 168 */         performer.getCultist().touchCooldown1();
/*     */       } 
/*     */     } 
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Wound target, short action, float counter) {
/* 178 */     boolean done = true;
/* 179 */     Creature owner = target.getCreature();
/* 180 */     if (owner != null)
/*     */     {
/* 182 */       if (!performer.isWithinDistanceTo(owner.getPosX(), owner.getPosY(), owner.getPositionZ() + owner.getAltOffZ(), 8.0F))
/*     */       {
/* 184 */         return true;
/*     */       }
/*     */     }
/* 187 */     if (action == 1)
/*     */     {
/* 189 */       return action(act, performer, target, action, counter);
/*     */     }
/* 191 */     if (action == 196) {
/*     */       
/* 193 */       if (!target.isDrownWound()) {
/* 194 */         return MethodsCreatures.firstAid(performer, source, target, counter, act);
/*     */       }
/* 196 */     } else if (action == 284) {
/*     */       
/* 198 */       if (!target.isDrownWound()) {
/* 199 */         return MethodsCreatures.treat(performer, source, target, counter, act);
/*     */       }
/* 201 */     } else if (action == 395) {
/*     */       
/* 203 */       if (!target.isDrownWound()) {
/* 204 */         return action(act, performer, target, action, counter);
/*     */       }
/* 206 */     } else if (act.isSpell()) {
/*     */       
/* 208 */       Spell spell = Spells.getSpell(action);
/* 209 */       if (spell != null)
/*     */       {
/* 211 */         if (spell.religious) {
/*     */ 
/*     */           
/* 214 */           if (Methods.isActionAllowed(performer, (short)245)) {
/* 215 */             return Methods.castSpell(performer, Spells.getSpell(action), target, act.getCounterAsFloat());
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 220 */         else if (Methods.isActionAllowed(performer, (short)547)) {
/* 221 */           return Methods.castSpell(performer, Spells.getSpell(action), target, act.getCounterAsFloat());
/*     */         } 
/*     */       }
/*     */     } 
/* 225 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WoundBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
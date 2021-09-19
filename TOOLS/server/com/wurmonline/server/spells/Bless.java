/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Bless
/*     */   extends ReligiousSpell
/*     */   implements AttitudeConstants
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(Bless.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 4;
/*     */ 
/*     */   
/*     */   Bless() {
/*  47 */     super("Bless", 245, 10, 10, 10, 8, 0L);
/*  48 */     this.targetCreature = true;
/*  49 */     this.targetItem = true;
/*  50 */     this.description = "adds a holy aura of purity";
/*  51 */     this.type = 0;
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
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  63 */     if (performer.getDeity() != null) {
/*     */       
/*  65 */       if (target.isPlayer()) {
/*     */         
/*  67 */         if (target.getAttitude(performer) != 2)
/*     */         {
/*  69 */           return true;
/*     */         }
/*  71 */         performer.getCommunicator().sendNormalServerMessage(performer
/*  72 */             .getDeity().getName() + " would never help the infidel " + target.getName() + ".", (byte)3);
/*     */         
/*  74 */         return false;
/*     */       } 
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
/*  88 */       boolean isLibila = performer.getDeity().isLibila();
/*  89 */       if (isLibila) {
/*     */         
/*  91 */         if (target.hasTrait(22)) {
/*     */           
/*  93 */           performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " is already corrupt.", (byte)3);
/*  94 */           return false;
/*     */         } 
/*     */ 
/*     */         
/*  98 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 103 */       if (target.hasTrait(22))
/*     */       {
/* 105 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 109 */       performer.getCommunicator().sendNormalServerMessage(target.getNameWithGenus() + " is not corrupt.", (byte)3);
/* 110 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 115 */     return false;
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
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 127 */     if (performer.getDeity() != null) {
/*     */       
/* 129 */       if (target.getBless() == null) {
/*     */         
/* 131 */         if (target.isUnfinished()) {
/*     */           
/* 133 */           performer.getCommunicator().sendNormalServerMessage("The spell will not work on unfinished items.", (byte)3);
/* 134 */           return false;
/*     */         } 
/* 136 */         return true;
/*     */       } 
/*     */       
/* 139 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 140 */           .getName() + " is already blessed to " + target.getBless().getName() + ".", (byte)3);
/*     */     } 
/* 142 */     return false;
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
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 154 */     target.getCommunicator().sendNormalServerMessage(performer.getName() + " blesses you.");
/* 155 */     performer.getCommunicator().sendNormalServerMessage("You bless " + target.getNameWithGenus() + ".");
/* 156 */     if (performer.getDeity() != null)
/*     */     {
/* 158 */       if (target.isPlayer()) {
/*     */         
/* 160 */         if (performer.getDeity().accepts(target.getAlignment())) {
/*     */ 
/*     */           
/*     */           try {
/* 164 */             if (target.getFavor() < performer.getFavor())
/*     */             {
/* 166 */               if (target.getFavor() < target.getFaith())
/*     */               {
/* 168 */                 if (performer.getDeity().isHateGod()) {
/* 169 */                   performer.maybeModifyAlignment(-1.0F);
/*     */                 } else {
/* 171 */                   performer.maybeModifyAlignment(1.0F);
/*     */                 }  } 
/* 173 */               target.setFavor(
/* 174 */                   (float)(target.getFavor() + ((this.cost * 100) / performer.getFaith() * 30.0F) * castSkill.getKnowledge(performer.zoneBonus) / 100.0D));
/* 175 */               target.getCommunicator().sendNormalServerMessage("The light of " + performer
/* 176 */                   .getDeity().getName() + " shines upon you.");
/*     */             }
/*     */           
/* 179 */           } catch (IOException iox) {
/*     */             
/* 181 */             logger.log(Level.WARNING, performer.getName(), iox);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 186 */           target.getCommunicator().sendNormalServerMessage(performer
/* 187 */               .getDeity().getName() + " does not seem pleased with " + target.getNameWithGenus() + ".");
/* 188 */           performer.getCommunicator().sendNormalServerMessage(performer
/* 189 */               .getDeity().getName() + " does not seem pleased with " + target.getNameWithGenus() + ".");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 194 */         blessCreature(performer, target);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void blessCreature(Creature performer, Creature target) {
/* 201 */     boolean isLibila = performer.getDeity().isLibila();
/* 202 */     boolean isCorrupt = target.hasTrait(22);
/*     */     
/* 204 */     if (isLibila && !isCorrupt) {
/*     */       
/* 206 */       target.getStatus().setTraitBit(22, true);
/*     */       
/* 208 */       if (!target.hasTrait(63))
/*     */       {
/* 210 */         performer.getCommunicator().sendNormalServerMessage("The dark energies of Libila flows through " + target
/* 211 */             .getNameWithGenus() + " corrupting " + target
/* 212 */             .getHimHerItString() + ".");
/*     */       }
/*     */       else
/*     */       {
/* 216 */         performer.getCommunicator().sendNormalServerMessage("The dark energies of Libila flows through " + target
/* 217 */             .getNameWithGenus() + " corrupting " + target
/* 218 */             .getHimHerItString() + ".");
/*     */       }
/*     */     
/* 221 */     } else if (!isLibila && isCorrupt) {
/*     */       
/* 223 */       target.getStatus().setTraitBit(22, false);
/*     */       
/* 225 */       String deityName = performer.getDeity().getName();
/*     */       
/* 227 */       performer.getCommunicator().sendNormalServerMessage(
/* 228 */           StringUtil.format("The cleansing power of %s courses through %s purifying %s.", new Object[] {
/*     */               
/* 230 */               deityName, target.getNameWithGenus(), target.getHimHerItString()
/*     */             }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 243 */     target.bless(performer.getDeity().getNumber());
/* 244 */     if (target.isDomainItem()) {
/*     */       
/* 246 */       target.setName(target.getName() + " of " + performer.getDeity().getName());
/* 247 */       performer.getCommunicator().sendNormalServerMessage("You may now pray at the blessed altar.");
/*     */     }
/*     */     else {
/*     */       
/* 251 */       performer.getCommunicator().sendNormalServerMessage("You bless the " + target
/* 252 */           .getName() + " with the power of " + performer.getDeity().getName() + ".");
/* 253 */       if (target.getTemplateId() == 654)
/*     */       {
/* 255 */         performer.getCommunicator().sendUpdateInventoryItem(target);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Bless.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
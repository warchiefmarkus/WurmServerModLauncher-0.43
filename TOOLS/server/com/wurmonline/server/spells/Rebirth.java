/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.behaviours.MethodsItems;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureStatus;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.server.skills.SkillsFactory;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public final class Rebirth
/*     */   extends ReligiousSpell
/*     */ {
/*  47 */   private static final Logger logger = Logger.getLogger(Rebirth.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 4;
/*     */ 
/*     */   
/*     */   Rebirth() {
/*  54 */     super("Rebirth", 273, 20, 40, 40, 40, 0L);
/*  55 */     this.targetItem = true;
/*  56 */     this.description = "raises zombies from corpses";
/*  57 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  63 */     return mayRaise(performer, target, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean postcondition(Skill castSkill, Creature performer, Item target, double power) {
/*     */     try {
/*  71 */       CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target.getData1());
/*  72 */       byte ctype = CreatureStatus.getModtypeForString(target.getName());
/*  73 */       if (template.isUnique())
/*     */       {
/*  75 */         if (power < 50.0D) {
/*     */           
/*  77 */           performer.getCommunicator().sendNormalServerMessage("The soul of this creature is strong, and it manages to resist your attempt.", (byte)3);
/*     */ 
/*     */           
/*  80 */           return false;
/*     */         } 
/*     */       }
/*  83 */       if (ctype == 99)
/*     */       {
/*  85 */         if (power < 20.0D)
/*     */         {
/*  87 */           performer.getCommunicator().sendNormalServerMessage("The soul of this creature is strong, and it manages to resist your attempt.", (byte)3);
/*     */ 
/*     */           
/*  90 */           return false;
/*     */         }
/*     */       
/*     */       }
/*  94 */     } catch (NoSuchCreatureTemplateException nst) {
/*     */       
/*  96 */       performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte)3);
/*     */       
/*  98 */       return false;
/*     */     } 
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean mayRaise(Creature performer, Item target, boolean sendMess) {
/* 105 */     if (target.getTemplateId() == 272) {
/*     */       
/* 107 */       if (performer.getDeity() != null) {
/*     */         
/* 109 */         if (target.getDamage() < 10.0F) {
/*     */           
/* 111 */           if (!target.isButchered()) {
/*     */ 
/*     */             
/*     */             try {
/* 115 */               CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target
/* 116 */                   .getData1());
/* 117 */               if (template.isRiftCreature()) {
/*     */                 
/* 119 */                 if (sendMess) {
/* 120 */                   performer.getCommunicator().sendNormalServerMessage("This corpse is too far gone for that to work.", (byte)3);
/*     */                 }
/* 122 */                 return false;
/*     */               } 
/* 124 */               if (template.isNotRebirthable()) {
/*     */                 
/* 126 */                 if (sendMess) {
/* 127 */                   performer.getCommunicator().sendNormalServerMessage("The soul refuses to return to this corpse.", (byte)3);
/*     */                 }
/* 129 */                 return false;
/*     */               } 
/* 131 */               if (template.isTowerBasher()) {
/*     */                 
/* 133 */                 if (sendMess) {
/* 134 */                   performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte)3);
/*     */                 }
/*     */                 
/* 137 */                 return false;
/*     */               } 
/* 139 */               if (template.isHuman()) {
/*     */                 
/* 141 */                 if (MethodsItems.isLootableBy(performer, target))
/*     */                 {
/* 143 */                   String name = target.getName().substring(10, target.getName().length());
/*     */ 
/*     */ 
/*     */                   
/*     */                   try {
/* 148 */                     long wid = Players.getInstance().getWurmIdFor(name);
/* 149 */                     if (wid == performer.getWurmId()) {
/*     */                       
/* 151 */                       if (sendMess)
/*     */                       {
/* 153 */                         performer.getCommunicator().sendNormalServerMessage(performer
/* 154 */                             .getDeity().getName() + " does not allow you to raise your own corpse.", (byte)3);
/*     */                       }
/*     */ 
/*     */                       
/* 158 */                       return false;
/*     */                     } 
/* 160 */                     return true;
/*     */                   }
/* 162 */                   catch (Exception ex) {
/*     */                     
/* 164 */                     if (sendMess)
/*     */                     {
/* 166 */                       performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte)3);
/*     */                     
/*     */                     }
/*     */                   }
/*     */                 
/*     */                 }
/* 172 */                 else if (sendMess)
/*     */                 {
/* 174 */                   performer.getCommunicator().sendNormalServerMessage("You may not touch this body right now.", (byte)3);
/*     */                 }
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/* 180 */                 return true;
/*     */               } 
/* 182 */             } catch (NoSuchCreatureTemplateException nst) {
/*     */               
/* 184 */               if (sendMess)
/*     */               {
/* 186 */                 performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte)3);
/*     */               
/*     */               }
/*     */             }
/*     */           
/*     */           }
/* 192 */           else if (sendMess) {
/* 193 */             performer.getCommunicator().sendNormalServerMessage("The corpse is butchered and may not be raised.", (byte)3);
/*     */           }
/*     */         
/* 196 */         } else if (sendMess) {
/* 197 */           performer.getCommunicator().sendNormalServerMessage("The corpse is too damaged.", (byte)3);
/*     */         }
/*     */       
/* 200 */       } else if (sendMess) {
/* 201 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens. No deity answers to the call.", (byte)3);
/*     */       }
/*     */     
/* 204 */     } else if (sendMess) {
/* 205 */       performer.getCommunicator().sendNormalServerMessage("The spell will only work on corpses.", (byte)3);
/* 206 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 212 */     if (power > 0.0D) {
/*     */       
/* 214 */       raise(power, performer, target, false);
/*     */     } else {
/*     */       
/* 217 */       performer.getCommunicator().sendNormalServerMessage("You fail to connect with the soul of this creature and bind it in a physical form.", (byte)3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void raise(double power, Creature performer, Item target, boolean massRaise) {
/* 224 */     if (target.getTemplateId() == 272) {
/*     */       
/* 226 */       if (performer.getDeity() != null) {
/*     */         
/* 228 */         if (target.getDamage() < 10.0F) {
/*     */           
/*     */           try
/*     */           {
/* 232 */             CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target.getData1());
/* 233 */             Creature cret = null;
/* 234 */             if (template.isHuman()) {
/*     */               
/* 236 */               if (MethodsItems.isLootableBy(performer, target)) {
/*     */                 
/* 238 */                 String name = target.getName().substring(10, target.getName().length());
/*     */                 
/*     */                 try {
/* 241 */                   long wid = Players.getInstance().getWurmIdFor(name);
/* 242 */                   byte sex = 0;
/* 243 */                   if (target.female)
/* 244 */                     sex = 1; 
/* 245 */                   if (wid == performer.getWurmId()) {
/*     */                     
/* 247 */                     if (!massRaise)
/*     */                     {
/* 249 */                       performer.getCommunicator()
/* 250 */                         .sendNormalServerMessage(performer
/* 251 */                           .getDeity().getName() + " does not allow you to raise your own corpse.", (byte)3);
/*     */                     }
/*     */                     
/*     */                     return;
/*     */                   } 
/*     */                   
/* 257 */                   cret = Creature.doNew(template.getTemplateId(), false, target.getPosX(), target.getPosY(), target
/* 258 */                       .getRotation(), target.isOnSurface() ? 0 : -1, 
/* 259 */                       LoginHandler.raiseFirstLetter("Zombie " + name), sex, performer.getKingdomId(), (byte)0, true);
/*     */ 
/*     */ 
/*     */                   
/* 263 */                   cret.getStatus().setTraitBit(63, true);
/* 264 */                   Skills skills = SkillsFactory.createSkills(wid);
/*     */                   
/*     */                   try {
/* 267 */                     skills.load();
/* 268 */                     cret.getSkills().delete();
/* 269 */                     cret.getSkills().clone(skills.getSkills());
/* 270 */                     Skill[] cskills = cret.getSkills().getSkills();
/* 271 */                     for (Skill cSkill : cskills) {
/*     */                       
/* 273 */                       if (cSkill.getNumber() == 10052) {
/* 274 */                         cSkill.setKnowledge(Math.min(70.0D, cSkill.getKnowledge() * 0.699999988079071D), false);
/*     */                       } else {
/* 276 */                         cSkill.setKnowledge(Math.min(40.0D, cSkill.getKnowledge() * 0.699999988079071D), false);
/*     */                       } 
/* 278 */                     }  cret.getSkills().save();
/*     */                   }
/* 280 */                   catch (Exception e) {
/*     */                     
/* 282 */                     logger.log(Level.WARNING, e.getMessage(), e);
/* 283 */                     if (!massRaise)
/*     */                     {
/* 285 */                       performer.getCommunicator().sendNormalServerMessage("You struggle to bring the corpse back to life, but you sense problems.", (byte)3);
/*     */                     
/*     */                     }
/*     */                   }
/*     */                 
/*     */                 }
/* 291 */                 catch (Exception ex) {
/*     */                   
/* 293 */                   if (!massRaise) {
/* 294 */                     performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte)3);
/*     */                   }
/*     */                 }
/*     */               
/*     */               }
/* 299 */               else if (!massRaise) {
/* 300 */                 performer.getCommunicator().sendNormalServerMessage("You may not touch this body right now.", (byte)3);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 305 */               byte ctype = CreatureStatus.getModtypeForString(target.getName());
/* 306 */               if (template.isUnique())
/*     */               {
/* 308 */                 if (power < 50.0D) {
/*     */                   
/* 310 */                   if (!massRaise) {
/* 311 */                     performer.getCommunicator().sendNormalServerMessage("The soul of this creature is strong, and it manages to resist your attempt.", (byte)3);
/*     */                   }
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               }
/* 317 */               if (ctype == 99)
/*     */               {
/* 319 */                 if (power < 20.0D) {
/*     */                   
/* 321 */                   if (!massRaise) {
/* 322 */                     performer.getCommunicator().sendNormalServerMessage("The soul of this creature is strong, and it manages to resist your attempt.", (byte)3);
/*     */                   }
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               }
/* 328 */               byte sex = 0;
/* 329 */               if (target.female) {
/* 330 */                 sex = 1;
/*     */               }
/*     */               try {
/* 333 */                 cret = Creature.doNew(template.getTemplateId(), false, target.getPosX(), target.getPosY(), target
/* 334 */                     .getRotation(), target.isOnSurface() ? 0 : -1, 
/* 335 */                     LoginHandler.raiseFirstLetter("Zombie " + template.getName()), sex, performer
/* 336 */                     .getKingdomId(), ctype, true);
/*     */ 
/*     */                 
/* 339 */                 cret.getStatus().setTraitBit(63, true);
/* 340 */                 if (template.isUnique()) {
/*     */                   
/*     */                   try {
/*     */                     
/* 344 */                     Skill[] skills = cret.getSkills().getSkills();
/* 345 */                     for (Skill lSkill : skills) {
/*     */                       
/* 347 */                       if (lSkill.getNumber() == 10052) {
/* 348 */                         lSkill.setKnowledge(lSkill.getKnowledge() * 0.4000000059604645D, false);
/*     */                       } else {
/* 350 */                         lSkill.setKnowledge(lSkill.getKnowledge() * 0.20000000298023224D, false);
/*     */                       } 
/*     */                     } 
/* 353 */                   } catch (Exception e) {
/*     */                     
/* 355 */                     logger.log(Level.WARNING, e.getMessage(), e);
/* 356 */                     if (!massRaise) {
/* 357 */                       performer.getCommunicator().sendNormalServerMessage("You struggle to bring the corpse back to life, but you sense problems.", (byte)3);
/*     */                     }
/*     */                   }
/*     */                 
/*     */                 }
/*     */               }
/* 363 */               catch (Exception e) {
/*     */                 
/* 365 */                 if (!massRaise) {
/* 366 */                   performer.getCommunicator().sendNormalServerMessage("You struggle to bring the corpse back to life.", (byte)3);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 371 */             if (cret != null)
/*     */             {
/* 373 */               if (!massRaise) {
/*     */                 
/* 375 */                 if (performer.getPet() != null) {
/*     */                   
/* 377 */                   performer.getCommunicator().sendNormalServerMessage(performer
/* 378 */                       .getPet().getName() + " stops obeying you.", (byte)2);
/*     */                   
/* 380 */                   if (performer.getPet().getLeader() == performer)
/* 381 */                     performer.getPet().setLeader(null); 
/* 382 */                   if (performer.getPet().isReborn()) {
/* 383 */                     performer.getPet().die(false, "Neglect");
/*     */                   } else {
/* 385 */                     performer.getPet().setDominator(-10L);
/*     */                   } 
/* 387 */                 }  performer.setPet(cret.getWurmId());
/* 388 */                 cret.setDominator(performer.getWurmId());
/* 389 */                 cret.setLoyalty(Math.max(10.0F, (float)power));
/* 390 */                 cret.getStatus().setLastPolledLoyalty();
/* 391 */                 cret.setTarget(-10L, true);
/* 392 */                 if (performer.getTarget() == cret)
/* 393 */                   performer.setTarget(-10L, true); 
/* 394 */                 if (cret.opponent != null)
/* 395 */                   cret.setOpponent(null); 
/* 396 */                 if (performer.opponent == cret)
/* 397 */                   performer.setOpponent(null); 
/* 398 */                 performer.getCommunicator().sendNormalServerMessage(cret.getName() + " now obeys you.", (byte)2);
/*     */ 
/*     */                 
/* 401 */                 VolaTile targetVolaTile = Zones.getTileOrNull(cret
/* 402 */                     .getTileX(), cret.getTileY(), cret.isOnSurface());
/* 403 */                 if (targetVolaTile != null)
/*     */                 {
/* 405 */                   targetVolaTile.sendAttachCreatureEffect(cret, (byte)8, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */                 }
/*     */               } 
/*     */             }
/*     */ 
/*     */             
/* 411 */             target.setDamage(Math.max(target.getDamage(), 10.0F));
/*     */           }
/* 413 */           catch (NoSuchCreatureTemplateException nst)
/*     */           {
/* 415 */             if (!massRaise) {
/* 416 */               performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte)3);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/* 421 */         else if (!massRaise) {
/* 422 */           performer.getCommunicator().sendNormalServerMessage("The corpse is too damaged.", (byte)3);
/*     */         }
/*     */       
/* 425 */       } else if (!massRaise) {
/* 426 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens. No deity answers to the call.", (byte)3);
/*     */       }
/*     */     
/* 429 */     } else if (!massRaise) {
/* 430 */       performer.getCommunicator().sendNormalServerMessage("The spell will only work on corpses.", (byte)3);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Rebirth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
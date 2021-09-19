/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PlanterBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*     */   public PlanterBehaviour() {
/*  51 */     super((short)55);
/*     */   }
/*     */ 
/*     */   
/*  55 */   private static final Logger logger = Logger.getLogger(PlanterBehaviour.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  60 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  61 */     if (canBePicked(target))
/*     */     {
/*  63 */       toReturn.add(Actions.actionEntrys[137]);
/*     */     }
/*  65 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  71 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*     */     
/*  73 */     if (source.isPotable() && target.getTemplateId() == 1161 && source.isRaw())
/*     */     {
/*  75 */       if (source.isPStateNone() || source.isFresh())
/*     */       {
/*  77 */         toReturn.add(new ActionEntry((short)186, "Plant " + source.getName(), "planting"));
/*     */       }
/*     */     }
/*  80 */     if (source.getTemplateId() == 176 && performer.getPower() >= 2 && target
/*  81 */       .getTemplateId() == 1162)
/*     */     {
/*  83 */       toReturn.add(Actions.actionEntrys[188]);
/*     */     }
/*  85 */     if (canBePicked(target))
/*     */     {
/*  87 */       toReturn.add(Actions.actionEntrys[137]);
/*     */     }
/*  89 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  94 */     if (action == 137) {
/*  95 */       return pickHerb(act, performer, target, counter);
/*     */     }
/*  97 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 103 */     if (action == 1)
/* 104 */       return action(act, performer, target, action, counter); 
/* 105 */     if (action == 186) {
/*     */       
/* 107 */       if (source.isSpice() && (source.isFresh() || source.isPStateNone()))
/* 108 */         return plantHerb(act, performer, source, target, counter); 
/* 109 */       if (source.isHerb() && (source.isFresh() || source.isPStateNone()))
/* 110 */         return plantHerb(act, performer, source, target, counter); 
/* 111 */       return true;
/*     */     } 
/* 113 */     if (action == 188 && source.getTemplateId() == 176 && performer.getPower() >= 2) {
/*     */ 
/*     */       
/* 116 */       target.advancePlanterWeek();
/* 117 */       return true;
/*     */     } 
/*     */     
/* 120 */     return super.action(act, performer, source, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean pickHerb(Action act, Creature performer, Item pot, float counter) {
/* 126 */     int time = 0;
/*     */     
/* 128 */     ItemTemplate growing = pot.getRealTemplate();
/*     */     
/* 130 */     if (growing == null) {
/*     */       
/* 132 */       performer.getCommunicator().sendNormalServerMessage("Not sure what is growing in here.", (byte)3);
/* 133 */       return true;
/*     */     } 
/* 135 */     if (!Methods.isActionAllowed(performer, act.getNumber()))
/*     */     {
/* 137 */       return true;
/*     */     }
/* 139 */     if (!canBePicked(pot)) {
/*     */       
/* 141 */       performer.getCommunicator().sendNormalServerMessage("It is not at correct age to be picked.", (byte)3);
/* 142 */       return true;
/*     */     } 
/* 144 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*     */       
/* 146 */       performer.getCommunicator().sendNormalServerMessage("Your inventory is full. You would have no space to put whatever you pick.");
/*     */       
/* 148 */       return true;
/*     */     } 
/*     */     
/* 151 */     Skill gardening = performer.getSkills().getSkillOrLearn(10045);
/*     */     
/* 153 */     if (counter == 1.0F) {
/*     */       
/* 155 */       time = Actions.getStandardActionTime(performer, gardening, pot, 0.0D) / 5;
/* 156 */       act.setTimeLeft(time);
/* 157 */       performer.getCommunicator().sendNormalServerMessage("You start picking " + growing.getNameWithGenus() + ".");
/* 158 */       Server.getInstance().broadCastAction(performer.getName() + " starts to pick some " + growing.getName() + ".", performer, 5);
/* 159 */       performer.sendActionControl(Actions.actionEntrys[137].getVerbString(), true, time);
/* 160 */       return false;
/*     */     } 
/*     */     
/* 163 */     time = act.getTimeLeft();
/*     */     
/* 165 */     if (counter * 10.0F > time) {
/*     */       
/* 167 */       if (act.getRarity() != 0)
/* 168 */         performer.playPersonalSound("sound.fx.drumroll"); 
/* 169 */       int age = pot.getAuxData() & Byte.MAX_VALUE;
/* 170 */       int knowledge = (int)gardening.getKnowledge(0.0D);
/* 171 */       float diff = getDifficulty(pot.getRealTemplateId(), knowledge);
/* 172 */       double power = gardening.skillCheck(diff, 0.0D, false, counter);
/*     */ 
/*     */       
/*     */       try {
/* 176 */         float ql = Herb.getQL(power, knowledge);
/* 177 */         Item newItem = ItemFactory.createItem(pot.getRealTemplateId(), Math.max(ql, 1.0F), (byte)0, act
/* 178 */             .getRarity(), null);
/* 179 */         if (ql < 0.0F) {
/* 180 */           newItem.setDamage(-ql / 2.0F);
/*     */         } else {
/* 182 */           newItem.setIsFresh(true);
/* 183 */         }  Item inventory = performer.getInventory();
/* 184 */         inventory.insertItem(newItem);
/*     */         
/* 186 */         performer.achievement(602);
/*     */       }
/* 188 */       catch (FailedException fe) {
/*     */         
/* 190 */         logger.log(Level.WARNING, performer.getName() + " " + fe.getMessage(), (Throwable)fe);
/*     */       }
/* 192 */       catch (NoSuchTemplateException nst) {
/*     */         
/* 194 */         logger.log(Level.WARNING, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/*     */       } 
/* 196 */       pot.setLastMaintained(WurmCalendar.currentTime);
/*     */       
/* 198 */       if (power < -50.0D) {
/*     */         
/* 200 */         performer.getCommunicator().sendNormalServerMessage("You broke off more than needed and damaged the plant, but still managed to get " + growing
/* 201 */             .getNameWithGenus() + ".");
/*     */         
/* 203 */         pot.setAuxData((byte)(age + 1));
/*     */       }
/* 205 */       else if (power > 0.0D) {
/*     */         
/* 207 */         performer.getCommunicator().sendNormalServerMessage("You successfully picked " + growing.getNameWithGenus() + ", it now looks healthier.");
/*     */ 
/*     */         
/* 210 */         pot.setAuxData((byte)(age - 1));
/*     */       }
/*     */       else {
/*     */         
/* 214 */         performer.getCommunicator().sendNormalServerMessage("You successfully picked " + growing.getNameWithGenus() + ".");
/*     */         
/* 216 */         pot.setAuxData((byte)age);
/*     */       } 
/* 218 */       return true;
/*     */     } 
/* 220 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canBePicked(Item pot) {
/* 225 */     if (pot.getTemplateId() != 1162) {
/* 226 */       return false;
/*     */     }
/* 228 */     ItemTemplate temp = pot.getRealTemplate();
/* 229 */     int age = pot.getAuxData() & Byte.MAX_VALUE;
/*     */     
/* 231 */     boolean pickable = ((pot.getAuxData() & 0x80) != 0);
/* 232 */     return (temp != null && pickable && age > 5 && age < 95);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean plantHerb(Action act, Creature performer, Item herbSpice, Item pot, float counter) {
/* 237 */     if (!Methods.isActionAllowed(performer, act.getNumber()))
/*     */     {
/* 239 */       return true;
/*     */     }
/* 241 */     int time = 0;
/* 242 */     if (counter == 1.0F) {
/*     */       
/* 244 */       String type = herbSpice.isSpice() ? "spice" : "herb";
/* 245 */       Skill gardening = performer.getSkills().getSkillOrLearn(10045);
/* 246 */       time = Actions.getStandardActionTime(performer, gardening, herbSpice, 0.0D);
/* 247 */       act.setTimeLeft(time);
/* 248 */       performer.getCommunicator().sendNormalServerMessage("You start planting the " + herbSpice.getName() + ".");
/* 249 */       Server.getInstance().broadCastAction(performer.getName() + " starts to plant some " + type + ".", performer, 5);
/* 250 */       performer.sendActionControl(Actions.actionEntrys[186].getVerbString(), true, time);
/*     */       
/* 252 */       return false;
/*     */     } 
/*     */     
/* 255 */     time = act.getTimeLeft();
/*     */     
/* 257 */     if (counter * 10.0F > time) {
/*     */       
/* 259 */       float ql = herbSpice.getQualityLevel() + pot.getQualityLevel();
/* 260 */       ql /= 2.0F;
/* 261 */       float dmg = herbSpice.getDamage() + pot.getDamage();
/* 262 */       dmg /= 2.0F;
/* 263 */       Skill gardening = performer.getSkills().getSkillOrLearn(10045);
/*     */       
/*     */       try {
/* 266 */         int toCreate = 1162;
/* 267 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(1162);
/* 268 */         double power = gardening.skillCheck((template.getDifficulty() + dmg), ql, false, counter);
/* 269 */         if (power > 0.0D) {
/*     */ 
/*     */           
/*     */           try {
/*     */             
/* 274 */             Item newPot = ItemFactory.createItem(1162, pot.getQualityLevel(), pot.getRarity(), performer.getName());
/* 275 */             newPot.setRealTemplate(herbSpice.getTemplate().getGrows());
/* 276 */             newPot.setLastOwnerId(pot.getLastOwnerId());
/* 277 */             newPot.setDescription(pot.getDescription());
/* 278 */             newPot.setDamage(pot.getDamage());
/* 279 */             Item parent = pot.getParentOrNull();
/* 280 */             if (parent != null && parent.getTemplateId() == 1110 && (parent.getItemsAsArray()).length > 30) {
/*     */               
/* 282 */               performer.getCommunicator().sendNormalServerMessage("The pot will not fit back into the rack, so you place it on the ground.", (byte)2);
/*     */ 
/*     */ 
/*     */               
/* 286 */               newPot.setPosXY(pot.getPosX(), pot.getPosY());
/* 287 */               VolaTile tile = Zones.getTileOrNull(pot.getTileX(), pot.getTileY(), pot.isOnSurface());
/* 288 */               if (tile != null)
/*     */               {
/* 290 */                 tile.addItem(newPot, false, false);
/*     */               }
/*     */             }
/* 293 */             else if (parent == null) {
/*     */ 
/*     */               
/* 296 */               newPot.setPosXYZRotation(pot.getPosX(), pot.getPosY(), pot.getPosZ(), pot.getRotation());
/* 297 */               newPot.setIsPlanted(pot.isPlanted());
/* 298 */               VolaTile tile = Zones.getTileOrNull(pot.getTileX(), pot.getTileY(), pot.isOnSurface());
/* 299 */               if (tile != null)
/*     */               {
/* 301 */                 tile.addItem(newPot, false, false);
/*     */               
/*     */               }
/*     */             }
/*     */             else {
/*     */               
/* 307 */               parent.insertItem(newPot, true);
/*     */             } 
/* 309 */             Items.destroyItem(pot.getWurmId());
/* 310 */             performer.getCommunicator().sendNormalServerMessage("You finished planting the " + herbSpice
/* 311 */                 .getName() + " in the pot.");
/*     */           }
/* 313 */           catch (NoSuchTemplateException nst) {
/*     */             
/* 315 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */           }
/* 317 */           catch (FailedException fe) {
/*     */             
/* 319 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */           } 
/*     */         } else {
/*     */           
/* 323 */           performer.getCommunicator().sendNormalServerMessage("Sadly, the fragile " + herbSpice
/* 324 */               .getName() + " do not survive despite your best efforts.", (byte)3);
/* 325 */         }  Items.destroyItem(herbSpice.getWurmId());
/*     */       }
/* 327 */       catch (NoSuchTemplateException nst) {
/*     */         
/* 329 */         logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */       } 
/* 331 */       return true;
/*     */     } 
/* 333 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float getDifficulty(int templateId, int knowledge) {
/* 338 */     float h = Herb.getDifficulty(templateId, knowledge);
/* 339 */     if (h > 0.0F)
/* 340 */       return h; 
/* 341 */     float f = Forage.getDifficulty(templateId, knowledge);
/* 342 */     if (f > 0.0F)
/* 343 */       return f; 
/* 344 */     return 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\PlanterBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
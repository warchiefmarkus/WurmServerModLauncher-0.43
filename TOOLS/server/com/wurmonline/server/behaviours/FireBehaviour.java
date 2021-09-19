/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FireBehaviour
/*     */   extends ItemBehaviour
/*     */   implements ItemMaterials
/*     */ {
/*     */   FireBehaviour() {
/*  54 */     super((short)18);
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
/*  66 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/*     */     try {
/*  69 */       toReturn.addAll(super.getBehavioursFor(performer, source, target));
/*  70 */       if (target.getTemplateId() == 1243) {
/*     */ 
/*     */         
/*  73 */         if ((source.getTemplateId() == 169 || source.getTemplateId() == 36 || source
/*  74 */           .isLiquidInflammable() || source.isPaper()) && 
/*  75 */           !source.isNewbieItem() && !source.isChallengeNewbieItem() && 
/*  76 */           !source.isIndestructible())
/*     */         {
/*  78 */           BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  79 */           if (result == null)
/*     */           {
/*  81 */             toReturn.add(new ActionEntry((short)117, "Fuel", "Fueling"));
/*     */           }
/*     */         }
/*     */       
/*  85 */       } else if ((source.isWood() || source.isCloth() || source.isMelting() || source.isLiquidInflammable() || source.isPaper()) && 
/*  86 */         !source.isNewbieItem() && !source.isChallengeNewbieItem() && 
/*  87 */         !source.isIndestructible() && !source.isComponentItem() && source.getTemplateId() != 1392) {
/*     */         
/*  89 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPosZ(), 4.0F))
/*     */         {
/*  91 */           if (target.getTemplateId() != 74)
/*     */           {
/*  93 */             BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/*  94 */             if (result == null)
/*     */             {
/*  96 */               toReturn.add(Actions.actionEntrys[117]);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       } 
/* 102 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 105 */     return toReturn;
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
/* 116 */     boolean done = true;
/* 117 */     if (action == 1) {
/*     */       
/* 119 */       int temperature = target.getTemperature();
/* 120 */       StringBuilder sendString = new StringBuilder(target.examine(performer));
/* 121 */       if (target.isPlanted()) {
/*     */         
/* 123 */         PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(target.lastOwner);
/* 124 */         String plantedBy = "someone";
/* 125 */         if (pInfo != null)
/* 126 */           plantedBy = pInfo.getName(); 
/* 127 */         sendString.append(" The " + target.getName() + " has been firmly secured to the ground by " + plantedBy + ".");
/*     */       } 
/* 129 */       String s = target.getSignature();
/* 130 */       if (s != null && !s.isEmpty()) {
/* 131 */         sendString.append(" You can barely make out the signature of its maker,  '" + s + "'.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 136 */       if (target.color != -1) {
/* 137 */         sendString.append(MethodsItems.getColorDesc(target.color));
/*     */       }
/* 139 */       if (temperature < 1000) {
/* 140 */         sendString.append(" The fire is not lit.");
/* 141 */       } else if (target.getTemplateId() == 1243) {
/* 142 */         sendString.append(" There are wisps of smoke steadily coming out of the nozzle.");
/* 143 */       } else if (temperature < 2000) {
/* 144 */         sendString.append(" A few red glowing coals can be found under a bed of ashes.");
/* 145 */       } else if (temperature < 3500) {
/* 146 */         sendString.append(" A layer of ashes is starting to form on the glowing coals.");
/* 147 */       } else if (temperature < 4000) {
/* 148 */         sendString.append(" Only a hot, red glowing bed of coal remains of the fire now.");
/* 149 */       } else if (temperature < 5000) {
/* 150 */         sendString.append(" A few flames still dance on the fire but soon they too will die.");
/* 151 */       } else if (temperature < 7000) {
/* 152 */         sendString.append(" The fire is starting to fade.");
/* 153 */       } else if (temperature < 9000) {
/* 154 */         sendString.append(" The fire burns with wild flames and still has much unburnt material.");
/*     */       } else {
/* 156 */         sendString.append(" The fire burns steadily and will still burn for a long time.");
/* 157 */       }  performer.getCommunicator().sendNormalServerMessage(sendString.toString());
/* 158 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*     */     } else {
/*     */       
/* 161 */       done = super.action(act, performer, target, action, counter);
/* 162 */     }  return done;
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
/* 175 */     boolean done = false;
/* 176 */     if (action == 117) {
/*     */ 
/*     */       
/* 179 */       if (source.getTemplateId() == 1276) {
/*     */         
/* 181 */         performer.getCommunicator().sendNormalServerMessage("You cannot fuel " + target
/* 182 */             .getNameWithGenus() + " with " + source.getNameWithGenus() + " silly.");
/* 183 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 187 */       if (source.isMagicContainer()) {
/*     */         
/* 189 */         performer.getCommunicator().sendNormalServerMessage("The " + source
/* 190 */             .getName() + " will not burn.");
/* 191 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 195 */       if (source.isHollow() && !source.isEmpty(true)) {
/*     */         
/* 197 */         boolean skip = false;
/* 198 */         for (Item item : source.getAllItems(true)) {
/*     */           
/* 200 */           if (item.getTemplateId() != 1392 || !item.isComponentItem())
/* 201 */             skip = true; 
/*     */         } 
/* 203 */         if (skip) {
/*     */           
/* 205 */           performer.getCommunicator().sendNormalServerMessage("The " + source
/* 206 */               .getName() + " must contain no items before using it to fuel the " + target
/* 207 */               .getName() + ".");
/* 208 */           return true;
/*     */         } 
/*     */       } 
/* 211 */       if (source.isComponentItem() || source.getTemplateId() == 1392) {
/*     */         
/* 213 */         performer.getCommunicator().sendNormalServerMessage("You cannot fuel " + target
/* 214 */             .getNameWithGenus() + " with " + source.getNameWithGenus() + ".");
/* 215 */         return true;
/*     */       } 
/*     */       
/* 218 */       if (target.getTemplateId() == 1243) {
/*     */         
/* 220 */         if (target.getAuxData() > 100) {
/*     */           
/* 222 */           performer.getCommunicator().sendNormalServerMessage("The bee smoker is already full.");
/* 223 */           return true;
/*     */         } 
/*     */         
/* 226 */         if ((source.getTemplateId() == 169 || source.getTemplateId() == 36 || source
/* 227 */           .isLiquidInflammable() || source.isPaper()) && 
/* 228 */           !source.isNewbieItem() && !source.isChallengeNewbieItem() && 
/* 229 */           !source.isIndestructible()) {
/*     */           
/* 231 */           BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/* 232 */           if (result == null)
/*     */           {
/* 234 */             performer.getCommunicator().sendNormalServerMessage("You fuel the " + target
/* 235 */                 .getName() + " with " + source.getNameWithGenus() + ".");
/* 236 */             Server.getInstance().broadCastAction(performer
/* 237 */                 .getName() + " fuels the " + target.getName() + " with " + source
/* 238 */                 .getNameWithGenus() + ".", performer, 5);
/*     */             
/* 240 */             double newTemp = (source.getWeightGrams() * Item.fuelEfficiency(source.getMaterial()));
/*     */             
/* 242 */             if (target.getTemperature() > 1000) {
/*     */               
/* 244 */               short maxTemp = 30000;
/* 245 */               short newPTemp = (short)(int)Math.min(30000.0D, target.getTemperature() + newTemp);
/* 246 */               target.setTemperature(newPTemp);
/*     */             } 
/* 248 */             Items.destroyItem(source.getWurmId());
/*     */ 
/*     */             
/* 251 */             target.setAuxData((byte)(int)Math.min(127.0D, target.getAuxData() + newTemp / 100.0D));
/*     */           }
/*     */         
/*     */         } 
/* 255 */       } else if ((source.isWood() || source.isCloth() || source.isMelting() || source.isLiquidInflammable() || source.isPaper()) && 
/* 256 */         !source.isNewbieItem() && !source.isChallengeNewbieItem() && 
/* 257 */         !source.isIndestructible()) {
/*     */         
/* 259 */         BlockingResult result = Blocking.getBlockerBetween(performer, target, 4);
/* 260 */         if (result == null)
/*     */         {
/* 262 */           if (target.getTemplateId() != 74)
/*     */           {
/* 264 */             if (target.getTemperature() > 1000)
/*     */             {
/* 266 */               performer.getCommunicator().sendNormalServerMessage("You fuel the " + target
/* 267 */                   .getName() + " with " + source.getNameWithGenus() + ".");
/* 268 */               Server.getInstance().broadCastAction(performer
/* 269 */                   .getName() + " fuels the " + target.getName() + " with " + source
/* 270 */                   .getNameWithGenus() + ".", performer, 5);
/* 271 */               short maxTemp = 30000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 279 */               double newTemp = (source.getWeightGrams() * Item.fuelEfficiency(source.getMaterial()));
/* 280 */               target.setTemperature((short)(int)Math.min(30000.0D, target.getTemperature() + newTemp));
/*     */               
/* 282 */               for (Item item : source.getAllItems(true)) {
/*     */                 
/* 284 */                 if (item.isComponentItem() || item.getTemplateId() == 1392)
/* 285 */                   Items.destroyItem(item.getWurmId()); 
/*     */               } 
/* 287 */               Items.destroyItem(source.getWurmId());
/*     */               
/* 289 */               if (target.getTemplateId() == 889)
/*     */               {
/* 291 */                 target.setAuxData((byte)(int)Math.min(127.0D, target.getAuxData() + newTemp / 1000.0D));
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 296 */               performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is not burning.");
/*     */             
/*     */             }
/*     */ 
/*     */           
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 306 */         performer.getCommunicator().sendNormalServerMessage("You cannot burn the " + target.getName() + ".");
/* 307 */       }  done = true;
/*     */     } else {
/*     */       
/* 310 */       if (action == 1)
/*     */       {
/* 312 */         return action(act, performer, target, action, counter);
/*     */       }
/* 314 */       if (action == 12)
/*     */       
/* 316 */       { if (source.getTemplateId() == 176 && performer.getPower() >= 2) {
/* 317 */           done = MethodsItems.setFire(performer, target);
/*     */         } else {
/* 319 */           done = MethodsItems.startFire(performer, source, target, counter);
/*     */         }  }
/*     */       else
/* 322 */       { done = super.action(act, performer, source, target, action, counter); } 
/* 323 */     }  return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\FireBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
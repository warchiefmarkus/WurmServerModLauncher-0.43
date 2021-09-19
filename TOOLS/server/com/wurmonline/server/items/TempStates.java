/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.GeneralUtilities;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.ItemBehaviour;
/*     */ import com.wurmonline.server.behaviours.MethodsItems;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public final class TempStates
/*     */   implements MiscConstants
/*     */ {
/*  47 */   private static final Logger logger = Logger.getLogger(TempStates.class.getName());
/*  48 */   private static final Set<TempState> tempStates = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  59 */     addState(new TempState(38, 46, (short)4000, true, false, true));
/*  60 */     addState(new TempState(697, 698, (short)7000, true, false, true));
/*  61 */     addState(new TempState(693, 694, (short)8000, true, false, true));
/*  62 */     addState(new TempState(684, 46, (short)2000, true, false, true));
/*  63 */     addState(new TempState(43, 47, (short)4000, true, false, true));
/*  64 */     addState(new TempState(39, 44, (short)4000, true, false, true));
/*  65 */     addState(new TempState(40, 45, (short)4000, true, false, true));
/*  66 */     addState(new TempState(42, 48, (short)4000, true, false, true));
/*  67 */     addState(new TempState(41, 49, (short)4000, true, false, true));
/*  68 */     addState(new TempState(207, 220, (short)4000, true, false, true));
/*     */     
/*  70 */     addState(new TempState(769, 776, (short)6000, true, true, false));
/*  71 */     addState(new TempState(777, 778, (short)7000, true, true, false));
/*  72 */     addState(new TempState(181, 76, (short)4000, true, true, false));
/*  73 */     addState(new TempState(182, 77, (short)4000, true, true, false));
/*  74 */     addState(new TempState(183, 78, (short)4000, true, true, false));
/*  75 */     addState(new TempState(812, 813, (short)4000, true, true, false));
/*  76 */     addState(new TempState(1019, 1020, (short)8500, true, true, false));
/*  77 */     addState(new TempState(1021, 1022, (short)10000, true, true, false));
/*     */     
/*  79 */     addState(new TempState(789, 788, (short)4000, true, true, false));
/*  80 */     addState(new TempState(342, 343, (short)4000, true, true, false));
/*     */     
/*  82 */     addState(new TempState(225, 221, (short)3500, true, true, true));
/*  83 */     addState(new TempState(224, 223, (short)3500, true, true, true));
/*     */     
/*  85 */     addState(new TempState(699, 698, (short)5500, true, true, true));
/*  86 */     addState(new TempState(695, 694, (short)6000, true, true, true));
/*     */     
/*  88 */     addState(new TempState(170, 46, (short)3500, true, true, true));
/*  89 */     addState(new TempState(197, 45, (short)3500, true, true, true));
/*  90 */     addState(new TempState(195, 47, (short)3500, true, true, true));
/*  91 */     addState(new TempState(198, 48, (short)3500, true, true, true));
/*  92 */     addState(new TempState(199, 49, (short)3500, true, true, true));
/*  93 */     addState(new TempState(196, 44, (short)3500, true, true, true));
/*  94 */     addState(new TempState(222, 220, (short)3500, true, true, true));
/*  95 */     addState(new TempState(206, 205, (short)3500, true, true, true));
/*  96 */     addState(new TempState(763, 764, (short)1500, true, true, true));
/*     */ 
/*     */     
/*  99 */     addState(new TempState(1160, 1161, (short)4000, true, true, false));
/* 100 */     addState(new TempState(1164, 1165, (short)5000, true, true, false));
/* 101 */     addState(new TempState(1168, 1169, (short)5000, true, true, false));
/* 102 */     addState(new TempState(1171, 1172, (short)5000, true, true, false));
/* 103 */     addState(new TempState(1251, 1252, (short)5500, true, true, false));
/*     */     
/* 105 */     addState(new TempState(1303, 1304, (short)4250, true, true, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addState(TempState state) {
/* 110 */     tempStates.add(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<TempState> getTempStates() {
/* 115 */     return tempStates;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkForChange(Item parent, Item target, short oldTemp, short newTemp, float qualityRatio) {
/* 121 */     for (TempState tempState : tempStates) {
/*     */       
/* 123 */       if (tempState.getOrigItemTemplateId() == target.getTemplateId())
/*     */       {
/* 125 */         return tempState.changeItem(parent, target, oldTemp, newTemp, qualityRatio);
/*     */       }
/*     */     } 
/* 128 */     if (newTemp > 1200 && !target.isLiquid() && target.isWrapped()) {
/*     */       
/* 130 */       if (Server.rand.nextInt(75) == 0)
/*     */       {
/*     */         
/* 133 */         if (target.canBeRawWrapped())
/*     */         {
/* 135 */           if (target.isMeat()) {
/* 136 */             target.setIsCooked();
/*     */           } else {
/* 138 */             target.setIsSteamed();
/*     */           }  } 
/* 140 */         target.setIsWrapped(false);
/*     */       }
/*     */     
/* 143 */     } else if (newTemp > 1500 && Server.rand.nextInt(75) == 0) {
/*     */       
/* 145 */       long lastowner = parent.getLastOwnerId();
/* 146 */       if (parent.isFoodMaker() || parent.getTemplate().isCooker())
/*     */       {
/*     */         
/* 149 */         for (Item i : parent.getItemsAsArray()) {
/*     */           
/* 151 */           if (i.getTemperature() < 1500)
/* 152 */             return false; 
/* 153 */           lastowner = i.getLastOwnerId();
/*     */         } 
/*     */       }
/* 156 */       Item realTarget = parent;
/* 157 */       Recipe recipe = Recipes.getRecipeFor(lastowner, (byte)1, (Item)null, parent, true, true);
/* 158 */       if (recipe == null && !target.isHollow()) {
/*     */         
/* 160 */         recipe = Recipes.getRecipeFor(lastowner, (byte)1, (Item)null, target, true, true);
/* 161 */         lastowner = target.getLastOwnerId();
/* 162 */         realTarget = target;
/*     */       } 
/* 164 */       if (recipe == null)
/* 165 */         return false; 
/* 166 */       ItemTemplate template = recipe.getResultTemplate(realTarget);
/*     */       
/* 168 */       Skill primSkill = null;
/* 169 */       Creature lastown = null;
/* 170 */       float alc = 0.0F;
/* 171 */       boolean chefMade = false;
/* 172 */       double bonus = 0.0D;
/* 173 */       boolean showOwner = false;
/*     */       
/*     */       try {
/* 176 */         lastown = Server.getInstance().getCreature(lastowner);
/* 177 */         bonus = lastown.getVillageSkillModifier();
/* 178 */         alc = Players.getInstance().getPlayer(lastowner).getAlcohol();
/* 179 */         Skills skills = lastown.getSkills();
/* 180 */         primSkill = skills.getSkillOrLearn(recipe.getSkillId());
/* 181 */         if (lastown.isRoyalChef())
/* 182 */           chefMade = true; 
/* 183 */         showOwner = (primSkill.getKnowledge(0.0D) > 70.0D);
/*     */       }
/* 185 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */       
/*     */       }
/* 189 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 194 */       int newWeight = 0;
/* 195 */       if (realTarget.isFoodMaker() || realTarget.getTemplate().isCooker()) {
/*     */ 
/*     */ 
/*     */         
/* 199 */         int liquid = 0;
/* 200 */         for (Item item : realTarget.getItemsAsArray()) {
/*     */           
/* 202 */           if (item.isLiquid()) {
/*     */             
/* 204 */             Ingredient ii = recipe.findMatchingIngredient(item);
/* 205 */             if (ii != null) {
/* 206 */               liquid = (int)(liquid + item.getWeightGrams() * (100 - ii.getLoss()) / 100.0F);
/*     */             }
/*     */           } else {
/* 209 */             newWeight += item.getWeightGrams();
/*     */           } 
/* 211 */         }  newWeight += liquid;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 216 */         newWeight = realTarget.getWeightGrams();
/*     */       } 
/*     */       
/* 219 */       int diff = recipe.getDifficulty(realTarget);
/* 220 */       float howHard = (recipe.getIngredientCount() + diff);
/* 221 */       if (template.isLiquid())
/* 222 */         howHard *= newWeight / template.getWeightGrams(); 
/* 223 */       float power = 10.0F;
/* 224 */       if (primSkill != null)
/* 225 */         power = (float)primSkill.skillCheck((diff + alc), null, bonus, false, howHard); 
/* 226 */       byte material = recipe.getResultMaterial(realTarget);
/*     */ 
/*     */       
/* 229 */       double avgQL = MethodsItems.getAverageQL(null, realTarget);
/* 230 */       double ql = Math.min(99.0D, Math.max(1.0D, avgQL + (power / 10.0F)));
/* 231 */       if (chefMade)
/* 232 */         ql = Math.max(30.0D, ql); 
/* 233 */       float maxMod = 1.0F;
/* 234 */       if (template.isLowNutrition()) {
/* 235 */         maxMod = 4.0F;
/* 236 */       } else if (template.isMediumNutrition()) {
/* 237 */         maxMod = 3.0F;
/* 238 */       } else if (template.isGoodNutrition()) {
/* 239 */         maxMod = 2.0F;
/* 240 */       } else if (template.isHighNutrition()) {
/* 241 */         maxMod = 1.0F;
/* 242 */       }  if (primSkill != null) {
/* 243 */         ql = Math.max(1.0D, Math.min(primSkill.getKnowledge(0.0D) * maxMod, ql));
/*     */       } else {
/* 245 */         ql = Math.max(1.0D, Math.min((20.0F * maxMod), ql));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       if (realTarget.getRarity() > 0)
/*     */       {
/* 257 */         ql += (100.0D - ql) / 20.0D * realTarget.getRarity();
/*     */       }
/*     */       
/*     */       try {
/* 261 */         byte rarity = 0;
/* 262 */         if (Server.rand.nextInt(500) == 0)
/*     */         {
/* 264 */           if (Server.rand.nextFloat() * 10000.0F <= 1.0F) {
/* 265 */             rarity = 3;
/* 266 */           } else if (Server.rand.nextInt(100) <= 0) {
/* 267 */             rarity = 2;
/* 268 */           } else if (Server.rand.nextBoolean()) {
/* 269 */             rarity = 1;
/*     */           }  } 
/* 271 */         ql = GeneralUtilities.calcRareQuality(ql, recipe.getLootableRarity(), realTarget.getRarity(), rarity);
/* 272 */         String owner = showOwner ? PlayerInfoFactory.getPlayerName(lastowner) : null;
/* 273 */         Item newItem = ItemFactory.createItem(template.getTemplateId(), (float)ql, material, rarity, owner);
/*     */         
/* 275 */         newItem.setIsSalted(ItemBehaviour.getSalted(null, realTarget));
/*     */         
/* 277 */         if (realTarget.isFoodMaker() || realTarget.getTemplate().isCooker()) {
/*     */           
/* 279 */           newItem.setWeight(newWeight, true);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 284 */           if (template.getTemplateId() == realTarget.getTemplateId()) {
/*     */             
/* 286 */             newItem.setQualityLevel(realTarget.getQualityLevel());
/* 287 */             newItem.setDamage(realTarget.getDamage());
/*     */           } 
/*     */           
/* 290 */           newItem.setWeight(newWeight, true);
/*     */         } 
/*     */         
/* 293 */         if (newWeight >= 0)
/*     */         {
/*     */           
/* 296 */           if (template.getWeightGrams() != newWeight) {
/* 297 */             MethodsItems.setSizes(realTarget, newWeight, newItem);
/*     */           }
/*     */         }
/* 300 */         if (RecipesByPlayer.saveRecipe(lastown, recipe, lastowner, null, realTarget) && lastown != null) {
/* 301 */           lastown.getCommunicator().sendServerMessage("Recipe \"" + recipe.getName() + "\" added to your cookbook.", 216, 165, 32, (byte)2);
/*     */         }
/*     */         
/* 304 */         newItem.calculateAndSaveNutrition(null, realTarget, recipe);
/*     */         
/* 306 */         if (lastown != null) {
/* 307 */           recipe.addAchievements(lastown, newItem);
/*     */         } else {
/* 309 */           recipe.addAchievementsOffline(lastowner, newItem);
/*     */         } 
/* 311 */         newItem.setName(recipe.getResultName(realTarget));
/*     */ 
/*     */ 
/*     */         
/* 315 */         ItemTemplate rit = recipe.getResultRealTemplate(realTarget);
/* 316 */         if (rit != null)
/* 317 */           newItem.setRealTemplate(rit.getTemplateId()); 
/* 318 */         if (recipe.hasResultState())
/* 319 */           newItem.setAuxData(recipe.getResultState()); 
/* 320 */         newItem.setTemperature((short)1500);
/*     */ 
/*     */         
/* 323 */         if (realTarget.getTemplateId() == 1236 || realTarget
/* 324 */           .getTemplateId() == 1223)
/*     */         {
/*     */           
/* 327 */           for (Item item : realTarget.getItemsAsArray())
/*     */           {
/* 329 */             Items.destroyItem(item.getWurmId());
/*     */           }
/*     */           
/* 332 */           Item c = realTarget.getParentOrNull();
/* 333 */           if (c != null)
/*     */           {
/* 335 */             Items.destroyItem(realTarget.getWurmId());
/* 336 */             c.insertItem(newItem);
/*     */             
/* 338 */             newItem.setLastOwnerId(lastowner);
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 346 */         else if (realTarget.isFoodMaker() || realTarget.getTemplate().isCooker())
/*     */         {
/* 348 */           long lastOwner = -10L;
/* 349 */           for (Item item : realTarget.getItemsAsArray())
/*     */           {
/* 351 */             Items.destroyItem(item.getWurmId());
/*     */           }
/* 353 */           if (newItem.isLiquid()) {
/*     */ 
/*     */ 
/*     */             
/* 357 */             int volAvail = realTarget.getFreeVolume();
/* 358 */             if (volAvail < newItem.getWeightGrams())
/* 359 */               newItem.setWeight(volAvail, true); 
/*     */           } 
/* 361 */           realTarget.insertItem(newItem);
/*     */           
/* 363 */           newItem.setLastOwnerId(lastowner);
/*     */         
/*     */         }
/*     */         else
/*     */         {
/* 368 */           Item c = realTarget.getParentOrNull();
/* 369 */           if (c != null)
/*     */           {
/* 371 */             Items.destroyItem(realTarget.getWurmId());
/* 372 */             c.insertItem(newItem);
/*     */             
/* 374 */             newItem.setLastOwnerId(lastowner);
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 383 */       catch (FailedException fe) {
/*     */ 
/*     */         
/* 386 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*     */       }
/* 388 */       catch (NoSuchTemplateException nste) {
/*     */ 
/*     */         
/* 391 */         logger.log(Level.WARNING, nste.getMessage(), (Throwable)nste);
/*     */       } 
/*     */     } 
/* 394 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getFoodTemplateFor(Item cookingItem) {
/* 399 */     switch (cookingItem.template.templateId) {
/*     */       
/*     */       case 75:
/* 402 */         return 347;
/*     */       case 351:
/* 404 */         return 352;
/*     */       case 77:
/* 406 */         return 346;
/*     */       case 350:
/* 408 */         return 348;
/*     */       case 287:
/* 410 */         return 345;
/*     */     } 
/* 412 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 414 */       logger.finer("Returning stew template for unexpected cookingItem: " + cookingItem);
/*     */     }
/*     */     
/* 417 */     return 345;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\TempStates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
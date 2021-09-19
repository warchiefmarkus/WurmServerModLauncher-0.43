/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Point4f;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.ai.scripts.FishAI;
/*      */ import com.wurmonline.server.items.ContainerRestriction;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.WaterType;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.FishingEnums;
/*      */ import java.awt.Color;
/*      */ import java.util.ArrayList;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MethodsFishing
/*      */   implements MiscConstants
/*      */ {
/*   75 */   private static final Logger logger = Logger.getLogger(MethodsFishing.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean fish(Creature performer, Item source, int tilex, int tiley, int tile, float counter, Action act) {
/*   99 */     if (!Terraforming.isTileUnderWater(tile, tilex, tiley, performer.isOnSurface())) {
/*      */       
/*  101 */       performer.getCommunicator().sendNormalServerMessage("The water is too shallow to fish.");
/*  102 */       return true;
/*      */     } 
/*  104 */     Item[] fishingItems = source.getFishingItems();
/*  105 */     Item fishingReel = fishingItems[0];
/*  106 */     Item fishingLine = fishingItems[1];
/*  107 */     Item fishingFloat = fishingItems[2];
/*  108 */     Item fishingHook = fishingItems[3];
/*  109 */     Item fishingBait = fishingItems[4];
/*  110 */     Skill fishing = performer.getSkills().getSkillOrLearn(10033);
/*  111 */     int timeleft = 1800;
/*  112 */     int defaultTimer = 1800;
/*  113 */     byte startCommand = 0;
/*  114 */     if (source.getTemplateId() == 1343) {
/*      */       
/*  116 */       startCommand = 40;
/*  117 */       if (performer.getVehicle() != -10L)
/*      */       {
/*  119 */         performer.getCommunicator().sendNormalServerMessage("You cannot use a fishing net whilst on a vehicle.");
/*      */         
/*  121 */         return true;
/*      */       }
/*      */     
/*  124 */     } else if (source.getTemplateId() == 705 || source.getTemplateId() == 707) {
/*      */       
/*  126 */       startCommand = 20;
/*  127 */       if (performer.getVehicle() != -10L)
/*      */       {
/*  129 */         performer.getCommunicator().sendNormalServerMessage("You cannot use a spear to fish whilst on a vehicle.");
/*      */         
/*  131 */         if (counter != 1.0F)
/*  132 */           sendSpearStop(performer, act); 
/*  133 */         return true;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  138 */       startCommand = 0;
/*  139 */       boolean failed = false;
/*  140 */       if (source.getTemplateId() == 1344) {
/*      */ 
/*      */         
/*  143 */         if (act.getCounterAsFloat() < act.getFailSecond()) {
/*      */ 
/*      */           
/*  146 */           if (fishingLine == null || fishingHook == null) {
/*      */             
/*  148 */             performer.getCommunicator().sendNormalServerMessage("Fishing pole needs a line with a fishing hook to be able to catch fish.");
/*      */             
/*  150 */             failed = true;
/*      */           } 
/*  152 */           if (fishingFloat == null) {
/*      */             
/*  154 */             performer.getCommunicator().sendNormalServerMessage("Fishing pole needs a float for you to be able to see when a fish can be hooked.");
/*      */             
/*  156 */             failed = true;
/*      */           } 
/*      */         } 
/*      */         
/*  160 */         if (failed)
/*      */         {
/*  162 */           act.setFailSecond(act.getCounterAsFloat());
/*      */         
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  168 */         if (act.getCounterAsFloat() < act.getFailSecond()) {
/*      */           
/*  170 */           if (fishingReel == null || fishingLine == null || fishingHook == null) {
/*      */             
/*  172 */             performer.getCommunicator().sendNormalServerMessage("Fishing rod needs a reel, line and a fishing hook to be able to catch fish.");
/*      */             
/*  174 */             failed = true;
/*      */           } 
/*  176 */           if (fishingFloat == null) {
/*      */             
/*  178 */             performer.getCommunicator().sendNormalServerMessage("Fishing rod needs a float for you to be able to see when a fish can be hooked.");
/*      */             
/*  180 */             failed = true;
/*      */           } 
/*      */         } 
/*      */         
/*  184 */         if (failed)
/*      */         {
/*  186 */           act.setFailSecond(act.getCounterAsFloat());
/*      */         }
/*      */       } 
/*  189 */       if (failed)
/*      */       {
/*      */         
/*  192 */         if (counter != 1.0F && act.getCreature() != null) {
/*      */ 
/*      */           
/*  195 */           byte b = (byte)act.getTickCount();
/*  196 */           if (b != 15)
/*      */           {
/*  198 */             processFishMovedOn(performer, act, 2.2F);
/*  199 */             act.setTickCount(15);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  204 */           sendFishStop(performer, act);
/*  205 */           return true;
/*      */         } 
/*      */       }
/*      */     } 
/*  209 */     if (counter == 1.0F) {
/*      */       String ss; float radi[], minRadius, maxRadius; byte rodType, rodMaterial; FishEnums.ReelType reelType; byte reelMaterial; FishEnums.FloatType floatType; FishEnums.HookType hookType; byte hookMaterial; FishEnums.BaitType baitType;
/*  211 */       if (fishingTestsFailed(performer, act.getPosX(), act.getPosY())) {
/*  212 */         return true;
/*      */       }
/*  214 */       switch (startCommand) {
/*      */ 
/*      */         
/*      */         case 40:
/*  218 */           if (!source.isEmpty(false)) {
/*      */             
/*  220 */             performer.getCommunicator().sendNormalServerMessage("The net is too unwieldly for you to cast, please empty it first!");
/*  221 */             return true;
/*      */           } 
/*  223 */           performer.getCommunicator().sendNormalServerMessage("You throw out the net and start slowly pulling it in, hoping to catch some small fish.");
/*  224 */           Server.getInstance().broadCastAction(performer.getName() + " throws out a net and starts fishing.", performer, 5);
/*      */           
/*  226 */           timeleft = 50 + Server.rand.nextInt(250);
/*  227 */           defaultTimer = 600;
/*      */           
/*  229 */           act.setTickCount(40);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 20:
/*  234 */           performer.getCommunicator().sendNormalServerMessage("You stand still to wait for a passing fish, are they like buses?");
/*  235 */           Server.getInstance().broadCastAction(performer.getName() + " stands still looking at the water.", performer, 5);
/*  236 */           performer.getCommunicator().sendSpearStart();
/*      */           
/*  238 */           timeleft = 100 + Server.rand.nextInt(250) - source.getRarity() * 10;
/*      */           
/*  240 */           act.setTickCount(21);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 0:
/*  245 */           ss = (performer.getVehicle() == -10L) ? "stand" : "sit";
/*  246 */           performer.getCommunicator().sendNormalServerMessage("You " + ss + " still and get ready to cast.");
/*  247 */           Server.getInstance().broadCastAction(performer.getName() + " gets ready to cast.", performer, 5);
/*      */           
/*  249 */           radi = getMinMaxRadius(source, fishingLine);
/*  250 */           minRadius = radi[0];
/*  251 */           maxRadius = radi[1];
/*      */           
/*  253 */           rodType = getRodType(source, fishingReel, fishingLine);
/*  254 */           rodMaterial = source.getMaterial();
/*  255 */           reelType = FishEnums.ReelType.fromItem(fishingReel);
/*  256 */           reelMaterial = (fishingReel == null) ? 0 : fishingReel.getMaterial();
/*  257 */           floatType = FishEnums.FloatType.fromItem(fishingFloat);
/*  258 */           hookType = FishEnums.HookType.fromItem(fishingHook);
/*  259 */           hookMaterial = fishingHook.getMaterial();
/*  260 */           baitType = FishEnums.BaitType.fromItem(fishingBait);
/*  261 */           performer.getCommunicator().sendFishStart(minRadius, maxRadius, rodType, rodMaterial, reelType.getTypeId(), reelMaterial, floatType
/*  262 */               .getTypeId(), baitType.getTypeId());
/*      */           
/*  264 */           remember(source, reelType, reelMaterial, floatType, hookType, hookMaterial, baitType);
/*      */ 
/*      */           
/*  267 */           timeleft = 300;
/*      */           
/*  269 */           defaultTimer = timeleft;
/*      */           
/*  271 */           act.setTickCount(0);
/*      */           break;
/*      */       } 
/*      */       
/*  275 */       act.setTimeLeft(defaultTimer);
/*  276 */       act.setData(timeleft);
/*  277 */       performer.sendActionControl(Actions.actionEntrys[160].getVerbString(), true, defaultTimer);
/*  278 */       return false;
/*      */     } 
/*  280 */     byte currentPhase = (byte)act.getTickCount();
/*      */     
/*  282 */     if (canTimeOut(act) && act.getSecond() * 10 > act.getTimeLeft()) {
/*      */ 
/*      */       
/*  285 */       switch (startCommand) {
/*      */ 
/*      */ 
/*      */         
/*      */         case 40:
/*  290 */           performer.getCommunicator().sendNormalServerMessage("You finish pulling in the net.");
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 20:
/*  296 */           performer.getCommunicator().sendNormalServerMessage("You speared nothing!");
/*  297 */           sendSpearStop(performer, act);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 0:
/*  302 */           switch (currentPhase) {
/*      */ 
/*      */ 
/*      */             
/*      */             case 0:
/*  307 */               performer.getCommunicator().sendNormalServerMessage("You never cast your line, so caught nothing.");
/*  308 */               return sendFishStop(performer, act);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  313 */           performer.getCommunicator().sendNormalServerMessage("You did not catch anything!");
/*  314 */           sendFishStop(performer, act);
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  321 */       return true;
/*      */     } 
/*  323 */     if (currentPhase == 17) {
/*      */ 
/*      */ 
/*      */       
/*  327 */       Creature fish = act.getCreature();
/*  328 */       if (performer.isWithinDistanceTo(fish, 1.0F)) {
/*      */         
/*  330 */         act.setTickCount(12);
/*      */       }
/*  332 */       else if (!performer.isWithinDistanceTo(fish, 10.0F)) {
/*      */         
/*  334 */         act.setTickCount(13);
/*      */       } 
/*      */     } 
/*      */     
/*  338 */     timeleft = (int)act.getData();
/*  339 */     if (act.getSecond() * 10 > timeleft || isInstant(currentPhase))
/*      */     {
/*      */       
/*  342 */       switch (startCommand) {
/*      */ 
/*      */         
/*      */         case 40:
/*  346 */           if (processNetPhase(performer, currentPhase, source, act, tilex, tiley, fishing)) {
/*  347 */             return true;
/*      */           }
/*      */           break;
/*      */         
/*      */         case 20:
/*  352 */           if (processSpearPhase(performer, currentPhase, source, act, fishing)) {
/*  353 */             return true;
/*      */           }
/*      */           break;
/*      */         
/*      */         case 0:
/*  358 */           if (processFishPhase(performer, currentPhase, source, act, fishing)) {
/*  359 */             return true;
/*      */           }
/*      */           break;
/*      */       } 
/*      */     }
/*  364 */     if (act.justTickedSecond()) {
/*      */       
/*  366 */       if (act.getSecond() % 2 == 0)
/*      */       {
/*  368 */         if (fishingTestsFailed(performer, act.getPosX(), act.getPosY())) {
/*      */           
/*  370 */           switch (startCommand) {
/*      */ 
/*      */             
/*      */             case 40:
/*  374 */               return true;
/*      */ 
/*      */             
/*      */             case 20:
/*  378 */               sendSpearStop(performer, act);
/*  379 */               return true;
/*      */ 
/*      */             
/*      */             case 0:
/*  383 */               sendFishStop(performer, act);
/*  384 */               return true;
/*      */           } 
/*      */           
/*  387 */           return true;
/*      */         } 
/*      */       }
/*      */       
/*  391 */       Creature fish = act.getCreature();
/*  392 */       if (fish != null && startCommand == 20) {
/*      */         
/*  394 */         FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/*  395 */         if (fish.getPosX() == faid.getTargetPosX() && fish.getPosY() == faid.getTargetPosY()) {
/*      */           
/*  397 */           testMessage(performer, "Fish out of range? Swam away!", "");
/*      */           
/*  399 */           act.setTickCount(28);
/*      */         } 
/*      */       } 
/*      */     } 
/*  403 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isInstant(byte currentPhase) {
/*  413 */     switch (currentPhase) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 3:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 9:
/*      */       case 10:
/*      */       case 13:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 27:
/*      */       case 28:
/*  431 */         return true;
/*      */     } 
/*  433 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean canTimeOut(Action act) {
/*  444 */     Creature fish = act.getCreature();
/*  445 */     return (fish == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void remember(Item rod, FishEnums.ReelType reelType, byte reelMaterial, FishEnums.FloatType floatType, FishEnums.HookType hookType, byte hookMaterial, FishEnums.BaitType baitType) {
/*  462 */     int types = (reelType.getTypeId() << 12) + (floatType.getTypeId() << 8) + (hookType.getTypeId() << 4) + baitType.getTypeId();
/*  463 */     int materials = (reelMaterial << 8) + hookMaterial;
/*  464 */     rod.setData(types, materials);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void playerOutOfRange(Creature performer, Action act) {
/*  474 */     Item source = act.getSubject();
/*  475 */     int stid = (source == null) ? 0 : source.getTemplateId();
/*  476 */     switch (stid) {
/*      */ 
/*      */       
/*      */       case 705:
/*      */       case 707:
/*  481 */         sendSpearStop(performer, act);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1344:
/*      */       case 1346:
/*  487 */         sendFishStop(performer, act);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processNetPhase(Creature performer, byte currentPhase, Item net, Action act, int tilex, int tiley, Skill fishing) {
/*  507 */     float posx = ((tilex << 2) + 2);
/*  508 */     float posy = ((tiley << 2) + 2);
/*      */     
/*  510 */     FishRow fr = caughtFish(performer, posx, posy, net);
/*      */     
/*  512 */     if (fr != null) {
/*      */       
/*  514 */       int weight = makeDeadFish(performer, act, fishing, fr.getFishTypeId(), net, net);
/*      */       
/*  516 */       FishEnums.FishData fd = FishEnums.FishData.fromInt(fr.getFishTypeId());
/*  517 */       float damMod = fd.getDamageMod() * Math.max(0.1F, weight / 3000.0F);
/*  518 */       float additionalDamage = additionalDamage(net, damMod * 10.0F, false);
/*  519 */       if (additionalDamage > 0.0F) {
/*      */         
/*  521 */         float newDam = net.getDamage() + additionalDamage;
/*  522 */         if (newDam >= 100.0F)
/*      */         {
/*  524 */           if (!net.isEmpty(false)) {
/*      */ 
/*      */             
/*  527 */             performer.getCommunicator().sendNormalServerMessage("As the " + net.getName() + " disintegrates, the fish inside all swim away");
/*  528 */             destroyContents(net);
/*      */           } 
/*      */         }
/*  531 */         net.setDamage(newDam);
/*      */       } 
/*      */     } 
/*      */     
/*  535 */     int moreTime = 50 + Server.rand.nextInt(250);
/*  536 */     int timeleft = act.getSecond() * 10 + moreTime;
/*  537 */     act.setData(timeleft);
/*  538 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processSpearPhase(Creature performer, byte currentPhase, Item spear, Action act, Skill fishing) {
/*      */     Creature fish;
/*  553 */     switch (currentPhase) {
/*      */ 
/*      */       
/*      */       case 27:
/*  557 */         return processSpearCancel(performer, act);
/*      */ 
/*      */       
/*      */       case 21:
/*  561 */         return processSpearMove(performer, act, spear, fishing);
/*      */ 
/*      */       
/*      */       case 28:
/*  565 */         return processSpearSwamAway(performer, act, spear, 50);
/*      */ 
/*      */       
/*      */       case 24:
/*  569 */         testMessage(performer, "You launch the spear at nothing!", "");
/*  570 */         act.setTickCount(21);
/*  571 */         return false;
/*      */ 
/*      */       
/*      */       case 23:
/*  575 */         return processSpearMissed(performer, act, spear);
/*      */ 
/*      */       
/*      */       case 22:
/*  579 */         return processSpearHit(performer, act, fishing, spear);
/*      */ 
/*      */       
/*      */       case 26:
/*  583 */         fish = act.getCreature();
/*  584 */         testMessage(performer, "You launch the spear at " + fish.getName() + ".", " @fx:" + 
/*  585 */             (int)fish.getPosX() + " fy:" + (int)fish.getPosY());
/*  586 */         performer.getCommunicator().sendSpearStrike(fish.getPosX(), fish.getPosY());
/*  587 */         return processSpearStrike(performer, act, fishing, spear, fish.getPosX(), fish.getPosY());
/*      */     } 
/*      */     
/*  590 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishPhase(Creature performer, byte currentPhase, Item rod, Action act, Skill fishing) {
/*      */     Creature fish;
/*      */     String fname;
/*  606 */     switch (currentPhase) {
/*      */ 
/*      */       
/*      */       case 10:
/*  610 */         return processFishCancel(performer, act);
/*      */ 
/*      */       
/*      */       case 1:
/*  614 */         return processFishBite(performer, act, rod, fishing);
/*      */ 
/*      */       
/*      */       case 18:
/*  618 */         return processFishPause(performer, act);
/*      */ 
/*      */       
/*      */       case 16:
/*  622 */         if (processFishMove(performer, act, fishing, rod)) {
/*      */           
/*  624 */           sendFishStop(performer, act);
/*  625 */           return true;
/*      */         } 
/*  627 */         return false;
/*      */ 
/*      */       
/*      */       case 2:
/*  631 */         return processFishMovedOn(performer, act);
/*      */ 
/*      */       
/*      */       case 19:
/*  635 */         return processFishMovingOn(performer, act);
/*      */ 
/*      */       
/*      */       case 14:
/*  639 */         return processFishSwamAway(performer, act, rod, 50);
/*      */ 
/*      */       
/*      */       case 3:
/*  643 */         return processFishHooked(performer, act, fishing, rod);
/*      */ 
/*      */       
/*      */       case 11:
/*  647 */         return processFishStrike(performer, act, fishing, rod);
/*      */ 
/*      */       
/*      */       case 17:
/*  651 */         if (processFishPull(performer, act, fishing, rod, false)) {
/*      */           
/*  653 */           Creature creature = act.getCreature();
/*  654 */           String str = (creature == null) ? "fish" : creature.getName();
/*  655 */           performer.getCommunicator().sendNormalServerMessage("The " + str + " swims off.");
/*  656 */           sendFishStop(performer, act);
/*  657 */           return true;
/*      */         } 
/*  659 */         return false;
/*      */ 
/*      */       
/*      */       case 12:
/*  663 */         return processFishCaught(performer, act, fishing, rod);
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/*  668 */         return sendFishStop(performer, act);
/*      */ 
/*      */       
/*      */       case 5:
/*  672 */         performer.getCommunicator().sendNormalServerMessage("You hooked nothing while reeling in your line.");
/*  673 */         return sendFishStop(performer, act);
/*      */ 
/*      */       
/*      */       case 13:
/*  677 */         fish = act.getCreature();
/*  678 */         fname = (fish == null) ? "fish" : fish.getName();
/*  679 */         performer.getCommunicator().sendNormalServerMessage("The " + fname + " managed to jump the fishing hook!");
/*  680 */         return sendFishStop(performer, act);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 7:
/*  690 */         return processFishLineSnapped(performer, act, rod);
/*      */ 
/*      */       
/*      */       case 15:
/*  694 */         return sendFishStop(performer, act);
/*      */     } 
/*      */     
/*  697 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean fishingTestsFailed(Creature performer, float posx, float posy) {
/*  709 */     int tilex = (int)posx >> 2;
/*  710 */     int tiley = (int)posy >> 2;
/*  711 */     if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */       
/*  713 */       performer.getCommunicator().sendNormalServerMessage("A huge shadow moves beneath the waves, and you reel in the line in panic.");
/*      */       
/*  715 */       return true;
/*      */     } 
/*  717 */     if (performer.getInventory().getNumItemsNotCoins() >= 100) {
/*      */       
/*  719 */       performer.getCommunicator().sendNormalServerMessage("You wouldn't be able to carry the fish. Drop something first.");
/*      */       
/*  721 */       return true;
/*      */     } 
/*  723 */     if (!performer.canCarry(1000)) {
/*      */       
/*  725 */       performer.getCommunicator().sendNormalServerMessage("You are too heavily loaded. Drop something first.");
/*      */       
/*  727 */       return true;
/*      */     } 
/*  729 */     int depth = FishEnums.getWaterDepth(performer.getPosX(), performer.getPosY(), performer.isOnSurface());
/*  730 */     if (performer.getBridgeId() == -10L && depth > 10 && performer.getVehicle() == -10L) {
/*      */       
/*  732 */       performer.getCommunicator().sendNormalServerMessage("You can't swim and fish at the same time.");
/*  733 */       return true;
/*      */     } 
/*  735 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean destroyFishCreature(Action act) {
/*  745 */     Creature fish = act.getCreature();
/*  746 */     act.setCreature(null);
/*  747 */     if (fish != null)
/*  748 */       fish.destroy(); 
/*  749 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isFishSpotValid(Point point) {
/*  760 */     if (WaterType.getWaterType(Zones.safeTileX(point.getX()), Zones.safeTileY(point.getY()), true) == 4) {
/*      */ 
/*      */       
/*  763 */       float ht = 0.0F;
/*      */       
/*      */       try {
/*  766 */         ht = Zones.calculateHeight(point.getX(), point.getY(), true) * 10.0F;
/*      */       }
/*  768 */       catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  773 */       if (ht < -100.0F)
/*  774 */         return true; 
/*      */     } 
/*  776 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Point[] getSpecialSpots(int tilex, int tiley, int season) {
/*  789 */     ArrayList<Point> specialSpots = new ArrayList<>();
/*  790 */     int zoneX = tilex / 128;
/*  791 */     int zoneY = tiley / 128;
/*      */     
/*  793 */     for (FishEnums.FishData fd : FishEnums.FishData.values()) {
/*      */       
/*  795 */       if (fd.isSpecialFish()) {
/*      */         
/*  797 */         Point tp = fd.getSpecialSpot(zoneX, zoneY, season);
/*  798 */         if ((tilex == 0 && tiley == 0) || isFishSpotValid(tp))
/*  799 */           specialSpots.add(tp); 
/*      */       } 
/*      */     } 
/*  802 */     return specialSpots.<Point>toArray(new Point[specialSpots.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FishRow[] getFishTable(Creature performer, float posX, float posY, Item rod) {
/*  815 */     Skill fishing = performer.getSkills().getSkillOrLearn(10033);
/*  816 */     float knowledge = (float)fishing.getKnowledge();
/*  817 */     Item[] fishingItems = rod.getFishingItems();
/*  818 */     Item fishingReel = fishingItems[0];
/*  819 */     Item fishingLine = fishingItems[1];
/*  820 */     Item fishingFloat = fishingItems[2];
/*  821 */     Item fishingHook = fishingItems[3];
/*  822 */     Item fishingBait = fishingItems[4];
/*  823 */     float totalChances = 0.0F;
/*  824 */     FishRow[] fishTable = new FishRow[FishEnums.FishData.getLength()];
/*  825 */     for (FishEnums.FishData fd : FishEnums.FishData.values()) {
/*      */       
/*  827 */       fishTable[fd.getTypeId()] = new FishRow(fd.getTypeId(), fd.getName());
/*      */       
/*  829 */       if (fd.getTypeId() != FishEnums.FishData.NONE.getTypeId() && fd.getTypeId() != FishEnums.FishData.CLAM.getTypeId()) {
/*      */         
/*  831 */         float fishChance = fd.getChance(knowledge, rod, fishingReel, fishingLine, fishingFloat, fishingHook, fishingBait, posX, posY, performer
/*  832 */             .isOnSurface());
/*  833 */         fishTable[fd.getTypeId()].setChance(fishChance);
/*  834 */         totalChances += fishChance;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  839 */     if (totalChances > 99.0F) {
/*      */       
/*  841 */       float percentageFactor = totalChances / 99.0F;
/*  842 */       totalChances = 0.0F;
/*      */       
/*  844 */       for (FishEnums.FishData fd : FishEnums.FishData.values()) {
/*      */         
/*  846 */         float chance = fishTable[fd.getTypeId()].getChance();
/*  847 */         if (chance > 0.0F)
/*      */         {
/*  849 */           fishTable[fd.getTypeId()].setChance(chance / percentageFactor);
/*      */         }
/*  851 */         totalChances += fishTable[fd.getTypeId()].getChance();
/*      */       } 
/*      */     } 
/*  854 */     if (totalChances < 100.0F)
/*      */     {
/*      */       
/*  857 */       fishTable[FishEnums.FishData.CLAM.getTypeId()].setChance(Math.min(2.0F, 100.0F - totalChances));
/*      */     }
/*  859 */     return fishTable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean showFishTable(Creature performer, Item source, int tileX, int tileY, float counter, Action act) {
/*  898 */     int time = act.getTimeLeft();
/*  899 */     if (counter == 1.0F) {
/*      */       
/*  901 */       time = 250;
/*  902 */       act.setTimeLeft(time);
/*  903 */       performer.getCommunicator().sendNormalServerMessage("You start looking around for what fish might be in this area.");
/*  904 */       performer.sendActionControl(act.getActionString(), true, time);
/*      */     }
/*  906 */     else if (act.justTickedSecond() && act.getSecond() == 5) {
/*      */       
/*  908 */       if (source.getTemplateId() == 1344) {
/*      */         
/*  910 */         Item[] fishingItems = source.getFishingItems();
/*  911 */         if (fishingItems[1] == null) {
/*      */           
/*  913 */           performer.getCommunicator().sendNormalServerMessage("Your pole is missing a line, float and fishing hook!");
/*  914 */           return true;
/*      */         } 
/*  916 */         if (fishingItems[2] == null) {
/*      */           
/*  918 */           if (fishingItems[3] == null) {
/*      */             
/*  920 */             performer.getCommunicator().sendNormalServerMessage("Your pole is missing a float and fishing hook!");
/*  921 */             return true;
/*      */           } 
/*      */ 
/*      */           
/*  925 */           performer.getCommunicator().sendNormalServerMessage("Your pole is missing a float!");
/*  926 */           return true;
/*      */         } 
/*      */         
/*  929 */         if (fishingItems[3] == null) {
/*      */           
/*  931 */           performer.getCommunicator().sendNormalServerMessage("Your pole is missing a fishing hook!");
/*  932 */           return true;
/*      */         } 
/*  934 */         if (fishingItems[4] == null) {
/*  935 */           performer.getCommunicator().sendNormalServerMessage("Your pole looks all set, but you think catching something could be easier using bait.");
/*      */         } else {
/*  937 */           performer.getCommunicator().sendNormalServerMessage("Your pole looks all set.");
/*      */         } 
/*  939 */       } else if (source.getTemplateId() == 1346) {
/*      */         
/*  941 */         Item[] fishingItems = source.getFishingItems();
/*  942 */         if (fishingItems[0] == null) {
/*      */           
/*  944 */           performer.getCommunicator().sendNormalServerMessage("Your rod is missing a reel, line, float and fishing hook!");
/*  945 */           return true;
/*      */         } 
/*  947 */         if (fishingItems[1] == null) {
/*      */           
/*  949 */           performer.getCommunicator().sendNormalServerMessage("Your rod is missing a line, float and fishing hook!");
/*  950 */           return true;
/*      */         } 
/*  952 */         if (fishingItems[2] == null) {
/*      */           
/*  954 */           if (fishingItems[3] == null) {
/*      */             
/*  956 */             performer.getCommunicator().sendNormalServerMessage("Your rod is missing a float and fishing hook!");
/*  957 */             return true;
/*      */           } 
/*      */ 
/*      */           
/*  961 */           performer.getCommunicator().sendNormalServerMessage("Your rod is missing a float!");
/*  962 */           return true;
/*      */         } 
/*      */         
/*  965 */         if (fishingItems[3] == null) {
/*      */           
/*  967 */           performer.getCommunicator().sendNormalServerMessage("Your rod is missing a fishing hook!");
/*  968 */           return true;
/*      */         } 
/*  970 */         if (fishingItems[4] == null) {
/*  971 */           performer.getCommunicator().sendNormalServerMessage("Your rod looks all set, but you think catching something could be easier using bait.");
/*      */         } else {
/*  973 */           performer.getCommunicator().sendNormalServerMessage("Your rod looks all set.");
/*      */         } 
/*      */       } 
/*      */     } 
/*  977 */     double fishingSkill = performer.getSkills().getSkillOrLearn(10033).getKnowledge(0.0D);
/*  978 */     float posx = ((tileX << 2) + 2);
/*  979 */     float posy = ((tileY << 2) + 2);
/*  980 */     String waterType = WaterType.getWaterTypeString(tileX, tileY, performer.isOnSurface()).toLowerCase();
/*  981 */     int waterDepth = FishEnums.getWaterDepth(posx, posy, performer.isOnSurface());
/*  982 */     if (act.justTickedSecond() && act.getSecond() == 10) {
/*      */       
/*  984 */       if (fishingSkill < 10.0D) {
/*  985 */         performer.getCommunicator().sendNormalServerMessage("You're not too sure what type of water you're standing in, perhaps with a bit more fishing knowledge you'll understand better.");
/*      */       }
/*  987 */       else if (fishingSkill < 40.0D) {
/*  988 */         performer.getCommunicator().sendNormalServerMessage("You believe this area of water to be " + waterType + ".");
/*      */       } else {
/*  990 */         performer.getCommunicator().sendNormalServerMessage("You believe this area of water to be " + waterType + " around a depth of " + (waterDepth / 10) + "m.");
/*      */       }
/*      */     
/*  993 */     } else if (act.justTickedSecond() && act.getSecond() == 15) {
/*      */       
/*  995 */       FishRow[] fishChances = getFishTable(performer, posx, posy, source);
/*  996 */       FishRow topChance = null;
/*  997 */       for (FishRow fishChance : fishChances) {
/*      */         
/*  999 */         if (!FishEnums.FishData.fromName(fishChance.getName()).isSpecialFish()) {
/*      */           
/* 1001 */           float chance = fishChance.getChance();
/* 1002 */           if (chance > 0.0F && (topChance == null || topChance.getChance() < chance))
/* 1003 */             topChance = fishChance; 
/*      */         } 
/* 1005 */       }  if (topChance == null) {
/*      */         
/* 1007 */         performer.getCommunicator().sendNormalServerMessage("You can't find anything that you'll catch in this area with the " + source.getName() + ".");
/* 1008 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1012 */       performer.getCommunicator().sendNormalServerMessage("The most common fish around here that you think you might catch with the " + source.getName() + " is a " + topChance
/* 1013 */           .getName() + ".");
/*      */     
/*      */     }
/* 1016 */     else if (act.justTickedSecond() && act.getSecond() == 25) {
/*      */       
/* 1018 */       FishRow[] fishChances = getFishTable(performer, posx, posy, source);
/* 1019 */       FishRow topChance = null;
/* 1020 */       ArrayList<FishRow> otherChances = new ArrayList<>();
/* 1021 */       for (FishRow fishChance : fishChances) {
/*      */         
/* 1023 */         if (!FishEnums.FishData.fromName(fishChance.getName()).isSpecialFish()) {
/*      */           
/* 1025 */           float chance = fishChance.getChance();
/* 1026 */           if (chance > 0.0F) {
/*      */             
/* 1028 */             if (topChance == null || topChance.getChance() < chance)
/* 1029 */               topChance = fishChance; 
/* 1030 */             if (chance > (100.0D - fishingSkill) / 5.0D + 5.0D)
/* 1031 */               otherChances.add(fishChance); 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1035 */       otherChances.remove(topChance);
/* 1036 */       if (otherChances.isEmpty() || fishingSkill < 20.0D) {
/* 1037 */         performer.getCommunicator().sendNormalServerMessage("You're not sure of what other fish you might find here.");
/*      */       } else {
/*      */         
/* 1040 */         String allOthers = "";
/* 1041 */         for (int i = 0; i < otherChances.size(); i++) {
/*      */           
/* 1043 */           if (i > 0 && i == otherChances.size() - 1) {
/* 1044 */             allOthers = allOthers + " and ";
/* 1045 */           } else if (i > 0) {
/* 1046 */             allOthers = allOthers + ", ";
/* 1047 */           }  allOthers = allOthers + ((FishRow)otherChances.get(i)).getName();
/*      */         } 
/* 1049 */         performer.getCommunicator().sendNormalServerMessage("You also think the " + source.getName() + " will be useful to catch " + allOthers + " in this area.");
/*      */       } 
/*      */       
/* 1052 */       return true;
/*      */     } 
/* 1054 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static FishRow caughtFish(Creature performer, float posX, float posY, Item rod) {
/* 1117 */     FishRow[] fishChances = getFishTable(performer, posX, posY, rod);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1124 */     float rno = Server.rand.nextFloat() * 100.0F;
/* 1125 */     float runningTotal = 0.0F;
/* 1126 */     for (FishRow fishChance : fishChances) {
/*      */       
/* 1128 */       if (fishChance.getChance() > 0.0F) {
/*      */         
/* 1130 */         runningTotal += fishChance.getChance();
/* 1131 */         if (runningTotal > rno)
/*      */         {
/*      */           
/* 1134 */           return fishChance;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1139 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean processSpearStrike(Creature performer, Action act, Skill fishing, Item spear, float posX, float posY) {
/* 1158 */     Creature fish = act.getCreature();
/* 1159 */     if (fish == null) {
/*      */ 
/*      */       
/* 1162 */       act.setTickCount(24);
/* 1163 */       return false;
/*      */     } 
/* 1165 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/*      */ 
/*      */     
/* 1168 */     float dx = Math.abs(fish.getStatus().getPositionX() - posX);
/* 1169 */     float dy = Math.abs(fish.getStatus().getPositionY() - posY);
/* 1170 */     float dd = (float)Math.sqrt((dx * dx + dy * dy));
/* 1171 */     testMessage(performer, "Distance away was " + dd + " fish:" + fish.getStatus().getPositionX() + "," + fish.getStatus().getPositionY() + " strike:" + posX + "," + posY, "");
/*      */     
/* 1173 */     performer.sendSpearStrike(posX, posY);
/* 1174 */     if (dd > 1.0F) {
/*      */       
/* 1176 */       performer.getCommunicator().sendNormalServerMessage("You missed the " + faid.getNameWithSize() + "!");
/* 1177 */       Server.getInstance().broadCastAction(performer
/* 1178 */           .getName() + " attempted to spear a fish and failed!", performer, 5);
/*      */       
/* 1180 */       act.setTickCount(23);
/* 1181 */       return false;
/*      */     } 
/* 1183 */     float result = getDifficulty(performer, act, performer.getPosX(), performer.getPosY(), fishing, spear, null, null, null, null, null, 0.0F, 100.0F);
/*      */     
/* 1185 */     if (result <= 0.0F) {
/*      */ 
/*      */       
/* 1188 */       if (result < -80.0F && performer.isWithinDistanceTo(posX, posY, 0.0F, 2.0F)) {
/*      */ 
/*      */         
/* 1191 */         Skill bodyStr = performer.getSkills().getSkillOrLearn(102);
/* 1192 */         byte foot = Server.rand.nextBoolean() ? 15 : 16;
/* 1193 */         performer.getCommunicator().sendNormalServerMessage("You miss the " + fish
/* 1194 */             .getName() + " and hit your own foot!");
/* 1195 */         Server.getInstance().broadCastAction(performer
/* 1196 */             .getName() + " spears their own foot.. how silly!", performer, 5);
/*      */         
/* 1198 */         performer.addWoundOfType(null, (byte)2, foot, false, 1.0F, true, 400.0D * bodyStr.getKnowledge(0.0D), 0.0F, 0.0F, false, false);
/*      */       }
/*      */       else {
/*      */         
/* 1202 */         performer.getCommunicator().sendNormalServerMessage("You missed the " + fish.getName() + "!");
/* 1203 */         Server.getInstance().broadCastAction(performer
/* 1204 */             .getName() + " attempted to spear a fish and failed!", performer, 5);
/*      */       } 
/*      */       
/* 1207 */       act.setTickCount(23);
/*      */     }
/*      */     else {
/*      */       
/* 1211 */       act.setTickCount(22);
/* 1212 */       performer.getCommunicator().sendNormalServerMessage("You managed to spear the " + fish.getName() + "!");
/* 1213 */       Server.getInstance().broadCastAction(performer
/* 1214 */           .getName() + " managed to spear a fish!", performer, 5);
/*      */ 
/*      */ 
/*      */       
/* 1218 */       performer.achievement(559);
/*      */     } 
/* 1220 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean processFishStrike(Creature performer, Action act, Skill fishing, Item rod) {
/* 1236 */     Creature fish = act.getCreature();
/* 1237 */     if (fish == null) {
/*      */       
/* 1239 */       act.setTickCount(5);
/* 1240 */       return false;
/*      */     } 
/* 1242 */     if (rod == null) {
/*      */       
/* 1244 */       act.setTickCount(7);
/* 1245 */       return false;
/*      */     } 
/* 1247 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 1248 */     String extra = "";
/* 1249 */     if (act.getTickCount() != 18) {
/*      */       
/* 1251 */       if (act.getTickCount() != 4 && act.getTickCount() != 2 && act
/* 1252 */         .getTickCount() != 19) {
/*      */ 
/*      */         
/* 1255 */         if (Servers.isThisATestServer() && (performer.getPower() > 1 || performer.hasFlag(51)))
/* 1256 */           extra = " (Test only) Cmd:" + fromCommand((byte)act.getTickCount()); 
/* 1257 */         performer.getCommunicator().sendNormalServerMessage("You scare off the " + faid.getNameWithSize() + " before it starts feeding." + extra);
/* 1258 */         processFishMovedOn(performer, act, 2.0F);
/* 1259 */         act.setTickCount(4);
/* 1260 */         performer.getCommunicator().sendFishSubCommand((byte)4, -1L);
/*      */       } 
/* 1262 */       return false;
/*      */     } 
/* 1264 */     Item[] fishingItems = rod.getFishingItems();
/* 1265 */     Item fishingReel = fishingItems[0];
/* 1266 */     Item fishingLine = fishingItems[1];
/* 1267 */     Item fishingFloat = fishingItems[2];
/* 1268 */     Item fishingHook = fishingItems[3];
/* 1269 */     Item fishingBait = fishingItems[4];
/* 1270 */     float result = getDifficulty(performer, act, performer.getPosX(), performer.getPosY(), fishing, rod, fishingReel, fishingLine, fishingFloat, fishingHook, fishingBait, 0.0F, 40.0F);
/*      */     
/* 1272 */     if (Servers.isThisATestServer() && (performer.getPower() > 1 || performer.hasFlag(51))) {
/* 1273 */       extra = " (Test only) Res:" + result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1285 */     act.setTickCount(3);
/*      */     
/* 1287 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static float getDifficulty(Creature performer, Action act, float posx, float posy, Skill fishing, Item source, Item reel, Item line, Item fishingFloat, Item hook, Item bait, float bonus, float times) {
/* 1310 */     Creature fish = act.getCreature();
/* 1311 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 1312 */     float difficulty = faid.getDifficulty();
/* 1313 */     if (difficulty == -10.0F) {
/*      */ 
/*      */       
/* 1316 */       FishEnums.FishData fd = FishEnums.FishData.fromInt(faid.getFishTypeId());
/* 1317 */       float knowledge = (float)fishing.getKnowledge();
/* 1318 */       difficulty = fd.getDifficulty(knowledge, posx, posy, performer.isOnSurface(), source, reel, line, fishingFloat, hook, bait);
/* 1319 */       faid.setDifficulty(difficulty);
/* 1320 */       testMessage(performer, "", fd.getName() + " Dif:" + difficulty);
/*      */     } 
/* 1322 */     double result = fishing.skillCheck(difficulty, source, bonus, false, times);
/* 1323 */     return (float)result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processSpearCancel(Creature performer, Action act) {
/* 1334 */     performer.getCommunicator().sendNormalServerMessage("You have cancelled your spearing!");
/* 1335 */     return sendSpearStop(performer, act);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processSpearMove(Creature performer, Action act, Item spear, Skill fishing) {
/* 1351 */     if (act.getCreature() == null) {
/*      */ 
/*      */       
/* 1354 */       if (makeFish(performer, act, spear, fishing, (byte)20))
/*      */       {
/*      */         
/* 1357 */         return true;
/*      */       }
/*      */       
/* 1360 */       act.setTickCount(21);
/* 1361 */       return false;
/*      */     } 
/*      */     
/* 1364 */     act.setTickCount(28);
/* 1365 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processSpearSwamAway(Creature performer, Action act, Item spear, int delay) {
/* 1379 */     performer.getCommunicator().sendNormalServerMessage("The " + act.getCreature().getName() + " swims off.");
/* 1380 */     destroyFishCreature(act);
/*      */     
/* 1382 */     int moreTime = delay + Server.rand.nextInt(250) - spear.getRarity() * 10;
/* 1383 */     int timeleft = act.getSecond() * 10 + moreTime;
/* 1384 */     act.setData(timeleft);
/* 1385 */     act.setTickCount(21);
/* 1386 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processSpearMissed(Creature performer, Action act, Item spear) {
/* 1398 */     Creature fish = act.getCreature();
/* 1399 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 1400 */     faid.setRaceAway(true);
/* 1401 */     int moreTime = (int)(faid.getTimeToTarget() / 2.0F);
/* 1402 */     int timeleft = act.getSecond() * 10 + moreTime;
/* 1403 */     act.setData(timeleft);
/* 1404 */     act.setTickCount(21);
/*      */     
/* 1406 */     FishEnums.FishData fd = faid.getFishData();
/* 1407 */     float fdam = fd.getDamageMod();
/* 1408 */     float damMod = fdam * Math.max(0.1F, (faid.getWeight() / 3000));
/* 1409 */     float additionalDamage = additionalDamage(spear, damMod * 10.0F, true);
/* 1410 */     if (additionalDamage > 0.0F)
/*      */     {
/* 1412 */       return spear.setDamage(spear.getDamage() + additionalDamage);
/*      */     }
/* 1414 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processSpearHit(Creature performer, Action act, Skill fishing, Item spear) {
/* 1427 */     Creature fish = act.getCreature();
/* 1428 */     if (fish == null)
/* 1429 */       return true; 
/* 1430 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 1431 */     byte fishTypeId = faid.getFishTypeId();
/* 1432 */     performer.getCommunicator().sendSpearHit(fishTypeId, fish.getWurmId());
/*      */     
/* 1434 */     makeDeadFish(performer, act, fishing, fishTypeId, spear, performer.getInventory());
/* 1435 */     destroyFishCreature(act);
/*      */     
/* 1437 */     FishEnums.FishData fd = faid.getFishData();
/* 1438 */     float fdam = fd.getDamageMod();
/* 1439 */     float damMod = fdam * Math.max(0.1F, (faid.getWeight() / 3000));
/* 1440 */     float additionalDamage = additionalDamage(spear, damMod * 8.0F, true);
/* 1441 */     if (additionalDamage > 0.0F)
/*      */     {
/* 1443 */       return spear.setDamage(spear.getDamage() + additionalDamage);
/*      */     }
/* 1445 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean sendSpearStop(Creature performer, Action act) {
/* 1456 */     destroyFishCreature(act);
/* 1457 */     performer.getCommunicator().sendFishSubCommand((byte)29, -1L);
/* 1458 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean makeFish(Creature performer, Action act, Item source, Skill fishing, byte startCmd) {
/*      */     Point4f mid, start, end;
/*      */     float angle;
/* 1473 */     FishRow fr = caughtFish(performer, act.getPosX(), act.getPosY(), source);
/* 1474 */     if (fr == null) {
/*      */ 
/*      */       
/* 1477 */       int i = 100 + Server.rand.nextInt(250);
/* 1478 */       int j = act.getSecond() * 10 + i;
/* 1479 */       act.setData(j);
/* 1480 */       testMessage(performer, "No Fish!", " Next attempt in approx:" + (i / 10) + "s");
/* 1481 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1490 */     float speedMod = 1.0F;
/*      */     
/* 1492 */     float rot = performer.getStatus().getRotation();
/* 1493 */     if (performer.getVehicle() == -10L) {
/*      */ 
/*      */       
/* 1496 */       int ang = Server.rand.nextInt(40);
/* 1497 */       if (Server.rand.nextBoolean()) {
/* 1498 */         angle = Creature.normalizeAngle(rot - 130.0F + ang);
/*      */       } else {
/* 1500 */         angle = Creature.normalizeAngle(rot + 130.0F - ang);
/*      */       } 
/*      */     } else {
/* 1503 */       angle = Server.rand.nextInt(360);
/* 1504 */     }  if (startCmd == 20) {
/*      */ 
/*      */       
/* 1507 */       float dist = 0.05F + Server.rand.nextFloat();
/* 1508 */       mid = calcSpot(act.getPosX(), act.getPosY(), performer.getStatus().getRotation(), dist);
/* 1509 */       start = calcSpot(mid.getPosX(), mid.getPosY(), angle, 10.0F);
/*      */       
/* 1511 */       end = calcSpot(start.getPosX(), start.getPosY(), start.getRot(), 20.0F);
/*      */       
/*      */       try {
/* 1514 */         if (Zones.calculateHeight(start.getPosX(), start.getPosY(), performer.isOnSurface()) > 0.0F)
/*      */         {
/*      */           
/* 1517 */           Point4f temp = start;
/* 1518 */           start = end;
/* 1519 */           end = temp;
/*      */         }
/*      */       
/* 1522 */       } catch (NoSuchZoneException e) {
/*      */ 
/*      */         
/* 1525 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/*      */       
/* 1528 */       speedMod = 0.5F;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1533 */       mid = new Point4f(act.getPosX(), act.getPosY(), 0.0F, Creature.normalizeAngle(rot + 180.0F));
/* 1534 */       start = calcSpot(mid.getPosX(), mid.getPosY(), angle, 10.0F);
/*      */       
/* 1536 */       end = new Point4f(mid.getPosX(), mid.getPosY(), 0.0F, start.getRot());
/*      */       
/*      */       try {
/* 1539 */         if (Zones.calculateHeight(start.getPosX(), start.getPosY(), performer.isOnSurface()) > 0.0F)
/*      */         {
/*      */           
/* 1542 */           Point4f temp = start;
/* 1543 */           start = end;
/* 1544 */           end = temp;
/*      */         }
/*      */       
/* 1547 */       } catch (NoSuchZoneException e) {
/*      */ 
/*      */         
/* 1550 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/*      */       
/* 1553 */       speedMod = 1.2F;
/*      */     } 
/* 1555 */     double ql = getQL(performer, source, fishing, fr.getFishTypeId(), act.getPosX(), act.getPosY(), true);
/* 1556 */     Creature fish = makeFishCreature(performer, start, fr.getName(), ql, fr.getFishTypeId(), speedMod, end);
/* 1557 */     if (fish == null) {
/*      */       
/* 1559 */       performer.getCommunicator().sendNormalServerMessage("You jump as you see a ghost of a fish, and stop fishing.");
/* 1560 */       return true;
/*      */     } 
/*      */     
/* 1563 */     act.setCreature(fish);
/* 1564 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 1565 */     int moreTime = (int)faid.getTimeToTarget();
/* 1566 */     int timeleft = act.getSecond() * 10 + moreTime;
/* 1567 */     act.setData(timeleft);
/*      */     
/* 1569 */     if (Servers.isThisATestServer() && (performer.getPower() > 1 || performer.hasFlag(51))) {
/*      */       
/* 1571 */       if (performer.getPower() >= 2) {
/* 1572 */         testMessage(performer, "", performer.getName() + " @px:" + (int)performer.getPosX() + ",py:" + 
/* 1573 */             (int)performer.getPosY() + ",pr:" + (int)performer.getStatus().getRotation());
/*      */       }
/* 1575 */       if (startCmd == 20)
/* 1576 */         addMarker(performer, fish.getName() + " mid", mid); 
/* 1577 */       addMarker(performer, fish.getName() + " start", start);
/* 1578 */       addMarker(performer, fish.getName() + " end", end);
/*      */     } 
/* 1580 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Creature makeFishCreature(Creature performer, Point4f start, String fishName, double ql, byte fishTypeId, float speedMod, Point4f end) {
/*      */     try {
/* 1600 */       Creature fish = Creature.doNew(119, start.getPosX(), start.getPosY(), start.getRot(), performer
/* 1601 */           .getLayer(), fishName, Server.rand.nextBoolean() ? 0 : 1, (byte)0);
/*      */       
/* 1603 */       fish.setVisible(false);
/* 1604 */       FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 1605 */       faid.setFishTypeId(fishTypeId);
/* 1606 */       faid.setQL(ql);
/* 1607 */       faid.setMovementSpeedModifier(speedMod);
/* 1608 */       faid.setTargetPos(end.getPosX(), end.getPosY());
/* 1609 */       fish.setVisible(true);
/* 1610 */       return fish;
/*      */     }
/* 1612 */     catch (Exception e) {
/*      */       
/* 1614 */       logger.log(Level.WARNING, e.getMessage(), e);
/* 1615 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addMarker(Creature performer, String string, Point4f loc) {
/* 1627 */     if (!Servers.isThisATestServer())
/*      */       return; 
/* 1629 */     String sloc = "";
/* 1630 */     if (performer.getPower() >= 2) {
/*      */       
/* 1632 */       int depth = FishEnums.getWaterDepth(loc.getPosX(), loc.getPosY(), performer.isOnSurface());
/* 1633 */       sloc = " @x:" + (int)loc.getPosX() + ", y:" + (int)loc.getPosY() + ", r:" + (int)loc.getRot() + ", d:" + depth;
/* 1634 */       testMessage(performer, string, sloc);
/*      */     } 
/*      */     
/* 1637 */     if (performer.getPower() >= 5 && performer.hasFlag(51)) {
/*      */       
/* 1639 */       VolaTile vtile = Zones.getOrCreateTile(loc.getTileX(), loc.getTileY(), performer.isOnSurface());
/*      */       
/*      */       try {
/* 1642 */         Item marker = ItemFactory.createItem(344, 1.0F, null);
/* 1643 */         marker.setSizes(10, 10, 100);
/* 1644 */         marker.setPosXY(loc.getPosX(), loc.getPosY());
/* 1645 */         marker.setRotation(loc.getRot());
/* 1646 */         marker.setDescription(string + sloc);
/* 1647 */         vtile.addItem(marker, false, false);
/*      */       }
/* 1649 */       catch (FailedException e) {
/*      */ 
/*      */         
/* 1652 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       }
/* 1654 */       catch (NoSuchTemplateException e) {
/*      */ 
/*      */         
/* 1657 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Point4f calcSpot(float posx, float posy, float rot, float dist) {
/* 1674 */     float r = rot * 3.1415927F / 180.0F;
/* 1675 */     float s = (float)Math.sin(r);
/* 1676 */     float c = (float)Math.cos(r);
/* 1677 */     float xo = s * dist;
/* 1678 */     float yo = c * -dist;
/* 1679 */     float newx = posx + xo;
/* 1680 */     float newy = posy + yo;
/*      */     
/* 1682 */     float angle = rot + 180.0F;
/* 1683 */     return new Point4f(newx, newy, 0.0F, Creature.normalizeAngle(angle));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int makeDeadFish(Creature performer, Action act, Skill fishing, byte fishTypeId, Item source, Item container) {
/*      */     double ql;
/*      */     FishEnums.FishData fd;
/*      */     int weight;
/* 1702 */     Creature fish = act.getCreature();
/* 1703 */     if (fish == null) {
/*      */       
/* 1705 */       if (source.getTemplateId() != 1343) {
/* 1706 */         return 0;
/*      */       }
/*      */       
/* 1709 */       fd = FishEnums.FishData.fromInt(fishTypeId);
/* 1710 */       ql = getQL(performer, source, fishing, fishTypeId, act.getPosX(), act.getPosY(), false);
/* 1711 */       ItemTemplate it = fd.getTemplate();
/* 1712 */       if (it == null) {
/*      */ 
/*      */         
/* 1715 */         testMessage(performer, "", fd.getName() + " no template!");
/* 1716 */         return 0;
/*      */       } 
/* 1718 */       weight = (int)(it.getWeightGrams() * ql / 100.0D);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1723 */       FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 1724 */       fd = faid.getFishData();
/* 1725 */       ql = faid.getQL();
/* 1726 */       weight = faid.getWeight();
/*      */     } 
/* 1728 */     if (weight == 0) {
/*      */       
/* 1730 */       performer.getCommunicator().sendNormalServerMessage("You fumbled when trying to move the fish to " + container
/* 1731 */           .getName() + ", and it got away.");
/* 1732 */       return 0;
/*      */     } 
/* 1734 */     testMessage(performer, "", fd.getTemplate().getName() + ":" + weight + "g");
/* 1735 */     if (weight >= fd.getMinWeight()) {
/*      */       
/*      */       try
/*      */       {
/* 1739 */         if (act.getRarity() != 0)
/* 1740 */           performer.playPersonalSound("sound.fx.drumroll"); 
/* 1741 */         Item deadFish = ItemFactory.createItem(fd.getTemplateId(), (float)ql, (byte)2, act
/* 1742 */             .getRarity(), performer.getName());
/* 1743 */         deadFish.setSizes(weight);
/* 1744 */         deadFish.setWeight(weight, false);
/* 1745 */         boolean inserted = container.insertItem(deadFish);
/* 1746 */         if (inserted) {
/*      */           
/* 1748 */           if (source.getTemplateId() == 1343) {
/* 1749 */             performer.getCommunicator().sendNormalServerMessage("You catch " + deadFish.getNameWithGenus() + " in the " + source.getName() + ".");
/*      */           }
/* 1751 */           if (fd.getTypeId() == FishEnums.FishData.CLAM.getTypeId())
/*      */           {
/* 1753 */             performer.getCommunicator().sendNormalServerMessage("You will need to pry open the clam with a knife.");
/*      */           }
/*      */           
/* 1756 */           performer.achievement(126);
/* 1757 */           if (weight > 3000)
/* 1758 */             performer.achievement(542); 
/* 1759 */           if (weight > 10000)
/* 1760 */             performer.achievement(585); 
/* 1761 */           if (weight > 175000) {
/* 1762 */             performer.achievement(297);
/*      */           }
/*      */         } else {
/*      */           
/* 1766 */           performer.getCommunicator().sendNormalServerMessage("You fumbled when trying to move the " + deadFish
/* 1767 */               .getName() + " to " + container.getName() + ", and it got away.");
/* 1768 */           Items.destroyItem(deadFish.getWurmId());
/*      */         } 
/*      */         
/* 1771 */         act.setRarity(performer.getRarity());
/*      */       }
/* 1773 */       catch (FailedException e)
/*      */       {
/*      */         
/* 1776 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       }
/* 1778 */       catch (NoSuchTemplateException e)
/*      */       {
/*      */         
/* 1781 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1786 */       if (source.getTemplateId() == 1343) {
/* 1787 */         performer.getCommunicator().sendNormalServerMessage("The " + fd.getTemplate().getName() + "  was so small, it swam through the net.");
/*      */       } else {
/*      */         
/* 1790 */         performer.getCommunicator().sendNormalServerMessage("The " + fd.getTemplate().getName() + "  was so small, you threw it back in.");
/* 1791 */         Server.getInstance().broadCastAction(performer.getName() + " throws the tiddler " + fd.getTemplate().getName() + " back in.", performer, 5);
/*      */       } 
/* 1793 */       return 0;
/*      */     } 
/* 1795 */     return weight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static float additionalDamage(Item item, float damMod, boolean flexible) {
/* 1808 */     if (item == null) {
/* 1809 */       return 0.0F;
/*      */     }
/* 1811 */     float typeMod = item.isWood() ? 2.0F : (item.isMetal() ? 1.0F : 0.5F);
/* 1812 */     float newDam = typeMod * item.getDamageModifier(false, flexible) / 10.0F * Math.max(10.0F, item.getQualityLevel());
/* 1813 */     float qlMod = (100.0F - item.getCurrentQualityLevel()) / 50.0F;
/* 1814 */     float extraDam = Math.min(qlMod, Math.max(0.1F, newDam) * damMod);
/* 1815 */     return extraDam;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static double getQL(Creature performer, Item source, Skill fishing, byte fishTypeId, float posx, float posy, boolean noSkill) {
/* 1832 */     Item[] fishingItems = source.getFishingItems();
/* 1833 */     Item fishingReel = fishingItems[0];
/* 1834 */     Item fishingLine = fishingItems[1];
/* 1835 */     Item fishingFloat = fishingItems[2];
/* 1836 */     Item fishingHook = fishingItems[3];
/* 1837 */     Item fishingBait = fishingItems[4];
/* 1838 */     FishEnums.FishData fd = FishEnums.FishData.fromInt(fishTypeId);
/*      */     
/* 1840 */     float knowledge = (float)fishing.getKnowledge();
/* 1841 */     float difficulty = fd.getDifficulty(knowledge, posx, posy, performer.isOnSurface(), source, fishingReel, fishingLine, fishingFloat, fishingHook, fishingBait);
/*      */ 
/*      */     
/* 1844 */     double power = fishing.skillCheck(difficulty, null, 0.0D, noSkill, 10.0F);
/* 1845 */     double ql = 10.0D + 0.9D * Math.max((Server.rand.nextFloat() * 10.0F), power);
/* 1846 */     return ql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean autoCast(Creature performer, Action act, Item rod) {
/*      */     int tile;
/* 1860 */     Item[] fishingItems = rod.getFishingItems();
/*      */     
/* 1862 */     Item line = fishingItems[1];
/*      */ 
/*      */ 
/*      */     
/* 1866 */     float[] radi = getMinMaxRadius(rod, line);
/* 1867 */     float minRadius = radi[0];
/* 1868 */     float maxRadius = radi[1];
/* 1869 */     float half = (maxRadius + minRadius) / 2.0F;
/*      */     
/* 1871 */     float rot = performer.getStatus().getRotation();
/* 1872 */     float r = rot * 3.1415927F / 180.0F;
/* 1873 */     float s = (float)Math.sin(r);
/* 1874 */     float c = (float)Math.cos(r);
/* 1875 */     float xo = s * half;
/* 1876 */     float yo = c * -half;
/* 1877 */     float castX = performer.getPosX() + xo;
/* 1878 */     float castY = performer.getPosY() + yo;
/*      */     
/* 1880 */     int tilex = (int)castX >> 2;
/* 1881 */     int tiley = (int)castY >> 2;
/*      */     
/* 1883 */     if (performer.isOnSurface()) {
/* 1884 */       tile = Server.surfaceMesh.getTile(tilex, tiley);
/*      */     } else {
/* 1886 */       tile = Server.caveMesh.getTile(tilex, tiley);
/* 1887 */     }  if (!Terraforming.isTileUnderWater(tile, tilex, tiley, performer.isOnSurface())) {
/*      */       
/* 1889 */       performer.getCommunicator().sendNormalServerMessage("The water is too shallow to fish.");
/* 1890 */       testMessage(performer, "", performer
/* 1891 */           .getTileX() + "=" + tilex + "," + performer.getTileY() + "=" + tiley);
/* 1892 */       return true;
/*      */     } 
/* 1894 */     testMessage(performer, "Auto cast as you were too lazy to cast!", "");
/* 1895 */     Server.getInstance().broadCastAction(performer.getName() + " casts and starts fishing.", performer, 5);
/* 1896 */     act.setTickCount(9);
/* 1897 */     return processFishCasted(performer, act, castX, castY, rod, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishCasted(Creature performer, Action act, float castX, float castY, Item rod, boolean manualMode) {
/* 1912 */     act.setPosX(castX);
/* 1913 */     act.setPosY(castY);
/*      */     
/* 1915 */     int defaultTimer = 1800;
/* 1916 */     act.setTimeLeft(1800);
/* 1917 */     performer.sendActionControl(Actions.actionEntrys[160].getVerbString(), true, 1800);
/* 1918 */     Item[] fishingItems = rod.getFishingItems();
/* 1919 */     Item floatItem = fishingItems[2];
/* 1920 */     performer.sendFishingLine(castX, castY, FishEnums.FloatType.fromItem(floatItem).getTypeId());
/*      */     
/* 1922 */     act.setTickCount(16);
/*      */     
/* 1924 */     int moreTime = getNextFishDelay(rod, 100, 600);
/* 1925 */     int timeleft = act.getSecond() * 10 + moreTime;
/* 1926 */     act.setData(timeleft);
/* 1927 */     if (Servers.isThisATestServer() && (performer.getPower() > 1 || performer.hasFlag(51))) {
/*      */ 
/*      */       
/* 1930 */       Point4f cast = new Point4f(castX, castY, 0.0F, 0.0F);
/* 1931 */       addMarker(performer, (manualMode ? "Manual" : "Auto") + " cast (" + (moreTime / 10) + "s)", cast);
/*      */     } 
/* 1933 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishMove(Creature performer, Action act, Skill fishing, Item rod) {
/* 1950 */     if (act.getCreature() == null) {
/*      */ 
/*      */       
/* 1953 */       if (makeFish(performer, act, rod, fishing, (byte)0))
/*      */       {
/*      */         
/* 1956 */         return true;
/*      */       }
/* 1958 */       if (act.getCreature() == null)
/*      */       {
/*      */         
/* 1961 */         return false;
/*      */       }
/* 1963 */       Creature fish = act.getCreature();
/* 1964 */       FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/*      */       
/* 1966 */       int moreTime = (int)faid.getTimeToTarget();
/* 1967 */       int timeleft = act.getSecond() * 10 + moreTime;
/* 1968 */       act.setData(timeleft);
/* 1969 */       act.setTickCount(1);
/* 1970 */       return false;
/*      */     } 
/*      */     
/* 1973 */     act.setTickCount(14);
/* 1974 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishMovedOn(Creature performer, Action act) {
/* 1985 */     performer.getCommunicator().sendFishSubCommand((byte)2, -1L);
/* 1986 */     processFishMovedOn(performer, act, 1.2F);
/* 1987 */     act.setTickCount(19);
/* 1988 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean processFishMovingOn(Creature performer, Action act) {
/* 1993 */     act.setTickCount(16);
/* 1994 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishMovedOn(Creature performer, Action act, float speed) {
/* 2006 */     Creature fish = act.getCreature();
/* 2007 */     testMessage(performer, fish.getName() + " Swims off.", "");
/* 2008 */     performer.getCommunicator().sendNormalServerMessage("The " + fish.getName() + " swims off into the distance.");
/* 2009 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 2010 */     Point4f end = calcSpot(fish.getPosX(), fish.getPosY(), performer.getStatus().getRotation(), 10.0F);
/*      */     
/* 2012 */     faid.setMovementSpeedModifier(faid.getMovementSpeedModifier() * speed);
/* 2013 */     faid.setTargetPos(end.getPosX(), end.getPosY());
/* 2014 */     int moreTime = (int)faid.getTimeToTarget();
/* 2015 */     int timeleft = act.getSecond() * 10 + moreTime;
/* 2016 */     act.setData(timeleft);
/* 2017 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishSwamAway(Creature performer, Action act, Item rod, int delay) {
/* 2030 */     destroyFishCreature(act);
/*      */     
/* 2032 */     int moreTime = getNextFishDelay(rod, delay, 200);
/* 2033 */     int timeleft = act.getSecond() * 10 + moreTime;
/* 2034 */     act.setData(timeleft);
/* 2035 */     act.setTickCount(16);
/* 2036 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getNextFishDelay(Item rod, int delay, int rnd) {
/* 2041 */     Item[] fishingItems = rod.getFishingItems();
/* 2042 */     Item bait = fishingItems[4];
/* 2043 */     int bonus = (bait == null) ? 0 : (bait.getRarity() * 10);
/*      */     
/* 2045 */     Item hook = fishingItems[3];
/* 2046 */     float fmod = (hook == null) ? 1.0F : hook.getMaterialFragrantModifier();
/* 2047 */     return (int)((delay + Server.rand.nextInt(rnd) - bonus) * fmod);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishCancel(Creature performer, Action act) {
/* 2058 */     performer.getCommunicator().sendNormalServerMessage("You have cancelled your fishing!");
/* 2059 */     return sendFishStop(performer, act);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishBite(Creature performer, Action act, Item rod, Skill fishing) {
/* 2071 */     Creature fish = act.getCreature();
/* 2072 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 2073 */     if (Servers.isThisATestServer() && (performer.getPower() > 1 || performer.hasFlag(51))) {
/*      */ 
/*      */ 
/*      */       
/* 2077 */       String fpos = (performer.getPower() >= 2) ? (" fx:" + (int)fish.getPosX() + ",fy:" + (int)fish.getPosY() + " cx:" + (int)act.getPosX() + ",cy:" + (int)act.getPosY()) : "";
/*      */       
/* 2079 */       testMessage(performer, fish.getName() + " takes a bite!", fpos);
/*      */     } 
/*      */     
/* 2082 */     Item[] fishingItems = rod.getFishingItems();
/*      */     
/* 2084 */     Item fishingLine = fishingItems[1];
/* 2085 */     Item fishingFloat = fishingItems[2];
/* 2086 */     Item fishingHook = fishingItems[3];
/* 2087 */     Item fishingBait = fishingItems[4];
/* 2088 */     FishEnums.FishData fd = faid.getFishData();
/* 2089 */     float damMod = fd.getDamageMod() * Math.max(0.1F, (faid.getWeight() / 3000));
/*      */     
/* 2091 */     float additionalDamage = additionalDamage(fishingFloat, damMod * 5.0F, false);
/* 2092 */     float newDamage = fishingFloat.getDamage() + additionalDamage;
/* 2093 */     if (newDamage > 100.0F);
/*      */ 
/*      */ 
/*      */     
/* 2097 */     if (additionalDamage > 0.0F) {
/* 2098 */       fishingFloat.setDamage(newDamage);
/*      */     }
/* 2100 */     additionalDamage = additionalDamage(fishingHook, damMod * 10.0F, false);
/* 2101 */     newDamage = fishingHook.getDamage() + additionalDamage;
/* 2102 */     if (newDamage > 100.0F)
/*      */     {
/*      */       
/* 2105 */       destroyContents(fishingHook);
/*      */     }
/* 2107 */     if (additionalDamage > 0.0F) {
/* 2108 */       fishingHook.setDamage(newDamage);
/*      */     }
/*      */     
/* 2111 */     fishingHook = fishingLine.getFishingHook();
/* 2112 */     if (fishingHook != null) {
/*      */       
/* 2114 */       fishingBait = fishingHook.getFishingBait();
/* 2115 */       if (fishingBait != null) {
/*      */ 
/*      */         
/* 2118 */         float dam = getBaitDamage(fd, fishingBait);
/* 2119 */         if (fishingBait.setDamage(fishingBait.getDamage() + dam));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2126 */     Item[] newFishingItems = rod.getFishingItems();
/* 2127 */     Item newFishingBait = newFishingItems[4];
/* 2128 */     int bonus = (newFishingBait == null) ? 0 : (newFishingBait.getRarity() * 10);
/* 2129 */     int skillBonus = (int)(fishing.getKnowledge(0.0D) / 3.0D);
/*      */     
/* 2131 */     int moreTime = 35 + skillBonus + Server.rand.nextInt(20) + bonus;
/* 2132 */     int timeleft = act.getSecond() * 10 + moreTime;
/* 2133 */     act.setData(timeleft);
/* 2134 */     act.setTickCount(18);
/* 2135 */     performer.getCommunicator().sendFishBite(faid.getFishTypeId(), fish.getWurmId(), -1L);
/* 2136 */     performer.getCommunicator().sendNormalServerMessage("You feel something nibble on the line.");
/* 2137 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static float getBaitDamage(FishEnums.FishData fd, Item bait) {
/* 2149 */     float crumbles = FishEnums.BaitType.fromItem(bait).getCrumbleFactor();
/*      */     
/* 2151 */     float dif = fd.getTemplateDifficulty();
/*      */     
/* 2153 */     float base = dif + Server.rand.nextFloat() * 20.0F;
/*      */     
/* 2155 */     float newDam = (base + 10.0F) / crumbles;
/*      */     
/* 2157 */     return Math.max(20.0F, newDam);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static float getRodDamageModifier(Item rod) {
/* 2167 */     Item reel = rod.getFishingReel();
/* 2168 */     if (reel == null)
/*      */     {
/* 2170 */       return 5.0F; } 
/* 2171 */     switch (reel.getTemplateId()) {
/*      */       
/*      */       case 1372:
/* 2174 */         return 4.5F;
/*      */       case 1373:
/* 2176 */         return 4.0F;
/*      */       case 1374:
/* 2178 */         return 3.5F;
/*      */       case 1375:
/* 2180 */         return 2.5F;
/*      */     } 
/* 2182 */     return 5.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static float getReelDamageModifier(Item reel) {
/* 2192 */     if (reel == null)
/* 2193 */       return 0.0F; 
/* 2194 */     switch (reel.getTemplateId()) {
/*      */       
/*      */       case 1372:
/* 2197 */         return 0.1F;
/*      */       case 1373:
/* 2199 */         return 0.07F;
/*      */       case 1374:
/* 2201 */         return 0.05F;
/*      */       case 1375:
/* 2203 */         return 0.02F;
/*      */     } 
/* 2205 */     return 0.1F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean autoReplace(Creature performer, int templateId, byte material, Item targetContainer) {
/* 2219 */     Item tacklebox = performer.getBestTackleBox();
/* 2220 */     if (tacklebox != null) {
/*      */       
/* 2222 */       Item compartment = getBoxCompartment(tacklebox, templateId);
/*      */       
/* 2224 */       if (compartment != null) {
/*      */         
/* 2226 */         Item[] contents = compartment.getItemsAsArray();
/* 2227 */         if (contents.length > 0)
/*      */         {
/*      */           
/* 2230 */           for (Item item : contents) {
/*      */             
/* 2232 */             if (item.getTemplateId() == templateId)
/*      */             {
/* 2234 */               if (material == 0 || item.getMaterial() == material) {
/*      */                 
/* 2236 */                 targetContainer.insertItem(item);
/*      */                 
/* 2238 */                 item.sendUpdate();
/* 2239 */                 return true;
/*      */               } 
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 2246 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean doAutoReplace(Creature performer, Action act) {
/* 2261 */     Item rod = act.getSubject();
/* 2262 */     if (rod == null)
/* 2263 */       return true; 
/* 2264 */     Skill fishing = performer.getSkills().getSkillOrLearn(10033);
/* 2265 */     boolean hasTacklebox = (performer.getBestTackleBox() != null);
/* 2266 */     float knowledge = (float)fishing.getKnowledge(0.0D);
/* 2267 */     boolean replaced = false;
/* 2268 */     Item reel = rod.getFishingReel();
/*      */     
/* 2270 */     if (rod.getTemplateId() == 1346 && reel == null && hasTacklebox) {
/*      */       
/* 2272 */       if (knowledge >= 90.0F) {
/*      */         
/* 2274 */         FishEnums.ReelType reelType = FishEnums.ReelType.fromInt(rod.getData1() >> 12 & 0xF);
/* 2275 */         byte reelMaterial = (byte)(rod.getData2() >> 8 & 0xFF);
/* 2276 */         replaced = autoReplace(performer, reelType.getTemplateId(), reelMaterial, rod);
/* 2277 */         if (replaced) {
/*      */           
/* 2279 */           reel = rod.getFishingReel();
/* 2280 */           performer.getCommunicator().sendNormalServerMessage("You managed to put another " + reel.getName() + " in the " + rod
/* 2281 */               .getName() + "!");
/*      */         }
/*      */         else {
/*      */           
/* 2285 */           performer.getCommunicator().sendNormalServerMessage("No replacement reel found!");
/*      */         } 
/*      */       } else {
/*      */         
/* 2289 */         performer.getCommunicator().sendNormalServerMessage("You cannot remember what the reel was!");
/* 2290 */       }  if (!replaced) {
/*      */         
/* 2292 */         performer.getCommunicator().sendNormalServerMessage("You are missing reel, line, float, fishing hook and bait!");
/* 2293 */         return true;
/*      */       } 
/*      */     } 
/* 2296 */     Item lineParent = (rod.getTemplateId() == 1346) ? reel : rod;
/* 2297 */     replaced = false;
/*      */     
/* 2299 */     Item line = lineParent.getFishingLine();
/* 2300 */     if (line == null && hasTacklebox) {
/*      */       
/* 2302 */       if (knowledge > 70.0F || reel != null) {
/*      */ 
/*      */         
/* 2305 */         int lineTemplateId = FishEnums.ReelType.fromItem(reel).getAssociatedLineTemplateId();
/*      */         
/* 2307 */         replaced = autoReplace(performer, lineTemplateId, (byte)0, lineParent);
/* 2308 */         if (replaced) {
/*      */           
/* 2310 */           line = lineParent.getFishingLine();
/* 2311 */           performer.getCommunicator().sendNormalServerMessage("You managed to put another " + line.getName() + " in the " + lineParent
/* 2312 */               .getName() + "!");
/*      */         }
/*      */         else {
/*      */           
/* 2316 */           performer.getCommunicator().sendNormalServerMessage("No replacement line found!");
/*      */         } 
/*      */       } else {
/*      */         
/* 2320 */         performer.getCommunicator().sendNormalServerMessage("You cannot remember what the line was!");
/* 2321 */       }  if (!replaced) {
/*      */         
/* 2323 */         performer.getCommunicator().sendNormalServerMessage("You are missing line, float, fishing hook and bait!");
/* 2324 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 2328 */     Item afloat = line.getFishingFloat();
/* 2329 */     if (afloat == null && hasTacklebox)
/*      */     {
/* 2331 */       if (knowledge > 50.0F) {
/*      */         
/* 2333 */         FishEnums.FloatType floatType = FishEnums.FloatType.fromInt(rod.getData1() >> 8 & 0xF);
/* 2334 */         replaced = autoReplace(performer, floatType.getTemplateId(), (byte)0, line);
/* 2335 */         if (replaced) {
/*      */           
/* 2337 */           afloat = line.getFishingFloat();
/* 2338 */           String floatName = afloat.getName();
/* 2339 */           performer.getCommunicator().sendNormalServerMessage("You managed to put another " + floatName + " on the " + line
/* 2340 */               .getName() + "!");
/*      */         }
/*      */         else {
/*      */           
/* 2344 */           performer.getCommunicator().sendNormalServerMessage("No replacement float found!");
/*      */         } 
/*      */       } else {
/*      */         
/* 2348 */         performer.getCommunicator().sendNormalServerMessage("You cannot remember what the float was!");
/*      */       } 
/*      */     }
/* 2351 */     Item hook = line.getFishingHook();
/* 2352 */     if (hook == null && hasTacklebox)
/*      */     {
/* 2354 */       if (knowledge > 30.0F) {
/*      */         
/* 2356 */         FishEnums.HookType hookType = FishEnums.HookType.fromInt(rod.getData1() >> 4 & 0xF);
/* 2357 */         byte hookMaterial = (byte)(rod.getData2() & 0xFF);
/* 2358 */         replaced = autoReplace(performer, hookType.getTemplateId(), hookMaterial, line);
/* 2359 */         if (replaced) {
/*      */           
/* 2361 */           hook = line.getFishingHook();
/* 2362 */           performer.getCommunicator().sendNormalServerMessage("You managed to put another " + hook.getName() + " on the " + line
/* 2363 */               .getName() + "!");
/*      */         }
/*      */         else {
/*      */           
/* 2367 */           performer.getCommunicator().sendNormalServerMessage("No replacement fishing hook found!");
/*      */         } 
/*      */       } else {
/*      */         
/* 2371 */         performer.getCommunicator().sendNormalServerMessage("You cannot remember what the fishing hook was!");
/*      */       }  } 
/* 2373 */     Item bait = null;
/* 2374 */     if (hook != null) {
/*      */       
/* 2376 */       bait = hook.getFishingBait();
/* 2377 */       if (bait == null && hasTacklebox)
/*      */       {
/* 2379 */         if (knowledge > 10.0F) {
/*      */           
/* 2381 */           FishEnums.BaitType baitType = FishEnums.BaitType.fromInt(rod.getData1() & 0xF);
/* 2382 */           replaced = autoReplace(performer, baitType.getTemplateId(), (byte)0, hook);
/* 2383 */           if (replaced) {
/*      */             
/* 2385 */             bait = hook.getFishingBait();
/* 2386 */             performer.getCommunicator().sendNormalServerMessage("You managed to put another " + bait.getName() + " on the " + hook
/* 2387 */                 .getName() + "!");
/*      */           }
/*      */           else {
/*      */             
/* 2391 */             performer.getCommunicator().sendNormalServerMessage("No replacement bait found!");
/*      */           } 
/*      */         } else {
/*      */           
/* 2395 */           performer.getCommunicator().sendNormalServerMessage("You cannot remember what the bait was!");
/*      */         }  } 
/*      */     } 
/* 2398 */     if (afloat == null && hook == null) {
/*      */       
/* 2400 */       performer.getCommunicator().sendNormalServerMessage("You are missing a float, fishing hook and bait!");
/* 2401 */       return true;
/*      */     } 
/* 2403 */     if (afloat == null && bait == null) {
/*      */       
/* 2405 */       performer.getCommunicator().sendNormalServerMessage("You are missing a float and bait!");
/* 2406 */       return true;
/*      */     } 
/* 2408 */     if (afloat == null) {
/*      */       
/* 2410 */       performer.getCommunicator().sendNormalServerMessage("You are missing a float!");
/* 2411 */       return true;
/*      */     } 
/* 2413 */     if (hook == null) {
/*      */       
/* 2415 */       performer.getCommunicator().sendNormalServerMessage("You are missing a fishing hook and bait!");
/* 2416 */       return true;
/*      */     } 
/* 2418 */     if (bait == null) {
/*      */       
/* 2420 */       performer.getCommunicator().sendNormalServerMessage("You are missing a bait!");
/* 2421 */       return true;
/*      */     } 
/* 2423 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Item getBoxCompartment(Item tacklebox, int templateId) {
/* 2436 */     for (Item compartment : tacklebox.getItems()) {
/*      */       
/* 2438 */       for (ContainerRestriction cRest : compartment.getTemplate().getContainerRestrictions()) {
/*      */         
/* 2440 */         if (cRest.contains(templateId))
/*      */         {
/* 2442 */           return compartment;
/*      */         }
/*      */       } 
/*      */     } 
/* 2446 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishPause(Creature performer, Action act) {
/* 2459 */     act.setTickCount(2);
/* 2460 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishHooked(Creature performer, Action act, Skill fishing, Item rod) {
/* 2474 */     if (act.getCreature() == null) {
/*      */       
/* 2476 */       act.setTickCount(5);
/*      */     }
/*      */     else {
/*      */       
/* 2480 */       Creature fish = act.getCreature();
/* 2481 */       FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/*      */ 
/*      */       
/* 2484 */       performer.getCommunicator().sendFishSubCommand((byte)3, -1L);
/* 2485 */       performer.sendFishHooked(faid.getFishTypeId(), fish.getWurmId());
/* 2486 */       if (processFishPull(performer, act, fishing, rod, true)) {
/*      */         
/* 2488 */         sendFishStop(performer, act);
/* 2489 */         return true;
/*      */       } 
/*      */     } 
/* 2492 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishPull(Creature performer, Action act, Skill fishing, Item rod, boolean initial) {
/* 2512 */     Item[] fishingItems = rod.getFishingItems();
/* 2513 */     Item fishingReel = fishingItems[0];
/* 2514 */     Item fishingLine = fishingItems[1];
/* 2515 */     Item fishingFloat = fishingItems[2];
/* 2516 */     Item fishingHook = fishingItems[3];
/* 2517 */     Item fishingBait = fishingItems[4];
/*      */     
/* 2519 */     Creature fish = act.getCreature();
/* 2520 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 2521 */     boolean isClam = (faid.getFishTypeId() == FishEnums.FishData.CLAM.getTypeId());
/* 2522 */     if (initial) {
/*      */       
/* 2524 */       performer.getCommunicator().sendNormalServerMessage("You hooked " + faid.getNameWithGenusAndSize() + "!");
/* 2525 */       Server.getInstance().broadCastAction(performer.getName() + " hooks " + faid.getNameWithGenusAndSize() + ".", performer, 5);
/*      */     } 
/* 2527 */     float fstr = faid.getBodyStrength();
/* 2528 */     float pstr = (float)performer.getSkills().getSkillOrLearn(102).getKnowledge(0.0D);
/* 2529 */     float strBonus = Math.max(1.0F, pstr - fstr);
/* 2530 */     float stamBonus = faid.getBodyStamina() - 20.0F;
/* 2531 */     float bonus = (strBonus + stamBonus) / 2.0F;
/*      */     
/* 2533 */     float result = getDifficulty(performer, act, act.getPosX(), act.getPosY(), fishing, rod, fishingReel, fishingLine, fishingFloat, fishingHook, fishingBait, bonus, 10.0F);
/*      */ 
/*      */ 
/*      */     
/* 2537 */     float adjusted = result;
/* 2538 */     if (fishingReel != null)
/* 2539 */       adjusted += (fishingReel.getRarity() * fishingReel.getRarity()); 
/* 2540 */     if (adjusted < 0.0F && !isClam) {
/*      */ 
/*      */       
/* 2543 */       if (result <= -(90 + rod.getRarity() * 3)) {
/*      */ 
/*      */         
/* 2546 */         act.setTickCount(7);
/*      */       }
/* 2548 */       else if (result <= -(70 + fishingLine.getRarity() * 3)) {
/*      */ 
/*      */         
/* 2551 */         act.setTickCount(6);
/*      */       }
/* 2553 */       else if (result <= -(50 + fishingHook.getRarity() * 3)) {
/*      */ 
/*      */         
/* 2556 */         act.setTickCount(13);
/*      */       }
/*      */       else {
/*      */         
/* 2560 */         if (!initial)
/*      */         {
/* 2562 */           if (result <= -30.0F) {
/* 2563 */             performer.getCommunicator().sendNormalServerMessage("The " + faid.getNameWithSize() + " pulls hard on the line!");
/* 2564 */           } else if (result <= -15.0F) {
/* 2565 */             performer.getCommunicator().sendNormalServerMessage("The " + faid.getNameWithSize() + " pulls somewhat on the line!");
/*      */           } else {
/* 2567 */             performer.getCommunicator().sendNormalServerMessage("The " + faid.getNameWithSize() + " pulls a bit on the line!");
/*      */           }  } 
/* 2569 */         testMessage(performer, faid.getNameWithSize() + " moving away", " Result:" + result);
/* 2570 */         faid.decBodyStamina(strBonus / 2.0F + 2.0F);
/*      */         
/* 2572 */         double lNewrot = Math.atan2((performer.getPosY() - fish.getPosY()), (performer.getPosX() - fish.getPosX()));
/* 2573 */         float rot = (float)(lNewrot * 57.29577951308232D) - 90.0F;
/* 2574 */         float angle = rot + result * 2.0F + 90.0F;
/*      */         
/* 2576 */         float dist = Math.min(2.0F, strBonus / 10.0F);
/* 2577 */         Point4f end = calcSpot(fish.getPosX(), fish.getPosY(), Creature.normalizeAngle(angle), dist);
/*      */ 
/*      */         
/* 2580 */         float speedMod = dist / 2.0F;
/* 2581 */         faid.setMovementSpeedModifier(speedMod);
/* 2582 */         faid.setTargetPos(end.getPosX(), end.getPosY());
/*      */         
/* 2584 */         int moreTime = (int)faid.getTimeToTarget();
/* 2585 */         int timeleft = act.getSecond() * 10 + moreTime;
/* 2586 */         act.setData(timeleft);
/*      */         
/* 2588 */         act.setTickCount(17);
/*      */       } 
/*      */     } else {
/*      */       Point4f end;
/*      */       float speedMod;
/* 2593 */       if (!initial && !isClam)
/*      */       {
/* 2595 */         if (result > 80.0F) {
/* 2596 */           performer.getCommunicator().sendNormalServerMessage("You manage to easily reel in the " + faid.getNameWithSize() + "!");
/* 2597 */         } else if (result > 50.0F) {
/* 2598 */           performer.getCommunicator().sendNormalServerMessage("The " + faid.getNameWithSize() + " stands no chance!");
/* 2599 */         } else if (result > 25.0F) {
/* 2600 */           performer.getCommunicator().sendNormalServerMessage("The " + faid.getNameWithSize() + " takes a rest, so you reel it in a bit!");
/*      */         } else {
/* 2602 */           performer.getCommunicator().sendNormalServerMessage("The " + faid.getNameWithSize() + " starts to get tired and you manage to reel it in a bit!");
/*      */         } 
/*      */       }
/*      */       
/* 2606 */       double lNewrot = Math.atan2((performer.getPosY() - fish.getPosY()), (performer.getPosX() - fish.getPosX()));
/* 2607 */       float rot = (float)(lNewrot * 57.29577951308232D);
/* 2608 */       if (isClam) {
/*      */         
/* 2610 */         performer.getCommunicator().sendNormalServerMessage("You quickly reel in the " + faid.getNameWithSize() + "!");
/* 2611 */         speedMod = 2.0F;
/* 2612 */         end = new Point4f(performer.getPosX(), performer.getPosY(), 0.0F, Creature.normalizeAngle(rot + 180.0F));
/*      */       }
/*      */       else {
/*      */         
/* 2616 */         testMessage(performer, faid.getNameWithSize() + " moving closer", " Result:" + result);
/* 2617 */         faid.decBodyStamina(1.0F);
/*      */ 
/*      */ 
/*      */         
/* 2621 */         float angle = rot + result + 40.0F;
/*      */         
/* 2623 */         float dist = Math.min(2.5F, strBonus / 15.0F + Math.max(0.0F, result - 50.0F) / 15.0F);
/* 2624 */         speedMod = 1.0F;
/* 2625 */         end = calcSpot(fish.getPosX(), fish.getPosY(), Creature.normalizeAngle(angle), dist);
/*      */       } 
/* 2627 */       faid.setMovementSpeedModifier(speedMod);
/* 2628 */       faid.setTargetPos(end.getPosX(), end.getPosY());
/*      */       
/* 2630 */       int moreTime = (int)faid.getTimeToTarget();
/* 2631 */       int timeleft = act.getSecond() * 10 + moreTime;
/* 2632 */       act.setData(timeleft);
/*      */       
/* 2634 */       act.setTickCount(17);
/*      */     } 
/*      */     
/* 2637 */     FishEnums.FishData fd = faid.getFishData();
/* 2638 */     float fdam = fd.getDamageMod();
/* 2639 */     float damMod = (float)(fdam * Math.max(0.10000000149011612D, (faid.getWeight() > 6000) ? Math.pow((faid.getWeight() / 1000), 0.6D) : (faid.getWeight() / 3000)));
/*      */     
/* 2641 */     float additionalDamage = additionalDamage(rod, damMod * getRodDamageModifier(rod), true);
/* 2642 */     float newDamage = rod.getDamage() + additionalDamage;
/* 2643 */     if (newDamage > 100.0F) {
/*      */       
/* 2645 */       destroyContents(rod);
/* 2646 */       return rod.setDamage(newDamage);
/*      */     } 
/* 2648 */     if (additionalDamage > 0.0F) {
/* 2649 */       rod.setDamage(newDamage);
/*      */     }
/* 2651 */     if (rod.getTemplateId() == 1344) {
/*      */       
/* 2653 */       fishingLine = rod.getFishingLine();
/*      */     }
/*      */     else {
/*      */       
/* 2657 */       fishingReel = rod.getFishingReel();
/* 2658 */       additionalDamage = additionalDamage(fishingReel, damMod * getReelDamageModifier(fishingReel), true);
/* 2659 */       newDamage = fishingReel.getDamage() + additionalDamage;
/* 2660 */       if (newDamage > 100.0F) {
/*      */         
/* 2662 */         destroyContents(fishingReel);
/* 2663 */         return fishingReel.setDamage(newDamage);
/*      */       } 
/* 2665 */       if (additionalDamage > 0.0F)
/* 2666 */         fishingReel.setDamage(newDamage); 
/* 2667 */       fishingLine = fishingReel.getFishingLine();
/*      */     } 
/*      */     
/* 2670 */     additionalDamage = additionalDamage(fishingLine, damMod * 0.3F, true);
/* 2671 */     newDamage = fishingLine.getDamage() + additionalDamage;
/* 2672 */     if (newDamage > 100.0F) {
/*      */       
/* 2674 */       destroyContents(fishingLine);
/* 2675 */       return fishingLine.setDamage(newDamage);
/*      */     } 
/* 2677 */     if (additionalDamage > 0.0F)
/* 2678 */       fishingLine.setDamage(newDamage); 
/* 2679 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishCaught(Creature performer, Action act, Skill fishing, Item rod) {
/* 2692 */     Creature fish = act.getCreature();
/* 2693 */     performer.getCommunicator().sendFishSubCommand((byte)12, -1L);
/* 2694 */     performer.sendFishingStopped();
/* 2695 */     FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 2696 */     performer.getCommunicator().sendNormalServerMessage("You catch " + faid.getNameWithGenusAndSize());
/* 2697 */     Server.getInstance().broadCastAction(performer.getName() + " lands " + faid.getNameWithGenusAndSize() + ".", performer, 5);
/* 2698 */     makeDeadFish(performer, act, fishing, faid.getFishTypeId(), rod, performer.getInventory());
/* 2699 */     destroyFishCreature(act);
/* 2700 */     return doAutoReplace(performer, act);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean processFishLineSnapped(Creature performer, Action act, Item rod) {
/* 2712 */     performer.getCommunicator().sendNormalServerMessage("Your line snapped!!");
/* 2713 */     Item[] fishingItems = rod.getFishingItems();
/* 2714 */     Item fishingReel = fishingItems[0];
/* 2715 */     Item fishingLine = fishingItems[1];
/*      */ 
/*      */ 
/*      */     
/* 2719 */     destroyContents(fishingLine);
/* 2720 */     byte currentPhase = (byte)act.getTickCount();
/* 2721 */     boolean brokeRod = false;
/* 2722 */     if (currentPhase == 7) {
/*      */ 
/*      */       
/* 2725 */       Creature fish = act.getCreature();
/* 2726 */       FishAI.FishAIData faid = (FishAI.FishAIData)fish.getCreatureAIData();
/* 2727 */       FishEnums.FishData fd = faid.getFishData();
/* 2728 */       int weight = faid.getWeight();
/* 2729 */       float fdam = fd.getDamageMod() * 5.0F;
/* 2730 */       float damMod = fdam * Math.max(0.1F, (weight / 3000));
/* 2731 */       float additionalDamage = additionalDamage(rod, damMod * getRodDamageModifier(rod) * 2.0F, true);
/* 2732 */       float newDamage = rod.getDamage() + additionalDamage;
/* 2733 */       if (newDamage > 100.0F)
/*      */       {
/* 2735 */         destroyContents(rod);
/*      */       }
/* 2737 */       if (additionalDamage > 0.0F) {
/* 2738 */         brokeRod = rod.setDamage(newDamage);
/*      */       }
/* 2740 */       if (!brokeRod && fishingReel != null) {
/*      */         
/* 2742 */         additionalDamage = additionalDamage(fishingReel, damMod * getReelDamageModifier(fishingReel) * 2.0F, true);
/* 2743 */         newDamage = fishingReel.getDamage() + additionalDamage;
/* 2744 */         if (newDamage > 100.0F)
/*      */         {
/* 2746 */           destroyContents(fishingReel);
/*      */         }
/* 2748 */         if (additionalDamage > 0.0F) {
/* 2749 */           fishingReel.setDamage(newDamage);
/*      */         }
/*      */       } 
/*      */     } 
/* 2753 */     performer.getCommunicator().sendFishSubCommand((byte)6, -1L);
/* 2754 */     processFishMovedOn(performer, act, 2.2F);
/* 2755 */     act.setTickCount(15);
/* 2756 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void destroyContents(Item container) {
/* 2765 */     for (Item item : container.getItemsAsArray()) {
/*      */       
/* 2767 */       if (!item.isEmpty(false))
/* 2768 */         destroyContents(item); 
/* 2769 */       item.setDamage(100.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean sendFishStop(Creature performer, Action act) {
/* 2781 */     destroyFishCreature(act);
/* 2782 */     performer.getCommunicator().sendFishSubCommand((byte)15, -1L);
/* 2783 */     performer.sendFishingStopped();
/*      */     
/* 2785 */     return doAutoReplace(performer, act);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fromClient(Creature performer, byte subCommand, float posX, float posY) {
/*      */     try {
/*      */       Item rod;
/*      */       Skill fishing;
/*      */       Item item1, spear;
/* 2800 */       Action act = performer.getCurrentAction();
/* 2801 */       if (act.getNumber() != 160) {
/*      */         
/* 2803 */         testMessage(performer, "not fishing? ", "Action:" + act.getNumber());
/* 2804 */         logger.log(Level.WARNING, "not fishing! " + act.getNumber());
/*      */         
/*      */         return;
/*      */       } 
/* 2808 */       byte phase = (byte)act.getTickCount();
/* 2809 */       switch (subCommand) {
/*      */ 
/*      */ 
/*      */         
/*      */         case 9:
/* 2814 */           if (phase != 0) {
/*      */             
/* 2816 */             testMessage(performer, "Incorrect fishing subcommand", " (" + 
/* 2817 */                 fromCommand(subCommand) + ") for phase (" + fromCommand(phase) + ")");
/* 2818 */             logger.log(Level.WARNING, "Incorrect fishing subcommand (" + fromCommand(subCommand) + ") for phase (" + 
/* 2819 */                 fromCommand(phase) + ")");
/*      */             
/*      */             return;
/*      */           } 
/* 2823 */           rod = act.getSubject();
/* 2824 */           if (rod == null) {
/*      */             
/* 2826 */             testMessage(performer, "", "Subject missing in action");
/* 2827 */             logger.log(Level.WARNING, "Subject missing in action");
/*      */           }
/*      */           else {
/*      */             
/* 2831 */             performer.getCommunicator().sendNormalServerMessage("You cast the line and start fishing.");
/* 2832 */             Server.getInstance().broadCastAction(performer.getName() + " casts and starts fishing.", performer, 5);
/* 2833 */             processFishCasted(performer, act, posX, posY, rod, true);
/*      */           } 
/*      */           return;
/*      */ 
/*      */         
/*      */         case 10:
/* 2839 */           act.setTickCount(subCommand);
/*      */           return;
/*      */ 
/*      */         
/*      */         case 11:
/* 2844 */           fishing = performer.getSkills().getSkillOrLearn(10033);
/* 2845 */           item1 = act.getSubject();
/* 2846 */           if (item1 == null) {
/*      */             
/* 2848 */             testMessage(performer, "", "Subject missing in action");
/* 2849 */             logger.log(Level.WARNING, "Subject missing in action");
/*      */           
/*      */           }
/* 2852 */           else if (canStrike(phase)) {
/*      */             
/* 2854 */             processFishStrike(performer, act, fishing, item1);
/*      */           } 
/*      */           return;
/*      */ 
/*      */         
/*      */         case 27:
/* 2860 */           act.setTickCount(subCommand);
/*      */           return;
/*      */ 
/*      */         
/*      */         case 26:
/* 2865 */           if (phase != 21) {
/*      */             
/* 2867 */             testMessage(performer, "Incorrect fishing subcommand", " (" + 
/* 2868 */                 fromCommand(subCommand) + ") for phase (" + fromCommand(phase) + ")");
/* 2869 */             logger.log(Level.WARNING, "Incorrect fishing subcommand (" + fromCommand(subCommand) + ") for phase (" + 
/* 2870 */                 fromCommand(phase) + ")");
/*      */             return;
/*      */           } 
/* 2873 */           act.setTickCount(subCommand);
/* 2874 */           fishing = performer.getSkills().getSkillOrLearn(10033);
/* 2875 */           spear = act.getSubject();
/* 2876 */           if (spear == null) {
/*      */             
/* 2878 */             testMessage(performer, "", "Subject missing in action");
/* 2879 */             logger.log(Level.WARNING, "Subject missing in action");
/*      */           } else {
/*      */             
/* 2882 */             processSpearStrike(performer, act, fishing, spear, posX, posY);
/*      */           } 
/*      */           return;
/*      */       } 
/*      */       
/* 2887 */       testMessage(performer, "Bad fishing subcommand!", " (" + fromCommand(subCommand) + ")");
/* 2888 */       logger.log(Level.WARNING, "Bad fishing subcommand! " + fromCommand(subCommand) + " (" + subCommand + ")");
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/* 2893 */     } catch (NoSuchActionException e) {
/*      */       
/* 2895 */       testMessage(performer, "", "No current action, should be FISH.");
/* 2896 */       logger.log(Level.WARNING, "No current action, should be FISH:" + e.getMessage(), (Throwable)e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean canStrike(byte phase) {
/* 2907 */     switch (phase) {
/*      */       
/*      */       case 3:
/*      */       case 17:
/* 2911 */         return false;
/*      */     } 
/* 2913 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void testMessage(Creature performer, String message, String powerMessage) {
/* 2924 */     if (Servers.isThisATestServer() && (performer.getPower() > 1 || performer.hasFlag(51)))
/*      */     {
/* 2926 */       if (performer.getPower() >= 2) {
/* 2927 */         performer.getCommunicator().sendNormalServerMessage("(test only) " + message + powerMessage);
/* 2928 */       } else if (message.length() > 0) {
/* 2929 */         performer.getCommunicator().sendNormalServerMessage("(test only) " + message);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String fromCommand(byte command) {
/* 2941 */     switch (command) {
/*      */ 
/*      */       
/*      */       case 0:
/* 2945 */         return "FISH_START";
/*      */       case 1:
/* 2947 */         return "FISH_BITE";
/*      */       case 2:
/* 2949 */         return "FISH_MOVED_ON";
/*      */       case 19:
/* 2951 */         return "FISH_MOVING_ON";
/*      */       case 3:
/* 2953 */         return "FISH_HOOKED";
/*      */       case 4:
/* 2955 */         return "FISH_MISSED";
/*      */       case 5:
/* 2957 */         return "FISH_NO_FISH";
/*      */       case 6:
/* 2959 */         return "FISH_LINE_SNAPPED";
/*      */       case 7:
/* 2961 */         return "FISH_ROD_BROKE";
/*      */       case 8:
/* 2963 */         return "FISH_TIME_OUT";
/*      */       case 9:
/* 2965 */         return "FISH_CASTED";
/*      */       case 10:
/* 2967 */         return "FISH_CANCEL";
/*      */       case 11:
/* 2969 */         return "FISH_STRIKE";
/*      */       case 12:
/* 2971 */         return "FISH_CAUGHT";
/*      */       case 13:
/* 2973 */         return "FISH_GOT_AWAY";
/*      */       case 14:
/* 2975 */         return "FISH_SWAM_AWAY";
/*      */       case 15:
/* 2977 */         return "FISH_STOP";
/*      */       case 16:
/* 2979 */         return "FISH_MOVE";
/*      */       case 17:
/* 2981 */         return "FISH_PULL";
/*      */       case 18:
/* 2983 */         return "FISH_PAUSE";
/*      */       
/*      */       case 20:
/* 2986 */         return "SPEAR_START";
/*      */       case 21:
/* 2988 */         return "SPEAR_MOVE";
/*      */       case 22:
/* 2990 */         return "SPEAR_HIT";
/*      */       case 23:
/* 2992 */         return "SPEAR_MISSED";
/*      */       case 24:
/* 2994 */         return "SPEAR_NO_FISH";
/*      */       case 25:
/* 2996 */         return "SPEAR_TIME_OUT";
/*      */       case 26:
/* 2998 */         return "SPEAR_STRIKE";
/*      */       case 27:
/* 3000 */         return "SPEAR_CANCEL";
/*      */       case 28:
/* 3002 */         return "SPEAR_SWAM_AWAY";
/*      */       case 29:
/* 3004 */         return "SPEAR_STOP";
/*      */       
/*      */       case 40:
/* 3007 */         return "NET_START";
/*      */       
/*      */       case 45:
/* 3010 */         return "SHOW_FISH_SPOTS";
/*      */     } 
/* 3012 */     return "Unknown (" + command + ")";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte getRodType(Item rod, @Nullable Item reel, @Nullable Item line) {
/* 3024 */     if (rod.getTemplateId() == 1344) {
/* 3025 */       return FishingEnums.RodType.FISHING_POLE.getTypeId();
/*      */     }
/* 3027 */     if (rod.getTemplateId() != 1346 || reel == null)
/* 3028 */       return -1; 
/* 3029 */     if (line == null) {
/*      */       
/* 3031 */       switch (reel.getTemplateId()) {
/*      */         
/*      */         case 1372:
/* 3034 */           return FishingEnums.RodType.FISHING_ROD_BASIC.getTypeId();
/*      */         case 1373:
/* 3036 */           return FishingEnums.RodType.FISHING_ROD_FINE.getTypeId();
/*      */         case 1374:
/* 3038 */           return FishingEnums.RodType.FISHING_ROD_DEEP_WATER.getTypeId();
/*      */         case 1375:
/* 3040 */           return FishingEnums.RodType.FISHING_ROD_DEEP_SEA.getTypeId();
/*      */       } 
/* 3042 */       return -1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3047 */     switch (reel.getTemplateId()) {
/*      */       
/*      */       case 1372:
/* 3050 */         return FishingEnums.RodType.FISHING_ROD_BASIC_WITH_LINE.getTypeId();
/*      */       case 1373:
/* 3052 */         return FishingEnums.RodType.FISHING_ROD_FINE_WITH_LINE.getTypeId();
/*      */       case 1374:
/* 3054 */         return FishingEnums.RodType.FISHING_ROD_DEEP_WATER_WITH_LINE.getTypeId();
/*      */       case 1375:
/* 3056 */         return FishingEnums.RodType.FISHING_ROD_DEEP_SEA_WITH_LINE.getTypeId();
/*      */     } 
/* 3058 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static float[] getMinMaxRadius(Item rod, Item line) {
/* 3071 */     float min = (rod.getTemplateId() == 1344) ? 2.0F : 4.0F;
/*      */     
/* 3073 */     float linelength = getSingleLineLength(line);
/* 3074 */     float max = Math.min((linelength - min) / 2.0F, 8.0F) + min;
/* 3075 */     return new float[] { min, max };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getLineLength(Item line) {
/* 3085 */     int lineTemplateWeight = line.getTemplate().getWeightGrams();
/* 3086 */     int lineWeight = line.getWeightGrams();
/* 3087 */     float comb = (lineWeight / lineTemplateWeight);
/* 3088 */     int slen = getSingleLineLength(line);
/* 3089 */     int tlen = (int)(comb * slen);
/* 3090 */     return tlen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getSingleLineLength(Item line) {
/* 3100 */     switch (line.getTemplateId()) {
/*      */       
/*      */       case 1347:
/* 3103 */         return 10;
/*      */       case 1348:
/* 3105 */         return 12;
/*      */       case 1349:
/* 3107 */         return 14;
/*      */       case 1350:
/* 3109 */         return 16;
/*      */       case 1351:
/* 3111 */         return 18;
/*      */     } 
/* 3113 */     return 10;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Color getBgColour(int season) {
/* 3124 */     int[] bgRed = { 181, 34, 183, 192 };
/* 3125 */     int[] bgGreen = { 230, 177, 141, 192 };
/* 3126 */     int[] bgBlue = { 29, 0, 76, 192 };
/* 3127 */     return new Color(bgRed[season], bgGreen[season], bgBlue[season]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Point getSeasonOffset(int season) {
/* 3138 */     int[] offsetXs = { 0, 128, 0, 128 };
/* 3139 */     int[] offsetYs = { 0, 0, 128, 128 };
/* 3140 */     return new Point(offsetXs[season], offsetYs[season]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Color getFishColour(int templateId) {
/* 3151 */     int red = 0;
/* 3152 */     int green = 0;
/* 3153 */     int blue = 0;
/* 3154 */     switch (templateId) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 1336:
/* 3159 */         red = 255;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 569:
/* 3170 */         red = 255;
/* 3171 */         green = 255;
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 570:
/* 3177 */         red = 255;
/* 3178 */         green = 127;
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 574:
/* 3184 */         green = 255;
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 573:
/* 3190 */         green = 255;
/* 3191 */         blue = 255;
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 571:
/* 3197 */         red = 127;
/* 3198 */         blue = 255;
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 575:
/* 3204 */         blue = 255;
/*      */         break;
/*      */     } 
/*      */     
/* 3208 */     return new Color(red, green, blue);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class FishRow
/*      */   {
/*      */     private final byte fishTypeId;
/*      */ 
/*      */     
/*      */     private final String name;
/*      */     
/* 3220 */     private float chance = 0.0F;
/*      */ 
/*      */     
/*      */     public FishRow(int fishTypeId, String name) {
/* 3224 */       this.fishTypeId = (byte)fishTypeId;
/* 3225 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getFishTypeId() {
/* 3233 */       return this.fishTypeId;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 3241 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float getChance() {
/* 3249 */       return this.chance;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setChance(float chance) {
/* 3258 */       this.chance = chance;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 3269 */       StringBuilder buf = new StringBuilder();
/* 3270 */       buf.append("FishData [");
/* 3271 */       buf.append("Name: ").append(this.name);
/* 3272 */       buf.append(", Id: ").append(this.fishTypeId);
/* 3273 */       buf.append(", Chance: ").append(this.chance);
/* 3274 */       buf.append("]");
/* 3275 */       return buf.toString();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MethodsFishing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
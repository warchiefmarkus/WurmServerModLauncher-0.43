/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.FocusZone;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ final class Flattening
/*      */   implements MiscConstants, SoundNames
/*      */ {
/*   66 */   private static final Logger logger = Logger.getLogger(Flattening.class.getName());
/*      */   
/*   68 */   private static int[][] flattenTiles = new int[4][4];
/*      */   
/*   70 */   private static int[][] rockTiles = new int[4][4];
/*      */   
/*   72 */   private static boolean[][] immutableTiles = new boolean[4][4];
/*      */   
/*   74 */   private static boolean[][] changedTiles = new boolean[4][4];
/*      */   
/*   76 */   private static int flattenSkill = 0;
/*      */   
/*   78 */   private static double skill = 0.0D;
/*      */   
/*   80 */   private static int flattenRock = 0;
/*      */   
/*   82 */   private static int flattenDone = 0;
/*      */   
/*   84 */   private static int flattenImmutable = 0;
/*      */   
/*   86 */   private static int flattenSlope = 0;
/*      */ 
/*      */ 
/*      */   
/*   90 */   private static int needsDirt = 0;
/*      */   
/*      */   private static final float DIGGING_SKILL_MULT = 3.0F;
/*      */   private static final float FLATTENING_MAX_DEPTH = -7.0F;
/*   94 */   private static short newFHeight = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean raising = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void fillFlattenTiles(Creature performer, int tilex, int tiley) {
/*  114 */     for (int x = -1; x < 3; x++) {
/*      */       
/*  116 */       for (int y = -1; y < 3; y++) {
/*      */ 
/*      */         
/*      */         try {
/*  120 */           flattenTiles[x + 1][y + 1] = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/*  121 */           rockTiles[x + 1][y + 1] = Tiles.decodeHeight(Server.rockMesh.getTile(tilex + x, tiley + y));
/*  122 */           immutableTiles[x + 1][y + 1] = isTileUntouchable(performer, tilex + x, tiley + y);
/*      */         }
/*  124 */         catch (Exception ex) {
/*      */           
/*  126 */           immutableTiles[x + 1][y + 1] = true;
/*  127 */           flattenTiles[x + 1][y + 1] = -100;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isTileUntouchable(Creature performer, int tilex, int tiley) {
/*  135 */     for (int x = 0; x >= -1; x--) {
/*      */       
/*  137 */       for (int y = 0; y >= -1; y--) {
/*      */ 
/*      */         
/*  140 */         if (Zones.protectedTiles[tilex + x][tiley + y])
/*  141 */           return true; 
/*  142 */         if (Zones.isWithinDuelRing(tilex, tiley, true) != null)
/*      */         {
/*  144 */           return true;
/*      */         }
/*  146 */         if (Features.Feature.BLOCK_HOTA.isEnabled())
/*      */         {
/*  148 */           for (FocusZone fz : FocusZone.getZonesAt(tilex, tiley)) {
/*      */             
/*  150 */             if (fz.isBattleCamp() || fz.isPvPHota() || fz.isNoBuild())
/*      */             {
/*  152 */               if (fz.covers(tilex, tiley))
/*      */               {
/*  154 */                 return true;
/*      */               }
/*      */             }
/*      */           } 
/*      */         }
/*  159 */         VolaTile vtile = Zones.getOrCreateTile(tilex + x, tiley + y, performer.isOnSurface());
/*      */         
/*  161 */         if (vtile.getStructure() != null && performer.getPower() < 5) {
/*  162 */           return true;
/*      */         }
/*  164 */         Village village = vtile.getVillage();
/*  165 */         if (village != null) {
/*      */           
/*  167 */           int tile = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/*  168 */           if (!village.isActionAllowed((short)144, performer, false, tile, 0)) {
/*  169 */             return true;
/*      */           }
/*      */         } 
/*  172 */         Fence[] fences = vtile.getFencesForLevel(0);
/*  173 */         if (fences.length > 0) {
/*      */           
/*  175 */           if (x == 0 && y == 0)
/*  176 */             return true; 
/*  177 */           if (x == -1 && y == 0) {
/*      */             
/*  179 */             for (Fence f : fences) {
/*      */               
/*  181 */               if (f.isHorizontal()) {
/*  182 */                 return true;
/*      */               }
/*      */             } 
/*  185 */           } else if (y == -1 && x == 0) {
/*      */             
/*  187 */             for (Fence f : fences) {
/*      */               
/*  189 */               if (!f.isHorizontal())
/*  190 */                 return true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  196 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean flatten(Creature performer, Item source, int tile, int tilex, int tiley, float counter, Action act) {
/*  204 */     return flatten(-10L, performer, source, tile, tilex, tiley, 2, 2, 4, counter, act);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean flattenTileBorder(long borderId, Creature performer, Item source, int tilex, int tiley, Tiles.TileBorderDirection dir, float counter, Action act) {
/*  210 */     int tile = Zones.getTileIntForTile(tilex, tiley, performer.isOnSurface() ? 0 : 1);
/*  211 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN)
/*      */     {
/*  213 */       return flatten(borderId, performer, source, tile, tilex, tiley, 1, 2, 2, counter, act);
/*      */     }
/*      */ 
/*      */     
/*  217 */     return flatten(borderId, performer, source, tile, tilex, tiley, 2, 1, 2, counter, act);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean flatten(long borderId, Creature performer, Item source, int tile, int tilex, int tiley, int endX, int endY, int numbCorners, float counter, Action act) {
/*  226 */     boolean done = false;
/*  227 */     String verb = act.getActionEntry().getVerbString().toLowerCase();
/*  228 */     String action = act.getActionEntry().getActionString().toLowerCase();
/*      */     
/*  230 */     if (tilex - 2 < 0 || tilex + 2 > 1 << Constants.meshSize || tiley - 2 < 0 || tiley + 2 > 1 << Constants.meshSize) {
/*      */       
/*  232 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to " + action + ".");
/*  233 */       done = true;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  239 */       VolaTile vt = performer.getCurrentTile();
/*  240 */       if (vt != null && vt.getStructure() != null && performer.getPower() < 5) {
/*      */         
/*  242 */         performer.getCommunicator().sendNormalServerMessage("You can not " + action + " from the inside.");
/*  243 */         return true;
/*      */       } 
/*  245 */       byte type = Tiles.decodeType(tile);
/*  246 */       if (Terraforming.isRockTile(type)) {
/*      */         
/*  248 */         performer.getCommunicator().sendNormalServerMessage("You can not dig in the solid rock.");
/*  249 */         return true;
/*      */       } 
/*  251 */       if (!performer.isOnSurface()) {
/*      */         
/*  253 */         performer.getCommunicator().sendNormalServerMessage("You can not " + action + " from the inside.");
/*  254 */         return true;
/*      */       } 
/*  256 */       boolean isTooDeep = isTileTooDeep(tilex, tiley, endX, endY, numbCorners);
/*  257 */       if (act.getNumber() == 532 && 
/*  258 */         !Terraforming.isFlat(performer.getTileX(), performer.getTileY(), performer.isOnSurface(), 0)) {
/*      */         
/*  260 */         performer.getCommunicator().sendNormalServerMessage("You need to be " + (isTooDeep ? "above" : "standing on") + " flat ground to be able to level.");
/*      */         
/*  262 */         return true;
/*      */       } 
/*      */       
/*  265 */       if (!Terraforming.isNonDiggableTile(type)) {
/*      */         
/*  267 */         boolean insta = (source.isWand() && performer.getPower() >= 2);
/*      */         
/*  269 */         if ((act.currentSecond() % 10 == 0 && act.getNumber() == 150) || (act
/*  270 */           .currentSecond() % 5 == 0 && act.getNumber() != 150) || insta || counter == 1.0F) {
/*      */ 
/*      */           
/*  273 */           flattenSkill = 0;
/*  274 */           flattenImmutable = 0;
/*  275 */           flattenRock = 0;
/*  276 */           flattenDone = 0;
/*  277 */           flattenSlope = 0;
/*  278 */           needsDirt = 0;
/*  279 */           skill = 0.0D;
/*      */           
/*  281 */           fillFlattenTiles(performer, tilex, tiley);
/*  282 */           short maxHeight = Short.MIN_VALUE;
/*  283 */           short maxHeight2 = Short.MIN_VALUE;
/*  284 */           short minHeight = Short.MAX_VALUE;
/*  285 */           for (int x = 1; x <= endX; x++) {
/*      */             
/*  287 */             for (int y = 1; y <= endY; y++) {
/*      */               
/*  289 */               short ht = Tiles.decodeHeight(flattenTiles[x][y]);
/*  290 */               if (ht > maxHeight) {
/*      */                 
/*  292 */                 maxHeight2 = maxHeight;
/*  293 */                 maxHeight = ht;
/*      */               }
/*  295 */               else if (ht > maxHeight2) {
/*  296 */                 maxHeight2 = ht;
/*  297 */               }  if (ht < minHeight)
/*  298 */                 minHeight = ht; 
/*  299 */               if (performer.getStrengthSkill() < 21.0D)
/*      */               {
/*  301 */                 if (Terraforming.isRoad(Tiles.decodeType(flattenTiles[x][y]))) {
/*      */                   
/*  303 */                   performer.getCommunicator().sendNormalServerMessage("You need to be stronger in order to " + action + " near roads.");
/*      */                   
/*  305 */                   return true;
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           } 
/*  310 */           short lAverageHeight = calcAverageHeight(performer, tile, tilex, tiley, endX, endY, numbCorners, act);
/*  311 */           if (isFlat(performer, lAverageHeight, endX, endY, numbCorners, act)) {
/*      */             
/*  313 */             performer.getCommunicator().sendNormalServerMessage("Already flat!");
/*  314 */             checkChangedTiles(tile, endX, endY, performer);
/*  315 */             return true;
/*      */           } 
/*      */           
/*  318 */           int ddone = 0;
/*  319 */           Skills skills = performer.getSkills();
/*  320 */           Skill digging = null;
/*  321 */           Skill shovel = null;
/*  322 */           if (!insta) {
/*      */             
/*  324 */             if ((!isTooDeep && !source.isDiggingtool()) || (isTooDeep && 
/*  325 */               !source.isDredgingTool())) {
/*      */               
/*  327 */               performer.getCommunicator().sendNormalServerMessage("You can't " + action + " with that.");
/*  328 */               return true;
/*      */             } 
/*      */             
/*      */             try {
/*  332 */               digging = skills.getSkill(1009);
/*      */             }
/*  334 */             catch (Exception ex) {
/*      */               
/*  336 */               digging = skills.learn(1009, 1.0F);
/*      */             } 
/*      */             
/*      */             try {
/*  340 */               shovel = skills.getSkill(source.getPrimarySkill());
/*      */             }
/*  342 */             catch (Exception ex) {
/*      */ 
/*      */               
/*      */               try {
/*  346 */                 shovel = skills.learn(source.getPrimarySkill(), 1.0F);
/*      */               }
/*  348 */               catch (NoSuchSkillException nse) {
/*      */                 
/*  350 */                 if (performer.getPower() <= 0)
/*  351 */                   logger.log(Level.WARNING, performer
/*      */                       
/*  353 */                       .getName() + " trying to " + action + " with an item with no primary skill: " + source
/*      */                       
/*  355 */                       .getName()); 
/*      */               } 
/*      */             } 
/*  358 */             skill = digging.getKnowledge(0.0D);
/*      */           } else {
/*      */             
/*  361 */             skill = 99.0D;
/*      */           } 
/*  363 */           if (type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_TAR.id || type == Tiles.Tile.TILE_PEAT.id)
/*      */           {
/*      */             
/*  366 */             if (skill < 70.0D) {
/*      */               
/*  368 */               performer.getCommunicator().sendNormalServerMessage("You just can not work how to " + action + " here it seems.");
/*      */               
/*  370 */               return true;
/*      */             } 
/*      */           }
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
/*  384 */           int tickTimes = 5;
/*  385 */           if (act.getNumber() == 150)
/*  386 */             tickTimes = 10; 
/*  387 */           if (type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_TAR.id || type == Tiles.Tile.TILE_PEAT.id) {
/*  388 */             tickTimes = 30;
/*      */           }
/*  390 */           if (counter == 1.0F && !insta) {
/*      */             float t;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  399 */             if (act.getNumber() == 532) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  405 */               int difmax = Math.abs(maxHeight - lAverageHeight);
/*  406 */               int difmax2 = Math.abs(maxHeight2 - lAverageHeight);
/*  407 */               int difmin = Math.abs(minHeight - lAverageHeight);
/*      */               
/*  409 */               if (minHeight < lAverageHeight) {
/*  410 */                 t = (Math.max(difmin, difmax) * tickTimes * 10);
/*      */               } else {
/*  412 */                 t = ((difmax + difmax2) * tickTimes * 10);
/*      */               } 
/*  414 */             } else if (act.getNumber() == 865) {
/*      */               
/*  416 */               t = ((maxHeight - minHeight) * tickTimes * 10);
/*      */             }
/*  418 */             else if (act.getNumber() == 533) {
/*      */               
/*  420 */               t = ((maxHeight - minHeight) / 4 * tickTimes * 10 + 50);
/*      */             } else {
/*      */               
/*  423 */               t = ((maxHeight - minHeight) / numbCorners * tickTimes * 10 + 100);
/*      */             } 
/*  425 */             if (t > 65535.0F) {
/*  426 */               t = 65535.0F;
/*      */             }
/*  428 */             int time = (int)Math.max(50.0F, t);
/*      */             
/*  430 */             String ctype = "ground";
/*  431 */             String btype = "the ground";
/*  432 */             if (act.getNumber() == 533 || act.getNumber() == 865) {
/*      */               
/*  434 */               ctype = "tile border";
/*  435 */               btype = "a tile border";
/*      */             } 
/*  437 */             performer.getCommunicator().sendNormalServerMessage("You start to " + action + " the " + ctype + ".");
/*      */             
/*  439 */             Server.getInstance().broadCastAction(performer
/*  440 */                 .getName() + " starts to " + action + " " + btype + ".", performer, 5);
/*      */             
/*  442 */             performer.sendActionControl(action, true, time);
/*  443 */             source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */           } 
/*      */           
/*  446 */           if (act.currentSecond() % tickTimes == 0 || insta)
/*      */           {
/*  448 */             if (Zones.protectedTiles[tilex][tiley]) {
/*      */               
/*  450 */               performer.getCommunicator().sendNormalServerMessage("Your body goes limp and you find no strength to continue here. Weird.");
/*      */               
/*  452 */               return true;
/*      */             } 
/*      */             
/*  455 */             if (performer.getStatus().getStamina() < 5000) {
/*      */               
/*  457 */               performer.getCommunicator().sendNormalServerMessage("You must rest.");
/*  458 */               return true;
/*      */             } 
/*  460 */             if (!insta)
/*      */             {
/*  462 */               performer.getStatus().modifyStamina(-4000.0F);
/*      */             }
/*      */             
/*  465 */             String sstring = "sound.work.digging1";
/*  466 */             int snd = Server.rand.nextInt(3);
/*  467 */             if (snd == 0) {
/*  468 */               sstring = "sound.work.digging2";
/*  469 */             } else if (snd == 1) {
/*  470 */               sstring = "sound.work.digging3";
/*  471 */             }  SoundPlayer.playSound(sstring, performer, 0.0F);
/*  472 */             resetChangedTiles();
/*  473 */             if (act.getNumber() == 533 || act.getNumber() == 865) {
/*      */               
/*  475 */               if (checkBorderCorners(performer, tilex, tiley, tilex, tiley, 1, 1, endX, endY, 
/*  476 */                   (int)Math.max(3.0D, skill * 3.0D), lAverageHeight, act))
/*      */               {
/*  478 */                 ddone++; } 
/*  479 */               if (checkBorderCorners(performer, tilex, tiley, tilex + endX - 1, tiley + endY - 1, endX, endY, 1, 1, 
/*  480 */                   (int)Math.max(3.0D, skill * 3.0D), lAverageHeight, act))
/*      */               {
/*  482 */                 ddone++;
/*      */               }
/*      */             } else {
/*      */               
/*  486 */               for (int xx = 1; xx <= endX; xx++) {
/*      */                 
/*  488 */                 for (int yy = 1; yy <= endY; yy++) {
/*      */                   
/*  490 */                   if (checkFlattenCorner(performer, tilex, tiley, tilex + xx - 1, tiley + yy - 1, xx, yy, 
/*  491 */                       (int)Math.max(3.0D, skill * 3.0D), lAverageHeight, act))
/*      */                   {
/*      */                     
/*  494 */                     ddone++;
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  504 */             checkChangedTiles(tile, tilex, tiley, performer);
/*      */ 
/*      */ 
/*      */             
/*  508 */             if (ddone + flattenSkill + flattenImmutable + flattenRock + flattenSlope >= numbCorners) {
/*      */               
/*  510 */               if (flattenSkill > 0)
/*  511 */                 performer.getCommunicator().sendNormalServerMessage("Some slope is too steep for your skill level."); 
/*  512 */               if (flattenImmutable > 0)
/*  513 */                 performer.getCommunicator().sendNormalServerMessage("Some corners can't be modified."); 
/*  514 */               if (flattenRock > 0)
/*  515 */                 performer.getCommunicator().sendNormalServerMessage("You hit the rock in a corner."); 
/*  516 */               if (flattenDone > 0)
/*  517 */                 performer.getCommunicator().sendNormalServerMessage("You have already flattened a corner."); 
/*  518 */               if (flattenSlope > 0) {
/*  519 */                 performer.getCommunicator().sendNormalServerMessage("The highway would become impassable.");
/*      */               }
/*  521 */               if (ddone == numbCorners) {
/*      */                 
/*  523 */                 done = true;
/*  524 */                 if (flattenSkill == 0 && flattenImmutable == 0 && flattenSlope == 0)
/*      */                 {
/*      */                   
/*  527 */                   checkUseDirt(tilex, tiley, endX, endY, performer, source, 
/*  528 */                       (int)Math.max(10.0D, skill * 3.0D), lAverageHeight, act, (insta && source
/*  529 */                       .getAuxData() == 1));
/*      */                 }
/*  531 */                 if (needsDirt > 0)
/*      */                 {
/*  533 */                   performer.getCommunicator().sendNormalServerMessage("If you carried some dirt, it would be used to fill the " + needsDirt + " corners that need it.");
/*      */                 }
/*      */ 
/*      */                 
/*  537 */                 lAverageHeight = calcAverageHeight(performer, tile, tilex, tiley, endX, endY, numbCorners, act);
/*  538 */                 if (!isFlat(performer, lAverageHeight, endX, endY, numbCorners, act)) {
/*      */                   
/*  540 */                   if (needsDirt == 0 && (act.getNumber() == 532 || act.getNumber() == 865)) {
/*  541 */                     done = false;
/*      */                   } else {
/*  543 */                     performer.getCommunicator().sendNormalServerMessage("You finish " + verb + ".");
/*      */                   }
/*      */                 
/*      */                 } else {
/*      */                   
/*  548 */                   performer.achievement(514);
/*      */                 } 
/*      */               } 
/*  551 */               checkChangedTiles(tile, tilex, tiley, performer);
/*  552 */               if (act.getNumber() == 533 || act.getNumber() == 865)
/*      */               {
/*  554 */                 if (performer.getVisionArea() != null && borderId != -10L)
/*      */                 {
/*  556 */                   performer.getVisionArea().broadCastUpdateSelectBar(borderId);
/*      */                 }
/*      */               }
/*  559 */               if (done || flattenSkill + flattenImmutable + flattenRock + flattenSlope > 0) {
/*  560 */                 return true;
/*      */               }
/*      */             } 
/*  563 */             if (performer.getStatus().getStamina() < 5000) {
/*      */               
/*  565 */               performer.getCommunicator().sendNormalServerMessage("You must rest.");
/*  566 */               return true;
/*      */             } 
/*      */ 
/*      */             
/*  570 */             if (!insta)
/*      */             {
/*  572 */               source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */               
/*  574 */               float skilltimes = 10.0F;
/*  575 */               if (act.getNumber() == 533)
/*  576 */                 skilltimes = 5.0F; 
/*  577 */               if (act.getNumber() == 865)
/*  578 */                 skilltimes = 2.5F; 
/*  579 */               if (act.getNumber() == 532) {
/*  580 */                 skilltimes = 1.5F;
/*      */               }
/*  582 */               if (shovel != null) {
/*  583 */                 shovel.skillCheck(30.0D, source, 0.0D, false, skilltimes);
/*      */               }
/*  585 */               digging.skillCheck(30.0D, source, 0.0D, false, skilltimes);
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/*  593 */         done = true;
/*  594 */         performer.getCommunicator().sendNormalServerMessage("You can't " + action + " that place, as " + source
/*  595 */             .getNameWithGenus() + " just won't do.");
/*      */       } 
/*      */     } 
/*  598 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkChangedTiles(int tile, int tilex, int tiley, Creature performer) {
/*  603 */     for (int x = 0; x < 4; x++) {
/*      */       
/*  605 */       for (int y = 0; y < 4; y++) {
/*      */         
/*  607 */         int modx = x - 1;
/*  608 */         int mody = y - 1;
/*      */         
/*  610 */         if (x < 3 && y < 3 && shouldBeRock(x, y)) {
/*      */           
/*  612 */           changedTiles[x][y] = true;
/*  613 */           flattenTiles[x][y] = Tiles.encode(Tiles.decodeHeight(flattenTiles[x][y]), Tiles.Tile.TILE_ROCK.id, (byte)0);
/*      */         } 
/*  615 */         if (changedTiles[x][y]) {
/*      */           
/*  617 */           changedTiles[x][y] = false;
/*  618 */           Server.surfaceMesh.setTile(tilex + modx, tiley + mody, flattenTiles[x][y]);
/*  619 */           Server.setBotanizable(tilex + modx, tiley + mody, false);
/*  620 */           Server.setForagable(tilex + modx, tiley + mody, false);
/*  621 */           Server.isDirtHeightLower(tilex + modx, tiley + mody, Tiles.decodeHeight(flattenTiles[x][y]));
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  626 */             Zone toCheckForChange = Zones.getZone(tilex + modx, tiley + mody, performer.isOnSurface());
/*  627 */             toCheckForChange.changeTile(tilex + modx, tiley + mody);
/*      */           }
/*  629 */           catch (NoSuchZoneException nsz) {
/*      */             
/*  631 */             logger.log(Level.INFO, "no such zone?: " + (tilex + modx) + ", " + (tiley + mody), (Throwable)nsz);
/*      */           } 
/*      */           
/*  634 */           performer.getMovementScheme().touchFreeMoveCounter();
/*  635 */           Players.getInstance().sendChangedTile(tilex + modx, tiley + mody, performer.isOnSurface(), true);
/*      */         } 
/*      */         
/*  638 */         int lNewTile = Server.surfaceMesh.getTile(tilex + modx, tiley + mody);
/*  639 */         byte type = Tiles.decodeType(lNewTile);
/*  640 */         Tiles.Tile theTile = Tiles.getTile(type);
/*  641 */         if (theTile.isTree()) {
/*      */           
/*  643 */           byte data = Tiles.decodeData(lNewTile);
/*  644 */           Zones.reposWildHive(tilex + modx, tiley + mody, theTile, data);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void resetChangedTiles() {
/*  652 */     for (int x = 0; x < 4; x++) {
/*      */       
/*  654 */       for (int y = 0; y < 4; y++)
/*      */       {
/*  656 */         changedTiles[x][y] = false;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static short calcAverageHeight(Creature performer, int tile, int tilex, int tiley, int endX, int endY, int numbCorners, Action act) {
/*  664 */     float lAverageHeight = 0.0F;
/*      */     
/*  666 */     if (act.getNumber() == 532) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  671 */       int mytile = Server.surfaceMesh.getTile(performer.getTileX(), performer.getTileY());
/*  672 */       lAverageHeight = Tiles.decodeHeight(mytile);
/*      */     }
/*  674 */     else if (act.getNumber() == 865) {
/*      */ 
/*      */       
/*  677 */       float distX = performer.getPosX() - (tilex << 2);
/*  678 */       float distY = performer.getPosY() - (tiley << 2);
/*  679 */       double dist = Math.sqrt((distX * distX + distY * distY));
/*      */       
/*  681 */       distX = performer.getPosX() - (tilex + endX - 1 << 2);
/*  682 */       distY = performer.getPosY() - (tiley + endY - 1 << 2);
/*  683 */       double dist2 = Math.sqrt((distX * distX + distY * distY));
/*      */       
/*  685 */       if (dist < dist2)
/*      */       {
/*  687 */         lAverageHeight = Tiles.decodeHeight(flattenTiles[1][1]);
/*      */       }
/*      */       else
/*      */       {
/*  691 */         lAverageHeight = Tiles.decodeHeight(flattenTiles[endX][endY]);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  696 */       for (int x = 1; x <= endX; x++) {
/*      */         
/*  698 */         for (int y = 1; y <= endY; y++)
/*      */         {
/*  700 */           lAverageHeight += Tiles.decodeHeight(flattenTiles[x][y]);
/*      */         }
/*      */       } 
/*      */       
/*  704 */       lAverageHeight = lAverageHeight / numbCorners + 1.0F / numbCorners;
/*      */     } 
/*  706 */     return (short)(int)lAverageHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isFlat(Creature performer, short requiredHeight, int endX, int endY, int numbCorners, Action act) {
/*  712 */     int ddone = 0;
/*  713 */     for (int x = 1; x <= endX; x++) {
/*      */       
/*  715 */       for (int y = 1; y <= endY; y++) {
/*      */         
/*  717 */         if (Tiles.decodeHeight(flattenTiles[x][y]) == requiredHeight)
/*  718 */           ddone++; 
/*      */       } 
/*      */     } 
/*  721 */     if (ddone == numbCorners) {
/*      */       
/*  723 */       String ctype = "ground";
/*  724 */       if (act.getNumber() == 533 || act.getNumber() == 865)
/*  725 */         ctype = "tile border"; 
/*  726 */       performer.getCommunicator().sendNormalServerMessage("The " + ctype + " is flat here.");
/*      */       
/*  728 */       return true;
/*      */     } 
/*  730 */     return false;
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
/*      */   private static final boolean checkFlattenCorner(Creature performer, int initx, int inity, int tilex, int tiley, int x, int y, int maxDiff, int preferredHeight, Action act) {
/*  742 */     boolean raise = false;
/*  743 */     if (Tiles.decodeHeight(flattenTiles[x][y]) < preferredHeight) {
/*  744 */       raise = true;
/*  745 */     } else if (Tiles.decodeHeight(flattenTiles[x][y]) == preferredHeight) {
/*  746 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  752 */     if (raise && !mayRaiseCorner(initx, inity, flattenTiles[x][y], x, y, maxDiff))
/*  753 */       return true; 
/*  754 */     if (!raise && !mayLowerCorner(initx, inity, flattenTiles[x][y], rockTiles[x][y], x, y, maxDiff)) {
/*  755 */       return true;
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
/*  766 */     if (x == 1 && y == 1) {
/*      */       
/*  768 */       if (changeCorner(performer, tilex, tiley, x, y, x + 1, y, maxDiff, preferredHeight, raise, act)) {
/*      */         
/*  770 */         if (changeCorner(performer, tilex, tiley, x, y, x, y + 1, maxDiff, preferredHeight, raise, act)) {
/*      */           
/*  772 */           if (changeCorner(performer, tilex, tiley, x, y, x + 1, y + 1, maxDiff, preferredHeight, raise, act))
/*      */           {
/*      */ 
/*      */             
/*  776 */             return true;
/*      */           }
/*      */ 
/*      */           
/*  780 */           return isCornerDone(x, y, preferredHeight);
/*      */         } 
/*      */ 
/*      */         
/*  784 */         return isCornerDone(x, y, preferredHeight);
/*      */       } 
/*      */       
/*  787 */       return isCornerDone(x, y, preferredHeight);
/*      */     } 
/*  789 */     if (x == 2 && y == 1) {
/*      */       
/*  791 */       if (changeCorner(performer, tilex, tiley, x, y, x - 1, y, maxDiff, preferredHeight, raise, act)) {
/*      */         
/*  793 */         if (changeCorner(performer, tilex, tiley, x, y, x, y + 1, maxDiff, preferredHeight, raise, act)) {
/*      */           
/*  795 */           if (changeCorner(performer, tilex, tiley, x, y, x - 1, y + 1, maxDiff, preferredHeight, raise, act))
/*      */           {
/*      */ 
/*      */             
/*  799 */             return true;
/*      */           }
/*      */           
/*  802 */           return isCornerDone(x, y, preferredHeight);
/*      */         } 
/*      */         
/*  805 */         return isCornerDone(x, y, preferredHeight);
/*      */       } 
/*      */       
/*  808 */       return isCornerDone(x, y, preferredHeight);
/*      */     } 
/*  810 */     if (x == 1 && y == 2) {
/*      */       
/*  812 */       if (changeCorner(performer, tilex, tiley, x, y, x + 1, y, maxDiff, preferredHeight, raise, act)) {
/*      */         
/*  814 */         if (changeCorner(performer, tilex, tiley, x, y, x, y - 1, maxDiff, preferredHeight, raise, act)) {
/*      */           
/*  816 */           if (changeCorner(performer, tilex, tiley, x, y, x + 1, y - 1, maxDiff, preferredHeight, raise, act))
/*      */           {
/*      */ 
/*      */             
/*  820 */             return true;
/*      */           }
/*      */           
/*  823 */           return isCornerDone(x, y, preferredHeight);
/*      */         } 
/*      */         
/*  826 */         return isCornerDone(x, y, preferredHeight);
/*      */       } 
/*      */       
/*  829 */       return isCornerDone(x, y, preferredHeight);
/*      */     } 
/*  831 */     if (x == 2 && y == 2) {
/*      */       
/*  833 */       if (changeCorner(performer, tilex, tiley, x, y, x - 1, y, maxDiff, preferredHeight, raise, act)) {
/*      */         
/*  835 */         if (changeCorner(performer, tilex, tiley, x, y, x, y - 1, maxDiff, preferredHeight, raise, act)) {
/*      */           
/*  837 */           if (changeCorner(performer, tilex, tiley, x, y, x - 1, y - 1, maxDiff, preferredHeight, raise, act))
/*      */           {
/*      */ 
/*      */             
/*  841 */             return true;
/*      */           }
/*      */           
/*  844 */           return isCornerDone(x, y, preferredHeight);
/*      */         } 
/*      */         
/*  847 */         return isCornerDone(x, y, preferredHeight);
/*      */       } 
/*      */       
/*  850 */       return isCornerDone(x, y, preferredHeight);
/*      */     } 
/*  852 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean checkBorderCorners(Creature performer, int initx, int inity, int tilex, int tiley, int x1, int y1, int x2, int y2, int maxDiff, int preferredHeight, Action act) {
/*  860 */     boolean raise = false;
/*  861 */     if (Tiles.decodeHeight(flattenTiles[x1][y1]) < preferredHeight) {
/*  862 */       raise = true;
/*  863 */     } else if (Tiles.decodeHeight(flattenTiles[x1][y1]) == preferredHeight) {
/*  864 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  870 */     if (raise && !mayRaiseCorner(initx, inity, flattenTiles[x1][y1], x1, y1, maxDiff))
/*  871 */       return true; 
/*  872 */     if (!raise && !mayLowerCorner(initx, inity, flattenTiles[x1][y1], rockTiles[x1][y1], x1, y1, maxDiff)) {
/*  873 */       return true;
/*      */     }
/*  875 */     if (changeCorner(performer, tilex, tiley, x1, y1, x2, y2, maxDiff, preferredHeight, raise, act))
/*      */     {
/*  877 */       return true;
/*      */     }
/*      */     
/*  880 */     return isCornerDone(x1, y1, preferredHeight);
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
/*      */   private static final boolean changeCorner(Creature performer, int tilex, int tiley, int x, int y, int targx, int targy, int maxDiff, int preferredHeight, boolean raise, Action act) {
/*  904 */     if (raise) {
/*      */       
/*  906 */       if (Tiles.decodeHeight(flattenTiles[targx][targy]) > preferredHeight)
/*      */       {
/*  908 */         if (mayLowerCorner(tilex, tiley, flattenTiles[targx][targy], rockTiles[targx][targy], targx, targy, maxDiff))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*  913 */           if (!changeFlattenCorner(performer, targx, targy, maxDiff, preferredHeight, false, act)) {
/*  914 */             return changeFlattenCorner(performer, x, y, maxDiff, preferredHeight, true, act);
/*      */           
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  920 */     else if (Tiles.decodeHeight(flattenTiles[targx][targy]) < preferredHeight) {
/*      */       
/*  922 */       if (mayRaiseCorner(tilex, tiley, flattenTiles[targx][targy], targx, targy, maxDiff))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  927 */         if (!changeFlattenCorner(performer, targx, targy, maxDiff, preferredHeight, true, act)) {
/*  928 */           return changeFlattenCorner(performer, x, y, maxDiff, preferredHeight, false, act);
/*      */         }
/*      */       }
/*      */     } 
/*  932 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean mayRaiseCorner(int tilex, int tiley, int tile, int x, int y, int maxDiff) {
/*  938 */     byte newType = Tiles.decodeType(tile);
/*  939 */     if (newType == Tiles.Tile.TILE_HOLE.id || Tiles.isMineDoor(newType)) {
/*      */       
/*  941 */       flattenImmutable++;
/*  942 */       return false;
/*      */     } 
/*  944 */     if (immutableTiles[x][y]) {
/*      */       
/*  946 */       flattenImmutable++;
/*  947 */       return false;
/*      */     } 
/*      */     
/*  950 */     if (Terraforming.isImmutableTile(Tiles.decodeType(tile)) || Tiles.decodeType(tile) == Tiles.Tile.TILE_HOLE.id) {
/*      */       
/*  952 */       flattenImmutable++;
/*  953 */       return false;
/*      */     } 
/*      */     
/*  956 */     if (Terraforming.isImmutableTile(Tiles.decodeType(flattenTiles[x][y - 1])) || 
/*  957 */       Tiles.decodeType(flattenTiles[x][y - 1]) == Tiles.Tile.TILE_HOLE.id) {
/*      */       
/*  959 */       flattenImmutable++;
/*  960 */       return false;
/*      */     } 
/*      */     
/*  963 */     if (Terraforming.isImmutableTile(Tiles.decodeType(flattenTiles[x - 1][y])) || 
/*  964 */       Tiles.decodeType(flattenTiles[x - 1][y]) == Tiles.Tile.TILE_HOLE.id) {
/*      */       
/*  966 */       flattenImmutable++;
/*  967 */       return false;
/*      */     } 
/*      */     
/*  970 */     if (Terraforming.isImmutableTile(Tiles.decodeType(flattenTiles[x - 1][y - 1])) || 
/*  971 */       Tiles.decodeType(flattenTiles[x - 1][y - 1]) == Tiles.Tile.TILE_HOLE.id) {
/*      */       
/*  973 */       flattenImmutable++;
/*  974 */       return false;
/*      */     } 
/*      */     
/*  977 */     int htN = Tiles.decodeHeight(flattenTiles[x][y - 1]);
/*  978 */     int htE = Tiles.decodeHeight(flattenTiles[x + 1][y]);
/*  979 */     int htS = Tiles.decodeHeight(flattenTiles[x][y + 1]);
/*  980 */     int htW = Tiles.decodeHeight(flattenTiles[x - 1][y]);
/*  981 */     int ht = Tiles.decodeHeight(flattenTiles[x][y]);
/*      */     
/*  983 */     if (Math.abs(ht - htS) > maxDiff) {
/*      */       
/*  985 */       flattenSkill++;
/*  986 */       return false;
/*      */     } 
/*  988 */     if (Math.abs(ht - htN) > maxDiff) {
/*      */       
/*  990 */       flattenSkill++;
/*  991 */       return false;
/*      */     } 
/*  993 */     if (Math.abs(ht - htE) > maxDiff) {
/*      */       
/*  995 */       flattenSkill++;
/*  996 */       return false;
/*      */     } 
/*  998 */     if (Math.abs(ht - htW) > maxDiff) {
/*      */       
/* 1000 */       flattenSkill++;
/* 1001 */       return false;
/*      */     } 
/*      */     
/* 1004 */     int htNE = Tiles.decodeHeight(flattenTiles[x + 1][y - 1]);
/* 1005 */     int htSE = Tiles.decodeHeight(flattenTiles[x + 1][y + 1]);
/* 1006 */     int htSW = Tiles.decodeHeight(flattenTiles[x - 1][y + 1]);
/* 1007 */     int htNW = Tiles.decodeHeight(flattenTiles[x - 1][y - 1]);
/*      */     
/* 1009 */     boolean pC = Tiles.isRoadType(flattenTiles[x][y]);
/* 1010 */     boolean pN = Tiles.isRoadType(flattenTiles[x][y - 1]);
/* 1011 */     boolean pNW = Tiles.isRoadType(flattenTiles[x - 1][y - 1]);
/* 1012 */     boolean pW = Tiles.isRoadType(flattenTiles[x - 1][y]);
/*      */     
/* 1014 */     boolean hC = (pC && MethodsHighways.onHighway(tilex + x - 1, tiley + y - 1, true));
/* 1015 */     boolean hN = (pN && MethodsHighways.onHighway(tilex + x - 1, tiley + y - 2, true));
/* 1016 */     boolean hNW = (pNW && MethodsHighways.onHighway(tilex + x - 2, tiley + y - 2, true));
/* 1017 */     boolean hW = (pW && MethodsHighways.onHighway(tilex + x - 2, tiley + y - 1, true));
/*      */     
/* 1019 */     int dS = ht - htS;
/* 1020 */     int dN = ht - htN;
/* 1021 */     int dE = ht - htE;
/* 1022 */     int dW = ht - htW;
/* 1023 */     if (Features.Feature.WAGONER.isEnabled()) {
/*      */ 
/*      */       
/* 1026 */       boolean wC = (hC && MethodsHighways.onWagonerCamp(tilex + x - 1, tiley + y - 1, true));
/* 1027 */       boolean wN = (hN && MethodsHighways.onWagonerCamp(tilex + x - 1, tiley + y - 2, true));
/* 1028 */       boolean wNW = (hNW && MethodsHighways.onWagonerCamp(tilex + x - 2, tiley + y - 2, true));
/* 1029 */       boolean wW = (hW && MethodsHighways.onWagonerCamp(tilex + x - 2, tiley + y - 1, true));
/* 1030 */       if ((wC || wN) && dE >= 0) {
/*      */         
/* 1032 */         flattenSlope++;
/* 1033 */         return false;
/*      */       } 
/* 1035 */       if ((wC || wW) && dS >= 0) {
/*      */         
/* 1037 */         flattenSlope++;
/* 1038 */         return false;
/*      */       } 
/* 1040 */       if ((wW || wNW) && dW >= 0) {
/*      */         
/* 1042 */         flattenSlope++;
/* 1043 */         return false;
/*      */       } 
/* 1045 */       if ((wN || wNW) && dN >= 0) {
/*      */         
/* 1047 */         flattenSlope++;
/* 1048 */         return false;
/*      */       } 
/* 1050 */       return true;
/*      */     } 
/*      */     
/* 1053 */     int dNE = ht - htNE;
/* 1054 */     int dSE = ht - htSE;
/* 1055 */     int dSW = ht - htSW;
/* 1056 */     int dNW = ht - htNW;
/*      */     
/* 1058 */     if ((hC || hN) && dE >= 20) {
/*      */       
/* 1060 */       flattenSlope++;
/* 1061 */       return false;
/*      */     } 
/* 1063 */     if ((hC || hW) && dS >= 20) {
/*      */       
/* 1065 */       flattenSlope++;
/* 1066 */       return false;
/*      */     } 
/* 1068 */     if ((hW || hNW) && dW >= 20) {
/*      */       
/* 1070 */       flattenSlope++;
/* 1071 */       return false;
/*      */     } 
/* 1073 */     if ((hN || hNW) && dN >= 20) {
/*      */       
/* 1075 */       flattenSlope++;
/* 1076 */       return false;
/*      */     } 
/*      */     
/* 1079 */     if (hC && dSE >= 28) {
/*      */       
/* 1081 */       flattenSlope++;
/* 1082 */       return false;
/*      */     } 
/* 1084 */     if (hW && dSW >= 28) {
/*      */       
/* 1086 */       flattenSlope++;
/* 1087 */       return false;
/*      */     } 
/* 1089 */     if (hNW && dNW >= 28) {
/*      */       
/* 1091 */       flattenSlope++;
/* 1092 */       return false;
/*      */     } 
/* 1094 */     if (hN && dNE >= 28) {
/*      */       
/* 1096 */       flattenSlope++;
/* 1097 */       return false;
/*      */     } 
/* 1099 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean mayLowerCorner(int tilex, int tiley, int tile, int rocktile, int x, int y, int maxDiff) {
/* 1104 */     if (immutableTiles[x][y]) {
/*      */       
/* 1106 */       flattenImmutable++;
/* 1107 */       return false;
/*      */     } 
/* 1109 */     byte newType = Tiles.decodeType(tile);
/* 1110 */     if (newType == Tiles.Tile.TILE_HOLE.id || newType == Tiles.Tile.TILE_CLIFF.id || Tiles.isMineDoor(newType)) {
/*      */       
/* 1112 */       flattenImmutable++;
/* 1113 */       return false;
/*      */     } 
/* 1115 */     if (Tiles.decodeHeight(tile) <= Tiles.decodeHeight(rocktile)) {
/*      */       
/* 1117 */       flattenRock++;
/* 1118 */       return false;
/*      */     } 
/* 1120 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_HOLE.id) {
/*      */       
/* 1122 */       flattenImmutable++;
/* 1123 */       return false;
/*      */     } 
/* 1125 */     int htN = Tiles.decodeHeight(flattenTiles[x][y - 1]);
/* 1126 */     int htE = Tiles.decodeHeight(flattenTiles[x + 1][y]);
/* 1127 */     int htS = Tiles.decodeHeight(flattenTiles[x][y + 1]);
/* 1128 */     int htW = Tiles.decodeHeight(flattenTiles[x - 1][y]);
/* 1129 */     int ht = Tiles.decodeHeight(flattenTiles[x][y]);
/* 1130 */     if (Math.abs(htS - ht) > maxDiff) {
/*      */       
/* 1132 */       flattenSkill++;
/* 1133 */       return false;
/*      */     } 
/* 1135 */     if (Math.abs(htN - ht) > maxDiff) {
/*      */       
/* 1137 */       flattenSkill++;
/* 1138 */       return false;
/*      */     } 
/* 1140 */     if (Math.abs(htE - ht) > maxDiff) {
/*      */       
/* 1142 */       flattenSkill++;
/* 1143 */       return false;
/*      */     } 
/* 1145 */     if (Math.abs(htW - ht) > maxDiff) {
/*      */       
/* 1147 */       flattenSkill++;
/* 1148 */       return false;
/*      */     } 
/*      */     
/* 1151 */     int htNE = Tiles.decodeHeight(flattenTiles[x + 1][y - 1]);
/* 1152 */     int htSE = Tiles.decodeHeight(flattenTiles[x + 1][y + 1]);
/* 1153 */     int htSW = Tiles.decodeHeight(flattenTiles[x - 1][y + 1]);
/* 1154 */     int htNW = Tiles.decodeHeight(flattenTiles[x - 1][y - 1]);
/*      */     
/* 1156 */     boolean pC = Tiles.isRoadType(flattenTiles[x][y]);
/* 1157 */     boolean pN = Tiles.isRoadType(flattenTiles[x][y - 1]);
/* 1158 */     boolean pNW = Tiles.isRoadType(flattenTiles[x - 1][y - 1]);
/* 1159 */     boolean pW = Tiles.isRoadType(flattenTiles[x - 1][y]);
/*      */     
/* 1161 */     boolean hC = (pC && MethodsHighways.onHighway(tilex + x - 1, tiley + y - 1, true));
/* 1162 */     boolean hN = (pN && MethodsHighways.onHighway(tilex + x - 1, tiley + y - 2, true));
/* 1163 */     boolean hNW = (pNW && MethodsHighways.onHighway(tilex + x - 2, tiley + y - 2, true));
/* 1164 */     boolean hW = (pW && MethodsHighways.onHighway(tilex + x - 2, tiley + y - 1, true));
/*      */     
/* 1166 */     int dS = htS - ht;
/* 1167 */     int dN = htN - ht;
/* 1168 */     int dE = htE - ht;
/* 1169 */     int dW = htW - ht;
/* 1170 */     if (Features.Feature.WAGONER.isEnabled()) {
/*      */ 
/*      */       
/* 1173 */       boolean wC = (hC && MethodsHighways.onWagonerCamp(tilex + x - 1, tiley + y - 1, true));
/* 1174 */       boolean wN = (hN && MethodsHighways.onWagonerCamp(tilex + x - 1, tiley + y - 2, true));
/* 1175 */       boolean wNW = (hNW && MethodsHighways.onWagonerCamp(tilex + x - 2, tiley + y - 2, true));
/* 1176 */       boolean wW = (hW && MethodsHighways.onWagonerCamp(tilex + x - 2, tiley + y - 1, true));
/* 1177 */       if ((wC || wN) && dE >= 0) {
/*      */         
/* 1179 */         flattenSlope++;
/* 1180 */         return false;
/*      */       } 
/* 1182 */       if ((wC || wW) && dS >= 0) {
/*      */         
/* 1184 */         flattenSlope++;
/* 1185 */         return false;
/*      */       } 
/* 1187 */       if ((wW || wNW) && dW >= 0) {
/*      */         
/* 1189 */         flattenSlope++;
/* 1190 */         return false;
/*      */       } 
/* 1192 */       if ((wN || wNW) && dN >= 0) {
/*      */         
/* 1194 */         flattenSlope++;
/* 1195 */         return false;
/*      */       } 
/* 1197 */       return true;
/*      */     } 
/*      */     
/* 1200 */     int dNE = htNE - ht;
/* 1201 */     int dSE = htSE - ht;
/* 1202 */     int dSW = htSW - ht;
/* 1203 */     int dNW = htNW - ht;
/*      */     
/* 1205 */     if ((hC || hN) && dE >= 20) {
/*      */       
/* 1207 */       flattenSlope++;
/* 1208 */       return false;
/*      */     } 
/* 1210 */     if ((hC || hW) && dS >= 20) {
/*      */       
/* 1212 */       flattenSlope++;
/* 1213 */       return false;
/*      */     } 
/* 1215 */     if ((hW || hNW) && dW >= 20) {
/*      */       
/* 1217 */       flattenSlope++;
/* 1218 */       return false;
/*      */     } 
/* 1220 */     if ((hN || hNW) && dN >= 20) {
/*      */       
/* 1222 */       flattenSlope++;
/* 1223 */       return false;
/*      */     } 
/*      */     
/* 1226 */     if (hC && dSE >= 28) {
/*      */       
/* 1228 */       flattenSlope++;
/* 1229 */       return false;
/*      */     } 
/* 1231 */     if (hW && dSW >= 28) {
/*      */       
/* 1233 */       flattenSlope++;
/* 1234 */       return false;
/*      */     } 
/* 1236 */     if (hNW && dNW >= 28) {
/*      */       
/* 1238 */       flattenSlope++;
/* 1239 */       return false;
/*      */     } 
/* 1241 */     if (hN && dNE >= 28) {
/*      */       
/* 1243 */       flattenSlope++;
/* 1244 */       return false;
/*      */     } 
/* 1246 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean shouldBeRock(int x, int y) {
/* 1251 */     int numberOfCornersAtRockHeight = 0;
/* 1252 */     for (int xx = 0; xx <= 1; xx++) {
/*      */       
/* 1254 */       for (int yy = 0; yy <= 1; yy++) {
/*      */         
/* 1256 */         short tileHeight = Tiles.decodeHeight(flattenTiles[x + xx][y + yy]);
/* 1257 */         short rockHeight = Tiles.decodeHeight(rockTiles[x + xx][y + yy]);
/* 1258 */         if (tileHeight <= rockHeight)
/*      */         {
/* 1260 */           numberOfCornersAtRockHeight++;
/*      */         }
/*      */       } 
/*      */     } 
/* 1264 */     if (numberOfCornersAtRockHeight == 4) {
/*      */       
/* 1266 */       byte type = Tiles.decodeType(flattenTiles[x][y]);
/* 1267 */       if (!Terraforming.isRockTile(type) && !Terraforming.isImmutableTile(type))
/*      */       {
/* 1269 */         return true;
/*      */       }
/*      */     } 
/* 1272 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isCornerDone(int x, int y, int preferredHeight) {
/* 1277 */     if (Tiles.decodeHeight(flattenTiles[x][y]) == Tiles.decodeHeight(rockTiles[x][y]))
/*      */     {
/*      */       
/* 1280 */       return true;
/*      */     }
/* 1282 */     return (Tiles.decodeHeight(flattenTiles[x][y]) == preferredHeight);
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
/*      */   private static final boolean changeFlattenCorner(Creature performer, int x, int y, int maxDiff, int preferredHeight, boolean raise, Action act) {
/* 1300 */     int newTile = flattenTiles[x][y];
/*      */     
/* 1302 */     byte oldType = Tiles.decodeType(newTile);
/* 1303 */     byte newType = oldType;
/* 1304 */     short newHeight = Tiles.decodeHeight(newTile);
/* 1305 */     if (raise) {
/* 1306 */       newHeight = (short)Math.min(32767, newHeight + 1);
/*      */     } else {
/* 1308 */       newHeight = (short)Math.max(-32768, newHeight - 1);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1314 */     for (int a = 0; a >= -1; a--) {
/*      */       
/* 1316 */       for (int b = 0; b >= -1; b--) {
/*      */         
/* 1318 */         int modx = x + a;
/* 1319 */         int mody = y + b;
/* 1320 */         if (a == 0 && b == 0) {
/*      */           
/* 1322 */           newFHeight = newHeight;
/* 1323 */           newType = oldType;
/* 1324 */           newTile = flattenTiles[x][y];
/*      */         }
/*      */         else {
/*      */           
/* 1328 */           newFHeight = Tiles.decodeHeight(flattenTiles[modx][mody]);
/* 1329 */           newType = Tiles.decodeType(flattenTiles[modx][mody]);
/* 1330 */           newTile = flattenTiles[modx][mody];
/*      */         } 
/*      */ 
/*      */         
/* 1334 */         if (Terraforming.isImmutableOrRoadTile(newType)) {
/*      */           
/* 1336 */           if (a == 0 && b == 0 && !raise)
/*      */           {
/* 1338 */             if (immutableTiles[modx][mody]) {
/* 1339 */               logger.log(Level.WARNING, "Does this really change anything? Changing " + modx + "," + mody + " from " + 
/* 1340 */                   Tiles.decodeHeight(flattenTiles[modx][mody]) + " to " + newFHeight + " at  protected=" + immutableTiles[modx][mody] + " xy=" + x + "," + y + " ab=" + a + "," + b, new Exception());
/*      */             }
/*      */             
/* 1343 */             changedTiles[modx][mody] = true;
/* 1344 */             flattenTiles[modx][mody] = Tiles.encode(newFHeight, newType, Tiles.decodeData(newTile));
/*      */           }
/* 1346 */           else if (Tiles.isRoadType(newType))
/*      */           {
/* 1348 */             changedTiles[modx][mody] = true;
/* 1349 */             flattenTiles[modx][mody] = Tiles.encode(newFHeight, newType, Tiles.decodeData(newTile));
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1354 */           if (newType == Tiles.Tile.TILE_SAND.id || oldType == Tiles.Tile.TILE_SAND.id) {
/*      */             
/* 1356 */             if (newType != Tiles.Tile.TILE_DIRT.id) {
/* 1357 */               newType = Tiles.Tile.TILE_SAND.id;
/*      */             }
/*      */           } else {
/* 1360 */             newType = Tiles.Tile.TILE_DIRT.id;
/* 1361 */           }  if (oldType != newType) {
/* 1362 */             TileEvent.log(performer.getTileX(), performer.getTileY(), 0, performer.getWurmId(), act.getNumber());
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1367 */           flattenTiles[modx][mody] = Tiles.encode(newFHeight, newType, (byte)0);
/* 1368 */           changedTiles[modx][mody] = true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1373 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void checkUseDirt(int tilex, int tiley, int endX, int endY, Creature performer, Item source, int maxdiff, int preferredHeight, Action act, boolean quickLevel) {
/* 1380 */     int higherFlatten = 0;
/* 1381 */     int lowerFlatten = 0; int x;
/* 1382 */     for (x = 1; x <= endX; x++) {
/*      */       
/* 1384 */       for (int y = 1; y <= endY; y++) {
/*      */         
/* 1386 */         int diff = Tiles.decodeHeight(flattenTiles[x][y]) - preferredHeight;
/*      */         
/* 1388 */         if (diff >= 1) {
/* 1389 */           lowerFlatten++;
/* 1390 */         } else if (diff <= -1) {
/* 1391 */           higherFlatten++;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1395 */     raising = false;
/* 1396 */     if ((lowerFlatten >= 2 && act.getNumber() == 150) || (lowerFlatten >= 1 && act
/* 1397 */       .getNumber() == 533)) {
/*      */       
/* 1399 */       if (performer.getCarriedItem(26) != null || performer
/* 1400 */         .getCarriedItem(298) != null)
/*      */       {
/* 1402 */         preferredHeight++;
/* 1403 */         raising = true;
/*      */       }
/*      */     
/* 1406 */     } else if (higherFlatten > 0) {
/* 1407 */       raising = true;
/*      */     } 
/* 1409 */     if (raising) {
/*      */       
/* 1411 */       for (x = 1; x <= endX; x++) {
/*      */         
/* 1413 */         for (int y = 1; y <= endY; y++) {
/*      */           
/* 1415 */           int diff = preferredHeight - Tiles.decodeHeight(flattenTiles[x][y]);
/* 1416 */           if (diff >= 1)
/*      */           {
/*      */ 
/*      */             
/* 1420 */             if (mayRaiseCorner(tilex, tiley, flattenTiles[x][y], x, y, maxdiff))
/* 1421 */               useDirt(performer, x, y, maxdiff, preferredHeight, quickLevel, act); 
/*      */           }
/*      */         } 
/*      */       } 
/* 1425 */       if (needsDirt == 3 && act.getNumber() != 532) {
/*      */ 
/*      */         
/* 1428 */         int dirtX = -1;
/* 1429 */         int dirtY = -1;
/*      */         
/* 1431 */         for (int i = 1; i <= endX; i++) {
/*      */           
/* 1433 */           for (int y = 1; y <= endY; y++) {
/*      */ 
/*      */             
/* 1436 */             int diff = Tiles.decodeHeight(flattenTiles[i][y]) - preferredHeight + 1;
/* 1437 */             if (diff >= 1)
/*      */             {
/*      */               
/* 1440 */               if (mayLowerCorner(tilex, tiley, flattenTiles[i][y], rockTiles[i][y], i, y, maxdiff))
/*      */               {
/* 1442 */                 if (dirtX == -1) {
/*      */                   
/* 1444 */                   dirtX = i;
/* 1445 */                   dirtY = y;
/*      */                 }
/* 1447 */                 else if (Server.rand.nextInt(2) == 1) {
/*      */                   
/* 1449 */                   dirtX = i;
/* 1450 */                   dirtY = y;
/*      */                 } 
/*      */               }
/*      */             }
/*      */           } 
/*      */         } 
/* 1456 */         if (dirtX > -1) {
/*      */ 
/*      */           
/* 1459 */           getDirt(performer, source, dirtX, dirtY, maxdiff, preferredHeight, quickLevel, act);
/* 1460 */           if (needsDirt == 3) {
/* 1461 */             needsDirt = 0;
/*      */           }
/*      */         } 
/*      */       } 
/* 1465 */     } else if (act.getNumber() == 532) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1470 */       int dirtX = -1;
/* 1471 */       int dirtY = -1;
/*      */       
/* 1473 */       for (int i = 1; i <= endX; i++) {
/*      */         
/* 1475 */         for (int y = 1; y <= endY; y++) {
/*      */           
/* 1477 */           int diff = Tiles.decodeHeight(flattenTiles[i][y]) - preferredHeight;
/* 1478 */           if (diff >= 1)
/*      */           {
/*      */             
/* 1481 */             if (mayLowerCorner(tilex, tiley, flattenTiles[i][y], rockTiles[i][y], i, y, maxdiff))
/*      */             {
/* 1483 */               if (dirtX == -1) {
/*      */                 
/* 1485 */                 dirtX = i;
/* 1486 */                 dirtY = y;
/*      */               }
/* 1488 */               else if (Server.rand.nextInt(2) == 1) {
/*      */                 
/* 1490 */                 dirtX = i;
/* 1491 */                 dirtY = y;
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/* 1497 */       if (dirtX > -1) {
/* 1498 */         getDirt(performer, source, dirtX, dirtY, maxdiff, preferredHeight, quickLevel, act);
/*      */       }
/*      */     } else {
/*      */       
/* 1502 */       for (x = 1; x <= endX; x++) {
/*      */         
/* 1504 */         for (int y = 1; y <= endY; y++) {
/*      */           
/* 1506 */           int diff = Tiles.decodeHeight(flattenTiles[x][y]) - preferredHeight;
/* 1507 */           if (diff >= 1)
/*      */           {
/*      */             
/* 1510 */             if (mayLowerCorner(tilex, tiley, flattenTiles[x][y], rockTiles[x][y], x, y, maxdiff))
/*      */             {
/* 1512 */               getDirt(performer, source, x, y, maxdiff, preferredHeight, quickLevel, act);
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void useDirt(Creature performer, int x, int y, int maxDiff, int preferredHeight, boolean quickLevel, Action act) {
/* 1523 */     Item dirt = performer.getCarriedItem(26);
/* 1524 */     if (dirt == null)
/* 1525 */       dirt = performer.getCarriedItem(298); 
/* 1526 */     if (dirt != null) {
/*      */       
/* 1528 */       Items.destroyItem(dirt.getWurmId());
/*      */ 
/*      */       
/*      */       try {
/* 1532 */         Item dirtAfter = Items.getItem(dirt.getWurmId());
/*      */         
/* 1534 */         if (quickLevel) {
/*      */           
/* 1536 */           performer.getCommunicator().sendNormalServerMessage("You have a ghost " + dirt
/* 1537 */               .getActualName() + " in inventory.");
/*      */           return;
/*      */         } 
/* 1540 */         performer.getCommunicator().sendNormalServerMessage("One corner could not be raised by a " + dirt
/* 1541 */             .getActualName() + " you are carrying.");
/* 1542 */         needsDirt++;
/*      */         
/*      */         return;
/* 1545 */       } catch (NoSuchItemException e) {
/*      */ 
/*      */         
/* 1548 */         if (!changeFlattenCorner(performer, x, y, maxDiff, preferredHeight, true, act)) {
/* 1549 */           performer.getCommunicator()
/* 1550 */             .sendNormalServerMessage("You use a " + dirt.getActualName() + " in one corner.");
/*      */         }
/*      */       } 
/* 1553 */     } else if (quickLevel) {
/*      */       
/* 1555 */       changeFlattenCorner(performer, x, y, maxDiff, preferredHeight, true, act);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1562 */       needsDirt++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void getDirt(Creature performer, Item source, int x, int y, int maxDiff, int preferredHeight, boolean quickLevel, Action act) {
/*      */     try {
/* 1570 */       byte type = Tiles.decodeType(flattenTiles[x][y]);
/* 1571 */       String dirtType = (type == Tiles.Tile.TILE_SAND.id) ? "heap of sand" : "dirt pile";
/* 1572 */       Item ivehic = null;
/* 1573 */       if (performer.getInventory().getNumItemsNotCoins() < 100 && performer
/* 1574 */         .canCarry(ItemTemplateFactory.getInstance().getTemplate(26).getWeightGrams())) {
/*      */         
/*      */         try {
/*      */           
/* 1578 */           ivehic = Items.getItem(performer.getVehicle());
/*      */         }
/* 1580 */         catch (NoSuchItemException nsi) {
/*      */           
/* 1582 */           ivehic = null;
/*      */         } 
/*      */       }
/* 1585 */       if (source.isDredgingTool() && !source.isWand()) {
/*      */         
/* 1587 */         int dirtVol = 1000;
/* 1588 */         if (ivehic != null && ivehic.getFreeVolume() < dirtVol) {
/*      */           
/* 1590 */           if (source.getFreeVolume() < dirtVol) {
/*      */             
/* 1592 */             needsDirt--;
/* 1593 */             performer.getCommunicator().sendNormalServerMessage("The " + ivehic.getName() + " and the " + source
/* 1594 */                 .getName() + " are both full.");
/*      */             
/*      */             return;
/*      */           } 
/* 1598 */         } else if (ivehic == null && source.getFreeVolume() < dirtVol) {
/*      */           
/* 1600 */           needsDirt--;
/* 1601 */           performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " cannot fit anything more inside of it.");
/*      */           return;
/*      */         } 
/*      */       } 
/* 1605 */       if (performer.getInventory().getNumItemsNotCoins() < 100 && performer
/* 1606 */         .canCarry(ItemTemplateFactory.getInstance().getTemplate(26).getWeightGrams()))
/*      */       {
/* 1608 */         if (!changeFlattenCorner(performer, x, y, maxDiff, preferredHeight, false, act))
/*      */         {
/* 1610 */           int template = 26;
/* 1611 */           if (type == Tiles.Tile.TILE_SAND.id)
/* 1612 */             template = 298; 
/* 1613 */           if (!quickLevel)
/*      */           {
/* 1615 */             Item dirt = ItemFactory.createItem(template, Server.rand.nextFloat() * maxDiff / 3.0F, performer
/* 1616 */                 .getName());
/* 1617 */             if (source.isDredgingTool() && !source.isWand()) {
/*      */               
/* 1619 */               boolean addedToBoat = false;
/*      */               
/* 1621 */               if (performer.getVehicle() != -10L && ivehic != null)
/*      */               {
/* 1623 */                 if (ivehic.isBoat())
/*      */                 {
/* 1625 */                   if (ivehic.testInsertItem(dirt)) {
/*      */                     
/* 1627 */                     ivehic.insertItem(dirt);
/* 1628 */                     performer.getCommunicator().sendNormalServerMessage("You put the " + dirt.getName() + " in the " + ivehic
/* 1629 */                         .getName() + ".");
/* 1630 */                     addedToBoat = true;
/*      */                   } 
/*      */                 }
/*      */               }
/* 1634 */               if (!addedToBoat) {
/* 1635 */                 source.insertItem(dirt, true);
/*      */               }
/*      */             } else {
/* 1638 */               performer.getInventory().insertItem(dirt, true);
/* 1639 */             }  performer.getCommunicator().sendNormalServerMessage("You assemble some " + ((template == 26) ? "dirt" : "sand") + " from a corner.");
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 1649 */         needsDirt--;
/* 1650 */         performer.getCommunicator().sendNormalServerMessage("You are not strong enough to carry one more " + dirtType + ".");
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1655 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 1657 */       logger.log(Level.WARNING, performer.getName() + " No dirt template?", (Throwable)nst);
/*      */     }
/* 1659 */     catch (FailedException fe) {
/*      */       
/* 1661 */       logger.log(Level.WARNING, performer.getName() + " failed to create dirt?", (Throwable)fe);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isTileTooDeep(int tilex, int tiley, int totalx, int totaly, int numbCorners) {
/* 1667 */     int belowWater = 0;
/* 1668 */     for (int x = tilex; x < tilex + totalx; x++) {
/*      */       
/* 1670 */       for (int y = tiley; y < tiley + totaly; y++) {
/*      */         
/* 1672 */         short ht = Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y));
/* 1673 */         if (ht <= -7.0F)
/* 1674 */           belowWater++; 
/*      */       } 
/*      */     } 
/* 1677 */     return (belowWater == numbCorners);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Flattening.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
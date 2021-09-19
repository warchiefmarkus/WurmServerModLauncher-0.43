/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.bodys.Wounds;
/*      */ import com.wurmonline.server.creatures.Brand;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.creatures.MountAction;
/*      */ import com.wurmonline.server.creatures.ai.NoPathException;
/*      */ import com.wurmonline.server.creatures.ai.Path;
/*      */ import com.wurmonline.server.creatures.ai.PathFinder;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.items.Trade;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.questions.AskKingdomQuestion;
/*      */ import com.wurmonline.server.questions.PriestQuestion;
/*      */ import com.wurmonline.server.questions.QuestionTypes;
/*      */ import com.wurmonline.server.questions.RoyalChallenge;
/*      */ import com.wurmonline.server.questions.SetKingdomQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.Track;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.AttitudeConstants;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.io.IOException;
/*      */ import java.util.Set;
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
/*      */ public final class MethodsCreatures
/*      */   implements MiscConstants, QuestionTypes, ItemTypes, CounterTypes, ItemMaterials, SoundNames, VillageStatus, TimeConstants, AttitudeConstants
/*      */ {
/*      */   public static final String cvsversion = "$Id: MethodsCreatures.java,v 1.31 2007-04-19 23:05:18 root Exp $";
/*  110 */   private static final Logger logger = Logger.getLogger(MethodsCreatures.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final void teleportSet(Creature performer, Item source, int tilex, int tiley) {
/*  121 */     if (source.getTemplateId() == 174) {
/*      */       
/*  123 */       VolaTile tile = performer.getCurrentTile();
/*  124 */       if (tile != null)
/*      */       {
/*  126 */         if (tile.getStructure() != null) {
/*      */           
/*  128 */           performer.getCommunicator().sendNormalServerMessage("You utter arcane words and make some strange gestures but nothing happens.");
/*      */           
/*      */           return;
/*      */         } 
/*  132 */         tilex = tile.tilex;
/*  133 */         tiley = tile.tiley;
/*  134 */         performer
/*  135 */           .getCommunicator()
/*  136 */           .sendNormalServerMessage("You utter arcane words and make some strange gestures. You will teleport to where you are standing now using the wand.");
/*      */         
/*  138 */         Server.getInstance().broadCastAction(performer
/*  139 */             .getName() + " utters some arcane words and makes a few strange gestures with " + source
/*  140 */             .getNameWithGenus() + ".", performer, 5);
/*      */       }
/*      */     
/*  143 */     } else if (source.getTemplateId() == 525) {
/*      */       
/*  145 */       boolean ok = false;
/*  146 */       int tilenum = Server.caveMesh.getTile(tilex, tiley);
/*  147 */       if (performer.isOnPvPServer() && Zones.isWithinDuelRing(performer.getTileX(), performer.getTileY(), true) != null) {
/*      */         
/*  149 */         performer.getCommunicator().sendNormalServerMessage("The magic of the duelling ring interferes. You can not use the " + source
/*  150 */             .getName() + " here.");
/*      */         return;
/*      */       } 
/*  153 */       if (performer.isInPvPZone()) {
/*      */         
/*  155 */         performer.getCommunicator().sendNormalServerMessage("The magic of the pvp zone interferes. You can not use the " + source
/*  156 */             .getName() + " now.");
/*      */         return;
/*      */       } 
/*  159 */       if (Servers.localServer.PVPSERVER && EndGameItems.getEvilAltar() != null) {
/*      */         
/*  161 */         EndGameItem egi = EndGameItems.getEvilAltar();
/*      */         
/*  163 */         if (performer.isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi.getItem().getPosZ(), 50.0F)) {
/*      */           
/*  165 */           performer.getCommunicator().sendNormalServerMessage("The magic of this place interferes. You can not use the " + source
/*  166 */               .getName() + " here.");
/*      */           
/*      */           return;
/*      */         } 
/*  170 */       } else if (Servers.localServer.PVPSERVER && EndGameItems.getGoodAltar() != null) {
/*      */         
/*  172 */         EndGameItem egi = EndGameItems.getGoodAltar();
/*      */         
/*  174 */         if (performer.isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi.getItem().getPosZ(), 50.0F)) {
/*      */           
/*  176 */           performer.getCommunicator().sendNormalServerMessage("The magic of this place interferes. You can not use the " + source
/*  177 */               .getName() + " here.");
/*      */           return;
/*      */         } 
/*      */       } 
/*  181 */       if (Tiles.isOreCave(Tiles.decodeType(tilenum)))
/*      */       {
/*  183 */         if (!performer.isOnSurface()) {
/*      */           
/*  185 */           VolaTile tile = performer.getCurrentTile();
/*  186 */           if (tile != null)
/*      */           {
/*  188 */             if (tile.getStructure() == null)
/*      */             {
/*  190 */               if (Math.abs(tilex - tile.tilex) <= 1 || Math.abs(tiley - tile.tiley) <= 1) {
/*      */                 
/*  192 */                 tilex = tile.tilex;
/*  193 */                 tiley = tile.tiley;
/*  194 */                 ok = true;
/*  195 */                 performer
/*  196 */                   .getCommunicator()
/*  197 */                   .sendNormalServerMessage("You knock the " + source
/*      */                     
/*  199 */                     .getName() + " in the cave wall a few times. You will teleport to where you are standing now using the stone.");
/*      */                 
/*  201 */                 Server.getInstance().broadCastAction(performer
/*  202 */                     .getName() + " knocks with " + source.getNameWithGenus() + " in the cave wall a few times.", performer, 5);
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/*  209 */       if (!ok) {
/*      */         
/*  211 */         performer.getCommunicator().sendNormalServerMessage("You knock the " + source
/*  212 */             .getName() + " in the cave wall but nothing happens.");
/*  213 */         Server.getInstance().broadCastAction(performer
/*  214 */             .getName() + " knocks with " + source.getNameWithGenus() + " a few times.", performer, 5);
/*      */         
/*      */         return;
/*      */       } 
/*  218 */     } else if (source.getTemplateId() == 524) {
/*      */       
/*  220 */       boolean ok = false;
/*  221 */       if (performer.isOnPvPServer() && Zones.isWithinDuelRing(performer.getTileX(), performer.getTileY(), true) != null) {
/*      */         
/*  223 */         performer.getCommunicator().sendNormalServerMessage("There magic of the duelling ring interferes. You can not use the " + source
/*  224 */             .getName() + " here.");
/*      */         return;
/*      */       } 
/*  227 */       if (performer.isInPvPZone()) {
/*      */         
/*  229 */         performer.getCommunicator().sendNormalServerMessage("The magic of the pvp zone interferes. You can not use the " + source
/*  230 */             .getName() + " now.");
/*      */         return;
/*      */       } 
/*  233 */       if (Servers.localServer.PVPSERVER && EndGameItems.getEvilAltar() != null) {
/*      */         
/*  235 */         EndGameItem egi = EndGameItems.getEvilAltar();
/*      */         
/*  237 */         if (performer.isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi.getItem().getPosZ(), 50.0F)) {
/*      */           
/*  239 */           performer.getCommunicator().sendNormalServerMessage("There magic of this place interferes. You can not use the " + source
/*  240 */               .getName() + " here.");
/*      */           
/*      */           return;
/*      */         } 
/*  244 */       } else if (Servers.localServer.PVPSERVER && EndGameItems.getGoodAltar() != null) {
/*      */         
/*  246 */         EndGameItem egi = EndGameItems.getGoodAltar();
/*      */         
/*  248 */         if (performer.isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi.getItem().getPosZ(), 50.0F)) {
/*      */           
/*  250 */           performer.getCommunicator().sendNormalServerMessage("There magic of this place interferes. You can not use the " + source
/*  251 */               .getName() + " here.");
/*      */           return;
/*      */         } 
/*      */       } 
/*  255 */       if (performer.isOnSurface()) {
/*      */         
/*  257 */         VolaTile tile = performer.getCurrentTile();
/*  258 */         if (tile != null)
/*      */         {
/*  260 */           if (tile.getStructure() == null)
/*      */           {
/*  262 */             if (Math.abs(tilex - tile.tilex) <= 1 || Math.abs(tiley - tile.tiley) <= 1) {
/*      */               
/*  264 */               int tilenum = Server.surfaceMesh.getTile(tilex, tiley);
/*  265 */               if (Tiles.isTree(Tiles.decodeType(tilenum))) {
/*      */                 
/*  267 */                 tilex = tile.tilex;
/*  268 */                 tiley = tile.tiley;
/*  269 */                 ok = true;
/*  270 */                 performer
/*  271 */                   .getCommunicator()
/*  272 */                   .sendNormalServerMessage("You tap the " + source
/*      */                     
/*  274 */                     .getName() + " at the tree a few times. You will teleport to where you are standing now using the twig.");
/*      */                 
/*  276 */                 Server.getInstance().broadCastAction(performer
/*  277 */                     .getName() + " taps " + source.getNameWithGenus() + " at a tree few times.", performer, 5);
/*      */               } 
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */       
/*  284 */       if (!ok) {
/*      */         
/*  286 */         performer.getCommunicator().sendNormalServerMessage("You tap the " + source
/*  287 */             .getName() + " a few times but nothing happens.");
/*  288 */         Server.getInstance().broadCastAction(performer
/*  289 */             .getName() + " taps " + source.getNameWithGenus() + " a few times.", performer, 5);
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } else {
/*  295 */       performer
/*  296 */         .getCommunicator()
/*  297 */         .sendNormalServerMessage("You utter arcane words and make some strange gestures. You will teleport to where you were pointing with the wand.");
/*      */       
/*  299 */       Server.getInstance().broadCastAction(performer
/*  300 */           .getName() + " utters some arcane words and makes a few strange gestures with " + source
/*  301 */           .getNameWithGenus() + ".", performer, 5);
/*      */     } 
/*  303 */     performer.setTeleportLayer(performer.getLayer());
/*  304 */     source.setData(tilex, tiley);
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
/*      */   static final void teleportCreature(Creature performer, Item source) {
/*  330 */     if (source.getTemplateId() == 176 || source.getTemplateId() == 315 || source
/*  331 */       .getTemplateId() == 174 || source.getTemplateId() == 1027 || source.getTemplateId() == 525 || source
/*  332 */       .getTemplateId() == 524)
/*      */     {
/*  334 */       if (performer.getVisionArea() != null && performer.getVisionArea().isInitialized()) {
/*      */         
/*  336 */         int tx = source.getData1();
/*  337 */         int ty = source.getData2();
/*  338 */         if (tx >= 0 && ty >= 0 && tx < 1 << Constants.meshSize && ty < 1 << Constants.meshSize) {
/*      */           
/*  340 */           long barid = -10L;
/*  341 */           if (performer.getPower() <= 0) {
/*      */             
/*  343 */             Item[] inventoryItems = performer.getInventory().getAllItems(true);
/*  344 */             for (Item lInventoryItem : inventoryItems) {
/*      */               
/*  346 */               if (lInventoryItem.isArtifact()) {
/*      */                 
/*  348 */                 performer.getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/*  349 */                     .getName() + " hums and disturbs the weave. You can not use the " + source
/*  350 */                     .getName() + " now.");
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*  355 */             Item[] bodyItems = performer.getBody().getBodyItem().getAllItems(true);
/*  356 */             for (Item lInventoryItem : bodyItems) {
/*      */               
/*  358 */               if (lInventoryItem.isArtifact()) {
/*      */                 
/*  360 */                 performer.getCommunicator().sendNormalServerMessage("The " + lInventoryItem
/*  361 */                     .getName() + " hums and disturbs the weave. You can not use the " + source
/*  362 */                     .getName() + " now.");
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  369 */           int layer = performer.getTeleportLayer();
/*  370 */           int floorLevel = performer.getTeleportFloorLevel();
/*  371 */           if (((source.getTemplateId() == 176 || source.getTemplateId() == 315) && performer
/*  372 */             .getPower() >= 2) || ((source
/*  373 */             .getTemplateId() == 174 || source.getTemplateId() == 1027) && performer.getPower() >= 1)) {
/*      */             
/*  375 */             barid = 0L;
/*  376 */           } else if (source.getTemplateId() == 525 || source
/*  377 */             .getTemplateId() == 524) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  383 */             if (performer.getEnemyPresense() > 0 || performer.isFighting()) {
/*      */               
/*  385 */               performer.getCommunicator().sendNormalServerMessage("There is a blocking enemy presence nearby. You can not use the " + source
/*  386 */                   .getName() + " now.");
/*      */               
/*      */               return;
/*      */             } 
/*  390 */             if (performer.isOnPvPServer() && 
/*  391 */               Zones.isWithinDuelRing(performer.getTileX(), performer.getTileY(), true) != null) {
/*      */               
/*  393 */               performer.getCommunicator().sendNormalServerMessage("The magic of the duelling ring interferes. You can not use the " + source
/*  394 */                   .getName() + " now.");
/*      */               
/*      */               return;
/*      */             } 
/*  398 */             if (performer.isInPvPZone()) {
/*      */               
/*  400 */               performer.getCommunicator().sendNormalServerMessage("The magic of the pvp zone interferes. You can not use the " + source
/*  401 */                   .getName() + " now.");
/*      */               return;
/*      */             } 
/*  404 */             if (Servers.localServer.PVPSERVER && EndGameItems.getEvilAltar() != null) {
/*      */               
/*  406 */               EndGameItem egi = EndGameItems.getEvilAltar();
/*      */               
/*  408 */               if (performer.isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi.getItem()
/*  409 */                   .getPosZ(), 50.0F)) {
/*      */                 
/*  411 */                 performer.getCommunicator().sendNormalServerMessage("There magic of this place interferes. You can not use the " + source
/*  412 */                     .getName() + " now.");
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*  417 */             } else if (Servers.localServer.PVPSERVER && EndGameItems.getGoodAltar() != null) {
/*      */               
/*  419 */               EndGameItem egi = EndGameItems.getGoodAltar();
/*      */               
/*  421 */               if (performer.isWithinDistanceTo(egi.getItem().getPosX(), egi.getItem().getPosY(), egi.getItem()
/*  422 */                   .getPosZ(), 50.0F)) {
/*      */                 
/*  424 */                 performer.getCommunicator().sendNormalServerMessage("There magic of this place interferes. You can not use the " + source
/*  425 */                     .getName() + " now.");
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*  430 */             barid = 0L;
/*  431 */             if (source.getTemplateId() == 525) {
/*  432 */               layer = -1;
/*      */             } else {
/*  434 */               layer = 0;
/*      */             } 
/*      */           } else {
/*      */             
/*  438 */             Item[] inventoryItems = performer.getInventory().getAllItems(false);
/*  439 */             for (Item lInventoryItem : inventoryItems) {
/*      */               
/*  441 */               if (lInventoryItem.getTemplateId() == 45)
/*      */               {
/*  443 */                 if (lInventoryItem.getWeightGrams() >= lInventoryItem.getTemplate().getWeightGrams()) {
/*      */                   
/*  445 */                   barid = lInventoryItem.getWurmId();
/*      */                   break;
/*      */                 } 
/*      */               }
/*      */             } 
/*  450 */             if (barid == -10L) {
/*      */               
/*  452 */               Item[] bodyItems = performer.getBody().getAllItems();
/*  453 */               for (Item lBodyItem : bodyItems) {
/*      */                 
/*  455 */                 if (lBodyItem.getTemplateId() == 45)
/*      */                 {
/*  457 */                   if (lBodyItem.getWeightGrams() >= lBodyItem.getTemplate().getWeightGrams()) {
/*      */                     
/*  459 */                     barid = lBodyItem.getWurmId();
/*      */                     break;
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*  466 */           if (barid == -10L) {
/*  467 */             performer.getCommunicator().sendNormalServerMessage("A standard sized silver lump will be consumed when teleporting. You need to acquire one.");
/*      */           }
/*      */           else {
/*      */             
/*  471 */             boolean port = false;
/*  472 */             if (source.getTemplateId() != 176 && source.getTemplateId() != 315 && source.getTemplateId() != 1027) {
/*      */               
/*  474 */               if (mayTelePortToTile(performer, tx, ty, (layer == 0))) {
/*      */                 
/*  476 */                 if (barid > 0L)
/*  477 */                   Items.destroyItem(barid); 
/*  478 */                 port = true;
/*      */               } else {
/*      */                 
/*  481 */                 performer.getCommunicator().sendNormalServerMessage("You can't teleport there. Please choose another destination.");
/*      */               } 
/*      */             } else {
/*      */               
/*  485 */               port = true;
/*  486 */             }  if (port) {
/*      */               
/*  488 */               if (performer.getPower() > 0) {
/*      */                 
/*  490 */                 if (performer.getLogger() != null) {
/*  491 */                   performer.getLogger().log(Level.INFO, "Teleporting to " + tx + ", " + ty);
/*      */                 
/*      */                 }
/*      */               }
/*  495 */               else if (logger.isLoggable(Level.FINE)) {
/*      */                 
/*  497 */                 logger.fine(performer + " using a source item, " + source + " is teleporting to " + tx + ", " + ty);
/*      */               } 
/*      */ 
/*      */               
/*  501 */               performer.setTeleportPoints((short)tx, (short)ty, layer, floorLevel);
/*  502 */               if (performer.startTeleporting()) {
/*      */                 
/*  504 */                 if (source.getTemplateId() == 525 || source
/*  505 */                   .getTemplateId() == 524) {
/*      */                   
/*  507 */                   Items.destroyItem(source.getWurmId());
/*  508 */                   Server.getInstance().broadCastAction(performer
/*  509 */                       .getName() + " uses a " + source.getName() + ".", performer, 5);
/*      */                 } 
/*  511 */                 performer.getCommunicator().sendNormalServerMessage("You feel a slight tingle in your spine.");
/*  512 */                 performer.getCommunicator().sendTeleport(false);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/*  518 */           performer.getCommunicator().sendNormalServerMessage("You need to set the teleportation endpoint for the wand to reasonable values first.");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  523 */         performer.getCommunicator().sendNormalServerMessage("You cannot teleport right now.");
/*  524 */         logger.info(performer.getName() + " could not teleport due to VisionArea " + performer.getVisionArea());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected static boolean mayTelePortToTile(Creature creature, int tilex, int tiley, boolean surfaced) {
/*  531 */     VolaTile tile = null;
/*  532 */     tile = Zones.getTileOrNull(tilex, tiley, surfaced);
/*  533 */     if (tile != null) {
/*      */       
/*  535 */       Structure struct = tile.getStructure();
/*  536 */       if (struct == null)
/*  537 */         return true; 
/*  538 */       if (struct.isFinished())
/*      */       {
/*  540 */         if (struct.mayPass(creature)) {
/*  541 */           return true;
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  551 */       return true;
/*  552 */     }  return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static void initiateTrade(Creature performer, Creature opponent) {
/*  557 */     Trade trade = new Trade(performer, opponent);
/*  558 */     performer.setTrade(trade);
/*  559 */     opponent.setTrade(trade);
/*  560 */     opponent.getCommunicator().sendStartTrading(performer);
/*  561 */     performer.getCommunicator().sendStartTrading(opponent);
/*  562 */     if (!opponent.isPlayer()) {
/*  563 */       opponent.addItemsToTrade();
/*      */     }
/*      */   }
/*      */   
/*      */   static String getDirectionStringFor(float rot, int dir) {
/*  568 */     int turnDir = 0;
/*  569 */     float degree = 22.5F;
/*  570 */     float lRot = Creature.normalizeAngle(rot);
/*  571 */     if (lRot >= 337.5D || lRot < 22.5F) {
/*  572 */       turnDir = 0;
/*      */     } else {
/*      */       
/*  575 */       for (int x = 0; x < 8; x++) {
/*      */         
/*  577 */         if (lRot < 22.5F + (45 * x)) {
/*      */           
/*  579 */           turnDir = x;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  585 */     String direction = "straight ahead";
/*  586 */     if (dir == turnDir + 1 || dir == turnDir - 7) {
/*  587 */       direction = "ahead to the right";
/*  588 */     } else if (dir == turnDir + 2 || dir == turnDir - 6) {
/*  589 */       direction = "right";
/*  590 */     } else if (dir == turnDir + 3 || dir == turnDir - 5) {
/*  591 */       direction = "back to the right";
/*  592 */     } else if (dir == turnDir + 4 || dir == turnDir - 4) {
/*  593 */       direction = "backwards";
/*  594 */     } else if (dir == turnDir + 5 || dir == turnDir - 3) {
/*  595 */       direction = "back to the left";
/*  596 */     } else if (dir == turnDir + 6 || dir == turnDir - 2) {
/*  597 */       direction = "to the left";
/*  598 */     } else if (dir == turnDir + 7 || dir == turnDir - 1) {
/*  599 */       direction = "ahead to the left";
/*  600 */     }  return direction;
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
/*      */   public static String getLocationStringFor(float rot, int dir, String performername) {
/*  614 */     int turnDir = 0;
/*  615 */     float degree = 22.5F;
/*  616 */     float lRot = Creature.normalizeAngle(rot);
/*  617 */     if (lRot >= 337.5D || lRot < 22.5F) {
/*  618 */       turnDir = 0;
/*      */     } else {
/*  620 */       for (int x = 0; x < 8; x++) {
/*      */         
/*  622 */         if (lRot < 22.5F + (45 * x)) {
/*      */           
/*  624 */           turnDir = x;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  629 */     String direction = "in front of " + performername;
/*  630 */     if (dir == turnDir + 1 || dir == turnDir - 7) {
/*  631 */       direction = "ahead of " + performername + " to the right";
/*  632 */     } else if (dir == turnDir + 2 || dir == turnDir - 6) {
/*  633 */       direction = "to the right of " + performername;
/*  634 */     } else if (dir == turnDir + 3 || dir == turnDir - 5) {
/*  635 */       direction = "behind " + performername + " to the right";
/*  636 */     } else if (dir == turnDir + 4 || dir == turnDir - 4) {
/*  637 */       direction = "behind " + performername;
/*  638 */     } else if (dir == turnDir + 5 || dir == turnDir - 3) {
/*  639 */       direction = "behind " + performername + " to the left";
/*  640 */     } else if (dir == turnDir + 6 || dir == turnDir - 2) {
/*  641 */       direction = "to the left of " + performername;
/*  642 */     } else if (dir == turnDir + 7 || dir == turnDir - 1) {
/*  643 */       direction = "ahead of " + performername + " to the left";
/*  644 */     }  return direction;
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
/*      */   static String getElapsedTimeString(int hours) {
/*      */     String timeString;
/*  662 */     if (hours > 48) {
/*  663 */       timeString = "more than two days ago.";
/*  664 */     } else if (hours > 24) {
/*  665 */       timeString = "more than a day ago.";
/*  666 */     } else if (hours > 12) {
/*  667 */       timeString = "more than half a day ago.";
/*  668 */     } else if (hours > 6) {
/*  669 */       timeString = "more than six hours ago.";
/*  670 */     } else if (hours > 3) {
/*  671 */       timeString = "more than three hours ago.";
/*  672 */     } else if (hours > 2) {
/*  673 */       timeString = "more than two hours ago.";
/*  674 */     } else if (hours > 1) {
/*  675 */       timeString = "more than an hour ago.";
/*      */     } else {
/*  677 */       timeString = "less than an hour ago.";
/*  678 */     }  return timeString;
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
/*      */   public static int getDir(Creature performer, int targetX, int targetY) {
/*  692 */     double newrot = Math.atan2(((targetY << 2) + 2 - (int)performer.getStatus().getPositionY()), ((targetX << 2) + 2 - 
/*  693 */         (int)performer.getStatus().getPositionX()));
/*  694 */     float attAngle = (float)(newrot * 57.29577951308232D) + 90.0F;
/*  695 */     attAngle = Creature.normalizeAngle(attAngle);
/*      */ 
/*      */     
/*  698 */     float degree = 22.5F;
/*  699 */     if (attAngle >= 337.5D || attAngle < 22.5F) {
/*  700 */       return 0;
/*      */     }
/*  702 */     for (int x = 0; x < 8; x++) {
/*      */       
/*  704 */       if (attAngle < 22.5F + (45 * x))
/*      */       {
/*  706 */         return x;
/*      */       }
/*      */     } 
/*  709 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean track(Creature performer, int tilex, int tiley, int tile, float counter) {
/*  714 */     boolean done = false;
/*  715 */     int trackTilex = (int)performer.getStatus().getPositionX() >> 2;
/*  716 */     int trackTiley = (int)performer.getStatus().getPositionY() >> 2;
/*      */     
/*  718 */     tile = Server.surfaceMesh.getTile(trackTilex, trackTiley);
/*  719 */     if (!performer.isOnSurface())
/*  720 */       tile = Server.caveMesh.getTile(trackTilex, trackTiley); 
/*  721 */     if (Tiles.decodeHeight(tile) > 0) {
/*      */ 
/*      */       
/*      */       try {
/*  725 */         Zone trackZone = Zones.getZone(trackTilex, trackTiley, performer.isOnSurface());
/*      */         
/*  727 */         int ownTracks = 0;
/*  728 */         Skill tracking = null;
/*  729 */         Skills skills = performer.getSkills();
/*      */         
/*      */         try {
/*  732 */           tracking = skills.getSkill(10018);
/*      */         }
/*  734 */         catch (Exception ex) {
/*      */           
/*  736 */           tracking = skills.learn(10018, 1.0F);
/*      */         } 
/*  738 */         int time = Actions.getStandardActionTime(performer, tracking, null, 0.0D);
/*      */         
/*  740 */         if (counter == 1.0F) {
/*      */           
/*  742 */           performer.getCommunicator().sendNormalServerMessage("You start to check the ground for tracks.");
/*  743 */           Server.getInstance().broadCastAction(performer.getName() + " starts to check the ground for tracks.", performer, 5);
/*      */           
/*  745 */           if (performer.getPower() == 0) {
/*      */             
/*  747 */             performer.sendActionControl(Actions.actionEntrys[109].getVerbString(), true, time);
/*      */             
/*  749 */             performer.getStatus().modifyStamina(-500.0F);
/*      */           } 
/*      */         } 
/*      */         
/*  753 */         if (counter * 10.0F > time || performer.getPower() > 0) {
/*      */           
/*  755 */           tracking.skillCheck(tracking.getKnowledge(0.0D), 0.0D, false, counter / 2.0F);
/*  756 */           done = true;
/*  757 */           Track[] tracks = trackZone.getTracksFor(trackTilex, trackTiley, 3);
/*  758 */           if (tracks.length > 0) {
/*      */             
/*  760 */             int max = Math.max(30, (int)tracking.getKnowledge(0.0D));
/*  761 */             int min = Math.min((int)tracking.getKnowledge(0.0D), max);
/*  762 */             if (performer.getPower() > 0)
/*  763 */               min = 100; 
/*  764 */             min = Math.min(min, tracks.length);
/*  765 */             for (int x = 0; x < min; x++) {
/*      */               
/*  767 */               if (!tracks[x].getCreatureName().equals(performer.getName())) {
/*      */                 
/*  769 */                 int dir = tracks[x].getDirection();
/*  770 */                 float rot = performer.getStatus().getRotation();
/*  771 */                 String direction = getDirectionStringFor(rot, dir);
/*  772 */                 long diff = System.currentTimeMillis() - tracks[x].getTime();
/*  773 */                 int gameSeconds = (int)(diff / 125L);
/*  774 */                 int hours = gameSeconds / 3600;
/*  775 */                 String timeString = getElapsedTimeString(hours);
/*  776 */                 if (performer.getPower() > 0) {
/*  777 */                   performer.getCommunicator().sendNormalServerMessage(tracks[x]
/*  778 */                       .getCreatureName() + " (" + tracks[x].getId() + " [" + direction + "] " + hours + " hrs");
/*      */                 } else {
/*      */                   
/*  781 */                   performer.getCommunicator().sendNormalServerMessage("You find tracks of " + tracks[x]
/*  782 */                       .getCreatureName() + " leading " + direction + " done " + timeString);
/*      */                 } 
/*      */               } else {
/*      */                 
/*  786 */                 ownTracks++;
/*      */               } 
/*  788 */             }  if (ownTracks == tracks.length) {
/*  789 */               performer.getCommunicator().sendNormalServerMessage("You find only your own tracks.");
/*      */             }
/*      */           } else {
/*  792 */             performer.getCommunicator().sendNormalServerMessage("You find no tracks.");
/*      */           } 
/*      */         } 
/*  795 */       } catch (NoSuchZoneException nsz) {
/*      */         
/*  797 */         done = true;
/*  798 */         performer.getCommunicator().sendNormalServerMessage("You find no tracks.");
/*  799 */         logger.log(Level.WARNING, "No zone for tiles " + trackTilex + ", " + trackTiley);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  804 */       done = true;
/*  805 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to track.");
/*      */     } 
/*  807 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean absorb(Creature performer, int tilex, int tiley, int tile, float counter, Action act) {
/*  813 */     boolean done = false;
/*      */     
/*  815 */     tile = Server.surfaceMesh.getTile(tilex, tiley);
/*  816 */     byte type = Tiles.decodeType(tile);
/*  817 */     byte data = Tiles.decodeData(tile);
/*  818 */     Tiles.Tile theTile = Tiles.getTile(type);
/*      */     
/*  820 */     if (!theTile.isMycelium()) {
/*      */       
/*  822 */       performer.getCommunicator().sendNormalServerMessage("You can not absorb that.", (byte)3);
/*  823 */       return true;
/*      */     } 
/*  825 */     Wounds wounds = performer.getBody().getWounds();
/*  826 */     if (performer.getDisease() == 0 && (wounds == null || (wounds.getWounds()).length == 0)) {
/*      */       
/*  828 */       performer.getCommunicator().sendNormalServerMessage("You focus on the mycelium, but nothing happens.", (byte)3);
/*  829 */       return true;
/*      */     } 
/*  831 */     if (performer.isPlayer() && ((Player)performer).myceliumHealCounter > 1) {
/*      */       
/*  833 */       performer.getCommunicator().sendNormalServerMessage("You are still full of energy, and must wait another " + ((Player)performer).myceliumHealCounter + " seconds to avoid dangerous effects.");
/*      */ 
/*      */       
/*  836 */       return true;
/*      */     } 
/*  838 */     if (Tiles.decodeHeight(tile) > 0) {
/*      */       
/*  840 */       int time = 100;
/*  841 */       if (counter == 1.0F) {
/*      */         
/*  843 */         performer.getCommunicator().sendNormalServerMessage("You focus on the mycelium. Your body starts to tremble.");
/*  844 */         Server.getInstance().broadCastAction(performer.getName() + " starts to tremble feverishly.", performer, 5);
/*  845 */         performer.sendActionControl(Actions.actionEntrys[347].getVerbString(), true, 100);
/*      */         
/*  847 */         performer.getStatus().modifyStamina(-500.0F);
/*      */       } 
/*  849 */       if (act.currentSecond() == 5)
/*      */       {
/*  851 */         performer.getCommunicator().sendNormalServerMessage("A soothing warmth permeates your body.");
/*  852 */         Server.getInstance().broadCastAction("Roots stretch out from the mycelium, entangling " + performer
/*  853 */             .getName() + ".", performer, 5);
/*      */       }
/*  855 */       else if (counter > 10.0F)
/*      */       {
/*  857 */         done = true;
/*  858 */         if (wounds != null && (wounds.getWounds()).length > 0) {
/*      */           
/*  860 */           Wound[] w = wounds.getWounds();
/*  861 */           w[0].modifySeverity(-5000);
/*      */         } 
/*  863 */         if (performer.getDisease() > 0)
/*      */         {
/*  865 */           if (Server.rand.nextInt(20) == 0)
/*  866 */             performer.setDisease((byte)0); 
/*      */         }
/*  868 */         performer.getCommunicator().sendNormalServerMessage("You absorb the mycelium.");
/*  869 */         Server.getInstance().broadCastAction("The mycelium disappears into the body of " + performer.getName() + ".", performer, 5);
/*      */         
/*  871 */         if (performer.isPlayer()) {
/*  872 */           ((Player)performer).myceliumHealCounter = 30;
/*      */         }
/*  874 */         if (type == Tiles.Tile.TILE_MYCELIUM.id) {
/*      */           
/*  876 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte)0);
/*      */         }
/*  878 */         else if (type == Tiles.Tile.TILE_MYCELIUM_LAWN.id) {
/*      */           
/*  880 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_LAWN.id, (byte)0);
/*      */         }
/*  882 */         else if (theTile.isMyceliumTree()) {
/*      */           
/*  884 */           byte newType = theTile.getTreeType(data).asNormalTree();
/*  885 */           byte newData = (byte)((data & 0xFC) + 1);
/*      */           
/*  887 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), newType, newData);
/*      */         }
/*  889 */         else if (theTile.isMyceliumBush()) {
/*      */           
/*  891 */           byte newType = theTile.getBushType(data).asNormalBush();
/*  892 */           byte newData = (byte)((data & 0xFC) + 1);
/*      */           
/*  894 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), newType, newData);
/*      */         } 
/*  896 */         Players.getInstance().sendChangedTile(tilex, tiley, true, true);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  901 */       done = true;
/*  902 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep. You can not absorb that.", (byte)3);
/*      */     } 
/*  904 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void destroyCreature(Creature target) {
/*  909 */     if (target != null) {
/*      */       
/*  911 */       Creatures.getInstance().setCreatureDead(target);
/*  912 */       Players.getInstance().setCreatureDead(target);
/*  913 */       target.destroy();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean firstAid(Creature performer, Item bandaid, Wound wound, float counter, Action act) {
/*  920 */     boolean done = false;
/*      */     
/*  922 */     String bandage = "bandage";
/*  923 */     if (performer.isPlayer() && ((Player)performer).stuckCounter > 0) {
/*      */       
/*  925 */       performer.getCommunicator().sendNormalServerMessage("You are a bit dizzy. Try again in " + ((Player)performer).stuckCounter + " second/s.", (byte)3);
/*      */       
/*  927 */       return true;
/*      */     } 
/*  929 */     if (bandaid.isHollow() && !bandaid.isEmpty(true)) {
/*      */       
/*  931 */       performer.getCommunicator().sendNormalServerMessage("Empty the " + bandaid.getName() + " first.", (byte)3);
/*  932 */       return true;
/*      */     } 
/*  934 */     if (bandaid.isHealingSalve() && (wound.isInternal() || wound.isBruise() || wound.isPoison())) {
/*      */       
/*  936 */       if (wound.isBandaged()) {
/*      */         
/*  938 */         performer.getCommunicator().sendNormalServerMessage("The wound has already been smeared.", (byte)3);
/*  939 */         done = true;
/*      */       } 
/*  941 */       int nums = bandaid.getRarity() + act.getRarity() + bandaid.getWeightGrams() / 10;
/*  942 */       if (nums < wound.getNumBandagesNeeded())
/*      */       {
/*  944 */         if (performer instanceof Player) {
/*      */           
/*  946 */           performer.getCommunicator().sendNormalServerMessage("The " + bandaid
/*  947 */               .getName() + " will not cover the wound.", (byte)3);
/*  948 */           done = true;
/*      */         } 
/*      */       }
/*      */       
/*  952 */       bandage = "smear";
/*      */     }
/*  954 */     else if (!wound.isInternal()) {
/*      */       
/*  956 */       int nums = bandaid.getRarity() + act.getRarity() + bandaid.getWeightGrams() / 100;
/*  957 */       if (nums < wound.getNumBandagesNeeded())
/*      */       {
/*  959 */         if (performer instanceof Player) {
/*      */           
/*  961 */           performer.getCommunicator().sendNormalServerMessage("The " + bandaid
/*  962 */               .getName() + " does not cover the wound.", (byte)3);
/*  963 */           done = true;
/*      */         } 
/*      */       }
/*  966 */       if (wound.isBandaged())
/*      */       {
/*  968 */         performer.getCommunicator().sendNormalServerMessage("The wound is already bandaged.", (byte)3);
/*  969 */         done = true;
/*      */       }
/*  971 */       else if (bandaid.getMaterial() != 17)
/*      */       {
/*  973 */         performer.getCommunicator().sendNormalServerMessage("You can't use " + bandaid
/*  974 */             .getNameWithGenus() + " to bind the wound!", (byte)3);
/*  975 */         done = true;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  980 */       performer.getCommunicator().sendNormalServerMessage("You can't use " + bandaid
/*  981 */           .getNameWithGenus() + " to bind the wound!", (byte)3);
/*  982 */       done = true;
/*      */     } 
/*  984 */     if (wound.getCreature() == null) {
/*  985 */       done = true;
/*  986 */     } else if (!done) {
/*      */       
/*  988 */       if (wound.getCreature().isGhost()) {
/*      */         
/*  990 */         performer.getCommunicator().sendNormalServerMessage("You can't treat that wound. It is not physical.", (byte)3);
/*  991 */         return true;
/*      */       } 
/*  993 */       Skill firstaid = null;
/*  994 */       int time = 1000;
/*  995 */       Skills skills = performer.getSkills();
/*  996 */       double power = 0.0D;
/*      */       
/*      */       try {
/*  999 */         firstaid = skills.getSkill(10056);
/*      */       }
/* 1001 */       catch (NoSuchSkillException nss) {
/*      */         
/* 1003 */         firstaid = skills.learn(10056, 1.0F);
/*      */       } 
/* 1005 */       if (counter == 1.0F) {
/*      */ 
/*      */         
/* 1008 */         time = 50;
/* 1009 */         if (wound.getSeverity() > 3275.0F)
/*      */         {
/* 1011 */           if (wound.getSeverity() < 9825.0F) {
/* 1012 */             time = 100;
/* 1013 */           } else if (wound.getSeverity() < 19650.0F) {
/* 1014 */             time = 150;
/* 1015 */           } else if (wound.getSeverity() < 29475.0F) {
/* 1016 */             time = 300;
/*      */           } else {
/* 1018 */             time = 450;
/*      */           } 
/*      */         }
/* 1021 */         act.setTimeLeft(time);
/* 1022 */         String targetName = wound.getCreature().getNameWithGenus();
/* 1023 */         if (wound.getCreature().isPlayer())
/* 1024 */           targetName = wound.getCreature().getName(); 
/* 1025 */         performer.sendActionControl(Actions.actionEntrys[196].getVerbString(), true, time);
/*      */ 
/*      */         
/* 1028 */         performer.getCommunicator().sendNormalServerMessage("You start to " + bandage + " the wound.");
/* 1029 */         if (wound.getCreature() == performer) {
/* 1030 */           Server.getInstance().broadCastAction(performer
/* 1031 */               .getName() + " starts tending " + performer.getHisHerItsString() + " wounds.", performer, 5);
/*      */         }
/*      */         else {
/*      */           
/* 1035 */           wound.getCreature().getCommunicator()
/* 1036 */             .sendNormalServerMessage(performer.getName() + " starts tending your wounds.");
/* 1037 */           Server.getInstance().broadCastAction(performer.getName() + " starts tending " + targetName + "'s wounds.", performer, wound
/* 1038 */               .getCreature(), 5);
/*      */         } 
/* 1040 */         if (act.getRarity() != 0)
/*      */         {
/* 1042 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1047 */         time = act.getTimeLeft();
/*      */       } 
/* 1049 */       if (counter > 5.0F && counter * 10.0F > time) {
/*      */         
/* 1051 */         Methods.sendSound(performer, "sound.work.firstaid.bandage");
/* 1052 */         power = firstaid.skillCheck((wound.getSeverity() / 1000.0F), bandaid, 0.0D, false, counter);
/* 1053 */         if (act.getRarity() > 0)
/* 1054 */           power = Math.max(power + (act.getRarity() * 10), (act.getRarity() * 10)); 
/* 1055 */         String targetName = wound.getCreature().getNameWithGenus();
/* 1056 */         if (wound.getCreature().isPlayer())
/* 1057 */           targetName = wound.getCreature().getName(); 
/* 1058 */         int rarity = 1 + bandaid.getRarity() + act.getRarity();
/* 1059 */         if (bandaid.isRoyal()) {
/*      */           
/* 1061 */           performer.getCommunicator().sendNormalServerMessage("The " + bandaid.getNameWithGenus() + " mends itself!");
/*      */         }
/* 1063 */         else if (!bandaid.isCombine()) {
/* 1064 */           Items.destroyItem(bandaid.getWurmId());
/* 1065 */         } else if (bandaid.isHealingSalve()) {
/* 1066 */           bandaid.setWeight(bandaid.getWeightGrams() - wound.getNumBandagesNeeded() * 10, true);
/*      */         } else {
/* 1068 */           bandaid.setWeight(bandaid.getWeightGrams() - wound.getNumBandagesNeeded() * 100, true);
/* 1069 */         }  if (power > 0.0D) {
/*      */           
/* 1071 */           performer.getCommunicator().sendNormalServerMessage("You " + bandage + " the wound with " + bandaid
/* 1072 */               .getNameWithGenus() + ".");
/* 1073 */           if (wound.getCreature() == performer) {
/* 1074 */             Server.getInstance().broadCastAction(performer
/* 1075 */                 .getName() + " " + bandage + "s " + performer.getHisHerItsString() + " wound with " + bandaid
/* 1076 */                 .getNameWithGenus() + ".", performer, 5);
/*      */           } else {
/*      */             
/* 1079 */             wound.getCreature()
/* 1080 */               .getCommunicator()
/* 1081 */               .sendNormalServerMessage(performer
/* 1082 */                 .getName() + " " + bandage + "s the wound with " + bandaid.getNameWithGenus() + ".");
/*      */             
/* 1084 */             Server.getInstance().broadCastAction(performer
/* 1085 */                 .getName() + " " + bandage + "s " + targetName + "'s wound with " + bandaid
/* 1086 */                 .getNameWithGenus() + ".", performer, wound.getCreature(), 5);
/* 1087 */             if (wound.getCreature() instanceof Player && (
/* 1088 */               performer.getDeity() == null || !performer.getDeity().isHateGod()))
/* 1089 */               performer.maybeModifyAlignment(1.0F); 
/*      */           } 
/* 1091 */           wound.setBandaged(true);
/* 1092 */           wound.modifySeverity((int)Math.min((-wound.getSeverity() / 10.0F), 
/*      */                 
/* 1094 */                 -((3275.0F * rarity) + power * ((performer.getCultist() != null && performer.getCultist().healsFaster()) ? 100.0F : 50.0F))));
/*      */ 
/*      */           
/* 1097 */           performer.achievement(536);
/*      */         }
/* 1099 */         else if (!wound.isInternal() && !wound.isBruise() && power < -90.0D && bandaid
/* 1100 */           .getRarity() == 0 && act.getRarity() == 0) {
/*      */           
/* 1102 */           performer.getCommunicator().sendNormalServerMessage("You try to " + bandage + " the wound with " + bandaid
/* 1103 */               .getNameWithGenus() + " but only make it worse!");
/*      */           
/* 1105 */           if (wound.getCreature() == performer) {
/* 1106 */             Server.getInstance().broadCastAction(performer
/* 1107 */                 .getName() + " tries to " + bandage + " " + performer.getHisHerItsString() + " wound with " + bandaid
/* 1108 */                 .getNameWithGenus() + " but only makes it worse.", performer, 5);
/*      */           }
/*      */           else {
/*      */             
/* 1112 */             wound.getCreature()
/* 1113 */               .getCommunicator()
/* 1114 */               .sendNormalServerMessage(performer
/* 1115 */                 .getName() + " tries to " + bandage + " the wound with " + bandaid
/* 1116 */                 .getNameWithGenus() + " but only makes it worse.");
/* 1117 */             Server.getInstance().broadCastAction(performer
/* 1118 */                 .getName() + " tries to " + bandage + " " + targetName + "'s wound with " + bandaid
/* 1119 */                 .getNameWithGenus() + " but only makes it worse.", performer, wound
/* 1120 */                 .getCreature(), 5);
/*      */           } 
/*      */           
/* 1123 */           wound.modifySeverity(3275);
/*      */         }
/*      */         else {
/*      */           
/* 1127 */           performer.getCommunicator().sendNormalServerMessage("You try to " + bandage + " the wound with " + bandaid
/* 1128 */               .getNameWithGenus() + " but fail.");
/* 1129 */           if (wound.getCreature() == performer) {
/* 1130 */             Server.getInstance().broadCastAction(performer
/* 1131 */                 .getName() + " tries to " + bandage + " " + performer.getHisHerItsString() + " wound with " + bandaid
/* 1132 */                 .getNameWithGenus() + " but fails.", performer, 5);
/*      */           } else {
/*      */             
/* 1135 */             wound.getCreature()
/* 1136 */               .getCommunicator()
/* 1137 */               .sendNormalServerMessage(performer
/* 1138 */                 .getName() + " tries to " + bandage + " the wound with " + bandaid
/* 1139 */                 .getNameWithGenus() + " but fails.");
/* 1140 */             Server.getInstance().broadCastAction(performer
/* 1141 */                 .getName() + " tries to " + bandage + " " + targetName + "'s wound with " + bandaid
/* 1142 */                 .getNameWithGenus() + " but fails.", performer, wound.getCreature(), 5);
/*      */           } 
/*      */         } 
/*      */         
/* 1146 */         done = true;
/*      */       } 
/*      */     } 
/* 1149 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean treat(Creature performer, Item cover, Wound wound, float counter, Action act) {
/* 1155 */     boolean done = false;
/*      */     
/* 1157 */     if (performer.isPlayer() && ((Player)performer).stuckCounter > 0) {
/*      */       
/* 1159 */       performer.getCommunicator().sendNormalServerMessage("You are a bit dizzy. Try again in " + ((Player)performer).stuckCounter + " second/s.", (byte)3);
/*      */       
/* 1161 */       return true;
/*      */     } 
/* 1163 */     if (wound.getCreature() == null) {
/* 1164 */       done = true;
/* 1165 */     } else if (!done) {
/*      */       
/* 1167 */       if (!wound.isAcidWound() && cover.getTemplateId() != 481) {
/*      */         
/* 1169 */         performer.getCommunicator().sendNormalServerMessage("You can't treat the wound with that.", (byte)3);
/* 1170 */         return true;
/*      */       } 
/* 1172 */       if (cover.getTemplateId() != 481 && cover.getTemplateId() != 128) {
/*      */         
/* 1174 */         performer.getCommunicator().sendNormalServerMessage("You can't treat the wound with that.", (byte)3);
/* 1175 */         return true;
/*      */       } 
/* 1177 */       if (!wound.isAcidWound() && wound.getHealEff() > cover.getAuxData()) {
/*      */         
/* 1179 */         performer.getCommunicator().sendNormalServerMessage("The wound already has a better cover.", (byte)3);
/* 1180 */         return true;
/*      */       } 
/* 1182 */       if (wound.getCreature().isGhost()) {
/*      */         
/* 1184 */         performer.getCommunicator().sendNormalServerMessage("You can't treat that wound. It is not physical.", (byte)3);
/* 1185 */         return true;
/*      */       } 
/* 1187 */       Skill firstaid = null;
/* 1188 */       int time = 1000;
/* 1189 */       Skills skills = performer.getSkills();
/* 1190 */       double power = 0.0D;
/*      */       
/*      */       try {
/* 1193 */         firstaid = skills.getSkill(10056);
/*      */       }
/* 1195 */       catch (NoSuchSkillException nss) {
/*      */         
/* 1197 */         firstaid = skills.learn(10056, 1.0F);
/*      */       } 
/* 1199 */       if (counter == 1.0F) {
/*      */         
/* 1201 */         time = (int)(3.0D * ((wound.getSeverity() / 2000.0F) + 100.0D - firstaid.getKnowledge(0.0D) / 2.0D));
/* 1202 */         act.setTimeLeft(time);
/* 1203 */         performer.sendActionControl(Actions.actionEntrys[196].getVerbString(), true, time);
/*      */         
/* 1205 */         performer.getCommunicator().sendNormalServerMessage("You start to treat the wound.");
/* 1206 */         if (wound.getCreature() == performer) {
/* 1207 */           Server.getInstance().broadCastAction(performer
/* 1208 */               .getName() + " starts treating " + performer.getHisHerItsString() + " wounds.", performer, 5);
/*      */         }
/*      */         else {
/*      */           
/* 1212 */           wound.getCreature().getCommunicator()
/* 1213 */             .sendNormalServerMessage(performer.getName() + " starts treating your wounds.");
/* 1214 */           Server.getInstance().broadCastAction(performer
/* 1215 */               .getName() + " starts treating " + wound.getCreature().getNameWithGenus() + "'s wounds.", performer, 5);
/*      */         } 
/*      */ 
/*      */         
/* 1219 */         if (act.getRarity() != 0)
/*      */         {
/* 1221 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1226 */         time = act.getTimeLeft();
/*      */       } 
/* 1228 */       if (counter > 5.0F && counter * 10.0F > time) {
/*      */         
/* 1230 */         Methods.sendSound(performer, "sound.work.firstaid.bandage");
/* 1231 */         power = firstaid.skillCheck((wound.getSeverity() / 1000.0F), cover, 0.0D, false, counter);
/* 1232 */         if (act.getPower() > 0.0F) {
/* 1233 */           power = Math.max(power + (act.getRarity() * 10), (act.getRarity() * 10));
/*      */         }
/* 1235 */         if (power > 0.0D || Server.rand.nextInt(100) < 20) {
/*      */           
/* 1237 */           performer.getCommunicator().sendNormalServerMessage("You treat the wound with " + cover
/* 1238 */               .getNameWithGenus() + ".");
/* 1239 */           if (wound.getCreature() == performer) {
/* 1240 */             Server.getInstance().broadCastAction(performer
/* 1241 */                 .getName() + " treats " + performer.getHisHerItsString() + " wound.", performer, 5);
/*      */           } else {
/*      */             
/* 1244 */             wound.getCreature()
/* 1245 */               .getCommunicator()
/* 1246 */               .sendNormalServerMessage(performer
/* 1247 */                 .getName() + " treats the wound with " + cover.getNameWithGenus() + " " + cover
/* 1248 */                 .getDescription().toLowerCase() + ".");
/* 1249 */             Server.getInstance().broadCastAction(performer
/* 1250 */                 .getName() + " treats " + wound.getCreature().getName() + "'s wound with " + cover
/* 1251 */                 .getNameWithGenus() + ".", performer, 5);
/* 1252 */             if (wound.getCreature() instanceof Player && (
/* 1253 */               performer.getDeity() == null || !performer.getDeity().isHateGod()))
/* 1254 */               performer.maybeModifyAlignment(1.0F); 
/*      */           } 
/* 1256 */           if (!wound.isAcidWound()) {
/*      */             
/* 1258 */             wound.setHealeff((byte)(int)(cover.getAuxData() + act.getPower() + cover.getRarity()));
/*      */           }
/* 1260 */           else if (cover.getTemplateId() == 128) {
/*      */             
/* 1262 */             if (cover.getWeightGrams() > wound.getSeverity() / 10.0F) {
/*      */               
/* 1264 */               wound.setType((byte)4);
/* 1265 */               wound.setHealeff((byte)(int)(18.0F + act.getPower() + cover.getRarity()));
/*      */             } else {
/*      */               
/* 1268 */               performer.getCommunicator().sendNormalServerMessage("You need more water! It buuurns!");
/*      */             } 
/*      */           } 
/* 1271 */         } else if (power < -90.0D && !cover.isLiquid() && cover.getRarity() == 0 && act.getRarity() == 0) {
/*      */           
/* 1273 */           performer.getCommunicator().sendNormalServerMessage("You try to treat the wound with " + cover
/* 1274 */               .getNameWithGenus() + " but only make it worse!");
/* 1275 */           if (wound.getCreature() == performer) {
/* 1276 */             Server.getInstance().broadCastAction(performer
/* 1277 */                 .getName() + " tries to treat " + performer.getHisHerItsString() + " wound but only makes it worse.", performer, 5);
/*      */           }
/*      */           else {
/*      */             
/* 1281 */             wound.getCreature()
/* 1282 */               .getCommunicator()
/* 1283 */               .sendNormalServerMessage(performer
/* 1284 */                 .getName() + " tries to treat the wound with " + cover.getName() + " but only makes it worse.");
/*      */             
/* 1286 */             Server.getInstance().broadCastAction(performer
/* 1287 */                 .getName() + " tries to treat " + wound.getCreature().getName() + "'s wound with " + cover
/* 1288 */                 .getNameWithGenus() + " but only makes it worse.", performer, 5);
/*      */           } 
/*      */           
/* 1291 */           wound.modifySeverity(3275);
/*      */         }
/*      */         else {
/*      */           
/* 1295 */           performer.getCommunicator().sendNormalServerMessage("You try to treat the wound with " + cover
/* 1296 */               .getNameWithGenus() + " but fail.");
/* 1297 */           if (wound.getCreature() == performer) {
/* 1298 */             Server.getInstance().broadCastAction(performer
/* 1299 */                 .getName() + " tries to treat " + performer.getHisHerItsString() + " wound with " + cover
/* 1300 */                 .getNameWithGenus() + " but fails.", performer, 5);
/*      */           } else {
/* 1302 */             Server.getInstance().broadCastAction(performer
/* 1303 */                 .getName() + " tries to treat " + wound.getCreature().getName() + "'s wound with " + cover
/* 1304 */                 .getNameWithGenus() + " but fails.", performer, 5);
/*      */           } 
/* 1306 */         }  if (cover.getTemplateId() != 128) {
/* 1307 */           Items.destroyItem(cover.getWurmId());
/*      */         } else {
/* 1309 */           cover.setWeight(cover.getWeightGrams() - (int)(wound.getSeverity() / 10.0F), true);
/* 1310 */         }  done = true;
/*      */       } 
/*      */     } 
/* 1313 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean shear(Action act, Item source, Creature performer, Creature target, float counter) {
/* 1318 */     if (!Methods.isActionAllowed(performer, (short)646))
/* 1319 */       return true; 
/* 1320 */     if (target.isSheared()) {
/*      */       
/* 1322 */       performer.getCommunicator().sendNormalServerMessage("The creature is already sheared.", (byte)3);
/* 1323 */       return true;
/*      */     } 
/*      */     
/* 1326 */     if (!performer.getInventory().mayCreatureInsertItem()) {
/*      */       
/* 1328 */       performer.getCommunicator().sendNormalServerMessage("You have no space left in your inventory to put what you shear.");
/*      */       
/* 1330 */       return true;
/*      */     } 
/*      */     
/* 1333 */     if (source.getTemplateId() != 394) {
/*      */       
/* 1335 */       performer.getCommunicator().sendNormalServerMessage("You are not sure what to do with this tool.", (byte)3);
/* 1336 */       return true;
/*      */     } 
/* 1338 */     target.shouldStandStill = true;
/* 1339 */     Skill animalHusbandry = null;
/* 1340 */     int time = 100;
/* 1341 */     if (counter == 1.0F) {
/*      */ 
/*      */       
/*      */       try {
/* 1345 */         animalHusbandry = performer.getSkills().getSkill(10085);
/*      */       }
/* 1347 */       catch (NoSuchSkillException nss) {
/*      */         
/* 1349 */         animalHusbandry = performer.getSkills().learn(10085, 1.0F);
/*      */       } 
/* 1351 */       time = Actions.getSlowActionTime(performer, animalHusbandry, source, 0.0D);
/*      */       
/* 1353 */       act.setTimeLeft(time);
/* 1354 */       performer.sendActionControl(Actions.actionEntrys[646].getVerbString(), true, time);
/*      */       
/* 1356 */       performer.getCommunicator().sendNormalServerMessage("You start shearing the " + target
/* 1357 */           .getName() + ".");
/* 1358 */       Server.getInstance().broadCastAction(performer
/* 1359 */           .getName() + " starts shearing the " + target.getName() + ".", performer, 5);
/*      */     }
/*      */     else {
/*      */       
/* 1363 */       time = act.getTimeLeft();
/*      */     } 
/* 1365 */     if (counter * 10.0F > time) {
/*      */       
/* 1367 */       if (act.getRarity() != 0)
/*      */       {
/* 1369 */         performer.playPersonalSound("sound.fx.drumroll");
/*      */       }
/*      */       
/*      */       try {
/* 1373 */         animalHusbandry = performer.getSkills().getSkill(10085);
/*      */       }
/* 1375 */       catch (NoSuchSkillException nss) {
/*      */         
/* 1377 */         animalHusbandry = performer.getSkills().learn(10085, 1.0F);
/*      */       } 
/*      */       
/* 1380 */       int numberOfWoolProduced = getWoolProduceForAge((target.getStatus()).age);
/* 1381 */       int knowledge = (int)animalHusbandry.getKnowledge(0.0D);
/* 1382 */       float diff = getShearingDifficulty((target.getStatus()).age, knowledge);
/* 1383 */       double power = animalHusbandry.skillCheck(diff, source, 0.0D, false, counter);
/* 1384 */       target.setSheared(true);
/* 1385 */       float ql = knowledge + (100 - knowledge) * (float)power / 100.0F;
/* 1386 */       ql = GeneralUtilities.calcRareQuality(ql, act.getRarity(), source.getRarity(), 0);
/* 1387 */       float modifier = 1.0F;
/* 1388 */       if (source.getSpellEffects() != null)
/*      */       {
/* 1390 */         modifier = source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */       }
/*      */       
/*      */       try {
/* 1394 */         ItemTemplate woolTemplate = ItemTemplateFactory.getInstance().getTemplate(921);
/* 1395 */         Item wool = ItemFactory.createItem(921, Math.max(1.0F, Math.min(100.0F, ql * modifier)), (byte)69, act
/* 1396 */             .getRarity(), null);
/* 1397 */         wool.setLastOwnerId(performer.getWurmId());
/* 1398 */         wool.setWeight(numberOfWoolProduced * woolTemplate.getWeightGrams(), true);
/*      */         
/* 1400 */         performer.getInventory().insertItem(wool);
/* 1401 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*      */         
/* 1403 */         performer.getCommunicator().sendNormalServerMessage("You finish shearing the " + target
/* 1404 */             .getName() + ".");
/* 1405 */         Server.getInstance().broadCastAction(performer
/* 1406 */             .getName() + " shears the " + target.getName() + ".", performer, 5);
/*      */       }
/* 1408 */       catch (FailedException e) {
/*      */ 
/*      */         
/* 1411 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       }
/* 1413 */       catch (NoSuchTemplateException e) {
/*      */ 
/*      */         
/* 1416 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/*      */ 
/*      */       
/* 1420 */       return true;
/*      */     } 
/*      */     
/* 1423 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final int getWoolProduceForAge(int age) {
/* 1428 */     if (age < 3)
/* 1429 */       return 1; 
/* 1430 */     if (age < 8)
/* 1431 */       return 2; 
/* 1432 */     if (age < 12)
/* 1433 */       return 3; 
/* 1434 */     if (age < 30)
/* 1435 */       return 4; 
/* 1436 */     if (age < 40) {
/* 1437 */       return 5;
/*      */     }
/* 1439 */     return 4;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final int getShearingDifficulty(int age, double currentSkill) {
/* 1444 */     if (age < 3)
/* 1445 */       return (int)currentSkill - 30; 
/* 1446 */     if (age < 8)
/* 1447 */       return (int)currentSkill - 25; 
/* 1448 */     if (age < 12)
/* 1449 */       return (int)currentSkill - 20; 
/* 1450 */     if (age < 30)
/* 1451 */       return (int)currentSkill - 15; 
/* 1452 */     if (age < 40) {
/* 1453 */       return (int)currentSkill - 10;
/*      */     }
/* 1455 */     return (int)currentSkill - 5;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean milk(Action act, Item tool, Creature performer, Creature target, float counter) {
/* 1460 */     if (!Methods.isActionAllowed(performer, (short)345))
/* 1461 */       return true; 
/* 1462 */     if (!tool.isContainerLiquid()) {
/*      */       
/* 1464 */       performer.getCommunicator().sendNormalServerMessage("You cannot keep milk in that.");
/* 1465 */       return true;
/*      */     } 
/* 1467 */     Item[] cont = tool.getAllItems(true);
/* 1468 */     if (cont.length < 2) {
/*      */       
/* 1470 */       if (cont.length == 1) {
/*      */         
/* 1472 */         if (cont[0].getTemplateId() != 142 && cont[0]
/* 1473 */           .getTemplateId() != 1012 && cont[0]
/* 1474 */           .getTemplateId() != 1013) {
/*      */           
/* 1476 */           performer.getCommunicator().sendNormalServerMessage("The milk would be destroyed. Empty the " + tool
/* 1477 */               .getName() + " first.");
/* 1478 */           return true;
/*      */         } 
/*      */         
/* 1481 */         if ((cont[0].getTemplateId() != 142 || target.getTemplate().getTemplateId() != 3) && (cont[0]
/* 1482 */           .getTemplateId() != 1012 || target.getTemplate().getTemplateId() != 96) && (cont[0]
/* 1483 */           .getTemplateId() != 1013 || target.getTemplate().getTemplateId() != 82))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1489 */           performer.getCommunicator().sendNormalServerMessage("You cannot mix types of milk. Empty the " + tool
/* 1490 */               .getName() + " first.");
/* 1491 */           return true;
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 1497 */       performer.getCommunicator().sendNormalServerMessage("The milk would be destroyed. Empty the " + tool
/* 1498 */           .getName() + " first.");
/* 1499 */       return true;
/*      */     } 
/* 1501 */     if (tool.getFreeVolume() == 0) {
/*      */       
/* 1503 */       performer.getCommunicator().sendNormalServerMessage("The " + tool
/* 1504 */           .getName() + " is already full and therefore will not be able to contain any milk.");
/* 1505 */       return true;
/*      */     } 
/* 1507 */     if (target.isMilked() && counter == 1.0F) {
/*      */       
/* 1509 */       performer.getCommunicator().sendNormalServerMessage("The creature is already milked.");
/* 1510 */       return true;
/*      */     } 
/* 1512 */     target.shouldStandStill = true;
/* 1513 */     boolean toReturn = false;
/* 1514 */     int time = 150;
/* 1515 */     Skill skill = performer.getSkills().getSkillOrLearn(10060);
/* 1516 */     time = Actions.getStandardActionTime(performer, skill, tool, 0.0D);
/*      */     
/* 1518 */     if (counter == 1.0F) {
/*      */       
/* 1520 */       if (tool.getFreeVolume() < 1000)
/*      */       {
/* 1522 */         performer.getCommunicator().sendNormalServerMessage("The " + tool
/* 1523 */             .getName() + " will not be able to contain all of the milk.");
/*      */       }
/* 1525 */       int maxSearches = calcMaxPasses(target, skill.getKnowledge(0.0D), tool);
/* 1526 */       time = Actions.getQuickActionTime(performer, skill, null, 0.0D);
/* 1527 */       act.setNextTick(time);
/* 1528 */       act.setTickCount(1);
/* 1529 */       act.setData(0L);
/* 1530 */       float totalTime = (time * maxSearches);
/* 1531 */       act.setTimeLeft((int)totalTime);
/*      */       
/* 1533 */       performer.getCommunicator().sendNormalServerMessage("You start to milk the " + target.getName() + ".");
/* 1534 */       Server.getInstance().broadCastAction(performer.getName() + " starts to milk a " + target.getName() + ".", performer, 5);
/* 1535 */       performer.sendActionControl(Actions.actionEntrys[345].getVerbString(), true, (int)totalTime);
/* 1536 */       performer.getStatus().modifyStamina(-500.0F);
/*      */     } else {
/*      */       
/* 1539 */       time = act.getTimeLeft();
/*      */     } 
/* 1541 */     if (tool != null && act.justTickedSecond()) {
/* 1542 */       tool.setDamage(tool.getDamage() + 3.0E-4F * tool.getDamageModifier());
/*      */     }
/* 1544 */     if (counter * 10.0F >= act.getNextTick()) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1549 */       int searchCount = act.getTickCount();
/* 1550 */       int maxSearches = calcMaxPasses(target, skill.getKnowledge(0.0D), tool);
/* 1551 */       act.incTickCount();
/* 1552 */       act.incNextTick(Actions.getQuickActionTime(performer, skill, null, 0.0D));
/* 1553 */       int knowledge = (int)skill.getKnowledge(0.0D);
/* 1554 */       int diff = getShearingDifficulty((target.getStatus()).age, knowledge);
/*      */       
/* 1556 */       performer.getStatus().modifyStamina((-1500 * searchCount));
/* 1557 */       int milkType = 142;
/* 1558 */       if (target.getTemplate().getTemplateId() == 96)
/* 1559 */         milkType = 1012; 
/* 1560 */       if (target.getTemplate().getTemplateId() == 82) {
/* 1561 */         milkType = 1013;
/*      */       }
/*      */       
/* 1564 */       if (searchCount >= maxSearches) {
/* 1565 */         toReturn = true;
/*      */       }
/* 1567 */       act.setData(act.getData() + 1L);
/*      */ 
/*      */       
/* 1570 */       double power = skill.skillCheck(diff, tool, 0.0D, false, counter / searchCount);
/*      */ 
/*      */       
/*      */       try {
/* 1574 */         Item milk = ItemFactory.createItem(milkType, 100.0F, null);
/* 1575 */         milk.setName(target.getNameWithoutPrefixes() + " milk");
/* 1576 */         if (target.isReborn()) {
/* 1577 */           milk.setIsZombiefied(true);
/*      */         }
/* 1579 */         SoundPlayer.playSound("sound.work.milking", target.getTileX(), target.getTileY(), true, 3.0F);
/*      */         
/* 1581 */         if (searchCount == 1) {
/* 1582 */           target.setMilked(true);
/*      */         }
/* 1584 */         MethodsItems.fillContainer(act, tool, milk, performer, false);
/*      */         
/* 1586 */         if (searchCount < maxSearches)
/*      */         {
/* 1588 */           if (tool.getFreeVolume() == 0) {
/*      */             
/* 1590 */             performer.getCommunicator().sendNormalServerMessage("The " + tool
/* 1591 */                 .getName() + " is full, and cannot fit anymore milk in.");
/* 1592 */             Server.getInstance().broadCastAction(performer
/* 1593 */                 .getName() + " milks the " + target.getName() + ".", performer, 5);
/* 1594 */             return true;
/*      */           } 
/* 1596 */           if (tool.getFreeVolume() < 1000)
/*      */           {
/* 1598 */             performer.getCommunicator().sendNormalServerMessage("The " + tool
/* 1599 */                 .getName() + " will not be able to contain all of the milk.");
/*      */           }
/*      */         }
/*      */       
/* 1603 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 1605 */         logger.log(Level.WARNING, "No template for " + milkType, (Throwable)nst);
/* 1606 */         performer.getCommunicator().sendNormalServerMessage("You fail to milk. You realize something is wrong with the world.");
/*      */         
/* 1608 */         return true;
/*      */       }
/* 1610 */       catch (FailedException fe) {
/*      */         
/* 1612 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 1613 */         performer.getCommunicator().sendNormalServerMessage("You fail to milk. You realize something is wrong with the world.");
/*      */         
/* 1615 */         return true;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1622 */       if (toReturn) {
/*      */         
/* 1624 */         performer.getCommunicator().sendNormalServerMessage("You finish milking the " + target
/* 1625 */             .getName() + ".");
/* 1626 */         Server.getInstance().broadCastAction(performer
/* 1627 */             .getName() + " milks the " + target.getName() + ".", performer, 5);
/*      */       } 
/*      */     } 
/* 1630 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean catchFlies(Creature performer, Creature target, Item jar, short action, Action act, float counter) {
/* 1636 */     int MIN_VINEGAR = 2;
/* 1637 */     int MIN_HONEY = 10;
/* 1638 */     boolean toReturn = false;
/* 1639 */     if (target.isGhost() || target.isPlayer() || !target.isDomestic()) {
/*      */       
/* 1641 */       performer.getCommunicator().sendNormalServerMessage("There are no flies on " + target.getHimHerItString() + "!");
/* 1642 */       return true;
/*      */     } 
/* 1644 */     if (!Methods.isActionAllowed(performer, (short)398))
/* 1645 */       return true; 
/* 1646 */     if (!jar.isFlyTrap()) {
/*      */       
/* 1648 */       performer.getCommunicator().sendNormalServerMessage("You cannot catch flies with that! You need honey or vinegar in the " + jar
/* 1649 */           .getName() + ".");
/* 1650 */       return true;
/*      */     } 
/* 1652 */     if (target.canBeGroomed() || counter > 1.0F) {
/*      */       
/* 1654 */       Skill breeding = performer.getSkills().getSkillOrLearn(10085);
/* 1655 */       int time = 20;
/* 1656 */       target.shouldStandStill = true;
/* 1657 */       int maxSearches = (int)((breeding.getKnowledge(0.0D) + 28.0D) / 27.0D) + 1;
/* 1658 */       if (counter == 1.0F) {
/*      */         
/* 1660 */         act.setNextTick(time);
/* 1661 */         act.setTickCount(1);
/* 1662 */         float totalTime = (time * maxSearches);
/* 1663 */         act.setTimeLeft((int)totalTime);
/* 1664 */         Server.getInstance().broadCastAction(performer.getName() + " starts to check a " + target.getName() + " for flies.", performer, 5);
/*      */         
/* 1666 */         performer.getCommunicator().sendNormalServerMessage("You start to check " + target.getName() + " for flies.");
/* 1667 */         performer.sendActionControl(Actions.actionEntrys[398].getVerbString(), true, act
/* 1668 */             .getTimeLeft());
/* 1669 */         performer.getStatus().modifyStamina(-500.0F);
/* 1670 */         return false;
/*      */       } 
/*      */       
/* 1673 */       time = act.getTimeLeft();
/*      */       
/* 1675 */       Item vinegar = jar.getVinegar();
/* 1676 */       Item honey = jar.getHoney();
/* 1677 */       if (vinegar != null) {
/*      */         
/* 1679 */         if (vinegar.getWeightGrams() < 2)
/*      */         {
/* 1681 */           performer.getCommunicator().sendNormalServerMessage("There is not enough " + vinegar.getName() + " to interest a fly!");
/* 1682 */           return true;
/*      */         }
/*      */       
/* 1685 */       } else if (honey != null) {
/*      */         
/* 1687 */         if (honey.getWeightGrams() < 10)
/*      */         {
/* 1689 */           performer.getCommunicator().sendNormalServerMessage("There is not enough " + honey.getName() + " to interest a fly!");
/* 1690 */           return true;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1695 */         performer.getCommunicator().sendNormalServerMessage("There is nothing in there to interest a fly!");
/* 1696 */         return true;
/*      */       } 
/* 1698 */       if (counter * 10.0F > act.getNextTick()) {
/*      */         
/* 1700 */         if (act.getRarity() != 0) {
/* 1701 */           performer.playPersonalSound("sound.fx.drumroll");
/*      */         }
/* 1703 */         int searchCount = act.getTickCount();
/* 1704 */         act.incTickCount();
/* 1705 */         act.incNextTick(20.0F);
/* 1706 */         performer.getStatus().modifyStamina((-200 * searchCount));
/*      */         
/* 1708 */         if (searchCount >= maxSearches) {
/* 1709 */           toReturn = true;
/*      */         }
/* 1711 */         if (breeding.skillCheck(target.getBaseCombatRating(), jar, 0.0D, false, counter / 2.0F) > 0.0D)
/*      */         {
/*      */           
/*      */           try {
/* 1715 */             target.setLastGroomed(System.currentTimeMillis());
/*      */             
/* 1717 */             int knowledge = (int)breeding.getKnowledge(0.0D);
/* 1718 */             float ql = knowledge - Server.rand.nextFloat() * (knowledge / 5);
/* 1719 */             Item fly = ItemFactory.createItem(1359, ql, act.getRarity(), null);
/* 1720 */             jar.insertItem(fly, true);
/* 1721 */             performer.getCommunicator().sendNormalServerMessage("You manage to catch a fly in the jar!");
/* 1722 */             Server.getInstance().broadCastAction(performer
/* 1723 */                 .getName() + " puts something in a jar.", performer, 5);
/*      */             
/* 1725 */             if (searchCount < maxSearches && !jar.mayCreatureInsertItem()) {
/*      */               
/* 1727 */               performer.getCommunicator().sendNormalServerMessage("The jar is now full. You would have no space to put another fly.");
/*      */               
/* 1729 */               toReturn = true;
/*      */             } 
/*      */ 
/*      */             
/* 1733 */             if (vinegar != null)
/*      */             {
/*      */               
/* 1736 */               vinegar.setWeight(vinegar.getWeightGrams() - 2, true);
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 1741 */             else if (honey != null)
/*      */             {
/* 1743 */               honey.setWeight(honey.getWeightGrams() - 10, true);
/*      */             }
/*      */           
/*      */           }
/* 1747 */           catch (FailedException e) {
/*      */ 
/*      */             
/* 1750 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 1751 */             toReturn = true;
/*      */           }
/* 1753 */           catch (NoSuchTemplateException e) {
/*      */ 
/*      */             
/* 1756 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 1757 */             toReturn = true;
/*      */           } 
/*      */           
/* 1760 */           if (searchCount < maxSearches && !toReturn)
/*      */           {
/* 1762 */             act.setRarity(performer.getRarity());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1767 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1768 */               .getName() + " shys away and interrupts the action.", (byte)3);
/*      */           
/* 1770 */           toReturn = true;
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 1776 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already well tended and therefore has no flies.", (byte)3);
/*      */       
/* 1778 */       toReturn = true;
/*      */     } 
/* 1780 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final int getCreatureAgeFactor(Creature creature) {
/* 1785 */     int age = (creature.getStatus()).age;
/*      */     
/* 1787 */     if (age < 3)
/* 1788 */       return 1; 
/* 1789 */     if (age < 8)
/* 1790 */       return 2; 
/* 1791 */     if (age < 12)
/* 1792 */       return 3; 
/* 1793 */     if (age < 30)
/* 1794 */       return 4; 
/* 1795 */     if (age < 40) {
/* 1796 */       return 5;
/*      */     }
/* 1798 */     return 4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int calcMaxPasses(Creature creature, double currentSkill, Item tool) {
/* 1805 */     int bonus = 0;
/* 1806 */     if (tool.getSpellEffects() != null) {
/*      */       
/* 1808 */       float extraChance = tool.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_FARMYIELD) - 1.0F;
/* 1809 */       if (extraChance > 0.0F && Server.rand.nextFloat() < extraChance)
/*      */       {
/* 1811 */         bonus++;
/*      */       }
/*      */     } 
/*      */     
/* 1815 */     return Math.min(getCreatureAgeFactor(creature), (int)(currentSkill + 28.0D) / 27 + bonus);
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendSetKingdomQuestion(Creature performer, Item wand) {
/* 1820 */     if ((wand.getTemplateId() == 315 || wand.getTemplateId() == 176) && performer
/* 1821 */       .getPower() >= 2) {
/*      */ 
/*      */       
/* 1824 */       SetKingdomQuestion question = new SetKingdomQuestion(performer, "Set kingdom", "Which player should receive which kingdom?", wand.getWurmId());
/* 1825 */       question.sendQuestion();
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1830 */       SetKingdomQuestion question = new SetKingdomQuestion(performer, "Set kingdom", "Leave current kingdom?", wand.getWurmId());
/* 1831 */       question.sendQuestion();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void sendChallengeKingQuestion(Creature responder) {
/* 1837 */     if (responder.isKing()) {
/*      */       
/* 1839 */       RoyalChallenge question = new RoyalChallenge(responder);
/* 1840 */       question.sendQuestion();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendAskKingdomQuestion(Creature performer, Creature target) {
/* 1846 */     if (performer.isPlayer())
/*      */     {
/* 1848 */       if (((Player)performer).lastSentQuestion > 0) {
/*      */         
/* 1850 */         performer.getCommunicator().sendNormalServerMessage("You must wait another " + ((Player)performer).lastSentQuestion + " seconds before asking again.");
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*      */     
/* 1856 */     AskKingdomQuestion question = new AskKingdomQuestion(target, "Change kingdom", "Do you wish to change kingdom?", performer.getWurmId());
/* 1857 */     question.sendQuestion();
/* 1858 */     if (performer.isPlayer()) {
/* 1859 */       ((Player)performer).lastSentQuestion = 60;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final void sendAskPriestQuestion(Creature performer, @Nullable Item altar, @Nullable Creature creature) {
/* 1865 */     if (performer.getDeity() != null && (performer.getFaith() == 30.0F || performer.getPower() > 0) && 
/* 1866 */       !performer.isPriest()) {
/*      */ 
/*      */       
/* 1869 */       if (!performer.isPaying()) {
/*      */         
/* 1871 */         performer.getCommunicator().sendAlertServerMessage("You need to be a premium player in order to become a priest.", (byte)2);
/*      */         
/*      */         return;
/*      */       } 
/* 1875 */       long targ = -10L;
/* 1876 */       if (altar != null) {
/* 1877 */         targ = altar.getWurmId();
/* 1878 */       } else if (creature != null) {
/* 1879 */         targ = creature.getWurmId();
/* 1880 */       }  PriestQuestion question = new PriestQuestion(performer, "Becoming a priest", "Do you wish to become a priest?", targ);
/*      */       
/* 1882 */       question.sendQuestion();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean tame(Action act, Creature performer, Creature target, Item food, float counter) {
/* 1889 */     boolean done = false;
/* 1890 */     if (target.isDominated() && target.getDominator() != performer) {
/*      */       
/* 1892 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " ignores your attempts.");
/* 1893 */       return true;
/*      */     } 
/* 1895 */     if (target.getLeader() != null && target.getLeader().isPlayer() && target.getLeader() != performer) {
/*      */       
/* 1897 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1898 */           .getName() + " seems focused on " + target.getLeader().getName() + " and ignores your attempts.");
/*      */       
/* 1900 */       return true;
/*      */     } 
/* 1902 */     if (target.getHitched() != null) {
/*      */       
/* 1904 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1905 */           .getName() + " is too restrained and ignores your attempts.");
/* 1906 */       return true;
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
/* 1918 */     if (!target.isDominatable(performer) || !target.isAnimal()) {
/*      */       
/* 1920 */       performer.getCommunicator().sendNormalServerMessage("You fail to connect with the " + target.getName() + ".");
/* 1921 */       return true;
/*      */     } 
/* 1923 */     if (target.isFighting() && target.opponent != performer) {
/*      */       
/* 1925 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1926 */           .getName() + " is busy fighting " + target.opponent.getName() + ".");
/* 1927 */       return true;
/*      */     } 
/* 1929 */     if (target.isReborn()) {
/*      */       
/* 1931 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " shows no affection.");
/* 1932 */       return true;
/*      */     } 
/* 1934 */     if (performer.getPositionZ() + performer.getAltOffZ() < 0.0F) {
/*      */       
/* 1936 */       performer.getCommunicator().sendNormalServerMessage("You can't tame while standing in the water. You need to be more mobile.");
/*      */       
/* 1938 */       return true;
/*      */     } 
/* 1940 */     if (performer.getCurrentTile() != null && target.getCurrentTile() != null)
/*      */     {
/* 1942 */       if (performer.getCurrentTile().getStructure() != target.getCurrentTile().getStructure()) {
/*      */         
/* 1944 */         performer.getCommunicator().sendNormalServerMessage("You can't reach " + target.getName() + ".");
/* 1945 */         return true;
/*      */       } 
/*      */     }
/* 1948 */     if (performer.getCurrentVillage() != target.getCurrentVillage()) {
/*      */       
/* 1950 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1951 */           .getName() + " sniffs the air and looks too nervous to focus.");
/* 1952 */       return true;
/*      */     } 
/* 1954 */     if (!Methods.isActionAllowed(performer, (short)46, target.getTileX(), target.getTileY())) {
/* 1955 */       return true;
/*      */     }
/* 1957 */     if (!Servers.isThisAPvpServer()) {
/*      */       
/* 1959 */       Village bVill = target.getBrandVillage();
/* 1960 */       if (bVill != null && !bVill.isActionAllowed((short)46, performer)) {
/*      */         
/* 1962 */         performer.getCommunicator().sendNormalServerMessage("The " + target
/* 1963 */             .getName() + " rolls its eyes and looks too nervous to focus.");
/* 1964 */         return true;
/*      */       } 
/*      */     } 
/* 1967 */     if (!performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target.getPositionZ(), 4.0F)) {
/*      */       
/* 1969 */       performer.getCommunicator().sendNormalServerMessage("You are too far away to do that.");
/* 1970 */       return true;
/*      */     } 
/* 1972 */     Skill taming = null;
/* 1973 */     int time = 10000;
/*      */     
/* 1975 */     if (counter == 1.0F) {
/*      */       
/* 1977 */       if (performer.getStatus().getStamina() < 16000) {
/*      */         
/* 1979 */         performer.getCommunicator().sendNormalServerMessage("Taming is exhausting, and you are too tired right now.");
/* 1980 */         return true;
/*      */       } 
/* 1982 */       if (performer.getPet() != null)
/*      */       {
/* 1984 */         if (DbCreatureStatus.getIsLoaded(performer.getPet().getWurmId()) == 1) {
/*      */           
/* 1986 */           performer.getCommunicator().sendNormalServerMessage("You have a pet in a cage, remove it first, to tame this one.", (byte)3);
/*      */           
/* 1988 */           return true;
/*      */         } 
/*      */       }
/*      */       
/*      */       try {
/* 1993 */         taming = performer.getSkills().getSkill(10078);
/*      */       }
/* 1995 */       catch (NoSuchSkillException nss) {
/*      */         
/* 1997 */         taming = performer.getSkills().learn(10078, 1.0F);
/*      */       } 
/* 1999 */       target.turnTowardsCreature(performer);
/* 2000 */       time = Actions.getSlowActionTime(performer, taming, food, 0.0D) / 2;
/* 2001 */       act.setTimeLeft(time);
/* 2002 */       performer.sendActionControl(Actions.actionEntrys[46].getVerbString(), true, time);
/* 2003 */       performer.getCommunicator().sendNormalServerMessage("You start to tame the " + target.getName() + ".");
/* 2004 */       Server.getInstance().broadCastAction(performer.getName() + " starts to tame the " + target.getName() + ".", performer, 5);
/*      */ 
/*      */       
/* 2007 */       performer.getStatus().modifyStamina(-1000.0F);
/*      */     } else {
/*      */       
/* 2010 */       time = act.getTimeLeft();
/* 2011 */     }  if (Server.rand.nextInt(10) == 0 && !target.isFighting() && !target.isDominated() && target
/* 2012 */       .isAggHuman()) {
/*      */       
/* 2014 */       target.setTarget(performer.getWurmId(), true);
/* 2015 */       performer.getCommunicator().sendNormalServerMessage(target.getName() + " becomes aggravated and attacks you!");
/*      */     } 
/* 2017 */     if (act.currentSecond() % 5 == 0) {
/* 2018 */       performer.getStatus().modifyStamina(-5000.0F);
/*      */     }
/* 2020 */     if (counter * 10.0F > time) {
/*      */ 
/*      */       
/*      */       try {
/* 2024 */         taming = performer.getSkills().getSkill(10078);
/*      */       }
/* 2026 */       catch (NoSuchSkillException nss) {
/*      */         
/* 2028 */         taming = performer.getSkills().learn(10078, 1.0F);
/*      */       } 
/* 2030 */       Skill soul = null;
/*      */       
/*      */       try {
/* 2033 */         soul = target.getSkills().getSkill(105);
/*      */       }
/* 2035 */       catch (NoSuchSkillException nss) {
/*      */         
/* 2037 */         logger.log(Level.WARNING, target.getName() + " no soul strength when " + performer.getName() + " tames.");
/* 2038 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " seems untamable.");
/* 2039 */         return true;
/*      */       } 
/* 2041 */       done = true;
/* 2042 */       if (target.eat(food) < 10) {
/*      */         
/* 2044 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " doesn't seem to bother.");
/* 2045 */         return true;
/*      */       } 
/* 2047 */       Skill ownsoul = null;
/*      */       
/*      */       try {
/* 2050 */         ownsoul = performer.getSkills().getSkill(105);
/*      */       }
/* 2052 */       catch (NoSuchSkillException nss) {
/*      */         
/* 2054 */         ownsoul = performer.getSkills().learn(105, 20.0F);
/*      */       } 
/* 2056 */       boolean dryrun = false;
/* 2057 */       if (target.getLoyalty() > 99.0F) {
/*      */         
/* 2059 */         dryrun = true;
/* 2060 */         performer.getCommunicator().sendNormalServerMessage(target
/* 2061 */             .getName() + " is already tame, so you don't learn anything new.");
/*      */       } 
/* 2063 */       float difficultyBonus = target.getBaseCombatRating();
/*      */ 
/*      */       
/* 2066 */       double bonus = Math.max(0.0D, ownsoul.skillCheck(soul.getKnowledge() + 5.0D, -difficultyBonus, dryrun, counter));
/* 2067 */       if (performer.getDeity() == Deities.getDeity(1))
/*      */       {
/* 2069 */         if (performer.getFaith() > 20.0F && performer.getFavor() >= 20.0F)
/* 2070 */           bonus += 20.0D; 
/*      */       }
/* 2072 */       double power = taming.skillCheck(soul.getKnowledge() + difficultyBonus, bonus, dryrun, counter);
/* 2073 */       if (power > 0.0D) {
/*      */         
/* 2075 */         double maxpower = taming.getKnowledge(0.0D);
/* 2076 */         tameEffect(performer, target, power, dryrun, maxpower);
/*      */       }
/*      */       else {
/*      */         
/* 2080 */         boolean aggressive = false;
/* 2081 */         if (power < -50.0D) {
/*      */           
/* 2083 */           if (!target.isFighting()) {
/*      */             
/* 2085 */             aggressive = true;
/* 2086 */             target.setTarget(performer.getWurmId(), true);
/* 2087 */             performer.getCommunicator().sendNormalServerMessage(target
/* 2088 */                 .getName() + " becomes aggravated and attacks you!");
/*      */           } 
/* 2090 */           if (target.isDominated() && target.getDominator() == performer)
/*      */           {
/* 2092 */             target.modifyLoyalty(-3.0F);
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2099 */         if (!aggressive)
/* 2100 */           performer.getCommunicator().sendNormalServerMessage(target.getName() + " ignores your commands."); 
/*      */       } 
/*      */     } 
/* 2103 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void tameEffect(Creature performer, Creature target, double power, boolean dryrun, double maxpower) {
/* 2109 */     target.setTarget(-10L, true);
/* 2110 */     target.stopFighting();
/* 2111 */     if (performer.getTarget() == target) {
/*      */       
/* 2113 */       performer.setTarget(-10L, true);
/* 2114 */       performer.stopFighting();
/*      */     } 
/* 2116 */     if (target.opponent == performer)
/*      */     {
/* 2118 */       target.setOpponent(null);
/*      */     }
/* 2120 */     if (performer.opponent == target)
/*      */     {
/* 2122 */       performer.setOpponent(null);
/*      */     }
/* 2124 */     boolean setAsNew = false;
/* 2125 */     if (performer.getPet() != null) {
/*      */       
/* 2127 */       if (performer.getPet() != target) {
/*      */         
/* 2129 */         performer.getCommunicator().sendNormalServerMessage("You can't keep control over " + performer
/* 2130 */             .getPet().getNameWithGenus() + " as well.");
/* 2131 */         performer.getPet().setDominator(-10L);
/*      */         
/* 2133 */         setAsNew = true;
/*      */       } 
/*      */     } else {
/*      */       
/* 2137 */       setAsNew = true;
/* 2138 */     }  if (setAsNew) {
/*      */ 
/*      */       
/*      */       try {
/* 2142 */         target.setKingdomId(performer.getKingdomId());
/*      */       }
/* 2144 */       catch (IOException iox) {
/*      */         
/* 2146 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/* 2148 */       target.setDominator(performer.getWurmId());
/* 2149 */       performer.setPet(target.getWurmId());
/* 2150 */       target.setLoyalty((float)Math.min(maxpower, power));
/* 2151 */       target.getStatus().setLastPolledLoyalty();
/* 2152 */       if (dryrun) {
/* 2153 */         performer.getCommunicator().sendNormalServerMessage("Your taming does not seem to have any effect.");
/*      */       } else {
/* 2155 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " is now submissive to your will.");
/*      */       } 
/*      */       
/* 2158 */       performer.achievement(526);
/*      */ 
/*      */     
/*      */     }
/* 2162 */     else if (dryrun) {
/* 2163 */       performer.getCommunicator().sendNormalServerMessage("Your taming does not seem to have any effect.");
/*      */     } else {
/*      */       
/* 2166 */       target.setLoyalty((float)Math.min(maxpower, target.getLoyalty() + power / 10.0D));
/* 2167 */       target.getStatus().setLastPolledLoyalty();
/* 2168 */       performer.getCommunicator()
/* 2169 */         .sendNormalServerMessage("You create a stronger bond with " + target.getName() + ".");
/*      */     } 
/*      */     
/* 2172 */     Server.getInstance().broadCastAction(performer.getName() + " tames " + target.getNameWithGenus() + ".", performer, 5);
/*      */   }
/*      */ 
/*      */   
/*      */   static final void petGiveAway(Creature dominator, Creature receiver) {
/* 2177 */     if (receiver instanceof Player) {
/*      */       
/* 2179 */       if (dominator.getPet() != null)
/*      */       {
/* 2181 */         if (DbCreatureStatus.getIsLoaded(dominator.getPet().getWurmId()) == 1) {
/*      */           
/* 2183 */           dominator.getCommunicator().sendNormalServerMessage("Your pet is in a cage you must first remove it to give it away.", (byte)3);
/*      */           
/*      */           return;
/*      */         } 
/*      */       }
/* 2188 */       if (!receiver.acceptsInvitations()) {
/*      */         
/* 2190 */         dominator.getCommunicator().sendNormalServerMessage(receiver
/* 2191 */             .getName() + " does not accept pets now. " + 
/* 2192 */             LoginHandler.raiseFirstLetter(receiver.getHeSheItString()) + " needs to type /invitations in a chat window.");
/*      */         
/*      */         return;
/*      */       } 
/* 2196 */       if (dominator.getAttitude(receiver) != 2) {
/*      */         
/* 2198 */         if (receiver.getPet() == null) {
/*      */           
/* 2200 */           if (dominator.getPet() != null) {
/*      */             try {
/*      */               Skill taming;
/*      */               
/* 2204 */               if (dominator.getPet().isReborn() || dominator.getPet().isMonster()) {
/*      */                 
/* 2206 */                 dominator.getCommunicator().sendNormalServerMessage("You control that creature with your will. It can not be given away.");
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */               
/*      */               try {
/* 2213 */                 taming = receiver.getSkills().getSkill(10078);
/*      */               }
/* 2215 */               catch (NoSuchSkillException nss) {
/*      */                 
/* 2217 */                 taming = receiver.getSkills().learn(10078, 1.0F);
/*      */               } 
/* 2219 */               Skill soul = null;
/*      */               
/*      */               try {
/* 2222 */                 soul = dominator.getPet().getSkills().getSkill(105);
/*      */               }
/* 2224 */               catch (NoSuchSkillException nss) {
/*      */                 
/* 2226 */                 logger.log(Level.WARNING, dominator
/* 2227 */                     .getPet().getName() + " no soul strength when " + receiver.getName() + " tames.");
/*      */                 
/* 2229 */                 receiver.getCommunicator().sendNormalServerMessage(dominator
/* 2230 */                     .getPet().getName() + " seems untamable.");
/*      */                 return;
/*      */               } 
/* 2233 */               Skill ownsoul = null;
/*      */               
/*      */               try {
/* 2236 */                 ownsoul = receiver.getSkills().getSkill(105);
/*      */               }
/* 2238 */               catch (NoSuchSkillException nss) {
/*      */                 
/* 2240 */                 ownsoul = receiver.getSkills().learn(105, 20.0F);
/*      */               } 
/* 2242 */               boolean dryrun = (System.currentTimeMillis() - taming.lastUsed < 60000L);
/* 2243 */               float difficultyBonus = 0.0F;
/* 2244 */               if (receiver.getKingdomTemplateId() == 3)
/* 2245 */                 difficultyBonus = 20.0F; 
/* 2246 */               double bonus = Math.max(0.0D, ownsoul.skillCheck(soul.getKnowledge(0.0D), 0.0D, dryrun, 1.0F));
/* 2247 */               double power = taming.skillCheck(soul.getKnowledge(0.0D) + difficultyBonus, bonus, dryrun, 1.0F);
/* 2248 */               if (power > 0.0D) {
/*      */                 
/* 2250 */                 dominator.getPet().setLeader(null);
/* 2251 */                 receiver.setPet(dominator.getPet().getWurmId());
/* 2252 */                 dominator.getPet().setDominator(receiver.getWurmId());
/* 2253 */                 dominator.setPet(-10L);
/* 2254 */                 dominator.getCommunicator().sendNormalServerMessage("You give away your pet to " + receiver
/* 2255 */                     .getName() + ".");
/* 2256 */                 receiver.getCommunicator().sendNormalServerMessage(dominator
/* 2257 */                     .getName() + " gives you " + dominator.getHisHerItsString() + " " + receiver
/* 2258 */                     .getPet().getName());
/*      */ 
/*      */               
/*      */               }
/* 2262 */               else if (power < -30.0D) {
/*      */                 
/* 2264 */                 if (!dominator.getPet().isFighting()) {
/*      */                   
/* 2266 */                   dominator.getPet().setTarget(receiver.getWurmId(), true);
/* 2267 */                   receiver.getCommunicator().sendNormalServerMessage(dominator
/* 2268 */                       .getPet().getName() + " becomes aggravated and attacks you!");
/*      */                 } 
/* 2270 */                 dominator.getCommunicator().sendAlertServerMessage(dominator
/* 2271 */                     .getPet().getName() + " is tame no more.");
/* 2272 */                 dominator.getPet().setDominator(-10L);
/* 2273 */                 dominator.setPet(-10L);
/*      */               } else {
/*      */                 
/* 2276 */                 dominator.getCommunicator().sendAlertServerMessage(receiver
/* 2277 */                     .getName() + " fails to take control over the " + dominator
/* 2278 */                     .getPet().getName() + ".");
/*      */               }
/*      */             
/* 2281 */             } catch (Exception ex) {
/*      */               
/* 2283 */               logger.log(Level.WARNING, dominator
/* 2284 */                   .getName() + " and " + receiver.getName() + ": " + ex.getMessage(), ex);
/*      */             } 
/*      */           } else {
/*      */             
/* 2288 */             dominator.getCommunicator().sendNormalServerMessage("You don't have a pet.");
/*      */           } 
/*      */         } else {
/* 2291 */           dominator.getCommunicator().sendNormalServerMessage(receiver.getName() + " already has a pet.");
/*      */         } 
/*      */       } else {
/* 2294 */         dominator.getCommunicator().sendNormalServerMessage(receiver.getName() + " is an enemy.");
/*      */       } 
/*      */     } else {
/* 2297 */       dominator.getCommunicator().sendNormalServerMessage(receiver.getName() + " can't control a pet.");
/*      */     } 
/*      */   }
/*      */   
/*      */   static boolean findCaveExit(Creature performer, Creature orderer) {
/* 2302 */     int tilePosX = (performer.getCurrentTile()).tilex;
/* 2303 */     int tilePosY = (performer.getCurrentTile()).tiley;
/* 2304 */     int[] tiles = { tilePosX, tilePosY };
/*      */     
/* 2306 */     if (!performer.isOnSurface())
/*      */     {
/* 2308 */       if ((performer.getCurrentTile()).isTransition) {
/*      */         
/* 2310 */         performer.setLayer(0, true);
/*      */       }
/*      */       else {
/*      */         
/* 2314 */         tiles = performer.findRandomCaveExit(tiles);
/* 2315 */         tilePosX = tiles[0];
/* 2316 */         tilePosY = tiles[1];
/* 2317 */         if (tilePosX != (performer.getCurrentTile()).tilex || tilePosY != (performer.getCurrentTile()).tiley) {
/*      */           
/* 2319 */           orderer.getCommunicator().sendNormalServerMessage(performer
/* 2320 */               .getName() + " found cave exit at " + tilePosX + ", " + tilePosY);
/*      */ 
/*      */           
/*      */           try {
/* 2324 */             PathFinder pf = new PathFinder();
/* 2325 */             Path path = pf.findPath(performer, performer.getTileX(), 
/* 2326 */                 (int)performer.getStatus().getPositionY() >> 2, tilePosX, tilePosY, performer.isOnSurface(), 10);
/*      */             
/* 2328 */             if (path != null) {
/*      */ 
/*      */ 
/*      */               
/* 2332 */               while (!path.isEmpty()) {
/*      */                 
/*      */                 try
/*      */                 {
/* 2336 */                   PathTile p = path.getFirst();
/* 2337 */                   ItemFactory.createItem(344, 100.0F, ((p.getTileX() << 2) + 2), ((p
/* 2338 */                       .getTileY() << 2) + 2), 180.0F, performer.isOnSurface(), (byte)0, -10L, null);
/* 2339 */                   path.removeFirst();
/*      */                 }
/* 2341 */                 catch (FailedException fe)
/*      */                 {
/* 2343 */                   logger.log(Level.INFO, performer.getName() + " " + fe.getMessage(), (Throwable)fe);
/* 2344 */                   orderer.getCommunicator().sendNormalServerMessage("Failed to create marker.");
/*      */                 }
/* 2346 */                 catch (NoSuchTemplateException nst)
/*      */                 {
/* 2348 */                   logger.log(Level.INFO, performer.getName() + " " + nst.getMessage(), (Throwable)nst);
/* 2349 */                   orderer.getCommunicator().sendNormalServerMessage("Failed to create marker.");
/*      */                 }
/*      */               
/*      */               } 
/*      */             } else {
/*      */               
/* 2355 */               orderer.getCommunicator().sendNormalServerMessage("No path available.");
/*      */             }
/*      */           
/* 2358 */           } catch (NoPathException np) {
/*      */             
/* 2360 */             orderer.getCommunicator().sendNormalServerMessage("Failed to find path: " + np.getMessage());
/*      */           } 
/*      */         } else {
/*      */           
/* 2364 */           orderer.getCommunicator().sendNormalServerMessage("Failed to find exit");
/*      */         } 
/*      */       }  } 
/* 2367 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean stealth(Creature performer, float counter) {
/* 2372 */     boolean done = true;
/* 2373 */     if (Servers.localServer.PVPSERVER && performer.getBodyControlSkill().getRealKnowledge() < 21.0D) {
/*      */       
/* 2375 */       performer.getCommunicator().sendNormalServerMessage("You need 21 body control skill in order to stealth on PvP servers.", (byte)3);
/*      */       
/* 2377 */       return done;
/*      */     } 
/* 2379 */     if (performer.isUndead()) {
/*      */       
/* 2381 */       performer.getCommunicator().sendNormalServerMessage("Hurr hfff sss.");
/* 2382 */       return done;
/*      */     } 
/* 2384 */     if (performer.isStealth()) {
/*      */       
/* 2386 */       performer.setStealth(false);
/* 2387 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2391 */     done = false;
/* 2392 */     if (counter == 1.0F) {
/*      */       
/* 2394 */       VolaTile t = performer.getCurrentTile();
/* 2395 */       if (t != null) {
/*      */         
/* 2397 */         Set<VolaTile> set = t.getThisAndSurroundingTiles(1);
/* 2398 */         for (VolaTile lVolaTile : set) {
/*      */           
/* 2400 */           Creature[] crets = lVolaTile.getCreatures();
/* 2401 */           for (Creature lCret : crets) {
/*      */             
/* 2403 */             if (lCret != performer && lCret.getPower() <= 0) {
/*      */               
/* 2405 */               performer.getCommunicator().sendToggle(3, false);
/* 2406 */               performer.getCommunicator()
/* 2407 */                 .sendNormalServerMessage("You are too close to other creatures now.", (byte)3);
/* 2408 */               return true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2413 */       performer.sendActionControl(Actions.actionEntrys[136].getVerbString(), true, 
/* 2414 */           (int)Math.max(50.0D, (35.0D - performer.getBodyControl()) * 10.0D));
/* 2415 */       performer.getCommunicator().sendNormalServerMessage("You try to hide.");
/*      */     }
/* 2417 */     else if (counter > Math.max(5.0D, 35.0D - performer.getBodyControl())) {
/*      */       
/* 2419 */       done = true;
/* 2420 */       performer.getStatus().modifyStamina(-4000.0F);
/* 2421 */       VolaTile t = performer.getCurrentTile();
/* 2422 */       if (t != null) {
/*      */         
/* 2424 */         double mod = getStealthTerrainModifier(performer, t.tilex, t.tiley, performer.isOnSurface());
/* 2425 */         if ((Server.rand.nextFloat() * 100.0F) < mod)
/*      */         {
/* 2427 */           performer.setStealth(true);
/* 2428 */           performer.achievement(114);
/*      */           
/*      */           try {
/* 2431 */             boolean found = false;
/* 2432 */             Item lhand = performer.getBody().getBodyPart(13);
/* 2433 */             Set<Item> wornl = lhand.getItems();
/* 2434 */             if (wornl != null)
/* 2435 */               for (Item i : wornl) {
/*      */                 
/* 2437 */                 if (i.getTemplateId() == 297 && i.getMaterial() == 7) {
/*      */                   
/* 2439 */                   performer.achievement(93);
/* 2440 */                   found = true;
/*      */                   break;
/*      */                 } 
/*      */               }  
/* 2444 */             if (!found) {
/*      */               
/* 2446 */               Item rhand = performer.getBody().getBodyPart(14);
/* 2447 */               Set<Item> wornr = rhand.getItems();
/* 2448 */               if (wornr != null) {
/* 2449 */                 for (Item i : wornr) {
/*      */                   
/* 2451 */                   if (i.getTemplateId() == 297 && i.getMaterial() == 7) {
/*      */                     
/* 2453 */                     performer.achievement(93);
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */             } 
/* 2459 */           } catch (NoSpaceException noSpaceException) {}
/*      */ 
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           
/* 2467 */           performer.getCommunicator().sendToggle(3, false);
/* 2468 */           performer.getCommunicator().sendNormalServerMessage("You fail to hide.", (byte)3);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 2473 */         performer.getCommunicator().sendToggle(3, false);
/* 2474 */         performer.getCommunicator().sendNormalServerMessage("You may not hide here.", (byte)3);
/*      */       } 
/*      */     } 
/*      */     
/* 2478 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean unbrand(Creature performer, Creature target, Item source, Action act, float counter) {
/* 2484 */     if (performer == null || target == null)
/*      */     {
/* 2486 */       return true;
/*      */     }
/* 2488 */     if (!performer.isPaying()) {
/*      */       
/* 2490 */       performer.getCommunicator().sendNormalServerMessage("You need to have premium time left in order to remove a brand from creatures.", (byte)3);
/*      */       
/* 2492 */       return true;
/*      */     } 
/* 2494 */     if (!source.isOnFire()) {
/*      */       
/* 2496 */       performer.getCommunicator().sendNormalServerMessage("The " + source
/* 2497 */           .getName() + " must be very hot if you're going to remove a brand with it.", (byte)3);
/*      */       
/* 2499 */       return true;
/*      */     } 
/*      */     
/* 2502 */     if (target.isLeadable(performer) && performer.getCitizenVillage() != null)
/*      */     {
/* 2504 */       if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 
/* 2505 */           (int)(target.getStatus().getPositionZ() + target.getAltOffZ()) >> 2, 3))
/*      */       {
/* 2507 */         if (target.getCurrentVillage() != null && target.getCurrentVillage() == performer.getCitizenVillage()) {
/*      */           
/* 2509 */           if (!performer.getCitizenVillage().isActionAllowed((short)484, performer))
/* 2510 */             return true; 
/* 2511 */           Brand brand = Creatures.getInstance().getBrand(target.getWurmId());
/* 2512 */           if (brand != null) {
/*      */             
/*      */             try {
/*      */ 
/*      */               
/* 2517 */               Village villageBrand = Villages.getVillage((int)brand.getBrandId());
/* 2518 */               if (villageBrand == performer.getCitizenVillage()) {
/*      */                 
/* 2520 */                 if (counter == 1.0F) {
/*      */                   
/* 2522 */                   Server.getInstance().broadCastAction(performer
/* 2523 */                       .getName() + " prepares to remove brand on " + target.getNameWithGenus() + ".", performer, 5);
/*      */ 
/*      */                   
/* 2526 */                   performer.getCommunicator().sendNormalServerMessage("You prepare to remove brand on the " + target
/* 2527 */                       .getName() + ".");
/* 2528 */                   performer.sendActionControl(Actions.getVerbForAction((short)643), true, 100);
/* 2529 */                   return false;
/*      */                 } 
/* 2531 */                 if (act.currentSecond() == 5) {
/*      */                   
/* 2533 */                   Server.getInstance().broadCastAction(performer
/* 2534 */                       .getName() + " promptly raises " + performer.getHisHerItsString() + " " + source
/* 2535 */                       .getName() + ".", performer, 5);
/*      */                   
/* 2537 */                   performer.getCommunicator().sendNormalServerMessage("Determined, you raise your " + source
/* 2538 */                       .getName() + ".");
/* 2539 */                   return false;
/*      */                 } 
/* 2541 */                 if (counter > 10.0F) {
/*      */                   
/* 2543 */                   SoundPlayer.playSound(target.getDeathSound(), target, 1.0F);
/* 2544 */                   Server.getInstance().broadCastAction("The " + target
/* 2545 */                       .getName() + " makes a horrible sound as " + performer.getName() + " removes the brand.", performer, 5);
/*      */                   
/* 2547 */                   performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2548 */                       .getName() + " makes a horrible sound as you press your " + source
/* 2549 */                       .getName() + " against it and remove the brand.");
/*      */                   
/* 2551 */                   source.setQualityLevel(source.getQualityLevel() - 0.1F);
/* 2552 */                   brand.deleteBrand();
/* 2553 */                   if (target.getVisionArea() != null)
/*      */                   {
/* 2555 */                     target.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*      */                   }
/* 2557 */                   return true;
/*      */                 } 
/* 2559 */                 return false;
/*      */               } 
/*      */ 
/*      */               
/* 2563 */               performer.getCommunicator()
/* 2564 */                 .sendNormalServerMessage("The " + target.getName() + " already has a brand.", (byte)3);
/*      */               
/* 2566 */               return true;
/*      */             
/*      */             }
/* 2569 */             catch (NoSuchVillageException e) {
/*      */               
/* 2571 */               performer.getCommunicator()
/* 2572 */                 .sendNormalServerMessage("The " + target.getName() + " already has a brand.", (byte)3);
/*      */               
/* 2574 */               return true;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     }
/* 2580 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean brand(Creature performer, Creature target, Item source, Action act, float counter) {
/* 2586 */     boolean toReturn = true;
/* 2587 */     if (performer == null || target == null)
/*      */     {
/* 2589 */       return true;
/*      */     }
/* 2591 */     if (!performer.isPaying()) {
/*      */       
/* 2593 */       performer.getCommunicator().sendNormalServerMessage("You need to have premium time left in order to brand creatures.", (byte)3);
/*      */       
/* 2595 */       return true;
/*      */     } 
/* 2597 */     if (!source.isOnFire()) {
/*      */       
/* 2599 */       performer.getCommunicator().sendNormalServerMessage("The " + source
/* 2600 */           .getName() + " must be very hot if you're going to brand with it.", (byte)3);
/* 2601 */       return true;
/*      */     } 
/* 2603 */     if (target.isLeadable(performer) && performer.getCitizenVillage() != null)
/*      */     {
/* 2605 */       if (performer.isWithinTileDistanceTo(target.getTileX(), target.getTileY(), 
/* 2606 */           (int)(target.getStatus().getPositionZ() + target.getAltOffZ()) >> 2, 3))
/*      */       {
/* 2608 */         if (target.getCurrentVillage() != null && target.getCurrentVillage() == performer.getCitizenVillage()) {
/*      */           
/* 2610 */           if (!performer.getCitizenVillage().isActionAllowed((short)484, performer))
/* 2611 */             return true; 
/* 2612 */           Brand brand = Creatures.getInstance().getBrand(target.getWurmId());
/* 2613 */           if (brand == null) {
/*      */             
/* 2615 */             if (!Servers.isThisAPvpServer() && target.getCurrentVillage().getMaxCitizens() <= (
/* 2616 */               Creatures.getInstance().getBranded(performer.getCurrentVillage().getId())).length) {
/*      */               
/* 2618 */               performer.getCommunicator().sendNormalServerMessage("Branding this animal would exceed your settlement's branded limit.", (byte)3);
/*      */ 
/*      */               
/* 2621 */               return true;
/*      */             } 
/* 2623 */             toReturn = false;
/* 2624 */             if (counter == 1.0F)
/*      */             {
/* 2626 */               Server.getInstance().broadCastAction(performer
/* 2627 */                   .getName() + " prepares to brand " + target.getNameWithGenus() + ".", performer, 5);
/*      */               
/* 2629 */               performer.getCommunicator().sendNormalServerMessage("You prepare to brand the " + target
/* 2630 */                   .getName() + ".");
/* 2631 */               performer.sendActionControl(Actions.getVerbForAction((short)484), true, 100);
/*      */             }
/* 2633 */             else if (act.currentSecond() == 5)
/*      */             {
/* 2635 */               Server.getInstance().broadCastAction(performer
/* 2636 */                   .getName() + " promptly raises " + performer.getHisHerItsString() + " " + source
/* 2637 */                   .getName() + ".", performer, 5);
/*      */               
/* 2639 */               performer.getCommunicator().sendNormalServerMessage("Determined, you raise your " + source
/* 2640 */                   .getName() + ".");
/*      */             }
/* 2642 */             else if (counter > 10.0F)
/*      */             {
/* 2644 */               toReturn = true;
/*      */               
/* 2646 */               SoundPlayer.playSound(target.getDeathSound(), target, 1.0F);
/* 2647 */               Server.getInstance().broadCastAction("The " + target
/* 2648 */                   .getName() + " makes a horrible sound as " + performer.getName() + " brands it.", performer, 5);
/*      */               
/* 2650 */               performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2651 */                   .getName() + " makes a horrible sound as you press your " + source.getName() + " against it.");
/*      */               
/* 2653 */               source.setQualityLevel(source.getQualityLevel() - 0.1F);
/* 2654 */               Creatures.getInstance().setBrand(target.getWurmId(), performer.getCitizenVillage().getId());
/* 2655 */               if (target.getVisionArea() != null)
/*      */               {
/* 2657 */                 target.getVisionArea().broadCastUpdateSelectBar(target.getWurmId());
/*      */               }
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 2663 */             toReturn = true;
/*      */           } 
/*      */         } 
/*      */       }
/*      */     }
/* 2668 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean naturalBreed(Creature breeder, Creature target, Action act, float counter) {
/* 2673 */     boolean toReturn = true;
/* 2674 */     if (breeder == null || target == null)
/*      */     {
/* 2676 */       return true;
/*      */     }
/* 2678 */     if (breeder.mayMate(target))
/*      */     {
/* 2680 */       if (!target.isPregnant())
/*      */       {
/* 2682 */         if (!breeder.isPregnant()) {
/*      */           
/* 2684 */           if (!breeder.isWithinDistanceTo(target.getPosX(), target.getPosY(), target
/* 2685 */               .getPositionZ() + target.getAltOffZ(), 3.0F, 0.0F))
/*      */           {
/* 2687 */             return true;
/*      */           }
/* 2689 */           if (breeder.getEggTemplateId() != -1 || target.getEggTemplateId() != -1)
/*      */           {
/* 2691 */             return true;
/*      */           }
/* 2693 */           if (!breeder.isInTheMoodToBreed(false))
/*      */           {
/* 2695 */             return true;
/*      */           }
/* 2697 */           toReturn = false;
/* 2698 */           if (counter == 1.0F) {
/*      */             
/* 2700 */             Server.getInstance().broadCastAction("The " + breeder
/* 2701 */                 .getName() + " and the " + target.getName() + " gets to it!", breeder, 5);
/*      */           }
/* 2703 */           else if (counter > (30 + Server.rand.nextInt(20))) {
/*      */             
/* 2705 */             toReturn = true;
/* 2706 */             if (Server.rand.nextInt(30) == 0) {
/*      */               
/* 2708 */               if (breeder.getSex() == 1) {
/*      */                 
/* 2710 */                 breeder.mate(target, null);
/* 2711 */                 Server.getInstance().broadCastAction("Something seems to have happened between the " + breeder
/* 2712 */                     .getName() + " and the " + target
/* 2713 */                     .getName() + "!", breeder, 5);
/*      */               }
/* 2715 */               else if (target.getSex() == 1) {
/*      */                 
/* 2717 */                 target.mate(breeder, null);
/* 2718 */                 Server.getInstance().broadCastAction("Something seems to have happened between the " + breeder
/* 2719 */                     .getName() + " and the " + target
/* 2720 */                     .getName() + "!", breeder, 5);
/*      */               } 
/*      */             } else {
/*      */               
/* 2724 */               Server.getInstance().broadCastAction("The " + breeder
/* 2725 */                   .getName() + " and the " + target.getName() + " stops whatever they were doing.", breeder, 5);
/*      */             } 
/* 2727 */             target.resetBreedCounter();
/* 2728 */             breeder.resetBreedCounter();
/*      */           } 
/*      */         } 
/*      */       }
/*      */     }
/* 2733 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean breed(Creature performer, Creature target, short action, Action act, float counter) {
/* 2739 */     boolean toReturn = true;
/*      */     
/* 2741 */     if (target.isGhost()) {
/*      */       
/* 2743 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " can't get pregnant!", (byte)3);
/*      */       
/* 2745 */       return true;
/*      */     } 
/* 2747 */     if (performer.getPower() >= 5 && target.isPregnant()) {
/*      */       
/* 2749 */       if (target.checkPregnancy(true)) {
/* 2750 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " gives birth!");
/*      */       } else {
/* 2752 */         performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " fails to give birth!");
/* 2753 */       }  return true;
/*      */     } 
/* 2755 */     if ((performer.getFollowers()).length == 1) {
/*      */       
/* 2757 */       Creature breeder = performer.getFollowers()[0];
/* 2758 */       if (breeder == null) {
/*      */         
/* 2760 */         performer.getCommunicator().sendNormalServerMessage("Which creature should breed with the " + target
/* 2761 */             .getName() + "?", (byte)3);
/* 2762 */         return true;
/*      */       } 
/* 2764 */       if (!Methods.isActionAllowed(performer, (short)379))
/* 2765 */         return true; 
/* 2766 */       if (breeder.mayMate(target)) {
/*      */         
/* 2768 */         if (!target.isPregnant()) {
/*      */           
/* 2770 */           if (!breeder.isPregnant()) {
/*      */             
/* 2772 */             if (breeder.isWithinDistanceTo(target.getPosX(), target.getPosY(), target
/* 2773 */                 .getPositionZ() + target.getAltOffZ(), 3.0F, 0.0F)) {
/*      */               
/* 2775 */               BlockingResult result = Blocking.getBlockerBetween(breeder, target, 4);
/* 2776 */               if (result != null)
/*      */               {
/* 2778 */                 performer.getCommunicator().sendNormalServerMessage("The creatures are separated by the " + result
/* 2779 */                     .getFirstBlocker().getName() + ".", (byte)3);
/*      */                 
/* 2781 */                 return true;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 2786 */               performer.getCommunicator().sendNormalServerMessage("The creatures are too far apart.");
/* 2787 */               performer.sendToLoggers(breeder.getName() + " at " + breeder.getPosX() + "," + breeder.getPosY() + ", " + target
/* 2788 */                   .getName() + " at " + target.getPosX() + "," + target.getPosY() + ".");
/* 2789 */               performer.sendToLoggers(Math.abs(breeder.getStatus().getPositionX() - target.getPosX() + 0.0F) + " < " + '\003' + " is false or");
/*      */               
/* 2791 */               performer.sendToLoggers(Math.abs(breeder.getStatus().getPositionY() - target.getPosY() + 0.0F) + " < " + '\003' + " is false");
/*      */               
/* 2793 */               return true;
/*      */             } 
/* 2795 */             if (breeder.getEggTemplateId() != -1 || target.getEggTemplateId() != -1) {
/*      */               
/* 2797 */               performer.getCommunicator().sendNormalServerMessage("You can't breed egglayers.", (byte)3);
/* 2798 */               return true;
/*      */             } 
/* 2800 */             if (!breeder.isInTheMoodToBreed(true) && performer.getPower() < 5) {
/*      */               
/* 2802 */               performer.getCommunicator().sendNormalServerMessage("The " + breeder
/* 2803 */                   .getName() + " doesn't seem to be in the mood right now.", (byte)3);
/* 2804 */               return true;
/*      */             } 
/* 2806 */             if (!target.isInTheMoodToBreed(true) && performer.getPower() < 5) {
/*      */               
/* 2808 */               performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2809 */                   .getName() + " doesn't seem to be in the mood right now.", (byte)3);
/* 2810 */               return true;
/*      */             } 
/* 2812 */             toReturn = false;
/* 2813 */             target.shouldStandStill = true;
/* 2814 */             if (counter == 1.0F) {
/*      */               
/* 2816 */               act.setTimeLeft(100 + Server.rand.nextInt(300));
/* 2817 */               Server.getInstance().broadCastAction(performer
/* 2818 */                   .getName() + " does " + performer.getHisHerItsString() + " job. The " + breeder
/* 2819 */                   .getName() + " and the " + target.getName() + " get intimate!", performer, 5);
/*      */               
/* 2821 */               performer.getCommunicator().sendNormalServerMessage("The " + breeder
/*      */                   
/* 2823 */                   .getName() + " and the " + target
/*      */                   
/* 2825 */                   .getName() + " get intimate." + (
/*      */                   
/* 2827 */                   (performer.getPower() >= 5) ? (target.getBreedCounter() + ", " + breeder
/* 2828 */                   .getBreedCounter()) : ""));
/* 2829 */               if (performer.isPlayer()) {
/* 2830 */                 performer.sendActionControl(Actions.actionEntrys[379]
/* 2831 */                     .getVerbString(), true, act.getTimeLeft());
/*      */               }
/* 2833 */             } else if (performer.getPower() >= 5 || counter > (act.getTimeLeft() / 10)) {
/*      */               
/* 2835 */               toReturn = true;
/* 2836 */               Skill breeding = null;
/*      */               
/*      */               try {
/* 2839 */                 breeding = performer.getSkills().getSkill(10085);
/*      */               }
/* 2841 */               catch (NoSuchSkillException nss) {
/*      */                 
/* 2843 */                 breeding = performer.getSkills().learn(10085, 1.0F);
/*      */               } 
/* 2845 */               if (breeding.skillCheck(breeder.getBaseCombatRating(), 0.0D, false, 
/* 2846 */                   (performer.getPower() >= 5) ? 20.0F : counter) > 0.0D) {
/*      */                 
/* 2848 */                 if (breeder.getSex() == 1) {
/*      */                   
/* 2850 */                   breeder.mate(target, performer);
/* 2851 */                   performer.getCommunicator().sendNormalServerMessage("The " + breeder
/* 2852 */                       .getName() + " will probably give birth in a while!", (byte)2);
/*      */                 
/*      */                 }
/* 2855 */                 else if (target.getSex() == 1) {
/*      */                   
/* 2857 */                   target.mate(breeder, performer);
/* 2858 */                   performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2859 */                       .getName() + " will probably give birth in a while!", (byte)2);
/*      */                 
/*      */                 }
/*      */                 else {
/*      */                   
/* 2864 */                   performer
/* 2865 */                     .getCommunicator()
/* 2866 */                     .sendNormalServerMessage("Weird. You realize that since none of the creatures is female there will probably be no offspring..", (byte)3);
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/* 2871 */                 performer.achievement(531);
/*      */               } else {
/*      */                 
/* 2874 */                 performer.getCommunicator().sendNormalServerMessage("The " + breeder
/* 2875 */                     .getName() + " shys away and interrupts the action.");
/* 2876 */               }  if (performer.getPower() < 5) {
/*      */                 
/* 2878 */                 target.resetBreedCounter();
/* 2879 */                 breeder.resetBreedCounter();
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/* 2884 */             performer.getCommunicator().sendNormalServerMessage("The " + breeder
/* 2885 */                 .getName() + " is already pregnant.", (byte)3);
/*      */           } 
/*      */         } else {
/* 2888 */           performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already pregnant.", (byte)3);
/*      */         } 
/*      */       } else {
/* 2891 */         performer.getCommunicator().sendNormalServerMessage("The " + breeder
/* 2892 */             .getName() + " may not mate with " + target.getName() + ".", (byte)3);
/*      */       } 
/*      */     } else {
/* 2895 */       performer.getCommunicator().sendNormalServerMessage("Which creature should breed? You must select only one follower.", (byte)3);
/*      */     } 
/* 2897 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean ride(Creature performer, Creature target, short action) {
/* 2903 */     boolean addedPassenger = false;
/* 2904 */     boolean addedDriver = false;
/* 2905 */     if (action == 331 || action == 332)
/*      */     {
/* 2907 */       if (performer.getVehicle() == -10L) {
/*      */         
/* 2909 */         if (target.getDominator() != null && performer.getKingdomId() != target.getDominator().getKingdomId()) {
/*      */           
/* 2911 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2912 */               .getName() + " refuses to let you embark.", (byte)3);
/* 2913 */           return true;
/*      */         } 
/* 2915 */         if (target.getHitched() != null) {
/*      */           
/* 2917 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2918 */               .getName() + " is too occupied to be ridden at the moment.", (byte)3);
/* 2919 */           return true;
/*      */         } 
/* 2921 */         if (performer.isWithinDistanceTo(target.getPosX(), target.getPosY(), target
/* 2922 */             .getPositionZ() + target.getAltOffZ(), 8.0F)) {
/*      */           
/* 2924 */           if (target.getStrengthSkill() < 20.0D) {
/*      */             
/* 2926 */             performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2927 */                 .getName() + " is too weak to ride.", (byte)3);
/* 2928 */             return true;
/*      */           } 
/* 2930 */           if (target.isOnSurface() != performer.isOnSurface()) {
/*      */             
/* 2932 */             if (performer.isOnSurface()) {
/* 2933 */               performer.getCommunicator().sendNormalServerMessage("You need to enter the cave first.", (byte)3);
/*      */             } else {
/* 2935 */               performer.getCommunicator().sendNormalServerMessage("You need to leave the cave first.", (byte)3);
/* 2936 */             }  return true;
/*      */           } 
/*      */           
/* 2939 */           if (!GeneralUtilities.isOnSameLevel(performer, target)) {
/*      */             
/* 2941 */             performer.getCommunicator()
/* 2942 */               .sendNormalServerMessage("You need to be on the same floor level first.", (byte)3);
/* 2943 */             return true;
/*      */           } 
/*      */ 
/*      */           
/* 2947 */           if (performer.getBridgeId() != target.getBridgeId()) {
/*      */             
/* 2949 */             performer.getCommunicator().sendNormalServerMessage("You need to be in the same structure as the " + target
/* 2950 */                 .getName() + ".", (byte)3);
/* 2951 */             return true;
/*      */           } 
/*      */ 
/*      */           
/* 2955 */           if (!target.canUseWithEquipment()) {
/*      */             
/* 2957 */             performer.getCommunicator().sendNormalServerMessage("The " + target
/* 2958 */                 .getName() + " looks confused by its equipment and refuses to move.", (byte)3);
/* 2959 */             return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2988 */           Vehicle vehicle = Vehicles.getVehicle(target);
/* 2989 */           if (vehicle != null) {
/*      */             
/* 2991 */             String vehicName = Vehicle.getVehicleName(vehicle);
/* 2992 */             if (action == 331) {
/*      */               
/* 2994 */               if (target.getHitched() != null) {
/*      */                 
/* 2996 */                 String text = "You can not control the %s while it is in front of the %s.";
/* 2997 */                 performer.getCommunicator().sendNormalServerMessage(
/* 2998 */                     StringUtil.format("You can not control the %s while it is in front of the %s.", new Object[] {
/* 2999 */                         target.getName(), 
/* 3000 */                         Vehicle.getVehicleName(target.getHitched())
/*      */                       }), (byte)3);
/* 3002 */                 return false;
/*      */               } 
/* 3004 */               if (performer.getDraggedItem() != null) {
/*      */                 
/* 3006 */                 String text = "You may not drag %s while trying to control the %s.";
/* 3007 */                 performer.getCommunicator().sendNormalServerMessage(
/* 3008 */                     StringUtil.format("You may not drag %s while trying to control the %s.", new Object[] {
/* 3009 */                         performer.getDraggedItem().getNameWithGenus(), vehicName
/*      */                       }), (byte)3);
/*      */                 
/* 3012 */                 return false;
/*      */               } 
/* 3014 */               if (VehicleBehaviour.mayDriveVehicle(performer, target)) {
/*      */                 
/* 3016 */                 if (VehicleBehaviour.canBeDriverOfVehicle(performer, vehicle))
/*      */                 {
/* 3018 */                   addedDriver = false;
/* 3019 */                   for (int x = 0; x < vehicle.seats.length; x++) {
/*      */                     
/* 3021 */                     if (!vehicle.seats[x].isOccupied() && (vehicle.seats[x]).type == 0) {
/*      */ 
/*      */                       
/* 3024 */                       float r = -(target.getStatus().getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 3025 */                       float s = (float)Math.sin(r);
/* 3026 */                       float c = (float)Math.cos(r);
/* 3027 */                       float xo = s * -(vehicle.seats[x]).offx - c * -(vehicle.seats[x]).offy;
/* 3028 */                       float yo = c * -(vehicle.seats[x]).offx + s * -(vehicle.seats[x]).offy;
/* 3029 */                       float newposx = target.getPosX() + xo;
/* 3030 */                       float newposy = target.getPosY() + yo;
/* 3031 */                       BlockingResult result = Blocking.getBlockerBetween(performer, performer
/* 3032 */                           .getPosX(), performer.getPosY(), newposx, newposy, performer
/* 3033 */                           .getPositionZ(), target.getPositionZ(), performer
/* 3034 */                           .isOnSurface(), target.isOnSurface(), false, 4, -1L, performer
/* 3035 */                           .getBridgeId(), target.getBridgeId(), false);
/* 3036 */                       if (result != null) {
/*      */                         
/* 3038 */                         performer.getCommunicator().sendNormalServerMessage("The " + result
/* 3039 */                             .getFirstBlocker().getName() + " is in the way. You can not reach the seat.", (byte)3);
/*      */ 
/*      */                         
/* 3042 */                         return true;
/*      */                       } 
/* 3044 */                       addedDriver = true;
/* 3045 */                       vehicle.seats[x].occupy(vehicle, performer);
/* 3046 */                       vehicle.pilotId = performer.getWurmId();
/* 3047 */                       performer.setVehicleCommander(true);
/* 3048 */                       MountAction m = new MountAction(target, null, vehicle, x, true, (vehicle.seats[x]).offz);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/* 3056 */                       if (performer.getVisionArea() != null)
/* 3057 */                         performer.getVisionArea().broadCastUpdateSelectBar(performer.getWurmId(), true); 
/* 3058 */                       performer.setMountAction(m);
/* 3059 */                       performer.setVehicle(target.getWurmId(), true, (byte)0);
/*      */ 
/*      */                       
/* 3062 */                       if (target.getTemplateId() == 3)
/* 3063 */                         performer.achievement(521); 
/* 3064 */                       if (target.getTemplateId() == 64)
/* 3065 */                         performer.achievement(535); 
/* 3066 */                       if (target.getTemplateId() == 12 || target
/* 3067 */                         .getTemplateId() == 42 || target
/* 3068 */                         .getTemplateId() == 49)
/* 3069 */                         performer.achievement(545); 
/* 3070 */                       if (target.getTemplateId() == 21) {
/* 3071 */                         performer.achievement(594);
/*      */                       }
/*      */                       break;
/*      */                     } 
/*      */                   } 
/* 3076 */                   if (!addedDriver)
/*      */                   {
/* 3078 */                     String text = "You may not %s the %s as a %s right now. The space is occupied.";
/* 3079 */                     performer.getCommunicator().sendNormalServerMessage(
/* 3080 */                         StringUtil.format("You may not %s the %s as a %s right now. The space is occupied.", new Object[] { vehicle.embarkString, vehicName, vehicle.pilotName }), (byte)3);
/*      */                   
/*      */                   }
/*      */ 
/*      */                 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */                   
/* 3089 */                   String text = "You are not agile enough to control the %s. You need %.2f in %s.";
/* 3090 */                   performer.getCommunicator().sendNormalServerMessage(
/* 3091 */                       StringUtil.format("You are not agile enough to control the %s. You need %.2f in %s.", new Object[] {
/*      */                           
/* 3093 */                           vehicName, Float.valueOf(vehicle.skillNeeded), 
/* 3094 */                           SkillSystem.getNameFor(104)
/*      */                         }), (byte)3);
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 3100 */                 String text = "You are not allowed to %s the %s as a %s.";
/* 3101 */                 performer.getCommunicator().sendNormalServerMessage(
/* 3102 */                     StringUtil.format("You are not allowed to %s the %s as a %s.", new Object[] { vehicle.embarkString, vehicName, vehicle.pilotName }), (byte)3);
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/* 3109 */             else if (action == 332) {
/*      */               
/* 3111 */               if (VehicleBehaviour.mayEmbarkVehicle(performer, target))
/*      */               {
/* 3113 */                 if (performer.getDraggedItem() == null) {
/*      */                   
/* 3115 */                   addedPassenger = false;
/*      */ 
/*      */ 
/*      */                   
/* 3119 */                   boolean wallInWay = false;
/* 3120 */                   for (int x = 0; x < vehicle.seats.length; x++) {
/*      */                     
/* 3122 */                     if (!vehicle.seats[x].isOccupied() && (vehicle.seats[x]).type == 1) {
/*      */ 
/*      */                       
/* 3125 */                       float r = -(target.getStatus().getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 3126 */                       float s = (float)Math.sin(r);
/* 3127 */                       float c = (float)Math.cos(r);
/* 3128 */                       float xo = s * -(vehicle.seats[x]).offx - c * -(vehicle.seats[x]).offy;
/* 3129 */                       float yo = c * -(vehicle.seats[x]).offx + s * -(vehicle.seats[x]).offy;
/* 3130 */                       float newposx = target.getPosX() + xo;
/* 3131 */                       float newposy = target.getPosY() + yo;
/*      */                       
/* 3133 */                       BlockingResult result = Blocking.getBlockerBetween(performer, performer
/* 3134 */                           .getPosX(), performer.getPosY(), newposx, newposy, performer
/* 3135 */                           .getPositionZ(), target.getPositionZ(), performer.isOnSurface(), target
/* 3136 */                           .isOnSurface(), false, 4, -1L, performer.getBridgeId(), target
/* 3137 */                           .getBridgeId(), false);
/*      */                       
/* 3139 */                       if (result == null) {
/*      */                         
/* 3141 */                         addedPassenger = true;
/* 3142 */                         vehicle.seats[x].occupy(vehicle, performer);
/* 3143 */                         MountAction m = new MountAction(target, null, vehicle, x, false, (vehicle.seats[x]).offz);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/* 3151 */                         if (performer.getVisionArea() != null) {
/* 3152 */                           performer.getVisionArea().broadCastUpdateSelectBar(performer.getWurmId(), true);
/*      */                         }
/* 3154 */                         performer.setMountAction(m);
/* 3155 */                         performer.setVehicle(target.getWurmId(), true, (byte)1);
/*      */                         
/*      */                         break;
/*      */                       } 
/* 3159 */                       wallInWay = true;
/*      */                     } 
/*      */                   } 
/* 3162 */                   if (!addedPassenger)
/*      */                   {
/* 3164 */                     String text = "You may not %s the %s as a passenger right now.";
/* 3165 */                     performer.getCommunicator().sendNormalServerMessage(
/* 3166 */                         StringUtil.format("You may not %s the %s as a passenger right now.", new Object[] { vehicle.embarkString, vehicName }));
/*      */ 
/*      */                     
/* 3169 */                     if (wallInWay) {
/* 3170 */                       performer.getCommunicator().sendNormalServerMessage("The wall is in the way. You can not reach a seat.", (byte)3);
/*      */                     } else {
/*      */                       
/* 3173 */                       performer.getCommunicator().sendNormalServerMessage("The seats are all occupied.", (byte)3);
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 
/*      */                 } else {
/*      */                   
/* 3180 */                   String text = "You may not %s the %s while dragging something.";
/* 3181 */                   performer.getCommunicator().sendNormalServerMessage(
/* 3182 */                       StringUtil.format("You may not %s the %s while dragging something.", new Object[] { vehicle.embarkString, vehicName }), (byte)3);
/*      */                 
/*      */                 }
/*      */ 
/*      */               
/*      */               }
/*      */               else
/*      */               {
/* 3190 */                 String text = "You are not allowed to %s the %s.";
/* 3191 */                 performer.getCommunicator()
/* 3192 */                   .sendNormalServerMessage(
/* 3193 */                     StringUtil.format("You are not allowed to %s the %s.", new Object[] { vehicle.embarkString, vehicName }), (byte)3);
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 3199 */             performer.getCommunicator().sendNormalServerMessage("You can't embark on that right now.", (byte)3);
/*      */           } 
/*      */         } else {
/*      */           
/* 3203 */           performer.getCommunicator().sendNormalServerMessage("You are too far away now.", (byte)3);
/*      */         } 
/*      */       } 
/*      */     }
/* 3207 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float getStealthTerrainModifier(Creature performer, int tilex, int tiley, boolean surfaced) {
/* 3213 */     float mod = 0.0F;
/*      */     
/* 3215 */     VolaTile t = null;
/* 3216 */     for (int x = -1; x <= 1; x++) {
/* 3217 */       for (int y = -1; y <= 1; y++) {
/*      */         
/* 3219 */         t = Zones.getTileOrNull(tilex + x, tiley + y, surfaced);
/* 3220 */         if (t != null) {
/*      */           
/* 3222 */           if ((t.getWalls()).length > 0)
/* 3223 */             mod += 5.0F; 
/* 3224 */           if (t.getFences() != null)
/* 3225 */             mod += 5.0F; 
/*      */         } 
/* 3227 */         if (surfaced)
/*      */         {
/* 3229 */           if (tilex > 0 && tilex < Zones.worldTileSizeX - 1 && tiley > 0 && tiley < Zones.worldTileSizeY - 1) {
/*      */             
/* 3231 */             int tt = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/* 3232 */             byte type = Tiles.decodeType(tt);
/* 3233 */             byte data = Tiles.decodeData(tt);
/* 3234 */             Tiles.Tile theTile = Tiles.getTile(type);
/* 3235 */             if (theTile.isTree())
/*      */             {
/* 3237 */               if (FoliageAge.getAgeAsByte(data) > FoliageAge.MATURE_TWO.getAgeId())
/* 3238 */                 mod += 5.0F; 
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 3244 */     if (!surfaced) {
/* 3245 */       mod = (float)(mod + 10.0D + performer.getBodyControl());
/* 3246 */     } else if (WurmCalendar.isNight()) {
/* 3247 */       mod = (float)(mod + 20.0D + performer.getBodyControl());
/* 3248 */     } else if (WurmCalendar.isMorning()) {
/* 3249 */       mod = (float)(mod + 5.0D + performer.getBodyControl() / 4.0D);
/* 3250 */     }  if (performer.getBestLightsource() != null)
/* 3251 */       mod -= 10.0F; 
/* 3252 */     if (performer.isRoyalExecutioner())
/* 3253 */       mod += 10.0F; 
/* 3254 */     return mod * ItemBonus.getStealthBonus(performer);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean groom(Creature performer, Creature target, Item groomTool, short action, Action act, float counter) {
/* 3260 */     boolean toReturn = true;
/*      */     
/* 3262 */     if (target.isGhost()) {
/*      */       
/* 3264 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " can't be groomed!", (byte)3);
/*      */       
/* 3266 */       return true;
/*      */     } 
/* 3268 */     if (!Methods.isActionAllowed(performer, action))
/* 3269 */       return true; 
/* 3270 */     if (target.canBeGroomed()) {
/*      */       
/* 3272 */       toReturn = false;
/* 3273 */       Skill breeding = null;
/*      */       
/*      */       try {
/* 3276 */         breeding = performer.getSkills().getSkill(10085);
/*      */       }
/* 3278 */       catch (NoSuchSkillException nss) {
/*      */         
/* 3280 */         breeding = performer.getSkills().learn(10085, 1.0F);
/*      */       } 
/* 3282 */       int time = act.getTimeLeft();
/* 3283 */       target.shouldStandStill = true;
/* 3284 */       if (counter == 1.0F) {
/*      */         
/* 3286 */         time = Actions.getSlowActionTime(performer, breeding, groomTool, 0.0D);
/* 3287 */         act.setTimeLeft(time);
/* 3288 */         Server.getInstance().broadCastAction(performer.getName() + " starts to tend to a " + target.getName() + ".", performer, 5);
/*      */         
/* 3290 */         performer.getCommunicator().sendNormalServerMessage("You start to tend to " + target.getName() + ".");
/* 3291 */         performer.sendActionControl(Actions.actionEntrys[398].getVerbString(), true, act
/* 3292 */             .getTimeLeft());
/* 3293 */         performer.getStatus().modifyStamina(-500.0F);
/* 3294 */         return toReturn;
/*      */       } 
/*      */       
/* 3297 */       if (act.currentSecond() % 5 == 2)
/*      */       {
/* 3299 */         SoundPlayer.playSound("sound.work.horse.groom", performer, 1.0F);
/*      */       }
/* 3301 */       if (act.currentSecond() % 15 == 0) {
/*      */         
/* 3303 */         int r = Server.rand.nextInt(10);
/* 3304 */         String toSend = "You talk comfortingly to " + target.getName() + ".";
/* 3305 */         if (r == 0)
/* 3306 */           toSend = "You pat the " + target.getName() + " reassuringly."; 
/* 3307 */         if (r == 1)
/* 3308 */           toSend = "The " + target.getName() + " flinches."; 
/* 3309 */         if (r == 2)
/* 3310 */           toSend = "You remove some dirt from the " + target.getName() + "."; 
/* 3311 */         if (r == 3)
/* 3312 */           toSend = "You find a flea on " + target.getName() + " that you dispose of."; 
/* 3313 */         if (r == 4)
/* 3314 */           toSend = "You stroke " + target.getName() + " over the back."; 
/* 3315 */         if (r == 6)
/* 3316 */           toSend = "You check the feet of the " + target.getName() + " and make sure all is in order."; 
/* 3317 */         if (r == 7)
/* 3318 */           toSend = "The " + target.getName() + " gives you a nervous look. What did you do now?"; 
/* 3319 */         if (r == 8)
/* 3320 */           toSend = "The " + target.getName() + " seems to really enjoy the treatment."; 
/* 3321 */         if (r == 9)
/* 3322 */           toSend = "You pick a tick from " + target.getName() + " and squish it. Slick!"; 
/* 3323 */         performer.getCommunicator().sendNormalServerMessage(toSend);
/* 3324 */         performer.getStatus().modifyStamina(-1500.0F);
/*      */       }
/* 3326 */       else if (performer.getPower() >= 5 || counter * 10.0F > act.getTimeLeft()) {
/*      */         
/* 3328 */         toReturn = true;
/* 3329 */         if (breeding.skillCheck(target.getBaseCombatRating(), groomTool, 0.0D, false, 
/* 3330 */             (performer.getPower() >= 5) ? 20.0F : (counter / 2.0F)) > 0.0D) {
/*      */           
/* 3332 */           target.setLastGroomed(System.currentTimeMillis());
/* 3333 */           Server.getInstance().broadCastAction(performer
/* 3334 */               .getName() + " finishes tending to the " + target.getName() + ".", performer, 5);
/* 3335 */           performer.getCommunicator().sendNormalServerMessage("You have now tended to " + target
/* 3336 */               .getName() + " and " + target.getHeSheItString() + " seems pleased.");
/*      */           
/* 3338 */           groomTool.setDamage(groomTool.getDamage() + 0.02F * groomTool.getDamageModifier());
/*      */         } else {
/*      */           
/* 3341 */           performer.getCommunicator().sendNormalServerMessage("The " + target
/* 3342 */               .getName() + " shys away and interrupts the action.", (byte)3);
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 3347 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is already well tended.", (byte)3);
/*      */     } 
/* 3349 */     return toReturn;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MethodsCreatures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
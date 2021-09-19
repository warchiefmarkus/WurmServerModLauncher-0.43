/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.questions.CultQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.io.IOException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
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
/*      */ public final class Cults
/*      */   implements TimeConstants, MiscConstants
/*      */ {
/*      */   public static final byte PATH_NONE = 0;
/*      */   public static final byte PATH_LOVE = 1;
/*      */   public static final byte PATH_HATE = 2;
/*      */   public static final byte PATH_KNOWLEDGE = 3;
/*      */   public static final byte PATH_INSANITY = 4;
/*      */   public static final byte PATH_POWER = 5;
/*   58 */   private static final String[] YES_NO_STRINGS = new String[] { "Yes", "No" };
/*      */ 
/*      */ 
/*      */   
/*   62 */   private static final Logger logger = Logger.getLogger(Cults.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte getPathFor(int tilex, int tiley, int layer, int performerHeight) {
/*   73 */     byte path = 0;
/*   74 */     if (layer >= 0) {
/*      */ 
/*      */ 
/*      */       
/*   78 */       short[] steepness = Creature.getTileSteepness(tilex, tiley, (layer >= 0));
/*   79 */       if (steepness[1] > 10)
/*   80 */         return 0; 
/*      */     } 
/*   82 */     int[][] closeTiles = getCloseTiles(tilex, tiley, layer);
/*   83 */     Map<Byte, Integer> dominatingType = new HashMap<>();
/*   84 */     boolean nearWater = false;
/*      */     
/*   86 */     boolean quickDropoff = false;
/*      */     
/*   88 */     boolean belowRockfall = false;
/*      */ 
/*      */     
/*   91 */     boolean insanityBlocker = false;
/*      */ 
/*      */     
/*   94 */     boolean hateEnabler = false;
/*   95 */     int flowerTiles = 0;
/*   96 */     int thornTiles = 0;
/*   97 */     for (int[] lCloseTile : closeTiles) {
/*      */       
/*   99 */       for (int y = 0; y < closeTiles.length; y++) {
/*      */         
/*  101 */         boolean detectedWater = false;
/*  102 */         byte type = Tiles.decodeType(lCloseTile[y]);
/*  103 */         Tiles.Tile theTile = Tiles.getTile(type);
/*  104 */         Integer nums = dominatingType.get(Byte.valueOf(type));
/*  105 */         if (nums == null)
/*  106 */           nums = Integer.valueOf(0); 
/*  107 */         if (layer < 0) {
/*      */           
/*  109 */           if (Tiles.isOreCave(type)) {
/*  110 */             insanityBlocker = true;
/*  111 */           } else if (type == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*  112 */             insanityBlocker = true;
/*      */           } 
/*  114 */         } else if (type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_TUNDRA.id || type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_TAR.id) {
/*      */           
/*  116 */           hateEnabler = true;
/*  117 */         }  nums = Integer.valueOf(nums.intValue() + 1);
/*  118 */         dominatingType.put(Byte.valueOf(type), nums);
/*  119 */         short s = Tiles.decodeHeight(lCloseTile[y]);
/*  120 */         int i = performerHeight - s;
/*  121 */         if (s < -5.0F) {
/*  122 */           detectedWater = true;
/*  123 */         } else if (i < -100) {
/*  124 */           belowRockfall = true;
/*  125 */         } else if (i > 100 && !nearWater) {
/*  126 */           quickDropoff = true;
/*  127 */         }  if (detectedWater)
/*  128 */           nearWater = true; 
/*  129 */         if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_REED.id) {
/*      */           
/*  131 */           GrassData.FlowerType f = GrassData.FlowerType.decodeTileData(Tiles.decodeData(lCloseTile[y]));
/*  132 */           if (f != GrassData.FlowerType.NONE) {
/*  133 */             flowerTiles++;
/*      */           }
/*  135 */         } else if (theTile.isMyceliumBush()) {
/*      */           
/*  137 */           if (theTile.isThorn(Tiles.decodeData(lCloseTile[y])))
/*  138 */             thornTiles++; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  142 */     if (layer < 0) {
/*      */ 
/*      */       
/*  145 */       if (!insanityBlocker)
/*  146 */         return 4; 
/*  147 */       return 0;
/*      */     } 
/*  149 */     byte domType = Tiles.Tile.TILE_GRASS.id;
/*  150 */     byte secondType = Tiles.Tile.TILE_GRASS.id;
/*  151 */     int maxCount = 0;
/*  152 */     for (Map.Entry<Byte, Integer> e : dominatingType.entrySet()) {
/*      */       
/*  154 */       int count = ((Integer)e.getValue()).intValue();
/*  155 */       if (count > maxCount) {
/*      */         
/*  157 */         secondType = domType;
/*  158 */         domType = ((Byte)e.getKey()).byteValue();
/*  159 */         maxCount = count;
/*      */       } 
/*      */     } 
/*      */     
/*  163 */     boolean farWater = false;
/*  164 */     int westTile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex + 20), Zones.safeTileY(tiley));
/*  165 */     short height = Tiles.decodeHeight(westTile);
/*  166 */     int diff = performerHeight - height;
/*  167 */     if (height < -5.0F) {
/*  168 */       farWater = true;
/*  169 */     } else if (diff < -400) {
/*  170 */       belowRockfall = true;
/*  171 */     } else if (diff > 400) {
/*  172 */       quickDropoff = true;
/*  173 */     }  int northTile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex), Zones.safeTileY(tiley - 20));
/*  174 */     height = Tiles.decodeHeight(northTile);
/*  175 */     diff = performerHeight - height;
/*  176 */     if (height < -5.0F) {
/*  177 */       farWater = true;
/*  178 */     } else if (diff < -400) {
/*  179 */       belowRockfall = true;
/*  180 */     } else if (diff > 400) {
/*  181 */       quickDropoff = true;
/*  182 */     }  int southTile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex), Zones.safeTileY(tiley + 20));
/*  183 */     height = Tiles.decodeHeight(southTile);
/*  184 */     diff = performerHeight - height;
/*  185 */     if (height < -5.0F) {
/*  186 */       farWater = true;
/*  187 */     } else if (diff < -400) {
/*  188 */       belowRockfall = true;
/*  189 */     } else if (diff > 400) {
/*  190 */       quickDropoff = true;
/*  191 */     }  int eastTile = Server.surfaceMesh.getTile(Zones.safeTileX(tilex - 20), Zones.safeTileY(tiley));
/*  192 */     height = Tiles.decodeHeight(eastTile);
/*  193 */     diff = performerHeight - height;
/*  194 */     if (height < -5.0F) {
/*  195 */       farWater = true;
/*  196 */     } else if (diff < -400) {
/*  197 */       belowRockfall = true;
/*  198 */     } else if (diff > 400) {
/*  199 */       quickDropoff = true;
/*      */     } 
/*      */     
/*  202 */     if (domType == Tiles.Tile.TILE_MOSS.id || domType == Tiles.Tile.TILE_CLAY.id || domType == Tiles.Tile.TILE_TUNDRA.id)
/*  203 */       return 0; 
/*  204 */     if (secondType == Tiles.Tile.TILE_MOSS.id || secondType == Tiles.Tile.TILE_CLAY.id || secondType == Tiles.Tile.TILE_TUNDRA.id) {
/*  205 */       return 0;
/*      */     }
/*  207 */     if (performerHeight > 2000) {
/*      */       
/*  209 */       if (!belowRockfall)
/*      */       {
/*  211 */         if (domType == Tiles.Tile.TILE_ROCK.id || domType == Tiles.Tile.TILE_CLIFF.id || quickDropoff)
/*  212 */           return 5; 
/*      */       }
/*  214 */       return 0;
/*      */     } 
/*      */     
/*  217 */     if (Tiles.getTile(domType).isMycelium() && !nearWater)
/*      */     {
/*  219 */       if (hateEnabler || thornTiles > 3) {
/*  220 */         return 2;
/*      */       }
/*      */     }
/*  223 */     if (farWater && nearWater) {
/*      */ 
/*      */       
/*  226 */       if (belowRockfall || quickDropoff)
/*  227 */         return 3; 
/*  228 */       if (domType == Tiles.Tile.TILE_SAND.id)
/*      */       {
/*  230 */         return 3;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  235 */     if (!nearWater && farWater && secondType == Tiles.Tile.TILE_GRASS.id)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  243 */       if (flowerTiles > 4)
/*  244 */         path = 1; 
/*      */     }
/*  246 */     return path;
/*      */   }
/*      */ 
/*      */   
/*      */   static final int[][] getCloseTiles(int tilex, int tiley, int layer) {
/*  251 */     int[][] toReturn = new int[5][5];
/*  252 */     for (int x = -2; x < 3; x++) {
/*      */       
/*  254 */       for (int y = -2; y < 3; y++) {
/*      */ 
/*      */         
/*      */         try {
/*  258 */           if (layer >= 0) {
/*  259 */             toReturn[x + 2][y + 2] = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/*      */           } else {
/*  261 */             toReturn[x + 2][y + 2] = Server.caveMesh.getTile(tilex + x, tiley + y);
/*      */           } 
/*  263 */         } catch (Exception ex) {
/*      */           
/*  265 */           toReturn[x + 2][y + 2] = -100;
/*      */         } 
/*      */       } 
/*      */     } 
/*  269 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean meditate(Creature performer, int layer, Action aAct, float counter, Item rug) {
/*  275 */     int tilex = performer.getTileX();
/*  276 */     int tiley = performer.getTileY();
/*  277 */     boolean done = false;
/*  278 */     if (performer.getPositionZ() + performer.getAltOffZ() < 0.1F) {
/*      */       
/*  280 */       performer.getCommunicator().sendNormalServerMessage("You can not meditate here. It is too wet.");
/*  281 */       return true;
/*      */     } 
/*  283 */     if (!rug.isMeditation()) {
/*      */       
/*  285 */       performer.getCommunicator().sendNormalServerMessage("You can't meditate with that.");
/*  286 */       return true;
/*      */     } 
/*      */     
/*  289 */     byte path = getPathFor(performer.getTileX(), performer.getTileY(), layer, (int)(performer.getPositionZ() * 10.0F));
/*  290 */     Skill meditation = null;
/*      */     
/*      */     try {
/*  293 */       meditation = performer.getSkills().getSkill(10086);
/*      */     }
/*  295 */     catch (NoSuchSkillException nss) {
/*      */       
/*  297 */       meditation = performer.getSkills().learn(10086, 1.0F);
/*      */     } 
/*  299 */     boolean increaseSkill = true;
/*  300 */     if (performer.getPower() == 0 && Math.abs(performer.getMeditateX() - performer.getTileX()) < 10 && 
/*  301 */       Math.abs(performer.getMeditateY() - performer.getTileY()) < 10) {
/*      */       
/*  303 */       if (counter == 1.0F) {
/*  304 */         performer.getCommunicator().sendNormalServerMessage("You recently meditated here and need to find new insights somewhere else.");
/*      */       }
/*  306 */       increaseSkill = false;
/*      */     } 
/*  308 */     Cultist cultist = Cultist.getCultist(performer.getWurmId());
/*      */     
/*  310 */     if (cultist == null)
/*      */     {
/*  312 */       if (meditation.getKnowledge(0.0D) >= 20.0D) {
/*      */         
/*  314 */         increaseSkill = false;
/*  315 */         if (counter == 1.0F) {
/*  316 */           performer.getCommunicator().sendNormalServerMessage("You need to find deeper mysteries in order to advance your skill in meditation now.");
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  322 */     int time = aAct.getTimeLeft();
/*  323 */     if (counter == 1.0F) {
/*      */       
/*  325 */       time = 110 + Server.rand.nextInt(20);
/*  326 */       if (performer.getPower() < 5)
/*  327 */         time *= 10; 
/*  328 */       aAct.setTimeLeft(time);
/*      */       
/*  330 */       String tosend = "You start meditating.";
/*  331 */       if (performer.getPower() >= 5)
/*  332 */         tosend = tosend + " Path is " + getPathNameFor(path); 
/*  333 */       performer.getCommunicator().sendNormalServerMessage(tosend);
/*  334 */       Server.getInstance().broadCastAction(performer.getName() + " starts meditating.", performer, 5);
/*  335 */       performer.sendActionControl(Actions.actionEntrys[384].getVerbString(), true, time);
/*  336 */       performer.playAnimation("meditate", false);
/*      */     }
/*  338 */     else if (aAct.currentSecond() == 5) {
/*      */       
/*  340 */       float difficulty = 5.0F;
/*  341 */       if (path != 0 && increaseSkill)
/*  342 */         difficulty = 10.0F; 
/*  343 */       if (!increaseSkill)
/*  344 */         difficulty = 20.0F; 
/*  345 */       if (meditation.skillCheck(difficulty, rug, 0.0D, true, 1.0F + rug.getRarity() * 0.1F) < 0.0D) {
/*      */         
/*  347 */         performer.getCommunicator().sendNormalServerMessage("You fail to relax.");
/*  348 */         if (performer.getPower() == 5) {
/*  349 */           performer.getCommunicator().sendNormalServerMessage("Difficulty=" + difficulty + " increaseSkill=" + increaseSkill + " cultist is null?" + ((cultist == null) ? 1 : 0));
/*      */         }
/*      */         
/*  352 */         return true;
/*      */       } 
/*  354 */       byte level = 0;
/*  355 */       if (cultist != null)
/*  356 */         level = cultist.getLevel(); 
/*  357 */       performer.getCommunicator().sendNormalServerMessage("You fall into a trance. " + 
/*  358 */           getMeditationString(path, level, tilex, tiley));
/*      */     } 
/*  360 */     if (counter * 10.0F > time) {
/*      */       
/*  362 */       if (performer.getPower() == 5)
/*      */       {
/*  364 */         if (performer.getLayer() < 0) {
/*  365 */           path = 4;
/*      */         } else {
/*      */           
/*  368 */           switch (performer.getKingdomTemplateId()) {
/*      */             
/*      */             case 3:
/*  371 */               path = 2;
/*      */               break;
/*      */             case 1:
/*  374 */               path = 3;
/*      */               break;
/*      */             case 4:
/*  377 */               path = 1;
/*      */               break;
/*      */             case 2:
/*  380 */               path = 5;
/*      */               break;
/*      */           } 
/*      */ 
/*      */ 
/*      */         
/*      */         } 
/*      */       }
/*  388 */       done = true;
/*  389 */       double power = 0.0D;
/*  390 */       float difficulty = 5.0F;
/*  391 */       performer.getCommunicator().sendNormalServerMessage("You finish your meditation.");
/*  392 */       Server.getInstance().broadCastAction(performer
/*  393 */           .getName() + " finishes " + performer.getHisHerItsString() + " meditation.", performer, 5);
/*  394 */       if (cultist != null)
/*      */       {
/*  396 */         if (meditation.getKnowledge(0.0D) >= 20.0D) {
/*      */           
/*  398 */           long timeSinceLast = System.currentTimeMillis() - cultist.getLastMeditated();
/*      */ 
/*      */ 
/*      */           
/*  402 */           if (increaseSkill && cultist
/*  403 */             .getSkillgainCount() > 0 && timeSinceLast > (Servers.localServer.isChallengeServer() ? 1800000L : 10800000L))
/*      */           {
/*  405 */             cultist.decreaseSkillGain();
/*      */           }
/*      */ 
/*      */           
/*  409 */           if (timeSinceLast < 1800000L || cultist.getSkillgainCount() >= 5) {
/*      */             
/*  411 */             increaseSkill = false;
/*      */           }
/*  413 */           else if (increaseSkill) {
/*      */ 
/*      */ 
/*      */             
/*  417 */             cultist.increaseSkillgain();
/*  418 */             if (cultist.getSkillgainCount() >= 5) {
/*  419 */               performer.getCommunicator().sendNormalServerMessage("You feel that it will take you a while before you are ready to meditate again.");
/*      */             }
/*  421 */             cultist.setLastMeditated();
/*      */           } 
/*      */           
/*      */           try {
/*  425 */             cultist.saveCultist(false);
/*      */           }
/*  427 */           catch (IOException iox) {
/*      */             
/*  429 */             logger.log(Level.WARNING, performer.getName() + iox.getMessage(), iox);
/*      */           } 
/*      */         } 
/*      */       }
/*  433 */       if (meditation.skillCheck(95.0D, rug, 0.0D, !increaseSkill, 1.0F + rug.getRarity() * 0.1F) > 0.0D) {
/*      */         
/*  435 */         float nut = (50 + Server.rand.nextInt(49)) / 100.0F;
/*  436 */         if (performer.getStatus().refresh(nut, false))
/*  437 */           performer.getCommunicator().sendNormalServerMessage("You feel invigorated."); 
/*      */       } 
/*  439 */       performer.achievement(103);
/*      */ 
/*      */       
/*  442 */       boolean mayIncreaseLevel = false;
/*  443 */       long timeToNextLevel = -1L;
/*  444 */       if (path != 0) {
/*      */         
/*  446 */         difficulty = 10.0F;
/*      */         
/*  448 */         if (cultist != null) {
/*      */           
/*  450 */           timeToNextLevel = cultist.getTimeLeftToIncreasePath(System.currentTimeMillis(), meditation.getKnowledge(0.0D));
/*  451 */           if (timeToNextLevel <= 0L) {
/*      */             
/*  453 */             if ((cultist.getLevel() * 10) - meditation.getKnowledge(0.0D) < 30.0D || meditation.getKnowledge(0.0D) > 90.0D)
/*      */             {
/*  455 */               mayIncreaseLevel = true;
/*  456 */               difficulty = (1 + cultist.getLevel() * 10);
/*      */             }
/*      */             else
/*      */             {
/*  460 */               difficulty = (1 + cultist.getLevel() * 3);
/*  461 */               performer
/*  462 */                 .getCommunicator()
/*  463 */                 .sendNormalServerMessage("You have a hard time to focus and no question comes to you. You probably need higher meditation skill.");
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  469 */             performer.getCommunicator().sendNormalServerMessage(getWaitForProgressMessage(timeToNextLevel));
/*  470 */             if (performer.getPower() == 5)
/*      */             {
/*  472 */               mayIncreaseLevel = true;
/*  473 */               difficulty = (1 + cultist.getLevel() * 10);
/*  474 */               timeToNextLevel = 0L;
/*  475 */               performer.getCommunicator().sendNormalServerMessage(".. but what the heck. Go on, man. Go on.");
/*      */             }
/*      */           
/*      */           } 
/*  479 */         } else if (meditation.getKnowledge(0.0D) >= 15.0D) {
/*      */           
/*  481 */           mayIncreaseLevel = true;
/*      */         }
/*      */         else {
/*      */           
/*  485 */           performer.getCommunicator().sendNormalServerMessage("You should return here when you have become better at meditating.");
/*      */         } 
/*      */       } 
/*      */       
/*  489 */       power = meditation.skillCheck(difficulty, rug, 0.0D, !increaseSkill, counter / 3.0F * (1.0F + rug.getRarity() * 0.1F));
/*      */       
/*  491 */       performer.setMeditateX(performer.getTileX());
/*  492 */       performer.setMeditateY(performer.getTileY());
/*      */       
/*  494 */       rug.setDamage(rug.getDamage() + 0.1F * rug.getDamageModifier());
/*      */       
/*  496 */       if (path != 0) {
/*      */         
/*  498 */         if (path != 4)
/*  499 */           performer.getCommunicator().sendNormalServerMessage("This is indeed a special place."); 
/*  500 */         if (mayIncreaseLevel)
/*      */         {
/*  502 */           if (power > 0.0D) {
/*      */             
/*  504 */             if (cultist == null) {
/*      */               
/*  506 */               CultQuestion cq = new CultQuestion(performer, "Following a path", "Do you wish to embark on a journey?", timeToNextLevel, null, path, false, false);
/*      */               
/*  508 */               cq.sendQuestion();
/*      */             }
/*  510 */             else if (path == cultist.getPath()) {
/*      */ 
/*      */               
/*  513 */               if (cultist.getLevel() < 6 || performer.isPaying())
/*      */               {
/*  515 */                 CultQuestion cq = new CultQuestion(performer, "Following a path", "Are you ready to proceed?", timeToNextLevel, cultist, path, false, false);
/*      */                 
/*  517 */                 cq.sendQuestion();
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  522 */               performer.getCommunicator().sendNormalServerMessage("You gain no inspiration here for your own path.");
/*      */             }
/*      */           
/*      */           }
/*  526 */           else if (performer.getPower() == 5) {
/*  527 */             performer.getCommunicator().sendNormalServerMessage("Power of skillroll is " + power + " for difficulty " + difficulty + ".");
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  532 */     return done;
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
/*      */   static String getWaitForProgressMessage(long timeToNextLevel) {
/*      */     String message;
/*  547 */     if (timeToNextLevel > 1814400000L) {
/*  548 */       message = "You still need to let your recent progress sink in for weeks to come.";
/*  549 */     } else if (timeToNextLevel > 1209600000L) {
/*  550 */       message = "You still need to let your recent progress sink in for a few weeks.";
/*  551 */     } else if (timeToNextLevel > 604800000L) {
/*  552 */       message = "You still need to let your recent progress sink in for more than a week.";
/*  553 */     } else if (timeToNextLevel > 259200000L) {
/*  554 */       message = "You still need to let your recent progress sink in for about a week.";
/*  555 */     } else if (timeToNextLevel > 86400000L) {
/*  556 */       message = "You still need to let your recent progress sink in for a few days.";
/*  557 */     } else if (timeToNextLevel > 43200000L) {
/*  558 */       message = "You still need to let your recent progress sink in for many hours.";
/*  559 */     } else if (timeToNextLevel > 10800000L) {
/*  560 */       message = "You still need to let your recent progress sink in for several hours.";
/*  561 */     } else if (timeToNextLevel > 0L) {
/*  562 */       message = "You still need to let your recent progress sink in for a few hours.";
/*      */     } else {
/*  564 */       message = "";
/*  565 */     }  return message;
/*      */   }
/*      */   
/*      */   public static final String getMeditationString(byte path, byte level, int tilex, int tiley) {
/*      */     int max, baseRand, pathForMessage;
/*  570 */     Random random = new Random();
/*  571 */     random.setSeed(((tilex << 16) + tiley));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  576 */     if (Server.rand.nextInt(4) != 0) {
/*      */       
/*  578 */       pathForMessage = 0;
/*      */       
/*  580 */       baseRand = random.nextInt(60);
/*  581 */       max = 60;
/*      */     }
/*      */     else {
/*      */       
/*  585 */       pathForMessage = path;
/*  586 */       max = 20;
/*  587 */       baseRand = random.nextInt(21);
/*      */     } 
/*      */ 
/*      */     
/*  591 */     int rand = baseRand - 2 + Server.rand.nextInt(5);
/*  592 */     if (rand < 0)
/*  593 */       rand += max; 
/*  594 */     if (rand > max) {
/*  595 */       rand -= max;
/*      */     }
/*      */     
/*  598 */     switch (pathForMessage)
/*      */     
/*      */     { case 0:
/*  601 */         medString = getMeditationStringForNoPath(rand);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  622 */         return medString;case 1: medString = getMeditationStringForLovePath(rand); return medString;case 5: medString = getMeditationStringForPowerPath(rand); return medString;case 2: medString = getMeditationStringForHatePath(rand); return medString;case 4: medString = getMeditationStringForInsanityPath(rand); return medString;case 3: medString = getMeditationStringForKnowledgePath(rand); return medString; }  String medString = "You start meditating over recent events."; return medString;
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
/*      */   static String getMeditationStringForKnowledgePath(int id) {
/*  636 */     switch (id)
/*      */     
/*      */     { case 0:
/*  639 */         medString = "You think about reflections and shadows.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  705 */         return medString;case 1: medString = "You wonder if the future exists but we only see parts of it like a mountainrange."; return medString;case 2: medString = "You try to recall if someone said that the gods don't play dice."; return medString;case 3: medString = "You wonder if you always try to decide on the best option."; return medString;case 4: medString = "You think about the next thing you want to learn."; return medString;case 5: medString = "You think of the things that interest you most."; return medString;case 6: medString = "You wonder if there is something really interesting out there that you haven't heard of."; return medString;case 7: medString = "You envision yourself teaching to the masses."; return medString;case 8: medString = "You wonder what you would say if you got to tell the whole world one sentence of truth."; return medString;case 9: medString = "You wonder how you would study and learn a book of lies."; return medString;case 10: medString = "What lies have you been told this week?"; return medString;case 11: medString = "You think about your barriers for learning and how to remove them."; return medString;case 12: medString = "You wonder if it is possible to find knowledge that in no possible situation would save lives."; return medString;case 13: medString = "What would you do if you possessed most knowledge in the world?"; return medString;case 14: medString = "If you could become best in the world at anything, what would that be?"; return medString;case 15: medString = "You think of the various ways people become the best at something and whether you should adopt some style."; return medString;case 16: medString = "You wonder if time can move backwards somehow."; return medString;case 17: medString = "How come people who ought to know so much often are proven wrong?"; return medString;case 18: medString = "Should you trust someone more because the person sounds more convincing?"; return medString;case 19: medString = "Do you often lie or take chances when you are asked to speak?"; return medString;case 20: medString = "Does history always repeat itself?"; return medString; }  String medString = "You start meditating over recent events."; return medString;
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
/*      */   static String getMeditationStringForInsanityPath(int id) {
/*  719 */     switch (id)
/*      */     
/*      */     { case 0:
/*  722 */         medString = "Are they after you?";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  788 */         return medString;case 1: medString = "Who was that?"; return medString;case 2: medString = "When? When when when?"; return medString;case 3: medString = "It sure is dark."; return medString;case 4: medString = "Where does eternity begin?"; return medString;case 5: medString = "Am I here, really?"; return medString;case 6: medString = "What is this smell?"; return medString;case 7: medString = "Isn't life really pretty disgusting?"; return medString;case 8: medString = "Who built the mountains?"; return medString;case 9: medString = "You consider whether you are supposed to be here."; return medString;case 10: medString = "Is all this a coincidence?"; return medString;case 11: medString = "You try to fathom the number of grains of sand in the world."; return medString;case 12: medString = "Is this all part of a plan?"; return medString;case 13: medString = "You try to figure out what your moral obligations are."; return medString;case 14: medString = "You think about how people can torture each other and who decides who will live and who will die."; return medString;case 15: medString = "The thought that someone is watching strikes you. "; return medString;case 16: medString = "You think of all the people that decide your fate."; return medString;case 17: medString = "A sense of hopelessness suddenly strikes."; return medString;case 18: medString = "You feel like laughing like a donkey."; return medString;case 19: medString = "You decide not to breathe at all. Does the future already exist?"; return medString;case 20: medString = "You are beset by thoughts of demons."; return medString; }  String medString = "You start meditating over recent events."; return medString;
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
/*      */   static String getMeditationStringForHatePath(int id) {
/*  802 */     switch (id)
/*      */     
/*      */     { case 0:
/*  805 */         medString = "You wonder why you never got that gift.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  871 */         return medString;case 1: medString = "Are they after you?"; return medString;case 2: medString = "You think about how you can do most damage in the least time."; return medString;case 3: medString = "You think about how destruction gives control."; return medString;case 4: medString = "What is the effect of fear?"; return medString;case 5: medString = "You wonder if you are afraid of something."; return medString;case 6: medString = "You think of armies gathering to crush the foe."; return medString;case 7: medString = "Where does your hate come from?"; return medString;case 8: medString = "What is the purpose of your hate?"; return medString;case 9: medString = "How can you make your hate grow?"; return medString;case 10: medString = "What triggers your aggression?"; return medString;case 11: medString = "How is aggression good for you?"; return medString;case 12: medString = "What happens if you use violence?"; return medString;case 13: medString = "Am I prepared to kill someone?"; return medString;case 14: medString = "Do I want to replace my hate?"; return medString;case 15: medString = "You wonder how you can use aggression."; return medString;case 16: medString = "You consider if hate really something is else."; return medString;case 17: medString = "Who controls my hate?"; return medString;case 18: medString = "Who is in control of me?"; return medString;case 19: medString = "You think of what your options today are."; return medString;case 20: medString = "You think of the faces of the people you hate or have hated."; return medString; }  String medString = "You start meditating over recent events."; return medString;
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
/*      */   static String getMeditationStringForPowerPath(int id) {
/*  885 */     switch (id)
/*      */     
/*      */     { case 0:
/*  888 */         medString = "You think of aggression and violence.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  954 */         return medString;case 1: medString = "You envision fire along the horizon. Dark clouds above."; return medString;case 2: medString = "Who is more worthy of power? You or your neighbour?"; return medString;case 3: medString = "What happens if all the bad people get all the gold?"; return medString;case 4: medString = "Do I have responsibility to gain power?"; return medString;case 5: medString = "What will happen to me if I never take control of my life?"; return medString;case 6: medString = "You think 'Do plans ever work? Do I need a plan?'"; return medString;case 7: medString = "How long plans can I make and do I wish to?"; return medString;case 8: medString = "Why do some people become rich?"; return medString;case 9: medString = "Do I want to be famous?"; return medString;case 10: medString = "You think of heroes and whether you want to be one."; return medString;case 11: medString = "You think of ways to make your neighbour do as you wish."; return medString;case 12: medString = "You envision a straight path through a forest and a tunnel through a distant mountain."; return medString;case 13: medString = "You try to hear the thundering waves of a stormy sea."; return medString;case 14: medString = "You consider the speed and effects of lightning."; return medString;case 15: medString = "You meditate upon the power of the bear."; return medString;case 16: medString = "You think of the things you can not buy for money."; return medString;case 17: medString = "You think about why we use money."; return medString;case 18: medString = "You think about hurricanes and volcanoes and how it would feel to be one."; return medString;case 19: medString = "The image of a small baby surrounded by snakes comes to mind."; return medString;case 20: medString = "You feel full of energy, like the sun."; return medString; }  String medString = "You start meditating over recent events."; return medString;
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
/*      */   static String getMeditationStringForLovePath(int id) {
/*  968 */     switch (id)
/*      */     
/*      */     { case 0:
/*  971 */         medString = "You think about colorful flowers.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1037 */         return medString;case 1: medString = "You recall the smile of a baby."; return medString;case 2: medString = "You wonder if you can punish with love."; return medString;case 3: medString = "Is it possible to kill for love?"; return medString;case 4: medString = "Who loves the most?"; return medString;case 5: medString = "What is love anyway?"; return medString;case 6: medString = "Is love a feeling or a need?"; return medString;case 7: medString = "If I give, do I do it for feeling good myself or for others?"; return medString;case 8: medString = "The image of a sprout growing through hard dirt comes to mind."; return medString;case 9: medString = "The bliss of rain on a hot summer day."; return medString;case 10: medString = "You think about the worst person you have met."; return medString;case 11: medString = "You think about the most perfect person you have met."; return medString;case 12: medString = "When I forgive, do I really do it for myself?"; return medString;case 13: medString = "Is letting go good or bad? Can I overcome my fears?"; return medString;case 14: medString = "If I let my loved one be ruined, how could I live with myself?"; return medString;case 15: medString = "Is love possible if I constantly fear rejection?"; return medString;case 16: medString = "You think about soft words and whispers."; return medString;case 17: medString = "Is it right to try to control the one you love?"; return medString;case 18: medString = "You think about how domination can destroy love and how some people want to be dominated."; return medString;case 19: medString = "Do people grow taller from love?"; return medString;case 20: medString = "You wonder if the people in your hometown are like people in other towns."; return medString; }  String medString = "You start meditating over recent events."; return medString;
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
/*      */   static String getMeditationStringForNoPath(int id) {
/* 1051 */     switch (id)
/*      */     
/*      */     { case 0:
/* 1054 */         medString = "You consider the wind and the places it goes.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1240 */         return medString;case 1: medString = "You feel the earth and what walks on it."; return medString;case 2: medString = "You think about the sea and the creatures that swim in it."; return medString;case 3: medString = "The light in a drop of water."; return medString;case 4: medString = "You think about the craftsmen, tinkering and toiling."; return medString;case 5: medString = "You ponder the miners, digging deep in the darkness of the mountains."; return medString;case 6: medString = "The sky lifts your heart."; return medString;case 7: medString = "You try to think of nothing here."; return medString;case 8: medString = "This place reminds you of home for some reason."; return medString;case 9: medString = "You try to recall all the faces you have met."; return medString;case 10: medString = "You think about rain."; return medString;case 11: medString = "You listen to the wind."; return medString;case 12: medString = "You think about sunshine."; return medString;case 13: medString = "You wonder how far you can go."; return medString;case 14: medString = "The thought of the highest mountain makes your mind wander."; return medString;case 15: medString = "The vision of slow flowing rivers calms your mind."; return medString;case 16: medString = "Is it really the same river the next time you step into it?"; return medString;case 17: medString = "What makes trees grow?"; return medString;case 18: medString = "Which path should you choose today?"; return medString;case 19: medString = "You focus on breathing."; return medString;case 20: medString = "You try to clear your mind."; return medString;case 21: medString = "Is there a smallest particle?"; return medString;case 22: medString = "What do the gods want?"; return medString;case 23: medString = "What is your spirit made of?"; return medString;case 24: medString = "Where does my soul live as I leave my body?"; return medString;case 25: medString = "Is it possible to move along the chain of time?"; return medString;case 26: medString = "Can you freeze water with thoughts?"; return medString;case 27: medString = "Aren't most things very predictive?"; return medString;case 28: medString = "Is there a physical representation of chance?"; return medString;case 29: medString = "Does everything have a shadow somewhere?"; return medString;case 30: medString = "If something sees light as something else, how does it look?"; return medString;case 31: medString = "What if light and dark is the same thing, just in different shapes."; return medString;case 32: medString = "It sure is a nice day."; return medString;case 33: medString = "Is fear and hate the same thing?"; return medString;case 34: medString = "Is love a large hole that needs to be filled or is it something else?"; return medString;case 35: medString = "Who has the right to lock someone up?"; return medString;case 36: medString = "Can you picture a situation where you could kill someone?"; return medString;case 37: medString = "Are you all that honest?"; return medString;case 38: medString = "When you judge and limit someone, does that limit you as well?"; return medString;case 39: medString = "Do you believe in what people say or what people do?"; return medString;case 40: medString = "Will you care about what happens after you are dead?"; return medString;case 41: medString = "Does hurting someone really hurt you more?"; return medString;case 42: medString = "Is it better to protect a child or let it risk a severe bruise now and then?"; return medString;case 43: medString = "Can something good come from small children fighting with each other?"; return medString;case 44: medString = "Should children really be allowed to sort out their indifferences themselves?"; return medString;case 45: medString = "Are there people who are soulless?"; return medString;case 46: medString = "Do you know anyone who has had a perfect childhood?"; return medString;case 47: medString = "Maybe we all have our problems."; return medString;case 48: medString = "Who decides who is allowed to complain about something?"; return medString;case 49: medString = "Do you taint people by complaining about things to them?"; return medString;case 50: medString = "Does what you do have any effect at all?"; return medString;case 51: medString = "What is the difference between words spoken and words thought?"; return medString;case 52: medString = "What is the difference between an arrow fired and an arrow sheathed?"; return medString;case 53: medString = "What is the smallest thing you can do that has effect on another person?"; return medString;case 54: medString = "May your mere appearance change the course of events?"; return medString;case 55: medString = "Should you accept when someone destroys what you love?"; return medString;case 56: medString = "Do you let too many things slip?"; return medString;case 57: medString = "Do you try to control things you should not or can not?"; return medString;case 58: medString = "Can you change anything in your life for the better?"; return medString;case 59: medString = "Does meditating really help anything?"; return medString;case 60: medString = "Should you be doing something completely different right now?"; return medString; }  String medString = "You start meditating over recent events."; return medString;
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
/*      */   public static final String getNameForLevel(byte path, byte level) {
/* 1256 */     switch (path)
/*      */     
/*      */     { case 2:
/* 1259 */         name = getNameForLevelForHatePath(level);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1278 */         return name;case 1: name = getNameForLevelForLovePath(level); return name;case 3: name = getNameForLevelForKnowledgePath(level); return name;case 5: name = getNameForLevelForPowerPath(level); return name;case 4: name = getNameForLevelForInsanityPath(level); return name; }  String name = "uninitiated"; return name;
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
/*      */   static String getNameForLevelForInsanityPath(byte level) {
/*      */     String name;
/* 1293 */     if (level > 11)
/* 1294 */     { name = level + "th Eidolon"; }
/*      */     else
/* 1296 */     { switch (level)
/*      */       
/*      */       { case 1:
/* 1299 */           name = "Initiate";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1335 */           return name;case 2: name = "Disturbed"; return name;case 3: name = "Crazed"; return name;case 4: name = "Deranged"; return name;case 5: name = "Sicko"; return name;case 6: name = "Mental"; return name;case 7: name = "Psycho"; return name;case 8: name = "Beast"; return name;case 9: name = "Maniac"; return name;case 10: name = "Drooling"; return name;case 11: name = "Gone"; return name; }  name = "uninitiated"; }  return name;
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
/*      */   static String getNameForLevelForPowerPath(byte level) {
/*      */     String name;
/* 1350 */     if (level > 11)
/* 1351 */     { name = level + "th Sovereign"; }
/*      */     else
/* 1353 */     { switch (level)
/*      */       
/*      */       { case 1:
/* 1356 */           name = "Initiate";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1392 */           return name;case 2: name = "Gatherer"; return name;case 3: name = "Greedy"; return name;case 4: name = "Strong"; return name;case 5: name = "Released"; return name;case 6: name = "Unafraid"; return name;case 7: name = "Brave"; return name;case 8: name = "Performer"; return name;case 9: name = "Liberator"; return name;case 10: name = "Force"; return name;case 11: name = "Vibrant Light"; return name; }  name = "uninitiated"; }  return name;
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
/*      */   static String getNameForLevelForKnowledgePath(byte level) {
/*      */     String name;
/* 1406 */     if (level > 11)
/* 1407 */     { name = level + "th Hierophant"; }
/*      */     else
/* 1409 */     { switch (level)
/*      */       
/*      */       { case 1:
/* 1412 */           name = "Initiate";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1448 */           return name;case 2: name = "Eager"; return name;case 3: name = "Explorer"; return name;case 4: name = "Sheetfolder"; return name;case 5: name = "Desertmind"; return name;case 6: name = "Observer"; return name;case 7: name = "Bookkeeper"; return name;case 8: name = "Mud-dweller"; return name;case 9: name = "Thought Eater"; return name;case 10: name = "Crooked"; return name;case 11: name = "Enlightened"; return name; }  name = "uninitiated"; }  return name;
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
/*      */   static String getNameForLevelForLovePath(byte level) {
/*      */     String name;
/* 1462 */     if (level > 11)
/* 1463 */     { name = level + "th Deva"; }
/*      */     else
/* 1465 */     { switch (level)
/*      */       
/*      */       { case 1:
/* 1468 */           name = "Initiate";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1504 */           return name;case 2: name = "Nice"; return name;case 3: name = "Gentle"; return name;case 4: name = "Warm"; return name;case 5: name = "Goodhearted"; return name;case 6: name = "Giving"; return name;case 7: name = "Rock"; return name;case 8: name = "Splendid"; return name;case 9: name = "Protector"; return name;case 10: name = "Respectful"; return name;case 11: name = "Saint"; return name; }  name = "uninitiated"; }  return name;
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
/*      */   static String getNameForLevelForHatePath(byte level) {
/*      */     String name;
/* 1518 */     if (level > 11)
/* 1519 */     { name = level + "th Harbinger"; }
/*      */     else
/* 1521 */     { switch (level)
/*      */       
/*      */       { case 1:
/* 1524 */           name = "Initiate";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1560 */           return name;case 2: name = "Ridiculous"; return name;case 3: name = "Envious"; return name;case 4: name = "Hateful"; return name;case 5: name = "Finger"; return name;case 6: name = "Sheep"; return name;case 7: name = "Snake"; return name;case 8: name = "Shark"; return name;case 9: name = "Infection"; return name;case 10: name = "Swarm"; return name;case 11: name = "Free"; return name; }  name = "uninitiated"; }  return name;
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
/*      */   public static final String getQuestionForLevel(byte path, byte level) {
/* 1574 */     String question = "Is this the question?";
/* 1575 */     switch (path)
/*      */     
/*      */     { case 2:
/* 1578 */         question = getQuestionForLevelForHatePath(level);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1596 */         return question;case 3: question = getQuestionForLevelForKnowledgePath(level); return question;case 5: question = getQuestionForLevelForPowerPath(level); return question;case 4: question = getQuestionForLevelForInsanityPath(level); return question;case 1: question = getQuestionForLevelForLovePath(level); return question; }  question = "Do you wish to follow a path?"; return question;
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
/*      */   static String getQuestionForLevelForHatePath(byte level) {
/* 1610 */     switch (level)
/*      */     
/*      */     { case 0:
/* 1613 */         question = "What is hate?";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1670 */         return question;case 1: question = "What is the best thing that comes from hate?"; return question;case 2: question = "What is the worst thing that hate can bring?"; return question;case 3: question = "What type of hate is the weakest?"; return question;case 4: question = "How can I best strengthen my hate?"; return question;case 5: question = "What is the best use of my hate?"; return question;case 6: question = "Who is in control of my aggression?"; return question;case 7: question = "How do I best control my hate?"; return question;case 8: question = "What is the best effect of displaying aggression?"; return question;case 9: question = "When should I get rid of my hate?"; return question;case 10: question = "When have I mastered my hate?"; return question;case 11: question = "What is the most beneficial aspect of blind hate?"; return question;case 12: question = "Which is the most important enemy to strike down?"; return question;case 13: question = "Which of these is most successful in controlling any number of people?"; return question;case 14: question = "Which of these diminishes the power of hate most?"; return question;case 15: question = "What will increase the effect of your hate most?"; return question;case 16: question = "What is the difference between hate and anger?"; return question;case 17: question = "Which hate is the strongest?"; return question; }  String question = "Is this the question?"; return question;
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
/*      */   static String getQuestionForLevelForKnowledgePath(byte level) {
/* 1684 */     switch (level)
/*      */     
/*      */     { case 0:
/* 1687 */         question = "What is knowledge?";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1744 */         return question;case 1: question = "What best constitutes a fact?"; return question;case 2: question = "Is there any absolute truth?"; return question;case 3: question = "How do I best prepare myself to learn things?"; return question;case 4: question = "What is the most important product of knowledge?"; return question;case 5: question = "What constitutes a professional?"; return question;case 6: question = "Which of these is the most certain way to rise above the crowd?"; return question;case 7: question = "What do I do when I have too little knowledge to make the best choice?"; return question;case 8: question = "How important is knowledge of history?"; return question;case 9: question = "What is required to make good decisions?"; return question;case 10: question = "What will make my knowledge useless?"; return question;case 11: question = "Which of these is a requirement for truly working knowledge?"; return question;case 12: question = "What knowledge is most valuable?"; return question;case 13: question = "What is the best path to understanding a particular human behaviour?"; return question;case 14: question = "What is the best path to understanding a particular function in society?"; return question;case 15: question = "What is the ultimate purpose of gaining knowledge?"; return question;case 16: question = "Who of these has the most valuable knowledge?"; return question;case 17: question = "Why does society care about knowledge?"; return question; }  String question = "Is this the question?"; return question;
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
/*      */   static String getQuestionForLevelForPowerPath(byte level) {
/* 1758 */     switch (level)
/*      */     
/*      */     { case 0:
/* 1761 */         question = "What is the ultimate purpose of power?";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1818 */         return question;case 1: question = "What is money?"; return question;case 2: question = "In large amounts, what of these things is the most certain route to power for an unknown person?"; return question;case 3: question = "What is the main reason that people strive for power and money?"; return question;case 4: question = "What is the safest way of getting rid of your enemy?"; return question;case 5: question = "If power is a sword, what do I need to swing it?"; return question;case 6: question = "What is the biggest risk of my gaining power?"; return question;case 7: question = "How do I best control other people while releasing their energy?"; return question;case 8: question = "Will I loose my power if I am dishonest?"; return question;case 9: question = "Why am I in power?"; return question;case 10: question = "Which of these are blocked from achieving ultimate power?"; return question;case 11: question = "Which of these is the least working tactic when bringing someone down socially?"; return question;case 12: question = "Which is most powerful?"; return question;case 13: question = "How do you best assume control of a democracy?"; return question;case 14: question = "How do you best assume control of the whole world?"; return question;case 15: question = "How do you best control your partner?"; return question;case 16: question = "What is the main reason society works?"; return question;case 17: question = "What is the purpose of violence?"; return question; }  String question = "Is this the question?"; return question;
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
/*      */   static String getQuestionForLevelForInsanityPath(byte level) {
/* 1832 */     switch (level)
/*      */     
/*      */     { case 0:
/* 1835 */         question = "How come some people are rich and some are poor?";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1892 */         return question;case 1: question = "Who has most control of my fate?"; return question;case 2: question = "If I win the lottery, what determined it?"; return question;case 3: question = "A horse slips and kills the rider. What happened?"; return question;case 4: question = "Who of these are usually hiding something"; return question;case 5: question = "Do I have a free will?"; return question;case 6: question = "Must I treat others as I want to be treated myself?"; return question;case 7: question = "These roads, mines and houses are"; return question;case 8: question = "Who usually has most incentive to attack a normal person like me?"; return question;case 9: question = "What best describes humankind?"; return question;case 10: question = "What best describes my life?"; return question;case 11: question = "Who committed the crime?"; return question;case 12: question = "He worries about someone therefor he ___ the person"; return question;case 13: question = "The poor"; return question;case 14: question = "Good people will often"; return question;case 15: question = "You have no friends because you are"; return question;case 16: question = "You should always vote for"; return question;case 17: question = "It is better to"; return question; }  String question = "Is this the question?"; return question;
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
/*      */   static String getQuestionForLevelForLovePath(byte level) {
/* 1906 */     switch (level)
/*      */     
/*      */     { case 0:
/* 1909 */         question = "What is love?";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1966 */         return question;case 1: question = "Without which can love not survive?"; return question;case 2: question = "Which of these negates love?"; return question;case 3: question = "How do I love someone more?"; return question;case 4: question = "What makes love grow most?"; return question;case 5: question = "What should I say to best strengthen existing love?"; return question;case 6: question = "What is the best reason to ask someone I love to change?"; return question;case 7: question = "What do I need to do to love everyone?"; return question;case 8: question = "Which of these is the biggest failure in love?"; return question;case 9: question = "What of these will ultimately make love fail?"; return question;case 10: question = "What is the biggest act of love?"; return question;case 11: question = "Are there people who can love you no matter what?"; return question;case 12: question = "What do I really have to fight in myself in order to be a loving person?"; return question;case 13: question = "What best describes a business?"; return question;case 14: question = "What best describes society?"; return question;case 15: question = "Being which of these gives you least friends?"; return question;case 16: question = "Which of these will always block love?"; return question;case 17: question = "Which of these is a side effect of love?"; return question; }  String question = "Is this the question?"; return question;
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
/*      */   public static final String[] getAnswerAlternativesForLevel(byte path, byte level) {
/* 1981 */     switch (path)
/*      */     
/*      */     { case 4:
/* 1984 */         alts = getAnswerAlternativesForLevelForInsanityPath(level);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2002 */         return alts;case 5: alts = getAnswerAlternativesForLevelForPowerPath(level); return alts;case 1: alts = getAnswerAlternativesForLevelForLovePath(level); return alts;case 2: alts = getAnswerAlternativesForLevelForHatePath(level); return alts;case 3: alts = getAnswerAlternativesForLevelForKnowledgePath(level); return alts; }  String[] alts = YES_NO_STRINGS; return alts;
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
/*      */   static String[] getAnswerAlternativesForLevelForInsanityPath(byte level) {
/* 2016 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2019 */         alts = new String[] { "It is natural", "Life is unfair", "Some people are unlucky", "There are not enough resources", "Who knows?" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2098 */         return alts;case 1: alts = new String[] { "I have", "People around me", "The stars", "The natural laws", "The government" }; return alts;case 2: alts = new String[] { "The number on the ticket", "The person who pulled the ticket", "Simple luck", "The events leading up to the draw", "World history" }; return alts;case 3: alts = new String[] { "World history lead to this", "Chance struck", "The situation called for it", "Nothing unusual", "Natural laws" }; return alts;case 4: alts = new String[] { "A loud person", "A hostile person", "A silent person", "A happy person", "A sad person" }; return alts;case 5: alts = new String[] { "Yes", "No", "I will answer some other day", "Maybe", "I refuse to answer" }; return alts;case 6: alts = new String[] { "No", "Sometimes I should", "Usually I should", "It works best for me", "Always" }; return alts;case 7: alts = new String[] { "Beautiful", "Natural", "Detestable", "An abomination", "Basically unnatural" }; return alts;case 8: alts = new String[] { "The mother", "A sister", "A neighbour", "The government", "A mob" }; return alts;case 9: alts = new String[] { "Animals", "Evil", "Good", "Natural", "Destructive" }; return alts;case 10: alts = new String[] { "A bit of a success", "Totally meaningless", "At least I am alive", "Fun", "Tragic" }; return alts;case 11: alts = new String[] { "The ex convict", "The whore", "The foreigner", "The poor guy", "The rich guy" }; return alts;case 12: alts = new String[] { "cares about", "loathes", "diminishes", "upsets", "lies about" }; return alts;case 13: alts = new String[] { "are more envious and dangerous", "are more common and popular", "don't have to be protected", "are more trustworthy", "have more to lose" }; return alts;case 14: alts = new String[] { "help you", "hurt you", "love you", "ignore you", "pay you" }; return alts;case 15: alts = new String[] { "ill", "shy", "superior", "dangerous", "annoying" }; return alts;case 16: alts = new String[] { "what benefits everyone else most", "your own idea", "someone elses idea", "what benefits you most", "nothing" }; return alts;case 17: alts = new String[] { "stick to your principles", "sugarcoat", "pay your friends off", "annoy your friends", "party" }; return alts; }  String[] alts = YES_NO_STRINGS; return alts;
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
/*      */   static String[] getAnswerAlternativesForLevelForPowerPath(byte level) {
/* 2112 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2115 */         alts = new String[] { "To become insanely rich", "To dominate other creatures", "To get away with everything", "To enjoy the small things in life", "To achieve personal freedom" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2203 */         return alts;case 1: alts = new String[] { "Money is mainly control", "You can buy everything with money", "Money is mainly oppression", "Money is mainly energy", "Money is only numbers" }; return alts;case 2: alts = new String[] { "Fear", "Money", "Knowledge", "Love", "Cooperation" }; return alts;case 3: alts = new String[] { "To push down their competitors", "To gain control over their lives", "To control others", "To become rich", "They act like animals" }; return alts;case 4: alts = new String[] { "Infiltrating them", "Harassing them", "Buying them", "Conquer them", "Letting them destroy themselves" }; return alts;case 5: alts = new String[] { "Intelligence", "Faith", "Courage", "Hate", "Money" }; return alts;case 6: alts = new String[] { "That I become a coward", "That I hurt someone", "That I go silent", "That I waste resources", "That I stop listening" }; return alts;case 7: alts = new String[] { "Remove their feeling of self sufficiency", "Terrorize them", "Give them what they want", "Give them limited power", "Ignore them" }; return alts;case 8: alts = new String[] { "Usually not", "Never", "Most often", "Always", "There is no such thing as dishonesty" }; return alts;case 9: alts = new String[] { "I was appointed", "I am lucky", "I am better", "I paid for it", "I took it" }; return alts;case 10: alts = new String[] { "Someone stupid", "A coward", "Someone nice", "A murderer", "None" }; return alts;case 11: alts = new String[] { "Telling poisonous jokes about the person", "Spreading big lies about the person", "Spreading small lies about the person", "Showing how you worry about the person", "Ridiculing the person" }; return alts;case 12: alts = new String[] { "Being able to kill", "Controlling people", "Being very rich", "Being free of riches and dependancies", "Being impervious to personal attacks" }; return alts;case 13: alts = new String[] { "Build a system of coherent ideology that can't be refuted", "Prey on peoples inner fears", "Use weapons and money", "Run for office using a reasonable philosophic base with solutions to contemporary problems", "Run for office vocalizing single popular issues" }; return alts;case 14: alts = new String[] { "Use your own country to conquer it by force", "Buy country after country", "Assume control of some super-state organization and increase its influence", "Join beings from outside of the world", "Use religion" }; return alts;case 15: alts = new String[] { "More Money", "Igniting Love", "Overwhelming Power", "Secret Knowledge", "Superior Sex" }; return alts;case 16: alts = new String[] { "Monopoly of Violence", "Secure Trade", "Coordination of Efforts", "Accumulation of Knowledge", "Taxes" }; return alts;case 17: alts = new String[] { "To impress", "There is no reason", "To extract information", "To achieve a purpose", "To diminish" }; return alts; }  String[] alts = YES_NO_STRINGS; return alts;
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
/*      */   static String[] getAnswerAlternativesForLevelForLovePath(byte level) {
/* 2217 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2220 */         alts = new String[] { "It is a spring", "It is a hole", "It is a shackle", "It is fragile glass", "It is a sword" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2303 */         return alts;case 1: alts = new String[] { "Trust", "Respect", "Hate", "Death", "Beauty" }; return alts;case 2: alts = new String[] { "Power", "Desire", "Hate", "Contempt", "Distrust" }; return alts;case 3: alts = new String[] { "I hate", "I demand", "I accept", "I desire", "I pursue" }; return alts;case 4: alts = new String[] { "Power", "Gifts", "Fear", "Time", "Kisses" }; return alts;case 5: alts = new String[] { "I respect you", "I hate you", "I need you", "I want you", "I love you" }; return alts;case 6: alts = new String[] { "To destroy the relationship", "To preserve balance", "To make the person grow", "To gain independence", "To assert control" }; return alts;case 7: alts = new String[] { "Test everyone", "Know everyone", "Distrust everyone", "Hate everyone", "Accept humankind" }; return alts;case 8: alts = new String[] { "Not protecting your love", "Stealing from your love", "Accusing your love", "Hiding from your love", "Testing your love" }; return alts;case 9: alts = new String[] { "Lack of money", "Lack of desire", "Lack of patience", "Lack of hate", "Lack of time" }; return alts;case 10: alts = new String[] { "Incarcerating", "Setting free", "Giving everything", "Killing to protect", "Forgiving" }; return alts;case 11: alts = new String[] { "Not if you hurt other people", "Not if you are really disgusting", "Only if you give them respect", "Yes. There are such people", "No that is impossible" }; return alts;case 12: alts = new String[] { "Fear", "Megalomania", "Envy", "Pride", "Control" }; return alts;case 13: alts = new String[] { "It represents a way of taking money from people", "It provides a service in much need", "It exists in order to distribute wealth", "It is an organisation driven by well-meaning people", "It is beneficial for everyone" }; return alts;case 14: alts = new String[] { "Organisation forced upon the individual", "A way for people to cooperate in order to reduce overall pain", "A way for people to cooperate in order to promote leaders", "A way for people to cooperate in order to make everyone rich", "A way for people to cooperate in order to promote individuals" }; return alts;case 15: alts = new String[] { "poor", "sick", "superior", "loving", "annoying" }; return alts;case 16: alts = new String[] { "pride", "jealousy", "fear", "hate", "principles" }; return alts;case 17: alts = new String[] { "vulnerability", "strength", "joy", "anger", "jealousy" }; return alts; }  String[] alts = YES_NO_STRINGS; return alts;
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
/*      */   static String[] getAnswerAlternativesForLevelForHatePath(byte level) {
/* 2317 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2320 */         alts = new String[] { "Something ethereal", "A fist", "An emotion", "A need", "A dog" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2409 */         return alts;case 1: alts = new String[] { "Death", "Energy", "Hate", "Beauty", "Action" }; return alts;case 2: alts = new String[] { "It can kill me", "It can hurt someone I care about", "It takes a lot of time", "It drains energy", "It is destructive" }; return alts;case 3: alts = new String[] { "Hate based on fear", "Hate based on facts", "Hate based on pain", "Hate based on envy", "Hate based on insults" }; return alts;case 4: alts = new String[] { "By pretending", "By hating", "By killing", "By becoming stupid", "By finding proof" }; return alts;case 5: alts = new String[] { "To kill my enemies", "To turn it into love", "To remove inner pressure", "To have fun", "To resolve my problems" }; return alts;case 6: alts = new String[] { "I am", "The people who watch me", "Anyone who knows about it", "The rulers", "My enemies" }; return alts;case 7: alts = new String[] { "With the truth", "With emotions", "With hate", "With force", "By letting it out" }; return alts;case 8: alts = new String[] { "Violence", "Fear", "Confusion", "Disruption", "Love" }; return alts;case 9: alts = new String[] { "No", "Yes", "If I hurt my friends", "If I loose out", "If I fail to hurt my enemy" }; return alts;case 10: alts = new String[] { "When my enemies are gone", "When I have power and knowledge to use it", "When I am calm inside", "When I can loose it at will", "Nobody can" }; return alts;case 11: alts = new String[] { "It lets you survive by striking first", "You scare people away and gets left alone", "Others will give you power and coins", "Nobody will stop you and you can take what you want", "You will have nothing left to tie your soul down" }; return alts;case 12: alts = new String[] { "The enemy which doesn't pay me", "The enemy which tries to like me", "The enemy in my head", "The enemy on the way to the real enemy", "The enemy who confronts me with words" }; return alts;case 13: alts = new String[] { "Abnormal beauty", "Physical strength", "Extreme intelligence", "Masterful psychology", "Random violence" }; return alts;case 14: alts = new String[] { "Believing in the concept of an afterlife", "Repeated compassion shown by another human being", "Believing in the concept of a saint", "Being saved to life by someone", "Being left alone for a long time" }; return alts;case 15: alts = new String[] { "Repetition", "Evilness", "Exact and overwhelming force", "Ruthlessness", "People who believe in you" }; return alts;case 16: alts = new String[] { "Fear", "Loathing", "Action", "Love", "The person" }; return alts;case 17: alts = new String[] { "Envy", "Feeling of injustice", "Fear", "Wrath", "Bad upbringing" }; return alts; }  String[] alts = YES_NO_STRINGS; return alts;
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
/*      */   static String[] getAnswerAlternativesForLevelForKnowledgePath(byte level) {
/* 2423 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2426 */         alts = new String[] { "Everything I have seen", "Lies are the truth", "Commonly accepted facts", "Information that I have accepted as a fact", "Applied experience" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2526 */         return alts;case 1: alts = new String[] { "Experience", "Anything I accept as the truth", "Logically verified information", "I trust nothing", "What a lot of people say" }; return alts;case 2: alts = new String[] { "Everything we experience is true", "Only what I decide to see within me", "No", "It is a joke", "What people decide" }; return alts;case 3: alts = new String[] { "I accept nothing as fact", "I test facts the first time I encounter them", "I only accept facts from my own experiences", "I do not trust my senses but other sources of information", "I initially accept everything as fact and try to constantly verify it" }; return alts;case 4: alts = new String[] { "Fearlessness", "Doubt", "Psychological strength", "Wealth", "Humility" }; return alts;case 5: alts = new String[] { "Someone who knows a lot", "Someone who is paid", "Someone who is extremely skillful", "Someone who knows a bit more than average", "Someone worthy of respect" }; return alts;case 6: alts = new String[] { "To really learn the details", "To buy new clothes", "To relax and take it easy", "To know a little about everything", "To talk to friends" }; return alts;case 7: alts = new String[] { "Always gather more facts", "Usually you could toss a coin", "Wait and see", "Let someone else decide", "Look for a sign" }; return alts;case 8: alts = new String[] { "It can help", "It is very important", "You should not be allowed to make decisions without it", "The history is useless", "I don't know" }; return alts;case 9: alts = new String[] { "A brave heart", "Fear of making an error", "Luck", "Fairly logical thinking", "More knowledge" }; return alts;case 10: alts = new String[] { "Being wrong", "Being right", "Inactivity", "Making too many decisions", "Killing someone innocent" }; return alts;case 11: alts = new String[] { "That there is nothingness", "That there are no people around to interfere with the results", "That there is no randomness in the world", "That I like the results", "That I have the acceptance of my peers" }; return alts;case 12: alts = new String[] { "To see what it is that you actually do to the world and the people around you", "To understand how the fabric of the world is constructed", "To understand how man-made things are constructed", "To accept what others are trying to teach you", "To see things from other peoples perspective" }; return alts;case 13: alts = new String[] { "Thinking in new paths", "Asking the person to explain his or her intentions", "By understanding how the behaviour is supposed to benefit the individual in question", "Meeting other types of people", "Trying to relax and focus" }; return alts;case 14: alts = new String[] { "Thinking outside the box", "Asking the government to improve in explaining the benefits", "Learning other related things", "Trying to relax and focus", "By investigating how the function is supposed to benefit everyone" }; return alts;case 15: alts = new String[] { "To gain status", "To achieve happiness", "To gain control", "To further humanity", "To learn from history" }; return alts;case 16: alts = new String[] { "The Priest", "The Expert", "The Professor", "The Fool", "The Lover" }; return alts;case 17: alts = new String[] { "To gain status", "To achieve happiness", "To gain control", "To survive", "To become rich" }; return alts; }  String[] alts = YES_NO_STRINGS; return alts;
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
/*      */   public static final String getWrongAnswerStringForLevel(byte path, byte level) {
/* 2541 */     switch (path)
/*      */     
/*      */     { case 5:
/* 2544 */         wrongAnswer = getWrongAnswerStringForLevelForPowerPath(level);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2562 */         return wrongAnswer;case 4: wrongAnswer = getWrongAnswerStringForLevelForInsanityPath(level); return wrongAnswer;case 1: wrongAnswer = getWrongAnswerStringForLevelForLovePath(level); return wrongAnswer;case 2: wrongAnswer = getWrongAnswerStringForLevelForHatePath(level); return wrongAnswer;case 3: wrongAnswer = getWrongAnswerStringForLevelForKnowledgePath(level); return wrongAnswer; }  String wrongAnswer = "You feel no revelation. You have to rethink."; return wrongAnswer;
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
/*      */   static String getWrongAnswerStringForLevelForPowerPath(byte level) {
/* 2576 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2579 */         wrongAnswer = "You need something more clear to light the path.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2636 */         return wrongAnswer;case 1: wrongAnswer = "Hmm. You feel no revelation."; return wrongAnswer;case 2: wrongAnswer = "There must be a safer route."; return wrongAnswer;case 3: wrongAnswer = "Hmm, is that really so?"; return wrongAnswer;case 4: wrongAnswer = "That does not feel like a clean solution."; return wrongAnswer;case 5: wrongAnswer = "Hmm. That's not required."; return wrongAnswer;case 6: wrongAnswer = "That answer doesn't quite cut it."; return wrongAnswer;case 7: wrongAnswer = "There must be a better way."; return wrongAnswer;case 8: wrongAnswer = "You consider the situations you have witnessed and see flaws in your reasoning."; return wrongAnswer;case 9: wrongAnswer = "Somehow you doubt that it is really the reason."; return wrongAnswer;case 10: wrongAnswer = "Hasn't that happened?"; return wrongAnswer;case 11: wrongAnswer = "Not effective enough."; return wrongAnswer;case 12: wrongAnswer = "Too blunt a weapon."; return wrongAnswer;case 13: wrongAnswer = "That can't be the best way."; return wrongAnswer;case 14: wrongAnswer = "That plan has many flaws."; return wrongAnswer;case 15: wrongAnswer = "Maybe there is something more powerful."; return wrongAnswer;case 16: wrongAnswer = "Even without that, society can exist."; return wrongAnswer;case 17: wrongAnswer = "Not quite."; return wrongAnswer; }  String wrongAnswer = "You felt no revelation. You will have to reconsider."; return wrongAnswer;
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
/*      */   static String getWrongAnswerStringForLevelForInsanityPath(byte level) {
/* 2650 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2653 */         wrongAnswer = "Too sane.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2710 */         return wrongAnswer;case 1: wrongAnswer = "Hmm. That's not insane."; return wrongAnswer;case 2: wrongAnswer = "That makes too much sense."; return wrongAnswer;case 3: wrongAnswer = "A normal answer."; return wrongAnswer;case 4: wrongAnswer = "Too sane."; return wrongAnswer;case 5: wrongAnswer = "Hmm. That's not insane."; return wrongAnswer;case 6: wrongAnswer = "A normal answer."; return wrongAnswer;case 7: wrongAnswer = "Too sane."; return wrongAnswer;case 8: wrongAnswer = "Nothing unusual about that answer."; return wrongAnswer;case 9: wrongAnswer = "You feel a weird sensation. Sanity at last?"; return wrongAnswer;case 10: wrongAnswer = "You think too clearly. Maybe you are not worthy?"; return wrongAnswer;case 11: wrongAnswer = "Where's your problem now?"; return wrongAnswer;case 12: wrongAnswer = "Too sane."; return wrongAnswer;case 13: wrongAnswer = "You don't feel that it is a fair definition."; return wrongAnswer;case 14: wrongAnswer = "Hmm. That definition feels too blunt."; return wrongAnswer;case 15: wrongAnswer = "Maybe you are that actually..."; return wrongAnswer;case 16: wrongAnswer = "Isn't it obvious that.."; return wrongAnswer;case 17: wrongAnswer = "You are better than that!"; return wrongAnswer; }  String wrongAnswer = "You felt no revelation. You will have to reconsider."; return wrongAnswer;
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
/*      */   static String getWrongAnswerStringForLevelForLovePath(byte level) {
/* 2724 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2727 */         wrongAnswer = "You don't feel any revelation. Not quite the answer.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2784 */         return wrongAnswer;case 1: wrongAnswer = "Hmm. Maybe love does not require that?"; return wrongAnswer;case 2: wrongAnswer = "Isn't that a common problem when it comes to love?"; return wrongAnswer;case 3: wrongAnswer = "No that can't be it. The path forward is easier."; return wrongAnswer;case 4: wrongAnswer = "That answer maybe was too obvious. You figure you did not challenge yourself enough."; return wrongAnswer;case 5: wrongAnswer = "There must be something better."; return wrongAnswer;case 6: wrongAnswer = "Does that benefit both of you most?"; return wrongAnswer;case 7: wrongAnswer = "You ask yourself 'Is that even possible?'"; return wrongAnswer;case 8: wrongAnswer = "You suspect that there is something worse."; return wrongAnswer;case 9: wrongAnswer = "You can probably love something without that."; return wrongAnswer;case 10: wrongAnswer = "Wouldn't you usually do that for your own benefit?"; return wrongAnswer;case 11: wrongAnswer = "Something feels wrong with that answer."; return wrongAnswer;case 12: wrongAnswer = "Something feels wrong with that answer."; return wrongAnswer;case 13: wrongAnswer = "Are you really worthy?"; return wrongAnswer;case 14: wrongAnswer = "Did you answer with love?"; return wrongAnswer;case 15: wrongAnswer = "What is the ne?"; return wrongAnswer;case 16: wrongAnswer = "One thing will block more"; return wrongAnswer;case 17: wrongAnswer = "Something else happens as well"; return wrongAnswer; }  String wrongAnswer = "You felt no revelation. You will have to reconsider."; return wrongAnswer;
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
/*      */   static String getWrongAnswerStringForLevelForHatePath(byte level) {
/* 2798 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2801 */         wrongAnswer = "It would seem so but you feel that it is more powerful.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2858 */         return wrongAnswer;case 1: wrongAnswer = "That is good but you suspect that there is something better."; return wrongAnswer;case 2: wrongAnswer = "Something must be worse than that."; return wrongAnswer;case 3: wrongAnswer = "'But does that go away so easily?', you think."; return wrongAnswer;case 4: wrongAnswer = "'Does that really help?', you wonder."; return wrongAnswer;case 5: wrongAnswer = "That doesn't seem powerful enough."; return wrongAnswer;case 6: wrongAnswer = "After all, are they that bad of a threat?"; return wrongAnswer;case 7: wrongAnswer = "But does that really constitute control?"; return wrongAnswer;case 8: wrongAnswer = "You feel that there is something more powerful."; return wrongAnswer;case 9: wrongAnswer = "Maybe not."; return wrongAnswer;case 10: wrongAnswer = "Is that absolute power?"; return wrongAnswer;case 11: wrongAnswer = "It must be more profound than that."; return wrongAnswer;case 12: wrongAnswer = "That suddenly sounds like semantics to you."; return wrongAnswer;case 13: wrongAnswer = "There is something even better."; return wrongAnswer;case 14: wrongAnswer = "Something that removes the core."; return wrongAnswer;case 15: wrongAnswer = "Not powerful enough."; return wrongAnswer;case 16: wrongAnswer = "There is a definite borderline."; return wrongAnswer;case 17: wrongAnswer = "Everyone can hate."; return wrongAnswer; }  String wrongAnswer = "You felt no revelation. You will have to reconsider."; return wrongAnswer;
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
/*      */   static String getWrongAnswerStringForLevelForKnowledgePath(byte level) {
/* 2872 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2875 */         wrongAnswer = "You don't feel that sense of revelation. The answer is probably too obvious.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2932 */         return wrongAnswer;case 1: wrongAnswer = "You feel that the answer contains holes."; return wrongAnswer;case 2: wrongAnswer = "You understand that you need to challenge your beliefs more in order to evolve."; return wrongAnswer;case 3: wrongAnswer = "That does not help you."; return wrongAnswer;case 4: wrongAnswer = "You suspect that there is something greater."; return wrongAnswer;case 5: wrongAnswer = "Somehow, that doesn't seem powerful enough."; return wrongAnswer;case 6: wrongAnswer = "You wonder if that would really help."; return wrongAnswer;case 7: wrongAnswer = "Come to think about it, is that the best way forward?"; return wrongAnswer;case 8: wrongAnswer = "Is that so?"; return wrongAnswer;case 9: wrongAnswer = "Then again, is that fruitful?"; return wrongAnswer;case 10: wrongAnswer = "Something feels wrong with that answer."; return wrongAnswer;case 11: wrongAnswer = "It must be something absolute."; return wrongAnswer;case 12: wrongAnswer = "Something feels wrong with that answer."; return wrongAnswer;case 13: wrongAnswer = "People may tell you that."; return wrongAnswer;case 14: wrongAnswer = "You are not getting to the core."; return wrongAnswer;case 15: wrongAnswer = "Maybe there is something even more important."; return wrongAnswer;case 16: wrongAnswer = "Without them there is nothing."; return wrongAnswer;case 17: wrongAnswer = "The only reason, really."; return wrongAnswer; }  String wrongAnswer = "You felt no revelation. You will have to reconsider."; return wrongAnswer;
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
/*      */   public static final int getCorrectAnswerForNextLevel(byte path, byte level) {
/* 2947 */     switch (path)
/*      */     
/*      */     { case 5:
/* 2950 */         answerForNextLevel = getCorrectAnswerForNextLevelForPowerPath(level);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2968 */         return answerForNextLevel;case 4: answerForNextLevel = getCorrectAnswerForNextLevelForInsanityPath(level); return answerForNextLevel;case 1: answerForNextLevel = getCorrectAnswerForNextLevelForLovePath(level); return answerForNextLevel;case 2: answerForNextLevel = getCorrectAnswerForNextLevelForHatePath(level); return answerForNextLevel;case 3: answerForNextLevel = getCorrectAnswerForNextLevelForKnowledgePath(level); return answerForNextLevel; }  int answerForNextLevel = 0; return answerForNextLevel;
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
/*      */   static int getCorrectAnswerForNextLevelForPowerPath(byte level) {
/* 2982 */     switch (level)
/*      */     
/*      */     { case 0:
/* 2985 */         answerForNextLevel = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3042 */         return answerForNextLevel;case 1: answerForNextLevel = 3; return answerForNextLevel;case 2: answerForNextLevel = 1; return answerForNextLevel;case 3: answerForNextLevel = 1; return answerForNextLevel;case 4: answerForNextLevel = 3; return answerForNextLevel;case 5: answerForNextLevel = 2; return answerForNextLevel;case 6: answerForNextLevel = 0; return answerForNextLevel;case 7: answerForNextLevel = 3; return answerForNextLevel;case 8: answerForNextLevel = 0; return answerForNextLevel;case 9: answerForNextLevel = 2; return answerForNextLevel;case 10: answerForNextLevel = 4; return answerForNextLevel;case 11: answerForNextLevel = 4; return answerForNextLevel;case 12: answerForNextLevel = 2; return answerForNextLevel;case 13: answerForNextLevel = 3; return answerForNextLevel;case 14: answerForNextLevel = 2; return answerForNextLevel;case 15: answerForNextLevel = 1; return answerForNextLevel;case 16: answerForNextLevel = 0; return answerForNextLevel;case 17: answerForNextLevel = 3; return answerForNextLevel; }  int answerForNextLevel = 0; return answerForNextLevel;
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
/*      */   static int getCorrectAnswerForNextLevelForInsanityPath(byte level) {
/* 3056 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3059 */         answerForNextLevel = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3116 */         return answerForNextLevel;case 1: answerForNextLevel = 2; return answerForNextLevel;case 2: answerForNextLevel = 2; return answerForNextLevel;case 3: answerForNextLevel = 1; return answerForNextLevel;case 4: answerForNextLevel = 2; return answerForNextLevel;case 5: answerForNextLevel = 0; return answerForNextLevel;case 6: answerForNextLevel = 4; return answerForNextLevel;case 7: answerForNextLevel = 4; return answerForNextLevel;case 8: answerForNextLevel = 3; return answerForNextLevel;case 9: answerForNextLevel = 1; return answerForNextLevel;case 10: answerForNextLevel = 1; return answerForNextLevel;case 11: answerForNextLevel = 4; return answerForNextLevel;case 12: answerForNextLevel = 0; return answerForNextLevel;case 13: answerForNextLevel = 3; return answerForNextLevel;case 14: answerForNextLevel = 2; return answerForNextLevel;case 15: answerForNextLevel = 2; return answerForNextLevel;case 16: answerForNextLevel = 3; return answerForNextLevel;case 17: answerForNextLevel = 0; return answerForNextLevel; }  int answerForNextLevel = 0; return answerForNextLevel;
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
/*      */   static int getCorrectAnswerForNextLevelForLovePath(byte level) {
/* 3130 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3133 */         answerForNextLevel = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3190 */         return answerForNextLevel;case 1: answerForNextLevel = 1; return answerForNextLevel;case 2: answerForNextLevel = 3; return answerForNextLevel;case 3: answerForNextLevel = 2; return answerForNextLevel;case 4: answerForNextLevel = 3; return answerForNextLevel;case 5: answerForNextLevel = 4; return answerForNextLevel;case 6: answerForNextLevel = 1; return answerForNextLevel;case 7: answerForNextLevel = 4; return answerForNextLevel;case 8: answerForNextLevel = 0; return answerForNextLevel;case 9: answerForNextLevel = 2; return answerForNextLevel;case 10: answerForNextLevel = 1; return answerForNextLevel;case 11: answerForNextLevel = 3; return answerForNextLevel;case 12: answerForNextLevel = 2; return answerForNextLevel;case 13: answerForNextLevel = 1; return answerForNextLevel;case 14: answerForNextLevel = 1; return answerForNextLevel;case 15: answerForNextLevel = 4; return answerForNextLevel;case 16: answerForNextLevel = 4; return answerForNextLevel;case 17: answerForNextLevel = 0; return answerForNextLevel; }  int answerForNextLevel = 0; return answerForNextLevel;
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
/*      */   static int getCorrectAnswerForNextLevelForHatePath(byte level) {
/* 3204 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3207 */         answerForNextLevel = 3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3264 */         return answerForNextLevel;case 1: answerForNextLevel = 1; return answerForNextLevel;case 2: answerForNextLevel = 0; return answerForNextLevel;case 3: answerForNextLevel = 0; return answerForNextLevel;case 4: answerForNextLevel = 4; return answerForNextLevel;case 5: answerForNextLevel = 4; return answerForNextLevel;case 6: answerForNextLevel = 2; return answerForNextLevel;case 7: answerForNextLevel = 0; return answerForNextLevel;case 8: answerForNextLevel = 1; return answerForNextLevel;case 9: answerForNextLevel = 3; return answerForNextLevel;case 10: answerForNextLevel = 3; return answerForNextLevel;case 11: answerForNextLevel = 0; return answerForNextLevel;case 12: answerForNextLevel = 1; return answerForNextLevel;case 13: answerForNextLevel = 3; return answerForNextLevel;case 14: answerForNextLevel = 0; return answerForNextLevel;case 15: answerForNextLevel = 4; return answerForNextLevel;case 16: answerForNextLevel = 2; return answerForNextLevel;case 17: answerForNextLevel = 1; return answerForNextLevel; }  int answerForNextLevel = 0; return answerForNextLevel;
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
/*      */   static int getCorrectAnswerForNextLevelForKnowledgePath(byte level) {
/* 3278 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3281 */         answerForNextLevel = 3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3338 */         return answerForNextLevel;case 1: answerForNextLevel = 1; return answerForNextLevel;case 2: answerForNextLevel = 1; return answerForNextLevel;case 3: answerForNextLevel = 4; return answerForNextLevel;case 4: answerForNextLevel = 0; return answerForNextLevel;case 5: answerForNextLevel = 1; return answerForNextLevel;case 6: answerForNextLevel = 0; return answerForNextLevel;case 7: answerForNextLevel = 1; return answerForNextLevel;case 8: answerForNextLevel = 0; return answerForNextLevel;case 9: answerForNextLevel = 3; return answerForNextLevel;case 10: answerForNextLevel = 2; return answerForNextLevel;case 11: answerForNextLevel = 2; return answerForNextLevel;case 12: answerForNextLevel = 0; return answerForNextLevel;case 13: answerForNextLevel = 2; return answerForNextLevel;case 14: answerForNextLevel = 4; return answerForNextLevel;case 15: answerForNextLevel = 1; return answerForNextLevel;case 16: answerForNextLevel = 4; return answerForNextLevel;case 17: answerForNextLevel = 3; return answerForNextLevel; }  int answerForNextLevel = 0; return answerForNextLevel;
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
/*      */   public static final String getCorrectAnswerStringForNextLevel(byte path, byte level) {
/* 3353 */     switch (path)
/*      */     
/*      */     { case 5:
/* 3356 */         answerForNextLevel = getCorrectAnswerStringForNextLevelForPowerPath(level);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3374 */         return answerForNextLevel;case 4: answerForNextLevel = getCorrectAnswerStringForNextLevelForInsanityPath(level); return answerForNextLevel;case 1: answerForNextLevel = getCorrectAnswerStringForNextLevelForLovePath(level); return answerForNextLevel;case 2: answerForNextLevel = getCorrectAnswerStringForNextLevelForHatePath(level); return answerForNextLevel;case 3: answerForNextLevel = getCorrectAnswerStringForNextLevelForKnowledgePath(level); return answerForNextLevel; }  String answerForNextLevel = "You think 'Yes, that felt right'"; return answerForNextLevel;
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
/*      */   static String getCorrectAnswerStringForNextLevelForKnowledgePath(byte level) {
/* 3388 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3391 */         answerForNextLevel = "You think 'I am on to something'";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3448 */         return answerForNextLevel;case 1: answerForNextLevel = "You think 'That must be it'"; return answerForNextLevel;case 2: answerForNextLevel = "You think 'That makes sense'"; return answerForNextLevel;case 3: answerForNextLevel = "You think 'Probably yes'"; return answerForNextLevel;case 4: answerForNextLevel = "You think 'Sounds about right'"; return answerForNextLevel;case 5: answerForNextLevel = "You think 'Yes, that probably is it'"; return answerForNextLevel;case 6: answerForNextLevel = "You think 'That feels right'"; return answerForNextLevel;case 7: answerForNextLevel = "You think 'Seems I got that right'"; return answerForNextLevel;case 8: answerForNextLevel = "You think 'Correct'"; return answerForNextLevel;case 9: answerForNextLevel = "You think 'Yes'"; return answerForNextLevel;case 10: answerForNextLevel = "You think 'Indeed so'"; return answerForNextLevel;case 11: answerForNextLevel = "There is undoubted certainty about this."; return answerForNextLevel;case 12: answerForNextLevel = "Absolutely. Of course!"; return answerForNextLevel;case 13: answerForNextLevel = "Insight in mind of others is the way to wisdom."; return answerForNextLevel;case 14: answerForNextLevel = "Insight in common goals is essential for omniscience."; return answerForNextLevel;case 15: answerForNextLevel = "The purpose in life."; return answerForNextLevel;case 16: answerForNextLevel = "Without love nothing has value."; return answerForNextLevel;case 17: answerForNextLevel = "Otherwise there is nothing."; return answerForNextLevel; }  String answerForNextLevel = "You think 'Yes, that felt right'"; return answerForNextLevel;
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
/*      */   static String getCorrectAnswerStringForNextLevelForHatePath(byte level) {
/* 3462 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3465 */         answerForNextLevel = "You think 'I am on to something'";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3522 */         return answerForNextLevel;case 1: answerForNextLevel = "You think 'That must be it'"; return answerForNextLevel;case 2: answerForNextLevel = "You think 'That makes sense'"; return answerForNextLevel;case 3: answerForNextLevel = "You think 'Yes. Fear is removed by certainty.'"; return answerForNextLevel;case 4: answerForNextLevel = "You think 'Sounds about right'"; return answerForNextLevel;case 5: answerForNextLevel = "You think 'Yes, that probably is it'"; return answerForNextLevel;case 6: answerForNextLevel = "You think 'That feels right'"; return answerForNextLevel;case 7: answerForNextLevel = "You think 'Seems I got that right'"; return answerForNextLevel;case 8: answerForNextLevel = "You think 'Correct'"; return answerForNextLevel;case 9: answerForNextLevel = "You think 'Yes'"; return answerForNextLevel;case 10: answerForNextLevel = "You think 'Indeed so'"; return answerForNextLevel;case 11: answerForNextLevel = "You feel certain about this."; return answerForNextLevel;case 12: answerForNextLevel = "You feel the strength of truth lift you."; return answerForNextLevel;case 13: answerForNextLevel = "This may be useful."; return answerForNextLevel;case 14: answerForNextLevel = "You realize that afterlife is a powerful enemy."; return answerForNextLevel;case 15: answerForNextLevel = "One person will double your efforts."; return answerForNextLevel;case 16: answerForNextLevel = "Once you truly hate, you will take action."; return answerForNextLevel;case 17: answerForNextLevel = "Injustice lives on for generations."; return answerForNextLevel; }  String answerForNextLevel = "You think 'Yes, that felt right'"; return answerForNextLevel;
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
/*      */   static String getCorrectAnswerStringForNextLevelForLovePath(byte level) {
/* 3536 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3539 */         answerForNextLevel = "You think 'Yes, that probably is it'";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3596 */         return answerForNextLevel;case 1: answerForNextLevel = "You think 'That must be it'"; return answerForNextLevel;case 2: answerForNextLevel = "You think 'That makes sense'"; return answerForNextLevel;case 3: answerForNextLevel = "You think 'Probably yes'"; return answerForNextLevel;case 4: answerForNextLevel = "You think 'Sounds about right'"; return answerForNextLevel;case 5: answerForNextLevel = "You think 'Yes, that probably is it'"; return answerForNextLevel;case 6: answerForNextLevel = "You think 'That feels right'"; return answerForNextLevel;case 7: answerForNextLevel = "You think 'Seems I got that right'"; return answerForNextLevel;case 8: answerForNextLevel = "You think 'Correct'"; return answerForNextLevel;case 9: answerForNextLevel = "You think 'Yes'"; return answerForNextLevel;case 10: answerForNextLevel = "You think 'Indeed so'"; return answerForNextLevel;case 11: answerForNextLevel = "You experience certainty and understanding."; return answerForNextLevel;case 12: answerForNextLevel = "You feel the strength of truth lift you."; return answerForNextLevel;case 13: answerForNextLevel = "More understanding and acceptance is balm for your soul."; return answerForNextLevel;case 14: answerForNextLevel = "Together we can do it."; return answerForNextLevel;case 15: answerForNextLevel = "Try not to."; return answerForNextLevel;case 16: answerForNextLevel = "Yes, that will make it hard to love."; return answerForNextLevel;case 17: answerForNextLevel = "New ties that can cause sorrow."; return answerForNextLevel; }  String answerForNextLevel = "You think 'Yes, that felt right'"; return answerForNextLevel;
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
/*      */   static String getCorrectAnswerStringForNextLevelForInsanityPath(byte level) {
/* 3610 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3613 */         answerForNextLevel = "You feel certain that this is right.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3670 */         return answerForNextLevel;case 1: answerForNextLevel = "You nod to yourself. Of course."; return answerForNextLevel;case 2: answerForNextLevel = "You try to pat yourself on the back."; return answerForNextLevel;case 3: answerForNextLevel = "You silently nod at your findings."; return answerForNextLevel;case 4: answerForNextLevel = "Yes, yes. The sly bastards!"; return answerForNextLevel;case 5: answerForNextLevel = "The insight is appalling and fascinating."; return answerForNextLevel;case 6: answerForNextLevel = "Yes. It will be a bit of a trouble but it is the only way. The only way!"; return answerForNextLevel;case 7: answerForNextLevel = "Your mind grows dark and heavy as you realize the sincerity of this. Where did we go wrong? Who is to blame?"; return answerForNextLevel;case 8: answerForNextLevel = "You can't quite remove the grin on your face that this revelation brings."; return answerForNextLevel;case 9: answerForNextLevel = "You find yourself cackling as you are on to something here."; return answerForNextLevel;case 10: answerForNextLevel = "Of course. You spin around on the spot, afraid that someone has noticed you. You feel light-headed."; return answerForNextLevel;case 11: answerForNextLevel = "Something feels terribly wrong. Or was it right?"; return answerForNextLevel;case 12: answerForNextLevel = "Who cares. Who cares. Who cares? You stare into nothingness wherever you look."; return answerForNextLevel;case 13: answerForNextLevel = "You cackle, bubble and froth happily at your findings."; return answerForNextLevel;case 14: answerForNextLevel = "You feel safe in the love of everyone who smiles at you."; return answerForNextLevel;case 15: answerForNextLevel = "People just don't see it yet."; return answerForNextLevel;case 16: answerForNextLevel = "That will surely make a better world."; return answerForNextLevel;case 17: answerForNextLevel = "There is never reason to change the foundations of a person."; return answerForNextLevel; }  String answerForNextLevel = "As you wake up with this insight, you have to wipe off some drool on your chin."; return answerForNextLevel;
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
/*      */   static String getCorrectAnswerStringForNextLevelForPowerPath(byte level) {
/* 3684 */     switch (level)
/*      */     
/*      */     { case 0:
/* 3687 */         answerForNextLevel = "You think 'I am on to something'";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3744 */         return answerForNextLevel;case 1: answerForNextLevel = "This must be the path."; return answerForNextLevel;case 2: answerForNextLevel = "You think 'That makes sense'"; return answerForNextLevel;case 3: answerForNextLevel = "You think 'Probably yes'"; return answerForNextLevel;case 4: answerForNextLevel = "You think 'Sounds about right'"; return answerForNextLevel;case 5: answerForNextLevel = "You think 'Yes, that probably is it'"; return answerForNextLevel;case 6: answerForNextLevel = "You think 'That feels right'"; return answerForNextLevel;case 7: answerForNextLevel = "You think 'Seems I got that right'"; return answerForNextLevel;case 8: answerForNextLevel = "You think 'Correct'"; return answerForNextLevel;case 9: answerForNextLevel = "You think 'Yes'"; return answerForNextLevel;case 10: answerForNextLevel = "You think 'Indeed so'"; return answerForNextLevel;case 11: answerForNextLevel = "The strength of truth inside!"; return answerForNextLevel;case 12: answerForNextLevel = "Change taking places. Feelings of insight."; return answerForNextLevel;case 13: answerForNextLevel = "You now feel ready to take charge."; return answerForNextLevel;case 14: answerForNextLevel = "There is nothing stopping you now!"; return answerForNextLevel;case 15: answerForNextLevel = "Nobody escapes his heart."; return answerForNextLevel;case 16: answerForNextLevel = "Without it, there is anarchy."; return answerForNextLevel;case 17: answerForNextLevel = "Even if it is to diminish, there is purpose for the executor."; return answerForNextLevel; }  String answerForNextLevel = "You think 'Yes, that felt right'"; return answerForNextLevel;
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
/*      */   public static final String getPathNameFor(byte path) {
/* 3756 */     switch (path) {
/*      */       
/*      */       case 2:
/* 3759 */         return "the path of hate";
/*      */       case 1:
/* 3761 */         return "the path of love";
/*      */       case 4:
/* 3763 */         return "the path of insanity";
/*      */       case 3:
/* 3765 */         return "the path of knowledge";
/*      */       case 5:
/* 3767 */         return "the path of power";
/*      */     } 
/* 3769 */     return "no path";
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Cults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
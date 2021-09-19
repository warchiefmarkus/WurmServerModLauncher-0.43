/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.GeneralUtilities;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Point;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
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
/*     */ public final class Fish
/*     */   implements MiscConstants
/*     */ {
/*     */   static final short POND = 20000;
/*     */   static final short LAKE = 7500;
/*     */   public static final short DEEP_SEA = 5000;
/*     */   public static final short DEEP_SEA_DEPTH = -250;
/*     */   private static MeshIO mesh;
/* 125 */   private static final Logger logger = Logger.getLogger(Fish.class.getName());
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
/*     */   public static short getFishFor(Creature performer, short waterSize, short effect, int tilex, int tiley, Item rod) {
/* 138 */     int rand = Server.rand.nextInt(80 + ((rod != null) ? (rod.getRarity() * 10) : 0)) + effect / 2;
/* 139 */     int toReturn = 164;
/* 140 */     if (waterSize == 20000) {
/*     */       
/* 142 */       if (rand < 60) {
/* 143 */         toReturn = 162;
/* 144 */       } else if (rand < 90) {
/* 145 */         toReturn = 163;
/*     */       } 
/* 147 */     } else if (waterSize == 7500) {
/*     */       
/* 149 */       if (rand < 40) {
/* 150 */         toReturn = 162;
/* 151 */       } else if (rand < 60) {
/* 152 */         toReturn = 163;
/* 153 */       } else if (rand < 70) {
/* 154 */         toReturn = 165;
/* 155 */       } else if (rand < 80) {
/* 156 */         toReturn = 157;
/* 157 */       } else if (rand < 90) {
/* 158 */         toReturn = 160;
/*     */       } 
/* 160 */     } else if (waterSize == 5000) {
/*     */       
/* 162 */       toReturn = 161;
/* 163 */       if (rand < 20) {
/* 164 */         toReturn = 162;
/* 165 */       } else if (rand < 40) {
/* 166 */         toReturn = 159;
/* 167 */       } else if (rand < 55) {
/* 168 */         toReturn = 163;
/* 169 */       } else if (rand < 60) {
/* 170 */         toReturn = 165;
/* 171 */       } else if (rand < 65) {
/* 172 */         toReturn = 157;
/* 173 */       } else if (rand < 75) {
/* 174 */         toReturn = 160;
/* 175 */       } else if (rand < 80) {
/* 176 */         toReturn = 164;
/* 177 */       } else if (rand < 90) {
/* 178 */         toReturn = 158;
/*     */       }
/*     */       else {
/*     */         
/* 182 */         short fish = checkForSpecialFish(performer, tilex, tiley);
/* 183 */         if (fish != 0)
/* 184 */           return fish; 
/*     */       } 
/*     */     } 
/* 187 */     return (short)toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static short checkForSpecialFish(Creature performer, int tilex, int tiley) {
/* 192 */     ArrayList<Integer> canCatch = getSpecialFishList(performer, tilex, tiley);
/* 193 */     if (canCatch.isEmpty())
/* 194 */       return 0; 
/* 195 */     if (canCatch.size() == 1) {
/* 196 */       return ((Integer)canCatch.get(0)).shortValue();
/*     */     }
/* 198 */     int randomfish = Server.rand.nextInt(canCatch.size());
/* 199 */     return ((Integer)canCatch.get(randomfish)).shortValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ArrayList<Integer> getSpecialFishList(Creature performer, int tilex, int tiley) {
/* 205 */     ArrayList<Integer> canCatch = new ArrayList<>();
/* 206 */     Point[] points = getRareSpots(tilex, tiley);
/* 207 */     for (Point point : points) {
/*     */ 
/*     */       
/* 210 */       if (performer.isWithinTileDistanceTo(point.getX(), point.getY(), 0, 5))
/*     */       {
/* 212 */         canCatch.add(Integer.valueOf(point.getH()));
/*     */       }
/*     */     } 
/* 215 */     return canCatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Point[] getRareSpots(int tilex, int tiley) {
/* 220 */     ArrayList<Point> rareSpots = new ArrayList<>();
/* 221 */     int season = WurmCalendar.getSeasonNumber();
/* 222 */     int zoneX = tilex / 100 * 100;
/* 223 */     int zoneY = tiley / 100 * 100;
/* 224 */     Point tp = getFishSpot(zoneX, zoneY, 572, season);
/* 225 */     if (isFishSpotValid(tp))
/* 226 */       rareSpots.add(tp); 
/* 227 */     tp = getFishSpot(zoneX, zoneY, 569, season);
/* 228 */     if (isFishSpotValid(tp))
/* 229 */       rareSpots.add(tp); 
/* 230 */     tp = getFishSpot(zoneX, zoneY, 570, season);
/* 231 */     if (isFishSpotValid(tp))
/* 232 */       rareSpots.add(tp); 
/* 233 */     tp = getFishSpot(zoneX, zoneY, 574, season);
/* 234 */     if (isFishSpotValid(tp))
/* 235 */       rareSpots.add(tp); 
/* 236 */     tp = getFishSpot(zoneX, zoneY, 573, season);
/* 237 */     if (isFishSpotValid(tp))
/* 238 */       rareSpots.add(tp); 
/* 239 */     tp = getFishSpot(zoneX, zoneY, 571, season);
/* 240 */     if (isFishSpotValid(tp))
/* 241 */       rareSpots.add(tp); 
/* 242 */     tp = getFishSpot(zoneX, zoneY, 575, season);
/* 243 */     if (isFishSpotValid(tp))
/* 244 */       rareSpots.add(tp); 
/* 245 */     return rareSpots.<Point>toArray(new Point[rareSpots.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Point getFishSpot(int zoneX, int zoneY, int fishtype, int season) {
/* 250 */     Random r = new Random((fishtype + Servers.localServer.id * 10 + season * 25));
/* 251 */     int rx = zoneX + r.nextInt(100);
/* 252 */     int ry = zoneY + r.nextInt(100);
/* 253 */     return new Point(rx, ry, fishtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isFishSpotValid(Point point) {
/* 263 */     int tile = Server.surfaceMesh.getTile(Zones.safeTileX(point.getX()), Zones.safeTileY(point.getY()));
/* 264 */     if (Tiles.decodeHeight(tile) < -250)
/*     */     {
/*     */       
/* 267 */       return true;
/*     */     }
/* 269 */     return false;
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
/*     */   
/*     */   static float getDifficultyFor(int fish) {
/* 283 */     float toReturn = 0.0F;
/* 284 */     if (fish == 158) {
/* 285 */       toReturn = 50.0F;
/* 286 */     } else if (fish == 164) {
/* 287 */       toReturn = 60.0F;
/* 288 */     } else if (fish == 160) {
/* 289 */       toReturn = 40.0F;
/* 290 */     } else if (fish == 159) {
/* 291 */       toReturn = 10.0F;
/* 292 */     } else if (fish == 163) {
/* 293 */       toReturn = 8.0F;
/* 294 */     } else if (fish == 157) {
/* 295 */       toReturn = 50.0F;
/* 296 */     } else if (fish == 162) {
/* 297 */       toReturn = 2.0F;
/* 298 */     } else if (fish == 161) {
/* 299 */       toReturn = 80.0F;
/* 300 */     } else if (fish == 165) {
/* 301 */       toReturn = 30.0F;
/* 302 */     } else if (fish == 569) {
/* 303 */       toReturn = 90.0F;
/* 304 */     } else if (fish == 570) {
/* 305 */       toReturn = 86.0F;
/* 306 */     } else if (fish == 571) {
/* 307 */       toReturn = 88.0F;
/* 308 */     } else if (fish == 572) {
/* 309 */       toReturn = 84.0F;
/* 310 */     } else if (fish == 573) {
/* 311 */       toReturn = 89.0F;
/* 312 */     } else if (fish == 574) {
/* 313 */       toReturn = 85.0F;
/* 314 */     } else if (fish == 575) {
/* 315 */       toReturn = 87.0F;
/* 316 */     }  return toReturn;
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
/*     */   
/*     */   static float getDamModFor(Item f) {
/* 330 */     float toreturn = getDamModFor(f.getTemplateId()) * Math.max(0.1F, (f.getWeightGrams() / 3000));
/*     */ 
/*     */ 
/*     */     
/* 334 */     return toreturn;
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
/*     */   
/*     */   static int getDamModFor(int type) {
/* 348 */     int mod = 1;
/* 349 */     if (type == 158) {
/*     */       
/* 351 */       mod = 2;
/*     */     }
/* 353 */     else if (type == 164) {
/*     */       
/* 355 */       mod = 3;
/*     */     }
/* 357 */     else if (type == 160) {
/*     */       
/* 359 */       mod = 4;
/*     */     }
/* 361 */     else if (type == 159) {
/*     */       
/* 363 */       mod = 1;
/*     */     }
/* 365 */     else if (type == 163) {
/*     */       
/* 367 */       mod = 1;
/*     */     }
/* 369 */     else if (type == 157) {
/*     */       
/* 371 */       mod = 4;
/*     */     }
/* 373 */     else if (type == 162) {
/*     */       
/* 375 */       mod = 1;
/*     */     }
/* 377 */     else if (type == 161) {
/*     */       
/* 379 */       mod = 5;
/*     */     }
/* 381 */     else if (type == 165) {
/*     */       
/* 383 */       mod = 2;
/*     */     }
/* 385 */     else if (type == 569) {
/* 386 */       mod = 2;
/* 387 */     } else if (type == 570) {
/* 388 */       mod = 2;
/* 389 */     } else if (type == 571) {
/* 390 */       mod = 1;
/* 391 */     } else if (type == 572) {
/* 392 */       mod = 1;
/* 393 */     } else if (type == 573) {
/* 394 */       mod = 1;
/* 395 */     } else if (type == 574) {
/* 396 */       mod = 1;
/* 397 */     } else if (type == 575) {
/* 398 */       mod = 2;
/* 399 */     }  return mod;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getMeatFor(Item fish) {
/* 404 */     double ql = (fish.getCurrentQualityLevel() / 100.0F);
/* 405 */     double weight = fish.getTemplate().getWeightGrams() * ql;
/* 406 */     return weight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean fish(Creature performer, Item source, int tilex, int tiley, int tile, float counter, Action act) {
/* 412 */     boolean done = false;
/* 413 */     if (!Terraforming.isTileUnderWater(tile, tilex, tiley, performer.isOnSurface())) {
/*     */       
/* 415 */       performer.getCommunicator().sendNormalServerMessage("The water is too shallow to fish.");
/* 416 */       done = true;
/*     */     } 
/* 418 */     if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*     */       
/* 420 */       performer.getCommunicator().sendNormalServerMessage("A huge shadow moves beneath the waves, and you reel in the line in panic.");
/*     */       
/* 422 */       done = true;
/*     */     } 
/* 424 */     if (source.getTemplateId() == 23 || source.getTemplateId() == 780) {
/*     */       
/* 426 */       performer.getCommunicator().sendNormalServerMessage("The " + source
/* 427 */           .getName() + " will do a poor job of catching fish.");
/* 428 */       done = true;
/*     */     } 
/*     */     
/* 431 */     if (source.getTemplateId() != 94 && source.getTemplateId() != 152 && source
/* 432 */       .getTemplateId() != 176)
/* 433 */       return true; 
/* 434 */     if (!done) {
/*     */       
/* 436 */       done = false;
/* 437 */       int time = 1800;
/* 438 */       if (counter == 1.0F) {
/*     */         
/* 440 */         int perfTileX = performer.getTileX();
/* 441 */         int perfTileY = performer.getTileY();
/* 442 */         mesh = Server.surfaceMesh;
/* 443 */         if (!performer.isOnSurface()) {
/* 444 */           mesh = Server.caveMesh;
/*     */         }
/* 446 */         int perfTile = mesh.getTile(perfTileX, perfTileY);
/* 447 */         short ph = Tiles.decodeHeight(perfTile);
/* 448 */         if (performer.getBridgeId() == -10L && ph < -10 && performer.getVehicle() == -10L) {
/*     */           
/* 450 */           done = true;
/* 451 */           performer.getCommunicator().sendNormalServerMessage("You can't swim and fish at the same time.");
/*     */         }
/*     */         else {
/*     */           
/* 455 */           performer.getCommunicator().sendNormalServerMessage("You throw out the line and start fishing.");
/* 456 */           Server.getInstance().broadCastAction(performer.getName() + " throws out a line and starts fishing.", performer, 5);
/*     */           
/* 458 */           performer.sendActionControl(Actions.actionEntrys[160].getVerbString(), true, 1800);
/* 459 */           short timeToBite = (short)(100 + Server.rand.nextInt(1000));
/*     */           
/*     */           try {
/* 462 */             performer.getCurrentAction().setTimeLeft(timeToBite);
/*     */           }
/* 464 */           catch (NoSuchActionException nsa) {
/*     */             
/* 466 */             logger.log(Level.INFO, "Strange, this action doesn't exist.");
/*     */           }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         
/* 472 */         int number = 10;
/*     */         
/*     */         try {
/* 475 */           number = performer.getCurrentAction().getTimeLeft();
/*     */         }
/* 477 */         catch (NoSuchActionException nsa) {
/*     */           
/* 479 */           logger.log(Level.INFO, "Strange, this action doesn't exist.");
/*     */         } 
/* 481 */         if (number < 2000) {
/*     */           
/* 483 */           if (act.currentSecond() * 10 > number || source.getTemplateId() == 176) {
/*     */             
/* 485 */             short waterFound = 0;
/* 486 */             short waterType = 20000;
/* 487 */             short maxDepth = 0;
/* 488 */             if (performer.isOnSurface()) {
/*     */               
/* 490 */               int x = 0;
/* 491 */               int y = 1;
/*     */               
/* 493 */               while (y++ < 10) {
/*     */                 
/* 495 */                 if (tiley - y > 0) {
/*     */                   
/* 497 */                   int t = Server.surfaceMesh.getTile(tilex, tiley - y);
/* 498 */                   short h = Tiles.decodeHeight(t);
/* 499 */                   if (h <= -3) {
/*     */                     
/* 501 */                     if (h < maxDepth)
/* 502 */                       maxDepth = h; 
/* 503 */                     waterFound = (short)(waterFound + 1);
/*     */                   } 
/*     */                   continue;
/*     */                 } 
/* 507 */                 waterFound = (short)(waterFound + 1);
/*     */               } 
/* 509 */               y = 1;
/* 510 */               while (y++ < 10) {
/*     */                 
/* 512 */                 if (tiley + y < Zones.worldTileSizeY) {
/*     */                   
/* 514 */                   int t = Server.surfaceMesh.getTile(tilex, tiley + y);
/* 515 */                   short h = Tiles.decodeHeight(t);
/* 516 */                   if (h <= -3) {
/*     */                     
/* 518 */                     if (h < maxDepth)
/* 519 */                       maxDepth = h; 
/* 520 */                     waterFound = (short)(waterFound + 1);
/*     */                   } 
/*     */                   continue;
/*     */                 } 
/* 524 */                 waterFound = (short)(waterFound + 1);
/*     */               } 
/* 526 */               y = 0;
/* 527 */               x = 1;
/* 528 */               while (x++ < 10) {
/*     */                 
/* 530 */                 if (tilex + x > Zones.worldTileSizeX) {
/*     */                   
/* 532 */                   int t = Server.surfaceMesh.getTile(tilex + x, tiley);
/* 533 */                   short h = Tiles.decodeHeight(t);
/* 534 */                   if (h <= -3) {
/*     */                     
/* 536 */                     if (h < maxDepth)
/* 537 */                       maxDepth = h; 
/* 538 */                     waterFound = (short)(waterFound + 1);
/*     */                   } 
/*     */                   continue;
/*     */                 } 
/* 542 */                 waterFound = (short)(waterFound + 1);
/*     */               } 
/* 544 */               x = 1;
/* 545 */               while (x++ < 10) {
/*     */                 
/* 547 */                 if (tilex - x > 0) {
/*     */                   
/* 549 */                   int t = Server.surfaceMesh.getTile(tilex - x, tiley);
/* 550 */                   short h = Tiles.decodeHeight(t);
/* 551 */                   if (h <= -3) {
/*     */                     
/* 553 */                     if (h < maxDepth)
/* 554 */                       maxDepth = h; 
/* 555 */                     waterFound = (short)(waterFound + 1);
/*     */                   } 
/*     */                   continue;
/*     */                 } 
/* 559 */                 waterFound = (short)(waterFound + 1);
/*     */               } 
/*     */             } 
/*     */             
/* 563 */             if (waterFound > 1)
/*     */             {
/* 565 */               if (waterFound < 9) {
/*     */                 
/* 567 */                 waterType = 20000;
/*     */               }
/* 569 */               else if (waterFound > 20 && maxDepth < -250) {
/*     */                 
/* 571 */                 waterType = 5000;
/*     */               }
/*     */               else {
/*     */                 
/* 575 */                 waterType = 7500;
/*     */               } 
/*     */             }
/*     */             
/*     */             try {
/* 580 */               performer.getCurrentAction().setTimeLeft(waterType);
/*     */             }
/* 582 */             catch (NoSuchActionException nsa) {
/*     */               
/* 584 */               logger.log(Level.INFO, "Strange, this action doesn't exist.");
/*     */             }
/*     */           
/* 587 */           } else if (act.justTickedSecond() && act.currentSecond() % 2 == 0) {
/*     */             Skill fishing;
/* 589 */             if (performer.getInventory().getNumItemsNotCoins() >= 100) {
/*     */               
/* 591 */               performer.getCommunicator().sendNormalServerMessage("You wouldn't be able to carry the fish. Drop something first.");
/*     */               
/* 593 */               return true;
/*     */             } 
/* 595 */             if (!performer.canCarry(1000)) {
/*     */               
/* 597 */               performer.getCommunicator().sendNormalServerMessage("You are too heavily loaded. Drop something first.");
/*     */               
/* 599 */               return true;
/*     */             } 
/* 601 */             Skills skills = performer.getSkills();
/*     */ 
/*     */             
/*     */             try {
/* 605 */               fishing = skills.getSkill(10033);
/*     */             }
/* 607 */             catch (NoSuchSkillException nss) {
/*     */               
/* 609 */               fishing = skills.learn(10033, 1.0F);
/*     */             } 
/* 611 */             fishing.skillCheck(50.0D, source, 0.0D, false, 2.0F);
/*     */           }
/*     */         
/* 614 */         } else if (act.justTickedSecond() && act.currentSecond() % 2 == 0) {
/*     */           
/* 616 */           if (performer.getInventory().getNumItemsNotCoins() >= 100) {
/*     */             
/* 618 */             performer.getCommunicator().sendNormalServerMessage("You wouldn't be able to carry the fish. Drop something first.");
/*     */             
/* 620 */             return true;
/*     */           } 
/* 622 */           int waterType = number;
/* 623 */           short fishOnHook = 0;
/* 624 */           Item fish = null;
/* 625 */           Skills skills = performer.getSkills();
/* 626 */           Skill fishing = null;
/*     */           
/*     */           try {
/* 629 */             fishing = skills.getSkill(10033);
/*     */           }
/* 631 */           catch (NoSuchSkillException nss) {
/*     */             
/* 633 */             fishing = skills.learn(10033, 1.0F);
/*     */           } 
/* 635 */           double bonus = 0.0D;
/* 636 */           if (source.getTemplateId() == 152) {
/* 637 */             bonus -= 20.0D;
/*     */           }
/*     */ 
/*     */           
/* 641 */           if (waterType != 0) {
/*     */ 
/*     */             
/* 644 */             if (waterType >= 20000) {
/*     */               
/* 646 */               fishOnHook = (short)(waterType - 20000);
/*     */             }
/* 648 */             else if (waterType >= 7500) {
/*     */               
/* 650 */               fishOnHook = (short)(waterType - 7500);
/*     */             }
/* 652 */             else if (waterType >= 5000) {
/*     */               
/* 654 */               fishOnHook = (short)(waterType - 5000);
/*     */             } 
/* 656 */             if (fishOnHook == 0) {
/*     */ 
/*     */ 
/*     */               
/* 660 */               double result = fishing.skillCheck((waterType / 250.0F), source, bonus, false, 1.0F);
/*     */               
/* 662 */               if (result > 0.0D) {
/*     */                 
/* 664 */                 int perfTileX = performer.getTileX();
/* 665 */                 int perfTileY = performer.getTileY();
/*     */                 
/* 667 */                 fishOnHook = getFishFor(performer, (short)waterType, (short)(int)result, perfTileX, perfTileY, source);
/*     */ 
/*     */                 
/*     */                 try {
/* 671 */                   if (act.getRarity() != 0)
/*     */                   {
/* 673 */                     performer.playPersonalSound("sound.fx.drumroll");
/*     */                   }
/* 675 */                   double fishresult = 10.0D + 0.9D * Math.max((Server.rand.nextFloat() * 10.0F), fishing
/* 676 */                       .skillCheck(getDifficultyFor(fishOnHook), source, bonus, true, 1.0F));
/*     */ 
/*     */                   
/* 679 */                   fish = ItemFactory.createItem(fishOnHook, (float)fishresult, (byte)2, act
/* 680 */                       .getRarity(), null);
/*     */ 
/*     */                   
/* 683 */                   int weight = (int)getMeatFor(fish);
/*     */ 
/*     */ 
/*     */                   
/* 687 */                   if (weight <= 50)
/*     */                   {
/* 689 */                     performer.getCommunicator().sendNormalServerMessage("You almost catch something but it escapes.");
/*     */                     
/* 691 */                     Server.getInstance().broadCastAction(performer
/* 692 */                         .getName() + "'s line twitches but nothing is on it.", performer, 5);
/* 693 */                     done = true;
/*     */                     
/* 695 */                     Items.decay(fish.getWurmId(), fish.getDbStrings());
/*     */                   }
/*     */                   else
/*     */                   {
/* 699 */                     fish.setSizes(weight);
/*     */                     
/* 701 */                     fish.setWeight(weight, false);
/* 702 */                     performer.getCommunicator().sendNormalServerMessage("Something bites.");
/* 703 */                     Server.getInstance().broadCastAction("Something bites on " + performer
/* 704 */                         .getName() + "'s hook.", performer, 5);
/* 705 */                     performer.getCurrentAction().setDestroyedItem(fish);
/*     */                   }
/*     */                 
/* 708 */                 } catch (NoSuchTemplateException nse) {
/*     */                   
/* 710 */                   logger.log(Level.INFO, "Failed to create item with id " + fishOnHook + " for performer " + performer
/* 711 */                       .getName(), (Throwable)nse);
/*     */                 }
/* 713 */                 catch (FailedException fe) {
/*     */                   
/* 715 */                   logger.log(Level.INFO, "Failed to create item with id " + fishOnHook + " for performer " + performer
/* 716 */                       .getName(), (Throwable)fe);
/*     */                 }
/* 718 */                 catch (NoSuchActionException nsa) {
/*     */                   
/* 720 */                   logger.log(Level.INFO, "no action found for performer");
/*     */                 } 
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 727 */                 performer.getCurrentAction().setTimeLeft((short)(waterType + fishOnHook));
/*     */               }
/* 729 */               catch (NoSuchActionException nsa) {
/*     */                 
/* 731 */                 logger.log(Level.INFO, "Strange, this action doesn't exist.");
/*     */               } 
/*     */             } else {
/*     */ 
/*     */               
/*     */               try {
/*     */ 
/*     */                 
/* 739 */                 fish = performer.getCurrentAction().getDestroyedItem();
/*     */               }
/* 741 */               catch (NoSuchActionException nsa) {
/*     */                 
/* 743 */                 logger.log(Level.INFO, "no action found, should not happen");
/*     */               } 
/* 745 */               float difficulty = getDifficultyFor(fishOnHook);
/*     */ 
/*     */ 
/*     */               
/* 749 */               double result = fishing.skillCheck(difficulty, source, bonus, false, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 754 */               if ((act.currentSecond() % 5 == 0 || source.getTemplateId() == 176) && act.justTickedSecond())
/*     */               {
/*     */ 
/*     */                 
/* 758 */                 if (result > 0.0D)
/*     */                 {
/* 760 */                   done = true;
/*     */                   
/*     */                   try {
/* 763 */                     performer.getCurrentAction().setDestroyedItem(null);
/*     */                   }
/* 765 */                   catch (NoSuchActionException nsa) {
/*     */                     
/* 767 */                     logger.log(Level.INFO, "no action found, should not happen");
/*     */                   } 
/*     */ 
/*     */                   
/* 771 */                   if (source.getTemplateId() != 176)
/* 772 */                     source.setDamage(source.getDamage() + 
/* 773 */                         Math.min((100.0F - source
/* 774 */                           .getCurrentQualityLevel()) / 50.0F, 
/* 775 */                           Math.max(0.1F, source
/*     */                             
/* 777 */                             .getDamageModifier(false, true) / 10.0F * 
/* 778 */                             Math.max(10.0F, source.getQualityLevel())) * 
/* 779 */                           getDamModFor(fish))); 
/* 780 */                   performer.getInventory().insertItem(fish, true);
/* 781 */                   performer.achievement(126);
/*     */                   
/* 783 */                   int weight = (int)getMeatFor(fish);
/* 784 */                   if (weight > 3000)
/* 785 */                     performer.achievement(542); 
/* 786 */                   if (weight > 10000)
/* 787 */                     performer.achievement(585); 
/* 788 */                   if (weight > 175000) {
/* 789 */                     performer.achievement(297);
/*     */                   }
/* 791 */                   performer.getCommunicator().sendNormalServerMessage("You catch " + fish
/* 792 */                       .getNameWithGenus() + ".");
/* 793 */                   Server.getInstance().broadCastAction(performer
/* 794 */                       .getName() + " catches " + fish.getNameWithGenus() + ".", performer, 5);
/* 795 */                   fishOnHook = 0;
/* 796 */                   if (fish.getTemplateId() == 570 || fish
/* 797 */                     .getTemplateId() == 571)
/*     */                   {
/*     */                     
/* 800 */                     result = fishing.skillCheck(difficulty, source, bonus, false, 1.0F);
/* 801 */                     if (result < 0.0D && performer.getPower() <= 1) {
/*     */                       try
/*     */                       {
/*     */                         
/* 805 */                         performer.getCommunicator().sendNormalServerMessage("The " + fish
/* 806 */                             .getName() + " bites you!");
/* 807 */                         Server.getInstance().broadCastAction(performer
/* 808 */                             .getName() + " is bit by the " + fish.getName() + "!", performer, 5);
/*     */                         
/* 810 */                         performer.addWoundOfType(null, (byte)3, 0, true, 1.0F, false, 
/* 811 */                             Math.max(3000, fish.getWeightGrams() / 10), 0.0F, 3.0F, false, false);
/*     */                       }
/* 813 */                       catch (Exception ex)
/*     */                       {
/* 815 */                         logger.log(Level.WARNING, performer.getName() + ": " + ex.getMessage(), ex);
/*     */                       }
/*     */                     
/*     */                     }
/*     */                   }
/*     */                 
/*     */                 }
/* 822 */                 else if (fish.getWeightGrams() > 500)
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 829 */                   if (result < -80.0D && !source.isWand() && Server.rand
/* 830 */                     .nextInt(Math.max(1, 30 - (int)fishing.getKnowledge(0.0D))) == 0)
/*     */                   {
/* 832 */                     done = true;
/* 833 */                     performer.getCommunicator().sendNormalServerMessage("The line snaps, and the fish escapes!");
/*     */                     
/* 835 */                     Server.getInstance().broadCastAction(performer
/* 836 */                         .getName() + "s line snaps and the fish escapes.", performer, 5);
/*     */ 
/*     */ 
/*     */                     
/* 840 */                     fishOnHook = 0;
/*     */ 
/*     */                     
/* 843 */                     source.setTemplateId(780);
/* 844 */                     source.setWeight(1000, false);
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
/*     */                   }
/* 880 */                   else if (result < -60.0D)
/*     */                   {
/* 882 */                     performer.getCommunicator().sendNormalServerMessage("The fish pulls very hard on the line, making the rod creak.");
/*     */                     
/* 884 */                     float damMod = getDamModFor(fish);
/*     */                     
/* 886 */                     source.setDamage(source.getDamage() + 
/* 887 */                         Math.max(0.1F, 
/*     */                           
/* 889 */                           Math.min((100.0F - source
/* 890 */                             .getCurrentQualityLevel()) / 100.0F, source
/* 891 */                             .getDamageModifier() / 10.0F * 
/* 892 */                             Math.max(10.0F, source.getQualityLevel())) * damMod));
/*     */                   }
/*     */                 
/*     */                 }
/*     */                 else
/*     */                 {
/* 898 */                   int mess = Server.rand.nextInt(4);
/* 899 */                   if (mess == 0)
/*     */                   {
/* 901 */                     performer.getCommunicator().sendNormalServerMessage("The fish pulls hard on the line, bending the rod.");
/*     */                     
/* 903 */                     Server.getInstance().broadCastAction("The fish pulls hard on " + performer
/* 904 */                         .getName() + "'s rod and bends it.", performer, 5);
/*     */                   
/*     */                   }
/* 907 */                   else if (mess == 1)
/*     */                   {
/* 909 */                     performer.getCommunicator().sendNormalServerMessage("The fish seems tired and you pull in the line a bit.");
/*     */                     
/* 911 */                     Server.getInstance().broadCastAction(performer
/* 912 */                         .getName() + " pulls in the line a bit.", performer, 5);
/*     */                   }
/* 914 */                   else if (mess == 2)
/*     */                   {
/* 916 */                     performer.getCommunicator().sendNormalServerMessage("The water splashes as the fish fights in the water.");
/*     */                     
/* 918 */                     Server.getInstance().broadCastAction("The water splashes as " + performer
/* 919 */                         .getName() + "'s fish fights in the water", performer, 5);
/*     */                   }
/*     */                 
/*     */                 }
/*     */               
/*     */               }
/*     */             }
/*     */           
/* 927 */           } else if (act.currentSecond() > 1 && act.currentSecond() % 60 == 0) {
/*     */             
/* 929 */             fishing.skillCheck(50.0D, source, 0.0D, false, 1.0F);
/*     */           } 
/* 931 */           if (act.currentSecond() > 180) {
/*     */             
/* 933 */             done = true;
/* 934 */             if (fishOnHook == 0) {
/*     */               
/* 936 */               performer.getCommunicator().sendNormalServerMessage("You pull in the line and stop fishing.");
/* 937 */               Server.getInstance().broadCastAction(performer.getName() + " pulls in the line and stops fishing.", performer, 5);
/*     */             
/*     */             }
/*     */             else {
/*     */               
/* 942 */               performer.getCommunicator().sendNormalServerMessage("The fish breaks loose and swims away.");
/* 943 */               Server.getInstance().broadCastAction(performer.getName() + "'s fish breaks loose and swims away.", performer, 5);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 950 */     return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Fish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
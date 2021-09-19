/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MeshTile;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.server.spells.Spells;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ final class TileBorderBehaviour
/*     */   extends Behaviour
/*     */   implements MiscConstants
/*     */ {
/*     */   TileBorderBehaviour() {
/*  60 */     super((short)32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, boolean border, int heightOffset) {
/*  67 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  68 */     if (hasHoleEachSide(tilex, tiley, onSurface, dir))
/*  69 */       return toReturn; 
/*  70 */     toReturn.addAll(super.getBehavioursFor(performer, subject, tilex, tiley, onSurface, dir, border, heightOffset));
/*  71 */     toReturn.add(Actions.actionEntrys[607]);
/*  72 */     int templateId = subject.getTemplateId();
/*  73 */     if (onSurface && (subject.isMineDoor() || subject.getTemplateId() == 315 || subject.getTemplateId() == 176)) {
/*     */       
/*  75 */       int[] opening = Terraforming.getCaveOpeningCoords(tilex, tiley);
/*  76 */       if (opening[0] != -1 && opening[1] != -1)
/*     */       {
/*  78 */         if (!isWideEntrance(opening[0], opening[1])) {
/*  79 */           toReturn.add(Actions.actionEntrys[363]);
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/*  84 */     boolean hasMarker = hasMarker(tilex, tiley, onSurface, dir);
/*  85 */     if (!hasMarker && MethodsStructure.isCorrectToolForBuilding(performer, templateId)) {
/*     */ 
/*     */       
/*  88 */       boolean ok = onSurface;
/*  89 */       if (!onSurface) {
/*     */         
/*  91 */         VolaTile vt = Zones.getOrCreateTile(tilex, tiley, onSurface);
/*  92 */         if (vt.getVillage() != null)
/*  93 */           ok = true; 
/*     */       } 
/*  95 */       if (ok) {
/*     */         
/*  97 */         toReturn.add(new ActionEntry((short)-12, "Fence", "Fence options"));
/*     */         
/*  99 */         toReturn.add(new ActionEntry((short)-5, "Iron", "Fence options"));
/* 100 */         toReturn.add(Actions.actionEntrys[611]);
/* 101 */         toReturn.add(Actions.actionEntrys[477]);
/* 102 */         toReturn.add(Actions.actionEntrys[479]);
/* 103 */         toReturn.add(Actions.actionEntrys[545]);
/* 104 */         toReturn.add(Actions.actionEntrys[546]);
/*     */         
/* 106 */         toReturn.add(new ActionEntry((short)-2, "Log", "Fence options"));
/* 107 */         toReturn.add(Actions.actionEntrys[165]);
/* 108 */         toReturn.add(Actions.actionEntrys[167]);
/*     */         
/* 110 */         toReturn.add(new ActionEntry((short)-8, "Marble", "Fence options"));
/* 111 */         toReturn.add(Actions.actionEntrys[844]);
/* 112 */         toReturn.add(Actions.actionEntrys[845]);
/* 113 */         toReturn.add(Actions.actionEntrys[846]);
/* 114 */         toReturn.add(Actions.actionEntrys[900]);
/* 115 */         toReturn.add(Actions.actionEntrys[901]);
/* 116 */         toReturn.add(Actions.actionEntrys[902]);
/* 117 */         toReturn.add(Actions.actionEntrys[903]);
/*     */         
/* 119 */         toReturn.add(Actions.actionEntrys[905]);
/*     */         
/* 121 */         toReturn.add(new ActionEntry((short)-4, "Plank", "Fence options"));
/* 122 */         toReturn.add(Actions.actionEntrys[520]);
/* 123 */         toReturn.add(Actions.actionEntrys[528]);
/* 124 */         toReturn.add(Actions.actionEntrys[166]);
/* 125 */         toReturn.add(Actions.actionEntrys[168]);
/*     */         
/* 127 */         toReturn.add(new ActionEntry((short)-8, "Pottery", "Fence options"));
/* 128 */         toReturn.add(Actions.actionEntrys[838]);
/* 129 */         toReturn.add(Actions.actionEntrys[839]);
/* 130 */         toReturn.add(Actions.actionEntrys[840]);
/* 131 */         toReturn.add(Actions.actionEntrys[894]);
/* 132 */         toReturn.add(Actions.actionEntrys[895]);
/* 133 */         toReturn.add(Actions.actionEntrys[896]);
/* 134 */         toReturn.add(Actions.actionEntrys[897]);
/*     */         
/* 136 */         toReturn.add(Actions.actionEntrys[899]);
/*     */         
/* 138 */         toReturn.add(new ActionEntry((short)-2, "Rope", "Rope options"));
/* 139 */         toReturn.add(Actions.actionEntrys[544]);
/* 140 */         toReturn.add(Actions.actionEntrys[543]);
/*     */         
/* 142 */         toReturn.add(new ActionEntry((short)-8, "Rounded stone", "Fence options"));
/* 143 */         toReturn.add(Actions.actionEntrys[835]);
/* 144 */         toReturn.add(Actions.actionEntrys[836]);
/* 145 */         toReturn.add(Actions.actionEntrys[837]);
/* 146 */         toReturn.add(Actions.actionEntrys[876]);
/* 147 */         toReturn.add(Actions.actionEntrys[877]);
/* 148 */         toReturn.add(Actions.actionEntrys[878]);
/* 149 */         toReturn.add(Actions.actionEntrys[879]);
/*     */         
/* 151 */         toReturn.add(Actions.actionEntrys[881]);
/*     */         
/* 153 */         toReturn.add(new ActionEntry((short)-8, "Sandstone", "Fence options"));
/* 154 */         toReturn.add(Actions.actionEntrys[841]);
/* 155 */         toReturn.add(Actions.actionEntrys[842]);
/* 156 */         toReturn.add(Actions.actionEntrys[843]);
/* 157 */         toReturn.add(Actions.actionEntrys[882]);
/* 158 */         toReturn.add(Actions.actionEntrys[883]);
/* 159 */         toReturn.add(Actions.actionEntrys[884]);
/* 160 */         toReturn.add(Actions.actionEntrys[885]);
/*     */         
/* 162 */         toReturn.add(Actions.actionEntrys[887]);
/*     */         
/* 164 */         toReturn.add(new ActionEntry((short)-3, "Shaft", "Fence options"));
/* 165 */         toReturn.add(Actions.actionEntrys[527]);
/* 166 */         toReturn.add(Actions.actionEntrys[526]);
/* 167 */         toReturn.add(Actions.actionEntrys[529]);
/*     */         
/* 169 */         toReturn.add(new ActionEntry((short)-8, "Slate", "Fence options"));
/* 170 */         toReturn.add(Actions.actionEntrys[832]);
/* 171 */         toReturn.add(Actions.actionEntrys[833]);
/* 172 */         toReturn.add(Actions.actionEntrys[834]);
/* 173 */         toReturn.add(Actions.actionEntrys[870]);
/* 174 */         toReturn.add(Actions.actionEntrys[871]);
/* 175 */         toReturn.add(Actions.actionEntrys[872]);
/* 176 */         toReturn.add(Actions.actionEntrys[873]);
/*     */         
/* 178 */         toReturn.add(Actions.actionEntrys[875]);
/*     */         
/* 180 */         toReturn.add(new ActionEntry((short)-5, "Stone", "Fence options"));
/* 181 */         toReturn.add(Actions.actionEntrys[542]);
/* 182 */         toReturn.add(Actions.actionEntrys[163]);
/* 183 */         toReturn.add(Actions.actionEntrys[654]);
/* 184 */         toReturn.add(Actions.actionEntrys[541]);
/* 185 */         toReturn.add(Actions.actionEntrys[164]);
/*     */         
/* 187 */         toReturn.add(new ActionEntry((short)-1, "Woven", "Fence options"));
/* 188 */         toReturn.add(Actions.actionEntrys[478]);
/*     */       } 
/*     */     } 
/*     */     
/* 192 */     if (onSurface && subject.getTemplateId() == 266) {
/*     */       
/* 194 */       if (!hasMarker) {
/* 195 */         toReturn.add(Actions.actionEntrys[186]);
/*     */       }
/* 197 */     } else if (onSurface && subject.isTrellis()) {
/*     */       
/* 199 */       toReturn.add(new ActionEntry((short)-3, "Plant", "Plant options"));
/* 200 */       toReturn.add(Actions.actionEntrys[746]);
/* 201 */       toReturn.add(new ActionEntry((short)176, "In center", "planting"));
/* 202 */       toReturn.add(Actions.actionEntrys[747]);
/*     */     }
/* 204 */     else if (onSurface && subject.isFlower()) {
/*     */       
/* 206 */       if (!hasMarker) {
/* 207 */         toReturn.add(Actions.actionEntrys[563]);
/*     */       }
/* 209 */     } else if (onSurface && subject.isDiggingtool()) {
/*     */       
/* 211 */       toReturn.add(Actions.actionEntrys[533]);
/* 212 */       toReturn.add(Actions.actionEntrys[865]);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     if (subject.isMagicStaff() || (templateId == 176 && performer
/* 219 */       .getPower() >= 4 && Servers.isThisATestServer())) {
/*     */       
/* 221 */       List<ActionEntry> slist = new LinkedList<>();
/*     */       
/* 223 */       if (performer.knowsKarmaSpell(556))
/* 224 */         slist.add(Actions.actionEntrys[556]); 
/* 225 */       if (performer.knowsKarmaSpell(557))
/* 226 */         slist.add(Actions.actionEntrys[557]); 
/* 227 */       if (performer.knowsKarmaSpell(558)) {
/* 228 */         slist.add(Actions.actionEntrys[558]);
/*     */       }
/* 230 */       if (performer.getPower() >= 4)
/* 231 */         toReturn.add(new ActionEntry((short)-slist.size(), "Sorcery", "casting")); 
/* 232 */       toReturn.addAll(slist);
/*     */     } 
/* 234 */     if (onSurface && (templateId == 176 || templateId == 315) && performer.getPower() >= 2)
/* 235 */       toReturn.add(Actions.actionEntrys[64]); 
/* 236 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, boolean border, int heightOffset) {
/* 243 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 244 */     if (hasHoleEachSide(tilex, tiley, onSurface, dir))
/* 245 */       return toReturn; 
/* 246 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, dir, border, heightOffset));
/* 247 */     toReturn.add(Actions.actionEntrys[607]);
/* 248 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, Tiles.TileBorderDirection dir, long borderId, short action, float counter) {
/* 256 */     boolean done = true;
/* 257 */     if (action == 1) {
/*     */       
/* 259 */       done = action(act, performer, tilex, tiley, onSurface, dir, borderId, action, counter);
/*     */     }
/* 261 */     else if (!hasHoleEachSide(tilex, tiley, onSurface, dir)) {
/*     */ 
/*     */ 
/*     */       
/* 265 */       if (Actions.isActionBuildFence(action))
/*     */       
/*     */       { 
/* 268 */         boolean ok = onSurface;
/* 269 */         if (!onSurface) {
/*     */           
/* 271 */           VolaTile vt = Zones.getOrCreateTile(tilex, tiley, onSurface);
/* 272 */           if (vt.getVillage() != null) {
/* 273 */             ok = true;
/*     */           } else {
/* 275 */             performer.getCommunicator().sendNormalServerMessage("You are not allowed to make a fence in a cave when not on a deed.");
/*     */           } 
/* 277 */         }  if (ok)
/*     */         {
/* 279 */           if (!hasMarker(tilex, tiley, onSurface, dir)) {
/* 280 */             done = MethodsStructure.buildFence(act, performer, source, tilex, tiley, onSurface, heightOffset, dir, borderId, action, counter);
/*     */           } else {
/*     */             
/* 283 */             performer.getCommunicator().sendNormalServerMessage("You are not allowed to make a fence across a highway.");
/*     */           } 
/*     */         } }
/* 286 */       else if (onSurface && action == 533 && source.isDiggingtool())
/*     */       
/* 288 */       { done = Flattening.flattenTileBorder(borderId, performer, source, tilex, tiley, dir, counter, act); }
/*     */       
/* 290 */       else if (onSurface && action == 865 && source.isDiggingtool())
/*     */       
/* 292 */       { done = Flattening.flattenTileBorder(borderId, performer, source, tilex, tiley, dir, counter, act); }
/*     */       
/* 294 */       else if (onSurface && action == 186)
/*     */       
/* 296 */       { if (!hasMarker(tilex, tiley, onSurface, dir)) {
/* 297 */           done = Terraforming.plantHedge(performer, source, tilex, tiley, onSurface, dir, counter, act);
/*     */         } else {
/* 299 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to plant a hedge across a highway.");
/*     */         }  }
/* 301 */       else if (onSurface && source.isTrellis() && (action == 176 || action == 746 || action == 747))
/*     */       
/* 303 */       { done = Terraforming.plantTrellis(performer, source, tilex, tiley, onSurface, dir, action, counter, act); }
/*     */       
/* 305 */       else if (onSurface && action == 563)
/*     */       
/* 307 */       { if (!hasMarker(tilex, tiley, onSurface, dir)) {
/* 308 */           done = Terraforming.plantFlowerbed(performer, source, tilex, tiley, onSurface, dir, counter, act);
/*     */         } else {
/* 310 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to plant a flowerbed across a highway.");
/*     */         }  }
/* 312 */       else if (onSurface && action == 363)
/*     */       
/* 314 */       { if (!hasMarker(tilex, tiley, onSurface, dir)) {
/*     */           
/* 316 */           int[] opening = Terraforming.getCaveOpeningCoords(tilex, tiley);
/* 317 */           if (opening[0] != -1 && opening[1] != -1)
/*     */           {
/* 319 */             if (!isWideEntrance(opening[0], opening[1])) {
/* 320 */               done = Terraforming.buildMineDoor(performer, source, act, opening[0], opening[1], onSurface, counter);
/*     */             } else {
/* 322 */               performer.getCommunicator().sendNormalServerMessage("You are not allowed to add a minedoor on wide mine entrances.");
/*     */             } 
/*     */           }
/*     */         } else {
/* 326 */           performer.getCommunicator().sendNormalServerMessage("You are not allowed to add a minedoor across a highway.");
/*     */         }  }
/* 328 */       else if (action == 64)
/*     */       
/* 330 */       { if (performer.getPower() >= 5)
/*     */         {
/* 332 */           if (source.getTemplateId() == 176) {
/*     */             try
/*     */             {
/*     */               
/* 336 */               Zone z = Zones.getZone(tilex, tiley, onSurface); int x;
/* 337 */               for (x = z.getStartX(); x < z.getStartX() + z.getSize(); x++) {
/*     */ 
/*     */                 
/*     */                 try {
/* 341 */                   ItemFactory.createItem(344, 2.0F, ((x << 2) + 2), ((z.getStartY() << 2) + 2), 1.0F, true, (byte)0, performer
/* 342 */                       .getBridgeId(), performer.getName());
/*     */                 }
/* 344 */                 catch (FailedException failedException) {
/*     */ 
/*     */                 
/* 347 */                 } catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 352 */               for (x = z.getStartX(); x < z.getStartX() + z.getSize(); x++) {
/*     */ 
/*     */                 
/*     */                 try {
/* 356 */                   ItemFactory.createItem(344, 2.0F, ((x << 2) + 2), ((z.getEndY() << 2) + 2), 1.0F, true, (byte)0, performer
/* 357 */                       .getBridgeId(), performer.getName());
/*     */                 }
/* 359 */                 catch (FailedException failedException) {
/*     */ 
/*     */                 
/* 362 */                 } catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */               } 
/*     */ 
/*     */               
/* 366 */               for (x = z.getStartY(); x < z.getStartY() + z.getSize(); x++) {
/*     */                 
/* 368 */                 if (x != z.getStartY()) {
/*     */                   
/*     */                   try {
/*     */                     
/* 372 */                     ItemFactory.createItem(344, 2.0F, ((z.getStartX() << 2) + 2), ((x << 2) + 2), 1.0F, true, (byte)0, performer
/* 373 */                         .getBridgeId(), performer.getName());
/*     */                   }
/* 375 */                   catch (FailedException failedException) {
/*     */ 
/*     */                   
/* 378 */                   } catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */                 }
/*     */               } 
/*     */ 
/*     */               
/* 383 */               for (x = z.getStartY(); x < z.getStartY() + z.getSize(); x++)
/*     */               {
/* 385 */                 if (x != z.getStartY()) {
/*     */                   
/*     */                   try {
/*     */                     
/* 389 */                     ItemFactory.createItem(344, 2.0F, ((z.getEndX() << 2) + 2), ((x << 2) + 2), 1.0F, true, (byte)0, performer
/* 390 */                         .getBridgeId(), performer.getName());
/*     */                   }
/* 392 */                   catch (FailedException failedException) {
/*     */ 
/*     */                   
/* 395 */                   } catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */                 
/*     */                 }
/*     */               }
/*     */             
/*     */             }
/* 401 */             catch (NoSuchZoneException nsz)
/*     */             {
/* 403 */               performer.getCommunicator().sendNormalServerMessage("No zone at " + tilex + ", " + tiley + "," + onSurface + ".");
/*     */             }
/*     */           
/*     */           }
/*     */         } }
/* 408 */       else if (act.isSpell())
/*     */       
/* 410 */       { Spell spell = Spells.getSpell(action);
/*     */ 
/*     */         
/* 413 */         int layer = onSurface ? 0 : -1;
/* 414 */         if (source.isMagicStaff() || (source
/* 415 */           .getTemplateId() == 176 && performer.getPower() >= 2 && Servers.isThisATestServer())) {
/*     */ 
/*     */           
/* 418 */           if (Methods.isActionAllowed(performer, (short)547)) {
/* 419 */             done = Methods.castSpell(performer, spell, tilex, tiley, layer, heightOffset, dir, counter);
/*     */           }
/*     */         } else {
/*     */           
/* 423 */           performer.getCommunicator().sendNormalServerMessage("You need to use a magic staff.");
/* 424 */           done = true;
/*     */         }
/*     */          }
/* 427 */       else if (onSurface && heightOffset == 0 && source.isMiningtool() && action == 145)
/*     */       
/* 429 */       { int digTilex = (int)performer.getStatus().getPositionX() + 2 >> 2;
/* 430 */         int digTiley = (int)performer.getStatus().getPositionY() + 2 >> 2;
/* 431 */         int tile = Server.surfaceMesh.getTile(digTilex, digTiley);
/* 432 */         byte type = Tiles.decodeType(tile);
/* 433 */         if (type == Tiles.Tile.TILE_ROCK.id) {
/* 434 */           done = TileRockBehaviour.mine(act, performer, source, tilex, tiley, action, counter, digTilex, digTiley);
/*     */         } }
/*     */       else
/* 437 */       { return action(act, performer, tilex, tiley, onSurface, dir, borderId, action, counter); } 
/* 438 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, long borderId, short action, float counter) {
/* 446 */     if (action == 1)
/* 447 */     { handle_EXAMINE(performer, tilex, tiley, onSurface, dir); }
/* 448 */     else { if (hasHoleEachSide(tilex, tiley, onSurface, dir))
/* 449 */         return true; 
/* 450 */       if (action == 607)
/* 451 */         performer.getCommunicator().sendAddTileBorderToCreationWindow(borderId);  }
/* 452 */      return true;
/*     */   }
/*     */   
/*     */   private static void handle_EXAMINE(Creature performer, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir) {
/*     */     MeshIO mesh;
/* 457 */     Communicator comm = performer.getCommunicator();
/* 458 */     if (hasHoleEachSide(tilex, tiley, onSurface, dir)) {
/* 459 */       comm.sendNormalServerMessage("This is in the middle of a wide cave entrance.");
/*     */     } else {
/* 461 */       comm.sendNormalServerMessage("This outlines where fences and walls may be built.");
/*     */     } 
/*     */     
/* 464 */     if (tilex - 2 < 0 || tilex + 2 > 1 << Constants.meshSize || tiley - 2 < 0 || tiley + 2 > 1 << Constants.meshSize) {
/*     */ 
/*     */       
/* 467 */       comm.sendNormalServerMessage("The water is too deep to measure.");
/*     */       return;
/*     */     } 
/* 470 */     if (!performer.isWithinTileDistanceTo(tilex, tiley, 20, 3)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 475 */     if (onSurface) {
/* 476 */       mesh = Server.surfaceMesh;
/*     */     } else {
/* 478 */       mesh = Server.caveMesh;
/*     */     } 
/* 480 */     short height = Tiles.decodeHeight(mesh.getTile(tilex, tiley));
/* 481 */     boolean away = false;
/* 482 */     short endheight = height;
/* 483 */     int diff = 0;
/* 484 */     if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*     */       
/* 486 */       int endx = tilex + 1;
/* 487 */       endheight = Tiles.decodeHeight(mesh.getTile(endx, tiley));
/* 488 */       float posx = performer.getPosX();
/* 489 */       diff = Math.abs(height - endheight);
/* 490 */       if (Math.abs(posx - (endx << 2)) > Math.abs(posx - (tilex << 2))) {
/* 491 */         away = true;
/*     */       }
/* 493 */     } else if (dir == Tiles.TileBorderDirection.DIR_DOWN) {
/*     */       
/* 495 */       int endy = tiley + 1;
/* 496 */       endheight = Tiles.decodeHeight(mesh.getTile(tilex, endy));
/* 497 */       float posy = performer.getPosY();
/* 498 */       diff = Math.abs(height - endheight);
/* 499 */       if (Math.abs(posy - (endy << 2)) > Math.abs(posy - (tiley << 2)))
/* 500 */         away = true; 
/*     */     } 
/* 502 */     String dist = "up.";
/* 503 */     if ((away && height > endheight) || (!away && endheight > height))
/*     */     {
/*     */       
/* 506 */       dist = "down.";
/*     */     }
/* 508 */     if (diff != 0) {
/* 509 */       comm.sendNormalServerMessage("The border is " + diff + " slope " + dist);
/*     */     } else {
/* 511 */       comm.sendNormalServerMessage("The border is level.");
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
/*     */   private static boolean hasHoleEachSide(int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir) {
/* 523 */     if (!onSurface)
/* 524 */       return false; 
/* 525 */     MeshTile mTileCurrent = new MeshTile(Server.surfaceMesh, tilex, tiley);
/* 526 */     if (dir == Tiles.TileBorderDirection.DIR_HORIZ) {
/*     */ 
/*     */       
/* 529 */       MeshTile mTileNorth = mTileCurrent.getNorthMeshTile();
/* 530 */       if (mTileCurrent.isHole() && mTileNorth.isHole()) {
/* 531 */         return true;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 536 */       MeshTile mTileWest = mTileCurrent.getWestMeshTile();
/* 537 */       if (mTileCurrent.isHole() && mTileWest.isHole())
/* 538 */         return true; 
/*     */     } 
/* 540 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isWideEntrance(int tilex, int tiley) {
/* 545 */     MeshTile mTileCurrent = new MeshTile(Server.surfaceMesh, tilex, tiley);
/* 546 */     if (mTileCurrent.isHole()) {
/*     */       
/* 548 */       MeshTile mTileNorth = mTileCurrent.getNorthMeshTile();
/* 549 */       if (mTileNorth.isHole())
/* 550 */         return true; 
/* 551 */       MeshTile mTileWest = mTileCurrent.getWestMeshTile();
/* 552 */       if (mTileWest.isHole())
/* 553 */         return true; 
/* 554 */       MeshTile mTileSouth = mTileCurrent.getSouthMeshTile();
/* 555 */       if (mTileSouth.isHole())
/* 556 */         return true; 
/* 557 */       MeshTile mTileEast = mTileCurrent.getEastMeshTile();
/* 558 */       if (mTileEast.isHole())
/* 559 */         return true; 
/*     */     } 
/* 561 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasMarker(int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir) {
/* 568 */     if (Items.getMarker(tilex, tiley, onSurface, 0, -10L) != null)
/* 569 */       return true; 
/* 570 */     if (dir == Tiles.TileBorderDirection.DIR_HORIZ && Items.getMarker(tilex + 1, tiley, onSurface, 0, -10L) != null)
/* 571 */       return true; 
/* 572 */     if (dir == Tiles.TileBorderDirection.DIR_DOWN && Items.getMarker(tilex, tiley + 1, onSurface, 0, -10L) != null) {
/* 573 */       return true;
/*     */     }
/*     */     
/* 576 */     if (Tiles.decodeType(Server.surfaceMesh.getTile(tilex, tiley)) == Tiles.Tile.TILE_HOLE.id) {
/*     */ 
/*     */       
/* 579 */       if (Items.getMarker(tilex, tiley, !onSurface, 0, -10L) != null)
/* 580 */         return true; 
/* 581 */       if (dir == Tiles.TileBorderDirection.DIR_HORIZ && Items.getMarker(tilex + 1, tiley, !onSurface, 0, -10L) != null)
/* 582 */         return true; 
/* 583 */       if (dir == Tiles.TileBorderDirection.DIR_DOWN && Items.getMarker(tilex, tiley + 1, !onSurface, 0, -10L) != null)
/* 584 */         return true; 
/*     */     } 
/* 586 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileBorderBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
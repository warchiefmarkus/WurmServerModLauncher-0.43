/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.RuneUtilities;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.Trap;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class TileFieldBehaviour
/*     */   extends TileBehaviour
/*     */ {
/*  61 */   private static final Logger logger = Logger.getLogger(TileFieldBehaviour.class.getName());
/*     */   
/*  63 */   private static final String[] harvestStrings = new String[] { "The ground is freshly sown and the seeds are evolving.", "A few green blades pop out of the ground.", "Small sprouts with many blades grow here.", "The sprouts are growing, a bit above half their mature height.", "The field is almost at full height.", "The field is at full height and ready to harvest!", "The field is at full height and ready to harvest!", "The field is barren, with weeds rotting in the dirt." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TileFieldBehaviour() {
/*  74 */     super((short)17);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile) {
/*  81 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  82 */     toReturn.addAll(super.getBehavioursFor(performer, subject, tilex, tiley, onSurface, tile));
/*  83 */     int data = Tiles.decodeData(tile) & 0xFF;
/*  84 */     int tileState = data >> 4;
/*  85 */     int tileAge = tileState & 0x7;
/*  86 */     boolean farmed = (tileState >> 3 == 1);
/*  87 */     int crop = data & 0xF;
/*     */     
/*  89 */     if (!farmed)
/*     */     {
/*  91 */       if (subject.isFieldTool() && !Zones.protectedTiles[tilex][tiley]) {
/*  92 */         toReturn.add(Actions.actionEntrys[151]);
/*     */       }
/*     */     }
/*  95 */     if (tileAge != 0 && tileAge != 7 && !Zones.protectedTiles[tilex][tiley])
/*     */     {
/*  97 */       if (subject.getTemplateId() == 268) {
/*     */         
/*  99 */         toReturn.add(Actions.actionEntrys[152]);
/*     */       }
/* 101 */       else if (crop > 3 || Tiles.decodeType(tile) == Tiles.Tile.TILE_FIELD2.id) {
/* 102 */         toReturn.add(Actions.actionEntrys[152]);
/*     */       } 
/*     */     }
/* 105 */     if (tileAge < 7)
/*     */     {
/* 107 */       if (subject.getTemplateId() == 176 && performer.getPower() >= 2)
/* 108 */         toReturn.add(Actions.actionEntrys[188]); 
/*     */     }
/* 110 */     if (subject.getTemplateId() == 526)
/*     */     {
/* 112 */       toReturn.add(Actions.actionEntrys[118]);
/*     */     }
/* 114 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/* 121 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 122 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, tile));
/*     */     
/* 124 */     int data = Tiles.decodeData(tile) & 0xFF;
/* 125 */     int tileState = data >> 4;
/* 126 */     int tileAge = tileState & 0x7;
/* 127 */     int crop = data & 0xF;
/* 128 */     if (tileAge != 0 && tileAge != 7 && !Zones.protectedTiles[tilex][tiley])
/*     */     {
/* 130 */       if (crop > 3 || Tiles.decodeType(tile) == Tiles.Tile.TILE_FIELD2.id) {
/* 131 */         toReturn.add(Actions.actionEntrys[152]);
/*     */       }
/*     */     }
/* 134 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, short action, float counter) {
/* 141 */     boolean done = true;
/* 142 */     if (action == 1) {
/*     */       
/* 144 */       byte data = Tiles.decodeData(tile);
/* 145 */       byte type = Tiles.decodeType(tile);
/* 146 */       int tileAge = Crops.decodeFieldAge(data);
/* 147 */       boolean farmed = Crops.decodeFieldState(data);
/* 148 */       int crop = Crops.getCropNumber(type, data);
/* 149 */       Communicator comm = performer.getCommunicator();
/* 150 */       Trap t = Trap.getTrap(tilex, tiley, performer.getLayer());
/* 151 */       if (performer.getPower() > 2) {
/*     */         
/* 153 */         comm.sendNormalServerMessage("data=" + data + ", age=" + tileAge + ", farmed=" + farmed + ", crop=" + crop + ".");
/*     */         
/* 155 */         if (t != null)
/*     */         {
/* 157 */           String villageName = "none";
/* 158 */           if (t.getVillage() > 0) {
/*     */             
/*     */             try {
/*     */               
/* 162 */               villageName = Villages.getVillage(t.getVillage()).getName();
/*     */             }
/* 164 */             catch (NoSuchVillageException noSuchVillageException) {}
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 169 */           comm.sendNormalServerMessage("A " + t.getName() + ", ql=" + t.getQualityLevel() + " kingdom=" + 
/* 170 */               Kingdoms.getNameFor(t.getKingdom()) + ", vill=" + villageName + ", rotdam=" + t.getRotDamage() + " firedam=" + t
/* 171 */               .getFireDamage() + " speed=" + t.getSpeedBon());
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 176 */       else if (t != null) {
/*     */         
/* 178 */         if (t.getKingdom() == performer.getKingdomId() || performer.getDetectDangerBonus() > 0.0F) {
/*     */           
/* 180 */           String qlString = "average";
/* 181 */           if (t.getQualityLevel() < 20) {
/* 182 */             qlString = "low";
/* 183 */           } else if (t.getQualityLevel() > 80) {
/* 184 */             qlString = "deadly";
/* 185 */           } else if (t.getQualityLevel() > 50) {
/* 186 */             qlString = "high";
/* 187 */           }  String villageName = ".";
/* 188 */           if (t.getVillage() > 0) {
/*     */             
/*     */             try {
/*     */               
/* 192 */               villageName = " of " + Villages.getVillage(t.getVillage()).getName() + ".";
/*     */             }
/* 194 */             catch (NoSuchVillageException noSuchVillageException) {}
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 199 */           String rotDam = "";
/* 200 */           if (t.getRotDamage() > 0)
/* 201 */             rotDam = " It has ugly black-green speckles."; 
/* 202 */           String fireDam = "";
/* 203 */           if (t.getFireDamage() > 0)
/* 204 */             fireDam = " It has the rune of fire."; 
/* 205 */           StringBuilder buf = new StringBuilder();
/* 206 */           buf.append("You detect a ");
/* 207 */           buf.append(t.getName());
/* 208 */           buf.append(" here, of ");
/* 209 */           buf.append(qlString);
/* 210 */           buf.append(" quality.");
/* 211 */           buf.append(" It has been set by people from ");
/* 212 */           buf.append(Kingdoms.getNameFor(t.getKingdom()));
/* 213 */           buf.append(villageName);
/* 214 */           buf.append(rotDam);
/* 215 */           buf.append(fireDam);
/* 216 */           comm.sendNormalServerMessage(buf.toString());
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 221 */       String growString = "The crops grow steadily.";
/* 222 */       if (!farmed)
/* 223 */         growString = "It could use a touch from the rake or some other farming tool."; 
/* 224 */       if (tileAge != 0 && tileAge != 7) {
/*     */         
/* 226 */         Skill farming = performer.getSkills().getSkillOrLearn(10049);
/* 227 */         String cropString = Crops.getCropName(crop);
/* 228 */         double skill = farming.getKnowledge(0.0D);
/* 229 */         if (skill < 10.0D) {
/* 230 */           comm.sendNormalServerMessage("You see a farmer's field. " + growString);
/* 231 */         } else if (skill < 20.0D) {
/* 232 */           comm.sendNormalServerMessage("You see a farmer's field growing " + cropString + ". " + growString);
/* 233 */         } else if (skill < 60.0D) {
/* 234 */           comm.sendNormalServerMessage("You see a farmer's field growing " + cropString + ". " + harvestStrings[tileAge] + " " + growString);
/*     */         }
/* 236 */         else if (skill <= 100.0D) {
/*     */ 
/*     */           
/* 239 */           float ageYieldFactor = 0.0F;
/*     */           
/* 241 */           if (tileAge >= 3)
/*     */           {
/*     */ 
/*     */             
/* 245 */             if (tileAge < 4) {
/* 246 */               ageYieldFactor = 0.5F;
/* 247 */             } else if (tileAge < 5) {
/* 248 */               ageYieldFactor = 0.7F;
/* 249 */             } else if (tileAge < 7) {
/* 250 */               ageYieldFactor = 1.0F;
/* 251 */             }  }  float realKnowledge = (float)farming.getKnowledge(0.0D);
/* 252 */           int worldResource = Server.getWorldResource(tilex, tiley);
/* 253 */           int farmedCount = worldResource >>> 11;
/* 254 */           int farmedChance = worldResource & 0x7FF;
/*     */ 
/*     */           
/* 257 */           short resource = (short)(farmedChance + Math.min(5, farmedCount) * 50);
/* 258 */           float div = 100.0F - realKnowledge / 15.0F;
/* 259 */           short bonusYield = (short)(int)(resource / div / 1.5F);
/* 260 */           float baseYield = realKnowledge / 15.0F;
/* 261 */           int quantity = (int)((baseYield + bonusYield) * ageYieldFactor);
/*     */           
/* 263 */           if (quantity <= 1 && farmedCount > 0 && (tileAge == 5 || tileAge == 6)) {
/*     */             
/* 265 */             if (farmedCount > 2)
/* 266 */               quantity++; 
/* 267 */             if (farmedCount > 4) {
/* 268 */               quantity++;
/*     */             }
/*     */           } 
/* 271 */           if (quantity == 0 && (tileAge == 5 || tileAge == 6))
/*     */           {
/*     */             
/* 274 */             quantity = 1;
/*     */           }
/*     */           
/* 277 */           String measureString = Crops.getMeasure(crop);
/* 278 */           String amountString = "about " + quantity + " " + cropString + "s.";
/* 279 */           if (measureString.length() > 0)
/* 280 */             amountString = "about " + quantity + " " + measureString + " of " + cropString + "."; 
/* 281 */           comm.sendNormalServerMessage("You see a farmer's field, which would render " + amountString + " " + harvestStrings[tileAge] + " " + growString);
/*     */         }
/*     */       
/*     */       }
/* 285 */       else if (tileAge == 0) {
/* 286 */         comm.sendNormalServerMessage("You see a patch of freshly sown field. " + growString);
/* 287 */       } else if (tileAge == 7) {
/* 288 */         comm.sendNormalServerMessage("You see a patch of soil containing old rotten weeds.");
/* 289 */       }  sendVillageString(performer, tilex, tiley, true);
/*     */     } else {
/* 291 */       if (action == 152 && !Zones.protectedTiles[tilex][tiley])
/*     */       {
/* 293 */         return Terraforming.harvest(performer, tilex, tiley, onSurface, tile, counter, null);
/*     */       }
/* 295 */       if (action == 109)
/*     */       
/* 297 */       { done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter); }
/*     */       
/* 299 */       else if (action == 56)
/* 300 */       { performer.getCommunicator().sendNormalServerMessage("You can't plan a building on a field."); }
/*     */       else
/* 302 */       { done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter); } 
/* 303 */     }  return done;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void graze(int tilex, int tiley, int tile) {
/* 308 */     byte data = Tiles.decodeData(tile);
/* 309 */     byte type = Tiles.decodeType(tile);
/* 310 */     int tileState = data >> 4;
/* 311 */     int tileAge = tileState & 0x7;
/* 312 */     int crop = Crops.getCropNumber(type, data);
/* 313 */     if (tileAge == 7 || tileAge <= 1) {
/*     */       
/* 315 */       Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0);
/* 316 */       Server.setWorldResource(tilex, tiley, 0);
/* 317 */       Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 322 */     tileAge--;
/* 323 */     Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Crops.getTileType(crop), 
/* 324 */         Crops.encodeFieldData(false, tileAge, crop));
/*     */     
/* 326 */     int worldResource = Server.getWorldResource(tilex, tiley);
/* 327 */     int farmedCount = worldResource >>> 11;
/* 328 */     int farmedChance = worldResource & 0x7FF;
/* 329 */     Random rand = new Random();
/*     */ 
/*     */     
/* 332 */     if (rand.nextBoolean()) {
/*     */       
/* 334 */       if (farmedCount == 0) {
/* 335 */         farmedCount++;
/*     */       }
/*     */       
/* 338 */       farmedChance = Math.min(farmedChance + 150, 2047);
/*     */     } 
/* 340 */     Server.setWorldResource(tilex, tiley, (farmedCount << 11) + farmedChance);
/* 341 */     Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/* 350 */     boolean done = true;
/* 351 */     if (action == 151 && source.isFieldTool() && !Zones.protectedTiles[tilex][tiley])
/*     */     
/* 353 */     { if (Tiles.decodeType(tile) != Tiles.Tile.TILE_FIELD.id && Tiles.decodeType(tile) != Tiles.Tile.TILE_FIELD2.id) {
/*     */         
/* 355 */         performer.getCommunicator().sendNormalServerMessage("The ground cannot be farmed any more.", (byte)3);
/* 356 */         return true;
/*     */       } 
/*     */       
/*     */       try {
/* 360 */         if (!Terraforming.isFlat(tilex, tiley, onSurface, 4))
/*     */         {
/* 362 */           performer.getCommunicator().sendNormalServerMessage("The ground is not flat enough for crops to grow. You need to flatten it first.", (byte)3);
/*     */           
/* 364 */           return true;
/*     */         }
/*     */       
/* 367 */       } catch (IllegalArgumentException iae) {
/*     */         
/* 369 */         performer.getCommunicator().sendNormalServerMessage("The water will eat away the field in no time. You cannot farm there.", (byte)3);
/*     */         
/* 371 */         return true;
/*     */       } 
/* 373 */       byte data = Tiles.decodeData(tile);
/* 374 */       byte type = Tiles.decodeType(tile);
/* 375 */       int tileAge = Crops.decodeFieldAge(data);
/* 376 */       boolean farmed = Crops.decodeFieldState(data);
/* 377 */       int crop = Crops.getCropNumber(type, data);
/* 378 */       double difficulty = Crops.getDifficultyFor(crop);
/* 379 */       Skills skills = performer.getSkills();
/* 380 */       Skill farming = skills.getSkillOrLearn(10049);
/* 381 */       Skill tool = null;
/*     */       
/*     */       try {
/* 384 */         tool = skills.getSkill(source.getPrimarySkill());
/*     */       }
/* 386 */       catch (Exception ex) {
/*     */ 
/*     */         
/*     */         try {
/* 390 */           tool = skills.learn(source.getPrimarySkill(), 1.0F);
/*     */         }
/* 392 */         catch (NoSuchSkillException nss) {
/*     */           
/* 394 */           logger.log(Level.INFO, source.getName() + " has no skill related for farming.");
/*     */         } 
/*     */       } 
/* 397 */       int time = 100;
/*     */       
/* 399 */       if (tileAge == 7) {
/*     */         
/* 401 */         done = false;
/* 402 */         if (counter == 1.0F)
/*     */         {
/* 404 */           time = Actions.getStandardActionTime(performer, farming, source, 0.0D);
/* 405 */           act.setTimeLeft(time);
/* 406 */           performer.getCommunicator().sendNormalServerMessage("You start preparing the field for sowing.");
/* 407 */           Server.getInstance().broadCastAction(performer.getName() + " starts preparing the field for sowing.", performer, 5);
/*     */           
/* 409 */           performer.sendActionControl(Actions.actionEntrys[151].getVerbString(), true, time);
/*     */           
/* 411 */           source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 412 */           performer.getStatus().modifyStamina(-1500.0F);
/*     */         }
/*     */         else
/*     */         {
/* 416 */           time = act.getTimeLeft();
/* 417 */           if (act.mayPlaySound())
/*     */           {
/* 419 */             Methods.sendSound(performer, "sound.work.farming.rake");
/*     */           }
/* 421 */           if (act.currentSecond() % 5 == 0) {
/*     */             
/* 423 */             source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/* 424 */             performer.getStatus().modifyStamina(-10000.0F);
/*     */           } 
/* 426 */           if (counter * 10.0F > time)
/*     */           {
/* 428 */             double power = farming.skillCheck(difficulty, source, 0.0D, false, counter);
/* 429 */             if (tool != null)
/* 430 */               tool.skillCheck(difficulty, source, 0.0D, false, counter); 
/* 431 */             done = true;
/*     */ 
/*     */             
/*     */             try {
/* 435 */               int seedTemplate = Crops.getItemTemplate(crop);
/* 436 */               double ql = (farming.getKnowledge(0.0D) + (100.0D - farming.getKnowledge(0.0D)) * ((float)power / 500.0F)) * 0.5D;
/* 437 */               Item result = ItemFactory.createItem(seedTemplate, (float)Math.max(Math.min(ql, 100.0D), 1.0D), null);
/*     */               
/* 439 */               performer.getCommunicator().sendNormalServerMessage("You find " + (
/* 440 */                   result.getName().endsWith("s") ? "some " : "a ") + result.getName() + " in amongst the weeds.");
/* 441 */               if (!performer.getInventory().insertItem(result, true)) {
/* 442 */                 performer.getCommunicator().sendNormalServerMessage("You can't carry the " + result.getName() + ". It falls to the ground and is ruined!");
/*     */               }
/* 444 */             } catch (FailedException|com.wurmonline.server.items.NoSuchTemplateException failedException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 449 */             Server.getInstance().broadCastAction(performer.getName() + " has prepared the field for sowing.", performer, 5);
/*     */             
/* 451 */             performer.getCommunicator().sendNormalServerMessage("The field is now prepared for sowing.");
/*     */             
/* 453 */             Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte)0);
/*     */ 
/*     */             
/* 456 */             Players.getInstance().sendChangedTile(tilex, tiley, onSurface, true);
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 467 */       else if (!farmed) {
/*     */         
/* 469 */         double power = 0.0D;
/* 470 */         double bonus = 0.0D;
/* 471 */         done = false;
/* 472 */         if (counter == 1.0F) {
/*     */           
/* 474 */           time = Actions.getStandardActionTime(performer, farming, source, 0.0D);
/* 475 */           act.setTimeLeft(time);
/* 476 */           Server.getInstance().broadCastAction(performer.getName() + " starts tending the field.", performer, 5);
/*     */           
/* 478 */           performer.getCommunicator().sendNormalServerMessage("You start removing weeds and otherwise put the field in good order.");
/*     */           
/* 480 */           performer.sendActionControl(Actions.actionEntrys[151].getVerbString(), true, time);
/*     */           
/* 482 */           source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/* 483 */           performer.getStatus().modifyStamina(-1500.0F);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 488 */           time = act.getTimeLeft();
/*     */           
/* 490 */           if (act.currentSecond() % 5 == 0) {
/*     */             
/* 492 */             Methods.sendSound(performer, "sound.work.farming.rake");
/* 493 */             source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 494 */             performer.getStatus().modifyStamina(-10000.0F);
/*     */           } 
/*     */         } 
/* 497 */         if (counter * 10.0F > time) {
/*     */           
/* 499 */           if (act.getRarity() != 0)
/* 500 */             performer.playPersonalSound("sound.fx.drumroll"); 
/* 501 */           if (tool != null)
/* 502 */             bonus = tool.skillCheck(difficulty, source, 0.0D, false, counter) / 10.0D; 
/* 503 */           power = Math.max(0.0D, farming.skillCheck(difficulty, source, bonus, false, counter));
/* 504 */           done = true;
/* 505 */           if (power <= 0.0D) {
/* 506 */             performer.getCommunicator().sendNormalServerMessage("The field is tended.");
/* 507 */           } else if (power < 25.0D) {
/* 508 */             performer.getCommunicator().sendNormalServerMessage("The field is now tended.");
/* 509 */           } else if (power < 50.0D) {
/* 510 */             performer.getCommunicator().sendNormalServerMessage("The field looks better after your tending.");
/* 511 */           } else if (power < 75.0D) {
/* 512 */             performer.getCommunicator().sendNormalServerMessage("The field is now groomed.");
/*     */           } else {
/* 514 */             performer.getCommunicator().sendNormalServerMessage("The field is now nicely groomed.");
/* 515 */           }  Server.getInstance().broadCastAction(performer
/* 516 */               .getName() + " is pleased as the field is now in order.", performer, 5);
/*     */           
/* 518 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Crops.getTileType(crop), 
/* 519 */               Crops.encodeFieldData(true, tileAge, crop));
/*     */           
/* 521 */           int worldResource = Server.getWorldResource(tilex, tiley);
/* 522 */           int farmedCount = worldResource >>> 11;
/* 523 */           int farmedChance = worldResource & 0x7FF;
/* 524 */           if (farmedCount < 5) {
/*     */ 
/*     */             
/* 527 */             farmedCount++;
/* 528 */             farmedChance = (int)Math.min(farmedChance + power * 2.0D + (act
/* 529 */                 .getRarity() * 110) + (source.getRarity() * 10), 2047.0D);
/*     */           } 
/*     */           
/* 532 */           if (source.getSpellEffects() != null) {
/*     */             
/* 534 */             float extraChance = source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_FARMYIELD) - 1.0F;
/* 535 */             if (extraChance > 0.0F && Server.rand.nextFloat() < extraChance) {
/*     */               
/* 537 */               performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " seems to have an extra effect on the field.");
/* 538 */               farmedChance = Math.min(farmedChance + 100, 2047);
/* 539 */               if (farmedCount < 5) {
/* 540 */                 farmedCount++;
/*     */               }
/*     */             } 
/*     */           } 
/* 544 */           Server.setWorldResource(tilex, tiley, (farmedCount << 11) + farmedChance);
/* 545 */           if (Servers.isThisATestServer()) {
/* 546 */             performer.getCommunicator().sendNormalServerMessage("farmedCount is:" + farmedCount + " farmedChance is:" + farmedChance);
/*     */           }
/* 548 */           Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/*     */         } 
/*     */       } else {
/*     */         
/* 552 */         performer.getCommunicator().sendNormalServerMessage("The crops are growing nicely and the field is in order already.");
/*     */       }
/*     */        }
/*     */     
/* 556 */     else if (action == 1)
/* 557 */     { done = action(act, performer, tilex, tiley, onSurface, tile, action, counter); }
/* 558 */     else { if (action == 152 && !Zones.protectedTiles[tilex][tiley]) {
/*     */         
/* 560 */         if (source.getTemplateId() == 268) {
/* 561 */           return Terraforming.harvest(performer, tilex, tiley, onSurface, tile, counter, source);
/*     */         }
/* 563 */         return Terraforming.harvest(performer, tilex, tiley, onSurface, tile, counter, null);
/*     */       } 
/* 565 */       if (action == 188) {
/*     */         
/* 567 */         if (source.getTemplateId() == 176 && performer.getPower() >= 2) {
/* 568 */           done = Terraforming.growFarm(performer, tile, tilex, tiley, onSurface);
/*     */         }
/* 570 */       } else if (action == 118 && source.getTemplateId() == 526) {
/*     */         
/* 572 */         performer.getCommunicator().sendNormalServerMessage("You draw a circle in the air in front of you with " + source
/* 573 */             .getNameWithGenus() + ".");
/* 574 */         Server.getInstance().broadCastAction(performer
/* 575 */             .getName() + " draws a circle in the air in front of " + performer.getHimHerItString() + " with " + source
/* 576 */             .getNameWithGenus() + ".", performer, 5);
/* 577 */         done = true;
/*     */         
/* 579 */         if (source.getAuxData() > 0) {
/*     */           
/* 581 */           byte data = Tiles.decodeData(tile);
/* 582 */           byte type = Tiles.decodeType(tile);
/* 583 */           int crop = Crops.getCropNumber(type, data);
/*     */           
/* 585 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Crops.getTileType(crop), 
/* 586 */               Crops.encodeFieldData(false, 5, crop));
/*     */           
/* 588 */           Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/* 589 */           source.setAuxData((byte)(source.getAuxData() - 1));
/*     */         } else {
/*     */           
/* 592 */           performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*     */         } 
/*     */       } else {
/* 595 */         done = super.action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter);
/*     */       }  }
/* 597 */      return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileFieldBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
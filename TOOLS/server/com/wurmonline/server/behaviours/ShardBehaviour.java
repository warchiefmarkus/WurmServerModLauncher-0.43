/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class ShardBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*     */   ShardBehaviour() {
/*  55 */     super((short)46);
/*     */   }
/*     */ 
/*     */   
/*  59 */   private static final Logger logger = Logger.getLogger(MethodsItems.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TYPE_MULT = 17;
/*     */ 
/*     */   
/*     */   private static final int TYPE_OFFSET = 17;
/*     */ 
/*     */   
/*     */   private static final int QL_MULT = 18;
/*     */ 
/*     */   
/*     */   private static final int QL_OFFSET = -3;
/*     */ 
/*     */ 
/*     */   
/*     */   List<ActionEntry> getShardBehaviours(Creature performer, @Nullable Item source, Item target) {
/*  77 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */ 
/*     */     
/*  80 */     if (target != null && (target.isShard() || target.isOre()))
/*     */     {
/*  82 */       toReturn.add(Actions.actionEntrys[536]);
/*     */     }
/*  84 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  90 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  91 */     toReturn.addAll(getShardBehaviours(performer, null, target));
/*  92 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  98 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  99 */     toReturn.addAll(getShardBehaviours(performer, source, target));
/* 100 */     return toReturn;
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
/* 111 */     return performShardAction(act, performer, null, target, action, counter);
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
/* 124 */     return performShardAction(act, performer, source, target, action, counter);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean performShardAction(Action act, Creature performer, @Nullable Item source, Item target, short action, float counter) {
/* 142 */     if (action == 536) {
/*     */       
/* 144 */       boolean done = false;
/* 145 */       if (target != null && (target.isShard() || target.isOre())) {
/*     */         
/* 147 */         if (target.getOwnerId() != performer.getWurmId()) {
/*     */           
/* 149 */           performer.getCommunicator().sendSafeServerMessage("You need to carry the " + target
/* 150 */               .getName() + " in order to analyse it.");
/* 151 */           return true;
/*     */         } 
/*     */         
/* 154 */         int tilex = target.getDataX();
/* 155 */         int tiley = target.getDataY();
/*     */         
/* 157 */         String targetType = target.isOre() ? "ore" : "shard";
/* 158 */         Skills skills = performer.getSkills();
/* 159 */         Skill prospecting = null;
/*     */         
/*     */         try {
/* 162 */           prospecting = skills.getSkill(10032);
/*     */         }
/* 164 */         catch (Exception ex) {
/*     */           
/* 166 */           prospecting = skills.learn(10032, 1.0F);
/*     */         } 
/*     */         
/* 169 */         if (prospecting.getKnowledge(0.0D) <= 20.0D) {
/*     */           
/* 171 */           performer.getCommunicator().sendNormalServerMessage("You are unable to work out how to analyse the " + targetType + ".");
/* 172 */           return true;
/*     */         } 
/*     */ 
/*     */         
/* 176 */         if (counter == 1.0F) {
/*     */           
/* 178 */           if (tilex > 0) {
/* 179 */             target.setDataXY(-tilex, tiley);
/*     */           }
/*     */         } else {
/* 182 */           tilex = -tilex;
/*     */         } 
/* 184 */         if (tilex == -1) {
/*     */           
/* 186 */           performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " looks too old for a decent analysis and therefore you decide not to analyse it.");
/* 187 */           return true;
/*     */         } 
/*     */         
/* 190 */         if (tilex <= 0 || tiley <= 0) {
/*     */           
/* 192 */           performer.getCommunicator().sendNormalServerMessage("It looks like someone has tampered with the " + target.getName() + " and therefore you decide not to analyse it.");
/* 193 */           return true;
/*     */         } 
/*     */         
/* 196 */         if (tilex < 1 || tilex > 1 << Constants.meshSize || tiley < 1 || tiley > 1 << Constants.meshSize) {
/*     */           
/* 198 */           performer.getCommunicator().sendNormalServerMessage("You are unable to determine the origin of the " + target.getName() + ", analysis would be futile.");
/* 199 */           return true;
/*     */         } 
/*     */         
/* 202 */         if (counter == 1.0F) {
/*     */ 
/*     */           
/* 205 */           String sstring = "sound.work.prospecting1";
/* 206 */           int x = Server.rand.nextInt(3);
/* 207 */           if (x == 0) {
/* 208 */             sstring = "sound.work.prospecting2";
/* 209 */           } else if (x == 1) {
/* 210 */             sstring = "sound.work.prospecting3";
/*     */           } 
/* 212 */           SoundPlayer.playSound(sstring, performer.getTileX(), performer.getTileY(), performer.isOnSurface(), 1.0F);
/* 213 */           int maxRadius = calcMaxRadius(prospecting.getKnowledge(0.0D));
/*     */           
/* 215 */           float time = calcTickTime(performer, prospecting);
/* 216 */           act.setNextTick(time);
/* 217 */           act.setTickCount(1);
/* 218 */           float totalTime = time * maxRadius;
/*     */           
/*     */           try {
/* 221 */             performer.getCurrentAction().setTimeLeft((int)totalTime);
/*     */           }
/* 223 */           catch (NoSuchActionException nsa) {
/*     */             
/* 225 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*     */           } 
/* 227 */           performer.getCommunicator().sendNormalServerMessage("You start to analyse the " + targetType + ".");
/* 228 */           Server.getInstance().broadCastAction(performer.getName() + " starts analysing the " + targetType + ".", performer, 5);
/*     */           
/* 230 */           performer.sendActionControl(Actions.actionEntrys[536].getVerbString(), true, (int)totalTime);
/* 231 */           performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/*     */         } 
/*     */ 
/*     */         
/* 235 */         if (counter * 10.0F >= act.getNextTick()) {
/*     */ 
/*     */           
/* 238 */           int radius = act.getTickCount();
/* 239 */           int currentSkill = (int)prospecting.getKnowledge(0.0D);
/* 240 */           int maxRadius = calcMaxRadius(currentSkill);
/* 241 */           int skillTypeOffset = currentSkill - radius * 17 - 17;
/* 242 */           int skillQLOffset = currentSkill - radius * 18 - -3;
/* 243 */           act.incTickCount();
/* 244 */           act.incNextTick(calcTickTime(performer, prospecting));
/*     */           
/* 246 */           performer.getStatus().modifyStamina((-1500 * radius));
/* 247 */           prospecting.skillCheck(target.getCurrentQualityLevel(), null, 0.0D, false, counter / radius);
/*     */ 
/*     */           
/* 250 */           if (radius >= maxRadius) {
/* 251 */             done = true;
/*     */           }
/*     */ 
/*     */           
/* 255 */           LinkedList<String> list = new LinkedList<>();
/*     */           
/* 257 */           for (int x = -radius; x <= radius; x++) {
/*     */             
/* 259 */             for (int y = -radius; y <= radius; y++) {
/*     */ 
/*     */               
/* 262 */               if (x == -radius || x == radius || y == -radius || y == radius) {
/*     */                 
/* 264 */                 String dir = "";
/* 265 */                 if (performer.getBestCompass() != null) {
/*     */                   
/* 267 */                   if (y < 0) {
/* 268 */                     dir = "north";
/* 269 */                   } else if (y > 0) {
/* 270 */                     dir = "south";
/* 271 */                   }  String we = "";
/* 272 */                   if (x < 0) {
/* 273 */                     we = "west";
/* 274 */                   } else if (x > 0) {
/* 275 */                     we = "east";
/*     */                   } 
/* 277 */                   if (dir.length() > 0) {
/*     */                     
/* 279 */                     if (we.length() > 0)
/*     */                     {
/* 281 */                       if (Math.abs(x) == Math.abs(y)) {
/* 282 */                         dir = dir + we;
/* 283 */                       } else if (Math.abs(x) < Math.abs(y)) {
/* 284 */                         dir = we + " of " + dir;
/*     */                       } else {
/* 286 */                         dir = dir + " of " + we;
/*     */                       } 
/*     */                     }
/*     */                   } else {
/* 290 */                     dir = we;
/*     */                   } 
/* 292 */                   dir = " (" + dir + ")";
/*     */                 } 
/*     */                 
/* 295 */                 int resource = Server.getCaveResource(tilex + x, tiley + y);
/* 296 */                 if (resource == 65535) {
/*     */                   
/* 298 */                   resource = Server.rand.nextInt(10000);
/* 299 */                   Server.setCaveResource(tilex + x, tiley + y, resource);
/*     */                 } 
/*     */                 
/* 302 */                 String foundString = checkTile(performer, tilex + x, tiley + y, radius, skillTypeOffset, skillQLOffset);
/*     */                 
/* 304 */                 add2List(list, foundString, dir);
/* 305 */                 if (prospecting.getKnowledge(0.0D) > 40.0D) {
/*     */ 
/*     */                   
/* 308 */                   byte type = Tiles.decodeType(Server.caveMesh.getTile(tilex + x, tiley + y));
/* 309 */                   if (type != Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id) {
/*     */                     
/* 311 */                     TileRockBehaviour.rockRandom.setSeed((tilex + x + (tiley + y) * Zones.worldTileSizeY) * 102533L);
/*     */                     
/* 313 */                     if (TileRockBehaviour.rockRandom.nextInt(100) == 0) {
/*     */                       
/* 315 */                       String foundSalt = checkSaltFlint(performer, tilex + x, tiley + y, radius, skillTypeOffset, true);
/*     */                       
/* 317 */                       add2List(list, foundSalt, dir);
/*     */                     } 
/*     */                   } 
/*     */                 } 
/* 321 */                 TileRockBehaviour.rockRandom.setSeed((tilex + x + (tiley + y) * Zones.worldTileSizeY) * 6883L);
/*     */                 
/* 323 */                 if (TileRockBehaviour.rockRandom.nextInt(200) == 0) {
/*     */                   
/* 325 */                   String foundFlint = checkSaltFlint(performer, tilex + x, tiley + y, radius, skillTypeOffset, false);
/*     */                   
/* 327 */                   add2List(list, foundFlint, dir);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/* 332 */           outputList(performer, list, radius);
/* 333 */           if (!done)
/* 334 */             furtherStudy(performer, radius + 1); 
/*     */         } 
/* 336 */         if (done)
/* 337 */           performer.getCommunicator().sendNormalServerMessage("You finish analysing the " + targetType + "."); 
/*     */       } 
/* 339 */       return done;
/*     */     } 
/*     */ 
/*     */     
/* 343 */     if (source == null) {
/* 344 */       return super.action(act, performer, target, action, counter);
/*     */     }
/* 346 */     return super.action(act, performer, source, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void add2List(LinkedList<String> list, String foundString, String dir) {
/* 352 */     if (foundString.length() > 0 && !list.contains(foundString + dir))
/*     */     {
/* 354 */       if (Server.rand.nextBoolean()) {
/* 355 */         list.addFirst(foundString + dir);
/*     */       } else {
/* 357 */         list.addLast(foundString + dir);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static String checkTile(Creature performer, int tilex, int tiley, int radius, int skillTypeOffset, int skillQLOffset) {
/* 363 */     String findString = "";
/* 364 */     int itemTemplate = TileRockBehaviour.getItemTemplateForTile(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)));
/* 365 */     if (itemTemplate != 146) {
/*     */       
/*     */       try {
/*     */         
/* 369 */         int itemSkillOffset = itemToSkillOffset(itemTemplate);
/* 370 */         ItemTemplate t = ItemTemplateFactory.getInstance().getTemplate(itemTemplate);
/*     */         
/* 372 */         findString = "something, but cannot quite make it out";
/* 373 */         if (skillTypeOffset > itemSkillOffset) {
/*     */           
/* 375 */           findString = t.getProspectName();
/* 376 */           TileBehaviour.r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/* 377 */           int m = 100;
/* 378 */           int max = Math.min(100, 20 + TileBehaviour.r.nextInt(80));
/* 379 */           findString = TileBehaviour.getShardQlDescription(max) + " " + findString;
/*     */         } 
/*     */ 
/*     */         
/* 383 */         if (radius == 6)
/*     */         {
/* 385 */           return "an " + radToString(radius) + "trace of " + findString;
/*     */         }
/*     */ 
/*     */         
/* 389 */         return "a " + radToString(radius) + "trace of " + findString;
/*     */       
/*     */       }
/* 392 */       catch (NoSuchTemplateException nst) {
/*     */         
/* 394 */         logger.log(Level.WARNING, performer.getName() + " - " + nst.getMessage() + ": " + itemTemplate + " at " + tilex + ", " + tiley, (Throwable)nst);
/*     */       } 
/*     */     }
/*     */     
/* 398 */     return findString;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String checkSaltFlint(Creature performer, int tilex, int tiley, int radius, int skillTypeOffset, boolean salt) {
/* 403 */     String findString = "";
/* 404 */     int itemTemplate = salt ? 349 : 446;
/*     */     
/*     */     try {
/* 407 */       int itemSkillOffset = salt ? 5 : 2;
/* 408 */       ItemTemplate t = ItemTemplateFactory.getInstance().getTemplate(itemTemplate);
/*     */       
/* 410 */       findString = "something, but cannot quite make it out";
/* 411 */       if (skillTypeOffset > itemSkillOffset)
/*     */       {
/* 413 */         findString = t.getName();
/*     */       }
/*     */       
/* 416 */       if (radius == 6)
/*     */       {
/* 418 */         return "an " + radToString(radius) + "trace of " + findString;
/*     */       }
/*     */ 
/*     */       
/* 422 */       return "a " + radToString(radius) + "trace of " + findString;
/*     */     
/*     */     }
/* 425 */     catch (NoSuchTemplateException nst) {
/*     */       
/* 427 */       logger.log(Level.WARNING, performer.getName() + " - " + nst.getMessage() + ": " + itemTemplate + " at " + tilex + ", " + tiley, (Throwable)nst);
/*     */ 
/*     */       
/* 430 */       return findString;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String radToString(int radius) {
/* 435 */     switch (radius) {
/*     */       
/*     */       case 2:
/* 438 */         return "slight ";
/*     */       case 3:
/* 440 */         return "faint ";
/*     */       case 4:
/* 442 */         return "minuscule ";
/*     */       case 5:
/* 444 */         return "vague ";
/*     */       case 6:
/* 446 */         return "indistinct ";
/*     */     } 
/* 448 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void furtherStudy(Creature performer, int radius) {
/* 454 */     switch (radius) {
/*     */       
/*     */       case 2:
/* 457 */         performer.getCommunicator().sendNormalServerMessage("You take a closer look.");
/*     */         return;
/*     */       case 3:
/* 460 */         performer.getCommunicator().sendNormalServerMessage("You study it a bit more.");
/*     */         return;
/*     */       case 4:
/* 463 */         performer.getCommunicator().sendNormalServerMessage("You study it real hard.");
/*     */         return;
/*     */       case 5:
/* 466 */         performer.getCommunicator().sendNormalServerMessage("You peer at it.");
/*     */         return;
/*     */     } 
/* 469 */     performer.getCommunicator().sendNormalServerMessage("You go cross-eyed studying it.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int calcMaxRadius(double currentSkill) {
/* 477 */     return (int)(currentSkill + 17.0D) / 17;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float calcTickTime(Creature performer, Skill prospecting) {
/* 482 */     return (Actions.getQuickActionTime(performer, prospecting, null, 0.0D) / 3 * 2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void outputList(Creature performer, LinkedList<String> list, int radius) {
/* 487 */     if (list.isEmpty()) {
/*     */       
/* 489 */       int x = Server.rand.nextInt(3);
/* 490 */       if (x == 0) {
/* 491 */         performer.getCommunicator().sendNormalServerMessage("You do not notice any unusual " + radToString(radius) + "traces.");
/* 492 */       } else if (x == 1) {
/* 493 */         performer.getCommunicator().sendNormalServerMessage("You cannot see anything unusual.");
/*     */       } else {
/* 495 */         performer.getCommunicator().sendNormalServerMessage("You cannot see any unusual " + radToString(radius) + "traces of anything.");
/*     */       } 
/*     */     } else {
/*     */       
/* 499 */       for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
/*     */         
/* 501 */         int x = Server.rand.nextInt(3);
/* 502 */         if (x == 0) {
/* 503 */           performer.getCommunicator().sendNormalServerMessage("You spot " + (String)it.next() + "."); continue;
/* 504 */         }  if (x == 1) {
/* 505 */           performer.getCommunicator().sendNormalServerMessage("You notice " + (String)it.next() + "."); continue;
/*     */         } 
/* 507 */         performer.getCommunicator().sendNormalServerMessage("You see " + (String)it.next() + ".");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int itemToSkillOffset(int itemTemplate) {
/* 514 */     switch (itemTemplate) {
/*     */       
/*     */       case 38:
/* 517 */         return 0;
/*     */       case 207:
/* 519 */         return 1;
/*     */       case 42:
/* 521 */         return 2;
/*     */       case 41:
/* 523 */         return 3;
/*     */       case 43:
/* 525 */         return 4;
/*     */       case 770:
/* 527 */         return 6;
/*     */       case 40:
/* 529 */         return 7;
/*     */       case 785:
/* 531 */         return 8;
/*     */       case 39:
/* 533 */         return 9;
/*     */       case 697:
/* 535 */         return 10;
/*     */       case 693:
/* 537 */         return 11;
/*     */       case 1116:
/* 539 */         return 12;
/*     */     } 
/* 541 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ShardBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
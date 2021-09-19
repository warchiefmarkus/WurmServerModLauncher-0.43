/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Point;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public final class Locates
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(Locates.class.getName());
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
/*     */   static void locateSpring(Creature performer, Item pendulum, Skill primSkill) {
/*  60 */     int[] closest = Zones.getClosestSpring(performer.getTileX(), performer.getTileY(), (int)(10.0F * getMaterialPendulumModifier(pendulum.getMaterial())));
/*  61 */     int max = Math.max(closest[0], closest[1]);
/*  62 */     double knowl = primSkill.getKnowledge(pendulum.getCurrentQualityLevel());
/*     */     
/*  64 */     float difficulty = Server.rand.nextFloat() * (max + 3) * 30.0F;
/*     */ 
/*     */     
/*  67 */     double result = Server.rand.nextFloat() * knowl * 10.0D;
/*  68 */     result -= difficulty;
/*     */     
/*  70 */     Server.getInstance().broadCastAction(performer
/*  71 */         .getName() + " lets out a mild sigh as " + performer.getHeSheItString() + " starts breathing again.", performer, 5);
/*     */     
/*  73 */     if (closest[0] == -1) {
/*     */       
/*  75 */       performer.getCommunicator().sendNormalServerMessage("The " + pendulum.getName() + " seems dead.");
/*     */ 
/*     */     
/*     */     }
/*  79 */     else if (result > 0.0D) {
/*     */       
/*  81 */       if (max < 1) {
/*  82 */         performer.getCommunicator().sendNormalServerMessage("The " + pendulum
/*  83 */             .getName() + " now swings frantically! There is something here!");
/*  84 */       } else if (max < 2) {
/*  85 */         performer.getCommunicator().sendNormalServerMessage("The " + pendulum
/*  86 */             .getName() + " swings rapidly back and forth! You are close to a water source!");
/*  87 */       } else if (max < 3) {
/*  88 */         performer.getCommunicator().sendNormalServerMessage("The " + pendulum
/*  89 */             .getName() + " is swinging in a circle, there is probably a water source in the ground nearby.");
/*     */       }
/*  91 */       else if (max < 5) {
/*  92 */         performer.getCommunicator().sendNormalServerMessage("The " + pendulum
/*  93 */             .getName() + " is starting to move, indicating a flow of energy somewhere near.");
/*  94 */       } else if (result > 30.0D) {
/*  95 */         performer.getCommunicator().sendNormalServerMessage("You think you detect some faint tugs in the " + pendulum
/*  96 */             .getName() + ".");
/*     */       } else {
/*  98 */         performer.getCommunicator().sendNormalServerMessage("The " + pendulum.getName() + " seems dead.");
/*     */       } 
/*     */     } else {
/* 101 */       performer.getCommunicator().sendNormalServerMessage("The " + pendulum.getName() + " seems dead.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void useLocateItem(Creature performer, Item pendulum, Skill primSkill) {
/* 108 */     if (pendulum.getSpellLocChampBonus() > 0.0F) {
/*     */ 
/*     */       
/* 111 */       locateChamp(performer, pendulum, primSkill);
/*     */     }
/* 113 */     else if (pendulum.getSpellLocEnemyBonus() > 0.0F) {
/*     */ 
/*     */       
/* 116 */       locateEnemy(performer, pendulum, primSkill);
/*     */     }
/* 118 */     else if (pendulum.getSpellLocFishBonus() > 0.0F) {
/*     */       
/* 120 */       if (Servers.isThisATestServer() && performer.isOnSurface()) {
/*     */ 
/*     */         
/* 123 */         performer.getCommunicator().sendAlertServerMessage("New fishing...");
/* 124 */         locateFish(performer, pendulum, primSkill, true);
/* 125 */         performer.getCommunicator().sendAlertServerMessage("Old fishing...");
/* 126 */         locateFish(performer, pendulum, primSkill, false);
/*     */       }
/*     */       else {
/*     */         
/* 130 */         locateFish(performer, pendulum, primSkill, true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void locateChamp(Creature performer, Item pendulum, Skill primSkill) {
/* 137 */     int x = performer.getTileX();
/* 138 */     int y = performer.getTileY();
/* 139 */     int dist = (int)(pendulum.getSpellLocChampBonus() / 100.0F * Zones.worldTileSizeX / 32.0F * getMaterialPendulumModifier(pendulum.getMaterial()));
/* 140 */     Creature firstChamp = findFirstCreature(x, y, dist, performer.isOnSurface(), true, performer);
/* 141 */     if (firstChamp != null) {
/*     */       
/* 143 */       int dx = Math.abs(x - firstChamp.getTileX());
/* 144 */       int dy = Math.abs(y - firstChamp.getTileY());
/* 145 */       int maxd = (int)Math.sqrt((dx * dx + dy * dy));
/* 146 */       if (primSkill.skillCheck((maxd / 10.0F), pendulum, 0.0D, false, 5.0F) > 0.0D)
/*     */       {
/* 148 */         int dir = MethodsCreatures.getDir(performer, firstChamp.getTileX(), firstChamp.getTileY());
/* 149 */         String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/* 150 */         String toReturn = EndGameItems.getDistanceString(maxd, firstChamp.getName(), direction, false);
/* 151 */         performer.getCommunicator().sendNormalServerMessage(toReturn);
/*     */       }
/*     */       else
/*     */       {
/* 155 */         performer.getCommunicator()
/* 156 */           .sendNormalServerMessage("You fail to make sense of the " + pendulum.getName() + ".");
/*     */       }
/*     */     
/* 159 */     } else if (primSkill.skillCheck(10.0D, pendulum, 0.0D, false, 5.0F) > 0.0D) {
/* 160 */       performer.getCommunicator().sendNormalServerMessage("The " + pendulum.getName() + " doesn't seem to move.");
/*     */     } else {
/* 162 */       performer.getCommunicator().sendNormalServerMessage("You fail to make sense of the " + pendulum.getName() + ".");
/*     */     } 
/*     */   }
/*     */   
/*     */   static void locateEnemy(Creature performer, Item pendulum, Skill primSkill) {
/* 167 */     int x = performer.getTileX();
/* 168 */     int y = performer.getTileY();
/* 169 */     int dist = (int)(pendulum.getSpellLocEnemyBonus() * getMaterialPendulumModifier(pendulum.getMaterial()));
/*     */     
/* 171 */     Creature firstEnemy = findFirstCreature(x, y, dist, performer.isOnSurface(), false, performer);
/* 172 */     if (firstEnemy != null) {
/*     */       
/* 174 */       int dx = Math.abs(x - firstEnemy.getTileX());
/* 175 */       int dy = Math.abs(y - firstEnemy.getTileY());
/* 176 */       int maxd = (int)Math.sqrt((dx * dx + dy * dy));
/*     */       
/* 178 */       if (primSkill.skillCheck((maxd / 10.0F), pendulum, 0.0D, false, 5.0F) > 0.0D)
/*     */       {
/* 180 */         int dir = MethodsCreatures.getDir(performer, firstEnemy.getTileX(), firstEnemy.getTileY());
/* 181 */         String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/* 182 */         String toReturn = EndGameItems.getDistanceString(maxd, "enemy", direction, false);
/* 183 */         performer.getCommunicator().sendNormalServerMessage(toReturn);
/*     */         
/* 185 */         locateTraitor(performer, pendulum, primSkill);
/*     */       }
/*     */       else
/*     */       {
/* 189 */         performer.getCommunicator()
/* 190 */           .sendNormalServerMessage("You fail to make sense of the " + pendulum.getName() + ".");
/*     */       }
/*     */     
/* 193 */     } else if (primSkill.skillCheck(10.0D, pendulum, 0.0D, false, 5.0F) > 0.0D) {
/*     */       
/* 195 */       if (!locateTraitor(performer, pendulum, primSkill)) {
/* 196 */         performer.getCommunicator().sendNormalServerMessage("The " + pendulum.getName() + " doesn't seem to move.");
/*     */       }
/*     */     } else {
/* 199 */       performer.getCommunicator().sendNormalServerMessage("You fail to make sense of the " + pendulum.getName() + ".");
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean locateTraitor(Creature performer, Item pendulum, Skill primSkill) {
/* 204 */     Creature[] possibleTraitors = EpicServerStatus.getCurrentTraitors();
/* 205 */     if (possibleTraitors != null) {
/*     */       
/* 207 */       int maxDist = (int)(pendulum.getSpellLocEnemyBonus() / 100.0F * Zones.worldTileSizeX / 16.0F * getMaterialPendulumModifier(pendulum.getMaterial()));
/* 208 */       for (Creature c : possibleTraitors) {
/*     */         
/* 210 */         if (performer.isWithinDistanceTo(c, maxDist)) {
/*     */           
/* 212 */           int dx = Math.abs(performer.getTileX() - c.getTileX());
/* 213 */           int dy = Math.abs(performer.getTileY() - c.getTileY());
/* 214 */           int maxd = (int)Math.sqrt((dx * dx + dy * dy));
/* 215 */           int dir = MethodsCreatures.getDir(performer, c.getTileX(), c.getTileY());
/* 216 */           String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/* 217 */           String toReturn = EndGameItems.getDistanceString(maxd, c.getName(), direction, false);
/*     */           
/* 219 */           performer.getCommunicator().sendNormalServerMessage(toReturn);
/*     */           
/* 221 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     return false;
/*     */   }
/*     */   
/*     */   static void locateFish(Creature performer, Item pendulum, Skill primSkill, boolean newFishing) {
/*     */     Point[] points;
/* 231 */     if (!performer.isOnSurface()) {
/*     */       
/* 233 */       performer.getCommunicator().sendNormalServerMessage("The " + pendulum.getName() + " does not move.");
/*     */       return;
/*     */     } 
/* 236 */     int maxDist = (int)(pendulum.getSpellLocFishBonus() / 10.0F * getMaterialPendulumModifier(pendulum.getMaterial()));
/*     */     
/* 238 */     if (newFishing) {
/*     */       
/* 240 */       int season = WurmCalendar.getSeasonNumber();
/* 241 */       points = MethodsFishing.getSpecialSpots(performer.getTileX(), performer.getTileY(), season);
/*     */     }
/*     */     else {
/*     */       
/* 245 */       points = Fish.getRareSpots(performer.getTileX(), performer.getTileY());
/*     */     } 
/* 247 */     boolean found = false;
/* 248 */     for (Point point : points) {
/*     */ 
/*     */       
/* 251 */       if (performer.isWithinTileDistanceTo(point.getX(), point.getY(), 0, maxDist + 5)) {
/*     */         
/* 253 */         sendFishFound(point.getX(), point.getY(), point.getH(), performer, primSkill, pendulum);
/* 254 */         found = true;
/*     */       } 
/*     */     } 
/* 257 */     if (!found) {
/* 258 */       performer.getCommunicator().sendNormalServerMessage("You fail to make sense of the " + pendulum.getName() + ".");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void sendFishFound(int targx, int targy, int fish, Creature performer, Skill primSkill, Item pendulum) {
/* 264 */     int x = performer.getTileX();
/* 265 */     int y = performer.getTileY();
/* 266 */     if (fish > 0) {
/*     */       
/* 268 */       int dx = Math.max(0, Math.abs(x - targx) - 5);
/* 269 */       int dy = Math.max(0, Math.abs(y - targy) - 5);
/* 270 */       int maxd = (int)Math.sqrt((dx * dx + dy * dy));
/* 271 */       double skillCheck = primSkill.skillCheck(maxd, pendulum, 0.0D, false, 5.0F);
/* 272 */       if (skillCheck > 0.0D)
/*     */       {
/* 274 */         int dir = MethodsCreatures.getDir(performer, targx, targy);
/* 275 */         String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/* 276 */         String spot = "fishing spot";
/* 277 */         if (skillCheck > 75.0D) {
/*     */ 
/*     */           
/* 280 */           String name = ItemTemplateFactory.getInstance().getTemplateName(fish);
/* 281 */           if (name.length() > 0)
/*     */           {
/* 283 */             spot = name + " fishing spot";
/*     */           }
/*     */         } 
/* 286 */         String loc = "";
/* 287 */         if (performer.getPower() >= 2)
/* 288 */           loc = " (" + targx + "," + targy + ")"; 
/* 289 */         String toReturn = EndGameItems.getDistanceString(maxd, spot + loc, direction, false);
/* 290 */         performer.getCommunicator().sendNormalServerMessage(toReturn);
/*     */       }
/*     */       else
/*     */       {
/* 294 */         performer.getCommunicator()
/* 295 */           .sendNormalServerMessage("You feel there is something there but cannot determine what.");
/*     */       }
/*     */     
/* 298 */     } else if (primSkill.skillCheck(10.0D, pendulum, 0.0D, false, 5.0F) > 0.0D) {
/* 299 */       performer.getCommunicator().sendNormalServerMessage("The " + pendulum.getName() + " doesn't seem to move.");
/*     */     } else {
/* 301 */       performer.getCommunicator().sendNormalServerMessage("You fail to make sense of the " + pendulum.getName() + ".");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final Creature findFirstCreature(int x, int y, int maxdist, boolean surfaced, boolean champ, Creature performer) {
/* 307 */     for (int tdist = 0; tdist <= maxdist; tdist++) {
/*     */       
/* 309 */       if (tdist == 0) {
/*     */         
/* 311 */         Creature c = getCreatureOnTile(x, y, tdist, surfaced, champ, performer);
/* 312 */         if (c != null) {
/* 313 */           return c;
/*     */         }
/*     */       } else {
/*     */         
/* 317 */         Creature c = findCreatureOnRow(x, y, tdist, surfaced, champ, performer);
/* 318 */         if (c != null) {
/* 319 */           return c;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 326 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Creature findCreatureOnRow(int x, int y, int dist, boolean surfaced, boolean champ, Creature performer) {
/* 333 */     for (int n = x; n < x + dist; n++) {
/*     */       
/* 335 */       if (n < Zones.worldTileSizeX && n > 0) {
/*     */         
/* 337 */         Creature toReturn = getCreatureOnTile(n, y - dist, dist, surfaced, champ, performer);
/* 338 */         if (toReturn != null)
/* 339 */           return toReturn; 
/*     */       } 
/*     */     } 
/*     */     int m;
/* 343 */     for (m = y - dist; m < y; m++) {
/*     */       
/* 345 */       if (m < Zones.worldTileSizeY && m > 0) {
/*     */         
/* 347 */         Creature toreturn = getCreatureOnTile(x + dist, dist, m, surfaced, champ, performer);
/* 348 */         if (toreturn != null) {
/* 349 */           return toreturn;
/*     */         }
/*     */       } 
/*     */     } 
/* 353 */     for (m = y; m <= y + dist; m++) {
/*     */       
/* 355 */       if (m < Zones.worldTileSizeY && m > 0) {
/*     */         
/* 357 */         Creature toreturn = getCreatureOnTile(x + dist, dist, m, surfaced, champ, performer);
/* 358 */         if (toreturn != null) {
/* 359 */           return toreturn;
/*     */         }
/*     */       } 
/*     */     } 
/* 363 */     for (int k = x; k < x + dist; k++) {
/*     */       
/* 365 */       if (k < Zones.worldTileSizeX && k > 0) {
/*     */         
/* 367 */         Creature toReturn = getCreatureOnTile(k, y + dist, dist, surfaced, champ, performer);
/* 368 */         if (toReturn != null) {
/* 369 */           return toReturn;
/*     */         }
/*     */       } 
/*     */     } 
/* 373 */     for (int j = y - dist; j < y; j++) {
/*     */       
/* 375 */       if (j < Zones.worldTileSizeY && j > 0) {
/*     */         
/* 377 */         Creature toreturn = getCreatureOnTile(x - dist, dist, j, surfaced, champ, performer);
/* 378 */         if (toreturn != null) {
/* 379 */           return toreturn;
/*     */         }
/*     */       } 
/*     */     } 
/* 383 */     for (int i = x - dist; i < x; i++) {
/*     */       
/* 385 */       if (i < Zones.worldTileSizeX && i > 0) {
/*     */         
/* 387 */         Creature toReturn = getCreatureOnTile(i, y + dist, dist, surfaced, champ, performer);
/* 388 */         if (toReturn != null) {
/* 389 */           return toReturn;
/*     */         }
/*     */       } 
/*     */     } 
/* 393 */     for (int ty = y; ty < y + dist; ty++) {
/*     */       
/* 395 */       if (ty < Zones.worldTileSizeY && ty > 0) {
/*     */         
/* 397 */         Creature toreturn = getCreatureOnTile(x - dist, ty, dist, surfaced, champ, performer);
/* 398 */         if (toreturn != null) {
/* 399 */           return toreturn;
/*     */         }
/*     */       } 
/*     */     } 
/* 403 */     for (int tx = x - dist; tx < x; tx++) {
/*     */       
/* 405 */       if (tx < Zones.worldTileSizeX && tx > 0) {
/*     */         
/* 407 */         Creature toReturn = getCreatureOnTile(tx, y - dist, dist, surfaced, champ, performer);
/* 408 */         if (toReturn != null)
/* 409 */           return toReturn; 
/*     */       } 
/*     */     } 
/* 412 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final Creature getCreatureOnTile(int x, int y, int dist, boolean surfaced, boolean champ, Creature performer) {
/* 418 */     VolaTile t = Zones.getTileOrNull(x, y, surfaced);
/* 419 */     if (t != null) {
/*     */       
/* 421 */       Creature[] crets = t.getCreatures();
/* 422 */       for (Creature c : crets) {
/*     */         
/* 424 */         if (champ && (c.getStatus().isChampion() || c.isUnique())) {
/* 425 */           return c;
/*     */         }
/* 427 */         if (!champ && c.isPlayer()) {
/*     */ 
/*     */ 
/*     */           
/* 431 */           boolean found = (c.getAttitude(performer) == 2);
/* 432 */           if (!found)
/* 433 */             found = (performer.getCitizenVillage() != null && performer.getCitizenVillage().isEnemy(c)); 
/* 434 */           if (found) {
/*     */ 
/*     */             
/* 437 */             if (c.isStealth() && dist > 25) {
/*     */               continue;
/*     */             }
/*     */             
/* 441 */             float nolocateEnchantPower = c.getNoLocateItemBonus(false);
/* 442 */             if (nolocateEnchantPower > 0.0F) {
/*     */               
/* 444 */               int maxDistance = 100;
/* 445 */               int distReduction = (int)(nolocateEnchantPower / 2.0F);
/* 446 */               if (dist > maxDistance - distReduction) {
/*     */                 continue;
/*     */               }
/*     */             } 
/*     */             
/* 451 */             if (c.getBonusForSpellEffect((byte)29) <= 0.0F)
/* 452 */               return c; 
/*     */           } 
/*     */         }  continue;
/*     */       } 
/*     */     } 
/* 457 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float getMaterialPendulumModifier(byte material) {
/* 462 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*     */     {
/* 464 */       switch (material) {
/*     */         
/*     */         case 56:
/* 467 */           return 1.15F;
/*     */         case 30:
/* 469 */           return 1.025F;
/*     */         case 31:
/* 471 */           return 1.05F;
/*     */         case 10:
/* 473 */           return 0.95F;
/*     */         case 57:
/* 475 */           return 1.2F;
/*     */         case 7:
/* 477 */           return 1.1F;
/*     */         case 12:
/* 479 */           return 0.9F;
/*     */         case 67:
/* 481 */           return 1.25F;
/*     */         case 8:
/* 483 */           return 1.05F;
/*     */         case 9:
/* 485 */           return 1.025F;
/*     */         case 34:
/* 487 */           return 0.95F;
/*     */         case 13:
/* 489 */           return 0.95F;
/*     */         case 96:
/* 491 */           return 1.075F;
/*     */       } 
/*     */     }
/* 494 */     return 1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Locates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
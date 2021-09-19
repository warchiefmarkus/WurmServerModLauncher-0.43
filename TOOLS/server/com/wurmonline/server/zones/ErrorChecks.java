/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.kingdom.GuardTower;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Cults;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.io.IOException;
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
/*     */ public final class ErrorChecks
/*     */   implements MiscConstants, CounterTypes
/*     */ {
/*  46 */   private static final Logger logger = Logger.getLogger(ErrorChecks.class.getName());
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
/*     */   public static void checkCreatures(Creature performer, String searchString) {
/*  64 */     long lStart = System.nanoTime();
/*  65 */     int nums = 0;
/*  66 */     Creature[] crets = Creatures.getInstance().getCreatures();
/*  67 */     boolean empty = (searchString == null || searchString.length() == 0);
/*  68 */     performer.getCommunicator().sendSafeServerMessage("Starting creature check...");
/*  69 */     for (int x = 0; x < crets.length; x++) {
/*     */       
/*  71 */       if (empty || crets[x].getName().contains(searchString)) {
/*     */         
/*  73 */         VolaTile t = crets[x].getCurrentTile();
/*  74 */         if (t != null) {
/*     */ 
/*     */           
/*     */           try {
/*  78 */             Zone z = Zones.getZone(crets[x].getTileX(), crets[x].getTileY(), crets[x].isOnSurface());
/*  79 */             VolaTile rt = z.getTileOrNull(crets[x].getTileX(), crets[x].getTileY());
/*  80 */             if (rt != null) {
/*     */               
/*  82 */               if (rt.getTileX() != t.getTileX() || rt.getTileY() != t.getTileY()) {
/*     */                 
/*  84 */                 performer.getCommunicator().sendNormalServerMessage(crets[x]
/*  85 */                     .getName() + " [" + crets[x].getWurmId() + "] at " + crets[x].getTileX() + "," + crets[x]
/*  86 */                     .getTileY() + " currenttile at " + t.getTileX() + " " + t.getTileY());
/*  87 */                 nums++;
/*     */               } 
/*     */               
/*  90 */               boolean found = false;
/*  91 */               Creature[] cc = rt.getCreatures(); int xx;
/*  92 */               for (xx = 0; xx < cc.length; xx++) {
/*     */                 
/*  94 */                 if (cc[xx].getWurmId() == crets[x].getWurmId())
/*  95 */                   found = true; 
/*     */               } 
/*  97 */               if (!found) {
/*     */                 
/*  99 */                 if (!crets[x].isDead()) {
/*     */                   
/* 101 */                   performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 102 */                       .getName() + " [" + crets[x].getWurmId() + "] not in list on tile " + rt
/* 103 */                       .getTileX() + " " + rt
/* 104 */                       .getTileY() + " #" + rt.hashCode() + " xy=" + crets[x].getTileX() + ", " + crets[x]
/*     */                       
/* 106 */                       .getTileY() + " surf=" + crets[x].isOnSurface() + " inactive=" + rt
/* 107 */                       .isInactive());
/* 108 */                   nums++;
/*     */                 } 
/* 110 */                 found = false;
/* 111 */                 cc = t.getCreatures();
/* 112 */                 for (xx = 0; xx < cc.length; xx++) {
/*     */                   
/* 114 */                   if (cc[xx].getWurmId() == crets[x].getWurmId())
/* 115 */                     found = true; 
/*     */                 } 
/* 117 */                 if (!found) {
/*     */                   
/* 119 */                   if (!crets[x].isDead())
/*     */                   {
/* 121 */                     performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 122 */                         .getName() + " [" + crets[x].getWurmId() + "] not in list on CURRENT tile " + t
/* 123 */                         .getTileX() + " " + t.getTileY() + " #" + t.hashCode() + " xy=" + crets[x]
/* 124 */                         .getTileX() + ", " + crets[x].getTileY() + " surf=" + crets[x]
/* 125 */                         .isOnSurface() + " inactive=" + t.isInactive());
/*     */                   }
/* 127 */                   if (crets[x].isDead()) {
/*     */                     
/* 129 */                     boolean delete = true;
/* 130 */                     if (crets[x].isKingdomGuard()) {
/*     */                       
/* 132 */                       GuardTower tower = Kingdoms.getTower(crets[x]);
/* 133 */                       if (tower != null) {
/*     */                         
/*     */                         try {
/*     */                           
/* 137 */                           delete = false;
/* 138 */                           tower.returnGuard(crets[x]);
/* 139 */                           performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 140 */                               .getName() + " [" + crets[x].getWurmId() + "] returned to tower.");
/*     */                         
/*     */                         }
/* 143 */                         catch (IOException iOException) {}
/*     */                       }
/*     */                     } 
/*     */ 
/*     */ 
/*     */                     
/* 149 */                     if (delete) {
/*     */                       
/* 151 */                       if (DbCreatureStatus.getIsLoaded(crets[x].getWurmId()) == 0)
/* 152 */                         crets[x].destroy(); 
/* 153 */                       performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 154 */                           .getName() + " [" + crets[x].getWurmId() + "] destroyed.");
/*     */                     } 
/*     */                   } 
/*     */                 } else {
/*     */                   
/* 159 */                   performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 160 */                       .getName() + " [" + crets[x].getWurmId() + "] IS in list on CURRENT tile " + t
/* 161 */                       .getTileX() + " " + t.getTileY() + " #" + t.hashCode() + " xy=" + crets[x]
/* 162 */                       .getTileX() + ", " + crets[x]
/* 163 */                       .getTileY() + " surf=" + crets[x].isOnSurface() + " inactive=" + t
/* 164 */                       .isInactive());
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 168 */               performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 169 */                   .getName() + " [" + crets[x].getWurmId() + "] null tile but current at " + t.getTileX() + ", " + t
/* 170 */                   .getTileY());
/*     */             } 
/* 172 */           } catch (NoSuchZoneException nsz) {
/*     */             
/* 174 */             performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 175 */                 .getName() + " [" + crets[x].getWurmId() + "] no zone at " + t.getTileX() + ", " + t
/* 176 */                 .getTileY());
/*     */           } 
/*     */         } else {
/*     */           
/* 180 */           performer.getCommunicator().sendNormalServerMessage(crets[x]
/* 181 */               .getName() + " [" + crets[x].getWurmId() + "] null current tile.");
/*     */         } 
/*     */       } 
/* 184 */     }  performer.getCommunicator().sendSafeServerMessage("...done. " + nums + " errors.");
/*     */     
/* 186 */     logger.info("#checkCreatures took " + ((float)(System.nanoTime() - lStart) / 1000000.0F) + "ms.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkItems(Creature performer, String searchString) {
/* 197 */     long lStart = System.nanoTime();
/* 198 */     logger.info(performer + " is checking Items using search string: " + searchString);
/*     */     
/* 200 */     Item[] items = Items.getAllItems();
/* 201 */     boolean empty = (searchString == null || searchString.length() == 0);
/* 202 */     performer.getCommunicator().sendSafeServerMessage("Starting items check...");
/* 203 */     int nums = 0;
/* 204 */     for (int x = 0; x < items.length; x++) {
/*     */       
/* 206 */       if (empty || items[x].getName().contains(searchString))
/*     */       {
/* 208 */         if (items[x].getZoneId() >= 0)
/*     */         {
/* 210 */           if (items[x].getTemplateId() != 177) {
/*     */             
/*     */             try {
/*     */               
/* 214 */               Zone z = Zones.getZone(items[x].getTileX(), items[x].getTileY(), items[x].isOnSurface());
/* 215 */               VolaTile rt = z.getTileOrNull(items[x].getTileX(), items[x].getTileY());
/*     */               
/* 217 */               if (rt != null) {
/*     */                 
/* 219 */                 if (rt.getTileX() != items[x].getTileX() || rt.getTileY() != items[x].getTileY()) {
/*     */ 
/*     */ 
/*     */                   
/* 223 */                   performer.getCommunicator().sendNormalServerMessage(items[x]
/* 224 */                       .getName() + " [" + items[x].getWurmId() + "] at " + items[x].getTileX() + "," + items[x]
/* 225 */                       .getTileY() + " currenttile at " + rt.getTileX() + " " + rt
/* 226 */                       .getTileY());
/* 227 */                   nums++;
/*     */                 } 
/* 229 */                 Item[] cc = rt.getItems();
/* 230 */                 boolean found = false;
/* 231 */                 for (int xx = 0; xx < cc.length; xx++) {
/*     */                   
/* 233 */                   if (cc[xx].getWurmId() == items[x].getWurmId())
/* 234 */                     found = true; 
/*     */                 } 
/* 236 */                 if (!found)
/*     */                 {
/* 238 */                   performer.getCommunicator().sendNormalServerMessage(items[x]
/* 239 */                       .getName() + " [" + items[x].getWurmId() + "] not in list on tile " + rt
/* 240 */                       .getTileX() + " " + rt
/* 241 */                       .getTileY() + " inactive=" + rt.isInactive());
/* 242 */                   nums++;
/*     */                 }
/*     */               
/*     */               } else {
/*     */                 
/* 247 */                 performer.getCommunicator().sendNormalServerMessage(items[x]
/* 248 */                     .getName() + " [" + items[x].getWurmId() + "] last:" + items[x].getLastParentId() + " pile=" + (
/* 249 */                     (WurmId.getType((items[x]).lastParentId) == 6) ? 1 : 0) + ", null tile but current at " + items[x]
/* 250 */                     .getTileX() + ", " + items[x].getTileY());
/* 251 */                 nums++;
/*     */               }
/*     */             
/* 254 */             } catch (NoSuchZoneException nsz) {
/*     */               
/* 256 */               performer.getCommunicator().sendNormalServerMessage(items[x]
/* 257 */                   .getName() + " [" + items[x].getWurmId() + "] no zone at " + items[x].getTileX() + ", " + items[x]
/* 258 */                   .getTileY());
/*     */             } 
/*     */           }
/*     */         }
/*     */       }
/*     */     } 
/* 264 */     performer.getCommunicator().sendSafeServerMessage("...done. " + nums + " errors.");
/*     */     
/* 266 */     logger.info("#checkItems took " + ((float)(System.nanoTime() - lStart) / 1000000.0F) + "ms.");
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
/*     */   public static void getInfo(Creature performer, int tilex, int tiley, int layer) {
/*     */     try {
/* 281 */       Zone z = Zones.getZone(tilex, tiley, (layer >= 0));
/* 282 */       VolaTile rt = z.getOrCreateTile(tilex, tiley);
/*     */       
/* 284 */       Creature[] cc = rt.getCreatures();
/* 285 */       VirtualZone[] watchers = rt.getWatchers();
/* 286 */       for (int xx = 0; xx < cc.length; xx++) {
/*     */         
/* 288 */         performer.getCommunicator().sendNormalServerMessage(tilex + ", " + tiley + " contains " + cc[xx].getName());
/*     */         
/*     */         try {
/* 291 */           Server.getInstance().getCreature(cc[xx].getWurmId());
/*     */         }
/* 293 */         catch (NoSuchCreatureException nsc) {
/*     */           
/* 295 */           performer.getCommunicator().sendNormalServerMessage("The Creatures list does NOT contain " + cc[xx]
/* 296 */               .getWurmId());
/*     */         }
/* 298 */         catch (NoSuchPlayerException nsp) {
/*     */           
/* 300 */           performer.getCommunicator().sendNormalServerMessage("The Players list does NOT contain " + cc[xx]
/* 301 */               .getWurmId());
/*     */         } 
/* 303 */         for (int v = 0; v < watchers.length; v++) {
/*     */ 
/*     */           
/*     */           try {
/* 307 */             if (!watchers[v].containsCreature(cc[xx]))
/*     */             {
/* 309 */               if (watchers[v].getWatcher() != null && watchers[v].getWatcher().getWurmId() != cc[xx].getWurmId()) {
/* 310 */                 performer.getCommunicator().sendNormalServerMessage(cc[xx]
/* 311 */                     .getName() + " (" + cc[xx].getWurmId() + ") is not visible to " + watchers[v]
/* 312 */                     .getWatcher().getName());
/* 313 */               } else if (watchers[v].getWatcher() == null) {
/* 314 */                 performer.getCommunicator().sendNormalServerMessage("The tile is monitored by an unknown creature or player who will not see the creature.");
/*     */               }
/*     */             
/*     */             }
/* 318 */           } catch (Exception e) {
/*     */             
/* 320 */             logger.log(Level.WARNING, e.getMessage(), e);
/*     */           } 
/*     */         } 
/*     */       } 
/* 324 */       Item[] items = rt.getItems();
/* 325 */       if (Servers.localServer.testServer) {
/*     */         
/* 327 */         for (Item i : items) {
/*     */ 
/*     */           
/* 330 */           String itemMessage = String.format("It contains %s, at floor level %d, at Z position %.2f", new Object[] { i
/* 331 */                 .getName(), Integer.valueOf(i.getFloorLevel()), Float.valueOf(i.getPosZ()) });
/* 332 */           performer.getCommunicator().sendNormalServerMessage(itemMessage);
/*     */         } 
/* 334 */         if (performer.getPower() >= 5) {
/*     */ 
/*     */           
/* 337 */           String zoneMessage = String.format("Tile belongs to zone %d, which covers %d, %d to %d, %d.", new Object[] {
/* 338 */                 Integer.valueOf(z.getId()), Integer.valueOf(z.getStartX()), Integer.valueOf(z.getStartY()), Integer.valueOf(z.getEndX()), Integer.valueOf(z.getEndY()) });
/* 339 */           performer.getCommunicator().sendNormalServerMessage(zoneMessage);
/*     */ 
/*     */           
/* 342 */           VolaTile caveTile = Zones.getOrCreateTile(rt.tilex, rt.tiley, false);
/* 343 */           String caveVTMessage = String.format("Cave VolaTile instance transition is %s, layer is %d. It contains %d items.", new Object[] {
/* 344 */                 Boolean.valueOf(caveTile.isTransition()), Integer.valueOf(caveTile.getLayer()), Integer.valueOf((caveTile.getItems()).length) });
/* 345 */           performer.getCommunicator().sendNormalServerMessage(caveVTMessage);
/*     */ 
/*     */           
/* 348 */           VolaTile surfTile = Zones.getOrCreateTile(rt.tilex, rt.tiley, true);
/* 349 */           String surfVTMessage = String.format("Surface VolaTile instance transition is %s, layer is %d. It contains %d items.", new Object[] {
/* 350 */                 Boolean.valueOf(surfTile.isTransition()), Integer.valueOf(surfTile.getLayer()), Integer.valueOf((surfTile.getItems()).length) });
/* 351 */           performer.getCommunicator().sendNormalServerMessage(surfVTMessage);
/*     */         } 
/*     */       } else {
/*     */         
/* 355 */         performer.getCommunicator().sendNormalServerMessage("It contains " + items.length + " items.");
/*     */       } 
/* 357 */     } catch (NoSuchZoneException nsz) {
/*     */       
/* 359 */       performer.getCommunicator().sendNormalServerMessage(tilex + "," + tiley + " no zone.");
/*     */     } 
/*     */     
/*     */     try {
/* 363 */       float height = Zones.calculateHeight(((tilex << 2) + 2), ((tiley << 2) + 2), performer.isOnSurface()) * 10.0F;
/* 364 */       byte path = Cults.getPathFor(tilex, tiley, 0, (int)height);
/* 365 */       performer.getCommunicator().sendNormalServerMessage("Meditation path is " + Cults.getPathNameFor(path) + ".");
/*     */     }
/* 367 */     catch (NoSuchZoneException nsz) {
/*     */       
/* 369 */       logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
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
/*     */   public static void checkZones(Creature checker) {
/* 381 */     logger.info(checker.getName() + " checking zones");
/* 382 */     checker.getCommunicator().sendNormalServerMessage("Checking cave zone tiles:");
/* 383 */     Zones.checkAllCaveZones(checker);
/* 384 */     checker.getCommunicator().sendNormalServerMessage("Checking surface zone tiles:");
/* 385 */     Zones.checkAllSurfaceZones(checker);
/* 386 */     checker.getCommunicator().sendNormalServerMessage("Done.");
/* 387 */     logger.info(checker.getName() + " finished checking zones");
/*     */   }
/*     */   
/*     */   public static void checkItemWatchers() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\ErrorChecks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
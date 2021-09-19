/*      */ package com.wurmonline.server.endgames;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.MethodsCreatures;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreaturePos;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.NotOwnedException;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.FocusZone;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.Enchants;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
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
/*      */ public final class EndGameItems
/*      */   implements MiscConstants, ItemTypes, Enchants, TimeConstants
/*      */ {
/*   88 */   public static final Map<Long, EndGameItem> altars = new HashMap<>();
/*   89 */   private static final Map<Long, EndGameItem> artifacts = new HashMap<>();
/*      */   
/*   91 */   private static final Logger logger = Logger.getLogger(EndGameItems.class.getName());
/*      */   private static final String LOAD_ENDGAMEITEMS = "SELECT * FROM ENDGAMEITEMS";
/*   93 */   private static float posx = 0.0F;
/*   94 */   private static float posy = 0.0F;
/*   95 */   private static int tileX = 0;
/*   96 */   private static int tileY = 0;
/*   97 */   private static final LinkedList<Kingdom> missingCrowns = new LinkedList<>();
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte chargeDecay = 10;
/*      */ 
/*      */ 
/*      */   
/*  105 */   private static long lastRechargedItem = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void createAltars() {
/*  116 */     logger.log(Level.INFO, "Creating altars.");
/*      */     
/*  118 */     boolean found = false;
/*  119 */     int startX = (Zones.worldTileSizeX - 10) / 2;
/*  120 */     int startY = Math.min(Zones.worldTileSizeY / 20, 300);
/*  121 */     int tries = 0;
/*  122 */     while (!found && tries < 1000) {
/*      */       
/*  124 */       tries++;
/*  125 */       float posz = findPlacementTile(startX, startY);
/*  126 */       if (posz <= 0.0F) {
/*      */         
/*  128 */         startX += Math.min(Zones.worldTileSizeX / 20, 300);
/*  129 */         if (startX >= Zones.worldTileSizeX - Math.min(Zones.worldTileSizeX / 20, 100)) {
/*      */           
/*  131 */           startX = (Zones.worldTileSizeX - 10) / 2;
/*  132 */           startY += Math.min(Zones.worldTileSizeY / 20, 100);
/*      */         } 
/*  134 */         if (startY >= Zones.worldTileSizeY - Math.min(Zones.worldTileSizeY / 20, 100))
/*      */           break; 
/*      */         continue;
/*      */       } 
/*  138 */       found = true;
/*      */     } 
/*  140 */     if (!found) {
/*      */       
/*  142 */       logger.log(Level.WARNING, "Failed to locate a good spot to create holy altar. Exiting.");
/*      */       return;
/*      */     } 
/*  145 */     posx = (tileX << 2);
/*  146 */     posy = (tileY << 2);
/*      */     
/*      */     try {
/*  149 */       Item holy = ItemFactory.createItem(327, 90.0F, posx, posy, 180.0F, true, (byte)0, -10L, null);
/*  150 */       holy.bless(1);
/*  151 */       holy.enchant((byte)5);
/*  152 */       EndGameItem eg = new EndGameItem(holy, true, (short)68, true);
/*  153 */       altars.put(new Long(eg.getWurmid()), eg);
/*  154 */       logger.log(Level.INFO, "Created holy altar at " + posx + ", " + posy + ".");
/*      */     }
/*  156 */     catch (NoSuchTemplateException nst) {
/*      */       
/*  158 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     }
/*  160 */     catch (FailedException fe) {
/*      */       
/*  162 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */     } 
/*      */ 
/*      */     
/*  166 */     tileX = 0;
/*  167 */     tileY = 0;
/*  168 */     found = false;
/*  169 */     startX = (Zones.worldTileSizeX - 10) / 2;
/*  170 */     startY = Math.max(Zones.worldTileSizeY - 300, Zones.worldTileSizeY - Zones.worldTileSizeY / 20);
/*  171 */     tries = 0;
/*  172 */     while (!found && tries < 1000) {
/*      */       
/*  174 */       tries++;
/*  175 */       float posz = findPlacementTile(startX, startY);
/*  176 */       if (posz <= 0.0F) {
/*      */         
/*  178 */         startX += Math.min(Zones.worldTileSizeX / 20, 300);
/*  179 */         if (startX >= Zones.worldTileSizeX - Math.min(Zones.worldTileSizeX / 20, 100)) {
/*      */           
/*  181 */           startX = (Zones.worldTileSizeX - 10) / 2;
/*  182 */           startY -= Math.min(Zones.worldTileSizeY / 20, 100);
/*      */         } 
/*  184 */         if (startY <= 0)
/*      */           break; 
/*      */         continue;
/*      */       } 
/*  188 */       found = true;
/*      */     } 
/*  190 */     if (!found) {
/*      */       
/*  192 */       logger.log(Level.WARNING, "Failed to locate a good spot to create unholy altar. Exiting.");
/*      */       return;
/*      */     } 
/*  195 */     posx = (tileX << 2);
/*  196 */     posy = (tileY << 2);
/*      */     
/*      */     try {
/*  199 */       Item unholy = ItemFactory.createItem(328, 90.0F, posx, posy, 180.0F, true, (byte)0, -10L, null);
/*  200 */       unholy.bless(4);
/*  201 */       unholy.enchant((byte)8);
/*  202 */       EndGameItem eg = new EndGameItem(unholy, false, (short)68, true);
/*  203 */       altars.put(new Long(eg.getWurmid()), eg);
/*  204 */       logger.log(Level.INFO, "Created unholy altar at " + posx + ", " + posy + ".");
/*      */     }
/*  206 */     catch (NoSuchTemplateException nst) {
/*      */       
/*  208 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     }
/*  210 */     catch (FailedException fe) {
/*      */       
/*  212 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final EndGameItem getEvilAltar() {
/*  219 */     if (altars != null)
/*      */     {
/*  221 */       for (EndGameItem eg : altars.values()) {
/*      */         
/*  223 */         if (eg.getItem().getTemplateId() == 328)
/*  224 */           return eg; 
/*      */       } 
/*      */     }
/*  227 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final EndGameItem getGoodAltar() {
/*  233 */     if (altars != null)
/*      */     {
/*  235 */       for (EndGameItem eg : altars.values()) {
/*      */         
/*  237 */         if (eg.getItem().getTemplateId() == 327)
/*  238 */           return eg; 
/*      */       } 
/*      */     }
/*  241 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final float findPlacementTile(int tx, int ty) {
/*  246 */     float maxZ = 0.0F;
/*  247 */     if (Zones.isWithinDuelRing(tx, ty, tx + 20, ty + 20))
/*  248 */       return maxZ; 
/*  249 */     for (int x = 0; x < 20; x++) {
/*      */       
/*  251 */       for (int y = 0; y < 20; y++) {
/*      */         
/*  253 */         int tile = Server.surfaceMesh.getTile(tx + x, ty + y);
/*      */         
/*  255 */         float z = Tiles.decodeHeight(tile);
/*  256 */         byte ttype = Tiles.decodeType(tile);
/*  257 */         if (ttype != Tiles.Tile.TILE_ROCK.id && ttype != Tiles.Tile.TILE_CLIFF.id && ttype != Tiles.Tile.TILE_HOLE.id)
/*      */         {
/*  259 */           if (z > 0.0F && z > maxZ && z < 700.0F) {
/*      */             
/*  261 */             tileX = tx + x;
/*  262 */             tileY = ty + y;
/*  263 */             maxZ = z;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  268 */     return maxZ;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void createArtifacts() {
/*      */     try {
/*  275 */       Item rod = ItemFactory.createItem(329, 90.0F, (byte)3, null);
/*  276 */       rod.bless(1);
/*  277 */       rod.enchant((byte)5);
/*  278 */       placeArtifact(rod);
/*  279 */       EndGameItem eg = new EndGameItem(rod, true, (short)69, true);
/*  280 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  282 */       Item crownmight = ItemFactory.createItem(330, 90.0F, (byte)3, null);
/*  283 */       crownmight.bless(2);
/*  284 */       crownmight.enchant((byte)6);
/*  285 */       placeArtifact(crownmight);
/*  286 */       eg = new EndGameItem(crownmight, true, (short)69, true);
/*  287 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  289 */       Item charmOfFo = ItemFactory.createItem(331, 90.0F, (byte)3, null);
/*  290 */       charmOfFo.bless(1);
/*  291 */       charmOfFo.enchant((byte)5);
/*  292 */       placeArtifact(charmOfFo);
/*  293 */       eg = new EndGameItem(charmOfFo, true, (short)69, true);
/*  294 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  296 */       Item vynorasEye = ItemFactory.createItem(332, 90.0F, (byte)3, null);
/*  297 */       vynorasEye.bless(3);
/*  298 */       vynorasEye.enchant((byte)7);
/*  299 */       placeArtifact(vynorasEye);
/*  300 */       eg = new EndGameItem(vynorasEye, true, (short)69, true);
/*  301 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  303 */       Item vynorasEar = ItemFactory.createItem(333, 90.0F, (byte)3, null);
/*  304 */       vynorasEar.bless(3);
/*  305 */       vynorasEar.enchant((byte)7);
/*  306 */       placeArtifact(vynorasEar);
/*  307 */       eg = new EndGameItem(vynorasEar, true, (short)69, true);
/*  308 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  310 */       Item vynorasMouth = ItemFactory.createItem(334, 90.0F, (byte)3, null);
/*  311 */       vynorasMouth.bless(3);
/*  312 */       vynorasEar.enchant((byte)7);
/*  313 */       placeArtifact(vynorasMouth);
/*  314 */       eg = new EndGameItem(vynorasMouth, true, (short)69, true);
/*  315 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  317 */       Item fingerOfFo = ItemFactory.createItem(335, 90.0F, (byte)3, null);
/*  318 */       fingerOfFo.bless(1);
/*  319 */       fingerOfFo.enchant((byte)5);
/*  320 */       placeArtifact(fingerOfFo);
/*  321 */       eg = new EndGameItem(fingerOfFo, true, (short)69, true);
/*  322 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  324 */       Item swordOfMagranon = ItemFactory.createItem(336, 90.0F, (byte)3, null);
/*  325 */       swordOfMagranon.bless(2);
/*  326 */       swordOfMagranon.enchant((byte)4);
/*  327 */       placeArtifact(swordOfMagranon);
/*  328 */       eg = new EndGameItem(swordOfMagranon, true, (short)69, true);
/*  329 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  331 */       Item hammerOfMagranon = ItemFactory.createItem(337, 90.0F, (byte)3, null);
/*  332 */       hammerOfMagranon.bless(2);
/*  333 */       hammerOfMagranon.enchant((byte)4);
/*  334 */       placeArtifact(hammerOfMagranon);
/*  335 */       eg = new EndGameItem(hammerOfMagranon, true, (short)69, true);
/*  336 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  338 */       Item libilasScale = ItemFactory.createItem(338, 90.0F, (byte)3, null);
/*  339 */       libilasScale.bless(4);
/*  340 */       libilasScale.enchant((byte)8);
/*  341 */       placeArtifact(libilasScale);
/*  342 */       eg = new EndGameItem(libilasScale, false, (short)69, true);
/*  343 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  345 */       Item orbOfDoom = ItemFactory.createItem(339, 90.0F, (byte)3, null);
/*  346 */       orbOfDoom.bless(4);
/*  347 */       orbOfDoom.enchant((byte)8);
/*  348 */       placeArtifact(orbOfDoom);
/*  349 */       eg = new EndGameItem(orbOfDoom, false, (short)69, true);
/*  350 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */       
/*  352 */       Item sceptreOfAscension = ItemFactory.createItem(340, 90.0F, (byte)3, null);
/*  353 */       sceptreOfAscension.bless(4);
/*  354 */       sceptreOfAscension.enchant((byte)1);
/*  355 */       placeArtifact(sceptreOfAscension);
/*  356 */       eg = new EndGameItem(sceptreOfAscension, false, (short)69, true);
/*  357 */       artifacts.put(new Long(eg.getWurmid()), eg);
/*      */     }
/*  359 */     catch (NoSuchTemplateException nst) {
/*      */       
/*  361 */       logger.log(Level.WARNING, "Failed to create item: " + nst.getMessage(), (Throwable)nst);
/*      */     }
/*  363 */     catch (FailedException fe) {
/*      */       
/*  365 */       logger.log(Level.WARNING, "Failed to create item: " + fe.getMessage(), (Throwable)fe);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void placeArtifact(Item artifact) {
/*  371 */     boolean found = false;
/*  372 */     while (!found) {
/*      */       
/*  374 */       int x = Server.rand.nextInt(Zones.worldTileSizeX);
/*  375 */       int y = Server.rand.nextInt(Zones.worldTileSizeX);
/*  376 */       int tile = Server.surfaceMesh.getTile(x, y);
/*  377 */       int rocktile = Server.rockMesh.getTile(x, y);
/*  378 */       float th = Tiles.decodeHeightAsFloat(tile);
/*  379 */       float rh = Tiles.decodeHeightAsFloat(rocktile);
/*      */       
/*  381 */       FocusZone hoderZone = FocusZone.getHotaZone();
/*  382 */       assert hoderZone != null;
/*  383 */       float seth = 0.0F;
/*  384 */       if (th > 4.0F && rh > 4.0F) {
/*      */         
/*  386 */         if (th - rh >= 1.0F)
/*  387 */           seth = Math.max(1, Server.rand.nextInt((int)(th * 10.0F - 5.0F - rh * 10.0F))); 
/*  388 */         if (seth > 0.0F) {
/*      */           
/*  390 */           VolaTile t = Zones.getTileOrNull(x, y, true);
/*  391 */           if (t == null || (t.getStructure() == null && t.getVillage() == null && t.getZone() != hoderZone)) {
/*      */             
/*  393 */             seth /= 10.0F;
/*  394 */             found = true;
/*  395 */             artifact.setPosXYZ(((x << 2) + 2), ((y << 2) + 2), rh + seth);
/*  396 */             artifact.setAuxData((byte)30);
/*  397 */             logger.log(Level.INFO, "Placed " + artifact.getName() + " at " + x + "," + y + " at height " + (rh + seth) + " rockheight=" + rh + " tileheight=" + th);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Item[] getArtifactDugUp(int x, int y, float height, boolean allCornersRock) {
/*  408 */     Set<Item> found = new HashSet<>();
/*  409 */     for (EndGameItem artifact : artifacts.values()) {
/*      */       
/*  411 */       if (artifact.getItem().getZoneId() == -10L && artifact.getItem().getOwnerId() == -10L)
/*      */       {
/*  413 */         if ((int)artifact.getItem().getPosX() >> 2 == x && (int)artifact.getItem().getPosY() >> 2 == y && (height <= artifact
/*  414 */           .getItem().getPosZ() || allCornersRock)) {
/*      */           
/*  416 */           found.add(artifact.getItem());
/*  417 */           artifact.setLastMoved(System.currentTimeMillis());
/*      */         } 
/*      */       }
/*      */     } 
/*  421 */     return found.<Item>toArray(new Item[found.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EndGameItem getArtifactAtTile(int x, int y) {
/*  426 */     for (EndGameItem artifact : artifacts.values()) {
/*      */       
/*  428 */       if (artifact.getItem().getZoneId() == -10L && artifact.getItem().getOwnerId() == -10L)
/*      */       {
/*  430 */         if ((int)artifact.getItem().getPosX() >> 2 == x && (int)artifact.getItem().getPosY() >> 2 == y)
/*      */         {
/*  432 */           return artifact;
/*      */         }
/*      */       }
/*      */     } 
/*  436 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void deleteEndGameItem(EndGameItem eg) {
/*  441 */     if (eg != null) {
/*      */       
/*  443 */       if (eg.getItem().isHugeAltar()) {
/*  444 */         altars.remove(new Long(eg.getWurmid()));
/*  445 */       } else if (eg.getItem().isArtifact()) {
/*  446 */         artifacts.remove(new Long(eg.getWurmid()));
/*  447 */       }  eg.delete();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadEndGameItems() {
/*  453 */     logger.info("Loading End Game Items.");
/*  454 */     long now = System.nanoTime();
/*      */     
/*  456 */     if (Servers.localServer.id == 3 || Servers.localServer.id == 12 || Servers.localServer.isChallengeServer() || (
/*  457 */       Server.getInstance().isPS() && Constants.loadEndGameItems)) {
/*      */       
/*  459 */       Connection dbcon = null;
/*  460 */       PreparedStatement ps = null;
/*  461 */       ResultSet rs = null;
/*      */       
/*      */       try {
/*  464 */         dbcon = DbConnector.getItemDbCon();
/*  465 */         ps = dbcon.prepareStatement("SELECT * FROM ENDGAMEITEMS");
/*  466 */         rs = ps.executeQuery();
/*  467 */         long iid = -10L;
/*  468 */         boolean holy = true;
/*  469 */         short type = 0;
/*  470 */         boolean found = false;
/*  471 */         boolean foundAltar = false;
/*  472 */         long lastMoved = 0L;
/*  473 */         while (rs.next()) {
/*      */           
/*  475 */           iid = rs.getLong("WURMID");
/*  476 */           holy = rs.getBoolean("HOLY");
/*  477 */           type = rs.getShort("TYPE");
/*  478 */           lastMoved = rs.getLong("LASTMOVED");
/*      */           
/*      */           try {
/*  481 */             Item item = Items.getItem(iid);
/*  482 */             EndGameItem eg = new EndGameItem(item, holy, type, false);
/*  483 */             eg.lastMoved = lastMoved;
/*  484 */             if (type == 68) {
/*      */               
/*  486 */               eg.setLastMoved(System.currentTimeMillis());
/*  487 */               foundAltar = true;
/*  488 */               altars.put(new Long(iid), eg); continue;
/*      */             } 
/*  490 */             if (type == 69) {
/*      */               
/*  492 */               found = true;
/*  493 */               artifacts.put(new Long(iid), eg);
/*  494 */               if (logger.isLoggable(Level.FINE))
/*      */               {
/*  496 */                 logger.fine("Loaded Artifact, ID: " + iid + ", " + eg);
/*      */               }
/*      */               
/*      */               continue;
/*      */             } 
/*  501 */             logger.warning("End Game Items should only be Huge Altars or Artifiacts not type " + type + ", ID: " + iid + ", " + eg);
/*      */ 
/*      */           
/*      */           }
/*  505 */           catch (NoSuchItemException nsi) {
/*      */ 
/*      */             
/*  508 */             if (Server.getInstance().isPS()) {
/*      */               
/*  510 */               logger.log(Level.INFO, "Endgame item missing: " + iid + ". Deleting entry.");
/*  511 */               EndGameItem.delete(iid);
/*  512 */               if (type == 68) {
/*  513 */                 logger.log(Level.INFO, (holy ? "White Light" : "Black Light") + " altar is missing. Destroy the " + (!holy ? "White Light" : "Black Light") + " altar to respawn both.");
/*      */               }
/*      */               continue;
/*      */             } 
/*  517 */             logger.log(Level.WARNING, "Endgame item missing: " + iid, (Throwable)nsi);
/*      */           } 
/*      */         } 
/*  520 */         DbUtilities.closeDatabaseObjects(ps, rs);
/*  521 */         if (!found) {
/*      */           
/*  523 */           createArtifacts();
/*      */         } else {
/*      */           
/*  526 */           setArtifactsInWorld();
/*  527 */         }  if (!foundAltar)
/*      */         {
/*  529 */           createAltars();
/*      */         }
/*      */       }
/*  532 */       catch (SQLException sqx) {
/*      */         
/*  534 */         logger.log(Level.WARNING, "Failed to load item datas: " + sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  538 */         DbUtilities.closeDatabaseObjects(ps, rs);
/*  539 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */     
/*  543 */     int numberOfAltars = (altars != null) ? altars.size() : 0;
/*  544 */     int numberOfArtifacts = (artifacts != null) ? artifacts.size() : 0;
/*  545 */     logger.log(Level.INFO, "Loaded " + numberOfAltars + " altars and " + numberOfArtifacts + " artifacts. That took " + (
/*  546 */         (float)(System.nanoTime() - now) / 1000000.0F) + " ms.");
/*      */   }
/*      */ 
/*      */   
/*      */   public static EndGameItem getEndGameItem(Item item) {
/*  551 */     if (item.isHugeAltar())
/*  552 */       return altars.get(new Long(item.getWurmId())); 
/*  553 */     if (item.isArtifact())
/*  554 */       return artifacts.get(new Long(item.getWurmId())); 
/*  555 */     return null;
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
/*      */   public static final boolean mayRechargeItem() {
/*  568 */     return (System.currentTimeMillis() - lastRechargedItem > 60000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void touchRecharge() {
/*  576 */     lastRechargedItem = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void destroyHugeAltar(Item altar, Creature destroyer) {
/*  581 */     EndGameItem eg = altars.get(new Long(altar.getWurmId()));
/*      */     
/*  583 */     if (eg != null) {
/*      */       
/*  585 */       Server.getInstance().broadCastAlert("The " + altar
/*  586 */           .getName() + " has fallen to the hands of " + destroyer.getName() + "!", true, (byte)4);
/*  587 */       HistoryManager.addHistory(destroyer.getName(), "Destroyed the " + altar.getName() + ".");
/*  588 */       if (destroyer.isPlayer()) {
/*      */         
/*  590 */         float sx = altar.getPosX() - 100.0F;
/*  591 */         float ex = altar.getPosX() + 100.0F;
/*  592 */         float sy = altar.getPosY() - 100.0F;
/*  593 */         float ey = altar.getPosY() + 100.0F;
/*      */         
/*  595 */         for (Player p : Players.getInstance().getPlayers()) {
/*      */           
/*  597 */           if (p.getPosX() > sx && p.getPosX() < ex && 
/*  598 */             p.getPosY() > sy && p.getPosY() < ey && 
/*  599 */             p.getKingdomId() == destroyer.getKingdomId()) {
/*      */             
/*  601 */             p.addTitle(Titles.Title.Altar_Destroyer);
/*  602 */             if (eg.isHoly()) {
/*  603 */               p.achievement(356);
/*      */             } else {
/*  605 */               p.achievement(357);
/*      */             } 
/*      */           } 
/*      */         } 
/*  609 */       }  Player[] players = Players.getInstance().getPlayers();
/*      */       
/*  611 */       for (Player lPlayer : players) {
/*      */         
/*  613 */         if (eg.isHoly()) {
/*      */           
/*  615 */           if (lPlayer.getDeity() != null && lPlayer.getDeity().isHateGod())
/*      */           {
/*  617 */             lPlayer.setFarwalkerSeconds((byte)100);
/*  618 */             lPlayer.healRandomWound(100);
/*      */           }
/*  620 */           else if (lPlayer.getDeity() != null && !lPlayer.getDeity().isHateGod())
/*      */           {
/*  622 */             lPlayer.getCommunicator().sendCombatAlertMessage("Your life force is drained, as it is used to heal the " + altar
/*  623 */                 .getName() + "!");
/*  624 */             lPlayer.addWoundOfType(null, (byte)9, 1, false, 1.0F, false, 5000.0D, 0.0F, 0.0F, false, false);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*  630 */         else if (lPlayer.getDeity() != null && !lPlayer.getDeity().isHateGod()) {
/*      */           
/*  632 */           lPlayer.setFarwalkerSeconds((byte)100);
/*  633 */           lPlayer.healRandomWound(100);
/*      */         }
/*  635 */         else if (lPlayer.getDeity() != null && lPlayer.getDeity().isHateGod()) {
/*      */           
/*  637 */           lPlayer.getCommunicator().sendCombatAlertMessage("Your life force is drained, as it is used to heal the " + altar
/*  638 */               .getName() + "!");
/*  639 */           lPlayer.addWoundOfType(null, (byte)9, 1, false, 1.0F, false, 5000.0D, 0.0F, 0.0F, false, false);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  644 */       healAndTeleportAltar(eg);
/*      */       
/*  646 */       hideRandomArtifact(eg.isHoly());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void healAndTeleportAltar(EndGameItem altar) {
/*  652 */     Item altarItem = altar.getItem();
/*      */     
/*  654 */     altarItem.putInVoid();
/*  655 */     altarItem.setDamage(0.0F);
/*      */     
/*  657 */     Player[] p = Players.getInstance().getPlayers();
/*  658 */     for (Player lPlayer : p)
/*  659 */       lPlayer.getCommunicator().sendRemoveEffect(altar.getWurmid()); 
/*  660 */     boolean found = false;
/*  661 */     int randX = Zones.worldTileSizeX - 200;
/*  662 */     int randY = Zones.worldTileSizeY / 2;
/*      */     
/*  664 */     int startX = Zones.safeTileX(100 + Server.rand.nextInt(randX));
/*  665 */     int startY = Zones.safeTileY(100 + Server.rand.nextInt(randY));
/*  666 */     if (!altar.isHoly())
/*  667 */       startY = Zones.safeTileY(Zones.worldTileSizeY / 2 + startY); 
/*  668 */     int tries = 0;
/*  669 */     float posz = 0.0F;
/*  670 */     while (!found && tries < 1000) {
/*      */       
/*  672 */       tries++;
/*  673 */       posz = findPlacementTile(startX, startY);
/*      */       
/*  675 */       if (Villages.getVillageWithPerimeterAt(tileX, tileY, true) != null)
/*  676 */         posz = -1.0F; 
/*  677 */       if (posz <= 0.0F) {
/*      */         
/*  679 */         startX = Zones.safeTileX(100 + Server.rand.nextInt(randX));
/*  680 */         startY = Zones.safeTileY(100 + Server.rand.nextInt(randY));
/*  681 */         if (!altar.isHoly())
/*  682 */           startY = Zones.safeTileY(Zones.worldTileSizeY / 2 + startY); 
/*      */         continue;
/*      */       } 
/*  685 */       found = true;
/*      */     } 
/*  687 */     if (!found) {
/*      */       
/*  689 */       logger.log(Level.WARNING, "Failed to locate a good spot to create holy altar. Exiting.");
/*      */       return;
/*      */     } 
/*  692 */     posx = ((tileX << 2) + 2);
/*  693 */     posy = ((tileY << 2) + 2);
/*  694 */     altarItem.setPosXYZ(posx, posy, posz);
/*      */     
/*      */     try {
/*  697 */       Zone z = Zones.getZone(tileX, tileY, true);
/*  698 */       z.addItem(altarItem);
/*  699 */       if (altar.isHoly()) {
/*      */         
/*  701 */         for (Player lPlayer : p) {
/*  702 */           lPlayer.getCommunicator().sendAddEffect(altar.getWurmid(), (short)2, altar
/*  703 */               .getItem().getPosX(), altar.getItem().getPosY(), altar.getItem().getPosZ(), (byte)0);
/*      */         }
/*      */       } else {
/*      */         
/*  707 */         for (Player lPlayer : p) {
/*  708 */           lPlayer.getCommunicator().sendAddEffect(altar.getWurmid(), (short)3, altar
/*  709 */               .getItem().getPosX(), altar.getItem().getPosY(), altar.getItem().getPosZ(), (byte)0);
/*      */         }
/*      */       } 
/*  712 */     } catch (NoSuchZoneException nsz) {
/*      */       
/*  714 */       logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void hideRandomArtifact(boolean holy) {
/*  720 */     EndGameItem[] arts = (EndGameItem[])artifacts.values().toArray((Object[])new EndGameItem[artifacts.size()]);
/*  721 */     Item artifactToPlace = null;
/*  722 */     List<Item> candidates = new ArrayList<>();
/*  723 */     for (EndGameItem lArt : arts) {
/*      */       
/*  725 */       Item artifact = lArt.getItem();
/*  726 */       if (lArt.isInWorld())
/*      */       {
/*  728 */         if (lArt.isHoly() == holy)
/*  729 */           candidates.add(artifact); 
/*      */       }
/*      */     } 
/*  732 */     if (candidates.size() > 0) {
/*      */       
/*  734 */       artifactToPlace = candidates.get(Server.rand.nextInt(candidates.size()));
/*      */       
/*      */       try {
/*  737 */         Item parent = artifactToPlace.getParent();
/*  738 */         parent.dropItem(artifactToPlace.getWurmId(), false);
/*  739 */         placeArtifact(artifactToPlace);
/*      */       }
/*  741 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void setArtifactsInWorld() {
/*  750 */     EndGameItem[] arts = (EndGameItem[])artifacts.values().toArray((Object[])new EndGameItem[artifacts.size()]);
/*  751 */     for (EndGameItem lArt : arts) {
/*      */       
/*  753 */       Item artifact = lArt.getItem();
/*  754 */       if (artifact.getOwnerId() != -10L) {
/*      */         
/*  756 */         CreaturePos stat = CreaturePos.getPosition(artifact.getOwnerId());
/*  757 */         if (stat != null && 
/*  758 */           stat.getPosX() > 0.0F) {
/*      */           
/*      */           try {
/*      */             
/*  762 */             Item parent = artifact.getParent();
/*  763 */             parent.dropItem(artifact.getWurmId(), false);
/*  764 */             Zone z = Zones.getZone((int)stat.getPosX() >> 2, (int)stat.getPosY() >> 2, 
/*  765 */                 (stat.getLayer() >= 0));
/*  766 */             artifact.setPosXY(stat.getPosX(), stat.getPosY());
/*  767 */             z.addItem(artifact);
/*  768 */             logger.log(Level.INFO, "Zone " + z
/*  769 */                 .getId() + " added " + artifact.getName() + " at " + (
/*  770 */                 (int)stat.getPosX() >> 2) + "," + ((int)stat.getPosY() >> 2));
/*      */           }
/*  772 */           catch (NoSuchItemException nsi) {
/*      */             
/*  774 */             logger.log(Level.WARNING, artifact.getName() + ": " + nsi.getMessage(), (Throwable)nsi);
/*      */           }
/*  776 */           catch (NoSuchZoneException nsz) {
/*      */             
/*  778 */             logger.log(Level.WARNING, artifact.getName() + ": " + nsz.getMessage(), (Throwable)nsz);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void pollAll() {
/*  787 */     EndGameItem[] arts = (EndGameItem[])artifacts.values().toArray((Object[])new EndGameItem[artifacts.size()]);
/*  788 */     for (EndGameItem lArt : arts) {
/*      */       
/*  790 */       if (lArt.isInWorld() && 
/*  791 */         System.currentTimeMillis() - lArt.getLastMoved() > (Servers.isThisATestServer() ? 60000L : 604800000L)) {
/*      */ 
/*      */         
/*  794 */         lArt.setLastMoved(System.currentTimeMillis());
/*  795 */         Item artifact = lArt.getItem();
/*  796 */         if (artifact.getAuxData() <= 0) {
/*      */           
/*  798 */           moveArtifact(artifact);
/*      */         }
/*      */         else {
/*      */           
/*  802 */           artifact.setAuxData((byte)Math.max(0, artifact.getAuxData() - 10));
/*      */           
/*      */           try {
/*  805 */             if (artifact.getOwner() != -10L)
/*      */             {
/*  807 */               Creature owner = Server.getInstance().getCreature(artifact.getOwner());
/*  808 */               owner.getCommunicator().sendNormalServerMessage(artifact.getName() + " vibrates faintly.");
/*      */             }
/*      */           
/*  811 */           } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */           
/*      */           }
/*  815 */           catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */           
/*      */           }
/*  819 */           catch (NotOwnedException notOwnedException) {}
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void moveArtifact(Item artifact) {
/*      */     String act;
/*      */     try {
/*  832 */       if (artifact.getOwner() != -10L)
/*      */       {
/*  834 */         Creature owner = Server.getInstance().getCreature(artifact.getOwner());
/*  835 */         owner.getCommunicator().sendNormalServerMessage(artifact
/*  836 */             .getName() + " disappears. It has fulfilled its mission.");
/*      */       }
/*      */     
/*  839 */     } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */     
/*      */     }
/*  843 */     catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */     
/*      */     }
/*  847 */     catch (NotOwnedException notOwnedException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  852 */     switch (Server.rand.nextInt(6)) {
/*      */       
/*      */       case 0:
/*  855 */         act = "is reported to have disappeared.";
/*      */         break;
/*      */       case 1:
/*  858 */         act = "is gone missing.";
/*      */         break;
/*      */       case 2:
/*  861 */         act = "returned to the depths.";
/*      */         break;
/*      */       case 3:
/*  864 */         act = "seems to have decided to leave.";
/*      */         break;
/*      */       case 4:
/*  867 */         act = "has found a new location.";
/*      */         break;
/*      */       default:
/*  870 */         act = "has vanished.";
/*      */         break;
/*      */     } 
/*  873 */     HistoryManager.addHistory("The " + artifact.getName(), act);
/*  874 */     artifact.putInVoid();
/*  875 */     placeArtifact(artifact);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void destroyArtifacts() {
/*  880 */     EndGameItem[] arts = (EndGameItem[])artifacts.values().toArray((Object[])new EndGameItem[artifacts.size()]);
/*  881 */     for (EndGameItem lArt : arts) {
/*      */       
/*  883 */       Item artifact = lArt.getItem();
/*      */       
/*      */       try {
/*  886 */         if (artifact.getOwner() != -10L)
/*      */         {
/*  888 */           Creature owner = Server.getInstance().getCreature(artifact.getOwner());
/*  889 */           owner.getCommunicator().sendNormalServerMessage(artifact
/*  890 */               .getName() + " disappears. It has fulfilled its mission.");
/*      */         }
/*      */       
/*  893 */       } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */       
/*      */       }
/*  897 */       catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */       
/*      */       }
/*  901 */       catch (NotOwnedException notOwnedException) {}
/*      */ 
/*      */ 
/*      */       
/*  905 */       Items.destroyItem(artifact.getWurmId());
/*  906 */       lArt.destroy();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String locateEndGameItem(int templateId, Creature performer) {
/*  912 */     String toReturn = "The artifact was hidden from view by the gods.";
/*  913 */     if (Servers.localServer.HOMESERVER) {
/*      */       
/*  915 */       if (Servers.localServer.serverEast != null && !Servers.localServer.serverEast.HOMESERVER)
/*  916 */         return "You feel a faint indication far to the east."; 
/*  917 */       if (Servers.localServer.serverSouth != null && !Servers.localServer.serverSouth.HOMESERVER)
/*  918 */         return "You feel a faint indication far to the south."; 
/*  919 */       if (Servers.localServer.serverWest != null && !Servers.localServer.serverWest.HOMESERVER)
/*  920 */         return "You feel a faint indication far to the west."; 
/*  921 */       if (Servers.localServer.serverNorth != null && !Servers.localServer.serverNorth.HOMESERVER)
/*  922 */         return "You feel a faint indication far to the north."; 
/*  923 */       return toReturn;
/*      */     } 
/*  925 */     EndGameItem itemsearched = null;
/*  926 */     if (templateId == -1) {
/*      */       
/*  928 */       if (Server.rand.nextBoolean()) {
/*      */         
/*  930 */         missingCrowns.clear();
/*  931 */         Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/*  932 */         for (int x = 0; x < kingdoms.length; x++) {
/*      */           
/*  934 */           if (kingdoms[x].isCustomKingdom() && kingdoms[x].existsHere()) {
/*      */             
/*  936 */             King k = King.getKing(kingdoms[x].getId());
/*  937 */             if (k == null)
/*      */             {
/*  939 */               missingCrowns.add(kingdoms[x]);
/*      */             }
/*      */           } 
/*      */         } 
/*  943 */         if (missingCrowns.size() > 0) {
/*      */           
/*  945 */           int crownToLookFor = Server.rand.nextInt(missingCrowns.size());
/*  946 */           Kingdom toLookFor = missingCrowns.get(crownToLookFor);
/*  947 */           Item[] _items = Items.getAllItems();
/*  948 */           for (Item lItem : _items) {
/*      */             
/*  950 */             if (lItem.isRoyal())
/*      */             {
/*  952 */               if (lItem.getKingdom() == toLookFor.getId())
/*      */               {
/*  954 */                 itemsearched = new EndGameItem(lItem, false, (short)122, false);
/*      */               }
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*  960 */       if (itemsearched == null) {
/*      */         
/*  962 */         int s = artifacts.size();
/*  963 */         if (s > 0) {
/*      */           
/*  965 */           int num = Server.rand.nextInt(s);
/*  966 */           int x = 0;
/*  967 */           for (Iterator<EndGameItem> it = artifacts.values().iterator(); it.hasNext(); )
/*      */           {
/*  969 */             itemsearched = it.next();
/*  970 */             if (x == num)
/*      */               break; 
/*  972 */             x++;
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  979 */       for (Iterator<EndGameItem> it = artifacts.values().iterator(); it.hasNext(); ) {
/*      */         
/*  981 */         itemsearched = it.next();
/*  982 */         if (itemsearched.getItem().getTemplateId() == templateId) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*  987 */     String name = "artifact";
/*  988 */     if (itemsearched != null && itemsearched.getItem() != null) {
/*      */       
/*  990 */       toReturn = "";
/*  991 */       name = itemsearched.getItem().getName();
/*  992 */       if (itemsearched.getType() == 122) {
/*      */         
/*  994 */         Kingdom k = Kingdoms.getKingdom(itemsearched.getItem().getKingdom());
/*  995 */         if (k != null)
/*  996 */           name = itemsearched.getItem().getName() + " of " + k.getName(); 
/*      */       } 
/*  998 */       int tilex = (int)itemsearched.getItem().getPosX() >> 2;
/*  999 */       int tiley = (int)itemsearched.getItem().getPosY() >> 2;
/*      */       
/* 1001 */       if (itemsearched.getItem().getOwnerId() != -10L) {
/*      */ 
/*      */         
/*      */         try {
/* 1005 */           Creature c = Server.getInstance().getCreature(itemsearched.getItem().getOwnerId());
/* 1006 */           toReturn = toReturn + "The " + name + " is carried by " + c.getName() + ". ";
/*      */           
/* 1008 */           VolaTile t = c.getCurrentTile();
/* 1009 */           if (t != null)
/*      */           {
/* 1011 */             if (t.getVillage() != null)
/*      */             {
/* 1013 */               toReturn = toReturn + c.getName() + " is in the settlement of " + t.getVillage().getName() + ". ";
/*      */             }
/* 1015 */             if (t.getStructure() != null)
/*      */             {
/* 1017 */               toReturn = toReturn + c.getName() + " is in the house of " + t.getStructure().getName() + ". ";
/*      */             }
/*      */           }
/*      */         
/* 1021 */         } catch (NoSuchCreatureException nsc) {
/*      */           
/* 1023 */           toReturn = toReturn + "In your vision, you can only discern a shadow that carries the " + name + ". ";
/*      */         }
/* 1025 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 1027 */           toReturn = toReturn + "In your vision, you can only discern a shadow that carries the " + name + ". ";
/*      */         }
/*      */       
/* 1030 */       } else if (itemsearched.isInWorld()) {
/*      */         
/* 1032 */         VolaTile t = Zones.getTileOrNull(tilex, tiley, itemsearched.getItem().isOnSurface());
/* 1033 */         if (t != null) {
/*      */           
/* 1035 */           if (t.getVillage() != null)
/*      */           {
/* 1037 */             toReturn = toReturn + "The " + name + " is in the settlement of " + t.getVillage().getName() + ". ";
/*      */           }
/* 1039 */           if (t.getStructure() != null)
/*      */           {
/* 1041 */             toReturn = toReturn + "The " + name + " is in the house of " + t.getStructure().getName() + ". ";
/*      */           }
/*      */           
/*      */           try {
/* 1045 */             if (itemsearched.getItem() != null)
/*      */             {
/* 1047 */               long parentId = itemsearched.getItem().getTopParent();
/* 1048 */               Item parent = Items.getItem(parentId);
/* 1049 */               if (parent != itemsearched.getItem())
/*      */               {
/* 1051 */                 toReturn = toReturn + "It is within a " + parent.getName() + ".";
/*      */               }
/*      */             }
/*      */           
/* 1055 */           } catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */           
/* 1059 */           toReturn = toReturn + "The " + name + " is in the wild. ";
/* 1060 */           VolaTile ct = performer.getCurrentTile();
/* 1061 */           if (ct != null) {
/*      */             
/* 1063 */             int ctx = ct.tilex;
/* 1064 */             int cty = ct.tiley;
/* 1065 */             int mindist = Math.max(Math.abs(tilex - ctx), Math.abs(tiley - cty));
/* 1066 */             int dir = MethodsCreatures.getDir(performer, tilex, tiley);
/* 1067 */             String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/*      */             
/* 1069 */             toReturn = toReturn + getDistanceString(mindist, name, direction, true);
/*      */           } 
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/* 1076 */             Zone z = Zones.getZone(tilex, tiley, true);
/* 1077 */             Village[] villages = z.getVillages();
/* 1078 */             if (villages.length > 0) {
/*      */               
/* 1080 */               for (Village lVillage : villages)
/*      */               {
/* 1082 */                 toReturn = toReturn + "The " + name + " is near the settlement of " + lVillage.getName() + ". ";
/*      */               }
/*      */             }
/*      */             else {
/*      */               
/* 1087 */               Structure[] structs = z.getStructures();
/* 1088 */               if (structs.length > 0) {
/*      */                 
/* 1090 */                 for (Structure lStruct : structs)
/*      */                 {
/* 1092 */                   toReturn = toReturn + "The " + name + " is near " + lStruct.getName() + ". ";
/*      */                 }
/*      */               }
/*      */               else {
/*      */                 
/* 1097 */                 VolaTile ct = performer.getCurrentTile();
/* 1098 */                 if (ct != null)
/*      */                 {
/* 1100 */                   int ctx = ct.tilex;
/* 1101 */                   int cty = ct.tiley;
/* 1102 */                   int mindist = Math.max(Math.abs(tilex - ctx), Math.abs(tiley - cty));
/* 1103 */                   int dir = MethodsCreatures.getDir(performer, tilex, tiley);
/* 1104 */                   String direction = MethodsCreatures.getLocationStringFor(performer.getStatus()
/* 1105 */                       .getRotation(), dir, "you");
/* 1106 */                   toReturn = toReturn + getDistanceString(mindist, name, direction, true);
/*      */                 }
/*      */               
/*      */               } 
/*      */             } 
/* 1111 */           } catch (NoSuchZoneException nsz) {
/*      */             
/* 1113 */             logger.log(Level.WARNING, "No Zone At " + tilex + ", " + tiley + " surf=true for item " + itemsearched
/* 1114 */                 .getItem().getName() + ".", (Throwable)nsz);
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 1120 */         toReturn = toReturn + "The " + name + " has not yet been revealed. ";
/* 1121 */         VolaTile ct = performer.getCurrentTile();
/* 1122 */         if (ct != null) {
/*      */           
/* 1124 */           int ctx = ct.tilex;
/* 1125 */           int cty = ct.tiley;
/* 1126 */           int mindist = Math.max(Math.abs(tilex - ctx), Math.abs(tiley - cty));
/* 1127 */           int dir = MethodsCreatures.getDir(performer, tilex, tiley);
/*      */           
/* 1129 */           String direction = MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), dir, "you");
/*      */           
/* 1131 */           toReturn = toReturn + getDistanceString(mindist, name, direction, true);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1135 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EndGameItem[] getArtifacts() {
/* 1140 */     return (EndGameItem[])artifacts.values().toArray((Object[])new EndGameItem[artifacts.values().size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getEpicPlayerLocateString(int mindist, String name, String direction) {
/* 1145 */     String toReturn = "";
/* 1146 */     if (mindist == 0) {
/*      */       
/* 1148 */       toReturn = toReturn + "You are practically standing on the " + name + "! ";
/*      */     }
/* 1150 */     else if (mindist < 1) {
/*      */       
/* 1152 */       toReturn = toReturn + "The " + name + " is " + direction + " a few steps away! ";
/*      */     }
/* 1154 */     else if (mindist < 4) {
/*      */       
/* 1156 */       toReturn = toReturn + "The " + name + " is " + direction + " a stone's throw away! ";
/*      */     }
/* 1158 */     else if (mindist < 6) {
/*      */       
/* 1160 */       toReturn = toReturn + "The " + name + " is " + direction + " very close. ";
/*      */     }
/* 1162 */     else if (mindist < 10) {
/*      */       
/* 1164 */       toReturn = toReturn + "The " + name + " is " + direction + " pretty close by. ";
/*      */     }
/* 1166 */     else if (mindist < 20) {
/*      */       
/* 1168 */       toReturn = toReturn + "The " + name + " is " + direction + " fairly close by. ";
/*      */     }
/* 1170 */     else if (mindist < 50) {
/*      */       
/* 1172 */       toReturn = toReturn + "The " + name + " is some distance away " + direction + ". ";
/*      */     }
/* 1174 */     else if (mindist < 200) {
/*      */       
/* 1176 */       toReturn = toReturn + "The " + name + " is quite some distance away " + direction + ". ";
/*      */     }
/*      */     else {
/*      */       
/* 1180 */       toReturn = toReturn + "No such soul found.";
/*      */     } 
/* 1182 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getDistanceString(int mindist, String name, String direction, boolean includeThe) {
/* 1188 */     String toReturn = "";
/* 1189 */     if (mindist == 0) {
/*      */       
/* 1191 */       toReturn = toReturn + "You are practically standing on the " + name + "! ";
/*      */     }
/* 1193 */     else if (mindist < 1) {
/*      */       
/* 1195 */       toReturn = toReturn + "The " + name + " is " + direction + " a few steps away! ";
/*      */     }
/* 1197 */     else if (mindist < 4) {
/*      */       
/* 1199 */       toReturn = toReturn + "The " + name + " is " + direction + " a stone's throw away! ";
/*      */     }
/* 1201 */     else if (mindist < 6) {
/*      */       
/* 1203 */       toReturn = toReturn + "The " + name + " is " + direction + " very close. ";
/*      */     }
/* 1205 */     else if (mindist < 10) {
/*      */       
/* 1207 */       toReturn = toReturn + "The " + name + " is " + direction + " pretty close by. ";
/*      */     }
/* 1209 */     else if (mindist < 20) {
/*      */       
/* 1211 */       toReturn = toReturn + "The " + name + " is " + direction + " fairly close by. ";
/*      */     }
/* 1213 */     else if (mindist < 50) {
/*      */       
/* 1215 */       toReturn = toReturn + "The " + name + " is some distance away " + direction + ". ";
/*      */     }
/* 1217 */     else if (mindist < 200) {
/*      */       
/* 1219 */       toReturn = toReturn + "The " + name + " is quite some distance away " + direction + ". ";
/*      */     }
/* 1221 */     else if (mindist < 500) {
/*      */       
/* 1223 */       toReturn = toReturn + "The " + name + " is rather a long distance away " + direction + ". ";
/*      */     }
/* 1225 */     else if (mindist < 1000) {
/*      */       
/* 1227 */       toReturn = toReturn + "The " + name + " is pretty far away " + direction + ". ";
/*      */     }
/* 1229 */     else if (mindist < 2000) {
/*      */       
/* 1231 */       toReturn = toReturn + "The " + name + " is far away " + direction + ". ";
/*      */     }
/*      */     else {
/*      */       
/* 1235 */       toReturn = toReturn + "The " + name + " is very far away " + direction + ". ";
/*      */     } 
/* 1237 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String locateRandomEndGameItem(Creature performer) {
/* 1242 */     return locateEndGameItem(-1, performer);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void relocateAllEndGameItems() {
/* 1247 */     for (EndGameItem eg : artifacts.values()) {
/*      */       
/* 1249 */       eg.setLastMoved(System.currentTimeMillis());
/* 1250 */       moveArtifact(eg.getItem());
/*      */     } 
/* 1252 */     for (EndGameItem altar : altars.values()) {
/*      */       
/* 1254 */       Items.destroyItem(altar.getItem().getWurmId());
/* 1255 */       altar.delete();
/*      */     } 
/* 1257 */     altars.clear();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\endgames\EndGameItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
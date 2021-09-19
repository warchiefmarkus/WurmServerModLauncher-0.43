/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MeshTile;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.highways.HighwayPos;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.FaithZone;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.Trap;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.io.IOException;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Random;
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
/*      */ 
/*      */ public final class TileRockBehaviour
/*      */   extends TileBehaviour
/*      */ {
/*   88 */   private static final Logger logger = Logger.getLogger(TileRockBehaviour.class.getName());
/*      */   
/*   90 */   static final Random rockRandom = new Random();
/*      */   
/*   92 */   private static final int worldSizeX = 1 << Constants.meshSize;
/*      */   private static final int mineZoneSize = 32;
/*   94 */   private static final int mineZoneDiv = worldSizeX / 32;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int minPrayingHeightDec = 400;
/*      */ 
/*      */ 
/*      */   
/*  103 */   private static final byte[][] minezones = new byte[mineZoneDiv + 1][mineZoneDiv + 1];
/*      */   
/*      */   static final long HUGE_PRIME = 789221L;
/*      */   
/*      */   static final long PROSPECT_PRIME = 181081L;
/*      */   
/*      */   public static final long SALT_PRIME = 102533L;
/*      */   public static final long SANDSTONE_PRIME = 123307L;
/*  111 */   static long SOURCE_PRIME = 786431L + Server.rand.nextInt(10000);
/*      */   
/*      */   public static final int saltFactor = 100;
/*      */   
/*      */   public static final int sandstoneFactor = 64;
/*      */   static final int flintFactor = 200;
/*  117 */   static int sourceFactor = 1000;
/*      */   
/*      */   static final long FLINT_PRIME = 6883L;
/*      */   
/*      */   static final int MIN_QL = 20;
/*  122 */   static int MAX_QL = 100;
/*      */ 
/*      */   
/*      */   static final int MAX_ROCK_QL = 100;
/*      */ 
/*      */   
/*      */   static final long EMERALD_PRIME = 66083L;
/*      */ 
/*      */   
/*      */   static final long OPAL_PRIME = 101333L;
/*      */ 
/*      */   
/*      */   static final long RUBY_PRIME = 812341L;
/*      */   
/*      */   static final long DIAMOND_PRIME = 104711L;
/*      */   
/*      */   static final long SAPPHIRE_PRIME = 781661L;
/*      */   
/*      */   private static final short CAVE_DESCENT_RATE = 20;
/*      */   
/*      */   static final int MAX_CEIL = 255;
/*      */   
/*      */   static final int DIG_CEIL = 30;
/*      */   
/*      */   public static final int MIN_CEIL = 5;
/*      */   
/*      */   static final int DIG_CEIL_REACH = 60;
/*      */   
/*      */   static final short MIN_CAVE_FLOOR = -25;
/*      */   
/*      */   static final short MAX_SLOPE_DOWN = -40;
/*      */   
/*      */   static final short MIN_ROCK_UNDERWATER = -25;
/*      */   
/*      */   public static final short CAVE_INIT_HEIGHT = -100;
/*      */   
/*      */   private static final int ORE_ZONE_FACTOR = 4;
/*      */   
/*  160 */   private static int oreRand = 0;
/*      */ 
/*      */   
/*      */   static {
/*  164 */     Random prand = new Random();
/*  165 */     prand.setSeed(181081L + Servers.getLocalServerId());
/*  166 */     Server.rand.setSeed(789221L);
/*  167 */     for (int x = 0; x <= mineZoneDiv; x++) {
/*      */       
/*  169 */       for (int y = 0; y <= mineZoneDiv; y++) {
/*      */         
/*  171 */         int num = Server.rand.nextInt(75);
/*  172 */         int prandnum = prand.nextInt(4);
/*  173 */         if (prandnum == 0) {
/*      */           
/*  175 */           minezones[x][y] = getOreId(num);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  187 */           minezones[x][y] = Tiles.Tile.TILE_CAVE_WALL.id;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   TileRockBehaviour() {
/*  194 */     super((short)9);
/*      */ 
/*      */ 
/*      */     
/*  198 */     sourceFactor = Servers.isThisAHomeServer() ? 100 : 50;
/*      */   }
/*      */ 
/*      */   
/*      */   TileRockBehaviour(short type) {
/*  203 */     super(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/*  210 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  211 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, tile));
/*  212 */     if (Tiles.decodeHeight(tile) > 400)
/*      */     {
/*  214 */       if (performer.getDeity() != null && performer.getDeity().isMountainGod())
/*      */       {
/*  216 */         Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]);
/*      */       }
/*      */     }
/*  219 */     if (performer.getCultist() != null && performer.getCultist().maySpawnVolcano()) {
/*      */       
/*  221 */       HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  222 */       if (highwaypos == null || !MethodsHighways.onHighway(highwaypos))
/*  223 */         toReturn.add(new ActionEntry((short)78, "Erupt", "erupting")); 
/*      */     } 
/*  225 */     toReturn.add(Actions.actionEntrys[642]);
/*  226 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile) {
/*  233 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  234 */     toReturn.addAll(super.getBehavioursFor(performer, subject, tilex, tiley, onSurface, tile));
/*  235 */     if (subject.isMiningtool()) {
/*      */       
/*  237 */       toReturn.add(new ActionEntry((short)-3, "Mining", "Mining options"));
/*      */       
/*  239 */       toReturn.add(Actions.actionEntrys[145]);
/*      */       
/*  241 */       toReturn.add(Actions.actionEntrys[156]);
/*      */       
/*  243 */       toReturn.add(Actions.actionEntrys[227]);
/*      */       
/*  245 */       if (performer.getPower() >= 4 && subject.getTemplateId() == 176) {
/*  246 */         toReturn.add(Actions.actionEntrys[518]);
/*      */       }
/*      */     }
/*  249 */     else if (subject.getTemplateId() == 782) {
/*      */       
/*  251 */       toReturn.add(Actions.actionEntrys[518]);
/*      */     } 
/*  253 */     if (Tiles.decodeHeight(tile) > 400)
/*      */     {
/*  255 */       if (performer.getDeity() != null && performer.getDeity().isMountainGod())
/*      */       {
/*  257 */         Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]);
/*      */       }
/*      */     }
/*  260 */     if ((performer.getCultist() != null && performer.getCultist().maySpawnVolcano()) || (subject
/*  261 */       .getTemplateId() == 176 && performer.getPower() >= 5)) {
/*      */       
/*  263 */       HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  264 */       if (highwaypos == null || !MethodsHighways.onHighway(highwaypos))
/*  265 */         toReturn.add(new ActionEntry((short)78, "Erupt", "erupting")); 
/*      */     } 
/*  267 */     toReturn.add(Actions.actionEntrys[642]);
/*  268 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, short action, float counter) {
/*  275 */     boolean done = true;
/*  276 */     if (action == 1) {
/*      */       
/*  278 */       Communicator comm = performer.getCommunicator();
/*  279 */       comm.sendNormalServerMessage("You see hard rock.");
/*  280 */       sendVillageString(performer, tilex, tiley, true);
/*  281 */       Trap t = Trap.getTrap(tilex, tiley, performer.getLayer());
/*  282 */       if (performer.getPower() > 3)
/*      */       {
/*  284 */         comm.sendNormalServerMessage("Your rot: " + Creature.normalizeAngle(performer.getStatus().getRotation()) + ", Wind rot=" + 
/*  285 */             Server.getWeather().getWindRotation() + ", pow=" + Server.getWeather().getWindPower() + " x=" + 
/*  286 */             Server.getWeather().getXWind() + ", y=" + Server.getWeather().getYWind());
/*  287 */         comm.sendNormalServerMessage("Tile is spring=" + Zone.hasSpring(tilex, tiley));
/*  288 */         if (performer.getPower() >= 5)
/*  289 */           comm.sendNormalServerMessage("tilex: " + tilex + ", tiley=" + tiley); 
/*  290 */         if (t != null)
/*      */         {
/*  292 */           String villageName = "none";
/*  293 */           if (t.getVillage() > 0) {
/*      */             
/*      */             try {
/*      */               
/*  297 */               villageName = Villages.getVillage(t.getVillage()).getName();
/*      */             }
/*  299 */             catch (NoSuchVillageException noSuchVillageException) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  304 */           comm.sendNormalServerMessage("A " + t.getName() + ", ql=" + t.getQualityLevel() + " kingdom=" + 
/*  305 */               Kingdoms.getNameFor(t.getKingdom()) + ", vill=" + villageName + ", rotdam=" + t.getRotDamage() + " firedam=" + t
/*  306 */               .getFireDamage() + " speed=" + t.getSpeedBon());
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  311 */       else if (t != null)
/*      */       {
/*  313 */         if (t.getKingdom() == performer.getKingdomId() || performer.getDetectDangerBonus() > 0.0F)
/*      */         {
/*  315 */           String qlString = "average";
/*  316 */           if (t.getQualityLevel() < 20) {
/*  317 */             qlString = "low";
/*  318 */           } else if (t.getQualityLevel() > 80) {
/*  319 */             qlString = "deadly";
/*  320 */           } else if (t.getQualityLevel() > 50) {
/*  321 */             qlString = "high";
/*  322 */           }  String villageName = ".";
/*  323 */           if (t.getVillage() > 0) {
/*      */             
/*      */             try {
/*      */               
/*  327 */               villageName = " of " + Villages.getVillage(t.getVillage()).getName() + ".";
/*      */             }
/*  329 */             catch (NoSuchVillageException noSuchVillageException) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  334 */           String rotDam = "";
/*  335 */           if (t.getRotDamage() > 0)
/*  336 */             rotDam = " It has ugly black-green speckles."; 
/*  337 */           String fireDam = "";
/*  338 */           if (t.getFireDamage() > 0)
/*  339 */             fireDam = " It has the rune of fire."; 
/*  340 */           StringBuilder buf = new StringBuilder();
/*  341 */           buf.append("You detect a ");
/*  342 */           buf.append(t.getName());
/*  343 */           buf.append(" here, of ");
/*  344 */           buf.append(qlString);
/*  345 */           buf.append(" quality.");
/*  346 */           buf.append(" It has been set by people from ");
/*  347 */           buf.append(Kingdoms.getNameFor(t.getKingdom()));
/*  348 */           buf.append(villageName);
/*  349 */           buf.append(rotDam);
/*  350 */           buf.append(fireDam);
/*  351 */           comm.sendNormalServerMessage(buf.toString());
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  356 */     } else if (action == 141) {
/*      */       
/*  358 */       if (Tiles.decodeHeight(tile) > 400 && 
/*  359 */         performer.getDeity() != null && performer.getDeity().isMountainGod()) {
/*  360 */         done = MethodsReligion.pray(act, performer, counter);
/*      */       }
/*  362 */     } else if (action == 78) {
/*      */       
/*  364 */       HighwayPos highwaypos = MethodsHighways.getHighwayPos(tilex, tiley, onSurface);
/*  365 */       if (highwaypos != null && MethodsHighways.onHighway(highwaypos)) {
/*  366 */         return true;
/*      */       }
/*      */       
/*  369 */       boolean cultistSpawn = (Methods.isActionAllowed(performer, (short)384) && performer.getCultist() != null && performer.getCultist().maySpawnVolcano());
/*  370 */       if (cultistSpawn || performer.getPower() >= 5) {
/*      */         
/*  372 */         if (cultistSpawn) {
/*      */           
/*  374 */           if (isHoleNear(tilex, tiley)) {
/*      */             
/*  376 */             performer.getCommunicator().sendNormalServerMessage("A cave entrance is too close.");
/*  377 */             return true;
/*      */           } 
/*  379 */           if (Zones.getKingdom(tilex, tiley) != performer.getKingdomId()) {
/*      */             
/*  381 */             performer.getCommunicator().sendNormalServerMessage("Nothing happens. Maybe you can not spawn lava too far from your own kingdom?");
/*      */             
/*  383 */             return true;
/*      */           } 
/*      */           
/*      */           try {
/*  387 */             FaithZone fz = Zones.getFaithZone(tilex, tiley, performer.isOnSurface());
/*  388 */             if (fz != null && fz.getCurrentRuler() != null && (fz.getCurrentRuler()).number != 2)
/*      */             {
/*  390 */               performer.getCommunicator().sendNormalServerMessage("Nothing happens. Maybe you can not spawn lava too far from Magranon's domain?");
/*      */               
/*  392 */               return true;
/*      */             }
/*      */           
/*  395 */           } catch (NoSuchZoneException nsz) {
/*      */             
/*  397 */             performer.getCommunicator().sendNormalServerMessage("Nothing happens. Maybe you can not spawn lava too far from Magranon's domain?");
/*      */             
/*  399 */             return true;
/*      */           } 
/*      */ 
/*      */           
/*  403 */           if (!Methods.isActionAllowed(performer, (short)547, tilex, tiley)) {
/*  404 */             return true;
/*      */           }
/*  406 */           done = false;
/*  407 */           if (counter == 1.0F) {
/*      */             
/*  409 */             int sx = Zones.safeTileX(tilex - 1);
/*  410 */             int sy = Zones.safeTileX(tiley - 1);
/*  411 */             int ey = Zones.safeTileX(tiley + 1);
/*  412 */             int ex = Zones.safeTileX(tilex + 1);
/*  413 */             for (int x = sx; x <= ex; x++) {
/*      */               
/*  415 */               for (int y = sy; y <= ey; y++) {
/*      */                 
/*  417 */                 VolaTile tt = Zones.getTileOrNull(x, y, onSurface);
/*  418 */                 if (tt != null) {
/*      */                   
/*  420 */                   Item[] its = tt.getItems();
/*  421 */                   for (Item i : its) {
/*      */                     
/*  423 */                     if (i.isNoTake()) {
/*      */                       
/*  425 */                       performer.getCommunicator().sendNormalServerMessage("The " + i
/*  426 */                           .getName() + " blocks your efforts.");
/*  427 */                       return true;
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/*  433 */             performer.getCommunicator().sendNormalServerMessage("You start concentrating on the rock.");
/*  434 */             Server.getInstance().broadCastAction(performer.getName() + " starts to look intensely on the rock.", performer, 5);
/*      */             
/*  436 */             if (cultistSpawn) {
/*  437 */               performer.sendActionControl("Erupting", true, 400);
/*      */             }
/*      */           } 
/*      */         } 
/*  441 */         if (!cultistSpawn || counter > 40.0F) {
/*      */           
/*  443 */           done = true;
/*  444 */           int caveTile = Server.caveMesh.getTile(tilex, tiley);
/*  445 */           byte type = Tiles.decodeType(caveTile);
/*      */ 
/*      */ 
/*      */           
/*  449 */           if (Tiles.isSolidCave(type) && !Tiles.getTile(type).isReinforcedCave()) {
/*      */             
/*  451 */             performer.getCommunicator().sendNormalServerMessage("The rock starts to bubble with lava.");
/*  452 */             Server.getInstance().broadCastAction(performer.getName() + " makes the rock boil with red hot lava.", performer, 5);
/*      */             
/*  454 */             int height = Tiles.decodeHeight(tile);
/*      */             
/*  456 */             TileEvent.log(tilex, tiley, 0, performer.getWurmId(), action);
/*  457 */             int nh = height + 4;
/*  458 */             if (cultistSpawn)
/*  459 */               performer.getCultist().touchCooldown2(); 
/*  460 */             Server.setSurfaceTile(tilex, tiley, (short)nh, Tiles.Tile.TILE_LAVA.id, (byte)0);
/*  461 */             for (int xx = 0; xx <= 1; xx++) {
/*      */               
/*  463 */               for (int yy = 0; yy <= 1; yy++) {
/*      */ 
/*      */                 
/*      */                 try {
/*  467 */                   int tempint3 = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + xx, tiley + yy));
/*  468 */                   Server.rockMesh.setTile(tilex + xx, tiley + yy, 
/*  469 */                       Tiles.encode((short)tempint3, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */                 }
/*  471 */                 catch (Exception exception) {}
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/*  477 */             Terraforming.setAsRock(tilex, tiley, false, true);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  482 */             performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/*  487 */       done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*      */     } 
/*  489 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/*  496 */     boolean done = true;
/*  497 */     if (action == 518 && (source.getTemplateId() == 782 || (performer
/*  498 */       .getPower() >= 4 && source.getTemplateId() == 176))) {
/*      */       
/*  500 */       int digTileX = (int)performer.getStatus().getPositionX() + 2 >> 2;
/*  501 */       int digTileY = (int)performer.getStatus().getPositionY() + 2 >> 2;
/*  502 */       done = CaveTileBehaviour.raiseRockLevel(performer, source, digTileX, digTileY, counter, act);
/*      */     }
/*  504 */     else if (source.isMiningtool() && action == 227) {
/*      */       
/*  506 */       if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize) {
/*      */         
/*  508 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to mine.", (byte)3);
/*  509 */         return true;
/*      */       } 
/*  511 */       if (Zones.isTileProtected(tilex, tiley)) {
/*      */         
/*  513 */         performer.getCommunicator().sendNormalServerMessage("This tile is protected by the gods. You can not mine here.", (byte)3);
/*      */         
/*  515 */         return true;
/*      */       } 
/*  517 */       short h = Tiles.decodeHeight(tile);
/*  518 */       if (h > -24) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  523 */         boolean makingWideTunnel = false;
/*  524 */         if (isHoleNear(tilex, tiley))
/*      */         {
/*  526 */           if (canHaveWideEntrance(performer, tilex, tiley)) {
/*      */             
/*  528 */             makingWideTunnel = true;
/*      */           }
/*      */           else {
/*      */             
/*  532 */             performer.getCommunicator().sendNormalServerMessage("Another tunnel is too close. It would collapse.");
/*  533 */             return true;
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*  538 */         Point lowestCorner = findLowestCorner(performer, tilex, tiley);
/*  539 */         if (lowestCorner == null) {
/*  540 */           return true;
/*      */         }
/*      */         
/*  543 */         Point nextLowestCorner = findNextLowestCorner(performer, tilex, tiley, lowestCorner);
/*  544 */         if (nextLowestCorner == null) {
/*  545 */           return true;
/*      */         }
/*  547 */         Point highestCorner = findHighestCorner(tilex, tiley);
/*  548 */         if (highestCorner == null) {
/*  549 */           return false;
/*      */         }
/*  551 */         Point nextHighestCorner = findNextHighestCorner(tilex, tiley, highestCorner);
/*  552 */         if (nextHighestCorner == null) {
/*  553 */           return false;
/*      */         }
/*      */         
/*  556 */         if ((nextLowestCorner.getH() != lowestCorner.getH() && 
/*  557 */           isStructureNear(nextLowestCorner.getX(), nextLowestCorner.getY())) || (nextHighestCorner
/*  558 */           .getH() != highestCorner.getH() && 
/*  559 */           isStructureNear(highestCorner.getX(), highestCorner.getY()))) {
/*      */           
/*  561 */           performer.getCommunicator().sendNormalServerMessage("Cannot create a tunnel here as there is a structure too close.", (byte)3);
/*      */           
/*  563 */           return true;
/*      */         } 
/*      */         
/*  566 */         for (int x = -1; x <= 1; x++) {
/*      */           
/*  568 */           for (int y = -1; y <= 1; y++) {
/*      */             
/*  570 */             VolaTile svt = Zones.getTileOrNull(tilex + x, tiley + y, true);
/*  571 */             Structure ss = (svt == null) ? null : svt.getStructure();
/*  572 */             if (ss != null && ss.isTypeBridge()) {
/*      */               
/*  574 */               performer.getCommunicator().sendNormalServerMessage("You can't tunnel here, there is a bridge in the way.");
/*  575 */               return true;
/*      */             } 
/*      */             
/*  578 */             VolaTile cvt = Zones.getTileOrNull(tilex + x, tiley + y, false);
/*  579 */             Structure cs = (cvt == null) ? null : cvt.getStructure();
/*  580 */             if (cs != null && cs.isTypeBridge()) {
/*      */               
/*  582 */               performer.getCommunicator().sendNormalServerMessage("You can't tunnel here, there is a bridge in the way.");
/*  583 */               return true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  588 */         done = false;
/*  589 */         Skills skills = performer.getSkills();
/*  590 */         Skill mining = null;
/*  591 */         Skill tool = null;
/*  592 */         boolean insta = (performer.getPower() >= 2 && source.isWand());
/*      */         
/*      */         try {
/*  595 */           mining = skills.getSkill(1008);
/*      */         }
/*  597 */         catch (Exception ex) {
/*      */           
/*  599 */           mining = skills.learn(1008, 1.0F);
/*      */         } 
/*      */         
/*      */         try {
/*  603 */           tool = skills.getSkill(source.getPrimarySkill());
/*      */         }
/*  605 */         catch (Exception ex) {
/*      */ 
/*      */           
/*      */           try {
/*  609 */             tool = skills.learn(source.getPrimarySkill(), 1.0F);
/*      */           }
/*  611 */           catch (NoSuchSkillException nse) {
/*      */             
/*  613 */             logger.log(Level.WARNING, performer.getName() + " trying to mine with an item with no primary skill: " + source
/*  614 */                 .getName());
/*      */           } 
/*      */         } 
/*  617 */         int time = 0;
/*  618 */         if (counter == 1.0F) {
/*      */           
/*  620 */           time = Actions.getStandardActionTime(performer, mining, source, 0.0D);
/*      */           
/*      */           try {
/*  623 */             performer.getCurrentAction().setTimeLeft(time);
/*      */           }
/*  625 */           catch (NoSuchActionException nsa) {
/*      */             
/*  627 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/*  629 */           if (affectsHighway(tilex, tiley)) {
/*      */             
/*  631 */             performer.getCommunicator().sendNormalServerMessage("A surface highway interferes with your tunneling operation.", (byte)3);
/*      */             
/*  633 */             return true;
/*      */           } 
/*      */           
/*  636 */           if (!isOutInTunnelOkay(performer, tilex, tiley, makingWideTunnel)) {
/*  637 */             return true;
/*      */           }
/*  639 */           Server.getInstance().broadCastAction(performer.getName() + " starts tunneling.", performer, 5);
/*  640 */           performer.getCommunicator().sendNormalServerMessage("You start to tunnel.");
/*  641 */           performer.sendActionControl(Actions.actionEntrys[227].getVerbString(), true, time);
/*      */           
/*  643 */           source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*  644 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */ 
/*      */             
/*  651 */             time = performer.getCurrentAction().getTimeLeft();
/*      */           }
/*  653 */           catch (NoSuchActionException nsa) {
/*      */             
/*  655 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/*  657 */           if (counter * 10.0F <= time && !insta) {
/*      */             
/*  659 */             if (act.currentSecond() % 5 == 0 || (act.currentSecond() == 3 && time < 50))
/*      */             {
/*  661 */               String sstring = "sound.work.mining1";
/*  662 */               int i = Server.rand.nextInt(3);
/*  663 */               if (i == 0) {
/*  664 */                 sstring = "sound.work.mining2";
/*  665 */               } else if (i == 1) {
/*  666 */                 sstring = "sound.work.mining3";
/*  667 */               }  SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 0.0F);
/*  668 */               source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*  669 */               performer.getStatus().modifyStamina(-7000.0F);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  674 */             if (act.getRarity() != 0)
/*      */             {
/*  676 */               performer.playPersonalSound("sound.fx.drumroll");
/*      */             }
/*  678 */             double bonus = 0.0D;
/*  679 */             double power = 0.0D;
/*  680 */             done = true;
/*  681 */             int itemTemplateCreated = 146;
/*  682 */             float diff = 1.0F;
/*  683 */             int mineDir = getTunnelExit(tilex, tiley);
/*  684 */             if (mineDir == -1) {
/*      */               
/*  686 */               performer.getCommunicator().sendNormalServerMessage("The topology here makes it impossible to mine in a good way.", (byte)3);
/*      */               
/*  688 */               return true;
/*      */             } 
/*  690 */             byte state = Zones.getMiningState(tilex, tiley);
/*  691 */             if (state == -1) {
/*      */               
/*  693 */               performer.getCommunicator().sendNormalServerMessage("You cannot keep mining here. The rock is unusually hard.", (byte)3);
/*      */               
/*  695 */               return true;
/*      */             } 
/*  697 */             if (affectsHighway(tilex, tiley)) {
/*      */               
/*  699 */               performer.getCommunicator().sendNormalServerMessage("A surface highway interferes with your tunneling operation.", (byte)3);
/*      */               
/*  701 */               return true;
/*      */             } 
/*  703 */             if (state >= Math.max(1, Servers.localServer.getTunnelingHits()) + Server.rand.nextInt(10) || insta) {
/*      */               
/*  705 */               int t = Server.caveMesh.getTile(tilex, tiley);
/*      */               
/*  707 */               if (Tiles.isReinforcedCave(Tiles.decodeType(t))) {
/*      */                 
/*  709 */                 performer.getCommunicator().sendNormalServerMessage("You cannot keep mining here. The rock is unusually hard.", (byte)3);
/*      */                 
/*  711 */                 return true;
/*      */               } 
/*  713 */               Zones.deleteMiningTile(tilex, tiley);
/*      */               
/*  715 */               if (areAllTilesRockOrReinforcedRock(tilex, tiley, tile, mineDir, true, makingWideTunnel)) {
/*      */                 
/*  717 */                 int drop = -20;
/*  718 */                 if (makingWideTunnel) {
/*      */ 
/*      */                   
/*  721 */                   MeshTile mTileCurrent = new MeshTile(Server.surfaceMesh, tilex, tiley);
/*  722 */                   MeshTile mCaveCurrent = new MeshTile(Server.caveMesh, tilex, tiley);
/*      */                   
/*  724 */                   MeshTile mTileNorth = mTileCurrent.getNorthMeshTile();
/*  725 */                   if (mTileNorth.isHole()) {
/*      */ 
/*      */                     
/*  728 */                     MeshTile mCaveNorth = mCaveCurrent.getNorthMeshTile();
/*  729 */                     drop = -Math.abs(mCaveNorth.getSouthSlope());
/*      */                   } 
/*  731 */                   MeshTile mTileWest = mTileCurrent.getWestMeshTile();
/*  732 */                   if (mTileWest.isHole()) {
/*      */ 
/*      */                     
/*  735 */                     MeshTile mCaveWest = mCaveCurrent.getWestMeshTile();
/*  736 */                     drop = -Math.abs(mCaveWest.getEastSlope());
/*      */                   } 
/*  738 */                   MeshTile mTileSouth = mTileCurrent.getSouthMeshTile();
/*  739 */                   if (mTileSouth.isHole()) {
/*      */ 
/*      */                     
/*  742 */                     MeshTile mCaveSouth = mCaveCurrent.getSouthMeshTile();
/*  743 */                     drop = -Math.abs(mCaveSouth.getNorthSlope());
/*      */                   } 
/*  745 */                   MeshTile mTileEast = mTileCurrent.getEastMeshTile();
/*  746 */                   if (mTileEast.isHole()) {
/*      */ 
/*      */                     
/*  749 */                     MeshTile mCaveEast = mCaveCurrent.getEastMeshTile();
/*  750 */                     drop = -Math.abs(mCaveEast.getWestSlope());
/*      */                   } 
/*      */                 } 
/*  753 */                 if (!createOutInTunnel(tilex, tiley, tile, performer, drop)) {
/*  754 */                   return true;
/*      */                 }
/*      */               } else {
/*      */                 
/*  758 */                 performer
/*  759 */                   .getCommunicator()
/*  760 */                   .sendNormalServerMessage("The ground sounds strangely hollow and brittle. You have to abandon the mining operation.", (byte)3);
/*      */                 
/*  762 */                 return true;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  767 */               if (!areAllTilesRockOrReinforcedRock(tilex, tiley, tile, mineDir, true, makingWideTunnel)) {
/*      */                 
/*  769 */                 performer
/*  770 */                   .getCommunicator()
/*  771 */                   .sendNormalServerMessage("The ground sounds strangely hollow and brittle. You have to abandon the mining operation.", (byte)3);
/*      */                 
/*  773 */                 return true;
/*      */               } 
/*  775 */               if (!isOutInTunnelOkay(performer, tilex, tiley, makingWideTunnel))
/*  776 */                 return true; 
/*      */             } 
/*  778 */             if (state > 10) {
/*      */               
/*  780 */               int t = Server.caveMesh.getTile(tilex, tiley);
/*  781 */               if (Tiles.isReinforcedCave(Tiles.decodeType(t))) {
/*      */                 
/*  783 */                 performer.getCommunicator().sendNormalServerMessage("You cannot keep mining here. The rock is unusually hard.", (byte)3);
/*      */                 
/*  785 */                 return true;
/*      */               } 
/*      */             } 
/*  788 */             if (state < 76) {
/*      */               
/*  790 */               state = (byte)(state + 1);
/*  791 */               Zones.setMiningState(tilex, tiley, state, false);
/*  792 */               if (state > Servers.localServer.getTunnelingHits()) {
/*  793 */                 performer.getCommunicator().sendNormalServerMessage("You will soon create an entrance.");
/*      */               }
/*      */             } 
/*  796 */             if (tool != null)
/*  797 */               bonus = tool.skillCheck(1.0D, source, 0.0D, false, counter) / 5.0D; 
/*  798 */             power = Math.max(1.0D, mining.skillCheck(1.0D, source, bonus, false, counter));
/*      */             
/*  800 */             if (performer.getTutorialLevel() == 10 && !performer.skippedTutorial())
/*      */             {
/*  802 */               performer.missionFinished(true, true);
/*      */             }
/*  804 */             if (Server.rand.nextInt(5) == 0) {
/*      */ 
/*      */               
/*      */               try {
/*  808 */                 if (mining.getKnowledge(0.0D) < power)
/*  809 */                   power = mining.getKnowledge(0.0D); 
/*  810 */                 rockRandom.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/*  811 */                 int m = 100;
/*      */                 
/*  813 */                 double imbueEnhancement = 1.0D + 0.23047D * source.getSkillSpellImprovement(1008) / 100.0D;
/*  814 */                 float modifier = 1.0F;
/*  815 */                 if (source.getSpellEffects() != null)
/*      */                 {
/*  817 */                   modifier *= source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */                 }
/*  819 */                 int max = (int)Math.min(100.0D, 20.0D + rockRandom
/*  820 */                     .nextInt(80) * imbueEnhancement * modifier + source.getRarity());
/*  821 */                 power = Math.min(power, max);
/*  822 */                 if (source.isCrude())
/*  823 */                   power = 1.0D; 
/*  824 */                 Item newItem = ItemFactory.createItem(146, (float)power, performer
/*  825 */                     .getPosX(), performer.getPosY(), Server.rand.nextFloat() * 360.0F, performer
/*  826 */                     .isOnSurface(), act.getRarity(), -10L, null);
/*  827 */                 newItem.setLastOwnerId(performer.getWurmId());
/*  828 */                 newItem.setDataXY(tilex, tiley);
/*      */                 
/*  830 */                 performer.getCommunicator().sendNormalServerMessage("You mine some " + newItem.getName() + ".");
/*  831 */                 Server.getInstance().broadCastAction(performer
/*  832 */                     .getName() + " mines some " + newItem.getName() + ".", performer, 5);
/*  833 */                 createGem(tilex, tiley, performer, power, true, act);
/*      */               }
/*  835 */               catch (Exception ex) {
/*      */                 
/*  837 */                 logger.log(Level.WARNING, "Factory failed to produce item", ex);
/*      */               } 
/*      */             } else {
/*      */               
/*  841 */               performer.getCommunicator().sendNormalServerMessage("You chip away at the rock.");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } else {
/*  846 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to mine.", (byte)3);
/*      */       } 
/*  848 */     } else if (source.isMiningtool() && action == 145) {
/*      */       
/*  850 */       int digTilex = (int)performer.getStatus().getPositionX() + 2 >> 2;
/*  851 */       int digTiley = (int)performer.getStatus().getPositionY() + 2 >> 2;
/*  852 */       done = mine(act, performer, source, tilex, tiley, action, counter, digTilex, digTiley);
/*      */     }
/*  854 */     else if (source.isMiningtool() && action == 156) {
/*      */       
/*  856 */       if (tilex < 0 || tilex > 1 << Constants.meshSize || tiley < 0 || tiley > 1 << Constants.meshSize) {
/*      */         
/*  858 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to prospect.", (byte)3);
/*  859 */         return true;
/*      */       } 
/*  861 */       float h = Tiles.decodeHeight(tile);
/*  862 */       if (h > -25.0F) {
/*      */         
/*  864 */         Skills skills = performer.getSkills();
/*  865 */         Skill prospecting = null;
/*  866 */         done = false;
/*      */         
/*      */         try {
/*  869 */           prospecting = skills.getSkill(10032);
/*      */         }
/*  871 */         catch (Exception ex) {
/*      */           
/*  873 */           prospecting = skills.learn(10032, 1.0F);
/*      */         } 
/*  875 */         int time = 0;
/*  876 */         if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  884 */           String sstring = "sound.work.prospecting1";
/*  885 */           int x = Server.rand.nextInt(3);
/*  886 */           if (x == 0) {
/*  887 */             sstring = "sound.work.prospecting2";
/*  888 */           } else if (x == 1) {
/*  889 */             sstring = "sound.work.prospecting3";
/*  890 */           }  SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 1.0F);
/*  891 */           time = (int)Math.max(30.0D, 100.0D - prospecting.getKnowledge(source, 0.0D));
/*      */           
/*      */           try {
/*  894 */             performer.getCurrentAction().setTimeLeft(time);
/*      */           }
/*  896 */           catch (NoSuchActionException nsa) {
/*      */             
/*  898 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/*  900 */           performer.getCommunicator().sendNormalServerMessage("You start to gather fragments of the rock.");
/*  901 */           Server.getInstance().broadCastAction(performer.getName() + " starts gathering fragments of the rock.", performer, 5);
/*      */           
/*  903 */           performer.sendActionControl(Actions.actionEntrys[156].getVerbString(), true, time);
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/*  909 */             time = performer.getCurrentAction().getTimeLeft();
/*      */           }
/*  911 */           catch (NoSuchActionException nsa) {
/*      */             
/*  913 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/*      */         } 
/*  916 */         if (counter * 10.0F > time) {
/*      */           
/*  918 */           performer.getStatus().modifyStamina(-3000.0F);
/*  919 */           prospecting.skillCheck(1.0D, source, 0.0D, false, counter);
/*  920 */           source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*  921 */           done = true;
/*  922 */           String findString = "only rock";
/*  923 */           LinkedList<String> list = new LinkedList<>();
/*  924 */           int m = 100;
/*  925 */           boolean saltExists = false;
/*  926 */           boolean flintExists = false;
/*      */           int x;
/*  928 */           for (x = -3; x <= 3; x++) {
/*      */             
/*  930 */             for (int y = -3; y <= 3; y++) {
/*      */               
/*  932 */               int resource = Server.getCaveResource(tilex + x, tiley + y);
/*  933 */               findString = "";
/*  934 */               if (resource == 65535) {
/*      */                 
/*  936 */                 resource = Server.rand.nextInt(10000);
/*  937 */                 Server.setCaveResource(tilex + x, tiley + y, resource);
/*      */               } 
/*  939 */               int itemTemplate = getItemTemplateForTile(Tiles.decodeType(Server.caveMesh.getTile(tilex + x, tiley + y)));
/*      */               
/*  941 */               if (itemTemplate != 146) {
/*      */                 
/*      */                 try {
/*      */                   
/*  945 */                   ItemTemplate t = ItemTemplateFactory.getInstance().getTemplate(itemTemplate);
/*  946 */                   String qlstring = "";
/*  947 */                   if (prospecting.getKnowledge(0.0D) > 20.0D) {
/*      */                     
/*  949 */                     rockRandom.setSeed((tilex + x + (tiley + y) * Zones.worldTileSizeY) * 789221L);
/*  950 */                     int max = Math.min(100, 20 + rockRandom.nextInt(80));
/*  951 */                     qlstring = " (" + getShardQlDescription(max) + ")";
/*      */                   } 
/*  953 */                   findString = t.getProspectName() + qlstring;
/*      */                 }
/*  955 */                 catch (NoSuchTemplateException nst) {
/*      */                   
/*  957 */                   logger.log(Level.WARNING, performer.getName() + " - " + nst.getMessage() + ": " + itemTemplate + " at " + tilex + ", " + tiley, (Throwable)nst);
/*      */                 } 
/*      */               }
/*      */               
/*  961 */               if (prospecting.getKnowledge(0.0D) > 40.0D) {
/*      */                 
/*  963 */                 rockRandom.setSeed((tilex + x + (tiley + y) * Zones.worldTileSizeY) * 102533L);
/*      */                 
/*  965 */                 if (rockRandom.nextInt(100) == 0)
/*  966 */                   saltExists = true; 
/*      */               } 
/*  968 */               if (prospecting.getKnowledge(0.0D) > 20.0D) {
/*      */                 
/*  970 */                 rockRandom.setSeed((tilex + x + (tiley + y) * Zones.worldTileSizeY) * 6883L);
/*      */                 
/*  972 */                 if (rockRandom.nextInt(200) == 0)
/*  973 */                   flintExists = true; 
/*      */               } 
/*  975 */               if (findString.length() > 0)
/*      */               {
/*  977 */                 if (!list.contains(findString))
/*      */                 {
/*  979 */                   if (Server.rand.nextBoolean()) {
/*  980 */                     list.addFirst(findString);
/*      */                   } else {
/*  982 */                     list.addLast(findString);
/*      */                   }  } 
/*      */               }
/*      */             } 
/*      */           } 
/*  987 */           if (list.isEmpty()) {
/*  988 */             findString = "only rock";
/*      */           } else {
/*      */             
/*  991 */             x = 0;
/*  992 */             for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
/*      */               
/*  994 */               if (x == 0) {
/*  995 */                 findString = it.next();
/*  996 */               } else if (x == list.size() - 1) {
/*      */                 
/*  998 */                 findString = findString + " and " + (String)it.next();
/*      */               } else {
/*      */                 
/* 1001 */                 findString = findString + ", " + (String)it.next();
/* 1002 */               }  x++;
/*      */             } 
/*      */           } 
/* 1005 */           performer.getCommunicator().sendNormalServerMessage("There is " + findString + " nearby.");
/* 1006 */           if (saltExists)
/* 1007 */             performer.getCommunicator().sendNormalServerMessage("You will find salt here!"); 
/* 1008 */           if (flintExists) {
/* 1009 */             performer.getCommunicator().sendNormalServerMessage("You will find flint here!");
/*      */           }
/*      */         } 
/*      */       } else {
/* 1013 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to prospect.");
/*      */       } 
/* 1015 */     } else if (action == 141 || action == 78) {
/*      */       
/* 1017 */       done = action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*      */     } else {
/*      */       
/* 1020 */       done = super.action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter);
/* 1021 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getTunnelExit(int tilex, int tiley) {
/* 1026 */     int lowestX = 100000;
/* 1027 */     int lowestY = 100000;
/* 1028 */     int nextLowestX = lowestX;
/* 1029 */     int nextLowestY = lowestY;
/* 1030 */     float lowestHeight = 100000.0F;
/* 1031 */     float nextLowestHeight = lowestHeight;
/* 1032 */     int sameX = lowestX;
/* 1033 */     int sameY = lowestY;
/* 1034 */     int lowerCount = 0;
/*      */     
/* 1036 */     for (int x = 0; x <= 1; x++) {
/*      */       
/* 1038 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 1040 */         int rockTile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 1041 */         short rockHeight = Tiles.decodeHeight(rockTile);
/*      */ 
/*      */ 
/*      */         
/* 1045 */         if (lowestHeight == 32767.0F) {
/*      */ 
/*      */           
/* 1048 */           lowestHeight = rockHeight;
/* 1049 */           lowestX = tilex + x;
/* 1050 */           lowestY = tiley + y;
/* 1051 */           lowerCount = 1;
/*      */         }
/* 1053 */         else if (rockHeight < lowestHeight) {
/*      */ 
/*      */           
/* 1056 */           lowestHeight = rockHeight;
/* 1057 */           lowestX = tilex + x;
/* 1058 */           lowestY = tiley + y;
/* 1059 */           lowerCount = 1;
/*      */         }
/* 1061 */         else if (rockHeight == lowestHeight) {
/*      */ 
/*      */           
/* 1064 */           sameX = tilex + x;
/* 1065 */           sameY = tiley + y;
/* 1066 */           lowerCount++;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1070 */     if (lowerCount > 2) {
/*      */ 
/*      */       
/* 1073 */       logger.log(Level.WARNING, "Bad tile at " + tilex + ", " + tiley);
/* 1074 */       return -1;
/*      */     } 
/*      */     
/* 1077 */     if (lowerCount == 2)
/*      */     {
/*      */       
/* 1080 */       if (sameX - lowestX != 0 && sameY - lowestY != 0) {
/*      */ 
/*      */         
/* 1083 */         logger.log(Level.WARNING, "Bad tile at " + tilex + ", " + tiley);
/* 1084 */         return -1;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1089 */     int nsY = tiley + 1 - lowestY - tiley;
/* 1090 */     int nsRockTile = Server.rockMesh.getTile(lowestX, nsY);
/* 1091 */     short nsRockHeight = Tiles.decodeHeight(nsRockTile);
/* 1092 */     nextLowestHeight = nsRockHeight;
/* 1093 */     nextLowestX = lowestX;
/* 1094 */     nextLowestY = nsY;
/*      */     
/* 1096 */     int weX = tilex + 1 - lowestX - tilex;
/* 1097 */     int weRockTile = Server.rockMesh.getTile(weX, lowestY);
/* 1098 */     short weRockHeight = Tiles.decodeHeight(weRockTile);
/* 1099 */     if (weRockHeight < nextLowestHeight) {
/*      */       
/* 1101 */       nextLowestHeight = weRockHeight;
/* 1102 */       nextLowestX = weX;
/* 1103 */       nextLowestY = lowestY;
/*      */     }
/* 1105 */     else if (weRockHeight == nextLowestHeight) {
/*      */ 
/*      */       
/* 1108 */       logger.log(Level.WARNING, "Bad tile at " + tilex + ", " + tiley);
/* 1109 */       return -1;
/*      */     } 
/*      */ 
/*      */     
/* 1113 */     if (lowestX == tilex + 0) {
/*      */       
/* 1115 */       if (lowestY == tiley + 0) {
/*      */         
/* 1117 */         if (nextLowestX == tilex + 1)
/*      */         {
/* 1119 */           if (nextLowestY == tiley + 0)
/*      */           {
/* 1121 */             return 3;
/*      */           }
/*      */         }
/* 1124 */         else if (nextLowestY == tiley + 1)
/*      */         {
/* 1126 */           return 2;
/*      */         }
/*      */       
/* 1129 */       } else if (lowestY == tiley + 1) {
/*      */         
/* 1131 */         if (nextLowestX == tilex + 1)
/*      */         {
/* 1133 */           if (nextLowestY == tiley + 1)
/*      */           {
/* 1135 */             return 5;
/*      */           }
/*      */         }
/* 1138 */         else if (nextLowestY == tiley + 0)
/*      */         {
/* 1140 */           return 2;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1146 */     else if (lowestY == tiley + 0) {
/*      */       
/* 1148 */       if (nextLowestX == tilex + 1)
/*      */       {
/* 1150 */         if (nextLowestY == tiley + 1)
/*      */         {
/* 1152 */           return 4;
/*      */         }
/*      */       }
/* 1155 */       else if (nextLowestY == tiley + 0)
/*      */       {
/* 1157 */         return 3;
/*      */       }
/*      */     
/* 1160 */     } else if (lowestY == tiley + 1) {
/*      */       
/* 1162 */       if (nextLowestX == tilex + 1) {
/*      */         
/* 1164 */         if (nextLowestY == tiley + 0)
/*      */         {
/* 1166 */           return 4;
/*      */         }
/*      */       }
/* 1169 */       else if (nextLowestY == tiley + 1) {
/*      */         
/* 1171 */         return 5;
/*      */       } 
/*      */     } 
/*      */     
/* 1175 */     logger.log(Level.WARNING, "Bad tile at " + tilex + ", " + tiley);
/* 1176 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setTileToTransition(int tilex, int tiley) {
/* 1181 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, true);
/* 1182 */     if (t != null)
/* 1183 */       t.isTransition = true; 
/* 1184 */     t = Zones.getTileOrNull(tilex, tiley, false);
/* 1185 */     if (t != null) {
/* 1186 */       t.isTransition = true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isOutInTunnelOkay(Creature performer, int tilex, int tiley, boolean makingWideTunnel) {
/* 1198 */     for (int x = -1; x <= 1; x++) {
/*      */       
/* 1200 */       for (int y = -1; y <= 1; y++) {
/*      */         
/* 1202 */         int tileNew = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/*      */         
/* 1204 */         if (Tiles.decodeType(tileNew) == Tiles.Tile.TILE_HOLE.id && !makingWideTunnel) {
/*      */           
/* 1206 */           performer.getCommunicator().sendNormalServerMessage("Another tunnel is too close. It would collapse.", (byte)3);
/*      */           
/* 1208 */           return false;
/*      */         } 
/* 1210 */         if (Tiles.isMineDoor(Tiles.decodeType(tileNew))) {
/*      */           
/* 1212 */           performer.getCommunicator().sendNormalServerMessage("Cannot make a tunnel next to a mine door.");
/* 1213 */           return false;
/*      */         } 
/* 1215 */         if (x >= 0 && y >= 0) {
/*      */           
/* 1217 */           int rockTile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 1218 */           short rockHeight = Tiles.decodeHeight(rockTile);
/* 1219 */           int caveTile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 1220 */           short cheight = Tiles.decodeHeight(caveTile);
/*      */           
/* 1222 */           if (!isNullWall(caveTile))
/*      */           {
/* 1224 */             if (rockHeight - cheight >= 255) {
/*      */               
/* 1226 */               performer.getCommunicator().sendNormalServerMessage("Not enough rock height to make a tunnel there.");
/* 1227 */               return false;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1233 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isHoleNear(int tilex, int tiley) {
/* 1241 */     MeshIO surfMesh = Server.surfaceMesh;
/*      */ 
/*      */     
/* 1244 */     for (int x = -1; x <= 1; x++) {
/*      */       
/* 1246 */       for (int y = -1; y <= 1; y++) {
/*      */         
/* 1248 */         int tileNew = surfMesh.getTile(tilex + x, tiley + y);
/* 1249 */         if (Tiles.decodeType(tileNew) == Tiles.Tile.TILE_HOLE.id)
/*      */         {
/* 1251 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 1255 */     return false;
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
/*      */   static boolean canHaveWideEntrance(@Nullable Creature performer, int tilex, int tiley) {
/* 1268 */     MeshIO surfMesh = Server.surfaceMesh;
/* 1269 */     if (!hasValidNearbyEntrance(performer, surfMesh, tilex, tiley)) {
/* 1270 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1276 */     MeshTile currentMT = new MeshTile(surfMesh, tilex, tiley);
/* 1277 */     MeshTile mTileNorth = currentMT.getNorthMeshTile();
/* 1278 */     if (mTileNorth.isHole()) {
/*      */ 
/*      */       
/* 1281 */       int dir = mTileNorth.getLowerLip();
/* 1282 */       if (dir == 6) {
/*      */ 
/*      */ 
/*      */         
/* 1286 */         if (currentMT.getWestSlope() != 0) {
/*      */           
/* 1288 */           if (performer != null)
/* 1289 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1290 */           return false;
/*      */         } 
/* 1292 */         if (currentMT.getSouthSlope() <= 0) {
/*      */           
/* 1294 */           if (performer != null)
/* 1295 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1296 */           return false;
/*      */         } 
/* 1298 */         return true;
/*      */       } 
/* 1300 */       if (dir == 2) {
/*      */ 
/*      */ 
/*      */         
/* 1304 */         if (currentMT.getEastSlope() != 0) {
/*      */           
/* 1306 */           if (performer != null)
/* 1307 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1308 */           return false;
/*      */         } 
/* 1310 */         if (currentMT.getSouthSlope() >= 0) {
/*      */           
/* 1312 */           if (performer != null)
/* 1313 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1314 */           return false;
/*      */         } 
/* 1316 */         return true;
/*      */       } 
/*      */     } 
/* 1319 */     MeshTile mTileWest = currentMT.getWestMeshTile();
/* 1320 */     if (mTileWest.isHole()) {
/*      */ 
/*      */       
/* 1323 */       int dir = mTileWest.getLowerLip();
/* 1324 */       if (dir == 0) {
/*      */ 
/*      */ 
/*      */         
/* 1328 */         if (currentMT.getNorthSlope() != 0) {
/*      */           
/* 1330 */           if (performer != null)
/* 1331 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1332 */           return false;
/*      */         } 
/* 1334 */         if (currentMT.getEastSlope() <= 0) {
/*      */           
/* 1336 */           if (performer != null)
/* 1337 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1338 */           return false;
/*      */         } 
/* 1340 */         return true;
/*      */       } 
/* 1342 */       if (dir == 4) {
/*      */ 
/*      */ 
/*      */         
/* 1346 */         if (currentMT.getSouthSlope() != 0) {
/*      */           
/* 1348 */           if (performer != null)
/* 1349 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1350 */           return false;
/*      */         } 
/* 1352 */         if (currentMT.getEastSlope() >= 0) {
/*      */           
/* 1354 */           if (performer != null)
/* 1355 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1356 */           return false;
/*      */         } 
/* 1358 */         return true;
/*      */       } 
/*      */     } 
/* 1361 */     MeshTile mTileSouth = currentMT.getSouthMeshTile();
/* 1362 */     if (mTileSouth.isHole()) {
/*      */ 
/*      */       
/* 1365 */       int dir = mTileSouth.getLowerLip();
/* 1366 */       if (dir == 6) {
/*      */ 
/*      */ 
/*      */         
/* 1370 */         if (currentMT.getWestSlope() != 0) {
/*      */           
/* 1372 */           if (performer != null)
/* 1373 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1374 */           return false;
/*      */         } 
/* 1376 */         if (currentMT.getNorthSlope() <= 0) {
/*      */           
/* 1378 */           if (performer != null)
/* 1379 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1380 */           return false;
/*      */         } 
/* 1382 */         return true;
/*      */       } 
/* 1384 */       if (dir == 2) {
/*      */ 
/*      */ 
/*      */         
/* 1388 */         if (currentMT.getEastSlope() != 0) {
/*      */           
/* 1390 */           if (performer != null)
/* 1391 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1392 */           return false;
/*      */         } 
/* 1394 */         if (currentMT.getNorthSlope() >= 0) {
/*      */           
/* 1396 */           if (performer != null)
/* 1397 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1398 */           return false;
/*      */         } 
/* 1400 */         return true;
/*      */       } 
/*      */     } 
/* 1403 */     MeshTile mTileEast = currentMT.getEastMeshTile();
/* 1404 */     if (mTileEast.isHole()) {
/*      */ 
/*      */       
/* 1407 */       int dir = mTileEast.getLowerLip();
/* 1408 */       if (dir == 0) {
/*      */ 
/*      */ 
/*      */         
/* 1412 */         if (currentMT.getNorthSlope() != 0) {
/*      */           
/* 1414 */           if (performer != null)
/* 1415 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1416 */           return false;
/*      */         } 
/* 1418 */         if (currentMT.getWestSlope() <= 0) {
/*      */           
/* 1420 */           if (performer != null)
/* 1421 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1422 */           return false;
/*      */         } 
/* 1424 */         return true;
/*      */       } 
/* 1426 */       if (dir == 4) {
/*      */ 
/*      */ 
/*      */         
/* 1430 */         if (currentMT.getSouthSlope() != 0) {
/*      */           
/* 1432 */           if (performer != null)
/* 1433 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs a flat border to correspond to lower part of adjacent cave entrance."); 
/* 1434 */           return false;
/*      */         } 
/* 1436 */         if (currentMT.getWestSlope() >= 0) {
/*      */           
/* 1438 */           if (performer != null)
/* 1439 */             performer.getCommunicator().sendNormalServerMessage("Current tile needs to be same orientation as adjacent cave entrance."); 
/* 1440 */           return false;
/*      */         } 
/* 1442 */         return true;
/*      */       } 
/*      */     } 
/* 1445 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean hasValidNearbyEntrance(@Nullable Creature performer, MeshIO surfMesh, int tilex, int tiley) {
/* 1450 */     int holeX = -1;
/* 1451 */     int holeY = -1;
/* 1452 */     int holeXX = -1;
/* 1453 */     int holeYY = -1;
/*      */     int x;
/* 1455 */     for (x = -1; x <= 1; x++) {
/*      */       
/* 1457 */       for (int y = -1; y <= 1; y++) {
/*      */ 
/*      */         
/* 1460 */         if (x != 0 && y != 0) {
/*      */           
/* 1462 */           int tileNew = surfMesh.getTile(tilex + x, tiley + y);
/* 1463 */           byte type = Tiles.decodeType(tileNew);
/* 1464 */           if (type == Tiles.Tile.TILE_HOLE.id) {
/*      */             
/* 1466 */             if (performer != null)
/* 1467 */               performer.getCommunicator().sendNormalServerMessage("Cannot have cave entrances meeting diagonally."); 
/* 1468 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1474 */     for (x = -1; x <= 1; x++) {
/*      */       
/* 1476 */       for (int y = -1; y <= 1; y++) {
/*      */ 
/*      */         
/* 1479 */         if (x != 0 || y != 0) {
/*      */           
/* 1481 */           int tileNew = surfMesh.getTile(tilex + x, tiley + y);
/* 1482 */           byte type = Tiles.decodeType(tileNew);
/* 1483 */           if (Tiles.isMineDoor(type)) {
/*      */             
/* 1485 */             if (performer != null)
/* 1486 */               performer.getCommunicator().sendNormalServerMessage("Cannot make a tunnel next to a mine door."); 
/* 1487 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1493 */     for (x = -1; x <= 1; x++) {
/*      */       
/* 1495 */       for (int y = -1; y <= 1; y++) {
/*      */ 
/*      */         
/* 1498 */         if (x != 0 || y != 0)
/*      */         {
/*      */           
/* 1501 */           if (x == 0 || y == 0) {
/*      */             
/* 1503 */             int tileNew = surfMesh.getTile(tilex + x, tiley + y);
/* 1504 */             if (Tiles.decodeType(tileNew) == Tiles.Tile.TILE_HOLE.id) {
/*      */ 
/*      */               
/* 1507 */               if (holeX != -1) {
/*      */ 
/*      */                 
/* 1510 */                 if (performer != null)
/* 1511 */                   performer.getCommunicator().sendNormalServerMessage("Can only make two or three tile wide cave entrances ."); 
/* 1512 */                 return false;
/*      */               } 
/* 1514 */               holeX = tilex + x;
/* 1515 */               holeY = tiley + y;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1522 */     if (holeX == -1) {
/* 1523 */       return true;
/*      */     }
/* 1525 */     for (int xx = -1; xx <= 1; xx++) {
/*      */       
/* 1527 */       for (int yy = -1; yy <= 1; yy++) {
/*      */ 
/*      */         
/* 1530 */         if (xx != 0 || yy != 0) {
/*      */           
/* 1532 */           int tileTwo = surfMesh.getTile(holeX + xx, holeY + yy);
/* 1533 */           if (Tiles.decodeType(tileTwo) == Tiles.Tile.TILE_HOLE.id) {
/*      */             
/* 1535 */             if (holeXX != -1) {
/*      */ 
/*      */               
/* 1538 */               if (performer != null)
/* 1539 */                 performer.getCommunicator().sendNormalServerMessage("Can only make two or three tile wide cave entrances ."); 
/* 1540 */               return false;
/*      */             } 
/* 1542 */             holeXX = holeX + xx;
/* 1543 */             holeYY = holeY + yy;
/*      */ 
/*      */             
/* 1546 */             if (tilex + xx + xx != holeXX || tiley + yy + yy != holeYY) {
/*      */               
/* 1548 */               if (performer != null)
/* 1549 */                 performer.getCommunicator().sendNormalServerMessage("Can only make two or three tile wide cave entrances ."); 
/* 1550 */               return false;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1557 */     if (holeXX == -1) {
/* 1558 */       return true;
/*      */     }
/* 1560 */     for (int xxx = -1; xxx <= 1; xxx++) {
/*      */       
/* 1562 */       for (int yyy = -1; yyy <= 1; yyy++) {
/*      */ 
/*      */         
/* 1565 */         if (xxx != 0 || yyy != 0) {
/*      */           
/* 1567 */           int tileThree = surfMesh.getTile(holeXX + xxx, holeYY + yyy);
/* 1568 */           if (Tiles.decodeType(tileThree) == Tiles.Tile.TILE_HOLE.id)
/*      */           {
/*      */ 
/*      */             
/* 1572 */             if (holeXX + xxx != holeX || holeYY + yyy != holeY) {
/*      */               
/* 1574 */               if (performer != null)
/* 1575 */                 performer.getCommunicator().sendNormalServerMessage("Can only make two or three tile wide cave entrances ."); 
/* 1576 */               return false;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1583 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isStructureNear(int tilex, int tiley) {
/* 1589 */     for (int x = -1; x <= 0; x++) {
/*      */       
/* 1591 */       for (int y = -1; y <= 0; y++) {
/*      */         
/* 1593 */         VolaTile vt = Zones.getTileOrNull(tilex + x, tiley + y, true);
/* 1594 */         if (vt != null && vt.getStructure() != null) {
/* 1595 */           return true;
/*      */         }
/* 1597 */         VolaTile vtc = Zones.getTileOrNull(tilex + x, tiley + y, false);
/* 1598 */         if (vtc != null && vtc.getStructure() != null)
/* 1599 */           return true; 
/*      */       } 
/*      */     } 
/* 1602 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean createOutInTunnel(int tilex, int tiley, int tile, Creature performer, int mod) {
/* 1607 */     MeshIO surfmesh = Server.surfaceMesh;
/* 1608 */     MeshIO cavemesh = Server.caveMesh;
/* 1609 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, true);
/* 1610 */     if (t != null) {
/*      */       
/* 1612 */       Item[] items = t.getItems();
/* 1613 */       for (Item lItem : items) {
/*      */         
/* 1615 */         if (lItem.isDecoration()) {
/*      */           
/* 1617 */           performer.getCommunicator().sendNormalServerMessage(
/* 1618 */               LoginHandler.raiseFirstLetter(lItem.getNameWithGenus()) + " on the surface disturbs your operation.");
/*      */           
/* 1620 */           return false;
/*      */         } 
/*      */       } 
/*      */       
/* 1624 */       if (t.getStructure() != null) {
/*      */         
/* 1626 */         performer.getCommunicator().sendNormalServerMessage("You can't tunnel here, there is a structure in the way.");
/* 1627 */         return false;
/*      */       } 
/*      */     } 
/* 1630 */     boolean makingWideTunnel = false;
/* 1631 */     if (isHoleNear(tilex, tiley))
/*      */     {
/* 1633 */       if (canHaveWideEntrance(performer, tilex, tiley)) {
/*      */         
/* 1635 */         makingWideTunnel = true;
/*      */       }
/*      */       else {
/*      */         
/* 1639 */         performer.getCommunicator().sendNormalServerMessage("Another tunnel is too close. It would collapse.");
/* 1640 */         return false;
/*      */       } 
/*      */     }
/* 1643 */     if (affectsHighway(tilex, tiley)) {
/*      */       
/* 1645 */       performer.getCommunicator().sendNormalServerMessage("A surface highway interferes with your tunneling operation.", (byte)3);
/*      */       
/* 1647 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1651 */     Point lowestCorner = findLowestCorner(performer, tilex, tiley);
/* 1652 */     if (lowestCorner == null) {
/* 1653 */       return false;
/*      */     }
/*      */     
/* 1656 */     Point nextLowestCorner = findNextLowestCorner(performer, tilex, tiley, lowestCorner);
/* 1657 */     if (nextLowestCorner == null) {
/* 1658 */       return false;
/*      */     }
/* 1660 */     Point highestCorner = findHighestCorner(tilex, tiley);
/* 1661 */     if (highestCorner == null) {
/* 1662 */       return false;
/*      */     }
/* 1664 */     Point nextHighestCorner = findNextHighestCorner(tilex, tiley, highestCorner);
/* 1665 */     if (nextHighestCorner == null) {
/* 1666 */       return false;
/*      */     }
/*      */     
/* 1669 */     if ((nextLowestCorner.getH() != lowestCorner.getH() && 
/* 1670 */       isStructureNear(nextLowestCorner.getX(), nextLowestCorner.getY())) || (nextHighestCorner
/* 1671 */       .getH() != highestCorner.getH() && 
/* 1672 */       isStructureNear(highestCorner.getX(), highestCorner.getY()))) {
/*      */       
/* 1674 */       performer.getCommunicator().sendNormalServerMessage("Cannot create a tunnel here as there is a structure too close.", (byte)3);
/*      */       
/* 1676 */       return false;
/*      */     } 
/*      */     
/* 1679 */     for (int x = -1; x <= 1; x++) {
/*      */       
/* 1681 */       for (int y = -1; y <= 1; y++) {
/*      */         
/* 1683 */         VolaTile svt = Zones.getTileOrNull(tilex + x, tiley + y, true);
/* 1684 */         Structure ss = (svt == null) ? null : svt.getStructure();
/* 1685 */         if (ss != null && ss.isTypeBridge()) {
/*      */           
/* 1687 */           performer.getCommunicator().sendNormalServerMessage("You can't tunnel here, there is a bridge in the way.");
/* 1688 */           return false;
/*      */         } 
/*      */         
/* 1691 */         VolaTile cvt = Zones.getTileOrNull(tilex + x, tiley + y, false);
/* 1692 */         Structure cs = (cvt == null) ? null : cvt.getStructure();
/* 1693 */         if (cs != null && cs.isTypeBridge()) {
/*      */           
/* 1695 */           performer.getCommunicator().sendNormalServerMessage("You can't tunnel here, there is a bridge in the way.");
/* 1696 */           return false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1702 */     int nsY = tiley + 1 - nextLowestCorner.getY() - tiley;
/* 1703 */     int weX = tilex + 1 - nextLowestCorner.getX() - tilex;
/*      */     
/* 1705 */     int nsCorner = surfmesh.getTile(nextLowestCorner.getX(), nsY);
/* 1706 */     if (!mayLowerCornerOnSlope(lowestCorner.getH(), performer, nsCorner)) {
/* 1707 */       return false;
/*      */     }
/* 1709 */     int weCorner = surfmesh.getTile(weX, nextLowestCorner.getY());
/* 1710 */     if (!mayLowerCornerOnSlope(lowestCorner.getH(), performer, weCorner))
/* 1711 */       return false; 
/* 1712 */     if (Tiles.isReinforcedCave(Tiles.decodeType(cavemesh.getTile(tilex, tiley)))) {
/* 1713 */       return false;
/*      */     }
/* 1715 */     if (makingWideTunnel) {
/* 1716 */       performer.getCommunicator().sendNormalServerMessage("You expand a tunnel entrance!");
/*      */     } else {
/* 1718 */       performer.getCommunicator().sendNormalServerMessage("You create a tunnel entrance!");
/* 1719 */     }  short targetHeight = (short)lowestCorner.getH();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1726 */     for (int i = tilex; i <= tilex + 1; i++) {
/*      */       
/* 1728 */       for (int y = tiley; y <= tiley + 1; y++) {
/*      */         
/* 1730 */         int tileNew = cavemesh.getTile(i, y);
/* 1731 */         int rockTile = Server.rockMesh.getTile(i, y);
/* 1732 */         short rockHeight = Tiles.decodeHeight(rockTile);
/* 1733 */         int surfTile = Server.surfaceMesh.getTile(i, y);
/* 1734 */         short surfHeight = Tiles.decodeHeight(surfTile);
/* 1735 */         if (i == tilex && y == tiley) {
/*      */ 
/*      */           
/* 1738 */           if ((i == lowestCorner.getX() && y == lowestCorner.getY()) || (i == nextLowestCorner
/* 1739 */             .getX() && y == nextLowestCorner.getY()))
/*      */           {
/*      */             
/* 1742 */             int[] newfloorceil = getFloorAndCeiling(i, y, targetHeight, 0, true, false, performer);
/* 1743 */             int newFloorHeight = newfloorceil[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1749 */             cavemesh.setTile(i, y, Tiles.encode((short)newFloorHeight, Tiles.Tile.TILE_CAVE_EXIT.id, (byte)0));
/* 1750 */             VolaTile surft = Zones.getTileOrNull(i, y, true);
/* 1751 */             if (surft != null)
/* 1752 */               surft.isTransition = true; 
/* 1753 */             VolaTile cavet = Zones.getTileOrNull(i, y, false);
/* 1754 */             if (cavet != null)
/* 1755 */               cavet.isTransition = true; 
/* 1756 */             if (rockHeight != newFloorHeight || surfHeight != newFloorHeight)
/*      */             {
/* 1758 */               Server.rockMesh.setTile(i, y, Tiles.encode((short)newFloorHeight, (short)0));
/* 1759 */               surfmesh.setTile(i, y, Tiles.encode((short)newFloorHeight, Tiles.decodeTileData(surfTile)));
/* 1760 */               Players.getInstance().sendChangedTile(i, y, true, true);
/*      */             }
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 1766 */             int[] newfloorceil = getFloorAndCeiling(i, y, targetHeight, mod, false, true, performer);
/* 1767 */             int newFloorHeight = newfloorceil[0];
/* 1768 */             int newCeil = newfloorceil[1];
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1773 */             if (Tiles.decodeType(tileNew) == Tiles.Tile.TILE_CAVE_WALL.id || 
/* 1774 */               Tiles.decodeType(tileNew) == Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1780 */               cavemesh.setTile(i, y, Tiles.encode((short)newFloorHeight, Tiles.Tile.TILE_CAVE_EXIT.id, (byte)(newCeil - newFloorHeight)));
/*      */               
/* 1782 */               VolaTile surft = Zones.getTileOrNull(i, y, true);
/* 1783 */               if (surft != null)
/* 1784 */                 surft.isTransition = true; 
/* 1785 */               VolaTile cavet = Zones.getTileOrNull(i, y, false);
/* 1786 */               if (cavet != null) {
/* 1787 */                 cavet.isTransition = true;
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */             else
/*      */             {
/*      */               
/* 1795 */               cavemesh.setTile(i, y, Tiles.encode((short)newFloorHeight, 
/* 1796 */                     Tiles.decodeType(tileNew), (byte)(newCeil - newFloorHeight)));
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 1803 */         else if ((i == lowestCorner.getX() && y == lowestCorner.getY()) || (i == nextLowestCorner
/* 1804 */           .getX() && y == nextLowestCorner.getY())) {
/*      */ 
/*      */           
/* 1807 */           int[] newfloorceil = getFloorAndCeiling(i, y, targetHeight, 0, true, false, performer);
/* 1808 */           int newFloorHeight = newfloorceil[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1814 */           cavemesh.setTile(i, y, Tiles.encode((short)newFloorHeight, Tiles.decodeType(tileNew), (byte)0));
/* 1815 */           if (rockHeight != newFloorHeight || surfHeight != newFloorHeight)
/*      */           {
/* 1817 */             Server.rockMesh.setTile(i, y, Tiles.encode((short)newFloorHeight, (short)0));
/* 1818 */             surfmesh.setTile(i, y, Tiles.encode((short)newFloorHeight, Tiles.decodeTileData(surfTile)));
/* 1819 */             Players.getInstance().sendChangedTile(i, y, true, true);
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 1825 */           int[] newfloorceil = getFloorAndCeiling(i, y, targetHeight, mod, false, true, performer);
/* 1826 */           int newFloorHeight = newfloorceil[0];
/* 1827 */           int newCeil = newfloorceil[1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1833 */           cavemesh.setTile(i, y, Tiles.encode((short)newFloorHeight, 
/* 1834 */                 Tiles.decodeType(tileNew), (byte)(newCeil - newFloorHeight)));
/*      */         } 
/*      */ 
/*      */         
/* 1838 */         Players.getInstance().sendChangedTile(i, y, false, true);
/*      */ 
/*      */         
/* 1841 */         for (int xx = -1; xx <= 0; xx++) {
/*      */           
/* 1843 */           for (int yy = -1; yy <= 0; yy++) {
/*      */ 
/*      */             
/*      */             try {
/* 1847 */               Zone toCheckForChange = Zones.getZone(i + xx, y + yy, false);
/* 1848 */               toCheckForChange.changeTile(i + xx, y + yy);
/*      */             }
/* 1850 */             catch (NoSuchZoneException nsz) {
/*      */               
/* 1852 */               logger.log(Level.INFO, "no such zone?: " + (i + xx) + ", " + (y + yy), (Throwable)nsz);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1858 */     setTileToTransition(tilex, tiley);
/* 1859 */     tile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1860 */     surfmesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_HOLE.id, Tiles.decodeData(tile)));
/*      */ 
/*      */ 
/*      */     
/* 1864 */     short targetUpperHeight = (short)nextHighestCorner.getH();
/*      */     
/* 1866 */     short tileData = Tiles.decodeTileData(Server.surfaceMesh.getTile(highestCorner.getX(), highestCorner.getY()));
/* 1867 */     Server.surfaceMesh.setTile(highestCorner.getX(), highestCorner.getY(), 
/* 1868 */         Tiles.encode(targetUpperHeight, tileData));
/* 1869 */     tileData = Tiles.decodeTileData(Server.rockMesh.getTile(highestCorner.getX(), highestCorner.getY()));
/* 1870 */     Server.rockMesh.setTile(highestCorner.getX(), highestCorner.getY(), 
/* 1871 */         Tiles.encode(targetUpperHeight, tileData));
/* 1872 */     tileData = Tiles.decodeTileData(Server.caveMesh.getTile(highestCorner.getX(), highestCorner.getY()));
/*      */ 
/*      */ 
/*      */     
/* 1876 */     Players.getInstance().sendChangedTile(highestCorner.getX(), highestCorner.getY(), true, true);
/* 1877 */     Players.getInstance().sendChangedTile(highestCorner.getX(), highestCorner.getY(), false, true);
/*      */     
/* 1879 */     Players.getInstance().sendChangedTile(tilex, tiley, true, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1908 */     VolaTile to = Zones.getOrCreateTile(tilex, tiley, true);
/* 1909 */     to.checkCaveOpening();
/* 1910 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Point findLowestCorner(Creature performer, int tilex, int tiley) {
/* 1922 */     int lowestX = 100000;
/* 1923 */     int lowestY = 100000;
/* 1924 */     short lowestHeight = Short.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1929 */     for (int x = 0; x <= 1; x++) {
/*      */       
/* 1931 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 1933 */         int rockTile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 1934 */         short rockHeight = Tiles.decodeHeight(rockTile);
/* 1935 */         int caveTile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 1936 */         short cheight = Tiles.decodeHeight(caveTile);
/*      */         
/* 1938 */         if (!isNullWall(caveTile))
/*      */         {
/* 1940 */           if (rockHeight - cheight >= 255) {
/*      */ 
/*      */ 
/*      */             
/* 1944 */             performer.getCommunicator().sendNormalServerMessage("The mountainside would risk crumbling. You cannot tunnel here.");
/*      */             
/* 1946 */             return null;
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1952 */         if (lowestHeight == Short.MAX_VALUE) {
/*      */ 
/*      */           
/* 1955 */           lowestHeight = rockHeight;
/* 1956 */           lowestX = tilex + x;
/* 1957 */           lowestY = tiley + y;
/*      */ 
/*      */         
/*      */         }
/* 1961 */         else if (rockHeight < lowestHeight) {
/*      */           
/* 1963 */           lowestHeight = rockHeight;
/* 1964 */           lowestX = tilex + x;
/* 1965 */           lowestY = tiley + y;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1970 */     return new Point(lowestX, lowestY, lowestHeight);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Point findNextLowestCorner(Creature performer, int tilex, int tiley, Point lowestCorner) {
/* 1977 */     int nextLowestX = lowestCorner.getX();
/* 1978 */     int nextLowestY = tiley + 1 - lowestCorner.getY() - tiley;
/* 1979 */     int nsRockTile = Server.rockMesh.getTile(nextLowestX, nextLowestY);
/* 1980 */     short nextLowestHeight = Tiles.decodeHeight(nsRockTile);
/*      */     
/* 1982 */     int weX = tilex + 1 - lowestCorner.getX() - tilex;
/* 1983 */     int weRockTile = Server.rockMesh.getTile(weX, lowestCorner.getY());
/* 1984 */     short weRockHeight = Tiles.decodeHeight(weRockTile);
/* 1985 */     if (weRockHeight < nextLowestHeight) {
/*      */       
/* 1987 */       nextLowestHeight = weRockHeight;
/* 1988 */       nextLowestX = weX;
/* 1989 */       nextLowestY = lowestCorner.getY();
/*      */     } 
/* 1991 */     return new Point(nextLowestX, nextLowestY, nextLowestHeight);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Point findHighestCorner(int tilex, int tiley) {
/* 1996 */     int highestX = 100000;
/* 1997 */     int highestY = 100000;
/* 1998 */     short highestHeight = Short.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2003 */     for (int x = 0; x <= 1; x++) {
/*      */       
/* 2005 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 2007 */         int rockTile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 2008 */         short rockHeight = Tiles.decodeHeight(rockTile);
/*      */ 
/*      */ 
/*      */         
/* 2012 */         if (highestHeight == Short.MAX_VALUE) {
/*      */ 
/*      */           
/* 2015 */           highestHeight = rockHeight;
/* 2016 */           highestX = tilex + x;
/* 2017 */           highestY = tiley + y;
/*      */ 
/*      */         
/*      */         }
/* 2021 */         else if (rockHeight > highestHeight) {
/*      */           
/* 2023 */           highestHeight = rockHeight;
/* 2024 */           highestX = tilex + x;
/* 2025 */           highestY = tiley + y;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2030 */     return new Point(highestX, highestY, highestHeight);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Point findNextHighestCorner(int tilex, int tiley, Point highestCorner) {
/* 2037 */     int nextHighestX = highestCorner.getX();
/* 2038 */     int nextHighestY = tiley + 1 - highestCorner.getY() - tiley;
/* 2039 */     int nsRockTile = Server.rockMesh.getTile(nextHighestX, nextHighestY);
/* 2040 */     short nextHighestHeight = Tiles.decodeHeight(nsRockTile);
/*      */     
/* 2042 */     int weX = tilex + 1 - highestCorner.getX() - tilex;
/* 2043 */     int weRockTile = Server.rockMesh.getTile(weX, highestCorner.getY());
/* 2044 */     short weRockHeight = Tiles.decodeHeight(weRockTile);
/* 2045 */     if (weRockHeight > nextHighestHeight) {
/*      */       
/* 2047 */       nextHighestHeight = weRockHeight;
/* 2048 */       nextHighestX = weX;
/* 2049 */       nextHighestY = highestCorner.getY();
/*      */     } 
/* 2051 */     return new Point(nextHighestX, nextHighestY, nextHighestHeight);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean mayLowerCornerOnSlope(int targetHeight, Creature performer, int checkedTile) {
/* 2056 */     int nCHeight = Tiles.decodeHeight(checkedTile);
/* 2057 */     if (nCHeight - targetHeight > 270) {
/*      */       
/* 2059 */       performer.getCommunicator().sendNormalServerMessage("The mountainside would risk crumbling. You can't open a hole here.");
/*      */       
/* 2061 */       return false;
/*      */     } 
/* 2063 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean areAllTilesRockOrReinforcedRock(int tilex, int tiley, int tile, int direction, boolean creatingExit, boolean makingWideTunnel) {
/* 2069 */     boolean checkTile = false;
/* 2070 */     int t = 0;
/* 2071 */     byte type = 0;
/* 2072 */     for (int x = -1; x <= 1; x++) {
/*      */       
/* 2074 */       for (int y = -1; y <= 1; y++) {
/*      */         
/* 2076 */         t = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 2077 */         type = Tiles.decodeType(t);
/*      */ 
/*      */         
/* 2080 */         if (direction == 3) {
/*      */           
/* 2082 */           if (y <= 0)
/*      */           {
/* 2084 */             checkTile = true;
/*      */           }
/*      */         }
/* 2087 */         else if (direction == 4) {
/*      */           
/* 2089 */           if (x >= 0)
/*      */           {
/* 2091 */             checkTile = true;
/*      */           }
/*      */         }
/* 2094 */         else if (direction == 5) {
/*      */           
/* 2096 */           if (y >= 0)
/*      */           {
/* 2098 */             checkTile = true;
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 2103 */         else if (x <= 0) {
/*      */           
/* 2105 */           checkTile = true;
/*      */         } 
/*      */ 
/*      */         
/* 2109 */         if (checkTile)
/*      */         {
/* 2111 */           if (creatingExit)
/*      */           {
/* 2113 */             if (type != Tiles.Tile.TILE_CAVE_WALL.id && type != Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id && 
/* 2114 */               !Tiles.isReinforcedCave(type))
/*      */             {
/* 2116 */               if (type != Tiles.Tile.TILE_CAVE_EXIT.id || !makingWideTunnel)
/*      */               {
/*      */                 
/* 2119 */                 return false;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2131 */         checkTile = false;
/*      */       } 
/*      */     } 
/* 2134 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isInsideTunnelOk(int tilex, int tiley, int tile, int action, int direction, Creature performer, boolean disintegrate) {
/* 2140 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id || 
/* 2141 */       Server.getCaveResource(tilex, tiley) <= 0 || disintegrate)
/*      */     {
/* 2143 */       if (Tiles.decodeHeight(tile) >= -25 || 
/* 2144 */         Tiles.decodeHeight(tile) == -100) {
/*      */         
/* 2146 */         int dir = 6;
/* 2147 */         if (direction == 3) {
/*      */           
/* 2149 */           dir = 0;
/*      */         }
/* 2151 */         else if (direction == 5) {
/*      */           
/* 2153 */           dir = 4;
/*      */         }
/* 2155 */         else if (direction == 4) {
/* 2156 */           dir = 2;
/* 2157 */         }  boolean[][] solids = new boolean[3][3];
/* 2158 */         float minHeight = 1000000.0F;
/* 2159 */         float maxHeight = 0.0F;
/* 2160 */         float currHeight = 100000.0F;
/* 2161 */         float currCeil = 0.0F;
/* 2162 */         for (int x = -1; x <= 1; x++) {
/*      */           
/* 2164 */           for (int y = -1; y <= 1; y++) {
/*      */             
/* 2166 */             int t = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 2167 */             solids[x + 1][y + 1] = Tiles.isSolidCave(Tiles.decodeType(t));
/* 2168 */             short height = Tiles.decodeHeight(t);
/* 2169 */             int ceil = Tiles.decodeData(t) & 0xFF;
/* 2170 */             boolean setCurrHeight = false;
/* 2171 */             boolean setExitheight = false;
/* 2172 */             if (dir == 0) {
/*      */               
/* 2174 */               if ((x == 0 && y == 1) || (x == 1 && y == 1)) {
/* 2175 */                 setCurrHeight = true;
/* 2176 */               } else if (y == 0 && x >= 0) {
/* 2177 */                 setExitheight = true;
/*      */               } 
/* 2179 */             } else if (dir == 2) {
/*      */               
/* 2181 */               if ((x == 0 && y == 0) || (x == 0 && y == 1)) {
/* 2182 */                 setCurrHeight = true;
/* 2183 */               } else if (x == 1 && y >= 0) {
/* 2184 */                 setExitheight = true;
/*      */               } 
/* 2186 */             } else if (dir == 6) {
/*      */               
/* 2188 */               if ((x == 1 && y == 1) || (x == 1 && y == 0)) {
/* 2189 */                 setCurrHeight = true;
/* 2190 */               } else if (x == 0 && y >= 0) {
/* 2191 */                 setExitheight = true;
/*      */               } 
/* 2193 */             } else if (dir == 4) {
/*      */               
/* 2195 */               if ((x == 0 && y == 0) || (x == 1 && y == 0)) {
/* 2196 */                 setCurrHeight = true;
/* 2197 */               } else if (y == 1 && x >= 0) {
/* 2198 */                 setExitheight = true;
/*      */               } 
/* 2200 */             }  if (setCurrHeight) {
/*      */               
/* 2202 */               if (height < currHeight)
/*      */               {
/* 2204 */                 currHeight = height;
/*      */               }
/* 2206 */               if ((height + ceil) > currCeil)
/*      */               {
/* 2208 */                 currCeil = (height + ceil);
/*      */               }
/*      */             } 
/* 2211 */             if (setExitheight && !isNullWall(t)) {
/*      */               
/* 2213 */               if (height < minHeight)
/*      */               {
/*      */                 
/* 2216 */                 minHeight = height;
/*      */               }
/* 2218 */               if ((height + ceil) > maxHeight)
/*      */               {
/*      */                 
/* 2221 */                 maxHeight = (height + ceil);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 2227 */         if (!solids[0][0])
/*      */         {
/* 2229 */           if (solids[1][0] && solids[0][1]) {
/*      */             
/* 2231 */             performer.getCommunicator().sendNormalServerMessage("The cave walls sound hollow. A dangerous side shaft could emerge.");
/*      */             
/* 2233 */             return false;
/*      */           } 
/*      */         }
/* 2236 */         if (!solids[2][0])
/*      */         {
/* 2238 */           if (solids[2][1] && solids[1][0]) {
/*      */             
/* 2240 */             performer.getCommunicator().sendNormalServerMessage("The cave walls sound hollow. A dangerous side shaft could emerge.");
/*      */             
/* 2242 */             return false;
/*      */           } 
/*      */         }
/* 2245 */         if (!solids[0][2])
/*      */         {
/* 2247 */           if (solids[1][2] && solids[0][1]) {
/*      */             
/* 2249 */             performer.getCommunicator().sendNormalServerMessage("The cave walls sound hollow. A dangerous side shaft could emerge.");
/*      */             
/* 2251 */             return false;
/*      */           } 
/*      */         }
/* 2254 */         if (!solids[2][2])
/*      */         {
/* 2256 */           if (solids[1][2] && solids[2][1]) {
/*      */             
/* 2258 */             performer.getCommunicator().sendNormalServerMessage("The cave walls sound hollow. A dangerous side shaft could emerge.");
/*      */             
/* 2260 */             return false;
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2266 */         if (action == 147) {
/*      */           
/* 2268 */           if (currHeight - 20.0F < minHeight) {
/* 2269 */             minHeight = currHeight - 20.0F;
/*      */           }
/* 2271 */         } else if (action == 146) {
/*      */           
/* 2273 */           if (currCeil + 20.0F > maxHeight)
/* 2274 */             maxHeight = currCeil + 20.0F; 
/*      */         } 
/* 2276 */         if (maxHeight - minHeight > 254.0F) {
/*      */           
/* 2278 */           performer.getCommunicator().sendNormalServerMessage("A dangerous crack is starting to form on the floor. You will have to find another way.");
/*      */           
/* 2280 */           return false;
/*      */         } 
/* 2282 */         if (maxHeight - minHeight > 100.0F)
/*      */         {
/* 2284 */           performer.getCommunicator().sendNormalServerMessage("You hear falling rocks from the other side of the wall. A deep shaft will probably emerge.");
/*      */         }
/*      */         
/* 2287 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 2291 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean wouldPassThroughRockLayer(int tilex, int tiley, int tile, int action) {
/* 2296 */     int maxCaveFloor = -100000;
/* 2297 */     int minRockHeight = 100000; int x;
/* 2298 */     for (x = 0; x <= 1; x++) {
/*      */       
/* 2300 */       for (int y = 0; y <= 1; y++) {
/*      */ 
/*      */ 
/*      */         
/* 2304 */         tile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 2305 */         short ht = Tiles.decodeHeight(tile);
/* 2306 */         boolean allSolid = true;
/* 2307 */         if (ht != -100) {
/*      */           
/* 2309 */           for (int xx = -1; xx <= 0 && allSolid; xx++) {
/*      */             
/* 2311 */             for (int yy = -1; yy <= 0 && allSolid; yy++) {
/*      */               
/* 2313 */               int encodedTile = Server.caveMesh.getTile(tilex + x + xx, tiley + y + yy);
/* 2314 */               byte type = Tiles.decodeType(encodedTile);
/* 2315 */               if (!Tiles.isSolidCave(type))
/* 2316 */                 allSolid = false; 
/*      */             } 
/*      */           } 
/* 2319 */           if (allSolid) {
/*      */             
/* 2321 */             ht = -100;
/* 2322 */             Server.caveMesh.setTile(tilex + x, tiley + y, Tiles.encode(ht, Tiles.decodeType(tile), (byte)0));
/*      */           } 
/*      */         } 
/* 2325 */         if (ht > maxCaveFloor)
/* 2326 */           maxCaveFloor = ht; 
/*      */       } 
/*      */     } 
/* 2329 */     for (x = 0; x <= 1; x++) {
/*      */       
/* 2331 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 2333 */         tile = Server.rockMesh.getTile(tilex + x, tiley + y);
/* 2334 */         short ht = Tiles.decodeHeight(tile);
/* 2335 */         if (ht < minRockHeight)
/* 2336 */           minRockHeight = Tiles.decodeHeight(tile); 
/*      */       } 
/*      */     } 
/* 2339 */     int mod = 0;
/* 2340 */     if (action == 147) {
/*      */       
/* 2342 */       mod = -20;
/*      */     }
/* 2344 */     else if (action == 146) {
/*      */       
/* 2346 */       mod = 20;
/*      */     } 
/* 2348 */     if (maxCaveFloor + mod + 30 > minRockHeight) {
/* 2349 */       return true;
/*      */     }
/* 2351 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean createInsideTunnel(int tilex, int tiley, int tile, Creature performer, int action, int direction, boolean disintegrate, @Nullable Action act) {
/* 2357 */     if (isInsideTunnelOk(tilex, tiley, tile, action, direction, performer, disintegrate)) {
/*      */       
/* 2359 */       if (wouldPassThroughRockLayer(tilex, tiley, tile, action)) {
/*      */         
/* 2361 */         int mineDir = getTunnelExit(tilex, tiley);
/* 2362 */         if (mineDir == -1) {
/*      */           
/* 2364 */           performer.getCommunicator().sendNormalServerMessage("The topology here makes it impossible to mine in a good way.");
/*      */           
/* 2366 */           return false;
/*      */         } 
/* 2368 */         boolean makingWideTunnel = false;
/* 2369 */         if (canHaveWideEntrance(performer, tilex, tiley))
/*      */         {
/* 2371 */           makingWideTunnel = true;
/*      */         }
/* 2373 */         if (areAllTilesRockOrReinforcedRock(tilex, tiley, tile, mineDir, true, makingWideTunnel))
/*      */         {
/* 2375 */           int t = Server.surfaceMesh.getTile(tilex, tiley);
/* 2376 */           if (Tiles.decodeType(t) != Tiles.Tile.TILE_ROCK.id && Tiles.decodeType(t) != Tiles.Tile.TILE_CLIFF.id) {
/*      */             
/* 2378 */             performer.getCommunicator().sendNormalServerMessage("The cave walls look very unstable and dirt flows in. You would be buried alive.");
/*      */             
/* 2380 */             return false;
/*      */           } 
/*      */ 
/*      */           
/* 2384 */           if (!createOutInTunnel(tilex, tiley, tile, performer, 0))
/*      */           {
/* 2386 */             return false;
/*      */           
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2392 */           performer.getCommunicator().sendNormalServerMessage("The cave walls look very unstable. You cannot keep mining here.");
/*      */ 
/*      */           
/* 2395 */           return false;
/*      */         }
/*      */       
/* 2398 */       } else if (!createStandardTunnel(tilex, tiley, tile, performer, action, direction, disintegrate, act)) {
/*      */         
/* 2400 */         return false;
/*      */       } 
/* 2402 */       TileEvent.log(tilex, tiley, -1, performer.getWurmId(), 227);
/* 2403 */       return true;
/*      */     } 
/* 2405 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean allCornersAtRockHeight(int tilex, int tiley) {
/* 2410 */     for (int x = 0; x <= 1; x++) {
/*      */       
/* 2412 */       for (int y = 0; y <= 1; y++) {
/*      */         
/* 2414 */         int cavet = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 2415 */         short caveheight = Tiles.decodeHeight(cavet);
/* 2416 */         int ceil = Tiles.decodeData(cavet) & 0xFF;
/* 2417 */         short rockHeight = Tiles.decodeHeight(Server.rockMesh.getTile(tilex + x, tiley + y));
/* 2418 */         if (caveheight + ceil != rockHeight)
/* 2419 */           return false; 
/*      */       } 
/*      */     } 
/* 2422 */     return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getCurrentCeilingHeight(int tilex, int tiley) {
/* 2488 */     int cavet = Server.caveMesh.getTile(tilex, tiley);
/* 2489 */     return Tiles.decodeHeight(cavet) + (Tiles.decodeData(cavet) & 0xFF);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final int getRockHeight(int tilex, int tiley) {
/* 2494 */     int rockTile = Server.rockMesh.getTile(tilex, tiley);
/* 2495 */     return Tiles.decodeHeight(rockTile);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean isNullWall(int tile) {
/* 2500 */     byte cavetype = Tiles.decodeType(tile);
/*      */     
/* 2502 */     if (!Tiles.isSolidCave(cavetype)) {
/* 2503 */       return false;
/*      */     }
/* 2505 */     return (Tiles.decodeHeight(tile) == -100 && (Tiles.decodeData(tile) & 0xFF) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int[] getFloorAndCeiling(int tilex, int tiley, int fromHeight, int mod, boolean tryZeroCeiling, boolean tryCeilingAtRockHeight, Creature performer) {
/* 2511 */     int targetFloor = fromHeight + mod;
/* 2512 */     boolean fixedHeight = false;
/*      */     
/* 2514 */     for (int x = -1; x <= 0; x++) {
/*      */       
/* 2516 */       for (int y = -1; y <= 0; y++) {
/*      */         
/* 2518 */         VolaTile vt = Zones.getTileOrNull(tilex + x, tiley + y, false);
/* 2519 */         if (vt != null && vt.getStructure() != null) {
/*      */           
/* 2521 */           fixedHeight = true;
/* 2522 */           int i = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 2523 */           targetFloor = Tiles.decodeHeight(i);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2528 */     int targetCeiling = targetFloor + 30;
/* 2529 */     if (!tryZeroCeiling && !tryCeilingAtRockHeight && !fixedHeight) {
/*      */       
/* 2531 */       if (Server.rand.nextInt(5) == 0)
/* 2532 */         targetCeiling = maybeAddExtraSlopes(performer, targetCeiling); 
/* 2533 */       if (Server.rand.nextInt(5) == 0) {
/* 2534 */         targetFloor = maybeAddExtraSlopes(performer, targetFloor);
/*      */       }
/* 2536 */     } else if (tryZeroCeiling) {
/* 2537 */       targetCeiling = targetFloor;
/* 2538 */     }  int rockHeight = getRockHeight(tilex, tiley);
/* 2539 */     int tile = Server.caveMesh.getTile(tilex, tiley);
/* 2540 */     int currentFloor = Tiles.decodeHeight(tile);
/* 2541 */     int currentCeiling = currentFloor + (Tiles.decodeData(tile) & 0xFF);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2546 */     if (targetFloor >= currentFloor && !isNullWall(tile))
/*      */     {
/* 2548 */       targetFloor = currentFloor;
/*      */     }
/*      */     
/* 2551 */     if (targetCeiling <= currentCeiling) {
/*      */       
/* 2553 */       targetCeiling = currentCeiling;
/* 2554 */       if (mod > 0 && targetFloor < currentFloor && !isNullWall(tile))
/*      */       {
/* 2556 */         targetFloor = currentFloor;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2561 */     if (targetCeiling >= rockHeight || tryCeilingAtRockHeight)
/*      */     {
/* 2563 */       targetCeiling = rockHeight;
/*      */     }
/* 2565 */     if (targetFloor >= rockHeight)
/*      */     {
/* 2567 */       targetFloor = rockHeight;
/*      */     }
/*      */ 
/*      */     
/* 2571 */     if (targetCeiling - targetFloor >= 255)
/*      */     {
/*      */       
/* 2574 */       if (targetFloor < currentFloor) {
/*      */         
/* 2576 */         targetFloor = currentCeiling - 255;
/* 2577 */         targetCeiling = currentCeiling;
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 2583 */         targetCeiling = Math.min(currentCeiling, targetFloor + 255);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 2588 */     if (targetCeiling < 5 && !tryZeroCeiling) {
/* 2589 */       targetCeiling = 5;
/*      */     }
/*      */     
/* 2592 */     return new int[] { targetFloor, targetCeiling };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int maybeAddExtraSlopes(Creature performer, int _previousValue) {
/*      */     int miningSkillMod;
/* 2604 */     if (performer.getPower() > 0) return _previousValue;
/*      */     
/* 2606 */     if (performer instanceof Player) {
/*      */       double realKnowledge;
/* 2608 */       Player p = (Player)performer;
/*      */       
/* 2610 */       Skill mine = null;
/*      */       
/*      */       try {
/* 2613 */         Skills skills = p.getSkills();
/* 2614 */         mine = skills.getSkill(1008);
/*      */       
/*      */       }
/* 2617 */       catch (NoSuchSkillException nss) {
/*      */         
/* 2619 */         logger.info(performer.getName() + ": No such skill for mining? " + nss);
/*      */       } 
/*      */       
/* 2622 */       if (mine == null) {
/* 2623 */         realKnowledge = 1.0D;
/*      */       } else {
/* 2625 */         realKnowledge = mine.getKnowledge(0.0D);
/*      */       } 
/*      */ 
/*      */       
/* 2629 */       if (realKnowledge > 90.0D)
/*      */       {
/* 2631 */         return _previousValue;
/*      */       }
/*      */       
/* 2634 */       if (realKnowledge > 70.0D) {
/*      */         
/* 2636 */         miningSkillMod = 1;
/*      */       }
/* 2638 */       else if (realKnowledge > 50.0D) {
/*      */         
/* 2640 */         miningSkillMod = 2;
/*      */       }
/*      */       else {
/*      */         
/* 2644 */         miningSkillMod = 3;
/*      */       } 
/*      */     } else {
/* 2647 */       miningSkillMod = 3;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2652 */     int randVal = Server.rand.nextInt(miningSkillMod * 2 + 1);
/*      */     
/* 2654 */     return _previousValue - miningSkillMod + randVal;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void maybeCreateSource(int tilex, int tiley, Creature performer) {
/* 2659 */     if (Server.rand.nextInt(10000) == 0 || (Servers.localServer.testServer && performer
/* 2660 */       .getPower() >= 5 && Server.rand.nextInt(10) == 0))
/*      */     {
/* 2662 */       if (!Servers.localServer.EPIC || !Servers.localServer.HOMESERVER)
/*      */       {
/*      */ 
/*      */         
/* 2666 */         if ((Items.getSourceSprings()).length > 0 && (Items.getSourceSprings()).length < Zones.worldTileSizeX / 20) {
/*      */           
/*      */           try {
/*      */             
/* 2670 */             Item target1 = ItemFactory.createItem(767, 100.0F, (tilex * 4 + 2), (tiley * 4 + 2), Server.rand
/* 2671 */                 .nextInt(360), false, (byte)0, -10L, "");
/*      */             
/* 2673 */             target1.setSizes(target1.getSizeX() + Server.rand.nextInt(1), target1
/* 2674 */                 .getSizeY() + Server.rand.nextInt(2), target1.getSizeZ() + Server.rand.nextInt(3));
/* 2675 */             logger.log(Level.INFO, "Created " + target1
/* 2676 */                 .getName() + " at " + target1.getTileX() + " " + target1.getTileY() + " sizes " + target1
/* 2677 */                 .getSizeX() + "," + target1.getSizeY() + "," + target1.getSizeZ() + ")");
/*      */             
/* 2679 */             Items.addSourceSpring(target1);
/* 2680 */             performer.getCommunicator().sendSafeServerMessage("You find a source spring!");
/*      */           }
/* 2682 */           catch (FailedException fe) {
/*      */             
/* 2684 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */           }
/* 2686 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 2688 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean createStandardTunnel(int tilex, int tiley, int tile, Creature performer, int action, int direction, boolean disintegrate, @Nullable Action act) {
/* 2698 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id || 
/* 2699 */       Server.getCaveResource(tilex, tiley) <= 0 || disintegrate)
/*      */     {
/* 2701 */       if (areAllTilesRockOrReinforcedRock(tilex, tiley, tile, direction, false, false)) {
/*      */         
/* 2703 */         if (Tiles.decodeHeight(tile) >= -25 || Tiles.decodeHeight(tile) == -100) {
/*      */           
/* 2705 */           int dir = 6;
/*      */           
/* 2707 */           if (direction == 3) {
/*      */             
/* 2709 */             dir = 0;
/*      */           }
/* 2711 */           else if (direction == 5) {
/*      */             
/* 2713 */             dir = 4;
/*      */           }
/* 2715 */           else if (direction == 4) {
/*      */             
/* 2717 */             dir = 2;
/*      */           } 
/*      */ 
/*      */           
/* 2721 */           int mod = 0;
/* 2722 */           if (action == 147) {
/* 2723 */             mod = -20;
/* 2724 */           } else if (action == 146) {
/* 2725 */             mod = 20;
/* 2726 */           }  if (disintegrate)
/* 2727 */             Server.setCaveResource(tilex, tiley, 0); 
/* 2728 */           if (dir == 0) {
/*      */             
/* 2730 */             int fromx = tilex;
/* 2731 */             int fromy = tiley + 1;
/* 2732 */             int t = Server.caveMesh.getTile(fromx, fromy);
/* 2733 */             short height = Tiles.decodeHeight(t);
/*      */             
/* 2735 */             int fromx2 = tilex + 1;
/* 2736 */             int fromy2 = tiley + 1;
/* 2737 */             int t2 = Server.caveMesh.getTile(fromx2, fromy2);
/* 2738 */             short height2 = Tiles.decodeHeight(t2);
/* 2739 */             short avheight = (short)((height + height2) / 2);
/*      */             
/* 2741 */             int[] newfloorceil = getFloorAndCeiling(tilex, tiley, avheight, mod, false, false, performer);
/*      */             
/* 2743 */             int newFloorHeight = newfloorceil[0];
/* 2744 */             if (newFloorHeight < -25)
/* 2745 */               newFloorHeight = -25; 
/* 2746 */             int newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2749 */             Server.caveMesh.setTile(tilex, tiley, 
/* 2750 */                 Tiles.encode((short)newFloorHeight, Tiles.Tile.TILE_CAVE.id, (byte)(newCeil - newFloorHeight)));
/*      */             
/* 2752 */             maybeCreateSource(tilex, tiley, performer);
/*      */             
/* 2754 */             t2 = Server.caveMesh.getTile(tilex + 1, tiley);
/* 2755 */             newfloorceil = getFloorAndCeiling(tilex + 1, tiley, avheight, mod, false, false, performer);
/*      */             
/* 2757 */             newFloorHeight = newfloorceil[0];
/* 2758 */             if (newFloorHeight < -25)
/* 2759 */               newFloorHeight = -25; 
/* 2760 */             newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2763 */             Server.caveMesh.setTile(tilex + 1, tiley, 
/* 2764 */                 Tiles.encode((short)newFloorHeight, Tiles.decodeType(t2), (byte)(newCeil - newFloorHeight)));
/*      */             
/* 2766 */             sendCaveTile(tilex, tiley, 0, 0);
/*      */           }
/* 2768 */           else if (dir == 4) {
/*      */             
/* 2770 */             int fromx = tilex;
/* 2771 */             int fromy = tiley;
/* 2772 */             int t = Server.caveMesh.getTile(fromx, fromy);
/* 2773 */             short height = Tiles.decodeHeight(t);
/* 2774 */             Server.caveMesh.setTile(tilex, tiley, Tiles.encode(height, Tiles.Tile.TILE_CAVE.id, Tiles.decodeData(t)));
/*      */             
/* 2776 */             maybeCreateSource(tilex, tiley, performer);
/*      */             
/* 2778 */             int fromx2 = tilex + 1;
/* 2779 */             int fromy2 = tiley;
/* 2780 */             int t2 = Server.caveMesh.getTile(fromx2, fromy2);
/* 2781 */             short height2 = Tiles.decodeHeight(t2);
/* 2782 */             short avheight = (short)((height + height2) / 2);
/*      */             
/* 2784 */             t2 = Server.caveMesh.getTile(tilex, tiley + 1);
/* 2785 */             int[] newfloorceil = getFloorAndCeiling(tilex, tiley + 1, avheight, mod, false, false, performer);
/* 2786 */             int newFloorHeight = newfloorceil[0];
/* 2787 */             if (newFloorHeight < -25)
/* 2788 */               newFloorHeight = -25; 
/* 2789 */             int newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2792 */             Server.caveMesh.setTile(tilex, tiley + 1, 
/* 2793 */                 Tiles.encode((short)newFloorHeight, Tiles.decodeType(t2), (byte)(newCeil - newFloorHeight)));
/*      */ 
/*      */             
/* 2796 */             t2 = Server.caveMesh.getTile(tilex + 1, tiley + 1);
/* 2797 */             newfloorceil = getFloorAndCeiling(tilex + 1, tiley + 1, avheight, mod, false, false, performer);
/* 2798 */             newFloorHeight = newfloorceil[0];
/* 2799 */             if (newFloorHeight < -25)
/* 2800 */               newFloorHeight = -25; 
/* 2801 */             newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2804 */             Server.caveMesh.setTile(tilex + 1, tiley + 1, 
/* 2805 */                 Tiles.encode((short)newFloorHeight, Tiles.decodeType(t2), (byte)(newCeil - newFloorHeight)));
/*      */             
/* 2807 */             sendCaveTile(tilex, tiley, 0, 0);
/*      */           }
/* 2809 */           else if (dir == 2) {
/*      */             
/* 2811 */             int fromx = tilex;
/* 2812 */             int fromy = tiley;
/* 2813 */             int t = Server.caveMesh.getTile(fromx, fromy);
/* 2814 */             short height = Tiles.decodeHeight(t);
/* 2815 */             Server.caveMesh.setTile(tilex, tiley, Tiles.encode(height, Tiles.Tile.TILE_CAVE.id, Tiles.decodeData(t)));
/*      */             
/* 2817 */             maybeCreateSource(tilex, tiley, performer);
/*      */             
/* 2819 */             int fromx2 = tilex;
/* 2820 */             int fromy2 = tiley + 1;
/* 2821 */             int t2 = Server.caveMesh.getTile(fromx2, fromy2);
/* 2822 */             short height2 = Tiles.decodeHeight(t2);
/* 2823 */             short avheight = (short)((height + height2) / 2);
/*      */             
/* 2825 */             t2 = Server.caveMesh.getTile(tilex + 1, tiley);
/* 2826 */             int[] newfloorceil = getFloorAndCeiling(tilex + 1, tiley, avheight, mod, false, false, performer);
/* 2827 */             int newFloorHeight = newfloorceil[0];
/* 2828 */             if (newFloorHeight < -25)
/* 2829 */               newFloorHeight = -25; 
/* 2830 */             int newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2833 */             Server.caveMesh.setTile(tilex + 1, tiley, 
/* 2834 */                 Tiles.encode((short)newFloorHeight, Tiles.decodeType(t2), (byte)(newCeil - newFloorHeight)));
/*      */ 
/*      */             
/* 2837 */             t2 = Server.caveMesh.getTile(tilex + 1, tiley + 1);
/* 2838 */             newfloorceil = getFloorAndCeiling(tilex + 1, tiley + 1, avheight, mod, false, false, performer);
/* 2839 */             newFloorHeight = newfloorceil[0];
/* 2840 */             if (newFloorHeight < -25)
/* 2841 */               newFloorHeight = -25; 
/* 2842 */             newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2845 */             Server.caveMesh.setTile(tilex + 1, tiley + 1, 
/* 2846 */                 Tiles.encode((short)newFloorHeight, Tiles.decodeType(t2), (byte)(newCeil - newFloorHeight)));
/*      */             
/* 2848 */             sendCaveTile(tilex, tiley, 0, 0);
/*      */           }
/* 2850 */           else if (dir == 6) {
/*      */             
/* 2852 */             int fromx = tilex + 1;
/* 2853 */             int fromy = tiley;
/* 2854 */             int t = Server.caveMesh.getTile(fromx, fromy);
/* 2855 */             short height = Tiles.decodeHeight(t);
/*      */             
/* 2857 */             int fromx2 = tilex + 1;
/* 2858 */             int fromy2 = tiley + 1;
/* 2859 */             int t2 = Server.caveMesh.getTile(fromx2, fromy2);
/* 2860 */             short height2 = Tiles.decodeHeight(t2);
/* 2861 */             short avheight = (short)((height + height2) / 2);
/*      */             
/* 2863 */             int[] newfloorceil = getFloorAndCeiling(tilex, tiley, avheight, mod, false, false, performer);
/* 2864 */             int newFloorHeight = newfloorceil[0];
/* 2865 */             if (newFloorHeight < -25)
/* 2866 */               newFloorHeight = -25; 
/* 2867 */             int newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2870 */             Server.caveMesh.setTile(tilex, tiley, 
/* 2871 */                 Tiles.encode((short)newFloorHeight, Tiles.Tile.TILE_CAVE.id, (byte)(newCeil - newFloorHeight)));
/*      */             
/* 2873 */             maybeCreateSource(tilex, tiley, performer);
/*      */             
/* 2875 */             t2 = Server.caveMesh.getTile(tilex, tiley + 1);
/* 2876 */             newfloorceil = getFloorAndCeiling(tilex, tiley + 1, avheight, mod, false, false, performer);
/* 2877 */             newFloorHeight = newfloorceil[0];
/* 2878 */             if (newFloorHeight < -25)
/* 2879 */               newFloorHeight = -25; 
/* 2880 */             newCeil = newfloorceil[1];
/*      */ 
/*      */             
/* 2883 */             Server.caveMesh.setTile(tilex, tiley + 1, 
/* 2884 */                 Tiles.encode((short)newFloorHeight, Tiles.decodeType(t2), (byte)(newCeil - newFloorHeight)));
/*      */             
/* 2886 */             sendCaveTile(tilex, tiley, 0, 0);
/*      */           } 
/* 2888 */           if (!performer.isPlayer()) {
/*      */             
/* 2890 */             Item gem = createGem(-1, -1, performer, (Server.rand.nextFloat() * 100.0F), false, act);
/* 2891 */             if (gem != null) {
/* 2892 */               performer.getInventory().insertItem(gem);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/* 2898 */         return false;
/*      */       } 
/*      */     }
/* 2901 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void sendCaveTile(int tilex, int tiley, int diffX, int diffY) {
/* 2906 */     Players.getInstance().sendChangedTile(tilex + diffX, tiley + diffY, false, true);
/*      */ 
/*      */     
/* 2909 */     for (int x = -1; x <= 0; x++) {
/*      */       
/* 2911 */       for (int y = -1; y <= 0; y++) {
/*      */ 
/*      */         
/*      */         try {
/* 2915 */           Zone toCheckForChange = Zones.getZone(tilex + diffX + x, tiley + diffY + y, false);
/* 2916 */           toCheckForChange.changeTile(tilex + diffX + x, tiley + diffY + y);
/*      */         }
/* 2918 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 2920 */           logger.log(Level.INFO, "no such zone?: " + (tilex + diffX + x) + ", " + (tiley + diffY + y), (Throwable)nsz);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean surroundedByWalls(int x, int y) {
/* 2929 */     if (!Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(x - 1, y)))) {
/* 2930 */       return false;
/*      */     }
/* 2932 */     if (!Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(x + 1, y)))) {
/* 2933 */       return false;
/*      */     }
/* 2935 */     if (!Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(x, y - 1)))) {
/* 2936 */       return false;
/*      */     }
/* 2938 */     if (!Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(x, y + 1))))
/* 2939 */       return false; 
/* 2940 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void reProspect() {
/* 2945 */     int numsChanged = 0;
/* 2946 */     int numsUntouched = 0;
/* 2947 */     for (int x = 0; x < (1 << Constants.meshSize) * (1 << Constants.meshSize); x++) {
/*      */       
/* 2949 */       int xx = x & (1 << Constants.meshSize) - 1;
/* 2950 */       int yy = x >> Constants.meshSize;
/*      */       
/* 2952 */       int old = Server.caveMesh.getTile(xx, yy);
/* 2953 */       if (Tiles.isOreCave(Tiles.decodeType(old)))
/*      */       {
/* 2955 */         if (xx > 5 && yy > 5 && xx < worldSizeX - 3 && yy < worldSizeX - 3)
/*      */         {
/* 2957 */           if (surroundedByWalls(xx, yy)) {
/*      */             
/* 2959 */             byte newType = prospect(xx, yy, true);
/* 2960 */             Server.caveMesh.setTile(xx, yy, Tiles.encode(Tiles.decodeHeight(old), newType, Tiles.decodeData(old)));
/* 2961 */             numsChanged++;
/*      */           } else {
/*      */             
/* 2964 */             numsUntouched++;
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2972 */     logger.log(Level.INFO, "Reprospect finished. Changed=" + numsChanged + ", untouched=" + numsUntouched);
/*      */     
/*      */     try {
/* 2975 */       Server.caveMesh.saveAllDirtyRows();
/*      */     }
/* 2977 */     catch (IOException iox) {
/*      */       
/* 2979 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Item createRandomGem() {
/* 2985 */     return createRandomGem(100.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Item createRandomGem(float maxql) {
/*      */     try {
/* 2992 */       int rand = Server.rand.nextInt(300);
/* 2993 */       int templateId = 349;
/* 2994 */       float ql = Server.rand.nextFloat() * maxql;
/* 2995 */       if (rand < 50) {
/*      */         
/* 2997 */         templateId = 349;
/*      */       }
/* 2999 */       else if (rand < 100) {
/* 3000 */         templateId = 446;
/* 3001 */       } else if (rand < 140) {
/*      */         
/* 3003 */         templateId = 376;
/* 3004 */         if (ql >= 99.0F) {
/* 3005 */           templateId = 377;
/*      */         }
/* 3007 */       } else if (rand < 180) {
/*      */         
/* 3009 */         templateId = 374;
/* 3010 */         if (ql >= 99.0F) {
/* 3011 */           templateId = 375;
/*      */         }
/* 3013 */       } else if (rand < 220) {
/*      */         
/* 3015 */         templateId = 382;
/* 3016 */         if (ql >= 99.0F) {
/* 3017 */           templateId = 383;
/*      */         }
/* 3019 */       } else if (rand < 260) {
/*      */         
/* 3021 */         templateId = 378;
/* 3022 */         if (ql >= 99.0F) {
/* 3023 */           templateId = 379;
/*      */         }
/* 3025 */       } else if (rand < 300) {
/*      */         
/* 3027 */         templateId = 380;
/* 3028 */         if (ql >= 99.0F)
/* 3029 */           templateId = 381; 
/*      */       } 
/* 3031 */       return ItemFactory.createItem(templateId, Server.rand.nextFloat() * ql, null);
/*      */     }
/* 3033 */     catch (FailedException fe) {
/*      */       
/* 3035 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */     }
/* 3037 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 3039 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     } 
/* 3041 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final Item createGem(int minedTilex, int minedTiley, Creature performer, double power, boolean surfaced, @Nullable Action act) {
/* 3047 */     return createGem(minedTilex, minedTiley, minedTilex, minedTiley, performer, power, surfaced, act);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final Item createGem(int tilex, int tiley, int createtilex, int createtiley, Creature performer, double power, boolean surfaced, @Nullable Action act) {
/* 3053 */     byte rarity = (act != null) ? act.getRarity() : 0;
/*      */     
/*      */     try {
/* 3056 */       rockRandom.setSeed((tilex + tiley * Zones.worldTileSizeY) * 102533L);
/* 3057 */       if (rockRandom.nextInt(100) == 0)
/*      */       {
/*      */         
/* 3060 */         if (Server.rand.nextInt(10) == 0) {
/*      */           
/* 3062 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3064 */             Item gem = ItemFactory.createItem(349, (float)power, null);
/* 3065 */             gem.setLastOwnerId(performer.getWurmId());
/* 3066 */             return gem;
/*      */           } 
/*      */ 
/*      */           
/* 3070 */           Item salt = ItemFactory.createItem(349, (float)power, rarity, null);
/* 3071 */           salt.setLastOwnerId(performer.getWurmId());
/* 3072 */           salt.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3077 */           performer.getCommunicator().sendNormalServerMessage("You mine some salt.");
/*      */         } 
/*      */       }
/*      */       
/* 3081 */       rockRandom.setSeed((tilex + tiley * Zones.worldTileSizeY) * SOURCE_PRIME);
/* 3082 */       if (rockRandom.nextInt(sourceFactor) == 0) {
/*      */ 
/*      */         
/* 3085 */         boolean isVein = Tiles.isOreCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)));
/* 3086 */         if (Server.rand.nextInt(10) == 0 && !isVein) {
/*      */           
/* 3088 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3090 */             Item gem = ItemFactory.createItem(765, (float)power, null);
/* 3091 */             gem.setLastOwnerId(performer.getWurmId());
/* 3092 */             return gem;
/*      */           } 
/*      */ 
/*      */           
/* 3096 */           Item crystal = ItemFactory.createItem(765, (float)power, rarity, null);
/* 3097 */           crystal.setLastOwnerId(performer.getWurmId());
/* 3098 */           crystal.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3103 */           performer.getCommunicator().sendNormalServerMessage("You mine some pink crystals.");
/*      */         } 
/*      */       } 
/*      */       
/* 3107 */       rockRandom.setSeed((tilex + tiley * Zones.worldTileSizeY) * 6883L);
/* 3108 */       if (rockRandom.nextInt(200) == 0)
/*      */       {
/*      */         
/* 3111 */         if (Server.rand.nextInt(40) == 0) {
/*      */           
/* 3113 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3115 */             Item gem = ItemFactory.createItem(446, (float)power, null);
/*      */             
/* 3117 */             gem.setLastOwnerId(performer.getWurmId());
/* 3118 */             return gem;
/*      */           } 
/*      */ 
/*      */           
/* 3122 */           Item flint = ItemFactory.createItem(446, (float)power, rarity, null);
/* 3123 */           flint.setLastOwnerId(performer.getWurmId());
/* 3124 */           flint.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3129 */           performer.getCommunicator().sendNormalServerMessage("You find flint!");
/*      */         } 
/*      */       }
/*      */       
/* 3133 */       if (Server.rand.nextInt(1000) == 0)
/*      */       {
/* 3135 */         int rand = Server.rand.nextInt(5);
/*      */         
/* 3137 */         if (rand == 0)
/*      */         {
/* 3139 */           int templateId = 376;
/* 3140 */           float ql = Math.min(MAX_QL, Server.rand.nextFloat() * 100.0F);
/* 3141 */           if (ql >= 99.0F)
/* 3142 */             templateId = 377; 
/* 3143 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3145 */             Item item = ItemFactory.createItem(templateId, (float)power, null);
/*      */             
/* 3147 */             item.setLastOwnerId(performer.getWurmId());
/* 3148 */             return item;
/*      */           } 
/*      */ 
/*      */           
/* 3152 */           Item gem = ItemFactory.createItem(templateId, (float)power, rarity, null);
/* 3153 */           gem.setLastOwnerId(performer.getWurmId());
/* 3154 */           gem.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3161 */           if (ql >= 99.0F)
/* 3162 */             performer.achievement(298); 
/* 3163 */           if (gem.getQualityLevel() > 90.0F)
/* 3164 */             performer.achievement(299); 
/* 3165 */           if (rarity > 2)
/* 3166 */             performer.achievement(334); 
/* 3167 */           performer.getCommunicator().sendNormalServerMessage("You find " + gem.getNameWithGenus() + "!");
/*      */         
/*      */         }
/* 3170 */         else if (rand == 1)
/*      */         {
/* 3172 */           int templateId = 374;
/* 3173 */           float ql = Math.min(MAX_QL, Server.rand.nextFloat() * 100.0F);
/* 3174 */           if (ql >= 99.0F)
/* 3175 */             templateId = 375; 
/* 3176 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3178 */             Item item = ItemFactory.createItem(templateId, (float)power, null);
/*      */             
/* 3180 */             item.setLastOwnerId(performer.getWurmId());
/* 3181 */             return item;
/*      */           } 
/*      */ 
/*      */           
/* 3185 */           Item gem = ItemFactory.createItem(templateId, (float)power, rarity, null);
/* 3186 */           gem.setLastOwnerId(performer.getWurmId());
/* 3187 */           gem.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3194 */           if (ql >= 99.0F)
/* 3195 */             performer.achievement(298); 
/* 3196 */           if (gem.getQualityLevel() > 90.0F)
/* 3197 */             performer.achievement(299); 
/* 3198 */           if (rarity > 2)
/* 3199 */             performer.achievement(334); 
/* 3200 */           performer.getCommunicator().sendNormalServerMessage("You find " + gem.getNameWithGenus() + "!");
/*      */         
/*      */         }
/* 3203 */         else if (rand == 2)
/*      */         {
/* 3205 */           int templateId = 382;
/* 3206 */           float ql = Math.min(MAX_QL, Server.rand.nextFloat() * 100.0F);
/* 3207 */           if (ql >= 99.0F)
/* 3208 */             templateId = 383; 
/* 3209 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3211 */             Item item = ItemFactory.createItem(templateId, (float)power, null);
/*      */             
/* 3213 */             item.setLastOwnerId(performer.getWurmId());
/* 3214 */             return item;
/*      */           } 
/*      */ 
/*      */           
/* 3218 */           Item gem = ItemFactory.createItem(templateId, (float)power, rarity, null);
/* 3219 */           gem.setLastOwnerId(performer.getWurmId());
/* 3220 */           gem.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3227 */           if (ql >= 99.0F)
/* 3228 */             performer.achievement(298); 
/* 3229 */           if (gem.getQualityLevel() > 90.0F)
/* 3230 */             performer.achievement(299); 
/* 3231 */           if (rarity > 2)
/* 3232 */             performer.achievement(334); 
/* 3233 */           performer.getCommunicator().sendNormalServerMessage("You find " + gem.getNameWithGenus() + "!");
/*      */         
/*      */         }
/* 3236 */         else if (rand == 3)
/*      */         {
/* 3238 */           int templateId = 378;
/* 3239 */           float ql = Math.min(MAX_QL, Server.rand.nextFloat() * 100.0F);
/* 3240 */           if (ql >= 99.0F)
/* 3241 */             templateId = 379; 
/* 3242 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3244 */             Item item = ItemFactory.createItem(templateId, (float)power, null);
/*      */             
/* 3246 */             item.setLastOwnerId(performer.getWurmId());
/* 3247 */             return item;
/*      */           } 
/*      */ 
/*      */           
/* 3251 */           Item gem = ItemFactory.createItem(templateId, (float)power, rarity, null);
/* 3252 */           gem.setLastOwnerId(performer.getWurmId());
/* 3253 */           gem.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3260 */           if (ql >= 99.0F)
/* 3261 */             performer.achievement(298); 
/* 3262 */           if (gem.getQualityLevel() > 90.0F)
/* 3263 */             performer.achievement(299); 
/* 3264 */           if (rarity > 2)
/* 3265 */             performer.achievement(334); 
/* 3266 */           performer.getCommunicator().sendNormalServerMessage("You find " + gem.getNameWithGenus() + "!");
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 3271 */           int templateId = 380;
/* 3272 */           float ql = Math.min(MAX_QL, Server.rand.nextFloat() * 100.0F);
/* 3273 */           if (ql >= 99.0F)
/* 3274 */             templateId = 381; 
/* 3275 */           if (tilex < 0 && tiley < 0) {
/*      */             
/* 3277 */             Item item = ItemFactory.createItem(templateId, (float)power, null);
/*      */             
/* 3279 */             item.setLastOwnerId(performer.getWurmId());
/* 3280 */             return item;
/*      */           } 
/*      */ 
/*      */           
/* 3284 */           Item gem = ItemFactory.createItem(templateId, (float)power, rarity, null);
/* 3285 */           gem.setLastOwnerId(performer.getWurmId());
/* 3286 */           gem.putItemInfrontof(performer, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3293 */           if (ql >= 99.0F)
/* 3294 */             performer.achievement(298); 
/* 3295 */           if (gem.getQualityLevel() > 90.0F)
/* 3296 */             performer.achievement(299); 
/* 3297 */           if (rarity > 2)
/* 3298 */             performer.achievement(334); 
/* 3299 */           performer.getCommunicator().sendNormalServerMessage("You find " + gem.getNameWithGenus() + "!");
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 3304 */     } catch (FailedException fe) {
/*      */       
/* 3306 */       logger.log(Level.WARNING, performer.getName() + ": " + fe.getMessage(), (Throwable)fe);
/*      */     }
/* 3308 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 3310 */       logger.log(Level.WARNING, performer.getName() + ": no template", (Throwable)nst);
/*      */     }
/* 3312 */     catch (Exception ex) {
/*      */       
/* 3314 */       logger.log(Level.WARNING, "Factory failed to produce item", ex);
/*      */     } 
/* 3316 */     return null;
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
/*      */   public static boolean cannotMineSlope(Creature performer, Skill mining, int digTilex, int digTiley) {
/* 3330 */     int diff = Terraforming.getMaxSurfaceDifference(Server.surfaceMesh.getTile(digTilex, digTiley), digTilex, digTiley);
/*      */ 
/*      */     
/* 3333 */     int maxSlope = (int)(mining.getKnowledge(0.0D) * (Servers.localServer.PVPSERVER ? true : 3));
/* 3334 */     if (Math.signum(diff) == 1.0F && diff > maxSlope) {
/*      */       
/* 3336 */       performer.getCommunicator().sendNormalServerMessage("You are too unskilled to mine here.", (byte)3);
/* 3337 */       return true;
/*      */     } 
/*      */     
/* 3340 */     if (Math.signum(diff) == -1.0F && -1 - diff > maxSlope) {
/*      */       
/* 3342 */       performer.getCommunicator().sendNormalServerMessage("You are too unskilled to mine here.", (byte)3);
/* 3343 */       return true;
/*      */     } 
/* 3345 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean mine(Action act, Creature performer, Item source, int tilex, int tiley, short action, float counter, int digTilex, int digTiley) {
/* 3351 */     boolean done = true;
/* 3352 */     int tile = Server.surfaceMesh.getTile(digTilex, digTiley);
/* 3353 */     if (digTilex < 1 || digTilex > (1 << Constants.meshSize) - 1 || digTiley < 1 || digTiley > (1 << Constants.meshSize) - 1) {
/*      */ 
/*      */       
/* 3356 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to mine.", (byte)3);
/* 3357 */       return true;
/*      */     } 
/* 3359 */     if (Zones.isTileProtected(digTilex, digTiley)) {
/*      */       
/* 3361 */       performer.getCommunicator().sendNormalServerMessage("This tile is protected by the gods. You can not mine here.", (byte)3);
/*      */       
/* 3363 */       return true;
/*      */     } 
/* 3365 */     short h = Tiles.decodeHeight(tile);
/* 3366 */     if (h > -25) {
/*      */       
/* 3368 */       done = false;
/* 3369 */       Skills skills = performer.getSkills();
/* 3370 */       Skill mining = null;
/* 3371 */       Skill tool = null;
/* 3372 */       boolean insta = (performer.getPower() > 3 && source.isWand());
/*      */       
/*      */       try {
/* 3375 */         mining = skills.getSkill(1008);
/*      */       }
/* 3377 */       catch (Exception ex) {
/*      */         
/* 3379 */         mining = skills.learn(1008, 1.0F);
/*      */       } 
/*      */       
/*      */       try {
/* 3383 */         tool = skills.getSkill(source.getPrimarySkill());
/*      */       }
/* 3385 */       catch (Exception ex) {
/*      */ 
/*      */         
/*      */         try {
/* 3389 */           tool = skills.learn(source.getPrimarySkill(), 1.0F);
/*      */         }
/* 3391 */         catch (NoSuchSkillException nse) {
/*      */           
/* 3393 */           logger.log(Level.WARNING, performer.getName() + " trying to mine with an item with no primary skill: " + source
/* 3394 */               .getName());
/*      */         } 
/*      */       }  int x;
/* 3397 */       for (x = -1; x <= 0; x++) {
/*      */         
/* 3399 */         for (int y = -1; y <= 0; y++) {
/*      */           
/* 3401 */           byte decType = Tiles.decodeType(Server.surfaceMesh.getTile(digTilex + x, digTiley + y));
/* 3402 */           if (decType != Tiles.Tile.TILE_ROCK.id && decType != Tiles.Tile.TILE_CLIFF.id) {
/*      */             
/* 3404 */             performer.getCommunicator().sendNormalServerMessage("The surrounding area needs to be rock before you mine.", (byte)3);
/*      */             
/* 3406 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/* 3410 */       for (x = 0; x >= -1; x--) {
/*      */         
/* 3412 */         for (int y = 0; y >= -1; y--) {
/*      */           
/* 3414 */           VolaTile volaTile = Zones.getTileOrNull(digTilex + x, digTiley + y, true);
/* 3415 */           if (volaTile != null && volaTile.getStructure() != null) {
/*      */             
/* 3417 */             if (volaTile.getStructure().isTypeHouse()) {
/*      */               
/* 3419 */               if (x == 0 && y == 0) {
/* 3420 */                 performer.getCommunicator().sendNormalServerMessage("You cannot mine in a building.", (byte)3);
/*      */               } else {
/*      */                 
/* 3423 */                 performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a building.", (byte)3);
/*      */               } 
/* 3425 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 3429 */             for (BridgePart bp : volaTile.getBridgeParts()) {
/*      */               
/* 3431 */               if (bp.getType().isSupportType()) {
/*      */                 
/* 3433 */                 performer.getCommunicator().sendNormalServerMessage("The bridge support nearby prevents mining.");
/* 3434 */                 return true;
/*      */               } 
/* 3436 */               if ((x == -1 && bp.hasEastExit()) || (x == 0 && bp
/* 3437 */                 .hasWestExit()) || (y == -1 && bp
/* 3438 */                 .hasSouthExit()) || (y == 0 && bp
/* 3439 */                 .hasNorthExit())) {
/*      */                 
/* 3441 */                 performer.getCommunicator().sendNormalServerMessage("The end of the bridge nearby prevents mining.");
/* 3442 */                 return true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3455 */       VolaTile vt = Zones.getTileOrNull(digTilex, digTiley, true);
/* 3456 */       if (vt != null && (vt.getFencesForLevel(0)).length > 0) {
/*      */         
/* 3458 */         performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a fence.", (byte)3);
/*      */         
/* 3460 */         return true;
/*      */       } 
/* 3462 */       vt = Zones.getTileOrNull(digTilex, digTiley - 1, true);
/* 3463 */       if (vt != null && (vt.getFencesForLevel(0)).length > 0)
/*      */       {
/* 3465 */         for (Fence f : vt.getFencesForLevel(0)) {
/*      */           
/* 3467 */           if (!f.isHorizontal()) {
/*      */             
/* 3469 */             performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a fence.", (byte)3);
/*      */             
/* 3471 */             return true;
/*      */           } 
/*      */         } 
/*      */       }
/* 3475 */       vt = Zones.getTileOrNull(digTilex - 1, digTiley, true);
/* 3476 */       if (vt != null && (vt.getFencesForLevel(0)).length > 0)
/*      */       {
/* 3478 */         for (Fence f : vt.getFencesForLevel(0)) {
/*      */           
/* 3480 */           if (f.isHorizontal()) {
/*      */             
/* 3482 */             performer.getCommunicator().sendNormalServerMessage("You cannot mine next to a fence.", (byte)3);
/*      */             
/* 3484 */             return true;
/*      */           } 
/*      */         } 
/*      */       }
/* 3488 */       int time = 0;
/* 3489 */       VolaTile dropTile = Zones.getTileOrNull((int)performer.getPosX() >> 2, (int)performer.getPosY() >> 2, true);
/* 3490 */       if (dropTile != null)
/*      */       {
/* 3492 */         if (dropTile.getNumberOfItems(performer.getFloorLevel()) > 99) {
/*      */           
/* 3494 */           performer.getCommunicator().sendNormalServerMessage("There is no space to mine here. Clear the area first.", (byte)3);
/*      */           
/* 3496 */           return true;
/*      */         } 
/*      */       }
/* 3499 */       if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */         
/* 3503 */         if (cannotMineSlope(performer, mining, digTilex, digTiley)) {
/* 3504 */           return true;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3522 */         time = Actions.getStandardActionTime(performer, mining, source, 0.0D);
/*      */         
/*      */         try {
/* 3525 */           performer.getCurrentAction().setTimeLeft(time);
/*      */         }
/* 3527 */         catch (NoSuchActionException nsa) {
/*      */           
/* 3529 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*      */         
/* 3532 */         Server.getInstance().broadCastAction(performer.getName() + " starts mining.", performer, 5);
/* 3533 */         performer.getCommunicator().sendNormalServerMessage("You start to mine.");
/* 3534 */         performer.sendActionControl(Actions.actionEntrys[145].getVerbString(), true, time);
/*      */         
/* 3536 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 3537 */         performer.getStatus().modifyStamina(-1000.0F);
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 3543 */           time = performer.getCurrentAction().getTimeLeft();
/*      */         }
/* 3545 */         catch (NoSuchActionException nsa) {
/*      */           
/* 3547 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/* 3549 */         if (counter * 10.0F <= time && !insta) {
/*      */           
/* 3551 */           if (act.currentSecond() % 5 == 0 || (act.currentSecond() == 3 && time < 50))
/*      */           {
/* 3553 */             String sstring = "sound.work.mining1";
/* 3554 */             int i = Server.rand.nextInt(3);
/* 3555 */             if (i == 0) {
/* 3556 */               sstring = "sound.work.mining2";
/* 3557 */             } else if (i == 1) {
/* 3558 */               sstring = "sound.work.mining3";
/* 3559 */             }  SoundPlayer.playSound(sstring, digTilex, digTiley, performer.isOnSurface(), 0.0F);
/* 3560 */             source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 3561 */             performer.getStatus().modifyStamina(-7000.0F);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 3566 */           if (act.getRarity() != 0)
/*      */           {
/* 3568 */             performer.playPersonalSound("sound.fx.drumroll");
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 3573 */           if (cannotMineSlope(performer, mining, digTilex, digTiley)) {
/* 3574 */             return true;
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
/* 3585 */           double bonus = 0.0D;
/* 3586 */           double power = 0.0D;
/* 3587 */           done = true;
/* 3588 */           int itemTemplateCreated = 146;
/* 3589 */           float diff = 1.0F;
/*      */           
/* 3591 */           int caveTile = Server.caveMesh.getTile(digTilex, digTiley);
/* 3592 */           short caveFloor = Tiles.decodeHeight(caveTile);
/* 3593 */           int caveCeilingHeight = caveFloor + (short)(Tiles.decodeData(caveTile) & 0xFF);
/*      */           
/* 3595 */           MeshIO mesh = Server.surfaceMesh;
/* 3596 */           if (h - 1 <= caveCeilingHeight) {
/*      */             
/* 3598 */             performer.getCommunicator().sendNormalServerMessage("The rock sounds hollow. You need to tunnel to proceed.", (byte)3);
/*      */             
/* 3600 */             return true;
/*      */           } 
/* 3602 */           double imbueEnhancement = 1.0D + 0.23047D * source.getSkillSpellImprovement(1008) / 100.0D;
/*      */           
/* 3604 */           int lNewTile = mesh.getTile(digTilex - 1, digTiley);
/* 3605 */           short maxDiff = (short)(int)Math.max(10.0D, mining.getKnowledge(0.0D) * 3.0D * imbueEnhancement);
/* 3606 */           if (Terraforming.checkMineSurfaceTile(lNewTile, performer, h, maxDiff)) {
/* 3607 */             return true;
/*      */           }
/* 3609 */           lNewTile = mesh.getTile(digTilex + 1, digTiley);
/* 3610 */           if (Terraforming.checkMineSurfaceTile(lNewTile, performer, h, maxDiff)) {
/* 3611 */             return true;
/*      */           }
/* 3613 */           lNewTile = mesh.getTile(digTilex, digTiley - 1);
/* 3614 */           if (Terraforming.checkMineSurfaceTile(lNewTile, performer, h, maxDiff)) {
/* 3615 */             return true;
/*      */           }
/* 3617 */           lNewTile = mesh.getTile(digTilex, digTiley + 1);
/* 3618 */           if (Terraforming.checkMineSurfaceTile(lNewTile, performer, h, maxDiff)) {
/* 3619 */             return true;
/*      */           }
/* 3621 */           if (Terraforming.isAltarBlocking(performer, tilex, tiley)) {
/*      */             
/* 3623 */             performer.getCommunicator().sendSafeServerMessage("You cannot build here, since this is holy ground.", (byte)2);
/*      */             
/* 3625 */             return true;
/*      */           } 
/* 3627 */           if (performer.getTutorialLevel() == 10 && !performer.skippedTutorial())
/*      */           {
/* 3629 */             performer.missionFinished(true, true);
/*      */           }
/*      */           
/* 3632 */           float tickCounter = counter;
/*      */           
/* 3634 */           if (tool != null)
/* 3635 */             bonus = tool.skillCheck(1.0D, source, 0.0D, false, tickCounter) / 5.0D; 
/* 3636 */           power = Math.max(1.0D, mining
/* 3637 */               .skillCheck(1.0D, source, bonus, false, tickCounter));
/* 3638 */           float chance = Math.max(0.2F, (float)mining.getKnowledge(0.0D) / 200.0F);
/*      */           
/* 3640 */           if (Server.rand.nextFloat() < chance) {
/*      */ 
/*      */             
/*      */             try {
/* 3644 */               if (mining.getKnowledge(0.0D) * imbueEnhancement < power)
/* 3645 */                 power = mining.getKnowledge(0.0D) * imbueEnhancement; 
/* 3646 */               rockRandom.setSeed((digTilex + digTiley * Zones.worldTileSizeY) * 789221L);
/* 3647 */               int m = 100;
/* 3648 */               int max = Math.min(100, 
/* 3649 */                   (int)(20.0D + rockRandom.nextInt(80) * imbueEnhancement));
/* 3650 */               power = Math.min(power, max);
/* 3651 */               if (source.isCrude()) {
/* 3652 */                 power = 1.0D;
/*      */               }
/* 3654 */               float modifier = 1.0F;
/* 3655 */               if (source.getSpellEffects() != null)
/*      */               {
/* 3657 */                 modifier *= source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */               }
/*      */ 
/*      */               
/* 3661 */               float orePower = GeneralUtilities.calcOreRareQuality(power * modifier, act.getRarity(), source.getRarity());
/*      */               
/* 3663 */               Item newItem = ItemFactory.createItem(146, orePower, performer
/*      */                   
/* 3665 */                   .getPosX(), performer.getPosY(), Server.rand.nextFloat() * 360.0F, performer
/* 3666 */                   .isOnSurface(), act.getRarity(), -10L, null);
/* 3667 */               newItem.setLastOwnerId(performer.getWurmId());
/* 3668 */               newItem.setDataXY(tilex, tiley);
/* 3669 */               performer.getCommunicator().sendNormalServerMessage("You mine some " + newItem.getName() + ".");
/* 3670 */               Server.getInstance().broadCastAction(performer
/* 3671 */                   .getName() + " mines some " + newItem.getName() + ".", performer, 5);
/*      */               
/* 3673 */               TileEvent.log(digTilex, digTiley, 0, performer.getWurmId(), action);
/*      */ 
/*      */               
/* 3676 */               short newHeight = (short)(h - 1);
/* 3677 */               mesh.setTile(digTilex, digTiley, 
/* 3678 */                   Tiles.encode(newHeight, Tiles.Tile.TILE_ROCK.id, Tiles.decodeData(tile)));
/* 3679 */               Server.rockMesh.setTile(digTilex, digTiley, Tiles.encode(newHeight, (short)0));
/* 3680 */               for (int xx = 0; xx >= -1; xx--) {
/*      */                 
/* 3682 */                 for (int yy = 0; yy >= -1; yy--)
/*      */                 {
/* 3684 */                   performer.getMovementScheme().touchFreeMoveCounter();
/* 3685 */                   Players.getInstance().sendChangedTile(digTilex + xx, digTiley + yy, performer
/* 3686 */                       .isOnSurface(), true);
/*      */ 
/*      */ 
/*      */                   
/*      */                   try {
/* 3691 */                     Zone toCheckForChange = Zones.getZone(digTilex + xx, digTiley + yy, performer
/* 3692 */                         .isOnSurface());
/* 3693 */                     toCheckForChange.changeTile(digTilex + xx, digTiley + yy);
/*      */                   }
/* 3695 */                   catch (NoSuchZoneException nsz) {
/*      */                     
/* 3697 */                     logger.log(Level.INFO, "no such zone?: " + tilex + ", " + tiley, (Throwable)nsz);
/*      */                   }
/*      */                 
/*      */                 }
/*      */               
/*      */               } 
/* 3703 */             } catch (Exception ex) {
/*      */               
/* 3705 */               logger.log(Level.WARNING, "Factory failed to produce item", ex);
/*      */             } 
/*      */           } else {
/*      */             
/* 3709 */             performer.getCommunicator().sendNormalServerMessage("You chip away at the rock.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/* 3714 */       performer.getCommunicator().sendNormalServerMessage("The water is too deep to mine.", (byte)3);
/* 3715 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final byte prospect(int x, int y, boolean reprospecting) {
/* 3720 */     oreRand = Server.rand.nextInt(reprospecting ? 75 : 1000);
/* 3721 */     if (oreRand < 74) {
/*      */       
/* 3723 */       if (reprospecting) {
/*      */         
/* 3725 */         if (minezones[x / 32][y / 32] != Tiles.Tile.TILE_CAVE_WALL.id) {
/*      */           
/* 3727 */           if (Server.rand.nextInt(5) == 0)
/* 3728 */             return getOreId(oreRand); 
/* 3729 */           return minezones[x / 32][y / 32];
/*      */         } 
/*      */         
/* 3732 */         return getOreId(oreRand);
/*      */       } 
/*      */ 
/*      */       
/* 3736 */       if (Server.rand.nextInt(5) == 0) {
/* 3737 */         return getOreId(oreRand);
/*      */       }
/*      */       
/* 3740 */       byte type = minezones[x / 32][y / 32];
/*      */       
/* 3742 */       return type;
/*      */     } 
/*      */ 
/*      */     
/* 3746 */     return Tiles.Tile.TILE_CAVE_WALL.id;
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
/*      */   static boolean affectsHighway(int tilex, int tiley) {
/* 3760 */     if (MethodsHighways.onHighway(tilex, tiley - 1, true))
/* 3761 */       return true; 
/* 3762 */     if (MethodsHighways.onHighway(tilex + 1, tiley - 1, true))
/* 3763 */       return true; 
/* 3764 */     if (MethodsHighways.onHighway(tilex + 1, tiley, true))
/* 3765 */       return true; 
/* 3766 */     if (MethodsHighways.onHighway(tilex + 1, tiley + 1, true))
/* 3767 */       return true; 
/* 3768 */     if (MethodsHighways.onHighway(tilex, tiley + 1, true))
/* 3769 */       return true; 
/* 3770 */     if (MethodsHighways.onHighway(tilex - 1, tiley + 1, true))
/* 3771 */       return true; 
/* 3772 */     if (MethodsHighways.onHighway(tilex - 1, tiley, true))
/* 3773 */       return true; 
/* 3774 */     if (MethodsHighways.onHighway(tilex - 1, tiley - 1, true))
/* 3775 */       return true; 
/* 3776 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte getOreId(int num) {
/* 3781 */     if (num < 2)
/*      */     {
/* 3783 */       return Tiles.Tile.TILE_CAVE_WALL_ORE_GOLD.id;
/*      */     }
/* 3785 */     if (num < 6)
/*      */     {
/* 3787 */       return Tiles.Tile.TILE_CAVE_WALL_ORE_SILVER.id;
/*      */     }
/* 3789 */     if (num < 10)
/*      */     {
/* 3791 */       return Tiles.Tile.TILE_CAVE_WALL_ORE_COPPER.id;
/*      */     }
/* 3793 */     if (num < 14)
/*      */     {
/* 3795 */       return Tiles.Tile.TILE_CAVE_WALL_ORE_ZINC.id;
/*      */     }
/* 3797 */     if (num < 18)
/*      */     {
/* 3799 */       return Tiles.Tile.TILE_CAVE_WALL_ORE_LEAD.id;
/*      */     }
/* 3801 */     if (num < 22)
/*      */     {
/* 3803 */       return Tiles.Tile.TILE_CAVE_WALL_ORE_TIN.id;
/*      */     }
/* 3805 */     if (num < 72)
/*      */     {
/* 3807 */       return Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id;
/*      */     }
/* 3809 */     if (num < 73)
/*      */     {
/*      */       
/* 3812 */       return Tiles.Tile.TILE_CAVE_WALL_MARBLE.id;
/*      */     }
/* 3814 */     if (num < 74)
/*      */     {
/*      */       
/* 3817 */       return Tiles.Tile.TILE_CAVE_WALL_SLATE.id;
/*      */     }
/* 3819 */     return Tiles.Tile.TILE_CAVE_WALL.id;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final int getItemTemplateForTile(byte type) {
/* 3864 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_COPPER.id)
/* 3865 */       return 43; 
/* 3866 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_GOLD.id)
/* 3867 */       return 39; 
/* 3868 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id)
/* 3869 */       return 38; 
/* 3870 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_LEAD.id)
/* 3871 */       return 41; 
/* 3872 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_SILVER.id)
/* 3873 */       return 40; 
/* 3874 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_TIN.id)
/* 3875 */       return 207; 
/* 3876 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_ZINC.id)
/* 3877 */       return 42; 
/* 3878 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_ADAMANTINE.id)
/* 3879 */       return 693; 
/* 3880 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL.id)
/* 3881 */       return 697; 
/* 3882 */     if (type == Tiles.Tile.TILE_CAVE_WALL_MARBLE.id)
/* 3883 */       return 785; 
/* 3884 */     if (type == Tiles.Tile.TILE_CAVE_WALL_SLATE.id)
/* 3885 */       return 770; 
/* 3886 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id)
/* 3887 */       return 1238; 
/* 3888 */     if (type == Tiles.Tile.TILE_CAVE_WALL_SANDSTONE.id)
/* 3889 */       return 1116; 
/* 3890 */     return 146;
/*      */   }
/*      */ 
/*      */   
/*      */   static final int getDifficultyForTile(byte type) {
/* 3895 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_COPPER.id || type == Tiles.Tile.TILE_CAVE_WALL_SLATE.id)
/* 3896 */       return 20; 
/* 3897 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_GOLD.id || type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_MARBLE.id)
/*      */     {
/* 3899 */       return 40; } 
/* 3900 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id)
/* 3901 */       return 3; 
/* 3902 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_LEAD.id)
/* 3903 */       return 20; 
/* 3904 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_SILVER.id)
/* 3905 */       return 35; 
/* 3906 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_TIN.id)
/* 3907 */       return 10; 
/* 3908 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_ADAMANTINE.id)
/* 3909 */       return 60; 
/* 3910 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL.id)
/* 3911 */       return 55; 
/* 3912 */     if (type == Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id)
/* 3913 */       return 30; 
/* 3914 */     if (type == Tiles.Tile.TILE_CAVE_WALL_SANDSTONE.id)
/* 3915 */       return 45; 
/* 3916 */     return 2;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileRockBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
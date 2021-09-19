/*      */ package com.wurmonline.server.zones;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.NoSpaceException;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
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
/*      */ public final class Trap
/*      */   implements TimeConstants, SoundNames
/*      */ {
/*   59 */   private static final Logger logger = Logger.getLogger(Trap.class.getName());
/*      */   private static final String INSERT_TRAP = "INSERT INTO TRAPS(TYPE,QL,KINGDOM,VILLAGE,ID,FDAMQL,ROTDAMQL,SPEEDBON) VALUES (?,?,?,?,?,?,?,?)";
/*      */   private static final String UPDATE_TRAPS = "UPDATE TRAPS SET QL=QL-1";
/*      */   private static final String DELETE_TRAP = "DELETE FROM TRAPS WHERE ID=?";
/*      */   private static final String DELETE_DECAYED_TRAPS = "DELETE FROM TRAPS WHERE QL<=0";
/*      */   private static final String LOAD_ALL_TRAPS = "SELECT * FROM TRAPS";
/*   65 */   private static final Map<Integer, Trap> traps = new HashMap<>();
/*   66 */   private static final Map<Integer, Trap> quickTraps = new HashMap<>();
/*   67 */   private static long lastPolled = System.currentTimeMillis();
/*   68 */   private static long lastPolledQuick = System.currentTimeMillis();
/*      */   
/*      */   static final byte TYPE_STICKS = 0;
/*      */   static final byte TYPE_POLE = 1;
/*      */   static final byte TYPE_CORROSION = 2;
/*      */   static final byte TYPE_AXE = 3;
/*      */   static final byte TYPE_KNIFE = 4;
/*      */   static final byte TYPE_NET = 5;
/*      */   static final byte TYPE_SCYTHE = 6;
/*      */   static final byte TYPE_MAN = 7;
/*      */   static final byte TYPE_BOW = 8;
/*      */   static final byte TYPE_ROPE = 9;
/*      */   public static final byte TYPE_FORECAST = 10;
/*   81 */   private static final byte[] emptyPos = new byte[0];
/*   82 */   private static final byte[] feet = new byte[] { 11, 12, 16, 15 };
/*      */   
/*      */   private final byte type;
/*      */   
/*      */   private byte ql;
/*      */   
/*      */   private final byte kingdom;
/*      */   
/*      */   private final byte fdamql;
/*      */   
/*      */   private final byte rotdamql;
/*      */   
/*      */   private byte speedbon;
/*      */   
/*      */   private final int village;
/*      */   private final int id;
/*      */   
/*      */   public Trap(byte _type, byte _ql, byte _kingdom, int _village, int _id, byte _rotdamql, byte _fdamql, byte _speedbon) {
/*  100 */     this.type = _type;
/*  101 */     this.ql = _ql;
/*  102 */     this.kingdom = _kingdom;
/*  103 */     this.village = _village;
/*  104 */     this.id = _id;
/*  105 */     this.rotdamql = _rotdamql;
/*  106 */     this.fdamql = _fdamql;
/*  107 */     this.speedbon = _speedbon;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean setQl(byte newQl) {
/*  112 */     this.ql = newQl;
/*  113 */     return (this.ql <= 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkQuickUpdate() throws IOException {
/*  118 */     if (System.currentTimeMillis() - lastPolledQuick > 1000L) {
/*      */       
/*  120 */       lastPolledQuick = System.currentTimeMillis();
/*  121 */       Integer[] ints = (Integer[])quickTraps.keySet().toArray((Object[])new Integer[quickTraps.size()]);
/*      */       
/*  123 */       for (int x = 0; x < ints.length; x++) {
/*      */         
/*  125 */         Trap t = quickTraps.get(ints[x]);
/*  126 */         if (t.setQl((byte)(t.getQualityLevel() - 1))) {
/*      */           
/*  128 */           quickTraps.remove(ints[x]);
/*  129 */           traps.remove(ints[x]);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkUpdate() throws IOException {
/*  142 */     if (System.currentTimeMillis() - lastPolled > 21600000L) {
/*      */       
/*  144 */       lastPolled = System.currentTimeMillis();
/*  145 */       Connection dbcon = null;
/*  146 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  149 */         dbcon = DbConnector.getZonesDbCon();
/*  150 */         ps = dbcon.prepareStatement("UPDATE TRAPS SET QL=QL-1");
/*  151 */         ps.executeUpdate();
/*      */       }
/*  153 */       catch (SQLException sqx) {
/*      */         
/*  155 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  159 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  160 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*  162 */       Integer[] ints = (Integer[])traps.keySet().toArray((Object[])new Integer[traps.size()]);
/*  163 */       for (int x = 0; x < ints.length; x++) {
/*      */         
/*  165 */         Trap t = traps.get(ints[x]);
/*  166 */         if (t.setQl((byte)(t.getQualityLevel() - 1))) {
/*  167 */           traps.remove(ints[x]);
/*      */         }
/*      */       } 
/*      */       try {
/*  171 */         dbcon = DbConnector.getZonesDbCon();
/*  172 */         ps = dbcon.prepareStatement("DELETE FROM TRAPS WHERE QL<=0");
/*  173 */         ps.executeUpdate();
/*      */       }
/*  175 */       catch (SQLException sqx) {
/*      */         
/*  177 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  181 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  182 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int createId(int tilex, int tiley, int layer) {
/*  189 */     return (tilex << 17) - (tiley << 4) + (layer & 0xFF);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Trap getTrap(int tilex, int tiley, int layer) {
/*  194 */     return traps.get(Integer.valueOf(createId(tilex, tiley, layer)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void create() throws IOException {
/*  199 */     if (isQuick()) {
/*      */ 
/*      */       
/*  202 */       quickTraps.put(Integer.valueOf(this.id), this);
/*  203 */       traps.put(Integer.valueOf(this.id), this);
/*      */     }
/*      */     else {
/*      */       
/*  207 */       Connection dbcon = null;
/*  208 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  211 */         dbcon = DbConnector.getZonesDbCon();
/*  212 */         ps = dbcon.prepareStatement("INSERT INTO TRAPS(TYPE,QL,KINGDOM,VILLAGE,ID,FDAMQL,ROTDAMQL,SPEEDBON) VALUES (?,?,?,?,?,?,?,?)");
/*  213 */         ps.setByte(1, this.type);
/*  214 */         ps.setByte(2, this.ql);
/*  215 */         ps.setByte(3, this.kingdom);
/*  216 */         ps.setInt(4, this.village);
/*  217 */         ps.setInt(5, this.id);
/*  218 */         ps.setByte(6, this.fdamql);
/*  219 */         ps.setByte(7, this.rotdamql);
/*  220 */         ps.setByte(8, this.speedbon);
/*  221 */         ps.executeUpdate();
/*  222 */         traps.put(Integer.valueOf(this.id), this);
/*      */       }
/*  224 */       catch (SQLException sqx) {
/*      */         
/*  226 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  230 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  231 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void delete() throws IOException {
/*  238 */     quickTraps.remove(Integer.valueOf(this.id));
/*  239 */     traps.remove(Integer.valueOf(this.id));
/*  240 */     Connection dbcon = null;
/*  241 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  244 */       dbcon = DbConnector.getZonesDbCon();
/*  245 */       ps = dbcon.prepareStatement("DELETE FROM TRAPS WHERE ID=?");
/*  246 */       ps.setInt(1, this.id);
/*  247 */       ps.executeUpdate();
/*      */     }
/*  249 */     catch (SQLException sqx) {
/*      */       
/*  251 */       logger.log(Level.WARNING, "Problem deleting Trap id " + this.id, sqx);
/*  252 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  256 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  257 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isQuick() {
/*  263 */     return (this.type == 10);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadAllTraps() throws IOException {
/*  268 */     Connection dbcon = null;
/*  269 */     PreparedStatement ps = null;
/*  270 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  273 */       dbcon = DbConnector.getZonesDbCon();
/*  274 */       ps = dbcon.prepareStatement("SELECT * FROM TRAPS");
/*  275 */       rs = ps.executeQuery();
/*  276 */       while (rs.next()) {
/*      */         
/*  278 */         int id = rs.getInt("ID");
/*  279 */         byte type = rs.getByte("TYPE");
/*  280 */         byte kingdom = rs.getByte("KINGDOM");
/*  281 */         byte ql = rs.getByte("QL");
/*  282 */         int village = rs.getInt("VILLAGE");
/*  283 */         byte fdamql = rs.getByte("FDAMQL");
/*  284 */         byte rotdamql = rs.getByte("ROTDAMQL");
/*  285 */         byte speedbon = rs.getByte("SPEEDBON");
/*  286 */         Trap trap = new Trap(type, ql, kingdom, village, id, fdamql, rotdamql, speedbon);
/*  287 */         traps.put(Integer.valueOf(id), trap);
/*  288 */         if (trap.isQuick()) {
/*  289 */           quickTraps.put(Integer.valueOf(id), trap);
/*      */         }
/*      */       } 
/*  292 */     } catch (SQLException sqx) {
/*      */       
/*  294 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  298 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  299 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte getTypeForTemplate(int template) {
/*  305 */     if (template == 619)
/*  306 */       return 9; 
/*  307 */     if (template == 610)
/*  308 */       return 0; 
/*  309 */     if (template == 611)
/*  310 */       return 1; 
/*  311 */     if (template == 612)
/*  312 */       return 2; 
/*  313 */     if (template == 613)
/*  314 */       return 3; 
/*  315 */     if (template == 614)
/*  316 */       return 4; 
/*  317 */     if (template == 615)
/*  318 */       return 5; 
/*  319 */     if (template == 616)
/*  320 */       return 6; 
/*  321 */     if (template == 617)
/*  322 */       return 7; 
/*  323 */     if (template == 618)
/*  324 */       return 8; 
/*  325 */     logger.log(Level.INFO, "Unknown trap type for templateid " + template);
/*  326 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mayTrapRemainOnTile(byte tiletype) {
/*  331 */     if (this.type == 9)
/*  332 */       return isRopeTile(tiletype); 
/*  333 */     if (this.type == 0)
/*  334 */       return isSticksTile(tiletype); 
/*  335 */     if (this.type == 1)
/*  336 */       return isPoleTile(tiletype); 
/*  337 */     if (this.type == 2)
/*  338 */       return isCorrosionTile(tiletype); 
/*  339 */     if (this.type == 3)
/*  340 */       return isPoleTile(tiletype); 
/*  341 */     if (this.type == 4)
/*  342 */       return isSticksTile(tiletype); 
/*  343 */     if (this.type == 5)
/*  344 */       return isTreeTile(tiletype); 
/*  345 */     if (this.type == 6)
/*  346 */       return isRockTile(tiletype); 
/*  347 */     if (this.type == 7)
/*  348 */       return isSticksTile(tiletype); 
/*  349 */     if (this.type == 8)
/*  350 */       return (isPoleTile(tiletype) || isRockTile(tiletype)); 
/*  351 */     if (this.type == 10)
/*  352 */       return true; 
/*  353 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean mayTrapTemplateOnTile(int template, byte tiletype) {
/*  358 */     if (template == 619)
/*  359 */       return isRopeTile(tiletype); 
/*  360 */     if (template == 610)
/*  361 */       return isSticksTile(tiletype); 
/*  362 */     if (template == 611)
/*  363 */       return isPoleTile(tiletype); 
/*  364 */     if (template == 612)
/*  365 */       return isCorrosionTile(tiletype); 
/*  366 */     if (template == 613)
/*  367 */       return isPoleTile(tiletype); 
/*  368 */     if (template == 614)
/*  369 */       return isSticksTile(tiletype); 
/*  370 */     if (template == 615)
/*  371 */       return isTreeTile(tiletype); 
/*  372 */     if (template == 616)
/*  373 */       return isRockTile(tiletype); 
/*  374 */     if (template == 617)
/*  375 */       return isSticksTile(tiletype); 
/*  376 */     if (template == 618)
/*  377 */       return (isPoleTile(tiletype) || isRockTile(tiletype)); 
/*  378 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isRopeTile(byte type) {
/*  383 */     return (Tiles.getTile(type).isTree() || type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_PEAT.id || type == Tiles.Tile.TILE_TAR.id || type == Tiles.Tile.TILE_MOSS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_ENCHANTED_GRASS.id || type == Tiles.Tile.TILE_SAND.id || type == Tiles.Tile.TILE_DIRT.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isSticksTile(byte type) {
/*  391 */     return (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_PEAT.id || type == Tiles.Tile.TILE_TAR.id || type == Tiles.Tile.TILE_MOSS.id || type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_ENCHANTED_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id || type == Tiles.Tile.TILE_SAND.id || type == Tiles.Tile.TILE_DIRT.id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isPoleTile(byte type) {
/*  400 */     return (Tiles.getTile(type).isTree() || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_TAR.id);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isTreeTile(byte type) {
/*  405 */     return Tiles.getTile(type).isTree();
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isCorrosionTile(byte type) {
/*  410 */     return Tiles.getTile(type).isTree();
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isRockTile(byte type) {
/*  415 */     return (type == Tiles.Tile.TILE_CAVE.id || type == Tiles.Tile.TILE_CAVE_EXIT.id || type == Tiles.Tile.TILE_ROCK.id);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean mayPlantCorrosion(int tilex, int tiley, int layer) {
/*  420 */     if (layer < 0) {
/*  421 */       return true;
/*      */     }
/*  423 */     VolaTile t = Zones.getTileOrNull(tilex, tiley, (layer >= 0));
/*  424 */     if (t != null)
/*      */     {
/*  426 */       if (t.getStructure() != null && t.getStructure().isFinished())
/*  427 */         return true; 
/*      */     }
/*  429 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean disarm(Creature performer, Item disarmItem, int tilex, int tiley, int layer, float counter, Action act) {
/*  435 */     boolean toReturn = true;
/*  436 */     if (disarmItem.isDisarmTrap()) {
/*      */       
/*  438 */       double power = 0.0D;
/*  439 */       int time = 2000;
/*      */       
/*  441 */       toReturn = false;
/*  442 */       if (counter == 1.0F) {
/*      */         
/*  444 */         Skill trapping = null;
/*      */         
/*      */         try {
/*  447 */           trapping = performer.getSkills().getSkill(10084);
/*      */         }
/*  449 */         catch (NoSuchSkillException nss) {
/*      */           
/*  451 */           trapping = performer.getSkills().learn(10084, 1.0F);
/*      */         } 
/*  453 */         time = Actions.getStandardActionTime(performer, trapping, disarmItem, 0.0D);
/*  454 */         act.setTimeLeft(time);
/*  455 */         performer.getCommunicator().sendNormalServerMessage("You try to trigger any traps in the area.");
/*  456 */         Server.getInstance().broadCastAction(performer.getName() + " starts to trigger traps in the area.", performer, 5);
/*      */         
/*  458 */         performer.sendActionControl(Actions.actionEntrys[375].getVerbString(), true, time);
/*      */       }
/*      */       else {
/*      */         
/*  462 */         time = act.getTimeLeft();
/*      */       } 
/*  464 */       if (counter * 10.0F > time) {
/*      */         
/*  466 */         Skill trapping = null;
/*      */         
/*      */         try {
/*  469 */           trapping = performer.getSkills().getSkill(10084);
/*      */         }
/*  471 */         catch (NoSuchSkillException nss) {
/*      */           
/*  473 */           trapping = performer.getSkills().learn(10084, 1.0F);
/*      */         } 
/*  475 */         power = trapping.skillCheck(10.0D, disarmItem.getCurrentQualityLevel(), true, counter);
/*  476 */         toReturn = true;
/*  477 */         if (power > 0.0D) {
/*      */           
/*  479 */           Trap t = getTrap(tilex, tiley, performer.getLayer());
/*  480 */           if (t != null) {
/*      */ 
/*      */             
/*      */             try {
/*  484 */               t.delete();
/*      */             }
/*  486 */             catch (IOException iox) {
/*      */               
/*  488 */               performer.getCommunicator().sendNormalServerMessage("You detect a " + t
/*  489 */                   .getName() + " but nothing happens. It may still be armed.");
/*  490 */               return true;
/*      */             } 
/*      */             
/*  493 */             power = trapping.skillCheck(10.0D, disarmItem.getCurrentQualityLevel(), false, counter);
/*  494 */             SoundPlayer.playSound(t.getSound(), tilex, tiley, performer.isOnSurface(), 0.0F);
/*      */             
/*  496 */             String tosend = "You trigger a  " + t.getName() + "!";
/*  497 */             performer.getStatus().modifyStamina(1000.0F);
/*  498 */             performer.getCommunicator().sendNormalServerMessage(tosend);
/*  499 */             Server.getInstance().broadCastAction(performer.getName() + " trigger a " + t.getName() + ".", performer, 5);
/*      */             
/*  501 */             Items.destroyItem(disarmItem.getWurmId());
/*      */           } else {
/*      */             
/*  504 */             performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*      */           } 
/*      */         } else {
/*  507 */           performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  512 */       performer.getCommunicator().sendNormalServerMessage("You can not disarm traps safely with " + disarmItem
/*  513 */           .getName() + ".");
/*      */     } 
/*  515 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private String getSound() {
/*  520 */     return getSoundForTrapType(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   static String getSoundForTrapType(byte type) {
/*  525 */     switch (type) {
/*      */       
/*      */       case 0:
/*  528 */         return "sound.trap.chak";
/*      */       case 1:
/*  530 */         return "sound.trap.thuk";
/*      */       case 2:
/*  532 */         return "sound.trap.splash";
/*      */       case 3:
/*  534 */         return "sound.trap.thuk";
/*      */       case 4:
/*  536 */         return "sound.trap.wham";
/*      */       case 5:
/*  538 */         return "sound.trap.swish";
/*      */       case 6:
/*  540 */         return "sound.trap.scith";
/*      */       case 7:
/*  542 */         return "sound.trap.chak";
/*      */       case 9:
/*  544 */         return "sound.trap.swish";
/*      */       case 8:
/*  546 */         return "sound.trap.thuk";
/*      */     } 
/*  548 */     return "sound.trap.thuk";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static byte getDamageForTrapType(byte type) {
/*  554 */     switch (type) {
/*      */       
/*      */       case 0:
/*  557 */         return 2;
/*      */       case 1:
/*  559 */         return 2;
/*      */       case 2:
/*  561 */         return 10;
/*      */       case 3:
/*  563 */         return 1;
/*      */       case 4:
/*  565 */         return 2;
/*      */       case 5:
/*  567 */         return -1;
/*      */       case 6:
/*  569 */         return 1;
/*      */       case 7:
/*  571 */         return 1;
/*      */       case 9:
/*  573 */         return -1;
/*      */       case 8:
/*  575 */         return 2;
/*      */       case 10:
/*  577 */         return 10;
/*      */     } 
/*  579 */     return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  585 */     return getNameForTrapType(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getKingdom() {
/*  590 */     return this.kingdom;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getVillage() {
/*  595 */     return this.village;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getFireDamage() {
/*  600 */     return this.fdamql;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getRotDamage() {
/*  605 */     return this.rotdamql;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getSpeedBon() {
/*  610 */     return this.speedbon;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getQualityLevel() {
/*  615 */     return this.ql;
/*      */   }
/*      */ 
/*      */   
/*      */   public void doEffect(Creature performer, int tilex, int tiley, int layer) {
/*  620 */     SoundPlayer.playSound(getSound(), tilex, tiley, (layer >= 0), 0.0F);
/*      */     
/*      */     try {
/*  623 */       delete();
/*      */     }
/*  625 */     catch (IOException iox) {
/*      */       
/*  627 */       performer.getCommunicator().sendNormalServerMessage("A " + 
/*  628 */           getName() + " triggers but nothing happens. It may still be armed.");
/*      */       return;
/*      */     } 
/*  631 */     if (this.speedbon == 0) {
/*  632 */       this.speedbon = 2;
/*  633 */     } else if (performer.getCultist() != null && performer.getCultist().ignoresTraps()) {
/*      */       
/*  635 */       performer.getCommunicator().sendSafeServerMessage("A " + getName() + " triggers but you easily avoid it!");
/*      */     }
/*  637 */     else if (performer.getBodyControlSkill().skillCheck(Server.rand.nextInt(100 + this.speedbon / 2), -this.ql, false, 10.0F) > 0.0D) {
/*      */       
/*  639 */       performer.getCommunicator().sendSafeServerMessage("A " + getName() + " triggers but you manage to avoid it!");
/*      */     }
/*      */     else {
/*      */       
/*  643 */       byte damtype = getDamageForTrapType(this.type);
/*  644 */       if (damtype == -1) {
/*      */         
/*  646 */         performer.getCommunicator().sendAlertServerMessage("A " + 
/*  647 */             getName() + " entangles you! Breaking free tires you.");
/*  648 */         performer.getStatus().modifyStamina2(-this.ql / 100.0F);
/*      */       } else {
/*      */         int nums; byte[] poses; float percentSpell;
/*      */         int baseDam;
/*  652 */         performer.getCommunicator()
/*  653 */           .sendAlertServerMessage("A " + getName() + " triggers and hits you with full force!");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  659 */         byte trapType = 54;
/*      */         
/*  661 */         switch (this.type) {
/*      */ 
/*      */           
/*      */           case 0:
/*  665 */             nums = Server.rand.nextInt(this.ql / 10) + 1;
/*  666 */             poses = emptyPos;
/*  667 */             percentSpell = 0.2F;
/*  668 */             baseDam = 150;
/*  669 */             trapType = 54;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 1:
/*  674 */             nums = 1;
/*  675 */             poses = emptyPos;
/*  676 */             percentSpell = 1.0F;
/*  677 */             baseDam = 250;
/*  678 */             trapType = 55;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 2:
/*  683 */             nums = Server.rand.nextInt(this.ql / 10) + 1;
/*  684 */             poses = emptyPos;
/*  685 */             percentSpell = 0.2F;
/*  686 */             baseDam = 150;
/*  687 */             trapType = 56;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 3:
/*  692 */             nums = 1;
/*  693 */             poses = emptyPos;
/*  694 */             percentSpell = 1.0F;
/*  695 */             baseDam = 300;
/*  696 */             trapType = 57;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 4:
/*  701 */             nums = Server.rand.nextInt(this.ql / 5) + 1;
/*  702 */             poses = emptyPos;
/*  703 */             percentSpell = 0.3F;
/*  704 */             baseDam = 200;
/*  705 */             trapType = 58;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 6:
/*  710 */             nums = 1;
/*  711 */             poses = emptyPos;
/*  712 */             percentSpell = 1.0F;
/*  713 */             baseDam = 350;
/*  714 */             trapType = 60;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 7:
/*  719 */             nums = 1;
/*  720 */             poses = feet;
/*  721 */             percentSpell = 1.0F;
/*  722 */             baseDam = 300;
/*  723 */             trapType = 61;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 8:
/*  728 */             nums = 1;
/*  729 */             poses = emptyPos;
/*  730 */             percentSpell = 1.0F;
/*  731 */             baseDam = 300;
/*  732 */             trapType = 62;
/*      */             break;
/*      */ 
/*      */           
/*      */           case 10:
/*  737 */             nums = 1;
/*  738 */             poses = emptyPos;
/*  739 */             percentSpell = 1.0F;
/*  740 */             baseDam = 300;
/*  741 */             trapType = 71;
/*      */             break;
/*      */           
/*      */           default:
/*  745 */             nums = 0;
/*  746 */             poses = emptyPos;
/*  747 */             percentSpell = 0.0F;
/*  748 */             baseDam = 300;
/*      */             break;
/*      */         } 
/*  751 */         VolaTile t = performer.getCurrentTile();
/*  752 */         if (t != null)
/*      */         {
/*  754 */           t.sendAddQuickTileEffect(trapType, layer * 30);
/*      */         }
/*      */         
/*  757 */         if (performer.isUnique())
/*      */         {
/*  759 */           baseDam = (int)(baseDam * 0.3F);
/*      */         }
/*  761 */         addDamage(performer, baseDam, nums, damtype, poses, percentSpell);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addDamage(Creature creature, int baseDam, int nums, byte damtype, byte[] positions, float percentSpellDamage) {
/*  769 */     for (int x = 0; x < nums; x++) {
/*      */ 
/*      */       
/*      */       try {
/*  773 */         byte pos = creature.getBody().getRandomWoundPos();
/*  774 */         if (positions.length > 0)
/*  775 */           pos = positions[Server.rand.nextInt(positions.length)]; 
/*  776 */         float armourMod = 1.0F;
/*      */ 
/*      */         
/*      */         try {
/*  780 */           byte bodyPosition = ArmourTemplate.getArmourPosition(pos);
/*  781 */           Item armour = creature.getArmour(bodyPosition);
/*  782 */           armourMod = ArmourTemplate.calculateDR(armour, damtype);
/*  783 */           if (creature.isPlayer())
/*      */           {
/*  785 */             armour.setDamage(armour.getDamage() + 
/*  786 */                 Math.max(0.05F, 
/*      */                   
/*  788 */                   Math.min(1.0F, (baseDam * this.ql) * ArmourTemplate.getArmourDamageModFor(armour, damtype) / 1200000.0F * armour
/*  789 */                     .getDamageModifier())));
/*      */           }
/*      */         }
/*  792 */         catch (NoArmourException noArmourException) {
/*      */ 
/*      */         
/*      */         }
/*  796 */         catch (NoSpaceException nsp) {
/*      */           
/*  798 */           logger.log(Level.WARNING, creature.getName() + " no armour space on loc " + pos);
/*      */         } 
/*  800 */         if (creature.getBonusForSpellEffect((byte)22) > 0.0F)
/*      */         {
/*  802 */           if (armourMod >= 1.0F) {
/*  803 */             armourMod = 0.2F + (1.0F - creature.getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F;
/*      */           } else {
/*  805 */             armourMod = Math.min(armourMod, 0.2F + (1.0F - creature
/*  806 */                 .getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F);
/*      */           }  } 
/*  808 */         if (!creature.isDead()) {
/*      */           
/*  810 */           CombatEngine.addWound(null, creature, damtype, pos, (this.ql * baseDam), armourMod, "hits", null, 0.0F, 0.0F, false, false, false, false);
/*      */         } else {
/*      */           return;
/*      */         } 
/*      */         
/*  815 */         if (Server.rand.nextFloat() < percentSpellDamage) {
/*      */           
/*  817 */           if (!creature.isDead() && this.rotdamql > 0) {
/*      */             
/*  819 */             creature.addWoundOfType(null, (byte)6, pos, false, 1.0F, true, ((this.rotdamql * baseDam) / 10.0F), this.ql, 0.0F, false, false);
/*      */           } else {
/*      */             return;
/*      */           } 
/*      */           
/*  824 */           if (!creature.isDead() && this.fdamql > 0) {
/*      */             
/*  826 */             creature.addWoundOfType(null, (byte)4, pos, false, 1.0F, true, ((this.fdamql * baseDam) / 10.0F), 0.0F, 0.0F, false, false);
/*      */           }
/*      */           else {
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*  833 */       } catch (Exception ex) {
/*      */         
/*  835 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static String getNameForTrapType(byte type) {
/*  843 */     switch (type) {
/*      */       
/*      */       case 0:
/*  846 */         return "stick trap";
/*      */       case 1:
/*  848 */         return "pole trap";
/*      */       case 2:
/*  850 */         return "corrosion trap";
/*      */       case 3:
/*  852 */         return "axe trap";
/*      */       case 4:
/*  854 */         return "knife trap";
/*      */       case 5:
/*  856 */         return "net trap";
/*      */       case 6:
/*  858 */         return "scythe trap";
/*      */       case 7:
/*  860 */         return "man trap";
/*      */       case 9:
/*  862 */         return "rope trap";
/*      */       case 8:
/*  864 */         return "bow trap";
/*      */     } 
/*  866 */     return "trap";
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
/*      */   public static String getQualityLevelString(byte qualityLevel) {
/*      */     String qlString;
/*  880 */     if (qualityLevel < 20) {
/*  881 */       qlString = "low";
/*  882 */     } else if (qualityLevel > 80) {
/*  883 */       qlString = "deadly";
/*  884 */     } else if (qualityLevel > 50) {
/*  885 */       qlString = "high";
/*      */     } else {
/*  887 */       qlString = "average";
/*      */     } 
/*  889 */     return qlString;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean trap(Creature performer, Item trap, int tile, int tilex, int tiley, int layer, float counter, Action act) {
/*  895 */     boolean toReturn = true;
/*  896 */     boolean ok = false;
/*  897 */     if (trap.isTrap())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  906 */       if (mayTrapTemplateOnTile(trap.getTemplateId(), Tiles.decodeType(tile))) {
/*      */ 
/*      */         
/*  909 */         if (getTrap(tilex, tiley, performer.getLayer()) == null)
/*      */         {
/*  911 */           ok = true;
/*      */         }
/*      */       }
/*  914 */       else if (trap.getTemplateId() == 612) {
/*      */         
/*  916 */         if (mayPlantCorrosion(tilex, tiley, performer.getLayer()))
/*      */         {
/*  918 */           if (getTrap(tilex, tiley, performer.getLayer()) == null)
/*      */           {
/*  920 */             ok = true;
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*  925 */     if (ok) {
/*      */       
/*  927 */       double power = 0.0D;
/*  928 */       int time = 2000;
/*      */       
/*  930 */       toReturn = false;
/*  931 */       if (counter == 1.0F) {
/*      */         
/*  933 */         Skill trapping = null;
/*      */         
/*      */         try {
/*  936 */           trapping = performer.getSkills().getSkill(10084);
/*      */         }
/*  938 */         catch (NoSuchSkillException nss) {
/*      */           
/*  940 */           trapping = performer.getSkills().learn(10084, 1.0F);
/*      */         } 
/*  942 */         if (Terraforming.isCornerUnderWater(tilex, tiley, performer.isOnSurface())) {
/*      */           
/*  944 */           performer.getCommunicator().sendNormalServerMessage("The ground is too moist here, the trap would not work.");
/*      */           
/*  946 */           return true;
/*      */         } 
/*  948 */         time = Actions.getSlowActionTime(performer, trapping, trap, 0.0D);
/*  949 */         act.setTimeLeft(time);
/*  950 */         performer.getCommunicator().sendNormalServerMessage("You start setting the " + trap.getName() + ".");
/*  951 */         Server.getInstance().broadCastAction(performer.getName() + " starts to set " + trap.getNameWithGenus() + ".", performer, 5);
/*      */         
/*  953 */         performer.sendActionControl(Actions.actionEntrys[374].getVerbString(), true, time);
/*      */       }
/*      */       else {
/*      */         
/*  957 */         time = act.getTimeLeft();
/*      */       } 
/*  959 */       if (counter * 10.0F > time)
/*      */       {
/*  961 */         Skill trapping = null;
/*      */         
/*      */         try {
/*  964 */           trapping = performer.getSkills().getSkill(10084);
/*      */         }
/*  966 */         catch (NoSuchSkillException nss) {
/*      */           
/*  968 */           trapping = performer.getSkills().learn(10084, 1.0F);
/*      */         } 
/*  970 */         power = trapping.skillCheck((trap.getCurrentQualityLevel() / 5.0F), trap.getCurrentQualityLevel(), false, counter);
/*  971 */         toReturn = true;
/*  972 */         if (power > 0.0D) {
/*      */           
/*  974 */           SoundPlayer.playSound("sound.trap.set", tilex, tiley, performer.isOnSurface(), 0.0F);
/*      */           
/*  976 */           byte type = getTypeForTemplate(trap.getTemplateId());
/*      */           
/*  978 */           int villid = 0;
/*  979 */           if (performer.getCitizenVillage() != null) {
/*  980 */             villid = (performer.getCitizenVillage()).id;
/*      */           }
/*      */ 
/*      */           
/*      */           try {
/*  985 */             Trap t = new Trap(type, (byte)(int)trap.getCurrentQualityLevel(), performer.getKingdomId(), villid, createId(tilex, tiley, layer), (byte)(int)trap.getSpellDamageBonus(), (byte)(int)trap.getSpellRotModifier(), (byte)(int)trap.getSpellSpeedBonus());
/*  986 */             t.create();
/*      */           }
/*  988 */           catch (IOException iox) {
/*      */             
/*  990 */             performer.getCommunicator().sendNormalServerMessage("Something goes awry! You sense bad omens and can not set traps right now.");
/*      */             
/*  992 */             return true;
/*      */           } 
/*  994 */           String tosend = "You carefully set the " + trap.getName() + ".";
/*  995 */           performer.getStatus().modifyStamina(-1000.0F);
/*  996 */           performer.getCommunicator().sendNormalServerMessage(tosend);
/*  997 */           Server.getInstance().broadCastAction(performer.getName() + " sets " + trap.getNameWithGenus() + ".", performer, 5);
/*      */         }
/*      */         else {
/*      */           
/* 1001 */           performer.getCommunicator().sendNormalServerMessage("Sadly, you fail to set the trap correctly. The trap triggers and is destroyed.");
/*      */         } 
/* 1003 */         Items.destroyItem(trap.getWurmId());
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1008 */       performer.getCommunicator().sendNormalServerMessage("You can not trap that place.");
/*      */     } 
/* 1010 */     return toReturn;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Trap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
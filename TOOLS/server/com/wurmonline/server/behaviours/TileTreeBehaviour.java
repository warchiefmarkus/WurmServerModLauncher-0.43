/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.BushData;
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.mesh.TreeData;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.PlonkData;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmHarvestables;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.Trap;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
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
/*      */ public class TileTreeBehaviour
/*      */   extends TileBehaviour
/*      */ {
/*   72 */   private static Logger logger = Logger.getLogger(TileTreeBehaviour.class.getName());
/*   73 */   private static final Logger cheatlogger = Logger.getLogger("Cheaters");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TileTreeBehaviour() {
/*   80 */     super((short)7);
/*      */   }
/*      */ 
/*      */   
/*      */   public TileTreeBehaviour(short type) {
/*   85 */     super(type);
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
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/*   97 */     PlonkData.TREE_ACTIONS.trigger(performer);
/*   98 */     List<ActionEntry> toReturn = new LinkedList<>();
/*   99 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, tile));
/*  100 */     byte type = Tiles.decodeType(tile);
/*  101 */     byte data = Tiles.decodeData(tile);
/*  102 */     Tiles.Tile theTile = Tiles.getTile(type);
/*  103 */     int age = data >> 4 & 0xF;
/*  104 */     if (theTile.isNormalTree())
/*      */     {
/*  106 */       if (performer.getDeity() != null && performer.getDeity().isForestGod())
/*      */       {
/*  108 */         if (isPrayingAge(age))
/*      */         {
/*  110 */           toReturn.add(Actions.actionEntrys[141]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  115 */     List<ActionEntry> nature = getNatureActions(performer, (Item)null, tilex, tiley, theTile, data);
/*  116 */     toReturn.addAll(getNatureMenu(performer, tilex, tiley, type, data, nature));
/*      */     
/*  118 */     return toReturn;
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
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile) {
/*  131 */     PlonkData.TREE_ACTIONS.trigger(performer);
/*  132 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  133 */     toReturn.addAll(super.getBehavioursFor(performer, subject, tilex, tiley, onSurface, tile));
/*  134 */     byte type = Tiles.decodeType(tile);
/*  135 */     byte data = Tiles.decodeData(tile);
/*  136 */     Tiles.Tile theTile = Tiles.getTile(type);
/*      */     
/*  138 */     int age = data >> 4 & 0xF;
/*      */     
/*  140 */     if (subject.isWeaponSlash() || subject.getTemplateId() == 24) {
/*      */       
/*  142 */       toReturn.add(Actions.actionEntrys[96]);
/*      */     }
/*  144 */     else if (subject.getTemplateId() == 526) {
/*      */       
/*  146 */       if (theTile.isNormalTree())
/*      */       {
/*  148 */         toReturn.add(Actions.actionEntrys[118]);
/*      */       }
/*      */     } 
/*  151 */     if (theTile.isNormalTree())
/*      */     {
/*  153 */       if (performer.getDeity() != null && performer.getDeity().isForestGod())
/*      */       {
/*  155 */         if (isPrayingAge(age))
/*      */         {
/*  157 */           toReturn.add(Actions.actionEntrys[141]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  162 */     List<ActionEntry> nature = getNatureActions(performer, subject, tilex, tiley, theTile, data);
/*  163 */     toReturn.addAll(getNatureMenu(performer, tilex, tiley, type, data, nature));
/*      */     
/*  165 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ActionEntry> getNatureActions(Creature performer, @Nullable Item tool, int tilex, int tiley, Tiles.Tile theTile, byte data) {
/*  171 */     List<ActionEntry> toReturn = new LinkedList<>();
/*      */     
/*  173 */     int age = data >> 4 & 0xF;
/*  174 */     TreeData.TreeType treeType = theTile.getTreeType(data);
/*  175 */     if (tool != null) {
/*      */       
/*  177 */       if (tool.getTemplateId() == 267 && !theTile.isEnchanted()) {
/*      */         
/*  179 */         if (performer.isWithinTileDistanceTo(tilex, tiley, 
/*  180 */             (int)(performer.getStatus().getPositionZ() + performer.getAltOffZ()) >> 2, 1)) {
/*      */           
/*  182 */           if (theTile != Tiles.Tile.TILE_BUSH_LINGONBERRY && isSproutingAge(age))
/*      */           {
/*  184 */             toReturn.add(Actions.actionEntrys[187]);
/*      */           }
/*  186 */           if (theTile.isTree())
/*      */           {
/*  188 */             if (hasFruit(performer, tilex, tiley, age) && treeType.isFruitTree()) {
/*  189 */               toReturn.add(Actions.actionEntrys[152]);
/*  190 */             } else if (treeType == TreeData.TreeType.CHESTNUT && hasFruit(performer, tilex, tiley, age)) {
/*  191 */               toReturn.add(Actions.actionEntrys[152]);
/*  192 */             } else if (treeType == TreeData.TreeType.WALNUT && hasFruit(performer, tilex, tiley, age)) {
/*  193 */               toReturn.add(Actions.actionEntrys[152]);
/*  194 */             } else if (treeType == TreeData.TreeType.PINE && hasFruit(performer, tilex, tiley, age)) {
/*  195 */               toReturn.add(Actions.actionEntrys[152]);
/*  196 */             } else if (treeType == TreeData.TreeType.OAK && hasFruit(performer, tilex, tiley, age)) {
/*  197 */               toReturn.add(Actions.actionEntrys[152]);
/*  198 */             }  if (age == 3 || age == 4 || age == 13 || age == 14)
/*      */             {
/*  200 */               toReturn.add(Actions.actionEntrys[373]);
/*      */             }
/*      */           }
/*  203 */           else if (theTile.isBush())
/*      */           {
/*      */             
/*  206 */             if (hasFruit(performer, tilex, tiley, age)) {
/*      */               
/*  208 */               toReturn.add(Actions.actionEntrys[152]);
/*      */             }
/*  210 */             else if (Servers.isThisATestServer()) {
/*  211 */               toReturn.add(Actions.actionEntrys[152]);
/*      */             } 
/*  213 */             if (age == 3 || age == 4 || age == 13 || age == 14 || (age == 15 && theTile.isThorn(data)))
/*      */             {
/*  215 */               toReturn.add(Actions.actionEntrys[373]);
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*  220 */       } else if (tool.getTemplateId() == 421 && theTile.isNormalTree() && !treeType.isFruitTree()) {
/*      */         
/*  222 */         if (performer.isWithinTileDistanceTo(tilex, tiley, 
/*  223 */             (int)(performer.getStatus().getPositionZ() + performer.getAltOffZ()) >> 2, 1))
/*      */         {
/*  225 */           if (theTile.isMaple(data))
/*      */           {
/*  227 */             if (hasFruit(performer, tilex, tiley, age)) {
/*  228 */               toReturn.add(Actions.actionEntrys[152]);
/*  229 */             } else if (Servers.isThisATestServer()) {
/*  230 */               toReturn.add(Actions.actionEntrys[152]);
/*      */             }  } 
/*      */         }
/*      */       } 
/*  234 */       if (performer.getPower() >= 2 && tool.getTemplateId() == 176)
/*      */       {
/*  236 */         toReturn.add(Actions.actionEntrys[188]);
/*      */       }
/*  238 */       GrassData.GrowthTreeStage growthStage = GrassData.GrowthTreeStage.decodeTileData(data);
/*      */       
/*  240 */       if (!theTile.isEnchanted())
/*      */       {
/*  242 */         if (growthStage != GrassData.GrowthTreeStage.LAWN && theTile != Tiles.Tile.TILE_BUSH_LINGONBERRY)
/*      */         {
/*  244 */           if (theTile.isMycelium()) {
/*      */ 
/*      */             
/*  247 */             if (tool.getTemplateId() == 394 || (tool
/*  248 */               .getTemplateId() == 176 && performer.getPower() >= 2))
/*      */             {
/*  250 */               toReturn.add(new ActionEntry((short)644, "Trim mycelium", "Trimming mycelium"));
/*      */             }
/*      */           }
/*  253 */           else if (growthStage == GrassData.GrowthTreeStage.SHORT) {
/*      */             
/*  255 */             if (tool.getTemplateId() == 394 || (tool
/*  256 */               .getTemplateId() == 176 && performer.getPower() >= 2))
/*      */             {
/*  258 */               toReturn.add(new ActionEntry((short)644, "Trim grass", "Trimming grass"));
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  263 */           else if (tool.getTemplate().isSharp()) {
/*      */             
/*  265 */             toReturn.add(new ActionEntry((short)645, "Cut grass", "Cutting grass"));
/*      */           } 
/*      */         }
/*      */       }
/*      */       
/*  270 */       if (theTile.isTree() && age == 15 && tool.getTemplateId() == 390)
/*      */       {
/*  272 */         toReturn.add(new ActionEntry((short)935, "Search for grubs", "searching"));
/*      */       }
/*      */     } 
/*      */     
/*  276 */     if (theTile.isNormalTree() || theTile.isNormalBush())
/*      */     {
/*  278 */       if (performer.getCultist() != null && performer.getCultist().mayEnchantNature())
/*      */       {
/*  280 */         toReturn.add(Actions.actionEntrys[388]);
/*      */       }
/*      */     }
/*  283 */     boolean canGrub = Server.hasGrubs(tilex, tiley);
/*  284 */     if (theTile.isBush() && age == 14 && canGrub)
/*      */     {
/*  286 */       toReturn.add(new ActionEntry((short)935, "Search for twigs", "searching"));
/*      */     }
/*  288 */     if (theTile.isTree() && treeType.getTypeId() == TreeData.TreeType.BIRCH.getTypeId() && age == 14 && canGrub)
/*      */     {
/*  290 */       toReturn.add(new ActionEntry((short)935, "Search for loose bark", "searching"));
/*      */     }
/*  292 */     if (theTile.canBearFruit() && hasFruit(performer, tilex, tiley, age))
/*      */     {
/*  294 */       toReturn.add(new ActionEntry((short)852, "Study", "making notes"));
/*      */     }
/*  296 */     return toReturn;
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
/*      */   static boolean isPrayingAge(int age) {
/*  308 */     return (age > FoliageAge.VERY_OLD_SPROUTING.getAgeId());
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
/*      */   static boolean isSproutingAge(int age) {
/*  320 */     return (age == FoliageAge.MATURE_SPROUTING.getAgeId() || age == FoliageAge.OLD_ONE_SPROUTING.getAgeId() || age == FoliageAge.OLD_TWO_SPROUTING
/*  321 */       .getAgeId() || age == FoliageAge.VERY_OLD_SPROUTING.getAgeId());
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
/*      */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, short action, float counter) {
/*  333 */     boolean done = true;
/*  334 */     byte tiletype = Tiles.decodeType(tile);
/*  335 */     Tiles.Tile theTile = Tiles.getTile(tiletype);
/*  336 */     byte data = Tiles.decodeData(tile);
/*  337 */     int age = data >> 4 & 0xF;
/*      */     
/*  339 */     if (action == 1) {
/*      */       
/*  341 */       Communicator comm = performer.getCommunicator();
/*  342 */       String ageString = "";
/*  343 */       if (theTile.isMycelium()) {
/*      */         
/*  345 */         ageString = "an infected ";
/*      */       }
/*  347 */       else if (theTile.isEnchanted()) {
/*      */         
/*  349 */         ageString = "an enchanted ";
/*      */       }
/*  351 */       else if (age < 8 || (age >= 12 && age < 14)) {
/*  352 */         ageString = "a ";
/*      */       } else {
/*  354 */         ageString = "an ";
/*      */       } 
/*  356 */       if (age < 4) {
/*  357 */         ageString = ageString + "young";
/*  358 */       } else if (age < 8) {
/*  359 */         ageString = ageString + "mature";
/*  360 */       } else if (age < 12) {
/*  361 */         ageString = ageString + "old";
/*  362 */       } else if (age < 14) {
/*  363 */         ageString = ageString + "very old";
/*  364 */       } else if (age == 14) {
/*  365 */         ageString = ageString + "overaged";
/*  366 */       } else if (age == 15) {
/*  367 */         ageString = ageString + "old and shriveled";
/*  368 */       }  if (performer.getPower() > 3) {
/*  369 */         ageString = ageString + " (" + age + ")";
/*      */       }
/*  371 */       int dam = Server.getWorldResource(tilex, tiley);
/*  372 */       if (dam == 65535) {
/*      */         
/*  374 */         Server.setWorldResource(tilex, tiley, 0);
/*  375 */         dam = 0;
/*      */       } 
/*  377 */       String damage = "";
/*  378 */       if (dam > 0)
/*  379 */         damage = " Damage=" + dam + "."; 
/*  380 */       String growthState = "foliage";
/*  381 */       String name = "unknown";
/*  382 */       if (theTile.isBush()) {
/*      */         
/*  384 */         BushData.BushType bushType = theTile.getBushType(data);
/*  385 */         name = bushType.getName();
/*  386 */         growthState = getGrowthState(performer, tilex, tiley, age, bushType, theTile.isMycelium());
/*      */       }
/*      */       else {
/*      */         
/*  390 */         TreeData.TreeType treeType = theTile.getTreeType(data);
/*  391 */         name = treeType.getName();
/*  392 */         growthState = getGrowthState(performer, tilex, tiley, age, treeType, theTile.isMycelium());
/*      */       } 
/*      */       
/*  395 */       comm.sendNormalServerMessage("You see " + ageString + " " + name + "." + growthState + damage);
/*      */       
/*  397 */       sendVillageString(performer, tilex, tiley, true);
/*  398 */       Trap t = Trap.getTrap(tilex, tiley, performer.getLayer());
/*  399 */       if (performer.getPower() > 3)
/*      */       {
/*  401 */         comm.sendNormalServerMessage("Your rot: " + Creature.normalizeAngle(performer.getStatus().getRotation()) + ", Wind rot=" + 
/*  402 */             Server.getWeather().getWindRotation() + ", pow=" + Server.getWeather().getWindPower() + " x=" + 
/*  403 */             Server.getWeather().getXWind() + ", y=" + Server.getWeather().getYWind());
/*  404 */         comm.sendNormalServerMessage("Tile is spring=" + Zone.hasSpring(tilex, tiley));
/*  405 */         if (performer.getPower() >= 5)
/*  406 */           comm.sendNormalServerMessage("tilex: " + tilex + ", tiley=" + tiley); 
/*  407 */         if (t != null)
/*      */         {
/*  409 */           String villageName = "none";
/*  410 */           if (t.getVillage() > 0) {
/*      */             
/*      */             try {
/*      */               
/*  414 */               villageName = Villages.getVillage(t.getVillage()).getName();
/*      */             }
/*  416 */             catch (NoSuchVillageException noSuchVillageException) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  421 */           comm.sendNormalServerMessage("A " + t.getName() + ", ql=" + t.getQualityLevel() + " kingdom=" + 
/*  422 */               Kingdoms.getNameFor(t.getKingdom()) + ", vill=" + villageName + ", rotdam=" + t.getRotDamage() + " firedam=" + t
/*  423 */               .getFireDamage() + " speed=" + t.getSpeedBon());
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  428 */       else if (t != null)
/*      */       {
/*  430 */         if (t.getKingdom() == performer.getKingdomId() || performer.getDetectDangerBonus() > 0.0F)
/*      */         {
/*      */           
/*  433 */           String qlString = "average";
/*  434 */           if (t.getQualityLevel() < 20) {
/*  435 */             qlString = "low";
/*  436 */           } else if (t.getQualityLevel() > 80) {
/*  437 */             qlString = "deadly";
/*  438 */           } else if (t.getQualityLevel() > 50) {
/*  439 */             qlString = "high";
/*  440 */           }  String villageName = ".";
/*  441 */           if (t.getVillage() > 0) {
/*      */             
/*      */             try {
/*      */               
/*  445 */               villageName = " of " + Villages.getVillage(t.getVillage()).getName() + ".";
/*      */             }
/*  447 */             catch (NoSuchVillageException noSuchVillageException) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  452 */           String rotDam = "";
/*  453 */           if (t.getRotDamage() > 0)
/*  454 */             rotDam = " It has ugly black-green speckles."; 
/*  455 */           String fireDam = "";
/*  456 */           if (t.getFireDamage() > 0)
/*  457 */             fireDam = " It has the rune of fire."; 
/*  458 */           StringBuilder buf = new StringBuilder();
/*  459 */           buf.append("You detect a ");
/*  460 */           buf.append(t.getName());
/*  461 */           buf.append(" here, of ");
/*  462 */           buf.append(qlString);
/*  463 */           buf.append(" quality.");
/*  464 */           buf.append(" It has been set by people from ");
/*  465 */           buf.append(Kingdoms.getNameFor(t.getKingdom()));
/*  466 */           buf.append(villageName);
/*  467 */           buf.append(rotDam);
/*  468 */           buf.append(fireDam);
/*  469 */           comm.sendNormalServerMessage(buf.toString());
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  474 */     } else if (action == 141 && theTile.isNormal() && theTile.isTree() && performer.getDeity() != null && performer
/*  475 */       .getDeity().isForestGod()) {
/*      */       
/*  477 */       if (isPrayingAge(age)) {
/*  478 */         done = MethodsReligion.pray(act, performer, counter);
/*      */       } else {
/*  480 */         done = true;
/*      */       } 
/*  482 */     } else if (action == 852 && theTile.canBearFruit() && hasFruit(performer, tilex, tiley, age)) {
/*      */ 
/*      */       
/*  485 */       done = study(act, performer, tilex, tiley, tile, action, counter);
/*      */     }
/*  487 */     else if (action == 935 && theTile.isTree() && theTile.getTreeType(data).getTypeId() == TreeData.TreeType.BIRCH.getTypeId() && age == 14) {
/*      */       
/*  489 */       done = Terraforming.pickBark(act, performer, tilex, tiley, tile, theTile, counter);
/*      */     }
/*  491 */     else if (action == 935 && theTile.isBush() && age == 14) {
/*      */       
/*  493 */       done = Terraforming.findTwigs(act, performer, tilex, tiley, tile, theTile, counter);
/*      */     }
/*      */     else {
/*      */       
/*  497 */       done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*      */     } 
/*  499 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getTreenameForMaterial(byte material) {
/*  504 */     String treeString = "tree";
/*  505 */     switch (material)
/*      */     
/*      */     { case 14:
/*  508 */         treeString = "birch";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  585 */         return treeString;case 37: treeString = "pine"; return treeString;case 38: treeString = "oak"; return treeString;case 39: treeString = "cedar"; return treeString;case 40: treeString = "willow"; return treeString;case 63: treeString = "chestnut"; return treeString;case 64: treeString = "walnut"; return treeString;case 41: treeString = "maple"; return treeString;case 42: treeString = "appletree"; return treeString;case 43: treeString = "lemontree"; return treeString;case 44: treeString = "olivetree"; return treeString;case 45: treeString = "cherrytree"; return treeString;case 46: treeString = "lavenderbush"; return treeString;case 47: treeString = "rosebush"; return treeString;case 48: treeString = "thornbush"; return treeString;case 49: treeString = "grapebush"; return treeString;case 50: treeString = "camelliabush"; return treeString;case 51: treeString = "oleanderbush"; return treeString;case 65: treeString = "fir"; return treeString;case 66: treeString = "linden"; return treeString;case 71: treeString = "hazelbush"; return treeString;case 88: treeString = "orangetree"; return treeString;case 90: treeString = "raspberrybush"; return treeString;case 91: treeString = "blueberrybush"; return treeString;case 92: treeString = "lingonberrybush"; return treeString; }  treeString = "tree"; return treeString;
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
/*      */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/*  599 */     boolean done = true;
/*  600 */     byte tiletype = Tiles.decodeType(tile);
/*  601 */     byte data = Tiles.decodeData(tile);
/*  602 */     int age = data >> 4 & 0xF;
/*  603 */     Tiles.Tile theTile = Tiles.getTile(tiletype);
/*  604 */     GrassData.GrowthTreeStage growth = GrassData.GrowthTreeStage.decodeTileData(Tiles.decodeData(tile));
/*      */     
/*  606 */     if ((source.isWeaponSlash() || source.getTemplateId() == 24) && action == 96) {
/*      */       
/*  608 */       done = Terraforming.handleChopAction(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter);
/*      */     
/*      */     }
/*  611 */     else if (action == 1 || action == 34) {
/*  612 */       done = action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*  613 */     } else if (!theTile.isEnchanted() && theTile != Tiles.Tile.TILE_BUSH_LINGONBERRY && action == 187 && isSproutingAge(age)) {
/*      */       
/*  615 */       done = Terraforming.pickSprout(performer, source, tilex, tiley, tile, theTile, counter, act);
/*      */     }
/*  617 */     else if (action == 188 && performer.getPower() >= 2 && source
/*  618 */       .getTemplateId() == 176) {
/*      */ 
/*      */       
/*  621 */       int type = data & 0xF;
/*  622 */       int newAge = age + 1;
/*      */       
/*  624 */       int newData = (newAge << 4) + type & 0xFF;
/*  625 */       Server.surfaceMesh.setTile(tilex, tiley, 
/*  626 */           Tiles.encode(Tiles.decodeHeight(tile), Tiles.decodeType(tile), (byte)newData));
/*  627 */       Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*      */     }
/*  629 */     else if (!theTile.isEnchanted() && action == 141) {
/*      */       
/*  631 */       done = action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*      */     }
/*  633 */     else if (!theTile.isEnchanted() && action == 152) {
/*      */       
/*  635 */       if (theTile.isNormalTree()) {
/*      */         
/*  637 */         done = Terraforming.harvestTree(act, performer, source, tilex, tiley, tile, theTile, counter);
/*      */       }
/*  639 */       else if (theTile.isNormalBush()) {
/*      */         
/*  641 */         done = Terraforming.harvestBush(act, performer, source, tilex, tiley, tile, theTile, counter);
/*      */       } else {
/*      */         
/*  644 */         done = true;
/*      */       } 
/*  646 */     } else if (action == 373 && !theTile.isEnchanted()) {
/*      */       
/*  648 */       if (theTile.isTree() || theTile.isBush()) {
/*      */         
/*  650 */         done = Terraforming.prune(act, performer, source, tilex, tiley, tile, theTile, counter);
/*      */       } else {
/*      */         
/*  653 */         done = true;
/*      */       } 
/*  655 */     } else if (action == 935 && theTile.isTree() && source.getTemplateId() == 390 && age == 15) {
/*      */       
/*  657 */       done = Terraforming.pickGrubs(act, performer, source, tilex, tiley, tile, theTile, counter);
/*      */     }
/*  659 */     else if (action == 935 && theTile.isTree() && theTile.getTreeType(data).getTypeId() == TreeData.TreeType.BIRCH.getTypeId() && age == 14) {
/*      */       
/*  661 */       done = Terraforming.pickBark(act, performer, tilex, tiley, tile, theTile, counter);
/*      */     }
/*  663 */     else if (action == 935 && theTile.isBush() && age == 14) {
/*      */       
/*  665 */       done = Terraforming.findTwigs(act, performer, tilex, tiley, tile, theTile, counter);
/*      */     }
/*  667 */     else if (action == 118 && source.getTemplateId() == 526 && !theTile.isEnchanted()) {
/*      */       
/*  669 */       performer.getCommunicator().sendNormalServerMessage("You draw a circle in the air in front of you with " + source
/*  670 */           .getNameWithGenus() + ".");
/*  671 */       Server.getInstance().broadCastAction(performer
/*  672 */           .getName() + " draws a circle in the air in front of " + performer.getHimHerItString() + " with " + source
/*  673 */           .getNameWithGenus() + ".", performer, 5);
/*  674 */       done = true;
/*      */       
/*  676 */       byte cdata = (byte)(Tiles.decodeData(tile) & 15 + (FoliageAge.VERY_OLD_SPROUTING.getAgeId() << 4) & 0xFF);
/*  677 */       byte newTreeType = 0;
/*  678 */       if (theTile.isNormal() && performer.getKingdomTemplateId() == 3) {
/*      */ 
/*      */         
/*  681 */         if (theTile.isTree())
/*      */         {
/*  683 */           TreeData.TreeType ttype = theTile.getTreeType(cdata);
/*  684 */           newTreeType = ttype.asMyceliumTree();
/*      */         }
/*      */         else
/*      */         {
/*  688 */           BushData.BushType btype = theTile.getBushType(cdata);
/*  689 */           newTreeType = btype.asMyceliumBush();
/*      */         }
/*      */       
/*  692 */       } else if (theTile.isMycelium() && performer.getKingdomTemplateId() != 3) {
/*      */ 
/*      */         
/*  695 */         if (theTile.isTree()) {
/*      */           
/*  697 */           TreeData.TreeType ttype = theTile.getTreeType(cdata);
/*  698 */           newTreeType = ttype.asNormalTree();
/*      */         }
/*      */         else {
/*      */           
/*  702 */           BushData.BushType btype = theTile.getBushType(cdata);
/*  703 */           newTreeType = btype.asNormalBush();
/*      */         } 
/*      */       } 
/*      */       
/*  707 */       if (newTreeType != 0 && source.getAuxData() > 0) {
/*      */         
/*  709 */         Server.surfaceMesh.setTile(tilex, tiley, 
/*      */ 
/*      */             
/*  712 */             Tiles.encode(Tiles.decodeHeight(tile), newTreeType, cdata));
/*  713 */         Players.getInstance().sendChangedTile(tilex, tiley, onSurface, true);
/*  714 */         source.setAuxData((byte)(source.getAuxData() - 1));
/*      */         
/*      */         try {
/*  717 */           Zone z = Zones.getZone(tilex, tiley, true);
/*  718 */           z.changeTile(tilex, tiley);
/*      */         }
/*  720 */         catch (NoSuchZoneException noSuchZoneException) {}
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  725 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*      */       } 
/*  727 */     } else if (action == 852 && theTile.canBearFruit() && hasFruit(performer, tilex, tiley, age)) {
/*      */ 
/*      */       
/*  730 */       done = study(act, performer, tilex, tiley, tile, action, counter);
/*      */     }
/*  732 */     else if (action == 644) {
/*      */       
/*  734 */       if ((growth == GrassData.GrowthTreeStage.SHORT || theTile.isMycelium()) && (source
/*  735 */         .getTemplateId() == 394 || (source
/*  736 */         .getTemplateId() == 176 && performer.getPower() >= 2))) {
/*  737 */         done = makeLawn(act, performer, source, tilex, tiley, tile, action, counter);
/*      */       }
/*  739 */     } else if (action == 645 && theTile.isNormal()) {
/*      */       
/*  741 */       if (growth != GrassData.GrowthTreeStage.LAWN && growth != GrassData.GrowthTreeStage.SHORT && (source
/*  742 */         .getTemplate().isSharp() || (source
/*  743 */         .getTemplateId() == 176 && performer.getPower() >= 2))) {
/*  744 */         done = cutGrass(act, performer, source, tilex, tiley, tile, action, counter);
/*      */       }
/*  746 */     } else if (act.isQuick()) {
/*  747 */       done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*      */     } else {
/*  749 */       done = super.action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter);
/*  750 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean hasFruit(Creature performer, int tilex, int tiley, int age) {
/*  755 */     int encodedTile = Server.surfaceMesh.getTile(tilex, tiley);
/*  756 */     byte data = Tiles.decodeData(encodedTile);
/*      */     
/*  758 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId()) {
/*      */       
/*  760 */       if (Servers.isThisATestServer() && performer.getPower() > 1)
/*  761 */         return true; 
/*  762 */       return TreeData.hasFruit(data);
/*      */     } 
/*  764 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean isAlmostRipe(int age, BushData.BushType type) {
/*  769 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId()) {
/*      */       
/*  771 */       switch (type) {
/*      */         
/*      */         case MAPLE:
/*  774 */           return WurmHarvestables.Harvestable.LAVENDER.isAlmostRipe();
/*      */         case APPLE:
/*  776 */           return WurmHarvestables.Harvestable.ROSE.isAlmostRipe();
/*      */         case LEMON:
/*  778 */           return WurmHarvestables.Harvestable.GRAPE.isAlmostRipe();
/*      */         case OLIVE:
/*  780 */           return WurmHarvestables.Harvestable.CAMELLIA.isAlmostRipe();
/*      */         case CHERRY:
/*  782 */           return WurmHarvestables.Harvestable.OLEANDER.isAlmostRipe();
/*      */         case CHESTNUT:
/*  784 */           return WurmHarvestables.Harvestable.HAZEL.isAlmostRipe();
/*      */         case WALNUT:
/*  786 */           return WurmHarvestables.Harvestable.RASPBERRY.isAlmostRipe();
/*      */         case PINE:
/*  788 */           return WurmHarvestables.Harvestable.BLUEBERRY.isAlmostRipe();
/*      */         case OAK:
/*  790 */           return WurmHarvestables.Harvestable.LINGONBERRY.isAlmostRipe();
/*      */       } 
/*  792 */       return false;
/*      */     } 
/*      */     
/*  795 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final boolean isAlmostRipe(int age, TreeData.TreeType type) {
/*  801 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId()) {
/*      */       
/*  803 */       switch (type) {
/*      */         
/*      */         case MAPLE:
/*  806 */           return WurmHarvestables.Harvestable.MAPLE.isAlmostRipe();
/*      */         case APPLE:
/*  808 */           return WurmHarvestables.Harvestable.APPLE.isAlmostRipe();
/*      */         case LEMON:
/*  810 */           return WurmHarvestables.Harvestable.LEMON.isAlmostRipe();
/*      */         case OLIVE:
/*  812 */           return WurmHarvestables.Harvestable.OLIVE.isAlmostRipe();
/*      */         case CHERRY:
/*  814 */           return WurmHarvestables.Harvestable.CHERRY.isAlmostRipe();
/*      */         case CHESTNUT:
/*  816 */           return WurmHarvestables.Harvestable.CHESTNUT.isAlmostRipe();
/*      */         case WALNUT:
/*  818 */           return WurmHarvestables.Harvestable.WALNUT.isAlmostRipe();
/*      */         case PINE:
/*  820 */           return WurmHarvestables.Harvestable.PINE.isAlmostRipe();
/*      */         case OAK:
/*  822 */           return WurmHarvestables.Harvestable.OAK.isAlmostRipe();
/*      */         case ORANGE:
/*  824 */           return WurmHarvestables.Harvestable.ORANGE.isAlmostRipe();
/*      */       } 
/*  826 */       return false;
/*      */     } 
/*      */     
/*  829 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final int getItem(int tilex, int tiley, int age, BushData.BushType type) {
/*  834 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId()) {
/*      */       
/*  836 */       switch (type) {
/*      */         
/*      */         case MAPLE:
/*  839 */           return 424;
/*      */         case APPLE:
/*  841 */           return 426;
/*      */         
/*      */         case LEMON:
/*  844 */           if (tiley > Zones.worldTileSizeY / 2) {
/*  845 */             return 411;
/*      */           }
/*  847 */           return 414;
/*      */         
/*      */         case OLIVE:
/*  850 */           return 422;
/*      */         case CHERRY:
/*  852 */           return 423;
/*      */         case CHESTNUT:
/*  854 */           return 134;
/*      */         case WALNUT:
/*  856 */           return 1196;
/*      */         case PINE:
/*  858 */           return 364;
/*      */         case OAK:
/*  860 */           return 367;
/*      */       } 
/*  862 */       return -10;
/*      */     } 
/*      */     
/*  865 */     return -10;
/*      */   }
/*      */ 
/*      */   
/*      */   static final int getItem(int tilex, int tiley, int age, TreeData.TreeType type) {
/*  870 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId()) {
/*      */       
/*  872 */       switch (type) {
/*      */         
/*      */         case MAPLE:
/*  875 */           return 416;
/*      */         case APPLE:
/*  877 */           return 6;
/*      */         case LEMON:
/*  879 */           return 410;
/*      */         case OLIVE:
/*  881 */           return 412;
/*      */         case CHERRY:
/*  883 */           return 409;
/*      */         case CHESTNUT:
/*  885 */           return 833;
/*      */         case WALNUT:
/*  887 */           return 832;
/*      */         case PINE:
/*  889 */           return 1184;
/*      */         case OAK:
/*  891 */           return 436;
/*      */         case ORANGE:
/*  893 */           return 1283;
/*      */       } 
/*  895 */       return -10;
/*      */     } 
/*      */     
/*  898 */     return -10;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String getGrowthState(Creature performer, int tilex, int tiley, int age, TreeData.TreeType treeType, boolean infected) {
/*  904 */     String toReturn = "";
/*  905 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId())
/*      */     {
/*  907 */       switch (treeType) {
/*      */         
/*      */         case MAPLE:
/*  910 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/*  912 */             if (infected) {
/*  913 */               toReturn = " The maple is infected and the sap is useless."; break;
/*      */             } 
/*  915 */             toReturn = " The maple is brimming with sap."; break;
/*      */           } 
/*  917 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/*  919 */             if (infected) {
/*  920 */               toReturn = " The maple is infected and the sap will be useless."; break;
/*      */             } 
/*  922 */             toReturn = " The maple will start to produce sap soon."; break;
/*      */           } 
/*  924 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/*  926 */             toReturn = " The maple has no sap left.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case APPLE:
/*  931 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/*  933 */             if (infected) {
/*  934 */               toReturn = " No apples grew to mature state, and they have weird brown spots."; break;
/*      */             } 
/*  936 */             toReturn = " The tree has some fine green apples."; break;
/*      */           } 
/*  938 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/*  940 */             if (infected) {
/*  941 */               toReturn = " The tree will not produce any healthy apples."; break;
/*      */             } 
/*  943 */             toReturn = " The apples will soon be ripe."; break;
/*      */           } 
/*  945 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/*  947 */             toReturn = " The tree has been picked clean of any apples.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case LEMON:
/*  952 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/*  954 */             if (infected) {
/*  955 */               toReturn = " No lemons grew to mature state, and they have weird brown spots."; break;
/*      */             } 
/*  957 */             toReturn = " The tree has some fine yellow lemons."; break;
/*      */           } 
/*  959 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/*  961 */             if (infected) {
/*  962 */               toReturn = " The tree will not produce any healthy lemons."; break;
/*      */             } 
/*  964 */             toReturn = " The lemons will soon be ripe."; break;
/*      */           } 
/*  966 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/*  968 */             toReturn = " The tree has been picked clean of its lemons.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case OLIVE:
/*  973 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/*  975 */             if (infected) {
/*  976 */               toReturn = " No olives grew to mature state, and they have weird white spots."; break;
/*      */             } 
/*  978 */             toReturn = " The tree has some fine black olives."; break;
/*      */           } 
/*  980 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/*  982 */             if (infected) {
/*  983 */               toReturn = " The tree will not produce any healthy olives."; break;
/*      */             } 
/*  985 */             toReturn = " The olives will soon be ripe."; break;
/*      */           } 
/*  987 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/*  989 */             toReturn = " The tree has been picked clean of any olives.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case CHERRY:
/*  994 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/*  996 */             if (infected) {
/*  997 */               toReturn = " No cherries grew to mature state, and they have weird brown spots."; break;
/*      */             } 
/*  999 */             toReturn = " The tree has some juicy red cherries."; break;
/*      */           } 
/* 1001 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/* 1003 */             if (infected) {
/* 1004 */               toReturn = " The tree will not produce any healthy cherries."; break;
/*      */             } 
/* 1006 */             toReturn = " The cherries will soon be ripe."; break;
/*      */           } 
/* 1008 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/* 1010 */             toReturn = " The tree has been picked clean of any cherries.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case CHESTNUT:
/* 1015 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/* 1017 */             if (infected) {
/* 1018 */               toReturn = " No chestnuts grew to mature state, and they have weird yellow spots."; break;
/*      */             } 
/* 1020 */             toReturn = " The tree has some interesting chestnuts."; break;
/*      */           } 
/* 1022 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/* 1024 */             if (infected) {
/* 1025 */               toReturn = " The tree will not produce any healthy chestnuts."; break;
/*      */             } 
/* 1027 */             toReturn = " The chestnuts will soon be ripe."; break;
/*      */           } 
/* 1029 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/* 1031 */             toReturn = " The tree has been picked clean of any chestnuts.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case WALNUT:
/* 1036 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/* 1038 */             if (infected) {
/* 1039 */               toReturn = " No walnuts grew to mature state, and they have weird brown spots."; break;
/*      */             } 
/* 1041 */             toReturn = " The tree has some juicy walnuts."; break;
/*      */           } 
/* 1043 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/* 1045 */             if (infected) {
/* 1046 */               toReturn = " The tree will not produce any healthy walnuts."; break;
/*      */             } 
/* 1048 */             toReturn = " The walnuts will soon be ripe."; break;
/*      */           } 
/* 1050 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/* 1052 */             toReturn = " The tree has been picked clean of any walnuts.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case PINE:
/* 1057 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/* 1059 */             if (infected) {
/* 1060 */               toReturn = " No pinenuts grew to mature state, and they have weird brown spots."; break;
/*      */             } 
/* 1062 */             toReturn = " The tree has some fine pinenuts."; break;
/*      */           } 
/* 1064 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/* 1066 */             if (infected) {
/* 1067 */               toReturn = " The tree will not produce any healthy pinenuts."; break;
/*      */             } 
/* 1069 */             toReturn = " The pinenuts will soon be ready."; break;
/*      */           } 
/* 1071 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/* 1073 */             toReturn = " The tree has been picked clean of any pinenuts.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case OAK:
/* 1078 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/* 1080 */             if (infected) {
/* 1081 */               toReturn = " No acorns grew to mature state, and they have weird brown spots."; break;
/*      */             } 
/* 1083 */             toReturn = " The tree has some fine acorns."; break;
/*      */           } 
/* 1085 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/* 1087 */             if (infected) {
/* 1088 */               toReturn = " The tree will not produce any healthy acorns."; break;
/*      */             } 
/* 1090 */             toReturn = " The acorns will soon be ready."; break;
/*      */           } 
/* 1092 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/* 1094 */             toReturn = " The tree has been picked clean of any acorns.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case ORANGE:
/* 1099 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/* 1101 */             if (infected) {
/* 1102 */               toReturn = " No oranges grew to mature state, and they have weird brown spots."; break;
/*      */             } 
/* 1104 */             toReturn = " The tree has some fine oranges."; break;
/*      */           } 
/* 1106 */           if (isAlmostRipe(age, treeType)) {
/*      */             
/* 1108 */             if (infected) {
/* 1109 */               toReturn = " The tree will not produce any healthy oranges."; break;
/*      */             } 
/* 1111 */             toReturn = " The oranges will soon be ripe."; break;
/*      */           } 
/* 1113 */           if (hasBeenPicked(tilex, tiley))
/*      */           {
/* 1115 */             toReturn = " The tree has been picked clean of its oranges.";
/*      */           }
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1123 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String getGrowthState(Creature performer, int tilex, int tiley, int age, BushData.BushType bushType, boolean infected) {
/* 1129 */     String toReturn = "";
/* 1130 */     if (age > FoliageAge.YOUNG_FOUR.getAgeId() && age < FoliageAge.OVERAGED.getAgeId())
/*      */     
/* 1132 */     { switch (bushType)
/*      */       
/*      */       { case MAPLE:
/* 1135 */           if (hasFruit(performer, tilex, tiley, age)) {
/*      */             
/* 1137 */             if (infected) {
/* 1138 */               toReturn = " The flowers are ugly and sick, with stinking ooze dripping from its petals.";
/*      */             } else {
/* 1140 */               toReturn = " The bush has some beautiful flowers.";
/*      */             } 
/* 1142 */           } else if (isAlmostRipe(age, bushType)) {
/*      */             
/* 1144 */             if (infected) {
/* 1145 */               toReturn = " The buds look sick.";
/*      */             } else {
/* 1147 */               toReturn = " The bush has a couple of buds.";
/*      */             } 
/* 1149 */           } else if (hasBeenPicked(tilex, tiley)) {
/*      */             
/* 1151 */             toReturn = " The bush has no flowers left; all have been picked.";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1345 */           return toReturn;case APPLE: if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " The flowers are ugly and sick, with stinking ooze dripping from its petals."; } else { toReturn = " The bush has some beautiful flowers."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The buds look sick."; } else { toReturn = " The bush has a couple of promising buds."; }  } else if (hasBeenPicked(tilex, tiley)) { toReturn = " The bush has no flowers left; all have been picked."; }  return toReturn;case LEMON: if (tiley > Zones.worldTileSizeY / 2) { if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " No grapes grew to mature state, and they have weird black spots."; } else { toReturn = " The bush has some juicy blue grapes."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The bush will not produce any healthy grapes."; } else { toReturn = " The bush has a couple of immature blue grapes."; }  } else if (hasBeenPicked(tilex, tiley)) { toReturn = " The bush has no grapes left; all have been picked."; }  } else if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " No grapes grew to mature state, and they have weird black spots."; } else { toReturn = " The bush has some juicy green grapes."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The bush will not produce any healthy grapes."; } else { toReturn = " The bush has a couple of immature green grapes."; }  } else if (hasBeenPicked(tilex, tiley)) { toReturn = " The bush has no grapes left; all have been picked."; }  return toReturn;case OLIVE: if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " The leaves are infected with disease. It looks as if someone sprinkled white flour on them."; } else { toReturn = " The bush has a number of leaves that look and smell perfect."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The leaves are infected with disease. It looks as if someone sprinkled white flour on them."; } else { toReturn = " The bush has started to give off an interesting scent."; }  }  return toReturn;case CHERRY: if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " The leaves are infected with disease. It looks as if someone sprinkled white flour on them."; } else { toReturn = " The bush has a number of strong smelling leaves."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The leaves are infected with disease. It looks as if someone sprinkled white flour on them."; } else { toReturn = " The bush has started to smell rather badly."; }  }  return toReturn;case CHESTNUT: if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " The nuts are infected with disease. It looks as if someone sprinkled white flour on them."; } else { toReturn = " The bush has a number of strong smelling nuts."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The nuts are infected with disease. It looks as if someone sprinkled white flour on them."; } else { toReturn = " The bush has started to smell rather odd."; }  } else if (hasBeenPicked(tilex, tiley)) { toReturn = " The bush has no nuts left; all have been picked."; }  return toReturn;case WALNUT: if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " No raspberries grew to mature state, and they have weird black spots."; } else { toReturn = " The bush has some juicy raspberries."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The bush will not produce any healthy raspberries."; } else { toReturn = " The bush has a couple of immature raspberries."; }  } else if (hasBeenPicked(tilex, tiley)) { toReturn = " The bush has no raspberries left; all have been picked."; }  return toReturn;case PINE: if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " No blueberries grew to mature state, and they have weird black spots."; } else { toReturn = " The bush has some juicy blueberries."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The bush will not produce any healthy blueberries."; } else { toReturn = " The bush has a couple of immature blueberries."; }  } else if (hasBeenPicked(tilex, tiley)) { toReturn = " The bush has no blueberries left; all have been picked."; }  return toReturn;case OAK: if (hasFruit(performer, tilex, tiley, age)) { if (infected) { toReturn = " No lingonberries grew to mature state, and they have weird black spots."; } else { toReturn = " The bush has some juicy lingonberries."; }  } else if (isAlmostRipe(age, bushType)) { if (infected) { toReturn = " The bush will not produce any healthy lingonberries."; } else { toReturn = " The bush has a couple of immature lingonberries."; }  } else if (hasBeenPicked(tilex, tiley)) { toReturn = " The bush has no lingonberries left; all have been picked."; }  return toReturn; }  toReturn = ""; }  return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean hasBeenPicked(int tilex, int tiley) {
/* 1350 */     int encodedTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1351 */     byte type = Tiles.decodeType(encodedTile);
/*      */     
/* 1353 */     switch (type) {
/*      */       
/*      */       case -114:
/* 1356 */         if (!WurmHarvestables.Harvestable.LAVENDER.isHarvestable())
/* 1357 */           return false; 
/*      */         break;
/*      */       case -113:
/* 1360 */         if (!WurmHarvestables.Harvestable.ROSE.isHarvestable())
/* 1361 */           return false; 
/*      */         break;
/*      */       case -111:
/* 1364 */         if (!WurmHarvestables.Harvestable.GRAPE.isHarvestable())
/* 1365 */           return false; 
/*      */         break;
/*      */       case -110:
/* 1368 */         if (!WurmHarvestables.Harvestable.CAMELLIA.isHarvestable())
/* 1369 */           return false; 
/*      */         break;
/*      */       case -109:
/* 1372 */         if (!WurmHarvestables.Harvestable.OLEANDER.isHarvestable())
/* 1373 */           return false; 
/*      */         break;
/*      */       case 105:
/* 1376 */         if (!WurmHarvestables.Harvestable.MAPLE.isHarvestable())
/* 1377 */           return false; 
/*      */         break;
/*      */       case 106:
/* 1380 */         if (!WurmHarvestables.Harvestable.APPLE.isHarvestable())
/* 1381 */           return false; 
/*      */         break;
/*      */       case 107:
/* 1384 */         if (!WurmHarvestables.Harvestable.LEMON.isHarvestable())
/* 1385 */           return false; 
/*      */         break;
/*      */       case 108:
/* 1388 */         if (!WurmHarvestables.Harvestable.OLIVE.isHarvestable())
/* 1389 */           return false; 
/*      */         break;
/*      */       case 109:
/* 1392 */         if (!WurmHarvestables.Harvestable.CHERRY.isHarvestable())
/* 1393 */           return false; 
/*      */         break;
/*      */       case 110:
/* 1396 */         if (!WurmHarvestables.Harvestable.CHESTNUT.isHarvestable())
/* 1397 */           return false; 
/*      */         break;
/*      */       case 111:
/* 1400 */         if (!WurmHarvestables.Harvestable.WALNUT.isHarvestable())
/* 1401 */           return false; 
/*      */         break;
/*      */       case 101:
/* 1404 */         if (!WurmHarvestables.Harvestable.PINE.isHarvestable())
/* 1405 */           return false; 
/*      */         break;
/*      */       case 102:
/* 1408 */         if (!WurmHarvestables.Harvestable.OAK.isHarvestable())
/* 1409 */           return false; 
/*      */         break;
/*      */       case -96:
/* 1412 */         if (!WurmHarvestables.Harvestable.HAZEL.isHarvestable())
/* 1413 */           return false; 
/*      */         break;
/*      */       case -93:
/* 1416 */         if (!WurmHarvestables.Harvestable.ORANGE.isHarvestable())
/* 1417 */           return false; 
/*      */         break;
/*      */       case -90:
/* 1420 */         if (!WurmHarvestables.Harvestable.RASPBERRY.isHarvestable())
/* 1421 */           return false; 
/*      */         break;
/*      */       case -87:
/* 1424 */         if (!WurmHarvestables.Harvestable.BLUEBERRY.isHarvestable())
/* 1425 */           return false; 
/*      */         break;
/*      */       case -84:
/* 1428 */         if (!WurmHarvestables.Harvestable.LINGONBERRY.isHarvestable())
/* 1429 */           return false; 
/*      */         break;
/*      */       default:
/* 1432 */         return false;
/*      */     } 
/* 1434 */     byte data = Tiles.decodeData(encodedTile);
/* 1435 */     return !TreeData.hasFruit(data);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void pick(int tilex, int tiley) {
/* 1441 */     int encodedTile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1442 */     byte tileType = Tiles.decodeType(encodedTile);
/* 1443 */     short newHeight = Tiles.decodeHeight(encodedTile);
/*      */     
/* 1445 */     byte tileData = Tiles.decodeData(encodedTile);
/* 1446 */     tileData = (byte)(tileData & 0xF7);
/* 1447 */     Server.setSurfaceTile(tilex, tiley, newHeight, tileType, tileData);
/* 1448 */     Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean cutGrass(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter) {
/* 1454 */     float maxQLFromUsedTool = 5.0F;
/* 1455 */     short yield = 2;
/* 1456 */     int time = 0;
/* 1457 */     Skill gardening = null;
/* 1458 */     Skill toolskill = null;
/* 1459 */     Item toolUsed = null;
/* 1460 */     boolean toReturn = false;
/*      */     
/* 1462 */     byte tileType = Tiles.decodeType(tile);
/* 1463 */     byte tileData = Tiles.decodeData(tile);
/* 1464 */     GrassData.GrowthTreeStage growthStage = GrassData.GrowthTreeStage.decodeTileData(tileData);
/*      */ 
/*      */     
/*      */     try {
/* 1468 */       float tilexpos = ((tilex << 2) + 1);
/* 1469 */       float tileypos = ((tiley << 2) + 1);
/* 1470 */       float tilezpos = Zones.calculateHeight(tilexpos, tileypos, true);
/*      */       
/* 1472 */       if (!performer.isWithinDistanceTo(tilexpos, tileypos, tilezpos, 20.0F))
/*      */       {
/* 1474 */         performer.getCommunicator().sendNormalServerMessage("The grass is growing out of your reach.");
/*      */         
/* 1476 */         return true;
/*      */       }
/*      */     
/* 1479 */     } catch (NoSuchZoneException nsze) {
/*      */       
/* 1481 */       logger.log(Level.WARNING, " No such zone exception at " + tilex + "," + tiley + " when player tried to TileTreeBehaviour.cutGrass()", (Throwable)nsze);
/*      */     } 
/*      */ 
/*      */     
/* 1485 */     if (source != null) {
/*      */ 
/*      */       
/* 1488 */       if (source.getTemplateId() == 267 || source.getTemplateId() == 268 || source
/* 1489 */         .getTemplateId() == 176) {
/*      */         
/* 1491 */         maxQLFromUsedTool = 100.0F;
/*      */       }
/* 1493 */       else if (source.getTemplate().isSharp()) {
/*      */         
/* 1495 */         maxQLFromUsedTool = 20.0F;
/*      */       }
/* 1497 */       else if (source.getTemplateId() == 14) {
/*      */         
/* 1499 */         maxQLFromUsedTool = 5.0F;
/*      */       }
/*      */       else {
/*      */         
/* 1503 */         performer.getCommunicator().sendNormalServerMessage("You can't cut grass with " + source
/* 1504 */             .getNameWithGenus() + ".");
/* 1505 */         return true;
/*      */       } 
/* 1507 */       toolUsed = source;
/*      */     }
/*      */     else {
/*      */       
/* 1511 */       performer.getCommunicator().sendNormalServerMessage("You need a tool to cut the grass.");
/* 1512 */       return true;
/*      */     } 
/*      */     
/* 1515 */     yield = GrassData.GrowthTreeStage.getYield(growthStage);
/*      */     
/* 1517 */     if (yield == 0) {
/*      */       
/* 1519 */       performer.getCommunicator().sendNormalServerMessage("You try to cut some " + growthStage
/* 1520 */           .toString().toLowerCase() + " grass but you fail to get any significant amount.");
/*      */       
/* 1522 */       return true;
/*      */     } 
/* 1524 */     if (counter == 1.0F) {
/*      */       
/* 1526 */       double toolBonus = 0.0D;
/*      */       
/*      */       try {
/* 1529 */         int weight = ItemTemplateFactory.getInstance().getTemplate(620).getWeightGrams() * yield;
/*      */         
/* 1531 */         if (performer.getInventory().getNumItemsNotCoins() + 1 >= 100) {
/*      */           
/* 1533 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the grass. You need to drop something first.");
/*      */           
/* 1535 */           return true;
/*      */         } 
/* 1537 */         if (!performer.canCarry(weight))
/*      */         {
/* 1539 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the grass. You need to drop some things first.");
/*      */           
/* 1541 */           return true;
/*      */         }
/*      */       
/* 1544 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 1546 */         logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 1547 */         return true;
/*      */       } 
/*      */       
/* 1550 */       gardening = performer.getSkills().getSkillOrLearn(10045);
/*      */ 
/*      */       
/*      */       try {
/* 1554 */         toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 1555 */         toolBonus = toolskill.getKnowledge(0.0D);
/*      */       }
/* 1557 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */       
/* 1561 */       time = Actions.getStandardActionTime(performer, gardening, source, toolBonus);
/* 1562 */       performer.getCommunicator().sendNormalServerMessage("You start to gather " + growthStage
/* 1563 */           .toString().toLowerCase() + " grass.");
/*      */       
/* 1565 */       Server.getInstance().broadCastAction(performer.getName() + " starts to gather grass.", performer, 5);
/* 1566 */       performer.sendActionControl("gathering grass", true, time);
/* 1567 */       act.setTimeLeft(time);
/* 1568 */       toReturn = false;
/*      */     } else {
/*      */       
/* 1571 */       time = act.getTimeLeft();
/* 1572 */     }  if (act.mayPlaySound())
/* 1573 */       Methods.sendSound(performer, "sound.work.foragebotanize"); 
/* 1574 */     if (counter * 10.0F >= time) {
/*      */ 
/*      */       
/*      */       try {
/* 1578 */         int weight = ItemTemplateFactory.getInstance().getTemplate(620).getWeightGrams() * yield;
/*      */         
/* 1580 */         if (!performer.canCarry(weight))
/*      */         {
/* 1582 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the grass. You need to drop some things first.");
/*      */           
/* 1584 */           return true;
/*      */         }
/*      */       
/* 1587 */       } catch (NoSuchTemplateException nst) {
/*      */         
/* 1589 */         logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 1590 */         return true;
/*      */       } 
/* 1592 */       source.setDamage(source.getDamage() + 0.003F * source.getDamageModifier());
/* 1593 */       double toolBonus = 0.0D;
/* 1594 */       double power = 0.0D;
/*      */       
/* 1596 */       gardening = performer.getSkills().getSkillOrLearn(10045);
/*      */ 
/*      */       
/*      */       try {
/* 1600 */         toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 1601 */         toolBonus = Math.max(1.0D, toolskill.skillCheck(1.0D, toolUsed, 0.0D, false, counter));
/*      */       }
/* 1603 */       catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */       
/* 1607 */       power = gardening.skillCheck(1.0D, toolUsed, toolBonus, false, counter);
/* 1608 */       if (toolUsed.getSpellEffects() != null)
/*      */       {
/* 1610 */         power *= toolUsed.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */       }
/* 1612 */       power += toolUsed.getRarity();
/*      */       
/*      */       try {
/* 1615 */         Item yieldItem = null;
/* 1616 */         for (int i = 0; i < yield; i++) {
/*      */           
/* 1618 */           maxQLFromUsedTool = Math.min(maxQLFromUsedTool, (float)Math.min(100.0D, power));
/* 1619 */           yieldItem = ItemFactory.createItem(620, Math.max(1.0F, maxQLFromUsedTool), null);
/*      */           
/* 1621 */           if (power < 0.0D) {
/* 1622 */             yieldItem.setDamage((float)-power / 2.0F);
/*      */           }
/* 1624 */           performer.getInventory().insertItem(yieldItem);
/*      */         } 
/*      */         
/* 1627 */         byte newdata = (byte)((tileData & 0xFC) + GrassData.GrowthTreeStage.SHORT.getCode());
/* 1628 */         Server.surfaceMesh
/* 1629 */           .setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), tileType, newdata));
/*      */         
/* 1631 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 1632 */         performer.getCommunicator().sendNormalServerMessage("You gather " + yield + " " + yieldItem.getName() + ".");
/* 1633 */         Server.getInstance().broadCastAction(performer.getName() + " gathers some " + yieldItem.getName() + ".", performer, 5);
/*      */ 
/*      */       
/*      */       }
/* 1637 */       catch (NoSuchTemplateException nst) {
/*      */         
/* 1639 */         logger.log(Level.WARNING, "No template for grass type item!", (Throwable)nst);
/* 1640 */         performer.getCommunicator().sendNormalServerMessage("You fail to gather the grass. Your sensitive mind notices a wrongness in the fabric of space.");
/*      */       
/*      */       }
/* 1643 */       catch (FailedException fe) {
/*      */         
/* 1645 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 1646 */         performer.getCommunicator().sendNormalServerMessage("You fail to gather the grass. Your sensitive mind notices a wrongness in the fabric of space.");
/*      */       } 
/*      */       
/* 1649 */       toReturn = true;
/*      */     } 
/* 1651 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean makeLawn(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter) {
/* 1657 */     byte tileType = Tiles.decodeType(tile);
/* 1658 */     String grass = "grass";
/* 1659 */     if (tileType == Tiles.Tile.TILE_MYCELIUM.id)
/* 1660 */       grass = "mycelium"; 
/* 1661 */     int time = 0;
/* 1662 */     Skill gardening = null;
/* 1663 */     Skill toolskill = null;
/* 1664 */     Item toolUsed = null;
/*      */     
/* 1666 */     byte tileData = Tiles.decodeData(tile);
/* 1667 */     boolean toReturn = Terraforming.cannotMakeLawn(performer, tilex, tiley);
/* 1668 */     if (toReturn) {
/* 1669 */       return toReturn;
/*      */     }
/*      */     
/*      */     try {
/* 1673 */       float tilexpos = ((tilex << 2) + 1);
/* 1674 */       float tileypos = ((tiley << 2) + 1);
/* 1675 */       float tilezpos = Zones.calculateHeight(tilexpos, tileypos, true);
/* 1676 */       if (!performer.isWithinDistanceTo(tilexpos, tileypos, tilezpos, 20.0F))
/*      */       {
/* 1678 */         performer.getCommunicator().sendNormalServerMessage("The " + grass + " is growing out of your reach.");
/*      */         
/* 1680 */         return true;
/*      */       }
/*      */     
/* 1683 */     } catch (NoSuchZoneException nsze) {
/*      */       
/* 1685 */       logger.log(Level.WARNING, " No such zone exception at " + tilex + "," + tiley + " when player tried to TileTreeBehaviour.makeLawn()", (Throwable)nsze);
/*      */     } 
/*      */ 
/*      */     
/* 1689 */     if (source != null) {
/*      */ 
/*      */       
/* 1692 */       if (source.getTemplateId() == 394 || source
/* 1693 */         .getTemplateId() == 176) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1702 */         toolUsed = source;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1710 */         if (counter == 1.0F) {
/*      */           
/* 1712 */           gardening = performer.getSkills().getSkillOrLearn(10045);
/* 1713 */           double toolBonus = 0.0D;
/*      */           
/*      */           try {
/* 1716 */             toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 1717 */             toolBonus = toolskill.getKnowledge(0.0D);
/*      */           }
/* 1719 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */           
/* 1723 */           time = Actions.getStandardActionTime(performer, gardening, source, toolBonus);
/* 1724 */           performer.getCommunicator().sendNormalServerMessage("You start to trim the " + grass + " to lawn length.");
/*      */ 
/*      */           
/* 1727 */           Server.getInstance().broadCastAction(performer.getName() + " starts to trim the " + grass + ".", performer, 5);
/* 1728 */           performer.sendActionControl("trimming " + grass, true, time);
/* 1729 */           act.setTimeLeft(time);
/* 1730 */           toReturn = false;
/*      */         } else {
/*      */           
/* 1733 */           time = act.getTimeLeft();
/*      */         } 
/* 1735 */         if (act.mayPlaySound()) {
/* 1736 */           Methods.sendSound(performer, "sound.work.foragebotanize");
/*      */         }
/* 1738 */         if (counter * 10.0F >= time) {
/*      */ 
/*      */           
/* 1741 */           source.setDamage(source.getDamage() + 0.003F * source.getDamageModifier());
/* 1742 */           double toolBonus = 0.0D;
/*      */           
/* 1744 */           gardening = performer.getSkills().getSkillOrLearn(10045);
/*      */           
/*      */           try {
/* 1747 */             toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 1748 */             toolBonus = Math.max(1.0D, toolskill.skillCheck(1.0D, toolUsed, 0.0D, false, counter));
/*      */           }
/* 1750 */           catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */           
/* 1754 */           gardening.skillCheck(1.0D, toolUsed, toolBonus, false, counter);
/*      */           
/* 1756 */           byte newdata = (byte)((tileData & 0xFC) + GrassData.GrowthTreeStage.LAWN.getCode());
/* 1757 */           Server.surfaceMesh
/* 1758 */             .setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), tileType, newdata));
/*      */           
/* 1760 */           Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 1761 */           performer.getCommunicator().sendNormalServerMessage("You trim the " + grass + " to look like a lawn.");
/* 1762 */           Server.getInstance().broadCastAction(performer
/* 1763 */               .getName() + " looks pleased that the " + grass + " is trimmed and now looks like a lawn.", performer, 5);
/*      */           
/* 1765 */           toReturn = true;
/*      */         } 
/* 1767 */         return toReturn;
/*      */       } 
/*      */       performer.getCommunicator().sendNormalServerMessage("You can't trim the " + grass + " with " + source.getNameWithGenus() + ".");
/*      */       return true;
/*      */     } 
/*      */     performer.getCommunicator().sendNormalServerMessage("You need a tool to trim the " + grass + ".");
/* 1773 */     return true; } private boolean study(Action act, Creature performer, int tilex, int tiley, int tile, short action, float counter) { byte tileType = Tiles.decodeType(tile);
/* 1774 */     Tiles.Tile theTile = Tiles.getTile(tileType);
/*      */     
/* 1776 */     int harvestableId = WurmHarvestables.getHarvestableIdFromTile(tileType);
/* 1777 */     WurmHarvestables.Harvestable harvestable = WurmHarvestables.getHarvestable(harvestableId);
/*      */     
/* 1779 */     if (harvestable == null) {
/*      */       
/* 1781 */       performer.getCommunicator().sendNormalServerMessage("You decide not to study the " + theTile
/* 1782 */           .getName() + " as it doesn't seem to ever be harvestable.");
/* 1783 */       return true;
/*      */     } 
/* 1785 */     int time = 0;
/*      */     
/* 1787 */     if (counter == 1.0F) {
/*      */       
/* 1789 */       time = 600;
/* 1790 */       performer.getCommunicator().sendNormalServerMessage("You start to study the " + theTile
/* 1791 */           .getName() + ".");
/*      */       
/* 1793 */       Server.getInstance().broadCastAction(performer.getName() + " starts to study the " + theTile.getName() + ".", performer, 5);
/* 1794 */       performer.sendActionControl("studying " + theTile.getName(), true, time);
/* 1795 */       act.setTimeLeft(time);
/* 1796 */       return false;
/*      */     } 
/*      */     
/* 1799 */     time = act.getTimeLeft();
/*      */     
/* 1801 */     if (act.mayPlaySound()) {
/* 1802 */       Methods.sendSound(performer, "sound.work.foragebotanize");
/*      */     }
/* 1804 */     sendStudyMessages(performer, harvestable, act.currentSecond());
/*      */     
/* 1806 */     if (counter * 10.0F >= time) {
/*      */ 
/*      */       
/* 1809 */       if (performer.getPower() < 2) {
/* 1810 */         pick(tilex, tiley);
/*      */       }
/* 1812 */       ((Player)performer).setStudied(harvestableId);
/* 1813 */       performer.getCommunicator().sendNormalServerMessage("You finish studying the " + theTile.getName() + ". You now need to record the study results.");
/*      */       
/* 1815 */       Server.getInstance().broadCastAction(performer
/* 1816 */           .getName() + " looks pleased with " + performer.getHisHerItsString() + " study results.", performer, 5);
/*      */ 
/*      */ 
/*      */       
/* 1820 */       performer.achievement(553);
/* 1821 */       return true;
/*      */     } 
/* 1823 */     return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void sendStudyMessages(Creature performer, WurmHarvestables.Harvestable harvestable, int currentSecond) {
/* 1829 */     if (currentSecond == 5) {
/* 1830 */       performer.getCommunicator().sendNormalServerMessage("You pick a leaf.");
/* 1831 */     } else if (currentSecond == 10) {
/* 1832 */       performer.getCommunicator().sendNormalServerMessage("You make a mental note of the shape of the leaf.");
/* 1833 */     } else if (currentSecond == 15) {
/* 1834 */       performer.getCommunicator().sendNormalServerMessage("You check the underside of the leaf for any unusual markings.");
/* 1835 */     } else if (currentSecond == 20) {
/* 1836 */       performer.getCommunicator().sendNormalServerMessage("You rub the leaf between your thumb and index finger to see what aroma comes from it.");
/* 1837 */     } else if (currentSecond == 25) {
/* 1838 */       performer.getCommunicator().sendNormalServerMessage("You look up the default harvest times in Wurmpedia.");
/* 1839 */     } else if (currentSecond == 30) {
/* 1840 */       performer.getCommunicator().sendNormalServerMessage("You throw away the damaged leaf.");
/*      */ 
/*      */     
/*      */     }
/* 1844 */     else if (harvestable.isSap()) {
/*      */       
/* 1846 */       if (currentSecond == 35) {
/* 1847 */         performer.getCommunicator().sendNormalServerMessage("You make a small hole in the bark.");
/* 1848 */       } else if (currentSecond == 40) {
/* 1849 */         performer.getCommunicator().sendNormalServerMessage("You wait for the sap to start flowing.");
/* 1850 */       } else if (currentSecond == 45) {
/* 1851 */         performer.getCommunicator().sendNormalServerMessage("You drain off " + harvestable.getFruitWithGenus() + ".");
/* 1852 */       } else if (currentSecond == 50) {
/* 1853 */         performer.getCommunicator().sendNormalServerMessage("You rub the " + harvestable.getFruit() + " between your thumb and forefinger.");
/* 1854 */       } else if (currentSecond == 55) {
/* 1855 */         performer.getCommunicator().sendNormalServerMessage("You pour the " + harvestable.getFruit() + " away.");
/*      */       } 
/* 1857 */     } else if (harvestable.isLeaf()) {
/*      */       
/* 1859 */       if (currentSecond == 35) {
/* 1860 */         performer.getCommunicator().sendNormalServerMessage("You pick another " + harvestable.getFruit() + ".");
/* 1861 */       } else if (currentSecond == 40) {
/* 1862 */         performer.getCommunicator().sendNormalServerMessage("You detect a slight oilyness on the skin.");
/* 1863 */       } else if (currentSecond == 45) {
/* 1864 */         performer.getCommunicator().sendNormalServerMessage("You wonder what would happen if it was infused in water.");
/* 1865 */       } else if (currentSecond == 50) {
/* 1866 */         performer.getCommunicator().sendNormalServerMessage("You crush the " + harvestable.getFruit() + " between your thumb and forefinger.");
/* 1867 */       } else if (currentSecond == 55) {
/* 1868 */         performer.getCommunicator().sendNormalServerMessage("You throw the crushed " + harvestable.getFruit() + " away.");
/*      */       } 
/* 1870 */     } else if (harvestable.isFlower()) {
/*      */       
/* 1872 */       if (currentSecond == 35) {
/* 1873 */         performer.getCommunicator().sendNormalServerMessage("You carefully pick " + harvestable.getFruitWithGenus() + ".");
/* 1874 */       } else if (currentSecond == 40) {
/* 1875 */         performer.getCommunicator().sendNormalServerMessage("You count the number of petals on the " + harvestable.getFruit() + ".");
/* 1876 */       } else if (currentSecond == 45) {
/* 1877 */         performer.getCommunicator().sendNormalServerMessage("You try to gauge the colour of the " + harvestable.getFruit() + ".");
/* 1878 */       } else if (currentSecond == 50) {
/* 1879 */         performer.getCommunicator().sendNormalServerMessage("You roll the " + harvestable.getFruit() + " between your thumb and forefinger.");
/* 1880 */       } else if (currentSecond == 55) {
/* 1881 */         performer.getCommunicator().sendNormalServerMessage("You throw the rolled " + harvestable.getFruit() + " away.");
/*      */       } 
/* 1883 */     } else if (harvestable.isNut()) {
/*      */       
/* 1885 */       if (currentSecond == 35) {
/* 1886 */         performer.getCommunicator().sendNormalServerMessage("You carefully pick " + harvestable.getFruitWithGenus() + ".");
/* 1887 */       } else if (currentSecond == 39) {
/* 1888 */         performer.getCommunicator().sendNormalServerMessage("You inspect the outside of the " + harvestable.getFruit() + ".");
/* 1889 */       } else if (currentSecond == 43) {
/* 1890 */         performer.getCommunicator().sendNormalServerMessage("You break open the " + harvestable.getFruit() + ".");
/* 1891 */       } else if (currentSecond == 47) {
/* 1892 */         performer.getCommunicator().sendNormalServerMessage("You study the " + harvestable.getFruit() + " to better understand just how old it really is.");
/* 1893 */       } else if (currentSecond == 51) {
/* 1894 */         performer.getCommunicator().sendNormalServerMessage("You taste the " + harvestable.getFruit() + ".");
/* 1895 */       } else if (currentSecond == 55) {
/* 1896 */         performer.getCommunicator().sendNormalServerMessage("You discard the " + harvestable.getFruit() + ".");
/*      */       } 
/* 1898 */     } else if (harvestable.isFruit()) {
/*      */       
/* 1900 */       if (currentSecond == 35) {
/* 1901 */         performer.getCommunicator().sendNormalServerMessage("You carefully pick " + harvestable.getFruitWithGenus() + ".");
/* 1902 */       } else if (currentSecond == 40) {
/* 1903 */         performer.getCommunicator().sendNormalServerMessage("You inspect the " + harvestable.getFruit() + ".");
/* 1904 */       } else if (currentSecond == 45) {
/* 1905 */         performer.getCommunicator().sendNormalServerMessage("You study the " + harvestable.getFruit() + " to better understand just how old it really is.");
/* 1906 */       } else if (currentSecond == 50) {
/* 1907 */         performer.getCommunicator().sendNormalServerMessage("You break open the " + harvestable.getFruit() + " to check for pips.");
/* 1908 */       } else if (currentSecond == 55) {
/* 1909 */         performer.getCommunicator().sendNormalServerMessage("You discard the " + harvestable.getFruit() + ".");
/*      */       } 
/* 1911 */     } else if (harvestable.isHops()) {
/*      */       
/* 1913 */       if (currentSecond == 35) {
/* 1914 */         performer.getCommunicator().sendNormalServerMessage("You carefully pick " + harvestable.getFruitWithGenus() + ".");
/* 1915 */       } else if (currentSecond == 40) {
/* 1916 */         performer.getCommunicator().sendNormalServerMessage("You inspect the " + harvestable.getFruit() + ".");
/* 1917 */       } else if (currentSecond == 45) {
/* 1918 */         performer.getCommunicator().sendNormalServerMessage("You squeeze the " + harvestable.getFruit() + " to see how firm they are.");
/* 1919 */       } else if (currentSecond == 50) {
/* 1920 */         performer.getCommunicator().sendNormalServerMessage("You sniff the " + harvestable.getFruit() + " to check their aroma.");
/* 1921 */       } else if (currentSecond == 55) {
/* 1922 */         performer.getCommunicator().sendNormalServerMessage("You discard the " + harvestable.getFruit() + ".");
/*      */       } 
/* 1924 */     } else if (harvestable.isBerry()) {
/*      */       
/* 1926 */       if (currentSecond == 35) {
/* 1927 */         performer.getCommunicator().sendNormalServerMessage("You carefully pick " + harvestable.getFruitWithGenus() + ".");
/* 1928 */       } else if (currentSecond == 40) {
/* 1929 */         performer.getCommunicator().sendNormalServerMessage("You study the " + harvestable.getFruit() + " to better understand just how old it really is.");
/* 1930 */       } else if (currentSecond == 45) {
/* 1931 */         performer.getCommunicator().sendNormalServerMessage("You dissect the " + harvestable.getFruit() + ".");
/* 1932 */       } else if (currentSecond == 50) {
/* 1933 */         performer.getCommunicator().sendNormalServerMessage("You taste the " + harvestable.getFruit() + " for sweetness.");
/* 1934 */       } else if (currentSecond == 55) {
/* 1935 */         performer.getCommunicator().sendNormalServerMessage("You discard the " + harvestable.getFruit() + ".");
/*      */       }
/*      */     
/*      */     }
/* 1939 */     else if (currentSecond == 35) {
/* 1940 */       performer.getCommunicator().sendNormalServerMessage("You carefully pick " + harvestable.getFruitWithGenus() + ".");
/* 1941 */     } else if (currentSecond == 40) {
/* 1942 */       performer.getCommunicator().sendNormalServerMessage("You inspect the outside of the " + harvestable.getFruit() + ".");
/* 1943 */     } else if (currentSecond == 45) {
/* 1944 */       performer.getCommunicator().sendNormalServerMessage("You dissect the " + harvestable.getFruit() + ".");
/* 1945 */     } else if (currentSecond == 50) {
/* 1946 */       performer.getCommunicator().sendNormalServerMessage("You study the inside of the " + harvestable.getFruit() + " to better understand just how old it really is.");
/* 1947 */     } else if (currentSecond == 55) {
/* 1948 */       performer.getCommunicator().sendNormalServerMessage("You discard the " + harvestable.getFruit() + ".");
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileTreeBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
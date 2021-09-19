/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.questions.OreQuestion;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.util.MaterialUtilities;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
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
/*      */ 
/*      */ public final class CaveWallBehaviour
/*      */   extends TileBehaviour
/*      */ {
/*   80 */   private static final Logger logger = Logger.getLogger(CaveWallBehaviour.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CaveWallBehaviour() {
/*   87 */     super((short)38);
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
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile, int dir) {
/*   99 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  100 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, tile));
/*  101 */     byte type = Tiles.decodeType(tile);
/*      */     
/*  103 */     if (performer.getDeity() != null && performer.getDeity().isMountainGod())
/*      */     {
/*  105 */       Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]);
/*      */     }
/*  107 */     if (type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || isPartlyClad(type))
/*      */     {
/*  109 */       toReturn.add(Actions.actionEntrys[607]);
/*      */     }
/*  111 */     return toReturn;
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
/*      */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile, int dir) {
/*  124 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  125 */     toReturn.addAll(super.getBehavioursFor(performer, subject, tilex, tiley, onSurface, tile));
/*  126 */     byte type = Tiles.decodeType(tile);
/*  127 */     if (type != Tiles.Tile.TILE_LAVA.id) {
/*      */       
/*  129 */       if (subject.isMiningtool()) {
/*      */ 
/*      */         
/*  132 */         if (!isClad(type))
/*      */         {
/*  134 */           toReturn.add(new ActionEntry((short)-4, "Mining", "Mining options"));
/*  135 */           toReturn.add(Actions.actionEntrys[145]);
/*  136 */           toReturn.add(Actions.actionEntrys[147]);
/*  137 */           toReturn.add(Actions.actionEntrys[146]);
/*  138 */           toReturn.add(Actions.actionEntrys[156]);
/*      */         }
/*      */       
/*      */       }
/*  142 */       else if (subject.getTemplateId() == 429 && type == Tiles.Tile.TILE_CAVE_WALL.id) {
/*  143 */         toReturn.add(Actions.actionEntrys[229]);
/*  144 */       } else if (subject.getTemplateId() == 525) {
/*  145 */         toReturn.add(Actions.actionEntrys[94]);
/*  146 */       } else if (subject.getTemplateId() == 526 || subject.getTemplateId() == 667) {
/*      */         
/*  148 */         if (Tiles.isOreCave(Tiles.decodeType(tile)))
/*      */         {
/*  150 */           toReturn.add(Actions.actionEntrys[118]);
/*      */         }
/*      */       }
/*  153 */       else if (subject.getTemplateId() == 668) {
/*      */         
/*  155 */         toReturn.add(Actions.actionEntrys[78]);
/*      */       } 
/*  157 */       if (performer.getDeity() != null && performer.getDeity().isMountainGod())
/*      */       {
/*  159 */         Methods.addActionIfAbsent(toReturn, Actions.actionEntrys[141]);
/*      */       }
/*  161 */       if (type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id && (subject.getTemplateId() == 493 || (subject
/*  162 */         .isWand() && performer.getPower() >= 4))) {
/*      */         
/*  164 */         List<ActionEntry> reinforce = new LinkedList<>();
/*  165 */         reinforce.add(Actions.actionEntrys[856]);
/*  166 */         reinforce.add(Actions.actionEntrys[857]);
/*  167 */         reinforce.add(Actions.actionEntrys[858]);
/*  168 */         reinforce.add(Actions.actionEntrys[859]);
/*  169 */         reinforce.add(Actions.actionEntrys[860]);
/*  170 */         reinforce.add(Actions.actionEntrys[861]);
/*  171 */         if (subject.isWand() && performer.getPower() >= 4)
/*  172 */           reinforce.add(Actions.actionEntrys[862]); 
/*  173 */         Collections.sort(reinforce);
/*  174 */         toReturn.add(new ActionEntry((short)-reinforce.size(), "Clad", "Clad options"));
/*  175 */         toReturn.addAll(reinforce);
/*      */       }
/*  177 */       else if (type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id && (subject
/*  178 */         .getTemplateId() == 63 || subject.getTemplateId() == 62)) {
/*      */         
/*  180 */         toReturn.add(Actions.actionEntrys[862]);
/*      */       }
/*  182 */       else if (subject.getTemplateId() == 493 || (subject.isWand() && performer.getPower() >= 4)) {
/*      */         
/*  184 */         if (type == Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id) {
/*  185 */           toReturn.add(new ActionEntry((short)856, "Continue cladding", "cladding"));
/*  186 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id) {
/*  187 */           toReturn.add(new ActionEntry((short)857, "Continue cladding", "cladding"));
/*  188 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id) {
/*  189 */           toReturn.add(new ActionEntry((short)858, "Continue cladding", "cladding"));
/*  190 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id) {
/*  191 */           toReturn.add(new ActionEntry((short)859, "Continue cladding", "cladding"));
/*  192 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id) {
/*  193 */           toReturn.add(new ActionEntry((short)860, "Continue cladding", "cladding"));
/*  194 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id) {
/*  195 */           toReturn.add(new ActionEntry((short)861, "Continue cladding", "cladding"));
/*  196 */         } else if (subject.isWand() && performer.getPower() >= 4 && type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id) {
/*  197 */           toReturn.add(new ActionEntry((short)862, "Continue cladding", "cladding"));
/*      */         } 
/*  199 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id && (subject
/*  200 */         .getTemplateId() == 63 || subject.getTemplateId() == 62)) {
/*      */         
/*  202 */         toReturn.add(new ActionEntry((short)862, "Continue cladding", "cladding"));
/*      */       } 
/*  204 */       if (type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id && (subject.getTemplateId() == 130 || (subject
/*  205 */         .isWand() && performer.getPower() >= 4))) {
/*      */         
/*  207 */         toReturn.add(Actions.actionEntrys[847]);
/*      */       }
/*  209 */       else if (type == Tiles.Tile.TILE_CAVE_WALL_RENDERED_REINFORCED.id && (subject.getTemplateId() == 1115 || (subject
/*  210 */         .isWand() && performer.getPower() >= 4))) {
/*      */         
/*  212 */         toReturn.add(new ActionEntry((short)847, "Remove render", "removing"));
/*      */       }
/*  214 */       else if (isClad(type) && (subject.getTemplateId() == 1115 || (subject.isWand() && performer.getPower() >= 4))) {
/*      */         
/*  216 */         toReturn.add(new ActionEntry((short)78, "Remove cladding", "removing"));
/*      */       } 
/*  218 */       if (type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || isPartlyClad(type))
/*      */       {
/*  220 */         toReturn.add(Actions.actionEntrys[607]);
/*      */       }
/*      */     } 
/*  223 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isClad(byte type) {
/*  228 */     return (type == Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_SLATE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_POTTERY_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_ROUNDED_STONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_SANDSTONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_RENDERED_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_MARBLE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_WOOD_REINFORCED.id);
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
/*      */   public static boolean isPartlyClad(byte type) {
/*  247 */     return (type == Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id);
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
/*      */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, int dir, short action, float counter) {
/*  265 */     boolean done = true;
/*  266 */     byte type = Tiles.decodeType(tile);
/*  267 */     if (action == 1) {
/*      */       
/*  269 */       Communicator comm = performer.getCommunicator();
/*  270 */       if (Tiles.isOreCave(type)) {
/*      */         
/*  272 */         if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_COPPER.id) {
/*  273 */           comm.sendNormalServerMessage("A vein of pure copper emerges here.");
/*  274 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_GOLD.id) {
/*  275 */           comm.sendNormalServerMessage("A vein of pure gold emerges here.");
/*  276 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_SILVER.id) {
/*  277 */           comm.sendNormalServerMessage("A vein of pure silver emerges here.");
/*  278 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_TIN.id) {
/*  279 */           comm.sendNormalServerMessage("A vein of pure tin emerges here.");
/*  280 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id) {
/*  281 */           comm.sendNormalServerMessage("A vein of pure iron emerges here.");
/*  282 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_ZINC.id) {
/*  283 */           comm.sendNormalServerMessage("A vein of pure zinc emerges here.");
/*  284 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_LEAD.id) {
/*  285 */           comm.sendNormalServerMessage("A vein of pure lead emerges here.");
/*  286 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_ADAMANTINE.id) {
/*  287 */           comm.sendNormalServerMessage("A vein of black adamantine emerges here.");
/*  288 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL.id) {
/*  289 */           comm.sendNormalServerMessage("A vein of shiny glimmersteel emerges here.");
/*  290 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_MARBLE.id) {
/*  291 */           comm.sendNormalServerMessage("A vein of an interlocking mosaic of carbonate crystals, otherwise known as marble, emerges here.");
/*  292 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_SLATE.id) {
/*  293 */           comm.sendNormalServerMessage("A vein of fine-grained, foliated, homogeneous metamorphic rock, or slate as we know it, emerges here.");
/*  294 */         } else if (type == Tiles.Tile.TILE_CAVE_WALL_SANDSTONE.id) {
/*  295 */           comm.sendNormalServerMessage("A vein of sand-sized minerals or rock grains, composed of quartz or feldspar, because these are the most common minerals in the Wurm's crust.");
/*      */         } else {
/*  297 */           comm.sendNormalServerMessage("Unknown vein!");
/*      */         } 
/*  299 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) {
/*  300 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams and metal bands.");
/*  301 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_WOOD_REINFORCED.id) {
/*  302 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and planks.");
/*  303 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id) {
/*  304 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and stone bricks.");
/*  305 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_SLATE_REINFORCED.id) {
/*  306 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and slate bricks.");
/*  307 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_ROUNDED_STONE_REINFORCED.id) {
/*  308 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and rounded stone bricks.");
/*  309 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_POTTERY_REINFORCED.id) {
/*  310 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and pottery bricks.");
/*  311 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_SANDSTONE_REINFORCED.id) {
/*  312 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and sandstone bricks.");
/*  313 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_RENDERED_REINFORCED.id) {
/*  314 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and stone bricks that have been rendered.");
/*  315 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_MARBLE_REINFORCED.id) {
/*  316 */         comm.sendNormalServerMessage("The cave wall has been reinforced with thick wooden beams, metal bands and marble bricks.");
/*  317 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_LAVA.id) {
/*  318 */         comm.sendNormalServerMessage("Thick, slow flowing lava blocks your way.");
/*  319 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id) {
/*  320 */         comm.sendNormalServerMessage("A vein of crystalized sediment formed over time by evaporation.");
/*  321 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_SANDSTONE.id) {
/*  322 */         comm.sendNormalServerMessage("A vein of clastic sedimentary rock composed mainly of sand-sized minerals or rock grains.");
/*  323 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id) {
/*  324 */         comm.sendNormalServerMessage(partlyCladExamine(tilex, tiley, "plank", false));
/*  325 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id) {
/*  326 */         comm.sendNormalServerMessage(partlyCladExamine(tilex, tiley, "stone brick", true));
/*  327 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id) {
/*  328 */         comm.sendNormalServerMessage(partlyCladExamine(tilex, tiley, "slate brick", true));
/*  329 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id) {
/*  330 */         comm.sendNormalServerMessage(partlyCladExamine(tilex, tiley, "rounded stone brick", true));
/*  331 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id) {
/*  332 */         comm.sendNormalServerMessage(partlyCladExamine(tilex, tiley, "pottery brick", true));
/*  333 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id) {
/*  334 */         comm.sendNormalServerMessage(partlyCladExamine(tilex, tiley, "sandstone brick", true));
/*  335 */       } else if (type == Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id) {
/*  336 */         comm.sendNormalServerMessage(partlyCladExamine(tilex, tiley, "marble brick", true));
/*      */       } else {
/*  338 */         comm.sendNormalServerMessage("You see dark dungeons.");
/*      */       } 
/*  340 */     } else if (action == 109) {
/*      */       
/*  342 */       performer.getCommunicator().sendNormalServerMessage("You cannot track there.");
/*      */     }
/*  344 */     else if (action == 141 && performer.getDeity() != null && performer.getDeity().isMountainGod()) {
/*      */       
/*  346 */       done = MethodsReligion.pray(act, performer, counter);
/*      */     }
/*  348 */     else if (action == 607) {
/*      */ 
/*      */       
/*  351 */       long target = Tiles.getTileId(tilex, tiley, 0, false);
/*  352 */       performer.getCommunicator().sendAddCaveWallToCreationWindow(target, type, -10L);
/*      */     } else {
/*      */       
/*  355 */       done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/*      */     } 
/*  357 */     return done;
/*      */   }
/*      */ 
/*      */   
/*      */   private String partlyCladExamine(int tilex, int tiley, String brickName, boolean needsMortar) {
/*  362 */     int resource = Server.getCaveResource(tilex, tiley);
/*  363 */     int needs = resource >>> 8;
/*  364 */     String requires = "  It requires " + needs + " more " + brickName + ((needs > 1) ? "s" : "") + (needsMortar ? " and the same number of mortar" : ((needs == 10) ? " and one large nail" : "")) + ".";
/*      */ 
/*      */     
/*  367 */     return "The cave wall has been reinforced with thick wooden beams, metal bands and partly clad with " + brickName + "." + requires;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Blocker checkForBlockersBetween(TilePos pos, int tileSurfaceSide, int floorLevel) {
/*  372 */     VolaTile posTile = Zones.getTileOrNull(pos, false);
/*  373 */     if (posTile != null) {
/*      */       
/*  375 */       Wall[] walls = posTile.getWalls();
/*  376 */       for (Wall wall : walls) {
/*      */         
/*  378 */         if (wall.getFloorLevel() == floorLevel && !wall.isDoor() && isBlockerBlockingSurfaceSide(pos, (Blocker)wall, tileSurfaceSide))
/*      */         {
/*  380 */           return (Blocker)wall;
/*      */         }
/*      */       } 
/*      */       
/*  384 */       Fence[] fences = posTile.getFences();
/*  385 */       for (Fence fence : fences) {
/*      */         
/*  387 */         if (fence.getFloorLevel() == floorLevel && !fence.isDoor() && isBlockerBlockingSurfaceSide(pos, (Blocker)fence, tileSurfaceSide))
/*  388 */           return (Blocker)fence; 
/*      */       } 
/*      */     } 
/*  391 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isBlockerBlockingSurfaceSide(TilePos pos, Blocker blocker, int surfaceSide) {
/*  396 */     switch (surfaceSide) {
/*      */       
/*      */       case 2:
/*  399 */         return blocker.isOnWestBorder(pos);
/*      */       case 5:
/*  401 */         return blocker.isOnSouthBorder(pos);
/*      */       case 3:
/*  403 */         return blocker.isOnNorthBorder(pos);
/*      */       case 4:
/*  405 */         return blocker.isOnEastBorder(pos);
/*      */     } 
/*  407 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Blocker isCaveWallBlocked(TilePos checkedTile, int actionSurface, int floorLevel) {
/*      */     TilePos vtile;
/*      */     int newSurface;
/*  414 */     switch (actionSurface) {
/*      */       
/*      */       case 2:
/*  417 */         vtile = checkedTile.East();
/*      */         break;
/*      */       case 5:
/*  420 */         vtile = checkedTile.North();
/*      */         break;
/*      */       case 3:
/*  423 */         vtile = checkedTile.South();
/*      */         break;
/*      */       case 4:
/*  426 */         vtile = checkedTile.West();
/*      */         break;
/*      */       default:
/*  429 */         return null;
/*      */     } 
/*  431 */     Blocker blocker = checkForBlockersBetween(vtile, actionSurface, floorLevel);
/*  432 */     if (blocker != null) {
/*  433 */       return blocker;
/*      */     }
/*  435 */     switch (actionSurface) {
/*      */       
/*      */       case 2:
/*  438 */         newSurface = 4;
/*      */         break;
/*      */       case 5:
/*  441 */         newSurface = 3;
/*      */         break;
/*      */       case 3:
/*  444 */         newSurface = 5;
/*      */         break;
/*      */       case 4:
/*  447 */         newSurface = 2;
/*      */         break;
/*      */       
/*      */       default:
/*  451 */         return null;
/*      */     } 
/*  453 */     Blocker blocker2 = checkForBlockersBetween(checkedTile, newSurface, floorLevel);
/*  454 */     if (blocker2 != null)
/*  455 */       return blocker2; 
/*  456 */     return null;
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
/*      */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, int dir, short action, float counter) {
/*  468 */     boolean done = true;
/*  469 */     byte type = Tiles.decodeType(tile);
/*  470 */     if (source.isMiningtool() && !isClad(type) && (action == 145 || action == 147 || action == 146)) {
/*      */ 
/*      */       
/*  473 */       if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */         
/*  475 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to mine.", (byte)3);
/*  476 */         return true;
/*      */       } 
/*  478 */       if (Zones.isTileProtected(tilex, tiley)) {
/*      */         
/*  480 */         performer.getCommunicator().sendNormalServerMessage("This tile is protected by the gods. You can not mine here.", (byte)3);
/*      */         
/*  482 */         return true;
/*      */       } 
/*  484 */       boolean ok = true;
/*  485 */       if (type != Tiles.Tile.TILE_CAVE_WALL.id && type != Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id && 
/*      */         
/*  487 */         !Tiles.isOreCave(type)) {
/*      */         
/*  489 */         ok = false;
/*  490 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) {
/*      */           
/*  492 */           if (performer.getPower() >= 2) {
/*      */             
/*  494 */             logger.log(Level.WARNING, performer.getName() + " removed reinforced cave wall at " + tile + " with GM powers.");
/*      */             
/*  496 */             ok = true;
/*      */           }
/*      */           else {
/*      */             
/*  500 */             VolaTile vt = Zones.getOrCreateTile(tilex, tiley, onSurface);
/*  501 */             Village vill = vt.getVillage();
/*  502 */             if (vill == null) {
/*  503 */               ok = false;
/*      */             } else {
/*  505 */               if (!Methods.isActionAllowed(performer, (short)229, false, tilex, tiley, tile, 2) || 
/*  506 */                 !Methods.isActionAllowed(performer, (short)145, false, tilex, tiley, tile, 2)) {
/*  507 */                 return true;
/*      */               }
/*  509 */               ok = true;
/*      */             } 
/*      */           } 
/*  512 */         } else if (!Methods.isActionAllowed(performer, (short)145, false, tilex, tiley, tile, 2)) {
/*  513 */           return true;
/*      */         } 
/*  515 */         if (!ok) {
/*      */           
/*  517 */           performer.getCommunicator().sendNormalServerMessage("The rock is too hard to mine.", (byte)3);
/*  518 */           return true;
/*      */         } 
/*      */       } 
/*  521 */       if (performer.isOnSurface()) {
/*      */         
/*  523 */         performer.getCommunicator().sendNormalServerMessage("You are too far away to mine there.", (byte)3);
/*  524 */         return true;
/*      */       } 
/*  526 */       int digTilex = performer.getTileX();
/*  527 */       int digTiley = performer.getTileY();
/*      */       
/*  529 */       if (dir == 1) {
/*      */         
/*  531 */         performer.getCommunicator().sendNormalServerMessage("The rock is too hard to mine.", (byte)3);
/*  532 */         logger.log(Level.WARNING, performer.getName() + " Tried to mine the roof of a cave wall.");
/*  533 */         return true;
/*      */       } 
/*  535 */       Blocker blocker = isCaveWallBlocked(TilePos.fromXY(tilex, tiley), dir, performer.getFloorLevel());
/*  536 */       if (blocker != null) {
/*      */         
/*  538 */         performer.getCommunicator().sendNormalServerMessage("The " + blocker.getName() + " is in the way.", (byte)3);
/*  539 */         if (performer.getPower() > 0)
/*      */         {
/*  541 */           performer.getCommunicator().sendNormalServerMessage(blocker.getName() + " is at " + blocker.getPositionX() + ", " + blocker.getPositionY() + " horiz=" + blocker.isHorizontal() + " wurmid=" + blocker.getId());
/*      */         }
/*  543 */         return done;
/*      */       } 
/*  545 */       short h = Tiles.decodeHeight(tile);
/*  546 */       if (h >= -25 || h == -100) {
/*      */         
/*  548 */         done = false;
/*  549 */         Skills skills = performer.getSkills();
/*  550 */         Skill mining = null;
/*  551 */         Skill tool = null;
/*  552 */         boolean insta = (performer.getPower() >= 2 && source.isWand());
/*      */         
/*      */         try {
/*  555 */           mining = skills.getSkill(1008);
/*      */         }
/*  557 */         catch (Exception ex) {
/*      */           
/*  559 */           mining = skills.learn(1008, 1.0F);
/*      */         } 
/*      */         
/*      */         try {
/*  563 */           tool = skills.getSkill(source.getPrimarySkill());
/*      */         }
/*  565 */         catch (Exception ex) {
/*      */ 
/*      */           
/*      */           try {
/*  569 */             tool = skills.learn(source.getPrimarySkill(), 1.0F);
/*      */           }
/*  571 */           catch (NoSuchSkillException nse) {
/*      */             
/*  573 */             logger.log(Level.WARNING, performer.getName() + " trying to mine with an item with no primary skill: " + source
/*  574 */                 .getName());
/*      */           } 
/*      */         } 
/*  577 */         int time = 0;
/*  578 */         if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  585 */           if (!Methods.isActionAllowed(performer, action, false, tilex, tiley, tile, dir))
/*  586 */             return true; 
/*  587 */           VolaTile dropTile = Zones.getTileOrNull(performer.getTilePos(), onSurface);
/*  588 */           if (dropTile != null)
/*      */           {
/*  590 */             if (dropTile.getNumberOfItems(performer.getFloorLevel()) > 99) {
/*      */               
/*  592 */               performer.getCommunicator().sendNormalServerMessage("There is no space to mine here. Clear the area first.", (byte)3);
/*      */               
/*  594 */               return true;
/*      */             } 
/*      */           }
/*      */           
/*  598 */           time = Actions.getVariableActionTime(performer, mining, source, 0.0D, 250, 25, 8000);
/*      */           
/*      */           try {
/*  601 */             performer.getCurrentAction().setTimeLeft(time);
/*      */           }
/*  603 */           catch (NoSuchActionException nsa) {
/*      */             
/*  605 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/*  607 */           String dirstring = "";
/*  608 */           if (action == 147) {
/*  609 */             dirstring = " down";
/*  610 */           } else if (action == 146) {
/*  611 */             dirstring = " up";
/*  612 */           }  Server.getInstance()
/*  613 */             .broadCastAction(performer.getName() + " starts mining" + dirstring + ".", performer, 5);
/*  614 */           performer.getCommunicator().sendNormalServerMessage("You start to mine" + dirstring + ".");
/*  615 */           performer.sendActionControl(Actions.actionEntrys[145]
/*  616 */               .getVerbString() + " " + dirstring, true, time);
/*      */           
/*  618 */           source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*  619 */           performer.getStatus().modifyStamina(-1000.0F);
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */ 
/*      */             
/*  626 */             time = performer.getCurrentAction().getTimeLeft();
/*      */           }
/*  628 */           catch (NoSuchActionException nsa) {
/*      */             
/*  630 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/*  632 */           if (counter * 10.0F <= time && !insta) {
/*      */             
/*  634 */             if (act.currentSecond() % 5 == 0 || (act.currentSecond() == 3 && time < 50))
/*      */             {
/*  636 */               String sstring = "sound.work.mining1";
/*  637 */               int x = Server.rand.nextInt(3);
/*  638 */               if (x == 0) {
/*  639 */                 sstring = "sound.work.mining2";
/*  640 */               } else if (x == 1) {
/*  641 */                 sstring = "sound.work.mining3";
/*      */               } 
/*  643 */               SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 1.0F);
/*  644 */               source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/*  645 */               performer.getStatus().modifyStamina(-7000.0F);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  650 */             if (act.getRarity() != 0)
/*      */             {
/*  652 */               performer.playPersonalSound("sound.fx.drumroll");
/*      */             }
/*  654 */             VolaTile dropTile = Zones.getTileOrNull(performer.getTilePos(), onSurface);
/*  655 */             if (dropTile != null)
/*      */             {
/*  657 */               if (dropTile.getNumberOfItems(performer.getFloorLevel()) > 99) {
/*      */                 
/*  659 */                 performer.getCommunicator().sendNormalServerMessage("There is no space to mine here. Clear the area first.", (byte)3);
/*      */                 
/*  661 */                 return true;
/*      */               } 
/*      */             }
/*  664 */             double bonus = 0.0D;
/*  665 */             double power = 0.0D;
/*      */             
/*  667 */             done = true;
/*  668 */             int resource = Server.getCaveResource(tilex, tiley);
/*  669 */             if (resource == 65535)
/*      */             {
/*  671 */               resource = Server.rand.nextInt(10000);
/*      */             }
/*      */             
/*  674 */             int itemTemplateCreated = TileRockBehaviour.getItemTemplateForTile(Tiles.decodeType(tile));
/*  675 */             if (resource > 50 && (itemTemplateCreated == 693 || itemTemplateCreated == 697))
/*      */             {
/*  677 */               resource = 50;
/*      */             }
/*      */ 
/*      */             
/*  681 */             float diff = TileRockBehaviour.getDifficultyForTile(Tiles.decodeType(tile));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  689 */             if (resource > 0)
/*  690 */               resource--; 
/*  691 */             Server.setCaveResource(tilex, tiley, resource);
/*  692 */             boolean dryrun = false;
/*      */ 
/*      */ 
/*      */             
/*  696 */             byte state = Zones.getMiningState(tilex, tiley);
/*  697 */             boolean createItem = true;
/*  698 */             int randint = Server.rand.nextInt(10);
/*      */             
/*  700 */             boolean normal = (itemTemplateCreated == 146 || itemTemplateCreated == 1238);
/*      */ 
/*      */             
/*  703 */             if (normal && state > Servers.localServer.getTunnelingHits() && state <= Servers.localServer.getTunnelingHits() + 5 + randint)
/*      */             {
/*  705 */               if (!TileRockBehaviour.isInsideTunnelOk(tilex, tiley, tile, action, dir, performer, false)) {
/*      */                 
/*  707 */                 dryrun = true;
/*  708 */                 createItem = false;
/*      */               } 
/*      */             }
/*      */             
/*  712 */             if ((normal && state > Servers.localServer.getTunnelingHits() + randint) || (!normal && resource <= 0) || insta) {
/*      */               
/*  714 */               if (!TileRockBehaviour.createInsideTunnel(tilex, tiley, tile, performer, action, dir, false, act))
/*      */               {
/*  716 */                 dryrun = true;
/*  717 */                 if (!normal || Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) {
/*      */                   
/*  719 */                   TileEvent.log(tilex, tiley, -1, performer.getWurmId(), action);
/*  720 */                   if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) {
/*  721 */                     performer.getCommunicator().sendNormalServerMessage("You manage to remove the reinforcement.");
/*      */                   } else {
/*      */                     
/*  724 */                     performer.getCommunicator().sendNormalServerMessage("The mine is now depleted.", (byte)3);
/*  725 */                   }  Server.caveMesh
/*  726 */                     .setTile(tilex, tiley, 
/*      */ 
/*      */                       
/*  729 */                       Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_CAVE_WALL.id, 
/*  730 */                         Tiles.decodeData(tile)));
/*  731 */                   TileRockBehaviour.sendCaveTile(tilex, tiley, 0, 0);
/*      */                 } 
/*  733 */                 createItem = false;
/*      */               }
/*      */               else
/*      */               {
/*  737 */                 if (itemTemplateCreated == 1238 || itemTemplateCreated == 1116) {
/*      */ 
/*      */ 
/*      */                   
/*  741 */                   Zones.setMiningState(tilex, tiley, (byte)-1, false);
/*      */                 }
/*      */                 else {
/*      */                   
/*  745 */                   Zones.setMiningState(tilex, tiley, (byte)0, false);
/*  746 */                   Zones.deleteMiningTile(tilex, tiley);
/*      */                 } 
/*  748 */                 performer.getCommunicator().sendNormalServerMessage("The wall breaks!");
/*  749 */                 Random rockRandom = new Random();
/*      */                 
/*  751 */                 rockRandom.setSeed((tilex + tiley * Zones.worldTileSizeY) * TileRockBehaviour.SOURCE_PRIME);
/*  752 */                 if (rockRandom.nextInt(TileRockBehaviour.sourceFactor) == 0)
/*      */                 {
/*  754 */                   TileRockBehaviour.SOURCE_PRIME += Server.rand.nextInt(10000);
/*      */                 
/*      */                 }
/*      */               }
/*      */             
/*      */             }
/*  760 */             else if (normal) {
/*      */               
/*  762 */               if (state < 56) {
/*      */                 
/*  764 */                 state = (byte)(state + 1);
/*  765 */                 if (state >= Servers.localServer.getTunnelingHits())
/*  766 */                   performer.getCommunicator().sendNormalServerMessage("The wall will break soon."); 
/*      */               } 
/*  768 */               Zones.setMiningState(tilex, tiley, state, false);
/*      */             }
/*  770 */             else if (resource < 5) {
/*      */               
/*  772 */               performer.getCommunicator().sendNormalServerMessage("The wall will break soon.");
/*      */             } 
/*      */ 
/*      */             
/*  776 */             boolean useNewSystem = false;
/*  777 */             float tickCounter = counter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  786 */             if (tool != null) {
/*  787 */               bonus = tool.skillCheck(diff, source, 0.0D, dryrun, tickCounter) / 5.0D;
/*      */             }
/*  789 */             power = Math.max(1.0D, mining
/*  790 */                 .skillCheck(diff, source, bonus, dryrun, tickCounter));
/*  791 */             if (performer.getTutorialLevel() == 10 && !performer.skippedTutorial())
/*      */             {
/*  793 */               performer.missionFinished(true, true);
/*      */             }
/*  795 */             if (createItem) {
/*      */               
/*      */               try {
/*      */ 
/*      */                 
/*  800 */                 double imbueEnhancement = 1.0D + 0.23047D * source.getSkillSpellImprovement(1008) / 100.0D;
/*      */                 
/*  802 */                 if (mining.getKnowledge(0.0D) * imbueEnhancement < power)
/*  803 */                   power = mining.getKnowledge(0.0D) * imbueEnhancement; 
/*  804 */                 r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/*  805 */                 int m = TileRockBehaviour.MAX_QL;
/*  806 */                 if (normal || itemTemplateCreated == 38) {
/*  807 */                   m = 100;
/*      */                 }
/*  809 */                 int max = (int)Math.min(m, (20 + r
/*  810 */                     .nextInt(80)) * imbueEnhancement);
/*      */                 
/*  812 */                 if (state == -1)
/*  813 */                   max = 99; 
/*  814 */                 power = Math.min(power, max);
/*  815 */                 if (source.isCrude()) {
/*  816 */                   power = 1.0D;
/*      */                 }
/*  818 */                 float modifier = 1.0F;
/*  819 */                 if (source.getSpellEffects() != null)
/*      */                 {
/*  821 */                   modifier *= source.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*      */                 }
/*      */ 
/*      */                 
/*  825 */                 float orePower = GeneralUtilities.calcOreRareQuality(power * modifier, act.getRarity(), source.getRarity());
/*      */                 
/*  827 */                 Item newItem = ItemFactory.createItem(itemTemplateCreated, orePower, act.getRarity(), null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  835 */                 if (itemTemplateCreated == 39)
/*  836 */                   performer.achievement(372); 
/*  837 */                 newItem.setLastOwnerId(performer.getWurmId());
/*  838 */                 newItem.setDataXY(tilex, tiley);
/*  839 */                 newItem.putItemInfrontof(performer, 0.0F);
/*      */                 
/*  841 */                 performer.getCommunicator().sendNormalServerMessage("You mine some " + newItem.getName() + ".");
/*  842 */                 Server.getInstance().broadCastAction(performer
/*  843 */                     .getName() + " mines some " + newItem.getName() + ".", performer, 5);
/*  844 */                 TileRockBehaviour.createGem(tilex, tiley, digTilex, digTiley, performer, power, false, act);
/*  845 */                 if (performer.getDeity() != null && (performer.getDeity()).number == 2)
/*      */                 {
/*  847 */                   performer.maybeModifyAlignment(0.5F);
/*      */                 }
/*      */                 
/*  850 */                 if (itemTemplateCreated == 38) {
/*      */                   
/*  852 */                   performer.achievement(516);
/*  853 */                   PlayerTutorial.firePlayerTrigger(performer.getWurmId(), PlayerTutorial.PlayerTrigger.MINE_IRON);
/*      */                 }
/*  855 */                 else if (itemTemplateCreated == 43) {
/*  856 */                   performer.achievement(527);
/*  857 */                 } else if (itemTemplateCreated == 207) {
/*  858 */                   performer.achievement(528);
/*      */                 } 
/*  860 */                 if (newItem.isMetal() && newItem.getCurrentQualityLevel() >= 80.0F) {
/*  861 */                   performer.achievement(603);
/*      */                 }
/*  863 */               } catch (Exception ex) {
/*      */                 
/*  865 */                 logger.log(Level.WARNING, "Factory failed to produce item", ex);
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/*  872 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to mine.", (byte)3);
/*      */       } 
/*  874 */     } else if (source.isMiningtool() && action == 156) {
/*      */       
/*  876 */       if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */         
/*  878 */         performer.getCommunicator().sendNormalServerMessage("The water is too deep to prospect.", (byte)3);
/*  879 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  883 */       Blocker blocker = isCaveWallBlocked(TilePos.fromXY(tilex, tiley), dir, performer.getFloorLevel());
/*  884 */       if (blocker != null) {
/*      */         
/*  886 */         performer.getCommunicator().sendNormalServerMessage("The " + blocker.getName() + " is in the way.", (byte)3);
/*  887 */         return done;
/*      */       } 
/*  889 */       Skills skills = performer.getSkills();
/*  890 */       Skill prospecting = null;
/*  891 */       done = false;
/*      */       
/*      */       try {
/*  894 */         prospecting = skills.getSkill(10032);
/*      */       }
/*  896 */       catch (Exception ex) {
/*      */         
/*  898 */         prospecting = skills.learn(10032, 1.0F);
/*      */       } 
/*  900 */       int time = 0;
/*  901 */       if (counter == 1.0F) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  909 */         String sstring = "sound.work.prospecting1";
/*  910 */         int x = Server.rand.nextInt(3);
/*  911 */         if (x == 0) {
/*  912 */           sstring = "sound.work.prospecting2";
/*  913 */         } else if (x == 1) {
/*  914 */           sstring = "sound.work.prospecting3";
/*      */         } 
/*  916 */         SoundPlayer.playSound(sstring, tilex, tiley, performer.isOnSurface(), 1.0F);
/*  917 */         time = (int)Math.max(30.0D, 100.0D - prospecting.getKnowledge(source, 0.0D));
/*      */         
/*      */         try {
/*  920 */           performer.getCurrentAction().setTimeLeft(time);
/*      */         }
/*  922 */         catch (NoSuchActionException nsa) {
/*      */           
/*  924 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*  926 */         performer.getCommunicator().sendNormalServerMessage("You start to gather fragments of the rock.");
/*  927 */         Server.getInstance().broadCastAction(performer.getName() + " starts gathering fragments of the rock.", performer, 5);
/*      */         
/*  929 */         performer.sendActionControl(Actions.actionEntrys[156].getVerbString(), true, time);
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */           
/*  936 */           time = performer.getCurrentAction().getTimeLeft();
/*      */         }
/*  938 */         catch (NoSuchActionException nsa) {
/*      */           
/*  940 */           logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */         } 
/*      */       } 
/*  943 */       if (counter * 10.0F > time) {
/*      */         
/*  945 */         performer.getStatus().modifyStamina(-3000.0F);
/*  946 */         prospecting.skillCheck(1.0D, source, 0.0D, false, counter);
/*  947 */         source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*  948 */         done = true;
/*  949 */         int resource = Server.getCaveResource(tilex, tiley);
/*      */         
/*  951 */         if (resource == 65535) {
/*      */           
/*  953 */           resource = Server.rand.nextInt(10000);
/*  954 */           Server.setCaveResource(tilex, tiley, resource);
/*      */         } 
/*  956 */         int itemTemplate = TileRockBehaviour.getItemTemplateForTile(Tiles.decodeType(tile));
/*      */         
/*      */         try {
/*  959 */           ItemTemplate t = ItemTemplateFactory.getInstance().getTemplate(itemTemplate);
/*  960 */           if (type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || isClad(type)) {
/*  961 */             performer.getCommunicator().sendNormalServerMessage("This wall is reinforced and may not be mined.");
/*  962 */           } else if (type == Tiles.Tile.TILE_CAVE_WALL_ROCKSALT.id) {
/*  963 */             performer.getCommunicator().sendNormalServerMessage("You would mine " + t.getName() + " here.");
/*      */           } else {
/*  965 */             performer.getCommunicator().sendNormalServerMessage("You would mine " + 
/*  966 */                 MaterialUtilities.getMaterialString(t.getMaterial()) + " " + t.getName() + " here.");
/*      */           }
/*      */         
/*  969 */         } catch (NoSuchTemplateException nst) {
/*      */           
/*  971 */           logger.log(Level.WARNING, performer.getName() + " - " + nst.getMessage() + ": " + itemTemplate + " at " + tilex + ", " + tiley);
/*      */         } 
/*      */         
/*  974 */         if (type != Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id && !isClad(type)) {
/*      */           
/*  976 */           if (prospecting.getKnowledge(0.0D) > 20.0D) {
/*      */             
/*  978 */             r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/*  979 */             int m = TileRockBehaviour.MAX_QL;
/*  980 */             if (itemTemplate == 146 || itemTemplate == 1238 || itemTemplate == 1116 || itemTemplate == 38)
/*      */             {
/*  982 */               m = 100; } 
/*  983 */             int max = Math.min(m, 20 + r.nextInt(80));
/*      */             
/*  985 */             byte state = Zones.getMiningState(tilex, tiley);
/*  986 */             if (state == -1)
/*  987 */               max = 99; 
/*  988 */             if (prospecting.getKnowledge(0.0D) > 80.0D) {
/*  989 */               performer.getCommunicator().sendNormalServerMessage("It has a max quality of " + max + ".");
/*      */             } else {
/*  991 */               performer.getCommunicator().sendNormalServerMessage("It is of " + getShardQlDescription(max) + ".");
/*      */             } 
/*  993 */           }  if (prospecting.getKnowledge(0.0D) > 60.0D)
/*      */           {
/*  995 */             if (itemTemplate != 146 && itemTemplate != 1238)
/*      */             {
/*  997 */               if (prospecting.getKnowledge(0.0D) > 90.0D) {
/*      */                 
/*  999 */                 performer.getCommunicator().sendNormalServerMessage("You will be able to mine here " + Server.getCaveResource(tilex, tiley) + " more times.");
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/* 1004 */                 String numString = "There is plenty of ore left.";
/* 1005 */                 if (resource > 10000) {
/* 1006 */                   numString = "There is plenty of ore left.";
/* 1007 */                 } else if (resource > 5000) {
/* 1008 */                   numString = "Only a few weeks mining remain here.";
/* 1009 */                 } else if (resource > 3000) {
/* 1010 */                   numString = "The ore is starting to deplete.";
/* 1011 */                 } else if (resource > 1000) {
/* 1012 */                   numString = "You should start to prospect for another vein of this ore.";
/* 1013 */                 } else if (resource > 100) {
/* 1014 */                   numString = "The ore will run out soon.";
/*      */                 } else {
/* 1016 */                   numString = "The ore will run out any hour.";
/* 1017 */                 }  performer.getCommunicator().sendNormalServerMessage(numString);
/*      */               } 
/*      */             }
/*      */           }
/* 1021 */           if (prospecting.getKnowledge(0.0D) > 40.0D) {
/*      */             
/* 1023 */             r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 102533L);
/* 1024 */             boolean saltExists = (r.nextInt(100) == 0);
/* 1025 */             if (saltExists)
/* 1026 */               performer.getCommunicator().sendNormalServerMessage("You will find salt here!"); 
/*      */           } 
/* 1028 */           if (prospecting.getKnowledge(0.0D) > 20.0D) {
/*      */             
/* 1030 */             r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 6883L);
/*      */             
/* 1032 */             if (r.nextInt(200) == 0)
/* 1033 */               performer.getCommunicator().sendNormalServerMessage("You will find flint here!"); 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1038 */       if (source.getTemplateId() == 429 && action == 229) {
/*      */         
/* 1040 */         if (Tiles.decodeType(tile) != Tiles.Tile.TILE_CAVE_WALL.id) {
/*      */           
/* 1042 */           if (Tiles.isOreCave(Tiles.decodeType(tile))) {
/* 1043 */             performer.getCommunicator().sendNormalServerMessage("You can not reinforce ore veins.", (byte)3);
/*      */           } else {
/* 1045 */             performer.getCommunicator().sendNormalServerMessage("The wall does not need reinforcing.", (byte)3);
/* 1046 */           }  return true;
/*      */         } 
/*      */         
/* 1049 */         if (!Methods.isActionAllowed(performer, (short)229, false, tilex, tiley, tile, 2) || 
/* 1050 */           !Methods.isActionAllowed(performer, (short)145, false, tilex, tiley, tile, 2))
/* 1051 */           return true; 
/* 1052 */         if (!GeneralUtilities.isValidTileLocation(tilex, tiley)) {
/*      */           
/* 1054 */           performer.getCommunicator().sendNormalServerMessage("The water is too deep here.", (byte)3);
/* 1055 */           return true;
/*      */         } 
/*      */         
/* 1058 */         done = false;
/* 1059 */         Skills skills = performer.getSkills();
/* 1060 */         Skill mining = null;
/* 1061 */         boolean insta = (performer.getPower() > 3);
/*      */         
/*      */         try {
/* 1064 */           mining = skills.getSkill(1008);
/*      */         }
/* 1066 */         catch (Exception ex) {
/*      */           
/* 1068 */           mining = skills.learn(1008, 1.0F);
/*      */         } 
/* 1070 */         int time = 0;
/* 1071 */         if (counter == 1.0F) {
/*      */           
/* 1073 */           SoundPlayer.playSound("sound.work.masonry", tilex, tiley, performer.isOnSurface(), 1.0F);
/*      */           
/* 1075 */           time = Math.min(250, Actions.getStandardActionTime(performer, mining, source, 0.0D));
/*      */           
/*      */           try {
/* 1078 */             performer.getCurrentAction().setTimeLeft(time);
/*      */           }
/* 1080 */           catch (NoSuchActionException nsa) {
/*      */             
/* 1082 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/* 1084 */           performer.getCommunicator().sendNormalServerMessage("You start to reinforce the rock wall.");
/* 1085 */           Server.getInstance().broadCastAction(performer.getName() + " starts to reinforce the rock wall.", performer, 5);
/* 1086 */           performer.sendActionControl(Actions.actionEntrys[229].getVerbString(), true, time);
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 1093 */             time = performer.getCurrentAction().getTimeLeft();
/*      */           }
/* 1095 */           catch (NoSuchActionException nsa) {
/*      */             
/* 1097 */             logger.log(Level.INFO, "This action does not exist?", (Throwable)nsa);
/*      */           } 
/*      */         } 
/* 1100 */         if (counter * 10.0F > time || insta) {
/*      */           
/* 1102 */           mining.skillCheck(20.0D, source, 0.0D, false, counter);
/* 1103 */           done = true;
/* 1104 */           performer.getCommunicator().sendNormalServerMessage("You reinforce the wall.");
/* 1105 */           Server.getInstance().broadCastAction(performer.getName() + " reinforces the rock wall.", performer, 5);
/* 1106 */           Items.destroyItem(source.getWurmId());
/* 1107 */           Server.caveMesh.setTile(tilex, tiley, 
/* 1108 */               Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id, Tiles.decodeData(tile)));
/* 1109 */           Zones.setMiningState(tilex, tiley, (byte)-1, false);
/* 1110 */           Players.getInstance().sendChangedTile(tilex, tiley, false, true);
/*      */         } 
/* 1112 */         return done;
/*      */       } 
/* 1114 */       if (action == 78 && source.getTemplateId() == 668)
/*      */       
/* 1116 */       { done = true;
/* 1117 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE_WALL.id) {
/*      */           
/* 1119 */           OreQuestion ctq = new OreQuestion(performer, tilex, tiley, source);
/* 1120 */           ctq.sendQuestion();
/*      */         } else {
/*      */           
/* 1123 */           performer.getCommunicator().sendNormalServerMessage("This rod only works on normal cave walls.");
/*      */         }  }
/* 1125 */       else if (action == 118 && (source.getTemplateId() == 526 || source
/* 1126 */         .getTemplateId() == 667))
/*      */       
/* 1128 */       { done = true;
/* 1129 */         if (source.getTemplateId() == 526) {
/*      */           
/* 1131 */           performer.getCommunicator().sendNormalServerMessage("You draw a circle in the air in front of you with " + source
/* 1132 */               .getNameWithGenus() + ".");
/* 1133 */           Server.getInstance().broadCastAction(performer
/* 1134 */               .getName() + " draws a circle in the air in front of " + performer.getHimHerItString() + " with " + source
/* 1135 */               .getNameWithGenus() + ".", performer, 5);
/* 1136 */           if (Tiles.isOreCave(Tiles.decodeType(tile)))
/*      */           {
/* 1138 */             if (source.getAuxData() > 0) {
/*      */               
/* 1140 */               int digTilex = (int)performer.getStatus().getPositionX() + 2 >> 2;
/* 1141 */               int digTiley = (int)performer.getStatus().getPositionY() + 2 >> 2;
/* 1142 */               if (digTilex > tilex + 1 || digTilex < tilex || digTiley > tiley + 1 || digTiley < tiley) {
/*      */                 
/* 1144 */                 performer.getCommunicator().sendNormalServerMessage("You are too far away to use the wand.");
/* 1145 */                 return true;
/*      */               } 
/* 1147 */               int itemTemplateCreated = TileRockBehaviour.getItemTemplateForTile(Tiles.decodeType(tile));
/* 1148 */               r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/* 1149 */               int m = TileRockBehaviour.MAX_QL;
/* 1150 */               if (itemTemplateCreated == 146 || itemTemplateCreated == 38)
/* 1151 */                 m = 100; 
/* 1152 */               int max = Math.min(m, 20 + r.nextInt(80));
/* 1153 */               byte state = Zones.getMiningState(tilex, tiley);
/*      */               
/* 1155 */               if (state == -1) {
/* 1156 */                 max = 99;
/*      */               }
/* 1158 */               float power = max;
/* 1159 */               if (source.isCrude())
/* 1160 */                 power = 1.0F; 
/* 1161 */               int resource = Server.getCaveResource(tilex, tiley);
/* 1162 */               if (resource == 65535)
/*      */               {
/* 1164 */                 resource = Server.rand.nextInt(10000);
/*      */               }
/* 1166 */               if (resource > 10) {
/*      */                 
/* 1168 */                 Server.setCaveResource(tilex, tiley, Math.max(1, resource - 10));
/*      */                 
/* 1170 */                 for (int x = 0; x < 10; x++) {
/*      */ 
/*      */                   
/*      */                   try {
/* 1174 */                     Item newItem = ItemFactory.createItem(itemTemplateCreated, power, performer
/* 1175 */                         .getStatus().getPositionX(), performer.getStatus().getPositionY(), Server.rand
/* 1176 */                         .nextFloat() * 360.0F, false, act.getRarity(), -10L, null);
/* 1177 */                     newItem.setLastOwnerId(performer.getWurmId());
/* 1178 */                     newItem.setDataXY(tilex, tiley);
/*      */                   }
/* 1180 */                   catch (Exception ex) {
/*      */                     
/* 1182 */                     logger.log(Level.WARNING, "Factory failed to produce item", ex);
/*      */                   } 
/*      */                 } 
/* 1185 */                 TileRockBehaviour.createGem(digTilex, digTiley, performer, power, false, act);
/* 1186 */                 source.setAuxData((byte)(source.getAuxData() - 1));
/*      */               } else {
/*      */                 
/* 1189 */                 performer.getCommunicator().sendNormalServerMessage("The " + source.getName() + " fails you.");
/*      */               } 
/*      */             } else {
/* 1192 */               performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*      */             }
/*      */           
/*      */           }
/*      */         }
/* 1197 */         else if (Tiles.isOreCave(Tiles.decodeType(tile))) {
/*      */           
/* 1199 */           int resource = Server.getCaveResource(tilex, tiley);
/* 1200 */           if (resource == 65535)
/*      */           {
/* 1202 */             resource = Server.rand.nextInt(10000);
/*      */           }
/* 1204 */           if (resource > 10)
/*      */           {
/* 1206 */             Server.setCaveResource(tilex, tiley, Math.max(1, resource - 10));
/*      */           }
/* 1208 */           byte state = Zones.getMiningState(tilex, tiley);
/* 1209 */           int itemTemplateCreated = TileRockBehaviour.getItemTemplateForTile(Tiles.decodeType(tile));
/* 1210 */           r.setSeed((tilex + tiley * Zones.worldTileSizeY) * 789221L);
/* 1211 */           int m = TileRockBehaviour.MAX_QL;
/* 1212 */           if (itemTemplateCreated == 146 || itemTemplateCreated == 38)
/* 1213 */             m = 100; 
/* 1214 */           int max = Math.min(m, 20 + r.nextInt(80));
/*      */           
/* 1216 */           if (state == -1)
/* 1217 */             max = 99; 
/* 1218 */           performer.getCommunicator().sendNormalServerMessage("You ding the " + source
/* 1219 */               .getName() + " on the wall. From the sounds you realize that the ore here has a max quality of " + max + ". You will be able to mine here " + 
/*      */               
/* 1221 */               Server.getCaveResource(tilex, tiley) + " more times.");
/*      */           
/* 1223 */           Items.destroyItem(source.getWurmId());
/*      */         } else {
/*      */           
/* 1226 */           performer.getCommunicator().sendNormalServerMessage("Nothing happens as you ding the " + source
/* 1227 */               .getName() + " on the wall.");
/*      */         }
/*      */          }
/* 1230 */       else if (action == 856)
/*      */       
/* 1232 */       { if ((type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id) && (source
/* 1233 */           .getTemplateId() == 493 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1235 */           done = clad(act, performer, source, tilex, tiley, tile, action, counter, 132, Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id);
/*      */         
/*      */         } }
/*      */       
/* 1239 */       else if (action == 857)
/*      */       
/* 1241 */       { if ((type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id) && (source
/* 1242 */           .getTemplateId() == 493 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1244 */           done = clad(act, performer, source, tilex, tiley, tile, action, counter, 1123, Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_SLATE_REINFORCED.id);
/*      */         
/*      */         } }
/*      */       
/* 1248 */       else if (action == 858)
/*      */       
/* 1250 */       { if ((type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id) && (source
/* 1251 */           .getTemplateId() == 493 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1253 */           done = clad(act, performer, source, tilex, tiley, tile, action, counter, 776, Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_POTTERY_REINFORCED.id);
/*      */         
/*      */         } }
/*      */       
/* 1257 */       else if (action == 859)
/*      */       
/* 1259 */       { if ((type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id) && (source
/* 1260 */           .getTemplateId() == 493 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1262 */           done = clad(act, performer, source, tilex, tiley, tile, action, counter, 1122, Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_ROUNDED_STONE_REINFORCED.id);
/*      */         
/*      */         } }
/*      */       
/* 1266 */       else if (action == 860)
/*      */       
/* 1268 */       { if ((type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id) && (source
/* 1269 */           .getTemplateId() == 493 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1271 */           done = clad(act, performer, source, tilex, tiley, tile, action, counter, 1121, Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_SANDSTONE_REINFORCED.id);
/*      */         
/*      */         } }
/*      */       
/* 1275 */       else if (action == 861)
/*      */       
/* 1277 */       { if ((type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id) && (source
/* 1278 */           .getTemplateId() == 493 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1280 */           done = clad(act, performer, source, tilex, tiley, tile, action, counter, 786, Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_MARBLE_REINFORCED.id);
/*      */         
/*      */         } }
/*      */       
/* 1284 */       else if (action == 862)
/*      */       
/* 1286 */       { if ((type == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id || type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id) && (source
/* 1287 */           .getTemplateId() == 63 || source.getTemplateId() == 62 || (source
/* 1288 */           .isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1290 */           done = clad(act, performer, source, tilex, tiley, tile, action, counter, 22, Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_WOOD_REINFORCED.id);
/*      */         
/*      */         } }
/*      */       
/* 1294 */       else if (action == 78 && isClad(type) && (source
/* 1295 */         .getTemplateId() == 1115 || (source.isWand() && performer.getPower() >= 4)))
/*      */       
/* 1297 */       { done = removeCladding(act, performer, source, tilex, tiley, tile, action, counter); }
/*      */       
/* 1299 */       else if (action == 78 && type == Tiles.Tile.TILE_CAVE_WALL_WOOD_REINFORCED.id && (source
/* 1300 */         .getTemplateId() == 219 || (source.isWand() && performer.getPower() >= 4)))
/*      */       
/* 1302 */       { done = removeCladding(act, performer, source, tilex, tiley, tile, action, counter); }
/*      */       
/* 1304 */       else if (action == 847)
/*      */       
/* 1306 */       { if (type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id && (source
/* 1307 */           .getTemplateId() == 130 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1309 */           done = toggleRenderWall(act, performer, source, tilex, tiley, tile, action, counter);
/*      */         }
/* 1311 */         else if (type == Tiles.Tile.TILE_CAVE_WALL_RENDERED_REINFORCED.id && (source
/* 1312 */           .getTemplateId() == 1115 || (source.isWand() && performer.getPower() >= 4)))
/*      */         {
/* 1314 */           done = toggleRenderWall(act, performer, source, tilex, tiley, tile, action, counter);
/*      */         }
/*      */          }
/* 1317 */       else if (action == 141 && performer.getDeity() != null && performer.getDeity().isMountainGod())
/*      */       
/* 1319 */       { done = MethodsReligion.pray(act, performer, counter); }
/*      */       
/* 1321 */       else if (action == 109)
/*      */       
/* 1323 */       { performer.getCommunicator().sendNormalServerMessage("You cannot track there."); }
/*      */       
/* 1325 */       else if (action == 607)
/*      */       
/*      */       { 
/* 1328 */         long target = Tiles.getTileId(tilex, tiley, 0, false);
/* 1329 */         performer.getCommunicator().sendAddCaveWallToCreationWindow(target, type, -10L); }
/*      */       else
/*      */       
/* 1332 */       { done = super.action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter); } 
/* 1333 */     }  return done;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean toggleRenderWall(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter) {
/*      */     String render, renders, walltype;
/* 1339 */     boolean insta = (source.isWand() && performer.getPower() >= 4);
/* 1340 */     byte type = Tiles.decodeType(tile);
/* 1341 */     if (!Methods.isActionAllowed(performer, (short)229, tilex, tiley) || 
/* 1342 */       !Methods.isActionAllowed(performer, (short)116, tilex, tiley))
/*      */     {
/* 1344 */       return true;
/*      */     }
/* 1346 */     if (type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id && !insta)
/*      */     {
/*      */       
/* 1349 */       if (source.getWeightGrams() < 10000) {
/*      */         
/* 1351 */         performer.getCommunicator().sendNormalServerMessage("It takes 10kg of " + source
/* 1352 */             .getName() + " to render the wall.");
/* 1353 */         return true;
/*      */       } 
/*      */     }
/* 1356 */     int time = 40;
/*      */ 
/*      */ 
/*      */     
/* 1360 */     if (type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id) {
/*      */       
/* 1362 */       render = "render";
/* 1363 */       renders = "renders";
/* 1364 */       walltype = "stone reinforced wall";
/*      */     }
/*      */     else {
/*      */       
/* 1368 */       render = "remove the render from";
/* 1369 */       renders = "removes the render from";
/* 1370 */       walltype = "rendered reinforced wall";
/*      */     } 
/* 1372 */     if (counter == 1.0F) {
/*      */       
/* 1374 */       act.setTimeLeft(time);
/* 1375 */       performer.sendActionControl("Rendering wall", true, time);
/* 1376 */       performer.getCommunicator().sendNormalServerMessage("You start to " + render + " the " + walltype + ".");
/* 1377 */       Server.getInstance().broadCastAction(
/* 1378 */           StringUtil.format("%s starts to " + render + " the " + walltype + ".", new Object[] { performer.getName() }), performer, 5);
/* 1379 */       return false;
/*      */     } 
/* 1381 */     time = act.getTimeLeft();
/* 1382 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 1384 */       performer.getCommunicator().sendNormalServerMessage("You " + render + " the " + walltype + ".");
/* 1385 */       Server.getInstance().broadCastAction(
/* 1386 */           StringUtil.format("%s " + renders + " the %s.", new Object[] { performer.getName(), walltype }), performer, 5);
/*      */       
/* 1388 */       if (type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id && !insta)
/*      */       {
/*      */         
/* 1391 */         source.setWeight(source.getWeightGrams() - 10000, true);
/*      */       }
/*      */       
/* 1394 */       byte newWallType = (type == Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id) ? Tiles.Tile.TILE_CAVE_WALL_RENDERED_REINFORCED.id : Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id;
/*      */ 
/*      */ 
/*      */       
/* 1398 */       Server.caveMesh.setTile(tilex, tiley, 
/* 1399 */           Tiles.encode(Tiles.decodeHeight(tile), newWallType, Tiles.decodeData(tile)));
/* 1400 */       TileRockBehaviour.sendCaveTile(tilex, tiley, 0, 0);
/*      */       
/* 1402 */       return true;
/*      */     } 
/* 1404 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeCladding(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter) {
/* 1411 */     boolean insta = (source.isWand() && performer.getPower() >= 4);
/* 1412 */     byte type = Tiles.decodeType(tile);
/* 1413 */     if (!Methods.isActionAllowed(performer, (short)229, tilex, tiley) || 
/* 1414 */       !Methods.isActionAllowed(performer, (short)145, tilex, tiley))
/*      */     {
/* 1416 */       return true;
/*      */     }
/*      */     
/* 1419 */     int time = 400;
/* 1420 */     if (counter == 1.0F) {
/*      */       
/* 1422 */       act.setTimeLeft(time);
/* 1423 */       performer.sendActionControl("Removing cladding", true, time);
/* 1424 */       performer.getCommunicator().sendNormalServerMessage("You start to remove the cladding from the " + Tiles.getTile(type).getName() + ".");
/* 1425 */       Server.getInstance().broadCastAction(performer.getName() + " starts to remove the cladding from the " + Tiles.getTile(type).getName() + ".", performer, 5);
/* 1426 */       return false;
/*      */     } 
/* 1428 */     time = act.getTimeLeft();
/* 1429 */     if (!insta && act.currentSecond() % 5 == 0) {
/*      */       
/* 1431 */       SoundPlayer.playSound((Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2", tilex, tiley, performer.isOnSurface(), 1.6F);
/* 1432 */       performer.getStatus().modifyStamina(-10000.0F);
/* 1433 */       source.setDamage(source.getDamage() + 0.001F * source.getDamageModifier());
/*      */     } 
/* 1435 */     if (counter * 10.0F > time || insta) {
/*      */       
/* 1437 */       performer.getCommunicator().sendNormalServerMessage("You remove the cladding from the " + Tiles.getTile(type).getName() + ".");
/* 1438 */       Server.getInstance().broadCastAction(performer.getName() + " removes the chadding from the " + Tiles.getTile(type).getName() + ".", performer, 5);
/*      */ 
/*      */       
/* 1441 */       byte newWallType = Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id;
/*      */       
/* 1443 */       Server.caveMesh.setTile(tilex, tiley, 
/* 1444 */           Tiles.encode(Tiles.decodeHeight(tile), newWallType, Tiles.decodeData(tile)));
/* 1445 */       TileRockBehaviour.sendCaveTile(tilex, tiley, 0, 0);
/*      */       
/* 1447 */       return true;
/*      */     } 
/* 1449 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean clad(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter, int brick, byte partwalltype, byte finishedWallType) {
/* 1455 */     boolean insta = (source.isWand() && performer.getPower() >= 4);
/* 1456 */     byte tiletype = Tiles.decodeType(tile);
/* 1457 */     if (!Methods.isActionAllowed(performer, (short)229, tilex, tiley) || 
/* 1458 */       !Methods.isActionAllowed(performer, (short)116, tilex, tiley))
/*      */     {
/* 1460 */       return true;
/*      */     }
/* 1462 */     int skillTemplate = (brick == 22) ? 1005 : 1013;
/* 1463 */     Skill skill = performer.getSkills().getSkillOrLearn(skillTemplate);
/* 1464 */     Skill toolskill = null;
/*      */     
/*      */     try {
/* 1467 */       toolskill = performer.getSkills().getSkillOrLearn(source.getPrimarySkill());
/*      */     }
/* 1469 */     catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */     
/* 1473 */     if (skillTemplate == 1013)
/*      */     {
/* 1475 */       if (skill.getKnowledge(0.0D) < 30.0D) {
/*      */         
/* 1477 */         performer.getCommunicator().sendNormalServerMessage("You need at least 30 masonry to clad reinforced walls with stone.");
/*      */         
/* 1479 */         performer.getCommunicator().sendActionResult(false);
/* 1480 */         return true;
/*      */       } 
/*      */     }
/*      */     
/* 1484 */     int resource = Server.getCaveResource(tilex, tiley);
/* 1485 */     int needs = resource >>> 8;
/*      */     
/* 1487 */     if (tiletype == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) {
/*      */       
/* 1489 */       needs = 10;
/* 1490 */       Server.setCaveResource(tilex, tiley, (needs << 8) + (resource & 0xF));
/* 1491 */       Server.caveMesh.setTile(tilex, tiley, 
/* 1492 */           Tiles.encode(Tiles.decodeHeight(tile), partwalltype, Tiles.decodeData(tile)));
/* 1493 */       TileRockBehaviour.sendCaveTile(tilex, tiley, 0, 0);
/* 1494 */       long target = Tiles.getTileId(tilex, tiley, 0, false);
/* 1495 */       performer.getCommunicator().sendAddCaveWallToCreationWindow(target, partwalltype, target);
/*      */     } 
/*      */     
/* 1498 */     int[] templatesNeeded = getTemplatesNeeded(tilex, tiley, partwalltype);
/*      */     
/* 1500 */     if (!insta && !hasTemplateItems(performer, templatesNeeded))
/*      */     {
/*      */       
/* 1503 */       return true;
/*      */     }
/* 1505 */     int time = 40;
/* 1506 */     if (counter == 1.0F) {
/*      */       
/* 1508 */       act.setTimeLeft(time);
/* 1509 */       performer.sendActionControl("Cladding reinforced wall", true, time);
/* 1510 */       performer.getCommunicator().sendNormalServerMessage("You start to clad the reinforced wall.");
/* 1511 */       Server.getInstance().broadCastAction(performer.getName() + " starts to clad the reinforced wall.", performer, 5);
/*      */     } else {
/*      */       
/* 1514 */       time = act.getTimeLeft();
/* 1515 */     }  if (act.currentSecond() % 5 == 0) {
/*      */       
/* 1517 */       if (source.getTemplateId() == 493) {
/* 1518 */         SoundPlayer.playSound("sound.work.masonry", tilex, tiley, performer.isOnSurface(), 1.6F);
/*      */       } else {
/* 1520 */         SoundPlayer.playSound((Server.rand.nextInt(2) == 0) ? "sound.work.carpentry.mallet1" : "sound.work.carpentry.mallet2", tilex, tiley, performer
/* 1521 */             .isOnSurface(), 1.6F);
/* 1522 */       }  performer.getStatus().modifyStamina(-10000.0F);
/* 1523 */       if (source.getTemplateId() == 63) {
/* 1524 */         source.setDamage(source.getDamage() + 0.0015F * source.getDamageModifier());
/* 1525 */       } else if (source.getTemplateId() == 62) {
/* 1526 */         source.setDamage(source.getDamage() + 3.0E-4F * source.getDamageModifier());
/* 1527 */       } else if (source.getTemplateId() == 493) {
/* 1528 */         source.setDamage(source.getDamage() + 5.0E-4F * source.getDamageModifier());
/*      */       } 
/* 1530 */     }  if (counter * 10.0F > time || insta) {
/*      */ 
/*      */       
/* 1533 */       if (!insta && !depleteTemplateItems(performer, templatesNeeded, act))
/* 1534 */         return true; 
/* 1535 */       if (insta) {
/*      */         
/* 1537 */         performer.getCommunicator().sendNormalServerMessage("You use the wand and summon the required materials and add those to the wall.");
/* 1538 */         act.setPower(50.0F);
/* 1539 */         if (needs > 1) {
/* 1540 */           needs = 1;
/*      */         } else {
/* 1542 */           needs = 0;
/*      */         } 
/*      */       } else {
/* 1545 */         needs--;
/* 1546 */       }  Server.setCaveResource(tilex, tiley, (needs << 8) + (resource & 0xF));
/* 1547 */       if (!insta) {
/*      */ 
/*      */         
/* 1550 */         double bonus = 0.0D;
/* 1551 */         if (toolskill != null) {
/*      */           
/* 1553 */           toolskill.skillCheck(10.0D, source, 0.0D, false, counter);
/* 1554 */           bonus = toolskill.getKnowledge(source, 0.0D) / 10.0D;
/*      */         } 
/* 1556 */         skill.skillCheck(10.0D, source, bonus, false, counter);
/*      */       } 
/*      */       
/* 1559 */       long target = Tiles.getTileId(tilex, tiley, 0, false);
/* 1560 */       if (needs <= 0) {
/*      */         
/* 1562 */         Server.caveMesh.setTile(tilex, tiley, 
/* 1563 */             Tiles.encode(Tiles.decodeHeight(tile), finishedWallType, Tiles.decodeData(tile)));
/* 1564 */         TileRockBehaviour.sendCaveTile(tilex, tiley, 0, 0);
/* 1565 */         performer.getCommunicator().sendRemoveFromCreationWindow(target);
/*      */       } else {
/*      */         
/* 1568 */         performer.getCommunicator().sendAddCaveWallToCreationWindow(target, partwalltype, target);
/* 1569 */       }  return true;
/*      */     } 
/* 1571 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean hasTemplateItems(Creature performer, int[] neededTemplates) {
/* 1576 */     boolean found = true;
/* 1577 */     Item[] inventoryItems = performer.getInventory().getAllItems(false);
/* 1578 */     Item[] bodyItems = performer.getBody().getAllItems();
/* 1579 */     int[] foundTemplates = getNotInitializedIntArray(neededTemplates.length);
/*      */     
/* 1581 */     for (int x = 0; x < inventoryItems.length; x++) {
/*      */       
/* 1583 */       for (int n = 0; n < neededTemplates.length; n++) {
/*      */         
/* 1585 */         if (inventoryItems[x].getTemplateId() == neededTemplates[n]) {
/*      */           
/* 1587 */           int neededTemplateWeightGrams = getItemTemplateWeightInGrams(inventoryItems[x].getTemplateId());
/* 1588 */           if (neededTemplateWeightGrams <= inventoryItems[x].getWeightGrams())
/*      */           {
/* 1590 */             foundTemplates[n] = neededTemplates[n];
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1595 */     for (int f = 0; f < foundTemplates.length; f++) {
/*      */       
/* 1597 */       if (foundTemplates[f] == -1)
/* 1598 */         found = false; 
/*      */     } 
/* 1600 */     if (!found) {
/*      */       
/* 1602 */       for (int j = 0; j < bodyItems.length; j++) {
/*      */         
/* 1604 */         for (int n = 0; n < neededTemplates.length; n++) {
/*      */           
/* 1606 */           if (bodyItems[j].getTemplateId() == neededTemplates[n]) {
/*      */             
/* 1608 */             int neededTemplateWeightGrams = getItemTemplateWeightInGrams(bodyItems[j].getTemplateId());
/* 1609 */             if (neededTemplateWeightGrams <= bodyItems[j].getWeightGrams())
/*      */             {
/* 1611 */               foundTemplates[n] = neededTemplates[n];
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 1616 */       found = true;
/* 1617 */       for (int i = 0; i < foundTemplates.length; i++) {
/*      */         
/* 1619 */         if (foundTemplates[i] == -1)
/* 1620 */           found = false; 
/*      */       } 
/*      */     } 
/* 1623 */     if (!found) {
/*      */ 
/*      */       
/* 1626 */       for (int n = 0; n < foundTemplates.length; n++) {
/*      */         
/* 1628 */         if (foundTemplates[n] == -1) {
/*      */           
/*      */           try {
/*      */             
/* 1632 */             if (neededTemplates[n] == 217) {
/* 1633 */               performer.getCommunicator().sendNormalServerMessage("You need large iron nails.");
/*      */             } else {
/*      */               
/* 1636 */               ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(neededTemplates[n]);
/* 1637 */               if (template.isCombine()) {
/* 1638 */                 performer.getCommunicator().sendNormalServerMessage("You need " + template.getName() + ".");
/*      */               } else {
/* 1640 */                 performer.getCommunicator()
/* 1641 */                   .sendNormalServerMessage("You need " + template.getNameWithGenus() + ".");
/*      */               } 
/*      */             } 
/* 1644 */           } catch (NoSuchTemplateException nst) {
/*      */             
/* 1646 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         }
/*      */       } 
/* 1650 */       return false;
/*      */     } 
/* 1652 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   static final boolean depleteTemplateItems(Creature performer, int[] neededTemplates, Action act) {
/* 1657 */     Item[] inventoryItems = performer.getInventory().getAllItems(false);
/* 1658 */     Item[] bodyItems = performer.getBody().getAllItems();
/* 1659 */     float qlevel = 0.0F;
/*      */     
/* 1661 */     int[] foundTemplates = getNotInitializedIntArray(neededTemplates.length);
/* 1662 */     Item[] depleteItems = new Item[neededTemplates.length];
/* 1663 */     for (int i = 0; i < neededTemplates.length; i++) {
/*      */       
/* 1665 */       if (foundTemplates[i] == -1)
/*      */       {
/* 1667 */         for (int m = 0; m < inventoryItems.length; m++) {
/*      */           
/* 1669 */           if (inventoryItems[m].getTemplateId() == neededTemplates[i]) {
/*      */             
/* 1671 */             int neededTemplateWeightGrams = getItemTemplateWeightInGrams(inventoryItems[m].getTemplateId());
/* 1672 */             if (neededTemplateWeightGrams <= inventoryItems[m].getWeightGrams()) {
/*      */               
/* 1674 */               depleteItems[i] = inventoryItems[m];
/* 1675 */               foundTemplates[i] = neededTemplates[i];
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 1682 */     boolean allInitialized = true; int j;
/* 1683 */     for (j = 0; j < foundTemplates.length; j++) {
/*      */       
/* 1685 */       if (foundTemplates[j] == -1) {
/*      */         
/* 1687 */         allInitialized = false;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1692 */     if (!allInitialized) {
/*      */       
/* 1694 */       for (j = 0; j < neededTemplates.length; j++) {
/*      */         
/* 1696 */         if (foundTemplates[j] == -1)
/*      */         {
/* 1698 */           for (int m = 0; m < bodyItems.length; m++) {
/*      */             
/* 1700 */             if (bodyItems[m].getTemplateId() == neededTemplates[j]) {
/*      */               
/* 1702 */               int neededTemplateWeightGrams = getItemTemplateWeightInGrams(bodyItems[m].getTemplateId());
/* 1703 */               if (neededTemplateWeightGrams <= bodyItems[m].getWeightGrams()) {
/*      */                 
/* 1705 */                 depleteItems[j] = bodyItems[m];
/* 1706 */                 foundTemplates[j] = neededTemplates[j];
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           }  } 
/*      */       } 
/* 1712 */       allInitialized = true;
/* 1713 */       for (j = 0; j < foundTemplates.length; j++) {
/*      */         
/* 1715 */         if (foundTemplates[j] == -1) {
/*      */           
/* 1717 */           allInitialized = false;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1723 */     if (!allInitialized) {
/*      */       
/* 1725 */       for (j = 0; j < foundTemplates.length; j++) {
/*      */         
/* 1727 */         if (foundTemplates[j] == -1) {
/*      */           
/*      */           try {
/*      */             
/* 1731 */             ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(neededTemplates[j]);
/* 1732 */             performer.getCommunicator().sendNormalServerMessage("You did not have enough " + template
/* 1733 */                 .getPlural() + ".");
/*      */           }
/* 1735 */           catch (NoSuchTemplateException nst) {
/*      */             
/* 1737 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */           } 
/*      */         }
/*      */       } 
/* 1741 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1745 */     StringBuilder buf = new StringBuilder();
/* 1746 */     for (int k = 0; k < depleteItems.length; k++) {
/*      */ 
/*      */       
/*      */       try {
/* 1750 */         ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(neededTemplates[k]);
/* 1751 */         if (k > 0)
/* 1752 */           buf.append(" and "); 
/* 1753 */         buf.append(template.getNameWithGenus());
/*      */       }
/* 1755 */       catch (NoSuchTemplateException nst) {
/*      */         
/* 1757 */         logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */       } 
/*      */       
/* 1760 */       if (depleteItems[k].isCombine()) {
/* 1761 */         depleteItems[k].setWeight(depleteItems[k]
/* 1762 */             .getWeightGrams() - depleteItems[k].getTemplate().getWeightGrams(), true);
/*      */       } else {
/* 1764 */         Items.destroyItem(depleteItems[k].getWurmId());
/* 1765 */       }  qlevel += depleteItems[k].getCurrentQualityLevel() / 21.0F;
/*      */     } 
/* 1767 */     performer.getCommunicator().sendNormalServerMessage("You use " + buf.toString() + ".");
/* 1768 */     act.setPower(qlevel);
/* 1769 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final int[] getNotInitializedIntArray(int len) {
/* 1775 */     int[] notInitializedArray = new int[len];
/* 1776 */     for (int x = 0; x < notInitializedArray.length; x++)
/* 1777 */       notInitializedArray[x] = -1; 
/* 1778 */     return notInitializedArray;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final int getItemTemplateWeightInGrams(int itemTemplateId) {
/*      */     int neededTemplateWeightGrams;
/*      */     
/* 1786 */     try { neededTemplateWeightGrams = ItemTemplateFactory.getInstance().getTemplate(itemTemplateId).getWeightGrams(); }
/*      */     
/* 1788 */     catch (NoSuchTemplateException nst)
/*      */     
/*      */     { 
/* 1791 */       switch (itemTemplateId)
/*      */       
/*      */       { case 22:
/* 1794 */           neededTemplateWeightGrams = 2000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1811 */           return neededTemplateWeightGrams;case 217: neededTemplateWeightGrams = 300; return neededTemplateWeightGrams;case 492: neededTemplateWeightGrams = 2000; return neededTemplateWeightGrams;case 132: neededTemplateWeightGrams = 150000; return neededTemplateWeightGrams; }  neededTemplateWeightGrams = 150000; }  return neededTemplateWeightGrams;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCorrectTool(byte type, Creature performer, @Nullable Item tool) {
/* 1816 */     if (tool == null)
/* 1817 */       return false; 
/* 1818 */     List<Integer> tools = getToolsForType(type, performer);
/* 1819 */     for (Integer t : tools) {
/*      */       
/* 1821 */       if (t.intValue() == tool.getTemplateId())
/* 1822 */         return true; 
/*      */     } 
/* 1824 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<Integer> getToolsForType(byte type, @Nullable Creature performer) {
/* 1829 */     List<Integer> list = new ArrayList<>();
/* 1830 */     if (type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id) {
/*      */       
/* 1832 */       list.add(Integer.valueOf(62));
/* 1833 */       list.add(Integer.valueOf(63));
/*      */       
/* 1835 */       if (performer != null)
/*      */       {
/* 1837 */         if (performer.getPower() >= 2 && Servers.isThisATestServer())
/*      */         {
/* 1839 */           list.add(Integer.valueOf(315));
/*      */         }
/*      */         
/* 1842 */         if (WurmPermissions.mayUseGMWand(performer))
/*      */         {
/* 1844 */           list.add(Integer.valueOf(176));
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1850 */       list.add(Integer.valueOf(493));
/* 1851 */       if (performer != null)
/*      */       {
/* 1853 */         if (WurmPermissions.mayUseGMWand(performer))
/*      */         {
/* 1855 */           list.add(Integer.valueOf(176));
/*      */         }
/*      */       }
/*      */     } 
/* 1859 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public static short actionFromWallType(byte type) {
/* 1864 */     if (type == Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id)
/* 1865 */       return 856; 
/* 1866 */     if (type == Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id)
/* 1867 */       return 857; 
/* 1868 */     if (type == Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id)
/* 1869 */       return 858; 
/* 1870 */     if (type == Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id)
/* 1871 */       return 859; 
/* 1872 */     if (type == Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id)
/* 1873 */       return 860; 
/* 1874 */     if (type == Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id) {
/* 1875 */       return 861;
/*      */     }
/* 1877 */     return 862;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] getTemplatesNeeded(int tilex, int tiley, byte type) {
/* 1882 */     int templatesNeeded[], resource = Server.getCaveResource(tilex, tiley);
/* 1883 */     int needs = resource >>> 8;
/* 1884 */     int brick = getBrickTypeNeeded(type);
/*      */ 
/*      */     
/* 1887 */     if (brick == 22) {
/*      */       
/* 1889 */       if (needs == 10) {
/* 1890 */         templatesNeeded = new int[] { 22, 217 };
/*      */       } else {
/* 1892 */         templatesNeeded = new int[] { 22 };
/*      */       } 
/*      */     } else {
/* 1895 */       templatesNeeded = new int[] { brick, 492 };
/* 1896 */     }  return templatesNeeded;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] getMaterialsNeeded(int tilex, int tiley, byte type) {
/* 1901 */     int materialsNeeded[], resource = Server.getCaveResource(tilex, tiley);
/* 1902 */     int needs = resource >>> 8;
/* 1903 */     int brick = getBrickTypeNeeded(type);
/*      */ 
/*      */     
/* 1906 */     if (brick == 22) {
/*      */       
/* 1908 */       if (needs == 10) {
/* 1909 */         materialsNeeded = new int[] { 22, 10, 217, 1 };
/*      */       } else {
/* 1911 */         materialsNeeded = new int[] { 22, needs };
/*      */       } 
/*      */     } else {
/* 1914 */       materialsNeeded = new int[] { brick, needs, 492, needs };
/* 1915 */     }  return materialsNeeded;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBrickTypeNeeded(byte type) {
/* 1920 */     switch (type & 0xFF) {
/*      */       
/*      */       case 239:
/* 1923 */         return 132;
/*      */       case 240:
/* 1925 */         return 1123;
/*      */       case 242:
/* 1927 */         return 1122;
/*      */       case 241:
/* 1929 */         return 776;
/*      */       case 243:
/* 1931 */         return 1121;
/*      */       case 244:
/* 1933 */         return 786;
/*      */       case 245:
/* 1935 */         return 22;
/*      */     } 
/* 1937 */     return 132;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] getMaterialsFromToolType(Creature performer, Item tool) {
/* 1942 */     switch (tool.getTemplateId()) {
/*      */       
/*      */       case 62:
/*      */       case 63:
/* 1946 */         return new byte[] { Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id };
/*      */       case 493:
/* 1948 */         return new byte[] { Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 176:
/* 1957 */         return new byte[] { Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 315:
/* 1968 */         if (performer.getPower() >= 2 && Servers.isThisATestServer())
/*      */         {
/* 1970 */           return new byte[] { Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id, Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id };
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1979 */         return new byte[0];
/*      */     } 
/*      */     
/* 1982 */     return new byte[0];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean canCladWall(byte type, Creature performer) {
/* 1988 */     int skillNumber = (type == Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id) ? 1005 : 1013;
/* 1989 */     Skill skill = performer.getSkills().getSkillOrLearn(skillNumber);
/* 1990 */     if (skill == null) {
/* 1991 */       return false;
/*      */     }
/* 1993 */     if (skillNumber == 1013)
/*      */     {
/* 1995 */       if (skill.getKnowledge(0.0D) < 30.0D)
/* 1996 */         return false; 
/*      */     }
/* 1998 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getSkillNumberNeededForCladding(short action) {
/* 2003 */     if (action == 862) {
/* 2004 */       return 1005;
/*      */     }
/* 2006 */     return 1013;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int[] getCorrectToolsForCladding(short action) {
/* 2011 */     if (action == 862)
/* 2012 */       return new int[] { 62, 63 }; 
/* 2013 */     return new int[] { 493 };
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte getPartReinforcedWallFromAction(short action) {
/* 2018 */     switch (action) {
/*      */       
/*      */       case 856:
/* 2021 */         return Tiles.Tile.TILE_CAVE_WALL_PART_STONE_REINFORCED.id;
/*      */       case 857:
/* 2023 */         return Tiles.Tile.TILE_CAVE_WALL_PART_SLATE_REINFORCED.id;
/*      */       case 858:
/* 2025 */         return Tiles.Tile.TILE_CAVE_WALL_PART_POTTERY_REINFORCED.id;
/*      */       case 859:
/* 2027 */         return Tiles.Tile.TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED.id;
/*      */       case 860:
/* 2029 */         return Tiles.Tile.TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED.id;
/*      */       case 861:
/* 2031 */         return Tiles.Tile.TILE_CAVE_WALL_PART_MARBLE_REINFORCED.id;
/*      */       case 862:
/* 2033 */         return Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id;
/*      */     } 
/* 2035 */     logger.log(Level.WARNING, "Part reinforced wall for action " + action + " is not found!", new Exception());
/* 2036 */     return Tiles.Tile.TILE_CAVE_WALL_PART_WOOD_REINFORCED.id;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte getReinforcedWallFromAction(short action) {
/* 2041 */     switch (action) {
/*      */       
/*      */       case 856:
/* 2044 */         return Tiles.Tile.TILE_CAVE_WALL_STONE_REINFORCED.id;
/*      */       case 857:
/* 2046 */         return Tiles.Tile.TILE_CAVE_WALL_SLATE_REINFORCED.id;
/*      */       case 858:
/* 2048 */         return Tiles.Tile.TILE_CAVE_WALL_POTTERY_REINFORCED.id;
/*      */       case 859:
/* 2050 */         return Tiles.Tile.TILE_CAVE_WALL_ROUNDED_STONE_REINFORCED.id;
/*      */       case 860:
/* 2052 */         return Tiles.Tile.TILE_CAVE_WALL_SANDSTONE_REINFORCED.id;
/*      */       case 861:
/* 2054 */         return Tiles.Tile.TILE_CAVE_WALL_MARBLE_REINFORCED.id;
/*      */       case 862:
/* 2056 */         return Tiles.Tile.TILE_CAVE_WALL_WOOD_REINFORCED.id;
/*      */     } 
/* 2058 */     logger.log(Level.WARNING, "Reinforced wall for action " + action + " is not found!", new Exception());
/* 2059 */     return Tiles.Tile.TILE_CAVE_WALL_WOOD_REINFORCED.id;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] getMaterialsNeededTotal(short action) {
/* 2065 */     switch (action) {
/*      */       
/*      */       case 856:
/* 2068 */         return new int[] { 132, 10, 492, 10 };
/*      */       case 857:
/* 2070 */         return new int[] { 1123, 10, 492, 10 };
/*      */       case 858:
/* 2072 */         return new int[] { 776, 10, 492, 10 };
/*      */       case 859:
/* 2074 */         return new int[] { 1122, 10, 492, 10 };
/*      */       case 860:
/* 2076 */         return new int[] { 1121, 10, 492, 10 };
/*      */       case 861:
/* 2078 */         return new int[] { 786, 10, 492, 10 };
/*      */       case 862:
/* 2080 */         return new int[] { 22, 10, 217, 1 };
/*      */     } 
/* 2082 */     logger.log(Level.WARNING, "Materials for reinforced wall for action " + action + " is not found!", new Exception());
/* 2083 */     return new int[0];
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\CaveWallBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
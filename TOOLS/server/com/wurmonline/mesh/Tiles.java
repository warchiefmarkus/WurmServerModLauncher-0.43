/*      */ package com.wurmonline.mesh;
/*      */ 
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import java.awt.Color;
/*      */ import java.util.HashSet;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Tiles
/*      */   implements CounterTypes
/*      */ {
/*      */   public static final int TILE_COUNT = 256;
/*   63 */   private static final Tile[] tiles = new Tile[256];
/*      */   
/*      */   public static final byte TILE_TYPE_NONE_ID = -1;
/*      */   
/*      */   public static final int TILE_TYPE_HOLE = 0;
/*      */   
/*      */   public static final int TILE_TYPE_SAND = 1;
/*      */   
/*      */   public static final int TILE_TYPE_GRASS = 2;
/*      */   
/*      */   public static final int TILE_TYPE_TREE = 3;
/*      */   
/*      */   public static final int TILE_TYPE_ROCK = 4;
/*      */   
/*      */   public static final int TILE_TYPE_DIRT = 5;
/*      */   
/*      */   public static final int TILE_TYPE_CLAY = 6;
/*      */   
/*      */   public static final int TILE_TYPE_FIELD = 7;
/*      */   
/*      */   public static final int TILE_TYPE_DIRT_PACKED = 8;
/*      */   
/*      */   public static final int TILE_TYPE_COBBLESTONE = 9;
/*      */   
/*      */   public static final int TILE_TYPE_MYCELIUM = 10;
/*      */   
/*      */   public static final int TILE_TYPE_MYCELIUM_TREE = 11;
/*      */   
/*      */   public static final int TILE_TYPE_LAVA = 12;
/*      */   
/*      */   public static final int TILE_TYPE_ENCHANTED_GRASS = 13;
/*      */   
/*      */   public static final int TILE_TYPE_ENCHANTED_TREE = 14;
/*      */   
/*      */   public static final int TILE_TYPE_PLANKS = 15;
/*      */   
/*      */   public static final int TILE_TYPE_STONE_SLABS = 16;
/*      */   
/*      */   public static final int TILE_TYPE_GRAVEL = 17;
/*      */   
/*      */   public static final int TILE_TYPE_PEAT = 18;
/*      */   
/*      */   public static final int TILE_TYPE_TUNDRA = 19;
/*      */   
/*      */   public static final int TILE_TYPE_MOSS = 20;
/*      */   
/*      */   public static final int TILE_TYPE_CLIFF = 21;
/*      */   
/*      */   public static final int TILE_TYPE_STEPPE = 22;
/*      */   
/*      */   public static final int TILE_TYPE_MARSH = 23;
/*      */   
/*      */   public static final int TILE_TYPE_TAR = 24;
/*      */   
/*      */   public static final int TILE_TYPE_MINE_DOOR_WOOD = 25;
/*      */   
/*      */   public static final int TILE_TYPE_MINE_DOOR_STONE = 26;
/*      */   
/*      */   public static final int TILE_TYPE_MINE_DOOR_GOLD = 27;
/*      */   
/*      */   public static final int TILE_TYPE_MINE_DOOR_SILVER = 28;
/*      */   
/*      */   public static final int TILE_TYPE_MINE_DOOR_STEEL = 29;
/*      */   
/*      */   public static final int TILE_TYPE_SNOW = 30;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH = 31;
/*      */   
/*      */   public static final int TILE_TYPE_KELP = 32;
/*      */   
/*      */   public static final int TILE_TYPE_REED = 33;
/*      */   
/*      */   public static final int TILE_TYPE_ENCHANTED_BUSH = 34;
/*      */   
/*      */   public static final int TILE_TYPE_MYCELIUM_BUSH = 35;
/*      */   
/*      */   public static final int TILE_TYPE_SLATE_BRICKS = 36;
/*      */   
/*      */   public static final int TILE_TYPE_MARBLE_SLABS = 37;
/*      */   
/*      */   public static final int TILE_TYPE_LAWN = 38;
/*      */   
/*      */   public static final int TILE_TYPE_PLANKS_TARRED = 39;
/*      */   
/*      */   public static final int TILE_TYPE_MYCELIUM_LAWN = 40;
/*      */   
/*      */   public static final int TILE_TYPE_COBBLESTONE_ROUGH = 41;
/*      */   
/*      */   public static final int TILE_TYPE_COBBLESTONE_ROUND = 42;
/*      */   
/*      */   public static final int TILE_TYPE_FIELD2 = 43;
/*      */   
/*      */   public static final int TILE_TYPE_SANDSTONE_BRICKS = 44;
/*      */   
/*      */   public static final int TILE_TYPE_SANDSTONE_SLABS = 45;
/*      */   
/*      */   public static final int TILE_TYPE_SLATE_SLABS = 46;
/*      */   
/*      */   public static final int TILE_TYPE_MARBLE_BRICKS = 47;
/*      */   
/*      */   public static final int TILE_TYPE_POTTERY_BRICKS = 48;
/*      */   
/*      */   public static final int TILE_TYPE_PREPARED_BRIDGE = 49;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_BIRCH = 100;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_PINE = 101;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_OAK = 102;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_CEDAR = 103;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_WILLOW = 104;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MAPLE = 105;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_APPLE = 106;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_LEMON = 107;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_OLIVE = 108;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_CHERRY = 109;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_CHESTNUT = 110;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_WALNUT = 111;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_FIR = 112;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_LINDEN = 113;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_BIRCH = 114;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_PINE = 115;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_OAK = 116;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_CEDAR = 117;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_WILLOW = 118;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_MAPLE = 119;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_APPLE = 120;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_LEMON = 121;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_OLIVE = 122;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_CHERRY = 123;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_CHESTNUT = 124;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_WALNUT = 125;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_FIR = 126;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_LINDEN = 127;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_BIRCH = 128;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_PINE = 129;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_OAK = 130;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_CEDAR = 131;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_WILLOW = 132;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_MAPLE = 133;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_APPLE = 134;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_LEMON = 135;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_OLIVE = 136;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_CHERRY = 137;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_CHESTNUT = 138;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_WALNUT = 139;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_FIR = 140;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_LINDEN = 141;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_LAVENDER = 142;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ROSE = 143;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_THORN = 144;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_GRAPE = 145;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_CAMELLIA = 146;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_OLEANDER = 147;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_LAVENDER = 148;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_ROSE = 149;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_THORN = 150;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_GRAPE = 151;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_CAMELLIA = 152;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_OLEANDER = 153;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_LAVENDER = 154;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_ROSE = 155;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_THORN = 156;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_GRAPE = 157;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_CAMELLIA = 158;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_OLEANDER = 159;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_HAZELNUT = 160;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_HAZELNUT = 161;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_HAZELNUT = 162;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ORANGE = 163;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_MYCELIUM_ORANGE = 164;
/*      */   
/*      */   public static final int TILE_TYPE_TREE_ENCHANTED_ORANGE = 165;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_RASPBERRY = 166;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_RASPBERRY = 167;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_RASPBERRY = 168;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_BLUEBERRY = 169;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_MYCELIUM_BLUEBERRY = 170;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_ENCHANTED_BLUEBERRY = 171;
/*      */   
/*      */   public static final int TILE_TYPE_BUSH_LINGONBERRY = 172;
/*      */   
/*      */   public static final int TILE_TYPE_CAVE = 200;
/*      */   public static final int TILE_TYPE_CAVE_EXIT = 201;
/*      */   public static final int TILE_TYPE_CAVE_WALL = 202;
/*      */   public static final int TILE_TYPE_CAVE_WALL_REINFORCED = 203;
/*      */   public static final int TILE_TYPE_CAVE_WALL_LAVA = 204;
/*      */   public static final int TILE_TYPE_CAVE_WALL_SLATE = 205;
/*      */   public static final int TILE_TYPE_CAVE_WALL_MARBLE = 206;
/*      */   public static final int TILE_TYPE_CAVE_FLOOR_REINFORCED = 207;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_GOLD = 220;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_SILVER = 221;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_IRON = 222;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_COPPER = 223;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_LEAD = 224;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_ZINC = 225;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_TIN = 226;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_ADAMANTINE = 227;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ORE_GLIMMERSTEEL = 228;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ROCKSALT = 229;
/*      */   public static final int TILE_TYPE_CAVE_WALL_SANDSTONE = 230;
/*      */   public static final int TILE_TYPE_CAVE_WALL_STONE_REINFORCED = 231;
/*      */   public static final int TILE_TYPE_CAVE_WALL_SLATE_REINFORCED = 232;
/*      */   public static final int TILE_TYPE_CAVE_WALL_POTTERY_REINFORCED = 233;
/*      */   public static final int TILE_TYPE_CAVE_WALL_ROUNDED_STONE_REINFORCED = 234;
/*      */   public static final int TILE_TYPE_CAVE_WALL_SANDSTONE_REINFORCED = 235;
/*      */   public static final int TILE_TYPE_CAVE_WALL_RENDERED_REINFORCED = 236;
/*      */   public static final int TILE_TYPE_CAVE_WALL_MARBLE_REINFORCED = 237;
/*      */   public static final int TILE_TYPE_CAVE_WALL_WOOD_REINFORCED = 238;
/*      */   public static final int TILE_TYPE_CAVE_WALL_PART_STONE_REINFORCED = 239;
/*      */   public static final int TILE_TYPE_CAVE_WALL_PART_SLATE_REINFORCED = 240;
/*      */   public static final int TILE_TYPE_CAVE_WALL_PART_POTTERY_REINFORCED = 241;
/*      */   public static final int TILE_TYPE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED = 242;
/*      */   public static final int TILE_TYPE_CAVE_WALL_PART_SANDSTONE_REINFORCED = 243;
/*      */   public static final int TILE_TYPE_CAVE_WALL_PART_MARBLE_REINFORCED = 244;
/*      */   public static final int TILE_TYPE_CAVE_WALL_PART_WOOD_REINFORCED = 245;
/*      */   public static final int TILE_TYPE_CAVE_PREPARED_FLOOR_REINFORCED = 246;
/*      */   public static final int CAVE_SIDE_FLOOR = 0;
/*      */   public static final int CAVE_SIDE_ROOF = 1;
/*      */   public static final int CAVE_SIDE_EAST = 2;
/*      */   public static final int CAVE_SIDE_SOUTH = 3;
/*      */   public static final int CAVE_SIDE_WEST = 4;
/*      */   public static final int CAVE_SIDE_NORTH = 5;
/*      */   public static final int CAVE_SIDE_MINE_DOOR = 6;
/*      */   public static final int CAVE_SIDE_CORNER = 7;
/*      */   public static final int CAVE_SIDE_BORDER_NORTH = 8;
/*      */   public static final int CAVE_SIDE_BORDER_WEST = 9;
/*      */   public static final int CAVE_SIDE_BORDER_SOUTH = 10;
/*      */   public static final int CAVE_SIDE_BORDER_EAST = 11;
/*      */   public static final int CAT_ALL = 0;
/*      */   public static final int CAT_BUSHES = 1;
/*      */   public static final int CAT_CAVE = 2;
/*      */   public static final int CAT_MINEDOORS = 3;
/*      */   public static final int CAT_NORMAL = 4;
/*      */   public static final int CAT_PAVING = 5;
/*      */   public static final int CAT_SURFACE = 6;
/*      */   public static final int CAT_TREES = 7;
/*      */   public static final int BAD_TILE = -1;
/*      */   public static final int BUFFER_SIDE_SIZE = 512;
/*      */   public static final int BUFFER_SIDE_MASK = 511;
/*      */   public static final int WORLD_SIDE_SIZE = 4096;
/*      */   public static final int TILE_WIDTH = 4;
/*      */   public static final float DIRT_FACTOR = 10.0F;
/*      */   public static final int FLOOR_FACTOR = 30;
/*      */   public static final int FLOOR_FACTOR_METERS = 3;
/*      */   public static final float FLOOR_THICKNESS = 0.25F;
/*      */   public static final byte INFILTRATION_NONE = 0;
/*      */   public static final byte INFILTRATION_SLOW = 1;
/*      */   public static final byte INFILTRATION_MODERATE = 2;
/*      */   public static final byte INFILTRATION_RAPID = 3;
/*      */   public static final byte RESERVOIR_SMALL = 0;
/*      */   public static final byte RESERVOIR_MEDIUM = 1;
/*      */   public static final byte RESERVOIR_LARGE = 2;
/*      */   public static final byte LEAKAGE_SLOW = 0;
/*      */   public static final byte LEAKAGE_MODERATE = 1;
/*      */   public static final byte LEAKAGE_RAPID = 2;
/*      */   
/*      */   private enum Flag
/*      */   {
/*  390 */     USESNEWDATA,
/*  391 */     ALIGNED, TREE, BUSH, NORMAL, MYCELIUM, ENCHANTED, GRASS, ROAD, FLATROAD, TUNDRA,
/*  392 */     CAVE, CAVEDOOR, VISIBLE_CAVEDOOR, SOLIDCAVE, REINFORCEDCAVE, ORECAVE, REINFORCEDFLOOR,
/*  393 */     BOTANIZE, FORAGE,
/*  394 */     BIRCH, PINE, OAK, CEDAR, WILLOW, MAPLE, APPLE, LEMON, OLIVE, CHERRY, CHESTNUT, WALNUT, FIR, LINDEN,
/*  395 */     LAVENDER, ROSE, THORN, GRAPE, CAMELLIA, OLEANDER, HAZELNUT, ORANGE, RASPBERRY, LINGONBERRY, BLUEBERRY;
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
/*      */   public enum Tile
/*      */   {
/*  415 */     TILE_HOLE(0, "Hole", "#000000", 1.0F, "img.texture.terrain.hole", new Tiles.Flag[] { Tiles.Flag.ALIGNED }, 60, (byte)3, (byte)2, (byte)2),
/*      */ 
/*      */     
/*  418 */     TILE_SAND(1, "Sand", "#A0936D", 0.8F, "img.texture.terrain.sand", new Tiles.Flag[0], 60, (byte)3, (byte)2, (byte)2),
/*      */ 
/*      */     
/*  421 */     TILE_GRASS(2, "Grass", "#366503", 0.75F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.GRASS, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  425 */     TILE_TREE(3, "TreePosition", "TreePosition (Superseded)", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.TREE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/*  429 */     TILE_ROCK(4, "Rock", "#726E6B", 1.0F, "img.texture.terrain.rock", new Tiles.Flag[0], 60, (byte)0, (byte)0, (byte)0),
/*      */ 
/*      */     
/*  432 */     TILE_DIRT(5, "Dirt", "#4B3F2F", 0.8F, "img.texture.terrain.dirt", new Tiles.Flag[0], 60, (byte)2, (byte)0, (byte)0),
/*      */ 
/*      */     
/*  435 */     TILE_CLAY(6, "Clay", "#717C76", 0.6F, "img.texture.terrain.clay", new Tiles.Flag[0], 60, (byte)0, (byte)0, (byte)0),
/*      */ 
/*      */     
/*  438 */     TILE_FIELD(7, "Field", "#473C2F", 0.8F, "img.texture.terrain.farm", new Tiles.Flag[] { Tiles.Flag.ALIGNED }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  441 */     TILE_DIRT_PACKED(8, "Packed dirt", "#4B3F2F", 0.9F, "img.texture.terrain.dirt.packed", new Tiles.Flag[0], 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  444 */     TILE_COBBLESTONE(9, "Cobblestone", "#5C5349", 1.0F, "img.texture.terrain.cobblestone", new Tiles.Flag[] { Tiles.Flag.ROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  448 */     TILE_MYCELIUM(10, "Mycelium", "#470233", 0.75F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.MYCELIUM, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  452 */     TILE_MYCELIUM_TREE(11, "Infected tree", "Infected tree (Superseded)", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.TREE, Tiles.Flag.MYCELIUM, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  457 */     TILE_LAVA(12, "Lava", "#d7331e", 0.5F, "img.texture.terrain.lava", new Tiles.Flag[0], 60, (byte)3, (byte)2, (byte)2),
/*      */ 
/*      */     
/*  460 */     TILE_ENCHANTED_GRASS(13, "Enchanted grass", "#2d5d2b", 0.8F, "img.texture.terrain.grass.enchanted", new Tiles.Flag[] { Tiles.Flag.ENCHANTED }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  465 */     TILE_ENCHANTED_TREE(14, "Enchanted tree", "Enchanted tree (Superseded)", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.TREE, Tiles.Flag.ENCHANTED }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  470 */     TILE_PLANKS(15, "Wooden planks", "#726650", 1.0F, "img.texture.terrain.planks", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)0, (byte)0),
/*      */ 
/*      */     
/*  473 */     TILE_STONE_SLABS(16, "Stone slabs", "#636363", 1.0F, "img.texture.terrain.stoneslabs", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)0, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  477 */     TILE_GRAVEL(17, "Gravel", "#4f4a40", 0.9F, "img.texture.terrain.gravel", new Tiles.Flag[] { Tiles.Flag.ROAD }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  480 */     TILE_PEAT(18, "Peat", "#362720", 0.7F, "img.texture.terrain.peat", new Tiles.Flag[] { Tiles.Flag.BOTANIZE }, 60, (byte)1, (byte)2, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  484 */     TILE_TUNDRA(19, "Tundra", "#76876d", 0.7F, "img.texture.terrain.tundra", new Tiles.Flag[] { Tiles.Flag.FORAGE, Tiles.Flag.TUNDRA }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  488 */     TILE_MOSS(20, "Moss", "#6a8e38", 0.7F, "img.texture.terrain.moss", new Tiles.Flag[] { Tiles.Flag.BOTANIZE }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  492 */     TILE_CLIFF(21, "Cliff", "#9b9794", 0.6F, "img.texture.terrain.rock.cliff", new Tiles.Flag[0], 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  495 */     TILE_STEPPE(22, "Steppe", "#727543", 0.8F, "img.texture.terrain.steppe", new Tiles.Flag[] { Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  499 */     TILE_MARSH(23, "Marsh", "#2b6548", 0.6F, "img.texture.terrain.marsh", new Tiles.Flag[] { Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE }, 60, (byte)3, (byte)2, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  503 */     TILE_TAR(24, "Tar", "#121528", 0.4F, "img.texture.terrain.tar", new Tiles.Flag[0], 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  506 */     TILE_MINE_DOOR_WOOD(25, "Mine door", "Wood mine door", "#293A02", 0.8F, "img.texture.terrain.minedoor.wood", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.VISIBLE_CAVEDOOR }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  511 */     TILE_MINE_DOOR_STONE(26, "Rock", "Stone mine door", "#726E6B", 1.0F, "img.texture.terrain.rock", new Tiles.Flag[] { Tiles.Flag.CAVEDOOR }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  515 */     TILE_MINE_DOOR_GOLD(27, "Mine door", "Gold mine door", "#1a3418", 0.8F, "img.texture.terrain.minedoor.gold", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.VISIBLE_CAVEDOOR }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  520 */     TILE_MINE_DOOR_SILVER(28, "Mine door", "Silver mine door", "#362720", 0.8F, "img.texture.terrain.minedoor.silver", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.VISIBLE_CAVEDOOR }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  525 */     TILE_MINE_DOOR_STEEL(29, "Mine door", "Steel mine door", "#2b6548", 0.8F, "img.texture.terrain.minedoor.steel", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.VISIBLE_CAVEDOOR }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  530 */     TILE_SNOW(30, "Snow", "Snow", "#FFFFFF", 0.5F, "img.texture.terrain.grass.winter", new Tiles.Flag[0], 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  533 */     TILE_BUSH(31, "BushPosition", "BushPosition (Superseded)", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.BUSH, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  537 */     TILE_KELP(32, "Kelp", "#366503", 0.75F, "img.texture.terrain.grass.kelp", new Tiles.Flag[] { Tiles.Flag.GRASS }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  541 */     TILE_REED(33, "Reed", "#366503", 0.75F, "img.texture.terrain.grass.reed", new Tiles.Flag[] { Tiles.Flag.GRASS }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  545 */     TILE_ENCHANTED_BUSH(34, "Enchanted bush", "Enchanted bush (Superseded)", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.BUSH, Tiles.Flag.ENCHANTED }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  550 */     TILE_MYCELIUM_BUSH(35, "Infected bush", "Infected bush (Superseded)", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.BUSH, Tiles.Flag.MYCELIUM, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  555 */     TILE_SLATE_BRICKS(36, "Slate bricks", "#5C5349", 1.0F, "img.texture.terrain.slatebricks", new Tiles.Flag[] { Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  560 */     TILE_MARBLE_SLABS(37, "Marble slabs", "#636363", 1.0F, "img.texture.terrain.marbleslabs", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  564 */     TILE_LAWN(38, "Lawn", "#366503", 0.8F, "img.texture.terrain.grass.lawn", new Tiles.Flag[] { Tiles.Flag.NORMAL }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  568 */     TILE_PLANKS_TARRED(39, "Wooden planks", "Tarred wooden planks", "#726650", 1.0F, "img.texture.terrain.planks.tarred", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  571 */     TILE_MYCELIUM_LAWN(40, "Mycelium Lawn", "#470233", 0.75F, "img.texture.terrain.mycelium.lawn", new Tiles.Flag[] { Tiles.Flag.MYCELIUM }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  575 */     TILE_COBBLESTONE_ROUGH(41, "Rough cobblestone", "#5C5349", 1.0F, "img.texture.terrain.cobble2", new Tiles.Flag[] { Tiles.Flag.ROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  580 */     TILE_COBBLESTONE_ROUND(42, "Round cobblestone", "#5C5349", 1.0F, "img.texture.terrain.cobble3", new Tiles.Flag[] { Tiles.Flag.ROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  585 */     TILE_FIELD2(43, "Field", "#473C2F", 0.8F, "img.texture.terrain.farm", new Tiles.Flag[] { Tiles.Flag.ALIGNED }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */     
/*  588 */     TILE_SANDSTONE_BRICKS(44, "Sandstone bricks", "#5C5349", 1.0F, "img.texture.terrain.sandstonebricks", new Tiles.Flag[] { Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  593 */     TILE_SANDSTONE_SLABS(45, "Sandstone slabs", "#5C5349", 1.0F, "img.texture.terrain.sandstoneslabs", new Tiles.Flag[] { Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  598 */     TILE_SLATE_SLABS(46, "Slate slabs", "#636363", 1.0F, "img.texture.terrain.slateslabs", new Tiles.Flag[] { Tiles.Flag.ALIGNED, Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */     
/*  602 */     TILE_MARBLE_BRICKS(47, "Marble bricks", "#5C5349", 1.0F, "img.texture.terrain.marblebricks", new Tiles.Flag[] { Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  607 */     TILE_POTTERY_BRICKS(48, "Pottery bricks", "#5C5349", 1.0F, "img.texture.terrain.potterybricks", new Tiles.Flag[] { Tiles.Flag.ROAD, Tiles.Flag.FLATROAD }, 60, (byte)0, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  613 */     TILE_PREPARED_BRIDGE(49, "Prepared for paving", "#636363", 1.0F, "img.texture.terrain.prepared", new Tiles.Flag[0], 60, (byte)0, (byte)0, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  619 */     TILE_TREE_BIRCH(100, "Birch tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.BIRCH, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  624 */     TILE_TREE_PINE(101, "Pine tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.PINE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  629 */     TILE_TREE_OAK(102, "Oak tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.OAK, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     TILE_TREE_CEDAR(103, "Cedar tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.CEDAR, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  639 */     TILE_TREE_WILLOW(104, "Willow tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.WILLOW, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  644 */     TILE_TREE_MAPLE(105, "Maple tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.MAPLE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  649 */     TILE_TREE_APPLE(106, "Apple tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.APPLE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  654 */     TILE_TREE_LEMON(107, "Lemon tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.LEMON, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  659 */     TILE_TREE_OLIVE(108, "Olive tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.OLIVE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  664 */     TILE_TREE_CHERRY(109, "Cherry tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.CHERRY, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  669 */     TILE_TREE_CHESTNUT(110, "Chestnut tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.CHESTNUT, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  674 */     TILE_TREE_WALNUT(111, "Walnut tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.WALNUT, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  679 */     TILE_TREE_FIR(112, "Fir tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.FIR, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  684 */     TILE_TREE_LINDEN(113, "Linden tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.LINDEN, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  690 */     TILE_MYCELIUM_TREE_BIRCH(114, "Infected birch tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.BIRCH, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  696 */     TILE_MYCELIUM_TREE_PINE(115, "Infected pine tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.PINE, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  702 */     TILE_MYCELIUM_TREE_OAK(116, "Infected oak tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.OAK, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  708 */     TILE_MYCELIUM_TREE_CEDAR(117, "Infected cedar tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.CEDAR, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  714 */     TILE_MYCELIUM_TREE_WILLOW(118, "Infected willow tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.WILLOW, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  720 */     TILE_MYCELIUM_TREE_MAPLE(119, "Infected maple tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.MAPLE, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  726 */     TILE_MYCELIUM_TREE_APPLE(120, "Infected apple tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.APPLE, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  732 */     TILE_MYCELIUM_TREE_LEMON(121, "Infected lemon tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.LEMON, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  738 */     TILE_MYCELIUM_TREE_OLIVE(122, "Infected olive tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.OLIVE, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  744 */     TILE_MYCELIUM_TREE_CHERRY(123, "Infected cherry tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.CHERRY, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  750 */     TILE_MYCELIUM_TREE_CHESTNUT(124, "Infected chestnut tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.CHESTNUT, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  756 */     TILE_MYCELIUM_TREE_WALNUT(125, "Infected walnut tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.WALNUT, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  762 */     TILE_MYCELIUM_TREE_FIR(126, "Infected fir tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.FIR, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  768 */     TILE_MYCELIUM_TREE_LINDEN(127, "Infected linden tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.LINDEN, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  775 */     TILE_ENCHANTED_TREE_BIRCH(128, "Enchanted birch tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.BIRCH, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  781 */     TILE_ENCHANTED_TREE_PINE(129, "Enchanted pine tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.PINE, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  787 */     TILE_ENCHANTED_TREE_OAK(130, "Enchanted oak tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.OAK, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  793 */     TILE_ENCHANTED_TREE_CEDAR(131, "Enchanted cedar tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.CEDAR, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  799 */     TILE_ENCHANTED_TREE_WILLOW(132, "Enchanted willow tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.WILLOW, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  805 */     TILE_ENCHANTED_TREE_MAPLE(133, "Enchanted maple tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.MAPLE, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  811 */     TILE_ENCHANTED_TREE_APPLE(134, "Enchanted apple tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.APPLE, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  817 */     TILE_ENCHANTED_TREE_LEMON(135, "Enchanted lemon tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.LEMON, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  823 */     TILE_ENCHANTED_TREE_OLIVE(136, "Enchanted olive tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.OLIVE, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  829 */     TILE_ENCHANTED_TREE_CHERRY(137, "Enchanted cherry tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.CHERRY, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  835 */     TILE_ENCHANTED_TREE_CHESTNUT(138, "Enchanted chestnut tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.CHESTNUT, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  841 */     TILE_ENCHANTED_TREE_WALNUT(139, "Enchanted walnut tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.WALNUT, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  847 */     TILE_ENCHANTED_TREE_FIR(140, "Enchanted fir tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.FIR, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  853 */     TILE_ENCHANTED_TREE_LINDEN(141, "Enchanted linden tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.LINDEN, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  860 */     TILE_BUSH_LAVENDER(142, "Lavender bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.LAVENDER, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  865 */     TILE_BUSH_ROSE(143, "Rose bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.ROSE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  870 */     TILE_BUSH_THORN(144, "Thorn bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.THORN, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  875 */     TILE_BUSH_GRAPE(145, "Grape bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.GRAPE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  880 */     TILE_BUSH_CAMELLIA(146, "Camellia bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.CAMELLIA, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  885 */     TILE_BUSH_OLEANDER(147, "Oleander bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.OLEANDER, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  891 */     TILE_MYCELIUM_BUSH_LAVENDER(148, "Infected lavender bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.LAVENDER, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  897 */     TILE_MYCELIUM_BUSH_ROSE(149, "Infected rose bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.ROSE, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  903 */     TILE_MYCELIUM_BUSH_THORN(150, "Infected thorn bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.THORN, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  909 */     TILE_MYCELIUM_BUSH_GRAPE(151, "Infected grape bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.GRAPE, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  915 */     TILE_MYCELIUM_BUSH_CAMELLIA(152, "Infected camellia bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.CAMELLIA, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  921 */     TILE_MYCELIUM_BUSH_OLEANDER(153, "Infected oleander bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.OLEANDER, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  928 */     TILE_ENCHANTED_BUSH_LAVENDER(154, "Enchanted lavender bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.LAVENDER, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  934 */     TILE_ENCHANTED_BUSH_ROSE(155, "Enchanted rose bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.ROSE, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  940 */     TILE_ENCHANTED_BUSH_THORN(156, "Enchanted thorn bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.THORN, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  946 */     TILE_ENCHANTED_BUSH_GRAPE(157, "Enchanted grape bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.GRAPE, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     TILE_ENCHANTED_BUSH_CAMELLIA(158, "Enchanted camellia bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.CAMELLIA, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  958 */     TILE_ENCHANTED_BUSH_OLEANDER(159, "Enchanted oleander bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.OLEANDER, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  965 */     TILE_BUSH_HAZELNUT(160, "Hazelnut bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.HAZELNUT, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  970 */     TILE_MYCELIUM_BUSH_HAZELNUT(161, "Infected hazelnut bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.HAZELNUT, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  976 */     TILE_ENCHANTED_BUSH_HAZELNUT(162, "Enchanted hazelnut bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.HAZELNUT, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  983 */     TILE_TREE_ORANGE(163, "Orange tree", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.ORANGE, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  988 */     TILE_MYCELIUM_TREE_ORANGE(164, "Infected orange tree", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.ORANGE, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  994 */     TILE_ENCHANTED_TREE_ORANGE(165, "Enchanted orange tree", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.ORANGE, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1001 */     TILE_BUSH_RASPBERRYE(166, "Raspberry bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.RASPBERRY, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1006 */     TILE_MYCELIUM_BUSH_RASPBERRY(167, "Infected raspberry bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.RASPBERRY, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1012 */     TILE_ENCHANTED_BUSH_RASPBERRY(168, "Enchanted raspberry bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.RASPBERRY, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1019 */     TILE_BUSH_BLUEBERRY(169, "Blueberry bush", "#293A02", 0.7F, "img.texture.terrain.grass", new Tiles.Flag[] { Tiles.Flag.BLUEBERRY, Tiles.Flag.NORMAL, Tiles.Flag.BOTANIZE, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1024 */     TILE_MYCELIUM_BUSH_BLUEBERRY(170, "Infected blueberry bush", "#DD0229", 0.7F, "img.texture.terrain.mycelium", new Tiles.Flag[] { Tiles.Flag.BLUEBERRY, Tiles.Flag.MYCELIUM, Tiles.Flag.USESNEWDATA, Tiles.Flag.FORAGE, Tiles.Flag.BOTANIZE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1030 */     TILE_ENCHANTED_BUSH_BLUEBERRY(171, "Enchanted blueberry bush", "#1a3418", 0.75F, "img.texture.terrain.tree.enchanted", new Tiles.Flag[] { Tiles.Flag.BLUEBERRY, Tiles.Flag.ENCHANTED, Tiles.Flag.USESNEWDATA }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1037 */     TILE_BUSH_LINGONBERRY(172, "Lingonberry bush", "#293A02", 0.7F, "img.texture.terrain.tundra", new Tiles.Flag[] { Tiles.Flag.LINGONBERRY, Tiles.Flag.NORMAL, Tiles.Flag.FORAGE, Tiles.Flag.USESNEWDATA, Tiles.Flag.TUNDRA }, 60, (byte)1, (byte)1, (byte)0),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1044 */     TILE_CAVE(200, "Cave", "#B9B9B9", 0.8F, "img.texture.cave.rock", new Tiles.Flag[] { Tiles.Flag.CAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1048 */     TILE_CAVE_EXIT(201, "Cave", "Cave exit", "#000000", 0.8F, "img.texture.cave.rock", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ALIGNED }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1053 */     TILE_CAVE_WALL(202, "Cave wall", "#7f7f7f", 0.001F, "img.texture.cave.rock", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.SOLIDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1057 */     TILE_CAVE_WALL_REINFORCED(203, "Reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.rock.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1063 */     TILE_CAVE_WALL_LAVA(204, "Lava wall", "#7f7f7f", 0.0F, "img.texture.terrain.lava", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.SOLIDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1067 */     TILE_CAVE_WALL_SLATE(205, "Slate wall", "#ffffff", 0.0F, "img.texture.cave.slate", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1071 */     TILE_CAVE_WALL_MARBLE(206, "Marble wall", "#ffffff", 0.0F, "img.texture.cave.marble", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1075 */     TILE_CAVE_FLOOR_REINFORCED(207, "Reinforced cave", "Reinforced cave floor", "#7f7f7f", 0.8F, "img.texture.cave.rock.floor.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDFLOOR }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1081 */     TILE_CAVE_WALL_ORE_GOLD(220, "Gold vein", "#ffffff", 0.001F, "img.texture.cave.ore.gold", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1085 */     TILE_CAVE_WALL_ORE_SILVER(221, "Silver vein", "#ffffff", 0.001F, "img.texture.cave.ore.silver", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1090 */     TILE_CAVE_WALL_ORE_IRON(222, "Iron vein", "#ffffff", 0.001F, "img.texture.cave.ore.iron", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 1234, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1094 */     TILE_CAVE_WALL_ORE_COPPER(223, "Copper vein", "#ffffff", 0.001F, "img.texture.cave.ore.copper", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1099 */     TILE_CAVE_WALL_ORE_LEAD(224, "Lead vein", "#ffffff", 0.001F, "img.texture.cave.ore.lead", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1103 */     TILE_CAVE_WALL_ORE_ZINC(225, "Zinc vein", "#ffffff", 0.001F, "img.texture.cave.ore.zinc", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)2, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1107 */     TILE_CAVE_WALL_ORE_TIN(226, "Tin vein", "#ffffff", 0.001F, "img.texture.cave.ore.tin", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */     
/* 1111 */     TILE_CAVE_WALL_ORE_ADAMANTINE(227, "Adamantine vein", "#ffffff", 0.001F, "img.texture.cave.ore.adamantine", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1116 */     TILE_CAVE_WALL_ORE_GLIMMERSTEEL(228, "Glimmersteel vein", "#ffffff", 0.001F, "img.texture.cave.ore.glimmersteel", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1121 */     TILE_CAVE_WALL_ROCKSALT(229, "Rocksalt", "#ffffff", 0.001F, "img.texture.cave.rocksalt", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.SOLIDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1126 */     TILE_CAVE_WALL_SANDSTONE(230, "Sandstone", "#ffffff", 0.001F, "img.texture.cave.sandstone", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.ORECAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1132 */     TILE_CAVE_WALL_STONE_REINFORCED(231, "Stone brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.stone.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1138 */     TILE_CAVE_WALL_SLATE_REINFORCED(232, "Slate brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.slate.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1144 */     TILE_CAVE_WALL_POTTERY_REINFORCED(233, "Pottery brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.pottery.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1150 */     TILE_CAVE_WALL_ROUNDED_STONE_REINFORCED(234, "Rounded stone brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.roundedstone.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1156 */     TILE_CAVE_WALL_SANDSTONE_REINFORCED(235, "Sandstone brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.sandstone.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1162 */     TILE_CAVE_WALL_RENDERED_REINFORCED(236, "Rendered brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.rendered.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1168 */     TILE_CAVE_WALL_MARBLE_REINFORCED(237, "Marble brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.marble.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1174 */     TILE_CAVE_WALL_WOOD_REINFORCED(238, "Wood clad reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.wood.wall.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1181 */     TILE_CAVE_WALL_PART_STONE_REINFORCED(239, "Incomplete stone brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.stone.wall.reinforced.unfinished", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1187 */     TILE_CAVE_WALL_PART_SLATE_REINFORCED(240, "Incomplete slate brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.slate.wall.reinforced.unfinished", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1193 */     TILE_CAVE_WALL_PART_POTTERY_REINFORCED(241, "Incomplete pottery brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.pottery.wall.reinforced.unfinished", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1199 */     TILE_CAVE_WALL_PART_ROUNDED_STONE_REINFORCED(242, "Incomplete rounded stone brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.roundedstone.wall.reinforced.unfinished", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1205 */     TILE_CAVE_WALL_PART_SANDSTONE_REINFORCED(243, "Incomplete sandstone brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.sandstone.wall.reinforced.unfinished", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1211 */     TILE_CAVE_WALL_PART_MARBLE_REINFORCED(244, "Incomplete marble brick reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.marble.wall.reinforced.unfinished", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1217 */     TILE_CAVE_WALL_PART_WOOD_REINFORCED(245, "Incomplete wood clad reinforced cave wall", "#7f7f7f", 0.001F, "img.texture.cave.wood.wall.reinforced.unfinished", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDCAVE }, 60, (byte)0, (byte)1, (byte)1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1223 */     TILE_CAVE_PREPATED_FLOOR_REINFORCED(246, "Prepared reinforced cave", "Prepared reinforced cave floor", "#7f7f7f", 0.8F, "img.texture.cave.prepared.floor.reinforced", new Tiles.Flag[] { Tiles.Flag.CAVE, Tiles.Flag.REINFORCEDFLOOR }, 60, (byte)0, (byte)1, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1248 */     public byte materialId = -1;
/*      */     private boolean aligned = false;
/* 1250 */     private Tiles.TileRoadDirection direction = Tiles.TileRoadDirection.DIR_STRAIGHT;
/* 1251 */     private int woodDifficulity = 2;
/* 1252 */     private String modelName = "model.tree.birch";
/* 1253 */     private BushData.BushType bushType = BushData.BushType.LAVENDER;
/* 1254 */     private TreeData.TreeType treeType = TreeData.TreeType.BIRCH;
/*      */     
/*      */     private boolean isCave = false;
/*      */     
/*      */     private boolean isCaveDoor = false;
/*      */     
/*      */     private boolean isVisibleCaveDoor = false;
/*      */     
/*      */     private boolean isTree = false;
/*      */     
/*      */     private boolean canBearFruit = false;
/*      */     
/*      */     private boolean isBush = false;
/*      */     
/*      */     private boolean isNormal = false;
/*      */     
/*      */     private boolean isMycelium = false;
/*      */     
/*      */     private boolean isEnchanted = false;
/*      */     
/*      */     private boolean isGrass = false;
/*      */     
/*      */     private boolean isTundra = false;
/*      */     
/*      */     private boolean isSolidCave = false;
/*      */     
/*      */     private boolean isReinforcedWall = false;
/*      */     
/*      */     private boolean isReinforcedFloor = false;
/*      */     
/*      */     private boolean isOreCave = false;
/*      */     
/*      */     private boolean isRoad = false;
/*      */     
/*      */     private boolean isFlatRoad = false;
/*      */     
/*      */     private boolean canBotanize = false;
/*      */     
/*      */     private boolean canForage = false;
/*      */     
/*      */     private boolean usesNewData = false;
/*      */     
/*      */     public final byte id;
/*      */     
/*      */     private final int intId;
/*      */     
/*      */     private final Color color;
/*      */     
/*      */     public final float unused;
/*      */     
/*      */     public final float speed;
/*      */     
/*      */     public final String textureResource;
/*      */     
/*      */     public final String tilename;
/*      */     
/*      */     public final String tiledesc;
/*      */     private final int iconId;
/*      */     private final byte waterInfiltrationCode;
/*      */     private final byte waterReservoirCode;
/*      */     private final byte waterLeakageCode;
/*      */     
/*      */     Tile(int id, String name, String uniqueName, String color, float speed, String textureResource, Tiles.Flag[] flags, int iconId, byte waterInfiltration, byte waterReservoir, byte waterLeakage) {
/* 1317 */       this.id = (byte)id;
/* 1318 */       if (this.id == -1)
/* 1319 */         throw new RuntimeException("Illegal id: " + this.id + ". It is reserved for NO_TILE"); 
/* 1320 */       this.intId = id;
/* 1321 */       this.tilename = name;
/* 1322 */       this.tiledesc = uniqueName;
/* 1323 */       this.color = Color.decode(color);
/* 1324 */       this.unused = (new Random()).nextInt();
/* 1325 */       this.speed = speed;
/* 1326 */       this.textureResource = textureResource;
/* 1327 */       this.iconId = iconId;
/* 1328 */       this.waterInfiltrationCode = waterInfiltration;
/* 1329 */       this.waterReservoirCode = waterReservoir;
/* 1330 */       this.waterLeakageCode = waterLeakage;
/* 1331 */       processFlags(flags);
/* 1332 */       Tiles.tiles[id] = this;
/*      */     }
/*      */ 
/*      */     
/*      */     private void processFlags(Tiles.Flag[] flags) {
/* 1337 */       for (Tiles.Flag flag : flags) {
/*      */         
/* 1339 */         switch (flag) {
/*      */           
/*      */           case USESNEWDATA:
/* 1342 */             this.usesNewData = true;
/*      */             break;
/*      */           case ALIGNED:
/* 1345 */             this.aligned = true;
/*      */             break;
/*      */           case TREE:
/* 1348 */             this.isTree = true;
/*      */             break;
/*      */           case BUSH:
/* 1351 */             this.isBush = true;
/*      */             break;
/*      */           case NORMAL:
/* 1354 */             this.isNormal = true;
/*      */             break;
/*      */           case MYCELIUM:
/* 1357 */             this.isMycelium = true;
/*      */             break;
/*      */           case ENCHANTED:
/* 1360 */             this.isEnchanted = true;
/*      */             break;
/*      */           case GRASS:
/* 1363 */             this.isGrass = true;
/*      */             break;
/*      */           case TUNDRA:
/* 1366 */             this.isTundra = true;
/*      */             break;
/*      */           case ROAD:
/* 1369 */             this.isRoad = true;
/*      */             break;
/*      */           case FLATROAD:
/* 1372 */             this.isFlatRoad = true;
/*      */             break;
/*      */           case CAVE:
/* 1375 */             this.isCave = true;
/*      */             break;
/*      */           case CAVEDOOR:
/* 1378 */             this.isCaveDoor = true;
/*      */             break;
/*      */           case VISIBLE_CAVEDOOR:
/* 1381 */             this.isVisibleCaveDoor = true;
/* 1382 */             this.isCaveDoor = true;
/*      */             break;
/*      */           case SOLIDCAVE:
/* 1385 */             this.isSolidCave = true;
/*      */             break;
/*      */           case REINFORCEDCAVE:
/* 1388 */             this.isSolidCave = true;
/* 1389 */             this.isReinforcedWall = true;
/*      */             break;
/*      */           case REINFORCEDFLOOR:
/* 1392 */             this.isReinforcedFloor = true;
/*      */             break;
/*      */           case ORECAVE:
/* 1395 */             this.isSolidCave = true;
/* 1396 */             this.isOreCave = true;
/*      */             break;
/*      */           case BIRCH:
/* 1399 */             this.isTree = true;
/* 1400 */             this.treeType = TreeData.TreeType.BIRCH;
/* 1401 */             this.modelName = this.treeType.getModelName();
/* 1402 */             this.materialId = this.treeType.getMaterial();
/* 1403 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1404 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case PINE:
/* 1407 */             this.isTree = true;
/* 1408 */             this.treeType = TreeData.TreeType.PINE;
/* 1409 */             this.modelName = this.treeType.getModelName();
/* 1410 */             this.materialId = this.treeType.getMaterial();
/* 1411 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1412 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case OAK:
/* 1415 */             this.isTree = true;
/* 1416 */             this.treeType = TreeData.TreeType.OAK;
/* 1417 */             this.modelName = this.treeType.getModelName();
/* 1418 */             this.materialId = this.treeType.getMaterial();
/* 1419 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1420 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case CEDAR:
/* 1423 */             this.isTree = true;
/* 1424 */             this.treeType = TreeData.TreeType.CEDAR;
/* 1425 */             this.modelName = this.treeType.getModelName();
/* 1426 */             this.materialId = this.treeType.getMaterial();
/* 1427 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1428 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case WILLOW:
/* 1431 */             this.isTree = true;
/* 1432 */             this.treeType = TreeData.TreeType.WILLOW;
/* 1433 */             this.modelName = this.treeType.getModelName();
/* 1434 */             this.materialId = this.treeType.getMaterial();
/* 1435 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1436 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case MAPLE:
/* 1439 */             this.isTree = true;
/* 1440 */             this.treeType = TreeData.TreeType.MAPLE;
/* 1441 */             this.modelName = this.treeType.getModelName();
/* 1442 */             this.materialId = this.treeType.getMaterial();
/* 1443 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1444 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case APPLE:
/* 1447 */             this.isTree = true;
/* 1448 */             this.treeType = TreeData.TreeType.APPLE;
/* 1449 */             this.modelName = this.treeType.getModelName();
/* 1450 */             this.materialId = this.treeType.getMaterial();
/* 1451 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1452 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case LEMON:
/* 1455 */             this.isTree = true;
/* 1456 */             this.treeType = TreeData.TreeType.LEMON;
/* 1457 */             this.modelName = this.treeType.getModelName();
/* 1458 */             this.materialId = this.treeType.getMaterial();
/* 1459 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1460 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case OLIVE:
/* 1463 */             this.isTree = true;
/* 1464 */             this.treeType = TreeData.TreeType.OLIVE;
/* 1465 */             this.modelName = this.treeType.getModelName();
/* 1466 */             this.materialId = this.treeType.getMaterial();
/* 1467 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1468 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case CHERRY:
/* 1471 */             this.isTree = true;
/* 1472 */             this.treeType = TreeData.TreeType.CHERRY;
/* 1473 */             this.modelName = this.treeType.getModelName();
/* 1474 */             this.materialId = this.treeType.getMaterial();
/* 1475 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1476 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case CHESTNUT:
/* 1479 */             this.isTree = true;
/* 1480 */             this.treeType = TreeData.TreeType.CHESTNUT;
/* 1481 */             this.modelName = this.treeType.getModelName();
/* 1482 */             this.materialId = this.treeType.getMaterial();
/* 1483 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1484 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case WALNUT:
/* 1487 */             this.isTree = true;
/* 1488 */             this.treeType = TreeData.TreeType.WALNUT;
/* 1489 */             this.modelName = this.treeType.getModelName();
/* 1490 */             this.materialId = this.treeType.getMaterial();
/* 1491 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1492 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case FIR:
/* 1495 */             this.isTree = true;
/* 1496 */             this.treeType = TreeData.TreeType.FIR;
/* 1497 */             this.modelName = this.treeType.getModelName();
/* 1498 */             this.materialId = this.treeType.getMaterial();
/* 1499 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1500 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case LINDEN:
/* 1503 */             this.isTree = true;
/* 1504 */             this.treeType = TreeData.TreeType.LINDEN;
/* 1505 */             this.modelName = this.treeType.getModelName();
/* 1506 */             this.materialId = this.treeType.getMaterial();
/* 1507 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1508 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case ORANGE:
/* 1511 */             this.isTree = true;
/* 1512 */             this.treeType = TreeData.TreeType.ORANGE;
/* 1513 */             this.modelName = this.treeType.getModelName();
/* 1514 */             this.materialId = this.treeType.getMaterial();
/* 1515 */             this.woodDifficulity = this.treeType.getDifficulty();
/* 1516 */             this.canBearFruit = this.treeType.canBearFruit();
/*      */             break;
/*      */           case LAVENDER:
/* 1519 */             this.isBush = true;
/* 1520 */             this.bushType = BushData.BushType.LAVENDER;
/* 1521 */             this.materialId = this.bushType.getMaterial();
/* 1522 */             this.modelName = this.bushType.getModelName();
/* 1523 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1524 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case ROSE:
/* 1527 */             this.isBush = true;
/* 1528 */             this.bushType = BushData.BushType.ROSE;
/* 1529 */             this.modelName = this.bushType.getModelName();
/* 1530 */             this.materialId = this.bushType.getMaterial();
/* 1531 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1532 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case THORN:
/* 1535 */             this.isBush = true;
/* 1536 */             this.bushType = BushData.BushType.THORN;
/* 1537 */             this.modelName = this.bushType.getModelName();
/* 1538 */             this.materialId = this.bushType.getMaterial();
/* 1539 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1540 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case GRAPE:
/* 1543 */             this.isBush = true;
/* 1544 */             this.bushType = BushData.BushType.GRAPE;
/* 1545 */             this.modelName = this.bushType.getModelName();
/* 1546 */             this.materialId = this.bushType.getMaterial();
/* 1547 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1548 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case CAMELLIA:
/* 1551 */             this.isBush = true;
/* 1552 */             this.bushType = BushData.BushType.CAMELLIA;
/* 1553 */             this.modelName = this.bushType.getModelName();
/* 1554 */             this.materialId = this.bushType.getMaterial();
/* 1555 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1556 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case OLEANDER:
/* 1559 */             this.isBush = true;
/* 1560 */             this.bushType = BushData.BushType.OLEANDER;
/* 1561 */             this.modelName = this.bushType.getModelName();
/* 1562 */             this.materialId = this.bushType.getMaterial();
/* 1563 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1564 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case HAZELNUT:
/* 1567 */             this.isBush = true;
/* 1568 */             this.bushType = BushData.BushType.HAZELNUT;
/* 1569 */             this.modelName = this.bushType.getModelName();
/* 1570 */             this.materialId = this.bushType.getMaterial();
/* 1571 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1572 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case RASPBERRY:
/* 1575 */             this.isBush = true;
/* 1576 */             this.bushType = BushData.BushType.RASPBERRY;
/* 1577 */             this.modelName = this.bushType.getModelName();
/* 1578 */             this.materialId = this.bushType.getMaterial();
/* 1579 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1580 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case BLUEBERRY:
/* 1583 */             this.isBush = true;
/* 1584 */             this.bushType = BushData.BushType.BLUEBERRY;
/* 1585 */             this.modelName = this.bushType.getModelName();
/* 1586 */             this.materialId = this.bushType.getMaterial();
/* 1587 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1588 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case LINGONBERRY:
/* 1591 */             this.isBush = true;
/* 1592 */             this.bushType = BushData.BushType.LINGONBERRY;
/* 1593 */             this.modelName = this.bushType.getModelName();
/* 1594 */             this.materialId = this.bushType.getMaterial();
/* 1595 */             this.woodDifficulity = this.bushType.getDifficulty();
/* 1596 */             this.canBearFruit = this.bushType.canBearFruit();
/*      */             break;
/*      */           case BOTANIZE:
/* 1599 */             this.canBotanize = true;
/*      */             break;
/*      */           case FORAGE:
/* 1602 */             this.canForage = true;
/*      */             break;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getTileName(byte data) {
/* 1612 */       if (this == TILE_TREE)
/*      */       {
/* 1614 */         return TreeData.getTypeName(data);
/*      */       }
/* 1616 */       if (this == TILE_BUSH)
/*      */       {
/* 1618 */         return BushData.getTypeName(data);
/*      */       }
/* 1620 */       if (this == TILE_MYCELIUM_TREE)
/*      */       {
/* 1622 */         return "Infected " + TreeData.getTypeName(data);
/*      */       }
/* 1624 */       if (this == TILE_MYCELIUM_BUSH)
/*      */       {
/* 1626 */         return "Infected " + BushData.getTypeName(data);
/*      */       }
/* 1628 */       if (this == TILE_ENCHANTED_TREE)
/*      */       {
/* 1630 */         return "Enchanted " + TreeData.getTypeName(data);
/*      */       }
/* 1632 */       if (this == TILE_ENCHANTED_BUSH)
/*      */       {
/* 1634 */         return "Enchanted " + BushData.getTypeName(data);
/*      */       }
/* 1636 */       if (this == TILE_GRASS)
/*      */       {
/* 1638 */         return GrassData.GrassType.decodeTileData(data).getName();
/*      */       }
/* 1640 */       if (this == TILE_KELP)
/*      */       {
/* 1642 */         return "Kelp";
/*      */       }
/* 1644 */       if (this == TILE_REED)
/*      */       {
/* 1646 */         return "Reed";
/*      */       }
/* 1648 */       if (this == TILE_LAWN)
/*      */       {
/* 1650 */         return "Lawn";
/*      */       }
/* 1652 */       if (this.isCave) {
/*      */         
/* 1654 */         if (data == 0)
/* 1655 */           return this.tilename + " floor"; 
/* 1656 */         if (data == 1)
/*      */         {
/* 1658 */           return "Cave ceiling"; } 
/* 1659 */         return this.tilename;
/*      */       } 
/*      */ 
/*      */       
/* 1663 */       return this.tilename;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getHelpSubject(byte data) {
/* 1669 */       if (this == TILE_TREE)
/*      */       {
/* 1671 */         return TreeData.getHelpSubject(data, false);
/*      */       }
/* 1673 */       if (this == TILE_BUSH)
/*      */       {
/* 1675 */         return BushData.getHelpSubject(data, false);
/*      */       }
/* 1677 */       if (this == TILE_MYCELIUM_TREE)
/*      */       {
/* 1679 */         return TreeData.getHelpSubject(data, true);
/*      */       }
/* 1681 */       if (this == TILE_MYCELIUM_BUSH)
/*      */       {
/* 1683 */         return BushData.getHelpSubject(data, true);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1689 */       if (this.intId >= TILE_CAVE.intId) {
/*      */         
/* 1691 */         if (data == 0)
/* 1692 */           return "Terrain:" + this.tilename + "_floor"; 
/* 1693 */         if (data == 1)
/* 1694 */           return "Terrain:" + this.tilename + "_ceiling"; 
/* 1695 */         return "Terrain:" + this.tilename;
/*      */       } 
/*      */ 
/*      */       
/* 1699 */       return "Terrain:" + this.tilename;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Tile[] getTiles() {
/* 1708 */       return Tiles.tiles;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Tile[] getTiles(int category, String filter) {
/* 1730 */       Set<Tile> catTiles = new HashSet<>();
/* 1731 */       for (Tile tile : Tiles.tiles) {
/*      */         
/* 1733 */         if (tile != null && tile.direction == Tiles.TileRoadDirection.DIR_STRAIGHT)
/*      */         {
/* 1735 */           switch (category) {
/*      */ 
/*      */             
/*      */             case 2:
/* 1739 */               if (tile.isCave() && wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1740 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */             
/*      */             case 6:
/* 1745 */               if (!tile.isCave() && wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1746 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */             
/*      */             case 3:
/* 1751 */               if (tile.isCaveDoor() && wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1752 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */             
/*      */             case 4:
/* 1757 */               if (!tile.isCave() && !tile.isTree() && !tile.isBush() && !tile.isRoad() && !tile.isCaveDoor() && 
/* 1758 */                 wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1759 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */             
/*      */             case 7:
/* 1764 */               if (tile.isTree() && wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1765 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */             
/*      */             case 1:
/* 1770 */               if (tile.isBush() && wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1771 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */             
/*      */             case 5:
/* 1776 */               if (tile.isRoad() && wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1777 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */             
/*      */             default:
/* 1782 */               if (wildCardMatch(tile.getName().toLowerCase(), filter.toLowerCase())) {
/* 1783 */                 catTiles.add(tile);
/*      */               }
/*      */               break;
/*      */           } 
/*      */         }
/*      */       } 
/* 1789 */       return catTiles.<Tile>toArray(new Tile[catTiles.size()]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static boolean wildCardMatch(String text, String pattern) {
/* 1808 */       String[] cards = pattern.split("\\*");
/*      */ 
/*      */       
/* 1811 */       int offset = 0;
/* 1812 */       boolean first = true;
/* 1813 */       for (String card : cards) {
/*      */         
/* 1815 */         if (card.length() > 0) {
/*      */           
/* 1817 */           int idx = text.indexOf(card, offset);
/*      */ 
/*      */           
/* 1820 */           if (idx == -1 || (first && idx != 0)) {
/* 1821 */             return false;
/*      */           }
/*      */           
/* 1824 */           offset = idx + card.length();
/*      */         } 
/*      */         
/* 1827 */         first = false;
/*      */       } 
/* 1829 */       if (offset < text.length() && !pattern.endsWith("*"))
/* 1830 */         return false; 
/* 1831 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 1841 */       return this.tilename;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getDesc() {
/* 1851 */       return this.tiledesc;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getId() {
/* 1861 */       return this.id;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getIntId() {
/* 1871 */       return this.intId;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Color getColor() {
/* 1879 */       return this.color;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float getSpeed() {
/* 1887 */       return this.speed;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getTextureResource() {
/* 1895 */       return this.textureResource;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte getMaterialId() {
/* 1900 */       return this.materialId;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTexturePosX(byte data) {
/* 1905 */       if (isTree())
/* 1906 */         return getTreeType(data).getTexturPosX(); 
/* 1907 */       if (isBush())
/* 1908 */         return getBushType(data).getTexturPosX(); 
/* 1909 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTexturePosY(byte data) {
/* 1914 */       if (isTree())
/* 1915 */         return getTreeType(data).getTexturPosY(); 
/* 1916 */       if (isBush())
/* 1917 */         return getBushType(data).getTexturPosY(); 
/* 1918 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getTreeImageWidth(byte data) {
/* 1923 */       if (isTree())
/* 1924 */         return getTreeType(data).getWidth(); 
/* 1925 */       if (isBush())
/* 1926 */         return getBushType(data).getWidth(); 
/* 1927 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getTreeImageHeight(byte data) {
/* 1932 */       if (isTree())
/* 1933 */         return getTreeType(data).getHeight(); 
/* 1934 */       if (isBush())
/* 1935 */         return getBushType(data).getHeight(); 
/* 1936 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getTreeBaseRadius(byte data) {
/* 1941 */       if (isTree())
/* 1942 */         return getTreeType(data).getRadius(); 
/* 1943 */       if (isBush())
/* 1944 */         return getBushType(data).getRadius(); 
/* 1945 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getModelResourceName(byte data) {
/* 1950 */       int treeAge = FoliageAge.getAgeAsByte(data);
/*      */       
/* 1952 */       if (isTree())
/*      */       {
/* 1954 */         return getTreeType(data).getModelResourceName(treeAge);
/*      */       }
/* 1956 */       if (isBush())
/*      */       {
/* 1958 */         return getBushType(data).getModelResourceName(treeAge);
/*      */       }
/*      */       
/* 1961 */       return this.modelName;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWoodDificulity() {
/* 1966 */       return this.woodDifficulity;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBotanize() {
/* 1971 */       return this.canBotanize;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canForage() {
/* 1976 */       return this.canForage;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean usesNewData() {
/* 1981 */       return this.usesNewData;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAligned() {
/* 1989 */       return this.aligned;
/*      */     }
/*      */ 
/*      */     
/*      */     public Tiles.TileRoadDirection getDirection() {
/* 1994 */       return this.direction;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isCave() {
/* 1999 */       return this.isCave;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isCaveDoor() {
/* 2004 */       return this.isCaveDoor;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isVisibleCaveDoor() {
/* 2009 */       return this.isVisibleCaveDoor;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isTree() {
/* 2014 */       return this.isTree;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBearFruit() {
/* 2019 */       return this.canBearFruit;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isBush() {
/* 2024 */       return this.isBush;
/*      */     }
/*      */ 
/*      */     
/*      */     public final byte getWaterInfiltrationCode() {
/* 2029 */       return this.waterInfiltrationCode;
/*      */     }
/*      */ 
/*      */     
/*      */     public final byte getWaterReservoirCode() {
/* 2034 */       return this.waterReservoirCode;
/*      */     }
/*      */ 
/*      */     
/*      */     public final byte getWaterLeakageCode() {
/* 2039 */       return this.waterLeakageCode;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isOak(byte data) {
/* 2051 */       if (!this.isTree)
/* 2052 */         return false; 
/* 2053 */       return (getTreeType(data) == TreeData.TreeType.OAK);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isWillow(byte data) {
/* 2065 */       if (!this.isTree)
/* 2066 */         return false; 
/* 2067 */       return (getTreeType(data) == TreeData.TreeType.WILLOW);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isThorn(byte data) {
/* 2079 */       if (!this.isBush)
/* 2080 */         return false; 
/* 2081 */       return (getBushType(data) == BushData.BushType.THORN);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isMaple(byte data) {
/* 2086 */       if (!this.isTree)
/* 2087 */         return false; 
/* 2088 */       return (getTreeType(data) == TreeData.TreeType.MAPLE);
/*      */     }
/*      */ 
/*      */     
/*      */     public TreeData.TreeType getTreeType(byte data) {
/* 2093 */       if (this.usesNewData)
/* 2094 */         return this.treeType; 
/* 2095 */       return TreeData.TreeType.fromInt(data & 0xF);
/*      */     }
/*      */ 
/*      */     
/*      */     public BushData.BushType getBushType(byte data) {
/* 2100 */       if (this.usesNewData)
/* 2101 */         return this.bushType; 
/* 2102 */       return BushData.BushType.fromInt(data & 0xF);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isNormal() {
/* 2107 */       return this.isNormal;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isMycelium() {
/* 2112 */       return this.isMycelium;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEnchanted() {
/* 2117 */       return this.isEnchanted;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isNormalBush() {
/* 2122 */       return (this.isNormal && this.isBush);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isMyceliumBush() {
/* 2127 */       return (this.isMycelium && this.isBush);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEnchantedBush() {
/* 2132 */       return (this.isEnchanted && this.isBush);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isNormalTree() {
/* 2137 */       return (this.isNormal && this.isTree);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isMyceliumTree() {
/* 2142 */       return (this.isMycelium && this.isTree);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEnchantedTree() {
/* 2147 */       return (this.isEnchanted && this.isTree);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isGrass() {
/* 2152 */       return this.isGrass;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isTundra() {
/* 2157 */       return this.isTundra;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSolidCave() {
/* 2162 */       return this.isSolidCave;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isReinforcedCave() {
/* 2167 */       return this.isReinforcedWall;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isReinforcedFloor() {
/* 2172 */       return this.isReinforcedFloor;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isOreCave() {
/* 2177 */       return this.isOreCave;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRoad() {
/* 2182 */       return this.isRoad;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isFlatRoad() {
/* 2187 */       return this.isFlatRoad;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getIconId() {
/* 2192 */       return this.iconId;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2201 */       return super.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public enum TileBorderDirection
/*      */   {
/* 2208 */     DIR_HORIZ((byte)0),
/* 2209 */     DIR_DOWN((byte)2),
/* 2210 */     CORNER((byte)4);
/*      */     
/*      */     private byte id;
/*      */ 
/*      */     
/*      */     TileBorderDirection(byte newId) {
/* 2216 */       this.id = newId;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte getCode() {
/* 2221 */       return this.id;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum TileRoadDirection
/*      */   {
/* 2233 */     DIR_STRAIGHT((byte)0),
/* 2234 */     DIR_NW((byte)1),
/* 2235 */     DIR_NE((byte)2),
/* 2236 */     DIR_SE((byte)3),
/* 2237 */     DIR_SW((byte)4);
/*      */     
/*      */     private byte id;
/*      */ 
/*      */     
/*      */     TileRoadDirection(byte newId) {
/* 2243 */       this.id = newId;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte getCode() {
/* 2248 */       return this.id;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static Tile getTile(int id) {
/* 2263 */     if (tiles[id & 0xFF] == null)
/*      */     {
/*      */       
/* 2266 */       int i = (Tile.values()).length;
/*      */     }
/* 2268 */     return tiles[id & 0xFF];
/*      */   }
/*      */ 
/*      */   
/*      */   public static Tile getTile(byte id) {
/* 2273 */     return getTile(id & 0xFF);
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
/*      */   public static int encode(float height, byte type, byte data) {
/* 2288 */     return encode((short)(int)(height * 10.0F), type, data);
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
/*      */   public static int encode(short height, byte type, byte data) {
/* 2303 */     return ((type & 0xFF) << 24) + ((data & 0xFF) << 16) + (height & 0xFFFF);
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
/*      */   public static int encode(short height, short tileData) {
/* 2316 */     return ((tileData & 0xFFFF) << 16) + (height & 0xFFFF);
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
/*      */   public static short decodeHeight(int encodedTile) {
/* 2328 */     return (short)(encodedTile & 0xFFFF);
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
/*      */   public static short decodeTileX(long clientWurmId) {
/* 2341 */     return (short)(int)(clientWurmId >> 32L & 0xFFFFL);
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
/*      */   public static int decodeTileY(long clientWurmId) {
/* 2354 */     return (short)(int)(clientWurmId >> 16L & 0xFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int decodeHeightOffset(long clientWurmId) {
/* 2365 */     return (short)(int)(clientWurmId >> 48L & 0xFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int decodeFloorLevel(long clientWurmId) {
/* 2376 */     return decodeHeightOffset(clientWurmId) / 30;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TileBorderDirection decodeDirection(long clientWurmId) {
/* 2387 */     byte code = (byte)(int)(clientWurmId >> 8L & 0xFL);
/* 2388 */     if (code == 0)
/* 2389 */       return TileBorderDirection.DIR_HORIZ; 
/* 2390 */     if (code == 2)
/* 2391 */       return TileBorderDirection.DIR_DOWN; 
/* 2392 */     if (code == 4) {
/* 2393 */       return TileBorderDirection.CORNER;
/*      */     }
/* 2395 */     return TileBorderDirection.DIR_HORIZ;
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
/*      */   public static byte decodeLayer(long clientWurmId) {
/* 2407 */     byte code = (byte)(int)(clientWurmId >> 12L & 0xFL);
/* 2408 */     if (code == 0) {
/* 2409 */       return 0;
/*      */     }
/* 2411 */     return -1;
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
/*      */   public static short decodeTileData(int encodedTile) {
/* 2423 */     return (short)(encodedTile >> 16 & 0xFFFF);
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
/*      */   public static float decodeHeightAsFloat(int encodedTile) {
/* 2435 */     return (short)(encodedTile & 0xFFFF) / 10.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte decodeType(int encodedTile) {
/* 2445 */     int type = encodedTile >> 24 & 0xFF;
/* 2446 */     return (byte)type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte decodeData(int encodedTile) {
/* 2456 */     return (byte)(encodedTile >> 16 & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isRoadType(int encodedTile) {
/* 2467 */     return isRoadType((byte)(encodedTile >> 24 & 0xFF));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isRoadType(byte type) {
/* 2478 */     if (type == Tile.TILE_COBBLESTONE.id || type == Tile.TILE_COBBLESTONE_ROUGH.id || type == Tile.TILE_COBBLESTONE_ROUND.id || type == Tile.TILE_STONE_SLABS.id || type == Tile.TILE_GRAVEL.id || type == Tile.TILE_POTTERY_BRICKS.id || type == Tile.TILE_SLATE_SLABS.id || type == Tile.TILE_SLATE_BRICKS.id || type == Tile.TILE_SANDSTONE_SLABS.id || type == Tile.TILE_SANDSTONE_BRICKS.id || type == Tile.TILE_MARBLE_SLABS.id || type == Tile.TILE_MARBLE_BRICKS.id || type == Tile.TILE_PLANKS.id || type == Tile.TILE_PLANKS_TARRED.id)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2485 */       return true; } 
/* 2486 */     return false;
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
/*      */   public static byte getSecondTypeforRoad(int _type) {
/*      */     int toReturn;
/* 2500 */     if (isTundra(_type))
/* 2501 */     { toReturn = 19; }
/* 2502 */     else if (isEnchanted(_type))
/* 2503 */     { toReturn = 13; }
/* 2504 */     else if (isMycelium(_type))
/* 2505 */     { toReturn = 10; }
/* 2506 */     else if (isNormal(_type))
/* 2507 */     { toReturn = 2; }
/*      */     else
/* 2509 */     { switch (_type & 0xFF)
/*      */       
/*      */       { case 7:
/*      */         case 12:
/* 2513 */           toReturn = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2529 */           return (byte)toReturn;case 21: case 25: case 26: case 27: case 28: case 29: toReturn = 4; return (byte)toReturn;case 0: toReturn = 9; return (byte)toReturn; }  toReturn = _type; }  return (byte)toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float shortHeightToFloat(short s) {
/* 2539 */     return s / 10.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSolidCave(byte _type) {
/* 2548 */     switch (_type & 0xFF)
/*      */     
/*      */     { case 202:
/*      */       case 203:
/*      */       case 204:
/*      */       case 205:
/*      */       case 206:
/*      */       case 220:
/*      */       case 221:
/*      */       case 222:
/*      */       case 223:
/*      */       case 224:
/*      */       case 225:
/*      */       case 226:
/*      */       case 227:
/*      */       case 228:
/*      */       case 229:
/*      */       case 230:
/*      */       case 231:
/*      */       case 232:
/*      */       case 233:
/*      */       case 234:
/*      */       case 235:
/*      */       case 236:
/*      */       case 237:
/*      */       case 238:
/*      */       case 239:
/*      */       case 240:
/*      */       case 241:
/*      */       case 242:
/*      */       case 243:
/*      */       case 244:
/*      */       case 245:
/* 2581 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2587 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isReinforcedCaveWall(byte _id) {
/* 2596 */     switch (_id & 0xFF)
/*      */     
/*      */     { case 203:
/*      */       case 231:
/*      */       case 232:
/*      */       case 233:
/*      */       case 234:
/*      */       case 235:
/*      */       case 236:
/*      */       case 237:
/*      */       case 238:
/*      */       case 239:
/*      */       case 240:
/*      */       case 241:
/*      */       case 242:
/*      */       case 243:
/*      */       case 244:
/*      */       case 245:
/* 2614 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2620 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSolidCave(Tile theTile) {
/* 2625 */     return theTile.isSolidCave();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isOreCave(byte _id) {
/* 2634 */     switch (_id & 0xFF)
/*      */     
/*      */     { case 205:
/*      */       case 206:
/*      */       case 220:
/*      */       case 221:
/*      */       case 222:
/*      */       case 223:
/*      */       case 224:
/*      */       case 225:
/*      */       case 226:
/*      */       case 227:
/*      */       case 228:
/*      */       case 230:
/* 2648 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2654 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isReinforcedCave(byte _id) {
/* 2663 */     switch (_id & 0xFF)
/*      */     
/*      */     { case 203:
/*      */       case 205:
/*      */       case 206:
/*      */       case 220:
/*      */       case 221:
/*      */       case 222:
/*      */       case 223:
/*      */       case 224:
/*      */       case 225:
/*      */       case 226:
/*      */       case 227:
/*      */       case 228:
/*      */       case 230:
/*      */       case 231:
/*      */       case 232:
/*      */       case 233:
/*      */       case 234:
/*      */       case 235:
/*      */       case 236:
/*      */       case 237:
/*      */       case 238:
/*      */       case 239:
/*      */       case 240:
/*      */       case 241:
/*      */       case 242:
/*      */       case 243:
/*      */       case 244:
/*      */       case 245:
/* 2693 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2699 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isReinforcedFloor(byte _id) {
/* 2704 */     switch (_id & 0xFF) {
/*      */       
/*      */       case 207:
/*      */       case 246:
/* 2708 */         return true;
/*      */     } 
/* 2710 */     return isRoadType(_id);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMineDoor(int _id) {
/* 2715 */     return (_id == 26 || isVisibleMineDoor(_id));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMineDoor(byte _id) {
/* 2720 */     return isMineDoor(_id & 0xFF);
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
/*      */   public static boolean isMineDoor(Tile tile) {
/* 2733 */     return (tile != null && isMineDoor(tile.intId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEnchanted(int tileId) {
/* 2741 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 13:
/*      */       case 14:
/*      */       case 34:
/*      */       case 128:
/*      */       case 129:
/*      */       case 130:
/*      */       case 131:
/*      */       case 132:
/*      */       case 133:
/*      */       case 134:
/*      */       case 135:
/*      */       case 136:
/*      */       case 137:
/*      */       case 138:
/*      */       case 139:
/*      */       case 140:
/*      */       case 141:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*      */       case 159:
/*      */       case 165:
/* 2767 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2773 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEnchantedBush(int tileId) {
/* 2781 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 34:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*      */       case 159:
/* 2790 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2796 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEnchantedTree(int tileId) {
/* 2804 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 14:
/*      */       case 128:
/*      */       case 129:
/*      */       case 130:
/*      */       case 131:
/*      */       case 132:
/*      */       case 133:
/*      */       case 134:
/*      */       case 135:
/*      */       case 136:
/*      */       case 137:
/*      */       case 138:
/*      */       case 139:
/*      */       case 140:
/*      */       case 141:
/*      */       case 165:
/* 2822 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2828 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isMycelium(int tileId) {
/* 2836 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 10:
/*      */       case 11:
/*      */       case 35:
/*      */       case 40:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 120:
/*      */       case 121:
/*      */       case 122:
/*      */       case 123:
/*      */       case 124:
/*      */       case 125:
/*      */       case 126:
/*      */       case 127:
/*      */       case 148:
/*      */       case 149:
/*      */       case 150:
/*      */       case 151:
/*      */       case 152:
/*      */       case 153:
/*      */       case 161:
/*      */       case 164:
/* 2864 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2870 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isMyceliumBush(int tileId) {
/* 2878 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 35:
/*      */       case 148:
/*      */       case 149:
/*      */       case 150:
/*      */       case 151:
/*      */       case 152:
/*      */       case 153:
/*      */       case 161:
/* 2888 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2894 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isMyceliumTree(int tileId) {
/* 2902 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 11:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 120:
/*      */       case 121:
/*      */       case 122:
/*      */       case 123:
/*      */       case 124:
/*      */       case 125:
/*      */       case 126:
/*      */       case 127:
/*      */       case 164:
/* 2920 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2926 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTundra(int tileId) {
/* 2931 */     switch (tileId & 0xFF) {
/*      */       
/*      */       case 19:
/*      */       case 172:
/* 2935 */         return true;
/*      */     } 
/* 2937 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNormal(int tileId) {
/* 2945 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 2:
/*      */       case 3:
/*      */       case 31:
/*      */       case 38:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 108:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 142:
/*      */       case 143:
/*      */       case 144:
/*      */       case 145:
/*      */       case 146:
/*      */       case 147:
/*      */       case 160:
/*      */       case 163:
/*      */       case 166:
/*      */       case 169:
/* 2975 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2981 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNormalBush(int tileId) {
/* 2989 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 31:
/*      */       case 142:
/*      */       case 143:
/*      */       case 144:
/*      */       case 145:
/*      */       case 146:
/*      */       case 147:
/*      */       case 160:
/*      */       case 166:
/*      */       case 169:
/*      */       case 172:
/* 3002 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3008 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNormalTree(int tileId) {
/* 3016 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 3:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 108:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 163:
/* 3034 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3040 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toNormal(int tileId) {
/* 3051 */     switch (tileId & 0xFF) {
/*      */       
/*      */       case 11:
/*      */       case 14:
/* 3055 */         return 3;
/*      */       case 40:
/* 3057 */         return 38;
/*      */       case 10:
/*      */       case 13:
/* 3060 */         return 2;
/*      */       case 34:
/*      */       case 35:
/* 3063 */         return 31;
/*      */       case 148:
/*      */       case 154:
/* 3066 */         return 142;
/*      */       case 149:
/*      */       case 155:
/* 3069 */         return 143;
/*      */       case 150:
/*      */       case 156:
/* 3072 */         return 144;
/*      */       case 151:
/*      */       case 157:
/* 3075 */         return 145;
/*      */       case 152:
/*      */       case 158:
/* 3078 */         return 146;
/*      */       case 153:
/*      */       case 159:
/* 3081 */         return 147;
/*      */       case 161:
/*      */       case 162:
/* 3084 */         return 160;
/*      */       case 167:
/*      */       case 168:
/* 3087 */         return 166;
/*      */       case 170:
/*      */       case 171:
/* 3090 */         return 169;
/*      */ 
/*      */       
/*      */       case 114:
/*      */       case 128:
/* 3095 */         return 100;
/*      */       case 115:
/*      */       case 129:
/* 3098 */         return 101;
/*      */       case 116:
/*      */       case 130:
/* 3101 */         return 102;
/*      */       case 117:
/*      */       case 131:
/* 3104 */         return 103;
/*      */       case 118:
/*      */       case 132:
/* 3107 */         return 104;
/*      */       case 119:
/*      */       case 133:
/* 3110 */         return 105;
/*      */       case 120:
/*      */       case 134:
/* 3113 */         return 106;
/*      */       case 121:
/*      */       case 135:
/* 3116 */         return 107;
/*      */       case 122:
/*      */       case 136:
/* 3119 */         return 108;
/*      */       case 123:
/*      */       case 137:
/* 3122 */         return 109;
/*      */       case 124:
/*      */       case 138:
/* 3125 */         return 110;
/*      */       case 125:
/*      */       case 139:
/* 3128 */         return 111;
/*      */       case 126:
/*      */       case 140:
/* 3131 */         return 112;
/*      */       case 127:
/*      */       case 141:
/* 3134 */         return 113;
/*      */       case 164:
/*      */       case 165:
/* 3137 */         return 163;
/*      */     } 
/* 3139 */     return tileId;
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
/*      */   public static int toEnchanted(int tileId) {
/* 3151 */     switch (tileId & 0xFF) {
/*      */       
/*      */       case 3:
/* 3154 */         return 14;
/*      */       case 2:
/* 3156 */         return 13;
/*      */       case 31:
/* 3158 */         return 34;
/*      */       case 142:
/* 3160 */         return 154;
/*      */       case 143:
/* 3162 */         return 155;
/*      */       case 144:
/* 3164 */         return 156;
/*      */       case 145:
/* 3166 */         return 157;
/*      */       case 146:
/* 3168 */         return 158;
/*      */       case 147:
/* 3170 */         return 159;
/*      */       case 160:
/* 3172 */         return 162;
/*      */       case 166:
/* 3174 */         return 168;
/*      */       case 169:
/* 3176 */         return 171;
/*      */       case 172:
/* 3178 */         return 172;
/*      */       case 100:
/* 3180 */         return 128;
/*      */       case 101:
/* 3182 */         return 129;
/*      */       case 102:
/* 3184 */         return 130;
/*      */       case 103:
/* 3186 */         return 131;
/*      */       case 104:
/* 3188 */         return 132;
/*      */       case 105:
/* 3190 */         return 133;
/*      */       case 106:
/* 3192 */         return 134;
/*      */       case 107:
/* 3194 */         return 135;
/*      */       case 108:
/* 3196 */         return 136;
/*      */       case 109:
/* 3198 */         return 137;
/*      */       case 110:
/* 3200 */         return 138;
/*      */       case 111:
/* 3202 */         return 139;
/*      */       case 112:
/* 3204 */         return 140;
/*      */       case 113:
/* 3206 */         return 141;
/*      */       case 163:
/* 3208 */         return 165;
/*      */     } 
/* 3210 */     return tileId;
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
/*      */   public static int toMycelium(int tileId) {
/* 3222 */     switch (tileId & 0xFF) {
/*      */       
/*      */       case 3:
/* 3225 */         return 11;
/*      */       case 2:
/* 3227 */         return 10;
/*      */       case 38:
/* 3229 */         return 40;
/*      */       case 31:
/* 3231 */         return 35;
/*      */       case 142:
/* 3233 */         return 148;
/*      */       case 143:
/* 3235 */         return 149;
/*      */       case 144:
/* 3237 */         return 150;
/*      */       case 145:
/* 3239 */         return 151;
/*      */       case 146:
/* 3241 */         return 152;
/*      */       case 147:
/* 3243 */         return 153;
/*      */       case 160:
/* 3245 */         return 161;
/*      */       case 166:
/* 3247 */         return 167;
/*      */       case 169:
/* 3249 */         return 170;
/*      */       case 172:
/* 3251 */         return 172;
/*      */       case 100:
/* 3253 */         return 114;
/*      */       case 101:
/* 3255 */         return 115;
/*      */       case 102:
/* 3257 */         return 116;
/*      */       case 103:
/* 3259 */         return 117;
/*      */       case 104:
/* 3261 */         return 118;
/*      */       case 105:
/* 3263 */         return 119;
/*      */       case 106:
/* 3265 */         return 120;
/*      */       case 107:
/* 3267 */         return 121;
/*      */       case 108:
/* 3269 */         return 122;
/*      */       case 109:
/* 3271 */         return 123;
/*      */       case 110:
/* 3273 */         return 124;
/*      */       case 111:
/* 3275 */         return 125;
/*      */       case 112:
/* 3277 */         return 126;
/*      */       case 113:
/* 3279 */         return 127;
/*      */       case 163:
/* 3281 */         return 164;
/*      */     } 
/* 3283 */     return tileId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isBush(byte tileId) {
/* 3289 */     switch (tileId & 0xFF) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 31:
/*      */       case 34:
/*      */       case 35:
/*      */       case 142:
/*      */       case 143:
/*      */       case 144:
/*      */       case 145:
/*      */       case 146:
/*      */       case 147:
/*      */       case 148:
/*      */       case 149:
/*      */       case 150:
/*      */       case 151:
/*      */       case 152:
/*      */       case 153:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*      */       case 159:
/*      */       case 160:
/*      */       case 161:
/*      */       case 162:
/*      */       case 166:
/*      */       case 167:
/*      */       case 168:
/*      */       case 169:
/*      */       case 170:
/*      */       case 171:
/*      */       case 172:
/* 3324 */         return true;
/*      */     } 
/* 3326 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isGrassType(byte tileId) {
/* 3332 */     switch (tileId & 0xFF) {
/*      */       
/*      */       case 2:
/*      */       case 32:
/*      */       case 33:
/*      */       case 38:
/* 3338 */         return true;
/*      */     } 
/* 3340 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isTree(byte tileId) {
/* 3349 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 3:
/*      */       case 11:
/*      */       case 14:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 108:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 120:
/*      */       case 121:
/*      */       case 122:
/*      */       case 123:
/*      */       case 124:
/*      */       case 125:
/*      */       case 126:
/*      */       case 127:
/*      */       case 128:
/*      */       case 129:
/*      */       case 130:
/*      */       case 131:
/*      */       case 132:
/*      */       case 133:
/*      */       case 134:
/*      */       case 135:
/*      */       case 136:
/*      */       case 137:
/*      */       case 138:
/*      */       case 139:
/*      */       case 140:
/*      */       case 141:
/*      */       case 163:
/*      */       case 164:
/*      */       case 165:
/* 3399 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3405 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean canSpawnTree(byte tileId) {
/* 3413 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 2:
/*      */       case 5:
/*      */       case 10:
/*      */       case 19:
/*      */       case 22:
/* 3420 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3426 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isVisibleMineDoor(int tileId) {
/* 3434 */     switch (tileId & 0xFF)
/*      */     
/*      */     { case 25:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/* 3440 */         toReturn = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3446 */         return toReturn; }  boolean toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isVisibleMineDoor(byte tileId) {
/* 3451 */     return isVisibleMineDoor(tileId & 0xFF);
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
/*      */   public static boolean isVisibleMineDoor(Tile tile) {
/* 3464 */     return isVisibleMineDoor(tile.intId);
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
/*      */   public static long getBorderObjectId(int x, int y, int heightOffset, byte layer, int dir, byte type) {
/* 3497 */     int layerBit = (layer == 0) ? 0 : 128;
/* 3498 */     return (heightOffset << 48L) + (x << 32L) + (y << 16L) + ((layerBit + dir) << 8L) + (type & 0xFF);
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
/*      */   public static long getBorderId(int x, int y, int heightOffset, byte layer, int dir) {
/* 3511 */     return getBorderObjectId(x, y, heightOffset, layer, dir, (byte)12);
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
/*      */   public static long getHouseWallId(int x, int y, int heightOffset, byte layer, byte dir) {
/* 3524 */     return getBorderObjectId(x, y, heightOffset, layer, dir, (byte)5);
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
/*      */   public static long getFenceId(int x, int y, int heightOffset, byte layer, byte dir) {
/* 3539 */     return getBorderObjectId(x, y, heightOffset, layer, dir, (byte)7);
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
/*      */   public static long getFloorId(int x, int y, int heightOffset, byte layer) {
/* 3554 */     return getBorderObjectId(x, y, heightOffset, layer, 0, (byte)23);
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
/*      */   public static long getTileCornerId(int x, int y, int heightOffset, byte layer) {
/* 3569 */     return getBorderObjectId(x, y, heightOffset, layer, TileBorderDirection.CORNER.getCode(), (byte)27);
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
/*      */   public static long getBridgePartId(int x, int y, int realHeight, byte layer, byte dir) {
/* 3584 */     return getBorderObjectId(x, y, realHeight, layer, dir, (byte)28);
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
/*      */   public static byte encodeTreeData(FoliageAge foliageAge, boolean hasFruit, boolean centre, GrassData.GrowthTreeStage growthTreeStage) {
/* 3602 */     return encodeTreeData(foliageAge.getAgeId(), hasFruit, centre, growthTreeStage.getEncodedData());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte encodeTreeData(byte foliageAge, boolean hasFruit, boolean centre, GrassData.GrowthTreeStage growthTreeStage) {
/* 3608 */     return encodeTreeData(foliageAge, hasFruit, centre, growthTreeStage.getEncodedData());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte encodeTreeData(byte treeAge, boolean hasFruit, boolean centre, byte growthTreeStage) {
/* 3614 */     byte tileData = (byte)(treeAge << 4 | growthTreeStage);
/* 3615 */     if (hasFruit)
/* 3616 */       tileData = (byte)(tileData | 0x8); 
/* 3617 */     if (centre)
/* 3618 */       tileData = (byte)(tileData | 0x4); 
/* 3619 */     return tileData;
/*      */   }
/*      */ 
/*      */   
/*      */   private static long getTileId(int x, int y, int heightOffset, byte type) {
/* 3624 */     return (heightOffset << 48L) + (x << 32L) + (y << 16L) + type;
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
/*      */   public static long getTileId(int x, int y, int heightOffset) {
/* 3638 */     return getTileId(x, y, heightOffset, (byte)3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static long getTileId(int x, int y, int heightOffset, boolean onSurface) {
/* 3643 */     if (onSurface)
/* 3644 */       return getTileId(x, y, heightOffset, (byte)3); 
/* 3645 */     return getTileId(x, y, heightOffset, (byte)17);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\Tiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
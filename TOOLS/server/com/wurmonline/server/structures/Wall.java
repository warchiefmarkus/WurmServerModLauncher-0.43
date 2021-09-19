/*      */ package com.wurmonline.server.structures;
/*      */ 
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.math.Vector3f;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.MethodsStructure;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchTileException;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.StructureMaterialEnum;
/*      */ import com.wurmonline.shared.constants.StructureStateEnum;
/*      */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Wall
/*      */   implements MiscConstants, TimeConstants, Blocker, StructureSupport, Permissions.IAllow
/*      */ {
/*      */   public int x1;
/*      */   public int x2;
/*      */   public int y1;
/*      */   public int y2;
/*  109 */   private static final Vector3f normalHoriz = new Vector3f(0.0F, 1.0F, 0.0F);
/*  110 */   private static final Vector3f normalVertical = new Vector3f(1.0F, 0.0F, 0.0F);
/*      */   
/*      */   private Vector3f centerPoint;
/*  113 */   private static final Map<Long, Set<Wall>> walls = new HashMap<>();
/*      */   private static final String GETALLWALLS = "SELECT * FROM WALLS WHERE STARTX<ENDX OR STARTY<ENDY";
/*  115 */   Permissions permissions = new Permissions();
/*      */   
/*  117 */   private static final Set<Wall> rubbleWalls = new HashSet<>();
/*      */ 
/*      */ 
/*      */   
/*  121 */   public long structureId = -10L;
/*      */ 
/*      */ 
/*      */   
/*  125 */   int number = -10;
/*      */   
/*  127 */   private static final Logger logger = Logger.getLogger(Wall.class.getName());
/*      */ 
/*      */   
/*      */   public float originalQL;
/*      */ 
/*      */   
/*      */   public float currentQL;
/*      */   
/*      */   public float damage;
/*      */   
/*  137 */   public StructureTypeEnum type = StructureTypeEnum.SOLID;
/*      */   public int tilex;
/*      */   public int tiley;
/*  140 */   private int floorLevel = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  145 */   public int heightOffset = 0;
/*      */   
/*  147 */   byte layer = 0;
/*      */ 
/*      */   
/*      */   public long lastUsed;
/*      */   
/*  152 */   public StructureStateEnum state = StructureStateEnum.INITIALIZED;
/*  153 */   private StructureMaterialEnum material = StructureMaterialEnum.WOOD;
/*  154 */   int color = -1;
/*      */   
/*      */   boolean wallOrientationFlag = false;
/*      */   
/*      */   private static final String WOOD = "wood";
/*      */   private static final String STONE = "stone";
/*      */   private static final String TIMBER_FRAMED = "timber framed";
/*      */   private static final String PLAIN_STONE = "plain stone";
/*      */   private static final String SLATE = "slate";
/*      */   private static final String ROUNDED_STONE = "rounded stone";
/*      */   private static final String POTTERY = "pottery";
/*      */   private static final String SANDSTONE = "sandstone";
/*      */   private static final String RENDERED = "rendered";
/*      */   private static final String MARBLE = "marble";
/*  168 */   private static final int[] emptyArr = new int[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isIndoor = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Wall(StructureTypeEnum aType, int aTileX, int aTileY, int aStartX, int aStartY, int aEndX, int aEndY, float aQualityLevel, long aStructure, StructureMaterialEnum _material, boolean _isIndoor, int _heightOffset, int _layer) {
/*  181 */     this.structureId = aStructure;
/*  182 */     this.tilex = aTileX;
/*  183 */     this.tiley = aTileY;
/*  184 */     this.x1 = aStartX;
/*  185 */     this.y1 = aStartY;
/*  186 */     this.x2 = aEndX;
/*  187 */     this.y2 = aEndY;
/*  188 */     this.currentQL = aQualityLevel;
/*  189 */     this.originalQL = aQualityLevel;
/*  190 */     this.lastUsed = System.currentTimeMillis();
/*  191 */     this.type = aType;
/*  192 */     this.material = _material;
/*  193 */     this.isIndoor = _isIndoor;
/*  194 */     this.heightOffset = _heightOffset;
/*  195 */     setFloorLevel();
/*  196 */     this.layer = (byte)(_layer & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Wall(int wallid, StructureTypeEnum typ, int tx, int ty, int xs, int ys, int xe, int ye, float qualityLevel, float origQl, float dam, long structure, long last, StructureStateEnum stat, int col, StructureMaterialEnum _material, boolean _isIndoor, int _heightOffset, int _layer, boolean _wallOrientation, int aSettings) {
/*  204 */     this.number = wallid;
/*  205 */     this.type = typ;
/*  206 */     this.tilex = tx;
/*  207 */     this.tiley = ty;
/*  208 */     this.x1 = xs;
/*  209 */     this.y1 = ys;
/*  210 */     this.x2 = xe;
/*  211 */     this.y2 = ye;
/*  212 */     this.currentQL = qualityLevel;
/*  213 */     this.originalQL = origQl;
/*  214 */     this.damage = dam;
/*  215 */     this.structureId = structure;
/*  216 */     this.lastUsed = last;
/*  217 */     this.state = stat;
/*  218 */     this.color = col;
/*  219 */     this.material = _material;
/*  220 */     this.isIndoor = _isIndoor;
/*  221 */     this.heightOffset = _heightOffset;
/*  222 */     setFloorLevel();
/*  223 */     this.layer = (byte)(_layer & 0xFF);
/*  224 */     this.wallOrientationFlag = _wallOrientation;
/*  225 */     setSettings(aSettings);
/*      */   }
/*      */ 
/*      */   
/*      */   public Wall(int wallid, boolean load) throws IOException {
/*  230 */     this.number = wallid;
/*  231 */     if (load) {
/*  232 */       load();
/*      */     }
/*      */   }
/*      */   
/*      */   public final String getIdName() {
/*  237 */     if (this.material == StructureMaterialEnum.WOOD) {
/*      */       
/*  239 */       if (this.type == StructureTypeEnum.SOLID)
/*  240 */         return "wooden_wall"; 
/*  241 */       if (this.type == StructureTypeEnum.WINDOW)
/*  242 */         return "wooden_window"; 
/*  243 */       if (this.type == StructureTypeEnum.DOOR)
/*  244 */         return "wooden_door"; 
/*  245 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  246 */         return "wooden_double_door"; 
/*  247 */       if (this.type == StructureTypeEnum.ARCHED)
/*  248 */         return "wooden_arched"; 
/*  249 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  250 */         return "wooden_portcullis"; 
/*  251 */       if (this.type == StructureTypeEnum.CANOPY_DOOR)
/*  252 */         return "wooden_canopy_door"; 
/*  253 */       if (this.type == StructureTypeEnum.WIDE_WINDOW)
/*  254 */         return "wooden_wide_window"; 
/*  255 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  256 */         return "wooden_arched_left"; 
/*  257 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  258 */         return "wooden_arched_right"; 
/*  259 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  260 */         return "wooden_arched_t";
/*      */       }
/*  262 */     } else if (this.material == StructureMaterialEnum.STONE) {
/*      */       
/*  264 */       if (this.type == StructureTypeEnum.SOLID)
/*  265 */         return "stone_wall"; 
/*  266 */       if (this.type == StructureTypeEnum.WINDOW)
/*  267 */         return "stone_window"; 
/*  268 */       if (this.type == StructureTypeEnum.DOOR)
/*  269 */         return "sturdy_door"; 
/*  270 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  271 */         return "sturdy_double_door"; 
/*  272 */       if (this.type == StructureTypeEnum.ARCHED)
/*  273 */         return "study_arched"; 
/*  274 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  275 */         return "sturdy_portcullis"; 
/*  276 */       if (this.type == StructureTypeEnum.ORIEL)
/*  277 */         return "stone_oriel"; 
/*  278 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  279 */         return "sturdy_arched_left"; 
/*  280 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  281 */         return "sturdy_arched_right"; 
/*  282 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  283 */         return "sturdy_arched_t";
/*      */       }
/*  285 */     } else if (this.material == StructureMaterialEnum.PLAIN_STONE) {
/*      */       
/*  287 */       if (this.type == StructureTypeEnum.SOLID)
/*  288 */         return "plain_stone_wall"; 
/*  289 */       if (this.type == StructureTypeEnum.WINDOW)
/*  290 */         return "plain_stone_window"; 
/*  291 */       if (this.type == StructureTypeEnum.NARROW_WINDOW)
/*  292 */         return "plain_narrow_stone_window"; 
/*  293 */       if (this.type == StructureTypeEnum.DOOR)
/*  294 */         return "plain_stone_door"; 
/*  295 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  296 */         return "plain_stone_double_door"; 
/*  297 */       if (this.type == StructureTypeEnum.ARCHED)
/*  298 */         return "plain_stone_arched"; 
/*  299 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  300 */         return "plain_stone_portcullis"; 
/*  301 */       if (this.type == StructureTypeEnum.BARRED)
/*  302 */         return "plain_stone_barred_wall"; 
/*  303 */       if (this.type == StructureTypeEnum.ORIEL)
/*  304 */         return "plain_stone_oriel"; 
/*  305 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  306 */         return "plain_stone_arched_left"; 
/*  307 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  308 */         return "plain_stone_arched_right"; 
/*  309 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  310 */         return "plain_stone_arched_t";
/*      */       }
/*  312 */     } else if (this.material == StructureMaterialEnum.SLATE) {
/*      */       
/*  314 */       if (this.type == StructureTypeEnum.SOLID)
/*  315 */         return "slate_wall"; 
/*  316 */       if (this.type == StructureTypeEnum.WINDOW)
/*  317 */         return "slate_window"; 
/*  318 */       if (this.type == StructureTypeEnum.NARROW_WINDOW)
/*  319 */         return "narrow_slate_window"; 
/*  320 */       if (this.type == StructureTypeEnum.DOOR)
/*  321 */         return "slate_door"; 
/*  322 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  323 */         return "slate_double_door"; 
/*  324 */       if (this.type == StructureTypeEnum.ARCHED)
/*  325 */         return "slate_arched"; 
/*  326 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  327 */         return "slate_portcullis"; 
/*  328 */       if (this.type == StructureTypeEnum.BARRED)
/*  329 */         return "slate_barred_wall"; 
/*  330 */       if (this.type == StructureTypeEnum.ORIEL)
/*  331 */         return "slate_oriel"; 
/*  332 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  333 */         return "slate_arched_left"; 
/*  334 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  335 */         return "slate_arched_right"; 
/*  336 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  337 */         return "slate_arched_t";
/*      */       }
/*  339 */     } else if (this.material == StructureMaterialEnum.ROUNDED_STONE) {
/*      */       
/*  341 */       if (this.type == StructureTypeEnum.SOLID)
/*  342 */         return "rounded_stone_wall"; 
/*  343 */       if (this.type == StructureTypeEnum.WINDOW)
/*  344 */         return "rounded_stone_window"; 
/*  345 */       if (this.type == StructureTypeEnum.NARROW_WINDOW)
/*  346 */         return "narrow_rounded_stone_window"; 
/*  347 */       if (this.type == StructureTypeEnum.DOOR)
/*  348 */         return "rounded_stone_door"; 
/*  349 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  350 */         return "rounded_stone_double_door"; 
/*  351 */       if (this.type == StructureTypeEnum.ARCHED)
/*  352 */         return "rounded_stone_arched"; 
/*  353 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  354 */         return "rounded_stone_portcullis"; 
/*  355 */       if (this.type == StructureTypeEnum.BARRED)
/*  356 */         return "rounded_stone_barred_wall"; 
/*  357 */       if (this.type == StructureTypeEnum.ORIEL)
/*  358 */         return "rounded_stone_oriel"; 
/*  359 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  360 */         return "rounded_stone_arched_left"; 
/*  361 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  362 */         return "rounded_stone_arched_right"; 
/*  363 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  364 */         return "rounded_stone_arched_t";
/*      */       }
/*  366 */     } else if (this.material == StructureMaterialEnum.POTTERY) {
/*      */       
/*  368 */       if (this.type == StructureTypeEnum.SOLID)
/*  369 */         return "pottery_brick_wall"; 
/*  370 */       if (this.type == StructureTypeEnum.WINDOW)
/*  371 */         return "pottery_brick_window"; 
/*  372 */       if (this.type == StructureTypeEnum.NARROW_WINDOW)
/*  373 */         return "narrow_pottery_brick_window"; 
/*  374 */       if (this.type == StructureTypeEnum.DOOR)
/*  375 */         return "pottery_brick_door"; 
/*  376 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  377 */         return "pottery_brick_double_door"; 
/*  378 */       if (this.type == StructureTypeEnum.ARCHED)
/*  379 */         return "pottery_brick_arched"; 
/*  380 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  381 */         return "pottery_brick_portcullis"; 
/*  382 */       if (this.type == StructureTypeEnum.BARRED)
/*  383 */         return "pottery_brick_barred_wall"; 
/*  384 */       if (this.type == StructureTypeEnum.ORIEL)
/*  385 */         return "pottery_brick_oriel"; 
/*  386 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  387 */         return "pottery_brick_arched_left"; 
/*  388 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  389 */         return "pottery_brick_arched_right"; 
/*  390 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  391 */         return "pottery_brick_arched_t";
/*      */       }
/*  393 */     } else if (this.material == StructureMaterialEnum.SANDSTONE) {
/*      */       
/*  395 */       if (this.type == StructureTypeEnum.SOLID)
/*  396 */         return "sandstone_wall"; 
/*  397 */       if (this.type == StructureTypeEnum.WINDOW)
/*  398 */         return "sandstone_window"; 
/*  399 */       if (this.type == StructureTypeEnum.NARROW_WINDOW)
/*  400 */         return "narrow_sandstone_window"; 
/*  401 */       if (this.type == StructureTypeEnum.DOOR)
/*  402 */         return "sandstone_door"; 
/*  403 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  404 */         return "sandstone_double_door"; 
/*  405 */       if (this.type == StructureTypeEnum.ARCHED)
/*  406 */         return "sandstone_arched"; 
/*  407 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  408 */         return "sandstone_portcullis"; 
/*  409 */       if (this.type == StructureTypeEnum.BARRED)
/*  410 */         return "sandstone_barred_wall"; 
/*  411 */       if (this.type == StructureTypeEnum.ORIEL)
/*  412 */         return "sandstone_oriel"; 
/*  413 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  414 */         return "sandstone_arched_left"; 
/*  415 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  416 */         return "sandstone_arched_right"; 
/*  417 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  418 */         return "sandstone_arched_t";
/*      */       }
/*  420 */     } else if (this.material == StructureMaterialEnum.RENDERED) {
/*      */       
/*  422 */       if (this.type == StructureTypeEnum.SOLID)
/*  423 */         return "rendered_wall"; 
/*  424 */       if (this.type == StructureTypeEnum.WINDOW)
/*  425 */         return "rendered_window"; 
/*  426 */       if (this.type == StructureTypeEnum.NARROW_WINDOW)
/*  427 */         return "narrow_rendered_window"; 
/*  428 */       if (this.type == StructureTypeEnum.DOOR)
/*  429 */         return "rendered_door"; 
/*  430 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  431 */         return "rendered_double_door"; 
/*  432 */       if (this.type == StructureTypeEnum.ARCHED)
/*  433 */         return "rendered_arched"; 
/*  434 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  435 */         return "rendered_portcullis"; 
/*  436 */       if (this.type == StructureTypeEnum.BARRED)
/*  437 */         return "rendered_barred_wall"; 
/*  438 */       if (this.type == StructureTypeEnum.ORIEL)
/*  439 */         return "rendered_oriel"; 
/*  440 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  441 */         return "rendered_arched_left"; 
/*  442 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  443 */         return "rendered_arched_right"; 
/*  444 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  445 */         return "rendered_arched_t";
/*      */       }
/*  447 */     } else if (this.material == StructureMaterialEnum.MARBLE) {
/*      */       
/*  449 */       if (this.type == StructureTypeEnum.SOLID)
/*  450 */         return "marble_wall"; 
/*  451 */       if (this.type == StructureTypeEnum.WINDOW)
/*  452 */         return "marble_window"; 
/*  453 */       if (this.type == StructureTypeEnum.NARROW_WINDOW)
/*  454 */         return "narrow_marble_window"; 
/*  455 */       if (this.type == StructureTypeEnum.DOOR)
/*  456 */         return "marble_door"; 
/*  457 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  458 */         return "marble_double_door"; 
/*  459 */       if (this.type == StructureTypeEnum.ARCHED)
/*  460 */         return "marble_arched"; 
/*  461 */       if (this.type == StructureTypeEnum.PORTCULLIS)
/*  462 */         return "marble_portcullis"; 
/*  463 */       if (this.type == StructureTypeEnum.BARRED)
/*  464 */         return "marble_barred_wall"; 
/*  465 */       if (this.type == StructureTypeEnum.ORIEL)
/*  466 */         return "marble_oriel"; 
/*  467 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  468 */         return "marble_arched_left"; 
/*  469 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  470 */         return "marble_arched_right"; 
/*  471 */       if (this.type == StructureTypeEnum.ARCHED_T) {
/*  472 */         return "marble_arched_t";
/*      */       }
/*  474 */     } else if (this.material == StructureMaterialEnum.TIMBER_FRAMED) {
/*      */       
/*  476 */       if (this.type == StructureTypeEnum.SOLID)
/*  477 */         return "timber_framed_wall"; 
/*  478 */       if (this.type == StructureTypeEnum.WINDOW)
/*  479 */         return "timber_framed_window"; 
/*  480 */       if (this.type == StructureTypeEnum.DOOR)
/*  481 */         return "timber_framed_door"; 
/*  482 */       if (this.type == StructureTypeEnum.DOUBLE_DOOR)
/*  483 */         return "timber_framed_double_door"; 
/*  484 */       if (this.type == StructureTypeEnum.ARCHED)
/*  485 */         return "timber_framed_arched"; 
/*  486 */       if (this.type == StructureTypeEnum.BALCONY)
/*  487 */         return "timber_framed_balcony"; 
/*  488 */       if (this.type == StructureTypeEnum.JETTY)
/*  489 */         return "timber_framed_jetty"; 
/*  490 */       if (this.type == StructureTypeEnum.ARCHED_LEFT)
/*  491 */         return "timber_framed_arched_left"; 
/*  492 */       if (this.type == StructureTypeEnum.ARCHED_RIGHT)
/*  493 */         return "timber_framed_arched_right"; 
/*  494 */       if (this.type == StructureTypeEnum.ARCHED_T)
/*  495 */         return "timber_framed_arched_t"; 
/*      */     } 
/*  497 */     if (this.type == StructureTypeEnum.PLAN)
/*  498 */       return "wall_plan"; 
/*  499 */     return "unknown_wall";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/*  505 */     return getName(this.type, this.material);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getName(StructureTypeEnum type, StructureMaterialEnum material) {
/*  512 */     if (type == StructureTypeEnum.RUBBLE)
/*  513 */       return "pile of debris"; 
/*  514 */     if (material == StructureMaterialEnum.WOOD) {
/*      */       
/*  516 */       if (type == StructureTypeEnum.SOLID)
/*  517 */         return "wooden wall"; 
/*  518 */       if (type == StructureTypeEnum.WINDOW)
/*  519 */         return "wooden window"; 
/*  520 */       if (type == StructureTypeEnum.DOOR)
/*  521 */         return "wooden door"; 
/*  522 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  523 */         return "wooden double door"; 
/*  524 */       if (type == StructureTypeEnum.ARCHED)
/*  525 */         return "wooden arched wall"; 
/*  526 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  527 */         return "wooden portcullis"; 
/*  528 */       if (type == StructureTypeEnum.CANOPY_DOOR)
/*  529 */         return "wooden canopy door"; 
/*  530 */       if (type == StructureTypeEnum.WIDE_WINDOW)
/*  531 */         return "wooden wide window"; 
/*  532 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  533 */         return "wooden left arch"; 
/*  534 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  535 */         return "wooden right arch"; 
/*  536 */       if (type == StructureTypeEnum.ARCHED_T)
/*  537 */         return "wooden T arch"; 
/*  538 */       if (type == StructureTypeEnum.SCAFFOLDING) {
/*  539 */         return "wooden scaffolding";
/*      */       }
/*  541 */     } else if (material == StructureMaterialEnum.STONE) {
/*      */       
/*  543 */       if (type == StructureTypeEnum.SOLID)
/*  544 */         return "stone wall"; 
/*  545 */       if (type == StructureTypeEnum.WINDOW)
/*  546 */         return "stone window"; 
/*  547 */       if (type == StructureTypeEnum.DOOR)
/*  548 */         return "sturdy door"; 
/*  549 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  550 */         return "sturdy double door"; 
/*  551 */       if (type == StructureTypeEnum.ARCHED)
/*  552 */         return "sturdy arched wall"; 
/*  553 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  554 */         return "sturdy portcullis"; 
/*  555 */       if (type == StructureTypeEnum.ORIEL)
/*  556 */         return "stone oriel"; 
/*  557 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  558 */         return "sturdy left arch"; 
/*  559 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  560 */         return "sturdy right arch"; 
/*  561 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  562 */         return "sturdy T arch";
/*      */       }
/*  564 */     } else if (material == StructureMaterialEnum.PLAIN_STONE) {
/*      */       
/*  566 */       if (type == StructureTypeEnum.SOLID)
/*  567 */         return "plain stone wall"; 
/*  568 */       if (type == StructureTypeEnum.WINDOW)
/*  569 */         return "plain stone window"; 
/*  570 */       if (type == StructureTypeEnum.NARROW_WINDOW)
/*  571 */         return "plain narrow stone window"; 
/*  572 */       if (type == StructureTypeEnum.DOOR)
/*  573 */         return "plain stone door"; 
/*  574 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  575 */         return "plain stone double door"; 
/*  576 */       if (type == StructureTypeEnum.ARCHED)
/*  577 */         return "plain stone arched wall"; 
/*  578 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  579 */         return "plain stone portcullis"; 
/*  580 */       if (type == StructureTypeEnum.BARRED)
/*  581 */         return "plain stone barred wall"; 
/*  582 */       if (type == StructureTypeEnum.ORIEL)
/*  583 */         return "plain stone oriel"; 
/*  584 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  585 */         return "plain stone left arch"; 
/*  586 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  587 */         return "plain stone right arch"; 
/*  588 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  589 */         return "plain stone T arch";
/*      */       }
/*  591 */     } else if (material == StructureMaterialEnum.SLATE) {
/*      */       
/*  593 */       if (type == StructureTypeEnum.SOLID)
/*  594 */         return "slate wall"; 
/*  595 */       if (type == StructureTypeEnum.WINDOW)
/*  596 */         return "slate window"; 
/*  597 */       if (type == StructureTypeEnum.NARROW_WINDOW)
/*  598 */         return "narrow slate window"; 
/*  599 */       if (type == StructureTypeEnum.DOOR)
/*  600 */         return "slate door"; 
/*  601 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  602 */         return "slate double door"; 
/*  603 */       if (type == StructureTypeEnum.ARCHED)
/*  604 */         return "slate arched"; 
/*  605 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  606 */         return "slate portcullis"; 
/*  607 */       if (type == StructureTypeEnum.BARRED)
/*  608 */         return "slate barred wall"; 
/*  609 */       if (type == StructureTypeEnum.ORIEL)
/*  610 */         return "slate oriel"; 
/*  611 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  612 */         return "slate left arch"; 
/*  613 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  614 */         return "slate right arch"; 
/*  615 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  616 */         return "slate T arch";
/*      */       }
/*  618 */     } else if (material == StructureMaterialEnum.ROUNDED_STONE) {
/*      */       
/*  620 */       if (type == StructureTypeEnum.SOLID)
/*  621 */         return "rounded stone wall"; 
/*  622 */       if (type == StructureTypeEnum.WINDOW)
/*  623 */         return "rounded stone window"; 
/*  624 */       if (type == StructureTypeEnum.NARROW_WINDOW)
/*  625 */         return "narrow rounded stone window"; 
/*  626 */       if (type == StructureTypeEnum.DOOR)
/*  627 */         return "rounded stone door"; 
/*  628 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  629 */         return "rounded stone double door"; 
/*  630 */       if (type == StructureTypeEnum.ARCHED)
/*  631 */         return "rounded stone arched"; 
/*  632 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  633 */         return "rounded stone portcullis"; 
/*  634 */       if (type == StructureTypeEnum.BARRED)
/*  635 */         return "rounded stone barred wall"; 
/*  636 */       if (type == StructureTypeEnum.ORIEL)
/*  637 */         return "rounded stone oriel"; 
/*  638 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  639 */         return "rounded stone left arch"; 
/*  640 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  641 */         return "rounded stone right arch"; 
/*  642 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  643 */         return "rounded stone T arch";
/*      */       }
/*  645 */     } else if (material == StructureMaterialEnum.POTTERY) {
/*      */       
/*  647 */       if (type == StructureTypeEnum.SOLID)
/*  648 */         return "pottery brick wall"; 
/*  649 */       if (type == StructureTypeEnum.WINDOW)
/*  650 */         return "pottery brick window"; 
/*  651 */       if (type == StructureTypeEnum.NARROW_WINDOW)
/*  652 */         return "narrow pottery brick window"; 
/*  653 */       if (type == StructureTypeEnum.DOOR)
/*  654 */         return "pottery brick door"; 
/*  655 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  656 */         return "pottery brick double door"; 
/*  657 */       if (type == StructureTypeEnum.ARCHED)
/*  658 */         return "pottery brick arched"; 
/*  659 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  660 */         return "pottery brick portcullis"; 
/*  661 */       if (type == StructureTypeEnum.BARRED)
/*  662 */         return "pottery brick barred wall"; 
/*  663 */       if (type == StructureTypeEnum.ORIEL)
/*  664 */         return "pottery brick oriel"; 
/*  665 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  666 */         return "pottery brick left arch"; 
/*  667 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  668 */         return "pottery brick right arch"; 
/*  669 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  670 */         return "pottery brick T arch";
/*      */       }
/*  672 */     } else if (material == StructureMaterialEnum.SANDSTONE) {
/*      */       
/*  674 */       if (type == StructureTypeEnum.SOLID)
/*  675 */         return "sandstone wall"; 
/*  676 */       if (type == StructureTypeEnum.WINDOW)
/*  677 */         return "sandstone window"; 
/*  678 */       if (type == StructureTypeEnum.NARROW_WINDOW)
/*  679 */         return "narrow sandstone window"; 
/*  680 */       if (type == StructureTypeEnum.DOOR)
/*  681 */         return "sandstone door"; 
/*  682 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  683 */         return "sandstone double door"; 
/*  684 */       if (type == StructureTypeEnum.ARCHED)
/*  685 */         return "sandstone arched"; 
/*  686 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  687 */         return "sandstone portcullis"; 
/*  688 */       if (type == StructureTypeEnum.BARRED)
/*  689 */         return "sandstone barred wall"; 
/*  690 */       if (type == StructureTypeEnum.ORIEL)
/*  691 */         return "sandstone oriel"; 
/*  692 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  693 */         return "sandstone left arch"; 
/*  694 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  695 */         return "sandstone right arch"; 
/*  696 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  697 */         return "sandstone T arch";
/*      */       }
/*  699 */     } else if (material == StructureMaterialEnum.RENDERED) {
/*      */       
/*  701 */       if (type == StructureTypeEnum.SOLID)
/*  702 */         return "rendered wall"; 
/*  703 */       if (type == StructureTypeEnum.WINDOW)
/*  704 */         return "rendered window"; 
/*  705 */       if (type == StructureTypeEnum.NARROW_WINDOW)
/*  706 */         return "narrow rendered window"; 
/*  707 */       if (type == StructureTypeEnum.DOOR)
/*  708 */         return "rendered door"; 
/*  709 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  710 */         return "rendered double door"; 
/*  711 */       if (type == StructureTypeEnum.ARCHED)
/*  712 */         return "rendered arched"; 
/*  713 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  714 */         return "rendered portcullis"; 
/*  715 */       if (type == StructureTypeEnum.BARRED)
/*  716 */         return "rendered barred wall"; 
/*  717 */       if (type == StructureTypeEnum.ORIEL)
/*  718 */         return "rendered oriel"; 
/*  719 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  720 */         return "rendered left arch"; 
/*  721 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  722 */         return "rendered right arch"; 
/*  723 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  724 */         return "rendered T arch";
/*      */       }
/*  726 */     } else if (material == StructureMaterialEnum.MARBLE) {
/*      */       
/*  728 */       if (type == StructureTypeEnum.SOLID)
/*  729 */         return "marble wall"; 
/*  730 */       if (type == StructureTypeEnum.WINDOW)
/*  731 */         return "marble window"; 
/*  732 */       if (type == StructureTypeEnum.NARROW_WINDOW)
/*  733 */         return "narrow marble window"; 
/*  734 */       if (type == StructureTypeEnum.DOOR)
/*  735 */         return "marble door"; 
/*  736 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  737 */         return "marble double door"; 
/*  738 */       if (type == StructureTypeEnum.ARCHED)
/*  739 */         return "marble arched"; 
/*  740 */       if (type == StructureTypeEnum.PORTCULLIS)
/*  741 */         return "marble portcullis"; 
/*  742 */       if (type == StructureTypeEnum.BARRED)
/*  743 */         return "marble barred wall"; 
/*  744 */       if (type == StructureTypeEnum.ORIEL)
/*  745 */         return "marble oriel"; 
/*  746 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  747 */         return "marble left arch"; 
/*  748 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  749 */         return "marble right arch"; 
/*  750 */       if (type == StructureTypeEnum.ARCHED_T) {
/*  751 */         return "marble T arch";
/*      */       }
/*  753 */     } else if (material == StructureMaterialEnum.TIMBER_FRAMED) {
/*      */       
/*  755 */       if (type == StructureTypeEnum.SOLID)
/*  756 */         return "timber framed wall"; 
/*  757 */       if (type == StructureTypeEnum.WINDOW)
/*  758 */         return "timber framed window"; 
/*  759 */       if (type == StructureTypeEnum.DOOR)
/*  760 */         return "timber framed door"; 
/*  761 */       if (type == StructureTypeEnum.DOUBLE_DOOR)
/*  762 */         return "timber framed double door"; 
/*  763 */       if (type == StructureTypeEnum.ARCHED)
/*  764 */         return "timber framed arched wall"; 
/*  765 */       if (type == StructureTypeEnum.JETTY)
/*  766 */         return "timber framed jetty"; 
/*  767 */       if (type == StructureTypeEnum.BALCONY)
/*  768 */         return "timber framed balcony"; 
/*  769 */       if (type == StructureTypeEnum.ARCHED_LEFT)
/*  770 */         return "timber framed left arch"; 
/*  771 */       if (type == StructureTypeEnum.ARCHED_RIGHT)
/*  772 */         return "timber framed right arch"; 
/*  773 */       if (type == StructureTypeEnum.ARCHED_T)
/*  774 */         return "timber framed T arch"; 
/*      */     } 
/*  776 */     if (type == StructureTypeEnum.PLAN)
/*  777 */       return "wall plan"; 
/*  778 */     return "unknown wall";
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getMaterialName(StructureMaterialEnum material) {
/*  783 */     if (material == StructureMaterialEnum.WOOD)
/*  784 */       return "Wooden"; 
/*  785 */     if (material == StructureMaterialEnum.STONE)
/*  786 */       return "Stone brick"; 
/*  787 */     if (material == StructureMaterialEnum.PLAIN_STONE)
/*  788 */       return "Plain stone"; 
/*  789 */     if (material == StructureMaterialEnum.SLATE)
/*  790 */       return "Slate"; 
/*  791 */     if (material == StructureMaterialEnum.ROUNDED_STONE)
/*  792 */       return "Rounded stone"; 
/*  793 */     if (material == StructureMaterialEnum.POTTERY)
/*  794 */       return "Pottery"; 
/*  795 */     if (material == StructureMaterialEnum.SANDSTONE)
/*  796 */       return "Sandstone"; 
/*  797 */     if (material == StructureMaterialEnum.MARBLE)
/*  798 */       return "Marble"; 
/*  799 */     if (material == StructureMaterialEnum.TIMBER_FRAMED)
/*  800 */       return "Timber framed"; 
/*  801 */     return "unknown";
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Wall[] getRubbleWalls() {
/*  806 */     return rubbleWalls.<Wall>toArray(new Wall[rubbleWalls.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final void addRubble(Wall wall) {
/*  811 */     rubbleWalls.add(wall);
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final void removeRubble(Wall wall) {
/*  816 */     rubbleWalls.remove(wall);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFence() {
/*  825 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWall() {
/*  834 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFloor() {
/*  843 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isRoof() {
/*  849 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStair() {
/*  855 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTile() {
/*  861 */     return false;
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
/*      */   public final boolean isDoor() {
/*  873 */     return (this.type == StructureTypeEnum.DOOR || this.type == StructureTypeEnum.DOUBLE_DOOR || this.type == StructureTypeEnum.PORTCULLIS || this.type == StructureTypeEnum.CANOPY_DOOR || 
/*      */ 
/*      */ 
/*      */       
/*  877 */       isArched(this.type));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getNormal() {
/*  883 */     if (isHorizontal())
/*  884 */       return normalHoriz; 
/*  885 */     return normalVertical;
/*      */   }
/*      */ 
/*      */   
/*      */   private final Vector3f calculateCenterPoint() {
/*  890 */     int sx = Math.min(this.x1, this.x2);
/*  891 */     int sy = Math.min(this.y1, this.y2);
/*      */     
/*  893 */     return new Vector3f(isHorizontal() ? (sx * 4 + 2) : (sx * 4), isHorizontal() ? (sy * 4) : (sy * 4 + 2), getMinZ() + 1.5F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Vector3f getCenterPoint() {
/*  900 */     if (this.centerPoint == null)
/*  901 */       this.centerPoint = calculateCenterPoint(); 
/*  902 */     return this.centerPoint;
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
/*      */   public final Vector3f isBlocking(Creature creature, Vector3f startPos, Vector3f endPos, Vector3f normal, int blockType, long target, boolean followGround) {
/*  935 */     if (target == getId())
/*  936 */       return null; 
/*  937 */     if (this.type == StructureTypeEnum.PLAN || this.type == StructureTypeEnum.RUBBLE || isArched(this.type)) {
/*  938 */       return null;
/*      */     }
/*  940 */     if (blockType == 5 && (isWindow() || isBalcony() || isJetty() || isOriel()))
/*  941 */       return null; 
/*  942 */     if (!isFinished())
/*  943 */       return null; 
/*  944 */     if (blockType == 6 || blockType == 8)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  954 */       if (isDoor())
/*      */       {
/*  956 */         if (getDoor() != null) {
/*      */           
/*  958 */           if (getDoor().canBeOpenedBy(creature, true))
/*      */           {
/*  960 */             return null;
/*      */           }
/*  962 */           return getIntersectionPoint(startPos, endPos, normal, creature, blockType, followGround);
/*      */         } 
/*      */       }
/*      */     }
/*  966 */     return getIntersectionPoint(startPos, endPos, normal, creature, blockType, followGround);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeOpenedBy(Creature creature, boolean wentThroughDoor) {
/*  972 */     if (this.type == StructureTypeEnum.PLAN || isArched())
/*  973 */       return true; 
/*  974 */     if (!isFinished())
/*  975 */       return true; 
/*  976 */     if (isDoor())
/*      */     {
/*  978 */       if (getDoor() != null)
/*      */       {
/*  980 */         if (getDoor().canBeOpenedBy(creature, true))
/*      */         {
/*      */           
/*  983 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  987 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBlockPercent(Creature creature) {
/*  993 */     if (this.type == StructureTypeEnum.RUBBLE)
/*  994 */       return 0.0F; 
/*  995 */     if (isFinished()) {
/*      */       
/*  997 */       if (isWindow() || isJetty())
/*  998 */         return 70.0F; 
/*  999 */       if (isOriel())
/* 1000 */         return 80.0F; 
/* 1001 */       if (isBalcony()) {
/* 1002 */         return 10.0F;
/*      */       }
/* 1004 */       return 100.0F;
/*      */     } 
/* 1006 */     return Math.max(0, (getState()).state);
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
/*      */   public final Vector3f getIntersectionPoint(Vector3f startPos, Vector3f endPos, Vector3f normal, Creature creature, int blockType, boolean followGround) {
/* 1052 */     Vector3f spcopy = startPos.clone();
/* 1053 */     Vector3f epcopy = endPos.clone();
/* 1054 */     if (getFloorLevel() == 0)
/*      */     {
/*      */       
/* 1057 */       if (followGround || spcopy.z <= getMinZ()) {
/*      */         
/* 1059 */         spcopy.z = getMinZ() + 1.75F;
/* 1060 */         if (followGround)
/* 1061 */           epcopy.z = getMinZ() + 0.5F; 
/*      */       } 
/*      */     }
/* 1064 */     Vector3f diff = getCenterPoint().subtract(spcopy);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1070 */     Vector3f diffend = epcopy.subtract(spcopy);
/*      */     
/* 1072 */     if (isHorizontal()) {
/*      */       
/* 1074 */       float steps = diff.y / normal.y;
/* 1075 */       Vector3f intersection = spcopy.add(normal.mult(steps));
/* 1076 */       Vector3f interDiff = intersection.subtract(spcopy);
/*      */       
/* 1078 */       if (diffend.length() + 0.01F < interDiff.length())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1088 */         return null;
/*      */       }
/* 1090 */       if (isWithinBounds(intersection, followGround))
/*      */       {
/*      */         
/* 1093 */         float u = getNormal().dot(getCenterPoint().subtract(startPos)) / getNormal().dot(epcopy.subtract(spcopy));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1111 */         if (u >= 0.0F && u <= 1.0F)
/*      */         {
/* 1113 */           return intersection;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1119 */         return null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1141 */       float steps = diff.x / normal.x;
/* 1142 */       Vector3f intersection = spcopy.add(normal.mult(steps));
/* 1143 */       Vector3f interDiff = intersection.subtract(spcopy);
/* 1144 */       if (diffend.length() < interDiff.length())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1153 */         return null;
/*      */       }
/* 1155 */       if (isWithinBounds(intersection, followGround)) {
/*      */ 
/*      */         
/* 1158 */         float u = getNormal().dot(getCenterPoint().subtract(spcopy)) / getNormal().dot(epcopy.subtract(spcopy));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1177 */         if (u >= 0.0F && u <= 1.0F)
/*      */         {
/* 1179 */           return intersection;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1185 */         return null;
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1212 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean isWithinBounds(Vector3f pointToCheck, boolean followGround) {
/* 1217 */     if (isHorizontal()) {
/*      */       
/* 1219 */       if (pointToCheck.getY() >= (this.y1 * 4) - 0.1F && pointToCheck
/* 1220 */         .getY() <= (this.y2 * 4) + 0.1F)
/*      */       {
/* 1222 */         if (pointToCheck.getX() >= (Math.min(this.x1, this.x2) * 4) && pointToCheck.getX() <= (Math.max(this.x2, this.x1) * 4) && ((
/* 1223 */           followGround && getFloorLevel() == 0) || (pointToCheck
/* 1224 */           .getZ() >= getMinZ() && pointToCheck.getZ() <= getMaxZ()))) {
/* 1225 */           return true;
/*      */         
/*      */         }
/*      */       }
/*      */     }
/* 1230 */     else if (pointToCheck.getX() >= (this.x1 * 4) - 0.1F && pointToCheck
/* 1231 */       .getX() <= (this.x2 * 4) + 0.1F) {
/*      */       
/* 1233 */       if (pointToCheck.getY() >= (Math.min(this.y1, this.y2) * 4) && pointToCheck.getY() <= (Math.max(this.y2, this.y1) * 4) && ((
/* 1234 */         followGround && getFloorLevel() == 0) || (pointToCheck
/* 1235 */         .getZ() >= getMinZ() && pointToCheck.getZ() <= getMaxZ()))) {
/* 1236 */         return true;
/*      */       }
/*      */     } 
/* 1239 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTileX() {
/* 1245 */     return this.tilex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getColor() {
/* 1255 */     return this.color;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean getWallOrientationFlag() {
/* 1260 */     return this.wallOrientationFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getNumber() {
/* 1269 */     return this.number;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTileY() {
/* 1275 */     return this.tiley;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Tiles.TileBorderDirection getDir() {
/* 1280 */     if (isHorizontal()) {
/* 1281 */       return Tiles.TileBorderDirection.DIR_HORIZ;
/*      */     }
/* 1283 */     return Tiles.TileBorderDirection.DIR_DOWN;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isHorizontal() {
/* 1289 */     return (this.y1 == this.y2);
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getCurrentQualityLevel() {
/* 1294 */     return this.currentQL * Math.max(1.0F, 100.0F - this.damage) / 100.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOnPvPServer() {
/* 1299 */     if (isHorizontal()) {
/*      */       
/* 1301 */       if (Zones.isOnPvPServer(this.x1, this.y1))
/* 1302 */         return true; 
/* 1303 */       if (Zones.isOnPvPServer(this.x1, this.y1 - 1)) {
/* 1304 */         return true;
/*      */       }
/*      */     } else {
/*      */       
/* 1308 */       if (Zones.isOnPvPServer(this.x1, this.y1))
/* 1309 */         return true; 
/* 1310 */       if (Zones.isOnPvPServer(this.x1 - 1, this.y1))
/* 1311 */         return true; 
/*      */     } 
/* 1313 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getOriginalQualityLevel() {
/* 1318 */     return this.originalQL;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartX() {
/* 1324 */     return this.x1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartY() {
/* 1330 */     return this.y1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinX() {
/* 1336 */     return Math.min(this.x1, this.x2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMinY() {
/* 1342 */     return Math.min(this.y1, this.y2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndX() {
/* 1348 */     return this.x2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getEndY() {
/* 1354 */     return this.y2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionX() {
/* 1360 */     return (this.x1 * 4 + this.x2 * 4) / 2.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getPositionY() {
/* 1366 */     return (this.y1 * 4 + this.y2 * 4) / 2.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setStructureId(long structure) {
/* 1375 */     this.structureId = structure;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getStructureId() {
/* 1384 */     return this.structureId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getOLDId() {
/* 1392 */     if (this.y1 == this.y2) {
/*      */       
/* 1394 */       if (this.x1 < this.x2)
/*      */       {
/* 1396 */         return 0L + (this.x1 << 32L) + (this.y1 << 16) + 5L;
/*      */       }
/* 1398 */       if (this.x1 > this.x2)
/*      */       {
/* 1400 */         return 72057594037927936L + (this.x2 << 32L) + (this.y1 << 16) + 5L;
/*      */       }
/*      */       
/* 1403 */       throw new IllegalStateException("Found a broken wall.");
/*      */     } 
/* 1405 */     if (this.x1 == this.x2) {
/*      */       
/* 1407 */       if (this.y1 < this.y2)
/*      */       {
/* 1409 */         return 72339069014638592L + (this.x1 << 32L) + (this.y1 << 16) + 5L;
/*      */       }
/* 1411 */       if (this.y1 > this.y2)
/*      */       {
/* 1413 */         return 281474976710656L + (this.x2 << 32L) + (this.y2 << 16) + 5L;
/*      */       }
/*      */       
/* 1416 */       throw new IllegalStateException("Found a broken wall.");
/*      */     } 
/*      */     
/* 1419 */     throw new IllegalStateException("Found a broken wall.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean equals(StructureSupport support) {
/* 1425 */     return (support.getId() == getId());
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
/*      */   public final long getId() {
/* 1438 */     if (this.y1 == this.y2) {
/*      */       
/* 1440 */       if (this.x1 < this.x2)
/*      */       {
/* 1442 */         return Tiles.getHouseWallId(this.x1, this.y1, this.heightOffset, getLayer(), (byte)0);
/*      */       }
/*      */ 
/*      */       
/* 1446 */       if (this.x1 > this.x2)
/*      */       {
/* 1448 */         return Tiles.getHouseWallId(this.x2, this.y1, this.heightOffset, getLayer(), (byte)0);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1453 */       throw new IllegalStateException("Found a broken wall.");
/*      */     } 
/* 1455 */     if (this.x1 == this.x2) {
/*      */       
/* 1457 */       if (this.y1 < this.y2)
/*      */       {
/* 1459 */         return Tiles.getHouseWallId(this.x1, this.y1, this.heightOffset, getLayer(), (byte)1);
/*      */       }
/*      */ 
/*      */       
/* 1463 */       if (this.y1 > this.y2)
/*      */       {
/* 1465 */         return Tiles.getHouseWallId(this.x2, this.y2, this.heightOffset, getLayer(), (byte)1);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1470 */       throw new IllegalStateException("Found a broken wall.");
/*      */     } 
/*      */     
/* 1473 */     throw new IllegalStateException("Found a broken wall.");
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
/*      */   public final void setType(StructureTypeEnum aType) {
/* 1496 */     this.type = aType;
/* 1497 */     this.lastUsed = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final StructureTypeEnum getType() {
/* 1506 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isArched() {
/* 1511 */     return isArched(this.type);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isLRArch() {
/* 1516 */     switch (this.type) {
/*      */       
/*      */       case ARCHED_LEFT:
/*      */       case ARCHED_RIGHT:
/* 1520 */         return true;
/*      */     } 
/* 1522 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isHalfArch() {
/* 1527 */     return isHalfArch(this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final StructureStateEnum getState() {
/* 1536 */     return this.state;
/*      */   }
/*      */ 
/*      */   
/*      */   public final StructureStateEnum getNeeded() {
/* 1541 */     int needed = (getFinalState()).state - (getState()).state;
/* 1542 */     if (isHalfArch())
/* 1543 */       needed--; 
/* 1544 */     return StructureStateEnum.getStateByValue((byte)needed);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final StructureStateEnum getFinalState() {
/* 1550 */     int extra = (isHalfArch() && !isWood()) ? 1 : 0;
/* 1551 */     if (isTimberFramed())
/* 1552 */       return StructureStateEnum.getStateByValue((byte)(26 + extra)); 
/* 1553 */     if (this.type == StructureTypeEnum.SCAFFOLDING) {
/* 1554 */       return StructureStateEnum.getStateByValue((byte)5);
/*      */     }
/* 1556 */     return StructureStateEnum.getStateByValue((byte)(21 + extra));
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isFinished() {
/* 1561 */     return (this.state == StructureStateEnum.FINISHED);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setState(StructureStateEnum paramStructureStateEnum);
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getOrCreateOuterTile(boolean surfaced) throws NoSuchZoneException, NoSuchTileException {
/* 1572 */     if (isHorizontal()) {
/*      */       
/* 1574 */       VolaTile volaTile1 = Zones.getZone(this.x1, this.y1, surfaced).getOrCreateTile(this.x1, this.y1);
/* 1575 */       if (volaTile1.getStructure() == null) {
/* 1576 */         return volaTile1;
/*      */       }
/*      */       
/* 1579 */       VolaTile volaTile2 = Zones.getZone(this.x1, this.y1 - 1, surfaced).getOrCreateTile(this.x1, this.y1 - 1);
/* 1580 */       return volaTile2;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1585 */     VolaTile t = Zones.getZone(this.x1, this.y1, surfaced).getOrCreateTile(this.x1, this.y1);
/* 1586 */     if (t.getStructure() == null) {
/* 1587 */       return t;
/*      */     }
/*      */     
/* 1590 */     VolaTile t2 = Zones.getZone(this.x1 - 1, this.y1, surfaced).getOrCreateTile(this.x1 - 1, this.y1);
/* 1591 */     return t2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getOrCreateInnerTile(boolean surfaced) throws NoSuchZoneException, NoSuchTileException {
/* 1598 */     if (isHorizontal()) {
/*      */       
/* 1600 */       VolaTile volaTile1 = Zones.getZone(this.x1, this.y1, surfaced).getOrCreateTile(this.x1, this.y1);
/* 1601 */       if (volaTile1.getStructure() != null) {
/*      */         
/* 1603 */         if (volaTile1.isTransition())
/*      */         {
/* 1605 */           return Zones.getZone(this.x1, this.y1, false).getOrCreateTile(this.x1, this.y1);
/*      */         }
/* 1607 */         return volaTile1;
/*      */       } 
/*      */ 
/*      */       
/* 1611 */       VolaTile volaTile2 = Zones.getZone(this.x1, this.y1 - 1, surfaced).getOrCreateTile(this.x1, this.y1 - 1);
/* 1612 */       if (volaTile2.getStructure() == null)
/* 1613 */         logger.log(Level.INFO, volaTile2 + " has no structure, so no inner wall exists.", new Exception()); 
/* 1614 */       if (volaTile2.isTransition())
/*      */       {
/* 1616 */         return Zones.getZone(this.x1, this.y1 - 1, false).getOrCreateTile(this.x1, this.y1 - 1);
/*      */       }
/* 1618 */       return volaTile2;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1623 */     VolaTile toReturn = Zones.getZone(this.x1, this.y1, surfaced).getOrCreateTile(this.x1, this.y1);
/* 1624 */     if (toReturn.getStructure() != null) {
/*      */       
/* 1626 */       if (toReturn.isTransition())
/*      */       {
/* 1628 */         return Zones.getZone(this.x1, this.y1, false).getOrCreateTile(this.x1, this.y1);
/*      */       }
/* 1630 */       return toReturn;
/*      */     } 
/*      */ 
/*      */     
/* 1634 */     VolaTile t2 = Zones.getZone(this.x1 - 1, this.y1, surfaced).getOrCreateTile(this.x1 - 1, this.y1);
/* 1635 */     if (t2.getStructure() == null)
/* 1636 */       logger.log(Level.INFO, t2 + " has no structure, so no inner wall exists.", new Exception()); 
/* 1637 */     if (t2.isTransition())
/*      */     {
/* 1639 */       return Zones.getZone(this.x1 - 1, this.y1, false).getOrCreateTile(this.x1 - 1, this.y1);
/*      */     }
/* 1641 */     return t2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void poll(long currTime, VolaTile t, Structure struct) {
/* 1648 */     if (this.type == StructureTypeEnum.PLAN) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1653 */     if (this.type == StructureTypeEnum.RUBBLE) {
/*      */       
/* 1655 */       setDamage(getDamage() + 4.0F);
/*      */       return;
/*      */     } 
/* 1658 */     if (struct == null) {
/*      */       
/* 1660 */       logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + " no structure attached.");
/*      */       
/*      */       return;
/*      */     } 
/* 1664 */     if (currTime - struct.getCreationDate() <= 172800000L) {
/*      */       return;
/*      */     }
/*      */     
/* 1668 */     float mod = 1.0F;
/*      */ 
/*      */ 
/*      */     
/* 1672 */     Village village = null;
/* 1673 */     if (t != null) {
/*      */       
/* 1675 */       village = t.getVillage();
/* 1676 */       if (village == null)
/*      */       {
/*      */         
/* 1679 */         if (!isHorizontal()) {
/*      */           
/* 1681 */           Village westTile = Zones.getVillage(this.tilex - 1, this.tiley, true);
/* 1682 */           if (westTile != null && getStartX() == this.tilex)
/*      */           {
/* 1684 */             village = westTile;
/*      */           }
/*      */           
/* 1687 */           Village eastTile = Zones.getVillage(this.tilex + 1, this.tiley, true);
/* 1688 */           if (eastTile != null && getStartX() == this.tilex + 1)
/*      */           {
/* 1690 */             village = eastTile;
/*      */           
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 1696 */           Village northTile = Zones.getVillage(this.tilex, this.tiley - 1, true);
/* 1697 */           if (northTile != null && getStartY() == this.tiley)
/*      */           {
/* 1699 */             village = northTile;
/*      */           }
/*      */           
/* 1702 */           Village southTile = Zones.getVillage(this.tilex, this.tiley + 1, true);
/* 1703 */           if (southTile != null && getStartY() == this.tiley + 1)
/*      */           {
/* 1705 */             village = southTile;
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/* 1710 */       if (village != null && !village.lessThanWeekLeft()) {
/*      */ 
/*      */         
/* 1713 */         if (village.moreThanMonthLeft()) {
/*      */           return;
/*      */         }
/* 1716 */         mod *= 10.0F;
/*      */       }
/* 1718 */       else if (t.getKingdom() == 0 || Servers.localServer.HOMESERVER) {
/*      */         
/* 1720 */         mod *= 0.5F;
/*      */       } 
/* 1722 */       if (!t.isOnSurface()) {
/* 1723 */         mod *= 0.75F;
/*      */       }
/*      */     } 
/* 1726 */     if ((float)(currTime - this.lastUsed) > (Servers.localServer.testServer ? (60000.0F * mod) : (8.64E7F * mod)) && !hasNoDecay()) {
/*      */ 
/*      */ 
/*      */       
/* 1730 */       long ownerId = struct.getOwnerId();
/* 1731 */       if (ownerId == -10L) {
/*      */ 
/*      */         
/* 1734 */         this.damage += 20.0F + Server.rand.nextFloat() * 10.0F;
/*      */       }
/*      */       else {
/*      */         
/* 1738 */         boolean ownerIsInactive = false;
/* 1739 */         long aMonth = Servers.isThisATestServer() ? 86400000L : 2419200000L;
/* 1740 */         PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(ownerId);
/* 1741 */         if (pInfo == null) {
/*      */           
/* 1743 */           ownerIsInactive = true;
/* 1744 */         } else if (pInfo.lastLogin == 0L && pInfo.lastLogout < System.currentTimeMillis() - 3L * aMonth) {
/*      */           
/* 1746 */           ownerIsInactive = true;
/*      */         } 
/* 1748 */         if (ownerIsInactive) {
/* 1749 */           this.damage += 3.0F;
/*      */         }
/* 1751 */         if (t != null && village == null) {
/*      */           
/* 1753 */           Village v = Villages.getVillageWithPerimeterAt(t.tilex, t.tiley, t.isOnSurface());
/* 1754 */           if (v != null)
/*      */           {
/* 1756 */             if (!v.isCitizen(ownerId))
/*      */             {
/* 1758 */               if (ownerIsInactive)
/* 1759 */                 this.damage += 3.0F; 
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/* 1764 */       setLastUsed(currTime);
/* 1765 */       setDamage(this.damage + 0.1F * getDamageModifier());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isArched(StructureTypeEnum type) {
/* 1771 */     switch (type) {
/*      */       
/*      */       case ARCHED_LEFT:
/*      */       case ARCHED_RIGHT:
/*      */       case ARCHED:
/*      */       case ARCHED_T:
/*      */       case SCAFFOLDING:
/* 1778 */         return true;
/*      */     } 
/* 1780 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isHalfArch(StructureTypeEnum type) {
/* 1785 */     switch (type) {
/*      */       
/*      */       case ARCHED_LEFT:
/*      */       case ARCHED_RIGHT:
/*      */       case ARCHED_T:
/* 1790 */         return true;
/*      */     } 
/* 1792 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final List<Wall> getWallsAsArrayListFor(long structureId) {
/* 1797 */     List<Wall> toReturn = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1807 */     Set<Wall> flset = walls.get(Long.valueOf(structureId));
/* 1808 */     if (flset != null)
/*      */     {
/* 1810 */       toReturn.addAll(flset);
/*      */     }
/* 1812 */     return toReturn;
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
/*      */   public static final void loadAllWalls() throws IOException {
/* 1850 */     logger.log(Level.INFO, "Loading all walls.");
/* 1851 */     long s = System.nanoTime();
/* 1852 */     Connection dbcon = null;
/* 1853 */     PreparedStatement ps = null;
/* 1854 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1857 */       dbcon = DbConnector.getZonesDbCon();
/* 1858 */       ps = dbcon.prepareStatement("SELECT * FROM WALLS WHERE STARTX<ENDX OR STARTY<ENDY");
/* 1859 */       rs = ps.executeQuery();
/* 1860 */       while (rs.next())
/*      */       {
/* 1862 */         long sid = rs.getLong("STRUCTURE");
/* 1863 */         Set<Wall> flset = walls.get(Long.valueOf(sid));
/* 1864 */         if (flset == null) {
/*      */           
/* 1866 */           flset = new HashSet<>();
/* 1867 */           walls.put(Long.valueOf(sid), flset);
/*      */         } 
/* 1869 */         flset.add(new DbWall(rs.getInt("ID"), 
/* 1870 */               StructureTypeEnum.getTypeByINDEX(rs.getByte("TYPE")), rs
/* 1871 */               .getInt("TILEX"), rs
/* 1872 */               .getInt("TILEY"), rs
/* 1873 */               .getInt("STARTX"), rs
/* 1874 */               .getInt("STARTY"), rs
/* 1875 */               .getInt("ENDX"), rs
/* 1876 */               .getInt("ENDY"), rs
/* 1877 */               .getFloat("CURRENTQL"), rs
/* 1878 */               .getFloat("ORIGINALQL"), rs
/* 1879 */               .getFloat("DAMAGE"), sid, rs
/* 1880 */               .getLong("LASTMAINTAINED"), 
/* 1881 */               StructureStateEnum.getStateByValue(rs.getByte("STATE")), rs
/* 1882 */               .getInt("COLOR"), 
/* 1883 */               StructureMaterialEnum.getEnumByMaterial(rs.getByte("MATERIAL")), rs
/* 1884 */               .getBoolean("ISINDOOR"), rs
/* 1885 */               .getInt("HEIGHTOFFSET"), rs
/* 1886 */               .getInt("LAYER"), rs
/* 1887 */               .getBoolean("WALLORIENTATION"), rs
/* 1888 */               .getInt("SETTINGS")));
/*      */       }
/*      */     
/* 1891 */     } catch (SQLException sqx) {
/*      */       
/* 1893 */       logger.log(Level.WARNING, "Failed to load walls! " + sqx.getMessage(), sqx);
/* 1894 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1898 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1899 */       DbConnector.returnConnection(dbcon);
/* 1900 */       long e = System.nanoTime();
/* 1901 */       logger.log(Level.INFO, "Loaded " + walls.size() + " wall. That took " + ((float)(e - s) / 1000000.0F) + " ms.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifier() {
/* 1909 */     if (this.type == StructureTypeEnum.RUBBLE) {
/* 1910 */       return 0.001F;
/*      */     }
/*      */     
/* 1913 */     if (isStone() || isPlainStone() || isSlate() || isRoundedStone() || 
/* 1914 */       isMarble() || isRendered() || isPottery() || isSandstone())
/* 1915 */       return 100.0F / Math.max(1.0F, this.currentQL * (100.0F - this.damage) / 100.0F) * 0.3F; 
/* 1916 */     return 100.0F / Math.max(1.0F, this.currentQL * (100.0F - this.damage) / 100.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Door getDoor() {
/* 1921 */     if (isDoor()) {
/*      */ 
/*      */       
/*      */       try {
/* 1925 */         for (Door door : getOrCreateInnerTile((getLayer() == 0)).getDoors())
/*      */         {
/*      */           
/*      */           try {
/* 1929 */             if (door.getWall() == this) {
/* 1930 */               return door;
/*      */             }
/* 1932 */           } catch (NoSuchWallException noSuchWallException) {}
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1938 */       catch (NoSuchTileException nst) {
/*      */         
/* 1940 */         logger.log(Level.WARNING, "Why: " + nst.getMessage() + " " + getTileX() + "," + getTileY() + ", StructureId: " + this.structureId + ", wall id=" + this);
/*      */       
/*      */       }
/* 1943 */       catch (NoSuchZoneException nst) {
/*      */         
/* 1945 */         logger.log(Level.WARNING, "Why: " + nst.getMessage() + " " + getTileX() + "," + getTileY() + ", StructureId: " + this.structureId + ", wall id=" + this);
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 1950 */         for (Door door : getOrCreateOuterTile(true).getDoors())
/*      */         {
/*      */           
/*      */           try {
/* 1954 */             if (door.getWall() == this) {
/* 1955 */               return door;
/*      */             }
/* 1957 */           } catch (NoSuchWallException noSuchWallException) {}
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1963 */       catch (NoSuchTileException nst) {
/*      */         
/* 1965 */         logger.log(Level.WARNING, "Why: " + nst.getMessage() + " " + getTileX() + "," + getTileY() + ", StructureId: " + this.structureId + ", wall id=" + this);
/*      */       
/*      */       }
/* 1968 */       catch (NoSuchZoneException nst) {
/*      */         
/* 1970 */         logger.log(Level.WARNING, "Why: " + nst.getMessage() + " " + getTileX() + "," + getTileY() + ", StructureId: " + this.structureId + ", wall id=" + this);
/*      */       } 
/*      */     } 
/*      */     
/* 1974 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final VolaTile getTile() {
/*      */     try {
/* 1981 */       Structure struct = Structures.getStructure(this.structureId);
/* 1982 */       return struct.getTileFor(this);
/*      */     }
/* 1984 */     catch (NoSuchStructureException nss) {
/*      */       
/* 1986 */       logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + ", StructureId: " + this.structureId + " - " + nss
/* 1987 */           .getMessage(), (Throwable)nss);
/*      */       
/* 1989 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void removeDoors() throws NoSuchStructureException {
/* 2000 */     Structure struct = Structures.getStructure(this.structureId);
/*      */     
/* 2002 */     if (isDoor()) {
/*      */       
/* 2004 */       Door[] doors = struct.getAllDoors();
/* 2005 */       for (int x = 0; x < doors.length; x++) {
/*      */ 
/*      */         
/*      */         try {
/* 2009 */           if (doors[x].getWall() == this)
/*      */           {
/* 2011 */             struct.removeDoor(doors[x]);
/* 2012 */             doors[x].removeFromTiles();
/*      */           }
/*      */         
/* 2015 */         } catch (NoSuchWallException nsw) {
/*      */           
/* 2017 */           logger.log(Level.WARNING, "Problem removing doors from wall in StructureId: " + this.structureId + " - " + nsw
/* 2018 */               .getMessage(), (Throwable)nsw);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void setPlanData() {
/* 2027 */     this.type = StructureTypeEnum.PLAN;
/* 2028 */     this.state = StructureStateEnum.INITIALIZED;
/* 2029 */     this.currentQL = 1.0F;
/* 2030 */     this.originalQL = 1.0F;
/* 2031 */     this.damage = 0.0F;
/* 2032 */     this.material = StructureMaterialEnum.WOOD;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void setRubbleData() {
/* 2037 */     this.type = StructureTypeEnum.RUBBLE;
/* 2038 */     this.state = StructureStateEnum.FINISHED;
/* 2039 */     this.currentQL = 100.0F;
/* 2040 */     this.originalQL = 1.0F;
/* 2041 */     this.damage = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsRubble() {
/*      */     try {
/* 2048 */       Structure struct = Structures.getStructure(this.structureId);
/* 2049 */       struct.setFinished(false);
/* 2050 */       VolaTile tile = struct.getTileFor(this);
/* 2051 */       if (tile != null) {
/*      */         
/* 2053 */         removeDoors();
/* 2054 */         setRubbleData();
/* 2055 */         setColor(-1);
/* 2056 */         tile.updateWall(this);
/* 2057 */         addRubble(this);
/*      */       } else {
/*      */         
/* 2060 */         logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + ": no tile!?  StructureId: " + this.structureId);
/*      */       }
/*      */     
/* 2063 */     } catch (NoSuchStructureException nss) {
/*      */       
/* 2065 */       logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + ", StructureId: " + this.structureId + " - " + nss
/* 2066 */           .getMessage(), (Throwable)nss);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsPlan() {
/*      */     try {
/* 2074 */       Structure struct = Structures.getStructure(this.structureId);
/* 2075 */       struct.setFinished(false);
/* 2076 */       VolaTile tile = struct.getTileFor(this);
/* 2077 */       if (tile != null) {
/*      */         
/* 2079 */         removeDoors();
/* 2080 */         setPlanData();
/* 2081 */         setColor(-1);
/* 2082 */         tile.updateWall(this);
/* 2083 */         removeRubble(this);
/*      */       } else {
/*      */         
/* 2086 */         logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + ": no tile!?  StructureId: " + this.structureId);
/*      */       }
/*      */     
/* 2089 */     } catch (NoSuchStructureException nss) {
/*      */       
/* 2091 */       logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + ", StructureId: " + this.structureId + " - " + nss
/* 2092 */           .getMessage(), (Throwable)nss);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWindow() {
/* 2098 */     return (this.type == StructureTypeEnum.WINDOW || this.type == StructureTypeEnum.WIDE_WINDOW);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isJetty() {
/* 2103 */     return (this.type == StructureTypeEnum.JETTY);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBalcony() {
/* 2108 */     return (this.type == StructureTypeEnum.BALCONY);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOriel() {
/* 2113 */     return (this.type == StructureTypeEnum.ORIEL);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getDamageModifierForItem(Item item) {
/* 2119 */     float mod = 0.0F;
/* 2120 */     if (this.type == StructureTypeEnum.RUBBLE)
/* 2121 */       return 0.01F; 
/* 2122 */     if (isWood() || isTimberFramed()) {
/*      */       
/* 2124 */       if (item.isWeaponAxe()) {
/* 2125 */         mod = 0.03F;
/* 2126 */       } else if (item.isWeaponCrush()) {
/* 2127 */         mod = 0.02F;
/* 2128 */       } else if (item.isWeaponSlash()) {
/* 2129 */         mod = 0.015F;
/* 2130 */       } else if (item.isWeaponPierce()) {
/* 2131 */         mod = 0.01F;
/* 2132 */       } else if (item.isWeaponMisc()) {
/* 2133 */         mod = 0.007F;
/*      */       } 
/* 2135 */     } else if (isStone() || isPlainStone() || isSlate() || isRoundedStone() || 
/* 2136 */       isMarble() || isRendered() || isPottery() || isSandstone()) {
/*      */       
/* 2138 */       if (item.getTemplateId() == 20) {
/* 2139 */         mod = 0.02F;
/* 2140 */       } else if (item.isWeaponCrush()) {
/* 2141 */         mod = 0.015F;
/* 2142 */       } else if (item.isWeaponAxe()) {
/* 2143 */         mod = 0.0075F;
/* 2144 */       } else if (item.isWeaponSlash()) {
/* 2145 */         mod = 0.005F;
/* 2146 */       } else if (item.isWeaponPierce()) {
/* 2147 */         mod = 0.005F;
/* 2148 */       } else if (item.isWeaponMisc()) {
/* 2149 */         mod = 0.002F;
/*      */       } 
/*      */     } 
/* 2152 */     return mod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final StructureMaterialEnum getMaterial() {
/* 2162 */     return this.material;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMaterial(StructureMaterialEnum aMaterial) {
/* 2173 */     this.material = aMaterial;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isStone() {
/* 2179 */     return (this.material == StructureMaterialEnum.STONE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isPlainStone() {
/* 2184 */     return (this.material == StructureMaterialEnum.PLAIN_STONE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSlate() {
/* 2189 */     return (this.material == StructureMaterialEnum.SLATE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isRendered() {
/* 2194 */     return (this.material == StructureMaterialEnum.RENDERED);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isRoundedStone() {
/* 2199 */     return (this.material == StructureMaterialEnum.ROUNDED_STONE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isPottery() {
/* 2204 */     return (this.material == StructureMaterialEnum.POTTERY);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSandstone() {
/* 2209 */     return (this.material == StructureMaterialEnum.SANDSTONE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isPlastered() {
/* 2214 */     return (this.material == StructureMaterialEnum.RENDERED);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMarble() {
/* 2219 */     return (this.material == StructureMaterialEnum.MARBLE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean canSupportStoneBridges() {
/* 2224 */     return (isStone() || isPlainStone() || isMarble() || isSandstone() || isRoundedStone() || isSlate() || 
/* 2225 */       isRendered() || isPottery());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWood() {
/* 2231 */     return (this.material == StructureMaterialEnum.WOOD);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMetal() {
/* 2237 */     return (this.material == StructureMaterialEnum.METAL);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTimberFramed() {
/* 2242 */     return (this.material == StructureMaterialEnum.TIMBER_FRAMED);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getCover() {
/* 2247 */     if (isFinished()) {
/*      */       
/* 2249 */       if (isWindow() || isJetty())
/* 2250 */         return 70; 
/* 2251 */       if (isOriel())
/* 2252 */         return 80; 
/* 2253 */       if (isBalcony()) {
/* 2254 */         return 10;
/*      */       }
/* 2256 */       return 100;
/*      */     } 
/* 2258 */     return Math.max(0, (getState()).state);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int[] getItemTemplatesDealtForWall(StructureTypeEnum type, StructureStateEnum state, boolean finished) {
/* 2265 */     if (finished) {
/*      */       
/* 2267 */       int[] toReturn = new int[20];
/* 2268 */       for (int x = 0; x < toReturn.length; x++)
/* 2269 */         toReturn[x] = 22; 
/* 2270 */       return toReturn;
/*      */     } 
/*      */ 
/*      */     
/* 2274 */     if (state.state > 0 && type != StructureTypeEnum.PLAN) {
/*      */       
/* 2276 */       int[] toReturn = new int[state.state];
/* 2277 */       for (int x = 0; x < state.state; x++)
/* 2278 */         toReturn[x] = 22; 
/* 2279 */       return toReturn;
/*      */     } 
/* 2281 */     return EMPTY_INT_ARRAY;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setColor(int newcolor) {
/* 2287 */     changeColor(newcolor);
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getMaterialString() {
/* 2292 */     if (isStone())
/* 2293 */       return "stone"; 
/* 2294 */     if (isWood())
/* 2295 */       return "wood"; 
/* 2296 */     if (isTimberFramed())
/* 2297 */       return "timber framed"; 
/* 2298 */     if (isPlainStone())
/* 2299 */       return "plain stone"; 
/* 2300 */     if (isSlate())
/* 2301 */       return "slate"; 
/* 2302 */     if (isRoundedStone())
/* 2303 */       return "rounded stone"; 
/* 2304 */     if (isPottery())
/* 2305 */       return "pottery"; 
/* 2306 */     if (isSandstone())
/* 2307 */       return "sandstone"; 
/* 2308 */     if (isPlastered())
/* 2309 */       return "rendered"; 
/* 2310 */     if (isMarble())
/* 2311 */       return "marble"; 
/* 2312 */     return "wood";
/*      */   }
/*      */ 
/*      */   
/*      */   public final int[] getTemplateIdsNeededForNextState(StructureTypeEnum type) {
/*      */     int[] templatesNeeded;
/* 2318 */     if (isHalfArch(type)) {
/*      */ 
/*      */       
/* 2321 */       if (isWood()) {
/*      */         
/* 2323 */         if (this.state == StructureStateEnum.INITIALIZED)
/*      */         {
/* 2325 */           templatesNeeded = new int[2];
/* 2326 */           templatesNeeded[0] = 860;
/* 2327 */           templatesNeeded[1] = 217;
/*      */         }
/* 2329 */         else if (this.state == StructureStateEnum.STATE_2_NEEDED)
/*      */         {
/* 2331 */           templatesNeeded = new int[1];
/* 2332 */           templatesNeeded[0] = 22;
/*      */         }
/* 2334 */         else if (!isFinished())
/*      */         {
/* 2336 */           templatesNeeded = new int[1];
/* 2337 */           templatesNeeded[0] = 22;
/*      */         }
/*      */         else
/*      */         {
/* 2341 */           templatesNeeded = emptyArr;
/*      */         }
/*      */       
/* 2344 */       } else if (isTimberFramed()) {
/*      */         
/* 2346 */         if (this.state == StructureStateEnum.INITIALIZED || this.state.state < 7)
/*      */         {
/* 2348 */           templatesNeeded = new int[1];
/* 2349 */           templatesNeeded[0] = 860;
/*      */         }
/* 2351 */         else if (this.state.state < 17)
/*      */         {
/* 2353 */           templatesNeeded = new int[2];
/* 2354 */           templatesNeeded[0] = 620;
/* 2355 */           templatesNeeded[1] = 130;
/*      */         }
/* 2357 */         else if (this.state.state < (getFinalState()).state)
/*      */         {
/* 2359 */           templatesNeeded = new int[1];
/* 2360 */           templatesNeeded[0] = 130;
/*      */         }
/*      */         else
/*      */         {
/* 2364 */           templatesNeeded = emptyArr;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 2369 */       else if (this.state == StructureStateEnum.INITIALIZED) {
/*      */         
/* 2371 */         templatesNeeded = new int[1];
/* 2372 */         templatesNeeded[0] = 681;
/*      */       }
/* 2374 */       else if (!isFinished()) {
/*      */         
/* 2376 */         templatesNeeded = new int[2];
/* 2377 */         templatesNeeded[0] = getBrickFromType();
/* 2378 */         templatesNeeded[1] = 492;
/*      */       }
/*      */       else {
/*      */         
/* 2382 */         templatesNeeded = emptyArr;
/*      */       }
/*      */     
/*      */     }
/* 2386 */     else if (isWood()) {
/*      */       
/* 2388 */       if (this.state == StructureStateEnum.INITIALIZED)
/*      */       {
/* 2390 */         templatesNeeded = new int[2];
/* 2391 */         templatesNeeded[0] = 22;
/* 2392 */         templatesNeeded[1] = 217;
/*      */       }
/* 2394 */       else if (!isFinished())
/*      */       {
/* 2396 */         templatesNeeded = new int[1];
/* 2397 */         templatesNeeded[0] = 22;
/*      */       }
/*      */       else
/*      */       {
/* 2401 */         templatesNeeded = emptyArr;
/*      */       }
/*      */     
/* 2404 */     } else if (isTimberFramed()) {
/*      */       
/* 2406 */       if (this.state == StructureStateEnum.INITIALIZED || this.state.state < 6)
/*      */       {
/* 2408 */         templatesNeeded = new int[1];
/* 2409 */         templatesNeeded[0] = 860;
/*      */       }
/* 2411 */       else if (this.state.state < 16)
/*      */       {
/* 2413 */         templatesNeeded = new int[2];
/* 2414 */         templatesNeeded[0] = 620;
/* 2415 */         templatesNeeded[1] = 130;
/*      */       }
/* 2417 */       else if (this.state.state < (getFinalState()).state)
/*      */       {
/* 2419 */         templatesNeeded = new int[1];
/* 2420 */         templatesNeeded[0] = 130;
/*      */       }
/*      */       else
/*      */       {
/* 2424 */         templatesNeeded = emptyArr;
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 2429 */     else if (!isFinished()) {
/*      */       
/* 2431 */       templatesNeeded = new int[2];
/* 2432 */       templatesNeeded[0] = getBrickFromType();
/* 2433 */       templatesNeeded[1] = 492;
/*      */     }
/*      */     else {
/*      */       
/* 2437 */       templatesNeeded = emptyArr;
/*      */     } 
/*      */     
/* 2440 */     return templatesNeeded;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getBrickName() {
/*      */     String brickType;
/* 2446 */     if (isSlate()) {
/* 2447 */       brickType = "slate brick";
/* 2448 */     } else if (isRoundedStone()) {
/* 2449 */       brickType = "rounded stone";
/* 2450 */     } else if (isPottery()) {
/* 2451 */       brickType = "pottery brick";
/* 2452 */     } else if (isSandstone()) {
/* 2453 */       brickType = "sandstone brick";
/* 2454 */     } else if (isMarble()) {
/* 2455 */       brickType = "marble brick";
/*      */     } else {
/* 2457 */       brickType = "stone brick";
/* 2458 */     }  return brickType;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getBrickFromType() {
/* 2463 */     return getBrickFromType(this.material);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getBrickFromType(StructureMaterialEnum material) {
/* 2468 */     if (material == StructureMaterialEnum.SLATE)
/* 2469 */       return 1123; 
/* 2470 */     if (material == StructureMaterialEnum.ROUNDED_STONE)
/* 2471 */       return 1122; 
/* 2472 */     if (material == StructureMaterialEnum.POTTERY)
/* 2473 */       return 776; 
/* 2474 */     if (material == StructureMaterialEnum.SANDSTONE)
/* 2475 */       return 1121; 
/* 2476 */     if (material == StructureMaterialEnum.MARBLE) {
/* 2477 */       return 786;
/*      */     }
/* 2479 */     return 132;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getRepairItemTemplate() {
/* 2484 */     if (isWood())
/* 2485 */       return 22; 
/* 2486 */     if (isStone() || isPlainStone() || isRendered())
/* 2487 */       return 132; 
/* 2488 */     if (isRoundedStone())
/* 2489 */       return 1122; 
/* 2490 */     if (isPottery())
/* 2491 */       return 776; 
/* 2492 */     if (isSandstone())
/* 2493 */       return 1121; 
/* 2494 */     if (isMarble())
/* 2495 */       return 786; 
/* 2496 */     if (isSlate()) {
/* 2497 */       return 1123;
/*      */     }
/* 2499 */     return 22;
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
/*      */   public static final Wall getWall(long wid) {
/* 2534 */     int x = Tiles.decodeTileX(wid);
/* 2535 */     int y = Tiles.decodeTileY(wid);
/* 2536 */     boolean onSurface = (Tiles.decodeLayer(wid) == 0);
/* 2537 */     for (int xx = 1; xx >= -1; xx--) {
/*      */       
/* 2539 */       for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */         
/*      */         try {
/* 2543 */           Zone zone = Zones.getZone(x + xx, y + yy, onSurface);
/* 2544 */           VolaTile tile = zone.getTileOrNull(x + xx, y + yy);
/* 2545 */           if (tile != null) {
/*      */             
/* 2547 */             Wall[] wallarr = tile.getWalls();
/* 2548 */             for (int s = 0; s < wallarr.length; s++)
/*      */             {
/* 2550 */               if (wallarr[s].getId() == wid)
/*      */               {
/* 2552 */                 return wallarr[s];
/*      */               }
/*      */             }
/*      */           
/*      */           } 
/* 2557 */         } catch (NoSuchZoneException noSuchZoneException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2563 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLastUsed() {
/* 2568 */     return this.lastUsed;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void load() throws IOException;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setLastUsed(long paramLong);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void improveOrigQualityLevel(float paramFloat);
/*      */ 
/*      */   
/*      */   abstract boolean changeColor(int paramInt);
/*      */ 
/*      */   
/*      */   public abstract void setWallOrientation(boolean paramBoolean);
/*      */ 
/*      */   
/*      */   public abstract void delete();
/*      */ 
/*      */   
/*      */   public final int getHeight() {
/* 2598 */     return this.heightOffset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHeightOffset(int newHeightOffset) {
/* 2608 */     this.heightOffset = newHeightOffset;
/* 2609 */     setFloorLevel();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOnFloorLevel(int level) {
/* 2614 */     return (level == this.floorLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFloorLevel() {
/* 2620 */     return this.floorLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void setFloorLevel() {
/* 2626 */     this.floorLevel = this.heightOffset / 30;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloorZ() {
/* 2632 */     return (this.heightOffset / 10);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMinZ() {
/* 2638 */     return Zones.getHeightForNode(getTileX(), getTileY(), getLayer()) + getFloorZ();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxZ() {
/* 2644 */     return getMinZ() + 3.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWithinZ(float maxZ, float minZ, boolean followGround) {
/* 2652 */     return ((getFloorLevel() == 0 && followGround) || (minZ <= getMaxZ() && maxZ >= getMinZ()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getLayer() {
/* 2663 */     return this.layer;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnSurface() {
/* 2668 */     return (this.layer == 0);
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
/*      */   public abstract void setIndoor(boolean paramBoolean);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIndoor() {
/* 2689 */     return this.isIndoor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/* 2698 */     if (!isIndoor()) {
/*      */       
/* 2700 */       setAsPlan();
/*      */       
/*      */       return;
/*      */     } 
/* 2704 */     if (!MethodsStructure.isWallInsideStructure(this, isOnSurface())) {
/*      */       
/* 2706 */       setAsPlan();
/*      */       return;
/*      */     } 
/* 2709 */     removeRubble(this);
/* 2710 */     removeIndoorWall();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void removeIndoorWall() {
/* 2719 */     if (!isIndoor()) {
/*      */       
/* 2721 */       logger.log(Level.WARNING, "Tried to wall.remove() completely for an outdoor wall!");
/*      */       
/*      */       return;
/*      */     } 
/* 2725 */     if (!MethodsStructure.isWallInsideStructure(this, isOnSurface())) {
/*      */       
/* 2727 */       logger.log(Level.WARNING, "Tried to wall.remove() completely next to a wall without structure tiles on both sides!");
/*      */       
/*      */       return;
/*      */     } 
/*      */     try {
/* 2732 */       removeDoors();
/*      */     }
/* 2734 */     catch (NoSuchStructureException nse) {
/*      */       
/* 2736 */       logger.log(Level.WARNING, "Structure not found when trying to remove doors from wall " + getStructureId());
/*      */       
/*      */       return;
/*      */     } 
/* 2740 */     delete();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2746 */     VolaTile myTile = getTile();
/* 2747 */     if (myTile != null) {
/*      */       
/* 2749 */       myTile.removeWall(this, false);
/*      */     } else {
/*      */       
/* 2752 */       logger.log(Level.INFO, getName() + " at " + getTileX() + "," + getTileY() + " not removed from tile since we couldn't locate it.");
/*      */     } 
/*      */     
/* 2755 */     Set<Wall> flset = walls.get(Long.valueOf(getStructureId()));
/* 2756 */     if (flset != null)
/*      */     {
/* 2758 */       flset.remove(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWallPlan() {
/* 2767 */     return (getState() == StructureStateEnum.INITIALIZED);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isRubble() {
/* 2772 */     return (getType() == StructureTypeEnum.RUBBLE);
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
/*      */   public boolean isAlwaysOpen() {
/* 2784 */     return isArched();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWithinFloorLevels(int maxFloorLevel, int minFloorLevel) {
/* 2790 */     return (this.floorLevel <= maxFloorLevel && this.floorLevel >= minFloorLevel);
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
/*      */   public boolean supports(StructureSupport support) {
/* 2803 */     if (!supports())
/* 2804 */       return false; 
/* 2805 */     if (support.isFloor()) {
/*      */       
/* 2807 */       if (getFloorLevel() == support.getFloorLevel() || getFloorLevel() == support.getFloorLevel() - 1)
/*      */       {
/* 2809 */         if (isHorizontal())
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 2814 */           if (getMinX() == support.getMinX())
/*      */           {
/* 2816 */             if (getMinY() == support.getStartY() || getStartY() == support.getEndY()) {
/* 2817 */               return true;
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         }
/* 2823 */         else if (getMinY() == support.getMinY())
/*      */         {
/* 2825 */           if (getMinX() == support.getStartX() || getMinX() == support.getEndX()) {
/* 2826 */             return true;
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     } else {
/*      */       
/* 2833 */       int levelMod = support.supports() ? -1 : 0;
/* 2834 */       if (support.getFloorLevel() >= getFloorLevel() + levelMod && support.getFloorLevel() <= getFloorLevel() + 1)
/*      */       {
/* 2836 */         if (support.getMinX() == getMinX() && support.getMinY() == getMinY() && 
/* 2837 */           isHorizontal() == support.isHorizontal())
/*      */         {
/* 2839 */           return true;
/*      */         }
/*      */       }
/*      */     } 
/* 2843 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supports() {
/* 2849 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSupportedByGround() {
/* 2855 */     return (getFloorLevel() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 2866 */     return "Wall [number=" + this.number + ", structureId=" + this.structureId + ", type=" + this.type + ", material=" + getMaterial() + ", QL=" + 
/* 2867 */       getQualityLevel() + ", DMG=" + getDamage() + "]";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTempId() {
/* 2873 */     return -10L;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTypeName() {
/* 2878 */     return WallEnum.getWall(getType(), getMaterial()).getName();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setTile(int newTileX, int newTileY) {
/* 2884 */     this.tilex = newTileX;
/* 2885 */     this.tiley = newTileY;
/*      */     
/*      */     try {
/* 2888 */       save();
/*      */     }
/* 2890 */     catch (IOException e) {
/*      */       
/* 2892 */       logger.log(Level.WARNING, StringUtil.format("Failed to move wall to %d,%d: %s", new Object[] { Integer.valueOf(newTileX), Integer.valueOf(newTileY), e.getMessage() }), e);
/*      */     } 
/* 2894 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   void setSettings(int aSettings) {
/* 2899 */     this.permissions.setPermissionBits(aSettings);
/*      */   }
/*      */ 
/*      */   
/*      */   Permissions getSettings() {
/* 2904 */     return this.permissions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeAlwaysLit() {
/* 2915 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeAutoFilled() {
/* 2926 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeAutoLit() {
/* 2937 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBePeggedByPlayer() {
/* 2948 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBePlanted() {
/* 2959 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canBeSealedByPlayer() {
/* 2970 */     return false;
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
/*      */   public boolean canChangeCreator() {
/* 2982 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDecay() {
/* 2993 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDestroy() {
/* 3004 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDrag() {
/* 3015 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableDrop() {
/* 3026 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableEatAndDrink() {
/* 3037 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableImprove() {
/* 3048 */     return true;
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
/*      */   public boolean canDisableLocking() {
/* 3060 */     return isDoor();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableLockpicking() {
/* 3071 */     return isDoor();
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
/*      */   public boolean canDisableMoveable() {
/* 3083 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canDisableOwnerMoveing() {
/* 3094 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canDisableOwnerTurning() {
/* 3105 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisablePainting() {
/* 3116 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisablePut() {
/* 3127 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableRepair() {
/* 3138 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canDisableRuneing() {
/* 3149 */     return false;
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
/*      */   public boolean canDisableSpellTarget() {
/* 3161 */     return false;
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
/*      */   public boolean canDisableTake() {
/* 3173 */     return false;
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
/*      */   public boolean canDisableTurning() {
/* 3185 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canHaveCourier() {
/* 3196 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canHaveDakrMessenger() {
/* 3207 */     return false;
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
/*      */   public String getCreatorName() {
/* 3219 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getDamage() {
/* 3230 */     return this.damage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getQualityLevel() {
/* 3241 */     return this.currentQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasCourier() {
/* 3252 */     return this.permissions.hasPermission(Permissions.Allow.HAS_COURIER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasDarkMessenger() {
/* 3263 */     return this.permissions.hasPermission(Permissions.Allow.HAS_DARK_MESSENGER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNoDecay() {
/* 3274 */     return this.permissions.hasPermission(Permissions.Allow.DECAY_DISABLED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAlwaysLit() {
/* 3285 */     return this.permissions.hasPermission(Permissions.Allow.ALWAYS_LIT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoFilled() {
/* 3296 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_FILL.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoLit() {
/* 3307 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_LIGHT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIndestructible() {
/* 3318 */     return this.permissions.hasPermission(Permissions.Allow.NO_BASH.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoDrag() {
/* 3329 */     return this.permissions.hasPermission(Permissions.Allow.NO_DRAG.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoDrop() {
/* 3340 */     return this.permissions.hasPermission(Permissions.Allow.NO_DROP.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoEatOrDrink() {
/* 3351 */     return this.permissions.hasPermission(Permissions.Allow.NO_EAT_OR_DRINK.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoImprove() {
/* 3362 */     return this.permissions.hasPermission(Permissions.Allow.NO_IMPROVE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoMove() {
/* 3373 */     return this.permissions.hasPermission(Permissions.Allow.NOT_MOVEABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoPut() {
/* 3384 */     return this.permissions.hasPermission(Permissions.Allow.NO_PUT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoRepair() {
/* 3395 */     return this.permissions.hasPermission(Permissions.Allow.NO_REPAIR.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoTake() {
/* 3406 */     return this.permissions.hasPermission(Permissions.Allow.NO_TAKE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotLockable() {
/* 3417 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotLockpickable() {
/* 3428 */     return this.permissions.hasPermission(Permissions.Allow.NOT_LOCKPICKABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotPaintable() {
/* 3439 */     return this.permissions.hasPermission(Permissions.Allow.NOT_PAINTABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotRuneable() {
/* 3450 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotSpellTarget() {
/* 3461 */     return this.permissions.hasPermission(Permissions.Allow.NO_SPELLS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNotTurnable() {
/* 3472 */     return this.permissions.hasPermission(Permissions.Allow.NOT_TURNABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOwnerMoveable() {
/* 3483 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_MOVEABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOwnerTurnable() {
/* 3494 */     return this.permissions.hasPermission(Permissions.Allow.OWNER_TURNABLE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPlanted() {
/* 3505 */     return this.permissions.hasPermission(Permissions.Allow.PLANTED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSealedByPlayer() {
/* 3516 */     if (this.permissions.hasPermission(Permissions.Allow.SEALED_BY_PLAYER.getBit()))
/* 3517 */       return true; 
/* 3518 */     return false;
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
/*      */   public void setCreator(String aNewCreator) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean setDamage(float paramFloat);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHasCourier(boolean aCourier) {
/* 3549 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_COURIER.getBit(), aCourier);
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
/*      */   public void setHasDarkMessenger(boolean aDarkmessenger) {
/* 3561 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_DARK_MESSENGER.getBit(), aDarkmessenger);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHasNoDecay(boolean aNoDecay) {
/* 3572 */     this.permissions.setPermissionBit(Permissions.Allow.DECAY_DISABLED.getBit(), aNoDecay);
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
/*      */   public void setIsAlwaysLit(boolean aAlwaysLit) {
/* 3584 */     this.permissions.setPermissionBit(Permissions.Allow.ALWAYS_LIT.getBit(), aAlwaysLit);
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
/*      */   public void setIsAutoFilled(boolean aAutoFill) {
/* 3596 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_FILL.getBit(), aAutoFill);
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
/*      */   public void setIsAutoLit(boolean aAutoLight) {
/* 3608 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_LIGHT.getBit(), aAutoLight);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsIndestructible(boolean aNoDestroy) {
/* 3619 */     this.permissions.setPermissionBit(Permissions.Allow.NO_BASH.getBit(), aNoDestroy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoDrag(boolean aNoDrag) {
/* 3630 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DRAG.getBit(), aNoDrag);
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
/*      */   public void setIsNoDrop(boolean aNoDrop) {
/* 3642 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DROP.getBit(), aNoDrop);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoEatOrDrink(boolean aNoEatOrDrink) {
/* 3653 */     this.permissions.setPermissionBit(Permissions.Allow.NO_EAT_OR_DRINK.getBit(), aNoEatOrDrink);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoImprove(boolean aNoImprove) {
/* 3664 */     this.permissions.setPermissionBit(Permissions.Allow.NO_IMPROVE.getBit(), aNoImprove);
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
/*      */   public void setIsNoMove(boolean aNoMove) {
/* 3676 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_MOVEABLE.getBit(), aNoMove);
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
/*      */   public void setIsNoPut(boolean aNoPut) {
/* 3688 */     this.permissions.setPermissionBit(Permissions.Allow.NO_PUT.getBit(), aNoPut);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNoRepair(boolean aNoRepair) {
/* 3699 */     this.permissions.setPermissionBit(Permissions.Allow.NO_REPAIR.getBit(), aNoRepair);
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
/*      */   public void setIsNoTake(boolean aNoTake) {
/* 3711 */     this.permissions.setPermissionBit(Permissions.Allow.NO_TAKE.getBit(), aNoTake);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotLockable(boolean aNoLock) {
/* 3722 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKABLE.getBit(), aNoLock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotLockpickable(boolean aNoLockpick) {
/* 3733 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKPICKABLE.getBit(), aNoLockpick);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotPaintable(boolean aNoPaint) {
/* 3744 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_PAINTABLE.getBit(), aNoPaint);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotRuneable(boolean aNoRune) {
/* 3755 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_RUNEABLE.getBit(), aNoRune);
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
/*      */   public void setIsNotSpellTarget(boolean aNoSpells) {
/* 3767 */     this.permissions.setPermissionBit(Permissions.Allow.NO_SPELLS.getBit(), aNoSpells);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsNotTurnable(boolean aNoTurn) {
/* 3778 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_TURNABLE.getBit(), aNoTurn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsOwnerMoveable(boolean aOwnerMove) {
/* 3789 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_MOVEABLE.getBit(), aOwnerMove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsOwnerTurnable(boolean aOwnerTurn) {
/* 3800 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_TURNABLE.getBit(), aOwnerTurn);
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
/*      */   public void setIsPlanted(boolean aPlant) {
/* 3812 */     this.permissions.setPermissionBit(Permissions.Allow.PLANTED.getBit(), aPlant);
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
/*      */   public void setIsSealedByPlayer(boolean aSealed) {
/* 3824 */     this.permissions.setPermissionBit(Permissions.Allow.SEALED_BY_PLAYER.getBit(), aSealed);
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
/*      */   public abstract boolean setQualityLevel(float paramFloat);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOriginalQualityLevel(float newQL) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void savePermissions();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOnSouthBorder(TilePos pos) {
/* 3864 */     return ((getStartX() == pos.x || getEndX() == pos.x) && getEndY() == pos.y + 1 && getStartY() == pos.y + 1);
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
/*      */   public final boolean isOnNorthBorder(TilePos pos) {
/* 3877 */     return ((getStartX() == pos.x || getEndX() == pos.x) && getEndY() == pos.y && getStartY() == pos.y);
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
/*      */   public final boolean isOnWestBorder(TilePos pos) {
/* 3890 */     return (getStartX() == pos.x && getEndX() == pos.x && (getEndY() == pos.y || getStartY() == pos.y));
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
/*      */   public final boolean isOnEastBorder(TilePos pos) {
/* 3903 */     return (getStartX() == pos.x + 1 && getEndX() == pos.x + 1 && (getEndY() == pos.y || getStartY() == pos.y));
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Wall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
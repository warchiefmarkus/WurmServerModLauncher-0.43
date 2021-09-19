/*      */ package com.wurmonline.server.highways;
/*      */ 
/*      */ import com.wurmonline.math.Vector2f;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MeshTile;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.utils.CoordUtils;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import com.wurmonline.shared.constants.HighwayConstants;
/*      */ import com.wurmonline.shared.constants.StructureConstants;
/*      */ import java.util.HashSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MethodsHighways
/*      */   implements MiscConstants, HighwayConstants
/*      */ {
/*      */   public static final boolean middleOfHighway(HighwayPos highwayPos) {
/*   76 */     int tilex = highwayPos.getTilex();
/*   77 */     int tiley = highwayPos.getTiley();
/*   78 */     boolean onSurface = highwayPos.isOnSurface();
/*   79 */     BridgePart currentBridgePart = highwayPos.getBridgePart();
/*   80 */     Floor currentFloor = highwayPos.getFloor();
/*      */     
/*   82 */     if (currentBridgePart != null)
/*   83 */       return bridgeChecks(tilex, tiley, onSurface, currentBridgePart); 
/*   84 */     if (currentFloor != null)
/*   85 */       return floorChecks(tilex, tiley, onSurface, currentFloor); 
/*   86 */     if (!onSurface)
/*   87 */       return caveChecks(tilex, tiley); 
/*   88 */     return surfaceChecks(tilex, tiley);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean onHighway(Item item) {
/*   99 */     if (!Features.Feature.HIGHWAYS.isEnabled())
/*  100 */       return false; 
/*  101 */     HighwayPos highwayPos = getHighwayPos(item);
/*  102 */     return onHighway(highwayPos);
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
/*      */   public static final boolean onHighway(int cornerx, int cornery, boolean onSurface) {
/*  116 */     if (!Features.Feature.HIGHWAYS.isEnabled())
/*  117 */       return false; 
/*  118 */     HighwayPos highwayPos = getHighwayPos(cornerx, cornery, onSurface);
/*  119 */     return onHighway(highwayPos);
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
/*      */   public static final boolean onWagonerCamp(int cornerx, int cornery, boolean onSurface) {
/*  132 */     if (!Features.Feature.HIGHWAYS.isEnabled())
/*  133 */       return false; 
/*  134 */     HighwayPos highwayPos = getHighwayPos(cornerx, cornery, onSurface);
/*  135 */     return onWagonerCamp(highwayPos);
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
/*      */   public static final boolean onHighway(BridgePart bridgePart) {
/*  147 */     if (!Features.Feature.HIGHWAYS.isEnabled())
/*  148 */       return false; 
/*  149 */     HighwayPos highwayPos = getHighwayPos(bridgePart);
/*  150 */     return onHighway(highwayPos);
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
/*      */   public static final boolean onHighway(Floor floor) {
/*  162 */     if (!Features.Feature.HIGHWAYS.isEnabled())
/*  163 */       return false; 
/*  164 */     HighwayPos highwayPos = getHighwayPos(floor);
/*  165 */     return onHighway(highwayPos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean onHighway(@Nullable HighwayPos highwaypos) {
/*  176 */     if (highwaypos == null)
/*  177 */       return false; 
/*  178 */     if (containsMarker(highwaypos, (byte)0))
/*  179 */       return true; 
/*  180 */     if (containsMarker(highwaypos, (byte)1))
/*  181 */       return true; 
/*  182 */     if (containsMarker(highwaypos, (byte)2))
/*  183 */       return true; 
/*  184 */     if (containsMarker(highwaypos, (byte)4))
/*  185 */       return true; 
/*  186 */     if (containsMarker(highwaypos, (byte)8))
/*  187 */       return true; 
/*  188 */     if (containsMarker(highwaypos, (byte)16))
/*  189 */       return true; 
/*  190 */     if (containsMarker(highwaypos, (byte)32))
/*  191 */       return true; 
/*  192 */     if (containsMarker(highwaypos, (byte)64))
/*  193 */       return true; 
/*  194 */     if (containsMarker(highwaypos, -128))
/*  195 */       return true; 
/*  196 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean onWagonerCamp(@Nullable HighwayPos highwaypos) {
/*  206 */     if (highwaypos == null)
/*  207 */       return false; 
/*  208 */     if (containsWagonerWaystone(highwaypos, (byte)0))
/*  209 */       return true; 
/*  210 */     if (containsWagonerWaystone(highwaypos, (byte)1))
/*  211 */       return true; 
/*  212 */     if (containsWagonerWaystone(highwaypos, (byte)2))
/*  213 */       return true; 
/*  214 */     if (containsWagonerWaystone(highwaypos, (byte)4))
/*  215 */       return true; 
/*  216 */     if (containsWagonerWaystone(highwaypos, (byte)8))
/*  217 */       return true; 
/*  218 */     if (containsWagonerWaystone(highwaypos, (byte)16))
/*  219 */       return true; 
/*  220 */     if (containsWagonerWaystone(highwaypos, (byte)32))
/*  221 */       return true; 
/*  222 */     if (containsWagonerWaystone(highwaypos, (byte)64))
/*  223 */       return true; 
/*  224 */     if (containsWagonerWaystone(highwaypos, -128))
/*  225 */       return true; 
/*  226 */     return false;
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
/*      */   private static final boolean caveChecks(int tilex, int tiley) {
/*  238 */     MeshIO caveMesh = Server.caveMesh;
/*  239 */     int currentEncodedTile = caveMesh.getTile(tilex, tiley);
/*  240 */     byte currentType = Tiles.decodeType(currentEncodedTile);
/*  241 */     boolean onSurface = false;
/*      */     
/*  243 */     if (currentType != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */ 
/*      */ 
/*      */       
/*  247 */       boolean foundBridge = false;
/*      */       
/*  249 */       if (!Tiles.isReinforcedFloor(currentType) && currentType != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*  250 */         return false;
/*      */       }
/*  252 */       int northEncodedTile = caveMesh.getTile(tilex, tiley - 1);
/*  253 */       byte northType = Tiles.decodeType(northEncodedTile);
/*  254 */       BridgePart bridgePartNorth = Zones.getBridgePartFor(tilex, tiley - 1, false);
/*  255 */       if (bridgePartNorth != null) {
/*      */ 
/*      */         
/*  258 */         if (bridgePartNorth.getSouthExit() == 0) {
/*  259 */           foundBridge = true;
/*      */         }
/*  261 */       } else if (!Tiles.isReinforcedFloor(northType) && northType != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*  262 */         return false;
/*      */       } 
/*  264 */       int westEncodedTile = caveMesh.getTile(tilex - 1, tiley);
/*  265 */       byte westType = Tiles.decodeType(westEncodedTile);
/*  266 */       BridgePart bridgePartWest = Zones.getBridgePartFor(tilex, tiley - 1, false);
/*  267 */       if (bridgePartWest != null) {
/*      */ 
/*      */         
/*  270 */         if (bridgePartWest.getEastExit() == 0) {
/*  271 */           foundBridge = true;
/*      */         }
/*  273 */       } else if (!Tiles.isReinforcedFloor(westType) && westType != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*  274 */         return false;
/*      */       } 
/*      */       
/*  277 */       if (foundBridge) {
/*      */         
/*  279 */         BridgePart bridgePart = Zones.getBridgePartFor(tilex - 1, tiley - 1, false);
/*  280 */         if (bridgePart == null) {
/*  281 */           return false;
/*      */         }
/*      */       } else {
/*      */         
/*  285 */         int northWestEncodedTile = caveMesh.getTile(tilex - 1, tiley - 1);
/*  286 */         byte northWestType = Tiles.decodeType(northWestEncodedTile);
/*  287 */         if (!Tiles.isReinforcedFloor(northWestType) && northWestType != Tiles.Tile.TILE_CAVE_EXIT.id)
/*  288 */           return false; 
/*      */       } 
/*  290 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  297 */     for (int x = -1; x <= 0; x++) {
/*      */       
/*  299 */       for (int y = -1; y <= 0; y++) {
/*      */         
/*  301 */         int encodedTile = caveMesh.getTile(tilex + x, tiley + y);
/*  302 */         byte type = Tiles.decodeType(encodedTile);
/*  303 */         if (!Tiles.isReinforcedFloor(type) && !Tiles.isRoadType(type) && type != Tiles.Tile.TILE_CAVE_EXIT.id)
/*      */         {
/*      */           
/*  306 */           if (Tiles.isSolidCave(type)) {
/*      */ 
/*      */             
/*  309 */             int surfaceTile = Server.surfaceMesh.getTile(tilex + x, tiley + y);
/*  310 */             byte surfaceType = Tiles.decodeType(surfaceTile);
/*  311 */             if (!Tiles.isRoadType(surfaceType)) {
/*  312 */               return false;
/*      */             }
/*      */           } else {
/*  315 */             return false;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  320 */     return true;
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
/*      */   private static final boolean surfaceChecks(int tilex, int tiley) {
/*  334 */     boolean foundBridge = false;
/*  335 */     boolean onSurface = true;
/*      */     
/*  337 */     int currentEncodedTile = Server.surfaceMesh.getTile(tilex, tiley);
/*  338 */     byte currentType = Tiles.decodeType(currentEncodedTile);
/*  339 */     if (!Tiles.isRoadType(currentType) && currentType != Tiles.Tile.TILE_HOLE.id) {
/*  340 */       return false;
/*      */     }
/*  342 */     int northEncodedTile = Server.surfaceMesh.getTile(tilex, tiley - 1);
/*  343 */     byte northType = Tiles.decodeType(northEncodedTile);
/*  344 */     if (!Tiles.isRoadType(northType) && northType != Tiles.Tile.TILE_HOLE.id) {
/*      */       
/*  346 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley - 1, true);
/*  347 */       if (bridgePart == null) {
/*  348 */         return false;
/*      */       }
/*  350 */       if (bridgePart.getSouthExit() == 0) {
/*  351 */         foundBridge = true;
/*      */       }
/*      */     } 
/*  354 */     int westEncodedTile = Server.surfaceMesh.getTile(tilex - 1, tiley);
/*  355 */     byte westType = Tiles.decodeType(westEncodedTile);
/*  356 */     if (!Tiles.isRoadType(westType) && westType != Tiles.Tile.TILE_HOLE.id) {
/*      */       
/*  358 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex - 1, tiley, true);
/*  359 */       if (bridgePart == null) {
/*  360 */         return false;
/*      */       }
/*  362 */       if (bridgePart.getEastExit() == 0) {
/*  363 */         foundBridge = true;
/*      */       }
/*      */     } 
/*      */     
/*  367 */     if (foundBridge) {
/*      */       
/*  369 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex - 1, tiley - 1, true);
/*  370 */       if (bridgePart == null) {
/*  371 */         return false;
/*      */       }
/*      */     } else {
/*      */       
/*  375 */       int northWestEncodedTile = Server.surfaceMesh.getTile(tilex - 1, tiley - 1);
/*  376 */       byte northWestType = Tiles.decodeType(northWestEncodedTile);
/*  377 */       if (!Tiles.isRoadType(northWestType) && northWestType != Tiles.Tile.TILE_HOLE.id)
/*  378 */         return false; 
/*      */     } 
/*  380 */     return true;
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
/*      */   private static final boolean bridgeChecks(int tilex, int tiley, boolean onSurface, BridgePart currentBridgePart) {
/*  401 */     if (currentBridgePart.hasNorthExit()) {
/*      */       
/*  403 */       if (currentBridgePart.getNorthExit() == 0) {
/*      */         
/*  405 */         MeshIO mesh = onSurface ? Server.surfaceMesh : Server.caveMesh;
/*      */         
/*  407 */         if (!Tiles.isRoadType(mesh.getTile(tilex, tiley - 1)))
/*  408 */           return false; 
/*  409 */         if (!Tiles.isRoadType(mesh.getTile(tilex - 1, tiley - 1)))
/*  410 */           return false; 
/*  411 */         BridgePart bridgePart1 = Zones.getBridgePartFor(tilex - 1, tiley, onSurface);
/*  412 */         if (bridgePart1 == null || bridgePart1.getBridgePartState() != BridgeConstants.BridgeState.COMPLETED)
/*  413 */           return false; 
/*  414 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  418 */       Floor floorNorth = Zones.getFloor(tilex, tiley - 1, onSurface, currentBridgePart.getNorthExitFloorLevel());
/*  419 */       if (floorNorth == null || floorNorth.getFloorState() != StructureConstants.FloorState.COMPLETED)
/*  420 */         return false; 
/*  421 */       Floor floorNorthWest = Zones.getFloor(tilex - 1, tiley - 1, onSurface, currentBridgePart.getNorthExitFloorLevel());
/*  422 */       if (floorNorthWest == null || floorNorthWest.getFloorState() != StructureConstants.FloorState.COMPLETED)
/*  423 */         return false; 
/*  424 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex - 1, tiley, onSurface);
/*  425 */       if (bridgePart == null || bridgePart.getBridgePartState() != BridgeConstants.BridgeState.COMPLETED)
/*  426 */         return false; 
/*  427 */       return true;
/*      */     } 
/*      */     
/*  430 */     if (currentBridgePart.hasWestExit()) {
/*      */       
/*  432 */       if (currentBridgePart.getWestExit() == 0) {
/*      */         
/*  434 */         MeshIO mesh = onSurface ? Server.surfaceMesh : Server.caveMesh;
/*      */         
/*  436 */         BridgePart bridgePart1 = Zones.getBridgePartFor(tilex, tiley - 1, onSurface);
/*  437 */         if (bridgePart1 == null || bridgePart1.getBridgePartState() != BridgeConstants.BridgeState.COMPLETED)
/*  438 */           return false; 
/*  439 */         if (!Tiles.isRoadType(mesh.getTile(tilex - 1, tiley - 1)))
/*  440 */           return false; 
/*  441 */         if (!Tiles.isRoadType(mesh.getTile(tilex - 1, tiley)))
/*  442 */           return false; 
/*  443 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  447 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley - 1, onSurface);
/*  448 */       if (bridgePart == null || bridgePart.getBridgePartState() != BridgeConstants.BridgeState.COMPLETED)
/*  449 */         return false; 
/*  450 */       Floor floorNorthWest = Zones.getFloor(tilex - 1, tiley - 1, onSurface, currentBridgePart.getWestExitFloorLevel());
/*  451 */       if (floorNorthWest == null || floorNorthWest.getFloorState() != StructureConstants.FloorState.COMPLETED)
/*  452 */         return false; 
/*  453 */       Floor floorWest = Zones.getFloor(tilex - 1, tiley, onSurface, currentBridgePart.getWestExitFloorLevel());
/*  454 */       if (floorWest == null || floorWest.getFloorState() != StructureConstants.FloorState.COMPLETED)
/*  455 */         return false; 
/*  456 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  460 */     BridgePart bridgePartNorth = Zones.getBridgePartFor(tilex, tiley - 1, onSurface);
/*  461 */     if (bridgePartNorth == null || bridgePartNorth.getBridgePartState() != BridgeConstants.BridgeState.COMPLETED)
/*  462 */       return false; 
/*  463 */     BridgePart bridgePartNorthWest = Zones.getBridgePartFor(tilex - 1, tiley - 1, onSurface);
/*  464 */     if (bridgePartNorthWest == null || bridgePartNorthWest.getBridgePartState() != BridgeConstants.BridgeState.COMPLETED)
/*  465 */       return false; 
/*  466 */     BridgePart bridgePartWest = Zones.getBridgePartFor(tilex - 1, tiley, onSurface);
/*  467 */     if (bridgePartWest == null || bridgePartWest.getBridgePartState() != BridgeConstants.BridgeState.COMPLETED)
/*  468 */       return false; 
/*  469 */     return true;
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
/*      */   private static final boolean floorChecks(int tilex, int tiley, boolean onSurface, Floor currentFloor) {
/*  487 */     Floor floorNorth = Zones.getFloor(tilex, tiley - 1, onSurface, currentFloor.getFloorLevel());
/*  488 */     if (floorNorth == null) {
/*      */ 
/*      */       
/*  491 */       BridgePart bridgePartNorth = Zones.getBridgePartFor(tilex, tiley - 1, onSurface);
/*  492 */       if (bridgePartNorth == null || bridgePartNorth.getSouthExitFloorLevel() != currentFloor.getFloorLevel())
/*  493 */         return false; 
/*  494 */       BridgePart bridgePartNorthWest = Zones.getBridgePartFor(tilex - 1, tiley - 1, onSurface);
/*  495 */       if (bridgePartNorthWest == null || bridgePartNorthWest.getSouthExitFloorLevel() != currentFloor.getFloorLevel())
/*  496 */         return false; 
/*      */     } 
/*  498 */     Floor floorWest = Zones.getFloor(tilex - 1, tiley, onSurface, currentFloor.getFloorLevel());
/*  499 */     if (floorWest == null) {
/*      */ 
/*      */       
/*  502 */       BridgePart bridgePartWest = Zones.getBridgePartFor(tilex - 1, tiley, onSurface);
/*  503 */       if (bridgePartWest == null || bridgePartWest.getEastExitFloorLevel() != currentFloor.getFloorLevel())
/*  504 */         return false; 
/*  505 */       BridgePart bridgePartNorthWest = Zones.getBridgePartFor(tilex - 1, tiley - 1, onSurface);
/*  506 */       if (bridgePartNorthWest == null || bridgePartNorthWest.getEastExitFloorLevel() != currentFloor.getFloorLevel())
/*  507 */         return false; 
/*      */     } 
/*  509 */     if (floorNorth != null && floorWest != null) {
/*      */ 
/*      */       
/*  512 */       Floor floorNorthWest = Zones.getFloor(tilex - 1, tiley - 1, onSurface, currentFloor.getFloorLevel());
/*  513 */       if (floorNorthWest == null)
/*  514 */         return false; 
/*      */     } 
/*  516 */     return true;
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
/*      */   public static final boolean hasLink(byte dirs, byte linkdir) {
/*  528 */     return ((dirs & linkdir) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte getPossibleLinksFrom(Item marker) {
/*  539 */     HighwayPos highwayPos = getHighwayPosFromMarker(marker);
/*  540 */     return getPossibleLinksFrom(highwayPos, marker, marker.getAuxData());
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
/*      */   public static final byte getPossibleLinksFrom(HighwayPos highwayPos, Item marker) {
/*  552 */     return getPossibleLinksFrom(highwayPos, marker, (byte)0);
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
/*      */   private static final byte getPossibleLinksFrom(HighwayPos highwayPos, Item marker, byte currentLinks) {
/*  567 */     byte possibles = (byte)((currentLinks ^ 0xFFFFFFFF) & 0xFF);
/*  568 */     possibles = checkLink(possibles, highwayPos, (byte)1);
/*  569 */     possibles = checkLink(possibles, highwayPos, (byte)2);
/*  570 */     possibles = checkLink(possibles, highwayPos, (byte)4);
/*  571 */     possibles = checkLink(possibles, highwayPos, (byte)8);
/*  572 */     possibles = checkLink(possibles, highwayPos, (byte)16);
/*  573 */     possibles = checkLink(possibles, highwayPos, (byte)32);
/*  574 */     possibles = checkLink(possibles, highwayPos, (byte)64);
/*  575 */     possibles = checkLink(possibles, highwayPos, -128);
/*  576 */     if (marker.getTemplateId() == 1114)
/*      */     {
/*      */       
/*  579 */       if (numberOfSetBits(possibles) > 2) {
/*      */         
/*  581 */         int lower = possibles & 0xF;
/*  582 */         int upper = possibles & 0xF0;
/*  583 */         int loup = lower << 4;
/*  584 */         int uplo = upper >>> 4;
/*  585 */         int upnew = upper & loup;
/*  586 */         int lonew = lower & uplo;
/*  587 */         byte poss = (byte)(upnew | lonew);
/*      */         
/*  589 */         if (numberOfSetBits(poss) == 2)
/*  590 */           possibles = poss; 
/*      */       } 
/*      */     }
/*  593 */     return possibles;
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
/*      */   private static final byte checkLink(byte possibles, HighwayPos currentHighwayPos, byte checkdir) {
/*  611 */     if (hasLink(possibles, checkdir)) {
/*      */       
/*  613 */       HighwayPos highwayPos = getNewHighwayPosLinked(currentHighwayPos, checkdir);
/*  614 */       if (highwayPos != null) {
/*      */ 
/*      */         
/*  617 */         Item marker = getMarker(highwayPos);
/*      */         
/*  619 */         if (marker == null)
/*  620 */           return (byte)(possibles & (checkdir ^ 0xFFFFFFFF)); 
/*  621 */         if (hasLink(getOppositedir(checkdir), marker.getAuxData()))
/*  622 */           return (byte)(possibles & (checkdir ^ 0xFFFFFFFF)); 
/*  623 */         if (marker.getTemplateId() == 1114 && numberOfSetBits(marker.getAuxData()) > 1) {
/*  624 */           return (byte)(possibles & (checkdir ^ 0xFFFFFFFF));
/*      */         }
/*      */       } else {
/*  627 */         return (byte)(possibles & (checkdir ^ 0xFFFFFFFF));
/*      */       } 
/*  629 */     }  return possibles;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void autoLink(Item newMarker, byte possibleLinks) {
/*  640 */     HighwayPos currentHighwayPos = getHighwayPosFromMarker(newMarker);
/*      */     
/*  642 */     addLink(newMarker, currentHighwayPos, possibleLinks, (byte)1, (byte)16);
/*  643 */     addLink(newMarker, currentHighwayPos, possibleLinks, (byte)2, (byte)32);
/*  644 */     addLink(newMarker, currentHighwayPos, possibleLinks, (byte)4, (byte)64);
/*  645 */     addLink(newMarker, currentHighwayPos, possibleLinks, (byte)8, -128);
/*  646 */     addLink(newMarker, currentHighwayPos, possibleLinks, (byte)16, (byte)1);
/*  647 */     addLink(newMarker, currentHighwayPos, possibleLinks, (byte)32, (byte)2);
/*  648 */     addLink(newMarker, currentHighwayPos, possibleLinks, (byte)64, (byte)4);
/*  649 */     addLink(newMarker, currentHighwayPos, possibleLinks, -128, (byte)8);
/*      */ 
/*      */ 
/*      */     
/*  653 */     Routes.checkForNewRoutes(newMarker);
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
/*      */   private static final void addLink(Item newMarker, HighwayPos currentHighwayPos, byte possibles, byte linkdir, byte reversedir) {
/*  669 */     if (hasLink(possibles, linkdir)) {
/*      */       
/*  671 */       Item linkMarker = getMarker(currentHighwayPos, linkdir);
/*  672 */       if (linkMarker != null) {
/*      */         
/*  674 */         newMarker.setAuxData((byte)(newMarker.getAuxData() | linkdir));
/*  675 */         linkMarker.setAuxData((byte)(linkMarker.getAuxData() | reversedir));
/*  676 */         newMarker.updateModelNameOnGroundItem();
/*  677 */         linkMarker.updateModelNameOnGroundItem();
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
/*      */   public static final void removeLinksTo(Item fromMarker) {
/*  689 */     Item[] markers = Routes.getRouteMarkers(fromMarker);
/*      */     
/*  691 */     HighwayPos currentHighwayPos = getHighwayPosFromMarker(fromMarker);
/*  692 */     removeLink(currentHighwayPos, (byte)1, (byte)16);
/*  693 */     removeLink(currentHighwayPos, (byte)2, (byte)32);
/*  694 */     removeLink(currentHighwayPos, (byte)4, (byte)64);
/*  695 */     removeLink(currentHighwayPos, (byte)8, -128);
/*  696 */     removeLink(currentHighwayPos, (byte)16, (byte)1);
/*  697 */     removeLink(currentHighwayPos, (byte)32, (byte)2);
/*  698 */     removeLink(currentHighwayPos, (byte)64, (byte)4);
/*  699 */     removeLink(currentHighwayPos, -128, (byte)8);
/*      */ 
/*      */     
/*  702 */     fromMarker.setAuxData((byte)0);
/*      */     
/*  704 */     Items.removeMarker(fromMarker);
/*  705 */     fromMarker.updateModelNameOnGroundItem();
/*      */     
/*  707 */     for (Item marker : markers)
/*      */     {
/*  709 */       marker.updateModelNameOnGroundItem();
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
/*      */   private static final void removeLink(HighwayPos currentHighwayPos, byte fromdir, byte linkdir) {
/*  722 */     Item marker = getMarker(currentHighwayPos, fromdir);
/*  723 */     if (marker != null)
/*      */     {
/*  725 */       if (hasLink(marker.getAuxData(), linkdir)) {
/*      */         
/*  727 */         marker.setAuxData((byte)(marker.getAuxData() & (linkdir ^ 0xFFFFFFFF)));
/*  728 */         marker.updateModelNameOnGroundItem();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static final Item getMarker(Item marker, byte dir) {
/*  745 */     HighwayPos currentHighwayPos = getHighwayPosFromMarker(marker);
/*  746 */     switch (dir) {
/*      */       
/*      */       case 1:
/*  749 */         return getMarker(currentHighwayPos, (byte)1);
/*      */       case 2:
/*  751 */         return getMarker(currentHighwayPos, (byte)2);
/*      */       case 4:
/*  753 */         return getMarker(currentHighwayPos, (byte)4);
/*      */       case 8:
/*  755 */         return getMarker(currentHighwayPos, (byte)8);
/*      */       case 16:
/*  757 */         return getMarker(currentHighwayPos, (byte)16);
/*      */       case 32:
/*  759 */         return getMarker(currentHighwayPos, (byte)32);
/*      */       case 64:
/*  761 */         return getMarker(currentHighwayPos, (byte)64);
/*      */       case -128:
/*  763 */         return getMarker(currentHighwayPos, -128);
/*      */     } 
/*  765 */     return null;
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
/*      */   public static final boolean viewProtection(Creature performer, Item marker) {
/*  777 */     HighwayPos highwayPos = getHighwayPosFromMarker(marker);
/*  778 */     return sendShowProtection(performer, marker, highwayPos);
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
/*      */   public static final boolean viewProtection(Creature performer, HighwayPos highwayPos, Item marker) {
/*  792 */     return sendShowProtection(performer, marker, highwayPos);
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
/*      */   public static final boolean viewLinks(Creature performer, Item marker) {
/*  804 */     HighwayPos highwayPos = getHighwayPosFromMarker(marker);
/*  805 */     return viewLinks(performer, highwayPos, marker, (byte)1, marker.getAuxData());
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
/*      */   public static final boolean viewLinks(Creature performer, HighwayPos highwayPos, Item marker) {
/*  818 */     byte links = getPossibleLinksFrom(highwayPos, marker);
/*  819 */     return viewLinks(performer, highwayPos, marker, (byte)0, links);
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
/*      */   public static final boolean viewLinks(Creature performer, HighwayPos currentHighwayPos, Item marker, byte linktype, byte links) {
/*  835 */     String linktypeString = (linktype == 1) ? "Links" : "Possible links";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  845 */     boolean showing = false;
/*  846 */     if (links == 0) {
/*  847 */       performer.getCommunicator().sendNormalServerMessage("There are no " + linktypeString.toLowerCase() + " from there!");
/*      */     } else {
/*      */       
/*  850 */       showing = sendShowLinks(performer, currentHighwayPos, marker, linktype, links);
/*  851 */       if (Servers.isThisATestServer()) {
/*      */ 
/*      */         
/*  854 */         int count = 0;
/*  855 */         int todo = numberOfSetBits(links);
/*  856 */         StringBuilder buf = new StringBuilder();
/*  857 */         buf.append(linktypeString + " are: ");
/*  858 */         if (hasLink(links, (byte)1) && containsMarker(currentHighwayPos, (byte)1)) {
/*      */           
/*  860 */           if (count++ > 0)
/*  861 */             if (count == todo) {
/*  862 */               buf.append(" and ");
/*      */             } else {
/*  864 */               buf.append(", ");
/*  865 */             }   buf.append(getLinkDirString((byte)1));
/*      */         } 
/*  867 */         if (hasLink(links, (byte)2) && containsMarker(currentHighwayPos, (byte)2)) {
/*      */           
/*  869 */           if (count++ > 0)
/*  870 */             if (count == todo) {
/*  871 */               buf.append(" and ");
/*      */             } else {
/*  873 */               buf.append(", ");
/*  874 */             }   buf.append(getLinkDirString((byte)2));
/*      */         } 
/*  876 */         if (hasLink(links, (byte)4) && containsMarker(currentHighwayPos, (byte)4)) {
/*      */           
/*  878 */           if (count++ > 0)
/*  879 */             if (count == todo) {
/*  880 */               buf.append(" and ");
/*      */             } else {
/*  882 */               buf.append(", ");
/*  883 */             }   buf.append(getLinkDirString((byte)4));
/*      */         } 
/*  885 */         if (hasLink(links, (byte)8) && containsMarker(currentHighwayPos, (byte)8)) {
/*      */           
/*  887 */           if (count++ > 0)
/*  888 */             if (count == todo) {
/*  889 */               buf.append(" and ");
/*      */             } else {
/*  891 */               buf.append(", ");
/*  892 */             }   buf.append(getLinkDirString((byte)8));
/*      */         } 
/*  894 */         if (hasLink(links, (byte)16) && containsMarker(currentHighwayPos, (byte)16)) {
/*      */           
/*  896 */           if (count++ > 0)
/*  897 */             if (count == todo) {
/*  898 */               buf.append(" and ");
/*      */             } else {
/*  900 */               buf.append(", ");
/*  901 */             }   buf.append(getLinkDirString((byte)16));
/*      */         } 
/*  903 */         if (hasLink(links, (byte)32) && containsMarker(currentHighwayPos, (byte)32)) {
/*      */           
/*  905 */           if (count++ > 0)
/*  906 */             if (count == todo) {
/*  907 */               buf.append(" and ");
/*      */             } else {
/*  909 */               buf.append(", ");
/*  910 */             }   buf.append(getLinkDirString((byte)32));
/*      */         } 
/*  912 */         if (hasLink(links, (byte)64) && containsMarker(currentHighwayPos, (byte)64)) {
/*      */           
/*  914 */           if (count++ > 0)
/*  915 */             if (count == todo) {
/*  916 */               buf.append(" and ");
/*      */             } else {
/*  918 */               buf.append(", ");
/*  919 */             }   buf.append(getLinkDirString((byte)64));
/*      */         } 
/*  921 */         if (hasLink(links, -128) && containsMarker(currentHighwayPos, -128)) {
/*      */           
/*  923 */           if (count++ > 0)
/*  924 */             if (count == todo) {
/*  925 */               buf.append(" and ");
/*      */             } else {
/*  927 */               buf.append(", ");
/*  928 */             }   buf.append(getLinkDirString(-128));
/*      */         } 
/*  930 */         performer.getCommunicator().sendNormalServerMessage("test only:" + buf.toString());
/*      */       } 
/*      */     } 
/*  933 */     return showing;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean sendShowLinks(Creature performer, HighwayPos currentHighwayPos, Item marker, byte linktype, byte links) {
/*  939 */     boolean markerType = (marker.getTemplateId() == 1112);
/*  940 */     byte[] glows = new byte[8];
/*  941 */     glows[0] = getLinkGlow(linktype, marker, links, (byte)1);
/*  942 */     glows[1] = getLinkGlow(linktype, marker, links, (byte)2);
/*  943 */     glows[2] = getLinkGlow(linktype, marker, links, (byte)4);
/*  944 */     glows[3] = getLinkGlow(linktype, marker, links, (byte)8);
/*  945 */     glows[4] = getLinkGlow(linktype, marker, links, (byte)16);
/*  946 */     glows[5] = getLinkGlow(linktype, marker, links, (byte)32);
/*  947 */     glows[6] = getLinkGlow(linktype, marker, links, (byte)64);
/*  948 */     glows[7] = getLinkGlow(linktype, marker, links, -128);
/*      */     
/*  950 */     return performer.getCommunicator().sendShowLinks(markerType, currentHighwayPos, glows);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final byte getLinkGlow(byte linktype, Item marker, byte links, byte link) {
/*  955 */     if (hasLink(links, link)) {
/*      */       
/*  957 */       if (linktype == 1) {
/*      */ 
/*      */         
/*  960 */         if (marker.getTemplateId() == 1112) {
/*      */           
/*  962 */           Node node = Routes.getNode(marker.getWurmId());
/*      */           
/*  964 */           if (node != null) {
/*      */             
/*  966 */             Route route = node.getRoute(link);
/*  967 */             if (route != null)
/*  968 */               return 3; 
/*      */           } 
/*  970 */           return 1;
/*      */         } 
/*      */ 
/*      */         
/*  974 */         int count = numberOfSetBits(marker.getAuxData());
/*  975 */         if (count == 2)
/*  976 */           return 3; 
/*  977 */         if (count == 1)
/*  978 */           return 2; 
/*  979 */         return 1;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  984 */       if (marker.getTemplateId() == 1112);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  995 */       return 2;
/*      */     } 
/*  997 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean sendShowProtection(Creature performer, Item marker, HighwayPos currentHighwayPos) {
/* 1002 */     StringBuilder buf = new StringBuilder();
/* 1003 */     buf.append("Protected: center");
/* 1004 */     boolean markerType = (marker.getTemplateId() == 1112);
/*      */     
/* 1006 */     HashSet<HighwayPos> protectedTiles = new HashSet<>();
/* 1007 */     HighwayPos highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)1);
/* 1008 */     if (highwayPos != null) {
/*      */       
/* 1010 */       protectedTiles.add(highwayPos);
/* 1011 */       buf.append(", north");
/*      */     } 
/* 1013 */     highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)2);
/* 1014 */     if (highwayPos != null && isPaved(highwayPos)) {
/*      */       
/* 1016 */       protectedTiles.add(highwayPos);
/* 1017 */       buf.append(", northeast");
/*      */     } 
/* 1019 */     highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)4);
/* 1020 */     if (highwayPos != null && isPaved(highwayPos)) {
/*      */       
/* 1022 */       protectedTiles.add(highwayPos);
/* 1023 */       buf.append(", east");
/*      */     } 
/* 1025 */     highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)8);
/* 1026 */     if (highwayPos != null && isPaved(highwayPos)) {
/*      */       
/* 1028 */       protectedTiles.add(highwayPos);
/* 1029 */       buf.append(", southeast");
/*      */     } 
/* 1031 */     highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)16);
/* 1032 */     if (highwayPos != null && isPaved(highwayPos)) {
/*      */       
/* 1034 */       protectedTiles.add(highwayPos);
/* 1035 */       buf.append(", south");
/*      */     } 
/* 1037 */     highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)32);
/* 1038 */     if (highwayPos != null && isPaved(highwayPos)) {
/*      */       
/* 1040 */       protectedTiles.add(highwayPos);
/* 1041 */       buf.append(", southwest");
/*      */     } 
/* 1043 */     highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)64);
/* 1044 */     if (highwayPos != null) {
/*      */       
/* 1046 */       protectedTiles.add(highwayPos);
/* 1047 */       buf.append(", west");
/*      */     } 
/* 1049 */     highwayPos = getNewHighwayPosLinked(currentHighwayPos, -128);
/* 1050 */     if (highwayPos != null) {
/*      */       
/* 1052 */       protectedTiles.add(highwayPos);
/* 1053 */       buf.append(", northwest");
/*      */     } 
/*      */     
/* 1056 */     HighwayPos[] protectedHPs = (HighwayPos[])protectedTiles.toArray((Object[])new HighwayPos[protectedTiles.size()]);
/* 1057 */     if (Servers.isThisATestServer()) {
/*      */       
/* 1059 */       int pos = buf.lastIndexOf(",");
/* 1060 */       if (pos > 0)
/* 1061 */         buf.replace(pos, pos + 1, " and"); 
/* 1062 */       performer.getCommunicator().sendNormalServerMessage("test only:" + buf.toString());
/*      */     } 
/*      */     
/* 1065 */     return performer.getCommunicator().sendShowProtection(markerType, currentHighwayPos, protectedHPs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isPaved(HighwayPos highwayPos) {
/* 1075 */     if (highwayPos.getBridgeId() != -10L)
/* 1076 */       return true; 
/* 1077 */     if (highwayPos.getFloorLevel() > 0)
/* 1078 */       return true; 
/* 1079 */     if (highwayPos.isOnSurface()) {
/*      */       
/* 1081 */       int surfaceTile = Server.surfaceMesh.getTile(highwayPos.getTilex(), highwayPos.getTiley());
/* 1082 */       byte surfaceType = Tiles.decodeType(surfaceTile);
/* 1083 */       if (!Tiles.isRoadType(surfaceType)) {
/* 1084 */         return false;
/*      */       }
/*      */     } else {
/*      */       
/* 1088 */       int caveTile = Server.caveMesh.getTile(highwayPos.getTilex(), highwayPos.getTiley());
/* 1089 */       byte caveType = Tiles.decodeType(caveTile);
/* 1090 */       if (!Tiles.isReinforcedFloor(caveType) && !Tiles.isRoadType(caveType) && caveType != Tiles.Tile.TILE_CAVE_EXIT.id)
/* 1091 */         return false; 
/*      */     } 
/* 1093 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getLinkAsString(byte links) {
/* 1104 */     int count = 0;
/* 1105 */     int todo = numberOfSetBits(links);
/* 1106 */     StringBuilder buf = new StringBuilder();
/* 1107 */     if (hasLink(links, (byte)1)) {
/*      */       
/* 1109 */       if (count++ > 0)
/* 1110 */         if (count == todo) {
/* 1111 */           buf.append(" and ");
/*      */         } else {
/* 1113 */           buf.append(", ");
/* 1114 */         }   buf.append(getLinkDirString((byte)1));
/*      */     } 
/* 1116 */     if (hasLink(links, (byte)2)) {
/*      */       
/* 1118 */       if (count++ > 0)
/* 1119 */         if (count == todo) {
/* 1120 */           buf.append(" and ");
/*      */         } else {
/* 1122 */           buf.append(", ");
/* 1123 */         }   buf.append(getLinkDirString((byte)2));
/*      */     } 
/* 1125 */     if (hasLink(links, (byte)4)) {
/*      */       
/* 1127 */       if (count++ > 0)
/* 1128 */         if (count == todo) {
/* 1129 */           buf.append(" and ");
/*      */         } else {
/* 1131 */           buf.append(", ");
/* 1132 */         }   buf.append(getLinkDirString((byte)4));
/*      */     } 
/* 1134 */     if (hasLink(links, (byte)8)) {
/*      */       
/* 1136 */       if (count++ > 0)
/* 1137 */         if (count == todo) {
/* 1138 */           buf.append(" and ");
/*      */         } else {
/* 1140 */           buf.append(", ");
/* 1141 */         }   buf.append(getLinkDirString((byte)8));
/*      */     } 
/* 1143 */     if (hasLink(links, (byte)16)) {
/*      */       
/* 1145 */       if (count++ > 0)
/* 1146 */         if (count == todo) {
/* 1147 */           buf.append(" and ");
/*      */         } else {
/* 1149 */           buf.append(", ");
/* 1150 */         }   buf.append(getLinkDirString((byte)16));
/*      */     } 
/* 1152 */     if (hasLink(links, (byte)32)) {
/*      */       
/* 1154 */       if (count++ > 0)
/* 1155 */         if (count == todo) {
/* 1156 */           buf.append(" and ");
/*      */         } else {
/* 1158 */           buf.append(", ");
/* 1159 */         }   buf.append(getLinkDirString((byte)32));
/*      */     } 
/* 1161 */     if (hasLink(links, (byte)64)) {
/*      */       
/* 1163 */       if (count++ > 0)
/* 1164 */         if (count == todo) {
/* 1165 */           buf.append(" and ");
/*      */         } else {
/* 1167 */           buf.append(", ");
/* 1168 */         }   buf.append(getLinkDirString((byte)64));
/*      */     } 
/* 1170 */     if (hasLink(links, -128)) {
/*      */       
/* 1172 */       if (count++ > 0)
/* 1173 */         if (count == todo) {
/* 1174 */           buf.append(" and ");
/*      */         } else {
/* 1176 */           buf.append(", ");
/* 1177 */         }   buf.append(getLinkDirString(-128));
/*      */     } 
/* 1179 */     if (count == 0)
/* 1180 */       buf.append("none"); 
/* 1181 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean containsWagonerWaystone(HighwayPos highwayPos, byte fromdir) {
/* 1192 */     Item marker = getMarker(highwayPos, fromdir);
/* 1193 */     if (marker == null || marker.getTemplateId() == 1114)
/* 1194 */       return false; 
/* 1195 */     return (marker.getData() != -1L);
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
/*      */   public static final boolean containsMarker(HighwayPos highwayPos, byte fromdir) {
/* 1207 */     return (getMarker(highwayPos, fromdir) != null);
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
/*      */   @Nullable
/*      */   public static final Item getMarker(@Nullable HighwayPos currentHighwayPos, byte fromdir) {
/* 1220 */     if (currentHighwayPos == null)
/* 1221 */       return null; 
/* 1222 */     if (fromdir == 0) {
/* 1223 */       return getMarker(currentHighwayPos);
/*      */     }
/* 1225 */     HighwayPos highwayPos = getNewHighwayPosLinked(currentHighwayPos, fromdir);
/* 1226 */     if (highwayPos != null)
/* 1227 */       return getMarker(highwayPos); 
/* 1228 */     return null;
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
/*      */   public static final Item getMarker(HighwayPos highwaypos) {
/* 1240 */     if (highwaypos == null)
/* 1241 */       return null; 
/* 1242 */     return Items.getMarker(highwaypos.getTilex(), highwaypos.getTiley(), highwaypos.isOnSurface(), highwaypos
/* 1243 */         .getFloorLevel(), highwaypos.getBridgeId());
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
/*      */   public static final Item getMarker(Creature creature) {
/* 1255 */     return Items.getMarker(creature.getTileX(), creature.getTileY(), creature.isOnSurface(), creature.getFloorLevel(), creature.getBridgeId());
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
/*      */   public static final HighwayPos getHighwayPos(Item marker) {
/* 1267 */     int tilex = marker.getTileX();
/* 1268 */     int tiley = marker.getTileY();
/* 1269 */     boolean onSurface = marker.isOnSurface();
/*      */     
/* 1271 */     if (marker.getBridgeId() != -10L)
/*      */     {
/*      */       
/* 1274 */       return new HighwayPos(tilex, tiley, onSurface, Zones.getBridgePartFor(tilex, tiley, onSurface), null);
/*      */     }
/*      */     
/* 1277 */     if (marker.getFloorLevel() > 0)
/*      */     {
/* 1279 */       return new HighwayPos(tilex, tiley, onSurface, null, Zones.getFloor(tilex, tiley, onSurface, marker.getFloorLevel()));
/*      */     }
/*      */     
/* 1282 */     return new HighwayPos(tilex, tiley, onSurface, null, null);
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
/*      */   public static final HighwayPos getHighwayPos(BridgePart bridgePart) {
/* 1294 */     int tilex = bridgePart.getTileX();
/* 1295 */     int tiley = bridgePart.getTileY();
/* 1296 */     boolean onSurface = bridgePart.isOnSurface();
/*      */ 
/*      */     
/* 1299 */     return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
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
/*      */   public static final HighwayPos getHighwayPos(Floor floor) {
/* 1311 */     int tilex = floor.getTileX();
/* 1312 */     int tiley = floor.getTileY();
/* 1313 */     boolean onSurface = floor.isOnSurface();
/*      */ 
/*      */     
/* 1316 */     return new HighwayPos(tilex, tiley, onSurface, null, floor);
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
/*      */   @Nullable
/*      */   public static final HighwayPos getNewHighwayPosLinked(@Nullable HighwayPos currentHighwayPos, byte todir) {
/* 1329 */     if (currentHighwayPos == null)
/* 1330 */       return null; 
/* 1331 */     int tilex = currentHighwayPos.getTilex();
/* 1332 */     int tiley = currentHighwayPos.getTiley();
/*      */     
/* 1334 */     switch (todir) {
/*      */ 
/*      */       
/*      */       case 1:
/* 1338 */         tiley--;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 2:
/* 1343 */         tiley--;
/* 1344 */         tilex++;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 4:
/* 1349 */         tilex++;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 8:
/* 1354 */         tiley++;
/* 1355 */         tilex++;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 16:
/* 1360 */         tiley++;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 32:
/* 1365 */         tiley++;
/* 1366 */         tilex--;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 64:
/* 1371 */         tilex--;
/*      */         break;
/*      */ 
/*      */       
/*      */       case -128:
/* 1376 */         tiley--;
/* 1377 */         tilex--;
/*      */         break;
/*      */     } 
/*      */     
/* 1381 */     boolean onSurface = currentHighwayPos.isOnSurface();
/*      */     
/* 1383 */     if (currentHighwayPos.getBridgePart() != null)
/* 1384 */       return getNewHighwayPosFromBridge(tilex, tiley, onSurface, currentHighwayPos.getBridgePart(), todir); 
/* 1385 */     if (currentHighwayPos.getFloor() != null)
/* 1386 */       return getNewHighwayPosFromFloor(tilex, tiley, onSurface, currentHighwayPos.getFloor(), todir); 
/* 1387 */     if (onSurface) {
/*      */ 
/*      */       
/* 1390 */       int encodedtile = Server.surfaceMesh.getTile(tilex, tiley);
/* 1391 */       byte type = Tiles.decodeType(encodedtile);
/* 1392 */       if (type == Tiles.Tile.TILE_HOLE.id)
/*      */       {
/* 1394 */         return new HighwayPos(tilex, tiley, false, null, null);
/*      */       }
/*      */ 
/*      */       
/* 1398 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley, onSurface);
/* 1399 */       if (bridgePart != null)
/*      */       {
/*      */         
/* 1402 */         if (bridgePart.getSouthExit() == 0 && (todir == Byte.MIN_VALUE || todir == 1 || todir == 2)) {
/* 1403 */           return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */         }
/* 1405 */         if (bridgePart.getWestExit() == 0 && (todir == 2 || todir == 4 || todir == 8)) {
/* 1406 */           return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */         }
/* 1408 */         if (bridgePart.getNorthExit() == 0 && (todir == 8 || todir == 16 || todir == 32)) {
/* 1409 */           return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */         }
/* 1411 */         if (bridgePart.getEastExit() == 0 && (todir == 32 || todir == 64 || todir == Byte.MIN_VALUE)) {
/* 1412 */           return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1419 */       int encodedCurrentTile = Server.caveMesh.getTile(currentHighwayPos.getTilex(), currentHighwayPos.getTiley());
/* 1420 */       byte currentType = Tiles.decodeType(encodedCurrentTile);
/* 1421 */       int encodedtile = Server.caveMesh.getTile(tilex, tiley);
/* 1422 */       byte type = Tiles.decodeType(encodedtile);
/* 1423 */       if (currentType == Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */ 
/*      */         
/* 1426 */         if (Tiles.isSolidCave(type))
/*      */         {
/* 1428 */           return new HighwayPos(tilex, tiley, true, null, null);
/*      */         }
/*      */       } else {
/* 1431 */         if (Tiles.isSolidCave(type))
/*      */         {
/* 1433 */           return null;
/*      */         }
/*      */ 
/*      */         
/* 1437 */         BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley, onSurface);
/* 1438 */         if (bridgePart != null) {
/*      */ 
/*      */           
/* 1441 */           if (bridgePart.getSouthExit() == 0 && (todir == Byte.MIN_VALUE || todir == 1 || todir == 2)) {
/* 1442 */             return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */           }
/* 1444 */           if (bridgePart.getWestExit() == 0 && (todir == 2 || todir == 4 || todir == 8)) {
/* 1445 */             return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */           }
/* 1447 */           if (bridgePart.getNorthExit() == 0 && (todir == 8 || todir == 16 || todir == 32)) {
/* 1448 */             return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */           }
/* 1450 */           if (bridgePart.getEastExit() == 0 && (todir == 32 || todir == 64 || todir == Byte.MIN_VALUE)) {
/* 1451 */             return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1456 */     return new HighwayPos(tilex, tiley, onSurface, null, null);
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
/*      */   @Nullable
/*      */   public static final HighwayPos getHighwayPos(int cornerx, int cornery, boolean onSurface) {
/* 1473 */     if (onSurface) {
/*      */       
/* 1475 */       int encodedTile = Server.surfaceMesh.getTile(cornerx, cornery);
/* 1476 */       byte type = Tiles.decodeType(encodedTile);
/* 1477 */       if (type == Tiles.Tile.TILE_HOLE.id)
/*      */       {
/*      */         
/* 1480 */         return new HighwayPos(cornerx, cornery, false, null, null);
/*      */       }
/*      */     } 
/* 1483 */     BridgePart bridgePart = Zones.getBridgePartFor(cornerx, cornery, onSurface);
/* 1484 */     if (bridgePart != null && (bridgePart.getNorthExit() == 0 || bridgePart.getEastExit() == 0 || bridgePart
/* 1485 */       .getSouthExit() == 0 || bridgePart.getWestExit() == 0))
/* 1486 */       return new HighwayPos(cornerx, cornery, onSurface, bridgePart, null); 
/* 1487 */     return new HighwayPos(cornerx, cornery, onSurface, null, null);
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
/*      */   @Nullable
/*      */   public static final HighwayPos getHighwayPos(int cornerx, int cornery, boolean onSurface, int heightOffset) {
/* 1502 */     if (heightOffset == 0) {
/* 1503 */       return getHighwayPos(cornerx, cornery, onSurface);
/*      */     }
/* 1505 */     Floor[] floors = Zones.getFloorsAtTile(cornerx, cornery, heightOffset, heightOffset, onSurface);
/* 1506 */     if (floors != null && floors.length == 1)
/*      */     {
/* 1508 */       return getHighwayPos(floors[0]);
/*      */     }
/* 1510 */     if (heightOffset > 0) {
/*      */ 
/*      */       
/* 1513 */       BridgePart bridgePart = Zones.getBridgePartFor(cornerx, cornery, onSurface);
/* 1514 */       if (bridgePart != null)
/*      */       {
/* 1516 */         return getHighwayPos(bridgePart);
/*      */       }
/*      */     } 
/* 1519 */     return null;
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
/*      */   public static final HighwayPos getHighwayPos(Creature creature) {
/* 1531 */     int tilex = creature.getTileX();
/* 1532 */     int tiley = creature.getTileY();
/* 1533 */     boolean onSurface = creature.isOnSurface();
/*      */     
/* 1535 */     if (creature.getBridgeId() != -10L) {
/*      */       
/* 1537 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley, onSurface);
/*      */       
/* 1539 */       if (bridgePart != null)
/* 1540 */         return new HighwayPos(tilex, tiley, onSurface, bridgePart, null); 
/*      */     } 
/* 1542 */     if (creature.getFloorLevel() > 0) {
/*      */       
/* 1544 */       Floor floor = Zones.getFloor(tilex, tiley, onSurface, creature.getFloorLevel());
/*      */       
/* 1546 */       if (floor != null)
/* 1547 */         return new HighwayPos(tilex, tiley, onSurface, null, floor); 
/*      */     } 
/* 1549 */     return new HighwayPos(tilex, tiley, onSurface, null, null);
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
/*      */   @Nullable
/*      */   public static final HighwayPos getNewHighwayPosCorner(Creature performer, int currentTilex, int currentTiley, boolean onSurface, @Nullable BridgePart currentBridgePart, @Nullable Floor currentFloor) {
/* 1568 */     Vector2f pos = performer.getPos2f();
/* 1569 */     int posTilex = CoordUtils.WorldToTile(pos.x + 2.0F);
/* 1570 */     int posTiley = CoordUtils.WorldToTile(pos.y + 2.0F);
/*      */ 
/*      */     
/* 1573 */     if (posTilex == currentTilex && posTiley == currentTiley) {
/* 1574 */       return new HighwayPos(currentTilex, currentTiley, onSurface, currentBridgePart, currentFloor);
/*      */     }
/*      */     
/* 1577 */     byte fromdir = 0;
/* 1578 */     if (posTilex == currentTilex && posTiley < currentTiley) {
/* 1579 */       fromdir = 1;
/* 1580 */     } else if (posTilex > currentTilex && posTiley < currentTiley) {
/* 1581 */       fromdir = 2;
/* 1582 */     } else if (posTilex > currentTilex && posTiley == currentTiley) {
/* 1583 */       fromdir = 4;
/* 1584 */     } else if (posTilex > currentTilex && posTiley > currentTiley) {
/* 1585 */       fromdir = 8;
/* 1586 */     } else if (posTilex == currentTilex && posTiley > currentTiley) {
/* 1587 */       fromdir = 16;
/* 1588 */     } else if (posTilex < currentTilex && posTiley > currentTiley) {
/* 1589 */       fromdir = 32;
/* 1590 */     } else if (posTilex < currentTilex && posTiley == currentTiley) {
/* 1591 */       fromdir = 64;
/* 1592 */     } else if (posTilex < currentTilex && posTiley < currentTiley) {
/* 1593 */       fromdir = Byte.MIN_VALUE;
/*      */     } 
/*      */     
/* 1596 */     if (currentBridgePart != null)
/* 1597 */       return getNewHighwayPosFromBridge(posTilex, posTiley, onSurface, currentBridgePart, fromdir); 
/* 1598 */     if (currentFloor != null) {
/* 1599 */       return getNewHighwayPosFromFloor(posTilex, posTiley, onSurface, currentFloor, fromdir);
/*      */     }
/* 1601 */     if (onSurface) {
/*      */ 
/*      */       
/* 1604 */       int encodedtile = Server.surfaceMesh.getTile(posTilex, posTiley);
/* 1605 */       byte type = Tiles.decodeType(encodedtile);
/* 1606 */       if (type == Tiles.Tile.TILE_HOLE.id)
/*      */       {
/* 1608 */         return new HighwayPos(posTilex, posTiley, false, null, null);
/*      */       }
/*      */       
/* 1611 */       BridgePart bridgePart = Zones.getBridgePartFor(posTilex, posTiley, onSurface);
/* 1612 */       if (bridgePart != null)
/*      */       {
/*      */         
/* 1615 */         if (bridgePart.getSouthExit() == 0 && (fromdir == Byte.MIN_VALUE || fromdir == 1 || fromdir == 2)) {
/* 1616 */           return new HighwayPos(posTilex, posTiley, onSurface, bridgePart, null);
/*      */         }
/* 1618 */         if (bridgePart.getWestExit() == 0 && (fromdir == 2 || fromdir == 4 || fromdir == 8)) {
/* 1619 */           return new HighwayPos(posTilex, posTiley, onSurface, bridgePart, null);
/*      */         }
/* 1621 */         if (bridgePart.getNorthExit() == 0 && (fromdir == 8 || fromdir == 16 || fromdir == 32)) {
/* 1622 */           return new HighwayPos(posTilex, posTiley, onSurface, bridgePart, null);
/*      */         }
/* 1624 */         if (bridgePart.getEastExit() == 0 && (fromdir == 32 || fromdir == 64 || fromdir == Byte.MIN_VALUE)) {
/* 1625 */           return new HighwayPos(posTilex, posTiley, onSurface, bridgePart, null);
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1632 */       int encodedtile = Server.caveMesh.getTile(posTilex, posTiley);
/* 1633 */       byte type = Tiles.decodeType(encodedtile);
/* 1634 */       if (Tiles.isSolidCave(type))
/*      */       {
/* 1636 */         return new HighwayPos(posTilex, posTiley, true, null, null);
/*      */       }
/*      */     } 
/* 1639 */     return new HighwayPos(posTilex, posTiley, onSurface, null, null);
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
/*      */   @Nullable
/*      */   private static final HighwayPos getNewHighwayPosFromBridge(int tilex, int tiley, boolean onSurface, BridgePart currentBridgePart, byte fromdir) {
/* 1658 */     if (currentBridgePart.hasNorthExit() && (fromdir == Byte.MIN_VALUE || fromdir == 1 || fromdir == 2)) {
/*      */ 
/*      */       
/* 1661 */       if (currentBridgePart.hasHouseNorthExit()) {
/*      */ 
/*      */         
/* 1664 */         Floor floor = Zones.getFloor(tilex, tiley, onSurface, currentBridgePart.getNorthExitFloorLevel());
/* 1665 */         if (floor == null)
/*      */         {
/*      */           
/* 1668 */           return null;
/*      */         }
/*      */         
/* 1671 */         return new HighwayPos(tilex, tiley, onSurface, null, floor);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1676 */       return new HighwayPos(tilex, tiley, onSurface, null, null);
/*      */     } 
/*      */ 
/*      */     
/* 1680 */     if (currentBridgePart.hasEastExit() && (fromdir == 2 || fromdir == 4 || fromdir == 32 || fromdir == 2)) {
/*      */ 
/*      */       
/* 1683 */       if (currentBridgePart.hasHouseEastExit()) {
/*      */ 
/*      */         
/* 1686 */         Floor floor = Zones.getFloor(tilex, tiley, onSurface, currentBridgePart.getEastExitFloorLevel());
/* 1687 */         if (floor == null)
/*      */         {
/*      */           
/* 1690 */           return null;
/*      */         }
/*      */         
/* 1693 */         return new HighwayPos(tilex, tiley, onSurface, null, floor);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1698 */       return new HighwayPos(tilex, tiley, onSurface, null, null);
/*      */     } 
/*      */ 
/*      */     
/* 1702 */     if (currentBridgePart.hasSouthExit() && (fromdir == 8 || fromdir == 16 || fromdir == 32)) {
/*      */ 
/*      */       
/* 1705 */       if (currentBridgePart.hasHouseSouthExit()) {
/*      */ 
/*      */         
/* 1708 */         Floor floor = Zones.getFloor(tilex, tiley, onSurface, currentBridgePart.getSouthExitFloorLevel());
/* 1709 */         if (floor == null)
/*      */         {
/*      */           
/* 1712 */           return null;
/*      */         }
/*      */         
/* 1715 */         return new HighwayPos(tilex, tiley, onSurface, null, floor);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1720 */       return new HighwayPos(tilex, tiley, onSurface, null, null);
/*      */     } 
/*      */ 
/*      */     
/* 1724 */     if (currentBridgePart.hasWestExit() && (fromdir == 32 || fromdir == 64 || fromdir == Byte.MIN_VALUE || fromdir == 2)) {
/*      */ 
/*      */       
/* 1727 */       if (currentBridgePart.hasHouseWestExit()) {
/*      */ 
/*      */         
/* 1730 */         Floor floor = Zones.getFloor(tilex, tiley, onSurface, currentBridgePart.getWestExitFloorLevel());
/* 1731 */         if (floor == null)
/*      */         {
/*      */           
/* 1734 */           return null;
/*      */         }
/*      */         
/* 1737 */         return new HighwayPos(tilex, tiley, onSurface, null, floor);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1742 */       return new HighwayPos(tilex, tiley, onSurface, null, null);
/*      */     } 
/*      */ 
/*      */     
/* 1746 */     BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley, onSurface);
/* 1747 */     if (bridgePart != null) {
/* 1748 */       return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */     }
/* 1750 */     return null;
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
/*      */   @Nullable
/*      */   private static final HighwayPos getNewHighwayPosFromFloor(int tilex, int tiley, boolean onSurface, Floor currentFloor, byte fromdir) {
/* 1767 */     Floor floor = Zones.getFloor(tilex, tiley, onSurface, currentFloor.getFloorLevel());
/* 1768 */     if (floor != null)
/*      */     {
/*      */       
/* 1771 */       return new HighwayPos(tilex, tiley, onSurface, null, floor);
/*      */     }
/*      */     
/* 1774 */     BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley, onSurface);
/* 1775 */     if (bridgePart != null) {
/*      */ 
/*      */       
/* 1778 */       if (bridgePart.getSouthExitFloorLevel() == currentFloor.getFloorLevel() && (fromdir == Byte.MIN_VALUE || fromdir == 1 || fromdir == 2)) {
/* 1779 */         return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */       }
/* 1781 */       if (bridgePart.getWestExitFloorLevel() == currentFloor.getFloorLevel() && (fromdir == 2 || fromdir == 4 || fromdir == 8)) {
/* 1782 */         return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */       }
/* 1784 */       if (bridgePart.getNorthExitFloorLevel() == currentFloor.getFloorLevel() && (fromdir == 8 || fromdir == 16 || fromdir == 32)) {
/* 1785 */         return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */       }
/* 1787 */       if (bridgePart.getEastExitFloorLevel() == currentFloor.getFloorLevel() && (fromdir == 32 || fromdir == 64 || fromdir == Byte.MIN_VALUE)) {
/* 1788 */         return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */       }
/*      */     } 
/*      */     
/* 1792 */     return null;
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
/*      */   private static final HighwayPos getHighwayPosFromMarker(Item marker) {
/* 1804 */     int tilex = marker.getTileX();
/* 1805 */     int tiley = marker.getTileY();
/* 1806 */     boolean onSurface = marker.isOnSurface();
/*      */     
/* 1808 */     if (marker.getBridgeId() != -10L) {
/*      */ 
/*      */       
/* 1811 */       BridgePart bridgePart = Zones.getBridgePartFor(tilex, tiley, onSurface);
/* 1812 */       return new HighwayPos(tilex, tiley, onSurface, bridgePart, null);
/*      */     } 
/* 1814 */     if (marker.getFloorLevel() > 0) {
/*      */ 
/*      */       
/* 1817 */       Floor floor = Zones.getFloor(tilex, tiley, marker.isOnSurface(), marker.getFloorLevel());
/* 1818 */       return new HighwayPos(marker.getTileX(), marker.getTileY(), marker.isOnSurface(), null, floor);
/*      */     } 
/*      */     
/* 1821 */     return new HighwayPos(marker.getTileX(), marker.getTileY(), marker.isOnSurface(), null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getLinkDirString(byte linkdir) {
/* 1832 */     switch (linkdir) {
/*      */       
/*      */       case 1:
/* 1835 */         return "north";
/*      */       case 2:
/* 1837 */         return "northeast";
/*      */       case 4:
/* 1839 */         return "east";
/*      */       case 8:
/* 1841 */         return "southeast";
/*      */       case 16:
/* 1843 */         return "south";
/*      */       case 32:
/* 1845 */         return "southwest";
/*      */       case 64:
/* 1847 */         return "west";
/*      */       case -128:
/* 1849 */         return "northwest";
/*      */     } 
/* 1851 */     return "unknown(" + linkdir + ")";
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
/*      */   public static final boolean canPlantMarker(@Nullable Creature performer, HighwayPos currentHighwayPos, Item marker, byte possibleLinks) {
/* 1869 */     int cornerX = currentHighwayPos.getTilex();
/* 1870 */     int cornerY = currentHighwayPos.getTiley();
/* 1871 */     Village village = Villages.getVillagePlus(cornerX, cornerY, true, 2);
/* 1872 */     int pcount = numberOfSetBits(possibleLinks);
/* 1873 */     if (marker.getTemplateId() == 1112) {
/*      */ 
/*      */       
/* 1876 */       if (pcount == 0)
/*      */       {
/*      */         
/* 1879 */         if (village == null)
/*      */         {
/*      */           
/* 1882 */           if (performer != null)
/*      */           {
/* 1884 */             performer.getCommunicator().sendNormalServerMessage("Can only plant if there is an adjacent marker.");
/*      */           }
/* 1886 */           return false;
/*      */         }
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1893 */       if (pcount == 0) {
/*      */ 
/*      */         
/* 1896 */         if (performer != null)
/*      */         {
/* 1898 */           performer.getCommunicator().sendNormalServerMessage("Can only plant if there is an adjacent marker.");
/*      */         }
/* 1900 */         return false;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1905 */       if (pcount > 2) {
/*      */ 
/*      */         
/* 1908 */         if (performer != null)
/*      */         {
/* 1910 */           performer.getCommunicator().sendNormalServerMessage("Catseyes can only be planted if there is a maximum of two possible links.");
/*      */         }
/* 1912 */         return false;
/*      */       } 
/*      */     } 
/*      */     
/* 1916 */     if (performer != null) {
/*      */       
/* 1918 */       if (village != null) {
/*      */         
/* 1920 */         if (!village.isActionAllowed((short)176, performer)) {
/*      */           
/* 1922 */           performer.getCommunicator().sendNormalServerMessage("You do not have permission to plant a " + marker.getName() + " on (or next to) \"" + village
/* 1923 */               .getName() + "\".");
/* 1924 */           return false;
/*      */         } 
/* 1926 */         if (!village.isHighwayAllowed()) {
/*      */           
/* 1928 */           performer.getCommunicator().sendNormalServerMessage("\"" + village.getName() + "\" does not allow highways.");
/* 1929 */           return false;
/*      */         } 
/* 1931 */         if ((village.getReputations()).length > 0) {
/*      */           
/* 1933 */           performer.getCommunicator().sendNormalServerMessage("You cannot plant a " + marker.getName() + " on (or next to) \"" + village
/* 1934 */               .getName() + "\" as it has an active kos list.");
/* 1935 */           return false;
/*      */         } 
/*      */       } 
/* 1938 */       Skill skill = performer.getSkills().getSkillOrLearn(10031);
/* 1939 */       if (skill.getRealKnowledge() < 20.1D) {
/*      */         
/* 1941 */         performer.getCommunicator().sendNormalServerMessage("You do not have enough skill to plant that.");
/* 1942 */         return false;
/*      */       } 
/* 1944 */       if (!performer.isPaying()) {
/*      */         
/* 1946 */         performer.getCommunicator().sendNormalServerMessage("You need to be premium to plant that.");
/* 1947 */         return false;
/*      */       } 
/*      */ 
/*      */       
/* 1951 */       if (checkSlopes(currentHighwayPos)) {
/*      */         
/* 1953 */         if (performer != null)
/*      */         {
/* 1955 */           performer.getCommunicator().sendNormalServerMessage("This area is too sloped to allow highway markers.");
/*      */         }
/* 1957 */         return false;
/*      */       } 
/* 1959 */       HighwayPos highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)1);
/* 1960 */       if (highwayPos != null && checkSlopes(highwayPos)) {
/*      */         
/* 1962 */         if (performer != null)
/*      */         {
/* 1964 */           performer.getCommunicator().sendNormalServerMessage("North tile is too sloped to allow highway markers.");
/*      */         }
/* 1966 */         return false;
/*      */       } 
/* 1968 */       highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)2);
/* 1969 */       if (highwayPos != null && checkSlopes(currentHighwayPos)) {
/*      */         
/* 1971 */         if (performer != null)
/*      */         {
/* 1973 */           performer.getCommunicator().sendNormalServerMessage("North East tile is too sloped to allow highway markers.");
/*      */         }
/* 1975 */         return false;
/*      */       } 
/* 1977 */       highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)4);
/* 1978 */       if (highwayPos != null && checkSlopes(currentHighwayPos)) {
/*      */         
/* 1980 */         if (performer != null)
/*      */         {
/* 1982 */           performer.getCommunicator().sendNormalServerMessage("East tile is too sloped to allow highway markers.");
/*      */         }
/* 1984 */         return false;
/*      */       } 
/* 1986 */       highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)8);
/* 1987 */       if (highwayPos != null && checkSlopes(currentHighwayPos)) {
/*      */         
/* 1989 */         if (performer != null)
/*      */         {
/* 1991 */           performer.getCommunicator().sendNormalServerMessage("South East tile is too sloped to allow highway markers.");
/*      */         }
/* 1993 */         return false;
/*      */       } 
/* 1995 */       highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)16);
/* 1996 */       if (highwayPos != null && checkSlopes(currentHighwayPos)) {
/*      */         
/* 1998 */         if (performer != null)
/*      */         {
/* 2000 */           performer.getCommunicator().sendNormalServerMessage("South tile is too sloped to allow highway markers.");
/*      */         }
/* 2002 */         return false;
/*      */       } 
/* 2004 */       highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)32);
/* 2005 */       if (highwayPos != null && checkSlopes(currentHighwayPos)) {
/*      */         
/* 2007 */         if (performer != null)
/*      */         {
/* 2009 */           performer.getCommunicator().sendNormalServerMessage("South West tile is too sloped to allow highway markers.");
/*      */         }
/* 2011 */         return false;
/*      */       } 
/* 2013 */       highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)64);
/* 2014 */       if (highwayPos != null && checkSlopes(currentHighwayPos)) {
/*      */         
/* 2016 */         if (performer != null)
/*      */         {
/* 2018 */           performer.getCommunicator().sendNormalServerMessage("West tile is too sloped to allow highway markers.");
/*      */         }
/* 2020 */         return false;
/*      */       } 
/* 2022 */       highwayPos = getNewHighwayPosLinked(currentHighwayPos, -128);
/* 2023 */       if (highwayPos != null && checkSlopes(currentHighwayPos)) {
/*      */         
/* 2025 */         if (performer != null)
/*      */         {
/* 2027 */           performer.getCommunicator().sendNormalServerMessage("North West tile is too sloped to allow highway markers.");
/*      */         }
/* 2029 */         return false;
/*      */       } 
/*      */     } 
/* 2032 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean checkSlopes(HighwayPos highwayPos) {
/* 2037 */     if (highwayPos.isSurfaceTile()) {
/*      */       
/* 2039 */       MeshTile meshTile = new MeshTile(Server.surfaceMesh, highwayPos.getTilex(), highwayPos.getTiley());
/* 2040 */       if (Tiles.isRoadType(meshTile.getTileType()) && meshTile.checkSlopes(20, 28))
/*      */       {
/*      */         
/* 2043 */         return true;
/*      */       }
/*      */     } 
/* 2046 */     if (highwayPos.isCaveTile()) {
/*      */       
/* 2048 */       MeshTile meshTile = new MeshTile(Server.caveMesh, highwayPos.getTilex(), highwayPos.getTiley());
/* 2049 */       if (Tiles.isRoadType(meshTile.getTileType()) && meshTile.checkSlopes(20, 28))
/*      */       {
/*      */         
/* 2052 */         return true;
/*      */       }
/*      */     } 
/* 2055 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void removeNearbyMarkers(Floor floor) {
/* 2064 */     HighwayPos highwayPos = new HighwayPos(floor.getTileX(), floor.getTileY(), floor.isOnSurface(), null, floor);
/* 2065 */     removeNearbyMarkers(highwayPos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void removeNearbyMarkers(BridgePart bridgePart) {
/* 2075 */     HighwayPos highwayPos = new HighwayPos(bridgePart.getTileX(), bridgePart.getTileY(), bridgePart.isOnSurface(), bridgePart, null);
/* 2076 */     removeNearbyMarkers(highwayPos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void removeNearbyMarkers(int tilex, int tiley, boolean onSurface) {
/* 2086 */     HighwayPos highwayPos = new HighwayPos(tilex, tiley, onSurface, null, null);
/* 2087 */     removeNearbyMarkers(highwayPos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void removeNearbyMarkers(HighwayPos highwayPos) {
/* 2097 */     Item marker = getMarker(highwayPos);
/* 2098 */     if (marker != null) {
/* 2099 */       marker.setDamage(100.0F);
/*      */     }
/* 2101 */     removeNearbyMarker(highwayPos, (byte)1);
/* 2102 */     removeNearbyMarker(highwayPos, (byte)2);
/* 2103 */     removeNearbyMarker(highwayPos, (byte)4);
/* 2104 */     removeNearbyMarker(highwayPos, (byte)8);
/* 2105 */     removeNearbyMarker(highwayPos, (byte)16);
/* 2106 */     removeNearbyMarker(highwayPos, (byte)32);
/* 2107 */     removeNearbyMarker(highwayPos, (byte)64);
/* 2108 */     removeNearbyMarker(highwayPos, -128);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void removeNearbyMarker(HighwayPos currentHighwayPos, byte linkdir) {
/* 2119 */     HighwayPos highwayPos = getNewHighwayPosLinked(currentHighwayPos, (byte)1);
/* 2120 */     if (highwayPos != null) {
/*      */       
/* 2122 */       Item marker = getMarker(highwayPos);
/* 2123 */       if (marker != null)
/*      */       {
/*      */         
/* 2126 */         if (currentHighwayPos.getBridgeId() != -10L || currentHighwayPos.getFloorLevel() != 0) {
/*      */ 
/*      */           
/* 2129 */           if (marker.getBridgeId() != -10L || marker.getFloorLevel() != 0) {
/* 2130 */             marker.setDamage(100.0F);
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 2135 */         else if (marker.getBridgeId() == -10L || marker.getFloorLevel() == 0) {
/* 2136 */           marker.setDamage(100.0F);
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
/*      */   
/*      */   public static final byte convertLink(byte link) {
/* 2150 */     switch (link) {
/*      */       
/*      */       case 1:
/* 2153 */         return 0;
/*      */       case 2:
/* 2155 */         return 1;
/*      */       case 4:
/* 2157 */         return 2;
/*      */       case 8:
/* 2159 */         return 3;
/*      */       case 16:
/* 2161 */         return 4;
/*      */       case 32:
/* 2163 */         return 5;
/*      */       case 64:
/* 2165 */         return 6;
/*      */       case -128:
/* 2167 */         return 7;
/*      */     } 
/* 2169 */     return -1;
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
/*      */   public static final byte getOppositedir(byte fromdir) {
/* 2181 */     int lr4 = (fromdir & 0xFF) >>> 4;
/* 2182 */     int ll4 = (fromdir & 0xFF) << 4;
/* 2183 */     int lc4 = lr4 | ll4;
/* 2184 */     byte oppositedir = (byte)(lc4 & 0xFF);
/* 2185 */     return oppositedir;
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
/*      */   public static final byte getOtherdir(byte dirs, byte fromdir) {
/* 2197 */     byte otherdir = (byte)(dirs & (fromdir ^ 0xFFFFFFFF));
/* 2198 */     return otherdir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isNextToACamp(HighwayPos currentHighwayPos) {
/* 2207 */     Item marker = getMarker(currentHighwayPos, (byte)1);
/* 2208 */     if (marker != null)
/*      */     {
/* 2210 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2211 */         return true; 
/*      */     }
/* 2213 */     marker = getMarker(currentHighwayPos, (byte)2);
/* 2214 */     if (marker != null)
/*      */     {
/* 2216 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2217 */         return true; 
/*      */     }
/* 2219 */     marker = getMarker(currentHighwayPos, (byte)4);
/* 2220 */     if (marker != null)
/*      */     {
/* 2222 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2223 */         return true; 
/*      */     }
/* 2225 */     marker = getMarker(currentHighwayPos, (byte)8);
/* 2226 */     if (marker != null)
/*      */     {
/* 2228 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2229 */         return true; 
/*      */     }
/* 2231 */     marker = getMarker(currentHighwayPos, (byte)16);
/* 2232 */     if (marker != null)
/*      */     {
/* 2234 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2235 */         return true; 
/*      */     }
/* 2237 */     marker = getMarker(currentHighwayPos, (byte)32);
/* 2238 */     if (marker != null)
/*      */     {
/* 2240 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2241 */         return true; 
/*      */     }
/* 2243 */     marker = getMarker(currentHighwayPos, (byte)64);
/* 2244 */     if (marker != null)
/*      */     {
/* 2246 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2247 */         return true; 
/*      */     }
/* 2249 */     marker = getMarker(currentHighwayPos, -128);
/* 2250 */     if (marker != null)
/*      */     {
/* 2252 */       if (marker.getTemplateId() == 1112 && marker.getData() != -1L)
/* 2253 */         return true; 
/*      */     }
/* 2255 */     return false;
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
/*      */   public static final int numberOfSetBits(byte b) {
/* 2270 */     return Integer.bitCount(b & 0xFF);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\MethodsHighways.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
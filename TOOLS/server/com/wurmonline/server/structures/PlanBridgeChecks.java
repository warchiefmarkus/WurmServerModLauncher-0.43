/*      */ package com.wurmonline.server.structures;
/*      */ 
/*      */ import com.wurmonline.mesh.CaveTile;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.highways.MethodsHighways;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class PlanBridgeChecks
/*      */   implements MiscConstants
/*      */ {
/*      */   public static boolean passChecks(Creature performer, Point start, Point trueEnd, byte dir, int[] hts, boolean insta) {
/*   66 */     Point end = new Point(trueEnd);
/*   67 */     if (dir == 0) {
/*   68 */       end.setY(end.getY() + 1);
/*      */     } else {
/*   70 */       end.setX(end.getX() + 1);
/*      */     } 
/*   72 */     PlanBridgeCheckResult ans = checkPlanWidth(start, end, dir, performer.getLayer());
/*   73 */     if (!ans.failed())
/*   74 */       ans = checkForHighways(start, end, dir, performer.getLayer()); 
/*   75 */     if (!ans.failed())
/*   76 */       ans = checkClearance(start, end, dir, insta, performer.getLayer()); 
/*   77 */     if (!ans.failed() && performer.isOnSurface())
/*   78 */       ans = checkForFolliage(start, end, dir, insta); 
/*   79 */     if (!ans.failed() && !performer.isOnSurface())
/*   80 */       ans = checkCeilingClearance(start, end, dir, insta, performer.getLayer()); 
/*   81 */     if (!ans.failed())
/*   82 */       ans = checkForCaveEntrances(start, end, dir, insta); 
/*   83 */     if (!ans.failed())
/*   84 */       ans = checkForSpecialItems(start, end, dir, insta, performer.isOnSurface()); 
/*   85 */     if (!ans.failed())
/*   86 */       ans = checkForSettlements(performer, start, end, dir, insta, performer.isOnSurface()); 
/*   87 */     if (!ans.failed())
/*   88 */       ans = checkForBridges(start, end, dir, insta, performer.isOnSurface()); 
/*   89 */     if (!ans.failed()) {
/*   90 */       ans = checkForBuildings(start, end, dir, insta, performer.isOnSurface());
/*      */     }
/*   92 */     if (ans.failed()) {
/*      */       
/*   94 */       performer.getCommunicator().sendNormalServerMessage("Failed to plan bridge because of the following:");
/*   95 */       performer.getCommunicator().sendNormalServerMessage(ans.pMsg());
/*   96 */       return false;
/*      */     } 
/*      */     
/*   99 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult calcHeights(Creature performer, String targetName, Point near, Point far, Point diff, int len, boolean insta) {
/*  105 */     String qMsg = "";
/*  106 */     String rMsg = "";
/*  107 */     if (near.getH() < 1) {
/*      */       
/*  109 */       String pMsg = "Your end of the bridge is too close to the water level!";
/*  110 */       return new PlanBridgeCheckResult(true, "Your end of the bridge is too close to the water level!", qMsg);
/*      */     } 
/*  112 */     if (far.getH() < 1) {
/*      */       
/*  114 */       String pMsg = "Your helpers end of the bridge is too close to the water level!";
/*  115 */       return new PlanBridgeCheckResult(true, "Your helpers end of the bridge is too close to the water level!", qMsg);
/*      */     } 
/*      */     
/*  118 */     if (diff.getH() != 0) {
/*      */       
/*  120 */       if (near.getH() > far.getH()) {
/*  121 */         performer.getCommunicator().sendNormalServerMessage("You appear to be " + diff.getH() + " dirt higher than " + targetName + ".");
/*      */       } else {
/*  123 */         performer.getCommunicator().sendNormalServerMessage("You appear to be " + diff.getH() + " dirt lower than " + targetName + ".");
/*      */       } 
/*      */     } else {
/*  126 */       performer.getCommunicator().sendNormalServerMessage("Both ends are the same height, Nice!");
/*      */     } 
/*  128 */     if (insta) {
/*      */       
/*  130 */       qMsg = " (Slope:" + (diff.getH() / len) + ")";
/*  131 */       rMsg = " (20)";
/*      */     } 
/*      */     
/*  134 */     if (diff.getH() > 20 * len) {
/*      */       
/*  136 */       String pMsg = "You calculate the bridge slope" + qMsg + " and realise the slope would exceed the maximum" + rMsg + ".";
/*  137 */       return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */     } 
/*      */     
/*  140 */     return new PlanBridgeCheckResult(false);
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
/*      */   public static PlanBridgeCheckResult checkPlanWidth(Point start, Point end, byte dir, int layer) {
/*      */     Structure startBuilding, endBuilding;
/*  154 */     String qMsg = "";
/*      */ 
/*      */ 
/*      */     
/*  158 */     if (dir == 0) {
/*      */       
/*  160 */       startBuilding = Structures.getStructureForTile(start.getX(), start.getY() - 1, (layer == 0));
/*  161 */       endBuilding = Structures.getStructureForTile(end.getX(), end.getY(), (layer == 0));
/*      */     }
/*      */     else {
/*      */       
/*  165 */       startBuilding = Structures.getStructureForTile(start.getX() - 1, start.getY(), (layer == 0));
/*  166 */       endBuilding = Structures.getStructureForTile(end.getX(), end.getY(), (layer == 0));
/*      */     } 
/*      */     
/*  169 */     if (startBuilding != null) {
/*      */ 
/*      */       
/*  172 */       if (dir == 0) {
/*      */ 
/*      */         
/*  175 */         for (int x = start.getX() + 1; x <= end.getX(); x++)
/*      */         {
/*  177 */           Structure building = Structures.getStructureForTile(x, start.getY() - 1, (layer == 0));
/*  178 */           if (building != null && startBuilding.getWurmId() != building.getWurmId())
/*      */           {
/*      */             
/*  181 */             String pMsg = "You notice that the north bridge end does not butt all to same building all way along.";
/*  182 */             return new PlanBridgeCheckResult(true, "You notice that the north bridge end does not butt all to same building all way along.", "");
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  189 */         for (int y = start.getY() + 1; y <= end.getY(); y++)
/*      */         {
/*  191 */           Structure building = Structures.getStructureForTile(start.getX() - 1, y, (layer == 0));
/*  192 */           if (building != null && startBuilding.getWurmId() != building.getWurmId())
/*      */           {
/*      */             
/*  195 */             String pMsg = "You notice that the west bridge end does not butt all to same building all way along.";
/*  196 */             return new PlanBridgeCheckResult(true, "You notice that the west bridge end does not butt all to same building all way along.", "");
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  204 */     else if (dir == 0) {
/*      */ 
/*      */       
/*  207 */       for (int x = start.getX(); x <= end.getX() + 1; x++)
/*      */       {
/*      */         
/*  210 */         int ht = (int)(Zones.getHeightForNode(x, start.getY(), layer) * 10.0F);
/*  211 */         if (ht != start.getH())
/*      */         {
/*  213 */           if (x == start.getX()) {
/*      */ 
/*      */             
/*  216 */             String str = "You notice that the north west corner of the plan has changed height since the start of the planning phase.";
/*      */             
/*  218 */             return new PlanBridgeCheckResult(true, "You notice that the north west corner of the plan has changed height since the start of the planning phase.", "");
/*      */           } 
/*  220 */           String pMsg = "You notice that the north bridge end is not level for the width of the bridge.";
/*  221 */           return new PlanBridgeCheckResult(true, "You notice that the north bridge end is not level for the width of the bridge.", "");
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  228 */       for (int y = start.getY(); y <= end.getY() + 1; y++) {
/*      */ 
/*      */         
/*  231 */         int ht = (int)(Zones.getHeightForNode(start.getX(), y, layer) * 10.0F);
/*  232 */         if (ht != start.getH()) {
/*      */           
/*  234 */           if (y == start.getY()) {
/*      */ 
/*      */             
/*  237 */             String str = "You notice that the north west corner of the plan has changed height since the start of the planning phase.";
/*      */             
/*  239 */             return new PlanBridgeCheckResult(true, "You notice that the north west corner of the plan has changed height since the start of the planning phase.", "");
/*      */           } 
/*  241 */           String pMsg = "You notice that the west bridge end is not level for the width of the bridge.";
/*  242 */           return new PlanBridgeCheckResult(true, "You notice that the west bridge end is not level for the width of the bridge.", "");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  247 */     if (endBuilding != null) {
/*      */ 
/*      */       
/*  250 */       if (dir == 0) {
/*      */ 
/*      */         
/*  253 */         for (int x = start.getX() + 1; x <= end.getX(); x++)
/*      */         {
/*  255 */           Structure building = Structures.getStructureForTile(x, end.getY(), (layer == 0));
/*  256 */           if (building != null && endBuilding.getWurmId() != building.getWurmId())
/*      */           {
/*      */             
/*  259 */             String pMsg = "You notice that the south bridge end does not butt all to same building all way along.";
/*  260 */             return new PlanBridgeCheckResult(true, "You notice that the south bridge end does not butt all to same building all way along.", "");
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  267 */         for (int y = start.getY() + 1; y <= end.getY(); y++)
/*      */         {
/*  269 */           Structure building = Structures.getStructureForTile(end.getX(), y, (layer == 0));
/*  270 */           if (building != null && endBuilding.getWurmId() != building.getWurmId())
/*      */           {
/*      */             
/*  273 */             String pMsg = "You notice that the east bridge end does not butt all to same building all way along.";
/*  274 */             return new PlanBridgeCheckResult(true, "You notice that the east bridge end does not butt all to same building all way along.", "");
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  282 */     else if (dir == 0) {
/*      */ 
/*      */       
/*  285 */       for (int x = start.getX(); x <= end.getX() + 1; x++)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  290 */         int ht = (int)(Zones.getHeightForNode(x, end.getY(), layer) * 10.0F);
/*  291 */         if (ht != end.getH())
/*      */         {
/*  293 */           if (x == start.getX()) {
/*      */ 
/*      */             
/*  296 */             String str = "You notice that the south west corner of the plan has changed height since the start of the planning phase.";
/*      */             
/*  298 */             return new PlanBridgeCheckResult(true, "You notice that the south west corner of the plan has changed height since the start of the planning phase.", "");
/*      */           } 
/*  300 */           String pMsg = "You notice that the south bridge end is not level for the width of the bridge.";
/*  301 */           return new PlanBridgeCheckResult(true, "You notice that the south bridge end is not level for the width of the bridge.", "");
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  308 */       for (int y = start.getY(); y <= end.getY() + 1; y++) {
/*      */ 
/*      */         
/*  311 */         int ht = (int)(Zones.getHeightForNode(end.getX(), y, layer) * 10.0F);
/*  312 */         if (ht != end.getH()) {
/*      */           
/*  314 */           if (y == start.getY()) {
/*      */ 
/*      */             
/*  317 */             String str = "You notice that the south west corner of the plan has changed height since the start of the planning phase.";
/*      */             
/*  319 */             return new PlanBridgeCheckResult(true, "You notice that the south west corner of the plan has changed height since the start of the planning phase.", "");
/*      */           } 
/*  321 */           String pMsg = "You notice that the east bridge end is not level for the width of the bridge.";
/*  322 */           return new PlanBridgeCheckResult(true, "You notice that the east bridge end is not level for the width of the bridge.", "");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  328 */     return new PlanBridgeCheckResult(false);
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
/*      */   public static PlanBridgeCheckResult checkForHighways(Point start, Point end, byte dir, int layer) {
/*  343 */     MeshIO mesh = (layer == 0) ? Server.surfaceMesh : Server.caveMesh;
/*      */     
/*  345 */     if (dir == 0) {
/*      */ 
/*      */       
/*  348 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*      */         
/*  350 */         int encodedStartTile = mesh.getTile(x, start.getY());
/*  351 */         byte startType = Tiles.decodeType(encodedStartTile);
/*  352 */         int startHeight = Tiles.decodeHeight(encodedStartTile);
/*  353 */         int startOffset = start.getH() - startHeight;
/*      */         
/*  355 */         if (startOffset == 0 && isRoadTile(startType) && MethodsHighways.onHighway(x, start.getY(), (layer == 0))) {
/*      */           
/*  357 */           String pMsg = "Cannot build a bridge on a highway.";
/*  358 */           return new PlanBridgeCheckResult(true, "Cannot build a bridge on a highway.", "");
/*      */         } 
/*  360 */         int y = end.getY() - 1;
/*  361 */         int encodedEndTile = mesh.getTile(x, y);
/*  362 */         byte endType = Tiles.decodeType(encodedEndTile);
/*  363 */         int endHeight = Tiles.decodeHeight(encodedEndTile);
/*  364 */         int endOffset = end.getH() - endHeight;
/*      */         
/*  366 */         if (start.getY() != end.getY() && endOffset == 0 && isRoadTile(endType) && MethodsHighways.onHighway(x, y, (layer == 0)))
/*      */         {
/*  368 */           String pMsg = "Cannot build a bridge on a highway.";
/*  369 */           return new PlanBridgeCheckResult(true, "Cannot build a bridge on a highway.", "");
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/*  375 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*      */         
/*  377 */         int encodedStartTile = mesh.getTile(start.getX(), y);
/*  378 */         byte startType = Tiles.decodeType(encodedStartTile);
/*  379 */         int startHeight = Tiles.decodeHeight(encodedStartTile);
/*  380 */         int startOffset = start.getH() - startHeight;
/*      */         
/*  382 */         if (startOffset == 0 && isRoadTile(startType) && MethodsHighways.onHighway(start.getX(), y, (layer == 0))) {
/*      */           
/*  384 */           String pMsg = "Cannot build a bridge on a highway.";
/*  385 */           return new PlanBridgeCheckResult(true, "Cannot build a bridge on a highway.", "");
/*      */         } 
/*  387 */         int x = end.getX() - 1;
/*  388 */         int encodedEndTile = mesh.getTile(x, y);
/*  389 */         byte endType = Tiles.decodeType(encodedEndTile);
/*  390 */         int endHeight = Tiles.decodeHeight(encodedEndTile);
/*  391 */         int endOffset = end.getH() - endHeight;
/*      */         
/*  393 */         if (start.getX() != end.getX() && endOffset == 0 && isRoadTile(endType) && MethodsHighways.onHighway(x, y, (layer == 0))) {
/*      */           
/*  395 */           String pMsg = "Cannot build a bridge on a highway.";
/*  396 */           return new PlanBridgeCheckResult(true, "Cannot build a bridge on a highway.", "");
/*      */         } 
/*      */       } 
/*      */     } 
/*  400 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isRoadTile(byte type) {
/*  405 */     return (Tiles.isReinforcedFloor(type) || Tiles.isRoadType(type));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkClearance(Point start, Point end, byte dir, boolean insta, int layer) {
/*  411 */     int len = Math.max(end.getX() - start.getX(), end.getY() - start.getY());
/*  412 */     String qMsg = "";
/*  413 */     float ns = 0.0F;
/*  414 */     if (len == 1) {
/*      */       Structure startBuilding;
/*      */       
/*      */       Structure endBuilding;
/*      */       
/*  419 */       if (dir == 0) {
/*      */         
/*  421 */         startBuilding = Structures.getStructureForTile(start.getX(), start.getY() - 1, (layer == 0));
/*  422 */         endBuilding = Structures.getStructureForTile(end.getX(), end.getY(), (layer == 0));
/*      */       }
/*      */       else {
/*      */         
/*  426 */         startBuilding = Structures.getStructureForTile(start.getX() - 1, start.getY(), (layer == 0));
/*  427 */         endBuilding = Structures.getStructureForTile(end.getX(), end.getY(), (layer == 0));
/*      */       } 
/*  429 */       if (startBuilding == null && endBuilding == null)
/*      */       {
/*  431 */         String pMsg = "One end of both ends must connect to a building!";
/*  432 */         return new PlanBridgeCheckResult(true, "One end of both ends must connect to a building!", qMsg);
/*      */       }
/*      */     
/*  435 */     } else if (dir == 0) {
/*      */       
/*  437 */       float slope = (end.getH() - start.getH()) / len;
/*      */ 
/*      */       
/*  440 */       float pz = start.getH();
/*      */       
/*  442 */       for (int y = start.getY() + 1; y < end.getY(); y++) {
/*      */         
/*  444 */         ns += slope;
/*  445 */         pz = start.getH() + ns;
/*  446 */         for (int x = start.getX(); x <= end.getX() + 1; x++) {
/*      */ 
/*      */           
/*  449 */           int ht = (int)(Zones.getHeightForNode(x, y, layer) * 10.0F);
/*  450 */           if ((ht + 10) > pz)
/*      */           {
/*  452 */             if (insta)
/*      */             {
/*  454 */               qMsg = "(N-S) S:" + start.getX() + "," + start.getY() + "," + start.getH() + " U:" + end.getX() + "," + end.getY() + "," + end.getH() + " Failed at x:" + x + ", y:" + y + ", z:" + pz + ", h:" + ht + ", s:" + slope;
/*      */             }
/*  456 */             String pMsg = "Some terrain is in the way!";
/*  457 */             return new PlanBridgeCheckResult(true, "Some terrain is in the way!", qMsg);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  464 */       float slope = (end.getH() - start.getH()) / len;
/*      */       
/*  466 */       float pz = end.getH();
/*      */       
/*  468 */       for (int x = start.getX() + 1; x < end.getX(); x++) {
/*      */         
/*  470 */         ns += slope;
/*  471 */         pz = start.getH() + ns;
/*  472 */         for (int y = start.getY(); y <= end.getY() + 1; y++) {
/*      */ 
/*      */           
/*  475 */           int ht = (int)(Zones.getHeightForNode(x, y, layer) * 10.0F);
/*  476 */           if ((ht + 10) > pz) {
/*      */             
/*  478 */             if (insta)
/*      */             {
/*  480 */               qMsg = "(W-E) S:" + start.getX() + "," + start.getY() + "," + start.getH() + " U:" + end.getX() + "," + end.getY() + "," + end.getH() + " Failed at x:" + x + ", y:" + y + ", z:" + pz + ", h:" + ht + ", s:" + slope;
/*      */             }
/*  482 */             String pMsg = "Some terrain is in the way!";
/*  483 */             return new PlanBridgeCheckResult(true, "Some terrain is in the way!", qMsg);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  489 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkCeilingClearance(Point start, Point end, byte dir, boolean insta, int layer) {
/*  497 */     int len = Math.max(end.getX() - start.getX(), end.getY() - start.getY());
/*  498 */     String qMsg = "";
/*  499 */     float ns = 0.0F;
/*  500 */     if (len == 1)
/*      */     {
/*      */       
/*  503 */       return new PlanBridgeCheckResult(false);
/*      */     }
/*  505 */     if (dir == 0) {
/*      */       
/*  507 */       float slope = (end.getH() - start.getH()) / len;
/*      */ 
/*      */       
/*  510 */       float pz = start.getH();
/*      */       
/*  512 */       for (int y = start.getY() + 1; y < end.getY(); y++) {
/*      */         
/*  514 */         ns += slope;
/*      */         
/*  516 */         pz = start.getH() + ns;
/*  517 */         for (int x = start.getX(); x <= end.getX() + 1; x++) {
/*      */ 
/*      */           
/*  520 */           int ht = (int)(Zones.getHeightForNode(x, y, layer) * 10.0F);
/*  521 */           int encodedTile = Server.caveMesh.getTile(x, y);
/*  522 */           int cht = CaveTile.decodeCeilingHeight(encodedTile);
/*  523 */           int tht = ht + cht;
/*  524 */           if (tht < pz + 30.0F)
/*      */           {
/*  526 */             if (insta)
/*      */             {
/*  528 */               qMsg = "(N-S) S:" + start.getX() + "," + start.getY() + "," + start.getH() + " U:" + end.getX() + "," + end.getY() + "," + end.getH() + " Failed at x:" + x + ", y:" + y + ", z:" + pz + ", h:" + ht + ", s:" + slope + " Ceiling:" + cht + " Total:" + tht;
/*      */             }
/*      */             
/*  531 */             String pMsg = "Ceiling is in the way!";
/*  532 */             return new PlanBridgeCheckResult(true, "Ceiling is in the way!", qMsg);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  539 */       float slope = (end.getH() - start.getH()) / len;
/*      */       
/*  541 */       float pz = end.getH();
/*      */       
/*  543 */       for (int x = start.getX() + 1; x < end.getX(); x++) {
/*      */         
/*  545 */         ns += slope;
/*      */         
/*  547 */         pz = start.getH() + ns;
/*  548 */         for (int y = start.getY(); y <= end.getY() + 1; y++) {
/*      */ 
/*      */           
/*  551 */           int ht = (int)(Zones.getHeightForNode(x, y, layer) * 10.0F);
/*  552 */           int encodedTile = Server.caveMesh.getTile(x, y);
/*  553 */           int cht = CaveTile.decodeCeilingHeight(encodedTile);
/*  554 */           int tht = ht + cht;
/*  555 */           if (tht < pz + 30.0F) {
/*      */             
/*  557 */             if (insta)
/*      */             {
/*  559 */               qMsg = "(W-E) S:" + start.getX() + "," + start.getY() + "," + start.getH() + " U:" + end.getX() + "," + end.getY() + "," + end.getH() + " Failed at x:" + x + ", y:" + y + ", z:" + pz + ", h:" + ht + ", s:" + slope + " Ceiling:" + cht + " Total:" + tht;
/*      */             }
/*      */             
/*  562 */             String pMsg = "Ceiling is in the way!";
/*  563 */             return new PlanBridgeCheckResult(true, "Ceiling is in the way!", qMsg);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  569 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkForFolliage(Point start, Point end, byte dir, boolean insta) {
/*  574 */     String qMsg = "";
/*  575 */     if (dir == 0) {
/*      */       
/*  577 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*      */         
/*  579 */         for (int y = start.getY(); y < end.getY(); y++) {
/*      */ 
/*      */           
/*  582 */           byte ttype = Tiles.decodeType(Server.surfaceMesh.getTile(x, y));
/*  583 */           if (!Terraforming.isBridgeableTile(ttype))
/*      */           {
/*  585 */             if (insta)
/*  586 */               qMsg = " (@ " + x + "," + y + ")"; 
/*  587 */             String pMsg = (Tiles.getTile(ttype)).tiledesc + " is blocking the planning" + qMsg + ".";
/*  588 */             return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  595 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*      */         
/*  597 */         for (int x = start.getX(); x < end.getX(); x++) {
/*      */ 
/*      */           
/*  600 */           byte ttype = Tiles.decodeType(Server.surfaceMesh.getTile(x, y));
/*  601 */           if (!Terraforming.isBridgeableTile(ttype)) {
/*      */             
/*  603 */             if (insta)
/*  604 */               qMsg = " (@ " + x + "," + y + ")"; 
/*  605 */             String pMsg = (Tiles.getTile(ttype)).tiledesc + " is blocking the planning" + qMsg + ".";
/*  606 */             return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  611 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkForCaveEntrances(Point start, Point end, byte dir, boolean insta) {
/*  616 */     if (dir == 0) {
/*      */       
/*  618 */       for (int x = start.getX() - 1; x <= end.getX() + 1; x++) {
/*      */         
/*  620 */         for (int y = start.getY() - 1; y <= end.getY(); y++) {
/*      */           
/*  622 */           PlanBridgeCheckResult pbcr = checkForCaveEntrances(x, y, insta);
/*  623 */           if (pbcr != null) {
/*  624 */             return pbcr;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  630 */       for (int y = start.getY() - 1; y <= end.getY() + 1; y++) {
/*      */         
/*  632 */         for (int x = start.getX() - 1; x <= end.getX(); x++) {
/*      */           
/*  634 */           PlanBridgeCheckResult pbcr = checkForCaveEntrances(x, y, insta);
/*  635 */           if (pbcr != null)
/*  636 */             return pbcr; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  640 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static PlanBridgeCheckResult checkForCaveEntrances(int x, int y, boolean insta) {
/*  646 */     byte ttype = Tiles.decodeType(Server.surfaceMesh.getTile(x, y));
/*  647 */     if (Terraforming.isCaveEntrance(ttype)) {
/*      */       
/*  649 */       String qMsg = "";
/*  650 */       if (insta)
/*  651 */         qMsg = " (" + (Tiles.getTile(ttype)).tiledesc + " @ " + x + "," + y + ")"; 
/*  652 */       String pMsg = "There is a cave entrance too close to where the bridge would go" + qMsg + ".";
/*  653 */       return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */     } 
/*  655 */     if (ttype == Tiles.Tile.TILE_LAVA.id) {
/*      */       
/*  657 */       String qMsg = "";
/*  658 */       if (insta)
/*  659 */         qMsg = " (" + (Tiles.getTile(ttype)).tiledesc + " @ " + x + "," + y + ")"; 
/*  660 */       String pMsg = "You cannot plan a bridge over lava" + qMsg + ".";
/*  661 */       return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */     } 
/*  663 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkForSpecialItems(Point start, Point end, byte dir, boolean insta, boolean onSurface) {
/*  669 */     if (dir == 0) {
/*      */       
/*  671 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*      */         
/*  673 */         for (int y = start.getY(); y < end.getY(); y++) {
/*      */           
/*  675 */           PlanBridgeCheckResult pbcr = checkForSpecialItems(x, y, insta, onSurface);
/*  676 */           if (pbcr != null) {
/*  677 */             return pbcr;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  683 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*      */         
/*  685 */         for (int x = start.getX(); x < end.getX(); x++) {
/*      */           
/*  687 */           PlanBridgeCheckResult pbcr = checkForSpecialItems(x, y, insta, onSurface);
/*  688 */           if (pbcr != null)
/*  689 */             return pbcr; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  693 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static PlanBridgeCheckResult checkForSpecialItems(int x, int y, boolean insta, boolean onSurface) {
/*  699 */     String qMsg = "";
/*      */     
/*  701 */     VolaTile vt = Zones.getTileOrNull(x, y, onSurface);
/*  702 */     if (vt != null)
/*      */     {
/*  704 */       for (Item i : vt.getItems()) {
/*      */ 
/*      */         
/*  707 */         if (i.getTemplateId() == 236) {
/*      */           
/*  709 */           if (insta)
/*  710 */             qMsg = " (" + i.getName() + " @ " + x + "," + y + ")"; 
/*  711 */           String pMsg = "There would put a settlement token under the bridge which is not allowed" + qMsg + ".";
/*  712 */           return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */         } 
/*  714 */         if (i.isGuardTower()) {
/*      */           
/*  716 */           if (insta)
/*  717 */             qMsg = " (" + i.getName() + " @ " + x + "," + y + ")"; 
/*  718 */           String pMsg = "There would put a guard tower under the bridge which is not allowed" + qMsg + ".";
/*  719 */           return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */         } 
/*  721 */         if (i.isCorpse()) {
/*      */           
/*  723 */           if (insta)
/*  724 */             qMsg = " (" + i.getName() + " @ " + x + "," + y + ")"; 
/*  725 */           String pMsg = "There is a corpse under the plan, please remove it before trying again" + qMsg + ".";
/*  726 */           return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */         } 
/*      */       } 
/*      */     }
/*  730 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkForSettlements(Creature performer, Point start, Point end, byte dir, boolean insta, boolean onSurface) {
/*  736 */     if (dir == 0) {
/*      */ 
/*      */       
/*  739 */       for (int x = start.getX() - 1; x <= end.getX() + 1; x++) {
/*      */         
/*  741 */         for (int y = start.getY() - 1; y <= end.getY(); y++)
/*      */         {
/*  743 */           PlanBridgeCheckResult pbcr = checkForSettlements(performer, x, y, insta, onSurface);
/*  744 */           if (pbcr != null) {
/*  745 */             return pbcr;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/*  752 */       for (int y = start.getY() - 1; y <= end.getY() + 1; y++) {
/*      */         
/*  754 */         for (int x = start.getX() - 1; x <= end.getX(); x++) {
/*      */           
/*  756 */           PlanBridgeCheckResult pbcr = checkForSettlements(performer, x, y, insta, onSurface);
/*  757 */           if (pbcr != null)
/*  758 */             return pbcr; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  762 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static PlanBridgeCheckResult checkForSettlements(Creature performer, int x, int y, boolean insta, boolean onSurface) {
/*  768 */     String pMsg = "";
/*  769 */     String qMsg = "";
/*      */     
/*  771 */     Village vill = Villages.getVillage(x, y, onSurface);
/*  772 */     if (vill != null)
/*      */     {
/*  774 */       if (!vill.isActionAllowed((short)116, performer, false, 0, 0)) {
/*      */         
/*  776 */         if (insta)
/*  777 */           qMsg = " (@ " + x + "," + y + ")"; 
/*  778 */         if (vill.isEnemy(performer)) {
/*  779 */           pMsg = vill.getName() + " does not allow that" + qMsg + ".";
/*      */         } else {
/*  781 */           pMsg = "That would be illegal here. You can check the settlement token for the local laws" + qMsg + ".";
/*  782 */         }  return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */       } 
/*      */     }
/*  785 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkForPerimeters(Creature performer, Point start, Point end, byte dir, boolean insta, boolean onSurface) {
/*  795 */     if (dir == 0) {
/*      */       
/*  797 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*      */         
/*  799 */         for (int y = start.getY(); y < end.getY(); y++) {
/*      */           
/*  801 */           Village vill = Villages.getVillage(x, y, onSurface);
/*  802 */           if (vill == null) {
/*      */ 
/*      */             
/*  805 */             Village perim = Villages.getVillageWithPerimeterAt(x, y, onSurface);
/*  806 */             if (perim != null)
/*      */             {
/*  808 */               if (!perim.isCitizen(performer))
/*      */               {
/*  810 */                 if (!Methods.isActionAllowed(performer, (short)116, x, y))
/*      */                 {
/*  812 */                   String qMsg = "";
/*  813 */                   if (insta)
/*  814 */                     qMsg = " (@ " + x + "," + y + ")"; 
/*  815 */                   String pMsg = "The bridge will pass through " + perim.getName() + "'s perimeter where you do not have permission to build" + qMsg + ".";
/*      */                   
/*  817 */                   return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */                 }
/*      */               
/*      */               }
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  827 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*      */         
/*  829 */         for (int x = start.getX(); x < end.getX(); x++) {
/*      */           
/*  831 */           Village vill = Villages.getVillage(x, y, onSurface);
/*  832 */           if (vill == null) {
/*      */ 
/*      */             
/*  835 */             Village perim = Villages.getVillageWithPerimeterAt(x, y, onSurface);
/*  836 */             if (perim != null)
/*      */             {
/*  838 */               if (!perim.isCitizen(performer))
/*      */               {
/*  840 */                 if (!Methods.isActionAllowed(performer, (short)116, x, y)) {
/*      */                   
/*  842 */                   String qMsg = "";
/*  843 */                   if (insta)
/*  844 */                     qMsg = " (@ " + x + "," + y + ")"; 
/*  845 */                   String pMsg = "The bridge will pass through " + perim.getName() + "'s perimeter where you do not have permission to build" + qMsg + ".";
/*      */                   
/*  847 */                   return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */                 } 
/*      */               }
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  855 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkForBridges(Point start, Point end, byte dir, boolean insta, boolean onSurface) {
/*  861 */     if (dir == 0) {
/*      */       
/*  863 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*      */ 
/*      */         
/*  866 */         PlanBridgeCheckResult pbcrN = checkForBridges(x, end.getY() - 1, insta, onSurface);
/*  867 */         if (pbcrN != null) {
/*  868 */           return pbcrN;
/*      */         }
/*      */         
/*  871 */         PlanBridgeCheckResult pbcrS = checkForBridges(x, end.getY(), insta, onSurface);
/*  872 */         if (pbcrS != null) {
/*  873 */           return pbcrS;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/*  878 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*      */ 
/*      */         
/*  881 */         PlanBridgeCheckResult pbcrW = checkForBridges(start.getX() - 1, y, insta, onSurface);
/*  882 */         if (pbcrW != null) {
/*  883 */           return pbcrW;
/*      */         }
/*      */         
/*  886 */         PlanBridgeCheckResult pbcrE = checkForBridges(end.getX(), y, insta, onSurface);
/*  887 */         if (pbcrE != null)
/*  888 */           return pbcrE; 
/*      */       } 
/*      */     } 
/*  891 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static PlanBridgeCheckResult checkForBridges(int x, int y, boolean insta, boolean onSurface) {
/*  897 */     Structure building = Structures.getStructureForTile(x, y, onSurface);
/*  898 */     if (building != null && building.isTypeBridge()) {
/*      */       
/*  900 */       String qMsg = "";
/*  901 */       if (insta)
/*  902 */         qMsg = " (@ " + x + "," + y + ")"; 
/*  903 */       String pMsg = "There is an existing bridge '" + building.getName() + "'too close" + qMsg + " to where you are planning this bridge to go.";
/*      */       
/*  905 */       return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */     } 
/*  907 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkForBuildings(Point start, Point end, byte dir, boolean insta, boolean onSurfrace) {
/*  913 */     if (dir == 0) {
/*      */       
/*  915 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*      */         
/*  917 */         for (int y = start.getY(); y < end.getY(); y++) {
/*      */           
/*  919 */           PlanBridgeCheckResult pbcr = checkForBuildings(x, y, insta, onSurfrace);
/*  920 */           if (pbcr != null) {
/*  921 */             return pbcr;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  927 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*      */         
/*  929 */         for (int x = start.getX(); x < end.getX(); x++) {
/*      */           
/*  931 */           PlanBridgeCheckResult pbcr = checkForBuildings(x, y, insta, onSurfrace);
/*  932 */           if (pbcr != null)
/*  933 */             return pbcr; 
/*      */         } 
/*      */       } 
/*      */     } 
/*  937 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static PlanBridgeCheckResult checkForBuildings(int x, int y, boolean insta, boolean onSurfrace) {
/*  942 */     Structure building = Structures.getStructureForTile(x, y, onSurfrace);
/*  943 */     if (building != null) {
/*      */       
/*  945 */       String qMsg = "";
/*  946 */       if (insta)
/*  947 */         qMsg = " (@ " + x + "," + y + ")"; 
/*  948 */       String stype = building.isTypeHouse() ? "house" : "bridge";
/*  949 */       String pMsg = "There is a " + stype + " called '" + building.getName() + "'" + qMsg + " where you are trying to plan this bridge.";
/*      */       
/*  951 */       return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */     } 
/*  953 */     return null;
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
/*      */   public static PlanBridgeCheckResult checkForBuildingPermissions(Creature performer, int x, int y, boolean insta, boolean onSurface) {
/*  968 */     Structure building = Structures.getStructureForTile(x, y, onSurface);
/*  969 */     if (building != null && building.isTypeHouse())
/*      */     {
/*  971 */       if (!building.isGuest(performer)) {
/*      */         
/*  973 */         String qMsg = "";
/*  974 */         if (insta)
/*  975 */           qMsg = " (@ " + x + "," + y + ")"; 
/*  976 */         String pMsg = "You need to be a guest of the house called '" + building.getName() + "'" + qMsg + " to be able to plan this bridge.";
/*      */         
/*  978 */         return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */       } 
/*      */     }
/*  981 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkCeilingClearance(Creature performer, int len, Point start, Point end, byte dir, boolean insta) {
/*  987 */     if (!performer.isOnSurface()) {
/*      */       
/*  989 */       int[] hts = PlanBridgeMethods.calcArch(performer, 5, len, start, end);
/*  990 */       PlanBridgeCheckResult res = checkCeilingClearance(start, end, dir, hts, insta);
/*  991 */       if (res.failed()) {
/*      */         
/*  993 */         hts = PlanBridgeMethods.calcArch(performer, 10, len, start, end);
/*  994 */         res = checkCeilingClearance(start, end, dir, hts, insta);
/*      */       } 
/*  996 */       if (res.failed()) {
/*      */         
/*  998 */         hts = PlanBridgeMethods.calcArch(performer, 15, len, start, end);
/*  999 */         res = checkCeilingClearance(start, end, dir, hts, insta);
/*      */       } 
/* 1001 */       if (res.failed()) {
/*      */         
/* 1003 */         hts = PlanBridgeMethods.calcArch(performer, 20, len, start, end);
/* 1004 */         res = checkCeilingClearance(start, end, dir, hts, insta);
/*      */       } 
/* 1006 */       return res;
/*      */     } 
/* 1008 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlanBridgeCheckResult checkCeilingClearance(Point start, Point end, byte dir, int[] hts, boolean insta) {
/* 1015 */     if (dir == 0) {
/*      */       
/* 1017 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*      */         
/* 1019 */         for (int y = start.getY(); y < end.getY(); y++) {
/*      */ 
/*      */           
/* 1022 */           int encodedTile = Server.caveMesh.getTile(x, y);
/* 1023 */           int ht = Tiles.decodeHeight(encodedTile);
/* 1024 */           int cht = CaveTile.decodeCeilingHeight(encodedTile);
/* 1025 */           int tht = ht + cht;
/*      */           
/* 1027 */           if (hts[y - start.getY()] + 30 > tht)
/*      */           {
/* 1029 */             String qMsg = insta ? (" (@ " + x + "," + y + ")") : "";
/* 1030 */             String pMsg = "Ceiling too close" + qMsg + ".";
/* 1031 */             return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1038 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*      */         
/* 1040 */         for (int x = start.getX(); x < end.getX(); x++) {
/*      */ 
/*      */           
/* 1043 */           int encodedTile = Server.caveMesh.getTile(x, y);
/* 1044 */           int ht = Tiles.decodeHeight(encodedTile);
/* 1045 */           int cht = CaveTile.decodeCeilingHeight(encodedTile);
/* 1046 */           int tht = ht + cht;
/*      */           
/* 1048 */           if (hts[x - start.getX()] + 30 > tht) {
/*      */             
/* 1050 */             String qMsg = insta ? (" (@ " + x + "," + y + ")") : "";
/* 1051 */             String pMsg = "Ceiling too close" + qMsg + ".";
/* 1052 */             return new PlanBridgeCheckResult(true, pMsg, qMsg);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1057 */     return new PlanBridgeCheckResult(false);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\PlanBridgeChecks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
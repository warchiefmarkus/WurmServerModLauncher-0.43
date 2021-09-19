/*     */ package com.wurmonline.server.highways;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.support.Trello;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.webinterface.WcTrelloHighway;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.HighwayConstants;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Routes
/*     */   implements HighwayConstants
/*     */ {
/*  54 */   private static Logger logger = Logger.getLogger(Routes.class.getName());
/*  55 */   private static int nextId = 1;
/*  56 */   private static ConcurrentHashMap<Integer, Route> allRoutes = new ConcurrentHashMap<>();
/*  57 */   private static ConcurrentHashMap<Long, Node> allNodes = new ConcurrentHashMap<>();
/*  58 */   private static final ConcurrentLinkedDeque<PlayerMessageToSend> playerMessagesToSend = new ConcurrentLinkedDeque<>();
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
/*     */   public static final void generateAllRoutes() {
/*  71 */     logger.info("Calculating All routes.");
/*  72 */     long start = System.nanoTime();
/*     */ 
/*     */     
/*  75 */     for (Item waystone : Items.getWaystones())
/*     */     {
/*  77 */       makeNodeFrom(waystone);
/*     */     }
/*     */     
/*  80 */     for (Item waystone : Items.getWaystones())
/*     */     {
/*  82 */       checkForRoutes(waystone, false, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  87 */     for (Item waystone : Items.getWaystones()) {
/*     */       
/*  89 */       Node startNode = getNode(waystone);
/*  90 */       HighwayFinder.queueHighwayFinding(null, startNode, null, (byte)0);
/*     */     } 
/*     */     
/*  93 */     logger.log(Level.INFO, "Calculated " + allRoutes.size() + " routes and " + allNodes.size() + " nodes.That took " + (
/*  94 */         (float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/*     */ 
/*     */     
/*  97 */     Players.getInstance().sendGmMessage(null, "Roads", "Calculated " + allRoutes.size() + " routes and " + allNodes.size() + " nodes. That took " + (
/*  98 */         (float)(System.nanoTime() - start) / 1000000.0F) + " ms.", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean checkForRoutes(Item waystone, boolean tellGms, Item marker) {
/* 108 */     boolean foundRoute = false;
/* 109 */     foundRoute |= checkForRoute(waystone, (byte)1, tellGms, marker);
/* 110 */     foundRoute |= checkForRoute(waystone, (byte)2, tellGms, marker);
/* 111 */     foundRoute |= checkForRoute(waystone, (byte)4, tellGms, marker);
/* 112 */     foundRoute |= checkForRoute(waystone, (byte)8, tellGms, marker);
/* 113 */     foundRoute |= checkForRoute(waystone, (byte)16, tellGms, marker);
/* 114 */     foundRoute |= checkForRoute(waystone, (byte)32, tellGms, marker);
/* 115 */     foundRoute |= checkForRoute(waystone, (byte)64, tellGms, marker);
/* 116 */     foundRoute |= checkForRoute(waystone, -128, tellGms, marker);
/* 117 */     return foundRoute;
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
/*     */   @Nullable
/*     */   private static final boolean checkForRoute(Item waystone, byte checkdir, boolean tellGms, Item planted) {
/* 131 */     if (!MethodsHighways.hasLink(waystone.getAuxData(), checkdir)) {
/* 132 */       return false;
/*     */     }
/* 134 */     Node startNode = getNode(waystone);
/*     */     
/* 136 */     if (startNode.getRoute(checkdir) != null) {
/* 137 */       return false;
/*     */     }
/* 139 */     HighwayPos highwayPos = MethodsHighways.getHighwayPos(waystone);
/*     */     
/* 141 */     Route newRoute = new Route(startNode, checkdir, nextId);
/* 142 */     boolean checking = true;
/* 143 */     byte linkdir = checkdir;
/* 144 */     while (checking) {
/*     */ 
/*     */       
/* 147 */       int lastx = highwayPos.getTilex();
/* 148 */       int lasty = highwayPos.getTiley();
/* 149 */       boolean lastSurf = highwayPos.isOnSurface();
/* 150 */       long lastbp = highwayPos.getBridgeId();
/* 151 */       int lastfl = highwayPos.getFloorLevel();
/* 152 */       byte lastdir = linkdir;
/*     */       
/* 154 */       highwayPos = MethodsHighways.getNewHighwayPosLinked(highwayPos, linkdir);
/* 155 */       Item marker = MethodsHighways.getMarker(highwayPos);
/* 156 */       if (marker == null) {
/*     */ 
/*     */         
/* 159 */         logger.warning("Lost! " + MethodsHighways.getLinkAsString(lastdir) + " from:x:" + lastx + ",y:" + lasty + ",Surface:" + lastSurf + ",bp:" + lastbp + ",fl:" + lastfl);
/*     */         
/* 161 */         return false;
/*     */       } 
/* 163 */       byte fromdir = MethodsHighways.getOppositedir(linkdir);
/*     */       
/* 165 */       if (!MethodsHighways.hasLink(marker.getAuxData(), fromdir)) {
/*     */ 
/*     */         
/* 168 */         logger.info("Missing Link! " + MethodsHighways.getLinkAsString(lastdir) + " from:x:" + lastx + ",y:" + lasty + ",bp:" + lastbp + ",fl:" + lastfl + "  to:x" + highwayPos
/*     */             
/* 170 */             .getTilex() + ",y:" + highwayPos.getTiley() + "Surf:" + highwayPos.isOnSurface() + ",bp:" + highwayPos
/* 171 */             .getBridgeId() + ",fl:" + highwayPos.getFloorLevel());
/* 172 */         return false;
/*     */       } 
/*     */       
/* 175 */       if (marker.getTemplateId() == 1114) {
/*     */ 
/*     */         
/* 178 */         byte todir = MethodsHighways.getOtherdir(marker.getAuxData(), fromdir);
/*     */         
/* 180 */         newRoute.AddCatseye(marker, false, todir);
/* 181 */         if (MethodsHighways.numberOfSetBits(todir) != 1) {
/*     */ 
/*     */ 
/*     */           
/* 185 */           if (Servers.isThisATestServer())
/*     */           {
/* 187 */             logger.info("End of road! @" + marker.getTileX() + "," + marker.getTileY() + " (from:" + 
/* 188 */                 MethodsHighways.getLinkAsString(fromdir) + ",to:" + 
/* 189 */                 MethodsHighways.getLinkAsString(todir) + ")");
/*     */           }
/* 191 */           return false;
/*     */         } 
/* 193 */         linkdir = todir; continue;
/*     */       } 
/* 195 */       if (marker.getTemplateId() == 1112) {
/*     */ 
/*     */         
/* 198 */         Node endNode = getNode(marker);
/* 199 */         newRoute.AddEndNode(endNode);
/* 200 */         startNode.AddRoute(checkdir, newRoute);
/* 201 */         allRoutes.put(Integer.valueOf(newRoute.getId()), newRoute);
/*     */         
/* 203 */         LinkedList<Item> catseyes = new LinkedList<>();
/* 204 */         for (Item catseye : newRoute.getCatseyesList())
/* 205 */           catseyes.addFirst(catseye); 
/* 206 */         byte backdir = fromdir;
/* 207 */         Route backRoute = new Route(endNode, backdir, ++nextId);
/* 208 */         for (Item catseye : catseyes) {
/*     */           
/* 210 */           byte oppdir = MethodsHighways.getOppositedir(backdir);
/* 211 */           backRoute.AddCatseye(catseye, false, oppdir);
/* 212 */           backdir = MethodsHighways.getOtherdir(catseye.getAuxData(), oppdir);
/*     */         } 
/* 214 */         backRoute.AddEndNode(startNode);
/* 215 */         endNode.AddRoute(fromdir, backRoute);
/* 216 */         allRoutes.put(Integer.valueOf(backRoute.getId()), backRoute);
/*     */         
/* 218 */         newRoute.SetOppositeRoute(backRoute);
/* 219 */         backRoute.SetOppositeRoute(newRoute);
/*     */ 
/*     */         
/* 222 */         if (tellGms) {
/*     */           
/* 224 */           waystone.updateModelNameOnGroundItem();
/* 225 */           for (Item catseye : newRoute.getCatseyes())
/* 226 */             catseye.updateModelNameOnGroundItem(); 
/* 227 */           marker.updateModelNameOnGroundItem();
/*     */ 
/*     */ 
/*     */           
/* 231 */           HighwayFinder.queueHighwayFinding(null, startNode, null, checkdir);
/* 232 */           HighwayFinder.queueHighwayFinding(null, endNode, null, fromdir);
/*     */         } 
/* 234 */         nextId++;
/*     */         
/* 236 */         checking = false;
/* 237 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 242 */       return false;
/*     */     } 
/*     */     
/* 245 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Node getNode(Item waystone) {
/* 256 */     Node node = allNodes.get(Long.valueOf(waystone.getWurmId()));
/* 257 */     if (node != null) {
/* 258 */       return node;
/*     */     }
/* 260 */     return makeNodeFrom(waystone);
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
/*     */   private static final Node makeNodeFrom(Item waystone) {
/* 272 */     Node node = new Node(waystone);
/* 273 */     VolaTile vt = Zones.getTileOrNull(waystone.getTileX(), waystone.getTileY(), waystone.isOnSurface());
/* 274 */     if (vt != null && vt.getVillage() != null)
/* 275 */       node.setVillage(vt.getVillage()); 
/* 276 */     allNodes.put(Long.valueOf(waystone.getWurmId()), node);
/* 277 */     return node;
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
/*     */   public static final void remove(Item marker) {
/* 311 */     if (marker.getTemplateId() == 1114) {
/*     */       
/* 313 */       for (Map.Entry<Integer, Route> entry : allRoutes.entrySet()) {
/*     */         
/* 315 */         if (((Route)entry.getValue()).containsCatseye(marker)) {
/*     */ 
/*     */           
/* 318 */           removeRoute(entry.getValue(), marker);
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 325 */       Node node = allNodes.remove(Long.valueOf(marker.getWurmId()));
/* 326 */       if (node != null) {
/*     */ 
/*     */         
/* 329 */         removeRoute(node, (byte)1, marker);
/* 330 */         removeRoute(node, (byte)2, marker);
/* 331 */         removeRoute(node, (byte)4, marker);
/* 332 */         removeRoute(node, (byte)8, marker);
/* 333 */         removeRoute(node, (byte)16, marker);
/* 334 */         removeRoute(node, (byte)32, marker);
/* 335 */         removeRoute(node, (byte)64, marker);
/* 336 */         removeRoute(node, -128, marker);
/*     */       } 
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
/*     */   private static final void removeRoute(Node node, byte checkdir, Item marker) {
/* 349 */     Route route = node.getRoute(checkdir);
/* 350 */     if (route != null) {
/* 351 */       removeRoute(route, marker);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void removeRoute(Route route, Item marker) {
/* 360 */     Node nodeStart = route.getStartNode();
/* 361 */     Node nodeEnd = route.getEndNode();
/* 362 */     boolean doCatseyes = nodeStart.removeRoute(route);
/* 363 */     allRoutes.remove(Integer.valueOf(route.getId()));
/* 364 */     Route oppRoute = route.getOppositeRoute();
/* 365 */     if (oppRoute != null) {
/*     */       
/* 367 */       Node oppStart = oppRoute.getStartNode();
/* 368 */       doCatseyes |= oppStart.removeRoute(oppRoute);
/* 369 */       allRoutes.remove(Integer.valueOf(oppRoute.getId()));
/*     */     } 
/* 371 */     if (doCatseyes) {
/*     */ 
/*     */ 
/*     */       
/* 375 */       nodeStart.getWaystone().updateModelNameOnGroundItem();
/* 376 */       for (Item catseye : route.getCatseyes())
/* 377 */         catseye.updateModelNameOnGroundItem(); 
/* 378 */       if (nodeEnd != null) {
/* 379 */         nodeEnd.getWaystone().updateModelNameOnGroundItem();
/*     */       }
/*     */     } 
/*     */     
/* 383 */     if (!marker.isReplacing()) {
/*     */ 
/*     */       
/* 386 */       String whatHappened = marker.getWhatHappened();
/* 387 */       if (whatHappened.length() == 0) {
/* 388 */         whatHappened = "unknown";
/*     */       }
/* 390 */       StringBuffer ttl = new StringBuffer();
/* 391 */       ttl.append(marker.getName());
/* 392 */       ttl.append(" @");
/* 393 */       ttl.append(marker.getTileX());
/* 394 */       ttl.append(",");
/* 395 */       ttl.append(marker.getTileY());
/* 396 */       ttl.append(",");
/* 397 */       ttl.append(marker.isOnSurface());
/* 398 */       ttl.append(" ");
/* 399 */       ttl.append(whatHappened);
/* 400 */       String title = ttl.toString();
/*     */       
/* 402 */       StringBuffer dsc = new StringBuffer();
/* 403 */       dsc.append("Routes removed between ");
/* 404 */       dsc.append(nodeStart.getWaystone().getTileX());
/* 405 */       dsc.append(",");
/* 406 */       dsc.append(nodeStart.getWaystone().getTileY());
/* 407 */       dsc.append(",");
/* 408 */       dsc.append(nodeStart.getWaystone().isOnSurface());
/* 409 */       dsc.append(" and ");
/* 410 */       if (nodeEnd != null) {
/*     */         
/* 412 */         dsc.append(nodeEnd.getWaystone().getTileX());
/* 413 */         dsc.append(",");
/* 414 */         dsc.append(nodeEnd.getWaystone().getTileY());
/* 415 */         dsc.append(",");
/* 416 */         dsc.append(nodeEnd.getWaystone().isOnSurface());
/*     */       } else {
/*     */         
/* 419 */         dsc.append(" end node missing!");
/* 420 */       }  String description = dsc.toString();
/*     */       
/* 422 */       sendToTrello(title, description);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendToTrello(String title, String description) {
/* 428 */     Players.getInstance().sendGmMessage(null, "Roads", title, false);
/* 429 */     if (Servers.isThisLoginServer()) {
/*     */       
/* 431 */       Trello.addHighwayMessage(Servers.localServer.getAbbreviation(), title, description);
/*     */     }
/*     */     else {
/*     */       
/* 435 */       WcTrelloHighway wtc = new WcTrelloHighway(title, description);
/* 436 */       wtc.sendToLoginServer();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean checkForNewRoutes(Item marker) {
/* 447 */     if (marker.getTemplateId() == 1112) {
/*     */ 
/*     */       
/* 450 */       getNode(marker);
/*     */       
/* 452 */       return checkForRoutes(marker, true, marker);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 457 */     if (MethodsHighways.numberOfSetBits(marker.getAuxData()) == 2) {
/*     */ 
/*     */ 
/*     */       
/* 461 */       byte startdir = getStartdir(marker);
/*     */       
/* 463 */       if (startdir != 0) {
/*     */         
/* 465 */         Set<Item> markersDone = new HashSet<>();
/* 466 */         HighwayPos highwayPos = MethodsHighways.getHighwayPos(marker);
/* 467 */         boolean checking = true;
/* 468 */         byte linkdir = startdir;
/* 469 */         while (checking) {
/*     */ 
/*     */           
/* 472 */           int lastx = highwayPos.getTilex();
/* 473 */           int lasty = highwayPos.getTiley();
/* 474 */           boolean lastSurf = highwayPos.isOnSurface();
/* 475 */           long lastbp = highwayPos.getBridgeId();
/* 476 */           int lastfl = highwayPos.getFloorLevel();
/* 477 */           byte lastdir = linkdir;
/*     */           
/* 479 */           highwayPos = MethodsHighways.getNewHighwayPosLinked(highwayPos, linkdir);
/* 480 */           Item nextMarker = MethodsHighways.getMarker(highwayPos);
/* 481 */           if (nextMarker == null) {
/*     */ 
/*     */             
/* 484 */             logger.warning("Dead End! " + MethodsHighways.getLinkAsString(lastdir) + " from:x:" + lastx + ",y:" + lasty + ",Surface:" + lastSurf + ",bp:" + lastbp + ",fl:" + lastfl);
/*     */             
/* 486 */             return false;
/*     */           } 
/* 488 */           if (markersDone.contains(nextMarker)) {
/*     */ 
/*     */             
/* 491 */             logger.warning("Circular! " + MethodsHighways.getLinkAsString(lastdir) + " from:x:" + lastx + ",y:" + lasty + ",Surface:" + lastSurf + ",bp:" + lastbp + ",fl:" + lastfl);
/*     */             
/* 493 */             return false;
/*     */           } 
/* 495 */           markersDone.add(nextMarker);
/* 496 */           byte fromdir = MethodsHighways.getOppositedir(linkdir);
/* 497 */           if (MethodsHighways.numberOfSetBits(fromdir) != 1) {
/*     */ 
/*     */             
/* 500 */             logger.warning("Lost! " + MethodsHighways.getLinkAsString(lastdir) + " from:x:" + lastx + ",y:" + lasty + ",Surface:" + lastSurf + ",bp:" + lastbp + ",fl:" + lastfl);
/*     */             
/* 502 */             return false;
/*     */           } 
/*     */           
/* 505 */           if (!MethodsHighways.hasLink(nextMarker.getAuxData(), fromdir)) {
/*     */ 
/*     */             
/* 508 */             logger.info("Missing Link! " + MethodsHighways.getLinkAsString(lastdir) + " from:x:" + lastx + ",y:" + lasty + ",bp:" + lastbp + ",fl:" + lastfl + "  to:x" + highwayPos
/*     */                 
/* 510 */                 .getTilex() + ",y:" + highwayPos.getTiley() + "Surf:" + highwayPos.isOnSurface() + ",bp:" + highwayPos
/* 511 */                 .getBridgeId() + ",fl:" + highwayPos.getFloorLevel());
/* 512 */             return false;
/*     */           } 
/*     */           
/* 515 */           if (nextMarker.getTemplateId() == 1114) {
/*     */ 
/*     */             
/* 518 */             byte todir = MethodsHighways.getOtherdir(nextMarker.getAuxData(), fromdir);
/*     */ 
/*     */             
/* 521 */             if (MethodsHighways.numberOfSetBits(todir) != 1) {
/*     */ 
/*     */ 
/*     */               
/* 525 */               if (Servers.isThisATestServer())
/*     */               {
/* 527 */                 logger.info("End of road! @" + nextMarker.getTileX() + "," + nextMarker.getTileY() + " (from:" + 
/* 528 */                     MethodsHighways.getLinkAsString(fromdir) + ",to:" + 
/* 529 */                     MethodsHighways.getLinkAsString(todir) + ")");
/*     */               }
/* 531 */               return false;
/*     */             } 
/* 533 */             linkdir = todir; continue;
/*     */           } 
/* 535 */           if (nextMarker.getTemplateId() == 1112) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 541 */             checking = false;
/* 542 */             return checkForRoute(nextMarker, fromdir, true, marker);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 547 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 553 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final byte getStartdir(Item marker) {
/* 558 */     byte startdir = 0;
/* 559 */     byte dirs = marker.getAuxData();
/* 560 */     if (MethodsHighways.hasLink(dirs, (byte)1)) {
/* 561 */       startdir = 1;
/* 562 */     } else if (MethodsHighways.hasLink(dirs, (byte)2)) {
/* 563 */       startdir = 2;
/* 564 */     } else if (MethodsHighways.hasLink(dirs, (byte)4)) {
/* 565 */       startdir = 4;
/* 566 */     } else if (MethodsHighways.hasLink(dirs, (byte)8)) {
/* 567 */       startdir = 8;
/* 568 */     } else if (MethodsHighways.hasLink(dirs, (byte)16)) {
/* 569 */       startdir = 16;
/* 570 */     } else if (MethodsHighways.hasLink(dirs, (byte)32)) {
/* 571 */       startdir = 32;
/* 572 */     } else if (MethodsHighways.hasLink(dirs, (byte)64)) {
/* 573 */       startdir = 64;
/* 574 */     } else if (MethodsHighways.hasLink(dirs, -128)) {
/* 575 */       startdir = Byte.MIN_VALUE;
/* 576 */     }  return startdir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Item[] getMarkers() {
/* 586 */     ConcurrentHashMap<Long, Item> markers = new ConcurrentHashMap<>();
/*     */     
/* 588 */     for (Route route : allRoutes.values()) {
/*     */       
/* 590 */       Item waystone = route.getStartNode().getWaystone();
/* 591 */       markers.put(Long.valueOf(waystone.getWurmId()), waystone);
/* 592 */       for (Item catseye : route.getCatseyes())
/*     */       {
/* 594 */         markers.put(Long.valueOf(catseye.getWurmId()), catseye);
/*     */       }
/* 596 */       Node node = route.getEndNode();
/*     */       
/* 598 */       if (node != null)
/*     */       {
/* 600 */         markers.put(Long.valueOf(node.getWaystone().getWurmId()), node.getWaystone());
/*     */       }
/*     */     } 
/* 603 */     return (Item[])markers.values().toArray((Object[])new Item[markers.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Item[] getRouteMarkers(Item marker) {
/* 614 */     ConcurrentHashMap<Long, Item> markers = new ConcurrentHashMap<>();
/* 615 */     if (marker.getTemplateId() == 1114) {
/*     */       
/* 617 */       for (Route route : allRoutes.values()) {
/*     */         
/* 619 */         if (route.containsCatseye(marker)) {
/*     */           
/* 621 */           Item startWaystone = route.getStartNode().getWaystone();
/* 622 */           markers.put(Long.valueOf(startWaystone.getWurmId()), startWaystone);
/* 623 */           for (Item catseye : route.getCatseyes())
/* 624 */             markers.put(Long.valueOf(catseye.getWurmId()), catseye); 
/* 625 */           Item endWaystone = route.getEndNode().getWaystone();
/* 626 */           markers.put(Long.valueOf(endWaystone.getWurmId()), endWaystone);
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 634 */       for (Route route : allRoutes.values()) {
/*     */         
/* 636 */         if (route.getStartNode().getWurmId() == marker.getWurmId()) {
/*     */           
/* 638 */           Item startWaystone = route.getStartNode().getWaystone();
/* 639 */           markers.put(Long.valueOf(startWaystone.getWurmId()), startWaystone);
/* 640 */           for (Item catseye : route.getCatseyes())
/* 641 */             markers.put(Long.valueOf(catseye.getWurmId()), catseye); 
/* 642 */           Item endWaystone = route.getEndNode().getWaystone();
/* 643 */           markers.put(Long.valueOf(endWaystone.getWurmId()), endWaystone);
/*     */         } 
/* 645 */         if (route.getEndNode().getWurmId() == marker.getWurmId()) {
/*     */           
/* 647 */           Item startWaystone = route.getStartNode().getWaystone();
/* 648 */           markers.put(Long.valueOf(startWaystone.getWurmId()), startWaystone);
/* 649 */           for (Item catseye : route.getCatseyes())
/* 650 */             markers.put(Long.valueOf(catseye.getWurmId()), catseye); 
/* 651 */           Item endWaystone = route.getEndNode().getWaystone();
/* 652 */           markers.put(Long.valueOf(endWaystone.getWurmId()), endWaystone);
/*     */         } 
/*     */       } 
/*     */     } 
/* 656 */     return (Item[])markers.values().toArray((Object[])new Item[markers.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isCatseyeUsed(Item catseye) {
/* 667 */     for (Route route : allRoutes.values()) {
/*     */       
/* 669 */       if (route.containsCatseye(catseye))
/* 670 */         return true; 
/*     */     } 
/* 672 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isMarkerUsed(Item marker) {
/* 683 */     if (marker.getTemplateId() == 1114)
/*     */     {
/* 685 */       return isCatseyeUsed(marker);
/*     */     }
/* 687 */     for (Route route : allRoutes.values()) {
/*     */       
/* 689 */       if (route.getStartNode().getWaystone().getWurmId() == marker.getWurmId())
/* 690 */         return true; 
/* 691 */       if (route.getEndNode() != null && route.getEndNode().getWaystone().getWurmId() == marker.getWurmId())
/* 692 */         return true; 
/*     */     } 
/* 694 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static final Route getRoute(int id) {
/* 706 */     return allRoutes.get(Integer.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static final Node getNode(long wurmId) {
/* 718 */     return allNodes.get(Long.valueOf(wurmId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Route[] getAllRoutes() {
/* 728 */     return (Route[])allRoutes.values().toArray((Object[])new Route[allRoutes.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Node[] getAllNodes() {
/* 738 */     return (Node[])allNodes.values().toArray((Object[])new Node[allNodes.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Village[] getVillages() {
/* 748 */     ConcurrentHashMap<Integer, Village> villages = new ConcurrentHashMap<>();
/* 749 */     for (Node node : allNodes.values()) {
/*     */       
/* 751 */       Village vill = node.getVillage();
/* 752 */       if (vill != null && vill.isHighwayFound())
/*     */       {
/* 754 */         villages.put(Integer.valueOf(vill.getId()), vill);
/*     */       }
/*     */     } 
/* 757 */     return (Village[])villages.values().toArray((Object[])new Village[villages.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Village[] getVillages(long waystoneId) {
/* 767 */     ConcurrentHashMap<Integer, Village> villages = new ConcurrentHashMap<>();
/* 768 */     for (Node node : allNodes.values()) {
/*     */       
/* 770 */       Village vill = node.getVillage();
/* 771 */       if (vill != null && vill.isHighwayFound())
/*     */       {
/* 773 */         if (PathToCalculate.isVillageConnected(waystoneId, vill))
/* 774 */           villages.put(Integer.valueOf(vill.getId()), vill); 
/*     */       }
/*     */     } 
/* 777 */     return (Village[])villages.values().toArray((Object[])new Village[villages.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Node[] getNodesFor(Village village) {
/* 787 */     ConcurrentHashMap<Long, Node> nodes = new ConcurrentHashMap<>();
/* 788 */     for (Node node : allNodes.values()) {
/*     */       
/* 790 */       Village vill = node.getVillage();
/* 791 */       if (vill != null && vill.equals(village))
/*     */       {
/* 793 */         nodes.put(Long.valueOf(node.getWaystone().getWurmId()), node);
/*     */       }
/*     */     } 
/* 796 */     return (Node[])nodes.values().toArray((Object[])new Node[nodes.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void handlePathsToSend() {
/* 804 */     PlayerMessageToSend playerMessageToSend = playerMessagesToSend.pollFirst();
/* 805 */     while (playerMessageToSend != null) {
/*     */       
/* 807 */       playerMessageToSend.send();
/* 808 */       playerMessageToSend = playerMessagesToSend.pollFirst();
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
/*     */   public static final void queuePlayerMessage(Player player, String text) {
/* 820 */     playerMessagesToSend.add(new PlayerMessageToSend(player, text));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\Routes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
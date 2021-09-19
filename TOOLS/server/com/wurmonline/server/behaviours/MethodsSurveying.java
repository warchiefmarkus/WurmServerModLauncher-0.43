/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.PlanBridgeCheckResult;
/*      */ import com.wurmonline.server.structures.PlanBridgeChecks;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.zones.Zones;
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
/*      */ public final class MethodsSurveying
/*      */   implements MiscConstants
/*      */ {
/*      */   public static boolean planBridge(Action act, Creature performer, Item source, Creature helper, @Nullable Item target, short action, float counter) {
/*   51 */     if (!Features.Feature.CAVE_BRIDGES.isEnabled()) {
/*      */       
/*   53 */       if (!performer.isOnSurface()) {
/*      */         
/*   55 */         performer.getCommunicator().sendNormalServerMessage("You must be on the surface to plan a bridge.");
/*   56 */         return true;
/*      */       } 
/*      */       
/*   59 */       if (target != null && !target.isOnSurface()) {
/*      */         
/*   61 */         performer.getCommunicator().sendNormalServerMessage("The range pole must be on the surface, so you can use it as a target, to plan a bridge.");
/*      */         
/*   63 */         return true;
/*      */       } 
/*   65 */       if (helper != null && !helper.isOnSurface()) {
/*      */         
/*   67 */         performer.getCommunicator().sendNormalServerMessage(helper
/*   68 */             .getName() + " must be on the surface to help you plan a bridge.");
/*   69 */         return true;
/*      */       } 
/*      */     } 
/*   72 */     if (target != null && !target.isPlanted()) {
/*      */       
/*   74 */       performer.getCommunicator().sendNormalServerMessage("The range pole must be planted to stop it swaying, to help plan a bridge.");
/*      */       
/*   76 */       return true;
/*      */     } 
/*   78 */     if (performer.getBuildingId() != -10L) {
/*      */       
/*   80 */       Structure plannedStructure = null;
/*      */       
/*      */       try {
/*   83 */         plannedStructure = performer.getStructure();
/*      */         
/*   85 */         if (plannedStructure.isTypeBridge()) {
/*   86 */           performer.getCommunicator().sendNormalServerMessage("You cannot design a bridge as your mind keeps reverting back to the bridge \"" + plannedStructure
/*      */               
/*   88 */               .getName() + "\" that you are currently constructing.");
/*   89 */         } else if (plannedStructure.isFinalized()) {
/*   90 */           performer.getCommunicator().sendNormalServerMessage("You cannot design a bridge as your mind keeps reverting back to the house \"" + plannedStructure
/*      */               
/*   92 */               .getName() + "\" that you are currently constructing.");
/*      */         } else {
/*   94 */           performer.getCommunicator().sendNormalServerMessage("You cannot design a bridge as your mind keeps reverting back to the house that you are currently in the process of planning.");
/*      */         }
/*      */       
/*      */       }
/*   98 */       catch (NoSuchStructureException nse) {
/*      */ 
/*      */         
/*  101 */         performer.getCommunicator().sendNormalServerMessage("You are already planning a building or a bridge, finish it before planning this bridge.");
/*      */       } 
/*      */       
/*  104 */       return true;
/*      */     } 
/*  106 */     if (performer.getVehicle() != -10L) {
/*      */       
/*  108 */       performer.getCommunicator().sendNormalServerMessage("A vehicle is not a steady enough platform to plan a bridge from.");
/*      */       
/*  110 */       return true;
/*      */     } 
/*  112 */     boolean done = false;
/*      */     
/*  114 */     boolean insta = (Servers.localServer.testServer && performer.getPower() > 1);
/*      */     
/*  116 */     int rangeMeters = (helper != null) ? Creature.rangeTo(performer, helper) : Creature.rangeTo(performer, target);
/*      */     
/*  118 */     int timePerStage = insta ? 2 : (rangeMeters / 8 + 2);
/*  119 */     int time = timePerStage * 170;
/*  120 */     if (counter == 1.0F) {
/*      */       
/*  122 */       act.setTimeLeft(time);
/*  123 */       performer.sendActionControl(Actions.actionEntrys[637].getVerbString(), true, time);
/*  124 */       performer.getCommunicator().sendNormalServerMessage("You carefully place the dioptra on its tripod in front of you.");
/*      */       
/*  126 */       Server.getInstance().broadCastAction(performer.getName() + " starts to plan a bridge.", performer, 5);
/*  127 */       act.setTickCount(0);
/*  128 */       if (insta) {
/*  129 */         performer.getCommunicator().sendNormalServerMessage("(Note: Anything shown in brackets will be shown on test server for GM+ only.)");
/*      */       }
/*      */     } else {
/*      */       
/*  133 */       time = act.getTimeLeft();
/*      */     } 
/*      */ 
/*      */     
/*  137 */     if (helper != null && !insta && act.getTickCount() > 5 && act.getTickCount() < 12 && 
/*  138 */       !isHoldingRangePole(helper)) {
/*      */       
/*  140 */       performer.getCommunicator().sendNormalServerMessage(helper.getName() + " is not holding a range pole.");
/*  141 */       done = true;
/*      */     } 
/*      */     
/*  144 */     if (!done && act.currentSecond() % timePerStage == 0) {
/*      */       PlanBridgeCheckResult ans; float maxDioptraTiles; Item item; float poleQL, maxPoleTiles;
/*  146 */       act.incTickCount();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  193 */       float hX = (helper != null) ? helper.getStatus().getPositionX() : target.getPosX();
/*  194 */       float hY = (helper != null) ? helper.getStatus().getPositionY() : target.getPosY();
/*  195 */       double rotRads = Math.atan2((performer.getStatus().getPositionY() - hY), (performer
/*  196 */           .getStatus().getPositionX() - hX));
/*  197 */       float rot = (float)(rotRads * 57.29577951308232D) + 90.0F;
/*      */       
/*  199 */       float rAngle = Creature.normalizeAngle(rot);
/*  200 */       String rs = (Servers.isThisATestServer() && insta) ? (" (LOS angle:" + rAngle + ")") : "";
/*  201 */       float pRot = performer.getStatus().getRotation();
/*  202 */       float pAngle = Creature.normalizeAngle(pRot - rot);
/*  203 */       String ps = (Servers.isThisATestServer() && insta) ? (" (performerAngle:" + pAngle + ")") : "";
/*  204 */       float tRot = (helper != null) ? helper.getStatus().getRotation() : 0.0F;
/*  205 */       float tAngle = Creature.normalizeAngle(tRot - rot);
/*  206 */       String ts = (Servers.isThisATestServer() && insta && helper != null) ? (" (targetAngle:" + tAngle + ")") : "";
/*      */       
/*  208 */       Point near = (helper != null) ? getBridgeEnd(performer) : getBridgeEnd(performer, null);
/*  209 */       Point far = (helper != null) ? getBridgeEnd(helper) : getBridgeEnd(performer, target);
/*      */ 
/*      */       
/*  212 */       Point diff = new Point(Math.abs(near.getX() - far.getX()), Math.abs(near.getY() - far.getY()), Math.abs(near.getH() - far.getH()));
/*      */ 
/*      */ 
/*      */       
/*  216 */       Point start = new Point(Math.min(near.getX(), far.getX()), Math.min(near.getY(), far.getY()));
/*      */ 
/*      */       
/*  219 */       Point end = new Point(Math.max(near.getX(), far.getX()), Math.max(near.getY(), far.getY()));
/*  220 */       byte dir = 0;
/*  221 */       int len = 0;
/*  222 */       int wid = 0;
/*  223 */       String lDir = "";
/*  224 */       if (performer.getStatus().getDir() == 0 || performer.getStatus().getDir() == 4) {
/*      */         
/*  226 */         dir = 0;
/*  227 */         len = diff.getY();
/*  228 */         wid = diff.getX() + 1;
/*  229 */         lDir = "North-South";
/*      */       }
/*      */       else {
/*      */         
/*  233 */         dir = 6;
/*  234 */         len = diff.getX();
/*  235 */         wid = diff.getY() + 1;
/*  236 */         lDir = "East-West";
/*      */       } 
/*      */ 
/*      */       
/*  240 */       if (dir == 0) {
/*      */         
/*  242 */         if (near.getY() < far.getY())
/*      */         {
/*  244 */           start.setH(near.getH());
/*  245 */           end.setH(far.getH());
/*      */         }
/*      */         else
/*      */         {
/*  249 */           start.setH(far.getH());
/*  250 */           end.setH(near.getH());
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*  256 */       else if (near.getX() < far.getX()) {
/*      */         
/*  258 */         start.setH(near.getH());
/*  259 */         end.setH(far.getH());
/*      */       }
/*      */       else {
/*      */         
/*  263 */         start.setH(far.getH());
/*  264 */         end.setH(near.getH());
/*      */       } 
/*      */       
/*  267 */       String rp = "the range pole";
/*  268 */       if (helper != null) {
/*  269 */         rp = helper.getName();
/*      */       }
/*  271 */       String pMsg = "";
/*  272 */       String hMsg = "";
/*  273 */       String bMsg = "";
/*  274 */       String qMsg = "";
/*  275 */       String rMsg = "";
/*      */ 
/*      */       
/*  278 */       switch (act.getTickCount()) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 1:
/*  284 */           if (insta)
/*  285 */             performer.getCommunicator().sendNormalServerMessage("Start(x,y,h):(" + start
/*  286 */                 .getX() + "," + start.getY() + "," + start.getH() + ") End(x,y,h):(" + end
/*  287 */                 .getX() + "," + end.getY() + "," + end.getH() + ")"); 
/*  288 */           performer.getCommunicator().sendNormalServerMessage("looks like bridge would be " + wid + "x" + len + " (" + lDir + ")");
/*      */           
/*  290 */           if (!isBorderLevel(performer)) {
/*      */             
/*  292 */             pMsg = "The (" + dirAsString(performer.getStatus().getDir()) + ") tile border in front of you must be level.";
/*      */             
/*  294 */             done = true;
/*      */             
/*      */             break;
/*      */           } 
/*  298 */           performer.getCommunicator().sendNormalServerMessage("The tile border to your " + 
/*  299 */               dirAsString(performer.getStatus().getDir()) + " is level");
/*  300 */           pMsg = "You make sure the dioptra is level.";
/*  301 */           bMsg = performer.getName() + " levels a dioptra.";
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 2:
/*  308 */           if (pAngle < 170.0F || pAngle > 190.0F) {
/*      */             
/*  310 */             pMsg = "You are not facing " + rp + ps + rs + ".";
/*  311 */             done = true;
/*      */             break;
/*      */           } 
/*  314 */           pMsg = "You point the dioptra at " + rp + ".";
/*  315 */           bMsg = performer.getName() + " points the dioptra.";
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 3:
/*  323 */           maxDioptraTiles = maxTilesFor(source);
/*  324 */           if (insta) {
/*      */             
/*  326 */             qMsg = " (QL:" + source.getQualityLevel() + " [max tiles=" + maxDioptraTiles + "])";
/*  327 */             rMsg = " (Range:" + rangeMeters + ")";
/*      */           }
/*  329 */           else if (maxDioptraTiles < (rangeMeters / 4)) {
/*      */             
/*  331 */             pMsg = "The quality of the dioptra makes it too hard to work out the distance.";
/*  332 */             done = true;
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  337 */           if (helper != null)
/*  338 */             Emotes.emoteAt((short)2013, performer, helper); 
/*  339 */           pMsg = "You line up the dioptra with " + rp + ".";
/*  340 */           hMsg = performer.getName() + " just signaled to you, so make sure you are holding your range pole and you are facing them!";
/*      */           
/*  342 */           bMsg = performer.getName() + " points the dioptra.";
/*      */           break;
/*      */         
/*      */         case 4:
/*  346 */           if (helper != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  351 */             if (tAngle > 10.0F && tAngle < 350.0F) {
/*      */               
/*  353 */               pMsg = helper.getName() + " is not facing you" + ts + rs + ".";
/*  354 */               hMsg = "You need to face " + performer.getName() + " to help plan the bridge.";
/*  355 */               Emotes.emoteAt((short)2017, performer, helper);
/*  356 */               done = true;
/*      */               break;
/*      */             } 
/*  359 */             int odir = (helper.getStatus().getDir() + 4) % 8;
/*  360 */             if (odir != performer.getStatus().getDir()) {
/*      */ 
/*      */               
/*  363 */               pMsg = "You (facing " + dirAsString(performer.getStatus().getDir()) + ") and " + helper.getName() + " (facing " + dirAsString(helper.getStatus().getDir()) + ") are not facing in opposite directions.";
/*      */               
/*  365 */               hMsg = "You need to face in opposite directions to plan the bridge.";
/*  366 */               done = true;
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */ 
/*      */             
/*  373 */             if (!insta && !isHoldingRangePole(helper)) {
/*      */               
/*  375 */               pMsg = helper.getName() + " is not holding a range pole.";
/*  376 */               done = true;
/*      */               break;
/*      */             } 
/*  379 */             if (insta) {
/*  380 */               rMsg = " (Range:" + rangeMeters + ")";
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  387 */           item = (helper != null) ? subjectItemOrNull(helper) : target;
/*  388 */           poleQL = 0.0F;
/*  389 */           maxPoleTiles = 0.0F;
/*  390 */           if (item != null) {
/*      */             
/*  392 */             poleQL = item.getQualityLevel();
/*  393 */             maxPoleTiles = maxTilesFor(item);
/*      */           } 
/*      */           
/*  396 */           if (insta) {
/*      */             
/*  398 */             qMsg = " (QL:" + poleQL + " [max tiles:" + maxPoleTiles + "])";
/*  399 */             rMsg = " (Range:" + rangeMeters + ")";
/*      */           }
/*  401 */           else if (maxPoleTiles < (rangeMeters / 4)) {
/*      */             
/*  403 */             pMsg = "You can't read the graduations on the range pole at this distance.";
/*  404 */             done = true;
/*      */             break;
/*      */           } 
/*  407 */           pMsg = "You read the graduations on the range pole" + qMsg + rMsg + " and work out the height difference.";
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 5:
/*  414 */           if (helper != null && !isBorderLevel(helper)) {
/*      */ 
/*      */             
/*  417 */             pMsg = "The (" + dirAsString(helper.getStatus().getDir()) + ") tile border in front of " + helper.getName() + " is not level.";
/*  418 */             hMsg = "The (" + dirAsString(helper.getStatus().getDir()) + ") tile border in front of you must be level.";
/*      */             
/*  420 */             done = true;
/*      */             break;
/*      */           } 
/*  423 */           if (target != null && !isBorderLevel(performer, target)) {
/*      */             
/*  425 */             pMsg = "The (" + dirAsString(getDir(performer, target)) + ") tile border in front of the range pole is not level.";
/*      */             
/*  427 */             done = true;
/*      */             break;
/*      */           } 
/*  430 */           if (helper != null) {
/*  431 */             performer.getCommunicator().sendNormalServerMessage("The tile border to your helpers " + 
/*  432 */                 dirAsString(helper.getStatus().getDir()) + " is level");
/*      */           } else {
/*  434 */             performer.getCommunicator().sendNormalServerMessage("The tile border to your rangepole's " + 
/*  435 */                 dirAsString(getDir(performer, target)) + " is level");
/*  436 */           }  pMsg = "You check the heights of the plan ends" + qMsg + ".";
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 6:
/*  443 */           ans = PlanBridgeChecks.calcHeights(performer, (helper != null) ? helper.getName() : "rangepole", near, far, diff, len, insta);
/*      */           
/*  445 */           done = ans.failed();
/*  446 */           qMsg = ans.qMsg();
/*  447 */           pMsg = ans.pMsg();
/*      */           
/*  449 */           if (!done) {
/*  450 */             pMsg = "You work out the orientation and width for the bridge" + qMsg + ".";
/*      */           }
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 7:
/*  460 */           if (wid > 1) {
/*      */             
/*  462 */             ans = PlanBridgeChecks.checkPlanWidth(start, end, dir, performer.getLayer());
/*  463 */             done = ans.failed();
/*  464 */             qMsg = ans.qMsg();
/*  465 */             pMsg = ans.pMsg();
/*      */           } 
/*  467 */           if (!done && Features.Feature.HIGHWAYS.isEnabled()) {
/*      */ 
/*      */ 
/*      */             
/*  471 */             ans = PlanBridgeChecks.checkForHighways(start, end, dir, performer.getLayer());
/*  472 */             done = ans.failed();
/*  473 */             qMsg = ans.qMsg();
/*  474 */             pMsg = ans.pMsg();
/*      */           } 
/*  476 */           if (!done) {
/*  477 */             pMsg = "You check for clearance between the bridge ends.";
/*      */           }
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 8:
/*  484 */           ans = PlanBridgeChecks.checkClearance(start, end, dir, insta, performer.getLayer());
/*  485 */           done = ans.failed();
/*  486 */           qMsg = ans.qMsg();
/*  487 */           pMsg = ans.pMsg();
/*  488 */           if (done) {
/*      */             
/*  490 */             if (pMsg.length() > 0)
/*  491 */               performer.getCommunicator().sendNormalServerMessage(pMsg); 
/*  492 */             if (qMsg.length() > 0)
/*  493 */               performer.getCommunicator().sendNormalServerMessage(qMsg); 
/*  494 */             pMsg = "You notice that there is not enough clearance for a bridge to be built here."; break;
/*      */           } 
/*  496 */           if (performer.isOnSurface()) {
/*  497 */             pMsg = "You check for protruding foliage."; break;
/*      */           } 
/*  499 */           pMsg = "You check for ceiling clearance.";
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 9:
/*  508 */           if (performer.isOnSurface()) {
/*  509 */             ans = PlanBridgeChecks.checkForFolliage(start, end, dir, insta);
/*      */           } else {
/*  511 */             ans = PlanBridgeChecks.checkCeilingClearance(start, end, dir, insta, performer.getLayer());
/*  512 */           }  done = ans.failed();
/*  513 */           qMsg = ans.qMsg();
/*  514 */           pMsg = ans.pMsg();
/*  515 */           if (!done) {
/*  516 */             pMsg = "You check for nearby cave entrances and any lava.";
/*      */           }
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 10:
/*  525 */           ans = PlanBridgeChecks.checkForCaveEntrances(start, end, dir, insta);
/*  526 */           done = ans.failed();
/*  527 */           qMsg = ans.qMsg();
/*  528 */           pMsg = ans.pMsg();
/*  529 */           if (!done) {
/*  530 */             pMsg = "You check for any items in the way.";
/*      */           }
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 11:
/*  537 */           ans = PlanBridgeChecks.checkForSpecialItems(start, end, dir, insta, performer.isOnSurface());
/*  538 */           done = ans.failed();
/*  539 */           qMsg = ans.qMsg();
/*  540 */           pMsg = ans.pMsg();
/*  541 */           if (!done) {
/*  542 */             pMsg = "You check that you have permissions to build the bridge.";
/*      */           }
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 12:
/*  550 */           ans = PlanBridgeChecks.checkForSettlements(performer, start, end, dir, insta, performer.isOnSurface());
/*  551 */           done = ans.failed();
/*  552 */           qMsg = ans.qMsg();
/*  553 */           pMsg = ans.pMsg();
/*  554 */           if (!done) {
/*      */             
/*  556 */             ans = PlanBridgeChecks.checkForPerimeters(performer, start, end, dir, insta, performer.isOnSurface());
/*  557 */             done = ans.failed();
/*  558 */             qMsg = ans.qMsg();
/*  559 */             pMsg = ans.pMsg();
/*      */             
/*  561 */             if (pMsg.length() == 0) {
/*  562 */               pMsg = "You check for any bridges that may interfere with this bridge.";
/*      */             }
/*      */           } 
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 13:
/*  570 */           ans = PlanBridgeChecks.checkForBridges(start, end, dir, insta, performer.isOnSurface());
/*  571 */           done = ans.failed();
/*  572 */           qMsg = ans.qMsg();
/*  573 */           pMsg = ans.pMsg();
/*  574 */           if (pMsg.length() == 0) {
/*  575 */             pMsg = "You check there would be no buildings under this bridge.";
/*      */           }
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 14:
/*  583 */           ans = PlanBridgeChecks.checkForBuildings(start, end, dir, insta, performer.isOnSurface());
/*  584 */           done = ans.failed();
/*  585 */           qMsg = ans.qMsg();
/*  586 */           pMsg = ans.pMsg();
/*  587 */           if (pMsg.length() == 0) {
/*      */ 
/*      */             
/*  590 */             ans = PlanBridgeChecks.checkForBuildingPermissions(performer, performer.getTileX(), performer
/*  591 */                 .getTileY(), insta, performer.isOnSurface());
/*  592 */             done = ans.failed();
/*  593 */             qMsg = ans.qMsg();
/*  594 */             pMsg = ans.pMsg();
/*  595 */             if (pMsg.length() == 0) {
/*      */ 
/*      */               
/*  598 */               if (helper != null) {
/*  599 */                 ans = PlanBridgeChecks.checkForBuildingPermissions(performer, helper.getTileX(), helper
/*  600 */                     .getTileY(), insta, performer.isOnSurface());
/*      */               } else {
/*  602 */                 ans = PlanBridgeChecks.checkForBuildingPermissions(performer, target.getTileX(), target
/*  603 */                     .getTileY(), insta, performer.isOnSurface());
/*  604 */               }  done = ans.failed();
/*  605 */               qMsg = ans.qMsg();
/*  606 */               pMsg = ans.pMsg();
/*      */             } 
/*      */           } 
/*  609 */           if (pMsg.length() == 0) {
/*  610 */             pMsg = "You work out the styles of bridges that would fit.";
/*      */           }
/*      */           break;
/*      */         
/*      */         case 15:
/*  615 */           pMsg = "You start working out the bridge components required.";
/*  616 */           bMsg = performer.getName() + " starts working out the bridge components required.";
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/*  621 */       rMsg = "";
/*  622 */       if (insta)
/*  623 */         rMsg = "(" + act.getTickCount() + ") "; 
/*  624 */       if (pMsg.length() > 0)
/*  625 */         performer.getCommunicator().sendNormalServerMessage(rMsg + pMsg); 
/*  626 */       if (helper != null && hMsg.length() > 0)
/*  627 */         helper.getCommunicator().sendNormalServerMessage(hMsg); 
/*  628 */       if (bMsg.length() > 0) {
/*  629 */         Server.getInstance().broadCastAction(bMsg, performer, 5);
/*      */       }
/*  631 */       if (act.getTickCount() == 15) {
/*      */         
/*  633 */         int floorLevel = (helper != null) ? helper.getFloorLevel() : target.getFloorLevel();
/*  634 */         Methods.sendPlanBridgeQuestion(performer, floorLevel, start, end, dir, wid, len);
/*      */       } 
/*      */     } 
/*      */     
/*  638 */     if (act.getTickCount() >= 16 || done) {
/*      */       
/*  640 */       performer.getCommunicator().sendNormalServerMessage("You stop surveying.");
/*  641 */       performer.getCommunicator().sendNormalServerMessage("You pack up the dioptra.");
/*  642 */       done = true;
/*      */     } 
/*      */     
/*  645 */     return done;
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
/*      */   public static boolean survey(Action act, Creature performer, Item source, Creature helper, @Nullable Item target, short action, float counter) {
/*  663 */     boolean done = false;
/*      */     
/*  665 */     if (helper != null && performer.isOnSurface() != helper.isOnSurface()) {
/*      */       
/*  667 */       performer.getCommunicator().sendNormalServerMessage("You must both be on surface or in a cave.");
/*  668 */       done = true;
/*      */     }
/*  670 */     else if (target != null && performer.isOnSurface() != target.isOnSurface()) {
/*      */       
/*  672 */       performer.getCommunicator().sendNormalServerMessage("You must both be on surface or in a cave.");
/*  673 */       done = true;
/*      */     }
/*  675 */     else if (target != null && !target.isPlanted()) {
/*      */       
/*  677 */       performer.getCommunicator().sendNormalServerMessage("The range pole must be planted to stop it swaying.");
/*      */       
/*  679 */       done = true;
/*      */     }
/*      */     else {
/*      */       
/*  683 */       boolean insta = (Servers.localServer.testServer && performer.getPower() > 1);
/*      */       
/*  685 */       int rangeMeters = (helper != null) ? Creature.rangeTo(performer, helper) : Creature.rangeTo(performer, target);
/*      */       
/*  687 */       int timePerStage = insta ? 2 : (rangeMeters / 8 + 2);
/*  688 */       int time = timePerStage * 70;
/*  689 */       if (counter == 1.0F) {
/*      */         
/*  691 */         act.setTimeLeft(time);
/*  692 */         performer.sendActionControl(Actions.actionEntrys[640].getVerbString(), true, time);
/*  693 */         performer.getCommunicator().sendNormalServerMessage("You carefully place the dioptra on its tripod in front of you.");
/*      */         
/*  695 */         Server.getInstance().broadCastAction(performer.getName() + " starts to survey.", performer, 5);
/*  696 */         act.setTickCount(0);
/*  697 */         if (insta) {
/*  698 */           performer.getCommunicator().sendNormalServerMessage("(Note: Anything shown in brackets will be shown on test server for GM+ only.)");
/*      */         }
/*      */       } else {
/*      */         
/*  702 */         time = act.getTimeLeft();
/*      */       } 
/*  704 */       if (!done && act.currentSecond() % timePerStage == 0) {
/*      */         float maxDioptraTiles; Item item; float maxPoleTiles, poleQL; Point diff;
/*  706 */         act.incTickCount();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  721 */         float hX = (helper != null) ? helper.getStatus().getPositionX() : target.getPosX();
/*  722 */         float hY = (helper != null) ? helper.getStatus().getPositionY() : target.getPosY();
/*  723 */         double rotRads = Math.atan2((performer.getStatus().getPositionY() - hY), (performer
/*  724 */             .getStatus().getPositionX() - hX));
/*  725 */         float rot = (float)(rotRads * 57.29577951308232D) + 90.0F;
/*      */         
/*  727 */         float rAngle = Creature.normalizeAngle(rot);
/*  728 */         String rs = (Servers.isThisATestServer() && insta) ? (" (LOS angle:" + rAngle + ")") : "";
/*  729 */         float pRot = performer.getStatus().getRotation();
/*  730 */         float pAngle = Creature.normalizeAngle(pRot - rot);
/*  731 */         String ps = (Servers.isThisATestServer() && insta) ? (" (performerAngle:" + pAngle + ")") : "";
/*  732 */         float tRot = (helper != null) ? helper.getStatus().getRotation() : 0.0F;
/*  733 */         float tAngle = Creature.normalizeAngle(tRot - rot);
/*  734 */         String ts = (Servers.isThisATestServer() && insta && helper != null) ? (" (targetAngle:" + tAngle + ")") : "";
/*      */         
/*  736 */         Point near = new Point(performer.getTileX(), performer.getTileY(), performer.getPosZDirts());
/*      */         
/*  738 */         Point far = (helper != null) ? new Point(helper.getTileX(), helper.getTileY(), helper.getPosZDirts()) : new Point(target.getTileX(), target.getTileY(), (int)(target.getPosZ() * 10.0F));
/*      */         
/*  740 */         String rp = "the range pole";
/*  741 */         if (helper != null) {
/*  742 */           rp = helper.getName();
/*      */         }
/*  744 */         String pMsg = "";
/*  745 */         String hMsg = "";
/*  746 */         String bMsg = "";
/*  747 */         String qMsg = "";
/*  748 */         String rMsg = "";
/*      */         
/*  750 */         switch (act.getTickCount()) {
/*      */ 
/*      */           
/*      */           case 1:
/*  754 */             pMsg = "You make sure the dioptra is level.";
/*  755 */             bMsg = performer.getName() + " levels a dioptra.";
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 2:
/*  762 */             if (pAngle < 170.0F || pAngle > 190.0F) {
/*      */               
/*  764 */               pMsg = "You are not facing " + rp + ps + rs + ".";
/*  765 */               done = true;
/*      */               break;
/*      */             } 
/*  768 */             pMsg = "You point the dioptra at " + rp + ".";
/*  769 */             bMsg = performer.getName() + " points the dioptra.";
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 3:
/*  777 */             maxDioptraTiles = maxTilesFor(source);
/*  778 */             if (insta) {
/*      */               
/*  780 */               qMsg = " (QL:" + source.getQualityLevel() + " [max tiles=" + maxDioptraTiles + "])";
/*  781 */               rMsg = " (Range:" + rangeMeters + ")";
/*      */             }
/*  783 */             else if (maxDioptraTiles < (rangeMeters / 4)) {
/*      */               
/*  785 */               pMsg = "The quality of the dioptra makes it too hard to work out the distance.";
/*  786 */               done = true;
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */             
/*  792 */             pMsg = "You line up the dioptra with the range pole.";
/*  793 */             bMsg = performer.getName() + " points the dioptra.";
/*      */             break;
/*      */           
/*      */           case 4:
/*  797 */             if (helper != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  802 */               if (tAngle > 10.0F && tAngle < 350.0F) {
/*      */                 
/*  804 */                 pMsg = helper.getName() + " is not facing you" + ts + rs + ".";
/*  805 */                 hMsg = "You need to face " + performer.getName() + " to help survey.";
/*  806 */                 Emotes.emoteAt((short)2017, performer, helper);
/*  807 */                 done = true;
/*      */ 
/*      */                 
/*      */                 break;
/*      */               } 
/*      */ 
/*      */               
/*  814 */               if (!insta && !isHoldingRangePole(helper)) {
/*      */                 
/*  816 */                 pMsg = helper.getName() + " is not holding a range pole.";
/*  817 */                 done = true;
/*      */                 break;
/*      */               } 
/*  820 */               if (insta) {
/*  821 */                 rMsg = " (Range:" + rangeMeters + ")";
/*      */               }
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/*  827 */             item = (helper != null) ? subjectItemOrNull(helper) : target;
/*  828 */             maxPoleTiles = 0.0F;
/*  829 */             poleQL = 0.0F;
/*  830 */             if (item != null) {
/*      */               
/*  832 */               poleQL = item.getQualityLevel();
/*  833 */               maxPoleTiles = maxTilesFor(item);
/*      */             } 
/*  835 */             if (insta) {
/*      */               
/*  837 */               qMsg = " (QL:" + poleQL + " [max tiles:" + maxPoleTiles + "])";
/*  838 */               rMsg = " (Range:" + rangeMeters + ")";
/*      */             }
/*  840 */             else if (maxPoleTiles < (rangeMeters / 4)) {
/*      */               
/*  842 */               pMsg = "You can't read the graduations on the range pole at this distance.";
/*  843 */               done = true;
/*      */               break;
/*      */             } 
/*  846 */             pMsg = "You read the graduations on the range pole" + qMsg + rMsg + " and work out the height difference.";
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 5:
/*  854 */             diff = new Point(near.getX() - far.getX(), near.getY() - far.getY(), Math.abs(near.getH() - far.getH()));
/*  855 */             if (diff.getY() != 0) {
/*      */               
/*  857 */               if (diff.getX() != 0)
/*      */               {
/*      */ 
/*      */                 
/*  861 */                 pMsg = "The range pole is " + Math.abs(diff.getY()) + " tiles " + ((diff.getY() > 0) ? "North" : "South") + " and " + Math.abs(diff.getX()) + " tiles " + ((diff.getX() > 0) ? "West" : "East") + " of you.";
/*      */               
/*      */               }
/*      */               else
/*      */               {
/*      */                 
/*  867 */                 pMsg = "The range pole is " + Math.abs(diff.getY()) + " tiles straight " + ((diff.getY() > 0) ? "North" : "South") + " of you.";
/*      */               }
/*      */             
/*  870 */             } else if (diff.getX() != 0) {
/*      */ 
/*      */               
/*  873 */               pMsg = "The range pole is " + Math.abs(diff.getX()) + " tiles straight " + ((diff.getX() > 0) ? "West" : "East") + " of you.";
/*      */             } 
/*      */             
/*  876 */             if (diff.getH() != 0) {
/*      */               
/*  878 */               if (near.getH() > far.getH()) {
/*  879 */                 pMsg = pMsg + " Also you appear to be " + diff.getH() + " dirt higher than the range pole.";
/*      */               } else {
/*  881 */                 pMsg = pMsg + " Also you appear to be " + diff.getH() + " dirt lower than the range pole.";
/*      */               } 
/*      */             } else {
/*  884 */               pMsg = pMsg + " Also both ends are the same height, Nice!";
/*      */             } 
/*  886 */             if (far.getH() <= 0 && near.getH() >= 0) {
/*      */ 
/*      */               
/*  889 */               if (insta)
/*  890 */                 rMsg = "(" + act.getTickCount() + ") "; 
/*  891 */               if (pMsg.length() > 0)
/*  892 */                 performer.getCommunicator().sendNormalServerMessage(rMsg + pMsg); 
/*  893 */               String h = (helper != null) ? "helper is stood" : "rangepole is planted";
/*  894 */               pMsg = "As your " + h + " in water, you work out how high you are above the water, and it is " + near.getH() + ".";
/*      */             } 
/*      */             break;
/*      */         } 
/*  898 */         rMsg = "";
/*  899 */         if (insta)
/*  900 */           rMsg = "(" + act.getTickCount() + ") "; 
/*  901 */         if (pMsg.length() > 0)
/*  902 */           performer.getCommunicator().sendNormalServerMessage(rMsg + pMsg); 
/*  903 */         if (helper != null && hMsg.length() > 0)
/*  904 */           helper.getCommunicator().sendNormalServerMessage(hMsg); 
/*  905 */         if (bMsg.length() > 0) {
/*  906 */           Server.getInstance().broadCastAction(bMsg, performer, 5);
/*      */         }
/*      */       } 
/*  909 */       if (act.getTickCount() >= 6 || done) {
/*      */         
/*  911 */         performer.getCommunicator().sendNormalServerMessage("You stop surveying.");
/*  912 */         performer.getCommunicator().sendNormalServerMessage("You pack up the dioptra.");
/*  913 */         done = true;
/*      */       } 
/*      */     } 
/*  916 */     return done;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isHoldingRangePole(Creature target) {
/*      */     try {
/*  923 */       return (((Player)target).getCurrentAction().getNumber() == 636 && 
/*  924 */         subjectItem(target).getTemplateId() == 901);
/*      */     }
/*  926 */     catch (NoSuchActionException e) {
/*      */       
/*  928 */       return false;
/*      */     }
/*  930 */     catch (NoSuchItemException e) {
/*      */       
/*  932 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Item subjectItemOrNull(Creature target) {
/*      */     try {
/*  940 */       long subject = ((Player)target).getCurrentAction().getSubjectId();
/*  941 */       Item item = Items.getItem(subject);
/*  942 */       return item;
/*      */     }
/*  944 */     catch (NoSuchActionException e) {
/*      */       
/*  946 */       return null;
/*      */     }
/*  948 */     catch (NoSuchItemException e) {
/*      */       
/*  950 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Item subjectItem(Creature target) throws NoSuchActionException, NoSuchItemException {
/*  956 */     long subject = ((Player)target).getCurrentAction().getSubjectId();
/*  957 */     Item item = Items.getItem(subject);
/*  958 */     return item;
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
/*      */   private static Point getBridgeEnd(Creature creature) {
/*  970 */     byte dir = creature.getStatus().getDir();
/*  971 */     Point point = new Point(creature.getTileX(), creature.getTileY());
/*  972 */     if (dir == 4) {
/*  973 */       point.setY(point.getY() + 1);
/*  974 */     } else if (dir == 2) {
/*  975 */       point.setX(point.getX() + 1);
/*      */     } 
/*      */     
/*  978 */     int tileH = (int)(Zones.getHeightForNode(point.getX(), point.getY(), creature.getLayer()) * 10.0F);
/*  979 */     int hoff = creature.getFloorLevel() * 30 + ((creature.getFloorLevel() != 0) ? 3 : 0);
/*  980 */     point.setH(tileH + hoff);
/*  981 */     return point;
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
/*      */   private static Point getBridgeEnd(Creature creature, Item item) {
/*  994 */     byte dir = getDir(creature, item);
/*  995 */     Point point = new Point(0, 0);
/*  996 */     int floorlevel = 0;
/*  997 */     if (item != null) {
/*      */       
/*  999 */       point.setX(item.getTileX());
/* 1000 */       point.setY(item.getTileY());
/*      */       
/* 1002 */       floorlevel = item.getFloorLevel();
/*      */     }
/*      */     else {
/*      */       
/* 1006 */       point.setX(creature.getTileX());
/* 1007 */       point.setY(creature.getTileY());
/* 1008 */       floorlevel = creature.getFloorLevel();
/*      */     } 
/* 1010 */     if (dir == 4) {
/* 1011 */       point.setY(point.getY() + 1);
/* 1012 */     } else if (dir == 2) {
/* 1013 */       point.setX(point.getX() + 1);
/*      */     } 
/*      */     
/* 1016 */     int tileH = (int)(Zones.getHeightForNode(point.getX(), point.getY(), creature.getLayer()) * 10.0F);
/* 1017 */     int hoff = floorlevel * 30 + ((floorlevel != 0) ? 3 : 0);
/* 1018 */     point.setH(tileH + hoff);
/* 1019 */     return point;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte getDir(Creature creature, Item item) {
/* 1024 */     return (item != null) ? (byte)((creature.getStatus().getDir() + 4) % 8) : creature.getStatus().getDir();
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isBorderLevel(Creature creature) {
/* 1029 */     Point pt = getBridgeEnd(creature);
/* 1030 */     byte dir = creature.getStatus().getDir();
/*      */     
/* 1032 */     if (creature.getFloorLevel() == 0) {
/*      */ 
/*      */       
/* 1035 */       int tileX2 = pt.getX();
/* 1036 */       int tileY2 = pt.getY();
/* 1037 */       if (dir == 0 || dir == 4) {
/* 1038 */         tileX2++;
/* 1039 */       } else if (dir == 2 || dir == 6) {
/* 1040 */         tileY2++;
/*      */       } 
/*      */       
/* 1043 */       int ht2 = (int)(Zones.getHeightForNode(tileX2, tileY2, creature.getLayer()) * 10.0F);
/*      */ 
/*      */       
/* 1046 */       return (pt.getH() == ht2);
/*      */     } 
/* 1048 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isBorderLevel(Creature creature, Item item) {
/* 1053 */     Point pt = getBridgeEnd(creature, item);
/* 1054 */     byte dir = creature.getStatus().getDir();
/*      */     
/* 1056 */     if (creature.getFloorLevel() == 0) {
/*      */ 
/*      */       
/* 1059 */       int tileX2 = pt.getX();
/* 1060 */       int tileY2 = pt.getY();
/* 1061 */       if (dir == 0 || dir == 4) {
/* 1062 */         tileX2++;
/* 1063 */       } else if (dir == 6 || dir == 2) {
/* 1064 */         tileY2++;
/* 1065 */       }  int floorlevel = item.getFloorLevel();
/* 1066 */       int hoff = floorlevel * 30 + ((floorlevel != 0) ? 3 : 0);
/*      */ 
/*      */       
/* 1069 */       int ht2 = (int)(Zones.getHeightForNode(tileX2, tileY2, creature.getLayer()) * 10.0F);
/*      */ 
/*      */       
/* 1072 */       return (pt.getH() == ht2 + hoff);
/*      */     } 
/* 1074 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static float maxTilesFor(Item item) {
/* 1079 */     return item.getQualityLevel() * 0.4F + item.getRarity();
/*      */   }
/*      */ 
/*      */   
/*      */   private static String dirAsString(byte dir) {
/* 1084 */     switch (dir) {
/*      */       
/*      */       case 0:
/* 1087 */         return "North";
/*      */       case 2:
/* 1089 */         return "East";
/*      */       case 4:
/* 1091 */         return "South";
/*      */       case 6:
/* 1093 */         return "West";
/*      */     } 
/* 1095 */     return "Unknown " + dir;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MethodsSurveying.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
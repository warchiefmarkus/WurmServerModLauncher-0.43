/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.structures.PlanBridgeCheckResult;
/*      */ import com.wurmonline.server.structures.PlanBridgeChecks;
/*      */ import com.wurmonline.server.structures.PlanBridgeMethods;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.BridgeConstants;
/*      */ import java.util.HashSet;
/*      */ import java.util.Properties;
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
/*      */ public class PlanBridgeQuestion
/*      */   extends Question
/*      */ {
/*   44 */   private static final Logger logger = Logger.getLogger(PlanBridgeQuestion.class.getName());
/*      */   
/*      */   private static final int MINHEIGHTWOOD = 0;
/*      */   private static final int MINHEIGHTSTONE = 0;
/*      */   private static final int MINSKILLROPE = 10;
/*      */   private static final int MINSKILLWOOD = 10;
/*      */   private static final int MINSKILLBRICK = 30;
/*      */   private static final int MINSKILLMARBLE = 40;
/*      */   private static final int MINSKILLARCH = 20;
/*      */   private int bridgeCount;
/*      */   private final Point start;
/*      */   private final Point end;
/*      */   private final byte dir;
/*      */   private final int width;
/*      */   private final int length;
/*      */   private int heightDiff;
/*   60 */   private int steepnessSelected = 20;
/*   61 */   private int bmlLines = 0;
/*   62 */   private int iconLines = 0;
/*      */   
/*      */   private int startFloorlevel;
/*      */   private int endFloorlevel;
/*      */   private final int targetFloorLevel;
/*   67 */   private String fail = "";
/*   68 */   private String reason = "";
/*      */ 
/*      */   
/*   71 */   private final String[] spansWood = new String[] { "", "C", "aA", "aCA", "aCCA", "aCCCA", "aASaCA", "aCASaCA" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   79 */   private final String[] spansBrick = new String[] { "", "E", "aA", "aDA", "abBA", "abCBA", "ESabBA", "ESabCBA", "aASabCBA", "ESabCBASE", "ESabCBASaA", "abCBASabCBA" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   91 */   private final String[] archWood = new String[] { "", "C", "aA", "aCA", "aCCA", "aCCCA", "aCCCCA", "aCCCCCA", "aCCCCCCA", "aCCCCCCCA", "aCCCCCCCCA", "aCCCCCCCCCA", "aASaCCCCASaA", "aASaCCCCCASaA", "aASaCCCCCCASaA", "aASaCCCCCCCASaA", "aASaCCCCCCCCASaA", "aCASaCCCCCCCASaCA", "aCASaCCCCCCCCASaCA", "aCCASaCCCCCCCASaCCA", "aCCASaCCCCCCCCASaCCA", "aASaASaCCCCCCCASaASaA", "aASaASaCCCCCCCCASaASaA", "aASaASaCCCCCCCCCASaASaA", "aCASaASaCCCCCCCCASaASaCA", "aCASaASaCCCCCCCCCASaASaCA", "aCASaASaCCCCCCCCCCASaASaCA", "aCASaCASaCCCCCCCCCASaCASaCA", "aCASaCASaCCCCCCCCCCASaCASaCA", "aCASaCASaCCCCCCCCCCCASaCASaCA", "aASaASaASaCCCCCCCCCCASaASaASaA", "aASaASaASaCCCCCCCCCCCASaASaASaA", "aASaASaASaCCCCCCCCCCCCASaASaASaA", "aCASaASaASaCCCCCCCCCCCASaASaASaCA", "aCASaASaASaCCCCCCCCCCCCASaASaASaCA", "aCASaASaASaCCCCCCCCCCCCCASaASaASaCA", "aCASaCASaASaCCCCCCCCCCCCASaASaCASaCA", "aCASaCASaASaCCCCCCCCCCCCCASaASaCASaCA", "aCASaCASaASaCCCCCCCCCCCCCCASaASaCASaCA", "aASaASaASaASaCCCCCCCCCCCCCASaASaASaASaA", "aASaASaASaASaCCCCCCCCCCCCCCASaASaASaASaA" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  133 */   private String bridgeName = getResponder().getName() + "'s bridge";
/*  134 */   private byte bridgeType = 0;
/*      */   private boolean arched = false;
/*  136 */   private String bridgePlan = "";
/*  137 */   private int page = 0;
/*  138 */   private int steepness = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int layer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlanBridgeQuestion(Creature aResponder, int aTargetFloorLevel, Point aStart, Point aEnd, byte aDir, int aWidth, int aLength) {
/*  155 */     this(aResponder, aTargetFloorLevel, "Decide on what bridge to build", aStart, aEnd, aDir, aWidth, aLength, 0);
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
/*      */   public PlanBridgeQuestion(Creature aResponder, int aTargetFloorLevel, Point aStart, Point aEnd, byte aDir, int aWidth, int aLength, byte aBridgeType, boolean aArched, String aBridgePlan, String aBridgeName) {
/*  177 */     this(aResponder, aTargetFloorLevel, "Name your Bridge", aStart, aEnd, aDir, aWidth, aLength, 1);
/*  178 */     this.bridgeType = aBridgeType;
/*  179 */     this.arched = aArched;
/*  180 */     this.bridgePlan = aBridgePlan;
/*  181 */     this.bridgeName = aBridgeName;
/*  182 */     this.layer = aResponder.getLayer();
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
/*      */   public PlanBridgeQuestion(Creature aResponder, int aTargetFloorLevel, String aQuestion, Point aStart, Point aEnd, byte aDir, int aWidth, int aLength, int aPage) {
/*  194 */     super(aResponder, "Plan Bridge", aQuestion, 116, -10L);
/*  195 */     this.start = aStart;
/*  196 */     this.end = aEnd;
/*  197 */     this.dir = aDir;
/*  198 */     this.width = aWidth;
/*  199 */     this.length = aLength;
/*  200 */     this.targetFloorLevel = aTargetFloorLevel;
/*  201 */     this.page = aPage;
/*  202 */     this.layer = aResponder.getLayer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void answer(Properties aAnswer) {
/*  212 */     setAnswer(aAnswer);
/*  213 */     if (this.type == 0) {
/*      */       
/*  215 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*  218 */     if (this.type == 116)
/*      */     {
/*      */       
/*  221 */       if (this.page == 0) {
/*      */         
/*  223 */         String sAction = aAnswer.getProperty("bridgereply");
/*  224 */         String[] reply = sAction.split(",");
/*      */ 
/*      */         
/*  227 */         this.bridgeType = Byte.parseByte(reply[0]);
/*  228 */         if (this.bridgeType == 0) {
/*      */ 
/*      */           
/*  231 */           getResponder().getCommunicator().sendNormalServerMessage("You decide not to build a bridge.");
/*      */           return;
/*      */         } 
/*  234 */         this.arched = Boolean.parseBoolean(reply[1]);
/*  235 */         String sBridgeType = BridgeConstants.BridgeMaterial.fromByte(this.bridgeType).getName().toLowerCase();
/*  236 */         String pre = (this.bridgeType == BridgeConstants.BridgeMaterial.ROPE.getCode()) ? "" : (this.arched ? "arched " : "flat ");
/*  237 */         this.bridgeName = getResponder().getName() + "'s " + pre + sBridgeType + " bridge";
/*  238 */         this.bridgePlan = reply[2];
/*  239 */         if (Servers.isThisATestServer() && getResponder().getPower() >= 2) {
/*  240 */           getResponder().getCommunicator().sendNormalServerMessage("(" + this.bridgeName + ":" + this.bridgePlan + ")");
/*      */         }
/*      */         
/*  243 */         if (!this.arched && this.length > 5) {
/*      */           
/*  245 */           getResponder().getCommunicator().sendOpenPlanWindow(this.bridgeName, this.dir, (byte)this.length, (byte)this.width, this.start, this.end, this.bridgeType, this.bridgePlan);
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*  250 */         PlanBridgeQuestion pbq = new PlanBridgeQuestion(getResponder(), this.targetFloorLevel, this.start, this.end, this.dir, this.width, this.length, this.bridgeType, this.arched, this.bridgePlan, this.bridgeName);
/*      */         
/*  252 */         pbq.sendQuestionPage2();
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/*  258 */         this.bridgeName = aAnswer.getProperty("bridgename");
/*  259 */         this.steepness = Integer.parseInt(aAnswer.getProperty("steepness"));
/*      */         
/*  261 */         if (this.steepnessSelected <= 20) {
/*  262 */           PlanBridgeMethods.planBridge(getResponder(), this.dir, this.bridgeType, this.arched, this.bridgePlan, this.steepness, this.start, this.end, this.bridgeName);
/*      */         } else {
/*      */           
/*  265 */           getResponder().getCommunicator().sendNormalServerMessage("You cancel planning a bridge");
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
/*      */ 
/*      */   
/*      */   public void sendQuestion() {
/*  281 */     grabFloorLevels();
/*      */     
/*  283 */     this.bmlLines = 13;
/*  284 */     this.iconLines = 0;
/*  285 */     StringBuilder buf = new StringBuilder();
/*  286 */     StringBuilder bridges = new StringBuilder();
/*  287 */     boolean doneWoodCheck = false;
/*  288 */     boolean doneStoneCheck = false;
/*  289 */     String[] woodFail = { "", "" };
/*  290 */     String[] stoneFail = { "", "" };
/*  291 */     this.bridgeCount = 0;
/*  292 */     boolean onSurface = getResponder().isOnSurface();
/*      */     
/*  294 */     String bridge = "";
/*      */ 
/*      */     
/*  297 */     String bridgeArea = "Planned bridge area is " + this.length + " tile" + ((this.length == 1) ? "" : "s") + " long and " + this.width + " tile" + ((this.width == 1) ? "" : "s") + " wide.";
/*      */     
/*  299 */     getResponder().getCommunicator().sendNormalServerMessage(bridgeArea);
/*  300 */     buf.append(getBmlHeaderWithScrollAndQuestion());
/*  301 */     buf.append("label{text=\"" + bridgeArea + "\"};");
/*  302 */     buf.append("label{type=\"bold\";text=\"Bridge types available...\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  310 */     this.fail = "";
/*  311 */     this.reason = "";
/*  312 */     bridge = "";
/*  313 */     int maxLength = 38;
/*  314 */     Skill requiredSkill = getResponder().getSkills().getSkillOrLearn(1014);
/*      */     
/*  316 */     if (this.width != 1) {
/*      */       
/*  318 */       this.fail = "Too Wide";
/*  319 */       this.reason = "Rope bridges can only be 1 tile wide";
/*      */     }
/*  321 */     else if (this.length > maxLength) {
/*      */       
/*  323 */       this.fail = "Too Long";
/*  324 */       this.reason = "Rope bridges are restricted to " + maxLength + " tiles long";
/*      */     }
/*  326 */     else if (hasLowSkill(requiredSkill, 10, maxLength, true)) {
/*      */       
/*  328 */       this.fail = "Low Skill";
/*  329 */       if (requiredSkill.getKnowledge(0.0D) < 10.0D) {
/*  330 */         this.reason = "You need at least 10 ropemaking skill to plan any rope bridge.";
/*      */       } else {
/*  332 */         this.reason = "You dont have enough ropemaking skill for this length rope bridge.";
/*      */       } 
/*      */     } else {
/*      */       
/*  336 */       String[] reply = PlanBridgeMethods.isBuildingOk(BridgeConstants.BridgeMaterial.ROPE.getCode(), this.dir, onSurface, this.start, this.startFloorlevel, this.end, this.endFloorlevel);
/*  337 */       this.fail = reply[0];
/*  338 */       this.reason = reply[1];
/*      */     } 
/*      */     
/*  341 */     if (this.fail.length() == 0)
/*      */     {
/*  343 */       if (this.length == 1) {
/*      */         
/*  345 */         bridge = "E";
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  350 */         bridge = makeArch("a", this.length, 'C', "A");
/*  351 */         ropeHeightsOk(bridge, calcMinSag());
/*      */       } 
/*      */     }
/*      */     
/*  355 */     bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.ROPE, true, "Rope", bridge));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  363 */     this.fail = "";
/*  364 */     this.reason = "";
/*  365 */     bridge = "";
/*  366 */     maxLength = 38;
/*  367 */     requiredSkill = getResponder().getSkills().getSkillOrLearn(1005);
/*      */     
/*  369 */     if (this.width > 2) {
/*      */       
/*  371 */       this.fail = "Too Wide";
/*  372 */       this.reason = "Wood bridges can only be a maximum of 2 tiles wide.";
/*      */     }
/*  374 */     else if (this.heightDiff > 20 * this.length) {
/*      */       
/*  376 */       this.fail = "Too Steep";
/*  377 */       this.reason = "The slope of part of the bridge would exceed 20 dirt.";
/*      */     }
/*  379 */     else if (this.length > maxLength) {
/*      */       
/*  381 */       this.fail = "Too Long";
/*  382 */       this.reason = "Wood bridges are restricted to " + maxLength + " tiles long";
/*      */     }
/*  384 */     else if (hasLowSkill(requiredSkill, 10, maxLength, false)) {
/*      */       
/*  386 */       this.fail = "Low Skill";
/*  387 */       if (requiredSkill.getKnowledge(0.0D) < 10.0D) {
/*  388 */         this.reason = "You need at least 10 carpentry skill to plan any wood bridge.";
/*      */       } else {
/*  390 */         this.reason = "You dont have enough carpentry skill for this length wood bridge.";
/*      */       } 
/*  392 */     } else if (this.length > 5 && (this.start.getH() < 0 || this.end.getH() < 0)) {
/*      */       
/*  394 */       this.fail = "Too Low";
/*  395 */       this.reason = "Both ends of a wood bridge (if it has supports) need to be 0 above water.";
/*      */     }
/*      */     else {
/*      */       
/*  399 */       String[] reply = PlanBridgeMethods.isBuildingOk(BridgeConstants.BridgeMaterial.WOOD.getCode(), this.dir, onSurface, this.start, this.startFloorlevel, this.end, this.endFloorlevel);
/*  400 */       this.fail = reply[0];
/*  401 */       this.reason = reply[1];
/*  402 */       doneWoodCheck = true;
/*  403 */       woodFail = reply;
/*      */     } 
/*      */     
/*  406 */     if (this.fail.length() > 0) {
/*  407 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.WOOD, false, "Flat wood", bridge));
/*      */     }
/*      */     else {
/*      */       
/*  411 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.WOOD, "Flat wood", getWoodSpan(this.length)));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  419 */     this.fail = "";
/*  420 */     this.reason = "";
/*  421 */     bridge = "";
/*  422 */     maxLength = 38;
/*  423 */     requiredSkill = getResponder().getSkills().getSkillOrLearn(1013);
/*      */     
/*  425 */     if (this.width > 3) {
/*      */       
/*  427 */       this.fail = "Too Wide";
/*  428 */       this.reason = "Brick bridges are limited to 3 tiles wide.";
/*      */     }
/*  430 */     else if (this.heightDiff > 20 * this.length) {
/*      */       
/*  432 */       this.fail = "Too Steep";
/*  433 */       this.reason = "The slope of part of the bridge would exceed 20 dirt.";
/*      */     }
/*  435 */     else if (this.length > maxLength) {
/*      */       
/*  437 */       this.fail = "Too Long";
/*  438 */       this.reason = "Brick bridges are restricted to " + maxLength + " tiles long";
/*      */     }
/*  440 */     else if (hasLowSkill(requiredSkill, 30, maxLength, false)) {
/*      */       
/*  442 */       this.fail = "Low Skill";
/*  443 */       if (requiredSkill.getKnowledge(0.0D) < 30.0D) {
/*  444 */         this.reason = "You need at least 30 masonry skill to make any brick bridge.";
/*      */       } else {
/*  446 */         this.reason = "You dont have enough masonry skill for this length brick bridge.";
/*      */       } 
/*  448 */     } else if (this.length > 5 && (this.start.getH() < 0 || this.end.getH() < 0)) {
/*      */       
/*  450 */       this.fail = "Too Low";
/*  451 */       this.reason = "Both ends of a Brick bridge (if it has supports) need to be 0 above water.";
/*      */     }
/*      */     else {
/*      */       
/*  455 */       String[] reply = PlanBridgeMethods.isBuildingOk(BridgeConstants.BridgeMaterial.BRICK.getCode(), this.dir, onSurface, this.start, this.startFloorlevel, this.end, this.endFloorlevel);
/*  456 */       this.fail = reply[0];
/*  457 */       this.reason = reply[1];
/*  458 */       doneStoneCheck = true;
/*  459 */       stoneFail = reply;
/*      */     } 
/*      */     
/*  462 */     if (this.fail.length() > 0) {
/*  463 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.BRICK, false, "Flat brick", bridge));
/*      */     } else {
/*      */       
/*  466 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.BRICK, "Flat brick", getBrickSpan(this.length)));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  474 */     this.fail = "";
/*  475 */     this.reason = "";
/*  476 */     bridge = "";
/*  477 */     maxLength = 38;
/*  478 */     requiredSkill = getResponder().getSkills().getSkillOrLearn(1013);
/*      */     
/*  480 */     if (this.width > 3) {
/*      */       
/*  482 */       this.fail = "Too Wide";
/*  483 */       this.reason = "Marble bridges are limited to 3 tiles wide.";
/*      */     }
/*  485 */     else if (this.heightDiff > 20 * this.length) {
/*      */       
/*  487 */       this.fail = "Too Steep";
/*  488 */       this.reason = "The slope of part of the bridge would exceed 20 dirt.";
/*      */     }
/*  490 */     else if (this.length > maxLength) {
/*      */       
/*  492 */       this.fail = "Too Long";
/*  493 */       this.reason = "Marble bridges are restricted to " + maxLength + " tiles long";
/*      */     }
/*  495 */     else if (hasLowSkill(requiredSkill, 40, maxLength, false)) {
/*      */       
/*  497 */       this.fail = "Low Skill";
/*  498 */       if (requiredSkill.getKnowledge(0.0D) < 40.0D) {
/*  499 */         this.reason = "You need at least 40 masonry skill to make any marble bridge.";
/*      */       } else {
/*  501 */         this.reason = "You dont have enough masonry skill for this length marble bridge.";
/*      */       } 
/*  503 */     } else if (this.length > 5 && (this.start.getH() < 0 || this.end.getH() < 0)) {
/*      */       
/*  505 */       this.fail = "Too Low";
/*  506 */       this.reason = "Both ends of a Marble bridge (if it has supports) need to be 0 above water.";
/*      */ 
/*      */     
/*      */     }
/*  510 */     else if (doneStoneCheck) {
/*      */       
/*  512 */       this.fail = stoneFail[0];
/*  513 */       this.reason = stoneFail[1];
/*      */     }
/*      */     else {
/*      */       
/*  517 */       String[] reply = PlanBridgeMethods.isBuildingOk(BridgeConstants.BridgeMaterial.MARBLE.getCode(), this.dir, onSurface, this.start, this.startFloorlevel, this.end, this.endFloorlevel);
/*  518 */       this.fail = reply[0];
/*  519 */       this.reason = reply[1];
/*  520 */       stoneFail = reply;
/*  521 */       doneStoneCheck = true;
/*      */     } 
/*      */ 
/*      */     
/*  525 */     if (this.fail.length() > 0) {
/*      */       
/*  527 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.MARBLE, false, "Flat " + BridgeConstants.BridgeMaterial.MARBLE.getTextureName(), bridge));
/*  528 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SLATE, false, "Flat " + BridgeConstants.BridgeMaterial.SLATE.getTextureName(), bridge));
/*  529 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.ROUNDED_STONE, false, "Flat " + BridgeConstants.BridgeMaterial.ROUNDED_STONE.getTextureName(), bridge));
/*  530 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.POTTERY, false, "Flat " + BridgeConstants.BridgeMaterial.POTTERY.getTextureName(), bridge));
/*  531 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SANDSTONE, false, "Flat " + BridgeConstants.BridgeMaterial.SANDSTONE.getTextureName(), bridge));
/*  532 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.RENDERED, false, "Flat " + BridgeConstants.BridgeMaterial.RENDERED.getTextureName(), bridge));
/*      */     }
/*      */     else {
/*      */       
/*  536 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.MARBLE, "Flat " + BridgeConstants.BridgeMaterial.MARBLE.getTextureName(), getBrickSpan(this.length)));
/*  537 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SLATE, "Flat " + BridgeConstants.BridgeMaterial.SLATE.getTextureName(), getBrickSpan(this.length)));
/*  538 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.ROUNDED_STONE, "Flat " + BridgeConstants.BridgeMaterial.ROUNDED_STONE.getTextureName(), getBrickSpan(this.length)));
/*  539 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.POTTERY, "Flat " + BridgeConstants.BridgeMaterial.POTTERY.getTextureName(), getBrickSpan(this.length)));
/*  540 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SANDSTONE, "Flat " + BridgeConstants.BridgeMaterial.SANDSTONE.getTextureName(), getBrickSpan(this.length)));
/*  541 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.RENDERED, "Flat " + BridgeConstants.BridgeMaterial.RENDERED.getTextureName(), getBrickSpan(this.length)));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  548 */     this.fail = "";
/*  549 */     this.reason = "";
/*  550 */     bridge = "";
/*  551 */     if (!getResponder().isOnSurface()) {
/*      */       
/*  553 */       boolean insta = (getResponder().getPower() > 1 && Servers.isThisATestServer());
/*  554 */       PlanBridgeCheckResult res = PlanBridgeChecks.checkCeilingClearance(getResponder(), this.length, this.start, this.end, this.dir, insta);
/*  555 */       if (res.failed()) {
/*      */         
/*  557 */         this.fail = "Too close";
/*  558 */         this.reason = res.pMsg();
/*  559 */         if (insta) {
/*  560 */           getResponder().getCommunicator().sendNormalServerMessage(res.pMsg());
/*      */         }
/*  562 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.WOOD, true, "Arched " + BridgeConstants.BridgeMaterial.WOOD.getTextureName(), bridge));
/*  563 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.BRICK, true, "Arched " + BridgeConstants.BridgeMaterial.BRICK.getTextureName(), bridge));
/*  564 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.MARBLE, true, "Arched " + BridgeConstants.BridgeMaterial.MARBLE.getTextureName(), bridge));
/*  565 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SLATE, true, "Arched " + BridgeConstants.BridgeMaterial.SLATE.getTextureName(), bridge));
/*  566 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.ROUNDED_STONE, true, "Arched " + BridgeConstants.BridgeMaterial.ROUNDED_STONE.getTextureName(), bridge));
/*  567 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.POTTERY, true, "Arched " + BridgeConstants.BridgeMaterial.POTTERY.getTextureName(), bridge));
/*  568 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SANDSTONE, true, "Arched " + BridgeConstants.BridgeMaterial.SANDSTONE.getTextureName(), bridge));
/*  569 */         bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.RENDERED, true, "Arched " + BridgeConstants.BridgeMaterial.RENDERED.getTextureName(), bridge));
/*      */       } 
/*      */     } 
/*  572 */     if (this.fail.length() == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  579 */       this.fail = "";
/*  580 */       this.reason = "";
/*  581 */       bridge = "";
/*  582 */       maxLength = 38;
/*  583 */       requiredSkill = getResponder().getSkills().getSkillOrLearn(1005);
/*      */       
/*  585 */       if (this.width > 2) {
/*      */         
/*  587 */         this.fail = "Too Wide";
/*  588 */         this.reason = "Wood bridges are limited to 2 tiles wide.";
/*      */       }
/*  590 */       else if (this.length < 2) {
/*      */         
/*  592 */         this.fail = "Too Short";
/*  593 */         this.reason = "Need to have a minium of 2 tiles to form an arch.";
/*      */       }
/*  595 */       else if (this.length * 2 > (PlanBridgeMethods.getHighest()).length) {
/*      */         
/*  597 */         this.fail = "Too Long";
/*  598 */         this.reason = "Arched wood bridges are restricted to " + ((PlanBridgeMethods.getHighest()).length >>> 1) + " tiles long";
/*      */       }
/*  600 */       else if (this.heightDiff > PlanBridgeMethods.getHighest()[this.length * 2]) {
/*      */         
/*  602 */         this.fail = "Too Steep";
/*  603 */         this.reason = "The slope of part of the bridge would exceed 20 dirt.";
/*      */       }
/*  605 */       else if (this.length > maxLength) {
/*      */         
/*  607 */         this.fail = "Too Long";
/*  608 */         this.reason = "Arched wood bridges are restricted to " + maxLength + " tiles long";
/*      */       }
/*  610 */       else if (hasLowSkill(requiredSkill, 20, maxLength, true)) {
/*      */         
/*  612 */         this.fail = "Low Skill";
/*  613 */         if (requiredSkill.getKnowledge(0.0D) < 10.0D) {
/*  614 */           this.reason = "You need at least 10 carpentry skill to make any arched wood bridge.";
/*      */         } else {
/*  616 */           this.reason = "You dont have enough carpentry skill for this length arched wood bridge.";
/*      */         }
/*      */       
/*  619 */       } else if (this.length > 11 && (this.start.getH() < 0 || this.end.getH() < 0)) {
/*      */         
/*  621 */         this.fail = "Too Low";
/*  622 */         this.reason = "Both ends of a wood arched bridge need to be 0 above water.";
/*      */ 
/*      */       
/*      */       }
/*  626 */       else if (doneWoodCheck) {
/*      */         
/*  628 */         this.fail = woodFail[0];
/*  629 */         this.reason = woodFail[1];
/*      */       }
/*      */       else {
/*      */         
/*  633 */         String[] reply = PlanBridgeMethods.isBuildingOk(BridgeConstants.BridgeMaterial.WOOD.getCode(), this.dir, onSurface, this.start, this.startFloorlevel, this.end, this.endFloorlevel);
/*  634 */         this.fail = reply[0];
/*  635 */         this.reason = reply[1];
/*      */       } 
/*      */ 
/*      */       
/*  639 */       if (this.fail.length() == 0)
/*      */       {
/*      */         
/*  642 */         bridge = addArchs(this.length, false);
/*      */       }
/*  644 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.WOOD, true, "Arched wood", bridge));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  651 */       this.fail = "";
/*  652 */       this.reason = "";
/*  653 */       bridge = "";
/*  654 */       maxLength = 38;
/*  655 */       requiredSkill = getResponder().getSkills().getSkillOrLearn(1013);
/*      */       
/*  657 */       if (this.width > 3) {
/*      */         
/*  659 */         this.fail = "Too Wide";
/*  660 */         this.reason = "Brick bridges are limited to 3 tiles wide.";
/*      */       }
/*  662 */       else if (this.length < 2) {
/*      */         
/*  664 */         this.fail = "Too Short";
/*  665 */         this.reason = "Need to have a minium of 2 tiles to form an arch.";
/*      */       }
/*  667 */       else if (this.length * 2 > (PlanBridgeMethods.getHighest()).length) {
/*      */         
/*  669 */         this.fail = "Too Long";
/*  670 */         this.reason = "Arched brick bridges are restricted to " + ((PlanBridgeMethods.getHighest()).length >>> 1) + " tiles long";
/*      */       }
/*  672 */       else if (this.heightDiff > PlanBridgeMethods.getHighest()[this.length * 2]) {
/*      */         
/*  674 */         this.fail = "Too Steep";
/*  675 */         this.reason = "The slope of part of the bridge would exceed 20 dirt.";
/*      */       }
/*  677 */       else if (this.length > maxLength) {
/*      */         
/*  679 */         this.fail = "Too Long";
/*  680 */         this.reason = "Arched brick bridges are restricted to " + maxLength + " tiles long";
/*      */       }
/*  682 */       else if (hasLowSkill(requiredSkill, 50, maxLength, true)) {
/*      */         
/*  684 */         this.fail = "Low Skill";
/*  685 */         if (requiredSkill.getKnowledge(0.0D) < 50.0D) {
/*  686 */           this.reason = "You need at least 50 masonry skill to make any arched brick bridge.";
/*      */         } else {
/*  688 */           this.reason = "You dont have enough masonry skill for this length arched brick bridge.";
/*      */         }
/*      */       
/*  691 */       } else if (this.length > 8 && (this.start.getH() < 0 || this.end.getH() < 0)) {
/*      */         
/*  693 */         this.fail = "Too Low";
/*  694 */         this.reason = "Both ends of a brick arched bridge need to be 0 above water.";
/*      */ 
/*      */       
/*      */       }
/*  698 */       else if (doneStoneCheck) {
/*      */         
/*  700 */         this.fail = stoneFail[0];
/*  701 */         this.reason = stoneFail[1];
/*      */       }
/*      */       else {
/*      */         
/*  705 */         String[] reply = PlanBridgeMethods.isBuildingOk(BridgeConstants.BridgeMaterial.BRICK.getCode(), this.dir, onSurface, this.start, this.startFloorlevel, this.end, this.endFloorlevel);
/*  706 */         this.fail = reply[0];
/*  707 */         this.reason = reply[1];
/*  708 */         stoneFail = reply;
/*  709 */         doneStoneCheck = true;
/*      */       } 
/*      */ 
/*      */       
/*  713 */       if (this.fail.length() == 0) {
/*      */ 
/*      */         
/*  716 */         bridge = addArchs(this.length, true);
/*  717 */         if (Servers.isThisATestServer() && getResponder().getPower() >= 2)
/*  718 */           getResponder().getCommunicator().sendNormalServerMessage("(" + this.length + ":" + bridge + ")"); 
/*      */       } 
/*  720 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.BRICK, true, "Arched brick", bridge));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  727 */       this.fail = "";
/*  728 */       this.reason = "";
/*  729 */       bridge = "";
/*  730 */       maxLength = 38;
/*  731 */       requiredSkill = getResponder().getSkills().getSkillOrLearn(1013);
/*      */       
/*  733 */       if (this.width > 3) {
/*      */         
/*  735 */         this.fail = "Too Wide";
/*  736 */         this.reason = "Marble bridges are limited to 3 tiles wide.";
/*      */       }
/*  738 */       else if (this.length < 2) {
/*      */         
/*  740 */         this.fail = "Too Short";
/*  741 */         this.reason = "Need to have a minium of 2 tiles to form an arch.";
/*      */       }
/*  743 */       else if (this.length * 2 > (PlanBridgeMethods.getHighest()).length) {
/*      */         
/*  745 */         this.fail = "Too Long";
/*  746 */         this.reason = "Arched marble bridges are restricted to " + ((PlanBridgeMethods.getHighest()).length >>> 1) + " tiles long";
/*      */       }
/*  748 */       else if (this.heightDiff > PlanBridgeMethods.getHighest()[this.length * 2]) {
/*      */         
/*  750 */         this.fail = "Too Steep";
/*  751 */         this.reason = "The slope of part of the bridge would exceed 20 dirt.";
/*      */       }
/*  753 */       else if (this.length > maxLength) {
/*      */         
/*  755 */         this.fail = "Too Long";
/*  756 */         this.reason = "Arched marble bridges are restricted to " + maxLength + " tiles long";
/*      */       }
/*  758 */       else if (hasLowSkill(requiredSkill, 60, maxLength, true)) {
/*      */         
/*  760 */         this.fail = "Low Skill";
/*  761 */         if (requiredSkill.getKnowledge(0.0D) < 60.0D) {
/*  762 */           this.reason = "You need at least 60 masonry skill to make any arched marble bridge.";
/*      */         } else {
/*  764 */           this.reason = "You dont have enough masonry skill for this length arched marble bridge.";
/*      */         }
/*      */       
/*  767 */       } else if (this.length > 8 && (this.start.getH() < 0 || this.end.getH() < 0)) {
/*      */         
/*  769 */         this.fail = "Too Low";
/*  770 */         this.reason = "Both ends of a marble arched bridge need to be 0 above water.";
/*      */ 
/*      */       
/*      */       }
/*  774 */       else if (doneStoneCheck) {
/*      */         
/*  776 */         this.fail = stoneFail[0];
/*  777 */         this.reason = stoneFail[1];
/*      */       }
/*      */       else {
/*      */         
/*  781 */         String[] reply = PlanBridgeMethods.isBuildingOk(BridgeConstants.BridgeMaterial.MARBLE.getCode(), this.dir, onSurface, this.start, this.startFloorlevel, this.end, this.endFloorlevel);
/*  782 */         this.fail = reply[0];
/*  783 */         this.reason = reply[1];
/*  784 */         stoneFail = reply;
/*  785 */         doneStoneCheck = true;
/*      */       } 
/*      */ 
/*      */       
/*  789 */       if (this.fail.length() == 0)
/*      */       {
/*      */         
/*  792 */         bridge = addArchs(this.length, true);
/*      */       }
/*  794 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.MARBLE, true, "Arched " + BridgeConstants.BridgeMaterial.MARBLE.getTextureName(), bridge));
/*  795 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SLATE, true, "Arched " + BridgeConstants.BridgeMaterial.SLATE.getTextureName(), bridge));
/*  796 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.ROUNDED_STONE, true, "Arched " + BridgeConstants.BridgeMaterial.ROUNDED_STONE.getTextureName(), bridge));
/*  797 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.POTTERY, true, "Arched " + BridgeConstants.BridgeMaterial.POTTERY.getTextureName(), bridge));
/*  798 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.SANDSTONE, true, "Arched " + BridgeConstants.BridgeMaterial.SANDSTONE.getTextureName(), bridge));
/*  799 */       bridges.append(addBridgeEntry(BridgeConstants.BridgeMaterial.RENDERED, true, "Arched " + BridgeConstants.BridgeMaterial.RENDERED.getTextureName(), bridge));
/*      */     } 
/*      */     
/*  802 */     buf.append("table{rows=\"" + (this.bridgeCount + 1) + "\";cols=\"3\";");
/*  803 */     buf.append(bridges);
/*      */ 
/*      */ 
/*      */     
/*  807 */     buf.append("radio{group=\"bridgereply\";id=\"0,\";selected=\"true\"};label{text=\"None\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */     
/*  811 */     buf.append("}");
/*      */     
/*  813 */     buf.append("label{text=\"\"}");
/*  814 */     buf.append("label{type=\"bolditalic\";text=\"Warning: As a bridge is a structure you will not be able to terraform or plant under it.\"}");
/*  815 */     buf.append("label{text=\"\"}");
/*      */     
/*  817 */     buf.append(createAnswerButton2("Next"));
/*      */     
/*  819 */     int bmlHeight = this.bmlLines * 15 + this.iconLines * 34 + 50;
/*  820 */     int bmlWidth = Math.max(480, 55 + (this.length + 2) * 34);
/*      */     
/*  822 */     getResponder().getCommunicator().sendBml(bmlWidth, bmlHeight, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private String addSlopeEntry(int maxSlope) {
/*  827 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*  830 */     int[] hts = PlanBridgeMethods.calcArch(getResponder(), maxSlope, this.length, this.start, this.end);
/*      */     
/*  832 */     boolean isOk = true;
/*  833 */     for (int i = 0; i < hts.length - 1; i++) {
/*      */       
/*  835 */       int slope = hts[i] - hts[i + 1];
/*  836 */       if (Math.abs(slope) > maxSlope + 1) {
/*      */         
/*  838 */         isOk = false;
/*  839 */         this.fail = "Too Steep";
/*  840 */         this.reason = "Part of this bridge would have over a 20 slope due to the height difference between the ends.";
/*  841 */         if (Servers.isThisATestServer() && getResponder().getPower() >= 2)
/*  842 */           getResponder().getCommunicator().sendNormalServerMessage("Failed with slope " + slope + "(" + i + ") max:" + maxSlope); 
/*      */         break;
/*      */       } 
/*      */     } 
/*  846 */     if (isOk && !getResponder().isOnSurface()) {
/*      */       
/*  848 */       boolean insta = (getResponder().getPower() > 1 && Servers.isThisATestServer());
/*  849 */       PlanBridgeCheckResult res = PlanBridgeChecks.checkCeilingClearance(this.start, this.end, this.dir, hts, insta);
/*  850 */       if (res.failed()) {
/*      */         
/*  852 */         isOk = false;
/*  853 */         this.fail = "Too Close";
/*  854 */         this.reason = res.pMsg();
/*      */       } 
/*      */     } 
/*  857 */     if (isOk && this.steepnessSelected > maxSlope)
/*  858 */       this.steepnessSelected = maxSlope; 
/*  859 */     buf.append("radio{group=\"steepness\";id=\"" + maxSlope + "\";text=\"" + maxSlope + "\";enabled=\"" + isOk + "\";selected=\"" + ((this.steepnessSelected == maxSlope) ? 1 : 0) + "\"};");
/*      */     
/*  861 */     if (isOk) {
/*  862 */       buf.append("label{color=\"66,255,66\";text=\"Good\"};");
/*      */     } else {
/*  864 */       buf.append("label{color=\"255,66,66\";text=\"" + this.fail + "\";hover=\"" + this.reason + "\"};");
/*  865 */     }  return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String addSagEntry(int testSag) {
/*  870 */     StringBuilder buf = new StringBuilder();
/*      */ 
/*      */     
/*  873 */     String bridge = makeArch("a", this.length, 'C', "A");
/*  874 */     boolean isOk = ropeHeightsOk(bridge, testSag);
/*      */     
/*  876 */     String hover = "";
/*  877 */     if (this.reason.length() > 0) {
/*  878 */       hover = ";hover=\"" + this.reason + "\"";
/*      */     }
/*  880 */     if (isOk && this.steepnessSelected > testSag)
/*  881 */       this.steepnessSelected = testSag; 
/*  882 */     buf.append("radio{group=\"steepness\";id=\"" + testSag + "\";text=\"" + testSag + "%\";enabled=\"" + isOk + "\";selected=\"" + ((this.steepnessSelected == testSag) ? 1 : 0) + "\"" + hover + "};");
/*      */     
/*  884 */     if (isOk) {
/*  885 */       buf.append("label{color=\"66,255,66\";text=\"Good\"};");
/*      */     } else {
/*  887 */       buf.append("harray{image{src=\"img.gui.bridge.north\";size=\"12,12\";text=\"" + this.reason + "\"} label{color=\"255,66,66\";text=\"" + this.fail + "\"" + hover + "}};");
/*      */     } 
/*  889 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean ropeHeightsOk(String bridge, int testSag) {
/*  894 */     this.fail = "";
/*  895 */     this.reason = "";
/*      */ 
/*      */     
/*  898 */     int[] hts = PlanBridgeMethods.calcHeights(getResponder(), this.dir, BridgeConstants.BridgeMaterial.ROPE.getCode(), true, bridge, testSag, this.start, this.end);
/*      */ 
/*      */     
/*  901 */     for (int x = 0; x < hts.length - 1; x++) {
/*      */       
/*  903 */       int slope = Math.abs(hts[x] - hts[x + 1]);
/*  904 */       if (slope > 20) {
/*      */         
/*  906 */         this.fail = "Too Steep";
/*  907 */         this.reason = "Part of this rope bridge would have over a 20 slope due to the height difference between the ends.";
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  912 */     if (this.fail.length() == 0) {
/*      */ 
/*      */       
/*  915 */       int cx = this.start.getX();
/*  916 */       int cy = this.start.getY();
/*  917 */       int ch = this.start.getH();
/*  918 */       for (int ht : hts) {
/*      */         
/*  920 */         boolean onAnEnd = ((cx <= this.start.getX() && cy <= this.start.getY()) || cx > this.end.getX() || cy > this.end.getY());
/*      */         
/*  922 */         int tht = (int)(Zones.getHeightForNode(cx, cy, this.layer) * 10.0F);
/*  923 */         if (Servers.isThisATestServer() && getResponder().getPower() >= 2)
/*  924 */           getResponder().getCommunicator().sendNormalServerMessage("(" + onAnEnd + " x:" + cx + " y:" + cy + " ht:" + ht + " tht:" + tht + ")"); 
/*  925 */         if (!onAnEnd && ht < 5) {
/*      */           
/*  927 */           this.fail = "Too Low";
/*  928 */           this.reason = "Part of this rope bridge would be in the water.";
/*      */           break;
/*      */         } 
/*  931 */         if (!onAnEnd && ht < tht + 5) {
/*      */           
/*  933 */           this.fail = "Too Low";
/*  934 */           this.reason = "Part of this rope bridge would not be suspended above the ground.";
/*      */           break;
/*      */         } 
/*  937 */         if (ht < ch - 20 || ch < ht - 20) {
/*      */           
/*  939 */           this.fail = "Too Steep";
/*  940 */           this.reason = "Part of this rope bridge would have over a 20 slope due to the height difference between the ends.";
/*      */           break;
/*      */         } 
/*  943 */         ch = ht;
/*  944 */         if (this.dir == 0 || this.dir == 4) {
/*  945 */           cy++;
/*      */         } else {
/*  947 */           cx++;
/*      */         } 
/*      */       } 
/*  950 */     }  return (this.fail.length() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String addBridgeEntry(BridgeConstants.BridgeMaterial floorMaterial, String aTypeName, String spans) {
/*  956 */     this.fail = "";
/*      */ 
/*      */     
/*  959 */     String[] sps = spans.split(",");
/*  960 */     Set<String> spanset = new HashSet<>();
/*  961 */     for (String s : sps)
/*      */     {
/*  963 */       spanset.add(s);
/*      */     }
/*      */ 
/*      */     
/*  967 */     StringBuilder buf = new StringBuilder();
/*  968 */     for (String s : spanset)
/*      */     {
/*  970 */       buf.append(addBridgeEntry(floorMaterial, false, aTypeName, s));
/*      */     }
/*  972 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String addBridge() {
/*  977 */     StringBuilder buf = new StringBuilder();
/*  978 */     BridgeConstants.BridgeMaterial floorMaterial = BridgeConstants.BridgeMaterial.fromByte(this.bridgeType);
/*      */     
/*  980 */     String img = "image{src=\"img.gui.bridge.";
/*  981 */     String size = "\";size=\"32,32\"";
/*      */     
/*  983 */     String material = floorMaterial.getTextureName() + ".";
/*      */     
/*  985 */     buf.append("varray{table{rows=\"1\";cols=\"" + (this.length + 2) + "\";");
/*  986 */     if (this.dir == 0) {
/*  987 */       buf.append("image{src=\"img.gui.bridge.north\";size=\"32,32\";text=\"north\"}");
/*      */     } else {
/*  989 */       buf.append("image{src=\"img.gui.bridge.west\";size=\"32,32\";text=\"west\"}");
/*  990 */     }  for (char c : this.bridgePlan.toCharArray()) {
/*      */       
/*  992 */       buf.append("image{src=\"img.gui.bridge." + material);
/*  993 */       buf.append(getType(c));
/*      */       
/*  995 */       buf.append("\";size=\"32,32\";text=\"" + getAltText(c) + "\"};");
/*      */     } 
/*  997 */     if (this.dir == 0) {
/*  998 */       buf.append("image{src=\"img.gui.bridge.south\";size=\"32,32\";text=\"south\"}");
/*      */     } else {
/* 1000 */       buf.append("image{src=\"img.gui.bridge.east\";size=\"32,32\";text=\"east\"}");
/* 1001 */     }  buf.append("};");
/* 1002 */     buf.append("image{src=\"img.gui.bridge.blank\";size=\"2,2\"}};");
/*      */     
/* 1004 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String addBridgeEntry(BridgeConstants.BridgeMaterial bridgeMaterial, boolean aArched, String aTypeName, String bridge) {
/* 1010 */     StringBuilder buf = new StringBuilder();
/* 1011 */     if (this.fail.length() > 0) {
/*      */       
/* 1013 */       this.bmlLines++;
/*      */ 
/*      */       
/* 1016 */       buf.append("label{text=\"\"};label{text=\"" + aTypeName + "\"};harray{image{src=\"img.gui.bridge.north\";size=\"12,12\";text=\"" + this.reason + "\"} label{color=\"255,66,66\";text=\"" + this.fail + "\";hover=\"" + this.reason + "\"}};");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1023 */       this.iconLines++;
/*      */       
/* 1025 */       String img = "image{src=\"img.gui.bridge.";
/* 1026 */       String size = "\";size=\"32,32\"";
/* 1027 */       String blank = "image{src=\"img.gui.bridge.blank\";size=\"2,34\";text=\"\"}";
/* 1028 */       String material = bridgeMaterial.getTextureName().replace(" ", "") + ".";
/* 1029 */       buf.append("harray{image{src=\"img.gui.bridge.blank\";size=\"2,34\";text=\"\"};radio{group=\"bridgereply\";id=\"" + bridgeMaterial.getCode() + "," + aArched + "," + bridge + "\"}};harray{label{text=\"" + aTypeName + "\"};" + "image{src=\"img.gui.bridge.blank\";size=\"2,34\";text=\"\"}" + "};");
/*      */ 
/*      */       
/* 1032 */       buf.append("varray{table{rows=\"1\";cols=\"" + (bridge.length() + 2) + "\";");
/* 1033 */       if (this.dir == 0) {
/* 1034 */         buf.append("image{src=\"img.gui.bridge.north\";size=\"32,32\";text=\"north\"}");
/*      */       } else {
/* 1036 */         buf.append("image{src=\"img.gui.bridge.west\";size=\"32,32\";text=\"west\"}");
/* 1037 */       }  for (char c : bridge.toCharArray()) {
/*      */         
/* 1039 */         buf.append("image{src=\"img.gui.bridge." + material);
/* 1040 */         buf.append(getType(c));
/*      */         
/* 1042 */         buf.append("\";size=\"32,32\";text=\"" + getAltText(c) + "\"};");
/*      */       } 
/* 1044 */       if (this.dir == 0) {
/* 1045 */         buf.append("image{src=\"img.gui.bridge.south\";size=\"32,32\";text=\"south\"}");
/*      */       } else {
/* 1047 */         buf.append("image{src=\"img.gui.bridge.east\";size=\"32,32\";text=\"east\"}");
/* 1048 */       }  buf.append("};");
/* 1049 */       buf.append("image{src=\"img.gui.bridge.blank\";size=\"2,2\"}};");
/*      */     } 
/* 1051 */     this.bridgeCount++;
/* 1052 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String getType(char c) {
/* 1057 */     switch (c) {
/*      */       
/*      */       case 'A':
/* 1060 */         return "abutment.right";
/*      */       case 'a':
/* 1062 */         return "abutment.left";
/*      */       case 'B':
/* 1064 */         return "bracing.right";
/*      */       case 'b':
/* 1066 */         return "bracing.left";
/*      */       case 'C':
/* 1068 */         return "crown";
/*      */       case 'D':
/* 1070 */         return "double";
/*      */       case 'E':
/* 1072 */         return "end";
/*      */       case 'F':
/* 1074 */         return "floating";
/*      */       case 'S':
/* 1076 */         return "support";
/*      */     } 
/* 1078 */     return "unknown";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String getAltText(char c) {
/* 1084 */     switch (c) {
/*      */       
/*      */       case 'A':
/*      */       case 'a':
/* 1088 */         return "abutment";
/*      */       case 'B':
/*      */       case 'b':
/* 1091 */         return "bracing";
/*      */       case 'C':
/* 1093 */         return "crown";
/*      */       case 'D':
/* 1095 */         return "double bracing";
/*      */       case 'E':
/* 1097 */         return "double abutment";
/*      */       case 'F':
/* 1099 */         return "floating";
/*      */       case 'S':
/* 1101 */         return "support";
/*      */     } 
/* 1103 */     return "unknown";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String addArchs(int alength, boolean isStone) {
/* 1109 */     if (alength == 2)
/* 1110 */       return "aA"; 
/* 1111 */     if (isStone) {
/*      */       
/* 1113 */       if (alength == 3) {
/* 1114 */         return "aDA";
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1120 */       if (alength < 9) {
/* 1121 */         return makeArch("ab", alength, 'F', "BA");
/*      */       }
/* 1123 */       return makeArch("ESab", alength, 'F', "BASE");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1128 */     return this.archWood[alength];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String makeArch(String oneEnd, int alength, char c, String farEnd) {
/* 1134 */     int middle = alength - oneEnd.length() * 2;
/* 1135 */     StringBuilder buf = new StringBuilder();
/* 1136 */     buf.append(oneEnd);
/* 1137 */     for (int i = 0; i < middle; i++)
/* 1138 */       buf.append(c); 
/* 1139 */     buf.append(farEnd);
/* 1140 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String getWoodSpan(int alength) {
/* 1145 */     if (alength < 8) {
/* 1146 */       return this.spansWood[alength];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1153 */     int supports = alength / 6;
/* 1154 */     int spans = supports - 1;
/* 1155 */     int middleLen = spans * 6 + 4;
/* 1156 */     int rem = alength - middleLen;
/* 1157 */     int left = rem / 2;
/* 1158 */     int right = rem - left;
/* 1159 */     StringBuilder buf = new StringBuilder();
/* 1160 */     buf.append("aCC".substring(0, left));
/*      */     
/* 1162 */     for (int i = 0; i < spans; i++)
/* 1163 */       buf.append("CASaCC"); 
/* 1164 */     buf.append("CASa");
/*      */     
/* 1166 */     StringBuilder bb = new StringBuilder("ACCC".substring(0, right));
/* 1167 */     buf.append(bb.reverse());
/* 1168 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String getBrickSpan(int alength) {
/* 1173 */     if (alength < 12) {
/* 1174 */       return this.spansBrick[alength];
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
/* 1185 */     int supports = alength / 6;
/* 1186 */     int spans = supports - 1;
/* 1187 */     int middleLen = spans * 6 + 1;
/* 1188 */     int rem = alength - middleLen;
/* 1189 */     int left = rem / 2;
/* 1190 */     int right = rem - left;
/* 1191 */     StringBuilder buf = new StringBuilder();
/* 1192 */     buf.append(this.spansBrick[left]);
/* 1193 */     for (int i = 0; i < spans; i++)
/* 1194 */       buf.append("SabCBA"); 
/* 1195 */     buf.append('S');
/* 1196 */     buf.append(this.spansBrick[right]);
/* 1197 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean hasLowSkill(Skill requiredSkill, int minSkill, int maxLength, boolean slidingScale) {
/* 1202 */     if (requiredSkill.getKnowledge(0.0D) < minSkill)
/* 1203 */       return true; 
/* 1204 */     if (slidingScale) {
/*      */ 
/*      */       
/* 1207 */       float as = (float)Math.min(requiredSkill.getKnowledge(0.0D), 99.0D);
/* 1208 */       float k = 90.0F / (99.0F - minSkill);
/* 1209 */       float a = 90.0F - (as - minSkill) * k;
/* 1210 */       float b = (float)Math.toRadians(a);
/* 1211 */       float c = (float)Math.sin(b);
/* 1212 */       float d = 1.0F - c;
/* 1213 */       float r = d * (maxLength - 5) + 5.0F;
/*      */ 
/*      */ 
/*      */       
/* 1217 */       if (r < this.length)
/* 1218 */         return true; 
/*      */     } 
/* 1220 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestionPage2() {
/* 1227 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1229 */     String bridgeArea = "Planned bridge area is " + this.length + " tile" + ((this.length == 1) ? "" : "s") + " long and " + this.width + " tile" + ((this.width == 1) ? "" : "s") + " wide.";
/*      */     
/* 1231 */     buf.append(getBmlHeaderWithScrollAndQuestion());
/* 1232 */     buf.append("label{text=\"" + bridgeArea + "\"};");
/* 1233 */     buf.append("label{type=\"bold\";text=\"Change bridge name here:\"}");
/* 1234 */     buf.append("input{maxchars=\"40\";id=\"bridgename\";text=\"" + this.bridgeName + "\"}");
/*      */     
/* 1236 */     buf.append("label{type=\"bolditalic\";text=\"Schematic of your bridge plan.\"}");
/* 1237 */     buf.append(addBridge());
/*      */     
/* 1239 */     if (this.arched && this.bridgeType != BridgeConstants.BridgeMaterial.ROPE.getCode()) {
/*      */ 
/*      */       
/* 1242 */       this.steepnessSelected = 40;
/* 1243 */       buf.append("label{type=\"bold\";text=\"Arched bridge max steepness.\"};");
/* 1244 */       buf.append("table{rows=\"1\";cols=\"8\";");
/* 1245 */       buf.append(addSlopeEntry(5));
/* 1246 */       buf.append(addSlopeEntry(10));
/* 1247 */       buf.append(addSlopeEntry(15));
/* 1248 */       buf.append(addSlopeEntry(20));
/* 1249 */       buf.append("}");
/*      */     }
/* 1251 */     else if (this.arched && this.bridgeType == BridgeConstants.BridgeMaterial.ROPE.getCode() && this.length > 1) {
/*      */ 
/*      */       
/* 1254 */       this.steepnessSelected = 40;
/*      */       
/* 1256 */       int minSag = calcMinSag();
/* 1257 */       int cols = Math.min((13 - minSag) * 2, 8);
/* 1258 */       buf.append("label{type=\"bold\";text=\"Rope bridge max saggyness.\"};");
/* 1259 */       buf.append("label{text=\"Your strength determines the minimum saggyness.\"};");
/* 1260 */       buf.append("table{rows=\"1\";cols=\"" + cols + "\";");
/* 1261 */       for (int sag = minSag; sag < 13; sag++) {
/* 1262 */         buf.append(addSagEntry(sag));
/*      */       }
/*      */       
/* 1265 */       buf.append("label{text=\"\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};label{text=\"\"};");
/*      */       
/* 1267 */       buf.append("}");
/*      */     }
/*      */     else {
/*      */       
/* 1271 */       this.steepnessSelected = 0;
/* 1272 */       buf.append("radio{group=\"steepness\";id=\"0\";text=\"0\";enabled=\"false\";selected=\"true\";hidden=\"true\"};");
/*      */     } 
/*      */ 
/*      */     
/* 1276 */     if (this.arched && this.bridgeType != BridgeConstants.BridgeMaterial.ROPE.getCode() && this.steepnessSelected > 20) {
/*      */       
/* 1278 */       buf.append("label{text=\"Could not work out a good steepness for this arched bridge.\"}");
/* 1279 */       buf.append(createAnswerButton2("Cancel"));
/*      */     }
/* 1281 */     else if (this.arched && this.bridgeType == BridgeConstants.BridgeMaterial.ROPE.getCode() && this.steepnessSelected > 12) {
/*      */       
/* 1283 */       buf.append("label{text=\"Could not work out a good saggyness for this rope bridge.\"}");
/* 1284 */       buf.append(createAnswerButton2("Cancel"));
/*      */     }
/*      */     else {
/*      */       
/* 1288 */       buf.append("label{text=\"Once satisified with name" + ((this.bridgeType == BridgeConstants.BridgeMaterial.ROPE.getCode()) ? " and saggyness" : (this.arched ? " and steepness for arch" : "")) + ", select 'Finalise'\"}");
/*      */       
/* 1290 */       buf.append(createAnswerButton2("Finalise"));
/*      */     } 
/*      */     
/* 1293 */     String sBridgeType = BridgeConstants.BridgeMaterial.fromByte(this.bridgeType).getName().toLowerCase();
/* 1294 */     String sTitle = "Name your " + ((this.bridgeType == BridgeConstants.BridgeMaterial.ROPE.getCode()) ? "" : (this.arched ? "arched " : "flat ")) + sBridgeType + " bridge";
/*      */     
/* 1296 */     int bmlHeight = 230 + (this.arched ? 100 : 0);
/* 1297 */     int bmlWidth = Math.max(370, 55 + (this.length + 2) * 34);
/* 1298 */     getResponder().getCommunicator().sendBml(bmlWidth, bmlHeight, true, true, buf.toString(), 200, 200, 200, sTitle);
/*      */   }
/*      */ 
/*      */   
/*      */   private int calcMinSag() {
/* 1303 */     Skill str = getResponder().getSkills().getSkillOrLearn(102);
/* 1304 */     return 12 - (int)(str.getKnowledge(0.0D) / 10.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void grabFloorLevels() {
/* 1310 */     int rFloorlevel = getResponder().getFloorLevel();
/* 1311 */     int tFloorlevel = this.targetFloorLevel;
/*      */ 
/*      */     
/* 1314 */     if (this.dir == 0) {
/*      */       
/* 1316 */       if (getResponder().getTileY() == this.start.getY() - 1) {
/*      */         
/* 1318 */         this.startFloorlevel = rFloorlevel;
/* 1319 */         this.endFloorlevel = tFloorlevel;
/*      */       }
/*      */       else {
/*      */         
/* 1323 */         this.startFloorlevel = tFloorlevel;
/* 1324 */         this.endFloorlevel = rFloorlevel;
/*      */       } 
/* 1326 */       this.end.setY(this.end.getY() - 1);
/*      */     }
/*      */     else {
/*      */       
/* 1330 */       if (getResponder().getTileX() == this.start.getX() - 1) {
/*      */         
/* 1332 */         this.startFloorlevel = rFloorlevel;
/* 1333 */         this.endFloorlevel = tFloorlevel;
/*      */       }
/*      */       else {
/*      */         
/* 1337 */         this.startFloorlevel = tFloorlevel;
/* 1338 */         this.endFloorlevel = rFloorlevel;
/*      */       } 
/* 1340 */       this.end.setX(this.end.getX() - 1);
/*      */     } 
/* 1342 */     this.heightDiff = Math.abs(this.end.getH() - this.start.getH());
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PlanBridgeQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
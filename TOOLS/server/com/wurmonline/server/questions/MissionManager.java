/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.ActionEntry;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.players.Achievement;
/*      */ import com.wurmonline.server.players.AchievementTemplate;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.skills.SkillTemplate;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.tutorial.Mission;
/*      */ import com.wurmonline.server.tutorial.MissionTargets;
/*      */ import com.wurmonline.server.tutorial.MissionTrigger;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.tutorial.Missions;
/*      */ import com.wurmonline.server.tutorial.SpecialEffects;
/*      */ import com.wurmonline.server.tutorial.TriggerEffect;
/*      */ import com.wurmonline.server.tutorial.TriggerEffects;
/*      */ import com.wurmonline.server.tutorial.Triggers2Effects;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Properties;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.regex.Pattern;
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
/*      */ public class MissionManager
/*      */   extends Question
/*      */   implements CounterTypes, TimeConstants, CreatureTypes
/*      */ {
/*   80 */   private int level = 0;
/*   81 */   private int missionId = 0;
/*   82 */   private int triggerId = 0;
/*   83 */   private int effectId = 0;
/*      */   
/*      */   private static final float REWSK1 = 0.001F;
/*      */   
/*      */   private static final float REWSK2 = 0.002F;
/*      */   
/*      */   private static final float REWSK3 = 0.01F;
/*      */   
/*      */   private static final float REWSK4 = 0.05F;
/*      */   
/*      */   private static final float REWSK5 = 0.1F;
/*      */   
/*      */   private static final float REWSK6 = 1.0F;
/*      */   private static final float REWSK7 = 10.0F;
/*      */   private static final float REWSK8 = 20.0F;
/*      */   private static final String percent = "%";
/*      */   private static final String percentComma = "%,";
/*      */   private static final int INTRO = 0;
/*      */   private static final int CREATE_MISSION = 1;
/*      */   private static final int EDIT_MISSION = 2;
/*      */   private static final int EDIT_TRIGGER = 3;
/*      */   private static final int CREATE_TRIGGER = 4;
/*      */   private static final int LIST_MISSIONS = 5;
/*      */   private static final int LIST_TRIGGERS = 6;
/*      */   private static final int EDIT_EFFECT = 7;
/*      */   private static final int CREATE_EFFECT = 8;
/*      */   private static final int LIST_EFFECTS = 9;
/*  110 */   private static Logger logger = Logger.getLogger(MissionManager.class.getName());
/*  111 */   private LinkedList<ItemTemplate> itemplates = new LinkedList<>();
/*  112 */   private LinkedList<Item> ritems = new LinkedList<>();
/*  113 */   private LinkedList<MissionTrigger> mtriggers = new LinkedList<>();
/*  114 */   private LinkedList<MissionTrigger> utriggers = new LinkedList<>();
/*  115 */   private LinkedList<SkillTemplate> stemplates = new LinkedList<>();
/*  116 */   private LinkedList<ActionEntry> actionEntries = new LinkedList<>();
/*  117 */   private LinkedList<Mission> missionsAvail = new LinkedList<>();
/*  118 */   private LinkedList<SpecialEffects> effectsAvail = new LinkedList<>();
/*  119 */   private LinkedList<CreatureTemplate> creaturesAvail = new LinkedList<>();
/*  120 */   private LinkedList<TriggerEffect> teffects = new LinkedList<>();
/*  121 */   private LinkedList<TriggerEffect> ueffects = new LinkedList<>();
/*  122 */   private LinkedList<AchievementTemplate> myAchievements = null;
/*  123 */   private LinkedList<Byte> creaturesTypes = new LinkedList<>();
/*      */   
/*      */   private final String targName;
/*      */   private final long missionRulerId;
/*      */   private boolean listMineOnly = false;
/*      */   private boolean dontListMine = false;
/*      */   private boolean onlyCurrent = false;
/*  130 */   private int includeM = 0;
/*      */   private boolean incMInactive = true;
/*      */   private boolean typeSystem = true;
/*      */   private boolean typeGM = true;
/*      */   private boolean typePlayer = true;
/*  135 */   private long listForUser = -10L;
/*  136 */   private String userName = "";
/*  137 */   private String groupName = "";
/*      */   private boolean incTInactive = true;
/*  139 */   private int showT = 0;
/*      */   private boolean incEInactive = true;
/*  141 */   private int showE = 0;
/*      */   
/*  143 */   private long currentTargetId = -10L;
/*  144 */   private int sortBy = 2;
/*  145 */   private String origQuestion = "";
/*  146 */   private String origTitle = "";
/*  147 */   private String lastQuestion = "";
/*  148 */   private String lastTitle = "";
/*  149 */   private String errorText = "";
/*  150 */   private byte creatorType = 0;
/*      */ 
/*      */   
/*  153 */   private String sbacks = "";
/*      */   
/*  155 */   public static byte CAN_SEE_EPIC_MISSIONS = 2;
/*      */ 
/*      */   
/*      */   private static final String red = "color=\"255,127,127\"";
/*      */ 
/*      */   
/*      */   private static final String green = "color=\"127,255,127\"";
/*      */ 
/*      */   
/*      */   private static final String orange = "color=\"255,177,40\"";
/*      */ 
/*      */   
/*      */   private static final String blue = "color=\"140,140,255\";";
/*      */ 
/*      */   
/*      */   private static final String hoverActive = ";hover=\"Active\"";
/*      */ 
/*      */   
/*      */   private static final String hoverInactive = ";hover=\"Inactive\"";
/*      */ 
/*      */   
/*      */   private static final String hoverNoTriggers = ";hover=\"No Triggers\"";
/*      */ 
/*      */   
/*      */   private static final String hoverCurrentTarget = ";hover=\"Current Target\"";
/*      */ 
/*      */   
/*      */   private static final String hoverNotImplemented = ";hover=\"Not implemented (yet)\"";
/*      */ 
/*      */   
/*      */   private static final String line = "label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}";
/*      */ 
/*      */   
/*      */   public MissionManager(Creature aResponder, String aTitle, String aQuestion, long aTarget, String targetName, long _missionRulerId) {
/*  189 */     super(aResponder, aTitle, aQuestion, 86, aTarget);
/*  190 */     this.targName = targetName;
/*  191 */     this.missionRulerId = _missionRulerId;
/*  192 */     this.currentTargetId = aTarget;
/*  193 */     this.origQuestion = aQuestion;
/*  194 */     this.origTitle = aTitle;
/*  195 */     this.lastQuestion = aQuestion;
/*  196 */     this.lastTitle = aTitle;
/*  197 */     if (aResponder.getPower() <= 1) {
/*      */       
/*  199 */       this.listMineOnly = true;
/*  200 */       this.typeSystem = false;
/*  201 */       this.typeGM = false;
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
/*      */   public void answer(Properties aAnswers) {
/*  213 */     Item ruler = null;
/*      */     
/*      */     try {
/*  216 */       ruler = Items.getItem(this.missionRulerId);
/*  217 */       if (ruler.getOwnerId() != getResponder().getWurmId()) {
/*      */         
/*  219 */         getResponder().getCommunicator().sendNormalServerMessage("You are not the ruler of this mission ruler.");
/*      */         
/*      */         return;
/*      */       } 
/*  223 */     } catch (NoSuchItemException nsi) {
/*      */       
/*  225 */       getResponder().getCommunicator().sendNormalServerMessage("The mission ruler is gone!");
/*      */       return;
/*      */     } 
/*  228 */     setAnswer(aAnswers);
/*  229 */     parseAnswer(ruler);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean parseAnswer(Item ruler) {
/*  234 */     boolean back = getBooleanProp("back");
/*  235 */     if (back)
/*      */     {
/*  237 */       return parseBack();
/*      */     }
/*  239 */     switch (this.level) {
/*      */       
/*      */       case 0:
/*  242 */         return parseIntro();
/*      */       case 1:
/*      */       case 2:
/*  245 */         return parseMission(ruler);
/*      */       case 5:
/*  247 */         return parseMissionList();
/*      */       case 3:
/*      */       case 4:
/*  250 */         return parseTrigger(ruler);
/*      */       case 6:
/*  252 */         return parseTriggerList();
/*      */       case 7:
/*      */       case 8:
/*  255 */         return parseEffect(ruler);
/*      */       case 9:
/*  257 */         return parseEffectsList();
/*      */     } 
/*  259 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean parseBack() {
/*  265 */     String[] backs = this.sbacks.split(Pattern.quote("|"));
/*  266 */     if (backs.length > 0) {
/*      */       
/*  268 */       this.errorText = "";
/*      */       
/*  270 */       StringBuilder buf = new StringBuilder();
/*  271 */       if (backs.length > 1) {
/*      */         
/*  273 */         buf.append(backs[0]);
/*  274 */         for (int s = 1; s < backs.length - 1; s++)
/*  275 */           buf.append("|" + backs[s]); 
/*      */       } 
/*  277 */       this.sbacks = buf.toString();
/*  278 */       String[] lparts = backs[backs.length - 1].split(",");
/*  279 */       int newLevel = Integer.parseInt(lparts[0]);
/*  280 */       this.missionId = Integer.parseInt(lparts[1]);
/*  281 */       this.triggerId = Integer.parseInt(lparts[2]);
/*  282 */       this.effectId = Integer.parseInt(lparts[3]);
/*  283 */       switch (newLevel) {
/*      */ 
/*      */ 
/*      */         
/*      */         case 0:
/*  288 */           return showIntro();
/*      */ 
/*      */         
/*      */         case 1:
/*      */         case 2:
/*  293 */           return editMission(this.missionId, (Properties)null);
/*      */ 
/*      */         
/*      */         case 5:
/*  297 */           this.sortBy = 2;
/*  298 */           return showMissionList();
/*      */ 
/*      */         
/*      */         case 3:
/*      */         case 4:
/*  303 */           return editTrigger(this.triggerId, (Properties)null);
/*      */ 
/*      */         
/*      */         case 6:
/*  307 */           this.sortBy = 2;
/*  308 */           return showTriggerList(this.missionId);
/*      */ 
/*      */         
/*      */         case 7:
/*      */         case 8:
/*  313 */           return editEffect(this.effectId, (Properties)null);
/*      */ 
/*      */         
/*      */         case 9:
/*  317 */           this.sortBy = 2;
/*  318 */           return showEffectsList(this.missionId, this.triggerId);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } else {
/*  325 */       return showIntro();
/*      */     } 
/*  327 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean parseIntro() {
/*  332 */     this.sbacks = "0,0,0,0";
/*  333 */     this.missionId = 0;
/*  334 */     this.triggerId = 0;
/*  335 */     this.effectId = 0;
/*  336 */     if (getResponder().getPower() > 0) {
/*      */       
/*  338 */       this.listMineOnly = getBooleanProp("listmine");
/*  339 */       this.dontListMine = getBooleanProp("nolistmine");
/*  340 */       this.onlyCurrent = getBooleanProp("onlyCurrent");
/*  341 */       this.includeM = getIntProp("includeM");
/*  342 */       this.incMInactive = getBooleanProp("incMInactive");
/*  343 */       this.typeSystem = getBooleanProp("typeSystem");
/*  344 */       this.typeGM = getBooleanProp("typeGM");
/*  345 */       this.typePlayer = getBooleanProp("typePlayer");
/*      */     } 
/*  347 */     this.groupName = getStringProp("groupName");
/*  348 */     if (this.groupName == null)
/*  349 */       this.groupName = ""; 
/*  350 */     String specialName = getStringProp("specialName");
/*  351 */     parsePlayerName(specialName);
/*      */     
/*  353 */     this.incTInactive = getBooleanProp("incTInactive");
/*  354 */     this.incEInactive = getBooleanProp("incEInactive");
/*  355 */     this.showE = getIntProp("showE");
/*      */     
/*  357 */     boolean listMissions = getBooleanProp("listMissions");
/*  358 */     boolean listTriggers = getBooleanProp("listTriggers");
/*  359 */     boolean listEffects = getBooleanProp("listEffects");
/*  360 */     boolean createMission = getBooleanProp("createMission");
/*  361 */     boolean createTrigger = getBooleanProp("createTrigger");
/*  362 */     boolean createEffect = getBooleanProp("createEffect");
/*  363 */     this.sortBy = 2;
/*      */     
/*  365 */     if (listMissions)
/*  366 */       return showMissionList(); 
/*  367 */     if (listTriggers)
/*  368 */       return showTriggerList(0); 
/*  369 */     if (listEffects)
/*  370 */       return showEffectsList(0, 0); 
/*  371 */     if (createMission)
/*  372 */       return createNewMission((Properties)null); 
/*  373 */     if (createTrigger)
/*  374 */       return createNewTrigger((Properties)null); 
/*  375 */     if (createEffect)
/*  376 */       return createNewEffect((Properties)null); 
/*  377 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean parseMissionList() {
/*  382 */     this.missionId = 0;
/*  383 */     this.triggerId = 0;
/*  384 */     boolean filter = getBooleanProp("filter");
/*  385 */     boolean editMission = getBooleanProp("editMission");
/*  386 */     boolean showStats = getBooleanProp("showStats");
/*  387 */     boolean listTriggers = getBooleanProp("listTriggers");
/*  388 */     boolean listEffects = getBooleanProp("listEffects");
/*  389 */     boolean createMission = getBooleanProp("createMission");
/*      */     
/*  391 */     if (filter) {
/*      */ 
/*      */ 
/*      */       
/*  395 */       if (getResponder().getPower() > 0) {
/*      */         
/*  397 */         this.includeM = Integer.parseInt(getAnswer().getProperty("includeM"));
/*  398 */         this.incMInactive = getBooleanProp("incMInactive");
/*      */       } 
/*  400 */       this.groupName = getAnswer().getProperty("groupName");
/*  401 */       if (this.groupName == null) {
/*  402 */         this.groupName = "";
/*      */       }
/*  404 */       String specialName = getAnswer().getProperty("specialName");
/*  405 */       parsePlayerName(specialName);
/*      */       
/*  407 */       return showMissionList();
/*      */     } 
/*  409 */     if (createMission) {
/*      */       
/*  411 */       this.sbacks += "|" + '\005' + ",0,0,0";
/*  412 */       return createNewMission((Properties)null);
/*      */     } 
/*  414 */     String sel = getAnswer().getProperty("sel");
/*  415 */     int mid = Integer.parseInt(sel);
/*  416 */     if (editMission) {
/*      */ 
/*      */       
/*  419 */       this.sbacks += "|" + '\005' + ",0,0,0";
/*  420 */       if (mid == 0)
/*  421 */         return createNewMission((Properties)null); 
/*  422 */       return editMission(mid, (Properties)null);
/*      */     } 
/*  424 */     if (showStats)
/*      */     {
/*      */       
/*  427 */       return showStats(mid);
/*      */     }
/*  429 */     if (listTriggers) {
/*      */       
/*  431 */       this.sortBy = 2;
/*  432 */       this.sbacks += "|" + '\005' + ",0,0,0";
/*  433 */       return showTriggerList(mid);
/*      */     } 
/*  435 */     if (listEffects) {
/*      */       
/*  437 */       this.sortBy = 2;
/*  438 */       this.sbacks += "|" + '\005' + ",0,0,0";
/*  439 */       return showEffectsList(mid, 0);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  445 */     for (String key : getAnswer().stringPropertyNames()) {
/*      */       
/*  447 */       if (key.startsWith("sort")) {
/*      */ 
/*      */         
/*  450 */         String sid = key.substring(4);
/*  451 */         this.sortBy = Integer.parseInt(sid);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  456 */     return showMissionList();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean parseTriggerList() {
/*  462 */     boolean filter = getBooleanProp("filter");
/*  463 */     boolean editTrigger = getBooleanProp("editTrigger");
/*  464 */     boolean listEffects = getBooleanProp("listEffects");
/*  465 */     boolean createTrigger = getBooleanProp("createTrigger");
/*      */     
/*  467 */     if (filter) {
/*      */ 
/*      */       
/*  470 */       this.incTInactive = getBooleanProp("incTInactive");
/*  471 */       this.showT = Integer.parseInt(getAnswer().getProperty("showT"));
/*      */       
/*  473 */       return showTriggerList(this.missionId);
/*      */     } 
/*  475 */     if (createTrigger) {
/*      */       
/*  477 */       this.sbacks += "|" + '\006' + "," + this.missionId + ",0,0";
/*  478 */       return createNewTrigger((Properties)null);
/*      */     } 
/*  480 */     String sel = getAnswer().getProperty("sel");
/*  481 */     int tid = Integer.parseInt(sel);
/*  482 */     if (editTrigger) {
/*      */ 
/*      */       
/*  485 */       this.sbacks += "|" + '\006' + "," + this.missionId + ",0,0";
/*      */       
/*  487 */       if (tid == 0)
/*  488 */         return createNewTrigger((Properties)null); 
/*  489 */       return editTrigger(tid, (Properties)null);
/*      */     } 
/*  491 */     if (listEffects) {
/*      */       
/*  493 */       this.sortBy = 2;
/*  494 */       this.sbacks += "|" + '\006' + "," + this.missionId + ",0,0";
/*  495 */       return showEffectsList(this.missionId, this.triggerId);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  501 */     for (String key : getAnswer().stringPropertyNames()) {
/*      */       
/*  503 */       if (key.startsWith("sort")) {
/*      */ 
/*      */         
/*  506 */         String sid = key.substring(4);
/*  507 */         this.sortBy = Integer.parseInt(sid);
/*      */         break;
/*      */       } 
/*  510 */       if (key.startsWith("delT")) {
/*      */         
/*  512 */         String sid = key.substring(4);
/*  513 */         int trigId = Integer.parseInt(sid);
/*  514 */         MissionTrigger trg = MissionTriggers.getTriggerWithId(trigId);
/*  515 */         if (trg == null) {
/*      */           
/*  517 */           getResponder().getCommunicator().sendNormalServerMessage("Cannot find trigger!");
/*      */           
/*      */           break;
/*      */         } 
/*  521 */         trg.destroy();
/*  522 */         getResponder().getCommunicator().sendNormalServerMessage("You delete the trigger " + trg
/*  523 */             .getName() + ".");
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  529 */     return showTriggerList(this.missionId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean parseEffectsList() {
/*  535 */     boolean filter = getBooleanProp("filter");
/*  536 */     boolean editEffect = getBooleanProp("editEffect");
/*  537 */     boolean showTriggers = getBooleanProp("showTriggers");
/*  538 */     boolean createEffect = getBooleanProp("createEffect");
/*      */     
/*  540 */     if (filter) {
/*      */ 
/*      */       
/*  543 */       this.incEInactive = getBooleanProp("incEInactive");
/*  544 */       this.showE = Integer.parseInt(getAnswer().getProperty("showE"));
/*      */       
/*  546 */       return showEffectsList(this.missionId, this.triggerId);
/*      */     } 
/*  548 */     if (createEffect) {
/*      */       
/*  550 */       this.sbacks += "|" + '\t' + "," + this.missionId + "," + this.triggerId + ",0";
/*  551 */       return createNewEffect((Properties)null);
/*      */     } 
/*  553 */     String sel = getAnswer().getProperty("sel");
/*  554 */     int eid = Integer.parseInt(sel);
/*  555 */     if (editEffect) {
/*      */ 
/*      */       
/*  558 */       this.sbacks += "|" + '\t' + "," + this.missionId + "," + this.triggerId + ",0";
/*      */       
/*  560 */       if (eid == 0)
/*  561 */         return createNewEffect((Properties)null); 
/*  562 */       return editEffect(eid, (Properties)null);
/*      */     } 
/*  564 */     if (showTriggers) {
/*      */       
/*  566 */       getResponder().getCommunicator().sendNormalServerMessage("Not Implemented (yet)");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/*  574 */       for (String key : getAnswer().stringPropertyNames()) {
/*      */         
/*  576 */         if (key.startsWith("sort")) {
/*      */ 
/*      */           
/*  579 */           String sid = key.substring(4);
/*  580 */           this.sortBy = Integer.parseInt(sid);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  585 */     return showEffectsList(this.missionId, this.triggerId);
/*      */   }
/*      */ 
/*      */   
/*      */   private void parsePlayerName(String playerName) {
/*  590 */     if (playerName != null && playerName.length() > 0) {
/*      */ 
/*      */       
/*  593 */       PlayerInfo pf = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */       
/*      */       try {
/*  596 */         pf.load();
/*  597 */         this.listForUser = pf.wurmId;
/*  598 */         this.userName = pf.getName();
/*      */       }
/*  600 */       catch (IOException iox) {
/*      */         
/*  602 */         getResponder().getCommunicator().sendNormalServerMessage("No such player: " + playerName + ".");
/*  603 */         this.listForUser = -10L;
/*  604 */         this.userName = "";
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  609 */       this.listForUser = -10L;
/*  610 */       this.userName = "";
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean showIntro() {
/*  616 */     MissionManager mm = new MissionManager(getResponder(), this.origTitle, this.origQuestion, this.target, this.targName, this.missionRulerId);
/*      */     
/*  618 */     cloneValues(mm);
/*  619 */     mm.level = 0;
/*  620 */     mm.sendQuestion();
/*  621 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showMissionList() {
/*  628 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  629 */       getResponder().getLogger().info(getResponder() + ": Listing MISSIONS");
/*      */     }
/*  631 */     MissionManager mm = new MissionManager(getResponder(), "Mission list", "Mission list", this.target, this.targName, this.missionRulerId);
/*      */     
/*  633 */     cloneValues(mm);
/*  634 */     mm.level = 5;
/*  635 */     mm.sendMissionList();
/*  636 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean editMission(int mid, @Nullable Properties ans) {
/*  641 */     String name = "";
/*  642 */     if (ans == null) {
/*      */ 
/*      */       
/*  645 */       if (mid == 0) {
/*      */         
/*  647 */         getResponder().getCommunicator().sendNormalServerMessage("No mission selected!");
/*  648 */         return false;
/*      */       } 
/*  650 */       Mission msn = Missions.getMissionWithId(mid);
/*  651 */       if (msn == null) {
/*      */         
/*  653 */         getResponder().getCommunicator().sendNormalServerMessage("Cannot find mission!");
/*  654 */         return false;
/*      */       } 
/*  656 */       name = msn.getName();
/*      */     }
/*      */     else {
/*      */       
/*  660 */       name = ans.getProperty("name", "");
/*      */     } 
/*      */ 
/*      */     
/*  664 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  665 */       getResponder().getLogger().info(getResponder() + ": Edit MISSION with name " + name + " and id " + mid);
/*      */     }
/*  667 */     MissionManager mm = new MissionManager(getResponder(), "Edit mission", "Edit mission:", this.target, this.targName, this.missionRulerId);
/*      */     
/*  669 */     cloneValues(mm);
/*  670 */     if (ans == null) {
/*      */       
/*  672 */       mm.level = 2;
/*  673 */       mm.missionId = mid;
/*      */     }
/*      */     else {
/*      */       
/*  677 */       getResponder().getCommunicator().sendAlertServerMessage(this.errorText);
/*  678 */       mm.level = this.level;
/*  679 */       mm.setAnswer(getAnswer());
/*      */     } 
/*  681 */     mm.sendManageMission();
/*  682 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean editTrigger(int tid, @Nullable Properties ans) {
/*  687 */     String name = "";
/*  688 */     if (ans == null) {
/*      */       
/*  690 */       if (tid == 0) {
/*      */         
/*  692 */         getResponder().getCommunicator().sendNormalServerMessage("No trigger selected!");
/*  693 */         return false;
/*      */       } 
/*  695 */       MissionTrigger trg = MissionTriggers.getTriggerWithId(tid);
/*  696 */       if (trg == null) {
/*      */         
/*  698 */         this.errorText = "Cannot find trigger!";
/*  699 */         getResponder().getCommunicator().sendNormalServerMessage(this.errorText);
/*  700 */         return false;
/*      */       } 
/*  702 */       name = trg.getName();
/*      */     }
/*      */     else {
/*      */       
/*  706 */       name = ans.getProperty("name", "");
/*      */     } 
/*      */ 
/*      */     
/*  710 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  711 */       getResponder().getLogger().info(getResponder() + ": Edit TRIGGER with name " + name + " and id " + tid);
/*      */     }
/*  713 */     MissionManager mm = new MissionManager(getResponder(), "Mission triggers", "Edit mission trigger", this.target, this.targName, this.missionRulerId);
/*      */     
/*  715 */     cloneValues(mm);
/*  716 */     if (ans == null) {
/*      */       
/*  718 */       mm.triggerId = tid;
/*  719 */       mm.level = 3;
/*      */     }
/*      */     else {
/*      */       
/*  723 */       getResponder().getCommunicator().sendAlertServerMessage(this.errorText);
/*  724 */       mm.level = this.level;
/*  725 */       mm.setAnswer(ans);
/*      */     } 
/*  727 */     mm.sendManageTrigger();
/*  728 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean editEffect(int eid, @Nullable Properties ans) {
/*  733 */     String name = "";
/*  734 */     if (ans == null) {
/*      */       
/*  736 */       if (eid == 0) {
/*      */         
/*  738 */         getResponder().getCommunicator().sendNormalServerMessage("No mission selected!");
/*  739 */         return false;
/*      */       } 
/*  741 */       TriggerEffect eff = TriggerEffects.getTriggerEffect(eid);
/*  742 */       if (eff == null) {
/*      */         
/*  744 */         getResponder().getCommunicator().sendNormalServerMessage("Cannot find effect!");
/*  745 */         return false;
/*      */       } 
/*  747 */       name = eff.getName();
/*      */     }
/*      */     else {
/*      */       
/*  751 */       name = ans.getProperty("name", "");
/*      */     } 
/*  753 */     MissionManager mm = new MissionManager(getResponder(), "Trigger effect", "Edit trigger effect " + name, this.target, this.targName, this.missionRulerId);
/*      */     
/*  755 */     cloneValues(mm);
/*  756 */     mm.triggerId = 0;
/*  757 */     if (ans == null) {
/*      */       
/*  759 */       mm.effectId = eid;
/*  760 */       mm.level = 7;
/*      */     }
/*      */     else {
/*      */       
/*  764 */       getResponder().getCommunicator().sendAlertServerMessage(this.errorText);
/*  765 */       mm.level = this.level;
/*  766 */       mm.setAnswer(ans);
/*      */     } 
/*  768 */     mm.sendManageEffect();
/*  769 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showStats(int mid) {
/*  775 */     if (mid == 0) {
/*      */       
/*  777 */       getResponder().getCommunicator().sendNormalServerMessage("No mission selected!");
/*  778 */       return false;
/*      */     } 
/*  780 */     Mission msn = Missions.getMissionWithId(mid);
/*  781 */     if (msn == null) {
/*      */       
/*  783 */       getResponder().getCommunicator().sendNormalServerMessage("Cannot find mission!");
/*  784 */       return false;
/*      */     } 
/*      */     
/*  787 */     MissionStats ms = new MissionStats(getResponder(), "Mission statistics", "Statistics for " + msn.getName(), msn.getId());
/*  788 */     ms.setRoot(this);
/*  789 */     ms.sendQuestion();
/*  790 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showTriggerList(int mid) {
/*      */     String mTitle, mQuestion;
/*  797 */     if (mid == 0) {
/*      */ 
/*      */       
/*  800 */       mTitle = "Trigger List";
/*  801 */       mQuestion = "Listing all mission triggers";
/*      */     }
/*      */     else {
/*      */       
/*  805 */       Mission msn = Missions.getMissionWithId(mid);
/*  806 */       if (msn == null) {
/*      */         
/*  808 */         getResponder().getCommunicator().sendNormalServerMessage("Cannot find mission!");
/*  809 */         return false;
/*      */       } 
/*  811 */       mTitle = msn.getName() + " Trigger List";
/*  812 */       mQuestion = "Listing mission " + msn.getName() + " triggers";
/*      */     } 
/*      */ 
/*      */     
/*  816 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  817 */       getResponder().getLogger().info(getResponder() + ": " + mTitle);
/*      */     }
/*  819 */     MissionManager mm = new MissionManager(getResponder(), mTitle, mQuestion, this.target, this.targName, this.missionRulerId);
/*  820 */     cloneValues(mm);
/*  821 */     mm.level = 6;
/*  822 */     mm.missionId = mid;
/*  823 */     mm.sendTriggerList();
/*  824 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showEffectsList(int mid, int tid) {
/*      */     String mTitle, mQuestion;
/*  831 */     if (mid == 0) {
/*      */       
/*  833 */       if (tid == 0)
/*      */       {
/*      */         
/*  836 */         mTitle = "Mission Effects List";
/*  837 */         mQuestion = "Listing all mission trigger effects";
/*      */       }
/*      */       else
/*      */       {
/*  841 */         MissionTrigger mt = MissionTriggers.getTriggerWithId(tid);
/*  842 */         if (mt == null) {
/*      */           
/*  844 */           getResponder().getCommunicator().sendNormalServerMessage("Cannot find trigger!");
/*  845 */           return false;
/*      */         } 
/*  847 */         mTitle = "Trigger " + mt.getName() + " Effects List";
/*  848 */         mQuestion = "Listing trigger " + mt.getName() + "  effects";
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  853 */       Mission msn = Missions.getMissionWithId(mid);
/*  854 */       if (msn == null) {
/*      */         
/*  856 */         getResponder().getCommunicator().sendNormalServerMessage("Cannot find mission!");
/*  857 */         return false;
/*      */       } 
/*  859 */       if (tid == 0) {
/*      */ 
/*      */         
/*  862 */         mTitle = msn.getName() + " Trigger Effects List";
/*  863 */         mQuestion = "Listing mission " + msn.getName() + " trigger effects";
/*      */       }
/*      */       else {
/*      */         
/*  867 */         MissionTrigger mt = MissionTriggers.getTriggerWithId(tid);
/*  868 */         if (mt == null) {
/*      */           
/*  870 */           getResponder().getCommunicator().sendNormalServerMessage("Cannot find trigger!");
/*  871 */           return false;
/*      */         } 
/*  873 */         mTitle = msn.getName() + " Trigger " + mt.getName() + " Effects List";
/*  874 */         mQuestion = "Listing trigger " + mt.getName() + "  effects";
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  879 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  880 */       getResponder().getLogger().info(getResponder() + ": " + mTitle);
/*      */     }
/*  882 */     MissionManager mm = new MissionManager(getResponder(), mTitle, mQuestion, this.target, this.targName, this.missionRulerId);
/*  883 */     cloneValues(mm);
/*  884 */     mm.level = 9;
/*      */     
/*  886 */     mm.missionId = mid;
/*  887 */     mm.triggerId = tid;
/*  888 */     mm.sendEffectList();
/*  889 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean createNewMission(@Nullable Properties ans) {
/*  896 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  897 */       getResponder().getLogger().info(getResponder() + ": Create new MISSION");
/*      */     }
/*  899 */     MissionManager mm = new MissionManager(getResponder(), "Create New Mission", "Create New Mission:", this.target, this.targName, this.missionRulerId);
/*      */     
/*  901 */     cloneValues(mm);
/*  902 */     mm.level = 1;
/*  903 */     mm.missionId = 0;
/*  904 */     mm.setAnswer(ans);
/*  905 */     mm.sendManageMission();
/*  906 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean createNewTrigger(@Nullable Properties ans) {
/*  913 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  914 */       getResponder().getLogger().info(getResponder() + ": Create TRIGGER");
/*      */     }
/*  916 */     MissionManager mm = new MissionManager(getResponder(), "New mission trigger", "Create a new mission trigger", this.target, this.targName, this.missionRulerId);
/*      */     
/*  918 */     cloneValues(mm);
/*  919 */     mm.level = 4;
/*  920 */     mm.missionId = 0;
/*  921 */     mm.triggerId = 0;
/*  922 */     mm.setAnswer(ans);
/*  923 */     mm.sendManageTrigger();
/*  924 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean createNewEffect(@Nullable Properties ans) {
/*  931 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0) {
/*  932 */       getResponder().getLogger().info(getResponder() + ": Create EFFECT");
/*      */     }
/*  934 */     MissionManager mm = new MissionManager(getResponder(), "New mission effect", "Create a new mission trigger effect", this.target, this.targName, this.missionRulerId);
/*      */     
/*  936 */     cloneValues(mm);
/*  937 */     mm.level = 8;
/*  938 */     mm.missionId = 0;
/*  939 */     mm.triggerId = 0;
/*  940 */     mm.effectId = 0;
/*  941 */     mm.setAnswer(ans);
/*  942 */     mm.sendManageEffect();
/*  943 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void cloneValues(MissionManager mm) {
/*  948 */     mm.missionId = this.missionId;
/*  949 */     mm.triggerId = this.triggerId;
/*  950 */     mm.effectId = this.effectId;
/*  951 */     mm.listMineOnly = this.listMineOnly;
/*  952 */     mm.dontListMine = this.dontListMine;
/*  953 */     mm.onlyCurrent = this.onlyCurrent;
/*  954 */     mm.includeM = this.includeM;
/*  955 */     mm.incMInactive = this.incMInactive;
/*  956 */     mm.typeSystem = this.typeSystem;
/*  957 */     mm.typeGM = this.typeGM;
/*  958 */     mm.typePlayer = this.typePlayer;
/*  959 */     mm.listForUser = this.listForUser;
/*  960 */     mm.userName = this.userName;
/*  961 */     mm.groupName = this.groupName;
/*  962 */     mm.currentTargetId = this.currentTargetId;
/*  963 */     mm.origQuestion = this.origQuestion;
/*  964 */     mm.origTitle = this.origTitle;
/*  965 */     mm.lastQuestion = this.lastQuestion;
/*  966 */     mm.lastTitle = this.lastTitle;
/*  967 */     mm.incTInactive = this.incTInactive;
/*  968 */     mm.showT = this.showT;
/*  969 */     mm.incEInactive = this.incEInactive;
/*  970 */     mm.showE = this.showE;
/*  971 */     mm.sbacks = this.sbacks;
/*  972 */     mm.errorText = this.errorText;
/*  973 */     mm.sortBy = this.sortBy;
/*      */     
/*  975 */     mm.creatorType = this.creatorType;
/*      */     
/*  977 */     mm.itemplates = this.itemplates;
/*  978 */     mm.ritems = this.ritems;
/*  979 */     mm.mtriggers = this.mtriggers;
/*  980 */     mm.stemplates = this.stemplates;
/*  981 */     mm.actionEntries = this.actionEntries;
/*  982 */     mm.missionsAvail = this.missionsAvail;
/*  983 */     mm.effectsAvail = this.effectsAvail;
/*  984 */     mm.creaturesAvail = this.creaturesAvail;
/*  985 */     mm.myAchievements = this.myAchievements;
/*  986 */     mm.creaturesTypes = this.creaturesTypes;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void cloneAndSendManageEffect(@Nullable String sound) {
/*  991 */     MissionManager mm = new MissionManager(getResponder(), this.lastTitle, this.lastQuestion, this.target, this.targName, this.missionRulerId);
/*      */     
/*  993 */     cloneValues(mm);
/*  994 */     mm.level = this.level;
/*  995 */     mm.setAnswer(getAnswer());
/*      */     
/*  997 */     if (sound != null)
/*      */     {
/*  999 */       mm.getAnswer().setProperty("sound", sound);
/*      */     }
/* 1001 */     mm.sendManageEffect();
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean reshow() {
/* 1006 */     MissionManager mm = new MissionManager(getResponder(), this.lastTitle, this.lastQuestion, this.target, this.targName, this.missionRulerId);
/*      */     
/* 1008 */     cloneValues(mm);
/* 1009 */     mm.level = this.level;
/* 1010 */     mm.lastQuestion = this.lastQuestion;
/* 1011 */     mm.lastTitle = this.lastTitle;
/* 1012 */     mm.sbacks = this.sbacks;
/*      */     
/* 1014 */     switch (this.level) {
/*      */       
/*      */       case 0:
/* 1017 */         mm.sendQuestion();
/* 1018 */         return true;
/*      */       case 1:
/*      */       case 2:
/* 1021 */         mm.setAnswer(getAnswer());
/* 1022 */         mm.sendManageMission();
/* 1023 */         return true;
/*      */       case 5:
/* 1025 */         mm.sendMissionList();
/* 1026 */         return true;
/*      */       case 3:
/*      */       case 4:
/* 1029 */         mm.setAnswer(getAnswer());
/* 1030 */         mm.sendManageTrigger();
/* 1031 */         return true;
/*      */       case 6:
/* 1033 */         mm.sendTriggerList();
/* 1034 */         return true;
/*      */       case 7:
/*      */       case 8:
/* 1037 */         mm.setAnswer(getAnswer());
/* 1038 */         mm.sendManageEffect();
/* 1039 */         return true;
/*      */       
/*      */       case 9:
/* 1042 */         mm.sendEffectList();
/* 1043 */         return true;
/*      */     } 
/* 1045 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendManageTrigger() {
/* 1050 */     MissionTrigger trg = null;
/* 1051 */     String name = "";
/* 1052 */     boolean inactive = false;
/* 1053 */     String desc = "";
/* 1054 */     int itemUsedId = -10;
/* 1055 */     int actionId = 0;
/* 1056 */     long trgTarget = 0L;
/* 1057 */     boolean useCurrentTarget = true;
/* 1058 */     String tgtString = "";
/* 1059 */     boolean spawnpoint = false;
/* 1060 */     String trigsecs = "0";
/* 1061 */     int missionRequired = 0;
/* 1062 */     String stateFromString = "0.0";
/* 1063 */     String stateToString = "0.0";
/*      */     
/* 1065 */     if (getAnswer() != null) {
/*      */ 
/*      */       
/* 1068 */       name = getStringProp("name");
/* 1069 */       inactive = getBooleanProp("inactive");
/* 1070 */       desc = getStringProp("desc");
/*      */       
/* 1072 */       itemUsedId = indexItemTemplate("onItemCreatedId", "Item Created");
/* 1073 */       actionId = indexActionId("actionId", "action");
/* 1074 */       trgTarget = getLongProp("targetid");
/* 1075 */       useCurrentTarget = getBooleanProp("useCurrentTarget");
/* 1076 */       tgtString = MissionTriggers.getTargetAsString(getResponder(), trgTarget);
/* 1077 */       spawnpoint = getBooleanProp("spawnpoint");
/* 1078 */       trigsecs = getStringProp("seconds");
/* 1079 */       missionRequired = indexMission("missionRequired", "available missions");
/* 1080 */       stateFromString = getStringProp("stateFrom");
/* 1081 */       stateToString = getStringProp("stateTo");
/*      */     }
/* 1083 */     else if (this.triggerId > 0) {
/*      */       
/* 1085 */       trg = MissionTriggers.getTriggerWithId(this.triggerId);
/* 1086 */       if (trg != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1100 */         name = trg.getName();
/* 1101 */         inactive = trg.isInactive();
/* 1102 */         desc = trg.getDescription();
/* 1103 */         itemUsedId = trg.getItemUsedId();
/* 1104 */         actionId = trg.getOnActionPerformed();
/* 1105 */         trgTarget = trg.getTarget();
/* 1106 */         useCurrentTarget = (trgTarget == this.currentTargetId);
/* 1107 */         tgtString = MissionTriggers.getTargetAsString(getResponder(), trg.getTarget());
/* 1108 */         if (getResponder().getPower() > 0)
/* 1109 */           spawnpoint = trg.isSpawnPoint(); 
/* 1110 */         trigsecs = Integer.toString(trg.getSeconds());
/* 1111 */         missionRequired = trg.getMissionRequired();
/* 1112 */         stateFromString = Float.toString(trg.getStateRequired());
/* 1113 */         stateToString = Float.toString(trg.getStateEnd());
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1118 */       tgtString = "None";
/*      */     } 
/* 1120 */     String currentString = MissionTriggers.getTargetAsString(getResponder(), this.currentTargetId);
/*      */     
/* 1122 */     StringBuilder buf = new StringBuilder();
/* 1123 */     buf.append("border{border{size=\"20,20\";null;null;varray{rescale=\"true\";harray{label{type='bold';text=\"" + this.question + "    \"};label{type=\"bolditalic\";" + "color=\"255,127,127\"" + ";text=\"" + this.errorText + "\"}}}harray{button{id=\"back\";text=\"Back\"};label{text=\"  \"}}null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1142 */         getId() + "\"}");
/*      */     
/* 1144 */     buf.append("harray{label{text=\"Name (max 40 chars)\"};input{id=\"name\";maxchars=\"40\";text=\"" + name + "\"};label{text=\" \"};checkbox{id=\"inactive\";selected=\"" + inactive + "\"};label{text=\"Inactive \"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1151 */     buf.append("label{text=\"Description (max 100 chars)\"};");
/* 1152 */     buf.append("input{id=\"intro\";maxchars=\"400\"text=\"" + desc + "\";maxlines=\"1\"};");
/* 1153 */     buf.append("text{text=\"\"}");
/* 1154 */     buf.append("header{text=\"How triggered\"}");
/*      */     
/* 1156 */     buf.append("harray{label{text=\"On item used\"};" + 
/*      */         
/* 1158 */         dropdownItemTemplates("onItemCreatedId", itemUsedId, false) + "}");
/*      */     
/* 1160 */     String curColour = "";
/* 1161 */     if (trgTarget == this.currentTargetId && trgTarget > 0L)
/* 1162 */       curColour = "color=\"140,140,255\";"; 
/* 1163 */     if (getResponder().getPower() > 0) {
/*      */       
/* 1165 */       buf.append("harray{label{text=\"Action performed\"};" + 
/*      */           
/* 1167 */           dropdownActions("actionId", (short)actionId) + "label{text=\" on target \"};label{" + curColour + "text=\"" + tgtString + "\"};}");
/*      */ 
/*      */ 
/*      */       
/* 1171 */       buf.append("harray{checkbox{text=\"\";id=\"useCurrentTarget\";selected=\"" + useCurrentTarget + "\"}label{text=\"Use current: \"};label{" + "color=\"140,140,255\";" + "text=\"" + currentString + "\"};label{text=\" Current Id: \"};input{id=\"targetid\";text=\"" + this.currentTargetId + "\";}}");
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1181 */       buf.append("harray{label{text=\"Action performed\"};" + 
/*      */           
/* 1183 */           dropdownActions("actionId", (short)actionId) + "label{text=\" on target \"};label{" + curColour + "text=\"" + tgtString + "\"};}");
/*      */ 
/*      */ 
/*      */       
/* 1187 */       buf.append("harray{checkbox{text=\"\";id=\"useCurrentTarget\";selected=\"" + useCurrentTarget + "\"}label{text=\"Use current: \"};label{" + "color=\"140,140,255\";" + "text=\"" + currentString + "\"};passthrough{id=\"targetid\";text=\"" + trgTarget + "\"}}");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1194 */     buf.append("text{text=\"\"}");
/* 1195 */     buf.append("header{text=\"General\"}");
/* 1196 */     buf.append("harray{" + (
/* 1197 */         (getResponder().getPower() > 0) ? ("label{text=\"Target is Spawn Point \"};checkbox{id=\"spawnpoint\";selected=\"" + spawnpoint + "\"};") : "") + "label{text=\"Seconds to trigger \"};input{id=\"seconds\";text=\"" + trigsecs + "\";maxchars=\"2\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1205 */     buf.append("harray{label{text=\"Trigger Mission \"};" + 
/*      */         
/* 1207 */         dropdownMissions("missionRequired", missionRequired, true) + "}");
/*      */ 
/*      */     
/* 1210 */     buf.append("harray{label{text=\"Triggered state from \"};input{id=\"stateFrom\";maxchars=\"5\";text=\"" + stateFromString + "\"};label{text=\" to \"};input{id=\"stateTo\";maxchars=\"5\";text=\"" + stateToString + "\"};label{" + "color=\"140,140,255\";" + "text=\"  See Note 4.\";hover=\"The state 'to' is only valid if greater than the 'from'. Leave 'to' as '0.0' if not required.\"}label{text=\"  % Use '.' for decimals.\"}};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1218 */     buf.append("text{text=\"\"}");
/*      */     
/* 1220 */     if (this.level == 4) {
/*      */       
/* 1222 */       buf.append(appendChargeInfo());
/* 1223 */       buf.append("harray{button{text=\"Create Trigger\";id=\"createTrigger\"};}");
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1229 */       buf.append("harray{button{text=\"Update Trigger\";id=\"updateTrigger\"};label{text=\"  \"};button{text=\"Delete Trigger\";id=\"deleteTrigger\"hover=\"This will delete " + name + "\";confirm=\"You are about to delete " + name + ".\";question=\"Do you really want to do that?\"};label{text=\"  \"};button{id=\"cloneTrigger\";text=\"Clone Trigger\"hover=\"This will show a copy of this trigger for creation\";};}");
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
/* 1244 */     buf.append("text{type=\"bold\";text=\"Notes:\"}");
/* 1245 */     buf.append("text{type=\"italic\";text=\"1. Creating a trigger on a tile with the action 'Step On' will disregard the item used.\"}");
/* 1246 */     buf.append("text{type=\"italic\";text=\"2. Using the 'Create' action will only trigger when the selected item is manufactured or finished by polishing.\"}");
/* 1247 */     buf.append("text{type=\"italic\";text=\"3. Be careful with restartable and second chance missions. They can really mess things up.\"}");
/* 1248 */     buf.append("text{type=\"italic\";text=\"4. The state 'to' is only valid if greater than the 'from'. Leave 'to' as '0.0' if not required.\"}");
/* 1249 */     if (trg != null) {
/*      */ 
/*      */       
/* 1252 */       buf.append("text{type=\"italic\";text=\"5. A trigger effect that triggers on Not Started (0) will start a new mission and set its state to 1.\"}");
/* 1253 */       buf.append("text{type=\"italic\";text=\"6. A trigger effect may increase the state of a mission (does not have to be the mission that this trigger is attached to).\"}");
/* 1254 */       buf.append("text{type=\"italic\";text=\"7. If the state of a mission is higher than 0 it is considered started.\"}");
/* 1255 */       buf.append("text{text=\"    If it is 100.0% the mission is finished. If the state is set to -1.0 it has failed.\"}");
/*      */ 
/*      */       
/* 1258 */       buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/* 1259 */       buf.append("harray{label{type=\"bold\";text=\"Effects:     \"}button{id=\"createEffect\";text=\"Create New Effect\"};label{text=\"  All Effects\"}" + 
/*      */ 
/*      */ 
/*      */           
/* 1263 */           dropdownUnlinkedEffects("linkEffects", 0) + "label{text=\" \"}button{id=\"linkEffect\";text=\"Link Effect\"};}");
/*      */ 
/*      */ 
/*      */       
/* 1267 */       MissionTrigger[] trigs = { trg };
/* 1268 */       TriggerEffect[] teffs = TriggerEffects.getFilteredEffects(trigs, getResponder(), this.showE, this.incEInactive, this.dontListMine, this.listMineOnly, this.listForUser, false);
/*      */       
/* 1270 */       if (teffs.length > 0) {
/*      */         
/* 1272 */         Arrays.sort((Object[])teffs);
/* 1273 */         buf.append("table{rows=\"1\";cols=\"7\";");
/* 1274 */         buf.append("label{text=\"\"};label{text=\"Id\"};label{text=\"Name\"};label{text=\"+State\"};label{text=\"Type\"};label{text=\"\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1282 */         for (TriggerEffect lEff : teffs) {
/*      */           
/* 1284 */           String colour = "color=\"127,255,127\"";
/* 1285 */           String hover = ";hover=\"Active\"";
/* 1286 */           if (lEff.isInactive()) {
/*      */             
/* 1288 */             colour = "color=\"255,127,127\"";
/* 1289 */             hover = ";hover=\"Inactive\"";
/*      */           }
/* 1291 */           else if (lEff.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */             
/* 1293 */             colour = "color=\"140,140,255\";";
/* 1294 */             hover = ";hover=\"Current Target\"";
/*      */           } 
/* 1296 */           String clrtxt = "label{text=\"         \";hover=\"no text to clear\"}";
/* 1297 */           if (!lEff.getTextDisplayed().isEmpty())
/*      */           {
/*      */             
/* 1300 */             clrtxt = "harray{label{text=\" \"};button{id=\"clrE" + lEff.getId() + "\";text=\"Clear Text\"};}";
/*      */           }
/* 1302 */           buf.append("harray{button{id=\"edtE" + lEff
/* 1303 */               .getId() + "\";text=\"Edit\"};label{text=\" \"};};label{" + colour + "text=\"" + lEff
/*      */ 
/*      */               
/* 1306 */               .getId() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 1307 */               .getName() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 1308 */               .getMissionStateChange() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 1309 */               .getType() + " \"" + hover + "};" + clrtxt + "harray{label{text=\" \"};button{id=\"unlE" + lEff
/*      */ 
/*      */ 
/*      */               
/* 1313 */               .getId() + "\";text=\"Unlink\"};};");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1321 */         buf.append("}");
/*      */       }
/*      */       else {
/*      */         
/* 1325 */         buf.append("label{text=\"No trigger effects found\"}");
/*      */       } 
/*      */     } 
/* 1328 */     buf.append("}};null;null;}");
/* 1329 */     if (trg == null) {
/* 1330 */       getResponder().getCommunicator().sendBml(600, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */     } else {
/* 1332 */       getResponder().getCommunicator().sendBml(630, 600, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */     } 
/*      */   }
/*      */   
/*      */   private long indexExistingItem(String key, long existingItem) {
/* 1337 */     long ans = 0L;
/* 1338 */     String sIndex = getStringProp(key);
/* 1339 */     if (sIndex != null && !sIndex.isEmpty())
/*      */     {
/* 1341 */       if (getResponder().getPower() > 0) {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1346 */           long newexistingItem = Long.parseLong(sIndex);
/* 1347 */           if (newexistingItem != existingItem)
/*      */           {
/*      */             
/*      */             try {
/* 1351 */               Item old = Items.getItem(existingItem);
/* 1352 */               getResponder().getInventory().insertItem(old);
/* 1353 */               getResponder().getCommunicator().sendNormalServerMessage("Your old item was returned.");
/*      */             }
/* 1355 */             catch (NoSuchItemException nsi) {
/*      */               
/* 1357 */               getResponder().getCommunicator().sendNormalServerMessage("The previous reward item could not be located.");
/*      */             } 
/*      */             
/* 1360 */             ans = newexistingItem;
/*      */           }
/*      */         
/* 1363 */         } catch (NumberFormatException nfe) {
/*      */           
/* 1365 */           if (this.errorText.isEmpty()) {
/* 1366 */             this.errorText = "Failed to parse value for existingItem.";
/*      */           }
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1374 */           int index = Integer.parseInt(sIndex);
/* 1375 */           if (index > 0) {
/* 1376 */             ans = ((Item)this.ritems.get(index - 1)).getWurmId();
/* 1377 */           } else if (existingItem > 0L) {
/*      */             
/* 1379 */             Items.destroyItem(existingItem);
/* 1380 */             getResponder().getCommunicator().sendNormalServerMessage("The old reward item was lost.");
/* 1381 */             ans = 0L;
/*      */           }
/*      */         
/* 1384 */         } catch (NumberFormatException nfe) {
/*      */           
/* 1386 */           if (this.errorText.isEmpty())
/* 1387 */             this.errorText = "Failed to parse value for existingItem."; 
/*      */         } 
/*      */       } 
/*      */     }
/* 1391 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexItemTemplate(String key, String type) {
/* 1396 */     int ans = 0;
/* 1397 */     String sIndex = getStringProp(key);
/* 1398 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1402 */         int index = Integer.parseInt(sIndex);
/* 1403 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1407 */             ans = ((ItemTemplate)this.itemplates.get(index - 1)).getTemplateId();
/*      */           }
/* 1409 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1412 */             if (this.errorText.isEmpty()) {
/* 1413 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1417 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1420 */         if (this.errorText.isEmpty()) {
/* 1421 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1424 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexLayer(String key, String type) {
/* 1429 */     if (indexBoolean(key, type))
/* 1430 */       return 0; 
/* 1431 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean indexBoolean(String key, String type) {
/* 1436 */     String sIndex = getStringProp(key);
/* 1437 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 1442 */         int index = Integer.parseInt(sIndex);
/* 1443 */         return (index == 0);
/*      */       }
/* 1445 */       catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1448 */         if (this.errorText.isEmpty()) {
/* 1449 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1452 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexSpecialEffect(String key, String type, boolean doChecks) {
/* 1457 */     int ans = 0;
/* 1458 */     String sIndex = getStringProp(key);
/* 1459 */     if (sIndex != null && !sIndex.isEmpty()) {
/*      */ 
/*      */       
/*      */       try {
/* 1463 */         int index = Integer.parseInt(sIndex);
/*      */         
/*      */         try {
/* 1466 */           ans = ((SpecialEffects)this.effectsAvail.get(index)).getId();
/*      */         }
/* 1468 */         catch (Exception ex) {
/*      */ 
/*      */           
/* 1471 */           if (this.errorText.isEmpty()) {
/* 1472 */             this.errorText = "Unknown " + type + "?";
/*      */           }
/*      */         } 
/* 1475 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1485 */         if (this.errorText.isEmpty())
/* 1486 */           this.errorText = "Failed to parse value for " + type + "."; 
/*      */       } 
/* 1488 */       if (doChecks && ans != 0) {
/*      */         
/* 1490 */         if (ans == 1)
/*      */         {
/* 1492 */           if (!maySetToOpen(getResponder(), this.currentTargetId)) {
/*      */             
/* 1494 */             ans = 0;
/* 1495 */             if (this.errorText.isEmpty())
/* 1496 */               this.errorText = "You may not set that to open. Try a fence gate you can open."; 
/*      */           } 
/*      */         }
/* 1499 */         if (ans != 0) {
/*      */           
/* 1501 */           SpecialEffects sp = SpecialEffects.getEffect(ans);
/* 1502 */           if (sp.getPowerRequired() > getResponder().getPower()) {
/*      */             
/* 1504 */             ans = 0;
/* 1505 */             if (this.errorText.isEmpty())
/* 1506 */               this.errorText = "Invalid effect. Set to no effect."; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1511 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexSkillNum(String key, String type) {
/* 1516 */     int ans = 0;
/* 1517 */     String sIndex = getStringProp(key);
/* 1518 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1522 */         int index = Integer.parseInt(sIndex);
/* 1523 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1527 */             ans = ((SkillTemplate)this.stemplates.get(index - 1)).getNumber();
/*      */           }
/* 1529 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1532 */             if (this.errorText.isEmpty()) {
/* 1533 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1537 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1540 */         if (this.errorText.isEmpty()) {
/* 1541 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1544 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private float indexSkillVal(String key, String type) {
/* 1549 */     float ans = 0.0F;
/* 1550 */     String sIndex = getStringProp(key);
/* 1551 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1555 */         int index = Integer.parseInt(sIndex);
/* 1556 */         if (index != 0)
/*      */         {
/* 1558 */           if (index == 1)
/* 1559 */             return 0.001F; 
/* 1560 */           if (getResponder().getPower() > 0) {
/*      */             
/* 1562 */             switch (index) {
/*      */               
/*      */               case 2:
/* 1565 */                 return 0.002F;
/*      */               case 3:
/* 1567 */                 return 0.01F;
/*      */               case 4:
/* 1569 */                 return 0.05F;
/*      */               case 5:
/* 1571 */                 return 0.1F;
/*      */               case 6:
/* 1573 */                 return 1.0F;
/*      */               case 7:
/* 1575 */                 return 10.0F;
/*      */               case 8:
/* 1577 */                 return 20.0F;
/*      */             } 
/* 1579 */             return 0.001F;
/*      */           } 
/*      */ 
/*      */           
/* 1583 */           return 0.001F;
/*      */         }
/*      */       
/* 1586 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1589 */         if (this.errorText.isEmpty()) {
/* 1590 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1593 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private float indexStateChange(String key, int specialEffect) {
/* 1598 */     float val = getFloatProp(key, -101.0F, 100.0F, true);
/* 1599 */     if (val != 0.0F && specialEffect == 1) {
/*      */       
/* 1601 */       val = 0.0F;
/* 1602 */       if (this.errorText.isEmpty())
/* 1603 */         this.errorText = "State change can only be 0 for effect OPEN DOOR because it triggers several times and blocks half through passing the door."; 
/*      */     } 
/* 1605 */     return val;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexTileType(String key) {
/* 1610 */     int index = getIntProp("newTileType", 0, 8, false);
/* 1611 */     switch (index) {
/*      */       
/*      */       case 0:
/* 1614 */         return 0;
/*      */       case 1:
/* 1616 */         return Tiles.Tile.TILE_TREE.id;
/*      */       case 2:
/* 1618 */         return Tiles.Tile.TILE_DIRT.id;
/*      */       case 3:
/* 1620 */         return Tiles.Tile.TILE_GRASS.id;
/*      */       case 4:
/* 1622 */         return Tiles.Tile.TILE_FIELD.id;
/*      */       case 5:
/* 1624 */         return Tiles.Tile.TILE_SAND.id;
/*      */       case 6:
/* 1626 */         return Tiles.Tile.TILE_MYCELIUM.id;
/*      */       case 7:
/* 1628 */         return Tiles.Tile.TILE_CAVE_WALL.id;
/*      */       case 8:
/* 1630 */         return Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id;
/*      */     } 
/* 1632 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexTrigger(String key, String type) {
/* 1637 */     int ans = 0;
/* 1638 */     String sIndex = getStringProp(key);
/* 1639 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1643 */         int index = Integer.parseInt(sIndex);
/* 1644 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1648 */             ans = ((MissionTrigger)this.mtriggers.get(index - 1)).getId();
/*      */           }
/* 1650 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1653 */             if (this.errorText.isEmpty()) {
/* 1654 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1658 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1661 */         if (this.errorText.isEmpty()) {
/* 1662 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1665 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexUnlinkedTrigger(String key, String type) {
/* 1670 */     int ans = 0;
/* 1671 */     String sIndex = getStringProp(key);
/* 1672 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1676 */         int index = Integer.parseInt(sIndex);
/* 1677 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1681 */             ans = ((MissionTrigger)this.utriggers.get(index - 1)).getId();
/*      */           }
/* 1683 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1686 */             if (this.errorText.isEmpty()) {
/* 1687 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1691 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1694 */         if (this.errorText.isEmpty()) {
/* 1695 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1698 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexEffect(String key, String type) {
/* 1703 */     int ans = 0;
/* 1704 */     String sIndex = getStringProp(key);
/* 1705 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1709 */         int index = Integer.parseInt(sIndex);
/* 1710 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1714 */             ans = ((TriggerEffect)this.teffects.get(index - 1)).getId();
/*      */           }
/* 1716 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1719 */             if (this.errorText.isEmpty()) {
/* 1720 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1724 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1727 */         if (this.errorText.isEmpty()) {
/* 1728 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1731 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexUnlinkedEffect(String key, String type) {
/* 1736 */     int ans = 0;
/* 1737 */     String sIndex = getStringProp(key);
/* 1738 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1742 */         int index = Integer.parseInt(sIndex);
/* 1743 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1747 */             ans = ((TriggerEffect)this.ueffects.get(index - 1)).getId();
/*      */           }
/* 1749 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1752 */             if (this.errorText.isEmpty()) {
/* 1753 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1757 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1760 */         if (this.errorText.isEmpty()) {
/* 1761 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1764 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexAchievementId(String key, String type) {
/* 1769 */     int ans = 0;
/* 1770 */     String sIndex = getStringProp(key);
/* 1771 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1775 */         int index = Integer.parseInt(sIndex);
/* 1776 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1780 */             ans = ((AchievementTemplate)this.myAchievements.get(index - 1)).getNumber();
/*      */           }
/* 1782 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1785 */             if (this.errorText.isEmpty()) {
/* 1786 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1790 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1793 */         if (this.errorText.isEmpty()) {
/* 1794 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1797 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexActionId(String key, String type) {
/* 1802 */     int ans = 0;
/* 1803 */     String sIndex = getStringProp(key);
/* 1804 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1808 */         int index = Integer.parseInt(sIndex);
/* 1809 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1813 */             ans = ((ActionEntry)this.actionEntries.get(index - 1)).getNumber();
/*      */           }
/* 1815 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1818 */             if (this.errorText.isEmpty()) {
/* 1819 */               this.errorText = "Unknown " + type + "?";
/*      */             }
/*      */           } 
/*      */         }
/* 1823 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1826 */         if (this.errorText.isEmpty()) {
/* 1827 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1830 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexMission(String key, String type) {
/* 1835 */     int ans = 0;
/* 1836 */     String sIndex = getStringProp(key);
/* 1837 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1841 */         int index = Integer.parseInt(sIndex);
/* 1842 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1846 */             ans = ((Mission)this.missionsAvail.get(index - 1)).getId();
/*      */           }
/* 1848 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1851 */             if (this.errorText.isEmpty()) {
/* 1852 */               this.errorText = "Unknown mission?";
/*      */             }
/*      */           } 
/*      */         }
/* 1856 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1859 */         if (this.errorText.isEmpty()) {
/* 1860 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1863 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private int indexCreatureTemplate(String key, String type) {
/* 1868 */     int ans = 0;
/* 1869 */     String sIndex = getStringProp(key);
/* 1870 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1874 */         int index = Integer.parseInt(sIndex);
/* 1875 */         if (index != 0) {
/*      */           
/*      */           try {
/*      */             
/* 1879 */             ans = ((CreatureTemplate)this.creaturesAvail.get(index - 1)).getTemplateId();
/*      */           }
/* 1881 */           catch (Exception ex) {
/*      */ 
/*      */             
/* 1884 */             if (this.errorText.isEmpty()) {
/* 1885 */               this.errorText = "Unknown creature?";
/*      */             }
/*      */           } 
/*      */         }
/* 1889 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1892 */         if (this.errorText.isEmpty()) {
/* 1893 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1896 */     return ans;
/*      */   }
/*      */ 
/*      */   
/*      */   private byte indexCreatureType(String key, String type) {
/* 1901 */     byte ans = 0;
/* 1902 */     String sIndex = getStringProp(key);
/* 1903 */     if (sIndex != null && !sIndex.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 1907 */         int index = Byte.parseByte(sIndex);
/*      */         
/*      */         try {
/* 1910 */           ans = ((Byte)this.creaturesTypes.get(index)).byteValue();
/*      */         }
/* 1912 */         catch (Exception ex) {
/*      */ 
/*      */           
/* 1915 */           if (this.errorText.isEmpty()) {
/* 1916 */             this.errorText = "Unknown " + type + "?";
/*      */           }
/*      */         } 
/* 1919 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/* 1922 */         if (this.errorText.isEmpty()) {
/* 1923 */           this.errorText = "Failed to parse value for " + type + ".";
/*      */         }
/*      */       }  
/* 1926 */     return ans;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendManageEffect() {
/* 1932 */     String name = "";
/* 1933 */     boolean inactive = false;
/* 1934 */     String desc = "";
/*      */     
/* 1936 */     boolean startSkill = false;
/* 1937 */     boolean stopSkill = false;
/* 1938 */     boolean destroysInventory = false;
/*      */     
/* 1940 */     int itemTemplate = 0;
/* 1941 */     byte itemMaterial = 0;
/* 1942 */     boolean newbieItem = false;
/* 1943 */     String sql = "0";
/* 1944 */     String snumbers = "0";
/* 1945 */     String sbytevalue = "0";
/* 1946 */     String sexistingItem = "0";
/* 1947 */     String sexistingContainer = "0";
/* 1948 */     boolean destroysTarget = false;
/*      */     
/* 1950 */     int rewardSkillNum = 0;
/* 1951 */     String srewardSkillVal = "0";
/*      */     
/* 1953 */     String sModifyTileX = "0";
/* 1954 */     String sModifyTileY = "0";
/* 1955 */     int newTileType = 0;
/* 1956 */     String snewTileData = "0";
/*      */     
/* 1958 */     String sSpawnTileX = "0";
/* 1959 */     String sSpawnTileY = "0";
/* 1960 */     int creatureSpawn = 0;
/* 1961 */     String sCreatureAge = "0";
/* 1962 */     String creatureName = "";
/* 1963 */     byte creatureType = 0;
/*      */     
/* 1965 */     String sTeleportX = "0";
/* 1966 */     String sTeleportY = "0";
/* 1967 */     int teleportLayer = 0;
/* 1968 */     int specialEffect = 0;
/*      */     
/* 1970 */     int achievement = 0;
/*      */     
/* 1972 */     int missionAffected = 0;
/* 1973 */     String sStateChange = "0.0";
/*      */     
/* 1975 */     String soundName = "";
/*      */     
/* 1977 */     int missionToActivate = 0;
/* 1978 */     int missionToDeactivate = 0;
/* 1979 */     int triggerToActivate = 0;
/* 1980 */     int triggerToDeactivate = 0;
/* 1981 */     int effectToActivate = 0;
/* 1982 */     int effectToDeactivate = 0;
/*      */     
/* 1984 */     String sWinSizeX = "0";
/* 1985 */     String sWinSizeY = "0";
/* 1986 */     String topText = "";
/* 1987 */     String textDisplayed = "";
/*      */     
/* 1989 */     if (getAnswer() != null) {
/*      */ 
/*      */       
/* 1992 */       name = getStringProp("name");
/* 1993 */       inactive = getBooleanProp("inactive");
/* 1994 */       desc = getStringProp("desc");
/*      */       
/* 1996 */       startSkill = getBooleanProp("startSkill");
/* 1997 */       stopSkill = getBooleanProp("stopSkill");
/* 1998 */       destroysInventory = getBooleanProp("destroysInventory");
/*      */       
/* 2000 */       itemTemplate = indexItemTemplate("itemReward", "Reward Item");
/* 2001 */       itemMaterial = getByteProp("itemMaterial");
/* 2002 */       newbieItem = getBooleanProp("newbie");
/* 2003 */       sql = getStringProp("ql");
/* 2004 */       snumbers = getStringProp("numbers");
/* 2005 */       sbytevalue = getStringProp("bytevalue");
/* 2006 */       sexistingItem = getStringProp("existingItem");
/* 2007 */       sexistingContainer = getStringProp("rewardTargetContainerId");
/* 2008 */       destroysTarget = getBooleanProp("destroysTarget");
/*      */       
/* 2010 */       rewardSkillNum = indexSkillNum("rewardSkillNum", "Reward Skill Number");
/* 2011 */       srewardSkillVal = getStringProp("rewardSkillVal");
/*      */       
/* 2013 */       sModifyTileX = getStringProp("modifyTileX");
/* 2014 */       sModifyTileY = getStringProp("modifyTileY");
/* 2015 */       newTileType = getIntProp("newTileType");
/* 2016 */       snewTileData = getStringProp("newTileData");
/*      */       
/* 2018 */       sSpawnTileX = getStringProp("spawnTileX");
/* 2019 */       sSpawnTileY = getStringProp("spawnTileY");
/* 2020 */       creatureSpawn = indexCreatureTemplate("creatureSpawn", "Creature Spawn");
/* 2021 */       sCreatureAge = getStringProp("creatureAge");
/* 2022 */       creatureName = getStringProp("creatureName");
/* 2023 */       creatureType = indexCreatureType("creatureType", "Creature Type");
/*      */       
/* 2025 */       sTeleportX = getStringProp("teleportTileX");
/* 2026 */       sTeleportY = getStringProp("teleportTileY");
/* 2027 */       teleportLayer = indexLayer("teleportLayer", "Teleport layer");
/* 2028 */       specialEffect = indexSpecialEffect("specialEffect", "Special Effect", false);
/*      */       
/* 2030 */       achievement = getIntProp("achievement");
/*      */       
/* 2032 */       missionAffected = indexMission("missionId", "mission id");
/* 2033 */       sStateChange = getStringProp("missionStateChange");
/*      */       
/* 2035 */       soundName = getStringProp("sound");
/*      */       
/* 2037 */       missionToActivate = indexMission("missionToActivate", "mission to activate");
/* 2038 */       missionToDeactivate = indexMission("missionToDeactivate", "mission to deactivate");
/* 2039 */       triggerToActivate = indexTrigger("triggerToActivate", "trigger to activate");
/* 2040 */       triggerToDeactivate = indexTrigger("triggerToDeactivate", "trigger to deactivate");
/* 2041 */       effectToActivate = indexEffect("effectToActivate", "effect to activate");
/* 2042 */       effectToDeactivate = indexEffect("effectToDeactivate", "effect to deactivate");
/*      */       
/* 2044 */       sWinSizeX = getStringProp("winsizeX");
/* 2045 */       sWinSizeY = getStringProp("winsizeY");
/* 2046 */       topText = getStringProp("toptext");
/* 2047 */       textDisplayed = getStringProp("textdisplayed");
/*      */     }
/* 2049 */     else if (this.effectId > 0) {
/*      */       
/* 2051 */       TriggerEffect eff = TriggerEffects.getTriggerEffect(this.effectId);
/* 2052 */       if (eff != null) {
/*      */ 
/*      */ 
/*      */         
/* 2056 */         if (getResponder().getLogger() != null && getResponder().getPower() > 0 && eff != null && eff
/* 2057 */           .getName() != null) {
/*      */           
/* 2059 */           String buildLogString = getResponder() + ": Viewing trigger EFFECT settings for effect with name: " + eff.getName();
/* 2060 */           buildLogString = buildLogString + ((eff.getDescription() != null) ? (" and description = " + eff.getDescription()) : "");
/* 2061 */           getResponder().getLogger().info(buildLogString);
/*      */         } 
/*      */         
/* 2064 */         this.creatorType = eff.getCreatorType();
/* 2065 */         name = eff.getName();
/* 2066 */         inactive = eff.isInactive();
/* 2067 */         desc = eff.getDescription();
/*      */         
/* 2069 */         startSkill = eff.isStartSkillgain();
/* 2070 */         stopSkill = eff.isStopSkillgain();
/* 2071 */         destroysInventory = eff.destroysInventory();
/*      */         
/* 2073 */         itemTemplate = eff.getRewardItem();
/* 2074 */         itemMaterial = eff.getItemMaterial();
/* 2075 */         newbieItem = eff.isNewbieItem();
/* 2076 */         sql = Integer.toString(eff.getRewardQl());
/* 2077 */         snumbers = Integer.toString(eff.getRewardNumbers());
/* 2078 */         sbytevalue = Byte.toString(eff.getRewardByteValue());
/* 2079 */         sexistingItem = Long.toString(eff.getExistingItemReward());
/* 2080 */         sexistingContainer = Long.toString(eff.getRewardTargetContainerId());
/* 2081 */         destroysTarget = eff.destroysTarget();
/*      */         
/* 2083 */         rewardSkillNum = eff.getRewardSkillNum();
/* 2084 */         srewardSkillVal = Float.toString(eff.getRewardSkillModifier());
/*      */         
/* 2086 */         sModifyTileX = Integer.toString(eff.getModifyTileX());
/* 2087 */         sModifyTileY = Integer.toString(eff.getModifyTileY());
/* 2088 */         newTileType = eff.getNewTileType();
/* 2089 */         snewTileData = Byte.toString(eff.getNewTileData());
/*      */         
/* 2091 */         sSpawnTileX = Integer.toString(eff.getSpawnTileX());
/* 2092 */         sSpawnTileY = Integer.toString(eff.getSpawnTileY());
/* 2093 */         creatureSpawn = eff.getCreatureSpawn();
/* 2094 */         sCreatureAge = Integer.toString(eff.getCreatureAge());
/* 2095 */         creatureName = eff.getCreatureName();
/* 2096 */         creatureType = eff.getCreatureType();
/*      */         
/* 2098 */         sTeleportX = Integer.toString(eff.getTeleportX());
/* 2099 */         sTeleportY = Integer.toString(eff.getTeleportY());
/* 2100 */         teleportLayer = eff.getTeleportLayer();
/* 2101 */         specialEffect = eff.getSpecialEffectId();
/*      */         
/* 2103 */         achievement = eff.getAchievementId();
/*      */         
/* 2105 */         missionAffected = eff.getMissionId();
/* 2106 */         sStateChange = Float.toString(eff.getMissionStateChange());
/*      */         
/* 2108 */         soundName = eff.getSoundName();
/*      */         
/* 2110 */         missionToActivate = eff.getMissionToActivate();
/* 2111 */         missionToDeactivate = eff.getMissionToDeActivate();
/* 2112 */         triggerToActivate = eff.getTriggerToActivate();
/* 2113 */         triggerToDeactivate = eff.getTriggerToDeActivate();
/* 2114 */         effectToActivate = eff.getEffectToActivate();
/* 2115 */         effectToDeactivate = eff.getEffectToDeActivate();
/*      */         
/* 2117 */         sWinSizeX = Integer.toString(eff.getWindowSizeX());
/* 2118 */         sWinSizeY = Integer.toString(eff.getWindowSizeY());
/* 2119 */         topText = eff.getTopText();
/* 2120 */         textDisplayed = eff.getTextDisplayed();
/*      */       } else {
/*      */         
/* 2123 */         this.creatorType = 0;
/*      */       } 
/*      */     } else {
/*      */       
/* 2127 */       this.creatorType = 0;
/*      */     } 
/* 2129 */     StringBuilder buf = new StringBuilder();
/* 2130 */     buf.append("border{border{size=\"20,20\";null;null;varray{rescale=\"true\";harray{label{type='bold';text=\"" + this.question + "    \"};label{type=\"bolditalic\";" + "color=\"255,127,127\"" + ";text=\"" + this.errorText + "\"}}}harray{button{id=\"back\";text=\"Back\"};label{text=\"  \"}}null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2149 */         getId() + "\"}");
/*      */     
/* 2151 */     buf.append("harray{label{text=\"Name (max 40 chars)\"};input{id=\"name\";maxchars=\"40\";text=\"" + name + "\"};label{text=\" \"};checkbox{id=\"inactive\";selected=\"" + inactive + "\";hover=\"Not sure this is implemented currently\"};label{text=\"Inactive \";hover=\"Not sure this is implemented currently\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2158 */     buf.append("label{text=\"Description (max 400 chars)\"};");
/* 2159 */     buf.append("input{id=\"desc\";maxchars=\"400\";text=\"" + desc + "\";maxlines=\"4\"};");
/* 2160 */     if (getResponder().getPower() > 0) {
/*      */       
/* 2162 */       buf.append("text{text=\"\"}");
/* 2163 */       buf.append("header{text=\"Dangerous\"};");
/* 2164 */       buf.append("harray{checkbox{id=\"startSkill\";text=\"Resumes skill gain.  \";selected=\"" + startSkill + "\"};checkbox{id=\"stopSkill\";text=\"Stops skill gain.  \";selected=\"" + stopSkill + "\"};checkbox{id=\"destroysInventory\";text=\"Destroys all (!) carried items.\";selected=\"" + destroysInventory + "\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2169 */       buf.append("text{text=\"\"}");
/* 2170 */       buf.append("header{text=\"Reward\"};");
/* 2171 */       buf.append("label{type=\"bolditalic\";text=\"Item\"}");
/* 2172 */       buf.append("harray{label{text=\"New reward item\"};" + 
/*      */           
/* 2174 */           dropdownItemTemplates("itemReward", itemTemplate, true) + "label{text=\" Material \"}" + 
/*      */           
/* 2176 */           dropdownItemMaterials("itemMaterial", itemMaterial) + "label{text=\" \"}checkbox{id=\"newbieItem\";text=\"Tutorial/newbie item.\";selected=\"" + newbieItem + "\"};}");
/*      */ 
/*      */ 
/*      */       
/* 2180 */       buf.append("harray{label{text=\"Quality \"};input{id=\"ql\";text=\"" + sql + "\";maxchars=\"2\"};label{text=\" Numbers \"};input{id=\"numbers\";text=\"" + snumbers + "\";maxchars=\"2\"};label{text=\" Byte value \"};input{id=\"bytevalue\";text=\"" + sbytevalue + "\";maxchars=\"3\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2188 */       buf.append("harray{label{text=\"Existing item reward id \"};input{id=\"existingItem\";text=\"" + sexistingItem + "\"};label{text=\"Existing container id to insert into \"};input{id=\"rewardTargetContainerId\";text=\"" + sexistingContainer + "\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2194 */       String hoverDel = ";hover=\"If you select Destroy target, the target that triggers this effect will be destroyed:\"";
/* 2195 */       buf.append("harray{checkbox{id=\"destroysTarget\";selected=\"" + destroysTarget + "\"};label{text=\"Destroy target \"" + ";hover=\"If you select Destroy target, the target that triggers this effect will be destroyed:\"" + "};}");
/*      */ 
/*      */ 
/*      */       
/* 2199 */       buf.append("label{type=\"bolditalic\";text=\"Skill\"}");
/* 2200 */       buf.append("harray{label{text=\"New skill Reward\"};" + 
/*      */           
/* 2202 */           dropdownSkillTemplates("rewardSkillNum", rewardSkillNum) + "label{text=\"Skill Reward value\"};" + 
/*      */           
/* 2204 */           dropdownSkillValues("rewardSkillVal", srewardSkillVal) + "}");
/*      */       
/* 2206 */       buf.append("text{text=\"\"}");
/* 2207 */       buf.append("header{text=\"Tiles\"};");
/* 2208 */       buf.append("label{type=\"bolditalic\";text=\"Modify\"}");
/* 2209 */       buf.append("harray{label{text=\"Modify Tile X \"};input{id=\"modifyTileX\";text=\"" + sModifyTileX + "\";maxchars=\"4\"};label{text=\" Y \"};input{id=\"modifyTileY\";text=\"" + sModifyTileY + "\";maxchars=\"4\"};label{text=\" Type \"};" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2215 */           dropdownTileTypes("newTileType", newTileType) + "label{text=\" Data \"};input{id=\"newTileData\";text=\"" + snewTileData + "\";maxchars=\"3\"};}");
/*      */ 
/*      */ 
/*      */       
/* 2219 */       buf.append("label{type=\"bolditalic\";text=\"Spawn\"}");
/* 2220 */       buf.append("harray{label{text=\"Spawn Tile X \"};input{id=\"spawnTileX\";text=\"" + sSpawnTileX + "\";maxchars=\"4\"};label{text=\" Y \"};input{id=\"spawnTileY\";text=\"" + sSpawnTileY + "\";maxchars=\"4\"};label{text=\" Template \"};" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2226 */           dropdownCreatureTemplates("creatureSpawn", creatureSpawn) + "}");
/*      */       
/* 2228 */       buf.append("harray{label{text=\" Age \"};input{id=\"creatureAge\";text=\"" + sCreatureAge + "\";maxchars=\"2\"};label{text=\" Name (instead of template name) (max 20 chars) \"};input{id=\"creatureName\";text=\"" + creatureName + "\";maxchars=\"40\"};label{text=\" Type \"};" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2234 */           dropdownCreatureTypes("creatureType", creatureType) + "}");
/*      */       
/* 2236 */       buf.append("label{type=\"bolditalic\";text=\"Teleport / Special\"}");
/* 2237 */       buf.append("harray{label{text=\"Teleport/Special effect Tile X \"};input{id=\"teleportTileX\";text=\"" + sTeleportX + "\";maxchars=\"4\"};label{text=\" Y \"};input{id=\"teleportTileY\";text=\"" + sTeleportY + "\";maxchars=\"4\"};label{text=\"Surface\"};" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2243 */           dropdownBoolean("teleportLayer", (teleportLayer >= 0)) + "}");
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 2249 */       buf.append("text{text=\"\"}");
/* 2250 */       buf.append("header{text=\"Reward\"};");
/* 2251 */       buf.append("label{type=\"bolditalic\";text=\"Item\"}");
/*      */       
/*      */       try {
/* 2254 */         long itemReward = Long.parseLong(sexistingItem);
/* 2255 */         if (itemReward > 0L)
/*      */         {
/* 2257 */           Item i = Items.getItem(itemReward);
/*      */           
/* 2259 */           buf.append("label{text=\"Existing item reward: " + i.getName() + "\"};");
/* 2260 */           buf.append("passthrough{id=\"existingItem\";text=\"" + sexistingItem + "\"}");
/*      */         }
/*      */         else
/*      */         {
/* 2264 */           buf.append("harray{label{text=\"Your Item Reward (ql)\"};" + 
/*      */               
/* 2266 */               dropdownInventory("existingItem", itemReward) + "}");
/*      */         }
/*      */       
/*      */       }
/* 2270 */       catch (NoSuchItemException nsi) {
/*      */         
/* 2272 */         buf.append("harray{label{text=\"Existing item reward: \"};label{color=\"255,127,127\"text=\"Not Found!\"}}");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 2277 */       catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */ 
/*      */       
/* 2281 */       buf.append("text{text=\"\"}");
/* 2282 */       buf.append("header{text=\"Tiles\"};");
/*      */     } 
/* 2284 */     buf.append("harray{label{text=\"Special effect \"};" + 
/*      */         
/* 2286 */         dropdownSpecialEffects("specialEffect", specialEffect) + "}");
/*      */     
/* 2288 */     buf.append("label{type=\"bolditalic\";text=\"Achievement\"}");
/* 2289 */     buf.append("harray{label{text=\"Trigger Achievement \"};" + 
/*      */         
/* 2291 */         dropdownAchievements("achievement", achievement) + "}");
/*      */     
/* 2293 */     buf.append("text{text=\"\"}");
/* 2294 */     buf.append("header{text=\"General\"};");
/* 2295 */     buf.append("label{type=\"bolditalic\";text=\"Mission\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2301 */     String hoverState = ";hover=\"An effect may increase the state of any mission.\"";
/* 2302 */     buf.append("harray{label{text=\"Mission state affected \";hover=\"An effect may increase the state of any mission.\"};" + 
/*      */         
/* 2304 */         dropdownMissions("missionId", missionAffected, true) + "label{text=\"State change \"" + ";hover=\"An effect may increase the state of any mission.\"" + "};input{id=\"missionStateChange\";maxchars=\"5\";text=\"" + sStateChange + "\"};label{text=\"%. Use '.' for decimals.\"" + ";hover=\"An effect may increase the state of any mission.\"" + "}}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2309 */     buf.append("label{type=\"bolditalic\";text=\"Sound\"}");
/* 2310 */     buf.append("harray{label{text=\"Sound mapping (max 50 chars)\"};input{id=\"sound\";text=\"" + soundName + "\";maxchars=\"50\"};label{text=\" \"};button{id=\"playSound\";text=\"Play Sound\"}label{text=\" \"};button{id=\"listSounds\";text=\"Show Sound List\"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2318 */     buf.append("label{type=\"bolditalic\";text=\"Activate / Deactivate\"}");
/* 2319 */     buf.append("harray{label{text=\"Mission to activate \"};" + 
/*      */         
/* 2321 */         dropdownMissions("missionToActivate", missionToActivate, false) + "label{text=\"Mission to deactivate \"};" + 
/*      */         
/* 2323 */         dropdownMissions("missionToDeactivate", missionToDeactivate, false) + "}");
/*      */     
/* 2325 */     buf.append("harray{label{text=\"Trigger to activate \"};" + 
/*      */         
/* 2327 */         dropdownTriggers("triggerToActivate", triggerToActivate, true) + "label{text=\"Trigger to deactivate \"};" + 
/*      */         
/* 2329 */         dropdownTriggers("triggerToDeactivate", triggerToDeactivate, false) + "}");
/*      */     
/* 2331 */     buf.append("harray{label{text=\"Effect to activate \"};" + 
/*      */         
/* 2333 */         dropdownEffects("effectToActivate", effectToActivate, true) + "label{text=\"Effect to deactivate \"};" + 
/*      */         
/* 2335 */         dropdownEffects("effectToDeactivate", effectToDeactivate, false) + "}");
/*      */     
/* 2337 */     buf.append("text{text=\"\"}");
/* 2338 */     buf.append("header{text=\"Popup\"};");
/* 2339 */     buf.append("harray{label{text=\"Window Size Width \"};input{id=\"winsizeX\";text=\"" + sWinSizeX + "\";maxchars=\"3\"};label{text=\" Height \"};input{id=\"winsizeY\";text=\"" + sWinSizeY + "\";maxchars=\"3\"};label{text=\"  \"};button{id=\"testText\";text=\"Verify Popup\";hover=\"Effect will be redisplayed after verification.\"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2347 */     buf.append("label{text=\"Top Text displayed (max 1000 chars)\"}");
/* 2348 */     buf.append("input{id=\"toptext\";maxchars=\"1000\";maxlines=\"3\";text=\"" + topText + "\"}");
/*      */ 
/*      */ 
/*      */     
/* 2352 */     buf.append("label{text=\"Text displayed (Can be normal text or BML) (max 1000 chars)\"}");
/* 2353 */     buf.append("input{id=\"textdisplayed\";maxchars=\"1000\";maxlines=\"6\";text=\"" + textDisplayed + "\"}");
/*      */ 
/*      */ 
/*      */     
/* 2357 */     buf.append("text{text=\"\"}");
/* 2358 */     if (this.level == 8) {
/*      */       
/* 2360 */       buf.append(appendChargeInfo());
/* 2361 */       buf.append("harray{button{id=\"createEffect\";text=\"Create Effect\";hover=\"After creation, this will redisplay this effect\"}}");
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 2367 */       buf.append("harray{button{id=\"updateEffect\";text=\"Update Effect\"}label{text=\"  \"}button{id=\"deleteEffect\";text=\"Delete Effect\"hover=\"This will delete effect " + name + "\";confirm=\"You are about to delete effect " + name + ".\";question=\"Do you really want to do that?\"}label{text=\"  \"}button{id=\"cloneEffect\";text=\"Clone Effect\"hover=\"This will show a copy of this effect for creation\";}}");
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
/* 2381 */     if (this.level == 7) {
/*      */       
/* 2383 */       buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/* 2384 */       buf.append("harray{label{type=\"bold\";text=\"Triggers:     \"}button{id=\"createTrigger\";text=\"Create New Trigger\"};label{text=\"  Triggers\"}" + 
/*      */ 
/*      */ 
/*      */           
/* 2388 */           dropdownUnlinkedTriggers("trigs", 0) + "label{text=\" \"}button{id=\"linkTrigger\";text=\"Link Trigger\"};}");
/*      */ 
/*      */ 
/*      */       
/* 2392 */       MissionTrigger[] trigs = Triggers2Effects.getTriggersForEffect(this.effectId, this.incTInactive);
/* 2393 */       if (trigs.length > 0) {
/*      */         
/* 2395 */         Arrays.sort((Object[])trigs);
/* 2396 */         buf.append("table{rows=\"1\";cols=\"7\";");
/* 2397 */         buf.append("label{text=\"\"};label{text=\"Id\"};label{text=\"Name\"};label{text=\"State\"};label{text=\"Action\"};label{text=\"Target\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2405 */         for (MissionTrigger trigger : trigs) {
/*      */           
/* 2407 */           String colour = "color=\"127,255,127\"";
/* 2408 */           String hover = ";hover=\"Active\"";
/* 2409 */           if (trigger.isInactive()) {
/*      */             
/* 2411 */             colour = "color=\"255,127,127\"";
/* 2412 */             hover = ";hover=\"Inactive\"";
/*      */           }
/* 2414 */           else if (trigger.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */             
/* 2416 */             colour = "color=\"140,140,255\";";
/* 2417 */             hover = ";hover=\"Current Target\"";
/*      */           } 
/* 2419 */           buf.append("harray{button{id=\"edtT" + trigger
/* 2420 */               .getId() + "\";text=\"Edit\"};label{text=\"  \"}};label{" + colour + "text=\"" + trigger
/*      */ 
/*      */               
/* 2423 */               .getId() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 2424 */               .getName() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 2425 */               .getStateRange() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 2426 */               .getActionString() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 2427 */               .getTargetAsString(getResponder()) + "\"" + hover + "};harray{label{text=\"  \"}button{id=\"unlT" + trigger
/*      */ 
/*      */               
/* 2430 */               .getId() + "\";text=\"Unlink\"hover=\"This will unlink the trigger " + trigger
/* 2431 */               .getName() + " from this effect\";};};");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2438 */         buf.append("}");
/*      */       }
/*      */       else {
/*      */         
/* 2442 */         buf.append("label{text=\"No triggers found\"}");
/*      */       } 
/*      */     } 
/* 2445 */     buf.append("}};null;null;}");
/* 2446 */     getResponder().getCommunicator().sendBml(600, 600, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownTriggers(String id, int current, boolean reload) {
/* 2451 */     StringBuilder buf = new StringBuilder();
/* 2452 */     int def = 0;
/* 2453 */     int counter = 0;
/* 2454 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2455 */     buf.append("None");
/* 2456 */     if (reload) {
/*      */       
/* 2458 */       MissionTrigger[] triggers = MissionTriggers.getFilteredTriggers(getResponder(), current, this.creatorType, this.listForUser, this.dontListMine, this.listMineOnly);
/*      */ 
/*      */       
/* 2461 */       Arrays.sort((Object[])triggers);
/* 2462 */       this.mtriggers.clear();
/* 2463 */       for (MissionTrigger trig : triggers) {
/*      */         
/* 2465 */         this.mtriggers.add(trig);
/* 2466 */         counter++;
/* 2467 */         buf.append(",");
/* 2468 */         buf.append(trig.getName());
/* 2469 */         if (current > 0 && current == trig.getId()) {
/* 2470 */           def = counter;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 2475 */       for (MissionTrigger trig : this.mtriggers) {
/*      */         
/* 2477 */         counter++;
/* 2478 */         buf.append(",");
/* 2479 */         buf.append(trig.getName());
/* 2480 */         if (current > 0 && current == trig.getId())
/* 2481 */           def = counter; 
/*      */       } 
/*      */     } 
/* 2484 */     buf.append("\";default=\"" + def + "\"}");
/* 2485 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownUnlinkedTriggers(String id, int show) {
/* 2490 */     StringBuilder buf = new StringBuilder();
/* 2491 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2492 */     buf.append("None");
/* 2493 */     MissionTrigger[] utrigs = MissionTriggers.getFilteredTriggers(getResponder(), show, this.incTInactive, 0, 0);
/*      */ 
/*      */     
/* 2496 */     Arrays.sort((Object[])utrigs);
/* 2497 */     this.utriggers.clear();
/* 2498 */     for (MissionTrigger trig : utrigs) {
/*      */       
/* 2500 */       this.utriggers.add(trig);
/* 2501 */       buf.append(",");
/* 2502 */       buf.append(trig.getName());
/*      */     } 
/* 2504 */     buf.append("\";default=\"0\"}");
/* 2505 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownEffects(String id, int current, boolean reload) {
/* 2510 */     StringBuilder buf = new StringBuilder();
/* 2511 */     int def = 0;
/* 2512 */     int counter = 0;
/* 2513 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2514 */     buf.append("None");
/* 2515 */     if (reload) {
/*      */       
/* 2517 */       TriggerEffect[] effects = TriggerEffects.getFilteredEffects(getResponder(), 0, this.incEInactive, this.dontListMine, this.listMineOnly, this.listForUser);
/*      */ 
/*      */       
/* 2520 */       Arrays.sort((Object[])effects);
/* 2521 */       this.teffects.clear();
/* 2522 */       for (TriggerEffect eff : effects) {
/*      */         
/* 2524 */         this.teffects.add(eff);
/* 2525 */         counter++;
/* 2526 */         buf.append(",");
/* 2527 */         buf.append(eff.getName());
/* 2528 */         if (current > 0 && current == eff.getId()) {
/* 2529 */           def = counter;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 2534 */       for (TriggerEffect eff : this.teffects) {
/*      */         
/* 2536 */         counter++;
/* 2537 */         buf.append(",");
/* 2538 */         buf.append(eff.getName());
/* 2539 */         if (current > 0 && current == eff.getId())
/* 2540 */           def = counter; 
/*      */       } 
/*      */     } 
/* 2543 */     buf.append("\";default=\"" + def + "\"}");
/* 2544 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownUnlinkedEffects(String id, int show) {
/* 2549 */     StringBuilder buf = new StringBuilder();
/* 2550 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2551 */     buf.append("None");
/* 2552 */     TriggerEffect[] effects = TriggerEffects.getFilteredEffects(getResponder(), show, this.incEInactive, this.dontListMine, this.listMineOnly, this.listForUser);
/*      */ 
/*      */     
/* 2555 */     Arrays.sort((Object[])effects);
/* 2556 */     this.ueffects.clear();
/* 2557 */     for (TriggerEffect eff : effects) {
/*      */       
/* 2559 */       this.ueffects.add(eff);
/* 2560 */       buf.append(",");
/* 2561 */       buf.append(eff.getName());
/*      */     } 
/* 2563 */     buf.append("\";default=\"0\"}");
/* 2564 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownMissions(String id, int current, boolean reload) {
/* 2569 */     StringBuilder buf = new StringBuilder();
/* 2570 */     int counter = 0;
/* 2571 */     int def = 0;
/* 2572 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2573 */     buf.append("None");
/* 2574 */     if (reload) {
/*      */       
/* 2576 */       Mission[] missions = Missions.getFilteredMissions(getResponder(), this.includeM, this.incMInactive, this.dontListMine, this.listMineOnly, this.listForUser, this.groupName, this.onlyCurrent, this.currentTargetId);
/*      */       
/* 2578 */       this.missionsAvail.clear();
/* 2579 */       for (Mission lMission : missions) {
/*      */         
/* 2581 */         this.missionsAvail.add(lMission);
/* 2582 */         counter++;
/* 2583 */         buf.append(",");
/* 2584 */         buf.append(lMission.getName());
/* 2585 */         if (current > 0 && current == lMission.getId()) {
/* 2586 */           def = counter;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 2591 */       for (Mission lMission : this.missionsAvail) {
/*      */         
/* 2593 */         counter++;
/* 2594 */         buf.append(",");
/* 2595 */         buf.append(lMission.getName());
/* 2596 */         if (current > 0 && current == lMission.getId())
/* 2597 */           def = counter; 
/*      */       } 
/*      */     } 
/* 2600 */     buf.append("\";default=\"" + def + "\"}");
/* 2601 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownSkillTemplates(String id, int current) {
/* 2606 */     StringBuilder buf = new StringBuilder();
/* 2607 */     SkillTemplate[] stemps = SkillSystem.getAllSkillTemplates();
/*      */     
/* 2609 */     Arrays.sort((Object[])stemps);
/* 2610 */     int def = 0;
/* 2611 */     int counter = 0;
/* 2612 */     this.stemplates.clear();
/*      */     
/* 2614 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2615 */     buf.append("None");
/* 2616 */     for (SkillTemplate lStemp : stemps) {
/*      */       
/* 2618 */       if (lStemp.isMission()) {
/*      */         
/* 2620 */         counter++;
/* 2621 */         buf.append(",");
/* 2622 */         this.stemplates.add(lStemp);
/* 2623 */         buf.append(lStemp.getName());
/* 2624 */         if (current > 0 && current == lStemp.getNumber())
/*      */         {
/* 2626 */           def = counter;
/*      */         }
/*      */       } 
/*      */     } 
/* 2630 */     buf.append("\";default=\"" + def + "\"}");
/* 2631 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownSpecialEffects(String id, int current) {
/* 2636 */     StringBuilder buf = new StringBuilder();
/* 2637 */     int def = 0;
/* 2638 */     int counter = 0;
/* 2639 */     buf.append("dropdown{id=\"specialEffect\";options=\"");
/* 2640 */     this.effectsAvail.clear();
/* 2641 */     SpecialEffects[] effects = SpecialEffects.getEffects();
/*      */     
/* 2643 */     for (SpecialEffects lEffect : effects) {
/*      */       
/* 2645 */       if (getResponder().getPower() >= lEffect.getPowerRequired()) {
/*      */         
/* 2647 */         this.effectsAvail.add(lEffect);
/* 2648 */         if (counter > 0)
/* 2649 */           buf.append(","); 
/* 2650 */         buf.append(lEffect.getName());
/* 2651 */         if (current > 0 && current == lEffect.getId()) {
/* 2652 */           def = counter;
/* 2653 */         } else if (current == 0 && lEffect.getId() == 0) {
/* 2654 */           def = counter;
/* 2655 */         }  counter++;
/*      */       } 
/*      */     } 
/* 2658 */     buf.append("\";default=\"" + def + "\"}");
/* 2659 */     return buf.toString();
/*      */   }
/*      */   
/*      */   private String dropdownSkillValues(String id, String current) {
/* 2663 */     StringBuilder buf = new StringBuilder();
/* 2664 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2665 */     buf.append("0,");
/* 2666 */     buf.append(0.001F);
/* 2667 */     buf.append("%");
/* 2668 */     buf.append(", ");
/* 2669 */     buf.append(0.002F);
/* 2670 */     buf.append("%,");
/* 2671 */     buf.append(0.01F);
/* 2672 */     buf.append("%,");
/* 2673 */     buf.append(0.05F);
/* 2674 */     buf.append("%,");
/* 2675 */     buf.append(0.1F);
/* 2676 */     buf.append("%,");
/* 2677 */     buf.append(1.0F);
/* 2678 */     buf.append(", ");
/* 2679 */     buf.append(10.0F);
/* 2680 */     buf.append(", ");
/* 2681 */     buf.append(20.0F);
/*      */     
/* 2683 */     int def = 0;
/*      */     
/*      */     try {
/* 2686 */       float cv = Float.parseFloat(current);
/* 2687 */       if (cv == 0.001F) {
/* 2688 */         def = 1;
/* 2689 */       } else if (cv == 0.002F) {
/* 2690 */         def = 2;
/* 2691 */       } else if (cv == 0.01F) {
/* 2692 */         def = 3;
/* 2693 */       } else if (cv == 0.05F) {
/* 2694 */         def = 4;
/* 2695 */       } else if (cv == 0.1F) {
/* 2696 */         def = 5;
/* 2697 */       } else if (cv == 1.0F) {
/* 2698 */         def = 6;
/* 2699 */       } else if (cv == 10.0F) {
/* 2700 */         def = 7;
/* 2701 */       } else if (cv == 20.0F) {
/* 2702 */         def = 8;
/*      */       } 
/* 2704 */     } catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */ 
/*      */     
/* 2708 */     buf.append("\";default=\"" + def + "\"}");
/* 2709 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownItemMaterials(String id, byte current) {
/* 2714 */     StringBuilder buf = new StringBuilder();
/* 2715 */     int def = 0;
/* 2716 */     int counter = 0;
/* 2717 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2718 */     for (int x = 0; x < 96; x++) {
/*      */       
/* 2720 */       if (x == 0) {
/* 2721 */         buf.append("standard");
/*      */       } else {
/*      */         
/* 2724 */         buf.append(",");
/* 2725 */         buf.append(Item.getMaterialString((byte)x));
/*      */       } 
/* 2727 */       if (current > 0 && current == x)
/*      */       {
/* 2729 */         def = counter;
/*      */       }
/* 2731 */       counter++;
/*      */     } 
/* 2733 */     buf.append("\";default=\"" + def + "\"}");
/* 2734 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownCreatureTemplates(String id, int current) {
/* 2739 */     StringBuilder buf = new StringBuilder();
/* 2740 */     CreatureTemplate[] crets = CreatureTemplateFactory.getInstance().getTemplates();
/* 2741 */     int def = 0;
/* 2742 */     int counter = 0;
/* 2743 */     buf.append("dropdown{id='creatureSpawn';options=\"");
/* 2744 */     for (int x = 0; x < crets.length; x++) {
/*      */       
/* 2746 */       if (x == 0) {
/* 2747 */         buf.append("none");
/*      */       
/*      */       }
/* 2750 */       else if ((crets[x]).baseCombatRating < 15.0F) {
/*      */         
/* 2752 */         buf.append(",");
/* 2753 */         this.creaturesAvail.add(crets[x]);
/* 2754 */         buf.append(crets[x].getName());
/* 2755 */         counter++;
/*      */       } 
/*      */       
/* 2758 */       if (current > 0 && current == crets[x].getTemplateId())
/*      */       {
/* 2760 */         def = counter;
/*      */       }
/*      */     } 
/* 2763 */     buf.append("\";default=\"" + def + "\"}");
/* 2764 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownBoolean(String id, boolean current) {
/* 2769 */     StringBuilder buf = new StringBuilder();
/* 2770 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2771 */     buf.append("True,");
/* 2772 */     buf.append("False");
/* 2773 */     buf.append("\";default=\"" + (current ? 0 : 1) + "\"}");
/* 2774 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownCreatureTypes(String id, byte current) {
/* 2779 */     StringBuilder buf = new StringBuilder();
/* 2780 */     int def = 0;
/* 2781 */     this.creaturesTypes.clear();
/* 2782 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2783 */     buf.append("None,");
/* 2784 */     this.creaturesTypes.add(Byte.valueOf((byte)0));
/* 2785 */     buf.append("fierce ");
/* 2786 */     this.creaturesTypes.add(Byte.valueOf((byte)1));
/* 2787 */     buf.append(", ");
/* 2788 */     buf.append("angry ");
/* 2789 */     this.creaturesTypes.add(Byte.valueOf((byte)2));
/* 2790 */     buf.append(", ");
/* 2791 */     buf.append("raging ");
/* 2792 */     this.creaturesTypes.add(Byte.valueOf((byte)3));
/* 2793 */     buf.append(", ");
/* 2794 */     buf.append("slow ");
/* 2795 */     this.creaturesTypes.add(Byte.valueOf((byte)4));
/* 2796 */     buf.append(", ");
/* 2797 */     buf.append("alert ");
/* 2798 */     this.creaturesTypes.add(Byte.valueOf((byte)5));
/* 2799 */     buf.append(", ");
/* 2800 */     buf.append("greenish ");
/* 2801 */     this.creaturesTypes.add(Byte.valueOf((byte)6));
/* 2802 */     buf.append(", ");
/* 2803 */     buf.append("lurking ");
/* 2804 */     this.creaturesTypes.add(Byte.valueOf((byte)7));
/* 2805 */     buf.append(", ");
/* 2806 */     buf.append("sly ");
/* 2807 */     this.creaturesTypes.add(Byte.valueOf((byte)8));
/* 2808 */     buf.append(", ");
/* 2809 */     buf.append("hardened ");
/* 2810 */     this.creaturesTypes.add(Byte.valueOf((byte)9));
/* 2811 */     buf.append(", ");
/* 2812 */     buf.append("scared ");
/* 2813 */     this.creaturesTypes.add(Byte.valueOf((byte)10));
/* 2814 */     buf.append(", ");
/* 2815 */     buf.append("diseased ");
/* 2816 */     this.creaturesTypes.add(Byte.valueOf((byte)11));
/* 2817 */     buf.append(", ");
/* 2818 */     buf.append("champion ");
/* 2819 */     this.creaturesTypes.add(Byte.valueOf((byte)99));
/*      */     
/* 2821 */     def = current & 0xFF;
/* 2822 */     if (current == 99)
/* 2823 */       def = 12; 
/* 2824 */     buf.append("\";default=\"" + def + "\"}");
/* 2825 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownItemTemplates(String id, int current, boolean incSpecial) {
/* 2830 */     StringBuilder buf = new StringBuilder();
/* 2831 */     ItemTemplate[] templates = ItemTemplateFactory.getInstance().getTemplates();
/*      */     
/* 2833 */     Arrays.sort((Object[])templates);
/* 2834 */     int def = 0;
/* 2835 */     int counter = 0;
/* 2836 */     this.itemplates.clear();
/* 2837 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2838 */     buf.append("None");
/* 2839 */     for (ItemTemplate lTemplate : templates) {
/*      */       
/* 2841 */       if (lTemplate.isMissionItem() || (incSpecial && lTemplate.getTemplateId() == 791)) {
/*      */         
/* 2843 */         buf.append(",");
/* 2844 */         this.itemplates.add(lTemplate);
/* 2845 */         if (lTemplate.isMetal()) {
/* 2846 */           buf.append(lTemplate.sizeString + Item.getMaterialString(lTemplate.getMaterial()) + " " + lTemplate
/* 2847 */               .getName());
/* 2848 */         } else if (lTemplate.bowUnstringed) {
/* 2849 */           buf.append(lTemplate.sizeString + lTemplate.getName() + " [unstringed]");
/*      */         } else {
/* 2851 */           buf.append(lTemplate.sizeString + lTemplate.getName());
/* 2852 */         }  counter++;
/* 2853 */         if (current > 0 && current == lTemplate.getTemplateId())
/*      */         {
/* 2855 */           def = counter;
/*      */         }
/*      */       } 
/*      */     } 
/* 2859 */     buf.append("\";default=\"" + def + "\"}");
/* 2860 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownInventory(String id, long itemReward) {
/* 2865 */     StringBuilder buf = new StringBuilder();
/* 2866 */     int def = 0;
/* 2867 */     int counter = 0;
/* 2868 */     Item[] itemarr = getResponder().getInventory().getAllItems(false);
/* 2869 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2870 */     buf.append("None");
/* 2871 */     for (Item lElement : itemarr) {
/*      */       
/* 2873 */       if (!lElement.isNoDrop() && !lElement.isArtifact()) {
/*      */         
/* 2875 */         buf.append(",");
/* 2876 */         this.ritems.add(lElement);
/* 2877 */         buf.append(lElement.getName() + " - " + (int)lElement.getQualityLevel());
/*      */         
/* 2879 */         counter++;
/* 2880 */         if (itemReward > 0L && itemReward == lElement.getWurmId())
/*      */         {
/* 2882 */           def = counter;
/*      */         }
/*      */       } 
/*      */     } 
/* 2886 */     buf.append("\";default=\"" + def + "\"}");
/* 2887 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownAchievements(String id, int current) {
/* 2892 */     StringBuilder buf = new StringBuilder();
/* 2893 */     int def = 0;
/* 2894 */     int counter = 0;
/* 2895 */     this.myAchievements = Achievement.getSteelAchievements(getResponder());
/* 2896 */     if (getResponder().getPower() > 0)
/*      */     {
/* 2898 */       this.myAchievements.add(Achievement.getTemplate(141));
/*      */     }
/* 2900 */     buf.append("dropdown{id='achievement';options=\"");
/* 2901 */     buf.append("None");
/* 2902 */     for (AchievementTemplate template : this.myAchievements) {
/*      */       
/* 2904 */       counter++;
/* 2905 */       buf.append(",");
/* 2906 */       buf.append(template.getName() + " (" + template.getCreator() + ")");
/* 2907 */       if (current > 0 && template.getNumber() == current)
/* 2908 */         def = counter; 
/*      */     } 
/* 2910 */     buf.append("\";default=\"" + def + "\"}");
/* 2911 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownActions(String id, short current) {
/* 2916 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 2918 */     ActionEntry[] acts = (ActionEntry[])Actions.actionEntrys.clone();
/*      */     
/* 2920 */     Arrays.sort((Object[])acts);
/* 2921 */     int def = 0;
/* 2922 */     int counter = 0;
/* 2923 */     this.actionEntries.clear();
/* 2924 */     buf.append("dropdown{id=\"" + id + "\";options=\"");
/* 2925 */     buf.append("None");
/* 2926 */     for (ActionEntry lAct : acts) {
/*      */       
/* 2928 */       if (lAct.isMission()) {
/*      */         
/* 2930 */         counter++;
/* 2931 */         buf.append(",");
/* 2932 */         buf.append(lAct.getActionString());
/* 2933 */         this.actionEntries.add(lAct);
/* 2934 */         if (current > 0 && current == lAct.getNumber())
/* 2935 */           def = counter; 
/*      */       } 
/*      */     } 
/* 2938 */     buf.append("\";default=\"" + def + "\"}");
/* 2939 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String dropdownTileTypes(String id, int current) {
/* 2944 */     StringBuilder buf = new StringBuilder();
/* 2945 */     int def = 0;
/* 2946 */     buf.append("dropdown{id=\"newTileType\";options=\"");
/* 2947 */     buf.append("None,");
/* 2948 */     buf.append("Tree,");
/* 2949 */     buf.append("Dirt,");
/* 2950 */     buf.append("Grass,");
/* 2951 */     buf.append("Field,");
/* 2952 */     buf.append("Sand,");
/* 2953 */     buf.append("Mycelium,");
/* 2954 */     buf.append("Cave wall,");
/* 2955 */     buf.append("Iron ore");
/* 2956 */     if (current == Tiles.Tile.TILE_TREE.id) {
/* 2957 */       def = 1;
/* 2958 */     } else if (current == Tiles.Tile.TILE_DIRT.id) {
/* 2959 */       def = 2;
/* 2960 */     } else if (current == Tiles.Tile.TILE_GRASS.id) {
/* 2961 */       def = 3;
/* 2962 */     } else if (current == Tiles.Tile.TILE_FIELD.id) {
/* 2963 */       def = 4;
/* 2964 */     } else if (current == Tiles.Tile.TILE_SAND.id) {
/* 2965 */       def = 5;
/* 2966 */     } else if (current == Tiles.Tile.TILE_MYCELIUM.id) {
/* 2967 */       def = 6;
/* 2968 */     } else if (current == Tiles.Tile.TILE_CAVE_WALL.id) {
/* 2969 */       def = 7;
/* 2970 */     } else if (current == Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id) {
/* 2971 */       def = 8;
/* 2972 */     }  buf.append("\";default=\"" + def + "\"}");
/* 2973 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendManageMission() {
/* 2978 */     String name = "";
/* 2979 */     String group = this.groupName;
/* 2980 */     boolean inactive = false;
/* 2981 */     boolean hidden = false;
/* 2982 */     String intro = "";
/* 2983 */     boolean faildeath = false;
/* 2984 */     boolean secondChance = false;
/* 2985 */     boolean mayBeRestarted = false;
/* 2986 */     String sdays = "0";
/* 2987 */     String shours = "0";
/* 2988 */     String sminutes = "0";
/* 2989 */     String sseconds = "0";
/* 2990 */     int height = 400;
/*      */     
/* 2992 */     if (getAnswer() != null) {
/*      */ 
/*      */       
/* 2995 */       name = getStringProp("name");
/* 2996 */       group = getStringProp("groupName");
/* 2997 */       intro = getStringProp("intro");
/* 2998 */       inactive = getBooleanProp("inactive");
/* 2999 */       hidden = getBooleanProp("hidden");
/* 3000 */       faildeath = getBooleanProp("faildeath");
/* 3001 */       mayBeRestarted = getBooleanProp("mayBeRestarted");
/* 3002 */       secondChance = getBooleanProp("secondChance");
/*      */       
/* 3004 */       sdays = getStringProp("days");
/* 3005 */       shours = getStringProp("hours");
/* 3006 */       sminutes = getStringProp("minutes");
/* 3007 */       sseconds = getStringProp("seconds");
/*      */     }
/* 3009 */     else if (this.missionId > 0) {
/*      */       
/* 3011 */       Mission m = Missions.getMissionWithId(this.missionId);
/* 3012 */       if (m != null) {
/*      */ 
/*      */ 
/*      */         
/* 3016 */         if (getResponder().getLogger() != null && getResponder().getPower() > 0 && m
/* 3017 */           .getName() != null) {
/* 3018 */           getResponder().getLogger().info(getResponder() + ": Viewing mission settings for mission with name: " + m.getName());
/*      */         }
/* 3020 */         name = m.getName();
/* 3021 */         group = m.getGroupName();
/* 3022 */         inactive = m.isInactive();
/* 3023 */         hidden = m.isHidden();
/* 3024 */         intro = m.getInstruction();
/* 3025 */         faildeath = m.isFailOnDeath();
/* 3026 */         secondChance = m.hasSecondChance();
/* 3027 */         mayBeRestarted = m.mayBeRestarted();
/* 3028 */         if (m.getMaxTimeSeconds() > 0) {
/*      */           
/* 3030 */           int left = m.getMaxTimeSeconds();
/* 3031 */           int days = (int)(left / 86400L);
/* 3032 */           left = (int)(left - 86400L * days);
/* 3033 */           int hours = (int)(left / 3600L);
/* 3034 */           left = (int)(left - 3600L * hours);
/* 3035 */           int minutes = (int)(left / 60L);
/* 3036 */           left = (int)(left - 60L * minutes);
/* 3037 */           int seconds = left;
/*      */           
/* 3039 */           sdays = "" + days;
/* 3040 */           shours = "" + hours;
/* 3041 */           sminutes = "" + minutes;
/* 3042 */           sseconds = "" + seconds;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3047 */     StringBuilder buf = new StringBuilder();
/* 3048 */     buf.append("border{border{size=\"20,20\";null;null;varray{rescale=\"true\";harray{label{type='bold';text=\"" + this.question + "    \"};label{type=\"bolditalic\";" + "color=\"255,127,127\"" + ";text=\"" + this.errorText + "\"}}}harray{button{id=\"back\";text=\"Back\"};label{text=\"  \"}}null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3067 */         getId() + "\"}");
/*      */     
/* 3069 */     buf.append("harray{label{text=\"Name (max 100 chars)\"};input{id=\"name\";maxchars=\"100\";text=\"" + name + "\"};label{text=\"  Group (20 chars)\"};input{id=\"groupName\";maxchars=\"20\";text=\"" + group + "\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3075 */     buf.append("harray{checkbox{id=\"inactive\";selected=\"" + inactive + "\"};label{text=\"Inactive \"};checkbox{id=\"hidden\";selected=\"" + hidden + "\"};label{text=\"Hidden from players \"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3081 */     buf.append("label{text=\"Introduction (max 400 chars) - Note: This text will appear as a popup when mission starts.\"};");
/* 3082 */     buf.append("input{id=\"intro\";maxchars=\"400\"text=\"" + intro + "\";maxlines=\"4\"};");
/* 3083 */     buf.append("text{text=\"\"}");
/* 3084 */     buf.append("header{text=\"Fail\"};");
/* 3085 */     buf.append("harray{checkbox{id=\"faildeath\";selected=\"" + faildeath + "\"};label{text=\"Fail on death \"};}");
/*      */ 
/*      */ 
/*      */     
/* 3089 */     buf.append("text{text=\"A mission may fail after a certain time period. Set this limit here.\"}");
/* 3090 */     buf.append("harray{label{text=\"Max time: days\"};input{id=\"days\";text=\"" + sdays + "\";maxchars=\"3\"};label{text=\" hours\"};input{id=\"hours\";text=\"" + shours + "\";maxchars=\"2\"};label{text=\" minutes\"};input{id=\"minutes\";text=\"" + sminutes + "\";maxchars=\"2\"};label{text=\" seconds\"};input{id=\"seconds\";text=\"" + sseconds + "\";maxchars=\"2\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3100 */     buf.append("text{text=\"\"}");
/* 3101 */     buf.append("header{text=\"Restart\"};");
/* 3102 */     buf.append("harray{checkbox{id=\"secondChance\";selected=\"" + secondChance + "\";text=\"New chance if fail \"};label{text=\"  \"};checkbox{id=\"mayBeRestarted\";selected=\"" + mayBeRestarted + "\";text=\"May restart when finished \"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3107 */     buf.append("text{text=\"\"}");
/*      */     
/* 3109 */     if (this.missionId == 0) {
/*      */       
/* 3111 */       buf.append(appendChargeInfo());
/* 3112 */       buf.append("harray{button{id=\"createMission\";text=\"Create New Mission\"};}");
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 3118 */       height = 500;
/* 3119 */       buf.append("harray{button{id=\"updateMission\";text=\"Update Mission\"};label{text=\"  \"};button{id=\"deleteMission\";text=\"Delete Mission \"hover=\"This will delete " + name + "\";confirm=\"You are about to delete " + name + ".\";question=\"Do you really want to do that?\"};label{text=\"  \"};button{id=\"cloneMission\";text=\"Clone Mission\"hover=\"This will show a copy of this mission for creation\";};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3133 */       buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/* 3134 */       buf.append("harray{label{type=\"bold\";text=\"Triggers:     \"}button{id=\"createTrigger\";text=\"Create New Trigger\"};label{text=\"  Unlinked Triggers\"}" + 
/*      */ 
/*      */ 
/*      */           
/* 3138 */           dropdownUnlinkedTriggers("unTrigs", 2) + "label{text=\" \"}button{id=\"linkTrigger\";text=\"Link Trigger\"};}");
/*      */ 
/*      */ 
/*      */       
/* 3142 */       MissionTrigger[] trigs = MissionTriggers.getFilteredTriggers(getResponder(), 0, this.incTInactive, this.missionId, 0);
/*      */       
/* 3144 */       if (trigs.length > 0) {
/*      */         
/* 3146 */         Arrays.sort((Object[])trigs);
/* 3147 */         buf.append("table{rows=\"1\";cols=\"7\";");
/* 3148 */         buf.append("label{text=\"\"};label{text=\"Id\"};label{text=\"Name\"};label{text=\"State\"};label{text=\"Action\"};label{text=\"Target\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3156 */         for (MissionTrigger trigger : trigs) {
/*      */           
/* 3158 */           String colour = "color=\"127,255,127\"";
/* 3159 */           String hover = ";hover=\"Active\"";
/* 3160 */           if (trigger.isInactive()) {
/*      */             
/* 3162 */             colour = "color=\"255,127,127\"";
/* 3163 */             hover = ";hover=\"Inactive\"";
/*      */           }
/* 3165 */           else if (trigger.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */             
/* 3167 */             colour = "color=\"140,140,255\";";
/* 3168 */             hover = ";hover=\"Current Target\"";
/*      */           } 
/* 3170 */           buf.append("button{id=\"edtT" + trigger.getId() + "\";text=\"Edit\"};label{" + colour + "text=\"" + trigger
/* 3171 */               .getId() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 3172 */               .getName() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 3173 */               .getStateRange() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 3174 */               .getActionString() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 3175 */               .getTargetAsString(getResponder()) + "\"" + hover + "};harray{label{text=\"  \"}button{id=\"unlT" + trigger
/*      */ 
/*      */               
/* 3178 */               .getId() + "\";text=\"Unlink\"hover=\"This will unlink the trigger " + trigger
/* 3179 */               .getName() + " from this mission\";};};");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3186 */         buf.append("}");
/*      */       }
/*      */       else {
/*      */         
/* 3190 */         buf.append("label{text=\"No triggers found\"}");
/*      */       } 
/* 3192 */       buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/*      */       
/* 3194 */       buf.append("harray{label{type=\"bold\";text=\"Trigger effects:     \"}button{id=\"createEffect\";text=\"Create New Effect\"};}");
/*      */ 
/*      */ 
/*      */       
/* 3198 */       TriggerEffect[] teffs = TriggerEffects.getFilteredEffects(trigs, getResponder(), this.showE, this.incEInactive, this.dontListMine, this.listMineOnly, this.listForUser, false);
/*      */       
/* 3200 */       if (teffs.length > 0) {
/*      */         
/* 3202 */         Arrays.sort((Object[])teffs);
/* 3203 */         buf.append("table{rows=\"1\";cols=\"7\";");
/* 3204 */         buf.append("label{text=\"\"};label{text=\"Id\"};label{text=\"Name\"};label{text=\"+State\"};label{text=\"Type\"};label{text=\"\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3212 */         for (TriggerEffect lEff : teffs) {
/*      */           
/* 3214 */           String colour = "color=\"127,255,127\"";
/* 3215 */           String hover = ";hover=\"Active\"";
/* 3216 */           if (lEff.isInactive()) {
/*      */             
/* 3218 */             colour = "color=\"255,127,127\"";
/* 3219 */             hover = ";hover=\"Inactive\"";
/*      */           }
/* 3221 */           else if (lEff.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */             
/* 3223 */             colour = "color=\"140,140,255\";";
/* 3224 */             hover = ";hover=\"Current Target\"";
/*      */           } 
/* 3226 */           String clrtxt = "label{type=\"italic\";text=\"   no text\";hover=\"no text to clear\"}";
/* 3227 */           if (!lEff.getTextDisplayed().isEmpty())
/*      */           {
/*      */             
/* 3230 */             clrtxt = "harray{label{text=\" \"};button{id=\"clrE" + lEff.getId() + "\";text=\"Clear Text\"};}";
/*      */           }
/* 3232 */           buf.append("button{id=\"edtE" + lEff.getId() + "\";text=\"Edit\"};label{" + colour + "text=\"" + lEff
/* 3233 */               .getId() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 3234 */               .getName() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 3235 */               .getMissionStateChange() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 3236 */               .getType() + " \"" + hover + "};" + clrtxt + "harray{label{text=\" \"}};");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3248 */         buf.append("}");
/*      */       }
/*      */       else {
/*      */         
/* 3252 */         buf.append("label{text=\"No trigger effects found\"}");
/*      */       } 
/* 3254 */       buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/*      */       
/* 3256 */       buf.append("label{type=\"bold\";text=\"Other Mission effects that change this missions state:\"}");
/* 3257 */       buf.append("harray{label{text=\"  Unlinked Effects\"}" + 
/*      */           
/* 3259 */           dropdownUnlinkedEffects("unEffs", 2) + "label{text=\" \"}button{id=\"linkEffect\";text=\"Link Effect\"};}");
/*      */ 
/*      */ 
/*      */       
/* 3263 */       TriggerEffect[] meffs = TriggerEffects.getFilteredEffects(trigs, getResponder(), this.showE, this.incEInactive, this.dontListMine, this.listMineOnly, this.listForUser, this.missionId);
/*      */       
/* 3265 */       if (meffs.length > 0) {
/*      */         
/* 3267 */         Arrays.sort((Object[])meffs);
/* 3268 */         buf.append("table{rows=\"1\";cols=\"7\";");
/* 3269 */         buf.append("label{text=\"\"};label{text=\"Id\"};label{text=\"Name\"};label{text=\"+State\"};label{text=\"Type\"};label{text=\"\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3277 */         for (TriggerEffect lEff : meffs) {
/*      */           
/* 3279 */           String colour = "color=\"127,255,127\"";
/* 3280 */           String hover = ";hover=\"Active\"";
/* 3281 */           if (lEff.isInactive()) {
/*      */             
/* 3283 */             colour = "color=\"255,127,127\"";
/* 3284 */             hover = ";hover=\"Inactive\"";
/*      */           }
/* 3286 */           else if (lEff.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */             
/* 3288 */             colour = "color=\"140,140,255\";";
/* 3289 */             hover = ";hover=\"Current Target\"";
/*      */           } 
/* 3291 */           String clrtxt = "label{type=\"italic\";text=\"   no text\";hover=\"no text to clear\"}";
/* 3292 */           if (!lEff.getTextDisplayed().isEmpty())
/*      */           {
/*      */             
/* 3295 */             clrtxt = "harray{label{text=\" \"};button{id=\"clrE" + lEff.getId() + "\";text=\"Clear Text\"};}";
/*      */           }
/* 3297 */           buf.append("button{id=\"edtE" + lEff.getId() + "\";text=\"Edit\"};label{" + colour + "text=\"" + lEff
/* 3298 */               .getId() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 3299 */               .getName() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 3300 */               .getMissionStateChange() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 3301 */               .getType() + " \"" + hover + "};" + clrtxt + "harray{label{text=\" \"}button{id=\"unlE" + lEff
/*      */ 
/*      */ 
/*      */               
/* 3305 */               .getId() + "\";text=\"Unlink\"hover=\"This will unlink effect " + lEff
/* 3306 */               .getName() + " from this mission\";};};");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3313 */         buf.append("}");
/*      */       }
/*      */       else {
/*      */         
/* 3317 */         buf.append("label{text=\"No mission effects found\"}");
/*      */       } 
/*      */     } 
/* 3320 */     buf.append("}};null;null;}");
/*      */     
/* 3322 */     getResponder().getCommunicator().sendBml(500, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private String appendChargeInfo() {
/* 3327 */     StringBuilder buf = new StringBuilder();
/* 3328 */     if (getResponder().getPower() == 0) {
/*      */       
/* 3330 */       Item ruler = null;
/*      */       
/*      */       try {
/* 3333 */         ruler = Items.getItem(this.missionRulerId);
/* 3334 */         if (ruler.getOwnerId() != getResponder().getWurmId())
/*      */         {
/* 3336 */           buf.append("text{text=\"You are not the ruler of this mission ruler.\"}");
/*      */         }
/* 3338 */         else if (ruler.getAuxData() <= 0)
/*      */         {
/* 3340 */           buf.append("text{text=\"Your " + ruler.getName() + " contains no charges. You can not create new mission functionality.\"}");
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 3345 */           buf.append("text{text=\"Your " + ruler.getName() + " contains " + ruler.getAuxData() + " charges. Creating a new mission functionality uses up 1 charge.\"}");
/*      */         }
/*      */       
/*      */       }
/* 3349 */       catch (NoSuchItemException nsi) {
/*      */         
/* 3351 */         buf.append("text{text=\"The mission ruler is gone!\"}");
/*      */       } 
/* 3353 */       buf.append("text{text=\"\"}");
/*      */     } 
/* 3355 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestion() {
/* 3366 */     StringBuilder buf = new StringBuilder(getBmlHeaderNoQuestion());
/* 3367 */     buf.append("header{text=\"Missions\"}");
/*      */     
/* 3369 */     if (getResponder().getPower() > 0) {
/*      */       
/* 3371 */       buf.append("harray{label{text=\"Filter:\"};checkbox{id=\"listmine\";text=\"Only my missions  \"" + 
/*      */           
/* 3373 */           addSel(this.listMineOnly) + "};checkbox{id=\"nolistmine\";text=\"Not my missions  \"" + 
/* 3374 */           addSel(this.dontListMine) + "};checkbox{id=\"onlyCurrent\";text=\"Only current target missions  \"" + 
/* 3375 */           addSel(this.onlyCurrent) + "}}");
/*      */       
/* 3377 */       buf.append("harray{label{text=\"Show:\"};radio{group=\"includeM\";id=\"0\";text=\"All  \"" + 
/*      */           
/* 3379 */           addSel((this.includeM == 0)) + "};radio{group=\"includeM\";id=\"" + '\001' + "\";text=\"With triggers  \"" + 
/* 3380 */           addSel((this.includeM == 1)) + "};radio{group=\"includeM\";id=\"" + '\002' + "\";text=\"Without triggers  \"" + 
/* 3381 */           addSel((this.includeM == 2)) + "};label{text=\"     \"};checkbox{id=\"incMInactive\";text=\"Include inactive\"" + 
/*      */           
/* 3383 */           addSel(this.incMInactive) + "};}");
/*      */       
/* 3385 */       buf.append("harray{label{text=\"Type: \"};checkbox{id=\"typeSystem\";text=\"System  \"" + 
/*      */           
/* 3387 */           addSel(this.typeSystem) + "};checkbox{id=\"typeGM\";text=\"GM  \"" + 
/* 3388 */           addSel(this.typeGM) + "};checkbox{id=\"typePlayer\";text=\"Player \"" + 
/* 3389 */           addSel(this.typePlayer) + "};};");
/*      */       
/* 3391 */       buf.append("harray{label{text=\"Group: \"};input{id=\"groupName\";text=\"" + this.groupName + "\";maxchars=\"20\"}label{text=\" Player: \"};input{id=\"specialName\";text=\"" + this.userName + "\";maxchars=\"30\"}};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3397 */       buf.append("text{text=\"\"}");
/*      */     }
/*      */     else {
/*      */       
/* 3401 */       buf.append("harray{label{text=\"Group: \"};input{id=\"groupName\";text=\"" + this.groupName + "\";maxchars=\"20\"}};");
/*      */ 
/*      */ 
/*      */       
/* 3405 */       buf.append(appendChargeInfo());
/*      */     } 
/* 3407 */     buf.append("harray{button{id=\"listMissions\";text=\"List Missions\"}label{text=\"  \"};button{id=\"createMission\";text=\"Create New Mission \"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3413 */     buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/* 3414 */     buf.append("header{text=\"Triggers\"}");
/* 3415 */     buf.append("harray{label{text=\"Show:\"};radio{group=\"showT\";id=\"0\";text=\"All  \"" + 
/*      */         
/* 3417 */         addSel((this.showT == 0)) + "};radio{group=\"showT\";id=\"" + '\001' + "\";text=\"Linked to missions\"" + 
/* 3418 */         addSel((this.showT == 1)) + "}radio{group=\"showT\";id=\"" + '\002' + "\";text=\"Unlinked\"" + 
/* 3419 */         addSel((this.showT == 2)) + "}label{text=\"     \"};checkbox{id=\"incTInactive\";text=\"Include inactive  \"" + 
/*      */         
/* 3421 */         addSel(this.incTInactive) + "};}");
/*      */     
/* 3423 */     buf.append("harray{button{id=\"listTriggers\";text=\"List Triggers \"};label{text=\"  \"};button{id=\"createTrigger\";text=\"Create New Trigger  \"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3429 */     buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/* 3430 */     buf.append("header{text=\"Effects\"}");
/* 3431 */     buf.append("harray{label{text=\"Show:\"};radio{group=\"showE\";id=\"0\";text=\"All  \"" + 
/*      */         
/* 3433 */         addSel((this.showE == 0)) + "};radio{group=\"showE\";id=\"" + '\001' + "\";text=\"Linked to trigger\"" + 
/* 3434 */         addSel((this.showE == 1)) + "}radio{group=\"showE\";id=\"" + '\002' + "\";text=\"Unlinked\"" + 
/* 3435 */         addSel((this.showE == 2)) + "}label{text=\"     \"};checkbox{id=\"incEInactive\";text=\"Include inactive  \"" + 
/*      */         
/* 3437 */         addSel(this.incEInactive) + "};}");
/*      */     
/* 3439 */     buf.append("harray{button{id=\"listEffects\";text=\"List Effects\"}label{text=\"  \"};button{id=\"createEffect\";text=\"Create New Effect \"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3444 */     buf.append("label{type=\"bold\";text=\"- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\"}");
/* 3445 */     buf.append("header{text=\"Current Target\"}");
/* 3446 */     String tgt = MissionTriggers.getTargetAsString(getResponder(), this.currentTargetId);
/* 3447 */     buf.append("harray{label{color=\"140,140,255\";text=\"" + tgt + "\"}" + (
/*      */         
/* 3449 */         (getResponder().getPower() > 0) ? ("label{color=\"140,140,255\";text=\"(Id:" + this.currentTargetId + ")\"}") : "") + "}");
/*      */     
/* 3451 */     buf.append("}};null;null;}");
/* 3452 */     getResponder().getCommunicator().sendBml(450, 450, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean maySetToOpen(Creature resp, long targetId) {
/* 3457 */     if (resp.getPower() == 0) {
/*      */       
/* 3459 */       if (WurmId.getType(targetId) == 5) {
/*      */         
/* 3461 */         Wall w = Wall.getWall(targetId);
/* 3462 */         if (w != null) {
/*      */           
/*      */           try {
/*      */             
/* 3466 */             Structure s = Structures.getStructure(w.getStructureId());
/* 3467 */             for (Item key : resp.getKeys()) {
/*      */               
/* 3469 */               if (key.getWurmId() == s.getWritId()) {
/* 3470 */                 return true;
/*      */               }
/*      */             } 
/* 3473 */           } catch (NoSuchStructureException noSuchStructureException) {}
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3478 */         return false;
/*      */       } 
/* 3480 */       if (WurmId.getType(targetId) == 7) {
/*      */         
/* 3482 */         Fence f = Fence.getFence(targetId);
/* 3483 */         if (f.isDoor()) {
/*      */           
/* 3485 */           FenceGate fg = FenceGate.getFenceGate(targetId);
/* 3486 */           if (fg == null)
/* 3487 */             return false; 
/* 3488 */           if (fg.canBeOpenedBy(resp, false))
/* 3489 */             return true; 
/*      */         } 
/*      */       } 
/* 3492 */       return false;
/*      */     } 
/* 3494 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean parseEffect(Item ruler) {
/* 3499 */     boolean delete = getBooleanProp("deleteEffect");
/* 3500 */     TriggerEffect eff = TriggerEffects.getTriggerEffect(this.effectId);
/* 3501 */     if (eff == null) {
/*      */       
/* 3503 */       if (delete) {
/*      */         
/* 3505 */         getResponder().getCommunicator().sendNormalServerMessage("You tried to delete a non-existing trigger effect.");
/* 3506 */         return reshow();
/*      */       } 
/* 3508 */       eff = new TriggerEffect();
/* 3509 */       eff.setCreatorName(getResponder().getName());
/* 3510 */       eff.setLastModifierName(getResponder().getName());
/*      */     }
/* 3512 */     else if (delete) {
/*      */       
/* 3514 */       eff.destroy();
/* 3515 */       getResponder().getCommunicator().sendNormalServerMessage("You delete the trigger effect.");
/* 3516 */       return parseBack();
/*      */     } 
/* 3518 */     this.errorText = "";
/* 3519 */     String name = eff.getName();
/* 3520 */     String description = eff.getDescription();
/*      */     
/* 3522 */     boolean inActive = eff.isInactive();
/*      */     
/* 3524 */     boolean startSkill = eff.isStartSkillgain();
/* 3525 */     boolean stopSkill = eff.isStopSkillgain();
/* 3526 */     boolean destroysInventory = eff.destroysInventory();
/*      */     
/* 3528 */     int itemReward = eff.getRewardItem();
/* 3529 */     byte itemMaterial = eff.getItemMaterial();
/* 3530 */     boolean newbie = eff.isNewbieItem();
/* 3531 */     int ql = eff.getRewardQl();
/* 3532 */     int numbers = eff.getRewardNumbers();
/* 3533 */     byte bytevalue = eff.getRewardByteValue();
/* 3534 */     long existingItem = eff.getExistingItemReward();
/* 3535 */     long rewardTargetContainerId = eff.getRewardTargetContainerId();
/* 3536 */     boolean destroysTarget = eff.destroysTarget();
/*      */     
/* 3538 */     int rewardSkillNum = eff.getRewardSkillNum();
/* 3539 */     float rewardSkillVal = eff.getRewardSkillModifier();
/*      */     
/* 3541 */     int modifyTileX = eff.getModifyTileX();
/* 3542 */     int modifyTileY = eff.getModifyTileY();
/* 3543 */     int newTileType = eff.getNewTileType();
/* 3544 */     byte newTileData = eff.getNewTileData();
/*      */     
/* 3546 */     int spawnTileX = eff.getSpawnTileX();
/* 3547 */     int spawnTileY = eff.getSpawnTileY();
/* 3548 */     int creatureSpawn = eff.getCreatureSpawn();
/* 3549 */     int creatureAge = eff.getCreatureAge();
/* 3550 */     String creatureName = eff.getCreatureName();
/* 3551 */     byte creatureType = eff.getCreatorType();
/*      */     
/* 3553 */     int teleportX = eff.getTeleportX();
/* 3554 */     int teleportY = eff.getTeleportY();
/* 3555 */     int teleportLayer = eff.getTeleportLayer();
/*      */     
/* 3557 */     int specialEffect = eff.getSpecialEffectId();
/*      */     
/* 3559 */     int achievementId = eff.getAchievementId();
/*      */ 
/*      */     
/* 3562 */     int missionAffectedId = eff.getMissionId();
/*      */     
/* 3564 */     float missionStateChange = eff.getMissionStateChange();
/*      */     
/* 3566 */     int missionActivated = eff.getMissionToActivate();
/* 3567 */     int missionDeActivated = eff.getMissionToDeActivate();
/*      */     
/* 3569 */     int triggerActivated = eff.getTriggerToActivate();
/* 3570 */     int triggerDeActivated = eff.getTriggerToDeActivate();
/*      */     
/* 3572 */     int effectActivated = eff.getEffectToActivate();
/* 3573 */     int effectDeActivated = eff.getEffectToDeActivate();
/*      */     
/* 3575 */     String sound = eff.getSoundName();
/*      */     
/* 3577 */     int setWindowSizeX = eff.getWindowSizeX();
/* 3578 */     int setWindowSizeY = eff.getWindowSizeY();
/* 3579 */     String textdisplayed = eff.getTextDisplayed();
/* 3580 */     String top = eff.getTopText();
/*      */     
/* 3582 */     boolean cloneEffect = getBooleanProp("cloneEffect");
/* 3583 */     if (cloneEffect) {
/*      */ 
/*      */       
/* 3586 */       this.sbacks += "|" + '\007' + "," + this.missionId + "," + this.triggerId + "," + this.effectId;
/* 3587 */       return createNewEffect(getAnswer());
/*      */     } 
/* 3589 */     sound = getStringProp("sound");
/* 3590 */     boolean playSound = getBooleanProp("playSound");
/* 3591 */     if (playSound) {
/*      */ 
/*      */       
/* 3594 */       if (!sound.isEmpty()) {
/* 3595 */         SoundPlayer.playSound(sound, getResponder(), 1.5F);
/*      */       } else {
/* 3597 */         getResponder().getCommunicator().sendNormalServerMessage("No sound mapped", (byte)1);
/* 3598 */       }  return reshow();
/*      */     } 
/* 3600 */     boolean listSounds = getBooleanProp("listSounds");
/* 3601 */     if (listSounds) {
/*      */ 
/*      */       
/* 3604 */       SoundList sl = new SoundList(getResponder(), "SoundList", "Select the Sound to use");
/* 3605 */       sl.setSelected(sound);
/* 3606 */       sl.setRoot(this);
/* 3607 */       sl.sendQuestion();
/* 3608 */       return false;
/*      */     } 
/*      */     
/* 3611 */     setWindowSizeX = getIntProp("winsizeX");
/* 3612 */     setWindowSizeY = getIntProp("winsizeY");
/* 3613 */     top = getStringProp("toptext");
/* 3614 */     textdisplayed = getStringProp("textdisplayed");
/* 3615 */     boolean testText = getBooleanProp("testText");
/* 3616 */     if (testText) {
/*      */       
/* 3618 */       if (!top.isEmpty() && !textdisplayed.isEmpty()) {
/*      */         
/* 3620 */         MissionPopup pop = new MissionPopup(getResponder(), "Mission progress", "");
/* 3621 */         if (setWindowSizeX > 0 && setWindowSizeY > 0) {
/*      */           
/* 3623 */           pop.windowSizeX = setWindowSizeX;
/* 3624 */           pop.windowSizeY = setWindowSizeY;
/*      */         } 
/* 3626 */         pop.setToSend(textdisplayed);
/* 3627 */         pop.setTop(top);
/* 3628 */         pop.setRoot(this);
/* 3629 */         pop.sendQuestion();
/* 3630 */         return false;
/*      */       } 
/* 3632 */       return reshow();
/*      */     } 
/* 3634 */     boolean updateEffect = getBooleanProp("updateEffect");
/* 3635 */     boolean createEffect = getBooleanProp("createEffect");
/* 3636 */     if (updateEffect || createEffect) {
/*      */       
/* 3638 */       name = getStringProp("name", 3);
/* 3639 */       description = getStringProp("desc");
/* 3640 */       inActive = getBooleanProp("inactive");
/* 3641 */       if (getResponder().getPower() > 0) {
/*      */ 
/*      */         
/* 3644 */         startSkill = getBooleanProp("startSkill");
/* 3645 */         stopSkill = getBooleanProp("stopSkill");
/* 3646 */         destroysInventory = getBooleanProp("destroysInventory");
/*      */         
/* 3648 */         itemReward = indexItemTemplate("itemReward", "Item Reward");
/* 3649 */         itemMaterial = getByteProp("itemMaterial");
/* 3650 */         newbie = getBooleanProp("newbieItem");
/* 3651 */         ql = getIntProp("ql", 0, 100, true);
/* 3652 */         numbers = getIntProp("numbers");
/* 3653 */         bytevalue = getByteProp("bytevalue");
/* 3654 */         rewardTargetContainerId = readContainerId();
/* 3655 */         destroysTarget = getBooleanProp("destroysTarget");
/*      */         
/* 3657 */         rewardSkillNum = indexSkillNum("rewardSkillNum", "Reward Skill Number");
/* 3658 */         rewardSkillVal = indexSkillVal("rewardSkillVal", "Reward Skill Value");
/*      */         
/* 3660 */         modifyTileX = getIntProp("modifyTileX");
/* 3661 */         modifyTileY = getIntProp("modifyTileY");
/* 3662 */         newTileData = getByteProp("newTileData");
/* 3663 */         newTileType = indexTileType("newTileType");
/*      */         
/* 3665 */         spawnTileX = getIntProp("spawnTileX");
/* 3666 */         spawnTileY = getIntProp("spawnTileY");
/* 3667 */         creatureSpawn = indexCreatureTemplate("creatureSpawn", "Creature Spawn");
/* 3668 */         creatureAge = getIntProp("creatureAge");
/* 3669 */         creatureName = getStringProp("creatureName");
/* 3670 */         creatureType = indexCreatureType("creatureType", "Creature Type");
/*      */         
/* 3672 */         teleportX = getIntProp("teleportTileX");
/* 3673 */         teleportY = getIntProp("teleportTileY");
/* 3674 */         teleportLayer = indexLayer("teleportLayer", "Teleport Layer");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3682 */       specialEffect = indexSpecialEffect("specialEffect", "Special Effect", true);
/*      */       
/* 3684 */       achievementId = indexAchievementId("achievement", "achievement");
/*      */       
/* 3686 */       missionAffectedId = indexMission("missionId", "mission Id");
/* 3687 */       missionStateChange = indexStateChange("missionStateChange", specialEffect);
/*      */       
/* 3689 */       missionActivated = indexMission("missionToActivate", "mission To Activate");
/* 3690 */       missionDeActivated = indexMission("missionToDeactivate", "mission To Deactivate");
/*      */       
/* 3692 */       triggerActivated = indexTrigger("triggerToActivate", "trigger To Activate");
/* 3693 */       triggerDeActivated = indexTrigger("triggerToDeactivate", "trigger To Deactivate");
/*      */       
/* 3695 */       effectActivated = indexTrigger("effectToActivate", "effect To Activate");
/* 3696 */       effectDeActivated = indexTrigger("effectToDeactivate", "effect To Deactivate");
/*      */       
/* 3698 */       if (getResponder().getPower() > 0 || this.level == 8)
/*      */       {
/* 3700 */         existingItem = indexExistingItem("existingItem", existingItem);
/*      */       }
/*      */       
/* 3703 */       if (this.errorText.length() > 0)
/*      */       {
/* 3705 */         return editEffect(this.effectId, getAnswer());
/*      */       }
/*      */       
/* 3708 */       boolean changed = false;
/* 3709 */       if (!name.equals(eff.getName())) {
/*      */         
/* 3711 */         eff.setName(name);
/* 3712 */         changed = true;
/*      */       } 
/* 3714 */       if (!description.equals(eff.getDescription())) {
/*      */         
/* 3716 */         eff.setDescription(description);
/* 3717 */         changed = true;
/*      */       } 
/* 3719 */       if (inActive != eff.isInactive()) {
/*      */         
/* 3721 */         eff.setInactive(inActive);
/* 3722 */         changed = true;
/*      */       } 
/* 3724 */       if (itemReward != eff.getRewardItem()) {
/*      */         
/* 3726 */         eff.setRewardItem(itemReward);
/* 3727 */         changed = true;
/*      */       } 
/* 3729 */       if (modifyTileX != eff.getModifyTileX()) {
/*      */         
/* 3731 */         eff.setModifyTileX(modifyTileX);
/* 3732 */         changed = true;
/*      */       } 
/* 3734 */       if (modifyTileY != eff.getModifyTileY()) {
/*      */         
/* 3736 */         eff.setModifyTileY(modifyTileY);
/* 3737 */         changed = true;
/*      */       } 
/* 3739 */       if (newTileType != eff.getNewTileType()) {
/*      */         
/* 3741 */         eff.setNewTileType(newTileType);
/* 3742 */         changed = true;
/*      */       } 
/* 3744 */       if (newTileData != eff.getNewTileData()) {
/*      */         
/* 3746 */         eff.setNewTileData(newTileData);
/* 3747 */         changed = true;
/*      */       } 
/* 3749 */       if (spawnTileX != eff.getSpawnTileX()) {
/*      */         
/* 3751 */         eff.setSpawnTileX(spawnTileX);
/* 3752 */         changed = true;
/*      */       } 
/* 3754 */       if (spawnTileY != eff.getSpawnTileY()) {
/*      */         
/* 3756 */         eff.setSpawnTileY(spawnTileY);
/* 3757 */         changed = true;
/*      */       } 
/* 3759 */       if (creatureSpawn != eff.getCreatureSpawn()) {
/*      */         
/* 3761 */         eff.setCreatureSpawn(creatureSpawn);
/* 3762 */         changed = true;
/*      */       } 
/* 3764 */       if (creatureAge != eff.getCreatureAge()) {
/*      */         
/* 3766 */         eff.setCreatureAge(creatureAge);
/* 3767 */         changed = true;
/*      */       } 
/* 3769 */       if (!creatureName.equals(eff.getCreatureName())) {
/*      */         
/* 3771 */         eff.setCreatureName(creatureName);
/* 3772 */         changed = true;
/*      */       } 
/* 3774 */       if (creatureType != eff.getCreatureType()) {
/*      */         
/* 3776 */         eff.setCreatureType(creatureType);
/* 3777 */         changed = true;
/*      */       } 
/* 3779 */       if (teleportX != eff.getTeleportX()) {
/*      */         
/* 3781 */         eff.setTeleportX(teleportX);
/* 3782 */         changed = true;
/*      */       } 
/* 3784 */       if (teleportY != eff.getTeleportY()) {
/*      */         
/* 3786 */         eff.setTeleportY(teleportY);
/* 3787 */         changed = true;
/*      */       } 
/* 3789 */       if (teleportLayer != eff.getTeleportLayer()) {
/*      */         
/* 3791 */         eff.setTeleportLayer(teleportLayer);
/* 3792 */         changed = true;
/*      */       } 
/* 3794 */       if (missionActivated != eff.getMissionToActivate()) {
/*      */         
/* 3796 */         eff.setMissionToActivate(missionActivated);
/* 3797 */         changed = true;
/*      */       } 
/* 3799 */       if (missionDeActivated != eff.getMissionToDeActivate()) {
/*      */         
/* 3801 */         eff.setMissionToDeActivate(missionDeActivated);
/* 3802 */         changed = true;
/*      */       } 
/* 3804 */       if (triggerActivated != eff.getTriggerToActivate()) {
/*      */         
/* 3806 */         eff.setTriggerToActivate(triggerActivated);
/* 3807 */         changed = true;
/*      */       } 
/* 3809 */       if (triggerDeActivated != eff.getTriggerToDeActivate()) {
/*      */         
/* 3811 */         eff.setTriggerToDeActivate(triggerDeActivated);
/* 3812 */         changed = true;
/*      */       } 
/* 3814 */       if (effectActivated != eff.getEffectToActivate()) {
/*      */         
/* 3816 */         eff.setEffectToActivate(effectActivated);
/* 3817 */         changed = true;
/*      */       } 
/* 3819 */       if (effectDeActivated != eff.getEffectToDeActivate()) {
/*      */         
/* 3821 */         eff.setEffectToDeActivate(effectDeActivated);
/* 3822 */         changed = true;
/*      */       } 
/* 3824 */       if (itemMaterial != eff.getItemMaterial()) {
/*      */         
/* 3826 */         eff.setItemMaterial(itemMaterial);
/* 3827 */         changed = true;
/*      */       } 
/* 3829 */       if (newbie != eff.isNewbieItem()) {
/*      */         
/* 3831 */         eff.setNewbieItem(newbie);
/* 3832 */         changed = true;
/*      */       } 
/* 3834 */       if (startSkill != eff.isStartSkillgain()) {
/*      */         
/* 3836 */         eff.setStartSkillgain(startSkill);
/* 3837 */         changed = true;
/*      */       } 
/* 3839 */       if (stopSkill != eff.isStopSkillgain()) {
/*      */         
/* 3841 */         eff.setStopSkillgain(stopSkill);
/* 3842 */         changed = true;
/*      */       } 
/* 3844 */       if (destroysInventory != eff.destroysInventory()) {
/*      */         
/* 3846 */         eff.setDestroyInventory(destroysInventory);
/* 3847 */         changed = true;
/*      */       } 
/* 3849 */       if (ql != eff.getRewardQl()) {
/*      */         
/* 3851 */         eff.setRewardQl(ql);
/* 3852 */         changed = true;
/*      */       } 
/* 3854 */       if (numbers != eff.getRewardNumbers()) {
/*      */         
/* 3856 */         eff.setRewardNumbers(numbers);
/* 3857 */         changed = true;
/*      */       } 
/* 3859 */       if (bytevalue != eff.getRewardByteValue()) {
/*      */         
/* 3861 */         eff.setRewardByteValue(bytevalue);
/* 3862 */         changed = true;
/*      */       } 
/* 3864 */       if (existingItem != eff.getExistingItemReward()) {
/*      */         
/* 3866 */         if (existingItem > 0L) {
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 3871 */             Item i = Items.getItem(existingItem);
/* 3872 */             if (i.getOwnerId() != getResponder().getWurmId() || i.isTraded() || i.isTransferred() || i.mailed) {
/*      */               
/* 3874 */               getResponder().getCommunicator().sendAlertServerMessage("The " + i
/* 3875 */                   .getName() + " may not be selected as reward right now.");
/* 3876 */               existingItem = 0L;
/*      */             } 
/* 3878 */             if (existingItem > 0L) {
/* 3879 */               i.putInVoid();
/*      */             }
/* 3881 */           } catch (NoSuchItemException nsi) {
/*      */             
/* 3883 */             existingItem = 0L;
/*      */           } 
/*      */         }
/* 3886 */         eff.setExistingItemReward(existingItem);
/* 3887 */         changed = true;
/*      */       } 
/* 3889 */       if (rewardTargetContainerId != eff.getRewardTargetContainerId()) {
/*      */         
/* 3891 */         eff.setRewardTargetContainerId(rewardTargetContainerId);
/* 3892 */         changed = true;
/*      */       } 
/* 3894 */       if (specialEffect != eff.getSpecialEffectId()) {
/*      */         
/* 3896 */         eff.setSpecialEffect(specialEffect);
/* 3897 */         changed = true;
/* 3898 */         if (eff.getTeleportX() > 0 || eff.getTeleportY() > 0)
/* 3899 */           getResponder().getCommunicator().sendNormalServerMessage("The special effect will affect tile " + eff
/* 3900 */               .getTeleportX() + "," + eff.getTeleportY() + "."); 
/*      */       } 
/* 3902 */       if (achievementId != eff.getAchievementId()) {
/*      */         
/* 3904 */         eff.setAchievementId(achievementId);
/* 3905 */         changed = true;
/*      */       } 
/* 3907 */       if (rewardSkillNum != eff.getRewardSkillNum()) {
/*      */         
/* 3909 */         eff.setRewardSkillNum(rewardSkillNum);
/* 3910 */         changed = true;
/*      */       } 
/* 3912 */       if (rewardSkillVal != eff.getRewardSkillModifier()) {
/*      */         
/* 3914 */         eff.setRewardSkillVal(rewardSkillVal);
/* 3915 */         changed = true;
/*      */       } 
/* 3917 */       if (missionAffectedId != eff.getMissionId()) {
/*      */         
/* 3919 */         eff.setMission(missionAffectedId);
/* 3920 */         changed = true;
/*      */       } 
/* 3922 */       if (missionStateChange != eff.getMissionStateChange()) {
/*      */         
/* 3924 */         eff.setMissionStateChange(missionStateChange);
/* 3925 */         changed = true;
/*      */       } 
/* 3927 */       if (destroysTarget != eff.destroysTarget()) {
/*      */         
/* 3929 */         eff.setDestroysTarget(destroysTarget);
/* 3930 */         changed = true;
/*      */       } 
/* 3932 */       if (!sound.equals(eff.getSoundName())) {
/*      */         
/* 3934 */         eff.setSoundName(sound);
/* 3935 */         changed = true;
/*      */       } 
/* 3937 */       if (setWindowSizeY != eff.getWindowSizeY()) {
/*      */         
/* 3939 */         eff.setWindowSizeY(setWindowSizeY);
/* 3940 */         changed = true;
/*      */       } 
/* 3942 */       if (setWindowSizeX != eff.getWindowSizeX()) {
/*      */         
/* 3944 */         eff.setWindowSizeX(setWindowSizeX);
/* 3945 */         changed = true;
/*      */       } 
/* 3947 */       if (!top.equals(eff.getTopText())) {
/*      */         
/* 3949 */         eff.setTopText(top);
/* 3950 */         changed = true;
/*      */       } 
/* 3952 */       if (!textdisplayed.equals(eff.getTextDisplayed())) {
/*      */         
/* 3954 */         eff.setTextDisplayed(textdisplayed);
/* 3955 */         changed = true;
/*      */       } 
/* 3957 */       if (this.level == 7) {
/*      */         
/* 3959 */         if (changed) {
/*      */           
/* 3961 */           eff.setLastModifierName(getResponder().getName());
/* 3962 */           eff.update();
/* 3963 */           getResponder().getCommunicator().sendNormalServerMessage("You update the effect " + eff.getName() + ".");
/*      */         } 
/* 3965 */         return parseBack();
/*      */       } 
/* 3967 */       if (changed) {
/*      */ 
/*      */         
/* 3970 */         if (getResponder().getPower() == 0) {
/*      */           
/* 3972 */           if (ruler.getAuxData() <= 0) {
/*      */             
/* 3974 */             getResponder().getCommunicator().sendAlertServerMessage("Your " + ruler
/* 3975 */                 .getName() + " contains no charges. You can not create a new trigger effect.");
/* 3976 */             return parseBack();
/*      */           } 
/*      */ 
/*      */           
/* 3980 */           ruler.setAuxData((byte)(ruler.getAuxData() - 1));
/* 3981 */           getResponder().getCommunicator().sendAlertServerMessage("You spend a charge from your " + ruler
/* 3982 */               .getName() + ". It now has " + ruler.getAuxData() + " charges left.");
/*      */ 
/*      */           
/* 3985 */           eff.setCreatorType((byte)3);
/*      */         } else {
/*      */           
/* 3988 */           eff.setCreatorType((byte)1);
/* 3989 */         }  eff.setOwnerId(getResponder().getWurmId());
/* 3990 */         eff.create();
/* 3991 */         TriggerEffects.addTriggerEffect(eff);
/* 3992 */         this.effectId = eff.getId();
/* 3993 */         getResponder().getCommunicator().sendNormalServerMessage("You create the effect " + eff.getName() + ".");
/* 3994 */         return editEffect(this.effectId, (Properties)null);
/*      */       } 
/* 3996 */       if (!changed)
/* 3997 */         getResponder().getCommunicator().sendNormalServerMessage("You change nothing."); 
/* 3998 */       return parseBack();
/*      */     } 
/*      */     
/* 4001 */     boolean createTrigger = getBooleanProp("createTrigger");
/* 4002 */     if (createTrigger) {
/*      */       
/* 4004 */       this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + "," + this.effectId;
/* 4005 */       return createNewTrigger((Properties)null);
/*      */     } 
/* 4007 */     boolean linkTrigger = getBooleanProp("linkTrigger");
/* 4008 */     if (linkTrigger) {
/*      */ 
/*      */       
/* 4011 */       int trig = indexUnlinkedTrigger("trigs", "Unlinked trigger");
/*      */       
/* 4013 */       if (trig > 0) {
/*      */ 
/*      */         
/* 4016 */         MissionTrigger mtrig = MissionTriggers.getTriggerWithId(trig);
/*      */         
/* 4018 */         if (mtrig != null) {
/*      */           
/* 4020 */           Triggers2Effects.addLink(mtrig.getId(), this.effectId, false);
/* 4021 */           getResponder().getCommunicator().sendNormalServerMessage("You link the trigger '" + mtrig
/* 4022 */               .getName() + "' to the effect '" + name + "'.");
/*      */         } 
/*      */       } 
/* 4025 */       return reshow();
/*      */     } 
/*      */     
/* 4028 */     for (String key : getAnswer().stringPropertyNames()) {
/*      */       
/* 4030 */       boolean edtT = key.startsWith("edtT");
/* 4031 */       boolean delT = key.startsWith("delT");
/* 4032 */       boolean unlT = key.startsWith("unlT");
/* 4033 */       if (edtT || delT || unlT) {
/*      */ 
/*      */ 
/*      */         
/* 4037 */         String sid = key.substring(4);
/* 4038 */         int tid = Integer.parseInt(sid);
/* 4039 */         MissionTrigger trg = MissionTriggers.getTriggerWithId(tid);
/* 4040 */         if (trg == null) {
/*      */           
/* 4042 */           this.errorText = "Cannot find trigger!";
/* 4043 */           getResponder().getCommunicator().sendNormalServerMessage(this.errorText);
/* 4044 */           return reshow();
/*      */         } 
/* 4046 */         if (edtT) {
/*      */           
/* 4048 */           this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + ",0";
/* 4049 */           return editTrigger(tid, (Properties)null);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4057 */         if (delT)
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 4062 */           return reshow();
/*      */         }
/* 4064 */         if (unlT) {
/*      */           
/* 4066 */           Triggers2Effects.deleteLink(tid, this.effectId);
/* 4067 */           getResponder().getCommunicator().sendNormalServerMessage("You unlink the trigger '" + trg
/* 4068 */               .getName() + "' from the effect '" + name + "'.");
/* 4069 */           return editEffect(this.effectId, (Properties)null);
/*      */         } 
/*      */       } 
/*      */     } 
/* 4073 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private long readContainerId() {
/* 4078 */     String svalue = getStringProp("rewardTargetContainerId");
/* 4079 */     long id = 0L;
/*      */     
/*      */     try {
/* 4082 */       id = Long.parseLong(svalue);
/*      */     }
/* 4084 */     catch (NumberFormatException nfe) {
/*      */       
/* 4086 */       if (this.errorText.isEmpty())
/* 4087 */         this.errorText = "Failed to parse value for rewardTargetContainerId."; 
/*      */     } 
/* 4089 */     if (id > 0L)
/*      */       
/*      */       try {
/*      */         
/* 4093 */         Item i = Items.getItem(id);
/* 4094 */         if (i.isTraded() || i.isTransferred() || i.mailed) {
/*      */           
/* 4096 */           id = 0L;
/* 4097 */           if (this.errorText.isEmpty()) {
/* 4098 */             this.errorText = "The " + i.getName() + " may not be used as container right now.";
/*      */           }
/*      */         } 
/* 4101 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 4103 */         id = 0L;
/* 4104 */         if (this.errorText.isEmpty()) {
/* 4105 */           this.errorText = "Container not found!";
/*      */         }
/*      */       }  
/* 4108 */     return id;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean parseMission(Item ruler) {
/* 4113 */     boolean delete = getBooleanProp("deleteMission");
/* 4114 */     Mission m = Missions.getMissionWithId(this.missionId);
/* 4115 */     if (m == null) {
/*      */       
/* 4117 */       if (delete) {
/*      */         
/* 4119 */         getResponder().getCommunicator().sendNormalServerMessage("You tried to delete a non-existing mission.");
/* 4120 */         return showMissionList();
/*      */       } 
/* 4122 */       m = new Mission(getResponder().getName(), getResponder().getName());
/*      */     }
/* 4124 */     else if (delete) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4131 */       m.destroy();
/* 4132 */       getResponder().getCommunicator().sendNormalServerMessage("You delete the mission.");
/* 4133 */       return showMissionList();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4141 */     this.errorText = "";
/* 4142 */     String name = m.getName();
/* 4143 */     String group = m.getGroupName();
/* 4144 */     String intro = m.getInstruction();
/* 4145 */     boolean inactive = m.isInactive();
/* 4146 */     boolean hidden = m.isHidden();
/* 4147 */     boolean faildeath = m.isFailOnDeath();
/* 4148 */     boolean mayBeRestarted = m.mayBeRestarted();
/* 4149 */     boolean hasSecondChance = m.hasSecondChance();
/* 4150 */     int maxTimeSeconds = m.getMaxTimeSeconds();
/*      */     
/* 4152 */     boolean cloneMission = getBooleanProp("cloneMission");
/* 4153 */     if (cloneMission) {
/*      */ 
/*      */       
/* 4156 */       this.sbacks += "|" + '\002' + "," + this.missionId + ",0,0";
/* 4157 */       return createNewMission(getAnswer());
/*      */     } 
/* 4159 */     boolean updateMission = getBooleanProp("updateMission");
/* 4160 */     boolean createMission = getBooleanProp("createMission");
/* 4161 */     if (updateMission || createMission) {
/*      */       
/* 4163 */       name = getStringProp("name", 3);
/* 4164 */       group = getStringProp("groupName");
/* 4165 */       intro = getStringProp("intro");
/* 4166 */       inactive = getBooleanProp("inactive");
/* 4167 */       hidden = getBooleanProp("hidden");
/* 4168 */       faildeath = getBooleanProp("faildeath");
/* 4169 */       mayBeRestarted = getBooleanProp("mayBeRestarted");
/* 4170 */       hasSecondChance = getBooleanProp("secondChance");
/*      */       
/* 4172 */       int days = getIntProp("days");
/* 4173 */       maxTimeSeconds = (int)(days * 86400L);
/* 4174 */       int hours = getIntProp("hours", 0, 23, false);
/* 4175 */       maxTimeSeconds = (int)(maxTimeSeconds + hours * 3600L);
/* 4176 */       int minutes = getIntProp("minutes", 0, 59, false);
/* 4177 */       maxTimeSeconds = (int)(maxTimeSeconds + minutes * 60L);
/* 4178 */       int secs = getIntProp("seconds", 0, 59, false);
/* 4179 */       maxTimeSeconds += secs;
/*      */       
/* 4181 */       if (this.errorText.length() > 0)
/*      */       {
/* 4183 */         return editMission(this.missionId, getAnswer());
/*      */       }
/*      */       
/* 4186 */       boolean changed = false;
/* 4187 */       if (!name.equals(m.getName())) {
/*      */         
/* 4189 */         m.setName(name);
/* 4190 */         changed = true;
/*      */       } 
/* 4192 */       if (!group.equals(m.getGroupName())) {
/*      */         
/* 4194 */         m.setGroupName(group);
/* 4195 */         changed = true;
/*      */       } 
/* 4197 */       if (!intro.equals(m.getInstruction())) {
/*      */         
/* 4199 */         m.setInstruction(intro);
/* 4200 */         changed = true;
/*      */       } 
/* 4202 */       if (inactive != m.isInactive()) {
/*      */         
/* 4204 */         m.setInactive(inactive);
/* 4205 */         changed = true;
/*      */       } 
/* 4207 */       if (hidden != m.isHidden()) {
/*      */         
/* 4209 */         m.setIsHidden(hidden);
/* 4210 */         changed = true;
/*      */       } 
/* 4212 */       if (faildeath != m.isFailOnDeath()) {
/*      */         
/* 4214 */         m.setFailOnDeath(faildeath);
/* 4215 */         changed = true;
/*      */       } 
/* 4217 */       if (mayBeRestarted != m.mayBeRestarted()) {
/*      */         
/* 4219 */         m.setMayBeRestarted(mayBeRestarted);
/* 4220 */         changed = true;
/*      */       } 
/* 4222 */       if (hasSecondChance != m.hasSecondChance()) {
/*      */         
/* 4224 */         m.setSecondChance(hasSecondChance);
/* 4225 */         changed = true;
/*      */       } 
/* 4227 */       if (maxTimeSeconds != m.getMaxTimeSeconds()) {
/*      */         
/* 4229 */         m.setMaxTimeSeconds(maxTimeSeconds);
/* 4230 */         changed = true;
/*      */       } 
/* 4232 */       if (this.level == 1) {
/*      */         
/* 4234 */         if (changed) {
/*      */           
/* 4236 */           if (getResponder().getPower() == 0) {
/*      */             
/* 4238 */             if (ruler.getAuxData() <= 0) {
/*      */               
/* 4240 */               getResponder().getCommunicator().sendAlertServerMessage("Your " + ruler
/* 4241 */                   .getName() + " contains no charges. You can not create a new mission.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 4248 */               return showMissionList();
/*      */             } 
/*      */ 
/*      */             
/* 4252 */             ruler.setAuxData((byte)(ruler.getAuxData() - 1));
/* 4253 */             getResponder().getCommunicator().sendAlertServerMessage("You spend a charge from your " + ruler
/* 4254 */                 .getName() + ". It now has " + ruler.getAuxData() + " charges left.");
/*      */ 
/*      */             
/* 4257 */             m.setCreatorType((byte)3);
/*      */           } else {
/*      */             
/* 4260 */             m.setCreatorType((byte)1);
/* 4261 */           }  m.setOwnerId(getResponder().getWurmId());
/* 4262 */           m.create();
/* 4263 */           Missions.addMission(m);
/* 4264 */           getResponder().getCommunicator().sendNormalServerMessage("You create the mission " + m.getName() + ".");
/* 4265 */           this.missionId = m.getId();
/* 4266 */           return editMission(this.missionId, (Properties)null);
/*      */         } 
/*      */ 
/*      */         
/* 4270 */         getResponder().getCommunicator().sendNormalServerMessage("You decide not to create a new mission.");
/*      */         
/* 4272 */         return parseBack();
/*      */       } 
/* 4274 */       if (changed) {
/*      */         
/* 4276 */         m.setLastModifierName(getResponder().getName());
/* 4277 */         m.update();
/* 4278 */         getResponder().getCommunicator().sendNormalServerMessage("You update the mission " + m.getName() + ".");
/*      */       } 
/* 4280 */       if (!changed) {
/* 4281 */         getResponder().getCommunicator().sendNormalServerMessage("You change nothing.");
/*      */       }
/* 4283 */       return parseBack();
/*      */     } 
/*      */     
/* 4286 */     boolean createTrigger = getBooleanProp("createTrigger");
/* 4287 */     if (createTrigger) {
/*      */       
/* 4289 */       this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + "," + this.effectId;
/* 4290 */       return createNewTrigger((Properties)null);
/*      */     } 
/* 4292 */     boolean linkTrigger = getBooleanProp("linkTrigger");
/* 4293 */     if (linkTrigger) {
/*      */ 
/*      */       
/* 4296 */       int utrig = indexUnlinkedTrigger("unTrigs", "Unlinked trigger");
/*      */       
/* 4298 */       if (utrig > 0) {
/*      */ 
/*      */         
/* 4301 */         MissionTrigger mtrig = MissionTriggers.getTriggerWithId(utrig);
/*      */         
/* 4303 */         if (mtrig != null) {
/*      */           
/* 4305 */           mtrig.setMissionRequirement(this.missionId);
/* 4306 */           getResponder().getCommunicator().sendNormalServerMessage("You link the trigger '" + mtrig
/* 4307 */               .getName() + "' to the mission '" + name + "'.");
/*      */         } 
/*      */       } 
/* 4310 */       return reshow();
/*      */     } 
/* 4312 */     boolean linkEffect = getBooleanProp("linkEffect");
/* 4313 */     if (linkEffect) {
/*      */       
/* 4315 */       this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + "," + this.effectId;
/*      */       
/* 4317 */       int uEff = indexUnlinkedEffect("unEffs", "Unlinked effects");
/*      */       
/* 4319 */       if (uEff > 0) {
/*      */ 
/*      */         
/* 4322 */         TriggerEffect mEff = TriggerEffects.getTriggerEffect(uEff);
/*      */         
/* 4324 */         if (mEff != null) {
/*      */           
/* 4326 */           mEff.setMission(this.missionId);
/* 4327 */           getResponder().getCommunicator().sendNormalServerMessage("You link the effect '" + mEff
/* 4328 */               .getName() + "' to the mision '" + name + "'.");
/*      */         } 
/*      */       } 
/* 4331 */       return reshow();
/*      */     } 
/* 4333 */     boolean createEffect = getBooleanProp("createEffect");
/* 4334 */     if (createEffect) {
/*      */       
/* 4336 */       this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + "," + this.effectId;
/* 4337 */       return createNewEffect((Properties)null);
/*      */     } 
/*      */     
/* 4340 */     for (String key : getAnswer().stringPropertyNames()) {
/*      */       
/* 4342 */       boolean edtT = key.startsWith("edtT");
/* 4343 */       boolean delT = key.startsWith("delT");
/* 4344 */       boolean unlT = key.startsWith("unlT");
/* 4345 */       if (edtT || delT || unlT) {
/*      */ 
/*      */ 
/*      */         
/* 4349 */         String sid = key.substring(4);
/* 4350 */         int tid = Integer.parseInt(sid);
/* 4351 */         MissionTrigger trg = MissionTriggers.getTriggerWithId(tid);
/* 4352 */         if (trg == null) {
/*      */           
/* 4354 */           this.errorText = "Cannot find trigger!";
/* 4355 */           getResponder().getCommunicator().sendNormalServerMessage(this.errorText);
/* 4356 */           return reshow();
/*      */         } 
/* 4358 */         if (edtT) {
/*      */           
/* 4360 */           this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + ",0";
/* 4361 */           return editTrigger(tid, (Properties)null);
/*      */         } 
/* 4363 */         if (delT)
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 4368 */           return reshow();
/*      */         }
/* 4370 */         if (unlT) {
/*      */           
/* 4372 */           trg.setMissionRequirement(0);
/* 4373 */           getResponder().getCommunicator().sendNormalServerMessage("You unlink the trigger '" + trg
/* 4374 */               .getName() + "' from the mission.");
/* 4375 */           return reshow();
/*      */         } 
/*      */       } 
/* 4378 */       boolean edtE = key.startsWith("edtE");
/* 4379 */       boolean delE = key.startsWith("delE");
/* 4380 */       boolean unlE = key.startsWith("unlE");
/* 4381 */       boolean clrE = key.startsWith("clrE");
/* 4382 */       if (edtE || delE || unlE || clrE) {
/*      */ 
/*      */ 
/*      */         
/* 4386 */         String sid = key.substring(4);
/* 4387 */         int eid = Integer.parseInt(sid);
/* 4388 */         TriggerEffect te = TriggerEffects.getTriggerEffect(eid);
/* 4389 */         if (te == null) {
/*      */           
/* 4391 */           this.errorText = "Cannot find effect!";
/* 4392 */           getResponder().getCommunicator().sendNormalServerMessage(this.errorText);
/* 4393 */           return reshow();
/*      */         } 
/* 4395 */         if (edtE) {
/*      */           
/* 4397 */           this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + ",0";
/* 4398 */           return editEffect(eid, (Properties)null);
/*      */         } 
/* 4400 */         if (delE)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4406 */           return reshow();
/*      */         }
/* 4408 */         if (unlE) {
/*      */ 
/*      */           
/* 4411 */           te.setMission(0);
/* 4412 */           getResponder().getCommunicator().sendNormalServerMessage("You unlink the effect '" + te
/* 4413 */               .getName() + "' from the mission.");
/*      */           
/* 4415 */           return reshow();
/*      */         } 
/* 4417 */         if (clrE) {
/*      */ 
/*      */           
/* 4420 */           te.setTextDisplayed("");
/* 4421 */           te.setTopText("");
/* 4422 */           getResponder().getCommunicator().sendNormalServerMessage("You clear the text for trigger effect " + te
/* 4423 */               .getName() + ".");
/* 4424 */           logger.log(Level.INFO, getResponder().getName() + " cleared text of effect " + te.getName());
/* 4425 */           return reshow();
/*      */         } 
/*      */       } 
/*      */     } 
/* 4429 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean parseTrigger(Item ruler) {
/* 4434 */     boolean delete = getBooleanProp("deleteTrigger");
/* 4435 */     MissionTrigger trg = MissionTriggers.getTriggerWithId(this.triggerId);
/* 4436 */     if (trg == null) {
/*      */       
/* 4438 */       if (delete) {
/*      */         
/* 4440 */         getResponder().getCommunicator().sendNormalServerMessage("You tried to delete a non-existing mission trigger.");
/* 4441 */         return showTriggerList(this.missionId);
/*      */       } 
/* 4443 */       trg = new MissionTrigger();
/* 4444 */       trg.setCreatorName(getResponder().getName());
/* 4445 */       trg.setLastModifierName(getResponder().getName());
/*      */     }
/* 4447 */     else if (delete) {
/*      */       
/* 4449 */       trg.destroy();
/* 4450 */       getResponder().getCommunicator().sendNormalServerMessage("You delete the mission trigger.");
/* 4451 */       return showTriggerList(this.missionId);
/*      */     } 
/*      */ 
/*      */     
/* 4455 */     if (getResponder().getLogger() != null && getResponder().getPower() > 0 && trg.getName() != null) {
/* 4456 */       getResponder().getLogger().info(getResponder() + ": Editing mission trigger with trigger name: " + trg.getName() + " and description " + trg.getDescription());
/*      */     }
/*      */     
/* 4459 */     this.errorText = "";
/* 4460 */     String name = trg.getName();
/*      */     
/* 4462 */     boolean inActive = trg.isInactive();
/* 4463 */     String description = trg.getDescription();
/*      */     
/* 4465 */     int onItemCreatedId = trg.getItemUsedId();
/*      */     
/* 4467 */     int onActionPerformed = trg.getOnActionPerformed();
/*      */     
/* 4469 */     long onActionTargetId = trg.getTarget();
/* 4470 */     boolean useCurrentTarget = (onActionTargetId == this.currentTargetId);
/*      */     
/* 4472 */     boolean spawnpoint = false;
/* 4473 */     spawnpoint = trg.isSpawnPoint();
/* 4474 */     int seconds = trg.getSeconds();
/*      */     
/* 4476 */     int missionRequired = trg.getMissionRequired();
/*      */     
/* 4478 */     float stateFrom = trg.getStateRequired();
/* 4479 */     float stateTo = trg.getStateEnd();
/*      */     
/* 4481 */     boolean cloneTrigger = getBooleanProp("cloneTrigger");
/* 4482 */     if (cloneTrigger) {
/*      */ 
/*      */       
/* 4485 */       this.sbacks += "|" + '\003' + "," + this.missionId + "," + this.triggerId + ",0";
/* 4486 */       return createNewTrigger(getAnswer());
/*      */     } 
/* 4488 */     boolean updateTrigger = getBooleanProp("updateTrigger");
/* 4489 */     boolean createTrigger = getBooleanProp("createTrigger");
/* 4490 */     if (updateTrigger || createTrigger) {
/*      */       
/* 4492 */       name = getStringProp("name", 3);
/* 4493 */       description = getStringProp("desc");
/* 4494 */       inActive = getBooleanProp("inactive");
/* 4495 */       spawnpoint = getBooleanProp("spawnpoint");
/* 4496 */       onItemCreatedId = indexItemTemplate("onItemCreatedId", "Item Created");
/* 4497 */       onActionPerformed = indexActionId("actionId", "action");
/* 4498 */       useCurrentTarget = getBooleanProp("useCurrentTarget");
/* 4499 */       if (useCurrentTarget) {
/*      */         
/* 4501 */         if (getResponder().getPower() == 0 && this.target <= 0L) {
/*      */           
/* 4503 */           if (this.errorText.isEmpty()) {
/* 4504 */             this.errorText = "The trigger needs a valid target.";
/*      */           }
/*      */         } else {
/* 4507 */           onActionTargetId = this.currentTargetId;
/*      */         } 
/*      */       } else {
/*      */         
/* 4511 */         onActionTargetId = getLongProp("targetid");
/* 4512 */         if (getResponder().getPower() == 0 && onActionTargetId <= 0L)
/*      */         {
/* 4514 */           if (this.errorText.isEmpty())
/* 4515 */             this.errorText = "The trigger needs a valid target."; 
/*      */         }
/*      */       } 
/* 4518 */       this.currentTargetId = onActionTargetId;
/* 4519 */       missionRequired = indexMission("missionRequired", "available missions");
/* 4520 */       if (onActionPerformed == 475)
/*      */       {
/* 4522 */         if (WurmId.getType(onActionTargetId) == 3)
/*      */         {
/* 4524 */           if (getResponder().getPower() <= 0) {
/*      */             
/* 4526 */             int tilex = Tiles.decodeTileX(onActionTargetId);
/* 4527 */             int tiley = Tiles.decodeTileY(onActionTargetId);
/* 4528 */             Village v = Villages.getVillage(tilex, tiley, true);
/* 4529 */             if (v == null || v != getResponder().getCitizenVillage()) {
/*      */               
/* 4531 */               if (this.errorText.isEmpty())
/* 4532 */                 this.errorText = "You are only allowed to set the step on trigger action in your own settlement."; 
/* 4533 */               onActionPerformed = 0;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/*      */       
/* 4539 */       stateFrom = getFloatProp("stateFrom", -1.0F, 100.0F, true);
/* 4540 */       stateTo = getFloatProp("stateTo", -1.0F, 100.0F, true);
/* 4541 */       if (stateTo < stateFrom)
/* 4542 */         stateTo = 0.0F; 
/* 4543 */       seconds = getIntProp("seconds", 0, 59, false);
/*      */       
/* 4545 */       if (this.errorText.length() > 0)
/*      */       {
/* 4547 */         return editTrigger(this.triggerId, getAnswer());
/*      */       }
/*      */       
/* 4550 */       boolean changed = false;
/* 4551 */       if (!name.equals(trg.getName())) {
/*      */         
/* 4553 */         trg.setName(name);
/* 4554 */         changed = true;
/*      */       } 
/* 4556 */       if (!description.equals(trg.getDescription())) {
/*      */         
/* 4558 */         trg.setDescription(description);
/* 4559 */         changed = true;
/*      */       } 
/* 4561 */       if (inActive != trg.isInactive()) {
/*      */         
/* 4563 */         trg.setInactive(inActive);
/* 4564 */         changed = true;
/*      */       } 
/* 4566 */       if (spawnpoint != trg.isSpawnPoint()) {
/*      */         
/* 4568 */         trg.setIsSpawnpoint(spawnpoint);
/* 4569 */         changed = true;
/*      */       } 
/* 4571 */       if (onActionTargetId != trg.getTarget()) {
/*      */         
/* 4573 */         if (this.level == 3)
/*      */         {
/* 4575 */           MissionTargets.removeMissionTrigger(trg, false);
/*      */         }
/* 4577 */         trg.setOnTargetId(onActionTargetId);
/* 4578 */         if (this.level == 3)
/*      */         {
/* 4580 */           MissionTargets.addMissionTrigger(trg);
/*      */         }
/* 4582 */         changed = true;
/*      */       } 
/* 4584 */       if (missionRequired != trg.getMissionRequired()) {
/*      */         
/* 4586 */         trg.setMissionRequirement(missionRequired);
/* 4587 */         changed = true;
/*      */       } 
/* 4589 */       if (stateFrom != trg.getStateRequired()) {
/*      */         
/* 4591 */         trg.setStateRequirement(stateFrom);
/* 4592 */         changed = true;
/*      */       } 
/* 4594 */       if (stateTo != trg.getStateEnd()) {
/*      */         
/* 4596 */         trg.setStateEnd(stateTo);
/* 4597 */         changed = true;
/*      */       } 
/* 4599 */       if (seconds > 0 && Action.isQuick(onActionPerformed)) {
/*      */         
/* 4601 */         seconds = 0;
/* 4602 */         getResponder().getCommunicator().sendAlertServerMessage("Seconds were set to 0 for that action since it is quick.");
/*      */       } 
/* 4604 */       if (seconds != trg.getSeconds()) {
/*      */         
/* 4606 */         trg.setSeconds(seconds);
/* 4607 */         changed = true;
/*      */       } 
/* 4609 */       if (onItemCreatedId != trg.getItemUsedId()) {
/*      */         
/* 4611 */         trg.setOnItemUsedId(onItemCreatedId);
/* 4612 */         changed = true;
/*      */       } 
/* 4614 */       if (onActionPerformed != trg.getOnActionPerformed()) {
/*      */         
/* 4616 */         trg.setOnActionPerformed(onActionPerformed);
/* 4617 */         changed = true;
/*      */       } 
/* 4619 */       if (this.level == 3) {
/*      */         
/* 4621 */         if (changed) {
/*      */           
/* 4623 */           trg.setLastModifierName(getResponder().getName());
/* 4624 */           trg.update();
/* 4625 */           getResponder().getCommunicator().sendNormalServerMessage("You change the trigger.");
/*      */         } 
/* 4627 */         return parseBack();
/*      */       } 
/* 4629 */       if (changed) {
/*      */ 
/*      */         
/* 4632 */         if (getResponder().getPower() == 0) {
/*      */           
/* 4634 */           if (ruler.getAuxData() <= 0) {
/*      */             
/* 4636 */             getResponder().getCommunicator().sendAlertServerMessage("Your " + ruler
/* 4637 */                 .getName() + " contains no charges. You can not create a new mission trigger.");
/* 4638 */             return parseBack();
/*      */           } 
/*      */ 
/*      */           
/* 4642 */           ruler.setAuxData((byte)(ruler.getAuxData() - 1));
/* 4643 */           getResponder().getCommunicator().sendAlertServerMessage("You spend a charge from your " + ruler
/* 4644 */               .getName() + ". It now has " + ruler.getAuxData() + " charges left.");
/*      */ 
/*      */           
/* 4647 */           trg.setCreatorType((byte)3);
/*      */         } else {
/*      */           
/* 4650 */           trg.setCreatorType((byte)1);
/* 4651 */         }  trg.setOwnerId(getResponder().getWurmId());
/* 4652 */         trg.create();
/* 4653 */         this.triggerId = trg.getId();
/* 4654 */         MissionTriggers.addMissionTrigger(trg);
/* 4655 */         getResponder().getCommunicator().sendNormalServerMessage("You create the trigger " + trg.getName() + ".");
/* 4656 */         return editTrigger(this.triggerId, (Properties)null);
/*      */       } 
/* 4658 */       if (!changed)
/* 4659 */         getResponder().getCommunicator().sendNormalServerMessage("You change nothing."); 
/* 4660 */       return parseBack();
/*      */     } 
/* 4662 */     boolean createEffect = getBooleanProp("createEffect");
/* 4663 */     if (createEffect) {
/*      */       
/* 4665 */       this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + ",0";
/* 4666 */       return createNewEffect((Properties)null);
/*      */     } 
/* 4668 */     boolean linkEffect = getBooleanProp("linkEffect");
/* 4669 */     if (linkEffect) {
/*      */ 
/*      */ 
/*      */       
/* 4673 */       int effe = indexUnlinkedEffect("linkEffects", "all effects");
/*      */       
/* 4675 */       if (effe > 0) {
/*      */ 
/*      */         
/* 4678 */         TriggerEffect meff = TriggerEffects.getTriggerEffect(effe);
/*      */         
/* 4680 */         if (meff != null) {
/*      */           
/* 4682 */           Triggers2Effects.addLink(this.triggerId, meff.getId(), false);
/* 4683 */           getResponder().getCommunicator().sendNormalServerMessage("You link the effect '" + meff
/* 4684 */               .getName() + "' to the trigger '" + name + "'.");
/*      */         } 
/*      */       } 
/* 4687 */       return editTrigger(this.triggerId, (Properties)null);
/*      */     } 
/*      */     
/* 4690 */     for (String key : getAnswer().stringPropertyNames()) {
/*      */       
/* 4692 */       boolean edtE = key.startsWith("edtE");
/* 4693 */       boolean delE = key.startsWith("delE");
/* 4694 */       boolean unlE = key.startsWith("unlE");
/* 4695 */       boolean clrE = key.startsWith("clrE");
/* 4696 */       if (edtE || delE || unlE || clrE) {
/*      */ 
/*      */ 
/*      */         
/* 4700 */         String sid = key.substring(4);
/* 4701 */         int eid = Integer.parseInt(sid);
/* 4702 */         TriggerEffect te = TriggerEffects.getTriggerEffect(eid);
/* 4703 */         if (te == null) {
/*      */           
/* 4705 */           this.errorText = "Cannot find effect!";
/* 4706 */           getResponder().getCommunicator().sendNormalServerMessage(this.errorText);
/* 4707 */           return reshow();
/*      */         } 
/* 4709 */         if (edtE) {
/*      */           
/* 4711 */           this.sbacks += "|" + this.level + "," + this.missionId + "," + this.triggerId + ",0";
/* 4712 */           return editEffect(eid, (Properties)null);
/*      */         } 
/* 4714 */         if (delE)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4720 */           return reshow();
/*      */         }
/* 4722 */         if (unlE) {
/*      */ 
/*      */           
/* 4725 */           Triggers2Effects.deleteLink(this.triggerId, eid);
/* 4726 */           getResponder().getCommunicator().sendNormalServerMessage("You unlink the effect '" + te
/* 4727 */               .getName() + "' from the trigger '" + name + "'.");
/* 4728 */           return editTrigger(this.triggerId, (Properties)null);
/*      */         } 
/* 4730 */         if (clrE) {
/*      */ 
/*      */           
/* 4733 */           te.setTextDisplayed("");
/* 4734 */           te.setTopText("");
/* 4735 */           getResponder().getCommunicator().sendNormalServerMessage("You clear the text for trigger effect " + te
/* 4736 */               .getName() + ".");
/* 4737 */           logger.log(Level.INFO, getResponder().getName() + " cleared text of effect " + te.getName());
/* 4738 */           return reshow();
/*      */         } 
/*      */       } 
/*      */     } 
/* 4742 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendMissionList() {
/* 4747 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4749 */     buf.append("border{border{size=\"20,40\";null;null;varray{rescale=\"true\";harray{label{type='bold';text=\"" + this.question + "\"};");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4757 */     if (getResponder().getPower() > 0) {
/*      */       
/* 4759 */       buf.append("label{text=\"Group: \";hover=\"TODO\"};input{id=\"groupName\";text=\"" + this.groupName + "\";maxchars=\"20\"}label{text=\" Player: \"};input{id=\"specialName\";text=\"" + this.userName + "\";maxchars=\"30\"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4764 */       buf.append("harray{checkbox{id=\"incMInactive\";text=\"Include inactive      \"" + 
/* 4765 */           addSel(this.incMInactive) + ";hover=\"TODO\"};label{text=\"Include:\"};radio{group=\"includeM\";id=\"" + Character.MIN_VALUE + "\";text=\"All  \"" + 
/*      */           
/* 4767 */           addSel((this.includeM == 0)) + ";hover=\"TODO\"};radio{group=\"includeM\";id=\"" + '\001' + "\";text=\"With triggers  \"" + 
/* 4768 */           addSel((this.includeM == 1)) + ";hover=\"TODO\"};radio{group=\"includeM\";id=\"" + '\002' + "\";text=\"Without triggers  \"" + 
/* 4769 */           addSel((this.includeM == 2)) + ";hover=\"TODO\"};}");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 4774 */       buf.append("label{text=\"Group: \";hover=\"TODO\"};input{id=\"groupName\";text=\"" + this.groupName + "\";maxchars=\"20\"}};");
/*      */ 
/*      */       
/* 4777 */       buf.append(appendChargeInfo());
/*      */     } 
/* 4779 */     buf.append("}varray{harray{label{text=\"           \"};button{text=\"Back\";id=\"back\"};label{text=\" \"}}harray{label{text=\" \"};button{text=\"Apply Filter\";id=\"filter\"};label{text=\" \"}}}null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4795 */         getId() + "\"}");
/* 4796 */     int absSortBy = Math.abs(this.sortBy);
/* 4797 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 4799 */     buf.append("table{rows=\"1\";cols=\"6\";label{text=\"\"};" + 
/*      */         
/* 4801 */         colHeader("Id", 1, this.sortBy) + 
/* 4802 */         colHeader("Group", 2, this.sortBy) + 
/* 4803 */         colHeader("Name", 3, this.sortBy) + 
/* 4804 */         colHeader("Owner", 4, this.sortBy) + 
/* 4805 */         colHeader("Last Modified", 5, this.sortBy));
/*      */ 
/*      */     
/* 4808 */     Mission[] missions = Missions.getFilteredMissions(getResponder(), this.includeM, this.incMInactive, this.dontListMine, this.listMineOnly, this.listForUser, this.groupName, this.onlyCurrent, this.currentTargetId);
/*      */     
/* 4810 */     if (missions.length > 0) {
/*      */ 
/*      */       
/* 4813 */       switch (absSortBy) {
/*      */         
/*      */         case 1:
/* 4816 */           Arrays.sort(missions, new Comparator<Mission>()
/*      */               {
/*      */                 
/*      */                 public int compare(Mission param1, Mission param2)
/*      */                 {
/* 4821 */                   int value1 = param1.getId();
/* 4822 */                   int value2 = param2.getId();
/* 4823 */                   if (value1 == value2)
/* 4824 */                     return 0; 
/* 4825 */                   if (value1 < value2) {
/* 4826 */                     return -1 * upDown;
/*      */                   }
/* 4828 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         
/*      */         case 2:
/* 4834 */           Arrays.sort(missions, new Comparator<Mission>()
/*      */               {
/*      */                 
/*      */                 public int compare(Mission param1, Mission param2)
/*      */                 {
/* 4839 */                   return param1.getName().compareTo(param2.getName());
/*      */                 }
/*      */               });
/* 4842 */           Arrays.sort(missions, new Comparator<Mission>()
/*      */               {
/*      */                 
/*      */                 public int compare(Mission param1, Mission param2)
/*      */                 {
/* 4847 */                   return param1.getGroupName().compareTo(param2.getGroupName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 3:
/* 4852 */           Arrays.sort(missions, new Comparator<Mission>()
/*      */               {
/*      */                 
/*      */                 public int compare(Mission param1, Mission param2)
/*      */                 {
/* 4857 */                   return param1.getName().compareTo(param2.getName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         
/*      */         case 4:
/* 4863 */           Arrays.sort(missions, new Comparator<Mission>()
/*      */               {
/*      */                 
/*      */                 public int compare(Mission param1, Mission param2)
/*      */                 {
/* 4868 */                   return param1.getName().compareTo(param2.getName()) * upDown;
/*      */                 }
/*      */               });
/* 4871 */           Arrays.sort(missions, new Comparator<Mission>()
/*      */               {
/*      */                 
/*      */                 public int compare(Mission param1, Mission param2)
/*      */                 {
/* 4876 */                   return param1.getOwnerName().compareTo(param2.getOwnerName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 5:
/* 4881 */           Arrays.sort(missions, new Comparator<Mission>()
/*      */               {
/*      */                 
/*      */                 public int compare(Mission param1, Mission param2)
/*      */                 {
/* 4886 */                   return param1.getLastModifiedDate().compareTo(param2.getLastModifiedDate()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */       } 
/*      */       
/* 4892 */       for (Mission mission : missions) {
/*      */         
/* 4894 */         String colour = "color=\"127,255,127\"";
/* 4895 */         String hover = ";hover=\"Active\"";
/* 4896 */         if (!mission.hasTriggers()) {
/*      */           
/* 4898 */           colour = "color=\"255,177,40\"";
/* 4899 */           hover = ";hover=\"No Triggers\"";
/*      */         }
/* 4901 */         else if (mission.isInactive()) {
/*      */           
/* 4903 */           colour = "color=\"255,127,127\"";
/* 4904 */           hover = ";hover=\"Inactive\"";
/*      */         }
/* 4906 */         else if (mission.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */           
/* 4908 */           colour = "color=\"140,140,255\";";
/* 4909 */           hover = ";hover=\"Current Target\"";
/*      */         } 
/* 4911 */         buf.append("radio{group=\"sel\";id=\"" + mission.getId() + "\"};label{" + colour + "text=\"" + mission
/* 4912 */             .getId() + " \"" + hover + "};label{" + colour + "text=\"" + mission
/* 4913 */             .getGroupName() + "\"" + hover + "};label{" + colour + "text=\"" + mission
/* 4914 */             .getName() + " \"" + hover + "};label{" + colour + "text=\"" + mission
/* 4915 */             .getOwnerName() + " \"" + hover + "};label{" + colour + "text=\"" + mission
/* 4916 */             .getLastModifiedString() + " \"" + hover + "};");
/*      */       } 
/*      */     } 
/*      */     
/* 4920 */     buf.append("}");
/* 4921 */     buf.append("radio{group=\"sel\";id=\"0\";selected=\"true\";text=\"None\"}");
/*      */     
/* 4923 */     buf.append("}};null;");
/* 4924 */     buf.append("varray{rescale=\"true\";");
/*      */     
/* 4926 */     buf.append("text{text=\"Select mission and choose what to do\"}");
/* 4927 */     buf.append("harray{button{id=\"editMission\";text=\"Edit Mission\";hover=\"If 'None' is selected then this will create a new mission.\"};label{text=\"  \"};button{id=\"showStats\";text=\"Show Stats \"};label{text=\"  \"};button{id=\"listTriggers\";text=\"List Triggers\";hover=\"If 'None' is selected then this will list all triggers.\"}label{text=\"  \"};button{id=\"listEffects\";text=\"List Effects \";hover=\"If 'None' is selected then this will list all effects.\"}label{text=\"  Or \"};button{id=\"createMission\";text=\"Create New Mission \"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4938 */     buf.append("}");
/* 4939 */     buf.append("}");
/* 4940 */     getResponder().getCommunicator().sendBml(600, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendTriggerList() {
/* 4945 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 4947 */     buf.append("border{border{size=\"20,40\";null;null;varray{rescale=\"true\";harray{label{type='bold';text=\"" + this.question + "\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4956 */     buf.append("harray{checkbox{id=\"incTInactive\";text=\"Include inactive      \"" + 
/* 4957 */         addSel(this.incTInactive) + ";hover=\"TODO\"};label{text=\"Show:\"};radio{group=\"showT\";id=\"" + Character.MIN_VALUE + "\";text=\"All  \"" + 
/*      */         
/* 4959 */         addSel((this.showT == 0)) + "};radio{group=\"showT\";id=\"" + '\001' + "\";text=\"Linked to missions\"" + 
/* 4960 */         addSel((this.showT == 1)) + "}radio{group=\"showT\";id=\"" + '\002' + "\";text=\"Unlinked\"" + 
/* 4961 */         addSel((this.showT == 2)) + "}}");
/*      */     
/* 4963 */     buf.append("}varray{harray{label{text=\"           \"};button{text=\"Back\";id=\"back\"};label{text=\" \"}}harray{label{text=\" \"};button{text=\"Apply Filter\";id=\"filter\"};label{text=\" \"}}}null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4979 */         getId() + "\"}");
/*      */     
/* 4981 */     int absSortBy = Math.abs(this.sortBy);
/* 4982 */     final int upDown = Integer.signum(this.sortBy);
/* 4983 */     MissionTrigger[] trigs = MissionTriggers.getFilteredTriggers(getResponder(), this.showT, this.incTInactive, this.missionId, 0);
/* 4984 */     if (trigs.length > 0) {
/*      */ 
/*      */       
/* 4987 */       switch (absSortBy) {
/*      */         
/*      */         case 1:
/* 4990 */           Arrays.sort(trigs, new Comparator<MissionTrigger>()
/*      */               {
/*      */                 
/*      */                 public int compare(MissionTrigger param1, MissionTrigger param2)
/*      */                 {
/* 4995 */                   int value1 = param1.getId();
/* 4996 */                   int value2 = param2.getId();
/* 4997 */                   if (value1 == value2)
/* 4998 */                     return 0; 
/* 4999 */                   if (value1 < value2) {
/* 5000 */                     return -1 * upDown;
/*      */                   }
/* 5002 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 2:
/* 5007 */           Arrays.sort(trigs, new Comparator<MissionTrigger>()
/*      */               {
/*      */                 
/*      */                 public int compare(MissionTrigger param1, MissionTrigger param2)
/*      */                 {
/* 5012 */                   return param1.getName().compareTo(param2.getName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 3:
/* 5017 */           Arrays.sort(trigs, new Comparator<MissionTrigger>()
/*      */               {
/*      */                 
/*      */                 public int compare(MissionTrigger param1, MissionTrigger param2)
/*      */                 {
/* 5022 */                   return param1.getStateRange().compareTo(param2.getStateRange()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 4:
/* 5027 */           Arrays.sort(trigs, new Comparator<MissionTrigger>()
/*      */               {
/*      */                 
/*      */                 public int compare(MissionTrigger param1, MissionTrigger param2)
/*      */                 {
/* 5032 */                   return param1.getActionString().compareTo(param2.getActionString()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 5:
/* 5037 */           Arrays.sort(trigs, new Comparator<MissionTrigger>()
/*      */               {
/*      */                 
/*      */                 public int compare(MissionTrigger param1, MissionTrigger param2)
/*      */                 {
/* 5042 */                   return param1.getTargetAsString(MissionManager.this.getResponder()).compareTo(param2.getTargetAsString(MissionManager.this.getResponder())) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */       } 
/* 5047 */       buf.append("table{rows=\"1\";cols=\"7\";label{text=\"\"};" + 
/*      */           
/* 5049 */           colHeader("Id", 1, this.sortBy) + 
/* 5050 */           colHeader("Name", 2, this.sortBy) + 
/* 5051 */           colHeader("State", 3, this.sortBy) + 
/* 5052 */           colHeader("Action", 4, this.sortBy) + 
/* 5053 */           colHeader("Target", 5, this.sortBy) + "label{text=\"\"};");
/*      */ 
/*      */       
/* 5056 */       for (MissionTrigger trigger : trigs) {
/*      */         
/* 5058 */         String colour = "color=\"127,255,127\"";
/* 5059 */         String hover = ";hover=\"Active\"";
/* 5060 */         if (trigger.isInactive()) {
/*      */           
/* 5062 */           colour = "color=\"255,127,127\"";
/* 5063 */           hover = ";hover=\"Inactive\"";
/*      */         }
/* 5065 */         else if (trigger.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */           
/* 5067 */           colour = "color=\"140,140,255\";";
/* 5068 */           hover = ";hover=\"Current Target\"";
/*      */         } 
/* 5070 */         buf.append("radio{group=\"sel\";id=\"" + trigger.getId() + "\"};label{" + colour + "text=\"" + trigger
/* 5071 */             .getId() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 5072 */             .getName() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 5073 */             .getStateRange() + " \"" + hover + "};label{" + colour + "text=\"" + trigger
/* 5074 */             .getActionString() + "\"" + hover + "};label{" + colour + "text=\"" + trigger
/* 5075 */             .getTargetAsString(getResponder()) + "\"" + hover + "};harray{label{text=\" \"}button{id=\"delT" + trigger
/*      */ 
/*      */             
/* 5078 */             .getId() + "\";text=\"Delete\"hover=\"This will delete " + trigger
/* 5079 */             .getName() + "\";confirm=\"You are about to delete " + trigger
/* 5080 */             .getName() + ".\";question=\"Do you really want to do that?\"};};");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5086 */       buf.append("}");
/*      */     } 
/* 5088 */     buf.append("radio{group=\"sel\";id=\"0\";selected=\"true\";text=\"None\"}");
/* 5089 */     buf.append("}};null;");
/*      */     
/* 5091 */     buf.append("varray{rescale=\"true\";");
/*      */     
/* 5093 */     buf.append("text{text=\"Select trigger and choose what to do\"}");
/* 5094 */     buf.append("harray{button{id=\"editTrigger\";text=\"Edit Trigger\"};label{text=\"  \"};button{id=\"listEffects\";text=\"List Effects \";hover=\"If 'None' is selected then this will list all effects.\"}label{text=\"  Or \"};button{id=\"createTrigger\";text=\"Create New Trigger\"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5101 */     buf.append("}");
/* 5102 */     buf.append("}");
/*      */     
/* 5104 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendEffectList() {
/* 5112 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 5114 */     buf.append("border{border{size=\"20,40\";null;null;varray{rescale=\"true\";harray{label{type='bold';text=\"" + this.question + "\"};}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5123 */     buf.append("harray{checkbox{id=\"incEInactive\";text=\"Include inactive  \"" + 
/* 5124 */         addSel(this.incEInactive) + "};label{text=\"Show:\"};radio{group=\"showE\";id=\"" + Character.MIN_VALUE + "\";text=\"All  \"" + 
/*      */         
/* 5126 */         addSel((this.showE == 0)) + "};radio{group=\"showE\";id=\"" + '\001' + "\";text=\"Linked to trigger\"" + 
/* 5127 */         addSel((this.showE == 1)) + "}radio{group=\"showE\";id=\"" + '\002' + "\";text=\"Unlinked\"" + 
/* 5128 */         addSel((this.showE == 2)) + "}}");
/*      */     
/* 5130 */     buf.append("}varray{harray{label{text=\"           \"};button{text=\"Back\";id=\"back\"};label{text=\" \"}}harray{label{text=\" \"};button{text=\"Apply Filter\";id=\"filter\"};label{text=\" \"}}}null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5146 */         getId() + "\"}");
/*      */     
/* 5148 */     int absSortBy = Math.abs(this.sortBy);
/* 5149 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 5151 */     boolean showAll = (this.missionId == 0 && this.triggerId == 0);
/* 5152 */     MissionTrigger[] trigs = MissionTriggers.getFilteredTriggers(getResponder(), this.showT, this.incTInactive, this.missionId, this.triggerId);
/* 5153 */     TriggerEffect[] effs = TriggerEffects.getFilteredEffects(trigs, getResponder(), this.showE, this.incEInactive, this.dontListMine, this.listMineOnly, this.listForUser, showAll);
/*      */     
/* 5155 */     if (effs.length > 0) {
/*      */ 
/*      */       
/* 5158 */       switch (absSortBy) {
/*      */         
/*      */         case 1:
/* 5161 */           Arrays.sort(effs, new Comparator<TriggerEffect>()
/*      */               {
/*      */                 
/*      */                 public int compare(TriggerEffect param1, TriggerEffect param2)
/*      */                 {
/* 5166 */                   int value1 = param1.getId();
/* 5167 */                   int value2 = param2.getId();
/* 5168 */                   if (value1 == value2)
/* 5169 */                     return 0; 
/* 5170 */                   if (value1 < value2) {
/* 5171 */                     return -1 * upDown;
/*      */                   }
/* 5173 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 2:
/* 5178 */           Arrays.sort(effs, new Comparator<TriggerEffect>()
/*      */               {
/*      */                 
/*      */                 public int compare(TriggerEffect param1, TriggerEffect param2)
/*      */                 {
/* 5183 */                   return param1.getName().compareTo(param2.getName()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 3:
/* 5188 */           Arrays.sort(effs, new Comparator<TriggerEffect>()
/*      */               {
/*      */                 
/*      */                 public int compare(TriggerEffect param1, TriggerEffect param2)
/*      */                 {
/* 5193 */                   float value1 = param1.getMissionStateChange();
/* 5194 */                   float value2 = param2.getMissionStateChange();
/* 5195 */                   if (value1 == value2)
/* 5196 */                     return 0; 
/* 5197 */                   if (value1 < value2) {
/* 5198 */                     return -1 * upDown;
/*      */                   }
/* 5200 */                   return 1 * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */         case 4:
/* 5205 */           Arrays.sort(effs, new Comparator<TriggerEffect>()
/*      */               {
/*      */                 
/*      */                 public int compare(TriggerEffect param1, TriggerEffect param2)
/*      */                 {
/* 5210 */                   return param1.getType().compareTo(param2.getType()) * upDown;
/*      */                 }
/*      */               });
/*      */           break;
/*      */       } 
/* 5215 */       buf.append("table{rows=\"1\";cols=\"7\";");
/* 5216 */       buf.append("label{text=\"\"};" + 
/* 5217 */           colHeader("Id", 1, this.sortBy) + 
/* 5218 */           colHeader("Name", 2, this.sortBy) + 
/* 5219 */           colHeader("+State", 3, this.sortBy) + 
/* 5220 */           colHeader("Type", 4, this.sortBy) + "label{text=\"\"};label{text=\"\"};");
/*      */ 
/*      */ 
/*      */       
/* 5224 */       for (TriggerEffect lEff : effs) {
/*      */         
/* 5226 */         String colour = "color=\"127,255,127\"";
/* 5227 */         String hover = ";hover=\"Active\"";
/* 5228 */         if (lEff.isInactive()) {
/*      */           
/* 5230 */           colour = "color=\"255,127,127\"";
/* 5231 */           hover = ";hover=\"Inactive\"";
/*      */         }
/* 5233 */         else if (lEff.hasTargetOf(this.currentTargetId, getResponder())) {
/*      */           
/* 5235 */           colour = "color=\"140,140,255\";";
/* 5236 */           hover = ";hover=\"Current Target\"";
/*      */         } 
/* 5238 */         String clrtxt = "label{text=\"  no text to clear\"}";
/* 5239 */         if (!lEff.getTextDisplayed().isEmpty())
/*      */         {
/*      */           
/* 5242 */           clrtxt = "harray{label{text=\" \";}button{id=\"clrE" + lEff.getId() + "\";text=\"Clear Text\"};}";
/*      */         }
/* 5244 */         buf.append("radio{group=\"sel\";id=\"" + lEff.getId() + "\"};label{" + colour + "text=\"" + lEff
/* 5245 */             .getId() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 5246 */             .getName() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 5247 */             .getMissionStateChange() + " \"" + hover + "};label{" + colour + "text=\"" + lEff
/* 5248 */             .getType() + " \"" + hover + "};harray{label{text=\" \";}button{id=\"delE" + lEff
/*      */ 
/*      */             
/* 5251 */             .getId() + "\";text=\"Delete\"hover=\"This will delete " + lEff
/* 5252 */             .getName() + "\";confirm=\"You are about to delete " + lEff
/* 5253 */             .getName() + ".\";question=\"Do you really want to do that?\"};};" + clrtxt);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5260 */       buf.append("}");
/*      */     } 
/* 5262 */     buf.append("radio{group=\"sel\";id=\"0\";selected=\"true\";text=\"None\"}");
/*      */     
/* 5264 */     buf.append("}};null;");
/*      */     
/* 5266 */     buf.append("varray{rescale=\"true\";");
/*      */     
/* 5268 */     buf.append("text{text=\"Select effect and choose what to do\"}");
/* 5269 */     buf.append("harray{button{id=\"editEffect\";text=\"Edit Effect\"};label{text=\"  \"};button{id=\"showTriggers\";text=\"Show Triggers\";hover=\"Not implemented (yet)\"};label{text=\"  Or \"};button{id=\"createEffect\";text=\"Create New Effect\"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5276 */     buf.append("}");
/* 5277 */     buf.append("}");
/*      */     
/* 5279 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private String addSel(boolean value) {
/* 5284 */     return ";selected=\"" + (value ? "true" : "false") + "\"";
/*      */   }
/*      */ 
/*      */   
/*      */   private int getIntProp(String key) {
/* 5289 */     int value = 0;
/* 5290 */     String svalue = getStringProp(key);
/* 5291 */     if (svalue != null && !svalue.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 5295 */         value = Integer.parseInt(svalue);
/*      */       }
/* 5297 */       catch (NumberFormatException nfe) {
/*      */         
/* 5299 */         if (this.errorText.isEmpty()) {
/* 5300 */           this.errorText = "Failed to parse value for " + key + ".";
/*      */         }
/*      */       }  
/* 5303 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private int getIntProp(String key, int min, int max, boolean restrict) {
/* 5308 */     int value = getIntProp(key);
/* 5309 */     if (max > min && restrict)
/*      */     {
/* 5311 */       if (restrict) {
/*      */         
/* 5313 */         if (value > max)
/* 5314 */           value = max; 
/* 5315 */         if (value < min) {
/* 5316 */           value = min;
/*      */         
/*      */         }
/*      */       }
/* 5320 */       else if (value < min || value > max) {
/*      */         
/* 5322 */         if (this.errorText.isEmpty())
/* 5323 */           this.errorText = key + " not in required range " + min + "-" + max + "."; 
/* 5324 */         value = min;
/*      */       } 
/*      */     }
/*      */     
/* 5328 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private byte getByteProp(String key) {
/* 5333 */     byte value = 0;
/* 5334 */     String svalue = getStringProp(key);
/* 5335 */     if (svalue != null && !svalue.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 5339 */         value = Byte.parseByte(svalue);
/*      */       }
/* 5341 */       catch (NumberFormatException nfe) {
/*      */         
/* 5343 */         if (this.errorText.isEmpty()) {
/* 5344 */           this.errorText = "Failed to parse value for " + key + ".";
/*      */         }
/*      */       }  
/* 5347 */     return value;
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
/*      */   private float getFloatProp(String key, float min, float max, boolean restrict) {
/* 5359 */     float value = 0.0F;
/* 5360 */     String svalue = getStringProp(key);
/* 5361 */     if (svalue != null && !svalue.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 5365 */         value = Float.parseFloat(svalue);
/* 5366 */         if (max > min && restrict)
/*      */         {
/* 5368 */           if (restrict)
/*      */           {
/* 5370 */             if (value > max)
/* 5371 */               value = max; 
/* 5372 */             if (value < min) {
/* 5373 */               value = min;
/*      */             
/*      */             }
/*      */           }
/* 5377 */           else if (value < min || value > max)
/*      */           {
/* 5379 */             if (this.errorText.isEmpty())
/* 5380 */               this.errorText = key + " not in required range " + min + "-" + max + "."; 
/* 5381 */             value = min;
/*      */           }
/*      */         
/*      */         }
/*      */       }
/* 5386 */       catch (NumberFormatException nfe) {
/*      */         
/* 5388 */         if (this.errorText.isEmpty()) {
/* 5389 */           this.errorText = "Failed to parse value for " + key + ".";
/*      */         }
/*      */       }  
/* 5392 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long getLongProp(String key) {
/* 5402 */     long value = 0L;
/* 5403 */     String svalue = getStringProp(key);
/* 5404 */     if (svalue != null && !svalue.isEmpty())
/*      */       
/*      */       try {
/*      */         
/* 5408 */         value = Long.parseLong(svalue);
/*      */       }
/* 5410 */       catch (NumberFormatException nfe) {
/*      */         
/* 5412 */         if (this.errorText.isEmpty()) {
/* 5413 */           this.errorText = "Failed to parse value for " + key + ".";
/*      */         }
/*      */       }  
/* 5416 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getStringProp(String key, int minLength) {
/* 5427 */     String value = getStringProp(key);
/* 5428 */     if (value == null || value.length() < minLength)
/*      */     {
/* 5430 */       if (this.errorText.isEmpty())
/* 5431 */         this.errorText = "Please select a name with at least " + minLength + " characters."; 
/*      */     }
/* 5433 */     if (value == null)
/* 5434 */       return ""; 
/* 5435 */     return value;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MissionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
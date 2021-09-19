/*      */ package com.wurmonline.server.epic;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.webinterface.WcCreateEpicMission;
/*      */ import com.wurmonline.server.webinterface.WcEpicStatusReport;
/*      */ import com.wurmonline.server.webinterface.WebCommand;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Random;
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
/*      */ public class EpicEntity
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*      */   private static final String CREATE_ENTITY = "INSERT INTO ENTITIES (ID,NAME,SPAWNPOINT,ENTITYTYPE,ATTACK,VITALITY,INATTACK,INVITALITY,CARRIER) VALUES (?,?,?,?,?,?,?,?,?)";
/*      */   private static final String CREATE_ENTITY_SKILLS = "INSERT INTO ENTITYSKILLS (ENTITYID,SKILLID,DEFAULTVAL,CURRENTVAL) VALUES (?,?,?,?)";
/*      */   private static final String UPDATE_ENTITY_SKILLS = "UPDATE ENTITYSKILLS SET DEFAULTVAL=?,CURRENTVAL=? WHERE ENTITYID=? AND SKILLID=?";
/*      */   private static final String UPDATE_ENTITY_COMPANION = "UPDATE ENTITIES SET COMPANION=? WHERE ID=?";
/*      */   private static final String UPDATE_ENTITY_DEMIGODPLUS = "UPDATE ENTITIES SET DEMIGODPLUS=? WHERE ID=?";
/*      */   private static final String UPDATE_ENTITY_CARRIER = "UPDATE ENTITIES SET CARRIER=? WHERE ID=?";
/*      */   private static final String UPDATE_ENTITY_POWERVIT = "UPDATE ENTITIES SET ATTACK=?,VITALITY=?,INATTACK=?,INVITALITY=? WHERE ID=?";
/*      */   private static final String UPDATE_ENTITY_HEX = "UPDATE ENTITIES SET CURRENTHEX=?,HELPED=?,ENTERED=?,LEAVING=?,TARGETHEX=? WHERE ID=?";
/*      */   private static final String UPDATE_ENTITY_TYPE = "UPDATE ENTITIES SET ENTITYTYPE=? WHERE ID=?";
/*      */   private static final String DELETE_ENTITY = "DELETE FROM ENTITIES WHERE ID=?";
/*   91 */   private static final Logger logger = Logger.getLogger(EpicEntity.class.getName());
/*      */ 
/*      */   
/*      */   static final int TYPE_DEITY = 0;
/*      */ 
/*      */   
/*      */   public static final int TYPE_SOURCE = 1;
/*      */ 
/*      */   
/*      */   public static final int TYPE_COLLECT = 2;
/*      */ 
/*      */   
/*      */   static final int TYPE_WURM = 4;
/*      */ 
/*      */   
/*      */   public static final int TYPE_MONSTER_SENTINEL = 5;
/*      */ 
/*      */   
/*      */   public static final int TYPE_ALLY = 6;
/*      */ 
/*      */   
/*      */   public static final int TYPE_DEMIGOD = 7;
/*      */ 
/*      */   
/*      */   static final long MIN_TIME_PER_HEX = 7200000L;
/*      */ 
/*      */   
/*      */   static final long MOVE_TIME_PER_HEX = 60000L;
/*      */ 
/*      */   
/*      */   static final long MIN_TIME_TRAPPED = 86400000L;
/*      */   
/*      */   static final long MAX_TIME_TRAPPED = 518400000L;
/*      */   
/*      */   private static final int HELPED_TIME_MODIFIER = 1;
/*      */   
/*      */   static final long MISSION_TIME_EFFECT = 43200000L;
/*      */   
/*      */   private static final int NOT_HELPED_TIME_MODIFIER = 12;
/*      */   
/*  131 */   private static final Random RAND = new Random();
/*      */   
/*      */   private boolean headingHome = false;
/*      */   
/*      */   private static final int DIEROLL = 20;
/*      */   
/*      */   private final String name;
/*      */   
/*      */   private final long identifier;
/*      */   
/*  141 */   private int type = 0;
/*      */   
/*      */   private boolean helped = false;
/*      */   
/*  145 */   private String collName = "";
/*      */ 
/*      */   
/*  148 */   private long enteredCurrentHex = 0L;
/*      */   
/*  150 */   private long timeUntilLeave = 0L;
/*      */   
/*      */   private boolean shouldCreateMission = false;
/*      */   
/*      */   private boolean succeedLastMission = false;
/*      */   
/*  156 */   private int targetHex = 0;
/*      */   
/*  158 */   private float attack = 0.0F;
/*      */   
/*  160 */   private float vitality = 0.0F;
/*      */   
/*  162 */   private float initialAttack = 0.0F;
/*      */   
/*  164 */   private float initialVitality = 0.0F;
/*      */   
/*  166 */   private MapHex hex = null;
/*      */   
/*  168 */   private HexMap myMap = null;
/*      */   
/*  170 */   private EpicEntity carrier = null;
/*      */   
/*  172 */   private EpicEntity companion = null;
/*      */   
/*  174 */   private int steps = 0;
/*      */   
/*  176 */   private byte demigodsToAppoint = 0;
/*      */ 
/*      */   
/*      */   private static final int TWELVE_HOURS = 43200000;
/*      */   
/*      */   private static final int TWENTY_HOURS = 72000000;
/*      */   
/*      */   private static final int LEAVE_TIME = 259200000;
/*      */   
/*  185 */   private long nextSpawnedCreatures = System.currentTimeMillis() + 43200000L + (new Random()).nextInt(43200000);
/*      */   
/*      */   private boolean dirtyVitality = false;
/*      */   
/*  189 */   private final List<EpicEntity> entities = new ArrayList<>();
/*      */   
/*      */   private static boolean dumpToXML = true;
/*      */   
/*  193 */   private long nextHeal = System.currentTimeMillis() + 3600000L;
/*      */   
/*      */   private WcCreateEpicMission lastSentWCC;
/*      */   
/*  197 */   private final Set<Integer> serversFailed = new HashSet<>();
/*      */   
/*  199 */   private int latestMissionDifficulty = -10;
/*      */   
/*  201 */   private HashMap<Integer, SkillVal> skills = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   EpicEntity(HexMap map, long id, String entityName, int entityType) {
/*  208 */     this.identifier = id;
/*  209 */     this.name = entityName;
/*  210 */     this.type = entityType;
/*  211 */     setHexMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   EpicEntity(HexMap map, long id, String entityName, int entityType, float entityInitialAttack, float entityInitialVitality) {
/*  220 */     this(map, id, entityName, entityType, entityInitialAttack, entityInitialVitality, false, 0L, System.currentTimeMillis() + 259200000L, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void toggleXmlDump(boolean dump) {
/*  226 */     dumpToXML = dump;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   EpicEntity(HexMap map, long id, String entityName, int entityType, float entityInitialAttack, float entityInitialVitality, boolean isHelped, long enterTime, long leaveTime, int targetH) {
/*  236 */     this.identifier = id;
/*  237 */     this.name = entityName;
/*  238 */     this.type = entityType;
/*  239 */     this.initialAttack = entityInitialAttack;
/*  240 */     this.attack = this.initialAttack;
/*  241 */     this.initialVitality = entityInitialVitality;
/*  242 */     this.vitality = this.initialVitality;
/*  243 */     this.helped = isHelped;
/*  244 */     this.enteredCurrentHex = enterTime;
/*  245 */     this.timeUntilLeave = leaveTime;
/*  246 */     this.targetHex = targetH;
/*  247 */     setHexMap(map);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLatestMissionDifficulty(int em) {
/*  252 */     this.latestMissionDifficulty = em;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLatestMissionDifficulty() {
/*  257 */     return this.latestMissionDifficulty;
/*      */   }
/*      */ 
/*      */   
/*      */   void setHexMap(HexMap newMap) {
/*  262 */     if (this.myMap != null)
/*  263 */       this.myMap.removeEntity(this); 
/*  264 */     this.myMap = newMap;
/*  265 */     if (this.myMap != null) {
/*  266 */       this.myMap.addEntity(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final long getId() {
/*  271 */     return this.identifier;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/*  276 */     return this.name;
/*      */   }
/*      */ 
/*      */   
/*      */   void setType(int newType) {
/*  281 */     this.type = newType;
/*  282 */     Connection dbcon = null;
/*  283 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  286 */       dbcon = DbConnector.getDeityDbCon();
/*  287 */       ps = dbcon.prepareStatement("UPDATE ENTITIES SET ENTITYTYPE=? WHERE ID=?");
/*  288 */       ps.setInt(1, this.type);
/*  289 */       ps.setLong(2, getId());
/*  290 */       ps.executeUpdate();
/*      */     }
/*  292 */     catch (SQLException sqx) {
/*      */       
/*  294 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  298 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  299 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   EpicEntity getDemiGod() {
/*  305 */     return this.myMap.getDemiGodFor(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getType() {
/*  310 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isDeity() {
/*  315 */     return (this.type == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getCollectibleName() {
/*  320 */     return this.collName;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isDemigod() {
/*  325 */     return (this.type == 7);
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isSentinelMonster() {
/*  330 */     return (this.type == 5);
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isWurm() {
/*  335 */     return (this.type == 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isCollectable() {
/*  340 */     return (this.type == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isAlly() {
/*  345 */     return (this.type == 6);
/*      */   }
/*      */ 
/*      */   
/*      */   final void setCompanion(EpicEntity entity) {
/*  350 */     setCompanion(entity, false);
/*      */   }
/*      */ 
/*      */   
/*      */   final void setCompanion(EpicEntity entity, boolean load) {
/*  355 */     if (this.companion != null)
/*  356 */       logger.log(Level.WARNING, getName() + " replacing " + this.companion.getName() + " with " + entity.getName()); 
/*  357 */     this.companion = entity;
/*  358 */     if (!load) {
/*  359 */       setCompanionForEntity((this.companion == null) ? 0L : this.companion.getId());
/*      */     }
/*      */   }
/*      */   
/*      */   public final void addFailedServer(int serverId) {
/*  364 */     this.serversFailed.add(Integer.valueOf(serverId));
/*  365 */     logger.log(Level.INFO, getName() + " adding failed server for epic mission creation command.");
/*      */   }
/*      */ 
/*      */   
/*      */   public final void checkifServerFailed(int serverId) {
/*  370 */     if (this.lastSentWCC == null)
/*      */       return; 
/*  372 */     boolean remove = false;
/*  373 */     for (Integer id : this.serversFailed) {
/*      */       
/*  375 */       if (id == Integer.valueOf(serverId))
/*      */       {
/*  377 */         if (this.lastSentWCC != null) {
/*      */           
/*  379 */           LoginServerWebConnection lsw = new LoginServerWebConnection(serverId);
/*  380 */           lsw.sendWebCommand(this.lastSentWCC.getType(), (WebCommand)this.lastSentWCC);
/*  381 */           logger.log(Level.INFO, getName() + " ... Server " + serverId + " has reconnected. Resent WCC!");
/*  382 */           remove = true;
/*      */         } 
/*      */       }
/*      */     } 
/*  386 */     if (remove)
/*  387 */       this.serversFailed.remove(Integer.valueOf(serverId)); 
/*  388 */     if (this.serversFailed.isEmpty()) {
/*  389 */       this.lastSentWCC = null;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void sendNewScenarioWebCommand(int difficulty) {
/*  394 */     if (this.myMap != null && isDeity()) {
/*      */       
/*  396 */       EpicMission oldmission = EpicServerStatus.getEpicMissionForEntity((int)getId());
/*  397 */       if (oldmission != null)
/*      */       {
/*  399 */         EpicServerStatus.deleteMission(oldmission);
/*      */       }
/*  401 */       int numberOfLoyalServers = Servers.getNumberOfLoyalServers((int)getId());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  407 */       if (!Servers.localServer.EPIC) {
/*      */ 
/*      */ 
/*      */         
/*  411 */         EpicMission mission = new EpicMission((int)getId(), this.myMap.getScenarioNumber(), getName() + " waiting for help", this.myMap.getScenarioName(), (int)getId(), (byte)-10, difficulty, 0.0F, numberOfLoyalServers, System.currentTimeMillis(), false, true);
/*  412 */         EpicServerStatus.addMission(mission);
/*  413 */         mission.setCurrent(true);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  420 */       WcCreateEpicMission wce = new WcCreateEpicMission(WurmId.getNextWCCommandId(), this.myMap.getScenarioName(), this.myMap.getScenarioNumber(), this.myMap.getReasonAndEffectInt(), this.myMap.getCollictblesRequiredToWin(), this.myMap.getCollictblesRequiredForWurmToWin(), this.myMap.isSpawnPointRequiredToWin(), this.myMap.getHexNumRequiredToWin(), this.myMap.getScenarioQuestString() + ' ' + getLocationStatus() + ' ' + getEnemyStatus(), getId(), difficulty, getName(), (getTimeUntilLeave() - System.currentTimeMillis()) / 1000L, false);
/*  421 */       this.lastSentWCC = wce;
/*  422 */       wce.sendFromLoginServer();
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
/*      */   final void setDemigodsToAppoint(byte aDemigodsToAppoint) {
/*  434 */     this.demigodsToAppoint = aDemigodsToAppoint;
/*      */   }
/*      */ 
/*      */   
/*      */   void setMapHex(MapHex mapHex) {
/*  439 */     if (mapHex != null)
/*  440 */       broadCastWithName(" enters " + mapHex.getName()); 
/*  441 */     setMapHex(mapHex, false);
/*      */   }
/*      */ 
/*      */   
/*      */   int resetSteps() {
/*  446 */     int toReturn = this.steps;
/*  447 */     this.steps = 0;
/*  448 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setMapHex(MapHex mapHex, boolean load) {
/*  453 */     if (mapHex != null && !mapHex.equals(this.hex)) {
/*      */       
/*  455 */       if (this.hex != null)
/*      */       {
/*  457 */         this.hex.removeEntity(this, load);
/*      */       }
/*  459 */       this.hex = mapHex;
/*  460 */       this.steps++;
/*  461 */       setHelped(false, load);
/*  462 */       toggleXmlDump(false);
/*  463 */       this.hex.addEntity(this);
/*  464 */       if (!load)
/*  465 */         setEnteredCurrentHex(); 
/*  466 */       toggleXmlDump(true);
/*      */     }
/*  468 */     else if (mapHex == null) {
/*      */       
/*  470 */       if (this.hex != null) {
/*      */         
/*  472 */         toggleXmlDump(false);
/*  473 */         this.hex.removeEntity(this, load);
/*  474 */         toggleXmlDump(true);
/*      */       } 
/*  476 */       this.hex = null;
/*  477 */       saveHexPos();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setHelped(boolean isHelped, boolean load) {
/*  488 */     this.helped = isHelped;
/*  489 */     if (!load)
/*      */     {
/*  491 */       saveHexPos();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final float getHelpModifier() {
/*  502 */     if (isDeity()) {
/*      */       
/*  504 */       if (this.helped) {
/*  505 */         return 1.0F;
/*      */       }
/*  507 */       return 12.0F;
/*      */     } 
/*  509 */     return 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getTimeUntilLeave() {
/*  514 */     if (this.hex != null)
/*      */     {
/*  516 */       return this.timeUntilLeave;
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
/*  531 */     return getMinTimePerHex();
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getTimeToNextHex() {
/*  536 */     if (this.targetHex > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  541 */       MapHex next = this.myMap.getMapHex(this.targetHex);
/*  542 */       if (next != null)
/*      */       {
/*  544 */         return (long)((float)getTimeUntilLeave() + 60000.0F * next.getMoveCost());
/*      */       }
/*      */     } 
/*  547 */     return getTimeUntilLeave();
/*      */   }
/*      */ 
/*      */   
/*      */   final void poll() {
/*  552 */     if (this.hex != null) {
/*      */       
/*  554 */       if (this.targetHex > 0)
/*      */       {
/*  556 */         if (System.currentTimeMillis() > getTimeUntilLeave()) {
/*      */           
/*  558 */           MapHex next = this.myMap.getMapHex(this.targetHex);
/*  559 */           if (next != null && System.currentTimeMillis() > getTimeToNextHex())
/*      */           {
/*  561 */             if (this.hex.checkLeaveStatus(this))
/*      */             {
/*  563 */               if (this.hex.isTeleport()) {
/*      */                 
/*  565 */                 next = this.myMap.getRandomHex();
/*  566 */                 while (!next.mayEnter(this))
/*      */                 {
/*  568 */                   next = this.myMap.getRandomHex();
/*      */                 }
/*  570 */                 this.targetHex = 0;
/*  571 */                 broadCastWithName(" shifts to " + next.getName() + ".");
/*      */               } 
/*  573 */               setMapHex(next);
/*      */             }
/*      */           
/*      */           }
/*      */         } 
/*      */       }
/*  579 */     } else if (!isCollectable() && !isSource()) {
/*  580 */       spawn();
/*  581 */     }  if (!isCollectable() && !isSource() && System.currentTimeMillis() > this.nextHeal)
/*      */     {
/*  583 */       if (getVitality() < getInitialVitality()) {
/*      */         
/*  585 */         setVitality(Math.min(getInitialVitality(), getVitality() + 1.0F));
/*  586 */         this.nextHeal = System.currentTimeMillis() + 72000000L;
/*      */       } 
/*      */     }
/*  589 */     if (isDeity() || isWurm()) {
/*  590 */       findNextTargetHex();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  596 */     if ((isDeity() || isWurm()) && System.currentTimeMillis() > this.nextSpawnedCreatures) {
/*      */       
/*  598 */       int next = 72000000;
/*  599 */       if (this.myMap.spawnCreatures(this))
/*  600 */         next = 144000000; 
/*  601 */       this.nextSpawnedCreatures = System.currentTimeMillis() + 72000000L + (new Random()).nextInt(next);
/*  602 */       logger.log(Level.INFO, getName() + " spawns creatures. Next in " + 
/*  603 */           Server.getTimeFor(this.nextSpawnedCreatures - System.currentTimeMillis()));
/*      */     } 
/*  605 */     if (this.dirtyVitality) {
/*      */       
/*  607 */       updateEntityVitality();
/*  608 */       this.dirtyVitality = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean setVitality(float newVitality) {
/*  614 */     return setVitality(newVitality, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final long getMinTimePerHex() {
/*  619 */     return 7200000L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean setVitality(float newVitality, boolean load) {
/*  629 */     if (this.initialVitality == 0.0F)
/*  630 */       this.initialVitality = newVitality; 
/*  631 */     this.vitality = newVitality;
/*  632 */     if (!load && this.vitality > 0.0F)
/*  633 */       this.dirtyVitality = true; 
/*  634 */     return (this.vitality <= 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   final void permanentlyModifyVitality(float modifierVal) {
/*  639 */     this.vitality += modifierVal;
/*  640 */     updateEntityVitality();
/*      */   }
/*      */ 
/*      */   
/*      */   final void permanentlyModifyAttack(float modifierVal) {
/*  645 */     this.attack += modifierVal;
/*  646 */     updateEntityVitality();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSource() {
/*  651 */     return (this.type == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getVitality() {
/*  656 */     return this.vitality;
/*      */   }
/*      */ 
/*      */   
/*      */   final float getInitialVitality() {
/*  661 */     return this.initialVitality;
/*      */   }
/*      */ 
/*      */   
/*      */   final float getInitialAttack() {
/*  666 */     return this.initialAttack;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isFriend(EpicEntity other) {
/*  671 */     if (other != null) {
/*      */       
/*  673 */       if (other.equals(this.companion)) {
/*  674 */         return true;
/*      */       }
/*  676 */       if (other.getCompanion() != null && other.getCompanion() != this && other.getCompanion().isCompanion(this))
/*  677 */         return true; 
/*  678 */       return other.isCompanion(this);
/*      */     } 
/*      */ 
/*      */     
/*  682 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isEnemy(EpicEntity other) {
/*  688 */     if (other == this) {
/*  689 */       return false;
/*      */     }
/*  691 */     if (isFriend(other))
/*  692 */       return false; 
/*  693 */     if (other.isFriend(this))
/*  694 */       return false; 
/*  695 */     if (other.isSentinelMonster())
/*  696 */       return !other.isWurm(); 
/*  697 */     if (other.isWurm())
/*  698 */       return (isDeity() || isAlly() || isDemigod()); 
/*  699 */     if (other.isDeity() || other.isDemigod())
/*      */     {
/*  701 */       if (isDeity() || isWurm() || isSentinelMonster() || isDemigod())
/*  702 */         return true; 
/*      */     }
/*  704 */     return other.isCompanion(this);
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean rollAttack() {
/*  709 */     int bonus = 0;
/*  710 */     for (EpicEntity e : this.entities) {
/*      */       
/*  712 */       if (e.isSource())
/*  713 */         bonus++; 
/*      */     } 
/*  715 */     if (this.hex != null) {
/*      */       
/*  717 */       if (this.hex.isHomeFor(this.identifier))
/*  718 */         bonus++; 
/*  719 */       if (this.hex.isSpawnFor(getId()))
/*  720 */         bonus++; 
/*      */     } 
/*  722 */     if (this.helped)
/*  723 */       bonus++; 
/*  724 */     return (RAND.nextInt(20) < Math.min(18.0F, this.attack + bonus));
/*      */   }
/*      */ 
/*      */   
/*      */   final EpicEntity getCompanion() {
/*  729 */     return this.companion;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isCompanion(EpicEntity entity) {
/*  734 */     return (entity != null && entity.equals(this.companion));
/*      */   }
/*      */ 
/*      */   
/*      */   final void setAttack(float newAttack) {
/*  739 */     setAttack(newAttack, false);
/*      */   }
/*      */ 
/*      */   
/*      */   final void setAttack(float newAttack, boolean load) {
/*  744 */     if (this.initialAttack == 0.0F)
/*  745 */       this.initialAttack = newAttack; 
/*  746 */     this.attack = Math.min(18.0F, Math.max(this.initialAttack, newAttack));
/*  747 */     if (!load) {
/*  748 */       updateEntityVitality();
/*      */     }
/*      */   }
/*      */   
/*      */   public final float getAttack() {
/*  753 */     return this.attack;
/*      */   }
/*      */ 
/*      */   
/*      */   final void spawn() {
/*  758 */     this.headingHome = false;
/*  759 */     this.carrier = null;
/*  760 */     this.vitality = this.initialVitality;
/*  761 */     this.attack = this.initialAttack;
/*  762 */     updateEntityVitality();
/*  763 */     this.targetHex = 0;
/*  764 */     this.helped = false;
/*  765 */     resetSteps();
/*  766 */     if (this.myMap != null) {
/*      */       
/*  768 */       MapHex mh = this.myMap.getSpawnHex(this);
/*  769 */       if (mh != null) {
/*      */         
/*  771 */         if (!mh.containsEnemy(this)) {
/*  772 */           mh.addEntity(this);
/*      */         }
/*      */       } else {
/*  775 */         saveHexPos();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   final EpicEntity getCarrier() {
/*  781 */     return this.carrier;
/*      */   }
/*      */ 
/*      */   
/*      */   int getHexNumRequiredToWin() {
/*  786 */     return this.myMap.getHexNumRequiredToWin();
/*      */   }
/*      */ 
/*      */   
/*      */   boolean mustReturnHomeToWin() {
/*  791 */     return this.myMap.isSpawnPointRequiredToWin();
/*      */   }
/*      */ 
/*      */   
/*      */   boolean hasEnoughCollectablesToWin() {
/*  796 */     if (isWurm())
/*  797 */       return (countCollectables() >= this.myMap.getCollictblesRequiredForWurmToWin()); 
/*  798 */     if (isDeity())
/*  799 */       return (countCollectables() >= this.myMap.getCollictblesRequiredToWin()); 
/*  800 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void findNextTargetHex() {
/*  805 */     if (this.hex != null)
/*      */     {
/*  807 */       if (this.targetHex == this.hex.getId() || this.targetHex <= 0) {
/*  808 */         setNextTargetHex(this.hex.findNextHex(this));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public final void setNextTargetHex(int target) {
/*  814 */     if (target > 0) {
/*  815 */       logger.log(Level.INFO, getName() + " set target hex to " + this.myMap.getMapHex(target).getName());
/*      */     } else {
/*  817 */       logger.log(Level.INFO, getName() + " set target hex to 0.");
/*  818 */     }  this.targetHex = target;
/*  819 */     saveHexPos();
/*  820 */     sendEntityData();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getTargetHex() {
/*  825 */     return this.targetHex;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getEnteredCurrentHexTime() {
/*  830 */     return this.enteredCurrentHex;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void setEnteredCurrentHex() {
/*  835 */     this.enteredCurrentHex = System.currentTimeMillis();
/*  836 */     if (isWurm()) {
/*  837 */       this.timeUntilLeave = System.currentTimeMillis() + 86400000L;
/*      */     } else {
/*  839 */       this.timeUntilLeave = System.currentTimeMillis() + 259200000L;
/*  840 */     }  if (this.hex != null) {
/*      */       
/*  842 */       if (this.hex.isTrap())
/*      */       {
/*  844 */         if (isWurm()) {
/*  845 */           this.timeUntilLeave += 86400000L;
/*      */         } else {
/*  847 */           this.timeUntilLeave += 259200000L;
/*      */         }  } 
/*  849 */       if (this.hex.isSlow())
/*      */       {
/*  851 */         if (isWurm()) {
/*  852 */           this.timeUntilLeave += 43200000L;
/*      */         } else {
/*  854 */           this.timeUntilLeave += 86400000L;
/*      */         }  } 
/*      */     } 
/*  857 */     setShouldCreateMission(true, true);
/*  858 */     saveHexPos();
/*      */   }
/*      */ 
/*      */   
/*      */   public long modifyTimeToLeave(long timeChanged) {
/*  863 */     this.timeUntilLeave += timeChanged;
/*  864 */     return this.timeUntilLeave;
/*      */   }
/*      */ 
/*      */   
/*      */   public MapHex getMapHex() {
/*  869 */     return this.hex;
/*      */   }
/*      */ 
/*      */   
/*      */   void setCarrier(EpicEntity entity, boolean setReverse, boolean load, boolean log) {
/*  874 */     if (setReverse) {
/*      */       
/*  876 */       if (entity != null)
/*  877 */         entity.addEntity(this, log, true); 
/*  878 */       if (this.carrier != null)
/*  879 */         this.carrier.removeEntity(this, log); 
/*      */     } 
/*  881 */     this.carrier = entity;
/*  882 */     if (!load) {
/*  883 */       saveCarrierForEntity();
/*      */     }
/*      */   }
/*      */   
/*      */   private final void addEntity(EpicEntity entity, boolean log, boolean receives) {
/*  888 */     if (!this.entities.contains(entity)) {
/*      */       
/*  890 */       this.entities.add(entity);
/*  891 */       if (log)
/*      */       {
/*  893 */         if (receives) {
/*  894 */           logWithName(" receives " + entity.getName());
/*      */         } else {
/*  896 */           logWithName(" finds " + entity.getName());
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private final void removeEntity(EpicEntity entity, boolean log) {
/*  903 */     if (this.entities.contains(entity)) {
/*      */       
/*  905 */       this.entities.remove(entity);
/*  906 */       if (log) {
/*  907 */         logWithName(" drops " + entity.getName());
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
/*      */   final void dropAll(boolean killedByDemigod) {
/*  919 */     toggleXmlDump(false);
/*  920 */     if (!this.entities.isEmpty())
/*      */     {
/*  922 */       for (ListIterator<EpicEntity> lit = this.entities.listIterator(); lit.hasNext(); ) {
/*      */         
/*  924 */         EpicEntity next = lit.next();
/*  925 */         lit.remove();
/*  926 */         next.setCarrier(null, false, false, true);
/*  927 */         if (killedByDemigod) {
/*  928 */           next.setMapHex(this.myMap.getRandomHex()); continue;
/*      */         } 
/*  930 */         next.setMapHex(this.hex);
/*      */       } 
/*      */     }
/*  933 */     toggleXmlDump(true);
/*      */   }
/*      */ 
/*      */   
/*      */   void setHeadingHome(boolean headingHomeToSet) {
/*  938 */     this.headingHome = headingHomeToSet;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean isHeadingHome() {
/*  943 */     return this.headingHome;
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadCastWithName(String toBroadCast) {
/*  948 */     if (this.myMap != null) {
/*  949 */       this.myMap.broadCast(this.name + toBroadCast);
/*      */     }
/*      */   }
/*      */   
/*      */   void broadCast(String toBroadCast) {
/*  954 */     if (this.myMap != null) {
/*  955 */       this.myMap.broadCast(toBroadCast);
/*      */     }
/*      */   }
/*      */   
/*      */   void log(String toLog) {
/*  960 */     logger.log(Level.INFO, toLog);
/*      */   }
/*      */ 
/*      */   
/*      */   void logWithName(String toLog) {
/*  965 */     logger.log(Level.INFO, this.name + toLog);
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getLocationStatus() {
/*  970 */     if (this.hex != null) {
/*      */       
/*  972 */       if (isCollectable() || isSource())
/*  973 */         return this.name + " is" + this.hex.getPrepositionString() + this.hex.getName() + "."; 
/*  974 */       String prep = this.name + this.hex.getFullPresenceString();
/*  975 */       if (this.hex.getSpawnEntityId() == getId())
/*  976 */         prep = this.name + this.hex.getOwnPresenceString(); 
/*  977 */       if (this.myMap != null && this.targetHex > 0)
/*      */       {
/*      */         
/*  980 */         prep = prep + " Heading to " + this.myMap.getMapHex(this.targetHex).getName() + " leaving in " + Server.getTimeFor(getTimeUntilLeave() - System.currentTimeMillis()) + " time to next=" + Server.getTimeFor(getTimeToNextHex() - System.currentTimeMillis());
/*      */       }
/*  982 */       return prep;
/*      */     } 
/*  984 */     return this.name + " is in an unknown location.";
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getEnemyStatus() {
/*  989 */     if (this.hex != null) {
/*      */       
/*  991 */       String prep = this.hex.getEnemyStatus(this);
/*  992 */       if (prep != null && prep.length() > 0)
/*  993 */         logger.log(Level.INFO, prep); 
/*  994 */       return prep;
/*      */     } 
/*  996 */     return this.name + " is in an unknown location.";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int countCollectables() {
/* 1004 */     int numColl = 0;
/* 1005 */     for (EpicEntity e : this.entities) {
/*      */       
/* 1007 */       if (e.isCollectable()) {
/*      */ 
/*      */ 
/*      */         
/* 1011 */         this.collName = e.getName();
/* 1012 */         numColl++;
/*      */       } 
/*      */     } 
/* 1015 */     return numColl;
/*      */   }
/*      */ 
/*      */   
/*      */   public final List<EpicEntity> getAllCollectedItems() {
/* 1020 */     return this.entities;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void giveCollectables(EpicEntity receiver) {
/* 1028 */     Set<EpicEntity> collsToGive = new HashSet<>();
/* 1029 */     for (EpicEntity e : this.entities) {
/*      */       
/* 1031 */       if (e.isCollectable())
/*      */       {
/* 1033 */         collsToGive.add(e);
/*      */       }
/*      */     } 
/* 1036 */     for (EpicEntity e : collsToGive) {
/*      */       
/* 1038 */       if (e.isCollectable())
/*      */       {
/* 1040 */         e.setCarrier(receiver, true, false, true);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean checkWinCondition() {
/* 1048 */     if (isDeity() || isWurm())
/*      */     {
/* 1050 */       if (this.hex != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1055 */         int numColl = countCollectables();
/* 1056 */         numColl += this.hex.countCollectibles();
/* 1057 */         if (this.steps > 0) {
/*      */           
/* 1059 */           if (this.hex.containsEnemy(this)) {
/*      */             
/* 1061 */             if (isShouldCreateMission())
/*      */             {
/* 1063 */               sendNewScenarioWebCommand(succeededLastMission() ? -2 : -3);
/*      */             }
/* 1065 */             setShouldCreateMission(false, false);
/* 1066 */             return false;
/*      */           } 
/* 1068 */           boolean win = this.myMap.winCondition(isWurm(), numColl, this.hex.isSpawnFor(getId()), this.hex.getId());
/* 1069 */           if (win) {
/*      */             
/* 1071 */             this.myMap.win(this, this.collName, numColl);
/*      */             
/* 1073 */             sendNewScenarioWebCommand(1);
/*      */           }
/* 1075 */           else if (isShouldCreateMission()) {
/*      */             
/* 1077 */             sendNewScenarioWebCommand(succeededLastMission() ? -2 : -3);
/*      */           } 
/* 1079 */           setShouldCreateMission(false, false);
/* 1080 */           return win;
/*      */         } 
/* 1082 */         if (isShouldCreateMission())
/*      */         {
/* 1084 */           sendNewScenarioWebCommand(succeededLastMission() ? -2 : -3);
/*      */         }
/* 1086 */         setShouldCreateMission(false, false);
/*      */       } 
/*      */     }
/* 1089 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final void createEntity(int spawn) {
/* 1094 */     if (spawn > 0)
/*      */     {
/* 1096 */       if (this.type != 2 && this.type != 1) {
/*      */         
/* 1098 */         MapHex mh = this.myMap.getMapHex(spawn);
/* 1099 */         if (!mh.isSpawnFor(this.identifier))
/*      */         {
/* 1101 */           if (!mh.isSpawn())
/* 1102 */             mh.setSpawnEntityId(this.identifier); 
/*      */         }
/*      */       } 
/*      */     }
/* 1106 */     Connection dbcon = null;
/* 1107 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1110 */       dbcon = DbConnector.getDeityDbCon();
/* 1111 */       ps = dbcon.prepareStatement("INSERT INTO ENTITIES (ID,NAME,SPAWNPOINT,ENTITYTYPE,ATTACK,VITALITY,INATTACK,INVITALITY,CARRIER) VALUES (?,?,?,?,?,?,?,?,?)");
/* 1112 */       ps.setLong(1, this.identifier);
/* 1113 */       ps.setString(2, this.name);
/* 1114 */       ps.setInt(3, spawn);
/* 1115 */       ps.setInt(4, this.type);
/* 1116 */       ps.setFloat(5, this.attack);
/* 1117 */       ps.setFloat(6, this.vitality);
/* 1118 */       ps.setFloat(7, this.attack);
/* 1119 */       ps.setFloat(8, this.vitality);
/* 1120 */       if (this.carrier != null) {
/* 1121 */         ps.setLong(9, this.carrier.getId());
/*      */       } else {
/* 1123 */         ps.setLong(9, 0L);
/* 1124 */       }  ps.executeUpdate();
/*      */     }
/* 1126 */     catch (SQLException sqx) {
/*      */       
/* 1128 */       logger.log(Level.WARNING, "Problem creating an Epic Entity for spawn: " + spawn + " due to " + sqx.getMessage(), sqx);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1133 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1134 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void createAndSaveSkills() {
/* 1140 */     if (this.skills.isEmpty()) {
/*      */       
/* 1142 */       logger.log(Level.WARNING, "Error creating skills for epic entity " + getName() + ". No default skills exist for this entity.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1147 */     Connection dbcon = null;
/* 1148 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1151 */       dbcon = DbConnector.getDeityDbCon();
/* 1152 */       for (Iterator<Integer> iterator = this.skills.keySet().iterator(); iterator.hasNext(); ) { int skillId = ((Integer)iterator.next()).intValue();
/*      */         
/* 1154 */         ps = dbcon.prepareStatement("INSERT INTO ENTITYSKILLS (ENTITYID,SKILLID,DEFAULTVAL,CURRENTVAL) VALUES (?,?,?,?)");
/* 1155 */         ps.setLong(1, this.identifier);
/* 1156 */         ps.setInt(2, skillId);
/* 1157 */         ps.setFloat(3, ((SkillVal)this.skills.get(Integer.valueOf(skillId))).getDefaultVal());
/* 1158 */         ps.setFloat(4, ((SkillVal)this.skills.get(Integer.valueOf(skillId))).getCurrentVal());
/* 1159 */         ps.executeUpdate(); }
/*      */ 
/*      */     
/* 1162 */     } catch (SQLException sqx) {
/*      */       
/* 1164 */       logger.log(Level.WARNING, "Problem creating an epic entity skill due to " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1168 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1169 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void updateSkills() {
/* 1175 */     if (this.skills.isEmpty()) {
/*      */       
/* 1177 */       logger.log(Level.WARNING, "Error updating skills for epic entity " + getName() + ". No skills exist for this entity.");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1182 */     Connection dbcon = null;
/* 1183 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1186 */       dbcon = DbConnector.getDeityDbCon();
/* 1187 */       for (Iterator<Integer> iterator = this.skills.keySet().iterator(); iterator.hasNext(); ) { int skillId = ((Integer)iterator.next()).intValue();
/*      */         
/* 1189 */         ps = dbcon.prepareStatement("UPDATE ENTITYSKILLS SET DEFAULTVAL=?,CURRENTVAL=? WHERE ENTITYID=? AND SKILLID=?");
/* 1190 */         ps.setFloat(1, ((SkillVal)this.skills.get(Integer.valueOf(skillId))).getDefaultVal());
/* 1191 */         ps.setFloat(2, ((SkillVal)this.skills.get(Integer.valueOf(skillId))).getCurrentVal());
/* 1192 */         ps.setLong(3, this.identifier);
/* 1193 */         ps.setInt(4, skillId);
/* 1194 */         ps.executeUpdate(); }
/*      */ 
/*      */     
/* 1197 */     } catch (SQLException sqx) {
/*      */       
/* 1199 */       logger.log(Level.WARNING, "Problem updating an epic entity skill due to " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1203 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1204 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void updateEntityVitality() {
/* 1210 */     Connection dbcon = null;
/* 1211 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1214 */       dbcon = DbConnector.getDeityDbCon();
/* 1215 */       ps = dbcon.prepareStatement("UPDATE ENTITIES SET ATTACK=?,VITALITY=?,INATTACK=?,INVITALITY=? WHERE ID=?");
/* 1216 */       ps.setFloat(1, this.attack);
/* 1217 */       ps.setFloat(2, this.vitality);
/* 1218 */       ps.setFloat(3, this.initialAttack);
/* 1219 */       ps.setFloat(4, this.initialVitality);
/* 1220 */       ps.setLong(5, this.identifier);
/* 1221 */       ps.executeUpdate();
/*      */     }
/* 1223 */     catch (SQLException sqx) {
/*      */       
/* 1225 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1229 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1230 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void sendEntityData() {
/* 1237 */     if (this.myMap != null)
/*      */     {
/* 1239 */       if (dumpToXML) {
/*      */         
/* 1241 */         EpicXmlWriter.dumpEntities(this.myMap);
/* 1242 */         WcEpicStatusReport report = new WcEpicStatusReport(WurmId.getNextWCCommandId(), false, 0, (byte)-1, -1);
/* 1243 */         report.fillStatusReport(this.myMap);
/* 1244 */         report.sendFromLoginServer();
/*      */         
/* 1246 */         if (Features.Feature.VALREI_MAP.isEnabled())
/*      */         {
/* 1248 */           ValreiMapData.updateFromEpicEntity(this);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final void saveHexPos() {
/* 1256 */     Connection dbcon = null;
/* 1257 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1260 */       dbcon = DbConnector.getDeityDbCon();
/* 1261 */       ps = dbcon.prepareStatement("UPDATE ENTITIES SET CURRENTHEX=?,HELPED=?,ENTERED=?,LEAVING=?,TARGETHEX=? WHERE ID=?");
/* 1262 */       if (this.hex != null) {
/* 1263 */         ps.setInt(1, this.hex.getId());
/*      */       } else {
/* 1265 */         ps.setInt(1, -1);
/* 1266 */       }  ps.setBoolean(2, this.helped);
/* 1267 */       ps.setLong(3, this.enteredCurrentHex);
/* 1268 */       ps.setLong(4, this.timeUntilLeave);
/* 1269 */       ps.setInt(5, this.targetHex);
/* 1270 */       ps.setLong(6, getId());
/* 1271 */       ps.executeUpdate();
/*      */     }
/* 1273 */     catch (SQLException sqx) {
/*      */       
/* 1275 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1279 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1280 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1282 */     sendEntityData();
/*      */   }
/*      */ 
/*      */   
/*      */   final void deleteEntity() {
/* 1287 */     Connection dbcon = null;
/* 1288 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1291 */       dbcon = DbConnector.getDeityDbCon();
/* 1292 */       ps = dbcon.prepareStatement("DELETE FROM ENTITIES WHERE ID=?");
/* 1293 */       ps.setLong(1, getId());
/* 1294 */       ps.executeUpdate();
/*      */     }
/* 1296 */     catch (SQLException sqx) {
/*      */       
/* 1298 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1302 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1303 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void setCompanionForEntity(long companionId) {
/* 1309 */     Connection dbcon = null;
/* 1310 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1313 */       dbcon = DbConnector.getDeityDbCon();
/* 1314 */       ps = dbcon.prepareStatement("UPDATE ENTITIES SET COMPANION=? WHERE ID=?");
/* 1315 */       ps.setLong(1, companionId);
/* 1316 */       ps.setLong(2, this.identifier);
/* 1317 */       ps.executeUpdate();
/*      */     }
/* 1319 */     catch (SQLException sqx) {
/*      */       
/* 1321 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1325 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1326 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final byte getDemigodsToAppoint() {
/* 1333 */     return this.demigodsToAppoint;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setDemigodPlusForEntity(byte numsToAppoint) {
/* 1338 */     this.demigodsToAppoint = numsToAppoint;
/* 1339 */     Connection dbcon = null;
/* 1340 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1343 */       dbcon = DbConnector.getDeityDbCon();
/* 1344 */       ps = dbcon.prepareStatement("UPDATE ENTITIES SET DEMIGODPLUS=? WHERE ID=?");
/* 1345 */       ps.setByte(1, numsToAppoint);
/* 1346 */       ps.setLong(2, this.identifier);
/* 1347 */       ps.executeUpdate();
/*      */     }
/* 1349 */     catch (SQLException sqx) {
/*      */       
/* 1351 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1355 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1356 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void saveCarrierForEntity() {
/* 1362 */     Connection dbcon = null;
/* 1363 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1366 */       dbcon = DbConnector.getDeityDbCon();
/* 1367 */       ps = dbcon.prepareStatement("UPDATE ENTITIES SET CARRIER=? WHERE ID=?");
/* 1368 */       ps.setLong(1, (this.carrier == null) ? 0L : this.carrier.getId());
/* 1369 */       ps.setLong(2, getId());
/* 1370 */       ps.executeUpdate();
/*      */     }
/* 1372 */     catch (SQLException sqx) {
/*      */       
/* 1374 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1378 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1379 */       DbConnector.returnConnection(dbcon);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isShouldCreateMission() {
/* 1416 */     return this.shouldCreateMission;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShouldCreateMission(boolean aShouldCreateMission, boolean lastMissionSuccess) {
/* 1427 */     this.shouldCreateMission = aShouldCreateMission;
/* 1428 */     this.succeedLastMission = lastMissionSuccess;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean succeededLastMission() {
/* 1433 */     return this.succeedLastMission;
/*      */   }
/*      */   
/*      */   public boolean isPlayerGod() {
/* 1437 */     if (isDeity() && this.identifier > 100L) {
/* 1438 */       return true;
/*      */     }
/* 1440 */     return false;
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
/*      */   public boolean setSkill(int skillId, float newCurrentVal) {
/* 1453 */     if (this.skills.containsKey(Integer.valueOf(skillId))) {
/*      */       
/* 1455 */       ((SkillVal)this.skills.get(Integer.valueOf(skillId))).setCurrentVal(newCurrentVal);
/* 1456 */       updateSkills();
/* 1457 */       return true;
/*      */     } 
/* 1459 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSkill(int skillId, float skillVal) {
/* 1469 */     setSkill(skillId, skillVal, skillVal);
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
/*      */   public void setSkill(int skillId, float defaultVal, float currentVal) {
/* 1481 */     if (!this.skills.containsKey(Integer.valueOf(skillId))) {
/* 1482 */       this.skills.put(Integer.valueOf(skillId), new SkillVal(defaultVal, currentVal));
/*      */     } else {
/*      */       
/* 1485 */       SkillVal existing = this.skills.get(Integer.valueOf(skillId));
/* 1486 */       existing.setDefaultVal(defaultVal);
/* 1487 */       existing.setCurrentVal(currentVal);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void increaseRandomSkill(float skillDivider) {
/* 1493 */     int randomSkill = 100 + Server.rand.nextInt(7);
/* 1494 */     float currentSkill = getCurrentSkill(randomSkill);
/*      */     
/* 1496 */     setSkill(randomSkill, currentSkill + (100.0F - currentSkill) / skillDivider);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SkillVal getSkill(int skillId) {
/* 1506 */     return this.skills.get(Integer.valueOf(skillId));
/*      */   }
/*      */ 
/*      */   
/*      */   public HashMap<Integer, SkillVal> getAllSkills() {
/* 1511 */     return this.skills;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCurrentSkill(int skillId) {
/* 1516 */     if (isCollectable() || isSource()) {
/* 1517 */       return -1.0F;
/*      */     }
/* 1519 */     if (this.skills.get(Integer.valueOf(skillId)) != null) {
/* 1520 */       return ((SkillVal)this.skills.get(Integer.valueOf(skillId))).getCurrentVal();
/*      */     }
/* 1522 */     if (skillId == 102 || skillId == 103 || skillId == 104 || skillId == 100 || skillId == 101 || skillId == 105 || skillId == 106) {
/*      */ 
/*      */ 
/*      */       
/* 1526 */       HexMap.VALREI.setEntityDefaultSkills(this);
/*      */       
/* 1528 */       if (this.skills.get(Integer.valueOf(skillId)) != null) {
/*      */         
/* 1530 */         createAndSaveSkills();
/* 1531 */         return ((SkillVal)this.skills.get(Integer.valueOf(skillId))).getCurrentVal();
/*      */       } 
/*      */     } 
/*      */     
/* 1535 */     logger.log(Level.WARNING, "Unable to find skill value for epic entity: " + getName() + " skill: " + skillId);
/* 1536 */     return -1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   class SkillVal
/*      */   {
/*      */     private float defaultVal;
/*      */     private float currentVal;
/*      */     
/*      */     SkillVal() {
/* 1546 */       this(-1.0F, -1.0F);
/*      */     }
/*      */ 
/*      */     
/*      */     SkillVal(float defaultVal, float currentVal) {
/* 1551 */       this.defaultVal = defaultVal;
/* 1552 */       this.currentVal = currentVal;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setCurrentVal(float newCurrentVal) {
/* 1557 */       this.currentVal = newCurrentVal;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getCurrentVal() {
/* 1562 */       return this.currentVal;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setDefaultVal(float newDefaultVal) {
/* 1567 */       this.defaultVal = newDefaultVal;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getDefaultVal() {
/* 1572 */       return this.defaultVal;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
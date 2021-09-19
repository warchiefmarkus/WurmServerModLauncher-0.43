/*      */ package com.wurmonline.server.tutorial;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.epic.EpicMission;
/*      */ import com.wurmonline.server.epic.EpicMissionEnum;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.questions.MissionPopup;
/*      */ import com.wurmonline.server.questions.SimplePopup;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.webinterface.WcEpicKarmaCommand;
/*      */ import com.wurmonline.server.webinterface.WcEpicStatusReport;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.LinkedList;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ public final class TriggerEffect
/*      */   implements CounterTypes, MiscConstants, Comparable<TriggerEffect>, TimeConstants
/*      */ {
/*   87 */   private static Logger logger = Logger.getLogger(TriggerEffect.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String UPDATE_EFFECT = "UPDATE TRIGGEREFFECTS SET NAME=?,DESCRIPTION=?,REWARDITEM=?,REWARDITEMNUMBERS=?,REWARDQUALITY=?,REWARDBYTE=?,EXISTINGREWARDITEMID=?,REWARDTARGETCONTAINERID=?,REWARDSKILLNUM=?,REWARDSKILLVAL=?,SPECIALEFFECTID=?,SOUND=?,TEXT=?,MISSION=?,MISSIONSTATECHANGE=?,INACTIVE=?,CREATOR=?,CREATEDDATE=?,LASTMODIFIER=?,TRIGGERID=?,DESTROYTARGET=?,ITEMMATERIAL=?,NEWBIE=?,MODIFYTILEX=?,MODIFYTILEY=?,NEWTILETYPE=?,NEWTILEDATA=?,SPAWNTILEX=?,SPAWNTILEY=?,CREATURESPAWN=?,CREATUREAGE=?,CREATURE_TYPE=?,CREATURE_NAME=?,TELEPORTX=?,TELEPORTY=?,MISSIONACTIVATED=?,MISSIONDEACTIVATED=?,TRIGGER_ACTIVATED=?,TRIGGER_DEACTIVATED=?,EFFECT_ACTIVATED=?,EFFECT_DEACTIVATED=?,WSZX=?,WSZY=?,STARTSKILLGAIN=?,STOPSKILLGAIN=?,DESTROYITEMS=?,TOP=?,TELEPORTLAYER=?,ACHIEVEMENTID=? WHERE ID=?";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String CREATE_EFFECT = "INSERT INTO TRIGGEREFFECTS (NAME, DESCRIPTION,REWARDITEM,REWARDITEMNUMBERS,REWARDQUALITY,REWARDBYTE,EXISTINGREWARDITEMID,REWARDTARGETCONTAINERID,REWARDSKILLNUM,REWARDSKILLVAL,SPECIALEFFECTID,SOUND,TEXT,MISSION,MISSIONSTATECHANGE,INACTIVE,CREATOR,CREATEDDATE,LASTMODIFIER,TRIGGERID,DESTROYTARGET,CREATORID,CREATORTYPE,ITEMMATERIAL,NEWBIE,MODIFYTILEX,MODIFYTILEY,NEWTILETYPE,NEWTILEDATA,SPAWNTILEX,SPAWNTILEY,CREATURESPAWN,CREATUREAGE,CREATURE_TYPE,CREATURE_NAME,TELEPORTX,TELEPORTY,MISSIONACTIVATED,MISSIONDEACTIVATED,TRIGGER_ACTIVATED,TRIGGER_DEACTIVATED,EFFECT_ACTIVATED,EFFECT_DEACTIVATED,WSZX,WSZY,STARTSKILLGAIN,STOPSKILLGAIN,DESTROYITEMS,TOP,TELEPORTLAYER,ACHIEVEMENTID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DELETE_EFFECT = "DELETE FROM TRIGGEREFFECTS WHERE ID=?";
/*      */ 
/*      */ 
/*      */   
/*      */   private int id;
/*      */ 
/*      */ 
/*      */   
/*      */   private String name;
/*      */ 
/*      */ 
/*      */   
/*      */   private String description;
/*      */ 
/*      */ 
/*      */   
/*      */   private int rewardItem;
/*      */ 
/*      */ 
/*      */   
/*      */   private int rewardNumbers;
/*      */ 
/*      */ 
/*      */   
/*      */   private int rewardQl;
/*      */ 
/*      */ 
/*      */   
/*      */   private byte rewardByteValue;
/*      */ 
/*      */ 
/*      */   
/*      */   private long existingItemReward;
/*      */ 
/*      */ 
/*      */   
/*      */   private long rewardTargetContainerId;
/*      */ 
/*      */ 
/*      */   
/*      */   private int rewardSkillNum;
/*      */ 
/*      */ 
/*      */   
/*      */   private float rewardSkillVal;
/*      */ 
/*      */ 
/*      */   
/*      */   private int triggerId;
/*      */ 
/*      */ 
/*      */   
/*      */   private int specialEffectId;
/*      */ 
/*      */ 
/*      */   
/*      */   private int achievementId;
/*      */ 
/*      */   
/*      */   private String soundName;
/*      */ 
/*      */   
/*      */   private String topText;
/*      */ 
/*      */   
/*      */   private String textDisplayed;
/*      */ 
/*      */   
/*      */   private int missionId;
/*      */ 
/*      */   
/*      */   private float missionStateChange;
/*      */ 
/*      */   
/*      */   private boolean destroysTarget = false;
/*      */ 
/*      */   
/*      */   private boolean inActive = false;
/*      */ 
/*      */   
/*      */   private boolean startSkillGain = true;
/*      */ 
/*      */   
/*      */   private boolean stopSkillGain = false;
/*      */ 
/*      */   
/*      */   private boolean destroyItems = false;
/*      */ 
/*      */   
/*      */   private String creatorName;
/*      */ 
/*      */   
/*      */   private String createdDate;
/*      */ 
/*      */   
/*      */   private String lastModifierName;
/*      */ 
/*      */   
/*      */   private Timestamp lastModifiedDate;
/*      */ 
/*      */   
/*  200 */   private long ownerId = 0L;
/*      */ 
/*      */   
/*  203 */   private byte creatorType = 0;
/*      */   
/*  205 */   private byte itemMaterial = 0;
/*      */   
/*      */   private boolean newbieItem = false;
/*      */   
/*  209 */   private int modifyTileX = 0;
/*      */   
/*  211 */   private int modifyTileY = 0;
/*      */   
/*  213 */   private int newTileType = 0;
/*      */   
/*  215 */   private byte newTileData = 0;
/*      */   
/*  217 */   private int spawnTileX = 0;
/*      */   
/*  219 */   private int spawnTileY = 0;
/*      */   
/*  221 */   private int creatureSpawn = 0;
/*      */   
/*  223 */   private int creatureAge = 0;
/*  224 */   private byte creatureType = 0;
/*  225 */   private String creatureName = "";
/*      */   
/*  227 */   private int teleportX = 0;
/*      */   
/*  229 */   private int teleportY = 0;
/*      */   
/*  231 */   private int teleportLayer = 0;
/*      */   
/*  233 */   private int missionToActivate = 0;
/*  234 */   private int missionToDeActivate = 0;
/*  235 */   private int triggerToActivate = 0;
/*  236 */   private int triggerToDeActivate = 0;
/*  237 */   private int effectToActivate = 0;
/*  238 */   private int effectToDeActivate = 0;
/*      */   
/*      */   private boolean destroyed = false;
/*      */   
/*  242 */   private int windowSizeX = 0;
/*      */   
/*  244 */   private int windowSizeY = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreatedDate(String aCreatedDate) {
/*  259 */     this.createdDate = aCreatedDate;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCreatedDate() {
/*  264 */     return this.createdDate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMissionToActivate(int val) {
/*  269 */     this.missionToActivate = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMissionToActivate() {
/*  274 */     return this.missionToActivate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMissionToDeActivate(int val) {
/*  279 */     this.missionToDeActivate = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMissionToDeActivate() {
/*  284 */     return this.missionToDeActivate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTriggerToActivate(int val) {
/*  289 */     this.triggerToActivate = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTriggerToActivate() {
/*  294 */     return this.triggerToActivate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTriggerToDeActivate(int val) {
/*  299 */     this.triggerToDeActivate = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTriggerToDeActivate() {
/*  304 */     return this.triggerToDeActivate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEffectToActivate(int val) {
/*  309 */     this.effectToActivate = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getEffectToActivate() {
/*  314 */     return this.effectToActivate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEffectToDeActivate(int val) {
/*  319 */     this.effectToDeActivate = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getEffectToDeActivate() {
/*  324 */     return this.effectToDeActivate;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTeleportY(int val) {
/*  329 */     this.teleportY = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTeleportY() {
/*  334 */     return this.teleportY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTeleportX(int val) {
/*  339 */     this.teleportX = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTeleportX() {
/*  344 */     return this.teleportX;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTeleportLayer(int val) {
/*  349 */     this.teleportLayer = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTeleportLayer() {
/*  354 */     return this.teleportLayer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWindowSizeX(int val) {
/*  359 */     this.windowSizeX = Math.max(0, Math.min(999, val));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getWindowSizeX() {
/*  364 */     return this.windowSizeX;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWindowSizeY(int val) {
/*  369 */     this.windowSizeY = Math.max(0, Math.min(999, val));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getWindowSizeY() {
/*  374 */     return this.windowSizeY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatureAge(int val) {
/*  379 */     this.creatureAge = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCreatureAge() {
/*  384 */     return this.creatureAge;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatureSpawn(int val) {
/*  389 */     this.creatureSpawn = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCreatureSpawn() {
/*  394 */     return this.creatureSpawn;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatureType(byte val) {
/*  399 */     this.creatureType = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getCreatureType() {
/*  404 */     return this.creatureType;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public void setCreatureName(String val) {
/*  410 */     if (val == null) {
/*  411 */       this.creatureName = "";
/*      */     } else {
/*  413 */       this.creatureName = val;
/*      */     } 
/*      */   }
/*      */   
/*      */   public String getCreatureName() {
/*  418 */     return this.creatureName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSpawnTileY(int val) {
/*  423 */     this.spawnTileY = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSpawnTileY() {
/*  428 */     return this.spawnTileY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSpawnTileX(int val) {
/*  433 */     this.spawnTileX = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSpawnTileX() {
/*  438 */     return this.spawnTileX;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setModifyTileY(int val) {
/*  443 */     this.modifyTileY = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getModifyTileY() {
/*  448 */     return this.modifyTileY;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNewTileType() {
/*  453 */     return this.newTileType;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNewTileType(int val) {
/*  458 */     this.newTileType = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setModifyTileX(int val) {
/*  463 */     this.modifyTileX = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getModifyTileX() {
/*  468 */     return this.modifyTileX;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNewTileData(byte tiledata) {
/*  473 */     this.newTileData = tiledata;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getNewTileData() {
/*  478 */     return this.newTileData;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemMaterial(byte material) {
/*  483 */     this.itemMaterial = material;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getItemMaterial() {
/*  488 */     return this.itemMaterial;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNewbieItem(boolean newbie) {
/*  493 */     this.newbieItem = newbie;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isNewbieItem() {
/*  498 */     return this.newbieItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStopSkillgain(boolean stop) {
/*  503 */     this.stopSkillGain = stop;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isStopSkillgain() {
/*  508 */     return this.stopSkillGain;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStartSkillgain(boolean start) {
/*  513 */     this.startSkillGain = start;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isStartSkillgain() {
/*  518 */     return this.startSkillGain;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDestroyInventory(boolean destroys) {
/*  523 */     this.destroyItems = destroys;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean destroysInventory() {
/*  528 */     return this.destroyItems;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatorType(byte aCreatorType) {
/*  533 */     this.creatorType = aCreatorType;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getCreatorType() {
/*  538 */     return this.creatorType;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOwnerId(long aWurmId) {
/*  543 */     this.ownerId = aWurmId;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getOwnerId() {
/*  548 */     return this.ownerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setId(int aId) {
/*  557 */     this.id = aId;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getId() {
/*  562 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setLastModifiedDate(Timestamp aLastModifiedDate) {
/*  571 */     this.lastModifiedDate = aLastModifiedDate;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getLastModifiedDate() {
/*  576 */     return DateFormat.getDateInstance(2).format(this.lastModifiedDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDescription() {
/*  581 */     return this.description;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/*  586 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getType() {
/*  593 */     StringBuilder buf = new StringBuilder();
/*  594 */     if (!getTopText().isEmpty())
/*  595 */       buf.append(",popup"); 
/*  596 */     if (getTeleportX() != 0)
/*  597 */       buf.append(",tp"); 
/*  598 */     if (getSpawnTileX() != 0)
/*  599 */       buf.append(",spawn"); 
/*  600 */     if (getModifyTileX() != 0)
/*  601 */       buf.append(",tile"); 
/*  602 */     if (getRewardItem() > 0)
/*  603 */       buf.append(",item"); 
/*  604 */     if (getSpecialEffectId() > 0)
/*  605 */       buf.append(",special"); 
/*  606 */     if (getRewardSkillNum() > 0)
/*  607 */       buf.append(",skill"); 
/*  608 */     if (!getSoundName().isEmpty())
/*  609 */       buf.append(",sound"); 
/*  610 */     if (buf.length() == 0)
/*  611 */       buf.append(",other"); 
/*  612 */     return buf.substring(1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDescription(String n) {
/*  617 */     this.description = n;
/*      */     
/*  619 */     if (this.description != null) {
/*  620 */       this.description = this.description.substring(0, Math.min(this.description.length(), 400));
/*      */     }
/*      */   }
/*      */   
/*      */   public void setName(String n) {
/*  625 */     this.name = n;
/*  626 */     if (this.name != null) {
/*  627 */       this.name = this.name.substring(0, Math.min(this.name.length(), 40));
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isInactive() {
/*  632 */     return this.inActive;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInactive(boolean inactive) {
/*  637 */     this.inActive = inactive;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean destroysTarget() {
/*  642 */     return this.destroysTarget;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDestroysTarget(boolean destroys) {
/*  647 */     this.destroysTarget = destroys;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSoundName() {
/*  652 */     return this.soundName;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRewardItem() {
/*  657 */     return this.rewardItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRewardNumbers() {
/*  662 */     return this.rewardNumbers;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getExistingItemReward() {
/*  667 */     return this.existingItemReward;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRewardQl() {
/*  672 */     return this.rewardQl;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getRewardByteValue() {
/*  677 */     return this.rewardByteValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatorName(String n) {
/*  682 */     this.creatorName = n;
/*  683 */     if (this.creatorName != null) {
/*  684 */       this.creatorName = this.creatorName.substring(0, Math.min(this.creatorName.length(), 40));
/*      */     }
/*      */   }
/*      */   
/*      */   public String getCreatorName() {
/*  689 */     return this.creatorName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastModifierName(String aName) {
/*  697 */     this.lastModifierName = aName;
/*  698 */     if (this.lastModifierName != null) {
/*  699 */       this.lastModifierName = this.lastModifierName.substring(0, Math.min(this.lastModifierName.length(), 40));
/*      */     }
/*      */   }
/*      */   
/*      */   public String getLastModifierName() {
/*  704 */     return this.lastModifierName;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTopText() {
/*  709 */     return this.topText;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTopText(String tdi) {
/*  714 */     this.topText = tdi;
/*  715 */     if (this.topText != null) {
/*  716 */       this.topText = this.topText.substring(0, Math.min(this.topText.length(), 1000));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTextDisplayed() {
/*  722 */     return this.textDisplayed;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTextDisplayed(String tdi) {
/*  727 */     this.textDisplayed = tdi;
/*  728 */     if (this.textDisplayed != null) {
/*  729 */       this.textDisplayed = this.textDisplayed.substring(0, Math.min(this.textDisplayed.length(), 1000));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRewardItem(int ri) {
/*  735 */     this.rewardItem = ri;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRewardNumbers(int nums) {
/*  740 */     this.rewardNumbers = nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setExistingItemReward(long rew) {
/*  745 */     this.existingItemReward = rew;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getRewardTargetContainerId() {
/*  750 */     return this.rewardTargetContainerId;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRewardSkillVal(float snum) {
/*  755 */     this.rewardSkillVal = snum;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRewardSkillModifier() {
/*  760 */     return this.rewardSkillVal;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSpecialEffect(int effectid) {
/*  765 */     this.specialEffectId = effectid;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSpecialEffectId() {
/*  770 */     return this.specialEffectId;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTrigger(int tid) {
/*  775 */     if (tid > 0)
/*  776 */       Triggers2Effects.addLink(tid, this.id, false); 
/*  777 */     this.triggerId = tid;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTriggerId() {
/*  783 */     MissionTrigger[] trigs = Triggers2Effects.getTriggersForEffect(this.id, true);
/*  784 */     if (trigs.length >= 1)
/*  785 */       return trigs[0].getId(); 
/*  786 */     return this.triggerId;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMissionStateChange(float stateChange) {
/*  791 */     this.missionStateChange = stateChange;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getMissionStateChange() {
/*  796 */     return this.missionStateChange;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMission(int mid) {
/*  801 */     this.missionId = mid;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMissionId() {
/*  806 */     return this.missionId;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRewardSkillNum(int snum) {
/*  811 */     this.rewardSkillNum = snum;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRewardSkillNum() {
/*  816 */     return this.rewardSkillNum;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRewardTargetContainerId(long rew) {
/*  821 */     this.rewardTargetContainerId = rew;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRewardQl(int ql) {
/*  826 */     this.rewardQl = ql;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRewardByteValue(byte bv) {
/*  831 */     this.rewardByteValue = bv;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSoundName(String sn) {
/*  836 */     this.soundName = sn;
/*  837 */     if (this.soundName != null) {
/*  838 */       this.soundName = this.soundName.substring(0, Math.min(this.soundName.length(), 50));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean setMissionState(Creature performer, MissionPerformed mperf, boolean setFinishedEpic) {
/*  846 */     boolean toReturn = false;
/*  847 */     Mission mission = Missions.getMissionWithId(this.missionId);
/*      */     
/*  849 */     if (mission != null && !mission.isInactive()) {
/*      */       
/*  851 */       float newstate = Math.min(100.0F, mperf.getState() + getMissionStateChange());
/*  852 */       boolean sendStartPopup = false;
/*  853 */       if (this.creatorType == 2) {
/*      */         
/*  855 */         if (setFinishedEpic)
/*      */         {
/*  857 */           newstate = 100.0F;
/*      */         }
/*      */       }
/*  860 */       else if (mperf.getMissionId() != this.missionId) {
/*      */         
/*  862 */         MissionPerformer missionPerfor = MissionPerformed.getMissionPerformer(performer.getWurmId());
/*      */         
/*  864 */         MissionPerformed mpf = missionPerfor.getMission(this.missionId);
/*  865 */         if (mpf != null) {
/*  866 */           mperf = mpf;
/*      */         }
/*      */         else {
/*      */           
/*  870 */           missionPerfor = MissionPerformed.startNewMission(this.missionId, performer.getWurmId(), 1.0F);
/*  871 */           sendStartPopup = true;
/*  872 */           mperf = missionPerfor.getMission(this.missionId);
/*      */         } 
/*      */       } 
/*  875 */       if (getMissionStateChange() == -1.0F) {
/*  876 */         newstate = -1.0F;
/*      */       }
/*  878 */       if (mperf != null && (mperf.isStarted() || mperf.isFailed())) {
/*      */ 
/*      */         
/*  881 */         boolean secondChance = ((mperf.isFailed() && mission.hasSecondChance()) || (mperf.isCompleted() && mission.mayBeRestarted()));
/*  882 */         if (secondChance) {
/*      */           
/*  884 */           toReturn = mperf.setState(1.0F, performer.getWurmId());
/*  885 */           sendStartPopup = true;
/*      */         } 
/*      */       } 
/*  888 */       if (!mperf.isFailed()) {
/*      */ 
/*      */ 
/*      */         
/*  892 */         if (sendStartPopup)
/*      */         {
/*  894 */           if (mission.getInstruction() != null && mission.getInstruction().length() > 0) {
/*      */             
/*  896 */             SimplePopup pop = new SimplePopup(performer, "Mission start", mission.getInstruction());
/*  897 */             pop.sendQuestion();
/*      */           } 
/*      */         }
/*  900 */         if (mission.getMaxTimeSeconds() > 0 && 
/*  901 */           System.currentTimeMillis() > mperf.getFinishTimeAsLong(mission.getMaxTimeSeconds())) {
/*      */           
/*  903 */           mperf.setState(-1.0F, performer.getWurmId());
/*  904 */           String miss = Server.getTimeFor(System.currentTimeMillis() - mperf
/*  905 */               .getFinishTimeAsLong(mission.getMaxTimeSeconds()));
/*  906 */           SimplePopup pop = new SimplePopup(performer, "Mission failed", "You failed " + mission.getName() + ". You are " + miss + " late.");
/*      */           
/*  908 */           pop.sendQuestion();
/*  909 */           toReturn = true;
/*      */         }
/*      */         else {
/*      */           
/*  913 */           performer.sendToLoggers("Proper state achieved for mission " + this.missionId, (byte)2);
/*  914 */           performer.sendToLoggers("Setting state of mission to " + newstate, (byte)2);
/*  915 */           toReturn = mperf.setState(newstate, performer.getWurmId());
/*      */         } 
/*      */       } 
/*      */     } 
/*  919 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean sendTriggerDescription(Creature performer) {
/*  924 */     if ((getTextDisplayed() != null && getTextDisplayed().length() > 0) || (
/*  925 */       getTopText() != null && getTopText().length() > 0)) {
/*      */       
/*  927 */       MissionPopup pop = new MissionPopup(performer, "Mission progress", "");
/*  928 */       if (this.windowSizeX > 0) {
/*      */         
/*  930 */         pop.windowSizeX = this.windowSizeX;
/*  931 */         pop.windowSizeY = this.windowSizeY;
/*      */       } 
/*  933 */       pop.setToSend(getTextDisplayed());
/*      */       
/*  935 */       pop.setTop(getTopText());
/*  936 */       pop.sendQuestion();
/*  937 */       return true;
/*      */     } 
/*  939 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void destroyInventoryItems(Item inventory, Creature performer) {
/*  944 */     Item[] inventoryItems = inventory.getItemsAsArray();
/*  945 */     for (int i = 0; i < inventoryItems.length; i++) {
/*      */       
/*  947 */       if (inventoryItems[i].isTraded())
/*      */       {
/*  949 */         if (performer.getTrade() != null)
/*  950 */           inventoryItems[i].getTradeWindow().removeItem(inventoryItems[i]); 
/*      */       }
/*  952 */       if (!inventoryItems[i].isNoDrop() && !inventoryItems[i].isCoin())
/*      */       {
/*  954 */         Items.destroyItem(inventoryItems[i].getWurmId());
/*      */       }
/*      */       
/*  957 */       if (inventoryItems[i].isInventoryGroup()) {
/*  958 */         destroyInventoryItems(inventoryItems[i], performer);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void effect(Creature performer, MissionPerformed perf, long target, boolean setFinished, boolean triggerEpicHelper) {
/*  965 */     performer.sendToLoggers("Running effect " + this.name + " state effect " + getMissionStateChange() + " on mission " + this.missionId, (byte)2);
/*      */     
/*  967 */     if (!this.inActive) {
/*      */       
/*  969 */       boolean sendSound = true;
/*      */ 
/*      */       
/*  972 */       if (performer.isPlayer() && this.creatorType != 2) {
/*      */         
/*  974 */         if (sendTriggerDescription(performer)) {
/*  975 */           ((Player)performer).setLastTrigger(this.id);
/*      */         }
/*      */       } else {
/*  978 */         sendSound = setFinished;
/*      */       } 
/*  980 */       if (sendSound && getSoundName() != null && getSoundName().length() > 0)
/*  981 */         SoundPlayer.playSound(getSoundName(), performer, 1.5F); 
/*  982 */       if (getMissionStateChange() != 0.0F) {
/*      */         
/*  984 */         if (setMissionState(performer, perf, setFinished))
/*      */         {
/*      */           
/*  987 */           if (this.destroysTarget && this.creatorType != 3)
/*      */           {
/*  989 */             destroyTarget(target);
/*      */           }
/*      */         }
/*      */         
/*  993 */         if (this.creatorType == 2) {
/*      */           
/*  995 */           performer.sendToLoggers("Trying to get epic mission for " + perf.getMissionId(), (byte)2);
/*  996 */           EpicMission em = EpicServerStatus.getEpicMissionForMission(perf.getMissionId());
/*  997 */           if (em != null)
/*      */           {
/*  999 */             if (em.isCurrent()) {
/*      */               
/* 1001 */               performer.sendToLoggers("Effect " + this.name + " state effect " + getMissionStateChange() + " on Epic mission " + em
/* 1002 */                   .getEntityName(), (byte)2);
/* 1003 */               em.updateProgress(em.getMissionProgress() + getMissionStateChange());
/* 1004 */               Deity d = Deities.translateDeityForEntity(em.getEpicEntityId());
/* 1005 */               int deityNum = -1;
/* 1006 */               if (d != null) {
/* 1007 */                 deityNum = d.getNumber();
/*      */               }
/* 1009 */               EpicMissionEnum missionEnum = EpicMissionEnum.getMissionForType(em.getMissionType());
/* 1010 */               if (Deities.getFavoredKingdom(deityNum) == performer.getKingdomTemplateId() || !Servers.localServer.EPIC)
/*      */               {
/*      */                 
/* 1013 */                 if (missionEnum != null && !EpicMissionEnum.isMissionKarmaGivenOnKill(missionEnum)) {
/*      */                   
/* 1015 */                   int karmaSplit = missionEnum.getKarmaBonusDiffMult() * em.getDifficulty();
/* 1016 */                   if (missionEnum.isKarmaMultProgress()) {
/*      */                     
/* 1018 */                     int karmaGained = (int)Math.ceil((karmaSplit / 100.0F * getMissionStateChange()));
/* 1019 */                     if (karmaGained > 0) {
/*      */                       
/* 1021 */                       performer.modifyKarma(karmaGained);
/* 1022 */                       if (performer.isPaying()) {
/*      */                         
/* 1024 */                         performer.setScenarioKarma(performer.getScenarioKarma() + karmaGained);
/* 1025 */                         if (Servers.localServer.EPIC) {
/*      */ 
/*      */                           
/* 1028 */                           WcEpicKarmaCommand wcek = new WcEpicKarmaCommand(WurmId.getNextWCCommandId(), new long[] { performer.getWurmId() }, new int[] { performer.getScenarioKarma() }, em.getEpicEntityId());
/* 1029 */                           wcek.sendToLoginServer();
/*      */                         } 
/*      */                       } 
/*      */                       
/* 1033 */                       if (getMissionStateChange() * 10.0F > 10.0F) {
/* 1034 */                         logger.log(Level.INFO, "Added karma " + karmaGained + " to " + performer.getName() + " for mission " + em
/* 1035 */                             .getMissionId() + " for " + em.getEntityName() + " state change=" + 
/* 1036 */                             getMissionStateChange() + " to " + em.getMissionProgress());
/*      */                       }
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               }
/* 1042 */               if (triggerEpicHelper || setFinished)
/* 1043 */                 performer.achievement(52); 
/* 1044 */               if (em.isCompleted()) {
/*      */                 
/* 1046 */                 logger.log(Level.INFO, "Mission is complete! " + performer.getName() + " mission " + em
/* 1047 */                     .getMissionId() + " for " + em.getEntityName() + ", performer mission is " + perf
/* 1048 */                     .getMissionId());
/* 1049 */                 if (performer.isPlayer()) {
/* 1050 */                   ((Player)performer).setLastTrigger(this.id);
/*      */                 }
/* 1052 */                 sendTriggerDescription(performer);
/* 1053 */                 Mission m = Missions.getMissionWithId(perf.getMissionId());
/* 1054 */                 if (m != null) {
/*      */                   
/* 1056 */                   if (Deities.getFavoredKingdom(deityNum) == performer.getKingdomTemplateId() || !Servers.localServer.EPIC) {
/*      */ 
/*      */                     
/* 1059 */                     LinkedList<Long> ids = new LinkedList<>();
/* 1060 */                     LinkedList<Integer> values = new LinkedList<>();
/*      */                     
/* 1062 */                     int baseKarma = missionEnum.getBaseKarma();
/* 1063 */                     int baseSleep = missionEnum.getBaseSleep();
/* 1064 */                     int karmaSplit = missionEnum.getKarmaBonusDiffMult() * em.getDifficulty();
/* 1065 */                     int sleepSplit = missionEnum.getSleepBonusDiffMult() * em.getDifficulty();
/*      */                     
/* 1067 */                     ArrayList<Player> totalNearby = new ArrayList<>();
/* 1068 */                     for (Player p : Players.getInstance().getPlayers()) {
/* 1069 */                       if (Deities.getFavoredKingdom(deityNum) == p.getKingdomTemplateId() || !Servers.localServer.EPIC)
/*      */                       {
/* 1071 */                         if (p.isWithinDistanceTo(performer, 300.0F))
/* 1072 */                           totalNearby.add(p);  } 
/*      */                     } 
/* 1074 */                     ArrayList<PlayerInfo> allParticipants = new ArrayList<>();
/* 1075 */                     MissionPerformer[] allperfs = MissionPerformed.getAllPerformers();
/* 1076 */                     for (MissionPerformer mp : allperfs) {
/*      */                       
/* 1078 */                       MissionPerformed thisMission = mp.getMission(m.getId());
/* 1079 */                       if (thisMission != null) {
/*      */                         
/* 1081 */                         thisMission.setState(100.0F, mp.getWurmId());
/* 1082 */                         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(mp.getWurmId());
/* 1083 */                         if (pinf != null) {
/* 1084 */                           allParticipants.add(pinf);
/*      */                         }
/*      */                       } 
/*      */                     } 
/* 1088 */                     int karmaGainedNearby = 0;
/* 1089 */                     int sleepGainedNearby = 0;
/* 1090 */                     if (karmaSplit > 0 && EpicMissionEnum.isKarmaSplitNearby(missionEnum)) {
/*      */                       
/* 1092 */                       karmaGainedNearby = (int)Math.ceil((karmaSplit / totalNearby.size()));
/* 1093 */                       karmaSplit = 0;
/*      */                     } 
/* 1095 */                     if (sleepSplit > 0 && missionEnum.isSleepMultNearby()) {
/*      */                       
/* 1097 */                       sleepGainedNearby = (int)Math.ceil((sleepSplit / totalNearby.size()));
/* 1098 */                       sleepSplit = 0;
/*      */                     } 
/*      */                     
/* 1101 */                     for (Player p : totalNearby) {
/*      */                       
/* 1103 */                       if (sleepGainedNearby > 0)
/* 1104 */                         p.getSaveFile().addToSleep((int)(Math.min(30, sleepGainedNearby) * 60L)); 
/* 1105 */                       if (karmaGainedNearby > 0) {
/*      */                         
/* 1107 */                         p.modifyKarma(Math.max(1, karmaGainedNearby));
/* 1108 */                         if (p.isPaying()) {
/*      */                           
/* 1110 */                           p.setScenarioKarma(p.getScenarioKarma() + Math.max(1, karmaGainedNearby));
/* 1111 */                           ids.add(Long.valueOf(p.getWurmId()));
/* 1112 */                           values.add(Integer.valueOf(p.getScenarioKarma()));
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*      */                     
/* 1117 */                     int karmaGained = baseKarma;
/* 1118 */                     if (karmaSplit > 0)
/* 1119 */                       karmaGained += (int)Math.ceil((karmaSplit / allParticipants.size())); 
/* 1120 */                     int sleepBonusGain = 0;
/* 1121 */                     if (sleepSplit > 0) {
/* 1122 */                       sleepBonusGain += (int)Math.ceil((sleepSplit / allParticipants.size()));
/*      */                     }
/* 1124 */                     for (PlayerInfo pinf : allParticipants) {
/*      */                       
/* 1126 */                       if (baseSleep > 0 || sleepBonusGain > 0) {
/* 1127 */                         pinf.addToSleep((int)((baseSleep + Math.min(30, sleepBonusGain)) * 60L));
/*      */                       }
/* 1129 */                       if (karmaGained > 0) {
/*      */                         
/* 1131 */                         pinf.setKarma(pinf.getKarma() + Math.max(1, karmaGained));
/* 1132 */                         if (pinf.isPaying()) {
/*      */                           
/* 1134 */                           pinf.setScenarioKarma(pinf.getScenarioKarma() + Math.max(1, karmaGained));
/* 1135 */                           ids.add(Long.valueOf(pinf.wurmId));
/* 1136 */                           values.add(Integer.valueOf(pinf.getScenarioKarma()));
/*      */                         } 
/*      */                       } 
/*      */                       
/* 1140 */                       if (Servers.localServer.PVPSERVER) {
/*      */                         
/*      */                         try {
/*      */                           
/* 1144 */                           pinf.setRank(pinf.getRank());
/*      */                         }
/* 1146 */                         catch (IOException iox) {
/*      */                           
/* 1148 */                           logger.log(Level.WARNING, getName() + ": failed to reset rank decay timer " + pinf.getRank(), iox);
/*      */                         } 
/*      */                       }
/*      */                       
/* 1152 */                       Achievements.triggerAchievement(pinf.wurmId, 601);
/*      */                     } 
/*      */                     
/* 1155 */                     if (Servers.localServer.EPIC) {
/*      */                       
/* 1157 */                       long[] idsToSend = new long[ids.size()];
/* 1158 */                       int[] valuesToSend = new int[values.size()];
/* 1159 */                       for (int x = 0; x < idsToSend.length; x++) {
/*      */                         
/* 1161 */                         idsToSend[x] = ((Long)ids.get(x)).longValue();
/* 1162 */                         valuesToSend[x] = ((Integer)values.get(x)).intValue();
/*      */                       } 
/*      */ 
/*      */                       
/* 1166 */                       WcEpicKarmaCommand wcek = new WcEpicKarmaCommand(WurmId.getNextWCCommandId(), idsToSend, valuesToSend, em.getEpicEntityId());
/* 1167 */                       wcek.sendToLoginServer();
/*      */                     } 
/*      */                     
/* 1170 */                     performer.achievement(53);
/*      */                   } 
/*      */                   
/* 1173 */                   Players.printRanks();
/* 1174 */                   MissionTrigger[] triggers = MissionTriggers.getAllTriggers();
/* 1175 */                   for (MissionTrigger t : triggers) {
/*      */                     
/* 1177 */                     if (t.getMissionRequired() == m.getId()) {
/*      */                       
/* 1179 */                       logger.log(Level.INFO, performer.getName() + "Destroying triggers for  mission " + em
/* 1180 */                           .getMissionId() + " for " + em.getEntityName() + ", performer mission is " + perf
/* 1181 */                           .getMissionId());
/*      */                       
/* 1183 */                       TriggerEffects.destroyEffectsForTrigger(t.getId());
/* 1184 */                       t.destroy();
/*      */                     } 
/*      */                   } 
/*      */                 } 
/* 1188 */                 logger.log(Level.INFO, performer.getName() + "Destroying effect for  mission " + em
/* 1189 */                     .getMissionId() + " for " + em.getEntityName() + ", performer mission is " + perf
/* 1190 */                     .getMissionId());
/*      */                 
/* 1192 */                 destroy();
/* 1193 */                 logger.log(Level.INFO, performer.getName() + " Sending report for " + em.getMissionId() + " for " + em
/* 1194 */                     .getEntityName() + ", performer mission is " + perf
/* 1195 */                     .getMissionId());
/*      */                 
/* 1197 */                 WcEpicStatusReport wce = new WcEpicStatusReport(WurmId.getNextWCCommandId(), true, em.getEpicEntityId(), em.getMissionType(), em.getDifficulty());
/* 1198 */                 wce.sendToLoginServer();
/* 1199 */                 EpicServerStatus.storeLastMissionForEntity(em.getEpicEntityId(), em);
/*      */                 
/* 1201 */                 if (!Servers.localServer.EPIC)
/*      */                 {
/* 1203 */                   int deityNumber = em.getEpicEntityId();
/* 1204 */                   if (deityNumber > 0 && deityNumber <= 4)
/*      */                   {
/*      */                     
/* 1207 */                     String entityName = Deities.getEntityName(deityNumber);
/* 1208 */                     EpicServerStatus es = new EpicServerStatus();
/* 1209 */                     if (EpicServerStatus.getCurrentScenario() == null)
/* 1210 */                       EpicServerStatus.loadLocalEntries(); 
/* 1211 */                     es.generateNewMissionForEpicEntity(deityNumber, entityName, em.getDifficulty() + 1, 604800, em
/* 1212 */                         .getScenarioName(), EpicServerStatus.getCurrentScenario().getScenarioNumber(), "You must really do this for " + entityName + " because yeah you know.", true);
/*      */                   }
/*      */                 
/*      */                 }
/*      */               
/*      */               } 
/*      */             } else {
/*      */               
/* 1220 */               performer.sendToLoggers("Not current mission: " + em.getEntityName(), (byte)2);
/*      */             }  } 
/*      */         } 
/*      */       } 
/* 1224 */       if (destroysInventory()) {
/*      */         
/* 1226 */         destroyInventoryItems(performer.getInventory(), performer);
/*      */         
/* 1228 */         Item[] boditems = performer.getBody().getContainersAndWornItems();
/* 1229 */         for (int i = 0; i < boditems.length; i++) {
/*      */           
/* 1231 */           if (boditems[i].isTraded())
/*      */           {
/* 1233 */             if (performer.getTrade() != null)
/* 1234 */               boditems[i].getTradeWindow().removeItem(boditems[i]); 
/*      */           }
/* 1236 */           if (!boditems[i].isNoDrop() && !boditems[i].isCoin())
/*      */           {
/* 1238 */             Items.destroyItem(boditems[i].getWurmId());
/*      */           }
/*      */         } 
/*      */       } 
/* 1242 */       if (this.rewardItem > 0 && this.creatorType != 3) {
/*      */         
/* 1244 */         performer.sendToLoggers("Creating " + this.rewardNumbers + " of template " + this.rewardItem, (byte)2);
/* 1245 */         for (int x = 0; x < this.rewardNumbers; x++) {
/*      */ 
/*      */           
/*      */           try {
/* 1249 */             Item toCreate = ItemFactory.createItem(this.rewardItem, this.rewardQl, this.itemMaterial, (byte)0, performer
/* 1250 */                 .getName());
/* 1251 */             if (this.newbieItem) {
/* 1252 */               toCreate.setAuxData((byte)1);
/* 1253 */             } else if (this.rewardByteValue != 0) {
/* 1254 */               toCreate.setAuxData(this.rewardByteValue);
/*      */             } 
/* 1256 */             insertItem(performer, toCreate, x);
/*      */           }
/* 1258 */           catch (NoSuchTemplateException nsi) {
/*      */             
/* 1260 */             logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*      */             
/*      */             break;
/* 1263 */           } catch (FailedException fe) {
/*      */             
/* 1265 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1270 */       if (this.existingItemReward > 0L) {
/*      */         
/*      */         try {
/*      */           
/* 1274 */           Item toCreate = Items.getItem(this.existingItemReward);
/* 1275 */           insertItem(performer, toCreate, 0);
/* 1276 */           performer.sendToLoggers("Inserting existing reward " + this.existingItemReward, (byte)2);
/*      */           
/* 1278 */           destroy();
/*      */         }
/* 1280 */         catch (NoSuchItemException nsi) {
/*      */           
/* 1282 */           performer.sendToLoggers("Inserting existing reward " + this.existingItemReward + " failed. Does not exist.", (byte)2);
/*      */           
/* 1284 */           this.existingItemReward = 0L;
/* 1285 */           update();
/*      */         } 
/*      */       }
/* 1288 */       if (this.rewardSkillNum > 0 && this.creatorType != 3) {
/*      */         
/* 1290 */         performer.sendToLoggers("Adding skill " + this.rewardSkillNum + " : " + this.rewardSkillVal, (byte)2);
/* 1291 */         Skills skills = performer.getSkills();
/* 1292 */         if (skills != null) {
/*      */           
/*      */           try {
/*      */             
/* 1296 */             Skill skill = skills.getSkill(this.rewardSkillNum);
/*      */             
/* 1298 */             double existing = skill.getKnowledge();
/*      */             
/* 1300 */             double reward = this.rewardSkillVal;
/*      */             
/* 1302 */             if (this.rewardSkillVal < 1.0F) {
/*      */               
/* 1304 */               double diff = 100.0D - existing;
/* 1305 */               reward = diff * this.rewardSkillVal;
/*      */             } 
/* 1307 */             skill.setKnowledge(existing + reward, false);
/*      */           }
/* 1309 */           catch (NoSuchSkillException nss) {
/*      */             
/* 1311 */             skills.learn(this.rewardSkillNum, this.rewardSkillVal);
/*      */           } 
/*      */         }
/*      */       } 
/* 1315 */       if (getSpecialEffectId() > 0)
/*      */       {
/* 1317 */         if (this.teleportX > 0 || this.teleportY > 0) {
/*      */           
/* 1319 */           SpecialEffects.getEffects()[getSpecialEffectId()].run(performer, this.teleportX, this.teleportY, this.teleportLayer);
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/* 1325 */             SpecialEffects.getEffects()[getSpecialEffectId()].run(performer, target, this.rewardNumbers);
/*      */           }
/* 1327 */           catch (Exception ex) {
/*      */             
/* 1329 */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */           } 
/*      */         }  } 
/* 1332 */       if (getAchievementId() > 0)
/*      */       {
/* 1334 */         performer.achievement(getAchievementId());
/*      */       }
/* 1336 */       if (getModifyTileX() > 0 || getModifyTileY() > 0)
/*      */       {
/* 1338 */         if (getNewTileType() != 0)
/*      */         {
/* 1340 */           if (getNewTileType() == Tiles.Tile.TILE_CAVE_WALL.id || getNewTileType() == Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id) {
/*      */             
/* 1342 */             Terraforming.setAsRock(getModifyTileX(), getModifyTileY(), false);
/* 1343 */             if (getNewTileType() == Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id) {
/*      */ 
/*      */               
/* 1346 */               int ntile = Server.caveMesh.getTile(getModifyTileX(), getModifyTileY());
/* 1347 */               if (Tiles.decodeType(ntile) == Tiles.Tile.TILE_CAVE_WALL.id)
/*      */               {
/* 1349 */                 Server.caveMesh.setTile(
/* 1350 */                     getModifyTileX(), 
/* 1351 */                     getModifyTileY(), 
/* 1352 */                     Tiles.encode(Tiles.decodeHeight(ntile), Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id, 
/* 1353 */                       Tiles.decodeData(ntile)));
/* 1354 */                 Server.setCaveResource(getModifyTileX(), getModifyTileY(), 10000 + Server.rand.nextInt(20000));
/* 1355 */                 Players.getInstance().sendChangedTile(getModifyTileX(), getModifyTileY(), false, true);
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/* 1361 */             int otile = Server.surfaceMesh.getTile(getModifyTileX(), getModifyTileY());
/* 1362 */             if (Tiles.decodeType(otile) != Tiles.Tile.TILE_HOLE.id) {
/*      */               
/* 1364 */               VolaTile t = Zones.getOrCreateTile(getModifyTileX(), getModifyTileY(), true);
/* 1365 */               if (t != null)
/*      */               {
/* 1367 */                 if (t.getStructure() == null) {
/*      */                   
/* 1369 */                   Server.setSurfaceTile(getModifyTileX(), getModifyTileY(), 
/* 1370 */                       Tiles.decodeHeight(otile), (byte)getNewTileType(), getNewTileData());
/* 1371 */                   Server.setWorldResource(getModifyTileX(), getModifyTileY(), 0);
/* 1372 */                   Players.getInstance().sendChangedTile(getModifyTileX(), getModifyTileY(), true, true);
/*      */                 } else {
/*      */                   
/* 1375 */                   performer.getCommunicator().sendNormalServerMessage("A structure bars an effect of your action.");
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/* 1382 */       if (this.spawnTileX > 0 || this.spawnTileY > 0) {
/*      */         
/*      */         try {
/*      */           
/* 1386 */           if (getCreatureSpawn() > 0) {
/*      */             
/* 1388 */             CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(getCreatureSpawn());
/* 1389 */             byte sex = ct.getSex();
/* 1390 */             if (!ct.keepSex)
/* 1391 */               sex = Server.rand.nextBoolean() ? 1 : 0; 
/* 1392 */             byte ttype = getCreatureType();
/* 1393 */             byte age = (byte)getCreatureAge();
/*      */             
/*      */             try {
/* 1396 */               Creature creature = Creature.doNew(ct.getTemplateId(), true, (this.spawnTileX * 4 + 2), (this.spawnTileY * 4 + 2), performer
/* 1397 */                   .getStatus().getRotation() - 180.0F, performer.getLayer(), ct.getName(), sex, (byte)0, ttype, false, age);
/*      */             
/*      */             }
/* 1400 */             catch (Exception cex) {
/*      */               
/* 1402 */               logger.log(Level.WARNING, performer.getName() + " " + cex.getMessage(), cex);
/*      */             }
/*      */           
/*      */           } 
/* 1406 */         } catch (NoSuchCreatureTemplateException nsc) {
/*      */           
/* 1408 */           logger.log(Level.WARNING, performer.getName() + " " + nsc.getMessage(), (Throwable)nsc);
/*      */         } 
/*      */       }
/* 1411 */       if (getMissionToActivate() > 0) {
/*      */         
/* 1413 */         Mission mis = Missions.getMissionWithId(getMissionToActivate());
/* 1414 */         if (mis != null)
/*      */         {
/* 1416 */           mis.setInactive(false);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1421 */       if (getMissionToDeActivate() > 0) {
/*      */         
/* 1423 */         Mission mis = Missions.getMissionWithId(getMissionToDeActivate());
/* 1424 */         if (mis != null)
/*      */         {
/* 1426 */           mis.setInactive(true);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1431 */       if (getTriggerToActivate() > 0) {
/*      */         
/* 1433 */         MissionTrigger trg = MissionTriggers.getTriggerWithId(getTriggerToActivate());
/* 1434 */         if (trg != null)
/*      */         {
/* 1436 */           trg.setInactive(false);
/*      */         }
/*      */       } 
/*      */       
/* 1440 */       if (getTriggerToDeActivate() > 0) {
/*      */         
/* 1442 */         MissionTrigger trg = MissionTriggers.getTriggerWithId(getTriggerToDeActivate());
/* 1443 */         if (trg != null)
/*      */         {
/* 1445 */           trg.setInactive(true);
/*      */         }
/*      */       } 
/*      */       
/* 1449 */       if (getEffectToActivate() > 0) {
/*      */         
/* 1451 */         TriggerEffect eff = TriggerEffects.getTriggerEffect(getEffectToActivate());
/* 1452 */         if (eff != null)
/*      */         {
/* 1454 */           eff.setInactive(false);
/*      */         }
/*      */       } 
/*      */       
/* 1458 */       if (getEffectToDeActivate() > 0) {
/*      */         
/* 1460 */         TriggerEffect eff = TriggerEffects.getTriggerEffect(getEffectToDeActivate());
/* 1461 */         if (eff != null)
/*      */         {
/* 1463 */           eff.setInactive(true);
/*      */         }
/*      */       } 
/*      */       
/* 1467 */       if (isStopSkillgain()) {
/*      */         
/* 1469 */         performer.setHasSkillGain(false);
/* 1470 */         performer.getCommunicator().sendAlertServerMessage("Your skill gain has been temporarily paused.");
/*      */       } 
/* 1472 */       if (isStartSkillgain()) {
/*      */         
/* 1474 */         if (!performer.hasSkillGain())
/* 1475 */           performer.getCommunicator().sendSafeServerMessage("Your skill gain has been unpaused."); 
/* 1476 */         performer.setHasSkillGain(true);
/*      */       } 
/* 1478 */       if (this.teleportX > 0 || this.teleportY > 0)
/*      */       {
/* 1480 */         if (getSpecialEffectId() <= 0)
/*      */         {
/* 1482 */           if (performer.getCurrentTile() != null && !performer.isTeleporting())
/*      */           {
/* 1484 */             performer.setTeleportPoints((short)this.teleportX, (short)this.teleportY, this.teleportLayer, 0);
/* 1485 */             performer.startTeleporting();
/* 1486 */             performer.getCommunicator().sendTeleport(false);
/*      */           }
/*      */         
/*      */         }
/*      */       }
/*      */     } else {
/*      */       
/* 1493 */       performer.sendToLoggers("The effect " + this.id + " is inactive", (byte)2);
/* 1494 */       logger.log(Level.WARNING, "The effect " + this.id + " is inactive but called anyways.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void destroyTarget(long targetId) {
/* 1500 */     if (WurmId.getType(targetId) == 1) {
/*      */       
/*      */       try {
/*      */         
/* 1504 */         Creature c = Creatures.getInstance().getCreature(targetId);
/* 1505 */         c.destroy();
/*      */       }
/* 1507 */       catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */     }
/*      */ 
/*      */     
/* 1511 */     if (WurmId.getType(targetId) == 0) {
/*      */       
/*      */       try {
/*      */         
/* 1515 */         Player p = Players.getInstance().getPlayer(targetId);
/* 1516 */         p.die(true, "Destruction");
/*      */       }
/* 1518 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     }
/*      */ 
/*      */     
/* 1522 */     if (WurmId.getType(targetId) == 5) {
/*      */       
/* 1524 */       Wall wall = Wall.getWall(targetId);
/* 1525 */       if (wall != null)
/*      */       {
/* 1527 */         wall.setAsPlan();
/*      */       }
/*      */     } 
/* 1530 */     if (WurmId.getType(targetId) == 2 || WurmId.getType(targetId) == 6 || 
/* 1531 */       WurmId.getType(targetId) == 19 || WurmId.getType(targetId) == 20) {
/*      */       
/* 1533 */       Items.destroyItem(targetId);
/*      */     }
/* 1535 */     else if (WurmId.getType(targetId) == 7) {
/*      */       
/* 1537 */       Fence fence = Fence.getFence(targetId);
/* 1538 */       if (fence != null)
/*      */       {
/* 1540 */         fence.destroy();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void insertItem(Creature performer, Item toCreate, int x) {
/* 1547 */     if (this.rewardTargetContainerId == 0L) {
/*      */       
/* 1549 */       performer.sendToLoggers("Inserting into inventory " + toCreate.getName(), (byte)2);
/* 1550 */       performer.getInventory().insertItem(toCreate, true);
/* 1551 */       if (x < 1) {
/* 1552 */         performer.getCommunicator().sendSafeServerMessage("You are rewarded a " + toCreate.getName() + ".");
/*      */       }
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/* 1558 */         Item container = Items.getItem(this.rewardTargetContainerId);
/* 1559 */         performer.sendToLoggers("Inserting into " + container.getName() + " " + toCreate.getName(), (byte)2);
/* 1560 */         container.insertItem(toCreate, true);
/* 1561 */         if (x < 1) {
/* 1562 */           performer.getCommunicator().sendSafeServerMessage("A " + container
/* 1563 */               .getName() + " now contains " + toCreate.getName() + ".");
/*      */         }
/* 1565 */       } catch (NoSuchItemException nsi) {
/*      */         
/* 1567 */         performer.sendToLoggers("Inserting " + toCreate.getName() + " into inventory because reward container no longer exists ", (byte)2);
/*      */         
/* 1569 */         performer.getInventory().insertItem(toCreate, true);
/* 1570 */         if (x < 1) {
/* 1571 */           performer.getCommunicator().sendSafeServerMessage("You are rewarded a " + toCreate.getName() + ".");
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void update() {
/* 1578 */     Connection dbcon = null;
/* 1579 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1582 */       dbcon = DbConnector.getPlayerDbCon();
/* 1583 */       ps = dbcon.prepareStatement("UPDATE TRIGGEREFFECTS SET NAME=?,DESCRIPTION=?,REWARDITEM=?,REWARDITEMNUMBERS=?,REWARDQUALITY=?,REWARDBYTE=?,EXISTINGREWARDITEMID=?,REWARDTARGETCONTAINERID=?,REWARDSKILLNUM=?,REWARDSKILLVAL=?,SPECIALEFFECTID=?,SOUND=?,TEXT=?,MISSION=?,MISSIONSTATECHANGE=?,INACTIVE=?,CREATOR=?,CREATEDDATE=?,LASTMODIFIER=?,TRIGGERID=?,DESTROYTARGET=?,ITEMMATERIAL=?,NEWBIE=?,MODIFYTILEX=?,MODIFYTILEY=?,NEWTILETYPE=?,NEWTILEDATA=?,SPAWNTILEX=?,SPAWNTILEY=?,CREATURESPAWN=?,CREATUREAGE=?,CREATURE_TYPE=?,CREATURE_NAME=?,TELEPORTX=?,TELEPORTY=?,MISSIONACTIVATED=?,MISSIONDEACTIVATED=?,TRIGGER_ACTIVATED=?,TRIGGER_DEACTIVATED=?,EFFECT_ACTIVATED=?,EFFECT_DEACTIVATED=?,WSZX=?,WSZY=?,STARTSKILLGAIN=?,STOPSKILLGAIN=?,DESTROYITEMS=?,TOP=?,TELEPORTLAYER=?,ACHIEVEMENTID=? WHERE ID=?");
/* 1584 */       ps.setString(1, this.name);
/* 1585 */       ps.setString(2, this.description);
/* 1586 */       ps.setInt(3, this.rewardItem);
/* 1587 */       ps.setInt(4, this.rewardNumbers);
/* 1588 */       ps.setInt(5, this.rewardQl);
/* 1589 */       ps.setByte(6, this.rewardByteValue);
/* 1590 */       ps.setLong(7, this.existingItemReward);
/* 1591 */       ps.setLong(8, this.rewardTargetContainerId);
/* 1592 */       ps.setInt(9, this.rewardSkillNum);
/* 1593 */       ps.setFloat(10, this.rewardSkillVal);
/* 1594 */       ps.setInt(11, this.specialEffectId);
/* 1595 */       ps.setString(12, this.soundName);
/* 1596 */       ps.setString(13, this.textDisplayed);
/* 1597 */       ps.setInt(14, this.missionId);
/* 1598 */       ps.setFloat(15, this.missionStateChange);
/* 1599 */       ps.setBoolean(16, this.inActive);
/* 1600 */       ps.setString(17, this.creatorName);
/* 1601 */       this.lastModifiedDate = new Timestamp(System.currentTimeMillis());
/* 1602 */       ps.setString(18, this.createdDate);
/* 1603 */       ps.setString(19, this.lastModifierName);
/* 1604 */       ps.setInt(20, this.triggerId);
/* 1605 */       ps.setBoolean(21, this.destroysTarget);
/* 1606 */       ps.setByte(22, this.itemMaterial);
/* 1607 */       ps.setBoolean(23, this.newbieItem);
/*      */       
/* 1609 */       ps.setInt(24, this.modifyTileX);
/* 1610 */       ps.setInt(25, this.modifyTileY);
/* 1611 */       ps.setInt(26, this.newTileType);
/* 1612 */       ps.setByte(27, this.newTileData);
/* 1613 */       ps.setInt(28, this.spawnTileX);
/* 1614 */       ps.setInt(29, this.spawnTileY);
/* 1615 */       ps.setInt(30, this.creatureSpawn);
/* 1616 */       ps.setInt(31, this.creatureAge);
/* 1617 */       ps.setByte(32, this.creatureType);
/* 1618 */       ps.setString(33, this.creatureName);
/*      */       
/* 1620 */       ps.setInt(34, this.teleportX);
/* 1621 */       ps.setInt(35, this.teleportY);
/* 1622 */       ps.setInt(36, this.missionToActivate);
/* 1623 */       ps.setInt(37, this.missionToDeActivate);
/* 1624 */       ps.setInt(38, this.triggerToActivate);
/* 1625 */       ps.setInt(39, this.triggerToDeActivate);
/* 1626 */       ps.setInt(40, this.effectToActivate);
/* 1627 */       ps.setInt(41, this.effectToDeActivate);
/* 1628 */       ps.setInt(42, this.windowSizeX);
/* 1629 */       ps.setInt(43, this.windowSizeY);
/* 1630 */       ps.setBoolean(44, this.startSkillGain);
/* 1631 */       ps.setBoolean(45, this.stopSkillGain);
/* 1632 */       ps.setBoolean(46, this.destroyItems);
/* 1633 */       ps.setString(47, this.topText);
/* 1634 */       ps.setInt(48, this.teleportLayer);
/* 1635 */       ps.setInt(49, this.achievementId);
/* 1636 */       ps.setInt(50, this.id);
/* 1637 */       ps.executeUpdate();
/*      */     }
/* 1639 */     catch (SQLException sqx) {
/*      */       
/* 1641 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1645 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1646 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void create() {
/* 1652 */     Connection dbcon = null;
/* 1653 */     PreparedStatement ps = null;
/* 1654 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1657 */       dbcon = DbConnector.getPlayerDbCon();
/* 1658 */       ps = dbcon.prepareStatement("INSERT INTO TRIGGEREFFECTS (NAME, DESCRIPTION,REWARDITEM,REWARDITEMNUMBERS,REWARDQUALITY,REWARDBYTE,EXISTINGREWARDITEMID,REWARDTARGETCONTAINERID,REWARDSKILLNUM,REWARDSKILLVAL,SPECIALEFFECTID,SOUND,TEXT,MISSION,MISSIONSTATECHANGE,INACTIVE,CREATOR,CREATEDDATE,LASTMODIFIER,TRIGGERID,DESTROYTARGET,CREATORID,CREATORTYPE,ITEMMATERIAL,NEWBIE,MODIFYTILEX,MODIFYTILEY,NEWTILETYPE,NEWTILEDATA,SPAWNTILEX,SPAWNTILEY,CREATURESPAWN,CREATUREAGE,CREATURE_TYPE,CREATURE_NAME,TELEPORTX,TELEPORTY,MISSIONACTIVATED,MISSIONDEACTIVATED,TRIGGER_ACTIVATED,TRIGGER_DEACTIVATED,EFFECT_ACTIVATED,EFFECT_DEACTIVATED,WSZX,WSZY,STARTSKILLGAIN,STOPSKILLGAIN,DESTROYITEMS,TOP,TELEPORTLAYER,ACHIEVEMENTID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/*      */       
/* 1660 */       ps.setString(1, this.name);
/* 1661 */       ps.setString(2, this.description);
/* 1662 */       ps.setInt(3, this.rewardItem);
/* 1663 */       ps.setInt(4, this.rewardNumbers);
/* 1664 */       ps.setInt(5, this.rewardQl);
/* 1665 */       ps.setByte(6, this.rewardByteValue);
/* 1666 */       ps.setLong(7, this.existingItemReward);
/* 1667 */       ps.setLong(8, this.rewardTargetContainerId);
/* 1668 */       ps.setInt(9, this.rewardSkillNum);
/* 1669 */       ps.setFloat(10, this.rewardSkillVal);
/* 1670 */       ps.setInt(11, this.specialEffectId);
/* 1671 */       ps.setString(12, this.soundName);
/* 1672 */       ps.setString(13, this.textDisplayed);
/* 1673 */       ps.setInt(14, this.missionId);
/* 1674 */       ps.setFloat(15, this.missionStateChange);
/* 1675 */       ps.setBoolean(16, this.inActive);
/* 1676 */       ps.setString(17, this.creatorName);
/* 1677 */       this.createdDate = DateFormat.getDateInstance(2).format(new Timestamp(System.currentTimeMillis()));
/* 1678 */       this.lastModifiedDate = new Timestamp(System.currentTimeMillis());
/* 1679 */       ps.setString(18, this.createdDate);
/* 1680 */       ps.setString(19, this.lastModifierName);
/* 1681 */       ps.setInt(20, this.triggerId);
/* 1682 */       ps.setBoolean(21, this.destroysTarget);
/* 1683 */       ps.setLong(22, this.ownerId);
/* 1684 */       ps.setByte(23, this.creatorType);
/* 1685 */       ps.setByte(24, this.itemMaterial);
/* 1686 */       ps.setBoolean(25, this.newbieItem);
/*      */       
/* 1688 */       ps.setInt(26, this.modifyTileX);
/* 1689 */       ps.setInt(27, this.modifyTileY);
/* 1690 */       ps.setInt(28, this.newTileType);
/* 1691 */       ps.setByte(29, this.newTileData);
/* 1692 */       ps.setInt(30, this.spawnTileX);
/* 1693 */       ps.setInt(31, this.spawnTileY);
/* 1694 */       ps.setInt(32, this.creatureSpawn);
/* 1695 */       ps.setInt(33, this.creatureAge);
/* 1696 */       ps.setByte(34, this.creatureType);
/* 1697 */       ps.setString(35, this.creatureName);
/*      */       
/* 1699 */       ps.setInt(36, this.teleportX);
/* 1700 */       ps.setInt(37, this.teleportY);
/* 1701 */       ps.setInt(38, this.missionToActivate);
/* 1702 */       ps.setInt(39, this.missionToDeActivate);
/* 1703 */       ps.setInt(40, this.triggerToActivate);
/* 1704 */       ps.setInt(41, this.triggerToDeActivate);
/* 1705 */       ps.setInt(42, this.effectToActivate);
/* 1706 */       ps.setInt(43, this.effectToDeActivate);
/* 1707 */       ps.setInt(44, this.windowSizeX);
/* 1708 */       ps.setInt(45, this.windowSizeY);
/* 1709 */       ps.setBoolean(46, this.startSkillGain);
/* 1710 */       ps.setBoolean(47, this.stopSkillGain);
/* 1711 */       ps.setBoolean(48, this.destroyItems);
/* 1712 */       ps.setString(49, this.topText);
/* 1713 */       ps.setInt(50, this.teleportLayer);
/* 1714 */       ps.setInt(51, this.achievementId);
/* 1715 */       ps.executeUpdate();
/* 1716 */       rs = ps.getGeneratedKeys();
/* 1717 */       if (rs.next())
/* 1718 */         this.id = rs.getInt(1); 
/* 1719 */       logger.log(Level.INFO, "Trigger effect " + this.name + " (" + this.id + ") created at " + this.createdDate);
/*      */     }
/* 1721 */     catch (SQLException sqx) {
/*      */       
/* 1723 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1727 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1728 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroy() {
/* 1734 */     TriggerEffects.removeEffect(this.id);
/* 1735 */     Triggers2Effects.deleteEffect(this.id);
/*      */     
/* 1737 */     Connection dbcon = null;
/* 1738 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1741 */       dbcon = DbConnector.getPlayerDbCon();
/* 1742 */       ps = dbcon.prepareStatement("DELETE FROM TRIGGEREFFECTS WHERE ID=?");
/* 1743 */       ps.setInt(1, this.id);
/* 1744 */       ps.executeUpdate();
/*      */     }
/* 1746 */     catch (SQLException sqx) {
/*      */       
/* 1748 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1752 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1753 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1755 */     this.destroyed = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDestroyed() {
/* 1760 */     return this.destroyed;
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
/*      */   public int compareTo(TriggerEffect aTriggerEffect) {
/* 1773 */     return getName().compareTo(aTriggerEffect.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAchievementId() {
/* 1783 */     return this.achievementId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAchievementId(int aAchievementId) {
/* 1794 */     this.achievementId = aAchievementId;
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
/*      */   public boolean hasTargetOf(long currentTargetId, Creature performer) {
/* 1806 */     MissionTrigger t = MissionTriggers.getTriggerWithId(this.triggerId);
/* 1807 */     if (t != null)
/* 1808 */       return (t.getTarget() == currentTargetId); 
/* 1809 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\TriggerEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
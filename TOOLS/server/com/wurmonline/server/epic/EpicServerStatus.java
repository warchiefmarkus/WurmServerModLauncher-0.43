/*      */ package com.wurmonline.server.epic;
/*      */ 
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.ActionEntry;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import com.wurmonline.server.creatures.ai.PathTile;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.effects.EffectFactory;
/*      */ import com.wurmonline.server.items.CreationEntry;
/*      */ import com.wurmonline.server.items.CreationMatrix;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.kingdom.GuardTower;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.tutorial.Mission;
/*      */ import com.wurmonline.server.tutorial.MissionPerformed;
/*      */ import com.wurmonline.server.tutorial.MissionPerformer;
/*      */ import com.wurmonline.server.tutorial.MissionTrigger;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.tutorial.Missions;
/*      */ import com.wurmonline.server.tutorial.TriggerEffect;
/*      */ import com.wurmonline.server.tutorial.TriggerEffects;
/*      */ import com.wurmonline.server.tutorial.Triggers2Effects;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.webinterface.WcEpicStatusReport;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import com.wurmonline.shared.util.TerrainUtilities;
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
/*      */ public class EpicServerStatus
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*  101 */   private static final Logger logger = Logger.getLogger(EpicServerStatus.class.getName());
/*      */   
/*  103 */   private static final Set<EpicMission> epicMissions = new HashSet<>();
/*      */   
/*  105 */   private static final EpicMission[] emptyEpicMArr = new EpicMission[0];
/*      */   
/*      */   private static HashMap<Integer, Integer> backupDifficultyMap;
/*      */   
/*      */   private final int serverId;
/*      */   
/*  111 */   private final PathTile notFoundTile = new PathTile(-1, -1, -1, true, 0);
/*      */   
/*      */   static final String LOAD_LOCAL_EPIC_ENTITY_MISSIONS = "SELECT * FROM EPICMISSIONS";
/*      */   
/*      */   private static final int BUILD_TARGET = 0;
/*      */   
/*      */   private static final int USE_TARGET = 1;
/*      */   
/*      */   private static final int USE_TILE = 2;
/*      */   
/*      */   private static final int USE_MANY_TILES = 3;
/*      */   
/*      */   private static final int USE_GUARDTOWER = 4;
/*      */   
/*      */   private static final int DRAIN_SETTLEMENT = 5;
/*      */   
/*      */   private static final int KILL_CREATURES = 6;
/*      */   
/*      */   private static final int SACRIFICE_ITEMS = 7;
/*      */   
/*      */   private static final int BUILD_COMPLEX_ITEM = 8;
/*      */   private static final int BRING_ITEM_TO_CREATURE = 9;
/*      */   private static final int SACRIFICE_CREATURES = 10;
/*      */   public static final byte TYPE_BUILDSTRUCTURE_SP = 101;
/*      */   public static final byte TYPE_BUILDSTRUCTURE_TO = 102;
/*      */   public static final byte TYPE_BUILDSTRUCTURE_SG = 103;
/*      */   public static final byte TYPE_RITUALMS_F = 104;
/*      */   public static final byte TYPE_RITUALMS_E = 105;
/*      */   public static final byte TYPE_CUTTREE_F = 106;
/*      */   public static final byte TYPE_CUTTREE_E = 107;
/*      */   public static final byte TYPE_RITUALGT = 108;
/*      */   public static final byte TYPE_SACMISSION = 109;
/*      */   public static final byte TYPE_SACITEM = 110;
/*      */   public static final byte TYPE_CREATEITEM = 111;
/*      */   public static final byte TYPE_GIVEITEM_F = 112;
/*      */   public static final byte TYPE_GIVEITEM_E = 113;
/*      */   public static final byte TYPE_SLAYCREATURE_P = 114;
/*      */   public static final byte TYPE_SLAYCREATURE_L = 115;
/*      */   public static final byte TYPE_SLAYCREATURE_H = 116;
/*      */   public static final byte TYPE_SLAYTRAITOR_P = 117;
/*      */   public static final byte TYPE_SLAYTRAITOR_L = 118;
/*      */   public static final byte TYPE_SLAYTRAITOR_H = 119;
/*      */   public static final byte TYPE_DESTROYGT = 120;
/*      */   public static final byte TYPE_SACCREATURE_P = 121;
/*      */   public static final byte TYPE_SACCREATURE_L = 122;
/*      */   public static final byte TYPE_SACCREATURE_H = 123;
/*      */   public static final byte TYPE_SLAYTOWERGUARD = 124;
/*  158 */   private int maxTimeSecs = 1000;
/*      */   
/*      */   private static EpicScenario currentScenario;
/*  161 */   private static final List<ItemTemplate> itemplates = new ArrayList<>();
/*      */ 
/*      */   
/*      */   static {
/*  165 */     setupMissionItemTemplates();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EpicServerStatus() {
/*  173 */     this.serverId = Servers.localServer.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   EpicServerStatus(int server) {
/*  181 */     this.serverId = server;
/*      */   }
/*      */   
/*  184 */   private static HexMap valrei = null;
/*      */ 
/*      */   
/*      */   public static final HexMap getValrei() {
/*  188 */     if (valrei == null)
/*  189 */       valrei = new Valrei(); 
/*  190 */     return valrei;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void setupMissionItemTemplates() {
/*  200 */     ItemTemplate[] templates = ItemTemplateFactory.getInstance().getTemplates();
/*  201 */     for (ItemTemplate lTemplate : templates) {
/*      */       
/*  203 */       if (lTemplate.isMissionItem() && !lTemplate.isUseOnGroundOnly() && !lTemplate.isNoTake() && !lTemplate.unique && !lTemplate.artifact && 
/*  204 */         !lTemplate.isRiftLoot() && lTemplate
/*  205 */         .getTemplateId() != 737 && lTemplate.getTemplateId() != 683 && lTemplate
/*  206 */         .getTemplateId() != 1414)
/*      */       {
/*  208 */         itemplates.add(lTemplate);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addMission(EpicMission mission) {
/*  215 */     epicMissions.add(mission);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EpicMission getEpicMissionForMission(int missionId) {
/*  220 */     for (EpicMission em : epicMissions) {
/*      */       
/*  222 */       if (em.getMissionId() == missionId)
/*  223 */         return em; 
/*      */     } 
/*  225 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EpicMission getEpicMissionForEntity(int entityId) {
/*  230 */     for (EpicMission em : epicMissions) {
/*      */ 
/*      */ 
/*      */       
/*  234 */       if (em.getEpicEntityId() == entityId && em.isCurrent())
/*  235 */         return em; 
/*      */     } 
/*  237 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EpicMission[] getEpicMissionsForKingdomTemplate(byte kingdomTemplateId) {
/*  242 */     List<EpicMission> toRet = new ArrayList<>();
/*      */     
/*  244 */     for (EpicMission em : epicMissions) {
/*      */       
/*  246 */       if (em.isCurrent()) {
/*      */         
/*  248 */         Deity d = Deities.translateDeityForEntity(em.getEpicEntityId());
/*  249 */         int deityNum = -1;
/*  250 */         if (d != null)
/*  251 */           deityNum = d.getNumber(); 
/*  252 */         if (Deities.getFavoredKingdom(deityNum) == kingdomTemplateId)
/*  253 */           toRet.add(em); 
/*      */       } 
/*      */     } 
/*  256 */     if (toRet.size() > 0) {
/*  257 */       return toRet.<EpicMission>toArray(new EpicMission[toRet.size()]);
/*      */     }
/*  259 */     return emptyEpicMArr;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EpicMission[] getCurrentEpicMissions() {
/*  264 */     List<EpicMission> toRet = new ArrayList<>();
/*      */     
/*  266 */     for (EpicMission em : epicMissions) {
/*      */       
/*  268 */       if (em.isCurrent())
/*      */       {
/*  270 */         toRet.add(em);
/*      */       }
/*      */     } 
/*  273 */     if (toRet.size() > 0) {
/*  274 */       return toRet.<EpicMission>toArray(new EpicMission[toRet.size()]);
/*      */     }
/*  276 */     return emptyEpicMArr;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EpicMission[] getEpicMissionsForDeity(int deityNum) {
/*  281 */     List<EpicMission> toRet = new ArrayList<>();
/*      */     
/*  283 */     for (EpicMission em : epicMissions) {
/*      */       
/*  285 */       if (em.isCurrent())
/*      */       {
/*  287 */         if (em.getEpicEntityId() == deityNum)
/*  288 */           toRet.add(em); 
/*      */       }
/*      */     } 
/*  291 */     if (toRet.size() > 0) {
/*  292 */       return toRet.<EpicMission>toArray(new EpicMission[toRet.size()]);
/*      */     }
/*  294 */     return emptyEpicMArr;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getServerId() {
/*  299 */     return this.serverId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final EpicScenario getCurrentScenario() {
/*  309 */     return currentScenario;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadLocalEntries() {
/*  314 */     logger.log(Level.INFO, "LOADING LOCAL EPIC MISSIONS");
/*  315 */     currentScenario = new EpicScenario();
/*  316 */     currentScenario.loadCurrentScenario();
/*  317 */     Connection dbcon = null;
/*  318 */     PreparedStatement ps = null;
/*  319 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  322 */       dbcon = DbConnector.getDeityDbCon();
/*  323 */       ps = dbcon.prepareStatement("SELECT * FROM EPICMISSIONS");
/*  324 */       rs = ps.executeQuery();
/*  325 */       while (rs.next())
/*      */       {
/*      */ 
/*      */         
/*  329 */         EpicMission mp = new EpicMission(rs.getInt("ENTITY"), rs.getInt("SCENARIO"), rs.getString("NAME"), rs.getString("SCENARIONAME"), rs.getInt("MISSION"), rs.getByte("MISSIONTYPE"), rs.getInt("DIFFICULTY"), rs.getFloat("PROGRESS"), rs.getInt("SERVERID"), rs.getLong("TSTAMP"), true, rs.getBoolean("CURRENT"));
/*      */         
/*  331 */         addMission(mp);
/*      */       }
/*      */     
/*  334 */     } catch (SQLException sqx) {
/*      */       
/*  336 */       logger.log(Level.WARNING, "Failed to load epic mission.", sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  340 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  341 */       DbConnector.returnConnection(dbcon);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int getRitualAction(byte targetKingdom) {
/*  535 */     if (targetKingdom != 3)
/*      */     {
/*  537 */       return 496 + Server.rand.nextInt(5);
/*      */     }
/*  539 */     return 496 + Server.rand.nextInt(7);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PathTile getTargetTileInProximityTo(PathTile p, int direction, int sizeX, int sizeY, boolean isTree) {
/*  633 */     if (direction == 0)
/*      */     {
/*      */       
/*  636 */       for (int x = p.getTileX(); x < p.getTileX() + sizeX; x++) {
/*  637 */         for (int y = p.getTileY() - sizeY; y < p.getTileY(); y++) {
/*      */ 
/*      */ 
/*      */           
/*  641 */           if (Server.rand.nextInt(Math.max(1, sizeX / 2)) == 0) {
/*      */             
/*  643 */             int tileToCheck = Zones.getTileIntForTile(x, y, 0);
/*  644 */             byte type = Tiles.decodeType(tileToCheck);
/*  645 */             byte data = Tiles.decodeData(tileToCheck);
/*  646 */             Tiles.Tile theTile = Tiles.getTile(type);
/*  647 */             if (isTree) {
/*      */               
/*  649 */               if (theTile.isMyceliumTree() || theTile.isNormalTree())
/*      */               {
/*  651 */                 if (FoliageAge.getAgeAsByte(data) > FoliageAge.MATURE_ONE.getAgeId()) {
/*  652 */                   return new PathTile(x, y, tileToCheck, true, 0);
/*      */                 }
/*      */               }
/*      */             } else {
/*  656 */               return new PathTile(x, y, tileToCheck, true, 0);
/*      */             } 
/*      */           } 
/*      */         } 
/*  660 */       }  }  if (direction == 6)
/*      */     {
/*      */       
/*  663 */       for (int x = p.getTileX() - sizeX; x < p.getTileX(); x++) {
/*  664 */         for (int y = p.getTileY(); y < p.getTileY() + sizeY; y++) {
/*      */ 
/*      */ 
/*      */           
/*  668 */           if (Server.rand.nextInt(Math.max(1, sizeX / 2)) == 0) {
/*      */             
/*  670 */             int tileToCheck = Zones.getTileIntForTile(x, y, 0);
/*  671 */             byte type = Tiles.decodeType(tileToCheck);
/*  672 */             byte data = Tiles.decodeData(tileToCheck);
/*  673 */             Tiles.Tile theTile = Tiles.getTile(type);
/*  674 */             if (isTree) {
/*      */               
/*  676 */               if (theTile.isMyceliumTree() || theTile.isNormalTree())
/*      */               {
/*  678 */                 if (FoliageAge.getAgeAsByte(data) > FoliageAge.MATURE_ONE.getAgeId()) {
/*  679 */                   return new PathTile(x, y, tileToCheck, true, 0);
/*      */                 }
/*      */               }
/*      */             } else {
/*  683 */               return new PathTile(x, y, tileToCheck, true, 0);
/*      */             } 
/*      */           } 
/*      */         } 
/*  687 */       }  }  if (direction == 2)
/*      */     {
/*      */       
/*  690 */       for (int x = p.getTileX(); x < p.getTileX() + sizeX; x++) {
/*  691 */         for (int y = p.getTileY(); y < p.getTileY() + sizeY; y++) {
/*      */ 
/*      */ 
/*      */           
/*  695 */           if (Server.rand.nextInt(Math.max(1, sizeX / 2)) == 0) {
/*      */             
/*  697 */             int tileToCheck = Zones.getTileIntForTile(x, y, 0);
/*  698 */             byte type = Tiles.decodeType(tileToCheck);
/*  699 */             byte data = Tiles.decodeData(tileToCheck);
/*  700 */             Tiles.Tile theTile = Tiles.getTile(type);
/*  701 */             if (isTree) {
/*      */               
/*  703 */               if (theTile.isMyceliumTree() || theTile.isNormalTree())
/*      */               {
/*  705 */                 if (FoliageAge.getAgeAsByte(data) > FoliageAge.MATURE_ONE.getAgeId()) {
/*  706 */                   return new PathTile(x, y, tileToCheck, true, 0);
/*      */                 }
/*      */               }
/*      */             } else {
/*  710 */               return new PathTile(x, y, tileToCheck, true, 0);
/*      */             } 
/*      */           } 
/*      */         } 
/*  714 */       }  }  if (direction == 4)
/*      */     {
/*      */       
/*  717 */       for (int x = p.getTileX(); x < p.getTileX() + sizeX; x++) {
/*  718 */         for (int y = p.getTileY(); y < p.getTileY() + sizeY; y++) {
/*      */ 
/*      */ 
/*      */           
/*  722 */           if (Server.rand.nextInt(Math.max(1, sizeX / 2)) == 0) {
/*      */             
/*  724 */             int tileToCheck = Zones.getTileIntForTile(x, y, 0);
/*  725 */             byte type = Tiles.decodeType(tileToCheck);
/*  726 */             byte data = Tiles.decodeData(tileToCheck);
/*  727 */             Tiles.Tile theTile = Tiles.getTile(type);
/*  728 */             if (isTree) {
/*      */               
/*  730 */               if (theTile.isMyceliumTree() || theTile.isNormalTree())
/*      */               {
/*  732 */                 if (FoliageAge.getAgeAsByte(data) > FoliageAge.MATURE_ONE.getAgeId()) {
/*  733 */                   return new PathTile(x, y, tileToCheck, true, 0);
/*      */                 }
/*      */               }
/*      */             } else {
/*  737 */               return new PathTile(x, y, tileToCheck, true, 0);
/*      */             } 
/*      */           } 
/*      */         } 
/*  741 */       }  }  return this.notFoundTile;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long getTileId(int x, int y) {
/*  746 */     return Tiles.getTileId(x, y, 0);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String generateNeedString(int difficulty) {
/* 1270 */     switch (difficulty)
/*      */     
/*      */     { case 1:
/* 1273 */         needString = " asks ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1296 */         return needString;case 2: needString = " wants "; return needString;case 3: needString = " requires "; return needString;case 4: needString = " needs "; return needString;case 5: needString = " urges "; return needString;case 6: needString = " demands "; return needString;case 7: needString = " commands "; return needString; }  String needString = " needs "; return needString;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void linkMission(Mission m, MissionTrigger trig, TriggerEffect effect, byte missionType, int difficulty) {
/* 1472 */     m.update();
/* 1473 */     Missions.addMission(m);
/* 1474 */     trig.setMissionRequirement(m.getId());
/* 1475 */     trig.create();
/* 1476 */     MissionTriggers.addMissionTrigger(trig);
/* 1477 */     effect.setTrigger(trig.getId());
/* 1478 */     effect.setMission(m.getId());
/* 1479 */     effect.setStopSkillgain(false);
/* 1480 */     effect.setStartSkillgain(false);
/* 1481 */     effect.create();
/* 1482 */     Triggers2Effects.addLink(trig.getId(), effect.getId(), false);
/* 1483 */     TriggerEffects.addTriggerEffect(effect);
/* 1484 */     for (EpicMission em : epicMissions) {
/*      */       
/* 1486 */       if (em.isCurrent() && em.getEpicEntityId() == (int)m.getOwnerId()) {
/*      */         
/* 1488 */         em.setCurrent(false);
/* 1489 */         em.update();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1494 */     EpicMission mp = new EpicMission((int)m.getOwnerId(), currentScenario.getScenarioNumber(), m.getName(), currentScenario.getScenarioName(), m.getId(), missionType, difficulty, 1.0F, Servers.localServer.id, System.currentTimeMillis() + this.maxTimeSecs * 1000L, false, true);
/*      */     
/* 1496 */     addMission(mp);
/* 1497 */     Players.getInstance().sendUpdateEpicMission(mp);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getAreaString(int tilex, int tiley) {
/* 1502 */     StringBuilder sbuild = new StringBuilder();
/* 1503 */     if (tiley < Zones.worldTileSizeY / 3) {
/* 1504 */       sbuild.append("north");
/* 1505 */     } else if (tiley > Zones.worldTileSizeY - Zones.worldTileSizeY / 3) {
/* 1506 */       sbuild.append("south");
/*      */     } else {
/* 1508 */       sbuild.append("center");
/* 1509 */     }  if (tilex < Zones.worldTileSizeX / 3) {
/* 1510 */       sbuild.append("west");
/* 1511 */     } else if (tilex > Zones.worldTileSizeX - Zones.worldTileSizeX / 3) {
/* 1512 */       sbuild.append("east");
/* 1513 */     }  sbuild.append(" regions");
/* 1514 */     return sbuild.toString();
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
/*      */   private final ItemTemplate getRandomItemTemplateUsed() {
/* 1527 */     return itemplates.get(Server.rand.nextInt(itemplates.size()));
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
/*      */   private final MissionTrigger initializeMissionTrigger(int epicEntityId, String epicEntityName) {
/* 1540 */     MissionTrigger trig = new MissionTrigger();
/* 1541 */     trig.setStateRequirement(0.0F);
/* 1542 */     trig.setName(epicEntityName + "Auto" + Server.rand.nextInt());
/* 1543 */     trig.setCreatorType((byte)2);
/* 1544 */     trig.setCreatorName("System");
/* 1545 */     trig.setLastModifierName(epicEntityName);
/* 1546 */     trig.setOwnerId(epicEntityId);
/* 1547 */     return trig;
/*      */   }
/*      */ 
/*      */   
/*      */   private final TriggerEffect initializeTriggerEffect(int epicEntityId, String epicEntityName) {
/* 1552 */     TriggerEffect effect = new TriggerEffect();
/* 1553 */     effect.setName(epicEntityName + "Auto" + Server.rand.nextInt());
/* 1554 */     effect.setCreatorType((byte)2);
/* 1555 */     effect.setCreatorName("System");
/* 1556 */     effect.setLastModifierName(epicEntityName);
/* 1557 */     effect.setTopText("Mission progress");
/* 1558 */     effect.setTextDisplayed(epicEntityName + " is pleased. You do your part well.");
/* 1559 */     effect.setSoundName("sound.music.song.spawn1");
/* 1560 */     effect.setOwnerId(epicEntityId);
/* 1561 */     return effect;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void deleteMission(EpicMission mission) {
/* 1566 */     epicMissions.remove(mission);
/* 1567 */     mission.delete();
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void destroyLastMissionForEntity(int epicEntityId) {
/* 1572 */     for (EpicMission em : epicMissions) {
/*      */       
/* 1574 */       if (em.isCurrent() && em.getEpicEntityId() == epicEntityId) {
/* 1575 */         destroySpecificMission(em);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final void destroySpecificMission(EpicMission em) {
/* 1581 */     boolean failed = false;
/* 1582 */     if (!em.isCompleted()) {
/*      */       
/* 1584 */       failed = true;
/* 1585 */       em.updateProgress(-1.0F);
/*      */     } 
/* 1587 */     em.setCurrent(false);
/* 1588 */     em.update();
/*      */     
/* 1590 */     if (Servers.localServer.EPIC) {
/* 1591 */       Players.getInstance().sendUpdateEpicMission(em);
/*      */     }
/* 1593 */     Mission[] missions = Missions.getAllMissions();
/* 1594 */     MissionTrigger[] triggers = MissionTriggers.getAllTriggers();
/* 1595 */     TriggerEffect[] effects = TriggerEffects.getAllEffects();
/* 1596 */     for (Mission m : missions) {
/*      */       
/* 1598 */       if (m.getCreatorType() == 2 && m.getOwnerId() == em.getEpicEntityId())
/*      */       {
/* 1600 */         if (!m.isInactive()) {
/*      */           
/* 1602 */           if (failed) {
/*      */             
/* 1604 */             MissionPerformer[] allperfs = MissionPerformed.getAllPerformers();
/* 1605 */             for (MissionPerformer mp : allperfs) {
/*      */               
/* 1607 */               MissionPerformed thisMission = mp.getMission(m.getId());
/* 1608 */               if (thisMission != null)
/*      */               {
/*      */                 
/* 1611 */                 thisMission.setState(-1.0F, mp.getWurmId());
/*      */               }
/*      */             } 
/*      */           } 
/* 1615 */           m.setInactive(true);
/* 1616 */           for (MissionTrigger t : triggers) {
/*      */             
/* 1618 */             if (t.getMissionRequired() == m.getId()) {
/*      */               
/* 1620 */               TriggerEffects.destroyEffectsForTrigger(t.getId());
/* 1621 */               t.destroy();
/*      */             } 
/*      */           } 
/* 1624 */           for (TriggerEffect e : effects) {
/*      */             
/* 1626 */             if (e.getMissionId() == m.getId()) {
/* 1627 */               e.destroy();
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final void pollExpiredMissions() {
/* 1636 */     HashSet<Integer> deities = new HashSet<>();
/* 1637 */     for (EpicMission m : getCurrentEpicMissions()) {
/*      */       
/* 1639 */       Mission mis = Missions.getMissionWithId(m.getMissionId());
/* 1640 */       if (m.isCurrent()) {
/* 1641 */         deities.add(Integer.valueOf(m.getEpicEntityId()));
/*      */       }
/* 1643 */       if (m.isCurrent() && System.currentTimeMillis() > m.getExpireTime()) {
/*      */         
/* 1645 */         int dietyNum = m.getEpicEntityId();
/* 1646 */         destroyLastMissionForEntity(dietyNum);
/*      */         
/* 1648 */         WcEpicStatusReport wce = new WcEpicStatusReport(WurmId.getNextWCCommandId(), false, dietyNum, m.getMissionType(), m.getDifficulty());
/* 1649 */         wce.sendToLoginServer();
/* 1650 */         storeLastMissionForEntity(m.getEpicEntityId(), m);
/*      */ 
/*      */ 
/*      */         
/* 1654 */         if (!Servers.localServer.EPIC)
/*      */         {
/* 1656 */           if (dietyNum > 0 && dietyNum <= 4) {
/*      */             
/* 1658 */             EpicServerStatus es = new EpicServerStatus();
/* 1659 */             if (getCurrentScenario() == null)
/* 1660 */               loadLocalEntries(); 
/* 1661 */             if (getCurrentScenario() != null) {
/* 1662 */               es.generateNewMissionForEpicEntity(dietyNum, Deities.getDeityName(dietyNum), Math.max(1, m.getDifficulty() - 2), 604800, 
/* 1663 */                   getCurrentScenario().getScenarioName(), getCurrentScenario()
/* 1664 */                   .getScenarioNumber(), 
/* 1665 */                   getCurrentScenario().getScenarioQuest(), true);
/*      */             }
/*      */           } 
/*      */         }
/* 1669 */       } else if (mis != null && !Servers.localServer.EPIC && m.isCurrent() && 
/* 1670 */         System.currentTimeMillis() > mis.getLastModifiedAsLong() + 302400000L) {
/*      */ 
/*      */         
/* 1673 */         if (m.getMissionProgress() < 33.0F) {
/*      */           
/* 1675 */           EpicMissionEnum missionEnum = EpicMissionEnum.getMissionForType(m.getMissionType());
/* 1676 */           if ((missionEnum.isKarmaMultProgress() || EpicMissionEnum.isRitualMission(missionEnum)) && m
/* 1677 */             .getExpireTime() > System.currentTimeMillis() + 43200000L) {
/* 1678 */             m.setExpireTime(System.currentTimeMillis() + 43200000L);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1683 */     Map<Integer, String> entityMap = Deities.getEntities();
/* 1684 */     for (Map.Entry<Integer, String> entry : entityMap.entrySet()) {
/*      */       
/* 1686 */       boolean found = false;
/* 1687 */       for (Integer i : deities) {
/*      */         
/* 1689 */         if (((Integer)entry.getKey()).intValue() == i.intValue())
/* 1690 */           found = true; 
/*      */       } 
/* 1692 */       if (!found) {
/*      */         
/* 1694 */         EpicMission m = getEpicMissionForEntity(((Integer)entry.getKey()).intValue());
/* 1695 */         if (m != null) {
/*      */           
/* 1697 */           destroyLastMissionForEntity(((Integer)entry.getKey()).intValue());
/*      */           
/* 1699 */           WcEpicStatusReport wce = new WcEpicStatusReport(WurmId.getNextWCCommandId(), false, ((Integer)entry.getKey()).intValue(), m.getMissionType(), m.getDifficulty());
/* 1700 */           wce.sendToLoginServer();
/* 1701 */           storeLastMissionForEntity(m.getEpicEntityId(), m);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void generateNewMissionForEpicEntity(int epicEntityId, String epicEntityName, int baseDifficulty, int maxTimeSeconds, String scenarioNameId, int scenarioIdentity, String questReasonString, boolean destroyPreviousMission) {
/* 1711 */     int attempt = 0;
/* 1712 */     boolean error = true;
/* 1713 */     String creationResponse = "";
/*      */     
/* 1715 */     while (error && attempt++ < 10) {
/*      */       
/* 1717 */       if (baseDifficulty == -1) {
/*      */ 
/*      */ 
/*      */         
/* 1721 */         EpicMission em = getEpicMissionForEntity(epicEntityId);
/* 1722 */         if (em != null) {
/* 1723 */           baseDifficulty = em.getDifficulty();
/*      */         } else {
/* 1725 */           baseDifficulty = 1;
/*      */         } 
/* 1727 */       } else if (baseDifficulty == -2 || baseDifficulty == -3) {
/*      */ 
/*      */ 
/*      */         
/* 1731 */         EpicEntity e = getValrei().getEntity(epicEntityId);
/* 1732 */         if (e != null && e.getLatestMissionDifficulty() != -10L) {
/*      */           
/* 1734 */           if (baseDifficulty == -2) {
/*      */             
/* 1736 */             baseDifficulty = e.getLatestMissionDifficulty();
/* 1737 */             if (Server.rand.nextInt(Math.max(1, baseDifficulty)) == 0) {
/* 1738 */               baseDifficulty++;
/*      */             }
/*      */           } else {
/* 1741 */             baseDifficulty = e.getLatestMissionDifficulty() - 2;
/*      */           }
/*      */         
/*      */         }
/* 1745 */         else if (backupDifficultyMap == null || !backupDifficultyMap.containsKey(Integer.valueOf(epicEntityId))) {
/*      */           
/* 1747 */           baseDifficulty = 1;
/* 1748 */           logger.log(Level.WARNING, "Error getting proper difficulty of new mission for " + epicEntityName + " sent from login server. Empty backup map.");
/*      */         }
/*      */         else {
/*      */           
/* 1752 */           if (baseDifficulty == -2) {
/*      */             
/* 1754 */             baseDifficulty = ((Integer)backupDifficultyMap.get(Integer.valueOf(epicEntityId))).intValue();
/* 1755 */             if (Server.rand.nextInt(Math.max(1, baseDifficulty)) == 0) {
/* 1756 */               baseDifficulty++;
/*      */             }
/*      */           } else {
/* 1759 */             baseDifficulty = ((Integer)backupDifficultyMap.get(Integer.valueOf(epicEntityId))).intValue() - 1;
/* 1760 */           }  logger.log(Level.WARNING, "Error getting proper difficulty of new mission for " + epicEntityName + " sent from login server. Used backup value.");
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1766 */       baseDifficulty = Math.max(1, Math.min(7, baseDifficulty));
/* 1767 */       if (!destroyPreviousMission)
/*      */       {
/* 1769 */         for (EpicMission epicMission : getCurrentEpicMissions()) {
/*      */ 
/*      */           
/* 1772 */           if (epicMission.getEpicEntityId() == epicEntityId && System.currentTimeMillis() < epicMission.getExpireTime() && epicMission
/* 1773 */             .getMissionProgress() < 100.0F)
/*      */             return; 
/*      */         } 
/*      */       }
/* 1777 */       destroyLastMissionForEntity(epicEntityId);
/*      */       
/* 1779 */       Deity d = Deities.translateDeityForEntity(epicEntityId);
/* 1780 */       int deityNumber = (d == null) ? -1 : d.getNumber();
/* 1781 */       byte favoredKingdomId = Deities.getFavoredKingdom(deityNumber);
/* 1782 */       boolean battlegroundServer = (Servers.localServer.PVPSERVER && !Servers.localServer.HOMESERVER);
/* 1783 */       boolean enemyHomeServer = (Servers.localServer.EPIC && Servers.localServer.HOMESERVER && Servers.localServer.KINGDOM != favoredKingdomId);
/*      */       
/* 1785 */       boolean friendlyHomeServer = (Servers.localServer.HOMESERVER && !enemyHomeServer);
/*      */       
/* 1787 */       EpicMissionEnum newMission = EpicMissionEnum.getRandomMission(baseDifficulty, battlegroundServer, friendlyHomeServer, enemyHomeServer);
/*      */ 
/*      */       
/* 1790 */       if (newMission == null) {
/*      */         return;
/*      */       }
/* 1793 */       if ((newMission.getMissionType() == 121 || newMission.getMissionType() == 122 || newMission
/* 1794 */         .getMissionType() == 123) && deityNumber == 4 && !Servers.localServer.EPIC && 
/* 1795 */         !Servers.localServer.isChaosServer())
/* 1796 */         newMission = null; 
/* 1797 */       if (newMission == null) {
/*      */         
/* 1799 */         int tries = 0;
/* 1800 */         while (newMission == null && tries < 30) {
/*      */           
/* 1802 */           newMission = EpicMissionEnum.getRandomMission(baseDifficulty, battlegroundServer, friendlyHomeServer, enemyHomeServer);
/*      */           
/* 1804 */           if ((newMission.getMissionType() == 121 || newMission.getMissionType() == 122 || newMission
/* 1805 */             .getMissionType() == 123) && deityNumber == 4 && !Servers.localServer.EPIC && 
/* 1806 */             !Servers.localServer.isChaosServer()) {
/* 1807 */             newMission = null;
/*      */           }
/* 1809 */           tries++;
/*      */         } 
/*      */         
/* 1812 */         if (newMission == null) {
/*      */           
/* 1814 */           logger.warning("Failed to create new mission for " + epicEntityName + " (difficulty=" + baseDifficulty + ")");
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1819 */       byte targetKingdom = favoredKingdomId;
/* 1820 */       if (friendlyHomeServer || enemyHomeServer)
/* 1821 */         targetKingdom = Servers.localServer.KINGDOM; 
/* 1822 */       if (friendlyHomeServer)
/* 1823 */         favoredKingdomId = Servers.localServer.KINGDOM; 
/* 1824 */       if (battlegroundServer && newMission.isEnemyTerritory()) {
/*      */         
/* 1826 */         byte[] enemyKingdoms = getEnemyKingdoms(epicEntityId, favoredKingdomId);
/* 1827 */         if (enemyKingdoms.length > 0) {
/* 1828 */           targetKingdom = enemyKingdoms[Server.rand.nextInt(enemyKingdoms.length)];
/*      */         } else {
/*      */           continue;
/*      */         } 
/*      */       } 
/* 1833 */       Mission m = new Mission("System", epicEntityName);
/* 1834 */       m.setCreatorType((byte)2);
/* 1835 */       m.setMaxTimeSeconds(maxTimeSeconds);
/* 1836 */       m.setOwnerId(epicEntityId);
/* 1837 */       m.setLastModifierName(epicEntityName);
/* 1838 */       this.maxTimeSecs = maxTimeSeconds;
/*      */       
/* 1840 */       switch (newMission.getMissionType()) {
/*      */         
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/* 1845 */           creationResponse = createBuildStructureMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 104:
/*      */         case 105:
/* 1849 */           creationResponse = createMSRitualMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 106:
/*      */         case 107:
/* 1853 */           creationResponse = createCutTreeMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 108:
/* 1856 */           creationResponse = createGTRitualMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 109:
/* 1859 */           creationResponse = createMISacrificeMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 110:
/* 1862 */           creationResponse = createGenericSacrificeMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 111:
/* 1865 */           creationResponse = createCreateItemMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 112:
/*      */         case 113:
/* 1869 */           creationResponse = createGiveItemMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 114:
/*      */         case 115:
/*      */         case 116:
/* 1874 */           creationResponse = createSlayCreatureMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 117:
/*      */         case 118:
/*      */         case 119:
/* 1879 */           creationResponse = createSlayTraitorMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 120:
/* 1882 */           creationResponse = createDestroyGTMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 121:
/*      */         case 122:
/*      */         case 123:
/* 1887 */           creationResponse = createSacrificeCreatureMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */         case 124:
/* 1890 */           creationResponse = createSlayTowerGuardsMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, targetKingdom);
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 1895 */       error = (creationResponse.contains("Error") || creationResponse.contains("error"));
/*      */     } 
/*      */     
/* 1898 */     logger.log(error ? Level.WARNING : Level.INFO, creationResponse);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createBuildStructureMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 1904 */     int targetItem = 712;
/* 1905 */     switch (newMission.getMissionType()) {
/*      */       
/*      */       case 101:
/* 1908 */         if (Server.rand.nextBoolean())
/* 1909 */           targetItem = 717; 
/*      */         break;
/*      */       case 102:
/* 1912 */         if (Server.rand.nextBoolean()) {
/* 1913 */           targetItem = 715; break;
/*      */         } 
/* 1915 */         targetItem = 714;
/*      */         break;
/*      */       case 103:
/* 1918 */         if (Server.rand.nextBoolean()) {
/* 1919 */           targetItem = 713; break;
/*      */         } 
/* 1921 */         targetItem = 716;
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1927 */       m.setName(getMissionName(epicEntityName, newMission));
/* 1928 */       m.create();
/*      */       
/* 1930 */       ItemTemplate targetTemplate = ItemTemplateFactory.getInstance().getTemplate(targetItem);
/* 1931 */       String actionString = Item.getMaterialString(targetTemplate.getMaterial()) + " " + targetTemplate.getName();
/* 1932 */       int placementLocation = EpicTargetItems.getTargetItemPlacement(m.getId());
/* 1933 */       String location = EpicTargetItems.getTargetItemPlacementString(placementLocation);
/* 1934 */       String requirement = EpicTargetItems.getInstructionStringForKingdom(targetItem, favoredKingdomId);
/*      */       
/* 1936 */       TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 1937 */       effect.setDescription("Create " + actionString);
/* 1938 */       effect.setMissionStateChange(100.0F);
/* 1939 */       effect.setTopText("Mission complete!");
/* 1940 */       effect.setTextDisplayed("The " + targetTemplate.getName() + " is complete! " + epicEntityName + " is pleased.");
/*      */ 
/*      */       
/* 1943 */       String missionInstruction = epicEntityName + generateNeedString(baseDifficulty) + "you to construct " + targetTemplate.getNameWithGenus() + '.' + ' ' + requirement + ' ' + location;
/*      */       
/* 1945 */       MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 1946 */       trig.setDescription("Create " + actionString);
/* 1947 */       trig.setOnActionPerformed(148);
/* 1948 */       trig.setOnItemUsedId(targetItem);
/* 1949 */       m.setInstruction(missionInstruction.toString());
/*      */       
/* 1951 */       linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */       
/* 1953 */       return "Build structure mission successfully created for " + epicEntityName + ".";
/*      */     }
/* 1955 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 1957 */       logger.log(Level.WARNING, nst.getMessage());
/* 1958 */       return "Error when creating build structure mission for " + epicEntityName + ".";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String createGenericRitualMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, Item ritualTargetItem, String targetName) {
/* 1966 */     if (ritualTargetItem == null) {
/* 1967 */       return "Error creating generic ritual mission for " + epicEntityName + ". Null target item.";
/*      */     }
/* 1969 */     m.setName(getMissionName(epicEntityName, newMission));
/* 1970 */     m.create();
/*      */     
/* 1972 */     String location = getAreaString(ritualTargetItem.getTileX(), ritualTargetItem.getTileY());
/* 1973 */     int action = getRitualAction(favoredKingdomId);
/* 1974 */     ActionEntry e = Actions.actionEntrys[action];
/* 1975 */     String actionString = "perform the " + e.getActionString();
/* 1976 */     int numbers = getNumberRequired(baseDifficulty, newMission);
/*      */     
/* 1978 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 1979 */     effect.setDescription(actionString);
/* 1980 */     effect.setMissionStateChange(100.0F / numbers);
/* 1981 */     effect.setTopText("Mission complete!");
/* 1982 */     effect.setTextDisplayed(epicEntityName + " is pleased.");
/*      */     
/* 1984 */     StringBuilder sbuild = new StringBuilder(epicEntityName + generateNeedString(baseDifficulty) + numbers + " of you to " + actionString + " at the " + targetName);
/*      */     
/* 1986 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 1987 */     if (baseDifficulty > 1) {
/*      */       
/* 1989 */       ItemTemplate it = getRandomItemTemplateUsed();
/* 1990 */       trig.setOnItemUsedId(it.getTemplateId());
/* 1991 */       sbuild.append(" using " + it.getNameWithGenus());
/*      */     } 
/* 1993 */     sbuild.append(". It is located in the " + location + ".");
/* 1994 */     trig.setDescription(actionString);
/* 1995 */     trig.setOnActionPerformed(action);
/* 1996 */     trig.setOnTargetId(ritualTargetItem.getWurmId());
/* 1997 */     m.setInstruction(sbuild.toString());
/*      */     
/* 1999 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2001 */     return "Ritual mission successfully created for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createMSRitualMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2007 */     Item ritualTargetItem = null;
/* 2008 */     int itemFindAttempts = 0;
/* 2009 */     while (ritualTargetItem == null) {
/*      */       
/* 2011 */       ritualTargetItem = EpicTargetItems.getRandomRitualTarget();
/* 2012 */       if (ritualTargetItem != null) {
/*      */         
/* 2014 */         VolaTile t = Zones.getTileOrNull(ritualTargetItem.getTilePos(), ritualTargetItem.isOnSurface());
/* 2015 */         if (t != null) {
/*      */           
/* 2017 */           if (t.getVillage() != null || t.getStructure() != null)
/* 2018 */             ritualTargetItem = null; 
/* 2019 */           if (t.getKingdom() != targetKingdom) {
/* 2020 */             ritualTargetItem = null;
/*      */           }
/*      */         } 
/*      */       } 
/* 2024 */       if (ritualTargetItem == null && ++itemFindAttempts > 50) {
/* 2025 */         return "Error finding correct target item for ritual mission for " + epicEntityName + ".";
/*      */       }
/*      */     } 
/* 2028 */     return createGenericRitualMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, ritualTargetItem, ritualTargetItem.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createGTRitualMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2034 */     Item ritualTargetItem = null;
/* 2035 */     int itemFindAttempts = 0;
/* 2036 */     String targetName = "";
/* 2037 */     while (ritualTargetItem == null && itemFindAttempts++ < 50) {
/*      */       
/* 2039 */       GuardTower tower = Kingdoms.getRandomTowerForKingdom(targetKingdom);
/* 2040 */       if (tower == null) {
/*      */         continue;
/*      */       }
/* 2043 */       ritualTargetItem = tower.getTower();
/* 2044 */       if (ritualTargetItem != null) {
/*      */         
/* 2046 */         if (ritualTargetItem.getKingdom() != targetKingdom) {
/*      */           
/* 2048 */           ritualTargetItem = null;
/*      */           continue;
/*      */         } 
/* 2051 */         VolaTile t = Zones.getTileOrNull(ritualTargetItem.getTilePos(), ritualTargetItem.isOnSurface());
/* 2052 */         if (t != null)
/*      */         {
/* 2054 */           if (t.getVillage() != null || t.getStructure() != null) {
/* 2055 */             ritualTargetItem = null;
/*      */           }
/*      */         }
/* 2058 */         targetName = Kingdoms.getNameFor(targetKingdom) + " guard tower (" + tower.getName() + ")";
/*      */       } 
/*      */     } 
/* 2061 */     if (ritualTargetItem == null) {
/* 2062 */       return "Error finding correct target item for guard tower ritual mission for " + epicEntityName + ".";
/*      */     }
/* 2064 */     return createGenericRitualMission(m, newMission, epicEntityId, epicEntityName, baseDifficulty, favoredKingdomId, ritualTargetItem, targetName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String createMISacrificeMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/*      */     try {
/* 2072 */       int required = getNumberRequired(baseDifficulty, newMission);
/* 2073 */       ItemTemplate usedTemplate = ItemTemplateFactory.getInstance().getTemplate(737);
/*      */       
/* 2075 */       m.setName(getMissionName(epicEntityName, newMission));
/* 2076 */       m.create();
/*      */       
/* 2078 */       String itemName = HexMap.generateFirstName(m.getId()) + ' ' + HexMap.generateSecondName(m.getId());
/* 2079 */       itemName = itemName + ((required > 1 && !itemName.endsWith("s")) ? "s" : "");
/* 2080 */       int action = 142;
/* 2081 */       String actionString = "Sacrifice";
/*      */       
/* 2083 */       TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2084 */       effect.setTopText("Mission progress");
/* 2085 */       effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2086 */       effect.setDescription("Sacrifice");
/* 2087 */       effect.setMissionStateChange(100.0F / required);
/*      */       
/* 2089 */       StringBuilder sbuild = new StringBuilder(epicEntityName + generateNeedString(baseDifficulty) + "you to sacrifice " + required + " of the hidden " + itemName + " which can be found by investigating and digging in the wilderness.");
/*      */ 
/*      */       
/* 2092 */       MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2093 */       trig.setDescription("Sacrifice");
/* 2094 */       trig.setOnActionPerformed(142);
/* 2095 */       trig.setOnTargetId(-10L);
/* 2096 */       trig.setOnItemUsedId(usedTemplate.getTemplateId());
/* 2097 */       m.setInstruction(sbuild.toString());
/*      */       
/* 2099 */       linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */       
/* 2101 */       return "Created sacrifice mission item mission for " + epicEntityName + ".";
/*      */     }
/* 2103 */     catch (NoSuchTemplateException nst) {
/*      */       
/* 2105 */       return "Error creating sacrifice mission item mission for " + epicEntityName + ". Unable to find template.";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createGenericSacrificeMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2112 */     int required = getNumberRequired(baseDifficulty, newMission);
/* 2113 */     ItemTemplate usedTemplate = null;
/* 2114 */     ItemTemplate[] templates = ItemTemplateFactory.getInstance().getEpicMissionTemplates();
/* 2115 */     if (templates.length == 0)
/* 2116 */       return "Error creating generic sacrifice mission for " + epicEntityName + ". Failed to load templates."; 
/* 2117 */     boolean found = false;
/*      */     
/*      */     try {
/* 2120 */       ItemTemplate altar = ItemTemplateFactory.getInstance().getTemplate(322);
/* 2121 */       while (!found) {
/*      */         
/* 2123 */         usedTemplate = templates[Server.rand.nextInt(templates.length)];
/* 2124 */         if (usedTemplate.getSizeZ() <= altar.getSizeZ() && usedTemplate.getSizeY() <= altar.getSizeY() && usedTemplate
/* 2125 */           .getSizeX() <= altar.getSizeX())
/*      */         {
/* 2127 */           CreationEntry ce = CreationMatrix.getInstance().getCreationEntry(usedTemplate.getTemplateId());
/* 2128 */           if (ce != null) {
/*      */             
/* 2130 */             required /= Math.min(100, ce.getTotalNumberOfItems());
/* 2131 */             if (ce.depleteSource || ce.depleteEqually) {
/*      */               
/* 2133 */               CreationEntry ceSource = CreationMatrix.getInstance().getCreationEntry(ce.getObjectSource());
/* 2134 */               if (ceSource != null && ceSource instanceof com.wurmonline.server.items.AdvancedCreationEntry)
/* 2135 */                 required /= Math.min(100, ceSource.getTotalNumberOfItems()); 
/*      */             } 
/* 2137 */             if (ce.depleteTarget || ce.depleteEqually) {
/*      */               
/* 2139 */               CreationEntry ceTarg = CreationMatrix.getInstance().getCreationEntry(ce.getObjectTarget());
/* 2140 */               if (ceTarg != null && ceTarg instanceof com.wurmonline.server.items.AdvancedCreationEntry) {
/* 2141 */                 required /= Math.min(100, ceTarg.getTotalNumberOfItems());
/*      */               }
/*      */             } 
/*      */           } 
/* 2145 */           found = true;
/*      */         }
/*      */       
/*      */       } 
/* 2149 */     } catch (NoSuchTemplateException nst) {
/*      */       
/* 2151 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     } 
/* 2153 */     required = Math.max(required, 1);
/*      */     
/* 2155 */     if (usedTemplate == null) {
/* 2156 */       return "Error creating generic sacrifice mission for " + epicEntityName + ". Null template.";
/*      */     }
/* 2158 */     m.setName(getMissionName(epicEntityName, newMission));
/* 2159 */     m.create();
/*      */     
/* 2161 */     String itemName = " decent " + usedTemplate.sizeString;
/*      */     
/* 2163 */     itemName = itemName + ((required > 1) ? usedTemplate.getPlural() : usedTemplate.getName());
/*      */     
/* 2165 */     int action = 142;
/* 2166 */     String actionString = "Sacrifice";
/*      */     
/* 2168 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2169 */     effect.setTopText("Mission progress");
/* 2170 */     effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2171 */     effect.setDescription("Sacrifice");
/* 2172 */     effect.setMissionStateChange(100.0F / required);
/*      */     
/* 2174 */     StringBuilder sbuild = new StringBuilder(epicEntityName + generateNeedString(baseDifficulty) + "you to sacrifice " + required + itemName + ".");
/*      */     
/* 2176 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2177 */     trig.setDescription("Sacrifice");
/* 2178 */     trig.setOnActionPerformed(142);
/* 2179 */     trig.setOnTargetId(-10L);
/* 2180 */     trig.setOnItemUsedId(usedTemplate.getTemplateId());
/* 2181 */     m.setInstruction(sbuild.toString());
/*      */     
/* 2183 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2185 */     return "Created sacrifice mission item mission for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createCutTreeMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2191 */     int action = 492;
/*      */     
/* 2193 */     if ((Villages.getVillages()).length == 0) {
/* 2194 */       return "Error creating cut down tree mission for " + epicEntityName + ". No villages.";
/*      */     }
/* 2196 */     Village randVillage = null;
/* 2197 */     int villageAttempts = 0;
/* 2198 */     while (randVillage == null) {
/*      */       
/* 2200 */       randVillage = Villages.getVillages()[Server.rand.nextInt((Villages.getVillages()).length)];
/* 2201 */       if (randVillage.kingdom != targetKingdom) {
/* 2202 */         randVillage = null;
/*      */       } else {
/*      */         
/* 2205 */         byte direction = (byte)Server.rand.nextInt(8);
/* 2206 */         int[] position = getTileOutsideVillage(direction, randVillage.getPerimeterSize() + baseDifficulty * baseDifficulty, randVillage
/* 2207 */             .getPerimeterSize() + baseDifficulty * baseDifficulty, randVillage);
/*      */         
/* 2209 */         if (position[0] < 0 || position[1] < 0) {
/* 2210 */           randVillage = null;
/*      */         } else {
/*      */           
/* 2213 */           m.setName(getMissionName(epicEntityName, newMission));
/* 2214 */           m.create();
/*      */           
/* 2216 */           String dirProximityString = MiscConstants.getDirectionString(direction) + " of " + randVillage.getName();
/* 2217 */           long nextTarget = getTileId(position[0], position[1]);
/* 2218 */           String location = getAreaString(position[0], position[1]);
/* 2219 */           int tileToCheck = Zones.getTileIntForTile(position[0], position[1], 0);
/* 2220 */           Tiles.Tile theTile = Tiles.getTile(Tiles.decodeType(tileToCheck));
/* 2221 */           String treeName = theTile.getTileName(Tiles.decodeData(tileToCheck));
/* 2222 */           String actionString = "cut down ";
/* 2223 */           byte kingdom = Zones.getKingdom(position[0], position[1]);
/*      */           
/* 2225 */           TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2226 */           effect.setMissionStateChange(100.0F);
/* 2227 */           effect.setDescription("cut down  " + treeName + " near " + randVillage.getName());
/*      */           
/* 2229 */           StringBuilder sbuild = new StringBuilder(epicEntityName + generateNeedString(baseDifficulty) + "you to " + "cut down " + "the " + treeName + ' ' + dirProximityString);
/*      */           
/* 2231 */           sbuild.append(". It is located in the " + location);
/*      */           
/* 2233 */           Kingdom k2 = Kingdoms.getKingdom(kingdom);
/* 2234 */           if (k2 != null) {
/* 2235 */             sbuild.append(" in " + k2.getName() + '.');
/*      */           } else {
/* 2237 */             sbuild.append('.');
/*      */           } 
/* 2239 */           MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2240 */           trig.setDescription("cut down ");
/* 2241 */           trig.setOnActionPerformed(492);
/* 2242 */           trig.setOnTargetId(nextTarget);
/* 2243 */           m.setInstruction(sbuild.toString());
/*      */ 
/*      */           
/*      */           try {
/* 2247 */             float xTree = (position[0] << 2) + 4.0F * TerrainUtilities.getTreePosX(position[0], position[1]);
/* 2248 */             float yTree = (position[1] << 2) + 4.0F * TerrainUtilities.getTreePosY(position[0], position[1]);
/* 2249 */             float zTree = Zones.calculateHeight(xTree, yTree, true) + 4.0F;
/* 2250 */             EffectFactory.getInstance().createGenericEffect(nextTarget, "tree", xTree, yTree, zTree, true, -1.0F, Server.rand.nextInt(360));
/*      */           }
/* 2252 */           catch (NoSuchZoneException e) {
/*      */             
/* 2254 */             logger.log(Level.WARNING, "Unable to add tree effect when creating mission", (Throwable)e);
/*      */           } 
/*      */           
/* 2257 */           linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */           
/* 2259 */           return "Cut tree mission successfully created for " + epicEntityName + ".";
/*      */         } 
/*      */       } 
/*      */       
/* 2263 */       if (++villageAttempts > 30) {
/*      */         break;
/*      */       }
/*      */     } 
/* 2267 */     return "Error creating cut down tree mission for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createCreateItemMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2273 */     CreationEntry[] entries = CreationMatrix.getInstance().getAdvancedEntriesNotEpicMission();
/* 2274 */     if (entries.length == 0) {
/* 2275 */       return "Error creating create item mission for " + epicEntityName + ". Found no creation entries.";
/*      */     }
/* 2277 */     int required = getNumberRequired(baseDifficulty, newMission);
/* 2278 */     ItemTemplate usedTemplate = null;
/*      */     
/*      */     try {
/* 2281 */       usedTemplate = ItemTemplateFactory.getInstance().getTemplate(entries[Server.rand
/* 2282 */             .nextInt(entries.length)].getObjectCreated());
/*      */       
/* 2284 */       CreationEntry ce = CreationMatrix.getInstance().getCreationEntry(usedTemplate.getTemplateId());
/* 2285 */       if (ce != null) {
/*      */         
/* 2287 */         required /= Math.min(100, ce.getTotalNumberOfItems());
/* 2288 */         if (ce.depleteSource || ce.depleteEqually) {
/*      */           
/* 2290 */           CreationEntry ceSource = CreationMatrix.getInstance().getCreationEntry(ce.getObjectSource());
/* 2291 */           if (ceSource != null && ceSource instanceof com.wurmonline.server.items.AdvancedCreationEntry)
/* 2292 */             required /= Math.min(100, ceSource.getTotalNumberOfItems()); 
/*      */         } 
/* 2294 */         if (ce.depleteTarget || ce.depleteEqually) {
/*      */           
/* 2296 */           CreationEntry ceTarg = CreationMatrix.getInstance().getCreationEntry(ce.getObjectTarget());
/* 2297 */           if (ceTarg != null && ceTarg instanceof com.wurmonline.server.items.AdvancedCreationEntry) {
/* 2298 */             required /= Math.min(100, ceTarg.getTotalNumberOfItems());
/*      */           }
/*      */         } 
/*      */       } 
/* 2302 */     } catch (NoSuchTemplateException e) {
/*      */       
/* 2304 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     } 
/* 2306 */     required = Math.max(required, 1);
/*      */     
/* 2308 */     if (usedTemplate == null) {
/* 2309 */       return "Error creating create item mission for " + epicEntityName + ". Null item template.";
/*      */     }
/* 2311 */     m.setName(getMissionName(epicEntityName, newMission));
/* 2312 */     m.create();
/*      */     
/* 2314 */     int action = 148;
/* 2315 */     String actionString = "Create";
/* 2316 */     String itemName = usedTemplate.sizeString;
/*      */     
/* 2318 */     itemName = itemName + ((required > 1) ? usedTemplate.getPlural() : usedTemplate.getName());
/*      */ 
/*      */     
/* 2321 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2322 */     effect.setDescription("Create");
/* 2323 */     effect.setMissionStateChange(100.0F / required);
/*      */     
/* 2325 */     StringBuilder sbuild = new StringBuilder(epicEntityName + generateNeedString(baseDifficulty) + "you to create " + required + ' ' + itemName + '.');
/*      */     
/* 2327 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2328 */     trig.setDescription("Create");
/* 2329 */     trig.setOnActionPerformed(148);
/* 2330 */     trig.setOnItemUsedId(usedTemplate.getTemplateId());
/* 2331 */     m.setInstruction(sbuild.toString());
/*      */     
/* 2333 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2335 */     return "Create item mission created successfully for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createGiveItemMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2341 */     ItemTemplate[] templates = ItemTemplateFactory.getInstance().getEpicMissionTemplates();
/* 2342 */     if (templates.length == 0) {
/* 2343 */       return "Error creating give item mission for " + epicEntityName + ". Unable to load templates.";
/*      */     }
/* 2345 */     ItemTemplate usedTemplate = templates[Server.rand.nextInt(templates.length)];
/* 2346 */     int required = getNumberRequired(baseDifficulty, newMission);
/*      */ 
/*      */     
/* 2349 */     String prefix = (required == 1 && !usedTemplate.getName().endsWith("s")) ? (usedTemplate.sizeString.equals("") ? (StringUtilities.isVowel(usedTemplate.getName().charAt(0)) ? "an " : "a ") : (StringUtilities.isVowel(usedTemplate.sizeString.charAt(0)) ? "an " : "a ")) : "";
/*      */     
/* 2351 */     if ((Villages.getVillages()).length == 0) {
/* 2352 */       return "Error creating give item mission for " + epicEntityName + ". No villages.";
/*      */     }
/* 2354 */     Village randVillage = null;
/* 2355 */     int villageAttempts = 0;
/* 2356 */     while (randVillage == null) {
/*      */       
/* 2358 */       randVillage = Villages.getVillages()[Server.rand.nextInt((Villages.getVillages()).length)];
/* 2359 */       if (randVillage.kingdom != targetKingdom) {
/* 2360 */         randVillage = null;
/*      */       } else {
/*      */         
/* 2363 */         byte direction = (byte)Server.rand.nextInt(8);
/* 2364 */         int[] position = getTileOutsideVillage(direction, randVillage.getDiameterX() * baseDifficulty, randVillage
/* 2365 */             .getDiameterY() * baseDifficulty, randVillage);
/*      */         
/* 2367 */         if (position[0] < 0 || position[1] < 0) {
/* 2368 */           randVillage = null;
/*      */         } else {
/*      */           
/* 2371 */           int cid = 68;
/* 2372 */           if (epicEntityId == 4) {
/* 2373 */             cid = 81;
/* 2374 */           } else if (epicEntityId == 3) {
/* 2375 */             cid = 78;
/* 2376 */           } else if (epicEntityId == 1) {
/* 2377 */             cid = 80;
/* 2378 */           } else if (epicEntityId == 2) {
/* 2379 */             cid = 79;
/*      */           } 
/*      */           try {
/* 2382 */             CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(cid);
/* 2383 */             byte sex = ct.getSex();
/* 2384 */             Creature target = Creature.doNew(cid, false, ((position[0] << 2) + 2), ((position[1] << 2) + 2), Server.rand
/* 2385 */                 .nextFloat() * 360.0F, 0, "Avatar of " + epicEntityName, sex, favoredKingdomId, (byte)0, false);
/*      */             
/* 2387 */             String proximityString = "near " + randVillage.getName();
/* 2388 */             int action = 47;
/* 2389 */             String actionString = "Give";
/*      */             
/* 2391 */             m.setName(getMissionName(epicEntityName, newMission));
/* 2392 */             m.create();
/*      */             
/* 2394 */             TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2395 */             effect.setTopText("Mission progress");
/* 2396 */             effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2397 */             effect.setDescription("Give");
/* 2398 */             effect.setMissionStateChange(100.0F / required);
/* 2399 */             effect.setDestroysTarget(true);
/*      */             
/* 2401 */             MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2402 */             trig.setDescription("Give");
/* 2403 */             trig.setOnActionPerformed(47);
/* 2404 */             trig.setOnTargetId(target.getWurmId());
/* 2405 */             trig.setOnItemUsedId(usedTemplate.getTemplateId());
/*      */             
/* 2407 */             String itemName = usedTemplate.sizeString;
/*      */             
/* 2409 */             itemName = itemName + ((required > 1) ? usedTemplate.getPlural() : usedTemplate.getName());
/*      */             
/* 2411 */             StringBuilder sbuild = new StringBuilder();
/* 2412 */             sbuild.append(epicEntityName + generateNeedString(baseDifficulty) + ' ' + required + " of you to bring and give " + prefix + itemName + " to " + target
/*      */ 
/*      */                 
/* 2415 */                 .getName() + ". ");
/* 2416 */             sbuild.append(target.getName().substring(0, 1).toUpperCase() + target.getName().substring(1));
/* 2417 */             sbuild.append(" was last seen " + proximityString + " in the " + getAreaString(position[0], position[1]) + '.');
/* 2418 */             m.setInstruction(sbuild.toString());
/*      */             
/* 2420 */             linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */             
/* 2422 */             return "Give item mission created successfully for " + epicEntityName + ".";
/*      */           }
/* 2424 */           catch (Exception ex) {
/*      */             
/* 2426 */             logger.log(Level.WARNING, ex.getMessage());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2431 */       if (++villageAttempts > 30)
/*      */         break; 
/*      */     } 
/* 2434 */     return "Error creating give item mission for " + epicEntityName + ". Unable to find suitable village.";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createSlayCreatureMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2440 */     CreatureTemplate[] templates = CreatureTemplateFactory.getInstance().getTemplates();
/* 2441 */     if (templates.length == 0) {
/* 2442 */       return "Error creating slay creature mission for " + epicEntityName + ". Unable to load templates.";
/*      */     }
/* 2444 */     ArrayList<CreatureTemplate> possibleTemplates = new ArrayList<>();
/* 2445 */     CreatureTemplate usedTemplate = null;
/* 2446 */     boolean forceChamps = false;
/* 2447 */     switch (newMission.getMissionType()) {
/*      */       
/*      */       case 114:
/* 2450 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2453 */           if (!t.isMissionDisabled())
/*      */           {
/*      */             
/* 2456 */             if (t.isHerbivore() && !t.isBabyCreature())
/* 2457 */               possibleTemplates.add(t);  } 
/*      */         } 
/*      */         break;
/*      */       case 115:
/* 2461 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2464 */           if (!t.isMissionDisabled())
/*      */           {
/*      */             
/* 2467 */             if (t.isEpicMissionSlayable() && !t.isBabyCreature())
/* 2468 */               possibleTemplates.add(t);  } 
/*      */         } 
/*      */         break;
/*      */       case 116:
/* 2472 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2475 */           if (!t.isMissionDisabled())
/*      */           {
/*      */             
/* 2478 */             if ((Servers.localServer.PVPSERVER && t.isFromValrei && t.getTemplateId() != 68) || (t
/* 2479 */               .isEpicMissionSlayable() && t.getBaseCombatRating() > 7.0F)) {
/*      */               
/* 2481 */               possibleTemplates.add(t);
/* 2482 */               if (!Servers.localServer.PVPSERVER)
/* 2483 */                 forceChamps = true; 
/*      */             }  } 
/*      */         } 
/*      */         break;
/*      */     } 
/* 2488 */     if (!possibleTemplates.isEmpty())
/* 2489 */       usedTemplate = possibleTemplates.get(Server.rand.nextInt(possibleTemplates.size())); 
/* 2490 */     if (usedTemplate == null) {
/* 2491 */       return "Error creating slay creature mission for " + epicEntityName + ". Null creature template.";
/*      */     }
/* 2493 */     int required = getNumberRequired(baseDifficulty, newMission);
/* 2494 */     int action = 491;
/* 2495 */     String actionString = "Slay";
/* 2496 */     int requiredSpawn = Zones.worldTileSizeX / 2048 * required;
/*      */     
/* 2498 */     if (newMission.getMissionType() == 116)
/* 2499 */       requiredSpawn /= 2; 
/* 2500 */     requiredSpawn = (int)Math.max(required * 1.5F, requiredSpawn);
/* 2501 */     for (int i = 0; i < requiredSpawn; i++) {
/* 2502 */       spawnSingleCreature(usedTemplate, forceChamps);
/*      */     }
/* 2504 */     m.setName(getMissionName(epicEntityName, newMission));
/* 2505 */     m.create();
/*      */     
/* 2507 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2508 */     effect.setTopText("Mission progress");
/* 2509 */     effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2510 */     effect.setDescription("Slay");
/* 2511 */     effect.setMissionStateChange(100.0F / required);
/*      */     
/* 2513 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2514 */     trig.setOnItemUsedId(usedTemplate.getTemplateId());
/* 2515 */     trig.setDescription("Slay");
/* 2516 */     trig.setOnActionPerformed(491);
/*      */     
/* 2518 */     String creatureName = (required > 1) ? usedTemplate.getPlural() : usedTemplate.getName();
/*      */     
/* 2520 */     StringBuilder sbuild = new StringBuilder();
/* 2521 */     sbuild.append(epicEntityName + generateNeedString(baseDifficulty) + "you to slay " + required + (forceChamps ? " champion " : " ") + creatureName + " that have appeared across the land.");
/*      */     
/* 2523 */     m.setInstruction(sbuild.toString());
/*      */     
/* 2525 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2527 */     return "Slay creature mission successfully created for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createSlayTraitorMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2533 */     CreatureTemplate[] templates = CreatureTemplateFactory.getInstance().getTemplates();
/* 2534 */     if (templates.length == 0) {
/* 2535 */       return "Error creating slay creature mission for " + epicEntityName + ". Unable to load templates.";
/*      */     }
/* 2537 */     ArrayList<CreatureTemplate> possibleTemplates = new ArrayList<>();
/* 2538 */     CreatureTemplate usedTemplate = null;
/* 2539 */     boolean forceChamps = false;
/* 2540 */     switch (newMission.getMissionType()) {
/*      */       
/*      */       case 117:
/* 2543 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2546 */           if (!t.isMissionDisabled())
/*      */           {
/*      */             
/* 2549 */             if (t.isHerbivore() && !t.isBabyCreature())
/* 2550 */               possibleTemplates.add(t);  } 
/*      */         } 
/*      */         break;
/*      */       case 118:
/* 2554 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2557 */           if (!t.isMissionDisabled())
/*      */           {
/*      */             
/* 2560 */             if (t.isEpicMissionTraitor() && !t.isBabyCreature())
/* 2561 */               possibleTemplates.add(t);  } 
/*      */         } 
/*      */         break;
/*      */       case 119:
/* 2565 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2568 */           if (!t.isMissionDisabled())
/*      */           {
/*      */             
/* 2571 */             if ((Servers.localServer.PVPSERVER && t.isFromValrei) || (t
/* 2572 */               .isEpicMissionTraitor() && t.getBaseCombatRating() > 7.0F)) {
/*      */               
/* 2574 */               possibleTemplates.add(t);
/* 2575 */               if (!Servers.localServer.PVPSERVER)
/* 2576 */                 forceChamps = true; 
/*      */             }  } 
/*      */         } 
/*      */         break;
/*      */     } 
/* 2581 */     if (!possibleTemplates.isEmpty())
/* 2582 */       usedTemplate = possibleTemplates.get(Server.rand.nextInt(possibleTemplates.size())); 
/* 2583 */     if (usedTemplate == null) {
/* 2584 */       return "Error creating slay traitor mission for " + epicEntityName + ". Null creature template.";
/*      */     }
/* 2586 */     int action = 491;
/* 2587 */     String actionString = "Slay";
/*      */     
/* 2589 */     Creature spawnedTraitor = spawnSingleCreature(usedTemplate, forceChamps);
/* 2590 */     if (spawnedTraitor == null)
/* 2591 */       return "Error creating slay traitor mission for " + epicEntityName + ". Failed to create creature."; 
/* 2592 */     spawnedTraitor.setName(usedTemplate.getName() + " (traitor)");
/* 2593 */     spawnedTraitor.getStatus().setTraitBit(28, true);
/* 2594 */     SpellEffect eff = new SpellEffect(spawnedTraitor.getWurmId(), (byte)22, 80.0F, 20000000, (byte)9, (byte)0, true);
/*      */     
/* 2596 */     if (spawnedTraitor.getSpellEffects() == null)
/* 2597 */       spawnedTraitor.createSpellEffects(); 
/* 2598 */     spawnedTraitor.getSpellEffects().addSpellEffect(eff);
/* 2599 */     spawnedTraitor.setVisible(false);
/* 2600 */     Zones.flash(spawnedTraitor.getTileX(), spawnedTraitor.getTileY(), false);
/* 2601 */     spawnedTraitor.setVisible(true);
/*      */     
/* 2603 */     Effect traitorEffect = EffectFactory.getInstance().createGenericEffect(spawnedTraitor.getWurmId(), "traitor", spawnedTraitor
/* 2604 */         .getPosX(), spawnedTraitor.getPosY(), spawnedTraitor.getPositionZ() + spawnedTraitor.getHalfHeightDecimeters() / 10.0F, spawnedTraitor
/* 2605 */         .isOnSurface(), -1.0F, spawnedTraitor.getStatus().getRotation());
/* 2606 */     spawnedTraitor.addEffect(traitorEffect);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2611 */       spawnedTraitor.getStatus().setChanged(true);
/* 2612 */       spawnedTraitor.save();
/*      */     }
/* 2614 */     catch (IOException e) {
/*      */       
/* 2616 */       logger.log(Level.WARNING, "Unable to save new traitor creature.", e);
/*      */     } 
/*      */     
/* 2619 */     m.setName(getMissionName(epicEntityName, newMission));
/* 2620 */     m.create();
/*      */     
/* 2622 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2623 */     effect.setTopText("Mission progress");
/* 2624 */     effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2625 */     effect.setDescription("Slay");
/* 2626 */     effect.setMissionStateChange(100.0F);
/*      */     
/* 2628 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2629 */     trig.setOnTargetId(spawnedTraitor.getWurmId());
/* 2630 */     trig.setDescription("Slay");
/* 2631 */     trig.setOnActionPerformed(491);
/*      */     
/* 2633 */     StringBuilder sbuild = new StringBuilder();
/* 2634 */     sbuild.append(epicEntityName + generateNeedString(baseDifficulty) + "you to slay the traitor" + (forceChamps ? " champion " : " ") + usedTemplate
/* 2635 */         .getName() + " seen fleeing from Valrei to these lands. It was last spotted in the " + 
/* 2636 */         getAreaString(spawnedTraitor.getTileX(), spawnedTraitor.getTileY()) + ".");
/* 2637 */     m.setInstruction(sbuild.toString());
/*      */     
/* 2639 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2641 */     return "Slay traitor mission successfully created for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createSacrificeCreatureMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2647 */     CreatureTemplate[] templates = CreatureTemplateFactory.getInstance().getTemplates();
/* 2648 */     if (templates.length == 0) {
/* 2649 */       return "Error creating sacrifice creature mission for " + epicEntityName + ". Unable to load templates.";
/*      */     }
/* 2651 */     ArrayList<CreatureTemplate> possibleTemplates = new ArrayList<>();
/* 2652 */     CreatureTemplate usedTemplate = null;
/* 2653 */     switch (newMission.getMissionType()) {
/*      */       
/*      */       case 121:
/* 2656 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2659 */           if (!t.isMissionDisabled() && !t.isSubmerged())
/*      */           {
/*      */             
/* 2662 */             if (t.isHerbivore() && !t.isBabyCreature())
/* 2663 */               possibleTemplates.add(t);  } 
/*      */         } 
/*      */         break;
/*      */       case 122:
/* 2667 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2670 */           if (!t.isMissionDisabled() && !t.isSubmerged())
/*      */           {
/*      */             
/* 2673 */             if (t.isEpicMissionSlayable() && t.getBaseCombatRating() <= 7.0F && !t.isBabyCreature())
/* 2674 */               possibleTemplates.add(t);  } 
/*      */         } 
/*      */         break;
/*      */       case 123:
/* 2678 */         for (CreatureTemplate t : templates) {
/*      */ 
/*      */           
/* 2681 */           if (!t.isMissionDisabled() && !t.isSubmerged())
/*      */           {
/*      */             
/* 2684 */             if ((Servers.localServer.PVPSERVER && t.isFromValrei) || (t
/* 2685 */               .isEpicMissionSlayable() && t.getBaseCombatRating() > 7.0F))
/* 2686 */               possibleTemplates.add(t);  } 
/*      */         } 
/*      */         break;
/*      */     } 
/* 2690 */     if (!possibleTemplates.isEmpty())
/* 2691 */       usedTemplate = possibleTemplates.get(Server.rand.nextInt(possibleTemplates.size())); 
/* 2692 */     if (usedTemplate == null) {
/* 2693 */       return "Error creating sacrifice creature mission for " + epicEntityName + ". Null creature template.";
/*      */     }
/* 2695 */     int required = getNumberRequired(baseDifficulty, newMission);
/* 2696 */     int action = 142;
/* 2697 */     String actionString = "Sacrifice";
/* 2698 */     int requiredSpawn = Math.max(required, Zones.worldTileSizeX / 2048 * required);
/*      */     
/* 2700 */     for (int i = 0; i < requiredSpawn; i++) {
/* 2701 */       spawnSingleCreature(usedTemplate, false);
/*      */     }
/* 2703 */     m.setName(getMissionName(epicEntityName, newMission));
/* 2704 */     m.create();
/*      */     
/* 2706 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2707 */     effect.setTopText("Mission progress");
/* 2708 */     effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2709 */     effect.setDescription("Sacrifice");
/* 2710 */     effect.setMissionStateChange(100.0F / required);
/*      */     
/* 2712 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2713 */     trig.setOnItemUsedId(-usedTemplate.getTemplateId());
/* 2714 */     trig.setOnTargetId(-10L);
/* 2715 */     trig.setDescription("Sacrifice");
/* 2716 */     trig.setOnActionPerformed(142);
/*      */     
/* 2718 */     String creatureName = (required > 1) ? usedTemplate.getPlural() : usedTemplate.getName();
/*      */     
/* 2720 */     StringBuilder sbuild = new StringBuilder();
/* 2721 */     sbuild.append(epicEntityName + generateNeedString(baseDifficulty) + "you to sacrifice " + required + " " + creatureName + " that " + ((required > 1) ? "have appeared across the land" : "has appeared in the world") + ". Use a sacrificial knife on them inside the domain of " + epicEntityName + " after weakening them to at least half of their health.");
/*      */ 
/*      */ 
/*      */     
/* 2725 */     m.setInstruction(sbuild.toString());
/*      */     
/* 2727 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2729 */     return "Sacrifice creature mission successfully created for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */   
/*      */   private String createSlayTowerGuardsMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/*      */     CreatureTemplate usedTemplate;
/* 2735 */     int templateId = 67;
/* 2736 */     Kingdom k = Kingdoms.getKingdom(targetKingdom);
/* 2737 */     if (k == null)
/* 2738 */       return "Error creating slay tower guards mission for " + epicEntityName + ". Invalid kingdom."; 
/* 2739 */     switch (k.getTemplate()) {
/*      */       
/*      */       case 3:
/* 2742 */         templateId = 35;
/*      */         break;
/*      */       case 1:
/* 2745 */         templateId = 34;
/*      */         break;
/*      */       case 2:
/* 2748 */         templateId = 36;
/*      */         break;
/*      */       default:
/* 2751 */         return "Error creating slay tower guards mission for " + epicEntityName + ". Invalid kingdom template.";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2757 */       usedTemplate = CreatureTemplateFactory.getInstance().getTemplate(templateId);
/*      */     }
/* 2759 */     catch (NoSuchCreatureTemplateException e) {
/*      */       
/* 2761 */       return "Error creating slay tower guards mission for " + epicEntityName + ". Invalid creature template.";
/*      */     } 
/*      */     
/* 2764 */     int required = getNumberRequired(baseDifficulty, newMission);
/* 2765 */     int action = 491;
/* 2766 */     String actionString = "Slay";
/*      */     
/* 2768 */     m.setName(getMissionName(epicEntityName, newMission));
/* 2769 */     m.create();
/*      */     
/* 2771 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2772 */     effect.setTopText("Mission progress");
/* 2773 */     effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2774 */     effect.setDescription("Slay");
/* 2775 */     effect.setMissionStateChange(100.0F / required);
/*      */     
/* 2777 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2778 */     trig.setOnItemUsedId(usedTemplate.getTemplateId());
/* 2779 */     trig.setDescription("Slay");
/* 2780 */     trig.setOnActionPerformed(491);
/*      */     
/* 2782 */     String creatureName = (required > 1) ? usedTemplate.getPlural() : usedTemplate.getName();
/*      */     
/* 2784 */     StringBuilder sbuild = new StringBuilder();
/* 2785 */     sbuild.append(epicEntityName + generateNeedString(baseDifficulty) + "you to slay " + required + " " + creatureName + " to help cleanse the lands.");
/*      */     
/* 2787 */     m.setInstruction(sbuild.toString());
/*      */     
/* 2789 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2791 */     return "Slay tower guard mission successfully created for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String createDestroyGTMission(Mission m, EpicMissionEnum newMission, int epicEntityId, String epicEntityName, int baseDifficulty, byte favoredKingdomId, byte targetKingdom) {
/* 2797 */     Item targetTower = null;
/* 2798 */     int itemFindAttempts = 0;
/* 2799 */     while (targetTower == null && itemFindAttempts++ < 50 && Zones.getGuardTowers().size() > 0) {
/*      */       
/* 2801 */       targetTower = Zones.getGuardTowers().get(Server.rand.nextInt(Zones.getGuardTowers().size()));
/* 2802 */       if (targetTower != null) {
/*      */         
/* 2804 */         if (targetTower.getKingdom() != targetKingdom) {
/*      */           
/* 2806 */           targetTower = null;
/*      */           continue;
/*      */         } 
/* 2809 */         VolaTile t = Zones.getTileOrNull(targetTower.getTilePos(), targetTower.isOnSurface());
/* 2810 */         if (t != null)
/*      */         {
/* 2812 */           if (t.getVillage() != null || t.getStructure() != null)
/* 2813 */             targetTower = null; 
/*      */         }
/*      */       } 
/*      */     } 
/* 2817 */     if (targetTower == null) {
/* 2818 */       return "Error finding correct target item for destroy guard tower mission for " + epicEntityName + ".";
/*      */     }
/* 2820 */     int action = 913;
/* 2821 */     String actionString = "Destroy";
/*      */     
/* 2823 */     m.setName(getMissionName(epicEntityName, newMission));
/* 2824 */     m.create();
/*      */     
/* 2826 */     TriggerEffect effect = initializeTriggerEffect(epicEntityId, epicEntityName);
/* 2827 */     effect.setTopText("Mission progress");
/* 2828 */     effect.setTextDisplayed(epicEntityName + " is pleased.");
/* 2829 */     effect.setDescription("Destroy");
/* 2830 */     effect.setMissionStateChange(100.0F);
/*      */     
/* 2832 */     MissionTrigger trig = initializeMissionTrigger(epicEntityId, epicEntityName);
/* 2833 */     trig.setOnTargetId(targetTower.getWurmId());
/* 2834 */     trig.setDescription("Destroy");
/* 2835 */     trig.setOnActionPerformed(913);
/*      */     
/* 2837 */     StringBuilder sbuild = new StringBuilder();
/* 2838 */     sbuild.append(epicEntityName + generateNeedString(baseDifficulty) + "you to destroy " + targetTower
/* 2839 */         .getName() + " which can be found in the " + getAreaString(targetTower.getTileX(), targetTower.getTileY()) + ".");
/* 2840 */     m.setInstruction(sbuild.toString());
/*      */     
/* 2842 */     linkMission(m, trig, effect, newMission.getMissionType(), baseDifficulty);
/*      */     
/* 2844 */     return "Destroy guard tower mission successfully created for " + epicEntityName + ".";
/*      */   }
/*      */ 
/*      */   
/*      */   private static int[] getTileOutsideVillage(byte direction, int distanceX, int distanceY, Village v) {
/* 2849 */     int startX = v.getTokenX();
/* 2850 */     int startY = v.getTokenY();
/*      */     
/* 2852 */     if (direction == 7 || direction == 0 || direction == 1) {
/* 2853 */       startY = v.getStartY() - distanceY;
/* 2854 */     } else if (direction == 6 || direction == 2) {
/*      */       
/* 2856 */       startY = v.getStartY();
/* 2857 */       distanceY = v.getDiameterY();
/*      */     } else {
/*      */       
/* 2860 */       startY = v.getEndY();
/*      */     } 
/* 2862 */     if (direction == 7 || direction == 6 || direction == 5) {
/* 2863 */       startX = v.getStartX() - distanceX;
/* 2864 */     } else if (direction == 0 || direction == 4) {
/*      */       
/* 2866 */       startX = v.getStartX();
/* 2867 */       distanceX = v.getDiameterX();
/*      */     } else {
/*      */       
/* 2870 */       startX = v.getEndX();
/*      */     } 
/* 2872 */     int tileAttempts = 0;
/* 2873 */     int tileX = -1;
/* 2874 */     int tileY = -1;
/* 2875 */     while ((tileX < 0 || tileY < 0) && tileAttempts++ < 50) {
/*      */       
/* 2877 */       int tempX = startX + Server.rand.nextInt(Math.max(1, distanceX));
/* 2878 */       int tempY = startY + Server.rand.nextInt(Math.max(1, distanceY));
/*      */       
/* 2880 */       Village vt = Villages.getVillageWithPerimeterAt(tempX, tempY, true);
/* 2881 */       if (vt != null) {
/*      */         continue;
/*      */       }
/* 2884 */       int tileToCheck = Zones.getTileIntForTile(tempX, tempY, 0);
/* 2885 */       byte type = Tiles.decodeType(tileToCheck);
/* 2886 */       byte data = Tiles.decodeData(tileToCheck);
/* 2887 */       Tiles.Tile theTile = Tiles.getTile(type);
/* 2888 */       if (theTile.isTree() && !theTile.isEnchanted())
/*      */       {
/* 2890 */         if (FoliageAge.getAgeAsByte(data) > FoliageAge.MATURE_ONE.getAgeId() && 
/* 2891 */           FoliageAge.getAgeAsByte(data) < FoliageAge.OVERAGED.getAgeId()) {
/*      */           
/* 2893 */           tileX = tempX;
/* 2894 */           tileY = tempY;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 2899 */     return new int[] { tileX, tileY };
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getNumberRequired(int difficulty, EpicMissionEnum newMission) {
/* 2904 */     int playerMax = (int)(Servers.localServer.PVPSERVER ? (difficulty * 1.5D) : (difficulty * 2.5D));
/* 2905 */     switch (newMission.getMissionType()) {
/*      */       
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/* 2910 */         return 1;
/*      */       case 104:
/*      */       case 105:
/* 2913 */         return Math.max(1, Math.min(difficulty * difficulty, playerMax));
/*      */       case 106:
/*      */       case 107:
/* 2916 */         return 1;
/*      */       case 108:
/* 2918 */         return Math.max(1, playerMax);
/*      */       case 109:
/* 2920 */         return Math.max(1, difficulty * difficulty);
/*      */       case 110:
/*      */       case 111:
/* 2923 */         return Math.max(1, difficulty * 75);
/*      */       case 112:
/*      */       case 113:
/* 2926 */         return Math.max(1, Math.min(difficulty * difficulty, playerMax));
/*      */       case 114:
/*      */       case 115:
/* 2929 */         return Math.max(1, difficulty * difficulty * difficulty / 2 + 5);
/*      */       case 116:
/* 2931 */         return Math.max(1, (difficulty * difficulty * difficulty + 5) / 15);
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/* 2935 */         return 1;
/*      */       case 120:
/* 2937 */         return 1;
/*      */       case 121:
/*      */       case 122:
/*      */       case 123:
/* 2941 */         return Math.max(1, (int)Math.min((difficulty * difficulty), difficulty * 2.5D));
/*      */       case 124:
/* 2943 */         return Math.max(1, difficulty * difficulty * difficulty / 2);
/*      */     } 
/* 2945 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final PathTile getTargetVillage(byte requiredKingdom) {
/* 2956 */     PathTile tile = null;
/* 2957 */     Village[] varr = Villages.getVillages();
/* 2958 */     List<Village> prospects = new ArrayList<>();
/* 2959 */     for (Village lElement : varr) {
/*      */       
/* 2961 */       if (lElement.kingdom == requiredKingdom)
/* 2962 */         prospects.add(lElement); 
/*      */     } 
/* 2964 */     if (prospects.size() > 0) {
/*      */       
/* 2966 */       Village prosp = prospects.get(Server.rand.nextInt(prospects.size()));
/* 2967 */       tile = new PathTile(prosp.getTokenX(), prosp.getTokenY(), 0, true, 0);
/*      */     } 
/* 2969 */     return tile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte[] getEnemyKingdoms(int epicEntityId, byte favoredKingdom) {
/* 2980 */     Set<Byte> toReturn = new HashSet<>();
/* 2981 */     if (favoredKingdom != 0) {
/*      */       
/* 2983 */       Kingdom[] karr = Kingdoms.getAllKingdoms();
/* 2984 */       for (Kingdom lElement : karr) {
/*      */         
/* 2986 */         if (lElement.getTemplate() != favoredKingdom)
/*      */         {
/* 2988 */           if (!lElement.isAllied(favoredKingdom))
/* 2989 */             toReturn.add(Byte.valueOf(lElement.getId())); 
/*      */         }
/*      */       } 
/*      */     } 
/* 2993 */     byte[] toRet = new byte[toReturn.size()];
/* 2994 */     int x = 0;
/* 2995 */     for (Byte b : toReturn) {
/*      */       
/* 2997 */       toRet[x] = b.byteValue();
/* 2998 */       x++;
/*      */     } 
/* 3000 */     return toRet;
/*      */   }
/*      */   
/* 3003 */   private static final String[] missionAdjectives = new String[] { "last ", "horrendous ", "shining ", "first ", "scary ", "mysterious ", "enigmatic ", "important ", "strong ", "massive ", "gigantic ", "heavy ", "light ", "bright ", "deadly ", "dangerous ", "marked ", "fantastic ", "imposing ", "paradoxical ", "final " };
/*      */ 
/*      */   
/* 3006 */   private static final String[] missionNames = new String[] { "whisper", "gesture", "tears", "laughter", "horror", "mystery", "enigma", "celebration", "word", "run", "challenge", "test", "jest", "joke", "need", "quest", "trip", "folly", "lesson", "journey", "adventure" };
/*      */ 
/*      */   
/* 3009 */   private static final String[] missionFor = new String[] { " to ", " for ", " from ", " in honour of ", " of ", " to help ", " in aid of ", " in service of " };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String getMissionName(String epicEntityName, EpicMissionEnum newMission) {
/* 3014 */     StringBuilder builder = new StringBuilder();
/* 3015 */     boolean startWithName = Server.rand.nextBoolean();
/* 3016 */     if (startWithName) {
/*      */       
/* 3018 */       builder.append(epicEntityName);
/* 3019 */       builder.append("'s ");
/*      */     } 
/* 3021 */     if (Server.rand.nextBoolean() && missionAdjectives.length > 0)
/*      */     {
/* 3023 */       builder.append(missionAdjectives[Server.rand.nextInt(missionAdjectives.length)]);
/*      */     }
/* 3025 */     builder.append(missionNames[Server.rand.nextInt(missionNames.length)]);
/* 3026 */     if (!startWithName) {
/*      */       
/* 3028 */       builder.append(missionFor[Server.rand.nextInt(missionFor.length)]);
/* 3029 */       builder.append(epicEntityName);
/*      */     }
/* 3031 */     else if (Server.rand.nextInt(3) == 0) {
/*      */       
/* 3033 */       if (Server.rand.nextBoolean()) {
/* 3034 */         builder.append(" of ");
/*      */       } else {
/* 3036 */         builder.append(" for ");
/* 3037 */       }  builder.append(newMission.getRandomMissionName());
/*      */     } 
/* 3039 */     String toret = LoginHandler.raiseFirstLetter(builder.toString());
/*      */     
/* 3041 */     if (!startWithName)
/* 3042 */       toret = toret.replace(epicEntityName.toLowerCase(), epicEntityName); 
/* 3043 */     return toret;
/*      */   }
/*      */   
/* 3046 */   private static ArrayList<EpicMission> matchingMissions = new ArrayList<>();
/*      */ 
/*      */   
/*      */   public static EpicMission getMISacrificeMission() {
/* 3050 */     matchingMissions.clear();
/* 3051 */     for (EpicMission m : getCurrentEpicMissions()) {
/*      */       
/* 3053 */       if (m.getMissionType() == 109) {
/* 3054 */         matchingMissions.add(m);
/*      */       }
/*      */     } 
/* 3057 */     if (matchingMissions.isEmpty()) {
/* 3058 */       return null;
/*      */     }
/* 3060 */     return matchingMissions.get(Server.rand.nextInt(matchingMissions.size()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static EpicMission[] getTraitorMissions() {
/* 3065 */     matchingMissions.clear();
/* 3066 */     for (EpicMission m : getCurrentEpicMissions()) {
/*      */       
/* 3068 */       if (m.getMissionType() == 117 || m
/* 3069 */         .getMissionType() == 118 || m
/* 3070 */         .getMissionType() == 119)
/*      */       {
/* 3072 */         matchingMissions.add(m);
/*      */       }
/*      */     } 
/*      */     
/* 3076 */     if (matchingMissions.isEmpty()) {
/* 3077 */       return null;
/*      */     }
/* 3079 */     return matchingMissions.<EpicMission>toArray(new EpicMission[matchingMissions.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Creature[] getCurrentTraitors() {
/* 3084 */     EpicMission[] missions = getTraitorMissions();
/* 3085 */     if (missions != null) {
/*      */       
/* 3087 */       ArrayList<Creature> creatureList = new ArrayList<>();
/* 3088 */       for (EpicMission m : missions) {
/*      */         
/* 3090 */         if (m.getMissionProgress() >= 1.0F && m.getMissionProgress() < 100.0F)
/*      */         {
/*      */           
/* 3093 */           for (MissionTrigger mt : MissionTriggers.getAllTriggers()) {
/*      */             
/* 3095 */             if (mt.getMissionRequired() == m.getMissionId()) {
/*      */               
/* 3097 */               long traitorId = mt.getTarget();
/* 3098 */               Creature c = Creatures.getInstance().getCreatureOrNull(traitorId);
/*      */               
/* 3100 */               if (c != null)
/* 3101 */                 creatureList.add(c); 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/* 3106 */       if (creatureList.isEmpty()) {
/* 3107 */         return null;
/*      */       }
/* 3109 */       return creatureList.<Creature>toArray(new Creature[creatureList.size()]);
/*      */     } 
/*      */     
/* 3112 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean doesGiveItemMissionExist(long creatureId) {
/* 3117 */     for (MissionTrigger mt : MissionTriggers.getAllTriggers()) {
/*      */       
/* 3119 */       if (mt.getOnActionPerformed() == 47)
/*      */       {
/* 3121 */         if (mt.getTarget() == creatureId) {
/*      */           
/* 3123 */           EpicMission mis = getEpicMissionForMission(mt.getMissionRequired());
/* 3124 */           if (mis != null) {
/* 3125 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 3130 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean doesTraitorMissionExist(long wurmId) {
/* 3135 */     Creature[] cList = getCurrentTraitors();
/*      */     
/* 3137 */     if (cList == null) {
/* 3138 */       return false;
/*      */     }
/* 3140 */     for (Creature c : cList) {
/* 3141 */       if (c.getWurmId() == wurmId)
/* 3142 */         return true; 
/*      */     } 
/* 3144 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void avatarCreatureKilled(long creatureId) {
/* 3149 */     for (MissionTrigger mt : MissionTriggers.getAllTriggers()) {
/*      */       
/* 3151 */       if (mt.getOnActionPerformed() == 47)
/*      */       {
/* 3153 */         if (mt.getTarget() == creatureId) {
/*      */           
/* 3155 */           EpicMission mis = getEpicMissionForMission(mt.getMissionRequired());
/* 3156 */           if (mis != null) {
/*      */             
/* 3158 */             destroySpecificMission(mis);
/*      */ 
/*      */             
/* 3161 */             WcEpicStatusReport wce = new WcEpicStatusReport(WurmId.getNextWCCommandId(), false, mis.getEpicEntityId(), mis.getMissionType(), mis.getDifficulty());
/* 3162 */             wce.sendToLoginServer();
/* 3163 */             storeLastMissionForEntity(mis.getEpicEntityId(), mis);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void traitorCreatureKilled(long creatureId) {
/* 3172 */     for (MissionTrigger mt : MissionTriggers.getAllTriggers()) {
/*      */       
/* 3174 */       if (mt.getOnActionPerformed() == 491)
/*      */       {
/* 3176 */         if (mt.getTarget() == creatureId) {
/*      */           
/* 3178 */           EpicMission mis = getEpicMissionForMission(mt.getMissionRequired());
/* 3179 */           if (mis != null) {
/*      */             
/* 3181 */             destroySpecificMission(mis);
/*      */ 
/*      */             
/* 3184 */             WcEpicStatusReport wce = new WcEpicStatusReport(WurmId.getNextWCCommandId(), false, mis.getEpicEntityId(), mis.getMissionType(), mis.getDifficulty());
/* 3185 */             wce.sendToLoginServer();
/* 3186 */             storeLastMissionForEntity(mis.getEpicEntityId(), mis);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Creature spawnSingleCreature(CreatureTemplate template, boolean champion) {
/* 3196 */     int attempts = 0;
/* 3197 */     while (attempts < 5000) {
/*      */       
/* 3199 */       attempts++;
/* 3200 */       int centerx = Server.rand.nextInt(Zones.worldTileSizeX);
/* 3201 */       int centery = Server.rand.nextInt(Zones.worldTileSizeY);
/* 3202 */       for (int x = 0; x < 10; x++) {
/*      */         
/* 3204 */         int tx = Zones.safeTileX(centerx - 5 + Server.rand.nextInt(10));
/* 3205 */         int ty = Zones.safeTileY(centery - 5 + Server.rand.nextInt(10));
/*      */         
/*      */         try {
/* 3208 */           float height = Zones.calculateHeight((tx * 4 + 2), (ty * 4 + 2), true);
/* 3209 */           if ((height >= 0.0F && !template.isSubmerged()) || (template
/* 3210 */             .isSubmerged() && height < -30.0F) || (template
/* 3211 */             .isSwimming() && height < -2.0F && height > -30.0F)) {
/*      */             
/* 3213 */             VolaTile t = Zones.getOrCreateTile(tx, ty, true);
/* 3214 */             if (t.getStructure() == null && t.getVillage() == null)
/*      */             {
/* 3216 */               byte sex = template.getSex();
/* 3217 */               if (sex == 0 && !template.keepSex && 
/* 3218 */                 Server.rand.nextBoolean()) {
/* 3219 */                 sex = 1;
/*      */               }
/* 3221 */               byte ctype = champion ? 99 : 0;
/* 3222 */               Creature toReturn = Creature.doNew(template.getTemplateId(), false, (tx * 4 + 2), (ty * 4 + 2), Server.rand
/* 3223 */                   .nextFloat() * 360.0F, 0, template.getName(), sex, (byte)0, ctype, false, 
/* 3224 */                   (byte)(Server.rand.nextInt(8) + 4));
/* 3225 */               toReturn.getStatus().setTraitBit(29, true);
/*      */               
/* 3227 */               return toReturn;
/*      */             }
/*      */           
/*      */           } 
/* 3231 */         } catch (Exception exception) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3238 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLastMissionForEntity(int epicEntityId, EpicMission em) {
/* 3244 */     EpicEntity entity = getValrei().getEntity(epicEntityId);
/* 3245 */     if (entity != null) {
/* 3246 */       entity.setLatestMissionDifficulty(em.getDifficulty());
/*      */     } else {
/*      */       
/* 3249 */       if (backupDifficultyMap == null)
/* 3250 */         backupDifficultyMap = new HashMap<>(); 
/* 3251 */       backupDifficultyMap.put(Integer.valueOf(epicEntityId), Integer.valueOf(em.getDifficulty()));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static EpicMission getRitualMissionForTarget(long targetId) {
/* 3257 */     ArrayList<MissionTrigger> allTriggers = new ArrayList<>();
/* 3258 */     for (int actionId = 496; actionId <= 502; actionId++) {
/*      */       
/* 3260 */       MissionTrigger[] triggers = MissionTriggers.getMissionTriggersWith(-1, actionId, targetId);
/* 3261 */       for (MissionTrigger t : triggers) {
/* 3262 */         allTriggers.add(t);
/*      */       }
/*      */     } 
/* 3265 */     if (allTriggers.isEmpty()) {
/* 3266 */       return null;
/*      */     }
/* 3268 */     return getEpicMissionForMission(((MissionTrigger)allTriggers.get(0)).getMissionRequired());
/*      */   }
/*      */ 
/*      */   
/*      */   public static EpicMission getBuildMissionForTemplate(int templateId) {
/* 3273 */     ArrayList<MissionTrigger> allTriggers = new ArrayList<>();
/* 3274 */     MissionTrigger[] triggers = MissionTriggers.getMissionTriggersWith(templateId, 148, -1L);
/* 3275 */     for (MissionTrigger t : triggers) {
/* 3276 */       allTriggers.add(t);
/*      */     }
/* 3278 */     if (allTriggers.isEmpty()) {
/* 3279 */       return null;
/*      */     }
/* 3281 */     return getEpicMissionForMission(((MissionTrigger)allTriggers.get(0)).getMissionRequired());
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicServerStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
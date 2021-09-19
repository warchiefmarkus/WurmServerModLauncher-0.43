/*      */ package com.wurmonline.server.epic;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.behaviours.Terraforming;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public class EpicTargetItems
/*      */   implements MiscConstants
/*      */ {
/*   50 */   private final long[] epicTargetItems = new long[18];
/*      */   
/*      */   private static final String LOAD_ALL_TARGET_ITEMS = "SELECT * FROM EPICTARGETITEMS WHERE KINGDOM=?";
/*      */   
/*      */   private static final String UPDATE_TARGET_ITEMS = "UPDATE EPICTARGETITEMS SET PILLARONE=?,PILLARTWO=?,PILLARTHREE=?,OBELISQUEONE=?,OBELISQUETWO=?,OBELISQUETHREE=?,PYLONONE=?,PYLONTWO=?,PYLONTHREE=?,TEMPLEONE=?,TEMPLETWO=?,TEMPLETHREE=?,SHRINEONE=?,SHRINETWO=?,SHRINETHREE=?,SPIRITGATEONE=?,SPIRITGATETWO=?,SPIRITGATETHREE=? WHERE KINGDOM=?";
/*      */   
/*      */   private static final String INSERT_TARGET_ITEMS = "INSERT INTO EPICTARGETITEMS (KINGDOM) VALUES(?)";
/*      */   
/*      */   private final byte kingdomId;
/*      */   
/*      */   static final int PILLAR_ONE = 0;
/*      */   
/*      */   static final int PILLAR_TWO = 1;
/*      */   
/*      */   static final int PILLAR_THREE = 2;
/*      */   
/*      */   static final int OBELISK_ONE = 3;
/*      */   
/*      */   static final int OBELISK_TWO = 4;
/*      */   
/*      */   static final int OBELISK_THREE = 5;
/*      */   
/*      */   static final int PYLON_ONE = 6;
/*      */   
/*      */   static final int PYLON_TWO = 7;
/*      */   
/*      */   static final int PYLON_THREE = 8;
/*      */   
/*      */   static final int TEMPLE_ONE = 9;
/*      */   
/*      */   static final int TEMPLE_TWO = 10;
/*      */   static final int TEMPLE_THREE = 11;
/*      */   static final int SHRINE_ONE = 12;
/*      */   static final int SHRINE_TWO = 13;
/*      */   static final int SHRINE_THREE = 14;
/*      */   static final int SPIRIT_GATE_ONE = 15;
/*      */   static final int SPIRIT_GATE_TWO = 16;
/*      */   static final int SPIRIT_GATE_THREE = 17;
/*   88 */   private static final Logger logger = Logger.getLogger(EpicTargetItems.class.getName());
/*   89 */   private static final Map<Byte, EpicTargetItems> KINGDOM_ITEMS = new ConcurrentHashMap<>();
/*      */   
/*   91 */   private static final ArrayList<Item> ritualTargetItems = new ArrayList<>();
/*      */ 
/*      */   
/*      */   static {
/*   95 */     getEpicTargets((byte)4);
/*   96 */     getEpicTargets((byte)1);
/*   97 */     getEpicTargets((byte)2);
/*   98 */     getEpicTargets((byte)3);
/*   99 */     getEpicTargets((byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EpicTargetItems(byte kingdomTemplateId) {
/*  107 */     this.kingdomId = kingdomTemplateId;
/*  108 */     loadAll();
/*  109 */     MissionHelper.loadAll();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeRitualTargetItem(Item ritualItem) {
/*  118 */     if (ritualTargetItems.contains(ritualItem)) {
/*  119 */       ritualTargetItems.remove(ritualItem);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addRitualTargetItem(Item ritualItem) {
/*  128 */     if (ritualItem == null) {
/*      */       return;
/*      */     }
/*  131 */     if (!ritualItem.isEpicTargetItem()) {
/*      */       return;
/*      */     }
/*  134 */     if (ritualItem.isUnfinished()) {
/*      */       return;
/*      */     }
/*  137 */     if (ritualTargetItems.contains(ritualItem)) {
/*      */       return;
/*      */     }
/*  140 */     ritualTargetItems.add(ritualItem);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item getRandomRitualTarget() {
/*  145 */     if (ritualTargetItems.isEmpty()) {
/*  146 */       return null;
/*      */     }
/*  148 */     return ritualTargetItems.get(Server.rand.nextInt(ritualTargetItems.size()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final EpicTargetItems getEpicTargets(byte kingdomTemplateId) {
/*  153 */     EpicTargetItems toReturn = KINGDOM_ITEMS.get(Byte.valueOf(kingdomTemplateId));
/*  154 */     if (toReturn == null) {
/*      */       
/*  156 */       toReturn = new EpicTargetItems(kingdomTemplateId);
/*  157 */       KINGDOM_ITEMS.put(Byte.valueOf(kingdomTemplateId), toReturn);
/*      */     } 
/*  159 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isItemAlreadyEpic(Item itemChecked) {
/*  164 */     for (EpicTargetItems etis : KINGDOM_ITEMS.values()) {
/*      */       
/*  166 */       for (long item : etis.epicTargetItems) {
/*      */         
/*  168 */         if (item == itemChecked.getWurmId())
/*  169 */           return true; 
/*      */       } 
/*      */     } 
/*  172 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getKingdomTemplateId() {
/*  177 */     return this.kingdomId;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void loadAll() {
/*  182 */     Connection dbcon = null;
/*  183 */     PreparedStatement ps = null;
/*  184 */     ResultSet rs = null;
/*      */     
/*  186 */     boolean found = false;
/*      */     
/*      */     try {
/*  189 */       dbcon = DbConnector.getZonesDbCon();
/*  190 */       ps = dbcon.prepareStatement("SELECT * FROM EPICTARGETITEMS WHERE KINGDOM=?");
/*  191 */       ps.setByte(1, this.kingdomId);
/*  192 */       rs = ps.executeQuery();
/*  193 */       while (rs.next())
/*      */       {
/*  195 */         rs.getByte(1);
/*  196 */         for (int x = 0; x <= 17; x++)
/*      */         {
/*  198 */           this.epicTargetItems[x] = rs.getLong(x + 2);
/*      */         }
/*  200 */         found = true;
/*      */       }
/*      */     
/*  203 */     } catch (SQLException sqx) {
/*      */       
/*  205 */       logger.log(Level.WARNING, "Failed to load epic target items.", sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  209 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  210 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  212 */     if (!found) {
/*  213 */       initialize();
/*      */     }
/*      */   }
/*      */   
/*      */   private final void initialize() {
/*  218 */     Connection dbcon = null;
/*  219 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  222 */       dbcon = DbConnector.getZonesDbCon();
/*  223 */       ps = dbcon.prepareStatement("INSERT INTO EPICTARGETITEMS (KINGDOM) VALUES(?)");
/*  224 */       ps.setByte(1, this.kingdomId);
/*  225 */       ps.executeUpdate();
/*      */     }
/*  227 */     catch (SQLException sqx) {
/*      */       
/*  229 */       logger.log(Level.WARNING, "Failed to save epic target status for kingdom " + this.kingdomId, sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  233 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  234 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void testSetCounter(int toSet, long wid) {
/*  240 */     this.epicTargetItems[toSet] = wid;
/*  241 */     update();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isEpicItemWithMission(Item epicItem) {
/*  246 */     for (EpicMission m : EpicServerStatus.getCurrentEpicMissions()) {
/*      */       
/*  248 */       boolean correctItem = false;
/*  249 */       switch (m.getMissionType()) {
/*      */         
/*      */         case 101:
/*  252 */           if (epicItem.getTemplateId() == 717 || epicItem
/*  253 */             .getTemplateId() == 712)
/*  254 */             correctItem = true; 
/*      */           break;
/*      */         case 102:
/*  257 */           if (epicItem.getTemplateId() == 715 || epicItem
/*  258 */             .getTemplateId() == 714)
/*  259 */             correctItem = true; 
/*      */           break;
/*      */         case 103:
/*  262 */           if (epicItem.getTemplateId() == 713 || epicItem
/*  263 */             .getTemplateId() == 716) {
/*  264 */             correctItem = true;
/*      */           }
/*      */           break;
/*      */       } 
/*  268 */       if (correctItem) {
/*      */         
/*  270 */         int placementLocation = getTargetItemPlacement(m.getMissionId());
/*  271 */         int itemLocation = epicItem.getGlobalMapPlacement();
/*  272 */         if (itemLocation == placementLocation)
/*  273 */           return true; 
/*  274 */         if (placementLocation == 0 && epicItem.isInTheNorth())
/*  275 */           return true; 
/*  276 */         if (placementLocation == 2 && epicItem.isInTheEast())
/*  277 */           return true; 
/*  278 */         if (placementLocation == 4 && epicItem.isInTheSouth())
/*  279 */           return true; 
/*  280 */         if (placementLocation == 6 && epicItem.isInTheWest()) {
/*  281 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  285 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean addEpicItem(Item epicItem, Creature performer) {
/*  290 */     if (epicItem.isEpicTargetItem())
/*      */     {
/*  292 */       if (mayBuildEpicItem(epicItem.getTemplateId(), epicItem.getTileX(), epicItem.getTileY(), epicItem.isOnSurface(), performer, performer
/*  293 */           .getKingdomTemplateId())) {
/*      */         
/*  295 */         if (epicItem.getGlobalMapPlacement() == getGlobalMapPlacementRequirement(epicItem.getTemplateId())) {
/*      */           
/*  297 */           logger.log(Level.INFO, performer.getName() + " Correct placement for " + epicItem);
/*  298 */           performer.sendToLoggers("Correct placement for " + epicItem, (byte)2);
/*  299 */           int toSet = getCurrentCounter(epicItem.getTemplateId());
/*  300 */           this.epicTargetItems[toSet] = epicItem.getWurmId();
/*  301 */           update();
/*  302 */           return true;
/*      */         } 
/*      */ 
/*      */         
/*  306 */         logger.log(Level.INFO, performer
/*  307 */             .getName() + " Not proper map placement " + epicItem.getGlobalMapPlacement() + " for " + epicItem
/*  308 */             .getName() + " here at " + epicItem
/*  309 */             .getTileX() + "," + epicItem
/*  310 */             .getTileY() + ": Required " + 
/*  311 */             getGlobalMapPlacementRequirement(epicItem.getTemplateId()));
/*  312 */         performer.sendToLoggers("Not proper map placement " + epicItem
/*  313 */             .getGlobalMapPlacement() + " for " + epicItem.getName() + " here at " + epicItem
/*  314 */             .getTileX() + "," + epicItem
/*  315 */             .getTileY() + ": Required " + 
/*  316 */             getGlobalMapPlacementRequirement(epicItem.getTemplateId()), (byte)2);
/*      */       }
/*      */       else {
/*      */         
/*  320 */         performer.sendToLoggers("May not build " + epicItem.getName() + " here at " + epicItem.getTileX() + "," + epicItem
/*  321 */             .getTileY(), (byte)2);
/*      */       }  } 
/*  323 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int testGetCurrentCounter(int templateId) {
/*  328 */     return getCurrentCounter(templateId);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getCurrentCounter(int itemTemplateId) {
/*  333 */     int toReturn = -1;
/*  334 */     switch (itemTemplateId)
/*      */     
/*      */     { 
/*      */       case 717:
/*  338 */         if (this.epicTargetItems[0] == 0L)
/*  339 */           return 0; 
/*  340 */         if (this.epicTargetItems[1] == 0L)
/*  341 */           return 1; 
/*  342 */         if (this.epicTargetItems[2] == 0L) {
/*  343 */           return 2;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  399 */         return toReturn;case 714: if (this.epicTargetItems[3] == 0L) return 3;  if (this.epicTargetItems[4] == 0L) return 4;  if (this.epicTargetItems[5] == 0L) return 5;  return toReturn;case 713: if (this.epicTargetItems[6] == 0L) return 6;  if (this.epicTargetItems[7] == 0L) return 7;  if (this.epicTargetItems[8] == 0L) return 8;  return toReturn;case 712: if (this.epicTargetItems[12] == 0L) return 12;  if (this.epicTargetItems[13] == 0L) return 13;  if (this.epicTargetItems[14] == 0L) return 14;  return toReturn;case 715: if (this.epicTargetItems[9] == 0L) return 9;  if (this.epicTargetItems[10] == 0L) return 10;  if (this.epicTargetItems[11] == 0L) return 11;  return toReturn;case 716: if (this.epicTargetItems[15] == 0L) return 15;  if (this.epicTargetItems[16] == 0L) return 16;  if (this.epicTargetItems[17] == 0L) return 17;  return toReturn; }  toReturn = -1; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getSymbolNamePartString(Creature performer) {
/*  404 */     String toReturn = "Faith";
/*  405 */     int rand = Server.rand.nextInt(50);
/*  406 */     byte kingdomId = performer.getKingdomTemplateId();
/*  407 */     switch (rand)
/*      */     
/*      */     { 
/*      */       case 0:
/*  411 */         toReturn = "Secrets";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  600 */         return toReturn;case 1: if (kingdomId == 3) { toReturn = "Libila"; } else if (kingdomId == 2) { toReturn = "Magranon"; } else if (performer.getDeity() != null && (performer.getDeity()).number == 1) { toReturn = "Fo"; } else { toReturn = "Vynora"; }  return toReturn;case 2: if (kingdomId == 3) { toReturn = "Hate"; } else if (kingdomId == 2) { toReturn = "Fire"; } else if (performer.getDeity() != null && (performer.getDeity()).number == 1) { toReturn = "Love"; } else { toReturn = "Mysteries"; }  return toReturn;case 3: if (kingdomId == 3) { toReturn = "Revenge"; } else if (kingdomId == 2) { toReturn = "Power"; } else if (performer.getDeity() != null && (performer.getDeity()).number == 1) { toReturn = "Compassion"; } else { toReturn = "Wisdom"; }  return toReturn;case 4: if (kingdomId == 3) { toReturn = "Death"; } else if (kingdomId == 2) { toReturn = "Sand"; } else if (performer.getDeity() != null && (performer.getDeity()).number == 1) { toReturn = "Tree"; } else { toReturn = "Water"; }  return toReturn;case 5: toReturn = "Spirit"; return toReturn;case 6: toReturn = "Soul"; return toReturn;case 7: toReturn = "Hope"; return toReturn;case 8: toReturn = "Despair"; return toReturn;case 9: toReturn = "Luck"; return toReturn;case 10: toReturn = "Heaven"; return toReturn;case 11: toReturn = "Valrei"; return toReturn;case 12: toReturn = "Strength"; return toReturn;case 13: toReturn = "Sleep"; return toReturn;case 14: toReturn = "Tongue"; return toReturn;case 15: toReturn = "Dreams"; return toReturn;case 16: toReturn = "Enlightened"; return toReturn;case 17: toReturn = "Fool"; return toReturn;case 18: toReturn = "Cat"; return toReturn;case 19: toReturn = "Troll"; return toReturn;case 20: toReturn = "Dragon"; return toReturn;case 21: toReturn = "Deep"; return toReturn;case 22: toReturn = "Square"; return toReturn;case 23: toReturn = "Song"; return toReturn;case 24: toReturn = "Jump"; return toReturn;case 25: toReturn = "High"; return toReturn;case 26: toReturn = "Low"; return toReturn;case 27: toReturn = "Inbetween"; return toReturn;case 28: toReturn = "One"; return toReturn;case 29: toReturn = "Many"; return toReturn;case 30: toReturn = "Sorrow"; return toReturn;case 31: toReturn = "Pain"; return toReturn;case 32: toReturn = "Oracle"; return toReturn;case 33: toReturn = "Slithering"; return toReturn;case 34: toReturn = "Roundabout"; return toReturn;case 35: toReturn = "Winter"; return toReturn;case 36: toReturn = "Summer"; return toReturn;case 37: toReturn = "Fallen"; return toReturn;case 38: toReturn = "Cherry"; return toReturn;case 39: toReturn = "Innocent"; return toReturn;case 40: toReturn = "Demon"; return toReturn;case 41: toReturn = "Left"; return toReturn;case 42: toReturn = "Shard"; return toReturn;case 43: toReturn = "Mantra"; return toReturn;case 44: toReturn = "Island"; return toReturn;case 45: toReturn = "Seafarer"; return toReturn;case 46: toReturn = "Ascendant"; return toReturn;case 47: toReturn = "Shame"; return toReturn;case 48: toReturn = "Running"; return toReturn;case 49: toReturn = "Lamentation"; return toReturn; }  toReturn = "Figure"; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getTypeNamePartString(int itemTemplateId) {
/*  607 */     String toReturn = "Focus";
/*  608 */     int rand = Server.rand.nextInt(10);
/*  609 */     toReturn = getTypeNamePartStringWithPart(itemTemplateId, rand);
/*  610 */     return toReturn;
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
/*      */   static final String getTypeNamePartStringWithPart(int itemTemplateId, int partId) {
/*  625 */     switch (itemTemplateId)
/*      */     
/*      */     { 
/*      */       case 717:
/*  629 */         switch (partId)
/*      */         
/*      */         { case 0:
/*  632 */             toReturn = "Pillar";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  864 */             return toReturn;case 1: toReturn = "Foundation"; return toReturn;case 2: toReturn = "Ram"; return toReturn;case 3: toReturn = "Symbol"; return toReturn;case 4: toReturn = "Tower"; return toReturn;case 5: toReturn = "Post"; return toReturn;case 6: toReturn = "Column"; return toReturn;case 7: toReturn = "Backbone"; return toReturn;case 8: toReturn = "Menhir"; return toReturn;case 9: toReturn = "Last Stand"; return toReturn; }  toReturn = "Pillar"; return toReturn;case 714: switch (partId) { case 0: toReturn = "Needle"; return toReturn;case 1: toReturn = "Fist"; return toReturn;case 2: toReturn = "Obelisk"; return toReturn;case 3: toReturn = "Charge"; return toReturn;case 4: toReturn = "Mantra"; return toReturn;case 5: toReturn = "Testimonial"; return toReturn;case 6: toReturn = "Trophy"; return toReturn;case 7: toReturn = "Stand"; return toReturn;case 8: toReturn = "Spear"; return toReturn;case 9: toReturn = "Challenge"; return toReturn; }  toReturn = "Obelisk"; return toReturn;case 713: switch (partId) { case 0: toReturn = "Memento"; return toReturn;case 1: toReturn = "Monument"; return toReturn;case 2: toReturn = "Path"; return toReturn;case 3: toReturn = "Way"; return toReturn;case 4: toReturn = "Door"; return toReturn;case 5: toReturn = "Victorial"; return toReturn;case 6: toReturn = "Shield"; return toReturn;case 7: toReturn = "Passage"; return toReturn;case 8: toReturn = "Rest"; return toReturn;case 9: toReturn = "Gate"; return toReturn; }  toReturn = "Pylon"; return toReturn;case 712: switch (partId) { case 0: toReturn = "Shrine"; return toReturn;case 1: toReturn = "Barrow"; return toReturn;case 2: toReturn = "Vault"; return toReturn;case 3: toReturn = "Long Home"; return toReturn;case 4: toReturn = "Mausoleum"; return toReturn;case 5: toReturn = "Chamber"; return toReturn;case 6: toReturn = "Reliquary"; return toReturn;case 7: toReturn = "Remembrance"; return toReturn;case 8: toReturn = "Sacrarium"; return toReturn;case 9: toReturn = "Sanctum"; return toReturn; }  toReturn = "Shrine"; return toReturn;case 715: switch (partId) { case 0: toReturn = "Church"; return toReturn;case 1: toReturn = "Temple"; return toReturn;case 2: toReturn = "Hand"; return toReturn;case 3: toReturn = "House"; return toReturn;case 4: toReturn = "Sanctuary"; return toReturn;case 5: toReturn = "Chapel"; return toReturn;case 6: toReturn = "Abode"; return toReturn;case 7: toReturn = "Walls"; return toReturn;case 8: toReturn = "Sign"; return toReturn;case 9: toReturn = "Fist"; return toReturn; }  toReturn = "Temple"; return toReturn;case 716: switch (partId) { case 0: toReturn = "Pathway"; return toReturn;case 1: toReturn = "Mirror"; return toReturn;case 2: toReturn = "Mystery"; return toReturn;case 3: toReturn = "Gate"; return toReturn;case 4: toReturn = "Shimmer"; return toReturn;case 5: toReturn = "Route"; return toReturn;case 6: toReturn = "Run"; return toReturn;case 7: toReturn = "Trail"; return toReturn;case 8: toReturn = "Wake"; return toReturn;case 9: toReturn = "Secret"; return toReturn; }  toReturn = "Gate"; return toReturn; }  String toReturn = "Monument"; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getInstructionString(int itemTemplateId) {
/*  869 */     return getInstructionStringForKingdom(itemTemplateId, this.kingdomId);
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
/*      */   public static final String getInstructionStringForKingdom(int itemTemplateId, byte aKingdomId) {
/*  883 */     switch (itemTemplateId)
/*      */     
/*      */     { case 717:
/*  886 */         toReturn = "This should be built in the darkness of a cave with sufficient ceiling height, not inside a settlement, and on a flat surface.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  930 */         return toReturn;case 714: toReturn = "This must be constructed on a 3x3 slabbed area, not inside a settlement, and on a flat surface."; return toReturn;case 713: toReturn = "This must be constructed on a 7x7 slabbed area close to water, not inside a settlement, and on a flat surface."; return toReturn;case 712: toReturn = "This must be constructed on a 5x5 slabbed area, not inside a settlement, and on a flat surface. A couple of fruit trees or bushes must be within 5 tiles."; return toReturn;case 715: if (aKingdomId == 3) { toReturn = "This must be constructed on a 5x5 slabbed area, not inside a settlement, and on a flat surface. It must be within 5 tiles of marsh or mycelium."; } else { toReturn = "This must be constructed on a 5x5 slabbed area, not inside a settlement, and on a flat surface. It must be built higher up than 100 steps."; }  return toReturn;case 716: if (aKingdomId == 3) { if (Servers.localServer.PVPSERVER) { toReturn = "This must be constructed on a 5x5 slabbed area, not inside a settlement, and on a flat surface. It must be within 5 tiles of marsh as well as mycelium."; } else { toReturn = "This must be constructed on a 5x5 slabbed area, not inside a settlement, and on a flat surface. It must be within 5 tiles of marsh as well as moss."; }  } else { toReturn = "This must be constructed on a 5x5 slabbed area, not inside a settlement, and on a flat surface. It must be built higher up than 100 steps."; }  return toReturn; }  String toReturn = "It is not the right time to build this now."; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getGlobalMapPlacementRequirementString(int itemTemplateId) {
/*  935 */     int placement = getGlobalMapPlacementRequirement(itemTemplateId);
/*      */ 
/*      */     
/*  938 */     switch (placement)
/*      */     
/*      */     { case 3:
/*  941 */         toReturn = "This must be built in the south east.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  956 */         return toReturn;case 5: toReturn = "This must be built in the south west."; return toReturn;case 7: toReturn = "This must be built in the north west."; return toReturn;case 1: toReturn = "This must be built in the north east."; return toReturn; }  String toReturn = "It is not the right time to build this now."; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getGlobalMapPlacementRequirement(int itemTemplateId) {
/*  961 */     int counter = getCurrentCounter(itemTemplateId);
/*      */     
/*  963 */     int toReturn = 0;
/*  964 */     if (counter <= -1) {
/*  965 */       return toReturn;
/*      */     }
/*  967 */     toReturn = getGlobalMapPlacementRequirementWithCounter(itemTemplateId, counter, this.kingdomId);
/*  968 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getTargetItemPlacementString(int placementLocation) {
/*  973 */     switch (placementLocation) {
/*      */       
/*      */       case 0:
/*  976 */         return "This must be built in the north.";
/*      */       case 1:
/*  978 */         return "This must be built in the northeast.";
/*      */       case 2:
/*  980 */         return "This must be built in the east.";
/*      */       case 3:
/*  982 */         return "This must be built in the southeast.";
/*      */       case 4:
/*  984 */         return "This must be built in the south.";
/*      */       case 5:
/*  986 */         return "This must be built in the southwest.";
/*      */       case 6:
/*  988 */         return "This must be built in the west.";
/*      */       case 7:
/*  990 */         return "This must be built in the northwest.";
/*      */     } 
/*  992 */     return "It is not the right time to build this now.";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getTargetItemPlacement(int missionId) {
/*  999 */     Random r = new Random(missionId);
/* 1000 */     return r.nextInt(8);
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
/*      */   static final int getGlobalMapPlacementRequirementWithCounter(int aItemTemplateId, int aCounter, byte aKingdomId) {
/* 1014 */     int toReturn = 0;
/* 1015 */     switch (aItemTemplateId)
/*      */     
/*      */     { 
/*      */       case 717:
/* 1019 */         switch (aKingdomId)
/*      */         
/*      */         { 
/*      */           case 2:
/* 1023 */             switch (aCounter)
/*      */             
/*      */             { case 0:
/* 1026 */                 toReturn = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1429 */                 return toReturn;case 1: toReturn = 1; return toReturn;case 2: toReturn = 3; return toReturn; }  toReturn = 0; return toReturn;case 3: switch (aCounter) { case 0: toReturn = 7; return toReturn;case 1: toReturn = 1; return toReturn;case 2: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn;case 1: case 4: switch (aCounter) { case 0: toReturn = 3; return toReturn;case 1: toReturn = 7; return toReturn;case 2: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn; }  toReturn = 0; return toReturn;case 714: switch (aKingdomId) { case 2: switch (aCounter) { case 3: toReturn = 7; return toReturn;case 4: toReturn = 3; return toReturn;case 5: toReturn = 1; return toReturn; }  toReturn = 0; return toReturn;case 3: switch (aCounter) { case 3: toReturn = 7; return toReturn;case 4: toReturn = 3; return toReturn;case 5: toReturn = 1; return toReturn; }  toReturn = 0; return toReturn;case 1: case 4: switch (aCounter) { case 3: toReturn = 7; return toReturn;case 4: toReturn = 3; return toReturn;case 5: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn; }  toReturn = 0; return toReturn;case 713: switch (aKingdomId) { case 2: switch (aCounter) { case 6: toReturn = 3; return toReturn;case 7: toReturn = 1; return toReturn;case 8: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn;case 3: switch (aCounter) { case 6: toReturn = 5; return toReturn;case 7: toReturn = 7; return toReturn;case 8: toReturn = 1; return toReturn; }  toReturn = 0; return toReturn;case 1: case 4: switch (aCounter) { case 6: toReturn = 3; return toReturn;case 7: toReturn = 7; return toReturn;case 8: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn; }  toReturn = 0; return toReturn;case 712: switch (aKingdomId) { case 2: switch (aCounter) { case 12: toReturn = 1; return toReturn;case 13: toReturn = 7; return toReturn;case 14: toReturn = 3; return toReturn; }  toReturn = 0; return toReturn;case 3: switch (aCounter) { case 12: toReturn = 1; return toReturn;case 13: toReturn = 3; return toReturn;case 14: toReturn = 7; return toReturn; }  toReturn = 0; return toReturn;case 1: case 4: switch (aCounter) { case 12: toReturn = 3; return toReturn;case 13: toReturn = 7; return toReturn;case 14: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn; }  toReturn = 0; return toReturn;case 715: switch (aKingdomId) { case 2: switch (aCounter) { case 9: toReturn = 3; return toReturn;case 10: toReturn = 1; return toReturn;case 11: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn;case 3: switch (aCounter) { case 9: toReturn = 7; return toReturn;case 10: toReturn = 5; return toReturn;case 11: toReturn = 1; return toReturn; }  toReturn = 0; return toReturn;case 1: case 4: switch (aCounter) { case 9: toReturn = 3; return toReturn;case 10: toReturn = 7; return toReturn;case 11: toReturn = 5; return toReturn; }  toReturn = 0; return toReturn; }  toReturn = 0; return toReturn;case 716: switch (aKingdomId) { case 2: switch (aCounter) { case 15: toReturn = 7; return toReturn;case 16: toReturn = 1; return toReturn;case 17: toReturn = 3; return toReturn; }  toReturn = 0; return toReturn;case 3: switch (aCounter) { case 15: toReturn = 1; return toReturn;case 16: toReturn = 7; return toReturn;case 17: toReturn = 3; return toReturn; }  toReturn = 0; return toReturn;case 1: case 4: switch (aCounter) { case 15: toReturn = 5; return toReturn;case 16: toReturn = 3; return toReturn;case 17: toReturn = 7; return toReturn; }  toReturn = 0; return toReturn; }  toReturn = 0; return toReturn; }  toReturn = 0; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean mayBuildEpicItem(int itemTemplateId, int tilex, int tiley, boolean surfaced, Creature performer, byte kingdomTemplateId) {
/*      */     int x;
/* 1435 */     if (!Terraforming.isFlat(tilex, tiley, surfaced, 4)) {
/*      */       
/* 1437 */       performer.sendToLoggers("The tile is not flat", (byte)2);
/* 1438 */       return false;
/*      */     } 
/* 1440 */     if (Villages.getVillage(tilex, tiley, surfaced) != null)
/* 1441 */       return false; 
/* 1442 */     boolean toReturn = true;
/* 1443 */     switch (itemTemplateId)
/*      */     
/*      */     { 
/*      */       
/*      */       case 717:
/* 1448 */         toReturn = false;
/* 1449 */         if (!surfaced) {
/*      */           
/* 1451 */           toReturn = true;
/* 1452 */           int cornerNorthW = Server.caveMesh.getTile(tilex, tiley);
/* 1453 */           short ceilHeight = (short)(Tiles.decodeData(cornerNorthW) & 0xFF);
/* 1454 */           if (ceilHeight < 50) {
/*      */             
/* 1456 */             performer.sendToLoggers("The NW corner is too low " + ceilHeight, (byte)2);
/* 1457 */             toReturn = false;
/*      */           } 
/* 1459 */           int cornerNorthE = Server.caveMesh.getTile(tilex + 1, tiley);
/* 1460 */           short ceilHeightNE = (short)(Tiles.decodeData(Tiles.decodeTileData(cornerNorthE)) & 0xFF);
/*      */           
/* 1462 */           if (ceilHeightNE < 50) {
/*      */             
/* 1464 */             performer.sendToLoggers("The NE corner is too low " + ceilHeightNE, (byte)2);
/* 1465 */             toReturn = false;
/*      */           } 
/* 1467 */           int cornerSE = Server.caveMesh.getTile(tilex + 1, tiley + 1);
/* 1468 */           short ceilHeightSE = (short)(Tiles.decodeData(Tiles.decodeTileData(cornerSE)) & 0xFF);
/*      */           
/* 1470 */           if (ceilHeightSE < 50) {
/*      */             
/* 1472 */             performer.sendToLoggers("The SE corner is too low " + ceilHeightSE, (byte)2);
/* 1473 */             toReturn = false;
/*      */           } 
/* 1475 */           int cornerSW = Server.caveMesh.getTile(tilex, tiley + 1);
/* 1476 */           short ceilHeightSW = (short)(Tiles.decodeData(Tiles.decodeTileData(cornerSW)) & 0xFF);
/*      */           
/* 1478 */           if (ceilHeightSW < 50) {
/*      */             
/* 1480 */             performer.sendToLoggers("The SW corner is too low " + ceilHeightSW, (byte)2);
/* 1481 */             toReturn = false;
/*      */           } 
/*      */         } else {
/*      */           
/* 1485 */           performer.sendToLoggers("The pillar is on the surface!", (byte)2);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1711 */         return toReturn;case 714: toReturn = true; for (x = Zones.safeTileX(tilex - 1); x <= Zones.safeTileX(tilex + 1); x++) { for (int y = Zones.safeTileY(tiley - 1); y <= Zones.safeTileY(tiley + 1); y++) { if (!Terraforming.isFlat(x, y, true, 4)) toReturn = false;  if (!Tiles.isRoadType(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)))) toReturn = false;  }  }  return toReturn;case 713: toReturn = true; for (x = Zones.safeTileX(tilex - 3); x <= Zones.safeTileX(tilex + 3); x++) { for (int y = Zones.safeTileY(tiley - 3); y <= Zones.safeTileY(tiley + 3); y++) { if (!Terraforming.isFlat(x, y, true, 4)) { toReturn = false; break; }  if (!Tiles.isRoadType(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)))) { toReturn = false; break; }  }  }  if (toReturn == true) { toReturn = false; for (x = Zones.safeTileX(tilex - 10); x <= Zones.safeTileX(tilex + 10); x += 5) { for (int y = Zones.safeTileY(tiley - 10); y <= Zones.safeTileY(tiley + 10); y += 5) { if (Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)) < 0) { toReturn = true; break; }  }  }  }  return toReturn;case 712: for (x = Zones.safeTileX(tilex - 2); x <= Zones.safeTileX(tilex + 2); x++) { for (int y = Zones.safeTileY(tiley - 2); y <= Zones.safeTileY(tiley + 2); y++) { if (!Terraforming.isFlat(x, y, true, 4)) { toReturn = false; break; }  if (!Tiles.isRoadType(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)))) { toReturn = false; break; }  }  }  if (toReturn == true) { toReturn = false; int numSmalltrees = 0; for (int i = Zones.safeTileX(tilex - 5); i <= Zones.safeTileX(tilex + 5); i++) { for (int y = Zones.safeTileY(tiley - 5); y <= Zones.safeTileY(tiley + 5); y++) { int t = Server.surfaceMesh.getTile(i, y); Tiles.Tile theTile = Tiles.getTile(Tiles.decodeType(t)); byte data = Tiles.decodeData(t); if (theTile.isNormalTree()) { if (theTile.getTreeType(data).isFruitTree()) numSmalltrees++;  } else if (theTile.isMyceliumTree() && kingdomTemplateId == 3) { if (theTile.getTreeType(data).isFruitTree()) numSmalltrees++;  }  }  }  if (numSmalltrees > 3) toReturn = true;  }  return toReturn;case 715: for (x = Zones.safeTileX(tilex - 2); x <= Zones.safeTileX(tilex + 2); x++) { for (int y = Zones.safeTileY(tiley - 2); y <= Zones.safeTileY(tiley + 2); y++) { if (!Terraforming.isFlat(x, y, true, 4)) { toReturn = false; break; }  if (!Tiles.isRoadType(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)))) { toReturn = false; break; }  }  }  if (toReturn == true) { toReturn = false; if (kingdomTemplateId == 3) { for (x = Zones.safeTileX(tilex - 5); x <= Zones.safeTileX(tilex + 5); x++) { for (int y = Zones.safeTileY(tiley - 5); y <= Zones.safeTileY(tiley + 5); y++) { int t = Server.surfaceMesh.getTile(x, y); byte type = Tiles.decodeType(t); if (Tiles.decodeType(t) == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_MYCELIUM.id || Tiles.getTile(type).isMyceliumTree()) { toReturn = true; break; }  }  }  } else { int t = Server.surfaceMesh.getTile(tilex, tiley); if (Tiles.decodeHeight(t) > 1000) toReturn = true;  }  }  return toReturn;case 716: for (x = Zones.safeTileX(tilex - 2); x <= Zones.safeTileX(tilex + 2); x++) { for (int y = Zones.safeTileY(tiley - 2); y <= Zones.safeTileY(tiley + 2); y++) { if (!Terraforming.isFlat(x, y, true, 4)) { toReturn = false; break; }  if (!Tiles.isRoadType(Tiles.decodeType(Server.surfaceMesh.getTile(x, y)))) { toReturn = false; break; }  }  }  if (toReturn == true) { toReturn = false; if (kingdomTemplateId == 3) { boolean foundMycel = false; boolean foundMarsh = false; for (int i = Zones.safeTileX(tilex - 5); i <= Zones.safeTileX(tilex + 5); i++) { for (int y = Zones.safeTileY(tiley - 5); y <= Zones.safeTileY(tiley + 5); y++) { int t = Server.surfaceMesh.getTile(i, y); if (Servers.localServer.PVPSERVER) { byte type = Tiles.decodeType(t); if (type == Tiles.Tile.TILE_MYCELIUM.id || Tiles.getTile(type).isMyceliumTree()) { foundMycel = true; continue; }  } else if (Tiles.decodeType(t) == Tiles.Tile.TILE_MOSS.id) { foundMycel = true; continue; }  if (Tiles.decodeType(t) == Tiles.Tile.TILE_MARSH.id) foundMarsh = true;  continue; }  }  if (foundMycel && foundMarsh) toReturn = true;  } else { int t = Server.surfaceMesh.getTile(tilex, tiley); if (Tiles.decodeHeight(t) > 1000) toReturn = true;  }  }  return toReturn; }  toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void update() {
/* 1716 */     Connection dbcon = null;
/* 1717 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1720 */       dbcon = DbConnector.getZonesDbCon();
/* 1721 */       ps = dbcon.prepareStatement("UPDATE EPICTARGETITEMS SET PILLARONE=?,PILLARTWO=?,PILLARTHREE=?,OBELISQUEONE=?,OBELISQUETWO=?,OBELISQUETHREE=?,PYLONONE=?,PYLONTWO=?,PYLONTHREE=?,TEMPLEONE=?,TEMPLETWO=?,TEMPLETHREE=?,SHRINEONE=?,SHRINETWO=?,SHRINETHREE=?,SPIRITGATEONE=?,SPIRITGATETWO=?,SPIRITGATETHREE=? WHERE KINGDOM=?");
/* 1722 */       for (int x = 0; x <= 17; x++)
/* 1723 */         ps.setLong(x + 1, this.epicTargetItems[x]); 
/* 1724 */       ps.setByte(19, this.kingdomId);
/* 1725 */       ps.executeUpdate();
/*      */     }
/* 1727 */     catch (SQLException sqx) {
/*      */       
/* 1729 */       logger.log(Level.WARNING, "Failed to save epic target status for kingdom " + this.kingdomId, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1733 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1734 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final long getRandomTarget() {
/* 1740 */     return getRandomTarget(0, 0, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final long getRandomTarget(int attempts, int targetTemplate, @Nullable ArrayList<Long> itemList) {
/* 1746 */     long itemFound = -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1751 */     if (Servers.localServer.PVPSERVER) {
/*      */       
/* 1753 */       int numsExisting = 0;
/*      */       int x;
/* 1755 */       for (x = 0; x < 17; x++) {
/*      */         
/* 1757 */         if (this.epicTargetItems[x] > 0L)
/* 1758 */           numsExisting++; 
/*      */       } 
/* 1760 */       if (numsExisting > 0)
/*      */       {
/*      */         
/* 1763 */         for (x = 0; x < 17; x++) {
/*      */           
/* 1765 */           if (this.epicTargetItems[x] > 0L) {
/*      */             
/*      */             try {
/*      */               
/* 1769 */               Item eti = Items.getItem(this.epicTargetItems[x]);
/* 1770 */               Village v = Villages.getVillage(eti.getTilePos(), eti.isOnSurface());
/*      */ 
/*      */ 
/*      */               
/* 1774 */               if (v == null) {
/*      */                 
/* 1776 */                 if (itemFound == -1L) {
/* 1777 */                   itemFound = this.epicTargetItems[x];
/* 1778 */                 } else if (Server.rand.nextInt(numsExisting) == 0) {
/* 1779 */                   itemFound = this.epicTargetItems[x];
/*      */                 } 
/*      */               } else {
/*      */                 
/* 1783 */                 logger.info("Disqualified Epic Mission Target item due to being in village " + v.getName() + ": Name: " + eti
/* 1784 */                     .getName() + " | WurmID: " + eti.getWurmId() + " | TileX: " + eti.getTileX() + " | TileY: " + eti
/* 1785 */                     .getTileY());
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/* 1791 */             catch (NoSuchItemException nsie) {
/*      */               
/* 1793 */               logger.warning("Epic mission item could not be found when loaded, maybe it was wrongfully deleted? WurmID:" + this.epicTargetItems[x] + ". " + nsie);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       int templateId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1807 */       if (logger.isLoggable(Level.FINE)) {
/* 1808 */         logger.fine("Entering Freedom Version of Valrei Mission Target Structure selection.");
/*      */       }
/* 1810 */       Connection dbcon = null;
/* 1811 */       PreparedStatement ps1 = null;
/* 1812 */       ResultSet rs = null;
/*      */       
/* 1814 */       int structureType = Server.rand.nextInt(6);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1820 */       if (targetTemplate > 0) {
/*      */         
/* 1822 */         templateId = targetTemplate;
/*      */       }
/*      */       else {
/*      */         
/* 1826 */         switch (structureType) {
/*      */           
/*      */           case 0:
/* 1829 */             templateId = 717;
/*      */             break;
/*      */           case 1:
/* 1832 */             templateId = 714;
/*      */             break;
/*      */           case 2:
/* 1835 */             templateId = 713;
/*      */             break;
/*      */           case 3:
/* 1838 */             templateId = 715;
/*      */             break;
/*      */           case 4:
/* 1841 */             templateId = 712;
/*      */             break;
/*      */           case 5:
/* 1844 */             templateId = 716;
/*      */             break;
/*      */           default:
/* 1847 */             templateId = 713;
/*      */             break;
/*      */         } 
/*      */ 
/*      */       
/*      */       } 
/* 1853 */       if (logger.isLoggable(Level.FINE)) {
/* 1854 */         logger.fine("Selected template with id=" + templateId);
/*      */       }
/* 1856 */       if (itemList == null) {
/*      */         
/* 1858 */         itemList = new ArrayList<>();
/*      */ 
/*      */         
/*      */         try {
/* 1862 */           String dbQueryString = "SELECT WURMID FROM ITEMS WHERE TEMPLATEID=?";
/*      */           
/* 1864 */           if (logger.isLoggable(Level.FINER)) {
/* 1865 */             logger.finer("Query String [ SELECT WURMID FROM ITEMS WHERE TEMPLATEID=? ]");
/*      */           }
/* 1867 */           dbcon = DbConnector.getItemDbCon();
/* 1868 */           ps1 = dbcon.prepareStatement("SELECT WURMID FROM ITEMS WHERE TEMPLATEID=?");
/* 1869 */           ps1.setInt(1, templateId);
/* 1870 */           rs = ps1.executeQuery();
/*      */           
/* 1872 */           while (rs.next())
/*      */           {
/* 1874 */             long currentLong = rs.getLong("WURMID");
/* 1875 */             if (currentLong > 0L)
/*      */             {
/* 1877 */               itemList.add(Long.valueOf(currentLong));
/*      */             }
/* 1879 */             if (logger.isLoggable(Level.FINEST)) {
/* 1880 */               logger.finest(rs.toString());
/*      */             }
/*      */           }
/*      */         
/* 1884 */         } catch (SQLException ex) {
/*      */           
/* 1886 */           logger.log(Level.WARNING, "Failed to locate mission items with templateid=" + templateId, ex);
/*      */         }
/*      */         finally {
/*      */           
/* 1890 */           DbUtilities.closeDatabaseObjects(ps1, null);
/* 1891 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1896 */       if (itemList.size() > 0) {
/*      */         
/* 1898 */         int randomIndex = Server.rand.nextInt(itemList.size());
/*      */         
/* 1900 */         if (itemList.get(randomIndex) != null)
/*      */         {
/*      */           
/* 1903 */           long selectedTarget = ((Long)itemList.get(randomIndex)).longValue();
/*      */ 
/*      */           
/*      */           try {
/* 1907 */             Item eti = Items.getItem(selectedTarget);
/* 1908 */             Village v = Villages.getVillage(eti.getTilePos(), eti.isOnSurface());
/*      */ 
/*      */ 
/*      */             
/* 1912 */             if (v == null) {
/*      */               
/* 1914 */               logger.info("Selected mission target with wurmid=" + selectedTarget);
/* 1915 */               return selectedTarget;
/*      */             } 
/*      */ 
/*      */             
/* 1919 */             logger.info("Disqualified Epic Mission Target item due to being in village " + v.getName() + ": Name: " + eti
/* 1920 */                 .getName() + " | WurmID: " + eti.getWurmId() + " | TileX: " + eti.getTileX() + " | TileY: " + eti
/* 1921 */                 .getTileY());
/*      */ 
/*      */             
/* 1924 */             int ATTEMPT_NUMBER_OF_TIMES = 25;
/*      */             
/* 1926 */             if (attempts < 25) {
/*      */               
/* 1928 */               logger.fine("Failing roll number " + attempts + "/" + '\031' + " and trying again.");
/* 1929 */               return getRandomTarget(attempts + 1, templateId, itemList);
/*      */             } 
/*      */ 
/*      */             
/* 1933 */             logger.info("Failing roll of finding structure with templateID=" + templateId + " completely,  could not find any mission structure not in a village in " + '\031' + " tries.");
/*      */             
/* 1935 */             return -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1943 */           catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           
/* 1951 */           logger.warning("WURMID was null for item with templateId=" + templateId);
/* 1952 */           return -1L;
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1958 */         logger.info("Couldn't find any items with itemtemplate=" + templateId + " failing, the roll.");
/* 1959 */         return -1L;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1964 */     return itemFound;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final int getNextBuildTarget(int difficulty) {
/* 1970 */     difficulty = Math.min(5, difficulty);
/* 1971 */     int start = difficulty * 3;
/* 1972 */     int templateFound = -1;
/*      */     int x;
/* 1974 */     for (x = start; x < 17; x++) {
/*      */       
/* 1976 */       if (this.epicTargetItems[x] <= 0L) {
/*      */         
/* 1978 */         templateFound = x;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1983 */     if (templateFound == -1)
/*      */     {
/*      */       
/* 1986 */       for (x = start; x > 0; x--) {
/*      */         
/* 1988 */         if (this.epicTargetItems[x] <= 0L) {
/*      */           
/* 1990 */           templateFound = x;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 1996 */     if (templateFound > -1) {
/*      */       
/* 1998 */       if (templateFound < 3)
/* 1999 */         return 717; 
/* 2000 */       if (templateFound < 6)
/* 2001 */         return 714; 
/* 2002 */       if (templateFound < 9)
/* 2003 */         return 713; 
/* 2004 */       if (templateFound < 12)
/* 2005 */         return 715; 
/* 2006 */       if (templateFound < 15) {
/* 2007 */         return 712;
/*      */       }
/* 2009 */       return 716;
/*      */     } 
/* 2011 */     return -1;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicTargetItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
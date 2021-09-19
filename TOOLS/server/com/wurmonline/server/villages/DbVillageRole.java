/*      */ package com.wurmonline.server.villages;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
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
/*      */ public final class DbVillageRole
/*      */   extends VillageRole
/*      */   implements VillageStatus, Comparable<DbVillageRole>
/*      */ {
/*   37 */   private static final Logger logger = Logger.getLogger(DbVillageRole.class.getName());
/*      */   
/*      */   private static final String CREATE_ROLE = "INSERT INTO VILLAGEROLE (VILLAGEID,NAME ,MAYTERRAFORM ,MAYCUTTREE ,MAYMINE ,MAYFARM ,MAYBUILD ,MAYHIRE,MAYINVITE,MAYDESTROY,MAYMANAGEROLES, MAYEXPAND,MAYLOCKFENCES, MAYPASSFENCES,DIPLOMAT, MAYATTACKCITIZ, MAYATTACKNONCITIZ,MAYFISH,MAYCUTOLD, STATUS,VILLAGEAPPLIEDTO,MAYPUSHPULLTURN,MAYUPDATEMAP,MAYLEAD,MAYPICKUP,MAYTAME,MAYLOAD,MAYBUTCHER,MAYATTACHLOCK,MAYPICKLOCKS,PLAYERAPPLIEDTO,SETTINGS,MORESETTINGS,EXTRASETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */   
/*      */   private static final String UPDATE_ROLE = "UPDATE VILLAGEROLE SET VILLAGEID=?,NAME=?,MAYTERRAFORM=?,MAYCUTTREE=?,MAYMINE=?,MAYFARM=?,MAYBUILD=?,MAYHIRE=?,MAYINVITE=?,MAYDESTROY=?,MAYMANAGEROLES=?,MAYEXPAND=?,MAYLOCKFENCES=?,MAYPASSFENCES=?,DIPLOMAT=?,MAYATTACKCITIZ=?,MAYATTACKNONCITIZ=?,MAYFISH=?,MAYCUTOLD=?,STATUS=?,VILLAGEAPPLIEDTO=?,MAYPUSHPULLTURN=?,MAYUPDATEMAP=?,MAYLEAD=?,MAYPICKUP=?,MAYTAME=?,MAYLOAD=?,MAYBUTCHER=?,MAYATTACHLOCK=?,MAYPICKLOCKS=?,PLAYERAPPLIEDTO=?,SETTINGS=?,MORESETTINGS=?,EXTRASETTINGS=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_NAME = "UPDATE VILLAGEROLE SET NAME=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYTERRAFORM = "UPDATE VILLAGEROLE SET MAYTERRAFORM=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYCUTTREE = "UPDATE VILLAGEROLE SET MAYCUTTREE=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYMINE = "UPDATE VILLAGEROLE SET MAYMINE=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYBUILD = "UPDATE VILLAGEROLE SET MAYBUILD=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYHIRE = "UPDATE VILLAGEROLE SET MAYHIRE=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYINVITE = "UPDATE VILLAGEROLE SET MAYINVITE=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYDESTROY = "UPDATE VILLAGEROLE SET MAYDESTROY=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYMANAGEROLES = "UPDATE VILLAGEROLE SET MAYMANAGEROLES=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYFARM = "UPDATE VILLAGEROLE SET MAYFARM=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYEXPAND = "UPDATE VILLAGEROLE SET MAYEXPAND=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYLOCKFENCES = "UPDATE VILLAGEROLE SET MAYLOCKFENCES=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYPASSFENCES = "UPDATE VILLAGEROLE SET MAYPASSFENCES=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYATTACKCITIZENS = "UPDATE VILLAGEROLE SET MAYATTACKCITIZ=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYATTACKNONCITIZENS = "UPDATE VILLAGEROLE SET MAYATTACKNONCITIZ=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYFISH = "UPDATE VILLAGEROLE SET MAYFISH=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYCUTOLD = "UPDATE VILLAGEROLE SET MAYCUTOLD=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_DIPLOMAT = "UPDATE VILLAGEROLE SET DIPLOMAT=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_VILLAGEAPPLIEDTO = "UPDATE VILLAGEROLE SET VILLAGEAPPLIEDTO=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYPUSHPULLTURN = "UPDATE VILLAGEROLE SET MAYPUSHPULLTURN=? WHERE ID=?";
/*      */   private static final String SET_MAYUPDATEMAP = "UPDATE VILLAGEROLE SET MAYUPDATEMAP=? WHERE ID=?";
/*      */   private static final String SET_MAYLEAD = "UPDATE VILLAGEROLE SET MAYLEAD=? WHERE ID=?";
/*      */   private static final String SET_MAYPICKUP = "UPDATE VILLAGEROLE SET MAYPICKUP=? WHERE ID=?";
/*      */   private static final String SET_MAYTAME = "UPDATE VILLAGEROLE SET MAYTAME=? WHERE ID=?";
/*      */   private static final String SET_MAYLOAD = "UPDATE VILLAGEROLE SET MAYLOAD=? WHERE ID=?";
/*      */   private static final String SET_MAYBUTCHER = "UPDATE VILLAGEROLE SET MAYBUTCHER=? WHERE ID=?";
/*      */   private static final String SET_MAYATTACHLOCK = "UPDATE VILLAGEROLE SET MAYATTACHLOCK=? WHERE ID=?";
/*      */   private static final String SET_MAYPICKLOCKS = "UPDATE VILLAGEROLE SET MAYPICKLOCKS=? WHERE ID=?";
/*      */   private static final String DELETE = "DELETE FROM VILLAGEROLE WHERE ID=?";
/*      */   
/*      */   public DbVillageRole(int aVillageId, String aName, boolean aTerraform, boolean aCutTrees, boolean aMine, boolean aFarm, boolean aBuild, boolean aHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayLockFences, boolean aMayPassFences, boolean aIsDiplomat, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, byte aStatus, int appliedToVillage, boolean aMayPushPullTurn, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long appliedToPlayer, int aSettings, int aMoreSettings, int aExtraSettings) throws IOException {
/*   93 */     super(aVillageId, aName, aTerraform, aCutTrees, aMine, aFarm, aBuild, aHire, aMayInvite, aMayDestroy, aMayManageRoles, aMayExpand, aMayLockFences, aMayPassFences, aIsDiplomat, aMayAttackCitizens, aMayAttackNonCitizens, aMayFish, aMayCutOldTrees, aStatus, appliedToVillage, aMayPushPullTurn, aMayUpdateMap, aMayLead, aMayPickup, aMayTame, aMayLoad, aMayButcher, aMayAttachLock, aMayPickLocks, appliedToPlayer, aSettings, aMoreSettings, aExtraSettings);
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
/*      */   DbVillageRole(int aId, int aVillageId, String aRoleName, boolean aMayTerraform, boolean aMayCuttrees, boolean aMayMine, boolean aMayFarm, boolean aMayBuild, boolean aMayHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayPassAllFences, boolean aMayLockFences, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, boolean aMayPushPullTurn, boolean aDiplomat, byte aStatus, int aVillageAppliedTo, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long aPlayerAppliedTo, int aSettings, int aMoreSettings, int aExtraSettings) {
/*  115 */     super(aId, aVillageId, aRoleName, aMayTerraform, aMayCuttrees, aMayMine, aMayFarm, aMayBuild, aMayHire, aMayInvite, aMayDestroy, aMayManageRoles, aMayExpand, aMayPassAllFences, aMayLockFences, aMayAttackCitizens, aMayAttackNonCitizens, aMayFish, aMayCutOldTrees, aMayPushPullTurn, aDiplomat, aStatus, aVillageAppliedTo, aMayUpdateMap, aMayLead, aMayPickup, aMayTame, aMayLoad, aMayButcher, aMayAttachLock, aMayPickLocks, aPlayerAppliedTo, aSettings, aMoreSettings, aExtraSettings);
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
/*      */   void create() throws IOException {
/*  130 */     Connection dbcon = null;
/*  131 */     PreparedStatement ps = null;
/*  132 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  135 */       dbcon = DbConnector.getZonesDbCon();
/*  136 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGEROLE (VILLAGEID,NAME ,MAYTERRAFORM ,MAYCUTTREE ,MAYMINE ,MAYFARM ,MAYBUILD ,MAYHIRE,MAYINVITE,MAYDESTROY,MAYMANAGEROLES, MAYEXPAND,MAYLOCKFENCES, MAYPASSFENCES,DIPLOMAT, MAYATTACKCITIZ, MAYATTACKNONCITIZ,MAYFISH,MAYCUTOLD, STATUS,VILLAGEAPPLIEDTO,MAYPUSHPULLTURN,MAYUPDATEMAP,MAYLEAD,MAYPICKUP,MAYTAME,MAYLOAD,MAYBUTCHER,MAYATTACHLOCK,MAYPICKLOCKS,PLAYERAPPLIEDTO,SETTINGS,MORESETTINGS,EXTRASETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/*  137 */       ps.setInt(1, this.villageid);
/*  138 */       ps.setString(2, this.name);
/*  139 */       ps.setBoolean(3, this.mayTerraform);
/*  140 */       ps.setBoolean(4, this.mayCuttrees);
/*  141 */       ps.setBoolean(5, this.mayMine);
/*  142 */       ps.setBoolean(6, this.mayFarm);
/*  143 */       ps.setBoolean(7, this.mayBuild);
/*  144 */       ps.setBoolean(8, this.mayHire);
/*  145 */       ps.setBoolean(9, this.mayInvite);
/*  146 */       ps.setBoolean(10, this.mayDestroy);
/*  147 */       ps.setBoolean(11, this.mayManageRoles);
/*  148 */       ps.setBoolean(12, this.mayExpand);
/*  149 */       ps.setBoolean(13, this.mayLockFences);
/*  150 */       ps.setBoolean(14, this.mayPassAllFences);
/*  151 */       ps.setBoolean(15, this.diplomat);
/*  152 */       ps.setBoolean(16, this.mayAttackCitizens);
/*  153 */       ps.setBoolean(17, this.mayAttackNonCitizens);
/*  154 */       ps.setBoolean(18, this.mayFish);
/*  155 */       ps.setBoolean(19, this.mayCutOldTrees);
/*  156 */       ps.setByte(20, this.status);
/*  157 */       ps.setInt(21, this.villageAppliedTo);
/*  158 */       ps.setBoolean(22, this.mayPushPullTurn);
/*  159 */       ps.setBoolean(23, this.mayUpdateMap);
/*  160 */       ps.setBoolean(24, this.mayLead);
/*  161 */       ps.setBoolean(25, this.mayPickup);
/*  162 */       ps.setBoolean(26, this.mayTame);
/*  163 */       ps.setBoolean(27, this.mayLoad);
/*  164 */       ps.setBoolean(28, this.mayButcher);
/*  165 */       ps.setBoolean(29, this.mayAttachLock);
/*  166 */       ps.setBoolean(30, this.mayPickLocks);
/*  167 */       ps.setLong(31, this.playerAppliedTo);
/*  168 */       ps.setInt(32, this.settings.getPermissions());
/*  169 */       ps.setInt(33, this.moreSettings.getPermissions());
/*  170 */       ps.setInt(34, this.extraSettings.getPermissions());
/*  171 */       ps.executeUpdate();
/*  172 */       rs = ps.getGeneratedKeys();
/*  173 */       if (rs.next()) {
/*  174 */         this.id = rs.getInt(1);
/*      */       }
/*  176 */     } catch (SQLException sqx) {
/*      */       
/*  178 */       logger.log(Level.WARNING, "Failed to set status for citizen " + this.name + ": " + sqx.getMessage(), sqx);
/*  179 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  183 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  184 */       DbConnector.returnConnection(dbcon);
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
/*      */   public void save() throws IOException {
/*  196 */     Connection dbcon = null;
/*  197 */     PreparedStatement ps = null;
/*  198 */     ResultSet rs = null;
/*      */     
/*  200 */     if (this.status == 1 && this.mayDestroy) {
/*      */       
/*  202 */       this.mayDestroy = false;
/*  203 */       logger.warning("Saving roleID " + this.id + ": mayDestroy set for ROLE_EVERYBODY");
/*  204 */       Thread.dumpStack();
/*      */     } 
/*      */     
/*      */     try {
/*  208 */       dbcon = DbConnector.getZonesDbCon();
/*  209 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET VILLAGEID=?,NAME=?,MAYTERRAFORM=?,MAYCUTTREE=?,MAYMINE=?,MAYFARM=?,MAYBUILD=?,MAYHIRE=?,MAYINVITE=?,MAYDESTROY=?,MAYMANAGEROLES=?,MAYEXPAND=?,MAYLOCKFENCES=?,MAYPASSFENCES=?,DIPLOMAT=?,MAYATTACKCITIZ=?,MAYATTACKNONCITIZ=?,MAYFISH=?,MAYCUTOLD=?,STATUS=?,VILLAGEAPPLIEDTO=?,MAYPUSHPULLTURN=?,MAYUPDATEMAP=?,MAYLEAD=?,MAYPICKUP=?,MAYTAME=?,MAYLOAD=?,MAYBUTCHER=?,MAYATTACHLOCK=?,MAYPICKLOCKS=?,PLAYERAPPLIEDTO=?,SETTINGS=?,MORESETTINGS=?,EXTRASETTINGS=? WHERE ID=?");
/*  210 */       ps.setInt(1, this.villageid);
/*  211 */       ps.setString(2, this.name);
/*  212 */       ps.setBoolean(3, this.mayTerraform);
/*  213 */       ps.setBoolean(4, this.mayCuttrees);
/*  214 */       ps.setBoolean(5, this.mayMine);
/*  215 */       ps.setBoolean(6, this.mayFarm);
/*  216 */       ps.setBoolean(7, this.mayBuild);
/*  217 */       ps.setBoolean(8, this.mayHire);
/*  218 */       ps.setBoolean(9, this.mayInvite);
/*  219 */       ps.setBoolean(10, this.mayDestroy);
/*  220 */       ps.setBoolean(11, this.mayManageRoles);
/*  221 */       ps.setBoolean(12, this.mayExpand);
/*  222 */       ps.setBoolean(13, this.mayLockFences);
/*  223 */       ps.setBoolean(14, this.mayPassAllFences);
/*  224 */       ps.setBoolean(15, this.diplomat);
/*  225 */       ps.setBoolean(16, this.mayAttackCitizens);
/*  226 */       ps.setBoolean(17, this.mayAttackNonCitizens);
/*  227 */       ps.setBoolean(18, this.mayFish);
/*  228 */       ps.setBoolean(19, this.mayCutOldTrees);
/*  229 */       ps.setByte(20, this.status);
/*  230 */       ps.setInt(21, this.villageAppliedTo);
/*  231 */       ps.setBoolean(22, this.mayPushPullTurn);
/*  232 */       ps.setBoolean(23, this.mayUpdateMap);
/*  233 */       ps.setBoolean(24, this.mayLead);
/*  234 */       ps.setBoolean(25, this.mayPickup);
/*  235 */       ps.setBoolean(26, this.mayTame);
/*  236 */       ps.setBoolean(27, this.mayLoad);
/*  237 */       ps.setBoolean(28, this.mayButcher);
/*  238 */       ps.setBoolean(29, this.mayAttachLock);
/*  239 */       ps.setBoolean(30, this.mayPickLocks);
/*  240 */       ps.setLong(31, this.playerAppliedTo);
/*  241 */       ps.setInt(32, this.settings.getPermissions());
/*  242 */       ps.setInt(33, this.moreSettings.getPermissions());
/*  243 */       ps.setInt(34, this.extraSettings.getPermissions());
/*  244 */       ps.setInt(35, this.id);
/*  245 */       ps.executeUpdate();
/*      */     }
/*  247 */     catch (SQLException sqx) {
/*      */       
/*  249 */       logger.log(Level.WARNING, "Failed to save role " + this.name + ": " + sqx.getMessage(), sqx);
/*  250 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  254 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  255 */       DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayHire(boolean hire) throws IOException {
/*  267 */     if (this.mayHire != hire) {
/*      */       
/*  269 */       this.mayHire = hire;
/*  270 */       Connection dbcon = null;
/*  271 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  274 */         dbcon = DbConnector.getZonesDbCon();
/*  275 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYHIRE=? WHERE ID=?");
/*  276 */         ps.setBoolean(1, this.mayHire);
/*  277 */         ps.setInt(2, this.id);
/*  278 */         ps.executeUpdate();
/*      */       }
/*  280 */       catch (SQLException sqx) {
/*      */         
/*  282 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  283 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  287 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  288 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setName(String aName) throws IOException {
/*  301 */     if (!this.name.equals(aName)) {
/*      */       
/*  303 */       this.name = aName;
/*  304 */       Connection dbcon = null;
/*  305 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  308 */         dbcon = DbConnector.getZonesDbCon();
/*  309 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET NAME=? WHERE ID=?");
/*  310 */         ps.setString(1, aName);
/*  311 */         ps.setInt(2, this.id);
/*  312 */         ps.executeUpdate();
/*      */       }
/*  314 */       catch (SQLException sqx) {
/*      */         
/*  316 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  317 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  321 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  322 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayBuild(boolean build) throws IOException {
/*  335 */     if (this.mayBuild != build) {
/*      */       
/*  337 */       this.mayBuild = build;
/*  338 */       Connection dbcon = null;
/*  339 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  342 */         dbcon = DbConnector.getZonesDbCon();
/*  343 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYBUILD=? WHERE ID=?");
/*  344 */         ps.setBoolean(1, this.mayBuild);
/*  345 */         ps.setInt(2, this.id);
/*  346 */         ps.executeUpdate();
/*      */       }
/*  348 */       catch (SQLException sqx) {
/*      */         
/*  350 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  351 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  355 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  356 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayCuttrees(boolean aCutTrees) throws IOException {
/*  369 */     if (this.mayCuttrees != aCutTrees) {
/*      */       
/*  371 */       this.mayCuttrees = aCutTrees;
/*  372 */       Connection dbcon = null;
/*  373 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  376 */         dbcon = DbConnector.getZonesDbCon();
/*  377 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYCUTTREE=? WHERE ID=?");
/*  378 */         ps.setBoolean(1, this.mayCuttrees);
/*  379 */         ps.setInt(2, this.id);
/*  380 */         ps.executeUpdate();
/*      */       }
/*  382 */       catch (SQLException sqx) {
/*      */         
/*  384 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  385 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  389 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  390 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayMine(boolean mine) throws IOException {
/*  403 */     if (this.mayMine != mine) {
/*      */       
/*  405 */       this.mayMine = mine;
/*  406 */       Connection dbcon = null;
/*  407 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  410 */         dbcon = DbConnector.getZonesDbCon();
/*  411 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYMINE=? WHERE ID=?");
/*  412 */         ps.setBoolean(1, this.mayMine);
/*  413 */         ps.setInt(2, this.id);
/*  414 */         ps.executeUpdate();
/*      */       }
/*  416 */       catch (SQLException sqx) {
/*      */         
/*  418 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  419 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  423 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  424 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayFarm(boolean farm) throws IOException {
/*  437 */     if (this.mayFarm != farm) {
/*      */       
/*  439 */       this.mayFarm = farm;
/*  440 */       Connection dbcon = null;
/*  441 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  444 */         dbcon = DbConnector.getZonesDbCon();
/*  445 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYFARM=? WHERE ID=?");
/*  446 */         ps.setBoolean(1, this.mayFarm);
/*  447 */         ps.setInt(2, this.id);
/*  448 */         ps.executeUpdate();
/*      */       }
/*  450 */       catch (SQLException sqx) {
/*      */         
/*  452 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  453 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  457 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  458 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayManageRoles(boolean mayManage) throws IOException {
/*  471 */     if (this.mayManageRoles != mayManage) {
/*      */       
/*  473 */       this.mayManageRoles = mayManage;
/*  474 */       Connection dbcon = null;
/*  475 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  478 */         dbcon = DbConnector.getZonesDbCon();
/*  479 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYMANAGEROLES=? WHERE ID=?");
/*  480 */         ps.setBoolean(1, this.mayManageRoles);
/*  481 */         ps.setInt(2, this.id);
/*  482 */         ps.executeUpdate();
/*      */       }
/*  484 */       catch (SQLException sqx) {
/*      */         
/*  486 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  487 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  491 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  492 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayDestroy(boolean destroy) throws IOException {
/*  505 */     if (this.status == 1 && destroy) {
/*  506 */       logger.warning("Attempting to set MayDestroy on RoleID " + this.id);
/*  507 */       Thread.dumpStack();
/*      */       return;
/*      */     } 
/*  510 */     if (this.mayDestroy != destroy) {
/*      */       
/*  512 */       this.mayDestroy = destroy;
/*  513 */       Connection dbcon = null;
/*  514 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  517 */         dbcon = DbConnector.getZonesDbCon();
/*  518 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYDESTROY=? WHERE ID=?");
/*  519 */         ps.setBoolean(1, this.mayDestroy);
/*  520 */         ps.setInt(2, this.id);
/*  521 */         ps.executeUpdate();
/*      */       }
/*  523 */       catch (SQLException sqx) {
/*      */         
/*  525 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  526 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  530 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  531 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayTerraform(boolean terraform) throws IOException {
/*  544 */     if (this.mayTerraform != terraform) {
/*      */       
/*  546 */       this.mayTerraform = terraform;
/*  547 */       Connection dbcon = null;
/*  548 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  551 */         dbcon = DbConnector.getZonesDbCon();
/*  552 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYTERRAFORM=? WHERE ID=?");
/*  553 */         ps.setBoolean(1, this.mayTerraform);
/*  554 */         ps.setInt(2, this.id);
/*  555 */         ps.executeUpdate();
/*      */       }
/*  557 */       catch (SQLException sqx) {
/*      */         
/*  559 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  560 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  564 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  565 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayUpdateMap(boolean updateMap) throws IOException {
/*  573 */     if (this.mayUpdateMap != updateMap) {
/*      */       
/*  575 */       this.mayUpdateMap = updateMap;
/*  576 */       Connection dbcon = null;
/*  577 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  580 */         dbcon = DbConnector.getZonesDbCon();
/*  581 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYUPDATEMAP=? WHERE ID=?");
/*  582 */         ps.setBoolean(1, this.mayUpdateMap);
/*  583 */         ps.setInt(2, this.id);
/*  584 */         ps.executeUpdate();
/*      */       }
/*  586 */       catch (SQLException sqx) {
/*      */         
/*  588 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  589 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  593 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  594 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayInvite(boolean invite) throws IOException {
/*  607 */     if (this.mayInvite != invite) {
/*      */       
/*  609 */       this.mayInvite = invite;
/*  610 */       Connection dbcon = null;
/*  611 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  614 */         dbcon = DbConnector.getZonesDbCon();
/*  615 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYINVITE=? WHERE ID=?");
/*  616 */         ps.setBoolean(1, this.mayInvite);
/*  617 */         ps.setInt(2, this.id);
/*  618 */         ps.executeUpdate();
/*      */       }
/*  620 */       catch (SQLException sqx) {
/*      */         
/*  622 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  623 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  627 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  628 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayExpand(boolean expand) throws IOException {
/*  641 */     if (this.mayExpand != expand) {
/*      */       
/*  643 */       this.mayExpand = expand;
/*  644 */       Connection dbcon = null;
/*  645 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  648 */         dbcon = DbConnector.getZonesDbCon();
/*  649 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYEXPAND=? WHERE ID=?");
/*  650 */         ps.setBoolean(1, this.mayExpand);
/*  651 */         ps.setInt(2, this.id);
/*  652 */         ps.executeUpdate();
/*      */       }
/*  654 */       catch (SQLException sqx) {
/*      */         
/*  656 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  657 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  661 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  662 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayPassAllFences(boolean maypass) throws IOException {
/*  675 */     if (this.mayPassAllFences != maypass) {
/*      */       
/*  677 */       this.mayPassAllFences = maypass;
/*  678 */       Connection dbcon = null;
/*  679 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  682 */         dbcon = DbConnector.getZonesDbCon();
/*  683 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYPASSFENCES=? WHERE ID=?");
/*  684 */         ps.setBoolean(1, this.mayPassAllFences);
/*  685 */         ps.setInt(2, this.id);
/*  686 */         ps.executeUpdate();
/*      */       }
/*  688 */       catch (SQLException sqx) {
/*      */         
/*  690 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  691 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  695 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  696 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayLockFences(boolean maylock) throws IOException {
/*  709 */     if (this.mayLockFences != maylock) {
/*      */       
/*  711 */       this.mayLockFences = maylock;
/*  712 */       Connection dbcon = null;
/*  713 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  716 */         dbcon = DbConnector.getZonesDbCon();
/*  717 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYLOCKFENCES=? WHERE ID=?");
/*  718 */         ps.setBoolean(1, this.mayLockFences);
/*  719 */         ps.setInt(2, this.id);
/*  720 */         ps.executeUpdate();
/*      */       }
/*  722 */       catch (SQLException sqx) {
/*      */         
/*  724 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  725 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  729 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  730 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setVillageAppliedTo(int newVillage) throws IOException {
/*  743 */     if (this.villageAppliedTo != newVillage) {
/*      */       
/*  745 */       this.villageAppliedTo = newVillage;
/*  746 */       Connection dbcon = null;
/*  747 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  750 */         dbcon = DbConnector.getZonesDbCon();
/*  751 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET VILLAGEAPPLIEDTO=? WHERE ID=?");
/*  752 */         ps.setInt(1, this.villageAppliedTo);
/*  753 */         ps.setInt(2, this.id);
/*  754 */         ps.executeUpdate();
/*      */       }
/*  756 */       catch (SQLException sqx) {
/*      */         
/*  758 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  759 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  763 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  764 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setDiplomat(boolean isDiplomat) throws IOException {
/*  777 */     if (this.diplomat != isDiplomat) {
/*      */       
/*  779 */       this.diplomat = isDiplomat;
/*  780 */       Connection dbcon = null;
/*  781 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  784 */         dbcon = DbConnector.getZonesDbCon();
/*  785 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET DIPLOMAT=? WHERE ID=?");
/*  786 */         ps.setBoolean(1, this.diplomat);
/*  787 */         ps.setInt(2, this.id);
/*  788 */         ps.executeUpdate();
/*      */       }
/*  790 */       catch (SQLException sqx) {
/*      */         
/*  792 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  793 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  797 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  798 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayAttackCitizens(boolean attack) throws IOException {
/*  811 */     if (this.mayAttackCitizens != attack) {
/*      */       
/*  813 */       this.mayAttackCitizens = attack;
/*  814 */       Connection dbcon = null;
/*  815 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  818 */         dbcon = DbConnector.getZonesDbCon();
/*  819 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYATTACKCITIZ=? WHERE ID=?");
/*  820 */         ps.setBoolean(1, this.mayAttackCitizens);
/*  821 */         ps.setInt(2, this.id);
/*  822 */         ps.executeUpdate();
/*      */       }
/*  824 */       catch (SQLException sqx) {
/*      */         
/*  826 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  827 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  831 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  832 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayAttackNonCitizens(boolean attack) throws IOException {
/*  845 */     if (this.mayAttackNonCitizens != attack) {
/*      */       
/*  847 */       this.mayAttackNonCitizens = attack;
/*  848 */       Connection dbcon = null;
/*  849 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  852 */         dbcon = DbConnector.getZonesDbCon();
/*  853 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYATTACKNONCITIZ=? WHERE ID=?");
/*  854 */         ps.setBoolean(1, this.mayAttackNonCitizens);
/*  855 */         ps.setInt(2, this.id);
/*  856 */         ps.executeUpdate();
/*      */       }
/*  858 */       catch (SQLException sqx) {
/*      */         
/*  860 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  861 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  865 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  866 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayPushPullTurn(boolean pushpullturn) throws IOException {
/*  874 */     if (this.mayPushPullTurn == pushpullturn) {
/*      */       return;
/*      */     }
/*  877 */     this.mayPushPullTurn = pushpullturn;
/*  878 */     Connection dbcon = null;
/*  879 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  882 */       dbcon = DbConnector.getZonesDbCon();
/*  883 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYPUSHPULLTURN=? WHERE ID=?");
/*  884 */       ps.setBoolean(1, pushpullturn);
/*  885 */       ps.setInt(2, this.id);
/*  886 */       ps.executeUpdate();
/*      */     }
/*  888 */     catch (SQLException sqx) {
/*      */       
/*  890 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  891 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  895 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  896 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayLead(boolean lead) throws IOException {
/*  904 */     if (this.mayLead == lead) {
/*      */       return;
/*      */     }
/*  907 */     this.mayLead = lead;
/*  908 */     Connection dbcon = null;
/*  909 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  912 */       dbcon = DbConnector.getZonesDbCon();
/*  913 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYLEAD=? WHERE ID=?");
/*  914 */       ps.setBoolean(1, lead);
/*  915 */       ps.setInt(2, this.id);
/*  916 */       ps.executeUpdate();
/*      */     }
/*  918 */     catch (SQLException sqx) {
/*      */       
/*  920 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  921 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  925 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  926 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayPickup(boolean pickup) throws IOException {
/*  933 */     if (this.mayPickup == pickup) {
/*      */       return;
/*      */     }
/*  936 */     this.mayPickup = pickup;
/*  937 */     Connection dbcon = null;
/*  938 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  941 */       dbcon = DbConnector.getZonesDbCon();
/*  942 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYPICKUP=? WHERE ID=?");
/*  943 */       ps.setBoolean(1, pickup);
/*  944 */       ps.setInt(2, this.id);
/*  945 */       ps.executeUpdate();
/*      */     }
/*  947 */     catch (SQLException sqx) {
/*      */       
/*  949 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  950 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  954 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  955 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayTame(boolean tame) throws IOException {
/*  962 */     if (this.mayTame == tame) {
/*      */       return;
/*      */     }
/*  965 */     this.mayTame = tame;
/*  966 */     Connection dbcon = null;
/*  967 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  970 */       dbcon = DbConnector.getZonesDbCon();
/*  971 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYTAME=? WHERE ID=?");
/*  972 */       ps.setBoolean(1, tame);
/*  973 */       ps.setInt(2, this.id);
/*  974 */       ps.executeUpdate();
/*      */     }
/*  976 */     catch (SQLException sqx) {
/*      */       
/*  978 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/*  979 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  983 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  984 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayLoad(boolean load) throws IOException {
/*  991 */     if (this.mayLoad == load) {
/*      */       return;
/*      */     }
/*  994 */     this.mayLoad = load;
/*  995 */     Connection dbcon = null;
/*  996 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  999 */       dbcon = DbConnector.getZonesDbCon();
/* 1000 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYLOAD=? WHERE ID=?");
/* 1001 */       ps.setBoolean(1, load);
/* 1002 */       ps.setInt(2, this.id);
/* 1003 */       ps.executeUpdate();
/*      */     }
/* 1005 */     catch (SQLException sqx) {
/*      */       
/* 1007 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/* 1008 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1012 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1013 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayButcher(boolean butcher) throws IOException {
/* 1020 */     if (this.mayButcher == butcher) {
/*      */       return;
/*      */     }
/* 1023 */     this.mayButcher = butcher;
/* 1024 */     Connection dbcon = null;
/* 1025 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1028 */       dbcon = DbConnector.getZonesDbCon();
/* 1029 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYBUTCHER=? WHERE ID=?");
/* 1030 */       ps.setBoolean(1, butcher);
/* 1031 */       ps.setInt(2, this.id);
/* 1032 */       ps.executeUpdate();
/*      */     }
/* 1034 */     catch (SQLException sqx) {
/*      */       
/* 1036 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/* 1037 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1041 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1042 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayAttachLock(boolean attachLock) throws IOException {
/* 1049 */     if (this.mayAttachLock == attachLock) {
/*      */       return;
/*      */     }
/* 1052 */     this.mayAttachLock = attachLock;
/* 1053 */     Connection dbcon = null;
/* 1054 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1057 */       dbcon = DbConnector.getZonesDbCon();
/* 1058 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYATTACHLOCK=? WHERE ID=?");
/* 1059 */       ps.setBoolean(1, attachLock);
/* 1060 */       ps.setInt(2, this.id);
/* 1061 */       ps.executeUpdate();
/*      */     }
/* 1063 */     catch (SQLException sqx) {
/*      */       
/* 1065 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/* 1066 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1070 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1071 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayPickLocks(boolean pickLocks) throws IOException {
/* 1078 */     if (this.mayPickLocks == pickLocks) {
/*      */       return;
/*      */     }
/* 1081 */     this.mayPickLocks = pickLocks;
/* 1082 */     Connection dbcon = null;
/* 1083 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1086 */       dbcon = DbConnector.getZonesDbCon();
/* 1087 */       ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYPICKLOCKS=? WHERE ID=?");
/* 1088 */       ps.setBoolean(1, pickLocks);
/* 1089 */       ps.setInt(2, this.id);
/* 1090 */       ps.executeUpdate();
/*      */     }
/* 1092 */     catch (SQLException sqx) {
/*      */       
/* 1094 */       logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/* 1095 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1099 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1100 */       DbConnector.returnConnection(dbcon);
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
/*      */   public void setMayFish(boolean fish) throws IOException {
/* 1112 */     if (this.mayFish != fish) {
/*      */       
/* 1114 */       this.mayFish = fish;
/* 1115 */       Connection dbcon = null;
/* 1116 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1119 */         dbcon = DbConnector.getZonesDbCon();
/* 1120 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYFISH=? WHERE ID=?");
/* 1121 */         ps.setBoolean(1, this.mayFish);
/* 1122 */         ps.setInt(2, this.id);
/* 1123 */         ps.executeUpdate();
/*      */       }
/* 1125 */       catch (SQLException sqx) {
/*      */         
/* 1127 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/* 1128 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1132 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1133 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void setCutOld(boolean cutold) throws IOException {
/* 1146 */     if (this.mayCutOldTrees != cutold) {
/*      */       
/* 1148 */       this.mayCutOldTrees = cutold;
/* 1149 */       Connection dbcon = null;
/* 1150 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1153 */         dbcon = DbConnector.getZonesDbCon();
/* 1154 */         ps = dbcon.prepareStatement("UPDATE VILLAGEROLE SET MAYCUTOLD=? WHERE ID=?");
/* 1155 */         ps.setBoolean(1, this.mayCutOldTrees);
/* 1156 */         ps.setInt(2, this.id);
/* 1157 */         ps.executeUpdate();
/*      */       }
/* 1159 */       catch (SQLException sqx) {
/*      */         
/* 1161 */         logger.log(Level.WARNING, "Failed to set data for role with id " + this.id, sqx);
/* 1162 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1166 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1167 */         DbConnector.returnConnection(dbcon);
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
/*      */   public void delete() throws IOException {
/* 1180 */     Connection dbcon = null;
/* 1181 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1184 */       dbcon = DbConnector.getZonesDbCon();
/* 1185 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGEROLE WHERE ID=?");
/* 1186 */       ps.setInt(1, this.id);
/* 1187 */       ps.executeUpdate();
/*      */     }
/* 1189 */     catch (SQLException sqx) {
/*      */       
/* 1191 */       logger.log(Level.WARNING, "Failed to delete role with id " + this.id, sqx);
/* 1192 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1196 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1197 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(DbVillageRole otherDbVillageRole) {
/* 1208 */     return getName().compareTo(otherDbVillageRole.getName());
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DbVillageRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class Features
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(Features.class.getName());
/*     */ 
/*     */   
/*  42 */   private static int currentProdVersion = 129;
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
/*     */   public static void loadAllFeatures() {
/*  56 */     Feature.dbReadOverriddenFeatures();
/*     */     
/*  58 */     logFeatureDetails();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum State
/*     */   {
/*  68 */     FUTURE,
/*  69 */     INDEV,
/*  70 */     COMPLETE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Wurm
/*     */   {
/*  80 */     NONE,
/*  81 */     WO,
/*  82 */     STEAM,
/*  83 */     BOTH,
/*  84 */     TEST;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Feature
/*     */   {
/*  96 */     NONE(0, "Dummy entry - do not use", 1, (String)Features.State.COMPLETE, Features.Wurm.NONE),
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
/* 127 */     NAMECHANGE(31, "Name change", 999, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 128 */     SURFACEWATER(32, "Surface Water", 140, (String)Features.State.INDEV, Features.Wurm.BOTH),
/* 129 */     CAVEWATER(33, "Cave Water", 999, (String)Features.State.FUTURE, Features.Wurm.BOTH),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     NEW_SKILL_SYSTEM(38, "New skill system", 125, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/*     */ 
/*     */     
/* 138 */     BLOCKED_TRADERS(41, "Blocked Traders", 121, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     CREATURE_COMBAT_CHANGES(51, "Combat system changes for creatures", 201, (String)Features.State.INDEV, Features.Wurm.BOTH),
/*     */ 
/*     */ 
/*     */     
/* 152 */     BLOCK_HOTA(55, "Blocked HOTA terraforming and building", 201, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 153 */     FREE_ITEMS(56, "Free armour and weapons on spawn", 201, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 154 */     TREASURE_CHESTS(57, "Random treasure chests", 201, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
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
/* 167 */     OWNERSHIP_PAPERS(70, "Ownership Papers", 999, (String)Features.State.FUTURE, Features.Wurm.BOTH),
/*     */     
/* 169 */     VALREI_MAP(72, "Valrei map", 125, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/*     */ 
/*     */     
/* 172 */     CROP_POLLER(75, "Crop tile poller split", 125, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 173 */     SINGLE_PLAYER_BRIDGES(76, "Single Player Bridges", 125, (String)Features.State.COMPLETE, Features.Wurm.STEAM),
/* 174 */     AMPHORA(77, "Amphora", 126, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 175 */     CHAOS(78, "Set as a chaos server (test pvp only)", 999, (String)Features.State.COMPLETE, Features.Wurm.TEST),
/* 176 */     BOAT_DESTINATION(79, "Set a destination on a boat", 126, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 177 */     NEW_PORTALS(80, "New portals", 999, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 178 */     TRANSFORM_RESOURCE_TILES(81, "Transform from resource tiles", 126, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 179 */     WAGON_PASSENGER(82, "Wagon Passenger", 126, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/*     */ 
/*     */     
/* 182 */     CAVE_DWELLINGS(85, "Cave Dwellings", 128, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 183 */     ITEMS_ON_FURNITURE(86, "Placing items on furniture", 999, (String)Features.State.FUTURE, Features.Wurm.BOTH),
/* 184 */     RIFTS(87, "Rifts", 128, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 185 */     TRANSFORM_TO_RESOURCE_TILES(88, "Transform to resource tiles", 126, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/*     */     
/* 187 */     CAVE_BRIDGES(89, "Cave Bridges", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/*     */     
/* 189 */     GIFT_PACKS(90, "Gift packs", 128, (String)Features.State.COMPLETE, Features.Wurm.WO),
/*     */     
/* 191 */     RETURNER_PACK_REGISTRATION(91, "Returner pack registration", 127, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 192 */     RIFTLOOTCHANCE(92, "Rift Loot Based on Participation", 140, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 193 */     EXTRAGIFT(93, "Extra Anniversary Gift", 128, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 194 */     NEWDOMAINS(94, "New Domain System - Override requires restart", 128, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 195 */     ALLOW_MEDPATHCHANGE(95, "Allow Meditation Path Change (Insanity Only)", 130, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 196 */     HIGHWAYS(96, "New Highway System - Override requires restart", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 197 */     NEW_PROJECTILES(97, "New Projectile Calculations", 128, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 198 */     NEW_KINGDOM_INF(98, "New Kingdom Influence", 140, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 199 */     WAGONER(99, "Wagoner System", 129, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 200 */     CREATURE_MOVEMENT_CHANGES(100, "Creature Movement Changes", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 201 */     POLLING_CHANGES(101, "Polling Optimisation - Tile Array Copying Changes", 999, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 202 */     DRIVE_ON_LEFT(102, "Wagoner Drive On Left", 129, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 203 */     TRANSPORTABLE_CREATURES(103, "Allows for transportation of creatures", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 204 */     MOVE_BULK_TO_BULK(104, "Move from one bulk container to another as action", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 205 */     DRIVE_SIDES(105, "Wagoner Drive on One Side", 129, (String)Features.State.COMPLETE, Features.Wurm.WO),
/* 206 */     AFFINITY_GAINS(106, "Chance to gain affinities from skill usage", 140, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 207 */     METALLIC_ITEMS(107, "All metals make all metal items", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 208 */     COMPOUND_TITLES(108, "Compound Titles", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 209 */     PVE_DEATHTABS(109, "PvE Server Death Tabs", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 210 */     NEW_ARMOUR_VALUES(110, "New Armour Values (Epic Tested)", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 211 */     TOWER_CHAINING(111, "Tower Chaining", 140, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 212 */     CHICKEN_COOPS(112, "Chicken Coops", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 213 */     SADDLEBAG_DECAY(113, "Decay in Saddlebags", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH),
/* 214 */     SKILLSTAT_DISABLE(114, "Disable SkillStat saving", 129, (String)Features.State.COMPLETE, Features.Wurm.BOTH);
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
/*     */     private static final String GET_ALL_OVERRIDDEN_FEATURES = "SELECT * FROM OVERRIDDENFEATURES";
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
/*     */     private static final String INSERT_OVERRIDDEN_FEATURE = "INSERT INTO OVERRIDDENFEATURES(FEATUREID,ENABLED) VALUES(?,?)";
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
/*     */     private static final String DELETE_OVERRIDDEN_FEATURE = "DELETE FROM OVERRIDDENFEATURES WHERE FEATUREID=?";
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
/*     */     private static final String UPDATE_OVERRIDDEN_FEATURE = "UPDATE OVERRIDDENFEATURES SET ENABLED=? WHERE FEATUREID=?";
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
/*     */     private final int featureId;
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
/*     */     private final String name;
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
/*     */     private final int version;
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
/*     */     private final boolean theDefault;
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
/*     */     private boolean overridden;
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
/*     */     private boolean enabled;
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
/*     */     private Features.State state;
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
/*     */     private Features.Wurm wurm;
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
/* 582 */     private static final Feature[] types = values(); Feature(int aFeatureId, String aName, int aVersion, Features.State aState, Features.Wurm aWurm) { this.featureId = aFeatureId; this.name = aName; this.version = aVersion; this.state = aState; this.wurm = aWurm; this.theDefault = workOutDefault(); this.enabled = (this.wurm != Features.Wurm.NONE && this.theDefault); if (Servers.localServer.isChallengeServer()) if (this.featureId == 55 || this.featureId == 56 || this.featureId == 57) this.enabled = true;   if (Servers.localServer.isChallengeOrEpicServer() && !Server.getInstance().isPS()) if (this.featureId == 79) this.enabled = false;   if (Servers.localServer.id == 15 && this.featureId == 41) this.enabled = false;  if (Servers.localServer.id == 3 && (this.featureId == 81 || this.featureId == 88)) this.enabled = false;  if (Servers.localServer.PVPSERVER && (this.featureId == 96 || this.featureId == 99)) this.enabled = false;  this.overridden = false; } private boolean workOutDefault() { if (Servers.isThisATestServer()) return true;  if (this.state != Features.State.COMPLETE) return false;  if (this.version > Features.currentProdVersion) return false;  if (this.wurm == Features.Wurm.BOTH) return true;  if (this.wurm == Features.Wurm.STEAM && Server.getInstance().isPS()) return true;  if (this.wurm == Features.Wurm.WO && !Server.getInstance().isPS()) return true;  return false; } public int getVersion() { return this.version; } public boolean getDefault() { return this.theDefault; } public int getFeatureId() { return this.featureId; }
/*     */     public String getName() { return this.name; }
/*     */     public boolean isEnabled() { return this.enabled; }
/*     */     public boolean isOverridden() { return this.overridden; }
/*     */     public Features.State getState() { return this.state; }
/*     */     public Features.Wurm getWurm() { return this.wurm; }
/* 588 */     private static Feature featureFromInt(int featureAsInt) { for (int i = 0; i < types.length; i++) {
/*     */         
/* 590 */         if (featureAsInt == types[i].getFeatureId()) {
/* 591 */           return types[i];
/*     */         }
/*     */       } 
/* 594 */       return NONE; }
/*     */     public boolean isShown() { if (getFeatureId() == 0) return false;  if (isEnabled()) return true;  if (this.wurm == Features.Wurm.NONE) return false;  if (Servers.isThisATestServer()) return true;  if (this.state != Features.State.COMPLETE) return false;  if (this.wurm == Features.Wurm.BOTH) return true;  if (Server.getInstance().isPS()) { if (this.wurm != Features.Wurm.STEAM)
/*     */           return false;  } else if (this.wurm != Features.Wurm.WO) { return false; }  return true; }
/*     */     public boolean isAvailable() { return (this.state == Features.State.COMPLETE || Servers.isThisATestServer()); }
/*     */     private void dbDeleteOverridden() { Connection dbcon = null; PreparedStatement ps = null; try { Features.logger.log(Level.INFO, "Removing override for feature: " + this.featureId); dbcon = DbConnector.getLoginDbCon(); ps = dbcon.prepareStatement("DELETE FROM OVERRIDDENFEATURES WHERE FEATUREID=?"); ps.setInt(1, this.featureId); ps.executeUpdate(); } catch (SQLException sqex) { Features.logger.log(Level.WARNING, "Failed to delete overridden feature " + this.featureId + " from logindb!" + sqex.getMessage(), sqex); } finally { DbUtilities.closeDatabaseObjects(ps, null); DbConnector.returnConnection(dbcon); }  }
/*     */     public void dbAddOverridden(boolean aEnabled) { Connection dbcon = null; PreparedStatement ps = null; try { dbcon = DbConnector.getLoginDbCon(); Features.logger.log(Level.INFO, "Adding new override for feature: " + this.featureId); ps = dbcon.prepareStatement("INSERT INTO OVERRIDDENFEATURES(FEATUREID,ENABLED) VALUES(?,?)"); ps.setInt(1, this.featureId); ps.setBoolean(2, aEnabled); ps.executeUpdate(); } catch (SQLException sqex) { Features.logger.log(Level.WARNING, "Failed to insert overridden feature " + this.featureId + " in logindb!" + sqex.getMessage(), sqex); } finally { DbUtilities.closeDatabaseObjects(ps, null); DbConnector.returnConnection(dbcon); }  }
/*     */     public void dbUpdateOverridden(boolean aEnabled) { Connection dbcon = null; PreparedStatement ps = null; try { dbcon = DbConnector.getLoginDbCon(); Features.logger.log(Level.INFO, "Updating override for feature: " + this.featureId); ps = dbcon.prepareStatement("UPDATE OVERRIDDENFEATURES SET ENABLED=? WHERE FEATUREID=?"); ps.setBoolean(1, aEnabled); ps.setInt(2, this.featureId); ps.executeUpdate(); } catch (SQLException sqex) { Features.logger.log(Level.WARNING, "Failed to insert overridden feature " + this.featureId + " in logindb!" + sqex.getMessage(), sqex); } finally { DbUtilities.closeDatabaseObjects(ps, null); DbConnector.returnConnection(dbcon); }  }
/*     */     private static void dbReadOverriddenFeatures() { Connection dbcon = null; PreparedStatement ps = null; ResultSet rs = null; int count = 0; try { Features.logger.log(Level.INFO, "Loading all overridden features for production version: " + Features.currentProdVersion + " and isTestServer: " + Servers.isThisATestServer() + '.'); dbcon = DbConnector.getLoginDbCon(); ps = dbcon.prepareStatement("SELECT * FROM OVERRIDDENFEATURES"); rs = ps.executeQuery(); while (rs.next()) { count++; int featureid = rs.getInt("FEATUREID"); boolean enabled = rs.getBoolean("ENABLED"); setOverridden(featureid, true, enabled); if (Features.logger.isLoggable(Level.FINE))
/*     */             Features.logger.fine("Loaded overridden feature " + featureid);  }  } catch (SQLException sqex) { Features.logger.log(Level.WARNING, "Failed to load all overridden features!" + sqex.getMessage(), sqex); } finally { DbUtilities.closeDatabaseObjects(ps, rs); DbConnector.returnConnection(dbcon); Features.logger.info("Loaded " + count + " overridden features from the database"); }  }
/*     */     private static void setOverridden(int featureId, boolean aOverridden, boolean aEnabled) { Feature feature = featureFromInt(featureId); feature.overridden = aOverridden; feature.enabled = aEnabled; }
/*     */     public static void setOverridden(int aServerId, int featureId, boolean aOverridden, boolean aEnabled, boolean global) { if (global)
/* 605 */         if (Servers.isThisLoginServer()) { for (ServerEntry server : Servers.getAllServers()) { if (server.id != Servers.loginServer.id && server.id != aServerId) { LoginServerWebConnection lsw = new LoginServerWebConnection(server.id); lsw.manageFeature(aServerId, featureId, aOverridden, aEnabled, false); }  }  } else { LoginServerWebConnection lsw = new LoginServerWebConnection(Servers.loginServer.id); lsw.manageFeature(aServerId, featureId, aOverridden, aEnabled, true); }   Feature feature = featureFromInt(featureId); if (feature.overridden && !aOverridden) { feature.dbDeleteOverridden(); feature.overridden = aOverridden; feature.enabled = feature.theDefault; } else if (!feature.overridden && aOverridden) { feature.dbAddOverridden(aEnabled); feature.overridden = aOverridden; feature.enabled = aEnabled; } else if (feature.overridden && feature.enabled != aEnabled) { feature.dbUpdateOverridden(aEnabled); feature.enabled = aEnabled; } else if (!global) { return; }  } public String toString() { StringBuilder lBuilder = new StringBuilder();
/* 606 */       lBuilder.append("Feature [");
/* 607 */       lBuilder.append("Name: ").append(this.name);
/* 608 */       lBuilder.append(", Id: ").append(this.featureId);
/* 609 */       lBuilder.append(", Version: ").append(this.version);
/* 610 */       lBuilder.append(", Default: ").append(this.theDefault);
/* 611 */       lBuilder.append(", Overridden: ").append(isOverridden());
/* 612 */       lBuilder.append(", Enabled: ").append(isEnabled());
/* 613 */       lBuilder.append(']');
/* 614 */       return lBuilder.toString(); }
/*     */     public static boolean isFeatureEnabled(int aServerId, int featureId) { if (aServerId == Servers.localServer.getId())
/*     */         return isFeatureEnabled(featureId);  for (ServerEntry server : Servers.getAllServers()) {
/*     */         if (server.id == aServerId) {
/*     */           LoginServerWebConnection lsw = new LoginServerWebConnection(server.id); return lsw.isFeatureEnabled(featureId);
/*     */         } 
/*     */       }  return false; }
/*     */     public static boolean isFeatureEnabled(int aFeatureId) { Feature f = featureFromInt(aFeatureId); if (f == NONE)
/*     */         return false;  return f.isEnabled(); } static {  }
/* 623 */   } public static void logFeatureDetails() { for (Feature lFeature : Feature.values())
/*     */     {
/* 625 */       logger.info(lFeature.toString());
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getVerionsNo() {
/* 631 */     return currentProdVersion;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Features.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
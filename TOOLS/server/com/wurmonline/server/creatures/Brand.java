/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.Vehicle;
/*     */ import com.wurmonline.server.behaviours.Vehicles;
/*     */ import com.wurmonline.server.players.PermissionsHistories;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ public class Brand
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static Logger logger = Logger.getLogger(Brand.class.getName());
/*     */   
/*     */   private static final String INSERT_CREATURE_BRAND = "INSERT INTO BRANDS (OWNERID,LASTBRANDED,WURMID) VALUES (?,?,?)";
/*     */   
/*     */   private static final String DELETE_CREATURE_BRAND = "DELETE FROM BRANDS WHERE WURMID=?";
/*     */   
/*     */   private static final String UPDATE_CREATURE_BRAND = "UPDATE BRANDS SET OWNERID=?,LASTBRANDED=? WHERE WURMID=?";
/*     */   
/*     */   private static final String GET_CREATURE_BRANDS = "SELECT * FROM BRANDS";
/*     */   private final long creatureId;
/*     */   private final long timeBranded;
/*     */   private long brand;
/*     */   
/*     */   public Brand(long _creatureId, long _timeBranded, long _brand, boolean load) {
/*  58 */     this.creatureId = _creatureId;
/*  59 */     this.timeBranded = _timeBranded;
/*     */     
/*  61 */     this.brand = _brand;
/*  62 */     if (!load) {
/*     */       
/*  64 */       save(true);
/*  65 */       addInitialPermissions();
/*     */     } else {
/*     */       
/*  68 */       Creatures.getInstance().addBrand(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void setBrandId(long newId) {
/*  73 */     this.brand = newId;
/*  74 */     save(false);
/*  75 */     addInitialPermissions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void addInitialPermissions() {
/*  84 */     if (Servers.isThisAPvpServer())
/*     */       return; 
/*  86 */     AnimalSettings.remove(this.creatureId);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  91 */       Creature creature = Creatures.getInstance().getCreature(this.creatureId);
/*     */ 
/*     */ 
/*     */       
/*  95 */       int value = AnimalSettings.Animal2Permissions.MANAGE.getValue() + AnimalSettings.Animal2Permissions.COMMANDER.getValue() + AnimalSettings.Animal2Permissions.ACCESS_HOLD.getValue();
/*  96 */       Vehicle vehicle = Vehicles.getVehicle(creature);
/*     */       
/*  98 */       if (vehicle != null && !vehicle.isUnmountable() && vehicle.getMaxPassengers() != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 104 */         value += AnimalSettings.Animal2Permissions.PASSENGER.getValue();
/*     */       }
/*     */       
/* 107 */       Village village = Villages.getVillage((int)this.brand);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 115 */       AnimalSettings.addPlayer(this.creatureId, -60L, value);
/*     */ 
/*     */     
/*     */     }
/* 119 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */     
/*     */     }
/* 123 */     catch (NoSuchVillageException noSuchVillageException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void save(boolean newBrand) {
/* 131 */     Connection dbcon = null;
/* 132 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 135 */       dbcon = DbConnector.getCreatureDbCon();
/* 136 */       if (newBrand) {
/* 137 */         ps = dbcon.prepareStatement("INSERT INTO BRANDS (OWNERID,LASTBRANDED,WURMID) VALUES (?,?,?)");
/*     */       } else {
/* 139 */         ps = dbcon.prepareStatement("UPDATE BRANDS SET OWNERID=?,LASTBRANDED=? WHERE WURMID=?");
/* 140 */       }  ps.setLong(1, this.brand);
/* 141 */       ps.setLong(2, this.timeBranded);
/* 142 */       ps.setLong(3, this.creatureId);
/* 143 */       ps.executeUpdate();
/* 144 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     }
/* 146 */     catch (SQLException sqex) {
/*     */       
/* 148 */       logger.log(Level.WARNING, "Failed to save brand " + this.creatureId, sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 152 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 153 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllBrands() throws NoSuchCreatureException {
/* 159 */     logger.info("Loading Brands");
/* 160 */     Connection dbcon = null;
/* 161 */     PreparedStatement ps = null;
/* 162 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 165 */       dbcon = DbConnector.getCreatureDbCon();
/* 166 */       ps = dbcon.prepareStatement("SELECT * FROM BRANDS");
/* 167 */       rs = ps.executeQuery();
/* 168 */       while (rs.next())
/*     */       {
/* 170 */         new Brand(rs.getLong("WURMID"), rs.getLong("LASTBRANDED"), rs.getLong("OWNERID"), true);
/*     */       }
/*     */     }
/* 173 */     catch (SQLException sqx) {
/*     */       
/* 175 */       logger.log(Level.WARNING, "Failed to load brands:" + sqx.getMessage(), sqx);
/* 176 */       throw new NoSuchCreatureException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 180 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 181 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getBrandId() {
/* 187 */     return this.brand;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void deleteBrand() {
/* 192 */     if (Creatures.isLoading())
/*     */       return; 
/* 194 */     if (Creatures.getInstance().getBrand(this.creatureId) != null) {
/*     */       
/* 196 */       Connection dbcon = null;
/* 197 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 200 */         dbcon = DbConnector.getCreatureDbCon();
/* 201 */         ps = dbcon.prepareStatement("DELETE FROM BRANDS WHERE WURMID=?");
/* 202 */         ps.setLong(1, this.creatureId);
/* 203 */         ps.executeUpdate();
/* 204 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       }
/* 206 */       catch (SQLException sqex) {
/*     */         
/* 208 */         logger.log(Level.WARNING, "Failed to delete brand " + this.creatureId, sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 212 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 213 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/* 215 */       Creatures.getInstance().setBrand(this.creatureId, -1L);
/* 216 */       AnimalSettings.remove(this.creatureId);
/* 217 */       PermissionsHistories.remove(this.creatureId);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCreatureId() {
/* 228 */     return this.creatureId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getBrand() {
/* 238 */     return this.brand;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Brand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
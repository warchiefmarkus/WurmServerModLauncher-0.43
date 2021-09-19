/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class VillagePerimeter
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(VillagePerimeter.class.getName());
/*     */   
/*     */   private final int villageId;
/*     */   
/*     */   private static final String INSERT_PERIMETERVALUES = "INSERT INTO VILLAGEPERIMETERS(SETTINGS,ID) VALUES (?,?)";
/*     */   
/*     */   private static final String UPDATE_PERIMETERVALUES = "UPDATE VILLAGEPERIMETERS SET SETTINGS=? WHERE ID=?";
/*     */   
/*     */   private static final String DELETE_PERIMETERVALUES = "DELETE FROM VILLAGEPERIMETERS WHERE ID=?";
/*     */   
/*     */   private static final String INSERT_PERIMETERFRIEND = "INSERT INTO PERIMETERFRIENDS(ID,NAME) VALUES (?,?)";
/*     */   
/*     */   private static final String DELETE_PERIMETERFRIEND = "DELETE FROM PERIMETERFRIENDS WHERE NAME=? AND ID=?";
/*     */   
/*     */   private static final String DELETE_PERIMETERFRIENDVILLAGE = "DELETE FROM PERIMETERFRIENDS WHERE ID=?";
/*     */   
/*  55 */   private final Set<String> perimeterFriends = new HashSet<>();
/*  56 */   private static final Map<Integer, VillagePerimeter> parmap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long settings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private static final String[] emptyFriends = new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   VillagePerimeter(int aVillageId) {
/*  80 */     this.villageId = aVillageId;
/*     */   }
/*     */ 
/*     */   
/*     */   static VillagePerimeter getPerimeter(int villageId) {
/*  85 */     return parmap.get(Integer.valueOf(villageId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void removePerimeter(int villageId) {
/*  95 */     parmap.remove(Integer.valueOf(villageId));
/*     */   }
/*     */ 
/*     */   
/*     */   void create() throws IOException {
/* 100 */     Connection dbcon = null;
/* 101 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 104 */       dbcon = DbConnector.getZonesDbCon();
/* 105 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGEPERIMETERS(SETTINGS,ID) VALUES (?,?)");
/* 106 */       ps.setLong(1, this.settings);
/* 107 */       ps.setInt(2, this.villageId);
/* 108 */       ps.executeUpdate();
/*     */     }
/* 110 */     catch (SQLException sqx) {
/*     */       
/* 112 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 116 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 117 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void update() throws IOException {
/* 123 */     Connection dbcon = null;
/* 124 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 127 */       dbcon = DbConnector.getZonesDbCon();
/* 128 */       ps = dbcon.prepareStatement("UPDATE VILLAGEPERIMETERS SET SETTINGS=? WHERE ID=?");
/* 129 */       ps.setLong(1, this.settings);
/* 130 */       ps.setInt(2, this.villageId);
/* 131 */       ps.executeUpdate();
/*     */     }
/* 133 */     catch (SQLException sqx) {
/*     */       
/* 135 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 139 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 140 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void delete() throws IOException {
/* 146 */     Connection dbcon = null;
/* 147 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 150 */       dbcon = DbConnector.getZonesDbCon();
/* 151 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGEPERIMETERS WHERE ID=?");
/* 152 */       ps.setInt(1, this.villageId);
/* 153 */       ps.executeUpdate();
/*     */     }
/* 155 */     catch (SQLException sqx) {
/*     */       
/* 157 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 161 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 162 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 164 */     deleteAllFriend();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setAllowsFenceDestruction(boolean allow) throws IOException {
/* 203 */     boolean allowFenceDestruction = ((this.settings & 0x1L) == 1L);
/* 204 */     if (allow != allowFenceDestruction) {
/*     */       
/* 206 */       if (allow) {
/* 207 */         this.settings++;
/*     */       } else {
/* 209 */         this.settings--;
/* 210 */       }  update();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void setAllowsRoadDestruction(boolean allow) throws IOException {
/* 216 */     boolean allowRoadDestruction = ((this.settings >> 1L & 0x1L) == 1L);
/* 217 */     if (allow != allowRoadDestruction) {
/*     */       
/* 219 */       if (allow) {
/* 220 */         this.settings += 2L;
/*     */       } else {
/* 222 */         this.settings -= 2L;
/* 223 */       }  update();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void setAllowsFenceBuilding(boolean allow) throws IOException {
/* 229 */     boolean allowFenceBuilding = ((this.settings >> 2L & 0x1L) == 1L);
/* 230 */     if (allow != allowFenceBuilding) {
/*     */       
/* 232 */       if (allow) {
/* 233 */         this.settings += 4L;
/*     */       } else {
/* 235 */         this.settings -= 4L;
/* 236 */       }  update();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void setAllowsRoadBuilding(boolean allow) throws IOException {
/* 242 */     boolean allowRoadBuilding = ((this.settings >> 3L & 0x1L) == 1L);
/* 243 */     if (allow != allowRoadBuilding) {
/*     */       
/* 245 */       if (allow) {
/* 246 */         this.settings += 8L;
/*     */       } else {
/* 248 */         this.settings -= 8L;
/* 249 */       }  update();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void setAllowsBuildings(boolean allow) throws IOException {
/* 255 */     boolean allowBuildings = ((this.settings >> 4L & 0x1L) == 1L);
/* 256 */     if (allow != allowBuildings) {
/*     */       
/* 258 */       if (allow) {
/* 259 */         this.settings += 16L;
/*     */       } else {
/* 261 */         this.settings -= 16L;
/* 262 */       }  update();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void setAllowsPerimeterActionsForAllies(boolean allow) throws IOException {
/* 268 */     boolean allowPerimeterActionsForAllies = ((this.settings >> 5L & 0x1L) == 1L);
/* 269 */     if (allow != allowPerimeterActionsForAllies) {
/*     */       
/* 271 */       if (allow) {
/* 272 */         this.settings += 32L;
/*     */       } else {
/* 274 */         this.settings -= 32L;
/* 275 */       }  update();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean allowsFenceDestruction() {
/* 281 */     return ((this.settings & 0x1L) == 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean allowsRoadDestruction() {
/* 286 */     return ((this.settings >> 1L & 0x1L) == 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean allowsFenceBuilding() {
/* 291 */     return ((this.settings >> 2L & 0x1L) == 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean allowsRoadBuilding() {
/* 296 */     return ((this.settings >> 3L & 0x1L) == 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean allowsBuildings() {
/* 301 */     return ((this.settings >> 4L & 0x1L) == 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean allowsPerimeterActionsForAllies() {
/* 306 */     return ((this.settings >> 5L & 0x1L) == 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   void deleteAllFriend() throws IOException {
/* 311 */     Connection dbcon = null;
/* 312 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 315 */       dbcon = DbConnector.getZonesDbCon();
/* 316 */       ps = dbcon.prepareStatement("DELETE FROM PERIMETERFRIENDS WHERE ID=?");
/* 317 */       ps.setInt(1, this.villageId);
/* 318 */       ps.executeUpdate();
/*     */     }
/* 320 */     catch (SQLException sqx) {
/*     */       
/* 322 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 326 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 327 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean addFriend(String friendName) throws IOException {
/* 333 */     if (!this.perimeterFriends.contains(friendName)) {
/*     */       
/* 335 */       Connection dbcon = null;
/* 336 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 339 */         dbcon = DbConnector.getZonesDbCon();
/* 340 */         ps = dbcon.prepareStatement("INSERT INTO PERIMETERFRIENDS(ID,NAME) VALUES (?,?)");
/* 341 */         ps.setInt(1, this.villageId);
/* 342 */         ps.setString(2, friendName);
/*     */         
/* 344 */         ps.executeUpdate();
/* 345 */         this.perimeterFriends.add(friendName);
/* 346 */         return true;
/*     */       }
/* 348 */       catch (SQLException sqx) {
/*     */         
/* 350 */         throw new IOException(sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 354 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 355 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 358 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean deleteFriend(String friendName) throws IOException {
/* 363 */     if (this.perimeterFriends.contains(friendName)) {
/*     */       
/* 365 */       Connection dbcon = null;
/* 366 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 369 */         dbcon = DbConnector.getZonesDbCon();
/* 370 */         ps = dbcon.prepareStatement("DELETE FROM PERIMETERFRIENDS WHERE NAME=? AND ID=?");
/* 371 */         ps.setString(1, friendName);
/* 372 */         ps.setInt(2, this.villageId);
/* 373 */         ps.executeUpdate();
/* 374 */         this.perimeterFriends.remove(friendName);
/* 375 */         return true;
/*     */       }
/* 377 */       catch (SQLException sqx) {
/*     */         
/* 379 */         throw new IOException(sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 383 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 384 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 387 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isFriend(String name) {
/* 420 */     return this.perimeterFriends.contains(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getFriends() {
/* 425 */     if (this.perimeterFriends.isEmpty())
/* 426 */       return emptyFriends; 
/* 427 */     return this.perimeterFriends.<String>toArray(new String[this.perimeterFriends.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\VillagePerimeter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
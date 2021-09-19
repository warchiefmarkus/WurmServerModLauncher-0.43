/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Date;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class RecruitmentAds
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(RecruitmentAds.class.getName());
/*     */   
/*  46 */   private static final Map<Integer, Map<Integer, RecruitmentAd>> recruitmentAds = new ConcurrentHashMap<>();
/*     */   
/*     */   private static final String loadAllAds = "SELECT * FROM VILLAGERECRUITMENT";
/*     */   private static final String deleteAd = "DELETE FROM VILLAGERECRUITMENT WHERE VILLAGE=?";
/*     */   private static final String createNewAdd = "INSERT INTO VILLAGERECRUITMENT (VILLAGE, DESCRIPTION, CONTACT, CREATED, KINGDOM) VALUES ( ?, ?, ?, ?, ?);";
/*     */   private static final String updateAd = "UPDATE VILLAGERECRUITMENT SET DESCRIPTION =?, CONTACT =?, CREATED =? WHERE VILLAGE=?;";
/*     */   
/*     */   public static void add(RecruitmentAd ad) {
/*  54 */     Map<Integer, RecruitmentAd> ads = recruitmentAds.get(Integer.valueOf(ad.getKingdom()));
/*  55 */     if (ads == null) {
/*     */       
/*  57 */       ads = new ConcurrentHashMap<>();
/*  58 */       recruitmentAds.put(Integer.valueOf(ad.getKingdom()), ads);
/*     */     } 
/*  60 */     ads.put(Integer.valueOf(ad.getVillageId()), ad);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean containsAdForVillage(int villageId) {
/*  65 */     Integer key = Integer.valueOf(villageId);
/*  66 */     boolean exists = false;
/*  67 */     for (Map<Integer, RecruitmentAd> ads : recruitmentAds.values()) {
/*     */       
/*  69 */       exists = ads.containsKey(key);
/*  70 */       if (exists)
/*     */         break; 
/*     */     } 
/*  73 */     return exists;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final RecruitmentAd create(int villageId, String description, long contactId, int kingdom) throws IOException {
/*  79 */     if (containsAdForVillage(villageId))
/*  80 */       return null; 
/*  81 */     Connection dbcon = null;
/*  82 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  85 */       Date created = new Date(System.currentTimeMillis());
/*     */       
/*  87 */       dbcon = DbConnector.getZonesDbCon();
/*  88 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGERECRUITMENT (VILLAGE, DESCRIPTION, CONTACT, CREATED, KINGDOM) VALUES ( ?, ?, ?, ?, ?);");
/*  89 */       ps.setInt(1, villageId);
/*  90 */       ps.setString(2, description);
/*  91 */       ps.setLong(3, contactId);
/*  92 */       ps.setDate(4, created);
/*  93 */       ps.setInt(5, kingdom);
/*  94 */       ps.executeUpdate();
/*     */       
/*  96 */       RecruitmentAd ad = new RecruitmentAd(villageId, contactId, description, created, kingdom);
/*  97 */       add(ad);
/*  98 */       return ad;
/*     */     }
/* 100 */     catch (SQLException sqx) {
/*     */       
/* 102 */       logger.log(Level.WARNING, "Failed to create new recruitment ad for village: " + villageId + ": " + sqx
/* 103 */           .getMessage(), sqx);
/* 104 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 108 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 109 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void deleteAd(RecruitmentAd ad) {
/* 115 */     Connection dbcon = null;
/* 116 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 119 */       dbcon = DbConnector.getZonesDbCon();
/* 120 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGERECRUITMENT WHERE VILLAGE=?");
/* 121 */       ps.setInt(1, ad.getVillageId());
/* 122 */       ps.executeUpdate();
/*     */     
/*     */     }
/* 125 */     catch (SQLException sqex) {
/*     */       
/* 127 */       logger.log(Level.WARNING, "Failed to delete recruitment ad due to " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 131 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 132 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void deleteVillageAd(Player player) {
/* 138 */     Village village = Villages.getVillageForCreature((Creature)player);
/* 139 */     if (village == null) {
/*     */       
/* 141 */       player.getCommunicator().sendNormalServerMessage("You are not part of a village and can not delete any recruitment ads.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 146 */     RecruitmentAd ad = getVillageAd(village.getId(), player.getKingdomId());
/* 147 */     remove(ad);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void deleteVillageAd(Village village) {
/* 152 */     RecruitmentAd ad = getVillageAd(village, village.kingdom);
/* 153 */     if (ad != null) {
/* 154 */       remove(ad);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final RecruitmentAd[] getAllRecruitmentAds() {
/* 159 */     Set<RecruitmentAd> adSet = new HashSet<>();
/* 160 */     for (Map<Integer, RecruitmentAd> ads : recruitmentAds.values())
/*     */     {
/* 162 */       adSet.addAll(ads.values());
/*     */     }
/*     */     
/* 165 */     RecruitmentAd[] adsArray = new RecruitmentAd[adSet.size()];
/* 166 */     adSet.toArray(adsArray);
/* 167 */     return adsArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final RecruitmentAd[] getKingdomAds(int kingdom) {
/* 172 */     Map<Integer, RecruitmentAd> ads = recruitmentAds.get(Integer.valueOf(kingdom));
/* 173 */     if (ads == null)
/* 174 */       return null; 
/* 175 */     RecruitmentAd[] rad = new RecruitmentAd[ads.size()];
/* 176 */     return (RecruitmentAd[])ads.values().toArray((Object[])rad);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final RecruitmentAd getVillageAd(int villageId, int kingdom) {
/* 181 */     Map<Integer, RecruitmentAd> ads = recruitmentAds.get(Integer.valueOf(kingdom));
/* 182 */     if (ads == null)
/* 183 */       return null; 
/* 184 */     return ads.get(Integer.valueOf(villageId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final RecruitmentAd getVillageAd(Village village, int kingdom) {
/* 189 */     return getVillageAd(village.getId(), kingdom);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadRecruitmentAds() throws IOException {
/* 194 */     long start = System.nanoTime();
/* 195 */     int loadedAds = 0;
/* 196 */     Connection dbcon = null;
/* 197 */     PreparedStatement ps = null;
/* 198 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 201 */       dbcon = DbConnector.getZonesDbCon();
/* 202 */       ps = dbcon.prepareStatement("SELECT * FROM VILLAGERECRUITMENT");
/* 203 */       rs = ps.executeQuery();
/* 204 */       while (rs.next())
/*     */       {
/*     */         
/* 207 */         RecruitmentAd ad = new RecruitmentAd(rs.getInt("VILLAGE"), rs.getLong("CONTACT"), rs.getString("DESCRIPTION"), rs.getDate("CREATED"), rs.getInt("KINGDOM"));
/* 208 */         add(ad);
/* 209 */         loadedAds++;
/*     */       }
/*     */     
/* 212 */     } catch (SQLException sqex) {
/*     */       
/* 214 */       logger.log(Level.WARNING, "Failed to load recruitment ads due to " + sqex.getMessage(), sqex);
/* 215 */       throw new IOException("Failed to load recruitment ads", sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 219 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 220 */       DbConnector.returnConnection(dbcon);
/* 221 */       long end = System.nanoTime();
/* 222 */       logger.info("Loaded " + loadedAds + " ads from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void poll() {
/* 229 */     long now = System.currentTimeMillis();
/* 230 */     Date nowDate = new Date(now);
/* 231 */     long divider = 86400000L;
/* 232 */     for (Map<Integer, RecruitmentAd> kList : recruitmentAds.values()) {
/*     */       
/* 234 */       for (RecruitmentAd ad : kList.values()) {
/*     */ 
/*     */         
/* 237 */         long daysAfterCreation = (nowDate.getTime() - ad.getCreated().getTime()) / 86400000L;
/* 238 */         if (daysAfterCreation > 60L) {
/* 239 */           remove(ad);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void remove(RecruitmentAd ad) {
/* 246 */     Integer kingKey = Integer.valueOf(ad.getKingdom());
/* 247 */     Integer villKey = Integer.valueOf(ad.getVillageId());
/*     */     
/* 249 */     if (!recruitmentAds.containsKey(kingKey))
/*     */       return; 
/* 251 */     Map<Integer, RecruitmentAd> kList = recruitmentAds.get(kingKey);
/* 252 */     if (kList.containsKey(villKey)) {
/*     */       
/* 254 */       kList.remove(villKey);
/* 255 */       deleteAd(ad);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void update(int villageId, String description, long contact, Date updated, byte kingdomId) throws IOException {
/* 263 */     Connection dbcon = null;
/* 264 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 267 */       dbcon = DbConnector.getZonesDbCon();
/* 268 */       ps = dbcon.prepareStatement("UPDATE VILLAGERECRUITMENT SET DESCRIPTION =?, CONTACT =?, CREATED =? WHERE VILLAGE=?;");
/* 269 */       ps.setString(1, description);
/* 270 */       ps.setLong(2, contact);
/* 271 */       ps.setDate(3, updated);
/* 272 */       ps.setInt(4, villageId);
/* 273 */       ps.executeUpdate();
/*     */       
/* 275 */       RecruitmentAd ad = getVillageAd(villageId, kingdomId);
/* 276 */       ad.setDescription(description);
/* 277 */       ad.setCreated(updated);
/* 278 */       ad.setContactId(contact);
/*     */     }
/* 280 */     catch (SQLException sqx) {
/*     */       
/* 282 */       logger.log(Level.WARNING, "Failed to create new recruitment ad for village: " + villageId + ": " + sqx
/* 283 */           .getMessage(), sqx);
/* 284 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 288 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 289 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\RecruitmentAds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
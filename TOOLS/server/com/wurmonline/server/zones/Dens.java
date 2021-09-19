/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class Dens
/*     */   implements CreatureTemplateIds
/*     */ {
/*     */   private static final String GET_DENS = "select * from DENS";
/*     */   private static final String DELETE_DEN = "DELETE FROM DENS  where TEMPLATEID=?";
/*     */   private static final String CREATE_DEN = "insert into DENS(TEMPLATEID,TILEX, TILEY, SURFACED) values(?,?,?,?)";
/*  54 */   private static final Logger logger = Logger.getLogger(Dens.class.getName());
/*     */   
/*  56 */   private static final Map<Integer, Den> dens = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addDen(Den den) {
/*  67 */     dens.put(Integer.valueOf(den.getTemplateId()), den);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void removeDen(int templateId) {
/*  72 */     dens.remove(Integer.valueOf(templateId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Den getDen(int templateId) {
/*  77 */     return dens.get(Integer.valueOf(templateId));
/*     */   }
/*     */   
/*     */   public static Map<Integer, Den> getDens() {
/*  81 */     return Collections.unmodifiableMap(dens);
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
/*     */   public static Den getDen(int tilex, int tiley) {
/*  95 */     for (Den d : dens.values()) {
/*     */       
/*  97 */       if (d.getTilex() == tilex && d.getTiley() == tiley)
/*  98 */         return d; 
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadDens() {
/* 108 */     logger.info("Loading dens");
/* 109 */     long start = System.nanoTime();
/* 110 */     Connection dbcon = null;
/* 111 */     PreparedStatement ps = null;
/* 112 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 115 */       dbcon = DbConnector.getZonesDbCon();
/* 116 */       ps = dbcon.prepareStatement("select * from DENS");
/*     */       
/* 118 */       rs = ps.executeQuery();
/* 119 */       int tid = -1;
/* 120 */       int tilex = 0;
/* 121 */       int tiley = 0;
/* 122 */       boolean surfaced = false;
/* 123 */       while (rs.next()) {
/*     */         
/* 125 */         tid = rs.getInt("TEMPLATEID");
/* 126 */         tilex = rs.getInt("TILEX");
/* 127 */         tiley = rs.getInt("TILEY");
/* 128 */         surfaced = rs.getBoolean("SURFACED");
/* 129 */         if (tid > 0) {
/*     */           
/* 131 */           Den den = new Den(tid, tilex, tiley, surfaced);
/* 132 */           addDen(den);
/* 133 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 135 */             logger.fine("Loaded Den: " + den);
/*     */           }
/*     */         } 
/*     */       } 
/* 139 */       checkDens(false);
/*     */     }
/* 141 */     catch (SQLException sqx) {
/*     */       
/* 143 */       logger.log(Level.WARNING, "Problem loading Dens - " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 147 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 148 */       DbConnector.returnConnection(dbcon);
/* 149 */       long end = System.nanoTime();
/* 150 */       logger.info("Loaded " + dens.size() + " dens from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkDens(boolean whileRunning) {
/* 160 */     checkTemplate(16, whileRunning);
/* 161 */     checkTemplate(89, whileRunning);
/* 162 */     checkTemplate(91, whileRunning);
/* 163 */     checkTemplate(90, whileRunning);
/* 164 */     checkTemplate(92, whileRunning);
/* 165 */     checkTemplate(17, whileRunning);
/* 166 */     checkTemplate(18, whileRunning);
/* 167 */     checkTemplate(19, whileRunning);
/* 168 */     checkTemplate(104, whileRunning);
/* 169 */     checkTemplate(103, whileRunning);
/* 170 */     checkTemplate(20, whileRunning);
/* 171 */     checkTemplate(22, whileRunning);
/* 172 */     checkTemplate(27, whileRunning);
/* 173 */     checkTemplate(11, whileRunning);
/* 174 */     checkTemplate(26, whileRunning);
/* 175 */     checkTemplate(23, whileRunning);
/* 176 */     Constants.respawnUniques = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Den getDragonSpawnTop(int templateId) {
/* 181 */     switch (templateId) {
/*     */       
/*     */       case 16:
/* 184 */         return Zones.getNorthTop(templateId);
/*     */       case 89:
/* 186 */         return Zones.getWestTop(templateId);
/*     */       case 91:
/* 188 */         return Zones.getSouthTop(templateId);
/*     */       case 90:
/* 190 */         return Zones.getNorthTop(templateId);
/*     */       case 92:
/* 192 */         return Zones.getEastTop(templateId);
/*     */       case 17:
/* 194 */         return Zones.getSouthTop(templateId);
/*     */       case 18:
/* 196 */         return Zones.getEastTop(templateId);
/*     */       case 19:
/* 198 */         return Zones.getWestTop(templateId);
/*     */       case 103:
/* 200 */         return Zones.getSouthTop(templateId);
/*     */       case 104:
/* 202 */         return Zones.getNorthTop(templateId);
/*     */     } 
/* 204 */     return Zones.getRandomTop();
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
/*     */   private static void checkTemplate(int templateId, boolean whileRunning) {
/*     */     try {
/* 222 */       CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
/* 223 */       boolean creatureExists = Creatures.getInstance().creatureWithTemplateExists(templateId);
/* 224 */       if (Constants.respawnUniques || whileRunning) {
/*     */         
/* 226 */         if (whileRunning)
/*     */         {
/* 228 */           if (Server.rand.nextInt(300) > 0) {
/*     */             return;
/*     */           }
/*     */         }
/* 232 */         if (!creatureExists) {
/*     */           
/* 234 */           Den d = dens.get(Integer.valueOf(templateId));
/* 235 */           if (d != null)
/* 236 */             deleteDen(templateId); 
/*     */         } 
/*     */       } else {
/*     */         return;
/*     */       } 
/* 241 */       if (!dens.containsKey(Integer.valueOf(templateId))) {
/*     */         
/* 243 */         Den den = null;
/* 244 */         if (CreatureTemplate.isDragon(templateId)) {
/*     */           
/* 246 */           if (!Servers.localServer.isChallengeServer())
/*     */           {
/* 248 */             if (Constants.respawnUniques) {
/* 249 */               den = getDragonSpawnTop(templateId);
/* 250 */             } else if (Server.rand.nextBoolean()) {
/* 251 */               den = Zones.getRandomTop();
/*     */             } else {
/* 253 */               den = Zones.getRandomForest(templateId);
/*     */             } 
/* 255 */             if (den != null)
/*     */             {
/* 257 */               den = createDen(den.getTemplateId(), den.getTilex(), den.getTiley(), den.isSurfaced());
/*     */             }
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 263 */           if (template.getLeaderTemplateId() > 0) {
/*     */             
/* 265 */             den = getDen(template.getLeaderTemplateId());
/* 266 */             if (den != null) {
/* 267 */               den.setTemplateId(templateId);
/*     */             }
/*     */           } else {
/* 270 */             den = Zones.getRandomForest(templateId);
/* 271 */           }  if (den != null)
/*     */           {
/* 273 */             den = createDen(den.getTemplateId(), den.getTilex(), den.getTiley(), den.isSurfaced());
/*     */           }
/*     */         } 
/* 276 */         if (den != null)
/*     */         {
/*     */           
/* 279 */           if (template.isUnique()) {
/*     */             
/* 281 */             VolaTile villtile = Zones.getOrCreateTile(den.getTilex(), den.getTiley(), den.isSurfaced());
/* 282 */             Village vill = villtile.getVillage();
/* 283 */             if (vill != null) {
/*     */               
/* 285 */               logger.log(Level.INFO, "Unique spawn " + template.getName() + ", on deed " + vill.getName() + ".");
/*     */               
/* 287 */               removeDen(templateId);
/*     */               return;
/*     */             } 
/*     */           } 
/* 291 */           if (!template.isUnique()) {
/*     */             
/*     */             try
/*     */             {
/* 295 */               Zone zone = Zones.getZone(den.getTilex(), den.getTiley(), den.isSurfaced());
/* 296 */               zone.den = den;
/* 297 */               logger.log(Level.INFO, "Zone at " + den.getTilex() + ", " + den.getTiley() + " now spawning " + template
/* 298 */                   .getName() + " (" + den.getTemplateId() + ")");
/*     */             }
/* 300 */             catch (NoSuchZoneException nsz)
/*     */             {
/* 302 */               logger.log(Level.WARNING, "Den at " + den
/* 303 */                   .getTilex() + ", " + den.getTiley() + " surf=" + den.isSurfaced() + " - zone does not exist.");
/*     */             }
/*     */           
/*     */           }
/* 307 */           else if (!creatureExists) {
/*     */             
/* 309 */             byte ctype = (byte)Math.max(0, Server.rand.nextInt(22) - 10);
/*     */             
/* 311 */             if (Server.rand.nextInt(3) < 2)
/* 312 */               ctype = 0; 
/* 313 */             if (Server.rand.nextInt(40) == 0) {
/* 314 */               ctype = 99;
/*     */             }
/*     */             try {
/* 317 */               Creature.doNew(templateId, ctype, ((den.getTilex() << 2) + 2), ((den.getTiley() << 2) + 2), 180.0F, 
/* 318 */                   den.isSurfaced() ? 0 : -1, template.getName(), template.getSex());
/* 319 */               logger.log(Level.INFO, "Created " + template
/* 320 */                   .getName() + " at " + den.getTilex() + "," + den.getTiley() + "!");
/*     */             }
/* 322 */             catch (Exception ex) {
/*     */               
/* 324 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */             } 
/*     */           } 
/* 327 */           addDen(den);
/*     */         }
/*     */       
/* 330 */       } else if (!template.isUnique()) {
/*     */         
/* 332 */         Den den = getDen(templateId);
/*     */         
/*     */         try {
/* 335 */           Zone zone = Zones.getZone(den.getTilex(), den.getTiley(), den.isSurfaced());
/* 336 */           zone.den = den;
/* 337 */           logger.log(Level.INFO, "Zone at " + den
/* 338 */               .getTilex() + ", " + den.getTiley() + " now spawning " + template.getName() + " (" + den
/* 339 */               .getTemplateId() + ")");
/*     */         }
/* 341 */         catch (NoSuchZoneException nsz) {
/*     */           
/* 343 */           logger.log(Level.WARNING, "Den at " + den.getTilex() + ", " + den.getTiley() + " surf=" + den.isSurfaced() + " - zone does not exist.");
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 348 */     } catch (NoSuchCreatureTemplateException nst) {
/*     */       
/* 350 */       logger.log(Level.WARNING, templateId + ":" + nst.getMessage(), (Throwable)nst);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteDen(int templateId) {
/* 360 */     logger.log(Level.INFO, "Deleting den for " + templateId);
/* 361 */     Den d = getDen(templateId);
/* 362 */     if (d != null)
/* 363 */       logger.log(Level.INFO, "Den for " + templateId + " was at " + d.getTilex() + "," + d.getTiley()); 
/* 364 */     removeDen(templateId);
/* 365 */     Connection dbcon = null;
/* 366 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 369 */       dbcon = DbConnector.getZonesDbCon();
/* 370 */       ps = dbcon.prepareStatement("DELETE FROM DENS  where TEMPLATEID=?");
/* 371 */       ps.setInt(1, templateId);
/* 372 */       ps.executeUpdate();
/*     */     }
/* 374 */     catch (SQLException sqx) {
/*     */       
/* 376 */       logger.log(Level.WARNING, templateId + ":" + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 380 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 381 */       DbConnector.returnConnection(dbcon);
/*     */     } 
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
/*     */   private static Den createDen(int templateId, int tilex, int tiley, boolean surfaced) {
/* 397 */     Den den = new Den(templateId, tilex, tiley, surfaced);
/* 398 */     addDen(den);
/* 399 */     Connection dbcon = null;
/* 400 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 403 */       dbcon = DbConnector.getZonesDbCon();
/* 404 */       ps = dbcon.prepareStatement("insert into DENS(TEMPLATEID,TILEX, TILEY, SURFACED) values(?,?,?,?)");
/* 405 */       ps.setInt(1, templateId);
/* 406 */       ps.setInt(2, tilex);
/* 407 */       ps.setInt(3, tiley);
/* 408 */       ps.setBoolean(4, surfaced);
/* 409 */       ps.executeUpdate();
/*     */     }
/* 411 */     catch (SQLException sqx) {
/*     */       
/* 413 */       logger.log(Level.WARNING, templateId + ":" + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 417 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 418 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 420 */     return den;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Dens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
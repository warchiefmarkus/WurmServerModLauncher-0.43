/*     */ package com.wurmonline.server.batchjobs;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.items.BodyDbStrings;
/*     */ import com.wurmonline.server.items.CoinDbStrings;
/*     */ import com.wurmonline.server.items.DbStrings;
/*     */ import com.wurmonline.server.items.ItemDbStrings;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.ItemTypes;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
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
/*     */ public final class ItemBatchJob
/*     */   implements ItemTypes, MiscConstants, ItemMaterials
/*     */ {
/*     */   private static final String deleteLegs = "DELETE FROM BODYITEMS WHERE TEMPLATEID=10";
/*     */   private static final String deleteFeet = "DELETE FROM BODYITEMS WHERE TEMPLATEID=15";
/*     */   private static final String deleteLeg = "DELETE FROM BODYITEMS WHERE TEMPLATEID=19";
/*  53 */   private static Logger logger = Logger.getLogger(ItemBatchJob.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fixStructureGuests() {
/*  84 */     logger.log(Level.INFO, "Fixing structure guests.");
/*  85 */     Connection dbcon = null;
/*  86 */     PreparedStatement ps = null;
/*  87 */     PreparedStatement ps2 = null;
/*  88 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  91 */       dbcon = DbConnector.getZonesDbCon();
/*  92 */       String getAll = "select WURMID,GUESTS from STRUCTURES";
/*  93 */       ps = dbcon.prepareStatement("select WURMID,GUESTS from STRUCTURES");
/*  94 */       rs = ps.executeQuery();
/*  95 */       while (rs.next()) {
/*     */         
/*  97 */         long wurmid = rs.getLong("WURMID");
/*  98 */         long[] guestArr = (long[])rs.getObject("GUESTS");
/*  99 */         for (long lGuest : guestArr)
/*     */         {
/* 101 */           ps2 = dbcon.prepareStatement("INSERT INTO STRUCTUREGUESTS (STRUCTUREID,GUESTID)VALUES(?,?)");
/* 102 */           ps2.setLong(1, wurmid);
/* 103 */           ps2.setLong(2, lGuest);
/* 104 */           ps2.executeUpdate();
/* 105 */           ps2.close();
/*     */         }
/*     */       
/*     */       } 
/* 109 */     } catch (SQLException ex) {
/*     */       
/* 111 */       logger.log(Level.WARNING, "Failed to move structure guests.", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 115 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 116 */       DbUtilities.closeDatabaseObjects(ps2, null);
/* 117 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void fixZonesStructure() {
/* 123 */     logger.log(Level.INFO, "Fixing zone structures.");
/* 124 */     Connection dbcon = null;
/* 125 */     PreparedStatement ps = null;
/* 126 */     PreparedStatement ps2 = null;
/* 127 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 130 */       dbcon = DbConnector.getZonesDbCon();
/* 131 */       String getAll = "select ZONEID, STRUCTURES FROM ZONES";
/* 132 */       ps = dbcon.prepareStatement("select ZONEID, STRUCTURES FROM ZONES");
/* 133 */       rs = ps.executeQuery();
/* 134 */       while (rs.next()) {
/*     */         
/* 136 */         int zoneid = rs.getInt("ZONEID");
/* 137 */         long[] structArr = (long[])rs.getObject("STRUCTURES");
/* 138 */         for (long lStructure : structArr)
/*     */         {
/* 140 */           ps2 = dbcon.prepareStatement("INSERT INTO ZONESTRUCTURES (ZONEID,STRUCTUREID)VALUES(?,?)");
/* 141 */           ps2.setInt(1, zoneid);
/* 142 */           ps2.setLong(2, lStructure);
/* 143 */           ps2.executeUpdate();
/* 144 */           ps2.close();
/*     */         }
/*     */       
/*     */       } 
/* 148 */     } catch (SQLException ex) {
/*     */       
/* 150 */       logger.log(Level.WARNING, "Failed to move zone structure.", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 154 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 155 */       DbUtilities.closeDatabaseObjects(ps2, null);
/* 156 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setNames() {
/* 162 */     Connection dbcon = null;
/* 163 */     PreparedStatement ps = null;
/* 164 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 167 */       dbcon = DbConnector.getItemDbCon();
/* 168 */       String getAll = "select * from ITEMS";
/* 169 */       ps = dbcon.prepareStatement("select * from ITEMS");
/* 170 */       rs = ps.executeQuery();
/* 171 */       while (rs.next())
/*     */       {
/* 173 */         long wurmid = rs.getLong("WURMID");
/* 174 */         String description = rs.getString("NAME");
/* 175 */         setDescription(dbcon, wurmid, description);
/*     */       }
/*     */     
/* 178 */     } catch (SQLException ex) {
/*     */       
/* 180 */       logger.log(Level.WARNING, "Failed to check if item exists.", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 184 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 185 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setDescription(Connection dbcon, long wurmId, String desc) {
/* 191 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 194 */       String setAll = "update ITEMS set DESCRIPTION=?, NAME=\"\" where WURMID=?";
/* 195 */       ps = dbcon.prepareStatement("update ITEMS set DESCRIPTION=?, NAME=\"\" where WURMID=?");
/* 196 */       ps.setString(1, desc);
/* 197 */       ps.setLong(2, wurmId);
/* 198 */       ps.executeUpdate();
/* 199 */       ps.close();
/*     */     }
/* 201 */     catch (SQLException ex) {
/*     */       
/* 203 */       logger.log(Level.WARNING, "Failed to save item " + wurmId, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 207 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void deleteFeet() {
/* 213 */     Connection dbcon = null;
/* 214 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 217 */       dbcon = DbConnector.getItemDbCon();
/* 218 */       ps = dbcon.prepareStatement("DELETE FROM BODYITEMS WHERE TEMPLATEID=10");
/* 219 */       ps.executeUpdate();
/* 220 */       ps.close();
/* 221 */       ps = dbcon.prepareStatement("DELETE FROM BODYITEMS WHERE TEMPLATEID=19");
/* 222 */       ps.executeUpdate();
/* 223 */       ps.close();
/* 224 */       ps = dbcon.prepareStatement("DELETE FROM BODYITEMS WHERE TEMPLATEID=15");
/* 225 */       ps.executeUpdate();
/* 226 */       ps.close();
/*     */     }
/* 228 */     catch (SQLException sqx) {
/*     */       
/* 230 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 234 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 235 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isFish(int templateId) {
/* 241 */     return (templateId == 158 || templateId == 164 || templateId == 160 || templateId == 159 || templateId == 163 || templateId == 157 || templateId == 162 || templateId == 161 || templateId == 165);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void trimSizes() {
/* 248 */     trimSizes((DbStrings)ItemDbStrings.getInstance());
/* 249 */     trimSizes((DbStrings)BodyDbStrings.getInstance());
/* 250 */     trimSizes((DbStrings)CoinDbStrings.getInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void trimSizes(DbStrings instance) {
/* 255 */     Connection dbcon = null;
/* 256 */     PreparedStatement ps = null;
/* 257 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 260 */       dbcon = DbConnector.getItemDbCon();
/* 261 */       ps = dbcon.prepareStatement(instance.getItemWeights());
/* 262 */       rs = ps.executeQuery();
/* 263 */       int maxSizeMod = 5;
/* 264 */       while (rs.next()) {
/*     */ 
/*     */         
/*     */         try {
/* 268 */           long id = rs.getLong("WURMID");
/* 269 */           ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(rs.getInt("TEMPLATEID"));
/* 270 */           if (template == null)
/* 271 */             throw new WurmServerException("No template."); 
/* 272 */           int weight = rs.getInt("WEIGHT");
/* 273 */           if ((template.isCombine() && !template.isLiquid()) || isFish(template.getTemplateId())) {
/*     */ 
/*     */ 
/*     */             
/* 277 */             float mod = weight / template.getWeightGrams();
/* 278 */             if (mod > 125.0F) {
/*     */               
/* 280 */               setSizeZ(id, template.getSizeZ() * 5, dbcon, instance);
/* 281 */               setSizeY(id, template.getSizeY() * 5, dbcon, instance);
/* 282 */               setSizeX(id, template.getSizeX() * 5, dbcon, instance); continue;
/*     */             } 
/* 284 */             if (mod > 25.0F) {
/*     */               
/* 286 */               setSizeZ(id, template.getSizeZ() * 5, dbcon, instance);
/* 287 */               setSizeY(id, template.getSizeY() * 5, dbcon, instance);
/* 288 */               mod /= 25.0F;
/* 289 */               setSizeX(id, (int)(template.getSizeX() * mod), dbcon, instance); continue;
/*     */             } 
/* 291 */             if (mod > 5.0F) {
/*     */               
/* 293 */               setSizeZ(id, template.getSizeZ() * 5, dbcon, instance);
/* 294 */               mod /= 5.0F;
/* 295 */               setSizeY(id, (int)(template.getSizeY() * mod), dbcon, instance);
/* 296 */               setSizeX(id, template.getSizeX(), dbcon, instance);
/*     */               
/*     */               continue;
/*     */             } 
/* 300 */             setSizeZ(id, Math.max(1, (int)(template.getSizeZ() * mod)), dbcon, instance);
/* 301 */             setSizeY(id, Math.max(1, (int)(template.getSizeY() * mod)), dbcon, instance);
/* 302 */             setSizeX(id, Math.max(1, (int)(template.getSizeX() * mod)), dbcon, instance);
/*     */             continue;
/*     */           } 
/* 305 */           if (!template.isLiquid())
/*     */           {
/* 307 */             setSizeX(id, template.getSizeX(), dbcon, instance);
/* 308 */             setSizeY(id, template.getSizeY(), dbcon, instance);
/* 309 */             setSizeZ(id, template.getSizeZ(), dbcon, instance);
/*     */           }
/*     */         
/* 312 */         } catch (Exception ex) {
/*     */           
/* 314 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 316 */             logger.log(Level.FINE, "Problem: " + ex.getMessage(), ex);
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 321 */     } catch (SQLException sqx) {
/*     */       
/* 323 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 327 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 328 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setSizeX(long id, int sizex, Connection dbcon, DbStrings instance) {
/* 334 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 337 */       ps = dbcon.prepareStatement(instance.setSizeX());
/* 338 */       ps.setInt(1, sizex);
/* 339 */       ps.setLong(2, id);
/* 340 */       ps.executeUpdate();
/* 341 */       ps.close();
/*     */     }
/* 343 */     catch (SQLException sqx) {
/*     */       
/* 345 */       logger.log(Level.WARNING, "Failed to save item " + id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 349 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setSizeY(long id, int sizey, Connection dbcon, DbStrings instance) {
/* 355 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 358 */       ps = dbcon.prepareStatement(instance.setSizeY());
/* 359 */       ps.setInt(1, sizey);
/* 360 */       ps.setLong(2, id);
/* 361 */       ps.executeUpdate();
/* 362 */       ps.close();
/*     */     }
/* 364 */     catch (SQLException sqx) {
/*     */       
/* 366 */       logger.log(Level.WARNING, "Failed to save item " + id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 370 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setSizeZ(long id, int sizez, Connection dbcon, DbStrings instance) {
/* 376 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 379 */       ps = dbcon.prepareStatement(instance.setSizeZ());
/* 380 */       ps.setInt(1, sizez);
/* 381 */       ps.setLong(2, id);
/* 382 */       ps.executeUpdate();
/* 383 */       ps.close();
/*     */     }
/* 385 */     catch (SQLException sqx) {
/*     */       
/* 387 */       logger.log(Level.WARNING, "Failed to save item " + id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 391 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setMat(long id, byte material, DbStrings instance) {
/* 397 */     Connection dbcon = null;
/* 398 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 401 */       dbcon = DbConnector.getItemDbCon();
/* 402 */       ps = dbcon.prepareStatement(instance.setMaterial());
/* 403 */       ps.setByte(1, material);
/* 404 */       ps.setLong(2, id);
/* 405 */       ps.executeUpdate();
/* 406 */       ps.close();
/*     */     }
/* 408 */     catch (SQLException sqx) {
/*     */       
/* 410 */       logger.log(Level.WARNING, "Failed to save item " + id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 414 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 415 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setPar(long id, long pid, DbStrings instance) {
/* 421 */     Connection dbcon = null;
/* 422 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 425 */       dbcon = DbConnector.getItemDbCon();
/* 426 */       ps = dbcon.prepareStatement(instance.setParentId());
/* 427 */       ps.setLong(1, pid);
/* 428 */       ps.setLong(2, id);
/* 429 */       ps.executeUpdate();
/*     */     }
/* 431 */     catch (SQLException sqx) {
/*     */       
/* 433 */       logger.log(Level.WARNING, "Failed to save item " + id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 437 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 438 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setDesc(long id, String name, DbStrings instance) {
/* 444 */     Connection dbcon = null;
/* 445 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 448 */       dbcon = DbConnector.getItemDbCon();
/* 449 */       ps = dbcon.prepareStatement(instance.setName());
/* 450 */       ps.setString(1, name);
/* 451 */       ps.setLong(2, id);
/* 452 */       ps.executeUpdate();
/*     */     }
/* 454 */     catch (SQLException sqx) {
/*     */       
/* 456 */       logger.log(Level.WARNING, "Failed to save item " + id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 460 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 461 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setW(long id, int w, DbStrings instance) {
/* 467 */     Connection dbcon = null;
/* 468 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 471 */       dbcon = DbConnector.getItemDbCon();
/* 472 */       ps = dbcon.prepareStatement(instance.setWeight());
/* 473 */       ps.setInt(1, w);
/* 474 */       ps.setLong(2, id);
/* 475 */       ps.executeUpdate();
/*     */     }
/* 477 */     catch (SQLException sqx) {
/*     */       
/* 479 */       logger.log(Level.WARNING, "Failed to save item " + id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 483 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 484 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\batchjobs\ItemBatchJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
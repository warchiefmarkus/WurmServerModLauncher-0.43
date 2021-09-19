/*     */ package com.wurmonline.server.economy;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LocalSupplyDemand
/*     */ {
/*     */   private final Map<Integer, Float> demandList;
/*     */   private final long traderId;
/*  52 */   private static final Logger logger = Logger.getLogger(LocalSupplyDemand.class.getName());
/*     */   
/*     */   private static final float MAX_DEMAND = -200.0F;
/*     */   
/*     */   private static final float INITIAL_DEMAND = -100.0F;
/*     */   
/*     */   private static final float MIN_DEMAND = -0.001F;
/*     */   
/*     */   private static final String GET_ALL_ITEM_DEMANDS = "SELECT * FROM LOCALSUPPLYDEMAND WHERE TRADERID=?";
/*     */   
/*     */   private static final String UPDATE_DEMAND = "UPDATE LOCALSUPPLYDEMAND SET DEMAND=? WHERE ITEMID=? AND TRADERID=?";
/*     */   
/*  64 */   private static final String INCREASE_ALL_DEMANDS = DbConnector.isUseSqlite() ? "UPDATE LOCALSUPPLYDEMAND SET DEMAND=MAX(-200.0,DEMAND*1.1)" : "UPDATE LOCALSUPPLYDEMAND SET DEMAND=GREATEST(-200.0,DEMAND*1.1)";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CREATE_DEMAND = "INSERT INTO LOCALSUPPLYDEMAND (DEMAND,ITEMID,TRADERID) VALUES(?,?,?)";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LocalSupplyDemand(long aTraderId) {
/*  80 */     this.traderId = aTraderId;
/*  81 */     this.demandList = new HashMap<>();
/*  82 */     loadAllItemDemands();
/*  83 */     createUnexistingDemands();
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
/*     */   double getPrice(int itemTemplateId, double basePrice, int nums, boolean selling) {
/* 102 */     Float dem = this.demandList.get(Integer.valueOf(itemTemplateId));
/* 103 */     float demand = -100.0F;
/* 104 */     if (dem != null)
/* 105 */       demand = dem.floatValue(); 
/* 106 */     double price = 1.0D;
/* 107 */     float halfSize = 100.0F;
/*     */     
/*     */     try {
/* 110 */       halfSize = (ItemTemplateFactory.getInstance().getTemplate(itemTemplateId)).priceHalfSize;
/*     */     }
/* 112 */     catch (NoSuchTemplateException nst) {
/*     */       
/* 114 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */     } 
/* 116 */     for (int x = 0; x < nums; x++) {
/*     */       
/* 118 */       if (selling) {
/*     */         
/* 120 */         price = basePrice * Math.max(0.20000000298023224D, Math.pow(demand / halfSize, 2.0D));
/* 121 */         demand = Math.max(-200.0F, demand - 1.0F);
/*     */       }
/*     */       else {
/*     */         
/* 125 */         demand = Math.min(-0.001F, demand + 1.0F);
/* 126 */         price = basePrice * Math.pow(demand / halfSize, 2.0D);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     return Math.max(0.0D, price);
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
/*     */   public void addItemSold(int itemTemplateId, float times) {
/* 146 */     Float dem = this.demandList.get(Integer.valueOf(itemTemplateId));
/* 147 */     float demand = -100.0F;
/* 148 */     if (dem != null)
/* 149 */       demand = dem.floatValue(); 
/* 150 */     demand -= times;
/* 151 */     demand = Math.max(-200.0F, demand);
/* 152 */     this.demandList.put(Integer.valueOf(itemTemplateId), new Float(demand));
/* 153 */     if (dem == null) {
/*     */       
/* 155 */       createDemand(itemTemplateId, demand);
/*     */     } else {
/*     */       
/* 158 */       updateDemand(itemTemplateId, demand);
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
/*     */   public void addItemPurchased(int itemTemplateId, float times) {
/* 170 */     Float dem = this.demandList.get(Integer.valueOf(itemTemplateId));
/* 171 */     float demand = -100.0F;
/* 172 */     if (dem != null)
/* 173 */       demand = dem.floatValue(); 
/* 174 */     demand = Math.min(-0.001F, demand + times);
/* 175 */     this.demandList.put(Integer.valueOf(itemTemplateId), new Float(demand));
/* 176 */     if (dem == null) {
/*     */       
/* 178 */       createDemand(itemTemplateId, demand);
/*     */     } else {
/*     */       
/* 181 */       updateDemand(itemTemplateId, demand);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lowerDemands() {
/* 191 */     ItemDemand[] dems = getItemDemands();
/* 192 */     for (ItemDemand lDem : dems) {
/*     */       
/* 194 */       lDem.setDemand(Math.max(-200.0F, lDem.getDemand() * 1.1F));
/* 195 */       this.demandList.put(Integer.valueOf(lDem.getTemplateId()), new Float(lDem.getDemand()));
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
/*     */   public ItemDemand[] getItemDemands() {
/* 207 */     ItemDemand[] dems = new ItemDemand[0];
/* 208 */     if (this.demandList.size() > 0) {
/*     */       
/* 210 */       dems = new ItemDemand[this.demandList.size()];
/* 211 */       int x = 0;
/* 212 */       for (Map.Entry<Integer, Float> entry : this.demandList.entrySet()) {
/*     */         
/* 214 */         int item = ((Integer)entry.getKey()).intValue();
/* 215 */         float demand = ((Float)entry.getValue()).floatValue();
/* 216 */         dems[x] = new ItemDemand(item, demand);
/* 217 */         x++;
/*     */       } 
/*     */     } 
/* 220 */     return dems;
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
/*     */   private void loadAllItemDemands() {
/* 232 */     long start = System.currentTimeMillis();
/* 233 */     Connection dbcon = null;
/* 234 */     PreparedStatement ps = null;
/* 235 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 238 */       dbcon = DbConnector.getEconomyDbCon();
/* 239 */       ps = dbcon.prepareStatement("SELECT * FROM LOCALSUPPLYDEMAND WHERE TRADERID=?");
/* 240 */       ps.setLong(1, this.traderId);
/* 241 */       rs = ps.executeQuery();
/* 242 */       while (rs.next())
/*     */       {
/* 244 */         this.demandList.put(Integer.valueOf(rs.getInt("ITEMID")), new Float(Math.min(-0.001F, rs.getFloat("DEMAND"))));
/*     */       }
/*     */     }
/* 247 */     catch (SQLException sqx) {
/*     */       
/* 249 */       logger.log(Level.WARNING, "Failed to load supplyDemand for trader " + this.traderId + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 253 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 254 */       DbConnector.returnConnection(dbcon);
/* 255 */       if (logger.isLoggable(Level.FINER)) {
/*     */         
/* 257 */         long end = System.currentTimeMillis();
/* 258 */         logger.finer("Loading LocalSupplyDemand for Trader: " + this.traderId + " took " + (end - start) + " ms");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createUnexistingDemands() {
/* 268 */     ItemTemplate[] templates = ItemTemplateFactory.getInstance().getTemplates();
/* 269 */     for (ItemTemplate lTemplate : templates) {
/*     */       
/* 271 */       if (lTemplate.isPurchased()) {
/*     */         
/* 273 */         Float dem = this.demandList.get(Integer.valueOf(lTemplate.getTemplateId()));
/* 274 */         if (dem == null) {
/*     */           
/* 276 */           createDemand(lTemplate.getTemplateId(), -100.0F);
/* 277 */           this.demandList.put(Integer.valueOf(lTemplate.getTemplateId()), Float.valueOf(-100.0F));
/*     */         } 
/*     */       } 
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
/*     */   private void updateDemand(int itemId, float demand) {
/* 293 */     Connection dbcon = null;
/* 294 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 297 */       dbcon = DbConnector.getEconomyDbCon();
/* 298 */       ps = dbcon.prepareStatement("UPDATE LOCALSUPPLYDEMAND SET DEMAND=? WHERE ITEMID=? AND TRADERID=?");
/* 299 */       ps.setFloat(1, Math.min(-0.001F, demand));
/* 300 */       ps.setInt(2, itemId);
/* 301 */       ps.setLong(3, this.traderId);
/* 302 */       ps.executeUpdate();
/*     */     }
/* 304 */     catch (SQLException sqx) {
/*     */       
/* 306 */       logger.log(Level.WARNING, "Failed to update trader " + this.traderId + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 310 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 311 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void increaseAllDemands() {
/* 322 */     Connection dbcon = null;
/* 323 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 326 */       dbcon = DbConnector.getEconomyDbCon();
/* 327 */       ps = dbcon.prepareStatement(INCREASE_ALL_DEMANDS);
/* 328 */       ps.executeUpdate();
/*     */     }
/* 330 */     catch (SQLException sqx) {
/*     */       
/* 332 */       logger.log(Level.WARNING, "Failed to increase all demands due to " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 336 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 337 */       DbConnector.returnConnection(dbcon);
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
/*     */   private void createDemand(int itemId, float demand) {
/* 351 */     Connection dbcon = null;
/* 352 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 355 */       dbcon = DbConnector.getEconomyDbCon();
/* 356 */       ps = dbcon.prepareStatement("INSERT INTO LOCALSUPPLYDEMAND (DEMAND,ITEMID,TRADERID) VALUES(?,?,?)");
/* 357 */       ps.setFloat(1, Math.min(-0.001F, demand));
/* 358 */       ps.setInt(2, itemId);
/* 359 */       ps.setLong(3, this.traderId);
/* 360 */       ps.executeUpdate();
/*     */     }
/* 362 */     catch (SQLException sqx) {
/*     */       
/* 364 */       logger.log(Level.WARNING, "Failed to update trader " + this.traderId + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 368 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 369 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\LocalSupplyDemand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
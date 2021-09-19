/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class ItemMealData
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(ItemMealData.class.getName());
/*     */   
/*  41 */   private static final Map<Long, ItemMealData> mealData = new ConcurrentHashMap<>();
/*     */   
/*     */   private static final String GET_ALL_MEAL_DATA = "SELECT * FROM MEALDATA";
/*     */   
/*     */   private static final String CREATE_MEAL_DATA = "INSERT INTO MEALDATA(MEALID,RECIPEID,CALORIES,CARBS,FATS,PROTEINS,BONUS,STAGESCOUNT,INGREDIENTSCOUNT) VALUES(?,?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String DELETE_MEAL_DATA = "DELETE FROM MEALDATA WHERE MEALID=?";
/*     */   
/*     */   private static final String UPDATE_MEAL_DATA = "UPDATE MEALDATA SET RECIPEID=?,CALORIES=?,CARBS=?,FATS=?,PROTEINS=?,BONUS=? WHERE MEALID=?";
/*     */   private final long wurmId;
/*     */   private short recipeId;
/*     */   private short calories;
/*     */   private short carbs;
/*     */   private short fats;
/*     */   private short proteins;
/*     */   private byte bonus;
/*     */   private byte stagesCount;
/*     */   private byte ingredientsCount;
/*     */   
/*     */   public ItemMealData(long mealId, short recipeId, short calorie, short carb, short fat, short protein, byte bonus, byte stages, byte ingredients) {
/*  61 */     this.wurmId = mealId;
/*  62 */     this.recipeId = recipeId;
/*  63 */     this.calories = calorie;
/*  64 */     this.carbs = carb;
/*  65 */     this.fats = fat;
/*  66 */     this.proteins = protein;
/*  67 */     this.bonus = bonus;
/*  68 */     this.stagesCount = stages;
/*  69 */     this.ingredientsCount = ingredients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMealId() {
/*  77 */     return this.wurmId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getRecipeId() {
/*  85 */     return this.recipeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCalories() {
/*  93 */     return this.calories;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCarbs() {
/* 101 */     return this.carbs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getFats() {
/* 109 */     return this.fats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getProteins() {
/* 117 */     return this.proteins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getBonus() {
/* 125 */     return this.bonus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getStages() {
/* 133 */     return this.stagesCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getIngredients() {
/* 141 */     return this.ingredientsCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getBonus(long playerId) {
/* 149 */     return (byte)((int)(this.bonus + (playerId >> 24L)) & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean update(short recipeId, short calorie, short carb, short fat, short protein, byte bonus) {
/* 155 */     this.recipeId = recipeId;
/* 156 */     this.calories = calorie;
/* 157 */     this.carbs = carb;
/* 158 */     this.fats = fat;
/* 159 */     this.proteins = protein;
/* 160 */     this.bonus = bonus;
/* 161 */     return dbUpdateMealData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dbUpdateMealData() {
/* 170 */     Connection dbcon = null;
/* 171 */     PreparedStatement ps = null;
/* 172 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 175 */       dbcon = DbConnector.getItemDbCon();
/* 176 */       ps = dbcon.prepareStatement("UPDATE MEALDATA SET RECIPEID=?,CALORIES=?,CARBS=?,FATS=?,PROTEINS=?,BONUS=? WHERE MEALID=?");
/* 177 */       ps.setShort(1, this.recipeId);
/* 178 */       ps.setShort(2, this.calories);
/* 179 */       ps.setShort(3, this.carbs);
/* 180 */       ps.setShort(4, this.fats);
/* 181 */       ps.setShort(5, this.proteins);
/* 182 */       ps.setByte(6, this.bonus);
/* 183 */       ps.setLong(7, this.wurmId);
/* 184 */       if (ps.executeUpdate() == 1) {
/* 185 */         return true;
/*     */       }
/* 187 */       return dbSaveMealData();
/*     */     }
/* 189 */     catch (SQLException sqex) {
/*     */       
/* 191 */       logger.log(Level.WARNING, "Failed to update item (meal) data: " + sqex.getMessage(), sqex);
/* 192 */       return false;
/*     */     }
/*     */     finally {
/*     */       
/* 196 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 197 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dbSaveMealData() {
/* 207 */     Connection dbcon = null;
/* 208 */     PreparedStatement ps = null;
/* 209 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 212 */       dbcon = DbConnector.getItemDbCon();
/* 213 */       ps = dbcon.prepareStatement("INSERT INTO MEALDATA(MEALID,RECIPEID,CALORIES,CARBS,FATS,PROTEINS,BONUS,STAGESCOUNT,INGREDIENTSCOUNT) VALUES(?,?,?,?,?,?,?,?,?)");
/* 214 */       ps.setLong(1, this.wurmId);
/* 215 */       ps.setShort(2, this.recipeId);
/* 216 */       ps.setShort(3, this.calories);
/* 217 */       ps.setShort(4, this.carbs);
/* 218 */       ps.setShort(5, this.fats);
/* 219 */       ps.setShort(6, this.proteins);
/* 220 */       ps.setByte(7, this.bonus);
/* 221 */       ps.setByte(8, this.stagesCount);
/* 222 */       ps.setByte(9, this.ingredientsCount);
/* 223 */       ps.executeUpdate();
/* 224 */       return true;
/*     */     }
/* 226 */     catch (SQLException sqex) {
/*     */       
/* 228 */       logger.log(Level.WARNING, "Failed to save item (meal) data: " + sqex.getMessage(), sqex);
/* 229 */       return false;
/*     */     }
/*     */     finally {
/*     */       
/* 233 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 234 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dbDeleteMealData() {
/* 244 */     Connection dbcon = null;
/* 245 */     PreparedStatement ps = null;
/* 246 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 249 */       dbcon = DbConnector.getItemDbCon();
/* 250 */       ps = dbcon.prepareStatement("DELETE FROM MEALDATA WHERE MEALID=?");
/* 251 */       ps.setLong(1, this.wurmId);
/* 252 */       ps.executeUpdate();
/* 253 */       return true;
/*     */     }
/* 255 */     catch (SQLException sqex) {
/*     */       
/* 257 */       logger.log(Level.WARNING, "Failed to delete item (meal) data: " + sqex.getMessage(), sqex);
/* 258 */       return false;
/*     */     }
/*     */     finally {
/*     */       
/* 262 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 263 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final ItemMealData add(ItemMealData itemData) {
/* 273 */     return mealData.put(Long.valueOf(itemData.getMealId()), itemData);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemMealData getItemMealData(long mealId) {
/* 278 */     return mealData.get(Long.valueOf(mealId));
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
/*     */   public static final void save(long mealId, short recipeId, short calories, short carbs, short fats, short proteins, byte bonus, byte stages, byte ingredients) {
/* 294 */     ItemMealData imd = new ItemMealData(mealId, recipeId, calories, carbs, fats, proteins, bonus, stages, ingredients);
/* 295 */     if (add(imd) != null) {
/*     */ 
/*     */       
/* 298 */       imd.dbUpdateMealData();
/*     */     } else {
/*     */       
/* 301 */       imd.dbSaveMealData();
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
/*     */   public static final void update(long mealId, short recipeId, short calories, short carbs, short fats, short proteins, byte bonus, byte stages, byte ingredients) {
/* 317 */     ItemMealData imd = mealData.get(Long.valueOf(mealId));
/* 318 */     if (imd != null) {
/*     */       
/* 320 */       imd.update(recipeId, calories, carbs, fats, proteins, bonus);
/*     */     }
/*     */     else {
/*     */       
/* 324 */       save(mealId, recipeId, calories, carbs, fats, proteins, bonus, stages, ingredients);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean delete(long mealId) {
/* 335 */     ItemMealData imd = mealData.get(Long.valueOf(mealId));
/* 336 */     if (imd != null)
/* 337 */       return imd.dbDeleteMealData(); 
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int loadAllMealData() {
/* 346 */     int count = 0;
/* 347 */     Connection dbcon = null;
/* 348 */     PreparedStatement ps = null;
/* 349 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 352 */       dbcon = DbConnector.getItemDbCon();
/* 353 */       ps = dbcon.prepareStatement("SELECT * FROM MEALDATA");
/* 354 */       rs = ps.executeQuery();
/* 355 */       while (rs.next())
/*     */       {
/* 357 */         count++;
/* 358 */         long mealId = rs.getLong("MEALID");
/* 359 */         short recipeId = rs.getShort("RECIPEID");
/* 360 */         short calories = rs.getShort("CALORIES");
/* 361 */         short carbs = rs.getShort("CARBS");
/* 362 */         short fats = rs.getShort("FATS");
/* 363 */         short proteins = rs.getShort("PROTEINS");
/* 364 */         byte bonus = rs.getByte("BONUS");
/* 365 */         byte stages = rs.getByte("STAGESCOUNT");
/* 366 */         byte ingredients = rs.getByte("INGREDIENTSCOUNT");
/* 367 */         add(new ItemMealData(mealId, recipeId, calories, carbs, fats, proteins, bonus, stages, ingredients));
/*     */       }
/*     */     
/* 370 */     } catch (SQLException sqex) {
/*     */       
/* 372 */       logger.log(Level.WARNING, "Failed to load all meal data: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 376 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 377 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 379 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemMealData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
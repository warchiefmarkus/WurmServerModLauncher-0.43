/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.gui.folders.DistEntity;
/*      */ import com.wurmonline.server.gui.folders.Folders;
/*      */ import com.wurmonline.server.gui.folders.GameEntity;
/*      */ import com.wurmonline.server.players.Achievement;
/*      */ import com.wurmonline.server.players.AchievementList;
/*      */ import com.wurmonline.server.players.AchievementTemplate;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.support.JSONArray;
/*      */ import com.wurmonline.server.support.JSONException;
/*      */ import com.wurmonline.server.support.JSONObject;
/*      */ import com.wurmonline.server.support.JSONTokener;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.shared.constants.IconConstants;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ public class Recipes
/*      */   implements ItemMaterials, MiscConstants, AchievementList
/*      */ {
/*   68 */   private static final Logger logger = Logger.getLogger(Recipes.class.getName());
/*      */   
/*   70 */   private static final Map<Short, Recipe> recipes = new HashMap<>();
/*      */   
/*   72 */   private static final Map<Short, String> namedRecipes = new HashMap<>();
/*      */   
/*   74 */   private static final List<Recipe> recipesList = new ArrayList<>();
/*      */ 
/*      */   
/*      */   private static final String GET_ALL_NAMED_RECIPES = "SELECT * FROM RECIPESNAMED";
/*      */ 
/*      */   
/*      */   private static final String CREATE_NAMED_RECIPE = "INSERT INTO RECIPESNAMED (RECIPEID, NAMER) VALUES(?,?)";
/*      */ 
/*      */   
/*      */   private static final String DELETE_NAMED_RECIPE = "DELETE FROM RECIPESNAMED WHERE RECIPEID=?";
/*      */ 
/*      */   
/*      */   public static final byte NOT_FOUND = 0;
/*      */ 
/*      */   
/*      */   public static final byte FOUND = 1;
/*      */ 
/*      */   
/*      */   public static final byte SWAPPED = 2;
/*      */ 
/*      */ 
/*      */   
/*      */   public static void add(Recipe recipe) {
/*   97 */     recipes.put(Short.valueOf(recipe.getMenuId()), recipe);
/*      */     
/*   99 */     recipesList.add(recipe);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean exists(short recipeId) {
/*  104 */     return recipes.containsKey(Short.valueOf((short)(recipeId + 8000)));
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadAllRecipes() {
/*  109 */     loadRecipes(Folders.getDist().getPathFor(DistEntity.Recipes));
/*  110 */     if (!GameEntity.Recipes.existsIn(Folders.getCurrent()))
/*      */       
/*      */       try {
/*  113 */         Files.createDirectory(Folders.getCurrent().getPathFor(GameEntity.Recipes), (FileAttribute<?>[])new FileAttribute[0]);
/*      */       }
/*  115 */       catch (IOException e) {
/*      */         
/*  117 */         logger.warning("Could not create recipe folder");
/*      */         return;
/*      */       }  
/*  120 */     loadRecipes(Folders.getCurrent().getPathFor(GameEntity.Recipes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadRecipes(Path path) {
/*  128 */     logger.info("Loading all Recipes");
/*  129 */     long start = System.nanoTime();
/*      */ 
/*      */     
/*      */     try {
/*  133 */       Files.walk(path, new java.nio.file.FileVisitOption[0])
/*  134 */         .sorted()
/*  135 */         .forEachOrdered(p -> {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             if (!Files.isDirectory(p, new java.nio.file.LinkOption[0])) {
/*      */               if (p.getFileName().toString().startsWith("recipe ") && p.getFileName().toString().endsWith(".json") && p.getFileName().toString().length() == 16) {
/*      */                 
/*      */                 readRecipeFile(p.toString());
/*      */               } else {
/*      */                 
/*      */                 logger.log(Level.INFO, "recipe file name (" + p.toString() + ") is not in correct format, expected \" recipe xxxx.json\" where xxxx are the recipe id (same as in the file).");
/*      */               } 
/*      */             }
/*      */           });
/*  152 */     } catch (IOException e) {
/*      */ 
/*      */       
/*  155 */       logger.warning("Exception loading recipes");
/*  156 */       e.printStackTrace();
/*      */     } 
/*  158 */     int numberOfRecipes = recipes.size();
/*  159 */     logger.log(Level.INFO, "Total number of recipes=" + numberOfRecipes + ".");
/*      */     
/*  161 */     int numberOfKnownRecipes = RecipesByPlayer.loadAllPlayerKnownRecipes();
/*  162 */     logger.log(Level.INFO, "Number of player known recipes=" + numberOfKnownRecipes + ".");
/*      */     
/*  164 */     int numberOfNamedRecipes = dbNamedRecipes();
/*  165 */     logger.log(Level.INFO, "Number of named recipes=" + numberOfNamedRecipes + ".");
/*      */     
/*  167 */     logger.log(Level.INFO, "Recipes loaded. That took " + (
/*  168 */         (float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Recipe[] getNamedRecipesFor(String playerName) {
/*  174 */     Set<Recipe> recipes = new HashSet<>();
/*  175 */     for (Map.Entry<Short, String> entry : namedRecipes.entrySet()) {
/*      */       
/*  177 */       if (((String)entry.getValue()).equalsIgnoreCase(playerName)) {
/*      */         
/*  179 */         Recipe recipe = getRecipeById(((Short)entry.getKey()).shortValue());
/*      */         
/*  181 */         if (recipe != null)
/*      */         {
/*  183 */           recipes.add(recipe);
/*      */         }
/*      */       } 
/*      */     } 
/*  187 */     return recipes.<Recipe>toArray(new Recipe[recipes.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Recipe[] getNamedRecipes() {
/*  192 */     Set<Recipe> recipes = new HashSet<>();
/*  193 */     for (Map.Entry<Short, String> entry : namedRecipes.entrySet()) {
/*      */       
/*  195 */       Recipe recipe = getRecipeById(((Short)entry.getKey()).shortValue());
/*      */       
/*  197 */       if (recipe != null)
/*      */       {
/*  199 */         recipes.add(recipe);
/*      */       }
/*      */     } 
/*  202 */     return recipes.<Recipe>toArray(new Recipe[recipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int dbNamedRecipes() {
/*  211 */     int count = 0;
/*  212 */     int failed = 0;
/*  213 */     Connection dbcon = null;
/*  214 */     PreparedStatement ps = null;
/*  215 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  218 */       dbcon = DbConnector.getItemDbCon();
/*  219 */       ps = dbcon.prepareStatement("SELECT * FROM RECIPESNAMED");
/*  220 */       rs = ps.executeQuery();
/*  221 */       while (rs.next())
/*      */       {
/*  223 */         short recipeId = rs.getShort("RECIPEID");
/*      */         
/*  225 */         String namer = rs.getString("NAMER");
/*  226 */         long playerId = PlayerInfoFactory.getWurmId(namer);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  231 */         namedRecipes.put(Short.valueOf(recipeId), namer);
/*  232 */         count++;
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  241 */     catch (SQLException sqex) {
/*      */       
/*  243 */       logger.log(Level.WARNING, "Failed to get namer on known recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  247 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  248 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  250 */     if (failed > 0)
/*  251 */       logger.log(Level.INFO, "Number of removed named recipes=" + failed + "."); 
/*  252 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setRecipeNamer(Recipe recipe, Creature creature) {
/*  262 */     if (recipe.isNameable() && !namedRecipes.containsKey(Short.valueOf(recipe.getRecipeId())))
/*      */     {
/*  264 */       if (creature.isPlayer() && !creature.hasFlag(50)) {
/*      */         
/*  266 */         namedRecipes.put(Short.valueOf(recipe.getRecipeId()), creature.getName());
/*  267 */         creature.setFlag(50, true);
/*  268 */         dbSetRecipeNamer(recipe.getRecipeId(), creature.getName());
/*      */         
/*  270 */         for (Player player : Players.getInstance().getPlayers()) {
/*      */ 
/*      */           
/*  273 */           if (player.isViewingCookbook())
/*      */           {
/*  275 */             if (RecipesByPlayer.isKnownRecipe(player.getWurmId(), recipe.getRecipeId())) {
/*  276 */               player.getCommunicator().sendCookbookRecipe(recipe);
/*      */             }
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String getRecipeNamer(short recipeId) {
/*  291 */     return namedRecipes.get(Short.valueOf(recipeId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short getNamedRecipe(String namer) {
/*  301 */     for (Map.Entry<Short, String> entry : namedRecipes.entrySet()) {
/*      */       
/*  303 */       if (((String)entry.getValue()).equalsIgnoreCase(namer))
/*      */       {
/*  305 */         return ((Short)entry.getKey()).shortValue();
/*      */       }
/*      */     } 
/*  308 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbSetRecipeNamer(short recipeId, String namer) {
/*  319 */     Connection dbcon = null;
/*  320 */     PreparedStatement ps = null;
/*  321 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  324 */       dbcon = DbConnector.getItemDbCon();
/*  325 */       ps = dbcon.prepareStatement("INSERT INTO RECIPESNAMED (RECIPEID, NAMER) VALUES(?,?)");
/*  326 */       ps.setShort(1, recipeId);
/*  327 */       ps.setString(2, namer);
/*  328 */       ps.executeUpdate();
/*      */     }
/*  330 */     catch (SQLException sqex) {
/*      */       
/*  332 */       logger.log(Level.WARNING, "Failed to save namer (" + namer + ") on recipe (" + recipeId + "): " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  336 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  337 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean removeRecipeNamer(short recipeId) {
/*  348 */     String namer = namedRecipes.remove(Short.valueOf(recipeId));
/*  349 */     if (namer != null) {
/*      */ 
/*      */       
/*  352 */       PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithName(namer);
/*  353 */       if (pInfo != null)
/*  354 */         pInfo.setFlag(50, false); 
/*      */     } 
/*  356 */     dbRemoveRecipeNamer(recipeId);
/*  357 */     return (namer != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbRemoveRecipeNamer(short recipeId) {
/*  363 */     Connection dbcon = null;
/*  364 */     PreparedStatement ps = null;
/*  365 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  368 */       dbcon = DbConnector.getItemDbCon();
/*  369 */       ps = dbcon.prepareStatement("DELETE FROM RECIPESNAMED WHERE RECIPEID=?");
/*  370 */       ps.setShort(1, recipeId);
/*  371 */       ps.executeUpdate();
/*      */     }
/*  373 */     catch (SQLException sqex) {
/*      */       
/*  375 */       logger.log(Level.WARNING, "Failed to delete entry for recipe (" + recipeId + "): " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  379 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  380 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void readRecipeFile(String fileName) {
/*      */     try {
/*  393 */       File file = new File(fileName);
/*  394 */       if (!file.exists()) {
/*      */         
/*  396 */         logger.log(Level.INFO, "file '" + fileName + "' not found!");
/*      */         
/*      */         return;
/*      */       } 
/*  400 */       BufferedReader br = new BufferedReader(new FileReader(file));
/*      */       
/*  402 */       StringBuilder sb = new StringBuilder(); String line;
/*  403 */       while ((line = br.readLine()) != null)
/*      */       {
/*  405 */         sb.append(line.trim());
/*      */       }
/*  407 */       br.close();
/*      */       
/*  409 */       InputStream in = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
/*      */ 
/*      */       
/*      */       try {
/*  413 */         String name = "";
/*  414 */         String recipeId = "unknown";
/*  415 */         String skillName = "";
/*      */         
/*      */         try {
/*  418 */           JSONTokener tk = new JSONTokener(in);
/*  419 */           JSONObject recipeJO = new JSONObject(tk);
/*      */           
/*  421 */           if (recipeJO.has("name")) {
/*  422 */             name = recipeJO.getString("name");
/*      */           }
/*  424 */           if (recipeJO.has("recipeid")) {
/*      */             
/*  426 */             recipeId = recipeJO.getString("recipeid");
/*      */ 
/*      */             
/*  429 */             if (recipeId.length() == 1) {
/*  430 */               recipeId = "000" + recipeId;
/*  431 */             } else if (recipeId.length() == 2) {
/*  432 */               recipeId = "00" + recipeId;
/*  433 */             } else if (recipeId.length() == 3) {
/*  434 */               recipeId = "0" + recipeId;
/*  435 */             }  if (recipeId.length() != 4)
/*  436 */               throw new JSONException("RecipeId " + recipeId + " for '" + name + "' is wrong length, should be 1 to 4 digits."); 
/*  437 */             if (!fileName.endsWith("recipe " + recipeId + ".json")) {
/*  438 */               throw new JSONException("RecipeId " + recipeId + " does not match the filename (" + fileName + ").");
/*      */             }
/*      */           } else {
/*  441 */             throw new JSONException("RecipeId for '" + name + "' is missing.");
/*      */           } 
/*  443 */           short rid = 0;
/*      */           
/*      */           try {
/*  446 */             rid = Short.parseShort(recipeId);
/*      */           }
/*  448 */           catch (NumberFormatException e) {
/*      */             
/*  450 */             throw new JSONException("RecipeId for '" + name + "' (" + recipeId + ") is not a number.");
/*      */           } 
/*  452 */           if (name.length() == 0)
/*  453 */             throw new JSONException("Name missing for recipe id of " + recipeId + "."); 
/*  454 */           checkRecipeSchema(rid, "recipe", recipeJO);
/*  455 */           if (rid < 1 || rid > 1999)
/*      */           {
/*  457 */             throw new JSONException("RecipeId for '" + name + "' (" + recipeId + ") is not in range (1..1999), was " + rid + " in recipe.");
/*      */           }
/*      */ 
/*      */           
/*  461 */           if (recipes.containsKey(Short.valueOf((short)(rid + 8000)))) {
/*      */             
/*  463 */             logger.info("Recipe '" + name + "' (" + recipeId + ") already exists, replacing");
/*  464 */             recipes.remove(Short.valueOf((short)(rid + 8000)));
/*      */           } 
/*      */           
/*  467 */           Recipe recipe = new Recipe(name, rid);
/*  468 */           if (recipeJO.has("skill"))
/*  469 */             skillName = recipeJO.getString("skill"); 
/*  470 */           if (skillName.length() == 0)
/*  471 */             throw new JSONException("Skill name missing for '" + name + "' (" + recipeId + ")."); 
/*  472 */           int skillId = SkillSystem.getSkillByName(skillName);
/*  473 */           if (skillId > 0) {
/*      */             
/*  475 */             recipe.setSkill(skillId, skillName);
/*      */           }
/*      */           else {
/*      */             
/*  479 */             throw new JSONException("Skill '" + skillName + "' does not exist in recipe '" + name + "' (" + recipeId + ").");
/*      */           } 
/*      */           
/*  482 */           if (recipeJO.has("known")) {
/*  483 */             recipe.setKnown(recipeJO.getBoolean("known"));
/*      */           }
/*  485 */           if (recipeJO.has("nameable")) {
/*  486 */             recipe.setNameable(recipeJO.getBoolean("nameable"));
/*      */           }
/*  488 */           if (recipeJO.has("lootable")) {
/*      */             
/*  490 */             JSONObject jo = recipeJO.getJSONObject("lootable");
/*  491 */             checkRecipeSchema(rid, "lootable", jo);
/*  492 */             int cid = -10;
/*  493 */             byte rarity = -1;
/*      */             
/*  495 */             if (jo.has("creature")) {
/*      */               
/*  497 */               String creatureName = jo.getString("creature");
/*      */               
/*      */               try {
/*  500 */                 CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(creatureName);
/*  501 */                 cid = ct.getTemplateId();
/*      */               }
/*  503 */               catch (Exception e) {
/*      */ 
/*      */                 
/*  506 */                 throw new JSONException("Recipe '" + name + "' (" + recipeId + ") Creature '" + creatureName + "' does not exist as a creature.");
/*      */               } 
/*      */             } else {
/*      */               
/*  510 */               throw new JSONException("Recipe '" + name + "' (" + recipeId + ") Lootable is missing creature.");
/*      */             } 
/*  512 */             if (jo.has("rarity")) {
/*      */               
/*  514 */               String rarityName = jo.getString("rarity");
/*  515 */               rarity = convertRarityStringIntoByte(recipe.getRecipeId(), rarityName);
/*      */             } else {
/*      */               
/*  518 */               throw new JSONException("Recipe '" + name + "' (" + recipeId + ") Lootable is missing rarity.");
/*      */             } 
/*  520 */             recipe.setLootable(cid, rarity);
/*      */           } 
/*  522 */           if (recipeJO.has("trigger")) {
/*      */             
/*  524 */             String triggerName = recipeJO.getString("trigger");
/*  525 */             byte triggerId = -1;
/*  526 */             switch (triggerName) {
/*      */               
/*      */               case "heat":
/*  529 */                 triggerId = 1;
/*      */                 break;
/*      */               case "time":
/*  532 */                 triggerId = 0;
/*      */                 break;
/*      */               case "create":
/*  535 */                 triggerId = 2;
/*      */                 break;
/*      */             } 
/*  538 */             if (triggerId >= 0) {
/*      */               
/*  540 */               recipe.setTrigger(triggerId);
/*      */             }
/*      */             else {
/*      */               
/*  544 */               throw new JSONException("Trigger '" + triggerName + "' does not exist.");
/*      */             } 
/*      */           } 
/*  547 */           if (recipeJO.has("cookers")) {
/*      */             
/*  549 */             JSONArray cja = recipeJO.getJSONArray("cookers");
/*  550 */             for (int c = 0; c < cja.length(); c++) {
/*      */               
/*  552 */               JSONObject jo = cja.getJSONObject(c);
/*  553 */               checkRecipeSchema(rid, "cookers", jo);
/*  554 */               String cooker = jo.getString("id");
/*      */               
/*  556 */               ItemTemplate cookerIT = ItemTemplateFactory.getInstance().getTemplate(cooker);
/*  557 */               if (cookerIT == null)
/*      */               {
/*  559 */                 throw new JSONException("Cooker '" + cooker + "' does not exist as a template.");
/*      */               }
/*  561 */               if (!cookerIT.isCooker())
/*      */               {
/*  563 */                 throw new JSONException("Cooker '" + cooker + "' cannot be used to make food in.");
/*      */               }
/*  565 */               int dif = 0;
/*  566 */               if (jo.has("difficulty"))
/*  567 */                 dif = jo.getInt("difficulty"); 
/*  568 */               if (dif < 0 || dif > 100)
/*      */               {
/*  570 */                 throw new JSONException("Difficulty for cooker '" + cooker + "' is out of range (0..100), was " + dif + " in recipe.");
/*      */               }
/*      */               
/*  573 */               recipe.addToCookerList(cookerIT.getTemplateId(), cooker, dif);
/*      */             } 
/*      */           } 
/*  576 */           if (recipeJO.has("containers")) {
/*      */             
/*  578 */             JSONArray cja = recipeJO.getJSONArray("containers");
/*  579 */             for (int c = 0; c < cja.length(); c++) {
/*      */               
/*  581 */               JSONObject jo = cja.getJSONObject(c);
/*  582 */               checkRecipeSchema(rid, "containers", jo);
/*  583 */               String container = jo.getString("id");
/*      */               
/*  585 */               ItemTemplate containerIT = ItemTemplateFactory.getInstance().getTemplate(container);
/*  586 */               if (containerIT == null)
/*      */               {
/*  588 */                 throw new JSONException("Container '" + container + "' does not exist as a template.");
/*      */               }
/*  590 */               if (!containerIT.isFoodMaker() && !containerIT.isRecipeItem())
/*      */               {
/*  592 */                 throw new JSONException("Container '" + container + "' cannot be used to make food in.");
/*      */               }
/*  594 */               int dif = 0;
/*  595 */               if (jo.has("difficulty"))
/*  596 */                 dif = jo.getInt("difficulty"); 
/*  597 */               if (dif < 0 || dif > 100)
/*      */               {
/*  599 */                 throw new JSONException("Difficulty for container '" + container + "' is out of range (0..100), was " + dif + " in recipe.");
/*      */               }
/*      */               
/*  602 */               recipe.addToContainerList(containerIT.getTemplateId(), container, dif);
/*      */             } 
/*      */           } 
/*  605 */           if (recipeJO.has("active")) {
/*      */             
/*  607 */             JSONObject activeJO = recipeJO.getJSONObject("active");
/*  608 */             checkRecipeSchema(rid, "active", activeJO);
/*  609 */             readIngredient(recipe, activeJO, "Active item", false, true, true, -2);
/*      */           } 
/*      */           
/*  612 */           if (recipeJO.has("target")) {
/*      */             
/*  614 */             JSONObject targetJO = recipeJO.getJSONObject("target");
/*  615 */             checkRecipeSchema(rid, "target", targetJO);
/*  616 */             readIngredient(recipe, targetJO, "Target item", false, true, true, -1);
/*      */           } 
/*      */           
/*  619 */           if (recipeJO.has("ingredients")) {
/*      */             
/*  621 */             JSONObject ingredientsJO = recipeJO.getJSONObject("ingredients");
/*  622 */             checkRecipeSchema(rid, "ingredients group", ingredientsJO);
/*  623 */             if (ingredientsJO.has("mandatory")) {
/*      */               
/*  625 */               IngredientGroup group = new IngredientGroup((byte)1);
/*  626 */               recipe.addToIngredientGroupList(group);
/*  627 */               JSONArray groupJA = ingredientsJO.getJSONArray("mandatory");
/*  628 */               for (int i = 0; i < groupJA.length(); i++) {
/*      */                 
/*  630 */                 JSONObject ingredientJO = groupJA.getJSONObject(i);
/*  631 */                 checkRecipeSchema(rid, "mandatory", ingredientJO);
/*  632 */                 readIngredient(recipe, ingredientJO, "Mandatory Ingredient", false, false, true, recipe
/*  633 */                     .getCurrentGroupId());
/*      */               } 
/*      */             } 
/*  636 */             if (ingredientsJO.has("zeroorone")) {
/*      */               
/*  638 */               JSONArray zerooroneJA = ingredientsJO.getJSONArray("zeroorone");
/*  639 */               for (int i = 0; i < zerooroneJA.length(); i++) {
/*      */                 
/*  641 */                 IngredientGroup group = new IngredientGroup((byte)2);
/*  642 */                 recipe.addToIngredientGroupList(group);
/*  643 */                 JSONObject groupJO = zerooroneJA.getJSONObject(i);
/*  644 */                 checkRecipeSchema(rid, "zeroorone group", groupJO);
/*  645 */                 JSONArray listJA = groupJO.getJSONArray("list");
/*  646 */                 for (int j = 0; j < listJA.length(); j++) {
/*      */                   
/*  648 */                   JSONObject listJO = listJA.getJSONObject(j);
/*  649 */                   checkRecipeSchema(rid, "zeroorone", listJO);
/*  650 */                   readIngredient(recipe, listJO, "ZeroOrOne Ingredient", false, false, true, recipe
/*  651 */                       .getCurrentGroupId());
/*      */                 } 
/*      */               } 
/*      */             } 
/*  655 */             if (ingredientsJO.has("oneof")) {
/*      */               
/*  657 */               JSONArray oneOfJA = ingredientsJO.getJSONArray("oneof");
/*  658 */               for (int i = 0; i < oneOfJA.length(); i++) {
/*      */                 
/*  660 */                 IngredientGroup group = new IngredientGroup((byte)3);
/*  661 */                 recipe.addToIngredientGroupList(group);
/*  662 */                 JSONObject groupJO = oneOfJA.getJSONObject(i);
/*  663 */                 checkRecipeSchema(rid, "oneof group", groupJO);
/*  664 */                 JSONArray listJA = groupJO.getJSONArray("list");
/*  665 */                 for (int j = 0; j < listJA.length(); j++) {
/*      */                   
/*  667 */                   JSONObject listJO = listJA.getJSONObject(j);
/*  668 */                   checkRecipeSchema(rid, "oneof", listJO);
/*  669 */                   readIngredient(recipe, listJO, "OneOf Ingredient ", false, false, true, recipe
/*  670 */                       .getCurrentGroupId());
/*      */                 } 
/*      */               } 
/*      */             } 
/*  674 */             if (ingredientsJO.has("oneormore")) {
/*      */               
/*  676 */               JSONArray oneormoreJA = ingredientsJO.getJSONArray("oneormore");
/*  677 */               for (int i = 0; i < oneormoreJA.length(); i++) {
/*      */                 
/*  679 */                 IngredientGroup group = new IngredientGroup((byte)4);
/*  680 */                 recipe.addToIngredientGroupList(group);
/*  681 */                 JSONObject groupJO = oneormoreJA.getJSONObject(i);
/*  682 */                 checkRecipeSchema(rid, "oneormore group", groupJO);
/*  683 */                 JSONArray listJA = groupJO.getJSONArray("list");
/*  684 */                 for (int j = 0; j < listJA.length(); j++) {
/*      */                   
/*  686 */                   JSONObject listJO = listJA.getJSONObject(j);
/*  687 */                   checkRecipeSchema(rid, "oneormore", listJO);
/*  688 */                   readIngredient(recipe, listJO, "OneOrMore Ingredient", false, false, true, recipe
/*  689 */                       .getCurrentGroupId());
/*      */                 } 
/*      */               } 
/*      */             } 
/*  693 */             if (ingredientsJO.has("optional")) {
/*      */               
/*  695 */               IngredientGroup group = new IngredientGroup((byte)5);
/*  696 */               recipe.addToIngredientGroupList(group);
/*  697 */               JSONArray groupJA = ingredientsJO.getJSONArray("optional");
/*  698 */               for (int i = 0; i < groupJA.length(); i++) {
/*      */                 
/*  700 */                 JSONObject ingredientJO = groupJA.getJSONObject(i);
/*  701 */                 checkRecipeSchema(rid, "optional", ingredientJO);
/*  702 */                 readIngredient(recipe, ingredientJO, "Optional Ingredient", false, false, true, recipe
/*  703 */                     .getCurrentGroupId());
/*      */               } 
/*      */             } 
/*  706 */             if (ingredientsJO.has("any")) {
/*      */               
/*  708 */               IngredientGroup group = new IngredientGroup((byte)6);
/*  709 */               recipe.addToIngredientGroupList(group);
/*  710 */               JSONArray groupJA = ingredientsJO.getJSONArray("any");
/*  711 */               for (int i = 0; i < groupJA.length(); i++) {
/*      */                 
/*  713 */                 JSONObject ingredientJO = groupJA.getJSONObject(i);
/*  714 */                 checkRecipeSchema(rid, "any", ingredientJO);
/*  715 */                 readIngredient(recipe, ingredientJO, "Any Ingredient", false, false, true, recipe
/*  716 */                     .getCurrentGroupId());
/*      */               } 
/*      */             } 
/*      */           } 
/*  720 */           JSONObject resultJO = recipeJO.getJSONObject("result");
/*  721 */           checkRecipeSchema(rid, "result", resultJO);
/*  722 */           readIngredient(recipe, resultJO, "Result item", true, false, true, -3);
/*      */           
/*  724 */           add(recipe);
/*      */         
/*      */         }
/*  727 */         catch (JSONException e) {
/*      */           
/*  729 */           if (name.equals("")) {
/*  730 */             logger.log(Level.WARNING, "Failed to load recipe from file " + fileName, (Throwable)e);
/*      */           } else {
/*  732 */             logger.log(Level.WARNING, "Failed to load recipe " + name + " {" + recipeId + "}", (Throwable)e);
/*      */           } 
/*      */         } 
/*  735 */       } catch (JSONException e) {
/*      */ 
/*      */         
/*  738 */         int lpos = e.getMessage().indexOf("[character ");
/*  739 */         if (lpos > -1) {
/*      */ 
/*      */           
/*  742 */           String rline = e.getMessage().substring(lpos + 11);
/*  743 */           String ss = rline.substring(0, rline.indexOf(" line 1]"));
/*      */           
/*      */           try {
/*  746 */             int pos = Integer.parseInt(ss);
/*      */             
/*  748 */             int p = sb.lastIndexOf("recipeid", pos);
/*  749 */             if (p > -1)
/*      */             {
/*  751 */               int pio = sb.indexOf("\"", p + 11);
/*  752 */               String recipeId = sb.substring(p + 11, pio);
/*  753 */               String lloc = sb.substring(pos - 17, pos - 2);
/*  754 */               String bad = sb.substring(pos - 2, pos - 1);
/*  755 */               String rloc = sb.substring(pos - 1, pos + 16);
/*  756 */               logger.log(Level.INFO, "Bad recipeid:" + recipeId + " |" + lloc + ">" + bad + "<" + rloc);
/*      */             }
/*      */           
/*  759 */           } catch (NumberFormatException|StringIndexOutOfBoundsException numberFormatException) {}
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  765 */         logger.log(Level.SEVERE, "Unable to load recipes:" + e.getMessage(), (Throwable)e);
/*      */       }
/*      */       finally {
/*      */         
/*  769 */         in.close();
/*      */       }
/*      */     
/*  772 */     } catch (Exception e) {
/*      */ 
/*      */       
/*  775 */       logger.log(Level.SEVERE, "Unable to load recipes:" + e.getMessage(), e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void readIngredient(Recipe recipe, JSONObject currentJO, String ingredientType, boolean isResult, boolean canBeTool, boolean canBeRecipeItem, int groupId) throws JSONException {
/*  794 */     String idName = currentJO.getString("id");
/*      */     
/*  796 */     ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(idName);
/*  797 */     if (template == null)
/*      */     {
/*  799 */       throw new JSONException(ingredientType + " '" + idName + "' does not exist as a template.");
/*      */     }
/*  801 */     if (!template.isFood() && !template.isLiquidCooking())
/*      */     {
/*      */       
/*  804 */       if (canBeTool) {
/*      */         
/*  806 */         if (canBeRecipeItem) {
/*      */           
/*  808 */           if (!template.isCookingTool() && !template.isRecipeItem()) {
/*  809 */             throw new JSONException(ingredientType + " '" + idName + "' is not a food item or tool or usable item.");
/*      */           
/*      */           }
/*      */         }
/*  813 */         else if (!template.isCookingTool()) {
/*  814 */           throw new JSONException(ingredientType + " '" + idName + "' is not a food item or tool.");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  819 */       else if (canBeRecipeItem) {
/*      */         
/*  821 */         if (!template.isRecipeItem() && !template.canBeFermented()) {
/*  822 */           throw new JSONException(ingredientType + " '" + idName + "' is not a food item or usable item.");
/*      */         }
/*      */       } else {
/*      */         
/*  826 */         throw new JSONException(ingredientType + " '" + idName + "' is not a food item.");
/*      */       } 
/*      */     }
/*      */     
/*  830 */     Ingredient ingredient = new Ingredient(template, isResult, (byte)groupId);
/*      */     
/*  832 */     if (currentJO.has("cstate")) {
/*      */       
/*  834 */       String cstate = currentJO.getString("cstate");
/*  835 */       ingredient.setCState(convertCookingStateIntoByte(recipe.getRecipeId(), cstate), cstate);
/*      */     } 
/*      */     
/*  838 */     if (currentJO.has("pstate")) {
/*      */       
/*  840 */       String pstate = currentJO.getString("pstate");
/*  841 */       ingredient.setPState(convertPhysicalStateIntoByte(recipe.getRecipeId(), pstate), pstate);
/*      */     } 
/*      */     
/*  844 */     if (currentJO.has("material")) {
/*      */       
/*  846 */       String material = currentJO.getString("material");
/*  847 */       byte mat = Materials.convertMaterialStringIntoByte(material);
/*  848 */       if (mat == 0)
/*  849 */         throw new JSONException(ingredientType + " Material '" + material + "' does not exist as a material."); 
/*  850 */       ingredient.setMaterial(mat, material);
/*      */     } 
/*      */     
/*  853 */     if (currentJO.has("realtemplate")) {
/*      */ 
/*      */       
/*  856 */       String realTemplateName = currentJO.getString("realtemplate");
/*      */ 
/*      */ 
/*      */       
/*  860 */       if (realTemplateName.equalsIgnoreCase("none")) {
/*      */         
/*  862 */         ingredient.setRealTemplate(null);
/*      */       }
/*      */       else {
/*      */         
/*  866 */         ItemTemplate realIT = ItemTemplateFactory.getInstance().getTemplate(realTemplateName);
/*  867 */         if (realIT == null)
/*      */         {
/*  869 */           throw new JSONException(ingredientType + " RealTemplate '" + realTemplateName + "' does not exist as a template.");
/*      */         }
/*  871 */         ingredient.setRealTemplate(realIT);
/*      */       } 
/*      */     } 
/*  874 */     if (currentJO.has("difficulty")) {
/*      */       
/*  876 */       int dif = currentJO.getInt("difficulty");
/*  877 */       if (dif < 0 || dif > 100)
/*      */       {
/*  879 */         throw new JSONException("Difficulty for ingredient '" + idName + "' is out of range (0..100), was " + dif + " in recipe.");
/*      */       }
/*      */       
/*  882 */       ingredient.setDifficulty(dif);
/*      */     } 
/*  884 */     if (isResult) {
/*      */ 
/*      */       
/*  887 */       if (currentJO.has("name"))
/*      */       {
/*  889 */         ingredient.setResultName(currentJO.getString("name"));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  894 */       if (currentJO.has("refmaterial")) {
/*      */ 
/*      */ 
/*      */         
/*  898 */         String ingredientRef = currentJO.getString("refmaterial");
/*  899 */         if (recipe.getTargetItem() != null && recipe
/*  900 */           .getTargetItem().getTemplateName().equalsIgnoreCase(ingredientRef)) {
/*      */           
/*  902 */           ingredient.setMaterialRef(ingredientRef);
/*      */         }
/*      */         else {
/*      */           
/*  906 */           IngredientGroup group = recipe.getGroupByType(1);
/*  907 */           if (group != null && group.contains(ingredientRef)) {
/*      */             
/*  909 */             ingredient.setMaterialRef(ingredientRef);
/*      */           }
/*      */           else {
/*      */             
/*  913 */             throw new JSONException("Result ref material '" + ingredientRef + "' does not reference a mandatory ingredient.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  918 */       if (currentJO.has("refrealtemplate")) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  925 */         String ingredientRef = currentJO.getString("refrealtemplate");
/*  926 */         if (recipe.getTargetItem() != null && recipe.getTargetItem().getTemplateName().equalsIgnoreCase(ingredientRef)) {
/*      */           
/*  928 */           ingredient.setRealTemplateRef(ingredientRef);
/*      */         }
/*  930 */         else if (recipe.hasContainer(ingredientRef)) {
/*      */           
/*  932 */           ingredient.setRealTemplateRef(ingredientRef);
/*      */         }
/*      */         else {
/*      */           
/*  936 */           IngredientGroup group = recipe.getGroupByType(1);
/*  937 */           if (group != null && group.contains(ingredientRef)) {
/*      */             
/*  939 */             ingredient.setRealTemplateRef(ingredientRef);
/*      */           }
/*      */           else {
/*      */             
/*  943 */             throw new JSONException("Result ref realtemplate '" + ingredientRef + "' does not reference a mandatory ingredient.");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  948 */       if (currentJO.has("achievement")) {
/*      */ 
/*      */         
/*  951 */         String achievementStr = currentJO.getString("achievement");
/*  952 */         AchievementTemplate at = Achievement.getTemplate(achievementStr);
/*  953 */         if (at != null) {
/*      */           
/*  955 */           if (at.isForCooking()) {
/*  956 */             recipe.setAchievementTriggered(at.getNumber(), at.getName());
/*      */           } else {
/*  958 */             throw new JSONException("Achievement '" + achievementStr + "' is not for recipes.");
/*      */           } 
/*      */         } else {
/*      */           
/*  962 */           throw new JSONException("Achievement '" + achievementStr + "' does not reference an achievement.");
/*      */         } 
/*      */       } 
/*      */       
/*  966 */       if (currentJO.has("usetemplateweight"))
/*      */       {
/*      */         
/*  969 */         ingredient.setUseResultTemplateWeight(currentJO.getBoolean("usetemplateweight"));
/*      */       }
/*      */       
/*  972 */       if (currentJO.has("description"))
/*      */       {
/*  974 */         ingredient.setResultDescription(currentJO.getString("description"));
/*      */       }
/*      */       
/*  977 */       if (currentJO.has("icon"))
/*      */       {
/*  979 */         String iconName = currentJO.getString("icon");
/*  980 */         int icon = IconConstants.getRecipeIconFromName(iconName);
/*  981 */         if (icon < 0)
/*      */         {
/*  983 */           throw new JSONException("No Icon found with name of '" + iconName + "' in recipe in '" + recipe.getName() + "' (" + recipe
/*  984 */               .getRecipeId() + ").");
/*      */         }
/*  986 */         ingredient.setIcon((short)icon);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  992 */       if (currentJO.has("creature")) {
/*      */         
/*  994 */         String corpseName = currentJO.getString("creature");
/*      */         
/*      */         try {
/*  997 */           CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(corpseName);
/*  998 */           ingredient.setCorpseData(ct.getTemplateId(), corpseName);
/*      */         }
/* 1000 */         catch (Exception e) {
/*      */ 
/*      */           
/* 1003 */           throw new JSONException(ingredientType + " Creature '" + corpseName + "' does not exist as a creature.");
/*      */         } 
/*      */       } 
/* 1006 */       if (ingredient.isLiquid()) {
/*      */ 
/*      */         
/* 1009 */         if (currentJO.has("ratio"))
/*      */         {
/* 1011 */           int rat = currentJO.getInt("ratio");
/* 1012 */           if (rat < 0 || rat > 10000)
/*      */           {
/* 1014 */             throw new JSONException("Ratio percentage for ingredient '" + idName + "' is out of range (0..10000), was " + rat + " in recipe.");
/*      */           }
/*      */           
/* 1017 */           ingredient.setRatio(rat);
/*      */         }
/*      */         else
/*      */         {
/* 1021 */           throw new JSONException("Ratio is missing for liquid ingredient '" + idName + "'.");
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1027 */       else if (currentJO.has("amount")) {
/*      */         
/* 1029 */         int amo = currentJO.getInt("amount");
/* 1030 */         if (amo < 1 || amo > 3)
/*      */         {
/* 1032 */           throw new JSONException("Amount for ingredient '" + idName + "' is out of range (1..3), was " + amo + " in recipe.");
/*      */         }
/*      */         
/* 1035 */         ingredient.setAmount(amo);
/*      */       } 
/*      */ 
/*      */       
/* 1039 */       if (currentJO.has("loss")) {
/*      */         
/* 1041 */         int los = currentJO.getInt("loss");
/* 1042 */         if (los < 0 || los > 100)
/*      */         {
/* 1044 */           throw new JSONException("Loss for ingredient '" + idName + "' is out of range (0..100), was " + los + " in recipe.");
/*      */         }
/*      */         
/* 1047 */         ingredient.setLoss(los);
/*      */       }
/* 1049 */       else if (ingredient.isLiquid()) {
/*      */         
/* 1051 */         throw new JSONException("Loss is missing for liquid ingredient '" + idName + "'.");
/*      */       } 
/* 1053 */       ingredient.setIngredientId(recipe.getIngredientCount());
/*      */     } 
/* 1055 */     recipe.addIngredient(ingredient);
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
/*      */   private static void checkRecipeSchema(short recipeId, String level, JSONObject recipeJO) throws JSONException {
/* 1068 */     Iterator<String> keys = recipeJO.keys();
/* 1069 */     while (keys.hasNext()) {
/*      */       
/* 1071 */       String nextKey = keys.next();
/* 1072 */       switch (level) {
/*      */ 
/*      */         
/*      */         case "recipe":
/* 1076 */           switch (nextKey) {
/*      */             case "name":
/*      */             case "recipeid":
/*      */             case "skill":
/*      */             case "trigger":
/*      */             case "cookers":
/*      */             case "containers":
/*      */             case "active":
/*      */             case "target":
/*      */             case "ingredients":
/*      */             case "result":
/*      */             case "known":
/*      */             case "nameable":
/*      */             case "lootable":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1094 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "lootable":
/* 1100 */           switch (nextKey) {
/*      */             case "creature":
/*      */             case "rarity":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1107 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "cookers":
/*      */         case "containers":
/* 1114 */           switch (nextKey) {
/*      */             case "id":
/*      */             case "difficulty":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1121 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "active":
/* 1127 */           switch (nextKey) {
/*      */             case "id":
/*      */             case "cstate":
/*      */             case "pstate":
/*      */             case "material":
/*      */             case "realtemplate":
/*      */             case "difficulty":
/*      */             case "loss":
/*      */             case "ratio":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1140 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "target":
/* 1146 */           switch (nextKey) {
/*      */             case "id":
/*      */             case "cstate":
/*      */             case "pstate":
/*      */             case "material":
/*      */             case "realtemplate":
/*      */             case "difficulty":
/*      */             case "loss":
/*      */             case "ratio":
/*      */             case "creature":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1160 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "ingredients group":
/* 1166 */           switch (nextKey) {
/*      */             case "mandatory":
/*      */             case "optional":
/*      */             case "oneof":
/*      */             case "zeroorone":
/*      */             case "oneormore":
/*      */             case "any":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1177 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "oneof group":
/*      */         case "zeroorone group":
/*      */         case "oneormore group":
/* 1185 */           switch (nextKey) {
/*      */             case "list":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1191 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "mandatory":
/*      */         case "optional":
/*      */         case "oneof":
/*      */         case "zeroorone":
/*      */         case "oneormore":
/*      */         case "any":
/* 1203 */           switch (nextKey) {
/*      */             case "id":
/*      */             case "cstate":
/*      */             case "pstate":
/*      */             case "material":
/*      */             case "realtemplate":
/*      */             case "difficulty":
/*      */             case "loss":
/*      */             case "ratio":
/*      */             case "amount":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1217 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case "result":
/* 1224 */           switch (nextKey) {
/*      */             case "id":
/*      */             case "name":
/*      */             case "cstate":
/*      */             case "pstate":
/*      */             case "material":
/*      */             case "realtemplate":
/*      */             case "refmaterial":
/*      */             case "refrealtemplate":
/*      */             case "difficulty":
/*      */             case "description":
/*      */             case "achievement":
/*      */             case "usetemplateweight":
/*      */             case "icon":
/*      */               continue;
/*      */           } 
/*      */ 
/*      */           
/* 1242 */           throw new JSONException("Recipe " + recipeId + " invalid " + level + " attribute " + nextKey + ".");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1247 */       throw new JSONException("Recipe " + recipeId + " invalid " + level + " when checing attributes .");
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
/*      */   private static byte convertCookingStateIntoByte(short recipeId, String state) {
/* 1259 */     switch (state) {
/*      */ 
/*      */       
/*      */       case "raw":
/* 1263 */         return 0;
/*      */       case "fried":
/* 1265 */         return 1;
/*      */       case "grilled":
/* 1267 */         return 2;
/*      */       case "boiled":
/* 1269 */         return 3;
/*      */       case "roasted":
/* 1271 */         return 4;
/*      */       case "steamed":
/* 1273 */         return 5;
/*      */       case "baked":
/* 1275 */         return 6;
/*      */       case "cooked":
/* 1277 */         return 7;
/*      */       case "candied":
/* 1279 */         return 8;
/*      */       case "chocolate coated":
/* 1281 */         return 9;
/*      */     } 
/*      */     
/* 1284 */     logger.warning("Recipe " + recipeId + " has unknown state name:" + state);
/* 1285 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte convertPhysicalStateIntoByte(short recipeId, String state) {
/* 1295 */     if (state.contains("+")) {
/*      */ 
/*      */       
/* 1298 */       String[] states = state.split("\\+");
/* 1299 */       byte theByte = 0;
/* 1300 */       for (String s : states) {
/*      */         
/* 1302 */         byte code = convertPhysicalStateIntoByte(recipeId, s);
/*      */         
/* 1304 */         theByte = (byte)(theByte | code);
/*      */       } 
/* 1306 */       return theByte;
/*      */     } 
/*      */ 
/*      */     
/* 1310 */     switch (state) {
/*      */ 
/*      */       
/*      */       case "none":
/* 1314 */         return 0;
/*      */       case "chopped":
/* 1316 */         return 16;
/*      */       case "diced":
/* 1318 */         return 16;
/*      */       case "ground":
/* 1320 */         return 16;
/*      */       case "unfermented":
/* 1322 */         return 16;
/*      */       case "zombiefied":
/* 1324 */         return 16;
/*      */       case "whipped":
/* 1326 */         return 16;
/*      */       case "mashed":
/* 1328 */         return 32;
/*      */       case "minced":
/* 1330 */         return 32;
/*      */       case "fermenting":
/* 1332 */         return 32;
/*      */       case "clotted":
/* 1334 */         return 32;
/*      */       case "wrapped":
/* 1336 */         return 64;
/*      */       case "undistilled":
/* 1338 */         return 64;
/*      */       case "salted":
/* 1340 */         return Byte.MIN_VALUE;
/*      */       case "fresh":
/* 1342 */         return Byte.MIN_VALUE;
/*      */     } 
/*      */     
/* 1345 */     logger.warning("Recipe " + recipeId + " has unknown state name:" + state);
/* 1346 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte convertRarityStringIntoByte(short recipeId, String rarityName) {
/* 1352 */     switch (rarityName) {
/*      */       
/*      */       case "common":
/* 1355 */         return 0;
/*      */       case "rare":
/* 1357 */         return 1;
/*      */       case "supreme":
/* 1359 */         return 2;
/*      */       case "fantastic":
/* 1361 */         return 3;
/*      */     } 
/* 1363 */     logger.warning("Recipe " + recipeId + " has unknown rarity name:" + rarityName);
/* 1364 */     return 0;
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
/*      */   @Nullable
/*      */   public static Recipe getRecipeFor(long playerId, byte wantedType, @Nullable Item activeItem, Item targetItem, boolean checkActive, boolean checkLiquids) {
/* 1387 */     return getRecipeFor(playerId, activeItem, targetItem, wantedType, checkActive, checkLiquids);
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
/*      */   @Nullable
/*      */   private static Recipe getRecipeFor(long playerId, @Nullable Item activeItem, Item targetItem, byte wantedType, boolean checkActive, boolean checkLiquids) {
/* 1405 */     for (Recipe recipe : recipesList) {
/*      */       
/* 1407 */       if (recipe.getTrigger() == wantedType)
/*      */       {
/* 1409 */         if (isRecipeOk(playerId, recipe, activeItem, targetItem, checkActive, checkLiquids) != 0)
/* 1410 */           return recipe; 
/*      */       }
/*      */     } 
/* 1413 */     return null;
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
/*      */   public static int isRecipeOk(long playerId, Recipe recipe, @Nullable Item activeItem, Item target, boolean checkActive, boolean checkLiquids) {
/* 1428 */     if (recipe.isRecipeOk(playerId, activeItem, target, checkActive, checkLiquids))
/* 1429 */       return 1; 
/* 1430 */     if (recipe.getTrigger() == 2 && activeItem != null && 
/* 1431 */       !activeItem.getTemplate().isCookingTool() && !target.getTemplate().isFoodMaker())
/*      */     {
/* 1433 */       if (recipe.isRecipeOk(playerId, target, activeItem, checkActive, checkLiquids))
/* 1434 */         return 2; 
/*      */     }
/* 1436 */     return 0;
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
/*      */   public static Recipe[] getPartialRecipeListFor(Creature performer, byte wantedType, Item target) {
/* 1449 */     Set<Recipe> recipes = new HashSet<>();
/* 1450 */     for (Recipe recipe : recipesList) {
/*      */       
/* 1452 */       if (recipe.getTrigger() == wantedType)
/*      */       {
/* 1454 */         if (recipe.isPartialMatch(target))
/*      */         {
/* 1456 */           recipes.add(recipe);
/*      */         }
/*      */       }
/*      */     } 
/* 1460 */     return recipes.<Recipe>toArray(new Recipe[recipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Recipe getRecipeByActionId(short id) {
/* 1471 */     return recipes.get(Short.valueOf(id));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Recipe getRecipeById(short id) {
/* 1482 */     return recipes.get(Short.valueOf((short)(id + 8000)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Recipe getRecipeByResult(Ingredient ingredient) {
/* 1494 */     for (Recipe recipe : recipesList) {
/*      */       
/* 1496 */       if (recipe.matchesResult(ingredient, true))
/* 1497 */         return recipe; 
/*      */     } 
/* 1499 */     for (Recipe recipe : recipesList) {
/*      */       
/* 1501 */       if (recipe.matchesResult(ingredient, false))
/* 1502 */         return recipe; 
/*      */     } 
/* 1504 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Recipe[] getRecipesByResult(Ingredient ingredient) {
/* 1516 */     LinkedList<Recipe> recipes = new LinkedList<>();
/*      */     
/* 1518 */     for (Recipe recipe : recipesList) {
/*      */       
/* 1520 */       if (recipe.matchesResult(ingredient, true)) {
/* 1521 */         recipes.add(recipe);
/*      */       }
/*      */     } 
/* 1524 */     if (recipes.size() == 0)
/*      */     {
/* 1526 */       for (Recipe recipe : recipesList) {
/*      */         
/* 1528 */         if (recipe.matchesResult(ingredient, false)) {
/* 1529 */           recipes.add(recipe);
/*      */         }
/*      */       } 
/*      */     }
/* 1533 */     Recipe[] recipesArr = recipes.<Recipe>toArray(new Recipe[recipes.size()]);
/* 1534 */     if (recipesArr.length > 1)
/*      */     {
/* 1536 */       Arrays.sort(recipesArr, new Comparator<Recipe>()
/*      */           {
/*      */             
/*      */             public int compare(Recipe param1, Recipe param2)
/*      */             {
/* 1541 */               return param1.getName().compareTo(param2.getName());
/*      */             }
/*      */           });
/*      */     }
/* 1545 */     return recipesArr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getIngredientName(@Nullable Ingredient ingredient) {
/* 1556 */     return getIngredientName(ingredient, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getIngredientName(@Nullable Ingredient ingredient, boolean withAmount) {
/* 1561 */     if (ingredient == null)
/* 1562 */       return ""; 
/* 1563 */     StringBuilder buf = new StringBuilder();
/* 1564 */     if (ingredient.hasCState()) {
/*      */       
/* 1566 */       buf.append(ingredient.getCStateName());
/* 1567 */       if (ingredient.hasPState())
/* 1568 */         buf.append(" " + ingredient.getPStateName()); 
/* 1569 */       buf.append(" ");
/*      */     }
/* 1571 */     else if (ingredient.hasPState()) {
/*      */       
/* 1573 */       buf.append(ingredient.getPStateName() + " ");
/*      */     } 
/* 1575 */     if (ingredient.hasCorpseData()) {
/*      */ 
/*      */       
/* 1578 */       buf.append(ingredient.getCorpseName() + " corpse");
/* 1579 */       return buf.toString();
/*      */     } 
/*      */     
/* 1582 */     if (ingredient.hasMaterial() || ingredient.hasRealTemplate() || ingredient.hasMaterialRef() || ingredient.hasRealTemplateRef()) {
/*      */       
/* 1584 */       Recipe[] recipes = getRecipesByResult(ingredient);
/* 1585 */       if (recipes.length > 0) {
/*      */ 
/*      */         
/* 1588 */         StringBuilder buf2 = new StringBuilder();
/* 1589 */         if (recipes.length == 1) {
/* 1590 */           buf2.append(recipes[0].getResultName(ingredient));
/*      */         } else {
/* 1592 */           buf2.append(ingredient.getName(withAmount));
/* 1593 */         }  return buf2.toString();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1602 */     return ingredient.getName(withAmount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isRecipeAction(short action) {
/* 1612 */     return (action >= 8000 && action < 10000);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Recipe> getKnownRecipes() {
/* 1621 */     Set<Recipe> knownRecipes = new HashSet<>();
/* 1622 */     for (Recipe recipe : recipesList) {
/*      */       
/* 1624 */       if (recipe.isKnown())
/*      */       {
/* 1626 */         knownRecipes.add(recipe);
/*      */       }
/*      */     } 
/* 1629 */     return knownRecipes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Recipe[] getUnknownRecipes() {
/* 1638 */     LinkedList<Recipe> unknownRecipes = new LinkedList<>();
/* 1639 */     for (Recipe recipe : recipesList) {
/*      */       
/* 1641 */       if (!recipe.isKnown())
/*      */       {
/* 1643 */         unknownRecipes.add(recipe);
/*      */       }
/*      */     } 
/* 1646 */     return unknownRecipes.<Recipe>toArray(new Recipe[unknownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Recipe[] getAllRecipes() {
/* 1655 */     return recipesList.<Recipe>toArray(new Recipe[recipesList.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isKnownRecipe(short recipeId) {
/* 1665 */     Recipe recipe = getRecipeById(recipeId);
/* 1666 */     if (recipe != null)
/* 1667 */       return recipe.isKnown(); 
/* 1668 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Recipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
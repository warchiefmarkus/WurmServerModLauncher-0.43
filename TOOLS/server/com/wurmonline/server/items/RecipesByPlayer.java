/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public final class RecipesByPlayer
/*      */   implements MiscConstants
/*      */ {
/*   52 */   private static final Logger logger = Logger.getLogger(RecipesByPlayer.class.getName());
/*      */   
/*   54 */   private static final Map<Long, RecipesByPlayer> playerRecipes = new ConcurrentHashMap<>();
/*      */   
/*      */   private static final String GET_ALL_PLAYER_RECIPES = "SELECT * FROM RECIPESPLAYER";
/*      */   
/*      */   private static final String GET_ALL_PLAYER_COOKERS = "SELECT * FROM RECIPEPLAYERCOOKERS";
/*      */   
/*      */   private static final String GET_ALL_PLAYER_CONTAINERS = "SELECT * FROM RECIPEPLAYERCONTAINERS";
/*      */   
/*      */   private static final String GET_ALL_PLAYER_INGREDIENTS = "SELECT * FROM RECIPEPLAYERINGREDIENTS";
/*      */   
/*      */   private static final String CREATE_PLAYER_RECIPE = "INSERT INTO RECIPESPLAYER (PLAYERID,RECIPEID,FAVOURITE,NOTES) VALUES(?,?,?,?)";
/*      */   private static final String CREATE_PLAYER_RECIPE_COOKER = "INSERT INTO RECIPEPLAYERCOOKERS (PLAYERID,RECIPEID,COOKERID) VALUES(?,?,?)";
/*      */   private static final String CREATE_PLAYER_RECIPE_CONTAINER = "INSERT INTO RECIPEPLAYERCONTAINERS (PLAYERID,RECIPEID,CONTAINERID) VALUES(?,?,?)";
/*      */   private static final String CREATE_PLAYER_RECIPE_INGREDIENT = "INSERT INTO RECIPEPLAYERINGREDIENTS (PLAYERID,RECIPEID,INGREDIENTID,GROUPID,TEMPLATEID,CSTATE,PSTATE,MATERIAL,REALTEMPLATEID) VALUES(?,?,?,?,?,?,?,?,?)";
/*      */   private static final String UPDATE_PLAYER_RECIPE_FAVOURITE = "UPDATE RECIPESPLAYER SET FAVOURITE=? WHERE PLAYERID=? AND RECIPEID=?";
/*      */   private static final String UPDATE_PLAYER_RECIPE_NOTES = "UPDATE RECIPESPLAYER SET NOTES=? WHERE PLAYERID=? AND RECIPEID=?";
/*      */   private static final String UPDATE_PLAYER_RECIPE_INGREDIENT = "UPDATE RECIPEPLAYERINGREDIENTS SET TEMPLATEID=?,CSTATE=?,PSTATE=?,MATERIAL=?,REALTEMPLATEID=? WHERE PLAYERID=? AND RECIPEID=? AND INGREDIENTID=?";
/*      */   private static final String DELETE_PLAYER_RECIPES = "DELETE FROM RECIPESPLAYER WHERE PLAYERID=?";
/*      */   private static final String DELETE_PLAYER_RECIPES_COOKERS = "DELETE FROM RECIPEPLAYERCOOKERS WHERE PLAYERID=?";
/*      */   private static final String DELETE_PLAYER_RECIPES_CONTAINERS = "DELETE FROM RECIPEPLAYERCONTAINERS WHERE PLAYERID=?";
/*      */   private static final String DELETE_PLAYER_RECIPES_INGREDIENTS = "DELETE FROM RECIPEPLAYERINGREDIENTS WHERE PLAYERID=?";
/*      */   private static final String DELETE_PLAYER_RECIPE = "DELETE FROM RECIPESPLAYER WHERE PLAYERID=? AND RECIPEID=?";
/*      */   private static final String DELETE_PLAYER_RECIPE_COOKERS = "DELETE FROM RECIPEPLAYERCOOKERS WHERE PLAYERID=? AND RECIPEID=?";
/*      */   private static final String DELETE_PLAYER_RECIPE_CONTAINERS = "DELETE FROM RECIPEPLAYERCONTAINERS WHERE PLAYERID=? AND RECIPEID=?";
/*      */   private static final String DELETE_PLAYER_RECIPE_INGREDIENTS = "DELETE FROM RECIPEPLAYERINGREDIENTS WHERE PLAYERID=? AND RECIPEID=?";
/*      */   private static final String DELETE_ALL_PLAYER_RECIPES = "DELETE FROM RECIPESPLAYER";
/*      */   private static final String DELETE_ALL_PLAYER_RECIPE_COOKERS = "DELETE FROM RECIPEPLAYERCOOKERS";
/*      */   private static final String DELETE_ALL_PLAYER_RECIPE_CONTAINERS = "DELETE FROM RECIPEPLAYERCONTAINERS";
/*      */   private static final String DELETE_ALL_PLAYER_RECIPE_INGREDIENTS = "DELETE FROM RECIPEPLAYERINGREDIENTS";
/*      */   private final long wurmId;
/*   84 */   private final Map<Short, Recipe> knownRecipes = new ConcurrentHashMap<>();
/*   85 */   private final Map<Short, Boolean> playerFavourites = new ConcurrentHashMap<>();
/*   86 */   private final Map<Short, String> playerNotes = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*      */   public RecipesByPlayer(long playerId) {
/*   90 */     this.wurmId = playerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getPlayerId() {
/*   99 */     return this.wurmId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addRecipe(Recipe recipe) {
/*  108 */     this.knownRecipes.put(Short.valueOf(recipe.getRecipeId()), recipe);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Recipe getRecipe(short recipeId) {
/*  119 */     return this.knownRecipes.get(Short.valueOf(recipeId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isKnownRecipe(short recipeId) {
/*  129 */     return this.knownRecipes.containsKey(Short.valueOf(recipeId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeRecipe(short recipeId) {
/*  138 */     this.knownRecipes.remove(Short.valueOf(recipeId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean setFavourite(short recipeId, boolean isFavourite) {
/*  149 */     Boolean wasFavourite = this.playerFavourites.put(Short.valueOf(recipeId), Boolean.valueOf(isFavourite));
/*      */     
/*  151 */     if (!this.playerNotes.containsKey(Short.valueOf(recipeId))) {
/*  152 */       this.playerNotes.put(Short.valueOf(recipeId), "");
/*      */     }
/*  154 */     return (wasFavourite == null || wasFavourite.booleanValue() != isFavourite);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean setNotes(short recipeId, String notes) {
/*  165 */     String newNotes = notes.substring(0, Math.min(notes.length(), 200));
/*  166 */     String oldNotes = this.playerNotes.put(Short.valueOf(recipeId), newNotes);
/*      */     
/*  168 */     if (!this.playerFavourites.containsKey(Short.valueOf(recipeId))) {
/*  169 */       this.playerFavourites.put(Short.valueOf(recipeId), Boolean.valueOf(false));
/*      */     }
/*  171 */     return (oldNotes == null || !oldNotes.equals(newNotes));
/*      */   }
/*      */ 
/*      */   
/*      */   boolean isFavourite(short recipeId) {
/*  176 */     Boolean isFavourite = this.playerFavourites.get(Short.valueOf(recipeId));
/*  177 */     return (isFavourite != null && isFavourite.booleanValue());
/*      */   }
/*      */ 
/*      */   
/*      */   String getNotes(short recipeId) {
/*  182 */     String notes = this.playerNotes.get(Short.valueOf(recipeId));
/*  183 */     if (notes != null)
/*  184 */       return notes.substring(0, Math.min(notes.length(), 200)); 
/*  185 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int loadAllPlayerKnownRecipes() {
/*  193 */     int count = 0;
/*  194 */     Connection dbcon = null;
/*  195 */     PreparedStatement ps = null;
/*  196 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  199 */       dbcon = DbConnector.getItemDbCon();
/*  200 */       ps = dbcon.prepareStatement("SELECT * FROM RECIPESPLAYER");
/*  201 */       rs = ps.executeQuery();
/*  202 */       while (rs.next())
/*      */       {
/*  204 */         long playerId = rs.getLong("PLAYERID");
/*  205 */         short recipeId = rs.getShort("RECIPEID");
/*  206 */         boolean favourite = rs.getBoolean("FAVOURITE");
/*  207 */         String notes = rs.getString("NOTES");
/*      */         
/*  209 */         Recipe templateRecipe = Recipes.getRecipeById(recipeId);
/*  210 */         if (templateRecipe != null) {
/*      */ 
/*      */ 
/*      */           
/*  214 */           RecipesByPlayer rbp = getRecipesByPlayer(playerId, true);
/*  215 */           rbp.setFavourite(recipeId, favourite);
/*  216 */           rbp.setNotes(recipeId, notes);
/*      */           
/*  218 */           if (!templateRecipe.isKnown()) {
/*      */             
/*  220 */             count++;
/*      */             
/*  222 */             Recipe playerRecipe = new Recipe(recipeId);
/*  223 */             rbp.addRecipe(playerRecipe);
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*  228 */         logger.log(Level.WARNING, "Known recipe is not found in templates " + recipeId + " for player " + playerId);
/*      */       }
/*      */     
/*      */     }
/*  232 */     catch (SQLException sqex) {
/*      */       
/*  234 */       logger.log(Level.WARNING, "Failed to load all player known recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  238 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  245 */       assert dbcon != null;
/*  246 */       ps = dbcon.prepareStatement("SELECT * FROM RECIPEPLAYERCOOKERS");
/*  247 */       rs = ps.executeQuery();
/*  248 */       while (rs.next()) {
/*      */         
/*  250 */         long playerId = rs.getLong("PLAYERID");
/*  251 */         short recipeId = rs.getShort("RECIPEID");
/*  252 */         short cookerId = rs.getShort("COOKERID");
/*      */         
/*  254 */         RecipesByPlayer rbp = getRecipesByPlayer(playerId, false);
/*  255 */         if (rbp != null)
/*      */         {
/*  257 */           Recipe recipe = rbp.getRecipe(recipeId);
/*      */           
/*  259 */           if (recipe != null)
/*      */           {
/*  261 */             recipe.addToCookerList(cookerId);
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*  266 */     } catch (SQLException sqex) {
/*      */       
/*  268 */       logger.log(Level.WARNING, "Failed to load all player known recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  272 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  279 */       ps = dbcon.prepareStatement("SELECT * FROM RECIPEPLAYERCONTAINERS");
/*  280 */       rs = ps.executeQuery();
/*  281 */       while (rs.next()) {
/*      */         
/*  283 */         long playerId = rs.getLong("PLAYERID");
/*  284 */         short recipeId = rs.getShort("RECIPEID");
/*  285 */         short containerId = rs.getShort("CONTAINERID");
/*      */         
/*  287 */         RecipesByPlayer rbp = getRecipesByPlayer(playerId, false);
/*  288 */         if (rbp != null)
/*      */         {
/*  290 */           Recipe recipe = rbp.getRecipe(recipeId);
/*      */           
/*  292 */           if (recipe != null)
/*      */           {
/*  294 */             recipe.addToContainerList(containerId);
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*  299 */     } catch (SQLException sqex) {
/*      */       
/*  301 */       logger.log(Level.WARNING, "Failed to load all player known recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  305 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  312 */       ps = dbcon.prepareStatement("SELECT * FROM RECIPEPLAYERINGREDIENTS");
/*  313 */       rs = ps.executeQuery();
/*  314 */       while (rs.next())
/*      */       {
/*  316 */         long playerId = rs.getLong("PLAYERID");
/*  317 */         short recipeId = rs.getShort("RECIPEID");
/*  318 */         byte ingredientId = rs.getByte("INGREDIENTID");
/*  319 */         byte groupId = rs.getByte("GROUPID");
/*  320 */         short templateId = rs.getShort("TEMPLATEID");
/*  321 */         byte cstate = rs.getByte("CSTATE");
/*  322 */         byte pstate = rs.getByte("PSTATE");
/*  323 */         byte material = rs.getByte("MATERIAL");
/*  324 */         short realTemplateId = rs.getShort("REALTEMPLATEID");
/*      */         
/*  326 */         RecipesByPlayer rbp = getRecipesByPlayer(playerId, false);
/*  327 */         if (rbp != null) {
/*      */ 
/*      */           
/*      */           try {
/*  331 */             Recipe recipe = rbp.getRecipe(recipeId);
/*      */             
/*  333 */             if (recipe != null) {
/*      */               
/*  335 */               Recipe refRecipe = Recipes.getRecipeById(recipeId);
/*  336 */               assert refRecipe != null;
/*  337 */               Ingredient refingredient = refRecipe.getIngredientById(ingredientId);
/*      */               
/*  339 */               Ingredient ingredient = makeIngredient(templateId, cstate, pstate, material, refingredient
/*  340 */                   .hasRealTemplate(), realTemplateId, groupId);
/*  341 */               if (ingredient != null) {
/*      */                 
/*  343 */                 ingredient.setAmount(refingredient.getAmount());
/*  344 */                 ingredient.setRatio(refingredient.getRatio());
/*  345 */                 ingredient.setLoss(refingredient.getLoss());
/*  346 */                 ingredient.setIngredientId(ingredientId);
/*  347 */                 recipe.addIngredient(ingredient);
/*      */                 
/*      */                 continue;
/*      */               } 
/*  351 */               logger.log(Level.WARNING, "Failed to find template for " + templateId + " or " + realTemplateId + ".");
/*      */ 
/*      */               
/*      */               continue;
/*      */             } 
/*      */             
/*  357 */             logger.log(Level.WARNING, "Failed to find player recipe " + recipeId + ".");
/*      */           
/*      */           }
/*  360 */           catch (Exception e) {
/*      */             
/*  362 */             logger.log(Level.WARNING, "Failed to load player recipe " + recipeId + ", so deleted entry on db.");
/*  363 */             dbRemovePlayerRecipe(playerId, recipeId);
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*  368 */         logger.log(Level.WARNING, "Failed to find player recipe list, so deleted entry on db.");
/*  369 */         dbRemovePlayerRecipe(playerId, recipeId);
/*      */       }
/*      */     
/*      */     }
/*  373 */     catch (SQLException sqex) {
/*      */       
/*  375 */       logger.log(Level.WARNING, "Failed to load all player known recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  379 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  380 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  382 */     return count;
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
/*      */   private static Ingredient makeIngredient(short templateId, byte cstate, byte pstate, byte material, boolean hasRealTemplate, short realTemplateId, int groupId) {
/*      */     try {
/*  401 */       ItemTemplate itemTemplate = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  402 */       Ingredient ingredient = new Ingredient(itemTemplate, false, (byte)groupId);
/*  403 */       ingredient.setCState(cstate);
/*  404 */       ingredient.setPState(pstate);
/*  405 */       ingredient.setMaterial(material);
/*  406 */       if (templateId == 272) {
/*      */ 
/*      */         
/*  409 */         ingredient.setCorpseData(realTemplateId);
/*      */       }
/*  411 */       else if (realTemplateId > -1) {
/*      */         
/*  413 */         ItemTemplate realItemTemplate = ItemTemplateFactory.getInstance().getTemplate(realTemplateId);
/*  414 */         ingredient.setRealTemplate(realItemTemplate);
/*      */       }
/*  416 */       else if (hasRealTemplate) {
/*  417 */         ingredient.setRealTemplate(null);
/*  418 */       }  return ingredient;
/*      */     }
/*  420 */     catch (NoSuchTemplateException e) {
/*      */       
/*  422 */       return null;
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
/*      */   static final RecipesByPlayer getRecipesByPlayer(long playerId, boolean autoCreate) {
/*  434 */     RecipesByPlayer rbp = playerRecipes.get(Long.valueOf(playerId));
/*  435 */     if (rbp == null && autoCreate) {
/*      */       
/*  437 */       rbp = new RecipesByPlayer(playerId);
/*  438 */       playerRecipes.put(Long.valueOf(playerId), rbp);
/*      */     } 
/*  440 */     return rbp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Set<Recipe> getKnownRecipesSetFor(long playerId) {
/*  451 */     Set<Recipe> recipes = Recipes.getKnownRecipes();
/*  452 */     RecipesByPlayer rbp = playerRecipes.get(Long.valueOf(playerId));
/*  453 */     if (rbp != null)
/*      */     {
/*  455 */       recipes.addAll(rbp.knownRecipes.values());
/*      */     }
/*  457 */     return recipes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isKnownRecipe(long playerId, short recipeId) {
/*  468 */     if (Recipes.isKnownRecipe(recipeId))
/*  469 */       return true; 
/*  470 */     RecipesByPlayer rbp = playerRecipes.get(Long.valueOf(playerId));
/*  471 */     return (rbp != null && rbp.knownRecipes.containsKey(Short.valueOf(recipeId)));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFavourite(long playerId, short recipeId) {
/*  476 */     RecipesByPlayer rbp = playerRecipes.get(Long.valueOf(playerId));
/*  477 */     return (rbp != null && rbp.isFavourite(recipeId));
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getNotes(long playerId, short recipeId) {
/*  482 */     RecipesByPlayer rbp = playerRecipes.get(Long.valueOf(playerId));
/*  483 */     if (rbp != null)
/*      */     {
/*  485 */       return rbp.getNotes(recipeId);
/*      */     }
/*  487 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Recipe getPlayerKnownRecipeOrNull(long playerId, short recipeId) {
/*  498 */     RecipesByPlayer rbp = playerRecipes.get(Long.valueOf(playerId));
/*  499 */     if (rbp != null)
/*  500 */       return rbp.knownRecipes.get(Short.valueOf(recipeId)); 
/*  501 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getTargetActionRecipesFor(long playerId) {
/*  512 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  513 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  515 */     for (Recipe recipe : recipes) {
/*      */       
/*  517 */       if (recipe.isTargetActionType())
/*  518 */         knownRecipes.add(recipe); 
/*      */     } 
/*  520 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getContainerActionRecipesFor(long playerId) {
/*  531 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  532 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  534 */     for (Recipe recipe : recipes) {
/*      */       
/*  536 */       if (recipe.isContainerActionType())
/*  537 */         knownRecipes.add(recipe); 
/*      */     } 
/*  539 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getHeatRecipesFor(long playerId) {
/*  550 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  551 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  553 */     for (Recipe recipe : recipes) {
/*      */       
/*  555 */       if (recipe.isHeatType())
/*  556 */         knownRecipes.add(recipe); 
/*      */     } 
/*  558 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getTimeRecipesFor(long playerId) {
/*  569 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  570 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  572 */     for (Recipe recipe : recipes) {
/*      */       
/*  574 */       if (recipe.isTimeType())
/*  575 */         knownRecipes.add(recipe); 
/*      */     } 
/*  577 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final ItemTemplate[] getKnownCookersFor(long playerId) {
/*  588 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  589 */     Set<ItemTemplate> knownCookers = new HashSet<>();
/*      */     
/*  591 */     for (Recipe recipe : recipes) {
/*      */       
/*  593 */       if (recipe.hasCooker())
/*      */       {
/*  595 */         knownCookers.addAll(recipe.getCookerTemplates());
/*      */       }
/*      */     } 
/*  598 */     return knownCookers.<ItemTemplate>toArray(new ItemTemplate[knownCookers.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getCookerRecipesFor(long playerId, int cookerId) {
/*  609 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  610 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  612 */     for (Recipe recipe : recipes) {
/*      */       
/*  614 */       if (recipe.hasCooker(cookerId))
/*  615 */         knownRecipes.add(recipe); 
/*      */     } 
/*  617 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final ItemTemplate[] getKnownContainersFor(long playerId) {
/*  628 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  629 */     Set<ItemTemplate> knownContainers = new HashSet<>();
/*      */     
/*  631 */     for (Recipe recipe : recipes) {
/*      */       
/*  633 */       if (recipe.hasContainer())
/*      */       {
/*  635 */         knownContainers.addAll(recipe.getContainerTemplates());
/*      */       }
/*      */     } 
/*  638 */     return knownContainers.<ItemTemplate>toArray(new ItemTemplate[knownContainers.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getContainerRecipesFor(long playerId, int containerId) {
/*  649 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  650 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  652 */     for (Recipe recipe : recipes) {
/*      */       
/*  654 */       if (recipe.hasContainer(containerId))
/*  655 */         knownRecipes.add(recipe); 
/*      */     } 
/*  657 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final ItemTemplate[] getKnownToolsFor(long playerId) {
/*  668 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  669 */     Set<ItemTemplate> knownTools = new HashSet<>();
/*      */     
/*  671 */     for (Recipe recipe : recipes) {
/*      */       
/*  673 */       if (recipe.getActiveItem() != null && recipe.getActiveItem().getTemplate().isCookingTool())
/*      */       {
/*  675 */         knownTools.add(recipe.getActiveItem().getTemplate());
/*      */       }
/*      */     } 
/*  678 */     return knownTools.<ItemTemplate>toArray(new ItemTemplate[knownTools.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getToolRecipesFor(long playerId, int toolId) {
/*  689 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  690 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  692 */     for (Recipe recipe : recipes) {
/*      */ 
/*      */ 
/*      */       
/*  696 */       if (recipe.getActiveItem() != null && recipe.getActiveItem().getTemplateId() == toolId)
/*  697 */         knownRecipes.add(recipe); 
/*      */     } 
/*  699 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Ingredient[] getKnownIngredientsFor(long playerId) {
/*  709 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  710 */     Map<String, Ingredient> knownIngredients = new HashMap<>();
/*      */     
/*  712 */     for (Recipe recipe : recipes) {
/*      */       
/*  714 */       for (Ingredient i : recipe.getAllIngredients(true).values()) {
/*      */         
/*  716 */         Ingredient ingredient = i.clone(null);
/*  717 */         knownIngredients.put(ingredient.getName(true), ingredient);
/*      */       } 
/*      */     } 
/*      */     
/*  721 */     return (Ingredient[])knownIngredients.values().toArray((Object[])new Ingredient[knownIngredients.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Ingredient[] getRecipeIngredientsFor(long playerId, int recipeId) {
/*  732 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  733 */     Map<String, Ingredient> knownIngredients = new HashMap<>();
/*      */     
/*  735 */     for (Recipe recipe : recipes) {
/*      */       
/*  737 */       if (recipe.getRecipeId() == recipeId) {
/*      */         
/*  739 */         knownIngredients.putAll(recipe.getAllIngredients(true));
/*      */         break;
/*      */       } 
/*      */     } 
/*  743 */     return (Ingredient[])knownIngredients.values().toArray((Object[])new Ingredient[knownIngredients.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getIngredientRecipesFor(long playerId, Ingredient ingredient) {
/*  754 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  755 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  757 */     for (Recipe recipe : recipes) {
/*      */       
/*  759 */       Map<String, Ingredient> recipeIngredients = recipe.getAllIngredients(true);
/*  760 */       for (Ingredient i : recipeIngredients.values()) {
/*      */         
/*  762 */         if (i.getName(false).equalsIgnoreCase(ingredient.getName(false)))
/*      */         {
/*  764 */           knownRecipes.add(recipe);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  769 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getSearchRecipesFor(long playerId, String searchFor) {
/*  780 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  781 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  783 */     for (Recipe recipe : recipes) {
/*      */       
/*  785 */       if (recipe.getName().toLowerCase().contains(searchFor.toLowerCase()))
/*  786 */         knownRecipes.add(recipe); 
/*      */     } 
/*  788 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe[] getKnownRecipesFor(long playerId) {
/*  799 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  800 */     Set<Recipe> knownRecipes = new HashSet<>();
/*      */     
/*  802 */     for (Recipe recipe : recipes)
/*      */     {
/*  804 */       knownRecipes.add(recipe);
/*      */     }
/*  806 */     return knownRecipes.<Recipe>toArray(new Recipe[knownRecipes.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Recipe getRecipe(long playerId, int recipeId) {
/*  817 */     Set<Recipe> recipes = getKnownRecipesSetFor(playerId);
/*  818 */     for (Recipe recipe : recipes) {
/*      */       
/*  820 */       if (recipe.getRecipeId() == recipeId)
/*  821 */         return recipe; 
/*      */     } 
/*  823 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packRecipes(DataOutputStream dos, long playerId) throws IOException {
/*  834 */     Set<Recipe> recipes = new HashSet<>();
/*  835 */     RecipesByPlayer rbp = playerRecipes.get(Long.valueOf(playerId));
/*  836 */     if (rbp != null)
/*      */     {
/*  838 */       recipes.addAll(rbp.knownRecipes.values());
/*      */     }
/*  840 */     dos.writeChar(88);
/*  841 */     dos.writeShort(recipes.size());
/*  842 */     logger.log(Level.INFO, "packing " + recipes.size() + " known recipes!");
/*  843 */     for (Recipe recipe : recipes) {
/*      */       
/*  845 */       dos.writeChar(82);
/*  846 */       recipe.pack(dos);
/*      */     } 
/*      */     
/*  849 */     if (rbp != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  855 */       int count = 0;
/*  856 */       for (Boolean b : rbp.playerFavourites.values()) {
/*      */         
/*  858 */         if (b.booleanValue()) {
/*  859 */           count++;
/*      */         }
/*      */       } 
/*  862 */       logger.log(Level.INFO, "packing " + count + " favourites!");
/*  863 */       dos.writeShort(count);
/*      */       
/*  865 */       for (Map.Entry<Short, Boolean> entry : rbp.playerFavourites.entrySet()) {
/*      */         
/*  867 */         if (((Boolean)entry.getValue()).booleanValue())
/*      */         {
/*  869 */           dos.writeShort(((Short)entry.getKey()).shortValue());
/*      */         }
/*      */       } 
/*      */       
/*  873 */       count = 0;
/*  874 */       for (String n : rbp.playerNotes.values()) {
/*      */         
/*  876 */         if (n.length() > 0) {
/*  877 */           count++;
/*      */         }
/*      */       } 
/*  880 */       logger.log(Level.INFO, "packing " + count + " notes!");
/*  881 */       dos.writeShort(count);
/*      */       
/*  883 */       for (Map.Entry<Short, String> entry : rbp.playerNotes.entrySet()) {
/*      */         
/*  885 */         if (((String)entry.getValue()).length() > 0)
/*      */         {
/*  887 */           dos.writeShort(((Short)entry.getKey()).shortValue());
/*  888 */           byte[] notesAsBytes = ((String)entry.getValue()).getBytes("UTF-8");
/*  889 */           dos.writeByte((byte)notesAsBytes.length);
/*  890 */           dos.write(notesAsBytes);
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/*  896 */       dos.writeShort(0);
/*  897 */       dos.writeShort(0);
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
/*      */   public static void unPackRecipes(DataInputStream dis, long playerId) throws IOException {
/*  910 */     deleteRecipesForPlayer(playerId);
/*      */     
/*  912 */     if (dis.readChar() != 'X')
/*      */     {
/*  914 */       throw new IOException(new Exception("unpacking error, no start recipe list 'X' char"));
/*      */     }
/*  916 */     int count = dis.readShort();
/*  917 */     logger.log(Level.INFO, "unpacking " + count + " known recipes!");
/*  918 */     if (count > 0) {
/*      */ 
/*      */       
/*  921 */       RecipesByPlayer rbp = getRecipesByPlayer(playerId, true);
/*  922 */       for (int x = 0; x < count; x++) {
/*      */         
/*  924 */         if (dis.readChar() != 'R')
/*      */         {
/*  926 */           throw new IOException(new Exception("unpacking error, no start recipe 'R' char for recipe " + x + " out of " + count + "."));
/*      */         }
/*      */ 
/*      */         
/*      */         try {
/*  931 */           Recipe recipe = new Recipe(dis);
/*  932 */           addRecipe(rbp, recipe);
/*      */         }
/*  934 */         catch (NoSuchTemplateException e) {
/*      */           
/*  936 */           logger.log(Level.INFO, "unpacking fail: " + e.getMessage(), (Throwable)e);
/*      */           
/*  938 */           throw new IOException(e.getMessage());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  943 */     count = dis.readShort();
/*  944 */     logger.log(Level.INFO, "unpacking " + count + " favourites!");
/*  945 */     if (count > 0)
/*      */     {
/*  947 */       for (int x = 0; x < count; x++) {
/*      */         
/*  949 */         short recipeId = dis.readShort();
/*  950 */         setIsFavourite(playerId, recipeId, true);
/*      */       } 
/*      */     }
/*      */     
/*  954 */     count = dis.readShort();
/*  955 */     logger.log(Level.INFO, "unpacking " + count + " notes!");
/*  956 */     if (count > 0)
/*      */     {
/*  958 */       for (int x = 0; x < count; x++) {
/*      */         
/*  960 */         short recipeId = dis.readShort();
/*  961 */         byte lByte = dis.readByte();
/*  962 */         int length = lByte & 0xFF;
/*  963 */         byte[] tempStringArr = new byte[length];
/*  964 */         int read = dis.read(tempStringArr);
/*  965 */         if (length != read)
/*      */         {
/*  967 */           logger.warning("Read in " + read + ", expected " + length);
/*      */         }
/*  969 */         String notes = new String(tempStringArr, "UTF-8");
/*  970 */         setNotes(playerId, recipeId, notes);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean saveRecipe(@Nullable Creature performer, Recipe templateRecipe, long playerId, @Nullable Item source, Item target) {
/*  988 */     if (templateRecipe.isKnown())
/*  989 */       return false; 
/*  990 */     if (playerId == -10L) {
/*      */       
/*  992 */       logger.log(Level.WARNING, "Failed to save recipe '" + templateRecipe.getName() + "' (#" + templateRecipe.getRecipeId() + "): No player ID given");
/*  993 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  997 */     if (performer != null)
/*  998 */       Recipes.setRecipeNamer(templateRecipe, performer); 
/*  999 */     boolean isChanged = false;
/* 1000 */     RecipesByPlayer rbp = getRecipesByPlayer(playerId, true);
/* 1001 */     Recipe playerRecipe = rbp.getRecipe(templateRecipe.getRecipeId());
/* 1002 */     if (playerRecipe != null) {
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
/* 1018 */       if (templateRecipe.hasCooker()) {
/*      */         
/* 1020 */         Item cooker = target.getTopParentOrNull();
/*      */         
/* 1022 */         if (cooker != null)
/*      */         {
/* 1024 */           if (!playerRecipe.hasCooker(cooker.getTemplateId())) {
/*      */             
/* 1026 */             playerRecipe.addToCookerList(cooker.getTemplateId());
/* 1027 */             dbSaveRecipeCooker(playerId, playerRecipe.getRecipeId(), cooker.getTemplateId());
/* 1028 */             isChanged = true;
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 1033 */       if (templateRecipe.hasContainer())
/*      */       {
/*      */         
/* 1036 */         if (target != null)
/*      */         {
/* 1038 */           if (!playerRecipe.hasContainer(target.getTemplateId())) {
/*      */             
/* 1040 */             playerRecipe.addToContainerList(target.getTemplateId());
/* 1041 */             dbSaveRecipeContainer(playerId, playerRecipe.getRecipeId(), target.getTemplateId());
/* 1042 */             isChanged = true;
/*      */           } 
/*      */         }
/*      */       }
/* 1046 */       if ((((source != null) ? 1 : 0) & ((templateRecipe.getActiveItem() != null) ? 1 : 0)) != 0 && playerRecipe.getActiveItem() != null)
/*      */       {
/* 1048 */         if (templateRecipe.getActiveItem().isFoodGroup() && !playerRecipe.getActiveItem().isFoodGroup() && playerRecipe
/* 1049 */           .getActiveItem().getTemplateId() != source.getTemplateId()) {
/*      */ 
/*      */           
/* 1052 */           Ingredient pi = playerRecipe.getIngredientById(templateRecipe.getActiveItem().getIngredientId());
/* 1053 */           pi.setTemplate(templateRecipe.getActiveItem().getTemplate());
/* 1054 */           dbSaveRecipeIngredient(true, playerId, playerRecipe.getRecipeId(), pi);
/* 1055 */           playerRecipe.addIngredient(pi);
/* 1056 */           isChanged = true;
/*      */         } 
/*      */       }
/* 1059 */       if (templateRecipe.getTargetItem() != null && playerRecipe.getTargetItem() != null)
/*      */       {
/* 1061 */         if (templateRecipe.getTargetItem().isFoodGroup() && !playerRecipe.getTargetItem().isFoodGroup() && playerRecipe
/* 1062 */           .getTargetItem().getTemplateId() != target.getTemplateId()) {
/*      */ 
/*      */           
/* 1065 */           Ingredient pi = playerRecipe.getIngredientById(templateRecipe.getTargetItem().getIngredientId());
/* 1066 */           pi.setTemplate(templateRecipe.getTargetItem().getTemplate());
/* 1067 */           dbSaveRecipeIngredient(true, playerId, playerRecipe.getRecipeId(), pi);
/* 1068 */           playerRecipe.addIngredient(pi);
/* 1069 */           isChanged = true;
/*      */         } 
/*      */       }
/* 1072 */       if (target.isFoodMaker() || target.getTemplate().isCooker())
/*      */       {
/*      */         
/* 1075 */         for (Item item : target.getItemsAsArray()) {
/*      */           
/* 1077 */           Ingredient ti = templateRecipe.findMatchingIngredient(item);
/* 1078 */           if (ti == null) {
/*      */             
/* 1080 */             logger.log(Level.WARNING, "Failed to find matching ingredient:" + item.getName() + ".");
/*      */           }
/*      */           else {
/*      */             
/* 1084 */             Ingredient pi = playerRecipe.getIngredientById(ti.getIngredientId());
/* 1085 */             if (pi == null) {
/*      */               
/* 1087 */               Ingredient ingredient = ti.clone(item);
/* 1088 */               ingredient.setTemplate(item.getTemplate());
/* 1089 */               dbSaveRecipeIngredient(false, playerId, playerRecipe.getRecipeId(), ingredient);
/* 1090 */               playerRecipe.addIngredient(ingredient);
/* 1091 */               isChanged = true;
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 1096 */             else if (ti.isFoodGroup() && !pi.isFoodGroup()) {
/*      */ 
/*      */               
/* 1099 */               pi.setTemplate(ti.getTemplate());
/* 1100 */               dbSaveRecipeIngredient(true, playerId, playerRecipe.getRecipeId(), pi);
/* 1101 */               playerRecipe.addIngredient(pi);
/* 1102 */               isChanged = true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 1107 */       if (isChanged && performer != null)
/*      */       {
/*      */         
/* 1110 */         performer.getCommunicator().sendCookbookRecipe(playerRecipe);
/*      */       }
/* 1112 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1117 */     playerRecipe = new Recipe(templateRecipe.getRecipeId());
/* 1118 */     dbSaveRecipe(playerId, playerRecipe.getRecipeId(), false, "");
/*      */     
/* 1120 */     if (templateRecipe.hasCooker()) {
/*      */       
/* 1122 */       Item cooker = target.getTopParentOrNull();
/*      */       
/* 1124 */       if (cooker != null) {
/*      */         
/* 1126 */         playerRecipe.addToCookerList((short)cooker.getTemplateId());
/* 1127 */         dbSaveRecipeCooker(playerId, playerRecipe.getRecipeId(), cooker.getTemplateId());
/*      */       } 
/*      */     } 
/*      */     
/* 1131 */     if (templateRecipe.hasContainer()) {
/*      */       
/* 1133 */       playerRecipe.addToContainerList((short)target.getTemplateId());
/* 1134 */       dbSaveRecipeContainer(playerId, playerRecipe.getRecipeId(), target.getTemplateId());
/*      */     } 
/* 1136 */     if (templateRecipe.getActiveItem() != null && source != null) {
/*      */       Ingredient pi;
/*      */ 
/*      */       
/* 1140 */       if (templateRecipe.getActiveItem().getTemplateId() == 14) {
/* 1141 */         pi = new Ingredient(templateRecipe.getActiveItem().getTemplate(), false, (byte)-2);
/*      */       } else {
/* 1143 */         pi = new Ingredient(source.getTemplate(), false, (byte)-2);
/* 1144 */       }  if (templateRecipe.getActiveItem().hasMaterial())
/* 1145 */         pi.setMaterial(source.getMaterial()); 
/* 1146 */       if (templateRecipe.getActiveItem().hasCState())
/* 1147 */         pi.setCState(source.getRightAuxData()); 
/* 1148 */       if (templateRecipe.getActiveItem().hasPState())
/* 1149 */         pi.setPState((byte)(source.getLeftAuxData() * 16)); 
/* 1150 */       if (templateRecipe.getActiveItem().hasRealTemplate())
/* 1151 */         pi.setRealTemplate(source.getRealTemplate()); 
/* 1152 */       pi.setIngredientId(templateRecipe.getActiveItem().getIngredientId());
/* 1153 */       dbSaveRecipeIngredient(false, playerId, playerRecipe.getRecipeId(), pi);
/* 1154 */       playerRecipe.addIngredient(pi);
/*      */     } 
/* 1156 */     if (templateRecipe.getTargetItem() != null) {
/*      */       
/* 1158 */       Ingredient pi = new Ingredient(target.getTemplate(), false, (byte)-1);
/* 1159 */       if (templateRecipe.getTargetItem().hasMaterial())
/* 1160 */         pi.setMaterial(target.getMaterial()); 
/* 1161 */       if (templateRecipe.getTargetItem().hasCState())
/* 1162 */         pi.setCState(target.getRightAuxData()); 
/* 1163 */       if (templateRecipe.getTargetItem().hasPState())
/* 1164 */         pi.setPState((byte)(target.getLeftAuxData() * 16)); 
/* 1165 */       if (templateRecipe.getTargetItem().hasRealTemplate())
/* 1166 */         pi.setRealTemplate(target.getRealTemplate()); 
/* 1167 */       if (templateRecipe.getTargetItem().hasCorpseData())
/* 1168 */         pi.setCorpseData(templateRecipe.getTargetItem().getCorpseData()); 
/* 1169 */       pi.setIngredientId(templateRecipe.getTargetItem().getIngredientId());
/* 1170 */       dbSaveRecipeIngredient(false, playerId, playerRecipe.getRecipeId(), pi);
/* 1171 */       playerRecipe.addIngredient(pi);
/*      */     } 
/* 1173 */     if (target.isFoodMaker() || target.getTemplate().isCooker()) {
/*      */ 
/*      */       
/* 1176 */       templateRecipe.clearFound();
/* 1177 */       for (Item item : target.getItemsAsArray()) {
/*      */         
/* 1179 */         Ingredient ti = templateRecipe.findMatchingIngredient(item);
/* 1180 */         if (ti == null) {
/*      */           
/* 1182 */           logger.log(Level.WARNING, "Failed to find matching ingredient:" + item.getName() + ".");
/*      */ 
/*      */         
/*      */         }
/* 1186 */         else if (!ti.wasFound(true, false)) {
/*      */           
/* 1188 */           ti.setFound(true);
/* 1189 */           Ingredient pi = ti.clone(item);
/* 1190 */           pi.setTemplate(item.getTemplate());
/* 1191 */           dbSaveRecipeIngredient(false, playerId, playerRecipe.getRecipeId(), pi);
/* 1192 */           playerRecipe.addIngredient(pi);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1196 */     rbp.addRecipe(playerRecipe);
/* 1197 */     if (performer != null)
/*      */     {
/*      */       
/* 1200 */       performer.getCommunicator().sendCookbookRecipe(playerRecipe);
/*      */     }
/* 1202 */     return true;
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
/*      */   public static boolean addRecipe(Creature performer, Recipe recipe) {
/* 1217 */     RecipesByPlayer rbp = getRecipesByPlayer(performer.getWurmId(), true);
/* 1218 */     if (addRecipe(rbp, recipe))
/* 1219 */       return true; 
/* 1220 */     performer.getCommunicator().sendNormalServerMessage("That recipe is already in your cookbook!");
/* 1221 */     return false;
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
/*      */   private static boolean addRecipe(RecipesByPlayer rbp, Recipe recipe) {
/* 1233 */     Recipe playerRecipe = rbp.getRecipe(recipe.getRecipeId());
/* 1234 */     if (playerRecipe == null) {
/*      */ 
/*      */       
/* 1237 */       dbSaveRecipe(rbp.getPlayerId(), recipe.getRecipeId(), rbp.isFavourite(recipe.getRecipeId()), rbp.getNotes(recipe.getRecipeId()));
/* 1238 */       if (recipe.hasCooker())
/*      */       {
/* 1240 */         for (ItemTemplate cooker : recipe.getCookerTemplates())
/*      */         {
/* 1242 */           dbSaveRecipeCooker(rbp.getPlayerId(), recipe.getRecipeId(), cooker.getTemplateId());
/*      */         }
/*      */       }
/*      */       
/* 1246 */       if (recipe.hasContainer())
/*      */       {
/* 1248 */         for (ItemTemplate container : recipe.getContainerTemplates())
/*      */         {
/* 1250 */           dbSaveRecipeContainer(rbp.getPlayerId(), recipe.getRecipeId(), container.getTemplateId());
/*      */         }
/*      */       }
/*      */       
/* 1254 */       if (recipe.getActiveItem() != null)
/*      */       {
/* 1256 */         dbSaveRecipeIngredient(false, rbp.getPlayerId(), recipe.getRecipeId(), recipe.getActiveItem());
/*      */       }
/* 1258 */       if (recipe.getTargetItem() != null)
/*      */       {
/* 1260 */         dbSaveRecipeIngredient(false, rbp.getPlayerId(), recipe.getRecipeId(), recipe.getTargetItem());
/*      */       }
/* 1262 */       for (Ingredient i : recipe.getAllIngredients(true).values())
/*      */       {
/* 1264 */         dbSaveRecipeIngredient(false, rbp.getPlayerId(), recipe.getRecipeId(), i);
/*      */       }
/* 1266 */       rbp.addRecipe(recipe);
/* 1267 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1271 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeRecipeForPlayer(long playerId, short recipeId) {
/* 1282 */     dbRemovePlayerRecipe(playerId, recipeId);
/* 1283 */     RecipesByPlayer rbp = getRecipesByPlayer(playerId, false);
/* 1284 */     if (rbp != null)
/*      */     {
/* 1286 */       rbp.removeRecipe(recipeId);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void deleteRecipesByNumber(short recipeId) {
/* 1296 */     for (Map.Entry<Long, RecipesByPlayer> entry : playerRecipes.entrySet()) {
/*      */       
/* 1298 */       Recipe recipe = ((RecipesByPlayer)entry.getValue()).getRecipe(recipeId);
/* 1299 */       if (recipe != null) {
/*      */         
/* 1301 */         RecipesByPlayer rbp = entry.getValue();
/* 1302 */         long playerId = ((Long)entry.getKey()).longValue();
/* 1303 */         dbRemovePlayerRecipe(playerId, recipeId);
/* 1304 */         rbp.removeRecipe(recipeId);
/*      */ 
/*      */         
/*      */         try {
/* 1308 */           Player player = Players.getInstance().getPlayer(playerId);
/* 1309 */           player.getCommunicator().sendCookbookRecipe(recipe);
/*      */         }
/* 1311 */         catch (NoSuchPlayerException noSuchPlayerException) {}
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
/*      */   
/*      */   public static void deleteRecipesForPlayer(long playerId) {
/* 1325 */     dbRemovePlayerRecipes(playerId);
/* 1326 */     playerRecipes.remove(Long.valueOf(playerId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void deleteAllKnownRecipes() {
/* 1334 */     dbRemoveAllPlayerRecipes();
/* 1335 */     playerRecipes.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setIsFavourite(long playerId, short recipeId, boolean isFavourite) {
/* 1346 */     RecipesByPlayer rbp = getRecipesByPlayer(playerId, true);
/*      */     
/* 1348 */     rbp.setFavourite(recipeId, isFavourite);
/* 1349 */     dbUpdateRecipeFavourite(playerId, recipeId, isFavourite);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setNotes(long playerId, short recipeId, String notes) {
/* 1360 */     RecipesByPlayer rbp = getRecipesByPlayer(playerId, true);
/*      */     
/* 1362 */     rbp.setNotes(recipeId, notes);
/* 1363 */     dbUpdateRecipeNotes(playerId, recipeId, notes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbSaveRecipe(long playerId, short recipeId, boolean favourite, String notes) {
/* 1373 */     Connection dbcon = null;
/* 1374 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1377 */       dbcon = DbConnector.getItemDbCon();
/* 1378 */       ps = dbcon.prepareStatement("INSERT INTO RECIPESPLAYER (PLAYERID,RECIPEID,FAVOURITE,NOTES) VALUES(?,?,?,?)");
/* 1379 */       ps.setLong(1, playerId);
/* 1380 */       ps.setShort(2, recipeId);
/* 1381 */       ps.setBoolean(3, favourite);
/* 1382 */       ps.setString(4, notes);
/* 1383 */       ps.executeUpdate();
/*      */     }
/* 1385 */     catch (SQLException sqex) {
/*      */       
/* 1387 */       logger.log(Level.WARNING, "Failed to save player recipe: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1391 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1392 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void dbSaveRecipeCooker(long playerId, short recipeId, int cookerId) {
/* 1404 */     Connection dbcon = null;
/* 1405 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1408 */       dbcon = DbConnector.getItemDbCon();
/* 1409 */       ps = dbcon.prepareStatement("INSERT INTO RECIPEPLAYERCOOKERS (PLAYERID,RECIPEID,COOKERID) VALUES(?,?,?)");
/* 1410 */       ps.setLong(1, playerId);
/* 1411 */       ps.setShort(2, recipeId);
/* 1412 */       ps.setShort(3, (short)cookerId);
/* 1413 */       ps.executeUpdate();
/*      */     }
/* 1415 */     catch (SQLException sqex) {
/*      */       
/* 1417 */       logger.log(Level.WARNING, "Failed to save player recipe cooker: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1421 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1422 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void dbSaveRecipeContainer(long playerId, short recipeId, int containerId) {
/* 1434 */     Connection dbcon = null;
/* 1435 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1438 */       dbcon = DbConnector.getItemDbCon();
/* 1439 */       ps = dbcon.prepareStatement("INSERT INTO RECIPEPLAYERCONTAINERS (PLAYERID,RECIPEID,CONTAINERID) VALUES(?,?,?)");
/* 1440 */       ps.setLong(1, playerId);
/* 1441 */       ps.setShort(2, recipeId);
/* 1442 */       ps.setShort(3, (short)containerId);
/* 1443 */       ps.executeUpdate();
/*      */     }
/* 1445 */     catch (SQLException sqex) {
/*      */       
/* 1447 */       logger.log(Level.WARNING, "Failed to save player recipe container: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1451 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1452 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static void dbSaveRecipeIngredient(boolean update, long playerId, short recipeId, Ingredient ingredient) {
/* 1467 */     Connection dbcon = null;
/* 1468 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1471 */       dbcon = DbConnector.getItemDbCon();
/* 1472 */       if (update) {
/*      */         
/* 1474 */         ps = dbcon.prepareStatement("UPDATE RECIPEPLAYERINGREDIENTS SET TEMPLATEID=?,CSTATE=?,PSTATE=?,MATERIAL=?,REALTEMPLATEID=? WHERE PLAYERID=? AND RECIPEID=? AND INGREDIENTID=?");
/* 1475 */         ps.setShort(1, (short)ingredient.getTemplateId());
/* 1476 */         ps.setByte(2, ingredient.getCState());
/* 1477 */         ps.setByte(3, ingredient.getPState());
/* 1478 */         ps.setByte(4, ingredient.getMaterial());
/*      */         
/* 1480 */         if (ingredient.getTemplateId() == 272) {
/* 1481 */           ps.setShort(5, (short)ingredient.getCorpseData());
/*      */         } else {
/* 1483 */           ps.setShort(5, (short)ingredient.getRealTemplateId());
/*      */         } 
/* 1485 */         ps.setLong(6, playerId);
/* 1486 */         ps.setShort(7, recipeId);
/* 1487 */         ps.setByte(8, ingredient.getIngredientId());
/* 1488 */         int did = ps.executeUpdate();
/* 1489 */         if (did > 0) {
/*      */           return;
/*      */         }
/* 1492 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */       } 
/* 1494 */       ps = dbcon.prepareStatement("INSERT INTO RECIPEPLAYERINGREDIENTS (PLAYERID,RECIPEID,INGREDIENTID,GROUPID,TEMPLATEID,CSTATE,PSTATE,MATERIAL,REALTEMPLATEID) VALUES(?,?,?,?,?,?,?,?,?)");
/* 1495 */       ps.setLong(1, playerId);
/* 1496 */       ps.setShort(2, recipeId);
/* 1497 */       ps.setByte(3, ingredient.getIngredientId());
/* 1498 */       ps.setByte(4, ingredient.getGroupId());
/* 1499 */       ps.setShort(5, (short)ingredient.getTemplateId());
/* 1500 */       ps.setByte(6, ingredient.getCState());
/* 1501 */       ps.setByte(7, ingredient.getPState());
/* 1502 */       ps.setByte(8, ingredient.getMaterial());
/*      */       
/* 1504 */       if (ingredient.getTemplateId() == 272) {
/* 1505 */         ps.setShort(9, (short)ingredient.getCorpseData());
/*      */       } else {
/* 1507 */         ps.setShort(9, (short)ingredient.getRealTemplateId());
/* 1508 */       }  ps.executeUpdate();
/*      */     }
/* 1510 */     catch (SQLException sqex) {
/*      */       
/* 1512 */       logger.log(Level.WARNING, "Failed to save player recipe ingredient: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1516 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1517 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateRecipeFavourite(long playerId, short recipeId, boolean isFavourite) {
/* 1527 */     Connection dbcon = null;
/* 1528 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1531 */       logger.info("update favourite for " + recipeId);
/* 1532 */       dbcon = DbConnector.getItemDbCon();
/* 1533 */       ps = dbcon.prepareStatement("UPDATE RECIPESPLAYER SET FAVOURITE=? WHERE PLAYERID=? AND RECIPEID=?");
/* 1534 */       ps.setBoolean(1, isFavourite);
/* 1535 */       ps.setLong(2, playerId);
/* 1536 */       ps.setShort(3, recipeId);
/* 1537 */       int did = ps.executeUpdate();
/* 1538 */       if (did > 0)
/*      */         return; 
/* 1540 */       logger.info("Update favourite failed, so trying create " + did);
/* 1541 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1542 */       ps = dbcon.prepareStatement("INSERT INTO RECIPESPLAYER (PLAYERID,RECIPEID,FAVOURITE,NOTES) VALUES(?,?,?,?)");
/* 1543 */       ps.setLong(1, playerId);
/* 1544 */       ps.setShort(2, recipeId);
/* 1545 */       ps.setBoolean(3, isFavourite);
/* 1546 */       ps.setString(4, "");
/* 1547 */       ps.executeUpdate();
/*      */     }
/* 1549 */     catch (SQLException sqex) {
/*      */       
/* 1551 */       logger.log(Level.WARNING, "Failed to update player (" + playerId + ") recipe (" + recipeId + ") favourite: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1555 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1556 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbUpdateRecipeNotes(long playerId, short recipeId, String notes) {
/* 1566 */     Connection dbcon = null;
/* 1567 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1570 */       logger.info("update notes for " + recipeId);
/* 1571 */       dbcon = DbConnector.getItemDbCon();
/* 1572 */       ps = dbcon.prepareStatement("UPDATE RECIPESPLAYER SET NOTES=? WHERE PLAYERID=? AND RECIPEID=?");
/* 1573 */       ps.setString(1, notes);
/* 1574 */       ps.setLong(2, playerId);
/* 1575 */       ps.setShort(3, recipeId);
/* 1576 */       int did = ps.executeUpdate();
/* 1577 */       if (did > 0)
/*      */         return; 
/* 1579 */       logger.info("Update notes failed, so trying create " + did);
/* 1580 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1581 */       ps = dbcon.prepareStatement("INSERT INTO RECIPESPLAYER (PLAYERID,RECIPEID,FAVOURITE,NOTES) VALUES(?,?,?,?)");
/* 1582 */       ps.setLong(1, playerId);
/* 1583 */       ps.setShort(2, recipeId);
/* 1584 */       ps.setBoolean(3, false);
/* 1585 */       ps.setString(4, notes);
/* 1586 */       ps.executeUpdate();
/*      */     }
/* 1588 */     catch (SQLException sqex) {
/*      */       
/* 1590 */       logger.log(Level.WARNING, "Failed to update player (" + playerId + ") recipe (" + recipeId + ") notes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1594 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1595 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbRemovePlayerRecipes(long playerId) {
/* 1605 */     Connection dbcon = null;
/* 1606 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1609 */       dbcon = DbConnector.getItemDbCon();
/* 1610 */       ps = dbcon.prepareStatement("DELETE FROM RECIPESPLAYER WHERE PLAYERID=?");
/* 1611 */       ps.setLong(1, playerId);
/* 1612 */       ps.executeUpdate();
/* 1613 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1614 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERCOOKERS WHERE PLAYERID=?");
/* 1615 */       ps.setLong(1, playerId);
/* 1616 */       ps.executeUpdate();
/* 1617 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1618 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERCONTAINERS WHERE PLAYERID=?");
/* 1619 */       ps.setLong(1, playerId);
/* 1620 */       ps.executeUpdate();
/* 1621 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1622 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERINGREDIENTS WHERE PLAYERID=?");
/* 1623 */       ps.setLong(1, playerId);
/* 1624 */       ps.executeUpdate();
/*      */     }
/* 1626 */     catch (SQLException sqex) {
/*      */       
/* 1628 */       logger.log(Level.WARNING, "Failed to remove player recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1632 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1633 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbRemovePlayerRecipe(long playerId, short recipeId) {
/* 1644 */     Connection dbcon = null;
/* 1645 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1648 */       dbcon = DbConnector.getItemDbCon();
/* 1649 */       ps = dbcon.prepareStatement("DELETE FROM RECIPESPLAYER WHERE PLAYERID=? AND RECIPEID=?");
/* 1650 */       ps.setLong(1, playerId);
/* 1651 */       ps.setShort(2, recipeId);
/* 1652 */       ps.executeUpdate();
/* 1653 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1654 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERCOOKERS WHERE PLAYERID=? AND RECIPEID=?");
/* 1655 */       ps.setLong(1, playerId);
/* 1656 */       ps.setShort(2, recipeId);
/* 1657 */       ps.executeUpdate();
/* 1658 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1659 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERCONTAINERS WHERE PLAYERID=? AND RECIPEID=?");
/* 1660 */       ps.setLong(1, playerId);
/* 1661 */       ps.setShort(2, recipeId);
/* 1662 */       ps.executeUpdate();
/* 1663 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1664 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERINGREDIENTS WHERE PLAYERID=? AND RECIPEID=?");
/* 1665 */       ps.setLong(1, playerId);
/* 1666 */       ps.setShort(2, recipeId);
/* 1667 */       ps.executeUpdate();
/*      */     }
/* 1669 */     catch (SQLException sqex) {
/*      */       
/* 1671 */       logger.log(Level.WARNING, "Failed to remove player recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1675 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1676 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbRemoveAllPlayerRecipes() {
/* 1686 */     Connection dbcon = null;
/* 1687 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1690 */       dbcon = DbConnector.getItemDbCon();
/* 1691 */       ps = dbcon.prepareStatement("DELETE FROM RECIPESPLAYER");
/* 1692 */       ps.executeUpdate();
/* 1693 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1694 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERCOOKERS");
/* 1695 */       ps.executeUpdate();
/* 1696 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1697 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERCONTAINERS");
/* 1698 */       ps.executeUpdate();
/* 1699 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1700 */       ps = dbcon.prepareStatement("DELETE FROM RECIPEPLAYERINGREDIENTS");
/* 1701 */       ps.executeUpdate();
/*      */     }
/* 1703 */     catch (SQLException sqex) {
/*      */       
/* 1705 */       logger.log(Level.WARNING, "Failed to remove all player recipes: " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1709 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1710 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\RecipesByPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
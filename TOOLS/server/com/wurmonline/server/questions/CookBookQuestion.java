/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.Ingredient;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.Recipe;
/*      */ import com.wurmonline.server.items.Recipes;
/*      */ import com.wurmonline.server.items.RecipesByPlayer;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ public class CookBookQuestion
/*      */   extends Question
/*      */ {
/*   47 */   private static final Logger logger = Logger.getLogger(CookBookQuestion.class.getName());
/*      */   
/*      */   private byte displayType;
/*      */   private final int sortBy;
/*      */   private Ingredient[] ingredients;
/*   52 */   private Ingredient ingred = null;
/*   53 */   private Recipe recip = null;
/*      */   private boolean showExtra = false;
/*      */   private boolean showLinks = true;
/*   56 */   private String from = "";
/*   57 */   private String searchFor = "";
/*   58 */   ArrayList<String> history = new ArrayList<>();
/*      */   
/*      */   private static final String red = "color=\"255,127,127\"";
/*      */   
/*      */   private static final String green = "color=\"127,255,127\"";
/*      */   
/*      */   public static final byte TYPE_INFO = 0;
/*      */   
/*      */   public static final byte TYPE_TARGET_ACTION_RECIPES = 1;
/*      */   
/*      */   public static final byte TYPE_CONTAINER_ACTION_RECIPES = 2;
/*      */   
/*      */   public static final byte TYPE_HEAT_RECIPES = 3;
/*      */   
/*      */   public static final byte TYPE_TIME_RECIPES = 4;
/*      */   public static final byte TYPE_COOKERS_LIST = 5;
/*      */   public static final byte TYPE_COOKER_RECIPES = 6;
/*      */   public static final byte TYPE_CONTAINERS_LIST = 7;
/*      */   public static final byte TYPE_CONTAINER_RECIPES = 8;
/*      */   public static final byte TYPE_TOOLS_LIST = 9;
/*      */   public static final byte TYPE_TOOL_RECIPES = 10;
/*      */   public static final byte TYPE_INGREDIENTS_LIST = 11;
/*      */   public static final byte TYPE_INGREDIENT_RECIPES = 12;
/*      */   public static final byte TYPE_RECIPE = 13;
/*      */   public static final byte TYPE_SEARCH_RECIPES = 14;
/*      */   public static final byte TYPE_BACK = 15;
/*      */   
/*      */   public CookBookQuestion(Creature aResponder, long aTargetId) {
/*   86 */     super(aResponder, aResponder.getName() + "'s CookBook", 
/*   87 */         makeTitle(aResponder, (byte)0, aTargetId), 135, aTargetId);
/*   88 */     this.sortBy = 1;
/*   89 */     if (aTargetId == -10L) {
/*   90 */       this.displayType = 0;
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*   97 */         Item target = Items.getItem(aTargetId);
/*   98 */         if (target.getTemplate().isCooker()) {
/*   99 */           this.displayType = 6;
/*  100 */         } else if (target.getTemplate().isCookingTool()) {
/*  101 */           this.displayType = 10;
/*      */         } else {
/*      */           
/*  104 */           this.displayType = 12;
/*      */         } 
/*  106 */       } catch (NoSuchItemException e) {
/*      */         
/*  108 */         this.displayType = 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CookBookQuestion(Creature aResponder, byte aDisplayType, long aTargetId) {
/*  115 */     super(aResponder, aResponder.getName() + "'s Cookbook", 
/*  116 */         makeTitle(aResponder, aDisplayType, aTargetId), 135, aTargetId);
/*  117 */     this.sortBy = 1;
/*  118 */     this.displayType = aDisplayType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CookBookQuestion(Creature aResponder, byte aDisplayType, long aTargetId, int sortId) {
/*  124 */     super(aResponder, aResponder.getName() + "'s Cookbook", 
/*  125 */         makeTitle(aResponder, aDisplayType, aTargetId), 135, aTargetId);
/*  126 */     this.sortBy = sortId;
/*  127 */     this.displayType = aDisplayType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CookBookQuestion(Creature aResponder, byte aDisplayType, Ingredient ingredient) {
/*  133 */     super(aResponder, aResponder.getName() + "'s Cookbook", "List of recipes that use " + ingredient
/*  134 */         .getName(false) + ".", 135, -10L);
/*  135 */     this.sortBy = 1;
/*  136 */     this.displayType = aDisplayType;
/*  137 */     this.ingred = ingredient;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CookBookQuestion(Creature aResponder, byte aDisplayType, Recipe recipe, boolean justRecipe, long aTarget, String signedBy) {
/*  143 */     super(aResponder, justRecipe ? recipe.getName() : (aResponder.getName() + "'s Cookbook"), "Recipe: " + recipe
/*  144 */         .getName(), 135, aTarget);
/*  145 */     this.sortBy = 1;
/*  146 */     this.displayType = aDisplayType;
/*  147 */     this.recip = recipe;
/*  148 */     this.showLinks = !justRecipe;
/*  149 */     if (signedBy.length() > 0)
/*  150 */       this.from = signedBy; 
/*  151 */     if (justRecipe)
/*      */     {
/*      */       
/*  154 */       this.history.add(aDisplayType + "," + recipe
/*  155 */           .getRecipeId() + "," + justRecipe + "," + aTarget + "," + signedBy);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CookBookQuestion(Creature aResponder, String searchFor, int sortId) {
/*  165 */     super(aResponder, aResponder.getName() + "'s Cookbook", 
/*  166 */         (searchFor.length() > 0) ? ("List of recipes that have a name with " + searchFor + " in it.") : "List of all your known recipes.", 135, -10L);
/*      */     
/*  168 */     this.sortBy = sortId;
/*  169 */     this.displayType = 14;
/*  170 */     this.searchFor = searchFor; } static String makeTitle(Creature aResponder, byte aType, long templateId) { ItemTemplate cookerIT;
/*      */     ItemTemplate containerIT;
/*      */     ItemTemplate toolIT;
/*      */     String cookerName;
/*      */     String toolName;
/*  175 */     switch (aType) {
/*      */       
/*      */       case 0:
/*  178 */         return aResponder.getName() + "'s Cookbook";
/*      */       case 1:
/*  180 */         return "Target Action Recipe List";
/*      */       case 2:
/*  182 */         return "Container Action Recipe List";
/*      */       case 3:
/*  184 */         return "Heat Recipe List";
/*      */       case 4:
/*  186 */         return "Time Recipe List";
/*      */       case 5:
/*  188 */         return "List of known cookers";
/*      */       
/*      */       case 6:
/*  191 */         cookerIT = ItemTemplateFactory.getInstance().getTemplateOrNull((int)templateId);
/*  192 */         cookerName = (cookerIT == null) ? "xxxx" : cookerIT.getName();
/*  193 */         return "Recipes made in " + cookerName;
/*      */       
/*      */       case 7:
/*  196 */         return "List of known containers";
/*      */       
/*      */       case 8:
/*  199 */         containerIT = ItemTemplateFactory.getInstance().getTemplateOrNull((int)templateId);
/*  200 */         cookerName = (containerIT == null) ? "xxxx" : containerIT.getName();
/*  201 */         return "Recipes made in " + cookerName;
/*      */       
/*      */       case 9:
/*  204 */         return "List of known tools";
/*      */       
/*      */       case 10:
/*  207 */         toolIT = ItemTemplateFactory.getInstance().getTemplateOrNull((int)templateId);
/*  208 */         toolName = (toolIT == null) ? "xxxx" : toolIT.getName();
/*  209 */         return "Recipes made with a " + toolName;
/*      */       
/*      */       case 11:
/*  212 */         return "List of known ingredients";
/*      */       case 12:
/*  214 */         return "List of Recipes that use a xxxx";
/*      */       case 14:
/*  216 */         return "List of recipes that were found with xxxx in";
/*      */     } 
/*  218 */     return ""; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void answer(Properties aAnswer) {
/*  224 */     setAnswer(aAnswer);
/*  225 */     if (this.type == 0) {
/*      */       
/*  227 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*  230 */     if (this.type == 135) {
/*      */       
/*  232 */       boolean close = getBooleanProp("close");
/*  233 */       if (close)
/*      */         return; 
/*  235 */       boolean add = getBooleanProp("add");
/*  236 */       if (add && this.target != -10L) {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/*  241 */           Item paper = Items.getItem(this.target);
/*  242 */           Item parent = paper.getTopParentOrNull();
/*  243 */           if (parent != null && parent.isInventory()) {
/*      */             
/*  245 */             if (RecipesByPlayer.addRecipe(getResponder(), this.recip)) {
/*      */               
/*  247 */               getResponder().getCommunicator().sendNormalServerMessage("You finish adding the " + this.recip
/*  248 */                   .getName() + " into your cookbook, just in time, as the recipe has decayed away.");
/*  249 */               Server.getInstance().broadCastAction(
/*  250 */                   getResponder().getName() + " stops writing.", getResponder(), 5);
/*      */               
/*  252 */               Items.destroyItem(this.target);
/*  253 */               getResponder().getCommunicator().sendCookbookRecipe(this.recip);
/*      */             } 
/*      */           } else {
/*      */             
/*  257 */             getResponder().getCommunicator().sendNormalServerMessage("Cannot find the recipe on you!");
/*      */           } 
/*  259 */         } catch (NoSuchItemException e) {
/*      */ 
/*      */           
/*  262 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*  267 */       for (String key : getAnswer().stringPropertyNames()) {
/*      */         
/*  269 */         if (key.startsWith("sort")) {
/*      */ 
/*      */           
/*  272 */           String sid = key.substring(4);
/*  273 */           int newSort = Integer.parseInt(sid);
/*      */           
/*  275 */           if (this.searchFor.equalsIgnoreCase("")) {
/*      */             
/*  277 */             CookBookQuestion cookBookQuestion1 = new CookBookQuestion(getResponder(), this.displayType, this.target, newSort);
/*  278 */             cookBookQuestion1.history = this.history;
/*  279 */             cookBookQuestion1.ingred = this.ingred;
/*  280 */             cookBookQuestion1.sendQuestion();
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  285 */           CookBookQuestion cookBookQuestion = new CookBookQuestion(getResponder(), this.searchFor, newSort);
/*  286 */           cookBookQuestion.history = this.history;
/*  287 */           cookBookQuestion.ingred = this.ingred;
/*  288 */           cookBookQuestion.sendQuestion();
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  293 */       if (getBooleanProp("find")) {
/*      */         
/*  295 */         String srch = aAnswer.getProperty("search");
/*  296 */         if (srch != null) {
/*      */ 
/*      */           
/*  299 */           CookBookQuestion cookBookQuestion = new CookBookQuestion(getResponder(), srch, 0);
/*  300 */           cookBookQuestion.history = this.history;
/*  301 */           cookBookQuestion.history.add("14," + srch);
/*      */ 
/*      */           
/*  304 */           cookBookQuestion.sendQuestion();
/*      */           
/*      */           return;
/*      */         } 
/*  308 */       } else if (getBooleanProp("remove")) {
/*      */ 
/*      */         
/*  311 */         RecipesByPlayer.removeRecipeForPlayer(getResponder().getWurmId(), this.recip.getRecipeId());
/*  312 */         getResponder().getCommunicator().sendCookbookRecipe(this.recip);
/*      */       }
/*  314 */       else if (getBooleanProp("back")) {
/*      */         CookBookQuestion cookBookQuestion1; int i, templateId, id; String sFor; CookBookQuestion cookBookQuestion3; byte cstate; Recipe recipe; CookBookQuestion cookBookQuestion2; byte pstate; CookBookQuestion cookBookQuestion4; byte material;
/*      */         boolean hasRealTemplate;
/*      */         int realTemplateId, corpseData;
/*  318 */         if (this.history.size() < 2) {
/*      */           
/*  320 */           CookBookQuestion cookBookQuestion = new CookBookQuestion(getResponder(), 0L);
/*  321 */           cookBookQuestion.history.add("0");
/*  322 */           cookBookQuestion.sendQuestion();
/*      */           
/*      */           return;
/*      */         } 
/*  326 */         this.history.remove(this.history.size() - 1);
/*  327 */         String last = this.history.get(this.history.size() - 1);
/*  328 */         String[] parts = last.split(",");
/*  329 */         byte type = Byte.parseByte(parts[0]);
/*  330 */         switch (type) {
/*      */ 
/*      */           
/*      */           case 0:
/*  334 */             cookBookQuestion1 = new CookBookQuestion(getResponder(), 0L);
/*  335 */             cookBookQuestion1.history.add("0");
/*  336 */             cookBookQuestion1.sendQuestion();
/*      */             return;
/*      */ 
/*      */           
/*      */           case 1:
/*      */           case 2:
/*      */           case 3:
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           case 8:
/*      */           case 9:
/*      */           case 10:
/*      */           case 11:
/*  351 */             i = Integer.parseInt(parts[1]);
/*  352 */             cookBookQuestion3 = new CookBookQuestion(getResponder(), type, i);
/*  353 */             cookBookQuestion3.history = this.history;
/*  354 */             cookBookQuestion3.sendQuestion();
/*      */             return;
/*      */ 
/*      */ 
/*      */           
/*      */           case 12:
/*  360 */             templateId = Integer.parseInt(parts[1]);
/*  361 */             cstate = Byte.parseByte(parts[2]);
/*  362 */             pstate = Byte.parseByte(parts[3]);
/*  363 */             material = Byte.parseByte(parts[4]);
/*  364 */             hasRealTemplate = Boolean.parseBoolean(parts[5]);
/*  365 */             realTemplateId = Integer.parseInt(parts[6]);
/*  366 */             corpseData = Integer.parseInt(parts[7]);
/*      */             
/*      */             try {
/*  369 */               Ingredient ingredient = new Ingredient(templateId, cstate, pstate, material, hasRealTemplate, realTemplateId, corpseData);
/*      */               
/*  371 */               CookBookQuestion cookBookQuestion = new CookBookQuestion(getResponder(), type, ingredient);
/*  372 */               cookBookQuestion.history = this.history;
/*  373 */               cookBookQuestion.sendQuestion();
/*      */               
/*      */               return;
/*  376 */             } catch (NoSuchTemplateException e) {
/*      */               
/*  378 */               logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */             } 
/*      */ 
/*      */           
/*      */           case 13:
/*  383 */             id = Integer.parseInt(parts[1]);
/*  384 */             recipe = RecipesByPlayer.getRecipe(getResponder().getWurmId(), id);
/*  385 */             cookBookQuestion4 = new CookBookQuestion(getResponder(), (byte)13, recipe, false, -10L, "");
/*      */             
/*  387 */             cookBookQuestion4.history = this.history;
/*  388 */             cookBookQuestion4.sendQuestion();
/*      */             return;
/*      */ 
/*      */           
/*      */           case 14:
/*  393 */             sFor = (parts.length > 1) ? parts[1] : "";
/*  394 */             cookBookQuestion2 = new CookBookQuestion(getResponder(), sFor, 0);
/*  395 */             cookBookQuestion2.history = this.history;
/*  396 */             cookBookQuestion2.sendQuestion();
/*      */             return;
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*  402 */       } else if (getBooleanProp("show")) {
/*      */         
/*  404 */         String sel = aAnswer.getProperty("sel");
/*      */         
/*  406 */         String[] parts = sel.split(",");
/*  407 */         byte type = Byte.parseByte(parts[0]);
/*      */         
/*  409 */         if (type == 12) {
/*      */ 
/*      */           
/*  412 */           int templateId = Integer.parseInt(parts[1]);
/*  413 */           byte cstate = Byte.parseByte(parts[2]);
/*  414 */           byte pstate = Byte.parseByte(parts[3]);
/*  415 */           byte material = Byte.parseByte(parts[4]);
/*  416 */           boolean hasRealTemplate = Boolean.parseBoolean(parts[5]);
/*  417 */           int realTemplateId = Integer.parseInt(parts[6]);
/*  418 */           int corpseData = Integer.parseInt(parts[7]);
/*      */           
/*      */           try {
/*  421 */             Ingredient ingredient = new Ingredient(templateId, cstate, pstate, material, hasRealTemplate, realTemplateId, corpseData);
/*      */             
/*  423 */             CookBookQuestion cookBookQuestion = new CookBookQuestion(getResponder(), type, ingredient);
/*  424 */             cookBookQuestion.history = this.history;
/*  425 */             this.history.add("12," + templateId + "," + cstate + "," + pstate + "," + material + "," + hasRealTemplate + "," + realTemplateId + "," + corpseData);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  434 */             cookBookQuestion.sendQuestion();
/*      */             
/*      */             return;
/*  437 */           } catch (NoSuchTemplateException e) {
/*      */             
/*  439 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */           } 
/*      */         } else {
/*  442 */           if (type == 13) {
/*      */             
/*  444 */             int i = Integer.parseInt(parts[1]);
/*  445 */             Recipe recipe = RecipesByPlayer.getRecipe(getResponder().getWurmId(), i);
/*  446 */             CookBookQuestion cookBookQuestion1 = new CookBookQuestion(getResponder(), (byte)13, recipe, false, -10L, "");
/*      */             
/*  448 */             cookBookQuestion1.history = this.history;
/*  449 */             this.history.add("13," + recipe
/*  450 */                 .getRecipeId() + "," + Character.MIN_VALUE + "," + i + ",");
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  455 */             cookBookQuestion1.sendQuestion();
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  460 */           int id = Integer.parseInt(parts[1]);
/*  461 */           CookBookQuestion cookBookQuestion = new CookBookQuestion(getResponder(), type, id);
/*  462 */           cookBookQuestion.history = this.history;
/*  463 */           cookBookQuestion.history.add(type + "," + id);
/*  464 */           cookBookQuestion.sendQuestion();
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  469 */       CookBookQuestion cbq = new CookBookQuestion(getResponder(), 0L);
/*  470 */       cbq.history.add("0");
/*  471 */       cbq.sendQuestion();
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestion() {
/*  480 */     this.showExtra = (getResponder().getPower() > 4 && Servers.isThisATestServer());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  488 */     switch (this.displayType) {
/*      */       
/*      */       case 0:
/*  491 */         sendInfo();
/*      */         break;
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 6:
/*      */       case 8:
/*      */       case 10:
/*      */       case 12:
/*      */       case 14:
/*  502 */         sendRecipes();
/*      */         break;
/*      */       case 5:
/*      */       case 7:
/*      */       case 9:
/*      */       case 11:
/*  508 */         sendList();
/*      */         break;
/*      */       case 13:
/*  511 */         sendRecipe();
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendInfo() {
/*  518 */     StringBuilder buf = new StringBuilder();
/*  519 */     String closeBtn = "harray{label{text=\" \"};button{text=\"Close\";id=\"close\";hover=\"Close the cookbook.\"};label{text=\" \"}};";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  524 */     buf.append("border{border{size=\"20,20\";null;null;center{varray{header{text=\"" + this.question + "\"}}};" + "harray{label{text=\" \"};button{text=\"Close\";id=\"close\";hover=\"Close the cookbook.\"};label{text=\" \"}};" + "null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  533 */         getId() + "\"}");
/*  534 */     buf.append("label{type=\"bold\";text=\"Recipes are split into various categories, these are:\"};");
/*  535 */     buf.append("radio{group=\"sel\";id=\"0,-10\";selected=\"true\";hidden=\"true\"};");
/*  536 */     buf.append("table{rows=\"4\";cols=\"2\";");
/*  537 */     buf.append("radio{group=\"sel\";id=\"1,-10\"};label{text=\"Target actions\";hover=\"e.g. ones where you use one item on another.\"};");
/*  538 */     buf.append("radio{group=\"sel\";id=\"2,-10\"};label{text=\"Container actions\";hover=\"e.g. when you use an item on a container.\"};");
/*  539 */     buf.append("radio{group=\"sel\";id=\"3,-10\"};label{text=\"Heat \";hover=\"normal cooking ones.\"};");
/*  540 */     buf.append("radio{group=\"sel\";id=\"4,-10\"};label{text=\"Time \";hover=\"ones that take time e.g. brewing).\"};");
/*  541 */     buf.append("}");
/*  542 */     buf.append("label{text=\"Selecting one of the above types will give a list of the recipes that you know about of that type.\"};");
/*  543 */     buf.append("label{text=\"\"};");
/*  544 */     buf.append("label{type=\"bold\";text=\"Or you can get a list of:\"};");
/*  545 */     buf.append("table{rows=\"4\";cols=\"2\";");
/*  546 */     buf.append("radio{group=\"sel\";id=\"5,-10\"};label{text=\"Cookers\"};");
/*  547 */     buf.append("radio{group=\"sel\";id=\"7,-10\"};label{text=\"Containers\"};");
/*  548 */     buf.append("radio{group=\"sel\";id=\"9,-10\"};label{text=\"Tools\"};");
/*  549 */     buf.append("radio{group=\"sel\";id=\"11,-10\"};label{text=\"Ingredients\"};");
/*  550 */     buf.append("}");
/*  551 */     buf.append("text{text=\"Selecting one of the above searches will give a list of those items that are used in that category that you know about.\"};");
/*  552 */     buf.append("label{text=\"\"};");
/*  553 */     buf.append("harray{label{type=\"bold\";text=\"Select what you want to do above and click :\"};button{text=\"here\";id=\"show\";default=\"true\"}}");
/*      */ 
/*      */ 
/*      */     
/*  557 */     buf.append("label{text=\"\"};");
/*  558 */     buf.append("harray{label{text=\"Or you can \"}button{text=\"search\";id=\"find\";hover=\"Dont forget to add a search criteria.\"}label{text=\" for \"}input{maxchars=\"20\";id=\"search\";text=\"\";onenter=\"find\"}label{text=\" in your known recipe names.\"}}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  565 */     buf.append("label{text=\"If you leave the input box blank and do a search, it will list all of your known recipes.\"};");
/*      */     
/*  567 */     buf.append("label{text=\"\"};");
/*  568 */     Recipe[] knownrecipes = RecipesByPlayer.getSearchRecipesFor(getResponder().getWurmId(), "");
/*  569 */     buf.append("label{text=\"You know a total of " + knownrecipes.length + " recipes\"};");
/*      */     
/*  571 */     buf.append("}};null;null;}");
/*      */     
/*  573 */     getResponder().getCommunicator().sendBml(500, 480, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRecipes() {
/*  578 */     StringBuilder buf = new StringBuilder();
/*  579 */     String closeBtn = "harray{label{text=\" \"};harray{button{text=\"Back\";id=\"back\";hover=\"Go back to last screen.\"};label{text=\" \"};button{text=\"Close\";id=\"close\";hover=\"Close the cookbook.\"};};label{text=\" \"}};";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  589 */     buf.append("border{border{size=\"20,25\";null;null;label{type=\"bold\";text=\"" + this.question + ((this.showExtra && this.target != -10L) ? (" - " + this.target) : "") + "\"};" + "harray{label{text=\" \"};harray{button{text=\"Back\";id=\"back\";hover=\"Go back to last screen.\"};label{text=\" \"};button{text=\"Close\";id=\"close\";hover=\"Close the cookbook.\"};};label{text=\" \"}};" + "null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
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
/*  600 */         getId() + "\"}");
/*      */     
/*  602 */     int width = 470;
/*  603 */     int height = 400;
/*  604 */     String mid = "null;";
/*  605 */     boolean defaultShow = true;
/*  606 */     switch (this.displayType) {
/*      */       
/*      */       case 1:
/*  609 */         buf.append(sendTargetActionRecipes());
/*      */         break;
/*      */       case 2:
/*  612 */         buf.append(sendContainerActionRecipes());
/*  613 */         width = 730;
/*      */         break;
/*      */       case 3:
/*  616 */         buf.append(sendHeatRecipes());
/*  617 */         width = 730;
/*      */         break;
/*      */       case 4:
/*  620 */         buf.append(sendTimeRecipes());
/*      */         break;
/*      */       case 6:
/*  623 */         buf.append(sendCookerRecipes());
/*      */         break;
/*      */       case 8:
/*  626 */         buf.append(sendContainerRecipes());
/*      */         break;
/*      */       case 10:
/*  629 */         buf.append(sendToolRecipes());
/*      */         break;
/*      */       case 12:
/*  632 */         buf.append(sendIngredientRecipes());
/*      */         break;
/*      */       case 14:
/*  635 */         buf.append(sendSearchRecipes());
/*  636 */         mid = "center{harray{button{text=\"Search\";id=\"find\";default=\"true\"};label{text=\" \"};input{maxchars=\"20\";id=\"search\";text=\"" + this.searchFor + "\"}}};";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  643 */         defaultShow = false;
/*      */         break;
/*      */     } 
/*      */     
/*  647 */     buf.append("radio{group=\"sel\";id=\"-10\";selected=\"true\";hidden=\"true\";text=\"None\"}");
/*  648 */     buf.append("text{text=\"\"}");
/*      */     
/*  650 */     buf.append("}};null;");
/*      */     
/*  652 */     buf.append("border{size=\"20,20\";null;harray{label{text=\" \"};button{text=\"Show selected\";id=\"show\"" + (defaultShow ? "default=\"true\"" : "") + "}};" + mid + "harray{button{text=\"Go to info\";id=\"info\"};label{text=\" \"}};null;}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  658 */     buf.append("}");
/*      */     
/*  660 */     getResponder().getCommunicator().sendBml(width, height, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendList() {
/*  665 */     StringBuilder buf = new StringBuilder();
/*  666 */     String closeBtn = "harray{label{text=\" \"};harray{button{text=\"Back\";id=\"back\";hover=\"Go back to last screen.\"};label{text=\" \"};button{text=\"Close\";id=\"close\";hover=\"Close the cookbook.\"};};label{text=\" \"}};";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  676 */     buf.append("border{border{size=\"20,25\";null;null;label{type=\"bold\";text=\"" + this.question + "          \"};" + "harray{label{text=\" \"};harray{button{text=\"Back\";id=\"back\";hover=\"Go back to last screen.\"};label{text=\" \"};button{text=\"Close\";id=\"close\";hover=\"Close the cookbook.\"};};label{text=\" \"}};" + "null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  685 */         getId() + "\"}");
/*      */     
/*  687 */     int width = 400;
/*  688 */     int height = 300;
/*  689 */     switch (this.displayType) {
/*      */       
/*      */       case 5:
/*  692 */         buf.append(sendCookersList());
/*      */         break;
/*      */       case 7:
/*  695 */         buf.append(sendContainersList());
/*      */         break;
/*      */       case 9:
/*  698 */         buf.append(sendToolsList());
/*      */         break;
/*      */       
/*      */       case 11:
/*  702 */         buf.append(sendIngredientsList());
/*  703 */         width = this.showExtra ? 550 : 450;
/*  704 */         height = 450;
/*      */         break;
/*      */     } 
/*      */     
/*  708 */     buf.append("radio{group=\"sel\";id=\"-10,-10\";selected=\"true\";hidden=\"true\";text=\"None\"}");
/*  709 */     buf.append("text{text=\"\"}");
/*      */     
/*  711 */     buf.append("}};null;");
/*      */     
/*  713 */     buf.append("border{size=\"20,20\";null;harray{label{text=\" \"};button{text=\"Show selected\";id=\"show\"}};null;harray{button{text=\"Go to info\";id=\"info\"};label{text=\" \"}};null;}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  719 */     buf.append("}");
/*      */     
/*  721 */     getResponder().getCommunicator().sendBml(width, height, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendTargetActionRecipes() {
/*  726 */     StringBuilder buf = new StringBuilder();
/*  727 */     Recipe[] recipes = RecipesByPlayer.getTargetActionRecipesFor(getResponder().getWurmId());
/*  728 */     int absSortBy = Math.abs(this.sortBy);
/*  729 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/*  731 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/*  734 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  739 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/*  740 */                   return -1;
/*      */                 }
/*  742 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/*  747 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  752 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/*  757 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  762 */                 return param1.getActiveItem().getName(false).compareTo(param2.getActiveItem().getName(false)) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/*  767 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  772 */                 return param1.getTargetItem().getName(false).compareTo(param2.getTargetItem().getName(false)) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/*  778 */     buf.append("table{rows=\"1\";cols=\"6\";label{text=\" \"};" + 
/*  779 */         colHeader("Recipe Name", 1, this.sortBy) + "label{text=\" \"};" + 
/*  780 */         colHeader("Active Item", 2, this.sortBy) + "label{text=\" \"};" + 
/*  781 */         colHeader("Target Item", 3, this.sortBy));
/*      */     
/*  783 */     for (Recipe recipe : recipes)
/*      */     {
/*  785 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\"\"}" + 
/*  786 */           colourRecipeName(recipe, recipe.getName(), false) + (
/*  787 */           (recipe.getActiveItem() == null) ? "label{text=\"\"}" : (
/*  788 */           recipe.getActiveItem().getTemplate().isCookingTool() ? ("radio{group=\"sel\";id=\"10," + recipe
/*  789 */           .getActiveItem().getTemplateId() + "\";text=\"\"}") : ("radio{group=\"sel\";id=\"12," + recipe
/*      */           
/*  791 */           .getActiveItem().getTemplateId() + "," + recipe
/*  792 */           .getActiveItem().getCState() + "," + recipe
/*  793 */           .getActiveItem().getPState() + "," + recipe
/*  794 */           .getActiveItem().getMaterial() + "," + recipe
/*  795 */           .getActiveItem().hasRealTemplate() + "," + recipe
/*  796 */           .getActiveItem().getRealTemplateId() + "," + recipe
/*  797 */           .getActiveItem().getCorpseData() + "\";text=\"\"}"))) + "label{text=\"" + 
/*      */           
/*  799 */           Recipes.getIngredientName(recipe.getActiveItem()) + ((this.showExtra && recipe
/*  800 */           .getActiveItem() != null) ? (" - " + recipe.getActiveItem().getTemplateId()) : "") + "\"};" + (
/*  801 */           (recipe.getTargetItem() == null) ? "label{text=\"\"}" : (
/*  802 */           recipe.getTargetItem().getTemplate().isFoodMaker() ? ("radio{group=\"sel\";id=\"8," + recipe
/*  803 */           .getTargetItem().getTemplateId() + "\";text=\"\"}") : ("radio{group=\"sel\";id=\"12," + recipe
/*      */           
/*  805 */           .getTargetItem().getTemplateId() + "," + recipe
/*  806 */           .getTargetItem().getCState() + "," + recipe
/*  807 */           .getTargetItem().getPState() + "," + recipe
/*  808 */           .getTargetItem().getMaterial() + "," + recipe
/*  809 */           .getTargetItem().hasRealTemplate() + "," + recipe
/*  810 */           .getTargetItem().getRealTemplateId() + "," + recipe
/*  811 */           .getTargetItem().getCorpseData() + "\";text=\"\"}"))) + "label{text=\"" + 
/*      */           
/*  813 */           Recipes.getIngredientName(recipe.getTargetItem()) + ((this.showExtra && recipe
/*  814 */           .getTargetItem() != null) ? (" - " + recipe.getTargetItem().getTemplateId()) : "") + "\"};");
/*      */     }
/*      */     
/*  817 */     buf.append("}");
/*  818 */     if (recipes.length == 0)
/*  819 */       buf.append("label{text=\"You dont know any target action recipes.\"}"); 
/*  820 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendContainerActionRecipes() {
/*  825 */     StringBuilder buf = new StringBuilder();
/*  826 */     Recipe[] recipes = RecipesByPlayer.getContainerActionRecipesFor(getResponder().getWurmId());
/*  827 */     int absSortBy = Math.abs(this.sortBy);
/*  828 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/*  830 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/*  833 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  838 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/*  839 */                   return -1;
/*      */                 }
/*  841 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/*  846 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  851 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/*  856 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  861 */                 return Recipes.getIngredientName(param1.getActiveItem()).compareTo(Recipes.getIngredientName(param2.getActiveItem())) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/*  866 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  871 */                 return param1.getContainersAsString().compareTo(param2.getContainersAsString()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/*  876 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  881 */                 return param1.getIngredientsAsString().compareTo(param2.getIngredientsAsString()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/*  887 */     buf.append("table{rows=\"1\";cols=\"8\";label{text=\" \"};" + 
/*  888 */         colHeader("Recipe Name", 1, this.sortBy) + "label{text=\" \"};" + 
/*  889 */         colHeader("Active Item", 2, this.sortBy) + "label{text=\" \"};" + 
/*  890 */         colHeader("Containers", 3, this.sortBy) + "label{text=\" \"};" + 
/*  891 */         colHeader("Ingredients", 4, this.sortBy));
/*      */ 
/*      */     
/*  894 */     for (Recipe recipe : recipes)
/*      */     {
/*  896 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\"\"}" + 
/*  897 */           colourRecipeName(recipe, recipe.getName(), false) + (
/*  898 */           (recipe.getActiveItem() == null) ? "label{text=\"\"}" : (
/*  899 */           recipe.getActiveItem().getTemplate().isCookingTool() ? ("radio{group=\"sel\";id=\"9," + recipe
/*  900 */           .getActiveItem().getTemplateId() + "\";text=\"\"}") : ("radio{group=\"sel\";id=\"12," + recipe
/*      */           
/*  902 */           .getActiveItem().getTemplateId() + "," + recipe
/*  903 */           .getActiveItem().getCState() + "," + recipe
/*  904 */           .getActiveItem().getPState() + "," + recipe
/*  905 */           .getActiveItem().getMaterial() + "," + recipe
/*  906 */           .getActiveItem().hasRealTemplate() + "," + recipe
/*  907 */           .getActiveItem().getRealTemplateId() + "," + recipe
/*  908 */           .getActiveItem().getCorpseData() + "\";text=\"\"}"))) + "label{text=\"" + recipe
/*      */           
/*  910 */           .getActiveItemName() + ((this.showExtra && recipe
/*  911 */           .getActiveItem() != null) ? (" - " + recipe.getActiveItem().getTemplateId()) : "") + "\"};" + (
/*  912 */           !recipe.hasOneContainer() ? "label{text=\"\"}" : ("radio{group=\"sel\";id=\"7," + recipe
/*  913 */           .getContainerId() + "\";text=\"\"}")) + "label{text=\"" + recipe
/*  914 */           .getContainersAsString() + "\"};label{text=\" \"};label{text=\"" + recipe
/*  915 */           .getIngredientsAsString() + "\"};");
/*      */     }
/*      */     
/*  918 */     buf.append("}");
/*      */     
/*  920 */     if (recipes.length == 0)
/*  921 */       buf.append("label{text=\"You dont know any container action recipes.\"}"); 
/*  922 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendHeatRecipes() {
/*  927 */     StringBuilder buf = new StringBuilder();
/*  928 */     Recipe[] recipes = RecipesByPlayer.getHeatRecipesFor(getResponder().getWurmId());
/*  929 */     int absSortBy = Math.abs(this.sortBy);
/*  930 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/*  932 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/*  935 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  940 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/*  941 */                   return -1;
/*      */                 }
/*  943 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/*  948 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  953 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/*  958 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  963 */                 return param1.getCookersAsString().compareTo(param2.getCookersAsString()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/*  968 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  973 */                 return param1.getContainersAsString().compareTo(param2.getContainersAsString()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 4:
/*  978 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/*  983 */                 return param1.getIngredientsAsString().compareTo(param2.getIngredientsAsString()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/*  989 */     buf.append("table{rows=\"1\";cols=\"8\";label{text=\" \"};" + 
/*  990 */         colHeader("Recipe Name", 1, this.sortBy) + "label{text=\" \"};" + 
/*  991 */         colHeader("Cookers List", 2, this.sortBy) + "label{text=\" \"};" + 
/*  992 */         colHeader("Containers List", 3, this.sortBy) + "label{text=\" \"};" + 
/*  993 */         colHeader("Ingredients List", 4, this.sortBy));
/*      */ 
/*      */     
/*  996 */     for (Recipe recipe : recipes)
/*      */     {
/*  998 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\"\"}" + 
/*  999 */           colourRecipeName(recipe, recipe.getName(), false) + (
/* 1000 */           !recipe.hasOneCooker() ? "label{text=\"\"}" : ("radio{group=\"sel\";id=\"6," + recipe
/* 1001 */           .getCookerId() + "\";text=\"\"}")) + "label{text=\"" + recipe
/* 1002 */           .getCookersAsString() + "\"};" + (
/* 1003 */           !recipe.hasOneContainer() ? "label{text=\"\"}" : ("radio{group=\"sel\";id=\"8," + recipe
/* 1004 */           .getContainerId() + "\";text=\"\"}")) + "label{text=\"" + recipe
/* 1005 */           .getContainersAsString() + "\"};label{text=\" \"};label{text=\"" + recipe
/* 1006 */           .getIngredientsAsString() + "\"};");
/*      */     }
/*      */     
/* 1009 */     buf.append("}");
/*      */     
/* 1011 */     if (recipes.length == 0)
/* 1012 */       buf.append("label{text=\"You dont know any heat recipes.\"}"); 
/* 1013 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendTimeRecipes() {
/* 1018 */     StringBuilder buf = new StringBuilder();
/* 1019 */     Recipe[] recipes = RecipesByPlayer.getTimeRecipesFor(getResponder().getWurmId());
/* 1020 */     int absSortBy = Math.abs(this.sortBy);
/* 1021 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1023 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1026 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1031 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/* 1032 */                   return -1;
/*      */                 }
/* 1034 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1039 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1044 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 2:
/* 1049 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1054 */                 return param1.getContainersAsString().compareTo(param2.getContainersAsString()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 3:
/* 1059 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1064 */                 return param1.getIngredientsAsString().compareTo(param2.getIngredientsAsString()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1070 */     buf.append("table{rows=\"1\";cols=\"6\";label{text=\" \"};" + 
/* 1071 */         colHeader("Recipe Name", 1, this.sortBy) + "label{text=\" \"};" + 
/* 1072 */         colHeader("Containers", 2, this.sortBy) + "label{text=\" \"};" + 
/* 1073 */         colHeader("Ingredients", 3, this.sortBy));
/*      */ 
/*      */     
/* 1076 */     for (Recipe recipe : recipes)
/*      */     {
/* 1078 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\" \"}" + 
/* 1079 */           colourRecipeName(recipe, recipe.getName(), false) + (
/* 1080 */           !recipe.hasOneContainer() ? "label{text=\" \"}" : ("radio{group=\"sel\";id=\"7," + recipe
/* 1081 */           .getContainerId() + "\";text=\"\"}")) + "label{text=\"" + recipe
/* 1082 */           .getContainersAsString() + "\"};label{text=\"\"};label{text=\"" + recipe
/*      */           
/* 1084 */           .getIngredientsAsString() + "\"};");
/*      */     }
/*      */     
/* 1087 */     buf.append("}");
/*      */     
/* 1089 */     if (recipes.length == 0)
/* 1090 */       buf.append("label{text=\"You dont know any time recipes.\"}"); 
/* 1091 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendCookersList() {
/* 1096 */     StringBuilder buf = new StringBuilder();
/* 1097 */     ItemTemplate[] templates = RecipesByPlayer.getKnownCookersFor(getResponder().getWurmId());
/* 1098 */     int absSortBy = Math.abs(this.sortBy);
/* 1099 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1101 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1104 */         Arrays.sort(templates, new Comparator<ItemTemplate>()
/*      */             {
/*      */               
/*      */               public int compare(ItemTemplate param1, ItemTemplate param2)
/*      */               {
/* 1109 */                 if (param1.getTemplateId() < param2.getTemplateId()) {
/* 1110 */                   return -1;
/*      */                 }
/* 1112 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1117 */         Arrays.sort(templates, new Comparator<ItemTemplate>()
/*      */             {
/*      */               
/*      */               public int compare(ItemTemplate param1, ItemTemplate param2)
/*      */               {
/* 1122 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1128 */     buf.append("table{rows=\"1\";cols=\"3\";label{text=\" \"};" + 
/* 1129 */         colHeader("Cooker Name", 1, this.sortBy) + "label{text=\" \"};");
/*      */ 
/*      */     
/* 1132 */     for (ItemTemplate itemTemplate : templates)
/*      */     {
/* 1134 */       buf.append("radio{group=\"sel\";id=\"6," + itemTemplate.getTemplateId() + "\";text=\" \"}label{text=\"" + itemTemplate
/* 1135 */           .getName() + (this.showExtra ? (" - " + itemTemplate
/* 1136 */           .getTemplateId()) : "") + "\"};label{text=\"\"};");
/*      */     }
/*      */     
/* 1139 */     buf.append("}");
/*      */     
/* 1141 */     if (templates.length == 0)
/* 1142 */       buf.append("label{text=\"You dont know any cookers.\"}"); 
/* 1143 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendCookerRecipes() {
/* 1148 */     StringBuilder buf = new StringBuilder();
/* 1149 */     Recipe[] recipes = RecipesByPlayer.getCookerRecipesFor(getResponder().getWurmId(), (int)this.target);
/* 1150 */     int absSortBy = Math.abs(this.sortBy);
/* 1151 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1153 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1156 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1161 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/* 1162 */                   return -1;
/*      */                 }
/* 1164 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1169 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1174 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1180 */     buf.append("table{rows=\"1\";cols=\"1\";" + colHeader("Recipe Name", 1, this.sortBy));
/* 1181 */     buf.append("table{rows=\"1\";cols=\"6\";");
/*      */     
/* 1183 */     for (Recipe recipe : recipes)
/*      */     {
/* 1185 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\" \"}" + 
/* 1186 */           colourRecipeName(recipe, recipe.getName(), false));
/*      */     }
/* 1188 */     int rem = recipes.length % 3;
/* 1189 */     if (rem > 0)
/* 1190 */       for (int i = 0; i < 3 - rem; i++)
/* 1191 */         buf.append("label{text=\"\"};label{text=\"\"}");  
/* 1192 */     buf.append("}");
/* 1193 */     buf.append("}");
/*      */     
/* 1195 */     if (recipes.length == 0)
/* 1196 */       buf.append("label{text=\"You dont know any recipes using that cooker.\"}"); 
/* 1197 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendContainersList() {
/* 1202 */     StringBuilder buf = new StringBuilder();
/* 1203 */     ItemTemplate[] templates = RecipesByPlayer.getKnownContainersFor(getResponder().getWurmId());
/* 1204 */     int absSortBy = Math.abs(this.sortBy);
/* 1205 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1207 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1210 */         Arrays.sort(templates, new Comparator<ItemTemplate>()
/*      */             {
/*      */               
/*      */               public int compare(ItemTemplate param1, ItemTemplate param2)
/*      */               {
/* 1215 */                 if (param1.getTemplateId() < param2.getTemplateId()) {
/* 1216 */                   return -1;
/*      */                 }
/* 1218 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1223 */         Arrays.sort(templates, new Comparator<ItemTemplate>()
/*      */             {
/*      */               
/*      */               public int compare(ItemTemplate param1, ItemTemplate param2)
/*      */               {
/* 1228 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1234 */     buf.append("table{rows=\"1\";cols=\"3\";label{text=\" \"};" + 
/* 1235 */         colHeader("Container Name", 1, this.sortBy) + "label{text=\" \"};");
/*      */ 
/*      */     
/* 1238 */     for (ItemTemplate itemTemplate : templates)
/*      */     {
/* 1240 */       buf.append("radio{group=\"sel\";id=\"8," + itemTemplate.getTemplateId() + "\";text=\"\"}label{text=\"" + itemTemplate
/* 1241 */           .getName() + (this.showExtra ? (" - " + itemTemplate
/* 1242 */           .getTemplateId()) : "") + "\"};label{text=\"\"};");
/*      */     }
/*      */     
/* 1245 */     buf.append("}");
/*      */     
/* 1247 */     if (templates.length == 0)
/* 1248 */       buf.append("label{text=\"You dont know any containers.\"}"); 
/* 1249 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendContainerRecipes() {
/* 1254 */     StringBuilder buf = new StringBuilder();
/* 1255 */     Recipe[] recipes = RecipesByPlayer.getContainerRecipesFor(getResponder().getWurmId(), (int)this.target);
/* 1256 */     int absSortBy = Math.abs(this.sortBy);
/* 1257 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1259 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1262 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1267 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/* 1268 */                   return -1;
/*      */                 }
/* 1270 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1275 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1280 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1286 */     buf.append("table{rows=\"1\";cols=\"1\";" + colHeader("Recipe Name", 1, this.sortBy));
/* 1287 */     buf.append("table{rows=\"1\";cols=\"6\";");
/*      */     
/* 1289 */     for (Recipe recipe : recipes)
/*      */     {
/* 1291 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\" \"}" + 
/* 1292 */           colourRecipeName(recipe, recipe.getName(), false));
/*      */     }
/* 1294 */     int rem = recipes.length % 3;
/* 1295 */     if (rem > 0)
/* 1296 */       for (int i = 0; i < 3 - rem; i++)
/* 1297 */         buf.append("label{text=\"\"};label{text=\"\"}");  
/* 1298 */     buf.append("}");
/* 1299 */     buf.append("}");
/*      */     
/* 1301 */     if (recipes.length == 0)
/* 1302 */       buf.append("label{text=\"You dont know any recipes using that container.\"}"); 
/* 1303 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendToolsList() {
/* 1308 */     StringBuilder buf = new StringBuilder();
/* 1309 */     ItemTemplate[] templates = RecipesByPlayer.getKnownToolsFor(getResponder().getWurmId());
/* 1310 */     int absSortBy = Math.abs(this.sortBy);
/* 1311 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1313 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1316 */         Arrays.sort(templates, new Comparator<ItemTemplate>()
/*      */             {
/*      */               
/*      */               public int compare(ItemTemplate param1, ItemTemplate param2)
/*      */               {
/* 1321 */                 if (param1.getTemplateId() < param2.getTemplateId()) {
/* 1322 */                   return -1;
/*      */                 }
/* 1324 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1329 */         Arrays.sort(templates, new Comparator<ItemTemplate>()
/*      */             {
/*      */               
/*      */               public int compare(ItemTemplate param1, ItemTemplate param2)
/*      */               {
/* 1334 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1340 */     buf.append("table{rows=\"1\";cols=\"3\";label{text=\" \"};" + 
/* 1341 */         colHeader("Tool Name", 1, this.sortBy) + "label{text=\" \"};");
/*      */ 
/*      */     
/* 1344 */     for (ItemTemplate itemTemplate : templates)
/*      */     {
/* 1346 */       buf.append("radio{group=\"sel\";id=\"10," + itemTemplate.getTemplateId() + "\";text=\"\"}label{text=\"" + itemTemplate
/* 1347 */           .getName() + (this.showExtra ? (" - " + itemTemplate
/* 1348 */           .getTemplateId()) : "") + "\"};label{text=\"\"};");
/*      */     }
/*      */     
/* 1351 */     buf.append("}");
/*      */     
/* 1353 */     if (templates.length == 0)
/* 1354 */       buf.append("label{text=\"You dont know any cooking tools.\"}"); 
/* 1355 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendToolRecipes() {
/* 1360 */     StringBuilder buf = new StringBuilder();
/* 1361 */     Recipe[] recipes = RecipesByPlayer.getToolRecipesFor(getResponder().getWurmId(), (int)this.target);
/* 1362 */     int absSortBy = Math.abs(this.sortBy);
/* 1363 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1365 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1368 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1373 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/* 1374 */                   return -1;
/*      */                 }
/* 1376 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1381 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1386 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/*      */     
/* 1392 */     buf.append("table{rows=\"1\";cols=\"1\";" + colHeader("Recipe Name", 1, this.sortBy));
/* 1393 */     buf.append("table{rows=\"1\";cols=\"6\";");
/*      */     
/* 1395 */     for (Recipe recipe : recipes)
/*      */     {
/* 1397 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\"\"}" + 
/* 1398 */           colourRecipeName(recipe, recipe.getName(), false));
/*      */     }
/* 1400 */     int rem = recipes.length % 3;
/* 1401 */     if (rem > 0)
/* 1402 */       for (int i = 0; i < 3 - rem; i++)
/* 1403 */         buf.append("label{text=\"\"};label{text=\"\"}");  
/* 1404 */     buf.append("}");
/* 1405 */     buf.append("}");
/*      */     
/* 1407 */     if (recipes.length == 0)
/* 1408 */       buf.append("label{text=\"You dont know any recipes using that tool.\"}"); 
/* 1409 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendIngredientsList() {
/* 1414 */     StringBuilder buf = new StringBuilder();
/* 1415 */     this.ingredients = RecipesByPlayer.getKnownIngredientsFor(getResponder().getWurmId());
/* 1416 */     int absSortBy = Math.abs(this.sortBy);
/* 1417 */     final int upDown = Integer.signum(this.sortBy);
/* 1418 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/*      */       case 1:
/* 1422 */         Arrays.sort(this.ingredients, new Comparator<Ingredient>()
/*      */             {
/*      */               
/*      */               public int compare(Ingredient param1, Ingredient param2)
/*      */               {
/* 1427 */                 return Recipes.getIngredientName(param1).compareTo(Recipes.getIngredientName(param2)) * upDown; }
/*      */             });
/*      */         break;
/*      */     } 
/* 1431 */     buf.append("table{rows=\"1\";cols=\"1\";" + 
/* 1432 */         colHeader("Ingredient Name", 1, this.sortBy));
/*      */     
/* 1434 */     buf.append("table{rows=\"1\";cols=\"9\";");
/*      */ 
/*      */     
/* 1437 */     for (Ingredient ingredient : this.ingredients)
/*      */     {
/* 1439 */       buf.append("radio{group=\"sel\";id=\"12," + ingredient
/* 1440 */           .getTemplateId() + "," + ingredient
/* 1441 */           .getCState() + "," + ingredient
/* 1442 */           .getPState() + "," + ingredient
/* 1443 */           .getMaterial() + "," + ingredient
/* 1444 */           .hasRealTemplate() + "," + ingredient
/* 1445 */           .getRealTemplateId() + "," + ingredient
/* 1446 */           .getCorpseData() + "\";text=\"\"}label{text=\"" + 
/*      */           
/* 1448 */           Recipes.getIngredientName(ingredient) + (this.showExtra ? (" - " + ingredient
/* 1449 */           .getTemplateId()) : "") + "\"};label{text=\"\"};");
/*      */     }
/*      */     
/* 1452 */     int rem = this.ingredients.length % 3;
/* 1453 */     if (rem > 0)
/* 1454 */       for (int i = 0; i < 3 - rem; i++)
/* 1455 */         buf.append("label{text=\"\"};label{text=\"\"}");  
/* 1456 */     buf.append("}");
/* 1457 */     buf.append("}");
/*      */     
/* 1459 */     if (this.ingredients.length == 0)
/* 1460 */       buf.append("label{text=\"You dont know any ingredients.\"}"); 
/* 1461 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendIngredientRecipes() {
/* 1466 */     StringBuilder buf = new StringBuilder();
/* 1467 */     Recipe[] recipes = RecipesByPlayer.getIngredientRecipesFor(getResponder().getWurmId(), this.ingred);
/* 1468 */     int absSortBy = Math.abs(this.sortBy);
/* 1469 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1471 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1474 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1479 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/* 1480 */                   return -1;
/*      */                 }
/* 1482 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1487 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1492 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/* 1497 */     buf.append("table{rows=\"1\";cols=\"1\";" + colHeader("Recipe Name", 1, this.sortBy));
/*      */     
/* 1499 */     buf.append("table{rows=\"1\";cols=\"6\";");
/*      */     
/* 1501 */     for (Recipe recipe : recipes)
/*      */     {
/* 1503 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\" \"}" + 
/* 1504 */           colourRecipeName(recipe, recipe.getName(), false));
/*      */     }
/* 1506 */     int rem = recipes.length % 3;
/* 1507 */     if (rem > 0)
/* 1508 */       for (int i = 0; i < 3 - rem; i++)
/* 1509 */         buf.append("label{text=\"\"};label{text=\"\"}");  
/* 1510 */     buf.append("}");
/* 1511 */     buf.append("}");
/*      */     
/* 1513 */     if (recipes.length == 0)
/* 1514 */       buf.append("label{text=\"You dont know any recipes using that ingredient.\"}"); 
/* 1515 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sendSearchRecipes() {
/* 1520 */     StringBuilder buf = new StringBuilder();
/* 1521 */     Recipe[] recipes = RecipesByPlayer.getSearchRecipesFor(getResponder().getWurmId(), this.searchFor);
/* 1522 */     int absSortBy = Math.abs(this.sortBy);
/* 1523 */     final int upDown = Integer.signum(this.sortBy);
/*      */     
/* 1525 */     switch (absSortBy) {
/*      */       
/*      */       case 0:
/* 1528 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1533 */                 if (param1.getRecipeId() < param2.getRecipeId()) {
/* 1534 */                   return -1;
/*      */                 }
/* 1536 */                 return 1;
/*      */               }
/*      */             });
/*      */         break;
/*      */       case 1:
/* 1541 */         Arrays.sort(recipes, new Comparator<Recipe>()
/*      */             {
/*      */               
/*      */               public int compare(Recipe param1, Recipe param2)
/*      */               {
/* 1546 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*      */               }
/*      */             });
/*      */         break;
/*      */     } 
/* 1551 */     buf.append("table{rows=\"1\";cols=\"1\";" + colHeader("Recipe Name", 1, this.sortBy));
/*      */     
/* 1553 */     buf.append("table{rows=\"1\";cols=\"6\";");
/*      */     
/* 1555 */     for (Recipe recipe : recipes)
/*      */     {
/* 1557 */       buf.append("radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\" \"}" + 
/* 1558 */           colourRecipeName(recipe, recipe.getName(), false));
/*      */     }
/* 1560 */     int rem = recipes.length % 3;
/* 1561 */     if (rem > 0)
/* 1562 */       for (int i = 0; i < 3 - rem; i++)
/* 1563 */         buf.append("label{text=\"\"};label{text=\"\"}");  
/* 1564 */     buf.append("}");
/* 1565 */     buf.append("}");
/*      */     
/* 1567 */     if (recipes.length == 0)
/* 1568 */       buf.append("label{text=\"You dont know any recipes using that ingredient.\"}"); 
/* 1569 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRecipe() {
/* 1574 */     if (RecipesByPlayer.isKnownRecipe(getResponder().getWurmId(), this.recip.getRecipeId())) {
/*      */       
/* 1576 */       this.ingredients = RecipesByPlayer.getRecipeIngredientsFor(getResponder().getWurmId(), this.recip.getRecipeId());
/*      */     }
/*      */     else {
/*      */       
/* 1580 */       Map<String, Ingredient> knownIngredients = this.recip.getAllIngredients(true);
/* 1581 */       this.ingredients = (Ingredient[])knownIngredients.values().toArray((Object[])new Ingredient[knownIngredients.size()]);
/*      */     } 
/*      */     
/* 1584 */     Arrays.sort(this.ingredients, new Comparator<Ingredient>()
/*      */         {
/*      */           
/*      */           public int compare(Ingredient param1, Ingredient param2)
/*      */           {
/* 1589 */             return Recipes.getIngredientName(param1).compareTo(Recipes.getIngredientName(param2));
/*      */           }
/*      */         });
/* 1592 */     Arrays.sort(this.ingredients, new Comparator<Ingredient>()
/*      */         {
/*      */           
/*      */           public int compare(Ingredient param1, Ingredient param2)
/*      */           {
/* 1597 */             return Byte.valueOf(param1.getGroupId()).compareTo(Byte.valueOf(param2.getGroupId()));
/*      */           }
/*      */         });
/* 1600 */     String name = colourRecipeName(this.recip, this.question, true);
/* 1601 */     StringBuilder buf = new StringBuilder();
/* 1602 */     String closeBtn = "harray{label{text=\" \"};harray{" + (this.showLinks ? "button{text=\"Back\";id=\"back\";hover=\"Go back to last screen.\"};" : "") + "label{text=\" \"};button{text=\"Close\";id=\"close\";hover=\"Close the cookbook.\"};};label{text=\" \"}};";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1611 */     buf.append("border{border{size=\"20,25\";null;null;" + name + closeBtn + "null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1620 */         getId() + "\"}");
/* 1621 */     buf.append("table{rows=\"1\";cols=\"5\";");
/*      */     
/* 1623 */     byte type = 1;
/* 1624 */     if (this.recip.isContainerActionType()) {
/* 1625 */       type = 2;
/* 1626 */     } else if (this.recip.isHeatType()) {
/* 1627 */       type = 3;
/* 1628 */     } else if (this.recip.isTimeType()) {
/* 1629 */       type = 4;
/* 1630 */     }  int lines = 7;
/* 1631 */     buf.append("label{text=\"Type:\"}");
/* 1632 */     buf.append(this.showLinks ? ("radio{group=\"sel\";id=\"" + type + "," + -10L + "\"}") : "label{text=\"\"}");
/* 1633 */     buf.append("label{text=\"" + this.recip.getTriggerName().toLowerCase() + "\"}");
/* 1634 */     buf.append("label{text=\"\"}");
/* 1635 */     buf.append("label{text=\"\"}");
/*      */     
/* 1637 */     if (this.recip.getSkillId() != -1) {
/*      */       
/* 1639 */       lines++;
/* 1640 */       buf.append("label{text=\"Skill:\"}");
/* 1641 */       buf.append("label{text=\"\"}");
/* 1642 */       buf.append("label{text=\"" + SkillSystem.getNameFor(this.recip.getSkillId()).toLowerCase() + "\"}");
/* 1643 */       buf.append("label{text=\"" + (this.showExtra ? (String)Integer.valueOf(this.recip.getSkillId()) : "") + "\"}");
/* 1644 */       buf.append("label{text=\"\"}");
/*      */     } 
/*      */     
/* 1647 */     if (this.recip.getActiveItem() != null) {
/*      */ 
/*      */       
/* 1650 */       lines++;
/* 1651 */       if (this.recip.getActiveItem().getTemplate().isCookingTool()) {
/*      */         
/* 1653 */         buf.append("label{text=\"Tool:\"}");
/* 1654 */         buf.append(this.showLinks ? ("radio{group=\"sel\";id=\"10," + this.recip.getActiveItem().getTemplateId() + "\"}") : "label{text=\"\"}");
/* 1655 */         buf.append("label{text=\"" + this.recip.getActiveItem().getName(false) + "\"}");
/* 1656 */         buf.append("label{text=\"" + (this.showExtra ? (String)Integer.valueOf(this.recip.getActiveItem().getTemplateId()) : "") + "\"}");
/* 1657 */         buf.append("label{text=\"\"}");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1662 */         Recipe recipe = Recipes.getRecipeByResult(this.recip.getActiveItem());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1667 */         String link = (recipe == null) ? "label{text=\" \"}" : (RecipesByPlayer.isKnownRecipe(getResponder().getWurmId(), recipe.getRecipeId()) ? ("harray{radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\"\"}" + colourRecipeName(recipe, "Show recipe", false) + "}") : ("label{color=\"255,127,127\";text=\"Unknown recipe" + (this.showExtra ? (" - " + recipe.getRecipeId()) : "") + "\"}"));
/* 1668 */         buf.append("label{text=\"Active Item:\"}");
/* 1669 */         buf.append(this.showLinks ? ("radio{group=\"sel\";id=\"12," + this.recip
/* 1670 */             .getActiveItem().getTemplateId() + "," + this.recip
/* 1671 */             .getActiveItem().getCState() + "," + this.recip
/* 1672 */             .getActiveItem().getPState() + "," + this.recip
/* 1673 */             .getActiveItem().getMaterial() + "," + this.recip
/* 1674 */             .getActiveItem().hasRealTemplate() + "," + this.recip
/* 1675 */             .getActiveItem().getRealTemplateId() + "," + this.recip
/* 1676 */             .getActiveItem().getCorpseData() + "\";text=\"\"}") : "label{text=\"\"}");
/*      */         
/* 1678 */         buf.append("label{text=\"" + Recipes.getIngredientName(this.recip.getActiveItem()) + "\"}");
/* 1679 */         buf.append("label{text=\"" + (this.showExtra ? (String)Integer.valueOf(this.recip.getActiveItem().getTemplateId()) : "") + "\"}");
/* 1680 */         buf.append(link);
/*      */       } 
/*      */     } 
/*      */     
/* 1684 */     if (this.recip.getTargetItem() != null) {
/*      */       
/* 1686 */       lines++;
/*      */       
/* 1688 */       Recipe recipe = Recipes.getRecipeByResult(this.recip.getTargetItem());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1693 */       String link = (recipe == null) ? "label{text=\" \"}" : (RecipesByPlayer.isKnownRecipe(getResponder().getWurmId(), recipe.getRecipeId()) ? ("harray{radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\"\"}" + colourRecipeName(recipe, "Show recipe", false) + "}") : ("label{color=\"255,127,127\";text=\"Unknown recipe" + (this.showExtra ? (" - " + recipe.getRecipeId()) : "") + "\"}"));
/* 1694 */       buf.append("label{text=\"Target Item:\"}");
/* 1695 */       buf.append(this.showLinks ? ("radio{group=\"sel\";id=\"12," + this.recip
/* 1696 */           .getTargetItem().getTemplateId() + "," + this.recip
/* 1697 */           .getTargetItem().getCState() + "," + this.recip
/* 1698 */           .getTargetItem().getPState() + "," + this.recip
/* 1699 */           .getTargetItem().getMaterial() + "," + this.recip
/* 1700 */           .getTargetItem().hasRealTemplate() + "," + this.recip
/* 1701 */           .getTargetItem().getRealTemplateId() + "," + this.recip
/* 1702 */           .getTargetItem().getCorpseData() + "\";text=\"\"}") : "label{text=\"\"}");
/*      */       
/* 1704 */       buf.append("label{text=\"" + Recipes.getIngredientName(this.recip.getTargetItem()) + "\"}");
/* 1705 */       buf.append("label{text=\"" + (this.showExtra ? (String)Integer.valueOf(this.recip.getTargetItem().getTemplateId()) : "") + "\"}");
/* 1706 */       buf.append(link);
/*      */     } 
/* 1708 */     if (this.recip.hasCooker()) {
/*      */       
/* 1710 */       String cooker = "Cooker:";
/* 1711 */       for (ItemTemplate template : this.recip.getCookerTemplates()) {
/*      */         
/* 1713 */         lines++;
/* 1714 */         buf.append("label{text=\"" + cooker + "\"}");
/* 1715 */         buf.append(this.showLinks ? ("radio{group=\"sel\";id=\"6," + template.getTemplateId() + "\"}") : "label{text=\"\"}");
/* 1716 */         buf.append("label{text=\"" + template.getName() + "\"}");
/* 1717 */         buf.append("label{text=\"" + (this.showExtra ? (String)Integer.valueOf(template.getTemplateId()) : "") + "\"}");
/* 1718 */         buf.append("label{text=\"\"}");
/* 1719 */         cooker = "";
/*      */       } 
/*      */     } 
/* 1722 */     if (this.recip.hasContainer()) {
/*      */       
/* 1724 */       String container = "Container:";
/* 1725 */       for (ItemTemplate template : this.recip.getContainerTemplates()) {
/*      */         
/* 1727 */         lines++;
/* 1728 */         buf.append("label{text=\"" + container + "\"}");
/* 1729 */         buf.append(this.showLinks ? ("radio{group=\"sel\";id=\"8," + template.getTemplateId() + "\"}") : "label{text=\"\"}");
/* 1730 */         buf.append("label{text=\"" + template.getName() + "\"}");
/* 1731 */         buf.append("label{text=\"" + (this.showExtra ? (String)Integer.valueOf(template.getTemplateId()) : "") + "\"}");
/* 1732 */         buf.append("label{text=\"\"}");
/* 1733 */         container = "";
/*      */       } 
/*      */     } 
/* 1736 */     if (this.ingredients.length > 0) {
/*      */       
/* 1738 */       byte gid = -5;
/* 1739 */       String strIngredient = "";
/* 1740 */       for (Ingredient ingredient : this.ingredients) {
/*      */ 
/*      */         
/* 1743 */         Recipe recipe = Recipes.getRecipeByResult(ingredient);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1748 */         String link = (recipe == null) ? "label{text=\" \"}" : (RecipesByPlayer.isKnownRecipe(getResponder().getWurmId(), recipe.getRecipeId()) ? ("harray{radio{group=\"sel\";id=\"13," + recipe.getRecipeId() + "\";text=\"\"}" + colourRecipeName(recipe, "Show recipe", false) + "}") : ("label{color=\"255,127,127\";text=\"Unknown recipe" + (this.showExtra ? (" - " + recipe.getRecipeId()) : "") + "\"}"));
/*      */         
/* 1750 */         if (ingredient.getGroupId() >= 0) {
/*      */           
/* 1752 */           if (gid == -5) {
/*      */             
/* 1754 */             lines++;
/* 1755 */             buf.append("label{type=\"bolditalic\";text=\"Ingredients:\"};label{text=\"\"};label{text=\"\"};label{text=\"\"}label{text=\"\"}");
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1761 */           if (gid < ingredient.getGroupId()) {
/*      */ 
/*      */             
/* 1764 */             gid = ingredient.getGroupId();
/* 1765 */             strIngredient = this.recip.getGroupById(gid).getGroupTypeName() + ":";
/*      */           } 
/* 1767 */           lines++;
/* 1768 */           buf.append("label{text=\"" + strIngredient + "\"}");
/* 1769 */           buf.append(this.showLinks ? ("radio{group=\"sel\";id=\"12," + ingredient
/* 1770 */               .getTemplateId() + "," + ingredient
/* 1771 */               .getCState() + "," + ingredient
/* 1772 */               .getPState() + "," + ingredient
/* 1773 */               .getMaterial() + "," + ingredient
/* 1774 */               .hasRealTemplate() + "," + ingredient
/* 1775 */               .getRealTemplateId() + "," + ingredient
/* 1776 */               .getCorpseData() + "\";text=\"\"}") : "label{text=\"\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1781 */           String amount = ((ingredient.isLiquid() && ingredient.getRatio() != 0) ? (" (ratio " + ingredient.getRatio() + "%)") : "") + ((ingredient.getLoss() > 0) ? (" (loss " + ingredient.getLoss() + "%)") : "");
/*      */           
/* 1783 */           buf.append("label{text=\"" + Recipes.getIngredientName(ingredient) + amount + "\"}");
/* 1784 */           buf.append("label{text=\"" + (this.showExtra ? (ingredient.getGroupId() + "," + ingredient
/* 1785 */               .getTemplateId() + "," + ingredient
/* 1786 */               .getCState() + "," + ingredient
/* 1787 */               .getPState() + "," + ingredient
/* 1788 */               .getRatio() + "," + ingredient
/* 1789 */               .getLoss() + "," + ingredient
/* 1790 */               .getMaterial() + "," + ingredient
/* 1791 */               .getRealTemplateId() + "," + ingredient
/* 1792 */               .getCorpseData()) : "") + "\"}");
/*      */           
/* 1794 */           buf.append(link);
/* 1795 */           strIngredient = "";
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1800 */     buf.append("}");
/*      */     
/* 1802 */     buf.append("radio{group=\"sel\";id=\"-10\";selected=\"true\";hidden=\"true\";text=\"None\"}");
/* 1803 */     buf.append("text{text=\"\"}");
/* 1804 */     boolean knownRecipe = false;
/* 1805 */     String mid = "null;";
/*      */     
/* 1807 */     if (getResponder().getPower() == 5) {
/* 1808 */       mid = "center{harray{button{text=\"Remove from Cookbook\";id=\"remove\";hover=\"Use with care\";confirm=\"Are you sure you want to do that?\";question=\"This will remove this recipe from your cookbook.\"};}};";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1816 */     if (Recipes.isKnownRecipe(this.recip.getRecipeId())) {
/*      */       
/* 1818 */       buf.append("label{color=\"127,255,127\"text=\"This recipe is known to everyone\"}");
/* 1819 */       lines++;
/* 1820 */       mid = "null;";
/*      */     } 
/*      */     
/* 1823 */     if (!this.showLinks) {
/*      */       
/* 1825 */       if (this.from.length() > 0) {
/*      */         
/* 1827 */         buf.append("harray{label{text=\"Signed:\"};label{type=\"italics\";text=\"" + this.from + "\"}}");
/*      */ 
/*      */ 
/*      */         
/* 1831 */         buf.append("text{text=\"\"}");
/* 1832 */         lines++;
/* 1833 */         lines++;
/*      */       } 
/* 1835 */       if (RecipesByPlayer.isKnownRecipe(getResponder().getWurmId(), this.recip.getRecipeId())) {
/*      */         
/* 1837 */         buf.append("label{type=\"bold\";color=\"255,127,127\"text=\"This recipe is already in your cookbook.\"}");
/* 1838 */         knownRecipe = true;
/*      */       } else {
/*      */         
/* 1841 */         buf.append("label{type=\"bold\";color=\"255,127,127\"text=\"To make this recipe it first must be put in your cookbook.\"}");
/* 1842 */       }  lines++;
/* 1843 */       mid = "null;";
/*      */     } 
/*      */     
/* 1846 */     buf.append("}};null;");
/* 1847 */     Item paper = null;
/* 1848 */     if (this.target != -10L) {
/*      */       
/*      */       try {
/*      */         
/* 1852 */         paper = Items.getItem(this.target);
/*      */       }
/* 1854 */       catch (NoSuchItemException e) {
/*      */         
/* 1856 */         logger.log(Level.WARNING, "Target (" + this.target + ") no longer exists!");
/*      */       } 
/*      */     }
/*      */     
/* 1860 */     if (this.showLinks) {
/* 1861 */       buf.append("border{size=\"20,20\";null;harray{label{text=\" \"};button{text=\"Show selected\";id=\"show\"}};" + mid + "harray{button{text=\"Go to info\";id=\"info\"};label{text=\" \"}};null;}");
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
/*      */     }
/* 1873 */     else if (this.target != -10L && !knownRecipe && paper != null && paper.getTopParent() == getResponder().getInventory().getWurmId()) {
/* 1874 */       buf.append("border{size=\"20,20\";null;null;center{harray{label{text=\" \"};button{text=\"Add to cookbook\";id=\"add\"}}};null;null;}");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1886 */       buf.append("null;");
/* 1887 */     }  buf.append("}");
/* 1888 */     int height = lines * 20;
/* 1889 */     getResponder().getCommunicator().sendBml(this.showExtra ? 470 : 400, height, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private String colourRecipeName(Recipe recipe, String name, boolean isBold) {
/* 1894 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1896 */     if (recipe.isKnown()) {
/*      */       
/* 1898 */       buf.append("label{" + (isBold ? "type=\"bold\";" : "") + "color=\"127,255,127\"" + "text=\"" + name.replace("\"", "''") + (this.showExtra ? (" - " + recipe
/* 1899 */           .getRecipeId()) : "") + "\"};");
/*      */     }
/*      */     else {
/*      */       
/* 1903 */       buf.append(nameColoredByRarity(name + (this.showExtra ? (" - " + recipe.getRecipeId()) : ""), "", recipe
/* 1904 */             .getLootableRarity(), isBold));
/*      */     } 
/* 1906 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public Recipe getRecipe() {
/* 1911 */     return this.recip;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getDisplayType() {
/* 1916 */     return this.displayType;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\CookBookQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
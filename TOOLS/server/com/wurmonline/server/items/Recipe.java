/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.players.Achievement;
/*      */ import com.wurmonline.server.players.AchievementTemplate;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Recipe
/*      */   implements MiscConstants
/*      */ {
/*   48 */   private static final Logger logger = Logger.getLogger(Recipe.class.getName());
/*      */   
/*      */   public static final byte TIME = 0;
/*      */   
/*      */   public static final byte HEAT = 1;
/*      */   
/*      */   public static final byte CREATE = 2;
/*      */   public static final short DEBUG_RECIPE = 0;
/*      */   private final String name;
/*      */   private final short recipeId;
/*      */   private boolean known = false;
/*      */   private boolean nameable = false;
/*   60 */   private String skillName = "";
/*   61 */   private int skillId = -1;
/*   62 */   private final Map<Short, String> cookers = new HashMap<>();
/*   63 */   private final Map<Short, Byte> cookersDif = new HashMap<>();
/*   64 */   private final Map<Short, String> containers = new HashMap<>();
/*   65 */   private final Map<Short, Byte> containersDif = new HashMap<>();
/*   66 */   private byte trigger = 2;
/*   67 */   private Ingredient activeItem = null;
/*   68 */   private Ingredient targetItem = null;
/*   69 */   private Ingredient resultItem = null;
/*   70 */   private final List<IngredientGroup> ingredientGroups = new ArrayList<>();
/*   71 */   private int achievementId = -1;
/*   72 */   private String achievementName = "";
/*   73 */   private final Map<Byte, Ingredient> allIngredients = new HashMap<>();
/*      */   private boolean lootable = false;
/*   75 */   private int lootableCreature = -10;
/*   76 */   private byte lootableRarity = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Recipe(String name, short recipeId) {
/*   87 */     this.name = name;
/*   88 */     this.recipeId = recipeId;
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
/*      */   public Recipe(short recipeId) {
/*  100 */     this.recipeId = recipeId;
/*      */     
/*  102 */     Recipe templateRecipe = Recipes.getRecipeById(this.recipeId);
/*  103 */     if (templateRecipe != null) {
/*      */       
/*  105 */       this.name = templateRecipe.name;
/*  106 */       setDefaults(templateRecipe);
/*      */     }
/*      */     else {
/*      */       
/*  110 */       this.name = "Null Recipe " + this.recipeId;
/*  111 */       logger.warning("Null recipe with ID: " + this.recipeId);
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
/*      */   public Recipe(DataInputStream dis) throws IOException, NoSuchTemplateException {
/*  124 */     this.recipeId = dis.readShort();
/*      */     
/*  126 */     Recipe templateRecipe = Recipes.getRecipeById(this.recipeId);
/*  127 */     if (templateRecipe != null) {
/*      */       
/*  129 */       this.name = templateRecipe.name;
/*  130 */       setDefaults(templateRecipe);
/*      */     }
/*      */     else {
/*      */       
/*  134 */       this.name = "Null Recipe " + this.recipeId;
/*      */     } 
/*      */     
/*  137 */     byte cookerCount = dis.readByte();
/*  138 */     if (cookerCount > 0)
/*      */     {
/*  140 */       for (int ic = 0; ic < cookerCount; ic++) {
/*      */         
/*  142 */         short cookerid = dis.readShort();
/*      */         
/*  144 */         addToCookerList(cookerid);
/*      */       } 
/*      */     }
/*  147 */     byte containerCount = dis.readByte();
/*  148 */     if (containerCount > 0)
/*      */     {
/*  150 */       for (int ic = 0; ic < containerCount; ic++) {
/*      */         
/*  152 */         short containerid = dis.readShort();
/*      */         
/*  154 */         addToContainerList(containerid);
/*      */       } 
/*      */     }
/*  157 */     boolean hasActiveItem = dis.readBoolean();
/*  158 */     if (hasActiveItem) {
/*  159 */       setActiveItem(new Ingredient(dis));
/*      */     }
/*  161 */     boolean hasTargetItem = dis.readBoolean();
/*  162 */     if (hasTargetItem) {
/*  163 */       setTargetItem(new Ingredient(dis));
/*      */     }
/*      */     
/*  166 */     byte groupCount = dis.readByte();
/*  167 */     if (groupCount > 0)
/*      */     {
/*  169 */       for (int ic = 0; ic < groupCount; ic++) {
/*      */         
/*  171 */         IngredientGroup ig = new IngredientGroup(dis);
/*  172 */         if (ig.size() > 0) {
/*  173 */           addToIngredientGroupList(ig);
/*      */         } else {
/*  175 */           logger.warning("recipe contains empty IngredientGroup: [" + this.recipeId + "] " + this.name);
/*      */         } 
/*  177 */         for (Ingredient i : ig.getIngredients())
/*      */         {
/*  179 */           this.allIngredients.put(Byte.valueOf(i.getIngredientId()), i);
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
/*      */ 
/*      */   
/*      */   public void pack(DataOutputStream dos) throws IOException {
/*  193 */     dos.writeShort(this.recipeId);
/*      */     
/*  195 */     dos.writeByte(this.cookers.size());
/*  196 */     for (Short cooker : this.cookers.keySet())
/*      */     {
/*  198 */       dos.writeShort(cooker.shortValue());
/*      */     }
/*      */     
/*  201 */     dos.writeByte(this.containers.size());
/*  202 */     for (Short container : this.containers.keySet())
/*      */     {
/*  204 */       dos.writeShort(container.shortValue());
/*      */     }
/*      */     
/*  207 */     dos.writeBoolean(hasActiveItem());
/*  208 */     if (hasActiveItem()) {
/*  209 */       this.activeItem.pack(dos);
/*      */     }
/*  211 */     dos.writeBoolean(hasTargetItem());
/*  212 */     if (hasTargetItem()) {
/*  213 */       this.targetItem.pack(dos);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  218 */     ArrayList<IngredientGroup> toSend = new ArrayList<>();
/*  219 */     for (IngredientGroup ig : this.ingredientGroups) {
/*      */       
/*  221 */       if (ig.size() > 0)
/*  222 */         toSend.add(ig); 
/*      */     } 
/*  224 */     dos.writeByte(toSend.size());
/*  225 */     for (IngredientGroup ig : toSend) {
/*  226 */       ig.pack(dos);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getRecipeName() {
/*  235 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  244 */     if (this.nameable) {
/*      */       
/*  246 */       String namer = Recipes.getRecipeNamer(this.recipeId);
/*  247 */       if (namer != null && namer.length() > 0)
/*      */       {
/*  249 */         return namer + "'s " + this.name;
/*      */       }
/*  251 */       return this.name + "+";
/*      */     } 
/*  253 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getRecipeId() {
/*  262 */     return this.recipeId;
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
/*      */   public byte getRecipeColourCode(long playerId) {
/*  278 */     int colour = 0;
/*  279 */     if (this.lootable)
/*  280 */       colour = this.lootableRarity; 
/*  281 */     if (isKnown())
/*  282 */       colour |= 0x4; 
/*  283 */     if (RecipesByPlayer.isFavourite(playerId, this.recipeId))
/*  284 */       colour |= 0x8; 
/*  285 */     if (!RecipesByPlayer.isKnownRecipe(playerId, this.recipeId))
/*  286 */       colour |= 0x10; 
/*  287 */     return (byte)colour;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getMenuId() {
/*  296 */     return (short)(this.recipeId + 8000);
/*      */   }
/*      */ 
/*      */   
/*      */   byte getCurrentGroupId() {
/*  301 */     return (byte)(this.ingredientGroups.size() - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLootable(int creatureId, byte rarity) {
/*  306 */     if (creatureId != -10) {
/*      */       
/*  308 */       this.lootable = true;
/*  309 */       this.lootableCreature = creatureId;
/*  310 */       this.lootableRarity = rarity;
/*      */     } else {
/*      */       
/*  313 */       this.lootable = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isLootable() {
/*  318 */     return this.lootable;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLootableCreature() {
/*  323 */     return this.lootableCreature;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getLootableRarity() {
/*  328 */     return this.lootableRarity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getIngredientCount() {
/*  337 */     return (byte)this.allIngredients.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIngredient(Ingredient ingredient) {
/*  347 */     byte gId = ingredient.getGroupId();
/*  348 */     if (gId == -3) {
/*      */ 
/*      */       
/*  351 */       setResultItem(ingredient);
/*      */     }
/*      */     else {
/*      */       
/*  355 */       Ingredient old = this.allIngredients.put(Byte.valueOf(ingredient.getIngredientId()), ingredient);
/*  356 */       if (old != null)
/*      */       {
/*  358 */         logger.info("Recipe (" + this.recipeId + ") Overridden Ingredient (" + old.getIngredientId() + ") group (" + gId + ") old:" + old
/*  359 */             .getName(true) + " new:" + ingredient.getName(true) + ".");
/*      */       }
/*  361 */       if (gId == -2) {
/*      */ 
/*      */         
/*  364 */         setActiveItem(ingredient);
/*      */       }
/*  366 */       else if (gId == -1) {
/*      */ 
/*      */         
/*  369 */         setTargetItem(ingredient);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  374 */         IngredientGroup ig = getGroupById(gId);
/*  375 */         if (ig != null) {
/*  376 */           ig.add(ingredient);
/*      */         } else {
/*  378 */           logger.log(Level.WARNING, "IngredientGroup is null for groupID: " + gId, new Exception());
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
/*      */   public Ingredient getIngredientById(byte ingredientId) {
/*  390 */     return this.allIngredients.get(Byte.valueOf(ingredientId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSubMenuName(Item container) {
/*  399 */     StringBuilder buf = new StringBuilder();
/*  400 */     if (this.resultItem.hasCState()) {
/*      */       
/*  402 */       buf.append(this.resultItem.getCStateName());
/*  403 */       if (this.resultItem.hasPState())
/*  404 */         buf.append(" " + this.resultItem.getPStateName()); 
/*  405 */       buf.append(" ");
/*      */     }
/*  407 */     else if (this.resultItem.hasPState() && this.resultItem.getPState() != 0) {
/*  408 */       buf.append(this.resultItem.getPStateName() + " ");
/*      */     } 
/*  410 */     buf.append(getResultName(container));
/*  411 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setKnown(boolean known) {
/*  420 */     this.known = known;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isKnown() {
/*  429 */     return this.known;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setNameable(boolean nameable) {
/*  438 */     this.nameable = nameable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNameable() {
/*  447 */     return this.nameable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkill(int skillId, String skillName) {
/*  457 */     this.skillName = skillName;
/*  458 */     this.skillId = skillId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSkillId() {
/*  469 */     return this.skillId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSkillName() {
/*  478 */     return this.skillName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTrigger(byte trigger) {
/*  488 */     this.trigger = trigger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getTrigger() {
/*  497 */     return this.trigger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDifficulty(Item target) {
/*  507 */     int diff = this.resultItem.getDifficulty();
/*  508 */     if (diff == -100)
/*      */     {
/*      */       
/*  511 */       diff = (int)getResultTemplate(target).getDifficulty();
/*      */     }
/*      */     
/*  514 */     if (target.isFoodMaker()) {
/*      */       
/*  516 */       for (IngredientGroup ig : this.ingredientGroups)
/*      */       {
/*  518 */         diff += ig.getGroupDifficulty();
/*      */       }
/*      */     }
/*  521 */     else if (hasTargetItem()) {
/*  522 */       diff += this.targetItem.getDifficulty();
/*      */     } 
/*  524 */     Item cooker = target.getTopParentOrNull();
/*  525 */     if (cooker != null) {
/*      */       
/*  527 */       Byte cookerDif = this.cookersDif.get(Short.valueOf((short)cooker.getTemplateId()));
/*      */       
/*  529 */       if (cookerDif != null) {
/*  530 */         diff += cookerDif.byteValue();
/*      */       }
/*      */     } 
/*  533 */     Byte containerDif = this.containersDif.get(Short.valueOf((short)target.getTemplateId()));
/*      */     
/*  535 */     if (containerDif != null)
/*  536 */       diff += containerDif.byteValue(); 
/*  537 */     return diff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToCookerList(int cookerTemplateId, String cookerName, int cookerDif) {
/*  548 */     this.cookers.put(Short.valueOf((short)cookerTemplateId), cookerName);
/*  549 */     this.cookersDif.put(Short.valueOf((short)cookerTemplateId), Byte.valueOf((byte)cookerDif));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToCookerList(int cookerTemplateId) {
/*  558 */     String name = "";
/*      */     
/*      */     try {
/*  561 */       ItemTemplate cookerIT = ItemTemplateFactory.getInstance().getTemplate(cookerTemplateId);
/*  562 */       name = cookerIT.getName();
/*      */     }
/*  564 */     catch (NoSuchTemplateException e) {
/*      */ 
/*      */       
/*  567 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     } 
/*  569 */     addToCookerList(cookerTemplateId, name, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isCooker(int cookerTemplateId) {
/*  579 */     return this.cookers.containsKey(Short.valueOf((short)cookerTemplateId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<ItemTemplate> getCookerTemplates() {
/*  588 */     Set<ItemTemplate> cookerTemplates = new HashSet<>();
/*  589 */     for (Short sc : this.cookers.keySet()) {
/*      */ 
/*      */       
/*      */       try {
/*  593 */         ItemTemplate cookerIT = ItemTemplateFactory.getInstance().getTemplate(sc.shortValue());
/*  594 */         cookerTemplates.add(cookerIT);
/*      */       }
/*  596 */       catch (NoSuchTemplateException noSuchTemplateException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  601 */     return cookerTemplates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToContainerList(int containerTemplateId, String containerName, int containerDif) {
/*  612 */     this.containers.put(Short.valueOf((short)containerTemplateId), containerName);
/*  613 */     this.containersDif.put(Short.valueOf((short)containerTemplateId), Byte.valueOf((byte)containerDif));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToContainerList(int containerTemplateId) {
/*  622 */     String name = "";
/*      */     
/*      */     try {
/*  625 */       ItemTemplate containerIT = ItemTemplateFactory.getInstance().getTemplate(containerTemplateId);
/*  626 */       name = containerIT.getName();
/*      */     }
/*  628 */     catch (NoSuchTemplateException e) {
/*      */ 
/*      */       
/*  631 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */     } 
/*  633 */     addToContainerList(containerTemplateId, name, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isContainer(int containerTemplateId) {
/*  642 */     return this.containers.containsKey(Short.valueOf((short)containerTemplateId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<ItemTemplate> getContainerTemplates() {
/*  651 */     Set<ItemTemplate> containerTemplates = new HashSet<>();
/*  652 */     for (Short sc : this.containers.keySet()) {
/*      */ 
/*      */       
/*      */       try {
/*  656 */         ItemTemplate cookerIT = ItemTemplateFactory.getInstance().getTemplate(sc.shortValue());
/*  657 */         containerTemplates.add(cookerIT);
/*      */       }
/*  659 */       catch (NoSuchTemplateException noSuchTemplateException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  664 */     return containerTemplates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Ingredient> getAllIngredients(boolean incActiveAndTargetItems) {
/*  674 */     Map<String, Ingredient> knownIngredients = new HashMap<>();
/*  675 */     for (Ingredient ingredient : this.allIngredients.values()) {
/*      */       
/*  677 */       if (ingredient.getGroupId() >= 0 || incActiveAndTargetItems)
/*      */       {
/*  679 */         if (!ingredient.getTemplate().isCookingTool())
/*  680 */           knownIngredients.put(ingredient.getName(true), ingredient); 
/*      */       }
/*      */     } 
/*  683 */     return knownIngredients;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setActiveItem(Ingredient ingredient) {
/*  692 */     this.activeItem = ingredient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Ingredient getActiveItem() {
/*  702 */     return this.activeItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasActiveItem() {
/*  711 */     return (this.activeItem != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isActiveItem(Item source) {
/*  722 */     if (this.activeItem.getTemplateId() == 14)
/*  723 */       return true; 
/*  724 */     if (!this.activeItem.checkFoodGroup(source))
/*  725 */       return false; 
/*  726 */     if (!this.activeItem.checkCorpseData(source))
/*  727 */       return false; 
/*  728 */     if (!this.activeItem.checkState(source))
/*  729 */       return false; 
/*  730 */     if (!this.activeItem.checkMaterial(source))
/*  731 */       return false; 
/*  732 */     if (!this.activeItem.checkRealTemplate(source))
/*  733 */       return false; 
/*  734 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getActiveItemName() {
/*  743 */     if (hasActiveItem())
/*  744 */       return this.activeItem.getName(false); 
/*  745 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTargetItem(Ingredient targetIngredient) {
/*  754 */     this.targetItem = targetIngredient;
/*  755 */     if (targetIngredient.getTemplateId() == 1173) {
/*  756 */       this.trigger = 2;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Ingredient getTargetItem() {
/*  765 */     return this.targetItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasTargetItem() {
/*  770 */     return (this.targetItem != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isTargetItem(Item target, boolean checkLiquids) {
/*  781 */     if (target.isFoodMaker()) {
/*      */ 
/*      */       
/*  784 */       for (Short ii : this.containers.keySet()) {
/*      */         
/*  786 */         if (ii.intValue() == target.getTemplateId())
/*      */         {
/*  788 */           return true;
/*      */         }
/*      */       } 
/*  791 */       return false;
/*      */     } 
/*  793 */     if (this.targetItem == null)
/*  794 */       return false; 
/*  795 */     if (!this.targetItem.checkFoodGroup(target))
/*  796 */       return false; 
/*  797 */     if (!this.targetItem.checkCorpseData(target))
/*  798 */       return false; 
/*  799 */     if (!this.targetItem.checkState(target))
/*  800 */       return false; 
/*  801 */     if (!this.targetItem.checkMaterial(target))
/*  802 */       return false; 
/*  803 */     if (!this.targetItem.checkRealTemplate(target))
/*  804 */       return false; 
/*  805 */     if (useResultTemplateWeight() && checkLiquids)
/*      */     {
/*      */       
/*  808 */       if (getTargetLossWeight(target) > target.getWeightGrams())
/*  809 */         return false; 
/*      */     }
/*  811 */     return true;
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
/*      */   public int getTargetLossWeight(Item target) {
/*  823 */     int loss = this.targetItem.getLoss();
/*  824 */     if (loss != 100) {
/*      */       
/*  826 */       int rWeight = (int)(this.resultItem.getTemplate().getWeightGrams() * 1.0F / (100 - loss) / 100.0F);
/*      */       
/*  828 */       return rWeight;
/*      */     } 
/*  830 */     return target.getWeightGrams();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTargetItemName() {
/*  839 */     if (hasTargetItem())
/*  840 */       return this.targetItem.getName(false); 
/*  841 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResultItem(Ingredient resultIngredient) {
/*  850 */     this.resultItem = resultIngredient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ingredient getResultItem() {
/*  858 */     return this.resultItem;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemTemplate getResultTemplate(Item container) {
/*  864 */     if (this.resultItem.isFoodGroup()) {
/*      */       
/*  866 */       Item item = findIngredient(container, this.resultItem);
/*  867 */       if (item != null)
/*  868 */         return item.getTemplate(); 
/*      */     } 
/*  870 */     return this.resultItem.getTemplate();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean useResultTemplateWeight() {
/*  875 */     return this.resultItem.useResultTemplateWeight();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getResultName(Item container) {
/*  884 */     String resultName = this.resultItem.getResultName();
/*  885 */     if (resultName.length() > 0)
/*      */     {
/*      */ 
/*      */       
/*  889 */       return doSubstituation(container, resultName);
/*      */     }
/*  891 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  893 */     if (this.resultItem.isFoodGroup()) {
/*      */       
/*  895 */       Item item = findIngredient(container, this.resultItem);
/*  896 */       if (item != null) {
/*  897 */         buf.append(item.getActualName());
/*      */       }
/*      */     } else {
/*      */       
/*  901 */       buf.append(this.resultItem.getTemplateName());
/*      */     } 
/*  903 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   String doSubstituation(Item container, String name) {
/*  908 */     String newName = name;
/*      */ 
/*      */     
/*  911 */     if (newName.indexOf('#') >= 0)
/*      */     {
/*      */ 
/*      */       
/*  915 */       if (this.resultItem.hasRealTemplateId() && this.resultItem.getRealItemTemplate() != null) {
/*  916 */         newName = newName.replace("#", this.resultItem.getRealItemTemplate().getName());
/*  917 */       } else if (this.resultItem.hasRealTemplateRef()) {
/*      */         
/*  919 */         ItemTemplate realTemplate = getResultRealTemplate(container);
/*      */         
/*  921 */         if (realTemplate != null) {
/*  922 */           newName = newName.replace("#", realTemplate.getName());
/*      */         } else {
/*  924 */           newName = newName.replace("# ", "").replace(" #", "");
/*      */         } 
/*      */       }  } 
/*  927 */     if (newName.indexOf('$') >= 0)
/*      */     {
/*      */       
/*  930 */       if (this.resultItem.hasMaterial()) {
/*  931 */         newName = newName.replace("$", this.resultItem.getMaterialName());
/*  932 */       } else if (this.resultItem.hasMaterialRef()) {
/*      */         
/*  934 */         byte material = getResultMaterial(container);
/*  935 */         newName = newName.replace("$", Materials.convertMaterialByteIntoString(material));
/*      */       } 
/*      */     }
/*  938 */     return newName.trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getResultName(Ingredient ingredient) {
/*  947 */     StringBuilder buf = new StringBuilder();
/*  948 */     String resultName = this.resultItem.getResultName();
/*  949 */     if (resultName.length() > 0) {
/*      */       
/*  951 */       if (this.resultItem.hasCState()) {
/*      */         
/*  953 */         buf.append(this.resultItem.getCStateName());
/*  954 */         if (this.resultItem.hasPState() && this.resultItem.getPStateName().length() > 0)
/*  955 */           buf.append(" " + this.resultItem.getPStateName()); 
/*  956 */         buf.append(" ");
/*      */       }
/*  958 */       else if (this.resultItem.hasPState() && this.resultItem.getPStateName().length() > 0) {
/*  959 */         buf.append(this.resultItem.getPStateName() + " ");
/*      */       } 
/*      */       
/*  962 */       if (resultName.indexOf('#') >= 0)
/*      */       {
/*  964 */         if (ingredient.getRealItemTemplate() != null) {
/*  965 */           resultName = resultName.replace("#", ingredient.getRealItemTemplate().getName().replace("any ", ""));
/*  966 */         } else if (this.resultItem.hasRealTemplateRef()) {
/*      */ 
/*      */           
/*  969 */           resultName = resultName.replace("# ", "").replace(" #", "");
/*      */         } 
/*      */       }
/*      */       
/*  973 */       if (resultName.indexOf('$') >= 0)
/*      */       {
/*  975 */         if (ingredient.hasMaterial()) {
/*  976 */           resultName = resultName.replace("$", ingredient.getMaterialName());
/*  977 */         } else if (this.resultItem.hasMaterialRef()) {
/*      */ 
/*      */           
/*  980 */           resultName = resultName.replace("$ ", "").replace(" $", "");
/*      */         } 
/*      */       }
/*  983 */       buf.append(resultName.trim());
/*  984 */       return buf.toString();
/*      */     } 
/*      */     
/*  987 */     buf.append(this.resultItem.getName(false));
/*  988 */     if (!this.resultItem.hasMaterial() && ingredient.hasMaterial())
/*  989 */       buf.append(" (" + ingredient.getMaterialName() + ")"); 
/*  990 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getResultNameWithGenus(Item container) {
/*  999 */     return StringUtilities.addGenus(getSubMenuName(container), container.isNamePlural());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasResultState() {
/* 1007 */     return this.resultItem.hasXState();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getResultState() {
/* 1015 */     return this.resultItem.getXState();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getResultMaterial(Item target) {
/* 1026 */     if (this.resultItem.hasMaterialRef()) {
/*      */ 
/*      */ 
/*      */       
/* 1030 */       if (this.targetItem != null && this.targetItem.getTemplateName().equalsIgnoreCase(this.resultItem.getMaterialRef())) {
/* 1031 */         return target.getMaterial();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1036 */       IngredientGroup group = getGroupByType(1);
/* 1037 */       if (group != null) {
/*      */         
/* 1039 */         Ingredient ingredient = group.getIngredientByName(this.resultItem.getMaterialRef());
/* 1040 */         if (ingredient != null && ingredient.getMaterial() != 0) {
/*      */ 
/*      */           
/* 1043 */           Item item = findIngredient(target, ingredient);
/* 1044 */           if (item != null) {
/* 1045 */             return item.getMaterial();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1050 */     if (this.resultItem.hasMaterial())
/* 1051 */       return this.resultItem.getMaterial(); 
/* 1052 */     return this.resultItem.getTemplate().getMaterial();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasDescription() {
/* 1060 */     return this.resultItem.hasResultDescription();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getResultDescription(Item container) {
/* 1068 */     return doSubstituation(container, this.resultItem.getResultDescription());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAchievements(Creature performer, Item newItem) {
/* 1074 */     if (this.achievementId != -1) {
/*      */       
/* 1076 */       AchievementTemplate at = Achievement.getTemplate(this.achievementId);
/* 1077 */       if (at != null)
/*      */       {
/* 1079 */         if (at.isInLiters()) {
/* 1080 */           performer.achievement(this.achievementId, newItem.getWeightGrams() / 1000);
/*      */         } else {
/* 1082 */           performer.achievement(this.achievementId);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addAchievementsOffline(long wurmId, Item newItem) {
/* 1089 */     if (this.achievementId != -1) {
/*      */       
/* 1091 */       AchievementTemplate at = Achievement.getTemplate(this.achievementId);
/* 1092 */       if (at != null)
/*      */       {
/* 1094 */         if (at.isInLiters()) {
/* 1095 */           Achievements.triggerAchievement(wurmId, this.achievementId, newItem.getWeightGrams() / 1000);
/*      */         } else {
/* 1097 */           Achievements.triggerAchievement(wurmId, this.achievementId);
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
/*      */   
/*      */   @Nullable
/*      */   public ItemTemplate getResultRealTemplate(Item target) {
/* 1111 */     if (this.resultItem.getRealTemplateRef().length() > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1118 */       if (hasOneContainer())
/*      */       {
/*      */         
/* 1121 */         for (Map.Entry<Short, String> container : this.containers.entrySet()) {
/*      */           
/* 1123 */           if (((String)container.getValue()).equalsIgnoreCase(this.resultItem.getRealTemplateRef()))
/*      */           {
/*      */             
/* 1126 */             return target.getRealTemplate();
/*      */           }
/*      */         } 
/*      */       }
/* 1130 */       if (this.targetItem != null && this.targetItem.getTemplateName().equalsIgnoreCase(this.resultItem.getRealTemplateRef())) {
/*      */         
/* 1132 */         ItemTemplate rit = target.getRealTemplate();
/* 1133 */         if (rit != null)
/* 1134 */           return rit; 
/* 1135 */         return target.getTemplate();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1141 */       IngredientGroup group = getGroupByType(1);
/* 1142 */       if (group != null) {
/*      */         
/* 1144 */         Ingredient ingredient = group.getIngredientByName(this.resultItem.getRealTemplateRef());
/* 1145 */         if (ingredient != null)
/*      */         {
/*      */           
/* 1148 */           Item item = findIngredient(target, ingredient);
/* 1149 */           if (item != null)
/*      */           {
/* 1151 */             ItemTemplate rit = item.getRealTemplate();
/* 1152 */             if (rit != null)
/* 1153 */               return rit; 
/* 1154 */             return item.getTemplate();
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       } 
/* 1160 */     } else if (this.resultItem.hasRealTemplate()) {
/*      */       
/* 1162 */       return this.resultItem.getRealItemTemplate();
/*      */     } 
/* 1164 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Item findIngredient(Item container, Ingredient ingredient) {
/* 1175 */     int foodGroup = ingredient.isFoodGroup() ? ingredient.getTemplateId() : 0;
/* 1176 */     if (container.isFoodMaker() || container.getTemplate().isCooker() || container.getTemplateId() == 1284) {
/*      */       
/* 1178 */       for (Item item : container.getItemsAsArray()) {
/*      */         
/* 1180 */         if (foodGroup > 0) {
/*      */ 
/*      */           
/* 1183 */           if (item.getTemplate().getFoodGroup() == foodGroup) {
/*      */ 
/*      */             
/* 1186 */             if (ingredient.hasRealTemplate())
/*      */             {
/* 1188 */               if (item.getRealTemplateId() != ingredient.getRealTemplateId())
/*      */                 continue; 
/*      */             }
/* 1191 */             if (ingredient.hasMaterial())
/*      */             {
/* 1193 */               if (item.getMaterial() != ingredient.getMaterial()) {
/*      */                 continue;
/*      */               }
/*      */             }
/* 1197 */             return item;
/*      */           }  continue;
/*      */         } 
/* 1200 */         if (item.getTemplateId() == ingredient.getTemplateId()) {
/*      */ 
/*      */           
/* 1203 */           if (ingredient.hasRealTemplate())
/*      */           {
/* 1205 */             if (item.getRealTemplateId() != ingredient.getRealTemplateId() && (item
/* 1206 */               .getRealTemplate() == null || item.getRealTemplate().getFoodGroup() != ingredient.getRealTemplateId()))
/*      */               continue; 
/*      */           }
/* 1209 */           if (ingredient.hasMaterial())
/*      */           {
/* 1211 */             if (item.getMaterial() != ingredient.getMaterial()) {
/*      */               continue;
/*      */             }
/*      */           }
/* 1215 */           return item;
/*      */         } 
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/* 1221 */     } else if (container.getTemplate().getFoodGroup() == foodGroup) {
/*      */ 
/*      */       
/* 1224 */       if (ingredient.hasRealTemplate())
/*      */       {
/* 1226 */         if (container.getRealTemplateId() != ingredient.getRealTemplateId())
/* 1227 */           return null; 
/*      */       }
/* 1229 */       if (ingredient.hasMaterial())
/*      */       {
/* 1231 */         if (container.getMaterial() != ingredient.getMaterial())
/* 1232 */           return null; 
/*      */       }
/* 1234 */       return container;
/*      */     } 
/*      */     
/* 1237 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Ingredient findMatchingIngredient(Item item) {
/* 1248 */     for (Ingredient ingredient : this.allIngredients.values()) {
/*      */       
/* 1250 */       if (ingredient.matches(item))
/* 1251 */         return ingredient; 
/*      */     } 
/* 1253 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isPartialMatch(Item container) {
/* 1263 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1266 */       System.out.println("isPartialMatch:" + getRecipeId() + " " + getTriggerName());
/*      */     }
/* 1268 */     if (hasTargetItem()) {
/*      */       
/* 1270 */       if (!isTargetItem(container, false)) {
/* 1271 */         return false;
/*      */       }
/* 1273 */     } else if (hasContainer()) {
/*      */       
/* 1275 */       if (!isContainer(container.getTemplateId())) {
/* 1276 */         return false;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1284 */     Item[] items = container.getItemsAsArray();
/* 1285 */     boolean[] founds = new boolean[items.length]; int x;
/* 1286 */     for (x = 0; x < founds.length; x++) {
/* 1287 */       founds[x] = false;
/*      */     }
/* 1289 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1292 */       System.out.println("isPartialMatch2:" + getRecipeId() + " " + getTriggerName());
/*      */     }
/*      */     
/* 1295 */     for (IngredientGroup ig : this.ingredientGroups) {
/*      */       
/* 1297 */       ig.clearFound();
/* 1298 */       for (int i = 0; i < items.length; i++) {
/*      */         
/* 1300 */         if (!founds[i] && ig.matches(items[i])) {
/* 1301 */           founds[i] = true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1306 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1309 */       System.out.println("isPartialMatch3:" + getRecipeId() + " " + getTriggerName());
/*      */     }
/*      */     
/* 1312 */     for (x = 0; x < items.length; x++) {
/*      */       
/* 1314 */       if (founds[x]) {
/*      */ 
/*      */         
/* 1317 */         Ingredient ingredient = findMatchingIngredient(items[x]);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1324 */         if (ingredient != null && !ingredient.wasFound(true, false)) {
/* 1325 */           return false;
/*      */         }
/*      */       } else {
/* 1328 */         return false;
/*      */       } 
/*      */     } 
/* 1331 */     for (IngredientGroup ig : this.ingredientGroups) {
/*      */ 
/*      */ 
/*      */       
/* 1335 */       if (ig.getGroupType() == 3 && ig.getFound(false) > 1)
/* 1336 */         return false; 
/* 1337 */       if (ig.getGroupType() == 2 && ig.getFound(false) > 1)
/* 1338 */         return false; 
/* 1339 */       if (ig.getGroupType() == 5 && !ig.wasFound()) {
/* 1340 */         return false;
/*      */       }
/*      */     } 
/* 1343 */     return true;
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
/*      */   public Ingredient[] getWhatsMissing() {
/* 1355 */     Set<Ingredient> ingredients = new HashSet<>();
/*      */     
/* 1357 */     for (IngredientGroup ig : this.ingredientGroups) {
/*      */ 
/*      */       
/* 1360 */       if (ig.getGroupType() == 1 || ig
/* 1361 */         .getGroupType() == 3 || ig
/* 1362 */         .getGroupType() == 4)
/*      */       {
/* 1364 */         if (!ig.wasFound())
/*      */         {
/*      */           
/* 1367 */           for (Ingredient ingredient : ig.getIngredients()) {
/*      */             
/* 1369 */             if (!ingredient.wasFound((ig.getGroupType() == 4), false))
/*      */             {
/*      */               
/* 1372 */               ingredients.add(ingredient);
/*      */             }
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/* 1378 */     return ingredients.<Ingredient>toArray(new Ingredient[ingredients.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToIngredientGroupList(IngredientGroup ingredientGroup) {
/* 1387 */     this.ingredientGroups.add(ingredientGroup);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaults(Recipe templateRecipe) {
/* 1396 */     for (IngredientGroup ig : templateRecipe.getGroups()) {
/*      */       
/* 1398 */       if (ig.size() > 0) {
/* 1399 */         addToIngredientGroupList(ig.clone());
/*      */       } else {
/* 1401 */         logger.warning("recipe contains empty IngredientGroup: [" + templateRecipe.recipeId + "] " + templateRecipe.name);
/*      */       } 
/*      */     } 
/* 1404 */     this.resultItem = templateRecipe.resultItem.clone(null);
/* 1405 */     this.lootable = templateRecipe.lootable;
/* 1406 */     this.nameable = templateRecipe.nameable;
/* 1407 */     this.lootableCreature = templateRecipe.lootableCreature;
/* 1408 */     this.lootableRarity = templateRecipe.lootableRarity;
/* 1409 */     this.trigger = templateRecipe.trigger;
/* 1410 */     this.skillId = templateRecipe.skillId;
/* 1411 */     this.skillName = templateRecipe.skillName;
/* 1412 */     this.achievementId = templateRecipe.achievementId;
/* 1413 */     this.achievementName = templateRecipe.achievementName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyGroupsFrom(Recipe recipe) {
/* 1422 */     for (IngredientGroup ig : recipe.getGroups())
/*      */     {
/* 1424 */       addToIngredientGroupList(ig.clone());
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
/*      */   @Nullable
/*      */   public IngredientGroup getGroupById(byte groupId) {
/*      */     try {
/* 1438 */       return this.ingredientGroups.get(groupId);
/*      */     }
/* 1440 */     catch (IndexOutOfBoundsException e) {
/*      */       
/* 1442 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public IngredientGroup getGroupByType(int groupType) {
/* 1454 */     for (IngredientGroup ig : this.ingredientGroups) {
/*      */       
/* 1456 */       if (ig.getGroupType() == groupType)
/* 1457 */         return ig; 
/*      */     } 
/* 1459 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public IngredientGroup[] getGroups() {
/* 1464 */     return this.ingredientGroups.<IngredientGroup>toArray(new IngredientGroup[this.ingredientGroups.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasCooker() {
/* 1469 */     return !this.cookers.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasCooker(int cookerId) {
/* 1474 */     return this.cookers.containsKey(Short.valueOf((short)cookerId));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasOneCooker() {
/* 1479 */     return (this.cookers.size() == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getCookerId() {
/* 1484 */     Iterator<Short> iterator = this.cookers.keySet().iterator(); if (iterator.hasNext()) { Short ss = iterator.next();
/*      */       
/* 1486 */       return ss.shortValue(); }
/*      */     
/* 1488 */     return -10;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasContainer() {
/* 1493 */     return !this.containers.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasOneContainer() {
/* 1498 */     return (this.containers.size() == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasContainer(int containerId) {
/* 1503 */     return this.containers.containsKey(Short.valueOf((short)containerId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasContainer(String containerName) {
/* 1510 */     for (Map.Entry<Short, String> container : this.containers.entrySet()) {
/*      */       
/* 1512 */       if (((String)container.getValue()).equalsIgnoreCase(containerName))
/*      */       {
/* 1514 */         return true;
/*      */       }
/*      */     } 
/* 1517 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public short getContainerId() {
/* 1522 */     Iterator<Short> iterator = this.containers.keySet().iterator(); if (iterator.hasNext()) { Short ss = iterator.next();
/*      */       
/* 1524 */       return ss.shortValue(); }
/*      */     
/* 1526 */     return -10;
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
/*      */   boolean checkIngredients(Item container) {
/* 1543 */     Item[] items = container.getItemsAsArray();
/* 1544 */     boolean[] founds = new boolean[items.length]; int x;
/* 1545 */     for (x = 0; x < founds.length; x++) {
/* 1546 */       founds[x] = false;
/*      */     }
/* 1548 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1551 */       System.out.println("checkIngredients:" + getRecipeId() + " " + getTriggerName());
/*      */     }
/* 1553 */     for (IngredientGroup ig : this.ingredientGroups) {
/*      */       
/* 1555 */       ig.clearFound();
/* 1556 */       for (int i = 0; i < items.length; i++) {
/*      */         
/* 1558 */         if (ig.matches(items[i]))
/* 1559 */           founds[i] = true; 
/*      */       } 
/*      */     } 
/* 1562 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1565 */       System.out.println("checkIngredients2:" + getRecipeId() + " " + getTriggerName());
/*      */     }
/*      */     
/* 1568 */     for (x = 0; x < founds.length; x++) {
/*      */       
/* 1570 */       if (!founds[x])
/* 1571 */         return false; 
/*      */     } 
/* 1573 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1576 */       System.out.println("checkIngredients3:" + getRecipeId() + " " + getTriggerName());
/*      */     }
/*      */     
/* 1579 */     for (IngredientGroup ig : this.ingredientGroups) {
/*      */       
/* 1581 */       if (!ig.wasFound())
/* 1582 */         return false; 
/*      */     } 
/* 1584 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1587 */       System.out.println("checkIngredients4:" + getRecipeId() + " " + getTriggerName());
/*      */     }
/* 1589 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getChanceFor(@Nullable Item activeItem, Item target, Creature performer) {
/* 1594 */     Skills skills = performer.getSkills();
/* 1595 */     Skill primSkill = null;
/* 1596 */     Skill secondarySkill = null;
/* 1597 */     double bonus = 0.0D;
/*      */     
/*      */     try {
/* 1600 */       primSkill = skills.getSkill(getSkillId());
/*      */ 
/*      */     
/*      */     }
/* 1604 */     catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1609 */       if (hasActiveItem() && activeItem != null && isActiveItem(activeItem)) {
/* 1610 */         secondarySkill = skills.getSkill(activeItem.getPrimarySkill());
/*      */       }
/* 1612 */     } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1617 */     if (secondarySkill != null)
/* 1618 */       bonus = Math.max(1.0D, secondarySkill.getKnowledge(activeItem, 0.0D) / 10.0D); 
/* 1619 */     float chance = 0.0F;
/* 1620 */     int diff = getDifficulty(target);
/* 1621 */     if (primSkill != null) {
/*      */       
/* 1623 */       chance = (float)primSkill.getChance(diff, activeItem, bonus);
/*      */     }
/*      */     else {
/*      */       
/* 1627 */       chance = (1 / (1 + diff) * 100);
/*      */     } 
/* 1629 */     return chance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setAchievementTriggered(int achievementId, String achievementName) {
/* 1639 */     this.achievementId = achievementId;
/* 1640 */     this.achievementName = achievementName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTriggerName() {
/* 1649 */     switch (this.trigger) {
/*      */       
/*      */       case 0:
/* 1652 */         return "Time";
/*      */       case 1:
/* 1654 */         return "Heat";
/*      */       case 2:
/* 1656 */         if (isTargetActionType())
/* 1657 */           return "Target Action"; 
/* 1658 */         if (isContainerActionType())
/* 1659 */           return "Container Action"; 
/* 1660 */         return "Create";
/*      */     } 
/* 1662 */     return "Unknown";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isRecipeOk(long playerId, @Nullable Item activeItem, Item target, boolean checkActive, boolean checkLiquids) {
/* 1669 */     if (getRecipeId() == 0)
/*      */     {
/*      */       
/* 1672 */       System.out.println("isRecipeOk:" + getRecipeId() + " " + checkActive + " " + getTriggerName() + "(" + target.getName() + ")");
/*      */     }
/* 1674 */     if (playerId != -10L && isLootable() && !RecipesByPlayer.isKnownRecipe(playerId, this.recipeId))
/*      */     {
/*      */       
/* 1677 */       return false;
/*      */     }
/*      */     
/* 1680 */     if (checkActive && activeItem != null && getActiveItem() != null) {
/*      */ 
/*      */       
/* 1683 */       if (!isActiveItem(activeItem)) {
/* 1684 */         return false;
/*      */       }
/* 1686 */       if (checkLiquids && activeItem.isLiquid()) {
/*      */         
/* 1688 */         int weightNeeded = getUsedActiveItemWeightGrams(activeItem, target);
/*      */         
/* 1690 */         if (activeItem.getWeightGrams() < weightNeeded) {
/* 1691 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1696 */     if (this.targetItem != null && !isTargetItem(target, checkLiquids)) {
/* 1697 */       return false;
/*      */     }
/* 1699 */     if (this.trigger == 1 && checkActive) {
/*      */       
/* 1701 */       Item cooker = target.getTopParentOrNull();
/* 1702 */       if (cooker == null)
/* 1703 */         return false; 
/* 1704 */       if (!isCooker((short)cooker.getTemplateId())) {
/* 1705 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1709 */     if (this.targetItem == null) {
/*      */       
/* 1711 */       if (hasContainer()) {
/*      */         
/* 1713 */         if (!isContainer((short)target.getTemplateId())) {
/* 1714 */           return false;
/*      */         }
/* 1716 */       } else if (hasCooker()) {
/*      */ 
/*      */ 
/*      */         
/* 1720 */         if (!isCooker((short)target.getTemplateId())) {
/* 1721 */           return false;
/*      */         }
/*      */       } 
/* 1724 */     } else if (this.trigger == 1 && checkActive) {
/*      */ 
/*      */       
/* 1727 */       Item cooker = target.getTopParentOrNull();
/* 1728 */       Item parent = target.getParentOrNull();
/* 1729 */       if (cooker == null || parent == null)
/* 1730 */         return false; 
/* 1731 */       if (cooker.getTemplateId() != parent.getTemplateId())
/* 1732 */         return false; 
/* 1733 */       if (hasContainer())
/*      */       {
/*      */         
/* 1736 */         if (!isContainer((short)parent.getTemplateId())) {
/* 1737 */           return false;
/*      */         }
/*      */       }
/*      */     } 
/* 1741 */     if (target.isFoodMaker() || target.getTemplate().isCooker() || (target.isRecipeItem() && target.isHollow())) {
/*      */       
/* 1743 */       if (getRecipeId() == 0)
/*      */       {
/*      */         
/* 1746 */         System.out.println("isRecipeOk2:" + getRecipeId() + " " + checkActive);
/*      */       }
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
/* 1764 */       if (!checkIngredients(target))
/* 1765 */         return false; 
/* 1766 */       if (getRecipeId() == 0)
/*      */       {
/*      */         
/* 1769 */         System.out.println("isRecipeOk3:" + getRecipeId() + " " + checkActive);
/*      */       }
/*      */       
/* 1772 */       if (checkLiquids && !getNewWeightGrams(target).isSuccess())
/* 1773 */         return false; 
/* 1774 */       return true;
/*      */     } 
/*      */     
/* 1777 */     int needed = (getActiveItem() != null) ? 2 : 1;
/* 1778 */     if (this.allIngredients.size() != needed)
/* 1779 */       return false; 
/* 1780 */     for (Ingredient ingredient : this.allIngredients.values()) {
/*      */       
/* 1782 */       if (ingredient.matches(target))
/* 1783 */         return true; 
/*      */     } 
/* 1785 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUsedActiveItemWeightGrams(Item source, Item target) {
/* 1790 */     int rat = (getActiveItem() != null) ? getActiveItem().getRatio() : 0;
/* 1791 */     if (source.isLiquid() && rat != 0)
/*      */     {
/*      */       
/* 1794 */       return target.getWeightGrams() * rat / 100;
/*      */     }
/* 1796 */     return source.getWeightGrams();
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
/*      */   public LiquidResult getNewWeightGrams(Item container) {
/* 1811 */     LiquidResult liquidResult = new LiquidResult();
/* 1812 */     Map<Short, Liquid> liquids = new HashMap<>();
/*      */     
/* 1814 */     for (Ingredient in : getAllIngredients(true).values()) {
/*      */       
/* 1816 */       if (in.getTemplate().isLiquid()) {
/*      */         
/* 1818 */         short id = (short)in.getTemplateId();
/* 1819 */         int ratio = in.getRatio();
/* 1820 */         String name = Recipes.getIngredientName(in, false);
/* 1821 */         int loss = in.getLoss();
/* 1822 */         liquids.put(Short.valueOf(id), new Liquid(id, name, ratio, loss));
/*      */       } 
/*      */     } 
/*      */     
/* 1826 */     int solidWeight = 0;
/* 1827 */     for (Item item : container.getItemsAsArray()) {
/*      */       
/* 1829 */       if (item.isLiquid()) {
/*      */         
/* 1831 */         short id = (short)item.getTemplateId();
/* 1832 */         int liquidWeight = item.getWeightGrams();
/* 1833 */         Liquid liquid = liquids.get(Short.valueOf(id));
/* 1834 */         if (liquid == null) {
/*      */ 
/*      */           
/* 1837 */           short fgid = (short)item.getTemplate().getFoodGroup();
/* 1838 */           liquid = liquids.get(Short.valueOf(fgid));
/*      */         } 
/*      */         
/* 1841 */         if (liquid != null) {
/*      */ 
/*      */           
/* 1844 */           if (liquid.getRatio() != 0) {
/* 1845 */             liquid.setWeight(liquidWeight);
/*      */           }
/*      */         } else {
/*      */           
/* 1849 */           logger.info("Liquid Item " + item.getName() + " missing ingredient?");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1854 */         solidWeight += item.getWeightGrams();
/*      */       } 
/*      */     } 
/* 1857 */     int newWeight = solidWeight;
/*      */     
/* 1859 */     for (Liquid liquid : liquids.values()) {
/*      */ 
/*      */       
/* 1862 */       if (liquid.getWeight() > 0) {
/*      */ 
/*      */         
/* 1865 */         int neededWeight = solidWeight * liquid.getRatio() / 100;
/* 1866 */         int minLiquid = (int)(neededWeight * 0.8D);
/* 1867 */         int maxLiquid = (int)(neededWeight * 1.2D);
/* 1868 */         if (liquid.getWeight() < minLiquid) {
/*      */           
/* 1870 */           liquidResult.add(liquid.getId(), "not enough " + liquid.getName() + ", looks like it should use between " + minLiquid + " and " + maxLiquid + " grams.");
/*      */         
/*      */         }
/* 1873 */         else if (liquid.getWeight() > maxLiquid) {
/*      */           
/* 1875 */           liquidResult.add(liquid.getId(), "too much " + liquid.getName() + ", looks like it should use between " + minLiquid + " and " + maxLiquid + " grams.");
/*      */         } 
/*      */         
/* 1878 */         newWeight += liquid.getWeight() * (100 - liquid.getLoss());
/*      */       } 
/*      */     } 
/* 1881 */     liquidResult.setNewWeight(newWeight);
/* 1882 */     return liquidResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTargetActionType() {
/* 1891 */     return (this.trigger == 2 && this.containers.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isContainerActionType() {
/* 1900 */     return (this.trigger == 2 && !this.containers.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHeatType() {
/* 1909 */     return (this.trigger == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTimeType() {
/* 1918 */     return (this.trigger == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getCookers() {
/* 1927 */     List<String> cookerList = new ArrayList<>();
/* 1928 */     for (String cooker : this.cookers.values())
/*      */     {
/* 1930 */       cookerList.add(cooker);
/*      */     }
/* 1932 */     return cookerList.<String>toArray(new String[cookerList.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCookersAsString() {
/* 1940 */     StringBuilder buf = new StringBuilder();
/* 1941 */     boolean first = true;
/* 1942 */     for (String s : this.cookers.values()) {
/*      */       
/* 1944 */       if (first) {
/* 1945 */         first = false;
/*      */       } else {
/* 1947 */         buf.append(",");
/* 1948 */       }  buf.append(s);
/*      */     } 
/* 1950 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getContainers() {
/* 1959 */     List<String> containerList = new ArrayList<>();
/* 1960 */     for (String container : this.containers.values())
/*      */     {
/* 1962 */       containerList.add(container);
/*      */     }
/* 1964 */     return containerList.<String>toArray(new String[containerList.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getContainersAsString() {
/* 1972 */     StringBuilder buf = new StringBuilder();
/* 1973 */     boolean first = true;
/* 1974 */     for (String s : this.containers.values()) {
/*      */       
/* 1976 */       if (first) {
/* 1977 */         first = false;
/*      */       } else {
/* 1979 */         buf.append(",");
/* 1980 */       }  buf.append(s);
/*      */     } 
/* 1982 */     return buf.toString();
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
/*      */   boolean matchesResult(Ingredient ingredient, boolean exactOnly) {
/* 2000 */     if (this.resultItem.getTemplateId() == ingredient.getTemplateId()) {
/*      */ 
/*      */       
/* 2003 */       boolean ok = (!this.resultItem.hasCState() && !ingredient.hasCState());
/* 2004 */       if (!ok)
/* 2005 */         ok = (this.resultItem.hasCState() && ingredient.hasCState() && this.resultItem.getCState() == ingredient.getCState()); 
/* 2006 */       if (!ok)
/* 2007 */         ok = (exactOnly && !ingredient.hasCState() && this.resultItem.hasCState()); 
/* 2008 */       if (!ok) {
/* 2009 */         return false;
/*      */       }
/* 2011 */       ok = (!this.resultItem.hasPState() && !ingredient.hasPState());
/* 2012 */       if (!ok)
/* 2013 */         ok = (this.resultItem.hasPState() && ingredient.hasPState() && this.resultItem.getPState() == ingredient.getPState()); 
/* 2014 */       if (!ok)
/* 2015 */         ok = (exactOnly && !ingredient.hasPState() && this.resultItem.hasPState()); 
/* 2016 */       if (!ok) {
/* 2017 */         return false;
/*      */       }
/* 2019 */       if (ingredient.hasRealTemplate())
/*      */       {
/*      */         
/* 2022 */         if (this.resultItem.hasRealTemplate()) {
/*      */           
/* 2024 */           if (this.resultItem.getRealTemplateId() != ingredient.getRealTemplateId()) {
/*      */             
/* 2026 */             if (exactOnly) {
/* 2027 */               return false;
/*      */             }
/*      */             
/* 2030 */             if (this.resultItem.getRealItemTemplate() != null && ingredient.getRealItemTemplate() != null) {
/*      */               
/* 2032 */               if (this.resultItem.getRealItemTemplate().isFoodGroup() && this.resultItem
/* 2033 */                 .getRealItemTemplate().getFoodGroup() != ingredient.getRealItemTemplate().getFoodGroup())
/* 2034 */                 return false; 
/* 2035 */               if (ingredient.getRealItemTemplate().isFoodGroup() && this.resultItem
/* 2036 */                 .getRealItemTemplate().getFoodGroup() != ingredient.getRealItemTemplate().getFoodGroup()) {
/* 2037 */                 return false;
/*      */               }
/*      */             } else {
/* 2040 */               return false;
/*      */             }
/*      */           
/*      */           } 
/* 2044 */         } else if (this.resultItem.hasRealTemplateRef()) {
/*      */ 
/*      */           
/* 2047 */           boolean match = false;
/*      */           
/* 2049 */           if (hasTargetItem())
/*      */           {
/* 2051 */             if (this.targetItem.getTemplateName().equalsIgnoreCase(this.resultItem.getRealTemplateRef())) {
/*      */               
/* 2053 */               Ingredient refingredient = this.targetItem;
/* 2054 */               if (ingredient.getRealItemTemplate() == null) {
/*      */                 
/* 2056 */                 if (refingredient.getTemplate() != null) {
/* 2057 */                   return false;
/*      */                 }
/* 2059 */                 match = true;
/*      */               }
/* 2061 */               else if (refingredient.getTemplateId() == ingredient.getRealItemTemplate().getTemplateId()) {
/*      */                 
/* 2063 */                 match = true;
/*      */               }
/* 2065 */               else if (!exactOnly) {
/*      */                 
/* 2067 */                 if (refingredient.getTemplate().getFoodGroup() == ingredient.getRealItemTemplate().getFoodGroup() || (refingredient
/* 2068 */                   .getTemplateId() == 369 && ingredient
/* 2069 */                   .getRealItemTemplate().getFoodGroup() == 1201))
/*      */                 {
/* 2071 */                   match = true;
/*      */                 }
/*      */               } else {
/*      */                 
/* 2075 */                 return false;
/*      */               } 
/*      */             }  } 
/* 2078 */           if (!match) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2084 */             IngredientGroup group = getGroupByType(1);
/* 2085 */             if (group != null) {
/*      */               
/* 2087 */               Ingredient refingredient = group.getIngredientByName(this.resultItem.getRealTemplateRef());
/*      */               
/* 2089 */               if (refingredient != null) {
/*      */ 
/*      */                 
/* 2092 */                 if (ingredient.getRealItemTemplate() == null) {
/*      */                   
/* 2094 */                   if (refingredient.getTemplate() != null) {
/* 2095 */                     return false;
/*      */                   }
/* 2097 */                   match = true;
/*      */ 
/*      */                 
/*      */                 }
/* 2101 */                 else if (!refingredient.hasRealTemplateId()) {
/*      */ 
/*      */                   
/* 2104 */                   if (exactOnly) {
/* 2105 */                     return false;
/*      */                   }
/* 2107 */                   if (refingredient.getTemplate().getFoodGroup() == ingredient.getRealItemTemplate().getFoodGroup()) {
/* 2108 */                     match = true;
/*      */                   
/*      */                   }
/*      */                   else {
/*      */                     
/* 2113 */                     Recipe[] ning = Recipes.getRecipesByResult(new Ingredient(refingredient.getTemplate(), false, refingredient.getGroupId()));
/* 2114 */                     if (ning == null || ning.length == 0) {
/* 2115 */                       return false;
/*      */                     }
/*      */                   } 
/* 2118 */                 } else if (refingredient.getTemplateId() == ingredient.getRealItemTemplate().getTemplateId()) {
/*      */                   
/* 2120 */                   match = true;
/*      */                 }
/* 2122 */                 else if (!exactOnly) {
/*      */                   
/* 2124 */                   if (refingredient.getTemplate().getFoodGroup() == ingredient.getRealItemTemplate().getFoodGroup() || (refingredient
/* 2125 */                     .getTemplateId() == 369 && ingredient
/* 2126 */                     .getRealItemTemplate().getFoodGroup() == 1201))
/*      */                   {
/* 2128 */                     match = true;
/*      */                   }
/*      */                 } else {
/*      */                   
/* 2132 */                   return false;
/*      */                 } 
/*      */               } else {
/*      */                 
/* 2136 */                 return false;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 2141 */               return false;
/*      */             }
/*      */           
/*      */           } 
/*      */         } else {
/*      */           
/* 2147 */           return false;
/*      */         } 
/*      */       }
/* 2150 */       if (ingredient.hasMaterial() && this.resultItem.hasMaterial())
/*      */       {
/* 2152 */         if (ingredient.getMaterial() != this.resultItem.getMaterial())
/* 2153 */           return false; 
/*      */       }
/* 2155 */       if (ingredient.hasMaterial() && this.resultItem.hasMaterialRef())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2161 */         if (this.targetItem != null) {
/*      */ 
/*      */           
/* 2164 */           if (!isInMaterialGroup(this.targetItem.getTemplateId(), ingredient.getMaterial())) {
/* 2165 */             return false;
/*      */           }
/*      */         } else {
/*      */           
/* 2169 */           IngredientGroup group = getGroupByType(1);
/* 2170 */           if (group != null) {
/*      */             
/* 2172 */             Ingredient refingredient = group.getIngredientByName(this.resultItem.getMaterialRef());
/*      */             
/* 2174 */             if (refingredient != null) {
/*      */ 
/*      */ 
/*      */               
/* 2178 */               if (!isInMaterialGroup(refingredient.getTemplateId(), ingredient.getMaterial())) {
/* 2179 */                 return false;
/*      */               }
/*      */             } else {
/* 2182 */               return false;
/*      */             } 
/*      */           } else {
/* 2185 */             return false;
/*      */           } 
/*      */         }  } 
/* 2188 */       return true;
/*      */     } 
/*      */     
/* 2191 */     if (this.resultItem.getTemplate().isFoodGroup()) {
/*      */ 
/*      */ 
/*      */       
/* 2195 */       if (this.targetItem != null) {
/*      */ 
/*      */         
/* 2198 */         if (!exactOnly || this.targetItem.getTemplate().getFoodGroup() != ingredient.getTemplate().getFoodGroup()) {
/* 2199 */           return false;
/*      */         }
/*      */       } else {
/* 2202 */         return false;
/* 2203 */       }  if (ingredient.hasCState() && this.resultItem.hasCState() && this.resultItem.getCState() != ingredient.getCState())
/* 2204 */         return false; 
/* 2205 */       if (ingredient.hasPState() && this.resultItem.hasPState() && this.resultItem.getPState() != ingredient.getPState())
/* 2206 */         return false; 
/* 2207 */       return true;
/*      */     } 
/* 2209 */     if (!exactOnly && ingredient.getTemplate().isFoodGroup()) {
/*      */ 
/*      */ 
/*      */       
/* 2213 */       if (this.resultItem.getTemplate().getFoodGroup() != ingredient.getTemplateId())
/* 2214 */         return false; 
/* 2215 */       if (this.resultItem.hasCState() && this.resultItem.getCState() != ingredient.getCState())
/* 2216 */         return false; 
/* 2217 */       if (this.resultItem.hasPState() && this.resultItem.getPState() != ingredient.getPState())
/* 2218 */         return false; 
/* 2219 */       return true;
/*      */     } 
/* 2221 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isInMaterialGroup(int templateGroup, byte material) {
/* 2226 */     switch (templateGroup) {
/*      */ 
/*      */       
/*      */       case 1261:
/* 2230 */         switch (material) {
/*      */           
/*      */           case 2:
/*      */           case 72:
/*      */           case 73:
/*      */           case 74:
/*      */           case 75:
/*      */           case 76:
/*      */           case 77:
/*      */           case 78:
/*      */           case 79:
/*      */           case 80:
/*      */           case 81:
/*      */           case 82:
/*      */           case 83:
/*      */           case 84:
/*      */           case 85:
/*      */           case 86:
/*      */           case 87:
/* 2249 */             return true;
/*      */         } 
/* 2251 */         return false;
/*      */ 
/*      */       
/*      */       case 200:
/*      */       case 201:
/*      */       case 1157:
/* 2257 */         switch (material) {
/*      */           
/*      */           case 3:
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/* 2263 */             return true;
/*      */         } 
/* 2265 */         return false;
/*      */     } 
/*      */     
/* 2268 */     return false;
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
/*      */   public String getIngredientsAsString() {
/* 2282 */     StringBuilder buf = new StringBuilder();
/* 2283 */     byte groupId = -1;
/* 2284 */     IngredientGroup group = null;
/* 2285 */     for (Ingredient ingredient : this.allIngredients.values()) {
/*      */ 
/*      */       
/* 2288 */       group = getGroupById(ingredient.getGroupId());
/* 2289 */       if (group != null && group.getGroupType() > 0) {
/*      */         
/* 2291 */         byte newGroupId = ingredient.getGroupId();
/* 2292 */         if (groupId != newGroupId) {
/*      */           IngredientGroup oldGroup;
/*      */ 
/*      */           
/* 2296 */           if (groupId > -1 && (oldGroup = getGroupById(groupId)) != null) {
/*      */             
/* 2298 */             switch (oldGroup.getGroupType()) {
/*      */               
/*      */               case 3:
/* 2301 */                 buf.append(")");
/*      */                 break;
/*      */               case 4:
/* 2304 */                 buf.append(")+");
/*      */                 break;
/*      */               case 2:
/* 2307 */                 buf.append("]");
/*      */                 break;
/*      */             } 
/* 2310 */             buf.append(",");
/*      */           } 
/*      */           
/* 2313 */           switch (group.getGroupType()) {
/*      */             
/*      */             case 5:
/* 2316 */               buf.append("[");
/*      */               break;
/*      */             case 3:
/* 2319 */               buf.append("(");
/*      */               break;
/*      */             case 4:
/* 2322 */               buf.append("(");
/*      */               break;
/*      */             case 2:
/* 2325 */               buf.append("[");
/*      */               break;
/*      */           } 
/*      */ 
/*      */ 
/*      */         
/*      */         } else {
/* 2332 */           switch (group.getGroupType()) {
/*      */             
/*      */             case 1:
/* 2335 */               buf.append(",");
/*      */               break;
/*      */             case 5:
/* 2338 */               buf.append(",[");
/*      */               break;
/*      */             case 3:
/* 2341 */               buf.append("|");
/*      */               break;
/*      */             case 4:
/* 2344 */               buf.append("|");
/*      */               break;
/*      */             case 2:
/* 2347 */               buf.append("|");
/*      */               break;
/*      */           } 
/*      */         } 
/* 2351 */         buf.append(Recipes.getIngredientName(ingredient));
/* 2352 */         groupId = newGroupId;
/* 2353 */         switch (group.getGroupType()) {
/*      */           
/*      */           case 5:
/* 2356 */             buf.append("]");
/*      */         } 
/*      */       
/*      */       } 
/*      */     } 
/* 2361 */     if (group != null)
/*      */     {
/* 2363 */       switch (group.getGroupType()) {
/*      */         
/*      */         case 3:
/* 2366 */           buf.append(")");
/*      */           break;
/*      */         case 4:
/* 2369 */           buf.append(")+");
/*      */           break;
/*      */         case 2:
/* 2372 */           buf.append("]");
/*      */           break;
/*      */       } 
/*      */     }
/* 2376 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void clearFound() {
/* 2384 */     for (IngredientGroup ig : this.ingredientGroups)
/*      */     {
/* 2386 */       ig.clearFound();
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
/*      */   public String toString() {
/* 2398 */     StringBuilder buf = new StringBuilder();
/* 2399 */     buf.append("Recipe:");
/* 2400 */     buf.append("recipeId:" + this.recipeId);
/* 2401 */     if (this.name.length() > 0)
/* 2402 */       buf.append(",name:" + this.name); 
/* 2403 */     if (this.skillId > 0)
/* 2404 */       buf.append(",skill:" + this.skillName + "(" + this.skillId + ")"); 
/* 2405 */     buf.append(",trigger:" + getTriggerName());
/* 2406 */     if (!this.cookers.isEmpty()) {
/*      */       
/* 2408 */       buf.append(",cookers[");
/* 2409 */       boolean first = true;
/* 2410 */       for (Map.Entry<Short, String> me : this.cookers.entrySet()) {
/*      */         
/* 2412 */         if (first) {
/* 2413 */           first = false;
/*      */         } else {
/* 2415 */           buf.append(",");
/* 2416 */         }  buf.append((String)me.getValue() + "(" + me.getKey() + "),dif=" + this.cookersDif.get(me.getKey()));
/*      */       } 
/* 2418 */       buf.append("]");
/*      */     } 
/* 2420 */     if (!this.containers.isEmpty()) {
/*      */       
/* 2422 */       buf.append(",containers[");
/* 2423 */       boolean first = true;
/* 2424 */       for (Map.Entry<Short, String> me : this.containers.entrySet()) {
/*      */         
/* 2426 */         if (first) {
/* 2427 */           first = false;
/*      */         } else {
/* 2429 */           buf.append(",");
/* 2430 */         }  buf.append((String)me.getValue() + "(" + me.getKey() + "),dif=" + this.containersDif.get(me.getKey()));
/*      */       } 
/* 2432 */       buf.append("]");
/*      */     } 
/* 2434 */     if (this.activeItem != null)
/* 2435 */       buf.append(",activeItem:" + this.activeItem.toString()); 
/* 2436 */     if (this.targetItem != null)
/* 2437 */       buf.append(",target:" + this.targetItem.toString()); 
/* 2438 */     if (!this.ingredientGroups.isEmpty()) {
/*      */       
/* 2440 */       buf.append(",ingredients{");
/* 2441 */       boolean first = true;
/* 2442 */       for (IngredientGroup ig : this.ingredientGroups) {
/*      */         
/* 2444 */         if (first) {
/* 2445 */           first = false;
/*      */         } else {
/* 2447 */           buf.append(",");
/* 2448 */         }  buf.append(ig.toString());
/*      */       } 
/* 2450 */       buf.append("}");
/*      */     } 
/* 2452 */     if (this.resultItem != null)
/* 2453 */       buf.append(",result:" + this.resultItem.toString()); 
/* 2454 */     if (this.achievementId != -1) {
/*      */       
/* 2456 */       buf.append(",achievementTriggered{");
/* 2457 */       buf.append(this.achievementName + "(" + this.achievementId + ")");
/* 2458 */       buf.append("}");
/*      */     } 
/* 2460 */     buf.append("}");
/* 2461 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class LiquidResult
/*      */   {
/* 2473 */     private final Map<Short, String> errors = new HashMap<>();
/* 2474 */     private int newWeight = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isSuccess() {
/* 2483 */       return this.errors.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<Short, String> getErrors() {
/* 2488 */       return this.errors;
/*      */     }
/*      */ 
/*      */     
/*      */     void add(short templateId, String error) {
/* 2493 */       this.errors.put(Short.valueOf(templateId), error);
/*      */     }
/*      */ 
/*      */     
/*      */     void setNewWeight(int newWeight) {
/* 2498 */       this.newWeight = newWeight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getNewWeight() {
/* 2503 */       return this.newWeight;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class Liquid
/*      */   {
/*      */     final short id;
/*      */ 
/*      */     
/*      */     final int ratio;
/*      */     
/* 2516 */     int weight = 0;
/*      */     
/*      */     final int loss;
/*      */     final String name;
/*      */     
/*      */     Liquid(short id, String name, int ratio, int loss) {
/* 2522 */       this.id = id;
/* 2523 */       this.name = name;
/* 2524 */       this.ratio = ratio;
/* 2525 */       this.loss = loss;
/*      */     }
/*      */ 
/*      */     
/*      */     short getId() {
/* 2530 */       return this.id;
/*      */     }
/*      */ 
/*      */     
/*      */     String getName() {
/* 2535 */       return this.name;
/*      */     }
/*      */ 
/*      */     
/*      */     int getRatio() {
/* 2540 */       return this.ratio;
/*      */     }
/*      */ 
/*      */     
/*      */     int getAbsRatio() {
/* 2545 */       return Math.abs(this.ratio);
/*      */     }
/*      */ 
/*      */     
/*      */     int getWeight() {
/* 2550 */       return this.weight;
/*      */     }
/*      */ 
/*      */     
/*      */     int getLoss() {
/* 2555 */       return this.loss;
/*      */     }
/*      */ 
/*      */     
/*      */     void setWeight(int newWeight) {
/* 2560 */       this.weight = newWeight;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Recipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
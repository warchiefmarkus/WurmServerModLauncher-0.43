/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import java.io.IOException;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ItemTemplateCreator
/*      */   implements ItemMaterials, TimeConstants
/*      */ {
/*      */   protected static final String ITEM_DESCRIPTION_ROTTEN = "rotten";
/*      */   protected static final String ITEM_DESCRIPTION_DRY = "dry";
/*      */   protected static final String ITEM_DESCRIPTION_FRESH = "fresh";
/*      */   protected static final String ITEM_DESCRIPTION_EXCELLENT = "excellent";
/*      */   protected static final String ITEM_DESCRIPTION_BRITTLE = "brittle";
/*      */   protected static final String ITEM_DESCRIPTION_WEAK = "Weak";
/*      */   protected static final String ITEM_DESCRIPTION_NORMAL = "normal";
/*      */   protected static final String ITEM_DESCRIPTION_STRONG = "strong";
/*      */   protected static final String ITEM_DESCRIPTION_POOR = "poor";
/*      */   protected static final String ITEM_DESCRIPTION_OK = "ok";
/*      */   protected static final String ITEM_DESCRIPTION_GOOD = "good";
/*      */   protected static final String ITEM_DESCRIPTION_SUPERB = "superb";
/*      */   protected static final String ITEM_DESCRIPTION_EMPTYISH = "emptyish";
/*      */   protected static final String ITEM_DESCRIPTION_HALF_FULL = "half-full";
/*      */   protected static final String ITEM_DESCRIPTION_SOMEWHAT_OCCUPIED = "somewhat occupied";
/*      */   protected static final String ITEM_DESCRIPTION_ALMOST_FULL = "almost full";
/*  103 */   private static final Logger logger = Logger.getLogger(ItemTemplateCreator.class.getName());
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
/*      */   public static ItemTemplate createItemTemplate(int templateId, String name, String plural, String itemDescriptionSuperb, String itemDescriptionNormal, String itemDescriptionBad, String itemDescriptionRotten, String itemDescriptionLong, short[] itemTypes, short imageNumber, short behaviourType, int combatDamage, long decayTime, int centimetersX, int centimetersY, int centimetersZ, int primarySkill, byte[] bodySpaces, String modelName, float difficulty, int weightGrams, byte material) throws IOException {
/*  147 */     return ItemTemplateFactory.getInstance().createItemTemplate(templateId, 3, name, plural, itemDescriptionSuperb, itemDescriptionNormal, itemDescriptionBad, itemDescriptionRotten, itemDescriptionLong, itemTypes, imageNumber, behaviourType, combatDamage, decayTime, centimetersX, centimetersY, centimetersZ, primarySkill, bodySpaces, modelName, difficulty, weightGrams, material, 0, false, 0);
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
/*      */   public static ItemTemplate createItemTemplate(int templateId, String name, String plural, String itemDescriptionSuperb, String itemDescriptionNormal, String itemDescriptionBad, String itemDescriptionRotten, String itemDescriptionLong, short[] itemTypes, short imageNumber, short behaviourType, int combatDamage, long decayTime, int centimetersX, int centimetersY, int centimetersZ, int primarySkill, byte[] bodySpaces, String modelName, float difficulty, int weightGrams, byte material, int value, boolean isPurchased) throws IOException {
/*  192 */     return ItemTemplateFactory.getInstance().createItemTemplate(templateId, 3, name, plural, itemDescriptionSuperb, itemDescriptionNormal, itemDescriptionBad, itemDescriptionRotten, itemDescriptionLong, itemTypes, imageNumber, behaviourType, combatDamage, decayTime, centimetersX, centimetersY, centimetersZ, primarySkill, bodySpaces, modelName, difficulty, weightGrams, material, value, isPurchased, 0);
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
/*      */   public static ItemTemplate createItemTemplate(int templateId, int size, String name, String plural, String itemDescriptionSuperb, String itemDescriptionNormal, String itemDescriptionBad, String itemDescriptionRotten, String itemDescriptionLong, short[] itemTypes, short imageNumber, short behaviourType, int combatDamage, long decayTime, int centimetersX, int centimetersY, int centimetersZ, int primarySkill, byte[] bodySpaces, String modelName, float difficulty, int weightGrams, byte material, int value, boolean isPurchased) throws IOException {
/*  236 */     return ItemTemplateFactory.getInstance().createItemTemplate(templateId, size, name, plural, itemDescriptionSuperb, itemDescriptionNormal, itemDescriptionBad, itemDescriptionRotten, itemDescriptionLong, itemTypes, imageNumber, behaviourType, combatDamage, decayTime, centimetersX, centimetersY, centimetersZ, primarySkill, bodySpaces, modelName, difficulty, weightGrams, material, value, isPurchased, 0);
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
/*      */   public static ItemTemplate createItemTemplate(int templateId, int size, String name, String plural, String itemDescriptionSuperb, String itemDescriptionNormal, String itemDescriptionBad, String itemDescriptionRotten, String itemDescriptionLong, short[] itemTypes, short imageNumber, short behaviourType, int combatDamage, long decayTime, int centimetersX, int centimetersY, int centimetersZ, int primarySkill, byte[] bodySpaces, String modelName, float difficulty, int weightGrams, byte material, int value, boolean isPurchased, int dyeAmountGramsOverride) throws IOException {
/*  280 */     return ItemTemplateFactory.getInstance().createItemTemplate(templateId, size, name, plural, itemDescriptionSuperb, itemDescriptionNormal, itemDescriptionBad, itemDescriptionRotten, itemDescriptionLong, itemTypes, imageNumber, behaviourType, combatDamage, decayTime, centimetersX, centimetersY, centimetersZ, primarySkill, bodySpaces, modelName, difficulty, weightGrams, material, value, isPurchased, dyeAmountGramsOverride);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void initialiseItemTemplates() throws IOException {
/*  289 */     long start = System.nanoTime();
/*      */     
/*  291 */     createItemTemplate(0, "inventory", "inventories", "almost full", "somewhat occupied", "half-full", "emptyish", "This is where you keep your things.", new short[] { 9, 1, 112 }, (short)20, (short)49, 0, Long.MAX_VALUE, 400, 400, 4000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  298 */     createItemTemplate(1, "backpack", "backpacks", "strong", "well-made", "ok", "fragile", "A backpack made from leather with metal husks.", new short[] { 44, 23, 1, 97 }, (short)241, (short)1, 0, 3024000L, 30, 50, 50, -10, new byte[] { 2, 42 }, "model.container.backpack.", 40.0F, 2000, (byte)16, 200, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  305 */     createItemTemplate(2, "satchel", "satchels", "strong", "well-made", "ok", "torn", "A piece of cloth sown with thick threads into a satchel perfect for carrying about the back.", new short[] { 44, 24, 1, 92, 147 }, (short)242, (short)1, 0, 3024000L, 20, 30, 30, 1020, new byte[] { 2, 42 }, "model.container.satchel.", 20.0F, 500, (byte)17, 200, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  313 */     createItemTemplate(5, "potion", "potions", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A small flask containing an oily substance that glows in the dark.", new short[] { 6 }, (short)260, (short)1, 0, 60L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.potion.", 10.0F, 200, (byte)21, 10000, false);
/*      */ 
/*      */ 
/*      */     
/*  317 */     createItemTemplate(7, "hatchet", "hatchets", "superb", "good", "ok", "poor", "A short but sturdy axe with a thick blade specially designed to cut down trees with but poor in combat.", new short[] { 44, 37, 108, 22, 15, 2, 97 }, (short)1207, (short)1, 10, 9072000L, 3, 10, 60, 10003, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.hatchet.", 11.0F, 2500, (byte)11, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  325 */     createItemTemplate(9, "log", "logs", "fresh", "dry", "brittle", "rotten", "A log cleared of branches. Good for making planks or shafts from.", new short[] { 133, 21, 84, 113, 129, 146, 175 }, (short)606, (short)1, 10, 604800L, 20, 20, 200, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.log.", 200.0F, 24000, (byte)0, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  331 */     createItemTemplate(10, "leg", "legs", "superb", "strong", "normal", "Weak", "A leg.", new short[] { 28, 8, 1, 48 }, (short)5, (short)10, 20, 86400L, 5, 5, 70, 10052, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  336 */     createItemTemplate(19, "legs", "legs", "superb", "strong", "normal", "Weak", "Two legs.", new short[] { 28, 8, 1 }, (short)5, (short)10, 20, 86400L, 1, 1, 1, 10052, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  342 */     createItemTemplate(11, "arm", "arms", "superb", "strong", "normal", "Weak", "An arm.", new short[] { 28, 8, 1, 48 }, (short)3, (short)10, 20, 86400L, 5, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  347 */     createItemTemplate(12, "head", "heads", "superb", "strong", "normal", "Weak", "A head.", new short[] { 28, 8, 1, 48 }, (short)2, (short)10, 20, 86400L, 10, 10, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  352 */     createItemTemplate(13, "torso", "torsos", "superb", "strong", "normal", "Weak", "A body without the extremities.", new short[] { 28, 8, 1, 48 }, (short)1, (short)10, 0, 86400L, 10, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  357 */     createItemTemplate(14, "hand", "hands", "superb", "strong", "normal", "Weak", "A hand.", new short[] { 28, 8, 1, 48, 210 }, (short)4, (short)10, 20, 86400L, 1, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  362 */     createItemTemplate(15, "foot", "feet", "superb", "strong", "normal", "Weak", "A foot.", new short[] { 28, 8, 1, 48 }, (short)6, (short)10, 20, 86400L, 4, 7, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  367 */     createItemTemplate(16, "body", "bodies", "superb", "strong", "normal", "Weak", "A body.", new short[] { 28, 8, 1 }, (short)0, (short)10, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  372 */     createItemTemplate(17, "face", "faces", "awesome", "good looking", "normal", "ugly", "A face.", new short[] { 28, 8, 1, 48 }, (short)2, (short)10, 20, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  377 */     createItemTemplate(18, "eye", "eyes", "awesome", "good looking", "normal", "ugly", "An eye.", new short[] { 28, 8, 48 }, (short)2, (short)10, 20, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 200.0F, 0, (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  383 */     createItemTemplate(273, "steel glove", "steel gloves", "superb", "good", "ok", "poor", "A glove composed of steel rings.", new short[] { 108, 44, 22, 4, 92 }, (short)1084, (short)1, 20, 12096000L, 1, 7, 15, -10, new byte[] { 13, 14 }, "model.armour.hand.", 200.0F, 300, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  389 */     createItemTemplate(25, "shovel", "shovels", "superb", "good", "ok", "poor", "A tool for digging.", new short[] { 133, 108, 44, 38, 22, 19, 37, 2, 97, 247 }, (short)746, (short)1, 2, 9072000L, 2, 20, 100, 10002, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.shovel.", 20.0F, 2000, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  396 */     createItemTemplate(27, "rake", "rakes", "superb", "good", "ok", "poor", "A tool for working the fields.", new short[] { 133, 108, 44, 38, 22, 7, 2, 37, 97 }, (short)745, (short)1, 10, 9072000L, 2, 20, 120, 10004, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.rake.", 10.0F, 1200, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  404 */     createItemTemplate(29, "wheat", "wheat", "superb", "good", "ok", "poor", "A few handfuls of wheat. Wheat is pretty sensitive as a crop. It is normally used to turn into flour and bake with or to sow with.", new short[] { 146, 102, 20, 5, 55, 129 }, (short)481, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wheat.", 200.0F, 300, (byte)6, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  413 */       .setNutritionValues(3270, 712, 15, 126)
/*  414 */       .setFoodGroup(1157);
/*      */     
/*  416 */     createItemTemplate(28, "barley", "barley", "superb", "good", "ok", "poor", "A few handfuls of barley. It is easy to farm, and some people prefer to brew beer with it.", new short[] { 146, 103, 20, 5, 55, 129 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.barley.", 200.0F, 300, (byte)5, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  425 */       .setNutritionValues(3520, 777, 12, 99)
/*  426 */       .setFoodGroup(1157);
/*      */     
/*  428 */     createItemTemplate(31, "oat", "oat", "superb", "good", "ok", "poor", "A few handfuls of oat. Oat is not the easiest of crops to grow. It is normally used to turn into flour and bake with or to sow with. It is also very tasteful as porridge.", new short[] { 20, 146, 5, 55, 129 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.oat.", 200.0F, 300, (byte)4, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  436 */       .setNutritionValues(3890, 663, 69, 169)
/*  437 */       .setFoodGroup(1157);
/*      */     
/*  439 */     createItemTemplate(32, "corn", "corn", "superb", "good", "ok", "poor", "A corn stick. Corn will be hard to grow in this environment. It is normally used to cook and eat, or to sow with.", new short[] { 146, 102, 20, 5, 55, 129, 217, 29, 212, 223 }, (short)502, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.corn.", 2.0F, 100, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  449 */       .setNutritionValues(860, 187, 14, 33)
/*  450 */       .setFoodGroup(1156);
/*      */     
/*  452 */     createItemTemplate(30, "rye", "rye", "superb", "good", "ok", "poor", "A few handfuls of rye. Rye grows pretty much anywhere and is easy to farm. It is normally used to turn into flour and bake with or to sow with.", new short[] { 20, 146, 5, 55, 129 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.rye.", 200.0F, 300, (byte)3, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  460 */       .setNutritionValues(334, 698, 24, 147)
/*  461 */       .setFoodGroup(1157);
/*      */     
/*  463 */     createItemTemplate(34, "pumpkin seed", "pumpkin seeds", "superb", "good", "ok", "poor", "A few handfuls of pumpkin seed. It can be used to sow with, but pumpkins can be quite hard to grow to acceptable quality.", new short[] { 20, 146, 5, 55, 129 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.pumpkinseed.", 200.0F, 100, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  472 */     createItemTemplate(317, "wemp seed", "wemp seeds", "superb", "good", "ok", "poor", "A few handfuls of wemp seed. It can be used to sow with, but wemp can be quite hard to grow to acceptable quality.", new short[] { 20, 146, 129 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wempseed.", 200.0F, 100, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  481 */     createItemTemplate(318, "wemp fibre", "wemp fibre", "superb", "good", "ok", "poor", "Some strong wemp fibres.", new short[] { 46, 146, 129 }, (short)547, (short)1, 0, 604800L, 3, 3, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wempfibre.", 200.0F, 500, (byte)53, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  487 */     createItemTemplate(748, "papyrus sheet", "papyrus sheets", "excellent", "good", "ok", "poor", "A flat piece of papyrus made from pressed reed fibre.", new short[] { 21, 159, 146, 211 }, (short)640, (short)44, 0, 3024000L, 1, 20, 25, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.papyrus.", 5.0F, 10, (byte)33, 5000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  495 */     createItemTemplate(744, "reed seed", "reed seeds", "superb", "good", "ok", "poor", "A few handfuls of reed seed. It can be used to sow with, but reed must be grown under very special conditions.", new short[] { 20, 146, 129 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.reedseed.", 200.0F, 100, (byte)22, 10, true);
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
/*  509 */     createItemTemplate(743, "reed plants", "reed plants", "superb", "good", "ok", "poor", "Some reed plants. It has very strong fibres.", new short[] { 146, 102, 46, 129 }, (short)547, (short)16, 0, 28800L, 5, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.reed.", 5.0F, 500, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  516 */       .setCrushsTo(745)
/*  517 */       .setPickSeeds(744);
/*      */     
/*  519 */     createItemTemplate(745, "reed fibre", "reed fibre", "superb", "good", "ok", "poor", "Some weak reed fibres.", new short[] { 46, 146, 129, 211 }, (short)547, (short)1, 0, 604800L, 3, 3, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.reedfibre.", 5.0F, 500, (byte)60, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  526 */     createItemTemplate(746, "rice", "rice", "superb", "good", "ok", "poor", "Some rice. Rice requires a lot of water to grow.", new short[] { 5, 146, 20, 55, 129, 212 }, (short)480, (short)1, 0, 28800L, 5, 5, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.rice.", 3.0F, 100, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  534 */       .setNutritionValues(3650, 800, 7, 71);
/*      */     
/*  536 */     createItemTemplate(319, "rope", "ropes", "excellent", "good", "ok", "poor", "A coarse rope made from from wemp fibre.", new short[] { 46, 64, 147, 146, 189 }, (short)621, (short)1, 0, 9072000L, 1, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wemprope.", 1.0F, 500, (byte)53, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  542 */     createItemTemplate(557, "thick rope", "thick ropes", "excellent", "good", "ok", "poor", "A very thick rope commonly used on ships.", new short[] { 46, 147, 146 }, (short)621, (short)1, 0, 3024000L, 10, 30, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wemprope.thick.", 20.0F, 5500, (byte)53, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  547 */     createItemTemplate(558, "mooring rope", "mooring ropes", "excellent", "good", "ok", "poor", "A sturdy but lean rope used for the anchor and for mooring a ship.", new short[] { 46, 147, 146 }, (short)621, (short)1, 0, 3024000L, 5, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wemprope.mooring.", 30.0F, 500, (byte)53, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  555 */     createItemTemplate(559, "cordage rope", "cordage ropes", "excellent", "good", "ok", "poor", "This rope is thinner and used for hoisting and lowering sails.", new short[] { 46, 147, 146 }, (short)621, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wemprope.cordage.", 40.0F, 500, (byte)53, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  562 */     createItemTemplate(565, "mooring anchor", "anchors", "excellent", "good", "ok", "poor", "A mooring rope tied to an anchor.", new short[] { 46, 147, 146, 22 }, (short)461, (short)1, 0, 3024000L, 10, 60, 70, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.anchor.rope.", 1.0F, 20500, (byte)12, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  568 */     createItemTemplate(1029, "halter rope", "halter ropes", "excellent", "good", "ok", "poor", "A long length of rope with knotted loops to lead multiple animals.", new short[] { 64, 198, 147 }, (short)621, (short)1, 0, 3024000L, 4, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wemprope.", 70.0F, 2000, (byte)53, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  574 */     createItemTemplate(320, "rope tool", "rope tools", "superb", "good", "ok", "poor", "A rope making tool, a small handheld spinning wheel with three large nails.", new short[] { 21, 44, 108, 147, 51 }, (short)880, (short)1, 0, 9072000L, 2, 25, 25, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.ropetool.small.", 10.0F, 1500, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  581 */     createItemTemplate(33, "pumpkin", "pumpkin", "superb", "good", "ok", "poor", "A nice red pumpkin.", new short[] { 146, 102, 5, 55, 129, 212, 29, 223 }, (short)501, (short)16, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pumpkin.", 2.0F, 1000, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  588 */       .setNutritionValues(260, 65, 1, 10)
/*  589 */       .setCrushsTo(34)
/*  590 */       .setFoodGroup(1156);
/*      */     
/*  592 */     createItemTemplate(522, "carved pumpkin", "pumpkin", "superb", "good", "ok", "poor", "A pumpkin carved into a scary face.", new short[] { 32, 59, 101 }, (short)501, (short)16, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pumpkin.halloween.", 1.0F, 1000, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  600 */     createItemTemplate(35, "potato", "potatoes", "superb", "good", "ok", "poor", "A potato. potatoes can be pretty hard to grow to edible size.", new short[] { 5, 146, 20, 55, 129, 212, 29, 223 }, (short)500, (short)1, 0, 28800L, 5, 5, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.potato.", 2.0F, 500, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  608 */       .setNutritionValues(770, 175, 1, 20)
/*  609 */       .setFoodGroup(1156);
/*      */     
/*  611 */     createItemTemplate(22, "plank", "planks", "fresh", "dry", "brittle", "rotten", "A three steps long wooden plank that could be used to build something.", new short[] { 133, 146, 21, 18, 144, 37, 84, 129, 158 }, (short)626, (short)1, 20, 28800L, 3, 5, 200, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.plank.", 3.0F, 2000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  619 */     createItemTemplate(23, "shaft", "shafts", "fresh", "dry", "brittle", "rotten", "A one step long wooden shaft that could be used to create a tool or a weapon.", new short[] { 133, 146, 21, 14, 144, 37, 84, 129, 158, 165 }, (short)646, (short)1, 25, 28800L, 3, 7, 100, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.shaft.", 3.0F, 1000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  627 */     createItemTemplate(24, "saw", "saws", "excellent", "good", "ok", "poor", "A saw, good for creating and sawing planks.", new short[] { 108, 44, 38, 22, 18, 11, 37, 84, 97 }, (short)747, (short)1, 20, 9072000L, 1, 5, 70, 10008, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.saw.", 20.0F, 1200, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  633 */     createItemTemplate(441, "metal brush", "metal brushes", "excellent", "good", "ok", "poor", "A piece of wood with a bundle of rough wires attached on one end.", new short[] { 108, 22, 44, 38, 11, 147 }, (short)882, (short)1, 0, 9072000L, 1, 5, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.metalbrush.", 25.0F, 1400, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  640 */     createItemTemplate(36, "kindling", "kindling", "excellent", "good", "ok", "poor", "Small and large pieces of chopped wood, perfect for lighting a fire with.", new short[] { 21, 146, 46, 113, 129, 165 }, (short)686, (short)1, 0, 28800L, 10, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.kindling.", 1.0F, 1500, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  647 */     createItemTemplate(37, "campfire", "campfires", "excellent", "good", "ok", "poor", "A nice and cosy campfire.", new short[] { 52, 21, 1, 31, 59, 65, 147, 165, 49, 209 }, (short)291, (short)18, 50, 60L, 41, 41, 201, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.campfire.", 1.0F, 1500, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  655 */     createItemTemplate(169, "wood scrap", "wood scrap", "excellent", "good", "ok", "poor", "Scrap wood, mostly useful for feeding fires with.", new short[] { 21, 146, 46, 113, 129, 157, 174, 211 }, (short)686, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 1500, (byte)14, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  665 */     createItemTemplate(171, "rags", "rags", "excellent", "good", "ok", "poor", "Cloth rags.", new short[] { 24, 146, 46, 92, 113, 129, 174, 157 }, (short)600, (short)1, 0, 28800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 200, (byte)17, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  672 */     createItemTemplate(172, "leather pieces", "leather pieces", "excellent", "good", "ok", "poor", "Small pieces of leather.", new short[] { 23, 146, 46, 113, 129, 174, 157, 211 }, (short)623, (short)1, 0, 86401L, 5, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 400, (byte)16, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  678 */     createItemTemplate(173, "pig food", "pig food", "excellent", "good", "ok", "poor", "Smelly food, edible only by pigs.", new short[] { 5, 146, 46, 55, 113, 129, 157, 174 }, (short)624, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.scrap.", 1.0F, 800, (byte)22, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  685 */     createItemTemplate(170, "scrap", "iron scrap", "excellent", "good", "ok", "poor", "Scrap iron, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)653, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 800, (byte)11, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  692 */     createItemTemplate(195, "scrap", "copper scrap", "excellent", "good", "ok", "poor", "Scrap copper, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 164, 174 }, (short)656, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 800, (byte)10, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  699 */     createItemTemplate(196, "scrap", "gold scrap", "excellent", "good", "ok", "poor", "Scrap gold, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)651, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 50, (byte)7, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  706 */     createItemTemplate(197, "scrap", "silver scrap", "excellent", "good", "ok", "poor", "Scrap silver, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)652, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 50, (byte)8, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  713 */     createItemTemplate(198, "scrap", "zinc scrap", "excellent", "good", "ok", "poor", "Scrap zinc, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)655, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 800, (byte)13, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  720 */     createItemTemplate(199, "scrap", "lead scrap", "excellent", "good", "ok", "poor", "Scrap lead, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157 }, (short)654, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 800, (byte)12, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  726 */     createItemTemplate(206, "scrap", "steel scrap", "excellent", "good", "ok", "poor", "Scrap steel, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)692, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 100, (byte)9, 40, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  733 */     createItemTemplate(225, "scrap", "brass scrap", "excellent", "good", "ok", "poor", "Scrap brass, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)693, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 100, (byte)30, 40, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  740 */     createItemTemplate(224, "scrap", "bronze scrap", "excellent", "good", "ok", "poor", "Scrap bronze, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)691, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 100, (byte)31, 40, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  747 */     createItemTemplate(222, "scrap", "tin scrap", "excellent", "good", "ok", "poor", "Scrap tin, with no obvious use except melting.", new short[] { 22, 146, 46, 113, 129, 157, 174 }, (short)657, (short)1, 0, 86401L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 400, (byte)34, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  755 */     createItemTemplate(43, "ore", "copper ore", "excellent", "good", "ok", "poor", "A white and pale green chunk of rock.", new short[] { 148, 146, 46, 113, 129, 141, 48, 157 }, (short)616, (short)46, 0, 86401L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)10, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  763 */     createItemTemplate(41, "ore", "lead ore", "excellent", "good", "ok", "poor", "A thick clump of large cubic grey metallic crystals.", new short[] { 148, 146, 46, 113, 129, 141, 48, 157 }, (short)614, (short)46, 0, 86401L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)12, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  771 */     createItemTemplate(39, "ore", "gold ore", "excellent", "good", "ok", "poor", "A crystalline chunk of clear quartz with golden clumps.", new short[] { 148, 146, 46, 113, 129, 141, 48, 157 }, (short)611, (short)46, 0, 86401L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)7, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  780 */     createItemTemplate(40, "ore", "silver ore", "excellent", "good", "ok", "poor", "A thick clump of rock with metallic red and grey colours.", new short[] { 148, 146, 46, 113, 129, 141, 48, 157 }, (short)612, (short)46, 0, 86401L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)8, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  789 */     createItemTemplate(38, "ore", "iron ore", "excellent", "good", "ok", "poor", "A reddish chunk of rock containing many cavities lined with dark, metallic crystals.", new short[] { 148, 146, 46, 113, 129, 141, 48, 157 }, (short)613, (short)46, 0, 86401L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)11, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  799 */     createItemTemplate(684, "rock", "iron rock", "excellent", "good", "ok", "poor", "A small vaguely reddish rock containing a few cavities lined with dark, metallic crystals.", new short[] { 148, 146, 46, 113, 129, 141, 151, 157, 175 }, (short)618, (short)1, 0, 86401L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.rock.", 200.0F, 3000, (byte)11, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  806 */     createItemTemplate(42, "ore", "zinc ore", "excellent", "good", "ok", "poor", "A chunk of greenish brown glassy crystallines.", new short[] { 148, 146, 46, 113, 129, 141, 48, 157 }, (short)615, (short)46, 0, 86401L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)13, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  815 */     createItemTemplate(692, "boulder", "boulders", "excellent", "good", "ok", "poor", "A huge boulder containing black chunks of glossy black rock lined with bleaker streaks of a dark metal.", new short[] { 113, 31, 152, 52 }, (short)60, (short)1, 0, 3024000L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.boulder.", 200.0F, 200000, (byte)56, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  823 */     createItemTemplate(693, "ore", "admantine ore", "excellent", "good", "ok", "poor", "A glossy black chunk of rock lined with bleaker streaks of a dark metal.", new short[] { 148, 146, 46, 113, 48, 157 }, (short)596, (short)46, 0, 3024000L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)56, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  831 */     createItemTemplate(694, "lump", "adamantine lumps", "excellent", "good", "ok", "poor", "A heavy lump of black glossy adamantine.", new short[] { 22, 146, 46, 113, 157 }, (short)639, (short)1, 0, 3024000L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 4.0F, 400, (byte)56, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  838 */     createItemTemplate(695, "scrap", "adamantine scrap", "excellent", "good", "ok", "poor", "Scrap adamantine. The best and obvious use is melting it.", new short[] { 22, 146, 46, 113, 157 }, (short)60, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 100, (byte)56, 40, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  846 */     createItemTemplate(696, "boulder", "boulders", "excellent", "good", "ok", "poor", "A huge boulder containing pale brittle rock sparkled with a shiny white metal.", new short[] { 113, 31, 152, 157, 52 }, (short)60, (short)1, 0, 3024000L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.boulder.", 200.0F, 200000, (byte)57, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  853 */     createItemTemplate(697, "ore", "glimmersteel ore", "excellent", "good", "ok", "poor", "This is a pale brittle rock sparkled with a shiny white metal.", new short[] { 148, 146, 46, 113, 48, 157 }, (short)595, (short)46, 0, 3024000L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)57, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  862 */     createItemTemplate(698, "lump", "glimmersteel lumps", "excellent", "good", "ok", "poor", "A heavy lump of faintly sparkling white metal.", new short[] { 22, 146, 46, 113, 157 }, (short)638, (short)1, 0, 3024000L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 4.0F, 400, (byte)57, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  869 */     createItemTemplate(699, "scrap", "glimmersteel scrap", "excellent", "good", "ok", "poor", "Scrap glimmersteel. The best and obvious use is melting it.", new short[] { 22, 146, 46, 113, 157 }, (short)60, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scrap.", 1.0F, 100, (byte)57, 40, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  877 */     createItemTemplate(26, "dirt", "pile of dirt", "excellent", "good", "ok", "poor", "A lot of dirt, enough to raise the ground somewhere.", new short[] { 25, 146, 112, 113, 129, 141, 175 }, (short)590, (short)1, 0, 28800L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.dirt.", 200.0F, 20000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  884 */     createItemTemplate(146, "rock shards", "rock shards", "superb", "good", "ok", "poor", "Lots of different sized rock shards.", new short[] { 25, 146, 46, 112, 113, 129, 48, 175, 242, 243 }, (short)610, (short)46, 0, 604800L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.", 1.0F, 20000, (byte)15, 5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  891 */     createItemTemplate(207, "ore", "tin ore", "excellent", "good", "ok", "poor", "A brown dirt-looking clump with regions of dark crystals.", new short[] { 148, 22, 146, 46, 113, 129, 141, 48, 157 }, (short)617, (short)46, 0, 86401L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ore.", 200.0F, 20000, (byte)34, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  899 */     createItemTemplate(132, "stone brick", "stone bricks", "excellent", "good", "ok", "poor", "A stone chiselled into a cubic shape.", new short[] { 25, 146, 113, 129, 158, 242, 243 }, (short)670, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.", 1.0F, 15000, (byte)15, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  906 */     createItemTemplate(519, "colossus brick", "colossus bricks", "excellent", "good", "ok", "poor", "Part of something greater, this stone is chiselled into a shape that will fit on a colossus.", new short[] { 25, 146, 113, 242, 243 }, (short)670, (short)1, 0, 12096000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.brick.colossus.", 20.0F, 15000, (byte)15, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  913 */     createItemTemplate(130, "clay", "clay", "excellent", "good", "ok", "poor", "Grey sticky clay that could be easily formed into items.", new short[] { 25, 146, 46, 112, 113, 141, 174 }, (short)591, (short)1, 0, 18000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.clay.", 1.0F, 2000, (byte)18, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  921 */     createItemTemplate(492, "mortar", "mortar", "excellent", "good", "ok", "poor", "Fine-grained brown mortar, perfect for masonry.", new short[] { 25, 146, 46, 112, 113, 158, 174 }, (short)591, (short)1, 0, 18000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.mortar.", 15.0F, 2000, (byte)18, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  928 */     createItemTemplate(446, "flint", "flints", "superb", "good", "ok", "poor", "A small, hard, black glistening piece of rock.", new short[] { 25, 146, 113 }, (short)863, (short)1, 0, 12096000L, 4, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.flint.", 1.0F, 100, (byte)15, 500, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  934 */     createItemTemplate(44, "lump", "gold lumps", "excellent", "good", "ok", "poor", "A small lump of pure gold.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)631, (short)1, 0, 3024000L, 3, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 1.0F, 100, (byte)7, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  940 */     createItemTemplate(45, "lump", "silver lumps", "excellent", "good", "ok", "poor", "A small lump of pure silver.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)632, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 1.0F, 100, (byte)8, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  946 */     createItemTemplate(46, "lump", "iron lumps", "excellent", "good", "ok", "poor", "A lump of pure iron.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)633, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 1.0F, 1000, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     createItemTemplate(47, "lump", "copper lumps", "excellent", "good", "ok", "poor", "A lump of pure red copper.", new short[] { 22, 146, 46, 113, 141, 164, 157 }, (short)636, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 1.0F, 1000, (byte)10, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  959 */     createItemTemplate(48, "lump", "zinc lumps", "excellent", "good", "ok", "poor", "A grey and white lump of zinc.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)635, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 1.0F, 1000, (byte)13, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  965 */     createItemTemplate(49, "lump", "lead lumps", "excellent", "good", "ok", "poor", "A heavy lump of dull grey lead.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)634, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 1.0F, 1000, (byte)12, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  971 */     createItemTemplate(205, "lump", "steel lumps", "excellent", "good", "ok", "poor", "A heavy lump of dark steel.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)672, (short)1, 0, 3024000L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 4.0F, 400, (byte)9, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  977 */     createItemTemplate(221, "lump", "brass lumps", "excellent", "good", "ok", "poor", "A shining yellowy red lump of brass.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)673, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 10.0F, 100, (byte)30, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  983 */     createItemTemplate(223, "lump", "bronze lumps", "excellent", "good", "ok", "poor", "A red glistening lump of bronze.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)671, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 10.0F, 100, (byte)31, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  989 */     createItemTemplate(220, "lump", "tin lumps", "excellent", "good", "ok", "poor", "A lump of light tin.", new short[] { 22, 146, 46, 113, 141, 157 }, (short)637, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lump.", 1.0F, 500, (byte)34, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  996 */     createItemTemplate(50, "coin", "copper coins", "excellent", "good", "ok", "poor", "A small round copper coin.", new short[] { 22, 50, 40 }, (short)572, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 10, (byte)10, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1001 */     createItemTemplate(51, "coin", "iron coins", "excellent", "good", "ok", "poor", "A small round iron coin.", new short[] { 22, 50, 40 }, (short)573, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 10, (byte)11, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1006 */     createItemTemplate(52, "coin", "silver coins", "excellent", "good", "ok", "poor", "A small round silver coin.", new short[] { 22, 50, 40 }, (short)571, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 10, (byte)8, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     createItemTemplate(53, "coin", "gold coins", "excellent", "good", "ok", "poor", "A small round gold coin.", new short[] { 22, 50, 40 }, (short)570, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 10, (byte)7, 1000000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1017 */     createItemTemplate(54, "five coin", "copper-five coins", "excellent", "good", "ok", "poor", "A round copper coin with five crowns on it.", new short[] { 22, 50, 40 }, (short)572, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 20, (byte)10, 500, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1023 */     createItemTemplate(55, "five coin", "iron-five coins", "excellent", "good", "ok", "poor", "A round iron coin with five crowns on it.", new short[] { 22, 50, 40 }, (short)573, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 20, (byte)11, 5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1029 */     createItemTemplate(56, "five coin", "silver-five coins", "excellent", "good", "ok", "poor", "A round silver coin with five crowns on it.", new short[] { 22, 50, 40 }, (short)571, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 20, (byte)8, 50000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1035 */     createItemTemplate(57, "five coin", "gold-five coins", "excellent", "good", "ok", "poor", "A round gold coin with five crowns on it.", new short[] { 22, 50, 40 }, (short)570, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 20, (byte)7, 5000000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1042 */     createItemTemplate(58, "twenty coin", "copper-twenty coins", "excellent", "good", "ok", "poor", "A large round copper coin with two sheaves on it.", new short[] { 22, 50, 40 }, (short)572, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 30, (byte)10, 2000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1048 */     createItemTemplate(59, "twenty coin", "iron-twenty coins", "excellent", "good", "ok", "poor", "A large round iron coin with two sheaves on it.", new short[] { 22, 50, 40 }, (short)573, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 30, (byte)11, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1054 */     createItemTemplate(60, "twenty coin", "silver-twenty coins", "excellent", "good", "ok", "poor", "A large round silver coin with two sheaves on it.", new short[] { 22, 50, 40 }, (short)571, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 30, (byte)8, 200000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1060 */     createItemTemplate(61, "twenty coin", "gold-twenty coins", "excellent", "good", "ok", "poor", "A large round gold coin two sheaves on it.", new short[] { 22, 50, 40 }, (short)570, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.coin.", 200.0F, 30, (byte)7, 20000000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1066 */     createItemTemplate(212, "bale", "cotton bales", "excellent", "good", "ok", "poor", "A lot of wool pressed and tied together to a roll.", new short[] { 24, 146, 46, 113, 157 }, (short)600, (short)1, 0, 3024000L, 10, 10, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.bale.", 200.0F, 1000, (byte)17, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1071 */     createItemTemplate(213, "square piece of cloth", "square pieces of cloth", "excellent", "good", "ok", "poor", "A piece of cloth, about an arm length square.", new short[] { 24, 146, 46 }, (short)640, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.yard.", 5.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1077 */     createItemTemplate(639, "meditation rug", "rugs", "excellent", "good", "ok", "poor", "This thin, colorful rug provides some comfort as you sit on it but its main purpose is to create a meditation zone where your mind can relax.", new short[] { 24, 44, 52, 140, 51, 184 }, (short)901, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.carpet.medi.one.", 45.0F, 1500, (byte)17, 2000, true);
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
/* 1092 */     createItemTemplate(644, "fine meditation rug", "rugs", "excellent", "good", "ok", "poor", "This thin, colorful rug provides some comfort as you sit on it but its main purpose is to create a meditation zone where your mind can relax.", new short[] { 24, 44, 52, 140, 51, 184 }, (short)901, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.carpet.medi.two.", 65.0F, 1500, (byte)17, 10000, true);
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
/* 1107 */     createItemTemplate(645, "beautiful meditation rug", "rugs", "excellent", "good", "ok", "poor", "This thin, colorful rug provides some comfort as you sit on it but its main purpose is to create a meditation zone where your mind can relax.", new short[] { 24, 44, 52, 140, 51, 184 }, (short)901, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.carpet.medi.three.", 80.0F, 1500, (byte)17, 20000, true);
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
/* 1122 */     createItemTemplate(646, "exquisite meditation rug", "rugs", "excellent", "good", "ok", "poor", "This thin, colorful rug provides some comfort as you sit on it but its main purpose is to create a meditation zone where your mind can relax.", new short[] { 24, 44, 52, 140, 51, 184 }, (short)901, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.carpet.medi.four.", 95.0F, 1500, (byte)17, 30000, true);
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
/* 1137 */     createItemTemplate(214, "string of cloth", "strings of cloth", "excellent", "good", "ok", "poor", "A fine string of cloth wound around a piece of wood.", new short[] { 24, 146, 46, 158 }, (short)620, (short)1, 0, 28800L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.string.", 1.0F, 200, (byte)17, 200, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1143 */     createItemTemplate(486, "sheet", "sheets", "excellent", "good", "ok", "poor", "A fine soft sheet.", new short[] { 24, 146, 92 }, (short)640, (short)1, 0, 3024000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 10.0F, 1500, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1148 */     createItemTemplate(487, "flag", "flags", "excellent", "good", "ok", "poor", "Apart from being a symbol of your allegiance and territorial demands, this is also a good indication of where the wind is blowing.", new short[] { 24, 92, 124, 147, 52, 167, 48, 86, 119, 44, 199, 173 }, (short)640, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.flag.", 30.0F, 2500, (byte)14, 10000, true);
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
/* 1164 */     createItemTemplate(577, "banner", "banners", "excellent", "good", "ok", "poor", "An elegant symbol of allegiance and faith.", new short[] { 24, 92, 147, 51, 52, 167, 48, 86, 119, 44, 199, 173 }, (short)640, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.banner.", 40.0F, 2500, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1173 */     createItemTemplate(579, "kingdom flag", "flags", "excellent", "good", "ok", "poor", "Apart from being a symbol of your allegiance and territorial demands, this is also a good indication of where the wind is blowing.", new short[] { 24, 92, 124, 52, 167, 48, 86, 119, 44, 173, 199 }, (short)640, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.flag.", 40.0F, 2500, (byte)14, 10000, true);
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
/* 1189 */     createItemTemplate(578, "kingdom banner", "banners", "excellent", "good", "ok", "poor", "An elegant symbol of allegiance and faith.", new short[] { 24, 92, 51, 52, 167, 48, 86, 119, 44, 173, 199 }, (short)640, (short)1, 0, 9072000L, 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.banner.", 50.0F, 2500, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1199 */     createItemTemplate(215, "needle", "iron needles", "excellent", "good", "ok", "poor", "A small iron needle.", new short[] { 38, 22, 44, 147, 174 }, (short)788, (short)1, 0, 3024000L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.needle.", 1.0F, 5, (byte)11, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1204 */     createItemTemplate(216, "needle", "copper needles", "excellent", "good", "ok", "poor", "A small copper needle.", new short[] { 38, 22, 44, 157, 174 }, (short)788, (short)1, 0, 3024000L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.needle.", 1.0F, 5, (byte)10, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1209 */     createItemTemplate(217, 4, "nails", "nails", "excellent", "good", "ok", "poor", "About ten metal nails, each one long as your hand.", new short[] { 38, 146, 22, 158, 174 }, (short)781, (short)1, 0, 3024000L, 1, 2, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.nails.large.", 1.0F, 300, (byte)11, 100, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1216 */     createItemTemplate(218, 2, "nails", "nails", "excellent", "good", "ok", "poor", "About ten metal nails, each long as a grown man's thumb.", new short[] { 38, 146, 22, 158, 174 }, (short)781, (short)1, 0, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.nails.small.", 1.0F, 200, (byte)11, 100, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1223 */     createItemTemplate(62, "hammer", "hammers", "excellent", "good", "ok", "poor", "A hammer with a metal head and wooden shaft.", new short[] { 108, 44, 38, 22, 14, 147, 247 }, (short)742, (short)1, 20, 3024000L, 1, 10, 30, 10026, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.hammer.", 10.0F, 1400, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1229 */     createItemTemplate(63, "mallet", "mallets", "excellent", "good", "ok", "poor", "A hammer with a thick head made entirely from wood. Normally used to bonk other wooden things together.", new short[] { 108, 44, 38, 21, 14, 119, 147, 165, 247 }, (short)741, (short)1, 20, 3024000L, 3, 10, 30, 10026, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.hammer.", 1.0F, 1200, (byte)14, 1000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1238 */     createItemTemplate(156, "mallet head", "mallet heads", "fresh", "dry", "brittle", "rotten", "A thick wooden hammerhead.", new short[] { 21, 147, 165 }, (short)721, (short)1, 0, 9072000L, 3, 5, 20, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.hammer.", 1.0F, 200, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1244 */     createItemTemplate(127, "hammer head", "hammer heads", "excellent", "good", "ok", "poor", "The thick metal head for a hammer.", new short[] { 22, 44 }, (short)722, (short)1, 0, 3024000L, 2, 4, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.hammer.", 4.0F, 400, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1251 */     createItemTemplate(219, "pliers", "pliers", "excellent", "good", "ok", "poor", "A long metal pliers for handling hot things.", new short[] { 108, 44, 38, 22, 147 }, (short)780, (short)1, 0, 3024000L, 2, 4, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.pliers.", 20.0F, 300, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1256 */     createItemTemplate(701, "branding iron", "branding iron", "excellent", "good", "ok", "poor", "A metal brand on a sturdy metal shaft.", new short[] { 108, 44, 38, 22 }, (short)780, (short)1, 0, 3024000L, 2, 4, 35, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.brandingiron.", 20.0F, 300, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1262 */     createItemTemplate(64, 2, "anvil", "anvils", "excellent", "good", "ok", "poor", "A small but heavy metal anvil.", new short[] { 108, 38, 22, 44, 157, 51 }, (short)791, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.anvil.small.", 5.0F, 2000, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1269 */     createItemTemplate(185, 4, "anvil", "anvils", "excellent", "good", "ok", "poor", "A large, heavy metal anvil.", new short[] { 108, 52, 38, 22, 44, 86, 67, 157, 51, 199, 1, 256, 139 }, (short)791, (short)1, 0, 12096000L, 30, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.anvil.large.", 20.0F, 10000, (byte)11, 10000, true, -1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1277 */       .setContainerSize(80, 40, 150);
/*      */     
/* 1279 */     createItemTemplate(65, "cheese drill", "cheese drills", "excellent", "good", "ok", "poor", "A wooden tube made from planks with a shaft to press in order to separate the whey from the curd, and to press the curd into a mould.", new short[] { 108, 44, 144, 38, 21, 92, 147, 51, 210 }, (short)266, (short)1, 0, 9072000L, 15, 15, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.cheesedrill.", 30.0F, 3000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1288 */     createItemTemplate(66, "cheese", "cheeses", "excellent", "good", "ok", "poor", "A mild cheese made from cow milk.", new short[] { 27, 5, 88, 146, 192, 212, 220, 224, 74 }, (short)561, (short)1, 0, 604800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cheese.", 5.0F, 500, (byte)28, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1298 */       .setNutritionValues(4020, 0, 380, 250)
/* 1299 */       .setFoodGroup(1198);
/*      */     
/* 1301 */     createItemTemplate(67, "goat cheese", "goat cheeses", "excellent", "good", "ok", "poor", "A tasty cheese made from goat milk.", new short[] { 27, 5, 88, 146, 192, 157, 212, 220, 224, 74 }, (short)561, (short)1, 0, 604800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cheese.goat.", 15.0F, 500, (byte)28, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1311 */       .setNutritionValues(4020, 0, 380, 250)
/* 1312 */       .setFoodGroup(1198);
/*      */     
/* 1314 */     createItemTemplate(68, "feta cheese", "feta cheeses", "excellent", "good", "ok", "poor", "A salty cheese made from sheep milk.", new short[] { 27, 5, 88, 146, 192, 212, 220, 224, 74 }, (short)561, (short)1, 0, 604800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cheese.feta.", 15.0F, 500, (byte)28, 10, true)
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
/* 1325 */       .setNutritionValues(4020, 0, 380, 250)
/* 1326 */       .setFoodGroup(1198);
/*      */     
/* 1328 */     createItemTemplate(69, "buffalo cheese", "buffalo cheeses", "excellent", "good", "ok", "poor", "A mild cheese with nutty taste made from buffalo milk.", new short[] { 27, 5, 88, 146, 192, 212, 220, 224, 74 }, (short)561, (short)1, 0, 604800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cheese.buffalo.", 25.0F, 500, (byte)28, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1338 */       .setNutritionValues(4020, 0, 380, 250)
/* 1339 */       .setFoodGroup(1198);
/*      */     
/* 1341 */     createItemTemplate(70, "honey", "honey", "excellent", "good", "ok", "poor", "A sweet food made by bees foraging nectar from flowers.", new short[] { 26, 5, 55, 88 }, (short)587, (short)1, 0, 4838400L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.honey.", 200.0F, 1000, (byte)29, 5, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1349 */       .setNutritionValues(3040, 820, 0, 3);
/*      */     
/* 1351 */     createItemTemplate(71, "hide", "hides", "excellent", "good", "ok", "poor", "A hide skinned from an animal.", new short[] { 23, 62, 129, 46, 146 }, (short)602, (short)1, 0, 172800L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.hide.", 1.0F, 3000, (byte)0, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1357 */     createItemTemplate(302, "fur", "furs", "excellent", "good", "ok", "poor", "A thick fur, skinned from an animal.", new short[] { 23, 62, 129, 46, 146 }, (short)603, (short)1, 0, 172800L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.fur.", 1.0F, 1000, (byte)55, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1364 */     createItemTemplate(303, "tooth", "teeth", "excellent", "good", "ok", "poor", "This sharp tooth was taken as a trophy.", new short[] { 105, 62, 129, 46, 146, 211 }, (short)495, (short)1, 0, 172800L, 1, 2, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.tooth.", 1.0F, 10, (byte)35, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1371 */     createItemTemplate(304, "horn", "horns", "excellent", "good", "ok", "poor", "A horn from an animal.", new short[] { 104, 23, 62, 129, 46, 146, 211 }, (short)496, (short)1, 0, 172800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.horn.", 1.0F, 300, (byte)35, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1377 */     createItemTemplate(312, "long horn", "long horns", "excellent", "good", "ok", "poor", "A long, sharp horn from an animal.", new short[] { 104, 62, 129, 46, 146, 211 }, (short)60, (short)1, 0, 172800L, 5, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.hornlong.", 1.0F, 300, (byte)35, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1384 */     createItemTemplate(311, "twisted horn", "horns", "excellent", "good", "ok", "poor", "A long twisted horn from an animal.", new short[] { 106, 62, 129, 46, 146, 211 }, (short)496, (short)1, 0, 172800L, 5, 5, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.horntwisted.", 1.0F, 300, (byte)35, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1391 */     createItemTemplate(305, "paw", "paws", "excellent", "good", "ok", "poor", "A paw from a butchered animal.", new short[] { 103, 62, 129, 46, 146 }, (short)535, (short)1, 0, 172800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.paw.", 1.0F, 100, (byte)2, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1398 */     createItemTemplate(306, "hoof", "hooves", "excellent", "good", "ok", "poor", "The hoof of a butchered animal.", new short[] { 103, 62, 129, 46, 146, 211 }, (short)534, (short)1, 0, 172800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.hoof.", 1.0F, 100, (byte)35, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1405 */     createItemTemplate(307, "tail", "tails", "excellent", "good", "ok", "poor", "This tail swings no more.", new short[] { 103, 62, 129, 46, 146, 211 }, (short)514, (short)1, 0, 172800L, 1, 1, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.tail.", 1.0F, 100, (byte)16, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1413 */     createItemTemplate(308, "eye", "eyes", "excellent", "good", "ok", "poor", "The eye looks back at you hollowly.", new short[] { 104, 28, 62, 5, 55, 129, 46, 146 }, (short)494, (short)1, 0, 172800L, 1, 1, 2, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.eye.", 1.0F, 10, (byte)2, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1423 */     createItemTemplate(636, "heart", "heart", "awesome", "good looking", "normal", "ugly", "A heart.", new short[] { 28, 5, 62, 74, 48, 106, 129, 46, 146 }, (short)516, (short)1, 0, 172800L, 4, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.heart.", 500.0F, 500, (byte)1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1432 */       .setNutritionValues(2500, 0, 100, 260);
/*      */     
/* 1434 */     createItemTemplate(309, "bladder", "bladders", "excellent", "good", "ok", "poor", "The sack where a now deceased animal used to keep its urine.", new short[] { 103, 62, 129, 46, 146, 211 }, (short)515, (short)1, 0, 172800L, 1, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.bladder.", 1.0F, 20, (byte)2, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1444 */     createItemTemplate(310, "gland", "glands", "excellent", "good", "ok", "poor", "A gland from a butchered animal, containing mysterious substances.", new short[] { 106, 62, 129, 46, 146, 211 }, (short)515, (short)1, 0, 172800L, 3, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.gland.", 1.0F, 10, (byte)2, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1454 */     createItemTemplate(313, "pelt", "pelts", "excellent", "good", "ok", "poor", "A fine pelt, skinned from an animal.", new short[] { 62 }, (short)602, (short)1, 0, 28800L, 1, 50, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.pelt.", 1.0F, 200, (byte)55, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1460 */     createItemTemplate(72, "leather", "leather", "excellent", "good", "ok", "poor", "The prepared hide of an animal.", new short[] { 23, 146 }, (short)602, (short)1, 0, 9072000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.leather.", 1.0F, 3000, (byte)16, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1465 */     createItemTemplate(73, "lye", "lye", "excellent", "good", "ok", "poor", "Made from ash and water, this is used when preparing leather.", new short[] { 26, 211 }, (short)540, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.lye.", 3.0F, 1000, (byte)27, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1471 */     createItemTemplate(74, "pile", "charcoal piles", "excellent", "good", "ok", "poor", "A large kiln-like structure made from logs covered in dirt. The purpose is to turn the logs into tar and charcoal.", new short[] { 52, 31, 1, 21, 51, 49, 48, 59, 63, 109 }, (short)60, (short)18, 0, 86400L, 100, 200, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.charcoalpile.", 15.0F, 2500000, (byte)27, 100, false);
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
/* 1486 */     createItemTemplate(153, "tar", "tar", "excellent", "good", "ok", "poor", "Black, greasy, sticky tar.", new short[] { 46, 34, 146 }, (short)585, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.tar.", 200.0F, 1000, (byte)58, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1494 */     createItemTemplate(204, "charcoal", "charcoal", "superb", "good", "ok", "poor", "Black, sooty charcoal.", new short[] { 46, 141, 146 }, (short)592, (short)1, 0, 28800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.coal.", 10.0F, 500, (byte)27, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1499 */     createItemTemplate(371, "drake hide", "drake hides", "excellent", "good", "ok", "poor", "The prepared hide from a drake.", new short[] { 23, 48, 62, 99, 46 }, (short)602, (short)1, 0, Long.MAX_VALUE, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.leather.dragon.", 20.0F, 500, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1505 */     createItemTemplate(372, "scale", "scales", "excellent", "good", "ok", "poor", "Light but extremely tough, these scales will withstand even the hardest of blows and the sharpest of cuts.", new short[] { 23, 48, 62, 99, 46 }, (short)554, (short)1, 0, Long.MAX_VALUE, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.scales.dragon.", 40.0F, 2500, (byte)16, 10000, true);
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
/* 1520 */     createItemTemplate(76, "pottery jar", "pottery jars", "excellent", "good", "ok", "poor", "A clay jar hardened by fire.", new short[] { 108, 30, 1, 33, 92, 215, 48, 211, 77 }, (short)510, (short)1, 0, 12096000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.jar.", 1.0F, 300, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1530 */     createItemTemplate(77, "pottery bowl", "pottery bowls", "excellent", "good", "ok", "poor", "A clay bowl hardened by fire.", new short[] { 108, 30, 1, 77, 92, 97, 33 }, (short)511, (short)1, 0, 12096000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.bowl.", 5.0F, 500, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1540 */     createItemTemplate(78, "pottery flask", "pottery flasks", "excellent", "good", "ok", "poor", "A clay flask hardened by fire.", new short[] { 108, 30, 1, 33, 92, 147, 215, 48 }, (short)512, (short)1, 0, 12096000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flask.", 15.0F, 100, (byte)19, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1550 */     createItemTemplate(181, "clay jar", "clay jars", "excellent", "good", "ok", "poor", "A clay jar that could be hardened by fire.", new short[] { 108, 196, 44, 147, 1, 63 }, (short)490, (short)1, 0, 172800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.jar.", 1.0F, 300, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1555 */     createItemTemplate(182, "clay bowl", "clay bowls", "excellent", "good", "ok", "poor", "A clay bowl that could be hardened by fire.", new short[] { 108, 196, 44, 147, 1, 63 }, (short)491, (short)1, 0, 172800L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.bowl.", 5.0F, 500, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1560 */     createItemTemplate(183, "clay flask", "clay flasks", "excellent", "good", "ok", "poor", "A clay flask that could be hardened by fire.", new short[] { 108, 196, 44, 147, 1, 63 }, (short)492, (short)1, 0, 172800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.flask.", 15.0F, 100, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1566 */     createItemTemplate(79, "water skin", "water skins", "excellent", "good", "ok", "poor", "A skin sown from leather with a wooden plug in a strap.", new short[] { 108, 44, 23, 1, 33, 147, 215, 48 }, (short)240, (short)1, 0, 3024000L, 1, 50, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.waterskin.", 20.0F, 250, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1574 */     createItemTemplate(443, "bag of keeping", "bags of keeping", "excellent", "good", "ok", "poor", "A small non-descript leather bag with a strap.", new short[] { 108, 23, 1, 42, 40, 246 }, (short)240, (short)1, 0, Long.MAX_VALUE, 1, 10, 20, -10, new byte[] { 41 }, "model.tool.bagkeeping.", 100.0F, 250, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1583 */     createItemTemplate(82, 2, "shield", "wooden shields", "excellent", "good", "ok", "poor", "A small sturdy shield made from wooden planks.", new short[] { 108, 97, 21, 3, 92, 147, 44 }, (short)970, (short)1, 0, 9072000L, 1, 20, 40, 10019, new byte[] { 3, 44 }, "model.shield.small.", 10.0F, 1000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1591 */     createItemTemplate(83, 2, "metal shield", "metal shields", "excellent", "good", "ok", "poor", "A small metal shield with a leather strap.", new short[] { 108, 44, 22, 3, 92, 147 }, (short)1010, (short)1, 0, 3024000L, 1, 20, 40, 10022, new byte[] { 3, 44 }, "model.shield.small.", 10.0F, 1500, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1599 */     createItemTemplate(84, "shield", "wooden shields", "excellent", "good", "ok", "poor", "A medium sized sturdy shield made from wooden planks with a metal spike.", new short[] { 108, 44, 21, 3, 92, 97, 147, 189 }, (short)971, (short)1, 0, 9072000L, 5, 30, 60, 10020, new byte[] { 3, 44 }, "model.shield.medium.", 20.0F, 2000, (byte)14, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1609 */     createItemTemplate(4, "shield", "metal shields", "excellent", "good", "ok", "poor", "A round shield made from a hammered metal sheet.", new short[] { 108, 44, 22, 3, 92, 147, 189 }, (short)1011, (short)1, 0, 3024000L, 5, 30, 60, 10006, new byte[] { 3, 44 }, "model.shield.medium.", 20.0F, 3000, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1616 */     createItemTemplate(85, 4, "shield", "wooden shields", "excellent", "good", "ok", "poor", "A large sturdy shield made from wooden planks.", new short[] { 108, 44, 21, 3, 92, 147 }, (short)972, (short)1, 0, 9072000L, 5, 40, 80, 10021, new byte[] { 3, 44 }, "model.shield.large.", 25.0F, 3000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1624 */     createItemTemplate(86, 4, "shield", "metal shields", "excellent", "good", "ok", "poor", "A large heavy shield hammered from a metal sheet.", new short[] { 108, 44, 22, 3, 92, 147 }, (short)1012, (short)1, 0, 3024000L, 5, 40, 80, 10023, new byte[] { 3, 44 }, "model.shield.large.", 30.0F, 4500, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1632 */     createItemTemplate(3, 2, "axe", "small axes", "superb", "good", "ok", "poor", "A short but sturdy axe with a thick blade.", new short[] { 108, 44, 37, 22, 15, 2, 147 }, (short)1207, (short)1, 20, 9072000L, 1, 10, 40, 10001, new byte[0], "model.weapon.axe.small.", 11.0F, 1500, (byte)11, 10000, false, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1640 */     createItemTemplate(523, "hatchet head", "hatchet heads", "excellent", "good", "ok", "poor", "A large axe head fit for a hatchet.", new short[] { 44, 22 }, (short)1206, (short)1, 0, 3024000L, 1, 10, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.hatchet.blade.", 10.0F, 1500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1646 */     createItemTemplate(89, 2, "axe head", "axe heads", "excellent", "good", "ok", "poor", "A small axe head.", new short[] { 44, 22 }, (short)1206, (short)1, 0, 3024000L, 1, 7, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.axe.small.blade.", 10.0F, 500, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1651 */     createItemTemplate(88, 5, "axe head", "axe heads", "excellent", "good", "ok", "poor", "A huge axe head.", new short[] { 44, 22 }, (short)1226, (short)1, 0, 3024000L, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.axe.large.blade.", 20.0F, 1500, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1656 */     createItemTemplate(91, 3, "axe head", "axe heads", "excellent", "good", "ok", "poor", "A medium sized battle axe head.", new short[] { 44, 22 }, (short)1206, (short)1, 0, 3024000L, 1, 10, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.axe.medium.blade.", 15.0F, 1000, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1662 */     createItemTemplate(90, 3, "axe", "medium axes", "excellent", "good", "ok", "poor", "A medium sized battle axe with a wooden shaft.", new short[] { 108, 44, 22, 2, 15, 37, 147, 189 }, (short)1207, (short)1, 30, 3024000L, 4, 10, 80, 10024, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.axe.medium.", 15.0F, 2000, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1669 */     createItemTemplate(87, 5, "axe", "huge axes", "excellent", "good", "ok", "poor", "A huge axe with a heavy head and a wooden shaft. You'll need to wield it with both hands.", new short[] { 108, 44, 22, 2, 15, 37, 84, 147, 189 }, (short)1227, (short)1, 60, 3024000L, 4, 15, 100, 10025, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.axe.large.", 35.0F, 2500, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1677 */     createItemTemplate(80, "short sword", "short swords", "excellent", "good", "ok", "poor", "A sword with a blade the length of an underarm.", new short[] { 108, 44, 22, 13, 37, 16, 97, 147 }, (short)1204, (short)1, 10, 3024000L, 1, 4, 75, 10027, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.swordshort.", 10.0F, 2000, (byte)11, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1685 */     createItemTemplate(21, "longsword", "longswords", "superb", "good", "ok", "poor", "A long and slender sword.", new short[] { 108, 44, 37, 22, 16, 2, 147, 189, 97 }, (short)1224, (short)1, 40, 12096000L, 1, 6, 110, 10005, new byte[0], "model.weapon.swordlong.", 30.0F, 3000, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1692 */     createItemTemplate(81, "two handed sword", "two handed swords", "excellent", "good", "ok", "poor", "A large heavy sword almost as tall as a ten year old child.", new short[] { 108, 44, 22, 2, 37, 16, 84, 147, 189 }, (short)1244, (short)1, 70, 3024000L, 1, 10, 140, 10028, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.swordtwo.", 40.0F, 4000, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1700 */     createItemTemplate(147, "short sword blade", "short sword blades", "superb", "good", "ok", "poor", "A blade for a shortsword.", new short[] { 44, 22 }, (short)1203, (short)1, 0, 3024000L, 1, 4, 70, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.swordshort.", 1.0F, 1900, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1705 */     createItemTemplate(148, "long sword blade", "long sword blades", "superb", "good", "ok", "poor", "A blade for a longsword.", new short[] { 44, 22 }, (short)1223, (short)1, 0, 3024000L, 1, 6, 110, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.swordlong.", 1.0F, 2900, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1710 */     createItemTemplate(149, 5, "sword blade", "sword blades", "superb", "good", "ok", "poor", "A blade for a giant sword.", new short[] { 44, 22 }, (short)1243, (short)1, 0, 3024000L, 1, 10, 140, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.swordtwo.", 2.0F, 3900, (byte)11, 100, false, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1717 */     createItemTemplate(711, "staff", "staves", "fresh", "dry", "brittle", "rotten", "A long, well polished staff that could be useful as a rudimentary weapon or aiding the wanderer.", new short[] { 133, 21, 14, 154, 144, 37, 84, 129 }, (short)646, (short)1, 35, 9072000L, 3, 4, 200, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.staff.", 10.0F, 2000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1725 */     createItemTemplate(710, "staff", "staves", "fresh", "dry", "brittle", "rotten", "A shiny steel staff etched with decorations that works as a weapon itself but which may be fitted with a blade for that little extra punch.", new short[] { 44, 133, 22, 14, 154, 37, 84, 129 }, (short)646, (short)1, 45, 9072000L, 3, 4, 150, 10090, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.staff.", 10.0F, 3000, (byte)9, 10000, true);
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
/* 1740 */     createItemTemplate(705, "long spear", "long spears", "excellent", "good", "ok", "poor", "A long well-polished shaft with a heavy sharp blade attached at one end.", new short[] { 133, 108, 44, 21, 13, 37, 154, 84, 147, 189 }, (short)1221, (short)1, 50, 3024000L, 3, 5, 205, 10088, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.spear.long.", 20.0F, 2700, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1750 */     createItemTemplate(707, "spear", "spears", "excellent", "good", "ok", "poor", "A sturdy spear.", new short[] { 133, 108, 44, 22, 13, 37, 154, 84, 147 }, (short)1221, (short)1, 90, 3024000L, 3, 5, 180, 10088, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.spear.", 40.0F, 3700, (byte)9, 30000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1758 */     createItemTemplate(706, "halberd", "halberds", "excellent", "good", "ok", "poor", "A long polearm with an head looking both like an axe and a spear at the same time. Excellent against charging attackers.", new short[] { 133, 108, 44, 22, 2, 37, 154, 84, 147, 189 }, (short)1247, (short)1, 70, 3024000L, 3, 10, 205, 10089, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.polearm.halberd.", 40.0F, 4500, (byte)11, 10000, true);
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
/* 1775 */     createItemTemplate(709, "spear tip", "spear tips", "excellent", "good", "ok", "poor", "The long heavy pointy blade for a spear.", new short[] { 44, 22 }, (short)1220, (short)1, 0, 3024000L, 3, 3, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.spear.long.", 5.0F, 700, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1781 */     createItemTemplate(708, 5, "halberd head", "halberd heads", "excellent", "good", "ok", "poor", "A huge halberd head. It looks like an axe but is also a spear at the top.", new short[] { 44, 22 }, (short)1246, (short)1, 0, 3024000L, 3, 20, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.halberd.", 15.0F, 2500, (byte)11, 100, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1788 */     createItemTemplate(98, "scabbard", "scabbards", "excellent", "good", "ok", "poor", "A leather scabbard for protecting the sword and also helping carrying it.", new short[] { 108, 44, 23, 92, 157 }, (short)762, (short)1, 0, 3024000L, 2, 10, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.clothing.scabbard.", 10.0F, 200, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1795 */     createItemTemplate(100, "strip of leather", "strips of leather", "excellent", "good", "ok", "poor", "A thin and long leather strip.", new short[] { 23, 146 }, (short)622, (short)1, 0, 3024000L, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.strip.", 1.0F, 30, (byte)16, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1802 */     createItemTemplate(99, "handle", "wooden handles", "excellent", "good", "ok", "poor", "A finely carved wooden handle that would fit well on a sword.", new short[] { 21, 146, 113, 211 }, (short)666, (short)1, 0, 9072000L, 2, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.handle.", 5.0F, 100, (byte)14, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1809 */     createItemTemplate(101, "leather wound handle", "leather wound handles", "excellent", "good", "ok", "poor", "A wooden handle wound with leather.", new short[] { 21 }, (short)765, (short)1, 0, 3024000L, 2, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.leatherwoundhandle.", 5.0F, 130, (byte)14, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1815 */     createItemTemplate(97, "stone chisel", "stone chisels", "excellent", "good", "ok", "poor", "A straight tool with a strong hard blade made for cutting stone.", new short[] { 108, 44, 38, 22, 13, 147 }, (short)1201, (short)1, 20, 3024000L, 1, 2, 15, 10030, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.chisel.", 7.0F, 400, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1823 */     createItemTemplate(102, "leather belt", "leather belts", "excellent", "good", "ok", "poor", "A thick strip of leather with holes and a metal husk.", new short[] { 108, 44, 23, 92, 147 }, (short)861, (short)1, 0, 3024000L, 1, 4, 10, -10, new byte[] { 34, 43 }, "model.clothing.belt.", 5.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1830 */     createItemTemplate(103, 3, "leather glove", "leather gloves", "excellent", "good", "ok", "poor", "A glove made from leather.", new short[] { 108, 44, 23, 4, 92, 147, 189, 97 }, (short)984, (short)1, 0, 3024000L, 1, 5, 15, -10, new byte[] { 13, 14 }, "model.armour.hand.", 10.0F, 200, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1839 */     createItemTemplate(104, 3, "leather jacket", "leather jackets", "excellent", "good", "ok", "poor", "A jacket made from leather with wooden husks.", new short[] { 108, 44, 23, 4, 92, 147, 189, 97 }, (short)980, (short)1, 0, 3024000L, 2, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.", 30.0F, 600, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1847 */     createItemTemplate(105, 3, "leather boot", "leather boots", "excellent", "good", "ok", "poor", "A sturdy boot made from leather.", new short[] { 108, 44, 23, 4, 92, 147, 189, 97 }, (short)985, (short)1, 0, 3024000L, 5, 20, 30, -10, new byte[] { 15, 16 }, "model.armour.foot.", 15.0F, 400, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1856 */     createItemTemplate(107, 3, "leather cap", "leather caps", "excellent", "good", "ok", "poor", "A leather cap with ear flaps and a strap.", new short[] { 108, 44, 23, 4, 92, 147, 189, 97 }, (short)983, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.", 10.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1864 */     createItemTemplate(108, 3, "leather pants", "leather pants", "excellent", "good", "ok", "poor", "Leg protection made from leather.", new short[] { 108, 44, 23, 4, 92, 147, 189, 97 }, (short)981, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.armour.leg.", 10.0F, 500, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1872 */     createItemTemplate(106, 3, "leather sleeve", "leather sleeves", "excellent", "good", "ok", "poor", "Leather sown into a cylinder to protect the arms.", new short[] { 108, 44, 23, 4, 92, 147, 189, 97 }, (short)982, (short)1, 0, 3024000L, 1, 5, 10, -10, new byte[] { 3, 4 }, "model.armour.sleeve.", 25.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1881 */     createItemTemplate(468, 3, "drake hide sleeve", "drake hide sleeves", "excellent", "good", "ok", "poor", "Drake hide sown into a cylinder to protect the arms.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1062, (short)1, 0, 29030400L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.armour.sleeve.dragon.", 50.0F, 300, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1890 */     createItemTemplate(472, 3, "drake hide glove", "drake hide gloves", "excellent", "good", "ok", "poor", "A glove made from smooth glistening drake hide.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1064, (short)1, 0, 29030400L, 1, 5, 15, -10, new byte[] { 13, 14 }, "model.armour.hand.dragon.", 50.0F, 200, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1899 */     createItemTemplate(473, 3, "drake hide jacket", "drake hide jackets", "excellent", "good", "ok", "poor", "A jacket made from finest drake hide with brass husks.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1060, (short)1, 0, 29030400L, 2, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.dragon.", 70.0F, 600, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1907 */     createItemTemplate(469, 3, "drake hide boot", "drake hide boots", "excellent", "good", "ok", "poor", "A sturdy boot made from shiny drake hide.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1065, (short)1, 0, 29030400L, 5, 20, 30, -10, new byte[] { 15, 16 }, "model.armour.foot.dragon.", 55.0F, 400, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1916 */     createItemTemplate(470, 3, "drake hide cap", "drake hide caps", "excellent", "good", "ok", "poor", "A drake hide cap with ear flaps and a strap.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1063, (short)1, 0, 29030400L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.dragon.", 60.0F, 300, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1925 */     createItemTemplate(471, 3, "drake hide pants", "drake hide pants", "excellent", "good", "ok", "poor", "Comfortable and endurable leg protection made from drake hide.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1061, (short)1, 0, 29030400L, 1, 10, 40, -10, new byte[] { 34 }, "model.armour.leg.dragon.", 50.0F, 500, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1934 */     createItemTemplate(475, 3, "dragon scale pants", "dragon scale pants", "excellent", "good", "ok", "poor", "Leg protection made from strong dragon scales.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1021, (short)1, 0, 29030400L, 1, 10, 40, -10, new byte[] { 34 }, "model.armour.leg.dragon.scale.", 50.0F, 1200, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1942 */     createItemTemplate(474, 3, "dragon scale boot", "dragon scale boots", "excellent", "good", "ok", "poor", "A protective leather boot made from dragon scales.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1025, (short)1, 0, 29030400L, 5, 20, 30, -10, new byte[] { 15, 16 }, "model.armour.foot.dragon.scale.", 40.0F, 500, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1951 */     createItemTemplate(476, 3, "dragon scale jacket", "dragon scale jackets", "excellent", "good", "ok", "poor", "A heavy jacket made from dragon scales.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1020, (short)1, 0, 29030400L, 3, 35, 40, -10, new byte[] { 2 }, "model.armour.torso.dragon.scale.", 65.0F, 2200, (byte)0, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1960 */     createItemTemplate(478, 3, "dragon scale glove", "dragon scale gloves", "excellent", "good", "ok", "poor", "A soft leather glove covered on the top with dragon scales.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1024, (short)1, 0, 29030400L, 2, 5, 16, -10, new byte[] { 13, 14 }, "model.armour.hand.dragon.scale.", 30.0F, 300, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1969 */     createItemTemplate(477, 3, "dragon scale sleeve", "dragon scale sleeves", "excellent", "good", "ok", "poor", "Leather sown into a cylinder to protect the arms and covered with thick dragon scales.", new short[] { 108, 44, 23, 4, 99, 249 }, (short)1022, (short)1, 0, 29030400L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.armour.sleeve.dragon.scale.", 35.0F, 400, (byte)16, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1979 */     createItemTemplate(109, 3, "cloth glove", "cloth gloves", "excellent", "good", "ok", "poor", "A thick cloth glove.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)964, (short)1, 0, 3024000L, 1, 5, 15, -10, new byte[] { 13, 14 }, "model.armour.hand.", 5.0F, 100, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1985 */     createItemTemplate(110, 3, "cloth shirt", "cloth shirts", "excellent", "good", "ok", "poor", "A fine but simple shirt made from cloth.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)960, (short)1, 0, 3024000L, 1, 35, 40, -10, new byte[] { 2 }, "model.clothing.torso.shirt.", 15.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1992 */     createItemTemplate(111, 3, "red striped cloth sleeve", "red striped cloth sleeves", "excellent", "good", "ok", "poor", "Cloth studded with wool and sown into a cylinder to protect the arms.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)962, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.armour.sleeve.red.", 10.0F, 200, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1999 */     createItemTemplate(1426, 3, "white cloth sleeve", "white cloth sleeves", "excellent", "good", "ok", "poor", "Cloth studded with wool and sown into a cylinder to protect the arms.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)962, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.armour.sleeve.", 10.0F, 200, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2007 */     createItemTemplate(112, 3, "red cloth jacket", "red cloth jackets", "excellent", "good", "ok", "poor", "A fine but simple jacket made from cloth studded with wool.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)960, (short)1, 0, 3024000L, 3, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.red.", 30.0F, 500, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2014 */     createItemTemplate(1427, 3, "white cloth jacket", "white cloth jackets", "excellent", "good", "ok", "poor", "A fine but simple jacket made from cloth studded with wool.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)960, (short)1, 0, 3024000L, 3, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.", 30.0F, 500, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2022 */     createItemTemplate(113, 3, "cloth pants", "cloth pants", "excellent", "good", "ok", "poor", "Leg protection sown from thick cloth.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)961, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.armour.leg.", 10.0F, 300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2030 */     createItemTemplate(120, 3, "studded leather jacket", "studded leather jackets", "excellent", "good", "ok", "poor", "A fine leather jacket strengthened with metal studs.", new short[] { 108, 44, 23, 4, 92, 147 }, (short)1000, (short)1, 0, 3024000L, 3, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.studded.", 45.0F, 900, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2037 */     createItemTemplate(114, 3, "cloth shoe", "cloth shoes", "excellent", "good", "ok", "poor", "Thick cloth strands interwowen with cotton and hay.", new short[] { 108, 44, 24, 4, 92, 147 }, (short)965, (short)1, 0, 3024000L, 5, 20, 30, -10, new byte[] { 15, 16 }, "model.armour.foot.", 15.0F, 200, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2045 */     createItemTemplate(118, 3, "studded leather pants", "studded leather pants", "excellent", "good", "ok", "poor", "Leg protection made from leather strengthened with metal studs.", new short[] { 108, 44, 23, 4, 92, 147 }, (short)1001, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.armour.leg.studded.", 20.0F, 1200, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2052 */     createItemTemplate(116, 3, "studded leather boot", "studded leather boots", "excellent", "good", "ok", "poor", "A leather boot strengthened with metal studs.", new short[] { 108, 44, 23, 4, 92, 147 }, (short)1005, (short)1, 0, 3024000L, 5, 20, 30, -10, new byte[] { 15, 16 }, "model.armour.foot.studded.", 20.0F, 500, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2060 */     createItemTemplate(117, 3, "studded leather cap", "studded leather caps", "excellent", "good", "ok", "poor", "A leather cap strengthened with metal studs.", new short[] { 108, 44, 23, 4, 92, 147 }, (short)1003, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.studded.", 15.0F, 400, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2068 */     createItemTemplate(119, 3, "studded leather glove", "studded leather gloves", "excellent", "good", "ok", "poor", "A leather glove strengthened with metal studs.", new short[] { 108, 44, 23, 4, 92, 147 }, (short)1004, (short)1, 0, 3024000L, 2, 5, 16, -10, new byte[] { 13, 14 }, "model.armour.hand.studded.", 20.0F, 300, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2076 */     createItemTemplate(115, 3, "studded leather sleeve", "studded leather sleeves", "excellent", "good", "ok", "poor", "Leather sown into a cylinder to protect the arms and strengthened with metal studs.", new short[] { 108, 44, 23, 4, 92, 147 }, (short)1002, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.armour.sleeve.studded.", 15.0F, 400, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2086 */     createItemTemplate(288, "armour chains", "armour chains", "new", "fancy", "ok", "old", "A piece of thick interwoven chain links.", new short[] { 22, 46, 146, 158 }, (short)604, (short)1, 0, 3024000L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.chain.", 15.0F, 500, (byte)0, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2092 */     createItemTemplate(276, 3, "chain jacket", "chain jackets", "excellent", "good", "ok", "poor", "A heavy jacket made from metal chain.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1040, (short)1, 0, 3024000L, 3, 35, 40, -10, new byte[] { 2 }, "model.armour.torso.chain.", 65.0F, 2200, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2100 */     createItemTemplate(275, 3, "chain pants", "chain pants", "excellent", "good", "ok", "poor", "Leg protection made from metal chain.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1041, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.armour.leg.chain.", 50.0F, 2200, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2108 */     createItemTemplate(274, 3, "chain boot", "chain boots", "excellent", "good", "ok", "poor", "A sock made from metal chain.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1045, (short)1, 0, 3024000L, 5, 20, 30, -10, new byte[] { 15, 16 }, "model.armour.foot.chain.", 30.0F, 700, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2116 */     createItemTemplate(277, 3, "chain sleeve", "chain sleeves", "excellent", "good", "ok", "poor", "Chains sown into a cylinder to protect the arms.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1042, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.armour.sleeve.chain.", 45.0F, 700, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2125 */     createItemTemplate(278, 3, "chain gauntlet", "chain gauntlet", "excellent", "good", "ok", "poor", "A chain glove.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1044, (short)1, 0, 3024000L, 2, 5, 16, -10, new byte[] { 13, 14 }, "model.armour.hand.chain.", 40.0F, 300, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2134 */     createItemTemplate(279, 3, "chain coif", "chain coifs", "excellent", "good", "ok", "poor", "A heavy chain coif, worn on the head.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1043, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.chain.", 35.0F, 400, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2144 */     createItemTemplate(282, 3, "breast plate", "breast plates", "excellent", "good", "ok", "poor", "A heavy and intricate set of metal plates, together covering the top of the body.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1080, (short)1, 0, 3024000L, 3, 35, 40, -10, new byte[] { 2 }, "model.armour.torso.plate.", 50.0F, 5200, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2152 */     createItemTemplate(281, 3, "plate leggings", "plate leggings", "excellent", "good", "ok", "poor", "Greaves for the lower part of the legs, a cuisse for the upper part, joined by a poleyne covering the knee.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1081, (short)1, 0, 3024000L, 1, 10, 40, -10, new byte[] { 34 }, "model.armour.leg.plate.", 45.0F, 4200, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2161 */     createItemTemplate(280, 3, "plate sabaton", "plate sabatons", "excellent", "good", "ok", "poor", "Plate armour for the foot, with plenty of space for the toes.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1085, (short)1, 0, 3024000L, 5, 20, 30, -10, new byte[] { 15, 16 }, "model.armour.foot.plate.", 35.0F, 1700, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2170 */     createItemTemplate(283, 3, "plate vambrace", "plate vambraces", "excellent", "good", "ok", "poor", "A protection closed completely around the lower arm and secured shut with spring snaps.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1082, (short)1, 0, 3024000L, 1, 5, 40, -10, new byte[] { 3, 4 }, "model.armour.sleeve.plate.", 40.0F, 2200, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2179 */     createItemTemplate(284, 3, "plate gauntlet", "plate gauntlet", "excellent", "good", "ok", "poor", "A glove made from small metal plates.", new short[] { 108, 44, 22, 4, 189, 147, 92 }, (short)1084, (short)1, 0, 3024000L, 2, 5, 16, -10, new byte[] { 13, 14 }, "model.armour.hand.plate.", 30.0F, 800, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2188 */     createItemTemplate(285, 3, "basinet helm", "basinet helms", "excellent", "good", "ok", "poor", "A shining basinet helm with a lift up visor.", new short[] { 108, 44, 22, 4, 147, 92 }, (short)967, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.basinet.", 20.0F, 1700, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2197 */     createItemTemplate(286, 3, "great helm", "great helms", "excellent", "good", "ok", "poor", "A thick helm that only exposes a pair of slits where the eyes are.", new short[] { 108, 44, 22, 4, 92, 147 }, (short)968, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.greathelm.", 25.0F, 1700, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2206 */     createItemTemplate(287, 3, "open helm", "open helms", "excellent", "good", "ok", "poor", "An open-faced round-top helm.", new short[] { 108, 44, 22, 4, 77, 92, 147, 1, 33, 139 }, (short)966, (short)1, 0, 3024000L, 10, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.openhelm.", 15.0F, 1100, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2214 */     createItemTemplate(121, "shovel blade", "shovel blades", "excellent", "good", "ok", "poor", "A metal blade that would make a shovel were it not for the lack of a shaft.", new short[] { 22, 92, 44 }, (short)726, (short)1, 0, 3024000L, 1, 15, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.shovel.blade.", 10.0F, 1000, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2220 */     createItemTemplate(122, "shovel blade", "wooden shovel blades", "excellent", "good", "ok", "poor", "A wooden blade that would make a shovel were it not for the lack of a shaft.", new short[] { 21 }, (short)726, (short)1, 0, 9072000L, 1, 15, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.shovel.blade.", 5.0F, 600, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2226 */     createItemTemplate(123, "pickaxe head", "pickaxe heads", "excellent", "good", "ok", "poor", "The strong iron head of a pick.", new short[] { 22, 44 }, (short)723, (short)1, 0, 3024000L, 1, 5, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.pickaxe.blade.", 15.0F, 1000, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2232 */     createItemTemplate(20, "pickaxe", "pickaxes", "superb", "good", "ok", "poor", "A tool for mining.", new short[] { 108, 44, 38, 22, 10, 2, 97, 247 }, (short)743, (short)1, 30, 9072000L, 1, 30, 70, 10009, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.pickaxe.", 20.0F, 2000, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2239 */     createItemTemplate(136, "oil lamp", "oil lamps", "excellent", "good", "ok", "poor", "A brass lamp with a wick.", new short[] { 52, 108, 44, 22, 32, 92, 59, 115, 101, 147 }, (short)823, (short)1, 0, 3024000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.oillamp.", 40.0F, 300, (byte)30, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2245 */       .setDyeAmountGrams(20);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2253 */     createItemTemplate(138, "torch", "torches", "excellent", "good", "ok", "poor", "A piece of wood with tar and moss.", new short[] { 21, 146, 32, 52, 59, 147 }, (short)820, (short)1, 0, 9072000L, 3, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.torch.", 1.0F, 1000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2259 */     createItemTemplate(649, "light token", "tokens", "excellent", "good", "ok", "poor", "A brightly glowing white stone.", new short[] { 25, 32, 52, 59, 45, 156 }, (short)820, (short)1, 0, 300L, 3, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.token.", 1.0F, 100, (byte)15, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2266 */     createItemTemplate(139, "spindle", "wooden spindles", "excellent", "good", "ok", "poor", "A spinning tool used to make cloth strings.", new short[] { 108, 44, 38, 21, 147 }, (short)787, (short)1, 0, 9072000L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.spindle.", 10.0F, 600, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2272 */     createItemTemplate(140, "fat", "fat", "excellent", "good", "ok", "poor", "Collected from a creature's belly, this can be used to create candles and in cooking.", new short[] { 146, 46, 211 }, (short)498, (short)1, 0, 86400L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.tallow.", 1.0F, 1000, (byte)32, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2280 */     createItemTemplate(141, "ash", "ash", "excellent", "good", "ok", "poor", "Blended with water, this would produce lye.", new short[] { 146, 25, 46, 113, 129 }, (short)641, (short)1, 0, 86400L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ash.", 1.0F, 100, (byte)27, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2286 */     createItemTemplate(128, "water", "water", "excellent", "good", "ok", "poor", "A clear scentless liquid.", new short[] { 26, 88, 90, 113, 212 }, (short)540, (short)1, 0, Long.MAX_VALUE, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 1.0F, 1000, (byte)26)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2295 */       .setNutritionValues(0, 0, 0, 0);
/*      */     
/* 2297 */     createItemTemplate(142, "milk", "milk", "excellent", "good", "ok", "poor", "White, frothing, full fat milk.", new short[] { 26, 88, 90, 113, 191, 212 }, (short)541, (short)1, 0, 3600L, 10, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 1.0F, 1000, (byte)28)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2304 */       .setNutritionValues(420, 50, 10, 34)
/* 2305 */       .setFoodGroup(1200);
/*      */     
/* 2307 */     createItemTemplate(143, "steel and flint", "flints and steel", "excellent", "good", "ok", "poor", "A piece of sharp flint and a steel handle, which will produce sparks when struck against each other.", new short[] { 44, 38, 22, 97 }, (short)783, (short)1, 0, Long.MAX_VALUE, 2, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.small.flintandsteel.", 10.0F, 400, (byte)9, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2315 */     createItemTemplate(145, "cotton seed", "cotton seed", "superb", "good", "ok", "poor", "A few handfuls of cotton seed. It can be used to sow with.", new short[] { 20, 146, 113, 211, 217 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.cottonseed.", 1.0F, 100, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2324 */     createItemTemplate(144, "cotton", "cotton", "superb", "good", "ok", "poor", "A bale of cotton.", new short[] { 24, 146, 46, 113 }, (short)600, (short)16, 0, 28800L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.cotton.", 1.0F, 1000, (byte)17, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2331 */       .setPickSeeds(145);
/*      */     
/* 2333 */     createItemTemplate(124, "rake blade", "rake blades", "excellent", "good", "ok", "poor", "A thin blade for a rake.", new short[] { 22, 44 }, (short)725, (short)1, 0, 3024000L, 1, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.rake.", 10.0F, 400, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2338 */     createItemTemplate(125, "butchering knife blade", "butchering knife blades", "excellent", "good", "ok", "poor", "The bent blade of a butchering knife.", new short[] { 44, 22 }, (short)735, (short)1, 0, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.knife.butchering.", 10.0F, 1100, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2344 */     createItemTemplate(93, "butchering knife", "butchering knives", "excellent", "good", "ok", "poor", "A heavy knife with a bent blade perfect for butchering.", new short[] { 108, 44, 38, 22, 17, 210, 13, 147 }, (short)755, (short)1, 1, 3024000L, 1, 3, 21, 10029, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.knife.butchering.", 1.0F, 1200, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2352 */     createItemTemplate(126, "carving knife blade", "carving knife blades", "excellent", "good", "ok", "poor", "The small, sharp blade of a carving knife.", new short[] { 44, 22, 119 }, (short)1200, (short)1, 0, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.knife.carving.", 10.0F, 300, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2359 */     createItemTemplate(8, "carving knife", "carving knives", "superb", "good", "ok", "poor", "Made for carving, this knife has a broad blade and half a hilt.", new short[] { 108, 44, 38, 22, 17, 13, 11, 97, 119 }, (short)1201, (short)1, 1, 3024000L, 1, 3, 10, 10007, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.knife.carving.", 10.0F, 400, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2368 */     createItemTemplate(129, "cooked meat", "cooked meat", "excellent", "good", "ok", "poor", "Cooked meat.", new short[] { 28, 146, 5, 212, 74, 219, 157 }, (short)523, (short)1, 0, 172800L, 2, 9, 14, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cooked.", 1.0F, 900, (byte)2, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2377 */       .setFoodGroup(1261);
/*      */     
/* 2379 */     createItemTemplate(131, "rivets", "rivets", "excellent", "good", "ok", "poor", "Small pieces of metal. They could be used to strengthen things.", new short[] { 22, 46, 146, 158 }, (short)770, (short)1, 0, 3024000L, 1, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.studs.", 1.0F, 200, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2385 */     createItemTemplate(444, "wires", "metal wires", "excellent", "good", "ok", "poor", "Small needle-like strings of metal. They rasp the skin.", new short[] { 44, 22, 46, 146, 147 }, (short)555, (short)1, 0, 3024000L, 4, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.wires.", 10.0F, 400, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2391 */     createItemTemplate(133, "candle", "candles", "excellent", "good", "ok", "poor", "A candle made from tallow repeatedly dipped around a cloth wicker.", new short[] { 27, 32, 59, 115, 52, 210 }, (short)821, (short)1, 0, 29030400L, 1, 1, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.candle.", 1.0F, 50, (byte)32, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2398 */     createItemTemplate(135, "lantern", "lanterns", "excellent", "good", "ok", "poor", "A small iron box with wicker and a cannister for oil.", new short[] { 108, 101, 44, 22, 92, 116, 59, 52, 147 }, (short)822, (short)1, 0, 3024000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lantern.", 30.0F, 800, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2405 */     createItemTemplate(497, "lamp head", "lamp heads", "excellent", "good", "ok", "poor", "A decorative iron box with wicker and a cannister for oil, with a socket for fitting on a pole.", new short[] { 108, 44, 22, 92, 59 }, (short)822, (short)1, 0, 3024000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lantern.head.", 40.0F, 800, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2411 */     createItemTemplate(674, "hanging lamp head", "lamp heads", "excellent", "good", "ok", "poor", "A decorative iron box with wicker and a cannister for oil, with a socket for fitting on a pole.", new short[] { 108, 44, 22, 92, 59 }, (short)822, (short)1, 0, 3024000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lantern.hanging.", 50.0F, 800, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2418 */     createItemTemplate(675, "imperial lamp head", "lamp heads", "excellent", "good", "ok", "poor", "A decorative iron box with wicker and a cannister for oil, with a socket for fitting on a pole.", new short[] { 108, 44, 22, 92, 59 }, (short)822, (short)1, 0, 3024000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lantern.metal.", 55.0F, 800, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2424 */     createItemTemplate(496, "lamp", "lamps", "fresh", "dry", "brittle", "rotten", "A decorative lamp head on a wooden post.", new short[] { 108, 22, 143, 48, 51, 52, 101, 44, 86, 116, 92, 199 }, (short)825, (short)1, 20, 4838400L, 2, 20, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lamp.street.", 25.0F, 5000, (byte)11, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2431 */       .setDyeAmountGrams(100);
/* 2432 */     createItemTemplate(658, "hanging lamp", "lamps", "fresh", "dry", "brittle", "rotten", "A decorative lamp head on a wooden pole.", new short[] { 108, 22, 143, 48, 51, 52, 101, 44, 86, 116, 92, 199 }, (short)825, (short)1, 20, 4838400L, 2, 20, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lamp.street.pole.", 30.0F, 5000, (byte)11, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2440 */       .setDyeAmountGrams(100);
/* 2441 */     createItemTemplate(657, "torch lamp", "lamps", "fresh", "dry", "brittle", "rotten", "A decorative torch on a wooden pole.", new short[] { 108, 22, 143, 48, 51, 52, 101, 44, 86, 116, 92, 199 }, (short)825, (short)1, 20, 4838400L, 2, 20, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lamp.street.torch.", 35.0F, 5000, (byte)11, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2449 */       .setDyeAmountGrams(100);
/* 2450 */     createItemTemplate(659, "imperial street lamp", "lamps", "fresh", "dry", "brittle", "rotten", "A decorative torch on a metal post.", new short[] { 108, 22, 143, 48, 51, 52, 101, 44, 86, 116, 92, 199 }, (short)825, (short)1, 20, 4838400L, 2, 20, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.lamp.street.metal.", 45.0F, 25000, (byte)11, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2459 */       .setDyeAmountGrams(100);
/* 2460 */     createItemTemplate(660, "metal torch", "torches", "excellent", "good", "ok", "poor", "A finely bent metal sheet that will hold some oil and a wicker.", new short[] { 108, 101, 44, 22, 92, 32, 59, 52, 51 }, (short)822, (short)1, 0, 3024000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.torch.metal.", 25.0F, 800, (byte)11, 1000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2468 */       .setDyeAmountGrams(100);
/*      */     
/* 2470 */     createItemTemplate(154, "chisel blade", "chisel blades", "excellent", "good", "ok", "poor", "A wide, flat blade that is tapped along the cut line to produce a groove.", new short[] { 44, 22 }, (short)720, (short)1, 0, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.chisel.", 3.0F, 300, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2477 */     createItemTemplate(166, "writ of ownership", "writs of ownership", "new", "fancy", "ok", "old", "A writ of ownership.", new short[] { 40, 41, 42, 53, 54, 83, 114 }, (short)322, (short)21, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.", 1.0F, 0, (byte)33, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2483 */     createItemTemplate(174, "wand of teleportation", "wands of teleportation", "superb", "good", "ok", "poor", "A wand made of deep red wood sprinkled with silver dust.", new short[] { 108, 40, 21, 48, 53 }, (short)400, (short)1, 0, 12096000L, 1, 1, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.teleport.", 50.0F, 20, (byte)14, 1000000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2489 */     createItemTemplate(175, "gift", "gifts", "superb", "good", "ok", "poor", "A long round, green packet with golden bands. The sigil is red with a coiled up snake.", new short[] { 53, 42 }, (short)243, (short)1, 0, 12096000L, 1, 1, 11, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.presentchristmas.", 10.0F, 23, (byte)33, 1000100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2495 */     createItemTemplate(651, "gift box", "giftboxes", "superb", "good", "ok", "poor", "A gift box with nice decorations.", new short[] { 53, 48, 108, 52, 21, 83 }, (short)243, (short)1, 0, 9072000L, 10, 10, 31, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.giftbox.", 10.0F, 23, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2501 */     createItemTemplate(176, "ebony wand", "ebony wands", "superb", "good", "ok", "poor", "A long slender wand made from black polished ebony.", new short[] { 108, 40, 42, 48, 53, 64, 19, 10, 125 }, (short)946, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.deity.", 10.0F, 5, (byte)0, 10000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2508 */     createItemTemplate(301, "cornucopia", "cornucopias", "superb", "good", "ok", "poor", "The Horn of Plenty! The Cornucopia! A huge goat horn filled with fruits and flowers, able to give to the person in possession of it whatever he or she wishes for!", new short[] { 108, 40, 42, 53 }, (short)496, (short)30, 0, Long.MAX_VALUE, 1, 5, 11, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.cornucopia.", 10.0F, 5, (byte)35, 10000000, false);
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
/* 2522 */     createItemTemplate(315, "ivory wand", "ivory wands", "superb", "good", "ok", "poor", "A long slender wand made from white shiny ivory.", new short[] { 108, 40, 42, 48, 53, 64, 10 }, (short)400, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.gm.", 10.0F, 5, (byte)0, 10000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2529 */     createItemTemplate(526, "granite wand", "granite wands", "superb", "good", "ok", "poor", "A grey thick wand from beautifully polished granite.", new short[] { 108, 40, 42, 53, 25 }, (short)400, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.nature.", 10.0F, 5, (byte)15, 50000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2536 */     createItemTemplate(633, "brittle wand", "brittle wands", "superb", "good", "ok", "poor", "A thin wand made from brittle wood.", new short[] { 108, 42, 53, 21 }, (short)400, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.brittle.", 10.0F, 5, (byte)39, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2543 */     createItemTemplate(602, "sculpting wand", "sculpting wands", "superb", "good", "ok", "poor", "A slender wooden twig that has been grown in a spiral around something. It may probably be recharged somewhere.", new short[] { 108, 40, 42, 53, 21, 130 }, (short)400, (short)1, 0, Long.MAX_VALUE, 1, 1, 11, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.wand.sculpturing.", 10.0F, 5, (byte)14, 50000, false);
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
/* 2557 */     createItemTemplate(177, "pile of items", "piles of items", "almost full", "somewhat occupied", "half-full", "emptyish", "A pile of items.", new short[] { 1, 45 }, (short)60, (short)2, 0, Long.MAX_VALUE, 4000, 4000, 4000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.pile.", 200.0F, 1000, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2563 */     createItemTemplate(179, "unfinished item", "unfinished items", "superb", "good", "ok", "poor", "An item under construction.", new short[] { 48, 135, 246 }, (short)60, (short)23, 0, 29030400L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 10.0F, 0, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2569 */     createItemTemplate(178, "oven", "stone ovens", "almost full", "somewhat occupied", "half-full", "emptyish", "An oven made from stone bricks and clay intended for cooking and baking.", new short[] { 108, 135, 1, 31, 25, 51, 86, 52, 59, 44, 147, 176, 199, 180, 209, 259 }, (short)289, (short)18, 0, 2419200L, 100, 121, 390, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.oven.", 10.0F, 70000, (byte)15, 100, false)
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
/* 2586 */       .setContainerSize(45, 45, 210);
/* 2587 */     createItemTemplate(180, "forge", "forges", "almost full", "somewhat occupied", "half-full", "emptyish", "A forge made from stone bricks and clay, intended for smelting and smithing.", new short[] { 108, 135, 1, 31, 25, 51, 86, 52, 59, 44, 147, 176, 180, 209, 199, 259 }, (short)288, (short)18, 0, 2419200L, 82, 122, 390, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fireplace.forge.", 12.0F, 70000, (byte)15, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2597 */       .setContainerSize(41, 61, 210);
/* 2598 */     createItemTemplate(186, 2, "cart", "carts", "almost full", "somewhat occupied", "half-full", "emptyish", "A small two wheel cart designed to be dragged by one person.", new short[] { 108, 135, 1, 31, 21, 51, 56, 52, 44, 92, 176, 160, 193, 117, 134, 47, 54 }, (short)60, (short)41, 0, 9072000L, 60, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.cart.", 10.0F, 60000, (byte)14, 10000, true, -1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2608 */       .setDyeAmountGrams(5000);
/* 2609 */     createItemTemplate(184, 4, "chest", "chests", "almost full", "somewhat occupied", "half-full", "emptyish", "A large chest made from planks and strengthened with iron ribbons.", new short[] { 108, 135, 1, 31, 21, 47, 51, 52, 147, 44, 92, 176 }, (short)244, (short)1, 0, 9072000L, 30, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.chest.large.", 10.0F, 20000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2619 */     createItemTemplate(187, 2, "wheel", "wheels", "almost full", "somewhat occupied", "half-full", "emptyish", "A small wheel, supposed to be fit on a shaft.", new short[] { 21, 44 }, (short)247, (short)1, 0, 9072000L, 2, 40, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.small.wheel.", 10.0F, 5000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2625 */     createItemTemplate(191, 2, "wheel axle", "wheel axles", "superb", "good", "ok", "poor", "Two small wheels fitted on a sturdy shaft.", new short[] { 21, 51, 44 }, (short)267, (short)1, 0, 9072000L, 40, 40, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.small.wheelaxle.", 5.0F, 11000, (byte)14, 100, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2631 */     createItemTemplate(289, 2, "raft", "rafts", "almost full", "somewhat occupied", "half-full", "emptyish", "A small, very rudimentary raft that can be dragged by one person in the water.", new short[] { 108, 135, 1, 21, 51, 56, 60, 52, 44, 92 }, (short)256, (short)1, 0, 9072000L, 60, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.raft.", 15.0F, 60000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2640 */     createItemTemplate(188, "ribbon", "iron ribbons", "almost full", "somewhat occupied", "half-full", "emptyish", "A sturdy iron ribbon, used to strengthen constructions like chests and barrels.", new short[] { 22, 146 }, (short)709, (short)1, 0, 3024000L, 1, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.ribbon.", 5.0F, 3000, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2646 */     createItemTemplate(190, 4, "barrel", "barrels", "almost full", "somewhat occupied", "half-full", "emptyish", "A large wooden barrel, strengthened by iron ribbons.", new short[] { 108, 135, 144, 1, 21, 86, 33, 51, 52, 44, 92, 157, 176, 199, 259 }, (short)245, (short)1, 0, 2419200L, 50, 50, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.barrel.large.", 5.0F, 5000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2656 */     createItemTemplate(576, 5, "tub", "tub", "almost full", "somewhat occupied", "half-full", "emptyish", "A wide, open wooden tub that can hold lots of liquid.", new short[] { 108, 135, 144, 1, 21, 86, 33, 51, 52, 44, 92, 176, 199, 259 }, (short)245, (short)1, 0, 2419200L, 100, 150, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.barrel.huge.", 45.0F, 60000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2666 */     createItemTemplate(189, 2, "barrel", "barrels", "almost full", "somewhat occupied", "half-full", "emptyish", "A small wooden barrel.", new short[] { 108, 135, 51, 1, 21, 33, 86, 52, 157, 44, 92, 236, 259 }, (short)245, (short)1, 0, 2419200L, 30, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.barrel.small.", 5.0F, 2000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2675 */     createItemTemplate(192, 2, "chest", "chests", "almost full", "somewhat occupied", "half-full", "emptyish", "A small chest made from planks.", new short[] { 108, 135, 1, 21, 47, 51, 52, 147, 44, 92 }, (short)244, (short)1, 0, 9072000L, 30, 30, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.chest.small.", 10.0F, 5000, (byte)14, 10000, true, -1);
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
/* 2691 */     createItemTemplate(200, "dough", "dough", "superb", "good", "ok", "poor", "Sticky dough, made from flour and water.", new short[] { 5, 146, 74 }, (short)525, (short)1, 0, 172800L, 5, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.dough.", 3.0F, 600, (byte)0, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2700 */     createItemTemplate(201, "flour", "flour", "superb", "good", "ok", "poor", "Ground cereals.", new short[] { 5, 146, 74 }, (short)642, (short)1, 0, 9072000L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.flour.", 1.0F, 300, (byte)0, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2709 */     createItemTemplate(202, "grindstone", "grindstones", "superb", "good", "ok", "poor", "This flat, circular stone that can be used to create flour from cereals.", new short[] { 25, 210, 44 }, (short)800, (short)1, 0, 12096000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.grinder.", 10.0F, 3000, (byte)15, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2715 */     createItemTemplate(296, "whetstone", "whetstones", "superb", "good", "ok", "poor", "A small finely polished stone used to sharpen the edges of weapons.", new short[] { 25, 147 }, (short)803, (short)1, 0, 28800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.whet.", 5.0F, 300, (byte)15, 40, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2722 */     createItemTemplate(203, "bread", "loaves of bread", "superb", "good", "ok", "poor", "A loaf of bread.", new short[] { 82, 74, 212, 222, 233, 146 }, (short)505, (short)1, 0, 172800L, 5, 10, 10, 10039, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bread.", 10.0F, 600, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2732 */     createItemTemplate(208, "pointing sign", "pointing signs", "fresh", "dry", "brittle", "rotten", "A wooden sign that is pointing towards something.", new short[] { 108, 147, 21, 142, 48, 51, 52, 44, 18, 86, 92, 200 }, (short)292, (short)1, 20, 9072000L, 3, 5, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.sign.pointing.", 10.0F, 3000, (byte)14, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2741 */     createItemTemplate(209, 4, "sign", "signs", "fresh", "dry", "brittle", "rotten", "A large wooden sign.", new short[] { 108, 147, 21, 142, 48, 51, 52, 44, 18, 86, 92, 200 }, (short)272, (short)1, 20, 9072000L, 2, 5, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.sign.large.", 15.0F, 5000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2749 */     createItemTemplate(210, 2, "sign", "signs", "fresh", "dry", "brittle", "rotten", "A small wooden sign.", new short[] { 108, 147, 21, 142, 48, 51, 52, 44, 18, 86, 92, 200 }, (short)252, (short)1, 20, 9072000L, 1, 5, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.sign.small.", 10.0F, 3000, (byte)14, 5000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2757 */     createItemTemplate(677, 2, "gm sign", "gm signs", "fresh", "dry", "brittle", "rotten", "GM information sign.", new short[] { 108, 147, 21, 142, 31, 48, 52, 44, 18, 86, 199, 51, 92 }, (short)252, (short)1, 20, 9072000L, 1, 5, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.sign.gmnotice.", 10.0F, 3000, (byte)14, 5000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2765 */     createItemTemplate(656, "shop sign", "shop signs", "fresh", "dry", "brittle", "rotten", "A signpost with a place for a shop sign.", new short[] { 108, 147, 21, 142, 48, 51, 52, 44, 18, 86, 200 }, (short)252, (short)1, 20, 12096000L, 2, 5, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.sign.shop.", 40.0F, 8000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2772 */     createItemTemplate(237, "size five village deed", "village deeds", "new", "fancy", "ok", "old", "A land claim for a village of the size 5.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 250000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2778 */     createItemTemplate(211, "size ten village deed", "village deeds", "new", "fancy", "ok", "old", "A land claim for a village of the size 10.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 1000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2784 */     createItemTemplate(238, "size fifteen village deed", "village deeds", "new", "fancy", "ok", "old", "A land claim for a village of the size 15.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 2250000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2790 */     createItemTemplate(239, "size twenty village deed", "village deeds", "new", "fancy", "ok", "old", "A land claim for a village of the size 20.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 4000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2797 */     createItemTemplate(242, "size fifty village deed", "village deeds", "new", "fancy", "ok", "old", "A land claim for a village of the size 50.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 20000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2804 */     createItemTemplate(244, "size hundred village deed", "village deeds", "new", "fancy", "ok", "old", "A land claim for a village of the size 100.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 40000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2810 */     createItemTemplate(245, "size twohundred village deed", "village deeds", "new", "fancy", "ok", "old", "A land claim for a village of the size 200.", new short[] { 83, 40, 42, 48, 53, 54, 57, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 55000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2817 */     createItemTemplate(234, "size five homestead deed", "size five homestead deeds", "new", "fancy", "ok", "old", "A land claim for a homestead of size 5.", new short[] { 83, 40, 42, 48, 53, 54, 58, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 50000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2823 */     createItemTemplate(253, "size ten homestead deed", "size ten homestead deeds", "new", "fancy", "ok", "old", "A land claim for a homestead of size 10.", new short[] { 83, 40, 42, 48, 53, 54, 58, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 750000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2829 */     createItemTemplate(254, "size twenty homestead deed", "size twenty homestead deeds", "new", "fancy", "ok", "old", "A land claim for a homestead of size 20.", new short[] { 83, 40, 42, 48, 53, 54, 58, 114 }, (short)340, (short)24, 0, Long.MAX_VALUE, 0, 0, 0, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.deed.", 100.0F, 0, (byte)33, 2000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2836 */     createItemTemplate(299, "trader contract", "trader contracts", "new", "fancy", "ok", "old", "A contract prepared for a public trader to settle down in an area and conduct business.", new short[] { 40, 42, 48, 53, 114 }, (short)324, (short)29, 0, Long.MAX_VALUE, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.trader.", 100.0F, 0, (byte)33, 500000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2841 */     createItemTemplate(300, "personal merchant contract", "merchant contracts", "new", "fancy", "ok", "old", "A contract declaring the rights for a person called a merchant to conduct trade on your behalf.", new short[] { 83, 40, 42, 48, 53, 114, 157 }, (short)324, (short)29, 0, Long.MAX_VALUE, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.writ.merchant.", 100.0F, 0, (byte)33, 10000 * (
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2847 */         Servers.localServer.isChallengeServer() ? 3 : 10), false);
/*      */     
/* 2849 */     createItemTemplate(236, "settlement token", "settlement tokens", "new", "fancy", "ok", "old", "A sign representing this settlement.", new short[] { 83, 60, 52, 40, 31, 123, 48, 53, 54, 49 }, (short)60, (short)25, 0, Long.MAX_VALUE, 100, 100, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.board.village.", 200000.0F, 0, (byte)15, 1000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2857 */     createItemTemplate(226, "floor loom", "floor looms", "superb", "good", "ok", "poor", "A large wooden loom, made for weaving.", new short[] { 108, 21, 31, 86, 139, 51, 52, 44, 176, 67, 199 }, (short)60, (short)1, 0, 9072000L, 150, 250, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.loom.", 10.0F, 150000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2864 */     createItemTemplate(227, "statuette", "statuettes", "superb", "good", "ok", "poor", "A finely carved statuette.", new short[] { 108, 52, 22, 44, 87, 92, 157 }, (short)282, (short)1, 0, 2419200L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.", 20.0F, 1000, (byte)0, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2870 */     createItemTemplate(505, "statuette of Fo", "statuettes", "superb", "good", "ok", "poor", "A statuette resembling the artists interpretation of the deity Fo.", new short[] { 108, 52, 22, 44, 87, 92, 147 }, (short)282, (short)1, 0, 2419200L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.fo.", 40.0F, 1000, (byte)0, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2877 */     createItemTemplate(507, "statuette of Magranon", "statuettes", "superb", "good", "ok", "poor", "A statuette resembling the artists interpretation of the deity Magranon.", new short[] { 108, 52, 22, 44, 87, 92, 147 }, (short)282, (short)1, 0, 2419200L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.magranon.", 40.0F, 1000, (byte)0, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2884 */     createItemTemplate(506, "statuette of Libila", "statuettes", "superb", "good", "ok", "poor", "A statuette resembling the artists interpretation of the deity Libila.", new short[] { 108, 52, 22, 44, 87, 92, 147 }, (short)282, (short)1, 0, 2419200L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.libila.", 40.0F, 1000, (byte)0, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2891 */     createItemTemplate(508, "statuette of Vynora", "statuettes", "superb", "good", "ok", "poor", "A statuette resembling the artists interpretation of the deity Vynora.", new short[] { 108, 52, 22, 44, 87, 92, 147 }, (short)282, (short)1, 0, 2419200L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.vynora.", 40.0F, 1000, (byte)0, 20000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2898 */     createItemTemplate(228, "candelabra", "candelabras", "superb", "good", "ok", "poor", "A beautiful candelabra. The number of arms on a candelabra varies depending on a number of factors, but mainly by the preferences of the designer. Apparently even fairly intelligent people sometimes fail to recognize the proper number which causes mild confusion.", new short[] { 108, 147, 52, 22, 44, 87, 32, 51, 199 }, (short)251, (short)1, 0, 2419200L, 5, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.candelabra.", 20.0F, 2500, (byte)0, 10000, true);
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
/* 2912 */     createItemTemplate(229, "chain", "chains", "new", "fancy", "ok", "old", "A fine chain with small links.", new short[] { 108, 147, 22, 44, 87 }, (short)520, (short)1, 0, 2419200L, 1, 1, 2, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.chain.", 35.0F, 100, (byte)0, 5000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2918 */     createItemTemplate(230, "necklace", "necklaces", "new", "fancy", "ok", "old", "A delicate necklace.", new short[] { 108, 147, 22, 44, 87, 153 }, (short)268, (short)1, 0, 2419200L, 1, 1, 2, -10, new byte[] { 29, 36 }, "model.decoration.necklace.", 40.0F, 100, (byte)0, 7000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2926 */     createItemTemplate(231, "bracelet", "bracelets", "new", "fancy", "ok", "old", "A slender bracelet.", new short[] { 108, 147, 22, 44, 87, 153, 258 }, (short)250, (short)1, 0, 2419200L, 1, 5, 5, -10, new byte[] { 3, 4 }, "model.decoration.armring.", 10.0F, 300, (byte)0, 2000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2933 */     createItemTemplate(232, "ball", "balls", "new", "fancy", "ok", "old", "Your face is reflected in this small well-polished ball.", new short[] { 22, 147, 44, 87 }, (short)813, (short)1, 0, 2419200L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.ball.", 30.0F, 300, (byte)0, 5000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2939 */     createItemTemplate(233, "pendulum", "pendulums", "new", "fancy", "ok", "old", "A pendulum with a fine chain and a well-polished ball.", new short[] { 108, 52, 147, 22, 44, 87 }, (short)801, (short)1, 0, 2419200L, 3, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.pendulum.", 10.0F, 400, (byte)0, 4000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2945 */     createItemTemplate(297, "ring", "rings", "new", "fancy", "ok", "old", "A ring to put on your finger.", new short[] { 108, 147, 22, 44, 87, 153 }, (short)249, (short)1, 0, 2419200L, 1, 2, 2, -10, new byte[] { 40, 39 }, "model.decoration.ring.", 40.0F, 50, (byte)0, 5000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2953 */     createItemTemplate(326, "bowl", "bowls", "new", "fancy", "ok", "old", "A one foot wide shiny metal bowl, hammered thin.", new short[] { 108, 147, 22, 44, 1, 87 }, (short)764, (short)1, 0, 2419200L, 1, 5, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.bowl.", 10.0F, 300, (byte)0, 2000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2960 */     createItemTemplate(247, "black mushroom", "black mushrooms", "excellent", "good", "ok", "poor", "A black, oily mushroom with a pointy hat and a fat, dark foot.", new short[] { 106, 147, 5, 146, 55, 212, 226 }, (short)586, (short)1, 0, 86400L, 3, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.black.", 200.0F, 400, (byte)22, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2969 */       .setNutritionValues(380, 70, 5, 15)
/* 2970 */       .setFoodGroup(1199);
/*      */     
/* 2972 */     createItemTemplate(246, "green mushroom", "green mushrooms", "excellent", "good", "ok", "poor", "A green, low, slimy mushroom with a wide hat covered with brown specks.", new short[] { 105, 147, 5, 146, 55, 212, 226 }, (short)506, (short)1, 0, 86400L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.green.", 200.0F, 400, (byte)22, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2979 */       .setNutritionValues(380, 70, 15, 15)
/* 2980 */       .setFoodGroup(1199);
/*      */     
/* 2982 */     createItemTemplate(249, "yellow mushroom", "yellow mushrooms", "excellent", "good", "ok", "poor", "A yellow agaric, formed like a small trumpet. It has a nutty scent.", new short[] { 104, 147, 5, 146, 55, 212, 226 }, (short)526, (short)1, 0, 86400L, 3, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.yellow.", 200.0F, 200, (byte)22, 5, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2991 */       .setNutritionValues(380, 30, 5, 15)
/* 2992 */       .setFoodGroup(1199);
/*      */     
/* 2994 */     createItemTemplate(250, "blue mushroom", "blue mushrooms", "excellent", "good", "ok", "poor", "A weird mushroom with a blue shining thick and broad hat and a sturdy white foot.", new short[] { 104, 147, 5, 146, 55, 212, 226 }, (short)566, (short)1, 0, 86400L, 3, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.blue.", 200.0F, 800, (byte)22, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3003 */       .setNutritionValues(380, 70, 5, 25)
/* 3004 */       .setFoodGroup(1199);
/*      */     
/* 3006 */     createItemTemplate(248, "brown mushroom", "brown mushrooms", "excellent", "good", "ok", "poor", "A fine mushroom with a fine dry, round and brown hat and a white foot.", new short[] { 103, 147, 5, 146, 55, 212, 226 }, (short)546, (short)1, 0, 86400L, 3, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.brown.", 200.0F, 600, (byte)22, 10, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3015 */       .setNutritionValues(390, 70, 5, 15)
/* 3016 */       .setFoodGroup(1199);
/*      */     
/* 3018 */     createItemTemplate(251, "red mushroom", "red mushrooms", "excellent", "good", "ok", "poor", "A strange agaric with a red leathery hat and a thin white foot.", new short[] { 5, 146, 147, 79, 55, 212, 226 }, (short)486, (short)1, 0, 86400L, 3, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.red.", 200.0F, 100, (byte)22, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3027 */       .setNutritionValues(380, 45, 15, 25)
/* 3028 */       .setFoodGroup(1199);
/*      */     
/* 3030 */     createItemTemplate(257, "spoon", "spoons", "excellent", "good", "ok", "poor", "A large spoon.", new short[] { 108, 22, 147, 44, 210, 87 }, (short)768, (short)1, 0, 3024000L, 1, 2, 15, 1018, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.food.spoon.", 10.0F, 30, (byte)0, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3037 */     createItemTemplate(259, "fork", "forks", "excellent", "good", "ok", "poor", "A large fork.", new short[] { 108, 22, 147, 44, 210, 87 }, (short)767, (short)1, 0, 3024000L, 1, 2, 15, 1018, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.food.fork.", 10.0F, 30, (byte)0, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3044 */     createItemTemplate(258, "knife", "knives", "excellent", "good", "ok", "poor", "A large cutlery knife.", new short[] { 108, 22, 147, 44, 210, 87 }, (short)940, (short)1, 0, 3024000L, 1, 2, 15, 1018, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.food.knife.", 10.0F, 30, (byte)0, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3052 */     createItemTemplate(262, 2, "square table", "square tables", "superb", "good", "ok", "poor", "A small square table.", new short[] { 108, 21, 135, 86, 31, 51, 52, 44, 157, 92, 176, 199, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 10, 60, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.table.square.small.", 15.0F, 10000, (byte)14, 10000, true, -1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3062 */       .setContainerSize(15, 60, 60);
/* 3063 */     createItemTemplate(260, "round table", "round tables", "superb", "good", "ok", "poor", "A small round table.", new short[] { 108, 21, 135, 86, 31, 51, 52, 44, 157, 92, 176, 199, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 10, 60, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.table.round.", 20.0F, 10000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3072 */       .setContainerSize(40, 150, 150);
/* 3073 */     createItemTemplate(264, 4, "dining table", "dining tables", "superb", "good", "ok", "poor", "A large square dining table.", new short[] { 108, 21, 135, 86, 31, 51, 52, 157, 199, 44, 92, 176, 1, 180, 256 }, (short)60, (short)1, 0, 9072000L, 10, 60, 250, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.table.square.large.", 30.0F, 18000, (byte)14, 10000, true, -1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3083 */       .setContainerSize(40, 100, 220);
/* 3084 */     createItemTemplate(442, 4, "delicious julbord", "julbords", "superb", "good", "ok", "poor", "A wonderful julbord with ham and spare ribs, assorted cheese, mustard, meatballs, pickled fish, ale, schnaps and julmust, egg, potatoes, chocolate candy, marzipan pigs, and more and more.", new short[] { 21, 31, 52, 157 }, (short)60, (short)1, 0, Long.MAX_VALUE, 10, 60, 250, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.table.square.large.julbord.", 30.0F, 18000, (byte)14, 10000, true, -1);
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
/* 3100 */     createItemTemplate(261, "stool", "stools", "superb", "good", "ok", "poor", "A small round chair without a back to lean against.", new short[] { 108, 21, 135, 86, 51, 52, 44, 92, 117, 197, 199, 48 }, (short)274, (short)41, 0, 9072000L, 10, 30, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.stool.round.", 10.0F, 4000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3108 */     createItemTemplate(263, "chair", "chairs", "superb", "good", "ok", "poor", "A small chair with a comfortable back to lean against.", new short[] { 108, 21, 135, 86, 51, 52, 44, 92, 117, 197, 199, 48 }, (short)274, (short)41, 0, 9072000L, 10, 30, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.chair.", 20.0F, 5000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3116 */     createItemTemplate(265, "armchair", "armchairs", "superb", "good", "ok", "poor", "A comfortable armchair with a seat padded with cloth.", new short[] { 108, 21, 135, 86, 51, 52, 44, 92, 117, 197, 199, 48, 249 }, (short)274, (short)41, 0, 9072000L, 10, 30, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.chair.armchair.", 30.0F, 7000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3125 */       .setSecondryItem("seat");
/* 3126 */     createItemTemplate(484, "bed", "beds", "superb", "good", "ok", "poor", "A cosy bed with furs and sheets.", new short[] { 109, 108, 21, 51, 52, 44, 86, 92, 31, 67, 135, 48, 110, 111, 176, 178, 157, 249, 1, 180, 256 }, (short)313, (short)1, 0, 9072000L, 60, 60, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.bed.standard.", 20.0F, 40000, (byte)14, 10000, true)
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
/* 3137 */       .setSecondryItem("covers")
/* 3138 */       .setContainerSize(40, 60, 200);
/* 3139 */     createItemTemplate(482, "head board", "head boards", "superb", "good", "ok", "poor", "The head part of a bed, consisting of two feet joined by planks.", new short[] { 21, 51, 44 }, (short)253, (short)1, 0, 9072000L, 5, 60, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.headboard.standard.", 10.0F, 8000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3145 */     createItemTemplate(485, "foot board", "foot boards", "superb", "good", "ok", "poor", "The foot part of a bed, consisting of two feet joined by planks.", new short[] { 21, 51, 44 }, (short)273, (short)1, 0, 9072000L, 5, 60, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.footboard.standard.", 7.0F, 6000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3151 */     createItemTemplate(483, "bed frame", "bed frames", "superb", "good", "ok", "poor", "The sleeping part of a bed, lacking the feet that keeps you above the moisture and the rats.", new short[] { 21, 51, 52, 44, 92 }, (short)293, (short)1, 0, 9072000L, 10, 60, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.bedframe.standard.", 15.0F, 20000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3159 */     createItemTemplate(266, "sprout", "sprouts", "superb", "good", "ok", "poor", "A delicate tree sprout with a few tender leaves.", new short[] { 21, 48, 147, 146 }, (short)484, (short)1, 0, 86400L, 1, 3, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 200.0F, 200, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3165 */     createItemTemplate(267, "sickle", "sickles", "superb", "good", "ok", "poor", "A kind of small hand-held scythe with a crescent-moon formed blade.", new short[] { 108, 44, 147, 38, 22, 2, 37 }, (short)752, (short)1, 30, 9072000L, 2, 20, 30, 10046, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.sickle.", 25.0F, 500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3172 */     createItemTemplate(268, "scythe", "scythes", "superb", "good", "ok", "poor", "A long pole with a half meter long sharp blade, used for cutting grass or harvesting crops.", new short[] { 108, 44, 147, 38, 22, 2, 37, 84 }, (short)753, (short)1, 40, 9072000L, 2, 40, 120, 10047, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.scythe.", 22.0F, 1200, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3180 */     createItemTemplate(269, "sickle blade", "sickle blades", "excellent", "good", "ok", "poor", "A thin, crescent moon formed blade for a sickle.", new short[] { 22, 44 }, (short)732, (short)1, 0, 3024000L, 1, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.sickle.", 20.0F, 400, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3186 */     createItemTemplate(270, "scythe blade", "scythe blades", "excellent", "good", "ok", "poor", "A thin long blade for a scythe.", new short[] { 22, 44 }, (short)733, (short)1, 0, 3024000L, 1, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.blade.scythe.", 10.0F, 600, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3192 */     createItemTemplate(271, "yoyo", "yoyos", "excellent", "good", "ok", "poor", "A small spool of wood with a string rolled up in the centre.", new short[] { 108, 21, 147 }, (short)761, (short)26, 0, 9072000L, 3, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.toy.yoyo.", 15.0F, 100, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3198 */     createItemTemplate(272, "corpse", "corpses", "excellent", "good", "ok", "poor", "A dead body.", new short[] { 1, 60, 28, 48, 255, 52, 54, 63, 112, 211, 237 }, (short)40, (short)28, 0, 14400L, 20, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.corpse.", 200.0F, 50000, (byte)2, 0, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3205 */     createItemTemplate(290, 4, "maul", "mauls", "excellent", "good", "ok", "poor", "A huge spiked heavy clump of metal on a shaft. You'll need both hands free to wield this one.", new short[] { 108, 44, 147, 22, 37, 14, 84, 189 }, (short)1233, (short)1, 70, 3024000L, 3, 10, 80, 10061, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.maul.large.", 30.0F, 5000, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3213 */     createItemTemplate(292, "maul", "mauls", "excellent", "good", "ok", "poor", "A large spiked heavy clump of metal on a shaft.", new short[] { 108, 44, 147, 22, 37, 14, 189 }, (short)1213, (short)1, 60, 3024000L, 1, 10, 70, 10062, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.maul.medium.", 17.0F, 4000, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3220 */     createItemTemplate(291, 2, "maul", "small mauls", "excellent", "good", "ok", "poor", "A smooth heavy clump of metal on a shaft.", new short[] { 108, 44, 147, 22, 37, 14 }, (short)1213, (short)1, 30, 3024000L, 1, 7, 70, 10063, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.maul.small.", 11.0F, 3000, (byte)11, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3227 */     createItemTemplate(293, 4, "maul head", "large maul heads", "excellent", "good", "ok", "poor", "The thick, heavy, spiked metal head for a maul.", new short[] { 44, 22 }, (short)1232, (short)1, 0, 3024000L, 20, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.head.large.maul.", 15.0F, 4000, (byte)11, 100, false, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3233 */     createItemTemplate(295, "maul head", "maul heads", "excellent", "good", "ok", "poor", "The heavy spiked metal head for a maul.", new short[] { 44, 22 }, (short)1212, (short)1, 0, 3024000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.head.medium.maul.", 10.0F, 3000, (byte)11, 40, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3239 */     createItemTemplate(294, 2, "maul head", "small maul heads", "excellent", "good", "ok", "poor", "The heavy metal head for a maul.", new short[] { 44, 22 }, (short)1212, (short)1, 0, 3024000L, 7, 7, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.head.small.maul.", 5.0F, 2000, (byte)11, 20, false, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3245 */     createItemTemplate(298, "heap of sand", "heaps of sand", "excellent", "good", "ok", "poor", "A lot of sand, enough to raise the ground somewhere.", new short[] { 25, 146, 147, 112, 113, 46, 129, 174 }, (short)593, (short)1, 0, 604800L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sand.", 200.0F, 20000, (byte)15, 5, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3252 */     createItemTemplate(567, "belaying pin", "pins", "fresh", "dry", "brittle", "rotten", "A wooden shaft with a rounded part used both to tie ropes to as well as a weapon for drunk sailors.", new short[] { 21, 14, 147, 37, 146 }, (short)646, (short)1, 35, 9072000L, 3, 5, 20, 1025, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.belaying.", 5.0F, 500, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3261 */     createItemTemplate(314, 5, "shod club", "shod clubs", "excellent", "good", "ok", "poor", "A huge metal shod gnarly branch, worn and strengthened by weather.", new short[] { 108, 21, 37, 14, 84 }, (short)1239, (short)1, 50, 86401L, 1, 20, 70, 10064, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.club.huge.", 20.0F, 15000, (byte)14, 2, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3268 */     createItemTemplate(321, "practice doll", "practice dolls", "superb", "good", "ok", "poor", "An exquisite mansized practice doll made from shafts, a pair of planks and a pumpkin.", new short[] { 21, 51, 52, 44 }, (short)881, (short)31, 0, 9072000L, 10, 30, 180, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.practicedoll.", 10.0F, 7000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3275 */     createItemTemplate(323, "altar", "altars", "almost full", "somewhat occupied", "half-full", "emptyish", "A stone altar, with decorations depicting famous religious events.", new short[] { 108, 1, 31, 25, 51, 52, 44, 48, 66, 67, 135, 259 }, (short)60, (short)33, 0, 12096000L, 41, 41, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.altar.", 12.0F, 200000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3284 */     createItemTemplate(533, 3, "circlet", "circlets", "excellent", "good", "ok", "poor", "A simple yet stunningly fashionable silver circlet.", new short[] { 52, 40, 42, 22, 122, 70, 4 }, (short)60, (short)1, 0, Long.MAX_VALUE, 5, 20, 20, -10, new byte[] { 1, 28 }, "model.decoration.crown." + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3290 */         Kingdoms.getSuffixFor((byte)2), 99.0F, 1500, (byte)8, 5000000, false);
/*      */     
/* 3292 */     createItemTemplate(534, 3, "chancellor cape", "capes", "excellent", "good", "ok", "poor", "The cape of the chancellor is thick, with an impressive collar higher than the head.", new short[] { 40, 24, 4, 92, 42, 122, 70 }, (short)60, (short)1, 0, Long.MAX_VALUE, 3, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.dragon.scale.leather.", 30.0F, 1500, (byte)17, 1000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3300 */     createItemTemplate(532, "staff of the office", "staves", "excellent", "good", "ok", "poor", "A long slender birch staff, encircled by silver strands and with a huge star ruby at the top.", new short[] { 52, 40, 42, 122, 21, 70 }, (short)60, (short)1, 80, Long.MAX_VALUE, 5, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.sceptre." + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3306 */         Kingdoms.getSuffixFor((byte)2), 99.0F, 3000, (byte)14, 5000000, false);
/*      */ 
/*      */     
/* 3309 */     createItemTemplate(536, 3, "cage crown", "crowns", "excellent", "good", "ok", "poor", "A crown in the form of an egg made from steel bands that cage the head of the wearer.", new short[] { 52, 40, 42, 122, 22, 70, 4 }, (short)60, (short)1, 0, Long.MAX_VALUE, 5, 20, 20, -10, new byte[] { 1, 28 }, "model.decoration.crown." + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3315 */         Kingdoms.getSuffixFor((byte)3), 99.0F, 1500, (byte)9, 5000000, false);
/*      */     
/* 3317 */     createItemTemplate(537, 3, "thorn robes", "robes", "excellent", "good", "ok", "poor", "Rather uncomfortable but menacing robes made from mycelium infested thorns, barbs pointing outwards.", new short[] { 40, 24, 4, 92, 42, 122, 70 }, (short)60, (short)1, 0, Long.MAX_VALUE, 3, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.dragon.scale.leather.", 30.0F, 1500, (byte)17, 1000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3326 */     createItemTemplate(535, "black claw", "claws", "excellent", "good", "ok", "poor", "The dried, blackened lower part of the leg and claw from some huge bird. Sometimes it clenches faintly, or is it normal vibrations?", new short[] { 52, 40, 42, 122, 28, 70 }, (short)60, (short)1, 80, Long.MAX_VALUE, 5, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.sceptre." + 
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
/* 3339 */         Kingdoms.getSuffixFor((byte)3), 99.0F, 3000, (byte)2, 5000000, false);
/*      */     
/* 3341 */     createItemTemplate(331, "Charm of Fo", "charms of fo", "excellent", "good", "ok", "poor", "A ball made of twigs with a green gem inside.", new short[] { 52, 48, 69, 40 }, (short)859, (short)35, 0, Long.MAX_VALUE, 2, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.charm.", 99.0F, 600, (byte)7, 2000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3347 */     createItemTemplate(332, "Eye of Vynora", "eyes of vynora", "excellent", "good", "ok", "poor", "A huge blue gem with a shimmering light inside.", new short[] { 52, 48, 69, 40 }, (short)919, (short)35, 0, Long.MAX_VALUE, 3, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.eye.", 99.0F, 500, (byte)52, 5000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3354 */     createItemTemplate(333, "Ear of Vynora", "ears of vynora", "excellent", "good", "ok", "poor", "A large seashell made from blue crystal.", new short[] { 52, 48, 69, 40 }, (short)879, (short)35, 0, Long.MAX_VALUE, 5, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.ear.", 99.0F, 500, (byte)52, 5000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3361 */     createItemTemplate(334, "Mouth of Vynora", "mouths of vynora", "excellent", "good", "ok", "poor", "A slender, blue twisted crystal horn.", new short[] { 52, 48, 69, 40 }, (short)899, (short)35, 0, Long.MAX_VALUE, 5, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.mouth.", 99.0F, 1000, (byte)52, 5000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3368 */     createItemTemplate(335, "Finger of Fo", "fingers of fo", "excellent", "good", "ok", "poor", "A very natural one foot long slender twig, except that it seems made from silver.", new short[] { 52, 48, 69, 40 }, (short)1299, (short)35, 0, Long.MAX_VALUE, 3, 3, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.finger.", 99.0F, 400, (byte)8, 5000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3375 */     createItemTemplate(336, "Sword of Magranon", "swords of magranon", "excellent", "good", "ok", "poor", "A heavy two-handed sword of incredible quality made from steel and with a comfortable hilt of leather bound silver filigran. ", new short[] { 52, 48, 69, 37, 2, 40, 71, 84, 16 }, (short)1319, (short)35, 70, Long.MAX_VALUE, 5, 10, 180, 10028, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.swordtwo.", 99.0F, 5000, (byte)9, 3000000, false);
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
/* 3390 */     createItemTemplate(337, "Hammer of Magranon", "hammers of magranon", "excellent", "good", "ok", "poor", "A huge brutal warhammer made totally from bronze.", new short[] { 52, 48, 69, 37, 14, 40, 71 }, (short)1339, (short)35, 80, Long.MAX_VALUE, 5, 10, 80, 10070, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.hammerhuge.", 99.0F, 7000, (byte)31, 3000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3398 */     createItemTemplate(338, "Scale of Libila", "scales of libila", "excellent", "good", "ok", "poor", "This looks somewhat like a huge black leaf, except that it is made from dark shiny iron.", new short[] { 52, 48, 69, 40, 3 }, (short)839, (short)35, 0, Long.MAX_VALUE, 1, 25, 45, 10006, new byte[] { 3, 44 }, "model.artifact.scale.", 99.0F, 1200, (byte)11, 3000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3406 */     createItemTemplate(339, "Orb of Doom", "orbs of doom", "excellent", "good", "ok", "poor", "A round crystal ball the size of your hand, with a treacherous red glow dancing amidst dark vapors inside.", new short[] { 52, 48, 69, 40, 59 }, (short)819, (short)35, 0, Long.MAX_VALUE, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.orbdoom.", 99.0F, 2000, (byte)20, 5000000, false);
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
/* 3420 */     createItemTemplate(340, "Sceptre of Ascension", "sceptres of ascension", "excellent", "good", "ok", "poor", "A large bulky sceptre, made from shiny white steel with black steel thorns attached to the head.", new short[] { 52, 48, 69, 37, 14, 40, 71 }, (short)1279, (short)35, 80, Long.MAX_VALUE, 5, 5, 60, 10061, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.sceptreascension.", 99.0F, 5000, (byte)9, 5000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3429 */     createItemTemplate(322, "altar", "altars", "almost full", "somewhat occupied", "half-full", "emptyish", "A wooden altar, covered in cloth and with carvings depicting famous religious events.", new short[] { 108, 1, 31, 21, 51, 52, 44, 48, 66, 67, 135, 259 }, (short)60, (short)33, 0, 9072000L, 41, 41, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.altar.", 12.0F, 150000, (byte)14, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3437 */     createItemTemplate(325, "altar", "altars", "almost full", "somewhat occupied", "half-full", "emptyish", "A silver altar, with mystical carvings and inscriptions.", new short[] { 108, 1, 31, 22, 51, 52, 44, 48, 66, 67, 135, 259 }, (short)60, (short)33, 0, 12096000L, 41, 41, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.altar.silver.", 52.0F, 5000, (byte)8, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3445 */     createItemTemplate(324, "altar", "altars", "almost full", "somewhat occupied", "half-full", "emptyish", "A golden altar, with mystical carvings and inscriptions.", new short[] { 108, 1, 44, 31, 22, 51, 52, 48, 66, 67, 135, 259 }, (short)60, (short)33, 0, 12096000L, 41, 41, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.altar.gold.", 62.0F, 5000, (byte)7, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3453 */     createItemTemplate(327, "Altar of Three", "altars", "almost full", "somewhat occupied", "half-full", "emptyish", "A huge shiny altar, built from white marble and polished granite laden with gold and silver. You notice some finely carved inscriptions on the surface of the base.", new short[] { 49, 1, 31, 52, 48, 68, 66, 70, 67, 59 }, (short)60, (short)34, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.altar.holy.", 99.0F, 2000000, (byte)0, 10000, false);
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
/* 3468 */     createItemTemplate(328, "Huge bone altar", "altars", "almost full", "somewhat occupied", "half-full", "emptyish", "A huge dark altar built from charred bones and skulls held together by bronze wires. The altar is covered in rotting meat, but you manage to notice some weird inscriptions on a stone slab tainted by dried blood.", new short[] { 49, 1, 31, 52, 44, 68, 48, 66, 70, 67, 59, 85 }, (short)60, (short)34, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.altar.unholy.", 99.0F, 2000000, (byte)0, 10000, false);
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
/* 3484 */     createItemTemplate(329, "Rod of Beguiling", "rods of beguiling", "excellent", "good", "ok", "poor", "A three foot long golden rod with a head covered with red gems.", new short[] { 52, 48, 69, 40 }, (short)1259, (short)35, 0, Long.MAX_VALUE, 5, 10, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.artifact.rodbeguiling.", 99.0F, 2500, (byte)7, 2000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3491 */     createItemTemplate(330, 3, "Crown of Might", "crowns of might", "excellent", "good", "ok", "poor", "A thick heavy gold crown covered in green and red gems.", new short[] { 52, 48, 69, 40, 4 }, (short)974, (short)35, 0, Long.MAX_VALUE, 5, 20, 20, -10, new byte[] { 1, 28 }, "model.artifact.crownmight.", 99.0F, 1500, (byte)7, 5000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3500 */     createItemTemplate(530, 3, "royal crown", "crowns", "excellent", "good", "ok", "poor", "A heavy gold crown covered in blue and green gems.", new short[] { 52, 40, 42, 122, 70, 4 }, (short)60, (short)1, 0, Long.MAX_VALUE, 5, 20, 20, -10, new byte[] { 1, 28 }, "model.decoration.crown." + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3506 */         Kingdoms.getSuffixFor((byte)1), 99.0F, 1500, (byte)7, 5000000, false);
/*      */     
/* 3508 */     createItemTemplate(531, 3, "royal robes", "robes", "excellent", "good", "ok", "poor", "Royal robes in red, trimmed with white soble.", new short[] { 40, 24, 4, 92, 42, 122, 70 }, (short)60, (short)1, 0, Long.MAX_VALUE, 3, 40, 40, -10, new byte[] { 2 }, "model.armour.torso.dragon.scale.leather.", 30.0F, 1500, (byte)17, 1000000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3516 */     createItemTemplate(529, "royal sceptre", "sceptres", "excellent", "good", "ok", "poor", "A slender sceptre, made from gold and white silver filigran with various small gems and a large blue sapphire at the head.", new short[] { 52, 40, 42, 122, 70 }, (short)60, (short)1, 80, Long.MAX_VALUE, 5, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.sceptre." + 
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
/* 3528 */         Kingdoms.getSuffixFor((byte)1), 99.0F, 3000, (byte)7, 5000000, false);
/*      */ 
/*      */     
/* 3531 */     createItemTemplate(370, "Crazy diamond", "crazy diamonds", "excellent", "good", "ok", "poor", "A weirldy cut diamond the size of a fist that reflects your image in its many facets. It shines brightly.", new short[] { 52, 40, 93, 48 }, (short)542, (short)1, 0, Long.MAX_VALUE, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.diamond.crazy.", 99.0F, 500, (byte)54, 2000000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3539 */     createItemTemplate(341, "key copy", "key copies", "superb", "good", "ok", "poor", "A metal key with a few large teeth. It has some weird dents.", new short[] { 41, 114 }, (short)809, (short)1, 0, 3024000L, 1, 1, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.key.", 30.0F, 50, (byte)10, 0, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3545 */     createItemTemplate(463, "lock picks", "lockpicks", "superb", "good", "ok", "poor", "A few small and fragile lock picks with various grooves and notches.", new short[] { 108, 22, 147 }, (short)769, (short)1, 0, 3024000L, 2, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.key.lockpicks.", 40.0F, 50, (byte)11, 0, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3551 */     createItemTemplate(342, "key mould", "moulds", "superb", "good", "ok", "poor", "A grey clay mould, made after a key.", new short[] { 48, 196, 72 }, (short)493, (short)1, 0, 86400L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.mold.clay.", 3.0F, 400, (byte)18);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3556 */     createItemTemplate(343, "key form", "forms", "superb", "good", "ok", "poor", "A red pottery form, made after a key.", new short[] { 48, 72, 73 }, (short)513, (short)1, 0, 12096000L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.mold.pottery.", 3.0F, 400, (byte)19);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3561 */     createItemTemplate(167, "door lock", "door locks", "superb", "good", "ok", "poor", "A metal lock, ready to fit in a door.", new short[] { 108, 40, 147, 39, 114 }, (short)790, (short)1, 0, Long.MAX_VALUE, 3, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.lock.door.", 10.0F, 600, (byte)11, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3566 */     createItemTemplate(194, "large padlock", "large padlocks", "superb", "good", "ok", "poor", "A large metal padlock.", new short[] { 108, 40, 39, 114 }, (short)790, (short)1, 0, Long.MAX_VALUE, 3, 6, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.lock.large.padlock.", 15.0F, 400, (byte)11, 5000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3571 */     createItemTemplate(193, "small padlock", "small padlocks", "superb", "good", "ok", "poor", "A small metal padlock.", new short[] { 108, 40, 39, 114 }, (short)790, (short)1, 0, Long.MAX_VALUE, 3, 6, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.lock.small.padlock.", 15.0F, 200, (byte)11, 3000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3576 */     createItemTemplate(252, "gate lock", "gate locks", "superb", "good", "ok", "poor", "A large heavy metal lock, ready to fit in a gate.", new short[] { 108, 40, 39, 114 }, (short)790, (short)1, 0, Long.MAX_VALUE, 3, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.lock.gate.", 10.0F, 800, (byte)11, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3581 */     createItemTemplate(568, "boat lock", "boat locks", "superb", "good", "ok", "poor", "An ornate and intricate metal lock used to lock a boat.", new short[] { 108, 40, 39, 114 }, (short)790, (short)1, 0, Long.MAX_VALUE, 3, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.lock.boat.", 40.0F, 800, (byte)11, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3588 */     createItemTemplate(363, "rosemary", "rosemary", "excellent", "good", "ok", "poor", "A herb with twig-like stems and small white flowers, as well as a very nice scent.", new short[] { 146, 105, 5, 78, 55, 212, 206, 221 }, (short)706, (short)1, 0, 9072000L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.rosemary.", 100.0F, 50, (byte)22, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3595 */       .setNutritionValues(1310, 210, 60, 33)
/* 3596 */       .setFoodGroup(1158);
/*      */     
/* 3598 */     createItemTemplate(365, "nettles", "nettles", "excellent", "good", "ok", "poor", "A long plant with soft, silky leaves that give skin rashes.", new short[] { 146, 104, 5, 78, 55, 212 }, (short)527, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.nettles.", 100.0F, 50, (byte)22, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3605 */       .setNutritionValues(420, 70, 1, 27)
/* 3606 */       .setFoodGroup(1158);
/*      */     
/* 3608 */     createItemTemplate(440, "woad", "woad", "excellent", "good", "ok", "poor", "A yellow-flowered, no-joke, low growing plant with slender green leaves.", new short[] { 146, 164 }, (short)700, (short)1, 0, 9072000L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.woad.", 100.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3615 */     createItemTemplate(498, "bouquet of yellow flowers", "yellow flowers", "excellent", "good", "ok", "poor", "Some yellow prickly flowers.", new short[] { 118, 146, 211 }, (short)681, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower1.", 100.0F, 50, (byte)22)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3623 */       .setNutritionValues(450, 90, 7, 27)
/* 3624 */       .setFoodGroup(1267);
/*      */     
/* 3626 */     createItemTemplate(499, "bouquet of orange-red flowers", "orange-red flowers", "excellent", "good", "ok", "poor", "Some long-stemmed orange-red flowers with thick, pointy leaves.", new short[] { 118, 146, 211 }, (short)681, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower2.", 100.0F, 50, (byte)22)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3634 */       .setNutritionValues(450, 90, 7, 27)
/* 3635 */       .setFoodGroup(1267);
/*      */     
/* 3637 */     createItemTemplate(500, "bouquet of purple flowers", "purple flowers", "excellent", "good", "ok", "poor", "Some purple fluffy flowers.", new short[] { 118, 146, 211 }, (short)681, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower3.", 100.0F, 50, (byte)22)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3645 */       .setNutritionValues(450, 90, 7, 27)
/* 3646 */       .setFoodGroup(1267);
/*      */     
/* 3648 */     createItemTemplate(501, "bouquet of white flowers", "white flowers", "excellent", "good", "ok", "poor", "Some white flowers with a thick stem and long leaves.", new short[] { 118, 146, 211 }, (short)681, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower4.", 100.0F, 50, (byte)22)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3656 */       .setNutritionValues(450, 90, 7, 27)
/* 3657 */       .setFoodGroup(1267);
/*      */     
/* 3659 */     createItemTemplate(502, "bouquet of blue flowers", "blue flowers", "excellent", "good", "ok", "poor", "Some crooked but beautiful blue flowers.", new short[] { 118, 146, 211 }, (short)681, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower5.", 100.0F, 50, (byte)22)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3667 */       .setNutritionValues(450, 90, 7, 27)
/* 3668 */       .setFoodGroup(1267);
/*      */     
/* 3670 */     createItemTemplate(503, "bouquet of greenish-yellow flowers", "greenish-yellow flowers", "excellent", "good", "ok", "poor", "Some greenish-yellow furry flowers.", new short[] { 118, 146, 211 }, (short)681, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower6.", 100.0F, 50, (byte)22)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3678 */       .setNutritionValues(450, 90, 7, 27)
/* 3679 */       .setFoodGroup(1267);
/*      */     
/* 3681 */     createItemTemplate(504, "bouquet of white-dotted flowers", "dotted flowers", "excellent", "good", "ok", "poor", "Some uncommon white-dotted flowers.", new short[] { 118, 146, 211, 157 }, (short)681, (short)1, 0, 86400L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower7.", 100.0F, 50, (byte)22)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3689 */       .setNutritionValues(450, 90, 7, 27)
/* 3690 */       .setFoodGroup(1267);
/*      */     
/* 3692 */     createItemTemplate(364, "blueberry", "blueberries", "excellent", "good", "ok", "poor", "A handful of small, blue berries with a wonderful sweet taste.", new short[] { 5, 146, 80, 46, 55, 212 }, (short)529, (short)1, 0, 86400L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fruit.blueberry.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3700 */       .setNutritionValues(570, 140, 3, 7)
/* 3701 */       .setFoodGroup(1179);
/*      */     
/* 3703 */     createItemTemplate(367, "lingonberry", "lingonberries", "excellent", "good", "ok", "poor", "A handful of small, red berries with a sour and sweet taste.", new short[] { 5, 146, 80, 46, 55, 212 }, (short)509, (short)1, 0, 86400L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fruit.lingonberry.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3712 */       .setNutritionValues(530, 115, 12, 8)
/* 3713 */       .setFoodGroup(1179);
/*      */     
/* 3715 */     createItemTemplate(362, "strawberries", "handfuls of strawberry", "excellent", "good", "ok", "poor", "Large, glistening red hearts with tiny yellow seeds.", new short[] { 5, 146, 80, 46, 55, 212 }, (short)489, (short)16, 0, 86400L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fruit.strawberries.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3724 */       .setNutritionValues(330, 77, 2, 7)
/* 3725 */       .setPickSeeds(750)
/* 3726 */       .setFoodGroup(1179);
/*      */     
/* 3728 */     createItemTemplate(750, "strawberry seeds", "strawberry seeds", "superb", "good", "ok", "poor", "A handful of strawberry seeds. They look healthy enough to survive on their own, but they will require a lot of attention to sprout.", new short[] { 20, 5, 146, 55, 129 }, (short)480, (short)1, 0, 28800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.strawberries.", 200.0F, 100, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3736 */       .setNutritionValues(330, 77, 2, 7);
/*      */     
/* 3738 */     createItemTemplate(316, "wemp plants", "wemp plants", "superb", "good", "ok", "poor", "Some wemp plants. It has very strong fibres.", new short[] { 146, 102, 46, 129 }, (short)547, (short)16, 0, 28800L, 5, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.wemp.", 200.0F, 700, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3744 */       .setCrushsTo(318)
/* 3745 */       .setPickSeeds(317);
/*      */     
/* 3747 */     createItemTemplate(134, "hazelnuts", "hazelnuts", "excellent", "good", "ok", "poor", "A handful of fine brown hazel nuts.", new short[] { 5, 21, 146, 80, 217, 74, 212 }, (short)507, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.hazelnut.", 1.0F, 200, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3756 */       .setNutritionValues(628, 167, 608, 150)
/* 3757 */       .setFoodGroup(1197);
/*      */     
/* 3759 */     createItemTemplate(368, "meat fillet", "meat fillets", "excellent", "good", "ok", "poor", "A fillet of meat.", new short[] { 28, 5, 62, 174, 219, 108, 74, 212, 146, 223 }, (short)503, (short)1, 0, 345600L, 2, 6, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.meat.filet.", 1.0F, 300, (byte)2, 20, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3768 */       .setNutritionValues(2500, 0, 150, 260)
/* 3769 */       .setFoodGroup(1261);
/*      */     
/* 3771 */     createItemTemplate(369, "fish fillet", "fish fillets", "excellent", "good", "ok", "poor", "A fillet of fish.", new short[] { 5, 62, 36, 75, 219, 108, 174, 212, 146, 223, 233 }, (short)524, (short)1, 0, 345600L, 2, 6, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.fish.filet.", 1.0F, 300, (byte)2, 20, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3781 */       .setNutritionValues(1200, 0, 50, 150);
/*      */     
/* 3783 */     createItemTemplate(488, "sandwich", "sandwiches", "excellent", "good", "ok", "poor", "A fine sandwich.", new short[] { 82, 212, 75, 220, 222, 233 }, (short)505, (short)1, 0, 172800L, 1, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.bread.sandwich.", 10.0F, 180, (byte)22, 20, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3793 */     createItemTemplate(92, "meat", "meat", "excellent", "good", "ok", "poor", "Raw meat from an animal.", new short[] { 28, 5, 62, 74, 219, 129, 146, 212, 223 }, (short)503, (short)1, 0, 172800L, 2, 6, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.meat.", 1.0F, 300, (byte)2, 20, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3802 */       .setNutritionValues(2500, 0, 150, 260)
/* 3803 */       .setFoodGroup(1261);
/*      */     
/* 3805 */     createItemTemplate(168, "key", "keys", "superb", "good", "ok", "poor", "A metal key with a few large teeth.", new short[] { 40, 41, 114 }, (short)789, (short)1, 0, Long.MAX_VALUE, 1, 1, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.key.", 5.0F, 50, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3812 */     createItemTemplate(344, "marker", "markers", "superb", "good", "ok", "poor", "A shiny magic marker.", new short[] { 45, 59, 52, 92 }, (short)60, (short)1, 0, 60L, 30, 30, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.marker.temporary.", 3.0F, 0, (byte)21);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3817 */     createItemTemplate(671, "Deed border", "markers", "superb", "good", "ok", "poor", "A survey marker.", new short[] { 45, 31, 59, 52 }, (short)60, (short)1, 0, 60L, 30, 30, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.marker.temporary.settle.", 3.0F, 0, (byte)21);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3822 */     createItemTemplate(679, "Construction marker", "markers", "superb", "good", "ok", "poor", "Someone is planning a build project here.", new short[] { 52 }, (short)900, (short)1, 0, 604800L, 30, 30, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.marker.build.", 10.0F, 4000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3828 */     createItemTemplate(673, "Perimeter", "markers", "superb", "good", "ok", "poor", "A survey marker.", new short[] { 45, 31, 59, 52 }, (short)60, (short)1, 0, 60L, 30, 30, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.marker.temporary.settle.perimeter.", 3.0F, 0, (byte)21);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3833 */     createItemTemplate(520, "firemarker", "markers", "superb", "good", "ok", "poor", "Fire.", new short[] { 65, 45, 31, 59, 52, 129 }, (short)60, (short)1, 0, 3L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.marker.invis.", 3.0F, 0, (byte)21);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3838 */     createItemTemplate(75, "frying pan", "frying pans", "excellent", "good", "ok", "poor", "A round metal plate with a shaft.", new short[] { 108, 147, 44, 38, 22, 18, 77, 1 }, (short)784, (short)1, 20, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fryingpan.", 10.0F, 2500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3845 */     createItemTemplate(350, "sauce pan", "sauce pans", "excellent", "good", "ok", "poor", "A deep bowl for cooking casseroles and lots of other recipes.", new short[] { 108, 44, 22, 18, 77, 1, 147, 33 }, (short)784, (short)1, 20, 3024000L, 11, 21, 31, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.saucepan.", 20.0F, 1500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3855 */     createItemTemplate(351, "cauldron", "cauldrons", "excellent", "good", "ok", "poor", "A huge dark bowl with three feet and a metal hanger.", new short[] { 108, 147, 44, 22, 77, 1, 33 }, (short)804, (short)1, 20, 3024000L, 40, 40, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.cauldron.", 30.0F, 5500, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3862 */     createItemTemplate(345, "stew", "stews", "excellent", "good", "ok", "poor", "A delicious stew.", new short[] { 5, 219, 26, 74, 233, 108, 212 }, (short)587, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.stew.", 1.0F, 700, (byte)2, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3872 */     createItemTemplate(347, "meal", "meals", "excellent", "good", "ok", "poor", "A delicious well-prepared meal.", new short[] { 82, 76, 219, 222, 233 }, (short)562, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.steak.", 1.0F, 700, (byte)2, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3882 */     createItemTemplate(346, "casserole", "casseroles", "excellent", "good", "ok", "poor", "A tasty casserole.", new short[] { 82, 76, 219, 222, 233 }, (short)531, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.casserole.", 1.0F, 700, (byte)22, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3892 */     createItemTemplate(348, "gulasch", "gulaschs", "excellent", "good", "ok", "poor", "A fine gulasch.", new short[] { 5, 75, 26, 219, 233, 108, 212 }, (short)607, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.gulasch.", 1.0F, 700, (byte)2, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3902 */     createItemTemplate(352, "soup", "soups", "excellent", "good", "ok", "poor", "A yummy soup.", new short[] { 5, 219, 55, 26, 90, 233, 108, 212 }, (short)531, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.soup.", 1.0F, 700, (byte)26, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3912 */     createItemTemplate(634, "dishwater", "dishwaters", "excellent", "good", "ok", "poor", "A tragic attempt at making something nutritious with liquid involved. The result is tasteless.", new short[] { 5, 137, 26, 90 }, (short)589, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.soup.", 1.0F, 700, (byte)26, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3920 */       .setNutritionValues(0, 0, 0, 0);
/*      */     
/* 3922 */     createItemTemplate(373, "porridge", "porridges", "excellent", "good", "ok", "poor", "A grey porridge.", new short[] { 5, 219, 26, 74, 108, 212 }, (short)608, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.porridge.", 1.0F, 700, (byte)26, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3932 */     createItemTemplate(729, "cake", "cakes", "excellent", "good", "ok", "poor", "A wonderful cake made purely of love.", new short[] { 82, 32, 76, 220, 233 }, (short)530, (short)1, 0, 172800L, 25, 25, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cake.", 1.0F, 1200, (byte)22, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3942 */     createItemTemplate(730, "cake slice", "cake slices", "excellent", "good", "ok", "poor", "A slice of cake from a wonderful cake which was made purely of love.", new short[] { 82, 74, 220, 222, 233 }, (short)530, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cake.slice.", 1.0F, 200, (byte)22, 3, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3952 */     createItemTemplate(349, "salt", "salt", "excellent", "good", "ok", "poor", "Fine white, chunky salt.", new short[] { 5, 146, 55 }, (short)643, (short)1, 0, 12096000L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.salt.", 1.0F, 1, (byte)36, 300, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3961 */     createItemTemplate(366, "sassafras", "sassafrases", "excellent", "good", "ok", "poor", "Dum de da, Dum de Da, Dum de Da dill - I love to climb up Sassafras Hill.", new short[] { 146, 103, 5, 78, 55, 212 }, (short)710, (short)1, 0, 9072000L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.sassafras.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3969 */       .setNutritionValues(2000, 450, 60, 80)
/* 3970 */       .setFoodGroup(1158);
/*      */     
/* 3972 */     createItemTemplate(353, "lovage", "lovage", "excellent", "good", "ok", "poor", "A tall plant with shiny, dark green leaves and hollow stems.", new short[] { 146, 105, 5, 78, 55, 212, 206, 221 }, (short)711, (short)1, 0, 9072000L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.lovage.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3980 */       .setNutritionValues(360, 60, 8, 30)
/* 3981 */       .setFoodGroup(1158);
/*      */     
/* 3983 */     createItemTemplate(354, "sage", "sage", "excellent", "good", "ok", "poor", "An evergreen shrub that reaches about three feet in height with leaves that are velvety with long stalks.", new short[] { 146, 104, 5, 55, 78, 212, 206, 221 }, (short)712, (short)1, 0, 9072000L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.sage.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3991 */       .setNutritionValues(3150, 610, 130, 110)
/* 3992 */       .setFoodGroup(1158);
/*      */     
/* 3994 */     createItemTemplate(355, "onion", "onions", "excellent", "good", "ok", "poor", "A small yellow edible onion. You can plant this but it can be hard to keep alive.", new short[] { 146, 102, 5, 55, 29, 212, 20, 223 }, (short)482, (short)1, 0, 1382400L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.onion.", 1.0F, 250, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4002 */       .setNutritionValues(400, 93, 1, 11)
/* 4003 */       .setFoodGroup(1156);
/*      */     
/* 4005 */     createItemTemplate(356, "garlic", "garlics", "excellent", "good", "ok", "poor", "A cluster of white half-moons the size of a small child's hand. These can be planted but will be very hard to grow in this climate.", new short[] { 146, 103, 5, 29, 20, 212, 55, 223 }, (short)483, (short)1, 0, 1382400L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.garlic.", 1.0F, 50, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4014 */       .setNutritionValues(1490, 331, 5, 64)
/* 4015 */       .setFoodGroup(1156);
/*      */     
/* 4017 */     createItemTemplate(357, "oregano", "oregano", "excellent", "good", "ok", "poor", "A sprawling plant with coarse leaves.", new short[] { 5, 146, 78, 212, 55, 206, 221 }, (short)703, (short)1, 0, 28800L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.oregano.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4026 */       .setNutritionValues(2650, 690, 43, 90)
/* 4027 */       .setFoodGroup(1158);
/*      */     
/* 4029 */     createItemTemplate(358, "parsley", "parsley", "excellent", "good", "ok", "poor", "A small but very tasty plant with curly, dark green foliage.", new short[] { 146, 102, 5, 55, 78, 212, 206, 221 }, (short)704, (short)1, 0, 9072000L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.parsley.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4038 */       .setNutritionValues(360, 60, 8, 30)
/* 4039 */       .setFoodGroup(1158);
/*      */     
/* 4041 */     createItemTemplate(359, "basil", "basil", "excellent", "good", "ok", "poor", "This plant has light green leaves and small white flowers.", new short[] { 5, 146, 55, 78, 212, 206, 221 }, (short)627, (short)1, 0, 9072000L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.basil.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4050 */       .setNutritionValues(220, 27, 6, 32)
/* 4051 */       .setFoodGroup(1158);
/*      */     
/* 4053 */     createItemTemplate(360, "thyme", "thyme", "excellent", "good", "ok", "poor", "A low-growing, wiry and woody plant with gray-green leaves.", new short[] { 5, 146, 78, 55, 212, 206, 221 }, (short)705, (short)1, 0, 9072000L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.thyme.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4062 */       .setNutritionValues(1010, 240, 17, 60)
/* 4063 */       .setFoodGroup(1158);
/*      */     
/* 4065 */     createItemTemplate(361, "belladonna", "belladonnas", "excellent", "good", "ok", "poor", "Bluish purple flowers in loose, drooping clusters on short stalks.", new short[] { 5, 146, 79, 78, 55, 212, 206, 221 }, (short)680, (short)1, 0, 9072000L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.belladonna.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4074 */       .setNutritionValues(30, 45, 7, 30)
/* 4075 */       .setFoodGroup(1158);
/*      */     
/* 4077 */     ItemTemplate template = createItemTemplate(374, "emerald", "emeralds", "excellent", "good", "ok", "poor", "A beautiful, forest green emerald.", new short[] { 93, 48, 157 }, (short)543, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.emerald.", 99.0F, 500, (byte)52, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4083 */     template.priceHalfSize = 200.0F;
/* 4084 */     template = createItemTemplate(375, "star emerald", "star emeralds", "excellent", "good", "ok", "poor", "A beautiful clear eggsized sea green emerald.", new short[] { 93, 48, 157 }, (short)543, (short)1, 0, Long.MAX_VALUE, 2, 2, 2, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.emerald.star.", 99.0F, 500, (byte)52, 200000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4090 */     template.priceHalfSize = 150.0F;
/* 4091 */     template = createItemTemplate(376, "ruby", "rubies", "excellent", "good", "ok", "poor", "A passionately red ruby.", new short[] { 93, 48, 157 }, (short)544, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.ruby.", 99.0F, 500, (byte)52, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4096 */     template.priceHalfSize = 200.0F;
/* 4097 */     template = createItemTemplate(377, "star ruby", "star rubies", "excellent", "good", "ok", "poor", "A bright red ruby, as large as the eye of a dragon.", new short[] { 93, 48, 157 }, (short)544, (short)1, 0, Long.MAX_VALUE, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.ruby.star.", 99.0F, 500, (byte)52, 200000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4103 */     template.priceHalfSize = 150.0F;
/* 4104 */     template = createItemTemplate(378, "opal", "opals", "excellent", "good", "ok", "poor", "A dark green, blue and black opal.", new short[] { 93, 48, 157 }, (short)563, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.opal.", 99.0F, 500, (byte)52, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4109 */     template.priceHalfSize = 200.0F;
/* 4110 */     template = createItemTemplate(379, "black opal", "black opals", "excellent", "good", "ok", "poor", "A perfectly black opal.", new short[] { 93, 48, 157 }, (short)563, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.opal.black.", 99.0F, 500, (byte)52, 200000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4115 */     template.priceHalfSize = 150.0F;
/* 4116 */     template = createItemTemplate(380, "diamond", "diamonds", "excellent", "good", "ok", "poor", "A glittering diamond.", new short[] { 93, 48, 157 }, (short)542, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.diamond.", 99.0F, 500, (byte)54, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4121 */     template.priceHalfSize = 200.0F;
/* 4122 */     template = createItemTemplate(381, "star diamond", "star diamond", "excellent", "good", "ok", "poor", "A perfectly clear, many-faceted diamond.", new short[] { 93, 48, 157 }, (short)542, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.diamond.star.", 99.0F, 500, (byte)54, 200000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4128 */     template.priceHalfSize = 150.0F;
/* 4129 */     template = createItemTemplate(382, "sapphire", "sapphires", "excellent", "good", "ok", "poor", "A shiny sky blue sapphire.", new short[] { 93, 48, 157 }, (short)564, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.sapphire.", 99.0F, 500, (byte)52, 100000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4134 */     template.priceHalfSize = 200.0F;
/* 4135 */     template = createItemTemplate(383, "star sapphire", "star sapphires", "excellent", "good", "ok", "poor", "A deep blue shining sapphire.", new short[] { 93, 48, 157 }, (short)564, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.sapphire.star.", 99.0F, 500, (byte)52, 200000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4140 */     template.priceHalfSize = 150.0F;
/* 4141 */     template = createItemTemplate(509, "resurrection stone", "resurrection stones", "excellent", "good", "ok", "poor", "A grey, round and smooth granite stone with a black skull. It is said that it may protect your belongings and knowledge once as you enter the realm of the dead.", new short[] { 40, 120, 25, 42, 53, 127 }, (short)463, (short)1, 0, Long.MAX_VALUE, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.resurrectionstone.", 99.0F, 500, (byte)15, 50000, false);
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
/* 4155 */     template = createItemTemplate(525, "farwalker stone", "farwalker stones", "excellent", "good", "ok", "poor", "A greenish stone glimmering from various precious minerals. It may be used to transport you quickly once to caves vast distances away.", new short[] { 40, 25, 42, 53, 48, 127 }, (short)799, (short)1, 0, Long.MAX_VALUE, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.teleportationstone.", 99.0F, 500, (byte)15, 50000, false);
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
/* 4169 */     template = createItemTemplate(524, "farwalker twig", "farwalker twigs", "excellent", "good", "ok", "poor", "This feet-long twig is polished and dry. It is rumoured that it may transport you one time to a tree far far away.", new short[] { 40, 21, 42, 53, 48, 127 }, (short)759, (short)1, 0, Long.MAX_VALUE, 3, 3, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 99.0F, 500, (byte)14, 50000, false);
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
/* 4183 */     createItemTemplate(384, "guard tower", "jk guard towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.jenn.", 20.0F, 500000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4190 */     createItemTemplate(430, "guard tower", "hots guard towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.hots.", 20.0F, 500000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4197 */     createItemTemplate(528, "guard tower", "mr guard towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.molr.", 20.0F, 500000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4205 */     createItemTemplate(638, "guard tower", "freedom guard towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.free.", 20.0F, 500000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4214 */     createItemTemplate(386, "unfinished item", "unfinished items", "superb", "good", "ok", "poor", "An item under construction.", new short[] { 48, 246 }, (short)60, (short)23, 0, 2419200L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 10.0F, 0, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4219 */     createItemTemplate(385, "felled tree", "felled trees", "fresh", "dry", "brittle", "rotten", "A felled tree. Can be used in palisades or to make smaller logs from.", new short[] { 21, 113, 52, 129, 54, 123, 147, 175, 237 }, (short)606, (short)37, 20, 86401L, 40, 40, 200, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.log.", 200.0F, 200000, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4226 */     createItemTemplate(387, "illusionary item", "items", "superb", "good", "ok", "poor", "An illusion!", new short[] { 48, 45, 59 }, (short)60, (short)1, 0, 60L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.", 10.0F, 0, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4231 */     createItemTemplate(388, "file", "files", "excellent", "good", "ok", "poor", "A rugged metal blade on a shaft, used to make wood smoother.", new short[] { 108, 44, 147, 38, 22, 119 }, (short)749, (short)1, 0, 3024000L, 1, 2, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.file.", 7.0F, 400, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4238 */     createItemTemplate(389, "file blade", "file blades", "excellent", "good", "ok", "poor", "A straight rugged metal blade.", new short[] { 44, 22, 119 }, (short)729, (short)1, 0, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.file.", 3.0F, 300, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4244 */     createItemTemplate(493, "trowel", "trowels", "excellent", "good", "ok", "poor", "A wide metal blade on a shaft, useful in construction and searching through dirt and rocks for useful items.", new short[] { 108, 44, 147, 38, 22, 247 }, (short)750, (short)1, 0, 3024000L, 1, 4, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.trowel.", 10.0F, 450, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4250 */     createItemTemplate(494, "trowel blade", "trowel blades", "excellent", "good", "ok", "poor", "A flat, faintly pointed broad blade used to smooth out mortar on bricks and sift through dirt and rocks.", new short[] { 44, 22 }, (short)730, (short)1, 0, 3024000L, 1, 4, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.trowel.", 7.0F, 350, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4257 */     createItemTemplate(390, "awl", "awls", "excellent", "good", "ok", "poor", "A thin pointy blade on a shaft, used to make small holes.", new short[] { 108, 44, 147, 38, 22, 13 }, (short)754, (short)1, 1, 3024000L, 1, 2, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.awl.", 7.0F, 400, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4263 */     createItemTemplate(391, "awl blade", "awl blades", "excellent", "good", "ok", "poor", "A small pointy, slightly conical blade the length of a hand.", new short[] { 44, 22 }, (short)734, (short)1, 0, 3024000L, 1, 1, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.awl.", 3.0F, 300, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4269 */     createItemTemplate(392, "leather knife", "leather knives", "excellent", "good", "ok", "poor", "A very short and sharp curved blade on a shaft, used to carve in leather.", new short[] { 108, 44, 147, 38, 22, 13 }, (short)766, (short)1, 1, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.leatherknife.", 7.0F, 400, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4276 */     createItemTemplate(393, "leather knife blade", "leather knife blades", "excellent", "good", "ok", "poor", "A very short and very sharp curved blade.", new short[] { 44, 22 }, (short)720, (short)1, 0, 3024000L, 1, 1, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.leatherknife.", 3.0F, 300, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4282 */     createItemTemplate(394, "scissors", "scissors", "excellent", "good", "ok", "poor", "Rough and clumsy, but pretty sharp scissors.", new short[] { 108, 44, 147, 38, 22, 13 }, (short)748, (short)1, 1, 3024000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.scissors.", 7.0F, 400, (byte)11, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4288 */     createItemTemplate(395, "scissor blade", "scissor blades", "excellent", "good", "ok", "poor", "One half of a scissors.", new short[] { 44, 22 }, (short)728, (short)1, 0, 3024000L, 1, 1, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.blade.scissors.", 3.0F, 300, (byte)11);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4293 */     createItemTemplate(396, "clay shaper", "wooden clay shapers", "excellent", "good", "ok", "poor", "A small pointy wooden stick used to mould and make decorations in clay .", new short[] { 108, 44, 21, 147 }, (short)802, (short)1, 0, 9072000L, 1, 1, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.clayshaper.", 3.0F, 100, (byte)14, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4300 */     createItemTemplate(397, "spatula", "wooden spatulas", "excellent", "good", "ok", "poor", "A small flat and smooth piece of wood used to mould clay and other greasy substances.", new short[] { 108, 44, 21, 147 }, (short)808, (short)1, 0, 9072000L, 1, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.spatula.", 3.0F, 100, (byte)14, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4306 */     createItemTemplate(398, "statue of nymph", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "A beautiful almost man-sized statue of a nymph, wearing only a thin robe as garment.", new short[] { 108, 31, 135, 25, 51, 52, 44, 86, 199, 92, 176, 178 }, (short)60, (short)1, 0, 12096000L, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.nymph.", 15.0F, 70000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4314 */     createItemTemplate(399, "statue of demon", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "A horrifying statue of a demon, complete with horns, claws, sharp teeth and mad piercing eyes.", new short[] { 108, 31, 25, 135, 51, 52, 44, 67, 86, 92, 176, 178, 199 }, (short)60, (short)1, 0, 12096000L, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.demon.", 15.0F, 100000, (byte)15);
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
/* 4331 */     createItemTemplate(400, "statue of dog", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "A statue of a large guard dog, fairly menacing yet lovingly portrayed.", new short[] { 108, 31, 25, 135, 51, 52, 44, 67, 86, 92, 176, 178, 199 }, (short)60, (short)1, 0, 12096000L, 20, 30, 160, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.dog.", 5.0F, 70000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4341 */     createItemTemplate(401, "statue of troll", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "A huge ugly troll, about one and a half times your size.", new short[] { 108, 31, 25, 135, 86, 51, 52, 44, 67, 92, 176, 199, 178 }, (short)60, (short)1, 0, 12096000L, 40, 40, 260, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.troll.", 5.0F, 150000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4351 */     createItemTemplate(518, "colossus", "colossus", "almost full", "somewhat occupied", "half-full", "emptyish", "A statue of gigantic proportions, resembling someone really noteworthy.", new short[] { 108, 31, 25, 52, 44, 67, 92, 49, 194, 195, 123, 135, 178, 157 }, (short)60, (short)1, 0, 12096000L, 500, 500, 2000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.colossus.", 50.0F, 7500000, (byte)15)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4360 */       .setDyeAmountGrams(65000);
/* 4361 */     createItemTemplate(402, "statue of boy", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "A real sized statue of a small cute boy.", new short[] { 108, 31, 25, 135, 86, 51, 52, 44, 67, 92, 176, 178, 199 }, (short)60, (short)1, 0, 12096000L, 10, 20, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.boy.", 10.0F, 60000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4371 */     createItemTemplate(403, "statue of girl", "statues", "almost full", "somewhat occupied", "half-full", "emptyish", "A real sized statue of a small cute girl.", new short[] { 108, 31, 25, 135, 86, 51, 52, 44, 67, 92, 176, 178, 199 }, (short)60, (short)1, 0, 12096000L, 10, 20, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statue.girl.", 10.0F, 60000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4381 */     createItemTemplate(404, "bench", "benches", "almost full", "somewhat occupied", "half-full", "emptyish", "A stone slab on two bricks. It looks very inviting.", new short[] { 108, 31, 135, 25, 86, 51, 52, 44, 67, 176, 178, 117, 197, 1, 180, 256 }, (short)60, (short)41, 0, 12096000L, 50, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.bench.", 5.0F, 110000, (byte)15)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4391 */       .setContainerSize(30, 60, 200);
/* 4392 */     createItemTemplate(406, "slab", "slabs", "almost full", "somewhat occupied", "half-full", "emptyish", "A flat and square stone slab. It is about your length and width.", new short[] { 25, 51, 135, 86, 44, 146, 158, 242, 243 }, (short)689, (short)1, 0, 12096000L, 10, 50, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.slab.", 3.0F, 80000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4400 */     createItemTemplate(407, "coffin", "coffins", "almost full", "somewhat occupied", "half-full", "emptyish", "A crude coffin made from stone with a heavy lid.", new short[] { 108, 31, 135, 86, 25, 51, 52, 44, 67, 1, 176, 178, 259 }, (short)254, (short)1, 0, 12096000L, 50, 51, 201, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.coffin.", 20.0F, 240000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4409 */     createItemTemplate(405, "decorative fountain", "fountains", "almost full", "somewhat occupied", "half-full", "emptyish", "A delicate stone drinking fountain.", new short[] { 108, 31, 86, 135, 25, 51, 52, 44, 67, 33, 128, 176, 178 }, (short)60, (short)1, 0, 12096000L, 50, 50, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.fountain.drink.", 30.0F, 50000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4418 */     createItemTemplate(408, "fountain", "fountains", "almost full", "somewhat occupied", "half-full", "emptyish", "A low stone fountain with a basin wide enough to sit in.", new short[] { 108, 31, 135, 86, 25, 51, 52, 44, 67, 1, 33, 128, 176, 178 }, (short)60, (short)1, 0, 12096000L, 50, 150, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.fountain.", 40.0F, 120000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4427 */     createItemTemplate(635, "ornate fountain", "fountains", "almost full", "somewhat occupied", "half-full", "emptyish", "A low stone fountain with a basin wide enough to sit in.", new short[] { 108, 31, 135, 86, 25, 51, 52, 44, 67, 1, 33, 128, 176, 178 }, (short)60, (short)1, 0, 12096000L, 50, 150, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.fountain.2.", 40.0F, 120000, (byte)15);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4437 */     createItemTemplate(6, "green apple", "green apples", "delicious", "nice", "old", "rotten", "A fruit, once probably picked from a tree.", new short[] { 5, 146, 80, 157, 55 }, (short)508, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.apple.green.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4445 */       .setNutritionValues(520, 140, 2, 3)
/* 4446 */       .setFoodGroup(1163);
/*      */     
/* 4448 */     createItemTemplate(410, "lemon", "lemons", "delicious", "nice", "old", "rotten", "A yellow lemon.", new short[] { 146, 103, 5, 80, 157, 55 }, (short)488, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.lemon.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4457 */       .setNutritionValues(290, 90, 3, 11)
/* 4458 */       .setFoodGroup(1163);
/*      */     
/* 4460 */     createItemTemplate(411, "blue grapes", "blue grapes", "delicious", "nice", "old", "rotten", "A fine cluster of blue grapes.", new short[] { 5, 146, 80, 46, 157, 55 }, (short)565, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.grapes.blue.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4469 */       .setNutritionValues(670, 170, 4, 6)
/* 4470 */       .setFoodGroup(1163);
/*      */     
/* 4472 */     createItemTemplate(414, "green grapes", "green grapes", "delicious", "nice", "old", "rotten", "A fine cluster of green grapes.", new short[] { 5, 146, 80, 46, 157, 55 }, (short)545, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.grapes.green.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4481 */       .setNutritionValues(680, 150, 2, 3)
/* 4482 */       .setFoodGroup(1163);
/*      */     
/* 4484 */     createItemTemplate(412, "olives", "olives", "delicious", "nice", "old", "rotten", "Black olives.", new short[] { 5, 146, 46, 157, 55 }, (short)548, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.olives.black.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4492 */       .setNutritionValues(1150, 60, 110, 8)
/* 4493 */       .setFoodGroup(1163);
/*      */     
/* 4495 */     createItemTemplate(409, "cherries", "red cherries", "delicious", "nice", "old", "rotten", "Red lucsious cherries, full of sweet juice.", new short[] { 5, 146, 80, 46, 157, 55 }, (short)485, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cherries.", 100.0F, 200, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4504 */       .setNutritionValues(500, 120, 3, 10)
/* 4505 */       .setFoodGroup(1163);
/*      */     
/* 4507 */     createItemTemplate(415, "maple syrup", "maple syrup", "excellent", "good", "ok", "poor", "A thick, lightly golden syrup, with a delicate flavour and smooth texture.", new short[] { 26, 88, 90 }, (short)587, (short)1, 0, 604800L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.syrup.maple.", 1.0F, 60, (byte)26)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4515 */       .setNutritionValues(2600, 670, 1, 0);
/*      */     
/* 4517 */     createItemTemplate(416, "maple sap", "maple sap", "excellent", "good", "ok", "poor", "A clear liquid with a slightly sweet taste.", new short[] { 26, 211 }, (short)587, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.sap.maple.", 10.0F, 1000, (byte)26)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4525 */       .setNutritionValues(2600, 670, 1, 0);
/*      */     
/* 4527 */     createItemTemplate(417, "fruit juice", "fruit juices", "excellent", "good", "ok", "poor", "The juice of a fruit.", new short[] { 26, 88, 90, 216, 233 }, (short)582, (short)1, 0, 604800L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.juice.", 10.0F, 100, (byte)26);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4537 */     createItemTemplate(419, "red wine", "red wines", "excellent", "good", "ok", "poor", "Red wine.", new short[] { 108, 26, 88, 89, 90, 213, 212 }, (short)582, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.wine.red.", 13.0F, 1000, (byte)26)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4546 */       .setAlcoholStrength(13);
/*      */     
/* 4548 */     createItemTemplate(420, "white wine", "white wines", "excellent", "good", "ok", "poor", "White wine.", new short[] { 108, 26, 88, 89, 90, 213, 212 }, (short)584, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.wine.white.", 11.0F, 1000, (byte)26)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4557 */       .setAlcoholStrength(11);
/*      */     
/* 4559 */     createItemTemplate(421, 2, "bucket", "buckets", "almost full", "somewhat occupied", "half-full", "emptyish", "A small wooden bucket.", new short[] { 108, 51, 1, 21, 33, 52, 44, 92, 147, 139 }, (short)265, (short)1, 0, 9072000L, 20, 20, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.bucket.small.", 5.0F, 1500, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4568 */     createItemTemplate(422, "camellia", "camellias", "excellent", "good", "ok", "poor", "A bundle of thin leathery dark green leaves.", new short[] { 146, 104, 211, 55, 78, 113, 157, 212 }, (short)707, (short)1, 0, 9072000L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.camellia.", 100.0F, 50, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4577 */       .setNutritionValues(30, 30, 10, 10);
/*      */     
/* 4579 */     createItemTemplate(423, "oleander", "oleanders", "excellent", "good", "ok", "poor", "Thin and pointy light green leaves in a small bundle.", new short[] { 5, 146, 78, 79, 55, 113, 157, 212 }, (short)708, (short)1, 0, 9072000L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.oleander.", 100.0F, 50, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4588 */       .setNutritionValues(30, 30, 10, 10)
/* 4589 */       .setFoodGroup(1158);
/*      */     
/* 4591 */     createItemTemplate(424, "lavender flower", "lavender flowers", "excellent", "good", "ok", "poor", "A cluster of small light blue flowers with a sweet scent.", new short[] { 146, 103, 113, 157, 212, 211 }, (short)661, (short)1, 0, 9072000L, 1, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.lavender.", 100.0F, 50, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4600 */       .setNutritionValues(30, 30, 10, 10);
/*      */     
/* 4602 */     createItemTemplate(425, "tea", "tea", "excellent", "good", "ok", "poor", "A green-brown watery liquid with a slightly smoky scent.", new short[] { 26, 88, 90, 233 }, (short)540, (short)1, 0, 86400L, 10, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 1.0F, 1000, (byte)26);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4611 */     createItemTemplate(426, "rose flower", "rose flowers", "excellent", "good", "ok", "poor", "A crimson rose, token of eternal love.", new short[] { 146, 102, 113, 157, 211, 212, 233 }, (short)660, (short)1, 0, 9072000L, 1, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.rose.", 100.0F, 50, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4620 */       .setNutritionValues(450, 90, 7, 27);
/*      */     
/* 4622 */     createItemTemplate(620, "mixed grass", "bunches of mixed grass", "excellent", "good", "ok", "poor", "Mixed grasses abundant with seeds.", new short[] { 186, 146, 46, 113, 55 }, (short)702, (short)1, 0, 9072000L, 1, 4, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.mixedgrass.", 100.0F, 50, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4632 */     createItemTemplate(755, "kelp", "bunches of kelp", "excellent", "good", "ok", "poor", "A brown algae long and slimy with air bladders clearly visible.", new short[] { 146, 46, 113, 55, 211, 157 }, (short)599, (short)1, 0, 9072000L, 1, 4, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.mixedgrass.", 100.0F, 50, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4642 */     createItemTemplate(427, "lemonade", "lemonade", "excellent", "good", "ok", "poor", "A very refreshing sweet lemonade.", new short[] { 26, 88, 90, 233 }, (short)540, (short)1, 0, 86400L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 1.0F, 120, (byte)26);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4651 */     createItemTemplate(428, "jam", "jams", "excellent", "good", "ok", "poor", "Sweet delicious jam.", new short[] { 108, 5, 220, 74, 233 }, (short)540, (short)1, 0, 604800L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.jam.", 5.0F, 60, (byte)26);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4661 */     createItemTemplate(413, "fruit press", "fruit presses", "excellent", "good", "ok", "poor", "A wooden contraption made from planks with a shaft to press in order to extract juice from fruits and vegetables.", new short[] { 108, 44, 144, 38, 21, 139, 33, 1, 147, 210 }, (short)246, (short)1, 0, 9072000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fruitpress.", 30.0F, 2000, (byte)14);
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
/* 4672 */     createItemTemplate(749, "reed pen", "reed pens", "excellent", "good", "ok", "poor", "A rough pen made from a section of a reed straw.", new short[] { 108, 1, 144, 38, 21, 139, 33 }, (short)275, (short)1, 0, 9072000L, 2, 2, 2, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.reedpen.", 10.0F, 200, (byte)60);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4682 */     createItemTemplate(747, "press", "presses", "excellent", "good", "ok", "poor", "A wooden construction made from planks with a large wooden screw connected to a shaft. It is used for making things flat.", new short[] { 108, 44, 144, 38, 21, 139, 210 }, (short)246, (short)1, 0, 9072000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.press.", 30.0F, 2000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4692 */     createItemTemplate(418, "olive oil", "olive oil", "excellent", "good", "ok", "poor", "Clear, syrupy olive oil with a sweet scent.", new short[] { 108, 26, 88, 34, 158, 113 }, (short)587, (short)1, 0, 2419200L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.oliveoil.", 10.0F, 180, (byte)26)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4701 */       .setNutritionValues(8840, 0, 1000, 0)
/* 4702 */       .setFoodGroup(1263);
/*      */     
/* 4704 */     createItemTemplate(429, "support beam", "support beams", "almost full", "somewhat occupied", "half-full", "emptyish", "A wooden construction made from logs, planks and metal ribbons intended to support walls in mines.", new short[] { 21, 146, 44, 113 }, (short)300, (short)1, 0, 9072000L, 60, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.joists.", 15.0F, 60000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4711 */     createItemTemplate(495, "floor boards", "floor boards", "almost full", "somewhat occupied", "half-full", "emptyish", "Wooden planks joined as to cover a section of a floor.", new short[] { 21, 146, 44, 113, 242, 243 }, (short)293, (short)1, 0, 9072000L, 21, 60, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.floorboards.", 15.0F, 10300, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4719 */     createItemTemplate(753, "black ink", "black ink", "excellent", "good", "ok", "poor", "Black ink, with a faint smell of fish.", new short[] { 26, 88, 90 }, (short)583, (short)1, 0, 9072000L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.ink.black.", 20.0F, 100, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4725 */     createItemTemplate(752, "ink sac", "ink sacs", "excellent", "good", "ok", "poor", "A sac from a butchered animal, containing mysterious substances.", new short[] { 106, 62, 46 }, (short)515, (short)1, 0, 172800L, 3, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.inksac.", 1.0F, 200, (byte)2, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4732 */     createItemTemplate(431, "black dye", "black dye", "excellent", "good", "ok", "poor", "Black dye, with a faint smell of iron and lemon.", new short[] { 26, 91, 113 }, (short)583, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.dye.black.", 40.0F, 1000, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4737 */     createItemTemplate(432, "white dye", "white dye", "excellent", "good", "ok", "poor", "White dye, used to bleach with.", new short[] { 26, 91, 113 }, (short)584, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.dye.white.", 20.0F, 1000, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4742 */     createItemTemplate(435, "green dye", "green dye", "excellent", "good", "ok", "poor", "Green dye.", new short[] { 26, 91, 113 }, (short)581, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.dye.green.", 10.0F, 1000, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4747 */     createItemTemplate(433, "red dye", "red dye", "excellent", "good", "ok", "poor", "Red dye, considered most luxurious.", new short[] { 26, 91, 113 }, (short)582, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.dye.red.", 30.0F, 1000, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4752 */     createItemTemplate(434, "blue dye", "blue dye", "excellent", "good", "ok", "poor", "Blue dye, made from some plant.", new short[] { 26, 91, 113 }, (short)580, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.dye.blue.", 15.0F, 1000, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4757 */     createItemTemplate(438, "dye", "dye", "excellent", "good", "ok", "poor", "Dye, used to colour clothing and items with.", new short[] { 26, 91 }, (short)540, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.dye.", 100.0F, 1000, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4762 */     createItemTemplate(436, "acorn", "acorns", "delicious", "nice", "old", "rotten", "A handful of oak acorns.", new short[] { 146, 103, 113 }, (short)487, (short)1, 0, 86400L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.acorn.", 100.0F, 200, (byte)22);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4767 */     createItemTemplate(437, "tannin", "tannin", "excellent", "good", "ok", "poor", "Tannin acid made from acorns and water.", new short[] { 26, 113 }, (short)583, (short)1, 0, 9072000L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.tannin.", 40.0F, 1000, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4772 */     createItemTemplate(439, "cochineal", "cochineals", "delicious", "nice", "old", "rotten", "A handful of a brightly red sticky substance.", new short[] { 146, 79, 113, 164, 211 }, (short)647, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.goo.red.", 100.0F, 200, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4777 */     createItemTemplate(445, 2, "catapult", "catapult", "almost full", "somewhat occupied", "half-full", "emptyish", "A small four wheel catapult designed to be dragged by one person. It could be used to hurl items at walls and fences.", new short[] { 108, 31, 21, 51, 56, 52, 44, 92, 86, 109, 176, 177 }, (short)60, (short)40, 0, 9072000L, 240, 240, 350, 10077, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.catapult.", 30.0F, 400000, (byte)14, 10000, true, -1);
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
/* 4795 */     createItemTemplate(447, "short bow", "short bows", "fresh", "dry", "brittle", "rotten", "An elegant, small and quick bow that will fire moderately far with good accuracy.", new short[] { 108, 44, 21, 94, 84, 147 }, (short)1214, (short)1, 50, 9072000L, 1, 3, 100, 10079, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bow.short.", 5.0F, 1000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4802 */     createItemTemplate(448, "bow", "bows", "fresh", "dry", "brittle", "rotten", "A sturdy bow that will fire pretty far with good accuracy.", new short[] { 108, 44, 21, 94, 84, 147 }, (short)1234, (short)1, 60, 9072000L, 1, 3, 150, 10080, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bow.medium.", 20.0F, 1000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4809 */     createItemTemplate(449, "long bow", "long bows", "fresh", "dry", "brittle", "rotten", "A long slender bow that must be fired pretty far for good accuracy. It will then provide a hefty punch.", new short[] { 108, 44, 21, 94, 84, 147 }, (short)1254, (short)1, 70, 9072000L, 1, 3, 200, 10081, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bow.long.", 20.0F, 1000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4817 */     createItemTemplate(459, "short bow", "short bows", "fresh", "dry", "brittle", "rotten", "An unstringed small bow.", new short[] { 108, 44, 21, 95, 84 }, (short)1214, (short)1, 0, 9072000L, 1, 3, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bow.short.", 5.0F, 1000, (byte)14, 50, true);
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
/* 4830 */     createItemTemplate(460, "bow", "bows", "fresh", "dry", "brittle", "rotten", "An unstringed sturdy bow.", new short[] { 108, 44, 21, 95, 84 }, (short)1234, (short)1, 0, 9072000L, 1, 3, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bow.medium.", 20.0F, 1000, (byte)14, 50, true);
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
/* 4843 */     createItemTemplate(461, "long bow", "long bows", "fresh", "dry", "brittle", "rotten", "A long but unstringed slender bow.", new short[] { 108, 44, 21, 95, 84 }, (short)1254, (short)1, 0, 9072000L, 1, 3, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bow.long.", 20.0F, 1000, (byte)14, 50, true);
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
/* 4857 */     createItemTemplate(454, "arrow shaft", "arrow shafts", "excellent", "good", "ok", "poor", "A straight, thin and finely polished shaft made of wood.", new short[] { 21, 146, 113, 129, 147 }, (short)1255, (short)1, 0, 28800L, 1, 1, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.shaft.", 1.0F, 100, (byte)14, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4863 */     createItemTemplate(455, "hunting arrow", "hunting arrows", "excellent", "good", "ok", "poor", "An arrow with a thin straight head that can more easily be pulled out of a killed creature.", new short[] { 108, 146, 44, 21, 147, 113 }, (short)1235, (short)1, 0, 9072000L, 1, 1, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.hunting.", 10.0F, 140, (byte)14, 20, false);
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
/* 4877 */     createItemTemplate(456, "war arrow", "war arrows", "excellent", "good", "ok", "poor", "An arrow with barbs designed to inflict damage to anyone trying to pull the arrow out.", new short[] { 108, 146, 44, 21, 147, 113 }, (short)1215, (short)1, 0, 9072000L, 1, 1, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.war.", 15.0F, 140, (byte)14, 30, false);
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
/* 4891 */     createItemTemplate(451, "hunting arrow head", "hunting arrow heads", "excellent", "good", "ok", "poor", "A straight pointy arrow head without barbs.", new short[] { 22, 146, 113, 158 }, (short)1236, (short)1, 0, 3024000L, 1, 1, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.head.hunt.", 5.0F, 40, (byte)11, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4897 */     createItemTemplate(452, "war arrow head", "war arrow heads", "excellent", "good", "ok", "poor", "A thin arrow head with vicious barbs.", new short[] { 22, 146, 113, 158 }, (short)1216, (short)1, 0, 3024000L, 1, 1, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.arrow.head.war.", 20.0F, 40, (byte)11, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4903 */     createItemTemplate(457, "bow string", "bow strings", "excellent", "good", "ok", "poor", "A strong string of entwined wemp fibres.", new short[] { 129, 146, 113 }, (short)620, (short)1, 0, 28800L, 1, 1, 1, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.string.", 10.0F, 10, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4908 */     createItemTemplate(458, "archery target", "archery targets", "superb", "good", "ok", "poor", "A wooden practice target made from planks and logs with a few circles around the bulls eye.", new short[] { 108, 21, 51, 52, 86, 44, 31, 176, 199 }, (short)255, (short)1, 0, 9072000L, 10, 80, 220, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.archerytarget.", 10.0F, 150000, (byte)14, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4916 */     createItemTemplate(462, "quiver", "quivers", "excellent", "good", "ok", "poor", "A quiver sown from leather, designed to be hanging across your back or from your hips.", new short[] { 108, 44, 23, 1, 147 }, (short)760, (short)1, 0, 3024000L, 5, 8, 41, -10, new byte[] { 41, 42 }, "model.container.quiver.", 15.0F, 250, (byte)16, 2000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4926 */     createItemTemplate(464, "egg", "eggs", "excellent", "good", "ok", "poor", "A white egg from a bird.", new short[] { 5, 48, 96, 113, 219, 212, 146, 74 }, (short)521, (short)1, 0, 172800L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.egg.", 200.0F, 100, (byte)28, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4935 */       .setNutritionValues(520, 7, 2, 110);
/*      */     
/* 4937 */     createItemTemplate(465, "huge egg", "huge eggs", "excellent", "good", "ok", "poor", "A huge yellow egg.", new short[] { 112, 5, 48, 96, 59, 219 }, (short)522, (short)1, 0, 172800L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.egg.large.", 200.0F, 100, (byte)28, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4943 */     createItemTemplate(466, "easter egg", "easter eggs", "excellent", "good", "ok", "poor", "A large funny-coloured egg.", new short[] { 96, 52, 237 }, (short)522, (short)1, 0, 3600L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.egg.easter.", 200.0F, 100, (byte)22, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4948 */     createItemTemplate(467, "peat", "peat", "excellent", "good", "ok", "poor", "A square piece of peat, perfect for burning.", new short[] { 21, 146, 27, 112, 113, 141, 46 }, (short)690, (short)1, 0, 9072000L, 15, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.peat.", 200.0F, 10000, (byte)59, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4954 */     createItemTemplate(479, "moss", "moss", "superb", "good", "ok", "poor", "A lot of gathered moss.", new short[] { 186, 27, 146, 112, 113, 141 }, (short)549, (short)1, 0, 86401L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.moss.", 200.0F, 300, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4960 */     createItemTemplate(480, "compass", "compasses", "superb", "good", "ok", "poor", "A rudimentary compass, made from a needle floating in oil on a piece of wood in a jar.", new short[] { 108, 22, 100, 147, 97 }, (short)792, (short)1, 0, 3024000L, 3, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.compass.", 10.0F, 600, (byte)30, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4966 */     createItemTemplate(481, "healing cover", "healing covers", "excellent", "good", "ok", "poor", "A bunch of interwoven grass mixed with various healing ingredients.", new short[] { 54, 113, 129, 147 }, (short)341, (short)1, 0, 28800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.healing.cover.", 100.0F, 400, (byte)22);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4972 */     createItemTemplate(489, "spyglass", "spyglasses", "superb", "good", "ok", "poor", "A brass tube the length of your underarm. It has a small hole in one end covered by glass, and a similar but larger one on the other end.", new short[] { 22, 44, 87, 42 }, (short)860, (short)1, 0, 2419200L, 3, 5, 40, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.statuette.", 200.0F, 2000, (byte)30, 500, true);
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
/* 4986 */     createItemTemplate(511, "spirit cottage", "spirit cottages", "almost full", "somewhat occupied", "half-full", "emptyish", "A stone mailbox fashioned in the way of a small cottage.", new short[] { 108, 86, 1, 31, 25, 51, 52, 44, 67, 92, 160 }, (short)60, (short)1, 0, 12096000L, 100, 100, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.spiritcottage.", 60.0F, 240000, (byte)15, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4994 */     createItemTemplate(513, "spirit castle", "spirit castles", "almost full", "somewhat occupied", "half-full", "emptyish", "A decorated model of a castle that works as a mailbox.", new short[] { 108, 86, 1, 31, 25, 51, 52, 44, 67, 92, 160 }, (short)60, (short)1, 0, 12096000L, 100, 100, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.spiritcastle.", 60.0F, 240000, (byte)15, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5002 */     createItemTemplate(510, "spirit house", "spirit houses", "almost full", "somewhat occupied", "half-full", "emptyish", "A wooden mailbox looking like a wooden house.", new short[] { 108, 86, 1, 31, 21, 51, 52, 44, 67, 92, 160 }, (short)60, (short)1, 0, 9072000L, 100, 100, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.spirithouse.", 50.0F, 150000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5010 */     createItemTemplate(512, "spirit mansion", "spirit mansions", "almost full", "somewhat occupied", "half-full", "emptyish", "A fashionable wooden mailbox that looks like a rich mansion.", new short[] { 108, 86, 1, 31, 21, 51, 52, 44, 67, 92, 160 }, (short)60, (short)1, 0, 9072000L, 100, 100, 200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.spiritmansion.", 50.0F, 150000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5019 */     createItemTemplate(514, "whip of One", "whips", "excellent", "good", "ok", "poor", "A thick black leather whip with metal hooks.", new short[] { 108, 44, 23, 92, 42, 2, 37, 70 }, (short)1359, (short)1, 20, 3024000L, 1, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.weapon.whip.one.", 100.0F, 500, (byte)16, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5025 */     createItemTemplate(515, 3, "crown of One", "crowns", "excellent", "good", "ok", "poor", "A thick steel crown that glimmers white in the sun.", new short[] { 42, 108, 44, 22, 4, 40, 70 }, (short)969, (short)1, 0, 3024000L, 1, 10, 20, -10, new byte[] { 1, 28 }, "model.armour.head.steelcrown.", 20.0F, 1700, (byte)0, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5033 */     createItemTemplate(516, "toolbelt", "toolbelts", "excellent", "good", "ok", "poor", "An ingenious system of pockets, pouches, hooks and holes designed to keep a wide array of common tools.", new short[] { 108, 44, 23, 92, 147, 121, 97 }, (short)861, (short)1, 0, 3024000L, 2, 5, 10, -10, new byte[] { 34, 43 }, "model.clothing.belt.toolbelt.", 35.0F, 1000, (byte)16, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5042 */     createItemTemplate(517, "hooks", "hooks", "excellent", "good", "ok", "poor", "Small bent pieces of metal used to hang up things on or to clasp two things together.", new short[] { 44, 22, 46, 147, 146 }, (short)845, (short)1, 0, 3024000L, 1, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.hooks.", 1.0F, 200, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5048 */     createItemTemplate(521, "lair", "lairs", "excellent", "good", "ok", "poor", "The den where some denizens reside.", new short[] { 52, 21, 31, 67, 86, 49, 98, 48, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 400, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.den.", 20.0F, 500000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5055 */     createItemTemplate(527, "farwalker amulet", "amulets", "superb", "good", "ok", "poor", "A couple of feathers and pearls attached to a few looped silver strings.", new short[] { 42, 22, 53, 48, 127 }, (short)779, (short)1, 0, Long.MAX_VALUE, 1, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.amulet.farwalker.", 200.0F, 100, (byte)8, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5062 */     createItemTemplate(538, "stone of the sword", "stones", "superb", "good", "ok", "poor", "A fairly large round stone with a deep notch.", new short[] { 25, 52, 31, 70, 40, 122, 123 }, (short)800, (short)1, 0, Long.MAX_VALUE, 50, 50, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.stone.swordstone.", 10.0F, 2000000, (byte)15, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5070 */     createItemTemplate(490, 2, "rowing boat", "rowing boats", "almost full", "somewhat occupied", "half-full", "emptyish", "A small rowing boat that will accommodate three people.", new short[] { 60, 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 47, 48, 54, 157, 160, 255 }, (short)60, (short)41, 0, 9072000L, 60, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.boat.rowing.", 17.0F, 120000, (byte)14, 10000, true, -1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5080 */       .setDyeAmountGrams(5000);
/* 5081 */     createItemTemplate(491, 2, "sailing boat", "sailing boats", "almost full", "somewhat occupied", "half-full", "emptyish", "A small sailing boat that will accommodate five people.", new short[] { 60, 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 47, 48, 54, 157, 160, 249, 255 }, (short)60, (short)41, 0, 9072000L, 60, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.small.boat.sailing.", 30.0F, 140000, (byte)14, 10000, true, -1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5091 */       .setSecondryItem("sail")
/* 5092 */       .setDyeAmountGrams(5000);
/* 5093 */     createItemTemplate(539, 4, "cart", "carts", "almost full", "somewhat occupied", "half-full", "emptyish", "A fairly large two wheel cart designed to be dragged by an animal.", new short[] { 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 134, 47, 193, 54, 176, 180, 160 }, (short)60, (short)41, 0, 9072000L, 130, 120, 410, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.large.cart.", 30.0F, 240000, (byte)14, 10000, true, -1)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5103 */       .setContainerSize(120, 120, 410)
/* 5104 */       .setDyeAmountGrams(15000);
/* 5105 */     createItemTemplate(540, "cog", "cogs", "almost full", "somewhat occupied", "half-full", "emptyish", "A sturdy, one-masted merchant ship with a flat bottom.", new short[] { 60, 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 47, 48, 54, 157, 160, 249, 255 }, (short)60, (short)41, 0, 9072000L, 210, 400, 610, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.boat.cog.", 50.0F, 800000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5115 */       .setSecondryItem("sail");
/* 5116 */     createItemTemplate(541, "corbita", "corbitas", "almost full", "somewhat occupied", "half-full", "emptyish", "A ship with square sails, steered using two side rudders connected to each other.", new short[] { 60, 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 47, 48, 54, 157, 180, 160, 249, 255 }, (short)60, (short)41, 0, 9072000L, 210, 600, 810, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.boat.corbita.", 40.0F, 1400000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5126 */       .setContainerSize(210, 550, 740)
/* 5127 */       .setSecondryItem("sail");
/* 5128 */     createItemTemplate(542, "knarr", "knarrs", "almost full", "somewhat occupied", "half-full", "emptyish", "A ship with a clinker-built hull assembled with iron rivets, and one mast with a square yard sail. In insufficient wind it is rowed with oars. The side rudder is on the starboard side.", new short[] { 60, 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 47, 48, 54, 157, 180, 160, 249, 255 }, (short)60, (short)41, 0, 9072000L, 400, 800, 1500, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.boat.knarr.", 60.0F, 2000000, (byte)14, 10000, true)
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
/* 5139 */       .setContainerSize(398, 385, 495)
/* 5140 */       .setSecondryItem("sail");
/* 5141 */     createItemTemplate(543, "caravel", "caravels", "almost full", "somewhat occupied", "half-full", "emptyish", "An impressive merchant ship.", new short[] { 60, 108, 1, 31, 21, 51, 56, 52, 44, 92, 117, 47, 48, 54, 157, 180, 160, 249, 255 }, (short)60, (short)41, 0, 9072000L, 600, 1200, 2100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.boat.caravel.", 70.0F, 2400000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5151 */       .setContainerSize(300, 600, 600)
/* 5152 */       .setSecondryItem("sail")
/* 5153 */       .setDyeAmountGrams(220000);
/* 5154 */     createItemTemplate(561, "peg", "pegs", "excellent", "good", "ok", "poor", "These pegs come in various shapes and sizes and are often used in carpentry and shipbuilding.", new short[] { 21, 129, 146, 113 }, (short)666, (short)1, 0, 28800L, 2, 2, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.peg.", 5.0F, 100, (byte)14, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5160 */     createItemTemplate(544, "rudder", "rudders", "fine", "nice", "mediocre", "poor", "This rudder consists of a flat area with a shaft attached.", new short[] { 21, 44 }, (short)60, (short)1, 0, 9072000L, 10, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.rudder.", 17.0F, 13000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5165 */     createItemTemplate(545, "seat", "seat", "fine", "nice", "mediocre", "poor", "A thick plank intended to sit on in a boat.", new short[] { 21, 44 }, (short)60, (short)1, 0, 9072000L, 10, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.seat.", 6.0F, 20000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5170 */     createItemTemplate(546, "hull plank", "hull planks", "fresh", "dry", "brittle", "rotten", "A six steps long wooden plank that could be used to build the hull of a boat.", new short[] { 21, 146, 129 }, (short)626, (short)1, 0, 9072000L, 3, 5, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.plank.hull.", 10.0F, 8000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5176 */     createItemTemplate(566, "deck board", "deck boards", "fresh", "dry", "brittle", "rotten", "A fairly short wooden plank used on the board of a deck.", new short[] { 21, 146, 129 }, (short)626, (short)1, 0, 9072000L, 3, 5, 100, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.plank.deck.", 27.0F, 4000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5182 */     createItemTemplate(547, "anchor", "anchors", "fine", "nice", "mediocre", "poor", "A heavy anchor made from lead used to moor ships.", new short[] { 22, 44 }, (short)460, (short)1, 0, 3024000L, 10, 60, 70, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.anchor.", 47.0F, 20000, (byte)12, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5187 */     createItemTemplate(548, "ship helm", "ship helms", "fine", "nice", "mediocre", "poor", "A ship wheel used to steer a large vessel.", new short[] { 21, 44 }, (short)60, (short)1, 0, 9072000L, 10, 80, 80, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.shipwheel.", 57.0F, 20000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5192 */     createItemTemplate(549, 2, "tackle", "tackles", "fine", "nice", "mediocre", "poor", "A tackle used together with a rope will provide a powerful pull. This is a smaller type.", new short[] { 21, 44, 146 }, (short)60, (short)1, 0, 9072000L, 5, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.tackle.small.", 45.0F, 5000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5198 */     createItemTemplate(550, 4, "tackle", "tackles", "fine", "nice", "mediocre", "poor", "A tackle used together with a rope will provide a powerful pull. This is the large beefy type.", new short[] { 21, 44, 146 }, (short)60, (short)1, 0, 9072000L, 10, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.tackle.large.", 35.0F, 10000, (byte)14, 10000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5204 */     createItemTemplate(556, "oar", "oars", "fine", "nice", "mediocre", "poor", "This is a standard oar, about the length of a tall man.", new short[] { 21, 44, 146 }, (short)60, (short)1, 0, 9072000L, 5, 10, 190, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.oar.", 25.0F, 5000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5209 */     createItemTemplate(551, "tenon", "tenons", "fine", "nice", "mediocre", "poor", "These are used to link hull planks together.", new short[] { 21, 129, 146 }, (short)666, (short)1, 0, 28800L, 3, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.tenon.", 3.0F, 1000, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5214 */     createItemTemplate(552, "tall mast", "masts", "fine", "nice", "mediocre", "poor", "A tall mast for a rig.", new short[] { 21, 44 }, (short)646, (short)1, 0, 9072000L, 20, 20, 610, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.mast.tall.", 45.0F, 120000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5219 */     createItemTemplate(588, "small mast", "masts", "fine", "nice", "mediocre", "poor", "A rather small mast for a rig.", new short[] { 21, 44 }, (short)646, (short)1, 0, 9072000L, 20, 20, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.mast.small.", 45.0F, 60000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5224 */     createItemTemplate(589, "medium mast", "masts", "fine", "nice", "mediocre", "poor", "A standard mast for a rig.", new short[] { 21, 44 }, (short)646, (short)1, 0, 9072000L, 20, 20, 410, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.mast.medium.", 45.0F, 80000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5229 */     createItemTemplate(590, "large mast", "masts", "fine", "nice", "mediocre", "poor", "A larger type of mast for a rig.", new short[] { 21, 44 }, (short)646, (short)1, 0, 9072000L, 20, 20, 510, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.mast.large.", 45.0F, 100000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5235 */     createItemTemplate(553, "stern", "sterns", "fine", "nice", "mediocre", "poor", "The stern of a ship, to be fitted on the hull.", new short[] { 21, 44 }, (short)60, (short)1, 0, 9072000L, 10, 200, 310, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.stern.", 20.0F, 120000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5240 */     createItemTemplate(562, "keel", "keels", "fine", "nice", "mediocre", "poor", "Running all along the bottom this is the large beam around which the hull of a ship is built.", new short[] { 21, 44 }, (short)60, (short)1, 0, 9072000L, 10, 200, 410, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.keel.", 30.0F, 365000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5245 */     createItemTemplate(560, "keel section", "keel sections", "fine", "nice", "mediocre", "poor", "This is part of a keel. Put together several of these and you will have the keel of a ship.", new short[] { 21, 44, 146 }, (short)60, (short)1, 0, 9072000L, 10, 200, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.keel.part.", 30.0F, 60000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5251 */     createItemTemplate(554, "triangular sail", "sails", "excellent", "good", "ok", "poor", "When examined, this sails seems to be of triangular shape.", new short[] { 24, 44 }, (short)640, (short)1, 0, 3024000L, 15, 25, 25, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.sail.triangle.", 55.0F, 4300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5257 */     createItemTemplate(555, "square sail", "sails", "excellent", "good", "ok", "poor", "This sail seems to be square in shape.", new short[] { 24, 44 }, (short)640, (short)1, 0, 3024000L, 15, 25, 25, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.sail.square.", 65.0F, 5300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5262 */     createItemTemplate(591, "small square sail", "sails", "excellent", "good", "ok", "poor", "This sail seems to be small and square in shape.", new short[] { 24, 44 }, (short)640, (short)1, 0, 3024000L, 15, 15, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.sail.square.small.", 50.0F, 4300, (byte)17, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5269 */     createItemTemplate(564, "square rig", "square rigs", "fine", "nice", "mediocre", "poor", "A tall mast, with a square sail and cordage.", new short[] { 21 }, (short)60, (short)1, 0, 9072000L, 20, 20, 410, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.rig.square.", 35.0F, 86000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5274 */     createItemTemplate(563, "triangular rig", "triangular rigs", "fine", "nice", "mediocre", "poor", "A tall mast, with a triangular sail and cordage.", new short[] { 21 }, (short)60, (short)1, 0, 9072000L, 20, 20, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.rig.triangular.", 25.0F, 65000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5279 */     createItemTemplate(584, "spinnaker rig", "spinnaker rigs", "fine", "nice", "mediocre", "poor", "A small pointy mast with a small square sail and cordage.", new short[] { 21 }, (short)60, (short)1, 0, 9072000L, 20, 20, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.rig.spinnaker.", 30.0F, 66000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5284 */     createItemTemplate(585, "large square rig", "large square rigs", "fine", "nice", "mediocre", "poor", "A large mast with a square sail, cordage and a crows nest.", new short[] { 21 }, (short)60, (short)1, 0, 9072000L, 20, 20, 510, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.rig.square.large.", 45.0F, 106000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5289 */     createItemTemplate(586, "square yard rig", "square yard rigs", "fine", "nice", "mediocre", "poor", "A large sized mast with a large square sail and cordage.", new short[] { 21 }, (short)60, (short)1, 0, 9072000L, 20, 20, 510, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.rig.square.yard.", 55.0F, 106000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5294 */     createItemTemplate(587, "tall square rig", "tall square rigs", "fine", "nice", "mediocre", "poor", "A really tall mast with a square sail, cordage and a crows nest near the top.", new short[] { 21 }, (short)60, (short)1, 0, 9072000L, 20, 20, 610, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.rig.square.tall.", 65.0F, 166000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5301 */     createItemTemplate(580, "market stall", "stalls", "superb", "good", "ok", "poor", "A market stall made for the purpose of vending goods.", new short[] { 108, 21, 31, 51, 86, 123, 52, 44, 98, 49, 109 }, (short)60, (short)1, 0, 9072000L, 200, 250, 400, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.marketstall.", 25.0F, 250000, (byte)14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5308 */     createItemTemplate(581, "dredge", "dredges", "almost full", "somewhat occupied", "half-full", "emptyish", "A dirty sack with four metal blades along its rim. A rope is attached to it. The idea is that you drag it along the bottom and gather mud.", new short[] { 108, 1, 21, 33, 147, 52, 44, 92, 139, 125, 247 }, (short)265, (short)1, 0, 9072000L, 30, 30, 40, 1020, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.dredge.", 15.0F, 4500, (byte)14, 10000, true);
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
/* 5323 */     createItemTemplate(582, "dredge scraping lip", "dredge lips", "excellent", "good", "ok", "poor", "A hard metallic blade used to weight down a dredge and scrape the bottom.", new short[] { 44, 22, 146 }, (short)556, (short)1, 0, 3024000L, 1, 5, 25, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.blade.dredgelip.", 3.0F, 1000, (byte)9);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5329 */     createItemTemplate(583, "crows nest", "crows nests", "almost full", "somewhat occupied", "half-full", "emptyish", "The place where the lookout stands in the top of tall masts.", new short[] { 108, 21, 51, 52, 44 }, (short)276, (short)1, 0, 9072000L, 75, 75, 75, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.crowsnest.", 45.0F, 40000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5337 */     createItemTemplate(592, "mine door", "plank mine doors", "almost full", "somewhat occupied", "half-full", "emptyish", "Wooden planks joined as to cover a mine opening.", new short[] { 21, 126, 44, 147, 146, 157 }, (short)317, (short)1, 0, 9072000L, 5, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.minedoor.wood.", 5.0F, 42000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5345 */     createItemTemplate(593, "mine door", "stone mine doors", "almost full", "somewhat occupied", "half-full", "emptyish", "A door that looks very similar to the rock around here.", new short[] { 25, 44, 126, 157 }, (short)337, (short)1, 0, 12096000L, 5, 60, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.minedoor.stone.", 55.0F, 120000, (byte)15, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5352 */     createItemTemplate(594, "mine door", "golden mine doors", "almost full", "somewhat occupied", "half-full", "emptyish", "A shiny golden mine door.", new short[] { 22, 44, 126, 157 }, (short)257, (short)1, 0, 2419200L, 5, 60, 209, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.minedoor.gold.", 85.0F, 121000, (byte)7, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5359 */     createItemTemplate(595, "mine door", "silver mine doors", "almost full", "somewhat occupied", "half-full", "emptyish", "A door made from the purest silver.", new short[] { 22, 44, 126, 157 }, (short)277, (short)1, 0, 2419200L, 5, 60, 209, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.minedoor.silver.", 75.0F, 121000, (byte)8, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5366 */     createItemTemplate(596, "mine door", "steel mine doors", "almost full", "somewhat occupied", "half-full", "emptyish", "A strong steel door that will sustain a lot of damage.", new short[] { 22, 44, 126, 157 }, (short)297, (short)1, 0, 2419200L, 5, 60, 209, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.minedoor.steel.", 90.0F, 61000, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5373 */     createItemTemplate(597, "sheet", "steel sheets", "excellent", "good", "ok", "poor", "A thin strong steel sheet.", new short[] { 22, 146, 158, 157 }, (short)675, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 30.0F, 6000, (byte)9, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5379 */     createItemTemplate(681, "fence bars", "fence bars", "excellent", "good", "ok", "poor", "Fence bars wrought from crude metal.", new short[] { 22, 146, 158 }, (short)625, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.ironfencebars.", 30.0F, 6000, (byte)11, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5384 */     createItemTemplate(598, "sheet", "silver sheets", "excellent", "good", "ok", "poor", "A thick dark silver sheet.", new short[] { 22, 146, 158, 157 }, (short)676, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 30.0F, 6000, (byte)8, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5390 */     createItemTemplate(599, "sheet", "gold sheets", "excellent", "good", "ok", "poor", "A large thick glistening gold sheet.", new short[] { 22, 146, 158, 157 }, (short)674, (short)1, 0, 3024000L, 5, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.", 30.0F, 6000, (byte)7, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5396 */     createItemTemplate(600, 3, "summer hat", "hats", "excellent", "good", "ok", "poor", "A beautiful hat, glistening in the sun. It is made from a strange green fabric and decorated with flowers that never seem to wither.", new short[] { 24, 4, 42 }, (short)973, (short)1, 0, Long.MAX_VALUE, 5, 20, 20, -10, new byte[] { 1 }, "model.armour.summerhat.", 99.0F, 300, (byte)8, 50000, true);
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
/* 5410 */     createItemTemplate(601, "shaker orb", "shaker orbs", "brilliantly glowing", "strongly glowing", "faintly glowing", "barely glowing", "A round small glass orb with a faintly shining liquid. It is commonly used by miners to create cave-ins.", new short[] { 6, 53, 127 }, (short)60, (short)1, 0, Long.MAX_VALUE, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.magic.orb.shaker", 10.0F, 50, (byte)21, 50000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5417 */     createItemTemplate(678, "Fo obelisk", "obelisks", "almost full", "somewhat occupied", "half-full", "emptyish", "The obelisk emanates a certain degree of heat.", new short[] { 25, 49, 31, 52, 40, 149 }, (short)60, (short)1, 0, 3600L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.1.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5423 */     createItemTemplate(680, "Libila stone", "stones", "almost full", "somewhat occupied", "half-full", "emptyish", "The stone is freezing to the touch.", new short[] { 25, 49, 31, 52, 40, 150 }, (short)60, (short)1, 0, 3600L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.2.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5429 */     createItemTemplate(603, "monolith portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish", "A large stone portal stands here, inviting you through.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.1.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5436 */     createItemTemplate(604, "ring portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish", "A large portal stands here, inviting you through.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.2.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5443 */     createItemTemplate(605, "desolate portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish", "A large stone portal stands here, inviting you through.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.3.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5451 */     createItemTemplate(637, "freedom stones", "standing stones", "almost full", "somewhat occupied", "half-full", "emptyish", "Large stones with glowing red markings loom above you holding great promises.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.6.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5459 */     createItemTemplate(606, "flame portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish", "A large stone portal decorated with flames stands here, inviting you through.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.4.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5466 */     createItemTemplate(607, "portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish", "A large portal in the form of two large stones stands here, inviting you through.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.5.", 99.0F, 2000000, (byte)15, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5473 */     createItemTemplate(732, "epic portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish", "This rudimentary structure made from stone bricks and logs is rumoured to lead to far away lands.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67, 51, 86, 178 }, (short)60, (short)1, 0, 2419200L, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.8.", 1.0F, 2000000, (byte)15, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5483 */     createItemTemplate(733, "huge epic portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish", "This impressive structure made from both known and unknown materials apparently lead to the mysterious Epic cluster of islands.", new short[] { 25, 131, 49, 31, 52, 44, 48, 67, 51, 178 }, (short)60, (short)1, 0, Long.MAX_VALUE, 500, 500, 1000, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.7.", 1.0F, 2000000, (byte)15, 100, false);
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
/* 5499 */     createItemTemplate(608, "well", "wells", "almost full", "somewhat occupied", "half-full", "emptyish", "A grey stone well with a bucket.", new short[] { 108, 31, 25, 51, 52, 44, 67, 1, 33, 128, 86, 176, 199, 178, 259 }, (short)60, (short)1, 0, 12096000L, 50, 50, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.well.", 30.0F, 120000, (byte)15);
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
/* 5511 */     ItemTemplateCreatorContinued.initializeTemplates();
/* 5512 */     ItemTemplateCreatorCooking.initializeTemplates();
/* 5513 */     ItemTemplateCreatorFishing.initializeTemplates();
/* 5514 */     logger.info("Initialising the Item Templates took " + ((System.nanoTime() - start) / 1000000L) + " milliseconds");
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplateCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.shared.constants.ModelConstants;
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
/*      */ public class ItemTemplateCreatorCooking
/*      */   extends ItemTemplateCreator
/*      */   implements ModelConstants, ItemTypes
/*      */ {
/*   43 */   private static final Logger logger = Logger.getLogger(ItemTemplateCreator.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void initializeTemplates() throws IOException {
/*   56 */     createItemTemplate(1130, "mint", "mint", "excellent", "good", "ok", "poor", "A small but very aromatic plant with dark-green leaves that have serrated margin.", new short[] { 146, 5, 78, 103, 206, 55, 212, 221 }, (short)701, (short)1, 0, 28800L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.mint.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*   65 */       .setNutritionValues(700, 150, 9, 38)
/*   66 */       .setFoodGroup(1158);
/*      */     
/*   68 */     createItemTemplate(1131, "fennel", "fennel", "excellent", "good", "ok", "poor", "A highly aromatic and flavorful herb with yellow flowers and feathery leaves.", new short[] { 146, 5, 78, 103, 55, 212, 217 }, (short)569, (short)1, 0, 28800L, 3, 3, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.fennel.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*   77 */       .setNutritionValues(310, 7, 2, 12)
/*   78 */       .setFoodGroup(1158);
/*      */ 
/*      */     
/*   81 */     createItemTemplate(1132, "fennel plant", "fennel plants", "excellent", "good", "ok", "poor", "A highly aromatic and flavorful herb with yellow flowers and feathery leaves.", new short[] { 146, 5, 78, 212, 221 }, (short)569, (short)16, 0, 28800L, 5, 5, 15, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.fennelplant.", 100.0F, 300, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*   89 */       .setCrushsTo(1131)
/*   90 */       .setPickSeeds(1151);
/*      */ 
/*      */     
/*   93 */     createItemTemplate(1133, "carrot", "carrots", "excellent", "good", "ok", "poor", "An orange vegetable that some say helps you see in the dark.", new short[] { 146, 5, 55, 29, 212, 223 }, (short)714, (short)16, 0, 28800L, 3, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.carrot.", 1.0F, 500, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  102 */       .setNutritionValues(410, 96, 2, 9)
/*  103 */       .setPickSeeds(1145)
/*  104 */       .setFoodGroup(1156);
/*      */     
/*  106 */     createItemTemplate(1134, "cabbage", "cabbages", "excellent", "good", "ok", "poor", "A leafy green vegetable, grown for their dence leaved heads.", new short[] { 146, 5, 55, 29, 212, 223 }, (short)715, (short)16, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cabbage.", 1.0F, 1000, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  115 */       .setNutritionValues(250, 50, 3, 19)
/*  116 */       .setPickSeeds(1146)
/*  117 */       .setFoodGroup(1156);
/*      */     
/*  119 */     createItemTemplate(1135, "tomato", "tomatoes", "excellent", "good", "ok", "poor", "An edible red berry-type fruit of the nightshade family.", new short[] { 146, 5, 55, 29, 212, 223 }, (short)716, (short)16, 0, 28800L, 4, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.tomato.", 1.0F, 500, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  128 */       .setNutritionValues(180, 39, 2, 9)
/*  129 */       .setPickSeeds(1147)
/*  130 */       .setFoodGroup(1156);
/*      */     
/*  132 */     createItemTemplate(1136, "sugar beet", "sugar beets", "excellent", "good", "ok", "poor", "A plant whose root contains a high concentration of sucrose.", new short[] { 146, 5, 55, 212 }, (short)717, (short)16, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.sugarbeet.", 1.0F, 1000, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  140 */       .setCrushsTo(1139)
/*  141 */       .setPickSeeds(1148);
/*      */     
/*  143 */     createItemTemplate(1137, "lettuce", "lettuces", "excellent", "good", "ok", "poor", "A leaf vegetable of the daisy family Asteraceae.", new short[] { 146, 5, 55, 29, 212, 223 }, (short)718, (short)16, 0, 28800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.lettuce.", 1.0F, 500, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  152 */       .setNutritionValues(130, 22, 2, 14)
/*  153 */       .setPickSeeds(1149)
/*  154 */       .setFoodGroup(1156);
/*      */     
/*  156 */     createItemTemplate(1138, "pea pod", "pea pods", "excellent", "good", "ok", "poor", "A seed-pod of the pod fruit Pisum sativum, can be used in salads or cooked.", new short[] { 146, 5, 55, 29, 212, 223 }, (short)719, (short)16, 0, 28800L, 2, 4, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.peapod.", 1.0F, 500, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  165 */       .setNutritionValues(420, 74, 2, 27)
/*  166 */       .setPickSeeds(1150)
/*  167 */       .setFoodGroup(1156);
/*      */     
/*  169 */     createItemTemplate(1139, "sugar", "sugar", "excellent", "good", "ok", "poor", "A sweet, short-chain, soluble carbohydrate.", new short[] { 146, 137, 5 }, (short)642, (short)1, 0, 28800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.sugar.", 1.0F, 100, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  177 */       .setNutritionValues(3870, 1000, 0, 0);
/*      */     
/*  179 */     createItemTemplate(1247, "cucumber", "cucumbers", "excellent", "good", "ok", "poor", "A widely cultivated plant in the gourd family.", new short[] { 146, 55, 5, 29, 212, 223 }, (short)684, (short)16, 0, 28800L, 3, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cucumber.", 1.0F, 500, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  188 */       .setNutritionValues(160, 36, 1, 7)
/*  189 */       .setPickSeeds(1248)
/*  190 */       .setFoodGroup(1156);
/*      */ 
/*      */     
/*  193 */     createItemTemplate(1140, "cumin", "cumins", "excellent", "good", "ok", "poor", "A dried seed from the fruit of a flowering plant in the family of Aplaceae.", new short[] { 206, 55, 146, 5, 205, 212, 221 }, (short)694, (short)1, 0, 28800L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.spice.cumin.", 1.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  202 */       .setNutritionValues(3750, 440, 220, 180)
/*  203 */       .setFoodGroup(1159);
/*      */     
/*  205 */     createItemTemplate(1141, "ginger", "ginger", "excellent", "good", "ok", "poor", "A hot, fragrant kitchen spice.", new short[] { 206, 55, 146, 5, 205, 212, 221 }, (short)696, (short)16, 0, 28800L, 3, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.spice.ginger.", 1.0F, 100, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  214 */       .setNutritionValues(800, 180, 8, 18)
/*  215 */       .setFoodGroup(1159);
/*      */     
/*  217 */     createItemTemplate(1142, "nutmeg", "nutmeg", "excellent", "good", "ok", "poor", "A seed of a tree in the genus Myristica, how it got here is anyone guess.", new short[] { 146, 55, 5, 205, 105, 212, 217 }, (short)697, (short)1, 0, 28800L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.spice.nutmeg.", 5.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  226 */       .setNutritionValues(5250, 490, 360, 60)
/*  227 */       .setFoodGroup(1159);
/*      */     
/*  229 */     createItemTemplate(1143, "paprika", "paprika", "excellent", "good", "ok", "poor", "Air-dried fruits of the chili pepper family of the species Capsicum annuum.", new short[] { 146, 55, 5, 205, 102, 212, 221 }, (short)698, (short)16, 0, 28800L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.spice.paprika.", 4.0F, 100, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  238 */       .setNutritionValues(2820, 540, 130, 140)
/*  239 */       .setPickSeeds(1153)
/*  240 */       .setFoodGroup(1159);
/*      */     
/*  242 */     createItemTemplate(1144, "turmeric", "turmeric", "excellent", "good", "ok", "poor", "A rhizomatous herbaceous perennial plant of the ginger family.", new short[] { 146, 55, 5, 205, 103, 212, 221 }, (short)699, (short)16, 0, 28800L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.spice.turmeric.", 3.0F, 100, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  251 */       .setNutritionValues(3540, 650, 100, 80)
/*  252 */       .setPickSeeds(1154)
/*  253 */       .setFoodGroup(1159);
/*      */ 
/*      */     
/*  256 */     createItemTemplate(1145, "carrot seed", "carrot seeds", "excellent", "good", "ok", "poor", "The seed from a carrot.", new short[] { 20, 55, 146, 5 }, (short)480, (short)1, 0, 28800L, 1, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.carrotseeds.", 100.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  265 */     createItemTemplate(1146, "cabbage seed", "cabbage seeds", "excellent", "good", "ok", "poor", "The seed from a cabbage.", new short[] { 20, 55, 146, 5 }, (short)480, (short)1, 0, 28800L, 1, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.cabbageseeds.", 100.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  274 */     createItemTemplate(1147, "tomato seed", "tomato seeds", "excellent", "good", "ok", "poor", "The seed from a tomato.", new short[] { 20, 55, 146, 5 }, (short)480, (short)1, 0, 28800L, 1, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.tomatoseeds.", 100.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  283 */     createItemTemplate(1148, "sugar beet seed", "sugar beet seeds", "excellent", "good", "ok", "poor", "The seed from a sugar beet.", new short[] { 20, 55, 146, 5 }, (short)480, (short)1, 0, 28800L, 1, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sugarbeetseeds.", 100.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  292 */     createItemTemplate(1149, "lettuce seed", "lettuce seeds", "excellent", "good", "ok", "poor", "The seed from a lettuce.", new short[] { 20, 55, 146, 5 }, (short)480, (short)1, 0, 28800L, 1, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.lettuceseeds.", 100.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  301 */     createItemTemplate(1150, "pea", "peas", "excellent", "good", "ok", "poor", "A small spherical seed of the pod fruit Pisum sativum.", new short[] { 20, 55, 146, 5, 29, 212, 223 }, (short)683, (short)1, 0, 28800L, 2, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.peas.", 1.0F, 100, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  310 */       .setNutritionValues(810, 140, 4, 50)
/*  311 */       .setFoodGroup(1156);
/*      */     
/*  313 */     createItemTemplate(1151, "fennel seed", "fennel seeds", "excellent", "good", "ok", "poor", "A seed from a fennel plant.", new short[] { 206, 55, 146, 5, 205, 212 }, (short)480, (short)1, 0, 28800L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.fennelseeds.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  322 */       .setGrows(1132)
/*  323 */       .setNutritionValues(810, 140, 4, 50)
/*  324 */       .setFoodGroup(1159);
/*      */     
/*  326 */     createItemTemplate(1153, "paprika seed", "paprika seeds", "excellent", "good", "ok", "poor", "A seed from air-dried fruits of the chili pepper family of the species Capsicum annuum.", new short[] { 206, 55, 146, 5, 205, 212 }, (short)480, (short)1, 0, 28800L, 1, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.paprikaseeds.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  335 */       .setGrows(1143);
/*      */     
/*  337 */     createItemTemplate(1154, "turmeric seed", "turmeric seeds", "excellent", "good", "ok", "poor", "A seed from a rhizomatous herbaceous perennial plant of the ginger family.", new short[] { 206, 55, 146, 5, 205, 212 }, (short)480, (short)1, 0, 28800L, 1, 2, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.turmericseeds.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  346 */       .setGrows(1144);
/*      */     
/*  348 */     createItemTemplate(1155, "cocoa bean", "cocoa beans", "excellent", "good", "ok", "poor", "The dried and fully fermented fatty seed of Theobroma cacao. Wonder how it got here.", new short[] { 146, 55, 5, 212 }, (short)518, (short)1, 0, 28800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.cocoabean.", 8.0F, 200, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  357 */     createItemTemplate(1248, "cucumber seed", "cucumber seeds", "excellent", "good", "ok", "poor", "The seed from a cucumber.", new short[] { 20, 55, 146, 5 }, (short)480, (short)1, 0, 28800L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.cucumberseeds.", 100.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  367 */     createItemTemplate(1160, "clay planter", "clay planters", "excellent", "good", "ok", "poor", "A clay planter that could be hardened by fire.", new short[] { 108, 196, 44, 147 }, (short)490, (short)1, 0, 172800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.planter.", 15.0F, 600, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  376 */     createItemTemplate(1161, "pottery planter", "pottery planters", "excellent", "good", "ok", "poor", "A clay planter hardened by fire.", new short[] { 108, 30, 52, 51, 199 }, (short)510, (short)55, 0, 12096000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.planter.", 15.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  386 */     createItemTemplate(1162, "pottery planter", "pottery planters", "excellent", "good", "ok", "poor", "A planter with something growing in it.", new short[] { 108, 30, 52, 51, 233, 199 }, (short)510, (short)55, 0, 12096000L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.planter.", 15.0F, 600, (byte)19, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  397 */     createItemTemplate(1164, "clay pie dish", "clay pie dishes", "excellent", "good", "ok", "poor", "A clay pie dish that could be hardened by fire.", new short[] { 108, 196, 44, 147, 1, 63 }, (short)491, (short)1, 0, 172800L, 10, 20, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.piedish.", 15.0F, 600, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  407 */     createItemTemplate(1165, "pie dish", "pie dishes", "excellent", "good", "ok", "poor", "A clay pie dish hardened by fire.", new short[] { 108, 30, 1, 77, 33, 231 }, (short)511, (short)1, 0, 12096000L, 10, 20, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.piedish.", 15.0F, 600, (byte)19, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  417 */     createItemTemplate(1166, "cake tin", "cake tins", "excellent", "good", "ok", "poor", "A circular tin used to make cakes in.", new short[] { 108, 44, 22, 1, 33, 77, 231 }, (short)263, (short)1, 0, 3024000L, 10, 20, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.caketin.", 15.0F, 600, (byte)34, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  427 */     createItemTemplate(1167, "baking stone", "baking stones", "excellent", "good", "ok", "poor", "A flat stone surface used for baking food.", new short[] { 108, 44, 25, 1, 33, 77, 231 }, (short)285, (short)1, 0, 12096000L, 15, 25, 35, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.stoneware.", 15.0F, 2000, (byte)15, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  437 */     createItemTemplate(1168, "clay roasting dish", "clay roasting dishs", "excellent", "good", "ok", "poor", "A clay roasting dish that could be hardened by fire.", new short[] { 108, 196, 44, 147, 1, 63 }, (short)264, (short)1, 0, 172800L, 10, 20, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.roastingdish.", 15.0F, 600, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  447 */     createItemTemplate(1169, "roasting dish", "roasting dishs", "excellent", "good", "ok", "poor", "A clay roasting dish hardened by fire.", new short[] { 108, 30, 1, 77, 33, 231 }, (short)284, (short)1, 0, 12096000L, 10, 20, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.roastingdish.", 15.0F, 600, (byte)19, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  457 */     createItemTemplate(1171, "clay measuring jug", "clay measuring jugs", "excellent", "good", "ok", "poor", "A kitchen utensil used primarily to measure the volume of liquid ingredients such as milk.", new short[] { 108, 196, 44, 147, 1, 63 }, (short)294, (short)1, 0, 172800L, 4, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.measuringjug.", 15.0F, 300, (byte)18, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  467 */     createItemTemplate(1172, "measuring jug", "measuring jugs", "excellent", "good", "ok", "poor", "A kitchen utensil used primarily to measure the volume of liquid ingredients such as milk.", new short[] { 108, 30, 1, 33, 180 }, (short)314, (short)1, 0, 12096000L, 4, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.measuringjug.", 15.0F, 300, (byte)19, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  476 */       .setContainerSize(10, 10, 100);
/*      */     
/*  478 */     createItemTemplate(1173, "plate", "plates", "excellent", "good", "ok", "poor", "A flat and usually round dish that is used for eating or serving food.", new short[] { 108, 21, 1, 77, 44, 92, 33, 231 }, (short)295, (short)1, 0, 9072000L, 10, 10, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.plate.", 1.0F, 250, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  488 */     createItemTemplate(1175, "bee hive", "bee hives", "excellent", "good", "ok", "poor", "An enclosed structure in which some honey bee species of the subgenus Apis live and raise their young.", new short[] { 108, 21, 1, 176, 63, 135, 52, 49, 225, 47, 199, 109, 44, 51 }, (short)287, (short)1, 0, 9072000L, 120, 120, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.hive.", 15.0F, 60000, (byte)14, 10000, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  499 */     createItemTemplate(1239, "wild bee hive", "wild bee hives", "excellent", "good", "ok", "poor", "A structure, built into a tree, in which some honey bee species of the subgenus Apis live and raise their young.", new short[] { 108, 21, 1, 31, 123, 52, 109, 218, 63 }, (short)287, (short)1, 0, 9072000L, 120, 120, 150, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.hive.wild.", 15.0F, 2000, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  509 */     createItemTemplate(1178, "still", "stills", "excellent", "good", "ok", "poor", "A still used in distilling alcohol.", new short[] { 108, 22, 147, 209, 52, 67, 59, 1, 44, 178, 33, 199, 180, 112, 259 }, (short)290, (short)18, 0, 3024000L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.still.", 45.0F, 50000, (byte)10, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  520 */       .setContainerSize(0, 0, 0)
/*  521 */       .setInitialContainers(new InitialContainer[] { new InitialContainer(1284, "boiler"), new InitialContainer(1285, "condenser") });
/*      */ 
/*      */ 
/*      */     
/*  525 */     createItemTemplate(1180, "mead", "mead", "excellent", "good", "ok", "poor", "An alcoholic beverage created by fermenting honey with water.", new short[] { 108, 26, 88, 90, 212, 213 }, (short)608, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.mead.", 15.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  534 */       .setAlcoholStrength(6);
/*      */     
/*  536 */     createItemTemplate(1181, "cider", "cider", "excellent", "good", "ok", "poor", "An alcoholic beverage created by scratting (grinding down), and then fermenting apples.", new short[] { 108, 26, 88, 90, 212, 213 }, (short)550, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.cider.", 20.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  545 */       .setAlcoholStrength(8);
/*      */     
/*  547 */     createItemTemplate(1182, "beer", "beer", "excellent", "good", "ok", "poor", "An alcoholic beverage created by brewing a mixture of barley, hops and water.", new short[] { 108, 26, 88, 90, 212, 213, 233 }, (short)568, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.beer.", 25.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  556 */       .setAlcoholStrength(4);
/*      */     
/*  558 */     createItemTemplate(1183, "whisky", "whisky", "excellent", "good", "ok", "poor", "An distilled alcoholic beverage made from fermented rye mash.", new short[] { 108, 26, 88, 90, 213, 212, 214, 89, 233 }, (short)568, (short)1, 0, 604800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.whisky.", 35.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  568 */       .setAlcoholStrength(35);
/*      */ 
/*      */     
/*  571 */     createItemTemplate(1184, "pinenut", "pinenuts", "excellent", "good", "ok", "poor", "The edible seeds of pines (family Pinaceae, genus Pinus).", new short[] { 5, 55, 146, 80, 212, 217 }, (short)519, (short)1, 0, 86400L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pinenuts.", 15.0F, 200, (byte)22, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  580 */       .setNutritionValues(6730, 131, 684, 137)
/*  581 */       .setFoodGroup(1197);
/*      */     
/*  583 */     createItemTemplate(1185, "chocolate", "chocolate", "excellent", "good", "ok", "poor", "A typically sweet, usually brown, food preparation of Theobroma cacao seeds.", new short[] { 82, 75, 146, 220, 222 }, (short)624, (short)1, 0, 172800L, 2, 3, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.chocolate.", 15.0F, 600, (byte)22, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  592 */       .setNutritionValues(5400, 594, 297, 76);
/*      */     
/*  594 */     createItemTemplate(1186, "butter", "butter", "excellent", "good", "ok", "poor", "A solid dairy product made by churning fresh milk.", new short[] { 82, 55, 222, 233 }, (short)609, (short)1, 0, 86400L, 2, 3, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.butter.", 15.0F, 250, (byte)28, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  603 */       .setNutritionValues(7170, 1, 810, 9);
/*      */     
/*  605 */     createItemTemplate(1187, "omelette", "omelette", "excellent", "good", "ok", "poor", "A dish made from beaten eggs quickly cooked with butter or oil in a frying pan.", new short[] { 82, 76, 219, 222, 233 }, (short)649, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.omelette.", 15.0F, 600, (byte)28, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  615 */     createItemTemplate(1188, "curry", "curry", "excellent", "good", "ok", "poor", "A dish that has a complex combinations of spices or herbs and includes a sauce.", new short[] { 82, 76, 219, 222, 233 }, (short)539, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.curry.", 15.0F, 600, (byte)2, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  625 */     createItemTemplate(1189, "salad", "salad", "excellent", "good", "ok", "poor", "A dish consisting of small pieces of food, served cold and will always have lettuce.", new short[] { 82, 212, 76, 220, 222, 233 }, (short)688, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.salad.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  635 */     createItemTemplate(1190, "sausage", "sausages", "excellent", "good", "ok", "poor", "A food usually made from ground meat, often pork, beef or veal, along with salt, spices and breadcrumbs, with a skin around it.", new short[] { 82, 212, 146, 74, 219, 222, 233 }, (short)695, (short)1, 0, 604800L, 2, 6, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.sausage.", 15.0F, 600, (byte)2, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  645 */     createItemTemplate(1191, "bacon", "bacon", "excellent", "good", "ok", "poor", "A meat product prepared from a pig.", new short[] { 82, 146, 212, 74, 219, 222, 228 }, (short)663, (short)1, 0, 604800L, 2, 6, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.bacon.", 15.0F, 300, (byte)2, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  656 */     createItemTemplate(1193, "cooking oil", "cooking oils", "excellent", "good", "ok", "poor", "Used as a cooking oil.", new short[] { 26, 88, 34, 158, 113, 233 }, (short)587, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.oil.", 15.0F, 180, (byte)25, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  665 */       .setFoodGroup(1263);
/*      */     
/*  667 */     createItemTemplate(1194, "gravy", "gravy", "excellent", "good", "ok", "poor", "A sauce, made often from the juices that run naturally during cooking and often thickened with wheat flour or corn flour for added texture.", new short[] { 26, 88, 74, 219, 212 }, (short)568, (short)1, 0, 86400L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.gravy.", 15.0F, 600, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  676 */       .setNutritionValues(790, 50, 60, 19);
/*      */     
/*  678 */     createItemTemplate(1195, "custard", "custard", "excellent", "good", "ok", "poor", "A variety of culinary preparations based on a cooked mixture of milk or cream and egg yolk.", new short[] { 5, 26, 88, 74, 108 }, (short)608, (short)1, 0, 86400L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.custard.", 15.0F, 600, (byte)28, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  686 */       .setNutritionValues(1220, 180, 40, 40);
/*      */     
/*  688 */     createItemTemplate(1196, "raspberries", "handfuls of raspberries", "excellent", "good", "ok", "poor", "The edible fruit of a multitude of plant species in the genus Rubus of the rose family.", new short[] { 5, 146, 80, 46, 212, 55 }, (short)677, (short)16, 0, 86400L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fruit.raspberries.", 100.0F, 300, (byte)22, 10, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  696 */       .setNutritionValues(530, 120, 7, 12)
/*  697 */       .setFoodGroup(1179);
/*      */     
/*  699 */     createItemTemplate(1179, "any berry", "any berry", "excellent", "good", "ok", "poor", "Any berry.", new short[] { 207, 208, 5, 80 }, (short)489, (short)1, 0, 172800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fruit.berries.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  708 */     createItemTemplate(1157, "any cereal", "any cereal", "excellent", "good", "ok", "poor", "Any cereal.", new short[] { 207, 208, 5 }, (short)480, (short)1, 0, 172800L, 1, 3, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.cereal.", 15.0F, 600, (byte)4, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  717 */     createItemTemplate(1198, "any cheese", "any cheese", "excellent", "good", "ok", "poor", "Any cheese.", new short[] { 207, 208, 5, 192, 224 }, (short)561, (short)1, 0, 604800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cheese.", 15.0F, 600, (byte)28, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  727 */     createItemTemplate(1201, "any fish", "any fish", "excellent", "good", "ok", "poor", "Any fish.", new short[] { 207, 208, 5, 36, 223 }, (short)504, (short)1, 0, 172800L, 5, 10, 50, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.", 15.0F, 600, (byte)2, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  736 */     createItemTemplate(1163, "any fruit", "any fruit", "excellent", "good", "ok", "poor", "Any fruit.", new short[] { 207, 208, 5, 80 }, (short)508, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.fruit.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  745 */     createItemTemplate(1158, "any herb", "any herb", "excellent", "good", "ok", "poor", "Any herb.", new short[] { 207, 208, 5, 78 }, (short)627, (short)1, 0, 172800L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.herb.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  754 */     createItemTemplate(1200, "any milk", "any milk", "excellent", "good", "ok", "poor", "Any milk.", new short[] { 207, 208, 26, 88, 191 }, (short)541, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 15.0F, 600, (byte)28, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  763 */     createItemTemplate(1199, "any mushroom", "any mushroom", "excellent", "good", "ok", "poor", "Any mushroom.", new short[] { 207, 208, 5, 226 }, (short)586, (short)1, 0, 172800L, 3, 4, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  772 */     createItemTemplate(1197, "any nut", "any nut", "excellent", "good", "ok", "poor", "Any nut.", new short[] { 207, 208, 5 }, (short)507, (short)1, 0, 604800L, 4, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.nut.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  781 */     createItemTemplate(1159, "any spice", "any spice", "excellent", "good", "ok", "poor", "Any spice.", new short[] { 207, 208, 5, 205 }, (short)694, (short)1, 0, 604800L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.spice.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  790 */     createItemTemplate(1156, "any veg", "any veg", "excellent", "good", "ok", "poor", "Any veg.", new short[] { 207, 208, 5, 29, 223 }, (short)502, (short)1, 0, 172800L, 2, 4, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.veg.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  800 */     createItemTemplate(1202, "biscuit", "biscuits", "excellent", "good", "ok", "poor", "A nutritious, easy-to-store, easy-to-carry, and long-lasting food item.", new short[] { 82, 129, 212, 76, 220, 222, 233 }, (short)532, (short)1, 0, 1209600L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.biscuit.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  810 */     createItemTemplate(1203, "fries", "fries", "excellent", "good", "ok", "poor", "Potatos thick cut julienne.", new short[] { 82, 129, 212, 74, 219, 222, 228 }, (short)685, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.fries.", 15.0F, 100, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  820 */     createItemTemplate(1204, "gelatine", "gelatine", "excellent", "good", "ok", "poor", "A translucent, colorless, flavorless food derived from collagen obtained from various animal by-products.", new short[] { 129, 137, 108, 26, 88 }, (short)587, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.gelatine.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  829 */     createItemTemplate(1205, "honey water", "honey water", "excellent", "good", "ok", "poor", "A mixture of honey and water.", new short[] { 129, 26, 213, 108, 88, 212 }, (short)587, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.honey.water.", 15.0F, 1000, (byte)29, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  839 */     createItemTemplate(1206, "passata", "passata", "excellent", "good", "ok", "poor", "A liquid tomato puree.", new short[] { 129, 212, 26, 108, 88, 55 }, (short)582, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.passata.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  849 */     createItemTemplate(1207, "pasta", "pasta", "excellent", "good", "ok", "poor", "Unleavened dough, made with wheat, water, and sometimes eggs.", new short[] { 82, 129, 212, 76, 219, 222, 233 }, (short)659, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pasta.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  859 */     createItemTemplate(1208, "pastry", "pastry", "excellent", "good", "ok", "poor", "Dough or paste consisting primarily of flour, water, and shortening that is baked and often used as a crust for foods such as pies and tarts.", new short[] { 82, 129, 212, 75, 222 }, (short)664, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pastry.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  869 */     createItemTemplate(1209, "pesto", "pesto", "excellent", "good", "ok", "poor", "A sauce made with oil, garlic, nuts, and basil.", new short[] { 129, 212, 26, 88, 55 }, (short)581, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pesto.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  879 */     createItemTemplate(1210, "stock", "stock", "excellent", "good", "ok", "poor", "A boiled down liquid from animal parts.", new short[] { 129, 55, 26, 88, 212 }, (short)568, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.stock.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  889 */     createItemTemplate(1211, "tomato ketchup", "tomato ketchup", "excellent", "good", "ok", "poor", "Used as a condiment with various dishes that are usually served hot.", new short[] { 82, 129, 74, 222 }, (short)582, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.ketchup.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  898 */     createItemTemplate(1212, "white sauce", "white sauce", "excellent", "good", "ok", "poor", "Plain white sauce.", new short[] { 129, 212, 55, 26, 88, 108, 233, 82 }, (short)584, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.whitesauce.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  908 */     createItemTemplate(1213, "wort", "wort", "excellent", "good", "ok", "poor", "Non-fermented beer.", new short[] { 129, 26, 213, 108, 233, 212 }, (short)587, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.wort.", 15.0F, 600, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  918 */     createItemTemplate(1214, "rat-on-a-stick", "rat-on-a-stick", "excellent", "good", "ok", "poor", "A rat on a stick that can be cooked over an open fire.", new short[] { 82, 129, 212, 76, 219, 222, 228 }, (short)687, (short)1, 0, 172800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.spit.rat.", 15.0F, 600, (byte)78, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  928 */     createItemTemplate(1215, "hog roast", "hog roast", "excellent", "good", "ok", "poor", "A pig that can be cooked over an open fire on a spit.", new short[] { 82, 129, 212, 76, 219, 222, 228, 52, 237 }, (short)597, (short)1, 0, 172800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.spit.pig.", 15.0F, 600, (byte)84, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  938 */     createItemTemplate(1216, "lamb spit", "lamb spit", "excellent", "good", "ok", "poor", "A lamb that can be cooked over an open fire on a spit.", new short[] { 82, 129, 212, 76, 219, 222, 228, 52, 237 }, (short)629, (short)1, 0, 172800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.spit.lamb.", 15.0F, 600, (byte)83, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  948 */     createItemTemplate(1152, "cocoa", "cocoa", "excellent", "good", "ok", "poor", "Ground cocoa bean.", new short[] { 5, 129, 212, 55 }, (short)518, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cocoa.", 15.0F, 200, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  957 */     createItemTemplate(1177, "pie", "pies", "excellent", "good", "ok", "poor", "A pastry pie with a filling.", new short[] { 82, 129, 76, 219, 220, 222, 233 }, (short)668, (short)1, 0, 604800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pie.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  967 */     createItemTemplate(1258, "tart", "tarts", "excellent", "good", "ok", "poor", "A pastry tart with a filling.", new short[] { 82, 129, 76, 219, 220, 222, 233 }, (short)1442, (short)1, 0, 604800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.tart.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  977 */     createItemTemplate(1192, "corn dough", "corn dough", "excellent", "good", "ok", "poor", "Sticky dough, made from corn flour and water.", new short[] { 5, 146, 129, 74 }, (short)525, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.dough.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  986 */     createItemTemplate(1217, "mushy peas", "mushy peas", "excellent", "good", "ok", "poor", "Mashed peas with a hint of mint.", new short[] { 5, 129, 212, 55, 108 }, (short)683, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.peas.mushy.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  995 */     createItemTemplate(1218, "croutons", "croutons", "excellent", "good", "ok", "poor", "A piece of cubed toast, that is used to add texture and flavor to salads and soups.", new short[] { 5, 129, 74, 108 }, (short)505, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bread.croutons.", 15.0F, 100, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1004 */     createItemTemplate(1219, "haggis", "haggis", "excellent", "good", "ok", "poor", "A savoury pudding containing sheep's pluck, onion, oats and spices.", new short[] { 82, 129, 75, 219, 222 }, (short)578, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.haggis.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1014 */     createItemTemplate(1220, "cornflour", "cornflour", "excellent", "good", "ok", "poor", "Ground corn, used in thickening sauces or soups.", new short[] { 5, 146, 129, 55 }, (short)642, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.flour.", 5.0F, 100, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1023 */     createItemTemplate(1221, "cheesecake", "cheesecake", "excellent", "good", "ok", "poor", "A sweet dessert consisting of one or more layers.", new short[] { 82, 129, 76, 220, 222, 233 }, (short)533, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cheesecake.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1033 */     createItemTemplate(1222, "kielbasa", "kielbasa", "excellent", "good", "ok", "poor", "Polish sausage.", new short[] { 82, 129, 76, 219, 222 }, (short)695, (short)1, 0, 604800L, 3, 4, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.kielbasa.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1043 */     createItemTemplate(1223, "mushroom", "mushroom", "excellent", "good", "ok", "poor", "A mushroom ready for stuffing.", new short[] { 1, 129, 5, 82, 77, 74, 233 }, (short)546, (short)1, 0, 172800L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.stuffed.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1053 */     createItemTemplate(1224, "stuffed mushroom", "stuffed mushroom", "excellent", "good", "ok", "poor", "A stuffing mushroom.", new short[] { 82, 129, 74, 219, 222 }, (short)506, (short)1, 0, 172800L, 3, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.mushroom.stuffed.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1063 */     createItemTemplate(1225, "crisps", "crisps", "excellent", "good", "ok", "poor", "A thin slice of vegatable, normally potato, that has been deep fried.", new short[] { 82, 129, 76, 220, 222 }, (short)537, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.crisps.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1073 */     createItemTemplate(1226, "jelly", "jelly", "excellent", "good", "ok", "poor", "A clear and sparkling with a fresh flavor of the fruit from which it is made. It is tender enough to quiver when moved.", new short[] { 82, 129, 55, 220, 222, 233 }, (short)628, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.jelly.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1083 */     createItemTemplate(1227, "scone", "scones", "excellent", "good", "ok", "poor", "A single-serving cake or quick bread.", new short[] { 82, 129, 76, 220, 222, 233 }, (short)1440, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.scone.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1093 */     createItemTemplate(1228, "toast", "toast", "excellent", "good", "ok", "poor", "A slice of bread that has been browned by exposure to radiant heat.", new short[] { 82, 129, 74, 222, 233 }, (short)1444, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bread.toast.", 15.0F, 100, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1103 */     createItemTemplate(1229, "pasty", "pasty", "excellent", "good", "ok", "poor", "Handy travel food.", new short[] { 82, 129, 76, 219, 220, 222, 233 }, (short)665, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pasty.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1113 */     createItemTemplate(1230, "chocolate nut spread", "chocolate nut spread", "excellent", "good", "ok", "poor", "A spread made with chocolate and hazelnuts.", new short[] { 5, 129, 74, 108 }, (short)624, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.chocalate.spread.", 25.0F, 600, (byte)22, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1121 */       .setNutritionValues(6000, 690, 330, 60);
/*      */     
/* 1123 */     createItemTemplate(1231, "vodka", "vodka", "excellent", "good", "ok", "poor", "A distilled beverage composed primarily of water and potato.", new short[] { 108, 26, 88, 90, 213, 212, 214, 89 }, (short)551, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.vodka.", 30.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1132 */       .setAlcoholStrength(30);
/*      */     
/* 1134 */     createItemTemplate(1232, "brandy", "brandy", "excellent", "good", "ok", "poor", "A spirit produced by distilling wine.", new short[] { 108, 26, 88, 90, 212, 214, 89, 233 }, (short)568, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.brandy.", 35.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1143 */       .setAlcoholStrength(35);
/*      */     
/* 1145 */     createItemTemplate(1233, "moonshine", "moonshine", "excellent", "good", "ok", "poor", "A high-proof distilled spirit.", new short[] { 108, 26, 88, 90, 213, 212, 214, 89 }, (short)551, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.moonshine.", 40.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1154 */       .setAlcoholStrength(40);
/*      */     
/* 1156 */     createItemTemplate(1234, "gin", "gin", "excellent", "good", "ok", "poor", "A spirit which derives its predominant flavour from wheat.", new short[] { 108, 26, 88, 90, 213, 212, 214, 89, 233 }, (short)551, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.gin.", 20.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1165 */       .setAlcoholStrength(20);
/*      */     
/* 1167 */     createItemTemplate(1235, "pineapple", "pineapples", "excellent", "good", "ok", "poor", "Normally a tropical edible fruit consisting of coalesced berries, but this one is a mixture of apple and pinenuts!", new short[] { 146, 5, 80, 212, 55 }, (short)669, (short)1, 0, 28800L, 7, 7, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pineapple.", 15.0F, 500, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1175 */       .setNutritionValues(410, 96, 2, 9);
/*      */     
/* 1177 */     createItemTemplate(1236, "sausage skin", "sausage skins", "excellent", "good", "ok", "poor", "A skin to hold the ingredients for a sausage in, made from a bladder.", new short[] { 5, 1, 77, 55 }, (short)695, (short)1, 0, 86400L, 6, 6, 13, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.sausage.skin.", 20.0F, 10, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1186 */     createItemTemplate(1237, "mortar and pestle", "mortar and pestle", "excellent", "good", "ok", "poor", "A device used to prepare ingredients or substances by crushing and grinding them into a fine paste or powder.", new short[] { 25, 210, 44 }, (short)756, (short)1, 0, 12096000L, 2, 4, 6, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.mortarandpestle.", 50.0F, 100, (byte)62, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1195 */     createItemTemplate(1238, "rock salt", "rock salts", "superb", "good", "ok", "poor", "Lots of different sized rock salts.", new short[] { 25, 146, 112, 113, 129, 48, 175, 211 }, (short)610, (short)46, 0, 604800L, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.rocksalt.", 5.0F, 20000, (byte)36, 15, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1205 */     createItemTemplate(1240, "spaghetti", "spaghetti", "excellent", "good", "ok", "poor", "Long, thin, cylindrical, solid pasta.", new short[] { 82, 212, 76, 219, 222 }, (short)659, (short)1, 0, 86400L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.spaghetti.", 10.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1215 */     createItemTemplate(1241, "icing", "icing", "excellent", "good", "ok", "poor", "A creamy glaze made of sugar with a liquid.", new short[] { 82, 212, 55, 233 }, (short)642, (short)1, 0, 86400L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.icing.", 25.0F, 50, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1224 */     createItemTemplate(1242, "cake mix", "cake mixes", "excellent", "good", "ok", "poor", "A wonderful cake mix.", new short[] { 5, 74 }, (short)525, (short)1, 0, 172800L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cake.mix.", 1.0F, 1200, (byte)22, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1233 */     createItemTemplate(1170, "slice of bread", "slices of bread", "superb", "good", "ok", "poor", "A slice of bread from a loaf.", new short[] { 82, 212, 74, 222 }, (short)505, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bread.slice.", 4.0F, 100, (byte)22, 2, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1242 */     createItemTemplate(1244, "breadcrumbs", "breadcrumbs", "superb", "good", "ok", "poor", "Small particles of dry bread, used for breading or crumbing foods.", new short[] { 82, 146, 212, 74, 222 }, (short)538, (short)1, 0, 172800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bread.crumbs.", 4.0F, 100, (byte)22, 2, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1251 */     createItemTemplate(1243, "bee smoker", "bee smokers", "excellent", "good", "ok", "poor", "A device used in beekeeping to calm honey bees. It is designed to generate smoke from the smouldering of various fuels, hence the name.", new short[] { 108, 22, 44, 52 }, (short)736, (short)18, 0, 3024000L, 10, 15, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.beesmoker.", 15.0F, 2100, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1260 */     createItemTemplate(1245, "meatballs", "meatballs", "superb", "good", "ok", "poor", "Minced meat rolled into small balls.", new short[] { 82, 146, 212, 74, 219, 222, 233 }, (short)648, (short)1, 0, 345600L, 2, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.meatballs.", 4.0F, 120, (byte)2, 2, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1270 */     createItemTemplate(1246, "vinegar", "vinegar", "excellent", "good", "ok", "poor", "A sour liquid containing acetic acid, produced by fermenting a solution, used as a condiment and preservative.", new short[] { 108, 26, 88, 212, 233 }, (short)568, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.vinegar.", 15.0F, 600, (byte)26, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1279 */     createItemTemplate(1249, "cream", "cream", "excellent", "good", "ok", "poor", "A dairy product composed of the higher-butterfat layer skimmed from the top of milk.", new short[] { 212, 26, 88, 233 }, (short)541, (short)1, 0, 86400L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.cream.", 15.0F, 20, (byte)28, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1287 */       .setNutritionValues(3450, 25, 367, 16);
/*      */     
/* 1289 */     createItemTemplate(1250, "goblin skull", "goblin skulls", "excellent", "good", "ok", "poor", "The skull from a goblin, looks like it could make a great drinking vessel.", new short[] { 62, 187, 211 }, (short)296, (short)1, 0, Long.MAX_VALUE, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.skull.small.", 200.0F, 1000, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1298 */     createItemTemplate(1251, "clay beer stein", "clay beer steins", "excellent", "good", "ok", "poor", "A beer mug made out of stoneware. Needs to be fired", new short[] { 108, 196, 44, 147, 1, 63 }, (short)258, (short)1, 0, 172800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.beerstein.clay.", 50.0F, 3000, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1308 */     createItemTemplate(1252, "beer stein", "beer steins", "excellent", "good", "ok", "poor", "A beer mug made out of stoneware.", new short[] { 108, 30, 1, 33 }, (short)259, (short)1, 0, Long.MAX_VALUE, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.beerstein.pottery.", 200.0F, 3000, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1317 */     createItemTemplate(1253, "skull cup", "skull cups", "excellent", "good", "ok", "poor", "The skull from a goblin sealed with wax to make it liquid proof.", new short[] { 108, 44, 1, 33, 187 }, (short)315, (short)1, 0, Long.MAX_VALUE, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.skull.mug.", 75.0F, 1000, (byte)35, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1327 */     createItemTemplate(1255, "wax sealing kit", "wax sealing kits", "excellent", "good", "ok", "poor", "A kit that can be used to seal some containers if they just have liquid in.", new short[] { 146 }, (short)498, (short)1, 0, 172800L, 4, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sealingkit.", 1.0F, 1000, (byte)33, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1336 */     createItemTemplate(1256, "biscuit mix", "biscuit mixes", "excellent", "good", "ok", "poor", "A wonderful biscuit mix.", new short[] { 5, 74 }, (short)525, (short)1, 0, 172800L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.biscuit.mix.", 1.0F, 1200, (byte)22, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1345 */     createItemTemplate(1257, "scone mix", "scone mixes", "excellent", "good", "ok", "poor", "A wonderful scone mix.", new short[] { 5, 74 }, (short)525, (short)1, 0, 172800L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.scone.mix.", 1.0F, 1200, (byte)22, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1354 */     createItemTemplate(1176, "fudge sauce", "fudge sauce", "excellent", "good", "ok", "poor", "Hot fudge sauce.", new short[] { 129, 212, 55, 219, 26, 88 }, (short)584, (short)1, 0, 86400L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.fudgesauce.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1364 */     createItemTemplate(1259, "stir fry", "stir frys", "excellent", "good", "ok", "poor", "A (quick) cooked meal.", new short[] { 82, 129, 212, 76, 219, 222, 233 }, (short)539, (short)1, 0, 86400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.stirfry.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1374 */     createItemTemplate(1260, "nori", "bunches of nori", "excellent", "good", "ok", "poor", "A flattened edible seaweed.", new short[] { 146, 46, 113, 5, 55 }, (short)645, (short)1, 0, 9072000L, 1, 4, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.nori.", 5.0F, 50, (byte)22, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1383 */     createItemTemplate(1261, "any meat", "any meat", "excellent", "good", "ok", "poor", "Any meat (meat, meat fillet, cooked meat).", new short[] { 207, 208, 5, 28, 223 }, (short)503, (short)1, 0, 172800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.meat.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1393 */     createItemTemplate(1262, "pizza", "pizzas", "excellent", "good", "ok", "poor", "A flatbread generally topped with tomato sauce and cheese and baked in an oven. It is commonly topped with a selection of meats, vegetables and condiments.", new short[] { 82, 129, 212, 76, 219, 222 }, (short)678, (short)1, 0, 86400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pizza.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1403 */     createItemTemplate(1263, "any oil", "any oil", "excellent", "good", "ok", "poor", "Any oil e.g. olive or cooking.", new short[] { 108, 26, 88, 34, 158, 113, 208 }, (short)587, (short)1, 0, 604800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.oil.", 15.0F, 180, (byte)25, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1413 */     createItemTemplate(1264, "sushi", "sushi", "excellent", "good", "ok", "poor", "A food preparation, consisting of cooked rice combined with other ingredients such as raw seafood, vegetables and sometimes tropical fruits.", new short[] { 82, 129, 212, 76, 220, 222, 233 }, (short)1441, (short)1, 0, 86400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.sushi.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1423 */     createItemTemplate(1265, "pickle", "pickles", "excellent", "good", "ok", "poor", "An edible product, such as a cucumber, that has been preserved and flavored in a solution of vinegar.", new short[] { 82, 129, 212, 74, 220, 222, 233 }, (short)667, (short)1, 0, 604800L, 4, 5, 19, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pickle.", 15.0F, 200, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1433 */     createItemTemplate(1266, "pudding", "puddings", "excellent", "good", "ok", "poor", "A sweet dish, often made from sugar, milk, flour, and flavoring, and usually eaten cold after a meal.", new short[] { 82, 129, 212, 76, 220, 222, 233 }, (short)679, (short)1, 0, 86400L, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.pudding.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1443 */     createItemTemplate(1174, "batter", "batter", "excellent", "good", "ok", "poor", "A mostly solid mixture of flour, milk and egg.", new short[] { 5, 129, 212, 55, 108 }, (short)584, (short)1, 0, 86400L, 4, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.batter.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1452 */     createItemTemplate(1267, "any flower", "any flowers", "excellent", "good", "ok", "poor", "Any flower.", new short[] { 207, 208, 211, 118 }, (short)681, (short)1, 0, 172800L, 10, 10, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.flower.flower1.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1461 */     createItemTemplate(1254, "beeswax", "beeswax", "excellent", "good", "ok", "poor", "A natural wax produced by honey bees which is formed into 'scales'.", new short[] { 146, 46 }, (short)498, (short)1, 0, 19353600L, 4, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.beeswax.", 15.0F, 100, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1470 */     createItemTemplate(1268, "broth", "broth", "excellent", "good", "ok", "poor", "A soup in which there are solid pieces of meat or fish, along with some vegetables.", new short[] { 82, 219, 76, 26, 90, 88, 233, 212 }, (short)531, (short)1, 0, 172800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.soup.", 1.0F, 700, (byte)26, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1480 */     createItemTemplate(1269, "label", "labels", "excellent", "good", "ok", "poor", "A piece of papyrus that has been cut into strips to use as a label.", new short[] { 21, 146 }, (short)640, (short)1, 0, 3024000L, 1, 5, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.label.", 5.0F, 2, (byte)33, 1000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1489 */     createItemTemplate(1270, "wood pulp", "wood pulp", "excellent", "good", "ok", "poor", "Wood fibre reduced chemically to pulp and used in the manufacture of paper.", new short[] { 21, 46, 146, 129, 211 }, (short)547, (short)1, 0, 604800L, 5, 5, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.woodpulp.", 5.0F, 3000, (byte)14, 10, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1498 */     createItemTemplate(1271, 3, "village message board", "village message boards", "fresh", "dry", "brittle", "rotten", "A board where you can leave messages for other citizens within your village, by pinning notes onto it.", new short[] { 21, 142, 51, 52, 44, 18, 199, 86, 178, 48, 47 }, (short)252, (short)1, 20, 9072000L, 1, 5, 60, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.sign.messageboard.", 10.0F, 5000, (byte)14, 5000, true, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1509 */     createItemTemplate(1272, "paper sheet", "paper sheets", "excellent", "good", "ok", "poor", "A flat piece of paper made from pressed wood pulp.", new short[] { 21, 159, 146, 211 }, (short)640, (short)44, 0, 3024000L, 1, 20, 25, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sheet.paper.", 5.0F, 10, (byte)33, 5000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1517 */     createItemTemplate(1273, "hops", "hops", "excellent", "good", "ok", "poor", "A small but very aromatic plant with dark-green leaves that have serrated margin.", new short[] { 146, 5, 55, 212 }, (short)545, (short)1, 0, 28800L, 1, 2, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.hops.", 100.0F, 50, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1525 */       .setNutritionValues(700, 150, 9, 38);
/*      */     
/* 1527 */     createItemTemplate(1274, "hops trellis", "trellises", "excellent", "good", "ok", "poor", "Some hops growing up a sturdy wooden trellis.", new short[] { 108, 21, 52, 44, 86, 178, 51, 199, 167, 230 }, (short)420, (short)58, 0, 9072000L, 10, 250, 300, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.trellis.hops.", 40.0F, 5000, (byte)68, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1537 */       .setHarvestsTo(1273);
/*      */     
/* 1539 */     createItemTemplate(1275, "hops seedling", "hops seedlings", "superb", "good", "ok", "poor", "A tiny hops seedling.", new short[] { 146 }, (short)484, (short)1, 0, 86400L, 1, 3, 10, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.sprout.", 200.0F, 50, (byte)68, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1548 */     createItemTemplate(1276, "snowball", "snowballs", "superb", "good", "ok", "poor", "A ball of snow that someone makes usually for throwing.", new short[] { 5, 27 }, (short)632, (short)1, 0, 3600L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.resource.snowball.", 1.0F, 100, (byte)26, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1556 */       .setNutritionValues(0, 0, 0, 0);
/*      */     
/* 1558 */     createItemTemplate(1277, "larder", "larders", "superb", "good", "ok", "poor", "A cool area for storing food.", new short[] { 108, 147, 135, 144, 21, 51, 52, 44, 47, 1, 92, 176, 48, 199, 180, 112 }, (short)286, (short)1, 0, 9072000L, 80, 120, 210, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.larder.", 50.0F, 6500, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1570 */       .setContainerSize(0, 0, 0)
/* 1571 */       .setInitialContainers(new InitialContainer[] { new InitialContainer(1279, "top shelf"), new InitialContainer(1279, "second shelf"), new InitialContainer(1279, "middle shelf"), new InitialContainer(1279, "fourth shelf"), new InitialContainer(1279, "lower shelf"), new InitialContainer(1278, "icebox") });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1579 */     createItemTemplate(1278, "ice box", "ice box", "superb", "good", "ok", "poor", "An insulated box for storing ice to keep items in a larder cool.", new short[] { 21, 1, 31, 54, 232, 240, 229, 112, 157, 245 }, (short)286, (short)1, 0, Long.MAX_VALUE, 80, 80, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 6500, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1590 */     createItemTemplate(1279, "food shelf", "food shelves", "superb", "good", "ok", "poor", "A food shelf.", new short[] { 21, 1, 31, 229, 112, 232, 240, 157, 245 }, (short)286, (short)1, 0, Long.MAX_VALUE, 80, 80, 120, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 6500, (byte)14, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1600 */     createItemTemplate(1280, "coconut", "coconuts", "excellent", "good", "ok", "poor", "Normally a tropical drupe from a coconut tree, but this one is a mixture of cocoa and hazelnuts!", new short[] { 146, 5, 80, 212, 55 }, (short)500, (short)1, 0, 28800L, 7, 7, 7, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.coconut.", 15.0F, 500, (byte)22, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1608 */       .setNutritionValues(3540, 150, 330, 33);
/*      */     
/* 1610 */     createItemTemplate(1281, "ice cream", "ice creams", "superb", "good", "ok", "poor", "A sweetened frozen food typically eaten as a snack or dessert.", new short[] { 5, 55, 220, 27, 108, 233 }, (short)598, (short)1, 0, 86400L, 3, 3, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.icecream.", 1.0F, 100, (byte)26, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1620 */     createItemTemplate(1282, "sweet", "sweets", "superb", "good", "ok", "poor", "A handfull of sweets typically eaten as a snack.", new short[] { 82, 74, 108, 212, 233, 220, 234 }, (short)1442, (short)1, 0, 604800L, 3, 3, 3, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.sweet.", 1.0F, 100, (byte)22, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1630 */     createItemTemplate(1283, "orange", "oranges", "delicious", "nice", "old", "rotten", "The orange is the fruit of the citrus species Citrus x sinensis in the family Rutaceae.", new short[] { 146, 104, 5, 80, 212, 157, 74 }, (short)658, (short)1, 0, 28800L, 5, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.orange.", 100.0F, 300, (byte)22, 15, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1639 */       .setNutritionValues(470, 120, 1, 9)
/* 1640 */       .setFoodGroup(1163);
/*      */     
/* 1642 */     createItemTemplate(1284, "boiler", "boilers", "superb", "good", "ok", "poor", "A boiler used in a still.", new short[] { 22, 1, 31, 54, 232, 240, 178, 33, 77, 229, 112, 157, 245 }, (short)290, (short)1, 0, Long.MAX_VALUE, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.still.boiler.", 50.0F, 6500, (byte)10, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1654 */     createItemTemplate(1285, "condenser", "condensers", "superb", "good", "ok", "poor", "A condenser used in a still.", new short[] { 22, 1, 31, 54, 232, 240, 178, 33, 63, 229, 112, 157, 245 }, (short)290, (short)1, 0, Long.MAX_VALUE, 25, 25, 30, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.still.condenser.", 50.0F, 6500, (byte)10, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1666 */     createItemTemplate(1286, "rum", "rum", "excellent", "good", "ok", "poor", "A spirit made from sugar.", new short[] { 108, 26, 88, 90, 213, 212, 214, 89 }, (short)551, (short)1, 0, 604800L, 2, 5, 5, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.rum.", 22.0F, 1000, (byte)26, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1675 */       .setAlcoholStrength(22);
/*      */     
/* 1677 */     createItemTemplate(1287, "muffin", "muffins", "excellent", "good", "ok", "poor", "Plain, unadorned muffins.  So sad.", new short[] { 82, 129, 212, 76, 219, 222, 233 }, (short)644, (short)1, 0, 1209600L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.muffin.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1687 */     createItemTemplate(1288, "cookie", "cookies", "excellent", "good", "ok", "poor", "A small, flat, sweet, baked biscuit.", new short[] { 82, 129, 212, 76, 220, 222, 233 }, (short)532, (short)1, 0, 1209600L, 2, 3, 4, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.food.biscuit.cookie.", 15.0F, 600, (byte)22, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1697 */     createItemTemplate(1296, "lunchbox", "lunchboxes", "superb", "good", "ok", "poor", "A container made from tin that keeps food fresh and warm when travelling.", new short[] { 108, 147, 135, 144, 178, 22, 44, 47, 1, 92, 48, 180 }, (short)423, (short)1, 0, 12096000L, 22, 17, 14, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tin.lunchbox.", 60.0F, 500, (byte)34, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1707 */       .setContainerSize(0, 0, 0)
/* 1708 */       .setInitialContainers(new InitialContainer[] {
/*      */           
/*      */           new InitialContainer(1294, "thermos", (byte)19), new InitialContainer(1295, "food tin", (byte)34)
/* 1711 */         }).setMaxItemCount(3)
/* 1712 */       .setMaxItemWeight(2000);
/*      */     
/* 1714 */     createItemTemplate(1297, "picnic basket", "picnic baskets", "superb", "good", "ok", "poor", "A wicker basket with containers for storing a bit of food and drink for travelling. 'Christmas 2016' has been engraved under the lid.", new short[] { 108, 135, 144, 238, 178, 47, 1, 92, 40, 187, 48, 180, 44 }, (short)424, (short)1, 0, Long.MAX_VALUE, 22, 17, 14, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.picnicbasket.", 50.0F, 300, (byte)70, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1723 */       .setContainerSize(0, 0, 0)
/* 1724 */       .setInitialContainers(new InitialContainer[] {
/*      */           
/*      */           new InitialContainer(1294, "thermos", (byte)19), new InitialContainer(1295, "food compartment", (byte)17)
/* 1727 */         }).setMaxItemCount(3)
/* 1728 */       .setMaxItemWeight(2200);
/*      */     
/* 1730 */     createItemTemplate(1294, "thermos", "thermos", "superb", "good", "ok", "poor", "An insulated container for storing liquids.", new short[] { 30, 1, 31, 229, 112, 238, 178, 33, 232, 40, 157 }, (short)425, (short)1, 0, Long.MAX_VALUE, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.pottery.thermos.", 50.0F, 250, (byte)19, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1741 */     createItemTemplate(1295, "food compartment", "food compartments", "superb", "good", "ok", "poor", "A small compartment for storing prepared foods.", new short[] { 22, 1, 31, 229, 112, 178, 232, 238, 40, 157 }, (short)426, (short)1, 0, Long.MAX_VALUE, 10, 10, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.tin.foodtin.", 50.0F, 250, (byte)34, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1752 */     createItemTemplate(1331, "chocolate milk", "chocolate milk", "excellent", "good", "ok", "poor", "Good old chocolate milk. Reminds you of hot summer days.", new short[] { 82, 74, 26, 90, 233 }, (short)541, (short)1, 0, 3600L, 10, 20, 20, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.liquid.", 1.0F, 1000, (byte)28);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplateCreatorCooking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
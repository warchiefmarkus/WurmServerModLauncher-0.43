/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
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
/*      */ public class ItemTemplateCreatorFishing
/*      */   extends ItemTemplateCreator
/*      */   implements ModelConstants, ItemTypes, MiscConstants, MonetaryConstants
/*      */ {
/*   39 */   private static final Logger logger = Logger.getLogger(ItemTemplateCreator.class.getName());
/*      */ 
/*      */ 
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
/*   52 */     createItemTemplate(94, "old fine fishing rod", "old fine fishing rods", "excellent", "good", "ok", "poor", "A long pole with a string and a hook made of iron.", new short[] { 157, 108, 44, 21 }, (short)786, (short)1, 0, 9072000L, 1, 1, 200, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.rod.ironhook.", 1.0F, 1110, (byte)14, 200, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   61 */     createItemTemplate(780, "old unstrung fishing rod", "old unstrung fishing rods", "excellent", "good", "ok", "poor", "A long pole that just needs a string and a hook in order to fish with.", new short[] { 157, 108, 44, 21 }, (short)786, (short)1, 0, 9072000L, 1, 1, 200, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.rod.unstrung.", 1.0F, 1000, (byte)14, 200, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   70 */     createItemTemplate(95, "old fine fishing hook", "old fine fishing hooks", "excellent", "good", "ok", "poor", "A small sharp hook made from iron.", new short[] { 157, 22 }, (short)785, (short)1, 0, 3024000L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.hook.", 3.0F, 10, (byte)11, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   79 */     createItemTemplate(96, "old fishing hook", "old wooden fishing hooks", "excellent", "good", "ok", "poor", "A wood chip carved into a hook with a stone as sink.", new short[] { 157, 21 }, (short)805, (short)1, 0, 9072000L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.hook.", 4.0F, 5, (byte)14, 10, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   88 */     createItemTemplate(152, "old fishing rod", "old fishing rods", "excellent", "good", "ok", "poor", "A long pole with a string and a hook made of wood and a small stone as a sink.", new short[] { 157, 108, 44, 21 }, (short)786, (short)1, 0, 9072000L, 1, 1, 200, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.rod.", 3.0F, 1105, (byte)14, 100, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   97 */     createItemTemplate(150, "old fine fishing line", "old fine fishing lines", "excellent", "good", "ok", "poor", "An iron fishing hook attached to a cotton string.", new short[] { 157, 21 }, (short)785, (short)1, 0, 3024000L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.hookstring.", 1.0F, 110, (byte)11, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  106 */     createItemTemplate(151, "old fishing line", "old fishing lines", "excellent", "good", "ok", "poor", "An wooden fishing hook with a stone sink, both attached to a cotton string.", new short[] { 157, 21 }, (short)785, (short)1, 0, 9072000L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.hookstring.", 1.0F, 105, (byte)14, 50, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  116 */     createItemTemplate(158, "smallmouth bass", "smallmouth bass", "delicious", "nice", "old", "rotten", "This fish has greenish sides with dark vertical bars that come and go, and three dark bars radiating from the eye.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 60, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.bass.", 47.0F, 4000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  125 */       .setNutritionValues(1240, 0, 26, 240)
/*  126 */       .setFoodGroup(1201);
/*      */     
/*  128 */     createItemTemplate(164, "carp", "carp", "delicious", "nice", "old", "rotten", "A large yellowy fish with large bulbous eyes.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 15, 100, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.carp.", 55.0F, 16000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  137 */       .setNutritionValues(1270, 0, 60, 180)
/*  138 */       .setFoodGroup(1201);
/*      */     
/*  140 */     createItemTemplate(160, "catfish", "catfishes", "delicious", "nice", "old", "rotten", "A pretty large and strange bluish, black fish with whiskers.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 15, 50, 200, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.catfish.", 35.0F, 25000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  149 */       .setNutritionValues(1270, 0, 60, 180)
/*  150 */       .setFoodGroup(1201);
/*      */     
/*  152 */     createItemTemplate(161, "snook", "snooks", "delicious", "nice", "old", "rotten", "This white fish is yellowish brown on the top back and has a distinct black lateral line.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 80, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.snook.", 70.0F, 20000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  161 */       .setNutritionValues(1270, 0, 60, 180)
/*  162 */       .setFoodGroup(1201);
/*      */     
/*  164 */     createItemTemplate(159, "herring", "herrings", "delicious", "nice", "old", "rotten", "A long and thin bright silvery fish with blue-black back and a protruding lower jaw .", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 30, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.herring.", 10.0F, 300, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  173 */       .setNutritionValues(1580, 0, 90, 180)
/*  174 */       .setFoodGroup(1201);
/*      */     
/*  176 */     createItemTemplate(157, "pike", "pikes", "delicious", "nice", "old", "rotten", "A long grey fish with a strong jaw lined with lots of small sharp teeth.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 60, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.pike.", 45.0F, 6000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  185 */       .setNutritionValues(1130, 0, 10, 250)
/*  186 */       .setFoodGroup(1201);
/*      */     
/*  188 */     createItemTemplate(162, "roach", "roaches", "delicious", "nice", "old", "rotten", "A small silvery fish of little worth.", new short[] { 108, 5, 36, 146, 212, 254, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 4, 7, 20, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.roach.", 3.0F, 200, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  197 */       .setNutritionValues(910, 0, 10, 190)
/*  198 */       .setFoodGroup(1201);
/*      */     
/*  200 */     createItemTemplate(165, "brook trout", "brook trouts", "delicious", "nice", "old", "rotten", "This fish has a brownish green back with wormlike marks and some small red spots with blue halos.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 40, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.trout.", 25.0F, 3000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  209 */       .setNutritionValues(1500, 0, 60, 230)
/*  210 */       .setFoodGroup(1201);
/*      */     
/*  212 */     createItemTemplate(163, "perch", "perches", "delicious", "nice", "old", "rotten", "An olive green and golden fish with dark ribbons.", new short[] { 108, 5, 36, 146, 212, 254, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 4, 7, 30, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.perch.", 7.0F, 500, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  221 */       .setNutritionValues(910, 0, 10, 190)
/*  222 */       .setFoodGroup(1201);
/*      */     
/*  224 */     createItemTemplate(569, "marlin", "marlins", "delicious", "nice", "old", "rotten", "This marlin is a bluish silvery sturdy thick fish with a distinctive upper jaw that forms a pointed spear or bill and some pretty extraordinary fins.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 20, 40, 180, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.marlin.", 80.0F, 200000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  233 */       .setNutritionValues(1550, 0, 50, 250)
/*  234 */       .setFoodGroup(1201);
/*      */     
/*  236 */     createItemTemplate(570, "blue shark", "blue sharks", "delicious", "nice", "old", "rotten", "Living in packs, the blue sharks could be called the wolves of the sea. Only a fool would fail to respect their menacing jaws.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 40, 50, 180, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.blueshark.", 75.0F, 40000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  245 */       .setNutritionValues(1300, 0, 50, 210)
/*  246 */       .setFoodGroup(1201);
/*      */     
/*  248 */     createItemTemplate(571, "white shark", "white sharks", "delicious", "nice", "old", "rotten", "These constantly hungry monsters look alive even in their death. The cold gaze of the shark penetrates you into the spine.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 20, 50, 180, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.whiteshark.", 77.0F, 200000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  257 */       .setNutritionValues(1350, 0, 60, 200)
/*  258 */       .setFoodGroup(1201);
/*      */     
/*  260 */     createItemTemplate(572, "octopus", "octopus", "delicious", "nice", "old", "rotten", "This is a weird beast with eight legs. You have heard scary rumours that larger variants sometimes attack ships.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 10, 15, 40, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.octopus.black.", 60.0F, 10000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  269 */       .setNutritionValues(1640, 0, 20, 300)
/*  270 */       .setFoodGroup(1201);
/*      */     
/*  272 */     createItemTemplate(573, "sailfish", "sailfish", "delicious", "nice", "old", "rotten", "Now, isn't this the weirdest thing? A fish with a glistening sail instead of a back fin!", new short[] { 108, 5, 36, 146, 74, 212, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 80, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.sailfish.", 65.0F, 10000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  281 */       .setNutritionValues(820, 0, 10, 180)
/*  282 */       .setFoodGroup(1201);
/*      */     
/*  284 */     createItemTemplate(574, "dorado", "dorados", "delicious", "nice", "old", "rotten", "The dorado is a thick beautiful green-blue fish with a massive head but thin tail. It somewhat resembles a huge water drop.", new short[] { 108, 5, 36, 146, 212, 75, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 80, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.dorado.", 72.0F, 30000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  293 */       .setNutritionValues(1270, 0, 60, 180)
/*  294 */       .setFoodGroup(1201);
/*      */     
/*  296 */     createItemTemplate(575, "tuna", "tunas", "delicious", "nice", "old", "rotten", "The tuna is a robust fish with conical head and a rather large mouth. The color is dark blue above and grey below, and they have a delicious red meat.", new short[] { 108, 5, 36, 146, 212, 76, 219, 223 }, (short)504, (short)1, 0, 86400L, 15, 40, 100, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.tuna.", 68.0F, 60000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  305 */       .setNutritionValues(1840, 0, 60, 300)
/*  306 */       .setFoodGroup(1201);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  311 */     createItemTemplate(1335, "salmon", "salmons", "delicious", "nice", "old", "rotten", "The common name for several species of ray-finned fish in the family Salmonidae.", new short[] { 108, 5, 36, 146, 212, 76, 219, 223 }, (short)504, (short)1, 0, 86400L, 8, 20, 100, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.salmon.", 50.0F, 25000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  320 */       .setNutritionValues(2040, 0, 40, 400)
/*  321 */       .setFoodGroup(1201);
/*      */ 
/*      */     
/*  324 */     createItemTemplate(1336, "tarpon", "tarpons", "delicious", "nice", "old", "rotten", "Tarpons are large air-breathing fish of the genus Megalops.", new short[] { 108, 5, 36, 146, 212, 76, 219, 223 }, (short)504, (short)1, 0, 86400L, 15, 40, 100, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.tarpon.", 57.0F, 50000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  333 */       .setNutritionValues(1500, 0, 50, 500)
/*  334 */       .setFoodGroup(1201);
/*      */ 
/*      */     
/*  337 */     createItemTemplate(1337, "sardine", "sardines", "delicious", "nice", "old", "rotten", "Sardine and pilchard are common names used to refer to various small, oily fish in the herring family, family Clupeidae.", new short[] { 108, 5, 36, 146, 212, 254, 76, 219, 223 }, (short)504, (short)1, 0, 86400L, 2, 4, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.sardine.", 2.0F, 100, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  346 */       .setNutritionValues(910, 0, 10, 190)
/*  347 */       .setFoodGroup(1201);
/*      */ 
/*      */     
/*  350 */     createItemTemplate(1338, "minnow", "minnows", "delicious", "nice", "old", "rotten", "Minnows are small freshwater fish of the family Cyprinidae, ideal for bait.", new short[] { 108, 5, 36, 146, 212, 254, 76, 219, 223 }, (short)504, (short)1, 0, 86400L, 2, 4, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.minnow.", 4.0F, 100, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  359 */       .setNutritionValues(800, 0, 20, 200)
/*  360 */       .setFoodGroup(1201);
/*      */ 
/*      */     
/*  363 */     createItemTemplate(1339, "loach", "loaches", "delicious", "nice", "old", "rotten", "The loach is a small eel-like freshwater fish belonging to the loach family (Cobitidae).", new short[] { 108, 5, 36, 146, 212, 76, 219, 223 }, (short)504, (short)1, 0, 86400L, 4, 7, 50, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.loach.", 21.0F, 2000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  372 */       .setNutritionValues(900, 0, 20, 200)
/*  373 */       .setFoodGroup(1201);
/*      */     
/*  375 */     createItemTemplate(1340, "wurmfish", "wurmfishs", "delicious", "nice", "old", "rotten", "A unique blind fish found in caves.", new short[] { 108, 5, 36, 146, 212, 76, 219, 223 }, (short)504, (short)1, 0, 86400L, 5, 10, 50, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.wurm.", 42.0F, 5000, (byte)2, 100, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  384 */       .setNutritionValues(2000, 0, 40, 500)
/*  385 */       .setFoodGroup(1201);
/*      */     
/*  387 */     createItemTemplate(1394, "clam", "clams", "superb", "good", "ok", "poor", "A clam that may have something useful in it.", new short[] { 229, 112, 157, 146 }, (short)1499, (short)1, 0, 86400L, 10, 10, 35, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.clam.", 50.0F, 2000, (byte)15, 100, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  397 */     createItemTemplate(1341, "tackle box", "tackle boxes", "almost full", "somewhat occupied", "half-full", "emptyish", "A small chest with compartments inside specifically for holding fishing tackle.", new short[] { 108, 135, 1, 47, 51, 52, 147, 44, 92, 180, 22 }, (short)244, (short)1, 0, 9072000L, 30, 30, 50, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.tacklebox.", 10.0F, 1000, (byte)34, 10000, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  407 */       .setContainerSize(0, 0, 0)
/*  408 */       .setInitialContainers(new InitialContainer[] { 
/*      */           new InitialContainer(1376, "reels"), new InitialContainer(1377, "lines"), new InitialContainer(1378, "floats"), new InitialContainer(1379, "fishing hooks"), new InitialContainer(1380, "bait - fly"), new InitialContainer(1381, "bait - cheese piece"), new InitialContainer(1382, "bait - dough ball"), new InitialContainer(1383, "bait - wurm"), new InitialContainer(1388, "bait - sardine"), new InitialContainer(1389, "bait - roach"), 
/*      */           new InitialContainer(1390, "bait - perch"), new InitialContainer(1391, "bait - minnow"), new InitialContainer(1384, "bait - fish bits"), new InitialContainer(1385, "bait - grub"), new InitialContainer(1386, "bait - wheat grain"), new InitialContainer(1387, "bait - corn kernal") });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  427 */     createItemTemplate(1376, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment for holding fishing reels.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 4, 4, 120, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 40, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  436 */       .addContainerRestriction(false, "empty fishing reel slot", new int[] { 1372, 1373, 1374, 1375 });
/*      */ 
/*      */     
/*  439 */     createItemTemplate(1377, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment for holding fishing lines.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 1001, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 20, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  448 */       .addContainerRestriction(false, "empty fishing line slot", new int[] { 1347, 1348, 1349, 1350, 1351 });
/*      */ 
/*      */     
/*  451 */     createItemTemplate(1378, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment for holding fishing floats.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 2, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  460 */       .addContainerRestriction(false, "empty float slot", new int[] { 1355, 1352, 1354, 1353 });
/*      */     
/*  462 */     createItemTemplate(1379, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment for holding fishing hooks.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  471 */       .addContainerRestriction(false, "empty fishing hook slot", new int[] { 1356, 1357, 1358 });
/*      */     
/*  473 */     createItemTemplate(1380, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold fly for baits.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  482 */       .addContainerRestriction(false, new int[] { 1359 });
/*      */     
/*  484 */     createItemTemplate(1381, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold cheese for baits.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  493 */       .addContainerRestriction(false, new int[] { 1360 });
/*      */     
/*  495 */     createItemTemplate(1382, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold dough for baits.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  504 */       .addContainerRestriction(false, new int[] { 1361 });
/*      */     
/*  506 */     createItemTemplate(1383, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold wurm for baits.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  515 */       .addContainerRestriction(false, new int[] { 1362 });
/*      */     
/*  517 */     createItemTemplate(1384, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold fish bits for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  526 */       .addContainerRestriction(false, new int[] { 1363 });
/*      */     
/*  528 */     createItemTemplate(1385, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold grub for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  537 */       .addContainerRestriction(false, new int[] { 1364 });
/*      */     
/*  539 */     createItemTemplate(1386, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold wheat grains for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  548 */       .addContainerRestriction(false, new int[] { 1365 });
/*      */     
/*  550 */     createItemTemplate(1387, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold corn kernals for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 1, 1, 101, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  559 */       .addContainerRestriction(false, new int[] { 1366 });
/*      */     
/*  561 */     createItemTemplate(1388, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold sardine for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 2, 4, 50, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  570 */       .addContainerRestriction(false, new int[] { 1337 });
/*      */     
/*  572 */     createItemTemplate(1389, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold roach for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 4, 10, 200, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  581 */       .addContainerRestriction(false, new int[] { 162 });
/*      */     
/*  583 */     createItemTemplate(1390, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold perch for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 4, 10, 200, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  592 */       .addContainerRestriction(false, new int[] { 163 });
/*      */     
/*  594 */     createItemTemplate(1391, "compartment", "compartments", "superb", "good", "ok", "poor", "A compartment to hold minnow for bait.", new short[] { 21, 1, 31, 229, 112, 232, 157, 245 }, (short)244, (short)1, 0, Long.MAX_VALUE, 2, 4, 220, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.furniture.wooden.shelf.", 50.0F, 10, (byte)14, 1, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  603 */       .addContainerRestriction(false, new int[] { 1338 });
/*      */     
/*  605 */     createItemTemplate(1342, "fish keep net", "fish keep nets", "almost full", "somewhat occupied", "half-full", "emptyish", "A net that is used to hold fish caught, whilst fishing, to keep them fresh.", new short[] { 108, 147, 24, 44, 1, 199, 52, 48 }, (short)343, (short)1, 0, 28800L, 50, 50, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.net.keep.", 45.0F, 10000, (byte)17, 3000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  614 */       .setContainerSize(150, 150, 800)
/*  615 */       .addContainerRestriction(false, "empty fish slot", new int[] { 
/*      */           158, 164, 160, 1394, 574, 159, 1339, 569, 1338, 572, 
/*      */           163, 157, 162, 573, 1335, 1337, 570, 571, 161, 1336, 
/*      */           165, 575, 1340 });
/*      */ 
/*      */     
/*  621 */     createItemTemplate(1343, "fishing net", "fishing nets", "almost full", "somewhat occupied", "half-full", "emptyish", "A net that can be used to catch small fish in shallows.", new short[] { 108, 147, 24, 44, 1, 63 }, (short)885, (short)1, 0, 28800L, 50, 50, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.net.fish.", 1.0F, 500, (byte)17, 3000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  629 */       .setContainerSize(50, 50, 100)
/*  630 */       .addContainerRestriction(false, "empty fish slot", new int[] { 1338, 163, 162, 1337, 1394 });
/*      */ 
/*      */ 
/*      */     
/*  634 */     createItemTemplate(1344, "fishing pole", "fishing poles", "excellent", "good", "ok", "poor", "A thin shaft that could be used to fish with.", new short[] { 108, 44, 21, 147, 1 }, (short)786, (short)1, 0, 9072000L, 3, 3, 150, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.pole.", 1.0F, 860, (byte)14, 100, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  642 */       .setContainerSize(1, 1, 21)
/*  643 */       .addContainerRestriction(true, new int[] { 1347 });
/*      */     
/*  645 */     createItemTemplate(1345, "eyelet", "eyelets", "excellent", "good", "ok", "poor", "A metal loop for passing a fishing line through.", new short[] { 22 }, (short)1488, (short)1, 0, 3024000L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.islet.", 3.0F, 3, (byte)11, 15, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  654 */     createItemTemplate(1346, "fishing rod", "fishing rods", "excellent", "good", "ok", "poor", "A long fishing pole with eyelets.", new short[] { 21, 108, 44, 147, 1 }, (short)846, (short)1, 0, 9072000L, 4, 4, 200, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.rod.", 5.0F, 1506, (byte)14, 200, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  662 */       .setContainerSize(4, 4, 11)
/*  663 */       .addContainerRestriction(true, "empty fishing reel slot", new int[] { 1372, 1373, 1374, 1375 });
/*      */ 
/*      */ 
/*      */     
/*  667 */     createItemTemplate(1347, "basic fishing line", "basic fishing lines", "excellent", "good", "ok", "poor", "A fine string of cloth twisted into a basic fishing line.", new short[] { 24, 1, 251 }, (short)620, (short)1, 0, 28800L, 1, 1, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishline.basic.", 1.0F, 10, (byte)17, 210, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  675 */       .addContainerRestriction(true, "empty wood or metal fishing hook slot", new int[] { 1356, 1357
/*  676 */         }).addContainerRestriction(true, "empty float slot", new int[] { 1352, 1353, 1354, 1355 });
/*      */     
/*  678 */     createItemTemplate(1348, "light fishing line", "light fishing lines", "excellent", "good", "ok", "poor", "A spun fishing line to make it stronger, ideal for lighter fish.", new short[] { 24, 1, 251 }, (short)620, (short)1, 0, 28800L, 1, 1, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishline.light.", 5.0F, 10, (byte)17, 220, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  686 */       .addContainerRestriction(true, "empty fishing hook slot", new int[] { 1356, 1357, 1358
/*  687 */         }).addContainerRestriction(true, "empty float slot", new int[] { 1352, 1353, 1354, 1355 });
/*      */     
/*  689 */     createItemTemplate(1349, "medium fishing line", "medium fishing lines", "excellent", "good", "ok", "poor", "Interweaved light fishing line to increase it's strength, ideal for medium weight fish.", new short[] { 24, 1, 251 }, (short)620, (short)1, 0, 28800L, 1, 1, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishline.medium.", 10.0F, 10, (byte)17, 230, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  697 */       .addContainerRestriction(true, "empty fishing hook slot", new int[] { 1356, 1357, 1358
/*  698 */         }).addContainerRestriction(true, "empty float slot", new int[] { 1352, 1353, 1354, 1355 });
/*      */     
/*  700 */     createItemTemplate(1350, "heavy fishing line", "heavy fishing lines", "excellent", "good", "ok", "poor", "A spun medium fishing line for added strength, ideal for deep water fishing.", new short[] { 24, 1, 251 }, (short)620, (short)1, 0, 28800L, 1, 1, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishline.heavy.", 15.0F, 10, (byte)17, 240, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  708 */       .addContainerRestriction(true, "empty fishing hook slot", new int[] { 1356, 1357, 1358
/*  709 */         }).addContainerRestriction(true, "empty float slot", new int[] { 1352, 1353, 1354, 1355 });
/*      */     
/*  711 */     createItemTemplate(1351, "braided fishing line", "braided fishing lines", "excellent", "good", "ok", "poor", "Braided heavy fishing line for even more strength, ideal for deep sea fishing for those special fish.", new short[] { 24, 1, 251 }, (short)620, (short)1, 0, 28800L, 1, 1, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishline.braided.", 20.0F, 10, (byte)17, 250, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  719 */       .addContainerRestriction(true, "empty fishing hook slot", new int[] { 1356, 1357, 1358
/*  720 */         }).addContainerRestriction(true, "empty float slot", new int[] { 1352, 1353, 1354, 1355 });
/*      */ 
/*      */     
/*  723 */     createItemTemplate(1352, "feather", "feathers", "excellent", "good", "ok", "poor", "A feather from some bird, looks like it would float well.", new short[] { 146, 157, 112, 252 }, (short)1489, (short)1, 0, 86401L, 1, 1, 2, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.float.feather.", 1.0F, 10, (byte)35, 2, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  732 */     createItemTemplate(1353, "twig", "twigs", "excellent", "good", "ok", "poor", "A twig from some bush, looks like it would float well.", new short[] { 21, 157, 112, 252 }, (short)1490, (short)1, 0, 86401L, 1, 1, 2, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.float.twig.", 1.0F, 10, (byte)14, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  741 */     createItemTemplate(1354, "small piece of moss", "small pieces of moss", "excellent", "good", "ok", "poor", "A small bit of moss, looks like it would float well.", new short[] { 27, 157, 112, 252 }, (short)1500, (short)1, 0, 86401L, 1, 1, 2, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.float.moss.", 1.0F, 10, (byte)22, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  750 */     createItemTemplate(1355, "bark", "barks", "excellent", "good", "ok", "poor", "A piece of bark from a tree, looks like it would float well.", new short[] { 21, 157, 112, 252 }, (short)1491, (short)1, 0, 86401L, 1, 1, 2, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.float.bark.", 1.0F, 10, (byte)14, 1, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  760 */     createItemTemplate(1356, "wooden fishing hook", "wooden fishing hooks", "excellent", "good", "ok", "poor", "A wood chip carved into a hook with a stone as sink.", new short[] { 21, 1, 253, 44 }, (short)805, (short)1, 0, 9072000L, 1, 1, 2, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.hook.", 4.0F, 5, (byte)14, 10, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  768 */       .setContainerSize(4, 7, 30)
/*  769 */       .addContainerRestriction(true, "optional bait slot", new int[] { 
/*      */           1360, 1366, 1361, 1363, 1359, 1364, 1365, 1362, 163, 1338, 
/*      */           162, 1337 });
/*      */     
/*  773 */     createItemTemplate(1357, "metal fishing hook", "metal fishing hooks", "excellent", "good", "ok", "poor", "A small sharp hook made from metal.", new short[] { 22, 1, 253, 44 }, (short)785, (short)1, 0, 3024000L, 1, 1, 2, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.hook.", 3.0F, 10, (byte)11, 20, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  781 */       .setContainerSize(4, 7, 30)
/*  782 */       .addContainerRestriction(true, "optional bait slot", new int[] { 
/*      */           1360, 1366, 1361, 1363, 1359, 1364, 1365, 1362, 163, 1338, 
/*      */           162, 1337 });
/*      */     
/*  786 */     createItemTemplate(1358, "bone fishing hook", "bone fishing hooks", "excellent", "good", "ok", "poor", "A slither of bone worked into a small sharp hook.", new short[] { 1, 253 }, (short)865, (short)1, 0, Long.MAX_VALUE, 1, 1, 2, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.tool.fish.hook.", 6.0F, 10, (byte)35, 30, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  794 */       .setContainerSize(4, 7, 30)
/*  795 */       .addContainerRestriction(true, "optional bait slot", new int[] { 
/*      */           1360, 1366, 1361, 1363, 1359, 1364, 1365, 1362, 163, 1338, 
/*      */           162, 1337 });
/*      */ 
/*      */     
/*  800 */     createItemTemplate(1359, "fly", "flies", "excellent", "good", "ok", "poor", "A captured fly, should be good fishing with it.", new short[] { 254, 59 }, (short)1480, (short)1, 0, 172800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.fly.", 6.0F, 10, (byte)2, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  809 */     createItemTemplate(1360, "small piece of cheese", "cheese pieces", "excellent", "good", "ok", "poor", "A bit of cheese, ideal for fishing bait.", new short[] { 27, 254, 59 }, (short)1481, (short)1, 0, 604800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.cheese.", 6.0F, 10, (byte)22, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  818 */     createItemTemplate(1361, "dough ball", "dough balls", "excellent", "good", "ok", "poor", "A small ball of dough, ideal for fishing bait.", new short[] { 27, 254, 59 }, (short)1482, (short)1, 0, 172800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.dough.", 6.0F, 10, (byte)0, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  827 */     createItemTemplate(1362, "wurm", "wurms", "excellent", "good", "ok", "poor", "A live wurm, would probably make good bait.", new short[] { 254, 59 }, (short)1483, (short)1, 0, 172800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.wurm.", 6.0F, 10, (byte)2, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  836 */     createItemTemplate(1363, "bit of fish", "bits of fish", "excellent", "good", "ok", "poor", "Chopped up fish, ideal for bait.", new short[] { 254, 59 }, (short)1484, (short)1, 0, 172800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.fish.", 6.0F, 10, (byte)2, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  845 */     createItemTemplate(1364, "grub", "grubs", "excellent", "good", "ok", "poor", "A grub prised from a tree, ideal for bait.", new short[] { 254, 59 }, (short)1485, (short)1, 0, 172800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.grub.", 6.0F, 10, (byte)2, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  854 */     createItemTemplate(1365, "grain of wheat", "wheat grains", "excellent", "good", "ok", "poor", "A grain of wheat, ideal for bait.", new short[] { 254, 59 }, (short)1486, (short)1, 0, 172800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.wheat.", 6.0F, 10, (byte)6, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  863 */     createItemTemplate(1366, "corn kernel", "corn kernels", "excellent", "good", "ok", "poor", "The kernel of corn, ideal for bait.", new short[] { 254, 59 }, (short)1487, (short)1, 0, 172800L, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.bait.corn.", 6.0F, 10, (byte)22, 1, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  873 */     createItemTemplate(1367, "wood reel", "wood reels", "excellent", "good", "ok", "poor", "A wooded reel including a spool and winder, all carved out of a single shaft.", new short[] { 21, 108, 44 }, (short)1494, (short)1, 0, 9072000L, 3, 3, 3, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.reel.", 15.0F, 150, (byte)14, 115, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  882 */     createItemTemplate(1368, "metal reel", "metal reels", "excellent", "good", "ok", "poor", "A metal reel including a spool and winder, made from a single lump.", new short[] { 22, 108, 44 }, (short)1494, (short)1, 0, 3024000L, 3, 3, 3, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.reel.", 25.0F, 150, (byte)11, 150, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  891 */     createItemTemplate(1369, "professional reel", "professional reels", "excellent", "good", "ok", "poor", "A professional reel including a spool and winder, made from a single lump.", new short[] { 22, 108, 44 }, (short)1494, (short)1, 0, 3024000L, 3, 3, 3, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.reel.", 25.0F, 150, (byte)9, 150, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  900 */     createItemTemplate(1370, "reinforced handle", "reinforced handles", "excellent", "good", "ok", "poor", "A leather wound handle with extra wires to reinforce it.", new short[] { 21 }, (short)765, (short)1, 0, 3024000L, 2, 2, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.handle.reinforced.", 5.0F, 380, (byte)14, 15, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  909 */     createItemTemplate(1371, "padded handle", "padded handles", "excellent", "good", "ok", "poor", "A leather wound handle with extra wires to reinforce it, then cloth added for comfort.", new short[] { 21 }, (short)765, (short)1, 0, 3024000L, 2, 2, 10, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.part.handle.padded.", 5.0F, 400, (byte)14, 20, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  918 */     createItemTemplate(1372, "light fishing reel", "light fishing reels", "excellent", "good", "ok", "poor", "A wooden reel with a plain wood handle.", new short[] { 21, 108, 1, 250, 44 }, (short)1495, (short)1, 0, 9072000L, 4, 4, 11, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishingreel.light.", 5.0F, 250, (byte)14, 15, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  926 */       .addContainerRestriction(true, new int[] { 1348
/*  927 */         }).setContainerSize(4, 4, 51);
/*      */     
/*  929 */     createItemTemplate(1373, "medium fishing reel", "medium fishing reels", "excellent", "good", "ok", "poor", "A wooden reel with a leather wound handle.", new short[] { 21, 108, 1, 250, 44 }, (short)1496, (short)1, 0, 9072000L, 4, 4, 11, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishingreel.medium.", 10.0F, 280, (byte)14, 15, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  937 */       .addContainerRestriction(true, new int[] { 1349
/*  938 */         }).setContainerSize(4, 4, 51);
/*      */     
/*  940 */     createItemTemplate(1374, "deep water fishing reel", "deep water fishing reels", "excellent", "good", "ok", "poor", "A metal reel with a reinforced handle.", new short[] { 22, 108, 1, 250, 44 }, (short)1497, (short)1, 0, 3024000L, 4, 4, 11, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishingreel.deepwater.", 20.0F, 530, (byte)11, 20, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  948 */       .addContainerRestriction(true, new int[] { 1350
/*  949 */         }).setContainerSize(4, 4, 51);
/*      */     
/*  951 */     createItemTemplate(1375, "professional fishing reel", "professional fishing reels", "excellent", "good", "ok", "poor", "A professional reel with a padded handle.", new short[] { 22, 108, 1, 250, 44 }, (short)1498, (short)1, 0, 3024000L, 4, 4, 11, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fishingreel.professional.", 40.0F, 550, (byte)9, 25, false)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  959 */       .addContainerRestriction(true, new int[] { 1351
/*  960 */         }).setContainerSize(4, 4, 51);
/*      */     
/*  962 */     createItemTemplate(1393, "fishing rod rack", "fishing rod racks", "almost full", "somewhat occupied", "half-full", "emptyish", "A purposeful rack that works for fishing poles and rods.", new short[] { 21, 44, 1, 86, 52, 67, 31, 51, 199, 176, 178 }, (short)60, (short)1, 0, 9072000L, 10, 110, 180, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.container.rods.", 35.0F, 20000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  972 */       .addContainerRestriction(false, "empty fishing pole or rod slot", new int[] { 1344, 1346 });
/*      */     
/*  974 */     createItemTemplate(1395, "fishing trophy", "fishing trophies", "almost full", "somewhat occupied", "half-full", "emptyish", "A trophy stand to show off what nice fish you caught.", new short[] { 21, 44, 86, 52, 48, 67, 51, 199 }, (short)626, (short)1, 0, 9072000L, 3, 15, 100, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.fish.trophy.", 30.0F, 2000, (byte)14, 10000, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  984 */     createItemTemplate(1396, "buoy", "buoys", "almost full", "somewhat occupied", "half-full", "emptyish", "A buoy with a lamp, used to mark a fishing spot or a sea lane.", new short[] { 108, 21, 143, 48, 51, 52, 101, 44, 86, 116, 92, 199, 249 }, (short)245, (short)1, 0, 9072000L, 30, 30, 80, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.light.buoy.", 45.0F, 3000, (byte)14, 10000, true)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  995 */       .setSecondryItem("lamp")
/*  996 */       .setDyeAmountGrams(40, 312);
/*      */     
/*  998 */     (createItemTemplate(1397, "pearl", "pearls", "excellent", "good", "ok", "poor", "A pearl, can vary in color from white to those with a hint of color, often pink, to brown or black.", new short[] { 93, 48, 157 }, (short)563, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.pearl.", 99.0F, 500, (byte)52, 100000, true)).priceHalfSize = 200.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1008 */     (createItemTemplate(1398, "black pearl", "black pearls", "excellent", "good", "ok", "poor", "A perfectly black pearl.", new short[] { 93, 48, 157 }, (short)563, (short)1, 0, Long.MAX_VALUE, 1, 1, 1, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.gem.pearl.black.", 99.0F, 500, (byte)52, 200000, true)).priceHalfSize = 150.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1017 */     createItemTemplate(1399, "pearl necklace", "pearl necklaces", "new", "fancy", "ok", "old", "A delicate necklace made from a string of pearls with a black pearl in middle.", new short[] { 108, 157, 25, 153 }, (short)268, (short)1, 0, 24192000L, 1, 1, 2, -10, new byte[] { 36 }, "model.decoration.necklace.pearl.", 40.0F, 100, (byte)0, 7000, true);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplateCreatorFishing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
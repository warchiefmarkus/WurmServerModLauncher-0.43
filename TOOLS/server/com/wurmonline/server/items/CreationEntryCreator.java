/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.Features;
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
/*      */ public final class CreationEntryCreator
/*      */ {
/*   39 */   private static final Logger logger = Logger.getLogger(CreationEntryCreator.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean entriesCreated = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void createCreationEntries() {
/*   60 */     long start = System.nanoTime();
/*      */     
/*   62 */     createSimpleEntry(1005, 24, 9, 22, false, true, 100.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*   64 */     createSimpleEntry(1005, 7, 9, 860, false, true, 15.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*   66 */     createSimpleEntry(1005, 8, 9, 23, false, true, 100.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*   68 */     createSimpleEntry(1005, 685, 688, 691, false, true, 100.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*   70 */     createSimpleEntry(1005, 8, 688, 691, false, true, 100.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*   72 */     createSimpleEntry(1005, 8, 9, 711, false, true, 100.0F, false, false, CreationCategories.WEAPONS);
/*      */ 
/*      */     
/*   75 */     createSimpleEntry(1031, 8, 9, 459, false, true, 100.0F, false, false, CreationCategories.BOWS);
/*      */     
/*   77 */     createSimpleEntry(1031, 8, 9, 461, false, true, 100.0F, false, false, CreationCategories.BOWS);
/*      */     
/*   79 */     createSimpleEntry(1031, 8, 9, 460, false, true, 100.0F, false, false, CreationCategories.BOWS);
/*      */ 
/*      */     
/*   82 */     createSimpleEntry(1031, 685, 9, 459, false, true, 200.0F, false, false, CreationCategories.BOWS);
/*      */     
/*   84 */     createSimpleEntry(1031, 685, 9, 461, false, true, 200.0F, false, false, CreationCategories.BOWS);
/*      */     
/*   86 */     createSimpleEntry(1031, 685, 9, 460, false, true, 200.0F, false, false, CreationCategories.BOWS);
/*      */     
/*   88 */     createSimpleEntry(1007, 8, 9, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*   90 */     createSimpleEntry(1007, 685, 9, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*   92 */     createSimpleEntry(1007, 24, 9, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*   94 */     createSimpleEntry(1007, 87, 9, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*   96 */     createSimpleEntry(1007, 3, 9, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*   98 */     createSimpleEntry(1007, 90, 9, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  100 */     createSimpleEntry(1007, 8, 169, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  102 */     createSimpleEntry(1007, 8, 22, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  104 */     createSimpleEntry(1007, 685, 169, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  106 */     createSimpleEntry(1007, 24, 169, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  108 */     createSimpleEntry(1007, 87, 169, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  110 */     createSimpleEntry(1007, 3, 169, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  112 */     createSimpleEntry(1007, 90, 169, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  114 */     createSimpleEntry(1007, 7, 9, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/*  116 */     createSimpleEntry(1007, 7, 169, 36, false, true, 0.0F, false, false, CreationCategories.KINDLINGS);
/*      */ 
/*      */     
/*  119 */     createSimpleEntry(1007, 24, 22, 790, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/*  122 */     createSimpleEntry(1010, 143, 36, 37, false, true, 0.0F, false, true, CreationCategories.FIRE);
/*      */ 
/*      */     
/*  125 */     createSimpleEntry(1010, 169, 36, 37, true, true, 0.0F, false, true, CreationCategories.FIRE);
/*      */     
/*  127 */     createSimpleEntry(1011, 14, 130, 181, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/*  129 */     createSimpleEntry(1011, 14, 130, 182, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/*  131 */     createSimpleEntry(1011, 14, 130, 183, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/*  133 */     createSimpleEntry(1011, 14, 130, 769, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  135 */     createSimpleEntry(1011, 14, 130, 777, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  137 */     createSimpleEntry(1011, 14, 130, 789, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  139 */     if (Features.Feature.AMPHORA.isEnabled()) {
/*      */       
/*  141 */       createSimpleEntry(1011, 14, 130, 1019, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */       
/*  143 */       createSimpleEntry(1011, 14, 130, 1021, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     } 
/*      */ 
/*      */     
/*  147 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/*  149 */       createMetallicEntries(10015, 63, 46, 64, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */       
/*  151 */       createMetallicEntries(10015, 62, 46, 64, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */       
/*  154 */       createMetallicEntries(10010, 185, 46, 148, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  156 */       createMetallicEntries(10010, 64, 46, 147, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  158 */       createMetallicEntries(10010, 185, 46, 149, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  160 */       createMetallicEntries(10010, 185, 46, 269, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  162 */       createMetallicEntries(10010, 185, 46, 270, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */       
/*  165 */       createMetallicEntries(10011, 64, 46, 89, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  167 */       createMetallicEntries(10011, 64, 46, 523, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  169 */       createMetallicEntries(10011, 185, 46, 91, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  171 */       createMetallicEntries(10011, 185, 46, 88, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  173 */       createMetallicEntries(10011, 185, 46, 293, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  175 */       createMetallicEntries(10011, 185, 46, 295, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  177 */       createMetallicEntries(10011, 185, 46, 294, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  179 */       createMetallicEntries(10011, 185, 46, 708, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  184 */       createSimpleEntry(10015, 63, 46, 64, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */       
/*  186 */       createSimpleEntry(10015, 62, 46, 64, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */       
/*  189 */       createSimpleEntry(10010, 185, 46, 148, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  191 */       createSimpleEntry(10010, 64, 46, 147, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  193 */       createSimpleEntry(10010, 185, 694, 148, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  195 */       createSimpleEntry(10010, 64, 694, 147, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  197 */       createSimpleEntry(10010, 185, 837, 148, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  199 */       createSimpleEntry(10010, 64, 837, 147, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  201 */       createSimpleEntry(10010, 185, 698, 148, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  203 */       createSimpleEntry(10010, 64, 698, 147, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  205 */       createSimpleEntry(10010, 185, 205, 148, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  207 */       createSimpleEntry(10010, 64, 205, 147, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  209 */       createSimpleEntry(10010, 185, 694, 149, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  211 */       createSimpleEntry(10010, 185, 837, 149, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  213 */       createSimpleEntry(10010, 185, 698, 149, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  215 */       createSimpleEntry(10010, 185, 46, 149, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  217 */       createSimpleEntry(10010, 185, 205, 149, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  219 */       createSimpleEntry(10010, 185, 46, 269, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  221 */       createSimpleEntry(10010, 185, 46, 270, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  223 */       createSimpleEntry(10010, 185, 698, 270, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  225 */       createSimpleEntry(10010, 185, 205, 270, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  227 */       createSimpleEntry(10010, 185, 694, 270, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  229 */       createSimpleEntry(10010, 185, 837, 270, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */       
/*  232 */       createSimpleEntry(10011, 64, 46, 89, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  234 */       createSimpleEntry(10011, 64, 46, 523, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  236 */       createSimpleEntry(10011, 185, 46, 91, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  238 */       createSimpleEntry(10011, 185, 46, 88, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  240 */       createSimpleEntry(10011, 185, 46, 293, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  242 */       createSimpleEntry(10011, 185, 46, 295, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  244 */       createSimpleEntry(10011, 185, 46, 294, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  246 */       createSimpleEntry(10011, 185, 46, 708, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  248 */       createSimpleEntry(10011, 64, 694, 89, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  250 */       createSimpleEntry(10011, 64, 694, 523, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  252 */       createSimpleEntry(10011, 185, 694, 91, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  254 */       createSimpleEntry(10011, 185, 694, 88, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  256 */       createSimpleEntry(10011, 185, 694, 293, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  258 */       createSimpleEntry(10011, 185, 694, 295, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  260 */       createSimpleEntry(10011, 185, 694, 294, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  262 */       createSimpleEntry(10011, 185, 694, 708, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  264 */       createSimpleEntry(10011, 64, 837, 89, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  266 */       createSimpleEntry(10011, 64, 837, 523, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  268 */       createSimpleEntry(10011, 185, 837, 91, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  270 */       createSimpleEntry(10011, 185, 837, 88, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  272 */       createSimpleEntry(10011, 185, 837, 293, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  274 */       createSimpleEntry(10011, 185, 837, 295, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  276 */       createSimpleEntry(10011, 185, 837, 294, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  278 */       createSimpleEntry(10011, 185, 837, 708, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  280 */       createSimpleEntry(10011, 64, 205, 89, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  282 */       createSimpleEntry(10011, 64, 205, 523, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  284 */       createSimpleEntry(10011, 185, 205, 91, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  286 */       createSimpleEntry(10011, 185, 205, 88, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  288 */       createSimpleEntry(10011, 185, 205, 293, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  290 */       createSimpleEntry(10011, 185, 205, 295, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  292 */       createSimpleEntry(10011, 185, 205, 294, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  294 */       createSimpleEntry(10011, 185, 205, 708, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  296 */       createSimpleEntry(10011, 64, 698, 89, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  298 */       createSimpleEntry(10011, 64, 698, 523, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  300 */       createSimpleEntry(10011, 185, 698, 91, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  302 */       createSimpleEntry(10011, 185, 698, 88, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  304 */       createSimpleEntry(10011, 185, 698, 293, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  306 */       createSimpleEntry(10011, 185, 698, 295, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  308 */       createSimpleEntry(10011, 185, 698, 294, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  310 */       createSimpleEntry(10011, 185, 698, 708, false, true, 0.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */     } 
/*      */ 
/*      */     
/*  314 */     createSimpleEntry(1005, 8, 23, 99, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  316 */     createSimpleEntry(1005, 8, 23, 862, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  318 */     createSimpleEntry(1005, 8, 23, 561, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  320 */     createSimpleEntry(1005, 8, 23, 397, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  322 */     createSimpleEntry(1005, 8, 23, 396, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  324 */     createSimpleEntry(1005, 685, 23, 99, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  326 */     createSimpleEntry(1005, 685, 23, 561, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  328 */     createSimpleEntry(1005, 685, 23, 397, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  330 */     createSimpleEntry(1005, 685, 23, 396, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/*  333 */     createSimpleEntry(1032, 685, 23, 454, false, true, 0.0F, false, false, CreationCategories.FLETCHING);
/*      */     
/*  335 */     createSimpleEntry(1032, 8, 23, 454, false, true, 0.0F, false, false, CreationCategories.FLETCHING);
/*      */     
/*  337 */     createSimpleEntry(1032, 451, 454, 455, true, true, 0.0F, false, false, CreationCategories.FLETCHING);
/*      */     
/*  339 */     createSimpleEntry(1032, 452, 454, 456, true, true, 0.0F, false, false, CreationCategories.FLETCHING);
/*      */ 
/*      */     
/*  342 */     createSimpleEntry(1016, 99, 148, 21, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  344 */     createSimpleEntry(1016, 99, 147, 80, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  346 */     createSimpleEntry(1016, 99, 149, 81, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  348 */     createSimpleEntry(1016, 99, 269, 267, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  350 */     createSimpleEntry(1016, 709, 711, 705, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  352 */     createSimpleEntry(1016, 710, 709, 707, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  354 */     createSimpleEntry(1016, 711, 708, 706, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  356 */     createSimpleEntry(1016, 23, 89, 3, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  358 */     createSimpleEntry(1016, 23, 91, 90, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  360 */     createSimpleEntry(1016, 23, 88, 87, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  362 */     createSimpleEntry(1016, 23, 270, 268, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/*  365 */     createSimpleEntry(1016, 23, 293, 290, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  367 */     createSimpleEntry(1016, 23, 295, 292, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  369 */     createSimpleEntry(1016, 23, 294, 291, true, true, 0.0F, false, false, CreationCategories.WEAPONS);
/*      */     
/*  371 */     createSimpleEntry(1005, 8, 9, 139, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  373 */     createSimpleEntry(1005, 685, 9, 139, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/*  376 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/*  378 */       createMetallicEntries(1016, 185, 205, 710, false, true, 10.0F, false, false, CreationCategories.WEAPONS);
/*      */       
/*  380 */       createMetallicEntries(10015, 64, 46, 215, false, true, 400.0F, false, false, CreationCategories.TOOLS);
/*      */       
/*  382 */       createMetallicEntries(10015, 64, 46, 259, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  384 */       createMetallicEntries(10015, 64, 46, 257, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  386 */       createMetallicEntries(10015, 64, 46, 258, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  391 */       createSimpleEntry(1016, 185, 205, 710, false, true, 10.0F, false, false, CreationCategories.WEAPONS);
/*      */       
/*  393 */       createSimpleEntry(10015, 64, 46, 215, false, true, 400.0F, false, false, CreationCategories.TOOLS);
/*      */       
/*  395 */       createSimpleEntry(10015, 64, 46, 259, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  397 */       createSimpleEntry(10015, 64, 46, 257, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  399 */       createSimpleEntry(10015, 64, 46, 258, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  401 */       createSimpleEntry(10015, 64, 45, 259, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  403 */       createSimpleEntry(10015, 64, 45, 257, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  405 */       createSimpleEntry(10015, 64, 45, 258, false, true, 10.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  407 */       createSimpleEntry(10015, 64, 47, 216, false, true, 400.0F, false, false, CreationCategories.TOOLS);
/*      */     } 
/*      */     
/*  410 */     createSimpleEntry(10015, 185, 46, 681, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  412 */     createSimpleEntry(10015, 446, 205, 143, true, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  414 */     createSimpleEntry(10015, 64, 47, 772, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/*  417 */     createSimpleEntry(10015, 64, 46, 773, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/*  420 */     createSimpleEntry(10015, 64, 220, 1298, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  422 */     createSimpleEntry(10015, 64, 49, 1299, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/*  425 */     createSimpleEntry(10015, 64, 205, 597, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/*  428 */     createSimpleEntry(10015, 64, 44, 599, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  430 */     createSimpleEntry(10015, 64, 45, 598, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  432 */     createSimpleEntry(10015, 185, 47, 838, false, true, 0.0F, false, false, 20, 30.0D, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */ 
/*      */     
/*  436 */     createSimpleEntry(10091, 128, 169, 1270, false, true, 0.0F, true, false, CreationCategories.WRITING);
/*      */     
/*  438 */     createSimpleEntry(10091, 747, 745, 748, false, true, 0.0F, false, false, CreationCategories.WRITING);
/*      */     
/*  440 */     createSimpleEntry(10091, 747, 1270, 1272, false, true, 0.0F, false, false, CreationCategories.WRITING);
/*      */     
/*  442 */     createSimpleEntry(10092, 774, 743, 756, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/*  444 */     createSimpleEntry(10092, 774, 620, 756, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/*  447 */     createSimpleEntry(10007, 8, 743, 749, false, true, 0.0F, false, false, CreationCategories.WRITING);
/*      */ 
/*      */     
/*  450 */     createSimpleEntry(10016, 216, 213, 113, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  452 */     createSimpleEntry(10016, 215, 213, 113, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  454 */     createSimpleEntry(10016, 216, 213, 109, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  456 */     createSimpleEntry(10016, 215, 213, 109, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  458 */     createSimpleEntry(10016, 216, 213, 1427, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  460 */     createSimpleEntry(10016, 215, 213, 1427, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  462 */     createSimpleEntry(10016, 215, 213, 704, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */     
/*  464 */     createSimpleEntry(10016, 216, 213, 704, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */     
/*  466 */     createSimpleEntry(10016, 215, 213, 1425, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  468 */     createSimpleEntry(10016, 216, 213, 1425, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/*  471 */     createSimpleEntry(10016, 216, 213, 486, false, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/*  473 */     createSimpleEntry(10016, 215, 213, 486, false, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */ 
/*      */     
/*  476 */     createSimpleEntry(10016, 216, 213, 110, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  478 */     createSimpleEntry(10016, 215, 213, 110, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  480 */     createSimpleEntry(10016, 216, 213, 114, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  482 */     createSimpleEntry(10016, 215, 213, 114, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  484 */     createSimpleEntry(10016, 216, 213, 1426, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  486 */     createSimpleEntry(10016, 215, 213, 1426, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  488 */     createSimpleEntry(10016, 215, 213, 2, false, true, 0.0F, false, false, CreationCategories.BAGS);
/*      */ 
/*      */     
/*  491 */     createSimpleEntry(10016, 215, 213, 555, false, true, 0.0F, false, false, CreationCategories.SAILS);
/*      */     
/*  493 */     createSimpleEntry(10016, 215, 213, 591, false, true, 0.0F, false, false, CreationCategories.SAILS);
/*      */     
/*  495 */     createSimpleEntry(10016, 215, 213, 554, false, true, 0.0F, false, false, CreationCategories.SAILS);
/*      */ 
/*      */     
/*  498 */     createSimpleEntry(10016, 216, 213, 555, false, true, 0.0F, false, false, CreationCategories.SAILS);
/*      */     
/*  500 */     createSimpleEntry(10016, 216, 213, 591, false, true, 0.0F, false, false, CreationCategories.SAILS);
/*      */     
/*  502 */     createSimpleEntry(10016, 216, 213, 554, false, true, 0.0F, false, false, CreationCategories.SAILS);
/*      */     
/*  504 */     createSimpleEntry(10016, 215, 213, 831, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  506 */     createSimpleEntry(10016, 216, 213, 831, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/*  509 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/*  511 */       createMetallicEntries(10015, 185, 46, 627, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */       
/*  513 */       createMetallicEntries(10015, 185, 46, 623, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */ 
/*      */       
/*  516 */       createMetallicEntries(10015, 64, 46, 127, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  518 */       createMetallicEntries(10010, 64, 46, 154, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  520 */       createMetallicEntries(10010, 64, 46, 389, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  522 */       createMetallicEntries(10010, 64, 46, 494, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  524 */       createMetallicEntries(10010, 64, 46, 709, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  526 */       createMetallicEntries(10010, 64, 46, 391, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  528 */       createMetallicEntries(10010, 64, 46, 393, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  530 */       createMetallicEntries(10010, 64, 46, 395, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  532 */       createMetallicEntries(10010, 64, 46, 125, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  534 */       createMetallicEntries(10010, 64, 46, 126, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */       
/*  537 */       createMetallicEntries(10015, 185, 46, 75, false, true, 0.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  539 */       createMetallicEntries(10015, 185, 46, 351, false, true, 0.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  541 */       createMetallicEntries(10015, 64, 46, 734, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  543 */       createMetallicEntries(10015, 64, 46, 720, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  545 */       createMetallicEntries(10015, 185, 46, 721, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  547 */       createMetallicEntries(10015, 64, 46, 735, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  549 */       createMetallicEntries(10015, 185, 46, 350, false, true, 0.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  554 */       createSimpleEntry(10015, 185, 46, 627, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */       
/*  556 */       createSimpleEntry(10015, 185, 46, 623, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */       
/*  558 */       createSimpleEntry(10015, 185, 205, 627, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */       
/*  560 */       createSimpleEntry(10015, 185, 205, 623, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */       
/*  562 */       createSimpleEntry(10043, 185, 44, 623, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */       
/*  564 */       createSimpleEntry(10043, 185, 45, 623, false, true, 0.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */ 
/*      */       
/*  567 */       createSimpleEntry(10015, 64, 46, 127, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  569 */       createSimpleEntry(10010, 64, 46, 154, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  571 */       createSimpleEntry(10010, 64, 46, 389, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  573 */       createSimpleEntry(10010, 64, 46, 494, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  575 */       createSimpleEntry(10010, 64, 46, 709, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  577 */       createSimpleEntry(10010, 64, 205, 709, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  579 */       createSimpleEntry(10010, 64, 698, 709, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  581 */       createSimpleEntry(10010, 64, 694, 709, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  583 */       createSimpleEntry(10010, 64, 837, 709, false, true, 0.0F, false, false, CreationCategories.BLADES);
/*      */       
/*  585 */       createSimpleEntry(10010, 64, 46, 391, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  587 */       createSimpleEntry(10010, 64, 46, 393, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  589 */       createSimpleEntry(10010, 64, 205, 393, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  591 */       createSimpleEntry(10010, 64, 694, 393, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  593 */       createSimpleEntry(10010, 64, 837, 393, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  595 */       createSimpleEntry(10010, 64, 698, 393, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  597 */       createSimpleEntry(10010, 64, 46, 395, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  599 */       createSimpleEntry(10015, 185, 46, 75, false, true, 0.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  601 */       createSimpleEntry(10015, 185, 46, 351, false, true, 0.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */       
/*  603 */       createSimpleEntry(10015, 64, 46, 734, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  605 */       createSimpleEntry(10015, 64, 221, 720, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  607 */       createSimpleEntry(10015, 185, 223, 721, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  609 */       createSimpleEntry(10015, 64, 46, 735, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  611 */       createSimpleEntry(10015, 185, 46, 350, false, true, 0.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */ 
/*      */       
/*  614 */       createSimpleEntry(10010, 64, 46, 125, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  616 */       createSimpleEntry(10010, 64, 46, 126, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  618 */       createSimpleEntry(10010, 64, 205, 126, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  620 */       createSimpleEntry(10010, 64, 698, 126, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  622 */       createSimpleEntry(10010, 64, 694, 126, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/*  624 */       createSimpleEntry(10010, 64, 837, 126, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     } 
/*      */ 
/*      */     
/*  628 */     createSimpleEntry(10015, 99, 393, 392, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  630 */     createSimpleEntry(10015, 395, 395, 394, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  632 */     createSimpleEntry(10015, 185, 205, 582, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/*  634 */     createSimpleEntry(10015, 99, 154, 97, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  636 */     createSimpleEntry(10015, 99, 389, 388, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  638 */     createSimpleEntry(10015, 99, 494, 493, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  640 */     createSimpleEntry(10015, 99, 391, 390, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  642 */     createSimpleEntry(10015, 23, 127, 62, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  644 */     createSimpleEntry(10010, 64, 45, 793, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/*  647 */     createSimpleEntry(10042, 128, 141, 73, true, true, 0.0F, false, false, CreationCategories.ALCHEMY);
/*      */     
/*  649 */     createSimpleEntry(10042, 128, 436, 437, true, true, 0.0F, false, false, CreationCategories.ALCHEMY);
/*      */     
/*  651 */     createSimpleEntry(10042, 437, 46, 431, true, true, 0.0F, false, false, CreationCategories.DYES);
/*      */     
/*  653 */     createSimpleEntry(10042, 128, 48, 432, true, true, 0.0F, false, false, CreationCategories.DYES);
/*      */     
/*  655 */     createSimpleEntry(10042, 128, 47, 435, true, true, 0.0F, false, false, CreationCategories.DYES);
/*      */     
/*  657 */     createSimpleEntry(10042, 128, 439, 433, true, true, 0.0F, false, false, CreationCategories.DYES);
/*      */     
/*  659 */     createSimpleEntry(10042, 128, 440, 434, true, true, 0.0F, false, false, CreationCategories.DYES);
/*      */     
/*  661 */     createSimpleEntry(10042, 140, 214, 133, true, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */     
/*  663 */     createSimpleEntry(10042, 356, 140, 650, true, true, 0.0F, false, false, CreationCategories.HEALING);
/*      */     
/*  665 */     createSimpleEntry(10042, 1254, 214, 133, true, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */ 
/*      */     
/*  668 */     createSimpleEntry(1013, 130, 298, 492, true, true, 0.0F, true, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */ 
/*      */     
/*  672 */     createSimpleEntry(1016, 99, 125, 93, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*  674 */     createSimpleEntry(1016, 99, 126, 8, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  705 */     createSimpleEntry(1005, 24, 9, 632, false, true, 0.0F, false, false, CreationCategories.CART_PARTS);
/*      */     
/*  707 */     createSimpleEntry(10016, 139, 144, 214, false, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/*  709 */     createSimpleEntry(10016, 139, 171, 214, false, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/*  711 */     createSimpleEntry(10016, 226, 214, 213, false, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/*  713 */     createSimpleEntry(10016, 226, 214, 646, false, true, 0.0F, false, false, CreationCategories.RUGS);
/*      */     
/*  715 */     createSimpleEntry(10016, 226, 214, 645, false, true, 0.0F, false, false, CreationCategories.RUGS);
/*      */     
/*  717 */     createSimpleEntry(10016, 226, 214, 644, false, true, 0.0F, false, false, CreationCategories.RUGS);
/*      */     
/*  719 */     createSimpleEntry(10016, 226, 214, 639, false, true, 0.0F, false, false, CreationCategories.RUGS);
/*      */     
/*  721 */     createSimpleEntry(10016, 213, 23, 487, true, true, 0.0F, false, false, CreationCategories.FLAGS);
/*      */     
/*  723 */     createSimpleEntry(10016, 213, 23, 577, true, true, 0.0F, false, false, CreationCategories.FLAGS);
/*      */     
/*  725 */     createSimpleEntry(10016, 213, 23, 579, true, true, 0.0F, false, false, CreationCategories.FLAGS);
/*      */     
/*  727 */     createSimpleEntry(10016, 213, 23, 578, true, true, 0.0F, false, false, CreationCategories.FLAGS);
/*      */     
/*  729 */     createSimpleEntry(10016, 213, 23, 999, true, true, 0.0F, false, false, 10, 35.0D, CreationCategories.FLAGS);
/*      */ 
/*      */     
/*  732 */     createSimpleEntry(1005, 685, 23, 862, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/*  735 */     createSimpleEntry(1020, 169, 385, 652, true, true, 0.0F, false, false, CreationCategories.DECORATION);
/*      */ 
/*      */     
/*  738 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/*  740 */       createMetallicEntries(10013, 185, 46, 284, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  742 */       createMetallicEntries(10013, 185, 46, 280, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  744 */       createMetallicEntries(10013, 185, 46, 281, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  746 */       createMetallicEntries(10013, 185, 46, 282, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  748 */       createMetallicEntries(10013, 185, 46, 283, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  750 */       createMetallicEntries(10013, 185, 46, 285, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  752 */       createMetallicEntries(10013, 185, 46, 286, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  754 */       createMetallicEntries(10013, 185, 46, 287, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */       
/*  757 */       createMetallicEntries(10012, 185, 46, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */ 
/*      */       
/*  760 */       createMetallicEntries(10043, 185, 46, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  765 */       createSimpleEntry(10013, 185, 205, 284, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  767 */       createSimpleEntry(10013, 185, 205, 280, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  769 */       createSimpleEntry(10013, 185, 205, 281, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  771 */       createSimpleEntry(10013, 185, 205, 282, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  773 */       createSimpleEntry(10013, 185, 205, 283, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  775 */       createSimpleEntry(10013, 185, 205, 285, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  777 */       createSimpleEntry(10013, 185, 205, 286, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  779 */       createSimpleEntry(10013, 185, 205, 287, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */       
/*  782 */       createSimpleEntry(10013, 185, 698, 284, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  784 */       createSimpleEntry(10013, 185, 698, 280, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  786 */       createSimpleEntry(10013, 185, 698, 281, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  788 */       createSimpleEntry(10013, 185, 698, 282, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  790 */       createSimpleEntry(10013, 185, 698, 283, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  792 */       createSimpleEntry(10013, 185, 698, 285, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  794 */       createSimpleEntry(10013, 185, 698, 286, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  796 */       createSimpleEntry(10013, 185, 698, 287, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */       
/*  799 */       createSimpleEntry(10013, 185, 694, 284, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  801 */       createSimpleEntry(10013, 185, 694, 280, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  803 */       createSimpleEntry(10013, 185, 694, 281, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  805 */       createSimpleEntry(10013, 185, 694, 282, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  807 */       createSimpleEntry(10013, 185, 694, 283, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  809 */       createSimpleEntry(10013, 185, 694, 285, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  811 */       createSimpleEntry(10013, 185, 694, 286, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  813 */       createSimpleEntry(10013, 185, 694, 287, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */       
/*  816 */       createSimpleEntry(10013, 185, 837, 284, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  818 */       createSimpleEntry(10013, 185, 837, 280, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  820 */       createSimpleEntry(10013, 185, 837, 281, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  822 */       createSimpleEntry(10013, 185, 837, 282, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  824 */       createSimpleEntry(10013, 185, 837, 283, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  826 */       createSimpleEntry(10013, 185, 837, 285, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  828 */       createSimpleEntry(10013, 185, 837, 286, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  830 */       createSimpleEntry(10013, 185, 837, 287, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */       
/*  833 */       createSimpleEntry(10013, 185, 46, 284, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  835 */       createSimpleEntry(10013, 185, 46, 280, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  837 */       createSimpleEntry(10013, 185, 46, 281, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  839 */       createSimpleEntry(10013, 185, 46, 282, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  841 */       createSimpleEntry(10013, 185, 46, 283, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  843 */       createSimpleEntry(10013, 185, 46, 285, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  845 */       createSimpleEntry(10013, 185, 46, 286, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */       
/*  847 */       createSimpleEntry(10013, 185, 46, 287, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */       
/*  850 */       createSimpleEntry(10012, 185, 205, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  852 */       createSimpleEntry(10012, 185, 46, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  854 */       createSimpleEntry(10012, 185, 694, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  856 */       createSimpleEntry(10012, 185, 837, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  858 */       createSimpleEntry(10012, 185, 698, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  860 */       createSimpleEntry(10012, 185, 47, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  862 */       createSimpleEntry(10012, 185, 223, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  864 */       createSimpleEntry(10012, 185, 45, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */       
/*  866 */       createSimpleEntry(10012, 185, 44, 288, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */ 
/*      */       
/*  869 */       createSimpleEntry(10043, 185, 205, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  871 */       createSimpleEntry(10043, 185, 46, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  873 */       createSimpleEntry(10043, 185, 47, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  875 */       createSimpleEntry(10043, 185, 223, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  877 */       createSimpleEntry(10043, 185, 45, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  879 */       createSimpleEntry(10043, 185, 44, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  881 */       createSimpleEntry(10043, 185, 694, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  883 */       createSimpleEntry(10043, 185, 837, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */       
/*  885 */       createSimpleEntry(10043, 185, 698, 326, false, true, 10.0F, false, false, CreationCategories.DECORATION);
/*      */     } 
/*      */ 
/*      */     
/*  889 */     createSimpleEntry(10013, 185, 372, 478, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  891 */     createSimpleEntry(10013, 185, 372, 474, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  893 */     createSimpleEntry(10013, 185, 372, 475, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  895 */     createSimpleEntry(10013, 185, 372, 476, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  897 */     createSimpleEntry(10013, 185, 372, 477, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */     
/*  900 */     createSimpleEntry(10012, 185, 288, 274, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  902 */     createSimpleEntry(10012, 185, 288, 279, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  904 */     createSimpleEntry(10012, 185, 288, 278, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  906 */     createSimpleEntry(10012, 185, 288, 275, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  908 */     createSimpleEntry(10012, 185, 288, 276, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  910 */     createSimpleEntry(10012, 185, 288, 277, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  912 */     createSimpleEntry(10012, 185, 288, 703, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */ 
/*      */     
/*  915 */     createSimpleEntry(10017, 73, 71, 72, true, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/*  917 */     createSimpleEntry(10017, 215, 172, 72, false, true, 50.0F, false, false, CreationCategories.RESOURCES);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  923 */     createSimpleEntry(10017, 215, 72, 105, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  925 */     createSimpleEntry(10017, 215, 72, 107, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  927 */     createSimpleEntry(10017, 215, 72, 103, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  929 */     createSimpleEntry(10017, 215, 72, 108, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  931 */     createSimpleEntry(10017, 215, 72, 104, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  933 */     createSimpleEntry(10017, 215, 72, 106, false, true, 10.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  935 */     createSimpleEntry(10017, 215, 72, 102, false, true, 10.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/*  937 */     createSimpleEntry(10017, 394, 72, 100, false, true, 10.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/*  939 */     createSimpleEntry(10017, 215, 371, 469, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  941 */     createSimpleEntry(10017, 215, 371, 470, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  943 */     createSimpleEntry(10017, 215, 371, 472, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  945 */     createSimpleEntry(10017, 215, 371, 471, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  947 */     createSimpleEntry(10017, 215, 371, 473, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/*  949 */     createSimpleEntry(10017, 215, 371, 468, false, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */     
/*  952 */     createSimpleEntry(10017, 215, 72, 79, false, true, 10.0F, false, false, CreationCategories.BAGS);
/*      */     
/*  954 */     createSimpleEntry(10017, 215, 72, 1, false, true, 10.0F, false, false, CreationCategories.BAGS);
/*      */     
/*  956 */     createSimpleEntry(10017, 215, 72, 462, false, true, 10.0F, false, false, CreationCategories.BAGS);
/*      */ 
/*      */     
/*  959 */     createSimpleEntry(10017, 215, 72, 629, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */     
/*  961 */     createSimpleEntry(10017, 215, 72, 630, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */     
/*  963 */     createSimpleEntry(10017, 215, 72, 628, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */     
/*  965 */     createSimpleEntry(10017, 215, 72, 631, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */     
/*  967 */     createSimpleEntry(10017, 215, 72, 625, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */     
/*  969 */     createSimpleEntry(10017, 215, 72, 626, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */ 
/*      */     
/*  972 */     createSimpleEntry(10017, 215, 72, 1332, false, true, 10.0F, false, false, CreationCategories.ANIMAL_EQUIPMENT_PART);
/*      */ 
/*      */     
/*  975 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/*  977 */       createMetallicEntries(10015, 64, 46, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  979 */       createMetallicEntries(10015, 64, 46, 517, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/*  981 */       createMetallicEntries(10015, 64, 46, 444, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */       
/*  984 */       createMetallicEntries(10011, 64, 46, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/*  986 */       createMetallicEntries(10011, 64, 46, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */ 
/*      */       
/*  989 */       createMetallicEntries(10015, 185, 46, 135, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/*  991 */       createMetallicEntries(10015, 185, 46, 497, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/*  993 */       createMetallicEntries(10015, 185, 46, 675, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/*  995 */       createMetallicEntries(10015, 185, 46, 660, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/*  997 */       createMetallicEntries(10015, 185, 46, 674, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */ 
/*      */       
/* 1000 */       createMetallicEntries(10011, 64, 46, 123, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */       
/* 1003 */       createMetallicEntries(10015, 64, 46, 219, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */       
/* 1005 */       createMetallicEntries(10015, 64, 46, 701, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */       
/* 1007 */       createMetallicEntries(10015, 64, 46, 124, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1009 */       createMetallicEntries(10015, 64, 46, 24, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1014 */       createSimpleEntry(10015, 64, 46, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1016 */       createSimpleEntry(10015, 64, 205, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1018 */       createSimpleEntry(10015, 64, 47, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1020 */       createSimpleEntry(10015, 64, 45, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1022 */       createSimpleEntry(10015, 64, 44, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1024 */       createSimpleEntry(10015, 64, 223, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1026 */       createSimpleEntry(10015, 64, 694, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1028 */       createSimpleEntry(10015, 64, 837, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1030 */       createSimpleEntry(10015, 64, 698, 131, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */       
/* 1033 */       createSimpleEntry(10015, 64, 46, 517, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1035 */       createSimpleEntry(10015, 64, 205, 517, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1037 */       createSimpleEntry(10015, 64, 47, 517, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1039 */       createSimpleEntry(10015, 64, 45, 517, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1041 */       createSimpleEntry(10015, 64, 44, 517, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1043 */       createSimpleEntry(10015, 64, 223, 517, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */       
/* 1046 */       createSimpleEntry(10015, 64, 46, 444, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1048 */       createSimpleEntry(10015, 64, 205, 444, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1050 */       createSimpleEntry(10015, 64, 47, 444, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1052 */       createSimpleEntry(10015, 64, 45, 444, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1054 */       createSimpleEntry(10015, 64, 44, 444, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1056 */       createSimpleEntry(10015, 64, 223, 444, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */       
/* 1059 */       createSimpleEntry(10011, 64, 46, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1061 */       createSimpleEntry(10011, 64, 698, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1063 */       createSimpleEntry(10011, 64, 694, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1065 */       createSimpleEntry(10011, 64, 837, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1067 */       createSimpleEntry(10011, 64, 205, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1069 */       createSimpleEntry(10011, 64, 47, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1071 */       createSimpleEntry(10011, 64, 45, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1073 */       createSimpleEntry(10011, 64, 44, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1075 */       createSimpleEntry(10011, 64, 223, 451, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1077 */       createSimpleEntry(10011, 64, 46, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1079 */       createSimpleEntry(10011, 64, 698, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1081 */       createSimpleEntry(10011, 64, 694, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1083 */       createSimpleEntry(10011, 64, 837, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1085 */       createSimpleEntry(10011, 64, 205, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1087 */       createSimpleEntry(10011, 64, 47, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1089 */       createSimpleEntry(10011, 64, 45, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1091 */       createSimpleEntry(10011, 64, 44, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 1093 */       createSimpleEntry(10011, 64, 223, 452, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */ 
/*      */       
/* 1096 */       createSimpleEntry(10015, 185, 46, 135, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1098 */       createSimpleEntry(10015, 185, 46, 497, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1100 */       createSimpleEntry(10015, 185, 221, 497, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1102 */       createSimpleEntry(10015, 185, 46, 675, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1104 */       createSimpleEntry(10015, 185, 46, 660, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */ 
/*      */       
/* 1107 */       createSimpleEntry(10015, 185, 47, 674, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1109 */       createSimpleEntry(10015, 185, 223, 674, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1111 */       createSimpleEntry(10015, 185, 221, 674, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1113 */       createSimpleEntry(10015, 185, 44, 674, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1115 */       createSimpleEntry(10015, 185, 45, 674, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */ 
/*      */       
/* 1118 */       createSimpleEntry(10011, 64, 46, 123, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1120 */       createSimpleEntry(10011, 64, 205, 123, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1122 */       createSimpleEntry(10011, 64, 694, 123, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1124 */       createSimpleEntry(10011, 64, 837, 123, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1126 */       createSimpleEntry(10011, 64, 698, 123, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */       
/* 1129 */       createSimpleEntry(10015, 64, 46, 219, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */       
/* 1131 */       createSimpleEntry(10015, 64, 46, 701, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */       
/* 1133 */       createSimpleEntry(10015, 64, 46, 124, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1135 */       createSimpleEntry(10015, 64, 46, 24, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     } 
/*      */ 
/*      */     
/* 1139 */     createSimpleEntry(10015, 64, 46, 217, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1141 */     createSimpleEntry(10015, 64, 46, 218, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1143 */     createSimpleEntry(10015, 23, 444, 441, true, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1145 */     createSimpleEntry(1005, 318, 23, 647, true, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 1148 */     createSimpleEntry(10017, 131, 105, 116, true, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/* 1150 */     createSimpleEntry(10017, 131, 107, 117, true, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/* 1152 */     createSimpleEntry(10017, 131, 103, 119, true, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/* 1154 */     createSimpleEntry(10017, 131, 108, 118, true, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/* 1156 */     createSimpleEntry(10017, 131, 104, 120, true, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */     
/* 1158 */     createSimpleEntry(10017, 131, 106, 115, true, true, 0.0F, false, false, CreationCategories.ARMOUR);
/*      */ 
/*      */     
/* 1161 */     createSimpleEntry(1005, 23, 156, 63, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1163 */     createSimpleEntry(1005, 8, 23, 156, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1165 */     createSimpleEntry(1005, 685, 23, 156, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1167 */     createSimpleEntry(10015, 185, 205, 609, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1169 */     createSimpleEntry(10015, 23, 523, 7, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 1172 */     createSimpleEntry(10017, 100, 99, 101, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 1175 */     createSimpleEntry(10015, 9, 497, 496, true, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */     
/* 1177 */     createSimpleEntry(10015, 9, 660, 657, true, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */     
/* 1179 */     createSimpleEntry(10015, 9, 674, 658, true, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */     
/* 1181 */     createSimpleEntry(10015, 185, 221, 136, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */     
/* 1183 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/* 1185 */       for (int sourceTemplateId : ItemFactory.metalLumpList)
/*      */       {
/*      */         
/*      */         try {
/* 1189 */           ItemTemplate lump = ItemTemplateFactory.getInstance().getTemplate(sourceTemplateId);
/* 1190 */           CreationEntry temp = createSimpleEntry(10015, sourceTemplateId, 675, 659, true, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */           
/* 1192 */           temp.objectTargetMaterial = lump.getMaterial();
/*      */         }
/* 1194 */         catch (NoSuchTemplateException noSuchTemplateException) {}
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1202 */       createSimpleEntry(10015, 46, 675, 659, true, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */     } 
/*      */ 
/*      */     
/* 1206 */     createSimpleEntry(10015, 23, 123, 20, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1208 */     createSimpleEntry(10015, 735, 721, 718, true, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1210 */     createSimpleEntry(10015, 23, 686, 687, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1212 */     createSimpleEntry(10015, 691, 686, 687, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 1215 */     createSimpleEntry(10015, 23, 1010, 1011, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1217 */     createSimpleEntry(10015, 691, 1010, 1011, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1219 */     createSimpleEntry(10015, 23, 124, 27, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 1222 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/* 1224 */       createMetallicEntries(10014, 185, 46, 86, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1226 */       createMetallicEntries(10014, 185, 46, 4, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1228 */       createMetallicEntries(10014, 185, 46, 83, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */ 
/*      */       
/* 1231 */       createMetallicEntries(10015, 185, 46, 121, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */       
/* 1234 */       createMetallicEntries(10043, 64, 46, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1236 */       createMetallicEntries(10043, 64, 46, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1238 */       createMetallicEntries(10043, 64, 46, 229, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1243 */       createSimpleEntry(10014, 185, 46, 86, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1245 */       createSimpleEntry(10014, 185, 46, 4, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1247 */       createSimpleEntry(10014, 185, 46, 83, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1249 */       createSimpleEntry(10014, 185, 698, 86, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1251 */       createSimpleEntry(10014, 185, 698, 4, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1253 */       createSimpleEntry(10014, 185, 698, 83, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1255 */       createSimpleEntry(10014, 185, 205, 86, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1257 */       createSimpleEntry(10014, 185, 205, 4, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1259 */       createSimpleEntry(10014, 185, 205, 83, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1261 */       createSimpleEntry(10014, 185, 694, 86, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1263 */       createSimpleEntry(10014, 185, 694, 4, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1265 */       createSimpleEntry(10014, 185, 694, 83, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1267 */       createSimpleEntry(10014, 185, 837, 86, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1269 */       createSimpleEntry(10014, 185, 837, 4, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */       
/* 1271 */       createSimpleEntry(10014, 185, 837, 83, false, true, 0.0F, false, false, CreationCategories.SHIELDS);
/*      */ 
/*      */       
/* 1274 */       createSimpleEntry(10015, 185, 46, 121, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1276 */       createSimpleEntry(10015, 185, 205, 121, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1278 */       createSimpleEntry(10015, 185, 698, 121, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1280 */       createSimpleEntry(10015, 185, 694, 121, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */       
/* 1282 */       createSimpleEntry(10015, 185, 837, 121, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */       
/* 1285 */       createSimpleEntry(10043, 64, 44, 229, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */       
/* 1287 */       createSimpleEntry(10043, 64, 45, 229, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */ 
/*      */       
/* 1290 */       createSimpleEntry(10043, 64, 44, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1292 */       createSimpleEntry(10043, 64, 45, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1294 */       createSimpleEntry(10043, 64, 46, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1296 */       createSimpleEntry(10043, 64, 698, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1298 */       createSimpleEntry(10043, 64, 694, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1300 */       createSimpleEntry(10043, 64, 837, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1302 */       createSimpleEntry(10043, 64, 47, 228, false, true, 0.0F, false, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */       
/* 1304 */       createSimpleEntry(10043, 64, 44, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1306 */       createSimpleEntry(10043, 64, 45, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1308 */       createSimpleEntry(10043, 64, 46, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1310 */       createSimpleEntry(10043, 64, 47, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1312 */       createSimpleEntry(10043, 64, 223, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1314 */       createSimpleEntry(10043, 64, 221, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1316 */       createSimpleEntry(10043, 64, 49, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1318 */       createSimpleEntry(10043, 64, 205, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1320 */       createSimpleEntry(10043, 64, 694, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1322 */       createSimpleEntry(10043, 64, 837, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */       
/* 1324 */       createSimpleEntry(10043, 64, 698, 232, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     } 
/*      */ 
/*      */     
/* 1328 */     createSimpleEntry(10015, 185, 46, 188, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1330 */     createSimpleEntry(10015, 23, 121, 25, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1332 */     createSimpleEntry(1005, 23, 689, 690, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1334 */     createSimpleEntry(1005, 691, 689, 690, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1336 */     createSimpleEntry(10041, 204, 46, 205, true, true, 0.0F, true, false, CreationCategories.RESOURCES);
/*      */     
/* 1338 */     createSimpleEntry(10041, 220, 47, 223, true, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/* 1340 */     createSimpleEntry(10041, 48, 47, 221, true, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/* 1342 */     createSimpleEntry(10043, 64, 44, 227, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1344 */     createSimpleEntry(10043, 64, 45, 227, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1346 */     createSimpleEntry(10043, 64, 1411, 227, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1348 */     createSimpleEntry(10043, 64, 44, 505, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1350 */     createSimpleEntry(10043, 64, 45, 505, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1352 */     createSimpleEntry(10043, 64, 1411, 505, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1354 */     createSimpleEntry(10043, 64, 44, 506, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1356 */     createSimpleEntry(10043, 64, 45, 506, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1358 */     createSimpleEntry(10043, 64, 1411, 506, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1360 */     createSimpleEntry(10043, 64, 44, 507, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1362 */     createSimpleEntry(10043, 64, 45, 507, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1364 */     createSimpleEntry(10043, 64, 1411, 507, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1366 */     createSimpleEntry(10043, 64, 44, 508, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1368 */     createSimpleEntry(10043, 64, 45, 508, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1370 */     createSimpleEntry(10043, 64, 1411, 508, false, true, 0.0F, false, false, CreationCategories.STATUETTES);
/*      */     
/* 1372 */     createSimpleEntry(10043, 64, 44, 230, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1374 */     createSimpleEntry(10043, 64, 45, 230, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1376 */     createSimpleEntry(10043, 64, 1411, 230, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1378 */     createSimpleEntry(10043, 64, 44, 231, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1380 */     createSimpleEntry(10043, 64, 45, 231, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1382 */     createSimpleEntry(10043, 64, 1411, 231, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1384 */     createSimpleEntry(10043, 64, 694, 231, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1386 */     createSimpleEntry(10043, 64, 837, 231, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1388 */     createSimpleEntry(10043, 64, 698, 231, false, true, 0.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1390 */     createSimpleEntry(10043, 64, 44, 297, false, true, 100.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1392 */     createSimpleEntry(10043, 64, 45, 297, false, true, 100.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1394 */     createSimpleEntry(10043, 64, 1411, 297, false, true, 100.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1396 */     createSimpleEntry(10043, 64, 698, 297, false, true, 100.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1398 */     createSimpleEntry(10043, 64, 694, 297, false, true, 100.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1400 */     createSimpleEntry(10043, 64, 837, 297, false, true, 100.0F, false, false, CreationCategories.JEWELRY);
/*      */     
/* 1402 */     createSimpleEntry(10043, 229, 232, 233, true, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1404 */     createSimpleEntry(10043, 185, 47, 839, false, true, 0.0F, false, false, 20, 30.0D, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1406 */     createSimpleEntry(10043, 185, 44, 840, false, true, 0.0F, false, false, 20, 30.0D, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/* 1409 */     createSimpleEntry(10051, 214, 23, 271, true, true, 100.0F, false, false, CreationCategories.TOYS);
/*      */     
/* 1411 */     createSimpleEntry(10074, 97, 146, 132, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1413 */     createSimpleEntry(10074, 97, 146, 1122, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1415 */     createSimpleEntry(10074, 97, 785, 786, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1417 */     createSimpleEntry(10074, 97, 770, 784, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1419 */     createSimpleEntry(10074, 97, 146, 519, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1421 */     createSimpleEntry(10074, 97, 1116, 1121, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1423 */     createSimpleEntry(10074, 97, 770, 1123, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/* 1426 */     createSimpleEntry(10074, 97, 146, 202, false, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1428 */     createSimpleEntry(10074, 97, 146, 296, false, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1430 */     createSimpleEntry(10074, 97, 146, 402, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1432 */     createSimpleEntry(10074, 97, 146, 399, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1434 */     createSimpleEntry(10074, 97, 146, 400, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1436 */     createSimpleEntry(10074, 97, 146, 403, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1438 */     createSimpleEntry(10074, 97, 146, 398, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1440 */     createSimpleEntry(10074, 97, 146, 401, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1442 */     createSimpleEntry(10074, 97, 146, 406, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1444 */     createSimpleEntry(10074, 97, 785, 787, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1446 */     createSimpleEntry(10074, 97, 1116, 1124, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1448 */     createSimpleEntry(10074, 97, 770, 771, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1450 */     createSimpleEntry(10074, 97, 146, 905, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1452 */     createSimpleEntry(10074, 97, 785, 906, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1454 */     createSimpleEntry(10074, 97, 770, 1302, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1456 */     createSimpleEntry(10074, 97, 1116, 1305, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1458 */     createSimpleEntry(1011, 14, 130, 1303, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1460 */     createSimpleEntry(10074, 97, 146, 408, false, true, 10.0F, false, true, CreationCategories.FOUNTAINS_AND_WELLS);
/*      */     
/* 1462 */     createSimpleEntry(10074, 97, 146, 635, false, true, 10.0F, false, true, CreationCategories.FOUNTAINS_AND_WELLS);
/*      */     
/* 1464 */     createSimpleEntry(10074, 97, 146, 405, false, true, 10.0F, false, true, CreationCategories.FOUNTAINS_AND_WELLS);
/*      */     
/* 1466 */     createSimpleEntry(10074, 97, 146, 593, false, true, 10.0F, false, true, CreationCategories.MINE_DOORS);
/*      */     
/* 1468 */     createSimpleEntry(10074, 684, 146, 685, true, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1470 */     createSimpleEntry(10074, 685, 146, 686, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1472 */     createSimpleEntry(10074, 685, 146, 689, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1474 */     createSimpleEntry(10074, 685, 146, 1010, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1476 */     createSimpleEntry(10074, 97, 146, 686, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1478 */     createSimpleEntry(10074, 97, 146, 689, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1480 */     createSimpleEntry(10074, 97, 146, 1010, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1482 */     createSimpleEntry(1005, 685, 688, 36, false, true, 10.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/* 1484 */     createSimpleEntry(1005, 8, 688, 36, false, true, 10.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/* 1486 */     createSimpleEntry(1005, 93, 688, 36, false, true, 10.0F, false, false, CreationCategories.KINDLINGS);
/*      */     
/* 1488 */     createSimpleEntry(1005, 685, 688, 23, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1490 */     createSimpleEntry(1005, 8, 688, 23, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1492 */     createSimpleEntry(1005, 93, 688, 23, false, true, 10.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/* 1495 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/* 1497 */       createMetallicEntries(10034, 64, 46, 167, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1499 */       createMetallicEntries(10034, 64, 46, 194, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1501 */       createMetallicEntries(10034, 64, 46, 193, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1503 */       createMetallicEntries(10034, 64, 46, 463, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.TOOLS);
/*      */       
/* 1505 */       createMetallicEntries(10034, 185, 46, 252, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1507 */       createMetallicEntries(10034, 185, 46, 568, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */ 
/*      */       
/* 1510 */       createMetallicEntries(10015, 64, 46, 185, false, true, 0.0F, false, true, CreationCategories.TOOLS);
/*      */ 
/*      */       
/* 1513 */       createMetallicEntries(10015, 185, 46, 547, false, true, 0.0F, false, true, CreationCategories.SHIPBUILDING);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1518 */       createSimpleEntry(10034, 64, 46, 167, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1520 */       createSimpleEntry(10034, 64, 46, 194, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1522 */       createSimpleEntry(10034, 64, 46, 193, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1524 */       createSimpleEntry(10034, 64, 694, 167, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1526 */       createSimpleEntry(10034, 64, 694, 194, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1528 */       createSimpleEntry(10034, 64, 694, 193, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1530 */       createSimpleEntry(10034, 64, 837, 167, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1532 */       createSimpleEntry(10034, 64, 837, 194, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1534 */       createSimpleEntry(10034, 64, 837, 193, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1536 */       createSimpleEntry(10034, 64, 698, 167, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1538 */       createSimpleEntry(10034, 64, 698, 194, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1540 */       createSimpleEntry(10034, 64, 698, 193, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1542 */       createSimpleEntry(10034, 64, 46, 463, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.TOOLS);
/*      */       
/* 1544 */       createSimpleEntry(10034, 64, 205, 463, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.TOOLS);
/*      */       
/* 1546 */       createSimpleEntry(10034, 64, 694, 463, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.TOOLS);
/*      */       
/* 1548 */       createSimpleEntry(10034, 64, 837, 463, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.TOOLS);
/*      */       
/* 1550 */       createSimpleEntry(10034, 64, 698, 463, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.TOOLS);
/*      */       
/* 1552 */       createSimpleEntry(10034, 64, 47, 463, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.TOOLS);
/*      */       
/* 1554 */       createSimpleEntry(10034, 185, 46, 252, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */       
/* 1556 */       createSimpleEntry(10034, 185, 46, 568, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */ 
/*      */       
/* 1559 */       createSimpleEntry(10015, 64, 46, 185, false, true, 0.0F, false, true, CreationCategories.TOOLS);
/*      */ 
/*      */       
/* 1562 */       createSimpleEntry(10015, 185, 49, 547, false, true, 0.0F, false, true, CreationCategories.SHIPBUILDING);
/*      */     } 
/*      */ 
/*      */     
/* 1566 */     createSimpleEntry(10034, 168, 130, 342, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1568 */     createSimpleEntry(10034, 343, 47, 341, false, true, 0.0F, false, false, CreationCategories.LOCKS);
/*      */     
/* 1570 */     createSimpleEntry(10034, 341, 130, 342, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 1573 */     createSimpleEntry(1014, 320, 318, 319, false, true, 30.0F, false, false, CreationCategories.ROPES);
/*      */     
/* 1575 */     createSimpleEntry(1014, 320, 318, 557, false, true, 30.0F, false, false, CreationCategories.ROPES);
/*      */     
/* 1577 */     createSimpleEntry(1014, 320, 318, 559, false, true, 30.0F, false, false, CreationCategories.ROPES);
/*      */     
/* 1579 */     createSimpleEntry(1014, 320, 318, 558, false, true, 30.0F, false, false, CreationCategories.ROPES);
/*      */     
/* 1581 */     createSimpleEntry(1014, 320, 318, 457, false, true, 30.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1583 */     createSimpleEntry(10039, 428, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */     
/* 1585 */     createSimpleEntry(10039, 66, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */     
/* 1587 */     createSimpleEntry(10039, 68, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */     
/* 1589 */     createSimpleEntry(10039, 69, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */     
/* 1591 */     createSimpleEntry(10039, 67, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */     
/* 1593 */     createSimpleEntry(10039, 415, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */     
/* 1595 */     createSimpleEntry(10039, 70, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */     
/* 1597 */     createSimpleEntry(10039, 464, 203, 488, true, true, 0.0F, false, false, CreationCategories.FOOD);
/*      */ 
/*      */     
/* 1600 */     createSimpleEntry(10042, 413, 752, 753, false, true, 50.0F, false, false, CreationCategories.WRITING);
/*      */ 
/*      */     
/* 1603 */     createSimpleEntry(1018, 8, 33, 522, false, true, 0.0F, false, false, CreationCategories.DECORATION);
/*      */     
/* 1605 */     createSimpleEntry(1018, 685, 33, 522, false, true, 0.0F, false, false, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 1608 */     createSimpleEntry(10043, 185, 45, 325, false, true, 0.0F, false, true, CreationCategories.ALTAR);
/*      */     
/* 1610 */     createSimpleEntry(10043, 185, 44, 324, false, true, 0.0F, false, true, CreationCategories.ALTAR);
/*      */ 
/*      */     
/* 1613 */     createSimpleEntry(10082, 24, 9, 556, false, true, 60.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1615 */     createSimpleEntry(10082, 24, 9, 545, false, true, 20.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1617 */     createSimpleEntry(10082, 8, 9, 550, false, true, 30.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1619 */     createSimpleEntry(10082, 8, 9, 549, false, true, 30.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1621 */     createSimpleEntry(10082, 8, 23, 567, false, true, 30.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */ 
/*      */     
/* 1624 */     createSimpleEntry(10082, 8, 9, 551, false, true, 40.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1626 */     createSimpleEntry(10082, 24, 9, 546, false, true, 0.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1628 */     createSimpleEntry(10082, 24, 9, 566, false, true, 40.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1630 */     createSimpleEntry(10082, 7, 385, 552, false, true, 20.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1632 */     createSimpleEntry(10082, 7, 385, 588, false, true, 20.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1634 */     createSimpleEntry(10082, 7, 385, 590, false, true, 20.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1636 */     createSimpleEntry(10082, 7, 385, 589, false, true, 20.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */ 
/*      */     
/* 1639 */     createSimpleEntry(10082, 7, 385, 560, false, true, 50.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1641 */     createSimpleEntry(1020, 558, 547, 565, true, true, 0.0F, false, false, CreationCategories.SHIPBUILDING);
/*      */ 
/*      */     
/* 1644 */     createSimpleEntry(10042, 73, 492, 782, true, true, 0.0F, true, false, CreationCategories.RESOURCES);
/*      */     
/* 1646 */     createSimpleEntry(10074, 97, 146, 811, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */ 
/*      */     
/* 1649 */     createSimpleEntry(10015, 185, 46, 859, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/* 1652 */     createSimpleEntry(10042, 383, 711, 825, true, true, 0.0F, false, false, CreationCategories.MAGIC);
/*      */     
/* 1654 */     createSimpleEntry(10042, 377, 711, 826, true, true, 0.0F, false, false, CreationCategories.MAGIC);
/*      */     
/* 1656 */     createSimpleEntry(10042, 381, 711, 827, true, true, 0.0F, false, false, CreationCategories.MAGIC);
/*      */     
/* 1658 */     createSimpleEntry(10042, 379, 711, 828, true, true, 0.0F, false, false, CreationCategories.MAGIC);
/*      */     
/* 1660 */     createSimpleEntry(10042, 375, 711, 829, true, true, 0.0F, false, false, CreationCategories.MAGIC);
/*      */     
/* 1662 */     createSimpleEntry(1011, 14, 130, 812, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/* 1664 */     createSimpleEntry(10074, 97, 146, 821, false, true, 0.0F, false, false, CreationCategories.DECORATION);
/*      */     
/* 1666 */     createSimpleEntry(10015, 64, 221, 902, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1668 */     createSimpleEntry(10015, 64, 223, 904, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */     
/* 1670 */     createSimpleEntry(10016, 922, 921, 925, false, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */     
/* 1672 */     createSimpleEntry(10016, 226, 925, 926, false, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */ 
/*      */     
/* 1675 */     createSimpleEntry(10016, 215, 926, 943, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 1678 */     createSimpleEntry(10016, 216, 926, 943, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */ 
/*      */     
/* 1682 */     createSimpleEntry(10016, 215, 926, 954, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 1685 */     createSimpleEntry(10016, 216, 926, 954, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */ 
/*      */     
/* 1689 */     createSimpleEntry(10017, 926, 847, 959, true, true, 0.0F, false, false, CreationCategories.ARMOUR)
/* 1690 */       .setFinalMaterial((byte)16);
/* 1691 */     createSimpleEntry(10017, 215, 72, 960, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */ 
/*      */     
/* 1695 */     createSimpleEntry(10016, 215, 926, 961, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 1698 */     createSimpleEntry(10016, 216, 926, 961, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */ 
/*      */     
/* 1702 */     createSimpleEntry(10016, 226, 214, 908, false, true, 0.0F, false, false, 0, 20.0D, CreationCategories.RUGS)
/*      */       
/* 1704 */       .setDepleteFromTarget(3000);
/*      */     
/* 1706 */     createSimpleEntry(10016, 226, 214, 909, false, true, 0.0F, false, false, 0, 30.0D, CreationCategories.RUGS)
/*      */       
/* 1708 */       .setDepleteFromTarget(4000);
/*      */     
/* 1710 */     createSimpleEntry(10016, 226, 214, 910, false, true, 0.0F, false, false, 0, 40.0D, CreationCategories.RUGS)
/*      */       
/* 1712 */       .setDepleteFromTarget(5000);
/*      */ 
/*      */     
/* 1715 */     createSimpleEntry(10074, 97, 785, 402, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1717 */     createSimpleEntry(10074, 97, 785, 403, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1719 */     createSimpleEntry(10074, 97, 785, 398, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1721 */     createSimpleEntry(10074, 97, 785, 401, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */     
/* 1723 */     createSimpleEntry(10074, 97, 785, 811, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */ 
/*      */     
/* 1726 */     createSimpleEntry(1011, 14, 130, 1160, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/* 1728 */     createSimpleEntry(1011, 14, 130, 1164, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/* 1730 */     createSimpleEntry(1011, 14, 130, 1168, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/* 1732 */     createSimpleEntry(1011, 14, 130, 1171, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/* 1734 */     createSimpleEntry(1011, 14, 130, 1251, false, true, 0.0F, false, false, CreationCategories.POTTERY);
/*      */     
/* 1736 */     createSimpleEntry(10015, 64, 220, 1166, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1738 */     createSimpleEntry(10074, 97, 785, 1167, false, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1740 */     createSimpleEntry(10074, 97, 785, 1237, false, true, 10.0F, false, false, CreationCategories.TOOLS);
/*      */     
/* 1742 */     createSimpleEntry(1005, 8, 9, 1173, false, true, 0.0F, false, false, CreationCategories.COOKING_UTENSILS);
/*      */ 
/*      */ 
/*      */     
/* 1746 */     int[] metalTypes = { 221, 223, 220, 694, 698, 44, 45, 205, 47, 46, 49, 48, 837 };
/*      */ 
/*      */     
/* 1749 */     int[] runeTypes = { 1290, 1293, 1292, 1289, 1291 };
/* 1750 */     for (int metal : metalTypes) {
/*      */       
/* 1752 */       for (int rune : runeTypes) {
/*      */         
/* 1754 */         createSimpleEntry(10074, 1102, metal, rune, true, true, 0.0F, false, false, CreationCategories.MAGIC)
/*      */           
/* 1756 */           .setDepleteFromTarget(200);
/* 1757 */         createSimpleEntry(10043, 1103, metal, rune, true, true, 0.0F, false, false, CreationCategories.MAGIC)
/*      */           
/* 1759 */           .setDepleteFromTarget(200);
/* 1760 */         createSimpleEntry(10044, 1104, metal, rune, true, true, 0.0F, false, false, CreationCategories.MAGIC)
/*      */           
/* 1762 */           .setDepleteFromTarget(200);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1768 */     createSimpleEntry(10074, 1102, 1411, 1290, true, true, 0.0F, false, false, CreationCategories.MAGIC)
/*      */       
/* 1770 */       .setDepleteFromTarget(200);
/* 1771 */     createSimpleEntry(10043, 1103, 1411, 1290, true, true, 0.0F, false, false, CreationCategories.MAGIC)
/*      */       
/* 1773 */       .setDepleteFromTarget(200);
/* 1774 */     createSimpleEntry(10044, 1104, 1411, 1290, true, true, 0.0F, false, false, CreationCategories.MAGIC)
/*      */       
/* 1776 */       .setDepleteFromTarget(200);
/*      */ 
/*      */     
/* 1779 */     createSimpleEntry(10041, 44, 45, 1411, true, true, 0.0F, false, false, CreationCategories.RESOURCES);
/*      */ 
/*      */     
/* 1782 */     createSimpleEntry(10074, 97, 785, 1430, false, true, 10.0F, false, true, CreationCategories.STATUES);
/*      */ 
/*      */ 
/*      */     
/* 1786 */     CreationEntry brownBearRug = createSimpleEntry(10017, 302, 349, 847, true, true, 0.0F, false, false, 0, 30.0D, CreationCategories.RUGS);
/*      */     
/* 1788 */     brownBearRug.setDepleteFromSource(3000);
/* 1789 */     brownBearRug.setDepleteFromTarget(1);
/*      */     
/* 1791 */     CreationEntry blackBearRug = createSimpleEntry(10017, 302, 349, 846, true, true, 0.0F, false, false, 0, 30.0D, CreationCategories.RUGS);
/*      */     
/* 1793 */     blackBearRug.setDepleteFromSource(3000);
/* 1794 */     blackBearRug.setDepleteFromTarget(1);
/*      */     
/* 1796 */     CreationEntry mountainLionRug = createSimpleEntry(10017, 313, 349, 848, true, true, 0.0F, false, false, 0, 30.0D, CreationCategories.RUGS);
/*      */     
/* 1798 */     mountainLionRug.setDepleteFromSource(300);
/* 1799 */     mountainLionRug.setDepleteFromTarget(1);
/*      */     
/* 1801 */     CreationEntry blackWolfRug = createSimpleEntry(10017, 302, 349, 849, true, true, 0.0F, false, false, 0, 30.0D, CreationCategories.RUGS);
/*      */     
/* 1803 */     blackWolfRug.setDepleteFromSource(3000);
/* 1804 */     blackWolfRug.setDepleteFromTarget(1);
/*      */     
/* 1806 */     AdvancedCreationEntry lRudder = createAdvancedEntry(10082, 23, 22, 544, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1808 */     lRudder.addRequirement(new CreationRequirement(1, 22, 5, true));
/*      */     
/* 1810 */     AdvancedCreationEntry bellCot = createAdvancedEntry(1005, 217, 22, 723, false, false, 0.0F, true, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 1812 */     bellCot.addRequirement(new CreationRequirement(1, 22, 25, true));
/* 1813 */     bellCot.addRequirement(new CreationRequirement(2, 9, 4, true));
/*      */     
/* 1815 */     AdvancedCreationEntry bellTower = createAdvancedEntry(1005, 718, 723, 722, false, false, 0.0F, true, false, CreationCategories.DECORATION);
/*      */     
/* 1817 */     bellTower.addRequirement(new CreationRequirement(1, 319, 1, true));
/*      */     
/* 1819 */     AdvancedCreationEntry bellSmall = createAdvancedEntry(10015, 734, 720, 719, false, false, 0.0F, true, false, CreationCategories.TOYS);
/*      */     
/* 1821 */     bellSmall.addRequirement(new CreationRequirement(1, 99, 1, true));
/*      */     
/* 1823 */     AdvancedCreationEntry lHelm = createAdvancedEntry(10082, 23, 22, 548, false, false, 0.0F, true, true, CreationCategories.SHIPBUILDING);
/*      */     
/* 1825 */     lHelm.addRequirement(new CreationRequirement(1, 23, 5, true));
/* 1826 */     lHelm.addRequirement(new CreationRequirement(2, 22, 4, true));
/* 1827 */     lHelm.addRequirement(new CreationRequirement(3, 561, 8, true));
/*      */     
/* 1829 */     AdvancedCreationEntry well = createAdvancedEntry(1013, 132, 130, 608, false, false, 0.0F, true, true, CreationCategories.FOUNTAINS_AND_WELLS);
/*      */     
/* 1831 */     well.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 1832 */     well.addRequirement(new CreationRequirement(2, 130, 10, true));
/* 1833 */     well.addRequirement(new CreationRequirement(3, 319, 1, true));
/* 1834 */     well.addRequirement(new CreationRequirement(4, 421, 1, true));
/*      */     
/* 1836 */     AdvancedCreationEntry lStern = createAdvancedEntry(10082, 551, 546, 553, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1838 */     lStern.addRequirement(new CreationRequirement(1, 546, 10, true));
/* 1839 */     lStern.addRequirement(new CreationRequirement(2, 551, 9, true));
/* 1840 */     lStern.addRequirement(new CreationRequirement(3, 561, 20, true));
/*      */     
/* 1842 */     AdvancedCreationEntry lRigS = createAdvancedEntry(10082, 589, 555, 564, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1844 */     lRigS.addRequirement(new CreationRequirement(1, 559, 4, true));
/* 1845 */     lRigS.addRequirement(new CreationRequirement(2, 549, 2, true));
/* 1846 */     lRigS.addRequirement(new CreationRequirement(3, 550, 2, true));
/*      */     
/* 1848 */     AdvancedCreationEntry lRigT = createAdvancedEntry(10082, 588, 554, 563, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1850 */     lRigT.addRequirement(new CreationRequirement(1, 559, 2, true));
/* 1851 */     lRigT.addRequirement(new CreationRequirement(2, 549, 2, true));
/*      */     
/* 1853 */     AdvancedCreationEntry spinRigT = createAdvancedEntry(10082, 588, 591, 584, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1855 */     spinRigT.addRequirement(new CreationRequirement(1, 559, 2, true));
/* 1856 */     spinRigT.addRequirement(new CreationRequirement(2, 549, 2, true));
/*      */     
/* 1858 */     AdvancedCreationEntry lRigCrows = createAdvancedEntry(10082, 590, 555, 585, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1860 */     lRigCrows.addRequirement(new CreationRequirement(1, 559, 8, true));
/* 1861 */     lRigCrows.addRequirement(new CreationRequirement(2, 583, 1, true));
/* 1862 */     lRigCrows.addRequirement(new CreationRequirement(3, 550, 4, true));
/* 1863 */     lRigCrows.addRequirement(new CreationRequirement(4, 549, 2, true));
/*      */     
/* 1865 */     AdvancedCreationEntry tRigCrows = createAdvancedEntry(10082, 552, 555, 587, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1867 */     tRigCrows.addRequirement(new CreationRequirement(1, 559, 16, true));
/* 1868 */     tRigCrows.addRequirement(new CreationRequirement(2, 583, 1, true));
/* 1869 */     tRigCrows.addRequirement(new CreationRequirement(3, 555, 5, true));
/* 1870 */     tRigCrows.addRequirement(new CreationRequirement(4, 550, 8, true));
/* 1871 */     tRigCrows.addRequirement(new CreationRequirement(5, 549, 4, true));
/*      */     
/* 1873 */     AdvancedCreationEntry lRigSqY = createAdvancedEntry(10082, 552, 555, 586, false, false, 0.0F, true, false, CreationCategories.SHIPBUILDING);
/*      */     
/* 1875 */     lRigSqY.addRequirement(new CreationRequirement(1, 559, 12, true));
/* 1876 */     lRigSqY.addRequirement(new CreationRequirement(2, 555, 3, true));
/* 1877 */     lRigSqY.addRequirement(new CreationRequirement(3, 550, 6, true));
/*      */     
/* 1879 */     AdvancedCreationEntry bardingLeather = createAdvancedEntry(10017, 131, 72, 702, false, false, 0.0F, true, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */     
/* 1881 */     bardingLeather.addRequirement(new CreationRequirement(1, 72, 4, true));
/* 1882 */     bardingLeather.addRequirement(new CreationRequirement(2, 131, 50, true));
/*      */     
/* 1884 */     AdvancedCreationEntry bridle = createAdvancedEntry(10017, 627, 631, 624, false, false, 0.0F, true, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */     
/* 1886 */     bridle.addRequirement(new CreationRequirement(1, 628, 1, true));
/*      */     
/* 1888 */     AdvancedCreationEntry saddle = createAdvancedEntry(10017, 625, 629, 621, false, false, 0.0F, true, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */     
/* 1890 */     saddle.addRequirement(new CreationRequirement(1, 626, 1, true));
/*      */     
/* 1892 */     AdvancedCreationEntry saddleL = createAdvancedEntry(10017, 625, 630, 622, false, false, 0.0F, true, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */ 
/*      */     
/* 1895 */     saddleL.addRequirement(new CreationRequirement(1, 626, 1, true));
/*      */     
/* 1897 */     AdvancedCreationEntry saddleBag = createAdvancedEntry(10017, 1332, 102, 1333, false, false, 0.0F, true, false, CreationCategories.ANIMAL_EQUIPMENT);
/*      */     
/* 1899 */     saddleBag.addRequirement(new CreationRequirement(1, 102, 1, true));
/* 1900 */     saddleBag.addRequirement(new CreationRequirement(2, 1332, 1, true));
/*      */     
/* 1902 */     AdvancedCreationEntry birdcage = createAdvancedEntry(10043, 897, 444, 1025, false, false, 0.0F, true, false, CreationCategories.DECORATION);
/*      */     
/* 1904 */     birdcage.addRequirement(new CreationRequirement(1, 897, 1, true));
/* 1905 */     birdcage.addRequirement(new CreationRequirement(2, 131, 10, true));
/* 1906 */     birdcage.addRequirement(new CreationRequirement(3, 444, 4, true));
/* 1907 */     birdcage.addRequirement(new CreationRequirement(4, 326, 1, true));
/* 1908 */     birdcage.addRequirement(new CreationRequirement(5, 221, 10, true));
/* 1909 */     birdcage.addRequirement(new CreationRequirement(6, 464, 1, true));
/* 1910 */     birdcage.setIsEpicBuildMissionTarget(false);
/*      */ 
/*      */ 
/*      */     
/* 1914 */     createBoatEntries();
/*      */ 
/*      */     
/* 1917 */     AdvancedCreationEntry sacknife = createAdvancedEntry(1016, 101, 793, 792, false, false, 0.0F, true, false, CreationCategories.WEAPONS);
/*      */ 
/*      */     
/* 1920 */     sacknife.addRequirement(new CreationRequirement(1, 376, 3, true));
/* 1921 */     sacknife.addRequirement(new CreationRequirement(2, 382, 3, true));
/* 1922 */     sacknife.addRequirement(new CreationRequirement(3, 380, 1, true));
/*      */     
/* 1924 */     AdvancedCreationEntry torch = createAdvancedEntry(1010, 153, 23, 138, false, false, 0.0F, true, false, CreationCategories.LIGHTS_AND_LAMPS);
/*      */     
/* 1926 */     torch.addRequirement(new CreationRequirement(1, 479, 1, true));
/*      */     
/* 1928 */     AdvancedCreationEntry oven = createAdvancedEntry(1013, 132, 130, 178, false, false, 0.0F, true, true, CreationCategories.FIRE);
/*      */     
/* 1930 */     oven.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 1931 */     oven.addRequirement(new CreationRequirement(2, 130, 10, true));
/*      */     
/* 1933 */     AdvancedCreationEntry forge = createAdvancedEntry(1013, 132, 130, 180, false, false, 0.0F, true, true, CreationCategories.FIRE);
/*      */     
/* 1935 */     forge.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 1936 */     forge.addRequirement(new CreationRequirement(2, 130, 10, true));
/*      */     
/* 1938 */     AdvancedCreationEntry colossus = createAdvancedEntry(1013, 519, 130, 518, false, false, 0.0F, true, true, CreationCategories.STATUES);
/*      */     
/* 1940 */     colossus.addRequirement(new CreationRequirement(1, 519, 1999, true));
/* 1941 */     colossus.addRequirement(new CreationRequirement(2, 130, 1999, true));
/* 1942 */     colossus.setIsEpicBuildMissionTarget(false);
/*      */     
/* 1944 */     AdvancedCreationEntry pylon = createAdvancedEntry(1013, 406, 130, 713, false, false, 0.0F, true, true, CreationCategories.EPIC);
/*      */     
/* 1946 */     pylon.addRequirement(new CreationRequirement(1, 406, 100, true));
/* 1947 */     pylon.addRequirement(new CreationRequirement(2, 130, 1999, true));
/* 1948 */     pylon.addRequirement(new CreationRequirement(3, 132, 1000, true));
/* 1949 */     pylon.addRequirement(new CreationRequirement(4, 221, 1000, true));
/* 1950 */     pylon.isOnlyCreateEpicTargetMission = true;
/*      */     
/* 1952 */     AdvancedCreationEntry shrine = createAdvancedEntry(1005, 406, 130, 712, false, false, 0.0F, true, true, CreationCategories.EPIC);
/*      */     
/* 1954 */     shrine.addRequirement(new CreationRequirement(1, 406, 10, true));
/* 1955 */     shrine.addRequirement(new CreationRequirement(2, 22, 100, true));
/* 1956 */     shrine.addRequirement(new CreationRequirement(3, 218, 10, true));
/* 1957 */     shrine.addRequirement(new CreationRequirement(4, 221, 100, true));
/* 1958 */     shrine.addRequirement(new CreationRequirement(5, 502, 10, true));
/* 1959 */     shrine.isOnlyCreateEpicTargetMission = true;
/*      */     
/* 1961 */     AdvancedCreationEntry temple = createAdvancedEntry(1013, 406, 130, 715, false, false, 0.0F, true, true, CreationCategories.EPIC);
/*      */     
/* 1963 */     temple.addRequirement(new CreationRequirement(1, 406, 10, true));
/* 1964 */     temple.addRequirement(new CreationRequirement(2, 130, 100, true));
/* 1965 */     temple.addRequirement(new CreationRequirement(3, 132, 1000, true));
/* 1966 */     temple.addRequirement(new CreationRequirement(4, 223, 100, true));
/* 1967 */     temple.addRequirement(new CreationRequirement(5, 504, 10, true));
/* 1968 */     temple.isOnlyCreateEpicTargetMission = true;
/*      */     
/* 1970 */     AdvancedCreationEntry obelisk = createAdvancedEntry(1013, 132, 130, 714, false, false, 0.0F, true, true, CreationCategories.EPIC);
/*      */     
/* 1972 */     obelisk.addRequirement(new CreationRequirement(1, 132, 1000, true));
/* 1973 */     obelisk.addRequirement(new CreationRequirement(2, 130, 1000, true));
/* 1974 */     obelisk.addRequirement(new CreationRequirement(3, 223, 100, true));
/* 1975 */     obelisk.isOnlyCreateEpicTargetMission = true;
/*      */     
/* 1977 */     AdvancedCreationEntry pillarDecoration = createAdvancedEntry(1013, 132, 130, 736, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 1979 */     pillarDecoration.addRequirement(new CreationRequirement(1, 132, 50, true));
/* 1980 */     pillarDecoration.addRequirement(new CreationRequirement(2, 130, 50, true));
/*      */     
/* 1982 */     AdvancedCreationEntry pillar = createAdvancedEntry(1013, 132, 130, 717, false, false, 0.0F, true, true, CreationCategories.EPIC);
/*      */     
/* 1984 */     pillar.addRequirement(new CreationRequirement(1, 132, 100, true));
/* 1985 */     pillar.addRequirement(new CreationRequirement(2, 130, 100, true));
/* 1986 */     pillar.addRequirement(new CreationRequirement(3, 439, 10, true));
/* 1987 */     pillar.isOnlyCreateEpicTargetMission = true;
/*      */     
/* 1989 */     AdvancedCreationEntry spiritgate = createAdvancedEntry(1013, 132, 130, 716, false, false, 0.0F, true, true, CreationCategories.EPIC);
/*      */     
/* 1991 */     spiritgate.addRequirement(new CreationRequirement(1, 132, 1000, true));
/* 1992 */     spiritgate.addRequirement(new CreationRequirement(2, 130, 1000, true));
/* 1993 */     spiritgate.addRequirement(new CreationRequirement(3, 44, 1000, true));
/* 1994 */     spiritgate.isOnlyCreateEpicTargetMission = true;
/*      */     
/* 1996 */     AdvancedCreationEntry sbench = createAdvancedEntry(1013, 132, 406, 404, false, false, 0.0F, true, true, CreationCategories.FURNITURE);
/*      */     
/* 1998 */     sbench.addRequirement(new CreationRequirement(1, 132, 1, true));
/* 1999 */     sbench.addRequirement(new CreationRequirement(2, 130, 2, true));
/*      */     
/* 2001 */     AdvancedCreationEntry coff = createAdvancedEntry(1013, 132, 406, 407, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2003 */     coff.addRequirement(new CreationRequirement(1, 132, 4, true));
/* 2004 */     coff.addRequirement(new CreationRequirement(2, 130, 4, true));
/* 2005 */     coff.addRequirement(new CreationRequirement(3, 406, 3, true));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2012 */     AdvancedCreationEntry ropetool = createAdvancedEntry(10044, 22, 23, 320, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2014 */     ropetool.addRequirement(new CreationRequirement(1, 217, 3, true));
/*      */     
/* 2016 */     AdvancedCreationEntry wheelSmall = createAdvancedEntry(10044, 22, 23, 187, false, false, 0.0F, true, false, CreationCategories.CART_PARTS);
/*      */     
/* 2018 */     wheelSmall.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2019 */     wheelSmall.addRequirement(new CreationRequirement(2, 23, 1, true));
/*      */     
/* 2021 */     AdvancedCreationEntry wheelAxlSmall = createAdvancedEntry(10044, 23, 187, 191, false, true, 0.0F, true, false, CreationCategories.CART_PARTS);
/*      */     
/* 2023 */     wheelAxlSmall.addRequirement(new CreationRequirement(1, 187, 1, true));
/*      */     
/* 2025 */     AdvancedCreationEntry cartSmall = createAdvancedEntry(10044, 22, 191, 186, false, false, 0.0F, true, true, CreationCategories.CARTS);
/*      */     
/* 2027 */     cartSmall.addRequirement(new CreationRequirement(1, 22, 5, true));
/* 2028 */     cartSmall.addRequirement(new CreationRequirement(2, 23, 2, true));
/* 2029 */     cartSmall.addRequirement(new CreationRequirement(3, 218, 2, true));
/*      */     
/* 2031 */     AdvancedCreationEntry cartLarge = createAdvancedEntry(10044, 22, 191, 539, false, false, 0.0F, true, true, CreationCategories.CARTS);
/*      */     
/* 2033 */     cartLarge.addRequirement(new CreationRequirement(1, 22, 15, true));
/* 2034 */     cartLarge.addRequirement(new CreationRequirement(2, 23, 2, true));
/* 2035 */     cartLarge.addRequirement(new CreationRequirement(3, 218, 4, true));
/* 2036 */     cartLarge.addRequirement(new CreationRequirement(4, 632, 1, true));
/*      */     
/* 2038 */     AdvancedCreationEntry catapultSmall = createAdvancedEntry(10044, 23, 9, 445, false, false, 0.0F, true, true, CreationCategories.WARMACHINES);
/*      */     
/* 2040 */     catapultSmall.addRequirement(new CreationRequirement(1, 319, 5, true));
/* 2041 */     catapultSmall.addRequirement(new CreationRequirement(2, 191, 2, true));
/* 2042 */     catapultSmall.addRequirement(new CreationRequirement(3, 9, 6, true));
/*      */     
/* 2044 */     createSimpleEntry(10011, 64, 46, 1126, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */ 
/*      */     
/* 2047 */     AdvancedCreationEntry batteringRam = createAdvancedEntry(10044, 860, 217, 1125, false, false, 0.0F, true, true, CreationCategories.WARMACHINES);
/*      */     
/* 2049 */     batteringRam.addRequirement(new CreationRequirement(1, 860, 9, true));
/* 2050 */     batteringRam.addRequirement(new CreationRequirement(2, 217, 4, true));
/* 2051 */     batteringRam.addRequirement(new CreationRequirement(3, 319, 2, true));
/* 2052 */     batteringRam.addRequirement(new CreationRequirement(4, 191, 2, true));
/* 2053 */     batteringRam.addRequirement(new CreationRequirement(5, 9, 3, true));
/* 2054 */     batteringRam.addRequirement(new CreationRequirement(6, 1126, 1, true));
/*      */     
/* 2056 */     AdvancedCreationEntry joist = createAdvancedEntry(1005, 23, 9, 429, false, false, 0.0F, true, true, CreationCategories.MINE_DOORS);
/*      */     
/* 2058 */     joist.addRequirement(new CreationRequirement(1, 188, 2, true));
/* 2059 */     joist.addRequirement(new CreationRequirement(2, 9, 1, true));
/* 2060 */     joist.addRequirement(new CreationRequirement(3, 23, 3, true));
/*      */     
/* 2062 */     AdvancedCreationEntry floor = createAdvancedEntry(1005, 217, 22, 495, false, false, 0.0F, true, true, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 2064 */     floor.addRequirement(new CreationRequirement(1, 22, 4, true));
/*      */     
/* 2066 */     AdvancedCreationEntry minedoor = createAdvancedEntry(1005, 217, 22, 592, false, false, 0.0F, true, true, CreationCategories.MINE_DOORS);
/*      */     
/* 2068 */     minedoor.addRequirement(new CreationRequirement(1, 22, 20, true));
/* 2069 */     minedoor.addRequirement(new CreationRequirement(2, 217, 1, true));
/* 2070 */     minedoor.addRequirement(new CreationRequirement(3, 167, 1, true));
/*      */     
/* 2072 */     AdvancedCreationEntry minedoorst = createAdvancedEntry(10015, 167, 597, 596, false, false, 0.0F, true, true, CreationCategories.MINE_DOORS);
/*      */     
/* 2074 */     minedoorst.addRequirement(new CreationRequirement(1, 597, 9, true));
/* 2075 */     minedoorst.addRequirement(new CreationRequirement(2, 131, 50, true));
/*      */     
/* 2077 */     AdvancedCreationEntry minedoors = createAdvancedEntry(10015, 167, 598, 595, false, false, 0.0F, true, true, CreationCategories.MINE_DOORS);
/*      */     
/* 2079 */     minedoors.addRequirement(new CreationRequirement(1, 598, 11, true));
/* 2080 */     minedoors.addRequirement(new CreationRequirement(2, 131, 50, true));
/*      */     
/* 2082 */     AdvancedCreationEntry minedoorg = createAdvancedEntry(10015, 167, 599, 594, false, false, 0.0F, true, true, CreationCategories.MINE_DOORS);
/*      */     
/* 2084 */     minedoorg.addRequirement(new CreationRequirement(1, 599, 11, true));
/* 2085 */     minedoorg.addRequirement(new CreationRequirement(2, 131, 50, true));
/*      */     
/* 2087 */     AdvancedCreationEntry cheeseDrill = createAdvancedEntry(10044, 309, 23, 65, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2089 */     cheeseDrill.addRequirement(new CreationRequirement(1, 22, 5, true));
/* 2090 */     cheeseDrill.addRequirement(new CreationRequirement(2, 266, 2, true));
/* 2091 */     cheeseDrill.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 2093 */     AdvancedCreationEntry fruitPress = createAdvancedEntry(10044, 23, 22, 413, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2095 */     fruitPress.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2096 */     fruitPress.addRequirement(new CreationRequirement(2, 266, 2, true));
/* 2097 */     fruitPress.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 2099 */     AdvancedCreationEntry papyrusPress = createAdvancedEntry(10044, 23, 22, 747, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2101 */     papyrusPress.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2102 */     papyrusPress.addRequirement(new CreationRequirement(2, 188, 2, true));
/* 2103 */     papyrusPress.addRequirement(new CreationRequirement(3, 218, 2, true));
/*      */     
/* 2105 */     AdvancedCreationEntry raftSmall = createAdvancedEntry(10082, 217, 22, 289, false, false, 0.0F, true, true, CreationCategories.SHIPBUILDING);
/*      */     
/* 2107 */     raftSmall.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2108 */     raftSmall.addRequirement(new CreationRequirement(2, 9, 4, true));
/* 2109 */     raftSmall.addRequirement(new CreationRequirement(3, 217, 7, true));
/*      */     
/* 2111 */     AdvancedCreationEntry archeryTarg = createAdvancedEntry(1005, 23, 23, 458, false, false, 0.0F, true, true, CreationCategories.COMBAT_TRAINING);
/*      */     
/* 2113 */     archeryTarg.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2114 */     archeryTarg.addRequirement(new CreationRequirement(2, 620, 4, true));
/* 2115 */     archeryTarg.addRequirement(new CreationRequirement(3, 217, 7, true));
/* 2116 */     archeryTarg.addRequirement(new CreationRequirement(4, 319, 1, true));
/*      */     
/* 2118 */     AdvancedCreationEntry buildmarker = createAdvancedEntry(1005, 23, 23, 679, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 2120 */     buildmarker.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2121 */     buildmarker.addRequirement(new CreationRequirement(2, 217, 1, true));
/*      */     
/* 2123 */     AdvancedCreationEntry doll = createAdvancedEntry(1005, 217, 23, 321, false, false, 0.0F, true, false, CreationCategories.COMBAT_TRAINING);
/*      */     
/* 2125 */     doll.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2126 */     doll.addRequirement(new CreationRequirement(2, 23, 3, true));
/* 2127 */     doll.addRequirement(new CreationRequirement(3, 33, 1, true));
/*      */     
/* 2129 */     AdvancedCreationEntry barrell = createAdvancedEntry(10044, 188, 22, 190, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2131 */     barrell.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2132 */     barrell.addRequirement(new CreationRequirement(2, 188, 1, true));
/*      */     
/* 2134 */     AdvancedCreationEntry hbarrell = createAdvancedEntry(10044, 188, 22, 576, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2136 */     hbarrell.addRequirement(new CreationRequirement(1, 22, 24, true));
/* 2137 */     hbarrell.addRequirement(new CreationRequirement(2, 188, 3, true));
/*      */     
/* 2139 */     AdvancedCreationEntry oilbarrell = createAdvancedEntry(10044, 188, 22, 757, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2141 */     oilbarrell.addRequirement(new CreationRequirement(1, 22, 24, true));
/* 2142 */     oilbarrell.addRequirement(new CreationRequirement(2, 188, 3, true));
/*      */     
/* 2144 */     AdvancedCreationEntry grains = createAdvancedEntry(1005, 188, 22, 661, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2146 */     grains.addRequirement(new CreationRequirement(1, 22, 24, true));
/* 2147 */     grains.addRequirement(new CreationRequirement(2, 217, 4, true));
/*      */     
/* 2149 */     AdvancedCreationEntry bulks = createAdvancedEntry(1005, 188, 22, 662, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2151 */     bulks.addRequirement(new CreationRequirement(1, 22, 24, true));
/* 2152 */     bulks.addRequirement(new CreationRequirement(2, 217, 4, true));
/*      */     
/* 2154 */     AdvancedCreationEntry trash = createAdvancedEntry(1005, 188, 22, 670, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2156 */     trash.addRequirement(new CreationRequirement(1, 22, 10, true));
/* 2157 */     trash.addRequirement(new CreationRequirement(2, 218, 2, true));
/*      */     
/* 2159 */     AdvancedCreationEntry barrels = createAdvancedEntry(10044, 218, 22, 189, false, false, 0.0F, true, false, CreationCategories.STORAGE);
/*      */     
/* 2161 */     barrels.addRequirement(new CreationRequirement(1, 22, 4, true));
/*      */     
/* 2163 */     AdvancedCreationEntry wineBarrel = createAdvancedEntry(10044, 218, 22, 768, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2165 */     wineBarrel.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2166 */     wineBarrel.addRequirement(new CreationRequirement(2, 188, 1, true));
/*      */     
/* 2168 */     AdvancedCreationEntry buckets = createAdvancedEntry(10044, 218, 22, 421, false, false, 0.0F, true, false, CreationCategories.STORAGE);
/*      */     
/* 2170 */     buckets.addRequirement(new CreationRequirement(1, 22, 4, true));
/*      */     
/* 2172 */     AdvancedCreationEntry dredge = createAdvancedEntry(10015, 213, 319, 581, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2174 */     dredge.addRequirement(new CreationRequirement(1, 582, 4, true));
/*      */     
/* 2176 */     AdvancedCreationEntry crows = createAdvancedEntry(10044, 188, 22, 583, false, false, 0.0F, true, true, CreationCategories.SHIPBUILDING);
/*      */     
/* 2178 */     crows.addRequirement(new CreationRequirement(1, 22, 12, true));
/* 2179 */     crows.addRequirement(new CreationRequirement(2, 188, 3, true));
/*      */     
/* 2181 */     AdvancedCreationEntry chests = createAdvancedEntry(10044, 218, 22, 192, false, false, 0.0F, true, false, CreationCategories.STORAGE);
/*      */     
/* 2183 */     chests.addRequirement(new CreationRequirement(1, 22, 3, true));
/*      */     
/* 2185 */     AdvancedCreationEntry signp = createAdvancedEntry(10044, 218, 22, 208, false, false, 0.0F, true, false, CreationCategories.SIGNS);
/*      */     
/* 2187 */     signp.addRequirement(new CreationRequirement(1, 23, 1, true));
/*      */     
/* 2189 */     AdvancedCreationEntry signl = createAdvancedEntry(10044, 218, 22, 209, false, false, 0.0F, true, false, CreationCategories.SIGNS);
/*      */     
/* 2191 */     signl.addRequirement(new CreationRequirement(1, 23, 1, true));
/* 2192 */     signl.addRequirement(new CreationRequirement(2, 22, 1, true));
/*      */     
/* 2194 */     AdvancedCreationEntry signshop = createAdvancedEntry(10044, 218, 22, 656, false, false, 0.0F, true, false, CreationCategories.SIGNS);
/*      */     
/* 2196 */     signshop.addRequirement(new CreationRequirement(1, 23, 2, true));
/* 2197 */     signshop.addRequirement(new CreationRequirement(2, 22, 2, true));
/*      */     
/* 2199 */     AdvancedCreationEntry signs = createAdvancedEntry(10044, 218, 22, 210, false, false, 0.0F, true, false, CreationCategories.SIGNS);
/*      */     
/* 2201 */     signs.addRequirement(new CreationRequirement(1, 23, 1, true));
/*      */     
/* 2203 */     AdvancedCreationEntry dale = createAdvancedEntry(10036, 36, 9, 74, false, false, 0.0F, true, true, CreationCategories.PRODUCTION);
/*      */     
/* 2205 */     dale.addRequirement(new CreationRequirement(1, 9, 20, true));
/* 2206 */     dale.addRequirement(new CreationRequirement(2, 26, 2, true));
/*      */     
/* 2208 */     AdvancedCreationEntry loom = createAdvancedEntry(10044, 218, 22, 226, false, false, 0.0F, true, true, CreationCategories.TOOLS);
/*      */     
/* 2210 */     loom.addRequirement(new CreationRequirement(1, 23, 10, true));
/* 2211 */     loom.addRequirement(new CreationRequirement(2, 214, 10, true));
/* 2212 */     loom.addRequirement(new CreationRequirement(3, 22, 2, true));
/*      */     
/* 2214 */     AdvancedCreationEntry marketstall = createAdvancedEntry(10044, 217, 22, 580, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 2216 */     marketstall.addRequirement(new CreationRequirement(1, 22, 20, true));
/* 2217 */     marketstall.addRequirement(new CreationRequirement(2, 213, 4, true));
/* 2218 */     marketstall.addRequirement(new CreationRequirement(3, 23, 4, true));
/*      */     
/* 2220 */     AdvancedCreationEntry squareTable = createAdvancedEntry(10044, 23, 22, 262, false, false, 0.0F, true, true, CreationCategories.FURNITURE);
/*      */     
/* 2222 */     squareTable.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2223 */     squareTable.addRequirement(new CreationRequirement(2, 23, 3, true));
/*      */     
/* 2225 */     AdvancedCreationEntry roundTable = createAdvancedEntry(10044, 23, 22, 260, false, false, 0.0F, true, true, CreationCategories.FURNITURE);
/*      */     
/* 2227 */     roundTable.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2228 */     roundTable.addRequirement(new CreationRequirement(2, 23, 3, true));
/*      */     
/* 2230 */     AdvancedCreationEntry diningTable = createAdvancedEntry(10044, 23, 22, 264, false, false, 0.0F, true, true, CreationCategories.FURNITURE);
/*      */     
/* 2232 */     diningTable.addRequirement(new CreationRequirement(1, 22, 6, true));
/* 2233 */     diningTable.addRequirement(new CreationRequirement(2, 23, 3, true));
/*      */     
/* 2235 */     AdvancedCreationEntry stoolRound = createAdvancedEntry(10044, 23, 22, 261, false, false, 0.0F, true, false, CreationCategories.FURNITURE);
/*      */     
/* 2237 */     stoolRound.addRequirement(new CreationRequirement(1, 22, 1, true));
/* 2238 */     stoolRound.addRequirement(new CreationRequirement(2, 23, 1, true));
/*      */     
/* 2240 */     AdvancedCreationEntry chair = createAdvancedEntry(10044, 22, 23, 263, false, false, 0.0F, true, true, CreationCategories.FURNITURE);
/*      */     
/* 2242 */     chair.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2243 */     chair.addRequirement(new CreationRequirement(2, 23, 2, true));
/*      */     
/* 2245 */     AdvancedCreationEntry armchair = createAdvancedEntry(10044, 23, 22, 265, false, false, 0.0F, true, true, CreationCategories.FURNITURE);
/*      */     
/* 2247 */     armchair.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2248 */     armchair.addRequirement(new CreationRequirement(2, 23, 2, true));
/* 2249 */     armchair.addRequirement(new CreationRequirement(3, 213, 1, true));
/*      */     
/* 2251 */     AdvancedCreationEntry bed = createAdvancedEntry(10044, 482, 483, 484, false, false, 0.0F, true, true, CreationCategories.FURNITURE);
/*      */     
/* 2253 */     bed.addRequirement(new CreationRequirement(1, 485, 1, true));
/* 2254 */     bed.addRequirement(new CreationRequirement(2, 486, 2, true));
/* 2255 */     bed.addRequirement(new CreationRequirement(3, 302, 3, true));
/* 2256 */     AdvancedCreationEntry tentExplorer = createAdvancedEntry(10044, 23, 213, 863, false, false, 0.0F, true, true, CreationCategories.TENTS);
/*      */     
/* 2258 */     tentExplorer.addRequirement(new CreationRequirement(1, 23, 8, true));
/* 2259 */     tentExplorer.addRequirement(new CreationRequirement(2, 213, 8, true));
/* 2260 */     tentExplorer.addRequirement(new CreationRequirement(3, 559, 6, true));
/* 2261 */     tentExplorer.addRequirement(new CreationRequirement(4, 561, 6, true));
/*      */     
/* 2263 */     AdvancedCreationEntry tentMilitary = createAdvancedEntry(10044, 23, 213, 864, false, false, 0.0F, true, true, CreationCategories.TENTS);
/*      */     
/* 2265 */     tentMilitary.addRequirement(new CreationRequirement(1, 23, 10, true));
/* 2266 */     tentMilitary.addRequirement(new CreationRequirement(2, 213, 12, true));
/* 2267 */     tentMilitary.addRequirement(new CreationRequirement(3, 559, 10, true));
/* 2268 */     tentMilitary.addRequirement(new CreationRequirement(4, 561, 10, true));
/*      */     
/* 2270 */     AdvancedCreationEntry pavilion = createAdvancedEntry(10044, 23, 213, 865, false, false, 0.0F, true, true, CreationCategories.TENTS);
/*      */     
/* 2272 */     pavilion.addRequirement(new CreationRequirement(1, 23, 10, true));
/* 2273 */     pavilion.addRequirement(new CreationRequirement(2, 213, 6, true));
/* 2274 */     pavilion.addRequirement(new CreationRequirement(3, 559, 10, true));
/* 2275 */     pavilion.addRequirement(new CreationRequirement(4, 561, 10, true));
/*      */     
/* 2277 */     AdvancedCreationEntry bedframe = createAdvancedEntry(10044, 217, 22, 483, false, false, 0.0F, true, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 2279 */     bedframe.addRequirement(new CreationRequirement(1, 22, 9, true));
/*      */     
/* 2281 */     AdvancedCreationEntry bedheadboard = createAdvancedEntry(10044, 218, 22, 482, false, false, 0.0F, true, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 2283 */     bedheadboard.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2284 */     bedheadboard.addRequirement(new CreationRequirement(2, 23, 2, true));
/*      */     
/* 2286 */     AdvancedCreationEntry bedfootboard = createAdvancedEntry(10044, 218, 22, 485, false, false, 0.0F, true, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */     
/* 2288 */     bedfootboard.addRequirement(new CreationRequirement(1, 22, 1, true));
/* 2289 */     bedfootboard.addRequirement(new CreationRequirement(2, 23, 2, true));
/*      */     
/* 2291 */     AdvancedCreationEntry altarwood = createAdvancedEntry(10044, 218, 22, 322, false, false, 0.0F, true, true, CreationCategories.ALTAR);
/*      */     
/* 2293 */     altarwood.addRequirement(new CreationRequirement(1, 22, 8, true));
/* 2294 */     altarwood.addRequirement(new CreationRequirement(2, 213, 2, true));
/* 2295 */     altarwood.addRequirement(new CreationRequirement(3, 326, 1, true));
/*      */     
/* 2297 */     AdvancedCreationEntry altarStone = createAdvancedEntry(1013, 130, 132, 323, false, false, 0.0F, true, true, CreationCategories.ALTAR);
/*      */     
/* 2299 */     altarStone.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 2300 */     altarStone.addRequirement(new CreationRequirement(2, 130, 10, true));
/* 2301 */     altarStone.addRequirement(new CreationRequirement(3, 326, 1, true));
/*      */     
/* 2303 */     AdvancedCreationEntry chestl = createAdvancedEntry(10044, 188, 22, 184, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 2305 */     chestl.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2306 */     chestl.addRequirement(new CreationRequirement(2, 188, 2, true));
/* 2307 */     chestl.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 2309 */     AdvancedCreationEntry shieldsm = createAdvancedEntry(10014, 218, 22, 82, false, false, 0.0F, true, false, CreationCategories.SHIELDS);
/*      */     
/* 2311 */     shieldsm.addRequirement(new CreationRequirement(1, 188, 1, true));
/*      */     
/* 2313 */     AdvancedCreationEntry shieldTurtle = createAdvancedEntry(10014, 218, 898, 899, false, false, 0.0F, true, false, CreationCategories.SHIELDS);
/*      */     
/* 2315 */     shieldTurtle.addRequirement(new CreationRequirement(1, 100, 2, true));
/*      */     
/* 2317 */     AdvancedCreationEntry shieldmed = createAdvancedEntry(10014, 218, 22, 84, false, false, 0.0F, true, false, CreationCategories.SHIELDS);
/*      */     
/* 2319 */     shieldmed.addRequirement(new CreationRequirement(1, 22, 1, true));
/* 2320 */     shieldmed.addRequirement(new CreationRequirement(2, 188, 1, true));
/*      */     
/* 2322 */     AdvancedCreationEntry shieldla = createAdvancedEntry(10014, 218, 22, 85, false, false, 0.0F, true, false, CreationCategories.SHIELDS);
/*      */     
/* 2324 */     shieldla.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2325 */     shieldla.addRequirement(new CreationRequirement(2, 188, 1, true));
/*      */     
/* 2327 */     AdvancedCreationEntry towerStone = createAdvancedEntry(1013, 132, 130, 384, false, false, 0.0F, true, true, CreationCategories.TOWERS);
/*      */     
/* 2329 */     towerStone.addRequirement(new CreationRequirement(1, 132, 500, true));
/* 2330 */     towerStone.addRequirement(new CreationRequirement(2, 130, 500, true));
/* 2331 */     towerStone.addRequirement(new CreationRequirement(3, 22, 100, true));
/*      */     
/* 2333 */     AdvancedCreationEntry compass = createAdvancedEntry(1020, 418, 76, 480, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2335 */     compass.addRequirement(new CreationRequirement(1, 215, 1, true));
/*      */     
/* 2337 */     AdvancedCreationEntry puppetFo = createAdvancedEntry(10051, 215, 214, 640, false, true, 0.0F, false, false, CreationCategories.TOYS);
/*      */     
/* 2339 */     puppetFo.addRequirement(new CreationRequirement(1, 213, 1, true));
/* 2340 */     puppetFo.addRequirement(new CreationRequirement(2, 436, 1, true));
/* 2341 */     puppetFo.addRequirement(new CreationRequirement(3, 23, 1, true));
/*      */     
/* 2343 */     AdvancedCreationEntry puppetVynora = createAdvancedEntry(10051, 215, 214, 642, false, true, 0.0F, false, false, CreationCategories.TOYS);
/*      */     
/* 2345 */     puppetVynora.addRequirement(new CreationRequirement(1, 213, 1, true));
/* 2346 */     puppetVynora.addRequirement(new CreationRequirement(2, 364, 1, true));
/* 2347 */     puppetVynora.addRequirement(new CreationRequirement(3, 23, 1, true));
/*      */     
/* 2349 */     AdvancedCreationEntry puppetLibila = createAdvancedEntry(10051, 215, 214, 643, false, true, 0.0F, false, false, CreationCategories.TOYS);
/*      */     
/* 2351 */     puppetLibila.addRequirement(new CreationRequirement(1, 213, 1, true));
/* 2352 */     puppetLibila.addRequirement(new CreationRequirement(2, 204, 1, true));
/* 2353 */     puppetLibila.addRequirement(new CreationRequirement(3, 23, 1, true));
/*      */     
/* 2355 */     AdvancedCreationEntry puppetMagranon = createAdvancedEntry(10051, 215, 214, 641, false, true, 0.0F, false, false, CreationCategories.TOYS);
/*      */     
/* 2357 */     puppetMagranon.addRequirement(new CreationRequirement(1, 213, 1, true));
/* 2358 */     puppetMagranon.addRequirement(new CreationRequirement(2, 439, 1, true));
/* 2359 */     puppetMagranon.addRequirement(new CreationRequirement(3, 23, 1, true));
/*      */     
/* 2361 */     AdvancedCreationEntry epicPortal = createAdvancedEntry(1013, 9, 132, 732, false, false, 0.0F, true, true, CreationCategories.EPIC);
/*      */     
/* 2363 */     epicPortal.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 2364 */     epicPortal.addRequirement(new CreationRequirement(2, 9, 3, true));
/*      */     
/* 2366 */     AdvancedCreationEntry mailboxStone = createAdvancedEntry(1013, 132, 130, 511, false, false, 0.0F, true, true, CreationCategories.MAILBOXES);
/*      */     
/* 2368 */     mailboxStone.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 2369 */     mailboxStone.addRequirement(new CreationRequirement(2, 130, 10, true));
/*      */     
/* 2371 */     AdvancedCreationEntry mailboxStone2 = createAdvancedEntry(1013, 132, 130, 513, false, false, 0.0F, true, true, CreationCategories.MAILBOXES);
/*      */     
/* 2373 */     mailboxStone2.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 2374 */     mailboxStone2.addRequirement(new CreationRequirement(2, 130, 10, true));
/* 2375 */     mailboxStone2.addRequirement(new CreationRequirement(3, 213, 2, true));
/*      */     
/* 2377 */     AdvancedCreationEntry mailboxWood = createAdvancedEntry(10044, 218, 22, 510, false, false, 0.0F, true, true, CreationCategories.MAILBOXES);
/*      */     
/* 2379 */     mailboxWood.addRequirement(new CreationRequirement(1, 22, 8, true));
/* 2380 */     mailboxWood.addRequirement(new CreationRequirement(2, 218, 2, true));
/*      */     
/* 2382 */     AdvancedCreationEntry mailboxWood2 = createAdvancedEntry(10044, 218, 22, 512, false, false, 0.0F, true, true, CreationCategories.MAILBOXES);
/*      */     
/* 2384 */     mailboxWood2.addRequirement(new CreationRequirement(1, 22, 8, true));
/* 2385 */     mailboxWood2.addRequirement(new CreationRequirement(2, 218, 2, true));
/* 2386 */     mailboxWood2.addRequirement(new CreationRequirement(3, 213, 2, true));
/*      */     
/* 2388 */     AdvancedCreationEntry toolbelt = createAdvancedEntry(10017, 517, 102, 516, false, false, 0.0F, true, false, CreationCategories.CLOTHES);
/*      */     
/* 2390 */     toolbelt.addRequirement(new CreationRequirement(1, 72, 1, true));
/* 2391 */     toolbelt.addRequirement(new CreationRequirement(2, 213, 1, true));
/*      */     
/* 2393 */     AdvancedCreationEntry trapSticks = createAdvancedEntry(1005, 217, 22, 610, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2395 */     trapSticks.addRequirement(new CreationRequirement(1, 99, 10, true));
/* 2396 */     trapSticks.addRequirement(new CreationRequirement(2, 22, 4, true));
/*      */     
/* 2398 */     AdvancedCreationEntry trapPole = createAdvancedEntry(1005, 217, 22, 611, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2400 */     trapPole.addRequirement(new CreationRequirement(1, 9, 1, true));
/* 2401 */     trapPole.addRequirement(new CreationRequirement(2, 22, 4, true));
/* 2402 */     trapPole.addRequirement(new CreationRequirement(3, 319, 1, true));
/*      */     
/* 2404 */     AdvancedCreationEntry trapCorrosion = createAdvancedEntry(1011, 217, 22, 612, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2406 */     trapCorrosion.addRequirement(new CreationRequirement(1, 78, 10, true));
/* 2407 */     trapCorrosion.addRequirement(new CreationRequirement(2, 73, 4, true));
/* 2408 */     trapCorrosion.addRequirement(new CreationRequirement(3, 457, 1, true));
/*      */     
/* 2410 */     AdvancedCreationEntry trapAxe = createAdvancedEntry(1005, 217, 22, 613, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2412 */     trapAxe.addRequirement(new CreationRequirement(1, 90, 1, true));
/* 2413 */     trapAxe.addRequirement(new CreationRequirement(2, 22, 4, true));
/* 2414 */     trapAxe.addRequirement(new CreationRequirement(3, 457, 1, true));
/* 2415 */     trapAxe.addRequirement(new CreationRequirement(4, 609, 1, true));
/*      */     
/* 2417 */     AdvancedCreationEntry trapDagger = createAdvancedEntry(1005, 217, 22, 614, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2419 */     trapDagger.addRequirement(new CreationRequirement(1, 126, 10, true));
/* 2420 */     trapDagger.addRequirement(new CreationRequirement(2, 22, 4, true));
/*      */     
/* 2422 */     AdvancedCreationEntry trapNet = createAdvancedEntry(1014, 319, 319, 615, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2424 */     trapNet.addRequirement(new CreationRequirement(1, 559, 5, true));
/* 2425 */     trapNet.addRequirement(new CreationRequirement(2, 23, 1, true));
/*      */     
/* 2427 */     AdvancedCreationEntry trapScythe = createAdvancedEntry(1013, 132, 130, 616, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2429 */     trapScythe.addRequirement(new CreationRequirement(1, 132, 10, true));
/* 2430 */     trapScythe.addRequirement(new CreationRequirement(2, 130, 10, true));
/* 2431 */     trapScythe.addRequirement(new CreationRequirement(3, 270, 1, true));
/* 2432 */     trapScythe.addRequirement(new CreationRequirement(4, 609, 1, true));
/*      */     
/* 2434 */     AdvancedCreationEntry trapMan = createAdvancedEntry(10015, 582, 582, 617, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2436 */     trapMan.addRequirement(new CreationRequirement(1, 609, 1, true));
/*      */     
/* 2438 */     AdvancedCreationEntry trapBow = createAdvancedEntry(1005, 217, 22, 618, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2440 */     trapBow.addRequirement(new CreationRequirement(1, 22, 1, true));
/* 2441 */     trapBow.addRequirement(new CreationRequirement(2, 447, 1, true));
/* 2442 */     trapBow.addRequirement(new CreationRequirement(3, 457, 1, true));
/* 2443 */     trapBow.addRequirement(new CreationRequirement(4, 456, 1, true));
/*      */     
/* 2445 */     AdvancedCreationEntry trapRope = createAdvancedEntry(1014, 319, 559, 619, false, false, 0.0F, true, false, CreationCategories.TRAPS);
/*      */     
/* 2447 */     trapRope.addRequirement(new CreationRequirement(1, 23, 1, true));
/* 2448 */     trapRope.addRequirement(new CreationRequirement(2, 457, 1, true));
/*      */     
/* 2450 */     AdvancedCreationEntry villageBoard = createAdvancedEntry(1005, 23, 22, 835, false, false, 0.0F, true, true, CreationCategories.SIGNS);
/*      */     
/* 2452 */     villageBoard.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2453 */     villageBoard.addRequirement(new CreationRequirement(2, 23, 1, true));
/* 2454 */     villageBoard.addRequirement(new CreationRequirement(3, 218, 3, true));
/*      */     
/* 2456 */     AdvancedCreationEntry copperBrazier = createAdvancedEntry(10015, 838, 839, 841, false, false, 0.0F, true, true, 20, 30.0D, CreationCategories.LIGHTS_AND_LAMPS);
/*      */ 
/*      */     
/* 2459 */     copperBrazier.addRequirement(new CreationRequirement(1, 838, 2, true));
/*      */     
/* 2461 */     AdvancedCreationEntry marbleBrazierPillar = createAdvancedEntry(1013, 786, 492, 842, false, false, 0.0F, true, true, 20, 30.0D, CreationCategories.LIGHTS_AND_LAMPS);
/*      */ 
/*      */     
/* 2464 */     marbleBrazierPillar.addRequirement(new CreationRequirement(1, 786, 49, true));
/* 2465 */     marbleBrazierPillar.addRequirement(new CreationRequirement(2, 492, 49, true));
/* 2466 */     marbleBrazierPillar.addRequirement(new CreationRequirement(3, 840, 1, true));
/*      */     
/* 2468 */     AdvancedCreationEntry wagon = createAdvancedEntry(10044, 22, 191, 850, false, false, 0.0F, true, true, 0, 40.0D, CreationCategories.CARTS);
/*      */     
/* 2470 */     wagon.addRequirement(new CreationRequirement(1, 191, 1, true));
/* 2471 */     wagon.addRequirement(new CreationRequirement(2, 22, 20, true));
/* 2472 */     wagon.addRequirement(new CreationRequirement(3, 23, 4, true));
/* 2473 */     wagon.addRequirement(new CreationRequirement(4, 218, 10, true));
/* 2474 */     wagon.addRequirement(new CreationRequirement(5, 632, 2, true));
/* 2475 */     wagon.addRequirement(new CreationRequirement(6, 486, 2, true));
/* 2476 */     wagon.setIsEpicBuildMissionTarget(false);
/*      */     
/* 2478 */     AdvancedCreationEntry smallCrate = createAdvancedEntry(1005, 22, 217, 851, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.STORAGE);
/*      */     
/* 2480 */     smallCrate.addRequirement(new CreationRequirement(1, 22, 10, true));
/*      */     
/* 2482 */     AdvancedCreationEntry largeCrate = createAdvancedEntry(1005, 22, 217, 852, false, false, 0.0F, true, true, 0, 60.0D, CreationCategories.STORAGE);
/*      */     
/* 2484 */     largeCrate.addRequirement(new CreationRequirement(1, 22, 15, true));
/*      */     
/* 2486 */     AdvancedCreationEntry shipCarrier = createAdvancedEntry(10044, 22, 191, 853, false, false, 0.0F, true, true, 0, 15.0D, CreationCategories.CARTS);
/*      */     
/* 2488 */     shipCarrier.addRequirement(new CreationRequirement(1, 191, 1, true));
/* 2489 */     shipCarrier.addRequirement(new CreationRequirement(2, 22, 9, true));
/* 2490 */     shipCarrier.addRequirement(new CreationRequirement(3, 23, 4, true));
/* 2491 */     shipCarrier.addRequirement(new CreationRequirement(4, 218, 4, true));
/* 2492 */     shipCarrier.addRequirement(new CreationRequirement(5, 632, 1, true));
/* 2493 */     shipCarrier.addRequirement(new CreationRequirement(6, 9, 2, true));
/*      */     
/* 2495 */     AdvancedCreationEntry creatureCarrier = createAdvancedEntry(10044, 22, 191, 1410, false, false, 0.0F, true, true, 0, 25.0D, CreationCategories.CARTS);
/*      */     
/* 2497 */     creatureCarrier.addRequirement(new CreationRequirement(1, 191, 1, true));
/* 2498 */     creatureCarrier.addRequirement(new CreationRequirement(2, 22, 9, true));
/* 2499 */     creatureCarrier.addRequirement(new CreationRequirement(3, 23, 4, true));
/* 2500 */     creatureCarrier.addRequirement(new CreationRequirement(4, 218, 4, true));
/* 2501 */     creatureCarrier.addRequirement(new CreationRequirement(5, 632, 1, true));
/* 2502 */     creatureCarrier.addRequirement(new CreationRequirement(6, 9, 2, true));
/*      */     
/* 2504 */     AdvancedCreationEntry colossusOfVynora = createAdvancedEntry(1013, 519, 130, 869, false, false, 0.0F, true, true, CreationCategories.STATUES);
/*      */ 
/*      */     
/* 2507 */     colossusOfVynora.setDeityRestriction(3);
/* 2508 */     colossusOfVynora.addRequirement(new CreationRequirement(1, 519, 1999, true));
/* 2509 */     colossusOfVynora.addRequirement(new CreationRequirement(2, 130, 1999, true));
/* 2510 */     colossusOfVynora.addRequirement(new CreationRequirement(3, 599, 10, true));
/* 2511 */     colossusOfVynora.setIsEpicBuildMissionTarget(false);
/*      */     
/* 2513 */     AdvancedCreationEntry colossusOfMagranon = createAdvancedEntry(1013, 519, 130, 870, false, false, 0.0F, true, true, CreationCategories.STATUES);
/*      */ 
/*      */     
/* 2516 */     colossusOfMagranon.setDeityRestriction(2);
/* 2517 */     colossusOfMagranon.addRequirement(new CreationRequirement(1, 519, 1999, true));
/* 2518 */     colossusOfMagranon.addRequirement(new CreationRequirement(2, 130, 1999, true));
/* 2519 */     colossusOfMagranon.addRequirement(new CreationRequirement(3, 598, 10, true));
/* 2520 */     colossusOfMagranon.setIsEpicBuildMissionTarget(false);
/*      */     
/* 2522 */     AdvancedCreationEntry bedsideTable = createAdvancedEntry(10044, 23, 22, 885, false, false, 0.0F, true, true, 0, 25.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2525 */     bedsideTable.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2526 */     bedsideTable.addRequirement(new CreationRequirement(2, 23, 3, true));
/* 2527 */     bedsideTable.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 2529 */     AdvancedCreationEntry colossusOfFo = createAdvancedEntry(1013, 519, 130, 907, false, false, 0.0F, true, true, CreationCategories.STATUES);
/*      */ 
/*      */     
/* 2532 */     colossusOfFo.setDeityRestriction(1);
/* 2533 */     colossusOfFo.addRequirement(new CreationRequirement(1, 519, 1999, true));
/* 2534 */     colossusOfFo.addRequirement(new CreationRequirement(2, 130, 1999, true));
/* 2535 */     colossusOfFo.addRequirement(new CreationRequirement(3, 598, 10, true));
/* 2536 */     colossusOfFo.setIsEpicBuildMissionTarget(false);
/*      */     
/* 2538 */     AdvancedCreationEntry colossusOfLibila = createAdvancedEntry(1013, 519, 130, 916, false, false, 0.0F, true, true, CreationCategories.STATUES);
/*      */ 
/*      */     
/* 2541 */     colossusOfLibila.setDeityRestriction(4);
/* 2542 */     colossusOfLibila.addRequirement(new CreationRequirement(1, 519, 1999, true));
/* 2543 */     colossusOfLibila.addRequirement(new CreationRequirement(2, 130, 1999, true));
/* 2544 */     colossusOfLibila.addRequirement(new CreationRequirement(3, 772, 10, true));
/* 2545 */     colossusOfLibila.setIsEpicBuildMissionTarget(false);
/*      */     
/* 2547 */     createSimpleEntry(10015, 185, 221, 897, false, true, 0.0F, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
/*      */ 
/*      */     
/* 2550 */     AdvancedCreationEntry openFireplace = createAdvancedEntry(1013, 492, 132, 889, false, false, 0.0F, true, true, 0, 35.0D, CreationCategories.FIRE);
/*      */ 
/*      */     
/* 2553 */     openFireplace.addRequirement(new CreationRequirement(1, 132, 9, true));
/* 2554 */     openFireplace.addRequirement(new CreationRequirement(2, 492, 9, true));
/* 2555 */     openFireplace.addRequirement(new CreationRequirement(3, 22, 2, true));
/*      */     
/* 2557 */     AdvancedCreationEntry canopyBed = createAdvancedEntry(10044, 482, 483, 890, false, false, 0.0F, true, true, 0, 65.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2560 */     canopyBed.addRequirement(new CreationRequirement(1, 485, 1, true));
/* 2561 */     canopyBed.addRequirement(new CreationRequirement(2, 22, 10, true));
/* 2562 */     canopyBed.addRequirement(new CreationRequirement(3, 486, 8, true));
/* 2563 */     canopyBed.addRequirement(new CreationRequirement(4, 302, 8, true));
/* 2564 */     canopyBed.addRequirement(new CreationRequirement(5, 217, 2, true));
/* 2565 */     canopyBed.addRequirement(new CreationRequirement(6, 218, 2, true));
/*      */     
/* 2567 */     AdvancedCreationEntry woodenBench = createAdvancedEntry(10044, 218, 22, 891, false, false, 0.0F, true, true, 0, 35.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2570 */     woodenBench.addRequirement(new CreationRequirement(1, 22, 7, true));
/* 2571 */     woodenBench.addRequirement(new CreationRequirement(2, 218, 3, true));
/* 2572 */     woodenBench.addRequirement(new CreationRequirement(3, 23, 4, true));
/* 2573 */     woodenBench.addRequirement(new CreationRequirement(4, 188, 2, true));
/*      */     
/* 2575 */     AdvancedCreationEntry wardrobe = createAdvancedEntry(10044, 217, 22, 892, false, false, 0.0F, true, true, 0, 55.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2578 */     wardrobe.addRequirement(new CreationRequirement(1, 22, 11, true));
/* 2579 */     wardrobe.addRequirement(new CreationRequirement(2, 23, 4, true));
/* 2580 */     wardrobe.addRequirement(new CreationRequirement(3, 217, 3, true));
/* 2581 */     wardrobe.addRequirement(new CreationRequirement(4, 218, 2, true));
/*      */     
/* 2583 */     AdvancedCreationEntry woodenCoffer = createAdvancedEntry(10044, 218, 22, 893, false, false, 0.0F, true, true, 0, 24.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2586 */     woodenCoffer.addRequirement(new CreationRequirement(1, 22, 5, true));
/* 2587 */     woodenCoffer.addRequirement(new CreationRequirement(2, 218, 2, true));
/*      */     
/* 2589 */     AdvancedCreationEntry royalThrone = createAdvancedEntry(10044, 217, 22, 894, false, false, 0.0F, true, true, 0, 70.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2592 */     royalThrone.addRequirement(new CreationRequirement(1, 22, 7, true));
/* 2593 */     royalThrone.addRequirement(new CreationRequirement(2, 23, 2, true));
/* 2594 */     royalThrone.addRequirement(new CreationRequirement(3, 217, 3, true));
/* 2595 */     royalThrone.addRequirement(new CreationRequirement(4, 218, 6, true));
/*      */     
/* 2597 */     AdvancedCreationEntry washingBowl = createAdvancedEntry(10015, 64, 221, 895, false, true, 0.0F, false, true, 0, 30.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2600 */     washingBowl.setDepleteFromTarget(1500);
/* 2601 */     washingBowl.addRequirement(new CreationRequirement(1, 897, 3, true));
/* 2602 */     washingBowl.addRequirement(new CreationRequirement(2, 77, 1, true));
/*      */     
/* 2604 */     AdvancedCreationEntry tripodTableSmall = createAdvancedEntry(10044, 22, 23, 896, false, false, 0.0F, true, true, 0, 30.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2607 */     tripodTableSmall.addRequirement(new CreationRequirement(1, 22, 1, true));
/* 2608 */     tripodTableSmall.addRequirement(new CreationRequirement(2, 23, 1, true));
/* 2609 */     tripodTableSmall.addRequirement(new CreationRequirement(3, 218, 2, true));
/*      */     
/* 2611 */     AdvancedCreationEntry highBookshelf = createAdvancedEntry(10044, 217, 22, 911, false, false, 0.0F, true, true, 0, 35.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2614 */     highBookshelf.addRequirement(new CreationRequirement(1, 22, 7, true));
/* 2615 */     highBookshelf.addRequirement(new CreationRequirement(2, 23, 2, true));
/* 2616 */     highBookshelf.addRequirement(new CreationRequirement(3, 217, 3, true));
/*      */     
/* 2618 */     AdvancedCreationEntry lowBookshelf = createAdvancedEntry(10044, 217, 22, 912, false, false, 0.0F, true, true, 0, 25.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2621 */     lowBookshelf.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2622 */     lowBookshelf.addRequirement(new CreationRequirement(2, 23, 1, true));
/* 2623 */     lowBookshelf.addRequirement(new CreationRequirement(3, 217, 1, true));
/*      */     
/* 2625 */     AdvancedCreationEntry emptyHighBookshelf = createAdvancedEntry(10044, 217, 22, 1401, false, false, 0.0F, true, true, 0, 35.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2628 */     emptyHighBookshelf.addRequirement(new CreationRequirement(1, 22, 7, true));
/* 2629 */     emptyHighBookshelf.addRequirement(new CreationRequirement(2, 23, 2, true));
/* 2630 */     emptyHighBookshelf.addRequirement(new CreationRequirement(3, 217, 3, true));
/*      */     
/* 2632 */     AdvancedCreationEntry emptyLowBookshelf = createAdvancedEntry(10044, 217, 22, 1400, false, false, 0.0F, true, true, 0, 25.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2635 */     emptyLowBookshelf.addRequirement(new CreationRequirement(1, 22, 3, true));
/* 2636 */     emptyLowBookshelf.addRequirement(new CreationRequirement(2, 23, 1, true));
/* 2637 */     emptyLowBookshelf.addRequirement(new CreationRequirement(3, 217, 1, true));
/*      */     
/* 2639 */     AdvancedCreationEntry barTable = createAdvancedEntry(10044, 218, 22, 1402, false, false, 0.0F, true, true, 0, 25.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2642 */     barTable.addRequirement(new CreationRequirement(1, 22, 14, true));
/* 2643 */     barTable.addRequirement(new CreationRequirement(2, 188, 2, true));
/* 2644 */     barTable.addRequirement(new CreationRequirement(3, 218, 4, true));
/*      */     
/* 2646 */     AdvancedCreationEntry fineHighChair = createAdvancedEntry(10044, 218, 22, 913, false, false, 0.0F, true, true, 0, 60.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2649 */     fineHighChair.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2650 */     fineHighChair.addRequirement(new CreationRequirement(2, 213, 2, true));
/* 2651 */     fineHighChair.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 2653 */     AdvancedCreationEntry highChair = createAdvancedEntry(10044, 218, 22, 914, false, false, 0.0F, true, true, 0, 50.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2656 */     highChair.addRequirement(new CreationRequirement(1, 22, 2, true));
/* 2657 */     highChair.addRequirement(new CreationRequirement(2, 188, 2, true));
/* 2658 */     highChair.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 2660 */     AdvancedCreationEntry pauperHighChair = createAdvancedEntry(10044, 218, 22, 915, false, false, 0.0F, true, true, 0, 40.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2663 */     pauperHighChair.addRequirement(new CreationRequirement(1, 22, 1, true));
/* 2664 */     pauperHighChair.addRequirement(new CreationRequirement(2, 23, 3, true));
/* 2665 */     pauperHighChair.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 2667 */     if (Features.Feature.AMPHORA.isEnabled()) {
/*      */       
/* 2669 */       AdvancedCreationEntry kiln = createAdvancedEntry(1013, 132, 132, 1023, false, false, 0.0F, true, true, CreationCategories.FIRE);
/*      */       
/* 2671 */       kiln.addRequirement(new CreationRequirement(1, 132, 18, true));
/* 2672 */       kiln.addRequirement(new CreationRequirement(2, 130, 20, true));
/* 2673 */       kiln.addRequirement(new CreationRequirement(3, 26, 2, true));
/*      */     } 
/*      */     
/* 2676 */     AdvancedCreationEntry smelter = createAdvancedEntry(1013, 132, 132, 1028, false, false, 0.0F, true, true, 0, 50.0D, CreationCategories.FIRE);
/*      */     
/* 2678 */     smelter.addRequirement(new CreationRequirement(1, 132, 48, true));
/* 2679 */     smelter.addRequirement(new CreationRequirement(2, 130, 50, true));
/* 2680 */     smelter.addRequirement(new CreationRequirement(3, 298, 5, true));
/*      */     
/* 2682 */     AdvancedCreationEntry simpleDioptra = createAdvancedEntry(10015, 902, 904, 903, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2684 */     simpleDioptra.addRequirement(new CreationRequirement(1, 480, 1, true));
/* 2685 */     simpleDioptra.addRequirement(new CreationRequirement(2, 23, 3, true));
/*      */     
/* 2687 */     AdvancedCreationEntry rangePole = createAdvancedEntry(1005, 711, 213, 901, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 2689 */     rangePole.addRequirement(new CreationRequirement(1, 213, 3, true));
/* 2690 */     rangePole.addRequirement(new CreationRequirement(2, 439, 8, true));
/*      */     
/* 2692 */     AdvancedCreationEntry pewpewdie = createAdvancedEntry(10044, 217, 22, 934, false, false, 0.0F, true, true, 0, 40.0D, CreationCategories.WARMACHINES);
/*      */ 
/*      */     
/* 2695 */     pewpewdie.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2696 */     pewpewdie.addRequirement(new CreationRequirement(2, 9, 1, true));
/* 2697 */     pewpewdie.addRequirement(new CreationRequirement(3, 859, 3, true));
/* 2698 */     pewpewdie.addRequirement(new CreationRequirement(4, 188, 2, true));
/*      */     
/* 2700 */     AdvancedCreationEntry siegeShield = createAdvancedEntry(1005, 217, 22, 931, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.WARMACHINES);
/*      */ 
/*      */     
/* 2703 */     siegeShield.addRequirement(new CreationRequirement(1, 22, 20, true));
/* 2704 */     siegeShield.addRequirement(new CreationRequirement(2, 9, 2, true));
/*      */     
/* 2706 */     AdvancedCreationEntry ballistaMount = createAdvancedEntry(10044, 217, 9, 933, false, false, 0.0F, true, true, 0, 60.0D, CreationCategories.WARMACHINES);
/*      */ 
/*      */     
/* 2709 */     ballistaMount.addRequirement(new CreationRequirement(1, 22, 8, true));
/*      */     
/* 2711 */     AdvancedCreationEntry barrier = createAdvancedEntry(1005, 217, 9, 938, false, false, 0.0F, true, true, 0, 5.0D, CreationCategories.WARMACHINES);
/*      */ 
/*      */     
/* 2714 */     barrier.addRequirement(new CreationRequirement(1, 23, 21, true));
/*      */     
/* 2716 */     createSimpleEntry(1032, 935, 23, 932, true, true, 0.0F, false, false, CreationCategories.FLETCHING);
/*      */     
/* 2718 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */       
/* 2720 */       createMetallicEntries(10011, 64, 698, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2722 */       createMetallicEntries(10015, 185, 205, 1115, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2727 */       createSimpleEntry(10011, 64, 698, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2729 */       createSimpleEntry(10011, 64, 694, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2731 */       createSimpleEntry(10011, 64, 837, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2733 */       createSimpleEntry(10011, 64, 205, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2735 */       createSimpleEntry(10011, 64, 47, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2737 */       createSimpleEntry(10011, 64, 45, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2739 */       createSimpleEntry(10011, 64, 44, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2741 */       createSimpleEntry(10011, 64, 223, 935, false, true, 10.0F, false, false, CreationCategories.WEAPON_HEADS);
/*      */       
/* 2743 */       createSimpleEntry(10015, 185, 205, 1115, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */     } 
/*      */ 
/*      */     
/* 2747 */     AdvancedCreationEntry ballista = createAdvancedEntry(10044, 933, 22, 936, false, false, 0.0F, true, true, 0, 60.0D, CreationCategories.WARMACHINES);
/*      */     
/* 2749 */     ballista.addRequirement(new CreationRequirement(1, 559, 4, true));
/* 2750 */     ballista.addRequirement(new CreationRequirement(2, 897, 2, true));
/* 2751 */     ballista.addRequirement(new CreationRequirement(3, 22, 4, true));
/* 2752 */     ballista.addRequirement(new CreationRequirement(4, 218, 1, true));
/* 2753 */     ballista.addRequirement(new CreationRequirement(5, 23, 1, true));
/*      */     
/* 2755 */     AdvancedCreationEntry trebuchet = createAdvancedEntry(10044, 9, 9, 937, false, false, 0.0F, true, true, 0, 80.0D, CreationCategories.WARMACHINES);
/*      */     
/* 2757 */     trebuchet.addRequirement(new CreationRequirement(1, 9, 20, true));
/* 2758 */     trebuchet.addRequirement(new CreationRequirement(2, 319, 5, true));
/* 2759 */     trebuchet.addRequirement(new CreationRequirement(3, 191, 2, true));
/* 2760 */     trebuchet.addRequirement(new CreationRequirement(4, 22, 40, true));
/*      */     
/* 2762 */     AdvancedCreationEntry archeryTower = createAdvancedEntry(10044, 217, 22, 939, false, false, 0.0F, true, true, 0, 40.0D, CreationCategories.WARMACHINES);
/*      */ 
/*      */     
/* 2765 */     archeryTower.addRequirement(new CreationRequirement(1, 22, 200, true));
/* 2766 */     archeryTower.addRequirement(new CreationRequirement(2, 9, 20, true));
/* 2767 */     archeryTower.addRequirement(new CreationRequirement(3, 860, 20, true));
/* 2768 */     archeryTower.addRequirement(new CreationRequirement(4, 217, 6, true));
/*      */     
/* 2770 */     AdvancedCreationEntry spinningWheel = createAdvancedEntry(10044, 187, 23, 922, false, false, 0.0F, true, true, 0, 30.0D, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 2773 */     spinningWheel.addRequirement(new CreationRequirement(1, 23, 1, true));
/* 2774 */     spinningWheel.addRequirement(new CreationRequirement(2, 22, 3, true));
/* 2775 */     spinningWheel.addRequirement(new CreationRequirement(3, 218, 2, true));
/* 2776 */     spinningWheel.addRequirement(new CreationRequirement(4, 153, 2, true));
/*      */     
/* 2778 */     AdvancedCreationEntry loungeChair = createAdvancedEntry(10044, 22, 23, 923, false, false, 0.0F, true, true, 0, 40.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2781 */     loungeChair.addRequirement(new CreationRequirement(1, 22, 5, true));
/* 2782 */     loungeChair.addRequirement(new CreationRequirement(2, 23, 3, true));
/* 2783 */     loungeChair.addRequirement(new CreationRequirement(3, 218, 2, true));
/* 2784 */     loungeChair.addRequirement(new CreationRequirement(4, 153, 4, true));
/*      */     
/* 2786 */     AdvancedCreationEntry royalLoungeChaise = createAdvancedEntry(10044, 22, 23, 924, false, false, 0.0F, true, true, 0, 70.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2789 */     royalLoungeChaise.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2790 */     royalLoungeChaise.addRequirement(new CreationRequirement(2, 23, 1, true));
/* 2791 */     royalLoungeChaise.addRequirement(new CreationRequirement(3, 218, 6, true));
/* 2792 */     royalLoungeChaise.addRequirement(new CreationRequirement(4, 926, 10, true));
/* 2793 */     royalLoungeChaise.addRequirement(new CreationRequirement(5, 925, 4, true));
/*      */     
/* 2795 */     AdvancedCreationEntry woodCupboard = createAdvancedEntry(10044, 218, 22, 927, false, false, 0.0F, true, true, 0, 40.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2798 */     woodCupboard.addRequirement(new CreationRequirement(1, 22, 4, true));
/* 2799 */     woodCupboard.addRequirement(new CreationRequirement(2, 218, 2, true));
/* 2800 */     woodCupboard.addRequirement(new CreationRequirement(3, 23, 2, true));
/* 2801 */     woodCupboard.addRequirement(new CreationRequirement(4, 153, 2, true));
/*      */     
/* 2803 */     AdvancedCreationEntry alchemyCupboard = createAdvancedEntry(10044, 218, 22, 1117, false, false, 0.0F, true, true, 0, 20.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2806 */     alchemyCupboard.addRequirement(new CreationRequirement(1, 22, 5, true));
/* 2807 */     alchemyCupboard.addRequirement(new CreationRequirement(2, 218, 2, true));
/* 2808 */     alchemyCupboard.addRequirement(new CreationRequirement(3, 23, 2, true));
/* 2809 */     alchemyCupboard.addRequirement(new CreationRequirement(4, 1254, 5, true));
/* 2810 */     alchemyCupboard.addRequirement(new CreationRequirement(5, 76, 10, true));
/*      */     
/* 2812 */     AdvancedCreationEntry storageUnit = createAdvancedEntry(1005, 217, 22, 1119, false, false, 0.0F, true, true, 0, 30.0D, CreationCategories.STORAGE);
/*      */ 
/*      */     
/* 2815 */     storageUnit.addRequirement(new CreationRequirement(1, 22, 14, true));
/* 2816 */     storageUnit.addRequirement(new CreationRequirement(2, 217, 3, true));
/* 2817 */     storageUnit.addRequirement(new CreationRequirement(3, 188, 4, true));
/* 2818 */     storageUnit.addRequirement(new CreationRequirement(4, 561, 10, true));
/* 2819 */     storageUnit.addRequirement(new CreationRequirement(5, 289, 3, true));
/*      */     
/* 2821 */     AdvancedCreationEntry roundMarbleTable = createAdvancedEntry(10074, 787, 130, 928, false, false, 0.0F, true, true, 0, 60.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2824 */     roundMarbleTable.addRequirement(new CreationRequirement(1, 787, 1, true));
/* 2825 */     roundMarbleTable.addRequirement(new CreationRequirement(2, 130, 3, true));
/*      */     
/* 2827 */     AdvancedCreationEntry rectagularMarbleTable = createAdvancedEntry(10074, 787, 130, 929, false, false, 0.0F, true, true, 0, 50.0D, CreationCategories.FURNITURE);
/*      */ 
/*      */     
/* 2830 */     rectagularMarbleTable.addRequirement(new CreationRequirement(1, 787, 1, true));
/* 2831 */     rectagularMarbleTable.addRequirement(new CreationRequirement(2, 130, 4, true));
/*      */     
/* 2833 */     AdvancedCreationEntry yellowWoolCap = createAdvancedEntry(10016, 128, 943, 944, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2836 */     yellowWoolCap.setUseTemplateWeight(true);
/* 2837 */     yellowWoolCap.setColouringCreation(true);
/* 2838 */     yellowWoolCap.addRequirement(new CreationRequirement(1, 47, 1, true));
/* 2839 */     yellowWoolCap.addRequirement(new CreationRequirement(2, 128, 1, true));
/* 2840 */     yellowWoolCap.addRequirement(new CreationRequirement(3, 439, 3, true));
/*      */     
/* 2842 */     AdvancedCreationEntry greenWoolCap = createAdvancedEntry(10016, 128, 943, 945, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2845 */     greenWoolCap.setUseTemplateWeight(true);
/* 2846 */     greenWoolCap.setColouringCreation(true);
/* 2847 */     greenWoolCap.addRequirement(new CreationRequirement(1, 47, 1, true));
/*      */     
/* 2849 */     AdvancedCreationEntry redWoolCap = createAdvancedEntry(10016, 128, 943, 946, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2852 */     redWoolCap.setUseTemplateWeight(true);
/* 2853 */     redWoolCap.setColouringCreation(true);
/* 2854 */     redWoolCap.addRequirement(new CreationRequirement(1, 439, 2, true));
/*      */     
/* 2856 */     AdvancedCreationEntry blueWoolCap = createAdvancedEntry(10016, 128, 943, 947, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2859 */     blueWoolCap.setUseTemplateWeight(true);
/* 2860 */     blueWoolCap.setColouringCreation(true);
/* 2861 */     blueWoolCap.addRequirement(new CreationRequirement(1, 440, 2, true));
/*      */     
/* 2863 */     AdvancedCreationEntry NIcoomonWoolHat = createAdvancedEntry(10016, 215, 926, 948, true, false, 5.0F, false, false, 0, 5.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2866 */     NIcoomonWoolHat.depleteSource = false;
/* 2867 */     NIcoomonWoolHat.setDepleteFromTarget(200);
/* 2868 */     NIcoomonWoolHat.addRequirement(new CreationRequirement(1, 925, 1, true));
/*      */     
/* 2870 */     AdvancedCreationEntry CIcoomonWoolHat = createAdvancedEntry(10016, 216, 926, 948, true, false, 5.0F, false, false, 0, 5.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2873 */     CIcoomonWoolHat.depleteSource = false;
/* 2874 */     CIcoomonWoolHat.setDepleteFromTarget(200);
/* 2875 */     CIcoomonWoolHat.addRequirement(new CreationRequirement(1, 925, 1, true));
/*      */     
/* 2877 */     AdvancedCreationEntry coomonWoolHatDark = createAdvancedEntry(10016, 128, 948, 949, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2880 */     coomonWoolHatDark.setUseTemplateWeight(true);
/* 2881 */     coomonWoolHatDark.setColouringCreation(true);
/* 2882 */     coomonWoolHatDark.addRequirement(new CreationRequirement(1, 46, 1, true));
/* 2883 */     coomonWoolHatDark.addRequirement(new CreationRequirement(2, 437, 1, true));
/*      */     
/* 2885 */     AdvancedCreationEntry coomonWoolHatBrown = createAdvancedEntry(10016, 128, 948, 950, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2888 */     coomonWoolHatBrown.setUseTemplateWeight(true);
/* 2889 */     coomonWoolHatBrown.setColouringCreation(true);
/* 2890 */     coomonWoolHatBrown.addRequirement(new CreationRequirement(1, 439, 3, true));
/* 2891 */     coomonWoolHatBrown.addRequirement(new CreationRequirement(2, 47, 2, true));
/* 2892 */     coomonWoolHatBrown.addRequirement(new CreationRequirement(3, 440, 1, true));
/* 2893 */     coomonWoolHatBrown.addRequirement(new CreationRequirement(4, 128, 2, true));
/*      */     
/* 2895 */     AdvancedCreationEntry coomonWoolHatGreen = createAdvancedEntry(10016, 128, 948, 951, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2898 */     coomonWoolHatGreen.setUseTemplateWeight(true);
/* 2899 */     coomonWoolHatGreen.setColouringCreation(true);
/* 2900 */     coomonWoolHatGreen.addRequirement(new CreationRequirement(1, 47, 1, true));
/* 2901 */     coomonWoolHatGreen.addRequirement(new CreationRequirement(2, 128, 1, true));
/*      */     
/* 2903 */     AdvancedCreationEntry coomonWoolHatRed = createAdvancedEntry(10016, 128, 948, 952, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2906 */     coomonWoolHatRed.setUseTemplateWeight(true);
/* 2907 */     coomonWoolHatRed.setColouringCreation(true);
/* 2908 */     coomonWoolHatRed.addRequirement(new CreationRequirement(1, 439, 2, true));
/* 2909 */     coomonWoolHatRed.addRequirement(new CreationRequirement(2, 128, 2, true));
/*      */     
/* 2911 */     AdvancedCreationEntry coomonWoolHatBlue = createAdvancedEntry(10016, 128, 948, 953, true, false, 0.0F, true, false, 0, 20.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2914 */     coomonWoolHatBlue.setUseTemplateWeight(true);
/* 2915 */     coomonWoolHatBlue.setColouringCreation(true);
/* 2916 */     coomonWoolHatBlue.addRequirement(new CreationRequirement(1, 440, 2, true));
/* 2917 */     coomonWoolHatBlue.addRequirement(new CreationRequirement(2, 128, 2, true));
/*      */     
/* 2919 */     AdvancedCreationEntry foresterWoolHatGreen = createAdvancedEntry(10016, 128, 954, 955, true, false, 0.0F, true, false, 0, 10.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2922 */     foresterWoolHatGreen.setUseTemplateWeight(true);
/* 2923 */     foresterWoolHatGreen.setColouringCreation(true);
/* 2924 */     foresterWoolHatGreen.addRequirement(new CreationRequirement(1, 47, 1, true));
/*      */     
/* 2926 */     AdvancedCreationEntry foresterWoolHatDark = createAdvancedEntry(10016, 128, 954, 956, true, false, 0.0F, true, false, 0, 10.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2929 */     foresterWoolHatDark.setUseTemplateWeight(true);
/* 2930 */     foresterWoolHatDark.setColouringCreation(true);
/* 2931 */     foresterWoolHatDark.addRequirement(new CreationRequirement(1, 46, 1, true));
/* 2932 */     foresterWoolHatDark.addRequirement(new CreationRequirement(2, 437, 1, true));
/*      */     
/* 2934 */     AdvancedCreationEntry foresterWoolHatBlue = createAdvancedEntry(10016, 128, 954, 957, true, false, 0.0F, true, false, 0, 10.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2937 */     foresterWoolHatBlue.setUseTemplateWeight(true);
/* 2938 */     foresterWoolHatBlue.setColouringCreation(true);
/* 2939 */     foresterWoolHatBlue.addRequirement(new CreationRequirement(1, 440, 2, true));
/*      */     
/* 2941 */     AdvancedCreationEntry foresterWoolHatRed = createAdvancedEntry(10016, 128, 954, 958, true, false, 0.0F, true, false, 0, 10.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2944 */     foresterWoolHatRed.setUseTemplateWeight(true);
/* 2945 */     foresterWoolHatRed.setColouringCreation(true);
/* 2946 */     foresterWoolHatRed.addRequirement(new CreationRequirement(1, 439, 2, true));
/*      */     
/* 2948 */     AdvancedCreationEntry squireWoolCapGreen = createAdvancedEntry(10016, 128, 961, 962, true, false, 0.0F, true, false, 0, 25.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2951 */     squireWoolCapGreen.setUseTemplateWeight(true);
/* 2952 */     squireWoolCapGreen.setColouringCreation(true);
/* 2953 */     squireWoolCapGreen.addRequirement(new CreationRequirement(1, 47, 1, true));
/*      */     
/* 2955 */     AdvancedCreationEntry squireWoolCapBlue = createAdvancedEntry(10016, 128, 961, 963, true, false, 0.0F, true, false, 0, 25.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2958 */     squireWoolCapBlue.setUseTemplateWeight(true);
/* 2959 */     squireWoolCapBlue.setColouringCreation(true);
/* 2960 */     squireWoolCapBlue.addRequirement(new CreationRequirement(1, 440, 2, true));
/*      */     
/* 2962 */     AdvancedCreationEntry squireWoolCapBlack = createAdvancedEntry(10016, 128, 961, 964, true, false, 0.0F, true, false, 0, 25.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2965 */     squireWoolCapBlack.setUseTemplateWeight(true);
/* 2966 */     squireWoolCapBlack.setColouringCreation(true);
/* 2967 */     squireWoolCapBlack.addRequirement(new CreationRequirement(1, 46, 1, true));
/* 2968 */     squireWoolCapBlack.addRequirement(new CreationRequirement(2, 437, 1, true));
/*      */     
/* 2970 */     AdvancedCreationEntry squireWoolCapRed = createAdvancedEntry(10016, 128, 961, 965, true, false, 0.0F, true, false, 0, 25.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2973 */     squireWoolCapRed.setUseTemplateWeight(true);
/* 2974 */     squireWoolCapRed.setColouringCreation(true);
/* 2975 */     squireWoolCapRed.addRequirement(new CreationRequirement(1, 439, 2, true));
/*      */     
/* 2977 */     AdvancedCreationEntry squireWoolCapYellow = createAdvancedEntry(10016, 128, 961, 966, true, false, 0.0F, true, false, 0, 25.0D, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 2980 */     squireWoolCapYellow.setUseTemplateWeight(true);
/* 2981 */     squireWoolCapYellow.setColouringCreation(true);
/* 2982 */     squireWoolCapYellow.addRequirement(new CreationRequirement(1, 47, 1, true));
/* 2983 */     squireWoolCapYellow.addRequirement(new CreationRequirement(2, 128, 1, true));
/* 2984 */     squireWoolCapYellow.addRequirement(new CreationRequirement(3, 439, 2, true));
/*      */     
/* 2986 */     AdvancedCreationEntry grapeTrellis = createAdvancedEntry(10045, 918, 23, 920, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 2989 */     grapeTrellis.addRequirement(new CreationRequirement(1, 23, 5, true));
/* 2990 */     grapeTrellis.addRequirement(new CreationRequirement(2, 218, 1, true));
/*      */     
/* 2992 */     AdvancedCreationEntry grapeTrellis1 = createAdvancedEntry(10045, 266, 23, 920, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 2995 */     grapeTrellis1.setObjectSourceMaterial((byte)49);
/* 2996 */     grapeTrellis1.addRequirement(new CreationRequirement(1, 23, 5, true));
/* 2997 */     grapeTrellis1.addRequirement(new CreationRequirement(2, 218, 1, true));
/*      */     
/* 2999 */     AdvancedCreationEntry ivyTrellis = createAdvancedEntry(10045, 917, 23, 919, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3002 */     ivyTrellis.addRequirement(new CreationRequirement(1, 23, 5, true));
/* 3003 */     ivyTrellis.addRequirement(new CreationRequirement(2, 218, 1, true));
/*      */     
/* 3005 */     AdvancedCreationEntry roseTrellis = createAdvancedEntry(10045, 1017, 23, 1018, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3008 */     roseTrellis.addRequirement(new CreationRequirement(1, 23, 5, true));
/* 3009 */     roseTrellis.addRequirement(new CreationRequirement(2, 218, 1, true));
/*      */     
/* 3011 */     AdvancedCreationEntry roseTrellis1 = createAdvancedEntry(10045, 266, 23, 1018, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3014 */     roseTrellis1.setObjectSourceMaterial((byte)47);
/* 3015 */     roseTrellis1.addRequirement(new CreationRequirement(1, 23, 5, true));
/* 3016 */     roseTrellis1.addRequirement(new CreationRequirement(2, 218, 1, true));
/*      */     
/* 3018 */     AdvancedCreationEntry hopsTrellis = createAdvancedEntry(10045, 1275, 23, 1274, false, false, 0.0F, true, true, 0, 10.0D, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3021 */     hopsTrellis.addRequirement(new CreationRequirement(1, 23, 5, true));
/* 3022 */     hopsTrellis.addRequirement(new CreationRequirement(2, 218, 1, true));
/*      */     
/* 3024 */     AdvancedCreationEntry tapestryStand = createAdvancedEntry(10044, 22, 23, 987, false, false, 0.0F, true, true, CreationCategories.RESOURCES);
/*      */     
/* 3026 */     tapestryStand.addRequirement(new CreationRequirement(1, 23, 3, true));
/* 3027 */     tapestryStand.addRequirement(new CreationRequirement(2, 218, 2, true));
/*      */     
/* 3029 */     AdvancedCreationEntry tapestryP1 = createAdvancedEntry(10016, 926, 987, 988, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 3031 */     tapestryP1.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3032 */     tapestryP1.addRequirement(new CreationRequirement(2, 925, 2, true));
/*      */     
/* 3034 */     AdvancedCreationEntry tapestryP2 = createAdvancedEntry(10016, 926, 987, 989, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 3036 */     tapestryP2.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3037 */     tapestryP2.addRequirement(new CreationRequirement(2, 925, 2, true));
/*      */     
/* 3039 */     AdvancedCreationEntry tapestryP3 = createAdvancedEntry(10016, 926, 987, 990, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 3041 */     tapestryP3.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3042 */     tapestryP3.addRequirement(new CreationRequirement(2, 925, 2, true));
/*      */     
/* 3044 */     AdvancedCreationEntry tapestryM1 = createAdvancedEntry(10016, 926, 987, 991, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 3046 */     tapestryM1.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3047 */     tapestryM1.addRequirement(new CreationRequirement(2, 925, 6, true));
/*      */     
/* 3049 */     AdvancedCreationEntry tapestryM2 = createAdvancedEntry(10016, 926, 987, 992, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3052 */     tapestryM2.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3053 */     tapestryM2.addRequirement(new CreationRequirement(2, 925, 6, true));
/*      */     
/* 3055 */     AdvancedCreationEntry tapestryM3 = createAdvancedEntry(10016, 926, 987, 993, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3058 */     tapestryM3.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3059 */     tapestryM3.addRequirement(new CreationRequirement(2, 925, 6, true));
/*      */     
/* 3061 */     AdvancedCreationEntry tapestryFaeldray = createAdvancedEntry(10016, 926, 987, 994, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3064 */     tapestryFaeldray.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3065 */     tapestryFaeldray.addRequirement(new CreationRequirement(2, 925, 6, true));
/*      */ 
/*      */     
/* 3068 */     AdvancedCreationEntry swordDisplay = createAdvancedEntry(10044, 987, 21, 1030, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3071 */     swordDisplay.addRequirement(new CreationRequirement(1, 21, 1, true));
/* 3072 */     swordDisplay.addRequirement(new CreationRequirement(2, 86, 1, true));
/*      */     
/* 3074 */     AdvancedCreationEntry axeDisplay = createAdvancedEntry(10044, 987, 90, 1031, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3077 */     axeDisplay.addRequirement(new CreationRequirement(1, 90, 1, true));
/* 3078 */     axeDisplay.addRequirement(new CreationRequirement(2, 86, 1, true));
/*      */     
/* 3080 */     AdvancedCreationEntry marblePlanter = createAdvancedEntry(10074, 786, 786, 1001, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3083 */     marblePlanter.addRequirement(new CreationRequirement(1, 786, 4, true));
/*      */     
/* 3085 */     AdvancedCreationEntry bunchOfRopes = createAdvancedEntry(1014, 319, 319, 1029, false, false, 0.0F, true, false, CreationCategories.ROPES);
/*      */     
/* 3087 */     bunchOfRopes.addRequirement(new CreationRequirement(1, 319, 2, true));
/*      */     
/* 3089 */     createSimpleEntry(10016, 216, 926, 1071, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/* 3091 */     createSimpleEntry(10016, 215, 926, 1071, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/* 3093 */     createSimpleEntry(10016, 216, 213, 1107, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/* 3095 */     createSimpleEntry(10016, 215, 213, 1107, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/* 3097 */     createSimpleEntry(10016, 216, 213, 1106, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */     
/* 3099 */     createSimpleEntry(10016, 215, 213, 1106, false, true, 0.0F, false, false, CreationCategories.CLOTHES);
/*      */ 
/*      */     
/* 3102 */     createNewClothing(1067, 110, 128, new int[] { 47, 439, 102 });
/* 3103 */     createNewClothing(1068, 110, 128, new int[] { 46, 437, 102 });
/* 3104 */     createNewClothing(1069, 110, 128, new int[] { 439, 100, 102 });
/* 3105 */     createNewClothing(1070, 1107, 128, new int[] { 439, 47, 440 });
/* 3106 */     createNewClothing(1072, 1107, 128, new int[] { 46, 437 });
/* 3107 */     createNewClothing(1073, 1107, 128, new int[] { 47 });
/* 3108 */     createNewClothing(1074, 1106, 128, new int[] { 47, 439, 100 });
/* 3109 */     createNewClothing(1105, 1106, 128, new int[] { 46, 437, 100 });
/* 3110 */     createNewClothing(1075, 1106, 128, new int[] { 439, 100 });
/* 3111 */     createNewClothing(779, 1425, 128, new int[] { 440 });
/* 3112 */     createNewClothing(111, 1426, 128, new int[] { 439 });
/* 3113 */     createNewClothing(112, 1427, 128, new int[] { 439 });
/*      */ 
/*      */     
/* 3116 */     AdvancedCreationEntry beeSmoker = createAdvancedEntry(10015, 188, 46, 1243, false, false, 0.0F, true, false, 0, 30.0D, CreationCategories.TOOLS);
/*      */ 
/*      */ 
/*      */     
/* 3120 */     beeSmoker.addRequirement(new CreationRequirement(1, 188, 1, true));
/* 3121 */     beeSmoker.addRequirement(new CreationRequirement(2, 72, 1, true));
/*      */     
/* 3123 */     AdvancedCreationEntry hive = createAdvancedEntry(10044, 23, 22, 1175, false, false, 0.0F, true, true, 0, 25.0D, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 3126 */     hive.addRequirement(new CreationRequirement(1, 22, 10, true));
/* 3127 */     hive.addRequirement(new CreationRequirement(2, 23, 3, true));
/* 3128 */     hive.addRequirement(new CreationRequirement(3, 218, 1, true));
/*      */     
/* 3130 */     AdvancedCreationEntry waxkit = createAdvancedEntry(10091, 213, 1254, 1255, false, false, 0.0F, true, false, 0, 21.0D, CreationCategories.COOKING_UTENSILS);
/*      */ 
/*      */     
/* 3133 */     waxkit.addRequirement(new CreationRequirement(1, 1269, 1, true));
/* 3134 */     waxkit.addRequirement(new CreationRequirement(2, 214, 1, true));
/*      */     
/* 3136 */     AdvancedCreationEntry skullMug = createAdvancedEntry(1012, 390, 1250, 1253, false, true, 0.0F, false, false, 0, 25.0D, CreationCategories.COOKING_UTENSILS);
/*      */ 
/*      */     
/* 3139 */     skullMug.addRequirement(new CreationRequirement(1, 444, 2, true));
/* 3140 */     skullMug.addRequirement(new CreationRequirement(2, 1254, 2, true));
/* 3141 */     skullMug.setIsEpicBuildMissionTarget(false);
/*      */     
/* 3143 */     AdvancedCreationEntry messageBoard = createAdvancedEntry(1005, 23, 22, 1271, false, false, 0.0F, true, true, CreationCategories.SIGNS);
/*      */     
/* 3145 */     messageBoard.addRequirement(new CreationRequirement(1, 22, 5, true));
/* 3146 */     messageBoard.addRequirement(new CreationRequirement(2, 23, 1, true));
/* 3147 */     messageBoard.addRequirement(new CreationRequirement(3, 218, 3, true));
/*      */     
/* 3149 */     AdvancedCreationEntry still = createAdvancedEntry(10015, 772, 772, 1178, false, false, 0.0F, true, true, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 3152 */     still.addRequirement(new CreationRequirement(1, 772, 5, true));
/* 3153 */     still.addRequirement(new CreationRequirement(2, 131, 25, true));
/* 3154 */     still.addRequirement(new CreationRequirement(3, 22, 1, true));
/* 3155 */     still.addRequirement(new CreationRequirement(4, 188, 2, true));
/*      */     
/* 3157 */     AdvancedCreationEntry larder = createAdvancedEntry(10044, 22, 23, 1277, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */     
/* 3159 */     larder.addRequirement(new CreationRequirement(1, 22, 19, true));
/* 3160 */     larder.addRequirement(new CreationRequirement(2, 218, 3, true));
/* 3161 */     larder.addRequirement(new CreationRequirement(3, 49, 5, true));
/* 3162 */     larder.addRequirement(new CreationRequirement(4, 188, 2, true));
/* 3163 */     larder.addRequirement(new CreationRequirement(5, 23, 3, true));
/*      */     
/* 3165 */     createAdvancedEntry(10015, 1298, 1299, 1296, false, false, 0.0F, true, false, CreationCategories.CONTAINER)
/*      */       
/* 3167 */       .addRequirement(new CreationRequirement(1, 76, 1, true))
/* 3168 */       .addRequirement(new CreationRequirement(2, 131, 6, true))
/* 3169 */       .addRequirement(new CreationRequirement(3, 444, 1, true))
/* 3170 */       .addRequirement(new CreationRequirement(4, 100, 2, true))
/* 3171 */       .addRequirement(new CreationRequirement(5, 1298, 2, true));
/*      */     
/* 3173 */     AdvancedCreationEntry weaponsRackS = createAdvancedEntry(10044, 218, 22, 724, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 3175 */     weaponsRackS.addRequirement(new CreationRequirement(1, 561, 7, true));
/* 3176 */     weaponsRackS.addRequirement(new CreationRequirement(2, 22, 9, true));
/*      */     
/* 3178 */     AdvancedCreationEntry weaponsRackP = createAdvancedEntry(10044, 218, 22, 725, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 3180 */     weaponsRackP.addRequirement(new CreationRequirement(1, 561, 7, true));
/* 3181 */     weaponsRackP.addRequirement(new CreationRequirement(2, 22, 9, true));
/*      */     
/* 3183 */     AdvancedCreationEntry armourStand = createAdvancedEntry(10044, 218, 22, 759, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 3185 */     armourStand.addRequirement(new CreationRequirement(1, 561, 7, true));
/* 3186 */     armourStand.addRequirement(new CreationRequirement(2, 22, 12, true));
/* 3187 */     armourStand.addRequirement(new CreationRequirement(3, 23, 6, true));
/*      */     
/* 3189 */     AdvancedCreationEntry weaponsRackB = createAdvancedEntry(10044, 218, 22, 758, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 3191 */     weaponsRackB.addRequirement(new CreationRequirement(1, 561, 7, true));
/* 3192 */     weaponsRackB.addRequirement(new CreationRequirement(2, 22, 9, true));
/*      */     
/* 3194 */     AdvancedCreationEntry thatchingTool = createAdvancedEntry(10044, 217, 22, 774, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 3196 */     thatchingTool.addRequirement(new CreationRequirement(1, 217, 6, true));
/* 3197 */     thatchingTool.addRequirement(new CreationRequirement(2, 23, 1, true));
/*      */     
/* 3199 */     AdvancedCreationEntry wineBarrelRack = createAdvancedEntry(1005, 217, 860, 1108, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */ 
/*      */     
/* 3202 */     wineBarrelRack.addRequirement(new CreationRequirement(1, 860, 1, true));
/*      */     
/* 3204 */     AdvancedCreationEntry smallBarrelRack = createAdvancedEntry(1005, 217, 860, 1109, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */ 
/*      */     
/* 3207 */     smallBarrelRack.addRequirement(new CreationRequirement(1, 860, 1, true));
/*      */     
/* 3209 */     AdvancedCreationEntry planterRack = createAdvancedEntry(1005, 217, 860, 1110, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */ 
/*      */     
/* 3212 */     planterRack.addRequirement(new CreationRequirement(1, 22, 4, true));
/*      */     
/* 3214 */     AdvancedCreationEntry amphoraRack = createAdvancedEntry(1005, 217, 860, 1111, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */ 
/*      */     
/* 3217 */     amphoraRack.addRequirement(new CreationRequirement(1, 22, 4, true));
/*      */     
/* 3219 */     AdvancedCreationEntry emptyShelf = createAdvancedEntry(1005, 217, 860, 1412, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3222 */     emptyShelf.addRequirement(new CreationRequirement(1, 22, 4, true));
/*      */     
/* 3224 */     createSimpleEntry(10091, 394, 748, 1269, false, true, 0.0F, false, false, CreationCategories.WRITING);
/*      */ 
/*      */     
/* 3227 */     createSimpleEntry(10091, 394, 1272, 1269, false, true, 0.0F, false, false, CreationCategories.WRITING);
/*      */ 
/*      */ 
/*      */     
/* 3231 */     if (Features.Feature.HIGHWAYS.isEnabled()) {
/*      */       
/* 3233 */       createSimpleEntry(10031, 97, 146, 1113, false, true, 10.0F, false, false, CreationCategories.HIGHWAY);
/*      */       
/* 3235 */       createSimpleEntry(10031, 308, 1113, 1114, true, true, 0.0F, false, false, CreationCategories.HIGHWAY);
/*      */ 
/*      */       
/* 3238 */       AdvancedCreationEntry waystone = createAdvancedEntry(10031, 97, 146, 1112, false, false, 10.0F, false, false, 0, 21.0D, CreationCategories.HIGHWAY);
/*      */       
/* 3240 */       waystone.setDepleteFromTarget(5000);
/* 3241 */       waystone.addRequirement(new CreationRequirement(1, 23, 1, true));
/* 3242 */       waystone.addRequirement(new CreationRequirement(2, 480, 1, true));
/*      */     } 
/*      */     
/* 3245 */     createSimpleEntry(10091, 748, 214, 1409, true, true, 0.0F, false, false, CreationCategories.WRITING);
/*      */     
/* 3247 */     createSimpleEntry(10091, 1272, 214, 1409, true, true, 0.0F, false, false, CreationCategories.WRITING);
/*      */ 
/*      */     
/* 3250 */     AdvancedCreationEntry archJournal = createAdvancedEntry(10017, 1409, 100, 1404, false, false, 0.0F, true, false, CreationCategories.WRITING);
/*      */     
/* 3252 */     archJournal.addRequirement(new CreationRequirement(1, 100, 2, true));
/* 3253 */     AdvancedCreationEntry almanac = createAdvancedEntry(10017, 1409, 100, 1127, false, false, 0.0F, true, false, CreationCategories.WRITING);
/*      */     
/* 3255 */     almanac.addRequirement(new CreationRequirement(1, 100, 2, true));
/*      */     
/* 3257 */     if (Features.Feature.WAGONER.isEnabled()) {
/*      */       
/* 3259 */       AdvancedCreationEntry wagonerContainer = createAdvancedEntry(1005, 217, 860, 1309, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */       
/* 3261 */       wagonerContainer.addRequirement(new CreationRequirement(1, 860, 1, true));
/*      */     } 
/*      */     
/* 3264 */     AdvancedCreationEntry crateRack = createAdvancedEntry(1005, 217, 860, 1312, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */     
/* 3266 */     crateRack.addRequirement(new CreationRequirement(1, 860, 1, true));
/* 3267 */     crateRack.addRequirement(new CreationRequirement(2, 22, 6, true));
/*      */     
/* 3269 */     AdvancedCreationEntry bsbRack = createAdvancedEntry(1005, 217, 860, 1315, false, false, 0.0F, true, true, CreationCategories.CONTAINER);
/*      */     
/* 3271 */     bsbRack.addRequirement(new CreationRequirement(1, 860, 1, true));
/* 3272 */     bsbRack.addRequirement(new CreationRequirement(2, 22, 6, true));
/*      */     
/* 3274 */     AdvancedCreationEntry bcu = createAdvancedEntry(10044, 217, 860, 1316, false, false, 0.0F, true, true, 35, 50.0D, CreationCategories.CONTAINER);
/*      */     
/* 3276 */     bcu.addRequirement(new CreationRequirement(1, 860, 2, true));
/* 3277 */     bcu.addRequirement(new CreationRequirement(2, 22, 6, true));
/* 3278 */     bcu.addRequirement(new CreationRequirement(3, 662, 4, true));
/*      */ 
/*      */     
/* 3281 */     if (Features.Feature.TRANSPORTABLE_CREATURES.isEnabled()) {
/*      */       
/* 3283 */       AdvancedCreationEntry creatureCrate = createAdvancedEntry(1005, 22, 217, 1311, false, false, 0.0F, true, true, 0, 60.0D, CreationCategories.STORAGE);
/*      */       
/* 3285 */       creatureCrate.addRequirement(new CreationRequirement(1, 22, 24, true));
/* 3286 */       creatureCrate.addRequirement(new CreationRequirement(2, 681, 6, true));
/* 3287 */       creatureCrate.addRequirement(new CreationRequirement(3, 217, 16, true));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3292 */     AdvancedCreationEntry tapestryEvening = createAdvancedEntry(10016, 926, 987, 1318, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3295 */     tapestryEvening.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3296 */     tapestryEvening.addRequirement(new CreationRequirement(2, 925, 6, true));
/*      */     
/* 3298 */     AdvancedCreationEntry tapestryMclavin = createAdvancedEntry(10016, 926, 987, 1319, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3301 */     tapestryMclavin.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3302 */     tapestryMclavin.addRequirement(new CreationRequirement(2, 925, 6, true));
/*      */     
/* 3304 */     AdvancedCreationEntry tapestryEhizellbob = createAdvancedEntry(10016, 926, 987, 1320, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */ 
/*      */     
/* 3307 */     tapestryEhizellbob.addRequirement(new CreationRequirement(1, 926, 7, true));
/* 3308 */     tapestryEhizellbob.addRequirement(new CreationRequirement(2, 925, 6, true));
/*      */ 
/*      */     
/* 3311 */     createAdvancedMetalicEntry(10015, 64, 220, 1341, false, true, 0.0F, false, false, CreationCategories.TOOLS, new CreationRequirement[] { new CreationRequirement(1, 790, 3, true), new CreationRequirement(2, 100, 1, true) });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3316 */     AdvancedCreationEntry keepNet = createAdvancedEntry(1014, 188, 319, 1342, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */     
/* 3318 */     keepNet.addRequirement(new CreationRequirement(1, 188, 1, true));
/* 3319 */     keepNet.addRequirement(new CreationRequirement(2, 214, 2, true));
/* 3320 */     keepNet.addRequirement(new CreationRequirement(3, 23, 2, true));
/*      */     
/* 3322 */     AdvancedCreationEntry fishingNet = createAdvancedEntry(10016, 214, 214, 1343, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 3325 */     fishingNet.addRequirement(new CreationRequirement(1, 23, 1, true));
/* 3326 */     fishingNet.addRequirement(new CreationRequirement(2, 319, 1, true));
/*      */     
/* 3328 */     createSimpleEntry(1005, 8, 23, 1344, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 3331 */     createSimpleEntry(1005, 685, 23, 1344, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 3334 */     createMetallicEntries(10043, 64, 46, 1345, false, true, 0.0F, false, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 3337 */     AdvancedCreationEntry fishingRod = createAdvancedEntry(10044, 1344, 23, 1346, false, false, 0.0F, true, false, CreationCategories.TOOLS);
/*      */ 
/*      */     
/* 3340 */     fishingRod.addRequirement(new CreationRequirement(1, 1345, 3, true));
/*      */     
/* 3342 */     createSimpleEntry(10016, 14, 214, 1347, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3345 */     createSimpleEntry(10016, 922, 1347, 1348, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3348 */     createSimpleEntry(1014, 320, 1348, 1349, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3351 */     createSimpleEntry(10016, 922, 1349, 1350, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3354 */     createSimpleEntry(1014, 320, 1350, 1351, false, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3357 */     createSimpleEntry(1005, 8, 9, 1356, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3360 */     createSimpleEntry(1005, 685, 9, 1356, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3363 */     createMetallicEntries(10015, 64, 46, 1357, false, true, 1000.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3366 */     createSimpleEntry(1020, 8, 1250, 1358, false, true, 900.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3369 */     createSimpleEntry(10044, 8, 23, 1367, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3372 */     createMetallicEntries(10043, 64, 46, 1368, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3375 */     createSimpleEntry(10043, 64, 205, 1369, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3378 */     createSimpleEntry(10043, 64, 837, 1369, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3381 */     createSimpleEntry(10043, 64, 694, 1369, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3384 */     createSimpleEntry(10043, 64, 698, 1369, false, true, 10.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3387 */     createSimpleEntry(10015, 101, 444, 1370, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3390 */     createSimpleEntry(10016, 1370, 213, 1371, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3393 */     createSimpleEntry(10016, 1370, 926, 1371, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3396 */     createSimpleEntry(1005, 99, 1367, 1372, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3399 */     createSimpleEntry(10044, 101, 1367, 1373, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3402 */     createSimpleEntry(10015, 1370, 1368, 1374, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3405 */     createSimpleEntry(10043, 1371, 1369, 1375, true, true, 0.0F, false, false, CreationCategories.TOOL_PARTS);
/*      */ 
/*      */     
/* 3408 */     AdvancedCreationEntry rackRods = createAdvancedEntry(1005, 218, 22, 1393, false, false, 0.0F, true, true, CreationCategories.STORAGE);
/*      */     
/* 3410 */     rackRods.addRequirement(new CreationRequirement(1, 561, 7, true));
/* 3411 */     rackRods.addRequirement(new CreationRequirement(2, 22, 9, true));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3416 */     AdvancedCreationEntry bouy = createAdvancedEntry(1005, 189, 565, 1396, false, false, 0.0F, true, true, CreationCategories.DECORATION);
/*      */     
/* 3418 */     bouy.addRequirement(new CreationRequirement(1, 23, 1, true));
/* 3419 */     bouy.addRequirement(new CreationRequirement(2, 497, 1, true));
/*      */     
/* 3421 */     AdvancedCreationEntry pearlNecklace = createAdvancedEntry(10043, 214, 1397, 1399, false, false, 0.0F, true, false, CreationCategories.DECORATION);
/*      */     
/* 3423 */     pearlNecklace.addRequirement(new CreationRequirement(1, 1397, 9, true));
/* 3424 */     pearlNecklace.addRequirement(new CreationRequirement(2, 1398, 1, true));
/*      */     
/* 3426 */     if (Features.Feature.CHICKEN_COOPS.isEnabled()) {
/*      */       
/* 3428 */       AdvancedCreationEntry chickenCoop = createAdvancedEntry(10044, 22, 217, 1432, false, false, 0.0F, true, true, 0, 50.0D, CreationCategories.STORAGE);
/*      */ 
/*      */ 
/*      */       
/* 3432 */       chickenCoop.addRequirement(new CreationRequirement(1, 217, 4, true));
/* 3433 */       chickenCoop.addRequirement(new CreationRequirement(2, 22, 24, true));
/* 3434 */       chickenCoop.addRequirement(new CreationRequirement(3, 23, 8, true));
/*      */       
/* 3436 */       chickenCoop.addRequirement(new CreationRequirement(4, 189, 1, true));
/*      */     } 
/*      */     
/* 3439 */     logger.info("Initialising the CreationEntries took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " millis.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createNewClothing(int aArticle, int aSource, int aTarget, int... components) {
/* 3446 */     AdvancedCreationEntry article = createAdvancedEntry(10016, aSource, aTarget, aArticle, false, false, 0.0F, true, false, 0, 10.0D, CreationCategories.CLOTHES);
/*      */     
/* 3448 */     article.setColouringCreation(true);
/* 3449 */     article.setFinalMaterial((byte)17);
/* 3450 */     article.setUseTemplateWeight(true);
/* 3451 */     int x = 1;
/* 3452 */     for (int component : components)
/*      */     {
/* 3454 */       article.addRequirement(new CreationRequirement(x++, component, 1, true));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void createBoatEntries() {
/* 3460 */     if (!entriesCreated) {
/*      */       
/* 3462 */       entriesCreated = true;
/* 3463 */       AdvancedCreationEntry lRowBoat = createAdvancedEntry(10082, 560, 560, 490, false, false, 0.0F, true, true, CreationCategories.SHIPS);
/*      */       
/* 3465 */       lRowBoat.addRequirement(new CreationRequirement(1, 560, 1, true));
/* 3466 */       lRowBoat.addRequirement(new CreationRequirement(2, 553, 1, true));
/* 3467 */       lRowBoat.addRequirement(new CreationRequirement(3, 546, 50, true));
/* 3468 */       lRowBoat.addRequirement(new CreationRequirement(4, 551, 50, true));
/* 3469 */       lRowBoat.addRequirement(new CreationRequirement(5, 561, 50, true));
/* 3470 */       lRowBoat.addRequirement(new CreationRequirement(6, 558, 1, true));
/* 3471 */       lRowBoat.addRequirement(new CreationRequirement(7, 153, 10, true));
/* 3472 */       lRowBoat.addRequirement(new CreationRequirement(8, 556, 2, true));
/* 3473 */       lRowBoat.addRequirement(new CreationRequirement(9, 545, 4, true));
/* 3474 */       lRowBoat.setIsEpicBuildMissionTarget(false);
/*      */       
/* 3476 */       AdvancedCreationEntry lSailBoat = createAdvancedEntry(10082, 560, 560, 491, false, false, 0.0F, true, true, CreationCategories.SHIPS);
/*      */       
/* 3478 */       lSailBoat.addRequirement(new CreationRequirement(1, 560, 1, true));
/* 3479 */       lSailBoat.addRequirement(new CreationRequirement(2, 553, 1, true));
/* 3480 */       lSailBoat.addRequirement(new CreationRequirement(3, 546, 50, true));
/* 3481 */       lSailBoat.addRequirement(new CreationRequirement(4, 551, 50, true));
/* 3482 */       lSailBoat.addRequirement(new CreationRequirement(5, 561, 50, true));
/* 3483 */       lSailBoat.addRequirement(new CreationRequirement(6, 557, 2, true));
/* 3484 */       lSailBoat.addRequirement(new CreationRequirement(7, 558, 1, true));
/* 3485 */       lSailBoat.addRequirement(new CreationRequirement(8, 153, 10, true));
/* 3486 */       lSailBoat.addRequirement(new CreationRequirement(9, 556, 2, true));
/* 3487 */       lSailBoat.addRequirement(new CreationRequirement(10, 545, 4, true));
/* 3488 */       lSailBoat.addRequirement(new CreationRequirement(11, 563, 1, true));
/* 3489 */       lSailBoat.addRequirement(new CreationRequirement(12, 567, 4, true));
/* 3490 */       lSailBoat.setIsEpicBuildMissionTarget(false);
/*      */       
/* 3492 */       AdvancedCreationEntry lCorbita = createAdvancedEntry(10082, 560, 560, 541, false, false, 0.0F, true, true, CreationCategories.SHIPS);
/*      */       
/* 3494 */       lCorbita.addRequirement(new CreationRequirement(1, 560, 2, true));
/* 3495 */       lCorbita.addRequirement(new CreationRequirement(2, 553, 1, true));
/* 3496 */       lCorbita.addRequirement(new CreationRequirement(3, 546, 200, true));
/* 3497 */       lCorbita.addRequirement(new CreationRequirement(4, 551, 200, true));
/* 3498 */       lCorbita.addRequirement(new CreationRequirement(5, 561, 400, true));
/* 3499 */       lCorbita.addRequirement(new CreationRequirement(6, 564, 1, true));
/* 3500 */       lCorbita.addRequirement(new CreationRequirement(7, 557, 8, true));
/* 3501 */       lCorbita.addRequirement(new CreationRequirement(8, 558, 4, true));
/* 3502 */       lCorbita.addRequirement(new CreationRequirement(9, 544, 2, true));
/* 3503 */       lCorbita.addRequirement(new CreationRequirement(10, 566, 40, true));
/* 3504 */       lCorbita.addRequirement(new CreationRequirement(11, 153, 50, true));
/* 3505 */       lCorbita.addRequirement(new CreationRequirement(12, 556, 2, true));
/* 3506 */       lCorbita.addRequirement(new CreationRequirement(13, 567, 10, true));
/* 3507 */       lCorbita.addRequirement(new CreationRequirement(14, 584, 1, true));
/* 3508 */       lCorbita.setIsEpicBuildMissionTarget(false);
/*      */       
/* 3510 */       AdvancedCreationEntry lCog = createAdvancedEntry(10082, 560, 560, 540, false, false, 0.0F, true, true, CreationCategories.SHIPS);
/*      */       
/* 3512 */       lCog.addRequirement(new CreationRequirement(1, 560, 2, true));
/* 3513 */       lCog.addRequirement(new CreationRequirement(2, 553, 1, true));
/* 3514 */       lCog.addRequirement(new CreationRequirement(3, 546, 300, true));
/* 3515 */       lCog.addRequirement(new CreationRequirement(4, 551, 200, true));
/* 3516 */       lCog.addRequirement(new CreationRequirement(5, 561, 400, true));
/* 3517 */       lCog.addRequirement(new CreationRequirement(6, 585, 1, true));
/* 3518 */       lCog.addRequirement(new CreationRequirement(7, 557, 8, true));
/* 3519 */       lCog.addRequirement(new CreationRequirement(8, 558, 4, true));
/* 3520 */       lCog.addRequirement(new CreationRequirement(9, 544, 1, true));
/* 3521 */       lCog.addRequirement(new CreationRequirement(10, 566, 60, true));
/* 3522 */       lCog.addRequirement(new CreationRequirement(11, 153, 50, true));
/* 3523 */       lCog.addRequirement(new CreationRequirement(12, 556, 2, true));
/* 3524 */       lCog.addRequirement(new CreationRequirement(13, 567, 10, true));
/* 3525 */       lCog.setIsEpicBuildMissionTarget(false);
/*      */       
/* 3527 */       AdvancedCreationEntry lKnarr = createAdvancedEntry(10082, 560, 560, 542, false, false, 0.0F, true, true, CreationCategories.SHIPS);
/*      */       
/* 3529 */       lKnarr.addRequirement(new CreationRequirement(1, 560, 3, true));
/* 3530 */       lKnarr.addRequirement(new CreationRequirement(2, 553, 1, true));
/* 3531 */       lKnarr.addRequirement(new CreationRequirement(3, 546, 400, true));
/* 3532 */       lKnarr.addRequirement(new CreationRequirement(4, 551, 200, true));
/* 3533 */       lKnarr.addRequirement(new CreationRequirement(5, 561, 400, true));
/* 3534 */       lKnarr.addRequirement(new CreationRequirement(6, 131, 200, true));
/* 3535 */       lKnarr.addRequirement(new CreationRequirement(7, 586, 1, true));
/* 3536 */       lKnarr.addRequirement(new CreationRequirement(8, 557, 8, true));
/* 3537 */       lKnarr.addRequirement(new CreationRequirement(9, 558, 4, true));
/* 3538 */       lKnarr.addRequirement(new CreationRequirement(10, 544, 1, true));
/* 3539 */       lKnarr.addRequirement(new CreationRequirement(11, 566, 80, true));
/* 3540 */       lKnarr.addRequirement(new CreationRequirement(12, 153, 100, true));
/* 3541 */       lKnarr.addRequirement(new CreationRequirement(13, 556, 10, true));
/* 3542 */       lKnarr.addRequirement(new CreationRequirement(14, 567, 10, true));
/* 3543 */       lKnarr.setIsEpicBuildMissionTarget(false);
/*      */       
/* 3545 */       AdvancedCreationEntry lCaravel = createAdvancedEntry(10082, 560, 560, 543, false, false, 0.0F, true, true, CreationCategories.SHIPS);
/*      */       
/* 3547 */       lCaravel.addRequirement(new CreationRequirement(1, 560, 3, true));
/* 3548 */       lCaravel.addRequirement(new CreationRequirement(2, 553, 1, true));
/* 3549 */       lCaravel.addRequirement(new CreationRequirement(3, 546, 400, true));
/* 3550 */       lCaravel.addRequirement(new CreationRequirement(4, 551, 300, true));
/* 3551 */       lCaravel.addRequirement(new CreationRequirement(5, 561, 600, true));
/* 3552 */       lCaravel.addRequirement(new CreationRequirement(6, 563, 1, true));
/* 3553 */       lCaravel.addRequirement(new CreationRequirement(7, 557, 12, true));
/* 3554 */       lCaravel.addRequirement(new CreationRequirement(8, 558, 8, true));
/* 3555 */       lCaravel.addRequirement(new CreationRequirement(9, 544, 1, true));
/* 3556 */       lCaravel.addRequirement(new CreationRequirement(10, 566, 80, true));
/* 3557 */       lCaravel.addRequirement(new CreationRequirement(11, 548, 1, true));
/* 3558 */       lCaravel.addRequirement(new CreationRequirement(12, 153, 150, true));
/* 3559 */       lCaravel.addRequirement(new CreationRequirement(13, 556, 10, true));
/* 3560 */       lCaravel.addRequirement(new CreationRequirement(14, 567, 10, true));
/* 3561 */       lCaravel.addRequirement(new CreationRequirement(15, 587, 1, true));
/* 3562 */       lCaravel.addRequirement(new CreationRequirement(16, 564, 1, true));
/* 3563 */       lCaravel.addRequirement(new CreationRequirement(17, 584, 1, true));
/* 3564 */       lCaravel.setIsEpicBuildMissionTarget(false);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static CreationEntry createSimpleEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean depleteSource, boolean depleteTarget, float aPercentageLost, boolean depleteEqually, boolean aCreateOnGround, CreationCategories aCategory) {
/* 3586 */     CreationEntry entry = new SimpleCreationEntry(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCategory);
/*      */     
/* 3588 */     CreationMatrix.getInstance().addCreationEntry(entry);
/* 3589 */     return entry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CreationEntry createMetallicEntries(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean depleteSource, boolean depleteTarget, float aPercentageLost, boolean depleteEqually, boolean aCreateOnGround, CreationCategories aCategory) {
/* 3596 */     CreationEntry defaultEntry = createSimpleEntry(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCategory);
/*      */ 
/*      */     
/* 3599 */     if (ItemFactory.isMetalLump(aObjectTarget)) {
/*      */       
/* 3601 */       for (int targetTemplateId : ItemFactory.metalLumpList)
/*      */       {
/* 3603 */         if (targetTemplateId != aObjectTarget)
/*      */         {
/*      */           
/* 3606 */           createSimpleEntry(aPrimarySkill, aObjectSource, targetTemplateId, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCategory);
/*      */         }
/*      */       }
/*      */     
/* 3610 */     } else if (ItemFactory.isMetalLump(aObjectSource)) {
/*      */       
/* 3612 */       for (int sourceTemplateId : ItemFactory.metalLumpList) {
/*      */         
/* 3614 */         if (sourceTemplateId != aObjectSource)
/*      */         {
/*      */           
/* 3617 */           createSimpleEntry(aPrimarySkill, sourceTemplateId, aObjectTarget, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCategory);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3622 */     return defaultEntry;
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
/*      */   public static CreationEntry createSimpleEntry(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean depleteSource, boolean depleteTarget, float aPercentageLost, boolean depleteEqually, boolean aCreateOnGround, int aCustomCreationCutOff, double aMinimumSkill, CreationCategories aCategory) {
/* 3645 */     CreationEntry entry = new SimpleCreationEntry(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCustomCreationCutOff, aMinimumSkill, aCategory);
/*      */ 
/*      */     
/* 3648 */     CreationMatrix.getInstance().addCreationEntry(entry);
/* 3649 */     return entry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CreationEntry createMetallicEntries(int aPrimarySkill, int aObjectSource, int aObjectTarget, int aObjectCreated, boolean depleteSource, boolean depleteTarget, float aPercentageLost, boolean depleteEqually, boolean aCreateOnGround, int aCustomCreationCutOff, double aMinimumSkill, CreationCategories aCategory) {
/* 3657 */     CreationEntry defaultEntry = createSimpleEntry(aPrimarySkill, aObjectSource, aObjectTarget, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCustomCreationCutOff, aMinimumSkill, aCategory);
/*      */ 
/*      */     
/* 3660 */     if (ItemFactory.isMetalLump(aObjectTarget)) {
/*      */       
/* 3662 */       for (int targetTemplateId : ItemFactory.metalLumpList)
/*      */       {
/* 3664 */         if (targetTemplateId != aObjectTarget)
/*      */         {
/*      */           
/* 3667 */           createSimpleEntry(aPrimarySkill, aObjectSource, targetTemplateId, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCustomCreationCutOff, aMinimumSkill, aCategory);
/*      */         }
/*      */       }
/*      */     
/* 3671 */     } else if (ItemFactory.isMetalLump(aObjectSource)) {
/*      */       
/* 3673 */       for (int sourceTemplateId : ItemFactory.metalLumpList) {
/*      */         
/* 3675 */         if (sourceTemplateId != aObjectSource)
/*      */         {
/*      */           
/* 3678 */           createSimpleEntry(aPrimarySkill, sourceTemplateId, aObjectTarget, aObjectCreated, depleteSource, depleteTarget, aPercentageLost, depleteEqually, aCreateOnGround, aCustomCreationCutOff, aMinimumSkill, aCategory);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3683 */     return defaultEntry;
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
/*      */   public static AdvancedCreationEntry createAdvancedEntry(int primarySkill, int objectSource, int objectTarget, int objectCreated, boolean depleteSource, boolean depleteTarget, float percentageLost, boolean destroyBoth, boolean createOnGround, CreationCategories category) {
/* 3712 */     AdvancedCreationEntry entry = new AdvancedCreationEntry(primarySkill, objectSource, objectTarget, objectCreated, depleteSource, depleteTarget, percentageLost, destroyBoth, createOnGround, category);
/*      */     
/* 3714 */     CreationMatrix.getInstance().addCreationEntry(entry);
/* 3715 */     return entry;
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
/*      */   public static AdvancedCreationEntry createAdvancedEntry(int primarySkill, int objectSource, int objectTarget, int objectCreated, boolean destroyTarget, boolean useCapacity, float percentageLost, boolean destroyBoth, boolean createOnGround, int customCutOffChance, double aMinimumSkill, CreationCategories category) {
/* 3746 */     AdvancedCreationEntry entry = new AdvancedCreationEntry(primarySkill, objectSource, objectTarget, objectCreated, destroyTarget, useCapacity, percentageLost, destroyBoth, createOnGround, customCutOffChance, aMinimumSkill, category);
/*      */ 
/*      */     
/* 3749 */     CreationMatrix.getInstance().addCreationEntry(entry);
/* 3750 */     return entry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AdvancedCreationEntry createAdvancedMetalicEntry(int primarySkill, int objectSource, int objectTarget, int objectCreated, boolean depleteSource, boolean depleteTarget, float percentageLost, boolean destroyBoth, boolean createOnGround, CreationCategories category, CreationRequirement... extras) {
/* 3758 */     AdvancedCreationEntry defaultEntry = createAdvancedEntry(primarySkill, objectSource, objectTarget, objectCreated, depleteSource, depleteTarget, percentageLost, destroyBoth, createOnGround, category);
/*      */ 
/*      */     
/* 3761 */     for (CreationRequirement extra : extras)
/*      */     {
/* 3763 */       defaultEntry.addRequirement(extra);
/*      */     }
/*      */     
/* 3766 */     if (ItemFactory.isMetalLump(objectTarget)) {
/*      */       
/* 3768 */       for (int targetTemplateId : ItemFactory.metalLumpList) {
/*      */         
/* 3770 */         if (targetTemplateId != objectTarget) {
/*      */ 
/*      */           
/* 3773 */           AdvancedCreationEntry metalEntry = createAdvancedEntry(primarySkill, objectSource, targetTemplateId, objectCreated, depleteSource, depleteTarget, percentageLost, destroyBoth, createOnGround, category);
/*      */ 
/*      */           
/* 3776 */           for (CreationRequirement extra : extras)
/*      */           {
/* 3778 */             metalEntry.addRequirement(extra);
/*      */           }
/*      */         } 
/*      */       } 
/* 3782 */     } else if (ItemFactory.isMetalLump(objectSource)) {
/*      */       
/* 3784 */       for (int sourceTemplateId : ItemFactory.metalLumpList) {
/*      */         
/* 3786 */         if (sourceTemplateId != objectSource) {
/*      */ 
/*      */           
/* 3789 */           AdvancedCreationEntry metalEntry = createAdvancedEntry(primarySkill, sourceTemplateId, objectTarget, objectCreated, depleteSource, depleteTarget, percentageLost, destroyBoth, createOnGround, category);
/*      */ 
/*      */           
/* 3792 */           for (CreationRequirement extra : extras)
/*      */           {
/* 3794 */             metalEntry.addRequirement(extra);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 3799 */     return defaultEntry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AdvancedCreationEntry createAdvancedMetalicEntry(int primarySkill, int objectSource, int objectTarget, int objectCreated, boolean destroyTarget, boolean useCapacity, float percentageLost, boolean destroyBoth, boolean createOnGround, int customCutOffChance, double aMinimumSkill, CreationCategories category, CreationRequirement... extras) {
/* 3807 */     AdvancedCreationEntry defaultEntry = createAdvancedEntry(primarySkill, objectSource, objectTarget, objectCreated, destroyTarget, useCapacity, percentageLost, destroyBoth, createOnGround, customCutOffChance, aMinimumSkill, category);
/*      */ 
/*      */ 
/*      */     
/* 3811 */     for (CreationRequirement extra : extras)
/*      */     {
/* 3813 */       defaultEntry.addRequirement(extra);
/*      */     }
/*      */     
/* 3816 */     if (ItemFactory.isMetalLump(objectTarget)) {
/*      */       
/* 3818 */       for (int targetTemplateId : ItemFactory.metalLumpList) {
/*      */         
/* 3820 */         if (targetTemplateId != objectTarget) {
/*      */ 
/*      */           
/* 3823 */           AdvancedCreationEntry metalEntry = createAdvancedEntry(primarySkill, objectSource, objectTarget, objectCreated, destroyTarget, useCapacity, percentageLost, destroyBoth, createOnGround, customCutOffChance, aMinimumSkill, category);
/*      */ 
/*      */ 
/*      */           
/* 3827 */           for (CreationRequirement extra : extras)
/*      */           {
/* 3829 */             metalEntry.addRequirement(extra);
/*      */           }
/*      */         } 
/*      */       } 
/* 3833 */     } else if (ItemFactory.isMetalLump(objectSource)) {
/*      */       
/* 3835 */       for (int sourceTemplateId : ItemFactory.metalLumpList) {
/*      */         
/* 3837 */         if (sourceTemplateId != objectSource) {
/*      */ 
/*      */           
/* 3840 */           AdvancedCreationEntry metalEntry = createAdvancedEntry(primarySkill, objectSource, objectTarget, objectCreated, destroyTarget, useCapacity, percentageLost, destroyBoth, createOnGround, customCutOffChance, aMinimumSkill, category);
/*      */ 
/*      */ 
/*      */           
/* 3844 */           for (CreationRequirement extra : extras)
/*      */           {
/* 3846 */             metalEntry.addRequirement(extra); } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3850 */     return defaultEntry;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\CreationEntryCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
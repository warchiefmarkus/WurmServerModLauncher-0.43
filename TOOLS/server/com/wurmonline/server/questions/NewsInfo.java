/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
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
/*     */ public final class NewsInfo
/*     */   extends Question
/*     */ {
/*     */   public NewsInfo(Creature aResponder) {
/*  33 */     super(aResponder, "Latest cooking news", "Latest cooking changes", 14, -10L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  48 */     StringBuilder buf = new StringBuilder();
/*  49 */     buf.append(getBmlHeader());
/*  50 */     buf.append("label{text=\"This will contain the latest fixes, so long as i remember to update it.\"}");
/*  51 */     buf.append("label{text=\"\"}");
/*     */ 
/*     */     
/*  54 */     buf.append("label{type=\"bold\";text=\"07 Nov 2016\"}");
/*  55 */     buf.append("label{text=\" * Updated recipes so more can optionally have salt, tomato ketchup and mayo.\"}");
/*  56 */     buf.append("label{text=\"\"}");
/*     */     
/*  58 */     buf.append("label{type=\"bold\";text=\"03 Nov 2016\"}");
/*  59 */     buf.append("label{text=\" * Made it so only processed food items can go onto food shelves.\"}");
/*  60 */     buf.append("label{text=\" * Made it so only unprocessed food items can go into a fsb.\"}");
/*  61 */     buf.append("label{text=\" * Added two more shelves to (new) larders.\"}");
/*  62 */     buf.append("label{text=\" * Fix for recipes that dont use a container but do use a cooker.\"}");
/*  63 */     buf.append("label{text=\" * Fix for matching bread to incorrect recipe.\"}");
/*  64 */     buf.append("label{text=\" * Added client cookbook, to use it type ''toggle cookbook'' into the console.\"}");
/*  65 */     buf.append("label{text=\" * Fix for hand mirror usage.\"}");
/*  66 */     buf.append("label{text=\"\"}");
/*     */     
/*  68 */     buf.append("label{type=\"bold\";text=\"29 Oct 2016\"}");
/*  69 */     buf.append("label{text=\" * Fix for loosing recipes when you cross servers.\"}");
/*  70 */     buf.append("label{text=\" * Fix for filling containers.\"}");
/*  71 */     buf.append("label{text=\"\"}");
/*     */     
/*  73 */     buf.append("label{type=\"bold\";text=\"28 Oct 2016\"}");
/*  74 */     buf.append("label{text=\" * Fix so one or more items works correctly.\"}");
/*  75 */     buf.append("label{text=\" * Removed ability to put butter in fsb.\"}");
/*  76 */     buf.append("label{text=\" * Put picking herbs and spices under deed harvest fruit permission.\"}");
/*  77 */     buf.append("label{text=\" * fix for chocolate coated nut having wrong recipe name.\"}");
/*  78 */     buf.append("label{text=\" * Made it possible to unseal when liquid isnt fermenting.\"}");
/*  79 */     buf.append("label{text=\"\"}");
/*     */     
/*  81 */     buf.append("label{type=\"bold\";text=\"27 Oct 2016\"}");
/*  82 */     buf.append("label{text=\" * Fix for dagwood.\"}");
/*  83 */     buf.append("label{text=\" * Updated the messages when you learn a new recipe.\"}");
/*  84 */     buf.append("label{text=\" * Expanded the volume setting for measureing jug to include 1g, 2g and 5g.\"}");
/*  85 */     buf.append("label{text=\" * Changed model for pancakes, new ones will use the omlette model.\"}");
/*  86 */     buf.append("label{text=\" * Updated the messages for writing recipes.\"}");
/*  87 */     buf.append("label{text=\"\"}");
/*     */     
/*  89 */     buf.append("label{type=\"bold\";text=\"26 Oct 2016\"}");
/*  90 */     buf.append("label{text=\" * Added missing fermenting recipes.\"}");
/*  91 */     buf.append("label{text=\" * Fix for LORE allowing partial matches when it should not.\"}");
/*  92 */     buf.append("label{text=\" * Fixed the cream merging with whipped cream.\"}");
/*  93 */     buf.append("label{text=\" * Enabled fermenting of unfermented spirits.\"}");
/*  94 */     buf.append("label{text=\"\"}");
/*     */     
/*  96 */     buf.append("label{type=\"bold\";text=\"25 Oct 2016\"}");
/*  97 */     buf.append("label{text=\" * Added message when you learn a new recipe.\"}");
/*  98 */     buf.append("label{text=\" * Fix for zombiefied spirits.\"}");
/*  99 */     buf.append("label{text=\" * Fix for LORE message when too many of an item.\"}");
/* 100 */     buf.append("label{text=\" * Made the number of slices you get from bread and cake depend on the weight of the item.\"}");
/* 101 */     buf.append("label{text=\" * Fix to remove fillet option when food knife is acive for meats and fish.\"}");
/* 102 */     buf.append("label{text=\" * Fix so LORE tells you the required material (if its specified in recipe).\"}");
/* 103 */     buf.append("label{text=\" * Fix so you cant attempt to add a recipe to cookbook when its not in inventory (part 2).\"}");
/* 104 */     buf.append("label{text=\"\"}");
/*     */     
/* 106 */     buf.append("label{type=\"bold\";text=\"24 Oct 2016\"}");
/* 107 */     buf.append("label{text=\" * Added fermenting phase to spirits.\"}");
/* 108 */     buf.append("label{text=\" * Added difficulty to LORE output.\"}");
/* 109 */     buf.append("label{text=\" * Fix so you cant attempt to add a recipe to cookbook when its not in inventory.\"}");
/* 110 */     buf.append("label{text=\" * Added old style sandwiches back in (now called endurance sandwiches).\"}");
/* 111 */     buf.append("label{text=\"   They are made by using bread on cheese (or jam, egg, syrup or honey). \"}");
/* 112 */     buf.append("label{text=\"   They can be eaten even when hungry for the stamina regeneration, but \"}");
/* 113 */     buf.append("label{text=\"   will not give any CCFP or a timed affinity.\"}");
/* 114 */     buf.append("label{text=\" * Added new command to toggle visibility of ccfp bar (/toggleccfp).\"}");
/* 115 */     buf.append("label{text=\" * Made tar combinable.\"}");
/* 116 */     buf.append("label{text=\"\"}");
/*     */     
/* 118 */     buf.append("label{type=\"bold\";text=\"23 Oct 2016\"}");
/* 119 */     buf.append("label{text=\" * Fix so can plant fresh spices.\"}");
/* 120 */     buf.append("label{text=\" * Fixed recipes that used stoneware.\"}");
/* 121 */     buf.append("label{text=\" * Fix for 'you cant breed egglayers' with animals that you should be able to.\"}");
/* 122 */     buf.append("label{text=\"\"}");
/*     */     
/* 124 */     buf.append("label{type=\"bold\";text=\"22 Oct 2016\"}");
/* 125 */     buf.append("label{text=\" * Typo fix in fudge sauce recipe name.\"}");
/* 126 */     buf.append("label{text=\" * Made beersteins easier to make.\"}");
/* 127 */     buf.append("label{text=\" * Fix for double spacing in lore statement.\"}");
/* 128 */     buf.append("label{text=\" * Fix so sweets can go into larder.\"}");
/* 129 */     buf.append("label{text=\" * Fix so can plant items (again).\"}");
/* 130 */     buf.append("label{text=\" * Fix so beesmoker when made does not end up on floor.\"}");
/* 131 */     buf.append("label{text=\" * Recipe typo fixes.\"}");
/* 132 */     buf.append("label{text=\" * Fix for examine message on chopped veg (etc).\"}");
/* 133 */     buf.append("label{text=\" * Fix for message given when you write a recipe.\"}");
/* 134 */     buf.append("label{text=\" * Fix for out of bounds error when using back button in cookbook.\"}");
/* 135 */     buf.append("label{text=\" * Renamed stoneware to baking stone.\"}");
/* 136 */     buf.append("label{text=\"\"}");
/*     */     
/* 138 */     buf.append("label{type=\"bold\";text=\"21 Oct 2016\"}");
/* 139 */     buf.append("label{text=\" * Fix so cooked meat in fsb uses cooked meat icon.\"}");
/* 140 */     buf.append("label{text=\"\"}");
/*     */     
/* 142 */     buf.append("label{type=\"bold\";text=\"18 Oct 2016\"}");
/* 143 */     buf.append("label{text=\" * Enabled pottery planters to be planted.\"}");
/* 144 */     buf.append("label{text=\"\"}");
/*     */     
/* 146 */     buf.append("label{type=\"bold\";text=\"17 Oct 2016\"}");
/* 147 */     buf.append("label{text=\" * Fix so can plant 4 trellis on a tile.\"}");
/* 148 */     buf.append("label{text=\" * Fixed recipe transfering when player does.\"}");
/* 149 */     buf.append("label{text=\" * Enabled trellis to be planted against walls and fences (as well s tile borders).\"}");
/* 150 */     buf.append("label{text=\"\"}");
/*     */     
/* 152 */     buf.append(createAnswerButton2());
/* 153 */     getResponder().getCommunicator().sendBml(500, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInfo() {
/* 163 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\NewsInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
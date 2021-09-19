/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.WurmHarvestables;
/*     */ import com.wurmonline.server.behaviours.Methods;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ShowHarvestableInfo
/*     */   extends Question
/*     */ {
/*     */   private Item paper;
/*     */   private Item almanac;
/*     */   private WurmHarvestables.Harvestable harvestable;
/*  46 */   private int filter = 0;
/*     */   private static final String red = "color=\"255,127,127\"";
/*     */   private static final String green = "color=\"127,255,127\"";
/*     */   private static final String orange = "color=\"255,177,40\";";
/*  50 */   private static final DecimalFormat df = new DecimalFormat("#.##");
/*     */   
/*     */   private static final int ALL = 0;
/*     */   
/*     */   private static final int HARVESTABLE = 1;
/*     */   
/*     */   private static final int ALMOST_RIPE = 2;
/*     */   
/*     */   private static final int NEARLY_HARVESTABLE = 3;
/*     */ 
/*     */   
/*     */   public ShowHarvestableInfo(Creature aResponder, Item paper, WurmHarvestables.Harvestable harvestable) {
/*  62 */     super(aResponder, getReportName(paper, harvestable), getReportName(paper, harvestable), 141, -10L);
/*  63 */     this.almanac = null;
/*  64 */     this.paper = paper;
/*  65 */     this.harvestable = harvestable;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getReportName(Item paper, WurmHarvestables.Harvestable harvestable) {
/*  70 */     return LoginHandler.raiseFirstLetter(harvestable.getName() + " report (" + df
/*  71 */         .format(paper.getCurrentQualityLevel()) + ")");
/*     */   }
/*     */ 
/*     */   
/*     */   public ShowHarvestableInfo(Creature aResponder, Item almanac) {
/*  76 */     this(aResponder, almanac, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShowHarvestableInfo(Creature aResponder, Item almanac, int summarytype) {
/*  81 */     super(aResponder, getSummaryName(almanac, summarytype), getSummaryName(almanac, summarytype), 141, -10L);
/*  82 */     this.almanac = almanac;
/*  83 */     this.paper = null;
/*  84 */     this.harvestable = null;
/*  85 */     this.filter = summarytype;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getSummaryName(Item almanac, int summarytype) {
/*     */     String summaryType;
/*  92 */     switch (summarytype) {
/*     */       
/*     */       case 1:
/*  95 */         summaryType = " Harvestable";
/*     */         break;
/*     */       case 2:
/*  98 */         summaryType = " Almost Ripe";
/*     */         break;
/*     */       case 3:
/* 101 */         summaryType = " (Nearly) Harvestable";
/*     */         break;
/*     */       default:
/* 104 */         summaryType = ""; break;
/*     */     } 
/* 106 */     String almanacType = (almanac.getTemplateId() == 1127) ? "Almanac" : almanac.getName();
/* 107 */     return almanacType + summaryType + " Summary";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 113 */     setAnswer(answers);
/* 114 */     if (this.type == 0) {
/*     */       
/* 116 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/* 119 */     if (this.type == 141 && this.almanac != null) {
/*     */ 
/*     */       
/* 122 */       boolean show = getBooleanProp("show");
/* 123 */       String sbyid = getAnswer().getProperty("by");
/* 124 */       int byid = Integer.parseInt(sbyid);
/* 125 */       if (show) {
/*     */         
/* 127 */         ShowHarvestableInfo shi = new ShowHarvestableInfo(getResponder(), this.almanac, byid);
/* 128 */         shi.sendQuestion();
/*     */       } 
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
/*     */   public void sendQuestion() {
/* 141 */     StringBuilder buf = new StringBuilder();
/* 142 */     String dropdown = "";
/* 143 */     if (this.almanac != null) {
/* 144 */       dropdown = "button{text=\"Show\";id=\"show\"};label{text=\" \"};dropdown{id=\"by\";default=\"" + this.filter + "\";options=\"All,Harvestables,Almost Ripe,(Nearly) Harvestable\"};";
/*     */     }
/*     */ 
/*     */     
/* 148 */     buf.append("border{border{size=\"20,20\";null;null;label{type='bold';text=\"" + this.question + "\"};harray{" + dropdown + "label{text=\" \"};button{text=\"Close\";id=\"close\"};label{text=\" \"}};null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
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
/* 160 */         getId() + "\"}");
/* 161 */     if (this.harvestable != null) {
/*     */       
/* 163 */       switch (this.harvestable.getHarvestableId()) {
/*     */         
/*     */         case 1:
/* 166 */           buf.append(showOliveInfo());
/*     */           break;
/*     */         case 2:
/* 169 */           buf.append(showGrapeInfo());
/*     */           break;
/*     */         case 3:
/* 172 */           buf.append(showCherryInfo());
/*     */           break;
/*     */         case 4:
/* 175 */           buf.append(showAppleInfo());
/*     */           break;
/*     */         case 5:
/* 178 */           buf.append(showLemonInfo());
/*     */           break;
/*     */         case 6:
/* 181 */           buf.append(showOleanderInfo());
/*     */           break;
/*     */         case 7:
/* 184 */           buf.append(showCamelliaInfo());
/*     */           break;
/*     */         case 8:
/* 187 */           buf.append(showLavenderInfo());
/*     */           break;
/*     */         case 9:
/* 190 */           buf.append(showMapleInfo());
/*     */           break;
/*     */         case 10:
/* 193 */           buf.append(showRoseInfo());
/*     */           break;
/*     */         case 11:
/* 196 */           buf.append(showChestnutInfo());
/*     */           break;
/*     */         case 12:
/* 199 */           buf.append(showWalnutInfo());
/*     */           break;
/*     */         case 13:
/* 202 */           buf.append(showPineInfo());
/*     */           break;
/*     */         case 14:
/* 205 */           buf.append(showHazelInfo());
/*     */           break;
/*     */         case 15:
/* 208 */           buf.append(showHopsInfo());
/*     */           break;
/*     */         case 16:
/* 211 */           buf.append(showOakInfo());
/*     */           break;
/*     */         case 17:
/* 214 */           buf.append(showOrangeInfo());
/*     */           break;
/*     */         case 18:
/* 217 */           buf.append(showRaspberryInfo());
/*     */           break;
/*     */         case 19:
/* 220 */           buf.append(showBlueberryInfo());
/*     */           break;
/*     */         case 20:
/* 223 */           buf.append(showLingonberryInfo());
/*     */           break;
/*     */       } 
/* 226 */       buf.append(showHarvestableTimes());
/*     */     }
/* 228 */     else if (this.almanac != null) {
/*     */       
/* 230 */       buf.append(showSummary());
/*     */     } 
/* 232 */     buf.append("label{text=\"\"}");
/*     */     
/* 234 */     buf.append("}};null;null;}");
/* 235 */     getResponder().getCommunicator().sendBml(500, 450, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String showSummary() {
/* 240 */     StringBuilder buf = new StringBuilder();
/* 241 */     buf.append("label{text=\"\"}");
/* 242 */     Item[] reports = Methods.getBestReports(getResponder(), this.almanac);
/* 243 */     if (reports.length == 0) {
/* 244 */       buf.append("label{text=\"No Reports found!\"}");
/*     */     } else {
/*     */       
/* 247 */       if (this.almanac.getOwnerId() == getResponder().getWurmId() && reports.length == 20)
/* 248 */         getResponder().achievement(575); 
/* 249 */       boolean showedSome = false;
/*     */       
/* 251 */       buf.append("table{rows=\"1\";cols=\"5\";text{type=\"bold\";text=\"Report\"};label{type=\"bold\";text=\"QL\"};label{type=\"bold\";text=\"State\"};label{type=\"bold\";text=\"Harvest Start\"};label{type=\"bold\";text=\"Harvest End\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 259 */       for (Item report : reports) {
/*     */         
/* 261 */         WurmHarvestables.Harvestable harvestable = report.getHarvestable();
/* 262 */         if (harvestable != null) {
/*     */           
/* 264 */           boolean output = false;
/*     */           
/* 266 */           long howGood = (long)(report.getCurrentQualityLevel() * 86400.0F);
/* 267 */           String harvestStart = "";
/* 268 */           String harvestEnd = "";
/* 269 */           String colourStart = "";
/* 270 */           String colourState = "";
/* 271 */           if (harvestable.isHarvestable() && showSummaryHarvestables()) {
/*     */ 
/*     */             
/* 274 */             if (knowHarvestStart(harvestable, howGood)) {
/*     */               
/* 276 */               harvestStart = WurmCalendar.getDaysFrom(harvestable.getSeasonStart());
/* 277 */               harvestEnd = WurmCalendar.getDaysFrom(harvestable.getSeasonEnd());
/*     */ 
/*     */             
/*     */             }
/* 281 */             else if (knowHarvestEnd(harvestable, howGood)) {
/* 282 */               harvestEnd = WurmCalendar.getDaysFrom(harvestable.getSeasonEnd());
/* 283 */             }  colourStart = "color=\"127,255,127\"";
/* 284 */             colourState = "color=\"127,255,127\"";
/* 285 */             output = true;
/*     */           }
/* 287 */           else if (harvestable.isAlmostRipe() && showSummaryAlmostRipe()) {
/*     */ 
/*     */             
/* 290 */             if (knowHarvestStart(harvestable, howGood)) {
/* 291 */               harvestStart = WurmCalendar.getDaysFrom(harvestable.getSeasonStart());
/*     */             } else {
/*     */               
/* 294 */               harvestStart = WurmCalendar.getDaysFrom(harvestable.getDefaultSeasonStart());
/* 295 */               colourStart = "color=\"255,177,40\";";
/*     */             } 
/*     */             
/* 298 */             if (knowHarvestEnd(harvestable, howGood)) {
/* 299 */               harvestEnd = WurmCalendar.getDaysFrom(harvestable.getSeasonEnd());
/*     */             } else {
/*     */               
/* 302 */               harvestEnd = WurmCalendar.getDaysFrom(harvestable.getDefaultSeasonEnd());
/* 303 */               colourStart = "color=\"255,177,40\";";
/*     */             } 
/* 305 */             output = true;
/*     */           }
/* 307 */           else if (showSummaryAll()) {
/*     */ 
/*     */             
/* 310 */             if (knowHarvestStart(harvestable, howGood)) {
/* 311 */               harvestStart = WurmCalendar.getDaysFrom(harvestable.getSeasonStart());
/*     */             } else {
/*     */               
/* 314 */               harvestStart = WurmCalendar.getDaysFrom(harvestable.getDefaultSeasonStart());
/* 315 */               colourStart = "color=\"255,177,40\";";
/*     */             } 
/* 317 */             output = true;
/*     */           } 
/* 319 */           if (output)
/*     */           {
/* 321 */             buf.append("label{" + colourState + "text=\"" + harvestable.getName() + "\"};label{" + colourState + "text=\"" + df
/* 322 */                 .format(report.getCurrentQualityLevel()) + "\"};label{" + colourState + "text=\"" + harvestable
/* 323 */                 .getState() + "\"};label{" + colourStart + "text=\"" + harvestStart + "\"};label{" + colourState + "text=\"" + harvestEnd + "\"};");
/*     */ 
/*     */ 
/*     */             
/* 327 */             showedSome = true;
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 333 */           buf.append("label{color=\"255,127,127\"text=\"Invalid\"};label{color=\"255,127,127\"text=\"\"};label{color=\"255,127,127\"text=\"Unknown\"};label{color=\"255,127,127\"text=\"Unknown\"};label{color=\"255,127,127\"text=\"Unknown\"};");
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 341 */       buf.append("}");
/* 342 */       if (!showedSome)
/*     */       {
/* 344 */         buf.append("label{text=\"Nothing to show!\"}");
/*     */       }
/* 346 */       buf.append("label{text=\"\"}");
/* 347 */       buf.append("label{type=\"bold\";text=\"Notes:\"}");
/* 348 */       buf.append("label{color=\"127,255,127\"text=\"Green text is for currently harvestable.\"}");
/* 349 */       buf.append("label{color=\"255,177,40\";text=\"Times in orange are the default ones.\"}");
/* 350 */       buf.append("label{text=\"White text is from the best report.\"}");
/*     */     } 
/* 352 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean showSummaryAll() {
/* 357 */     return (this.filter == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean showSummaryHarvestables() {
/* 362 */     return (this.filter == 0 || this.filter == 1 || this.filter == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean showSummaryAlmostRipe() {
/* 367 */     return (this.filter == 0 || this.filter == 2 || this.filter == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   private String showHarvestableTimes() {
/* 372 */     StringBuilder buf = new StringBuilder();
/* 373 */     buf.append("label{text=\"\"}");
/* 374 */     buf.append("label{type=\"bold\";text=\"Harvestable Times\"}");
/*     */     
/* 376 */     long howGood = (long)(this.paper.getCurrentQualityLevel() * 86400.0F);
/* 377 */     if (this.harvestable.isHarvestable()) {
/*     */ 
/*     */       
/* 380 */       buf.append("text{text=\"It is currently harvestable.\"}");
/*     */       
/* 382 */       if (knowHarvestStart(this.harvestable, howGood)) {
/* 383 */         buf.append("text{text=\"The harvest season started " + 
/* 384 */             WurmCalendar.getDaysFrom(this.harvestable.getSeasonStart()) + "\"}");
/*     */       }
/* 386 */       if (knowHarvestEnd(this.harvestable, howGood)) {
/* 387 */         buf.append("text{text=\"The harvest season should end in " + 
/* 388 */             WurmCalendar.getDaysFrom(this.harvestable.getSeasonEnd()) + "\"}");
/*     */       }
/* 390 */     } else if (this.harvestable.isAlmostRipe()) {
/*     */ 
/*     */       
/* 393 */       if (knowHarvestStart(this.harvestable, howGood)) {
/* 394 */         buf.append("text{text=\"The harvest season will start in " + 
/* 395 */             WurmCalendar.getDaysFrom(this.harvestable.getSeasonStart()) + "\"}");
/*     */       } else {
/* 397 */         buf.append("text{text=\"The default harvest season is in " + 
/* 398 */             WurmCalendar.getDaysFrom(this.harvestable.getDefaultSeasonStart()) + "\"}");
/*     */       } 
/* 400 */       if (knowHarvestEnd(this.harvestable, howGood)) {
/* 401 */         buf.append("text{text=\"The harvest season should end in " + 
/* 402 */             WurmCalendar.getDaysFrom(this.harvestable.getSeasonEnd()) + "\"}");
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 407 */     else if (knowHarvestStart(this.harvestable, howGood)) {
/* 408 */       buf.append("text{text=\"The harvest season will start in " + 
/* 409 */           WurmCalendar.getDaysFrom(this.harvestable.getSeasonStart()) + "\"}");
/*     */     } else {
/* 411 */       buf.append("text{text=\"The default harvest season is in " + 
/* 412 */           WurmCalendar.getDaysFrom(this.harvestable.getDefaultSeasonStart()) + " This is only approximate as it can be plus or minus 2 weeks from that date. \"}");
/*     */     } 
/*     */ 
/*     */     
/* 416 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean knowHarvestEnd(WurmHarvestables.Harvestable harvestable, long howGood) {
/* 421 */     return (harvestable.getSeasonEnd() - howGood < WurmCalendar.getCurrentTime());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean knowHarvestStart(WurmHarvestables.Harvestable harvestable, long howGood) {
/* 426 */     if (harvestable.isHarvestable())
/*     */     {
/* 428 */       return (harvestable.getSeasonStart() + howGood > WurmCalendar.getCurrentTime()); } 
/* 429 */     return (harvestable.getSeasonStart() - howGood < WurmCalendar.getCurrentTime());
/*     */   }
/*     */ 
/*     */   
/*     */   private String showOliveInfo() {
/* 434 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 436 */     buf.append("text{text=\"\"}");
/* 437 */     buf.append("image{src=\"img.almanac.tree.olive\";size=\"128,128\";text=\"Olive tree\"}");
/* 438 */     buf.append("text{text=\"\"}");
/* 439 */     buf.append("text{text=\"The olive tree, known by the botanical name Olea wurmea, meaning ''Wurm olive'', is a small tree with a wide, gnarled trunk.  It is found and cultivated in many places and considered naturalized in all the lands of Wurm.  It is part of the Oleaceae family, and represents the earliest source of oil in Wurm history.\"}");
/*     */ 
/*     */ 
/*     */     
/* 443 */     buf.append("text{text=\"\"}");
/* 444 */     buf.append("image{src=\"img.almanac.produce.olive\";size=\"128,128\";text=\"Olive fruit\"}");
/* 445 */     buf.append("text{text=\"\"}");
/* 446 */     buf.append("text{text=\"The fruit of the olive tree, also called the olive, is of major agricultural importance in wurm as the source of olive oil. The tree and its fruit give their name to the plant family. The word ''oil'' in multiple languages ultimately derives from the name of this tree and its fruit, signifying its importance in many economies.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showGrapeInfo() {
/* 456 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 458 */     buf.append("text{text=\"\"}");
/* 459 */     buf.append("image{src=\"img.almanac.bush.grape\";size=\"128,128\";text=\"Grape bush\"}");
/* 460 */     buf.append("text{text=\"\"}");
/* 461 */     buf.append("text{text=\"The grape bush is a deciduous, woody vine, of the flowering plant genus Vitis.  Reaching a height of about 1.5 meters when fully grown, it can cover large areas if allowed to grow wild.  Vitis vinifera is found throughout the lands of Wurm, but it has evolved into warmer and cooler climate sub-varieties.\"}");
/*     */ 
/*     */ 
/*     */     
/* 465 */     buf.append("text{text=\"\"}");
/* 466 */     buf.append("image{src=\"img.almanac.trellis.grape\";size=\"128,128\";text=\"Grape trellis\"}");
/* 467 */     buf.append("text{text=\"\"}");
/* 468 */     buf.append("text{text=\"In recent years, thanks to advances in horticultural and gardening techniques, grapes have become available to be grown on trellises.  While purists bemoan the loss of rustic charm of fields of grape vines, farmers everywhere insist that the flavour of trellis-grown grapes is every bit as good as 'natural' ones.\"}");
/*     */ 
/*     */ 
/*     */     
/* 472 */     buf.append("text{text=\"\"}");
/* 473 */     buf.append("image{src=\"img.almanac.produce.grape\";size=\"128,128\";text=\"Green and Blue grapes\"}");
/* 474 */     buf.append("text{text=\"\"}");
/* 475 */     buf.append("text{text=\"Grapes can be eaten fresh as table grapes or they can be used for making wine, jam, juice, jelly, or various other dishes. Grapes are a non-climacteric type of fruit, generally occurring in clusters.\"}");
/*     */ 
/*     */     
/* 478 */     buf.append("label{text=\" \"}");
/* 479 */     buf.append("text{text=\"Green grapes, preferring a cooler climate, only grow in the North; warmer climate blue grapes are found in Southern regions.\"}");
/*     */ 
/*     */     
/* 482 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showCherryInfo() {
/* 487 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 489 */     buf.append("text{text=\"\"}");
/* 490 */     buf.append("image{src=\"img.almanac.tree.cherry\";size=\"128,128\";text=\"Cherry tree\"}");
/* 491 */     buf.append("text{text=\"\"}");
/* 492 */     buf.append("text{text=\"The Wurmian cherry, or Prunus avia wurmosa, is found in most areas.  Known for its distinctive flowers, which occur in small corymbs of several together, the fruit is smooth skinned, with a weak groove (or no groove) along one side.\"}");
/*     */ 
/*     */     
/* 495 */     buf.append("text{text=\"\"}");
/* 496 */     buf.append("image{src=\"img.almanac.produce.cherry\";size=\"128,128\";text=\"Cherries\"}");
/* 497 */     buf.append("text{text=\"\"}");
/* 498 */     buf.append("text{text=\"Cherry fruit is a fleshy drupe (stone fruit), growing in clusters of 2 or 3.  Wurmians have been known to use cherries for a variety of cooked dishes, as well as several drinks (both alcoholic and non-alcoholic).  It is also of great economic value to alchemists, as the juice may be used for tile transformation.\"}");
/*     */ 
/*     */ 
/*     */     
/* 502 */     buf.append("label{text=\" \"}");
/* 503 */     buf.append("text{text=\"Due to the small size of Prunus trees, cherry wood is time-consuming to harvest in large quantities, and requires advanced carpentry techniques to combine the inevitable small harvested logs into usable lumber.\"}");
/*     */ 
/*     */ 
/*     */     
/* 507 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showAppleInfo() {
/* 512 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 514 */     buf.append("text{text=\"\"}");
/* 515 */     buf.append("image{src=\"img.almanac.tree.apple\";size=\"128,128\";text=\"Apple tree\"}");
/* 516 */     buf.append("text{text=\"\"}");
/* 517 */     buf.append("text{text=\"The apple tree (Malus pumila, commonly and erroneously called Malus domestica) is a deciduous tree best known for its sweet, pomaceous fruit, the apple. It is cultivated Wurmwide as a fruit tree, and is the most widely grown species in the genus Malus.  It is related to the common rose, and is one of the oldest known fruit trees in the world. \"}");
/*     */ 
/*     */ 
/*     */     
/* 521 */     buf.append("text{text=\"\"}");
/* 522 */     buf.append("image{src=\"img.almanac.produce.apple\";size=\"128,128\";text=\"Green Apple\"}");
/* 523 */     buf.append("text{text=\"\"}");
/* 524 */     buf.append("text{text=\"Malus domestica was first discovered by an adventurer called Bramley, and is thus commonly known as the Bramley apple, or simply Bramleys.  It is usually eaten cooked due to its sourness, although some epicureans have been known to eat this apple raw in order to cleanse the palate.  It is more usually used for cooking, appearing in a variety of sauces, pies and other dishes.  A peculiarity of the variety is that when cooked, it becomes fluffy and golden, taking on a much lighter flavour.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 530 */     buf.append("label{text=\" \"}");
/* 531 */     buf.append("text{text=\"Apples may be pressed to produce a cloudy and much-prized juice, which is rumoured to have uses in alchemy.\"}");
/*     */ 
/*     */     
/* 534 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showLemonInfo() {
/* 539 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 541 */     buf.append("text{text=\"\"}");
/* 542 */     buf.append("image{src=\"img.almanac.tree.lemon\";size=\"128,128\";text=\"Lemon tree\"}");
/* 543 */     buf.append("text{text=\"\"}");
/* 544 */     buf.append("text{text=\"The lemon, or Citrus limon, is a species of small evergreen tree in the flowering plant family Rutaceae.  It is native to Wurm, and is found in all areas of the world.  Originally used for ornamental or medicinal purposes, lemons are now produced in large-scale cultivation.\"}");
/*     */ 
/*     */     
/* 547 */     buf.append("text{text=\"\"}");
/* 548 */     buf.append("image{src=\"img.almanac.produce.lemon\";size=\"128,128\";text=\"Lemon\"}");
/* 549 */     buf.append("text{text=\"\"}");
/* 550 */     buf.append("text{text=\"The tree's ellipsoidal yellow fruit is used for culinary and non-culinary purposes throughout Wurm.  The juice, pulp and rind (zest) are all used in cooking and baking, and the juice also has useful alchemical properties.  The juice of the lemon is about 5% to 6% citric acid, which gives a distinctive sour taste; this makes it a key ingredient in drinks and foods such as lemonade.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 556 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showOleanderInfo() {
/* 561 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 563 */     buf.append("text{text=\"\"}");
/* 564 */     buf.append("image{src=\"img.almanac.bush.oleander\";size=\"128,128\";text=\"Oleander bush\"}");
/* 565 */     buf.append("text{text=\"\"}");
/* 566 */     buf.append("text{text=\"Nerium oleander is a shrub in the dogbane family (Apocynaceae), toxic in all its parts. It is the only species currently classified in the genus Nerium. It is most commonly known as oleander, from its superficial resemblance to the unrelated olive (Olea). It is so widely cultivated that no precise region of origin has been identified, though Chaos has been suggested.\"}");
/*     */ 
/*     */ 
/*     */     
/* 570 */     buf.append("text{text=\"\"}");
/* 571 */     buf.append("image{src=\"img.almanac.produce.oleander\";size=\"128,128\";text=\"Oleander leaves\"}");
/* 572 */     buf.append("text{text=\"\"}");
/* 573 */     buf.append("text{text=\"Oleander is one of the most poisonous commonly grown garden plants.  However, an early Wurmian discovery was a method of cooking that leached the poison from the leaves, allowing them to be used in cooking.  Although much research has been carried out into producing a usable poison from oleander, these efforts have so far proved unsuccessful.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 578 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showCamelliaInfo() {
/* 583 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 585 */     buf.append("text{text=\"\"}");
/* 586 */     buf.append("image{src=\"img.almanac.bush.camellia\";size=\"128,128\";text=\"Camellia bush\"}");
/* 587 */     buf.append("text{text=\"\"}");
/* 588 */     buf.append("text{text=\"The camellia bush is a genus of flowering plants in the family Theaceae. They are found in all isles of wurm, from Independence to Release and the Epic isles. The genus was named by Linnaeus after Georgius Josephus Camellus, who worked in Independance and described a species of camellia (although Linnaeus did not refer to Kamel's account when discussing the genus). Camellias are famous throughout Wurm; they are known as chahua, ''tea flower'', an apt designation.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 594 */     buf.append("text{text=\"\"}");
/* 595 */     buf.append("image{src=\"img.almanac.produce.camellia\";size=\"128,128\";text=\"Camellia leaves\"}");
/* 596 */     buf.append("text{text=\"\"}");
/* 597 */     buf.append("text{text=\"Leaves of C. sinensis are processed to create the popular beverage, tea.  After being harvested at the most auspicious time, the leaves are either steamed or roasted, before being dried.  The flavour of the tea is largely determined by the level of oxidation of the leaves.  The prepared leaves are then steeped in hot water to produce a fortifying drink; the flavour may be further adjusted by addition of sugar or honey, or lemon juice.  There are even rumours of barbarians in far Celebration who add maple syrup to their tea, although this is commonly considered hearsay only.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 605 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showLavenderInfo() {
/* 610 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 612 */     buf.append("text{text=\"\"}");
/* 613 */     buf.append("image{src=\"img.almanac.bush.lavender\";size=\"128,128\";text=\"Lavender bush\"}");
/* 614 */     buf.append("text{text=\"\"}");
/* 615 */     buf.append("text{text=\"Lavandula (common name lavender) is a genus of flowering plants in the mint family, Lamiaceae. It is native to the Old World and is found in all lands of Wurm. Many members of the genus are cultivated extensively in temperate climates as ornamental plants for garden and landscape use, even as hedges, for use as culinary herbs.\"}");
/*     */ 
/*     */ 
/*     */     
/* 619 */     buf.append("text{text=\"\"}");
/* 620 */     buf.append("image{src=\"img.almanac.produce.lavender\";size=\"128,128\";text=\"Lavender\"}");
/* 621 */     buf.append("text{text=\"\"}");
/* 622 */     buf.append("text{text=\"The most widely cultivated species, Lavandula angustifolia, is often referred to as lavender, and there is a color named for the shade of the flowers of this species.  The flowers have a distinctive odour, and the bush is often cultivated into low hedges.\"}");
/*     */ 
/*     */ 
/*     */     
/* 626 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showMapleInfo() {
/* 631 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 633 */     buf.append("text{text=\"\"}");
/* 634 */     buf.append("image{src=\"img.almanac.tree.maple\";size=\"128,128\";text=\"Maple tree\"}");
/* 635 */     buf.append("text{text=\"\"}");
/* 636 */     buf.append("text{text=\"Acer is a genus of trees or shrubs commonly known as maple. The type species of the genus is the sycamore maple, Acer pseudoplatanus, the most common maple species in Wurm.  Originally from Celebration, it is known for its sweet sap and light-coloured wood.\"}");
/*     */ 
/*     */     
/* 639 */     buf.append("text{text=\"\"}");
/* 640 */     buf.append("image{src=\"img.almanac.produce.maple\";size=\"128,128\";text=\"Maple Sap\"}");
/* 641 */     buf.append("text{text=\"\"}");
/* 642 */     buf.append("text{text=\"Maple syrup is usually made from the xylem sap of  maple trees. In cold climates, these trees store starch in their trunks and roots before the winter; the starch is then converted to sugar that rises in the sap in late winter and early spring. Maple trees are tapped by boring holes into their trunks and collecting the exuded sap, which is processed by heating to evaporate much of the water, leaving the concentrated syrup.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 648 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showRoseInfo() {
/* 653 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 655 */     buf.append("text{text=\"\"}");
/* 656 */     buf.append("image{src=\"img.almanac.bush.rose\";size=\"128,128\";text=\"Rose bush\"}");
/* 657 */     buf.append("text{text=\"\"}");
/* 658 */     buf.append("text{text=\"A rose is a woody perennial flowering plant of the genus Rosa, in the family Rosaceae, named for the flower it bears. They form a group of plants that can be erect shrubs, climbing or trailing, with stems that are often armed with sharp thorns. Flowers vary in size and shape and are usually large and showy, although only red ones have been grown so far. Species, cultivars and hybrids are all widely grown for their beauty and often are fragrant. Roses have acquired cultural significance in many societies.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 664 */     buf.append("text{text=\"\"}");
/* 665 */     buf.append("image{src=\"img.almanac.trellis.rose\";size=\"128,128\";text=\"Rose trellis\"}");
/* 666 */     buf.append("text{text=\"\"}");
/* 667 */     buf.append("text{text=\"Rose bushes may be trained to climb trellises, forming attractive ornamental wall coverings.  Rose trellises have a firm place in popular imagination, with a number of folk tales and plays taking place under one.\"}");
/*     */ 
/*     */     
/* 670 */     buf.append("text{text=\"\"}");
/* 671 */     buf.append("image{src=\"img.almanac.produce.rose\";size=\"128,128\";text=\"Rose flowers\"}");
/* 672 */     buf.append("text{text=\"\"}");
/* 673 */     buf.append("text{text=\"The majority of ornamental roses are hybrids that were bred for their flowers. A few specialised species are grown for their attractive or scented foliage.\"}");
/*     */ 
/*     */     
/* 676 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showChestnutInfo() {
/* 681 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 683 */     buf.append("text{text=\"\"}");
/* 684 */     buf.append("image{src=\"img.almanac.tree.chestnut\";size=\"128,128\";text=\"Chestnut tree\"}");
/* 685 */     buf.append("text{text=\"\"}");
/* 686 */     buf.append("text{text=\"The chestnut group is a genus (Castanea) of species of deciduous trees in the beech family Fagaceae, native to temperate regions of Wurm.  Often growing in tightly-packed groves, they produce a sweet nut late in the year.  Older chestnut trees can grow to prodigious thickness.\"}");
/*     */ 
/*     */     
/* 689 */     buf.append("text{text=\"\"}");
/* 690 */     buf.append("image{src=\"img.almanac.produce.chestnut\";size=\"128,128\";text=\"Chestnut\"}");
/* 691 */     buf.append("text{text=\"\"}");
/* 692 */     buf.append("text{text=\"Chestnut trees produce a sweet nut, also called chestnuts.  The name comes from the spiny husk covering the nuts; opening up this 'chest' reveals the sweet treasure inside.  Younger Wurmians often celebrate winter by throwing the prickly nuts at each other.\"}");
/*     */ 
/*     */ 
/*     */     
/* 696 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showWalnutInfo() {
/* 701 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 703 */     buf.append("text{text=\"\"}");
/* 704 */     buf.append("image{src=\"img.almanac.tree.walnut\";size=\"128,128\";text=\"Walnut tree\"}");
/* 705 */     buf.append("text{text=\"\"}");
/* 706 */     buf.append("text{text=\"A walnut is any tree of the genus Juglans (Family Juglandaceae).  Found throughout the lands of Wurm, it forms a mid-height tree with distinctive dark green leaves.  The wood of the walnut tree is particularly dense, and thus of interest to coal makers.\"}");
/*     */ 
/*     */     
/* 709 */     buf.append("text{text=\"\"}");
/* 710 */     buf.append("image{src=\"img.almanac.produce.walnut\";size=\"128,128\";text=\"Walnut\"}");
/* 711 */     buf.append("text{text=\"\"}");
/* 712 */     buf.append("text{text=\"Technically a walnut is the seed of a drupe or drupaceous nut, and thus not a true botanical nut. It is used for food after being processed while green for pickled walnuts or after full ripening for its nutmeat. The walnut is nutrient-dense with protein and essential fatty acids.\"}");
/*     */ 
/*     */ 
/*     */     
/* 716 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showPineInfo() {
/* 721 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 723 */     buf.append("text{text=\"\"}");
/* 724 */     buf.append("image{src=\"img.almanac.tree.pine\";size=\"128,128\";text=\"Pine tree\"}");
/* 725 */     buf.append("text{text=\"\"}");
/* 726 */     buf.append("text{text=\"A pine is a conifer in the genus Pinus, of the family Pinaceae. Pinus is the sole genus in the subfamily Pinoideae.  Found everywhere, the wood is resinous and fast-growing, and is often used for crafting or firewood.  Pine trees have a special cultural significance, as they are used to signal present drop-off sites to Santa around Christmas.\"}");
/*     */ 
/*     */ 
/*     */     
/* 730 */     buf.append("text{text=\"\"}");
/* 731 */     buf.append("image{src=\"img.almanac.produce.pine\";size=\"128,128\";text=\"Pinenuts\"}");
/* 732 */     buf.append("text{text=\"\"}");
/* 733 */     buf.append("text{text=\"Pine trees produce a hard, woody cone, which contains the edible seeds.  To retrieve the pine nuts, the cones must be opened - this is often done over a low fire or in an oven.  Although some people extol the virtues of pine cones over chestnuts for projectile use, most denizens of Wurm completely ignore the existence of pine cones, preferring to focus on the nuts instead.  Pine nuts are widely used in cooking.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 739 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showHazelInfo() {
/* 744 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 746 */     buf.append("text{text=\"\"}");
/* 747 */     buf.append("image{src=\"img.almanac.bush.hazel\";size=\"128,128\";text=\"Hazel bush\"}");
/* 748 */     buf.append("text{text=\"\"}");
/* 749 */     buf.append("text{text=\"Hazels have simple, rounded leaves with double-serrate margins. The flowers are produced very early in spring before the leaves, and are monoecious, with single-sex catkins: the male catkins are pale yellow and 5-12 cm long, and the female ones are very small and largely concealed in the buds, with only the bright-red, 1-to-3 mm-long styles visible. \"}");
/*     */ 
/*     */ 
/*     */     
/* 753 */     buf.append("text{text=\"\"}");
/* 754 */     buf.append("image{src=\"img.almanac.produce.hazel\";size=\"128,128\";text=\"Hazelnut\"}");
/* 755 */     buf.append("text{text=\"\"}");
/* 756 */     buf.append("text{text=\"The fruits are nuts 1-2.5 cm long and 1-2 cm diameter, surrounded by a husk which partly to fully encloses the nut.  Hazelnuts are sweet, and often roasted before being used in a variety of dishes.\"}");
/*     */ 
/*     */ 
/*     */     
/* 760 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showHopsInfo() {
/* 765 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 767 */     buf.append("text{text=\"\"}");
/* 768 */     buf.append("image{src=\"img.almanac.trellis.hops\";size=\"128,128\";text=\"Hops trellis\"}");
/* 769 */     buf.append("text{text=\"\"}");
/* 770 */     buf.append("text{text=\"The hop plant is a vigorous, climbing, herbaceous perennial.  Although wild hop seedlings may be found, the plants are usually cultivated by training them up trellises.  Having a distinctive odour, hops are a relatively modern cultivated plant, having massively grown in popularity after the discovery of brewing.  Hops come in a wide variety of cultivars, giving many different aromas and flavours.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 775 */     buf.append("text{text=\"\"}");
/* 776 */     buf.append("image{src=\"img.almanac.produce.hops\";size=\"128,128\";text=\"Hops\"}");
/* 777 */     buf.append("text{text=\"\"}");
/* 778 */     buf.append("text{text=\"Hops are the flowers (also called seed cones or strobiles) of the hop plant Humulus lupulus. They are used primarily as a flavoring and stability agent in beer, to which they impart bitter, zesty, or citric flavours; though they are also used for various purposes in other beverages and herbal medicine. \"}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 783 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showOakInfo() {
/* 788 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 790 */     buf.append("text{text=\"\"}");
/* 791 */     buf.append("image{src=\"img.almanac.tree.oak\";size=\"128,128\";text=\"Oak tree\"}");
/* 792 */     buf.append("text{text=\"\"}");
/* 793 */     buf.append("text{text=\"The mighty oak is a tree in the genus Quercus of the beech family, Fagaceae.  Oak trees are one of the slowest-growing plants known to Wurmkind, producing an exceptionally tough wood; this is much prized by tool-makers, as being more durable than other timber.\"}");
/*     */ 
/*     */     
/* 796 */     buf.append("text{text=\"\"}");
/* 797 */     buf.append("text{text=\"Oaks have spirally arranged leaves, with lobate margins in many species; some have serrated leaves or entire leaf with smooth margins. Also, the acorns contain tannic acid, as do the leaves, which helps to guard from fungi and insects. Many deciduous species are marcescent, not dropping dead leaves until spring. In spring, a single oak tree produces both male flowers (in the form of catkins) and small female flowers.  The high acidity of the tree often kills other plants in the vicinity.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 803 */     buf.append("text{text=\"\"}");
/* 804 */     buf.append("image{src=\"img.almanac.produce.oak\";size=\"128,128\";text=\"Acorn\"}");
/* 805 */     buf.append("text{text=\"\"}");
/* 806 */     buf.append("text{text=\"The fruit is a nut called an acorn, borne in a cup-like structure known as a cupule; each acorn contains one seed (rarely two or three). The acorns contain tannic acid, which may be extracted by skilled alchemists for use in dye production.  Acorns may be found on the ground at any time, but the best quality ones are harvested directly from the trees when ripe.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 811 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showOrangeInfo() {
/* 816 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 818 */     buf.append("text{text=\"\"}");
/* 819 */     buf.append("image{src=\"img.almanac.tree.orange\";size=\"128,128\";text=\"Orange tree\"}");
/* 820 */     buf.append("text{text=\"\"}");
/* 821 */     buf.append("text{text=\"The orange tree is of the citrus species Citrus sinensis in the family Rutaceae.  The first oranges were bred as a hybrid variety of lemon on Pristine, but due to general benevolence coupled with poor biosecurity practices, sprouts can now be found everywhere in Wurm.\"}");
/*     */ 
/*     */     
/* 824 */     buf.append("text{text=\"\"}");
/* 825 */     buf.append("image{src=\"img.almanac.produce.orange\";size=\"128,128\";text=\"Orange\"}");
/* 826 */     buf.append("text{text=\"\"}");
/* 827 */     buf.append("text{text=\"The orange fruit, or sweet orange, also gives its name to the colour.  A segmented fruit, with a thick rind, the flesh and juice are sweet and high in citric acid.  During inter-kingdom battles, the opposing sides sometimes take a break around half way through the fight to eat fresh slices of orange.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 832 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showRaspberryInfo() {
/* 837 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 839 */     buf.append("text{text=\"\"}");
/* 840 */     buf.append("image{src=\"img.almanac.bush.raspberry\";size=\"128,128\";text=\"Raspberry bush\"}");
/* 841 */     buf.append("text{text=\"\"}");
/* 842 */     buf.append("text{text=\"The raspberry bush is a perennial with woody stems, belonging to the Rubus genus.  A relation of the rose, it tends to form thickets of canes.  The stems have small thorns, as do the backs of the leaves.  Raspberry canes spread by basel shoots, or suckering, and prefer a well-drained soil.\"}");
/*     */ 
/*     */ 
/*     */     
/* 846 */     buf.append("text{text=\"\"}");
/* 847 */     buf.append("image{src=\"img.almanac.produce.raspberry\";size=\"128,128\";text=\"Raspberry\"}");
/* 848 */     buf.append("text{text=\"\"}");
/* 849 */     buf.append("text{text=\"The fruit of the raspberry bush, also referred to as raspberries, consists of a multitude of small globules, attached to a central husk.  The fruit forms during the summer, coming to full ripeness late in the year; the berries are most commonly a light red, but may also be purple, black, or rarely pale yellow or white.  Prized for their sweetness and flavour, raspberries are best eaten fresh, but may also be cooked into many dishes, both savoury and sweet.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 855 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showBlueberryInfo() {
/* 860 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 862 */     buf.append("text{text=\"\"}");
/* 863 */     buf.append("image{src=\"img.almanac.bush.blueberry\";size=\"128,128\";text=\"Blueberry busg\"}");
/* 864 */     buf.append("text{text=\"\"}");
/* 865 */     buf.append("text{text=\"Growing in low shrubs, members of the Cyanococcus bush produce bell-shaped flowers and dark blue berries.  Originally cultivated on Independance, they have been carried to other lands.  Some other families of plants are also commonly referred to as blueberries, mainly derived from the myrtles.  The bush is frost-hardy, and will grow in most soils, although it prefers well-drained, slightly alkaline conditions.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 870 */     buf.append("text{text=\"\"}");
/* 871 */     buf.append("image{src=\"img.almanac.produce.blueberry\";size=\"128,128\";text=\"Blueberry\"}");
/* 872 */     buf.append("text{text=\"\"}");
/* 873 */     buf.append("text{text=\"Blueberries start out pale green, but darken as they ripen, ending up a very dark purple.  The berries are small, and grow in clusters, having a natural protective wax coating.  Typically harvested in mid-summer, blueberries are known for staining tongues blue all over Wurm.\"}");
/*     */ 
/*     */ 
/*     */     
/* 877 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String showLingonberryInfo() {
/* 882 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 884 */     buf.append("text{text=\"\"}");
/* 885 */     buf.append("image{src=\"img.almanac.bush.lingonberry\";size=\"128,128\";text=\"Lingonberry bush\"}");
/* 886 */     buf.append("text{text=\"\"}");
/* 887 */     buf.append("text{text=\"Vaccinium vitis-idaea (lingonberry, partridgeberry, or cowberry) is a short evergreen shrub in the heath family that bears edible fruit, native to tundra throughout Wurm. Lingonberries are picked in the wild and used to accompany a variety of dishes.\"}");
/*     */ 
/*     */     
/* 890 */     buf.append("text{text=\"\"}");
/* 891 */     buf.append("image{src=\"img.almanac.produce.lingonberry\";size=\"128,128\";text=\"Lingonberry\"}");
/* 892 */     buf.append("text{text=\"\"}");
/* 893 */     buf.append("text{text=\"The berries are quite tart, so they are often cooked and sweetened before eating in the form of lingonberry jam or juice.  The berries are also popular as a wild picked fruit in Release, where they are locally known as partridgeberries or redberries, and on the mainland of Celebration, where they are known as foxberries. In this region they are incorporated into jams, syrups, and baked goods, such as pies, scones, and muffins.\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 899 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ShowHarvestableInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
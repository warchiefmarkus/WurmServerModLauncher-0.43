/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.function.BiConsumer;
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
/*     */ public final class ServerTweaksHandler
/*     */ {
/*     */   public enum Tweak
/*     */   {
/*  35 */     UNKNOWN("UNKNOWN", "", "", 0, (String)ServerTweaksHandler::handleUnknownCommad),
/*  36 */     FIELD_GROWTH("/setfieldgrowthtime", "/setfieldgrowthtime <time> <password>", "Sets how often fields will be polled.", 2, (String)ServerTweaksHandler::handleFieldGrowthCommand),
/*  37 */     SKILL_GAIN_RATE("/setskillgainmultiplier", "/setskillgainmultiplier <rate> <password>", "Skill gain rate multiplier.", 2, (String)ServerTweaksHandler::handleSkillGainRateCommand),
/*  38 */     CHARACTERISTICS_START("/setcharacteristicsstartvalue", "/setcharacteristicsstartvalue <value> <password>", "Sets the starting value of the characteristics skills for new players. (requires restart)", 2, (String)ServerTweaksHandler::handleCharacteristicsStartCommand, true),
/*  39 */     MIND_LOGIC_START("/setmindlogicstartvalue", "/setmindlogicstartvalue <value> <password>", "Sets the starting value of mind logic for new players. (requires restart)", 2, (String)ServerTweaksHandler::handleMindLogicStartCommand, true),
/*  40 */     BC_START("/setbodycontrolstartvalue", "/setbodycontrolstartvalue <value> <password>", "Sets the starting value of the body control skill for new players. (requires restart)", 2, (String)ServerTweaksHandler::handleBodyControlStartCommand, true),
/*  41 */     FIGHT_START("/setfightingstartvalue", "/setfightingstartvalue <value> <password>", "Sets the fighting skill start value for new players. (requires restart)", 2, (String)ServerTweaksHandler::handleFightingStartCommand, true),
/*  42 */     OVERALL_START("/setoverallstartskillvalue", "/setoverallstartskillvalue <value> <password>", "Sets the overall starting skill value for new players. (restart required)", 2, (String)ServerTweaksHandler::handleOverallStartCommand, true),
/*  43 */     PLAYER_CR("/setplayercrmod", "/setplayercrmod <CR> <password>", "Sets the combat rating of players.", 2, (String)ServerTweaksHandler::handlePlayerCRCommand),
/*  44 */     ACTION_SPEED("/setactionspeedmod", "/setactionspeedmod <mod> <password>", "Speeds up or slows down action timers.", 2, (String)ServerTweaksHandler::handleActionSpeedCommand),
/*  45 */     HOTA("/sethotadelay", "/sethotadelay <delay> <password>", "HOTA delay", 2, (String)ServerTweaksHandler::handleHOTACommand),
/*  46 */     MAX_CREATURES("/setmaxcreatures", "/setmaxcreatures <max> <password>", "Sets the maximum number of creatures that can naturally spawn on the server.", 2, (String)ServerTweaksHandler::handleMaxCreaturesCommand),
/*  47 */     AGG_PERCENT("/setmaxaggcreatures", "/setmaxaggcreatures <percent> <password>", "Sets the % of the creature pool that can be aggressive creatures.", 2, (String)ServerTweaksHandler::handleAggCreaturesCommand),
/*  48 */     UPKEEP("/setupkeep", "/setupkeep <true/false> <password>", "Toggle settlement upkeep on or off.", 2, (String)ServerTweaksHandler::handleUpkeepCommand),
/*  49 */     FREE_DEEDS("/setfreedeeds", "/setfreedeeds <true/false> <password>", "Toggle free deed creation on or off.", 2, (String)ServerTweaksHandler::handleFreeDeedsCommand),
/*  50 */     TRADER_MAX_MONEY("/settradermaxmoney", "/settradermaxmoney <silver> <password>", "Sets the max amount of money a trader can have.", 2, (String)ServerTweaksHandler::handleTraderMaxMoneyCommand),
/*  51 */     TRADER_INITIAL_MONEY("/settraderinitialmoney", "/settraderinitialmoney <silver> <password>", "Sets the initial amount of money a trader has.", 2, (String)ServerTweaksHandler::handleTraderInitialMoneyCommand),
/*  52 */     MINING_HITS("/setminimummininghits", "/setminimummininghits <hits> <password>", "Sets the amount of hits required to tunnel through rock.", 2, (String)ServerTweaksHandler::handleMinimumHitsCommand),
/*  53 */     BREEDING_TIME("/setbreedingtime", "/setbreedingtime <mod> <password>", "Modifier to speed up or slow down breeding.", 2, (String)ServerTweaksHandler::handleBreedingTimeCommand),
/*  54 */     TREE_GROWTH("/settreespreadodds", "/settreespreadodds <odds> <password>", "Toggles the spreading of trees and mushrooms.", 2, (String)ServerTweaksHandler::handleTreeSpreadOddsCommand),
/*  55 */     MONEY_POOL("/setmoneypool", "/setmoneypool <silver> <password>", "Sets the amount of money in the server pool. (requires restart)", 2, (String)ServerTweaksHandler::handleMoneyPoolCommand);
/*     */     
/*     */     final String command;
/*     */     
/*     */     final String parameterString;
/*     */     final String helpDescription;
/*     */     final int expectedNumberOfTokens;
/*     */     final BiConsumer<StringTokenizer, Player> cmd;
/*     */     final boolean requiresRestart;
/*     */     
/*     */     Tweak(String _command, String _parameter, String _helpDescription, int numberOfTokens, BiConsumer<StringTokenizer, Player> _cmd) {
/*  66 */       this.command = _command;
/*  67 */       this.parameterString = _parameter;
/*  68 */       this.helpDescription = _helpDescription;
/*  69 */       this.expectedNumberOfTokens = numberOfTokens;
/*  70 */       this.cmd = _cmd;
/*  71 */       this.requiresRestart = false;
/*     */     }
/*     */ 
/*     */     
/*     */     Tweak(String _command, String _parameter, String _helpDescription, int numberOfTokens, BiConsumer<StringTokenizer, Player> _cmd, boolean restart) {
/*  76 */       this.command = _command;
/*  77 */       this.parameterString = _parameter;
/*  78 */       this.helpDescription = _helpDescription;
/*  79 */       this.expectedNumberOfTokens = numberOfTokens;
/*  80 */       this.cmd = _cmd;
/*  81 */       this.requiresRestart = restart;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getCommand() {
/*  86 */       return this.command;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getParameterString() {
/*  91 */       return this.parameterString;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int tokenCount() {
/*  96 */       return this.expectedNumberOfTokens;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void execute(StringTokenizer tokenizer, Player admin) {
/* 101 */       if (this.cmd != null)
/*     */       {
/* 103 */         this.cmd.accept(tokenizer, admin);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public static final Tweak getByCommand(String cmd) {
/* 109 */       for (Tweak tweak : values()) {
/*     */         
/* 111 */         if (tweak.getCommand().equalsIgnoreCase(cmd))
/*     */         {
/* 113 */           return tweak;
/*     */         }
/*     */       } 
/*     */       
/* 117 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTweakCommand(String message) {
/* 124 */     for (Tweak tweak : Tweak.values()) {
/*     */       
/* 126 */       if (message.startsWith(tweak.getCommand())) {
/* 127 */         return true;
/*     */       }
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleTweakCommand(String message, Player admin) {
/* 135 */     StringTokenizer tokenizer = new StringTokenizer(message);
/*     */     
/* 137 */     String cmd = tokenizer.nextToken();
/* 138 */     Tweak tweak = Tweak.getByCommand(cmd);
/*     */     
/* 140 */     tweak.execute(tokenizer, admin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatePassword(String pass, Player admin) {
/* 148 */     String adminPass = ServerProperties.getString("ADMINPASSWORD", "");
/* 149 */     if (adminPass.isEmpty()) {
/*     */       
/* 151 */       admin.getCommunicator().sendNormalServerMessage("There is no admin password on this server, so admin commands is disabled.");
/* 152 */       return false;
/*     */     } 
/*     */     
/* 155 */     if (pass.equals(adminPass)) {
/* 156 */       return true;
/*     */     }
/* 158 */     admin.getCommunicator().sendNormalServerMessage("Incorrect admin password.");
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleUnknownCommad(StringTokenizer tokenizer, Player admin) {
/* 164 */     admin.getCommunicator().sendNormalServerMessage("Unknown command.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean tokenCheck(Tweak tweak, StringTokenizer tokenizer, Player admin) {
/* 169 */     int numTokens = tokenizer.countTokens();
/* 170 */     if (numTokens != tweak.tokenCount()) {
/*     */       
/* 172 */       String message = "Incorrect number of parameters! Provided: %d Expected: %d";
/* 173 */       admin.getCommunicator().sendNormalServerMessage(String.format("Incorrect number of parameters! Provided: %d Expected: %d", new Object[] { Integer.valueOf(numTokens), Integer.valueOf(tweak.tokenCount()) }));
/* 174 */       return false;
/*     */     } 
/*     */     
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleSkillGainRateCommand(StringTokenizer tokenizer, Player admin) {
/* 182 */     if (!tokenCheck(Tweak.SKILL_GAIN_RATE, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 185 */     String param = tokenizer.nextToken();
/* 186 */     String pass = tokenizer.nextToken();
/*     */     
/* 188 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 193 */       float rate = Float.parseFloat(param);
/* 194 */       rate = Math.max(0.01F, rate);
/* 195 */       admin.getCommunicator().sendNormalServerMessage("Changed skill gain multiplier to: " + rate + ".");
/* 196 */       Servers.localServer.setSkillGainRate(rate);
/*     */ 
/*     */       
/* 199 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 201 */     catch (NumberFormatException nfe) {
/*     */       
/* 203 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleFieldGrowthCommand(StringTokenizer tokenizer, Player admin) {
/* 209 */     if (!tokenCheck(Tweak.FIELD_GROWTH, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 212 */     String param = tokenizer.nextToken();
/* 213 */     String pass = tokenizer.nextToken();
/*     */     
/* 215 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 219 */       Float time = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 221 */       admin.getCommunicator().sendNormalServerMessage("Changed field growth timer to: " + time.toString() + " hours.");
/* 222 */       Servers.localServer.setFieldGrowthTime((long)(time.floatValue() * 3600.0F * 1000.0F));
/*     */       
/* 224 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 226 */     catch (NumberFormatException nfe) {
/*     */       
/* 228 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleCharacteristicsStartCommand(StringTokenizer tokenizer, Player admin) {
/* 234 */     if (!tokenCheck(Tweak.CHARACTERISTICS_START, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 237 */     String param = tokenizer.nextToken();
/* 238 */     String pass = tokenizer.nextToken();
/*     */     
/* 240 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 244 */       Float charVal = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 246 */       admin.getCommunicator().sendNormalServerMessage("Changed characteristics start value to: " + charVal.toString() + ".");
/* 247 */       Servers.localServer.setSkillbasicval(charVal.floatValue());
/*     */       
/* 249 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/* 250 */       admin.getCommunicator().sendNormalServerMessage("Server restart needed before the changes take effect.");
/*     */     }
/* 252 */     catch (NumberFormatException nfe) {
/*     */       
/* 254 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleMindLogicStartCommand(StringTokenizer tokenizer, Player admin) {
/* 260 */     if (!tokenCheck(Tweak.MIND_LOGIC_START, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 263 */     String param = tokenizer.nextToken();
/* 264 */     String pass = tokenizer.nextToken();
/*     */     
/* 266 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 270 */       Float val = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 272 */       admin.getCommunicator().sendNormalServerMessage("Changed mind logic start value to: " + val.toString() + ".");
/* 273 */       Servers.localServer.setSkillmindval(val.floatValue());
/*     */       
/* 275 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/* 276 */       admin.getCommunicator().sendNormalServerMessage("Server restart needed before the changes take effect.");
/*     */     }
/* 278 */     catch (NumberFormatException nfe) {
/*     */       
/* 280 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleBodyControlStartCommand(StringTokenizer tokenizer, Player admin) {
/* 286 */     if (!tokenCheck(Tweak.BC_START, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 289 */     String param = tokenizer.nextToken();
/* 290 */     String pass = tokenizer.nextToken();
/*     */     
/* 292 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 296 */       Float val = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 298 */       admin.getCommunicator().sendNormalServerMessage("Changed body control start value to: " + val.toString() + ".");
/* 299 */       Servers.localServer.setSkillbcval(val.floatValue());
/*     */       
/* 301 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/* 302 */       admin.getCommunicator().sendNormalServerMessage("Server restart needed before the changes take effect.");
/*     */     }
/* 304 */     catch (NumberFormatException nfe) {
/*     */       
/* 306 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleFightingStartCommand(StringTokenizer tokenizer, Player admin) {
/* 312 */     if (!tokenCheck(Tweak.FIGHT_START, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 315 */     String param = tokenizer.nextToken();
/* 316 */     String pass = tokenizer.nextToken();
/*     */     
/* 318 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 322 */       Float val = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 324 */       admin.getCommunicator().sendNormalServerMessage("Changed fighting start value to: " + val.toString() + ".");
/* 325 */       Servers.localServer.setSkillfightval(val.floatValue());
/*     */       
/* 327 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/* 328 */       admin.getCommunicator().sendNormalServerMessage("Server restart needed before the changes take effect.");
/*     */     }
/* 330 */     catch (NumberFormatException nfe) {
/*     */       
/* 332 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleOverallStartCommand(StringTokenizer tokenizer, Player admin) {
/* 338 */     if (!tokenCheck(Tweak.OVERALL_START, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 341 */     String param = tokenizer.nextToken();
/* 342 */     String pass = tokenizer.nextToken();
/*     */     
/* 344 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 348 */       Float val = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 350 */       admin.getCommunicator().sendNormalServerMessage("Changed overall start skill value to: " + val.toString() + ".");
/* 351 */       Servers.localServer.setSkilloverallval(val.floatValue());
/*     */       
/* 353 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/* 354 */       admin.getCommunicator().sendNormalServerMessage("Server restart needed before the changes take effect.");
/*     */     }
/* 356 */     catch (NumberFormatException nfe) {
/*     */       
/* 358 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handlePlayerCRCommand(StringTokenizer tokenizer, Player admin) {
/* 364 */     if (!tokenCheck(Tweak.PLAYER_CR, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 367 */     String param = tokenizer.nextToken();
/* 368 */     String pass = tokenizer.nextToken();
/*     */     
/* 370 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 374 */       Float val = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 376 */       admin.getCommunicator().sendNormalServerMessage("Changed player CR mod to: " + val.toString() + ".");
/* 377 */       Servers.localServer.setCombatRatingModifier(val.floatValue());
/*     */       
/* 379 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 381 */     catch (NumberFormatException nfe) {
/*     */       
/* 383 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleActionSpeedCommand(StringTokenizer tokenizer, Player admin) {
/* 389 */     if (!tokenCheck(Tweak.ACTION_SPEED, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 392 */     String param = tokenizer.nextToken();
/* 393 */     String pass = tokenizer.nextToken();
/*     */     
/* 395 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 399 */       Float val = Float.valueOf(Float.parseFloat(param));
/*     */       
/* 401 */       admin.getCommunicator().sendNormalServerMessage("Changed action speed mod to: " + val.toString() + ".");
/* 402 */       Servers.localServer.setActionTimer(val.floatValue());
/*     */       
/* 404 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 406 */     catch (NumberFormatException nfe) {
/*     */       
/* 408 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleHOTACommand(StringTokenizer tokenizer, Player admin) {
/* 414 */     if (!tokenCheck(Tweak.HOTA, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 417 */     String param = tokenizer.nextToken();
/* 418 */     String pass = tokenizer.nextToken();
/*     */     
/* 420 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 424 */       Integer val = Integer.valueOf(Integer.parseInt(param));
/*     */       
/* 426 */       admin.getCommunicator().sendNormalServerMessage("Changed HOTA delay to: " + val.toString() + ".");
/* 427 */       Servers.localServer.setHotaDelay(val.intValue());
/*     */       
/* 429 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 431 */     catch (NumberFormatException nfe) {
/*     */       
/* 433 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleMaxCreaturesCommand(StringTokenizer tokenizer, Player admin) {
/* 439 */     if (!tokenCheck(Tweak.MAX_CREATURES, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 442 */     String param = tokenizer.nextToken();
/* 443 */     String pass = tokenizer.nextToken();
/*     */     
/* 445 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 449 */       int val = Integer.parseInt(param);
/* 450 */       val = Math.max(0, val);
/*     */       
/* 452 */       admin.getCommunicator().sendNormalServerMessage("Changed max creatures to: " + val + ".");
/* 453 */       Servers.localServer.maxCreatures = val;
/*     */       
/* 455 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 457 */     catch (NumberFormatException nfe) {
/*     */       
/* 459 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleAggCreaturesCommand(StringTokenizer tokenizer, Player admin) {
/* 465 */     if (!tokenCheck(Tweak.AGG_PERCENT, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 468 */     String param = tokenizer.nextToken();
/* 469 */     String pass = tokenizer.nextToken();
/*     */     
/* 471 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 475 */       float val = Float.parseFloat(param);
/* 476 */       val = Math.max(0.0F, Math.min(100.0F, val));
/* 477 */       admin.getCommunicator().sendNormalServerMessage("Changed aggressive creature % to: " + val + ".");
/* 478 */       Servers.localServer.percentAggCreatures = val;
/*     */       
/* 480 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 482 */     catch (NumberFormatException nfe) {
/*     */       
/* 484 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleUpkeepCommand(StringTokenizer tokenizer, Player admin) {
/* 490 */     if (!tokenCheck(Tweak.UPKEEP, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 493 */     String param = tokenizer.nextToken();
/* 494 */     String pass = tokenizer.nextToken();
/*     */     
/* 496 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/* 499 */     boolean val = Boolean.parseBoolean(param);
/*     */     
/* 501 */     admin.getCommunicator().sendNormalServerMessage("Changed upkeep to: " + val + ".");
/* 502 */     Servers.localServer.setUpkeep(val);
/*     */     
/* 504 */     Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void handleFreeDeedsCommand(StringTokenizer tokenizer, Player admin) {
/* 510 */     if (!tokenCheck(Tweak.FREE_DEEDS, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 513 */     String param = tokenizer.nextToken();
/* 514 */     String pass = tokenizer.nextToken();
/*     */     
/* 516 */     if (!validatePassword(pass, admin))
/*     */       return; 
/* 518 */     boolean val = Boolean.parseBoolean(param);
/*     */     
/* 520 */     admin.getCommunicator().sendNormalServerMessage("Changed free deeding to: " + val + ".");
/* 521 */     Servers.localServer.setFreeDeeds(val);
/*     */     
/* 523 */     Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void handleTraderMaxMoneyCommand(StringTokenizer tokenizer, Player admin) {
/* 529 */     if (!tokenCheck(Tweak.TRADER_MAX_MONEY, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 532 */     String param = tokenizer.nextToken();
/* 533 */     String pass = tokenizer.nextToken();
/*     */     
/* 535 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 539 */       int val = Integer.parseInt(param);
/* 540 */       val = Math.max(0, val);
/* 541 */       admin.getCommunicator().sendNormalServerMessage("Changed trader max money to: " + val + " silver.");
/* 542 */       Servers.localServer.setTraderMaxIrons(val * 10000);
/*     */       
/* 544 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 546 */     catch (NumberFormatException nfe) {
/*     */       
/* 548 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleTraderInitialMoneyCommand(StringTokenizer tokenizer, Player admin) {
/* 554 */     if (!tokenCheck(Tweak.TRADER_INITIAL_MONEY, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 557 */     String param = tokenizer.nextToken();
/* 558 */     String pass = tokenizer.nextToken();
/*     */     
/* 560 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 564 */       int val = Integer.parseInt(param);
/* 565 */       val = Math.max(0, val);
/* 566 */       admin.getCommunicator().sendNormalServerMessage("Changed trader initial money to: " + val + " silver.");
/* 567 */       Servers.localServer.setInitialTraderIrons(val * 10000);
/*     */       
/* 569 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 571 */     catch (NumberFormatException nfe) {
/*     */       
/* 573 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleMinimumHitsCommand(StringTokenizer tokenizer, Player admin) {
/* 579 */     if (!tokenCheck(Tweak.MINING_HITS, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 582 */     String param = tokenizer.nextToken();
/* 583 */     String pass = tokenizer.nextToken();
/*     */     
/* 585 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 589 */       int val = Integer.parseInt(param);
/* 590 */       val = Math.max(0, val);
/* 591 */       admin.getCommunicator().sendNormalServerMessage("Changed minimum mining hits on rock to: " + val + ".");
/* 592 */       Servers.localServer.setTunnelingHits(val);
/*     */       
/* 594 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 596 */     catch (NumberFormatException nfe) {
/*     */       
/* 598 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleBreedingTimeCommand(StringTokenizer tokenizer, Player admin) {
/* 604 */     if (!tokenCheck(Tweak.BREEDING_TIME, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 607 */     String param = tokenizer.nextToken();
/* 608 */     String pass = tokenizer.nextToken();
/*     */     
/* 610 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 614 */       long val = Long.parseLong(param);
/* 615 */       val = Math.max(1L, val);
/* 616 */       admin.getCommunicator().sendNormalServerMessage("Changed breeding time modifier to: " + val + ".");
/* 617 */       Servers.localServer.setBreedingTimer(val);
/*     */       
/* 619 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 621 */     catch (NumberFormatException nfe) {
/*     */       
/* 623 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleTreeSpreadOddsCommand(StringTokenizer tokenizer, Player admin) {
/* 629 */     if (!tokenCheck(Tweak.TREE_GROWTH, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 632 */     String param = tokenizer.nextToken();
/* 633 */     String pass = tokenizer.nextToken();
/*     */     
/* 635 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 639 */       int val = Integer.parseInt(param);
/* 640 */       val = Math.max(0, val);
/* 641 */       admin.getCommunicator().sendNormalServerMessage("Changed tree spread odds to: " + val + ".");
/* 642 */       Servers.localServer.treeGrowth = val;
/*     */       
/* 644 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 646 */     catch (NumberFormatException nfe) {
/*     */       
/* 648 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void handleMoneyPoolCommand(StringTokenizer tokenizer, Player admin) {
/* 654 */     if (!tokenCheck(Tweak.MONEY_POOL, tokenizer, admin)) {
/*     */       return;
/*     */     }
/* 657 */     String param = tokenizer.nextToken();
/* 658 */     String pass = tokenizer.nextToken();
/*     */     
/* 660 */     if (!validatePassword(pass, admin)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 664 */       int val = Integer.parseInt(param);
/* 665 */       val = Math.max(0, val);
/* 666 */       admin.getCommunicator().sendNormalServerMessage("Money pool will be set to: " + val + " after a restart.");
/* 667 */       Servers.localServer.setKingsmoneyAtRestart(val * 10000);
/*     */       
/* 669 */       Servers.localServer.saveNewGui(Servers.localServer.id);
/*     */     }
/* 671 */     catch (NumberFormatException nfe) {
/*     */       
/* 673 */       admin.getCommunicator().sendNormalServerMessage("'" + param + "' is not in the correct format.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sendHelp(Player player) {
/* 679 */     Communicator com = player.getCommunicator();
/*     */     
/* 681 */     for (Tweak tweak : Tweak.values()) {
/*     */       
/* 683 */       if (tweak != Tweak.UNKNOWN)
/*     */       {
/* 685 */         com.sendHelpMessage(tweak.parameterString + " - " + tweak.helpDescription);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\ServerTweaksHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class PlayerProfileQuestion
/*     */   extends Question
/*     */ {
/*  34 */   private static final Logger logger = Logger.getLogger(PlayerProfileQuestion.class.getName());
/*     */   
/*     */   private static final int WIDTH = 420;
/*     */   
/*     */   private static final int HEIGHT = 540;
/*     */   
/*     */   private static final String OPT_NO_PMS = "opt1";
/*     */   
/*     */   private static final String OPT_CROSS_KINGDOM = "opt2";
/*     */   
/*     */   private static final String OPT_CROSS_SERVER = "opt3";
/*     */   
/*     */   private static final String OPT_FRIENDS_OVERRIDE = "opt4";
/*     */   
/*     */   private static final String OPT_HIDE_EQUIP_OPTIONS = "opt5";
/*     */   private static final String OPT_KINGDOM_CHAT = "opt6";
/*     */   private static final String OPT_KINGDOM_MESSAGE = "opt7";
/*     */   private static final String OPT_GLOBAL_KINGDOM_CHAT = "opt8";
/*     */   private static final String OPT_GLOBAL_KINGDOM_MESSAGE = "opt9";
/*     */   private static final String OPT_TRADE_CHANNEL = "opt10";
/*     */   private static final String OPT_TRADE_MESSAGE = "opt11";
/*     */   private static final String OPT_CA = "opt16";
/*     */   private static final String OPT_LOOT_ALLIANCE = "opt17";
/*     */   private static final String OPT_LOOT_VILLAGE = "opt18";
/*     */   private static final String OPT_LOOT_TRUSTED_FRIENDS = "opt19";
/*     */   private static final String OPT_SB_IDLEOFF = "opt20";
/*     */   private static final String OPT_PVP_BLOCK = "opt21";
/*     */   private static final String OPT_GV = "opt22";
/*     */   private static final String OPT_COOKING_AFFINITIES = "opt23";
/*     */   private static final String OPT_NO_WAGONER_CHAT = "opt24";
/*     */   private static final String OPT_SEE_PLAYER_TITLES = "opt25";
/*     */   private static final String OPT_SEE_VILLAGE_TITLES = "opt26";
/*     */   private static final String OPT_SHOW_OWN_VILLAGE_TITLES = "opt27";
/*     */   private static final String OPT_HIDE_MY_PVE_DEATHS = "opt28";
/*     */   private static final String OPT_IGNORE_PVE_DEATHS_TAB = "opt29";
/*     */   
/*     */   public PlayerProfileQuestion(Creature aResponder) {
/*  71 */     super(aResponder, aResponder.getName() + " profile", "Profile maintenance", 106, aResponder.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  81 */     setAnswer(aAnswer);
/*  82 */     if (this.type == 0) {
/*     */       
/*  84 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  87 */     if (this.type == 106) {
/*     */       
/*  89 */       boolean opt = Boolean.parseBoolean(aAnswer.getProperty("opt1"));
/*  90 */       checkFlag(1, opt, false, "incoming PMs");
/*  91 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt2"));
/*  92 */       checkFlag(2, opt, true, "cross kingdom PMs");
/*  93 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt3"));
/*  94 */       checkFlag(3, opt, true, "cross server PMs");
/*  95 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt4"));
/*  96 */       checkFlag(4, opt, false, "override for friend PMs");
/*  97 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt5"));
/*  98 */       checkFlag(7, opt, false, "equip options in menu");
/*  99 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt6"));
/* 100 */       checkFlag(29, opt, false, "kingdom chat");
/* 101 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt8"));
/* 102 */       checkFlag(30, opt, false, "global kingdom chat");
/* 103 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt10"));
/* 104 */       checkFlag(31, opt, false, "trade channel");
/* 105 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt7"));
/* 106 */       checkFlag(35, opt, false, "kingdom start message");
/* 107 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt9"));
/* 108 */       checkFlag(36, opt, false, "global kingdom start message");
/* 109 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt11"));
/* 110 */       checkFlag(37, opt, false, "trade start message");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 119 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt16"));
/* 120 */       boolean seeCA = ((Player)getResponder()).seesPlayerAssistantWindow();
/* 121 */       if (seeCA != opt)
/*     */       {
/* 123 */         if (opt) {
/*     */ 
/*     */           
/* 126 */           ((Player)getResponder()).togglePlayerAssistantWindow(true);
/* 127 */           getResponder().getCommunicator().sendNormalServerMessage("You have switched on CA Help.");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 132 */           ((Player)getResponder()).togglePlayerAssistantWindow(false);
/* 133 */           getResponder().getCommunicator().sendNormalServerMessage("You have switched off CA Help.");
/*     */         } 
/*     */       }
/*     */       
/* 137 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt17"));
/* 138 */       checkFlag(32, opt, false, "alliance looting of your corpse");
/* 139 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt18"));
/* 140 */       checkFlag(33, opt, false, "village looting of your corpse");
/* 141 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt19"));
/* 142 */       checkFlag(34, opt, false, "trusted friends looting of your corpse");
/* 143 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt20"));
/* 144 */       checkFlag(43, opt, false, "auto-freezing sleep bonus after " + 
/* 145 */           Server.getTimeFor(600000L) + " of inactivity");
/* 146 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt21"));
/* 147 */       checkFlag(44, opt, false, "blocking PvP travel");
/* 148 */       boolean sendextra = Boolean.parseBoolean(aAnswer.getProperty("sendextra"));
/* 149 */       ((Player)getResponder()).setSendExtraBytes(sendextra);
/*     */       
/* 151 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt22"));
/* 152 */       boolean seeGV = ((Player)getResponder()).seesGVHelpWindow();
/* 153 */       if (seeGV != opt)
/*     */       {
/* 155 */         if (opt) {
/*     */ 
/*     */           
/* 158 */           ((Player)getResponder()).toggleGVHelpWindow(true);
/* 159 */           getResponder().getCommunicator().sendNormalServerMessage("You have switched on GV Help.");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 164 */           ((Player)getResponder()).toggleGVHelpWindow(false);
/* 165 */           getResponder().getCommunicator().sendNormalServerMessage("You have switched off GV Help.");
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 170 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt23"));
/* 171 */       checkFlag(53, opt, true, "fixed cooking affinities");
/*     */       
/* 173 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt24"));
/* 174 */       checkFlag(54, opt, false, "hearing wagoner chat");
/*     */ 
/*     */       
/* 177 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt25"));
/* 178 */       checkFlag(56, opt, false, "showing other player titles");
/* 179 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt26"));
/* 180 */       checkFlag(57, opt, false, "showing other player village title");
/* 181 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt27"));
/* 182 */       checkFlag(58, opt, false, "showing your village title");
/*     */ 
/*     */       
/* 185 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt28"));
/* 186 */       checkFlag(59, opt, false, "participating in the PvE server death tabs");
/* 187 */       opt = Boolean.parseBoolean(aAnswer.getProperty("opt29"));
/* 188 */       checkFlag(60, opt, false, "viewing the Deaths tab on PvE");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkFlag(int oldFlag, boolean newFlag, boolean state, String what) {
/* 198 */     if (oldFlag == 44 && getResponder().getVehicle() != -10L && 
/* 199 */       !getResponder().isVehicleCommander() && !((Player)getResponder()).isBlockingPvP()) {
/*     */       
/*     */       try {
/*     */         
/* 203 */         Item bItem = Items.getItem(getResponder().getVehicle());
/* 204 */         if (bItem.isBoat()) {
/*     */           
/* 206 */           getResponder().getCommunicator()
/* 207 */             .sendNormalServerMessage("You cannot block PvP travel while embarked as a passenger on a boat.");
/*     */           
/*     */           return;
/*     */         } 
/* 211 */       } catch (NoSuchItemException noSuchItemException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     if (getResponder().hasFlag(oldFlag) != newFlag) {
/*     */       
/* 219 */       getResponder().setFlag(oldFlag, newFlag);
/* 220 */       String oo = (newFlag == state) ? "on " : "off ";
/* 221 */       getResponder().getCommunicator().sendNormalServerMessage("You have switched " + oo + what + ".");
/*     */       
/* 223 */       if (!newFlag)
/*     */       {
/* 225 */         switch (oldFlag) {
/*     */           
/*     */           case 29:
/* 228 */             Players.getInstance().sendStartKingdomChat((Player)getResponder());
/*     */             break;
/*     */           case 30:
/* 231 */             Players.getInstance().sendStartGlobalKingdomChat((Player)getResponder());
/*     */             break;
/*     */           case 31:
/* 234 */             Players.getInstance().sendStartGlobalTradeChannel((Player)getResponder());
/*     */             break;
/*     */         } 
/*     */       
/*     */       }
/* 239 */       if (oldFlag == 43 && ((Player)getResponder()).isSBIdleOffEnabled())
/*     */       {
/* 241 */         ((Player)getResponder()).resetInactivity(true);
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
/* 254 */     StringBuilder buf = new StringBuilder();
/* 255 */     buf.append(getBmlHeader());
/*     */     
/* 257 */     buf.append("text{text=\"\"}");
/* 258 */     buf.append("text{type=\"bold\";text=\"Player Chat settings\"}");
/* 259 */     buf.append("table{rows=\"5\";cols=\"3\";");
/* 260 */     buf.append("label{text=\"1. Incoming PMs\"};radio{group=\"opt1\";id=\"false\";selected=\"" + (
/* 261 */         !getResponder().hasFlag(1) ? 1 : 0) + "\";text=\"Allow\"};radio{group=\"" + "opt1" + "\";id=\"true\";selected=\"" + 
/* 262 */         getResponder().hasFlag(1) + "\";text=\"Disallow\"};");
/* 263 */     buf.append("label{text=\"2. Cross-Kingdoms\"};radio{group=\"opt2\";id=\"false\";selected=\"" + (
/* 264 */         !getResponder().hasFlag(2) ? 1 : 0) + "\";text=\"Same Only\"};radio{group=\"" + "opt2" + "\";id=\"true\";selected=\"" + 
/* 265 */         getResponder().hasFlag(2) + "\";text=\"Any\"};");
/* 266 */     buf.append("label{text=\"3. Cross-Servers\"};radio{group=\"opt3\";id=\"false\";selected=\"" + (
/* 267 */         !getResponder().hasFlag(3) ? 1 : 0) + "\";text=\"Local Only\"};radio{group=\"" + "opt3" + "\";id=\"true\";selected=\"" + 
/* 268 */         getResponder().hasFlag(3) + "\";text=\"Any\"};");
/* 269 */     buf.append("label{text=\"4. Friends-Override\"};radio{group=\"opt4\";id=\"false\";selected=\"" + (
/* 270 */         !getResponder().hasFlag(4) ? 1 : 0) + "\";text=\"Always\"};radio{group=\"" + "opt4" + "\";id=\"true\";selected=\"" + 
/* 271 */         getResponder().hasFlag(4) + "\";text=\"Never\"};");
/* 272 */     buf.append("}");
/* 273 */     buf.append("text{text=\"\"}");
/* 274 */     buf.append("text{type=\"bold\";text=\"Notes\"};");
/* 275 */     buf.append("text{text=\" GMs will still be able to PM you.\"}");
/* 276 */     buf.append("text{text=\" You will still be able to initiate a PM, so long as the person accepts them.\"}");
/* 277 */     buf.append("text{text=\"\"}");
/*     */     
/* 279 */     buf.append("text{type=\"bold\";text=\"Misc settings (show is default)\"}");
/* 280 */     if (Servers.localServer.isChallengeOrEpicServer()) {
/* 281 */       buf.append("table{rows=\"1\";cols=\"3\";");
/*     */     } else {
/* 283 */       buf.append("table{rows=\"2\";cols=\"3\";");
/* 284 */     }  buf.append("label{text=\"1. Equip options\"};radio{group=\"opt5\";id=\"false\";selected=\"" + (
/* 285 */         !getResponder().hasFlag(7) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt5" + "\";id=\"true\";selected=\"" + 
/* 286 */         getResponder().hasFlag(7) + "\";text=\"Hide\"};");
/* 287 */     if (Server.getInstance().isPS() || !Servers.localServer.isChallengeOrEpicServer())
/*     */     {
/* 289 */       buf.append("label{text=\"2. Block PvP Crossing\"};radio{group=\"opt21\";id=\"false\";selected=\"" + (
/* 290 */           !getResponder().hasFlag(44) ? 1 : 0) + "\";text=\"On\"};radio{group=\"" + "opt21" + "\";id=\"true\";selected=\"" + 
/* 291 */           getResponder().hasFlag(44) + "\";text=\"Off\"};");
/*     */     }
/* 293 */     buf.append("}");
/*     */     
/* 295 */     buf.append("text{text=\"\"}");
/* 296 */     buf.append("text{type=\"bold\";text=\"Other Chat Options (show is default)\"}");
/* 297 */     buf.append("table{rows=\"2\";cols=\"6\";");
/* 298 */     buf.append("label{text=\"1. Kingdom Chat\"};radio{group=\"opt6\";id=\"false\";selected=\"" + (
/* 299 */         !getResponder().hasFlag(29) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt6" + "\";id=\"true\";selected=\"" + 
/* 300 */         getResponder().hasFlag(29) + "\";text=\"Hide\"};label{text=\" | Startup Message\"}radio{group=\"" + "opt7" + "\";id=\"false\";selected=\"" + (
/*     */         
/* 302 */         !getResponder().hasFlag(35) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt7" + "\";id=\"true\";selected=\"" + 
/* 303 */         getResponder().hasFlag(35) + "\";text=\"Hide\"};");
/* 304 */     buf.append("label{text=\"2. Global Kingdom Chat\"};radio{group=\"opt8\";id=\"false\";selected=\"" + (
/* 305 */         !getResponder().hasFlag(30) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt8" + "\";id=\"true\";selected=\"" + 
/* 306 */         getResponder().hasFlag(30) + "\";text=\"Hide\"};label{text=\" | Startup Message\"}radio{group=\"" + "opt9" + "\";id=\"false\";selected=\"" + (
/*     */         
/* 308 */         !getResponder().hasFlag(36) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt9" + "\";id=\"true\";selected=\"" + 
/* 309 */         getResponder().hasFlag(36) + "\";text=\"Hide\"};");
/* 310 */     buf.append("label{text=\"3. Global Trade Channel\"};radio{group=\"opt10\";id=\"false\";selected=\"" + (
/* 311 */         !getResponder().hasFlag(31) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt10" + "\";id=\"true\";selected=\"" + 
/* 312 */         getResponder().hasFlag(31) + "\";text=\"Hide\"};label{text=\" | Startup Message\"}radio{group=\"" + "opt11" + "\";id=\"false\";selected=\"" + (
/*     */         
/* 314 */         !getResponder().hasFlag(37) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt11" + "\";id=\"true\";selected=\"" + 
/* 315 */         getResponder().hasFlag(37) + "\";text=\"Hide\"};");
/* 316 */     boolean seeCA = ((Player)getResponder()).seesPlayerAssistantWindow();
/* 317 */     buf.append("label{text=\"4. CA Help\"};radio{group=\"opt16\";id=\"true\";selected=\"" + seeCA + "\";text=\"Show\"};radio{group=\"" + "opt16" + "\";id=\"false\";selected=\"" + (!seeCA ? 1 : 0) + "\";text=\"Hide\"};label{text=\"\"}label{text=\"\"};label{text=\"\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     if (((Player)getResponder()).maySeeGVHelpWindow()) {
/*     */       
/* 325 */       boolean seeGVHelp = ((Player)getResponder()).seesGVHelpWindow();
/* 326 */       buf.append("label{text=\"5. GV Help\"};radio{group=\"opt22\";id=\"true\";selected=\"" + seeGVHelp + "\";text=\"Show\"};radio{group=\"" + "opt22" + "\";id=\"false\";selected=\"" + (!seeGVHelp ? 1 : 0) + "\";text=\"Hide\"};label{text=\"\"}label{text=\"\"};label{text=\"\"};");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 333 */     buf.append("}");
/* 334 */     buf.append("text{text=\"\"}");
/*     */     
/* 336 */     buf.append("text{type=\"bold\";text=\"Your Corpse Lootability (allow is default)\"}");
/* 337 */     buf.append("table{rows=\"3\";cols=\"3\";");
/* 338 */     buf.append("label{text=\"1. Alliance\"};radio{group=\"opt17\";id=\"false\";selected=\"" + (
/* 339 */         !getResponder().hasFlag(32) ? 1 : 0) + "\";text=\"Allow\"};radio{group=\"" + "opt17" + "\";id=\"true\";selected=\"" + 
/* 340 */         getResponder().hasFlag(32) + "\";text=\"Deny\"};");
/* 341 */     buf.append("label{text=\"2. Village\"};radio{group=\"opt18\";id=\"false\";selected=\"" + (
/* 342 */         !getResponder().hasFlag(33) ? 1 : 0) + "\";text=\"Allow\"};radio{group=\"" + "opt18" + "\";id=\"true\";selected=\"" + 
/* 343 */         getResponder().hasFlag(33) + "\";text=\"Deny\"};");
/* 344 */     buf.append("label{text=\"3. Trusted Friends\"};radio{group=\"opt19\";id=\"false\";selected=\"" + (
/* 345 */         !getResponder().hasFlag(34) ? 1 : 0) + "\";text=\"Allow\"};radio{group=\"" + "opt19" + "\";id=\"true\";selected=\"" + 
/* 346 */         getResponder().hasFlag(34) + "\";text=\"Deny\"};");
/* 347 */     buf.append("}");
/* 348 */     buf.append("text{text=\"\"}");
/* 349 */     buf.append("text{type=\"bold\";text=\"Other Settings\"}");
/* 350 */     buf.append("table{rows=\"2\";cols=\"3\";");
/* 351 */     buf.append("label{text=\"1. Sleep Bonus Idle Timeout (" + Server.getTimeFor(600000L) + ")\"};radio{group=\"" + "opt20" + "\";id=\"false\";selected=\"" + (
/* 352 */         !getResponder().hasFlag(43) ? 1 : 0) + "\";text=\"On\"};radio{group=\"" + "opt20" + "\";id=\"true\";selected=\"" + 
/* 353 */         getResponder().hasFlag(43) + "\";text=\"Off\"};");
/* 354 */     if (!getResponder().hasFlag(53)) {
/*     */       
/* 356 */       buf.append("label{text=\"2. Fix Cooking Affinities\"};radio{group=\"opt23\";id=\"true\";selected=\"" + 
/* 357 */           getResponder().hasFlag(53) + "\";text=\"On\"};radio{group=\"" + "opt23" + "\";id=\"false\";selected=\"" + (
/* 358 */           !getResponder().hasFlag(53) ? 1 : 0) + "\";text=\"Off\"};");
/*     */     }
/*     */     else {
/*     */       
/* 362 */       buf.append("label{text=\"2. Fix Cooking Affinities\"};radio{group=\"opt23\";id=\"true\";selected=\"" + 
/* 363 */           getResponder().hasFlag(53) + "\";text=\"On\"};label{text=\"\"};");
/*     */     } 
/*     */     
/* 366 */     buf.append("label{text=\"3. Wagoner Chat\"};radio{group=\"opt24\";id=\"false\";selected=\"" + (
/* 367 */         !getResponder().hasFlag(54) ? 1 : 0) + "\";text=\"Hear\"};radio{group=\"" + "opt24" + "\";id=\"true\";selected=\"" + 
/* 368 */         getResponder().hasFlag(54) + "\";text=\"Ignore\"};");
/*     */     
/* 370 */     buf.append("label{text=\"4. Show Other Player Titles\"};radio{group=\"opt25\";id=\"false\";selected=\"" + (
/* 371 */         !getResponder().hasFlag(56) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt25" + "\";id=\"true\";selected=\"" + 
/* 372 */         getResponder().hasFlag(56) + "\";text=\"Hide\"};");
/*     */     
/* 374 */     buf.append("label{text=\"5. Show Other Player Villages\"};radio{group=\"opt26\";id=\"false\";selected=\"" + (
/* 375 */         !getResponder().hasFlag(57) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt26" + "\";id=\"true\";selected=\"" + 
/* 376 */         getResponder().hasFlag(57) + "\";text=\"Hide\"};");
/*     */     
/* 378 */     buf.append("label{text=\"6. Show Own Village Titles\"};radio{group=\"opt27\";id=\"false\";selected=\"" + (
/* 379 */         !getResponder().hasFlag(58) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt27" + "\";id=\"true\";selected=\"" + 
/* 380 */         getResponder().hasFlag(58) + "\";text=\"Hide\"};");
/*     */     
/* 382 */     buf.append("label{text=\"7. Participate in PvE Deaths tab on Dying\"};radio{group=\"opt28\";id=\"false\";selected=\"" + (
/* 383 */         !getResponder().hasFlag(59) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt28" + "\";id=\"true\";selected=\"" + 
/* 384 */         getResponder().hasFlag(59) + "\";text=\"Hide\"};");
/*     */     
/* 386 */     buf.append("label{text=\"8. Ignore the PvE Deaths Tab\"};radio{group=\"opt29\";id=\"false\";selected=\"" + (
/* 387 */         !getResponder().hasFlag(60) ? 1 : 0) + "\";text=\"Show\"};radio{group=\"" + "opt29" + "\";id=\"true\";selected=\"" + 
/* 388 */         getResponder().hasFlag(60) + "\";text=\"Hide\"};");
/*     */     
/* 390 */     buf.append("}");
/* 391 */     buf.append("text{type=\"italic\";color=\"237,28,36\";text=\"NOTE: Cooking affinities option cannot be turned off once enabled.\"};");
/* 392 */     buf.append("text{text=\"\"}");
/*     */     
/* 394 */     if (Servers.isThisATestServer()) {
/*     */       
/* 396 */       buf.append("text{type=\"italic\";text=\"Following are only shown on test server.\"}");
/* 397 */       buf.append("text{type=\"bold\";text=\"Player Session flags\"}");
/* 398 */       if (getResponder().getPower() >= 2)
/* 399 */         buf.append("checkbox{id=\"signedin\";text=\"Signed In\";selected=\"" + ((Player)getResponder()).isSignedIn() + "\"}"); 
/* 400 */       buf.append("checkbox{id=\"afk\";text=\"AFK\";selected=\"" + ((Player)getResponder()).isAFK() + "\"}");
/* 401 */       buf.append("checkbox{id=\"sendextra\";text=\"show Forage/Bot\";selected=\"" + ((Player)getResponder()).isSendExtraBytes() + "\"}");
/* 402 */       buf.append("text{text=\"\"}");
/*     */     } 
/*     */     
/* 405 */     buf.append(createAnswerButton2());
/* 406 */     getResponder().getCommunicator().sendBml(420, 540, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PlayerProfileQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
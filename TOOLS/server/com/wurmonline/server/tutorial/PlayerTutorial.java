/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.Titles;
/*     */ import com.wurmonline.server.tutorial.stages.FishingStage;
/*     */ import com.wurmonline.server.tutorial.stages.WelcomeStage;
/*     */ import java.util.HashMap;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerTutorial
/*     */ {
/*  21 */   private static HashMap<Long, PlayerTutorial> currentTutorials = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PlayerTutorial getTutorialForPlayer(long wurmId, boolean create) {
/*  32 */     if (!currentTutorials.containsKey(Long.valueOf(wurmId)) && create) {
/*  33 */       addTutorial(wurmId, new PlayerTutorial(wurmId));
/*     */     }
/*  35 */     return currentTutorials.get(Long.valueOf(wurmId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addTutorial(long wurmId, PlayerTutorial tutorial) {
/*  40 */     currentTutorials.put(Long.valueOf(wurmId), tutorial);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeTutorial(long wurmId) {
/*  45 */     currentTutorials.remove(Long.valueOf(wurmId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void firePlayerTrigger(long wurmId, PlayerTrigger trigger) {
/*  50 */     if (!currentTutorials.containsKey(Long.valueOf(wurmId))) {
/*     */       return;
/*     */     }
/*  53 */     if (!((PlayerTutorial)currentTutorials.get(Long.valueOf(wurmId))).getCurrentStage().awaitingTrigger(trigger)) {
/*     */       return;
/*     */     }
/*  56 */     ((PlayerTutorial)currentTutorials.get(Long.valueOf(wurmId))).getCurrentStage().clearTrigger();
/*  57 */     ((PlayerTutorial)currentTutorials.get(Long.valueOf(wurmId))).sendUpdateStageBML();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean doesTutorialExist(long wurmId) {
/*  62 */     return currentTutorials.containsKey(Long.valueOf(wurmId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void endTutorial(Player p) {
/*  67 */     if (doesTutorialExist(p.getWurmId())) {
/*  68 */       (getTutorialForPlayer(p.getWurmId(), false)).customMethods.tutorialSkipped(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void startTutorialCommand(Player player, String message) {
/*  74 */     getTutorialForPlayer(player.getWurmId(), true).sendCurrentStageBML();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void skipTutorialCommand(Player player, String message) {
/*  79 */     if (doesTutorialExist(player.getWurmId())) {
/*     */       
/*  81 */       player.getCommunicator().sendCloseWindow(getTutorialForPlayer(player.getWurmId(), false).getCurrentStage().getWindowId());
/*  82 */       endTutorial(player);
/*  83 */       removeTutorial(player.getWurmId());
/*     */     } else {
/*     */       
/*  86 */       player.getCommunicator().sendNormalServerMessage("You do not currently have an active tutorial. Nothing to skip.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void testTutorialCommand(Player player, String message) {
/*  91 */     StringTokenizer st = new StringTokenizer(message);
/*  92 */     st.nextToken();
/*     */     
/*  94 */     if (doesTutorialExist(player.getWurmId())) {
/*     */       
/*  96 */       player.getCommunicator().sendCloseWindow(getTutorialForPlayer(player.getWurmId(), false).getCurrentStage().getWindowId());
/*  97 */       endTutorial(player);
/*  98 */       removeTutorial(player.getWurmId());
/*     */ 
/*     */     
/*     */     }
/* 102 */     else if (st.hasMoreTokens()) {
/*     */       
/* 104 */       String tutorialType = st.nextToken();
/* 105 */       int fastForward = 0;
/* 106 */       if (st.hasMoreTokens())
/* 107 */         fastForward = Integer.parseInt(st.nextToken()); 
/* 108 */       switch (tutorialType.toLowerCase()) {
/*     */         
/*     */         case "fishing":
/* 111 */           startNewTutorial(player, TutorialType.FISHING, fastForward);
/*     */           return;
/*     */       } 
/* 114 */       startNewTutorial(player, TutorialType.DEFAULT, fastForward);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 119 */       getTutorialForPlayer(player.getWurmId(), true).sendCurrentStageBML();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void startNewTutorial(Player p, TutorialType t, int fastForward) {
/*     */     PlayerTutorial newTut;
/* 125 */     if (doesTutorialExist(p.getWurmId())) {
/*     */       
/* 127 */       p.getCommunicator().sendCloseWindow(getTutorialForPlayer(p.getWurmId(), false).getCurrentStage().getWindowId());
/* 128 */       if (!p.hasFlag(42))
/* 129 */         endTutorial(p); 
/* 130 */       removeTutorial(p.getWurmId());
/*     */     } 
/*     */     
/* 133 */     switch (t) {
/*     */       
/*     */       case FISHING:
/* 136 */         newTut = new PlayerTutorial(p.getWurmId(), (TutorialStage)new FishingStage(p.getWurmId()), new TutorialMethods()
/*     */             {
/*     */ 
/*     */               
/*     */               public void tutorialCompleted(Player p)
/*     */               {
/* 142 */                 p.getCommunicator().sendNormalServerMessage("Fishing tutorial completed. You can restart the tutorial through your journal.");
/*     */               }
/*     */ 
/*     */ 
/*     */               
/*     */               public void tutorialSkipped(Player p) {
/* 148 */                 p.getCommunicator().sendNormalServerMessage("Fishing tutorial closed. You can restart the tutorial through your journal.");
/*     */               }
/*     */             });
/*     */         
/* 152 */         addTutorial(p.getWurmId(), newTut);
/*     */         
/* 154 */         if (fastForward > 0) {
/*     */           
/* 156 */           for (int i = 0; i < fastForward; ) {
/*     */             
/* 158 */             if (newTut.skipCurrentStage()) {
/*     */               i++; continue;
/*     */             } 
/* 161 */             p.getCommunicator().sendNormalServerMessage("Cannot skip to stage " + fastForward + " as there are only " + i + " stages in this tutorial.");
/*     */           } 
/*     */           
/* 164 */           newTut.getCurrentStage().setForceOpened(true);
/*     */         } 
/* 166 */         newTut.sendCurrentStageBML();
/*     */         return;
/*     */     } 
/* 169 */     PlayerTutorial newBasicTut = getTutorialForPlayer(p.getWurmId(), true);
/*     */     
/* 171 */     if (fastForward > 0) {
/*     */       
/* 173 */       for (int i = 0; i < fastForward; ) {
/*     */         
/* 175 */         if (newBasicTut.skipCurrentStage()) {
/*     */           i++; continue;
/*     */         } 
/* 178 */         p.getCommunicator().sendNormalServerMessage("Cannot skip to stage " + fastForward + " as there are only " + i + " stages in this tutorial.");
/*     */       } 
/*     */       
/* 181 */       newBasicTut.getCurrentStage().setForceOpened(true);
/*     */     } 
/* 183 */     newBasicTut.sendCurrentStageBML();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendTutorialList(Player p) {
/* 192 */     p.getCommunicator().sendPersonalJournalTutorial((byte)-1, (byte)TutorialType.DEFAULT.ordinal(), "New Player Tutorial");
/* 193 */     p.getCommunicator().sendPersonalJournalTutorial((byte)-1, (byte)TutorialType.FISHING.ordinal(), "Fishing Tutorial");
/*     */     int i;
/* 195 */     for (i = 0; i < DEFAULT_STAGE_NAMES.length; i++) {
/* 196 */       p.getCommunicator().sendPersonalJournalTutorial((byte)TutorialType.DEFAULT.ordinal(), (byte)i, DEFAULT_STAGE_NAMES[i]);
/*     */     }
/* 198 */     for (i = 0; i < FISHING_STAGE_NAMES.length; i++)
/* 199 */       p.getCommunicator().sendPersonalJournalTutorial((byte)TutorialType.FISHING.ordinal(), (byte)i, FISHING_STAGE_NAMES[i]); 
/*     */   }
/*     */   
/*     */   public enum TutorialType
/*     */   {
/* 204 */     DEFAULT,
/* 205 */     FISHING;
/*     */   }
/*     */   
/*     */   public enum PlayerTrigger
/*     */   {
/* 210 */     NONE,
/* 211 */     MOVED_PLAYER_VIEW,
/* 212 */     MOVED_PLAYER,
/* 213 */     ENABLED_CLIMBING,
/* 214 */     DISABLED_CLIMBING,
/* 215 */     ENABLED_INVENTORY,
/* 216 */     DISABLED_INVENTORY,
/* 217 */     ACTIVATED_ITEM,
/* 218 */     EQUIPPED_ITEM,
/* 219 */     ENABLED_CHARACTER,
/* 220 */     PLACED_ITEM,
/* 221 */     TAKEN_ITEM,
/* 222 */     DIG_TILE,
/* 223 */     CUT_TREE,
/* 224 */     FELL_TREE,
/* 225 */     CREATE_LOG,
/* 226 */     ENABLED_CREATION,
/* 227 */     CREATE_KINDLING,
/* 228 */     CREATE_CAMPFIRE,
/* 229 */     MINE_IRON;
/*     */   }
/*     */   
/*     */   public static abstract class TutorialMethods
/*     */   {
/*     */     public abstract void tutorialCompleted(Player param1Player);
/*     */     
/*     */     public abstract void tutorialSkipped(Player param1Player);
/*     */   }
/*     */   
/* 239 */   public static final String[] DEFAULT_STAGE_NAMES = new String[] { "Welcome to Wurm", "Looking Around", "Movement", "Inventory & Items", "Starting Out", "Activating & Equipping", "World Interaction", "Dropping & Taking", "Terraforming", "Woodcutting", "Creating Items", "Mining", "Skills", "Combat", "Keybinds, Rules & Settings" };
/*     */ 
/*     */ 
/*     */   
/* 243 */   public static final String[] FISHING_STAGE_NAMES = new String[] { "Intro, Net & Spear Fishing", "Rod & Pole Fishing", "Final Tips" };
/*     */   
/*     */   private long playerId;
/*     */   
/*     */   private TutorialStage currentStage;
/*     */   
/*     */   private final TutorialStage initialStage;
/*     */   
/*     */   private final TutorialMethods customMethods;
/*     */ 
/*     */   
/*     */   public PlayerTutorial(long playerId) {
/* 255 */     this(playerId, (TutorialStage)new WelcomeStage(playerId), new TutorialMethods()
/*     */         {
/*     */ 
/*     */           
/*     */           public void tutorialCompleted(Player p)
/*     */           {
/* 261 */             p.getCommunicator().sendOpenWindow((short)41, true);
/* 262 */             p.addTitle(Titles.Title.Educated);
/*     */             
/* 264 */             if (!p.hasFlag(42)) {
/*     */               
/* 266 */               p.setFlag(42, true);
/* 267 */               p.getSaveFile().addToSleep(3600);
/* 268 */               p.getCommunicator().sendNormalServerMessage("For completing the tutorial you are awarded 1 hour of sleep bonus!", (byte)2);
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void tutorialSkipped(Player p) {
/* 275 */             p.getCommunicator().sendNormalServerMessage("Tutorial closed. You can restart the tutorial through your journal.");
/*     */             
/* 277 */             p.getCommunicator().sendOpenWindow((short)9, false);
/* 278 */             p.getCommunicator().sendOpenWindow((short)5, false);
/* 279 */             p.getCommunicator().sendOpenWindow((short)1, false);
/* 280 */             p.getCommunicator().sendOpenWindow((short)3, false);
/* 281 */             p.getCommunicator().sendOpenWindow((short)11, false);
/* 282 */             p.getCommunicator().sendOpenWindow((short)4, false);
/*     */             
/* 284 */             p.getCommunicator().sendOpenWindow((short)6, false);
/* 285 */             p.getCommunicator().sendOpenWindow((short)7, false);
/* 286 */             p.getCommunicator().sendOpenWindow((short)2, false);
/* 287 */             p.getCommunicator().sendOpenWindow((short)12, false);
/* 288 */             p.getCommunicator().sendOpenWindow((short)13, false);
/* 289 */             p.getCommunicator().sendOpenWindow((short)41, false);
/*     */             
/* 291 */             p.getCommunicator().sendToggleAllQuickbarBtns(true);
/*     */             
/* 293 */             p.addTitle(Titles.Title.Educated);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerTutorial(long playerId, TutorialStage initialStage, TutorialMethods customMethods) {
/* 301 */     this.playerId = playerId;
/* 302 */     this.initialStage = initialStage;
/* 303 */     this.currentStage = initialStage;
/* 304 */     this.customMethods = customMethods;
/*     */     
/* 306 */     addTutorial(playerId, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/* 311 */     return this.playerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public TutorialStage getCurrentStage() {
/* 316 */     return this.currentStage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean skipCurrentStage() {
/* 321 */     if (this.currentStage.getNextStage() != null) {
/*     */       
/* 323 */       this.currentStage = this.currentStage.getNextStage();
/* 324 */       return true;
/*     */     } 
/*     */     
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean increaseCurrentStage() {
/* 332 */     if (!getCurrentStage().increaseSubStage()) {
/*     */       
/* 334 */       if (this.currentStage.isForceOpened()) {
/* 335 */         this.currentStage = null;
/*     */       } else {
/* 337 */         this.currentStage = this.currentStage.getNextStage();
/*     */       } 
/* 339 */       if (this.currentStage == null) {
/*     */         
/* 341 */         removeTutorial(getPlayerId());
/* 342 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 346 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void restart() {
/* 351 */     this.currentStage = this.initialStage;
/* 352 */     this.currentStage.resetSubStage();
/* 353 */     sendCurrentStageBML();
/*     */   }
/*     */ 
/*     */   
/*     */   public void decreaseCurrentStage() {
/* 358 */     if (getCurrentStage().decreaseSubStage()) {
/*     */       
/* 360 */       this.currentStage = this.currentStage.getLastStage();
/* 361 */       this.currentStage.toLastSubStage();
/*     */       
/* 363 */       if (this.currentStage == null)
/*     */       {
/* 365 */         this.currentStage = this.initialStage;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendCurrentStageBML() {
/*     */     try {
/* 374 */       Player p = Players.getInstance().getPlayer(getPlayerId());
/* 375 */       p.getCommunicator().sendBml(getCurrentStage().getWindowId(), 320, 450, 0.0F, 0.5F, false, p.hasFlag(42), 
/* 376 */           getCurrentStage().getCurrentBML(), 255, 255, 255, "Tutorial");
/*     */     }
/* 378 */     catch (NoSuchPlayerException e) {
/*     */ 
/*     */       
/* 381 */       removeTutorial(getPlayerId());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendUpdateStageBML() {
/*     */     try {
/* 389 */       Player p = Players.getInstance().getPlayer(getPlayerId());
/* 390 */       p.getCommunicator().sendBml(getCurrentStage().getWindowId(), 320, 450, 0.0F, 0.5F, false, p.hasFlag(42), 
/* 391 */           getCurrentStage().getUpdateBML(), 255, 255, 255, "Tutorial");
/*     */     }
/* 393 */     catch (NoSuchPlayerException e) {
/*     */ 
/*     */       
/* 396 */       removeTutorial(getPlayerId());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateReceived(Properties answers) {
/* 402 */     String skipTutorial = answers.getProperty("close");
/* 403 */     if (skipTutorial != null && skipTutorial.equals("true")) {
/*     */ 
/*     */       
/*     */       try {
/* 407 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 408 */         endTutorial(p);
/*     */       }
/* 410 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */       
/* 413 */       removeTutorial(getPlayerId());
/*     */     } 
/*     */     
/* 416 */     String nextStage = answers.getProperty("next");
/* 417 */     String skipStage = answers.getProperty("skip");
/* 418 */     if ((nextStage != null && nextStage.equals("true")) || (skipStage != null && skipStage.equals("true"))) {
/*     */       
/* 420 */       boolean wasForced = getCurrentStage().isForceOpened();
/* 421 */       if (increaseCurrentStage()) {
/*     */         
/* 423 */         sendCurrentStageBML();
/* 424 */         if (getCurrentStage().shouldSkipTrigger()) {
/* 425 */           sendUpdateStageBML();
/*     */         }
/* 427 */       } else if (!wasForced) {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 432 */           Player p = Players.getInstance().getPlayer(this.playerId);
/* 433 */           this.customMethods.tutorialCompleted(p);
/*     */         }
/* 435 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 442 */     String lastStage = answers.getProperty("back");
/* 443 */     if (lastStage != null && lastStage.equals("true")) {
/*     */       
/* 445 */       decreaseCurrentStage();
/* 446 */       sendCurrentStageBML();
/* 447 */       getCurrentStage().clearTrigger();
/* 448 */       sendUpdateStageBML();
/*     */     } 
/*     */     
/* 451 */     String restartTut = answers.getProperty("restart");
/* 452 */     if (restartTut != null && restartTut.equals("true"))
/*     */     {
/* 454 */       getTutorialForPlayer(getPlayerId(), false).restart();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\PlayerTutorial.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
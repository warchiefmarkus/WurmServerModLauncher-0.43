/*     */ package com.wurmonline.server.tutorial.stages;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.tutorial.TutorialStage;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FinalStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 1500;
/*     */   
/*     */   public short getWindowId() {
/*  20 */     return (short)(1500 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public FinalStage(long playerId) {
/*  25 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  31 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  37 */     return new CombatStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  43 */     this.subStages.add(new KeybindSubStage(getPlayerId()));
/*  44 */     this.subStages.add(new QuickbindSubStage(getPlayerId()));
/*  45 */     this.subStages.add(new WurmpediaSubStage(getPlayerId()));
/*  46 */     this.subStages.add(new SettingsSubStage(getPlayerId()));
/*  47 */     this.subStages.add(new GoodLuckSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class KeybindSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public KeybindSubStage(long playerId) {
/*  55 */       super(FinalStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  61 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  62 */           BMLBuilder.createCenteredNode(
/*  63 */             BMLBuilder.createVertArrayNode(false)
/*  64 */             .addText("")
/*  65 */             .addHeader("Keybindings", Color.LIGHT_GRAY)), null, 
/*     */           
/*  67 */           BMLBuilder.createVertArrayNode(false)
/*  68 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  69 */           .addText("\r\nMost actions in Wurm have a corresponding keybind that you can bind to any key you would like. Keybindings can be changed from the game Settings.\r\n\r\nOpen the Settings window from the main menu, then choose the Keybinds tab to bind a key.\r\n\r\nFor example, if you bind the Open action to the F key you can then hover over any container in game with your mouse and press F to open it instead of right clicking and selecting Open.\r\n\r\nFor a full list of possible Keybinds, check the Settings via the main menu or visit the Keybinds page on the Wurmpedia.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  76 */           .addText(""), null, 
/*     */           
/*  78 */           BMLBuilder.createLeftAlignedNode(
/*  79 */             BMLBuilder.createHorizArrayNode(false)
/*  80 */             .addButton("back", "Back", 80, 20, true)
/*  81 */             .addText("", null, null, null, 35, 0)
/*  82 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  85 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class QuickbindSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public QuickbindSubStage(long playerId) {
/*  95 */       super(FinalStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 101 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 102 */           BMLBuilder.createCenteredNode(
/* 103 */             BMLBuilder.createVertArrayNode(false)
/* 104 */             .addText("")
/* 105 */             .addHeader("Keybindings", Color.LIGHT_GRAY)), null, 
/*     */           
/* 107 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 108 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 109 */             .addText("\r\nYou can also quickly bind a key to an action from the right click menu.\r\n\r\nSimply right click to bring up the menu with the action you want to bind then hover your mouse over that action and hold down a key or key combination for a second. When the key is bound, it will showin the right click menu as a reminder.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 114 */             .addImage("image.tutorial.quickbind", 300, 150)
/* 115 */             .addText("\r\nPlease note that you can only quickbind actions that are normally possible to bind from the game Settings.\r\n\r\n", null, null, null, 300, 400)
/*     */             
/* 117 */             .addText("").toString()), null, 
/*     */           
/* 119 */           BMLBuilder.createLeftAlignedNode(
/* 120 */             BMLBuilder.createHorizArrayNode(false)
/* 121 */             .addButton("back", "Back", 80, 20, true)
/* 122 */             .addText("", null, null, null, 35, 0)
/* 123 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 126 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class WurmpediaSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public WurmpediaSubStage(long playerId) {
/* 136 */       super(FinalStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 142 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 143 */           BMLBuilder.createCenteredNode(
/* 144 */             BMLBuilder.createVertArrayNode(false)
/* 145 */             .addText("")
/* 146 */             .addHeader("Wurmpedia", Color.LIGHT_GRAY)), null, 
/*     */           
/* 148 */           BMLBuilder.createVertArrayNode(false)
/* 149 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 150 */           .addText("\r\nThe Wurmpedia is your main resource for information on Skills, Items, Actions and Crafting.\r\n\r\nYou can access the Wurmpedia from in-game by pressing [$bind:'toggle wikisearch'$] and typing a keyword before hitting enter.\r\n\r\nYou can also right click anything in game, and select the Wurmpedia action to open the Wurmpedia window and search for that target.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 155 */           .addText(""), null, 
/*     */           
/* 157 */           BMLBuilder.createLeftAlignedNode(
/* 158 */             BMLBuilder.createHorizArrayNode(false)
/* 159 */             .addButton("back", "Back", 80, 20, true)
/* 160 */             .addText("", null, null, null, 35, 0)
/* 161 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 164 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class SettingsSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public SettingsSubStage(long playerId) {
/* 174 */       super(FinalStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 180 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 181 */           BMLBuilder.createCenteredNode(
/* 182 */             BMLBuilder.createVertArrayNode(false)
/* 183 */             .addText("")
/* 184 */             .addHeader("Profile & Settings", Color.LIGHT_GRAY)), null, 
/*     */           
/* 186 */           BMLBuilder.createVertArrayNode(false)
/* 187 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 188 */           .addText("\r\nA large number of settings are available in the game client to help your game run more smoothly. You can find these by pressing [$bind:STOP_OR_MAIN_MENU$] and clicking Settings. Additional windows and information can be found under HUD Settings in the Main Menu.\r\n\r\nAs well as these settings, there are a number of character specific settings you can change from your character profile. You can find your profile by pressing [$bind:TOGGLE_CHARACTER$] then rightclicking the body icon in the bottom right hand corner and choosing Manage -> Profile.\r\n\r\nAlternatively you can right click the Inventory line in your inventory window then choose Manage -> Profile.\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 196 */           .addText(""), null, 
/*     */           
/* 198 */           BMLBuilder.createLeftAlignedNode(
/* 199 */             BMLBuilder.createHorizArrayNode(false)
/* 200 */             .addButton("back", "Back", 80, 20, true)
/* 201 */             .addText("", null, null, null, 35, 0)
/* 202 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 205 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class GoodLuckSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public GoodLuckSubStage(long playerId) {
/* 215 */       super(FinalStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 221 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 222 */           BMLBuilder.createCenteredNode(
/* 223 */             BMLBuilder.createVertArrayNode(false)
/* 224 */             .addText("")
/* 225 */             .addHeader("Good Luck!", Color.LIGHT_GRAY)), null, 
/*     */           
/* 227 */           BMLBuilder.createVertArrayNode(false)
/* 228 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 229 */           .addText("\r\nYou've made it to the end of the tutorial. You should now know enough about Wurm to get yourself started.\r\n\r\nIf you are looking for some goals or things to do, take a look at the Personal Journal window for some suggestions.\r\n\r\nYou can play through this tutorial again at any point by typing /tutorial into chat.\r\n\r\nIf you are ever going through this tutorial again in the future and want to close it, you can type /skipTutorial into chat at any point during the tutorial to end it.\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 236 */           .addText(""), null, 
/*     */           
/* 238 */           BMLBuilder.createLeftAlignedNode(
/* 239 */             BMLBuilder.createHorizArrayNode(false)
/* 240 */             .addButton("back", "Back", 80, 20, true)
/* 241 */             .addText("", null, null, null, 35, 0)
/* 242 */             .addButton("next", "End Tutorial", 80, 20, true)
/* 243 */             .addText("", null, null, null, 35, 0)
/* 244 */             .addButton("restart", "Restart Tutorial", " ", "Are you sure you want to restart the tutorial from the beginning?", null, false, 80, 20, true)));
/*     */ 
/*     */ 
/*     */       
/* 248 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 256 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 257 */         p.getCommunicator().sendOpenWindow((short)7, false);
/* 258 */         p.getCommunicator().sendOpenWindow((short)2, false);
/* 259 */         p.getCommunicator().sendToggleQuickbarBtn((short)2002, true);
/* 260 */         p.getCommunicator().sendToggleQuickbarBtn((short)2003, true);
/* 261 */         p.getCommunicator().sendToggleQuickbarBtn((short)2004, true);
/* 262 */         p.getCommunicator().sendToggleQuickbarBtn((short)2010, true);
/* 263 */         p.getCommunicator().sendToggleQuickbarBtn((short)2013, true);
/*     */       }
/* 265 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\FinalStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
/*     */ public class WorldStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 700;
/*     */   
/*     */   public short getWindowId() {
/*  20 */     return (short)(700 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldStage(long playerId) {
/*  25 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  31 */     return new DropTakeStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  37 */     return new EquipmentStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  43 */     this.subStages.add(new InteractSubStage(getPlayerId()));
/*  44 */     this.subStages.add(new SelectSubStage(getPlayerId()));
/*  45 */     this.subStages.add(new KeybindSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class InteractSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public InteractSubStage(long playerId) {
/*  53 */       super(WorldStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  59 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  60 */           BMLBuilder.createCenteredNode(
/*  61 */             BMLBuilder.createVertArrayNode(false)
/*  62 */             .addText("")
/*  63 */             .addHeader("World Interaction", Color.LIGHT_GRAY)), null, 
/*     */           
/*  65 */           BMLBuilder.createVertArrayNode(false)
/*  66 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  67 */           .addText("\r\nA large amount of game interaction can be completed through the menus that appear when right clicking something while hovering over it.\r\n\r\nThis will show various actions you can take with the hovered target, and may show extra options depending on what item you have activated at the time.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  72 */           .addImage("image.tutorial.rightclick", 300, 150)
/*  73 */           .addText(""), null, 
/*     */           
/*  75 */           BMLBuilder.createLeftAlignedNode(
/*  76 */             BMLBuilder.createHorizArrayNode(false)
/*  77 */             .addButton("back", "Back", 80, 20, true)
/*  78 */             .addText("", null, null, null, 35, 0)
/*  79 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  82 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class SelectSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public SelectSubStage(long playerId) {
/*  92 */       super(WorldStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  98 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  99 */           BMLBuilder.createCenteredNode(
/* 100 */             BMLBuilder.createVertArrayNode(false)
/* 101 */             .addText("")
/* 102 */             .addHeader("World Interaction", Color.LIGHT_GRAY)), null, 
/*     */           
/* 104 */           BMLBuilder.createVertArrayNode(false)
/* 105 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 106 */           .addText("\r\nYou can also left click on anything in the world to select it and have it show in the Select bar. This will then show the primary actions you can take for the selected target with your current activated item as buttons on the bottom of the Select bar.\r\n\r\nIn addition you can right click the selected target in this bar to get the full right click action menu as well.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 111 */           .addText(""), null, 
/*     */           
/* 113 */           BMLBuilder.createLeftAlignedNode(
/* 114 */             BMLBuilder.createHorizArrayNode(false)
/* 115 */             .addButton("back", "Back", 80, 20, true)
/* 116 */             .addText("", null, null, null, 35, 0)
/* 117 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 120 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 128 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 129 */         p.getCommunicator().sendOpenWindow((short)12, true);
/*     */       }
/* 131 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class KeybindSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public KeybindSubStage(long playerId) {
/* 144 */       super(WorldStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 150 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 151 */           BMLBuilder.createCenteredNode(
/* 152 */             BMLBuilder.createVertArrayNode(false)
/* 153 */             .addText("")
/* 154 */             .addHeader("World Interaction", Color.LIGHT_GRAY)), null, 
/*     */           
/* 156 */           BMLBuilder.createVertArrayNode(false)
/* 157 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 158 */           .addText("\r\nMost actions can also be bound to a key of your choice, which will attempt the action when the key is pressed while hovering the mouse over a certain target. You can set your keybinds from the settings window via the main menu.\r\n\r\nIn addition, double clicking most targets in the world will Examine that item and give you some extra information about them. Be careful when double clicking another creature though, as that will cause you to attempt to attack it.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 165 */           .addText(""), null, 
/*     */           
/* 167 */           BMLBuilder.createLeftAlignedNode(
/* 168 */             BMLBuilder.createHorizArrayNode(false)
/* 169 */             .addButton("back", "Back", 80, 20, true)
/* 170 */             .addText("", null, null, null, 35, 0)
/* 171 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 174 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 182 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 183 */         p.getCommunicator().sendToggleQuickbarBtn((short)2014, true);
/*     */       }
/* 185 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\WorldStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
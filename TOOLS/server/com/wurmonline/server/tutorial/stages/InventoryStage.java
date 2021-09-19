/*     */ package com.wurmonline.server.tutorial.stages;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*     */ import com.wurmonline.server.tutorial.TutorialStage;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InventoryStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 400;
/*     */   
/*     */   public short getWindowId() {
/*  21 */     return (short)(400 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public InventoryStage(long playerId) {
/*  26 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  32 */     return new StartingStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  38 */     return new MovementStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  44 */     this.subStages.add(new InventorySubStage(getPlayerId()));
/*  45 */     this.subStages.add(new MoveItemsSubStage(getPlayerId()));
/*  46 */     this.subStages.add(new QualitySubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class InventorySubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public InventorySubStage(long playerId) {
/*  54 */       super(InventoryStage.this, playerId);
/*  55 */       setNextTrigger(PlayerTutorial.PlayerTrigger.ENABLED_INVENTORY);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  61 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  62 */           BMLBuilder.createCenteredNode(
/*  63 */             BMLBuilder.createVertArrayNode(false)
/*  64 */             .addText("")
/*  65 */             .addHeader("Inventory & Items", Color.LIGHT_GRAY)), null, 
/*     */           
/*  67 */           BMLBuilder.createVertArrayNode(false)
/*  68 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  69 */           .addText("\r\nPress [$bind:'toggle inventory'$] or click on the inventory button to open your inventory.\r\n\r\n", null, null, null, 300, 400)
/*     */           
/*  71 */           .addText(""), null, 
/*     */           
/*  73 */           BMLBuilder.createLeftAlignedNode(
/*  74 */             BMLBuilder.createHorizArrayNode(false)
/*  75 */             .addButton("back", "Back", 80, 20, true)
/*  76 */             .addText("", null, null, null, 35, 0)
/*  77 */             .addButton("next", "Waiting...", 80, 20, false)
/*  78 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/*  81 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/*  89 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/*  90 */         p.getCommunicator().sendToggleQuickbarBtn((short)2007, true);
/*     */       }
/*  92 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class MoveItemsSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public MoveItemsSubStage(long playerId) {
/* 105 */       super(InventoryStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 111 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 112 */           BMLBuilder.createCenteredNode(
/* 113 */             BMLBuilder.createVertArrayNode(false)
/* 114 */             .addText("")
/* 115 */             .addHeader("Inventory & Items", Color.LIGHT_GRAY)), null, 
/*     */           
/* 117 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 118 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 119 */             .addText("\r\nIn this window you can select items, move them around, and view any containers and items inside containers that are in your inventory.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 122 */             .addImage("image.tutorial.moveitems", 300, 150)
/* 123 */             .addText("\r\nWhen opening any container item in the world (such as a chest), you will have a similar window show up for that item where you can interact with any items that are stored inside.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */             
/* 126 */             .addText("").toString()), null, 
/*     */           
/* 128 */           BMLBuilder.createLeftAlignedNode(
/* 129 */             BMLBuilder.createHorizArrayNode(false)
/* 130 */             .addButton("back", "Back", 80, 20, true)
/* 131 */             .addText("", null, null, null, 35, 0)
/* 132 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 135 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class QualitySubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public QualitySubStage(long playerId) {
/* 145 */       super(InventoryStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 151 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 152 */           BMLBuilder.createCenteredNode(
/* 153 */             BMLBuilder.createVertArrayNode(false)
/* 154 */             .addText("")
/* 155 */             .addHeader("Inventory & Items", Color.LIGHT_GRAY)), null, 
/*     */           
/* 157 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 158 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 159 */             .addText("\r\nEach item in Wurm has an associated Quality Level (QL) and Damage, both between 0 and 100.\r\n\r\n", null, null, null, 300, 400)
/*     */             
/* 161 */             .addImage("image.tutorial.qldmg", 300, 150)
/* 162 */             .addText("\r\nAn item with a higher QL will be more effective than a lower QL item and will generally last longer.\r\n\r\nAs an item is used it will gain Damage which lowers the effective QL of the item, and will cause it to be destroyed if the Damage ever reaches 100 without being repaired.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */             
/* 166 */             .addText("").toString()), null, 
/*     */           
/* 168 */           BMLBuilder.createLeftAlignedNode(
/* 169 */             BMLBuilder.createHorizArrayNode(false)
/* 170 */             .addButton("back", "Back", 80, 20, true)
/* 171 */             .addText("", null, null, null, 35, 0)
/* 172 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 175 */       this.bmlString = builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\InventoryStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
/*     */ public class EquipmentStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 600;
/*     */   
/*     */   public short getWindowId() {
/*  21 */     return (short)(600 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public EquipmentStage(long playerId) {
/*  26 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  32 */     return new WorldStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  38 */     return new StartingStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  44 */     this.subStages.add(new ActivateSubStage(getPlayerId()));
/*  45 */     this.subStages.add(new CharacterSubStage(getPlayerId()));
/*  46 */     this.subStages.add(new EquipSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class ActivateSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public ActivateSubStage(long playerId) {
/*  54 */       super(EquipmentStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  60 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  61 */           BMLBuilder.createCenteredNode(
/*  62 */             BMLBuilder.createVertArrayNode(false)
/*  63 */             .addText("")
/*  64 */             .addHeader("Activating & Equipping", Color.LIGHT_GRAY)), null, 
/*     */           
/*  66 */           BMLBuilder.createVertArrayNode(false)
/*  67 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  68 */           .addText("\r\nMost actions that you can complete in Wurm require an active item in order to do the action. This may mean you need to activate a shovel to dig some dirt, or a hatchet in order to chop down a tree.\r\n\r\nTo activate an item, simply double click it while it is inside your inventory. The item will turn green, and will show at the bottom of your inventory as your currently active item.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  73 */           .addImage("image.tutorial.activate", 300, 150)
/*  74 */           .addText(""), null, 
/*     */           
/*  76 */           BMLBuilder.createLeftAlignedNode(
/*  77 */             BMLBuilder.createHorizArrayNode(false)
/*  78 */             .addButton("back", "Back", 80, 20, true)
/*  79 */             .addText("", null, null, null, 35, 0)
/*  80 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/*  83 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/*  91 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/*  92 */         p.getCommunicator().sendOpenWindow((short)20, false);
/*     */       }
/*  94 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class CharacterSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public CharacterSubStage(long playerId) {
/* 107 */       super(EquipmentStage.this, playerId);
/* 108 */       setNextTrigger(PlayerTutorial.PlayerTrigger.ENABLED_CHARACTER);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 114 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 115 */           BMLBuilder.createCenteredNode(
/* 116 */             BMLBuilder.createVertArrayNode(false)
/* 117 */             .addText("")
/* 118 */             .addHeader("Activating & Equipping", Color.LIGHT_GRAY)), null, 
/*     */           
/* 120 */           BMLBuilder.createVertArrayNode(false)
/* 121 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 122 */           .addText("\r\nEquipping items is different than activating them, and is generally only used for Armour and Weapons.\r\n\r\nYou can still activate items that are equipped, but items equipped in your hands are not considered activated by default.\r\n\r\nPress [$bind:TOGGLE_CHARACTER$] or click the character button to open the Character Window.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 127 */           .addImage("image.tutorial.character", 300, 150)
/* 128 */           .addText(""), null, 
/*     */           
/* 130 */           BMLBuilder.createLeftAlignedNode(
/* 131 */             BMLBuilder.createHorizArrayNode(false)
/* 132 */             .addButton("back", "Back", 80, 20, true)
/* 133 */             .addText("", null, null, null, 35, 0)
/* 134 */             .addButton("next", "Waiting...", 80, 20, false)
/* 135 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 138 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 146 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 147 */         p.getCommunicator().sendToggleQuickbarBtn((short)2009, true);
/*     */       }
/* 149 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class EquipSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public EquipSubStage(long playerId) {
/* 162 */       super(EquipmentStage.this, playerId);
/* 163 */       setNextTrigger(PlayerTutorial.PlayerTrigger.EQUIPPED_ITEM);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 169 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 170 */           BMLBuilder.createCenteredNode(
/* 171 */             BMLBuilder.createVertArrayNode(false)
/* 172 */             .addText("")
/* 173 */             .addHeader("Activating & Equipping", Color.LIGHT_GRAY)), null, 
/*     */           
/* 175 */           BMLBuilder.createScrollPanelNode(true, false).addString(BMLBuilder.createVertArrayNode(false)
/* 176 */             .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 177 */             .addText("\r\nClick and drag any equippable items such as armour pieces to your character in this window to have it equip.\r\n\r\n", null, null, null, 300, 400)
/*     */             
/* 179 */             .addImage("image.tutorial.equipping", 300, 150)
/* 180 */             .addText("\r\nTo unequip an item, drag it from the slot it is equipped in to your character in this window again, and it will move the item back to your inventory.\r\n\r\nYou can also right click an item in your inventory and select the Equip action.\r\n\r\nEquip an item now to continue.", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 185 */             .addText("").toString()), null, 
/*     */           
/* 187 */           BMLBuilder.createLeftAlignedNode(
/* 188 */             BMLBuilder.createHorizArrayNode(false)
/* 189 */             .addButton("back", "Back", 80, 20, true)
/* 190 */             .addText("", null, null, null, 35, 0)
/* 191 */             .addButton("next", "Waiting...", 80, 20, false)
/* 192 */             .maybeAddSkipButton()));
/*     */ 
/*     */       
/* 195 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 203 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 204 */         p.getCommunicator().sendOpenWindow((short)20, false);
/* 205 */         p.getCommunicator().sendOpenWindow((short)26, false);
/*     */       }
/* 207 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class ToolbeltSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public ToolbeltSubStage(long playerId) {
/* 220 */       super(EquipmentStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 226 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 227 */           BMLBuilder.createCenteredNode(
/* 228 */             BMLBuilder.createVertArrayNode(false)
/* 229 */             .addText("")
/* 230 */             .addHeader("Activating & Equipping", Color.LIGHT_GRAY)), null, 
/*     */           
/* 232 */           BMLBuilder.createVertArrayNode(false)
/* 233 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 234 */           .addText("\r\nIf you have a toolbelt equipped, it will show up on screen now. You can use your toolbelt as a quick access to any items you have in your inventory or equipped on your body. Simply drag the item to any toolbelt slot to add it to your toolbelt.\r\n\r\nOnce on your toolbelt, a single click on an item icon on your toolbelt will activate it allowing you to use it in actions. You can also bind keys to your toolbelt slots for quick access without having to click on the icons.\r\n\r\nThe amount of slots shown for your toolbelt depends on the quality of your toolbelt, with one additional slot per 10QL.Clicking the arrows on the toolbelt will switch your toolbelt presets which you can save and load by right clicking on the toolbelt at any time.\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 243 */           .addText(""), null, 
/*     */           
/* 245 */           BMLBuilder.createLeftAlignedNode(
/* 246 */             BMLBuilder.createHorizArrayNode(false)
/* 247 */             .addButton("back", "Back", 80, 20, true)
/* 248 */             .addText("", null, null, null, 35, 0)
/* 249 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 252 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 260 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 261 */         p.getCommunicator().sendOpenWindow((short)7, true);
/*     */       }
/* 263 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\EquipmentStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
/*     */ public class StartingStage
/*     */   extends TutorialStage
/*     */ {
/*     */   private static final short WINDOW_ID = 500;
/*     */   
/*     */   public short getWindowId() {
/*  19 */     return (short)(500 + getCurrentSubStage());
/*     */   }
/*     */ 
/*     */   
/*     */   public StartingStage(long playerId) {
/*  24 */     super(playerId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getNextStage() {
/*  30 */     return new EquipmentStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TutorialStage getLastStage() {
/*  36 */     return new InventoryStage(getPlayerId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSubStages() {
/*  42 */     this.subStages.add(new StartingInvSubStage(getPlayerId()));
/*  43 */     this.subStages.add(new DeathSubStage(getPlayerId()));
/*  44 */     this.subStages.add(new ChatSubStage(getPlayerId()));
/*  45 */     this.subStages.add(new EventSubStage(getPlayerId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public class StartingInvSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public StartingInvSubStage(long playerId) {
/*  53 */       super(StartingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/*  59 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/*  60 */           BMLBuilder.createCenteredNode(
/*  61 */             BMLBuilder.createVertArrayNode(false)
/*  62 */             .addText("")
/*  63 */             .addHeader("Starting Out", Color.LIGHT_GRAY)), null, 
/*     */           
/*  65 */           BMLBuilder.createVertArrayNode(false)
/*  66 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/*  67 */           .addText("\r\nInside your inventory you will have some items to help you get started in Wurm. This includes some basic armour, weapons and tools.\r\n\r\nThese starter items cannot be improved, and will stay with you through death.\r\n\r\nIn order to get better versions of these items, you will need to create and improve them yourself or have another player help you.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  72 */           .addText(""), null, 
/*     */           
/*  74 */           BMLBuilder.createLeftAlignedNode(
/*  75 */             BMLBuilder.createHorizArrayNode(false)
/*  76 */             .addButton("back", "Back", 80, 20, true)
/*  77 */             .addText("", null, null, null, 35, 0)
/*  78 */             .addButton("next", "Next", 80, 20, true)));
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
/*  90 */         p.getCommunicator().sendOpenWindow((short)20, false);
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
/*     */   public class DeathSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public DeathSubStage(long playerId) {
/* 105 */       super(StartingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 111 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 112 */           BMLBuilder.createCenteredNode(
/* 113 */             BMLBuilder.createVertArrayNode(false)
/* 114 */             .addText("")
/* 115 */             .addHeader("Starting Out", Color.LIGHT_GRAY)), null, 
/*     */           
/* 117 */           BMLBuilder.createVertArrayNode(false)
/* 118 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 119 */           .addText("\r\nUpon death, your body will become a corpse at the location you died, and will contain any items you were carrying that aren't starter items. If you want to get those items back, you'll need to travel back to your corpse and retrieve your items.\r\n\r\nAs a part of your starting items you will have a tent. Dropping this tent on the ground will let you respawn there when you die. As well as your tent location, you'll be able to choose to spawn at a village if you are a member of one, any allied villages, or a starter town.\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 126 */           .addText(""), null, 
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 143 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 144 */         p.getCommunicator().sendOpenWindow((short)20, false);
/*     */       }
/* 146 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class ChatSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public ChatSubStage(long playerId) {
/* 159 */       super(StartingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 165 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 166 */           BMLBuilder.createCenteredNode(
/* 167 */             BMLBuilder.createVertArrayNode(false)
/* 168 */             .addText("")
/* 169 */             .addHeader("Starting Out", Color.LIGHT_GRAY)), null, 
/*     */           
/* 171 */           BMLBuilder.createVertArrayNode(false)
/* 172 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 173 */           .addText("\r\nThis is the chat window where you can talk to other players in the game. You can use the CA-Help chat to ask questions about game mechanics and how to do something.\r\n\r\nLocal chat will send messages to any players nearby you in the world and your kingdom chat (e.g. Freedom) will send messages to everyone on your current server in that kingdom.\r\n\r\nAny chat starting with GL will send messages to all connected servers instead of just your server.\r\n\r\nThis window also contains the Trade tab, where you can find other players selling or buying goods.\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 180 */           .addText(""), null, 
/*     */           
/* 182 */           BMLBuilder.createLeftAlignedNode(
/* 183 */             BMLBuilder.createHorizArrayNode(false)
/* 184 */             .addButton("back", "Back", 80, 20, true)
/* 185 */             .addText("", null, null, null, 35, 0)
/* 186 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 189 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 197 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 198 */         p.getCommunicator().sendOpenWindow((short)1, true);
/*     */       }
/* 200 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class EventSubStage
/*     */     extends TutorialStage.TutorialSubStage
/*     */   {
/*     */     public EventSubStage(long playerId) {
/* 213 */       super(StartingStage.this, playerId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void buildBMLString() {
/* 219 */       BMLBuilder builder = BMLBuilder.createBMLBorderPanel(
/* 220 */           BMLBuilder.createCenteredNode(
/* 221 */             BMLBuilder.createVertArrayNode(false)
/* 222 */             .addText("")
/* 223 */             .addHeader("Starting Out", Color.LIGHT_GRAY)), null, 
/*     */           
/* 225 */           BMLBuilder.createVertArrayNode(false)
/* 226 */           .addPassthrough("tutorialid", Long.toString(getPlayerId()))
/* 227 */           .addText("\r\nThe event window will show you any relevant information to the action you're doing, as well as showing you a progress bar underneath with the current action you're completing and the time remaining.\r\n\r\n", null, null, null, 300, 400)
/*     */ 
/*     */ 
/*     */           
/* 231 */           .addText(""), null, 
/*     */           
/* 233 */           BMLBuilder.createLeftAlignedNode(
/* 234 */             BMLBuilder.createHorizArrayNode(false)
/* 235 */             .addButton("back", "Back", 80, 20, true)
/* 236 */             .addText("", null, null, null, 35, 0)
/* 237 */             .addButton("next", "Next", 80, 20, true)));
/*     */ 
/*     */       
/* 240 */       this.bmlString = builder.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {
/*     */       try {
/* 248 */         Player p = Players.getInstance().getPlayer(getPlayerId());
/* 249 */         p.getCommunicator().sendOpenWindow((short)3, true);
/*     */       }
/* 251 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\stages\StartingStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
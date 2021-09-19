/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class OreQuestion
/*     */   extends Question
/*     */ {
/*     */   private final int tilex;
/*     */   private final int tiley;
/*     */   private int numtiles;
/*     */   private static final String NOCHANGE = "No change";
/*     */   private final Item rod;
/*  42 */   private final Map<Integer, Integer> oretiles = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OreQuestion(Creature _responder, int _tilex, int _tiley, Item _rod) {
/*  53 */     super(_responder, "Selecting Ore Type", "Which ore type should this tile have?", 82, _rod.getWurmId());
/*  54 */     this.tilex = _tilex;
/*  55 */     this.tiley = _tiley;
/*  56 */     this.rod = _rod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  67 */     if (this.type == 82) {
/*     */       
/*  69 */       if (this.rod.deleted) {
/*     */         
/*  71 */         getResponder().getCommunicator().sendNormalServerMessage("The rod has been destroyed.");
/*     */         return;
/*     */       } 
/*  74 */       if (this.rod.getOwnerId() != getResponder().getWurmId()) {
/*     */         
/*  76 */         getResponder().getCommunicator().sendNormalServerMessage("You need to be in possession of the rod.");
/*     */         return;
/*     */       } 
/*  79 */       if (this.rod.getWurmId() == this.target) {
/*     */         
/*  81 */         String d1 = answers.getProperty("data1");
/*  82 */         if (d1 != null) {
/*     */           
/*  84 */           int index = 0;
/*     */           
/*     */           try {
/*  87 */             index = Integer.parseInt(d1);
/*     */           }
/*  89 */           catch (Exception ex) {
/*     */             
/*  91 */             getResponder().getCommunicator().sendNormalServerMessage(d1 + " was selected - Error. No change.");
/*     */             return;
/*     */           } 
/*  94 */           if (index == this.numtiles) {
/*     */             
/*  96 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to change nothing.");
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 101 */           if (getResponder().getLogger() != null)
/* 102 */             getResponder().getLogger().log(Level.INFO, 
/* 103 */                 getResponder() + " setting ore " + this.tilex + ", " + this.tiley + " to : " + d1); 
/* 104 */           Integer newType = this.oretiles.get(Integer.valueOf(index));
/* 105 */           if (newType == null) {
/*     */             
/* 107 */             getResponder().getCommunicator().sendNormalServerMessage("Invalid choice " + index + ".");
/*     */             return;
/*     */           } 
/* 110 */           byte nt = (byte)newType.intValue();
/* 111 */           if (getResponder().isOnSurface()) {
/*     */             
/* 113 */             getResponder().getCommunicator().sendNormalServerMessage("Please enter the cave.");
/*     */             return;
/*     */           } 
/* 116 */           if (Tiles.decodeType(Server.caveMesh.getTile(this.tilex, this.tiley)) == nt) {
/*     */             
/* 118 */             getResponder().getCommunicator().sendNormalServerMessage("The terrain is already of that type.");
/*     */             return;
/*     */           } 
/* 121 */           if (!Tiles.isOreCave(nt)) {
/*     */             
/* 123 */             getResponder().getCommunicator().sendNormalServerMessage("The rod must set to ore of some kind.");
/*     */             return;
/*     */           } 
/* 126 */           if (Tiles.decodeType(Server.caveMesh.getTile(this.tilex, this.tiley)) == Tiles.Tile.TILE_CAVE_WALL.id) {
/*     */             
/* 128 */             Server.caveMesh.setTile(this.tilex, this.tiley, Tiles.encode(Tiles.decodeHeight(Server.caveMesh.getTile(this.tilex, this.tiley)), nt, 
/* 129 */                   Tiles.decodeData(Server.caveMesh.getTile(this.tilex, this.tiley))));
/* 130 */             Players.getInstance().sendChangedTile(this.tilex, this.tiley, false, true);
/* 131 */             getResponder().getCommunicator().sendNormalServerMessage("The wall changes to purest " + 
/* 132 */                 (Tiles.getTile(nt)).tiledesc + " and the rod vanishes!");
/* 133 */             Server.setCaveResource(this.tilex, this.tiley, 10000);
/* 134 */             Zones.setMiningState(this.tilex, this.tiley, (byte)-1, false);
/* 135 */             Items.destroyItem(this.rod.getWurmId());
/*     */           } else {
/*     */             
/* 138 */             getResponder().getCommunicator().sendNormalServerMessage("You need to use this on a cave wall.");
/*     */           } 
/*     */         } 
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
/* 153 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */ 
/*     */     
/* 156 */     buf.append("harray{label{text='Tile type'}dropdown{id='data1';options=\"");
/*     */     
/* 158 */     Tiles.Tile[] tiles = Tiles.Tile.getTiles();
/* 159 */     for (int x = 0; x < tiles.length; x++) {
/*     */       
/* 161 */       if (tiles[x] != null)
/*     */       {
/* 163 */         if (Tiles.isOreCave((tiles[x]).id))
/*     */         {
/* 165 */           if ((tiles[x]).id != Tiles.Tile.TILE_CAVE_WALL_ORE_ADAMANTINE.id && (tiles[x]).id != Tiles.Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL.id) {
/*     */ 
/*     */             
/* 168 */             this.oretiles.put(Integer.valueOf(this.numtiles), Integer.valueOf(x));
/* 169 */             this.numtiles++;
/* 170 */             buf.append((tiles[x]).tiledesc);
/* 171 */             buf.append(",");
/*     */           } 
/*     */         }
/*     */       }
/*     */     } 
/* 176 */     buf.append("No change");
/* 177 */     buf.append("\"}}");
/* 178 */     buf.append(createAnswerButton2());
/* 179 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\OreQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
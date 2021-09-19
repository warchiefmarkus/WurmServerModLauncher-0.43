/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.Terraforming;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ public final class TerrainQuestion
/*     */   extends Question
/*     */ {
/*     */   private Item wand;
/*     */   private final int tilex;
/*     */   private final int tiley;
/*  46 */   private List<Integer> tilelist = null;
/*  47 */   private int category = 0;
/*  48 */   private String filter = "*";
/*     */   
/*     */   private static final String NOCHANGE = "No change";
/*     */ 
/*     */   
/*     */   public TerrainQuestion(Creature _responder, Item wand, int _tilex, int _tiley) {
/*  54 */     super(_responder, "Changing the terrain", "Which tile type should this tile have?", 52, _responder.getWurmId());
/*  55 */     this.wand = wand;
/*  56 */     this.tilex = _tilex;
/*  57 */     this.tiley = _tiley;
/*     */   }
/*     */ 
/*     */   
/*     */   public TerrainQuestion(Creature _responder, Item wand, int _tilex, int _tiley, int category, String filter) {
/*  62 */     this(_responder, wand, _tilex, _tiley);
/*  63 */     this.category = category;
/*  64 */     this.filter = filter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  70 */     if (this.type == 52)
/*     */     {
/*  72 */       if (getResponder().getWurmId() == this.target) {
/*     */         
/*  74 */         String cat = answers.getProperty("cat");
/*  75 */         int catn = Integer.valueOf(cat).intValue();
/*  76 */         String newFilter = answers.getProperty("filtertext");
/*  77 */         if (newFilter == null || newFilter.length() == 0) {
/*  78 */           newFilter = "*";
/*     */         }
/*  80 */         String buttonChangeCat = answers.getProperty("changecat");
/*  81 */         String buttonFilter = answers.getProperty("filterme");
/*     */         
/*  83 */         if ((buttonChangeCat != null && buttonChangeCat.equals("true")) || (buttonFilter != null && buttonFilter.equals("true"))) {
/*     */           
/*  85 */           TerrainQuestion tq = new TerrainQuestion(getResponder(), this.wand, this.tilex, this.tiley, catn, newFilter);
/*  86 */           tq.sendQuestion();
/*     */           return;
/*     */         } 
/*  89 */         boolean auto = false;
/*  90 */         int sizex = 0;
/*  91 */         int sizey = 0;
/*  92 */         String autoStr = answers.getProperty("auto");
/*  93 */         if (autoStr != null && autoStr.equals("true")) {
/*     */           
/*  95 */           auto = true;
/*  96 */           String sizexStr = answers.getProperty("sizex", "0");
/*  97 */           String sizeyStr = answers.getProperty("sizey", "0");
/*  98 */           if (sizexStr.length() > 0 && sizeyStr.length() > 0) {
/*     */             
/* 100 */             sizex = Integer.valueOf(sizexStr).intValue();
/* 101 */             sizey = Integer.valueOf(sizeyStr).intValue();
/*     */           } 
/*     */         } 
/* 104 */         String d1 = answers.getProperty("data1");
/* 105 */         if (d1 != null) {
/*     */           
/* 107 */           int ttype = 0;
/*     */           
/*     */           try {
/* 110 */             ttype = ((Integer)this.tilelist.get(Integer.parseInt(d1))).intValue();
/*     */           }
/* 112 */           catch (Exception ex) {
/*     */             
/* 114 */             getResponder().getCommunicator().sendNormalServerMessage(d1 + " was selected - Error. No change.");
/*     */             return;
/*     */           } 
/* 117 */           if (ttype == 0) {
/*     */             
/* 119 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to change nothing.");
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 124 */           Tiles.Tile theTile = Tiles.getTile(ttype);
/* 125 */           if (getResponder().getLogger() != null)
/*     */           {
/* 127 */             getResponder().getLogger().log(Level.INFO, 
/* 128 */                 getResponder() + " changing tile " + this.tilex + ", " + this.tiley + " to : " + theTile.getDesc() + " (" + theTile.getId() + ")");
/*     */           }
/* 130 */           getResponder().getCommunicator().sendNormalServerMessage("Trying to change tile " + this.tilex + ", " + this.tiley + " to : " + theTile
/* 131 */               .getDesc() + " (" + theTile.getId() + ").");
/* 132 */           byte newType = (byte)ttype;
/* 133 */           if (auto) {
/*     */ 
/*     */             
/* 136 */             this.wand.setAuxData(newType);
/* 137 */             if (sizex > 0 && sizey > 0) {
/*     */               
/* 139 */               this.wand.setData1(sizex);
/* 140 */               this.wand.setData2(sizey);
/* 141 */               getResponder().getCommunicator().sendNormalServerMessage("wand setup for " + theTile
/* 142 */                   .getDesc() + " (" + theTile.getId() + ") and x,y of " + sizex + "," + sizey + ".");
/*     */             }
/*     */             else {
/*     */               
/* 146 */               getResponder().getCommunicator().sendNormalServerMessage("wand setup for " + theTile
/* 147 */                   .getDesc() + " (" + theTile.getId() + ").");
/*     */             } 
/* 149 */           }  if (getResponder().isOnSurface()) {
/*     */             
/* 151 */             if (Tiles.decodeType(Server.surfaceMesh.getTile(this.tilex, this.tiley)) == newType) {
/*     */               
/* 153 */               getResponder().getCommunicator().sendNormalServerMessage("The terrain is already of that type.");
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/* 159 */             if (theTile.isSolidCave() || newType == Tiles.Tile.TILE_CAVE.id || theTile
/*     */               
/* 161 */               .isReinforcedCave()) {
/*     */               
/* 163 */               getResponder().getCommunicator().sendNormalServerMessage("You cannot set the surface terrain to some sort of rock.");
/*     */               
/*     */               return;
/*     */             } 
/* 167 */             if (getResponder().getPower() < 5 && (newType == Tiles.Tile.TILE_ROCK.id || 
/*     */               
/* 169 */               Tiles.decodeType(Server.surfaceMesh.getTile(this.tilex, this.tiley)) == Tiles.Tile.TILE_ROCK.id || newType == Tiles.Tile.TILE_CLIFF.id || 
/*     */               
/* 171 */               Tiles.decodeType(Server.surfaceMesh.getTile(this.tilex, this.tiley)) == Tiles.Tile.TILE_CLIFF.id)) {
/*     */               
/* 173 */               getResponder()
/* 174 */                 .getCommunicator()
/* 175 */                 .sendNormalServerMessage("That would have impact on the rock layer, and is not allowed for now.");
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/* 181 */             if (newType == Tiles.Tile.TILE_ROCK.id) {
/*     */               
/* 183 */               Server.caveMesh
/* 184 */                 .setTile(this.tilex, this.tiley, 
/*     */ 
/*     */                   
/* 187 */                   Tiles.encode((short)-100, Tiles.Tile.TILE_CAVE_WALL.id, (byte)0));
/*     */               
/* 189 */               Server.rockMesh
/* 190 */                 .setTile(this.tilex, this.tiley, 
/*     */ 
/*     */                   
/* 193 */                   Tiles.encode(Tiles.decodeHeight(Server.surfaceMesh.getTile(this.tilex, this.tiley)), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 198 */             byte tileData = 0;
/* 199 */             if (Tiles.isBush(newType) || Tiles.isTree(newType))
/*     */             {
/* 201 */               tileData = 1; } 
/* 202 */             Server.setSurfaceTile(this.tilex, this.tiley, 
/* 203 */                 Tiles.decodeHeight(Server.surfaceMesh.getTile(this.tilex, this.tiley)), newType, tileData);
/*     */ 
/*     */             
/* 206 */             if (newType == Tiles.Tile.TILE_FIELD.id || newType == Tiles.Tile.TILE_FIELD2.id) {
/*     */               
/* 208 */               Server.setWorldResource(this.tilex, this.tiley, 0);
/*     */             } else {
/*     */               
/* 211 */               Server.setWorldResource(this.tilex, this.tiley, -1);
/* 212 */             }  Players.getInstance().sendChangedTile(this.tilex, this.tiley, true, true);
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */ 
/*     */           
/* 220 */           Terraforming.paintCaveTerrain((Player)getResponder(), newType, this.tilex, this.tiley);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 231 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 232 */     buf.append("harray{label{text=\"Category\"}dropdown{id=\"cat\";default=\"" + this.category + "\"options=\"All,Bushes,Cave,Minedoors,Normal,Paving,Surface,Trees\"}label{text=\" Filter:\"};input{maxchars=\"30\";id=\"filtertext\";text=\"" + this.filter + "\";onenter=\"filterme\"}label{text=\" \"};button{text=\"Update list\";id=\"filterme\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     buf.append("label{text=\"\"}");
/*     */     
/* 243 */     this.tilelist = new ArrayList<>();
/* 244 */     buf.append("harray{label{text='Tile type'}dropdown{id='data1';options=\"");
/* 245 */     Tiles.Tile[] tiles = Tiles.Tile.getTiles(this.category, this.filter);
/*     */     
/* 247 */     if (tiles.length == 0) {
/*     */ 
/*     */       
/* 250 */       this.tilelist.add(Integer.valueOf(0));
/* 251 */       buf.append("No change");
/*     */     }
/* 253 */     else if (tiles.length == 1) {
/*     */ 
/*     */       
/* 256 */       buf.append((tiles[0]).tiledesc + " (" + ((tiles[0]).id & 0xFF) + ")");
/* 257 */       this.tilelist.add(Integer.valueOf((tiles[0]).id));
/*     */     }
/*     */     else {
/*     */       
/* 261 */       this.tilelist.add(Integer.valueOf(0));
/* 262 */       buf.append("No change");
/*     */       
/* 264 */       Arrays.sort(tiles, new Comparator<Tiles.Tile>()
/*     */           {
/*     */             
/*     */             public int compare(Tiles.Tile param1, Tiles.Tile param2)
/*     */             {
/* 269 */               return param1.getDesc().compareTo(param2.getDesc());
/*     */             }
/*     */           });
/*     */       
/* 273 */       for (int x = 0; x < tiles.length; x++) {
/*     */         
/* 275 */         if (tiles[x] != null) {
/*     */           
/* 277 */           buf.append("," + (tiles[x]).tiledesc + " (" + ((tiles[x]).id & 0xFF) + ")");
/* 278 */           this.tilelist.add(Integer.valueOf((tiles[x]).id));
/*     */         } 
/*     */       } 
/*     */     } 
/* 282 */     buf.append("\"}}");
/* 283 */     buf.append("label{text=\"\"}");
/*     */     
/* 285 */     buf.append("checkbox{id=\"auto\";text=\"Check this if you want the aux byte set on the wand for paint terrain.\"};");
/* 286 */     buf.append("harray{label{text=\"You can also set the area to paint, max is 9 in either direction \"};input{id=\"sizex\";text=\"\";maxchars=\"1\"};label{text=\" \"};input{id=\"sizey\";text=\"\";maxchars=\"1\"};}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     buf.append("label{text=\"\"}");
/* 296 */     buf.append(createAnswerButton2());
/* 297 */     getResponder().getCommunicator().sendBml(370, 240, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TerrainQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.Crops;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TileDataQuestion
/*     */   extends Question
/*     */ {
/*     */   private final int tilex;
/*     */   private final int tiley;
/*     */   
/*     */   public TileDataQuestion(Creature aResponder, String aTitle, String aQuestion, int aTilex, int aTiley, long aTarget) {
/*  47 */     super(aResponder, aTitle, aQuestion, 35, aTarget);
/*  48 */     this.tilex = aTilex;
/*  49 */     this.tiley = aTiley;
/*     */   }
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
/*     */   public void sendQuestion() {
/*  70 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  72 */     int surfResource = Server.getWorldResource(this.tilex, this.tiley);
/*  73 */     int surfMesh = Server.surfaceMesh.getTile(this.tilex, this.tiley);
/*  74 */     byte surfData = Tiles.decodeData(surfMesh);
/*  75 */     byte surfType = Tiles.decodeType(surfMesh);
/*  76 */     short surfHeight = Tiles.decodeHeight(surfMesh);
/*  77 */     Tiles.Tile surfTile = Tiles.getTile(surfType);
/*  78 */     byte surfClientFlags = Server.getClientSurfaceFlags(this.tilex, this.tiley);
/*  79 */     byte surfServerFlags = Server.getServerSurfaceFlags(this.tilex, this.tiley);
/*     */     
/*  81 */     int caveResource = Server.getCaveResource(this.tilex, this.tiley);
/*  82 */     int caveMesh = Server.caveMesh.getTile(this.tilex, this.tiley);
/*  83 */     byte caveData = Tiles.decodeData(caveMesh);
/*  84 */     byte caveType = Tiles.decodeType(caveMesh);
/*  85 */     short caveHeight = Tiles.decodeHeight(caveMesh);
/*  86 */     Tiles.Tile caveTile = Tiles.getTile(caveType);
/*  87 */     byte caveClientFlags = Server.getClientCaveFlags(this.tilex, this.tiley);
/*  88 */     byte caveServerFlags = Server.getServerCaveFlags(this.tilex, this.tiley);
/*     */     
/*  90 */     int rockMesh = Server.rockMesh.getTile(this.tilex, this.tiley);
/*  91 */     short rockHeight = Tiles.decodeHeight(rockMesh);
/*     */     
/*  93 */     String botUnused = " (unused)";
/*  94 */     String forUnused = " (unused)";
/*  95 */     String colUnused = " (unused)";
/*  96 */     String txUnused = " (unused)";
/*  97 */     String noGUnused = " (unused)";
/*  98 */     boolean canHaveGrubs = false;
/*     */ 
/*     */     
/* 101 */     if (surfTile.isBush() || surfTile.isTree()) {
/*     */       
/* 103 */       buf.append("harray{label{text=\"Surface resource - Damage\"};input{id=\"surf\";maxchars=\"6\";text=\"" + surfResource + "\"};}");
/*     */ 
/*     */       
/* 106 */       if (!surfTile.isEnchanted()) {
/*     */         
/* 108 */         if (surfType != Tiles.Tile.TILE_BUSH_LINGONBERRY.id) {
/*     */           
/* 110 */           botUnused = "";
/* 111 */           colUnused = "";
/*     */         } 
/* 113 */         forUnused = "";
/*     */       } 
/* 115 */       txUnused = "";
/*     */       
/* 117 */       if (surfTile.isTree())
/*     */       {
/*     */         
/* 120 */         if ((surfData >>> 4 & 0xF) == 15) {
/*     */           
/* 122 */           noGUnused = "";
/* 123 */           canHaveGrubs = true;
/*     */         } 
/*     */       }
/* 126 */       if (surfTile.isBush())
/*     */       {
/*     */         
/* 129 */         if ((surfData >>> 4 & 0xE) == 14) {
/*     */           
/* 131 */           noGUnused = "";
/* 132 */           canHaveGrubs = true;
/*     */         } 
/*     */       }
/* 135 */       if (surfType == Tiles.Tile.TILE_TREE_BIRCH.id || surfType == Tiles.Tile.TILE_ENCHANTED_TREE_BIRCH.id || surfType == Tiles.Tile.TILE_MYCELIUM_TREE_BIRCH.id)
/*     */       {
/*     */         
/* 138 */         if ((surfData >>> 4 & 0xE) == 14)
/*     */         {
/* 140 */           noGUnused = "";
/* 141 */           canHaveGrubs = true;
/*     */         }
/*     */       
/*     */       }
/* 145 */     } else if (surfType == Tiles.Tile.TILE_FIELD.id || surfType == Tiles.Tile.TILE_FIELD2.id) {
/*     */       
/* 147 */       buf.append("harray{label{text=\"Surface resource - \"};");
/* 148 */       buf.append("label{text=\"Farmed:\"};");
/* 149 */       buf.append("input{id=\"count\";maxchars=\"2\";text=\"" + (surfResource >>> 11 & 0x1F) + "\"};");
/* 150 */       buf.append("label{text=\"Yield chance:\"};");
/* 151 */       buf.append("input{id=\"surf\";maxchars=\"4\";text=\"" + (surfResource & 0x3FF) + "\"};");
/* 152 */       buf.append("}");
/* 153 */       txUnused = "";
/*     */     }
/* 155 */     else if (surfType == Tiles.Tile.TILE_SAND.id || surfType == Tiles.Tile.TILE_GRASS.id || surfType == Tiles.Tile.TILE_MYCELIUM.id || surfType == Tiles.Tile.TILE_STEPPE.id || surfType == Tiles.Tile.TILE_CLAY.id || surfType == Tiles.Tile.TILE_PEAT.id || surfType == Tiles.Tile.TILE_TAR.id || surfType == Tiles.Tile.TILE_TUNDRA.id || surfType == Tiles.Tile.TILE_MOSS.id) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 160 */       int qlCnt = surfResource >>> 8 & 0xFF;
/* 161 */       int dugCnt = surfResource & 0xFF;
/*     */       
/* 163 */       buf.append("harray{label{text=\"Surface resource - Transform count\"};input{id=\"qlcnt\";maxchars=\"3\";text=\"" + qlCnt + "\"};");
/*     */       
/* 165 */       String ls = "Spare";
/* 166 */       if (surfType == Tiles.Tile.TILE_CLAY.id)
/*     */       {
/* 168 */         ls = "Dug Count";
/*     */       }
/* 170 */       buf.append("label{text=\" " + ls + "\"};input{id=\"surf\";maxchars=\"3\";text=\"" + dugCnt + "\"};label{text=\" (255 = not used)\"};}");
/*     */ 
/*     */       
/* 173 */       if (surfType == Tiles.Tile.TILE_GRASS.id || surfType == Tiles.Tile.TILE_MYCELIUM.id || surfType == Tiles.Tile.TILE_STEPPE.id) {
/*     */         
/* 175 */         botUnused = "";
/* 176 */         forUnused = "";
/* 177 */         colUnused = "";
/*     */       }
/* 179 */       else if (surfType == Tiles.Tile.TILE_PEAT.id || surfType == Tiles.Tile.TILE_MOSS.id) {
/*     */         
/* 181 */         botUnused = "";
/* 182 */         forUnused = "";
/*     */       }
/* 184 */       else if (surfType == Tiles.Tile.TILE_TUNDRA.id) {
/*     */         
/* 186 */         forUnused = "";
/*     */       } 
/* 188 */       if (surfType != Tiles.Tile.TILE_SAND.id)
/*     */       {
/* 190 */         txUnused = "";
/*     */       }
/* 192 */       if (surfType == Tiles.Tile.TILE_GRASS.id || surfType == Tiles.Tile.TILE_MYCELIUM.id)
/*     */       {
/* 194 */         noGUnused = "";
/* 195 */         canHaveGrubs = true;
/*     */       }
/*     */     
/* 198 */     } else if (surfTile.isCaveDoor()) {
/*     */       
/* 200 */       buf.append("harray{label{text=\"Surface resource - Door Strength\"};input{id=\"surf\";maxchars=\"6\";text=\"" + surfResource + "\"};}");
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 206 */       if (surfType == Tiles.Tile.TILE_DIRT.id) {
/*     */         
/* 208 */         noGUnused = "";
/* 209 */         canHaveGrubs = true;
/*     */       } 
/* 211 */       buf.append("harray{label{text=\"Surface resource \"};input{id=\"surf\";maxchars=\"6\";text=\"" + surfResource + "\"};}");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 216 */     buf.append("harray{label{text=\"Cave resource \"}input{id=\"cave\";maxchars=\"6\";text=\"" + caveResource + "\"};}");
/*     */ 
/*     */ 
/*     */     
/* 220 */     if (surfTile.isGrass()) {
/*     */       
/* 222 */       buf.append("harray{label{text=\"Grass tile data \"};");
/*     */       
/* 224 */       buf.append("label{text=\" Growth:\"};input{id=\"growth\";maxchars=\"1\";text=\"" + (surfData >>> 6 & 0x3) + "\"};");
/* 225 */       buf.append("label{text=\" GrassType(not used):\"};input{id=\"grasstype\";maxchars=\"1\";text=\"" + (surfData >>> 4 & 0x3) + "\"};");
/* 226 */       buf.append("label{text=\" Flower:\"};input{id=\"flower\";maxchars=\"2\";text=\"" + (surfData & 0xF) + "\"};");
/* 227 */       buf.append("}");
/*     */     }
/* 229 */     else if (surfType == Tiles.Tile.TILE_MYCELIUM.id) {
/*     */       
/* 231 */       buf.append("harray{label{text=\"Mycelium tile data \"};");
/*     */       
/* 233 */       buf.append("label{text=\" Growth:\"};input{id=\"growth\";maxchars=\"1\";text=\"" + (surfData >>> 6 & 0x3) + "\"};");
/* 234 */       buf.append("label{text=\" GrassType(not used):\"};input{id=\"grasstype\";maxchars=\"1\";text=\"" + (surfData >>> 4 & 0x3) + "\"};");
/* 235 */       buf.append("label{text=\" Flower (not used):\"};input{id=\"flower\";maxchars=\"2\";text=\"" + (surfData & 0xF) + "\"};");
/* 236 */       buf.append("}");
/*     */     }
/* 238 */     else if (surfType == Tiles.Tile.TILE_BUSH.id || surfType == Tiles.Tile.TILE_ENCHANTED_BUSH.id || surfType == Tiles.Tile.TILE_TREE.id || surfType == Tiles.Tile.TILE_ENCHANTED_TREE.id || surfType == Tiles.Tile.TILE_MYCELIUM_BUSH.id || surfType == Tiles.Tile.TILE_MYCELIUM_TREE.id) {
/*     */ 
/*     */ 
/*     */       
/* 242 */       buf.append("harray{label{text=\"Tree/Bush tile data \"};");
/*     */       
/* 244 */       buf.append("label{text=\" Age:\"};input{id=\"age\";maxchars=\"3\";text=\"" + (surfData >>> 4 & 0xF) + "\"};");
/* 245 */       buf.append("label{text=\" Type:\"};input{id=\"type\";maxchars=\"3\";text=\"" + (surfData & 0xF) + "\"};");
/* 246 */       buf.append("}");
/*     */     }
/* 248 */     else if (surfTile.usesNewData()) {
/*     */       
/* 250 */       buf.append("harray{label{text=\"Tree/Bush (new) tile data \"};");
/*     */       
/* 252 */       buf.append("label{text=\" Age:\"};input{id=\"age\";maxchars=\"3\";text=\"" + (surfData >>> 4 & 0xF) + "\"};");
/* 253 */       buf.append("label{text=\" Fruit:\"};input{id=\"harvestable\";maxchars=\"1\";text=\"" + (surfData >>> 3 & 0x1) + "\"};");
/* 254 */       buf.append("label{text=\" Centre:\"};input{id=\"incentre\";maxchars=\"1\";text=\"" + (surfData >>> 2 & 0x1) + "\"};");
/* 255 */       buf.append("label{text=\" Grass Length:\"};input{id=\"growth\";maxchars=\"1\";text=\"" + (surfData & 0x3) + "\"};");
/* 256 */       buf.append("}");
/*     */     }
/* 258 */     else if (surfType == Tiles.Tile.TILE_FIELD.id || surfType == Tiles.Tile.TILE_FIELD2.id) {
/*     */       
/* 260 */       buf.append("harray{label{text=\"Field" + ((surfType == Tiles.Tile.TILE_FIELD2.id) ? "2" : "") + " tile data \"};");
/*     */       
/* 262 */       buf.append("label{text=\" Tended:\"};checkbox{id=\"tended\";selected=\"" + Crops.decodeFieldState(surfData) + "\"};");
/* 263 */       buf.append("label{text=\" Age:\"};input{id=\"age\";maxchars=\"3\";text=\"" + Crops.decodeFieldAge(surfData) + "\"};");
/* 264 */       buf.append("label{text=\" Crop:\"};input{id=\"crop\";maxchars=\"3\";text=\"" + (surfData & 0xF) + "\"};");
/* 265 */       buf.append("}");
/*     */     }
/* 267 */     else if (Tiles.isRoadType(surfType)) {
/*     */       
/* 269 */       buf.append("harray{label{text=\"Surface tile data \"};");
/* 270 */       buf.append("label{text=\" (not used):\"};input{id=\"unused\";maxchars=\"2\";text=\"" + (surfData >>> 3 & 0x1F) + "\"};");
/* 271 */       buf.append("label{text=\" Dir:\"};dropdown{id='dir';options=\"none,NW,NE,SE,SW\";default=\"" + (surfData & 0x7) + "\"}");
/* 272 */       buf.append("}");
/* 273 */       if (surfType == Tiles.Tile.TILE_STONE_SLABS.id || surfType == Tiles.Tile.TILE_POTTERY_BRICKS.id || surfType == Tiles.Tile.TILE_SANDSTONE_BRICKS.id || surfType == Tiles.Tile.TILE_SANDSTONE_SLABS.id || surfType == Tiles.Tile.TILE_SLATE_BRICKS.id || surfType == Tiles.Tile.TILE_SLATE_SLABS.id || surfType == Tiles.Tile.TILE_MARBLE_BRICKS.id || surfType == Tiles.Tile.TILE_MARBLE_SLABS.id)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 279 */         colUnused = "";
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 284 */       buf.append("harray{label{text=\"Surface tile data \"};");
/* 285 */       buf.append("input{id=\"surftiledata\";maxchars=\"3\";text=\"" + surfData + "\"};");
/* 286 */       buf.append("}");
/*     */     } 
/*     */     
/* 289 */     buf.append("harray{label{text=\"Surface Height \"};input{id=\"surfaceheight\";maxchars=\"6\";text=\"" + surfHeight + "\"}};");
/* 290 */     buf.append("harray{label{text=\"Rock Height    \"};input{id=\"rockheight\";maxchars=\"6\";text=\"" + rockHeight + "\"}};");
/* 291 */     buf.append("harray{label{text=\"Cave Height    \"};input{id=\"caveheight\";maxchars=\"6\";text=\"" + caveHeight + "\"}label{text=\"  Cave ceiling:\"};input{id=\"caveceiling\";maxchars=\"3\";text=\"" + caveData + "\"}label{text=\"  (Default is -100, 0)\"}};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     buf.append("label{text=\"Surface Flags:\"}");
/* 298 */     buf.append("checkbox{id=\"bot\";selected=\"" + Server.isBotanizable(this.tilex, this.tiley) + "\";text=\"Botanize" + botUnused + "\"};");
/* 299 */     buf.append("checkbox{id=\"forage\";selected=\"" + Server.isForagable(this.tilex, this.tiley) + "\";text=\"Forage" + forUnused + "\"};");
/* 300 */     buf.append("checkbox{id=\"collect\";selected=\"" + Server.isGatherable(this.tilex, this.tiley) + "\";text=\"Collect" + colUnused + "\"};");
/*     */     
/* 302 */     buf.append("checkbox{id=\"transforming\";selected=\"" + Server.isBeingTransformed(this.tilex, this.tiley) + "\";text=\"Being Transformed" + txUnused + "\"};");
/* 303 */     buf.append("checkbox{id=\"transformed\";selected=\"" + Server.wasTransformed(this.tilex, this.tiley) + "\";text=\"Been Transformed" + txUnused + "\"};");
/* 304 */     buf.append("checkbox{id=\"hive\";selected=\"" + Server.isCheckHive(this.tilex, this.tiley) + "\";text=\"Check Hive\";hover=\"Used exclusivly by the hive generation code.\"};");
/* 305 */     buf.append("checkbox{id=\"hasGrubs\";enabled=\"" + canHaveGrubs + "\";selected=\"" + ((canHaveGrubs && Server.hasGrubs(this.tilex, this.tiley)) ? 1 : 0) + "\";text=\"Has Grubs (or Wurms)" + noGUnused + "\"};");
/*     */     
/* 307 */     buf.append("harray{label{text=\"Cave Flags:\"};label{text=\" Server:\"}input{id=\"caveserverflag\";maxchars=\"3\";text=\"" + (
/*     */         
/* 309 */         Server.getServerCaveFlags(this.tilex, this.tiley) & 0xFF) + "\"}label{text=\" Client:\"}input{id=\"caveclientflag\";maxchars=\"3\";text=\"" + (
/*     */         
/* 311 */         Server.getClientCaveFlags(this.tilex, this.tiley) & 0xFF) + "\"}};");
/*     */ 
/*     */     
/* 314 */     if (getResponder().getPower() >= 2 && getResponder().getPower() < 5)
/*     */     {
/* 316 */       buf.append("label{type=\"bold\";text=\"Take care when changing values that you do not understand!\"}");
/*     */     }
/*     */     
/* 319 */     buf.append(createAnswerButton2());
/*     */     
/* 321 */     getResponder().getCommunicator().sendBml(420, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 327 */     setAnswer(answers);
/*     */     
/* 329 */     if (getResponder().getPower() >= 4 || Servers.isThisATestServer()) {
/* 330 */       QuestionParser.parseTileDataQuestion(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getTilex() {
/* 340 */     return this.tilex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getTiley() {
/* 350 */     return this.tiley;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TileDataQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
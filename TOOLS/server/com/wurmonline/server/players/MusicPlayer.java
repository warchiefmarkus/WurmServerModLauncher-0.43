/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.behaviours.Action;
/*     */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.SoundNames;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MusicPlayer
/*     */   implements SoundNames, MiscConstants
/*     */ {
/*  42 */   private int currentTile = 0;
/*  43 */   private int tileType = 0;
/*  44 */   private int lasttileType = 0;
/*  45 */   private int numSameTiles = 0;
/*     */ 
/*     */   
/*  48 */   private int secondsSinceLastCheck = 0;
/*  49 */   private int nextSongAvail = 200;
/*  50 */   private String lastSong = "";
/*  51 */   private int tilesOnWater = 0;
/*     */ 
/*     */   
/*     */   private final Player listener;
/*     */   
/*  56 */   private static final Logger logger = Logger.getLogger(MusicPlayer.class.getName());
/*     */ 
/*     */   
/*     */   MusicPlayer(Player _listener) {
/*  60 */     this.listener = _listener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isItOkToPlaySong(boolean checkRand) {
/*  71 */     if (this.secondsSinceLastCheck > this.nextSongAvail) {
/*     */ 
/*     */       
/*  74 */       this.secondsSinceLastCheck = 0;
/*  75 */       this.nextSongAvail = 180;
/*     */       
/*  77 */       if (!checkRand) {
/*  78 */         return true;
/*     */       }
/*  80 */       if (Server.rand.nextInt(2) == 0)
/*     */       {
/*  82 */         return true;
/*     */       }
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_BLACKLIGHT_SND() {
/*  90 */     playSong("sound.music.song.blacklight", 30);
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_CAVEHALL1_SND() {
/*  96 */     if (Server.rand.nextInt(10) == 0)
/*     */     {
/*  98 */       if (this.listener.getLayer() < 0) {
/*     */         
/* 100 */         int tx = this.listener.getTileX();
/* 101 */         int ty = this.listener.getTileY();
/* 102 */         for (int x = -2; x <= 2; x++) {
/*     */           
/* 104 */           for (int y = -2; y <= 2; y++) {
/*     */             
/* 106 */             int c = Server.caveMesh.getTile(tx + x, ty + y);
/* 107 */             if (Tiles.decodeType(c) != Tiles.Tile.TILE_CAVE.id)
/*     */             {
/* 109 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/* 113 */         playSong("sound.music.song.cavehall1", 60);
/* 114 */         return true;
/*     */       } 
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_CAVEHALL2_SND() {
/* 122 */     if (Server.rand.nextInt(10) == 0)
/*     */     {
/* 124 */       if (this.listener.getLayer() < 0) {
/*     */ 
/*     */         
/* 127 */         int tx = this.listener.getTileX();
/* 128 */         int ty = this.listener.getTileY();
/* 129 */         for (int x = -1; x <= 1; x++) {
/*     */           
/* 131 */           for (int y = -1; y <= 1; y++) {
/*     */             
/* 133 */             int c = Server.caveMesh.getTile(tx + x, ty + y);
/* 134 */             if (Tiles.decodeType(c) != Tiles.Tile.TILE_CAVE.id)
/*     */             {
/* 136 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/* 140 */         playSong("sound.music.song.cavehall2", 60);
/* 141 */         return true;
/*     */       } 
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_COLOSSUS_SND() {
/* 149 */     playSong("sound.music.song.colossus", 60);
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_DISBAND_SND() {
/* 155 */     playSong("sound.music.song.disbandvillage", 60);
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_DYING1_SND() {
/* 161 */     if (isItOkToPlaySong(false)) {
/*     */       
/* 163 */       int r = Server.rand.nextInt(3);
/* 164 */       if (r == 0) {
/*     */         
/* 166 */         playSong("sound.music.song.dying1", 30);
/* 167 */         return true;
/*     */       } 
/* 169 */       if (r == 1) {
/*     */         
/* 171 */         playSong("sound.music.song.dying2", 30);
/* 172 */         return true;
/*     */       } 
/*     */     } 
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_FOUNDSETTLEMENT_SND() {
/* 180 */     int tx = this.listener.getTileX();
/* 181 */     int ty = this.listener.getTileY();
/* 182 */     for (int x = -20; x <= 20; x++) {
/*     */       
/* 184 */       for (int y = -20; y <= 20; y++) {
/*     */         
/* 186 */         VolaTile t = Zones.getTileOrNull(tx + x, ty + y, true);
/* 187 */         if (t != null) {
/*     */           
/* 189 */           Creature[] crets = t.getCreatures();
/* 190 */           for (int c = 0; c < crets.length; c++) {
/*     */             
/* 192 */             if (crets[c].getMusicPlayer() != null)
/* 193 */               crets[c].getMusicPlayer().playSong("sound.music.song.foundsettlement"); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_MOUNTAINTOP_SND() {
/* 203 */     if (Server.rand.nextInt(5) == 0 && this.listener.getLayer() >= 0) {
/*     */       
/* 205 */       float height = Tiles.decodeHeightAsFloat(this.currentTile);
/*     */       
/* 207 */       if (height > 100.0F) {
/*     */         
/* 209 */         int tx = this.listener.getTileX();
/* 210 */         int ty = this.listener.getTileY();
/* 211 */         for (int x = -1; x <= 1; x++) {
/*     */           
/* 213 */           for (int y = -1; y <= 1; y++) {
/*     */             
/* 215 */             int c = Server.surfaceMesh.getTile(tx + x, ty + y);
/* 216 */             if (Tiles.decodeHeight(c) > height)
/*     */             {
/* 218 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/* 222 */         if (this.listener.getKingdomTemplateId() == 3) {
/* 223 */           playSongEvil("sound.music.song.mountaintop", 60);
/*     */         } else {
/* 225 */           playSong("sound.music.song.mountaintop");
/* 226 */         }  return true;
/*     */       } 
/*     */     } 
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean playPrayer() {
/* 234 */     if (this.listener.getDeity() != null) {
/*     */       
/* 236 */       if ((this.listener.getDeity()).number == 1)
/* 237 */         playSong("sound.music.song.prayingfo"); 
/* 238 */       if ((this.listener.getDeity()).number == 3)
/* 239 */         playSong("sound.music.song.prayingvynora"); 
/* 240 */       if ((this.listener.getDeity()).number == 2)
/* 241 */         playSong("sound.music.song.prayingmagranon"); 
/* 242 */       if ((this.listener.getDeity()).number == 4)
/* 243 */         playSong("sound.music.song.prayinglibila"); 
/* 244 */       return true;
/*     */     } 
/* 246 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_TERRITORYHOTS_SND() {
/* 251 */     if (Server.rand.nextInt(10) == 0) {
/*     */       
/* 253 */       playSong("sound.music.song.territoryhots", 60);
/* 254 */       return true;
/*     */     } 
/* 256 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_TERRITORYWL_SND() {
/* 261 */     if (Server.rand.nextInt(10) == 0) {
/*     */       
/* 263 */       if (this.listener.getKingdomTemplateId() == 3) {
/* 264 */         playSongEvil("sound.music.song.territorywl", 60);
/*     */       } else {
/* 266 */         playSong("sound.music.song.territorywl", 60);
/* 267 */       }  return true;
/*     */     } 
/* 269 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_WHITELIGHT_SND() {
/* 274 */     playSong("sound.music.song.whitelight", 60);
/* 275 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_UNLIMITED_SND() {
/* 280 */     playSong("sound.music.song.unlimited", 600);
/* 281 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_VILLAGERAIN_SND() {
/* 286 */     if (this.listener.getCurrentVillage() != null)
/*     */     {
/* 288 */       if (this.listener.getCitizenVillage() == this.listener.getCurrentVillage())
/*     */       {
/* 290 */         if (Server.rand.nextInt(2) == 0 && isItOkToPlaySong(false))
/*     */         {
/* 292 */           if (Server.getWeather().getRain() > 0.4F) {
/*     */             
/* 294 */             if (this.listener.getKingdomTemplateId() == 3) {
/* 295 */               playSongEvil("sound.music.song.villagerain");
/*     */             } else {
/* 297 */               playSong("sound.music.song.villagerain");
/* 298 */             }  return true;
/*     */           } 
/*     */         }
/*     */       }
/*     */     }
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_VILLAGESUN_SND() {
/* 308 */     if (this.listener.getCurrentVillage() != null)
/*     */     {
/* 310 */       if (this.listener.getCitizenVillage() == this.listener.getCurrentVillage())
/*     */       {
/* 312 */         if (Server.rand.nextInt(2) == 0 && isItOkToPlaySong(false))
/*     */         {
/* 314 */           if (Server.getWeather().getCloudiness() < 0.5F) {
/*     */             
/* 316 */             if (this.listener.getKingdomTemplateId() == 3) {
/* 317 */               playSongEvil("sound.music.song.villagesun");
/*     */             } else {
/* 319 */               playSong("sound.music.song.villagesun");
/* 320 */             }  return true;
/*     */           } 
/*     */         }
/*     */       }
/*     */     }
/* 325 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_SUNRISEPASS_SND() {
/* 330 */     if (Server.rand.nextInt(2) == 0 && isItOkToPlaySong(false))
/*     */     {
/* 332 */       if (Server.getWeather().getCloudiness() < 0.5F)
/*     */       {
/* 334 */         if (this.listener.getStatus().getRotation() < 165.0F && this.listener.getStatus().getRotation() > 15.0F)
/*     */         {
/* 336 */           if (WurmCalendar.isMorning()) {
/*     */             
/* 338 */             if (this.listener.getKingdomTemplateId() == 3) {
/* 339 */               playSongEvil("sound.music.song.sunrisepass");
/*     */             } else {
/* 341 */               playSong("sound.music.song.sunrisepass");
/* 342 */             }  return true;
/*     */           } 
/*     */         }
/*     */       }
/*     */     }
/* 347 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_SUNRISE1_SND() {
/* 352 */     if (Server.rand.nextInt(2) == 0 && isItOkToPlaySong(false))
/*     */     {
/* 354 */       if (Server.getWeather().getCloudiness() < 0.5F)
/*     */       {
/* 356 */         if (this.listener.getStatus().getRotation() < 165.0F && this.listener.getStatus().getRotation() > 15.0F)
/*     */         {
/* 358 */           if (WurmCalendar.isMorning()) {
/*     */             
/* 360 */             if (this.listener.getKingdomTemplateId() == 3) {
/* 361 */               playSongEvil("sound.music.song.sunrise1");
/*     */             } else {
/* 363 */               playSong("sound.music.song.sunrise1");
/* 364 */             }  return true;
/*     */           } 
/*     */         }
/*     */       }
/*     */     }
/* 369 */     return false;
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
/*     */   public final boolean checkMUSIC_VILLAGEWORK_SND() {
/* 385 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkMUSIC_WURMISWAITING_SND() {
/* 390 */     if (Server.rand.nextInt(100) == 0 && isItOkToPlaySong(false)) {
/*     */       
/* 392 */       playSong("sound.music.song.wurmiswaiting");
/* 393 */       return true;
/*     */     } 
/* 395 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_ANTHEMHOTS_SND() {
/* 400 */     if (isItOkToPlaySong(false))
/* 401 */       playSong("sound.music.song.anthemhots"); 
/* 402 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_ANTHEMMOLREHAN_SND() {
/* 407 */     if (isItOkToPlaySong(false))
/* 408 */       playSong("sound.music.song.anthemmolrehan"); 
/* 409 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean checkMUSIC_ANTHEMJENN_SND() {
/* 414 */     if (isItOkToPlaySong(false))
/* 415 */       playSong("sound.music.song.anthemjenn"); 
/* 416 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkTravelling() {
/* 421 */     if (this.numSameTiles > 100) {
/*     */       
/* 423 */       this.numSameTiles = 0;
/* 424 */       int song = Server.rand.nextInt(6);
/* 425 */       if (song == 0)
/* 426 */         return playSong("sound.music.song.travelling1"); 
/* 427 */       if (song == 1)
/* 428 */         return playSong("sound.music.song.travelling2"); 
/* 429 */       if (song == 2) {
/* 430 */         return playSong("sound.music.song.travelling3");
/*     */       }
/*     */     } 
/* 433 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkBattleAdventure(boolean mix) {
/* 438 */     if (this.listener.currentKingdom != this.listener.getKingdomId() || mix || this.listener.getEnemyPresense() > 0) {
/*     */       
/* 440 */       if (!mix && Server.rand.nextInt(10) == 0)
/* 441 */         return checkTravelExploration(true); 
/* 442 */       int num = Server.rand.nextInt(mix ? 20 : 8);
/* 443 */       if (num == 0)
/* 444 */         return playSong("sound.music.song.abandon", 400); 
/* 445 */       if (num == 1)
/* 446 */         return playSong("sound.music.song.backhome", 400); 
/* 447 */       if (num == 2)
/* 448 */         return playSong("sound.music.song.deadwater", 400); 
/* 449 */       if (num == 3)
/* 450 */         return playSong("sound.music.song.contact", 400); 
/* 451 */       if (num == 4)
/* 452 */         return playSong("sound.music.song.sunglow", 400); 
/* 453 */       if (num == 5)
/* 454 */         return playSong("sound.music.song.dancehorde", 400); 
/* 455 */       return false;
/*     */     } 
/* 457 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final boolean checkTravelExploration(boolean mix) {
/* 462 */     if (this.listener.getCurrentVillage() == null || mix) {
/*     */       
/* 464 */       if (!mix && Server.rand.nextInt(10) == 0)
/* 465 */         return checkVillageMeditation(true); 
/* 466 */       if (this.listener.getVehicle() != -10L) {
/*     */         
/* 468 */         if (this.listener.getMovementScheme().getMountSpeed() > 0.1F && this.listener.isMoving()) {
/*     */           
/* 470 */           int j = Server.rand.nextInt(6);
/* 471 */           if (j == 0)
/* 472 */             return playSong("sound.music.song.north", 400); 
/* 473 */           if (j == 1)
/* 474 */             return playSong("sound.music.song.stride", 400); 
/* 475 */           if (j == 2)
/* 476 */             return playSong("sound.music.song.shores", 400); 
/* 477 */           if (j == 3 && this.listener.getCurrentKingdom() == this.listener.getKingdomId())
/* 478 */             return playSong("sound.music.song.familiar", 400); 
/* 479 */           return false;
/*     */         } 
/*     */ 
/*     */         
/* 483 */         int i = Server.rand.nextInt(6);
/* 484 */         if (i == 0)
/* 485 */           return playSong("sound.music.song.north", 400); 
/* 486 */         if (i == 1)
/* 487 */           return playSong("sound.music.song.ridge", 400); 
/* 488 */         if (i == 2)
/* 489 */           return playSong("sound.music.song.through", 400); 
/* 490 */         if (i == 3)
/* 491 */           return playSong("sound.music.song.skyfire", 400); 
/* 492 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 497 */       int num = Server.rand.nextInt(8);
/* 498 */       if (num == 0)
/* 499 */         return playSong("sound.music.song.ridge", 400); 
/* 500 */       if (num == 1)
/* 501 */         return playSong("sound.music.song.skyfire", 400); 
/* 502 */       if (num == 2)
/* 503 */         return playSong("sound.music.song.shores", 400); 
/* 504 */       if (num == 3)
/* 505 */         return playSong("sound.music.song.through", 400); 
/* 506 */       if (num == 4 && this.listener.getCurrentKingdom() == this.listener.getKingdomId())
/* 507 */         return playSong("sound.music.song.familiar", 400); 
/* 508 */       return false;
/*     */     } 
/*     */     
/* 511 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkVillageMeditation(boolean mix) {
/* 516 */     if (!mix && Server.rand.nextInt(10) == 0)
/* 517 */       return checkTravelExploration(true); 
/* 518 */     int num = 20;
/* 519 */     boolean meditating = false;
/*     */     
/*     */     try {
/* 522 */       Action act = this.listener.getCurrentAction();
/* 523 */       if (act.getNumber() == 384) {
/* 524 */         meditating = true;
/*     */       }
/* 526 */     } catch (NoSuchActionException noSuchActionException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 531 */     if (this.listener.getCurrentVillage() != null || mix) {
/*     */       
/* 533 */       if (meditating)
/* 534 */         num = 18; 
/* 535 */       if (this.listener.getCurrentVillage() == this.listener.getCitizenVillage() || this.listener.getCitizenVillage() == null)
/* 536 */         num /= 2; 
/* 537 */       int song = Server.rand.nextInt(num);
/* 538 */       if (song == 0)
/* 539 */         return playSong("sound.music.song.wakingup", 420); 
/* 540 */       if (song == 1)
/* 541 */         return playSong("sound.music.song.fingerfo", 400); 
/* 542 */       if (song == 2 && WurmCalendar.isNight())
/* 543 */         return playSong("sound.music.song.inyoureyes", 400); 
/* 544 */       if (song == 3)
/* 545 */         return playSong("sound.music.song.beatinganvil", 400); 
/* 546 */       if (song == 4)
/* 547 */         return playSong("sound.music.song.promisingfoal", 400); 
/* 548 */       if (song == 5)
/* 549 */         return playSong("sound.music.song.longsummer", 420); 
/* 550 */       if (song == 6)
/* 551 */         return playSong("sound.music.song.whyyoudive", 400); 
/*     */     } 
/* 553 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkEchoes() {
/* 558 */     int song = Server.rand.nextInt(10);
/* 559 */     if (song == 0)
/* 560 */       return playSong("sound.music.song.echoes1", 120); 
/* 561 */     if (song == 1)
/* 562 */       return playSong("sound.music.song.echoes2", 120); 
/* 563 */     if (song == 2 && WurmCalendar.isNight())
/* 564 */       return playSong("sound.music.song.echoes3", 120); 
/* 565 */     if (song == 3)
/* 566 */       return playSong("sound.music.song.echoes4", 120); 
/* 567 */     if (song == 4)
/* 568 */       return playSong("sound.music.song.echoes5", 120); 
/* 569 */     if (song == 5) {
/* 570 */       return playSong("sound.music.song.echoes6", 120);
/*     */     }
/* 572 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveTile(int _tileNum, boolean onWater) {
/* 577 */     this.currentTile = _tileNum;
/* 578 */     int _tileType = Tiles.decodeType(this.currentTile);
/* 579 */     if (this.tileType == _tileType || this.lasttileType == _tileType) {
/*     */       
/* 581 */       this.numSameTiles++;
/*     */     }
/*     */     else {
/*     */       
/* 585 */       this.lasttileType = this.tileType;
/* 586 */       this.tileType = _tileType;
/* 587 */       this.numSameTiles = 0;
/*     */     } 
/* 589 */     if (onWater)
/*     */     {
/* 591 */       this.tilesOnWater++;
/*     */     }
/* 593 */     if (isItOkToPlaySong(true))
/*     */     {
/* 595 */       if (this.listener.getLayer() >= 0) {
/*     */         
/* 597 */         if (!checkMUSIC_MOUNTAINTOP_SND())
/*     */         {
/* 599 */           if (!checkTravelling() && this.tilesOnWater > 100)
/*     */           {
/* 601 */             int song = Server.rand.nextInt(4);
/* 602 */             if (song == 0)
/* 603 */               playSong("sound.music.song.shanty1"); 
/* 604 */             this.tilesOnWater = 0;
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 610 */       else if (!checkMUSIC_CAVEHALL1_SND()) {
/*     */         
/* 612 */         if (!checkMUSIC_CAVEHALL2_SND())
/*     */         {
/* 614 */           if (!checkEchoes()) {
/* 615 */             checkTravelling();
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void tickSecond() {
/* 624 */     this.secondsSinceLastCheck++;
/* 625 */     if (isItOkToPlaySong(true))
/*     */     {
/* 627 */       if (!checkBattleAdventure(!Servers.localServer.PVPSERVER))
/*     */       {
/* 629 */         if (!checkTravelExploration(false))
/*     */         {
/* 631 */           if (!checkVillageMeditation(false))
/*     */           {
/* 633 */             if (!checkEchoes()) {
/*     */               
/* 635 */               if (!checkMUSIC_SUNRISE1_SND() && 
/* 636 */                 !checkMUSIC_SUNRISEPASS_SND() && 
/* 637 */                 !checkMUSIC_VILLAGESUN_SND()) {
/* 638 */                 checkMUSIC_VILLAGERAIN_SND();
/*     */               }
/* 640 */               checkMUSIC_WURMISWAITING_SND();
/*     */             } 
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean playSong(String song) {
/* 650 */     return playSong(song, 180);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean playSongEvil(String song) {
/* 655 */     return playSongEvil(song, 180);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean playSong(String song, int _nextSongAvail) {
/* 660 */     if (!song.equals(this.lastSong)) {
/*     */       
/* 662 */       this.nextSongAvail = _nextSongAvail;
/*     */       
/* 664 */       SoundPlayer.playSong(song, this.listener);
/* 665 */       this.lastSong = song;
/* 666 */       return true;
/*     */     } 
/* 668 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean playSongEvil(String song, int _nextSongAvail) {
/* 673 */     if (!song.equals(this.lastSong)) {
/*     */       
/* 675 */       this.nextSongAvail = _nextSongAvail;
/*     */       
/* 677 */       SoundPlayer.playSong(song, this.listener, 1.0F);
/* 678 */       this.lastSong = song;
/* 679 */       return true;
/*     */     } 
/* 681 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\MusicPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
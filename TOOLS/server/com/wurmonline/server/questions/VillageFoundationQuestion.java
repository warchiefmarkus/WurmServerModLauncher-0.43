/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.kingdom.InfluenceChain;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.sounds.SoundPlayer;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.GuardPlan;
/*      */ import com.wurmonline.server.villages.NoSuchRoleException;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class VillageFoundationQuestion
/*      */   extends Question
/*      */   implements VillageStatus, ItemTypes, MonetaryConstants, CounterTypes
/*      */ {
/*   83 */   private static final Logger logger = Logger.getLogger(VillageFoundationQuestion.class.getName());
/*      */   public int tokenx;
/*      */   public int tokeny;
/*      */   public boolean expanding = false;
/*   87 */   private String error = "";
/*   88 */   private int sequence = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   93 */   public int selectedWest = 5;
/*   94 */   public int selectedEast = 5;
/*   95 */   public int selectedNorth = 5;
/*   96 */   public int selectedSouth = 5;
/*      */   
/*   98 */   private int diameterX = this.selectedWest + this.selectedEast + 1;
/*   99 */   private int diameterY = this.selectedNorth + this.selectedSouth + 1;
/*  100 */   private int maxGuards = GuardPlan.getMaxGuards(this.diameterX, this.diameterY);
/*  101 */   public int selectedGuards = 1;
/*  102 */   public byte dir = 0;
/*      */   private final boolean hasCompass;
/*  104 */   private int tiles = this.diameterX * this.diameterY;
/*  105 */   public int initialPerimeter = 0;
/*  106 */   private int perimeterDiameterX = this.diameterX + 5 + 5 + this.initialPerimeter + this.initialPerimeter;
/*      */   
/*  108 */   private int perimeterDiameterY = this.diameterY + 5 + 5 + this.initialPerimeter + this.initialPerimeter;
/*      */   
/*  110 */   private int perimeterTiles = this.perimeterDiameterX * this.perimeterDiameterY - (this.diameterX + 5 + 5) * (this.diameterY + 5 + 5);
/*      */ 
/*      */   
/*  113 */   public String motto = "A settlement like no other.";
/*      */   private static final int maxFactor = 4;
/*  115 */   public String villageName = "";
/*      */   
/*      */   private boolean permanent = false;
/*      */   
/*      */   public boolean democracy = false;
/*  120 */   public byte spawnKingdom = 0;
/*  121 */   private Item deed = null;
/*      */   
/*      */   public static final long MINIMUM_LEFT_UPKEEP = 30000L;
/*      */   
/*      */   private static final long DEED_VALUE = 100000L;
/*      */   
/*      */   public boolean surfaced;
/*  128 */   private long totalUpkeep = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean changingName = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VillageFoundationQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  142 */     super(aResponder, aTitle, aQuestion, 7, aTarget);
/*  143 */     this.tokenx = aResponder.getTileX();
/*  144 */     this.tokeny = aResponder.getTileY();
/*  145 */     this.surfaced = true;
/*  146 */     this.hasCompass = (aResponder.getBestCompass() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VillageFoundationQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, Village aVillage, boolean aExpanding) {
/*  164 */     super(aResponder, aTitle, aQuestion, 7, aTarget);
/*  165 */     this.tokenx = aVillage.getTokenX();
/*  166 */     this.tokeny = aVillage.getTokenY();
/*  167 */     this.surfaced = aVillage.isOnSurface();
/*  168 */     this.initialPerimeter = aVillage.getPerimeterSize();
/*  169 */     this.democracy = aVillage.isDemocracy();
/*  170 */     this.spawnKingdom = aVillage.kingdom;
/*  171 */     this.motto = aVillage.getMotto();
/*  172 */     this.villageName = aVillage.getName();
/*  173 */     this.selectedWest = this.tokenx - aVillage.getStartX();
/*  174 */     this.selectedEast = aVillage.getEndX() - this.tokenx;
/*  175 */     this.selectedNorth = this.tokeny - aVillage.getStartY();
/*  176 */     this.selectedSouth = aVillage.getEndY() - this.tokeny;
/*  177 */     this.selectedGuards = aVillage.plan.getNumHiredGuards();
/*  178 */     this.expanding = aExpanding;
/*  179 */     this.hasCompass = (aResponder.getBestCompass() != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTileX() {
/*  184 */     return this.tokenx;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTileY() {
/*  189 */     return this.tokeny;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSurfaced() {
/*  198 */     return this.surfaced;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void answer(Properties answers) {
/*  209 */     setAnswer(answers);
/*  210 */     if (answersFail()) {
/*      */       
/*  212 */       removeSettlementMarkers();
/*  213 */       removePerimeterMarkers();
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean answersFail() {
/*  226 */     if (!checkDeedItem())
/*  227 */       return false; 
/*  228 */     if (!checkToken())
/*  229 */       return false; 
/*  230 */     String val = getAnswer().getProperty("cancel");
/*  231 */     if (val != null && val.equals("true")) {
/*      */       
/*  233 */       getResponder().getCommunicator().sendNormalServerMessage("You decide not to fill in the form right now.");
/*  234 */       return true;
/*      */     } 
/*      */     
/*  237 */     if (this.expanding) {
/*      */       
/*      */       try {
/*      */         
/*  241 */         Village oldvill = Villages.getVillage(this.deed.getData2());
/*  242 */         if (oldvill.plan != null && 
/*  243 */           oldvill.plan.isUnderSiege())
/*      */         {
/*  245 */           getResponder().getCommunicator().sendNormalServerMessage("You can't do this now since the settlement is under siege.");
/*      */           
/*  247 */           return true;
/*      */         }
/*      */       
/*  250 */       } catch (NoSuchVillageException nsv) {
/*      */         
/*  252 */         return true;
/*      */       } 
/*      */     }
/*  255 */     if (this.sequence == 0) {
/*      */       
/*  257 */       createQuestion1();
/*  258 */       return false;
/*      */     } 
/*  260 */     if (this.sequence == 1) {
/*      */       
/*  262 */       parseVillageFoundationQuestion1();
/*  263 */       return false;
/*      */     } 
/*  265 */     if (!checkBlockingCreatures())
/*  266 */       return true; 
/*  267 */     if (!checkStructuresInArea())
/*  268 */       return true; 
/*  269 */     if (!checkBlockingItems()) {
/*  270 */       return true;
/*      */     }
/*  272 */     setSize();
/*  273 */     if (!checkTile())
/*  274 */       return true; 
/*  275 */     if (this.sequence == 2) {
/*      */       
/*  277 */       parseVillageFoundationQuestion2();
/*  278 */       return false;
/*      */     } 
/*  280 */     if (this.sequence != 6 && !checkSize())
/*  281 */       return true; 
/*  282 */     if (this.sequence == 3)
/*  283 */       parseVillageFoundationQuestion3(); 
/*  284 */     if (this.sequence == 4)
/*  285 */       parseVillageFoundationQuestion4(); 
/*  286 */     if (this.sequence == 5)
/*  287 */       parseVillageFoundationQuestion5(); 
/*  288 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean checkTile() {
/*  293 */     VolaTile tile = Zones.getOrCreateTile(this.tokenx, this.tokeny, this.surfaced);
/*  294 */     if (tile != null) {
/*      */       
/*  296 */       int tx = tile.tilex;
/*  297 */       int ty = tile.tiley;
/*  298 */       int tt = Server.surfaceMesh.getTile(tx, ty);
/*  299 */       if (Tiles.decodeType(tt) == Tiles.Tile.TILE_LAVA.id || Tiles.isMineDoor(Tiles.decodeType(tt))) {
/*      */         
/*  301 */         getResponder().getCommunicator().sendSafeServerMessage("You cannot found a settlement here.");
/*  302 */         return false;
/*      */       } 
/*  304 */       for (int x = -1; x <= 1; x++) {
/*      */         
/*  306 */         for (int y = -1; y <= 1; y++) {
/*      */           
/*  308 */           int t = Server.surfaceMesh.getTile(tx + x, ty + y);
/*  309 */           if (Tiles.decodeHeight(t) < 0) {
/*      */             
/*  311 */             getResponder().getCommunicator().sendSafeServerMessage("You cannot found a settlement here. Too close to water.");
/*      */             
/*  313 */             return false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  318 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void createIntro() {
/*  323 */     VillageFoundationQuestion vf = new VillageFoundationQuestion(getResponder(), "Settlement Application Form", "Welcome to the Settlement Application Form", this.target);
/*      */ 
/*      */     
/*  326 */     if (vf != null) {
/*      */       
/*  328 */       copyValues(vf);
/*  329 */       vf.sequence = 0;
/*  330 */       vf.sendIntro();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createQuestion1() {
/*  336 */     VillageFoundationQuestion vf = new VillageFoundationQuestion(getResponder(), "Settlement Size", "Stage One - The size of your settlement", this.target);
/*      */ 
/*      */     
/*  339 */     if (vf != null) {
/*      */       
/*  341 */       copyValues(vf);
/*  342 */       vf.sequence = 1;
/*  343 */       vf.sendQuestion();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createQuestion2() {
/*  349 */     VillageFoundationQuestion vfq = new VillageFoundationQuestion(getResponder(), "The Deed Perimeter", "Congratulations! You have chosen a deed that will be " + this.diameterX + " tiles by " + this.diameterY + " tiles.", this.target);
/*      */ 
/*      */     
/*  352 */     if (vfq != null) {
/*      */       
/*  354 */       copyValues(vfq);
/*  355 */       vfq.sequence = 2;
/*  356 */       vfq.sendQuestion2();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createQuestion3() {
/*  362 */     VillageFoundationQuestion vfq = new VillageFoundationQuestion(getResponder(), "Naming Your Deed", "Your survey was a success!", this.target);
/*      */     
/*  364 */     if (vfq != null) {
/*      */       
/*  366 */       copyValues(vfq);
/*  367 */       vfq.sequence = 3;
/*  368 */       vfq.sendQuestion3();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createQuestion4() {
/*  374 */     VillageFoundationQuestion vfq = new VillageFoundationQuestion(getResponder(), "Guards", "Congratulations! You have reserved the name '" + this.villageName + "'", this.target);
/*      */ 
/*      */     
/*  377 */     if (vfq != null) {
/*      */       
/*  379 */       copyValues(vfq);
/*  380 */       vfq.sequence = 4;
/*  381 */       vfq.sendQuestion4();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createQuestion5() {
/*  387 */     VillageFoundationQuestion vfq = new VillageFoundationQuestion(getResponder(), "Check your settings", "Check your settings and Found your settlement!", this.target);
/*      */     
/*  389 */     if (vfq != null) {
/*      */       
/*  391 */       copyValues(vfq);
/*  392 */       vfq.sequence = 5;
/*  393 */       vfq.sendQuestion5();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createQuestion6() {
/*  399 */     if (!this.expanding) {
/*      */       
/*  401 */       SoundPlayer.playSong("sound.music.song.foundsettlement", getResponder());
/*  402 */       VillageFoundationQuestion vfq = new VillageFoundationQuestion(getResponder(), "CONGRATULATIONS!", "CONGRATULATIONS!", this.target);
/*      */ 
/*      */       
/*  405 */       if (vfq != null) {
/*      */         
/*  407 */         copyValues(vfq);
/*  408 */         vfq.sequence = 6;
/*  409 */         vfq.sendQuestion6();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   boolean parseVillageFoundationQuestion1() {
/*  416 */     String key = "back";
/*  417 */     String val = getAnswer().getProperty(key);
/*  418 */     if (val != null && val.equals("true")) {
/*      */       
/*  420 */       createIntro();
/*  421 */       return false;
/*      */     } 
/*      */     
/*  424 */     this.error = "";
/*      */     
/*  426 */     this.selectedWest = 5;
/*  427 */     this.selectedEast = 5;
/*  428 */     this.selectedNorth = 5;
/*  429 */     this.selectedSouth = 5;
/*  430 */     key = "sizeW";
/*  431 */     val = getAnswer().getProperty(key);
/*  432 */     if (val != null) {
/*      */       
/*      */       try {
/*      */         
/*  436 */         this.selectedWest = Integer.parseInt(val);
/*  437 */         if (this.selectedWest < 5)
/*      */         {
/*  439 */           this.error = "The minimum size is 5. ";
/*  440 */           this.selectedWest = 5;
/*      */         }
/*      */       
/*      */       }
/*  444 */       catch (NumberFormatException nsf) {
/*      */         
/*  446 */         this.error += "* Failed to parse the desired size of " + val + " to a valid number. ";
/*      */       } 
/*      */     }
/*  449 */     key = "sizeE";
/*  450 */     val = getAnswer().getProperty(key);
/*  451 */     if (val != null) {
/*      */       
/*      */       try {
/*      */         
/*  455 */         this.selectedEast = Integer.parseInt(val);
/*  456 */         if (this.selectedEast < 5)
/*      */         {
/*  458 */           this.error = "The minimum size is 5. ";
/*  459 */           this.selectedEast = 5;
/*      */         }
/*      */       
/*      */       }
/*  463 */       catch (NumberFormatException nsf) {
/*      */         
/*  465 */         this.error += "* Failed to parse the desired size of " + val + " to a valid number. ";
/*      */       } 
/*      */     }
/*  468 */     key = "sizeN";
/*  469 */     val = getAnswer().getProperty(key);
/*  470 */     if (val != null) {
/*      */       
/*      */       try {
/*      */         
/*  474 */         this.selectedNorth = Integer.parseInt(val);
/*  475 */         if (this.selectedNorth < 5)
/*      */         {
/*  477 */           this.error = "The minimum size is 5. ";
/*  478 */           this.selectedNorth = 5;
/*      */         }
/*      */       
/*  481 */       } catch (NumberFormatException nsf) {
/*      */         
/*  483 */         this.error += "Failed to parse the desired size of " + val + " to a valid number. ";
/*      */       } 
/*      */     }
/*  486 */     key = "sizeS";
/*  487 */     val = getAnswer().getProperty(key);
/*  488 */     if (val != null) {
/*      */       
/*      */       try {
/*      */         
/*  492 */         this.selectedSouth = Integer.parseInt(val);
/*  493 */         if (this.selectedSouth < 5)
/*      */         {
/*  495 */           this.error = "The minimum size is 5. ";
/*  496 */           this.selectedSouth = 5;
/*      */         }
/*      */       
/*  499 */       } catch (NumberFormatException nsf) {
/*      */         
/*  501 */         this.error += "Failed to parse the desired size of " + val + " to a valid number. ";
/*      */       } 
/*      */     }
/*  504 */     this.diameterX = this.selectedWest + this.selectedEast + 1;
/*  505 */     this.diameterY = this.selectedNorth + this.selectedSouth + 1;
/*      */     
/*  507 */     if (!((this.diameterX / this.diameterY <= 4.0F && this.diameterY / this.diameterX <= 4.0F) ? 1 : 0))
/*      */     {
/*  509 */       this.error += "The deed would be too stretched. One edge is not allowed to be more than 4 times the length of the other.";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  517 */     if (this.error.length() < 1) {
/*      */       
/*  519 */       int xa = Zones.safeTileX(this.tokenx - this.selectedWest);
/*  520 */       int xe = Zones.safeTileX(this.tokenx + this.selectedEast);
/*  521 */       int ya = Zones.safeTileY(this.tokeny - this.selectedNorth);
/*  522 */       int ye = Zones.safeTileY(this.tokeny + this.selectedSouth);
/*  523 */       for (int x = xa; x <= xe; x++) {
/*      */         
/*  525 */         for (int y = ya; y <= ye; y++) {
/*      */           
/*  527 */           boolean create = false;
/*  528 */           if (x == xa) {
/*      */             
/*  530 */             if (y == ya || y == ye || y % 5 == 0) {
/*  531 */               create = true;
/*      */             }
/*  533 */           } else if (x == xe) {
/*      */             
/*  535 */             if (y == ya || y == ye || y % 5 == 0)
/*      */             {
/*      */ 
/*      */ 
/*      */               
/*  540 */               create = true;
/*      */             }
/*      */           }
/*  543 */           else if (y == ya || y == ye) {
/*      */             
/*  545 */             if (x % 5 == 0)
/*  546 */               create = true; 
/*      */           } 
/*  548 */           if (create) {
/*      */             
/*      */             try {
/*      */               
/*  552 */               Item i = ItemFactory.createItem(671, 80.0F, getResponder().getName());
/*  553 */               i.setPosXYZ(((x << 2) + 2), ((y << 2) + 2), Zones.calculateHeight(((x << 2) + 2), ((y << 2) + 2), true) + 5.0F);
/*  554 */               Zones.getZone(x, y, true).addItem(i);
/*      */             
/*      */             }
/*  557 */             catch (Exception ex) {
/*      */               
/*  559 */               logger.log(Level.INFO, ex.getMessage());
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  565 */     setSize();
/*  566 */     if (!checkBlockingKingdoms())
/*      */     {
/*  568 */       this.error += "You would be founding too close to enemy kingdom influence.";
/*      */     }
/*  570 */     if (this.error.length() > 0) {
/*      */       
/*  572 */       createQuestion1();
/*  573 */       return false;
/*      */     } 
/*  575 */     createQuestion2();
/*  576 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeSettlementMarkers() {
/*  581 */     int xa = Zones.safeTileX(this.tokenx - this.selectedWest);
/*  582 */     int xe = Zones.safeTileX(this.tokenx + this.selectedEast);
/*  583 */     int ya = Zones.safeTileY(this.tokeny - this.selectedNorth);
/*  584 */     int ye = Zones.safeTileY(this.tokeny + this.selectedSouth);
/*  585 */     boolean notFound = false;
/*  586 */     for (int x = xa; x <= xe; x++) {
/*      */       
/*  588 */       if (notFound)
/*      */         break; 
/*  590 */       for (int y = ya; y <= ye; y++) {
/*      */         
/*  592 */         boolean remove = false;
/*  593 */         if (x == xa) {
/*      */           
/*  595 */           if (y == ya || y == ye || y % 5 == 0) {
/*  596 */             remove = true;
/*      */           }
/*  598 */         } else if (x == xe) {
/*      */           
/*  600 */           if (y == ya || y == ye || y % 5 == 0)
/*      */           {
/*  602 */             remove = true;
/*      */           }
/*      */         }
/*  605 */         else if (y == ya || y == ye) {
/*      */           
/*  607 */           if (x % 5 == 0)
/*  608 */             remove = true; 
/*      */         } 
/*  610 */         if (remove) {
/*      */           
/*      */           try {
/*      */             
/*  614 */             Zone zone = Zones.getZone(x, y, true);
/*  615 */             VolaTile vtile = zone.getTileOrNull(x, y);
/*  616 */             if (vtile != null) {
/*      */               
/*  618 */               Item[] items = vtile.getItems();
/*  619 */               for (int i = 0; i < items.length; i++) {
/*      */                 
/*  621 */                 if (items[i].getTemplateId() == 671) {
/*      */                   
/*  623 */                   items[i].removeAndEmpty();
/*  624 */                   notFound = false;
/*      */                   
/*      */                   break;
/*      */                 } 
/*  628 */                 notFound = true;
/*      */               } 
/*  630 */               if (notFound) {
/*      */                 break;
/*      */               }
/*      */             } 
/*  634 */           } catch (NoSuchZoneException nsz) {
/*      */             
/*  636 */             logger.log(Level.INFO, nsz.getMessage());
/*  637 */             notFound = true;
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void removePerimeterMarkers() {
/*  647 */     int xa = Zones.safeTileX(this.tokenx - this.selectedWest - 5 - this.initialPerimeter);
/*  648 */     int xe = Zones.safeTileX(this.tokenx + this.selectedEast + 5 + this.initialPerimeter);
/*  649 */     int ya = Zones.safeTileY(this.tokeny - this.selectedNorth - 5 - this.initialPerimeter);
/*  650 */     int ye = Zones.safeTileY(this.tokeny + this.selectedSouth + 5 + this.initialPerimeter);
/*  651 */     boolean notFound = false;
/*  652 */     for (int x = xa; x <= xe; x++) {
/*      */       
/*  654 */       if (notFound)
/*      */         break; 
/*  656 */       for (int y = ya; y <= ye; y++) {
/*      */         
/*  658 */         boolean remove = false;
/*  659 */         if (x == xa) {
/*      */           
/*  661 */           if (y == ya || y == ye || y % 10 == 0) {
/*  662 */             remove = true;
/*      */           }
/*  664 */         } else if (x == xe) {
/*      */           
/*  666 */           if (y == ya || y == ye || y % 10 == 0)
/*      */           {
/*  668 */             remove = true;
/*      */           }
/*      */         }
/*  671 */         else if (y == ya || y == ye) {
/*      */           
/*  673 */           if (x % 10 == 0)
/*  674 */             remove = true; 
/*      */         } 
/*  676 */         if (remove) {
/*      */           
/*      */           try {
/*      */             
/*  680 */             Zone zone = Zones.getZone(x, y, this.surfaced);
/*  681 */             VolaTile tile = zone.getTileOrNull(x, y);
/*  682 */             if (tile != null) {
/*      */               
/*  684 */               Item[] items = tile.getItems();
/*  685 */               for (int i = 0; i < items.length; i++) {
/*      */                 
/*  687 */                 if (items[i].getTemplateId() == 673) {
/*      */                   
/*  689 */                   notFound = false;
/*  690 */                   items[i].removeAndEmpty();
/*      */                   
/*      */                   break;
/*      */                 } 
/*  694 */                 notFound = true;
/*      */               } 
/*  696 */               if (notFound) {
/*      */                 break;
/*      */               }
/*      */             } 
/*  700 */           } catch (NoSuchZoneException nsz) {
/*      */             
/*  702 */             logger.log(Level.INFO, nsz.getMessage());
/*  703 */             notFound = true;
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   boolean parseVillageFoundationQuestion2() {
/*  713 */     String key = "back";
/*  714 */     String val = getAnswer().getProperty(key);
/*  715 */     if (val != null && val.equals("true")) {
/*      */       
/*  717 */       removeSettlementMarkers();
/*  718 */       removePerimeterMarkers();
/*  719 */       createQuestion1();
/*  720 */       return false;
/*      */     } 
/*      */     
/*  723 */     this.error = "";
/*  724 */     key = "perimeter";
/*  725 */     val = getAnswer().getProperty(key);
/*  726 */     if (val != null) {
/*      */       
/*      */       try {
/*      */         
/*  730 */         removePerimeterMarkers();
/*  731 */         this.initialPerimeter = Math.max(0, Integer.parseInt(val));
/*  732 */         int xa = Zones.safeTileX(this.tokenx - this.selectedWest - 5 - this.initialPerimeter);
/*  733 */         int xe = Zones.safeTileX(this.tokenx + this.selectedEast + 5 + this.initialPerimeter);
/*  734 */         int ya = Zones.safeTileY(this.tokeny - this.selectedNorth - 5 - this.initialPerimeter);
/*  735 */         int ye = Zones.safeTileY(this.tokeny + this.selectedSouth + 5 + this.initialPerimeter);
/*  736 */         for (int x = xa; x <= xe; x++) {
/*      */           
/*  738 */           for (int y = ya; y <= ye; y++) {
/*      */             
/*  740 */             boolean create = false;
/*  741 */             if (x == xa) {
/*      */               
/*  743 */               if (y == ya || y == ye || y % 10 == 0) {
/*  744 */                 create = true;
/*      */               }
/*  746 */             } else if (x == xe) {
/*      */               
/*  748 */               if (y == ya || y == ye || y % 10 == 0)
/*      */               {
/*  750 */                 create = true;
/*      */               }
/*      */             }
/*  753 */             else if (y == ya || y == ye) {
/*      */               
/*  755 */               if (x % 10 == 0)
/*  756 */                 create = true; 
/*      */             } 
/*  758 */             if (create) {
/*      */               try
/*      */               {
/*      */                 
/*  762 */                 Item i = ItemFactory.createItem(673, 80.0F, getResponder().getName());
/*  763 */                 i.setPosXYZ(((x << 2) + 2), ((y << 2) + 2), 
/*  764 */                     Zones.calculateHeight(((x << 2) + 2), ((y << 2) + 2), true) + 5.0F);
/*  765 */                 Zones.getZone(x, y, true).addItem(i);
/*      */               }
/*  767 */               catch (Exception ex)
/*      */               {
/*  769 */                 logger.log(Level.INFO, ex.getMessage());
/*      */               }
/*      */             
/*      */             }
/*      */           } 
/*      */         } 
/*  775 */       } catch (NumberFormatException nsf) {
/*      */         
/*  777 */         this.error = "Failed to parse the desired perimeter of " + val + " to a valid number.";
/*  778 */         return false;
/*      */       } 
/*      */     }
/*  781 */     setSize();
/*  782 */     if (!checkSize()) {
/*      */       
/*  784 */       createQuestion2();
/*  785 */       return false;
/*      */     } 
/*  787 */     if (this.error.length() > 0) {
/*      */       
/*  789 */       createQuestion2();
/*  790 */       return false;
/*      */     } 
/*  792 */     createQuestion3();
/*  793 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean parseVillageFoundationQuestion3() {
/*  798 */     String key = "back";
/*  799 */     String val = getAnswer().getProperty(key);
/*  800 */     if (val != null && val.equals("true")) {
/*      */       
/*  802 */       createQuestion2();
/*  803 */       removePerimeterMarkers();
/*  804 */       return false;
/*      */     } 
/*  806 */     this.error = "";
/*      */     
/*  808 */     this.villageName = getAnswer().getProperty("vname");
/*  809 */     this.villageName = this.villageName.replaceAll("\"", "");
/*  810 */     this.villageName = this.villageName.trim();
/*      */ 
/*      */     
/*  813 */     if (this.villageName.length() > 3) {
/*      */ 
/*      */       
/*  816 */       this.villageName = StringUtilities.raiseFirstLetter(this.villageName);
/*  817 */       StringTokenizer tokens = new StringTokenizer(this.villageName);
/*  818 */       String newName = tokens.nextToken();
/*  819 */       while (tokens.hasMoreTokens())
/*  820 */         newName = newName + " " + StringUtilities.raiseFirstLetter(tokens.nextToken()); 
/*  821 */       this.villageName = newName;
/*      */     } 
/*      */     
/*  824 */     if (this.expanding) {
/*      */       
/*      */       try {
/*      */         
/*  828 */         Village v = Villages.getVillage(this.deed.getData2());
/*      */         
/*  830 */         if (v.mayChangeName())
/*      */         {
/*  832 */           if (this.villageName.length() >= 41)
/*      */           {
/*  834 */             this.villageName = this.villageName.substring(0, 39);
/*  835 */             this.error += " * The name of the settlement would be ''" + this.villageName + "''. Please select a shorter name.";
/*      */           
/*      */           }
/*  838 */           else if (this.villageName.length() < 3)
/*      */           {
/*  840 */             this.error += " * The name of the settlement would be ''" + this.villageName + "''. Please select a name with at least 3 letters.";
/*      */           
/*      */           }
/*  843 */           else if (QuestionParser.containsIllegalVillageCharacters(this.villageName))
/*      */           {
/*  845 */             this.error += " * The name ''" + this.villageName + "'' contains illegal characters. Please select another name.";
/*      */           }
/*  847 */           else if (this.villageName.equals("Wurm"))
/*      */           {
/*  849 */             this.error += " * The name ''" + this.villageName + "'' is illegal. Please select another name.";
/*      */           }
/*  851 */           else if (!Villages.isNameOk(this.villageName, v.id))
/*      */           {
/*  853 */             this.error += " * The name ''" + this.villageName + "'' is already taken. Please select another name.";
/*      */           }
/*  855 */           else if (!this.villageName.equals(v.getName()))
/*      */           {
/*  857 */             this.changingName = true;
/*      */           }
/*      */         
/*      */         }
/*  861 */       } catch (NoSuchVillageException nsv) {
/*      */         
/*  863 */         getResponder().getCommunicator().sendAlertServerMessage("The settlement no longer exists and the operation failed.");
/*      */         
/*  865 */         return false;
/*      */       } 
/*      */     }
/*  868 */     this.motto = getAnswer().getProperty("motto");
/*  869 */     this.motto = this.motto.replaceAll("\"", "");
/*      */     
/*  871 */     if (this.motto.length() >= 101) {
/*      */       
/*  873 */       this.motto = this.motto.substring(0, 101);
/*  874 */       this.error += " * The motto of the settlement would be ''" + this.motto + "''. Please select a shorter devise.";
/*      */     }
/*  876 */     else if (QuestionParser.containsIllegalCharacters(this.motto)) {
/*      */       
/*  878 */       this.error += " * The motto contains illegal characters. Please select another motto.";
/*  879 */       this.motto = "We use improper characters";
/*      */     }
/*  881 */     else if (!this.expanding) {
/*      */       
/*  883 */       if (this.villageName.length() >= 41) {
/*      */         
/*  885 */         this.villageName = this.villageName.substring(0, 39);
/*  886 */         this.error += " * The name of the settlement would be ''" + this.villageName + "''. Please select a shorter name.";
/*      */       }
/*  888 */       else if (this.villageName.length() < 3) {
/*      */         
/*  890 */         this.error += " * The name of the settlement would be ''" + this.villageName + "''. Please select a name with at least 3 letters.";
/*      */       
/*      */       }
/*  893 */       else if (QuestionParser.containsIllegalVillageCharacters(this.villageName)) {
/*      */         
/*  895 */         this.error += " * The name ''" + this.villageName + "'' contains illegal characters. Please select another name.";
/*      */       }
/*  897 */       else if (this.villageName.equals("Wurm")) {
/*      */         
/*  899 */         this.error += " * The name ''" + this.villageName + "'' is illegal. Please select another name.";
/*      */       }
/*  901 */       else if (!Villages.isNameOk(this.villageName)) {
/*      */         
/*  903 */         this.error += " * The name ''" + this.villageName + "'' is already taken. Please select another name.";
/*      */       } 
/*      */     } 
/*  906 */     this.democracy = false;
/*  907 */     key = "democracy";
/*  908 */     val = getAnswer().getProperty(key);
/*  909 */     if (val != null) {
/*  910 */       this.democracy = val.equals("true");
/*      */     }
/*  912 */     this.permanent = false;
/*  913 */     this.spawnKingdom = 0;
/*  914 */     if (getResponder().getPower() >= 3) {
/*      */       
/*  916 */       key = "permanent";
/*  917 */       val = getAnswer().getProperty(key);
/*  918 */       if (val != null)
/*  919 */         this.permanent = val.equals("true"); 
/*  920 */       val = getAnswer().getProperty("kingdomid");
/*  921 */       if (val != null)
/*  922 */         this.spawnKingdom = Byte.parseByte(val); 
/*      */     } 
/*  924 */     if (this.error.length() > 0) {
/*      */       
/*  926 */       createQuestion3();
/*  927 */       return false;
/*      */     } 
/*  929 */     createQuestion4();
/*  930 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean parseVillageFoundationQuestion4() {
/*  935 */     String key = "back";
/*  936 */     String val = getAnswer().getProperty(key);
/*  937 */     if (val != null && val.equals("true")) {
/*      */       
/*  939 */       createQuestion3();
/*  940 */       return false;
/*      */     } 
/*  942 */     this.error = "";
/*  943 */     key = "guards";
/*  944 */     val = getAnswer().getProperty(key);
/*  945 */     if (val != null && val.length() > 0) {
/*      */       
/*      */       try
/*      */       {
/*  949 */         this.selectedGuards = Math.max(0, Math.min(this.maxGuards, Integer.parseInt(val)));
/*      */       }
/*  951 */       catch (NumberFormatException nsf)
/*      */       {
/*  953 */         this.error = "Failed to parse the desired guards of " + val + " to a valid number.";
/*  954 */         return false;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  959 */       this.error += "* The number of required guards MUST be specified. ";
/*      */     } 
/*  961 */     if (this.error.length() > 0) {
/*      */       
/*  963 */       createQuestion4();
/*  964 */       return false;
/*      */     } 
/*  966 */     createQuestion5();
/*  967 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private long getResizeCostDiff() {
/*  972 */     if (Servers.localServer.isFreeDeeds()) {
/*  973 */       return 0L;
/*      */     }
/*      */     try {
/*  976 */       Village oldvill = Villages.getVillage(this.deed.getData2());
/*  977 */       long moneyNeeded = 0L;
/*  978 */       long moneyToRefund = 0L;
/*  979 */       int diffDeed = this.tiles - oldvill.getNumTiles();
/*  980 */       int diffPerim = this.perimeterTiles - oldvill.getPerimeterNonFreeTiles();
/*  981 */       long costDeedDiff = diffDeed * Villages.TILE_COST;
/*  982 */       long costPerimDiff = diffPerim * Villages.PERIMETER_COST;
/*  983 */       long costTotalDiff = costDeedDiff + costPerimDiff;
/*      */       
/*  985 */       if (costTotalDiff > 0L) {
/*  986 */         moneyNeeded += costTotalDiff;
/*      */       }
/*  988 */       if (this.changingName) {
/*  989 */         moneyNeeded += 50000L;
/*      */       }
/*  991 */       int diffGuard = this.selectedGuards - oldvill.plan.getNumHiredGuards();
/*  992 */       if (diffGuard > 0)
/*      */       {
/*  994 */         moneyNeeded += diffGuard * Villages.GUARD_COST;
/*      */       }
/*  996 */       moneyNeeded -= 0L;
/*  997 */       return moneyNeeded;
/*      */     }
/*  999 */     catch (NoSuchVillageException nsv) {
/*      */       
/* 1001 */       return 0L;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static long getExpandMoneyNeededFromBank(long moneyNeeded, Village village) {
/* 1007 */     long moneyAvailInPlan = village.getAvailablePlanMoney();
/* 1008 */     if (moneyNeeded > moneyAvailInPlan)
/*      */     {
/* 1010 */       return moneyNeeded - moneyAvailInPlan;
/*      */     }
/*      */     
/* 1013 */     return 0L;
/*      */   }
/*      */ 
/*      */   
/*      */   private long getFoundingCost() {
/* 1018 */     long moneyNeeded = this.tiles * Villages.TILE_COST;
/* 1019 */     moneyNeeded += this.perimeterTiles * Villages.PERIMETER_COST;
/* 1020 */     moneyNeeded += this.selectedGuards * Villages.GUARD_COST;
/* 1021 */     return moneyNeeded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long getFoundingCharge() {
/* 1031 */     if (Servers.localServer.isFreeDeeds())
/* 1032 */       return 0L; 
/* 1033 */     return getFoundingCost() + 30000L - ((this.deed.getTemplateId() == 862) ? 0L : 100000L);
/*      */   }
/*      */ 
/*      */   
/*      */   boolean parseVillageFoundationQuestion5() {
/* 1038 */     String key = "back";
/* 1039 */     String val = getAnswer().getProperty("back");
/* 1040 */     if (val != null && val.equals("true")) {
/*      */       
/* 1042 */       createQuestion4();
/* 1043 */       return false;
/*      */     } 
/*      */     
/* 1046 */     long moneyNeeded = getFoundingCost();
/*      */     
/* 1048 */     Village oldvill = null;
/* 1049 */     if (this.expanding)
/*      */       
/*      */       try {
/* 1052 */         oldvill = Villages.getVillage(this.deed.getData2());
/*      */       }
/* 1054 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 1056 */         getResponder().getCommunicator().sendAlertServerMessage("The settlement no longer exists. The settlement form was cancelled.");
/*      */         
/* 1058 */         return false;
/*      */       }  
/* 1060 */     if (this.expanding) {
/* 1061 */       moneyNeeded = getResizeCostDiff();
/*      */     }
/*      */     try {
/* 1064 */       if (!this.expanding)
/*      */       {
/* 1066 */         long toCharge = getFoundingCharge();
/* 1067 */         boolean charge = (toCharge > 0L);
/* 1068 */         long left = (this.deed.getTemplateId() == 862) ? 30000L : 0L;
/* 1069 */         if (toCharge < 0L)
/*      */         {
/*      */           
/* 1072 */           left = 100000L - getFoundingCost();
/*      */         }
/* 1074 */         if (!charge || ((Player)getResponder()).chargeMoney(toCharge)) {
/*      */           
/*      */           try
/*      */           {
/* 1078 */             Village v = Villages.createVillage(Zones.safeTileX(this.tokenx - this.selectedWest), 
/* 1079 */                 Zones.safeTileX(this.tokenx + this.selectedEast), Zones.safeTileY(this.tokeny - this.selectedNorth), 
/* 1080 */                 Zones.safeTileY(this.tokeny + this.selectedSouth), this.tokenx, this.tokeny, this.villageName, 
/* 1081 */                 getResponder(), this.target, this.surfaced, this.democracy, this.motto, this.permanent, this.spawnKingdom, this.initialPerimeter);
/*      */             
/* 1083 */             logger.log(Level.INFO, getResponder().getName() + " founded " + this.villageName + " for " + toCharge + " irons.");
/* 1084 */             Server.getInstance().broadCastSafe(WurmCalendar.getTime(), false);
/* 1085 */             Server.getInstance().broadCastSafe("The settlement of " + this.villageName + " has just been founded by " + 
/* 1086 */                 getResponder().getName() + ".");
/*      */             
/* 1088 */             getResponder().getCommunicator().sendSafeServerMessage("The settlement of " + this.villageName + " has been founded according to your specifications!");
/*      */             
/* 1090 */             this.deed.setDescription(this.villageName);
/* 1091 */             this.deed.setName("Settlement deed");
/*      */             
/* 1093 */             v.setIsHighwayAllowed(v.hasHighway());
/* 1094 */             if (getResponder().getPower() < 5 && this.deed
/* 1095 */               .getTemplateId() != 862) {
/*      */               
/* 1097 */               Shop shop = Economy.getEconomy().getKingsShop();
/* 1098 */               shop.setMoney(shop.getMoney() - (int)(this.deed.getValue() * 0.4F));
/*      */             } 
/* 1100 */             if (left > 0L) {
/* 1101 */               v.plan.updateGuardPlan(0, left, this.selectedGuards);
/*      */             } else {
/* 1103 */               v.plan.updateGuardPlan(0, 30000L, this.selectedGuards);
/*      */             } 
/* 1105 */             createQuestion6();
/*      */           }
/* 1107 */           catch (FailedException fe)
/*      */           {
/* 1109 */             getResponder().getCommunicator().sendSafeServerMessage(fe.getMessage());
/* 1110 */             return false;
/*      */           }
/* 1112 */           catch (NoSuchPlayerException nsp)
/*      */           {
/* 1114 */             logger.log(Level.WARNING, "Failed to create settlement: " + nsp.getMessage(), (Throwable)nsp);
/* 1115 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to locate a resource needed for that request. Please contact administration.");
/*      */             
/* 1117 */             return false;
/*      */           }
/* 1119 */           catch (NoSuchCreatureException nsc)
/*      */           {
/* 1121 */             logger.log(Level.WARNING, "Failed to create settlement: " + nsc.getMessage(), (Throwable)nsc);
/* 1122 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to locate a resource needed for that request. Please contact administration.");
/*      */             
/* 1124 */             return false;
/*      */           }
/* 1126 */           catch (IOException iox)
/*      */           {
/* 1128 */             logger.log(Level.WARNING, "Failed to create settlement:" + iox.getMessage(), iox);
/* 1129 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to locate a resource needed for that request. Please contact administration.");
/*      */             
/* 1131 */             return false;
/*      */           }
/* 1133 */           catch (NoSuchRoleException nsr)
/*      */           {
/* 1135 */             logger.log(Level.WARNING, "Failed to create settlement:" + nsr.getMessage(), (Throwable)nsr);
/* 1136 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to locate a role needed for that request. Please contact administration.");
/*      */             
/* 1138 */             return false;
/*      */           }
/* 1140 */           catch (NoSuchItemException nsi)
/*      */           {
/* 1142 */             logger.log(Level.WARNING, "Failed to create settlement:" + nsi.getMessage(), (Throwable)nsi);
/* 1143 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to located the deed. The operation was aborted.");
/*      */             
/* 1145 */             return false;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1150 */           Change funds = new Change(toCharge);
/* 1151 */           getResponder().getCommunicator().sendAlertServerMessage("You do not have the required " + funds
/* 1152 */               .getChangeString() + " available in your bank account.");
/* 1153 */           removeSettlementMarkers();
/* 1154 */           removePerimeterMarkers();
/* 1155 */           return false;
/*      */         }
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 1161 */         if (oldvill == null) {
/*      */           
/* 1163 */           getResponder().getCommunicator().sendNormalServerMessage("The settlement no longer exists.");
/* 1164 */           return false;
/*      */         } 
/*      */         
/* 1167 */         boolean change = true;
/* 1168 */         if (!this.permanent && moneyNeeded > 0L) {
/*      */           
/* 1170 */           long rest = getExpandMoneyNeededFromBank(moneyNeeded, oldvill);
/*      */           
/* 1172 */           if (rest > 0L)
/*      */           {
/*      */             
/* 1175 */             if (Servers.localServer.testServer) {
/* 1176 */               getResponder().getCommunicator().sendNormalServerMessage("We need " + moneyNeeded + ". " + rest + " must be taken from the bank.");
/*      */             }
/* 1178 */             if (!((Player)getResponder()).chargeMoney(rest)) {
/*      */               
/* 1180 */               change = false;
/*      */ 
/*      */               
/* 1183 */               this.error = "You try to change the settlement size, but your bank account could not be charged. The action was aborted.";
/* 1184 */               removeSettlementMarkers();
/* 1185 */               removePerimeterMarkers();
/* 1186 */               return false;
/*      */             } 
/*      */ 
/*      */             
/* 1190 */             if (Servers.localServer.testServer) {
/* 1191 */               getResponder().getCommunicator().sendNormalServerMessage("We also take " + oldvill
/* 1192 */                   .getAvailablePlanMoney() + " from upkeep.");
/*      */             }
/* 1194 */             oldvill.plan.updateGuardPlan(oldvill.plan.moneyLeft - oldvill.getAvailablePlanMoney());
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */             
/* 1203 */             if (Servers.localServer.testServer) {
/* 1204 */               getResponder().getCommunicator().sendNormalServerMessage("We charge " + moneyNeeded + " from the plan which has " + oldvill.plan.moneyLeft);
/*      */             }
/* 1206 */             oldvill.plan.updateGuardPlan(oldvill.plan.moneyLeft - moneyNeeded);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 1212 */         else if (moneyNeeded < 0L) {
/*      */           
/* 1214 */           LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 1215 */           if (!lsw.addMoney(getResponder().getWurmId(), getResponder().getName(), Math.abs(moneyNeeded), "Resize " + oldvill
/* 1216 */               .getName())) {
/*      */             
/* 1218 */             change = false;
/* 1219 */             logger.log(Level.INFO, "Changing values did not yield money for " + oldvill.getName() + " to " + 
/* 1220 */                 getResponder().getName() + ": " + Math.abs(moneyNeeded) + "?");
/* 1221 */             getResponder()
/* 1222 */               .getCommunicator()
/* 1223 */               .sendAlertServerMessage("You try to change the settlement size, but you could not be reimbursed because we could not reach your bank account. The action was aborted.");
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1228 */         if (change)
/*      */         {
/* 1230 */           StringBuilder builder = new StringBuilder();
/* 1231 */           if (oldvill.getPerimeterSize() != this.initialPerimeter) {
/*      */             
/* 1233 */             builder.append("Perimeter " + this.initialPerimeter + " ");
/* 1234 */             oldvill.setPerimeter(this.initialPerimeter);
/*      */           } 
/* 1236 */           if (oldvill.plan.getNumHiredGuards() != this.selectedGuards) {
/*      */             
/* 1238 */             builder.append("Guards " + this.selectedGuards + " ");
/* 1239 */             oldvill.plan.changePlan(0, this.selectedGuards);
/*      */           } 
/*      */ 
/*      */           
/* 1243 */           if (oldvill.getStartX() != this.tokenx - this.selectedWest || oldvill
/* 1244 */             .getStartY() != this.tokeny - this.selectedNorth || oldvill
/* 1245 */             .getEndX() != this.tokenx + this.selectedEast || oldvill
/* 1246 */             .getEndY() != this.tokeny + this.selectedSouth) {
/*      */             
/* 1248 */             builder.append("From token: " + this.selectedWest + " West to " + this.selectedEast + " East and ");
/* 1249 */             builder.append(this.selectedNorth + " North to " + this.selectedSouth + " South ");
/* 1250 */             oldvill.setNewBounds(Zones.safeTileX(oldvill.getTokenX() - this.selectedWest), 
/* 1251 */                 Zones.safeTileY(oldvill.getTokenY() - this.selectedNorth), 
/* 1252 */                 Zones.safeTileX(oldvill.getTokenX() + this.selectedEast), 
/* 1253 */                 Zones.safeTileY(oldvill.getTokenY() + this.selectedSouth));
/*      */           } 
/* 1255 */           if (!oldvill.getMotto().equals(this.motto)) {
/*      */             
/* 1257 */             oldvill.addHistory(getResponder().getName(), "New motto: " + this.motto);
/* 1258 */             oldvill.setMotto(this.motto);
/*      */           } 
/* 1260 */           if (oldvill.isDemocracy() != this.democracy) {
/*      */             
/* 1262 */             oldvill.setDemocracy(this.democracy);
/* 1263 */             builder.append("Democracy: " + this.democracy);
/*      */           } 
/*      */           
/* 1266 */           if (this.changingName) {
/*      */             
/* 1268 */             oldvill.setName(this.villageName);
/*      */             
/* 1270 */             oldvill.setFaithCreate(0.0F);
/* 1271 */             oldvill.setFaithHeal(0.0F);
/* 1272 */             oldvill.setFaithWar(0.0F);
/* 1273 */             oldvill.setLastChangedName(System.currentTimeMillis());
/* 1274 */             this.deed.setDescription(this.villageName);
/* 1275 */             builder.append("Changed name to " + this.villageName);
/*      */           } 
/*      */           
/* 1278 */           oldvill.addHistory(getResponder().getName(), builder.toString());
/* 1279 */           getResponder().getCommunicator().sendSafeServerMessage("The settlement has been updated according to your specifications!");
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 1284 */     } catch (IOException iox) {
/*      */       
/* 1286 */       logger.log(Level.INFO, "Failed to create settlement:" + iox.getMessage(), iox);
/* 1287 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to charge your account. The operation was aborted.");
/*      */       
/* 1289 */       return false;
/*      */     } 
/* 1291 */     if (this.error.length() > 0) {
/*      */       
/* 1293 */       createQuestion5();
/* 1294 */       return false;
/*      */     } 
/* 1296 */     removeSettlementMarkers();
/* 1297 */     removePerimeterMarkers();
/* 1298 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean checkSize() {
/* 1303 */     if (getResponder().getPower() < 3)
/*      */     {
/* 1305 */       if (Features.Feature.TOWER_CHAINING.isEnabled()) {
/*      */ 
/*      */         
/* 1308 */         InfluenceChain chain = InfluenceChain.getInfluenceChain(getResponder().getKingdomId());
/* 1309 */         boolean found = false;
/* 1310 */         for (Item marker : chain.getChainMarkers()) {
/*      */ 
/*      */           
/* 1313 */           if (!marker.isChained() && getResponder().getKingdomId() != 4) {
/*      */             continue;
/*      */           }
/*      */           
/* 1317 */           if (!marker.isGuardTower()) {
/*      */             continue;
/*      */           }
/* 1320 */           if (Math.abs(this.tokenx - marker.getTileX()) <= 50)
/*      */           {
/* 1322 */             if (Math.abs(this.tokeny - marker.getTileY()) <= 50) {
/*      */ 
/*      */               
/* 1325 */               found = true;
/*      */               break;
/*      */             } 
/*      */           }
/*      */         } 
/* 1330 */         if (!found)
/*      */         {
/*      */           
/* 1333 */           getResponder().getCommunicator().sendSafeServerMessage("You must found the settlement within 50 tiles of an allied tower linked to the kingdom influence chain.");
/*      */           
/* 1335 */           return false;
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1341 */         boolean found = false;
/* 1342 */         for (int x = this.tokenx - 50; x <= this.tokenx + 50; x += 10) {
/*      */           
/* 1344 */           for (int y = this.tokeny - 50; y <= this.tokeny + 50; y += 10) {
/*      */             
/* 1346 */             if (Zones.getKingdom(this.tokenx, this.tokeny) == getResponder().getKingdomId()) {
/*      */               
/* 1348 */               found = true;
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/* 1353 */         if (!found) {
/*      */           
/* 1355 */           getResponder().getCommunicator().sendSafeServerMessage("You must found the settlement within 50 tiles of your own kingdom.");
/*      */           
/* 1357 */           return false;
/*      */         } 
/*      */       } 
/*      */     }
/* 1361 */     Village oldvill = null;
/* 1362 */     if (this.expanding) {
/*      */       
/*      */       try {
/* 1365 */         oldvill = Villages.getVillage(this.deed.getData2());
/*      */       }
/* 1367 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 1369 */         getResponder().getCommunicator().sendSafeServerMessage("The settlement could not be located.");
/* 1370 */         return false;
/*      */       } 
/*      */     }
/* 1373 */     Map<Village, String> decliners = Villages.canFoundVillage(this.selectedWest, this.selectedEast, this.selectedNorth, this.selectedSouth, this.tokenx, this.tokeny, this.initialPerimeter, true, oldvill, 
/* 1374 */         getResponder());
/* 1375 */     if (!decliners.isEmpty()) {
/*      */       
/* 1377 */       getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here:");
/* 1378 */       for (Village vill : decliners.keySet()) {
/*      */         
/* 1380 */         String reason = decliners.get(vill);
/*      */         
/* 1382 */         if (reason.startsWith("has perimeter")) {
/* 1383 */           getResponder().getCommunicator().sendSafeServerMessage(vill.getName() + " " + reason); continue;
/*      */         } 
/* 1385 */         getResponder().getCommunicator().sendSafeServerMessage("Some settlement nearby " + reason);
/*      */       } 
/* 1387 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1391 */     boolean checkFocusZones = !this.expanding;
/* 1392 */     if (this.expanding && oldvill != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1399 */       if (oldvill.getStartX() != this.tokenx - this.selectedWest)
/* 1400 */         checkFocusZones = true; 
/* 1401 */       if (oldvill.getStartY() != this.tokeny - this.selectedNorth)
/* 1402 */         checkFocusZones = true; 
/* 1403 */       if (oldvill.getEndX() != this.tokenx + this.selectedEast)
/* 1404 */         checkFocusZones = true; 
/* 1405 */       if (oldvill.getEndY() != this.tokenx + this.selectedNorth)
/* 1406 */         checkFocusZones = true; 
/* 1407 */       if (oldvill.getPerimeterDiameterX() != this.perimeterDiameterX)
/* 1408 */         checkFocusZones = true; 
/* 1409 */       if (oldvill.getPerimeterDiameterY() != this.perimeterDiameterY)
/* 1410 */         checkFocusZones = true; 
/*      */     } 
/* 1412 */     if (checkFocusZones) {
/*      */       
/* 1414 */       String focusZoneReject = Villages.isFocusZoneBlocking(this.selectedWest, this.selectedEast, this.selectedNorth, this.selectedSouth, this.tokenx, this.tokeny, this.initialPerimeter, true);
/*      */       
/* 1416 */       if (focusZoneReject.length() > 0) {
/*      */         
/* 1418 */         getResponder().getCommunicator().sendSafeServerMessage(focusZoneReject);
/* 1419 */         return false;
/*      */       } 
/*      */     } 
/* 1422 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSize() {
/* 1427 */     this.diameterX = this.selectedWest + this.selectedEast + 1;
/* 1428 */     this.diameterY = this.selectedNorth + this.selectedSouth + 1;
/* 1429 */     this.maxGuards = GuardPlan.getMaxGuards(this.diameterX, this.diameterY);
/* 1430 */     this.tiles = this.diameterX * this.diameterY;
/*      */     
/* 1432 */     this.perimeterDiameterX = this.diameterX + 5 + 5 + this.initialPerimeter + this.initialPerimeter;
/*      */     
/* 1434 */     this.perimeterDiameterY = this.diameterY + 5 + 5 + this.initialPerimeter + this.initialPerimeter;
/*      */     
/* 1436 */     this.perimeterTiles = this.perimeterDiameterX * this.perimeterDiameterY - (this.diameterX + 5 + 5) * (this.diameterY + 5 + 5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkToken() {
/* 1443 */     if (!Villages.mayCreateTokenOnTile(true, this.tokenx, this.tokeny)) {
/*      */       
/* 1445 */       getResponder()
/* 1446 */         .getCommunicator()
/* 1447 */         .sendNormalServerMessage("You may not found the settlement there. A fence or a wall is nearby. The token is a public affair and must not be restricted access.");
/*      */       
/* 1449 */       return false;
/*      */     } 
/* 1451 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkDeedItem() {
/*      */     try {
/* 1458 */       this.deed = Items.getItem(this.target);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1465 */       if (!this.deed.isNewDeed() && !Servers.localServer.testServer && this.deed
/* 1466 */         .getTemplateId() != 862) {
/*      */         
/* 1468 */         getResponder().getCommunicator().sendNormalServerMessage("This " + this.deed.getName() + " may no longer be used.");
/* 1469 */         return false;
/*      */       } 
/* 1471 */       return true;
/*      */     }
/* 1473 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1475 */       logger.log(Level.WARNING, "Failed to locate settlement deed with id " + this.target, (Throwable)nsi);
/* 1476 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*      */ 
/*      */       
/* 1479 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean checkBlockingCreatures() {
/* 1485 */     Village currVill = null;
/* 1486 */     if (this.expanding) {
/*      */       
/*      */       try {
/*      */         
/* 1490 */         currVill = Villages.getVillage(this.deed.getData2());
/*      */       }
/* 1492 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 1494 */         return false;
/*      */       } 
/*      */     }
/* 1497 */     Object cret = Villages.isAggOnDeed(currVill, getResponder(), this.selectedWest, this.selectedEast, this.selectedNorth, this.selectedSouth, this.tokenx, this.tokeny, true);
/*      */     
/* 1499 */     if (cret != null) {
/*      */       
/* 1501 */       getResponder()
/* 1502 */         .getCommunicator()
/* 1503 */         .sendSafeServerMessage("You cannot found the settlement here, since there are dangerous aggressive creatures or dens in the area.");
/*      */       
/* 1505 */       return false;
/*      */     } 
/* 1507 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean checkBlockingKingdoms() {
/* 1512 */     if (getResponder().getPower() < 2) {
/*      */       
/* 1514 */       int mindist = Kingdoms.minKingdomDist;
/* 1515 */       if (Zones.getKingdom(this.tokenx, this.tokeny) == getResponder().getKingdomId()) {
/* 1516 */         mindist = 60;
/*      */       }
/* 1518 */       int eZoneStartX = -1;
/* 1519 */       int eZoneStartY = -1;
/* 1520 */       int eZoneEndX = -1;
/* 1521 */       int eZoneEndY = -1;
/* 1522 */       Village existing = Villages.getVillage(this.tokenx, this.tokeny, true);
/* 1523 */       if (existing != null) {
/*      */         
/* 1525 */         eZoneStartX = existing.getStartX() - 5 - existing.getPerimeterSize() - mindist;
/* 1526 */         eZoneStartY = existing.getStartY() - 5 - existing.getPerimeterSize() - mindist;
/* 1527 */         eZoneEndX = existing.getEndX() + 5 + existing.getPerimeterSize() + mindist;
/* 1528 */         eZoneEndY = existing.getEndY() + 5 + existing.getPerimeterSize() + mindist;
/*      */       } 
/*      */ 
/*      */       
/* 1532 */       return Zones.isKingdomBlocking(this.tokenx - this.selectedWest - 5 - this.initialPerimeter - mindist, this.tokeny - this.selectedNorth - 5 - this.initialPerimeter - mindist, this.tokenx + this.selectedEast + 5 + this.initialPerimeter + mindist, this.tokeny + this.selectedSouth + 5 + this.initialPerimeter + mindist, 
/*      */ 
/*      */ 
/*      */           
/* 1536 */           getResponder().getKingdomId(), eZoneStartX, eZoneStartY, eZoneEndX, eZoneEndY);
/*      */     } 
/* 1538 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean checkBlockingItems() {
/* 1543 */     if (getResponder().getPower() < 2) {
/*      */       
/* 1545 */       int maxnorth = Math.max(0, this.tokeny - this.selectedNorth - (
/* 1546 */           Servers.localServer.isChallengeServer() ? 20 : Kingdoms.minKingdomDist));
/* 1547 */       int maxsouth = Math.min(Zones.worldTileSizeY, this.tokeny + this.selectedSouth + (
/* 1548 */           Servers.localServer.isChallengeServer() ? 20 : Kingdoms.minKingdomDist));
/*      */       
/* 1550 */       int maxwest = Math.max(0, this.tokenx - this.selectedWest - (
/* 1551 */           Servers.localServer.isChallengeServer() ? 20 : Kingdoms.minKingdomDist));
/* 1552 */       int maxeast = Math.min(Zones.worldTileSizeX, this.tokenx + this.selectedEast + (
/* 1553 */           Servers.localServer.isChallengeServer() ? 20 : Kingdoms.minKingdomDist));
/* 1554 */       int maxcnorth = Math.max(0, this.tokeny - this.selectedNorth - (
/* 1555 */           Servers.localServer.isChallengeServer() ? 20 : 60));
/* 1556 */       int maxcsouth = Math.min(Zones.worldTileSizeY, this.tokeny + this.selectedSouth + (
/*      */ 
/*      */           
/* 1559 */           Servers.localServer.isChallengeServer() ? 20 : 60));
/*      */       
/* 1561 */       int maxcwest = Math.max(0, this.tokenx - this.selectedWest - (
/* 1562 */           Servers.localServer.isChallengeServer() ? 20 : 60));
/* 1563 */       int maxceast = Math.min(Zones.worldTileSizeX, this.tokenx + this.selectedEast + (
/*      */ 
/*      */           
/* 1566 */           Servers.localServer.isChallengeServer() ? 20 : 60));
/*      */       
/* 1568 */       Rectangle2D rectangleToCheck = new Rectangle2D.Float(maxwest, maxnorth, (maxeast - maxwest), (maxsouth - maxnorth));
/* 1569 */       for (Item targ : Items.getWarTargets()) {
/*      */         
/* 1571 */         if (targ.getTileX() > maxcwest && targ.getTileX() < maxceast && targ
/* 1572 */           .getTileY() < maxcsouth && targ.getTileY() > maxcnorth) {
/*      */           
/* 1574 */           getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since this is a battle ground.");
/*      */           
/* 1576 */           return false;
/*      */         } 
/*      */       } 
/*      */       
/* 1580 */       for (Item targ : Items.getSupplyDepots()) {
/*      */         
/* 1582 */         if (targ.getTileX() > maxcwest && targ.getTileX() < maxceast && targ
/* 1583 */           .getTileY() < maxcsouth && targ.getTileY() > maxcnorth) {
/*      */           
/* 1585 */           getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since this is a battle ground.");
/*      */           
/* 1587 */           return false;
/*      */         } 
/*      */       } 
/* 1590 */       Item altar = Villages.isAltarOnDeed(this.selectedWest, this.selectedEast, this.selectedNorth, this.selectedSouth, this.tokenx, this.tokeny, true);
/*      */       
/* 1592 */       if (altar != null)
/*      */       {
/* 1594 */         if (altar.isEpicTargetItem() && Servers.localServer.PVPSERVER) {
/*      */           
/* 1596 */           if (EpicServerStatus.getRitualMissionForTarget(altar.getWurmId()) != null)
/*      */           {
/* 1598 */             getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since the " + altar
/* 1599 */                 .getName() + " is currently required for an active mission.");
/* 1600 */             return false;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1605 */           getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since the " + altar
/* 1606 */               .getName() + " makes it holy ground.");
/* 1607 */           return false;
/*      */         } 
/*      */       }
/* 1610 */       EndGameItem alt = EndGameItems.getGoodAltar();
/* 1611 */       if (alt != null)
/*      */       {
/* 1613 */         if (alt.getItem() != null)
/*      */         {
/* 1615 */           if ((int)alt.getItem().getPosX() >> 2 > maxwest && (int)alt.getItem().getPosX() >> 2 < maxeast && 
/* 1616 */             (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth) {
/*      */             
/* 1618 */             getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since this is holy ground.");
/*      */             
/* 1620 */             return false;
/*      */           } 
/*      */         }
/*      */       }
/* 1624 */       alt = EndGameItems.getEvilAltar();
/* 1625 */       if (alt != null)
/*      */       {
/* 1627 */         if (alt.getItem() != null)
/*      */         {
/* 1629 */           if ((int)alt.getItem().getPosX() >> 2 > maxwest && (int)alt.getItem().getPosX() >> 2 < maxeast && 
/* 1630 */             (int)alt.getItem().getPosY() >> 2 < maxsouth && (int)alt.getItem().getPosY() >> 2 > maxnorth) {
/*      */             
/* 1632 */             getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since this is holy ground.");
/*      */             
/* 1634 */             return false;
/*      */           } 
/*      */         }
/*      */       }
/*      */       
/* 1639 */       if (Zones.isWithinDuelRing(this.tokenx - this.selectedWest - 5 - this.initialPerimeter, this.tokeny - this.selectedNorth - 5 - this.initialPerimeter, this.tokenx + this.selectedEast + 5 + this.initialPerimeter, this.tokeny + this.selectedSouth + 5 + this.initialPerimeter)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1644 */         getResponder().getCommunicator().sendSafeServerMessage("You cannot found the settlement here, since the duelling ring is holy ground.");
/*      */         
/* 1646 */         return false;
/*      */       } 
/*      */     } 
/* 1649 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean checkStructuresInArea() {
/* 1654 */     if (getResponder().getPower() <= 1) {
/*      */       
/* 1656 */       ArrayList<Structure> newStructures = new ArrayList<>();
/* 1657 */       ArrayList<Structure> oldStructures = new ArrayList<>();
/*      */       
/* 1659 */       Structure[] surfStructures = Zones.getStructuresInArea(this.tokenx - this.selectedWest - 2, this.tokeny - this.selectedNorth - 2, this.tokenx + this.selectedEast + 2, this.tokeny + this.selectedSouth + 2, true);
/*      */       
/* 1661 */       Structure[] caveStructures = Zones.getStructuresInArea(this.tokenx - this.selectedWest - 2, this.tokeny - this.selectedNorth - 2, this.tokenx + this.selectedEast + 2, this.tokeny + this.selectedSouth + 2, false);
/*      */       
/* 1663 */       for (Structure c : surfStructures)
/* 1664 */         newStructures.add(c); 
/* 1665 */       for (Structure c : caveStructures) {
/* 1666 */         newStructures.add(c);
/*      */       }
/* 1668 */       if (this.expanding) {
/*      */         
/*      */         try {
/*      */           
/* 1672 */           Village oldvill = Villages.getVillage(this.deed.getData2());
/* 1673 */           Structure[] surfStructuresOld = Zones.getStructuresInArea(oldvill.getStartX(), oldvill
/* 1674 */               .getStartY(), oldvill.getEndX(), oldvill.getEndY(), true);
/* 1675 */           Structure[] caveStructuresOld = Zones.getStructuresInArea(oldvill.getStartX(), oldvill
/* 1676 */               .getStartY(), oldvill.getEndX(), oldvill.getEndY(), true);
/* 1677 */           for (Structure c : surfStructuresOld)
/* 1678 */             oldStructures.add(c); 
/* 1679 */           for (Structure c : caveStructuresOld) {
/* 1680 */             oldStructures.add(c);
/*      */           }
/* 1682 */         } catch (NoSuchVillageException noSuchVillageException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1687 */       if (newStructures.size() > 0) {
/*      */         
/* 1689 */         boolean ok = false;
/*      */ 
/*      */         
/* 1692 */         for (Structure lStructure : newStructures) {
/*      */           
/* 1694 */           ok = false;
/* 1695 */           if (this.expanding)
/*      */           {
/*      */             
/* 1698 */             for (Structure lOldstructure : oldStructures) {
/*      */               
/* 1700 */               if (lOldstructure.getWurmId() == lStructure.getWurmId()) {
/*      */                 
/* 1702 */                 ok = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           }
/* 1707 */           if (!ok)
/*      */           {
/*      */             
/* 1710 */             if (lStructure.isTypeHouse() && !lStructure.mayManage(getResponder())) {
/*      */               
/* 1712 */               getResponder().getCommunicator().sendSafeServerMessage("You need to have manage permissions for the structure " + lStructure
/* 1713 */                   .getName() + " to found the settlement here.");
/*      */               
/* 1715 */               return false;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1721 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addTileCost(StringBuilder buf) {
/* 1726 */     buf.append("text{text=\"You selected a size of " + this.diameterX + " by " + this.diameterY + ".\"}");
/*      */     
/* 1728 */     if (!this.expanding) {
/*      */       
/* 1730 */       if (!Servers.localServer.isFreeDeeds())
/* 1731 */         buf.append("text{text=\"The Purchase price for these tiles are " + (new Change(this.tiles * Villages.TILE_COST))
/* 1732 */             .getChangeString() + ".\"}"); 
/* 1733 */       if (Servers.localServer.isUpkeep()) {
/* 1734 */         buf.append("text{text=\"The Monthly upkeep is " + (new Change(this.tiles * Villages.TILE_UPKEEP)).getChangeString() + ".\"}");
/*      */       }
/* 1736 */       buf.append("text{text=\"\"}");
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1742 */         Village oldvill = Villages.getVillage(this.deed.getData2());
/* 1743 */         int diff = this.tiles - oldvill.getNumTiles();
/* 1744 */         if (diff > 0 && !Servers.localServer.isFreeDeeds()) {
/* 1745 */           buf.append("text{text=\"The initial cost for the tiles will be " + (new Change(diff * Villages.TILE_COST))
/* 1746 */               .getChangeString() + ".\"}");
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1751 */         if (Servers.localServer.isUpkeep())
/* 1752 */           buf.append("text{text=\"The new monthly upkeep cost for the tiles will be " + (new Change(this.tiles * Villages.TILE_UPKEEP))
/* 1753 */               .getChangeString() + ".\"}"); 
/* 1754 */         buf.append("text{text=\"\"}");
/*      */       }
/* 1756 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 1758 */         buf.append("text{text=\"The settlement for this deed was not found, so nothing will happen.\"}");
/*      */       } 
/*      */     } 
/*      */     
/* 1762 */     this.totalUpkeep = this.tiles * Villages.TILE_UPKEEP;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addChangeNameCost(StringBuilder buf) {
/* 1767 */     if (this.changingName && !Servers.localServer.isFreeDeeds())
/*      */     {
/* 1769 */       buf.append("text{text=\"The cost for changing name is 5 silver.\"}");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void addPerimeterCost(StringBuilder buf) {
/* 1775 */     if (!this.expanding) {
/*      */ 
/*      */       
/* 1778 */       buf.append("text{text=\"You have selected a perimeter of 5" + (
/* 1779 */           Servers.localServer.isFreeDeeds() ? "" : " free") + " tiles plus " + this.initialPerimeter + " additional tiles from your settlement boundary.\"}");
/*      */       
/* 1781 */       if (this.initialPerimeter > 0) {
/*      */         
/* 1783 */         if (!Servers.localServer.isFreeDeeds())
/* 1784 */           buf.append("text{text=\"The initial cost for the perimeter tiles will be " + (new Change(this.perimeterTiles * Villages.PERIMETER_COST))
/* 1785 */               .getChangeString() + ".\"}"); 
/* 1786 */         if (Servers.localServer.isUpkeep())
/* 1787 */           buf.append("text{text=\"The monthly upkeep cost for the perimeter tiles will be " + (new Change(this.perimeterTiles * Villages.PERIMETER_UPKEEP))
/* 1788 */               .getChangeString() + ".\"}"); 
/*      */       } 
/* 1790 */       buf.append("text{text=\"\"}");
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1796 */         Village oldvill = Villages.getVillage(this.deed.getData2());
/* 1797 */         buf.append("text{text=\"You selected a perimeter size of " + this.initialPerimeter + " outside of the free " + '\005' + " tiles.\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1806 */         int diff = this.perimeterTiles - oldvill.getPerimeterNonFreeTiles();
/* 1807 */         if (diff > 0 && !Servers.localServer.isFreeDeeds()) {
/* 1808 */           buf.append("text{text=\"The additional cost for the extra perimeter tiles will be " + (new Change(diff * Villages.PERIMETER_COST))
/* 1809 */               .getChangeString() + ".\"}");
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1814 */         if (Servers.localServer.isUpkeep())
/*      */         {
/* 1816 */           if (this.initialPerimeter > 0) {
/* 1817 */             buf.append("text{text=\"The new monthly upkeep cost for the perimeter tiles will be " + (new Change(this.perimeterTiles * Villages.PERIMETER_UPKEEP))
/* 1818 */                 .getChangeString() + ".\"}");
/* 1819 */           } else if (diff < 0) {
/* 1820 */             buf.append("text{text=\"The monthly upkeep cost for perimeter tiles will go away now.\"}");
/*      */           }  } 
/* 1822 */         buf.append("text{text=\"\"}");
/*      */       }
/* 1824 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 1826 */         buf.append("text{text=\"The settlement for this deed was not found, so nothing will happen.\"}");
/*      */       } 
/*      */     } 
/*      */     
/* 1830 */     this.totalUpkeep += this.perimeterTiles * Villages.PERIMETER_UPKEEP;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addCitizenMultiplier(StringBuilder buf) {
/* 1835 */     buf.append("text{type=\"bold\";text=\"Notes\"}");
/* 1836 */     buf.append("text{type=\"italic\";text=\"The maximum number of citizens, including guards, is " + (this.tiles / 11) + "\"}");
/* 1837 */     if (!Servers.isThisAPvpServer())
/* 1838 */       buf.append("text{type=\"italic\";text=\"The maximum number of branded animals is " + (this.tiles / 11) + "\"}"); 
/* 1839 */     buf.append("text{text=\"\"}");
/* 1840 */     if (this.expanding) {
/*      */ 
/*      */       
/*      */       try {
/* 1844 */         Village old = Villages.getVillage(this.deed.getData2());
/* 1845 */         Citizen[] citizens = old.getCitizens();
/* 1846 */         int curr = 0;
/*      */         
/* 1848 */         for (Citizen lCitizen : citizens) {
/*      */           
/* 1850 */           if (WurmId.getType(lCitizen.wurmId) == 0)
/* 1851 */             curr++; 
/*      */         } 
/* 1853 */         buf.append("text{text=\"You have " + curr + " player citizens.\"}");
/* 1854 */         if (Servers.localServer.isUpkeep())
/*      */         {
/* 1856 */           if (curr > this.tiles / 11) {
/*      */             
/* 1858 */             buf.append("text{text=\"Since you have more than the max amount of citizens (" + (this.tiles / 11) + "), upkeep costs are doubled.\"}");
/*      */             
/* 1860 */             this.totalUpkeep *= 2L;
/*      */           } else {
/*      */             
/* 1863 */             buf.append("text{text=\"If you exceed the max amount of citizens (" + (this.tiles / 11) + "), upkeep costs will be doubled.\"}");
/*      */           }
/*      */         
/*      */         }
/* 1867 */       } catch (NoSuchVillageException nsv) {
/*      */         
/* 1869 */         logger.log(Level.WARNING, nsv.getMessage());
/*      */       }
/*      */     
/* 1872 */     } else if (Servers.localServer.isUpkeep()) {
/* 1873 */       buf.append("text{text=\"If you exceed the max amount of citizens (" + (this.tiles / 11) + "), upkeep costs will be doubled.\"}");
/*      */     } 
/* 1875 */     if (this.totalUpkeep < Villages.MINIMUM_UPKEEP && Servers.localServer.isUpkeep()) {
/*      */       
/* 1877 */       this.totalUpkeep = Villages.MINIMUM_UPKEEP;
/* 1878 */       buf.append("text{text=\"Upkeep is always minimum " + Villages.MINIMUM_UPKEEP_STRING + ".\"}");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addTotalUpkeep(StringBuilder buf) {
/* 1884 */     if (Servers.localServer.isUpkeep()) {
/* 1885 */       buf.append("text{text=\"Total upkeep per month will be " + (new Change(this.totalUpkeep)).getChangeString() + ".\"}");
/*      */     }
/*      */   }
/*      */   
/*      */   private void addGuardCost(StringBuilder buf) {
/* 1890 */     if (!this.expanding) {
/*      */       
/* 1892 */       if (this.selectedGuards > 0) {
/*      */         
/* 1894 */         if (Servers.localServer.isFreeDeeds()) {
/* 1895 */           buf.append("text{text=\"You will hire " + this.selectedGuards + " guards.\"}");
/*      */         } else {
/* 1897 */           buf.append("text{text=\"You will hire " + this.selectedGuards + " guards for a cost of " + (new Change(this.selectedGuards * Villages.GUARD_COST))
/* 1898 */               .getChangeString() + ".\"}");
/*      */         } 
/* 1900 */         if (Servers.localServer.isUpkeep()) {
/* 1901 */           buf.append("text{text=\"The guard upkeep will be " + (new Change(
/* 1902 */                 GuardPlan.getCostForGuards(this.selectedGuards))).getChangeString() + ".\"}");
/*      */         }
/*      */       } else {
/* 1905 */         buf.append("text{text=\"You decide to hire no guards right now.\"}");
/*      */       } 
/* 1907 */       buf.append("text{text=\"\"}");
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1913 */         Village oldvill = Villages.getVillage(this.deed.getData2());
/* 1914 */         int diff = this.selectedGuards - oldvill.plan.getNumHiredGuards();
/* 1915 */         if (diff > 0) {
/*      */           
/* 1917 */           if (Servers.localServer.isFreeDeeds()) {
/* 1918 */             buf.append("text{text=\"You will hire " + diff + " new guards.\"}");
/*      */           } else {
/* 1920 */             buf.append("text{text=\"You will hire " + diff + " new guards for a cost of " + (new Change(diff * Villages.GUARD_COST))
/* 1921 */                 .getChangeString() + ".\"}");
/* 1922 */           }  if (Servers.localServer.isUpkeep()) {
/* 1923 */             buf.append("text{text=\"The new guard upkeep will be " + (new Change(
/* 1924 */                   GuardPlan.getCostForGuards(this.selectedGuards))).getChangeString() + ".\"}");
/*      */           }
/*      */         } else {
/*      */           
/* 1928 */           buf.append("text{text=\"You will dismiss " + Math.abs(diff) + " guards.\"}");
/* 1929 */           if (Servers.localServer.isUpkeep())
/* 1930 */             buf.append("text{text=\"The new guard upkeep will be " + (new Change(
/* 1931 */                   GuardPlan.getCostForGuards(this.selectedGuards))).getChangeString() + ".\"}"); 
/*      */         } 
/* 1933 */         buf.append("text{text=\"\"}");
/*      */       }
/* 1935 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 1937 */         buf.append("text{text=\"The settlement for this deed was not found, so nothing will happen.\"}");
/*      */       } 
/*      */     } 
/* 1940 */     this.totalUpkeep += GuardPlan.getCostForGuards(this.selectedGuards);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendIntro() {
/* 1945 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 1946 */     buf.append("text{text=\"\"}");
/* 1947 */     String fs = "founding";
/* 1948 */     if (this.expanding)
/*      */     {
/* 1950 */       fs = "resizing";
/*      */     }
/* 1952 */     buf.append("text{type=\"italic\";text=\"This form will take you through the process of " + fs + " your settlement. You will be asked how large you want your settlement to be, if you want to buy a larger perimeter, what you want to call your new settlement and how many guards you wish to hire. Please be aware that there is no refund for purchased tiles.\"}");
/*      */ 
/*      */ 
/*      */     
/* 1956 */     buf.append("text{text=\"\"}");
/* 1957 */     if (Servers.localServer.isChallengeOrEpicServer()) {
/*      */       
/* 1959 */       buf.append("text{type=\"bold\";color=\"255,0,0\";text=\"Note that deeds in the Epic and Challenge cluster may become harmed by natural events. It is rare but may happen.\"}");
/* 1960 */       buf.append("text{type=\"bold\";color=\"255,0,0\";text=\"In case you are not prepared for this, please resell this form.\"}");
/* 1961 */       buf.append("text{text=\"\"}");
/*      */     } 
/* 1963 */     buf.append("text{type=\"bold\";text=\"What you need first:\"}");
/* 1964 */     buf.append("text{text=\"\"}");
/* 1965 */     buf.append("text{type=\"italic\";text=\"  1. Have may Manage permission for all buildings within your new settlement area.\"}");
/* 1966 */     buf.append("text{type=\"italic\";text=\"  2. To check there are no large animals such as spiders in your local area (including underground).\"}");
/* 1967 */     buf.append("text{type=\"italic\";text=\"  3. To check there are no lairs in your local area.\"}");
/* 1968 */     if (!Servers.localServer.isFreeDeeds())
/* 1969 */       buf.append("text{type=\"italic\";text=\"  4. Have sufficient funds in your in-game bank account.\"}"); 
/* 1970 */     buf.append("text{text=\"\"}");
/* 1971 */     buf.append("text{type=\"bold\";text=\"How to use this form\"}");
/* 1972 */     buf.append("text{text=\"\"}");
/* 1973 */     buf.append("text{type=\"italic\";text=\"Each stage of the process is on its own page. This is so we can make some checks as we go along and also give you a running commentary as to cost.\"}");
/*      */     
/* 1975 */     buf.append("text{type=\"italic\";text=\"At the bottom of the screen are additional notes and explanations if you need them.\"}");
/* 1976 */     buf.append("text{text=\"\"}");
/* 1977 */     buf.append("text{type=\"italic\";text=\"You can abort this process by closing the window or selecting cancel on the next screen.\"}");
/* 1978 */     buf.append("text{text=\"\"}");
/* 1979 */     buf.append("text{type=\"italic\";text=\"Okay, lets get started!\"}");
/* 1980 */     buf.append("text{text=\"\"}");
/*      */     
/* 1982 */     buf.append("harray {button{text=\"Start the Settlement Application\";id=\"submit\"}}}};null;null;}");
/*      */     
/* 1984 */     getResponder().getCommunicator().sendBml(600, 500, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestion() {
/* 1991 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*      */     
/* 1993 */     if (!checkDeedItem()) {
/*      */       
/* 1995 */       buf.append("text{text=\"There is a problem with the " + this.deed.getName() + " used.\"}");
/*      */     }
/* 1997 */     else if (this.deed.isNewDeed() || this.deed.getTemplateId() == 862) {
/*      */       
/* 1999 */       buf.append("text{text=\"\"}");
/* 2000 */       if (this.error != null && this.error.length() > 0) {
/*      */         
/* 2002 */         buf.append("text{color=\"255,0,0\";text=\"" + this.error + "\"}");
/* 2003 */         buf.append("text{text=\"\"}");
/*      */       } 
/* 2005 */       buf.append("text{type=\"italic\";text=\"Note: This is the main settlement, NOT the perimeter.\"}");
/* 2006 */       buf.append("text{text=\"\"}");
/* 2007 */       String from = " you ";
/* 2008 */       if (!this.expanding) {
/*      */         
/* 2010 */         buf.append("text{text=\"Please stand on the tile where you want your new settlement token to be.\"}");
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 2016 */           Village old = Villages.getVillage(this.deed.getData2());
/* 2017 */           this.tokenx = old.getTokenX();
/* 2018 */           this.tokeny = old.getTokenY();
/* 2019 */           this.selectedWest = this.tokenx - old.getStartX();
/* 2020 */           this.selectedEast = old.getEndX() - this.tokenx;
/* 2021 */           this.selectedNorth = this.tokeny - old.getStartY();
/* 2022 */           this.selectedSouth = old.getEndY() - this.tokeny;
/*      */ 
/*      */           
/* 2025 */           from = " the token ";
/*      */         
/*      */         }
/* 2028 */         catch (NoSuchVillageException noSuchVillageException) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2033 */       buf.append("text{text=\"Please enter in the boxes below the distances in tiles between" + from + "and the border of your settlement. Example: 5, 5, 6, 6 will create a deed 11 tiles by 13 tiles. Minimum is 5.\"}");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2038 */       buf.append("text{text=\"\"}");
/* 2039 */       buf.append("text{text=\"");
/* 2040 */       if (!Servers.localServer.isFreeDeeds()) {
/* 2041 */         buf.append("One tile costs " + Villages.TILE_COST_STRING + " initially. ");
/*      */       } else {
/* 2043 */         buf.append("There is no initial cost for deeding here. ");
/* 2044 */       }  if (Servers.localServer.isUpkeep()) {
/* 2045 */         buf.append("Every month the upkeep per tile is " + Villages.TILE_UPKEEP_STRING + ".");
/*      */       } else {
/* 2047 */         buf.append("There are no monthly upkeep charges here.");
/* 2048 */       }  buf.append("\"}");
/*      */       
/* 2050 */       if (!this.hasCompass && !this.expanding) {
/*      */         
/* 2052 */         if (this.dir == 0)
/*      */         {
/* 2054 */           buf.append("harray{label{text=\"Settlement size left:\"}");
/* 2055 */           buf.append("input{maxchars=\"3\";id=\"sizeW\";text=\"" + this.selectedWest + "\"}");
/* 2056 */           buf.append("label{text=\" right:\"}");
/* 2057 */           buf.append("input{maxchars=\"3\";id=\"sizeE\";text=\"" + this.selectedEast + "\"}");
/* 2058 */           buf.append("}");
/* 2059 */           buf.append("harray{label{text=\"Settlement size front:\"}");
/* 2060 */           buf.append("input{maxchars=\"3\";id=\"sizeN\";text=\"" + this.selectedNorth + "\"}");
/* 2061 */           buf.append("label{text=\" back:\"}");
/* 2062 */           buf.append("input{maxchars=\"3\";id=\"sizeS\";text=\"" + this.selectedSouth + "\"}");
/* 2063 */           buf.append("}");
/*      */         }
/* 2065 */         else if (this.dir == 2)
/*      */         {
/* 2067 */           buf.append("harray{label{text=\"Settlement size left:\"}");
/* 2068 */           buf.append("input{maxchars=\"3\";id=\"sizeN\";text=\"" + this.selectedNorth + "\"}");
/* 2069 */           buf.append("label{text=\" right:\"}");
/* 2070 */           buf.append("input{maxchars=\"3\";id=\"sizeS\";text=\"" + this.selectedSouth + "\"}");
/* 2071 */           buf.append("}");
/* 2072 */           buf.append("harray{label{text=\"Settlement size front:\"}");
/* 2073 */           buf.append("input{maxchars=\"3\";id=\"sizeE\";text=\"" + this.selectedEast + "\"}");
/* 2074 */           buf.append("label{text=\" back:\"}");
/* 2075 */           buf.append("input{maxchars=\"3\";id=\"sizeW\";text=\"" + this.selectedWest + "\"}");
/* 2076 */           buf.append("}");
/*      */         }
/* 2078 */         else if (this.dir == 4)
/*      */         {
/* 2080 */           buf.append("harray{label{text=\"Settlement size left:\"}");
/* 2081 */           buf.append("input{maxchars=\"3\";id=\"sizeE\";text=\"" + this.selectedEast + "\"}");
/* 2082 */           buf.append("label{text=\" right:\"}");
/* 2083 */           buf.append("input{maxchars=\"3\";id=\"sizeW\";text=\"" + this.selectedWest + "\"}");
/* 2084 */           buf.append("}");
/* 2085 */           buf.append("harray{label{text=\"Settlement size front:\"}");
/* 2086 */           buf.append("input{maxchars=\"3\";id=\"sizeS\";text=\"" + this.selectedSouth + "\"}");
/* 2087 */           buf.append("label{text=\" back:\"}");
/* 2088 */           buf.append("input{maxchars=\"3\";id=\"sizeN\";text=\"" + this.selectedNorth + "\"}");
/* 2089 */           buf.append("}");
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 2094 */           buf.append("harray{label{text=\"Settlement size left:\"}");
/* 2095 */           buf.append("input{maxchars=\"3\";id=\"sizeS\";text=\"" + this.selectedSouth + "\"}");
/* 2096 */           buf.append("label{text=\" right:\"}");
/* 2097 */           buf.append("input{maxchars=\"3\";id=\"sizeN\";text=\"" + this.selectedNorth + "\"}");
/* 2098 */           buf.append("}");
/* 2099 */           buf.append("harray{label{text=\"Settlement size front:\"}");
/* 2100 */           buf.append("input{maxchars=\"3\";id=\"sizeW\";text=\"" + this.selectedWest + "\"}");
/* 2101 */           buf.append("label{text=\" back:\"}");
/* 2102 */           buf.append("input{maxchars=\"3\";id=\"sizeE\";text=\"" + this.selectedEast + "\"}");
/* 2103 */           buf.append("}");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 2108 */         buf.append("harray{label{text=\"Settlement size west:\"}");
/* 2109 */         buf.append("input{maxchars=\"3\";id=\"sizeW\";text=\"" + this.selectedWest + "\"}");
/* 2110 */         buf.append("label{text=\" east:\"}");
/* 2111 */         buf.append("input{maxchars=\"3\";id=\"sizeE\";text=\"" + this.selectedEast + "\"}");
/* 2112 */         buf.append("}");
/* 2113 */         buf.append("harray{label{text=\"Settlement size north:\"}");
/* 2114 */         buf.append("input{maxchars=\"3\";id=\"sizeN\";text=\"" + this.selectedNorth + "\"}");
/* 2115 */         buf.append("label{text=\" south:\"}");
/* 2116 */         buf.append("input{maxchars=\"3\";id=\"sizeS\";text=\"" + this.selectedSouth + "\"}");
/* 2117 */         buf.append("}");
/*      */       } 
/*      */     } 
/* 2120 */     buf.append("text{text=\"\"}");
/* 2121 */     buf.append("harray {button{text=\"Survey Area\";id=\"submit\"}label{text=\" \";id=\"spacedlxg\"};button{text=\"Show Intro\";id=\"back\"};label{text=\" \";id=\"sacedlxg\"};button{text=\"Cancel\";id=\"cancel\"};};");
/* 2122 */     buf.append("text{text=\"\"}");
/* 2123 */     buf.append("text{type=\"bold\";text=\"Help\"}");
/* 2124 */     buf.append("text{text=\"\"}");
/* 2125 */     buf.append("text{type=\"italic\";text=\"This form will take you through the process of founding or resizing your settlement. You will be asked how large you want your settlement to be, if you want to buy a larger perimeter, what you want to call your new settlement and how many guards you wish to hire. You may wish to read the help articles on the Wiki for more information before you start.\"}");
/* 2126 */     buf.append("text{text=\"\"}");
/* 2127 */     buf.append("text{type=\"bold\";text=\"What you need first:\"}");
/* 2128 */     buf.append("text{text=\"\"}");
/* 2129 */     buf.append("text{type=\"italic\";text=\"  1. Have may Manage permission for all buildings within your new settlement area.\"}");
/* 2130 */     buf.append("text{type=\"italic\";text=\"  2. To check there are no large animals such as spiders in your local area (including underground).\"}");
/* 2131 */     buf.append("text{type=\"italic\";text=\"  3. To check there are no lairs in your local area.\"}");
/* 2132 */     if (!Servers.localServer.isFreeDeeds())
/* 2133 */       buf.append("text{type=\"italic\";text=\"  4. Sufficient funds in your in-game bank account.\"}"); 
/* 2134 */     buf.append("text{text=\"\"}");
/*      */     
/* 2136 */     buf.append("}};null;null;}");
/* 2137 */     getResponder().getCommunicator().sendBml(600, 600, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestion2() {
/* 2143 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 2144 */     buf.append("text{text=\"\"}");
/*      */     
/* 2146 */     addTileCost(buf);
/* 2147 */     buf.append("text{type=\"italic\";text=\"You may now move around and inspect the selected area while continuing with this form. The border has been marked for your convenience.\"}");
/* 2148 */     buf.append("text{text=\"\"}");
/* 2149 */     buf.append("text{type=\"bold\";text=\"Stage Two - The Deed Perimeter\"}");
/* 2150 */     if (this.error != null && this.error.length() > 0) {
/*      */       
/* 2152 */       buf.append("text{color=\"255,0,0\";type=\"bold\";text=\"" + this.error + "\"}");
/* 2153 */       buf.append("text{text=\"\"}");
/*      */     } 
/*      */     
/* 2156 */     buf.append("text{text=\"\"}");
/* 2157 */     buf.append("text{text=\"Please enter the number of tiles BEYOND the 5" + (
/*      */         
/* 2159 */         Servers.localServer.isFreeDeeds() ? "" : " initial tiles that you get for free") + ". You can simply leave the number at zero if you are happy with the " + '\005' + (
/*      */ 
/*      */         
/* 2162 */         Servers.localServer.isFreeDeeds() ? "" : " free") + " tiles or extend the perimeter at a later date.\"}");
/*      */ 
/*      */     
/* 2165 */     buf.append("text{text=\"\"}");
/*      */     
/* 2167 */     buf.append("harray{label{text=\"Perimeter Size: 5" + (
/*      */         
/* 2169 */         Servers.localServer.isFreeDeeds() ? "" : " free") + " tiles plus: \"}");
/*      */     
/* 2171 */     buf.append("input{maxchars=\"3\";id=\"perimeter\";text=\"" + this.initialPerimeter + "\"};label{text=\" tiles radius\"}}");
/* 2172 */     buf.append("text{text=\"\"}");
/* 2173 */     buf.append("harray {button{text=\"Survey Area\";id=\"submit\"};label{text=\" \";id=\"spacedlxg\"};button{text=\"Back\";id=\"back\"};label{text=\" \";id=\"spacedlxg\"};button{text=\"Cancel\";id=\"cancel\"};};");
/*      */     
/* 2175 */     buf.append("text{text=\"\"}");
/*      */     
/* 2177 */     buf.append("text{type=\"bold\";text=\"Help\"}");
/* 2178 */     buf.append("text{text=\"\"}");
/* 2179 */     buf.append("text{type=\"italic\";text=\"The perimeter surrounding a settlement on all sides is purely to stop non-citizens from building or founding their own settlement - there are no other restrictions. You do not own and control the perimeter in the same way as the main settlement. On PvP servers kingdom guards will hunt enemies within the perimeter.\"}");
/* 2180 */     buf.append("text{text=\"\"}");
/* 2181 */     buf.append("text{type=\"italic\";text=\"The minimum perimeter size is 5");
/* 2182 */     if (Servers.localServer.isFreeDeeds() && !Servers.localServer.isUpkeep()) {
/* 2183 */       buf.append(", which you may increase.\"}");
/*      */     } else {
/*      */       
/* 2186 */       buf.append(", which comes at no cost. You may pay to extend this if you wish. The current cost is " + Villages.PERIMETER_COST_STRING + " per tile");
/*      */       
/* 2188 */       if (Servers.localServer.isUpkeep())
/* 2189 */         buf.append(", and the upkeep is " + Villages.PERIMETER_UPKEEP_STRING + " per tile"); 
/* 2190 */       buf.append(".\"}");
/* 2191 */       buf.append("text{type=\"italic\";text=\"There are no refunds for downsizing your deed. You will receive what is left in the upkeep fund if you later choose to disband.\"}");
/*      */     } 
/* 2193 */     buf.append("}};null;null;}");
/* 2194 */     getResponder().getCommunicator().sendBml(600, 600, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendQuestion3() {
/* 2199 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 2200 */     buf.append("text{type=\"italic\";text=\"Your deed size will be " + this.diameterX + " by " + this.diameterY + " and the perimeter will extend beyond the border by " + (5 + this.initialPerimeter) + " tiles (including the required " + '\005' + (
/*      */ 
/*      */         
/* 2203 */         Servers.localServer.isFreeDeeds() ? "" : " free") + " tiles)\"}");
/* 2204 */     buf.append("text{text=\"\"}");
/* 2205 */     if (this.error != null && this.error.length() > 0) {
/*      */       
/* 2207 */       buf.append("text{color=\"255,0,0\";type=\"bold\";text=\"" + this.error + "\"}");
/* 2208 */       buf.append("text{text=\"\"}");
/*      */     } 
/*      */     
/* 2211 */     buf.append("text{type=\"bold\";text=\"Stage Three - Naming Your Deed\"}");
/* 2212 */     buf.append("text{text=\"\"}");
/*      */     
/* 2214 */     buf.append("text{type=\"italic\";text=\"Note! The name and motto may contain the following letters: \"}");
/* 2215 */     buf.append("text{type=\"italic\";text=\"a-z,A-Z,', and -\"}");
/* 2216 */     buf.append("text{text=\"\"}");
/* 2217 */     buf.append("text{type=\"bold\";text=\"Settlement name:\"}");
/* 2218 */     if (this.expanding) {
/*      */ 
/*      */       
/*      */       try {
/* 2222 */         Village village = Villages.getVillage(this.deed.getData2());
/* 2223 */         if (village.mayChangeName()) {
/*      */           
/* 2225 */           buf.append("input{maxchars=\"40\";id=\"vname\";text=\"" + this.villageName + "\"}");
/* 2226 */           buf.append("text{type=\"bold\";color=\"255,50,0\";text=\"NOTE: Changing name will" + (
/* 2227 */               Servers.localServer.isFreeDeeds() ? "" : " cost 5 silver,") + " remove all faith bonuses, and lock the name for 6 months.\"}");
/*      */         }
/*      */         else {
/*      */           
/* 2231 */           buf.append("text{text=\"" + this.villageName + "\"}");
/*      */           
/* 2233 */           buf.append("passthrough{id=\"vname\";text=\"" + this.villageName + "\"}");
/*      */         } 
/* 2235 */         this.permanent = village.isPermanent;
/*      */       }
/* 2237 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 2239 */         buf.append("text{type=\"bold\";text=\"Error: This settlement no longer exists and the operation will fail.\"}");
/*      */       } 
/*      */     } else {
/*      */       
/* 2243 */       buf.append("input{maxchars=\"40\";id=\"vname\";text=\"" + this.villageName + "\"}");
/* 2244 */     }  buf.append("text{text=\"\"}");
/* 2245 */     buf.append("text{type=\"bold\";text=\"Settlement motto:\"}");
/* 2246 */     buf.append("input{maxchars=\"100\";id=\"motto\";text=\"" + this.motto + "\"}");
/* 2247 */     buf.append("text{text=\"\"}");
/* 2248 */     buf.append("checkbox{id=\"democracy\";selected=\"" + this.democracy + "\";text=\"Make this a democracy: \"}");
/* 2249 */     Kingdom k = Kingdoms.getKingdom(this.spawnKingdom);
/* 2250 */     if (k != null && !k.isCustomKingdom())
/*      */     {
/* 2252 */       if (getResponder().getPower() >= 3) {
/*      */         
/* 2254 */         buf.append("checkbox{id=\"permanent\";selected=\"" + this.permanent + "\";text=\"Make this a permanent settlement: \"}");
/*      */         
/* 2256 */         buf.append("harray{label{text=\"Select a kingdom if this is the start town: \"};dropdown{id=\"kingdomid\";default=\"" + this.spawnKingdom + "\";options=\"None," + "Jenn-Kellon" + "," + "Mol Rehan" + "," + "Horde of the Summoned" + "," + "Freedom Isles" + "\"}}");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2266 */     buf.append("harray {button{text=\"Save this name\";id=\"submit\"};label{text=\" \";id=\"spacedlxg\"};button{text=\"Go Back\";id=\"back\"};label{text=\" \";id=\"spacedlxg\"};button{text=\"Cancel\";id=\"cancel\"};};");
/* 2267 */     buf.append("text{text=\"\"}");
/*      */     
/* 2269 */     buf.append("text{type=\"bold\";text=\"Help\"}");
/* 2270 */     buf.append("text{type=\"italic\";text=\"You can enter the name and motto for your deed as well as mark this as a democracy. The citizens of a settlement are allowed to vote for a new mayor up to once every week. The challenger requires 51% of the vote to succeed if it is a democracy, otherwise it is not possible to change mayor.  In a democracy you cannot revoke the citizenship of a citizen. By default, Wurm settlements are Autocracies.\"}");
/* 2271 */     buf.append("text{text=\"\"}");
/*      */     
/* 2273 */     buf.append("}};null;null;}");
/* 2274 */     getResponder().getCommunicator().sendBml(600, 600, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestion4() {
/* 2280 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 2281 */     buf.append("text{text=\"\"}");
/* 2282 */     if (this.error != null && this.error.length() > 0) {
/*      */       
/* 2284 */       buf.append("text{color=\"255,0,0\";text=\"" + this.error + "\"}");
/* 2285 */       buf.append("text{text=\"\"}");
/*      */     } 
/* 2287 */     if (this.changingName) {
/*      */       
/* 2289 */       buf.append("text{color=\"255,0,0\";text=\"You are changing name and the settlement will lose its faith bonuses.\"}");
/* 2290 */       buf.append("text{text=\"\"}");
/*      */     } 
/* 2292 */     buf.append("text{type=\"bold\";text=\"Settlement Size\"}");
/* 2293 */     addTileCost(buf);
/* 2294 */     addPerimeterCost(buf);
/* 2295 */     addChangeNameCost(buf);
/* 2296 */     buf.append("text{text=\"\"}");
/* 2297 */     buf.append("text{type=\"italic\";text=\"You have nearly finished the deed process.\"}");
/* 2298 */     buf.append("text{type=\"italic\";text=\"Please note that if the mayor changes kingdom for some reason the settlement will start disbanding.\"}");
/* 2299 */     buf.append("text{text=\"\"}");
/* 2300 */     buf.append("text{type=\"bold\";text=\"Do you wish to hire guards?\"}");
/* 2301 */     buf.append("text{text=\"\"}");
/*      */     
/* 2303 */     buf.append("text{text=\"For " + this.villageName + " you may hire up to " + this.maxGuards + " guards.\"}");
/* 2304 */     buf.append("text{text=\"\"}");
/* 2305 */     if (Servers.localServer.isChallengeOrEpicServer() && !Servers.localServer.isFreeDeeds()) {
/*      */       
/* 2307 */       buf.append("text{text=\"The only guard type is heavy guards. The running upkeep cost increases the more guards you have in a sort of ladder system. The first guards are cheaper than the last.\"};");
/* 2308 */       buf.append("text{text=\"You can test various numbers and review the cost for upkeep on the next screen.\"};");
/*      */     }
/* 2310 */     else if (Servers.localServer.isFreeDeeds()) {
/*      */       
/* 2312 */       buf.append("text{text=\"The only guard type is heavy guards. There is no cost for hiring guards here" + (
/* 2313 */           Servers.localServer.isUpkeep() ? (", but they have an upkeep of " + Villages.GUARD_UPKEEP_STRING + " per month") : ".") + "\"};");
/*      */       
/* 2315 */       buf.append("text{text=\"\"};");
/*      */     }
/*      */     else {
/*      */       
/* 2319 */       buf.append("text{text=\"The only guard type is heavy guards. The cost for hiring them is " + Villages.GUARD_COST_STRING + " and running upkeep is " + Villages.GUARD_UPKEEP_STRING + " per month.\"};");
/*      */       
/* 2321 */       buf.append("text{text=\"\"};");
/*      */     } 
/* 2323 */     if (Servers.localServer.PVPSERVER) {
/*      */       
/* 2325 */       buf.append("label{text=\"Note that you will need at least 1 guard to enforce the role rules on deed!\"}");
/* 2326 */       buf.append("text{text=\"\"}");
/*      */     } 
/* 2328 */     buf.append("harray{label{text=\"How many guards do you wish to hire?\"}");
/* 2329 */     buf.append("input{maxchars=\"3\";text=\"" + this.selectedGuards + "\";id=\"guards\"}}");
/* 2330 */     buf.append("text{text=\"\"}");
/* 2331 */     buf.append("harray {button{text=\"Save the number of guards\";id=\"submit\"};label{text=\" \";id=\"spacedlxg\"};button{text=\"Go Back\";id=\"back\"};label{text=\" \";id=\"spacedlxg\"};button{text=\"Cancel\";id=\"cancel\"};};");
/* 2332 */     buf.append("text{text=\"\"}");
/*      */     
/* 2334 */     buf.append("text{type=\"bold\";text=\"Help\"}");
/* 2335 */     buf.append("text{text=\"\"}");
/* 2336 */     buf.append("text{type=\"italic\";text=\"Guards enforce the rules on deed and protect you against enemies. Guards can be hired at any time, but it is good to start your settlement with at least a few! ");
/* 2337 */     if (!Servers.localServer.isFreeDeeds() && Servers.localServer.isUpkeep()) {
/*      */       
/* 2339 */       buf.append("There are two costs to guards, their initial hiring fee (covers their travel expenses) and their monthly salary which is added to the deed upkeep cost. There is a maximum number of guards you can have depending on the size of your deed though you can hire none at all if you wish!\"}");
/*      */ 
/*      */     
/*      */     }
/* 2343 */     else if (Servers.localServer.isUpkeep()) {
/*      */       
/* 2345 */       buf.append("There is no cost to hire guards, but you must pay a monthly salary which is added to the deed upkeep cost. There is a maximum number of guards you can have depending on the size of your deed though you can hire none at all if you wish!\"}");
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 2351 */       buf.append("There is no cost to hire guards and no monthly salary to pay. There is a maximum number of guards you can have depending on the size of your deed though you can hire none at all if you wish!\"}");
/*      */     } 
/*      */     
/* 2354 */     buf.append("text{text=\"\"}");
/* 2355 */     buf.append("}};null;null;}");
/* 2356 */     getResponder().getCommunicator().sendBml(600, 600, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendQuestion5() {
/* 2361 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 2362 */     buf.append("text{text=\"\"}");
/* 2363 */     if (this.error != null && this.error.length() > 0) {
/*      */       
/* 2365 */       buf.append("text{color=\"255,0,0\";text=\"" + this.error + "\"}");
/* 2366 */       buf.append("text{text=\"\"}");
/*      */     } 
/* 2368 */     if (this.changingName) {
/*      */       
/* 2370 */       buf.append("text{color=\"255,0,0\";text=\"You are changing name and the settlement will lose its faith bonuses.\"}");
/* 2371 */       buf.append("text{text=\"\"}");
/*      */     } 
/* 2373 */     String fs = "Found";
/* 2374 */     if (this.expanding)
/* 2375 */       fs = "Resize"; 
/* 2376 */     buf.append("text{type=\"italic\";text=\"Here are all your settings and the costs. If you are happy, click on " + fs + " Settlement. If you wish to change anything, use the Go Back button.\"}");
/*      */     
/* 2378 */     buf.append("text{text=\"\"}");
/* 2379 */     buf.append("text{type=\"bold\";text=\"Settlement Size\"}");
/* 2380 */     addTileCost(buf);
/* 2381 */     addPerimeterCost(buf);
/* 2382 */     addChangeNameCost(buf);
/* 2383 */     buf.append("text{type=\"bold\";text=\"Guards\"}");
/* 2384 */     addGuardCost(buf);
/* 2385 */     buf.append("text{type=\"bold\";text=\"Citizens\"}");
/* 2386 */     addCitizenMultiplier(buf);
/* 2387 */     buf.append("text{text=\"\"}");
/* 2388 */     if (!Servers.localServer.isFreeDeeds() || Servers.localServer.isUpkeep())
/* 2389 */       buf.append("text{type=\"bold\";text=\"Total\"}"); 
/* 2390 */     addTotalUpkeep(buf);
/* 2391 */     buf.append("text{text=\"\"}");
/* 2392 */     buf.append("text{type=\"bold\";text=\"Name and Type\"}");
/* 2393 */     if (!this.expanding)
/* 2394 */       buf.append("text{text=\"Your settlement name is: " + this.villageName + "\"}"); 
/* 2395 */     buf.append("text{text=\"Your settlement motto is: '" + this.motto + "'\"}");
/*      */     
/* 2397 */     buf.append("text{text=\"\"}");
/* 2398 */     if (this.democracy) {
/* 2399 */       buf.append("label{text=\"The settlement is a democracy.\"}");
/*      */     } else {
/* 2401 */       buf.append("label{text=\"The settlement is an autocracy.\"}");
/* 2402 */     }  buf.append("text{text=\"\"}");
/* 2403 */     if (getResponder().getPower() >= 3)
/*      */     {
/* 2405 */       if (this.permanent) {
/*      */         
/* 2407 */         buf.append("text{type=\"bold\";text=\"Start City\"}");
/* 2408 */         buf.append("label{text=\"The settlement will be a start city.\"}");
/* 2409 */         buf.append("label{text=\"The start kingdom will be " + Kingdoms.getNameFor(this.spawnKingdom) + ".\"}");
/* 2410 */         buf.append("text{text=\"\"}");
/*      */       } 
/*      */     }
/*      */     
/* 2414 */     boolean hasError = false;
/* 2415 */     Village village = null;
/* 2416 */     if (this.expanding) {
/*      */       
/*      */       try {
/*      */         
/* 2420 */         village = Villages.getVillage(this.deed.getData2());
/*      */       }
/* 2422 */       catch (NoSuchVillageException nsv) {
/*      */         
/* 2424 */         hasError = true;
/* 2425 */         buf.append("text{type=\"bold\";color=\"255,0,0\";text=\"Error: This settlement no longer exists and the operation will fail.\"}");
/*      */       } 
/*      */     }
/*      */     
/* 2429 */     if (!hasError && Features.Feature.HIGHWAYS.isEnabled()) {
/*      */       
/* 2431 */       buf.append("text{type=\"bold\";text=\"" + this.villageName + " KOS.v.Highways\"}");
/* 2432 */       if (this.expanding) {
/*      */ 
/*      */         
/* 2435 */         if (village.isKosAllowed() || (village.getReputations()).length > 0) {
/*      */           
/* 2437 */           if (willHaveHighay())
/*      */           {
/* 2439 */             hasError = true;
/* 2440 */             buf.append("text{type=\"bold\";color=\"255,0,0\";text=\"Error: Cannot expand over a highway if KOS is enabled, see settlment settings to change this option.\"}");
/*      */           }
/*      */           else
/*      */           {
/* 2444 */             buf.append("text{text=\"Note: You will not be able to have highway through this settlement as KOS is active.\"}");
/*      */           }
/*      */         
/*      */         }
/* 2448 */         else if (willHaveHighay()) {
/*      */           
/* 2450 */           if (!village.isHighwayAllowed()) {
/*      */             
/* 2452 */             hasError = true;
/* 2453 */             buf.append("text{type=\"bold\";color=\"255,0,0\";text=\"Error: The new size covers a highway but highways have not been allowed, see settlment settings to change this option.\"}");
/*      */           
/*      */           }
/* 2456 */           else if (village.hasHighway()) {
/* 2457 */             buf.append("text{text=\"The new size will still cover a highway which is already allowed by settlement settings.\"}");
/*      */           } else {
/* 2459 */             buf.append("text{text=\"The new size will now cover a highway which is already allowed by settlement settings.\"}");
/*      */           } 
/* 2461 */         } else if (village.isHighwayAllowed()) {
/*      */           
/* 2463 */           buf.append("text{text=\"Note: You will not be able to use KOS as highways have been enabled for this settlment, see settlment settings to change this option.\"}");
/*      */         }
/*      */         else {
/*      */           
/* 2467 */           buf.append("label{text=\"Note: To allow KOS or Highways, use the settlement settings.\"}");
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 2475 */       else if (willHaveHighay()) {
/*      */         
/* 2477 */         buf.append("text{text=\"The settlement size covers a highway, and will auto-set that flag in settlement settings.\"}");
/*      */       }
/*      */       else {
/*      */         
/* 2481 */         buf.append("label{text=\"Note: To allow KOS or Highways, use the settlement settings after founding.\"}");
/*      */       } 
/*      */     } 
/*      */     
/* 2485 */     if (!hasError) {
/*      */       
/* 2487 */       buf.append("text{type=\"bold\";text=\"" + fs + " " + this.villageName + "\"}");
/*      */       
/* 2489 */       buf.append("text{type=\"italic\";text=\"By clicking on the " + fs + " Settlement button you agree to the following terms:\"}");
/*      */       
/* 2491 */       buf.append("text{type=\"italic\";text=\"This is an irreversible and non refundable operation.\"}");
/* 2492 */       buf.append("text{text=\"\"}");
/* 2493 */       if (this.expanding) {
/*      */         
/* 2495 */         long resCost = getResizeCostDiff();
/* 2496 */         if (resCost > 0L) {
/*      */           
/* 2498 */           Change needed = new Change(resCost);
/* 2499 */           long avail = village.getAvailablePlanMoney();
/* 2500 */           Change availc = new Change(avail);
/* 2501 */           buf.append("text{type=\"italic\";text=\"This change will cost " + needed.getChangeString() + ".\"}");
/* 2502 */           buf.append("text{type=\"italic\";text=\"Up to " + availc.getChangeString() + " can be taken from the settlement upkeep funds.\"}");
/*      */           
/* 2504 */           long rest = getExpandMoneyNeededFromBank(resCost, village);
/* 2505 */           if (rest > 0L)
/*      */           {
/* 2507 */             Change restc = new Change(rest);
/* 2508 */             buf.append("text{type=\"italic\";text=\"" + restc.getChangeString() + " will be taken from your bank account.\"}");
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 2517 */           buf.append("text{type=\"italic\";text=\"This change is free of charge.\"}");
/*      */         } 
/* 2519 */       } else if (!Servers.localServer.isFreeDeeds()) {
/*      */         
/* 2521 */         long fullcost = getFoundingCost();
/* 2522 */         Change cfull = new Change(fullcost);
/* 2523 */         buf.append("text{type=\"italic\";text=\"The full cost for founding this deed will be " + cfull.getChangeString() + ".\"}");
/*      */         
/* 2525 */         if (this.deed.getTemplateId() != 862)
/* 2526 */           buf.append("text{type=\"italic\";text=\"The cost of the deed form will cover up to 7 silver coins of these. The rest will go into upkeep.\"}"); 
/* 2527 */         long toCharge = getFoundingCharge();
/* 2528 */         if (toCharge > 0L) {
/*      */           
/* 2530 */           Change cc = new Change(toCharge);
/* 2531 */           buf.append("text{type=\"italic\";text=\"" + cc.getChangeString() + " will be taken from your bank account.\"}");
/*      */         }
/* 2533 */         else if (this.deed.getTemplateId() != 862) {
/*      */           
/* 2535 */           long left = 100000L - fullcost;
/* 2536 */           Change leftc = new Change(left);
/* 2537 */           buf.append("text{type=\"italic\";text=\"The settlement will be founded and " + leftc.getChangeString() + " will be put into upkeep.\"}");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2542 */     buf.append("harray{" + (!hasError ? ("button{text=\"" + fs + " Settlement\";id=\"submit\"};label{text=\" \"};") : "") + "button{text=\"Go Back\";id=\"back\"};label{text=\" \"};button{text=\"Cancel\";id=\"cancel\"};}}};null;null;}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2549 */     getResponder().getCommunicator().sendBml(600, 700, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendQuestion6() {
/* 2554 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 2555 */     buf.append("text{text=\"\"}");
/* 2556 */     buf.append("text{type=\"italic\";text=\"You have founded and become the mayor and first citizen of " + this.villageName + ".\"}");
/*      */     
/* 2558 */     buf.append("text{text=\"\"}");
/* 2559 */     buf.append("text{type=\"italic\";text=\"The deed for this settlement has been placed in your inventory, and if you have hired any guards, they will no doubt be here as soon as they can be.\"}");
/* 2560 */     buf.append("text{text=\"\"}");
/* 2561 */     buf.append("text{type=\"italic\";text=\"You can change the size of your deed and its perimeter if you wish, as well as hire and fire guards, using the tools attached to the deed or token.\"}");
/* 2562 */     buf.append("text{text=\"\"}");
/* 2563 */     buf.append("text{type=\"italic\";text=\"Remember to keep money for upkeep as otherwise your settlement will disband and you will lose everything.\"}");
/* 2564 */     buf.append("text{text=\"\"}");
/* 2565 */     buf.append("text{type=\"italic\";text=\"Good luck, and may the Gods look over you with kindness! \"}");
/* 2566 */     buf.append("text{text=\"\"}");
/* 2567 */     buf.append("text{type=\"bold\";text=\"Lords of Wurm\"}");
/* 2568 */     buf.append("text{text=\"\"}");
/* 2569 */     buf.append("text{type=\"bold\";text=\"Notes\"}");
/* 2570 */     buf.append("text{type=\"italic\";text=\"The maximum number of citizens, including guards, is " + (this.tiles / 11) + "\"}");
/* 2571 */     if (!Servers.isThisAPvpServer())
/* 2572 */       buf.append("text{type=\"italic\";text=\"The maximum number of branded animals is " + (this.tiles / 11) + "\"}"); 
/* 2573 */     buf.append("text{text=\"\"}");
/* 2574 */     buf.append("harray {button{text=\"Finish\";id=\"submit\"}}}};null;null;}");
/*      */     
/* 2576 */     getResponder().getCommunicator().sendBml(600, 430, true, false, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void copyValues(VillageFoundationQuestion vfq) {
/* 2582 */     vfq.dir = this.dir;
/* 2583 */     vfq.error = this.error;
/* 2584 */     vfq.initialPerimeter = this.initialPerimeter;
/* 2585 */     vfq.sequence = this.sequence;
/*      */ 
/*      */     
/* 2588 */     vfq.selectedWest = this.selectedWest;
/* 2589 */     vfq.selectedEast = this.selectedEast;
/* 2590 */     vfq.selectedNorth = this.selectedNorth;
/* 2591 */     vfq.selectedSouth = this.selectedSouth;
/* 2592 */     vfq.diameterX = this.selectedWest + this.selectedEast + 1;
/* 2593 */     vfq.diameterY = this.selectedNorth + this.selectedSouth + 1;
/* 2594 */     vfq.maxGuards = GuardPlan.getMaxGuards(this.diameterX, this.diameterY);
/* 2595 */     vfq.changingName = this.changingName;
/* 2596 */     vfq.selectedGuards = this.selectedGuards;
/* 2597 */     vfq.tiles = this.diameterX * this.diameterY;
/* 2598 */     vfq.perimeterDiameterX = this.diameterX + 5 + 5 + this.initialPerimeter + this.initialPerimeter;
/*      */     
/* 2600 */     vfq.perimeterDiameterY = this.diameterY + 5 + 5 + this.initialPerimeter + this.initialPerimeter;
/*      */     
/* 2602 */     vfq.perimeterTiles = this.perimeterDiameterX * this.perimeterDiameterY - (this.diameterX + 5 + 5) * (this.diameterY + 5 + 5);
/*      */ 
/*      */     
/* 2605 */     vfq.motto = this.motto;
/*      */     
/* 2607 */     vfq.villageName = this.villageName;
/*      */     
/* 2609 */     vfq.permanent = this.permanent;
/*      */     
/* 2611 */     vfq.democracy = this.democracy;
/* 2612 */     vfq.spawnKingdom = this.spawnKingdom;
/* 2613 */     vfq.deed = this.deed;
/* 2614 */     vfq.tokenx = this.tokenx;
/* 2615 */     vfq.tokeny = this.tokeny;
/* 2616 */     vfq.surfaced = this.surfaced;
/* 2617 */     vfq.expanding = this.expanding;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean willHaveHighay() {
/* 2623 */     int startx = this.tokenx - this.selectedWest;
/* 2624 */     int starty = this.tokeny - this.selectedNorth;
/* 2625 */     int endx = this.tokenx + this.selectedEast;
/* 2626 */     int endy = this.tokeny + this.selectedSouth;
/*      */     
/* 2628 */     for (Item marker : Items.getMarkers()) {
/*      */       
/* 2630 */       int x = marker.getTileX();
/* 2631 */       int y = marker.getTileY();
/* 2632 */       if (x >= startx - 2 && x <= endx + 2 && y >= starty - 2 && y <= endy + 2)
/*      */       {
/* 2634 */         return true;
/*      */       }
/*      */     } 
/* 2637 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSequence(int newseq) {
/* 2642 */     this.sequence = newseq;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageFoundationQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
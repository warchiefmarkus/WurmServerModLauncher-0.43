/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.DbDoor;
/*     */ import com.wurmonline.server.structures.Door;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.server.structures.WallEnum;
/*     */ import com.wurmonline.shared.constants.StructureStateEnum;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GMBuildAllWallsQuestion
/*     */   extends Question
/*     */ {
/*  22 */   private static final Logger logger = Logger.getLogger(GMBuildAllWallsQuestion.class.getName());
/*     */   
/*     */   private final Structure targetStructure;
/*     */   
/*     */   private final Item buildItem;
/*     */   private final List<WallEnum> wallList;
/*     */   
/*     */   public GMBuildAllWallsQuestion(Creature aResponder, Structure aTarget) {
/*  30 */     super(aResponder, "Build all unfinished walls", "What type of walls?", 143, aTarget.getWurmId());
/*  31 */     this.targetStructure = aTarget;
/*  32 */     this.buildItem = aResponder.getCarriedItem(176);
/*  33 */     this.wallList = WallEnum.getWallsByTool(getResponder(), this.buildItem, false, false);
/*  34 */     this.wallList.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  40 */     setAnswer(aAnswer);
/*  41 */     if (this.type == 0) {
/*     */       
/*  43 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  46 */     if (this.type == 143)
/*     */     {
/*  48 */       if (getResponder().getPower() >= 4) {
/*     */         
/*     */         try {
/*  51 */           String prop = aAnswer.getProperty("walltype");
/*  52 */           if (prop != null) {
/*     */             
/*  54 */             short wallId = Short.parseShort(prop);
/*  55 */             if (wallId < this.wallList.size() && wallId >= 0) {
/*     */               
/*  57 */               getResponder().getLogger().log(Level.INFO, "Building all unfinished walls of structure with ID=" + this.targetStructure.getWurmId() + "to type " + ((WallEnum)this.wallList
/*  58 */                   .get(wallId)).getName());
/*     */               
/*  60 */               for (Wall w : this.targetStructure.getWalls()) {
/*     */                 
/*  62 */                 if (w.isWallPlan()) {
/*     */                   
/*  64 */                   w.setQualityLevel(80.0F);
/*  65 */                   w.setMaterial(((WallEnum)this.wallList.get(wallId)).getMaterial());
/*  66 */                   w.setType(((WallEnum)this.wallList.get(wallId)).getType());
/*  67 */                   w.setState(StructureStateEnum.FINISHED);
/*  68 */                   w.setDamage(0.0F);
/*     */                   
/*  70 */                   if (w.isDoor()) {
/*     */ 
/*     */                     
/*  73 */                     DbDoor dbDoor = new DbDoor(w);
/*  74 */                     dbDoor.setStructureId(this.targetStructure.getOwnerId());
/*  75 */                     this.targetStructure.addDoor((Door)dbDoor);
/*     */                     
/*     */                     try {
/*  78 */                       dbDoor.save();
/*  79 */                       dbDoor.addToTiles();
/*     */                     }
/*  81 */                     catch (IOException e) {
/*     */                       
/*  83 */                       logger.warning("Failed to save door! " + e);
/*  84 */                       getResponder().getCommunicator().sendAlertServerMessage("ERROR: IOException. Aborting!");
/*     */ 
/*     */                       
/*     */                       return;
/*     */                     } 
/*     */                   } 
/*     */                   
/*  91 */                   w.getTile().updateWall(w);
/*     */                 } 
/*     */               } 
/*  94 */               this.targetStructure.updateStructureFinishFlag();
/*     */             
/*     */             }
/*     */             else {
/*     */               
/*  99 */               logger.fine("WallID was larger than WallList.size(), parsed value = " + wallId);
/* 100 */               getResponder().getCommunicator().sendAlertServerMessage("ERROR: Something went wrong with parsing the input. Aborting.");
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 106 */         } catch (NumberFormatException nsf) {
/*     */           
/* 108 */           getResponder().getCommunicator().sendNormalServerMessage("Unable to set wall types with that answer");
/*     */           return;
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 119 */     if (this.buildItem == null) {
/*     */       
/* 121 */       getResponder().getCommunicator().sendNormalServerMessage("You need to have at least one Ebony Wand in your inventory for this to work.");
/*     */       return;
/*     */     } 
/* 124 */     StringBuilder buf = new StringBuilder();
/* 125 */     buf.append(getBmlHeader());
/* 126 */     if (getResponder().getPower() >= 4) {
/*     */ 
/*     */       
/* 129 */       buf.append("text{type=\"bold\";text=\"Choose the type of wall all wall plans will be turned into:\"}");
/* 130 */       boolean isSelected = false;
/* 131 */       buf.append("harray{label{text='Tile type'}dropdown{id='walltype';options=\"");
/* 132 */       for (int i = 0; i < this.wallList.size(); i++)
/*     */       {
/*     */ 
/*     */         
/* 136 */         buf.append(((WallEnum)this.wallList.get(i)).getName() + ",");
/*     */       }
/* 138 */       buf.append("\"}}");
/* 139 */       buf.append(createAnswerButton2());
/* 140 */       getResponder().getCommunicator().sendBml(250, 150, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GMBuildAllWallsQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
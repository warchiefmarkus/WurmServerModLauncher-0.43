/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public final class HideQuestion
/*     */   extends Question
/*     */ {
/*     */   public HideQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  37 */     super(aResponder, aTitle, aQuestion, 70, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  43 */     if (getResponder().getPower() >= 2) {
/*     */ 
/*     */       
/*  46 */       boolean putOnSurface = false;
/*  47 */       String key2 = "putonsurf";
/*  48 */       String val2 = answers.getProperty("putonsurf");
/*  49 */       if (val2 != null && val2.equals("true"))
/*  50 */         putOnSurface = true; 
/*  51 */       String key = "height";
/*  52 */       String val = answers.getProperty("height");
/*  53 */       if ((val != null && val.length() > 0) || putOnSurface) {
/*     */         
/*     */         try {
/*     */           
/*  57 */           int x = (val == null) ? 0 : Integer.parseInt(val);
/*     */           
/*     */           try {
/*  60 */             Item i = Items.getItem(this.target);
/*  61 */             short rock = Tiles.decodeHeight(Server.rockMesh.getTile((getResponder().getCurrentTile()).tilex, 
/*  62 */                   (getResponder().getCurrentTile()).tiley));
/*  63 */             short height = Tiles.decodeHeight(Server.surfaceMesh.getTile(
/*  64 */                   (getResponder().getCurrentTile()).tilex, (getResponder().getCurrentTile()).tiley));
/*  65 */             int diff = height - rock;
/*  66 */             if (i.getOwnerId() == -10L || i.getOwnerId() == getResponder().getWurmId())
/*     */             {
/*  68 */               if (x < diff || putOnSurface) {
/*     */                 
/*  70 */                 Items.hideItem(getResponder(), i, (rock + x) / 10.0F, putOnSurface);
/*  71 */                 if (putOnSurface) {
/*  72 */                   getResponder().getCommunicator().sendNormalServerMessage("You carefully hide the " + i
/*  73 */                       .getName() + " here.");
/*     */                 } else {
/*  75 */                   getResponder().getCommunicator().sendNormalServerMessage("You carefully hide the " + i
/*  76 */                       .getName() + " at " + ((rock + x) / 10.0F) + " meters.");
/*     */                 } 
/*     */               } else {
/*  79 */                 getResponder().getCommunicator().sendNormalServerMessage("You can not hide the " + i
/*  80 */                     .getName() + " at " + (rock + x) + ". Rock is at " + rock + ", and surface is at " + height + ".");
/*     */               }
/*     */             
/*     */             }
/*  84 */           } catch (NoSuchItemException nsi) {
/*     */             
/*  86 */             getResponder().getCommunicator().sendNormalServerMessage("The item can no longer be found!");
/*     */           }
/*     */         
/*  89 */         } catch (NumberFormatException nf) {
/*     */           
/*  91 */           getResponder().getCommunicator().sendNormalServerMessage("Failed to parse " + val + " as an integer number.");
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
/* 102 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */ 
/*     */     
/*     */     try {
/* 106 */       Item it = Items.getItem(this.target);
/* 107 */       buf.append("text{type='';text='Hiding " + it.getName() + ".'}");
/* 108 */       if (!getResponder().isOnSurface()) {
/* 109 */         buf.append("text{type='';text='You can only hide items on the surface now.'}");
/*     */       } else {
/*     */         
/* 112 */         short rock = Tiles.decodeHeight(Server.rockMesh.getTile((getResponder().getCurrentTile()).tilex, 
/* 113 */               (getResponder().getCurrentTile()).tiley));
/* 114 */         short height = Tiles.decodeHeight(Server.surfaceMesh.getTile((getResponder().getCurrentTile()).tilex, 
/* 115 */               (getResponder().getCurrentTile()).tiley));
/* 116 */         int diff = height - rock;
/* 117 */         buf.append("text{type='';text='The rock is at " + rock + " decimeter, soil at " + height + " decimeter above sea level. Suggested height above rock is " + (diff / 2) + " decimeter.'}");
/*     */         
/* 119 */         if (diff > 3) {
/*     */           
/* 121 */           buf.append("harray{input{id='height'; maxchars='4'; text='" + (diff / 2) + "'}label{text='Height in decimeters over rock layer'}}");
/*     */         }
/*     */         else {
/*     */           
/* 125 */           buf.append("text{type='';text='The soil here is too shallow.'}");
/* 126 */         }  buf.append("harray{label{text=\"Just put on surface \"};checkbox{id=\"putonsurf\";selected=\"false\"};}");
/* 127 */         buf.append("text{type='';text='Here is a random location position for treasure hunts:'}");
/* 128 */         findTreasureHuntLocation(buf);
/*     */       }
/*     */     
/*     */     }
/* 132 */     catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */     
/* 135 */     buf.append(createAnswerButton2());
/* 136 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void findTreasureHuntLocation(StringBuilder buf) {
/* 141 */     for (int x = 0; x < 10; x++) {
/*     */       
/* 143 */       int suggx = Server.rand.nextInt(Zones.worldTileSizeX);
/* 144 */       int suggy = Server.rand.nextInt(Zones.worldTileSizeY);
/* 145 */       short rock = Tiles.decodeHeight(Server.rockMesh.getTile(suggx, suggy));
/* 146 */       short height = Tiles.decodeHeight(Server.surfaceMesh.getTile(suggx, suggy));
/* 147 */       if (height > 0) {
/*     */         
/* 149 */         int diff = height - rock;
/* 150 */         if (diff >= 2) {
/*     */           
/* 152 */           buf.append("text{type='';text='Tile at " + suggx + ", " + suggy + " has depth " + diff + "'}");
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\HideQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
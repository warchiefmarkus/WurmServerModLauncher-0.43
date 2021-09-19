/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ public final class VillagePerimeterQuestion
/*     */   extends Question
/*     */ {
/*     */   private final int villageId;
/*     */   
/*     */   public VillagePerimeterQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, int villid) {
/*  45 */     super(aResponder, aTitle, aQuestion, 75, aTarget);
/*  46 */     this.villageId = villid;
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
/*     */   public void answer(Properties props) {}
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
/*     */   public void sendQuestion() {
/* 173 */     if (!Servers.localServer.HOMESERVER) {
/*     */       
/* 175 */       StringBuilder buf = new StringBuilder(getBmlHeaderWithScroll());
/* 176 */       buf.append("text{type='bold';text='Perimeters are not active on wild servers.'}");
/* 177 */       buf.append(createAnswerButton2());
/* 178 */       getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 183 */         Village village = Villages.getVillage(this.villageId);
/* 184 */         String deedType = "settlement";
/*     */ 
/*     */         
/* 187 */         StringBuilder buf = new StringBuilder(getBmlHeaderWithScroll());
/* 188 */         buf.append("text{type='bold';text='Set the permissions for the settlement perimeter.'}");
/*     */         
/* 190 */         buf.append("text{text=''}");
/* 191 */         int max = (village.getEndX() - village.getStartX()) / 2;
/* 192 */         buf.append("text{text=\"Perimeter friends. These may do all these things in your perimeter. " + village
/* 193 */             .getName() + " may have up to " + max + " friends.\"}");
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
/* 212 */         buf.append(createAnswerButton3());
/* 213 */         getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */       }
/* 215 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 217 */         getResponder().getCommunicator().sendNormalServerMessage("Failed to update perimeter settings. No such village could be located.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillagePerimeterQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
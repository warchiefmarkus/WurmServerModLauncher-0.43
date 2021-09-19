/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTypes;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageStatus;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
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
/*     */ public final class VillageExpansionQuestion
/*     */   extends Question
/*     */   implements VillageStatus, ItemTypes
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(VillageExpansionQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Item token;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VillageExpansionQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, Item aToken) {
/*  53 */     super(aResponder, aTitle, aQuestion, 12, aTarget);
/*  54 */     this.token = aToken;
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
/*  65 */     setAnswer(answers);
/*  66 */     QuestionParser.parseVillageExpansionQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getToken() {
/*  75 */     return this.token;
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
/*  86 */     int villid = this.token.getData2();
/*     */ 
/*     */     
/*     */     try {
/*  90 */       Item deed = Items.getItem(this.target);
/*  91 */       int oldVill = deed.getData2();
/*  92 */       if (oldVill != -1) {
/*     */ 
/*     */         
/*     */         try {
/*  96 */           getResponder().getCommunicator().sendSafeServerMessage("This is the deed for " + 
/*  97 */               Villages.getVillage(oldVill).getName() + "! You cannot use it to expand a settlement!");
/*     */         
/*     */         }
/* 100 */         catch (NoSuchVillageException nsv) {
/*     */           
/* 102 */           getResponder().getCommunicator().sendSafeServerMessage("This deed already is already used! You cannot use it to expand this settlement!");
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 107 */       Village village = Villages.getVillage(villid);
/* 108 */       StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */       
/* 110 */       if (village != null)
/*     */       {
/* 112 */         int size = Villages.getSizeForDeed(deed.getTemplateId());
/*     */         
/* 114 */         buf.append("text{text='The expansion will set the size of the settlement to " + size + " tiles out in all directions from the " + this.token
/* 115 */             .getName() + ".'}");
/*     */         
/* 117 */         buf.append("text{text='You will require all the house deeds for any houses in the new area.'}");
/* 118 */         buf
/* 119 */           .append("text{text='Also note that in the case that the allowed number of citizens is decreased any surplus will be kicked from the settlement automatically and in no particular order so you may want to do that manually instead.'}");
/* 120 */         buf.append(createAnswerButton2());
/*     */         
/* 122 */         getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */       }
/*     */       else
/*     */       {
/* 126 */         getResponder().getCommunicator().sendSafeServerMessage("This token has no settlement associated with it. It cannot be expanded.");
/*     */       }
/*     */     
/* 129 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 131 */       logger.log(Level.WARNING, "Failed to locate settlement with id " + this.target, (Throwable)nsi);
/* 132 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*     */     
/*     */     }
/* 135 */     catch (NoSuchVillageException nss) {
/*     */       
/* 137 */       logger.log(Level.WARNING, "Failed to locate settlement with id " + villid, (Throwable)nss);
/* 138 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for that request. Please contact administration.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageExpansionQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
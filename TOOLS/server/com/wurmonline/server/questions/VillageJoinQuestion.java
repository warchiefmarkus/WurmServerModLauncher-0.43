/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.villages.Village;
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
/*     */ 
/*     */ public final class VillageJoinQuestion
/*     */   extends Question
/*     */ {
/*  34 */   private static final Logger logger = Logger.getLogger(VillageJoinQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Creature invited;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VillageJoinQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) throws NoSuchCreatureException, NoSuchPlayerException {
/*  50 */     super(aResponder, aTitle, aQuestion, 11, aTarget);
/*  51 */     this.invited = Server.getInstance().getCreature(aTarget);
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
/*  62 */     setAnswer(answers);
/*  63 */     QuestionParser.parseVillageJoinQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Creature getInvited() {
/*  72 */     return this.invited;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  78 */     Village village = getResponder().getCitizenVillage();
/*  79 */     if (village != null) {
/*     */       
/*  81 */       StringBuilder buf = new StringBuilder();
/*  82 */       buf.append(getBmlHeader());
/*  83 */       buf.append("text{type=\"bold\";text=\"Joining settlement " + village.getName() + ":\"}");
/*  84 */       buf.append("text{text=\"You have been invited by " + getResponder().getName() + " to join " + village.getName() + ". \"}");
/*     */       
/*  86 */       if (getInvited().isPlayer() && getInvited().mayChangeVillageInMillis() > 0L) {
/*     */         
/*  88 */         buf.append("text{text=\"You may not change settlement in " + 
/*  89 */             Server.getTimeFor(getInvited().mayChangeVillageInMillis()) + ". \"}");
/*     */       }
/*     */       else {
/*     */         
/*  93 */         Village currvill = getInvited().getCitizenVillage();
/*  94 */         if (currvill != null) {
/*  95 */           buf.append("text{text=\"Your " + currvill.getName() + " citizenship will be revoked. \"}");
/*     */         } else {
/*  97 */           buf.append("text{text=\"You are currently not citizen in any settlement. \"}");
/*  98 */         }  if (village.isDemocracy()) {
/*  99 */           buf
/* 100 */             .append("text{text=\"" + village
/* 101 */               .getName() + " is a democracy. This means your citizenship cannot be revoked by any city officials such as the mayor. \"}");
/*     */         } else {
/*     */           
/* 104 */           buf
/* 105 */             .append("text{text=\"" + village
/* 106 */               .getName() + " is a non-democracy. This means your citizenship can be revoked by any city officials such as the mayor. \"}");
/*     */         } 
/* 108 */         buf.append("text{text=\"Do you want to join " + village.getName() + "?\"}");
/*     */         
/* 110 */         buf.append("radio{ group=\"join\"; id=\"true\";text=\"Yes\"}");
/* 111 */         buf.append("radio{ group=\"join\"; id=\"false\";text=\"No\";selected=\"true\"}");
/*     */       } 
/* 113 */       buf.append(createAnswerButton2());
/* 114 */       getInvited().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/*     */     else {
/*     */       
/* 118 */       logger.log(Level.WARNING, getResponder().getName() + " tried to invite to null settlement!");
/* 119 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for that invitation. Please contact administration.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageJoinQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
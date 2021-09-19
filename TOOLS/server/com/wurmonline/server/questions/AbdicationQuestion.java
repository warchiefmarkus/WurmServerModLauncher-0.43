/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
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
/*     */ public final class AbdicationQuestion
/*     */   extends Question
/*     */ {
/*     */   public AbdicationQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  34 */     super(aResponder, aTitle, aQuestion, 68, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  43 */     String key = "abd";
/*  44 */     String val = answers.getProperty("abd");
/*  45 */     if (val != null && val.equals("true")) {
/*     */       
/*  47 */       byte kdom = getResponder().getKingdomId();
/*  48 */       King k = King.getKing(kdom);
/*  49 */       if (k != null && k.kingid == getResponder().getWurmId()) {
/*     */         
/*  51 */         k.abdicate(getResponder().isOnSurface(), false);
/*  52 */         getResponder().getCommunicator().sendNormalServerMessage("You are no longer the " + 
/*  53 */             King.getRulerTitle(getResponder().isNotFemale(), kdom) + " of " + 
/*  54 */             Kingdoms.getNameFor(kdom) + ".");
/*     */       } else {
/*     */         
/*  57 */         getResponder().getCommunicator().sendNormalServerMessage("You are not the " + 
/*  58 */             King.getRulerTitle(getResponder().isNotFemale(), kdom) + " of " + 
/*  59 */             Kingdoms.getNameFor(kdom) + ".");
/*     */       } 
/*     */     } else {
/*     */       
/*  63 */       getResponder().getCommunicator().sendNormalServerMessage("You decide not to abdicate.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  70 */     StringBuilder buf = new StringBuilder();
/*  71 */     buf.append(getBmlHeader());
/*     */     
/*  73 */     buf.append("text{type='italic';text='This is nothing to take lightly. You may never get the chance to become " + 
/*  74 */         King.getRulerTitle(getResponder().isNotFemale(), getResponder().getKingdomId()) + " again.'}");
/*  75 */     buf.append("text{type='italic';text='You should not be forced into doing this. You are the " + 
/*  76 */         King.getRulerTitle(getResponder().isNotFemale(), getResponder().getKingdomId()) + "!'}");
/*  77 */     buf
/*  78 */       .append("text{type='italic';text='Valid reasons for abdication include lack of time, that you perform poorly and have no hopes of succeeding, or that you are being rewarded for stepping down.'}");
/*  79 */     buf
/*  80 */       .append("text{type='italic';text='Just do not give up too easily. All you may have to do is to take control and be more active.'}");
/*     */     
/*     */     try {
/*  83 */       Kingdom kingd = Kingdoms.getKingdom(getResponder().getKingdomId());
/*  84 */       if (kingd != null)
/*     */       {
/*  86 */         if (kingd.isCustomKingdom()) {
/*  87 */           buf.append("text{type='bold';text='Please note that the royal items will drop on the ground upon abdicating for anyone to pick up.'}");
/*     */         } else {
/*  89 */           buf.append("text{type='bold';text='Please note that the royal items will be destroyed and anyone else in the kingdom may attempt to become new ruler.'}");
/*     */         } 
/*     */       }
/*  92 */     } catch (Exception ex) {
/*     */       
/*  94 */       logger.log(Level.WARNING, getResponder().getName() + " " + ex.getMessage(), ex);
/*     */     } 
/*  96 */     buf.append("text{type='italic';text='With those words, hopefully you may take the right decision.'}");
/*  97 */     buf.append("text{text=''}");
/*  98 */     buf.append("text{text='Do you want to Abdicate?'}");
/*  99 */     buf.append("text{text=''}");
/* 100 */     buf.append("text{type='bold';text='If you answer yes you will no longer be " + 
/* 101 */         King.getRulerTitle(getResponder().isNotFemale(), getResponder().getKingdomId()) + " of " + 
/* 102 */         Kingdoms.getNameFor(getResponder().getKingdomId()) + ".'}");
/* 103 */     buf.append("text{text=''}");
/* 104 */     buf.append("radio{ group='abd'; id='true';text='Yes'}");
/* 105 */     buf.append("radio{ group='abd'; id='false';text='No';selected='true'}");
/* 106 */     buf.append(createAnswerButton2());
/* 107 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AbdicationQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
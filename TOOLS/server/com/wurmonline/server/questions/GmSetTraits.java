/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Traits;
/*     */ import com.wurmonline.shared.util.StringUtilities;
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
/*     */ 
/*     */ public class GmSetTraits
/*     */   extends Question
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(GmSetTraits.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private final Creature creature;
/*     */ 
/*     */ 
/*     */   
/*     */   public GmSetTraits(Creature aResponder, Creature aTarget) {
/*  42 */     super(aResponder, "Creature Traits", aTarget.getName() + " Traits", 103, aTarget.getWurmId());
/*  43 */     this.creature = aTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  54 */     setAnswer(aAnswer);
/*  55 */     if (this.type == 0) {
/*     */       
/*  57 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  60 */     if (this.type == 103)
/*     */     {
/*  62 */       if (getResponder().getPower() >= 4) {
/*     */         
/*  64 */         boolean somethingChanged = false;
/*  65 */         for (int x = 0; x < 64; x++) {
/*     */           
/*  67 */           boolean was = this.creature.getStatus().isTraitBitSet(x);
/*  68 */           boolean is = Boolean.parseBoolean(aAnswer.getProperty("sel" + x));
/*  69 */           if (is != was) {
/*     */ 
/*     */             
/*  72 */             this.creature.getStatus().setTraitBit(x, is);
/*  73 */             logger.log(Level.INFO, getResponder().getName() + " setting trait " + x + " " + this.creature
/*  74 */                 .getName() + ", " + this.creature.getWurmId());
/*  75 */             getResponder().getLogger().log(Level.INFO, " setting trait " + x + " " + this.creature
/*  76 */                 .getName() + ", " + this.creature.getWurmId());
/*  77 */             somethingChanged = true;
/*     */           } 
/*     */         } 
/*  80 */         if (somethingChanged) {
/*     */           
/*  82 */           this.creature.refreshVisible();
/*     */           
/*  84 */           GmSetTraits gt = new GmSetTraits(getResponder(), this.creature);
/*  85 */           gt.sendQuestion();
/*     */         } 
/*     */       } 
/*     */     }
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
/* 109 */     StringBuilder buf = new StringBuilder();
/* 110 */     buf.append(getBmlHeader());
/* 111 */     if (getResponder().getPower() >= 4) {
/*     */ 
/*     */       
/* 114 */       for (int x = 0; x < 64; x++) {
/*     */         
/* 116 */         String traitName = Traits.getTraitString(x);
/* 117 */         if (x == 15) {
/* 118 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 119 */         } else if (x == 16) {
/* 120 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 121 */         } else if (x == 17) {
/* 122 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 123 */         } else if (x == 18) {
/* 124 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 125 */         } else if (x == 24) {
/* 126 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 127 */         } else if (x == 25) {
/* 128 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 129 */         } else if (x == 23) {
/* 130 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 131 */         } else if (x == 30) {
/* 132 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 133 */         } else if (x == 31) {
/* 134 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 135 */         } else if (x == 32) {
/* 136 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 137 */         } else if (x == 33) {
/* 138 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/* 139 */         } else if (x == 34) {
/* 140 */           traitName = StringUtilities.raiseFirstLetterOnly(this.creature.getColourName(x));
/*     */         } 
/* 142 */         if (traitName.length() > 0) {
/*     */           
/* 144 */           String colour = "";
/* 145 */           if (Traits.isTraitNegative(x)) {
/* 146 */             colour = "color=\"255,66,66\";";
/* 147 */           } else if (!Traits.isTraitNeutral(x)) {
/* 148 */             colour = "color=\"66,255,66\";";
/*     */           } 
/* 150 */           buf.append("harray{checkbox{id=\"sel" + x + "\";selected=\"" + this.creature.getStatus().isTraitBitSet(x) + "\"};label{" + colour + "text=\"" + traitName + "\"};}");
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 156 */       buf.append("label{text=\"\"};");
/* 157 */       buf.append("text{type=\"bold\";text=\"--------------- Help -------------------\"}");
/* 158 */       buf.append("text{text=\"Can add or remove traits.\"}");
/* 159 */       buf.append("text{text=\"If anything is changed, then once the change is applied it will show this screen again.\"}");
/* 160 */       buf.append(createAnswerButton2());
/* 161 */       getResponder().getCommunicator().sendBml(250, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GmSetTraits.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
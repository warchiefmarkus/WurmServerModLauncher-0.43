/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Cultist;
/*     */ import com.wurmonline.server.players.Cults;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ public class GmSetMedPath
/*     */   extends Question
/*     */ {
/*  15 */   private static final Logger logger = Logger.getLogger(GmSetMedPath.class.getName());
/*     */   
/*     */   private final Creature creature;
/*     */   
/*     */   public GmSetMedPath(Creature aResponder, Creature aTarget) {
/*  20 */     super(aResponder, "Meditation Path", aTarget.getName() + " Meditation Path", 132, aTarget.getWurmId());
/*  21 */     this.creature = aTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  27 */     setAnswer(aAnswer);
/*  28 */     if (this.type == 0) {
/*     */       
/*  30 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  33 */     if (this.type == 132)
/*     */     {
/*  35 */       if (getResponder().getPower() >= 4) {
/*     */         
/*     */         try {
/*  38 */           String prop = aAnswer.getProperty("newcult");
/*  39 */           if (prop != null) {
/*     */             
/*  41 */             getResponder().getLogger().log(Level.INFO, "Setting meditation path and level of " + this.creature.getName());
/*     */             
/*  43 */             byte cultId = Byte.parseByte(prop);
/*  44 */             Cultist c = this.creature.getCultist();
/*  45 */             if (c == null && cultId > 0) {
/*  46 */               c = new Cultist(this.creature.getWurmId(), cultId);
/*  47 */             } else if (c != null) {
/*     */               
/*  49 */               if (cultId > 0) {
/*  50 */                 c.setPath(cultId);
/*     */               } else {
/*     */                 
/*     */                 try {
/*  54 */                   c.deleteCultist();
/*     */                   
/*  56 */                   if (this.creature != getResponder())
/*  57 */                     this.creature.getCommunicator().sendNormalServerMessage("You are no longer following any path."); 
/*  58 */                   getResponder().getCommunicator().sendNormalServerMessage(this.creature.getName() + " is no longer following any path.");
/*     */                   
/*  60 */                   this.creature.refreshVisible();
/*  61 */                   this.creature.getCommunicator().sendOwnTitles();
/*     */                   
/*     */                   return;
/*  64 */                 } catch (IOException iOException) {}
/*     */               } 
/*     */             } else {
/*     */               return;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*  73 */           prop = aAnswer.getProperty("cultlevel");
/*  74 */           if (prop != null) {
/*     */             
/*  76 */             byte cultLvl = Byte.parseByte(prop);
/*  77 */             Cultist c = this.creature.getCultist();
/*  78 */             if (c != null) {
/*  79 */               c.setLevel(cultLvl);
/*     */             }
/*     */           } 
/*  82 */           if (this.creature != getResponder())
/*  83 */             this.creature.getCommunicator().sendNormalServerMessage("You are now " + this.creature.getCultist().getCultistTitle()); 
/*  84 */           getResponder().getCommunicator().sendNormalServerMessage(this.creature.getName() + " is now " + this.creature.getCultist().getCultistTitle());
/*     */           
/*  86 */           this.creature.refreshVisible();
/*  87 */           this.creature.getCommunicator().sendOwnTitles();
/*     */         }
/*  89 */         catch (NumberFormatException nsf) {
/*     */           
/*  91 */           getResponder().getCommunicator().sendNormalServerMessage("Unable to set meditation path and level with those answers.");
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
/* 102 */     StringBuilder buf = new StringBuilder();
/* 103 */     buf.append(getBmlHeader());
/* 104 */     if (getResponder().getPower() >= 4) {
/*     */       
/* 106 */       buf.append("text{type=\"bold\";text=\"Choose path to set the target to:\"}");
/*     */       
/* 108 */       Cultist c = this.creature.getCultist();
/*     */       
/* 110 */       int cId = (c == null) ? 0 : c.getPath();
/* 111 */       boolean isSelected = false;
/* 112 */       for (int i = 0; i < 6; i++) {
/*     */         
/* 114 */         if (i == cId) {
/* 115 */           isSelected = true;
/*     */         } else {
/* 117 */           isSelected = false;
/*     */         } 
/* 119 */         buf.append("radio{ group='newcult'; id='" + i + "'; text='" + Cults.getPathNameFor((byte)i) + "'" + (isSelected ? ";selected='true'" : "") + "}");
/*     */       } 
/*     */       
/* 122 */       int cLvl = (c == null) ? 0 : c.getLevel();
/* 123 */       buf.append("harray{input{id='cultlevel'; maxchars='2'; text='" + cLvl + "'}label{text='Path Level'}}");
/* 124 */       buf.append(createAnswerButton2());
/* 125 */       getResponder().getCommunicator().sendBml(250, 250, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GmSetMedPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
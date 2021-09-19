/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.behaviours.BehaviourDispatcher;
/*     */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*     */ import com.wurmonline.server.behaviours.NoSuchBehaviourException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.structures.NoSuchWallException;
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
/*     */ public final class SleepQuestion
/*     */   extends Question
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(SleepQuestion.class.getName());
/*     */ 
/*     */   
/*     */   public SleepQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  42 */     super(aResponder, aTitle, aQuestion, 47, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  48 */     String key = "sleep";
/*  49 */     String val = answers.getProperty("sleep");
/*  50 */     if (val != null && val.equals("true")) {
/*     */ 
/*     */       
/*     */       try {
/*  54 */         getResponder().getCurrentAction();
/*  55 */         getResponder().getCommunicator().sendNormalServerMessage("You are too busy to sleep right now.");
/*     */       }
/*  57 */       catch (NoSuchActionException nsa) {
/*     */ 
/*     */         
/*     */         try {
/*  61 */           BehaviourDispatcher.action(getResponder(), getResponder().getCommunicator(), -1L, this.target, (short)140);
/*     */         }
/*  63 */         catch (FailedException failedException) {
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*  68 */         catch (NoSuchBehaviourException nsb) {
/*     */           
/*  70 */           logger.log(Level.WARNING, nsb.getMessage(), (Throwable)nsb);
/*     */         }
/*  72 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */         
/*  75 */         } catch (NoSuchItemException nsi) {
/*     */           
/*  77 */           logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*     */         }
/*  79 */         catch (NoSuchPlayerException noSuchPlayerException) {
/*     */ 
/*     */         
/*  82 */         } catch (NoSuchWallException nsw) {
/*     */           
/*  84 */           logger.log(Level.WARNING, nsw.getMessage(), (Throwable)nsw);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/*  89 */       getResponder().getCommunicator().sendNormalServerMessage("You decide not to go to sleep right now.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  95 */     StringBuilder buf = new StringBuilder();
/*  96 */     buf.append(getBmlHeader());
/*     */     
/*  98 */     buf.append("text{text='Do you want to go to sleep? You will log off Wurm.'}text{text=''}");
/*     */     
/* 100 */     buf.append("radio{ group='sleep'; id='true';text='Yes';selected='true'}");
/* 101 */     buf.append("radio{ group='sleep'; id='false';text='No'}");
/* 102 */     buf.append(createAnswerButton2());
/*     */     
/* 104 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SleepQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
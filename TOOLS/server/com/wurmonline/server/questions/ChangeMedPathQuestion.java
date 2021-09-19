/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.players.Cultist;
/*    */ import com.wurmonline.server.players.Cults;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChangeMedPathQuestion
/*    */   extends Question
/*    */ {
/*    */   private final Cultist cultist;
/*    */   
/*    */   public ChangeMedPathQuestion(Creature aResponder, Cultist cultist, Item target) {
/* 18 */     super(aResponder, "Meditation Path", aResponder.getName() + " Meditation Path", 722, target.getWurmId());
/* 19 */     this.cultist = cultist;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 25 */     setAnswer(answers);
/* 26 */     if (this.type == 0) {
/*    */       
/* 28 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*    */       return;
/*    */     } 
/* 31 */     if (this.type == 722)
/*    */     {
/* 33 */       if (this.cultist.getPath() == 4) {
/*    */         
/* 35 */         String prop = answers.getProperty("newcult");
/* 36 */         if (prop != null)
/*    */         {
/* 38 */           byte cultId = Byte.parseByte(prop);
/* 39 */           this.cultist.setPath(cultId);
/*    */           
/* 41 */           getResponder().getCommunicator().sendNormalServerMessage("You are now " + this.cultist.getCultistTitle());
/*    */           
/* 43 */           getResponder().refreshVisible();
/* 44 */           getResponder().getCommunicator().sendOwnTitles();
/*    */         }
/*    */         else
/*    */         {
/* 48 */           getResponder().getCommunicator().sendNormalServerMessage("Your path was not changed.");
/*    */         }
/*    */       
/*    */       } else {
/*    */         
/* 53 */         getResponder().getCommunicator().sendNormalServerMessage("You are not currently on the correct path to be able to change.");
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 61 */     StringBuilder buf = new StringBuilder();
/* 62 */     buf.append(getBmlHeader());
/* 63 */     if (this.cultist.getPath() == 4) {
/*    */       
/* 65 */       buf.append("text{type=\"bold\";text=\"Choose which path you want to change to. This cannot be undone. This change is only available once, and only from the path of insanity.\"}");
/*    */ 
/*    */       
/* 68 */       int cId = this.cultist.getPath();
/* 69 */       for (int i = 1; i < 6; i++) {
/*    */         
/* 71 */         if (i != cId)
/*    */         {
/*    */           
/* 74 */           buf.append("radio{ group='newcult'; id='" + i + "'; text='" + Cults.getPathNameFor((byte)i) + "'}");
/*    */         }
/*    */       } 
/* 77 */       buf.append(createAnswerButton2());
/* 78 */       getResponder().getCommunicator().sendBml(350, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ChangeMedPathQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class GMForceSpawnRiftLootQuestion
/*    */   extends Question
/*    */ {
/* 10 */   private static final Logger logger = Logger.getLogger(GMForceSpawnRiftLootQuestion.class.getName());
/*    */   
/*    */   public GMForceSpawnRiftLootQuestion(Creature aResponder) {
/* 13 */     super(aResponder, "Spawn Rift Loot", "Which item would you like to spawn?", 144, aResponder.getWurmId());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties aAnswer) {
/* 19 */     setAnswer(aAnswer);
/*    */   }
/*    */   
/*    */   public void sendQuestion() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GMForceSpawnRiftLootQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
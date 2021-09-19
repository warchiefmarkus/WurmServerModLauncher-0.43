/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.NoSuchPlayerException;
/*    */ import com.wurmonline.server.Players;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.players.Player;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FriendQuestion
/*    */   extends Question
/*    */ {
/* 36 */   private static final Logger logger = Logger.getLogger(FriendQuestion.class.getName());
/*    */ 
/*    */   
/*    */   public FriendQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/* 40 */     super(aResponder, aTitle, aQuestion, 25, aTarget);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 51 */     setAnswer(answers);
/* 52 */     QuestionParser.parseFriendQuestion(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/*    */     try {
/* 65 */       Player sender = Players.getInstance().getPlayer(this.target);
/* 66 */       StringBuilder buf = new StringBuilder();
/* 67 */       buf.append(getBmlHeader());
/* 68 */       buf.append("text{text='" + sender.getName() + " wants to add you to " + sender.getHisHerItsString() + " friends list.'}");
/*    */       
/* 70 */       buf.append("text{text='This will mean " + sender.getHeSheItString() + " will see you log on and off, and be able to allow you into structures " + sender
/* 71 */           .getHeSheItString() + " controls.'}");
/*    */       
/* 73 */       buf.append("text{text='Do you accept?'}");
/*    */       
/* 75 */       buf.append("radio{ group='join'; id='accept';text='Accept'}");
/* 76 */       buf.append("radio{ group='join'; id='decline';text='Decline';selected='true'}");
/* 77 */       buf.append(createAnswerButton2());
/* 78 */       getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */     }
/* 80 */     catch (NoSuchPlayerException nsp) {
/*    */       
/* 82 */       logger.log(Level.WARNING, "Player with id " + this.target + " trying to send a question, but cant be found?", (Throwable)nsp);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\FriendQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
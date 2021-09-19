/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.deities.Deity;
/*    */ import java.util.Properties;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RealDeathQuestion
/*    */   extends Question
/*    */ {
/*    */   private final Deity deity;
/*    */   
/*    */   public RealDeathQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, Deity aDeity) {
/* 42 */     super(aResponder, aTitle, aQuestion, 32, aTarget);
/* 43 */     this.deity = aDeity;
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
/* 54 */     setAnswer(answers);
/* 55 */     QuestionParser.parseRealDeathQuestion(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 66 */     StringBuilder buf = new StringBuilder();
/* 67 */     buf.append(getBmlHeader());
/* 68 */     buf.append("text{text='Your faith and soul are strong enough to become a Champion of " + this.deity.name + ".'};text{text=''}");
/*    */     
/* 70 */     buf.append("text{text='The decision to do this is dangerous.'}");
/* 71 */     buf.append("text{text='You will immediately get a boost of power, granted by " + this.deity.name + ".'}");
/* 72 */     buf.append("text{text='You will get some temporary bonuses to your characteristics (5 points each) and certain other skills.'}");
/* 73 */     buf.append("text{text='You will be able to withstand a lot more physical damage.'}");
/* 74 */     if (Servers.localServer.isChallengeOrEpicServer()) {
/*    */       
/* 76 */       buf.append("text{text='Champions are expected to face dangers and assault the enemy.'}");
/* 77 */       buf.append("text{text='This means that unless they enter enemy territory now and then they will lose certain powers.'}text{text=''}");
/*    */     } 
/* 79 */     buf.append("text{text='Your faith and favor will be set to max, and you may not lose or change deity.'}");
/* 80 */     buf.append("text{text='But first and foremost, your name will live in the history of Wurm forever!'}text{text=''}");
/* 81 */     buf.append("label{text='Champion points:';type='italic'}");
/* 82 */     buf.append("text{text=\"You start out with 0 champion points.\"}");
/* 83 */     buf.append("text{text=\"You gain 3 points for draining an enemy deed with 5+ guards.\"}");
/* 84 */     buf.append("text{text=\"You gain 30 points for recording a kill on an enemy champion.\"}");
/* 85 */     buf.append("text{text=\"You gain 1 point when you record a kill on an enemy with 50+ fight skill. You may gain point like this 1 time per day from the same player and 10 times from the same player.\"}text{text=''}");
/* 86 */     buf.append("text{text=\"You also get the champion points worth of any battle points you gain from any kill.\"}text{text=''}");
/* 87 */     buf.append("text{text=\"You lose 1 champion point per week down to 0.\"}");
/* 88 */     buf.append("text{text=\"You lose 1 champion point per successful item enchant spell.\"}");
/* 89 */     buf.append("text{text=\"All champion points you have gained as champion is recorded and saved in the Eternal Records for everyone to see, forever!\"}");
/* 90 */     buf.append("label{text='Losing champion status:';type='italic'}");
/* 91 */     buf.append("text{text='You lose champion status when you have died 3 times or after 6 months.'}text{text=''}");
/* 92 */     buf.append("text{text=\"When you lose champion status you lose 1 extra point in every stat on top of the 5 you gained when you became champion. Your channeling is reset to what it was before. Faith is set to 50. You can't go champion in 6 months.\"}text{text=''}");
/* 93 */     buf.append("text{text='Do you want to become a champion of " + this.deity.name + "?'}");
/* 94 */     buf.append("radio{ group='rd'; id='true';text='Yes'}");
/* 95 */     buf.append("radio{ group='rd'; id='false';text='No';selected='true'}");
/*    */     
/* 97 */     buf.append(createAnswerButton2());
/* 98 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\RealDeathQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.players.Player;
/*    */ import java.util.Date;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChallengeInfoQuestion
/*    */   extends Question
/*    */ {
/*    */   public ChallengeInfoQuestion(Creature aResponder) {
/* 45 */     super(aResponder, "Challenge Server Notification", "Server reset in " + 
/* 46 */         Server.getTimeFor(Servers.localServer.getChallengeEnds() - System.currentTimeMillis()) + "!", 117, aResponder
/*    */         
/* 48 */         .getWurmId());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 54 */     String val = answers.getProperty("okaycb");
/* 55 */     if (val != null)
/*    */     {
/* 57 */       if (val.equals("true"))
/* 58 */         getResponder().setFlag(27, true); 
/*    */     }
/* 60 */     ((Player)getResponder()).setQuestion(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 66 */     StringBuilder buf = new StringBuilder();
/* 67 */     buf.append(getBmlHeader());
/* 68 */     buf.append("text{text=\"\"}");
/* 69 */     buf.append("text{text=\"The challenge server resets after a certain time or when a set goal is achieved.\"}");
/*    */     
/* 71 */     buf
/* 72 */       .append("text{text=\"This server will shut down around " + new Date(Servers.localServer.getChallengeEnds()) + ".\"}");
/*    */     
/* 74 */     buf.append("text{text=\"\"}");
/* 75 */     buf
/* 76 */       .append("text{text=\"This means that all your items and skills will be lost. What you do keep is your money and titles. Also, please note that on this server settlements are harder to defend than normal and it usually requires some manpower. This means you may want to avoid deeding on your own.\"}");
/*    */     
/* 78 */     buf.append("text{text=\"\"}");
/* 79 */     buf
/* 80 */       .append("text{text=\"This is a competitive server and all investments are considered to be made in order to further your chances to win. Hence, when the server resets, settlements are disbanded and the mayor will be refunded what is left in upkeep but the cost of the tiles is a sunk cost.\"}");
/*    */     
/* 82 */     buf.append("text{text=\"\"}");
/* 83 */     buf
/* 84 */       .append("text{text=\"Also please note that the nature of this server means that GM presence is very limited, so please keep our no reimbursement policy in mind if you lose your stuff for whatever reason.\"}");
/*    */     
/* 86 */     buf.append("text{text=\"\"}");
/* 87 */     buf.append("text{text=''}");
/* 88 */     buf
/* 89 */       .append("checkbox{id='okaycb';selected='false';text='I have understood this message and do not need to see it ever again'}");
/*    */     
/* 91 */     buf.append(createAnswerButton2());
/* 92 */     getResponder().getCommunicator().sendBml(600, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ChallengeInfoQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
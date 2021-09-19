/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ public class SummonSoulAcceptQuestion
/*    */   extends Question
/*    */ {
/*    */   private final String summonerName;
/*    */   private final int summonX;
/*    */   private final int summonY;
/*    */   private final int summonLayer;
/*    */   private final int summonFloor;
/*    */   
/*    */   public SummonSoulAcceptQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, Creature summoner) {
/* 19 */     super(aResponder, aTitle, aQuestion, 27, aTarget);
/* 20 */     this.summonerName = summoner.getName();
/* 21 */     this.summonX = summoner.getTileX() * 4;
/* 22 */     this.summonY = summoner.getTileY() * 4;
/* 23 */     this.summonLayer = summoner.getLayer();
/* 24 */     this.summonFloor = summoner.getFloorLevel();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 30 */     setAnswer(answers);
/* 31 */     boolean accept = getAnswer().getProperty("summ").equals("true");
/* 32 */     if (accept) {
/*    */       
/* 34 */       if (getResponder().getEnemyPresense() > 0) {
/*    */         
/* 36 */         getResponder().getCommunicator().sendNormalServerMessage("You cannot be summoned right now, enemies are nearby.");
/*    */         return;
/*    */       } 
/* 39 */       getResponder().setTeleportPoints(this.summonX, this.summonY, this.summonLayer, this.summonFloor);
/* 40 */       if (getResponder().startTeleporting())
/*    */       {
/* 42 */         getResponder().getCommunicator().sendNormalServerMessage("You are summoned to the location of " + this.summonerName + ".");
/* 43 */         getResponder().getCommunicator().sendTeleport(false);
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 48 */       getResponder().getCommunicator().sendNormalServerMessage("You decline the summon from " + this.summonerName + ".");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/*    */     try {
/* 57 */       Creature asker2 = Server.getInstance().getCreature(this.target);
/* 58 */       StringBuilder buf = new StringBuilder();
/* 59 */       buf.append(getBmlHeader());
/* 60 */       buf.append("text{text='" + asker2.getName() + " would like to summon you to their location.'}text{text=''}text{text=''}text{text='Would you like to accept?'}");
/* 61 */       buf.append("text{text=''}");
/* 62 */       buf.append("radio{ group='summ'; id='true';text='Yes'}");
/* 63 */       buf.append("radio{ group='summ'; id='false';text='No';selected='true'}");
/* 64 */       buf.append(createAnswerButton2());
/* 65 */       getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/* 66 */       asker2.getCommunicator().sendNormalServerMessage("You request " + getResponder().getName() + " to be summoned to your location.");
/*    */     }
/* 68 */     catch (NoSuchCreatureException|com.wurmonline.server.NoSuchPlayerException noSuchCreatureException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SummonSoulAcceptQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
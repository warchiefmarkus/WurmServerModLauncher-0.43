/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.NoSuchPlayerException;
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.players.PlayerInfo;
/*    */ import com.wurmonline.server.players.PlayerInfoFactory;
/*    */ import com.wurmonline.shared.exceptions.WurmServerException;
/*    */ import com.wurmonline.shared.util.StringUtilities;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class SummonSoulQuestion
/*    */   extends Question
/*    */ {
/*    */   private boolean properlySent = false;
/* 18 */   private static final Logger logger = Logger.getLogger(SummonSoulQuestion.class.getName());
/*    */ 
/*    */ 
/*    */   
/*    */   public SummonSoulQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/* 23 */     super(aResponder, aTitle, aQuestion, 79, aTarget);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties aAnswers) {
/* 29 */     if (!this.properlySent) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     String name = aAnswers.getProperty("name");
/* 34 */     Creature soul = null;
/* 35 */     if (name != null && name.length() > 1)
/*    */     {
/* 37 */       soul = acquireSoul(StringUtilities.raiseFirstLetter(name));
/*    */     }
/* 39 */     if (soul == null || soul.getPower() > getResponder().getPower()) {
/*    */       
/* 41 */       getResponder().getCommunicator().sendNormalServerMessage("No such soul found.");
/*    */     }
/*    */     else {
/*    */       
/* 45 */       SummonSoulAcceptQuestion ssaq = new SummonSoulAcceptQuestion(soul, "Accept Summon?", "Would you like to accept a summon from " + getResponder().getName() + "?", getResponder().getWurmId(), getResponder());
/* 46 */       ssaq.sendQuestion();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static Creature acquireSoul(String name) {
/* 52 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/* 53 */     if (pinf != null && pinf.loaded) {
/*    */       
/*    */       try {
/*    */         
/* 57 */         return Server.getInstance().getCreature(pinf.wurmId);
/*    */       }
/* 59 */       catch (NoSuchPlayerException|com.wurmonline.server.creatures.NoSuchCreatureException ex) {
/*    */         
/* 61 */         logger.log(Level.WARNING, ex.getMessage());
/*    */       } 
/*    */     }
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 70 */     this.properlySent = true;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 75 */     String sb = getBmlHeader() + "text{text='Which soul do you wish to summon?'};label{text='Name:'};input{id='name';maxchars='40';text=\"\"};" + createAnswerButton2();
/* 76 */     getResponder().getCommunicator().sendBml(300, 300, true, true, sb, 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SummonSoulQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
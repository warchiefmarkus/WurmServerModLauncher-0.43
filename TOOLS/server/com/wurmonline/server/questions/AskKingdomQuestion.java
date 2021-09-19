/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.NoSuchPlayerException;
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*    */ import com.wurmonline.server.kingdom.Kingdoms;
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
/*    */ public final class AskKingdomQuestion
/*    */   extends Question
/*    */ {
/*    */   public AskKingdomQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/* 42 */     super(aResponder, aTitle, aQuestion, 38, aTarget);
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
/* 53 */     setAnswer(answers);
/* 54 */     QuestionParser.parseAskKingdomQuestion(this);
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
/* 67 */       Creature asker = Server.getInstance().getCreature(this.target);
/* 68 */       String kname = Kingdoms.getNameFor(asker.getKingdomId());
/* 69 */       StringBuilder buf = new StringBuilder();
/* 70 */       buf.append(getBmlHeader());
/*    */       
/* 72 */       if (asker.getKingdomId() != getResponder().getKingdomId()) {
/*    */         
/* 74 */         buf.append("text{text='" + asker.getName() + " asks if you wish to join " + kname + ".'}");
/* 75 */         if (getResponder().getAppointments() != 0L)
/* 76 */           buf.append("text{type='bold';text='If you accept, you will loose all your royal orders, titles and appointments!'}"); 
/* 77 */         if (!Servers.localServer.HOMESERVER && Kingdoms.getKingdom(asker.getKingdomId()).isCustomKingdom() && 
/* 78 */           getResponder().getCitizenVillage() != null)
/*    */         {
/* 80 */           if ((getResponder().getCitizenVillage().getMayor()).wurmId == getResponder().getWurmId())
/* 81 */             buf.append("text{type='bold';text='If you accept, your whole village will convert!'}"); 
/*    */         }
/* 83 */         buf.append("text{text='Do you want to join " + kname + "?'}");
/* 84 */         buf.append("radio{ group='conv'; id='true';text='Yes'}");
/* 85 */         buf.append("radio{ group='conv'; id='false';text='No';selected='true'}");
/*    */       } else {
/*    */         
/* 88 */         buf.append("text{text='You are already in " + kname + ".'}");
/* 89 */       }  buf.append(createAnswerButton2());
/* 90 */       getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/* 91 */       asker.getCommunicator().sendNormalServerMessage("You ask " + getResponder().getName() + " to join " + kname + ".");
/*    */     
/*    */     }
/* 94 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*    */ 
/*    */     
/* 97 */     } catch (NoSuchPlayerException noSuchPlayerException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AskKingdomQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.players.Player;
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
/*    */ public final class DropInfoQuestion
/*    */   extends Question
/*    */ {
/*    */   public DropInfoQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/* 33 */     super(aResponder, aTitle, aQuestion, 49, aTarget);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 39 */     String val = answers.getProperty("okaycb");
/* 40 */     if (val != null)
/*    */     {
/* 42 */       if (val.equals("true"))
/* 43 */         getResponder().setTheftWarned(true); 
/*    */     }
/* 45 */     ((Player)getResponder()).setQuestion(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 51 */     StringBuilder buf = new StringBuilder();
/* 52 */     buf.append(getBmlHeader());
/* 53 */     buf.append("header{text=\"Theft warning:\"}");
/* 54 */     buf.append("text{text=''}");
/* 55 */     buf.append("text{text=\"You are dropping an item.\"}");
/* 56 */     if (!Servers.localServer.PVPSERVER)
/* 57 */       buf
/* 58 */         .append("text{text=\"Usually, if you stay within one tile of the item nobody else may pick them up unless you team up with them.\"}"); 
/* 59 */     buf.append("text{text=\"\"}");
/* 60 */     buf
/* 61 */       .append("text{text=\"Otherwise, if this area is not on a settlement deed it may be stolen. Anyone may pass by and steal this unless you pick it up first. You need to build a house for your things to be protected.\"}");
/*    */     
/* 63 */     buf.append("text{text=''}");
/* 64 */     buf
/* 65 */       .append("checkbox{id='okaycb';selected='false';text='I have understood this message and do not need to see it ever again'}");
/*    */     
/* 67 */     buf.append(createAnswerButton2());
/* 68 */     getResponder().getCommunicator().sendBml(300, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\DropInfoQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
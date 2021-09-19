/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.players.Player;
/*    */ import com.wurmonline.server.villages.Village;
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
/*    */ public class KosWarningInfo
/*    */   extends Question
/*    */ {
/*    */   private final Village village;
/*    */   
/*    */   public KosWarningInfo(Creature aResponder, long aTarget, Village vill) {
/* 44 */     super(aResponder, "Kos Warning", "You have been put on the KOS list", 97, -10L);
/* 45 */     this.windowSizeX = 300;
/* 46 */     this.windowSizeY = 300;
/* 47 */     this.village = vill;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 53 */     String val = answers.getProperty("okaycb");
/* 54 */     if (val != null)
/*    */     {
/* 56 */       if (val.equals("true"))
/*    */       {
/* 58 */         ((Player)getResponder()).disableKosPopups(this.village.getId());
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 66 */     StringBuilder buf = new StringBuilder();
/* 67 */     buf.append(getBmlHeader());
/* 68 */     buf.append("text{text=\"\"}");
/* 69 */     buf.append("text{text=\"You have been deemed a criminal by " + this.village.getName() + ".\"}");
/* 70 */     buf.append("text{text=\"This means that you have to leave " + this.village.getName() + " within 2 minutes.\"}");
/* 71 */     buf.append("text{text=\"\"}");
/* 72 */     buf.append("text{text=\"If you fail to leave the area during this time you will be killed on sight by its guards.\"}");
/* 73 */     buf.append("text{text=\"\"}");
/* 74 */     buf
/* 75 */       .append("checkbox{id='okaycb';selected='false';text=\"I do not want to receive these warnings from " + this.village
/* 76 */         .getName() + " any more until server restart.\"}");
/* 77 */     buf.append(createOkAnswerButton());
/* 78 */     getResponder().getCommunicator().sendBml(this.windowSizeX, this.windowSizeY, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\KosWarningInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
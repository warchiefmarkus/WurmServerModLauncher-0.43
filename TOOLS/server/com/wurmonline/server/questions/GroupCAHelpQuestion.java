/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.webinterface.WcCAHelpGroupMessage;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GroupCAHelpQuestion
/*     */   extends Question
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(GroupCAHelpQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupCAHelpQuestion(Creature aResponder) {
/*  42 */     super(aResponder, "Group CA Helps", "Setup Grouping of CA Helps?", 115, aResponder.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  53 */     setAnswer(aAnswer);
/*  54 */     Creature responder = getResponder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     for (ServerEntry se : Servers.getAllServers()) {
/*     */       
/*  65 */       byte newCAHelpGroup = Byte.parseByte(aAnswer.getProperty(se.getAbbreviation()));
/*  66 */       if (se.getCAHelpGroup() != newCAHelpGroup) {
/*     */ 
/*     */         
/*  69 */         if (se.getId() != Servers.getLocalServerId()) {
/*     */           
/*  71 */           WcCAHelpGroupMessage wchgm = new WcCAHelpGroupMessage(newCAHelpGroup);
/*  72 */           wchgm.sendToServer(se.getId());
/*     */         } 
/*  74 */         se.updateCAHelpGroup(newCAHelpGroup);
/*     */       } 
/*     */     } 
/*     */     
/*  78 */     responder.getCommunicator().sendNormalServerMessage("CA Help Groups Updated.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  89 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*  90 */     buf.append("text{text=\"This allows you to combine CA Help tabs across servers.\"}");
/*  91 */     buf.append("text{text=\"\"}");
/*     */     
/*  93 */     buf.append("table{rows=\"" + ((Servers.getAllServers()).length + 1) + "\";cols=\"9\";text{type=\"bold\";text=\"Server\"};text{type=\"bold\";text=\"Single\"};text{type=\"bold\";text=\"Group0\"};text{type=\"bold\";text=\"Group1\"};text{type=\"bold\";text=\"Group2\"};text{type=\"bold\";text=\"Group3\"};text{type=\"bold\";text=\"Group4\"};text{type=\"bold\";text=\"Group5\"};text{type=\"bold\";text=\"Group6\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     for (ServerEntry se : Servers.getAllServers()) {
/*     */       
/* 106 */       String sea = se.getAbbreviation();
/* 107 */       byte seg = se.getCAHelpGroup();
/* 108 */       buf.append("label{text=\"" + sea + "\"};radio{group=\"" + sea + "\";id=\"-1\"" + ((seg == -1) ? ";selected=\"true\"" : "") + "};radio{group=\"" + sea + "\";id=\"0\"" + ((seg == 0) ? ";selected=\"true\"" : "") + "};radio{group=\"" + sea + "\";id=\"1\"" + ((seg == 1) ? ";selected=\"true\"" : "") + "};radio{group=\"" + sea + "\";id=\"2\"" + ((seg == 2) ? ";selected=\"true\"" : "") + "};radio{group=\"" + sea + "\";id=\"3\"" + ((seg == 3) ? ";selected=\"true\"" : "") + "};radio{group=\"" + sea + "\";id=\"4\"" + ((seg == 4) ? ";selected=\"true\"" : "") + "};radio{group=\"" + sea + "\";id=\"5\"" + ((seg == 5) ? ";selected=\"true\"" : "") + "};radio{group=\"" + sea + "\";id=\"6\"" + ((seg == 6) ? ";selected=\"true\"" : "") + "};");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     buf.append("}");
/* 119 */     buf.append(createAnswerButton2());
/*     */     
/* 121 */     getResponder().getCommunicator().sendBml(450, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GroupCAHelpQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
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
/*     */ public final class ChangeNameQuestion
/*     */   extends Question
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(ChangeNameQuestion.class.getName());
/*     */   
/*  43 */   private final int maxSize = 40;
/*  44 */   private final int minSize = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Item certificate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChangeNameQuestion(Creature aResponder, Item cert) {
/*  56 */     super(aResponder, "Name change", "Do you wish to change your name?", 109, cert.getWurmId());
/*  57 */     this.certificate = cert;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  68 */     setAnswer(answers);
/*  69 */     Creature responder = getResponder();
/*  70 */     String oldname = responder.getName();
/*  71 */     String newname = answers.getProperty("answer");
/*  72 */     String oldpw = answers.getProperty("oldpw");
/*  73 */     String newpw = answers.getProperty("newpw");
/*     */     
/*  75 */     if (this.certificate == null || this.certificate.deleted || this.certificate.getOwnerId() != getResponder().getWurmId()) {
/*     */       
/*  77 */       responder.getCommunicator().sendNormalServerMessage("You are no longer in possession of the certificate it seems.");
/*     */       return;
/*     */     } 
/*  80 */     if (oldpw == null || oldpw.length() < 6) {
/*     */       
/*  82 */       responder.getCommunicator().sendNormalServerMessage("The old password contains at least 6 characters.");
/*     */       return;
/*     */     } 
/*  85 */     if (newpw == null || newpw.length() < 6) {
/*     */       
/*  87 */       responder.getCommunicator().sendNormalServerMessage("The new password needs at least 6 characters.");
/*     */       return;
/*     */     } 
/*  90 */     if (newpw.length() > 40) {
/*     */       
/*  92 */       responder.getCommunicator().sendNormalServerMessage("The new password is over 40 characters long.");
/*     */       return;
/*     */     } 
/*  95 */     String hashedpw = "";
/*     */     
/*     */     try {
/*  98 */       hashedpw = LoginHandler.hashPassword(oldpw, LoginHandler.encrypt(LoginHandler.raiseFirstLetter(getResponder().getName())));
/*     */     }
/* 100 */     catch (Exception e) {
/*     */       
/* 102 */       logger.log(Level.WARNING, "Failed to encrypt pw for " + getResponder().getName() + " with " + oldpw);
/*     */     } 
/* 104 */     if (!hashedpw.equals(((Player)getResponder()).getSaveFile().getPassword())) {
/*     */       
/* 106 */       responder.getCommunicator().sendNormalServerMessage("You provided the wrong password.");
/*     */       return;
/*     */     } 
/* 109 */     if (newname == null || newname.length() < 3) {
/*     */       
/* 111 */       responder.getCommunicator().sendNormalServerMessage("Your name remains the same since it would be too short.");
/*     */       return;
/*     */     } 
/* 114 */     if (QuestionParser.containsIllegalCharacters(newname)) {
/*     */       
/* 116 */       responder.getCommunicator().sendNormalServerMessage("The name contains illegal characters.");
/*     */       return;
/*     */     } 
/* 119 */     if (newname.equalsIgnoreCase(getResponder().getName())) {
/*     */       
/* 121 */       responder.getCommunicator().sendNormalServerMessage("Your name remains the same.");
/*     */       return;
/*     */     } 
/* 124 */     if (newname.length() > 40) {
/*     */       
/* 126 */       responder.getCommunicator().sendNormalServerMessage("Too long. Your name remains the same.");
/*     */       return;
/*     */     } 
/* 129 */     if (Deities.isNameOkay(newname)) {
/*     */       
/* 131 */       if (Players.getInstance().doesPlayerNameExist(newname)) {
/*     */         
/* 133 */         responder.getCommunicator().sendNormalServerMessage("The name " + newname + " is already in use.");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 138 */         newname = LoginHandler.raiseFirstLetter(newname);
/* 139 */         LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 140 */         String toReturn = lsw.renamePlayer(oldname, newname, newpw, 
/* 141 */             getResponder().getPower());
/*     */         
/* 143 */         responder.getCommunicator().sendNormalServerMessage("You try to change the name from " + oldname + " to " + newname + " and set the password to '" + newpw + "'.");
/*     */ 
/*     */         
/* 146 */         responder.getCommunicator().sendNormalServerMessage("The result is:");
/* 147 */         responder.getCommunicator().sendNormalServerMessage(toReturn);
/* 148 */         if (!toReturn.contains("Error.")) {
/*     */           
/* 150 */           Items.destroyItem(this.certificate.getWurmId());
/* 151 */           logger.info(oldname + " (" + getResponder().getWurmId() + ") changed " + 
/* 152 */               getResponder().getHisHerItsString() + " name to " + newname + '.');
/*     */           
/* 154 */           Server.getInstance().broadCastSafe(oldname + " changed " + 
/* 155 */               getResponder().getHisHerItsString() + " name to " + newname + '.');
/* 156 */           getResponder().refreshVisible();
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 161 */       responder.getCommunicator().sendNormalServerMessage("The name  " + newname + " is illegal.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 172 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 173 */     buf.append("text{text=\"The name change system is spread across several servers and the name is used in a lot of complex situations.\"}");
/* 174 */     buf.append("text{text=\"It will not work perfectly and there will be certain data loss, especially regarding signatures and statistics.\"}");
/* 175 */     buf.append("text{text=\"In case you are not prepared to risk this you should close this window and sell the certificate back.\"}");
/*     */     
/* 177 */     buf.append("text{text=\"What would you like your name to be?\"}");
/* 178 */     buf.append("input{id=\"answer\";maxchars=\"40\";text=\"" + getResponder().getName() + "\"}");
/* 179 */     buf.append("text{text=\"Your password is required for security reasons. You can keep your old password.\"}");
/* 180 */     buf.append("harray{label{text=\"Old password\"};input{id=\"oldpw\";maxchars=\"40\";text=\"\"};}");
/* 181 */     buf.append("harray{label{text=\"New password\"};input{id=\"newpw\";maxchars=\"40\";text=\"\"};}");
/* 182 */     buf.append("text{text=\"\"}");
/* 183 */     buf.append(createAnswerButton2());
/*     */     
/* 185 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ChangeNameQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
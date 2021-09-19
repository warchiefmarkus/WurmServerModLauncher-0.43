/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.webinterface.WebInterfaceImpl;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public class ChangeEmailQuestion
/*     */   extends Question
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(ChangeEmailQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean providedPassword = false;
/*     */ 
/*     */ 
/*     */   
/*  49 */   String passProvided = "unknown";
/*  50 */   String passProvidedHashed = "unknown";
/*  51 */   String alertMessage = "";
/*     */ 
/*     */   
/*     */   public ChangeEmailQuestion(Creature aResponder) {
/*  55 */     super(aResponder, "Email address for " + aResponder.getName(), "Changing email address", 112, -10L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  67 */     if (!this.providedPassword) {
/*     */       
/*  69 */       String oldpw = answers.getProperty("pwinput");
/*  70 */       if (oldpw == null || oldpw.length() < 6) {
/*     */         
/*  72 */         getResponder().getCommunicator().sendNormalServerMessage("The old password contains at least 6 characters.");
/*     */         return;
/*     */       } 
/*  75 */       String hashedpw = "";
/*     */       
/*     */       try {
/*  78 */         hashedpw = LoginHandler.hashPassword(oldpw, LoginHandler.encrypt(LoginHandler.raiseFirstLetter(getResponder().getName())));
/*     */       }
/*  80 */       catch (Exception e) {
/*     */         
/*  82 */         logger.log(Level.WARNING, "Failed to encrypt pw for " + getResponder().getName() + " with " + oldpw);
/*     */       } 
/*  84 */       if (hashedpw.equals(((Player)getResponder()).getSaveFile().getPassword())) {
/*     */         
/*  86 */         this.providedPassword = true;
/*  87 */         this.passProvided = oldpw;
/*  88 */         this.passProvidedHashed = hashedpw;
/*  89 */         ChangeEmailQuestion ceq = new ChangeEmailQuestion(getResponder());
/*  90 */         ceq.providedPassword = true;
/*  91 */         ceq.passProvided = this.passProvided;
/*  92 */         ceq.passProvidedHashed = this.passProvidedHashed;
/*  93 */         ceq.sendQuestion();
/*     */       }
/*     */       else {
/*     */         
/*  97 */         getResponder().getCommunicator().sendNormalServerMessage("You provided the wrong password.");
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } else {
/* 103 */       boolean resend = false;
/* 104 */       this.alertMessage = "";
/* 105 */       String newEmail = answers.getProperty("emailAddress");
/* 106 */       String ppassword = answers.getProperty("pwinput2");
/* 107 */       if (ppassword == null || ppassword.length() < 2)
/* 108 */         ppassword = this.passProvided; 
/* 109 */       String pwQuestion = answers.getProperty("pwQuestion");
/* 110 */       if (pwQuestion == null || pwQuestion.length() < 5) {
/*     */         
/* 112 */         getResponder()
/* 113 */           .getCommunicator()
/* 114 */           .sendAlertServerMessage("You need to provide a password retrieval question at least 5 characters long. This is used on the website.");
/*     */         
/* 116 */         this.alertMessage = "You need to provide a password retrieval question at least 5 characters long. This is used on the website.";
/* 117 */         resend = true;
/*     */       } 
/* 119 */       String pwAnswer = answers.getProperty("pwAnswer");
/* 120 */       if (pwAnswer == null || pwAnswer.length() < 3) {
/*     */         
/* 122 */         getResponder()
/* 123 */           .getCommunicator()
/* 124 */           .sendAlertServerMessage("You need to provide a password retrieval answer at least 3 characters long. This is used on the website.");
/*     */         
/* 126 */         this.alertMessage = "You need to provide a password retrieval answer at least 3 characters long. This is used on the website.";
/* 127 */         resend = true;
/*     */       } 
/* 129 */       if (!resend)
/*     */       {
/* 131 */         if (newEmail != null && WebInterfaceImpl.isEmailValid(newEmail)) {
/*     */           
/* 133 */           if (!newEmail.equalsIgnoreCase((((Player)getResponder()).getSaveFile()).emailAddress)) {
/*     */             
/* 135 */             resend = false;
/* 136 */             getResponder().getCommunicator().sendNormalServerMessage("You try to change the email to '" + newEmail + "' - result:");
/*     */             
/* 138 */             LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 139 */             String isok = lsw.changeEmail(getResponder().getName(), getResponder().getName(), newEmail, ppassword, 
/* 140 */                 getResponder().getPower(), pwQuestion, pwAnswer);
/* 141 */             getResponder().getCommunicator().sendNormalServerMessage(isok);
/* 142 */             if (isok.contains("- ok")) {
/*     */               try
/*     */               {
/*     */                 
/* 146 */                 PlayerInfoFactory.changeEmail(getResponder().getName(), getResponder().getName(), newEmail, ppassword, 
/* 147 */                     getResponder().getPower(), pwQuestion, pwAnswer);
/* 148 */                 logger.log(Level.INFO, getResponder().getName() + " changed the email to " + newEmail);
/* 149 */                 (getResponder().getCommunicator()).lastChangedEmail = System.currentTimeMillis();
/*     */               }
/* 151 */               catch (IOException iox)
/*     */               {
/* 153 */                 logger.log(Level.INFO, getResponder().getName() + " FAILED changed the email to " + newEmail, new Exception());
/*     */                 
/* 155 */                 getResponder().getCommunicator().sendAlertServerMessage("The email was successfully changed on the login server, but not changed locally!");
/*     */               }
/*     */             
/*     */             }
/*     */           } else {
/*     */             
/* 161 */             getResponder().getCommunicator().sendAlertServerMessage("No change was made.");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 166 */           getResponder()
/* 167 */             .getCommunicator()
/* 168 */             .sendAlertServerMessage("The email " + newEmail + " is not a valid email address.");
/*     */           
/* 170 */           this.alertMessage = "The email " + newEmail + " is not a valid email address.";
/* 171 */           resend = true;
/*     */         } 
/*     */       }
/*     */       
/* 175 */       if (resend) {
/*     */         
/* 177 */         ChangeEmailQuestion ceq = new ChangeEmailQuestion(getResponder());
/* 178 */         ceq.providedPassword = true;
/* 179 */         ceq.passProvided = this.passProvided;
/* 180 */         ceq.passProvidedHashed = this.passProvidedHashed;
/* 181 */         ceq.alertMessage = this.alertMessage;
/* 182 */         ceq.sendQuestion();
/*     */       } 
/*     */     } 
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
/* 195 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 196 */     buf.append("text{text=\"Current email:\"}");
/* 197 */     buf.append("text{text=\"" + (((Player)getResponder()).getSaveFile()).emailAddress + "\"}");
/* 198 */     if (!this.providedPassword) {
/*     */       
/* 200 */       buf.append("text{text=\"Provide the account password in order to update email information:\"}");
/* 201 */       buf.append("input{id=\"pwinput\";maxchars=\"32\"};");
/*     */     }
/*     */     else {
/*     */       
/* 205 */       if (this.alertMessage != null)
/*     */       {
/* 207 */         buf.append("label{color=\"255,40,40\";text=\"" + this.alertMessage + "\"}");
/*     */       }
/* 209 */       buf.append("text{text=\"Desired email:\"}");
/* 210 */       buf.append("input{id=\"emailAddress\";maxchars=\"127\";text=\"" + 
/* 211 */           (((Player)getResponder()).getSaveFile()).emailAddress + "\"};");
/* 212 */       buf.append("text{text=\"If you want to change the email to one already in use, you need to provide the password for an account using that email.\"}");
/* 213 */       buf.append("text{text=\"If you want to change the email to one that is not in use, you need to leave this empty instead:\"}");
/* 214 */       buf.append("input{id=\"pwinput2\";maxchars=\"30\"};");
/* 215 */       buf.append("text{text=\"Question for password retrieval via website:\"}");
/*     */       
/* 217 */       buf.append("input{id=\"pwQuestion\";maxchars=\"127\";text=\"" + 
/* 218 */           (((Player)getResponder()).getSaveFile()).pwQuestion + "\"};");
/* 219 */       buf.append("text{text=\"Answer to that question:\"}");
/* 220 */       buf.append("input{id=\"pwAnswer\";maxchars=\"20\";text=\"" + 
/* 221 */           (((Player)getResponder()).getSaveFile()).pwAnswer + "\"};");
/*     */     } 
/* 223 */     buf.append(createAnswerButton2());
/*     */     
/* 225 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ChangeEmailQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
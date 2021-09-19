/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
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
/*     */ public final class ShutDownQuestion
/*     */   extends Question
/*     */ {
/*     */   public ShutDownQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  33 */     super(aResponder, aTitle, aQuestion, 13, aTarget);
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
/*  44 */     setAnswer(answers);
/*  45 */     QuestionParser.parseShutdownQuestion(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  64 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  66 */     if (Servers.localServer.testServer) {
/*     */       
/*  68 */       buf.append("harray{input{maxchars=\"2\";id=\"minutes\";text=\"0\"};label{text=\"minutes and\"}input{maxchars=\"2\";id=\"seconds\";text=\"30\"};label{text=\"seconds to shutdown\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  74 */       buf.append("label{text=\"Reason\"};");
/*  75 */       buf.append("input{id=\"reason\";text=\"Quick restart. Debugging.\"};");
/*     */     }
/*     */     else {
/*     */       
/*  79 */       buf.append("harray{input{maxchars=\"2\";id=\"minutes\";text=\"20\"};label{text=\"minutes and\"}input{maxchars=\"2\";id=\"seconds\";text=\"00\"};label{text=\"seconds to shutdown\"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  85 */       buf.append("label{text=\"Reason\"};");
/*  86 */       buf.append("input{id=\"reason\";text=\"Maintenance restart. Up to thirty minutes downtime.\"};");
/*     */     } 
/*  88 */     buf.append("checkbox{id=\"global\";text=\"Global\"};");
/*  89 */     String serverType = "local";
/*  90 */     if (Servers.isThisATestServer()) {
/*     */       
/*  92 */       if (Servers.isThisLoginServer()) {
/*  93 */         serverType = "test login";
/*     */       } else {
/*  95 */         serverType = "test";
/*     */       }
/*     */     
/*     */     }
/*  99 */     else if (Servers.isThisLoginServer()) {
/* 100 */       serverType = "live login";
/*     */     } else {
/* 102 */       serverType = "live";
/*     */     } 
/* 104 */     buf.append("text{text=\"You are currently on a " + serverType + " server (" + Servers.getLocalServerName() + ").\"};");
/*     */     
/* 106 */     buf.append("text{type=\"bold\";text=\"----- help -----\"};");
/* 107 */     buf.append("text{text=\"If using global from any server other than login, then it just tell the login server to shutdown all servers.\"};");
/* 108 */     buf.append("text{text=\"If using global from login server, it tells all servers to shutdown.\"};");
/* 109 */     buf.append("text{text=\"In both cases the login server would be the last to start shutting down.\"};");
/* 110 */     buf.append(createAnswerButton2());
/* 111 */     getResponder().getCommunicator().sendBml(365, 320, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ShutDownQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
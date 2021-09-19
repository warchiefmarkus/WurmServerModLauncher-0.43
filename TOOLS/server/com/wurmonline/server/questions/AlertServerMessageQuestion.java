/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.webinterface.WcGlobalAlarmMessage;
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
/*     */ public final class AlertServerMessageQuestion
/*     */   extends Question
/*     */ {
/*     */   public AlertServerMessageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  33 */     super(aResponder, aTitle, aQuestion, 45, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  39 */     String time1 = answers.getProperty("alt1");
/*  40 */     if (time1 == null || time1.length() == 0) {
/*  41 */       Server.timeBetweenAlertMess1 = Long.MAX_VALUE;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*  46 */         long seconds = Long.parseLong(time1);
/*  47 */         if (seconds <= 0L)
/*     */         {
/*  49 */           Server.timeBetweenAlertMess1 = Long.MAX_VALUE;
/*  50 */           Server.lastAlertMess1 = Long.MAX_VALUE;
/*     */         }
/*     */         else
/*     */         {
/*  54 */           Server.timeBetweenAlertMess1 = Math.max(10L, seconds) * 1000L;
/*  55 */           Server.lastAlertMess1 = 0L;
/*     */         }
/*     */       
/*  58 */       } catch (Exception e) {
/*     */         
/*  60 */         getResponder().getCommunicator().sendAlertServerMessage(time1 + " is not a number.");
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     String time2 = answers.getProperty("alt2");
/*  65 */     if (time2 == null || time2.length() == 0) {
/*  66 */       Server.timeBetweenAlertMess2 = Long.MAX_VALUE;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*  71 */         long seconds = Long.parseLong(time2);
/*  72 */         if (seconds <= 0L)
/*     */         {
/*  74 */           Server.timeBetweenAlertMess2 = Long.MAX_VALUE;
/*  75 */           Server.lastAlertMess2 = Long.MAX_VALUE;
/*     */         }
/*     */         else
/*     */         {
/*  79 */           Server.timeBetweenAlertMess2 = Math.max(10L, seconds) * 1000L;
/*  80 */           Server.lastAlertMess2 = 0L;
/*     */         }
/*     */       
/*  83 */       } catch (Exception e) {
/*     */         
/*  85 */         getResponder().getCommunicator().sendAlertServerMessage(time2 + " is not a number.");
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     String time3 = answers.getProperty("alt3");
/*  90 */     if (time3 == null || time3.length() == 0) {
/*  91 */       Server.timeBetweenAlertMess3 = Long.MAX_VALUE;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*  96 */         long seconds = Long.parseLong(time3);
/*  97 */         if (seconds <= 0L)
/*     */         {
/*  99 */           Server.timeBetweenAlertMess3 = Long.MAX_VALUE;
/* 100 */           Server.lastAlertMess3 = Long.MAX_VALUE;
/*     */         }
/*     */         else
/*     */         {
/* 104 */           Server.timeBetweenAlertMess3 = Math.max(10L, seconds) * 1000L;
/* 105 */           Server.lastAlertMess3 = 0L;
/*     */         }
/*     */       
/* 108 */       } catch (Exception e) {
/*     */         
/* 110 */         getResponder().getCommunicator().sendAlertServerMessage(time3 + " is not a number.");
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     String time4 = answers.getProperty("alt4");
/* 115 */     if (time4 == null || time4.length() == 0) {
/* 116 */       Server.timeBetweenAlertMess4 = Long.MAX_VALUE;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 121 */         long seconds = Long.parseLong(time3);
/* 122 */         if (seconds <= 0L)
/*     */         {
/* 124 */           Server.timeBetweenAlertMess4 = Long.MAX_VALUE;
/* 125 */           Server.lastAlertMess4 = Long.MAX_VALUE;
/*     */         }
/*     */         else
/*     */         {
/* 129 */           Server.timeBetweenAlertMess4 = Math.max(10L, seconds) * 1000L;
/* 130 */           Server.lastAlertMess4 = 0L;
/*     */         }
/*     */       
/* 133 */       } catch (Exception e) {
/*     */         
/* 135 */         getResponder().getCommunicator().sendAlertServerMessage(time4 + " is not a number.");
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     String mess1 = answers.getProperty("alm1");
/* 140 */     if (mess1 == null || mess1.length() == 0) {
/*     */       
/* 142 */       if (Server.alertMessage1.length() > 0)
/* 143 */         getResponder().getCommunicator().sendSafeServerMessage("Reset message 1."); 
/* 144 */       Server.alertMessage1 = "";
/* 145 */       Server.timeBetweenAlertMess1 = Long.MAX_VALUE;
/* 146 */       Server.lastAlertMess1 = Long.MAX_VALUE;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 153 */       String msg1 = mess1.replaceAll("\"", "");
/* 154 */       Server.alertMessage1 = msg1;
/* 155 */       getResponder().getCommunicator().sendSafeServerMessage("Set message 1.");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     String mess2 = answers.getProperty("alm2");
/* 167 */     if (mess2 == null || mess2.length() == 0) {
/*     */       
/* 169 */       if (Server.alertMessage2.length() > 0)
/* 170 */         getResponder().getCommunicator().sendSafeServerMessage("Reset message 2."); 
/* 171 */       Server.alertMessage2 = "";
/* 172 */       Server.timeBetweenAlertMess2 = Long.MAX_VALUE;
/* 173 */       Server.lastAlertMess2 = Long.MAX_VALUE;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 180 */       String msg2 = mess2.replaceAll("\"", "");
/* 181 */       Server.alertMessage2 = msg2;
/* 182 */       getResponder().getCommunicator().sendSafeServerMessage("Set message 2.");
/*     */     } 
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
/* 194 */     String mess3 = answers.getProperty("alm3");
/* 195 */     if (mess3 == null || mess3.length() == 0) {
/*     */       
/* 197 */       if (Server.alertMessage3.length() > 0)
/* 198 */         getResponder().getCommunicator().sendSafeServerMessage("Reset global alert 3."); 
/* 199 */       Server.alertMessage3 = "";
/* 200 */       Server.timeBetweenAlertMess3 = Long.MAX_VALUE;
/* 201 */       Server.lastAlertMess3 = Long.MAX_VALUE;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 208 */       String msg3 = mess3.replaceAll("\"", "");
/* 209 */       Server.alertMessage3 = msg3;
/* 210 */       getResponder().getCommunicator().sendSafeServerMessage("Set message 3.");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     String mess4 = answers.getProperty("alm4");
/* 222 */     if (mess4 == null || mess4.length() == 0) {
/*     */       
/* 224 */       if (Server.alertMessage4.length() > 0)
/* 225 */         getResponder().getCommunicator().sendSafeServerMessage("Reset global alert 4."); 
/* 226 */       Server.alertMessage4 = "";
/* 227 */       Server.timeBetweenAlertMess4 = Long.MAX_VALUE;
/* 228 */       Server.lastAlertMess4 = Long.MAX_VALUE;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 235 */       String msg4 = mess4.replaceAll("\"", "");
/* 236 */       Server.alertMessage4 = msg4;
/* 237 */       getResponder().getCommunicator().sendSafeServerMessage("Set message 4.");
/*     */     } 
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
/* 249 */     WcGlobalAlarmMessage wgam = new WcGlobalAlarmMessage(Server.alertMessage3, Server.timeBetweenAlertMess3, Server.alertMessage4, Server.timeBetweenAlertMess4);
/*     */     
/* 251 */     if (Servers.isThisLoginServer()) {
/* 252 */       wgam.sendFromLoginServer();
/*     */     } else {
/* 254 */       wgam.sendToLoginServer();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 260 */     StringBuilder sb = new StringBuilder();
/* 261 */     sb.append(getBmlHeader());
/* 262 */     sb.append("text{text='You may have 3 alert messages going, set at various intervals.'}");
/* 263 */     sb.append("text{text='If you omit the text or the time, or set time in seconds to 0 or less the message will not be displayed.'}");
/* 264 */     sb.append("text{text='The minimum number of seconds between alerts is 10.'}");
/*     */     
/* 266 */     sb.append("label{text='Alert message 1:'};input{id='alm1';text=\"" + Server.alertMessage1 + "\"}");
/*     */     
/* 268 */     sb.append("label{text='Seconds between polls:'};input{id='alt1';text='" + ((Server.timeBetweenAlertMess1 == Long.MAX_VALUE) ? 180L : (Server.timeBetweenAlertMess1 / 1000L)) + "'}");
/*     */     
/* 270 */     sb.append("label{text='Alert message 2:'};input{id='alm2';text=\"" + Server.alertMessage2 + "\"}");
/* 271 */     sb.append("label{text='Seconds between polls:'};input{id='alt2';text='" + ((Server.timeBetweenAlertMess2 == Long.MAX_VALUE) ? 180L : (Server.timeBetweenAlertMess2 / 1000L)) + "'}");
/*     */     
/* 273 */     sb.append("label{text=\"Global Alert message 3:\"};input{id='alm3';text=\"" + Server.alertMessage3 + "\"}");
/* 274 */     sb.append("label{text='Seconds between polls:'};input{id='alt3';text='" + ((Server.timeBetweenAlertMess3 == Long.MAX_VALUE) ? 180L : (Server.timeBetweenAlertMess3 / 1000L)) + "'}");
/*     */     
/* 276 */     sb.append("label{text=\"Global Alert message 4:\"};input{id='alm4';text=\"" + Server.alertMessage4 + "\"}");
/* 277 */     sb.append("label{text='Seconds between polls:'};input{id=\"alt4\";text='" + ((Server.timeBetweenAlertMess4 == Long.MAX_VALUE) ? 180L : (Server.timeBetweenAlertMess4 / 1000L)) + "'}");
/*     */ 
/*     */     
/* 280 */     sb.append(createAnswerButton2());
/* 281 */     getResponder().getCommunicator().sendBml(300, 350, true, true, sb.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AlertServerMessageQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
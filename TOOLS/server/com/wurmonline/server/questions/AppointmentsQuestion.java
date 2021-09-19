/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.Appointment;
/*     */ import com.wurmonline.server.kingdom.Appointments;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AppointmentsQuestion
/*     */   extends Question
/*     */ {
/*     */   public AppointmentsQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  47 */     super(aResponder, aTitle, aQuestion, 63, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  53 */     King k = King.getKing(getResponder().getKingdomId());
/*  54 */     if (k != null && k.kingid == getResponder().getWurmId()) {
/*     */       
/*  56 */       Appointments a = Appointments.getAppointments(k.era);
/*  57 */       if (a != null)
/*  58 */         addAppointments(a, k, answers); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAppointments(Appointments a, King k, Properties answers) {
/*     */     int x;
/*  64 */     for (x = 0; x < a.availableOrders.length; x++) {
/*     */       
/*  66 */       String val = answers.getProperty("order" + x);
/*  67 */       if (val != null && val.length() > 0) {
/*     */         
/*  69 */         Player p = Players.getInstance().getPlayerOrNull(LoginHandler.raiseFirstLetter(val));
/*  70 */         if (p == null) {
/*     */           
/*  72 */           getResponder().getCommunicator().sendNormalServerMessage("There is no person with the name " + val + " present in your kingdom.");
/*     */         }
/*     */         else {
/*     */           
/*  76 */           p.addAppointment(a.getAppointment(x + 30), getResponder());
/*     */         } 
/*     */       } 
/*  79 */     }  for (x = 0; x < a.availableTitles.length; x++) {
/*     */       
/*  81 */       String val = answers.getProperty("title" + x);
/*  82 */       if (val != null && val.length() > 0) {
/*     */         
/*  84 */         Player p = Players.getInstance().getPlayerOrNull(LoginHandler.raiseFirstLetter(val));
/*  85 */         if (p == null) {
/*     */           
/*  87 */           getResponder().getCommunicator().sendNormalServerMessage("There is no person with the name " + val + " present in your kingdom.");
/*     */         }
/*     */         else {
/*     */           
/*  91 */           p.addAppointment(a.getAppointment(x), getResponder());
/*     */         } 
/*     */       } 
/*  94 */     }  for (x = 0; x < a.officials.length; x++) {
/*     */       
/*  96 */       String val = answers.getProperty("official" + x);
/*  97 */       if (val == null || val.length() <= 0) {
/*     */         
/*  99 */         Appointment app = a.getAppointment(x + 1500);
/* 100 */         if (app != null && a.officials[x] > 0L)
/*     */         {
/* 102 */           Player oldp = Players.getInstance().getPlayerOrNull(a.officials[x]);
/* 103 */           if (oldp != null) {
/*     */             
/* 105 */             oldp.getCommunicator().sendNormalServerMessage("You are hereby notified that you have been removed of the office as " + app
/*     */                 
/* 107 */                 .getNameForGender(oldp.getSex()) + ".", (byte)2);
/*     */           }
/*     */           else {
/*     */             
/* 111 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(a.officials[x]);
/* 112 */             if (pinf != null)
/* 113 */               getResponder().getCommunicator().sendNormalServerMessage("Failed to notify " + pinf
/* 114 */                   .getName() + " that they have been removed from the office of " + app
/* 115 */                   .getNameForGender((byte)0) + ".", (byte)3); 
/*     */           } 
/* 117 */           getResponder().getCommunicator().sendNormalServerMessage("You vacate the office of " + app
/* 118 */               .getNameForGender((byte)0) + ".", (byte)2);
/* 119 */           a.setOfficial(x + 1500, 0L);
/*     */         }
/*     */       
/* 122 */       } else if (val.compareToIgnoreCase(PlayerInfoFactory.getPlayerName(a.officials[x])) != 0) {
/*     */         
/* 124 */         Player p = Players.getInstance().getPlayerOrNull(LoginHandler.raiseFirstLetter(val));
/* 125 */         if (p == null) {
/*     */           
/* 127 */           getResponder().getCommunicator().sendNormalServerMessage("There is no person with the name " + val + " present in your kingdom.");
/*     */         }
/*     */         else {
/*     */           
/* 131 */           p.addAppointment(a.getAppointment(x + 1500), getResponder());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addTitleStrings(Appointments a, King k, StringBuilder buf) {
/* 138 */     buf.append("text{type='italic';text='Titles'}");
/* 139 */     for (int x = 0; x < a.availableTitles.length; x++) {
/*     */       
/* 141 */       String key = "title" + x;
/* 142 */       if (a.getAvailTitlesForId(x) > 0) {
/*     */         
/* 144 */         Appointment app = a.getAppointment(x);
/* 145 */         if (app != null)
/*     */         {
/* 147 */           buf.append("harray{label{text='" + app.getNameForGender((byte)0) + " (" + a.getAvailTitlesForId(x) + ")'}};input{id='" + key + "'; maxchars='40'; text=''}");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     buf.append("text{text=''}");
/*     */   }
/*     */ 
/*     */   
/*     */   private void addOrderStrings(Appointments a, King k, StringBuilder buf) {
/* 157 */     buf.append("text{type='italic';text='Orders and decorations'}");
/* 158 */     for (int x = 0; x < a.availableOrders.length; x++) {
/*     */       
/* 160 */       String key = "order" + x;
/* 161 */       if (a.getAvailOrdersForId(x + 30) > 0) {
/*     */         
/* 163 */         Appointment app = a.getAppointment(x + 30);
/* 164 */         if (app != null)
/*     */         {
/* 166 */           buf.append("harray{label{text='" + app.getNameForGender((byte)0) + " (" + a
/* 167 */               .getAvailOrdersForId(x + 30) + ")'}};input{id='" + key + "'; maxchars='40'; text=''}");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     buf.append("text{text=''}");
/*     */   }
/*     */ 
/*     */   
/*     */   private void addOfficeStrings(Appointments a, King k, StringBuilder buf) {
/* 177 */     buf.append("text{type='italic';text='Offices. Note: You can only set these once per week and only to players who are online.'}");
/* 178 */     for (int x = 0; x < a.officials.length; x++) {
/*     */       
/* 180 */       String key = "official" + x;
/* 181 */       String oldval = "";
/* 182 */       long current = a.getOfficialForId(x + 1500);
/* 183 */       if (current > 0L) {
/*     */         
/* 185 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(current);
/* 186 */         if (pinf != null)
/*     */         {
/* 188 */           oldval = pinf.getName();
/*     */         }
/*     */       } 
/* 191 */       Appointment app = a.getAppointment(x + 1500);
/* 192 */       if (app != null) {
/*     */         
/* 194 */         String set = "(available)";
/* 195 */         if (a.isOfficeSet(x + 1500))
/*     */         {
/* 197 */           set = "(not available)";
/*     */         }
/* 199 */         String aname = app.getNameForGender((byte)0);
/* 200 */         if (getResponder().getSex() == 0 && app.getId() == 1507)
/* 201 */           aname = app.getNameForGender((byte)1); 
/* 202 */         buf.append("harray{label{text='" + aname + " " + set + "'}};input{id='" + key + "'; maxchars='40'; text='" + oldval + "'}");
/*     */       } 
/*     */     } 
/*     */     
/* 206 */     buf.append("text{text=''}");
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
/* 217 */     StringBuilder buf = new StringBuilder();
/* 218 */     buf.append(getBmlHeader());
/* 219 */     buf.append("header{text='Kingdom appointments:'}text{text=''}");
/* 220 */     King k = King.getKing(getResponder().getKingdomId());
/* 221 */     if (k != null && k.kingid == getResponder().getWurmId()) {
/*     */       
/* 223 */       Appointments a = Appointments.getAppointments(k.era);
/* 224 */       if (a == null) {
/*     */         return;
/*     */       }
/* 227 */       long timeLeft = a.getResetTimeRemaining();
/* 228 */       if (timeLeft <= 0L) {
/* 229 */         buf.append("text{text='Titles and orders will refresh shortly.'}");
/*     */       } else {
/* 231 */         buf.append("text{text='Titles and orders will refresh in " + Server.getTimeFor(timeLeft) + ".'}");
/* 232 */       }  buf.append("text{text=''}");
/* 233 */       addTitleStrings(a, k, buf);
/* 234 */       addOrderStrings(a, k, buf);
/* 235 */       addOfficeStrings(a, k, buf);
/*     */     } else {
/*     */       
/* 238 */       buf.append("text{text='You are not the current ruler.'}");
/* 239 */     }  buf.append(createAnswerButton2());
/* 240 */     getResponder().getCommunicator().sendBml(600, 600, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AppointmentsQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ public final class ReimbursementQuestion
/*     */   extends Question
/*     */ {
/*  31 */   private String[] nameArr = new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReimbursementQuestion(Creature aResponder, long aTarget) {
/*  39 */     super(aResponder, "Reimbursements", "These are your available reimbursements:", 50, aTarget);
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
/*  50 */     String key = "";
/*  51 */     String value = "";
/*     */     
/*  53 */     for (int x = 0; x < this.nameArr.length; x++) {
/*     */       
/*  55 */       int days = 0;
/*  56 */       int trinkets = 0;
/*  57 */       int silver = 0;
/*  58 */       boolean boktitle = false;
/*  59 */       boolean mbok = false;
/*  60 */       key = "silver" + this.nameArr[x];
/*  61 */       value = answers.getProperty(key);
/*  62 */       if (value != null) {
/*     */         
/*     */         try {
/*     */           
/*  66 */           silver = Integer.parseInt(value);
/*     */         }
/*  68 */         catch (Exception ex) {
/*     */           
/*  70 */           getResponder().getCommunicator().sendAlertServerMessage("Wrong amount of silver for " + this.nameArr[x]);
/*     */           return;
/*     */         } 
/*     */       }
/*  74 */       key = "days" + this.nameArr[x];
/*  75 */       value = answers.getProperty(key);
/*  76 */       if (value != null) {
/*     */         
/*     */         try {
/*     */           
/*  80 */           days = Integer.parseInt(value);
/*     */         }
/*  82 */         catch (Exception ex) {
/*     */           
/*  84 */           getResponder().getCommunicator().sendAlertServerMessage("Wrong amount of days for " + this.nameArr[x]);
/*     */           return;
/*     */         } 
/*     */       }
/*  88 */       key = "trinket" + this.nameArr[x];
/*  89 */       value = answers.getProperty(key);
/*  90 */       if (value != null) {
/*     */         
/*     */         try {
/*     */           
/*  94 */           trinkets = Integer.parseInt(value);
/*     */         }
/*  96 */         catch (Exception ex) {
/*     */           
/*  98 */           getResponder().getCommunicator().sendAlertServerMessage("Wrong amount of trinkets for " + this.nameArr[x]);
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/* 103 */       key = "mbok" + this.nameArr[x];
/* 104 */       value = answers.getProperty(key);
/* 105 */       if (value != null) {
/*     */         
/*     */         try {
/*     */           
/* 109 */           boktitle = Boolean.parseBoolean(value);
/* 110 */           if (boktitle) {
/* 111 */             mbok = true;
/*     */           }
/* 113 */         } catch (Exception ex) {
/*     */           
/* 115 */           getResponder().getCommunicator().sendAlertServerMessage("Unable to parse the MBoK/Title answer for " + this.nameArr[x]);
/*     */           return;
/*     */         } 
/*     */       }
/* 119 */       if (!boktitle) {
/*     */         
/* 121 */         key = "bok" + this.nameArr[x];
/* 122 */         value = answers.getProperty(key);
/* 123 */         if (value != null) {
/*     */           
/*     */           try {
/*     */             
/* 127 */             boktitle = Boolean.parseBoolean(value);
/*     */           }
/* 129 */           catch (Exception ex) {
/*     */             
/* 131 */             getResponder().getCommunicator().sendAlertServerMessage("Unable to parse the BoK/Title answer for " + this.nameArr[x]);
/*     */             
/*     */             return;
/*     */           } 
/*     */         }
/*     */       } 
/* 137 */       if (days > 0 || trinkets > 0 || silver > 0 || boktitle)
/*     */       {
/* 139 */         if (days < 0 || trinkets < 0 || silver < 0) {
/* 140 */           getResponder().getCommunicator().sendAlertServerMessage("Less than 0 value entered for " + this.nameArr[x]);
/*     */         } else {
/*     */           
/* 143 */           LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 144 */           getResponder().getCommunicator().sendNormalServerMessage(lsw
/* 145 */               .withDraw((Player)getResponder(), this.nameArr[x], (((Player)getResponder()).getSaveFile()).emailAddress, trinkets, silver, boktitle, mbok, days));
/*     */         } 
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
/* 159 */     LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 160 */     StringBuilder buf = new StringBuilder();
/* 161 */     buf.append(getBmlHeader());
/* 162 */     String s = lsw.getReimburseInfo((Player)getResponder());
/* 163 */     if (s.equals("text{text='You have no reimbursements pending.'}")) {
/* 164 */       ((Player)getResponder()).getSaveFile().setHasNoReimbursementLeft(true);
/*     */     } else {
/*     */       
/* 167 */       String ttext = s;
/* 168 */       String newName = "";
/* 169 */       Set<String> names = new HashSet<>();
/* 170 */       boolean keepGoing = true;
/* 171 */       while (keepGoing) {
/*     */         
/* 173 */         newName = getNextName(ttext);
/* 174 */         if (newName.equals("")) {
/* 175 */           keepGoing = false;
/*     */           continue;
/*     */         } 
/* 178 */         names.add(newName);
/*     */         
/* 180 */         ttext = ttext.substring(ttext.indexOf(" - '}") + 5, ttext.length());
/*     */       } 
/*     */       
/* 183 */       this.nameArr = names.<String>toArray(new String[names.size()]);
/*     */     } 
/* 185 */     buf.append(s);
/* 186 */     buf.append(createAnswerButton2());
/* 187 */     getResponder().getCommunicator().sendBml(400, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getNextName(String ttext) {
/* 192 */     int place = ttext.indexOf("Name=");
/* 193 */     if (place > 0)
/*     */     {
/* 195 */       return ttext.substring(place + 5, ttext.indexOf(" - '}"));
/*     */     }
/* 197 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ReimbursementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public final class ReferralQuestion
/*     */   extends Question
/*     */   implements MonetaryConstants
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(ReferralQuestion.class.getName());
/*     */   
/*     */   private Map<String, Byte> referrals;
/*     */   
/*     */   public ReferralQuestion(Creature aResponder, long aTarget) {
/*  47 */     super(aResponder, "Referrals", "These are your referrals:", 46, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReferrer(String receiver) {
/*     */     try {
/*  54 */       PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(receiver);
/*     */       
/*     */       try {
/*  57 */         pinf.load();
/*     */       }
/*  59 */       catch (IOException iox) {
/*     */         
/*  61 */         getResponder().getCommunicator().sendNormalServerMessage(receiver + " - no such player exists. Please check the spelling.");
/*     */         
/*     */         return;
/*     */       } 
/*  65 */       if (pinf.wurmId == getResponder().getWurmId()) {
/*     */         
/*  67 */         getResponder().getCommunicator().sendNormalServerMessage("You may not refer yourself.");
/*     */         return;
/*     */       } 
/*  70 */       if (pinf.getPaymentExpire() <= 0L) {
/*     */         
/*  72 */         getResponder().getCommunicator().sendNormalServerMessage(pinf
/*  73 */             .getName() + " has never had a premium account and may not receive referrals.");
/*     */         return;
/*     */       } 
/*  76 */       if (PlayerInfoFactory.addReferrer(pinf.wurmId, getResponder().getWurmId())) {
/*     */         
/*  78 */         ((Player)getResponder()).getSaveFile().setReferedby(pinf.wurmId);
/*  79 */         getResponder().getCommunicator().sendNormalServerMessage("Okay, you have set " + receiver + " as your referrer.");
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  84 */       getResponder().getCommunicator().sendNormalServerMessage("You have already awarded referral to that player.");
/*     */     }
/*  86 */     catch (Exception e) {
/*     */       
/*  88 */       logger.log(Level.WARNING, e.getMessage() + " " + receiver + " from " + getResponder().getName(), e);
/*  89 */       getResponder().getCommunicator()
/*  90 */         .sendNormalServerMessage("An error occurred. Please write a bug report about this.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void acceptReferrer(long wurmid, String awarderName, boolean money) {
/*  96 */     String name = awarderName;
/*  97 */     PlayerInfo pinf = null;
/*     */     
/*     */     try {
/* 100 */       long l = Long.parseLong(awarderName);
/* 101 */       pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(l);
/*     */     }
/* 103 */     catch (NumberFormatException nfe) {
/*     */       
/* 105 */       pinf = PlayerInfoFactory.createPlayerInfo(name);
/*     */       
/*     */       try {
/* 108 */         pinf.load();
/*     */       }
/* 110 */       catch (IOException iox) {
/*     */         
/* 112 */         logger.log(Level.WARNING, iox.getMessage());
/* 113 */         getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the player " + awarderName + " in the database.");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 118 */     if (pinf != null) {
/*     */ 
/*     */       
/*     */       try {
/* 122 */         if (PlayerInfoFactory.acceptReferer(wurmid, pinf.wurmId, money)) {
/*     */           
/*     */           try
/*     */           {
/* 126 */             if (money) {
/*     */               
/* 128 */               PlayerInfoFactory.addMoneyToBank(wurmid, 30000L, "Refby " + pinf.getName());
/*     */             } else {
/*     */               
/* 131 */               PlayerInfoFactory.addPlayingTime(wurmid, 0, 20, "Refby " + pinf.getName());
/* 132 */             }  getResponder().getCommunicator().sendNormalServerMessage("Okay, accepted the referral from " + awarderName + ". The reward will arrive soon if it has not already.");
/*     */ 
/*     */           
/*     */           }
/* 136 */           catch (Exception ex)
/*     */           {
/* 138 */             logger.log(Level.WARNING, "An error occurred wurmid: " + wurmid + ", awarderName: " + awarderName + ", money: " + money + " - " + ex
/* 139 */                 .getMessage(), ex);
/* 140 */             PlayerInfoFactory.revertReferer(wurmid, pinf.wurmId);
/* 141 */             getResponder().getCommunicator().sendNormalServerMessage("An error occured. Please try later or post a bug report.");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 146 */           getResponder().getCommunicator().sendNormalServerMessage("Failed to match " + awarderName + " to any existing referral.");
/*     */         }
/*     */       
/* 149 */       } catch (Exception ex) {
/*     */         
/* 151 */         getResponder().getCommunicator().sendNormalServerMessage("An error occured. Please try later or post a bug report.");
/*     */         
/* 153 */         logger.log(Level.WARNING, "An error occurred wurmid: " + wurmid + ", awarderName: " + awarderName + ", money: " + money + " - " + ex
/* 154 */             .getMessage(), ex);
/*     */       } 
/*     */     } else {
/*     */       
/* 158 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate " + awarderName + " in the database.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 164 */     if (((Player)getResponder()).getPaymentExpire() > 0L && !getResponder().hasFlag(63)) {
/*     */       
/* 166 */       LoginServerWebConnection lsw = null;
/* 167 */       String referrer = answers.getProperty("awarder");
/*     */       
/* 169 */       if (referrer != null && referrer.length() > 0) {
/*     */         
/* 171 */         referrer = LoginHandler.raiseFirstLetter(referrer);
/* 172 */         if ((((Player)getResponder()).getSaveFile()).referrer <= 0L) {
/*     */           
/* 174 */           if (referrer.length() > 2) {
/*     */             
/* 176 */             if (Servers.isRealLoginServer()) {
/*     */               
/* 178 */               addReferrer(referrer);
/*     */             }
/*     */             else {
/*     */               
/* 182 */               lsw = new LoginServerWebConnection();
/* 183 */               lsw.addReferrer((Player)getResponder(), referrer);
/*     */             } 
/*     */           } else {
/*     */             
/* 187 */             getResponder().getCommunicator().sendNormalServerMessage("The name " + referrer + " is too short.");
/*     */           } 
/*     */         } else {
/* 190 */           getResponder().getCommunicator().sendNormalServerMessage("Our records tell us that you have already referred someone.");
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 199 */       if (this.referrals.size() > 0)
/*     */       {
/* 201 */         for (Iterator<Map.Entry<String, Byte>> it = this.referrals.entrySet().iterator(); it.hasNext(); ) {
/*     */           
/* 203 */           Map.Entry<String, Byte> entry = it.next();
/* 204 */           String name = entry.getKey();
/* 205 */           byte referralType = ((Byte)entry.getValue()).byteValue();
/* 206 */           if (referralType == 0) {
/*     */             
/* 208 */             String a = answers.getProperty(name + "group");
/* 209 */             if (a != null) {
/*     */               
/* 211 */               if (a.equals(name + "silver")) {
/*     */                 
/* 213 */                 if (Servers.isRealLoginServer()) {
/*     */                   
/* 215 */                   acceptReferrer(getResponder().getWurmId(), name, true);
/*     */                   
/*     */                   continue;
/*     */                 } 
/* 219 */                 if (lsw == null)
/* 220 */                   lsw = new LoginServerWebConnection(); 
/* 221 */                 lsw.acceptReferrer(getResponder(), name, true);
/*     */                 continue;
/*     */               } 
/* 224 */               if (a.equals(name + "time")) {
/*     */                 
/* 226 */                 if (Servers.isRealLoginServer()) {
/*     */                   
/* 228 */                   acceptReferrer(getResponder().getWurmId(), name, false);
/*     */                   
/*     */                   continue;
/*     */                 } 
/* 232 */                 if (lsw == null)
/* 233 */                   lsw = new LoginServerWebConnection(); 
/* 234 */                 lsw.acceptReferrer(getResponder(), name, false);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 247 */     ((Player)getResponder()).lastReferralQuestion = System.currentTimeMillis();
/* 248 */     StringBuilder buf = new StringBuilder();
/* 249 */     buf.append(getBmlHeader());
/*     */     
/* 251 */     if (Servers.isRealLoginServer()) {
/* 252 */       this.referrals = PlayerInfoFactory.getReferrers(this.target);
/*     */     } else {
/* 254 */       this.referrals = (new LoginServerWebConnection()).getReferrers(getResponder(), this.target);
/* 255 */     }  if (this.referrals == null) {
/* 256 */       buf.append("text{text='An error occurred. Please try later.'}");
/*     */     } else {
/*     */       
/* 259 */       if (((Player)getResponder()).getPaymentExpire() > 0L && !getResponder().hasFlag(63)) {
/*     */         
/* 261 */         if ((((Player)getResponder()).getSaveFile()).referrer <= 0L) {
/*     */           
/* 263 */           buf.append("text{text='You may reward a player for directing you to Wurm Online.'}");
/* 264 */           buf.append("text{text='That player must have or have had a premium account once.'}");
/* 265 */           buf.append("text{text='You may only do this once, and you may not refer to yourself.'}");
/*     */           
/* 267 */           buf.append("text{text='The player you type in the box will see that you rewarded him, and may opt to receive 20 days premium playing time or the amount of 3 silver coins in his or her bank account, plus an hour of skillgain speed bonus.'}");
/* 268 */           buf.append("text{text='This reward is therefor valuable, and should not be given away unless you have given it proper consideration.'}");
/* 269 */           buf.append("text{text='You may currently not change referrer once you select one, even if he/she does not collect the reward.'}");
/* 270 */           buf.append("harray{label{text='Who do you wish to award?'};input{id='awarder'; text='';maxchars='40'}}");
/*     */         } else {
/*     */           
/* 273 */           buf.append("text{text='You have already used up your referral award.'};text{text=''}");
/*     */         } 
/*     */       } else {
/* 276 */         buf.append("text{text='You are not playing a premier account, so you can not refer to anyone.'};text{text=''}");
/* 277 */       }  if (this.referrals.size() > 0) {
/*     */         
/* 279 */         buf.append("text{type='bold';text='Here are your referrals:'}");
/*     */         
/* 281 */         for (Iterator<Map.Entry<String, Byte>> it = this.referrals.entrySet().iterator(); it.hasNext(); )
/*     */         {
/* 283 */           Map.Entry<String, Byte> entry = it.next();
/* 284 */           String name = entry.getKey();
/* 285 */           byte referralType = ((Byte)entry.getValue()).byteValue();
/* 286 */           if (referralType == 0) {
/*     */             
/* 288 */             buf.append("harray{label{text=\"" + name + "\"};radio{id=\"" + name + "none\";text='Decide later';group=\"" + name + "group\";selected='true'};radio{id=\"" + name + "silver\";text='3 silver';group=\"" + name + "group\"};radio{id=\"" + name + "time\";text='20 days and 1 hour sleep bonus';group=\"" + name + "group\"}}");
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 293 */           if (referralType == 1) {
/* 294 */             buf.append("harray{label{text=\"" + name + "\"};label{text='   3 silver'}}"); continue;
/*     */           } 
/* 296 */           buf.append("harray{label{text=\"" + name + "\"};label{text='   20 days'}}");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 301 */         buf.append("text{text='Nobody has referred to you, so you have no award to collect right now.'}");
/*     */       } 
/* 303 */     }  buf.append(createAnswerButton2());
/* 304 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ReferralQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
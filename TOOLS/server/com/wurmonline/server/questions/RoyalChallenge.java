/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
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
/*     */ public class RoyalChallenge
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  38 */   private static Logger logger = Logger.getLogger(RoyalChallenge.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RoyalChallenge(Creature aResponder) {
/*  49 */     super(aResponder, "Challenge for power", "You have been challenged", 94, aResponder.getWurmId());
/*  50 */     if (aResponder.isPlayer())
/*     */     {
/*  52 */       ((Player)aResponder).sentChallenge = true;
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
/*     */   public void answer(Properties aAnswers) {
/*  64 */     if (getResponder().isKing()) {
/*     */       
/*  66 */       King k = King.getKing(getResponder().getKingdomId());
/*  67 */       if (k != null) {
/*     */         
/*  69 */         boolean decline = false;
/*  70 */         boolean accept = false;
/*  71 */         String key2 = "decide";
/*  72 */         String val2 = aAnswers.getProperty("decide");
/*  73 */         if (val2 != null && val2.equals("decline")) {
/*  74 */           decline = true;
/*  75 */         } else if (val2 != null && val2.equals("accept")) {
/*  76 */           accept = true;
/*  77 */         }  if (decline) {
/*     */           
/*  79 */           getResponder().getCommunicator().sendNormalServerMessage("You decline the challenge.");
/*     */           
/*  81 */           k.setChallengeDeclined();
/*  82 */           if (k.hasFailedAllChallenges()) {
/*     */             
/*  84 */             getResponder().getCommunicator().sendAlertServerMessage("The people of " + 
/*  85 */                 Kingdoms.getNameFor(k.kingdom) + " may now vote you from the throne at the duelling ring.");
/*     */ 
/*     */             
/*  88 */             HistoryManager.addHistory(getResponder().getName(), " may now be voted away from the throne within one week at the duelling stone.");
/*     */             
/*  90 */             Server.getInstance().broadCastNormal(
/*  91 */                 getResponder().getName() + " may now be voted away from the throne within one week at the duelling stone.");
/*     */ 
/*     */             
/*  94 */             logger.log(Level.INFO, getResponder().getName() + " may now be voted away.");
/*     */           } 
/*     */           return;
/*     */         } 
/*  98 */         if (!accept) {
/*     */           
/* 100 */           getResponder().getCommunicator()
/* 101 */             .sendNormalServerMessage("You decide to wait with answering the challenge.");
/* 102 */           long timeLeft = 604800000L + k.getChallengeDate() - System.currentTimeMillis();
/* 103 */           String tl = Server.getTimeFor(timeLeft);
/* 104 */           getResponder().getCommunicator().sendNormalServerMessage("Unless you answer this challenge within " + tl + " you will automatically have declined " + (k
/*     */               
/* 106 */               .getDeclinedChallengesNumber() + 1) + " challenges.");
/* 107 */           getResponder().getCommunicator().sendNormalServerMessage("You may bring this window up again by typing /challenge.");
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 112 */         String keyday = "day";
/* 113 */         String valday = aAnswers.getProperty("day");
/* 114 */         if (valday != null && valday.length() > 0) {
/*     */ 
/*     */           
/*     */           try {
/* 118 */             int day = Integer.parseInt(valday);
/* 119 */             String keyhour = "hours";
/* 120 */             String valhour = aAnswers.getProperty("hours");
/* 121 */             if (valhour != null && valhour.length() > 0) {
/*     */ 
/*     */               
/*     */               try {
/*     */                 
/* 126 */                 int hour = Integer.parseInt(valhour);
/*     */                 
/* 128 */                 long time = System.currentTimeMillis() + day * 86400000L + hour * 3600000L;
/* 129 */                 if (Servers.localServer.testServer) {
/*     */                   
/* 131 */                   getResponder()
/* 132 */                     .getCommunicator()
/* 133 */                     .sendSafeServerMessage("You have accepted the challenge and since this is the test server you must be at the duelling ring exactly in 2 minutes instead of " + day + " days and " + hour + " hours (which is " + (day * 24 + hour) + " hours away). You must stay there a bit more than half an hour until you receive a message.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 141 */                   time = System.currentTimeMillis() + 120000L;
/*     */                 } else {
/*     */                   
/* 144 */                   getResponder()
/* 145 */                     .getCommunicator()
/* 146 */                     .sendSafeServerMessage("You have accepted the challenge and must be at the duelling ring exactly in " + day + " days and " + hour + " hours (which is " + (day * 24 + hour) + " hours away). You must stay there a bit more than half an hour until you receive a message.");
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 155 */                 k.setChallengeAccepted(time);
/*     */ 
/*     */                 
/* 158 */                 Message mess = new Message(getResponder(), (byte)10, Kingdoms.getChatNameFor(getResponder().getKingdomId()), "<" + getResponder().getName() + "> has accepted the challenge and must be at the duelling ring exactly in " + day + " days and " + hour + " hours");
/*     */ 
/*     */ 
/*     */                 
/* 162 */                 Player[] playarr = Players.getInstance().getPlayers();
/*     */                 
/* 164 */                 byte windowKingdom = getResponder().getKingdomId();
/*     */                 
/* 166 */                 for (Player lElement : playarr)
/*     */                 {
/* 168 */                   if (windowKingdom == lElement.getKingdomId() || lElement.getPower() > 0) {
/* 169 */                     lElement.getCommunicator().sendMessage(mess);
/*     */                   }
/*     */                 }
/*     */               
/* 173 */               } catch (NumberFormatException nfe) {
/*     */                 
/* 175 */                 getResponder().getCommunicator().sendAlertServerMessage("You must select a valid hour in order to accept the challenge!");
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 180 */               getResponder().getCommunicator().sendAlertServerMessage("You must select a valid hour in order to accept the challenge!");
/*     */             }
/*     */           
/* 183 */           } catch (NumberFormatException nfe) {
/*     */             
/* 185 */             getResponder().getCommunicator().sendAlertServerMessage("You must select a valid day in order to accept the challenge!");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 190 */           getResponder().getCommunicator().sendAlertServerMessage("You must select a valid day in order to accept the challenge!");
/*     */         } 
/*     */         return;
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
/* 205 */     StringBuilder buf = new StringBuilder();
/* 206 */     buf.append(getBmlHeader());
/* 207 */     if (getResponder().isKing()) {
/*     */       
/* 209 */       King k = King.getKing(getResponder().getKingdomId());
/* 210 */       if (k != null) {
/*     */         
/* 212 */         buf.append("text{text='Your people has challenged your rulership at the duelling ring.'};text{text=''}");
/* 213 */         buf.append("text{text='The duelling ring is an area where your people can fight and kill eachother without harm to their reputation.'};text{text=''}");
/* 214 */         buf.append("text{text='Eventually you have to go there in order to present yourself and answer the challenge.'}");
/* 215 */         buf.append("text{text='You will have to stay in the proximity of the ring for at least half an hour.'}");
/* 216 */         buf.append("text{text=''}");
/* 217 */         buf.append("text{text='In case you leave the proximity or die within the proximity during this time you may be removed from the throne of " + 
/* 218 */             Kingdoms.getNameFor(getResponder().getKingdomId()) + ".'}");
/* 219 */         buf.append("text{text=''}");
/* 220 */         buf.append("text{text='You may be challenged once per week and must respond within one week or you are considered to have declined. You may decline these challenges two times but the third time you are strongly adviced to accept. In case you accept the challenge the first week nobody may challenge you for two more weeks.'}");
/* 221 */         buf.append("text{text=''}");
/* 222 */         buf.append("text{text='If you fail to respond to or decline the third challenge you may also be removed from the throne if enough people vote at the duelling ring.'}");
/* 223 */         buf.append("text{text=''}");
/* 224 */         if (k.hasFailedToRespondToChallenge()) {
/*     */           
/* 226 */           buf.append("text{text=\"You failed to accept a challenge in time and now have to wait for a decision by your people.\"}");
/*     */         }
/* 228 */         else if (k.getChallengeAcceptedDate() > 0L) {
/*     */           
/* 230 */           if (k.getChallengeAcceptedDate() > System.currentTimeMillis()) {
/*     */             
/* 232 */             long timeLeft = k.getChallengeAcceptedDate() - System.currentTimeMillis();
/* 233 */             String tl = Server.getTimeFor(timeLeft);
/* 234 */             buf.append("text{text=\"You have accepted to be at the duelling ring in " + tl + " and defend your sovereignty.\"}");
/*     */           }
/*     */           else {
/*     */             
/* 238 */             buf.append("text{text=\"You have accepted to be at the duelling ring now.\"}");
/*     */           } 
/* 240 */         } else if (k.getChallengeDate() < System.currentTimeMillis()) {
/*     */           
/* 242 */           long timeLeft = 604800000L + k.getChallengeDate() - System.currentTimeMillis();
/* 243 */           String tl = Server.getTimeFor(timeLeft);
/* 244 */           buf.append("text{text=\"Unless you answer this challenge within " + tl + " you will have declined " + (k
/* 245 */               .getDeclinedChallengesNumber() + 1) + " challenges.\"}");
/*     */           
/* 247 */           if (k.getDeclinedChallengesNumber() + 1 == 3)
/*     */           {
/* 249 */             buf.append("text{text=\"Since this would be the third time your people will be able to vote you from the throne.\"}");
/*     */           }
/* 251 */           buf.append("text{text=''}");
/* 252 */           buf.append("radio{ group='decide'; id='decline';text=\" Decline\"}");
/* 253 */           buf.append("radio{ group='decide'; id='accept';text=\" Accept\"}");
/* 254 */           buf.append("radio{ group='decide'; id='wait';text=\" Wait\";selected=\"true\"}");
/* 255 */           buf.append("text{text=\"If you wish to wait with this decision, you may also close this window.\"}");
/* 256 */           buf.append("text{text=''}");
/* 257 */           buf.append("text{text=\"You decide yourself when you wish to enter the duelling ring given the options below.\"}");
/* 258 */           if (Servers.localServer.testServer) {
/*     */             
/* 260 */             buf.append("label{text=\"This is the test server and it will always be in 2 minutes:\"}");
/* 261 */             buf.append("text{text=''}");
/*     */           } 
/* 263 */           buf.append("label{text=\"First select in how many days (24 hour periods). Minimum is in 2 days:\"}");
/* 264 */           buf.append("radio{ group='day'; id='2';text='2'}");
/* 265 */           buf.append("radio{ group='day'; id='3';text='3'}");
/* 266 */           buf.append("radio{ group='day'; id='4';text='4'}");
/* 267 */           buf.append("radio{ group='day'; id='5';text='5'}");
/* 268 */           buf.append("radio{ group='day'; id='6';text='6'}");
/* 269 */           buf.append("label{text='Then select in how many hours to pinpoint your appearance.:'}");
/* 270 */           buf.append("radio{ group='hours'; id='0';text='0'}");
/* 271 */           buf.append("radio{ group='hours'; id='3';text='3'}");
/* 272 */           buf.append("radio{ group='hours'; id='6';text='6'}");
/* 273 */           buf.append("radio{ group='hours'; id='9';text='9'}");
/* 274 */           buf.append("radio{ group='hours'; id='12';text='12'}");
/* 275 */           buf.append("radio{ group='hours'; id='15';text='15'}");
/* 276 */           buf.append("radio{ group='hours'; id='18';text='18'}");
/* 277 */           buf.append("radio{ group='hours'; id='21';text='21'}");
/*     */         } 
/* 279 */         buf.append("label{text='Good luck!'}");
/*     */       } else {
/*     */         
/* 282 */         buf.append("label{text='You are not the ruler!'}");
/*     */       } 
/*     */     } else {
/* 285 */       buf.append("label{text='You are not the ruler!'}");
/* 286 */     }  buf.append(createAnswerButton2());
/* 287 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\RoyalChallenge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
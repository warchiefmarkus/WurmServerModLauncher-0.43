/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
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
/*     */ public final class PlayerPaymentQuestion
/*     */   extends Question
/*     */   implements MonetaryConstants
/*     */ {
/*     */   public static final long silverCost = 10L;
/*     */   public static final long silverCostFirstTime = 2L;
/*     */   public static final long silverCost15Day = 5L;
/*     */   
/*     */   public PlayerPaymentQuestion(Creature aResponder) {
/*  42 */     super(aResponder, "Purchase Premium Time", "Choose an option from the below:", 20, aResponder.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  48 */     setAnswer(answers);
/*     */     
/*  50 */     long money = getResponder().getMoney();
/*     */     
/*  52 */     if (((Player)getResponder()).getSaveFile().getPaymentExpire() <= 0L || getResponder().hasFlag(63)) {
/*     */       
/*  54 */       if (money < 20000L) {
/*     */         
/*  56 */         getResponder().getCommunicator().sendNormalServerMessage("You need at least 2 silver in your account to purchase premium game time.");
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/*  63 */         boolean purchaseFirstTime = Boolean.parseBoolean(answers.getProperty("purchaseFirstTime"));
/*  64 */         long referredBy = (((Player)getResponder()).getSaveFile()).referrer;
/*  65 */         if (purchaseFirstTime && referredBy == 0L) {
/*     */ 
/*     */           
/*     */           try {
/*  69 */             if (getResponder().chargeMoney(20000L)) {
/*     */               
/*  71 */               LoginServerWebConnection lsw = new LoginServerWebConnection();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  78 */               lsw.addPlayingTime(getResponder(), getResponder().getName(), 0, 30, "firstBuy" + (
/*  79 */                   System.currentTimeMillis() - 1400000000000L) + Servers.localServer.name);
/*  80 */               getResponder().getCommunicator().sendSafeServerMessage("Your request for playing time is being processed. It may take up to half an hour until the system is fully updated.");
/*     */ 
/*     */               
/*  83 */               ((Player)getResponder()).getSaveFile().setReferedby(getResponder().getWurmId());
/*     */               
/*  85 */               getResponder().setFlag(63, false);
/*     */             } else {
/*     */               
/*  88 */               getResponder().getCommunicator().sendAlertServerMessage("Failed to charge you 2 silvers. Please try later.");
/*     */             }
/*     */           
/*  91 */           } catch (IOException ex) {
/*     */             
/*  93 */             getResponder().getCommunicator().sendSafeServerMessage("Your request for playing time could not be processed.");
/*     */           }
/*     */         
/*  96 */         } else if (purchaseFirstTime && referredBy != 0L) {
/*     */           
/*  98 */           getResponder().getCommunicator().sendNormalServerMessage("You have already purchased this option once, if you still have not received your play time after 30 minutes, please contact /support.");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 103 */           getResponder().getCommunicator().sendNormalServerMessage("You decide not to buy any premium game time for now.");
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 109 */       String purchaseStr = answers.getProperty("purchase");
/* 110 */       if ("30day".equals(purchaseStr)) {
/*     */         
/* 112 */         if (money >= 100000L) {
/*     */ 
/*     */           
/*     */           try {
/* 116 */             if (getResponder().chargeMoney(100000L))
/*     */             {
/* 118 */               LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 119 */               lsw.addPlayingTime(getResponder(), getResponder().getName(), 0, 30, System.currentTimeMillis() + Servers.localServer.name);
/* 120 */               getResponder().getCommunicator().sendSafeServerMessage("Your request for playing time is being processed. It may take up to half an hour until the system is fully updated.");
/*     */ 
/*     */               
/* 123 */               Economy.getEconomy().getKingsShop().setMoney(Economy.getEconomy().getKingsShop().getMoney() + 30000L);
/* 124 */               logger.log(Level.INFO, getResponder().getName() + " purchased 1 month premium time for " + 10L + " silver coins. " + 30000L + " iron added to king.");
/*     */             }
/*     */             else
/*     */             {
/* 128 */               getResponder().getCommunicator().sendAlertServerMessage("Failed to charge you 10 silvers. Please try later.");
/*     */             }
/*     */           
/* 131 */           } catch (IOException ex) {
/*     */             
/* 133 */             getResponder().getCommunicator().sendSafeServerMessage("Your request for playing time could not be processed.");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 138 */           getResponder().getCommunicator().sendNormalServerMessage("You need at least 10 silver in your account to purchase 30 days of premium game time.");
/*     */         }
/*     */       
/*     */       }
/* 142 */       else if ("15day".equals(purchaseStr)) {
/*     */         
/* 144 */         if (money >= 50000L) {
/*     */ 
/*     */           
/*     */           try {
/* 148 */             if (getResponder().chargeMoney(50000L))
/*     */             {
/* 150 */               LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 151 */               lsw.addPlayingTime(getResponder(), getResponder().getName(), 0, 15, System.currentTimeMillis() + Servers.localServer.name);
/* 152 */               getResponder().getCommunicator().sendSafeServerMessage("Your request for playing time is being processed. It may take up to half an hour until the system is fully updated.");
/*     */ 
/*     */               
/* 155 */               Economy.getEconomy().getKingsShop().setMoney(Economy.getEconomy().getKingsShop().getMoney() + -20000L);
/* 156 */               logger.log(Level.INFO, getResponder().getName() + " purchased 1 month premium time for " + 5L + " silver coins. " + -20000L + " iron added to king.");
/*     */             }
/*     */             else
/*     */             {
/* 160 */               getResponder().getCommunicator().sendAlertServerMessage("Failed to charge you 10 silvers. Please try later.");
/*     */             }
/*     */           
/* 163 */           } catch (IOException ex) {
/*     */             
/* 165 */             getResponder().getCommunicator().sendSafeServerMessage("Your request for playing time could not be processed.");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 170 */           getResponder().getCommunicator().sendNormalServerMessage("You need at least 5 silver in your account to purchase 15 days of premium game time.");
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 176 */         getResponder().getCommunicator().sendNormalServerMessage("You decide not to buy any premium game time for now.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 184 */     StringBuilder buf = new StringBuilder();
/* 185 */     long money = getResponder().getMoney();
/* 186 */     Change change = Economy.getEconomy().getChangeFor(money);
/* 187 */     buf.append(getBmlHeader());
/*     */     
/* 189 */     if (Features.Feature.RETURNER_PACK_REGISTRATION.isEnabled())
/*     */     {
/* 191 */       if (getResponder().hasFlag(47)) {
/* 192 */         buf.append("text{text='You are successfully registered for the returner pack!'}");
/*     */       }
/*     */     }
/* 195 */     if (((Player)getResponder()).getSaveFile().getPaymentExpire() <= 0L || getResponder().hasFlag(63)) {
/*     */       
/* 197 */       if (money < 20000L)
/*     */       {
/* 199 */         buf.append("text{text='As this is your first time purchasing premium game time, you will need at least 2 silver in your bank account.'}");
/*     */         
/* 201 */         buf.append("text{text=''}");
/* 202 */         buf.append("text{text='You currently only have " + change.getChangeString() + " in your account.'}");
/*     */       }
/*     */       else
/*     */       {
/* 206 */         buf.append("text{text='As this is your first time purchasing premium game time, you may purchase 30 days for 2 silver. After this first time the price will become 5 silver for 15 days, or 10 silver for 30 days.'}");
/*     */ 
/*     */         
/* 209 */         buf.append("text{text=''}");
/* 210 */         buf.append("text{text='You currently have " + change.getChangeString() + " in your account.'}");
/* 211 */         buf.append("text{text=''}");
/* 212 */         buf.append("checkbox{id='purchaseFirstTime'; selected='true'; text='Purchase 30 days of premium playing time for 2 silver.'}");
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 217 */     else if (money < 50000L) {
/*     */       
/* 219 */       buf.append("text{text='To purchase more premium game time you will need at least 5 silver in your bank account.'}");
/* 220 */       buf.append("text{text=''}");
/* 221 */       buf.append("text{text='You currently only have " + change.getChangeString() + " in your account.'}");
/*     */     }
/*     */     else {
/*     */       
/* 225 */       buf.append("text{text='You may purchase another 30 days of premium playing time for 10 silver, or 15 days of premium playing time for 5 silver.'}");
/*     */       
/* 227 */       buf.append("text{text=''}");
/* 228 */       buf.append("text{text='You currently have " + change.getChangeString() + " in your account.'}");
/* 229 */       buf.append("text{text=''}");
/* 230 */       buf.append("label{text=\"Purchase Premium Time?\"};");
/*     */       
/* 232 */       if (money >= 100000L)
/* 233 */         buf.append("radio{group='purchase';id='30day';selected='false';text='30 days for 10 silver'};"); 
/* 234 */       buf.append("radio{group='purchase';id='15day';selected='false';text='15 days for 5 silver'};");
/* 235 */       buf.append("radio{group='purchase';id='none';selected='true';text='Nothing'};");
/*     */     } 
/*     */ 
/*     */     
/* 239 */     buf.append(createAnswerButton2());
/* 240 */     getResponder().getCommunicator().sendBml(400, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PlayerPaymentQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
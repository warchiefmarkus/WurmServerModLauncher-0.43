/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.Citizen;
/*     */ import com.wurmonline.server.villages.GuardPlan;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageStatus;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.text.NumberFormat;
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
/*     */ public final class VillageUpkeep
/*     */   extends Question
/*     */   implements VillageStatus, TimeConstants, MonetaryConstants
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(VillageUpkeep.class.getName());
/*  49 */   private static final NumberFormat nf = NumberFormat.getInstance();
/*     */ 
/*     */   
/*     */   public VillageUpkeep(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  53 */     super(aResponder, aTitle, aQuestion, 120, aTarget);
/*  54 */     nf.setMaximumFractionDigits(6);
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
/*  65 */     setAnswer(answers);
/*  66 */     QuestionParser.parseVillageUpkeepQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*     */     try {
/*     */       Village village;
/*  76 */       if (this.target == -10L) {
/*     */         
/*  78 */         village = getResponder().getCitizenVillage();
/*  79 */         if (village == null) {
/*  80 */           throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*     */         }
/*     */       } else {
/*     */         
/*  84 */         Item deed = Items.getItem(this.target);
/*  85 */         int villageId = deed.getData2();
/*  86 */         village = Villages.getVillage(villageId);
/*     */       } 
/*     */       
/*  89 */       StringBuilder buf = new StringBuilder();
/*  90 */       buf.append(getBmlHeader());
/*  91 */       buf.append("header{text=\"" + village.getName() + "\"}");
/*     */       
/*  93 */       GuardPlan plan = village.plan;
/*  94 */       if (village.isPermanent) {
/*  95 */         buf.append("text{text='This village is permanent, and should never run out of money or be drained.'}");
/*     */       }
/*  97 */       else if (!Servers.localServer.isUpkeep()) {
/*  98 */         buf.append("text{text='There are no upkeep costs for settlements here.'}");
/*  99 */       } else if (plan != null) {
/*     */ 
/*     */         
/* 102 */         if (village.isCitizen(getResponder()) || getResponder().getPower() >= 2) {
/*     */           
/* 104 */           Change c = Economy.getEconomy().getChangeFor(plan.moneyLeft);
/* 105 */           buf.append("text{text='The settlement has " + c.getChangeString() + " left in its coffers.'}");
/* 106 */           Change upkeep = Economy.getEconomy().getChangeFor(plan.getMonthlyCost());
/* 107 */           buf.append("text{text='Upkeep per month is " + upkeep.getChangeString() + ".'}");
/* 108 */           float left = (float)plan.moneyLeft / (float)plan.getMonthlyCost();
/* 109 */           buf.append("text{text=\"This means that the upkeep should last for about " + (int)(left * 28.0F) + " days.\"}");
/*     */ 
/*     */           
/* 112 */           if (Servers.localServer.PVPSERVER) {
/*     */             
/* 114 */             buf.append("text{text=\"A drain would cost " + 
/* 115 */                 Economy.getEconomy().getChangeFor(plan.getMoneyDrained()).getChangeString() + ".\"};");
/* 116 */             if (plan.moneyLeft < 30000L) {
/* 117 */               buf.append("text{type='bold';text='Since minimum drain is 75 copper it may be drained to disband in less than 5 days.'}");
/*     */             }
/*     */           } 
/*     */           
/* 121 */           if (village.isMayor(getResponder()) && Servers.localServer.isFreeDeeds() && Servers.localServer.isUpkeep() && village
/* 122 */             .getCreationDate() < System.currentTimeMillis() + 2419200000L)
/*     */           {
/* 124 */             buf.append("text{text=\"\"}");
/* 125 */             buf.append("text{type='bold';text='Free deeding is enabled and your settlement is less than 30 days old. You will not receive a refund if you choose to disband before your village is 30 days old.'}");
/*     */           }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         
/* 131 */         buf.append("text{text=\"No plan found!\"}");
/* 132 */       }  buf.append("text{text=\"\"}");
/*     */       
/* 134 */       long money = getResponder().getMoney();
/*     */       
/* 136 */       if (money > 0L && (!village.isPermanent || getResponder().getPower() >= 2) && Servers.localServer
/* 137 */         .isUpkeep()) {
/*     */         
/* 139 */         buf.append("text{text=\"If you wish to contribute to the upkeep costs of this settlement, fill in the amount below:\"}");
/* 140 */         Change change = Economy.getEconomy().getChangeFor(money);
/* 141 */         buf.append("text{text=\"You may pay up to " + change.getChangeString() + ".\"}");
/* 142 */         buf.append("text{text=\"The money will be added to the settlement upkeep fund.\"}");
/* 143 */         buf.append("text{type=\"italic\";text=\"If the settlement has more than one month worth of upkeep, there will be no decay on houses, fences, and bulk and food storage bins will not be subject to a 5% loss every 30 days. If there is less than a week, decay will be very fast and bulk and food storage bins will lose 5% of their contents every 30 days.\"};text{text=\"\"}");
/*     */ 
/*     */ 
/*     */         
/* 147 */         long gold = change.getGoldCoins();
/* 148 */         long silver = change.getSilverCoins();
/* 149 */         long copper = change.getCopperCoins();
/* 150 */         long iron = change.getIronCoins();
/* 151 */         if (gold > 0L) {
/* 152 */           buf.append("harray{input{maxchars=\"10\";id=\"gold\";text=\"0\"};label{text=\"(" + gold + ") Gold coins\"}}");
/*     */         }
/* 154 */         if (silver > 0L || gold > 0L) {
/* 155 */           buf.append("harray{input{maxchars=\"10\";id=\"silver\";text=\"0\"};label{text=\"(" + silver + ") Silver coins\"}}");
/*     */         }
/* 157 */         if (copper > 0L || silver > 0L || gold > 0L) {
/* 158 */           buf.append("harray{input{maxchars=\"10\";id=\"copper\";text=\"0\"};label{text=\"(" + copper + ") Copper coins\"}}");
/*     */         }
/* 160 */         if (iron > 0L || copper > 0L || silver > 0L || gold > 0L) {
/* 161 */           buf.append("harray{input{maxchars=\"10\";id=\"iron\";text=\"0\"};label{text=\"(" + iron + ") Iron coins\"}}");
/*     */         }
/*     */       }
/* 164 */       else if (Servers.localServer.isUpkeep() && money == 0L) {
/* 165 */         buf.append("text{text=\"You may contribute to the upkeep costs of this settlement if you have money in the bank.\"}");
/* 166 */       }  buf.append("text{text=\"\"}");
/*     */       
/* 168 */       Citizen mayor = village.getMayor();
/* 169 */       if (mayor != null) {
/* 170 */         buf.append("text{type=\"italic\";text=\"" + mayor.getName() + ", " + mayor.getRole().getName() + ", " + village
/* 171 */             .getName() + "\"};text{text=\"\"}");
/*     */       } else {
/* 173 */         buf.append("text{type=\"italic\";text=\"The Citizens, " + village.getName() + "\"};text{text=\"\"}");
/* 174 */       }  buf.append(createAnswerButton2());
/* 175 */       getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 177 */     catch (NoSuchItemException nsi) {
/*     */       
/* 179 */       logger.log(Level.WARNING, getResponder().getName() + " tried to get info for null token with id " + this.target, (Throwable)nsi);
/* 180 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for that request. Please contact administration.");
/*     */     
/*     */     }
/* 183 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 185 */       logger.log(Level.WARNING, getResponder().getName() + " tried to get info for null settlement for token with id " + this.target);
/*     */       
/* 187 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for that request. Please contact administration.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageUpkeep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
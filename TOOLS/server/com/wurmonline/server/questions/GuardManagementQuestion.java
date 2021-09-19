/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.villages.GuardPlan;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ public final class GuardManagementQuestion
/*     */   extends Question
/*     */   implements TimeConstants, MonetaryConstants
/*     */ {
/*     */   public GuardManagementQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  40 */     super(aResponder, aTitle, aQuestion, 9, aTarget);
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
/*  51 */     setAnswer(answers);
/*  52 */     QuestionParser.parseGuardRentalQuestion(this);
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
/*  63 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  65 */     if (getResponder().getCitizenVillage() != null) {
/*     */       
/*  67 */       if ((getResponder().getCitizenVillage()).plan != null) {
/*     */         
/*  69 */         GuardPlan plan = (getResponder().getCitizenVillage()).plan;
/*  70 */         if (getResponder().getCitizenVillage().isCitizen(getResponder())) {
/*     */           
/*  72 */           buf.append("text{text=\"The size of " + getResponder().getCitizenVillage().getName() + " is " + 
/*  73 */               getResponder().getCitizenVillage().getDiameterX() + " by " + 
/*  74 */               getResponder().getCitizenVillage().getDiameterY() + ".\"}");
/*  75 */           buf.append("text{text=\"The perimeter is " + (5 + 
/*  76 */               getResponder().getCitizenVillage().getPerimeterSize()) + " and it has " + plan
/*  77 */               .getNumHiredGuards() + " guards hired.\"}");
/*     */         } 
/*  79 */         buf.append("text{text=\"\"}");
/*  80 */         if ((getResponder().getCitizenVillage()).isPermanent) {
/*  81 */           buf.append("text{text='This village is permanent, and should never run out of money or be drained.'}");
/*     */         } else {
/*     */           
/*  84 */           Change c = Economy.getEconomy().getChangeFor(plan.moneyLeft);
/*  85 */           buf.append("text{text='The settlement has " + c.getChangeString() + " left in its coffers.'}");
/*  86 */           Change upkeep = Economy.getEconomy().getChangeFor(plan.getMonthlyCost());
/*  87 */           buf.append("text{text='Upkeep per month is " + upkeep.getChangeString() + ".'}");
/*  88 */           float left = (float)plan.moneyLeft / (float)plan.getMonthlyCost();
/*  89 */           buf.append("text{text=\"This means that the upkeep should last for about " + (left * 28.0F) + " days.\"}");
/*     */ 
/*     */           
/*  92 */           if (Servers.localServer.PVPSERVER || Servers.localServer.id == 3) {
/*     */             
/*  94 */             buf.append("text{text=\"A drain would cost " + 
/*  95 */                 Economy.getEconomy().getChangeFor(plan.getMoneyDrained()).getChangeString() + ".\"};");
/*  96 */             if (plan.moneyLeft < 30000L) {
/*  97 */               buf.append("text{type='bold';text='Since minimum drain is 75 copper it may be drained to disband in less than 5 days.'}");
/*     */             }
/*     */           } 
/* 100 */           buf.append("text{text=\"\"}");
/*     */         } 
/* 102 */         if (Servers.localServer.isChallengeOrEpicServer()) {
/*     */           
/* 104 */           buf.append("text{text=\"The only guard type is heavy guards. The running upkeep cost increases the more guards you have in a sort of ladder system. The first guards are cheaper than the last.\"};");
/*     */ 
/*     */           
/* 107 */           buf.append("text{text=\"Make sure to review the cost for upkeep once you are done.\"};");
/*     */         }
/*     */         else {
/*     */           
/* 111 */           buf.append("text{text=\"The only guard type is heavy guards. The cost for hiring them is " + Villages.GUARD_COST_STRING + " and running upkeep is " + Villages.GUARD_UPKEEP_STRING + " per month.\"};");
/*     */ 
/*     */           
/* 114 */           buf.append("text{text=\"\"};");
/*     */         } 
/* 116 */         buf.append("text{text=\"The cost for hiring the guards is a one-time summoning fee that is not returned in case you decide to lower the amount of guards.\"};");
/*     */         
/* 118 */         buf.append("text{text=\"\"}");
/* 119 */         if (Servers.localServer.PVPSERVER) {
/*     */           
/* 121 */           buf.append("label{text='Note that you will need at least 1 guard to enforce the role rules on deed!'}");
/* 122 */           buf.append("text{text=\"\"}");
/*     */         } 
/* 124 */         buf.append("text{text=\"How many guards do you wish to have? You currently have " + plan.getNumHiredGuards() + " and may hire up to " + 
/* 125 */             GuardPlan.getMaxGuards(getResponder().getCitizenVillage()) + ".\"};input{text=\"" + plan
/* 126 */             .getNumHiredGuards() + "\";id=\"hired\"}; ");
/* 127 */         buf.append("text{text=\"\"};");
/*     */       } 
/*     */       
/* 130 */       buf.append(createAnswerButton2());
/* 131 */       getResponder().getCommunicator().sendBml(400, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GuardManagementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
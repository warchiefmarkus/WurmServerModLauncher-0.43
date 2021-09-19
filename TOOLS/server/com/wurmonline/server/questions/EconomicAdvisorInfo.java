/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.Shop;
/*     */ import com.wurmonline.server.kingdom.Appointment;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
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
/*     */ public final class EconomicAdvisorInfo
/*     */   extends Question
/*     */ {
/*     */   public EconomicAdvisorInfo(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  44 */     super(aResponder, aTitle, aQuestion, 74, aTarget);
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
/*     */   public void answer(Properties aAnswers) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  65 */     StringBuilder sb = new StringBuilder();
/*  66 */     sb.append(getBmlHeader());
/*  67 */     Appointment a = Appointment.getAppointment(1505, getResponder().getKingdomId());
/*     */ 
/*     */     
/*  70 */     String nam = "Economic advisor";
/*  71 */     if (a != null)
/*  72 */       nam = a.getNameForGender(getResponder().getSex()); 
/*  73 */     sb.append("text{type='italic';text=\"" + a + " confidential information.\"}");
/*  74 */     sb.append("text{text=\"  Economic statement for " + Kingdoms.getNameFor(getResponder().getKingdomId()) + ".\"}");
/*  75 */     long sum = 0L;
/*  76 */     StringBuilder sb2 = new StringBuilder();
/*  77 */     Creature[] crets = Creatures.getInstance().getCreatures();
/*  78 */     for (int x = 0; x < crets.length; x++) {
/*     */       
/*  80 */       if (crets[x].isTrader() && crets[x].getKingdomId() == getResponder().getKingdomId())
/*     */       {
/*  82 */         if (crets[x].isNpcTrader()) {
/*     */           
/*  84 */           Shop shop = Economy.getEconomy().getShop(crets[x]);
/*  85 */           if (!shop.isPersonal()) {
/*     */             
/*  87 */             if (shop.getMoney() >= 0L) {
/*  88 */               sb2.append("text{text=\"  Trader - " + (new Change(shop.getMoney())).getChangeShortString() + ". Ratio=" + shop
/*  89 */                   .getSellRatio() + "\"}");
/*     */             } else {
/*  91 */               sb2.append("text{text=\"  Trader - " + shop.getMoney() + " irons. Ratio=" + shop.getSellRatio() + "\"}");
/*     */             } 
/*  93 */             sum += shop.getMoney();
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*  98 */     Shop kingshop = Economy.getEconomy().getKingsShop();
/*  99 */     sb.append("text{text=\"  Kings coffers: " + (new Change(kingshop.getMoney())).getChangeString() + " (" + kingshop
/* 100 */         .getMoney() + " irons).\"}");
/* 101 */     sb.append("text{text=\"  Total money at traders: " + (new Change(sum)).getChangeString() + ".\"}");
/* 102 */     sb.append("text{text=\"\"}");
/* 103 */     sb.append("text{type='bold';text=\"Trader breakdown:\"}");
/* 104 */     sb.append(sb2.toString());
/* 105 */     sb.append(createAnswerButton2());
/* 106 */     getResponder().getCommunicator().sendBml(300, 300, true, true, sb.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\EconomicAdvisorInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
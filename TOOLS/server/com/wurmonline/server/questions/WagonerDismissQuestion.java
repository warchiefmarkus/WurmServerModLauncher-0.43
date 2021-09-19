/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Delivery;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WagonerDismissQuestion
/*     */   extends Question
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(WagonerDismissQuestion.class.getName());
/*     */   
/*     */   private final Wagoner wagoner;
/*     */ 
/*     */   
/*     */   public WagonerDismissQuestion(Creature aResponder, Wagoner wagoner) {
/*  49 */     super(aResponder, "Dismiss " + wagoner.getName() + " question", "Dismiss " + wagoner.getName() + " question", 149, wagoner.getWurmId());
/*  50 */     this.wagoner = wagoner;
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
/*  61 */     setAnswer(answers);
/*  62 */     if (this.type == 0) {
/*     */       
/*  64 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  67 */     if (this.type == 149) {
/*     */       
/*  69 */       boolean close = getBooleanProp("close");
/*  70 */       if (close) {
/*     */         return;
/*     */       }
/*     */       
/*  74 */       boolean dismiss = getBooleanProp("dismiss");
/*  75 */       if (dismiss)
/*     */       {
/*  77 */         if (this.wagoner.getVillageId() == -1) {
/*  78 */           getResponder().getCommunicator().sendNormalServerMessage("Wagoner is already dismissing!");
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*  83 */             Village village = Villages.getVillage(this.wagoner.getVillageId());
/*  84 */             village.deleteWagoner(this.wagoner.getCreature());
/*     */           }
/*  86 */           catch (NoSuchVillageException e) {
/*     */ 
/*     */             
/*  89 */             getResponder().getCommunicator().sendNormalServerMessage("Cannot find wagoner's village!");
/*  90 */             logger.log(Level.WARNING, "Cannot find wagoner's village!", (Throwable)e);
/*     */           } 
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
/* 105 */     StringBuilder buf = new StringBuilder();
/* 106 */     buf.append(getBmlHeader());
/*     */     
/*     */     try {
/* 109 */       Village village = Villages.getVillage(this.wagoner.getVillageId());
/* 110 */       int waiting = Delivery.countWaitingForAccept(this.wagoner.getWurmId());
/* 111 */       int queued = Delivery.getQueueLength(this.wagoner.getWurmId());
/* 112 */       boolean onDelivery = (this.wagoner.getDeliveryId() != -10L);
/* 113 */       boolean atCamp = this.wagoner.isIdle();
/* 114 */       buf.append("label{text=\"\"}");
/* 115 */       buf.append("label{text=\"You are about to dismiss " + this.wagoner.getName() + ".\"}");
/* 116 */       buf.append("label{text=\"This will do the following:\"}");
/* 117 */       buf.append("label{text=\"1. They will be kicked out of the village '" + village.getName() + "'.\"}");
/* 118 */       buf.append("label{text=\"\"}");
/* 119 */       if (waiting == 0) {
/*     */         
/* 121 */         buf.append("label{text=\"2. Luckily " + this.wagoner.getName() + " does not have any deliveries waiting to be accepted.\"}");
/* 122 */         buf.append("label{text=\"   a. But if there were any, they would have been auto-rejected.\"}");
/*     */       }
/*     */       else {
/*     */         
/* 126 */         buf.append("label{text=\"2. The " + waiting + ((waiting == 1) ? " delivery" : " deliveries") + " waiting to be accepted will be auto-rejected.\"}");
/* 127 */         buf.append("label{text=\"   a. Note: this will NOT unseal the associated containers.\"}");
/*     */       } 
/* 129 */       buf.append("label{text=\"\"}");
/* 130 */       if (queued == 0) {
/*     */         
/* 132 */         buf.append("label{text=\"3. Luckily " + this.wagoner.getName() + " does not have any deliveries queued.\"}");
/* 133 */         buf.append("label{text=\"   a. But if they did have some then the sender of the queued deliveries would have to pick a different wagoner.\"}");
/*     */       }
/*     */       else {
/*     */         
/* 137 */         buf.append("label{text=\"3. The " + queued + ((queued == 1) ? " delivery" : " deliveries") + " in " + this.wagoner.getName() + "'s queue will be unassigned.\"}");
/* 138 */         buf.append("label{text=\"   a. The sender of these deliveries will have to pick another wagoner for each of the deliveries.\"}");
/*     */       } 
/* 140 */       buf.append("label{text=\"\"}");
/* 141 */       if (onDelivery) {
/*     */         
/* 143 */         buf.append("label{text=\"4. As " + this.wagoner.getName() + " is performing a delivery then: \"}");
/* 144 */         buf.append("label{text=\"   a. They will contine on that delivery.\"}");
/* 145 */         buf.append("label{text=\"   b. When the delivery is complete, they will drive back to their home waystone.\"}");
/*     */       }
/*     */       else {
/*     */         
/* 149 */         buf.append("label{text=\"4. If " + this.wagoner.getName() + " was performing a delivery (which they are not) then: \"}");
/* 150 */         buf.append("label{text=\"   a. They would have continued on that delivery.\"}");
/* 151 */         buf.append("label{text=\"   b. And when the delivery was complete, they would have driven back to their home waystone.\"}");
/*     */       } 
/* 153 */       buf.append("label{text=\"\"}");
/* 154 */       buf.append("label{text=\"5. " + (atCamp ? "As" : "When") + " the wagoner is back at camp then\"}");
/* 155 */       buf.append("label{text=\"   a. They will pack up their camp and vanish.\"}");
/* 156 */       buf.append("label{text=\"   b. The contract can then be used elsewhere.\"}");
/* 157 */       buf.append("label{text=\"\"}");
/* 158 */       buf.append("harray{button{text=\"Dismiss\";id=\"dismiss\";hover=\"This will dismiss " + this.wagoner
/*     */           
/* 160 */           .getName() + "\";confirm=\"You are about to dismiss " + this.wagoner
/* 161 */           .getName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"};button{text=\"Close\";id=\"close\";}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 167 */     catch (NoSuchVillageException e) {
/*     */ 
/*     */       
/* 170 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 171 */       buf.append("harray{button{text=\"Close\";id=\"close\";}");
/*     */     } 
/*     */ 
/*     */     
/* 175 */     buf.append("text=\"\"}");
/* 176 */     buf.append("}};null;null;}");
/*     */     
/* 178 */     getResponder().getCommunicator().sendBml(560, 460, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WagonerDismissQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
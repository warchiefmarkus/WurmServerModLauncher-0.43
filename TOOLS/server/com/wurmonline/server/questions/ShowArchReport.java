/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.DeadVillage;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ public class ShowArchReport
/*     */   extends Question
/*     */ {
/*  13 */   private Item reportItem = null;
/*     */ 
/*     */   
/*     */   public ShowArchReport(Creature aResponder, Item report) {
/*  17 */     super(aResponder, report.getName(), report.getName(), 151, -10L);
/*  18 */     this.reportItem = report;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  30 */     if (this.reportItem == null) {
/*     */       return;
/*     */     }
/*  33 */     StringBuilder buf = new StringBuilder();
/*  34 */     buf.append(getBmlHeaderNoQuestion());
/*     */     
/*  36 */     DeadVillage dv = Villages.getDeadVillage(this.reportItem.getData());
/*  37 */     if (dv != null) {
/*     */       
/*  39 */       buf.append("label{type=\"bold\";text=\"" + dv.getDeedName() + " Archaeological Report\";}");
/*  40 */       buf.append("text{text='';}");
/*  41 */       if (this.reportItem.getAuxBit(0))
/*     */       {
/*  43 */         if (this.reportItem.getAuxBit(1)) {
/*     */           
/*  45 */           if (this.reportItem.getAuxBit(2)) {
/*     */             
/*  47 */             if (this.reportItem.getAuxBit(3))
/*     */             {
/*  49 */               buf.append("text{text='There is enough location information written in this report that you believe that you could use it to track down the exact location of this village.';}");
/*     */               
/*  51 */               buf.append("text{text='Maybe following the report to the location will have some effect.';}");
/*     */             }
/*     */             else
/*     */             {
/*  55 */               buf.append("text{text='There is almost enough location information written in this report to be able to use it to track down this village.';}");
/*     */               
/*  57 */               buf.append("text{text='Perhaps investigating around the village area some more will provide more location clues.';}");
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  62 */             buf.append("text{text='Written in this report is a decent number of location markers and reference to nearby landmarks, but you doubt it is enough information to be able to find the village.';}");
/*     */             
/*  64 */             buf.append("text{text='Perhaps investigating around the village area some more will provide more location clues.';}");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/*  69 */           buf.append("text{text='There are a few basic hints to where this village once may have resided, but not enough for anything meaningful.';}");
/*     */           
/*  71 */           buf.append("text{text='Perhaps investigating around the village area some more will provide more location clues.';}");
/*     */         } 
/*     */       }
/*  74 */       buf.append("label{text='';}");
/*     */       
/*  76 */       buf.append("label{type='bold';text='Last Mayor:';};");
/*  77 */       if (this.reportItem.getAuxBit(4)) {
/*  78 */         buf.append("text{text='" + dv.getMayorName() + "';}");
/*     */       } else {
/*  80 */         buf.append("text{text='[ Not Recorded ]';}");
/*     */       } 
/*  82 */       buf.append("label{type='bold';text='Village Founder:';};");
/*  83 */       if (this.reportItem.getAuxBit(5)) {
/*  84 */         buf.append("text{text='" + dv.getFounderName() + "';}");
/*     */       } else {
/*  86 */         buf.append("text{text='[ Not Recorded ]';}");
/*     */       } 
/*  88 */       buf.append("label{type='bold';text='Abandoned for:';};");
/*  89 */       if (this.reportItem.getAuxBit(6)) {
/*  90 */         buf.append("text{text='" + DeadVillage.getTimeString(dv.getTimeSinceDisband(), (dv.getTimeSinceDisband() > 12.0F)) + "';}");
/*     */       } else {
/*  92 */         buf.append("text{text='[ Not Recorded ]';}");
/*     */       } 
/*  94 */       buf.append("label{type='bold';text='Inhabited for:';};");
/*  95 */       if (this.reportItem.getAuxBit(7)) {
/*  96 */         buf.append("text{text='" + DeadVillage.getTimeString(dv.getTotalAge(), false) + "';}");
/*     */       } else {
/*  98 */         buf.append("text{text='[ Not Recorded ]';}");
/*     */       } 
/*     */     } else {
/*     */       
/* 102 */       buf.append("text{type='bold';text='The report seems to be written in some foreign language. Perhaps it is meant for a village from some distant lands.'}");
/*     */     } 
/*     */ 
/*     */     
/* 106 */     buf.append("}};null;null;}");
/* 107 */     getResponder().getCommunicator().sendBml(500, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ShowArchReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
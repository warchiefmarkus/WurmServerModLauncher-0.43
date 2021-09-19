/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.WurmHarvestables;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
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
/*     */ public final class GMSelectHarvestable
/*     */   extends Question
/*     */ {
/*  35 */   private WurmHarvestables.Harvestable[] harvestables = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private Item paper;
/*     */ 
/*     */ 
/*     */   
/*     */   public GMSelectHarvestable(Creature aResponder, Item apaper) {
/*  44 */     super(aResponder, "Select Harvestabke", "Select Harvestabke", 140, -10L);
/*  45 */     this.paper = apaper;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  51 */     setAnswer(answers);
/*  52 */     String sel = answers.getProperty("harvestable");
/*  53 */     int selId = Integer.parseInt(sel);
/*  54 */     WurmHarvestables.Harvestable harvestable = this.harvestables[selId];
/*     */     
/*  56 */     this.paper.setAuxData((byte)(harvestable.getHarvestableId() + 8));
/*     */     
/*  58 */     this.paper.setData1(99);
/*     */     
/*  60 */     this.paper.setInscription(harvestable.getName() + " report", getResponder().getName(), 0);
/*  61 */     this.paper.setName(harvestable.getName() + " report", true);
/*  62 */     getResponder().getCommunicator().sendNormalServerMessage("You carefully finish writing the " + harvestable
/*  63 */         .getName() + " report and sign it.");
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
/*  74 */     this.harvestables = WurmHarvestables.getHarvestables();
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
/*  90 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*  91 */     buf.append("harray{label{text=\"Harvestable\"};");
/*  92 */     buf.append("dropdown{id=\"harvestable\";default=\"0\";options=\"");
/*  93 */     for (int i = 0; i < this.harvestables.length; i++) {
/*     */       
/*  95 */       if (i > 0)
/*  96 */         buf.append(","); 
/*  97 */       WurmHarvestables.Harvestable harvestable = this.harvestables[i];
/*  98 */       buf.append(harvestable.getName().replace(",", "") + " (" + harvestable.getHarvestableId() + ")");
/*     */     } 
/* 100 */     buf.append("\"}}");
/* 101 */     buf.append("label{text=\"\"}");
/* 102 */     buf.append(createAnswerButton2());
/* 103 */     getResponder().getCommunicator().sendBml(300, 120, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GMSelectHarvestable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
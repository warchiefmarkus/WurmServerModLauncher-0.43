/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.WurmColor;
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
/*     */ public final class TextInputQuestion
/*     */   extends Question
/*     */ {
/*     */   private final int maxSize;
/*  30 */   private String oldtext = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sign = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NOCHANGE = "No change";
/*     */ 
/*     */   
/*     */   private Item liquid;
/*     */ 
/*     */   
/*     */   private final Item[] items;
/*     */ 
/*     */ 
/*     */   
/*     */   public TextInputQuestion(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget, int aMaxsize, boolean isSign) {
/*  49 */     super(aResponder, aTitle, aQuestion, aType, aTarget);
/*  50 */     this.maxSize = aMaxsize;
/*  51 */     this.sign = isSign;
/*  52 */     this.items = new Item[0];
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
/*     */ 
/*     */   
/*     */   public TextInputQuestion(Creature aResponder, String aTitle, String aQuestion, Item[] aTargets) {
/*  66 */     super(aResponder, aTitle, aQuestion, 1, -10L);
/*  67 */     this.maxSize = 20;
/*  68 */     this.sign = false;
/*  69 */     this.items = aTargets;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLiquid(Item aLiquid) {
/*  74 */     this.liquid = aLiquid;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  80 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*  81 */     if (this.target == -10L) {
/*  82 */       buf.append("label{text=\"Renaming multiple items at the same time.\"}");
/*     */     }
/*  84 */     buf.append("input{id=\"answer\";maxchars=\"" + this.maxSize + "\";");
/*  85 */     if (this.type == 2 && this.liquid != null) {
/*     */       
/*  87 */       buf.append("maxlines=\"-1\";bgcolor=\"200,200,200\";");
/*     */ 
/*     */       
/*  90 */       if (this.liquid.getTemplateId() == 753) {
/*  91 */         buf.append("color=\"0,0,0\";");
/*     */       } else {
/*  93 */         buf.append("color=\"" + WurmColor.getColorRed(this.liquid.color) + "," + 
/*  94 */             WurmColor.getColorGreen(this.liquid.color) + "," + 
/*  95 */             WurmColor.getColorBlue(this.liquid.color) + "\";");
/*     */       } 
/*  97 */     }  buf.append("text=\"" + this.oldtext + "\"}");
/*  98 */     if (this.sign) {
/*     */ 
/*     */       
/* 101 */       buf.append("harray{label{text='Sign image'}dropdown{id='data1';options=\"");
/* 102 */       buf.append("No change,");
/* 103 */       buf.append("Bowl,Beer,Bear and beer,Crops,Construction,Sleep,Wine,Coins,Horse,Hunt,Sword and bowl,Lumber,Swordsmith,Anvil,Helmet,Baker,Shipwright,Anchor,Pirate,Mystery,Tailor,Alchemy,");
/*     */       
/* 105 */       buf.append("No change");
/* 106 */       buf.append("\"}}");
/*     */     } 
/* 108 */     buf.append(createAnswerButton2());
/*     */     
/* 110 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 116 */     setAnswer(answers);
/* 117 */     QuestionParser.parseTextInputQuestion(this, this.liquid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getOldtext() {
/* 127 */     return this.oldtext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOldtext(String aOldtext) {
/* 138 */     this.oldtext = aOldtext;
/*     */   }
/*     */ 
/*     */   
/*     */   Item[] getItems() {
/* 143 */     return this.items;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TextInputQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
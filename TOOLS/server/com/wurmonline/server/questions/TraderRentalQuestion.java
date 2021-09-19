/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.villages.Village;
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
/*     */ public final class TraderRentalQuestion
/*     */   extends Question
/*     */ {
/*     */   public TraderRentalQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  34 */     super(aResponder, aTitle, aQuestion, 22, aTarget);
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
/*  45 */     setAnswer(answers);
/*  46 */     QuestionParser.parseTraderRentalQuestion(this);
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
/*     */   public void sendQuestion() {
/* 100 */     boolean citiz = false;
/* 101 */     Village v = getResponder().getCurrentTile().getVillage();
/* 102 */     if (v != null)
/*     */     {
/* 104 */       citiz = true;
/*     */     }
/* 106 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 107 */     buf.append("text{text=''}");
/* 108 */     buf.append("text{type=\"bold\";text='Traders:'}");
/* 109 */     buf.append("text{text='There are two types of traders:'}");
/* 110 */     buf.append("text{text='1. A normal trader buys and sells anything. He owns his own shop.'}");
/* 111 */     buf.append("text{text='2. A personal merchant tries to sell anything you give him to other players. Then you can come back and collect the money.'}");
/* 112 */     buf.append("text{text='Traders will only appear in finished structures where no other creatures but you stand.'}");
/* 113 */     buf.append("text{text='If you are citizen of a village or homestead, the trader will donate part of its income from foreign traders to the settlement funds.'}");
/* 114 */     buf.append("text{text=''}");
/* 115 */     buf.append("text{type='bold';text='Hire normal trader:'}");
/* 116 */     buf.append("text{text='By using this contract a normal trader will appear.'}");
/* 117 */     buf.append("text{text='You do not own a trader and can not count on receiving any money back from using the contract.'}");
/* 118 */     buf.append("text{text='The trader will appear where you stand, if the tile is inside a structure and contains no other creature.'}");
/* 119 */     buf.append("text{text='This contract will disappear once the trader arrives.'}");
/* 120 */     buf.append("text{text=\"The trader will stop receiving money if it doesn't sell for approximately 10% of what it purchases.\"}");
/* 121 */     buf.append("text{text=\"The trader will only set up shop if there are no other normal traders in the area.\"}");
/* 122 */     buf.append("text{text='You will not be able to set local prices, or the price modifier for a normal trader.'}");
/* 123 */     buf.append("text{text=''}");
/* 124 */     buf.append("text{type=\"bold\";text=\"Note that if the trader is citizen of a settlement when it disbands, he or she will disappear regardless of whether he is on deed or not!\"}");
/* 125 */     if (citiz && v != null) {
/* 126 */       buf.append("text{type='italic';color='200,40,40';text=\"The trader will become part of " + v.getName() + " and pay taxes there.\"}");
/*     */     } else {
/* 128 */       buf.append("text{type='italic';color='200,40,40';text='The trader will not become part of any village, so no tax revenue will be gained.'}");
/* 129 */     }  buf.append("text{text=''}");
/* 130 */     buf.append("label{text='Gender: '}");
/* 131 */     if (getResponder().getSex() == 1) {
/*     */       
/* 133 */       buf.append("radio{ group='gender'; id='male';text='Male'}");
/* 134 */       buf.append("radio{ group='gender'; id='female';text='Female';selected='true'}");
/*     */     }
/*     */     else {
/*     */       
/* 138 */       buf.append("radio{ group='gender'; id='male';text='Male';selected='true'}");
/* 139 */       buf.append("radio{ group='gender'; id='female';text='Female'}");
/*     */     } 
/* 141 */     if (citiz) {
/*     */       
/* 143 */       buf.append("text{text='You must now decide upon the fraction of the profit the trader makes that will go directly to the village upkeep fund.'}");
/*     */       
/* 145 */       buf.append("text{text='Note that for now you cannot change this number later. Max is 40 percent.'}");
/* 146 */       buf.append("harray{label{text='Tax, in percent: '};input{maxchars='2';id='tax';text='20'}}");
/*     */     } 
/* 148 */     buf.append("text{text=''}");
/* 149 */     buf.append("harray{label{text='The trader shall be called '};input{maxchars='20';id='ntradername'};label{text='!'}}");
/* 150 */     buf.append("text{text=''}");
/* 151 */     buf.append(createAnswerButton2());
/* 152 */     getResponder().getCommunicator().sendBml(500, 660, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TraderRentalQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
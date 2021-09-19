/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.deities.Deity;
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
/*     */ public final class ConvertQuestion
/*     */   extends Question
/*     */ {
/*     */   private final Item holyItem;
/*  40 */   private float skillcounter = 0.0F;
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
/*     */   public ConvertQuestion(Creature aResponder, String aTitle, String aQuestion, long aAsker, Item aHolyItem) {
/*  54 */     super(aResponder, aTitle, aQuestion, 28, aAsker);
/*  55 */     this.holyItem = aHolyItem;
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
/*  66 */     setAnswer(answers);
/*  67 */     QuestionParser.parseConvertQuestion(this);
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
/*     */   public void sendQuestion() {
/*     */     try {
/*  80 */       Creature asker = Server.getInstance().getCreature(this.target);
/*  81 */       Deity deity = asker.getDeity();
/*     */       
/*  83 */       StringBuilder buf = new StringBuilder();
/*  84 */       buf.append(getBmlHeader());
/*  85 */       if (!QuestionParser.doesKingdomTemplateAcceptDeity(getResponder().getKingdomTemplateId(), deity)) {
/*     */         
/*  87 */         buf.append("text{text='" + getResponder().getKingdomName() + " would never accept a follower of " + deity.name + ".'}");
/*  88 */         buf.append("text{text=''}");
/*     */       }
/*  90 */       else if (deity != getResponder().getDeity()) {
/*     */         
/*  92 */         buf.append("text{text='" + asker.getName() + " asks if you wish to become a follower of " + deity.name + ".'}");
/*  93 */         buf.append("text{text=''}");
/*     */         
/*  95 */         buf.append("text{text=''}");
/*  96 */         if (getResponder().getDeity() != null)
/*  97 */           buf.append("text{type='bold';text='If you answer yes, your faith and all your abilities granted by " + 
/*  98 */               (getResponder().getDeity()).name + " will be lost!'}"); 
/*  99 */         if (!Servers.localServer.PVPSERVER) {
/* 100 */           buf.append("text{type='bold';text='Warning: Converting to a deity on Freedom then travelling to a Chaos kingdom that does notalign with your deity you will lose all faith and abilities granted, and you will stop following that deity. Libila does not align with WL kingdoms and Fo/Vynora/Magranon do not align with BL kingdoms.'}");
/*     */         }
/*     */         
/* 103 */         buf.append("text{type='italic';text='Do you want to become a follower of " + deity.name + "?'}");
/* 104 */         buf.append("text{text=''}");
/*     */         
/* 106 */         buf.append("radio{ group='conv'; id='true';text='Yes'}");
/* 107 */         buf.append("radio{ group='conv'; id='false';text='No';selected='true'}");
/*     */       } else {
/*     */         
/* 110 */         buf.append("text{text='You are already a follower of " + deity.name + ".'}");
/* 111 */       }  buf.append(createAnswerButton2());
/* 112 */       getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 114 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */     
/* 117 */     } catch (NoSuchPlayerException noSuchPlayerException) {}
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
/*     */   public float getSkillcounter() {
/* 129 */     return this.skillcounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkillcounter(float aSkillcounter) {
/* 140 */     this.skillcounter = aSkillcounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Item getHolyItem() {
/* 150 */     return this.holyItem;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ConvertQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.io.IOException;
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
/*     */ public final class ChangeAppearanceQuestion
/*     */   extends Question
/*     */ {
/*     */   private Item mirror;
/*     */   private byte gender;
/*     */   
/*     */   public ChangeAppearanceQuestion(Creature aResponder, Item aItem) {
/*  27 */     super(aResponder, "Golden Mirror", "This mirror allows you to change your gender and alter your appearance.", 51, aResponder.getWurmId());
/*  28 */     this.mirror = aItem;
/*  29 */     this.gender = Byte.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleGenderChange() {
/*  34 */     if (getResponder().getSex() != this.gender) {
/*     */       
/*  36 */       Player player = (Player)getResponder();
/*     */       
/*     */       try {
/*  39 */         player.getSaveFile().setFace(0L);
/*     */       }
/*  41 */       catch (IOException ex) {
/*     */         
/*  43 */         player.getCommunicator().sendAlertServerMessage("Something went wrong changing your gender. You remain as you were.", (byte)3);
/*  44 */         logger.warning("Error setting face for player " + player.getName() + ": " + ex.getMessage());
/*     */         return;
/*     */       } 
/*  47 */       player.setVisible(false);
/*  48 */       getResponder().setSex(this.gender, false);
/*  49 */       if (player.getCurrentTile() != null)
/*  50 */         player.getCurrentTile().setNewFace((Creature)player); 
/*  51 */       getResponder().setModelName("Human");
/*  52 */       player.setVisible(true);
/*  53 */       getResponder().getCommunicator().sendNewFace(getResponder().getWurmId(), getResponder().getFace());
/*  54 */       getResponder().getCommunicator().sendSafeServerMessage("You feel a strange sensation as Vynora's power alters your body. You are now " + ((this.gender == 1) ? "female" : "male") + ".", (byte)2);
/*     */     }
/*     */     else {
/*     */       
/*  58 */       getResponder().getCommunicator().sendSafeServerMessage("Your gender remains the same.");
/*  59 */     }  this.mirror.setAuxData((byte)1);
/*  60 */     this.mirror.sendUpdate();
/*  61 */     getResponder().getCommunicator().sendSafeServerMessage("The mirror's glow diminishes slightly as some of the magic is used.", (byte)2);
/*  62 */     getResponder().getCommunicator().sendCustomizeFace(getResponder().getFace(), this.mirror.getWurmId());
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendConfirmation() {
/*  67 */     if (this.mirror.getAuxData() == 1)
/*     */       return; 
/*  69 */     StringBuilder buf = new StringBuilder();
/*  70 */     buf.append(getBmlHeader());
/*  71 */     buf.append("harray{text{text=''}}text{type='bold';text='Are you sure? This mirror will not allow you to make this choice again.'}harray{text{text=''}}");
/*  72 */     if (this.gender == getResponder().getSex()) {
/*  73 */       buf.append("radio{group='confirm';id='yes';text='Yes, I wish to remain " + ((this.gender == 1) ? "female" : "male") + "';}");
/*     */     } else {
/*  75 */       buf.append("radio{group='confirm';id='yes';text='Yes, I wish to become " + ((this.gender == 1) ? "female" : "male") + "';}");
/*  76 */     }  buf.append("radio{group='confirm';id='no';text='No, I do not wish to make this decision now.'}");
/*  77 */     buf.append("harray{text{text=''}}");
/*  78 */     buf.append(createAnswerButton2("Next"));
/*  79 */     getResponder().getCommunicator().sendBml(300, 250, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  85 */     if (this.mirror.getOwnerId() != getResponder().getWurmId()) {
/*     */       
/*  87 */       getResponder().getCommunicator().sendAlertServerMessage("You are no longer in possession of this mirror.", (byte)3);
/*     */       return;
/*     */     } 
/*  90 */     if (answers.getProperty("confirm", "").equals("yes")) {
/*  91 */       handleGenderChange();
/*  92 */     } else if (answers.getProperty("gender", "").equals("male") || answers.getProperty("gender", "").equals("female")) {
/*     */       
/*  94 */       ChangeAppearanceQuestion question = new ChangeAppearanceQuestion(getResponder(), this.mirror);
/*  95 */       if (answers.getProperty("gender").equals("male")) {
/*  96 */         question.gender = 0;
/*     */       } else {
/*  98 */         question.gender = 1;
/*  99 */       }  question.sendConfirmation();
/*     */     } else {
/*     */       
/* 102 */       getResponder().getCommunicator().sendSafeServerMessage("You put the mirror away, leaving your body as it was.", (byte)2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 108 */     if (this.mirror.getAuxData() == 1)
/*     */       return; 
/* 110 */     StringBuilder buf = new StringBuilder();
/* 111 */     buf.append(getBmlHeader());
/* 112 */     buf.append("harray{text{text=''}}text{text='Before you may change your appearance, you must choose to select a new gender or keep your current one.'}harray{text{text=''}}text{type='bold';text='What will your gender be?'}");
/*     */     
/* 114 */     buf.append(femaleOption());
/* 115 */     buf.append(maleOption());
/* 116 */     buf.append("harray{text{text=''}}");
/* 117 */     buf.append(createAnswerButton2("Next"));
/* 118 */     getResponder().getCommunicator().sendBml(300, 250, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private final String maleOption() {
/* 123 */     if (getResponder().getSex() == 0)
/* 124 */       return "harray{text{text=''}radio{ group='gender'; id='male';text='Male (current)';selected='true'}}"; 
/* 125 */     return "harray{text{text=''}radio{ group='gender'; id='male';text='Male'}}";
/*     */   }
/*     */ 
/*     */   
/*     */   private final String femaleOption() {
/* 130 */     if (getResponder().getSex() == 1)
/* 131 */       return "harray{text{text=''}radio{ group='gender'; id='female';text='Female (current)';selected='true'}}"; 
/* 132 */     return "harray{text{text=''}radio{ group='gender'; id='female';text='Female'}}";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ChangeAppearanceQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
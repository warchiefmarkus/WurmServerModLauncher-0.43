/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
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
/*     */ public final class SuicideQuestion
/*     */   extends Question
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(SuicideQuestion.class.getName());
/*     */ 
/*     */   
/*     */   public SuicideQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  41 */     super(aResponder, aTitle, aQuestion, 48, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  47 */     String key = "suicide";
/*  48 */     String val = answers.getProperty("suicide");
/*  49 */     if (val != null && val.equals("true")) {
/*     */       
/*  51 */       if (getResponder().isDead())
/*  52 */         getResponder().getCommunicator().sendNormalServerMessage("You are already dead."); 
/*  53 */       if (getResponder().isTeleporting()) {
/*  54 */         getResponder().getCommunicator().sendAlertServerMessage("You are too confused to kill yourself right now.");
/*  55 */       } else if (getResponder().getBattle() != null) {
/*  56 */         getResponder().getCommunicator().sendAlertServerMessage("You are too full of adrenaline from the battle to kill yourself right now.");
/*     */       }
/*     */       else {
/*     */         
/*  60 */         if ((((Player)getResponder()).getSaveFile()).realdeath > 2) {
/*     */           
/*  62 */           getResponder().getCommunicator().sendAlertServerMessage("You cannot force yourself to suicide this time.");
/*     */           return;
/*     */         } 
/*  65 */         logger.log(Level.INFO, getResponder().getName() + " SUICIDE " + getResponder().getName() + " at coords: " + 
/*  66 */             getResponder().getTileX() + ", " + getResponder().getTileY());
/*  67 */         getResponder()
/*  68 */           .getCommunicator()
/*  69 */           .sendAlertServerMessage("Using an old Kelatchka Nomad-trick you once heard of, you swallow your tongue and quickly fall down dead.");
/*     */         
/*  71 */         Server.getInstance().broadCastAction(
/*  72 */             getResponder().getName() + " falls down dead, having swallowed " + getResponder().getHisHerItsString() + " tongue.", 
/*  73 */             getResponder(), 5);
/*  74 */         ((Player)getResponder()).suiciding = true;
/*  75 */         ((Player)getResponder()).lastSuicide = System.currentTimeMillis();
/*     */         
/*  77 */         getResponder().die(false, "Suicide");
/*     */       } 
/*     */     } else {
/*     */       
/*  81 */       getResponder().getCommunicator().sendNormalServerMessage("You decide not to commit suicide for now.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  87 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  89 */     buf
/*  90 */       .append("text{text='Committing suicide may be helpful when you are stuck in a mine or fenced in. At low levels you will quickly regain the skill you lose when doing so, and more experienced players should know better than to risk becoming stuck anyways.'}");
/*  91 */     buf.append("text{text='If you are stuck in a fence, using the command /stuck may help as well.'}");
/*  92 */     buf
/*  93 */       .append("text{text='You may not commit suicide when you are already dead, teleporting, or have just been engaged in battle.'}");
/*  94 */     boolean lastDeath = false;
/*     */     
/*     */     try {
/*  97 */       Skill body = getResponder().getSkills().getSkill(102);
/*  98 */       if (getResponder().isPlayer() && !getResponder().isPaying())
/*     */       {
/* 100 */         if (body.getKnowledge() < 20.0D)
/*     */         {
/* 102 */           if (body.minimum - body.getKnowledge() > 0.05000000074505806D && body.minimum - body.getKnowledge() <= 0.06D)
/*     */           {
/* 104 */             lastDeath = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 109 */     catch (NoSuchSkillException nss) {
/*     */       
/* 111 */       getResponder().getSkills().learn(102, 1.0F);
/* 112 */       logger.log(Level.WARNING, getResponder().getName() + " learnt body strength.");
/*     */     } 
/* 114 */     if (lastDeath) {
/*     */       
/* 116 */       buf.append("text{type='italic';text='You may not suicide now. You need more strength.'}");
/*     */     }
/*     */     else {
/*     */       
/* 120 */       buf.append("text{type='italic';text='Do you wish to commit suicide?'}");
/* 121 */       buf.append("text{text=''}");
/*     */       
/* 123 */       buf.append("radio{ group='suicide'; id='true';text='Yes'}");
/* 124 */       buf.append("radio{ group='suicide'; id='false';text='No';selected='true'}");
/*     */     } 
/* 126 */     buf.append(createAnswerButton2());
/* 127 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SuicideQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
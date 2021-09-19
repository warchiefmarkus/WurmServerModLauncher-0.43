/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Mailer;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.NotOwnedException;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.webinterface.WebInterfaceImpl;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import java.util.Random;
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
/*     */ public class WishQuestion
/*     */   extends Question
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(WishQuestion.class.getName());
/*     */   
/*     */   private final long coinId;
/*     */   private static final String RESPONSE1 = ". Will the gods listen?";
/*     */   private static final String RESPONSE2 = ". Do you consider yourself lucky?";
/*     */   private static final String RESPONSE3 = ". Is this your turn?";
/*     */   private static final String RESPONSE4 = ". You get the feeling that someone listens.";
/*     */   private static final String RESPONSE5 = ". Good luck!";
/*     */   private static final String RESPONSE6 = ". Will it come true?";
/*  52 */   private static final Random rand = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String INSERT_WISH = "INSERT INTO WISHES (PLAYER,WISH,COIN,TOFULFILL) VALUES(?,?,?,?)";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WishQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, long coin) {
/*  65 */     super(aResponder, aTitle, aQuestion, 77, aTarget);
/*  66 */     this.coinId = coin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/*  77 */     Item coin = null;
/*  78 */     Item targetItem = null;
/*     */     
/*     */     try {
/*  81 */       targetItem = Items.getItem(this.target);
/*     */     }
/*  83 */     catch (NoSuchItemException nsi) {
/*     */       
/*  85 */       getResponder().getCommunicator().sendNormalServerMessage("You fail to locate the target!");
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/*  90 */       coin = Items.getItem(this.coinId);
/*  91 */       if (coin.getOwner() == getResponder().getWurmId() && !coin.isBanked() && !coin.mailed) {
/*     */         
/*  93 */         String key = "data1";
/*  94 */         String val = aAnswers.getProperty("data1");
/*  95 */         if (val != null && val.length() > 0) {
/*     */           
/*  97 */           String tstring = ". Will the gods listen?";
/*  98 */           int x = rand.nextInt(6);
/*  99 */           if (x == 1) {
/* 100 */             tstring = ". Do you consider yourself lucky?";
/* 101 */           } else if (x == 2) {
/* 102 */             tstring = ". Is this your turn?";
/* 103 */           } else if (x == 3) {
/* 104 */             tstring = ". You get the feeling that someone listens.";
/* 105 */           } else if (x == 4) {
/* 106 */             tstring = ". Good luck!";
/* 107 */           } else if (x == 5) {
/* 108 */             tstring = ". Will it come true?";
/* 109 */           }  getResponder().getCommunicator().sendNormalServerMessage("You wish for " + val + tstring);
/* 110 */           long moneyVal = Economy.getValueFor(coin.getTemplateId());
/* 111 */           float chance = (float)moneyVal / 3.0E7F;
/* 112 */           float chantLevel = targetItem.getSpellCourierBonus();
/*     */           
/* 114 */           float timeBonus = WurmCalendar.isNight() ? 1.05F : 1.0F;
/*     */           
/* 116 */           float newchance = chance * targetItem.getCurrentQualityLevel() / 100.0F * (1.0F + chantLevel / 100.0F) * (1.0F + coin.getCurrentQualityLevel() / 1000.0F) * timeBonus;
/* 117 */           logger.log(Level.INFO, "New chance=" + newchance + " after coin=" + chance + ", chant=" + chantLevel + " ql=" + targetItem
/* 118 */               .getCurrentQualityLevel());
/* 119 */           boolean toFulfill = (rand.nextFloat() < newchance);
/* 120 */           if (getResponder().getPower() >= 5)
/* 121 */             toFulfill = true; 
/* 122 */           Connection dbcon = null;
/* 123 */           PreparedStatement ps = null;
/*     */           
/*     */           try {
/* 126 */             dbcon = DbConnector.getPlayerDbCon();
/* 127 */             ps = dbcon.prepareStatement("INSERT INTO WISHES (PLAYER,WISH,COIN,TOFULFILL) VALUES(?,?,?,?)");
/* 128 */             ps.setLong(1, getResponder().getWurmId());
/* 129 */             ps.setString(2, val);
/* 130 */             ps.setLong(3, moneyVal);
/* 131 */             ps.setBoolean(4, toFulfill);
/* 132 */             ps.executeUpdate();
/*     */           }
/* 134 */           catch (SQLException sqx) {
/*     */             
/* 136 */             logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */           }
/*     */           finally {
/*     */             
/* 140 */             DbUtilities.closeDatabaseObjects(ps, null);
/* 141 */             DbConnector.returnConnection(dbcon);
/*     */           } 
/* 143 */           Items.destroyItem(coin.getWurmId());
/* 144 */           if (toFulfill) {
/*     */             
/*     */             try {
/*     */               
/* 148 */               Mailer.sendMail(WebInterfaceImpl.mailAccount, "rolf@wurmonline.com", getResponder().getName() + " made a wish!", 
/* 149 */                   getResponder().getName() + " wants the wish " + val + " to be fulfilled!");
/*     */             
/*     */             }
/* 152 */             catch (Exception ex) {
/*     */               
/* 154 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */             } 
/*     */           }
/*     */         } else {
/*     */           
/* 159 */           getResponder().getCommunicator().sendNormalServerMessage("You make no wish this time.");
/*     */         } 
/*     */       } else {
/*     */         
/* 163 */         getResponder().getCommunicator().sendNormalServerMessage("You are no longer in possesion of the " + coin
/* 164 */             .getName() + "!");
/*     */         
/*     */         return;
/*     */       } 
/* 168 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 170 */       getResponder().getCommunicator().sendNormalServerMessage("You are no longer in possesion of the coin!");
/*     */       
/*     */       return;
/* 173 */     } catch (NotOwnedException no) {
/*     */       
/* 175 */       getResponder().getCommunicator().sendNormalServerMessage("You are no longer in possesion of the coin!");
/*     */       return;
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
/* 188 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/* 190 */     buf.append("harray{label{text='What is your wish?'};input{maxchars='40';id='data1'; text=''}}");
/* 191 */     buf.append("label{text=\"Just leave it blank if you don't want to lose your coin.\"}");
/*     */     
/* 193 */     buf.append(createAnswerButton2());
/* 194 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WishQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
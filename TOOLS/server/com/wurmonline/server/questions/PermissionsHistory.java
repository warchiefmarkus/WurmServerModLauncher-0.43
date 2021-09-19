/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.PermissionsHistories;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
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
/*     */ public class PermissionsHistory
/*     */   extends Question
/*     */   implements CounterTypes
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(PlanBridgeQuestion.class.getName());
/*     */ 
/*     */   
/*     */   public PermissionsHistory(Creature aResponder, long aTarget) {
/*  47 */     super(aResponder, "History of Permission Changes", getQuestion(aTarget), 126, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getQuestion(long aTarget) {
/*  53 */     int ct = WurmId.getType(aTarget);
/*  54 */     if (ct == 2) {
/*     */       
/*     */       try {
/*     */         
/*  58 */         Item item = Items.getItem(aTarget);
/*  59 */         if (item.isBoat())
/*  60 */           return "History of this Ship's Permissions changes"; 
/*  61 */         if (item.isBed())
/*  62 */           return "History of this Bed's Permissions changes"; 
/*  63 */         if (item.getTemplateId() == 186)
/*  64 */           return "History of this Small Cart's Permissions changes"; 
/*  65 */         if (item.getTemplateId() == 539)
/*  66 */           return "History of this Large Cart's Permissions changes"; 
/*  67 */         if (item.getTemplateId() == 850)
/*  68 */           return "History of this Large Wagon's Permissions changes"; 
/*  69 */         if (item.getTemplateId() == 853)
/*  70 */           return "History of this Large Ship Carrier's Permissions changes"; 
/*  71 */         if (item.getTemplateId() == 1410)
/*  72 */           return "History of this Creature Transporter's Permission changes"; 
/*  73 */         return "History of this Item's Permissions changes";
/*     */       }
/*  75 */       catch (NoSuchItemException e) {
/*     */         
/*  77 */         return "History of this not found Item's Permissions changes";
/*     */       } 
/*     */     }
/*  80 */     if (ct == 4)
/*  81 */       return "History of this Building's Permissions changes"; 
/*  82 */     if (ct == 5)
/*  83 */       return "History of this Door's Permissions changes"; 
/*  84 */     if (ct == 7)
/*  85 */       return "History of this Gate's Permissions changes"; 
/*  86 */     if (ct == 1) {
/*     */ 
/*     */       
/*     */       try {
/*  90 */         Creature creature = Creatures.getInstance().getCreature(aTarget);
/*  91 */         if (creature.isWagoner()) {
/*  92 */           return "History of this Wagoner's Permissions changes";
/*     */         }
/*  94 */       } catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */ 
/*     */       
/*  98 */       return "History of this Creature's Permissions changes";
/*     */     } 
/*     */     
/* 101 */     return "History of this Mine Door's Permissions changes";
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
/* 112 */     setAnswer(answers);
/* 113 */     if (this.type == 0) {
/*     */       
/* 115 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/* 118 */     if (this.type == 126);
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
/*     */   public void sendQuestion() {
/* 132 */     StringBuilder buf = new StringBuilder();
/* 133 */     buf.append(getBmlHeaderWithScrollAndQuestion());
/* 134 */     buf.append("label{text=\"\"}");
/*     */     
/* 136 */     String[] histories = PermissionsHistories.getPermissionsHistoryFor(this.target).getHistory(100);
/* 137 */     if (histories.length == 0) {
/*     */       
/* 139 */       buf.append("label{text=\"No History found!\"}");
/*     */     }
/*     */     else {
/*     */       
/* 143 */       for (String history : histories)
/*     */       {
/* 145 */         buf.append("label{text=\"" + history + "\"}");
/*     */       }
/*     */     } 
/*     */     
/* 149 */     buf.append("label{text=\"\"}");
/* 150 */     buf.append(createAnswerButton3());
/*     */     
/* 152 */     getResponder().getCommunicator().sendBml(500, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PermissionsHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
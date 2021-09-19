/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.io.IOException;
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
/*     */ public final class SwapDeityQuestion
/*     */   extends Question
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(GmTool.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   final Deity playerDeity;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SwapDeityQuestion(Creature aResponder) {
/*  48 */     super(aResponder, "Switch deity", "Which other deity do you want to change your deity to?", 110, -10L);
/*  49 */     this.playerDeity = aResponder.getDeity();
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
/*  60 */     setAnswer(answers);
/*  61 */     String did = answers.getProperty("did");
/*  62 */     int deityid = Integer.parseInt(did);
/*  63 */     Player player = (Player)getResponder();
/*  64 */     if (deityid != this.playerDeity.getNumber()) {
/*     */       
/*  66 */       Deity deity = Deities.getDeity(deityid);
/*     */ 
/*     */       
/*     */       try {
/*  70 */         player.setDeity(deity);
/*  71 */         player.getCommunicator().sendNormalServerMessage("You are now a follower of " + deity.name + ".");
/*  72 */         if (deity.getNumber() == 4)
/*     */         {
/*  74 */           if (player.getKingdomTemplateId() != 3)
/*     */           {
/*  76 */             player.setKingdomId((byte)3);
/*  77 */             player.setAlignment(Math.min(-50.0F, player.getAlignment()));
/*  78 */             player.getCommunicator().sendNormalServerMessage("You are now with the Horde of the Summoned.");
/*     */           }
/*     */         
/*     */         }
/*  82 */         else if (player.getAlignment() < 0.0F)
/*     */         {
/*  84 */           if (player.getKingdomId() == 3)
/*     */           {
/*  86 */             if (player.getCurrentTile().getKingdom() != 0) {
/*  87 */               player.setKingdomId(player.getCurrentTile().getKingdom());
/*     */             } else {
/*  89 */               player.setKingdomId((byte)4);
/*     */             }  } 
/*  91 */           player.setAlignment(50.0F);
/*     */         }
/*     */       
/*  94 */       } catch (IOException e) {
/*     */ 
/*     */         
/*  97 */         logger.log(Level.WARNING, e.getMessage(), e);
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
/* 110 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 111 */     if (this.playerDeity == null) {
/*     */       
/* 113 */       buf.append("label{type=\"bold\";text=\"You are not following any deity.}");
/* 114 */       buf.append("label{text=\"so therefore cannot change it!}");
/*     */     }
/*     */     else {
/*     */       
/* 118 */       buf.append("label{text=\"\"}");
/* 119 */       buf.append("label{type=\"bold\";text=\"Select your replacement deity.\"}");
/* 120 */       buf.append("label{type=\"italic\";text=\"Note that this is a once only option, and is not reverseable.\"}");
/* 121 */       buf.append("label{text=\"\"}");
/*     */       
/* 123 */       Deity[] deitys = Deities.getDeities();
/* 124 */       buf.append("table{rows=\"1\";cols=\"2\";");
/* 125 */       for (Deity d : deitys) {
/*     */         
/* 127 */         if (Servers.isThisAPvpServer() || d.getNumber() != 4) {
/* 128 */           buf.append("radio{group=\"did\";id=\"" + d.getNumber() + "\"};label{text=\"" + d
/* 129 */               .getName() + (
/* 130 */               (d.getNumber() == this.playerDeity.getNumber()) ? " (No Change)" : "") + "\"};");
/*     */         }
/*     */       } 
/* 133 */       buf.append("}");
/* 134 */       buf.append("label{text=\"\"}");
/* 135 */       buf.append(createAnswerButton2());
/*     */     } 
/*     */     
/* 138 */     getResponder().getCommunicator().sendBml(350, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SwapDeityQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
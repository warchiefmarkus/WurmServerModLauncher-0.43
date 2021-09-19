/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.epic.MissionHelper;
/*     */ import com.wurmonline.server.items.InscriptionData;
/*     */ import com.wurmonline.server.items.WurmColor;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
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
/*     */ public final class SimplePopup
/*     */   extends Question
/*     */ {
/*  34 */   private String toSend = "";
/*  35 */   private long missionId = -10L;
/*  36 */   private float maxHelps = -10.0F;
/*  37 */   private InscriptionData papyrusData = null;
/*     */ 
/*     */   
/*     */   public SimplePopup(Creature _responder, String _title, String _question) {
/*  41 */     super(_responder, _title, _question, 62, -10L);
/*  42 */     this.windowSizeX = 300;
/*  43 */     this.windowSizeY = 300;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimplePopup(Creature _responder, String _title, String _question, String _toSend) {
/*  48 */     super(_responder, _title, _question, 62, -10L);
/*  49 */     this.windowSizeX = 300;
/*  50 */     this.windowSizeY = 300;
/*  51 */     this.toSend = _toSend;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimplePopup(Creature _responder, String _title, InscriptionData thePapyrusData) {
/*  56 */     super(_responder, _title, "You find a message inscribed on the papyrus.", 62, -10L);
/*  57 */     this.windowSizeX = 350;
/*  58 */     this.windowSizeY = 300;
/*  59 */     this.papyrusData = thePapyrusData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimplePopup(Creature _responder, String _title, String _question, long _missionId, float _maxHelps) {
/*  65 */     super(_responder, _title, _question, 62, -10L);
/*  66 */     this.windowSizeX = 300;
/*  67 */     this.windowSizeY = 300;
/*  68 */     this.missionId = _missionId;
/*  69 */     this.maxHelps = _maxHelps;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion(String sendButtonText) {
/*  80 */     StringBuilder buf = new StringBuilder();
/*  81 */     buf.append(getBmlHeader());
/*  82 */     buf.append("text{text=\"\"}");
/*  83 */     if (this.missionId > 0L) {
/*     */       
/*  85 */       for (MissionHelper helper : MissionHelper.getHelpers().values()) {
/*     */         
/*  87 */         int i = helper.getHelps(this.missionId);
/*  88 */         if (i > 0) {
/*     */           
/*  90 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(helper.getPlayerId());
/*  91 */           if (pinf != null) {
/*  92 */             buf.append("text{text=\"" + pinf.getName() + ": " + i + " (" + ((i * 100) / this.maxHelps) + "%) \"}"); continue;
/*     */           } 
/*  94 */           buf.append("text{text=\"Unknown: " + i + " (" + ((i * 100) / this.maxHelps) + "%) \"}");
/*     */         } 
/*     */       } 
/*  97 */       buf.append("text{text=\"\"}");
/*     */     }
/*  99 */     else if (this.papyrusData != null) {
/*     */ 
/*     */       
/* 102 */       buf.append("input{id=\"answer\";enabled=\"false\";maxchars=\"" + this.papyrusData
/* 103 */           .getInscription().length() + "\";maxlines=\"-1\";bgcolor=\"200,200,200\";color=\"" + 
/*     */ 
/*     */ 
/*     */           
/* 107 */           WurmColor.getColorRed(this.papyrusData.getPenColour()) + "," + 
/* 108 */           WurmColor.getColorGreen(this.papyrusData.getPenColour()) + "," + 
/* 109 */           WurmColor.getColorBlue(this.papyrusData.getPenColour()) + "\";text=\"" + this.papyrusData
/* 110 */           .getInscription() + "\"}");
/* 111 */       buf.append("text{text=\"Signed " + this.papyrusData.getInscriber() + "\"};");
/*     */     }
/* 113 */     else if (this.toSend.length() > 0) {
/*     */       
/* 115 */       if (this.toSend.contains("{") && this.toSend.contains("}")) {
/* 116 */         buf.append(this.toSend);
/*     */       } else {
/* 118 */         buf.append("text{text=\"" + this.toSend + "\"}");
/* 119 */       }  buf.append("text{text=\"\"}");
/*     */     } 
/*     */     
/* 122 */     buf.append(createAnswerButton2(sendButtonText));
/* 123 */     getResponder().getCommunicator().sendBml(this.windowSizeX, this.windowSizeY, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 130 */     sendQuestion("Send");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToSend() {
/* 140 */     return this.toSend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToSend(String aToSend) {
/* 151 */     this.toSend = aToSend;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SimplePopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
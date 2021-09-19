/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.InscriptionData;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.WurmColor;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.villages.Citizen;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageMessages;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public class VillageMessagePopup
/*     */   extends Question
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(VillageMessagePopup.class.getName());
/*     */   private Village village;
/*     */   private InscriptionData papyrusData;
/*  48 */   private String message = null;
/*     */   private final Item messageBoard;
/*  50 */   private final Map<Integer, Long> idMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String red = "color=\"255,127,127\"";
/*     */ 
/*     */ 
/*     */   
/*     */   public VillageMessagePopup(Creature aResponder, Village aVillage, InscriptionData ins, long aSource, Item noticeBoard) {
/*  59 */     super(aResponder, getTitle(aVillage), getQuestion(aVillage), 137, aSource);
/*  60 */     this.messageBoard = noticeBoard;
/*  61 */     this.village = aVillage;
/*  62 */     this.papyrusData = ins;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getTitle(Village village) {
/*  67 */     return village.getName() + " notice board";
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getQuestion(Village village) {
/*  72 */     return "Add Note";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  83 */     setAnswer(aAnswer);
/*     */     
/*  85 */     String selected = aAnswer.getProperty("select");
/*  86 */     int select = Integer.parseInt(selected);
/*  87 */     if (select > 0) {
/*     */       
/*  89 */       long cit = ((Long)this.idMap.get(Integer.valueOf(select))).longValue();
/*  90 */       VillageMessages.create(this.village.getId(), getResponder().getWurmId(), cit, this.message, this.papyrusData
/*  91 */           .getPenColour(), (cit == -1L));
/*  92 */       if (cit == -1L) {
/*     */         
/*  94 */         getResponder().getCommunicator().sendNormalServerMessage("You posted a public notice.");
/*     */       }
/*  96 */       else if (cit == -10L) {
/*     */         
/*  98 */         getResponder().getCommunicator().sendNormalServerMessage("You posted a notice.");
/*     */       }
/*     */       else {
/*     */         
/* 102 */         getResponder().getCommunicator().sendNormalServerMessage("You posted a note to " + getPlayerName(cit) + ".");
/*     */       } 
/* 104 */       Items.destroyItem(this.target);
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
/*     */   
/*     */   public void sendQuestion() {
/* 117 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 119 */     buf.append(getBmlHeader());
/*     */     
/* 121 */     int msglen = this.papyrusData.getInscription().length();
/* 122 */     int mlen = Math.min(msglen, 500);
/* 123 */     this.message = this.papyrusData.getInscription().substring(0, mlen);
/* 124 */     buf.append("input{id=\"answer\";enabled=\"false\";maxchars=\"" + mlen + "\";maxlines=\"-1\";bgcolor=\"200,200,200\";color=\"" + 
/*     */ 
/*     */ 
/*     */         
/* 128 */         WurmColor.getColorRed(this.papyrusData.getPenColour()) + "," + 
/* 129 */         WurmColor.getColorGreen(this.papyrusData.getPenColour()) + "," + 
/* 130 */         WurmColor.getColorBlue(this.papyrusData.getPenColour()) + "\";text=\"" + this.message + "\"}");
/*     */     
/* 132 */     buf.append("text{text=\"\"}");
/*     */     
/* 134 */     if (mlen < msglen)
/*     */     {
/* 136 */       buf.append("label{color=\"255,127,127\"text=\"Message is too long, so will be truncated.\"};");
/*     */     }
/*     */     
/* 139 */     buf.append("harray{text{type=\"bold\";text=\"Post\"};dropdown{id=\"select\";options=\"");
/* 140 */     buf.append("no where");
/* 141 */     this.idMap.put(Integer.valueOf(0), Long.valueOf(-10L));
/*     */     
/* 143 */     if (this.messageBoard.mayPostNotices(getResponder())) {
/*     */       
/* 145 */       if (getResponder().getCitizenVillage() == this.village) {
/*     */         
/* 147 */         this.idMap.put(Integer.valueOf(this.idMap.size()), Long.valueOf(-10L));
/* 148 */         buf.append(",as village notice");
/*     */       } 
/* 150 */       this.idMap.put(Integer.valueOf(this.idMap.size()), Long.valueOf(-1L));
/* 151 */       buf.append(",as public notice");
/*     */     } 
/*     */     
/* 154 */     if (this.messageBoard.mayAddPMs(getResponder())) {
/*     */ 
/*     */       
/* 157 */       Citizen[] citizens = this.village.getCitizens();
/* 158 */       Arrays.sort((Object[])citizens);
/*     */       
/* 160 */       for (Citizen c : citizens) {
/*     */ 
/*     */         
/* 163 */         if (c.isPlayer() && c.getId() != getResponder().getWurmId())
/*     */         {
/*     */           
/* 166 */           if (getPlayerName(c.getId()).length() > 0) {
/*     */             
/* 168 */             this.idMap.put(Integer.valueOf(this.idMap.size()), Long.valueOf(c.getId()));
/* 169 */             buf.append(",to " + c.getName());
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 174 */     buf.append("\"}}");
/* 175 */     buf.append(createAnswerButton2());
/* 176 */     getResponder().getCommunicator().sendBml(400, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private final String getPlayerName(long id) {
/* 181 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(id);
/* 182 */     if (info == null)
/* 183 */       return ""; 
/* 184 */     return info.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageMessagePopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
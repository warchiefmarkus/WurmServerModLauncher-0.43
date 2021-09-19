/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.WurmColor;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageMessage;
/*     */ import com.wurmonline.server.villages.VillageMessages;
/*     */ import com.wurmonline.server.villages.VillageRole;
/*     */ import java.util.Arrays;
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
/*     */ public class VillageMessageBoard
/*     */   extends Question
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(VillageMessageBoard.class.getName());
/*     */   
/*     */   private Village village;
/*     */   private Item messageBoard;
/*     */   
/*     */   public VillageMessageBoard(Creature aResponder, Village aVillage, Item noticeBoard) {
/*  48 */     super(aResponder, getTitle(aVillage), "", 136, noticeBoard.getWurmId());
/*  49 */     this.village = aVillage;
/*  50 */     this.messageBoard = noticeBoard;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getTitle(Village village) {
/*  55 */     return village.getName() + " notice board";
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
/*     */   public void answer(Properties aAnswer) {
/*  71 */     setAnswer(aAnswer);
/*     */     
/*  73 */     for (String key : getAnswer().stringPropertyNames()) {
/*     */       
/*  75 */       if (key.startsWith("del")) {
/*     */ 
/*     */         
/*  78 */         String sid = key.substring(3);
/*  79 */         long posted = Long.parseLong(sid);
/*  80 */         VillageMessages.delete(this.village.getId(), getResponder().getWurmId(), posted);
/*     */         
/*  82 */         VillageMessageBoard vmb = new VillageMessageBoard(getResponder(), this.village, this.messageBoard);
/*  83 */         vmb.sendQuestion();
/*     */         return;
/*     */       } 
/*  86 */       if (key.startsWith("rem")) {
/*     */ 
/*     */         
/*  89 */         String sid = key.substring(3);
/*  90 */         long posted = Long.parseLong(sid);
/*  91 */         VillageMessages.delete(this.village.getId(), -10L, posted);
/*     */         
/*  93 */         VillageMessageBoard vmb = new VillageMessageBoard(getResponder(), this.village, this.messageBoard);
/*  94 */         vmb.sendQuestion();
/*     */         return;
/*     */       } 
/*  97 */       if (key.startsWith("pub")) {
/*     */ 
/*     */         
/* 100 */         String sid = key.substring(3);
/* 101 */         long posted = Long.parseLong(sid);
/* 102 */         VillageMessages.delete(this.village.getId(), -1L, posted);
/*     */         
/* 104 */         VillageMessageBoard vmb = new VillageMessageBoard(getResponder(), this.village, this.messageBoard);
/* 105 */         vmb.sendQuestion();
/*     */         return;
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
/* 119 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 121 */     buf.append("border{border{size=\"20,20\";null;null;label{type='bold';text=\"" + this.question + "\"};harray{label{text=\" \"};button{text=\"Close\";id=\"close\"};label{text=\" \"}};null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
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
/* 132 */         getId() + "\"}");
/*     */     
/* 134 */     buf.append("label{type=\"bold\";text=\"Public notices\"}");
/* 135 */     if (this.messageBoard.mayAccessHold(getResponder())) {
/*     */       
/* 137 */       VillageMessage[] publicNotices = VillageMessages.getVillageMessages(this.village.getId(), -1L);
/* 138 */       if (publicNotices.length > 0) {
/*     */         
/* 140 */         Arrays.sort((Object[])publicNotices);
/* 141 */         for (VillageMessage vm : publicNotices)
/* 142 */           buf.append(showMessage(vm)); 
/* 143 */         buf.append("label{text=\"\"}");
/*     */       } else {
/*     */         
/* 146 */         buf.append("label{type=\"bold\";text=\"none.\"}");
/*     */       } 
/*     */     } else {
/* 149 */       buf.append("label{type=\"bold\";text=\"no permission.\"}");
/*     */     } 
/* 151 */     if (this.messageBoard.mayAccessHold(getResponder()) && getResponder().getCitizenVillage() == this.village) {
/*     */       
/* 153 */       VillageMessage[] notices = VillageMessages.getVillageMessages(this.village.getId(), -10L);
/* 154 */       if (notices.length > 0) {
/*     */         
/* 156 */         buf.append("label{type=\"bold\";text=\"Village notices for: " + this.village.getName() + "\"}");
/* 157 */         Arrays.sort((Object[])notices);
/* 158 */         for (VillageMessage vm : notices) {
/* 159 */           buf.append(showMessage(vm));
/*     */         }
/*     */       } else {
/* 162 */         buf.append("label{text=\"No village notices\"}");
/*     */       } 
/* 164 */       buf.append("label{text=\"\"}");
/* 165 */       buf.append("text{type=\"bold\";text=\"Personal messages for: " + getResponder().getName() + "\"}");
/* 166 */       VillageMessage[] personals = VillageMessages.getVillageMessages(this.village.getId(), getResponder().getWurmId());
/* 167 */       if (personals.length > 0) {
/*     */         
/* 169 */         Arrays.sort((Object[])personals);
/* 170 */         for (VillageMessage vm : personals) {
/* 171 */           buf.append(showMessage(vm));
/*     */         }
/*     */       } else {
/* 174 */         buf.append("label{text=\"None.\"}");
/*     */       } 
/*     */     } 
/* 177 */     buf.append("}};null;null;}");
/* 178 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String showMessage(VillageMessage vm) {
/* 183 */     StringBuilder buf = new StringBuilder();
/*     */ 
/*     */     
/* 186 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(vm.getPosterId());
/* 187 */     if (info == null)
/* 188 */       return buf.toString(); 
/* 189 */     VillageRole vr = this.village.getRoleFor(vm.getPosterId());
/*     */     
/* 191 */     if (vr == null)
/* 192 */       return buf.toString(); 
/* 193 */     buf.append("label{text=\"\"}");
/* 194 */     buf.append("input{id=\"ans" + vm.getPostedTime() + "\";enabled=\"false\";maxchars=\"" + vm
/*     */         
/* 196 */         .getMessage().length() + "\";maxlines=\"-1\";bgcolor=\"200,200,200\";color=\"" + 
/*     */ 
/*     */         
/* 199 */         WurmColor.getColorRed(vm.getPenColour()) + "," + 
/* 200 */         WurmColor.getColorGreen(vm.getPenColour()) + "," + 
/* 201 */         WurmColor.getColorBlue(vm.getPenColour()) + "\";text=\"" + vm
/* 202 */         .getMessage() + "\"}");
/*     */ 
/*     */     
/* 205 */     if (vm.getToId() == -10L || vm.getToId() == -1L) {
/*     */       
/* 207 */       String id = (vm.getToId() == -10L) ? ("rem" + vm.getPostedTime()) : ("pub" + vm.getPostedTime());
/* 208 */       String note = (vm.getToId() == -10L) ? "village notice" : "public notice";
/*     */ 
/*     */ 
/*     */       
/* 212 */       String delButton = "harray{label{text=\" \"};button{text=\"Delete\";id=\"" + id + "\"confirm=\"You are about to delete a " + note + " posted by " + vm.getPosterName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"};}";
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       if (!this.messageBoard.mayCommand(getResponder()) && vm.getPosterId() != getResponder().getWurmId())
/* 218 */         delButton = "null;"; 
/* 219 */       buf.append("border{size=\"20,20\";null;null;harray{label{text=\"Posted by: " + info
/*     */ 
/*     */ 
/*     */           
/* 223 */           .getName() + "  \"};label{text=\"Role: " + vr
/* 224 */           .getName() + "  \"};label{text=\"When: " + vm
/* 225 */           .getDate() + "\"}};" + delButton + "null;}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 236 */       String delButton = "harray{label{text=\" \"};button{text=\"Delete\";id=\"del" + vm.getPostedTime() + "\"confirm=\"You are about to delete a personal message from " + vm.getPosterName() + ".\";question=\"Do you really want to do that?\"}label{text=\" \"};}";
/*     */ 
/*     */ 
/*     */       
/* 240 */       buf.append("border{size=\"20,20\";null;null;harray{label{text=\"From:" + info
/*     */ 
/*     */ 
/*     */           
/* 244 */           .getName() + "  \"};label{text=\"Role:" + vr
/* 245 */           .getName() + "  \"};label{text=\"When:" + vm
/* 246 */           .getDate() + "\"}};" + delButton + "null;}");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageMessageBoard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.behaviours.FishEnums;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.ai.scripts.FishAI;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CreatureDataQuestion
/*     */   extends Question
/*     */   implements ItemMaterials
/*     */ {
/*     */   private static final String red = "color=\"255,127,127\"";
/*     */   private static final String green = "color=\"127,255,127\"";
/*     */   private final Creature creature;
/*     */   
/*     */   public CreatureDataQuestion(Creature aResponder, Creature target) {
/*  46 */     super(aResponder, "Creature data", "Set the desired data:", 154, target.getWurmId());
/*  47 */     this.creature = target;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  53 */     setAnswer(answers);
/*  54 */     FishAI.FishAIData faid = (FishAI.FishAIData)this.creature.getCreatureAIData();
/*  55 */     boolean accept = getBooleanProp("accept");
/*  56 */     boolean settarget = getBooleanProp("settarget");
/*  57 */     if (accept) {
/*     */       
/*  59 */       byte fishTypeId = Byte.parseByte(answers.getProperty("ftype"));
/*  60 */       float ql = Float.parseFloat(answers.getProperty("ql"));
/*     */       
/*  62 */       this.creature.setVisible(false);
/*  63 */       faid.setFishTypeId(fishTypeId);
/*  64 */       faid.setQL(ql);
/*  65 */       this.creature.setVisible(true);
/*  66 */       CreatureDataQuestion spm = new CreatureDataQuestion(getResponder(), this.creature);
/*  67 */       spm.sendQuestion();
/*     */     }
/*  69 */     else if (settarget) {
/*     */       
/*  71 */       float tx = Float.parseFloat(answers.getProperty("tx"));
/*  72 */       float ty = Float.parseFloat(answers.getProperty("ty"));
/*  73 */       if (tx != -1.0F && ty != -1.0F) {
/*  74 */         faid.setTargetPos(tx, ty);
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
/*     */   public void sendQuestion() {
/*  86 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*  87 */     buf.append("label{text=\"Name: " + this.creature.getName() + "\"}");
/*     */     
/*  89 */     FishAI.FishAIData faid = (FishAI.FishAIData)this.creature.getCreatureAIData();
/*  90 */     FishEnums.FishData fd = faid.getFishData();
/*  91 */     String options = "None";
/*  92 */     for (FishEnums.FishData ffd : FishEnums.FishData.values()) {
/*  93 */       if (ffd.getTypeId() > 0)
/*  94 */         options = options + "," + ffd.getName(); 
/*  95 */     }  buf.append("label{type=\"bolditalic\"; text=\"When changing type, accept before changing anything else. \"}");
/*  96 */     buf.append("table{rows=\"1\";cols=\"5\";");
/*  97 */     buf.append("label{text=\"Fish type:\"}dropdown{id=\"ftype\";options=\"" + options + "\";default=\"" + faid
/*  98 */         .getFishTypeId() + "\"}");
/*  99 */     buf.append("label{text=\" QL:\"}input{id=\"ql\"; maxchars=\"6\";text=\"" + faid
/* 100 */         .getQL() + "\"}");
/* 101 */     buf.append("harray{label{text=\" \"};button{text=\"Accept\";id=\"accept\"}}");
/* 102 */     buf.append("}");
/* 103 */     buf.append("label{type=\"bolditalic\"; text=\"Movement, set target position (m).\";hover=\"position in meters\"}");
/* 104 */     buf.append("table{rows=\"1\";cols=\"5\";");
/* 105 */     buf.append("label{text=\"Target:\"}input{id=\"tx\"; maxchars=\"6\";text=\"" + faid
/* 106 */         .getTargetPosX() + "\"}label{text=\",\"}input{id=\"ty\"; maxchars=\"6\";text=\"" + faid
/*     */         
/* 108 */         .getTargetPosY() + "\"}");
/* 109 */     buf.append("harray{label{text=\" \"};button{text=\"Set Target\";id=\"settarget\"}}");
/* 110 */     buf.append("}");
/* 111 */     buf.append("label{type=\"bolditalic\";text=\"Following cannot be changed, but are calculated from type and ql. \"}");
/* 112 */     buf.append("table{rows=\"1\";cols=\"6\";");
/* 113 */     buf.append("label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Value\"};label{type=\"bold\";text=\"Base\"};label{type=\"bold\";text=\"Name\"};label{type=\"bold\";text=\"Value\"};label{type=\"bold\";text=\"Base\"}");
/*     */     
/* 115 */     buf.append("label{type=\"bold\";text=\"Speed:\"};label{text=\"" + faid.getSpeed() + "\"};label{text=\"" + fd.getBaseSpeed() + "\"};label{type=\"bold\";text=\"\"};label{type=\"bold\";text=\"\"};label{type=\"bold\";text=\"\"}");
/*     */     
/* 117 */     buf.append("label{type=\"bold\";text=\"Body Strength:\"};label{text=\"" + faid.getBodyStrength() + "\"};label{text=\"" + fd.getBodyStrength() + "\"};label{type=\"bold\";text=\"Body Stamina:\"};label{text=\"" + faid
/* 118 */         .getBodyStamina() + "\"};label{text=\"" + fd.getBodyStamina() + "\"}");
/* 119 */     buf.append("label{type=\"bold\";text=\"Body Control:\"};label{text=\"" + faid.getBodyControl() + "\"};label{text=\"" + fd.getBodyControl() + "\"};label{type=\"bold\";text=\"Mind Speed:\"};label{text=\"" + faid
/* 120 */         .getMindSpeed() + "\"};label{text=\"" + fd.getMindSpeed() + "\"}");
/* 121 */     buf.append("}");
/* 122 */     buf.append("label{type=\"bolditalic\";text=\"Following cannot be changed, Fish Data from fish type id. \"}");
/* 123 */     buf.append("table{rows=\"1\";cols=\"6\";");
/* 124 */     buf.append("label{type=\"bold\";text=\"Name:\"};label{text=\"" + fd.getName() + "\"}label{text=\"\"};label{text=\"\"}label{type=\"bold\";text=\"Special:\"};label{" + 
/*     */         
/* 126 */         showBoolean(fd.isSpecialFish()) + "};");
/* 127 */     buf.append("label{type=\"bold\";text=\"Surface:\"};label{" + showBoolean(fd.onSurface()) + "};label{type=\"bold\";text=\"Water:\"};label{" + 
/* 128 */         showBoolean(fd.inWater()) + "}label{type=\"bold\";text=\"Pond:\"};label{" + 
/* 129 */         showBoolean(fd.inPond()) + "};");
/* 130 */     buf.append("label{type=\"bold\";text=\"Lake:\"};label{" + showBoolean(fd.inLake()) + "}label{type=\"bold\";text=\"Sea:\"};label{" + 
/* 131 */         showBoolean(fd.inSea()) + "};label{type=\"bold\";text=\"Shallows:\"};label{" + 
/* 132 */         showBoolean(fd.inShallows()) + "}");
/* 133 */     buf.append("label{type=\"bold\";text=\"Min depth:\"};label{text=\"" + fd.getMinDepth() + "\"}label{type=\"bold\";text=\"Max depth:\"};label{text=\"" + fd
/* 134 */         .getMaxDepth() + "\"};label{type=\"bold\";text=\"Keeper weight:\"};label{text=\"" + fd
/* 135 */         .getMinWeight() + "\"}");
/* 136 */     buf.append("label{type=\"bold\";text=\"Use Pole:\"};label{" + showBoolean(fd.useFishingPole()) + "}label{type=\"bold\";text=\"Use Net:\"};label{" + 
/* 137 */         showBoolean(fd.useFishingNet()) + "};label{type=\"bold\";text=\"Use Spear:\"};label{" + 
/* 138 */         showBoolean(fd.useSpear()) + "}");
/* 139 */     buf.append("label{type=\"bold\";text=\"Use Basic Rod:\"};label{" + showBoolean(fd.useReelBasic()) + "}label{type=\"bold\";text=\"Use Fine Rod:\"};label{" + 
/* 140 */         showBoolean(fd.useReelFine()) + "};label{type=\"bold\";text=\"Use Deep Water Rod:\"};label{" + 
/* 141 */         showBoolean(fd.useReelWater()) + "}");
/* 142 */     buf.append("label{type=\"bold\";text=\"Use Professional Rod:\"};label{" + showBoolean(fd.useReelProfessional()) + "}label{type=\"bold\";text=\"\"};label{text=\"\"};label{type=\"bold\";text=\"\"};label{text=\"\"}");
/*     */ 
/*     */     
/* 145 */     buf.append("}");
/* 146 */     buf.append("label{type=\"bolditalic\";text=\"Lots more could be added here...\";hover=\"e.g. feed heights, baits?, damage mod\"}");
/*     */     
/* 148 */     buf.append("}};null;null;}");
/* 149 */     getResponder().getCommunicator().sendBml(400, 440, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   private String showBoolean(boolean flag) {
/* 154 */     StringBuilder buf = new StringBuilder();
/* 155 */     if (flag) {
/* 156 */       buf.append("color=\"127,255,127\"");
/*     */     } else {
/* 158 */       buf.append("color=\"255,127,127\"");
/* 159 */     }  buf.append("text=\"" + flag + "\"");
/* 160 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\CreatureDataQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
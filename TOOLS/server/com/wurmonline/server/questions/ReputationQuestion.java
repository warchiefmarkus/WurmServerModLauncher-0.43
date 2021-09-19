/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Reputation;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class ReputationQuestion
/*     */   extends Question
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(ReputationQuestion.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  49 */   private final Map<Long, Integer> itemMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   public ReputationQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  53 */     super(aResponder, aTitle, aQuestion, 24, aTarget);
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
/*  64 */     setAnswer(answers);
/*  65 */     QuestionParser.parseReputationQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Long, Integer> getItemMap() {
/*  74 */     return this.itemMap;
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
/*     */       Village village;
/* 158 */       int ids = 0;
/*     */ 
/*     */       
/* 161 */       if (this.target == -10L) {
/* 162 */         village = getResponder().getCitizenVillage();
/*     */       } else {
/*     */         
/* 165 */         Item deed = Items.getItem(this.target);
/* 166 */         int villageId = deed.getData2();
/* 167 */         village = Villages.getVillage(villageId);
/*     */       } 
/*     */       
/* 170 */       if (village == null)
/*     */       {
/* 172 */         getResponder().getCommunicator().sendNormalServerMessage("No settlement found.");
/*     */       }
/*     */       else
/*     */       {
/* 176 */         Reputation[] reputations = village.getReputations();
/* 177 */         boolean kos = village.isKosAllowed();
/*     */         
/* 179 */         StringBuilder buf = new StringBuilder(getBmlHeader());
/* 180 */         buf.append("text{type=\"bold\";text=\"Reputations for " + village.getName() + "\"}text{text=\"\"}");
/* 181 */         buf.append("text{text=\"Permanent means that it will not change. If set to 0 it will go away though.\"}");
/* 182 */         buf.append("text{text=\"Use permanent with care and normally to point out enemies, since it effectively overrides any settlement role settings.\"}");
/* 183 */         buf.append("text{text=\"Max is 100 and min is -100. The guards attack at -30.\"}");
/* 184 */         buf.append("text{text=\"\"}");
/*     */         
/* 186 */         boolean showlist = true;
/* 187 */         if (Features.Feature.HIGHWAYS.isEnabled() && !kos) {
/*     */           
/* 189 */           if (village.hasHighway()) {
/* 190 */             buf.append("text{color=\"155,155,50\";text=\"KOS is disabled for this settlement as there is highway in it.\"}");
/*     */           } else {
/* 192 */             buf.append("text{text=\"Note: KOS is not enabled for this settlement, to change this, use settlement settings.\"}");
/* 193 */           }  buf.append("text{text=\"\"}");
/* 194 */           showlist = (reputations.length > 0);
/*     */         } 
/* 196 */         if (Features.Feature.HIGHWAYS.isEnabled() && kos && reputations.length == 0)
/*     */         {
/* 198 */           buf.append("text{color=\"155,155,50\";text=\"If you add anyone to KOS, you will not be able to add a highway in this village.\"}");
/*     */         }
/* 200 */         int szy = 300;
/* 201 */         if (showlist) {
/*     */           
/* 203 */           buf.append("table{rows=\"" + (Math.min(100, reputations.length) + 2) + "\";cols=\"3\";");
/* 204 */           buf.append("label{text=\"Creature name\"};label{text=\"Reputation\"};label{text=\"Permanent\"}");
/*     */ 
/*     */ 
/*     */           
/* 208 */           if (!Features.Feature.HIGHWAYS.isEnabled() || kos) {
/*     */ 
/*     */             
/* 211 */             buf.append("harray{input{maxchars=\"40\";id=\"nn\"};label{text=\" \"}}");
/* 212 */             buf.append("harray{input{maxchars=\"4\"; id=\"nr\";text=\"-100\"};label{text=\" \"}}");
/* 213 */             buf.append("checkbox{id=\"np\";selected=\"false\";text=\" \"}");
/*     */           } 
/* 215 */           szy = 400;
/* 216 */           if (reputations.length > 10)
/* 217 */             szy = 500; 
/* 218 */           for (int x = 0; x < reputations.length; x++) {
/*     */             
/* 220 */             if (ids < 100) {
/*     */               
/* 222 */               long wid = reputations[x].getWurmId();
/*     */               
/*     */               try {
/* 225 */                 ids++;
/* 226 */                 buf.append("label{text=\"" + reputations[x].getNameFor() + "\"};");
/* 227 */                 buf.append("harray{input{maxchars=\"4\"; id=\"" + ids + "r\"; text=\"" + reputations[x].getValue() + "\"};label{text=\" \"}}");
/* 228 */                 String ch = reputations[x].isPermanent() ? "selected=\"true\";" : "";
/* 229 */                 buf.append("checkbox{id=\"" + ids + "p\";" + ch + "text= \" \"}");
/* 230 */                 this.itemMap.put(new Long(wid), Integer.valueOf(ids));
/*     */               }
/* 232 */               catch (NoSuchPlayerException nsp) {
/*     */                 
/* 234 */                 village.removeReputation(wid);
/*     */               } 
/*     */             } 
/*     */           } 
/* 238 */           buf.append("}");
/*     */           
/* 240 */           if (ids >= 99) {
/* 241 */             buf.append("text{text=\"The list was truncated. Some reputations are missing.\"}");
/*     */           }
/* 243 */           buf.append("text{text=\"\"}");
/*     */         } 
/* 245 */         buf.append(createAnswerButton2());
/*     */         
/* 247 */         getResponder().getCommunicator().sendBml(500, szy, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */       }
/*     */     
/* 250 */     } catch (NoSuchItemException nsi) {
/*     */       
/* 252 */       getResponder().getCommunicator().sendNormalServerMessage("No such item.");
/* 253 */       logger.log(Level.WARNING, getResponder().getName(), (Throwable)nsi);
/*     */     }
/* 255 */     catch (NoSuchVillageException nsp) {
/*     */       
/* 257 */       getResponder().getCommunicator().sendNormalServerMessage("No such settlement.");
/* 258 */       logger.log(Level.WARNING, getResponder().getName(), (Throwable)nsp);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ReputationQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Cultist;
/*     */ import com.wurmonline.server.players.Cults;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public class CultQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*     */   private Cultist cultist;
/*     */   private final boolean leavePath;
/*     */   private final boolean askStatus;
/*     */   private final byte path;
/*  43 */   private static final Logger logger = Logger.getLogger(CultQuestion.class.getName());
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
/*     */   public CultQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, @Nullable Cultist _cultist, byte _path, boolean leave, boolean _askStatus) {
/*  56 */     super(aResponder, aTitle, aQuestion, 78, aTarget);
/*  57 */     this.cultist = _cultist;
/*  58 */     this.path = _path;
/*  59 */     this.leavePath = leave;
/*  60 */     this.askStatus = _askStatus;
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
/*     */   public void answer(Properties aAnswers) {
/*  72 */     if (this.askStatus)
/*     */       return; 
/*  74 */     String prop = aAnswers.getProperty("quit");
/*  75 */     if (prop != null) {
/*     */ 
/*     */       
/*     */       try {
/*  79 */         int num = Integer.parseInt(prop);
/*  80 */         if (num == 1)
/*     */         {
/*  82 */           if (this.cultist == null) {
/*  83 */             getResponder().getCommunicator().sendNormalServerMessage("You are not following a philosophical path!");
/*     */           } else {
/*     */             
/*  86 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to stop pursuing the insights of " + 
/*  87 */                 Cults.getPathNameFor(this.path) + ".");
/*     */             
/*     */             try {
/*  90 */               this.cultist.deleteCultist();
/*  91 */               if (getResponder().isPlayer())
/*     */               {
/*  93 */                 ((Player)getResponder()).setLastChangedPath(System.currentTimeMillis());
/*     */               }
/*     */               
/*     */               return;
/*  97 */             } catch (IOException iox) {
/*     */               
/*  99 */               logger.log(Level.WARNING, getResponder().getName() + ":" + iox.getMessage(), iox);
/*     */             }
/*     */           
/*     */           } 
/*     */         }
/* 104 */       } catch (NumberFormatException nsf) {
/*     */         
/* 106 */         getResponder().getCommunicator().sendNormalServerMessage("The answer you provided was impossible to understand. You are sorry.");
/*     */         
/*     */         return;
/*     */       } 
/* 110 */       getResponder().getCommunicator().sendNormalServerMessage("You decide to keep pursuing the insights of " + 
/* 111 */           Cults.getPathNameFor(this.path) + ".");
/*     */       return;
/*     */     } 
/* 114 */     prop = aAnswers.getProperty("answer");
/* 115 */     if (prop != null) {
/*     */ 
/*     */       
/*     */       try {
/* 119 */         int num = Integer.parseInt(prop);
/* 120 */         if (this.cultist == null) {
/*     */           
/* 122 */           if (num == 1) {
/*     */             
/* 124 */             if (getResponder().isPlayer())
/*     */             {
/* 126 */               if (System.currentTimeMillis() - ((Player)getResponder()).getLastChangedPath() < 86400000L) {
/*     */                 
/* 128 */                 getResponder()
/* 129 */                   .getCommunicator()
/* 130 */                   .sendNormalServerMessage("You recently left a cult and need to contemplate the changes for another " + 
/*     */                     
/* 132 */                     Server.getTimeFor(((Player)getResponder()).getLastChangedPath() + 86400000L - 
/* 133 */                       System.currentTimeMillis()) + " before embarking on a new philosophical journey.");
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             }
/* 138 */             getResponder().getCommunicator().sendNormalServerMessage("You decide to start pursuing the insights of " + 
/* 139 */                 Cults.getPathNameFor(this.path) + ".");
/* 140 */             this.cultist = new Cultist(getResponder().getWurmId(), this.path);
/*     */ 
/*     */             
/* 143 */             getResponder().achievement(548);
/*     */           } else {
/*     */             
/* 146 */             getResponder().getCommunicator().sendNormalServerMessage("You decide not to follow " + 
/* 147 */                 Cults.getPathNameFor(this.path) + ".");
/*     */           } 
/* 149 */         } else if (num == Cults.getCorrectAnswerForNextLevel(this.cultist.getPath(), this.cultist.getLevel())) {
/*     */           
/* 151 */           if (this.cultist == null)
/* 152 */             this.cultist = new Cultist(getResponder().getWurmId(), this.path); 
/* 153 */           getResponder().getCommunicator().sendSafeServerMessage(
/* 154 */               Cults.getCorrectAnswerStringForNextLevel(this.path, this.cultist.getLevel()));
/* 155 */           this.cultist.increaseLevel();
/*     */ 
/*     */           
/*     */           try {
/* 159 */             this.cultist.saveCultist(false);
/*     */           }
/* 161 */           catch (IOException iox) {
/*     */             
/* 163 */             logger.log(Level.WARNING, "Failed to set " + 
/* 164 */                 getResponder().getName() + " to level " + iox.getMessage(), iox);
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 169 */         else if (this.cultist == null) {
/* 170 */           getResponder().getCommunicator().sendNormalServerMessage(
/* 171 */               Cults.getWrongAnswerStringForLevel(this.path, (byte)0));
/*     */         } else {
/*     */           
/* 174 */           this.cultist.failedToLevel();
/* 175 */           getResponder().getCommunicator().sendNormalServerMessage(
/* 176 */               Cults.getWrongAnswerStringForLevel(this.path, this.cultist.getLevel()));
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 181 */       catch (NumberFormatException nsf) {
/*     */         
/* 183 */         getResponder().getCommunicator().sendNormalServerMessage("The answer you provided was impossible to understand. You are sorry.");
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } else {
/* 189 */       getResponder().getCommunicator().sendNormalServerMessage("You decide not to answer the question right now and instead meditate more.");
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
/* 201 */     StringBuilder buf = new StringBuilder();
/* 202 */     buf.append(getBmlHeader());
/* 203 */     int width = 300;
/* 204 */     int height = 330;
/* 205 */     if (this.askStatus) {
/*     */       
/* 207 */       buf.append("text{text='You consider the local leaders of the path:'}");
/* 208 */       Map<Integer, Set<Cultist>> treemap = Cultist.getCultistLeaders(this.cultist.getPath(), getResponder()
/* 209 */           .getKingdomId());
/*     */       
/* 211 */       boolean showedLevel = false;
/* 212 */       int localServer = Servers.localServer.id;
/* 213 */       for (Integer level : treemap.keySet())
/*     */       {
/* 215 */         Set<Cultist> subset = treemap.get(level);
/* 216 */         buf.append("text{text='" + Cults.getNameForLevel(this.cultist.getPath(), level.byteValue()) + ":'}");
/* 217 */         for (Cultist cist : subset) {
/*     */           
/* 219 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(cist.getWurmId());
/*     */           
/* 221 */           if (pinf != null && pinf.currentServer == localServer) {
/*     */             
/* 223 */             if (pinf.wurmId == this.cultist.getWurmId()) {
/* 224 */               buf.append("text{type=\"bold\";text='" + pinf.getName() + " '}");
/*     */               continue;
/*     */             } 
/* 227 */             if (!showedLevel)
/*     */             {
/* 229 */               if (level.byteValue() - this.cultist.getLevel() == 3) {
/*     */                 
/* 231 */                 buf.append("text{type='bold';text='Those on this level may help you advance the path:'}");
/* 232 */                 showedLevel = true;
/*     */               } 
/*     */             }
/* 235 */             buf.append("label{text=\"" + pinf.getName() + " \"}");
/*     */           } 
/*     */         } 
/*     */         
/* 239 */         buf.append("text{text=''}");
/* 240 */         width = 500;
/* 241 */         height = 400;
/*     */       }
/*     */     
/* 244 */     } else if (this.leavePath) {
/*     */       
/* 246 */       buf.append("text{text='Select quit to stop following this path. The result is immediate and dramatic:'}");
/* 247 */       buf.append("radio{ group='quit'; id='0';text='Stay';selected='true'}");
/* 248 */       buf.append("radio{ group='quit'; id='1';text='Quit'}");
/* 249 */       buf.append("text{text=''}");
/*     */     }
/* 251 */     else if (this.cultist == null) {
/*     */       
/* 253 */       buf.append("text{text=\"As you meditate upon these things you realize that there is a pattern of thinking that you can try to follow.\"}");
/* 254 */       buf.append("text{text=\"If this path contains the truth or simply the figment of someones imagination, you do not know.\"}");
/* 255 */       buf.append("text{text=\"Nonetheless, it may pose an interesting challenge.\"}");
/* 256 */       buf.append("text{text=\"Do you wish to embark on the philosophical journey of " + Cults.getPathNameFor(this.path) + "?\"}");
/*     */       
/* 258 */       buf.append("text{text=\"If you choose yes, know that you join the Cult of " + Cults.getPathNameFor(this.path) + " with secrets supposed to lead to enlightenment. Divulging those secrets may lead to expulsion.\"}");
/*     */       
/* 260 */       buf.append("text{type='bold';text=\"If you decide to join, you will be challenged by the selected path as you visit more places like this one and meditate.\"}");
/* 261 */       buf.append("text{text=''}");
/* 262 */       buf.append("radio{ group='answer'; id='0';text='No';selected='true'}");
/* 263 */       buf.append("radio{ group='answer'; id='1';text='Yes'}");
/*     */     }
/*     */     else {
/*     */       
/* 267 */       buf.append("text{text=\"If " + Cults.getPathNameFor(this.cultist.getPath()) + " contains the truth or simply is the figment of someones imagination, you do not know.\"}");
/*     */       
/* 269 */       buf.append("text{text=\"Nonetheless, it poses an interesting challenge.\"}");
/* 270 */       buf.append("text{text=\"The following question springs to mind:\"}");
/* 271 */       buf.append("text{type='bold';text=\"" + Cults.getQuestionForLevel(this.path, this.cultist.getLevel()) + "\"}");
/*     */       
/* 273 */       buf.append("text{text=''}");
/* 274 */       String[] answers = Cults.getAnswerAlternativesForLevel(this.path, this.cultist.getLevel());
/* 275 */       for (int x = 0; x < answers.length; x++)
/*     */       {
/* 277 */         buf.append("radio{ group='answer'; id='" + x + "';text=\"" + answers[x] + "\"}");
/*     */       }
/* 279 */       buf.append("text{text=\"Know that you are part of the Cult of " + Cults.getPathNameFor(this.path) + " with secrets supposed to lead to enlightenment. Divulging those secrets may lead to expulsion.\"}");
/*     */       
/* 281 */       buf.append("text{text=''}");
/*     */     } 
/* 283 */     buf.append(createAnswerButton2());
/* 284 */     getResponder().getCommunicator().sendBml(width, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\CultQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
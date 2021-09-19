/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.villages.PvPAlliance;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.WarDeclaration;
/*     */ import com.wurmonline.server.zones.FocusZone;
/*     */ import java.util.Arrays;
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
/*     */ public final class ManageAllianceQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*     */   private static final String NOCHANGE = "No change";
/*  39 */   private Village[] allies = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManageAllianceQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  47 */     super(aResponder, aTitle, aQuestion, 19, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  56 */     setAnswer(answers);
/*  57 */     QuestionParser.parseManageAllianceQuestion(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Village[] getAllies() {
/*  62 */     return this.allies;
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
/*  76 */     Village village = getResponder().getCitizenVillage();
/*  77 */     if (village != null) {
/*     */       
/*  79 */       this.allies = village.getAllies();
/*     */       
/*  81 */       Arrays.sort((Object[])this.allies);
/*  82 */       StringBuilder buf = new StringBuilder();
/*  83 */       buf.append(getBmlHeader());
/*  84 */       PvPAlliance pvpAll = PvPAlliance.getPvPAlliance(village.getAllianceNumber());
/*  85 */       if (pvpAll != null) {
/*     */         
/*  87 */         buf.append("text{text=\"You are in the " + pvpAll.getName() + ".\"}");
/*  88 */         if (FocusZone.getHotaZone() != null)
/*     */         {
/*  90 */           buf.append("text{text=\"" + pvpAll.getName() + " has won the Hunt of the Ancients " + pvpAll
/*  91 */               .getNumberOfWins() + " times.\"}");
/*     */         }
/*     */ 
/*     */         
/*  95 */         if (village.getId() == village.getAllianceNumber()) {
/*     */           
/*  97 */           if (village.getMayor().getId() == getResponder().getWurmId()) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 102 */             buf.append("text{text=\"" + village
/* 103 */                 .getName() + " is the capital in the alliance which means your diplomats are responsible for ousting other settlements. The mayor may change name, disband or set another village as the alliance capital:\"};");
/*     */ 
/*     */ 
/*     */             
/* 107 */             buf.append("harray{label{text=\"Alliance name:\"};input{id=\"allName\"; text=\"" + pvpAll.getName() + "\";maxchars=\"20\"}}");
/*     */             
/* 109 */             buf.append("harray{label{text='Alliance capital:'}dropdown{id=\"masterVill\";options=\"");
/*     */             
/* 111 */             for (int x = 0; x < this.allies.length; x++)
/*     */             {
/* 113 */               buf.append(this.allies[x].getName() + ",");
/*     */             }
/*     */             
/* 116 */             buf.append("No change");
/* 117 */             buf.append("\";default=\"" + this.allies.length + "\"}}");
/* 118 */             buf.append("harray{checkbox{text=\"Check this if you wish to disband this alliance: \";id=\"disbandAll\"; selected=\"false\"}}");
/*     */           } 
/*     */ 
/*     */           
/* 122 */           for (Village ally : this.allies)
/*     */           {
/* 124 */             if (ally != village) {
/* 125 */               buf.append("harray{label{text=\"Check to break alliance with " + ally.getName() + ":\"}checkbox{id=\"break" + ally
/*     */                   
/* 127 */                   .getId() + "\";text=' '}}");
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
/*     */             }
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
/*     */           }
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
/*     */         }
/*     */         else {
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
/* 177 */           buf.append("harray{label{text=\"Check to break alliance with " + pvpAll.getName() + ":\"}checkbox{id=\"break" + pvpAll
/*     */               
/* 179 */               .getId() + "\";text=' '}}");
/*     */         } 
/* 181 */         buf.append("text{type=\"bold\";text=\"Alliance message of the day:\"}");
/* 182 */         buf.append("input{maxchars=\"200\";id=\"motd\";text=\"" + pvpAll.getMotd() + "\"}");
/*     */       } 
/* 184 */       if (this.allies.length == 0)
/* 185 */         buf.append("text{text='You have no allies.'}"); 
/* 186 */       buf.append("text{text=''}");
/* 187 */       buf.append("text{text=''}");
/* 188 */       if (village.warDeclarations != null) {
/*     */         
/* 190 */         buf.append("text{type='bold'; text='The current village war declarations:' }");
/* 191 */         for (WarDeclaration declaration : village.warDeclarations.values()) {
/*     */           
/* 193 */           if (declaration.declarer == village) {
/*     */             
/* 195 */             if (Servers.isThisAChaosServer() && 
/* 196 */               System.currentTimeMillis() - declaration.time > 86400000L) {
/*     */               
/* 198 */               declaration.accept();
/* 199 */               buf.append("harray{label{text=\"" + declaration.receiver.getName() + " has now automatically accepted your declaration.\"}}");
/*     */               
/*     */               continue;
/*     */             } 
/* 203 */             buf.append("harray{label{text=\"Check to withdraw declaration to " + declaration.receiver.getName() + ":\"}checkbox{id'decl" + declaration.receiver
/* 204 */                 .getId() + "';text=' '}}");
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 209 */           if (Servers.isThisAChaosServer()) {
/*     */             
/* 211 */             if (System.currentTimeMillis() - declaration.time < 86400000L) {
/*     */               
/* 213 */               buf.append("harray{label{text=\"You have " + 
/* 214 */                   Server.getTimeFor(System.currentTimeMillis() - declaration.time) + " until you automatically accept the declaration of war.\"}}");
/*     */ 
/*     */               
/* 217 */               buf.append("harray{label{text=\"Check to accept declaration from " + declaration.declarer
/* 218 */                   .getName() + ":\"}checkbox{id='recv" + declaration.declarer
/*     */                   
/* 220 */                   .getId() + "';text=' '}}");
/*     */               
/*     */               continue;
/*     */             } 
/* 224 */             declaration.accept();
/* 225 */             buf.append("harray{label{text=\"" + declaration.receiver.getName() + " has now automatically accepted the war declaration from " + declaration.declarer
/*     */                 
/* 227 */                 .getName() + ".\"}}");
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 232 */           buf.append("harray{label{text=\"Check to accept declaration from " + declaration.declarer.getName() + ":\"}checkbox{id='recv" + declaration.declarer
/*     */               
/* 234 */               .getId() + "';text=' '}}");
/*     */         } 
/*     */         
/* 237 */         buf.append("text{text=''}");
/* 238 */         buf.append("text{text=''}");
/*     */       }
/* 240 */       else if (Servers.localServer.PVPSERVER) {
/* 241 */         buf.append("text{text='You have no pending war declarations.'}");
/* 242 */       }  Village[] enemies = village.getEnemies();
/* 243 */       if (enemies.length > 0) {
/*     */         
/* 245 */         buf.append("harray{text{type='bold'; text='We are at war with: '}text{text=\" ");
/*     */         
/* 247 */         Arrays.sort((Object[])enemies);
/*     */         
/* 249 */         for (int x = 0; x < enemies.length; x++) {
/*     */           
/* 251 */           if (x == enemies.length - 1) {
/* 252 */             buf.append(enemies[x].getName());
/* 253 */           } else if (x == enemies.length - 2) {
/* 254 */             buf.append(enemies[x].getName() + " and ");
/*     */           } else {
/* 256 */             buf.append(enemies[x].getName() + ", ");
/*     */           } 
/* 258 */         }  buf.append(".\"}}");
/*     */       }
/* 260 */       else if (Servers.localServer.PVPSERVER) {
/* 261 */         buf.append("text{text='You are not at war with any particular settlement.'}");
/* 262 */       }  buf.append(createAnswerButton2());
/* 263 */       getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ManageAllianceQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
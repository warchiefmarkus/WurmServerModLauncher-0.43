/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.skills.SkillSystem;
/*     */ import com.wurmonline.server.skills.SkillTemplate;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ public class SkillProgressQuestion
/*     */   extends Question
/*     */ {
/*     */   private static final String progressDBString = "SELECT * FROM SKILLS WHERE OWNER=? AND NUMBER=?";
/*  47 */   private static final Logger logger = Logger.getLogger(SkillProgressQuestion.class.getName());
/*     */   boolean answering = false;
/*  49 */   String name = "";
/*  50 */   int skill = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SkillProgressQuestion(Creature aResponder, long wurmId, String _name) {
/*  60 */     super(aResponder, "Skill progress check", "Progress for " + _name + ":", 124, wurmId);
/*  61 */     this.name = _name;
/*  62 */     this.answering = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SkillProgressQuestion(Creature aResponder) {
/*  72 */     super(aResponder, "Skill progress check", "Select a player and skill to check latest progress", 124, -10L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  82 */     if (getResponder().getPower() > 1) {
/*     */       
/*  84 */       String player = answers.getProperty("data1");
/*     */       
/*  86 */       if (player != null && player.length() > 0) {
/*     */         
/*  88 */         getResponder().getLogger().log(Level.INFO, 
/*  89 */             getResponder().getName() + " checking " + player + " for skill progress.");
/*  90 */         logger.log(Level.INFO, getResponder().getName() + " checking " + player + " for skill progress.");
/*  91 */         player = LoginHandler.raiseFirstLetter(player);
/*  92 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithName(player);
/*  93 */         if (pinf == null) {
/*     */           
/*  95 */           getResponder().getCommunicator().sendAlertServerMessage("No player found with name " + player + ".");
/*     */           return;
/*     */         } 
/*  98 */         String sknums = answers.getProperty("data2");
/*  99 */         if (sknums != null && sknums.length() > 0) {
/*     */ 
/*     */           
/*     */           try {
/* 103 */             int sknum = Integer.parseInt(sknums);
/* 104 */             Collection<SkillTemplate> temps = SkillSystem.templates.values();
/* 105 */             SkillTemplate[] templates = temps.<SkillTemplate>toArray(new SkillTemplate[temps.size()]);
/*     */             
/* 107 */             Arrays.sort((Object[])templates);
/*     */             
/* 109 */             int sk = templates[sknum].getNumber();
/* 110 */             SkillProgressQuestion newq = new SkillProgressQuestion(getResponder(), pinf.wurmId, pinf.getName());
/* 111 */             newq.skill = sk;
/* 112 */             newq.sendQuestion();
/*     */           }
/* 114 */           catch (Exception ex) {
/*     */             
/* 116 */             getResponder().getCommunicator().sendAlertServerMessage("No skill found in array at " + sknums + ".");
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } else {
/* 122 */           getResponder().getCommunicator().sendAlertServerMessage("No skill found in array.");
/*     */           return;
/*     */         } 
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
/* 136 */     StringBuilder buf = new StringBuilder();
/* 137 */     buf.append(getBmlHeader());
/* 138 */     if (!this.answering) {
/*     */       
/* 140 */       buf.append("harray{label{text='Player name'};input{maxchars='40';id='data1'; text=''}}");
/* 141 */       buf.append("harray{label{text='Skill to check'}dropdown{id='data2';options='");
/* 142 */       Collection<SkillTemplate> temps = SkillSystem.templates.values();
/* 143 */       SkillTemplate[] templates = temps.<SkillTemplate>toArray(new SkillTemplate[temps.size()]);
/*     */       
/* 145 */       Arrays.sort((Object[])templates);
/* 146 */       for (int x = 0; x < templates.length; x++) {
/*     */         
/* 148 */         if (x > 0)
/* 149 */           buf.append(","); 
/* 150 */         buf.append(templates[x].getName());
/*     */       } 
/*     */       
/* 153 */       buf.append("'}}");
/*     */     }
/*     */     else {
/*     */       
/* 157 */       Connection dbcon = null;
/* 158 */       PreparedStatement ps = null;
/* 159 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 162 */         dbcon = DbConnector.getPlayerDbCon();
/* 163 */         ps = dbcon.prepareStatement("SELECT * FROM SKILLS WHERE OWNER=? AND NUMBER=?");
/* 164 */         ps.setLong(1, getTarget());
/* 165 */         ps.setInt(2, this.skill);
/* 166 */         rs = ps.executeQuery();
/* 167 */         while (rs.next())
/*     */         {
/* 169 */           String skname = SkillSystem.getNameFor(this.skill);
/* 170 */           buf.append("harray{label{type=\"bolditalic\";text=\"" + skname + "\"}}");
/* 171 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(getTarget());
/* 172 */           if (pinf != null && pinf.currentServer != Servers.localServer.id) {
/* 173 */             buf.append("text=\"" + pinf.getName() + " does not seem to currently be on this server!\"}");
/*     */           }
/* 175 */           buf.append("harray{label{text='Current value:'}label{text=\"" + 
/* 176 */               String.valueOf(rs.getFloat("VALUE")) + "\"}}");
/* 177 */           buf.append("harray{label{text='1 day ago:'};label{text=\"" + String.valueOf(rs.getFloat("DAY1")) + "\"}}");
/*     */           
/* 179 */           buf.append("harray{label{text='2 days ago:'};label{text=\"" + String.valueOf(rs.getFloat("DAY2")) + "\"}}");
/*     */           
/* 181 */           buf.append("harray{label{text='3 days ago:'};label{text=\"" + String.valueOf(rs.getFloat("DAY3")) + "\"}}");
/*     */           
/* 183 */           buf.append("harray{label{text='4 days ago:'};label{text=\"" + String.valueOf(rs.getFloat("DAY4")) + "\"}}");
/*     */           
/* 185 */           buf.append("harray{label{text='5 days ago:'};label{text=\"" + String.valueOf(rs.getFloat("DAY5")) + "\"}}");
/*     */           
/* 187 */           buf.append("harray{label{text='6 days ago:'};label{text=\"" + String.valueOf(rs.getFloat("DAY6")) + "\"}}");
/*     */           
/* 189 */           buf.append("harray{label{text='7 days ago:'};label{text=\"" + String.valueOf(rs.getFloat("DAY7")) + "\"}}");
/*     */           
/* 191 */           buf.append("harray{label{text='2 weeks ago:'};label{text=\"" + String.valueOf(rs.getFloat("WEEK2")) + "\"}}");
/*     */           
/* 193 */           buf.append("text{text=\"\"}");
/* 194 */           buf.append("text{type=\"bolditalic\";text=\"Note that a 0 value usually means no change for the period or that the player was inactive.\"}");
/*     */         }
/*     */       
/* 197 */       } catch (SQLException ex) {
/*     */         
/* 199 */         logger.log(Level.WARNING, "Failed to show skill " + this.skill + " for " + this.name + " " + ex.getMessage(), new Exception());
/*     */         
/* 201 */         getResponder().getCommunicator().sendAlertServerMessage("Error when checking skill.");
/*     */ 
/*     */         
/*     */         return;
/*     */       } finally {
/* 206 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 207 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 210 */     buf.append(createAnswerButton2());
/* 211 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SkillProgressQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
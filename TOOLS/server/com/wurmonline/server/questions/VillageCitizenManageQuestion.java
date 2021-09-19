/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.players.PlayerState;
/*     */ import com.wurmonline.server.villages.Citizen;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageRole;
/*     */ import com.wurmonline.server.villages.VillageStatus;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.shared.constants.PlayerOnlineStatus;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
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
/*     */ public final class VillageCitizenManageQuestion
/*     */   extends Question
/*     */   implements VillageStatus
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(VillageCitizenManageQuestion.class.getName());
/*     */   
/*     */   private boolean selecting = false;
/*  53 */   private String allowedLetters = "abcdefghijklmnopqrstuvwxyz";
/*  54 */   private final Map<Integer, Long> idMap = new HashMap<>();
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
/*     */   public VillageCitizenManageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  67 */     super(aResponder, aTitle, aQuestion, 8, aTarget);
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
/*     */   public VillageCitizenManageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, boolean aSelecting) {
/*  81 */     super(aResponder, aTitle, aQuestion, 8, aTarget);
/*  82 */     this.selecting = aSelecting;
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
/*  93 */     setAnswer(answers);
/*  94 */     QuestionParser.parseVillageCitizenManageQuestion(this);
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
/*     */   public void sendQuestion() {
/*     */     try {
/*     */       Village village;
/* 109 */       if (this.target == -10L) {
/*     */         
/* 111 */         village = getResponder().getCitizenVillage();
/* 112 */         if (village == null) {
/* 113 */           throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*     */         }
/*     */       } else {
/*     */         
/* 117 */         Item deed = Items.getItem(this.target);
/* 118 */         int villageId = deed.getData2();
/* 119 */         village = Villages.getVillage(villageId);
/*     */       } 
/* 121 */       StringBuilder buf = new StringBuilder(getBmlHeader());
/* 122 */       buf.append("text{text=\"Manage citizens. Assign them roles and titles.\"}");
/* 123 */       Citizen[] citizens = village.getCitizens();
/* 124 */       String ch = village.unlimitedCitizens ? ";selected='true'" : "";
/* 125 */       buf.append("text{type=\"bold\";text=\"Unlimited citizens:\"}");
/* 126 */       if (!Servers.isThisAPvpServer())
/* 127 */         buf.append("text{type=\"italic\";text=\"The maximum number of branded animals is " + village.getMaxCitizens() + "\"}"); 
/* 128 */       buf.append("checkbox{id=\"unlimitC\"" + ch + ";text=\"Mark this if you want to be able to recruit more than " + village.getMaxCitizens() + " citizens.\"}text{text=\"Your upkeep costs are doubled as long as you have more than that amount of citizens.\"}");
/*     */ 
/*     */       
/* 131 */       if (this.selecting) {
/*     */         
/* 133 */         if (citizens.length > 40) {
/*     */           
/* 135 */           buf.append("label{text=\"Select the range of citizens to manage:\"}");
/* 136 */           buf.append("dropdown{id=\"selectRange\";options=\"A-F,G-L,M-R,S-Z\"}");
/* 137 */           buf.append(createAnswerButton2());
/* 138 */           getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */           
/*     */           return;
/*     */         } 
/* 142 */         this.selecting = false;
/*     */       } 
/* 144 */       buf.append("text{text=\"\"}");
/*     */       
/* 146 */       VillageRole[] roles = village.getRoles();
/*     */ 
/*     */       
/* 149 */       Arrays.sort((Object[])citizens);
/*     */       
/* 151 */       buf.append("table{rows=\"" + (citizens.length - 1) + "\";cols=\"7\";");
/* 152 */       for (int x = 0; x < citizens.length; x++) {
/*     */         
/* 154 */         if (this.allowedLetters.indexOf(citizens[x].getName().substring(0, 1).toLowerCase()) >= 0) {
/*     */           
/* 156 */           this.idMap.put(Integer.valueOf(x), new Long(citizens[x].getId()));
/* 157 */           VillageRole role = citizens[x].getRole();
/* 158 */           int roleid = role.getId();
/* 159 */           if (role.getStatus() != 4 && role.getStatus() != 5 && role.getStatus() != 6) {
/*     */             
/* 161 */             int defaultrole = 0;
/* 162 */             buf.append("label{text=\"" + citizens[x].getName() + "\"}label{text=\"role:\"}dropdown{id=\"" + x + "\";options=\"");
/*     */ 
/*     */             
/* 165 */             int added = 0;
/* 166 */             for (int r = 0; r < roles.length; r++) {
/*     */               
/* 168 */               if (roles[r].getStatus() != 4 && roles[r].getStatus() != 5 && roles[r]
/* 169 */                 .getStatus() != 1 && roles[r]
/* 170 */                 .getStatus() != 6) {
/*     */                 
/* 172 */                 if (added > 0 && r != roles.length)
/* 173 */                   buf.append(","); 
/* 174 */                 String name = roles[r].getName();
/* 175 */                 if (name.length() == 0)
/* 176 */                   name = "[blank]"; 
/* 177 */                 buf.append(name.substring(0, Math.min(name.length(), 10)));
/*     */                 
/* 179 */                 if (roleid == roles[r].getId())
/* 180 */                   defaultrole = added; 
/* 181 */                 added++;
/*     */               } 
/*     */             } 
/* 184 */             buf.append("\";default=\"" + defaultrole + "\"}");
/*     */ 
/*     */             
/* 187 */             PlayerState cState = PlayerInfoFactory.getPlayerState(citizens[x].getId());
/* 188 */             if (cState == null) {
/*     */               
/* 190 */               buf.append("label{text=\"\"}");
/* 191 */               buf.append("label{text=\"\"}");
/* 192 */               buf.append("label{text=\"\"}");
/*     */             }
/*     */             else {
/*     */               
/* 196 */               String sColour = "";
/* 197 */               String sState = "";
/* 198 */               long changedDate = 0L;
/* 199 */               if (cState.getState() == PlayerOnlineStatus.ONLINE) {
/*     */                 
/* 201 */                 sColour = "66,225,66";
/* 202 */                 sState = "Online";
/* 203 */                 changedDate = cState.getLastLogin();
/*     */               }
/*     */               else {
/*     */                 
/* 207 */                 sColour = "255,66,66";
/* 208 */                 sState = "Offline";
/* 209 */                 changedDate = cState.getLastLogout();
/*     */               } 
/* 211 */               buf.append("label{color=\"" + sColour + "\";text=\"" + sState + "\"}");
/* 212 */               buf.append("label{text=\"" + convertTime(changedDate) + "\"}");
/* 213 */               buf.append("label{text=\"" + cState.getServerName() + "\"}");
/*     */             } 
/*     */             
/* 216 */             if (!village.isDemocracy() && citizens[x].getId() != getResponder().getWurmId() && citizens[x]
/* 217 */               .getRole().getStatus() != 2) {
/* 218 */               buf.append("checkbox{id=\"" + x + "revoke\";selected=\"false\";text=\" Revoke citizenship \"}");
/*     */             } else {
/* 220 */               buf.append("label{text=\"\"}");
/*     */             }
/*     */           
/* 223 */           } else if (citizens[x].getRole().getStatus() == 6) {
/*     */             
/* 225 */             Wagoner wagoner = Wagoner.getWagoner(citizens[x].getId());
/* 226 */             buf.append("label{text=\"" + citizens[x].getName() + "\"}label{text=\"role:\"};label{text=\"" + citizens[x]
/*     */                 
/* 228 */                 .getRole().getName() + "\"};label{text=\"" + wagoner
/* 229 */                 .getStateName() + "\"};label{text=\"\"};label{text=\"\"};label{text=\"\"}");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 237 */       buf.append("}text{text=\"\"}");
/* 238 */       buf.append(createAnswerButton2());
/* 239 */       getResponder().getCommunicator().sendBml(500, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 241 */     catch (NoSuchItemException nsi) {
/*     */       
/* 243 */       logger.log(Level.WARNING, "Failed to locate village/homestead deed with id " + this.target, (Throwable)nsi);
/* 244 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*     */     
/*     */     }
/* 247 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 249 */       logger.log(Level.WARNING, "Failed to locate the village/homestead for the deed with id " + this.target, (Throwable)nsv);
/* 250 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the village for that deed. Please contact administration.");
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
/*     */   boolean isSelecting() {
/* 262 */     return this.selecting;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelecting(boolean aSelecting) {
/* 272 */     this.selecting = aSelecting;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setAllowedLetters(String aAllowedLetters) {
/* 282 */     this.allowedLetters = aAllowedLetters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Map<Integer, Long> getIdMap() {
/* 292 */     return this.idMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String convertTime(long time) {
/* 297 */     String fd = (new SimpleDateFormat("dd/MMM/yyyy HH:mm")).format(new Date(time));
/* 298 */     return fd;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageCitizenManageQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
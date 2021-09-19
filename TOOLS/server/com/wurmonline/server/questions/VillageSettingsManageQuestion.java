/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageStatus;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ public final class VillageSettingsManageQuestion
/*     */   extends Question
/*     */   implements VillageStatus, TimeConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(VillageSettingsManageQuestion.class.getName());
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
/*     */   public VillageSettingsManageQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  58 */     super(aResponder, aTitle, aQuestion, 10, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  67 */     setAnswer(answers);
/*  68 */     QuestionParser.parseVillageSettingsManageQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*     */     try {
/*     */       Village village;
/*  78 */       if (this.target == -10L) {
/*     */         
/*  80 */         village = getResponder().getCitizenVillage();
/*  81 */         if (village == null) {
/*  82 */           throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*     */         }
/*     */       } else {
/*     */         
/*  86 */         Item deed = Items.getItem(this.target);
/*  87 */         int villageId = deed.getData2();
/*  88 */         village = Villages.getVillage(villageId);
/*     */       } 
/*     */       
/*  91 */       StringBuilder buf = new StringBuilder(getBmlHeaderWithScroll());
/*     */       
/*  93 */       if (village.isDisbanding()) {
/*     */         
/*  95 */         long timeleft = village.getDisbanding() - System.currentTimeMillis();
/*  96 */         String times = Server.getTimeFor(timeleft);
/*  97 */         buf.append("text{type='bold';text='This village is disbanding'}");
/*     */         
/*  99 */         if (timeleft > 0L) {
/* 100 */           buf.append("text{type='bold';text=\"Eta: " + times + ".\"};text{text=''};");
/*     */         } else {
/* 102 */           buf.append("text{type='bold';text='Eta:  any minute now.'};text{text=''};");
/*     */         } 
/*     */       } 
/*     */       
/* 106 */       if (!village.isDisbanding() && village
/* 107 */         .mayChangeName() && village
/* 108 */         .getRoleFor(getResponder()).mayResizeSettlement()) {
/*     */         
/* 110 */         buf.append("header{text=\"Settlement Name:\"};input{maxchars=\"40\";id=\"vname\";text=\"" + village
/* 111 */             .getName() + "\"}");
/* 112 */         buf.append("text{type=\"bold\";color=\"255,50,0\";text=\"NOTE: Changing the name will" + (
/* 113 */             Servers.localServer.isFreeDeeds() ? "" : " cost 5 silver,") + " remove all the faith bonuses, and lock the name for 6 months.\"}");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 118 */         buf.append("header{text=\"Settlement Name\"};text{text=\"" + village
/* 119 */             .getName() + "\"}");
/*     */         
/* 121 */         buf.append("passthrough{id=\"vname\";text=\"" + village.getName() + "\"}");
/*     */       } 
/*     */       
/* 124 */       buf.append("header{text=\"Settlement motto:\"}");
/* 125 */       buf.append("input{maxchars=\"100\";id=\"motto\"; text=\"" + village.getMotto() + "\"}");
/* 126 */       buf.append("header{text=\"Settlement message of the day:\"}");
/* 127 */       buf.append("input{maxchars=\"200\";id=\"motd\"; text=\"" + village.getMotd() + "\"}");
/*     */       
/* 129 */       buf.append("text{type=\"bold\";text=\"Politics:\"}");
/* 130 */       if ((village.isDemocracy() && village.getFounderName().equals(getResponder().getName()) && village
/* 131 */         .getMayor().getId() == getResponder().getWurmId()) || 
/* 132 */         getResponder().getPower() >= 4) {
/* 133 */         buf.append("checkbox{id=\"nondemocracy\";text=\"Mark this if you want to make this settlement a non-democracy so you cannot be removed from office: \"}");
/*     */       } else {
/* 135 */         buf.append("checkbox{id=\"democracy\";text=\"Mark this if you want to make this settlement a permanent democracy (founding mayors can revert):\"}");
/* 136 */       }  String ch = village.allowsAggCreatures() ? ";selected='true'" : "";
/* 137 */       buf.append("text{type=\"bold\";text=\"Aggressive creatures:\"}");
/* 138 */       buf.append("checkbox{id=\"aggros\"" + ch + ";text=\"Mark this if you want guards to ignore aggressive creatures\"}");
/*     */       
/* 140 */       ch = village.unlimitedCitizens ? ";selected='true'" : "";
/* 141 */       buf.append("text{type=\"bold\";text=\"Unlimited citizens:\"}");
/* 142 */       buf.append("checkbox{id=\"unlimitC\"" + ch + ";text=\"Mark this if you want to be able to recruit more than " + village
/* 143 */           .getMaxCitizens() + " citizens.\"}label{text=\"Your upkeep costs are doubled as long as you have more than that amount of citizens.\"}");
/*     */       
/* 145 */       if (Features.Feature.HIGHWAYS.isEnabled()) {
/*     */         
/* 147 */         boolean hasHighway = village.hasHighway();
/* 148 */         boolean hasKOS = ((village.getReputations()).length > 0);
/* 149 */         String disable = ";enabled=\"false\"";
/* 150 */         String hover = "";
/* 151 */         String en = "";
/* 152 */         ch = "";
/*     */         
/* 154 */         if (village.isHighwayAllowed()) {
/*     */           
/* 156 */           ch = ";selected='true'";
/*     */           
/* 158 */           if (hasHighway)
/*     */           {
/*     */             
/* 161 */             en = ";enabled=\"false\"";
/* 162 */             hover = ";hover=\"Cannot disable as a highway marker is in or next to the village.\"";
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/* 168 */         else if (hasKOS) {
/*     */           
/* 170 */           en = ";enabled=\"false\"";
/* 171 */           hover = ";hover=\"Cannot enable as there is an active kos.\"";
/*     */         } 
/*     */         
/* 174 */         buf.append("text{type=\"bold\";text=\"Allow Highways:\"}");
/* 175 */         buf.append("checkbox{id=\"highways\"" + ch + en + hover + ";text=\"Mark this if you want citizens to be able to make a highway to your village.\"}");
/*     */ 
/*     */         
/* 178 */         hover = "";
/* 179 */         en = "";
/* 180 */         ch = "";
/*     */         
/* 182 */         if (village.isKosAllowed()) {
/*     */           
/* 184 */           ch = ";selected='true'";
/*     */           
/* 186 */           if (hasKOS)
/*     */           {
/* 188 */             en = ";enabled=\"false\"";
/* 189 */             hover = ";hover=\"Cannot disable as there is an active kos.\"";
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/* 195 */         else if (hasHighway) {
/*     */           
/* 197 */           en = ";enabled=\"false\"";
/* 198 */           hover = ";hover=\"Cannot enable as there is a highway marker in or right next to the village.\"";
/*     */         } 
/*     */         
/* 201 */         buf.append("text{type=\"bold\";text=\"Allow KOS:\"}");
/* 202 */         buf.append("checkbox{id=\"kos\"" + ch + en + hover + ";text=\"Mark this if you want to be able to use  KOS.\"}");
/*     */         
/* 204 */         buf.append("label{text=\"Note: Allow highways and Allow KOS cannot be both enabled.\"}");
/*     */         
/* 206 */         hover = "";
/* 207 */         en = "";
/* 208 */         ch = "";
/*     */         
/* 210 */         if (village.isHighwayFound()) {
/*     */           
/* 212 */           ch = ";selected='true'";
/*     */           
/* 214 */           if (village.isPermanent)
/*     */           {
/* 216 */             en = ";enabled=\"false\"";
/* 217 */             hover = ";hover=\"Cannot disable as this is a permanent village.\"";
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/* 223 */         else if (hasKOS) {
/*     */           
/* 225 */           en = ";enabled=\"false\"";
/* 226 */           hover = ";hover=\"Cannot enable as there is an active kos.\"";
/*     */         } 
/*     */         
/* 229 */         buf.append("text{type=\"bold\";text=\"Highway Routing:\"}");
/* 230 */         buf.append("checkbox{id=\"routing\"" + ch + en + hover + ";text=\"Mark this if you want your village to show in the find route village list.\";hover=\"It will only show in the list if there is a route to your village.\"}");
/*     */ 
/*     */         
/* 233 */         buf.append("label{text=\"Note: This will only be available if Allow Highways is enabled.\"}");
/* 234 */         buf.append("label{text=\"Note: They will only be able to find a route here if you are part of the (same) highway network.\"}");
/*     */       } 
/* 236 */       buf.append("text{text=\"\"}");
/* 237 */       buf.append(createAnswerButton3());
/* 238 */       getResponder().getCommunicator().sendBml(535, 430, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 240 */     catch (NoSuchItemException nsi) {
/*     */       
/* 242 */       logger.log(Level.WARNING, "Failed to locate village deed with id " + this.target, (Throwable)nsi);
/* 243 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the deed item for that request. Please contact administration.");
/*     */     
/*     */     }
/* 246 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 248 */       logger.log(Level.WARNING, "Failed to locate village for deed with id " + this.target, (Throwable)nsv);
/* 249 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the village for that request. Please contact administration.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageSettingsManageQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
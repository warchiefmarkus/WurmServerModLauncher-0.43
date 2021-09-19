/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.villages.AllianceWar;
/*     */ import com.wurmonline.server.villages.Citizen;
/*     */ import com.wurmonline.server.villages.NoSuchRoleException;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.PvPAlliance;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageRole;
/*     */ import com.wurmonline.server.villages.VillageStatus;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.villages.WarDeclaration;
/*     */ import com.wurmonline.server.zones.FocusZone;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ public final class VillageInfo
/*     */   extends Question
/*     */   implements VillageStatus, TimeConstants
/*     */ {
/*  56 */   private static final Logger logger = Logger.getLogger(VillageInfo.class.getName());
/*  57 */   private static final NumberFormat nf = NumberFormat.getInstance();
/*  58 */   private VillageRole playerRole = null;
/*     */ 
/*     */   
/*     */   public VillageInfo(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  62 */     super(aResponder, aTitle, aQuestion, 14, aTarget);
/*  63 */     nf.setMaximumFractionDigits(6);
/*     */   }
/*     */ 
/*     */   
/*     */   public VillageInfo(Creature aResponder, VillageRole vRole) {
/*  68 */     super(aResponder, "", "", 14, -10L);
/*  69 */     this.playerRole = vRole;
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
/*  80 */     setAnswer(answers);
/*     */     
/*  82 */     if (Boolean.parseBoolean(getAnswer().getProperty("showPlayerRole"))) {
/*     */       
/*  84 */       VillageInfo vi = new VillageInfo(getResponder(), this.playerRole);
/*  85 */       vi.sendQuestion();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  92 */     if (this.playerRole != null) {
/*     */       
/*  94 */       VillageRolesManageQuestion.roleShow(getResponder(), getId(), null, this.playerRole, "");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*     */       Village village;
/* 101 */       if (this.target == -10L) {
/*     */         
/* 103 */         village = getResponder().getCitizenVillage();
/* 104 */         if (village == null) {
/* 105 */           throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*     */         }
/*     */       } else {
/*     */         
/* 109 */         Item deed = Items.getItem(this.target);
/* 110 */         int villageId = deed.getData2();
/* 111 */         village = Villages.getVillage(villageId);
/*     */       } 
/*     */       
/* 114 */       if (village.getMayor() != null && village.getMayor().getId() == getResponder().getWurmId()) {
/*     */         
/*     */         try {
/*     */           
/* 118 */           Item deed = Items.getItem(village.getDeedId());
/* 119 */           if (deed.getOwnerId() < 0L)
/*     */           {
/* 121 */             logger.log(Level.INFO, 
/* 122 */                 getResponder().getName() + " retrieving and inserting deed " + village.getDeedId() + " for " + village
/* 123 */                 .getName() + ".");
/* 124 */             deed.setTransferred(false);
/* 125 */             deed.setMailed(false);
/* 126 */             getResponder().getInventory().insertItem(deed);
/* 127 */             getResponder().getCommunicator().sendNormalServerMessage("You have retrieved your settlement deed.");
/*     */           }
/*     */         
/* 130 */         } catch (NoSuchItemException iox) {
/*     */           
/* 132 */           logger.log(Level.WARNING, "No deed available for " + village.getName() + ". Creating new. Exception was " + iox
/* 133 */               .getMessage(), (Throwable)iox);
/* 134 */           village.replaceNoDeed(getResponder());
/* 135 */           getResponder().getCommunicator().sendNormalServerMessage("You have received a new settlement deed.");
/*     */         } 
/*     */       }
/*     */       
/* 139 */       StringBuilder buf = new StringBuilder();
/* 140 */       buf.append(getBmlHeader());
/* 141 */       buf.append("header{text=\"" + village.getName() + "\"}");
/* 142 */       buf.append("text{type=\"italic\";text=\"" + village.getMotto() + "\"};text{text=\"\"}");
/* 143 */       if (village.isCapital()) {
/* 144 */         buf.append("text{type=\"bold\";text=\"Welcome to the capital of " + Kingdoms.getNameFor(village.kingdom) + "!\"};text{text=\"\"}");
/*     */       }
/* 146 */       if (village.isDisbanding()) {
/*     */         
/* 148 */         long timeleft = village.getDisbanding() - System.currentTimeMillis();
/* 149 */         String times = Server.getTimeFor(timeleft);
/* 150 */         buf.append("text{type=\"bold\";text=\"This settlement is disbanding\"}");
/* 151 */         if (timeleft > 0L) {
/* 152 */           buf.append("text{type=\"bold\";text=\"Eta: " + times + ".\"};text{text=\"\"}");
/*     */         } else {
/* 154 */           buf.append("text{type=\"bold\";text=\"Eta: any minute now.\"};text{text=\"\"}");
/*     */         } 
/* 156 */       }  if (village.isCitizen(getResponder()) || getResponder().getPower() >= 2) {
/*     */         
/* 158 */         buf.append("text{text=\"The size of " + village.getName() + " is " + village.getDiameterX() + " by " + village
/* 159 */             .getDiameterY() + ".\"}");
/* 160 */         buf.append("text{text=\"The perimeter is " + (5 + village.getPerimeterSize()) + " and it has " + village.plan
/* 161 */             .getNumHiredGuards() + " guards hired.\"}");
/* 162 */         if (Servers.localServer.testServer)
/* 163 */           buf.append("text{text='[TEST] Number of current guards in guardPlan: " + (village.getGuards()).length + "'}"); 
/* 164 */         long money = village.plan.getMoneyLeft();
/* 165 */         Change ca = new Change(money);
/*     */         
/* 167 */         if (Servers.localServer.isUpkeep()) {
/*     */           
/* 169 */           buf.append("text{text=\"The settlement has " + ca.getChangeString() + " in its coffers.\"}");
/* 170 */           if (getResponder().getPower() >= 2) {
/*     */             
/* 172 */             logger.log(Level.INFO, getResponder().getName() + " checking " + village.getName() + " financial info.");
/* 173 */             getResponder().getLogger().log(Level.INFO, 
/* 174 */                 getResponder().getName() + " checking " + village.getName() + " financial info.");
/* 175 */             long moneyTick = (long)village.plan.calculateUpkeep(false);
/* 176 */             Change cu = new Change(moneyTick);
/* 177 */             buf.append("text{text=\"Every tick (~8 mins) will drain " + cu.getChangeString() + ".\"}");
/*     */           } 
/* 179 */           long monthly = village.plan.getMonthlyCost();
/* 180 */           Change c = new Change(monthly);
/* 181 */           buf.append("text{text=\"The monthly cost is " + c.getChangeString() + ".\"}");
/* 182 */           long timeleft = village.plan.getTimeLeft();
/* 183 */           buf.append("text{text=\"The upkeep will last approximately " + Server.getTimeFor(timeleft) + " more.\"}");
/*     */         } 
/* 185 */         buf.append("text{text=\"\"}");
/* 186 */         buf.append("text{text=\"The settlement is granted the following faith bonuses:\"}");
/* 187 */         buf.append("text{text=\"War (" + nf.format(village.getFaithWarValue()) + ") damage: " + nf
/* 188 */             .format(village.getFaithWarBonus()) + "% CR: " + nf
/* 189 */             .format(village.getFaithWarBonus()) + "%, Healing (" + nf.format(village.getFaithHealValue()) + "): " + nf
/*     */             
/* 191 */             .format(village.getFaithHealBonus()) + "%, Enchanting (" + nf
/* 192 */             .format(village.getFaithCreateValue()) + "): " + nf
/*     */             
/* 194 */             .format(village.getFaithCreateBonus()) + "%, Rarity window: " + 
/* 195 */             (int)Math.min(10.0F, village.getFaithCreateValue()) + " bonus seconds\"}");
/*     */         
/* 197 */         buf.append("text{text=\"These bonuses will decrease by 15% per day.\"}");
/* 198 */         buf.append("text{text=\"\"}");
/* 199 */         float ratio = village.getCreatureRatio();
/* 200 */         buf.append("text{text=\"The tile per creature ratio of this deed is " + ratio + ". Optimal is " + Village.OPTIMUMCRETRATIO + " or more.");
/* 201 */         if (ratio < Village.OPTIMUMCRETRATIO) {
/* 202 */           buf.append(" This means that you will see more disease and miscarriage.");
/*     */         } else {
/* 204 */           buf.append(" This is a good figure.");
/* 205 */         }  int brandedCreatures = (Creatures.getInstance().getBranded(village.getId())).length;
/* 206 */         if (brandedCreatures > 1) {
/* 207 */           buf.append(String.format(" There are %d creatures currently branded.", new Object[] { Integer.valueOf(brandedCreatures) }));
/* 208 */         } else if (brandedCreatures == 1) {
/* 209 */           buf.append(String.format(" There is %d creature currently branded.", new Object[] { Integer.valueOf(brandedCreatures) }));
/* 210 */         }  buf.append("\"};text{text=\"\"}");
/* 211 */         if (village.isDemocracy()) {
/* 212 */           buf.append("text{text=\"" + village
/* 213 */               .getName() + " is a democracy. This means your citizenship cannot be revoked by any city officials such as the mayor. \"}");
/*     */         } else {
/*     */           
/* 216 */           buf.append("text{text=\"" + village
/* 217 */               .getName() + " is a non-democracy. This means your citizenship can be revoked by any city officials such as the mayor. \"}");
/*     */         } 
/* 219 */         buf.append("");
/* 220 */         buf.append("text{text=\"\"}");
/*     */       } 
/* 222 */       String visitor = "Visitor";
/*     */       
/* 224 */       this.playerRole = village.getRoleForPlayer(getResponder().getWurmId());
/* 225 */       if (this.playerRole != null) {
/*     */         
/* 227 */         if (village.isCitizen(getResponder())) {
/* 228 */           visitor = this.playerRole.getName() + " of " + village.getName();
/*     */         } else {
/* 230 */           visitor = "Individual (" + this.playerRole.getName() + ") role";
/*     */         } 
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 236 */           if (getResponder().getCitizenVillage() == null) {
/*     */ 
/*     */             
/* 239 */             visitor = "visitor";
/* 240 */             this.playerRole = village.getRoleForStatus((byte)1);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 245 */             this.playerRole = village.getRoleForVillage(getResponder().getCitizenVillage().getId());
/* 246 */             if (this.playerRole != null)
/*     */             {
/* 248 */               visitor = "Citizen of " + getResponder().getCitizenVillage().getName();
/*     */             }
/* 250 */             else if (getResponder().getCitizenVillage().isAlly(village))
/*     */             {
/* 252 */               visitor = "Ally";
/* 253 */               this.playerRole = village.getRoleForStatus((byte)5);
/*     */             }
/*     */             else
/*     */             {
/* 257 */               visitor = "visitor";
/* 258 */               this.playerRole = village.getRoleForStatus((byte)1);
/*     */             }
/*     */           
/*     */           } 
/* 262 */         } catch (NoSuchRoleException e) {
/*     */ 
/*     */           
/* 265 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 266 */           visitor = "problem";
/*     */         } 
/*     */       } 
/*     */       
/* 270 */       buf.append("harray{button{text=\"Show role for " + visitor + "\";id=\"showPlayerRole\"}}");
/* 271 */       buf.append("text{text=\"\"}");
/*     */       
/* 273 */       if (FocusZone.getHotaZone() != null) {
/*     */         
/* 275 */         buf.append("text{text=\"" + village.getName() + " has won the Hunt of the Ancients " + village
/* 276 */             .getHotaWins() + " times.\"}");
/*     */         
/* 278 */         if (Servers.localServer.getNextHota() == Long.MAX_VALUE) {
/* 279 */           buf.append("text{text=\"The Hunt of the Ancients is afoot!\"}");
/*     */         } else {
/*     */           
/* 282 */           long timeLeft = Servers.localServer.getNextHota() - System.currentTimeMillis();
/* 283 */           buf.append("text{text=\"The next Hunt of the Ancients is in " + Server.getTimeFor(timeLeft) + ".\"}");
/*     */         } 
/*     */       } 
/*     */       
/* 287 */       Village[] allies = village.getAllies();
/* 288 */       if (allies.length > 0) {
/*     */         
/* 290 */         PvPAlliance alliance = PvPAlliance.getPvPAlliance(village.getAllianceNumber());
/* 291 */         if (alliance != null) {
/*     */           
/* 293 */           Village capital = alliance.getAllianceCapital();
/* 294 */           buf.append("text{text=\"We are in the " + alliance.getName() + ". ");
/* 295 */           buf.append("The capital is " + capital.getName() + ".\"}");
/* 296 */           if (FocusZone.getHotaZone() != null)
/*     */           {
/* 298 */             buf.append("text{text=\"" + alliance.getName() + " has won the Hunt of the Ancients " + alliance
/* 299 */                 .getNumberOfWins() + " times.\"}");
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 304 */         buf.append("label{text=\"The alliance consists of: \"};text{text=\"");
/*     */         
/* 306 */         Arrays.sort((Object[])allies);
/*     */         
/* 308 */         for (int x = 0; x < allies.length; x++) {
/*     */           
/* 310 */           if (x == allies.length - 1) {
/* 311 */             buf.append(allies[x].getName());
/* 312 */           } else if (x == allies.length - 2) {
/* 313 */             buf.append(allies[x].getName() + " and ");
/*     */           } else {
/* 315 */             buf.append(allies[x].getName() + ", ");
/*     */           } 
/* 317 */         }  buf.append(".\"}");
/* 318 */         buf.append("text{text=\"\"}");
/* 319 */         if (alliance != null) {
/*     */           
/* 321 */           AllianceWar[] wars = alliance.getWars();
/* 322 */           if (wars.length > 0) {
/*     */             
/* 324 */             buf.append("text{type=\"bold\";text=\"We are at war with the following alliances: \"};text{text=\"");
/*     */             
/* 326 */             for (int i = 0; i < wars.length; i++) {
/*     */               
/* 328 */               PvPAlliance enemy = null;
/* 329 */               if (wars[i].hasEnded()) {
/* 330 */                 wars[i].delete();
/*     */               } else {
/*     */                 
/* 333 */                 if (wars[i].getAggressor() != alliance.getId()) {
/* 334 */                   enemy = PvPAlliance.getPvPAlliance(wars[i].getAggressor());
/*     */                 } else {
/* 336 */                   enemy = PvPAlliance.getPvPAlliance(wars[i].getDefender());
/* 337 */                 }  if (enemy != null)
/*     */                 {
/* 339 */                   if (i == wars.length - 1) {
/* 340 */                     buf.append(enemy.getName());
/* 341 */                   } else if (i == wars.length - 2) {
/* 342 */                     buf.append(enemy.getName() + " and ");
/*     */                   } else {
/* 344 */                     buf.append(enemy.getName() + ", ");
/*     */                   }  } 
/*     */               } 
/*     */             } 
/* 348 */             buf.append(".\"}");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 353 */       if (Servers.localServer.HOMESERVER && Servers.localServer.EPIC) {
/*     */         
/* 355 */         buf.append("text{type=\"bold\";text=\"Our notoriety is " + village.getVillageReputation() + ".\"};");
/*     */         
/* 357 */         if (village.getVillageReputation() >= 50) {
/* 358 */           buf.append("text{text=\" Over 50 - other settlements may declare war on us. \"};text{text=\"\"}");
/*     */         } else {
/*     */           
/* 361 */           buf.append("text{text=\" Below 50 - other settlements may not declare war on us. \"};text{text=\"\"}");
/*     */         } 
/*     */       } 
/* 364 */       Village[] enemies = village.getEnemies();
/* 365 */       if (enemies.length > 0) {
/*     */         
/* 367 */         buf.append("text{type=\"bold\";text=\"We are at war with the following settlements: \"};text{text=\"");
/*     */         
/* 369 */         Arrays.sort((Object[])enemies);
/*     */         
/* 371 */         for (int x = 0; x < enemies.length; x++) {
/*     */           
/* 373 */           if (x == enemies.length - 1) {
/* 374 */             buf.append(enemies[x].getName());
/* 375 */           } else if (x == enemies.length - 2) {
/* 376 */             buf.append(enemies[x].getName() + " and ");
/*     */           } else {
/* 378 */             buf.append(enemies[x].getName() + ", ");
/*     */           } 
/* 380 */         }  buf.append(".\"}");
/* 381 */         buf.append("text{text=\"\"}");
/*     */       } 
/* 383 */       if (village.warDeclarations != null) {
/*     */         
/* 385 */         buf.append("label{text=\"The current settlement war declarations are: \"}");
/* 386 */         for (Iterator<WarDeclaration> it = village.warDeclarations.values().iterator(); it.hasNext(); ) {
/*     */           
/* 388 */           WarDeclaration declaration = it.next();
/* 389 */           buf.append("text{text=\"" + declaration.receiver.getName() + " must answer the challenge from " + declaration.declarer
/* 390 */               .getName() + " within " + 
/* 391 */               Server.getTimeFor(declaration.time + 86400000L - System.currentTimeMillis()) + ".\"}");
/*     */         } 
/* 393 */         buf.append("text{text=\"\"}");
/*     */       } 
/*     */       
/* 396 */       if (village.getSkillModifier() == 0.0D) {
/* 397 */         buf.append("text{text=\"This settlement has no acquired knowledge.\"}");
/*     */       } else {
/* 399 */         buf.append("text{text=\"This settlement has acquired knowledge that increases the productivity bonus of its citizens by " + village
/* 400 */             .getSkillModifier() + "%.\"}");
/* 401 */       }  buf.append("text{text=\"\"}");
/*     */       
/* 403 */       Citizen mayor = village.getMayor();
/* 404 */       if (mayor != null) {
/* 405 */         buf.append("text{type=\"italic\";text=\"" + mayor.getName() + ", " + mayor.getRole().getName() + ", " + village
/* 406 */             .getName() + "\"};text{text=\"\"}");
/*     */       } else {
/* 408 */         buf.append("text{type=\"italic\";text=\"The Citizens, " + village.getName() + "\"};text{text=\"\"}");
/* 409 */       }  buf.append(createAnswerButton2());
/* 410 */       getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 412 */     catch (NoSuchItemException nsi) {
/*     */       
/* 414 */       logger.log(Level.WARNING, getResponder().getName() + " tried to get info for null token with id " + this.target, (Throwable)nsi);
/* 415 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for that request. Please contact administration.");
/*     */     
/*     */     }
/* 418 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 420 */       logger.log(Level.WARNING, getResponder().getName() + " tried to get info for null settlement for token with id " + this.target);
/*     */       
/* 422 */       getResponder().getCommunicator().sendNormalServerMessage("Failed to locate the settlement for that request. Please contact administration.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\VillageInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
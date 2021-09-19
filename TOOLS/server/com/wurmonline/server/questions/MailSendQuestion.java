/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import java.io.IOException;
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
/*     */ public final class MailSendQuestion
/*     */   extends Question
/*     */   implements MonetaryConstants
/*     */ {
/*     */   private final Item mailbox;
/*     */   private final Item[] items;
/*     */   
/*     */   public MailSendQuestion(Creature aResponder, String aTitle, String aQuestion, Item aMailbox) {
/*  48 */     super(aResponder, aTitle, aQuestion, 54, aMailbox.getWurmId());
/*  49 */     this.mailbox = aMailbox;
/*  50 */     this.items = this.mailbox.getItemsAsArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validateMailboxContents(Item[] items, Item mailbox) {
/*  61 */     if (items.length != mailbox.getItems().size())
/*  62 */       return false; 
/*  63 */     for (Item i : items) {
/*     */       
/*  65 */       if (!mailbox.getItems().contains(i))
/*  66 */         return false; 
/*     */     } 
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  74 */     String nosend = answers.getProperty("dontsend");
/*  75 */     if (!validateMailboxContents(this.items, this.mailbox)) {
/*     */       
/*  77 */       getResponder().getCommunicator().sendNormalServerMessage("The items in the mailbox have changed. Please try sending again.");
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     if (nosend != null && nosend.equals("true")) {
/*  82 */       getResponder().getCommunicator().sendNormalServerMessage("You decide to not send the shipment yet.");
/*     */     } else {
/*     */       
/*  85 */       String name = answers.getProperty("IDrecipient");
/*  86 */       if (name != null && name.length() > 2) {
/*     */         
/*  88 */         if (LoginHandler.containsIllegalCharacters(name)) {
/*  89 */           getResponder().getCommunicator().sendNormalServerMessage("The name of the receiver contains illegal characters. Please check the name.");
/*     */         }
/*     */         else {
/*     */           
/*  93 */           name = LoginHandler.raiseFirstLetter(name);
/*  94 */           PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/*  95 */           long[] info = { Servers.localServer.id, pinf.wurmId };
/*     */ 
/*     */           
/*     */           try {
/*  99 */             pinf.load();
/* 100 */             info = new long[] { Servers.localServer.id, pinf.wurmId };
/*     */             
/* 102 */             byte kingdom = Players.getInstance().getKingdomForPlayer(pinf.wurmId);
/* 103 */             if (kingdom != getResponder().getKingdomId()) {
/*     */               
/* 105 */               String kname = Kingdoms.getNameFor(kingdom);
/* 106 */               getResponder().getCommunicator().sendNormalServerMessage(pinf
/* 107 */                   .getName() + " is with the " + kname + ". You may not trade with the enemy.");
/*     */               return;
/*     */             } 
/* 110 */             if (getResponder().getEnemyPresense() > 0) {
/*     */               
/* 112 */               getResponder().getCommunicator().sendNormalServerMessage("You cannot send mail while there is an enemy nearby.");
/*     */ 
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */ 
/*     */             
/* 120 */             LoginServerWebConnection lsw = new LoginServerWebConnection();
/*     */             
/*     */             try {
/* 123 */               info = lsw.getCurrentServer(name, -1L);
/*     */             }
/* 125 */             catch (Exception e) {
/*     */               
/* 127 */               getResponder().getCommunicator().sendNormalServerMessage("That island is not available currently. Please try again later.");
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/* 132 */           } catch (IOException iox) {
/*     */             
/* 134 */             LoginServerWebConnection lsw = new LoginServerWebConnection();
/*     */             
/*     */             try {
/* 137 */               info = lsw.getCurrentServer(name, -1L);
/*     */             }
/* 139 */             catch (Exception e) {
/*     */               
/* 141 */               info = new long[] { -1L, -1L };
/*     */             } 
/*     */           } 
/*     */           
/* 145 */           if (info[1] > 0L && (int)info[0] > 0) {
/*     */             
/* 147 */             boolean sameServer = (info[0] == Servers.localServer.id);
/* 148 */             boolean[] cods = new boolean[this.items.length];
/* 149 */             for (int x = 0; x < this.items.length; x++) {
/*     */               
/* 151 */               String val = answers.getProperty(x + "cod");
/* 152 */               if (val != null && val.equals("true")) {
/* 153 */                 cods[x] = true;
/*     */               } else {
/* 155 */                 cods[x] = false;
/* 156 */               }  if (this.items[x].getTemplateId() == 651) {
/*     */                 
/* 158 */                 getResponder().getCommunicator().sendNormalServerMessage("The spirits refuse to touch the " + this.items[x]
/* 159 */                     .getName() + " since they don't know what's in it.");
/*     */                 
/*     */                 return;
/*     */               } 
/* 163 */               if (this.items[x].isCoin()) {
/*     */                 
/* 165 */                 getResponder().getCommunicator().sendNormalServerMessage("The spirits refuse to touch the coin.");
/*     */                 
/*     */                 return;
/*     */               } 
/* 169 */               if (this.items[x].isBanked()) {
/*     */                 
/* 171 */                 getResponder().getCommunicator().sendNormalServerMessage("The " + this.items[x]
/* 172 */                     .getName() + " is currently unavailable.");
/*     */                 return;
/*     */               } 
/* 175 */               if (this.items[x].isUnfinished()) {
/*     */                 
/* 177 */                 getResponder().getCommunicator().sendNormalServerMessage("The spirits seem afraid to break the " + this.items[x]
/* 178 */                     .getName() + ".");
/*     */                 return;
/*     */               } 
/* 181 */               if (!sameServer) {
/*     */                 
/* 183 */                 boolean changingCluster = false;
/* 184 */                 ServerEntry entry = Servers.getServerWithId((int)info[0]);
/* 185 */                 if (entry == null) {
/*     */                   
/* 187 */                   getResponder().getCommunicator().sendNormalServerMessage("You can not mail the " + this.items[x]
/* 188 */                       .getName() + " that far.");
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
/*     */                   return;
/*     */                 } 
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
/* 214 */                 if (changingCluster) {
/*     */                   
/* 216 */                   getResponder().getCommunicator().sendNormalServerMessage("You can not mail that far.");
/*     */                   return;
/*     */                 } 
/* 219 */                 if (!this.items[x].willLeaveServer(false, changingCluster, false)) {
/*     */                   
/* 221 */                   getResponder().getCommunicator().sendNormalServerMessage("You can not mail the " + this.items[x]
/* 222 */                       .getName() + " that far.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 234 */                 Item[] contained = this.items[x].getAllItems(true);
/* 235 */                 for (int c = 0; c < contained.length; c++) {
/*     */                   
/* 237 */                   if (!contained[c].willLeaveServer(false, changingCluster, false)) {
/*     */                     
/* 239 */                     getResponder().getCommunicator().sendNormalServerMessage("You can not mail the " + contained[c]
/* 240 */                         .getName() + " that far.");
/*     */                     return;
/*     */                   } 
/* 243 */                   if (!isContainerMailItemOk(contained[c], this.items[x])) {
/*     */                     return;
/*     */                   }
/*     */                 } 
/*     */               } else {
/*     */                 
/* 249 */                 Item[] contained = this.items[x].getAllItems(true);
/* 250 */                 for (int c = 0; c < contained.length; c++) {
/*     */                   
/* 252 */                   if (!isContainerMailItemOk(contained[c], this.items[x]))
/*     */                     return; 
/*     */                 } 
/*     */               } 
/*     */             } 
/* 257 */             MailSendConfirmQuestion msc = new MailSendConfirmQuestion(getResponder(), "Confirm the price", "Check the price of the shipment and set C.O.D prices:", this.mailbox, this.items, cods, name, info);
/*     */ 
/*     */             
/* 260 */             msc.sendQuestion();
/*     */           } else {
/*     */             
/* 263 */             getResponder().getCommunicator().sendNormalServerMessage("Unknown recipient '" + name + "'.");
/*     */           } 
/*     */         } 
/*     */       } else {
/* 267 */         getResponder().getCommunicator().sendNormalServerMessage("Unknown recipient. Please try again.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final boolean isContainerMailItemOk(Item item, Item container) {
/* 273 */     if (item.isArtifact() || item.isRoyal()) {
/*     */       
/* 275 */       getResponder().getCommunicator()
/* 276 */         .sendAlertServerMessage("You can not mail the " + item
/* 277 */           .getName() + " (in the " + container
/* 278 */           .getName() + "). The spirits refuse to deal with it.");
/*     */       
/* 280 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     if (item.getTemplateId() == 651) {
/*     */       
/* 293 */       getResponder().getCommunicator().sendNormalServerMessage("The gift boxes are not handled by the mail service.");
/*     */       
/* 295 */       return false;
/*     */     } 
/* 297 */     if (item.isCoin()) {
/*     */       
/* 299 */       getResponder().getCommunicator().sendNormalServerMessage("Coins are currently not handled by the mail service.");
/*     */       
/* 301 */       return false;
/*     */     } 
/* 303 */     if (item.isBanked()) {
/*     */       
/* 305 */       getResponder().getCommunicator().sendNormalServerMessage("The " + item
/* 306 */           .getName() + " is currently unavailable.");
/* 307 */       return false;
/*     */     } 
/* 309 */     if (item.isUnfinished()) {
/*     */       
/* 311 */       getResponder().getCommunicator().sendNormalServerMessage("Unfinished items would be broken by the mail service.");
/*     */       
/* 313 */       return false;
/*     */     } 
/* 315 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 321 */     String lHtml = getBmlHeader();
/* 322 */     StringBuilder buf = new StringBuilder(lHtml);
/* 323 */     buf.append("text{type='bold';text='Use mailboxes with caution and inexpensive items until you rely on the spirits. They are still learning and take no responsibility for lost items.'};");
/*     */     
/* 325 */     buf.append("text{text='Here you decide who you want to send the contains of the mailbox to, and whether they should be C.O.D.'};");
/* 326 */     buf.append("text{text='If an item should be C.O.D (Cash On Delivery, paid by the receiver), check the corresponding checkbox.'};");
/* 327 */     buf.append("text{text='Note that sending C.O.D with an intent to scam other players is a bannable offense.'};");
/* 328 */     buf.append("label{text=\"Select the recipient:\"};");
/* 329 */     buf.append("input{text='';maxchars='40'; id='IDrecipient'};");
/* 330 */     buf.append("text{text=''}");
/*     */     
/* 332 */     buf.append("text{text='The costs are shown on the next screen. It is safe to click send here, but if you click send on the next screen, the goods will be sent.'};");
/* 333 */     buf.append("checkbox{text='Check this if you do not want to go to the next screen';id='dontsend'};");
/* 334 */     buf.append("table{rows='" + (this.items.length + 1) + "'; cols='4';label{text='Item name'};label{text='QL'};label{text='DAM'};label{text='C.O.D'};");
/*     */ 
/*     */ 
/*     */     
/* 338 */     for (int x = 0; x < this.items.length; x++) {
/*     */       
/* 340 */       buf.append(itemNameWithColorByRarity(this.items[x]));
/* 341 */       buf.append("label{text=\"" + String.format("%.2f", new Object[] { Float.valueOf(this.items[x].getQualityLevel()) }) + "\"};");
/* 342 */       buf.append("label{text=\"" + String.format("%.2f", new Object[] { Float.valueOf(this.items[x].getDamage()) }) + "\"};");
/* 343 */       buf.append("checkbox{id='" + x + "cod'};");
/*     */     } 
/* 345 */     buf.append("};");
/*     */     
/* 347 */     buf.append(createAnswerButton2());
/* 348 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MailSendQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
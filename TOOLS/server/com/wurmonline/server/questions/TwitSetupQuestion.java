/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
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
/*     */ public class TwitSetupQuestion
/*     */   extends Question
/*     */ {
/*  32 */   private static Logger logger = Logger.getLogger(TwitSetupQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean isVillage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TwitSetupQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, boolean village) {
/*  45 */     super(aResponder, aTitle, aQuestion, 90, aTarget);
/*  46 */     this.isVillage = village;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/*  57 */     String _consumerKeyToUse = "";
/*  58 */     String _consumerSecretToUse = "";
/*  59 */     String _applicationToken = "";
/*  60 */     String _applicationSecret = "";
/*  61 */     String key = "consumerKeyToUse";
/*  62 */     boolean twitChat = false;
/*  63 */     String val = aAnswers.getProperty(key);
/*  64 */     if (val != null)
/*     */     {
/*  66 */       _consumerKeyToUse = val.trim();
/*     */     }
/*  68 */     key = "consumerSecretToUse";
/*  69 */     val = aAnswers.getProperty(key);
/*  70 */     if (val != null)
/*     */     {
/*  72 */       _consumerSecretToUse = val.trim();
/*     */     }
/*  74 */     key = "applicationToken";
/*  75 */     val = aAnswers.getProperty(key);
/*  76 */     if (val != null)
/*     */     {
/*  78 */       _applicationToken = val.trim();
/*     */     }
/*  80 */     key = "applicationSecret";
/*  81 */     val = aAnswers.getProperty(key);
/*  82 */     if (val != null)
/*     */     {
/*  84 */       _applicationSecret = val.trim();
/*     */     }
/*  86 */     key = "twitChat";
/*  87 */     val = aAnswers.getProperty(key);
/*  88 */     if (val != null)
/*     */     {
/*  90 */       twitChat = val.equals("true");
/*     */     }
/*  92 */     boolean champtwits = false;
/*  93 */     key = "champtwit";
/*  94 */     val = aAnswers.getProperty(key);
/*  95 */     if (val != null)
/*     */     {
/*  97 */       champtwits = val.equals("true");
/*     */     }
/*  99 */     if (this.isVillage) {
/*     */       try {
/*     */         Village village;
/*     */ 
/*     */ 
/*     */         
/* 105 */         if (this.target == -10L) {
/*     */           
/* 107 */           village = getResponder().getCitizenVillage();
/* 108 */           if (village == null) {
/* 109 */             throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*     */           }
/*     */         } else {
/*     */           
/* 113 */           village = Villages.getVillage((int)this.target);
/*     */         } 
/*     */         
/* 116 */         boolean twitEnabled = village.isTwitEnabled();
/* 117 */         if (getResponder().getPower() > 0) {
/*     */           
/* 119 */           key = "twitEnabled";
/* 120 */           val = aAnswers.getProperty(key);
/* 121 */           if (val != null)
/*     */           {
/* 123 */             twitEnabled = val.equals("true");
/*     */           }
/* 125 */           getResponder().getLogger().log(Level.INFO, "Setting " + village
/* 126 */               .getName() + " twitter enable to " + twitEnabled + ".");
/*     */         } 
/* 128 */         if (_consumerKeyToUse == null || _consumerSecretToUse == null || _applicationToken == null || _applicationSecret == null) {
/*     */ 
/*     */           
/* 131 */           logger.info(getResponder() + " has cleared the Twitter credentials for Settlement: " + village);
/* 132 */           village.setTwitCredentials("", "", "", "", false, twitEnabled);
/* 133 */           getResponder().getCommunicator().sendNormalServerMessage("No twitting will occur now.");
/*     */         }
/*     */         else {
/*     */           
/* 137 */           village.setTwitCredentials(_consumerKeyToUse, _consumerSecretToUse, _applicationToken, _applicationSecret, twitChat, twitEnabled);
/*     */           
/* 139 */           if (village.canTwit())
/*     */           {
/* 141 */             logger.info(getResponder() + " has set the Twitter credentials for Settlement: " + village);
/* 142 */             getResponder().getCommunicator().sendNormalServerMessage("Allright, twit away!");
/*     */           }
/*     */           else
/*     */           {
/* 146 */             logger.info(getResponder() + " has set invalid Twitter credentials for Settlement: " + village + ", so Twitter is now disabled.");
/*     */             
/* 148 */             getResponder().getCommunicator().sendNormalServerMessage("You won't be twittin' with those keys.");
/*     */           }
/*     */         
/*     */         } 
/* 152 */       } catch (NoSuchVillageException nsv) {
/*     */         
/* 154 */         getResponder().getCommunicator().sendAlertServerMessage("No such settlement.");
/*     */       }
/*     */     
/* 157 */     } else if (getResponder().getPower() >= 3) {
/*     */       
/* 159 */       if (_consumerKeyToUse == null || _consumerSecretToUse == null || _applicationToken == null || _applicationSecret == null) {
/*     */ 
/*     */         
/* 162 */         logger.info(getResponder() + " has cleared the Twitter credentials for this server, whose ID is: " + Servers.localServer.id);
/*     */         
/* 164 */         if (champtwits) {
/* 165 */           Servers.localServer.setChampTwitter("", "", "", "");
/*     */         } else {
/* 167 */           Servers.setTwitCredentials(Servers.localServer.id, "", "", "", "");
/* 168 */         }  getResponder().getCommunicator().sendNormalServerMessage("No twitting will occur now.");
/*     */ 
/*     */       
/*     */       }
/* 172 */       else if (champtwits) {
/*     */         
/* 174 */         Servers.localServer.setChampTwitter(_consumerKeyToUse, _consumerSecretToUse, _applicationToken, _applicationSecret);
/*     */         
/* 176 */         Servers.localServer.canTwit();
/* 177 */         if (Servers.localServer.canTwitChamps)
/*     */         {
/* 179 */           logger.info(getResponder() + " has set the Champion Twitter credentials for this server, whose ID is: " + Servers.localServer.id);
/*     */           
/* 181 */           getResponder().getCommunicator().sendNormalServerMessage("Allright, twit away!");
/*     */         }
/*     */         else
/*     */         {
/* 185 */           logger.info(getResponder() + " has set invalid Champion Twitter credentials for this server, whose ID is: " + Servers.localServer.id + ", so Champion Twitter is now disabled.");
/*     */ 
/*     */           
/* 188 */           getResponder().getCommunicator().sendNormalServerMessage("You won't be twittin' with those keys.");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 193 */         Servers.setTwitCredentials(Servers.localServer.id, _consumerKeyToUse, _consumerSecretToUse, _applicationToken, _applicationSecret);
/*     */         
/* 195 */         if (Servers.localServer.canTwit())
/*     */         {
/* 197 */           logger.info(getResponder() + " has set the Twitter credentials for this server, whose ID is: " + Servers.localServer.id);
/*     */           
/* 199 */           getResponder().getCommunicator().sendNormalServerMessage("Allright, twit away!");
/*     */         }
/*     */         else
/*     */         {
/* 203 */           logger.info(getResponder() + " has set invalid Twitter credentials for this server, whose ID is: " + Servers.localServer.id + ", so Twitter is now disabled.");
/*     */           
/* 205 */           getResponder().getCommunicator().sendNormalServerMessage("You won't be twittin' with those keys.");
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 212 */       logger.info(getResponder() + " tried to set the Twitter credentials but was not allowed.");
/* 213 */       getResponder().getCommunicator().sendAlertServerMessage("You are not allowed to edit this information.");
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
/* 225 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 226 */     String _consumerKeyToUse = "";
/* 227 */     String _consumerSecretToUse = "";
/* 228 */     String _applicationToken = "";
/* 229 */     String _applicationSecret = "";
/* 230 */     boolean twitChat = false;
/* 231 */     boolean enabled = true;
/* 232 */     String vilname = "";
/* 233 */     if (this.isVillage) {
/*     */       try {
/*     */         Village village;
/*     */ 
/*     */ 
/*     */         
/* 239 */         if (this.target == -10L) {
/*     */           
/* 241 */           village = getResponder().getCitizenVillage();
/* 242 */           if (village == null) {
/* 243 */             throw new NoSuchVillageException("You are not a citizen of any village (on this server).");
/*     */           }
/*     */         } else {
/*     */           
/* 247 */           village = Villages.getVillage((int)this.target);
/*     */         } 
/* 249 */         _consumerKeyToUse = village.getConsumerKey();
/* 250 */         _consumerSecretToUse = village.getConsumerSecret();
/* 251 */         _applicationToken = village.getApplicationToken();
/* 252 */         _applicationSecret = village.getApplicationSecret();
/* 253 */         twitChat = village.twitChat();
/* 254 */         enabled = village.isTwitEnabled();
/* 255 */         vilname = village.getName();
/*     */       }
/* 257 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 259 */         buf.append("text{text=\"settlement not found.\"};");
/* 260 */         buf.append("text{text=\"\"};");
/*     */       }
/*     */     
/* 263 */     } else if (getResponder().getPower() >= 3) {
/*     */       
/* 265 */       _consumerKeyToUse = Servers.localServer.getConsumerKey();
/* 266 */       _consumerSecretToUse = Servers.localServer.getConsumerSecret();
/* 267 */       _applicationToken = Servers.localServer.getApplicationToken();
/* 268 */       _applicationSecret = Servers.localServer.getApplicationSecret();
/*     */     } 
/* 270 */     buf.append("text{text=\"In order to use this functionality you need to perform the following steps:\"};");
/* 271 */     buf.append("text{text=\"1. You need to create a twitter account.\"}");
/* 272 */     buf.append("text{text=\"2. You need to register an application (use the developers link down to the right in twitter).\"}");
/* 273 */     buf.append("text{text=\"3. Insert the consumer key and secret, as well as the access token information for the application here.\"}");
/* 274 */     buf.append("harray{label{text=\"Consumer key \"};input{id=\"consumerKeyToUse\"; maxchars=\"70\"; text=\"" + _consumerKeyToUse + "\"}}");
/*     */     
/* 276 */     buf.append("harray{label{text=\"Consumer secret \"};input{id=\"consumerSecretToUse\"; maxchars=\"70\"; text=\"" + _consumerSecretToUse + "\"}}");
/*     */     
/* 278 */     buf.append("harray{label{text=\"Application key (oauth token) \"}input{id=\"applicationToken\"; maxchars=\"70\"; text=\"" + _applicationToken + "\"}}");
/*     */     
/* 280 */     buf.append("harray{label{text=\"Application secret (oauth token secret) \"}input{id=\"applicationSecret\"; maxchars=\"70\"; text=\"" + _applicationSecret + "\"}}");
/*     */     
/* 282 */     if (this.isVillage) {
/*     */       
/* 284 */       buf.append("checkbox{id=\"twitChat\";text=\"Twit all settlement chat? \";selected=\"" + twitChat + "\"};");
/* 285 */       if (getResponder().getPower() > 0)
/*     */       {
/* 287 */         getResponder().getLogger().log(Level.INFO, "Editing " + vilname + " twitter settings.");
/* 288 */         buf.append("checkbox{id=\"twitEnabled\";text=\"Enable twit? \";selected=\"" + enabled + "\"};");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 294 */       buf.append("checkbox{id=\"champtwit\";text=\"Setting for Champion tweets? \";selected=\"false\"};");
/*     */     } 
/* 296 */     buf.append(createAnswerButton2());
/* 297 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TwitSetupQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
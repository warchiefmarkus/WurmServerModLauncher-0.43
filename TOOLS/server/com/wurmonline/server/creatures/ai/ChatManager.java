/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.epic.EpicMission;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChatManager
/*     */   implements TimeConstants
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(ChatManager.class.getName());
/*     */   
/*     */   final Creature owner;
/*  45 */   final LinkedList<String> mychats = new LinkedList<>();
/*  46 */   final ConcurrentHashMap<String, String> localchats = new ConcurrentHashMap<>();
/*  47 */   final ConcurrentHashMap<Message, String> unansweredLChats = new ConcurrentHashMap<>();
/*  48 */   final ConcurrentHashMap<String, String> receivedchats = new ConcurrentHashMap<>();
/*  49 */   final ConcurrentHashMap<String, String> unansweredChats = new ConcurrentHashMap<>();
/*  50 */   final HashSet<String> localChats = new HashSet<>();
/*  51 */   int chatPoller = 0;
/*  52 */   long lastChattedLocal = 0L;
/*  53 */   long lastPCChattedLocal = 0L;
/*  54 */   int lastTx = 0;
/*  55 */   int lastTy = 0;
/*  56 */   int chattiness = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   int numchatsSinceLast;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChat(String chatter, String message) {
/*  66 */     this.unansweredChats.put(message, chatter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkChats() {
/*  71 */     if (this.chatPoller > 0) {
/*     */       
/*  73 */       this.chatPoller--;
/*  74 */       if (this.chatPoller % 10 == 0) {
/*     */         
/*  76 */         pollLocal();
/*  77 */         startLocalChat();
/*     */       } 
/*  79 */       if (this.chatPoller % 8 == 0)
/*     */       {
/*  81 */         if (this.unansweredChats.size() > 0) {
/*     */           
/*  83 */           int answered = 0;
/*  84 */           for (Map.Entry<String, String> entry : this.unansweredChats.entrySet()) {
/*     */             
/*  86 */             this.receivedchats.put(entry.getKey(), entry.getValue());
/*     */             
/*     */             try {
/*  89 */               Player p = Players.getInstance().getPlayer(entry.getValue());
/*  90 */               if (answered < 2 || Server.rand.nextBoolean())
/*     */               {
/*  92 */                 createAndSendMessage(p, getAnswerToMessage(entry.getValue(), ((String)entry.getKey()).replace(".", "")), (Server.rand.nextInt(10) == 0));
/*     */               }
/*     */             }
/*  95 */             catch (NoSuchPlayerException nsp) {
/*     */               
/*  97 */               logger.log(Level.INFO, nsp.getMessage());
/*     */             } 
/*  99 */             answered++;
/*     */           } 
/* 101 */           this.unansweredChats.clear();
/*     */         } 
/*     */       }
/*     */       return;
/*     */     } 
/* 106 */     this.chatPoller = 25;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void createAndSendMessage(Player receiver, String message, boolean emote) {
/* 111 */     if (!this.mychats.contains(message))
/* 112 */       this.mychats.add(message.toLowerCase().replace(".", "").trim()); 
/* 113 */     receiver.showPM(this.owner.getName(), this.owner.getName(), message, emote);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String[] getReceivedChatsAsArr() {
/* 118 */     return (String[])this.receivedchats.keySet().toArray((Object[])new String[this.receivedchats.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String[] getLocalChatsAsArr() {
/* 123 */     return (String[])this.localchats.keySet().toArray((Object[])new String[this.localchats.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getAnswerToMessage(String receiver, String message) {
/* 128 */     int rand = Server.rand.nextInt(100);
/* 129 */     int next = Server.rand.nextInt(100);
/* 130 */     boolean toMe = message.toLowerCase().contains(this.owner.getName().toLowerCase());
/* 131 */     String emptyOrReceiver = "";
/* 132 */     if (toMe)
/* 133 */       emptyOrReceiver = LoginHandler.raiseFirstLetter(receiver) + ", "; 
/* 134 */     if (message.endsWith("?")) {
/*     */       
/* 136 */       message = message.replace("?", "");
/* 137 */       if (rand < 10 || message.toLowerCase().contains("where")) {
/*     */         
/* 139 */         if (rand < 5) {
/* 140 */           return emptyOrReceiver + "I heard there's one Key of the Heavens hidden somewhere.";
/*     */         }
/* 142 */         String loc = "north";
/* 143 */         if (next < 10) {
/* 144 */           loc = "west";
/* 145 */         } else if (next < 20) {
/* 146 */           loc = "east";
/* 147 */         } else if (next < 30) {
/* 148 */           loc = "south";
/* 149 */         } else if (next < 40) {
/* 150 */           loc = "water";
/* 151 */         } else if (next < 50) {
/* 152 */           loc = "sky";
/* 153 */         } else if (next < 60) {
/* 154 */           loc = "fire";
/* 155 */         } else if (next < 70) {
/* 156 */           loc = "darkest spot";
/* 157 */         } else if (next < 80) {
/* 158 */           loc = "eye of the beholder";
/* 159 */         } else if (next < 90) {
/* 160 */           loc = "back of the room";
/*     */         } 
/* 162 */         return emptyOrReceiver + "In the " + loc + " as far as I know.";
/*     */       } 
/* 164 */       if (rand < 20) {
/*     */         
/* 166 */         if (this.localchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */           
/* 168 */           String[] chats = getLocalChatsAsArr();
/* 169 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 170 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 171 */           String str1 = stringTokenizer1.nextToken();
/* 172 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 174 */             String a = stringTokenizer1.nextToken();
/* 175 */             if (Server.rand.nextBoolean())
/* 176 */               str1 = a; 
/*     */           } 
/* 178 */           return emptyOrReceiver + "Look for " + str1 + ".";
/*     */         } 
/* 180 */         if (rand < 15)
/* 181 */           return emptyOrReceiver + "The gods may grant you a Key of the Heavens if you do their missions."; 
/* 182 */         return "No" + (Server.rand.nextBoolean() ? "!" : ".");
/*     */       } 
/* 184 */       if (rand < 30) {
/*     */         
/* 186 */         if (this.mychats.size() > 0)
/* 187 */           return emptyOrReceiver + "I just said " + (String)this.mychats.get(Server.rand.nextInt(this.mychats.size())) + ", didn't I?"; 
/* 188 */         return "I think so" + (Server.rand.nextBoolean() ? "!" : ".");
/*     */       } 
/* 190 */       if (rand < 40) {
/*     */         
/* 192 */         if (this.localchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */           
/* 194 */           String[] chats = getLocalChatsAsArr();
/* 195 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 196 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 197 */           String str1 = stringTokenizer1.nextToken();
/* 198 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 200 */             String a = stringTokenizer1.nextToken();
/* 201 */             if (Server.rand.nextBoolean())
/* 202 */               str1 = a; 
/*     */           } 
/* 204 */           return "Would this help you: " + str1 + ".";
/*     */         } 
/* 206 */         return "Yes" + (Server.rand.nextBoolean() ? "!" : ".");
/*     */       } 
/* 208 */       if (rand < 50 || message.toLowerCase().contains("who")) {
/*     */         
/* 210 */         String loc = "The Forest Giant";
/* 211 */         if (next < 10) {
/* 212 */           loc = "You";
/* 213 */         } else if (next < 20) {
/* 214 */           loc = "Brightberry";
/* 215 */         } else if (next < 30) {
/* 216 */           loc = "Ceyer";
/* 217 */         } else if (next < 40) {
/* 218 */           loc = "Fo";
/* 219 */         } else if (next < 50) {
/* 220 */           loc = "Libila";
/* 221 */         } else if (next < 60) {
/* 222 */           loc = "Magranon";
/* 223 */         } else if (next < 70) {
/*     */           
/* 225 */           loc = "The Unknown One";
/* 226 */           for (Deity d : Deities.getDeities()) {
/*     */             
/* 228 */             if (d.isCustomDeity() && Server.rand.nextBoolean()) {
/*     */               
/* 230 */               loc = d.getName();
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 235 */         } else if (next < 80) {
/* 236 */           loc = "Uttacha";
/* 237 */         } else if (next < 90) {
/* 238 */           loc = "The Deathcrawler";
/*     */         } 
/* 240 */         return emptyOrReceiver + loc + " as far as I know.";
/*     */       } 
/* 242 */       if (rand < 60) {
/*     */         
/* 244 */         if (this.receivedchats.size() > 3) {
/*     */           
/* 246 */           String[] chats = getReceivedChatsAsArr();
/* 247 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 248 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 249 */           String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 250 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 252 */             String a = stringTokenizer1.nextToken();
/* 253 */             if (Server.rand.nextBoolean())
/* 254 */               str1 = a; 
/*     */           } 
/* 256 */           return "Well, someone secretely told me something about " + str1 + " before.";
/*     */         } 
/* 258 */         return emptyOrReceiver + "Who?";
/*     */       } 
/* 260 */       if (rand < 70) {
/*     */         
/* 262 */         if (this.receivedchats.size() > 3) {
/*     */           
/* 264 */           String[] chats = getReceivedChatsAsArr();
/* 265 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 266 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 267 */           String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 268 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 270 */             String a = stringTokenizer1.nextToken();
/* 271 */             if (Server.rand.nextBoolean())
/* 272 */               str1 = a; 
/*     */           } 
/* 274 */           return "All I know is that " + str1 + ".";
/*     */         } 
/* 276 */         return emptyOrReceiver + "I thought I answered that already?";
/*     */       } 
/* 278 */       if (rand < 80) {
/*     */         
/* 280 */         StringTokenizer stringTokenizer1 = new StringTokenizer(message);
/* 281 */         String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 282 */         while (stringTokenizer1.hasMoreTokens()) {
/*     */           
/* 284 */           String a = stringTokenizer1.nextToken();
/* 285 */           if (Server.rand.nextBoolean())
/* 286 */             str1 = a; 
/*     */         } 
/* 288 */         return emptyOrReceiver + "Can you explain what you mean by " + str1 + "?";
/*     */       } 
/* 290 */       if (rand < 90) {
/*     */         
/* 292 */         StringTokenizer stringTokenizer1 = new StringTokenizer(message);
/* 293 */         String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 294 */         while (stringTokenizer1.hasMoreTokens()) {
/*     */           
/* 296 */           String a = stringTokenizer1.nextToken();
/* 297 */           if (Server.rand.nextBoolean())
/* 298 */             str1 = str1 + " " + a; 
/*     */         } 
/* 300 */         return "I would never agree on " + str1 + ".";
/*     */       } 
/* 302 */       StringTokenizer stringTokenizer = new StringTokenizer(message);
/* 303 */       String str = stringTokenizer.nextToken().toLowerCase();
/* 304 */       while (stringTokenizer.hasMoreTokens()) {
/*     */         
/* 306 */         if (Server.rand.nextBoolean())
/* 307 */           str = str + " " + stringTokenizer.nextToken(); 
/*     */       } 
/* 309 */       return emptyOrReceiver + "Have you heard about " + str + "?";
/*     */     } 
/* 311 */     if (message.endsWith("!")) {
/*     */       
/* 313 */       message = message.replace("!", "");
/* 314 */       if (rand < 10 || message.toLowerCase().contains("what")) {
/*     */         
/* 316 */         if (this.localchats.size() > 3) {
/*     */           
/* 318 */           String[] chats = getLocalChatsAsArr();
/* 319 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 320 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 321 */           String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 322 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 324 */             String a = stringTokenizer1.nextToken();
/* 325 */             if (Server.rand.nextBoolean())
/* 326 */               str1 = a; 
/*     */           } 
/* 328 */           return "Well, as long as people declare things about " + str1 + " everything goes I guess.";
/*     */         } 
/* 330 */         return "Isn't it?";
/*     */       } 
/* 332 */       if (rand < 20 || message.toLowerCase().contains("how")) {
/*     */         
/* 334 */         if (this.localchats.size() > 3) {
/*     */           
/* 336 */           String[] chats = getLocalChatsAsArr();
/* 337 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 338 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 339 */           String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 340 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 342 */             String a = stringTokenizer1.nextToken();
/* 343 */             if (Server.rand.nextBoolean())
/* 344 */               str1 = a; 
/*     */           } 
/* 346 */           String name = this.localchats.get(garbled);
/* 347 */           if (name != null)
/* 348 */             return "Didn't " + name + " say something related like " + str1 + "?"; 
/* 349 */           return "That may be related to " + str1 + ".";
/*     */         } 
/* 351 */         return "Anything is possible.";
/*     */       } 
/* 353 */       if (rand < 30) {
/*     */         
/* 355 */         if (this.mychats.size() > 0)
/* 356 */           return "I always claim that " + (String)this.mychats.get(Server.rand.nextInt(this.mychats.size())) + " too!"; 
/* 357 */         return emptyOrReceiver + "I can only agree.";
/*     */       } 
/* 359 */       if (rand < 40) {
/*     */         
/* 361 */         if (this.receivedchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */           
/* 363 */           String[] chats = getReceivedChatsAsArr();
/* 364 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 365 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 366 */           String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 367 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 369 */             String a = stringTokenizer1.nextToken();
/* 370 */             if (Server.rand.nextBoolean())
/* 371 */               str1 = a; 
/*     */           } 
/* 373 */           return "Like " + str1 + " you mean?";
/*     */         } 
/* 375 */         return emptyOrReceiver + "Yes!";
/*     */       } 
/* 377 */       if (rand < 50 || message.toLowerCase().contains("who")) {
/*     */         
/* 379 */         String loc = "The Forest Giant";
/* 380 */         if (next < 10) {
/* 381 */           loc = "You";
/* 382 */         } else if (next < 20) {
/* 383 */           loc = "Brightberry";
/* 384 */         } else if (next < 30) {
/* 385 */           loc = "Ceyer";
/* 386 */         } else if (next < 40) {
/* 387 */           loc = "Fo";
/* 388 */         } else if (next < 50) {
/* 389 */           loc = "Libila";
/* 390 */         } else if (next < 60) {
/* 391 */           loc = "Magranon";
/* 392 */         } else if (next < 70) {
/*     */           
/* 394 */           loc = "Vynora";
/* 395 */           for (Deity d : Deities.getDeities()) {
/*     */             
/* 397 */             if (d.isCustomDeity() && Server.rand.nextBoolean()) {
/*     */               
/* 399 */               loc = d.getName();
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 404 */         } else if (next < 80) {
/* 405 */           loc = "Uttacha";
/* 406 */         } else if (next < 90) {
/* 407 */           loc = "The Deathcrawler";
/*     */         } 
/* 409 */         return "I tend to say " + loc + ".";
/*     */       } 
/* 411 */       if (rand < 60) {
/*     */         
/* 413 */         if (this.localchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */           
/* 415 */           String[] chats = getLocalChatsAsArr();
/* 416 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 417 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 418 */           String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 419 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 421 */             String a = stringTokenizer1.nextToken();
/* 422 */             if (Server.rand.nextBoolean())
/* 423 */               str1 = a; 
/*     */           } 
/* 425 */           String name = this.localchats.get(garbled);
/* 426 */           if (name != null)
/* 427 */             return "Not unless " + str1 + ", which I think " + name + " mentioned."; 
/* 428 */           return "Not unless " + str1 + ".";
/*     */         } 
/* 430 */         return emptyOrReceiver + "What?";
/*     */       } 
/* 432 */       if (rand < 70) {
/*     */         
/* 434 */         if (this.localchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */           
/* 436 */           String[] chats = getLocalChatsAsArr();
/* 437 */           String garbled = chats[Server.rand.nextInt(chats.length)];
/* 438 */           StringTokenizer stringTokenizer1 = new StringTokenizer(garbled);
/* 439 */           String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 440 */           while (stringTokenizer1.hasMoreTokens()) {
/*     */             
/* 442 */             String a = stringTokenizer1.nextToken();
/* 443 */             if (Server.rand.nextBoolean())
/* 444 */               str1 = a; 
/*     */           } 
/* 446 */           return "Look for " + str1 + ".";
/*     */         } 
/* 448 */         StringBuilder sb = new StringBuilder("Ha");
/* 449 */         for (int x = 0; x < Server.rand.nextInt(10); x++)
/* 450 */           sb.append("ha"); 
/* 451 */         return sb.append("!").toString();
/*     */       } 
/* 453 */       if (rand < 80) {
/*     */         
/* 455 */         StringTokenizer stringTokenizer1 = new StringTokenizer(message);
/* 456 */         String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 457 */         while (stringTokenizer1.hasMoreTokens()) {
/*     */           
/* 459 */           String a = stringTokenizer1.nextToken();
/* 460 */           if (Server.rand.nextBoolean())
/* 461 */             str1 = a; 
/*     */         } 
/* 463 */         return emptyOrReceiver + "If " + str1 + " isn't good enough for you I don't know what is.";
/*     */       } 
/* 465 */       if (rand < 90) {
/*     */         
/* 467 */         StringTokenizer stringTokenizer1 = new StringTokenizer(message);
/* 468 */         String str1 = stringTokenizer1.nextToken().toLowerCase();
/* 469 */         while (stringTokenizer1.hasMoreTokens()) {
/*     */           
/* 471 */           String a = stringTokenizer1.nextToken();
/* 472 */           if (Server.rand.nextBoolean())
/* 473 */             str1 = str1 + " " + a; 
/*     */         } 
/* 475 */         return "Someone mentioned " + str1 + " before!";
/*     */       } 
/* 477 */       StringTokenizer stringTokenizer = new StringTokenizer(message);
/* 478 */       String str = stringTokenizer.nextToken().toLowerCase();
/* 479 */       while (stringTokenizer.hasMoreTokens()) {
/*     */         
/* 481 */         String a = stringTokenizer.nextToken();
/* 482 */         if (Server.rand.nextBoolean())
/* 483 */           str = str + " " + a; 
/*     */       } 
/* 485 */       return "The " + str + " is strong in that one.";
/*     */     } 
/*     */ 
/*     */     
/* 489 */     if (rand < 10 || message.toLowerCase().contains("the")) {
/*     */       
/* 491 */       StringTokenizer stringTokenizer = new StringTokenizer(message);
/* 492 */       String str = stringTokenizer.nextToken().toLowerCase();
/* 493 */       while (stringTokenizer.hasMoreTokens()) {
/*     */         
/* 495 */         str = stringTokenizer.nextToken();
/* 496 */         if (str.equalsIgnoreCase("the")) {
/*     */           
/* 498 */           str = stringTokenizer.nextToken();
/*     */           break;
/*     */         } 
/*     */       } 
/* 502 */       str = str + ((str.endsWith("s") || str.equals("sheep") || str.equals("fish") || str.equals("feet")) ? "" : "s");
/* 503 */       if (next < 10)
/* 504 */         return "Them " + str + " are someone elses problem."; 
/* 505 */       if (next < 20)
/* 506 */         return "What about " + str + "?"; 
/* 507 */       if (next < 30)
/* 508 */         return "I never understood all that talk about " + str + "."; 
/* 509 */       if (next < 40)
/* 510 */         return "Why do you care about " + str + "?"; 
/* 511 */       if (next < 50)
/* 512 */         return "I can't recall anyone mentioning " + str + " before."; 
/* 513 */       return "I had no clue that you cared so much about " + str + ".";
/*     */     } 
/* 515 */     if (rand < 20 || message.toLowerCase().contains("what")) {
/*     */       
/* 517 */       if (this.mychats.size() > 0 && Server.rand.nextBoolean())
/* 518 */         return "What you are really saying is that " + (String)this.mychats.get(Server.rand.nextInt(this.mychats.size())) + "?"; 
/* 519 */       return "I don't understand what you are saying.";
/*     */     } 
/* 521 */     if (rand < 30) {
/*     */       
/* 523 */       if (this.mychats.size() > 0)
/* 524 */         return "Someone said that " + (String)this.mychats.get(Server.rand.nextInt(this.mychats.size())) + "."; 
/* 525 */       return "Obviously.";
/*     */     } 
/* 527 */     if (rand < 40) {
/*     */       
/* 529 */       if (Server.rand.nextInt(3) == 0)
/* 530 */         return "I might go there actually."; 
/* 531 */       if (Server.rand.nextBoolean())
/* 532 */         return "Don't do it."; 
/* 533 */       return "Make my day, will you.";
/*     */     } 
/* 535 */     if (rand < 50 || message.toLowerCase().contains("you")) {
/*     */ 
/*     */       
/* 538 */       String loc = "The Forest Giant";
/* 539 */       if (next < 10) {
/* 540 */         loc = "You";
/* 541 */       } else if (next < 20) {
/*     */         
/* 543 */         loc = "Brightberry";
/* 544 */         PlayerInfo[] pinfs = PlayerInfoFactory.getPlayerInfos();
/* 545 */         if (pinfs.length > 0)
/*     */         {
/* 547 */           loc = pinfs[Server.rand.nextInt(pinfs.length)].getName();
/*     */         }
/*     */       }
/* 550 */       else if (next < 40) {
/* 551 */         loc = "Fo";
/* 552 */       } else if (next < 50) {
/* 553 */         loc = "Libila";
/* 554 */       } else if (next < 60) {
/* 555 */         loc = "Magranon";
/* 556 */       } else if (next < 70) {
/*     */         
/* 558 */         loc = "Vynora";
/* 559 */         for (Deity d : Deities.getDeities()) {
/*     */           
/* 561 */           if (d.isCustomDeity() && Server.rand.nextBoolean()) {
/*     */             
/* 563 */             loc = d.getName();
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 568 */       } else if (next < 80) {
/* 569 */         loc = "Uttacha";
/* 570 */       } else if (next < 90) {
/* 571 */         loc = "The Deathcrawler";
/* 572 */       } else if (next < 95) {
/*     */         
/* 574 */         loc = "a tree";
/* 575 */         ItemTemplate[] temps = ItemTemplateFactory.getInstance().getTemplates();
/* 576 */         if (temps.length > 0)
/*     */         {
/* 578 */           loc = temps[Server.rand.nextInt(temps.length)].getNameWithGenus();
/*     */         }
/*     */       } 
/* 581 */       if (Server.rand.nextBoolean())
/* 582 */         return emptyOrReceiver + "That's " + loc + " you're referring to."; 
/* 583 */       if (this.receivedchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */         
/* 585 */         String[] chats = getReceivedChatsAsArr();
/* 586 */         String garbled = chats[Server.rand.nextInt(chats.length)];
/* 587 */         StringTokenizer stringTokenizer = new StringTokenizer(garbled);
/* 588 */         String str1 = stringTokenizer.nextToken().toLowerCase();
/* 589 */         while (stringTokenizer.hasMoreTokens()) {
/*     */           
/* 591 */           String a = stringTokenizer.nextToken();
/* 592 */           if (Server.rand.nextBoolean())
/* 593 */             str1 = str1 + " " + a; 
/*     */         } 
/* 595 */         return emptyOrReceiver + "Maybe the " + str1 + " is something to meditate over?";
/*     */       } 
/* 597 */       return emptyOrReceiver + "We can also discuss " + loc + " if you want.";
/*     */     } 
/* 599 */     if (rand < 60) {
/*     */       
/* 601 */       if (this.receivedchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */         
/* 603 */         String[] chats = getReceivedChatsAsArr();
/* 604 */         String garbled = chats[Server.rand.nextInt(chats.length)];
/* 605 */         StringTokenizer stringTokenizer = new StringTokenizer(garbled);
/* 606 */         String str1 = stringTokenizer.nextToken().toLowerCase();
/* 607 */         while (stringTokenizer.hasMoreTokens()) {
/*     */           
/* 609 */           String a = stringTokenizer.nextToken();
/* 610 */           if (Server.rand.nextBoolean())
/* 611 */             str1 = str1 + " " + a; 
/*     */         } 
/* 613 */         return "On the other hand I heard that " + str1 + ".";
/*     */       } 
/* 615 */       EpicMission[] ems = EpicServerStatus.getCurrentEpicMissions();
/* 616 */       for (EpicMission em : ems) {
/*     */         
/* 618 */         if (em.isCurrent()) {
/*     */           
/* 620 */           Deity deity = Deities.getDeity(em.getEpicEntityId());
/* 621 */           if (deity != null)
/*     */           {
/* 623 */             if (deity.getFavoredKingdom() == this.owner.getKingdomId())
/*     */             {
/* 625 */               return "I'm considering helping " + deity.getName() + " out with " + em.getScenarioName() + ".";
/*     */             }
/*     */           }
/*     */         } 
/*     */       } 
/* 630 */       return "Please.";
/*     */     } 
/* 632 */     if (rand < 70) {
/*     */       
/* 634 */       if (this.localchats.size() > 3 && Server.rand.nextBoolean()) {
/*     */         
/* 636 */         String[] chats = getLocalChatsAsArr();
/* 637 */         String garbled = chats[Server.rand.nextInt(chats.length)];
/* 638 */         StringTokenizer stringTokenizer = new StringTokenizer(garbled);
/* 639 */         String str1 = stringTokenizer.nextToken().toLowerCase();
/* 640 */         while (stringTokenizer.hasMoreTokens()) {
/*     */           
/* 642 */           String a = stringTokenizer.nextToken();
/* 643 */           if (Server.rand.nextInt(3) == 0)
/* 644 */             str1 = str1 + " " + a; 
/*     */         } 
/* 646 */         return "Unless " + str1 + " of course.";
/*     */       } 
/* 648 */       return emptyOrReceiver + "Not today.";
/*     */     } 
/* 650 */     if (rand < 80) {
/*     */       
/* 652 */       StringTokenizer stringTokenizer = new StringTokenizer(message);
/* 653 */       String str = stringTokenizer.nextToken().toLowerCase();
/* 654 */       while (stringTokenizer.hasMoreTokens()) {
/*     */         
/* 656 */         String a = stringTokenizer.nextToken();
/* 657 */         if (Server.rand.nextBoolean())
/* 658 */           str = a; 
/*     */       } 
/* 660 */       return "There's always been " + str + " if you need it.";
/*     */     } 
/* 662 */     if (rand < 90) {
/*     */       
/* 664 */       StringTokenizer stringTokenizer = new StringTokenizer(message);
/* 665 */       String str = stringTokenizer.nextToken().toLowerCase();
/* 666 */       while (stringTokenizer.hasMoreTokens()) {
/*     */         
/* 668 */         String a = stringTokenizer.nextToken();
/* 669 */         if (Server.rand.nextBoolean())
/* 670 */           str = str + " " + a; 
/*     */       } 
/* 672 */       return emptyOrReceiver + "I know for a fact that " + str + " has been around these parts somewhere.";
/*     */     } 
/* 674 */     StringTokenizer st = new StringTokenizer(message);
/* 675 */     String s = st.nextToken().toLowerCase();
/* 676 */     while (st.hasMoreTokens()) {
/*     */       
/* 678 */       String a = st.nextToken();
/* 679 */       if (Server.rand.nextBoolean())
/* 680 */         s = s + " " + a; 
/*     */     } 
/* 682 */     return emptyOrReceiver + "I wouldn't " + s + " for my life.";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void startLocalChat() {
/* 688 */     int chattinessMod = 1;
/* 689 */     float mod = 1.0F;
/* 690 */     if (System.currentTimeMillis() - this.lastPCChattedLocal < 300000L) {
/*     */       
/* 692 */       chattinessMod = Server.rand.nextInt(10);
/* 693 */       mod = 0.3F;
/*     */     } 
/* 695 */     if ((float)(System.currentTimeMillis() - this.lastChattedLocal) > 60000.0F * Math.max(1.0F, (this.chattiness - chattinessMod) * mod))
/*     */     {
/* 697 */       if (!this.owner.isDead())
/*     */       {
/* 699 */         if (this.lastTx != this.owner.getTileX() || this.lastTy != this.owner.getTileY())
/*     */         {
/* 701 */           for (int x = -8; x <= 8; x++) {
/* 702 */             for (int y = -8; y <= 8; y++) {
/*     */               
/* 704 */               VolaTile t = Zones.getTileOrNull(this.owner.getTileX() + x, this.owner.getTileY() + y, this.owner.isOnSurface());
/* 705 */               if (t != null)
/*     */               {
/* 707 */                 for (Creature c : t.getCreatures()) {
/*     */                   
/* 709 */                   if (c.getPower() <= 0 && c.getWurmId() != this.owner.getWurmId() && ((c.isPlayer() && c.isVisibleTo(this.owner)) || c.isNpc())) {
/*     */                     
/* 711 */                     String s = getSayToCreature(c);
/* 712 */                     if (s != null && s.length() > 0)
/*     */                     {
/* 714 */                       if (!this.localChats.contains(s)) {
/*     */                         
/* 716 */                         VolaTile tile = this.owner.getCurrentTile();
/* 717 */                         if (tile != null) {
/*     */                           
/* 719 */                           if (this.owner.isFriendlyKingdom(c.getKingdomId()))
/* 720 */                             this.owner.turnTowardsCreature(c); 
/* 721 */                           this.localChats.add(s);
/* 722 */                           Message m = new Message(this.owner, (byte)0, ":Local", "<" + this.owner.getName() + "> " + s);
/*     */                           
/* 724 */                           tile.broadCastMessage(m);
/* 725 */                           this.lastTx = this.owner.getTileX();
/* 726 */                           this.lastTy = this.owner.getTileY();
/* 727 */                           this.lastChattedLocal = System.currentTimeMillis();
/*     */                           return;
/*     */                         } 
/*     */                       } 
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getSayToCreature(Creature creature) {
/* 744 */     int rand = Server.rand.nextInt(100);
/* 745 */     int next = Server.rand.nextInt(100);
/* 746 */     if (creature.getPlayingTime() < 3600000L && next < 5) {
/*     */       
/* 748 */       if (this.owner.isFriendlyKingdom(creature.getKingdomId())) {
/*     */         
/* 750 */         if (rand < 20)
/* 751 */           return "Hey " + creature.getName() + "! Nice to see you! I'll do my best to get the Key of the Heavens before you, you know!"; 
/* 752 */         if (rand < 40)
/* 753 */           return "Oh " + creature.getName() + "! Have you seen the Key of the Heavens? I badly need it."; 
/* 754 */         if (rand < 60)
/* 755 */           return creature.getName() + "! Please give me a Key of the Heavens if you find one."; 
/* 756 */         if (rand < 80)
/* 757 */           return "Please " + creature.getName() + ", I badly need a Key of the Heavens. I'll pay well."; 
/* 758 */         return "Look, a newcomer. " + creature.getName() + ", I bet I find the Key of the Heavens before you do!";
/*     */       } 
/*     */ 
/*     */       
/* 762 */       if (rand < 20)
/* 763 */         return "Hey " + creature.getName() + "! You'll never get the Key of the Heavens you know!"; 
/* 764 */       if (rand < 40)
/* 765 */         return "You! " + creature.getName() + "! Have you seen the Key of the Heavens?"; 
/* 766 */       if (rand < 60)
/* 767 */         return creature.getName() + "! Give me a Key of the Heavens!"; 
/* 768 */       if (rand < 80)
/* 769 */         return "Now " + creature.getName() + ", I will have the Key of the Heavens. No matter the cost."; 
/* 770 */       return "Look, " + creature.getName() + ". You stand no chance of finding the Key of the Heavens before I do!";
/*     */     } 
/*     */     
/* 773 */     if (next < 20) {
/*     */       
/* 775 */       Item[] worn = creature.getBody().getContainersAndWornItems();
/* 776 */       if (worn != null && worn.length > 0) {
/*     */         
/* 778 */         Item selected = worn[Server.rand.nextInt(worn.length)];
/* 779 */         if (this.owner.isFriendlyKingdom(creature.getKingdomId())) {
/*     */           
/* 781 */           if (rand < 20)
/* 782 */             return "Nice " + selected.getName() + " there, " + creature.getName() + "."; 
/* 783 */           if (rand < 40) {
/*     */             
/* 785 */             if (selected.getDamage() > 0.0F)
/* 786 */               return "Hey " + creature.getName() + ". Your " + selected.getName() + " needs repairing."; 
/* 787 */             return "Hey " + creature.getName() + ". Your " + selected.getName() + " looks really nice.";
/*     */           } 
/* 789 */           if (rand < 60) {
/*     */             
/* 791 */             if (selected.getRarity() > 0)
/* 792 */               return "Hey " + creature.getName() + ". Your " + selected.getName() + " looks really special."; 
/* 793 */             return "Nothing special with your " + selected.getName() + " " + creature.getName() + ", is there?";
/*     */           } 
/* 795 */           if (rand < 80) {
/*     */             
/* 797 */             if (selected.isWeapon()) {
/*     */               
/* 799 */               if (selected.getCurrentQualityLevel() > 40.0F)
/* 800 */                 return "That " + selected.getName() + " looks pretty dangerous " + creature.getName() + "."; 
/* 801 */               return "That " + selected.getName() + " doesn't look very dangerous " + creature.getName() + ".";
/*     */             } 
/* 803 */             if (selected.isArmour()) {
/*     */               
/* 805 */               if (selected.getCurrentQualityLevel() > 40.0F)
/* 806 */                 return "That " + selected.getName() + " looks pretty darn good, " + creature.getName() + "."; 
/* 807 */               return "That " + selected.getName() + " looks pretty darn rotten, " + creature.getName() + ".";
/*     */             } 
/* 809 */             if (selected.isFood()) {
/*     */               
/* 811 */               if (selected.getCurrentQualityLevel() > 60.0F)
/* 812 */                 return "That " + selected.getName() + " looks really tasty, " + creature.getName() + "."; 
/* 813 */               return "That " + selected.getName() + " looks pretty awful, " + creature.getName() + ".";
/*     */             } 
/* 815 */             if (selected.isEnchantableJewelry()) {
/*     */               
/* 817 */               if (selected.getCurrentQualityLevel() > 40.0F)
/* 818 */                 return "You should enchant that " + selected.getName() + " you know, " + creature.getName() + ". If you haven't already."; 
/* 819 */               return "That " + selected.getName() + " looks pretty mundane, " + creature.getName() + ".";
/*     */             } 
/*     */           } 
/* 822 */           return "Look, " + creature.getName() + ". If you find a Key of the Heavens just give it to me will you?";
/*     */         } 
/*     */ 
/*     */         
/* 826 */         if (rand < 20)
/* 827 */           return "That " + selected.getName() + " there looks awful, " + creature.getName() + "."; 
/* 828 */         if (rand < 40) {
/*     */           
/* 830 */           if (selected.getDamage() > 0.0F)
/* 831 */             return "Haha " + creature.getName() + ". Your " + selected.getName() + " needs repairing."; 
/* 832 */           return "Well, " + creature.getName() + ". Your " + selected.getName() + " stinks.";
/*     */         } 
/* 834 */         if (rand < 60) {
/*     */           
/* 836 */           if (selected.getRarity() > 0)
/* 837 */             return "Ooh " + creature.getName() + ". I think I'll enjoy your " + selected.getName() + "."; 
/* 838 */           return "Your " + selected.getName() + " will look better with your blood on it, " + creature.getName() + ".";
/*     */         } 
/* 840 */         if (rand < 80) {
/*     */           
/* 842 */           if (selected.isWeapon()) {
/*     */             
/* 844 */             if (selected.getCurrentQualityLevel() > 40.0F)
/* 845 */               return "That " + selected.getName() + " doesn't scare me " + creature.getName() + "."; 
/* 846 */             return "That " + selected.getName() + " is the laughing stock of the lands, " + creature.getName() + ".";
/*     */           } 
/* 848 */           if (selected.isArmour()) {
/*     */             
/* 850 */             if (selected.getCurrentQualityLevel() > 40.0F)
/* 851 */               return "That " + selected.getName() + " won't keep you alive, " + creature.getName() + "."; 
/* 852 */             return "That " + selected.getName() + " is pathetic, " + creature.getName() + ".";
/*     */           } 
/*     */         } 
/* 855 */         return "I'll pry the " + selected.getName() + " from you before your body has gone cold, " + creature.getName() + ".";
/*     */       } 
/*     */       
/* 858 */       if (this.owner.isFriendlyKingdom(creature.getKingdomId()))
/*     */       {
/* 860 */         return "You should get some stuff, " + creature.getName() + ".";
/*     */       }
/*     */       
/* 863 */       return "What a disappointment you are, " + creature.getName() + ".";
/*     */     } 
/* 865 */     if (next < 40) {
/*     */       
/* 867 */       Skill[] skills = creature.getSkills().getSkills();
/* 868 */       if (skills != null && skills.length > 0) {
/*     */         
/* 870 */         if (!this.owner.isFriendlyKingdom(creature.getKingdomId())) {
/*     */           
/* 872 */           Skill skill = skills[Server.rand.nextInt(skills.length)];
/* 873 */           if (skill.getKnowledge() < 50.0D)
/* 874 */             return "Can't say I fear your knowledge in " + skill.getName() + ", " + creature.getName() + "."; 
/* 875 */           return "Your knowledge in " + skill.getName() + " won't save you now, " + creature.getName() + ".";
/*     */         } 
/*     */ 
/*     */         
/* 879 */         Skill selected = skills[Server.rand.nextInt(skills.length)];
/* 880 */         if (selected.getKnowledge() < 50.0D)
/* 881 */           return "How is your knowledge in " + selected.getName() + " now, " + creature.getName() + "?"; 
/* 882 */         return "Word is that you have improved your knowledge in " + selected.getName() + ", " + creature.getName() + ".";
/*     */       } 
/*     */       
/* 885 */       if (this.owner.isFriendlyKingdom(creature.getKingdomId()))
/*     */       {
/* 887 */         return "You should get some skills, " + creature.getName() + ".";
/*     */       }
/*     */       
/* 890 */       return "What a disappointment you are, " + creature.getName() + ".";
/*     */     } 
/* 892 */     if (next < 60) {
/*     */       
/* 894 */       Village[] vills = Villages.getVillages();
/* 895 */       if (vills != null && vills.length > 0) {
/*     */         
/* 897 */         if (this.owner.isFriendlyKingdom(creature.getKingdomId()))
/*     */         {
/* 899 */           return "Have you visited " + vills[Server.rand.nextInt(vills.length)].getName() + " lately " + creature.getName() + "?";
/*     */         }
/*     */         
/* 902 */         return "I'll send you back to " + vills[Server.rand.nextInt(vills.length)].getName() + " " + creature.getName() + "?";
/*     */       } 
/* 904 */       if (this.owner.isFriendlyKingdom(creature.getKingdomId())) {
/* 905 */         return "It's just a big empty nothing out here isn't it, " + creature.getName() + "?";
/*     */       }
/* 907 */       return "It's just a big empty nothingness to die in here isn't it, " + creature.getName() + "?";
/*     */     } 
/* 909 */     if (next < 80) {
/*     */       
/* 911 */       if (this.owner.isFriendlyKingdom(creature.getKingdomId())) {
/* 912 */         return "Did you hear, " + creature.getName() + "? " + EndGameItems.locateRandomEndGameItem(this.owner);
/*     */       }
/* 914 */       return "You'll be dead soon enough, " + creature.getName() + ".";
/*     */     } 
/* 916 */     if (next < 90) {
/*     */       
/* 918 */       if (this.owner.isFriendlyKingdom(creature.getKingdomId())) {
/* 919 */         return "I recently had a vision.. " + Deities.getRandomStatus() + " That made me really afraid.";
/*     */       }
/* 921 */       return "There will be no next time, " + creature.getName() + ".";
/*     */     } 
/* 923 */     if (this.owner.isFriendlyKingdom(creature.getKingdomId())) {
/* 924 */       return "Are you enjoying yourself, " + creature.getName() + "?";
/*     */     }
/* 926 */     return "See you in the Soulfall, " + creature.getName() + "!";
/*     */   }
/*     */   
/* 929 */   public ChatManager(Creature _owner) { this.numchatsSinceLast = 0;
/*     */     this.owner = _owner;
/*     */     this.chattiness = Math.max(1, (int)(this.owner.getWurmId() % 10L)); } private final void pollLocal() {
/* 932 */     Message last = null;
/* 933 */     String mess = null;
/* 934 */     for (Message message : this.unansweredLChats.keySet()) {
/*     */ 
/*     */       
/*     */       try {
/* 938 */         mess = message.getMessage().substring(message.getMessage().indexOf(">") + 1, message.getMessage().length());
/* 939 */         mess = mess.replace(".", "");
/* 940 */         mess = mess.trim();
/* 941 */         if (mess.length() > 0)
/*     */         {
/* 943 */           this.localchats.put(mess, message.getSender().getName());
/* 944 */           this.numchatsSinceLast++;
/* 945 */           last = message;
/*     */         }
/*     */       
/* 948 */       } catch (Exception ex) {
/*     */         
/* 950 */         logger.log(Level.INFO, "Failed chat: " + ex.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 954 */     this.unansweredLChats.clear();
/* 955 */     if (last != null) {
/* 956 */       answerLocalChat(last, mess);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void answerLocalChat(Message message, @Nullable String mess) {
/* 961 */     int chattinessMod = 0;
/* 962 */     boolean toMe = false;
/* 963 */     if (message.getSender() != null && message.getSender().isPlayer()) {
/*     */       
/* 965 */       this.lastPCChattedLocal = System.currentTimeMillis();
/* 966 */       chattinessMod = Server.rand.nextInt(15);
/*     */       
/* 968 */       if (mess != null && mess.toLowerCase().contains(this.owner.getName().toLowerCase()))
/*     */       {
/* 970 */         toMe = true;
/*     */       }
/*     */     } 
/* 973 */     if (toMe || (System.currentTimeMillis() - this.lastChattedLocal > 30000L * Math.max(1, this.chattiness - chattinessMod) && Server.rand.nextBoolean() && this.numchatsSinceLast > 0)) {
/*     */       
/* 975 */       this.lastChattedLocal = System.currentTimeMillis();
/*     */       
/*     */       try {
/* 978 */         if (mess != null) {
/*     */ 
/*     */           
/* 981 */           Message m = new Message(this.owner, (byte)0, ":Local", "<" + this.owner.getName() + "> " + getAnswerToMessage(message.getSender().getName(), mess));
/* 982 */           VolaTile tile = this.owner.getCurrentTile();
/* 983 */           if (tile != null)
/* 984 */             tile.broadCastMessage(m); 
/*     */         } 
/* 986 */         this.numchatsSinceLast = 0;
/*     */       }
/* 988 */       catch (Exception ex) {
/*     */         
/* 990 */         logger.log(Level.INFO, ex.getMessage(), ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addLocalChat(Message message) {
/* 997 */     this.unansweredLChats.put(message, message.getSender().getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\ChatManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
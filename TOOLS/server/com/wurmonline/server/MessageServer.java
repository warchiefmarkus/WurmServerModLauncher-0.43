/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public final class MessageServer
/*     */   implements MiscConstants
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(MessageServer.class.getName());
/*     */   
/*  52 */   private static final Logger chatlogger = Logger.getLogger("Chat");
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static final Queue<Message> MESSAGES = new LinkedBlockingDeque<>();
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
/*     */   static synchronized void initialise() {
/*  72 */     logger.info("Initialising MessageServer");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addMessage(Message message) {
/*  80 */     MESSAGES.add(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void sendMessages() {
/*  88 */     for (Message message : MESSAGES) {
/*     */       
/*  90 */       long senderid = message.getSenderId();
/*  91 */       if (senderid < 0L && message.getSender() != null) {
/*  92 */         senderid = message.getSender().getWurmId();
/*     */       }
/*  94 */       if (message.getType() == 9) {
/*     */         
/*  96 */         chatlogger.log(Level.INFO, "PR-" + message.getMessage());
/*  97 */         Player[] playarr = Players.getInstance().getPlayers();
/*  98 */         for (int x = 0; x < playarr.length; x++) {
/*     */           
/* 100 */           if (playarr[x].mayHearMgmtTalk() && !playarr[x].isIgnored(senderid))
/* 101 */             playarr[x].getCommunicator().sendMessage(message); 
/*     */         }  continue;
/*     */       } 
/* 104 */       if (message.getType() == 10) {
/*     */         
/* 106 */         chatlogger.log(Level.INFO, "SH-" + message.getMessage());
/* 107 */         Player[] playarr = Players.getInstance().getPlayers();
/* 108 */         byte kingdom = message.getSender().getKingdomId();
/* 109 */         for (int x = 0; x < playarr.length; x++) {
/*     */           
/* 111 */           if (playarr[x].isKingdomChat() && !playarr[x].isIgnored(senderid) && (playarr[x]
/* 112 */             .getPower() > 0 || kingdom == playarr[x].getKingdomId()))
/*     */           {
/* 114 */             if (!playarr[x].getCommunicator().isInvulnerable())
/* 115 */               playarr[x].getCommunicator().sendMessage(message);  } 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 119 */       if (message.getType() == 16) {
/*     */         
/* 121 */         chatlogger.log(Level.INFO, "KSH-" + message.getMessage());
/* 122 */         Player[] playarr = Players.getInstance().getPlayers();
/* 123 */         byte kingdom = message.getSenderKingdom();
/* 124 */         for (int x = 0; x < playarr.length; x++) {
/*     */           
/* 126 */           if (playarr[x].isGlobalChat() && ((!playarr[x].isIgnored(senderid) && kingdom == playarr[x]
/* 127 */             .getKingdomId()) || playarr[x].getPower() > 0))
/*     */           {
/* 129 */             if (!playarr[x].getCommunicator().isInvulnerable())
/* 130 */               playarr[x].getCommunicator().sendMessage(message);  } 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 134 */       if (message.getType() == 5) {
/*     */         
/* 136 */         chatlogger.log(Level.INFO, "BR-" + message.getMessage());
/* 137 */         Server.getInstance().twitLocalServer(message.getMessage());
/* 138 */         Player[] playarr = Players.getInstance().getPlayers();
/* 139 */         for (Player lElement : playarr)
/*     */         {
/* 141 */           lElement.getCommunicator().sendMessage(message); } 
/*     */         continue;
/*     */       } 
/* 144 */       if (message.getType() == 1) {
/*     */         
/* 146 */         chatlogger.log(Level.INFO, "ANN-" + message.getMessage());
/* 147 */         Player[] playarr = Players.getInstance().getPlayers();
/* 148 */         for (Player lElement : playarr)
/*     */         {
/* 150 */           lElement.getCommunicator().sendMessage(message); } 
/*     */         continue;
/*     */       } 
/* 153 */       if (message.getType() == 11) {
/*     */         
/* 155 */         chatlogger.log(Level.INFO, "GM-" + message.getMessage());
/* 156 */         Player[] playarr = Players.getInstance().getPlayers();
/* 157 */         for (Player lElement : playarr) {
/*     */           
/* 159 */           if (lElement.mayHearDevTalk())
/* 160 */             lElement.getCommunicator().sendMessage(message); 
/*     */         }  continue;
/*     */       } 
/* 163 */       if (message.getType() == 3) {
/*     */ 
/*     */         
/*     */         try {
/* 167 */           Player p = Players.getInstance().getPlayer(message.getReceiver());
/* 168 */           p.getCommunicator().sendMessage(message);
/*     */         }
/* 170 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 175 */       if (message.getType() == 17) {
/*     */ 
/*     */         
/*     */         try {
/* 179 */           Player p = Players.getInstance().getPlayer(message.getReceiver());
/* 180 */           p.getCommunicator().sendNormalServerMessage(message.getMessage());
/*     */         }
/* 182 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 187 */       if (message.getType() == 18) {
/*     */         
/* 189 */         chatlogger.log(Level.INFO, "TD-" + message.getMessage());
/* 190 */         Player[] playarr = Players.getInstance().getPlayers();
/* 191 */         byte kingdom = message.getSenderKingdom();
/* 192 */         int red = message.getRed();
/* 193 */         int blue = message.getBlue();
/* 194 */         int green = message.getGreen();
/* 195 */         for (int x = 0; x < playarr.length; x++) {
/*     */           
/* 197 */           if (playarr[x].isTradeChannel() && ((!playarr[x].isIgnored(senderid) && kingdom == playarr[x]
/* 198 */             .getKingdomId()) || playarr[x].getPower() > 0)) {
/*     */             
/* 200 */             boolean tell = true;
/* 201 */             if (message.getMessage().contains(") @")) {
/*     */               
/* 203 */               String str = "@" + playarr[x].getName().toLowerCase() + "\\b";
/* 204 */               Pattern pattern1 = Pattern.compile(str);
/* 205 */               Matcher matcher1 = pattern1.matcher(message.getMessage().toLowerCase());
/* 206 */               tell = matcher1.find();
/*     */             } 
/* 208 */             String patternString = "\\b" + playarr[x].getName().toLowerCase() + "\\b";
/* 209 */             Pattern pattern = Pattern.compile(patternString);
/* 210 */             Matcher matcher = pattern.matcher(message.getMessage().toLowerCase());
/* 211 */             if (matcher.find()) {
/*     */               
/* 213 */               message.setColorR(100);
/* 214 */               message.setColorG(170);
/* 215 */               message.setColorB(255);
/*     */             }
/*     */             else {
/*     */               
/* 219 */               message.setColorR(red);
/* 220 */               message.setColorG(green);
/* 221 */               message.setColorB(blue);
/*     */             } 
/* 223 */             if (!playarr[x].getCommunicator().isInvulnerable() && tell)
/* 224 */               playarr[x].getCommunicator().sendMessage(message); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 229 */     MESSAGES.clear();
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
/*     */   public static void broadCastNormal(String message) {
/* 242 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 244 */       logger.fine("Broadcasting Serverwide Normal message: " + message);
/*     */     }
/* 246 */     Player[] playarr = Players.getInstance().getPlayers();
/* 247 */     for (Player lElement : playarr) {
/* 248 */       lElement.getCommunicator().sendNormalServerMessage(message);
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
/*     */   
/*     */   public static void broadCastSafe(String message, byte messageType) {
/* 261 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 263 */       logger.fine("Broadcasting Serverwide Safe message: " + message);
/*     */     }
/* 265 */     Player[] playarr = Players.getInstance().getPlayers();
/* 266 */     for (Player lElement : playarr) {
/* 267 */       lElement.getCommunicator().sendSafeServerMessage(message, messageType);
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
/*     */ 
/*     */   
/*     */   public static void broadCastAlert(String message, byte messageType) {
/* 281 */     logger.info("Broadcasting Serverwide Alert: " + message);
/* 282 */     Player[] playarr = Players.getInstance().getPlayers();
/* 283 */     for (Player lPlayer : playarr) {
/*     */       
/* 285 */       lPlayer.getCommunicator().sendAlertServerMessage(message, messageType);
/*     */       
/* 287 */       if (lPlayer.isFighting()) {
/* 288 */         lPlayer.getCommunicator().sendCombatAlertMessage(message);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void broadCastAction(String message, Creature performer, int tileDist) {
/* 306 */     broadCastAction(message, performer, null, tileDist, false);
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
/*     */   public static void broadCastAction(String message, Creature performer, Creature receiver, int tileDist) {
/* 326 */     broadCastAction(message, performer, receiver, tileDist, false);
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
/*     */   public static void broadCastAction(String message, Creature performer, @Nullable Creature receiver, int tileDist, boolean combat) {
/* 348 */     if (message.length() > 0) {
/*     */       
/* 350 */       int lTileDist = Math.abs(tileDist);
/* 351 */       int tilex = performer.getTileX();
/* 352 */       int tiley = performer.getTileY();
/* 353 */       for (int x = tilex - lTileDist; x <= tilex + lTileDist; x++) {
/*     */         
/* 355 */         for (int y = tiley - lTileDist; y <= tiley + lTileDist; y++) {
/*     */ 
/*     */           
/*     */           try {
/* 359 */             Zone zone = Zones.getZone(x, y, performer.isOnSurface());
/* 360 */             VolaTile tile = zone.getTileOrNull(x, y);
/* 361 */             if (tile != null) {
/* 362 */               tile.broadCastAction(message, performer, receiver, combat);
/*     */             }
/* 364 */           } catch (NoSuchZoneException noSuchZoneException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void broadcastColoredAction(List<MulticolorLineSegment> segments, Creature performer, int tileDist, boolean combat) {
/* 375 */     broadcastColoredAction(segments, performer, null, tileDist, combat, (byte)0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void broadcastColoredAction(List<MulticolorLineSegment> segments, Creature performer, @Nullable Creature receiver, int tileDist, boolean combat) {
/* 381 */     broadcastColoredAction(segments, performer, receiver, tileDist, combat, (byte)0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void broadcastColoredAction(List<MulticolorLineSegment> segments, Creature performer, @Nullable Creature receiver, int tileDist, boolean combat, byte onScreenMessage) {
/* 387 */     if (segments == null || segments.isEmpty()) {
/*     */       return;
/*     */     }
/* 390 */     int lTileDist = Math.abs(tileDist);
/* 391 */     int tilex = performer.getTileX();
/* 392 */     int tiley = performer.getTileY();
/* 393 */     for (int x = tilex - lTileDist; x <= tilex + lTileDist; x++) {
/*     */       
/* 395 */       for (int y = tiley - lTileDist; y <= tiley + lTileDist; y++) {
/*     */ 
/*     */         
/*     */         try {
/* 399 */           Zone zone = Zones.getZone(x, y, performer.isOnSurface());
/* 400 */           VolaTile tile = zone.getTileOrNull(x, y);
/* 401 */           if (tile != null) {
/* 402 */             tile.broadCastMulticolored(segments, performer, receiver, combat, onScreenMessage);
/*     */           }
/* 404 */         } catch (NoSuchZoneException noSuchZoneException) {}
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
/*     */   public static void broadCastMessage(String message, int tilex, int tiley, boolean surfaced, int tiledistance) {
/* 431 */     if (message.length() > 0)
/*     */     {
/* 433 */       for (int x = tilex - tiledistance; x <= tilex + tiledistance; x++) {
/*     */         
/* 435 */         for (int y = tiley - tiledistance; y <= tiley + tiledistance; y++) {
/*     */ 
/*     */           
/*     */           try {
/* 439 */             Zone zone = Zones.getZone(x, y, surfaced);
/* 440 */             VolaTile tile = zone.getTileOrNull(x, y);
/* 441 */             if (tile != null) {
/* 442 */               tile.broadCast(message);
/*     */             }
/* 444 */           } catch (NoSuchZoneException noSuchZoneException) {}
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\MessageServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
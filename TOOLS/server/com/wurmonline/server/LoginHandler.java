/*      */ package com.wurmonline.server;
/*      */ 
/*      */ import com.wurmonline.communication.SimpleConnectionListener;
/*      */ import com.wurmonline.communication.SocketConnection;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*      */ import com.wurmonline.server.behaviours.Seat;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.VehicleBehaviour;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.BodyTemplate;
/*      */ import com.wurmonline.server.bodys.Wounds;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.MountAction;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.epic.ValreiMapData;
/*      */ import com.wurmonline.server.intra.MountTransfer;
/*      */ import com.wurmonline.server.intra.PlayerTransfer;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Abilities;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.players.Ban;
/*      */ import com.wurmonline.server.players.HackerIp;
/*      */ import com.wurmonline.server.players.ItemBonus;
/*      */ import com.wurmonline.server.players.KingdomIp;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerCommunicator;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.Spawnpoint;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.questions.SelectSpawnQuestion;
/*      */ import com.wurmonline.server.skills.AffinitiesTimed;
/*      */ import com.wurmonline.server.steam.SteamId;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.tutorial.MissionPerformed;
/*      */ import com.wurmonline.server.tutorial.MissionPerformer;
/*      */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*      */ import com.wurmonline.server.utils.ProtocolUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.webinterface.WCGmMessage;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.constants.PlayerOnlineStatus;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigInteger;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.spec.InvalidKeySpecException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.crypto.SecretKeyFactory;
/*      */ import javax.crypto.spec.PBEKeySpec;
/*      */ import sun.misc.BASE64Encoder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class LoginHandler
/*      */   implements SimpleConnectionListener, TimeConstants, MiscConstants, CreatureTemplateIds, ProtoConstants, CounterTypes
/*      */ {
/*      */   private final SocketConnection conn;
/*  129 */   private static Logger logger = Logger.getLogger(LoginHandler.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String legalChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int DISCONNECT_TICKS = 400;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  157 */   private int loadedItems = 0;
/*      */   
/*      */   private boolean redirected = false;
/*      */   
/*      */   private static final byte clientDevLevel = 2;
/*      */   
/*  163 */   static int redirects = 0;
/*      */   
/*  165 */   static int logins = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String BROKEN_PLAYER_MODEL = "model.player.broken";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String MESSAGE_FORMAT_UTF_8 = "UTF-8";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String PROBLEM_SENDING_LOGIN_DENIED_MESSAGE = ", problem sending login denied message: ";
/*      */ 
/*      */ 
/*      */   
/*  181 */   public static final Map<String, HackerIp> failedIps = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAX_REAL_DEATHS = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAX_NAME_LENGTH = 40;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MIN_NAME_LENGTH = 3;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int ITERATIONS = 1000;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int KEY_LENGTH = 192;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoginHandler(SocketConnection aConn) {
/*  209 */     this.conn = aConn;
/*  210 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  212 */       logger.finer("Creating LoginHandler for SocketConnection " + aConn);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean containsIllegalCharacters(String name) {
/*  228 */     char[] chars = name.toCharArray();
/*      */     
/*  230 */     for (char lC : chars) {
/*      */       
/*  232 */       if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(lC) < 0)
/*  233 */         return true; 
/*      */     } 
/*  235 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String raiseFirstLetter(String oldString) {
/*  248 */     if (oldString.length() == 0)
/*  249 */       return oldString; 
/*  250 */     String lOldString = oldString.toLowerCase();
/*  251 */     String firstLetter = lOldString.substring(0, 1).toUpperCase();
/*  252 */     String newString = firstLetter + lOldString.substring(1, lOldString.length());
/*  253 */     return newString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reallyHandle(int num, ByteBuffer byteBuffer) {
/*  264 */     short cmd = (short)byteBuffer.get();
/*  265 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/*  267 */       logger.finest("Handling block with Command: " + cmd + ", " + ProtocolUtilities.getDescriptionForCommand((byte)cmd));
/*      */     }
/*  269 */     if (cmd == -15 || cmd == 23) {
/*      */ 
/*      */       
/*  272 */       int protocolVersion = byteBuffer.getInt();
/*      */ 
/*      */       
/*  275 */       if (protocolVersion != 250990585) {
/*      */         
/*  277 */         String message = "Incompatible communication protocol.\nPlease update the client at http://www.wurmonline.com or wait for the server to be updated.";
/*  278 */         logger.log(Level.INFO, "Rejected protocol " + protocolVersion + ". Mine=" + 250990585 + ", (" + "0xEF5CFF9s" + ") " + this.conn);
/*      */ 
/*      */         
/*      */         try {
/*  282 */           sendLoginAnswer(false, "Incompatible communication protocol.\nPlease update the client at http://www.wurmonline.com or wait for the server to be updated.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */         }
/*  284 */         catch (IOException ioe) {
/*      */ 
/*      */           
/*  287 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/*  289 */             logger.log(Level.FINE, this.conn.getIp() + ", problem sending login denied message: " + "Incompatible communication protocol.\nPlease update the client at http://www.wurmonline.com or wait for the server to be updated.", ioe);
/*      */           }
/*      */         } 
/*      */         return;
/*      */       } 
/*  294 */       String name = getNextString(byteBuffer, "name", true);
/*  295 */       name = raiseFirstLetter(name);
/*      */       
/*  297 */       String password = getNextString(byteBuffer, "password for " + name, false);
/*      */       
/*  299 */       String serverPassword = getNextString(byteBuffer, "server password for " + name, false);
/*      */       
/*  301 */       String steamIDAsString = getNextString(byteBuffer, "steamid for " + name, false);
/*      */       
/*  303 */       boolean sendExtraBytes = false;
/*  304 */       if (byteBuffer.hasRemaining())
/*      */       {
/*  306 */         sendExtraBytes = (byteBuffer.get() != 0);
/*      */       }
/*  308 */       if (cmd == 23)
/*      */       {
/*  310 */         reconnect(name, password, false, serverPassword, steamIDAsString);
/*      */       }
/*      */       else
/*      */       {
/*  314 */         login(name, password, sendExtraBytes, false, serverPassword, steamIDAsString);
/*      */       }
/*      */     
/*  317 */     } else if (cmd == -52) {
/*      */ 
/*      */       
/*      */       try {
/*  321 */         String steamIDAsString = getNextString(byteBuffer, "steamid", false);
/*      */         
/*  323 */         long authTicket = byteBuffer.getLong();
/*  324 */         int arrayLenght = byteBuffer.getInt();
/*      */         
/*  326 */         byte[] ticketArray = new byte[arrayLenght];
/*  327 */         for (int i = 0; i < ticketArray.length; i++)
/*      */         {
/*  329 */           ticketArray[i] = byteBuffer.get();
/*      */         }
/*      */         
/*  332 */         long tokenLen = byteBuffer.getLong();
/*      */ 
/*      */ 
/*      */         
/*  336 */         if ((Server.getInstance()).steamHandler.getIsOfflineServer()) {
/*      */           
/*  338 */           (Server.getInstance()).steamHandler.setIsPlayerAuthenticated(steamIDAsString);
/*  339 */           sendAuthenticationAnswer(true, "");
/*      */           
/*      */           return;
/*      */         } 
/*  343 */         boolean wasAddedBeforeWithSameIp = (Server.getInstance()).steamHandler.addLoginHandler(steamIDAsString, this);
/*      */         
/*  345 */         int authenticationResult = (Server.getInstance()).steamHandler.BeginAuthSession(steamIDAsString, ticketArray, tokenLen);
/*      */ 
/*      */         
/*  348 */         if (authenticationResult != 0)
/*      */         {
/*  350 */           if (authenticationResult == 2) {
/*      */ 
/*      */             
/*  353 */             if (!wasAddedBeforeWithSameIp)
/*      */             {
/*  355 */               logger.log(Level.INFO, "Duplicate authentication");
/*  356 */               sendAuthenticationAnswer(false, "Duplicate authentication");
/*      */             }
/*      */             else
/*      */             {
/*  360 */               (Server.getInstance()).steamHandler.setIsPlayerAuthenticated(steamIDAsString);
/*  361 */               sendAuthenticationAnswer(true, "");
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  366 */             logger.log(Level.INFO, "Steam could not authenticate the user");
/*  367 */             sendAuthenticationAnswer(false, "Steam could not authenticate the user");
/*      */           }
/*      */         
/*      */         }
/*  371 */       } catch (Throwable t) {
/*      */         
/*  373 */         logger.log(Level.SEVERE, "Error while authenticating the user with steam.");
/*  374 */         sendAuthenticationAnswer(false, "Error while authenticating the user with steam.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getNextString(ByteBuffer byteBuffer, String name, boolean logValue) {
/*      */     String decoded;
/*  390 */     byte[] bytes = new byte[byteBuffer.get() & 0xFF];
/*  391 */     byteBuffer.get(bytes);
/*      */ 
/*      */     
/*      */     try {
/*  395 */       decoded = new String(bytes, "UTF-8");
/*      */     }
/*  397 */     catch (UnsupportedEncodingException nse) {
/*      */       
/*  399 */       decoded = new String(bytes);
/*  400 */       String logMessage = "Unsupported encoding for " + (logValue ? (name + ": " + decoded) : name);
/*  401 */       logger.log(Level.WARNING, logMessage, nse);
/*      */     } 
/*  403 */     return decoded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void login(String name, String password, boolean sendExtraBytes, boolean isUndead, String serverPassword, String steamIDAsString) {
/*      */     try {
/*  422 */       password = hashPassword(password, encrypt(raiseFirstLetter(name)));
/*      */     }
/*  424 */     catch (Exception ex) {
/*      */       
/*  426 */       logger.log(Level.SEVERE, name + " Failed to encrypt password", ex);
/*  427 */       String message = "We failed to encrypt your password. Please try another.";
/*      */       
/*      */       try {
/*  430 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  431 */         sendLoginAnswer(false, "We failed to encrypt your password. Please try another.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */       }
/*  433 */       catch (IOException ioe) {
/*      */ 
/*      */         
/*  436 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  438 */           logger.log(Level.FINE, this.conn.getIp() + ", problem sending login denied message: " + "We failed to encrypt your password. Please try another.", ioe);
/*      */         }
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     try {
/*  445 */       if (Servers.localServer.LOGINSERVER || Constants.maintaining || isUndead || Servers.localServer.testServer) {
/*  446 */         handleLogin(name, password, sendExtraBytes, false, false, isUndead, serverPassword, steamIDAsString);
/*      */       } else {
/*      */         
/*  449 */         logger.log(Level.WARNING, name + " logging in directly! Rejected.");
/*  450 */         String message = "You need to connect to the login server.";
/*      */         
/*      */         try {
/*  453 */           (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  454 */           sendLoginAnswer(false, "You need to connect to the login server.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */         }
/*  456 */         catch (IOException ioe) {
/*      */ 
/*      */           
/*  459 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/*  461 */             logger.log(Level.FINE, this.conn.getIp() + ", problem sending login denied message: " + "You need to connect to the login server.", ioe);
/*      */           }
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*  467 */     } catch (Exception ex) {
/*      */       
/*  469 */       logger.log(Level.SEVERE, "Failed to log " + name + " due to an Exception: " + ex.getMessage(), ex);
/*  470 */       String message = "We failed to log you in. " + ex.getMessage();
/*      */       
/*      */       try {
/*  473 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  474 */         sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */       }
/*  476 */       catch (IOException ioe) {
/*      */ 
/*      */         
/*  479 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  481 */           logger.log(Level.FINE, this.conn.getIp() + ", problem sending login denied message: " + message, ioe);
/*      */         }
/*      */       } 
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reconnect(String name, String sessionkey, boolean isUndead, String serverPassword, String steamIDAsString) {
/*  503 */     this.redirected = true;
/*      */     
/*      */     try {
/*  506 */       handleLogin(name, sessionkey, false, false, true, isUndead, serverPassword, steamIDAsString);
/*      */ 
/*      */     
/*      */     }
/*  510 */     catch (Exception ex) {
/*      */       
/*  512 */       logger.log(Level.SEVERE, name + " " + ex.getMessage(), ex);
/*  513 */       String message = "We failed to log you in. " + ex.getMessage();
/*      */       
/*      */       try {
/*  516 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  517 */         sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */       }
/*  519 */       catch (IOException ioe) {
/*      */ 
/*      */         
/*  522 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  524 */           logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + message, ioe);
/*      */         }
/*      */       } 
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean preValidateLogin(String name, String steamIDAsString) {
/*  550 */     if (Server.getInstance().isLagging()) {
/*      */       
/*  552 */       logger.log(Level.INFO, "Refusing connection due to lagging server for " + name);
/*  553 */       String message = "The server is lagging. Retrying in 20 seconds.";
/*      */       
/*      */       try {
/*  556 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  557 */         sendLoginAnswer(false, "The server is lagging. Retrying in 20 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 20);
/*      */       }
/*  559 */       catch (IOException ioe) {
/*      */ 
/*      */         
/*  562 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  564 */           logger.log(Level.FINE, this.conn.getIp() + ", problem sending login denied message: " + "The server is lagging. Retrying in 20 seconds.", ioe);
/*      */         }
/*      */       } 
/*  567 */       return false;
/*      */     } 
/*  569 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleLogin(String name, String password, boolean sendExtraBytes, boolean usingWeb, boolean reconnecting, boolean isUndead, String serverPassword, String steamIDAsString) {
/*  589 */     if (!preValidateLogin(name, steamIDAsString)) {
/*      */       return;
/*      */     }
/*  592 */     String hashedSteamId = steamIDAsString;
/*      */     
/*      */     try {
/*  595 */       hashedSteamId = hashPassword(hashedSteamId, encrypt(raiseFirstLetter(name)));
/*      */     }
/*  597 */     catch (Exception ex) {
/*      */       
/*  599 */       logger.log(Level.SEVERE, name + " Failed to encrypt password", ex);
/*  600 */       String message = "We failed to encrypt your password. Please try another.";
/*      */       
/*      */       try {
/*  603 */         (Server.getInstance()).steamHandler.removeIsPlayerAuthenticated(steamIDAsString);
/*  604 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  605 */         sendLoginAnswer(false, "We failed to encrypt your password. Please try another.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */       }
/*  607 */       catch (IOException ioe) {
/*      */ 
/*      */         
/*  610 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  612 */           logger.log(Level.FINE, this.conn.getIp() + ", problem sending login denied message: " + "We failed to encrypt your password. Please try another.", ioe);
/*      */         }
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  619 */     if (!(Server.getInstance()).steamHandler.isPlayerAuthenticated(steamIDAsString)) {
/*      */ 
/*      */       
/*  622 */       (Server.getInstance()).steamHandler.removeIsPlayerAuthenticated(steamIDAsString);
/*      */ 
/*      */       
/*      */       try {
/*  626 */         String message = "You need to be authenticated";
/*  627 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  628 */         sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -2);
/*      */       }
/*  630 */       catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  635 */       if (!password.equals(hashedSteamId)) {
/*      */         
/*  637 */         logger.log(Level.INFO, "Unauthenticated user trying to login with incorrect credentials, with ip: " + this.conn.getIp());
/*      */       }
/*      */       else {
/*      */         
/*  641 */         logger.log(Level.INFO, "Unauthenticated user trying to login, with ip: " + this.conn.getIp());
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  647 */     (Server.getInstance()).steamHandler.removeIsPlayerAuthenticated(steamIDAsString);
/*      */     
/*  649 */     if (!password.equals(hashedSteamId)) {
/*      */ 
/*      */       
/*      */       try {
/*  653 */         String message = "You need to be authenticated";
/*  654 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  655 */         sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -2);
/*      */       }
/*  657 */       catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */       
/*  661 */       logger.log(Level.INFO, "Authenticated user trying to login with incorrect credentials, with ip: " + this.conn.getIp());
/*      */       
/*      */       return;
/*      */     } 
/*  665 */     String steamServerPassword = Servers.localServer.getSteamServerPassword();
/*      */     
/*  667 */     if (!steamServerPassword.equals(serverPassword)) {
/*      */ 
/*      */       
/*      */       try {
/*  671 */         String message = "Incorrect server password!";
/*  672 */         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  673 */         sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -2);
/*      */       
/*      */       }
/*  676 */       catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */       
/*  680 */       logger.log(Level.INFO, "Incorrect server password: " + this.conn.getIp());
/*      */       
/*      */       return;
/*      */     } 
/*  684 */     if (checkName(name)) {
/*      */       
/*  686 */       if (isUndead && !Servers.localServer.LOGINSERVER)
/*      */       {
/*  688 */         name = "Undead " + name;
/*      */       }
/*  690 */       boolean wasinvuln = false;
/*      */       
/*      */       try {
/*  693 */         Player p = Players.getInstance().getPlayer(name);
/*  694 */         p.setSteamID(SteamId.fromSteamID64(Long.valueOf(steamIDAsString).longValue()));
/*      */         
/*  696 */         String dbpassw = "";
/*  697 */         dbpassw = p.getSaveFile().getPassword();
/*  698 */         if (!reconnecting)
/*      */         {
/*  700 */           p.setSendExtraBytes(sendExtraBytes);
/*      */         }
/*      */         
/*  703 */         if (dbpassw.equals(password) || sendExtraBytes) {
/*      */           
/*  705 */           Ban ban = Players.getInstance().getAnyBan(this.conn.getIp(), p, steamIDAsString);
/*  706 */           if (ban != null) {
/*      */             
/*  708 */             String time = Server.getTimeFor(ban.getExpiry() - System.currentTimeMillis());
/*      */             
/*  710 */             String message = ban.getIdentifier() + " is banned for " + time + " more. Reason: " + ban.getReason();
/*  711 */             if (ban.getExpiry() - System.currentTimeMillis() > 29030400000L) {
/*  712 */               message = ban.getIdentifier() + " is permanently banned. Reason: " + ban.getReason();
/*      */             }
/*      */             try {
/*  715 */               (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  716 */               sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */             }
/*  718 */             catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */             
/*  722 */             logger.log(Level.INFO, name + " is banned, trying to log on from " + this.conn.getIp());
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  732 */           if (!p.isFullyLoaded() || p.isLoggedOut()) {
/*      */ 
/*      */             
/*      */             try {
/*  736 */               Zone zone = Zones.getZone(p.getTileX(), p.getTileY(), p.isOnSurface());
/*  737 */               zone.deleteCreature((Creature)p, true);
/*      */             }
/*  739 */             catch (NoSuchZoneException|NoSuchCreatureException|NoSuchPlayerException noSuchZoneException) {}
/*      */ 
/*      */             
/*  742 */             Players.getInstance().logoutPlayer(p);
/*  743 */             logger.log(Level.INFO, this.conn.getIp() + "," + name + " logged on too early after reconnecting.");
/*      */             
/*      */             return;
/*      */           } 
/*  747 */           if (p.getCommunicator().getCurrentmove() != null && p.getCommunicator().getCurrentmove().getNext() != null) {
/*      */             
/*  749 */             logger.log(Level.INFO, this.conn.getIp() + "," + name + " was still moving at reconnect - " + p.getCommunicator().getMoves());
/*  750 */             String message = "You are still moving on the server. Retry in 10 seconds.";
/*      */             
/*      */             try {
/*  753 */               (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  754 */               sendLoginAnswer(false, "You are still moving on the server. Retry in 10 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 10);
/*      */             }
/*  756 */             catch (IOException ioe) {
/*      */ 
/*      */               
/*  759 */               if (logger.isLoggable(Level.FINE))
/*      */               {
/*  761 */                 logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "You are still moving on the server. Retry in 10 seconds.", ioe);
/*      */               }
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*  767 */           if ((p.getSaveFile()).realdeath > 4) {
/*      */             
/*  769 */             String message = "Your account has suffered real death. You can not log on.";
/*      */             
/*      */             try {
/*  772 */               (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  773 */               sendLoginAnswer(false, "Your account has suffered real death. You can not log on.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */             }
/*  775 */             catch (IOException ioe) {
/*      */ 
/*      */               
/*  778 */               if (logger.isLoggable(Level.FINE))
/*      */               {
/*  780 */                 logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "Your account has suffered real death. You can not log on.", ioe);
/*      */               }
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*  786 */           logger.log(Level.INFO, this.conn.getIp() + "," + name + " successfully reconnected.");
/*  787 */           wasinvuln = p.getCommunicator().isInvulnerable();
/*  788 */           p.getCommunicator().sendShutDown("Reconnected", true);
/*      */ 
/*      */           
/*  791 */           p.stopTeleporting();
/*  792 */           p.getMovementScheme().clearIntraports();
/*  793 */           (p.getCommunicator()).player = null;
/*  794 */           p.setCommunicator((Communicator)new PlayerCommunicator(p, this.conn));
/*  795 */           this.conn.setLogin(true);
/*  796 */           if ((p.getSaveFile()).currentServer != Servers.localServer.id) {
/*      */ 
/*      */             
/*  799 */             p.getSaveFile().setLogin();
/*  800 */             p.getSaveFile().logout();
/*      */             
/*  802 */             String message = "Failed to redirect to another server.";
/*  803 */             logger.log(Level.INFO, this.conn.getIp() + "," + name + " redirected from " + Servers.localServer.id + " to " + 
/*  804 */                 (p.getSaveFile()).currentServer);
/*      */             
/*      */             try {
/*  807 */               ServerEntry entry = Servers.getServerWithId((p.getSaveFile()).currentServer);
/*      */               
/*  809 */               if (entry != null) {
/*      */                 
/*  811 */                 if (entry.isAvailable(p.getPower(), p.isReallyPaying()))
/*      */                 {
/*  813 */                   p.getCommunicator().sendReconnect(entry.EXTERNALIP, Integer.parseInt(entry.EXTERNALPORT), password);
/*      */                 
/*      */                 }
/*      */                 else
/*      */                 {
/*  818 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  819 */                   message = "The server is currently not available. Please try later.";
/*  820 */                   sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 300);
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/*  825 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  826 */                 sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */               }
/*      */             
/*  829 */             } catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */             
/*  833 */             Players.getInstance().logoutPlayer(p);
/*  834 */             redirects++;
/*  835 */             this.conn.ticksToDisconnect = 400;
/*      */             return;
/*      */           } 
/*  838 */           logins++;
/*  839 */           p.setLoginHandler(this);
/*  840 */           this.conn.setConnectionListener((SimpleConnectionListener)p.getCommunicator());
/*  841 */           Server.getInstance().addIp(this.conn.getIp());
/*  842 */           p.setIpaddress(this.conn.getIp());
/*  843 */           if (p.isTransferring()) {
/*      */             
/*  845 */             String message = "You are being transferred to another server.";
/*      */             
/*      */             try {
/*  848 */               (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  849 */               sendLoginAnswer(false, "You are being transferred to another server.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */             }
/*  851 */             catch (IOException ioe) {
/*      */ 
/*      */               
/*  854 */               if (logger.isLoggable(Level.FINE))
/*      */               {
/*  856 */                 logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "You are being transferred to another server.", ioe);
/*      */               }
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*  862 */           if (p.getPower() < 1 && !p.isPaying() && 
/*  863 */             Players.getInstance().numberOfPlayers() > Servers.localServer.pLimit) {
/*      */             
/*  865 */             String message = "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.";
/*      */             
/*      */             try {
/*  868 */               (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  869 */               sendLoginAnswer(false, "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */             }
/*  871 */             catch (IOException ioe) {
/*      */ 
/*      */               
/*  874 */               if (logger.isLoggable(Level.FINE))
/*      */               {
/*  876 */                 logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.", ioe);
/*      */               }
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*  882 */           if (Constants.maintaining)
/*      */           {
/*  884 */             if (p.getPower() <= 1) {
/*      */               
/*  886 */               String message = "The server is in maintenance mode. Retrying in 60 seconds.";
/*      */               
/*      */               try {
/*  889 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*  890 */                 sendLoginAnswer(false, "The server is in maintenance mode. Retrying in 60 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */               }
/*  892 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/*  895 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/*  897 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "The server is in maintenance mode. Retrying in 60 seconds.", ioe);
/*      */                 }
/*      */               } 
/*      */               
/*      */               return;
/*      */             } 
/*      */           }
/*  904 */           p.setLink(true);
/*      */           
/*  906 */           if (!p.isDead())
/*      */           {
/*  908 */             p.setNewTile(null, 0.0F, true);
/*      */           }
/*  910 */           if (p.isTeleporting()) {
/*  911 */             p.cancelTeleport();
/*      */           }
/*  913 */           putOutsideWall(p);
/*  914 */           if (p.isOnSurface()) {
/*      */             
/*  916 */             putOutsideFence(p);
/*  917 */             if (!p.isDead() && creatureIsInsideWrongHouse(p, false))
/*      */             {
/*  919 */               putOutsideHouse(p, false);
/*      */             }
/*      */           } 
/*  922 */           p.sendAddChampionPoints();
/*  923 */           p.getCommunicator().sendWeather();
/*  924 */           p.getCommunicator().checkSendWeather();
/*  925 */           p.getCommunicator().sendSleepInfo();
/*  926 */           if (!p.isDead()) {
/*      */             
/*  928 */             putInBoatAndAssignSeat(p, true);
/*      */             
/*      */             try {
/*  931 */               Zone zone = Zones.getZone(p.getTileX(), p.getTileY(), p.isOnSurface());
/*  932 */               zone.addCreature(p.getWurmId());
/*      */             }
/*  934 */             catch (NoSuchZoneException nsz) {
/*      */               
/*  936 */               logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*  937 */               p.logoutIn(2, "You were out of bounds.");
/*      */               
/*      */               return;
/*  940 */             } catch (NoSuchCreatureException|NoSuchPlayerException e) {
/*      */               
/*  942 */               logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*  943 */               p.logoutIn(2, "A server error occurred.");
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */           try {
/*  949 */             String message = "Reconnecting " + name + "! " + (Servers.localServer.hasMotd() ? Servers.localServer.getMotd() : Constants.motd);
/*      */ 
/*      */             
/*  952 */             float posx = p.getStatus().getPositionX();
/*  953 */             float posy = p.getStatus().getPositionY();
/*  954 */             byte commandType = 0;
/*  955 */             if (p.isVehicleCommander()) {
/*      */               
/*  957 */               Vehicle vehic = Vehicles.getVehicleForId(p.getVehicle());
/*  958 */               if (vehic != null) {
/*      */                 
/*  960 */                 Seat s = vehic.getPilotSeat();
/*  961 */                 if (s != null && s.occupant == p.getWurmId()) {
/*      */                   
/*  963 */                   float posz = p.getStatus().getPositionZ();
/*      */                   
/*      */                   try {
/*  966 */                     VolaTile tile = Zones.getOrCreateTile((int)(p.getPosX() / 4.0F), 
/*  967 */                         (int)(p.getPosY() / 4.0F), (p.getLayer() >= 0));
/*  968 */                     boolean skipSetZ = false;
/*  969 */                     if (tile != null) {
/*      */                       
/*  971 */                       Structure structure = tile.getStructure();
/*  972 */                       if (structure != null)
/*      */                       {
/*  974 */                         skipSetZ = (structure.isTypeHouse() || structure.getWurmId() == p.getBridgeId());
/*      */                       }
/*      */                     } 
/*      */                     
/*  978 */                     if (!skipSetZ)
/*      */                     {
/*  980 */                       posz = Zones.calculateHeight(p.getStatus().getPositionX(), p
/*  981 */                           .getStatus().getPositionY(), p
/*  982 */                           .isOnSurface());
/*      */                     }
/*  984 */                     if (posz < 0.0F)
/*  985 */                       posz = Math.max(-1.45F, posz); 
/*  986 */                     p.getStatus().setPositionZ(posz);
/*      */                   }
/*  988 */                   catch (NoSuchZoneException noSuchZoneException) {}
/*      */ 
/*      */ 
/*      */                   
/*  992 */                   commandType = vehic.commandType;
/*      */ 
/*      */ 
/*      */                   
/*  996 */                   posz = Math.max(posz + s.offz, s.offz);
/*  997 */                   posy += s.offy;
/*  998 */                   posx += s.offx;
/*  999 */                   p.getStatus().setPositionZ(posz);
/*      */                 } 
/*      */               } 
/*      */             } 
/* 1003 */             p.getMovementScheme().setPosition(posx, posy, p.getStatus().getPositionZ(), p
/* 1004 */                 .getStatus().getRotation(), p.isOnSurface() ? 0 : -1);
/* 1005 */             VolaTile targetTile = Zones.getTileOrNull((int)(posx / 4.0F), (int)(posy / 4.0F), p.isOnSurface());
/* 1006 */             if (targetTile != null) {
/*      */               
/* 1008 */               float height = (p.getFloorLevel() > 0) ? (p.getFloorLevel() * 3) : 0.0F;
/*      */ 
/*      */               
/* 1011 */               if (p.getBridgeId() > 0L)
/*      */               {
/* 1013 */                 height = 0.0F;
/*      */               }
/*      */               
/* 1016 */               p.getMovementScheme().setGroundOffset((int)(height * 10.0F), true);
/* 1017 */               p.calculateFloorLevel(targetTile, true);
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1024 */             p.getMovementScheme().haltSpeedModifier();
/* 1025 */             p.setTeleporting(true);
/* 1026 */             p.setTeleportCounter(p.getTeleportCounter() + 1);
/*      */             
/* 1028 */             byte power = Players.isArtist(p.getWurmId(), false, false) ? 2 : (byte)p.getPower();
/* 1029 */             sendLoginAnswer(true, message, p.getStatus().getPositionX(), p.getStatus().getPositionY(), p
/* 1030 */                 .getStatus().getPositionZ(), p.getStatus().getRotation(), p.isOnSurface() ? 0 : -1, p
/* 1031 */                 .getModelName(), power, 0, commandType, p
/* 1032 */                 .getKingdomTemplateId(), p.getFace(), p.getTeleportCounter(), p.getBlood(), p
/* 1033 */                 .getBridgeId(), p.getMovementScheme().getGroundOffset());
/* 1034 */             if (logger.isLoggable(Level.FINE))
/*      */             {
/* 1036 */               logger.log(Level.FINE, "Sent " + p.getStatus().getPositionX() + "," + p.getStatus().getPositionY() + "," + p
/* 1037 */                   .getStatus().getPositionZ() + "," + p.getStatus().getRotation());
/*      */             }
/*      */           }
/* 1040 */           catch (IOException ioe) {
/*      */             
/* 1042 */             logger.log(Level.INFO, "Player " + name + " dropped during login.", ioe);
/* 1043 */             p.logoutIn(2, "You seemed to have lost your connection to Wurm.");
/*      */             
/*      */             return;
/*      */           } 
/* 1047 */           Server.getInstance().addToPlayersAtLogin(p);
/*      */           
/*      */           try {
/* 1050 */             p.loadSkills();
/* 1051 */             p.sendSkills();
/* 1052 */             sendAllItemModelNames(p);
/* 1053 */             sendAllEquippedArmor(p);
/* 1054 */             if (p.getStatus().getBody().getBodyItem() != null)
/* 1055 */               p.getStatus().getBody().getBodyItem().addWatcher(-1L, (Creature)p); 
/* 1056 */             p.getInventory().addWatcher(-1L, (Creature)p);
/* 1057 */             p.getBody().sendWounds();
/* 1058 */             Players.loadAllPrivatePOIForPlayer(p);
/* 1059 */             p.sendAllMapAnnotations();
/* 1060 */             p.resetLastSentToolbelt();
/* 1061 */             ValreiMapData.sendAllMapData(p);
/*      */           }
/* 1063 */           catch (Exception ex) {
/*      */             
/* 1065 */             logger.log(Level.SEVERE, "Failed to load status for player " + name + ".", ex);
/* 1066 */             p.getCommunicator().sendAlertServerMessage("Failed to load your status! Please contact server administrators.");
/*      */             
/* 1068 */             p.logoutIn(2, "The game failed to load your status. Please contact server administrators.");
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/* 1073 */           p.sendReligion();
/* 1074 */           p.sendKarma();
/* 1075 */           p.sendScenarioKarma();
/* 1076 */           if (p.getTeam() != null)
/* 1077 */             p.getTeam().creatureReconnectedTeam((Creature)p); 
/* 1078 */           p.lastSentHasCompass = false;
/* 1079 */           Players.getInstance().sendReconnect(p);
/*      */           
/*      */           try {
/* 1082 */             p.sendActionControl(p.getCurrentAction().getActionString(), true, p
/* 1083 */                 .getCurrentAction().getTimeLeft());
/*      */           }
/* 1085 */           catch (NoSuchActionException noSuchActionException) {}
/*      */ 
/*      */ 
/*      */           
/* 1089 */           if (p.getSpellEffects() != null)
/* 1090 */             p.getSpellEffects().sendAllSpellEffects(); 
/* 1091 */           ItemBonus.sendAllItemBonusToPlayer(p);
/* 1092 */           p.sendSpellResistances();
/* 1093 */           p.getCommunicator().sendClimb(p.isClimbing());
/* 1094 */           p.getCommunicator().sendToggle(0, p.isClimbing());
/* 1095 */           p.getCommunicator().sendToggle(2, p.isLegal());
/* 1096 */           p.getCommunicator().sendToggle(1, p.faithful);
/* 1097 */           p.getCommunicator().sendToggle(3, p.isStealth());
/*      */           
/* 1099 */           p.getCommunicator().sendToggle(4, p.isAutofight());
/* 1100 */           p.getCommunicator().sendToggle(100, p.isArcheryMode());
/*      */           
/* 1102 */           if (p.getShield() != null)
/* 1103 */             p.getCommunicator().sendToggleShield(true); 
/* 1104 */           Item dragged = p.getMovementScheme().getDraggedItem();
/* 1105 */           if (dragged != null) {
/*      */             
/* 1107 */             Items.stopDragging(dragged);
/* 1108 */             Items.startDragging((Creature)p, dragged);
/*      */           } 
/* 1110 */           Players.getInstance().addToGroups(p);
/*      */         }
/*      */         else {
/*      */           
/* 1114 */           String message = "Password incorrect. Please try again or create a new player with a different name than " + name + ".";
/*      */           
/* 1116 */           HackerIp ip = failedIps.get(this.conn.getIp());
/* 1117 */           if (ip != null) {
/*      */             
/* 1119 */             ip.name = name;
/* 1120 */             ip.timesFailed++;
/* 1121 */             long atime = 0L;
/* 1122 */             if (ip.timesFailed == 10)
/*      */             {
/* 1124 */               atime = 180000L;
/*      */             }
/* 1126 */             if (ip.timesFailed == 20) {
/*      */               
/* 1128 */               atime = 600000L;
/*      */             }
/* 1130 */             else if (ip.timesFailed % 20 == 0) {
/*      */               
/* 1132 */               atime = 10800000L;
/*      */             } 
/* 1134 */             if (ip.timesFailed == 100)
/*      */             {
/* 1136 */               Players.addGmMessage("System", "The ip " + this.conn.getIp() + " has failed the password for " + name + " 100 times. It is now banned one hour every failed attempt.");
/*      */             }
/*      */             
/* 1139 */             if (ip.timesFailed > 100)
/*      */             {
/* 1141 */               atime = 3600000L;
/*      */             }
/* 1143 */             ip.mayTryAgain = System.currentTimeMillis() + atime;
/* 1144 */             if (atime > 0L) {
/* 1145 */               message = message + " Because of the repeated failures you may try again in " + Server.getTimeFor(atime) + ".";
/*      */             }
/*      */           } else {
/*      */             
/* 1149 */             failedIps.put(this.conn.getIp(), new HackerIp(1, System.currentTimeMillis(), name));
/*      */           } 
/*      */           
/*      */           try {
/* 1153 */             (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1154 */             sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -2);
/*      */           }
/* 1156 */           catch (IOException ioe) {
/*      */ 
/*      */             
/* 1159 */             if (logger.isLoggable(Level.FINE))
/*      */             {
/* 1161 */               logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + message, ioe);
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 1169 */         p.destroyVisionArea();
/*      */         
/*      */         try {
/* 1172 */           p.createVisionArea();
/*      */         }
/* 1174 */         catch (Exception ex) {
/*      */           
/* 1176 */           logger.log(Level.WARNING, "Failed to create visionarea for player " + p.getName(), ex);
/* 1177 */           p.logoutIn(2, "The game failed to create your vision area. Please contact the administrators.");
/*      */           
/*      */           return;
/*      */         } 
/* 1181 */         if (!p.hasLink()) {
/*      */           
/* 1183 */           p.destroyVisionArea();
/*      */           return;
/*      */         } 
/* 1186 */         if (!p.isDead()) {
/*      */           
/* 1188 */           VolaTile tile = p.getCurrentTile();
/* 1189 */           if (tile == null) {
/*      */             
/* 1191 */             logger.log(Level.WARNING, p.getName() + " isn't in the world. Adding and retrying.");
/* 1192 */             p.sendToWorld();
/*      */           } 
/*      */           
/* 1195 */           p.getCommunicator().sendSelfToLocal();
/* 1196 */           Achievements.sendAchievementList((Creature)p);
/* 1197 */           p.getCommunicator().sendAllKingdoms();
/* 1198 */           tile = p.getCurrentTile();
/* 1199 */           Door[] doors = tile.getDoors();
/* 1200 */           if (doors != null)
/*      */           {
/* 1202 */             for (Door lDoor : doors) {
/*      */               
/* 1204 */               if (lDoor.canBeOpenedBy((Creature)p, false))
/*      */               {
/* 1206 */                 if (lDoor instanceof FenceGate) {
/*      */                   
/* 1208 */                   p.getCommunicator().sendOpenFence(((FenceGate)lDoor).getFence(), true, true);
/*      */                 } else {
/*      */                   
/* 1211 */                   p.getCommunicator().sendOpenDoor(lDoor);
/*      */                 }  } 
/*      */             } 
/*      */           }
/* 1215 */           if (!wasinvuln)
/*      */           {
/* 1217 */             p.getCommunicator().sendAlertServerMessage("You are not invulnerable now.");
/* 1218 */             p.getCommunicator().setInvulnerable(false);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1223 */           p.getCommunicator().sendDead();
/* 1224 */           p.sendSpawnQuestion();
/*      */         } 
/*      */         
/* 1227 */         p.setFullyLoaded();
/* 1228 */         p.getCommunicator().setReady(true);
/* 1229 */         sendLoggedInPeople(p);
/* 1230 */         sendStatus(p);
/*      */ 
/*      */         
/* 1233 */         p.getCombatHandler().sendRodEffect();
/* 1234 */         p.sendHasFingerEffect();
/* 1235 */         p.getStatus().sendStateString();
/* 1236 */         p.getCommunicator().sendMapInfo();
/* 1237 */         Server.getInstance().addToPlayersAtLogin(p);
/* 1238 */         if (p.getVisionArea() != null && p.getVisionArea().getSurface() != null)
/* 1239 */           p.getVisionArea().getSurface().sendCreatureItems((Creature)p); 
/* 1240 */         p.setBestLightsource(null, true);
/*      */         
/* 1242 */         boolean isEducated = false;
/* 1243 */         for (Titles.Title t : p.getTitles()) {
/* 1244 */           if (t == Titles.Title.Educated)
/* 1245 */             isEducated = true; 
/* 1246 */         }  if (!isEducated) {
/* 1247 */           PlayerTutorial.getTutorialForPlayer(p.getWurmId(), true).sendCurrentStageBML();
/*      */         }
/* 1249 */         p.isLit = false;
/* 1250 */         p.recalcLimitingFactor(null);
/* 1251 */         if (p.getCultist() != null)
/*      */         {
/* 1253 */           p.getCultist().sendBuffs();
/*      */         }
/* 1255 */         AffinitiesTimed.sendTimedAffinitiesFor((Creature)p);
/* 1256 */         if (p.isDeathProtected())
/*      */         {
/* 1258 */           p.getCommunicator().sendAddStatusEffect(SpellEffectsEnum.DEATH_PROTECTION, 2147483647);
/*      */         }
/* 1260 */         MissionPerformer.sendEpicMissionsPerformed((Creature)p, p.getCommunicator());
/* 1261 */         MissionPerformer mp = MissionPerformed.getMissionPerformer(p.getWurmId());
/* 1262 */         if (mp != null)
/*      */         {
/* 1264 */           mp.sendAllMissionPerformed(p.getCommunicator());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1289 */         checkPutOnBoat(p);
/*      */       }
/* 1291 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 1293 */         PlayerInfo file = PlayerInfoFactory.createPlayerInfo(name);
/* 1294 */         Player player = null;
/*      */         
/*      */         try {
/* 1297 */           if (isUndead) {
/*      */             
/* 1299 */             file.undeadType = (byte)(1 + Server.rand.nextInt(3));
/*      */             
/* 1301 */             if (Servers.localServer.LOGINSERVER) {
/*      */               
/*      */               try {
/* 1304 */                 if (file.currentServer <= 1)
/*      */                 {
/* 1306 */                   for (ServerEntry serverEntry : Servers.getAllServers()) {
/*      */                     
/* 1308 */                     if (serverEntry.EPIC && serverEntry.isAvailable(file.getPower(), true))
/* 1309 */                       file.currentServer = serverEntry.getId(); 
/*      */                   } 
/*      */                 }
/* 1312 */                 player = new Player(file, this.conn);
/* 1313 */                 player.setSteamID(SteamId.fromSteamID64(Long.valueOf(steamIDAsString).longValue()));
/* 1314 */                 Ban ban = Players.getInstance().getAnyBan(this.conn.getIp(), player, steamIDAsString);
/* 1315 */                 if (ban != null) {
/*      */ 
/*      */                   
/*      */                   try {
/*      */                     
/* 1320 */                     String time = Server.getTimeFor(ban.getExpiry() - System.currentTimeMillis());
/*      */                     
/* 1322 */                     String str1 = ban.getIdentifier() + " is banned for " + time + " more. Reason: " + ban.getReason();
/*      */                     
/* 1324 */                     (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*      */                     
/* 1326 */                     if (ban.getExpiry() - System.currentTimeMillis() > 29030400000L)
/* 1327 */                       str1 = ban.getIdentifier() + " is permanently banned. Reason: " + ban.getReason(); 
/* 1328 */                     sendLoginAnswer(false, str1, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */                   }
/* 1330 */                   catch (IOException ioe) {
/*      */ 
/*      */                     
/* 1333 */                     if (logger.isLoggable(Level.FINE))
/*      */                     {
/* 1335 */                       logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem sending banned IP login denied message", ioe);
/*      */                     }
/*      */                   } 
/*      */                   
/* 1339 */                   logger.log(Level.INFO, name + " is banned, trying to log on from " + this.conn.getIp());
/*      */                   return;
/*      */                 } 
/* 1342 */                 String message = "The server is currently not available. Please try later.";
/* 1343 */                 ServerEntry entry = Servers.getServerWithId(file.currentServer);
/* 1344 */                 if (entry != null) {
/*      */                   
/* 1346 */                   if (entry.isAvailable(file.getPower(), file.isPaying()))
/*      */                   {
/* 1348 */                     player.getCommunicator().sendReconnect(entry.EXTERNALIP, 
/* 1349 */                         Integer.parseInt(entry.EXTERNALPORT), password);
/* 1350 */                     logger.log(Level.INFO, this.conn.getIp() + ", " + name + " redirected from " + Servers.localServer.id + " to server ID: " + file.currentServer);
/*      */                   
/*      */                   }
/*      */                   else
/*      */                   {
/* 1355 */                     (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1356 */                     message = "The server is currently not available. Please try later.";
/* 1357 */                     sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 300);
/*      */                     
/* 1359 */                     logger.log(Level.INFO, this.conn.getIp() + ", " + name + " could not be redirected from " + Servers.localServer.id + " to server ID: " + file.currentServer + " not avail.");
/*      */                   
/*      */                   }
/*      */ 
/*      */                 
/*      */                 }
/*      */                 else {
/*      */                   
/* 1367 */                   logger.warning(this.conn.getIp() + ", " + name + " could not be redirected from " + Servers.localServer.id + " to non-existant server ID: " + file.currentServer + ", the database entry is wrong");
/*      */ 
/*      */                   
/* 1370 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1371 */                   sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -1);
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/*      */                 return;
/* 1377 */               } catch (IOException ioe) {
/*      */                 return;
/*      */               } 
/*      */             }
/*      */           } 
/* 1382 */           file.load();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1387 */           if (password.equals(file.getPassword())) {
/*      */             
/* 1389 */             player = new Player(file, this.conn);
/* 1390 */             player.setSteamID(SteamId.fromSteamID64(Long.valueOf(steamIDAsString).longValue()));
/* 1391 */             Ban ban = Players.getInstance().getAnyBan(this.conn.getIp(), player, steamIDAsString);
/* 1392 */             if (ban != null) {
/*      */ 
/*      */               
/*      */               try {
/*      */                 
/* 1397 */                 String time = Server.getTimeFor(ban.getExpiry() - System.currentTimeMillis());
/* 1398 */                 String message = ban.getIdentifier() + " is banned for " + time + " more. Reason: " + ban.getReason();
/*      */                 
/* 1400 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*      */                 
/* 1402 */                 if (ban.getExpiry() - System.currentTimeMillis() > 29030400000L)
/* 1403 */                   message = ban.getIdentifier() + " is permanently banned. Reason: " + ban.getReason(); 
/* 1404 */                 sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */               }
/* 1406 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/* 1409 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/* 1411 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem sending banned IP login denied message", ioe);
/*      */                 }
/*      */               } 
/*      */               
/* 1415 */               logger.log(Level.INFO, name + " is banned, trying to log on from " + this.conn.getIp());
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 1425 */             if (file.currentServer != Servers.localServer.id) {
/*      */               
/* 1427 */               String message = "The server is currently not available. Please try later.";
/*      */               
/*      */               try {
/* 1430 */                 ServerEntry entry = Servers.getServerWithId(file.currentServer);
/* 1431 */                 if (entry != null)
/*      */                 {
/* 1433 */                   if (entry.isAvailable(file.getPower(), file.isPaying()))
/*      */                   {
/* 1435 */                     player.getCommunicator().sendReconnect(entry.EXTERNALIP, 
/* 1436 */                         Integer.parseInt(entry.EXTERNALPORT), password);
/* 1437 */                     logger.log(Level.INFO, this.conn.getIp() + ", " + name + " redirected from " + Servers.localServer.id + " to server ID: " + file.currentServer);
/*      */                   
/*      */                   }
/*      */                   else
/*      */                   {
/* 1442 */                     (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1443 */                     message = "The server is currently not available. Please try later.";
/* 1444 */                     sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 300);
/*      */                     
/* 1446 */                     logger.log(Level.INFO, this.conn.getIp() + ", " + name + " could not be redirected from " + Servers.localServer.id + " to server ID: " + file.currentServer + " not avail.");
/*      */                   
/*      */                   }
/*      */ 
/*      */                 
/*      */                 }
/*      */                 else
/*      */                 {
/* 1454 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1455 */                   logger.warning(this.conn.getIp() + ", " + name + " could not be redirected from " + Servers.localServer.id + " to non-existant server ID: " + file.currentServer + ", the database entry is wrong");
/*      */ 
/*      */                   
/* 1458 */                   sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -1);
/*      */                 }
/*      */               
/*      */               }
/* 1462 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/* 1465 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/* 1467 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem redirecting from " + Servers.localServer.id + " to server ID: " + file.currentServer, ioe);
/*      */                 }
/*      */               } 
/*      */               
/* 1471 */               file.lastLogin = System.currentTimeMillis() - 10000L;
/* 1472 */               file.logout();
/* 1473 */               file.save();
/* 1474 */               redirects++;
/* 1475 */               this.conn.ticksToDisconnect = 400;
/*      */               return;
/*      */             } 
/* 1478 */             if (!Constants.isGameServer && file.currentServer == Servers.localServer.id) {
/*      */               
/* 1480 */               logger.log(Level.WARNING, name + " tried to logon locally.");
/* 1481 */               String message = "You can not log on to this type of server. Contact a GM or Dev";
/*      */               
/*      */               try {
/* 1484 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1485 */                 sendLoginAnswer(false, "You can not log on to this type of server. Contact a GM or Dev", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -1);
/*      */               
/*      */               }
/* 1488 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/* 1491 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/* 1493 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "You can not log on to this type of server. Contact a GM or Dev", ioe);
/*      */                 }
/*      */               } 
/*      */ 
/*      */               
/* 1498 */               file.lastLogin = System.currentTimeMillis() - 10000L;
/* 1499 */               file.logout();
/* 1500 */               file.save();
/* 1501 */               this.conn.ticksToDisconnect = 400;
/*      */               return;
/*      */             } 
/* 1504 */             if ((player.getSaveFile()).realdeath > 4) {
/*      */               
/* 1506 */               String message = "Your account has suffered real death. You can not log on.";
/*      */               
/*      */               try {
/* 1509 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1510 */                 sendLoginAnswer(false, "Your account has suffered real death. You can not log on.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -1);
/*      */               
/*      */               }
/* 1513 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/* 1516 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/* 1518 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "Your account has suffered real death. You can not log on.", ioe);
/*      */                 }
/*      */               } 
/*      */               
/*      */               return;
/*      */             } 
/*      */             
/* 1525 */             player.setLoginHandler(this);
/*      */             
/* 1527 */             this.conn.setConnectionListener((SimpleConnectionListener)player.getCommunicator());
/* 1528 */             logins++;
/* 1529 */             if (player.getPower() < 1 && !player.isPaying() && 
/* 1530 */               Players.getInstance().numberOfPlayers() > Servers.localServer.pLimit) {
/*      */               
/* 1532 */               String message = "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.";
/*      */               
/*      */               try {
/* 1535 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1536 */                 sendLoginAnswer(false, "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */               }
/* 1538 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/* 1541 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/* 1543 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.", ioe);
/*      */                 }
/*      */               } 
/*      */               
/*      */               return;
/*      */             } 
/*      */             
/* 1550 */             if (player.getPower() < 1 && !player.isPaying() && Servers.localServer.ISPAYMENT) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1555 */               String message = "This server is a premium only server. You can not log on until you have purchased premium time in the webshop.";
/*      */               
/*      */               try {
/* 1558 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1559 */                 sendLoginAnswer(false, "This server is a premium only server. You can not log on until you have purchased premium time in the webshop.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */               }
/* 1561 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/* 1564 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/* 1566 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "This server is a premium only server. You can not log on until you have purchased premium time in the webshop.", ioe);
/*      */                 }
/*      */               } 
/*      */               
/*      */               return;
/*      */             } 
/*      */             
/* 1573 */             if (Constants.maintaining)
/*      */             {
/* 1575 */               if (player.getPower() <= 1) {
/*      */ 
/*      */                 
/*      */                 try {
/* 1579 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1580 */                   sendLoginAnswer(false, "The server is in maintenance mode. Retrying in 60 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */ 
/*      */                 
/*      */                 }
/* 1584 */                 catch (IOException ioe) {
/*      */ 
/*      */                   
/* 1587 */                   if (logger.isLoggable(Level.FINE))
/*      */                   {
/* 1589 */                     logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem sending maintenance mode login denied", ioe);
/*      */                   }
/*      */                 } 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             }
/* 1596 */             if (Constants.enableSpyPrevention && Servers.localServer.PVPSERVER && !Servers.localServer.testServer)
/*      */             {
/*      */               
/* 1599 */               if (player.getPower() < 1) {
/*      */                 
/* 1601 */                 byte kingdom = Players.getInstance().getKingdomForPlayer(player.getWurmId());
/* 1602 */                 KingdomIp kip = KingdomIp.getKIP(this.conn.getIp(), kingdom);
/* 1603 */                 if (kip != null) {
/*      */                   
/* 1605 */                   long answer = kip.mayLogonKingdom(kingdom);
/* 1606 */                   if (answer < 0L) {
/*      */ 
/*      */                     
/*      */                     try {
/* 1610 */                       Kingdom k = Kingdoms.getKingdom(kip.getKingdom());
/* 1611 */                       (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1612 */                       if (k != null) {
/* 1613 */                         sendLoginAnswer(false, "Spy prevention: Someone is playing on kingdom " + k
/* 1614 */                             .getName() + " from this ip address.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */                       }
/*      */                       else {
/*      */                         
/* 1618 */                         sendLoginAnswer(false, "Spy prevention: Someone is playing on another kingdom from this ip address.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */                       
/*      */                       }
/*      */                     
/*      */                     }
/* 1623 */                     catch (IOException ioe) {
/*      */ 
/*      */                       
/* 1626 */                       if (logger.isLoggable(Level.FINE))
/*      */                       {
/* 1628 */                         logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem sending spy prevention login denied", ioe);
/*      */                       }
/*      */                     } 
/*      */                     
/*      */                     return;
/*      */                   } 
/* 1634 */                   if (answer > 1L) {
/*      */                     
/* 1636 */                     String timeLeft = Server.getTimeFor(answer);
/* 1637 */                     if (answer < 0L) {
/*      */ 
/*      */                       
/*      */                       try {
/* 1641 */                         (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1642 */                         Kingdom k = Kingdoms.getKingdom(kingdom);
/* 1643 */                         if (k != null) {
/* 1644 */                           sendLoginAnswer(false, "Spy prevention: You have to wait " + timeLeft + " because someone was recently playing " + k
/* 1645 */                               .getName() + " from this ip address.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */                         
/*      */                         }
/*      */                         else {
/*      */                           
/* 1650 */                           sendLoginAnswer(false, "Spy prevention: You have to wait " + timeLeft + " because someone was recently playing in another kingdom from this ip address.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */ 
/*      */                         
/*      */                         }
/*      */ 
/*      */                       
/*      */                       }
/* 1657 */                       catch (IOException ioe) {
/*      */ 
/*      */                         
/* 1660 */                         if (logger.isLoggable(Level.FINE))
/*      */                         {
/* 1662 */                           logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem sending spy prevention login denied", ioe);
/*      */                         }
/*      */                       } 
/*      */                       
/*      */                       return;
/*      */                     } 
/*      */                   } 
/* 1669 */                   kip.logon(kingdom);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */             
/* 1674 */             logger.log(Level.INFO, this.conn
/* 1675 */                 .getIp() + "," + name + " successfully logged on, id: " + player.getWurmId() + '.');
/*      */ 
/*      */ 
/*      */             
/* 1679 */             Server.getInstance().addPlayer(player);
/* 1680 */             player.initialisePlayer(file);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1691 */             if (!reconnecting)
/* 1692 */               player.setSendExtraBytes(sendExtraBytes); 
/* 1693 */             player.checkBodyInventoryConsistency();
/* 1694 */             player.getBody().createBodyParts();
/* 1695 */             Server.getInstance().startSendingFinals(player);
/* 1696 */             long start = System.nanoTime();
/* 1697 */             player.loadSkills();
/*      */             
/* 1699 */             Items.loadAllItemsForCreature((Creature)player, player.getStatus().getInventoryId());
/* 1700 */             player.getCommunicator().sendMapInfo();
/* 1701 */             Players.loadAllPrivatePOIForPlayer(player);
/* 1702 */             player.resetLastSentToolbelt();
/* 1703 */             player.sendAllMapAnnotations();
/* 1704 */             ValreiMapData.sendAllMapData(player);
/* 1705 */             player.getCommunicator().sendClearTickets();
/* 1706 */             player.getCommunicator().sendClearFriendsList();
/* 1707 */             if (player.getCultist() != null)
/*      */             {
/* 1709 */               player.getCultist().sendBuffs();
/*      */             }
/* 1711 */             AffinitiesTimed.sendTimedAffinitiesFor((Creature)player);
/* 1712 */             Players.getInstance().sendConnectInfo(player, " has logged in.", player.getLastLogin(), PlayerOnlineStatus.ONLINE, true);
/*      */             
/* 1714 */             Players.getInstance().addToGroups(player);
/* 1715 */             if (player.getBridgeId() != -10L) {
/*      */               
/* 1717 */               BridgePart[] bridgeParts = player.getCurrentTile().getBridgeParts();
/* 1718 */               boolean foundBridge = false;
/* 1719 */               for (BridgePart bp : bridgeParts) {
/*      */                 
/* 1721 */                 foundBridge = true;
/* 1722 */                 if (!bp.isFinished()) {
/*      */                   
/* 1724 */                   foundBridge = false;
/*      */                   break;
/*      */                 } 
/*      */               } 
/* 1728 */               if (foundBridge) {
/*      */                 
/* 1730 */                 for (BridgePart bp : bridgeParts) {
/*      */                   
/* 1732 */                   if (bp.isFinished() && bp.hasAnExit() && bp.getStructureId() != player.getBridgeId()) {
/*      */                     
/* 1734 */                     logger.info(String.format("Player %s logged in at [%s, %s] where bridge ID %s used to be built, but has since been replaced by the bridge ID %s.", new Object[] { player.getName(), Integer.valueOf(player.getTileX()), Integer.valueOf(player.getTileY()), Long.valueOf(player.getBridgeId()), Long.valueOf(bp.getStructureId()) }));
/* 1735 */                     player.setBridgeId(bp.getStructureId());
/*      */ 
/*      */                     
/*      */                     break;
/*      */                   } 
/*      */                 } 
/*      */               } else {
/* 1742 */                 logger.info(String.format("Player %s logged in at [%s, %s] where a bridge used to be, but no longer exists.", new Object[] { player.getName(), Integer.valueOf(player.getTileX()), Integer.valueOf(player.getTileY()) }));
/* 1743 */                 player.setBridgeId(-10L);
/*      */               } 
/*      */             } 
/*      */             
/* 1747 */             if (logger.isLoggable(Level.FINE))
/*      */             {
/* 1749 */               logger.info("Loading all skills and items took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " millis for " + name);
/*      */             
/*      */             }
/*      */           }
/*      */           else {
/*      */             
/* 1755 */             logger.log(Level.INFO, this.conn.getIp() + "," + name + ", tried to log in with wrong password.");
/* 1756 */             if (file.getPower() > 0) {
/*      */               
/* 1758 */               Players.getInstance().sendConnectAlert(this.conn
/* 1759 */                   .getIp() + "," + name + ", tried to log in with wrong password.");
/*      */               
/* 1761 */               Players.addGmMessage(name, this.conn
/* 1762 */                   .getIp() + "," + name + ", tried to log in with wrong password.");
/*      */               
/* 1764 */               WCGmMessage wc = new WCGmMessage(WurmId.getNextWCCommandId(), name, "(" + Servers.localServer.id + ") " + this.conn.getIp() + "," + name + ", tried to log in with wrong password.", false);
/*      */               
/* 1766 */               wc.sendToLoginServer();
/*      */             } 
/* 1768 */             String message = "Password incorrect. Please try again or create a new player with a different name than " + name + ".";
/*      */             
/* 1770 */             HackerIp ip = failedIps.get(this.conn.getIp());
/* 1771 */             if (ip != null) {
/*      */               
/* 1773 */               ip.name = name;
/* 1774 */               ip.timesFailed++;
/* 1775 */               long atime = 0L;
/* 1776 */               if (ip.timesFailed == 10)
/*      */               {
/* 1778 */                 atime = 180000L;
/*      */               }
/* 1780 */               if (ip.timesFailed == 20) {
/*      */                 
/* 1782 */                 atime = 600000L;
/*      */               }
/* 1784 */               else if (ip.timesFailed % 20 == 0) {
/*      */                 
/* 1786 */                 atime = 10800000L;
/*      */               } 
/* 1788 */               if (ip.timesFailed == 100)
/*      */               {
/* 1790 */                 Players.addGmMessage("System", "The ip " + this.conn.getIp() + " has failed the password for " + name + " 100 times. It is now banned one hour every failed attempt.");
/*      */               }
/*      */               
/* 1793 */               if (ip.timesFailed > 100)
/*      */               {
/* 1795 */                 atime = 3600000L;
/*      */               }
/* 1797 */               ip.mayTryAgain = System.currentTimeMillis() + atime;
/* 1798 */               if (atime > 0L)
/*      */               {
/* 1800 */                 message = message + " Because of the repeated failures you may try again in " + Server.getTimeFor(atime) + ".";
/*      */               }
/*      */             } else {
/*      */               
/* 1804 */               failedIps.put(this.conn.getIp(), new HackerIp(1, System.currentTimeMillis(), name));
/*      */             } 
/* 1806 */             (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1807 */             sendLoginAnswer(false, message, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */             
/*      */             return;
/*      */           } 
/* 1811 */         } catch (Exception ex) {
/*      */           
/* 1813 */           if (!isUndead && !Servers.localServer.testServer && logger.isLoggable(Level.INFO) && 
/* 1814 */             !Server.getInstance().isPS())
/*      */           {
/* 1816 */             logger.log(Level.INFO, "Caught Exception while trying to log player in:" + ex.getMessage() + " for " + name, ex);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/* 1822 */             if (sendExtraBytes || Servers.localServer.testServer || isUndead || Server.getInstance().isPS()) {
/*      */               
/* 1824 */               if (Constants.maintaining) {
/*      */ 
/*      */                 
/*      */                 try {
/* 1828 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1829 */                   sendLoginAnswer(false, "The server is in maintenance mode. Retrying in 60 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */ 
/*      */                 
/*      */                 }
/* 1833 */                 catch (IOException ioe) {
/*      */ 
/*      */                   
/* 1836 */                   if (logger.isLoggable(Level.FINE))
/*      */                   {
/* 1838 */                     logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem sending maintenance mode login denied", ioe);
/*      */                   }
/*      */                 } 
/*      */                 
/*      */                 return;
/*      */               } 
/* 1844 */               if (Players.getInstance().numberOfPlayers() > Servers.localServer.pLimit) {
/*      */                 
/* 1846 */                 String str = "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.";
/*      */                 
/*      */                 try {
/* 1849 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1850 */                   sendLoginAnswer(false, "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 60);
/*      */                 }
/* 1852 */                 catch (IOException ioe) {
/*      */ 
/*      */                   
/* 1855 */                   if (logger.isLoggable(Level.FINE))
/*      */                   {
/* 1857 */                     logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "The server is full. If you pay for a premium account you will be able to enter anyway. Retrying in 60 seconds.", ioe);
/*      */                   }
/*      */                 } 
/*      */                 
/*      */                 return;
/*      */               } 
/* 1863 */               if (Servers.localServer.id != Servers.loginServer.id && !isUndead && !Servers.localServer.testServer) {
/*      */ 
/*      */                 
/* 1866 */                 String str = "There are multiple login servers in the cluster, please remove so it is only one";
/*      */                 
/*      */                 try {
/* 1869 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 1870 */                   sendLoginAnswer(false, "There are multiple login servers in the cluster, please remove so it is only one", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, -1);
/*      */                 
/*      */                 }
/* 1873 */                 catch (IOException ioe) {
/*      */ 
/*      */                   
/* 1876 */                   if (logger.isLoggable(Level.FINE))
/*      */                   {
/* 1878 */                     logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "There are multiple login servers in the cluster, please remove so it is only one", ioe);
/*      */                   }
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */ 
/*      */               
/* 1888 */               logger.log(Level.INFO, this.conn.getIp() + "," + name + " was created successfully.");
/* 1889 */               player = Player.doNewPlayer(1, this.conn);
/* 1890 */               player.setName(name);
/* 1891 */               float posX = (Servers.localServer.SPAWNPOINTJENNX * 4 + Server.rand.nextInt(10));
/* 1892 */               float posY = (Servers.localServer.SPAWNPOINTJENNY * 4 + Server.rand.nextInt(10));
/*      */               
/* 1894 */               int r = Server.rand.nextInt(3);
/*      */               
/* 1896 */               float rot = Server.rand.nextInt(360);
/* 1897 */               byte kingdom = 1;
/*      */               
/* 1899 */               if (isUndead) {
/*      */                 
/* 1901 */                 kingdom = 0;
/* 1902 */                 float[] txty = Player.findRandomSpawnX(false, false);
/* 1903 */                 posX = txty[0];
/* 1904 */                 posY = txty[1];
/*      */               }
/*      */               else {
/*      */                 
/* 1908 */                 if (Servers.localServer.KINGDOM != 0) {
/*      */                   
/* 1910 */                   kingdom = Servers.localServer.KINGDOM;
/*      */ 
/*      */                 
/*      */                 }
/* 1914 */                 else if (r == 1) {
/*      */                   
/* 1916 */                   kingdom = 2;
/* 1917 */                   posX = (Servers.localServer.SPAWNPOINTMOLX * 4 + Server.rand.nextInt(10));
/* 1918 */                   posY = (Servers.localServer.SPAWNPOINTMOLY * 4 + Server.rand.nextInt(10));
/*      */                 }
/* 1920 */                 else if (r == 2) {
/*      */                   
/* 1922 */                   kingdom = 3;
/* 1923 */                   posX = (Servers.localServer.SPAWNPOINTLIBX * 4 + Server.rand.nextInt(10));
/* 1924 */                   posY = (Servers.localServer.SPAWNPOINTLIBY * 4 + Server.rand.nextInt(10));
/*      */                 } 
/*      */                 
/* 1927 */                 if (Servers.localServer.randomSpawns) {
/*      */                   
/* 1929 */                   float[] txty = Player.findRandomSpawnX(true, true);
/* 1930 */                   posX = txty[0];
/* 1931 */                   posY = txty[1];
/*      */                 } 
/*      */               } 
/* 1934 */               Spawnpoint sp = getInitialSpawnPoint(kingdom);
/* 1935 */               if (sp != null) {
/*      */                 
/* 1937 */                 posX = (sp.tilex * 4 + Server.rand.nextInt(10));
/* 1938 */                 posY = (sp.tiley * 4 + Server.rand.nextInt(10));
/*      */               } 
/* 1940 */               long wurmId = WurmId.getNextPlayerId();
/* 1941 */               player = (Player)player.setWurmId(wurmId, posX, posY, rot, 0);
/* 1942 */               Ban ban = Players.getInstance().getAnyBan(this.conn.getIp(), player, steamIDAsString);
/* 1943 */               if (ban != null) {
/*      */ 
/*      */                 
/*      */                 try {
/* 1947 */                   String time = Server.getTimeFor(ban.getExpiry() - System.currentTimeMillis());
/*      */                   
/* 1949 */                   String str1 = ban.getIdentifier() + " is banned for " + time + " more. Reason: " + ban.getReason();
/*      */                   
/* 1951 */                   (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/*      */                   
/* 1953 */                   if (ban.getExpiry() - System.currentTimeMillis() > 29030400000L)
/* 1954 */                     str1 = ban.getIdentifier() + " is permanently banned. Reason: " + ban.getReason(); 
/* 1955 */                   sendLoginAnswer(false, str1, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */                 }
/* 1957 */                 catch (IOException ioe) {
/*      */ 
/*      */                   
/* 1960 */                   if (logger.isLoggable(Level.FINE))
/*      */                   {
/* 1962 */                     logger.log(Level.FINE, this.conn.getIp() + ", " + name + " problem sending IP banned login denied message", ioe);
/*      */                   }
/*      */                 } 
/*      */                 
/* 1966 */                 logger.log(Level.INFO, name + " is banned, trying to log on from " + this.conn.getIp());
/*      */                 
/*      */                 return;
/*      */               } 
/* 1970 */               putOutsideWall(player);
/* 1971 */               if (player.isOnSurface()) {
/*      */                 
/* 1973 */                 putOutsideHouse(player, false);
/* 1974 */                 putOutsideFence(player);
/*      */               } 
/* 1976 */               String message = "Welcome to Wurm, " + name + "! " + (Servers.localServer.hasMotd() ? Servers.localServer.getMotd() : Constants.motd);
/*      */ 
/*      */               
/* 1979 */               player.getMovementScheme().setPosition(player.getStatus().getPositionX(), player
/* 1980 */                   .getStatus().getPositionY(), player.getStatus().getPositionZ(), player
/* 1981 */                   .getStatus().getRotation(), player.isOnSurface() ? 0 : -1);
/* 1982 */               VolaTile targetTile = Zones.getTileOrNull((int)(player.getStatus().getPositionX() / 4.0F), 
/* 1983 */                   (int)(player.getStatus().getPositionY() / 4.0F), player.isOnSurface());
/* 1984 */               if (targetTile != null) {
/*      */                 
/* 1986 */                 float height = (player.getFloorLevel() > 0) ? (player.getFloorLevel() * 3) : 0.0F;
/*      */                 
/* 1988 */                 player.getMovementScheme().setGroundOffset((int)(height * 10.0F), true);
/* 1989 */                 player.calculateFloorLevel(targetTile, true);
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1996 */               player.getStatus().checkStaminaEffects(65535);
/* 1997 */               player.getMovementScheme().haltSpeedModifier();
/* 1998 */               player.setTeleporting(true);
/* 1999 */               player.setTeleportCounter(player.getTeleportCounter() + 1);
/*      */               
/* 2001 */               player.setNewPlayer(true);
/*      */ 
/*      */               
/* 2004 */               file.initialize(name, player.getWurmId(), password, "What is your mother's maiden name?", "Sawyer", Server.rand
/*      */                   
/* 2006 */                   .nextLong(), sendExtraBytes);
/* 2007 */               file.setEmailAddress(name + "@test.com");
/* 2008 */               player.setSaveFile(file);
/* 2009 */               player.setSteamID(SteamId.fromSteamID64(Long.valueOf(steamIDAsString).longValue()));
/*      */               
/* 2011 */               if (player.isUndead())
/*      */               {
/* 2013 */                 file.setUndeadData();
/*      */               }
/*      */               
/* 2016 */               logins++;
/*      */               
/* 2018 */               player.setLoginHandler(this);
/* 2019 */               this.conn.setConnectionListener((SimpleConnectionListener)player.getCommunicator());
/* 2020 */               Server.getInstance().addIp(this.conn.getIp());
/* 2021 */               player.setIpaddress(this.conn.getIp());
/* 2022 */               player.setSteamID(SteamId.fromSteamID64(Long.valueOf(steamIDAsString).longValue()));
/* 2023 */               player.setFlag(3, true);
/* 2024 */               player.setFlag(53, true);
/*      */               
/* 2026 */               Server.getInstance().addPlayer(player);
/*      */               
/* 2028 */               player.getBody().createBodyParts();
/* 2029 */               player.getLoginhandler().sendLoginAnswer(true, message, player.getStatus().getPositionX(), player.getStatus()
/* 2030 */                   .getPositionY(), player.getStatus().getPositionZ(), player.getStatus().getRotation(), 
/* 2031 */                   player.isOnSurface() ? 0 : -1, player.getModelName(), (byte)0, 0, (byte)0, kingdom, 0L, player
/* 2032 */                   .getTeleportCounter(), player.getBlood(), player.getBridgeId(), player.getMovementScheme().getGroundOffset());
/* 2033 */               SelectSpawnQuestion question = new SelectSpawnQuestion(player, "Define your character", "Please select gender:", player.getWurmId(), message, isUndead);
/* 2034 */               question.sendQuestion();
/*      */               
/* 2036 */               if (player.getStatus().getBody().getBodyItem() != null)
/* 2037 */                 player.getStatus().getBody().getBodyItem().addWatcher(-1L, (Creature)player); 
/* 2038 */               player.setFlag(76, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2057 */               String message = "You need to register an account on www.wurmonline.com.";
/*      */               
/*      */               try {
/* 2060 */                 (Server.getInstance()).steamHandler.EndAuthSession(steamIDAsString);
/* 2061 */                 sendLoginAnswer(false, "You need to register an account on www.wurmonline.com.", 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */               }
/* 2063 */               catch (IOException ioe) {
/*      */ 
/*      */                 
/* 2066 */                 if (logger.isLoggable(Level.FINE))
/*      */                 {
/* 2068 */                   logger.log(Level.FINE, this.conn.getIp() + ", " + name + ", problem sending login denied message: " + "You need to register an account on www.wurmonline.com.", ioe);
/*      */                 }
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/* 2076 */           } catch (Exception ex2) {
/*      */             
/* 2078 */             logger.log(Level.WARNING, "Failed to create player with name " + name, ex2);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setHasLoadedItems(int step) {
/* 2093 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2095 */       logger.finer("Setting loadedItems to " + step);
/*      */     }
/* 2097 */     this.loadedItems = step;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int loadPlayer(final Player player, int step) {
/* 2109 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2111 */       logger.finest("Loading " + player + ", step: " + step);
/*      */     }
/* 2113 */     if (step == 0) {
/*      */ 
/*      */       
/* 2116 */       player.sendAddChampionPoints();
/*      */       
/* 2118 */       (player.getSaveFile()).frozenSleep = true;
/*      */ 
/*      */ 
/*      */       
/* 2122 */       return step;
/*      */     } 
/* 2124 */     if (step == 1) {
/*      */       
/* 2126 */       final Player p = player;
/* 2127 */       final LoginHandler l = this;
/*      */       
/* 2129 */       if (this.loadedItems == 0) {
/*      */ 
/*      */ 
/*      */         
/* 2133 */         Thread t = new Thread("PlayerLoader-Thread-" + p.getWurmId())
/*      */           {
/*      */ 
/*      */             
/*      */             public void run()
/*      */             {
/*      */               try {
/* 2140 */                 p.getBody().load();
/*      */ 
/*      */ 
/*      */                 
/* 2144 */                 player.getSaveFile().loadIgnored(player.getWurmId());
/* 2145 */                 player.getSaveFile().loadFriends(player.getWurmId());
/* 2146 */                 player.getSaveFile().loadTitles(player.getWurmId());
/* 2147 */                 player.getSaveFile().loadHistoryIPs(player.getWurmId());
/* 2148 */                 player.getSaveFile().loadHistorySteamIds(player.getWurmId());
/* 2149 */                 player.getSaveFile().loadHistoryEmails(player.getWurmId());
/* 2150 */                 l.setHasLoadedItems(1);
/*      */               }
/* 2152 */               catch (Exception sex2) {
/*      */ 
/*      */                 
/*      */                 try {
/* 2156 */                   LoginHandler.logger.log(Level.WARNING, p.getName() + " has no body. Creating!", sex2);
/* 2157 */                   p.getStatus().createNewBody();
/* 2158 */                   l.setHasLoadedItems(1);
/*      */                 }
/* 2160 */                 catch (Exception sex) {
/*      */                   
/* 2162 */                   LoginHandler.logger.log(Level.WARNING, p.getName() + " has no body.", sex);
/* 2163 */                   l.setHasLoadedItems(-1);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           };
/* 2168 */         this.loadedItems = Integer.MAX_VALUE;
/* 2169 */         t.setPriority(4);
/* 2170 */         t.start();
/*      */       } 
/* 2172 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 2174 */         logger.finer("Body step=" + step + ", loadedItems=" + this.loadedItems);
/*      */       }
/* 2176 */       if (this.loadedItems == 1 || this.loadedItems == -1)
/* 2177 */         return this.loadedItems; 
/* 2178 */       return step - 1;
/*      */     } 
/* 2180 */     if (step == 2) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2216 */       if (!player.isReallyPaying())
/*      */       {
/*      */         
/* 2219 */         if (player.hasFlag(8)) {
/*      */           
/* 2221 */           if (player.getPaymentExpire() == 0L) {
/* 2222 */             logger.log(Level.INFO, player.getName() + " logged on to prevent expiry.");
/*      */           }
/* 2224 */           player.setFlag(8, false);
/*      */         } 
/*      */       }
/* 2227 */       this.loadedItems = 2;
/* 2228 */       return this.loadedItems;
/*      */     } 
/* 2230 */     if (step == 3) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2245 */       putOutsideWall(player);
/* 2246 */       if (player.isOnSurface()) {
/*      */         
/* 2248 */         putOutsideHouse(player, true);
/* 2249 */         putOutsideFence(player);
/*      */       } 
/*      */       
/* 2252 */       player.getCommunicator().sendWeather();
/* 2253 */       player.getCommunicator().checkSendWeather();
/* 2254 */       if (!player.isDead())
/*      */       {
/* 2256 */         putInBoatAndAssignSeat(player, false);
/*      */       }
/* 2258 */       return step;
/*      */     } 
/* 2260 */     if (step == 4) {
/*      */       
/* 2262 */       player.getMovementScheme().setPosition(player.getStatus().getPositionX(), player.getStatus().getPositionY(), player
/* 2263 */           .getStatus().getPositionZ(), player.getStatus().getRotation(), player.isOnSurface() ? 0 : -1);
/* 2264 */       VolaTile targetTile = Zones.getTileOrNull((int)(player.getStatus().getPositionX() / 4.0F), 
/* 2265 */           (int)(player.getStatus().getPositionY() / 4.0F), player.isOnSurface());
/* 2266 */       if (targetTile != null) {
/*      */         
/* 2268 */         float height = (player.getFloorLevel() > 0) ? (player.getFloorLevel() * 3) : 0.0F;
/*      */ 
/*      */         
/* 2271 */         if (player.getBridgeId() > 0L)
/*      */         {
/* 2273 */           height = 0.0F;
/*      */         }
/*      */         
/* 2276 */         player.getMovementScheme().setGroundOffset((int)(height * 10.0F), true);
/* 2277 */         player.calculateFloorLevel(targetTile, true);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2284 */       player.getMovementScheme().haltSpeedModifier();
/*      */       
/*      */       try {
/* 2287 */         String message = "Welcome back, " + player.getName() + "! " + (Servers.localServer.hasMotd() ? Servers.localServer.getMotd() : Constants.motd);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2296 */         player.setTeleporting(true);
/* 2297 */         player.setTeleportCounter(player.getTeleportCounter() + 1);
/*      */         
/* 2299 */         byte power = Players.isArtist(player.getWurmId(), false, false) ? 2 : (byte)player.getPower();
/* 2300 */         sendLoginAnswer(true, message, player.getStatus().getPositionX(), player.getStatus().getPositionY(), player
/* 2301 */             .getStatus().getPositionZ(), player.getStatus().getRotation(), player.isOnSurface() ? 0 : -1, player
/* 2302 */             .getModelName(), power, 0, (byte)0, player
/*      */             
/* 2304 */             .getKingdomId(), player.getFace(), player.getTeleportCounter(), player.getBlood(), player
/* 2305 */             .getBridgeId(), player.getMovementScheme().getGroundOffset());
/* 2306 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/* 2308 */           logger.log(Level.FINE, player.getName() + ": sent Position X,Y,Z,Rotation: " + player
/* 2309 */               .getStatus().getPositionX() + "," + player.getStatus().getPositionY() + "," + player
/* 2310 */               .getStatus().getPositionZ() + "," + player.getStatus().getRotation());
/*      */         }
/*      */       }
/* 2313 */       catch (IOException ioe) {
/*      */         
/* 2315 */         logger.log(Level.FINE, "Player " + player.getName() + " dropped during login.", ioe);
/* 2316 */         return -1;
/*      */       } 
/*      */       
/* 2319 */       sendAllItemModelNames(player);
/* 2320 */       sendAllEquippedArmor(player);
/* 2321 */       return step;
/*      */     } 
/* 2323 */     if (step == 5) {
/*      */       
/* 2325 */       if (!player.isDead())
/*      */       {
/* 2327 */         if (!willGoOnBoat(player)) {
/*      */           
/*      */           try {
/*      */             
/* 2331 */             player.createVisionArea();
/*      */           }
/* 2333 */           catch (Exception ve) {
/*      */             
/* 2335 */             logger.log(Level.WARNING, "Failed to create visionarea for player " + player.getName(), ve);
/* 2336 */             return -1;
/*      */           } 
/*      */         }
/*      */       }
/* 2340 */       if (!player.hasLink()) {
/*      */         
/* 2342 */         player.destroyVisionArea();
/* 2343 */         return -1;
/*      */       } 
/* 2345 */       return step;
/*      */     } 
/* 2347 */     if (step == 6) {
/*      */       
/* 2349 */       player.getCommunicator().sendToggle(0, player.isClimbing());
/* 2350 */       player.getCommunicator().sendToggle(2, player.isLegal());
/* 2351 */       player.getCommunicator().sendToggle(1, player.faithful);
/* 2352 */       player.getCommunicator().sendToggle(3, player.isStealth());
/* 2353 */       player.getCommunicator().sendToggle(4, player.isAutofight());
/*      */       
/* 2355 */       if (player.isStealth())
/* 2356 */         player.getMovementScheme().setStealthMod(true); 
/* 2357 */       if (player.getShield() != null)
/* 2358 */         player.getCommunicator().sendToggleShield(true); 
/* 2359 */       if (player.getPower() > 0) {
/*      */         
/* 2361 */         (player.getStatus()).visible = false;
/* 2362 */         player.getCommunicator().sendNormalServerMessage("You should not be visible now.");
/*      */       } 
/* 2364 */       player.sendActionControl("", false, 0);
/* 2365 */       if (!player.isDead()) {
/*      */         
/* 2367 */         player.getCommunicator().sendClimb(player.isClimbing());
/* 2368 */         player.sendToWorld();
/*      */         
/* 2370 */         player.getCommunicator().sendSelfToLocal();
/* 2371 */         player.getCommunicator().sendAllKingdoms();
/*      */         
/* 2373 */         Achievements.sendAchievementList((Creature)player);
/*      */         
/* 2375 */         if (player.getVisionArea() != null)
/*      */         {
/* 2377 */           if (this.redirected) {
/*      */ 
/*      */             
/* 2380 */             player.getCommunicator().sendAlertServerMessage("You may not move right now.");
/* 2381 */             player.transferCounter = 10;
/*      */           } else {
/*      */             
/* 2384 */             player.getCommunicator().setReady(true);
/*      */           }  } 
/* 2386 */         VolaTile tile = player.getCurrentTile();
/*      */         
/* 2388 */         if (tile != null) {
/*      */           
/* 2390 */           Door[] doors = tile.getDoors();
/* 2391 */           if (doors != null)
/*      */           {
/* 2393 */             for (Door lDoor : doors) {
/*      */               
/* 2395 */               if (lDoor.covers(player.getPosX(), player.getPosY(), player.getPositionZ(), player.getFloorLevel(), player
/* 2396 */                   .followsGround()) && lDoor
/* 2397 */                 .canBeOpenedBy((Creature)player, false))
/*      */               {
/* 2399 */                 if (lDoor instanceof FenceGate) {
/*      */                   
/* 2401 */                   player.getCommunicator().sendOpenFence(((FenceGate)lDoor).getFence(), true, true);
/*      */                 } else {
/*      */                   
/* 2404 */                   player.getCommunicator().sendOpenDoor(lDoor);
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */         } else {
/* 2410 */           logger.log(Level.WARNING, player.getName() + "- tile is null!", new Exception());
/*      */         } 
/*      */       } else {
/* 2413 */         player.getCommunicator().sendDead();
/*      */       } 
/* 2415 */       return step;
/*      */     } 
/* 2417 */     if (step == 7) {
/*      */       
/* 2419 */       player.getBody().getBodyItem().addWatcher(-1L, (Creature)player);
/* 2420 */       return step;
/*      */     } 
/* 2422 */     if (step == 8) {
/*      */       
/* 2424 */       player.getInventory().addWatcher(-1L, (Creature)player);
/* 2425 */       return step;
/*      */     } 
/* 2427 */     if (step == 9) {
/*      */       
/* 2429 */       setStamina(player);
/* 2430 */       if (player.isDead()) {
/*      */         
/* 2432 */         player.sendSpawnQuestion();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2448 */         player.checkChallengeWarnQuestion();
/*      */       } 
/*      */       
/* 2451 */       player.recalcLimitingFactor(null);
/* 2452 */       return step;
/*      */     } 
/* 2454 */     if (step == 10) {
/*      */       
/* 2456 */       player.getBody().loadWounds();
/*      */ 
/*      */       
/* 2459 */       return step;
/*      */     } 
/* 2461 */     if (step == 11) {
/*      */       
/* 2463 */       if (player.mayHearDevTalk())
/* 2464 */         Players.sendGmMessages(player); 
/* 2465 */       if (player.mayHearMgmtTalk()) {
/* 2466 */         Players.sendMgmtMessages(player);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2478 */       return step;
/*      */     } 
/* 2480 */     if (step == 12) {
/*      */       
/* 2482 */       player.createSpellEffects();
/* 2483 */       player.addNewbieBuffs();
/* 2484 */       player.getSaveFile().setLogin();
/* 2485 */       player.sendSpellResistances();
/* 2486 */       return step;
/*      */     } 
/* 2488 */     if (step == 13) {
/*      */       
/* 2490 */       sendStatus(player);
/* 2491 */       Team t = Groups.getTeamForOfflineMember(player.getWurmId());
/* 2492 */       if (t != null)
/* 2493 */         player.setTeam(t, false); 
/* 2494 */       return step;
/*      */     } 
/* 2496 */     if (step == 14) {
/*      */       
/* 2498 */       player.getStatus().sendStateString();
/* 2499 */       player.setBestLightsource(null, true);
/* 2500 */       return step;
/*      */     } 
/* 2502 */     if (step == 15) {
/*      */       
/* 2504 */       if (player.hasLink()) {
/*      */         
/* 2506 */         player.setIpaddress(this.conn.getIp());
/* 2507 */         Server.getInstance().addIp(this.conn.getIp());
/* 2508 */         MissionPerformer.sendEpicMissionsPerformed((Creature)player, player.getCommunicator());
/* 2509 */         MissionPerformer mp = MissionPerformed.getMissionPerformer(player.getWurmId());
/* 2510 */         if (mp != null)
/*      */         {
/* 2512 */           mp.sendAllMissionPerformed(player.getCommunicator());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2520 */         return step;
/*      */       } 
/*      */       
/* 2523 */       return Integer.MAX_VALUE;
/*      */     } 
/* 2525 */     if (step == 16) {
/*      */       
/* 2527 */       sendLoggedInPeople(player);
/* 2528 */       player.sendReligion();
/* 2529 */       player.sendKarma();
/* 2530 */       player.sendScenarioKarma();
/* 2531 */       player.setFullyLoaded();
/* 2532 */       if (player.getCultist() != null)
/*      */       {
/* 2534 */         player.getCultist().sendBuffs();
/*      */       }
/* 2536 */       AffinitiesTimed.sendTimedAffinitiesFor((Creature)player);
/* 2537 */       if (player.isDeathProtected())
/*      */       {
/* 2539 */         player.getCommunicator().sendAddStatusEffect(SpellEffectsEnum.DEATH_PROTECTION, 2147483647);
/*      */       }
/* 2541 */       player.recalcLimitingFactor(null);
/* 2542 */       player.getCommunicator().sendSafeServerMessage("Type /help for available commands.");
/*      */       
/* 2544 */       if (player.isOnHostileHomeServer()) {
/* 2545 */         player.getCommunicator().sendAlertServerMessage("These enemy lands drain you of your confidence. You fight less effectively.");
/*      */       }
/*      */       
/* 2548 */       boolean isEducated = false;
/* 2549 */       for (Titles.Title t : player.getTitles()) {
/* 2550 */         if (t == Titles.Title.Educated)
/* 2551 */           isEducated = true; 
/* 2552 */       }  if (!isEducated) {
/* 2553 */         PlayerTutorial.getTutorialForPlayer(player.getWurmId(), true).sendCurrentStageBML();
/*      */       }
/* 2555 */       return step;
/*      */     } 
/* 2557 */     if (step == 17) {
/*      */       
/* 2559 */       checkReimbursement(player);
/* 2560 */       if ((player.getSaveFile()).pet != -10L) {
/*      */         
/*      */         try {
/*      */           
/* 2564 */           Creature c = Creatures.getInstance().getCreature((player.getSaveFile()).pet);
/* 2565 */           if (c.dominator != player.getWurmId())
/*      */           {
/* 2567 */             player.getCommunicator().sendNormalServerMessage(c.getNameWithGenus() + " is no longer your pet.");
/* 2568 */             player.setPet(-10L);
/*      */           }
/*      */         
/* 2571 */         } catch (NoSuchCreatureException nsc) {
/*      */ 
/*      */           
/*      */           try {
/* 2575 */             Creature c = Creatures.getInstance().loadOfflineCreature((player.getSaveFile()).pet);
/* 2576 */             if (c.dominator != player.getWurmId())
/*      */             {
/* 2578 */               if (logger.isLoggable(Level.FINER))
/*      */               {
/* 2580 */                 logger.finer(c.getName() + "," + c.getWurmId() + " back from offline - no longer dominated by " + player
/* 2581 */                     .getWurmId());
/*      */               }
/* 2583 */               player.getCommunicator().sendNormalServerMessage(c.getNameWithGenus() + " is no longer your pet.");
/* 2584 */               player.setPet(-10L);
/*      */             }
/*      */           
/* 2587 */           } catch (NoSuchCreatureException nsc2) {
/*      */             
/* 2589 */             if (logger.isLoggable(Level.FINER))
/*      */             {
/* 2591 */               logger.finer("Failed to load from offline to " + (player.getSaveFile()).pet);
/*      */             }
/* 2593 */             player.getCommunicator().sendNormalServerMessage("Your pet is nowhere to be found. It may have died of old age.");
/*      */             
/* 2595 */             player.setPet(-10L);
/*      */           } 
/*      */         } 
/*      */       }
/* 2599 */       Creatures.getInstance().returnCreaturesForPlayer(player.getWurmId());
/*      */       
/* 2601 */       if (player.getVisionArea() != null && player.getVisionArea().getSurface() != null) {
/* 2602 */         player.getVisionArea().getSurface().sendCreatureItems((Creature)player);
/*      */       }
/* 2604 */       checkPutOnBoat(player);
/* 2605 */       if (!player.checkTileInvulnerability()) {
/*      */         
/* 2607 */         player.getCommunicator().sendAlertServerMessage("You are not invulnerable here.");
/* 2608 */         player.getCommunicator().setInvulnerable(false);
/*      */       } 
/* 2610 */       if (player.isStealth()) {
/* 2611 */         player.setStealth(false);
/*      */       }
/* 2613 */       return step;
/*      */     } 
/* 2615 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int createPlayer(Player player, int step) {
/* 2627 */     if (logger.isLoggable(Level.FINEST))
/*      */     {
/* 2629 */       logger.finest("Creating player " + player + ", step: " + step);
/*      */     }
/* 2631 */     if (step == 0) {
/*      */ 
/*      */       
/*      */       try {
/* 2635 */         player.loadSkills();
/* 2636 */         player.sendSkills();
/*      */ 
/*      */       
/*      */       }
/* 2640 */       catch (Exception ex) {
/*      */         
/* 2642 */         logger.log(Level.INFO, "Failed to create skills: " + ex.getMessage(), ex);
/* 2643 */         return -1;
/*      */       } 
/* 2645 */       return step;
/*      */     } 
/* 2647 */     if (step == 1) {
/*      */ 
/*      */       
/*      */       try {
/* 2651 */         player.getBody().createBodyParts();
/*      */       }
/* 2653 */       catch (Exception ex) {
/*      */         
/* 2655 */         logger.log(Level.INFO, "Failed to create bodyparts: " + ex.getMessage(), ex);
/* 2656 */         return -1;
/*      */       } 
/* 2658 */       return step;
/*      */     } 
/* 2660 */     if (step == 2) {
/*      */ 
/*      */       
/*      */       try {
/* 2664 */         player.createPossessions();
/*      */       }
/* 2666 */       catch (Exception ex) {
/*      */         
/* 2668 */         logger.log(Level.INFO, "Failed to create possessions: " + ex.getMessage(), ex);
/* 2669 */         return -1;
/*      */       } 
/* 2671 */       return step;
/*      */     } 
/* 2673 */     if (step == 3)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 2678 */       return step;
/*      */     }
/* 2680 */     if (step == 4) {
/*      */ 
/*      */       
/*      */       try {
/* 2684 */         player.createVisionArea();
/*      */       
/*      */       }
/* 2687 */       catch (Exception ve) {
/*      */         
/* 2689 */         logger.log(Level.WARNING, "Failed to create visionarea for player " + player.getName(), ve);
/*      */         
/* 2691 */         return -1;
/*      */       } 
/* 2693 */       if (!player.hasLink()) {
/*      */         
/* 2695 */         player.destroyVisionArea();
/* 2696 */         return -1;
/*      */       } 
/* 2698 */       player.getCommunicator().setReady(true);
/* 2699 */       return step;
/*      */     } 
/* 2701 */     if (step == 5) {
/*      */       
/* 2703 */       player.createSomeItems(1.0F, false);
/* 2704 */       return step;
/*      */     } 
/* 2706 */     if (step == 6) {
/*      */       
/* 2708 */       player.setFullyLoaded();
/* 2709 */       player.getCommunicator().sendToggle(0, player.isClimbing());
/* 2710 */       player.getCommunicator().sendToggle(2, player.isLegal());
/* 2711 */       player.getCommunicator().sendToggle(1, player.faithful);
/* 2712 */       player.getCommunicator().sendToggle(3, player.isStealth());
/* 2713 */       player.getCommunicator().sendToggle(4, player.isAutofight());
/* 2714 */       return step;
/*      */     } 
/* 2716 */     if (step == 7) {
/*      */       
/* 2718 */       player.sendReligion();
/* 2719 */       player.sendKarma();
/* 2720 */       player.sendScenarioKarma();
/* 2721 */       player.sendToWorld();
/* 2722 */       player.getCommunicator().sendWeather();
/* 2723 */       player.getCommunicator().checkSendWeather();
/* 2724 */       player.getCommunicator().sendSelfToLocal();
/* 2725 */       return step;
/*      */     } 
/* 2727 */     if (step == 8) {
/*      */       
/* 2729 */       if (!player.isGuest()) {
/*      */         
/* 2731 */         player.getStatus().setStatusExists(true);
/*      */         
/*      */         try {
/* 2734 */           player.save();
/*      */         }
/* 2736 */         catch (Exception ex) {
/*      */           
/* 2738 */           logger.log(Level.INFO, "Failed to save player: " + ex.getMessage(), ex);
/* 2739 */           return -1;
/*      */         } 
/*      */       } 
/* 2742 */       return step;
/*      */     } 
/* 2744 */     if (step == 9)
/*      */     {
/*      */       
/* 2747 */       return step;
/*      */     }
/* 2749 */     if (step == 10) {
/*      */       
/* 2751 */       player.createSpellEffects();
/* 2752 */       player.getSaveFile().setLogin();
/* 2753 */       return step;
/*      */     } 
/* 2755 */     if (step == 11) {
/*      */       
/* 2757 */       sendStatus(player);
/* 2758 */       return step;
/*      */     } 
/* 2760 */     if (step == 12) {
/*      */       
/* 2762 */       player.getStatus().sendStateString();
/* 2763 */       return step;
/*      */     } 
/* 2765 */     if (step == 13) {
/*      */       
/* 2767 */       sendLoggedInPeople(player);
/* 2768 */       MissionPerformer.sendEpicMissionsPerformed((Creature)player, player.getCommunicator());
/* 2769 */       MissionPerformer mp = MissionPerformed.getMissionPerformer(player.getWurmId());
/* 2770 */       if (mp != null)
/*      */       {
/* 2772 */         mp.sendAllMissionPerformed(player.getCommunicator());
/*      */       }
/* 2774 */       return step;
/*      */     } 
/* 2776 */     if (step == 14) {
/*      */       
/* 2778 */       checkReimbursement(player);
/*      */ 
/*      */       
/* 2781 */       if (player.isNew()) {
/*      */ 
/*      */         
/* 2784 */         Item mirroritem = player.getCarriedItem(781);
/* 2785 */         if (mirroritem != null)
/*      */         {
/*      */           
/* 2788 */           player.getCommunicator().sendCustomizeFace(player.getFace(), mirroritem.getWurmId());
/*      */         }
/*      */       } 
/*      */       
/* 2792 */       player.setNewPlayer(false);
/* 2793 */       if (player.getVisionArea() != null && player.getVisionArea().getSurface() != null) {
/* 2794 */         player.getVisionArea().getSurface().sendCreatureItems((Creature)player);
/*      */       }
/* 2796 */       sendAllEquippedArmor(player);
/* 2797 */       sendAllItemModelNames(player);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2802 */       boolean isEducated = false;
/* 2803 */       for (Titles.Title t : player.getTitles()) {
/* 2804 */         if (t == Titles.Title.Educated)
/* 2805 */           isEducated = true; 
/* 2806 */       }  if (!isEducated) {
/* 2807 */         PlayerTutorial.getTutorialForPlayer(player.getWurmId(), true).sendCurrentStageBML();
/*      */       }
/* 2809 */       return Integer.MAX_VALUE;
/*      */     } 
/* 2811 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkReimbursement(Player player) {
/* 2824 */     player.reimburse();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setStamina(Player player) {
/* 2838 */     if (System.currentTimeMillis() - player.getSaveFile().getLastLogin() < 21600000L) {
/*      */       
/* 2840 */       player.getStatus().modifyStamina2(
/* 2841 */           (float)(System.currentTimeMillis() - (player.getSaveFile()).lastLogout) / 2.16E7F);
/*      */     }
/*      */     else {
/*      */       
/* 2845 */       player.getStatus().modifyStamina2(1.0F);
/* 2846 */       player.getStatus().modifyHunger(-10000, 0.5F);
/* 2847 */       player.getStatus().modifyThirst(-10000.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean creatureIsInsideWrongHouse(Player player, boolean load) {
/* 2866 */     if (player.getPower() > 1)
/* 2867 */       return false; 
/* 2868 */     VolaTile startTile = null;
/* 2869 */     int tilex = player.getTileX();
/* 2870 */     int tiley = player.getTileY();
/* 2871 */     startTile = Zones.getTileOrNull(tilex, tiley, player.isOnSurface());
/* 2872 */     if (startTile != null) {
/*      */       
/* 2874 */       Structure struct = startTile.getStructure();
/* 2875 */       if (struct != null && struct.isFinished())
/*      */       {
/*      */         
/* 2878 */         return !struct.mayPass((Creature)player);
/*      */       }
/*      */     } 
/* 2881 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean putOutsideHouse(Player player, boolean load) {
/* 2897 */     if (player.getPower() > 1)
/* 2898 */       return false; 
/* 2899 */     VolaTile startTile = null;
/* 2900 */     int tilex = player.getTileX();
/* 2901 */     int tiley = player.getTileY();
/* 2902 */     startTile = Zones.getTileOrNull(tilex, tiley, player.isOnSurface());
/* 2903 */     if (startTile != null) {
/*      */       
/* 2905 */       Structure struct = startTile.getStructure();
/* 2906 */       if (struct != null && struct.isFinished() && struct.isTypeHouse()) {
/*      */         
/* 2908 */         Item[] keys = player.getKeys();
/* 2909 */         for (Item lKey : keys) {
/*      */           
/* 2911 */           if (lKey.getWurmId() == struct.getWritId())
/* 2912 */             return false; 
/*      */         } 
/* 2914 */         if (struct.mayPass((Creature)player))
/* 2915 */           return false; 
/* 2916 */         Door[] doors = struct.getAllDoors();
/* 2917 */         for (Door door : doors) {
/*      */           
/* 2919 */           if (!door.isLocked())
/*      */           {
/*      */             
/* 2922 */             if (door.getOuterTile().getStructure() != struct) {
/* 2923 */               return false;
/*      */             }
/*      */           }
/*      */         } 
/* 2927 */         for (Door door : doors) {
/*      */           
/* 2929 */           if (door.getOuterTile().getStructure() != struct) {
/*      */             
/* 2931 */             startTile = door.getOuterTile();
/*      */             break;
/*      */           } 
/*      */         } 
/* 2935 */         if (startTile == null) {
/* 2936 */           startTile = Zones.getOrCreateTile(Server.rand.nextBoolean() ? (struct.getMaxX() + 1) : (struct.getMinX() - 1), 
/* 2937 */               Server.rand.nextBoolean() ? (struct.getMaxY() + 1) : (struct.getMinY() - 1), true);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2950 */         float posX = ((startTile.getTileX() << 2) + 2);
/* 2951 */         float posY = ((startTile.getTileY() << 2) + 2);
/* 2952 */         if (Servers.localServer.entryServer) {
/*      */           
/* 2954 */           posX = (startTile.getTileX() << 2) + 0.5F + Server.rand.nextFloat() * 3.0F;
/* 2955 */           posY = (startTile.getTileY() << 2) + 0.5F + Server.rand.nextFloat() * 3.0F;
/*      */         } 
/*      */         
/* 2958 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/* 2960 */           logger.fine("Setting " + player.getName() + " outside structure " + struct.getName() + " on " + startTile
/* 2961 */               .getTileX() + ", " + startTile.getTileY() + ". New or reconnect=" + (!load ? 1 : 0));
/*      */         }
/*      */         
/* 2964 */         MountTransfer mt = MountTransfer.getTransferFor(player.getWurmId());
/* 2965 */         if (mt != null)
/*      */         {
/* 2967 */           mt.remove(player.getWurmId());
/*      */         }
/* 2969 */         player.setPositionX(posX);
/* 2970 */         player.setPositionY(posY);
/* 2971 */         if (player.getVehicle() != -10L) {
/* 2972 */           player.disembark(false);
/*      */         }
/*      */         try {
/* 2975 */           player.setPositionZ(Zones.calculateHeight(posX, posY, true));
/*      */         }
/* 2977 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 2979 */           logger.log(Level.WARNING, player.getName() + " ending up outside map: " + player.getStatus().getPositionX() + ", " + player
/* 2980 */               .getStatus().getPositionY());
/* 2981 */           player.calculateSpawnPoints();
/* 2982 */           if (player.spawnpoints != null) {
/*      */             
/* 2984 */             Iterator<Spawnpoint> iterator = player.spawnpoints.iterator(); if (iterator.hasNext()) { Spawnpoint p = iterator.next();
/*      */               
/* 2986 */               tilex = p.tilex;
/* 2987 */               tiley = p.tiley;
/* 2988 */               posX = (tilex * 4);
/* 2989 */               posY = (tiley * 4);
/*      */               
/* 2991 */               player.setPositionX(posX + 2.0F);
/* 2992 */               player.setPositionY(posY + 2.0F);
/*      */               
/*      */               try {
/* 2995 */                 player.setPositionZ(Zones.calculateHeight(posX, posY, true));
/*      */               }
/* 2997 */               catch (NoSuchZoneException nsz2) {
/*      */                 
/* 2999 */                 logger.log(Level.WARNING, player.getName() + " Respawn failed at spawnpoint " + tilex + "," + tiley);
/*      */               } 
/*      */               
/* 3002 */               player.getCommunicator().sendNormalServerMessage("You have been respawned since your position was out of bounds.");
/*      */               
/* 3004 */               return true; }
/*      */           
/*      */           } 
/*      */         } 
/* 3008 */         putOutsideEnemyDeed(player, load);
/* 3009 */         return true;
/*      */       } 
/*      */     } 
/* 3012 */     return putOutsideEnemyDeed(player, load);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final VolaTile getStartTileForDeed(Player player) {
/* 3017 */     int tilex = player.getTileX();
/* 3018 */     int tiley = player.getTileY();
/* 3019 */     Village v = Zones.getVillage(tilex, tiley, player.isOnSurface());
/* 3020 */     if (v != null)
/*      */     {
/*      */ 
/*      */       
/* 3024 */       if (v.isEnemy((Creature)player, true)) {
/*      */         
/* 3026 */         player.getCommunicator().sendSafeServerMessage("You find yourself outside the " + v.getName() + " settlement.");
/*      */         
/* 3028 */         int ntx = v.getEndX() + Server.rand.nextInt(10);
/* 3029 */         if (Server.rand.nextBoolean())
/* 3030 */           ntx = v.getStartX() - Server.rand.nextInt(10); 
/* 3031 */         int nty = v.getEndY() + Server.rand.nextInt(10);
/* 3032 */         if (Server.rand.nextBoolean())
/* 3033 */           nty = v.getStartY() - Server.rand.nextInt(10); 
/* 3034 */         VolaTile startTile = Zones.getTileOrNull(ntx, nty, player.isOnSurface());
/*      */         
/* 3036 */         if (startTile != null) {
/*      */           
/* 3038 */           Structure struct = startTile.getStructure();
/* 3039 */           if (struct == null || !struct.isFinished())
/*      */           {
/* 3041 */             return startTile;
/*      */           }
/*      */         } else {
/*      */           
/* 3045 */           return Zones.getOrCreateTile(ntx, nty, true);
/*      */         } 
/*      */         
/* 3048 */         ntx = v.getEndX() + Server.rand.nextInt(10);
/* 3049 */         if (Server.rand.nextBoolean())
/* 3050 */           ntx = v.getStartX() - Server.rand.nextInt(10); 
/* 3051 */         nty = v.getStartY() - 10 + Server.rand.nextInt(v.getEndY() + 20 - v.getStartY());
/* 3052 */         startTile = Zones.getTileOrNull(ntx, nty, player.isOnSurface());
/*      */         
/* 3054 */         if (startTile != null) {
/*      */           
/* 3056 */           Structure struct = startTile.getStructure();
/* 3057 */           if (struct == null || !struct.isFinished())
/*      */           {
/* 3059 */             return startTile;
/*      */           }
/*      */         } else {
/*      */           
/* 3063 */           return Zones.getOrCreateTile(ntx, nty, true);
/*      */         } 
/*      */         
/* 3066 */         for (int x = 0; x < 20; x++) {
/*      */           
/* 3068 */           nty = v.getEndY() + Server.rand.nextInt(10);
/* 3069 */           if (Server.rand.nextBoolean())
/* 3070 */             nty = v.getStartY() - Server.rand.nextInt(10); 
/* 3071 */           ntx = v.getStartX() - 10 + Server.rand.nextInt(v.getEndX() + 20 - v.getStartX());
/* 3072 */           startTile = Zones.getTileOrNull(ntx, nty, player.isOnSurface());
/*      */           
/* 3074 */           if (startTile != null) {
/*      */             
/* 3076 */             Structure struct = startTile.getStructure();
/* 3077 */             if (struct == null || !struct.isFinished())
/*      */             {
/* 3079 */               return startTile;
/*      */             }
/*      */           } else {
/*      */             
/* 3083 */             return Zones.getOrCreateTile(ntx, nty, true);
/*      */           } 
/*      */         } 
/*      */       }  } 
/* 3087 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean putOutsideEnemyDeed(Player player, boolean load) {
/* 3092 */     if (load && player.getPower() == 0) {
/*      */       
/* 3094 */       VolaTile startTile = getStartTileForDeed(player);
/* 3095 */       if (startTile != null) {
/*      */ 
/*      */         
/* 3098 */         float posX = ((startTile.getTileX() << 2) + 2);
/* 3099 */         float posY = ((startTile.getTileY() << 2) + 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3107 */         MountTransfer mt = MountTransfer.getTransferFor(player.getWurmId());
/* 3108 */         if (mt != null)
/*      */         {
/* 3110 */           mt.remove(player.getWurmId());
/*      */         }
/* 3112 */         player.setPositionX(posX);
/* 3113 */         player.setPositionY(posY);
/* 3114 */         if (player.getVehicle() != -10L) {
/* 3115 */           player.disembark(false);
/*      */         }
/*      */         try {
/* 3118 */           player.setPositionZ(Zones.calculateHeight(posX, posY, true));
/*      */         }
/* 3120 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 3122 */           logger.log(Level.WARNING, player.getName() + " ending up outside map: " + player.getStatus().getPositionX() + ", " + player
/* 3123 */               .getStatus().getPositionY());
/* 3124 */           player.calculateSpawnPoints();
/* 3125 */           if (player.spawnpoints != null) {
/*      */             
/* 3127 */             Iterator<Spawnpoint> iterator = player.spawnpoints.iterator(); if (iterator.hasNext()) { Spawnpoint p = iterator.next();
/*      */               
/* 3129 */               int tilex = p.tilex;
/* 3130 */               int tiley = p.tiley;
/* 3131 */               posX = (tilex * 4);
/* 3132 */               posY = (tiley * 4);
/*      */               
/* 3134 */               player.setPositionX(posX + 2.0F);
/* 3135 */               player.setPositionY(posY + 2.0F);
/*      */               
/*      */               try {
/* 3138 */                 player.setPositionZ(Zones.calculateHeight(posX, posY, true));
/*      */               }
/* 3140 */               catch (NoSuchZoneException nsz2) {
/*      */                 
/* 3142 */                 logger.log(Level.WARNING, player.getName() + " Respawn failed at spawnpoint " + tilex + "," + tiley);
/*      */               } 
/*      */               
/* 3145 */               player.getCommunicator().sendNormalServerMessage("You have been respawned since your position was out of bounds.");
/*      */               
/* 3147 */               return true; }
/*      */           
/*      */           } 
/*      */         } 
/* 3151 */         return true;
/*      */       } 
/*      */     } 
/* 3154 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void putOutsideWall(Player player) {
/* 3173 */     if (player.getStatus().getLayer() < 0) {
/*      */       
/* 3175 */       int tilex = player.getTileX();
/* 3176 */       int tiley = player.getTileY();
/* 3177 */       int tile = Server.caveMesh.getTile(tilex, tiley);
/* 3178 */       if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*      */         
/* 3180 */         boolean saved = false;
/* 3181 */         for (int x = -1; x <= 1; x++) {
/*      */           
/* 3183 */           for (int y = -1; y <= 1; y++) {
/*      */             
/* 3185 */             tile = Server.caveMesh.getTile(tilex + x, tiley + y);
/* 3186 */             if (!Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*      */               
/* 3188 */               float posX = ((tilex + x) * 4);
/* 3189 */               float posY = ((tiley + y) * 4);
/*      */               
/* 3191 */               player.setPositionX(posX + 2.0F);
/* 3192 */               player.setPositionY(posY + 2.0F);
/*      */               
/* 3194 */               saved = true;
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/* 3200 */         if (!saved)
/*      */         {
/* 3202 */           player.setLayer(0, false);
/*      */         }
/*      */         
/*      */         try {
/* 3206 */           player.setPositionZ(Zones.calculateHeight(player.getStatus().getPositionX(), player.getStatus()
/* 3207 */                 .getPositionY(), player.isOnSurface()));
/*      */         }
/* 3209 */         catch (NoSuchZoneException nsz) {
/*      */           
/* 3211 */           logger.log(Level.WARNING, player.getName() + " ending up outside map: " + player.getStatus().getPositionX() + ", " + player
/* 3212 */               .getStatus().getPositionY() + ". Respawning.");
/* 3213 */           player.calculateSpawnPoints();
/* 3214 */           if (player.spawnpoints != null) {
/*      */             
/* 3216 */             Iterator<Spawnpoint> iterator = player.spawnpoints.iterator(); if (iterator.hasNext()) { Spawnpoint p = iterator.next();
/*      */               
/* 3218 */               tilex = p.tilex;
/* 3219 */               tiley = p.tiley;
/* 3220 */               float posX = (tilex * 4);
/* 3221 */               float posY = (tiley * 4);
/*      */               
/* 3223 */               player.setPositionX(posX + 2.0F);
/* 3224 */               player.setPositionY(posY + 2.0F);
/*      */               
/*      */               try {
/* 3227 */                 player.setPositionZ(Zones.calculateHeight(posX, posY, true));
/*      */               }
/* 3229 */               catch (NoSuchZoneException nsz2) {
/*      */                 
/* 3231 */                 logger.log(Level.WARNING, player.getName() + " Respawn failed at spawnpoint " + tilex + "," + tiley);
/*      */               } 
/*      */               
/* 3234 */               player.getCommunicator().sendNormalServerMessage("You have been respawned since your position was out of bounds.");
/*      */               return; }
/*      */           
/*      */           } 
/*      */         } 
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean putOutsideFence(Player player) {
/* 3253 */     boolean moved = true;
/*      */     
/* 3255 */     int tilex = player.getTileX();
/* 3256 */     int tiley = player.getTileY();
/*      */     
/* 3258 */     float posX = (tilex * 4);
/* 3259 */     float posY = (tiley * 4);
/* 3260 */     if (player.getBridgeId() <= 0L) {
/*      */       
/* 3262 */       posX = posX + 0.5F + Server.rand.nextFloat() * 3.0F;
/* 3263 */       posY = posY + 0.5F + Server.rand.nextFloat() * 3.0F;
/*      */     }
/*      */     else {
/*      */       
/* 3267 */       posX += 2.0F;
/* 3268 */       posY += 2.0F;
/*      */     } 
/* 3270 */     player.setPositionX(posX);
/* 3271 */     player.setPositionY(posY);
/*      */     
/* 3273 */     if (player.getFloorLevel() <= 0)
/*      */       
/*      */       try {
/* 3276 */         player.setPositionZ(Zones.calculateHeight(posX, posY, true));
/*      */       }
/* 3278 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 3280 */         logger.log(Level.WARNING, player.getName() + " ending up outside map: " + player.getStatus().getPositionX() + ", " + player
/*      */             
/* 3282 */             .getStatus().getPositionY() + ". Respawning.");
/* 3283 */         player.calculateSpawnPoints();
/* 3284 */         if (player.spawnpoints != null) {
/*      */           
/* 3286 */           Iterator<Spawnpoint> iterator = player.spawnpoints.iterator(); if (iterator.hasNext()) { Spawnpoint p = iterator.next();
/*      */             
/* 3288 */             tilex = p.tilex;
/* 3289 */             tiley = p.tiley;
/* 3290 */             posX = (tilex * 4);
/* 3291 */             posY = (tiley * 4);
/*      */             
/* 3293 */             player.setPositionX(posX + 2.0F);
/* 3294 */             player.setPositionY(posY + 2.0F);
/*      */             
/*      */             try {
/* 3297 */               player.setPositionZ(Zones.calculateHeight(posX, posY, true));
/*      */             }
/* 3299 */             catch (NoSuchZoneException nsz2) {
/*      */               
/* 3301 */               logger.log(Level.WARNING, player.getName() + " Respawn failed at spawnpoint " + tilex + "," + tiley);
/*      */             } 
/* 3303 */             player.getCommunicator().sendNormalServerMessage("You have been respawned since your position was out of bounds.");
/*      */             
/* 3305 */             return true; }
/*      */         
/*      */         } 
/*      */       }  
/* 3309 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean willGoOnBoat(Player player) {
/* 3314 */     MountTransfer mt = MountTransfer.getTransferFor(player.getWurmId());
/* 3315 */     if (mt != null) {
/*      */       
/* 3317 */       long vehicleId = mt.getVehicleId();
/* 3318 */       Vehicle vehic = Vehicles.getVehicleForId(vehicleId);
/* 3319 */       if (vehic != null)
/*      */       {
/* 3321 */         if (!vehic.creature) {
/*      */           
/*      */           try {
/* 3324 */             Item i = Items.getItem(vehicleId);
/* 3325 */             if (i.isBoat()) {
/* 3326 */               return true;
/*      */             }
/* 3328 */           } catch (Exception ex) {
/*      */             
/* 3330 */             logger.log(Level.WARNING, "Failed to locate boat with id " + vehicleId + " for player " + player
/* 3331 */                 .getName(), ex);
/*      */           } 
/*      */         } else {
/*      */           
/*      */           try {
/* 3336 */             Creatures.getInstance().getCreature(vehicleId);
/* 3337 */             return true;
/*      */           }
/* 3339 */           catch (Exception ex) {
/*      */             
/* 3341 */             logger.log(Level.WARNING, "Failed to locate creature with id " + vehicleId + " for player " + player
/* 3342 */                 .getName(), ex);
/*      */           } 
/*      */         }  } 
/*      */     } 
/* 3346 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean putInBoatAndAssignSeat(Player player, boolean reconnect) {
/* 3403 */     MountTransfer mt = MountTransfer.getTransferFor(player.getWurmId());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3409 */     if (mt == null && (player.getVehicle() == -10L || reconnect)) {
/*      */       
/* 3411 */       long vehicleId = (player.getSaveFile()).lastvehicle;
/* 3412 */       if (reconnect)
/* 3413 */         vehicleId = player.getVehicle(); 
/* 3414 */       Vehicle vehic = Vehicles.getVehicleForId(vehicleId);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3419 */       if (vehic != null) {
/*      */         
/*      */         try {
/*      */           
/* 3423 */           Item i = null;
/* 3424 */           Creature creature = null;
/* 3425 */           int freeseatnum = -1;
/* 3426 */           float offz = 0.0F;
/* 3427 */           float offx = 0.0F;
/* 3428 */           float offy = 0.0F;
/* 3429 */           int start = 9999;
/* 3430 */           float posx = 50.0F;
/* 3431 */           float posy = 50.0F;
/* 3432 */           int layer = 0;
/* 3433 */           if (WurmId.getType(vehicleId) == 2) {
/*      */             
/* 3435 */             i = Items.getItem(vehicleId);
/* 3436 */             if (reconnect || i.isBoat()) {
/*      */               
/* 3438 */               posx = i.getPosX();
/* 3439 */               posy = i.getPosY();
/* 3440 */               layer = i.isOnSurface() ? 0 : -1;
/* 3441 */               if ((VehicleBehaviour.hasKeyForVehicle((Creature)player, i) || 
/* 3442 */                 VehicleBehaviour.mayDriveVehicle((Creature)player, i, null)) && 
/* 3443 */                 VehicleBehaviour.canBeDriverOfVehicle((Creature)player, vehic)) {
/* 3444 */                 start = 1;
/* 3445 */               } else if (VehicleBehaviour.hasKeyForVehicle((Creature)player, i) || 
/* 3446 */                 VehicleBehaviour.mayEmbarkVehicle((Creature)player, i)) {
/* 3447 */                 start = 1;
/*      */               } else {
/* 3449 */                 logger.log(Level.INFO, player.getName() + " may no longer embark the vehicle " + i.getName());
/*      */               } 
/*      */             } else {
/* 3452 */               return false;
/*      */             } 
/* 3454 */           } else if (WurmId.getType(vehicleId) == 1) {
/*      */             
/* 3456 */             creature = Creatures.getInstance().getCreature(vehicleId);
/* 3457 */             posx = creature.getPosX();
/* 3458 */             posy = creature.getPosY();
/* 3459 */             layer = creature.getLayer();
/* 3460 */             if (VehicleBehaviour.mayDriveVehicle((Creature)player, creature) && 
/* 3461 */               VehicleBehaviour.canBeDriverOfVehicle((Creature)player, vehic)) {
/* 3462 */               start = 1;
/* 3463 */             } else if (VehicleBehaviour.mayEmbarkVehicle((Creature)player, creature)) {
/* 3464 */               start = 1;
/*      */             } else {
/* 3466 */               logger.log(Level.INFO, player.getName() + " may no longer mount the " + creature.getName());
/*      */             } 
/*      */           }  int x;
/* 3469 */           for (x = 0; x < vehic.seats.length; x++) {
/*      */             
/* 3471 */             if ((vehic.seats[x]).occupant == player.getWurmId()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 3477 */               freeseatnum = x;
/* 3478 */               offz = (vehic.seats[x]).offz;
/* 3479 */               offy += (vehic.seats[x]).offy;
/* 3480 */               offx += (vehic.seats[x]).offx;
/*      */             } 
/*      */           } 
/*      */           
/* 3484 */           if (freeseatnum < 0)
/*      */           {
/* 3486 */             for (x = start; x < vehic.seats.length; x++) {
/*      */               
/* 3488 */               if ((vehic.seats[x]).occupant == -10L)
/*      */               {
/* 3490 */                 if (freeseatnum < 0) {
/*      */                   
/* 3492 */                   freeseatnum = x;
/* 3493 */                   offz = (vehic.seats[x]).offz;
/* 3494 */                   offy += (vehic.seats[x]).offy;
/* 3495 */                   offx += (vehic.seats[x]).offx;
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           }
/*      */           
/* 3501 */           player.setPositionX(posx + offx);
/* 3502 */           player.setPositionY(posy + offy);
/*      */           
/* 3504 */           VolaTile tile = Zones.getOrCreateTile((int)(player.getPosX() / 4.0F), (int)(player.getPosY() / 4.0F), 
/* 3505 */               (player.getLayer() >= 0));
/* 3506 */           boolean skipSetZ = false;
/* 3507 */           if (tile != null) {
/*      */             
/* 3509 */             Structure structure = tile.getStructure();
/* 3510 */             if (structure != null)
/*      */             {
/* 3512 */               skipSetZ = (structure.isTypeHouse() || structure.getWurmId() == player.getBridgeId());
/*      */             }
/*      */           } 
/*      */           
/* 3516 */           if (!skipSetZ)
/*      */           {
/* 3518 */             player.setPositionZ(Math.max(Zones.calculateHeight(posx + offx, posy + offy, (layer >= 0)) + offz, (freeseatnum >= 0) ? offz : -1.45F));
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 3523 */           if (freeseatnum >= 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3528 */             mt = new MountTransfer(vehicleId, (freeseatnum == 0) ? player.getWurmId() : -10L);
/* 3529 */             mt.addToSeat(player.getWurmId(), freeseatnum);
/*      */           } 
/* 3531 */           return true;
/*      */         }
/* 3533 */         catch (NoSuchItemException nsi) {
/*      */           
/* 3535 */           logger.log(Level.WARNING, "No item to board for " + player.getName() + ":" + vehicleId, (Throwable)nsi);
/*      */         }
/* 3537 */         catch (Exception ex) {
/*      */           
/* 3539 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */         } 
/*      */       }
/*      */     } 
/* 3543 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean checkPutOnBoat(Player player) {
/* 3548 */     MountTransfer mt = MountTransfer.getTransferFor(player.getWurmId());
/* 3549 */     if (mt != null) {
/*      */       
/* 3551 */       long vehicleId = mt.getVehicleId();
/* 3552 */       Vehicle vehic = Vehicles.getVehicleForId(vehicleId);
/* 3553 */       if (vehic != null) {
/*      */         
/* 3555 */         if (vehic.isChair())
/*      */         {
/*      */           
/* 3558 */           return false;
/*      */         }
/*      */ 
/*      */         
/*      */         try {
/* 3563 */           Item i = null;
/* 3564 */           Creature creature = null;
/* 3565 */           if (WurmId.getType(vehicleId) == 2) {
/* 3566 */             i = Items.getItem(vehicleId);
/* 3567 */           } else if (WurmId.getType(vehicleId) == 1) {
/* 3568 */             creature = Creatures.getInstance().getCreature(vehicleId);
/* 3569 */           }  int seatnum = mt.getSeatFor(player.getWurmId());
/* 3570 */           if (seatnum >= 0)
/*      */           {
/* 3572 */             (vehic.seats[seatnum]).occupant = player.getWurmId();
/* 3573 */             if (mt.getPilotId() == player.getWurmId()) {
/*      */               
/* 3575 */               vehic.pilotId = player.getWurmId();
/* 3576 */               player.setVehicleCommander(true);
/*      */             } 
/*      */             
/* 3579 */             MountAction m = new MountAction(creature, i, vehic, seatnum, (mt.getPilotId() == player.getWurmId()), (vehic.seats[seatnum]).offz);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3586 */             player.setMountAction(m);
/*      */ 
/*      */             
/* 3589 */             player.setVehicle(vehicleId, false, vehic.seats[seatnum].getType());
/* 3590 */             return true;
/*      */           }
/*      */         
/* 3593 */         } catch (NoSuchItemException nsi) {
/*      */           
/* 3595 */           logger.log(Level.WARNING, "No item to board for " + player.getName() + ":" + vehicleId, (Throwable)nsi);
/*      */         }
/* 3597 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 3599 */           logger.log(Level.WARNING, "No creature to mount for " + player.getName() + ":" + vehicleId, (Throwable)nsc);
/*      */         }
/* 3601 */         catch (Exception ex) {
/*      */           
/* 3603 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */         } 
/*      */       } 
/*      */     } 
/* 3607 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sendWho(Player player, boolean loggingin) {
/* 3617 */     if (player.isUndead())
/*      */       return; 
/* 3619 */     String[] names = Players.getInstance().getPlayerNames();
/* 3620 */     String playerList = "none!";
/* 3621 */     Communicator comm = player.getCommunicator();
/* 3622 */     int otherServers = 0;
/* 3623 */     String localServerName = Servers.localServer.name;
/*      */     
/* 3625 */     if (localServerName.length() > 1) {
/*      */       
/* 3627 */       localServerName = localServerName.toLowerCase();
/* 3628 */       localServerName = Character.toUpperCase(localServerName.charAt(0)) + localServerName.substring(1);
/*      */     } 
/*      */ 
/*      */     
/* 3632 */     int epic = 0;
/* 3633 */     for (ServerEntry entry : Servers.getAllServers()) {
/*      */       
/* 3635 */       if (!entry.EPIC) {
/*      */         
/* 3637 */         if (!entry.isLocal)
/*      */         {
/* 3639 */           otherServers += entry.currentPlayers;
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 3644 */         epic += entry.currentPlayers;
/*      */       } 
/*      */     } 
/*      */     
/* 3648 */     if (player.getPower() > 0) {
/*      */       
/* 3650 */       comm.sendSafeServerMessage("These other players are online on " + localServerName + ":");
/* 3651 */       int nums = names.length;
/* 3652 */       if (names.length > 1)
/*      */       {
/* 3654 */         playerList = "";
/* 3655 */         for (int x = 0; x < names.length; x++) {
/*      */           
/* 3657 */           if (!names[x].equals(player.getName())) {
/*      */             
/* 3659 */             PlayerInfo p = PlayerInfoFactory.createPlayerInfo(names[x]);
/* 3660 */             if (player.getPower() >= p.getPower()) {
/* 3661 */               playerList = playerList + names[x] + " ";
/*      */             } else {
/* 3663 */               nums--;
/*      */             } 
/* 3665 */           }  if (x != 0 && x % 10 == 0) {
/*      */             
/* 3667 */             comm.sendSafeServerMessage(playerList);
/* 3668 */             playerList = "";
/*      */           } 
/*      */         } 
/* 3671 */         if (playerList.length() > 0)
/* 3672 */           comm.sendSafeServerMessage(playerList); 
/* 3673 */         String ss = "";
/* 3674 */         if (names.length > 1)
/* 3675 */           ss = "s"; 
/* 3676 */         comm.sendSafeServerMessage(nums + " player" + ss + " on this server. (" + (nums + otherServers + epic) + " totally in Wurm)");
/*      */       }
/*      */       else
/*      */       {
/* 3680 */         comm.sendSafeServerMessage("none! (" + (nums + otherServers + epic) + " totally in Wurm)");
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 3686 */     else if (names.length > 1) {
/*      */       
/* 3688 */       comm.sendSafeServerMessage((names.length - 1) + " other players are online. You are on " + localServerName + " (" + (names.length + otherServers + epic) + " totally in Wurm).");
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 3694 */       comm.sendSafeServerMessage("No other players are online on " + localServerName + " (" + (1 + otherServers) + " totally in Wurm).");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sendLoggedInPeople(Player player) {
/* 3709 */     if (player.isUndead())
/*      */       return; 
/* 3711 */     if (player.isSignedIn()) {
/* 3712 */       player.getCommunicator().signIn("Just transferred.");
/* 3713 */     } else if (player.canSignIn() && player.getPower() <= 1) {
/* 3714 */       player.getCommunicator().remindToSignIn();
/* 3715 */     }  sendWho(player, true);
/* 3716 */     Village vill = player.getCitizenVillage();
/* 3717 */     if (vill != null) {
/* 3718 */       vill.sendCitizensToPlayer(player);
/*      */     }
/* 3720 */     if (player.mayHearMgmtTalk() || player.mayHearDevTalk()) {
/* 3721 */       Players.getInstance().sendGmsToPlayer(player);
/*      */     }
/* 3723 */     if (player.seesPlayerAssistantWindow())
/* 3724 */       Players.getInstance().sendPAWindow(player); 
/* 3725 */     if (player.seesGVHelpWindow() && !Servers.isThisLoginServer())
/* 3726 */       Players.getInstance().sendGVHelpWindow(player); 
/* 3727 */     Players.getInstance().sendAltarsToPlayer(player);
/* 3728 */     Players.getInstance().sendTicketsToPlayer(player);
/* 3729 */     player.checkKingdom();
/* 3730 */     if (player.isGlobalChat())
/* 3731 */       Players.getInstance().sendStartGlobalKingdomChat(player); 
/* 3732 */     if (player.isKingdomChat())
/* 3733 */       Players.getInstance().sendStartKingdomChat(player); 
/* 3734 */     if (player.isTradeChannel()) {
/* 3735 */       Players.getInstance().sendStartGlobalTradeChannel(player);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sendStatus(Player player) {
/* 3745 */     player.getStatus().sendHunger();
/* 3746 */     player.getStatus().sendThirst();
/* 3747 */     (player.getStatus()).lastSentStamina = -200;
/* 3748 */     player.getStatus().sendStamina();
/* 3749 */     player.sendDeityEffectBonuses();
/* 3750 */     player.getCommunicator().sendOwnTitles();
/* 3751 */     player.getCommunicator().sendSleepInfo();
/* 3752 */     player.sendAllPoisonEffect();
/* 3753 */     Abilities.sendEffectsToCreature((Creature)player);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendLoginAnswer(boolean ok, String message, float x, float y, float z, float rot, int layer, String bodyName, byte power, int retrySeconds) throws IOException {
/* 3796 */     sendLoginAnswer(ok, message, x, y, z, rot, layer, bodyName, power, retrySeconds, (byte)0, (byte)0, 0L, 0, (byte)0, -10L, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendLoginAnswer(boolean ok, String message, float x, float y, float z, float rot, int layer, String bodyName, byte power, int retrySeconds, byte commandType, byte templateKingdomId, long face, int teleportCounter, byte blood, long bridgeId, float groundOffset) throws IOException {
/*      */     try {
/* 3836 */       if (Constants.useQueueToSendDataToPlayers);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3841 */       byte[] messageb = message.getBytes("UTF-8");
/* 3842 */       ByteBuffer bb = this.conn.getBuffer();
/*      */       
/* 3844 */       bb.put((byte)-15);
/* 3845 */       if (ok) {
/* 3846 */         bb.put((byte)1);
/*      */       } else {
/* 3848 */         bb.put((byte)0);
/*      */       } 
/* 3850 */       bb.putShort((short)messageb.length);
/* 3851 */       bb.put(messageb);
/*      */       
/* 3853 */       bb.put((byte)layer);
/* 3854 */       bb.putLong(WurmCalendar.currentTime);
/* 3855 */       bb.putLong(System.currentTimeMillis());
/* 3856 */       bb.putFloat(rot);
/* 3857 */       bb.putFloat(x);
/* 3858 */       bb.putFloat(y);
/* 3859 */       bb.putFloat(z);
/* 3860 */       byte[] bodyb = bodyName.getBytes("UTF-8");
/* 3861 */       bb.putShort((short)bodyb.length);
/* 3862 */       bb.put(bodyb);
/* 3863 */       if (power == 0) {
/* 3864 */         bb.put((byte)0);
/* 3865 */       } else if (power == 1) {
/* 3866 */         bb.put((byte)2);
/*      */       } else {
/* 3868 */         bb.put((byte)1);
/* 3869 */       }  bb.put(commandType);
/* 3870 */       bb.putShort((short)retrySeconds);
/*      */       
/* 3872 */       bb.putLong(face);
/* 3873 */       bb.put(templateKingdomId);
/* 3874 */       bb.putInt(teleportCounter);
/* 3875 */       bb.put(blood);
/* 3876 */       bb.putLong(bridgeId);
/* 3877 */       bb.putFloat(groundOffset);
/* 3878 */       bb.putInt(Zones.worldTileSizeX);
/* 3879 */       this.conn.flush();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 3886 */     catch (IOException ioe) {
/*      */ 
/*      */ 
/*      */       
/* 3890 */       throw ioe;
/*      */     }
/* 3892 */     catch (Exception ex) {
/*      */       
/* 3894 */       logger.log(Level.WARNING, "Failed to send login answer.", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendAuthenticationAnswer(boolean wasSucces, String failedMessage) {
/* 3900 */     ByteBuffer bb = this.conn.getBuffer();
/* 3901 */     bb.put((byte)-52);
/* 3902 */     if (wasSucces) {
/* 3903 */       bb.put((byte)1);
/*      */     } else {
/* 3905 */       bb.put((byte)0);
/*      */     } 
/*      */     
/*      */     try {
/* 3909 */       byte[] failedMessageb = failedMessage.getBytes("UTF-8");
/* 3910 */       bb.putShort((short)failedMessageb.length);
/* 3911 */       bb.put(failedMessageb);
/*      */     }
/* 3913 */     catch (Exception e) {
/*      */ 
/*      */       
/* 3916 */       bb.putShort((short)0);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 3921 */       this.conn.flush();
/*      */     }
/* 3923 */     catch (Exception ex) {
/*      */       
/* 3925 */       logger.log(Level.WARNING, "Failed to send Auth answer.", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] createAndReturnPlayer(String name, String password, String pwQuestion, String pwAnswer, String email, byte kingdom, byte power, long appearance, byte gender, boolean titleKeeper, boolean addPremium, boolean passwordIsHashed) throws Exception {
/* 3953 */     return createAndReturnPlayer(name, password, pwQuestion, pwAnswer, email, kingdom, power, appearance, gender, titleKeeper, addPremium, passwordIsHashed, -10L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] createAndReturnPlayer(String name, String password, String pwQuestion, String pwAnswer, String email, byte kingdom, byte power, long appearance, byte gender, boolean titleKeeper, boolean addPremium, boolean passwordIsHashed, long wurmId) throws Exception {
/* 3986 */     if (Servers.localServer.HOMESERVER && Servers.localServer.KINGDOM != kingdom)
/* 3987 */       throw new WurmServerException("Illegal kingdom"); 
/* 3988 */     name = raiseFirstLetter(name);
/* 3989 */     if (!passwordIsHashed) {
/*      */       
/*      */       try {
/*      */         
/* 3993 */         password = hashPassword(password, encrypt(name));
/*      */       }
/* 3995 */       catch (Exception ex) {
/*      */         
/* 3997 */         throw new WurmServerException("We failed to encrypt your password. Please try another.");
/*      */       } 
/*      */     }
/* 4000 */     if (wurmId < 0L) {
/*      */       
/* 4002 */       String result = checkName2(name);
/* 4003 */       if (result.length() > 0)
/* 4004 */         throw new WurmServerException(result); 
/* 4005 */       if (Players.getInstance().getWurmIdByPlayerName(name) != -1L)
/* 4006 */         throw new WurmServerException("That name is taken."); 
/*      */     } 
/* 4008 */     Player player = Player.doNewPlayer(1);
/* 4009 */     player.setName(name);
/* 4010 */     int startx = Servers.localServer.SPAWNPOINTJENNX;
/* 4011 */     int starty = Servers.localServer.SPAWNPOINTJENNY;
/* 4012 */     Spawnpoint spawn = getInitialSpawnPoint(kingdom);
/* 4013 */     if (spawn != null) {
/*      */       
/* 4015 */       startx = spawn.tilex;
/* 4016 */       starty = spawn.tiley;
/*      */ 
/*      */     
/*      */     }
/* 4020 */     else if (kingdom == 3) {
/*      */       
/* 4022 */       if (Servers.localServer.SPAWNPOINTLIBX > 0)
/*      */       {
/* 4024 */         startx = Servers.localServer.SPAWNPOINTLIBX;
/* 4025 */         starty = Servers.localServer.SPAWNPOINTLIBY;
/*      */       }
/*      */     
/* 4028 */     } else if (kingdom == 2) {
/*      */       
/* 4030 */       if (Servers.localServer.SPAWNPOINTMOLX > 0) {
/*      */         
/* 4032 */         startx = Servers.localServer.SPAWNPOINTMOLX;
/* 4033 */         starty = Servers.localServer.SPAWNPOINTMOLY;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 4038 */     if (Servers.localServer.id == 5) {
/*      */       
/* 4040 */       startx = 2884;
/* 4041 */       starty = 3004;
/*      */     } 
/* 4043 */     float posX = (startx * 4) + Server.rand.nextFloat() * 2.0F * 4.0F - 4.0F;
/* 4044 */     float posY = (starty * 4) + Server.rand.nextFloat() * 2.0F * 4.0F - 4.0F;
/*      */     
/* 4046 */     float rot = Server.rand.nextInt(45) - 22.5F;
/* 4047 */     if (wurmId < 0L) {
/* 4048 */       player.setWurmId(WurmId.getNextPlayerId(), posX, posY, rot, 0);
/*      */     } else {
/* 4050 */       player.setWurmId(wurmId, posX, posY, rot, 0);
/*      */     } 
/* 4052 */     putOutsideWall(player);
/* 4053 */     if (player.isOnSurface()) {
/*      */       
/* 4055 */       putOutsideHouse(player, false);
/* 4056 */       putOutsideFence(player);
/*      */     } 
/* 4058 */     player.setNewPlayer(true);
/*      */     
/* 4060 */     PlayerInfo file = PlayerInfoFactory.createPlayerInfo(name);
/* 4061 */     file.initialize(name, player.getWurmId(), password, pwQuestion, pwAnswer, appearance, false);
/* 4062 */     player.getStatus().setStatusExists(true);
/* 4063 */     file.setEmailAddress(email);
/* 4064 */     file.loaded = true;
/* 4065 */     player.setSaveFile(file);
/* 4066 */     file.togglePlayerAssistantWindow(true);
/*      */     
/* 4068 */     player.loadSkills();
/*      */     
/* 4070 */     player.getBody().createBodyParts();
/* 4071 */     player.createPossessions();
/* 4072 */     player.createSomeItems(1.0F, false);
/* 4073 */     player.setPower(power);
/* 4074 */     checkReimbursement(player);
/* 4075 */     player.setSex(gender, true);
/* 4076 */     player.getStatus().setKingdom(kingdom);
/* 4077 */     player.setFlag(53, true);
/* 4078 */     player.setFlag(76, true);
/*      */     
/* 4080 */     Players.loadAllPrivatePOIForPlayer(player);
/* 4081 */     player.sendAllMapAnnotations();
/* 4082 */     ValreiMapData.sendAllMapData(player);
/*      */     
/* 4084 */     player.setFullyLoaded();
/* 4085 */     if (power > 0)
/* 4086 */       file.setReimbursed(false); 
/* 4087 */     if (titleKeeper)
/*      */     {
/* 4089 */       if (kingdom == 3) {
/* 4090 */         player.addTitle(Titles.Title.Destroyer_Faith);
/*      */       } else {
/* 4092 */         player.addTitle(Titles.Title.Keeper_Faith);
/*      */       }  } 
/* 4094 */     if (addPremium)
/* 4095 */       file.setPaymentExpire(System.currentTimeMillis()); 
/* 4096 */     player.sleep();
/* 4097 */     PlayerInfoFactory.addPlayerInfo(file);
/* 4098 */     Server.addNewbie();
/* 4099 */     return PlayerTransfer.createPlayerData(Wounds.emptyWounds, player.getSaveFile(), player.getStatus(), player
/* 4100 */         .getAllItems(), player.getSkills().getSkillsNoTemp(), null, Servers.localServer.id, 0L, kingdom);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Spawnpoint getInitialSpawnPoint(byte kingdom) {
/* 4105 */     if (!Servers.localServer.entryServer || Server.getInstance().isPS()) {
/*      */       
/* 4107 */       Village[] villages = Villages.getPermanentVillages(kingdom);
/* 4108 */       if (villages.length > 0) {
/*      */         
/* 4110 */         Village chosen = villages[Server.rand.nextInt(villages.length)];
/* 4111 */         return new Spawnpoint(chosen.getName(), (byte)1, chosen.getMotto(), 
/* 4112 */             (short)chosen.getTokenX(), (short)chosen.getTokenY(), true, chosen.kingdom);
/*      */       } 
/*      */     } 
/* 4115 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long createPlayer(String name, String password, String pwQuestion, String pwAnswer, String email, byte kingdom, byte power, long appearance, byte gender) throws Exception {
/*      */     try {
/* 4139 */       password = hashPassword(password, encrypt(raiseFirstLetter(name)));
/*      */     }
/* 4141 */     catch (Exception ex) {
/*      */       
/* 4143 */       throw new WurmServerException("We failed to encrypt your password. Please try another.");
/*      */     } 
/* 4145 */     return createPlayer(name, password, pwQuestion, pwAnswer, email, kingdom, power, appearance, gender, false, false, -10L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long createPlayer(String name, String hashedPassword, String pwQuestion, String pwAnswer, String email, byte kingdom, byte power, long appearance, byte gender, boolean titleKeeper, boolean addPremium, long wurmId) throws Exception {
/* 4172 */     if (Servers.localServer.HOMESERVER && 
/* 4173 */       Servers.localServer.KINGDOM != kingdom)
/* 4174 */       kingdom = Servers.localServer.KINGDOM; 
/* 4175 */     name = raiseFirstLetter(name);
/*      */     
/* 4177 */     if (wurmId < 0L) {
/*      */       
/* 4179 */       String result = checkName2(name);
/* 4180 */       if (result.length() > 0)
/* 4181 */         throw new WurmServerException(result); 
/* 4182 */       if (Players.getInstance().getWurmIdByPlayerName(name) != -1L)
/* 4183 */         throw new WurmServerException("That name is taken."); 
/*      */     } 
/* 4185 */     Player player = Player.doNewPlayer(1);
/*      */     
/* 4187 */     player.setName(name);
/*      */     
/* 4189 */     int startx = Servers.localServer.SPAWNPOINTJENNX;
/* 4190 */     int starty = Servers.localServer.SPAWNPOINTJENNY;
/* 4191 */     Spawnpoint spawn = getInitialSpawnPoint(kingdom);
/* 4192 */     if (spawn != null) {
/*      */       
/* 4194 */       startx = spawn.tilex;
/* 4195 */       starty = spawn.tiley;
/*      */ 
/*      */     
/*      */     }
/* 4199 */     else if (kingdom == 3) {
/*      */       
/* 4201 */       if (Servers.localServer.SPAWNPOINTLIBX > 0)
/*      */       {
/* 4203 */         startx = Servers.localServer.SPAWNPOINTLIBX;
/* 4204 */         starty = Servers.localServer.SPAWNPOINTLIBY;
/*      */       }
/*      */     
/* 4207 */     } else if (kingdom == 2) {
/*      */       
/* 4209 */       if (Servers.localServer.SPAWNPOINTMOLX > 0) {
/*      */         
/* 4211 */         startx = Servers.localServer.SPAWNPOINTMOLX;
/* 4212 */         starty = Servers.localServer.SPAWNPOINTMOLY;
/*      */       } 
/*      */     } 
/*      */     
/* 4216 */     float posX = (startx * 4) + Server.rand.nextFloat() * 2.0F * 4.0F - 4.0F;
/* 4217 */     float posY = (starty * 4) + Server.rand.nextFloat() * 2.0F * 4.0F - 4.0F;
/*      */     
/* 4219 */     float rot = 4.0F;
/* 4220 */     if (wurmId < 0L) {
/* 4221 */       player.setWurmId(WurmId.getNextPlayerId(), posX, posY, 4.0F, 0);
/*      */     } else {
/* 4223 */       player.setWurmId(wurmId, posX, posY, 4.0F, 0);
/*      */     } 
/* 4225 */     Players.getInstance().addPlayer(player);
/*      */     
/* 4227 */     player.setNewPlayer(true);
/*      */     
/* 4229 */     PlayerInfo file = PlayerInfoFactory.createPlayerInfo(name);
/* 4230 */     file.initialize(name, player.getWurmId(), hashedPassword, pwQuestion, pwAnswer, appearance, false);
/* 4231 */     player.getStatus().setStatusExists(true);
/* 4232 */     file.loaded = true;
/* 4233 */     player.setSaveFile(file);
/* 4234 */     file.togglePlayerAssistantWindow(true);
/* 4235 */     file.setEmailAddress(email);
/* 4236 */     putOutsideWall(player);
/* 4237 */     if (player.isOnSurface()) {
/*      */       
/* 4239 */       putOutsideHouse(player, false);
/* 4240 */       putOutsideFence(player);
/*      */     } 
/* 4242 */     player.loadSkills();
/*      */     
/* 4244 */     player.createPossessions();
/*      */ 
/*      */     
/* 4247 */     player.getBody().createBodyParts();
/* 4248 */     player.createSomeItems(1.0F, false);
/* 4249 */     player.setPower(power);
/* 4250 */     checkReimbursement(player);
/* 4251 */     player.setSex(gender, true);
/* 4252 */     player.getStatus().setKingdom(kingdom);
/* 4253 */     player.setFlag(53, true);
/* 4254 */     player.setFlag(76, true);
/*      */     
/* 4256 */     Players.loadAllPrivatePOIForPlayer(player);
/* 4257 */     player.sendAllMapAnnotations();
/* 4258 */     ValreiMapData.sendAllMapData(player);
/*      */     
/* 4260 */     Kingdom k = Kingdoms.getKingdom(kingdom);
/* 4261 */     if (k != null)
/* 4262 */       if (k.isCustomKingdom()) {
/*      */         
/* 4264 */         player.calculateSpawnPoints();
/* 4265 */         Set<Spawnpoint> spawns = player.spawnpoints;
/* 4266 */         if (spawns != null)
/*      */         {
/* 4268 */           for (Spawnpoint sp : spawns) {
/*      */             
/* 4270 */             if (sp.tilex > 20 && sp.tilex < Zones.worldTileSizeX - 20 && 
/* 4271 */               sp.tiley > 20 && sp.tiley < Zones.worldTileSizeY - 20) {
/*      */               
/* 4273 */               float nposX = (sp.tilex * 4 + 1) + Server.rand.nextFloat() * 2.0F;
/* 4274 */               float nposY = (sp.tiley * 4 + 1) + Server.rand.nextFloat() * 2.0F;
/* 4275 */               player.getStatus().setPositionXYZ(nposX, nposY, Zones.calculateHeight(nposX, nposY, true));
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } else {
/* 4283 */         if (kingdom == 3) {
/*      */           
/* 4285 */           player.setDeity(Deities.getDeity(4));
/* 4286 */           player.setFaith(1.0F);
/*      */         } 
/*      */         
/* 4289 */         if (Servers.localServer.entryServer)
/*      */         {
/* 4291 */           if (Players.getInstance().getNumberOfPlayers() > 100) {
/*      */             
/* 4293 */             player.calculateSpawnPoints();
/* 4294 */             Set<Spawnpoint> spawns = player.spawnpoints;
/*      */             
/* 4296 */             if (spawns != null)
/*      */             {
/* 4298 */               if (spawns.size() > 0) {
/*      */                 
/* 4300 */                 int rand = Server.rand.nextInt(spawns.size());
/* 4301 */                 int current = 0;
/* 4302 */                 for (Spawnpoint sp : spawns) {
/*      */                   
/* 4304 */                   if (rand == current) {
/*      */                     
/* 4306 */                     startx = sp.tilex;
/* 4307 */                     starty = sp.tiley;
/* 4308 */                     float posNX = (startx * 4) + Server.rand.nextFloat() * 2.0F * 4.0F - 4.0F;
/* 4309 */                     float posNY = (starty * 4) + Server.rand.nextFloat() * 2.0F * 4.0F - 4.0F;
/* 4310 */                     player.getStatus().getPosition().setPosX(posNX);
/* 4311 */                     player.getStatus().getPosition().setPosY(posNY);
/* 4312 */                     player.updateEffects();
/*      */                     break;
/*      */                   } 
/* 4315 */                   current++;
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         }
/*      */       }  
/* 4322 */     player.setFullyLoaded();
/* 4323 */     if (power > 0)
/* 4324 */       file.setReimbursed(false); 
/* 4325 */     if (titleKeeper)
/*      */     {
/* 4327 */       if (kingdom == 3) {
/* 4328 */         player.addTitle(Titles.Title.Destroyer_Faith);
/*      */       } else {
/* 4330 */         player.addTitle(Titles.Title.Keeper_Faith);
/*      */       }  } 
/* 4332 */     if (addPremium)
/* 4333 */       file.setPaymentExpire(System.currentTimeMillis()); 
/* 4334 */     player.sleep();
/* 4335 */     PlayerInfoFactory.addPlayerInfo(file);
/* 4336 */     Server.addNewbie();
/* 4337 */     Players.getInstance().removePlayer(player);
/* 4338 */     return player.getWurmId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkName(String name) {
/* 4349 */     String result = checkName2(name);
/*      */     
/* 4351 */     if (result.length() > 0) {
/*      */ 
/*      */       
/*      */       try {
/* 4355 */         sendLoginAnswer(false, result, 0.0F, 0.0F, 0.0F, 0.0F, 0, "model.player.broken", (byte)0, 0);
/*      */       }
/* 4357 */       catch (IOException ioe) {
/*      */ 
/*      */         
/* 4360 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/* 4362 */           logger.log(Level.FINE, this.conn.getIp() + ", problem sending login denied message: " + result, ioe);
/*      */         }
/*      */       } 
/* 4365 */       return false;
/*      */     } 
/* 4367 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void sendAllItemModelNames(Player player) {
/* 4372 */     for (ItemTemplate item : ItemTemplateFactory.getInstance().getTemplates()) {
/*      */       
/* 4374 */       if (!item.isNoTake())
/*      */       {
/* 4376 */         player.getCommunicator().sendItemTemplateList(item.getTemplateId(), item.getModelName());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void sendAllEquippedArmor(Player player) {
/* 4383 */     for (Item item : player.getBody().getContainersAndWornItems()) {
/*      */       
/* 4385 */       if (item != null) {
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */           
/* 4391 */           byte armorSlot = item.isArmour() ? BodyTemplate.convertToArmorEquipementSlot((byte)item.getParent().getPlace()) : BodyTemplate.convertToItemEquipementSlot((byte)item.getParent().getPlace());
/*      */           
/* 4393 */           player.getCommunicator().sendWearItem(-1L, item.getTemplateId(), armorSlot, 
/* 4394 */               WurmColor.getColorRed(item.getColor()), WurmColor.getColorGreen(item.getColor()), 
/* 4395 */               WurmColor.getColorBlue(item.getColor()), 
/* 4396 */               WurmColor.getColorRed(item.getColor2()), WurmColor.getColorGreen(item.getColor2()), 
/* 4397 */               WurmColor.getColorBlue(item.getColor2()), item.getMaterial(), item
/* 4398 */               .getRarity());
/*      */         
/*      */         }
/* 4401 */         catch (Exception exception) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String checkName2(String name) {
/* 4418 */     boolean notok = containsIllegalCharacters(name);
/* 4419 */     if (notok)
/*      */     {
/* 4421 */       return "Please use only letters from a to z in your name.";
/*      */     }
/* 4423 */     if (name.length() < 3)
/*      */     {
/* 4425 */       return "Please use a name at least 3 letters long.";
/*      */     }
/* 4427 */     if (name.length() > 40)
/*      */     {
/* 4429 */       return "Please use a name no longer than 40 letters.";
/*      */     }
/* 4431 */     if (!Deities.isNameOkay(name) || !CreatureTemplateFactory.isNameOkay(name))
/*      */     {
/* 4433 */       return "Illegal name.";
/*      */     }
/* 4435 */     return "";
/*      */   }
/*      */   
/*      */   public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
/* 4439 */     char[] passwordChars = password.toCharArray();
/* 4440 */     byte[] saltBytes = salt.getBytes();
/*      */     
/* 4442 */     PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, 1000, 192);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4448 */     SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
/* 4449 */     byte[] hashedPassword = key.generateSecret(spec).getEncoded();
/* 4450 */     return String.format("%x", new Object[] { new BigInteger(hashedPassword) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encrypt(String plaintext) throws Exception {
/* 4461 */     MessageDigest md = null;
/*      */     
/*      */     try {
/* 4464 */       md = MessageDigest.getInstance("SHA");
/*      */     }
/* 4466 */     catch (NoSuchAlgorithmException e) {
/*      */       
/* 4468 */       throw new WurmServerException("No such algorithm 'SHA'", e);
/*      */     } 
/*      */     
/*      */     try {
/* 4472 */       md.update(plaintext.getBytes("UTF-8"));
/*      */     }
/* 4474 */     catch (UnsupportedEncodingException e) {
/*      */       
/* 4476 */       throw new WurmServerException("No such encoding: UTF-8", e);
/*      */     } 
/* 4478 */     byte[] raw = md.digest();
/* 4479 */     String hash = (new BASE64Encoder()).encode(raw);
/* 4480 */     return hash;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getConnectionIp() {
/* 4485 */     if (this.conn != null)
/*      */     {
/* 4487 */       return this.conn.getIp();
/*      */     }
/*      */     
/* 4490 */     return "";
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\LoginHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.communication.SocketConnection;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.ActionEntry;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.PlayerMove;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.Trade;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.sounds.Sound;
/*      */ import com.wurmonline.server.structures.Door;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.NoSuchWallException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Water;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*      */ import java.io.IOException;
/*      */ import java.math.BigInteger;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class PlayerCommunicatorQueued
/*      */   extends PlayerCommunicator
/*      */ {
/*   81 */   private static final Logger logger = Logger.getLogger(PlayerCommunicatorQueued.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   private static final BlockingQueue<PlayerMessage> MESSAGES_TO_PLAYERS = new LinkedBlockingQueue<>();
/*      */   private static final String EMPTY_STRING = "";
/*   88 */   private long timeMod = 0L;
/*      */   
/*   90 */   private final DateFormat df = DateFormat.getTimeInstance();
/*   91 */   private long newSeed = (Server.rand.nextInt() & Integer.MAX_VALUE);
/*   92 */   private int newSeedPointer = 0;
/*      */   
/*      */   private PlayerMove currentmove;
/*      */   
/*      */   boolean receivedTicks = false;
/*      */   
/*      */   private static final float woundMultiplier = 0.0015259022F;
/*   99 */   private static final int emptyRock = Tiles.encode((short)-100, Tiles.Tile.TILE_CAVE_WALL.id, (byte)0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlayerCommunicatorQueued(Player aPlayer, SocketConnection aConn) {
/*  113 */     super(aPlayer, aConn);
/*      */     
/*  115 */     logger.info("Created");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteBuffer getBuffer(int aBufferCapacity) {
/*  121 */     ByteBuffer lBuffer = ByteBuffer.allocate(aBufferCapacity);
/*  122 */     lBuffer.clear();
/*  123 */     return lBuffer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean pollNextMove() {
/*  134 */     return super.pollNextMove();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMessage(Message message) {
/*  140 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  144 */         byte[] byteArray = message.getMessage().getBytes("UTF-8");
/*  145 */         byte[] window = message.getWindow().getBytes();
/*  146 */         ByteBuffer bb = getBuffer(32767);
/*  147 */         bb.put((byte)99);
/*  148 */         bb.put((byte)window.length);
/*  149 */         bb.put(window);
/*  150 */         bb.put((byte)message.getRed());
/*  151 */         bb.put((byte)message.getGreen());
/*  152 */         bb.put((byte)message.getBlue());
/*  153 */         bb.putShort((short)byteArray.length);
/*  154 */         bb.put(byteArray);
/*  155 */         bb.put((byte)0);
/*  156 */         addMessageToQueue(bb);
/*      */       }
/*  158 */       catch (Exception ex) {
/*      */         
/*  160 */         logger.log(Level.INFO, this.player
/*  161 */             .getName() + " could not send a message '" + message + "' due to : " + ex.getMessage(), ex);
/*  162 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addMessageToQueue(ByteBuffer aByteBuffer) {
/*  173 */     byte[] lArray = new byte[aByteBuffer.position()];
/*  174 */     System.arraycopy(aByteBuffer.array(), 0, lArray, 0, aByteBuffer.position());
/*  175 */     PlayerMessage lPlayerMessage = new PlayerMessage(new Long(this.player.getWurmId()), lArray);
/*      */ 
/*      */     
/*      */     try {
/*  179 */       if (Players.getInstance().getPlayer(this.player.getWurmId()) != null)
/*      */       {
/*  181 */         MESSAGES_TO_PLAYERS.add(lPlayerMessage);
/*  182 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/*  184 */           logger.finest("Added " + lPlayerMessage + " message with " + aByteBuffer.position() + " bytes to MESSAGES_TO_PLAYERS, queue size: " + MESSAGES_TO_PLAYERS
/*  185 */               .size());
/*      */         }
/*      */       }
/*      */     
/*  189 */     } catch (NoSuchPlayerException e) {
/*      */       
/*  191 */       logger.log(Level.WARNING, "Player is not in Players map so could not add " + lPlayerMessage + " message with " + aByteBuffer
/*  192 */           .position() + " bytes to MESSAGES_TO_PLAYERS - " + e.getMessage(), (Throwable)e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendGmMessage(long time, String sender, String message) {
/*  199 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  203 */         String fd = this.df.format(new Date(time));
/*  204 */         byte[] byteArray = (fd + " <" + sender + "> " + message).getBytes("UTF-8");
/*  205 */         byte[] window = "GM".getBytes();
/*  206 */         ByteBuffer bb = getBuffer(32767);
/*  207 */         bb.put((byte)99);
/*  208 */         bb.put((byte)window.length);
/*  209 */         bb.put(window);
/*  210 */         bb.put((byte)-56);
/*  211 */         bb.put((byte)-56);
/*  212 */         bb.put((byte)-56);
/*  213 */         bb.putShort((short)byteArray.length);
/*  214 */         bb.put(byteArray);
/*  215 */         bb.put((byte)0);
/*  216 */         addMessageToQueue(bb);
/*      */       }
/*  218 */       catch (Exception ex) {
/*      */         
/*  220 */         logger.log(Level.INFO, this.player
/*  221 */             .getName() + " could not send a GM message '" + message + "' due to : " + ex.getMessage(), ex);
/*  222 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendSafeServerMessage(String message) {
/*  230 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  234 */         byte[] byteArray = message.getBytes("UTF-8");
/*      */ 
/*      */ 
/*      */         
/*  238 */         ByteBuffer bb = getBuffer(32767);
/*  239 */         bb.put((byte)99);
/*  240 */         bb.put((byte)event.length);
/*  241 */         bb.put(event);
/*  242 */         bb.put((byte)102);
/*  243 */         bb.put((byte)-72);
/*  244 */         bb.put((byte)120);
/*  245 */         bb.putShort((short)byteArray.length);
/*  246 */         bb.put(byteArray);
/*  247 */         bb.put((byte)0);
/*  248 */         addMessageToQueue(bb);
/*      */       }
/*  250 */       catch (Exception ex) {
/*      */         
/*  252 */         logger.log(Level.INFO, this.player
/*  253 */             .getName() + " could not send a message '" + message + "' due to : " + ex.getMessage(), ex);
/*  254 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendNormalServerMessage(String message) {
/*  262 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  266 */         byte[] byteArray = message.getBytes("UTF-8");
/*      */ 
/*      */ 
/*      */         
/*  270 */         ByteBuffer bb = getBuffer(32767);
/*  271 */         bb.put((byte)99);
/*  272 */         bb.put((byte)event.length);
/*  273 */         bb.put(event);
/*  274 */         bb.put((byte)-1);
/*  275 */         bb.put((byte)-1);
/*  276 */         bb.put((byte)-1);
/*  277 */         bb.putShort((short)byteArray.length);
/*  278 */         bb.put(byteArray);
/*  279 */         bb.put((byte)0);
/*  280 */         addMessageToQueue(bb);
/*      */       }
/*  282 */       catch (Exception ex) {
/*      */         
/*  284 */         logger.log(Level.INFO, this.player
/*  285 */             .getName() + " could not send a message '" + message + "' due to : " + ex.getMessage(), ex);
/*  286 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCombatNormalMessage(String message) {
/*  294 */     sendCombatServerMessage(message, (byte)-1, (byte)-1, (byte)-1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCombatAlertMessage(String message) {
/*  300 */     sendCombatServerMessage(message, (byte)-1, (byte)-106, (byte)10);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCombatSafeMessage(String message) {
/*  306 */     sendCombatServerMessage(message, (byte)102, (byte)-72, (byte)120);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCombatServerMessage(String message, byte r, byte g, byte b) {
/*  312 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  316 */         byte[] byteArray = message.getBytes("UTF-8");
/*      */ 
/*      */ 
/*      */         
/*  320 */         ByteBuffer bb = getBuffer(32767);
/*  321 */         bb.put((byte)99);
/*  322 */         bb.put((byte)combat.length);
/*  323 */         bb.put(combat);
/*  324 */         bb.put(r);
/*  325 */         bb.put(g);
/*  326 */         bb.put(b);
/*  327 */         bb.putShort((short)byteArray.length);
/*  328 */         bb.put(byteArray);
/*  329 */         bb.put((byte)0);
/*  330 */         addMessageToQueue(bb);
/*      */       }
/*  332 */       catch (Exception ex) {
/*      */         
/*  334 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  335 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAlertServerMessage(String message) {
/*  343 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  347 */         byte[] byteArray = message.getBytes("UTF-8");
/*      */ 
/*      */ 
/*      */         
/*  351 */         ByteBuffer bb = getBuffer(32767);
/*  352 */         bb.put((byte)99);
/*  353 */         bb.put((byte)event.length);
/*  354 */         bb.put(event);
/*  355 */         bb.put((byte)-1);
/*  356 */         bb.put((byte)-106);
/*  357 */         bb.put((byte)10);
/*  358 */         bb.putShort((short)byteArray.length);
/*  359 */         bb.put(byteArray);
/*  360 */         bb.put((byte)0);
/*  361 */         addMessageToQueue(bb);
/*      */       }
/*  363 */       catch (Exception ex) {
/*      */         
/*  365 */         logger.log(Level.INFO, this.player.getName() + ": " + ex.getMessage(), ex);
/*  366 */         this.player.setLink(false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddToInventory(Item item, long inventoryWindow, long rootid, int price) {
/*  456 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  460 */         int weight = item.getFullWeight();
/*  461 */         if (item.isLockable() && item.getLockId() != -10L) {
/*      */           
/*      */           try {
/*      */             
/*  465 */             Item lock = Items.getItem(item.getLockId());
/*  466 */             if (!this.player.hasKeyForLock(lock)) {
/*  467 */               weight = item.getFullWeight();
/*      */             }
/*  469 */           } catch (NoSuchItemException nsi) {
/*      */             
/*  471 */             logger.log(Level.WARNING, item.getWurmId() + " has lock " + item.getLockId() + " but that doesn't exist." + nsi
/*  472 */                 .getMessage(), (Throwable)nsi);
/*      */           } 
/*      */         }
/*  475 */         byte[] byteArray = item.getName().getBytes("UTF-8");
/*      */         
/*  477 */         ByteBuffer bb = getBuffer(32767);
/*      */ 
/*      */         
/*  480 */         bb.put((byte)76);
/*  481 */         bb.putLong(inventoryWindow);
/*  482 */         long parentId = 0L;
/*  483 */         if (item.isBanked()) {
/*  484 */           parentId = inventoryWindow;
/*  485 */         } else if (rootid != 0L && item.getParentId() > 0L) {
/*  486 */           parentId = item.getParentId();
/*  487 */         }  bb.putLong(parentId);
/*      */ 
/*      */ 
/*      */         
/*  491 */         bb.putLong(item.getWurmId());
/*  492 */         bb.putShort(item.getImageNumber());
/*      */         
/*  494 */         bb.put((byte)byteArray.length);
/*  495 */         bb.put(byteArray);
/*  496 */         byteArray = item.getDescription().getBytes("UTF-8");
/*  497 */         bb.put((byte)byteArray.length);
/*  498 */         bb.put(byteArray);
/*  499 */         bb.putFloat(item.getQualityLevel());
/*  500 */         bb.putFloat(item.getDamage());
/*  501 */         bb.putInt(weight);
/*  502 */         bb.put((byte)((item.color == -1) ? 0 : 1));
/*  503 */         if (item.color != -1) {
/*      */           
/*  505 */           bb.put((byte)WurmColor.getColorRed(item.color));
/*  506 */           bb.put((byte)WurmColor.getColorGreen(item.color));
/*  507 */           bb.put((byte)WurmColor.getColorBlue(item.color));
/*      */         } 
/*  509 */         bb.put((byte)((price >= 0) ? 1 : 0));
/*  510 */         if (price >= 0)
/*      */         {
/*  512 */           bb.putInt(price);
/*      */         }
/*  514 */         addMessageToQueue(bb);
/*  515 */         if (!item.isEmpty(false))
/*      */         {
/*  517 */           if (item.isViewableBy(this.player))
/*      */           {
/*  519 */             sendHasMoreItems(inventoryWindow, item.getWurmId());
/*      */           }
/*      */         }
/*      */       }
/*  523 */       catch (Exception ex) {
/*      */         
/*  525 */         logger.log(Level.INFO, this.player.getName() + ":" + item.getName() + ": " + item.getDescription(), ex);
/*      */         
/*  527 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUpdateInventoryItem(Item item, long inventoryWindow, int price) {
/*  535 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*  540 */         byte[] byteArray = item.getName().getBytes("UTF-8");
/*      */         
/*  542 */         ByteBuffer bb = getBuffer(32767);
/*  543 */         bb.put((byte)68);
/*  544 */         bb.putLong(inventoryWindow);
/*  545 */         bb.putLong(item.getWurmId());
/*  546 */         long parentId = -1L;
/*  547 */         if (item.getParentId() > 0L)
/*  548 */           parentId = item.getParentId(); 
/*  549 */         bb.putLong(parentId);
/*  550 */         bb.put((byte)byteArray.length);
/*  551 */         bb.put(byteArray);
/*  552 */         byteArray = item.getDescription().getBytes("UTF-8");
/*  553 */         bb.put((byte)byteArray.length);
/*  554 */         bb.put(byteArray);
/*  555 */         bb.putFloat(item.getQualityLevel());
/*  556 */         bb.putFloat(item.getDamage());
/*  557 */         bb.putInt(item.getFullWeight());
/*  558 */         bb.put((byte)((item.color == -1) ? 0 : 1));
/*  559 */         if (item.color != -1) {
/*      */           
/*  561 */           bb.put((byte)WurmColor.getColorRed(item.color));
/*  562 */           bb.put((byte)WurmColor.getColorGreen(item.color));
/*  563 */           bb.put((byte)WurmColor.getColorBlue(item.color));
/*      */         } 
/*  565 */         bb.put((byte)((price >= 0) ? 1 : 0));
/*  566 */         if (price >= 0)
/*      */         {
/*  568 */           bb.putInt(price);
/*      */         }
/*      */         
/*  571 */         addMessageToQueue(bb);
/*      */       }
/*  573 */       catch (Exception ex) {
/*      */         
/*  575 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  576 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUpdateInventoryItem(Item item) {
/*  585 */     long inventoryWindow = item.getTopParent();
/*  586 */     if (this.player == null)
/*  587 */       logger.log(Level.WARNING, "Player is null ", new Exception()); 
/*  588 */     if (item.getOwnerId() == this.player.getWurmId())
/*  589 */       inventoryWindow = -1L; 
/*  590 */     sendUpdateInventoryItem(item, inventoryWindow, -1);
/*  591 */     if (item.isTraded()) {
/*  592 */       item.getTradeWindow().updateItem(item);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendRemoveFromInventory(Item item, long inventoryWindow) {
/*  598 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  605 */         ByteBuffer bb = getBuffer(17);
/*  606 */         bb.put((byte)-10);
/*  607 */         bb.putLong(inventoryWindow);
/*  608 */         bb.putLong(item.getWurmId());
/*  609 */         addMessageToQueue(bb);
/*      */       }
/*  611 */       catch (Exception ex) {
/*      */         
/*  613 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  614 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveFromInventory(Item item) {
/*  624 */     if (this.player != null) {
/*      */       
/*  626 */       long inventoryWindow = item.getTopParent();
/*  627 */       if (item.getOwnerId() == this.player.getWurmId())
/*  628 */         inventoryWindow = -1L; 
/*  629 */       sendRemoveFromInventory(item, inventoryWindow);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendOpenInventoryWindow(long inventoryWindow, String title) {
/*  636 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/*  642 */         ByteBuffer bb = getBuffer(32767);
/*  643 */         byte[] byteArray = title.getBytes("UTF-8");
/*  644 */         bb.put((byte)116);
/*  645 */         bb.putLong(inventoryWindow);
/*  646 */         bb.put((byte)byteArray.length);
/*  647 */         bb.put(byteArray);
/*  648 */         addMessageToQueue(bb);
/*      */       }
/*  650 */       catch (Exception ex) {
/*      */         
/*  652 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  653 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean sendCloseInventoryWindow(long inventoryWindow) {
/*  662 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/*  668 */         ByteBuffer bb = getBuffer(9);
/*  669 */         bb.put((byte)120);
/*  670 */         bb.putLong(inventoryWindow);
/*  671 */         addMessageToQueue(bb);
/*      */       }
/*  673 */       catch (Exception ex) {
/*      */         
/*  675 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  676 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */     
/*      */     try {
/*  681 */       return this.player.removeItemWatched(Items.getItem(inventoryWindow));
/*      */     }
/*  683 */     catch (NoSuchItemException nsi) {
/*      */       
/*  685 */       return true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendNewCreature(long id, String name, String model, float x, float y, float z, long onBridge, float rot, byte layer, boolean onGround, boolean floating, boolean isSolid, byte kingdomId, long face, byte blood, boolean isUndead, boolean isCopy, byte modtype) {
/*  695 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  699 */         byte[] byteArray = model.getBytes("UTF-8");
/*      */ 
/*      */         
/*  702 */         ByteBuffer bb = getBuffer(32767);
/*      */         
/*  704 */         bb.put((byte)108);
/*  705 */         bb.putLong(id);
/*      */         
/*  707 */         bb.put((byte)byteArray.length);
/*  708 */         bb.put(byteArray);
/*  709 */         bb.put((byte)(isSolid ? 1 : 0));
/*  710 */         bb.putFloat(y);
/*  711 */         bb.putFloat(x);
/*  712 */         bb.putFloat(rot);
/*  713 */         if (onGround) {
/*      */           
/*  715 */           if (Structure.isGroundFloorAtPosition(x, y, (layer == 0)))
/*      */           {
/*  717 */             bb.putFloat(z + 0.1F);
/*      */           }
/*      */           else
/*      */           {
/*  721 */             bb.putFloat(-3000.0F);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  726 */           bb.putFloat(z);
/*      */         } 
/*      */         
/*  729 */         byteArray = name.getBytes("UTF-8");
/*  730 */         bb.put((byte)byteArray.length);
/*  731 */         bb.put(byteArray);
/*  732 */         bb.putLong(onBridge);
/*      */         
/*  734 */         if (floating) {
/*  735 */           bb.put((byte)1);
/*      */         } else {
/*  737 */           bb.put((byte)0);
/*  738 */         }  bb.put(layer);
/*  739 */         if ((WurmId.getType(id) == 0 || isCopy) && !isUndead) {
/*      */ 
/*      */ 
/*      */           
/*  743 */           bb.put((byte)1);
/*      */         } else {
/*      */           
/*  746 */           bb.put((byte)0);
/*      */         } 
/*  748 */         bb.put((byte)0);
/*  749 */         bb.put(kingdomId);
/*  750 */         bb.putLong(face);
/*  751 */         if ((WurmId.getType(id) == 0 || isCopy) && !isUndead)
/*      */         {
/*      */           
/*  754 */           bb.putInt(Math.abs(generateSoundSourceId(id)));
/*      */         }
/*  756 */         bb.put(blood);
/*  757 */         bb.put(modtype);
/*  758 */         bb.put((byte)0);
/*  759 */         addMessageToQueue(bb);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  766 */       catch (NullPointerException np) {
/*      */         
/*  768 */         logger.log(Level.WARNING, this.player.getName() + ":" + np.getMessage(), np);
/*  769 */         this.player.setLink(false);
/*      */       }
/*  771 */       catch (Exception ex) {
/*      */         
/*  773 */         logger.log(Level.WARNING, this.player.getName() + ": " + name + " " + id + " " + ex.getMessage(), ex);
/*  774 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMoveCreature(long id, float x, float y, int rot, boolean keepMoving) {
/*  782 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  786 */         ByteBuffer bb = getBuffer(12);
/*  787 */         bb.put((byte)36);
/*  788 */         bb.putLong(id);
/*  789 */         bb.putFloat(y);
/*  790 */         bb.putFloat(x);
/*  791 */         bb.put((byte)rot);
/*      */         
/*  793 */         addMessageToQueue(bb);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  813 */       catch (NullPointerException np) {
/*      */         
/*  815 */         logger.log(Level.WARNING, np.getMessage(), np);
/*  816 */         this.player.setLink(false);
/*      */       }
/*  818 */       catch (Exception ex) {
/*      */         
/*  820 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  821 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMoveCreatureAndSetZ(long id, float x, float y, float z, int rot) {
/*  829 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  833 */         ByteBuffer bb = getBuffer(14);
/*  834 */         bb.put((byte)72);
/*  835 */         bb.putLong(id);
/*  836 */         bb.putFloat(z);
/*  837 */         bb.putFloat(x);
/*  838 */         bb.put((byte)rot);
/*  839 */         bb.putFloat(y);
/*  840 */         addMessageToQueue(bb);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  859 */       catch (NullPointerException np) {
/*      */         
/*  861 */         logger.log(Level.WARNING, np.getMessage(), np);
/*  862 */         this.player.setLink(false);
/*      */       }
/*  864 */       catch (Exception ex) {
/*      */         
/*  866 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  867 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCreatureChangedLayer(long wurmid, byte newlayer) {
/*  875 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  879 */         ByteBuffer bb = getBuffer(10);
/*  880 */         bb.put((byte)30);
/*  881 */         bb.putLong(wurmid);
/*  882 */         bb.put(newlayer);
/*  883 */         addMessageToQueue(bb);
/*      */       }
/*  885 */       catch (Exception ex) {
/*      */         
/*  887 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  888 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendDeleteCreature(long id) {
/*  896 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  900 */         ByteBuffer bb = getBuffer(9);
/*  901 */         bb.put((byte)14);
/*  902 */         bb.putLong(id);
/*  903 */         addMessageToQueue(bb);
/*      */       }
/*  905 */       catch (NullPointerException np) {
/*      */         
/*  907 */         logger.log(Level.WARNING, this.player.getName() + ":" + np.getMessage(), np);
/*  908 */         this.player.setLink(false);
/*      */       }
/*  910 */       catch (Exception ex) {
/*      */         
/*  912 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  913 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendTileStripFar(short xStart, short yStart, int width, int height) {
/*  921 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  925 */         ByteBuffer bb = getBuffer(32767);
/*  926 */         bb.put((byte)103);
/*  927 */         bb.putShort(xStart);
/*  928 */         bb.putShort(yStart);
/*  929 */         bb.putShort((short)width);
/*  930 */         bb.putShort((short)height); int x;
/*  931 */         for (x = 0; x < width; x++) {
/*      */           
/*  933 */           for (int y = 0; y < height; y++) {
/*      */             
/*  935 */             int xx = (xStart + x) * 16;
/*  936 */             int yy = (yStart + y) * 16;
/*  937 */             if (xx < 0 || xx >= 1 << Constants.meshSize || yy < 0 || yy >= 1 << Constants.meshSize) {
/*      */               
/*  939 */               xx = 0;
/*  940 */               yy = 0;
/*      */             } 
/*  942 */             bb.putShort(Tiles.decodeHeight(Server.surfaceMesh.data[xx | yy << Constants.meshSize]));
/*      */           } 
/*      */         } 
/*  945 */         for (x = 0; x < width; x++) {
/*      */           
/*  947 */           int ms = Constants.meshSize - 4;
/*  948 */           for (int y = 0; y < height; y++) {
/*      */             
/*  950 */             int xx = xStart + x;
/*  951 */             int yy = yStart + y;
/*  952 */             if (xx < 0 || xx >= 1 << ms || yy < 0 || yy >= 1 << ms) {
/*      */               
/*  954 */               xx = 0;
/*  955 */               yy = 0;
/*      */             } 
/*  957 */             bb.put(Server.surfaceMesh.getDistantTerrainTypes()[xx | yy << ms]);
/*      */           } 
/*      */         } 
/*      */         
/*  961 */         addMessageToQueue(bb);
/*      */       }
/*  963 */       catch (Exception ex) {
/*      */         
/*  965 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/*  966 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendTileStrip(short xStart, short yStart, int width, int height) throws IOException {
/*  974 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/*  978 */         ByteBuffer bb = getBuffer(32767);
/*  979 */         bb.put((byte)73);
/*  980 */         bb.put(Features.Feature.SURFACEWATER.isEnabled() ? 1 : 0);
/*  981 */         bb.put(this.player.isSendExtraBytes() ? 1 : 0);
/*  982 */         bb.putShort(yStart);
/*  983 */         bb.putShort((short)width);
/*  984 */         bb.putShort((short)height);
/*  985 */         bb.putShort(xStart);
/*      */ 
/*      */ 
/*      */         
/*  989 */         for (int x = 0; x < width; x++) {
/*      */           
/*  991 */           for (int y = 0; y < height; y++) {
/*      */             
/*  993 */             int tempTileX = xStart + x;
/*  994 */             int tempTileY = yStart + y;
/*  995 */             if (tempTileX < 0 || tempTileX >= 1 << Constants.meshSize || tempTileY < 0 || tempTileY >= 1 << Constants.meshSize) {
/*      */ 
/*      */               
/*  998 */               tempTileX = 0;
/*  999 */               tempTileY = 0;
/*      */             } 
/* 1001 */             bb.putInt(Server.surfaceMesh.data[tempTileX | tempTileY << Constants.meshSize]);
/* 1002 */             if (Features.Feature.SURFACEWATER.isEnabled())
/* 1003 */               bb.putShort((short)Water.getSurfaceWater(tempTileX, tempTileY)); 
/* 1004 */             if (this.player.isSendExtraBytes())
/* 1005 */               bb.put(Server.getClientSurfaceFlags(tempTileX, tempTileY)); 
/*      */           } 
/*      */         } 
/* 1008 */         addMessageToQueue(bb);
/*      */       }
/* 1010 */       catch (Exception ex) {
/*      */         
/* 1012 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1013 */         this.player.setLink(false);
/* 1014 */         throw new IOException(this.player.getName() + ":" + ex.getMessage());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCaveStrip(short xStart, short yStart, int width, int height) {
/* 1022 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1026 */         ByteBuffer bb = getBuffer(32767);
/* 1027 */         bb.put((byte)102);
/* 1028 */         bb.put(Features.Feature.CAVEWATER.isEnabled() ? 1 : 0);
/* 1029 */         bb.put(this.player.isSendExtraBytes() ? 1 : 0);
/* 1030 */         bb.putShort(xStart);
/* 1031 */         bb.putShort(yStart);
/* 1032 */         bb.putShort((short)width);
/* 1033 */         bb.putShort((short)height);
/* 1034 */         boolean onSurface = this.player.isOnSurface();
/*      */ 
/*      */ 
/*      */         
/* 1038 */         for (int x = 0; x < width; x++) {
/*      */           
/* 1040 */           for (int y = 0; y < height; y++) {
/*      */             
/* 1042 */             int xx = xStart + x;
/* 1043 */             int yy = yStart + y;
/*      */             
/* 1045 */             if (xx < 0 || xx >= Zones.worldTileSizeX || yy < 0 || yy >= Zones.worldTileSizeY) {
/*      */               
/* 1047 */               bb.putInt(emptyRock);
/* 1048 */               xx = 0;
/* 1049 */               yy = 0;
/*      */             }
/* 1051 */             else if (!onSurface) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1057 */               bb.putInt(Server.caveMesh.data[xx | yy << Constants.meshSize]);
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 1062 */             else if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.data[xx | yy << Constants.meshSize]))) {
/*      */               
/* 1064 */               bb.putInt(getDummyWall(xx, yy));
/*      */             }
/*      */             else {
/*      */               
/* 1068 */               bb.putInt(Server.caveMesh.data[xx | yy << Constants.meshSize]);
/*      */             } 
/* 1070 */             if (Features.Feature.CAVEWATER.isEnabled())
/* 1071 */               bb.putShort((short)Water.getCaveWater(xx, yy)); 
/* 1072 */             if (this.player.isSendExtraBytes())
/* 1073 */               bb.put(Server.getClientCaveFlags(xx, yy)); 
/*      */           } 
/*      */         } 
/* 1076 */         addMessageToQueue(bb);
/*      */       }
/* 1078 */       catch (Exception ex) {
/*      */         
/* 1080 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1081 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private int getDummyWall(int tilex, int tiley) {
/* 1088 */     return Tiles.encode(
/* 1089 */         Tiles.decodeHeight(Server.caveMesh.data[tilex | tiley << Constants.meshSize]), Tiles.Tile.TILE_CAVE_WALL.id, 
/*      */         
/* 1091 */         Tiles.decodeData(Server.caveMesh.data[tilex | tiley << Constants.meshSize]));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isCaveWallHidden(int tilex, int tiley) {
/* 1097 */     if (!isCaveWallSolid(tilex, tiley))
/* 1098 */       return false; 
/* 1099 */     if (!isCaveWallSolid(tilex, tiley - 1))
/* 1100 */       return false; 
/* 1101 */     if (!isCaveWallSolid(tilex + 1, tiley))
/* 1102 */       return false; 
/* 1103 */     if (!isCaveWallSolid(tilex, tiley + 1))
/* 1104 */       return false; 
/* 1105 */     if (!isCaveWallSolid(tilex - 1, tiley))
/* 1106 */       return false; 
/* 1107 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isCaveWallSolid(int tilex, int tiley) {
/* 1113 */     if (tilex < 0 || tilex >= Zones.worldTileSizeX || tiley < 0 || tiley >= Zones.worldTileSizeY)
/* 1114 */       return true; 
/* 1115 */     if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.data[tilex | tiley << Constants.meshSize])))
/* 1116 */       return true; 
/* 1117 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAvailableActions(byte requestId, List<ActionEntry> availableActions, String helpstring) {
/* 1123 */     if (this.player.hasLink()) {
/*      */       
/*      */       try
/*      */       {
/*      */ 
/*      */         
/* 1129 */         if (logger.isLoggable(Level.FINEST)) {
/*      */           
/* 1131 */           logger.finest(this.player.getName() + ", sending # of available Actions: " + availableActions.size() + ", requestId: " + 
/* 1132 */               String.valueOf(requestId) + ", availableActions: " + availableActions + ", helpstring: " + helpstring);
/*      */         
/*      */         }
/* 1135 */         else if (logger.isLoggable(Level.FINER)) {
/*      */           
/* 1137 */           logger.finer(this.player.getName() + ", sending # of available Actions: " + availableActions.size() + " , requestId: " + 
/* 1138 */               String.valueOf(requestId) + ", helpstring: " + helpstring);
/*      */         } 
/* 1140 */         ByteBuffer bb = getBuffer(32767);
/* 1141 */         bb.put((byte)20);
/* 1142 */         bb.put(requestId);
/* 1143 */         bb.put((byte)availableActions.size());
/* 1144 */         for (Iterator<ActionEntry> it = availableActions.iterator(); it.hasNext(); ) {
/*      */           
/* 1146 */           ActionEntry entry = it.next();
/* 1147 */           bb.putShort(entry.getNumber());
/* 1148 */           String actionString = entry.getActionString();
/*      */           
/* 1150 */           byte[] arrayOfByte = actionString.getBytes("UTF-8");
/* 1151 */           bb.put((byte)arrayOfByte.length);
/* 1152 */           bb.put(arrayOfByte);
/* 1153 */           if (entry.isQuickSkillLess()) {
/* 1154 */             bb.put((byte)1); continue;
/*      */           } 
/* 1156 */           bb.put((byte)0);
/*      */         } 
/*      */         
/* 1159 */         byte[] byteArray = helpstring.getBytes("UTF-8");
/* 1160 */         bb.put((byte)byteArray.length);
/* 1161 */         bb.put(byteArray);
/* 1162 */         addMessageToQueue(bb);
/*      */       }
/* 1164 */       catch (Exception ex)
/*      */       {
/* 1166 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1167 */         this.player.setLink(false);
/*      */       }
/*      */     
/* 1170 */     } else if (logger.isLoggable(Level.FINER)) {
/*      */       
/* 1172 */       logger.finer("Not sending Available Actions as Player has lost link, requestId: " + String.valueOf(requestId) + ", availableActions: " + availableActions + ", helpstring: " + helpstring);
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
/*      */   public void sendItem(Item item, long creatureId, boolean onGroundLevel) {
/* 1186 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1190 */         long id = item.getWurmId();
/* 1191 */         byte[] byteArray = item.getModelName().getBytes("UTF-8");
/*      */         
/* 1193 */         ByteBuffer bb = getBuffer(32767);
/* 1194 */         bb.put((byte)-9);
/* 1195 */         bb.putLong(id);
/* 1196 */         bb.put((byte)byteArray.length);
/* 1197 */         bb.put(byteArray);
/* 1198 */         byteArray = item.getName().getBytes("UTF-8");
/* 1199 */         bb.put((byte)byteArray.length);
/* 1200 */         bb.put(byteArray);
/* 1201 */         bb.putFloat(item.getPosX());
/* 1202 */         bb.putFloat(item.getPosY());
/* 1203 */         bb.putFloat(item.getRotation());
/* 1204 */         if (item.isFloating() && item.getPosZ() <= 0.0F) {
/*      */           
/* 1206 */           if (item.getCurrentQualityLevel() < 10.0F) {
/* 1207 */             bb.putFloat(-3000.0F);
/*      */           } else {
/* 1209 */             bb.putFloat(0.0F);
/*      */           } 
/* 1211 */         } else if (item.getFloorLevel() > 0 || !onGroundLevel) {
/*      */           
/* 1213 */           bb.putFloat(item.getPosZ());
/*      */         } else {
/*      */           
/* 1216 */           bb.putFloat(-3000.0F);
/*      */         } 
/* 1218 */         bb.put((byte)(item.isOnSurface() ? 0 : -1));
/*      */ 
/*      */ 
/*      */         
/* 1222 */         byteArray = item.getDescription().getBytes("UTF-8");
/* 1223 */         bb.put((byte)byteArray.length);
/* 1224 */         bb.put(byteArray);
/* 1225 */         bb.putShort(item.getImageNumber());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1233 */         addMessageToQueue(bb);
/* 1234 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1236 */           logger.finer(this.player.getName() + " sent item " + item.getName() + " - " + item.getWurmId());
/*      */         }
/*      */       }
/* 1239 */       catch (Exception ex) {
/*      */         
/* 1241 */         logger.log(Level.WARNING, "Failed to send item: " + this.player
/* 1242 */             .getName() + ":" + item.getWurmId() + ", " + ex.getMessage(), ex);
/* 1243 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRename(Item item, String newName, String newModelName) {
/* 1251 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1255 */         long id = item.getWurmId();
/* 1256 */         byte[] byteArray = newName.getBytes("UTF-8");
/* 1257 */         ByteBuffer bb = getBuffer(32767);
/* 1258 */         bb.put((byte)44);
/* 1259 */         bb.putLong(id);
/* 1260 */         bb.put((byte)byteArray.length);
/* 1261 */         bb.put(byteArray);
/*      */         
/* 1263 */         bb.put(item.getMaterial());
/* 1264 */         byteArray = item.getDescription().getBytes("UTF-8");
/* 1265 */         bb.put((byte)byteArray.length);
/* 1266 */         bb.put(byteArray);
/* 1267 */         bb.putShort(item.getImageNumber());
/* 1268 */         bb.put(item.getRarity());
/* 1269 */         addMessageToQueue(bb);
/*      */       }
/* 1271 */       catch (Exception ex) {
/*      */         
/* 1273 */         logger.log(Level.WARNING, "Failed to rename item: " + this.player
/* 1274 */             .getName() + ":" + item.getWurmId() + ", " + ex.getMessage(), ex);
/* 1275 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveItem(Item item) {
/* 1283 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1287 */         ByteBuffer bb = getBuffer(9);
/* 1288 */         bb.put((byte)10);
/* 1289 */         bb.putLong(item.getWurmId());
/* 1290 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1292 */           logger.finer(this.player.getName() + " Sending remove " + item.getWurmId());
/*      */         }
/* 1294 */         addMessageToQueue(bb);
/*      */       }
/* 1296 */       catch (Exception ex) {
/*      */         
/* 1298 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1299 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddSkill(int id, int parentSkillId, String name, float value, float maxValue, int affinities) {
/* 1308 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1312 */         byte[] byteArray = name.getBytes("UTF-8");
/* 1313 */         ByteBuffer bb = getBuffer(byteArray.length + 22);
/* 1314 */         bb.put((byte)124);
/* 1315 */         bb.putLong(BigInteger.valueOf(parentSkillId).shiftLeft(32).longValue() + 18L);
/* 1316 */         bb.putLong(BigInteger.valueOf(id).shiftLeft(32).longValue() + 18L);
/*      */         
/* 1318 */         bb.put((byte)byteArray.length);
/* 1319 */         bb.put(byteArray);
/* 1320 */         bb.putFloat(value);
/* 1321 */         bb.putFloat(maxValue);
/* 1322 */         bb.put((byte)affinities);
/* 1323 */         addMessageToQueue(bb);
/*      */       }
/* 1325 */       catch (Exception ex) {
/*      */         
/* 1327 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1328 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUpdateSkill(int id, float value, int affinities) {
/* 1336 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1340 */         ByteBuffer bb = getBuffer(13);
/* 1341 */         bb.put((byte)66);
/* 1342 */         bb.putLong((id << 32L) + 18L);
/* 1343 */         bb.putFloat(value);
/* 1344 */         bb.put((byte)affinities);
/* 1345 */         addMessageToQueue(bb);
/*      */       }
/* 1347 */       catch (Exception ex) {
/*      */         
/* 1349 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1350 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendActionControl(long creatureId, String actionString, boolean start, int timeLeft) {
/* 1358 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1362 */         byte[] byteArray = "".getBytes("UTF-8");
/* 1363 */         if (start)
/* 1364 */           byteArray = actionString.getBytes("UTF-8"); 
/* 1365 */         ByteBuffer bb = getBuffer(32767);
/* 1366 */         bb.put((byte)-12);
/*      */         
/* 1368 */         bb.put((byte)byteArray.length);
/* 1369 */         bb.put(byteArray);
/*      */         
/* 1371 */         int lTimeLeft = Math.min(timeLeft, 65535);
/* 1372 */         bb.putShort((short)lTimeLeft);
/* 1373 */         bb.putLong(creatureId);
/* 1374 */         addMessageToQueue(bb);
/*      */       }
/* 1376 */       catch (Exception ex) {
/*      */         
/* 1378 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1379 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddEffect(long id, short type, float x, float y, float z, byte layer) {
/* 1387 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1391 */         ByteBuffer bb = getBuffer(24);
/* 1392 */         bb.put((byte)64);
/* 1393 */         bb.putLong(id);
/* 1394 */         bb.putShort(type);
/* 1395 */         bb.putFloat(x);
/* 1396 */         bb.putFloat(y);
/* 1397 */         bb.putFloat(z);
/* 1398 */         bb.put(layer);
/* 1399 */         addMessageToQueue(bb);
/*      */       }
/* 1401 */       catch (Exception ex) {
/*      */         
/* 1403 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1404 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveEffect(long id) {
/* 1412 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1416 */         ByteBuffer bb = getBuffer(9);
/* 1417 */         bb.put((byte)37);
/* 1418 */         bb.putLong(id);
/* 1419 */         addMessageToQueue(bb);
/*      */       }
/* 1421 */       catch (Exception ex) {
/*      */         
/* 1423 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1424 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendStamina(int stamina, int damage) {
/* 1432 */     if (this.player.hasLink() && !this.player.isTransferring()) {
/*      */       
/*      */       try {
/*      */         
/* 1436 */         ByteBuffer bb = getBuffer(5);
/* 1437 */         bb.put((byte)90);
/* 1438 */         short lStamina = (short)(int)((stamina & 0xFFFE) | this.newSeed >> this.newSeedPointer++ & 0x1L);
/*      */         
/* 1440 */         bb.putShort(lStamina);
/* 1441 */         bb.putShort((short)damage);
/* 1442 */         addMessageToQueue(bb);
/* 1443 */         if (this.newSeedPointer == 32)
/*      */         {
/* 1445 */           (getConnection()).encryptRandom.setSeed(this.newSeed & 0xFFFFFFFFFFFFFFFFL);
/* 1446 */           getConnection().changeProtocol(this.newSeed);
/* 1447 */           this.newSeedPointer = 0;
/* 1448 */           this.newSeed = (Server.rand.nextInt() & Integer.MAX_VALUE);
/*      */         }
/*      */       
/* 1451 */       } catch (Exception ex) {
/*      */         
/* 1453 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1454 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendThirst(int thirst) {
/* 1462 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1466 */         ByteBuffer bb = getBuffer(3);
/* 1467 */         bb.put((byte)105);
/* 1468 */         bb.putShort((short)thirst);
/* 1469 */         addMessageToQueue(bb);
/*      */       }
/* 1471 */       catch (Exception ex) {
/*      */         
/* 1473 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1474 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendHunger(int hunger, float nutrition, float calories, float carbs, float fats, float proteins) {
/* 1482 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1486 */         ByteBuffer bb = getBuffer(3);
/* 1487 */         bb.put((byte)61);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1494 */         bb.putShort((short)hunger);
/* 1495 */         bb.put((byte)(int)(nutrition * 100.0F));
/* 1496 */         bb.put((byte)(int)(calories * 100.0F));
/* 1497 */         bb.put((byte)(int)(carbs * 100.0F));
/* 1498 */         bb.put((byte)(int)(fats * 100.0F));
/* 1499 */         bb.put((byte)(int)(proteins * 100.0F));
/* 1500 */         addMessageToQueue(bb);
/*      */       }
/* 1502 */       catch (Exception ex) {
/*      */         
/* 1504 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1505 */         this.player.setLink(false);
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
/*      */   protected void sendWeight(byte weight) {
/* 1518 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1522 */         ByteBuffer bb = getBuffer(32767);
/* 1523 */         bb.put((byte)5);
/* 1524 */         bb.put(weight);
/* 1525 */         addMessageToQueue(bb);
/*      */       }
/* 1527 */       catch (Exception ex) {
/*      */         
/* 1529 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1530 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendSpeedModifier(float speedModifier) {
/* 1538 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1542 */         ByteBuffer bb = getBuffer(5);
/* 1543 */         bb.put((byte)32);
/* 1544 */         bb.putFloat(speedModifier);
/* 1545 */         addMessageToQueue(bb);
/*      */       }
/* 1547 */       catch (Exception ex) {
/*      */         
/* 1549 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1550 */         this.player.setLink(false);
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
/*      */   protected void sendTimeLeft(short tenthOfSeconds) {
/* 1563 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1567 */         ByteBuffer bb = getBuffer(32767);
/* 1568 */         bb.put((byte)87);
/* 1569 */         bb.putShort(tenthOfSeconds);
/* 1570 */         addMessageToQueue(bb);
/*      */       }
/* 1572 */       catch (Exception ex) {
/*      */         
/* 1574 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1575 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendSingleBuildMarker(long structureId, int tilex, int tiley, byte layer) {
/* 1583 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1587 */         ByteBuffer bb = getBuffer(14);
/* 1588 */         bb.put((byte)96);
/* 1589 */         bb.putLong(structureId);
/* 1590 */         bb.put(layer);
/* 1591 */         bb.put((byte)1);
/* 1592 */         bb.putShort((short)tilex);
/* 1593 */         bb.putShort((short)tiley);
/* 1594 */         addMessageToQueue(bb);
/* 1595 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1597 */           logger.finer("adding or removing single marker");
/*      */         }
/*      */       }
/* 1600 */       catch (Exception ex) {
/*      */         
/* 1602 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1603 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMultipleBuildMarkers(long structureId, VolaTile[] tiles, byte layer) {
/* 1611 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1615 */         ByteBuffer bb = getBuffer(32767);
/* 1616 */         bb.put((byte)96);
/* 1617 */         bb.putLong(structureId);
/* 1618 */         bb.put(layer);
/* 1619 */         bb.put((byte)tiles.length);
/* 1620 */         for (int x = 0; x < tiles.length; x++) {
/*      */           
/* 1622 */           bb.putShort((short)tiles[x].getTileX());
/* 1623 */           bb.putShort((short)tiles[x].getTileY());
/*      */         } 
/* 1625 */         addMessageToQueue(bb);
/*      */       }
/* 1627 */       catch (Exception ex) {
/*      */         
/* 1629 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1630 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddStructure(String name, short centerTilex, short centerTiley, long structureId, byte structureType, byte layer) {
/* 1639 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1643 */         ByteBuffer bb = getBuffer(32767);
/* 1644 */         bb.put((byte)112);
/* 1645 */         bb.putLong(structureId);
/* 1646 */         bb.put(structureType);
/* 1647 */         byte[] byteArray = name.getBytes("UTF-8");
/* 1648 */         bb.put((byte)byteArray.length);
/* 1649 */         bb.put(byteArray);
/* 1650 */         bb.putShort(centerTilex);
/* 1651 */         bb.putShort(centerTiley);
/* 1652 */         bb.put(layer);
/* 1653 */         addMessageToQueue(bb);
/*      */       }
/* 1655 */       catch (Exception ex) {
/*      */         
/* 1657 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1658 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveStructure(long structureId) {
/* 1666 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1670 */         ByteBuffer bb = getBuffer(9);
/* 1671 */         bb.put((byte)48);
/* 1672 */         bb.putLong(structureId);
/* 1673 */         addMessageToQueue(bb);
/*      */       }
/* 1675 */       catch (Exception ex) {
/*      */         
/* 1677 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1678 */         this.player.setLink(false);
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
/*      */   protected void sendUpdateFence(Fence fence) {
/* 1691 */     sendRemoveFence(fence);
/* 1692 */     sendAddFence(fence);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddWall(long structureId, Wall wall) {
/* 1698 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1702 */         ByteBuffer bb = getBuffer(32767);
/* 1703 */         bb.put((byte)49);
/* 1704 */         bb.putLong(structureId);
/* 1705 */         bb.putShort((short)Math.min(wall.getStartY(), wall.getEndY()));
/* 1706 */         bb.putShort((short)Math.min(wall.getStartX(), wall.getEndX()));
/* 1707 */         if (wall.isHorizontal()) {
/* 1708 */           bb.put((byte)0);
/*      */         } else {
/* 1710 */           bb.put((byte)1);
/* 1711 */         }  if (wall.isFinished()) {
/* 1712 */           bb.put((byte)wall.getType().ordinal());
/*      */         } else {
/* 1714 */           bb.put((byte)StructureTypeEnum.PLAN.ordinal());
/* 1715 */         }  byte[] byteArray = wall.getMaterialString().getBytes();
/* 1716 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1718 */           logger.finer(structureId + " Updating " + wall.getMaterialString());
/*      */         }
/* 1720 */         bb.put((byte)byteArray.length);
/* 1721 */         bb.put(byteArray);
/* 1722 */         bb.put((byte)((wall.getColor() == -1) ? 0 : 1));
/* 1723 */         if (wall.getColor() != -1) {
/*      */           
/* 1725 */           bb.put((byte)WurmColor.getColorRed(wall.getColor()));
/* 1726 */           bb.put((byte)WurmColor.getColorGreen(wall.getColor()));
/* 1727 */           bb.put((byte)WurmColor.getColorBlue(wall.getColor()));
/*      */         } 
/* 1729 */         addMessageToQueue(bb);
/*      */       }
/* 1731 */       catch (Exception ex) {
/*      */         
/* 1733 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1734 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendPassable(boolean passable, Door door) {
/* 1742 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 1747 */         Wall wall = door.getWall();
/* 1748 */         ByteBuffer bb = getBuffer(32767);
/* 1749 */         bb.put((byte)125);
/* 1750 */         bb.putLong(door.getStructureId());
/* 1751 */         bb.putShort((short)Math.min(wall.getStartX(), wall.getEndX()));
/* 1752 */         bb.putShort((short)Math.min(wall.getStartY(), wall.getEndY()));
/* 1753 */         if (wall.isHorizontal()) {
/* 1754 */           bb.put((byte)0);
/*      */         } else {
/* 1756 */           bb.put((byte)1);
/* 1757 */         }  if (passable) {
/* 1758 */           bb.put((byte)1);
/*      */         } else {
/* 1760 */           bb.put((byte)0);
/* 1761 */         }  addMessageToQueue(bb);
/*      */       }
/* 1763 */       catch (NoSuchWallException nsw) {
/*      */         
/* 1765 */         logger.log(Level.WARNING, this.player.getName() + ": Trying to make door passable for wall with no id! Structure=" + door
/* 1766 */             .getStructureId(), (Throwable)nsw);
/*      */         
/*      */         return;
/* 1769 */       } catch (Exception ex) {
/*      */         
/* 1771 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1772 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendOpenDoor(Door door) {
/* 1780 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1784 */         Wall wall = door.getWall();
/* 1785 */         ByteBuffer bb = getBuffer(32767);
/* 1786 */         bb.put((byte)122);
/* 1787 */         bb.putLong(door.getStructureId());
/* 1788 */         bb.putShort((short)Math.min(wall.getStartX(), wall.getEndX()));
/* 1789 */         bb.putShort((short)Math.min(wall.getStartY(), wall.getEndY()));
/* 1790 */         if (wall.isHorizontal()) {
/* 1791 */           bb.put((byte)0);
/*      */         } else {
/* 1793 */           bb.put((byte)1);
/* 1794 */         }  addMessageToQueue(bb);
/*      */       }
/* 1796 */       catch (NoSuchWallException nsw) {
/*      */         
/* 1798 */         logger.log(Level.WARNING, this.player.getName() + ": trying to open door for wall with no id! Structure=" + door
/* 1799 */             .getStructureId(), (Throwable)nsw);
/*      */         
/*      */         return;
/* 1802 */       } catch (Exception ex) {
/*      */         
/* 1804 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1805 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCloseDoor(Door door) {
/* 1813 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1817 */         Wall wall = door.getWall();
/* 1818 */         ByteBuffer bb = getBuffer(32767);
/* 1819 */         bb.put(127);
/* 1820 */         bb.putLong(door.getStructureId());
/* 1821 */         bb.putShort((short)Math.min(wall.getStartX(), wall.getEndX()));
/* 1822 */         bb.putShort((short)Math.min(wall.getStartY(), wall.getEndY()));
/* 1823 */         if (wall.isHorizontal()) {
/* 1824 */           bb.put((byte)0);
/*      */         } else {
/* 1826 */           bb.put((byte)1);
/* 1827 */         }  addMessageToQueue(bb);
/*      */       }
/* 1829 */       catch (NoSuchWallException nsw) {
/*      */         
/* 1831 */         logger.log(Level.WARNING, this.player.getName() + ": trying to close door for wall with no id! Structure=" + door
/* 1832 */             .getStructureId(), (Throwable)nsw);
/*      */         
/*      */         return;
/* 1835 */       } catch (Exception ex) {
/*      */         
/* 1837 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1838 */         this.player.setLink(false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendBml(int width, int height, boolean resizeable, boolean closeable, String content, int r, int g, int b, String title) {
/* 1871 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1875 */         byte[] byteArray = title.getBytes("UTF-8");
/* 1876 */         ByteBuffer bb = getBuffer(32767);
/* 1877 */         bb.put((byte)106);
/* 1878 */         bb.put((byte)byteArray.length);
/* 1879 */         bb.put(byteArray);
/* 1880 */         bb.putShort((short)width);
/* 1881 */         bb.putShort((short)height);
/* 1882 */         bb.put(closeable ? 1 : 0);
/* 1883 */         bb.put(resizeable ? 1 : 0);
/* 1884 */         bb.put((byte)r);
/* 1885 */         bb.put((byte)g);
/* 1886 */         bb.put((byte)b);
/* 1887 */         byteArray = content.getBytes("UTF-8");
/* 1888 */         bb.putShort((short)byteArray.length);
/* 1889 */         bb.put(byteArray);
/* 1890 */         addMessageToQueue(bb);
/*      */       }
/* 1892 */       catch (Exception ex) {
/*      */         
/* 1894 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1895 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendChangeStructureName(long structureId, String newName) {
/* 1903 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1907 */         byte[] byteArray = newName.getBytes("UTF-8");
/* 1908 */         ByteBuffer bb = getBuffer(32767);
/* 1909 */         bb.put((byte)47);
/* 1910 */         bb.put((byte)0);
/* 1911 */         bb.putLong(structureId);
/* 1912 */         bb.put((byte)byteArray.length);
/* 1913 */         bb.put(byteArray);
/* 1914 */         addMessageToQueue(bb);
/*      */       }
/* 1916 */       catch (Exception ex) {
/*      */         
/* 1918 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 1919 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUseBinoculars() {
/* 1927 */     sendClientFeature((byte)1, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendStopUseBinoculars() {
/* 1933 */     sendClientFeature((byte)1, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendToggle(int toggle, boolean set) {
/* 1939 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1943 */         ByteBuffer bb = getBuffer(3);
/* 1944 */         bb.put((byte)62);
/* 1945 */         bb.put((byte)toggle);
/* 1946 */         bb.put((byte)(set ? 1 : 0));
/* 1947 */         addMessageToQueue(bb);
/*      */       }
/* 1949 */       catch (Exception ex) {
/*      */         
/* 1951 */         logger.log(Level.INFO, "Problem sending toggle (" + toggle + ',' + set + ") to " + this.player.getName() + " due to :" + ex
/* 1952 */             .getMessage(), ex);
/* 1953 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendTeleport(boolean aLocal) {
/* 1962 */     sendTeleport(aLocal, true, (byte)0);
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
/*      */   public void sendTeleport(boolean aLocal, boolean disembark, byte commandType) {
/* 1975 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 1979 */         ByteBuffer bb = getBuffer(21);
/* 1980 */         bb.put((byte)51);
/* 1981 */         bb.putFloat(this.player.getStatus().getPositionX());
/* 1982 */         bb.putFloat(this.player.getStatus().getPositionY());
/* 1983 */         bb.putFloat(this.player.getStatus().getPositionZ());
/* 1984 */         bb.putFloat(this.player.getStatus().getRotation());
/* 1985 */         bb.put((byte)(aLocal ? 1 : 0));
/*      */         
/* 1987 */         bb.put((byte)(this.player.isOnSurface() ? 0 : -1));
/* 1988 */         bb.put((byte)(disembark ? 1 : 0));
/* 1989 */         bb.put(commandType);
/* 1990 */         addMessageToQueue(bb);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2003 */         this.currentmove = null;
/* 2004 */         setMoves(0);
/* 2005 */         this.receivedTicks = false;
/*      */       }
/* 2007 */       catch (Exception ex) {
/*      */         
/* 2009 */         logger.log(Level.INFO, "Problem sending teleport (local: " + aLocal + ") to " + this.player.getName() + " due to :" + ex
/* 2010 */             .getMessage(), ex);
/* 2011 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendStartTrading(Creature opponent) {
/* 2019 */     Trade trade = this.player.getTrade();
/* 2020 */     if (trade != null)
/*      */     {
/* 2022 */       if (this.player.hasLink()) {
/*      */         
/*      */         try {
/*      */           
/* 2026 */           String name = opponent.getName();
/* 2027 */           byte[] byteArray = name.getBytes("UTF-8");
/* 2028 */           ByteBuffer bb = getBuffer(32767);
/* 2029 */           bb.put((byte)119);
/* 2030 */           bb.put((byte)byteArray.length);
/* 2031 */           bb.put(byteArray);
/* 2032 */           if (trade.creatureOne == this.player) {
/*      */             
/* 2034 */             bb.putLong(1L);
/* 2035 */             bb.putLong(2L);
/* 2036 */             bb.putLong(3L);
/* 2037 */             bb.putLong(4L);
/*      */           }
/*      */           else {
/*      */             
/* 2041 */             bb.putLong(2L);
/* 2042 */             bb.putLong(1L);
/* 2043 */             bb.putLong(4L);
/* 2044 */             bb.putLong(3L);
/*      */           } 
/* 2046 */           addMessageToQueue(bb);
/*      */         }
/* 2048 */         catch (Exception ex) {
/*      */           
/* 2050 */           logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2051 */           this.player.setLink(false);
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCloseTradeWindow() {
/* 2060 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2064 */         ByteBuffer bb = getBuffer(1);
/* 2065 */         bb.put((byte)121);
/* 2066 */         addMessageToQueue(bb);
/*      */       }
/* 2068 */       catch (Exception ex) {
/*      */         
/* 2070 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2071 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendTradeAgree(Creature agreer, boolean agree) {
/* 2079 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2083 */         boolean me = false;
/* 2084 */         if (agreer == this.player)
/* 2085 */           me = true; 
/* 2086 */         if (me && agree)
/*      */           return; 
/* 2088 */         ByteBuffer bb = getBuffer(2);
/* 2089 */         bb.put((byte)42);
/* 2090 */         bb.put((byte)(agree ? 1 : 0));
/* 2091 */         addMessageToQueue(bb);
/*      */       }
/* 2093 */       catch (Exception ex) {
/*      */         
/* 2095 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2096 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendTradeChanged(int id) {
/* 2104 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2108 */         ByteBuffer bb = getBuffer(5);
/* 2109 */         bb.put((byte)91);
/* 2110 */         bb.putInt(id);
/* 2111 */         addMessageToQueue(bb);
/*      */       }
/* 2113 */       catch (Exception ex) {
/*      */         
/* 2115 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2116 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddFence(Fence fence) {
/* 2124 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2128 */         ByteBuffer bb = getBuffer(32767);
/* 2129 */         bb.put((byte)12);
/* 2130 */         bb.putShort((short)fence.getTileX());
/* 2131 */         bb.putShort((short)fence.getTileY());
/* 2132 */         bb.put(fence.getDir().getCode());
/* 2133 */         bb.putShort((short)fence.getType().ordinal());
/* 2134 */         bb.put((byte)(fence.isFinished() ? 1 : 0));
/* 2135 */         bb.put((byte)((fence.getColor() == -1) ? 0 : 1));
/* 2136 */         if (fence.getColor() != -1) {
/*      */           
/* 2138 */           bb.put((byte)WurmColor.getColorRed(fence.getColor()));
/* 2139 */           bb.put((byte)WurmColor.getColorGreen(fence.getColor()));
/* 2140 */           bb.put((byte)WurmColor.getColorBlue(fence.getColor()));
/*      */         } 
/* 2142 */         addMessageToQueue(bb);
/*      */       }
/* 2144 */       catch (Exception ex) {
/*      */         
/* 2146 */         logger.log(Level.INFO, this.player.getName() + " adding fence: " + fence + " :" + ex.getMessage(), ex);
/* 2147 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveFence(Fence fence) {
/* 2155 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2159 */         ByteBuffer bb = getBuffer(32767);
/* 2160 */         bb.put((byte)13);
/* 2161 */         bb.putShort((short)fence.getTileX());
/* 2162 */         bb.putShort((short)fence.getTileY());
/* 2163 */         bb.put(fence.getDir().getCode());
/* 2164 */         addMessageToQueue(bb);
/*      */       }
/* 2166 */       catch (Exception ex) {
/*      */         
/* 2168 */         logger.log(Level.INFO, this.player.getName() + " problem removing fence: " + fence + " due to :" + ex.getMessage(), ex);
/*      */         
/* 2170 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendOpenFence(Fence fence, boolean passable, boolean changePassable) {
/* 2178 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2182 */         ByteBuffer bb = getBuffer(32767);
/* 2183 */         bb.put((byte)83);
/* 2184 */         bb.putShort((short)fence.getTileX());
/* 2185 */         bb.putShort((short)fence.getTileY());
/* 2186 */         bb.put(fence.getDir().getCode());
/* 2187 */         bb.put((byte)1);
/* 2188 */         if (changePassable) {
/* 2189 */           bb.put((byte)(passable ? 1 : 0));
/*      */         } else {
/* 2191 */           bb.put((byte)2);
/* 2192 */         }  addMessageToQueue(bb);
/*      */       }
/* 2194 */       catch (Exception ex) {
/*      */         
/* 2196 */         logger.log(Level.INFO, this.player.getName() + " problem opening fence: " + fence + " due to :" + ex.getMessage(), ex);
/*      */         
/* 2198 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCloseFence(Fence fence, boolean passable, boolean changePassable) {
/* 2206 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2210 */         ByteBuffer bb = getBuffer(32767);
/* 2211 */         bb.put((byte)83);
/* 2212 */         bb.putShort((short)fence.getTileX());
/* 2213 */         bb.putShort((short)fence.getTileY());
/* 2214 */         bb.put(fence.getDir().getCode());
/* 2215 */         bb.put((byte)0);
/* 2216 */         if (changePassable) {
/* 2217 */           bb.put((byte)(passable ? 1 : 0));
/*      */         } else {
/* 2219 */           bb.put((byte)2);
/* 2220 */         }  addMessageToQueue(bb);
/*      */       }
/* 2222 */       catch (Exception ex) {
/*      */         
/* 2224 */         logger.log(Level.INFO, this.player.getName() + " problem closing fence: " + fence + " due to :" + ex.getMessage(), ex);
/*      */         
/* 2226 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendSound(Sound sound) {
/* 2234 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2238 */         String name = sound.getName();
/* 2239 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2240 */         ByteBuffer bb = getBuffer(32767);
/* 2241 */         bb.put((byte)86);
/* 2242 */         bb.put((byte)byteArray.length);
/* 2243 */         bb.put(byteArray);
/* 2244 */         bb.putFloat(sound.getPosX());
/* 2245 */         bb.putFloat(sound.getPosY());
/* 2246 */         bb.putFloat(sound.getPosZ());
/* 2247 */         bb.putFloat(sound.getPitch());
/* 2248 */         bb.putFloat(sound.getVolume());
/* 2249 */         bb.putFloat(sound.getPriority());
/* 2250 */         addMessageToQueue(bb);
/*      */       }
/* 2252 */       catch (Exception ex) {
/*      */         
/* 2254 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2255 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMusic(Sound sound) {
/* 2263 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2267 */         String name = sound.getName();
/* 2268 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2269 */         ByteBuffer bb = getBuffer(32767);
/* 2270 */         bb.put((byte)115);
/* 2271 */         bb.put((byte)byteArray.length);
/* 2272 */         bb.put(byteArray);
/* 2273 */         bb.putFloat(sound.getPosX());
/* 2274 */         bb.putFloat(sound.getPosY());
/* 2275 */         bb.putFloat(sound.getPosZ());
/* 2276 */         bb.putFloat(sound.getPitch());
/* 2277 */         bb.putFloat(sound.getVolume());
/* 2278 */         bb.putFloat(sound.getPriority());
/* 2279 */         addMessageToQueue(bb);
/*      */       }
/* 2281 */       catch (Exception ex) {
/*      */         
/* 2283 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2284 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendStatus(String status) {
/* 2292 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2296 */         byte[] byteArray = status.getBytes("UTF-8");
/* 2297 */         ByteBuffer bb = getBuffer(32767);
/* 2298 */         bb.put((byte)-18);
/* 2299 */         bb.put((byte)byteArray.length);
/* 2300 */         bb.put(byteArray);
/* 2301 */         addMessageToQueue(bb);
/*      */       }
/* 2303 */       catch (Exception ex) {
/*      */         
/* 2305 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2306 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddWound(Wound wound, Item bodyPart) {
/* 2314 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2318 */         byte[] byteArray = wound.getName().getBytes("UTF-8");
/*      */         
/* 2320 */         ByteBuffer bb = getBuffer(32767);
/*      */ 
/*      */         
/* 2323 */         bb.put((byte)76);
/* 2324 */         if (this.player == wound.getCreature()) {
/* 2325 */           bb.putLong(-1L);
/*      */         } else {
/*      */           
/* 2328 */           Item body = wound.getCreature().getBody().getBodyItem();
/* 2329 */           bb.putLong(body.getWurmId());
/*      */         } 
/* 2331 */         long parentId = bodyPart.getWurmId();
/* 2332 */         bb.putLong(parentId);
/* 2333 */         bb.putLong(wound.getWurmId());
/* 2334 */         bb.putShort((short)wound.getWoundIconId());
/*      */         
/* 2336 */         bb.put((byte)byteArray.length);
/* 2337 */         bb.put(byteArray);
/* 2338 */         byteArray = wound.getDescription().getBytes("UTF-8");
/* 2339 */         bb.put((byte)byteArray.length);
/* 2340 */         bb.put(byteArray);
/* 2341 */         bb.putFloat(100.0F);
/* 2342 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 2344 */           logger.finer("Sending wound ID: " + wound.getWurmId() + ", severity: " + wound.getSeverity() + "*" + 0.0015259022F + "=" + (wound
/* 2345 */               .getSeverity() * 0.0015259022F));
/*      */         }
/* 2347 */         bb.putFloat(wound.getSeverity() * 0.0015259022F);
/* 2348 */         bb.putInt(0);
/* 2349 */         bb.put((byte)0);
/* 2350 */         bb.put((byte)0);
/* 2351 */         addMessageToQueue(bb);
/*      */       }
/* 2353 */       catch (Exception ex) {
/*      */         
/* 2355 */         logger.log(Level.INFO, this.player.getName() + ":" + wound.getWoundString(), ex);
/*      */         
/* 2357 */         this.player.setLink(false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveWound(Wound wound) {
/* 2403 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2407 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 2409 */           logger.finer("Removing wound ID " + wound.getWurmId() + " from player inventory.");
/*      */         }
/* 2411 */         ByteBuffer bb = getBuffer(17);
/* 2412 */         bb.put((byte)-10);
/* 2413 */         if (this.player == wound.getCreature()) {
/* 2414 */           bb.putLong(-1L);
/*      */         } else {
/*      */           
/* 2417 */           Item body = wound.getCreature().getBody().getBodyItem();
/* 2418 */           bb.putLong(body.getWurmId());
/*      */         } 
/* 2420 */         bb.putLong(wound.getWurmId());
/* 2421 */         addMessageToQueue(bb);
/*      */       }
/* 2423 */       catch (Exception ex) {
/*      */         
/* 2425 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2426 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUpdateWound(Wound wound, Item bodyPart) {
/* 2434 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2438 */         byte[] byteArray = wound.getName().getBytes("UTF-8");
/*      */         
/* 2440 */         ByteBuffer bb = getBuffer(32767);
/* 2441 */         bb.put((byte)68);
/*      */         
/* 2443 */         if (this.player == wound.getCreature()) {
/* 2444 */           bb.putLong(-1L);
/*      */         } else {
/*      */           
/* 2447 */           Item body = wound.getCreature().getBody().getBodyItem();
/* 2448 */           bb.putLong(body.getWurmId());
/*      */         } 
/* 2450 */         bb.putLong(wound.getWurmId());
/* 2451 */         long parentId = bodyPart.getWurmId();
/* 2452 */         bb.putLong(parentId);
/* 2453 */         bb.put((byte)byteArray.length);
/* 2454 */         bb.put(byteArray);
/* 2455 */         byteArray = wound.getDescription().getBytes("UTF-8");
/* 2456 */         bb.put((byte)byteArray.length);
/* 2457 */         bb.put(byteArray);
/* 2458 */         bb.putFloat(100.0F);
/* 2459 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 2461 */           logger.finer("Sending wound ID: " + wound.getWurmId() + ", severity: " + wound.getSeverity() + "*" + 0.0015259022F + "=" + (wound
/* 2462 */               .getSeverity() * 0.0015259022F));
/*      */         }
/* 2464 */         bb.putFloat(wound.getSeverity() * 0.0015259022F);
/* 2465 */         bb.putInt(0);
/* 2466 */         bb.put((byte)0);
/* 2467 */         bb.put((byte)0);
/* 2468 */         addMessageToQueue(bb);
/*      */       }
/* 2470 */       catch (Exception ex) {
/*      */         
/* 2472 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2473 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendSelfToLocal() {
/* 2481 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2485 */         byte[] byteArray = this.player.getName().getBytes("UTF-8");
/* 2486 */         ByteBuffer bb = getBuffer(32767);
/* 2487 */         bb.put((byte)-13);
/* 2488 */         bb.put((byte)local.length);
/* 2489 */         bb.put(local);
/* 2490 */         bb.put((byte)byteArray.length);
/* 2491 */         bb.put(byteArray);
/* 2492 */         bb.putLong(this.player.getWurmId());
/* 2493 */         addMessageToQueue(bb);
/*      */       }
/* 2495 */       catch (Exception ex) {
/*      */         
/* 2497 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2498 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddVillager(String name, long wurmid) {
/* 2506 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2510 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2511 */         ByteBuffer bb = getBuffer(32767);
/* 2512 */         bb.put((byte)-13);
/* 2513 */         bb.put((byte)village.length);
/* 2514 */         bb.put(village);
/* 2515 */         bb.put((byte)byteArray.length);
/* 2516 */         bb.put(byteArray);
/* 2517 */         bb.putLong(wurmid);
/* 2518 */         addMessageToQueue(bb);
/*      */       }
/* 2520 */       catch (Exception ex) {
/*      */         
/* 2522 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2523 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveVillager(String name) {
/* 2531 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2535 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2536 */         ByteBuffer bb = getBuffer(32767);
/* 2537 */         bb.put((byte)114);
/* 2538 */         bb.put((byte)village.length);
/* 2539 */         bb.put(village);
/* 2540 */         bb.put((byte)byteArray.length);
/* 2541 */         bb.put(byteArray);
/* 2542 */         addMessageToQueue(bb);
/*      */       }
/* 2544 */       catch (Exception ex) {
/*      */         
/* 2546 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2547 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddAlly(String name, long wurmid) {
/* 2555 */     if (this.player != null && this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2559 */         byte[] tempStringArr = name.getBytes("UTF-8");
/* 2560 */         ByteBuffer bb = getBuffer(32767);
/* 2561 */         bb.put((byte)-13);
/* 2562 */         bb.put((byte)alliance.length);
/* 2563 */         bb.put(alliance);
/* 2564 */         bb.put((byte)tempStringArr.length);
/* 2565 */         bb.put(tempStringArr);
/* 2566 */         bb.putLong(wurmid);
/* 2567 */         addMessageToQueue(bb);
/*      */       }
/* 2569 */       catch (Exception ex) {
/*      */         
/* 2571 */         logger.log(Level.WARNING, this.player.getName() + ':' + ex.getMessage(), ex);
/* 2572 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveAlly(String name) {
/* 2580 */     if (this.player != null && this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2584 */         byte[] tempStringArr = name.getBytes("UTF-8");
/* 2585 */         ByteBuffer bb = getBuffer(32767);
/* 2586 */         bb.put((byte)114);
/* 2587 */         bb.put((byte)alliance.length);
/* 2588 */         bb.put(alliance);
/* 2589 */         bb.put((byte)tempStringArr.length);
/* 2590 */         bb.put(tempStringArr);
/* 2591 */         addMessageToQueue(bb);
/*      */       }
/* 2593 */       catch (Exception ex) {
/*      */         
/* 2595 */         logger.log(Level.WARNING, this.player.getName() + ':' + ex.getMessage(), ex);
/* 2596 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddLocal(String name, long wurmid) {
/* 2604 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/* 2610 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2611 */         ByteBuffer bb = getBuffer(32767);
/* 2612 */         bb.put((byte)-13);
/* 2613 */         bb.put((byte)local.length);
/* 2614 */         bb.put(local);
/* 2615 */         bb.put((byte)byteArray.length);
/* 2616 */         bb.put(byteArray);
/* 2617 */         bb.putLong(wurmid);
/* 2618 */         addMessageToQueue(bb);
/*      */       }
/* 2620 */       catch (Exception ex) {
/*      */         
/* 2622 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2623 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveLocal(String name) {
/* 2631 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/* 2637 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2638 */         ByteBuffer bb = getBuffer(32767);
/* 2639 */         bb.put((byte)114);
/* 2640 */         bb.put((byte)local.length);
/* 2641 */         bb.put(local);
/* 2642 */         bb.put((byte)byteArray.length);
/* 2643 */         bb.put(byteArray);
/* 2644 */         addMessageToQueue(bb);
/*      */       }
/* 2646 */       catch (Exception ex) {
/*      */         
/* 2648 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2649 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAddGm(String name, long wurmid) {
/* 2657 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2661 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2662 */         ByteBuffer bb = getBuffer(32767);
/* 2663 */         bb.put((byte)-13);
/* 2664 */         bb.put((byte)gms.length);
/* 2665 */         bb.put(gms);
/* 2666 */         bb.put((byte)byteArray.length);
/* 2667 */         bb.put(byteArray);
/* 2668 */         bb.putLong(wurmid);
/* 2669 */         addMessageToQueue(bb);
/*      */       }
/* 2671 */       catch (Exception ex) {
/*      */         
/* 2673 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2674 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveGm(String name) {
/* 2682 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2686 */         byte[] byteArray = name.getBytes("UTF-8");
/* 2687 */         ByteBuffer bb = getBuffer(32767);
/* 2688 */         bb.put((byte)114);
/* 2689 */         bb.put((byte)gms.length);
/* 2690 */         bb.put(gms);
/* 2691 */         bb.put((byte)byteArray.length);
/* 2692 */         bb.put(byteArray);
/* 2693 */         addMessageToQueue(bb);
/*      */       }
/* 2695 */       catch (Exception ex) {
/*      */         
/* 2697 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2698 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void changeAttitude(long creatureId, byte status) {
/* 2706 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2710 */         ByteBuffer bb = getBuffer(10);
/* 2711 */         bb.put((byte)6);
/* 2712 */         bb.putLong(creatureId);
/* 2713 */         bb.put(status);
/* 2714 */         addMessageToQueue(bb);
/*      */       }
/* 2716 */       catch (Exception ex) {
/*      */         
/* 2718 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2719 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendWeather() {
/* 2727 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2731 */         ByteBuffer bb = getBuffer(29);
/* 2732 */         bb.put((byte)46);
/* 2733 */         bb.putFloat(Server.getWeather().getCloudiness());
/* 2734 */         bb.putFloat(Server.getWeather().getFog());
/* 2735 */         bb.putFloat(Server.getWeather().getRain());
/* 2736 */         bb.putFloat(Server.getWeather().getXWind());
/* 2737 */         bb.putFloat(Server.getWeather().getYWind());
/* 2738 */         bb.putFloat(Server.getWeather().getWindRotation());
/* 2739 */         bb.putFloat(Server.getWeather().getWindPower());
/* 2740 */         addMessageToQueue(bb);
/*      */         
/* 2742 */         sendNormalServerMessage("The wind is now coming from " + Server.getWeather().getWindRotation() + "- strength x=" + 
/* 2743 */             Server.getWeather().getXWind() + ", y=" + Server.getWeather().getYWind());
/*      */       }
/* 2745 */       catch (Exception ex) {
/*      */         
/* 2747 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2748 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendDead() {
/* 2756 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2760 */         ByteBuffer bb = getBuffer(3);
/* 2761 */         bb.put((byte)65);
/* 2762 */         addMessageToQueue(bb);
/*      */       }
/* 2764 */       catch (Exception ex) {
/*      */         
/* 2766 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2767 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendClimb(boolean climbing) {
/* 2775 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2779 */         ByteBuffer bb = getBuffer(2);
/* 2780 */         bb.put((byte)79);
/* 2781 */         bb.put((byte)(climbing ? 1 : 0));
/* 2782 */         addMessageToQueue(bb);
/*      */       }
/* 2784 */       catch (Exception ex) {
/*      */         
/* 2786 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2787 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendReconnect(String ip, int port, String session) {
/* 2795 */     if (logger.isLoggable(Level.FINE))
/*      */     {
/* 2797 */       logger.fine("Sending reconnect to server: " + ip + ':' + port + " to " + this.player);
/*      */     }
/* 2799 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2803 */         ByteBuffer bb = getBuffer(32767);
/* 2804 */         bb.put((byte)23);
/* 2805 */         byte[] byteArray = ip.getBytes("UTF-8");
/* 2806 */         bb.put((byte)byteArray.length);
/* 2807 */         bb.put(byteArray);
/* 2808 */         bb.putInt(port);
/* 2809 */         byteArray = session.getBytes("UTF-8");
/* 2810 */         bb.put((byte)byteArray.length);
/* 2811 */         bb.put(byteArray);
/* 2812 */         addMessageToQueue(bb);
/*      */       }
/* 2814 */       catch (Exception ex) {
/*      */         
/* 2816 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2817 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendHasMoreItems(long inventoryId, long wurmid) {
/* 2825 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2829 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 2831 */           logger.finer("Inventory " + inventoryId + " containing Wurmid " + wurmid + " has MORE.");
/*      */         }
/* 2833 */         ByteBuffer bb = getBuffer(17);
/* 2834 */         bb.put((byte)29);
/* 2835 */         bb.putLong(inventoryId);
/* 2836 */         bb.putLong(wurmid);
/* 2837 */         addMessageToQueue(bb);
/*      */       }
/* 2839 */       catch (Exception ex) {
/*      */         
/* 2841 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2842 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendIsEmpty(long inventoryId, long wurmid) {
/* 2850 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2854 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 2856 */           logger.finer("Inventory " + inventoryId + " containing Wurmid " + wurmid + " has no more items.");
/*      */         }
/* 2858 */         ByteBuffer bb = getBuffer(17);
/* 2859 */         bb.put((byte)-16);
/* 2860 */         bb.putLong(inventoryId);
/* 2861 */         bb.putLong(wurmid);
/* 2862 */         addMessageToQueue(bb);
/*      */       }
/* 2864 */       catch (Exception ex) {
/*      */         
/* 2866 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2867 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendCompass(Item item) {
/* 2875 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2879 */         ByteBuffer bb = getBuffer(3);
/* 2880 */         bb.put((byte)-30);
/* 2881 */         bb.put((byte)0);
/* 2882 */         if (item == null) {
/* 2883 */           bb.put((byte)0);
/*      */         } else {
/* 2885 */           bb.put((byte)(int)Math.max(1.0F, item.getCurrentQualityLevel()));
/* 2886 */         }  addMessageToQueue(bb);
/*      */       }
/* 2888 */       catch (Exception ex) {
/*      */         
/* 2890 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2891 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendToolbelt(Item item) {
/* 2899 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2903 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 2905 */           if (item != null) {
/* 2906 */             logger.finer(this.player.getName() + " sending toolbelt with wurmid " + item.getWurmId() + ".");
/*      */           } else {
/* 2908 */             logger.finer(this.player.getName() + " sending toolbelt null.");
/*      */           } 
/*      */         }
/* 2911 */         ByteBuffer bb = getBuffer(3);
/* 2912 */         bb.put((byte)-30);
/* 2913 */         bb.put((byte)2);
/* 2914 */         if (item == null) {
/* 2915 */           bb.put((byte)0);
/*      */         } else {
/* 2917 */           bb.put((byte)(int)Math.max(1.0F, item.getCurrentQualityLevel()));
/* 2918 */         }  addMessageToQueue(bb);
/*      */       }
/* 2920 */       catch (Exception ex) {
/*      */         
/* 2922 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2923 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendClientFeature(byte feature, boolean on) {
/* 2930 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2934 */         ByteBuffer bb = getBuffer(3);
/* 2935 */         bb.put((byte)-30);
/* 2936 */         bb.put(feature);
/* 2937 */         if (on) {
/* 2938 */           bb.put((byte)1);
/*      */         } else {
/* 2940 */           bb.put((byte)0);
/* 2941 */         }  addMessageToQueue(bb);
/*      */       }
/* 2943 */       catch (Exception ex) {
/*      */         
/* 2945 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2946 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendServerTime() {
/* 2954 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2958 */         ByteBuffer bb = getBuffer(17);
/* 2959 */         bb.put((byte)107);
/* 2960 */         bb.putLong(System.currentTimeMillis());
/* 2961 */         bb.putLong(WurmCalendar.currentTime + this.timeMod);
/* 2962 */         addMessageToQueue(bb);
/*      */       }
/* 2964 */       catch (Exception ex) {
/*      */         
/* 2966 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 2967 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAttachEffect(long targetId, byte effectType, byte data0, byte data1, byte data2, byte dimension) {
/* 2976 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 2980 */         ByteBuffer bb = getBuffer(13);
/* 2981 */         bb.put((byte)109);
/*      */ 
/*      */         
/* 2984 */         bb.putLong(targetId);
/* 2985 */         bb.put(effectType);
/*      */ 
/*      */         
/* 2988 */         bb.put(data0);
/*      */ 
/*      */ 
/*      */         
/* 2992 */         bb.put(data1);
/*      */         
/* 2994 */         bb.put(data2);
/*      */         
/* 2996 */         bb.put(dimension);
/* 2997 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/* 2999 */           logger.finest(this.player.getName() + ": " + targetId + ", light colour: " + data0 + ", " + data1 + ", " + data2);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3004 */         addMessageToQueue(bb);
/*      */       }
/* 3006 */       catch (Exception ex) {
/*      */         
/* 3008 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3009 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRemoveEffect(long targetId, byte effectType) {
/* 3017 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3021 */         ByteBuffer bb = getBuffer(10);
/* 3022 */         bb.put((byte)18);
/* 3023 */         bb.putLong(targetId);
/* 3024 */         bb.put(effectType);
/* 3025 */         addMessageToQueue(bb);
/* 3026 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 3028 */           logger.finer(this.player.getName() + " removing :" + targetId + ", light " + effectType);
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 3033 */       catch (Exception ex) {
/*      */         
/* 3035 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3036 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendWieldItem(long creatureId, byte slot, String modelname, byte rarity, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue) {
/* 3046 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3050 */         if (creatureId == -1L || WurmId.getType(creatureId) == 0)
/*      */         {
/* 3052 */           ByteBuffer bb = getBuffer(32767);
/* 3053 */           bb.put((byte)101);
/* 3054 */           bb.putLong(creatureId);
/* 3055 */           bb.put(slot);
/* 3056 */           byte[] byteArray = modelname.getBytes("UTF-8");
/* 3057 */           bb.putShort((short)byteArray.length);
/* 3058 */           bb.put(byteArray);
/* 3059 */           bb.put(rarity);
/* 3060 */           bb.putInt(colorRed);
/* 3061 */           bb.putInt(colorGreen);
/* 3062 */           bb.putInt(colorBlue);
/* 3063 */           bb.putInt(secondaryColorRed);
/* 3064 */           bb.putInt(secondaryColorGreen);
/* 3065 */           bb.putInt(secondaryColorBlue);
/* 3066 */           addMessageToQueue(bb);
/*      */         }
/*      */       
/* 3069 */       } catch (Exception ex) {
/*      */         
/* 3071 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3072 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUseItem(long creatureId, String modelname, byte rarity, int colorRed, int colorGreen, int colorBlue, int secondaryColorRed, int secondaryColorGreen, int secondaryColorBlue) {
/* 3081 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3085 */         if (creatureId == -1L || WurmId.getType(creatureId) == 0)
/*      */         {
/* 3087 */           ByteBuffer bb = getBuffer(32767);
/* 3088 */           bb.put((byte)110);
/* 3089 */           bb.putLong(creatureId);
/* 3090 */           byte[] byteArray = modelname.getBytes("UTF-8");
/* 3091 */           bb.putShort((short)byteArray.length);
/* 3092 */           bb.put(byteArray);
/* 3093 */           bb.put(rarity);
/* 3094 */           bb.putFloat(colorRed);
/* 3095 */           bb.putFloat(colorGreen);
/* 3096 */           bb.putFloat(colorBlue);
/* 3097 */           bb.putInt(secondaryColorRed);
/* 3098 */           bb.putInt(secondaryColorGreen);
/* 3099 */           bb.putInt(secondaryColorBlue);
/* 3100 */           addMessageToQueue(bb);
/*      */         }
/*      */       
/* 3103 */       } catch (Exception ex) {
/*      */         
/* 3105 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3106 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendStopUseItem(long creatureId) {
/* 3114 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3118 */         if (creatureId == -1L || WurmId.getType(creatureId) == 0)
/*      */         {
/* 3120 */           ByteBuffer bb = getBuffer(9);
/* 3121 */           bb.put((byte)71);
/* 3122 */           bb.putLong(creatureId);
/* 3123 */           addMessageToQueue(bb);
/*      */         }
/*      */       
/* 3126 */       } catch (Exception ex) {
/*      */         
/* 3128 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3129 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRepaint(long wurmid, byte r, byte g, byte b, byte alpha, byte paintType) {
/* 3137 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3141 */         ByteBuffer bb = getBuffer(14);
/* 3142 */         bb.put((byte)92);
/* 3143 */         bb.putLong(wurmid);
/* 3144 */         bb.put(r);
/* 3145 */         bb.put(g);
/* 3146 */         bb.put(b);
/* 3147 */         bb.put(alpha);
/* 3148 */         bb.put(paintType);
/* 3149 */         addMessageToQueue(bb);
/*      */       }
/* 3151 */       catch (Exception ex) {
/*      */         
/* 3153 */         logger.log(Level.INFO, this.player.getName() + ": " + ex.getMessage(), ex);
/* 3154 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendResize(long wurmid, byte xscaleMod, byte yscaleMod, byte zscaleMod) {
/* 3162 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3166 */         ByteBuffer bb = getBuffer(12);
/* 3167 */         bb.put((byte)74);
/* 3168 */         bb.putLong(wurmid);
/* 3169 */         bb.put(xscaleMod);
/* 3170 */         bb.put(yscaleMod);
/* 3171 */         bb.put(zscaleMod);
/* 3172 */         addMessageToQueue(bb);
/*      */       }
/* 3174 */       catch (Exception ex) {
/*      */         
/* 3176 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3177 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendNewMovingItem(long id, String name, String model, float x, float y, float z, long onBridge, float rot, byte layer, boolean onGround, boolean floating, boolean isSolid, byte material, byte rarity) {
/* 3187 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3191 */         byte[] byteArray = model.getBytes("UTF-8");
/*      */ 
/*      */ 
/*      */         
/* 3195 */         ByteBuffer bb = getBuffer(32767);
/*      */         
/* 3197 */         bb.put((byte)108);
/* 3198 */         bb.putLong(id);
/*      */         
/* 3200 */         bb.put((byte)byteArray.length);
/* 3201 */         bb.put(byteArray);
/* 3202 */         bb.put((byte)(isSolid ? 1 : 0));
/* 3203 */         bb.putFloat(y);
/* 3204 */         bb.putFloat(x);
/* 3205 */         bb.putLong(onBridge);
/* 3206 */         bb.putFloat(rot);
/* 3207 */         if (onGround) {
/*      */           
/* 3209 */           if (Structure.isGroundFloorAtPosition(x, y, (layer == 0)))
/*      */           {
/* 3211 */             bb.putFloat(z + 0.1F);
/*      */           }
/*      */           else
/*      */           {
/* 3215 */             bb.putFloat(-3000.0F);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 3220 */           bb.putFloat(z);
/*      */         } 
/*      */         
/* 3223 */         byteArray = name.getBytes("UTF-8");
/* 3224 */         bb.put((byte)byteArray.length);
/* 3225 */         bb.put(byteArray);
/*      */         
/* 3227 */         if (floating) {
/* 3228 */           bb.put((byte)1);
/*      */         } else {
/* 3230 */           bb.put((byte)0);
/* 3231 */         }  bb.put((byte)2);
/* 3232 */         bb.put(layer);
/* 3233 */         bb.put(material);
/* 3234 */         bb.put((byte)0);
/* 3235 */         bb.put(rarity);
/* 3236 */         addMessageToQueue(bb);
/* 3237 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 3239 */           logger.finer(this.player.getName() + " sent creature " + name + " model= " + model + " x " + x + " y " + y + " z " + z);
/*      */         
/*      */         }
/*      */       }
/* 3243 */       catch (NullPointerException np) {
/*      */         
/* 3245 */         logger.log(Level.WARNING, this.player.getName() + ":" + np.getMessage(), np);
/* 3246 */         this.player.setLink(false);
/*      */       }
/* 3248 */       catch (Exception ex) {
/*      */         
/* 3250 */         logger.log(Level.WARNING, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3251 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMoveMovingItem(long id, float x, float y, int rot) {
/* 3259 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 3264 */         ByteBuffer bb = getBuffer(12);
/*      */         
/* 3266 */         bb.put((byte)36);
/* 3267 */         bb.putLong(id);
/* 3268 */         bb.putFloat(x);
/* 3269 */         bb.putFloat(y);
/* 3270 */         bb.put((byte)rot);
/*      */         
/* 3272 */         addMessageToQueue(bb);
/*      */       }
/* 3274 */       catch (NullPointerException np) {
/*      */         
/* 3276 */         logger.log(Level.WARNING, np.getMessage(), np);
/* 3277 */         this.player.setLink(false);
/*      */       }
/* 3279 */       catch (Exception ex) {
/*      */         
/* 3281 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3282 */         this.player.setLink(false);
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
/*      */   
/*      */   public void sendMoveMovingItemAndSetZ(long id, float x, float y, float z, int rot) {
/* 3300 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 3305 */         ByteBuffer bb = getBuffer(14);
/*      */         
/* 3307 */         bb.put((byte)72);
/* 3308 */         bb.putLong(id);
/* 3309 */         bb.putFloat(x);
/* 3310 */         bb.putFloat(y);
/* 3311 */         bb.putFloat(z);
/* 3312 */         bb.put((byte)rot);
/* 3313 */         addMessageToQueue(bb);
/*      */       }
/* 3315 */       catch (NullPointerException np) {
/*      */         
/* 3317 */         logger.log(Level.WARNING, np.getMessage(), np);
/* 3318 */         this.player.setLink(false);
/*      */       }
/* 3320 */       catch (Exception ex) {
/*      */         
/* 3322 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3323 */         this.player.setLink(false);
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
/*      */   public void sendMovingItemChangedLayer(long wurmid, byte newlayer) {
/* 3337 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3341 */         ByteBuffer bb = getBuffer(10);
/* 3342 */         bb.put((byte)30);
/* 3343 */         bb.putLong(wurmid);
/* 3344 */         bb.put(newlayer);
/* 3345 */         addMessageToQueue(bb);
/*      */       
/*      */       }
/* 3348 */       catch (Exception ex) {
/*      */         
/* 3350 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3351 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendDeleteMovingItem(long id) {
/* 3359 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3363 */         ByteBuffer bb = getBuffer(9);
/* 3364 */         bb.put((byte)14);
/* 3365 */         bb.putLong(id);
/* 3366 */         addMessageToQueue(bb);
/*      */       }
/* 3368 */       catch (NullPointerException np) {
/*      */         
/* 3370 */         logger.log(Level.WARNING, this.player.getName() + ":" + np.getMessage(), np);
/* 3371 */         this.player.setLink(false);
/*      */       }
/* 3373 */       catch (Exception ex) {
/*      */         
/* 3375 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3376 */         this.player.setLink(false);
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
/*      */   public void sendShutDown(String reason, boolean requested) {
/* 3389 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3393 */         ByteBuffer bb = getBuffer(3);
/* 3394 */         bb.put((byte)4);
/* 3395 */         byte[] tempStringArr = reason.getBytes("UTF-8");
/* 3396 */         bb.putShort((short)tempStringArr.length);
/* 3397 */         bb.put(tempStringArr);
/* 3398 */         bb.put(requested ? 1 : 0);
/* 3399 */         addMessageToQueue(bb);
/*      */       }
/* 3401 */       catch (Exception ex) {
/*      */         
/* 3403 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3404 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void attachCreature(long source, long target, float offx, float offy, float offz, int seatId) {
/* 3413 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3417 */         ByteBuffer bb = getBuffer(29);
/* 3418 */         bb.put((byte)111);
/* 3419 */         bb.putLong(source);
/* 3420 */         bb.putLong(target);
/* 3421 */         bb.putFloat(offx);
/* 3422 */         bb.putFloat(offy);
/* 3423 */         bb.putFloat(offz);
/* 3424 */         bb.put((byte)seatId);
/* 3425 */         addMessageToQueue(bb);
/*      */       }
/* 3427 */       catch (Exception ex) {
/*      */         
/* 3429 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3430 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVehicleController(long playerId, long targetId, float offx, float offy, float offz, float maxDepth, float maxHeight, float maxHeightDiff, float vehicleRotation, int seatId) {
/* 3440 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/* 3446 */         ByteBuffer bb = getBuffer(45);
/* 3447 */         bb.put((byte)63);
/* 3448 */         bb.putLong(playerId);
/* 3449 */         bb.putLong(targetId);
/* 3450 */         bb.putFloat(offx);
/* 3451 */         bb.putFloat(offy);
/* 3452 */         bb.putFloat(offz);
/* 3453 */         bb.putFloat(maxDepth);
/* 3454 */         bb.putFloat(maxHeight);
/* 3455 */         bb.putFloat(maxHeightDiff);
/* 3456 */         bb.putFloat(vehicleRotation);
/* 3457 */         bb.put((byte)seatId);
/* 3458 */         addMessageToQueue(bb);
/*      */       }
/* 3460 */       catch (Exception ex) {
/*      */         
/* 3462 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3463 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendAnimation(long creatureId, String animationName, boolean looping, boolean freezeAtFinish) {
/* 3472 */     if (this.player.hasLink())
/*      */     {
/*      */       
/* 3475 */       if (creatureId > 0L) {
/*      */         
/*      */         try {
/*      */           
/* 3479 */           ByteBuffer bb = getBuffer(32767);
/* 3480 */           bb.put((byte)24);
/* 3481 */           bb.putLong(creatureId);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3486 */           byte[] byteArray = animationName.getBytes("UTF-8");
/* 3487 */           bb.put((byte)byteArray.length);
/* 3488 */           bb.put(byteArray);
/* 3489 */           if (looping) {
/* 3490 */             bb.put((byte)1);
/*      */           } else {
/* 3492 */             bb.put((byte)0);
/* 3493 */           }  if (freezeAtFinish) {
/* 3494 */             bb.put((byte)1);
/*      */           } else {
/* 3496 */             bb.put((byte)0);
/*      */           } 
/* 3498 */           addMessageToQueue(bb);
/*      */         }
/* 3500 */         catch (Exception ex) {
/*      */           
/* 3502 */           logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3503 */           this.player.setLink(false);
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendCombatOptions(byte[] options, short tenthsOfSeconds) {
/* 3512 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3516 */         ByteBuffer bb = getBuffer(32767);
/* 3517 */         bb.put((byte)98);
/* 3518 */         bb.put((byte)options.length);
/* 3519 */         bb.put(options);
/* 3520 */         bb.putShort(tenthsOfSeconds);
/* 3521 */         addMessageToQueue(bb);
/*      */       }
/* 3523 */       catch (NullPointerException np) {
/*      */         
/* 3525 */         logger.log(Level.WARNING, this.player.getName() + ":" + np.getMessage(), np);
/* 3526 */         this.player.setLink(false);
/*      */       }
/* 3528 */       catch (Exception ex) {
/*      */         
/* 3530 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3531 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendCombatStatus(float distanceToTarget, float footing, byte stance) {
/* 3539 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3543 */         ByteBuffer bb = getBuffer(10);
/* 3544 */         bb.put((byte)-14);
/* 3545 */         bb.putFloat(distanceToTarget);
/* 3546 */         bb.putFloat(footing);
/* 3547 */         bb.put(stance);
/* 3548 */         addMessageToQueue(bb);
/*      */       }
/* 3550 */       catch (NullPointerException np) {
/*      */         
/* 3552 */         logger.log(Level.WARNING, this.player.getName() + ":" + np.getMessage(), np);
/* 3553 */         this.player.setLink(false);
/*      */       }
/* 3555 */       catch (Exception ex) {
/*      */         
/* 3557 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3558 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendStunned(boolean stunned) {
/* 3566 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3570 */         ByteBuffer bb = getBuffer(32767);
/* 3571 */         bb.put((byte)28);
/* 3572 */         if (stunned) {
/* 3573 */           bb.put((byte)1);
/*      */         } else {
/* 3575 */           bb.put((byte)0);
/* 3576 */         }  addMessageToQueue(bb);
/*      */       }
/* 3578 */       catch (Exception ex) {
/*      */         
/* 3580 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3581 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendSpecialMove(short move, String movename) {
/* 3589 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3593 */         byte[] byteArray = movename.getBytes("UTF-8");
/* 3594 */         ByteBuffer bb = getBuffer(32767);
/* 3595 */         bb.put((byte)-17);
/* 3596 */         bb.putShort(move);
/* 3597 */         bb.put((byte)byteArray.length);
/* 3598 */         bb.put(byteArray);
/* 3599 */         addMessageToQueue(bb);
/*      */       }
/* 3601 */       catch (Exception ex) {
/*      */         
/* 3603 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3604 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendToggleShield(boolean on) {
/* 3612 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3616 */         ByteBuffer bb = getBuffer(32767);
/* 3617 */         bb.put((byte)-17);
/* 3618 */         bb.putShort((short)105);
/* 3619 */         if (on) {
/* 3620 */           bb.put((byte)1);
/*      */         } else {
/* 3622 */           bb.put((byte)0);
/* 3623 */         }  addMessageToQueue(bb);
/*      */       }
/* 3625 */       catch (Exception ex) {
/*      */         
/* 3627 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3628 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendTarget(long id) {
/* 3636 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3640 */         ByteBuffer bb = getBuffer(9);
/* 3641 */         bb.put((byte)25);
/* 3642 */         bb.putLong(id);
/* 3643 */         addMessageToQueue(bb);
/*      */       }
/* 3645 */       catch (Exception ex) {
/*      */         
/* 3647 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3648 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendFightStyle(byte style) {
/* 3656 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3660 */         ByteBuffer bb = getBuffer(2);
/* 3661 */         bb.put((byte)26);
/* 3662 */         bb.put(style);
/* 3663 */         addMessageToQueue(bb);
/*      */       }
/* 3665 */       catch (Exception ex) {
/*      */         
/* 3667 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3668 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreatureDamage(long wurmid, float damagePercent) {
/* 3676 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3680 */         ByteBuffer bb = getBuffer(13);
/* 3681 */         bb.put((byte)11);
/* 3682 */         bb.putLong(wurmid);
/* 3683 */         bb.putFloat(damagePercent);
/* 3684 */         addMessageToQueue(bb);
/*      */       }
/* 3686 */       catch (Exception ex) {
/*      */         
/* 3688 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3689 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendWindImpact(byte windimpact) {
/* 3697 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3701 */         ByteBuffer bb = getBuffer(2);
/* 3702 */         bb.put((byte)117);
/* 3703 */         bb.put(windimpact);
/* 3704 */         addMessageToQueue(bb);
/* 3705 */         this.player.sentWind = System.currentTimeMillis();
/*      */       }
/* 3707 */       catch (Exception ex) {
/*      */         
/* 3709 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3710 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMountSpeed(short mountSpeed) {
/* 3718 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3722 */         ByteBuffer bb = getBuffer(2);
/* 3723 */         bb.put((byte)60);
/* 3724 */         bb.putShort(mountSpeed);
/* 3725 */         addMessageToQueue(bb);
/* 3726 */         this.player.sentMountSpeed = System.currentTimeMillis();
/*      */       }
/* 3728 */       catch (Exception ex) {
/*      */         
/* 3730 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3731 */         this.player.setLink(false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendRotate(long itemId, float rotation) {
/* 3739 */     if (this.player.hasLink()) {
/*      */       
/*      */       try {
/*      */         
/* 3743 */         ByteBuffer bb = getBuffer(13);
/* 3744 */         bb.put((byte)67);
/* 3745 */         bb.putLong(itemId);
/* 3746 */         bb.putFloat(rotation);
/* 3747 */         addMessageToQueue(bb);
/*      */       }
/* 3749 */       catch (Exception ex) {
/*      */         
/* 3751 */         logger.log(Level.INFO, this.player.getName() + ":" + ex.getMessage(), ex);
/* 3752 */         this.player.setLink(false);
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
/*      */   static BlockingQueue<PlayerMessage> getMessageQueue() {
/* 3765 */     return MESSAGES_TO_PLAYERS;
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
/*      */   public String toString() {
/* 3777 */     return super.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerCommunicatorQueued.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
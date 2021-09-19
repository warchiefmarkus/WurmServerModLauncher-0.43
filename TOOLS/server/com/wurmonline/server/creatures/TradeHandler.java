/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.economy.Change;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.MonetaryConstants;
/*     */ import com.wurmonline.server.economy.Shop;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.ItemTypes;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.items.Trade;
/*     */ import com.wurmonline.server.items.TradingWindow;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ public final class TradeHandler
/*     */   implements MiscConstants, ItemTypes, MonetaryConstants
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(TradeHandler.class.getName());
/*     */   private Creature creature;
/*     */   private Trade trade;
/*     */   private boolean balanced = false;
/*     */   private boolean waiting = false;
/*  55 */   private final Map<Integer, Set<Item>> itemMap = new HashMap<>();
/*  56 */   private final Map<Integer, List<Item>> currentDemandMap = new HashMap<>();
/*  57 */   private final Map<Integer, Set<Item>> purchaseMap = new HashMap<>();
/*  58 */   private static int maxPersonalItems = 50;
/*     */   private final Shop shop;
/*  60 */   private long pdemand = 0L;
/*     */   private static final float maxNums = 80.0F;
/*     */   private final boolean ownerTrade;
/*  63 */   private final Set<Item> fullPriceItems = new HashSet<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasOtherItems = false;
/*     */ 
/*     */ 
/*     */   
/*     */   TradeHandler(Creature aCreature, Trade _trade) {
/*  72 */     this.creature = aCreature;
/*  73 */     this.trade = _trade;
/*  74 */     this.shop = Economy.getEconomy().getShop(aCreature);
/*  75 */     if (this.shop.isPersonal()) {
/*     */       
/*  77 */       this.ownerTrade = (this.shop.getOwnerId() == this.trade.creatureOne.getWurmId());
/*  78 */       if (this.ownerTrade) {
/*     */         
/*  80 */         this.trade.creatureOne.getCommunicator().sendSafeServerMessage(aCreature
/*  81 */             .getName() + " says, 'Welcome back, " + this.trade.creatureOne.getName() + "!'");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  98 */         this.trade.creatureOne.getCommunicator().sendSafeServerMessage(aCreature
/*  99 */             .getName() + " says, 'I will not buy anything, but i can offer these things.'");
/*     */       } 
/*     */     } else {
/*     */       
/* 103 */       this.ownerTrade = false;
/* 104 */       if (this.shop.getMoney() <= 1L) {
/*     */         
/* 106 */         if (this.trade.creatureOne.getPower() >= 3) {
/* 107 */           this.trade.creatureOne.getCommunicator().sendSafeServerMessage(aCreature
/* 108 */               .getName() + " says, 'I only have " + this.shop.getMoney() + " and can't buy anything right now.'");
/*     */         } else {
/*     */           
/* 111 */           this.trade.creatureOne.getCommunicator().sendSafeServerMessage(aCreature
/* 112 */               .getName() + " says, 'I am a bit low on money and can't buy anything right now.'");
/*     */         } 
/* 114 */       } else if (this.shop.getMoney() < 100L) {
/*     */         
/* 116 */         if (this.trade.creatureOne.getPower() >= 3) {
/* 117 */           this.trade.creatureOne.getCommunicator().sendSafeServerMessage(aCreature
/* 118 */               .getName() + " says, 'I only have " + (new Change(this.shop.getMoney())).getChangeShortString() + ".'");
/*     */         } else {
/*     */           
/* 121 */           this.trade.creatureOne.getCommunicator().sendSafeServerMessage(aCreature
/* 122 */               .getName() + " says, 'I am a bit low on money, but let's see what you have.'");
/*     */         } 
/* 124 */       } else if (this.trade.creatureOne.getPower() >= 3) {
/*     */         
/* 126 */         this.trade.creatureOne.getCommunicator().sendSafeServerMessage(aCreature
/* 127 */             .getName() + " says, 'I have " + (new Change(this.shop.getMoney())).getChangeShortString() + ".'");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void end() {
/* 134 */     this.creature = null;
/* 135 */     this.trade = null;
/*     */   }
/*     */ 
/*     */   
/*     */   void addToInventory(Item item, long inventoryWindow) {
/* 140 */     if (this.trade != null)
/*     */     {
/* 142 */       if (inventoryWindow == 2L) {
/*     */         
/* 144 */         tradeChanged();
/* 145 */         if (logger.isLoggable(Level.FINEST) && item != null)
/*     */         {
/* 147 */           logger.finest("Added " + item.getName() + " to his offer window.");
/*     */         }
/*     */       }
/* 150 */       else if (inventoryWindow == 1L) {
/*     */         
/* 152 */         if (logger.isLoggable(Level.FINEST) && item != null)
/*     */         {
/* 154 */           logger.finest("Added " + item.getName() + " to my offer window.");
/*     */         }
/*     */       }
/* 157 */       else if (inventoryWindow == 3L) {
/*     */         
/* 159 */         if (logger.isLoggable(Level.FINEST) && item != null)
/*     */         {
/* 161 */           logger.finest("Added " + item.getName() + " to his request window.");
/*     */         }
/*     */       }
/* 164 */       else if (inventoryWindow == 4L) {
/*     */         
/* 166 */         if (logger.isLoggable(Level.FINEST) && item != null)
/*     */         {
/* 168 */           logger.finest("Added " + item.getName() + " to my request window.");
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void tradeChanged() {
/* 176 */     this.balanced = false;
/* 177 */     this.waiting = false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addItemsToTrade() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield trade : Lcom/wurmonline/server/items/Trade;
/*     */     //   4: ifnull -> 588
/*     */     //   7: iconst_0
/*     */     //   8: istore_1
/*     */     //   9: iconst_0
/*     */     //   10: istore_2
/*     */     //   11: aload_0
/*     */     //   12: getfield creature : Lcom/wurmonline/server/creatures/Creature;
/*     */     //   15: invokevirtual getInventory : ()Lcom/wurmonline/server/items/Item;
/*     */     //   18: invokevirtual getItems : ()Ljava/util/Set;
/*     */     //   21: astore_3
/*     */     //   22: aload_3
/*     */     //   23: aload_3
/*     */     //   24: invokeinterface size : ()I
/*     */     //   29: anewarray com/wurmonline/server/items/Item
/*     */     //   32: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
/*     */     //   37: checkcast [Lcom/wurmonline/server/items/Item;
/*     */     //   40: astore #4
/*     */     //   42: aload_0
/*     */     //   43: getfield trade : Lcom/wurmonline/server/items/Trade;
/*     */     //   46: lconst_1
/*     */     //   47: invokevirtual getTradingWindow : (J)Lcom/wurmonline/server/items/TradingWindow;
/*     */     //   50: astore #5
/*     */     //   52: bipush #-10
/*     */     //   54: istore #6
/*     */     //   56: aload #5
/*     */     //   58: invokevirtual startReceivingItems : ()V
/*     */     //   61: iconst_0
/*     */     //   62: istore #7
/*     */     //   64: iload #7
/*     */     //   66: aload #4
/*     */     //   68: arraylength
/*     */     //   69: if_icmpge -> 385
/*     */     //   72: aload #4
/*     */     //   74: iload #7
/*     */     //   76: aaload
/*     */     //   77: invokevirtual getTemplateId : ()I
/*     */     //   80: istore #6
/*     */     //   82: aload_0
/*     */     //   83: getfield itemMap : Ljava/util/Map;
/*     */     //   86: iload #6
/*     */     //   88: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   91: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   96: checkcast java/util/Set
/*     */     //   99: astore #8
/*     */     //   101: aload #8
/*     */     //   103: ifnonnull -> 115
/*     */     //   106: new java/util/HashSet
/*     */     //   109: dup
/*     */     //   110: invokespecial <init> : ()V
/*     */     //   113: astore #8
/*     */     //   115: aload #8
/*     */     //   117: aload #4
/*     */     //   119: iload #7
/*     */     //   121: aaload
/*     */     //   122: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   127: pop
/*     */     //   128: aload_0
/*     */     //   129: getfield shop : Lcom/wurmonline/server/economy/Shop;
/*     */     //   132: invokevirtual isPersonal : ()Z
/*     */     //   135: ifeq -> 182
/*     */     //   138: aload_0
/*     */     //   139: getfield ownerTrade : Z
/*     */     //   142: ifeq -> 158
/*     */     //   145: aload #5
/*     */     //   147: aload #4
/*     */     //   149: iload #7
/*     */     //   151: aaload
/*     */     //   152: invokevirtual addItem : (Lcom/wurmonline/server/items/Item;)V
/*     */     //   155: goto -> 362
/*     */     //   158: aload #4
/*     */     //   160: iload #7
/*     */     //   162: aaload
/*     */     //   163: invokevirtual isCoin : ()Z
/*     */     //   166: ifne -> 362
/*     */     //   169: aload #5
/*     */     //   171: aload #4
/*     */     //   173: iload #7
/*     */     //   175: aaload
/*     */     //   176: invokevirtual addItem : (Lcom/wurmonline/server/items/Item;)V
/*     */     //   179: goto -> 362
/*     */     //   182: aload #4
/*     */     //   184: iload #7
/*     */     //   186: aaload
/*     */     //   187: invokevirtual getTemplateId : ()I
/*     */     //   190: sipush #843
/*     */     //   193: if_icmpne -> 234
/*     */     //   196: getstatic com/wurmonline/server/Features$Feature.NAMECHANGE : Lcom/wurmonline/server/Features$Feature;
/*     */     //   199: invokevirtual isEnabled : ()Z
/*     */     //   202: ifne -> 232
/*     */     //   205: aload #8
/*     */     //   207: aload #4
/*     */     //   209: iload #7
/*     */     //   211: aaload
/*     */     //   212: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */     //   217: pop
/*     */     //   218: aload #4
/*     */     //   220: iload #7
/*     */     //   222: aaload
/*     */     //   223: invokevirtual getWurmId : ()J
/*     */     //   226: invokestatic destroyItem : (J)V
/*     */     //   229: goto -> 379
/*     */     //   232: iconst_1
/*     */     //   233: istore_1
/*     */     //   234: aload #4
/*     */     //   236: iload #7
/*     */     //   238: aaload
/*     */     //   239: invokevirtual getTemplateId : ()I
/*     */     //   242: sipush #1129
/*     */     //   245: if_icmpne -> 286
/*     */     //   248: getstatic com/wurmonline/server/Features$Feature.WAGONER : Lcom/wurmonline/server/Features$Feature;
/*     */     //   251: invokevirtual isEnabled : ()Z
/*     */     //   254: ifne -> 284
/*     */     //   257: aload #8
/*     */     //   259: aload #4
/*     */     //   261: iload #7
/*     */     //   263: aaload
/*     */     //   264: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */     //   269: pop
/*     */     //   270: aload #4
/*     */     //   272: iload #7
/*     */     //   274: aaload
/*     */     //   275: invokevirtual getWurmId : ()J
/*     */     //   278: invokestatic destroyItem : (J)V
/*     */     //   281: goto -> 379
/*     */     //   284: iconst_1
/*     */     //   285: istore_2
/*     */     //   286: aload #8
/*     */     //   288: invokeinterface size : ()I
/*     */     //   293: bipush #10
/*     */     //   295: if_icmpge -> 326
/*     */     //   298: aload #4
/*     */     //   300: iload #7
/*     */     //   302: aaload
/*     */     //   303: invokevirtual getLockId : ()J
/*     */     //   306: ldc2_w -10
/*     */     //   309: lcmp
/*     */     //   310: ifne -> 326
/*     */     //   313: aload #5
/*     */     //   315: aload #4
/*     */     //   317: iload #7
/*     */     //   319: aaload
/*     */     //   320: invokevirtual addItem : (Lcom/wurmonline/server/items/Item;)V
/*     */     //   323: goto -> 362
/*     */     //   326: aload #8
/*     */     //   328: invokeinterface size : ()I
/*     */     //   333: bipush #50
/*     */     //   335: if_icmple -> 362
/*     */     //   338: aload #8
/*     */     //   340: aload #4
/*     */     //   342: iload #7
/*     */     //   344: aaload
/*     */     //   345: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */     //   350: pop
/*     */     //   351: aload #4
/*     */     //   353: iload #7
/*     */     //   355: aaload
/*     */     //   356: invokevirtual getWurmId : ()J
/*     */     //   359: invokestatic destroyItem : (J)V
/*     */     //   362: aload_0
/*     */     //   363: getfield itemMap : Ljava/util/Map;
/*     */     //   366: iload #6
/*     */     //   368: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   371: aload #8
/*     */     //   373: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   378: pop
/*     */     //   379: iinc #7, 1
/*     */     //   382: goto -> 64
/*     */     //   385: aload_0
/*     */     //   386: getfield shop : Lcom/wurmonline/server/economy/Shop;
/*     */     //   389: invokevirtual isPersonal : ()Z
/*     */     //   392: ifne -> 583
/*     */     //   395: iconst_0
/*     */     //   396: istore #7
/*     */     //   398: getstatic com/wurmonline/server/Features$Feature.NAMECHANGE : Lcom/wurmonline/server/Features$Feature;
/*     */     //   401: invokevirtual isEnabled : ()Z
/*     */     //   404: ifeq -> 469
/*     */     //   407: iload_1
/*     */     //   408: ifne -> 469
/*     */     //   411: aload_0
/*     */     //   412: getfield creature : Lcom/wurmonline/server/creatures/Creature;
/*     */     //   415: invokevirtual getInventory : ()Lcom/wurmonline/server/items/Item;
/*     */     //   418: astore #8
/*     */     //   420: sipush #843
/*     */     //   423: bipush #60
/*     */     //   425: getstatic com/wurmonline/server/Server.rand : Ljava/util/Random;
/*     */     //   428: bipush #40
/*     */     //   430: invokevirtual nextInt : (I)I
/*     */     //   433: iadd
/*     */     //   434: i2f
/*     */     //   435: invokestatic createItem : (IF)Lcom/wurmonline/server/items/Item;
/*     */     //   438: astore #9
/*     */     //   440: aload #8
/*     */     //   442: aload #9
/*     */     //   444: invokevirtual insertItem : (Lcom/wurmonline/server/items/Item;)Z
/*     */     //   447: pop
/*     */     //   448: iconst_1
/*     */     //   449: istore #7
/*     */     //   451: goto -> 469
/*     */     //   454: astore #8
/*     */     //   456: getstatic com/wurmonline/server/creatures/TradeHandler.logger : Ljava/util/logging/Logger;
/*     */     //   459: getstatic java/util/logging/Level.INFO : Ljava/util/logging/Level;
/*     */     //   462: ldc 'Failed to create name change cert for creature.'
/*     */     //   464: aload #8
/*     */     //   466: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   469: getstatic com/wurmonline/server/Features$Feature.WAGONER : Lcom/wurmonline/server/Features$Feature;
/*     */     //   472: invokevirtual isEnabled : ()Z
/*     */     //   475: ifeq -> 540
/*     */     //   478: iload_2
/*     */     //   479: ifne -> 540
/*     */     //   482: aload_0
/*     */     //   483: getfield creature : Lcom/wurmonline/server/creatures/Creature;
/*     */     //   486: invokevirtual getInventory : ()Lcom/wurmonline/server/items/Item;
/*     */     //   489: astore #8
/*     */     //   491: sipush #1129
/*     */     //   494: bipush #60
/*     */     //   496: getstatic com/wurmonline/server/Server.rand : Ljava/util/Random;
/*     */     //   499: bipush #40
/*     */     //   501: invokevirtual nextInt : (I)I
/*     */     //   504: iadd
/*     */     //   505: i2f
/*     */     //   506: invokestatic createItem : (IF)Lcom/wurmonline/server/items/Item;
/*     */     //   509: astore #9
/*     */     //   511: aload #8
/*     */     //   513: aload #9
/*     */     //   515: invokevirtual insertItem : (Lcom/wurmonline/server/items/Item;)Z
/*     */     //   518: pop
/*     */     //   519: iconst_1
/*     */     //   520: istore #7
/*     */     //   522: goto -> 540
/*     */     //   525: astore #8
/*     */     //   527: getstatic com/wurmonline/server/creatures/TradeHandler.logger : Ljava/util/logging/Logger;
/*     */     //   530: getstatic java/util/logging/Level.INFO : Ljava/util/logging/Level;
/*     */     //   533: ldc 'Failed to create wagoner contract for creature.'
/*     */     //   535: aload #8
/*     */     //   537: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   540: iload #7
/*     */     //   542: ifeq -> 583
/*     */     //   545: aload_0
/*     */     //   546: getfield trade : Lcom/wurmonline/server/items/Trade;
/*     */     //   549: getfield creatureOne : Lcom/wurmonline/server/creatures/Creature;
/*     */     //   552: invokevirtual getCommunicator : ()Lcom/wurmonline/server/creatures/Communicator;
/*     */     //   555: new java/lang/StringBuilder
/*     */     //   558: dup
/*     */     //   559: invokespecial <init> : ()V
/*     */     //   562: aload_0
/*     */     //   563: getfield creature : Lcom/wurmonline/server/creatures/Creature;
/*     */     //   566: invokevirtual getName : ()Ljava/lang/String;
/*     */     //   569: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   572: ldc ' says, 'Oh, I forgot I have some new merchandise. Let us trade again and I will show them to you.''
/*     */     //   574: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   577: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   580: invokevirtual sendSafeServerMessage : (Ljava/lang/String;)V
/*     */     //   583: aload #5
/*     */     //   585: invokevirtual stopReceivingItems : ()V
/*     */     //   588: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #183	-> 0
/*     */     //   #185	-> 7
/*     */     //   #186	-> 9
/*     */     //   #187	-> 11
/*     */     //   #188	-> 22
/*     */     //   #189	-> 42
/*     */     //   #190	-> 52
/*     */     //   #191	-> 56
/*     */     //   #192	-> 61
/*     */     //   #194	-> 72
/*     */     //   #195	-> 82
/*     */     //   #196	-> 101
/*     */     //   #197	-> 106
/*     */     //   #198	-> 115
/*     */     //   #199	-> 128
/*     */     //   #201	-> 138
/*     */     //   #202	-> 145
/*     */     //   #203	-> 158
/*     */     //   #204	-> 169
/*     */     //   #208	-> 182
/*     */     //   #210	-> 196
/*     */     //   #212	-> 205
/*     */     //   #213	-> 218
/*     */     //   #214	-> 229
/*     */     //   #217	-> 232
/*     */     //   #219	-> 234
/*     */     //   #221	-> 248
/*     */     //   #223	-> 257
/*     */     //   #224	-> 270
/*     */     //   #225	-> 281
/*     */     //   #228	-> 284
/*     */     //   #230	-> 286
/*     */     //   #232	-> 313
/*     */     //   #238	-> 326
/*     */     //   #241	-> 338
/*     */     //   #242	-> 351
/*     */     //   #246	-> 362
/*     */     //   #192	-> 379
/*     */     //   #248	-> 385
/*     */     //   #251	-> 395
/*     */     //   #252	-> 398
/*     */     //   #256	-> 411
/*     */     //   #257	-> 420
/*     */     //   #258	-> 440
/*     */     //   #259	-> 448
/*     */     //   #264	-> 451
/*     */     //   #261	-> 454
/*     */     //   #263	-> 456
/*     */     //   #266	-> 469
/*     */     //   #270	-> 482
/*     */     //   #271	-> 491
/*     */     //   #272	-> 511
/*     */     //   #273	-> 519
/*     */     //   #278	-> 522
/*     */     //   #275	-> 525
/*     */     //   #277	-> 527
/*     */     //   #280	-> 540
/*     */     //   #282	-> 545
/*     */     //   #283	-> 566
/*     */     //   #282	-> 580
/*     */     //   #287	-> 583
/*     */     //   #289	-> 588
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   101	278	8	its	Ljava/util/Set;
/*     */     //   64	321	7	x	I
/*     */     //   420	31	8	inventory	Lcom/wurmonline/server/items/Item;
/*     */     //   440	11	9	item	Lcom/wurmonline/server/items/Item;
/*     */     //   456	13	8	ex	Ljava/lang/Exception;
/*     */     //   491	31	8	inventory	Lcom/wurmonline/server/items/Item;
/*     */     //   511	11	9	item	Lcom/wurmonline/server/items/Item;
/*     */     //   527	13	8	ex	Ljava/lang/Exception;
/*     */     //   398	185	7	newMerchandise	Z
/*     */     //   9	579	1	foundDeclaration	Z
/*     */     //   11	577	2	foundWagonerContract	Z
/*     */     //   22	566	3	ite	Ljava/util/Set;
/*     */     //   42	546	4	itarr	[Lcom/wurmonline/server/items/Item;
/*     */     //   52	536	5	myOffers	Lcom/wurmonline/server/items/TradingWindow;
/*     */     //   56	532	6	templateId	I
/*     */     //   0	589	0	this	Lcom/wurmonline/server/creatures/TradeHandler;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   101	278	8	its	Ljava/util/Set<Lcom/wurmonline/server/items/Item;>;
/*     */     //   22	566	3	ite	Ljava/util/Set<Lcom/wurmonline/server/items/Item;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   411	451	454	java/lang/Exception
/*     */     //   482	522	525	java/lang/Exception
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTraderSellPriceForItem(Item item, TradingWindow window) {
/* 293 */     if (item.isFullprice())
/* 294 */       return item.getValue(); 
/* 295 */     if (this.shop.isPersonal() && item.getPrice() > 0)
/*     */     {
/* 297 */       return item.getPrice();
/*     */     }
/* 299 */     int numberSold = 1;
/* 300 */     if (window == this.trade.getCreatureOneRequestWindow())
/*     */     {
/* 302 */       if (!this.shop.isPersonal() || this.shop.usesLocalPrice()) {
/*     */         
/* 304 */         if (item.isCombine()) {
/*     */           
/* 306 */           ItemTemplate temp = item.getTemplate();
/* 307 */           numberSold = Math.max(1, item.getWeightGrams() / temp.getWeightGrams());
/*     */         } 
/* 309 */         Item[] whatPlayerWants = this.trade.getCreatureOneRequestWindow().getItems();
/* 310 */         for (int x = 0; x < whatPlayerWants.length; x++) {
/*     */           
/* 312 */           if (whatPlayerWants[x] != item && whatPlayerWants[x].getTemplateId() == item.getTemplateId())
/*     */           {
/* 314 */             if (whatPlayerWants[x].isCombine()) {
/*     */               
/* 316 */               ItemTemplate temp = whatPlayerWants[x].getTemplate();
/* 317 */               numberSold += Math.max(1, whatPlayerWants[x].getWeightGrams() / temp.getWeightGrams());
/*     */             } else {
/*     */               
/* 320 */               numberSold++;
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     }
/* 325 */     double localPrice = this.shop.getLocalTraderSellPrice(item, 100, numberSold);
/* 326 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 328 */       logger.finest("localSellPrice for " + item.getName() + "=" + localPrice + " numberSold=" + numberSold);
/*     */     }
/* 330 */     return (int)Math.max(2.0D, localPrice * this.shop.getPriceModifier());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTraderBuyPriceForItem(Item item) {
/* 335 */     if (item.isFullprice())
/* 336 */       return item.getValue(); 
/* 337 */     int price = 1;
/* 338 */     List<Item> itlist = this.currentDemandMap.get(Integer.valueOf(item.getTemplateId()));
/* 339 */     int extra = 1;
/* 340 */     if (itlist == null) {
/*     */       
/* 342 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 344 */         logger.finest("Weird. We're trading items which don't exist.");
/*     */       }
/* 346 */       extra = 1;
/*     */ 
/*     */     
/*     */     }
/* 350 */     else if (item.isCombine()) {
/*     */       
/* 352 */       ItemTemplate temp = item.getTemplate();
/* 353 */       for (Item i : itlist)
/*     */       {
/* 355 */         extra += Math.max(1, i.getWeightGrams() / temp.getWeightGrams());
/*     */       }
/*     */     } else {
/*     */       
/* 359 */       extra += itlist.size();
/*     */     } 
/* 361 */     price = (int)this.shop.getLocalTraderBuyPrice(item, 1, extra);
/*     */     
/* 363 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 365 */       logger.finest("localBuyPrice for " + item.getName() + "=" + price + " extra=" + extra + " price for extra+1=" + 
/* 366 */           (int)this.shop.getLocalTraderBuyPrice(item, 1, extra + 1));
/*     */     }
/* 368 */     return Math.max(0, price);
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
/*     */   private long getDiff() {
/* 380 */     if (this.shop.isPersonal())
/*     */     {
/* 382 */       if (this.ownerTrade)
/* 383 */         return 0L; 
/*     */     }
/* 385 */     Item[] whatPlayerWants = this.trade.getCreatureOneRequestWindow().getItems();
/* 386 */     Item[] whatIWant = this.trade.getCreatureTwoRequestWindow().getItems();
/* 387 */     this.pdemand = 0L;
/* 388 */     long mydemand = 0L;
/* 389 */     int templateId = -10;
/*     */ 
/*     */ 
/*     */     
/* 393 */     this.fullPriceItems.clear();
/* 394 */     this.hasOtherItems = false;
/* 395 */     this.purchaseMap.clear();
/*     */ 
/*     */     
/*     */     int x;
/*     */ 
/*     */     
/* 401 */     for (x = 0; x < whatPlayerWants.length; x++) {
/*     */       
/* 403 */       if (whatPlayerWants[x].isFullprice()) {
/*     */         
/* 405 */         this.pdemand += whatPlayerWants[x].getValue();
/* 406 */         this.fullPriceItems.add(whatPlayerWants[x]);
/*     */       }
/*     */       else {
/*     */         
/* 410 */         double localPrice = 2.0D;
/* 411 */         if (this.shop.isPersonal() && whatPlayerWants[x].getPrice() > 0) {
/*     */           
/* 413 */           localPrice = whatPlayerWants[x].getPrice();
/* 414 */           this.pdemand = (long)(this.pdemand + localPrice);
/*     */         }
/*     */         else {
/*     */           
/* 418 */           templateId = whatPlayerWants[x].getTemplateId();
/* 419 */           int numberSold = 0;
/*     */           
/*     */           try {
/* 422 */             if (!this.shop.isPersonal() || this.shop.usesLocalPrice()) {
/*     */               
/* 424 */               int tid = whatPlayerWants[x].getTemplateId();
/* 425 */               Set<Item> itlist = this.purchaseMap.get(Integer.valueOf(tid));
/* 426 */               if (itlist == null) {
/* 427 */                 itlist = new HashSet<>();
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 432 */               itlist.add(whatPlayerWants[x]);
/* 433 */               this.purchaseMap.put(Integer.valueOf(tid), itlist);
/*     */ 
/*     */ 
/*     */               
/* 437 */               if (whatPlayerWants[x].isCombine()) {
/*     */                 
/* 439 */                 ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/* 440 */                 for (Item i : itlist)
/*     */                 {
/* 442 */                   numberSold += Math.max(1, i.getWeightGrams() / temp.getWeightGrams());
/*     */                 }
/*     */               } else {
/*     */                 
/* 446 */                 numberSold = itlist.size();
/*     */               } 
/*     */             } 
/* 449 */             localPrice = this.shop.getLocalTraderSellPrice(whatPlayerWants[x], 100, numberSold);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 455 */             if (logger.isLoggable(Level.FINEST))
/*     */             {
/* 457 */               logger.finest("LocalSellPrice for " + whatPlayerWants[x].getName() + "=" + localPrice + " mod=" + this.shop
/* 458 */                   .getPriceModifier() + " sum=" + Math.max(2.0D, localPrice * this.shop.getPriceModifier()));
/*     */             }
/* 460 */             this.pdemand = (long)(this.pdemand + Math.max(2.0D, localPrice * this.shop.getPriceModifier()));
/*     */           }
/* 462 */           catch (NoSuchTemplateException nst) {
/*     */             
/* 464 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 476 */     this.purchaseMap.clear();
/* 477 */     for (x = 0; x < whatIWant.length; x++) {
/*     */       
/* 479 */       if (whatIWant[x].isFullprice()) {
/* 480 */         mydemand += whatIWant[x].getValue();
/*     */       } else {
/*     */         
/* 483 */         this.hasOtherItems = true;
/* 484 */         if (this.fullPriceItems.isEmpty()) {
/*     */           
/* 486 */           long price = 1L;
/*     */ 
/*     */           
/* 489 */           int tid = whatIWant[x].getTemplateId();
/* 490 */           Set<Item> itlist = this.purchaseMap.get(Integer.valueOf(tid));
/* 491 */           if (itlist == null) {
/* 492 */             itlist = new HashSet<>();
/*     */           }
/* 494 */           itlist.add(whatIWant[x]);
/* 495 */           this.purchaseMap.put(Integer.valueOf(tid), itlist);
/* 496 */           int extra = 0;
/* 497 */           if (whatIWant[x].isCombine()) {
/*     */ 
/*     */             
/*     */             try {
/* 501 */               ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(whatIWant[x]
/* 502 */                   .getTemplateId());
/* 503 */               for (Item i : itlist)
/*     */               {
/* 505 */                 extra += Math.max(1, i.getWeightGrams() / temp.getWeightGrams());
/*     */               }
/*     */             }
/* 508 */             catch (NoSuchTemplateException nst) {
/*     */               
/* 510 */               logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */             } 
/*     */           } else {
/*     */             
/* 514 */             extra = itlist.size();
/* 515 */           }  price = this.shop.getLocalTraderBuyPrice(whatIWant[x], 1, extra);
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
/* 528 */           mydemand += Math.max(0L, price);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 533 */     long diff = this.pdemand - mydemand;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     return diff;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getMaxNumPersonalItems() {
/* 546 */     return maxPersonalItems;
/*     */   }
/*     */ 
/*     */   
/*     */   private void suckInterestingItems() {
/* 551 */     TradingWindow currWin = this.trade.getTradingWindow(2L);
/* 552 */     TradingWindow targetWin = this.trade.getTradingWindow(4L);
/* 553 */     Item[] offItems = currWin.getItems();
/* 554 */     Item[] setItems = targetWin.getItems();
/* 555 */     if (!this.shop.isPersonal()) {
/*     */       
/* 557 */       this.currentDemandMap.clear();
/*     */       
/* 559 */       for (int x = 0; x < setItems.length; x++) {
/*     */         
/* 561 */         int j = setItems[x].getTemplateId();
/* 562 */         List<Item> itlist = this.currentDemandMap.get(Integer.valueOf(j));
/* 563 */         if (itlist == null)
/* 564 */           itlist = new LinkedList<>(); 
/* 565 */         itlist.add(setItems[x]);
/* 566 */         this.currentDemandMap.put(Integer.valueOf(j), itlist);
/*     */       } 
/*     */       
/* 569 */       int templateId = -10;
/* 570 */       targetWin.startReceivingItems();
/* 571 */       for (int i = 0; i < offItems.length; i++) {
/*     */         
/* 573 */         if (!offItems[i].isArtifact() && offItems[i].isPurchased() && offItems[i].getLockId() == -10L) {
/*     */           
/* 575 */           Item parent = offItems[i];
/*     */           
/*     */           try {
/* 578 */             parent = offItems[i].getParent();
/*     */           }
/* 580 */           catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */ 
/*     */           
/* 584 */           if (offItems[i] == parent || parent.isViewableBy(this.creature))
/*     */           {
/* 586 */             if (offItems[i].isHollow() && !offItems[i].isEmpty(true)) {
/*     */               
/* 588 */               this.trade.creatureOne.getCommunicator().sendSafeServerMessage(this.creature
/* 589 */                   .getName() + " says, 'Please empty the " + offItems[i].getName() + " first.'");
/*     */             }
/*     */             else {
/*     */               
/* 593 */               templateId = offItems[i].getTemplateId();
/*     */               
/* 595 */               List<Item> itlist = this.currentDemandMap.get(Integer.valueOf(templateId));
/* 596 */               if (itlist == null) {
/* 597 */                 itlist = new LinkedList<>();
/*     */               }
/* 599 */               if (itlist.size() < 80.0F)
/*     */               {
/* 601 */                 currWin.removeItem(offItems[i]);
/* 602 */                 targetWin.addItem(offItems[i]);
/* 603 */                 itlist.add(offItems[i]);
/* 604 */                 this.currentDemandMap.put(Integer.valueOf(templateId), itlist);
/*     */               }
/*     */             
/*     */             } 
/*     */           }
/* 609 */         } else if ((offItems[i].isHomesteadDeed() || offItems[i].isVillageDeed()) && offItems[i].getData2() <= 0) {
/*     */           
/* 611 */           templateId = offItems[i].getTemplateId();
/* 612 */           List<Item> itlist = this.currentDemandMap.get(Integer.valueOf(templateId));
/* 613 */           if (itlist == null)
/* 614 */             itlist = new LinkedList<>(); 
/* 615 */           currWin.removeItem(offItems[i]);
/* 616 */           targetWin.addItem(offItems[i]);
/* 617 */           itlist.add(offItems[i]);
/* 618 */           this.currentDemandMap.put(Integer.valueOf(templateId), itlist);
/*     */         } 
/*     */       } 
/* 621 */       targetWin.stopReceivingItems();
/*     */ 
/*     */     
/*     */     }
/* 625 */     else if (this.ownerTrade) {
/*     */       
/* 627 */       TradingWindow myOffers = this.trade.getTradingWindow(1L);
/* 628 */       Item[] currItems = myOffers.getItems();
/* 629 */       int size = 0;
/* 630 */       for (int c = 0; c < currItems.length; c++) {
/*     */         
/* 632 */         if (!currItems[c].isCoin())
/* 633 */           size++; 
/*     */       } 
/* 635 */       size += setItems.length;
/* 636 */       if (size > maxPersonalItems) {
/* 637 */         this.trade.creatureOne.getCommunicator().sendNormalServerMessage(this.creature
/* 638 */             .getName() + " says, 'I cannot add more items to my stock right now.'");
/*     */       } else {
/*     */         
/* 641 */         TradingWindow hisReq = this.trade.getTradingWindow(3L);
/* 642 */         Item[] reqItems = hisReq.getItems();
/* 643 */         for (int i = 0; i < reqItems.length; i++) {
/* 644 */           if (!reqItems[i].isCoin())
/* 645 */             size++; 
/* 646 */         }  if (size > maxPersonalItems) {
/* 647 */           this.trade.creatureOne.getCommunicator().sendNormalServerMessage(this.creature
/* 648 */               .getName() + " says, 'I cannot add more items to my stock right now.'");
/*     */         } else {
/*     */           
/* 651 */           targetWin.startReceivingItems();
/* 652 */           for (int x = 0; x < offItems.length; x++) {
/*     */             
/* 654 */             if (offItems[x].getTemplateId() != 272 && offItems[x].getTemplateId() != 781 && 
/* 655 */               !offItems[x].isArtifact() && !offItems[x].isRoyal() && ((
/* 656 */               !offItems[x].isVillageDeed() && !offItems[x].isHomesteadDeed()) || !offItems[x].hasData()) && (offItems[x]
/* 657 */               .getTemplateId() != 300 || offItems[x].getData2() == -1))
/*     */             {
/* 659 */               if (size > maxPersonalItems) {
/*     */                 
/* 661 */                 if (offItems[x].isCoin())
/*     */                 {
/* 663 */                   currWin.removeItem(offItems[x]);
/* 664 */                   targetWin.addItem(offItems[x]);
/*     */                 
/*     */                 }
/*     */               
/*     */               }
/* 669 */               else if ((offItems[x].isLockable() && offItems[x].isLocked()) || (offItems[x]
/* 670 */                 .isHollow() && !offItems[x].isEmpty(true) && 
/* 671 */                 !offItems[x].isSealedByPlayer())) {
/*     */                 
/* 673 */                 if (offItems[x].isLockable() && offItems[x].isLocked()) {
/* 674 */                   this.trade.creatureOne.getCommunicator().sendSafeServerMessage(this.creature
/* 675 */                       .getName() + " says, 'I don't accept locked items any more. Sorry for the inconvenience.'");
/*     */                 } else {
/* 677 */                   this.trade.creatureOne.getCommunicator().sendSafeServerMessage(this.creature
/* 678 */                       .getName() + " says, 'Please empty the " + offItems[x].getName() + " first.'");
/*     */                 }
/*     */               
/*     */               } else {
/*     */                 
/* 683 */                 currWin.removeItem(offItems[x]);
/* 684 */                 targetWin.addItem(offItems[x]);
/* 685 */                 size++;
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 690 */           targetWin.stopReceivingItems();
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 696 */       targetWin.startReceivingItems();
/* 697 */       for (int x = 0; x < offItems.length; x++) {
/*     */         
/* 699 */         if (offItems[x].isCoin()) {
/*     */           
/* 701 */           Item parent = offItems[x];
/*     */           
/*     */           try {
/* 704 */             parent = offItems[x].getParent();
/*     */           }
/* 706 */           catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */ 
/*     */           
/* 710 */           if (offItems[x] == parent || parent.isViewableBy(this.creature)) {
/*     */             
/* 712 */             currWin.removeItem(offItems[x]);
/* 713 */             targetWin.addItem(offItems[x]);
/*     */           } 
/*     */         } 
/*     */       } 
/* 717 */       targetWin.stopReceivingItems();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void balance() {
/* 724 */     if (!this.balanced)
/*     */     {
/* 726 */       if (this.ownerTrade) {
/*     */         
/* 728 */         suckInterestingItems();
/* 729 */         this.trade.setSatisfied(this.creature, true, this.trade.getCurrentCounter());
/* 730 */         this.balanced = true;
/*     */       }
/* 732 */       else if (this.shop.isPersonal() && !this.waiting) {
/*     */         
/* 734 */         suckInterestingItems();
/* 735 */         removeCoins(this.trade.getCreatureOneRequestWindow().getItems());
/* 736 */         long diff = getDiff();
/* 737 */         if (diff > 0L)
/*     */         {
/* 739 */           this.waiting = true;
/* 740 */           Change change = new Change(diff);
/* 741 */           String toSend = this.creature.getName() + " demands ";
/* 742 */           toSend = toSend + change.getChangeString();
/* 743 */           toSend = toSend + " coins to make the trade.";
/* 744 */           this.trade.creatureOne.getCommunicator().sendSafeServerMessage(toSend);
/*     */         }
/* 746 */         else if (diff < 0L)
/*     */         {
/*     */ 
/*     */           
/* 750 */           Item[] mon = Economy.getEconomy().getCoinsFor(Math.abs(diff));
/* 751 */           this.trade.getCreatureOneRequestWindow().startReceivingItems();
/* 752 */           for (int x = 0; x < mon.length; x++)
/* 753 */             this.trade.getCreatureOneRequestWindow().addItem(mon[x]); 
/* 754 */           this.trade.getCreatureOneRequestWindow().stopReceivingItems();
/* 755 */           this.trade.setSatisfied(this.creature, true, this.trade.getCurrentCounter());
/* 756 */           this.trade.setMoneyAdded(this.pdemand);
/* 757 */           this.balanced = true;
/*     */         
/*     */         }
/*     */         else
/*     */         {
/* 762 */           this.trade.setMoneyAdded(this.pdemand);
/* 763 */           this.trade.setSatisfied(this.creature, true, this.trade.getCurrentCounter());
/* 764 */           this.balanced = true;
/*     */         }
/*     */       
/* 767 */       } else if (!this.waiting) {
/*     */         
/* 769 */         suckInterestingItems();
/* 770 */         removeCoins(this.trade.getCreatureOneRequestWindow().getItems());
/* 771 */         long diff = getDiff();
/* 772 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/* 774 */           logger.finest("diff is " + diff);
/*     */         }
/* 776 */         if (!this.fullPriceItems.isEmpty() && this.hasOtherItems) {
/*     */           
/* 778 */           for (Item lItem : this.fullPriceItems)
/* 779 */             this.trade.creatureOne.getCommunicator().sendSafeServerMessage(this.creature
/* 780 */                 .getName() + " says, 'Sorry, " + this.trade.creatureOne.getName() + ". I must charge full price in coin value for the " + lItem
/* 781 */                 .getName() + ".'"); 
/* 782 */           this.waiting = true;
/*     */         }
/* 784 */         else if (diff > 0L) {
/*     */           
/* 786 */           this.waiting = true;
/* 787 */           Change change = new Change(diff);
/* 788 */           String toSend = this.creature.getName() + " demands ";
/* 789 */           toSend = toSend + change.getChangeString();
/* 790 */           toSend = toSend + " coins to make the trade.";
/* 791 */           this.trade.creatureOne.getCommunicator().sendSafeServerMessage(toSend);
/*     */         }
/* 793 */         else if (diff < 0L) {
/*     */           
/* 795 */           if (Math.abs(diff) > this.shop.getMoney())
/*     */           {
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
/* 810 */             for (Item i : this.trade.getCreatureTwoRequestWindow().getAllItems()) {
/* 811 */               if (!i.isCoin()) {
/*     */                 
/* 813 */                 String toSend = this.creature.getName() + " says, 'I am low on cash and can not purchase those items.'";
/*     */                 
/* 815 */                 this.waiting = true;
/*     */                 
/* 817 */                 this.trade.creatureOne.getCommunicator().sendSafeServerMessage(toSend);
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/* 822 */             Item[] mon = Economy.getEconomy().getCoinsFor(Math.abs(diff));
/* 823 */             this.trade.getCreatureOneRequestWindow().startReceivingItems();
/* 824 */             for (int x = 0; x < mon.length; x++)
/* 825 */               this.trade.getCreatureOneRequestWindow().addItem(mon[x]); 
/* 826 */             this.trade.getCreatureOneRequestWindow().stopReceivingItems();
/* 827 */             this.trade.setSatisfied(this.creature, true, this.trade.getCurrentCounter());
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 832 */             this.balanced = true;
/*     */           }
/*     */           else
/*     */           {
/* 836 */             Item[] mon = Economy.getEconomy().getCoinsFor(Math.abs(diff));
/* 837 */             this.trade.getCreatureOneRequestWindow().startReceivingItems();
/* 838 */             for (int x = 0; x < mon.length; x++)
/* 839 */               this.trade.getCreatureOneRequestWindow().addItem(mon[x]); 
/* 840 */             this.trade.getCreatureOneRequestWindow().stopReceivingItems();
/* 841 */             this.trade.setSatisfied(this.creature, true, this.trade.getCurrentCounter());
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 846 */             this.balanced = true;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 851 */           this.trade.setSatisfied(this.creature, true, this.trade.getCurrentCounter());
/* 852 */           this.balanced = true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean removeCoins(Item[] items) {
/* 860 */     boolean foundCoins = false;
/* 861 */     for (int x = 0; x < items.length; x++) {
/*     */       
/* 863 */       if (items[x].isCoin()) {
/*     */         
/* 865 */         foundCoins = true;
/* 866 */         this.trade.getCreatureOneRequestWindow().removeItem(items[x]);
/*     */       } 
/*     */     } 
/* 869 */     return foundCoins;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\TradeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
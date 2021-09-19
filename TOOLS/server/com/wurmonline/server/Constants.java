/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
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
/*     */ public final class Constants
/*     */ {
/*  45 */   public static String dbHost = "localhost";
/*  46 */   public static String dbPort = ":3306";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean DEFAULT_DB_AUTO_MIGRATE = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean dbAutoMigrate = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean enabledMounts = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean loadNpcs = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean loadEndGameItems = true;
/*     */ 
/*     */   
/*     */   public static boolean enableSpyPrevention = true;
/*     */ 
/*     */   
/*     */   public static boolean enableAutoNetworking = true;
/*     */ 
/*     */   
/*     */   public static boolean analyseAllDbTables = false;
/*     */ 
/*     */   
/*     */   public static boolean checkAllDbTables = false;
/*     */ 
/*     */   
/*     */   public static boolean optimiseAllDbTables = false;
/*     */ 
/*     */   
/*     */   public static boolean useSplitCreaturesTable = false;
/*     */ 
/*     */   
/*     */   public static boolean createTemporaryDatabaseIndicesAtStartup = true;
/*     */ 
/*     */   
/*     */   public static boolean dropTemporaryDatabaseIndicesAtStartup = true;
/*     */ 
/*     */   
/*     */   public static boolean usePrepStmts = false;
/*     */ 
/*     */   
/*     */   public static boolean gatherDbStats = false;
/*     */ 
/*     */   
/*     */   public static boolean checkWurmLogs = false;
/*     */ 
/*     */   
/*     */   public static boolean startChallenge = false;
/*     */ 
/*     */   
/* 105 */   public static int challengeDays = 30;
/*     */ 
/*     */   
/*     */   private static final boolean DEFAULT_IS_GAME_SERVER = true;
/*     */ 
/*     */   
/*     */   public static boolean isGameServer = true;
/*     */ 
/*     */   
/*     */   public static boolean isEigcEnabled = false;
/*     */   
/* 116 */   public static String dbUser = "";
/* 117 */   public static String dbPass = "";
/* 118 */   public static String dbDriver = "com.mysql.jdbc.Driver";
/* 119 */   public static String webPath = ".";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean useDb = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean usePooledDb = true;
/*     */ 
/*     */   
/*     */   public static boolean trackOpenDatabaseResources = false;
/*     */ 
/*     */   
/*     */   public static boolean enablePnpPortForward = true;
/*     */ 
/*     */   
/* 136 */   private static Logger logger = Logger.getLogger(Constants.class.getName());
/*     */   
/* 138 */   private static Properties props = null;
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
/* 152 */   public static int numberOfDirtyMeshRowsToSaveEachCall = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useDirectByteBuffersForMeshIO = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public static int meshSize;
/*     */ 
/*     */   
/*     */   public static boolean createSeeds = false;
/*     */ 
/*     */   
/*     */   public static boolean devmode = false;
/*     */ 
/*     */   
/* 169 */   public static String motd = "Wurm has been waiting for you.";
/* 170 */   public static String skillTemplatesDBPath = "templates" + File.separator + "skills" + File.separator;
/* 171 */   public static String zonesDBPath = "zones" + File.separator;
/* 172 */   public static String itemTemplatesDBPath = "templates" + File.separator + "items" + File.separator;
/* 173 */   public static String creatureTemplatesDBPath = "templates" + File.separator + "creatures" + File.separator;
/* 174 */   public static String creatureStatsDBPath = "creatures" + File.separator;
/* 175 */   public static String playerStatsDBPath = "players" + File.separator;
/* 176 */   public static String itemStatsDBPath = "items" + File.separator;
/* 177 */   public static String tileStatsDBPath = "tiles" + File.separator;
/* 178 */   public static String itemOldStatsDBPath = "olditems" + File.separator;
/* 179 */   public static String creatureOldStatsDBPath = "deadCreatures" + File.separator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean RUNBATCH = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean maintaining = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useQueueToSendDataToPlayers = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useMultiThreadedBankPolling = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToCountEggs = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToSaveConstants = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToSaveDirtyMeshRows = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToSendTimeSync = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToSwitchFatigue = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToTickCalendar = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToWriteLogs = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorForServer = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   public static int scheduledExecutorServiceThreads = 1;
/* 254 */   public static String playerStatLog = "numplayers.log";
/* 255 */   public static String logonStatLog = "numlogons.log";
/* 256 */   public static String ipStatLog = "numips.log";
/* 257 */   public static String tutorialLog = "tutorial.log";
/* 258 */   public static String newbieStatLog = "newbies.log";
/* 259 */   public static String totIpStatLog = "totalips.log";
/* 260 */   public static String payingLog = "paying.log";
/* 261 */   public static String subscriptionLog = "subscriptions.log";
/* 262 */   public static String moneyLog = "mrtgmoney.log";
/* 263 */   public static String economyLog = "economy.log";
/*     */   
/* 265 */   public static String expiryLog = "expiry.log";
/* 266 */   public static String lagLog = "lag.log";
/*     */   
/* 268 */   public static String retentionStatLog = "retention.log";
/* 269 */   public static String retentionPercentStatLog = "retentionpercent.log";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useItemTransferLog = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useTileEventLog = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useDatabaseForServerStatisticsLog = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToUpdateCreaturePositionInDatabase = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 297 */   public static int numberOfDbCreaturePositionsToUpdateEachTime = 500;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToUpdatePlayerPositionInDatabase = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 308 */   public static int numberOfDbPlayerPositionsToUpdateEachTime = 500;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToUpdateItemDamageInDatabase = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 319 */   public static int numberOfDbItemDamagesToUpdateEachTime = 500;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToUpdateItemOwnerInDatabase = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToUpdateItemLastOwnerInDatabase = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToUpdateItemParentInDatabase = true;
/*     */ 
/*     */   
/* 334 */   public static int numberOfDbItemOwnersToUpdateEachTime = 500;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorToConnectToTwitter = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useScheduledExecutorForTrello = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 349 */   public static long lagThreshold = 1000L;
/*     */   
/*     */   static boolean crashed = true;
/*     */   
/*     */   public static boolean respawnUniques = false;
/*     */   
/*     */   public static boolean pruneDb = true;
/*     */   public static boolean reprospect = false;
/*     */   public static boolean caveImg = false;
/* 358 */   public static long minMillisBetweenPlayerConns = 1000L;
/*     */ 
/*     */   
/* 361 */   public static String trelloBoardid = "";
/* 362 */   public static String trelloApiKey = "";
/* 363 */   public static String trelloToken = null;
/* 364 */   public static String trelloMVBoardId = "";
/*     */   
/*     */   private static final boolean DEFAULT_USE_INCOMING_RMI = false;
/*     */   
/*     */   public static boolean useIncomingRMI = false;
/*     */   
/*     */   public static boolean isNewbieFriendly = false;
/*     */ 
/*     */   
/*     */   static {
/* 374 */     load();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void load() {
/* 379 */     props = new Properties();
/* 380 */     File file = null;
/*     */     
/*     */     try {
/* 383 */       file = new File(ServerDirInfo.getConstantsFileName());
/* 384 */       logger.info("Loading configuration file at " + file.getAbsolutePath());
/* 385 */       FileInputStream fis = new FileInputStream(file);
/* 386 */       props.load(fis);
/* 387 */       fis.close();
/*     */     }
/* 389 */     catch (FileNotFoundException ex) {
/*     */       
/* 391 */       logger.log(Level.SEVERE, "Failed to locate wurm initializer file at " + file.getAbsolutePath());
/*     */       
/*     */       try {
/* 394 */         save();
/*     */       }
/* 396 */       catch (Exception fex) {
/*     */         
/* 398 */         logger.log(Level.SEVERE, "Failed to create wurm initializer file at " + file.getAbsolutePath(), fex);
/*     */       }
/*     */     
/* 401 */     } catch (IOException ex) {
/*     */       
/* 403 */       logger.log(Level.SEVERE, "Failed to load properties at " + file.getAbsolutePath());
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 408 */       motd = props.getProperty("MOTD");
/*     */       
/* 410 */       File dbdir = null;
/* 411 */       dbHost = props.getProperty("DB_HOST");
/* 412 */       dbUser = props.getProperty("DB_USER");
/* 413 */       dbPass = props.getProperty("DB_PASS");
/* 414 */       dbDriver = props.getProperty("DB_DRIVER");
/* 415 */       webPath = props.getProperty("WEB_PATH");
/*     */ 
/*     */ 
/*     */       
/* 419 */       usePooledDb = getBoolean("USE_POOLED_DB", false);
/* 420 */       trackOpenDatabaseResources = getBoolean("TRACK_OPEN_DATABASE_RESOURCES", false);
/* 421 */       createSeeds = getBoolean("CREATESEEDS", false);
/* 422 */       if (props.getProperty("DB_PORT") != null && props.getProperty("DB_PORT").length() > 0) {
/* 423 */         dbPort = ":" + props.getProperty("DB_PORT");
/*     */       }
/* 425 */       dbAutoMigrate = getBoolean("DB_AUTO_MIGRATE", true);
/*     */ 
/*     */       
/*     */       try {
/* 429 */         numberOfDirtyMeshRowsToSaveEachCall = Integer.parseInt(props
/* 430 */             .getProperty("NUMBER_OF_DIRTY_MESH_ROWS_TO_SAVE_EACH_CALL"));
/* 431 */         useDirectByteBuffersForMeshIO = getBoolean("USE_DIRECT_BYTE_BUFFERS_FOR_MESHIO", false);
/*     */       }
/* 433 */       catch (Exception ex) {
/*     */         
/* 435 */         numberOfDirtyMeshRowsToSaveEachCall = 10;
/* 436 */         useDirectByteBuffersForMeshIO = false;
/*     */       } 
/* 438 */       File worldMachineOutput = new File(ServerDirInfo.getFileDBPath() + "top_layer.map");
/*     */ 
/*     */       
/* 441 */       long baseFileSize = worldMachineOutput.length();
/*     */       
/* 443 */       int mapDimension = (int)Math.sqrt(baseFileSize) / 2;
/*     */       
/* 445 */       meshSize = (int)(Math.log(mapDimension) / Math.log(2.0D));
/*     */ 
/*     */       
/* 448 */       System.out.println("Meshsize=" + meshSize);
/*     */       
/* 450 */       devmode = getBoolean("DEVMODE", false);
/* 451 */       crashed = getBoolean("CRASHED", false);
/*     */ 
/*     */       
/* 454 */       RUNBATCH = getBoolean("RUNBATCH", false);
/* 455 */       maintaining = getBoolean("MAINTAINING", false);
/*     */ 
/*     */ 
/*     */       
/* 459 */       checkWurmLogs = getBoolean("CHECK_WURMLOGS", false);
/*     */       
/*     */       try {
/* 462 */         startChallenge = getBoolean("STARTCHALLENGE", false);
/* 463 */         challengeDays = Integer.parseInt(props.getProperty("CHALLENGEDAYS"));
/*     */       }
/* 465 */       catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */       
/* 469 */       isGameServer = getBoolean("IS_GAME_SERVER", true);
/*     */       
/* 471 */       lagThreshold = getLong("LAG_THRESHOLD", 1000L);
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
/* 482 */       useSplitCreaturesTable = getBoolean("USE_SPLIT_CREATURES_TABLE", false);
/* 483 */       analyseAllDbTables = getBoolean("ANALYSE_ALL_DB_TABLES", false);
/* 484 */       checkAllDbTables = getBoolean("CHECK_ALL_DB_TABLES", false);
/* 485 */       optimiseAllDbTables = getBoolean("OPTIMISE_ALL_DB_TABLES", false);
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
/* 496 */       usePrepStmts = getBoolean("PREPSTATEMENTS", false);
/* 497 */       gatherDbStats = getBoolean("DBSTATS", false);
/* 498 */       pruneDb = getBoolean("PRUNEDB", false);
/*     */       
/* 500 */       reprospect = getBoolean("PROSPECT", false);
/* 501 */       props.put("PROSPECT", String.valueOf(false));
/*     */       
/* 503 */       caveImg = getBoolean("CAVEIMG", false);
/* 504 */       props.put("CAVEIMG", String.valueOf(false));
/*     */ 
/*     */       
/*     */       try {
/* 508 */         respawnUniques = getBoolean("RESPAWN", false);
/* 509 */         props.put("RESPAWN", String.valueOf(false));
/*     */       }
/* 511 */       catch (Exception ex) {
/*     */         
/* 513 */         logger.log(Level.WARNING, "Not respawning uniques");
/*     */       } 
/*     */       
/* 516 */       loadNpcs = getBoolean("NPCS", true);
/* 517 */       minMillisBetweenPlayerConns = getLong("PLAYER_CONN_MILLIS", 1000L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 527 */       useQueueToSendDataToPlayers = getBoolean("USE_QUEUE_TO_SEND_DATA_TO_PLAYERS", false);
/* 528 */       useMultiThreadedBankPolling = getBoolean("USE_MULTI_THREADED_BANK_POLLING", false);
/* 529 */       useScheduledExecutorToCountEggs = getBoolean("USE_SCHEDULED_EXECUTOR_TO_COUNT_EGGS", false);
/*     */ 
/*     */ 
/*     */       
/* 533 */       useScheduledExecutorToSaveDirtyMeshRows = getBoolean("USE_SCHEDULED_EXECUTOR_TO_SAVE_DIRTY_MESH_ROWS", false);
/* 534 */       useScheduledExecutorToSendTimeSync = getBoolean("USE_SCHEDULED_EXECUTOR_TO_SEND_TIME_SYNC", false);
/* 535 */       useScheduledExecutorToSwitchFatigue = getBoolean("USE_SCHEDULED_EXECUTOR_TO_SWITCH_FATIGUE", false);
/* 536 */       useScheduledExecutorToTickCalendar = getBoolean("USE_SCHEDULED_EXECUTOR_TO_TICK_CALENDAR", false);
/* 537 */       useScheduledExecutorToWriteLogs = getBoolean("USE_SCHEDULED_EXECUTOR", false);
/* 538 */       useScheduledExecutorForServer = getBoolean("USE_SCHEDULED_EXECUTOR_FOR_SERVER", false);
/*     */       
/* 540 */       useScheduledExecutorForTrello = getBoolean("USE_SCHEDULED_EXECUTOR_FOR_TRELLO", false);
/*     */       
/* 542 */       scheduledExecutorServiceThreads = getInt("SCHEDULED_EXECUTOR_SERVICE_NUMBER_OF_THREADS", scheduledExecutorServiceThreads);
/* 543 */       useItemTransferLog = getBoolean("USE_ITEM_TRANSFER_LOG", false);
/*     */       
/* 545 */       useTileEventLog = getBoolean("USE_TILE_LOG", false);
/*     */       
/* 547 */       useDatabaseForServerStatisticsLog = getBoolean("USE_DATABASE_FOR_SERVER_STATISTICS_LOG", false);
/*     */       
/* 549 */       useScheduledExecutorToUpdateCreaturePositionInDatabase = getBoolean("USE_SCHEDULED_EXECUTOR_TO_UPDATE_CREATURE_POSITION_IN_DATABASE", false);
/*     */       
/* 551 */       useScheduledExecutorToUpdatePlayerPositionInDatabase = getBoolean("USE_SCHEDULED_EXECUTOR_TO_UPDATE_PLAYER_POSITION_IN_DATABASE", false);
/*     */       
/* 553 */       useScheduledExecutorToUpdateItemDamageInDatabase = getBoolean("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_DAMAGE_IN_DATABASE", false);
/*     */       
/* 555 */       useScheduledExecutorToUpdateItemOwnerInDatabase = getBoolean("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_OWNER_IN_DATABASE", true);
/*     */       
/* 557 */       useScheduledExecutorToUpdateItemLastOwnerInDatabase = getBoolean("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_LASTOWNER_IN_DATABASE", true);
/*     */       
/* 559 */       useScheduledExecutorToUpdateItemParentInDatabase = getBoolean("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_PARENT_IN_DATABASE", true);
/*     */       
/* 561 */       numberOfDbCreaturePositionsToUpdateEachTime = getInt("NUMBER_OF_DB_CREATURE_POSITIONS_TO_UPDATE_EACH_TIME", numberOfDbCreaturePositionsToUpdateEachTime);
/*     */ 
/*     */       
/* 564 */       numberOfDbPlayerPositionsToUpdateEachTime = getInt("NUMBER_OF_DB_PLAYER_POSITIONS_TO_UPDATE_EACH_TIME", numberOfDbPlayerPositionsToUpdateEachTime);
/*     */       
/* 566 */       numberOfDbItemDamagesToUpdateEachTime = getInt("NUMBER_OF_DB_ITEM_DAMAGES_TO_UPDATE_EACH_TIME", numberOfDbItemDamagesToUpdateEachTime);
/*     */       
/* 568 */       numberOfDbItemOwnersToUpdateEachTime = getInt("NUMBER_OF_DB_ITEM_OWNERS_TO_UPDATE_EACH_TIME", numberOfDbItemOwnersToUpdateEachTime);
/*     */ 
/*     */       
/* 571 */       trelloBoardid = props.getProperty("TRELLO_BOARD_ID", "");
/*     */       
/* 573 */       trelloMVBoardId = props.getProperty("TRELLO_MUTE_VOTE_BOARD_ID", "");
/* 574 */       trelloApiKey = props.getProperty("TRELLO_APIKEY", "");
/* 575 */       trelloToken = props.getProperty("TRELLO_TOKEN");
/*     */       
/* 577 */       enableAutoNetworking = getBoolean("AUTO_NETWORKING", true);
/* 578 */       enablePnpPortForward = getBoolean("ENABLE_PNP_PORT_FORWARD", true);
/*     */       
/* 580 */       useIncomingRMI = getBoolean("USE_INCOMING_RMI", false);
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
/* 591 */       String logfile = props.getProperty("PLAYERLOG");
/* 592 */       if (logfile != null && logfile.length() > 0) {
/*     */         
/* 594 */         if (logfile.endsWith(".log")) {
/* 595 */           playerStatLog = logfile;
/*     */         } else {
/* 597 */           logger.log(Level.WARNING, "PLAYERLOG file does not end with '.log'. Using default: " + playerStatLog);
/*     */         } 
/*     */       } else {
/* 600 */         logger.log(Level.WARNING, "PLAYERLOG not specified. Using default: " + playerStatLog);
/*     */       } 
/* 602 */     } catch (Exception ex) {
/*     */       
/* 604 */       logger.log(Level.WARNING, "Failed to load property.", ex);
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
/*     */   private static boolean getBoolean(String key, boolean defaultValue) {
/* 616 */     String maybeBoolean = props.getProperty(key);
/* 617 */     return (maybeBoolean == null) ? defaultValue : Boolean.parseBoolean(maybeBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getInt(String key, int defaultValue) {
/* 628 */     String maybeInt = props.getProperty(key);
/* 629 */     if (maybeInt == null) {
/* 630 */       System.out.println(key + " - " + maybeInt);
/* 631 */       return defaultValue;
/*     */     } 
/*     */     try {
/* 634 */       return Integer.parseInt(maybeInt);
/* 635 */     } catch (NumberFormatException e) {
/* 636 */       System.out.println(key + " - " + maybeInt);
/* 637 */       return defaultValue;
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
/*     */   private static long getLong(String key, long defaultValue) {
/* 649 */     String maybeLong = props.getProperty(key);
/* 650 */     if (maybeLong == null) {
/* 651 */       return defaultValue;
/*     */     }
/*     */     try {
/* 654 */       return Long.parseLong(maybeLong);
/* 655 */     } catch (NumberFormatException e) {
/* 656 */       System.out.println(key + " - " + maybeLong);
/* 657 */       return defaultValue;
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
/*     */   public static int getMeshSize() {
/* 673 */     return meshSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void save() {
/* 682 */     File file = new File(ServerDirInfo.getConstantsFileName());
/* 683 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 685 */       logger.finer("Saving wurm initializer file at " + file.getAbsolutePath());
/*     */     }
/*     */     
/*     */     try {
/* 689 */       if (!ServerDirInfo.getFileDBPath().endsWith(File.separator)) {
/* 690 */         ServerDirInfo.setFileDBPath(ServerDirInfo.getFileDBPath() + File.separator);
/*     */       }
/* 692 */       props.put("DBPATH", ServerDirInfo.getFileDBPath());
/* 693 */       props.put("MOTD", motd);
/*     */       
/* 695 */       props.put("CHECK_WURMLOGS", String.valueOf(checkWurmLogs));
/* 696 */       props.put("LAG_THRESHOLD", String.valueOf(lagThreshold));
/*     */       
/* 698 */       props.put("DB_HOST", dbHost);
/* 699 */       props.put("DB_USER", dbUser);
/* 700 */       props.put("DB_PASS", dbPass);
/* 701 */       props.put("DB_DRIVER", dbDriver);
/* 702 */       props.put("USEDB", String.valueOf(true));
/* 703 */       props.put("USE_POOLED_DB", String.valueOf(usePooledDb));
/* 704 */       props.put("TRACK_OPEN_DATABASE_RESOURCES", String.valueOf(trackOpenDatabaseResources));
/* 705 */       props.put("NUMBER_OF_DIRTY_MESH_ROWS_TO_SAVE_EACH_CALL", Integer.toString(numberOfDirtyMeshRowsToSaveEachCall));
/* 706 */       props.put("USE_DIRECT_BYTE_BUFFERS_FOR_MESHIO", Boolean.toString(useDirectByteBuffersForMeshIO));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 712 */       props.put("MAINTAINING", String.valueOf(false));
/* 713 */       props.put("RUNBATCH", String.valueOf(false));
/* 714 */       props.put("PROSPECT", String.valueOf(false));
/* 715 */       props.put("CAVEIMG", String.valueOf(false));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 722 */       props.put("USE_QUEUE_TO_SEND_DATA_TO_PLAYERS", Boolean.toString(useQueueToSendDataToPlayers));
/* 723 */       props.put("USE_MULTI_THREADED_BANK_POLLING", Boolean.toString(useMultiThreadedBankPolling));
/* 724 */       props.put("USE_SCHEDULED_EXECUTOR_TO_COUNT_EGGS", Boolean.toString(useScheduledExecutorToCountEggs));
/*     */       
/* 726 */       props.put("USE_SCHEDULED_EXECUTOR_TO_SAVE_DIRTY_MESH_ROWS", 
/* 727 */           Boolean.toString(useScheduledExecutorToSaveDirtyMeshRows));
/* 728 */       props.put("USE_SCHEDULED_EXECUTOR_TO_SEND_TIME_SYNC", Boolean.toString(useScheduledExecutorToSendTimeSync));
/* 729 */       props.put("USE_SCHEDULED_EXECUTOR_TO_SWITCH_FATIGUE", Boolean.toString(useScheduledExecutorToSwitchFatigue));
/* 730 */       props.put("USE_SCHEDULED_EXECUTOR_TO_TICK_CALENDAR", Boolean.toString(useScheduledExecutorToTickCalendar));
/* 731 */       props.put("USE_SCHEDULED_EXECUTOR", Boolean.toString(useScheduledExecutorToWriteLogs));
/* 732 */       props.put("USE_SCHEDULED_EXECUTOR_FOR_SERVER", Boolean.toString(useScheduledExecutorForServer));
/* 733 */       props.put("USE_SCHEDULED_EXECUTOR_FOR_TRELLO", Boolean.toString(useScheduledExecutorForTrello));
/* 734 */       props.put("SCHEDULED_EXECUTOR_SERVICE_NUMBER_OF_THREADS", Integer.toString(scheduledExecutorServiceThreads));
/* 735 */       props.put("PLAYERLOG", playerStatLog);
/* 736 */       props.put("USE_ITEM_TRANSFER_LOG", Boolean.toString(useItemTransferLog));
/* 737 */       props.put("USE_TILE_LOG", Boolean.toString(useTileEventLog));
/* 738 */       props.put("USE_DATABASE_FOR_SERVER_STATISTICS_LOG", Boolean.toString(useDatabaseForServerStatisticsLog));
/* 739 */       props.put("USE_SCHEDULED_EXECUTOR_TO_UPDATE_CREATURE_POSITION_IN_DATABASE", 
/* 740 */           Boolean.toString(useScheduledExecutorToUpdateCreaturePositionInDatabase));
/* 741 */       props.put("USE_SCHEDULED_EXECUTOR_TO_UPDATE_PLAYER_POSITION_IN_DATABASE", 
/* 742 */           Boolean.toString(useScheduledExecutorToUpdatePlayerPositionInDatabase));
/* 743 */       props.put("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_DAMAGE_IN_DATABASE", 
/* 744 */           Boolean.toString(useScheduledExecutorToUpdateItemDamageInDatabase));
/* 745 */       props.put("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_OWNER_IN_DATABASE", 
/* 746 */           Boolean.toString(useScheduledExecutorToUpdateItemOwnerInDatabase));
/* 747 */       props.put("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_LASTOWNER_IN_DATABASE", 
/* 748 */           Boolean.toString(useScheduledExecutorToUpdateItemLastOwnerInDatabase));
/* 749 */       props.put("USE_SCHEDULED_EXECUTOR_TO_UPDATE_ITEM_Parent_IN_DATABASE", 
/* 750 */           Boolean.toString(useScheduledExecutorToUpdateItemParentInDatabase));
/* 751 */       props.put("NUMBER_OF_DB_CREATURE_POSITIONS_TO_UPDATE_EACH_TIME", 
/* 752 */           Integer.toString(numberOfDbCreaturePositionsToUpdateEachTime));
/* 753 */       props.put("NUMBER_OF_DB_PLAYER_POSITIONS_TO_UPDATE_EACH_TIME", 
/* 754 */           Integer.toString(numberOfDbPlayerPositionsToUpdateEachTime));
/* 755 */       props.put("NUMBER_OF_DB_ITEM_DAMAGES_TO_UPDATE_EACH_TIME", Integer.toString(numberOfDbItemDamagesToUpdateEachTime));
/* 756 */       props.put("NUMBER_OF_DB_ITEM_OWNERS_TO_UPDATE_EACH_TIME", Integer.toString(numberOfDbItemOwnersToUpdateEachTime));
/* 757 */       props.put("USE_SCHEDULED_EXECUTOR_TO_UPDATE_TWITTER", Boolean.toString(useScheduledExecutorToConnectToTwitter));
/* 758 */       props.put("WEB_PATH", webPath);
/* 759 */       props.put("CREATESEEDS", "false");
/* 760 */       props.put("DEVMODE", String.valueOf(devmode));
/* 761 */       props.put("CRASHED", String.valueOf(crashed));
/*     */ 
/*     */       
/* 764 */       props.put("PRUNEDB", String.valueOf(pruneDb));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 772 */       props.put("PLAYER_CONN_MILLIS", String.valueOf(minMillisBetweenPlayerConns));
/*     */       
/* 774 */       props.put("USE_SPLIT_CREATURES_TABLE", Boolean.toString(useSplitCreaturesTable));
/* 775 */       props.put("ANALYSE_ALL_DB_TABLES", Boolean.toString(analyseAllDbTables));
/* 776 */       props.put("CHECK_ALL_DB_TABLES", Boolean.toString(checkAllDbTables));
/* 777 */       props.put("OPTIMISE_ALL_DB_TABLES", Boolean.toString(optimiseAllDbTables));
/* 778 */       props.put("CREATE_TEMPORARY_DATABASE_INDICES_AT_STARTUP", Boolean.toString(createTemporaryDatabaseIndicesAtStartup));
/* 779 */       props.put("DROP_TEMPORARY_DATABASE_INDICES_AT_STARTUP", Boolean.toString(dropTemporaryDatabaseIndicesAtStartup));
/* 780 */       props.put("PREPSTATEMENTS", String.valueOf(usePrepStmts));
/* 781 */       props.put("DBSTATS", String.valueOf(gatherDbStats));
/* 782 */       props.put("MOUNTS", String.valueOf(enabledMounts));
/*     */ 
/*     */ 
/*     */       
/* 786 */       props.put("DB_PORT", String.valueOf(dbPort.replace(":", "")));
/*     */       
/* 788 */       props.put("TRELLO_BOARD_ID", trelloBoardid);
/* 789 */       props.put("TRELLO_MUTE_VOTE_BOARD_ID", trelloMVBoardId);
/* 790 */       props.put("TRELLO_APIKEY", trelloApiKey);
/* 791 */       props.put("NPCS", Boolean.toString(loadNpcs));
/* 792 */       if (trelloToken != null) {
/* 793 */         props.put("TRELLO_TOKEN", trelloToken);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 809 */     catch (Exception fex) {
/*     */       
/* 811 */       logger.log(Level.SEVERE, "Failed to create wurm initializer file at " + file.getAbsolutePath(), fex);
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
/*     */   static void logConstantValues(boolean aWithPasswords) {
/* 827 */     logger.info("motd: " + motd);
/* 828 */     logger.info("");
/*     */     
/* 830 */     logger.info("fileName: " + ServerDirInfo.getConstantsFileName());
/* 831 */     logger.info("");
/*     */ 
/*     */     
/* 834 */     logger.info("Check WURMLOGS: " + checkWurmLogs);
/* 835 */     logger.info("isGameServer: " + isGameServer);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 840 */     logger.info("devmode: " + devmode);
/* 841 */     logger.info("maintaining: " + maintaining);
/* 842 */     logger.info("crashed: " + crashed);
/* 843 */     logger.info("RUNBATCH: " + RUNBATCH);
/* 844 */     logger.info("pruneDb: " + pruneDb);
/* 845 */     logger.info("reprospect: " + reprospect);
/* 846 */     logger.info("caveImg: " + caveImg);
/* 847 */     logger.info("createSeeds: " + createSeeds);
/*     */ 
/*     */ 
/*     */     
/* 851 */     logger.info("Min millis between player connections: " + minMillisBetweenPlayerConns);
/* 852 */     logger.info("");
/*     */     
/* 854 */     logger.info("fileDBPath: " + ServerDirInfo.getFileDBPath());
/* 855 */     logger.info("dbHost: " + dbHost);
/* 856 */     logger.info("useSplitCreaturesTable: " + useSplitCreaturesTable);
/* 857 */     logger.info("analyseAllDbTables: " + analyseAllDbTables);
/* 858 */     logger.info("checkAllDbTables: " + checkAllDbTables);
/* 859 */     logger.info("optimiseAllDbTables: " + optimiseAllDbTables);
/* 860 */     logger.info("createTemporaryDatabaseIndicesAtStartup: " + createTemporaryDatabaseIndicesAtStartup);
/* 861 */     logger.info("dropTemporaryDatabaseIndicesAtStartup: " + dropTemporaryDatabaseIndicesAtStartup);
/* 862 */     logger.info("usePrepStmts: " + usePrepStmts);
/* 863 */     logger.info("gatherDbStats: " + gatherDbStats);
/* 864 */     logger.info("");
/*     */     
/* 866 */     logger.info("");
/*     */     
/* 868 */     logger.info("");
/*     */     
/* 870 */     logger.info("dbUser: " + dbUser);
/* 871 */     if (aWithPasswords)
/* 872 */       logger.info("dbPass: " + dbPass); 
/* 873 */     logger.info("dbDriver: " + dbDriver);
/* 874 */     logger.info("useDb: true");
/* 875 */     logger.info("usePooledDb: " + usePooledDb);
/* 876 */     logger.info("dbPort: " + dbPort);
/* 877 */     logger.info("trackOpenDatabaseResources: " + trackOpenDatabaseResources);
/* 878 */     logger.info("");
/*     */     
/* 880 */     logger.info("numberOfDirtyMeshRowsToSaveEachCall: " + numberOfDirtyMeshRowsToSaveEachCall);
/* 881 */     logger.info("useDirectByteBuffersForMeshIO: " + useDirectByteBuffersForMeshIO);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 889 */     logger.info("");
/*     */     
/* 891 */     logger.info("webPath: " + webPath);
/*     */     
/* 893 */     logger.info("skillTemplatesDBPath: " + skillTemplatesDBPath);
/* 894 */     logger.info("zonesDBPath: " + zonesDBPath);
/* 895 */     logger.info("itemTemplatesDBPath: " + itemTemplatesDBPath);
/* 896 */     logger.info("creatureStatsDBPath: " + creatureStatsDBPath);
/* 897 */     logger.info("playerStatsDBPath: " + playerStatsDBPath);
/* 898 */     logger.info("itemStatsDBPath: " + itemStatsDBPath);
/* 899 */     logger.info("tileStatsDBPath: " + tileStatsDBPath);
/* 900 */     logger.info("itemOldStatsDBPath: " + itemOldStatsDBPath);
/* 901 */     logger.info("creatureOldStatsDBPath: " + creatureOldStatsDBPath);
/*     */     
/* 903 */     logger.info("useQueueToSendDataToPlayers: " + useQueueToSendDataToPlayers);
/* 904 */     logger.info("useMultiThreadedBankPolling: " + useMultiThreadedBankPolling);
/* 905 */     logger.info("useScheduledExecutorToCountEggs: " + useScheduledExecutorToCountEggs);
/* 906 */     logger.info("useScheduledExecutorToSaveConstants: " + useScheduledExecutorToSaveConstants);
/* 907 */     logger.info("useScheduledExecutorToSaveDirtyMeshRows: " + useScheduledExecutorToSaveDirtyMeshRows);
/* 908 */     logger.info("useScheduledExecutorToSendTimeSync: " + useScheduledExecutorToSendTimeSync);
/* 909 */     logger.info("useScheduledExecutorToSwitchFatigue: " + useScheduledExecutorToSwitchFatigue);
/* 910 */     logger.info("useScheduledExecutorToTickCalendar: " + useScheduledExecutorToTickCalendar);
/* 911 */     logger.info("useScheduledExecutorToWriteLogs: " + useScheduledExecutorToWriteLogs);
/* 912 */     logger.info("useScheduledExecutorForServer: " + useScheduledExecutorForServer);
/* 913 */     logger.info("scheduledExecutorServiceThreads: " + scheduledExecutorServiceThreads);
/* 914 */     logger.info("useItemTransferLog: " + useItemTransferLog);
/* 915 */     logger.info("useTileEventLog: " + useTileEventLog);
/* 916 */     logger.info("useDatabaseForServerStatisticsLog: " + useDatabaseForServerStatisticsLog);
/* 917 */     logger.info("useScheduledExecutorToUpdateCreaturePositionInDatabase: " + useScheduledExecutorToUpdateCreaturePositionInDatabase);
/*     */     
/* 919 */     logger.info("useScheduledExecutorToUpdatePlayerPositionInDatabase: " + useScheduledExecutorToUpdatePlayerPositionInDatabase);
/*     */     
/* 921 */     logger.info("useScheduledExecutorToUpdateItemDamageInDatabase: " + useScheduledExecutorToUpdateItemDamageInDatabase);
/* 922 */     logger.info("useScheduledExecutorToUpdateItemOwnerInDatabase: " + useScheduledExecutorToUpdateItemOwnerInDatabase);
/* 923 */     logger.info("useScheduledExecutorToUpdateItemLastOwnerInDatabase: " + useScheduledExecutorToUpdateItemLastOwnerInDatabase);
/* 924 */     logger.info("useScheduledExecutorToUpdateItemParentInDatabase: " + useScheduledExecutorToUpdateItemParentInDatabase);
/* 925 */     logger.info("useScheduledExecutorToConnectToTwitter: " + useScheduledExecutorToConnectToTwitter);
/* 926 */     logger.info("numberOfDbCreaturePositionsToUpdateEachTime: " + numberOfDbCreaturePositionsToUpdateEachTime);
/* 927 */     logger.info("numberOfDbPlayerPositionsToUpdateEachTime: " + numberOfDbPlayerPositionsToUpdateEachTime);
/* 928 */     logger.info("numberOfDbItemDamagesToUpdateEachTime: " + numberOfDbItemDamagesToUpdateEachTime);
/* 929 */     logger.info("numberOfDbItemOwnersToUpdateEachTime: " + numberOfDbItemOwnersToUpdateEachTime);
/* 930 */     logger.info("playerStatLog: " + playerStatLog);
/* 931 */     logger.info("logonStatLog: " + logonStatLog);
/* 932 */     logger.info("ipStatLog: " + ipStatLog);
/* 933 */     logger.info("newbieStatLog: " + newbieStatLog);
/* 934 */     logger.info("totIpStatLog: " + totIpStatLog);
/* 935 */     logger.info("payingLog: " + payingLog);
/* 936 */     logger.info("moneyLog: " + moneyLog);
/* 937 */     logger.info("lagLog: " + lagLog);
/* 938 */     logger.info("lagThreshold: " + lagThreshold);
/* 939 */     logger.info("Eigc enabled " + isEigcEnabled);
/*     */ 
/*     */     
/* 942 */     logger.info("useIncomingRMI: " + useIncomingRMI);
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
/*     */   static final class ConstantsSaver
/*     */     implements Runnable
/*     */   {
/*     */     public void run() {
/* 961 */       if (Constants.logger.isLoggable(Level.FINER))
/*     */       {
/* 963 */         Constants.logger.finer("Running newSingleThreadScheduledExecutor for saving Constants to wurm.ini");
/*     */       }
/*     */       
/*     */       try {
/* 967 */         long now = System.nanoTime();
/*     */         
/* 969 */         Constants.save();
/*     */         
/* 971 */         float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 972 */         if (lElapsedTime > (float)Constants.lagThreshold)
/*     */         {
/* 974 */           Constants.logger.info("Finished saving Constants to wurm.ini, which took " + lElapsedTime + " millis.");
/*     */         
/*     */         }
/*     */       }
/* 978 */       catch (RuntimeException e) {
/*     */         
/* 980 */         Constants.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorService while calling Constants.save()", e);
/* 981 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Constants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
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
/*     */ final class ServerStatistics
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(ServerStatistics.class.getName());
/*     */ 
/*     */   
/*     */   private static final String INSERT_SERVER_STATISTIC_INT = "insert into SERVER_STATS_LOG ( SERVER_ID, SERVER_NAME, STATISTIC_ID, VALUE_INT ) VALUES(?,?,?,?)";
/*     */   
/*  43 */   private static final int SERVER_ID = Servers.getLocalServerId();
/*     */   
/*  45 */   private static final String SERVER_NAME = Servers.getLocalServerName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_PREMIUM_PLAYERS = "numberOfPremiumPlayers";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_PLAYERS = "numberOfPlayers";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_CLIENT_IPS = "numberOfClientIps";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_PLAYERS_THROUGH_TUTORIAL = "playersThroughTutorial";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_PREMIUM_PLAYER_LOGONS = "logonsPrem";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_PLAYER_LOGONS = "logons";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_SECONDS_LAG = "secondsLag";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_NEW_PLAYERS = "newbies";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATISTIC_TYPE_NUMBER_OF_PREMIUM_PLAYER_REGISTERED = "premiumPlayersRegistered";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeIntegerStatisticsToMrtgFile(String aStatisticType, String aFileName, int aFirstIntegerStatistic, int aSecondIntegerStatistic) {
/*  98 */     File file = null;
/*  99 */     FileOutputStream fos = null;
/*     */     
/*     */     try {
/* 102 */       file = new File(aFileName);
/* 103 */       fos = new FileOutputStream(file);
/* 104 */       fos.write((aFirstIntegerStatistic + "\n").getBytes());
/* 105 */       fos.write((aSecondIntegerStatistic + "\n\nwww.wurmonline.com").getBytes());
/* 106 */       fos.flush();
/*     */     }
/* 108 */     catch (IOException e) {
/*     */       
/* 110 */       logger.log(Level.WARNING, "Problem writing " + aStatisticType + " to file - " + e.getMessage(), e);
/*     */     }
/*     */     finally {
/*     */       
/* 114 */       StreamUtilities.closeOutputStreamIgnoreExceptions(fos);
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
/*     */   private static void storeOneIntegerStatisticInDatabase(String aStatisticType, int aIntegerStatisticValue) {
/* 128 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 130 */       logger.finer("Going to log " + aStatisticType + " to database");
/*     */     }
/* 132 */     Connection logsConnection = null;
/* 133 */     PreparedStatement logsStatement = null;
/*     */     
/*     */     try {
/* 136 */       logsConnection = DbConnector.getLogsDbCon();
/* 137 */       logsStatement = logsConnection.prepareStatement("insert into SERVER_STATS_LOG ( SERVER_ID, SERVER_NAME, STATISTIC_ID, VALUE_INT ) VALUES(?,?,?,?)");
/*     */       
/* 139 */       logsStatement.setInt(1, SERVER_ID);
/* 140 */       logsStatement.setString(2, SERVER_NAME);
/*     */       
/* 142 */       logsStatement.setString(3, aStatisticType);
/* 143 */       logsStatement.setInt(4, aIntegerStatisticValue);
/* 144 */       logsStatement.execute();
/*     */     }
/* 146 */     catch (SQLException sqex) {
/*     */       
/* 148 */       logger.log(Level.WARNING, "Failed to store " + aStatisticType + " - " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 152 */       DbUtilities.closeDatabaseObjects(logsStatement, null);
/* 153 */       DbConnector.returnConnection(logsConnection);
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
/*     */   static void storeNumberOfPlayers(int numberOfPremiumPlayers, int numberOfPlayers) {
/* 167 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 169 */       logger.finer("storeNumberOfPlayers - numberOfPremiumPlayers: " + numberOfPremiumPlayers + ", numberOfPlayers: " + numberOfPlayers);
/*     */     }
/*     */     
/* 172 */     writeIntegerStatisticsToMrtgFile("numberOfPlayers", Constants.playerStatLog, numberOfPremiumPlayers, numberOfPlayers);
/*     */     
/* 174 */     if (Constants.useDatabaseForServerStatisticsLog) {
/*     */       
/* 176 */       storeOneIntegerStatisticInDatabase("numberOfPremiumPlayers", numberOfPremiumPlayers);
/* 177 */       storeOneIntegerStatisticInDatabase("numberOfPlayers", numberOfPlayers);
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
/*     */   static void storeNumberOfClientIps(int numberOfClientIps) {
/* 189 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 191 */       logger.finer("storeNumberOfClientIps - numberOfClientIps: " + numberOfClientIps);
/*     */     }
/*     */     
/* 194 */     writeIntegerStatisticsToMrtgFile("numberOfClientIps", Constants.ipStatLog, numberOfClientIps, numberOfClientIps);
/*     */ 
/*     */     
/* 197 */     if (Constants.useDatabaseForServerStatisticsLog)
/*     */     {
/* 199 */       storeOneIntegerStatisticInDatabase("numberOfClientIps", numberOfClientIps);
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
/*     */   static void storeNumberOfPlayersThroughTutorial(int playersThroughTutorial) {
/* 211 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 213 */       logger.finer("storeNumberOfPlayersThroughTutorial - playersThroughTutorial: " + playersThroughTutorial);
/*     */     }
/*     */     
/* 216 */     writeIntegerStatisticsToMrtgFile("playersThroughTutorial", Constants.tutorialLog, playersThroughTutorial, playersThroughTutorial);
/*     */ 
/*     */     
/* 219 */     if (Constants.useDatabaseForServerStatisticsLog)
/*     */     {
/* 221 */       storeOneIntegerStatisticInDatabase("playersThroughTutorial", playersThroughTutorial);
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
/*     */   static void storeNumberOfPlayerLogons(int logonsPrem, int logons) {
/* 235 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 237 */       logger.finer("storeNumberOfPlayerLogons - logonsPrem: " + logonsPrem + ", logons: " + logons);
/*     */     }
/*     */     
/* 240 */     writeIntegerStatisticsToMrtgFile("logons", Constants.logonStatLog, logonsPrem, logons);
/*     */     
/* 242 */     if (Constants.useDatabaseForServerStatisticsLog) {
/*     */       
/* 244 */       storeOneIntegerStatisticInDatabase("logonsPrem", logonsPrem);
/* 245 */       storeOneIntegerStatisticInDatabase("logons", logons);
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
/*     */   static void storeNumberOfSecondsLag(int secondsLag) {
/* 257 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 259 */       logger.finer("storeNumberOfSecondsLag - secondsLag: " + secondsLag);
/*     */     }
/*     */     
/* 262 */     writeIntegerStatisticsToMrtgFile("secondsLag", Constants.lagLog, secondsLag, secondsLag);
/*     */     
/* 264 */     if (Constants.useDatabaseForServerStatisticsLog)
/*     */     {
/* 266 */       storeOneIntegerStatisticInDatabase("secondsLag", secondsLag);
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
/*     */   static void storeNumberOfNewPlayers(int newbies) {
/* 278 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 280 */       logger.finer("storeNumberOfNewPlayers - newbies: " + newbies);
/*     */     }
/*     */     
/* 283 */     writeIntegerStatisticsToMrtgFile("newbies", Constants.newbieStatLog, newbies, newbies);
/*     */     
/* 285 */     if (Constants.useDatabaseForServerStatisticsLog)
/*     */     {
/* 287 */       storeOneIntegerStatisticInDatabase("newbies", newbies);
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
/*     */   static void storeNumberOfPayingPlayers(int paying, int payingMWithoutnewPremiums) {
/* 304 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 306 */       logger.finer("storeNumberOfPayingPlayers - paying: " + paying + ", payingMWithoutnewPremiums: " + payingMWithoutnewPremiums);
/*     */     }
/*     */     
/* 309 */     writeIntegerStatisticsToMrtgFile("premiumPlayersRegistered", Constants.payingLog, paying, payingMWithoutnewPremiums);
/*     */     
/* 311 */     if (Constants.useDatabaseForServerStatisticsLog)
/*     */     {
/* 313 */       storeOneIntegerStatisticInDatabase("premiumPlayersRegistered", paying);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\ServerStatistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
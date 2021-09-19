/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.steam.SteamHandler;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerProperties
/*     */ {
/*     */   private static final String loadAll = "SELECT * FROM SERVERPROPERTIES";
/*     */   private static final String insert = "INSERT INTO SERVERPROPERTIES(PROPVAL,PROPKEY) VALUES (?,?)";
/*     */   private static final String update = "UPDATE SERVERPROPERTIES SET PROPVAL=? WHERE PROPKEY=?";
/*     */   private static final String createTable = "CREATE TABLE IF NOT EXISTS SERVERPROPERTIES        (            PROPKEY                 VARCHAR(50)   NOT NULL DEFAULT '',            PROPVAL                 VARCHAR(50)   NOT NULL DEFAULT ''        )";
/*  21 */   private static final Properties props = new Properties();
/*     */   
/*     */   public static final String STEAMQUERY = "STEAMQUERYPORT";
/*     */   
/*     */   public static final String NPCS = "NPCS";
/*     */   
/*     */   public static final String ADMIN_PASSWORD = "ADMINPASSWORD";
/*     */   public static final String ENDGAMEITEMS = "ENDGAMEITEMS";
/*     */   public static final String SPY_PREVENTION = "SPYPREVENTION";
/*     */   public static final String AUTO_NETWORKING = "AUTO_NETWORKING";
/*     */   public static final String ENABLE_PNP_PORT_FORWARD = "ENABLE_PNP_PORT_FORWARD";
/*     */   public static final String NEWBIE_FRIENDLY = "NEWBIEFRIENDLY";
/*  33 */   private static final Logger logger = Logger.getLogger(Servers.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void loadProperties() {
/*  42 */     checkIfCreateTable();
/*  43 */     Connection dbcon = null;
/*  44 */     PreparedStatement ps = null;
/*  45 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  48 */       dbcon = DbConnector.getLoginDbCon();
/*  49 */       ps = dbcon.prepareStatement("SELECT * FROM SERVERPROPERTIES");
/*  50 */       rs = ps.executeQuery();
/*  51 */       while (rs.next())
/*     */       {
/*  53 */         String key = rs.getString("PROPKEY");
/*  54 */         String value = rs.getString("PROPVAL");
/*  55 */         props.put(key, value);
/*     */       }
/*     */     
/*  58 */     } catch (SQLException sqex) {
/*     */       
/*  60 */       logger.log(Level.WARNING, "Failed to load properties!" + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/*  64 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  65 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*  67 */     checkProperties();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void checkProperties() {
/*  72 */     String sqp = props.getProperty("STEAMQUERYPORT");
/*  73 */     if (sqp == null) {
/*  74 */       setValue("STEAMQUERYPORT", Short.toString(SteamHandler.steamQueryPort));
/*     */     } else {
/*     */       
/*  77 */       SteamHandler.steamQueryPort = getShort("STEAMQUERYPORT", SteamHandler.steamQueryPort);
/*     */     } 
/*  79 */     String npcs = props.getProperty("NPCS");
/*  80 */     if (npcs == null) {
/*  81 */       setValue("NPCS", Boolean.toString(Constants.loadNpcs));
/*     */     } else {
/*     */       
/*  84 */       Constants.loadNpcs = getBoolean("NPCS", Constants.loadNpcs);
/*     */     } 
/*  86 */     String egi = props.getProperty("ENDGAMEITEMS");
/*  87 */     if (egi == null) {
/*  88 */       setValue("ENDGAMEITEMS", Boolean.toString(Constants.loadEndGameItems));
/*     */     } else {
/*     */       
/*  91 */       Constants.loadEndGameItems = getBoolean("ENDGAMEITEMS", Constants.loadEndGameItems);
/*     */     } 
/*  93 */     String spy = props.getProperty("SPYPREVENTION");
/*  94 */     if (spy == null) {
/*  95 */       setValue("SPYPREVENTION", Boolean.toString(Constants.enableSpyPrevention));
/*     */     } else {
/*     */       
/*  98 */       Constants.enableSpyPrevention = getBoolean("SPYPREVENTION", Constants.enableSpyPrevention);
/*     */     } 
/* 100 */     String newbie = props.getProperty("NEWBIEFRIENDLY");
/* 101 */     if (newbie == null) {
/* 102 */       setValue("NEWBIEFRIENDLY", Boolean.toString(Constants.isNewbieFriendly));
/*     */     } else {
/*     */       
/* 105 */       Constants.isNewbieFriendly = getBoolean("NEWBIEFRIENDLY", Constants.isNewbieFriendly);
/*     */     } 
/* 107 */     String autoNet = props.getProperty("AUTO_NETWORKING");
/* 108 */     if (autoNet == null) {
/* 109 */       setValue("AUTO_NETWORKING", Boolean.toString(Constants.enableAutoNetworking));
/*     */     } else {
/* 111 */       Constants.enableAutoNetworking = getBoolean("AUTO_NETWORKING", Constants.enableAutoNetworking);
/* 112 */     }  String pnpPF = props.getProperty("ENABLE_PNP_PORT_FORWARD");
/* 113 */     if (pnpPF == null) {
/* 114 */       setValue("ENABLE_PNP_PORT_FORWARD", Boolean.toString(Constants.enablePnpPortForward));
/*     */     } else {
/* 116 */       Constants.enablePnpPortForward = getBoolean("ENABLE_PNP_PORT_FORWARD", Constants.enablePnpPortForward);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final void checkIfCreateTable() {
/* 121 */     Connection dbcon = null;
/* 122 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 125 */       dbcon = DbConnector.getLoginDbCon();
/* 126 */       ps = dbcon.prepareStatement("CREATE TABLE IF NOT EXISTS SERVERPROPERTIES        (            PROPKEY                 VARCHAR(50)   NOT NULL DEFAULT '',            PROPVAL                 VARCHAR(50)   NOT NULL DEFAULT ''        )");
/* 127 */       ps.execute();
/*     */       
/* 129 */       logger.info("Created properties table in the database");
/*     */     }
/* 131 */     catch (SQLException sqex) {
/*     */       
/* 133 */       logger.log(Level.WARNING, "Failed to create properties table!" + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 137 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 138 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void setValue(String key, String value) {
/* 144 */     Connection dbcon = null;
/* 145 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 148 */       dbcon = DbConnector.getLoginDbCon();
/* 149 */       if (props.containsKey(key)) {
/* 150 */         ps = dbcon.prepareStatement("UPDATE SERVERPROPERTIES SET PROPVAL=? WHERE PROPKEY=?");
/*     */       } else {
/* 152 */         ps = dbcon.prepareStatement("INSERT INTO SERVERPROPERTIES(PROPVAL,PROPKEY) VALUES (?,?)");
/* 153 */       }  ps.setString(1, value);
/* 154 */       ps.setString(2, key);
/* 155 */       ps.execute();
/*     */     }
/* 157 */     catch (SQLException sqex) {
/*     */       
/* 159 */       logger.log(Level.WARNING, "Failed to update property " + key + ":" + value + ", " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 163 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 164 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 166 */     props.put(key, value);
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
/*     */   public static boolean getBoolean(String key, boolean defaultValue) {
/* 181 */     String maybeBoolean = props.getProperty(key);
/* 182 */     return (maybeBoolean == null) ? defaultValue : Boolean.parseBoolean(maybeBoolean);
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
/*     */   public static int getInt(String key, int defaultValue) {
/* 197 */     String maybeInt = props.getProperty(key);
/* 198 */     if (maybeInt == null)
/*     */     {
/* 200 */       return defaultValue;
/*     */     }
/*     */     
/*     */     try {
/* 204 */       return Integer.parseInt(maybeInt);
/*     */     }
/* 206 */     catch (NumberFormatException e) {
/*     */       
/* 208 */       return defaultValue;
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
/*     */   public static long getLong(String key, long defaultValue) {
/* 224 */     String maybeLong = props.getProperty(key);
/* 225 */     if (maybeLong == null)
/*     */     {
/* 227 */       return defaultValue;
/*     */     }
/*     */     
/*     */     try {
/* 231 */       return Long.parseLong(maybeLong);
/*     */     }
/* 233 */     catch (NumberFormatException e) {
/*     */       
/* 235 */       return defaultValue;
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
/*     */   public static short getShort(String key, short defaultValue) {
/* 251 */     String maybeShort = props.getProperty(key);
/* 252 */     if (maybeShort == null)
/*     */     {
/* 254 */       return defaultValue;
/*     */     }
/*     */     
/*     */     try {
/* 258 */       return Short.parseShort(maybeShort);
/*     */     }
/* 260 */     catch (NumberFormatException e) {
/*     */       
/* 262 */       return defaultValue;
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
/*     */   public static String getString(String key, String defaultValue) {
/* 278 */     String maybeString = props.getProperty(key);
/* 279 */     return (maybeString == null) ? defaultValue : maybeString;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\ServerProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
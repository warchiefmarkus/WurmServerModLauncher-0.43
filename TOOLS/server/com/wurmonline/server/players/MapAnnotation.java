/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
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
/*     */ public final class MapAnnotation
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(MapAnnotation.class.getName());
/*     */   
/*     */   public static final byte PRIVATE_POI = 0;
/*     */   
/*     */   public static final byte VILLAGE_POI = 1;
/*     */   
/*     */   public static final byte ALLIANCE_POI = 2;
/*     */   
/*     */   public static final short MAX_PRIVATE_ANNOTATIONS = 500;
/*     */   
/*     */   public static final short MAX_VILLAGE_ANNOTATIONS = 500;
/*     */   public static final short MAX_ALLIANCE_ANNOTATIONS = 500;
/*     */   private long id;
/*     */   private String name;
/*     */   private byte type;
/*     */   private long position;
/*     */   private long ownerId;
/*     */   private String server;
/*     */   private byte icon;
/*     */   private static final String CREATE_NEW_POI = "INSERT INTO MAP_ANNOTATIONS (ID, NAME, POSITION, POITYPE, OWNERID, SERVER, ICON) VALUES ( ?, ?, ?, ?, ?, ?, ? );";
/*     */   private static final String DELETE_POI = "DELETE FROM MAP_ANNOTATIONS WHERE ID=?;";
/*     */   private static final String DELETE_ALL_PRIVATE_ANNOTATIONS_BY_OWNER = "DELETE FROM MAP_ANNOTATIONS WHERE OWNERID=? AND POITYPE=0;";
/*     */   
/*     */   public static final MapAnnotation createNew(long id, String _name, byte _type, long _position, long _ownerId, String _server, byte _icon) throws IOException {
/*  62 */     Connection dbcon = null;
/*  63 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  66 */       dbcon = DbConnector.getPlayerDbCon();
/*  67 */       ps = dbcon.prepareStatement("INSERT INTO MAP_ANNOTATIONS (ID, NAME, POSITION, POITYPE, OWNERID, SERVER, ICON) VALUES ( ?, ?, ?, ?, ?, ?, ? );");
/*  68 */       ps.setLong(1, id);
/*  69 */       ps.setString(2, _name);
/*  70 */       ps.setLong(3, _position);
/*  71 */       ps.setByte(4, _type);
/*  72 */       ps.setLong(5, _ownerId);
/*  73 */       ps.setString(6, _server);
/*  74 */       ps.setByte(7, _icon);
/*  75 */       ps.executeUpdate();
/*     */       
/*  77 */       MapAnnotation poi = new MapAnnotation(id, _name, _type, _position, _ownerId, _server, _icon);
/*  78 */       return poi;
/*     */     }
/*  80 */     catch (SQLException sqx) {
/*     */       
/*  82 */       logger.log(Level.WARNING, "Failed to create POI: " + _name + ": " + sqx.getMessage(), sqx);
/*  83 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  87 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  88 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MapAnnotation createNew(String _name, byte _type, long _position, long _ownerId, String _server, byte _icon) throws IOException {
/*  96 */     long id = WurmId.getNextPoiId();
/*  97 */     Connection dbcon = null;
/*  98 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 101 */       dbcon = DbConnector.getPlayerDbCon();
/* 102 */       ps = dbcon.prepareStatement("INSERT INTO MAP_ANNOTATIONS (ID, NAME, POSITION, POITYPE, OWNERID, SERVER, ICON) VALUES ( ?, ?, ?, ?, ?, ?, ? );");
/* 103 */       ps.setLong(1, id);
/* 104 */       ps.setString(2, _name);
/* 105 */       ps.setLong(3, _position);
/* 106 */       ps.setByte(4, _type);
/* 107 */       ps.setLong(5, _ownerId);
/* 108 */       ps.setString(6, _server);
/* 109 */       ps.setByte(7, _icon);
/* 110 */       ps.executeUpdate();
/*     */       
/* 112 */       MapAnnotation poi = new MapAnnotation(id, _name, _type, _position, _ownerId, _server, _icon);
/* 113 */       return poi;
/*     */     }
/* 115 */     catch (SQLException sqx) {
/*     */       
/* 117 */       logger.log(Level.WARNING, "Failed to create POI: " + _name + ": " + sqx.getMessage(), sqx);
/* 118 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 122 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 123 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteAnnotation(long id) throws IOException {
/* 129 */     Connection dbcon = null;
/* 130 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 133 */       dbcon = DbConnector.getPlayerDbCon();
/* 134 */       ps = dbcon.prepareStatement("DELETE FROM MAP_ANNOTATIONS WHERE ID=?;");
/* 135 */       ps.setLong(1, id);
/* 136 */       ps.executeUpdate();
/*     */     }
/* 138 */     catch (SQLException sqx) {
/*     */       
/* 140 */       logger.log(Level.WARNING, "Failed to delete POI: " + id + " :" + sqx
/* 141 */           .getMessage(), sqx);
/* 142 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 146 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 147 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deletePrivateAnnotationsForOwner(long ownerId) throws IOException {
/* 153 */     Connection dbcon = null;
/* 154 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 157 */       dbcon = DbConnector.getPlayerDbCon();
/* 158 */       ps = dbcon.prepareStatement("DELETE FROM MAP_ANNOTATIONS WHERE OWNERID=? AND POITYPE=0;");
/* 159 */       ps.setLong(1, ownerId);
/* 160 */       ps.executeUpdate();
/*     */     }
/* 162 */     catch (SQLException sqx) {
/*     */       
/* 164 */       logger.log(Level.WARNING, "Failed to delete POI's for owner: " + ownerId + " :" + sqx
/* 165 */           .getMessage(), sqx);
/* 166 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 170 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 171 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MapAnnotation(long _id) {
/* 177 */     this.id = _id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapAnnotation(long _id, String _name, byte _type, long _position, long _ownerId, String _server, byte _icon) {
/* 183 */     this.id = _id;
/* 184 */     this.name = _name;
/* 185 */     this.type = _type;
/* 186 */     this.position = _position;
/* 187 */     this.ownerId = _ownerId;
/* 188 */     this.server = _server;
/* 189 */     this.icon = _icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte getIcon() {
/* 194 */     return this.icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getId() {
/* 199 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 204 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getOwnerId() {
/* 209 */     return this.ownerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getPosition() {
/* 214 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getServer() {
/* 219 */     return this.server;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte getType() {
/* 224 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getXPos() {
/* 229 */     return BigInteger.valueOf(this.position).shiftRight(32).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getYPos() {
/* 234 */     return BigInteger.valueOf(this.position).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIcon(byte _icon) {
/* 239 */     this.icon = _icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String _name) {
/* 244 */     this.name = _name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOwnerId(long _ownerId) {
/* 249 */     this.ownerId = _ownerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosition(int x, int y) {
/* 254 */     long pos = x;
/* 255 */     this.position = BigInteger.valueOf(pos).shiftLeft(32).longValue() + y;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosition(long _position) {
/* 260 */     this.position = _position;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServer(String _server) {
/* 265 */     this.server = _server;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setType(byte _type) {
/* 270 */     this.type = _type;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\MapAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
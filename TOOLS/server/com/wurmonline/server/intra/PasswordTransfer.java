/*     */ package com.wurmonline.server.intra;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class PasswordTransfer
/*     */   extends IntraCommand
/*     */   implements MiscConstants
/*     */ {
/*     */   private static final String CREATE_PASSWORD_TRANSFER = "INSERT INTO PASSWORDTRANSFERS(NAME,WURMID,TIMESTAMP,PASSWORD) VALUES (?,?,?,?)";
/*     */   private static final String DELETE_PASSWORD_TRANSFER = "DELETE FROM PASSWORDTRANSFERS WHERE NAME=? AND WURMID=? AND TIMESTAMP=? AND PASSWORD=?";
/*     */   private static final String GET_ALL_PASSWORDTRANSFERS = "SELECT * FROM PASSWORDTRANSFERS";
/*  51 */   private static Logger logger = Logger.getLogger(PasswordTransfer.class.getName());
/*     */   private final String name;
/*     */   private final long wurmid;
/*     */   private final String newPassword;
/*     */   private final long timestamp;
/*     */   private boolean done = false;
/*  57 */   private IntraClient client = null;
/*     */   
/*     */   private boolean started = false;
/*     */   
/*     */   public boolean deleted = false;
/*     */   private boolean sentTransfer = false;
/*  63 */   public static final List<PasswordTransfer> transfers = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PasswordTransfer(String aName, long playerId, String password, long _timestamp, boolean load) {
/*  76 */     this.name = aName;
/*  77 */     this.wurmid = playerId;
/*  78 */     this.newPassword = password;
/*  79 */     this.timestamp = _timestamp;
/*  80 */     this.timeOutTime = 30000L;
/*  81 */     if (!load)
/*     */     {
/*  83 */       save();
/*     */     }
/*  85 */     transfers.add(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void save() {
/*  90 */     Connection dbcon = null;
/*  91 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  94 */       dbcon = DbConnector.getLoginDbCon();
/*  95 */       ps = dbcon.prepareStatement("INSERT INTO PASSWORDTRANSFERS(NAME,WURMID,TIMESTAMP,PASSWORD) VALUES (?,?,?,?)");
/*     */       
/*  97 */       ps.setString(1, this.name);
/*  98 */       ps.setLong(2, this.wurmid);
/*  99 */       ps.setLong(3, this.timestamp);
/* 100 */       ps.setString(4, this.newPassword);
/* 101 */       ps.executeUpdate();
/*     */     }
/* 103 */     catch (SQLException sqex) {
/*     */       
/* 105 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 109 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 110 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void delete() {
/* 116 */     Connection dbcon = null;
/* 117 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 120 */       dbcon = DbConnector.getLoginDbCon();
/* 121 */       ps = dbcon.prepareStatement("DELETE FROM PASSWORDTRANSFERS WHERE NAME=? AND WURMID=? AND TIMESTAMP=? AND PASSWORD=?");
/*     */       
/* 123 */       ps.setString(1, this.name);
/* 124 */       ps.setLong(2, this.wurmid);
/* 125 */       ps.setLong(3, this.timestamp);
/* 126 */       ps.setString(4, this.newPassword);
/* 127 */       ps.executeUpdate();
/* 128 */       this.deleted = true;
/*     */     }
/* 130 */     catch (SQLException sqex) {
/*     */       
/* 132 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 136 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 137 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadAllPasswordTransfers() {
/* 143 */     long start = System.nanoTime();
/* 144 */     int loadedPasswordTransfers = 0;
/* 145 */     Connection dbcon = null;
/* 146 */     PreparedStatement ps = null;
/* 147 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 150 */       dbcon = DbConnector.getLoginDbCon();
/* 151 */       ps = dbcon.prepareStatement("SELECT * FROM PASSWORDTRANSFERS");
/* 152 */       rs = ps.executeQuery();
/* 153 */       while (rs.next())
/*     */       {
/* 155 */         new PasswordTransfer(rs.getString("Name"), rs.getLong("WURMID"), rs.getString("PASSWORD"), rs
/* 156 */             .getLong("TIMESTAMP"), true);
/* 157 */         loadedPasswordTransfers++;
/*     */       }
/*     */     
/* 160 */     } catch (SQLException sqex) {
/*     */       
/* 162 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 166 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 167 */       DbConnector.returnConnection(dbcon);
/* 168 */       long end = System.nanoTime();
/* 169 */       logger.info("Loaded " + loadedPasswordTransfers + " PasswordTransfers from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
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
/*     */   public boolean poll() {
/* 182 */     if (System.currentTimeMillis() > this.timeOutAt) {
/*     */       
/* 184 */       PlayerInfo info = PlayerInfoFactory.createPlayerInfo(this.name);
/*     */       
/*     */       try {
/* 187 */         info.load();
/*     */       }
/* 189 */       catch (Exception eex) {
/*     */         
/* 191 */         logger.log(Level.WARNING, "Failed to load info for wurmid " + this.wurmid + ".", eex);
/* 192 */         delete();
/* 193 */         this.done = true;
/*     */       } 
/* 195 */       if (info.wurmId <= 0L) {
/*     */         
/* 197 */         logger.log(Level.WARNING, "Failed to load info for wurmid " + this.wurmid + ". No info available.");
/* 198 */         delete();
/* 199 */         this.done = true;
/*     */       }
/* 201 */       else if (info.currentServer == Servers.localServer.id) {
/*     */         
/* 203 */         delete();
/* 204 */         this.done = true;
/*     */       } 
/* 206 */       if (!this.done) {
/*     */         
/* 208 */         this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/* 209 */         ServerEntry entry = Servers.getServerWithId(info.currentServer);
/* 210 */         if (entry != null) {
/*     */           
/* 212 */           if ((new LoginServerWebConnection(info.currentServer)).changePassword(this.wurmid, this.newPassword))
/*     */           {
/*     */ 
/*     */             
/* 216 */             this.sentTransfer = true;
/* 217 */             this.done = true;
/* 218 */             delete();
/* 219 */             return true;
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 225 */           logger
/* 226 */             .log(Level.INFO, this.wurmid + " for currentserver " + info.currentServer + ": the server does not exist.");
/*     */           
/* 228 */           delete();
/* 229 */           this.done = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 233 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean pollOld() {
/* 241 */     logger2.log(Level.INFO, "PT poll " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", timestamp: " + this.timestamp);
/* 242 */     PlayerInfo info = PlayerInfoFactory.createPlayerInfo(this.name);
/*     */     
/*     */     try {
/* 245 */       info.load();
/*     */     }
/* 247 */     catch (Exception eex) {
/*     */       
/* 249 */       logger.log(Level.WARNING, "Failed to load info for wurmid " + this.wurmid + ".", eex);
/* 250 */       delete();
/* 251 */       this.done = true;
/*     */     } 
/* 253 */     if (info.wurmId <= 0L) {
/*     */       
/* 255 */       logger.log(Level.WARNING, "Failed to load info for wurmid " + this.wurmid + ". No info available.");
/* 256 */       delete();
/* 257 */       this.done = true;
/*     */     }
/* 259 */     else if (info.currentServer == Servers.localServer.id) {
/*     */       
/* 261 */       delete();
/* 262 */       this.done = true;
/*     */     }
/* 264 */     else if (this.client == null && (System.currentTimeMillis() > this.timeOutAt || !this.started)) {
/*     */       
/* 266 */       logger2.log(Level.INFO, "PT starting " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", timestamp: " + this.timestamp);
/*     */       
/* 268 */       ServerEntry entry = Servers.getServerWithId(info.currentServer);
/* 269 */       if (entry != null) {
/*     */         
/*     */         try
/*     */         {
/* 273 */           this.startTime = System.currentTimeMillis();
/* 274 */           this.timeOutAt = this.startTime + this.timeOutTime;
/* 275 */           this.started = true;
/* 276 */           this.client = new IntraClient(entry.INTRASERVERADDRESS, Integer.parseInt(entry.INTRASERVERPORT), this);
/* 277 */           this.client.login(entry.INTRASERVERPASSWORD, true);
/* 278 */           this.done = false;
/*     */         }
/* 280 */         catch (IOException iox)
/*     */         {
/* 282 */           this.done = true;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 287 */         delete();
/* 288 */         this.done = true;
/* 289 */         logger.log(Level.WARNING, "No server entry for server with id " + info.currentServer);
/*     */       } 
/*     */     } 
/* 292 */     if (this.client != null && !this.done) {
/*     */       
/* 294 */       if (System.currentTimeMillis() > this.timeOutAt) {
/*     */         
/* 296 */         logger2.log(Level.INFO, "PT timeout " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", timestamp: " + this.timestamp);
/*     */         
/* 298 */         this.done = true;
/*     */       } 
/* 300 */       if (this.client.loggedIn && !this.done)
/*     */       {
/* 302 */         if (!this.sentTransfer) {
/*     */           
/*     */           try {
/*     */             
/* 306 */             this.client.executePasswordUpdate(this.wurmid, this.newPassword, this.timestamp);
/* 307 */             this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/* 308 */             this.sentTransfer = true;
/*     */           }
/* 310 */           catch (IOException iox) {
/*     */             
/* 312 */             logger.log(Level.WARNING, iox.getMessage(), iox);
/* 313 */             this.done = true;
/*     */           } 
/*     */         }
/*     */       }
/* 317 */       if (!this.done) {
/*     */         
/*     */         try {
/*     */           
/* 321 */           this.client.update();
/*     */         }
/* 323 */         catch (Exception ex) {
/*     */           
/* 325 */           this.done = true;
/*     */         } 
/*     */       }
/*     */     } 
/* 329 */     if (this.done && this.client != null) {
/*     */       
/* 331 */       this.sentTransfer = false;
/* 332 */       this.client.disconnect("Done");
/* 333 */       this.client = null;
/* 334 */       logger2.log(Level.INFO, "PT Disconnected " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", timestamp: " + this.timestamp);
/*     */     } 
/*     */     
/* 337 */     if (this.done) {
/* 338 */       logger2.log(Level.INFO, "PT finishing " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", timestamp: " + this.timestamp);
/*     */     }
/* 340 */     return this.done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reschedule(IntraClient aClient) {
/* 351 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(IntraClient aClient) {
/* 362 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandExecuted(IntraClient aClient) {
/* 373 */     delete();
/* 374 */     logger2.log(Level.INFO, "PT accepted " + num + ", name: " + this.name + ", wurmid: " + this.wurmid + ", timestamp: " + this.timestamp);
/* 375 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commandFailed(IntraClient aClient) {
/* 386 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dataReceived(IntraClient aClient) {
/* 397 */     this.done = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void receivingData(ByteBuffer buffer) {
/* 408 */     this.done = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\PasswordTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
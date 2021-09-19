/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
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
/*     */ class DbSkill
/*     */   extends Skill
/*     */ {
/*  36 */   private static Logger logger = Logger.getLogger(DbSkill.class.getName());
/*     */ 
/*     */   
/*     */   private static final String createCreatureSkill = "insert into SKILLS (VALUE, LASTUSED, MINVALUE, NUMBER, OWNER,ID) values(?,?,?,?,?,?)";
/*     */   
/*     */   private static final String updateCreatureSkill = "update SKILLS set VALUE=?, LASTUSED=?, MINVALUE=? where ID=?";
/*     */   
/*     */   private static final String updateNumber = "update SKILLS set NUMBER=? where ID=?";
/*     */   
/*     */   private static final String setJoat = "update SKILLS set JOAT=? where ID=?";
/*     */   
/*     */   private static final String getCreatureSkill = "select * from SKILLS where ID=?";
/*     */   
/*     */   private static final String createSkillChance = "insert into SKILLCHANCES (SKILL,DIFFICULTY,CHANCE) values(?,?,?)";
/*     */   
/*     */   private static final String loadSkillChance = "select * from SKILLCHANCES";
/*     */ 
/*     */   
/*     */   DbSkill(int aNumber, double aStartValue, Skills aParent) {
/*  55 */     super(aNumber, aStartValue, aParent);
/*     */   }
/*     */ 
/*     */   
/*     */   DbSkill(long aId, Skills aParent) throws IOException {
/*  60 */     super(aId, aParent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   DbSkill(long aId, Skills aParent, int aNumber, double aKnowledge, double aMinimum, long aLastused) {
/*  66 */     super(aId, aParent, aNumber, aKnowledge, aMinimum, aLastused);
/*     */   }
/*     */ 
/*     */   
/*     */   DbSkill(long aId, int aNumber, double aKnowledge, double aMinimum, long aLastused) {
/*  71 */     super(aId, aNumber, aKnowledge, aMinimum, aLastused);
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
/*     */   void save() throws IOException {
/*  84 */     if (this.parent.isPersonal()) {
/*     */       
/*  86 */       Connection dbcon = null;
/*  87 */       PreparedStatement ps = null;
/*  88 */       long wurmId = this.parent.getId();
/*     */       
/*     */       try {
/*  91 */         if (WurmId.getType(wurmId) == 1) {
/*  92 */           dbcon = DbConnector.getCreatureDbCon();
/*     */         } else {
/*  94 */           dbcon = DbConnector.getPlayerDbCon();
/*  95 */         }  if (exists(dbcon, this.id))
/*     */         {
/*  97 */           ps = dbcon.prepareStatement("update SKILLS set VALUE=?, LASTUSED=?, MINVALUE=? where ID=?");
/*  98 */           ps.setDouble(1, this.knowledge);
/*  99 */           ps.setLong(2, this.lastUsed);
/* 100 */           ps.setDouble(3, this.minimum);
/* 101 */           ps.setLong(4, this.id);
/* 102 */           ps.executeUpdate();
/*     */         }
/*     */         else
/*     */         {
/* 106 */           ps = dbcon.prepareStatement("insert into SKILLS (VALUE, LASTUSED, MINVALUE, NUMBER, OWNER,ID) values(?,?,?,?,?,?)");
/* 107 */           ps.setDouble(1, this.knowledge);
/* 108 */           ps.setLong(2, this.lastUsed);
/* 109 */           ps.setDouble(3, this.minimum);
/* 110 */           ps.setInt(4, this.number);
/* 111 */           ps.setLong(5, wurmId);
/* 112 */           ps.setLong(6, this.id);
/* 113 */           ps.executeUpdate();
/*     */         }
/*     */       
/* 116 */       } catch (SQLException e) {
/*     */         
/* 118 */         throw new IOException("Problem updating or creating Creature skills, ID: " + this.id + ", wurmID: " + wurmId, e);
/*     */       }
/*     */       finally {
/*     */         
/* 122 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 123 */         DbConnector.returnConnection(dbcon);
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
/*     */   void saveValue(boolean player) throws IOException {
/* 146 */     Connection dbcon = null;
/* 147 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 150 */       if (player) {
/* 151 */         dbcon = DbConnector.getPlayerDbCon();
/*     */       } else {
/* 153 */         dbcon = DbConnector.getCreatureDbCon();
/*     */       } 
/* 155 */       ps = dbcon.prepareStatement("update SKILLS set VALUE=?, LASTUSED=?, MINVALUE=? where ID=?");
/* 156 */       ps.setDouble(1, this.knowledge);
/* 157 */       ps.setLong(2, this.lastUsed);
/* 158 */       ps.setDouble(3, this.minimum);
/* 159 */       ps.setLong(4, this.id);
/* 160 */       ps.executeUpdate();
/*     */     }
/* 162 */     catch (SQLException sql) {
/*     */       
/* 164 */       throw new IOException("Problem updating or creating Creature skills, ID: " + this.id, sql);
/*     */     }
/*     */     finally {
/*     */       
/* 168 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 169 */       DbConnector.returnConnection(dbcon);
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
/*     */   public void setJoat(boolean _joat) throws IOException {
/* 183 */     if (_joat != this.joat) {
/*     */       
/* 185 */       this.joat = _joat;
/* 186 */       Connection dbcon = null;
/* 187 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 190 */         dbcon = DbConnector.getPlayerDbCon();
/* 191 */         ps = dbcon.prepareStatement("update SKILLS set JOAT=? where ID=?");
/* 192 */         ps.setBoolean(1, this.joat);
/* 193 */         ps.setLong(2, this.id);
/* 194 */         ps.executeUpdate();
/*     */       }
/* 196 */       catch (SQLException sql) {
/*     */         
/* 198 */         throw new IOException("Problem setting JOAT, ID: " + this.id, sql);
/*     */       }
/*     */       finally {
/*     */         
/* 202 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 203 */         DbConnector.returnConnection(dbcon);
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
/*     */   public void setNumber(int newNumber) throws IOException {
/* 218 */     this.number = newNumber;
/* 219 */     Connection dbcon = null;
/* 220 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 223 */       dbcon = DbConnector.getPlayerDbCon();
/* 224 */       ps = dbcon.prepareStatement("update SKILLS set NUMBER=? where ID=?");
/* 225 */       ps.setInt(1, this.number);
/* 226 */       ps.setLong(2, this.id);
/* 227 */       ps.executeUpdate();
/*     */     }
/* 229 */     catch (SQLException sql) {
/*     */       
/* 231 */       throw new IOException("Problem setting Number, ID: " + this.id, sql);
/*     */     }
/*     */     finally {
/*     */       
/* 235 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 236 */       DbConnector.returnConnection(dbcon);
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
/*     */   void load() throws IOException {
/* 249 */     Connection dbcon = null;
/* 250 */     PreparedStatement ps = null;
/* 251 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 254 */       if (this.parent.isPersonal())
/*     */       {
/* 256 */         long wurmId = this.parent.getId();
/* 257 */         if (WurmId.getType(wurmId) == 1) {
/* 258 */           dbcon = DbConnector.getCreatureDbCon();
/*     */         } else {
/* 260 */           dbcon = DbConnector.getPlayerDbCon();
/* 261 */         }  ps = dbcon.prepareStatement("select * from SKILLS where ID=?");
/* 262 */         ps.setLong(1, this.id);
/* 263 */         rs = ps.executeQuery();
/* 264 */         if (rs.next())
/*     */         {
/* 266 */           this.number = rs.getInt("NUMBER");
/* 267 */           this.knowledge = rs.getDouble("VALUE");
/* 268 */           this.minimum = rs.getDouble("MINVALUE");
/* 269 */           this.lastUsed = rs.getLong("LASTUSED");
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 280 */     catch (SQLException sqx) {
/*     */       
/* 282 */       throw new IOException("Problem updating or creating Creature/Player skills, ID: " + this.id, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 286 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 287 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean exists(Connection aDbcon, long aId) throws SQLException {
/* 297 */     PreparedStatement ps = null;
/* 298 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 301 */       ps = aDbcon.prepareStatement("select * from SKILLS where ID=?");
/* 302 */       ps.setLong(1, aId);
/* 303 */       rs = ps.executeQuery();
/* 304 */       return rs.next();
/*     */     }
/* 306 */     catch (SQLException sqx) {
/*     */       
/* 308 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 310 */         logger.log(Level.FINE, "Problem checking if creature skill ID exists, ID: " + aId, sqx);
/*     */       }
/* 312 */       throw sqx;
/*     */     }
/*     */     finally {
/*     */       
/* 316 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static byte[][] loadSkillChances() throws Exception {
/* 322 */     Connection dbcon = null;
/* 323 */     PreparedStatement ps = null;
/* 324 */     ResultSet rs = null;
/* 325 */     byte[][] toReturn = (byte[][])null;
/*     */     
/*     */     try {
/* 328 */       dbcon = DbConnector.getTemplateDbCon();
/* 329 */       ps = dbcon.prepareStatement("select * from SKILLCHANCES");
/* 330 */       rs = ps.executeQuery();
/*     */       
/* 332 */       while (rs.next())
/*     */       {
/* 334 */         if (toReturn == null)
/* 335 */           toReturn = new byte[101][101]; 
/* 336 */         byte sk = rs.getByte("SKILL");
/* 337 */         byte diff = rs.getByte("DIFFICULTY");
/* 338 */         byte chance = rs.getByte("CHANCE");
/* 339 */         toReturn[sk][diff] = chance;
/*     */       }
/*     */     
/* 342 */     } catch (SQLException sqx) {
/*     */       
/* 344 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 346 */         logger.log(Level.FINE, "Problem loading skill chances", sqx);
/*     */       }
/* 348 */       throw sqx;
/*     */     }
/*     */     finally {
/*     */       
/* 352 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 353 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 355 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   static void saveSkillChances(byte[][] chances) throws Exception {
/* 360 */     Connection dbcon = null;
/* 361 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 364 */       dbcon = DbConnector.getTemplateDbCon();
/* 365 */       for (int x = 0; x < 101; x++) {
/*     */         
/* 367 */         for (int y = 0; y < 101; y++)
/*     */         {
/* 369 */           ps = dbcon.prepareStatement("insert into SKILLCHANCES (SKILL,DIFFICULTY,CHANCE) values(?,?,?)");
/* 370 */           ps.setByte(1, (byte)x);
/* 371 */           ps.setByte(2, (byte)y);
/* 372 */           ps.setByte(3, chances[x][y]);
/* 373 */           ps.executeUpdate();
/*     */         }
/*     */       
/*     */       } 
/* 377 */     } catch (SQLException sqx) {
/*     */       
/* 379 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 381 */         logger.log(Level.FINE, "Problem saving skill chances", sqx);
/*     */       }
/* 383 */       throw sqx;
/*     */     }
/*     */     finally {
/*     */       
/* 387 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 388 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\DbSkill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
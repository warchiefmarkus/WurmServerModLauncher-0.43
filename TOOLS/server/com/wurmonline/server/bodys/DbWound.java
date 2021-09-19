/*     */ package com.wurmonline.server.bodys;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.NoSpaceException;
/*     */ import com.wurmonline.server.utils.DbUtilities;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DbWound
/*     */   extends Wound
/*     */ {
/*     */   private static final String CREATE_WOUND = "INSERT INTO WOUNDS( ID, OWNER,TYPE,LOCATION,SEVERITY, POISONSEVERITY,INFECTIONSEVERITY,BANDAGED,LASTPOLLED) VALUES(?,?,?,?,?,?,?,?,?)";
/*     */   private static final String DELETE_WOUND = "DELETE FROM WOUNDS WHERE ID=?";
/*     */   private static final String SET_SEVERITY = "update WOUNDS set SEVERITY=? where ID=?";
/*     */   private static final String SET_POISONSEVERITY = "update WOUNDS set POISONSEVERITY=? where ID=?";
/*     */   private static final String SET_INFECTIONSEVERITY = "update WOUNDS set INFECTIONSEVERITY=? where ID=?";
/*     */   private static final String SET_BANDAGED = "update WOUNDS set BANDAGED=? where ID=?";
/*     */   private static final String SET_HEALEFF = "update WOUNDS set HEALEFF=? where ID=?";
/*  50 */   private static final Logger logger = Logger.getLogger(DbWound.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbWound(byte aType, byte aLocation, float aSeverity, long aOwner, float aPoisonSeverity, float aInfectionSeverity, boolean pvp, boolean spell) {
/*  65 */     super(aType, aLocation, aSeverity, aOwner, aPoisonSeverity, aInfectionSeverity, false, pvp, spell);
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
/*     */   public DbWound(long aId, byte aType, byte aLocation, float aSeverity, long aOwner, float aPoisonSeverity, float aInfectionSeverity, long aLastPolled, boolean aBandaged, byte aHealEff) {
/*  84 */     super(aId, aType, aLocation, aSeverity, aOwner, aPoisonSeverity, aInfectionSeverity, aLastPolled, aBandaged, aHealEff);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void create() {
/*  90 */     Connection dbcon = null;
/*  91 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  94 */       dbcon = DbConnector.getPlayerDbCon();
/*  95 */       ps = dbcon.prepareStatement("INSERT INTO WOUNDS( ID, OWNER,TYPE,LOCATION,SEVERITY, POISONSEVERITY,INFECTIONSEVERITY,BANDAGED,LASTPOLLED) VALUES(?,?,?,?,?,?,?,?,?)");
/*  96 */       ps.setLong(1, getId());
/*  97 */       ps.setLong(2, getOwner());
/*  98 */       ps.setByte(3, getType());
/*  99 */       ps.setByte(4, getLocation());
/* 100 */       ps.setFloat(5, getSeverity());
/* 101 */       ps.setFloat(6, getPoisonSeverity());
/* 102 */       ps.setFloat(7, getInfectionSeverity());
/* 103 */       ps.setBoolean(8, isBandaged());
/* 104 */       ps.setLong(9, getLastPolled());
/* 105 */       ps.executeUpdate();
/*     */     }
/* 107 */     catch (SQLException sqex) {
/*     */       
/* 109 */       logger.log(Level.WARNING, getId() + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 113 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 114 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void setSeverity(float sev) {
/* 121 */     sev = Math.max(0.0F, sev);
/* 122 */     if (getSeverity() != sev) {
/*     */       
/* 124 */       this.severity = sev;
/*     */       
/* 126 */       Connection dbcon = null;
/* 127 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 130 */         dbcon = DbConnector.getPlayerDbCon();
/* 131 */         ps = dbcon.prepareStatement("update WOUNDS set SEVERITY=? where ID=?");
/* 132 */         ps.setFloat(1, getSeverity());
/* 133 */         ps.setLong(2, getId());
/* 134 */         ps.executeUpdate();
/*     */       }
/* 136 */       catch (SQLException sqx) {
/*     */         
/* 138 */         logger.log(Level.WARNING, "Failed to save wound " + getId() + ":" + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 142 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 143 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPoisonSeverity(float sev) {
/* 151 */     if (this.poisonSeverity != sev) {
/*     */       
/* 153 */       this.poisonSeverity = Math.max(0.0F, sev);
/* 154 */       this.poisonSeverity = Math.min(100.0F, this.poisonSeverity);
/*     */       
/* 156 */       if (this.creature != null && this.creature.isPlayer())
/*     */       {
/* 158 */         if (sev == 0.0F) {
/* 159 */           this.creature.poisonChanged(true, this);
/*     */         } else {
/* 161 */           this.creature.poisonChanged(false, this);
/*     */         }  } 
/* 163 */       Connection dbcon = null;
/* 164 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 167 */         dbcon = DbConnector.getPlayerDbCon();
/* 168 */         ps = dbcon.prepareStatement("update WOUNDS set POISONSEVERITY=? where ID=?");
/*     */         
/* 170 */         ps.setFloat(1, getPoisonSeverity());
/* 171 */         ps.setLong(2, getId());
/* 172 */         ps.executeUpdate();
/*     */       }
/* 174 */       catch (SQLException sqx) {
/*     */         
/* 176 */         logger.log(Level.WARNING, "Failed to save wound " + getId() + ":" + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 180 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 181 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setInfectionSeverity(float sev) {
/* 189 */     if (this.infectionSeverity != sev) {
/*     */       
/* 191 */       this.infectionSeverity = Math.max(0.0F, sev);
/* 192 */       this.infectionSeverity = Math.min(100.0F, this.infectionSeverity);
/* 193 */       Connection dbcon = null;
/* 194 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 197 */         dbcon = DbConnector.getPlayerDbCon();
/* 198 */         ps = dbcon.prepareStatement("update WOUNDS set INFECTIONSEVERITY=? where ID=?");
/*     */         
/* 200 */         ps.setFloat(1, this.infectionSeverity);
/* 201 */         ps.setLong(2, getId());
/* 202 */         ps.executeUpdate();
/*     */       }
/* 204 */       catch (SQLException sqx) {
/*     */         
/* 206 */         logger.log(Level.WARNING, "Failed to save wound " + getId() + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 210 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 211 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBandaged(boolean aBandaged) {
/* 219 */     if (this.isBandaged != aBandaged) {
/*     */       
/* 221 */       this.isBandaged = aBandaged;
/* 222 */       Connection dbcon = null;
/* 223 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 226 */         dbcon = DbConnector.getPlayerDbCon();
/* 227 */         ps = dbcon.prepareStatement("update WOUNDS set BANDAGED=? where ID=?");
/* 228 */         ps.setBoolean(1, isBandaged());
/* 229 */         ps.setLong(2, getId());
/* 230 */         ps.executeUpdate();
/*     */       }
/* 232 */       catch (SQLException sqx) {
/*     */         
/* 234 */         logger.log(Level.WARNING, "Failed to set bandaged for wound " + getId() + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 238 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 239 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHealeff(byte healeff) {
/* 247 */     if (this.healEff < healeff) {
/*     */       
/* 249 */       this.healEff = healeff;
/* 250 */       Connection dbcon = null;
/* 251 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 254 */         dbcon = DbConnector.getPlayerDbCon();
/* 255 */         ps = dbcon.prepareStatement("update WOUNDS set HEALEFF=? where ID=?");
/* 256 */         ps.setByte(1, getHealEff());
/* 257 */         ps.setLong(2, getId());
/* 258 */         ps.executeUpdate();
/*     */       }
/* 260 */       catch (SQLException sqx) {
/*     */         
/* 262 */         logger.log(Level.WARNING, "Failed to save wound " + getId() + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 266 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 267 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */       
/*     */       try {
/* 271 */         if (getCreature().getBody() != null) {
/*     */           
/* 273 */           Item bodypart = getCreature().getBody().getBodyPartForWound(this);
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 278 */             Creature[] watchers = bodypart.getWatchers();
/* 279 */             for (int x = 0; x < watchers.length; x++) {
/* 280 */               watchers[x].getCommunicator().sendUpdateWound(this, bodypart);
/*     */             }
/* 282 */           } catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 289 */         else if (getCreature() != null) {
/* 290 */           logger.log(Level.WARNING, getCreature().getName() + " body is null.", new Exception());
/*     */         } else {
/* 292 */           logger.log(Level.WARNING, "Wound: creature==null", new Exception());
/*     */         }
/*     */       
/* 295 */       } catch (NoSpaceException nsp) {
/*     */         
/* 297 */         logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void setLastPolled(long lp) {
/* 305 */     if (this.lastPolled != lp)
/*     */     {
/* 307 */       this.lastPolled = lp;
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
/*     */   final void delete() {
/* 331 */     Connection dbcon = null;
/* 332 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 335 */       dbcon = DbConnector.getPlayerDbCon();
/* 336 */       ps = dbcon.prepareStatement("DELETE FROM WOUNDS WHERE ID=?");
/* 337 */       ps.setLong(1, getId());
/* 338 */       ps.executeUpdate();
/*     */     }
/* 340 */     catch (SQLException sqx) {
/*     */       
/* 342 */       logger.log(Level.WARNING, "Failed to delete wound " + getId() + ":" + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 346 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 347 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/* 350 */     if (this.poisonSeverity > 0.0F)
/*     */     {
/* 352 */       if (this.creature != null && this.creature.isPlayer())
/*     */       {
/* 354 */         this.creature.poisonChanged(true, this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\DbWound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
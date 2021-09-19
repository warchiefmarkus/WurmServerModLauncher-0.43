/*    */ package com.wurmonline.server.bodys;
/*    */ 
/*    */ import com.wurmonline.server.DbConnector;
/*    */ import com.wurmonline.server.MiscConstants;
/*    */ import com.wurmonline.server.utils.DbUtilities;
/*    */ import java.io.IOException;
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WoundMetaData
/*    */   implements MiscConstants
/*    */ {
/* 37 */   private static final Logger logger = Logger.getLogger(WoundMetaData.class.getName());
/*    */   
/*    */   private final byte location;
/*    */   
/*    */   private final long id;
/*    */   
/*    */   private final byte type;
/*    */   
/*    */   private final float severity;
/*    */   
/*    */   private final long owner;
/*    */   
/*    */   private final float poisonSeverity;
/*    */   
/*    */   private final float infectionSeverity;
/*    */   private final boolean isBandaged;
/*    */   private final long lastPolled;
/*    */   private final byte healEff;
/*    */   private static final String CREATE_WOUND = "INSERT INTO WOUNDS( ID, OWNER,TYPE,LOCATION,SEVERITY, POISONSEVERITY,INFECTIONSEVERITY,BANDAGED,LASTPOLLED, HEALEFF) VALUES(?,?,?,?,?,?,?,?,?,?)";
/*    */   
/*    */   public WoundMetaData(long aId, byte aType, byte aLocation, float aSeverity, long aOwner, float aPoisonSeverity, float aInfectionSeverity, boolean aBandaged, long aLastPolled, byte aHealEff) {
/* 58 */     this.id = aId;
/* 59 */     this.type = aType;
/* 60 */     this.location = aLocation;
/* 61 */     this.severity = aSeverity;
/* 62 */     this.owner = aOwner;
/* 63 */     this.poisonSeverity = aPoisonSeverity;
/* 64 */     this.infectionSeverity = aInfectionSeverity;
/* 65 */     this.lastPolled = aLastPolled;
/* 66 */     this.healEff = aHealEff;
/* 67 */     this.isBandaged = aBandaged;
/*    */   }
/*    */ 
/*    */   
/*    */   public void save() throws IOException {
/* 72 */     Connection dbcon = null;
/* 73 */     PreparedStatement ps = null;
/*    */     
/*    */     try {
/* 76 */       dbcon = DbConnector.getPlayerDbCon();
/* 77 */       ps = dbcon.prepareStatement("INSERT INTO WOUNDS( ID, OWNER,TYPE,LOCATION,SEVERITY, POISONSEVERITY,INFECTIONSEVERITY,BANDAGED,LASTPOLLED, HEALEFF) VALUES(?,?,?,?,?,?,?,?,?,?)");
/* 78 */       ps.setLong(1, this.id);
/* 79 */       ps.setLong(2, this.owner);
/* 80 */       ps.setByte(3, this.type);
/* 81 */       ps.setByte(4, this.location);
/* 82 */       ps.setFloat(5, this.severity);
/* 83 */       ps.setFloat(6, this.poisonSeverity);
/* 84 */       ps.setFloat(7, this.infectionSeverity);
/* 85 */       ps.setBoolean(8, this.isBandaged);
/* 86 */       ps.setLong(9, this.lastPolled);
/* 87 */       ps.setByte(10, this.healEff);
/* 88 */       ps.executeUpdate();
/*    */     }
/* 90 */     catch (SQLException sqex) {
/*    */       
/* 92 */       throw new IOException(this.id + " " + sqex.getMessage(), sqex);
/*    */     }
/*    */     finally {
/*    */       
/* 96 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 97 */       DbConnector.returnConnection(dbcon);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\WoundMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
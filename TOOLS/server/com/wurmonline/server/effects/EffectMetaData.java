/*    */ package com.wurmonline.server.effects;
/*    */ 
/*    */ import com.wurmonline.server.DbConnector;
/*    */ import com.wurmonline.server.utils.DbUtilities;
/*    */ import java.io.IOException;
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EffectMetaData
/*    */ {
/*    */   private final long owner;
/*    */   private final short type;
/*    */   private final float posX;
/*    */   private final float posY;
/*    */   private final float posZ;
/*    */   private final long startTime;
/*    */   private static final String CREATE_EFFECT = "INSERT INTO EFFECTS(  OWNER,TYPE,POSX,POSY,POSZ,STARTTIME) VALUES(?,?,?,?,?,?)";
/*    */   
/*    */   public EffectMetaData(long aOwner, short aType, float aPosx, float aPosy, float aPosz, long aStartTime) {
/* 49 */     this.owner = aOwner;
/* 50 */     this.type = aType;
/* 51 */     this.posX = aPosx;
/* 52 */     this.posY = aPosy;
/* 53 */     this.posZ = aPosz;
/* 54 */     this.startTime = aStartTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public void save() throws IOException {
/* 59 */     Connection dbcon = null;
/* 60 */     PreparedStatement ps = null;
/*    */     
/*    */     try {
/* 63 */       dbcon = DbConnector.getItemDbCon();
/* 64 */       ps = dbcon.prepareStatement("INSERT INTO EFFECTS(  OWNER,TYPE,POSX,POSY,POSZ,STARTTIME) VALUES(?,?,?,?,?,?)");
/* 65 */       ps.setLong(1, this.owner);
/* 66 */       ps.setShort(2, this.type);
/* 67 */       ps.setFloat(3, this.posX);
/* 68 */       ps.setFloat(4, this.posY);
/* 69 */       ps.setFloat(5, this.posZ);
/* 70 */       ps.setLong(6, this.startTime);
/* 71 */       ps.executeUpdate();
/*    */     }
/* 73 */     catch (SQLException sqex) {
/*    */       
/* 75 */       throw new IOException(this.owner + " " + sqex.getMessage(), sqex);
/*    */     }
/*    */     finally {
/*    */       
/* 79 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 80 */       DbConnector.returnConnection(dbcon);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\effects\EffectMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
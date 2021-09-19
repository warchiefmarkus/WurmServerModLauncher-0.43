/*     */ package com.wurmonline.server.batchjobs;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.structures.DbDoor;
/*     */ import com.wurmonline.server.structures.DbWall;
/*     */ import com.wurmonline.server.structures.Door;
/*     */ import com.wurmonline.server.structures.FenceGate;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Structures;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.shared.constants.StructureStateEnum;
/*     */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
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
/*     */ public final class StructureBatchJob
/*     */ {
/*     */   private static final String DELETE_DOORS = "DELETE FROM DOORS WHERE LOCKID<=0";
/*     */   private static final String DELETE_GATES = "DELETE FROM GATES WHERE LOCKID<=0";
/*     */   private static final String LOAD_WALLS = "SELECT * FROM WALLS";
/*     */   private static final String LOAD_FENCES = "SELECT * FROM FENCES";
/*     */   private static final String updateGate = "UPDATE GATES SET ID=? WHERE ID=?";
/*  63 */   private static Logger logger = Logger.getLogger(StructureBatchJob.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void runBatch1() {
/*  75 */     logger.log(Level.INFO, "Running batch 1.");
/*     */     
/*     */     try {
/*  78 */       Map<Long, LinkedList<Door>> doors = new HashMap<>();
/*  79 */       Map<Long, LinkedList<Wall>> walls = new HashMap<>();
/*     */       
/*  81 */       Connection dbcon = DbConnector.getZonesDbCon();
/*  82 */       PreparedStatement psA = dbcon.prepareStatement("DELETE FROM DOORS WHERE LOCKID<=0");
/*  83 */       psA.executeUpdate();
/*  84 */       psA.close();
/*  85 */       PreparedStatement ps = dbcon.prepareStatement("SELECT * FROM WALLS");
/*  86 */       ResultSet rs = ps.executeQuery();
/*  87 */       while (rs.next()) {
/*     */ 
/*     */         
/*     */         try {
/*  91 */           DbWall dbWall = new DbWall(rs.getInt("ID"));
/*  92 */           ((Wall)dbWall).x1 = rs.getInt("STARTX");
/*  93 */           ((Wall)dbWall).x2 = rs.getInt("ENDX");
/*  94 */           ((Wall)dbWall).y1 = rs.getInt("STARTY");
/*  95 */           ((Wall)dbWall).y2 = rs.getInt("ENDY");
/*  96 */           ((Wall)dbWall).tilex = rs.getInt("TILEX");
/*  97 */           ((Wall)dbWall).tiley = rs.getInt("TILEY");
/*  98 */           ((Wall)dbWall).currentQL = rs.getFloat("ORIGINALQL");
/*  99 */           ((Wall)dbWall).originalQL = rs.getFloat("CURRENTQL");
/* 100 */           ((Wall)dbWall).lastUsed = rs.getLong("LASTMAINTAINED");
/* 101 */           ((Wall)dbWall).structureId = rs.getLong("STRUCTURE");
/* 102 */           ((Wall)dbWall).type = StructureTypeEnum.getTypeByINDEX(rs.getByte("TYPE"));
/* 103 */           ((Wall)dbWall).state = StructureStateEnum.getStateByValue(rs.getByte("STATE"));
/* 104 */           ((Wall)dbWall).damage = rs.getFloat("DAMAGE");
/* 105 */           dbWall.setColor(rs.getInt("COLOR"));
/* 106 */           dbWall.setIndoor(rs.getBoolean("ISINDOOR"));
/* 107 */           ((Wall)dbWall).heightOffset = rs.getInt("HEIGHTOFFSET");
/*     */           
/* 109 */           if (dbWall.getType() == StructureTypeEnum.DOOR || dbWall.getType() == StructureTypeEnum.DOUBLE_DOOR || dbWall
/* 110 */             .isArched())
/*     */           {
/* 112 */             LinkedList<Wall> wallist = walls.get(Long.valueOf(((Wall)dbWall).structureId));
/* 113 */             if (wallist == null) {
/*     */               
/* 115 */               wallist = new LinkedList<>();
/* 116 */               walls.put(Long.valueOf(((Wall)dbWall).structureId), wallist);
/*     */             } 
/* 118 */             wallist.add(dbWall);
/*     */             
/* 120 */             LinkedList<Door> doorlist = doors.get(Long.valueOf(((Wall)dbWall).structureId));
/* 121 */             if (doorlist == null) {
/*     */               
/* 123 */               doorlist = new LinkedList<>();
/* 124 */               doors.put(Long.valueOf(((Wall)dbWall).structureId), doorlist);
/*     */             } 
/* 126 */             boolean updated = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 180 */             DbDoor dbDoor = new DbDoor((Wall)dbWall);
/* 181 */             doorlist.add(dbDoor);
/* 182 */             doors.put(Long.valueOf(((Wall)dbWall).structureId), doorlist);
/*     */           }
/*     */         
/*     */         }
/* 186 */         catch (IOException iox) {
/*     */           
/* 188 */           logger.log(Level.INFO, "IOException");
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 193 */       ps.close();
/* 194 */       rs.close();
/*     */     }
/* 196 */     catch (SQLException sqx) {
/*     */       
/* 198 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     } 
/* 200 */     logger.log(Level.INFO, "Done running batch 1.");
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
/*     */   public static final void convertToNewPermissions() {
/* 251 */     logger.log(Level.INFO, "Converting Structures to New Permission System.");
/* 252 */     int structuresDone = 0;
/* 253 */     for (Structure structure : Structures.getAllStructures()) {
/*     */       
/* 255 */       if (structure.convertToNewPermissions())
/* 256 */         structuresDone++; 
/*     */     } 
/* 258 */     logger.log(Level.INFO, "Converted " + structuresDone + " structures to New Permissions System.");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void convertGatesToNewPermissions() {
/* 263 */     logger.log(Level.INFO, "Converting Gates to New Permission System.");
/* 264 */     int gatesDone = 0;
/* 265 */     for (FenceGate gate : FenceGate.getAllGates()) {
/*     */       
/* 267 */       if (gate.convertToNewPermissions())
/* 268 */         gatesDone++; 
/*     */     } 
/* 270 */     logger.log(Level.INFO, "Converted " + gatesDone + " gates to New Permissions System.");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void fixGatesForNewPermissions() {
/* 275 */     logger.log(Level.INFO, "fixing Gates for New Permission System.");
/* 276 */     int gatesDone = 0;
/* 277 */     for (FenceGate gate : FenceGate.getAllGates()) {
/*     */       
/* 279 */       if (gate.fixForNewPermissions())
/* 280 */         gatesDone++; 
/*     */     } 
/* 282 */     logger.log(Level.INFO, "Fixed " + gatesDone + " gates to New Permissions System.");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\batchjobs\StructureBatchJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
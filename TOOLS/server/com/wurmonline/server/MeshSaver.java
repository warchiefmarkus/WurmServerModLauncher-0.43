/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import java.io.IOException;
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
/*     */ final class MeshSaver
/*     */   implements Runnable
/*     */ {
/*  29 */   private static final Logger logger = Logger.getLogger(MeshSaver.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MeshIO iMapLayer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String iMapLayerName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int iNumberOfRowsPerCall;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MeshSaver(MeshIO aMapLayerToSave, String aMapLayerName, int aNumberOfRowsPerCall) {
/*  52 */     this.iMapLayer = aMapLayerToSave;
/*  53 */     this.iMapLayerName = aMapLayerName;
/*  54 */     this.iNumberOfRowsPerCall = aNumberOfRowsPerCall;
/*  55 */     logger.info("Created MeshSaver for map layer: '" + this.iMapLayerName + "', " + aMapLayerToSave + ", rowsPerCall: " + aNumberOfRowsPerCall);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MeshSaver(MeshIO aMapLayerToSave, String aMapLayerName) {
/*  66 */     this(aMapLayerToSave, aMapLayerName, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  77 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*  79 */       logger.finest("Running MeshSaver for calling MeshIO.saveDirtyRow() for '" + this.iMapLayerName + "', " + this.iMapLayer + ", rowsPerCall: " + this.iNumberOfRowsPerCall);
/*     */     }
/*     */     
/*     */     try {
/*  83 */       long now = System.nanoTime();
/*  84 */       int numberOfRowsSaved = this.iNumberOfRowsPerCall;
/*     */       
/*  86 */       if (this.iNumberOfRowsPerCall <= 0) {
/*     */         
/*  88 */         numberOfRowsSaved = this.iMapLayer.saveAllDirtyRows();
/*     */       }
/*  90 */       else if (this.iNumberOfRowsPerCall > this.iMapLayer.getSize()) {
/*     */         
/*  92 */         this.iMapLayer.saveAll();
/*     */       
/*     */       }
/*     */       else {
/*     */         
/*  97 */         for (int i = 0; i < this.iNumberOfRowsPerCall; i++) {
/*     */           
/*  99 */           if (this.iMapLayer.saveNextDirtyRow()) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 106 */       float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 107 */       if (lElapsedTime > (float)Constants.lagThreshold || logger.isLoggable(Level.FINER))
/*     */       {
/* 109 */         logger.info("Finished saving " + numberOfRowsSaved + " rows for '" + this.iMapLayerName + "', which took " + lElapsedTime + " millis.");
/*     */       
/*     */       }
/*     */     }
/* 113 */     catch (RuntimeException e) {
/*     */       
/* 115 */       logger.log(Level.WARNING, "Caught exception in MeshSaver while saving Mesh for '" + this.iMapLayerName + "' " + this.iMapLayer, e);
/* 116 */       throw e;
/*     */     }
/* 118 */     catch (IOException e) {
/*     */       
/* 120 */       logger.log(Level.WARNING, "Caught exception in MeshSaver while saving Mesh for '" + this.iMapLayerName + "' " + this.iMapLayer, e);
/* 121 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\MeshSaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
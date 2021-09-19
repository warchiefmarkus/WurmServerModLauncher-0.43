/*    */ package com.wurmonline.server.batchjobs;
/*    */ 
/*    */ import com.wurmonline.server.creatures.MineDoorPermission;
/*    */ import java.util.logging.Level;
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
/*    */ 
/*    */ 
/*    */ public class MineDoorBatchJob
/*    */ {
/* 33 */   private static Logger logger = Logger.getLogger(MineDoorBatchJob.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void convertToNewPermissions() {
/* 41 */     logger.log(Level.INFO, "Converting Mine Doors to New Permission System.");
/* 42 */     int minedoorsDone = 0;
/* 43 */     for (MineDoorPermission md : MineDoorPermission.getAllMineDoors()) {
/*    */       
/* 45 */       if (md.convertToNewPermissions())
/* 46 */         minedoorsDone++; 
/*    */     } 
/* 48 */     logger.log(Level.INFO, "Converted " + minedoorsDone + " Mine Doors to New Permissions System.");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\batchjobs\MineDoorBatchJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
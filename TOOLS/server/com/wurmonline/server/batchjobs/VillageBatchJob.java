/*     */ package com.wurmonline.server.batchjobs;
/*     */ 
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.VillageRole;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class VillageBatchJob
/*     */ {
/*  36 */   private static Logger logger = Logger.getLogger(VillageBatchJob.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void convertToNewRolePermissionSystem() {
/*  45 */     logger.log(Level.INFO, "Converting to New Village Permission System.");
/*  46 */     int villagesDone = 0;
/*  47 */     int rolesDone = 0;
/*  48 */     int failed = 0;
/*  49 */     for (Village v : Villages.getVillages()) {
/*     */       
/*  51 */       villagesDone++;
/*  52 */       for (VillageRole vr : v.getRoles()) {
/*     */         
/*  54 */         vr.convertSettings();
/*     */ 
/*     */         
/*     */         try {
/*  58 */           rolesDone++;
/*  59 */           vr.save();
/*     */         }
/*  61 */         catch (IOException ioe) {
/*     */           
/*  63 */           failed++;
/*  64 */           logger.log(Level.INFO, "Failed to save role " + vr.getName() + " for village " + v.getName() + ".", ioe);
/*     */         } 
/*     */       } 
/*     */     } 
/*  68 */     logger.log(Level.INFO, "Converted " + rolesDone + " roles in " + villagesDone + " villages to New Permissions System." + ((failed > 0) ? (" Failed " + failed + " saves") : ""));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fixNewRolePermissionSystem() {
/*  74 */     logger.log(Level.INFO, "fix for New Village Permission System.");
/*  75 */     int villagesDone = 0;
/*  76 */     int rolesDone = 0;
/*  77 */     int failed = 0;
/*  78 */     for (Village v : Villages.getVillages()) {
/*     */       
/*  80 */       villagesDone++;
/*  81 */       for (VillageRole vr : v.getRoles()) {
/*     */         
/*  83 */         boolean fixed = false;
/*  84 */         if (vr.mayBreed()) {
/*     */           
/*  86 */           vr.setCanBrand(true);
/*  87 */           fixed = true;
/*     */         } 
/*  89 */         if (vr.mayManageSettings()) {
/*     */           
/*  91 */           vr.setCanManageAllowedObjects(true);
/*  92 */           fixed = true;
/*     */         } 
/*     */         
/*  95 */         if (fixed) {
/*     */           
/*     */           try {
/*     */             
/*  99 */             rolesDone++;
/* 100 */             vr.save();
/*     */           }
/* 102 */           catch (IOException ioe) {
/*     */             
/* 104 */             failed++;
/* 105 */             logger.log(Level.INFO, "Failed to save role " + vr.getName() + " for village " + v.getName() + ".", ioe);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 110 */     logger.log(Level.INFO, "Fixed " + rolesDone + " roles in " + villagesDone + " villages to New Permissions System." + ((failed > 0) ? (" Failed " + failed + " saves") : ""));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\batchjobs\VillageBatchJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
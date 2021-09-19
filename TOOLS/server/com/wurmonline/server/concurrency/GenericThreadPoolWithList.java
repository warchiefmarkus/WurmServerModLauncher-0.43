/*     */ package com.wurmonline.server.concurrency;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class GenericThreadPoolWithList
/*     */ {
/*  34 */   private static Logger logger = Logger.getLogger(GenericThreadPoolWithList.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String VERSION = "$Revision: 1.0 $";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void multiThreadedPoll(List<? extends Pollable> lInputList, int aNumberOfTasks) {
/*  50 */     System.out.println("Polling banks");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     ExecutorService execSvc = Executors.newCachedThreadPool();
/*     */ 
/*     */     
/*  60 */     int lLastID = lInputList.size();
/*  61 */     int lFirstID = 0;
/*     */     
/*  63 */     List<? extends Callable<?>> toRun = new ArrayList();
/*  64 */     int lNumberOfTasks = Math.min(aNumberOfTasks, lInputList.size());
/*     */     
/*  66 */     for (int i = 1; i <= aNumberOfTasks;) {
/*     */       
/*  68 */       if (lNumberOfTasks <= i) {
/*     */         
/*  70 */         int m = lLastID * i / aNumberOfTasks;
/*  71 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/*  73 */           logger.log(Level.FINEST, i + " - First: " + lFirstID + ", last: " + m);
/*     */         }
/*  75 */         toRun.add(new GenericPollerWithList(lFirstID, m, lInputList));
/*  76 */         System.out.println("ADDED A TASK");
/*  77 */         lFirstID = m + 1;
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     long start = System.nanoTime();
/*     */     
/*     */     try {
/*  86 */       execSvc.invokeAll(toRun);
/*  87 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/*  89 */         logger.log(Level.FINEST, "invokeAll took " + ((float)(System.nanoTime() - start) / 1000000.0F) + "ms");
/*     */       }
/*     */     }
/*  92 */     catch (InterruptedException e) {
/*     */       
/*  94 */       logger.log(Level.WARNING, "task invocation interrupted", e);
/*     */     }
/*  96 */     catch (RejectedExecutionException e) {
/*     */       
/*  98 */       if (!execSvc.isShutdown())
/*     */       {
/* 100 */         logger.log(Level.WARNING, "task submission rejected", e);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 106 */     execSvc.shutdown();
/*     */ 
/*     */     
/* 109 */     if (execSvc instanceof ThreadPoolExecutor) {
/*     */       
/* 111 */       ThreadPoolExecutor tpe = (ThreadPoolExecutor)execSvc;
/* 112 */       if (logger.isLoggable(Level.FINE))
/*     */       {
/* 114 */         logger.log(Level.FINE, "ThreadPoolExecutor CorePoolSize: " + tpe.getCorePoolSize() + ", LargestPoolSize: " + tpe.getLargestPoolSize() + ", TaskCount: " + tpe
/* 115 */             .getTaskCount());
/*     */       }
/*     */     } 
/* 118 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 120 */       logger.log(Level.FINEST, "execSvc.isTerminated(): " + execSvc.isTerminated() + " took: " + ((float)(System.nanoTime() - start) / 1000000.0F) + "ms");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 126 */       if (!execSvc.awaitTermination(30L, TimeUnit.SECONDS))
/*     */       {
/* 128 */         logger.log(Level.WARNING, "ThreadPoolExceutor timed out instead of terminating");
/*     */       }
/*     */     }
/* 131 */     catch (InterruptedException e) {
/*     */       
/* 133 */       logger.log(Level.WARNING, "task awaitTermination interrupted", e);
/*     */     } 
/* 135 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*     */       
/* 138 */       logger.log(Level.FINEST, "execSvc.isTerminated(): " + execSvc.isTerminated() + " took: " + ((float)(System.nanoTime() - start) / 1000000.0F) + "ms");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\concurrency\GenericThreadPoolWithList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
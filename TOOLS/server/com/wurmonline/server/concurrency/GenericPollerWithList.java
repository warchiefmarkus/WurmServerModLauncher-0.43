/*     */ package com.wurmonline.server.concurrency;
/*     */ 
/*     */ import com.wurmonline.server.banks.Bank;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public class GenericPollerWithList<V>
/*     */   implements Callable
/*     */ {
/*  29 */   private static Logger logger = Logger.getLogger(GenericPollerWithList.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int iFirstID;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int iLastID;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<? extends Pollable> iTaskList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericPollerWithList(int aFirstID, int aLastID, List<? extends Pollable> aTaskList) {
/*  52 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*  54 */       logger.entering(GenericPollerWithList.class.getName(), "GenericPollerWithList()", new Object[] {
/*  55 */             Integer.valueOf(aFirstID), Integer.valueOf(aLastID), aTaskList
/*     */           }); } 
/*  57 */     if (aTaskList != null) {
/*     */       
/*  59 */       this.iTaskList = aTaskList;
/*     */     }
/*     */     else {
/*     */       
/*  63 */       throw new IllegalArgumentException("GenericPollerWithList TaskList argument must not be null");
/*     */     } 
/*     */     
/*  66 */     if (aFirstID < 0) {
/*     */       
/*  68 */       this.iFirstID = 0;
/*     */     }
/*     */     else {
/*     */       
/*  72 */       this.iFirstID = aFirstID;
/*     */     } 
/*  74 */     if (aLastID < this.iFirstID) {
/*     */       
/*  76 */       this.iLastID = this.iFirstID;
/*     */     }
/*  78 */     else if (aLastID > aTaskList.size()) {
/*     */       
/*  80 */       this.iLastID = aTaskList.size();
/*     */     }
/*     */     else {
/*     */       
/*  84 */       this.iLastID = aLastID;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long call() throws Exception {
/*  95 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*  97 */       logger.entering(GenericPollerWithList.class.getName(), "call()");
/*     */     }
/*  99 */     long start = System.nanoTime();
/* 100 */     System.out.println("TASK CALLED");
/*     */     
/* 102 */     for (int i = this.iFirstID; i < this.iLastID; i++) {
/*     */       
/* 104 */       Pollable lTask = this.iTaskList.get(i);
/*     */       
/* 106 */       if (lTask != null && lTask instanceof Bank) {
/*     */         
/* 108 */         ((Bank)lTask).poll(System.currentTimeMillis());
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 113 */         logger.log(Level.WARNING, "Unsupported Pollable Class: " + lTask);
/*     */       } 
/*     */     } 
/* 116 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 118 */       logger.log(Level.FINEST, "Tasks from " + this.iFirstID + " to " + this.iLastID + " took " + ((float)(System.nanoTime() - start) / 1000000.0F) + "ms");
/*     */     }
/*     */     
/* 121 */     return Long.valueOf(System.nanoTime() - start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "GenericPollerWithList + First ID: " + this.iFirstID + ", Last ID: " + this.iLastID + ", Number of Tasks: " + this.iTaskList.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\concurrency\GenericPollerWithList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
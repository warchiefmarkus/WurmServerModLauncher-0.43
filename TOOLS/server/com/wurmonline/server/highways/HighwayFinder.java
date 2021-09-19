/*     */ package com.wurmonline.server.highways;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
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
/*     */ public class HighwayFinder
/*     */   extends Thread
/*     */   implements MiscConstants
/*     */ {
/*  40 */   private static Logger logger = Logger.getLogger(HighwayFinder.class.getName());
/*     */   
/*  42 */   private static final ConcurrentLinkedDeque<PathToCalculate> pathingQueue = new ConcurrentLinkedDeque<>();
/*     */   private boolean shouldStop = false;
/*     */   private boolean sleeping = false;
/*  45 */   private int waystoneno = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HighwayFinder() {
/*  52 */     super("HighwayFinder-Thread");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  58 */     while (!this.shouldStop) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  63 */         PathToCalculate nextPath = getNextPathToCalculate();
/*  64 */         if (nextPath != null) {
/*     */           
/*  66 */           nextPath.calculate();
/*     */           
/*  68 */           this.sleeping = true;
/*  69 */           sleep(100L);
/*  70 */           this.sleeping = false;
/*     */           
/*     */           continue;
/*     */         } 
/*  74 */         this.sleeping = true;
/*  75 */         sleep(15000L);
/*  76 */         this.sleeping = false;
/*     */         
/*  78 */         int nextwaystone = this.waystoneno++;
/*  79 */         Item[] waystones = Items.getWaystones();
/*     */         
/*  81 */         if (nextwaystone >= waystones.length) {
/*     */           
/*  83 */           this.waystoneno = 0;
/*     */           
/*     */           continue;
/*     */         } 
/*  87 */         Item waystone = waystones[nextwaystone];
/*     */         
/*  89 */         Node startNode = Routes.getNode(waystone);
/*  90 */         pathingQueue.add(new PathToCalculate(null, startNode, null, (byte)0));
/*     */ 
/*     */       
/*     */       }
/*  94 */       catch (InterruptedException e) {
/*     */ 
/*     */         
/*  97 */         this.sleeping = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shouldStop() {
/* 104 */     this.shouldStop = true;
/*     */     
/* 106 */     interrupt();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isSleeping() {
/* 111 */     return this.sleeping;
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
/*     */   public static final void queueHighwayFinding(Creature creature, Node startNode, Village village, byte checkDir) {
/* 123 */     HighwayFinder highwayThread = Server.getInstance().getHighwayFinderThread();
/*     */     
/* 125 */     if (highwayThread != null) {
/*     */       
/* 127 */       pathingQueue.add(new PathToCalculate(creature, startNode, village, checkDir));
/*     */       
/* 129 */       if (highwayThread.isSleeping())
/*     */       {
/*     */         
/* 132 */         highwayThread.interrupt();
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
/*     */   private static final PathToCalculate getNextPathToCalculate() {
/* 144 */     return pathingQueue.pollFirst();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\HighwayFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
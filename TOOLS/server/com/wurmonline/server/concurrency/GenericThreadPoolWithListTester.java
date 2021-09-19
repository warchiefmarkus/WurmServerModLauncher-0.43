/*    */ package com.wurmonline.server.concurrency;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenericThreadPoolWithListTester
/*    */ {
/*    */   public static void main(String[] args) {
/*    */     try {
/* 41 */       Thread.sleep(1000L);
/*    */     }
/* 43 */     catch (InterruptedException e1) {
/*    */       
/* 45 */       e1.printStackTrace();
/*    */     } 
/* 47 */     long lLastID = 10000L;
/* 48 */     List<Pollable> lInputList = new ArrayList<>(10010); long j;
/* 49 */     for (j = 0L; j < 10000L; j++) {
/*    */       
/* 51 */       lInputList.add(new Pollable()
/*    */           {
/*    */             public void poll(long now)
/*    */             {
/* 55 */               Thread.yield();
/*    */             }
/*    */           });
/*    */     } 
/*    */     
/* 60 */     for (int lNumberOfTasks = 1; lNumberOfTasks < 50; lNumberOfTasks++)
/*    */     {
/* 62 */       GenericThreadPoolWithList.multiThreadedPoll(lInputList, lNumberOfTasks);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\concurrency\GenericThreadPoolWithListTester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
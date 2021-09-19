/*    */ package org.seamless.util;
/*    */ 
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.lang.management.ThreadMXBean;
/*    */ import java.util.Arrays;
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
/*    */ public class Threads
/*    */ {
/*    */   public static ThreadGroup getRootThreadGroup() {
/* 26 */     ThreadGroup tg = Thread.currentThread().getThreadGroup();
/*    */     ThreadGroup ptg;
/* 28 */     while ((ptg = tg.getParent()) != null)
/* 29 */       tg = ptg; 
/* 30 */     return tg;
/*    */   }
/*    */   
/*    */   public static Thread[] getAllThreads() {
/* 34 */     ThreadGroup root = getRootThreadGroup();
/* 35 */     ThreadMXBean thbean = ManagementFactory.getThreadMXBean();
/* 36 */     int nAlloc = thbean.getThreadCount();
/* 37 */     int n = 0;
/*    */     
/*    */     while (true) {
/* 40 */       nAlloc *= 2;
/* 41 */       Thread[] threads = new Thread[nAlloc];
/* 42 */       n = root.enumerate(threads, true);
/* 43 */       if (n != nAlloc)
/* 44 */         return Arrays.<Thread>copyOf(threads, n); 
/*    */     } 
/*    */   }
/*    */   public static Thread getThread(long id) {
/* 48 */     Thread[] threads = getAllThreads();
/* 49 */     for (Thread thread : threads) {
/* 50 */       if (thread.getId() == id)
/* 51 */         return thread; 
/* 52 */     }  return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\Threads.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
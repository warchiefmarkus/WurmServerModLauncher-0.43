/*    */ package com.wurmonline.server;
/*    */ 
/*    */ import com.wurmonline.shared.exceptions.WurmServerException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*    */ import javax.annotation.concurrent.GuardedBy;
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
/*    */ public final class Factions
/*    */ {
/* 28 */   private static Factions instance = null;
/*    */ 
/*    */   
/*    */   @GuardedBy("FACTIONS_RW_LOCK")
/*    */   private static Map<String, Faction> factions;
/*    */ 
/*    */   
/* 35 */   private static final ReentrantReadWriteLock FACTIONS_RW_LOCK = new ReentrantReadWriteLock();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Factions getInstance() {
/* 43 */     if (instance == null)
/* 44 */       instance = new Factions(); 
/* 45 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Factions() {
/* 53 */     FACTIONS_RW_LOCK.writeLock().lock();
/*    */     
/*    */     try {
/* 56 */       factions = new HashMap<>();
/*    */     }
/*    */     finally {
/*    */       
/* 60 */       FACTIONS_RW_LOCK.writeLock().unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void addFaction(Faction faction) {
/* 66 */     FACTIONS_RW_LOCK.writeLock().lock();
/*    */     
/*    */     try {
/* 69 */       factions.put(faction.getName(), faction);
/*    */     }
/*    */     finally {
/*    */       
/* 73 */       FACTIONS_RW_LOCK.writeLock().unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static Faction getFaction(String name) throws Exception {
/* 79 */     FACTIONS_RW_LOCK.readLock().lock();
/*    */     
/*    */     try {
/* 82 */       Faction toReturn = factions.get(name);
/* 83 */       if (toReturn == null)
/* 84 */         throw new WurmServerException("No faction with name " + name); 
/* 85 */       return toReturn;
/*    */     }
/*    */     finally {
/*    */       
/* 89 */       FACTIONS_RW_LOCK.readLock().unlock();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Factions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
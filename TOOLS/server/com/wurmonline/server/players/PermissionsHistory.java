/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ import java.util.LinkedList;
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
/*    */ public class PermissionsHistory
/*    */ {
/* 26 */   private LinkedList<PermissionsHistoryEntry> historyEntries = new LinkedList<>();
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
/*    */   void add(long eventTime, long playerId, String playerName, String event) {
/* 44 */     this.historyEntries.addFirst(new PermissionsHistoryEntry(eventTime, playerId, playerName, event.replace("\"", "'")));
/*    */   }
/*    */ 
/*    */   
/*    */   public PermissionsHistoryEntry[] getHistoryEvents() {
/* 49 */     return this.historyEntries.<PermissionsHistoryEntry>toArray(new PermissionsHistoryEntry[this.historyEntries.size()]);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getHistory(int numevents) {
/* 54 */     String[] hist = new String[0];
/* 55 */     int lHistorySize = this.historyEntries.size();
/* 56 */     if (lHistorySize > 0) {
/*    */       
/* 58 */       int numbersToFetch = Math.min(numevents, lHistorySize);
/*    */       
/* 60 */       hist = new String[numbersToFetch];
/* 61 */       PermissionsHistoryEntry[] events = getHistoryEvents();
/* 62 */       for (int x = 0; x < numbersToFetch; x++)
/*    */       {
/* 64 */         hist[x] = events[x].getLongDesc();
/*    */       }
/*    */     } 
/* 67 */     return hist;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PermissionsHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
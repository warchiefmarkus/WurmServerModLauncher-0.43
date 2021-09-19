/*    */ package com.wurmonline.server;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.util.Date;
/*    */ import net.jcip.annotations.Immutable;
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
/*    */ @Immutable
/*    */ public final class HistoryEvent
/*    */   implements Comparable<HistoryEvent>
/*    */ {
/* 36 */   private final DateFormat df = DateFormat.getDateTimeInstance();
/*    */ 
/*    */   
/*    */   public final long time;
/*    */ 
/*    */   
/*    */   public final String performer;
/*    */ 
/*    */   
/*    */   public final String event;
/*    */ 
/*    */   
/*    */   public final int identifier;
/*    */ 
/*    */   
/*    */   public HistoryEvent(long aTime, String aPerformer, String aEvent, int aIdentifier) {
/* 52 */     this.time = aTime;
/* 53 */     this.performer = aPerformer;
/* 54 */     this.event = aEvent;
/* 55 */     this.identifier = aIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDate() {
/* 66 */     return this.df.format(new Date(this.time));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLongDesc() {
/* 76 */     if (this.performer == null || this.performer.length() == 0)
/* 77 */       return getDate() + "  " + this.event; 
/* 78 */     return getDate() + "  " + this.performer + " " + this.event;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(HistoryEvent he) {
/* 89 */     return Long.compare(this.time, he.time);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 98 */     return "HistoryEvent [" + getLongDesc() + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\HistoryEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
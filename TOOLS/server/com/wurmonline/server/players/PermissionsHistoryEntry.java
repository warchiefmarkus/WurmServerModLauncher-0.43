/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
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
/*     */ public class PermissionsHistoryEntry
/*     */   implements Comparable<PermissionsHistoryEntry>
/*     */ {
/*  30 */   private static final DateFormat df = DateFormat.getDateTimeInstance();
/*     */   
/*     */   private final long eventDate;
/*     */   
/*     */   private final long playerId;
/*     */   
/*     */   public final String performer;
/*     */   public final String event;
/*     */   
/*     */   public PermissionsHistoryEntry(long eventDate, long playerId, String performer, String event) {
/*  40 */     this.eventDate = eventDate;
/*  41 */     this.playerId = playerId;
/*  42 */     this.performer = performer;
/*  43 */     this.event = event;
/*     */   }
/*     */ 
/*     */   
/*     */   long getEventDate() {
/*  48 */     return this.eventDate;
/*     */   }
/*     */ 
/*     */   
/*     */   String getPlayerName() {
/*  53 */     return this.performer;
/*     */   }
/*     */ 
/*     */   
/*     */   String getEvent() {
/*  58 */     return this.event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDate() {
/*  69 */     return df.format(new Date(this.eventDate));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/*  74 */     return this.playerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLongDesc() {
/*  83 */     if (this.performer == null || this.performer.length() == 0)
/*  84 */       return getDate() + "  " + this.event; 
/*  85 */     return getDate() + "  " + this.performer + " " + this.event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(PermissionsHistoryEntry he) {
/*  96 */     return Long.compare(this.eventDate, he.eventDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return "PermissionsHistoryEntry [" + getLongDesc() + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PermissionsHistoryEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
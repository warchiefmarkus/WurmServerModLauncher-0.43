/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ public class CommunicationsException
/*     */   extends SQLException
/*     */   implements StreamingNotifiable
/*     */ {
/*  44 */   private String exceptionMessage = null;
/*     */   
/*     */   private boolean streamingResultSetInPlay = false;
/*     */   
/*     */   private ConnectionImpl conn;
/*     */   
/*     */   private long lastPacketSentTimeMs;
/*     */   
/*     */   private long lastPacketReceivedTimeMs;
/*     */   
/*     */   private Exception underlyingException;
/*     */   
/*     */   public CommunicationsException(ConnectionImpl conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException) {
/*  57 */     this.conn = conn;
/*  58 */     this.lastPacketReceivedTimeMs = lastPacketReceivedTimeMs;
/*  59 */     this.lastPacketSentTimeMs = lastPacketSentTimeMs;
/*  60 */     this.underlyingException = underlyingException;
/*     */     
/*  62 */     if (underlyingException != null) {
/*  63 */       initCause(underlyingException);
/*     */     }
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
/*     */   
/*     */   public String getMessage() {
/*  77 */     if (this.exceptionMessage == null) {
/*  78 */       this.exceptionMessage = SQLError.createLinkFailureMessageBasedOnHeuristics(this.conn, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, this.underlyingException, this.streamingResultSetInPlay);
/*     */ 
/*     */       
/*  81 */       this.conn = null;
/*  82 */       this.underlyingException = null;
/*     */     } 
/*  84 */     return this.exceptionMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSQLState() {
/*  93 */     return "08S01";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWasStreamingResults() {
/* 100 */     this.streamingResultSetInPlay = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\CommunicationsException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
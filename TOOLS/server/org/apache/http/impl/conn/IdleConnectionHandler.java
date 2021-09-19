/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpConnection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class IdleConnectionHandler
/*     */ {
/*  53 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final Map<HttpConnection, TimeValues> connectionToTimes;
/*     */ 
/*     */ 
/*     */   
/*     */   public IdleConnectionHandler() {
/*  61 */     this.connectionToTimes = new HashMap<HttpConnection, TimeValues>();
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
/*     */   public void add(HttpConnection connection, long validDuration, TimeUnit unit) {
/*  74 */     long timeAdded = System.currentTimeMillis();
/*     */     
/*  76 */     if (this.log.isDebugEnabled()) {
/*  77 */       this.log.debug("Adding connection at: " + timeAdded);
/*     */     }
/*     */     
/*  80 */     this.connectionToTimes.put(connection, new TimeValues(timeAdded, validDuration, unit));
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
/*     */   public boolean remove(HttpConnection connection) {
/*  92 */     TimeValues times = this.connectionToTimes.remove(connection);
/*  93 */     if (times == null) {
/*  94 */       this.log.warn("Removing a connection that never existed!");
/*  95 */       return true;
/*     */     } 
/*  97 */     return (System.currentTimeMillis() <= times.timeExpires);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAll() {
/* 105 */     this.connectionToTimes.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idleTime) {
/* 116 */     long idleTimeout = System.currentTimeMillis() - idleTime;
/*     */     
/* 118 */     if (this.log.isDebugEnabled()) {
/* 119 */       this.log.debug("Checking for connections, idle timeout: " + idleTimeout);
/*     */     }
/*     */     
/* 122 */     for (Map.Entry<HttpConnection, TimeValues> entry : this.connectionToTimes.entrySet()) {
/* 123 */       HttpConnection conn = entry.getKey();
/* 124 */       TimeValues times = entry.getValue();
/* 125 */       long connectionTime = times.timeAdded;
/* 126 */       if (connectionTime <= idleTimeout) {
/* 127 */         if (this.log.isDebugEnabled()) {
/* 128 */           this.log.debug("Closing idle connection, connection time: " + connectionTime);
/*     */         }
/*     */         try {
/* 131 */           conn.close();
/* 132 */         } catch (IOException ex) {
/* 133 */           this.log.debug("I/O error closing connection", ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 141 */     long now = System.currentTimeMillis();
/* 142 */     if (this.log.isDebugEnabled()) {
/* 143 */       this.log.debug("Checking for expired connections, now: " + now);
/*     */     }
/*     */     
/* 146 */     for (Map.Entry<HttpConnection, TimeValues> entry : this.connectionToTimes.entrySet()) {
/* 147 */       HttpConnection conn = entry.getKey();
/* 148 */       TimeValues times = entry.getValue();
/* 149 */       if (times.timeExpires <= now) {
/* 150 */         if (this.log.isDebugEnabled()) {
/* 151 */           this.log.debug("Closing connection, expired @: " + times.timeExpires);
/*     */         }
/*     */         try {
/* 154 */           conn.close();
/* 155 */         } catch (IOException ex) {
/* 156 */           this.log.debug("I/O error closing connection", ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TimeValues
/*     */   {
/*     */     private final long timeAdded;
/*     */     
/*     */     private final long timeExpires;
/*     */ 
/*     */     
/*     */     TimeValues(long now, long validDuration, TimeUnit validUnit) {
/* 172 */       this.timeAdded = now;
/* 173 */       if (validDuration > 0L) {
/* 174 */         this.timeExpires = now + validUnit.toMillis(validDuration);
/*     */       } else {
/* 176 */         this.timeExpires = Long.MAX_VALUE;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\IdleConnectionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*     */ public class Connection
/*     */ {
/*     */   public static class StatusInfo
/*     */   {
/*     */     private Connection.Status status;
/*     */     private long uptimeSeconds;
/*     */     private Connection.Error lastError;
/*     */     
/*     */     public StatusInfo(Connection.Status status, UnsignedIntegerFourBytes uptime, Connection.Error lastError) {
/*  32 */       this(status, uptime.getValue().longValue(), lastError);
/*     */     }
/*     */     
/*     */     public StatusInfo(Connection.Status status, long uptimeSeconds, Connection.Error lastError) {
/*  36 */       this.status = status;
/*  37 */       this.uptimeSeconds = uptimeSeconds;
/*  38 */       this.lastError = lastError;
/*     */     }
/*     */     
/*     */     public Connection.Status getStatus() {
/*  42 */       return this.status;
/*     */     }
/*     */     
/*     */     public long getUptimeSeconds() {
/*  46 */       return this.uptimeSeconds;
/*     */     }
/*     */     
/*     */     public UnsignedIntegerFourBytes getUptime() {
/*  50 */       return new UnsignedIntegerFourBytes(getUptimeSeconds());
/*     */     }
/*     */     
/*     */     public Connection.Error getLastError() {
/*  54 */       return this.lastError;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  59 */       if (this == o) return true; 
/*  60 */       if (o == null || getClass() != o.getClass()) return false;
/*     */       
/*  62 */       StatusInfo that = (StatusInfo)o;
/*     */       
/*  64 */       if (this.uptimeSeconds != that.uptimeSeconds) return false; 
/*  65 */       if (this.lastError != that.lastError) return false; 
/*  66 */       if (this.status != that.status) return false;
/*     */       
/*  68 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  73 */       int result = this.status.hashCode();
/*  74 */       result = 31 * result + (int)(this.uptimeSeconds ^ this.uptimeSeconds >>> 32L);
/*  75 */       result = 31 * result + this.lastError.hashCode();
/*  76 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  81 */       return "(" + getClass().getSimpleName() + ") " + getStatus();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/*  89 */     Unconfigured,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     IP_Routed,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     IP_Bridged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Status
/*     */   {
/* 107 */     Unconfigured,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     Connecting,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     Connected,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     PendingDisconnect,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     Disconnecting,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     Disconnected;
/*     */   }
/*     */   
/*     */   public enum Error {
/* 141 */     ERROR_NONE,
/* 142 */     ERROR_COMMAND_ABORTED,
/* 143 */     ERROR_NOT_ENABLED_FOR_INTERNET,
/* 144 */     ERROR_USER_DISCONNECT,
/* 145 */     ERROR_ISP_DISCONNECT,
/* 146 */     ERROR_IDLE_DISCONNECT,
/* 147 */     ERROR_FORCED_DISCONNECT,
/* 148 */     ERROR_NO_CARRIER,
/* 149 */     ERROR_IP_CONFIGURATION,
/* 150 */     ERROR_UNKNOWN;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\Connection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
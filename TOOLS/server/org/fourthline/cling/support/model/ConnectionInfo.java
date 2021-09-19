/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import org.fourthline.cling.model.ServiceReference;
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
/*     */ public class ConnectionInfo
/*     */ {
/*     */   protected final int connectionID;
/*     */   protected final int rcsID;
/*     */   protected final int avTransportID;
/*     */   protected final ProtocolInfo protocolInfo;
/*     */   protected final ServiceReference peerConnectionManager;
/*     */   protected final int peerConnectionID;
/*     */   protected final Direction direction;
/*     */   
/*     */   public enum Status
/*     */   {
/*  29 */     OK,
/*  30 */     ContentFormatMismatch,
/*  31 */     InsufficientBandwidth,
/*  32 */     UnreliableChannel,
/*  33 */     Unknown;
/*     */   }
/*     */   
/*     */   public enum Direction {
/*  37 */     Output,
/*  38 */     Input;
/*     */     
/*     */     public Direction getOpposite() {
/*  41 */       return equals(Output) ? Input : Output;
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
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected Status connectionStatus = Status.Unknown;
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
/*     */   public ConnectionInfo() {
/*  81 */     this(0, 0, 0, null, null, -1, Direction.Input, Status.Unknown);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionInfo(int connectionID, int rcsID, int avTransportID, ProtocolInfo protocolInfo, ServiceReference peerConnectionManager, int peerConnectionID, Direction direction, Status connectionStatus) {
/*  90 */     this.connectionID = connectionID;
/*  91 */     this.rcsID = rcsID;
/*  92 */     this.avTransportID = avTransportID;
/*  93 */     this.protocolInfo = protocolInfo;
/*  94 */     this.peerConnectionManager = peerConnectionManager;
/*  95 */     this.peerConnectionID = peerConnectionID;
/*  96 */     this.direction = direction;
/*  97 */     this.connectionStatus = connectionStatus;
/*     */   }
/*     */   
/*     */   public int getConnectionID() {
/* 101 */     return this.connectionID;
/*     */   }
/*     */   
/*     */   public int getRcsID() {
/* 105 */     return this.rcsID;
/*     */   }
/*     */   
/*     */   public int getAvTransportID() {
/* 109 */     return this.avTransportID;
/*     */   }
/*     */   
/*     */   public ProtocolInfo getProtocolInfo() {
/* 113 */     return this.protocolInfo;
/*     */   }
/*     */   
/*     */   public ServiceReference getPeerConnectionManager() {
/* 117 */     return this.peerConnectionManager;
/*     */   }
/*     */   
/*     */   public int getPeerConnectionID() {
/* 121 */     return this.peerConnectionID;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/* 125 */     return this.direction;
/*     */   }
/*     */   
/*     */   public synchronized Status getConnectionStatus() {
/* 129 */     return this.connectionStatus;
/*     */   }
/*     */   
/*     */   public synchronized void setConnectionStatus(Status connectionStatus) {
/* 133 */     this.connectionStatus = connectionStatus;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 138 */     if (this == o) return true; 
/* 139 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 141 */     ConnectionInfo that = (ConnectionInfo)o;
/*     */     
/* 143 */     if (this.avTransportID != that.avTransportID) return false; 
/* 144 */     if (this.connectionID != that.connectionID) return false; 
/* 145 */     if (this.peerConnectionID != that.peerConnectionID) return false; 
/* 146 */     if (this.rcsID != that.rcsID) return false; 
/* 147 */     if (this.connectionStatus != that.connectionStatus) return false; 
/* 148 */     if (this.direction != that.direction) return false; 
/* 149 */     if ((this.peerConnectionManager != null) ? !this.peerConnectionManager.equals(that.peerConnectionManager) : (that.peerConnectionManager != null))
/* 150 */       return false; 
/* 151 */     if ((this.protocolInfo != null) ? !this.protocolInfo.equals(that.protocolInfo) : (that.protocolInfo != null)) return false;
/*     */     
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 158 */     int result = this.connectionID;
/* 159 */     result = 31 * result + this.rcsID;
/* 160 */     result = 31 * result + this.avTransportID;
/* 161 */     result = 31 * result + ((this.protocolInfo != null) ? this.protocolInfo.hashCode() : 0);
/* 162 */     result = 31 * result + ((this.peerConnectionManager != null) ? this.peerConnectionManager.hashCode() : 0);
/* 163 */     result = 31 * result + this.peerConnectionID;
/* 164 */     result = 31 * result + this.direction.hashCode();
/* 165 */     result = 31 * result + this.connectionStatus.hashCode();
/* 166 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     return "(" + getClass().getSimpleName() + ") ID: " + getConnectionID() + ", Status: " + getConnectionStatus();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\ConnectionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
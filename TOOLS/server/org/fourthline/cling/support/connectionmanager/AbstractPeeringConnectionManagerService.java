/*     */ package org.fourthline.cling.support.connectionmanager;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.binding.annotations.UpnpAction;
/*     */ import org.fourthline.cling.binding.annotations.UpnpInputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
/*     */ import org.fourthline.cling.controlpoint.ControlPoint;
/*     */ import org.fourthline.cling.model.ServiceReference;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.model.types.csv.CSV;
/*     */ import org.fourthline.cling.support.connectionmanager.callback.ConnectionComplete;
/*     */ import org.fourthline.cling.support.connectionmanager.callback.PrepareForConnection;
/*     */ import org.fourthline.cling.support.model.ConnectionInfo;
/*     */ import org.fourthline.cling.support.model.ProtocolInfo;
/*     */ import org.fourthline.cling.support.model.ProtocolInfos;
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
/*     */ public abstract class AbstractPeeringConnectionManagerService
/*     */   extends ConnectionManagerService
/*     */ {
/*  47 */   private static final Logger log = Logger.getLogger(AbstractPeeringConnectionManagerService.class.getName());
/*     */   
/*     */   protected AbstractPeeringConnectionManagerService(ConnectionInfo... activeConnections) {
/*  50 */     super(activeConnections);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractPeeringConnectionManagerService(ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo, ConnectionInfo... activeConnections) {
/*  55 */     super(sourceProtocolInfo, sinkProtocolInfo, activeConnections);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPeeringConnectionManagerService(PropertyChangeSupport propertyChangeSupport, ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo, ConnectionInfo... activeConnections) {
/*  61 */     super(propertyChangeSupport, sourceProtocolInfo, sinkProtocolInfo, activeConnections);
/*     */   }
/*     */   
/*     */   protected synchronized int getNewConnectionId() {
/*  65 */     int currentHighestID = -1;
/*  66 */     for (Integer key : this.activeConnections.keySet()) {
/*  67 */       if (key.intValue() > currentHighestID) currentHighestID = key.intValue(); 
/*     */     } 
/*  69 */     return ++currentHighestID;
/*     */   }
/*     */   
/*     */   protected synchronized void storeConnection(ConnectionInfo info) {
/*  73 */     CSV<UnsignedIntegerFourBytes> oldConnectionIDs = getCurrentConnectionIDs();
/*  74 */     this.activeConnections.put(Integer.valueOf(info.getConnectionID()), info);
/*  75 */     log.fine("Connection stored, firing event: " + info.getConnectionID());
/*  76 */     CSV<UnsignedIntegerFourBytes> newConnectionIDs = getCurrentConnectionIDs();
/*  77 */     getPropertyChangeSupport().firePropertyChange("CurrentConnectionIDs", oldConnectionIDs, newConnectionIDs);
/*     */   }
/*     */   
/*     */   protected synchronized void removeConnection(int connectionID) {
/*  81 */     CSV<UnsignedIntegerFourBytes> oldConnectionIDs = getCurrentConnectionIDs();
/*  82 */     this.activeConnections.remove(Integer.valueOf(connectionID));
/*  83 */     log.fine("Connection removed, firing event: " + connectionID);
/*  84 */     CSV<UnsignedIntegerFourBytes> newConnectionIDs = getCurrentConnectionIDs();
/*  85 */     getPropertyChangeSupport().firePropertyChange("CurrentConnectionIDs", oldConnectionIDs, newConnectionIDs);
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
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "ConnectionID", stateVariable = "A_ARG_TYPE_ConnectionID", getterName = "getConnectionID"), @UpnpOutputArgument(name = "AVTransportID", stateVariable = "A_ARG_TYPE_AVTransportID", getterName = "getAvTransportID"), @UpnpOutputArgument(name = "RcsID", stateVariable = "A_ARG_TYPE_RcsID", getterName = "getRcsID")})
/*     */   public synchronized ConnectionInfo prepareForConnection(@UpnpInputArgument(name = "RemoteProtocolInfo", stateVariable = "A_ARG_TYPE_ProtocolInfo") ProtocolInfo remoteProtocolInfo, @UpnpInputArgument(name = "PeerConnectionManager", stateVariable = "A_ARG_TYPE_ConnectionManager") ServiceReference peerConnectionManager, @UpnpInputArgument(name = "PeerConnectionID", stateVariable = "A_ARG_TYPE_ConnectionID") int peerConnectionId, @UpnpInputArgument(name = "Direction", stateVariable = "A_ARG_TYPE_Direction") String direction) throws ActionException {
/*     */     ConnectionInfo.Direction dir;
/* 100 */     int connectionId = getNewConnectionId();
/*     */ 
/*     */     
/*     */     try {
/* 104 */       dir = ConnectionInfo.Direction.valueOf(direction);
/* 105 */     } catch (Exception ex) {
/* 106 */       throw new ConnectionManagerException(ErrorCode.ARGUMENT_VALUE_INVALID, "Unsupported direction: " + direction);
/*     */     } 
/*     */     
/* 109 */     log.fine("Preparing for connection with local new ID " + connectionId + " and peer connection ID: " + peerConnectionId);
/*     */     
/* 111 */     ConnectionInfo newConnectionInfo = createConnection(connectionId, peerConnectionId, peerConnectionManager, dir, remoteProtocolInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     storeConnection(newConnectionInfo);
/*     */     
/* 121 */     return newConnectionInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public synchronized void connectionComplete(@UpnpInputArgument(name = "ConnectionID", stateVariable = "A_ARG_TYPE_ConnectionID") int connectionID) throws ActionException {
/* 127 */     ConnectionInfo info = getCurrentConnectionInfo(connectionID);
/* 128 */     log.fine("Closing connection ID " + connectionID);
/* 129 */     closeConnection(info);
/* 130 */     removeConnection(connectionID);
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
/*     */ 
/*     */   
/*     */   public synchronized int createConnectionWithPeer(ServiceReference localServiceReference, ControlPoint controlPoint, final Service peerService, final ProtocolInfo protInfo, final ConnectionInfo.Direction direction) {
/* 148 */     final int localConnectionID = getNewConnectionId();
/*     */     
/* 150 */     log.fine("Creating new connection ID " + localConnectionID + " with peer: " + peerService);
/* 151 */     final boolean[] failed = new boolean[1];
/* 152 */     (new PrepareForConnection(peerService, controlPoint, protInfo, localServiceReference, localConnectionID, direction)
/*     */       {
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
/*     */         public void received(ActionInvocation invocation, int peerConnectionID, int rcsID, int avTransportID)
/*     */         {
/* 169 */           ConnectionInfo info = new ConnectionInfo(localConnectionID, rcsID, avTransportID, protInfo, peerService.getReference(), peerConnectionID, direction.getOpposite(), ConnectionInfo.Status.OK);
/*     */ 
/*     */           
/* 172 */           AbstractPeeringConnectionManagerService.this.storeConnection(info);
/*     */         }
/*     */ 
/*     */         
/*     */         public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
/* 177 */           AbstractPeeringConnectionManagerService.this.peerFailure(invocation, operation, defaultMsg);
/*     */ 
/*     */           
/* 180 */           failed[0] = true;
/*     */         }
/* 182 */       }).run();
/*     */     
/* 184 */     return failed[0] ? -1 : localConnectionID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void closeConnectionWithPeer(ControlPoint controlPoint, Service peerService, int connectionID) throws ActionException {
/* 193 */     closeConnectionWithPeer(controlPoint, peerService, getCurrentConnectionInfo(connectionID));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void closeConnectionWithPeer(ControlPoint controlPoint, Service peerService, final ConnectionInfo connectionInfo) throws ActionException {
/* 204 */     log.fine("Closing connection ID " + connectionInfo.getConnectionID() + " with peer: " + peerService);
/* 205 */     (new ConnectionComplete(peerService, controlPoint, connectionInfo
/*     */ 
/*     */         
/* 208 */         .getPeerConnectionID())
/*     */       {
/*     */         
/*     */         public void success(ActionInvocation invocation)
/*     */         {
/* 213 */           AbstractPeeringConnectionManagerService.this.removeConnection(connectionInfo.getConnectionID());
/*     */         }
/*     */ 
/*     */         
/*     */         public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
/* 218 */           AbstractPeeringConnectionManagerService.this.peerFailure(invocation, operation, defaultMsg);
/*     */         }
/* 222 */       }).run();
/*     */   }
/*     */   
/*     */   protected abstract ConnectionInfo createConnection(int paramInt1, int paramInt2, ServiceReference paramServiceReference, ConnectionInfo.Direction paramDirection, ProtocolInfo paramProtocolInfo) throws ActionException;
/*     */   
/*     */   protected abstract void closeConnection(ConnectionInfo paramConnectionInfo);
/*     */   
/*     */   protected abstract void peerFailure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse, String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\AbstractPeeringConnectionManagerService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.fourthline.cling.support.connectionmanager;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.binding.annotations.UpnpAction;
/*     */ import org.fourthline.cling.binding.annotations.UpnpInputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpService;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceId;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceType;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariable;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariables;
/*     */ import org.fourthline.cling.model.ServiceReference;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.model.types.csv.CSV;
/*     */ import org.fourthline.cling.model.types.csv.CSVUnsignedIntegerFourBytes;
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
/*     */ @UpnpService(serviceId = @UpnpServiceId("ConnectionManager"), serviceType = @UpnpServiceType(value = "ConnectionManager", version = 1), stringConvertibleTypes = {ProtocolInfo.class, ProtocolInfos.class, ServiceReference.class})
/*     */ @UpnpStateVariables({@UpnpStateVariable(name = "SourceProtocolInfo", datatype = "string"), @UpnpStateVariable(name = "SinkProtocolInfo", datatype = "string"), @UpnpStateVariable(name = "CurrentConnectionIDs", datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_ConnectionStatus", allowedValuesEnum = ConnectionInfo.Status.class, sendEvents = false), @UpnpStateVariable(name = "A_ARG_TYPE_ConnectionManager", datatype = "string", sendEvents = false), @UpnpStateVariable(name = "A_ARG_TYPE_Direction", allowedValuesEnum = ConnectionInfo.Direction.class, sendEvents = false), @UpnpStateVariable(name = "A_ARG_TYPE_ProtocolInfo", datatype = "string", sendEvents = false), @UpnpStateVariable(name = "A_ARG_TYPE_ConnectionID", datatype = "i4", sendEvents = false), @UpnpStateVariable(name = "A_ARG_TYPE_AVTransportID", datatype = "i4", sendEvents = false), @UpnpStateVariable(name = "A_ARG_TYPE_RcsID", datatype = "i4", sendEvents = false)})
/*     */ public class ConnectionManagerService
/*     */ {
/*  65 */   private static final Logger log = Logger.getLogger(ConnectionManagerService.class.getName());
/*     */   
/*     */   protected final PropertyChangeSupport propertyChangeSupport;
/*  68 */   protected final Map<Integer, ConnectionInfo> activeConnections = new ConcurrentHashMap<>();
/*     */   
/*     */   protected final ProtocolInfos sourceProtocolInfo;
/*     */   
/*     */   protected final ProtocolInfos sinkProtocolInfo;
/*     */ 
/*     */   
/*     */   public ConnectionManagerService() {
/*  76 */     this(new ConnectionInfo[] { new ConnectionInfo() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionManagerService(ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo) {
/*  83 */     this(sourceProtocolInfo, sinkProtocolInfo, new ConnectionInfo[] { new ConnectionInfo() });
/*     */   }
/*     */   
/*     */   public ConnectionManagerService(ConnectionInfo... activeConnections) {
/*  87 */     this(null, new ProtocolInfos(new ProtocolInfo[0]), new ProtocolInfos(new ProtocolInfo[0]), activeConnections);
/*     */   }
/*     */   
/*     */   public ConnectionManagerService(ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo, ConnectionInfo... activeConnections) {
/*  91 */     this(null, sourceProtocolInfo, sinkProtocolInfo, activeConnections);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionManagerService(PropertyChangeSupport propertyChangeSupport, ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo, ConnectionInfo... activeConnections) {
/*  97 */     this.propertyChangeSupport = (propertyChangeSupport == null) ? new PropertyChangeSupport(this) : propertyChangeSupport;
/*     */ 
/*     */ 
/*     */     
/* 101 */     this.sourceProtocolInfo = sourceProtocolInfo;
/* 102 */     this.sinkProtocolInfo = sinkProtocolInfo;
/*     */     
/* 104 */     for (ConnectionInfo activeConnection : activeConnections) {
/* 105 */       this.activeConnections.put(Integer.valueOf(activeConnection.getConnectionID()), activeConnection);
/*     */     }
/*     */   }
/*     */   
/*     */   public PropertyChangeSupport getPropertyChangeSupport() {
/* 110 */     return this.propertyChangeSupport;
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
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "RcsID", getterName = "getRcsID"), @UpnpOutputArgument(name = "AVTransportID", getterName = "getAvTransportID"), @UpnpOutputArgument(name = "ProtocolInfo", getterName = "getProtocolInfo"), @UpnpOutputArgument(name = "PeerConnectionManager", stateVariable = "A_ARG_TYPE_ConnectionManager", getterName = "getPeerConnectionManager"), @UpnpOutputArgument(name = "PeerConnectionID", stateVariable = "A_ARG_TYPE_ConnectionID", getterName = "getPeerConnectionID"), @UpnpOutputArgument(name = "Direction", getterName = "getDirection"), @UpnpOutputArgument(name = "Status", stateVariable = "A_ARG_TYPE_ConnectionStatus", getterName = "getConnectionStatus")})
/*     */   public synchronized ConnectionInfo getCurrentConnectionInfo(@UpnpInputArgument(name = "ConnectionID") int connectionId) throws ActionException {
/* 124 */     log.fine("Getting connection information of connection ID: " + connectionId);
/*     */     ConnectionInfo info;
/* 126 */     if ((info = this.activeConnections.get(Integer.valueOf(connectionId))) == null) {
/* 127 */       throw new ConnectionManagerException(ConnectionManagerErrorCode.INVALID_CONNECTION_REFERENCE, "Non-active connection ID: " + connectionId);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "ConnectionIDs")})
/*     */   public synchronized CSV<UnsignedIntegerFourBytes> getCurrentConnectionIDs() {
/* 139 */     CSVUnsignedIntegerFourBytes cSVUnsignedIntegerFourBytes = new CSVUnsignedIntegerFourBytes();
/* 140 */     for (Integer connectionID : this.activeConnections.keySet()) {
/* 141 */       cSVUnsignedIntegerFourBytes.add(new UnsignedIntegerFourBytes(connectionID.intValue()));
/*     */     }
/* 143 */     log.fine("Returning current connection IDs: " + cSVUnsignedIntegerFourBytes.size());
/* 144 */     return (CSV<UnsignedIntegerFourBytes>)cSVUnsignedIntegerFourBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "Source", stateVariable = "SourceProtocolInfo", getterName = "getSourceProtocolInfo"), @UpnpOutputArgument(name = "Sink", stateVariable = "SinkProtocolInfo", getterName = "getSinkProtocolInfo")})
/*     */   public synchronized void getProtocolInfo() throws ActionException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized ProtocolInfos getSourceProtocolInfo() {
/* 156 */     return this.sourceProtocolInfo;
/*     */   }
/*     */   
/*     */   public synchronized ProtocolInfos getSinkProtocolInfo() {
/* 160 */     return this.sinkProtocolInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\ConnectionManagerService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
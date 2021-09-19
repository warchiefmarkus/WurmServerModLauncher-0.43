/*     */ package org.fourthline.cling.support.avtransport.impl;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.support.avtransport.AVTransportErrorCode;
/*     */ import org.fourthline.cling.support.avtransport.AVTransportException;
/*     */ import org.fourthline.cling.support.avtransport.AbstractAVTransportService;
/*     */ import org.fourthline.cling.support.avtransport.impl.state.AbstractState;
/*     */ import org.fourthline.cling.support.lastchange.LastChange;
/*     */ import org.fourthline.cling.support.model.AVTransport;
/*     */ import org.fourthline.cling.support.model.DeviceCapabilities;
/*     */ import org.fourthline.cling.support.model.MediaInfo;
/*     */ import org.fourthline.cling.support.model.PlayMode;
/*     */ import org.fourthline.cling.support.model.PositionInfo;
/*     */ import org.fourthline.cling.support.model.RecordQualityMode;
/*     */ import org.fourthline.cling.support.model.SeekMode;
/*     */ import org.fourthline.cling.support.model.StorageMedium;
/*     */ import org.fourthline.cling.support.model.TransportAction;
/*     */ import org.fourthline.cling.support.model.TransportInfo;
/*     */ import org.fourthline.cling.support.model.TransportSettings;
/*     */ import org.seamless.statemachine.StateMachineBuilder;
/*     */ import org.seamless.statemachine.TransitionException;
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
/*     */ public class AVTransportService<T extends AVTransport>
/*     */   extends AbstractAVTransportService
/*     */ {
/*  81 */   private static final Logger log = Logger.getLogger(AVTransportService.class.getName());
/*     */   
/*  83 */   private final Map<Long, AVTransportStateMachine> stateMachines = new ConcurrentHashMap<>();
/*     */   
/*     */   final Class<? extends AVTransportStateMachine> stateMachineDefinition;
/*     */   
/*     */   final Class<? extends AbstractState> initialState;
/*     */   final Class<? extends AVTransport> transportClass;
/*     */   
/*     */   public AVTransportService(Class<? extends AVTransportStateMachine> stateMachineDefinition, Class<? extends AbstractState> initialState) {
/*  91 */     this(stateMachineDefinition, initialState, (Class)AVTransport.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AVTransportService(Class<? extends AVTransportStateMachine> stateMachineDefinition, Class<? extends AbstractState> initialState, Class<T> transportClass) {
/*  97 */     this.stateMachineDefinition = stateMachineDefinition;
/*  98 */     this.initialState = initialState;
/*  99 */     this.transportClass = transportClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAVTransportURI(UnsignedIntegerFourBytes instanceId, String currentURI, String currentURIMetaData) throws AVTransportException {
/*     */     URI uri;
/*     */     try {
/* 108 */       uri = new URI(currentURI);
/* 109 */     } catch (Exception ex) {
/* 110 */       throw new AVTransportException(ErrorCode.INVALID_ARGS, "CurrentURI can not be null or malformed");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 116 */       AVTransportStateMachine transportStateMachine = findStateMachine(instanceId, true);
/* 117 */       transportStateMachine.setTransportURI(uri, currentURIMetaData);
/* 118 */     } catch (TransitionException ex) {
/* 119 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNextAVTransportURI(UnsignedIntegerFourBytes instanceId, String nextURI, String nextURIMetaData) throws AVTransportException {
/*     */     URI uri;
/*     */     try {
/* 129 */       uri = new URI(nextURI);
/* 130 */     } catch (Exception ex) {
/* 131 */       throw new AVTransportException(ErrorCode.INVALID_ARGS, "NextURI can not be null or malformed");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 137 */       AVTransportStateMachine transportStateMachine = findStateMachine(instanceId, true);
/* 138 */       transportStateMachine.setNextTransportURI(uri, nextURIMetaData);
/* 139 */     } catch (TransitionException ex) {
/* 140 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setPlayMode(UnsignedIntegerFourBytes instanceId, String newPlayMode) throws AVTransportException {
/* 145 */     AVTransport transport = ((AbstractState)findStateMachine(instanceId).getCurrentState()).getTransport();
/*     */     try {
/* 147 */       transport.setTransportSettings(new TransportSettings(
/*     */             
/* 149 */             PlayMode.valueOf(newPlayMode), transport
/* 150 */             .getTransportSettings().getRecQualityMode()));
/*     */     
/*     */     }
/* 153 */     catch (IllegalArgumentException ex) {
/* 154 */       throw new AVTransportException(AVTransportErrorCode.PLAYMODE_NOT_SUPPORTED, "Unsupported play mode: " + newPlayMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecordQualityMode(UnsignedIntegerFourBytes instanceId, String newRecordQualityMode) throws AVTransportException {
/* 161 */     AVTransport transport = ((AbstractState)findStateMachine(instanceId).getCurrentState()).getTransport();
/*     */     try {
/* 163 */       transport.setTransportSettings(new TransportSettings(transport
/*     */             
/* 165 */             .getTransportSettings().getPlayMode(), 
/* 166 */             RecordQualityMode.valueOrExceptionOf(newRecordQualityMode)));
/*     */     
/*     */     }
/* 169 */     catch (IllegalArgumentException ex) {
/* 170 */       throw new AVTransportException(AVTransportErrorCode.RECORDQUALITYMODE_NOT_SUPPORTED, "Unsupported record quality mode: " + newRecordQualityMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaInfo getMediaInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/* 177 */     return ((AbstractState)findStateMachine(instanceId).getCurrentState()).getTransport().getMediaInfo();
/*     */   }
/*     */   
/*     */   public TransportInfo getTransportInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/* 181 */     return ((AbstractState)findStateMachine(instanceId).getCurrentState()).getTransport().getTransportInfo();
/*     */   }
/*     */   
/*     */   public PositionInfo getPositionInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/* 185 */     return ((AbstractState)findStateMachine(instanceId).getCurrentState()).getTransport().getPositionInfo();
/*     */   }
/*     */   
/*     */   public DeviceCapabilities getDeviceCapabilities(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/* 189 */     return ((AbstractState)findStateMachine(instanceId).getCurrentState()).getTransport().getDeviceCapabilities();
/*     */   }
/*     */   
/*     */   public TransportSettings getTransportSettings(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/* 193 */     return ((AbstractState)findStateMachine(instanceId).getCurrentState()).getTransport().getTransportSettings();
/*     */   }
/*     */   
/*     */   public void stop(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/*     */     try {
/* 198 */       findStateMachine(instanceId).stop();
/* 199 */     } catch (TransitionException ex) {
/* 200 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void play(UnsignedIntegerFourBytes instanceId, String speed) throws AVTransportException {
/*     */     try {
/* 206 */       findStateMachine(instanceId).play(speed);
/* 207 */     } catch (TransitionException ex) {
/* 208 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pause(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/*     */     try {
/* 214 */       findStateMachine(instanceId).pause();
/* 215 */     } catch (TransitionException ex) {
/* 216 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void record(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/*     */     try {
/* 222 */       findStateMachine(instanceId).record();
/* 223 */     } catch (TransitionException ex) {
/* 224 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void seek(UnsignedIntegerFourBytes instanceId, String unit, String target) throws AVTransportException {
/*     */     SeekMode seekMode;
/*     */     try {
/* 231 */       seekMode = SeekMode.valueOrExceptionOf(unit);
/* 232 */     } catch (IllegalArgumentException ex) {
/* 233 */       throw new AVTransportException(AVTransportErrorCode.SEEKMODE_NOT_SUPPORTED, "Unsupported seek mode: " + unit);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 239 */       findStateMachine(instanceId).seek(seekMode, target);
/* 240 */     } catch (TransitionException ex) {
/* 241 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void next(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/*     */     try {
/* 247 */       findStateMachine(instanceId).next();
/* 248 */     } catch (TransitionException ex) {
/* 249 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void previous(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/*     */     try {
/* 255 */       findStateMachine(instanceId).previous();
/* 256 */     } catch (TransitionException ex) {
/* 257 */       throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected TransportAction[] getCurrentTransportActions(UnsignedIntegerFourBytes instanceId) throws Exception {
/* 263 */     AVTransportStateMachine stateMachine = findStateMachine(instanceId);
/*     */     try {
/* 265 */       return ((AbstractState)stateMachine.getCurrentState()).getCurrentTransportActions();
/* 266 */     } catch (TransitionException ex) {
/* 267 */       return new TransportAction[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
/* 273 */     synchronized (this.stateMachines) {
/* 274 */       UnsignedIntegerFourBytes[] ids = new UnsignedIntegerFourBytes[this.stateMachines.size()];
/* 275 */       int i = 0;
/* 276 */       for (Long id : this.stateMachines.keySet()) {
/* 277 */         ids[i] = new UnsignedIntegerFourBytes(id.longValue());
/* 278 */         i++;
/*     */       } 
/* 280 */       return ids;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AVTransportStateMachine findStateMachine(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/* 285 */     return findStateMachine(instanceId, true);
/*     */   }
/*     */   
/*     */   protected AVTransportStateMachine findStateMachine(UnsignedIntegerFourBytes instanceId, boolean createDefaultTransport) throws AVTransportException {
/* 289 */     synchronized (this.stateMachines) {
/* 290 */       long id = instanceId.getValue().longValue();
/* 291 */       AVTransportStateMachine stateMachine = this.stateMachines.get(Long.valueOf(id));
/* 292 */       if (stateMachine == null && id == 0L && createDefaultTransport) {
/* 293 */         log.fine("Creating default transport instance with ID '0'");
/* 294 */         stateMachine = createStateMachine(instanceId);
/* 295 */         this.stateMachines.put(Long.valueOf(id), stateMachine);
/* 296 */       } else if (stateMachine == null) {
/* 297 */         throw new AVTransportException(AVTransportErrorCode.INVALID_INSTANCE_ID);
/*     */       } 
/* 299 */       log.fine("Found transport control with ID '" + id + "'");
/* 300 */       return stateMachine;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected AVTransportStateMachine createStateMachine(UnsignedIntegerFourBytes instanceId) {
/* 306 */     return (AVTransportStateMachine)StateMachineBuilder.build(this.stateMachineDefinition, this.initialState, new Class[] { this.transportClass }, new Object[] {
/*     */ 
/*     */ 
/*     */           
/* 310 */           createTransport(instanceId, getLastChange())
/*     */         });
/*     */   }
/*     */   
/*     */   protected AVTransport createTransport(UnsignedIntegerFourBytes instanceId, LastChange lastChange) {
/* 315 */     return new AVTransport(instanceId, lastChange, StorageMedium.NETWORK);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\impl\AVTransportService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
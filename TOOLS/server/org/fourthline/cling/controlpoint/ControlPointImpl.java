/*     */ package org.fourthline.cling.controlpoint;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.context.ApplicationScoped;
/*     */ import javax.enterprise.event.Observes;
/*     */ import javax.inject.Inject;
/*     */ import org.fourthline.cling.UpnpServiceConfiguration;
/*     */ import org.fourthline.cling.controlpoint.event.ExecuteAction;
/*     */ import org.fourthline.cling.controlpoint.event.Search;
/*     */ import org.fourthline.cling.model.message.header.MXHeader;
/*     */ import org.fourthline.cling.model.message.header.STAllHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.registry.Registry;
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
/*     */ @ApplicationScoped
/*     */ public class ControlPointImpl
/*     */   implements ControlPoint
/*     */ {
/*  46 */   private static Logger log = Logger.getLogger(ControlPointImpl.class.getName());
/*     */   
/*     */   protected UpnpServiceConfiguration configuration;
/*     */   
/*     */   protected ProtocolFactory protocolFactory;
/*     */   protected Registry registry;
/*     */   
/*     */   protected ControlPointImpl() {}
/*     */   
/*     */   @Inject
/*     */   public ControlPointImpl(UpnpServiceConfiguration configuration, ProtocolFactory protocolFactory, Registry registry) {
/*  57 */     log.fine("Creating ControlPoint: " + getClass().getName());
/*     */     
/*  59 */     this.configuration = configuration;
/*  60 */     this.protocolFactory = protocolFactory;
/*  61 */     this.registry = registry;
/*     */   }
/*     */   
/*     */   public UpnpServiceConfiguration getConfiguration() {
/*  65 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/*  69 */     return this.protocolFactory;
/*     */   }
/*     */   
/*     */   public Registry getRegistry() {
/*  73 */     return this.registry;
/*     */   }
/*     */   
/*     */   public void search(@Observes Search search) {
/*  77 */     search(search.getSearchType(), search.getMxSeconds());
/*     */   }
/*     */   
/*     */   public void search() {
/*  81 */     search((UpnpHeader)new STAllHeader(), MXHeader.DEFAULT_VALUE.intValue());
/*     */   }
/*     */   
/*     */   public void search(UpnpHeader searchType) {
/*  85 */     search(searchType, MXHeader.DEFAULT_VALUE.intValue());
/*     */   }
/*     */   
/*     */   public void search(int mxSeconds) {
/*  89 */     search((UpnpHeader)new STAllHeader(), mxSeconds);
/*     */   }
/*     */   
/*     */   public void search(UpnpHeader searchType, int mxSeconds) {
/*  93 */     log.fine("Sending asynchronous search for: " + searchType.getString());
/*  94 */     getConfiguration().getAsyncProtocolExecutor().execute((Runnable)
/*  95 */         getProtocolFactory().createSendingSearch(searchType, mxSeconds));
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(ExecuteAction executeAction) {
/* 100 */     execute(executeAction.getCallback());
/*     */   }
/*     */   
/*     */   public Future execute(ActionCallback callback) {
/* 104 */     log.fine("Invoking action in background: " + callback);
/* 105 */     callback.setControlPoint(this);
/* 106 */     ExecutorService executor = getConfiguration().getSyncProtocolExecutorService();
/* 107 */     return executor.submit(callback);
/*     */   }
/*     */   
/*     */   public void execute(SubscriptionCallback callback) {
/* 111 */     log.fine("Invoking subscription in background: " + callback);
/* 112 */     callback.setControlPoint(this);
/* 113 */     getConfiguration().getSyncProtocolExecutorService().execute(callback);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\controlpoint\ControlPointImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
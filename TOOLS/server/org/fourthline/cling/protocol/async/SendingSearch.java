/*     */ package org.fourthline.cling.protocol.async;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.discovery.OutgoingSearchRequest;
/*     */ import org.fourthline.cling.model.message.header.MXHeader;
/*     */ import org.fourthline.cling.model.message.header.STAllHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.protocol.SendingAsync;
/*     */ import org.fourthline.cling.transport.RouterException;
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
/*     */ public class SendingSearch
/*     */   extends SendingAsync
/*     */ {
/*  39 */   private static final Logger log = Logger.getLogger(SendingSearch.class.getName());
/*     */ 
/*     */   
/*     */   private final UpnpHeader searchTarget;
/*     */   
/*     */   private final int mxSeconds;
/*     */ 
/*     */   
/*     */   public SendingSearch(UpnpService upnpService) {
/*  48 */     this(upnpService, (UpnpHeader)new STAllHeader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SendingSearch(UpnpService upnpService, UpnpHeader searchTarget) {
/*  55 */     this(upnpService, searchTarget, MXHeader.DEFAULT_VALUE.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SendingSearch(UpnpService upnpService, UpnpHeader searchTarget, int mxSeconds) {
/*  62 */     super(upnpService);
/*     */     
/*  64 */     if (!UpnpHeader.Type.ST.isValidHeaderType(searchTarget.getClass())) {
/*  65 */       throw new IllegalArgumentException("Given search target instance is not a valid header class for type ST: " + searchTarget
/*  66 */           .getClass());
/*     */     }
/*     */     
/*  69 */     this.searchTarget = searchTarget;
/*  70 */     this.mxSeconds = mxSeconds;
/*     */   }
/*     */   
/*     */   public UpnpHeader getSearchTarget() {
/*  74 */     return this.searchTarget;
/*     */   }
/*     */   
/*     */   public int getMxSeconds() {
/*  78 */     return this.mxSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void execute() throws RouterException {
/*  83 */     log.fine("Executing search for target: " + this.searchTarget.getString() + " with MX seconds: " + getMxSeconds());
/*     */     
/*  85 */     OutgoingSearchRequest msg = new OutgoingSearchRequest(this.searchTarget, getMxSeconds());
/*  86 */     prepareOutgoingSearchRequest(msg);
/*     */     
/*  88 */     for (int i = 0; i < getBulkRepeat(); i++) {
/*     */       
/*     */       try {
/*  91 */         getUpnpService().getRouter().send((OutgoingDatagramMessage)msg);
/*     */ 
/*     */         
/*  94 */         log.finer("Sleeping " + getBulkIntervalMilliseconds() + " milliseconds");
/*  95 */         Thread.sleep(getBulkIntervalMilliseconds());
/*     */       }
/*  97 */       catch (InterruptedException ex) {
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBulkRepeat() {
/* 105 */     return 5;
/*     */   }
/*     */   
/*     */   public int getBulkIntervalMilliseconds() {
/* 109 */     return 500;
/*     */   }
/*     */   
/*     */   protected void prepareOutgoingSearchRequest(OutgoingSearchRequest message) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\async\SendingSearch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
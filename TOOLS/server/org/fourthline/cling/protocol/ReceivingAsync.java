/*     */ package org.fourthline.cling.protocol;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ import org.seamless.util.Exceptions;
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
/*     */ public abstract class ReceivingAsync<M extends UpnpMessage>
/*     */   implements Runnable
/*     */ {
/*  41 */   private static final Logger log = Logger.getLogger(UpnpService.class.getName());
/*     */   
/*     */   private final UpnpService upnpService;
/*     */   
/*     */   private M inputMessage;
/*     */   
/*     */   protected ReceivingAsync(UpnpService upnpService, M inputMessage) {
/*  48 */     this.upnpService = upnpService;
/*  49 */     this.inputMessage = inputMessage;
/*     */   }
/*     */   
/*     */   public UpnpService getUpnpService() {
/*  53 */     return this.upnpService;
/*     */   }
/*     */   
/*     */   public M getInputMessage() {
/*  57 */     return this.inputMessage;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     boolean proceed;
/*     */     try {
/*  63 */       proceed = waitBeforeExecution();
/*  64 */     } catch (InterruptedException ex) {
/*  65 */       log.info("Protocol wait before execution interrupted (on shutdown?): " + getClass().getSimpleName());
/*  66 */       proceed = false;
/*     */     } 
/*     */     
/*  69 */     if (proceed) {
/*     */       try {
/*  71 */         execute();
/*  72 */       } catch (Exception ex) {
/*  73 */         Throwable cause = Exceptions.unwrap(ex);
/*  74 */         if (cause instanceof InterruptedException) {
/*  75 */           log.log(Level.INFO, "Interrupted protocol '" + getClass().getSimpleName() + "': " + ex, cause);
/*     */         } else {
/*  77 */           throw new RuntimeException("Fatal error while executing protocol '" + 
/*  78 */               getClass().getSimpleName() + "': " + ex, ex);
/*     */         } 
/*     */       } 
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
/*     */   protected boolean waitBeforeExecution() throws InterruptedException {
/*  94 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract void execute() throws RouterException;
/*     */   
/*     */   protected <H extends UpnpHeader> H getFirstHeader(UpnpHeader.Type headerType, Class<H> subtype) {
/* 100 */     return (H)getInputMessage().getHeaders().getFirstHeader(headerType, subtype);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return "(" + getClass().getSimpleName() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\ReceivingAsync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
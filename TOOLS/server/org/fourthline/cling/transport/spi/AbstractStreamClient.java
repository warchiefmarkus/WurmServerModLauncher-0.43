/*     */ package org.fourthline.cling.transport.spi;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
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
/*     */ public abstract class AbstractStreamClient<C extends StreamClientConfiguration, REQUEST>
/*     */   implements StreamClient<C>
/*     */ {
/*  36 */   private static final Logger log = Logger.getLogger(StreamClient.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamResponseMessage sendRequest(StreamRequestMessage requestMessage) throws InterruptedException {
/*  41 */     if (log.isLoggable(Level.FINE)) {
/*  42 */       log.fine("Preparing HTTP request: " + requestMessage);
/*     */     }
/*  44 */     REQUEST request = createRequest(requestMessage);
/*  45 */     if (request == null) {
/*  46 */       return null;
/*     */     }
/*  48 */     Callable<StreamResponseMessage> callable = createCallable(requestMessage, request);
/*     */ 
/*     */     
/*  51 */     long start = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */     
/*  55 */     Future<StreamResponseMessage> future = getConfiguration().getRequestExecutorService().submit(callable);
/*     */ 
/*     */     
/*     */     try {
/*  59 */       if (log.isLoggable(Level.FINE)) {
/*  60 */         log.fine("Waiting " + 
/*  61 */             getConfiguration().getTimeoutSeconds() + " seconds for HTTP request to complete: " + requestMessage);
/*     */       }
/*     */ 
/*     */       
/*  65 */       StreamResponseMessage response = future.get(getConfiguration().getTimeoutSeconds(), TimeUnit.SECONDS);
/*     */ 
/*     */       
/*  68 */       long elapsed = System.currentTimeMillis() - start;
/*  69 */       if (log.isLoggable(Level.FINEST))
/*  70 */         log.finest("Got HTTP response in " + elapsed + "ms: " + requestMessage); 
/*  71 */       if (getConfiguration().getLogWarningSeconds() > 0 && elapsed > (
/*  72 */         getConfiguration().getLogWarningSeconds() * 1000)) {
/*  73 */         log.warning("HTTP request took a long time (" + elapsed + "ms): " + requestMessage);
/*     */       }
/*     */       
/*  76 */       return response;
/*     */     }
/*  78 */     catch (InterruptedException ex) {
/*     */       
/*  80 */       if (log.isLoggable(Level.FINE))
/*  81 */         log.fine("Interruption, aborting request: " + requestMessage); 
/*  82 */       abort(request);
/*  83 */       throw new InterruptedException("HTTP request interrupted and aborted");
/*     */     }
/*  85 */     catch (TimeoutException ex) {
/*     */       
/*  87 */       log.info("Timeout of " + 
/*  88 */           getConfiguration().getTimeoutSeconds() + " seconds while waiting for HTTP request to complete, aborting: " + requestMessage);
/*     */ 
/*     */       
/*  91 */       abort(request);
/*  92 */       return null;
/*     */     }
/*  94 */     catch (ExecutionException ex) {
/*  95 */       Throwable cause = ex.getCause();
/*  96 */       if (!logExecutionException(cause)) {
/*  97 */         log.log(Level.WARNING, "HTTP request failed: " + requestMessage, Exceptions.unwrap(cause));
/*     */       }
/*  99 */       return null;
/*     */     } finally {
/* 101 */       onFinally(request);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract REQUEST createRequest(StreamRequestMessage paramStreamRequestMessage);
/*     */   
/*     */   protected abstract Callable<StreamResponseMessage> createCallable(StreamRequestMessage paramStreamRequestMessage, REQUEST paramREQUEST);
/*     */   
/*     */   protected abstract void abort(REQUEST paramREQUEST);
/*     */   
/*     */   protected abstract boolean logExecutionException(Throwable paramThrowable);
/*     */   
/*     */   protected void onFinally(REQUEST request) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\AbstractStreamClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.inject.Alternative;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.control.ActionMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionRequestMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionResponseMessage;
/*     */ import org.fourthline.cling.transport.spi.SOAPActionProcessor;
/*     */ import org.seamless.xml.XmlPullParserUtils;
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
/*     */ @Alternative
/*     */ public class RecoveringSOAPActionProcessorImpl
/*     */   extends PullSOAPActionProcessorImpl
/*     */ {
/*  55 */   private static Logger log = Logger.getLogger(SOAPActionProcessor.class.getName());
/*     */   
/*     */   public void readBody(ActionRequestMessage requestMessage, ActionInvocation actionInvocation) throws UnsupportedDataException {
/*     */     try {
/*  59 */       super.readBody(requestMessage, actionInvocation);
/*  60 */     } catch (UnsupportedDataException ex) {
/*     */ 
/*     */       
/*  63 */       if (!requestMessage.isBodyNonEmptyString()) {
/*  64 */         throw ex;
/*     */       }
/*  66 */       log.warning("Trying to recover from invalid SOAP XML request: " + ex);
/*  67 */       String body = getMessageBody((ActionMessage)requestMessage);
/*     */ 
/*     */ 
/*     */       
/*  71 */       String fixedBody = XmlPullParserUtils.fixXMLEntities(body);
/*     */ 
/*     */       
/*     */       try {
/*  75 */         requestMessage.setBody(fixedBody);
/*  76 */         super.readBody(requestMessage, actionInvocation);
/*  77 */       } catch (UnsupportedDataException ex2) {
/*  78 */         handleInvalidMessage(actionInvocation, ex, ex2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readBody(ActionResponseMessage responseMsg, ActionInvocation actionInvocation) throws UnsupportedDataException {
/*     */     try {
/*  85 */       super.readBody(responseMsg, actionInvocation);
/*  86 */     } catch (UnsupportedDataException ex) {
/*     */ 
/*     */       
/*  89 */       if (!responseMsg.isBodyNonEmptyString()) {
/*  90 */         throw ex;
/*     */       }
/*  92 */       log.warning("Trying to recover from invalid SOAP XML response: " + ex);
/*  93 */       String body = getMessageBody((ActionMessage)responseMsg);
/*     */ 
/*     */       
/*  96 */       String fixedBody = XmlPullParserUtils.fixXMLEntities(body);
/*     */ 
/*     */ 
/*     */       
/* 100 */       if (fixedBody.endsWith("</s:Envelop")) {
/* 101 */         fixedBody = fixedBody + "e>";
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 106 */         responseMsg.setBody(fixedBody);
/* 107 */         super.readBody(responseMsg, actionInvocation);
/* 108 */       } catch (UnsupportedDataException ex2) {
/* 109 */         handleInvalidMessage(actionInvocation, ex, ex2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleInvalidMessage(ActionInvocation actionInvocation, UnsupportedDataException originalException, UnsupportedDataException recoveringException) throws UnsupportedDataException {
/* 133 */     throw originalException;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\RecoveringSOAPActionProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
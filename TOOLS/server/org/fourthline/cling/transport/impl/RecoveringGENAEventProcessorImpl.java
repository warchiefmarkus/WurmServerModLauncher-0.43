/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.enterprise.inject.Alternative;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.XMLUtil;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.gena.IncomingEventRequestMessage;
/*     */ import org.fourthline.cling.transport.spi.GENAEventProcessor;
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
/*     */ @Alternative
/*     */ public class RecoveringGENAEventProcessorImpl
/*     */   extends PullGENAEventProcessorImpl
/*     */ {
/*  53 */   private static Logger log = Logger.getLogger(GENAEventProcessor.class.getName());
/*     */   
/*     */   public void readBody(IncomingEventRequestMessage requestMessage) throws UnsupportedDataException {
/*     */     try {
/*  57 */       super.readBody(requestMessage);
/*  58 */     } catch (UnsupportedDataException ex) {
/*     */ 
/*     */       
/*  61 */       if (!requestMessage.isBodyNonEmptyString()) {
/*  62 */         throw ex;
/*     */       }
/*  64 */       log.warning("Trying to recover from invalid GENA XML event: " + ex);
/*     */ 
/*     */       
/*  67 */       requestMessage.getStateVariableValues().clear();
/*     */       
/*  69 */       String body = getMessageBody((UpnpMessage)requestMessage);
/*     */       
/*  71 */       String fixedBody = fixXMLEncodedLastChange(
/*  72 */           XmlPullParserUtils.fixXMLEntities(body));
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  77 */         requestMessage.setBody(fixedBody);
/*  78 */         super.readBody(requestMessage);
/*  79 */       } catch (UnsupportedDataException ex2) {
/*     */         
/*  81 */         if (requestMessage.getStateVariableValues().isEmpty())
/*     */         {
/*  83 */           throw ex;
/*     */         }
/*  85 */         log.warning("Partial read of GENA event properties (probably due to truncated XML)");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String fixXMLEncodedLastChange(String xml) {
/*  91 */     Pattern pattern = Pattern.compile("<LastChange>(.*)</LastChange>", 32);
/*  92 */     Matcher matcher = pattern.matcher(xml);
/*     */     
/*  94 */     if (matcher.find() && matcher.groupCount() == 1) {
/*     */       
/*  96 */       String lastChange = matcher.group(1);
/*     */       
/*  98 */       if (XmlPullParserUtils.isNullOrEmpty(lastChange)) {
/*  99 */         return xml;
/*     */       }
/* 101 */       lastChange = lastChange.trim();
/*     */       
/* 103 */       String fixedLastChange = lastChange;
/*     */       
/* 105 */       if (lastChange.charAt(0) == '<')
/*     */       {
/* 107 */         fixedLastChange = XMLUtil.encodeText(fixedLastChange);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       if (fixedLastChange.equals(lastChange)) {
/* 117 */         return xml;
/*     */       }
/*     */       
/* 120 */       return "<?xml version=\"1.0\" encoding=\"utf-8\"?><e:propertyset xmlns:e=\"urn:schemas-upnp-org:event-1-0\"><e:property><LastChange>" + fixedLastChange + "</LastChange>" + "</e:property>" + "</e:propertyset>";
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     return xml;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\RecoveringGENAEventProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
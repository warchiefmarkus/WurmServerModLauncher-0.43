/*    */ package org.fourthline.cling.transport.impl;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.enterprise.inject.Alternative;
/*    */ import org.fourthline.cling.model.UnsupportedDataException;
/*    */ import org.fourthline.cling.model.message.UpnpMessage;
/*    */ import org.fourthline.cling.model.message.gena.IncomingEventRequestMessage;
/*    */ import org.fourthline.cling.model.meta.StateVariable;
/*    */ import org.fourthline.cling.model.state.StateVariableValue;
/*    */ import org.fourthline.cling.transport.spi.GENAEventProcessor;
/*    */ import org.seamless.xml.XmlPullParserUtils;
/*    */ import org.xmlpull.v1.XmlPullParser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Alternative
/*    */ public class PullGENAEventProcessorImpl
/*    */   extends GENAEventProcessorImpl
/*    */ {
/* 46 */   private static Logger log = Logger.getLogger(GENAEventProcessor.class.getName());
/*    */   
/*    */   public void readBody(IncomingEventRequestMessage requestMessage) throws UnsupportedDataException {
/* 49 */     log.fine("Reading body of: " + requestMessage);
/* 50 */     if (log.isLoggable(Level.FINER)) {
/* 51 */       log.finer("===================================== GENA BODY BEGIN ============================================");
/* 52 */       log.finer((requestMessage.getBody() != null) ? requestMessage.getBody().toString() : null);
/* 53 */       log.finer("-===================================== GENA BODY END ============================================");
/*    */     } 
/*    */     
/* 56 */     String body = getMessageBody((UpnpMessage)requestMessage);
/*    */     try {
/* 58 */       XmlPullParser xpp = XmlPullParserUtils.createParser(body);
/* 59 */       readProperties(xpp, requestMessage);
/* 60 */     } catch (Exception ex) {
/* 61 */       throw new UnsupportedDataException("Can't transform message payload: " + ex.getMessage(), ex, body);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readProperties(XmlPullParser xpp, IncomingEventRequestMessage message) throws Exception {
/* 67 */     StateVariable[] stateVariables = message.getService().getStateVariables();
/*    */     int event;
/* 69 */     while ((event = xpp.next()) != 1) {
/* 70 */       if (event == 2 && 
/* 71 */         xpp.getName().equals("property")) {
/* 72 */         readProperty(xpp, message, stateVariables);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readProperty(XmlPullParser xpp, IncomingEventRequestMessage message, StateVariable[] stateVariables) throws Exception {
/*    */     int event;
/*    */     do {
/* 81 */       event = xpp.next();
/* 82 */       if (event != 2)
/*    */         continue; 
/* 84 */       String stateVariableName = xpp.getName();
/* 85 */       for (StateVariable stateVariable : stateVariables) {
/* 86 */         if (stateVariable.getName().equals(stateVariableName)) {
/* 87 */           log.fine("Reading state variable value: " + stateVariableName);
/* 88 */           String value = xpp.nextText();
/* 89 */           message.getStateVariableValues().add(new StateVariableValue(stateVariable, value));
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/* 94 */     } while (event != 1 && (event != 3 || !xpp.getName().equals("property")));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\PullGENAEventProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
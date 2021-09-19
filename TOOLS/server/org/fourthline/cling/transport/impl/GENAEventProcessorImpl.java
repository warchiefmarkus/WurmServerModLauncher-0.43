/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.XMLUtil;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.gena.IncomingEventRequestMessage;
/*     */ import org.fourthline.cling.model.message.gena.OutgoingEventRequestMessage;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.state.StateVariableValue;
/*     */ import org.fourthline.cling.transport.spi.GENAEventProcessor;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public class GENAEventProcessorImpl
/*     */   implements GENAEventProcessor, ErrorHandler
/*     */ {
/*  51 */   private static Logger log = Logger.getLogger(GENAEventProcessor.class.getName());
/*     */   
/*     */   protected DocumentBuilderFactory createDocumentBuilderFactory() throws FactoryConfigurationError {
/*  54 */     return DocumentBuilderFactory.newInstance();
/*     */   }
/*     */   
/*     */   public void writeBody(OutgoingEventRequestMessage requestMessage) throws UnsupportedDataException {
/*  58 */     log.fine("Writing body of: " + requestMessage);
/*     */ 
/*     */     
/*     */     try {
/*  62 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  63 */       factory.setNamespaceAware(true);
/*  64 */       Document d = factory.newDocumentBuilder().newDocument();
/*  65 */       Element propertysetElement = writePropertysetElement(d);
/*     */       
/*  67 */       writeProperties(d, propertysetElement, requestMessage);
/*     */       
/*  69 */       requestMessage.setBody(UpnpMessage.BodyType.STRING, toString(d));
/*     */       
/*  71 */       if (log.isLoggable(Level.FINER)) {
/*  72 */         log.finer("===================================== GENA BODY BEGIN ============================================");
/*  73 */         log.finer(requestMessage.getBody().toString());
/*  74 */         log.finer("====================================== GENA BODY END =============================================");
/*     */       }
/*     */     
/*  77 */     } catch (Exception ex) {
/*  78 */       throw new UnsupportedDataException("Can't transform message payload: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readBody(IncomingEventRequestMessage requestMessage) throws UnsupportedDataException {
/*  84 */     log.fine("Reading body of: " + requestMessage);
/*  85 */     if (log.isLoggable(Level.FINER)) {
/*  86 */       log.finer("===================================== GENA BODY BEGIN ============================================");
/*  87 */       log.finer((requestMessage.getBody() != null) ? requestMessage.getBody().toString() : "null");
/*  88 */       log.finer("-===================================== GENA BODY END ============================================");
/*     */     } 
/*     */     
/*  91 */     String body = getMessageBody((UpnpMessage)requestMessage);
/*     */     
/*     */     try {
/*  94 */       DocumentBuilderFactory factory = createDocumentBuilderFactory();
/*  95 */       factory.setNamespaceAware(true);
/*  96 */       DocumentBuilder documentBuilder = factory.newDocumentBuilder();
/*  97 */       documentBuilder.setErrorHandler(this);
/*     */       
/*  99 */       Document d = documentBuilder.parse(new InputSource(new StringReader(body)));
/*     */ 
/*     */ 
/*     */       
/* 103 */       Element propertysetElement = readPropertysetElement(d);
/*     */       
/* 105 */       readProperties(propertysetElement, requestMessage);
/*     */     }
/* 107 */     catch (Exception ex) {
/* 108 */       throw new UnsupportedDataException("Can't transform message payload: " + ex.getMessage(), ex, body);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Element writePropertysetElement(Document d) {
/* 115 */     Element propertysetElement = d.createElementNS("urn:schemas-upnp-org:event-1-0", "e:propertyset");
/* 116 */     d.appendChild(propertysetElement);
/* 117 */     return propertysetElement;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Element readPropertysetElement(Document d) {
/* 122 */     Element propertysetElement = d.getDocumentElement();
/* 123 */     if (propertysetElement == null || !getUnprefixedNodeName(propertysetElement).equals("propertyset")) {
/* 124 */       throw new RuntimeException("Root element was not 'propertyset'");
/*     */     }
/* 126 */     return propertysetElement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeProperties(Document d, Element propertysetElement, OutgoingEventRequestMessage message) {
/* 132 */     for (StateVariableValue stateVariableValue : message.getStateVariableValues()) {
/* 133 */       Element propertyElement = d.createElementNS("urn:schemas-upnp-org:event-1-0", "e:property");
/* 134 */       propertysetElement.appendChild(propertyElement);
/* 135 */       XMLUtil.appendNewElement(d, propertyElement, stateVariableValue
/*     */ 
/*     */           
/* 138 */           .getStateVariable().getName(), stateVariableValue
/* 139 */           .toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readProperties(Element propertysetElement, IncomingEventRequestMessage message) {
/* 145 */     NodeList propertysetElementChildren = propertysetElement.getChildNodes();
/*     */     
/* 147 */     StateVariable[] stateVariables = message.getService().getStateVariables();
/*     */     
/* 149 */     for (int i = 0; i < propertysetElementChildren.getLength(); i++) {
/* 150 */       Node propertysetChild = propertysetElementChildren.item(i);
/*     */       
/* 152 */       if (propertysetChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 155 */         if (getUnprefixedNodeName(propertysetChild).equals("property")) {
/*     */           
/* 157 */           NodeList propertyChildren = propertysetChild.getChildNodes();
/*     */           
/* 159 */           for (int j = 0; j < propertyChildren.getLength(); j++) {
/* 160 */             Node propertyChild = propertyChildren.item(j);
/*     */             
/* 162 */             if (propertyChild.getNodeType() == 1) {
/*     */ 
/*     */               
/* 165 */               String stateVariableName = getUnprefixedNodeName(propertyChild);
/* 166 */               for (StateVariable stateVariable : stateVariables) {
/* 167 */                 if (stateVariable.getName().equals(stateVariableName)) {
/* 168 */                   log.fine("Reading state variable value: " + stateVariableName);
/* 169 */                   String value = XMLUtil.getTextContent(propertyChild);
/* 170 */                   message.getStateVariableValues().add(new StateVariableValue(stateVariable, value));
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getMessageBody(UpnpMessage message) throws UnsupportedDataException {
/* 185 */     if (!message.isBodyNonEmptyString()) {
/* 186 */       throw new UnsupportedDataException("Can't transform null or non-string/zero-length body of: " + message);
/*     */     }
/*     */     
/* 189 */     return message.getBodyString().trim();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String toString(Document d) throws Exception {
/* 194 */     String output = XMLUtil.documentToString(d);
/* 195 */     while (output.endsWith("\n") || output.endsWith("\r")) {
/* 196 */       output = output.substring(0, output.length() - 1);
/*     */     }
/*     */     
/* 199 */     return output;
/*     */   }
/*     */   
/*     */   protected String getUnprefixedNodeName(Node node) {
/* 203 */     return (node.getPrefix() != null) ? node
/* 204 */       .getNodeName().substring(node.getPrefix().length() + 1) : node
/* 205 */       .getNodeName();
/*     */   }
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 209 */     log.warning(e.toString());
/*     */   }
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 213 */     throw e;
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 217 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\GENAEventProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
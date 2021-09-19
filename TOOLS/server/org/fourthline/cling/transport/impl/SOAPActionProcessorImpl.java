/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.XMLUtil;
/*     */ import org.fourthline.cling.model.action.ActionArgumentValue;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.control.ActionMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionRequestMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionResponseMessage;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ import org.fourthline.cling.transport.spi.SOAPActionProcessor;
/*     */ import org.w3c.dom.Attr;
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
/*     */ public class SOAPActionProcessorImpl
/*     */   implements SOAPActionProcessor, ErrorHandler
/*     */ {
/*  59 */   private static Logger log = Logger.getLogger(SOAPActionProcessor.class.getName());
/*     */   
/*     */   protected DocumentBuilderFactory createDocumentBuilderFactory() throws FactoryConfigurationError {
/*  62 */     return DocumentBuilderFactory.newInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBody(ActionRequestMessage requestMessage, ActionInvocation actionInvocation) throws UnsupportedDataException {
/*  67 */     log.fine("Writing body of " + requestMessage + " for: " + actionInvocation);
/*     */ 
/*     */     
/*     */     try {
/*  71 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  72 */       factory.setNamespaceAware(true);
/*  73 */       Document d = factory.newDocumentBuilder().newDocument();
/*  74 */       Element body = writeBodyElement(d);
/*     */       
/*  76 */       writeBodyRequest(d, body, requestMessage, actionInvocation);
/*     */       
/*  78 */       if (log.isLoggable(Level.FINER)) {
/*  79 */         log.finer("===================================== SOAP BODY BEGIN ============================================");
/*  80 */         log.finer(requestMessage.getBodyString());
/*  81 */         log.finer("-===================================== SOAP BODY END ============================================");
/*     */       }
/*     */     
/*  84 */     } catch (Exception ex) {
/*  85 */       throw new UnsupportedDataException("Can't transform message payload: " + ex, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBody(ActionResponseMessage responseMessage, ActionInvocation actionInvocation) throws UnsupportedDataException {
/*  91 */     log.fine("Writing body of " + responseMessage + " for: " + actionInvocation);
/*     */ 
/*     */     
/*     */     try {
/*  95 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  96 */       factory.setNamespaceAware(true);
/*  97 */       Document d = factory.newDocumentBuilder().newDocument();
/*  98 */       Element body = writeBodyElement(d);
/*     */       
/* 100 */       if (actionInvocation.getFailure() != null) {
/* 101 */         writeBodyFailure(d, body, responseMessage, actionInvocation);
/*     */       } else {
/* 103 */         writeBodyResponse(d, body, responseMessage, actionInvocation);
/*     */       } 
/*     */       
/* 106 */       if (log.isLoggable(Level.FINER)) {
/* 107 */         log.finer("===================================== SOAP BODY BEGIN ============================================");
/* 108 */         log.finer(responseMessage.getBodyString());
/* 109 */         log.finer("-===================================== SOAP BODY END ============================================");
/*     */       }
/*     */     
/* 112 */     } catch (Exception ex) {
/* 113 */       throw new UnsupportedDataException("Can't transform message payload: " + ex, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readBody(ActionRequestMessage requestMessage, ActionInvocation actionInvocation) throws UnsupportedDataException {
/* 119 */     log.fine("Reading body of " + requestMessage + " for: " + actionInvocation);
/* 120 */     if (log.isLoggable(Level.FINER)) {
/* 121 */       log.finer("===================================== SOAP BODY BEGIN ============================================");
/* 122 */       log.finer(requestMessage.getBodyString());
/* 123 */       log.finer("-===================================== SOAP BODY END ============================================");
/*     */     } 
/*     */     
/* 126 */     String body = getMessageBody((ActionMessage)requestMessage);
/*     */     
/*     */     try {
/* 129 */       DocumentBuilderFactory factory = createDocumentBuilderFactory();
/* 130 */       factory.setNamespaceAware(true);
/* 131 */       DocumentBuilder documentBuilder = factory.newDocumentBuilder();
/* 132 */       documentBuilder.setErrorHandler(this);
/*     */       
/* 134 */       Document d = documentBuilder.parse(new InputSource(new StringReader(body)));
/*     */       
/* 136 */       Element bodyElement = readBodyElement(d);
/*     */       
/* 138 */       readBodyRequest(d, bodyElement, requestMessage, actionInvocation);
/*     */     }
/* 140 */     catch (Exception ex) {
/* 141 */       throw new UnsupportedDataException("Can't transform message payload: " + ex, ex, body);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readBody(ActionResponseMessage responseMsg, ActionInvocation actionInvocation) throws UnsupportedDataException {
/* 147 */     log.fine("Reading body of " + responseMsg + " for: " + actionInvocation);
/* 148 */     if (log.isLoggable(Level.FINER)) {
/* 149 */       log.finer("===================================== SOAP BODY BEGIN ============================================");
/* 150 */       log.finer(responseMsg.getBodyString());
/* 151 */       log.finer("-===================================== SOAP BODY END ============================================");
/*     */     } 
/*     */     
/* 154 */     String body = getMessageBody((ActionMessage)responseMsg);
/*     */     
/*     */     try {
/* 157 */       DocumentBuilderFactory factory = createDocumentBuilderFactory();
/* 158 */       factory.setNamespaceAware(true);
/* 159 */       DocumentBuilder documentBuilder = factory.newDocumentBuilder();
/* 160 */       documentBuilder.setErrorHandler(this);
/*     */       
/* 162 */       Document d = documentBuilder.parse(new InputSource(new StringReader(body)));
/*     */       
/* 164 */       Element bodyElement = readBodyElement(d);
/*     */       
/* 166 */       ActionException failure = readBodyFailure(d, bodyElement);
/*     */       
/* 168 */       if (failure == null) {
/* 169 */         readBodyResponse(d, bodyElement, responseMsg, actionInvocation);
/*     */       } else {
/* 171 */         actionInvocation.setFailure(failure);
/*     */       }
/*     */     
/* 174 */     } catch (Exception ex) {
/* 175 */       throw new UnsupportedDataException("Can't transform message payload: " + ex, ex, body);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBodyFailure(Document d, Element bodyElement, ActionResponseMessage message, ActionInvocation actionInvocation) throws Exception {
/* 186 */     writeFaultElement(d, bodyElement, actionInvocation);
/* 187 */     message.setBody(toString(d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBodyRequest(Document d, Element bodyElement, ActionRequestMessage message, ActionInvocation actionInvocation) throws Exception {
/* 195 */     Element actionRequestElement = writeActionRequestElement(d, bodyElement, message, actionInvocation);
/* 196 */     writeActionInputArguments(d, actionRequestElement, actionInvocation);
/* 197 */     message.setBody(toString(d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBodyResponse(Document d, Element bodyElement, ActionResponseMessage message, ActionInvocation actionInvocation) throws Exception {
/* 206 */     Element actionResponseElement = writeActionResponseElement(d, bodyElement, message, actionInvocation);
/* 207 */     writeActionOutputArguments(d, actionResponseElement, actionInvocation);
/* 208 */     message.setBody(toString(d));
/*     */   }
/*     */   
/*     */   protected ActionException readBodyFailure(Document d, Element bodyElement) throws Exception {
/* 212 */     return readFaultElement(bodyElement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readBodyRequest(Document d, Element bodyElement, ActionRequestMessage message, ActionInvocation actionInvocation) throws Exception {
/* 220 */     Element actionRequestElement = readActionRequestElement(bodyElement, message, actionInvocation);
/* 221 */     readActionInputArguments(actionRequestElement, actionInvocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readBodyResponse(Document d, Element bodyElement, ActionResponseMessage message, ActionInvocation actionInvocation) throws Exception {
/* 229 */     Element actionResponse = readActionResponseElement(bodyElement, actionInvocation);
/* 230 */     readActionOutputArguments(actionResponse, actionInvocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Element writeBodyElement(Document d) {
/* 237 */     Element envelopeElement = d.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "s:Envelope");
/* 238 */     Attr encodingStyleAttr = d.createAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "s:encodingStyle");
/* 239 */     encodingStyleAttr.setValue("http://schemas.xmlsoap.org/soap/encoding/");
/* 240 */     envelopeElement.setAttributeNode(encodingStyleAttr);
/* 241 */     d.appendChild(envelopeElement);
/*     */     
/* 243 */     Element bodyElement = d.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "s:Body");
/* 244 */     envelopeElement.appendChild(bodyElement);
/*     */     
/* 246 */     return bodyElement;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Element readBodyElement(Document d) {
/* 251 */     Element envelopeElement = d.getDocumentElement();
/*     */     
/* 253 */     if (envelopeElement == null || !getUnprefixedNodeName(envelopeElement).equals("Envelope")) {
/* 254 */       throw new RuntimeException("Response root element was not 'Envelope'");
/*     */     }
/*     */     
/* 257 */     NodeList envelopeElementChildren = envelopeElement.getChildNodes();
/* 258 */     for (int i = 0; i < envelopeElementChildren.getLength(); i++) {
/* 259 */       Node envelopeChild = envelopeElementChildren.item(i);
/*     */       
/* 261 */       if (envelopeChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 264 */         if (getUnprefixedNodeName(envelopeChild).equals("Body")) {
/* 265 */           return (Element)envelopeChild;
/*     */         }
/*     */       }
/*     */     } 
/* 269 */     throw new RuntimeException("Response envelope did not contain 'Body' child element");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Element writeActionRequestElement(Document d, Element bodyElement, ActionRequestMessage message, ActionInvocation actionInvocation) {
/* 279 */     log.fine("Writing action request element: " + actionInvocation.getAction().getName());
/*     */     
/* 281 */     Element actionRequestElement = d.createElementNS(message
/* 282 */         .getActionNamespace(), "u:" + actionInvocation
/* 283 */         .getAction().getName());
/*     */     
/* 285 */     bodyElement.appendChild(actionRequestElement);
/*     */     
/* 287 */     return actionRequestElement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Element readActionRequestElement(Element bodyElement, ActionRequestMessage message, ActionInvocation actionInvocation) {
/* 293 */     NodeList bodyChildren = bodyElement.getChildNodes();
/*     */     
/* 295 */     log.fine("Looking for action request element matching namespace:" + message.getActionNamespace());
/*     */     
/* 297 */     for (int i = 0; i < bodyChildren.getLength(); i++) {
/* 298 */       Node bodyChild = bodyChildren.item(i);
/*     */       
/* 300 */       if (bodyChild.getNodeType() == 1) {
/*     */ 
/*     */         
/* 303 */         String unprefixedName = getUnprefixedNodeName(bodyChild);
/* 304 */         if (unprefixedName.equals(actionInvocation.getAction().getName())) {
/* 305 */           if (bodyChild.getNamespaceURI() == null || 
/* 306 */             !bodyChild.getNamespaceURI().equals(message.getActionNamespace())) {
/* 307 */             throw new UnsupportedDataException("Illegal or missing namespace on action request element: " + bodyChild);
/*     */           }
/*     */           
/* 310 */           log.fine("Reading action request element: " + unprefixedName);
/* 311 */           return (Element)bodyChild;
/*     */         } 
/*     */       } 
/* 314 */     }  throw new UnsupportedDataException("Could not read action request element matching namespace: " + message
/* 315 */         .getActionNamespace());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Element writeActionResponseElement(Document d, Element bodyElement, ActionResponseMessage message, ActionInvocation actionInvocation) {
/* 326 */     log.fine("Writing action response element: " + actionInvocation.getAction().getName());
/* 327 */     Element actionResponseElement = d.createElementNS(message
/* 328 */         .getActionNamespace(), "u:" + actionInvocation
/* 329 */         .getAction().getName() + "Response");
/*     */     
/* 331 */     bodyElement.appendChild(actionResponseElement);
/*     */     
/* 333 */     return actionResponseElement;
/*     */   }
/*     */   
/*     */   protected Element readActionResponseElement(Element bodyElement, ActionInvocation actionInvocation) {
/* 337 */     NodeList bodyChildren = bodyElement.getChildNodes();
/*     */     
/* 339 */     for (int i = 0; i < bodyChildren.getLength(); i++) {
/* 340 */       Node bodyChild = bodyChildren.item(i);
/*     */       
/* 342 */       if (bodyChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 345 */         if (getUnprefixedNodeName(bodyChild).equals(actionInvocation.getAction().getName() + "Response")) {
/* 346 */           log.fine("Reading action response element: " + getUnprefixedNodeName(bodyChild));
/* 347 */           return (Element)bodyChild;
/*     */         }  } 
/*     */     } 
/* 350 */     log.fine("Could not read action response element");
/* 351 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeActionInputArguments(Document d, Element actionRequestElement, ActionInvocation actionInvocation) {
/* 360 */     for (ActionArgument argument : actionInvocation.getAction().getInputArguments()) {
/* 361 */       log.fine("Writing action input argument: " + argument.getName());
/* 362 */       String value = (actionInvocation.getInput(argument) != null) ? actionInvocation.getInput(argument).toString() : "";
/* 363 */       XMLUtil.appendNewElement(d, actionRequestElement, argument.getName(), value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readActionInputArguments(Element actionRequestElement, ActionInvocation actionInvocation) throws ActionException {
/* 369 */     actionInvocation.setInput(
/* 370 */         readArgumentValues(actionRequestElement
/* 371 */           .getChildNodes(), actionInvocation
/* 372 */           .getAction().getInputArguments()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeActionOutputArguments(Document d, Element actionResponseElement, ActionInvocation actionInvocation) {
/* 383 */     for (ActionArgument argument : actionInvocation.getAction().getOutputArguments()) {
/* 384 */       log.fine("Writing action output argument: " + argument.getName());
/* 385 */       String value = (actionInvocation.getOutput(argument) != null) ? actionInvocation.getOutput(argument).toString() : "";
/* 386 */       XMLUtil.appendNewElement(d, actionResponseElement, argument.getName(), value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readActionOutputArguments(Element actionResponseElement, ActionInvocation actionInvocation) throws ActionException {
/* 393 */     actionInvocation.setOutput(
/* 394 */         readArgumentValues(actionResponseElement
/* 395 */           .getChildNodes(), actionInvocation
/* 396 */           .getAction().getOutputArguments()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeFaultElement(Document d, Element bodyElement, ActionInvocation actionInvocation) {
/* 405 */     Element faultElement = d.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "s:Fault");
/* 406 */     bodyElement.appendChild(faultElement);
/*     */ 
/*     */     
/* 409 */     XMLUtil.appendNewElement(d, faultElement, "faultcode", "s:Client");
/* 410 */     XMLUtil.appendNewElement(d, faultElement, "faultstring", "UPnPError");
/*     */     
/* 412 */     Element detailElement = d.createElement("detail");
/* 413 */     faultElement.appendChild(detailElement);
/*     */     
/* 415 */     Element upnpErrorElement = d.createElementNS("urn:schemas-upnp-org:control-1-0", "UPnPError");
/* 416 */     detailElement.appendChild(upnpErrorElement);
/*     */     
/* 418 */     int errorCode = actionInvocation.getFailure().getErrorCode();
/* 419 */     String errorDescription = actionInvocation.getFailure().getMessage();
/*     */     
/* 421 */     log.fine("Writing fault element: " + errorCode + " - " + errorDescription);
/*     */     
/* 423 */     XMLUtil.appendNewElement(d, upnpErrorElement, "errorCode", Integer.toString(errorCode));
/* 424 */     XMLUtil.appendNewElement(d, upnpErrorElement, "errorDescription", errorDescription);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActionException readFaultElement(Element bodyElement) {
/* 430 */     boolean receivedFaultElement = false;
/* 431 */     String errorCode = null;
/* 432 */     String errorDescription = null;
/*     */     
/* 434 */     NodeList bodyChildren = bodyElement.getChildNodes();
/*     */     
/* 436 */     for (int i = 0; i < bodyChildren.getLength(); i++) {
/* 437 */       Node bodyChild = bodyChildren.item(i);
/*     */       
/* 439 */       if (bodyChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 442 */         if (getUnprefixedNodeName(bodyChild).equals("Fault")) {
/*     */           
/* 444 */           receivedFaultElement = true;
/*     */           
/* 446 */           NodeList faultChildren = bodyChild.getChildNodes();
/*     */           
/* 448 */           for (int j = 0; j < faultChildren.getLength(); j++) {
/* 449 */             Node faultChild = faultChildren.item(j);
/*     */             
/* 451 */             if (faultChild.getNodeType() == 1)
/*     */             {
/*     */               
/* 454 */               if (getUnprefixedNodeName(faultChild).equals("detail")) {
/*     */                 
/* 456 */                 NodeList detailChildren = faultChild.getChildNodes();
/* 457 */                 for (int x = 0; x < detailChildren.getLength(); x++) {
/* 458 */                   Node detailChild = detailChildren.item(x);
/*     */                   
/* 460 */                   if (detailChild.getNodeType() == 1)
/*     */                   {
/*     */                     
/* 463 */                     if (getUnprefixedNodeName(detailChild).equals("UPnPError")) {
/*     */                       
/* 465 */                       NodeList errorChildren = detailChild.getChildNodes();
/* 466 */                       for (int y = 0; y < errorChildren.getLength(); y++) {
/* 467 */                         Node errorChild = errorChildren.item(y);
/*     */                         
/* 469 */                         if (errorChild.getNodeType() == 1) {
/*     */ 
/*     */                           
/* 472 */                           if (getUnprefixedNodeName(errorChild).equals("errorCode")) {
/* 473 */                             errorCode = XMLUtil.getTextContent(errorChild);
/*     */                           }
/* 475 */                           if (getUnprefixedNodeName(errorChild).equals("errorDescription"))
/* 476 */                             errorDescription = XMLUtil.getTextContent(errorChild); 
/*     */                         } 
/*     */                       } 
/*     */                     }  } 
/*     */                 } 
/*     */               }  } 
/*     */           } 
/*     */         }  } 
/*     */     } 
/* 485 */     if (errorCode != null)
/*     */       try {
/* 487 */         int numericCode = Integer.valueOf(errorCode).intValue();
/* 488 */         ErrorCode standardErrorCode = ErrorCode.getByCode(numericCode);
/* 489 */         if (standardErrorCode != null) {
/* 490 */           log.fine("Reading fault element: " + standardErrorCode.getCode() + " - " + errorDescription);
/* 491 */           return new ActionException(standardErrorCode, errorDescription, false);
/*     */         } 
/* 493 */         log.fine("Reading fault element: " + numericCode + " - " + errorDescription);
/* 494 */         return new ActionException(numericCode, errorDescription);
/*     */       }
/* 496 */       catch (NumberFormatException ex) {
/* 497 */         throw new RuntimeException("Error code was not a number");
/*     */       }  
/* 499 */     if (receivedFaultElement) {
/* 500 */       throw new RuntimeException("Received fault element but no error code");
/*     */     }
/* 502 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getMessageBody(ActionMessage message) throws UnsupportedDataException {
/* 509 */     if (!message.isBodyNonEmptyString()) {
/* 510 */       throw new UnsupportedDataException("Can't transform null or non-string/zero-length body of: " + message);
/*     */     }
/*     */     
/* 513 */     return message.getBodyString().trim();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String toString(Document d) throws Exception {
/* 518 */     String output = XMLUtil.documentToString(d);
/* 519 */     while (output.endsWith("\n") || output.endsWith("\r")) {
/* 520 */       output = output.substring(0, output.length() - 1);
/*     */     }
/*     */     
/* 523 */     return output;
/*     */   }
/*     */   
/*     */   protected String getUnprefixedNodeName(Node node) {
/* 527 */     return (node.getPrefix() != null) ? node
/* 528 */       .getNodeName().substring(node.getPrefix().length() + 1) : node
/* 529 */       .getNodeName();
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
/*     */   protected ActionArgumentValue[] readArgumentValues(NodeList nodeList, ActionArgument[] args) throws ActionException {
/* 541 */     List<Node> nodes = getMatchingNodes(nodeList, args);
/*     */     
/* 543 */     ActionArgumentValue[] values = new ActionArgumentValue[args.length];
/*     */     
/* 545 */     for (int i = 0; i < args.length; i++) {
/*     */       
/* 547 */       ActionArgument arg = args[i];
/* 548 */       Node node = findActionArgumentNode(nodes, arg);
/* 549 */       if (node == null) {
/* 550 */         throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Could not find argument '" + arg
/*     */             
/* 552 */             .getName() + "' node");
/*     */       }
/* 554 */       log.fine("Reading action argument: " + arg.getName());
/* 555 */       String value = XMLUtil.getTextContent(node);
/* 556 */       values[i] = createValue(arg, value);
/*     */     } 
/* 558 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Node> getMatchingNodes(NodeList nodeList, ActionArgument[] args) throws ActionException {
/* 567 */     List<String> names = new ArrayList<>();
/* 568 */     for (ActionArgument argument : args) {
/* 569 */       names.add(argument.getName());
/* 570 */       names.addAll(Arrays.asList(argument.getAliases()));
/*     */     } 
/*     */     
/* 573 */     List<Node> matches = new ArrayList<>();
/* 574 */     for (int i = 0; i < nodeList.getLength(); i++) {
/* 575 */       Node child = nodeList.item(i);
/*     */       
/* 577 */       if (child.getNodeType() == 1)
/*     */       {
/*     */         
/* 580 */         if (names.contains(getUnprefixedNodeName(child)))
/* 581 */           matches.add(child); 
/*     */       }
/*     */     } 
/* 584 */     if (matches.size() < args.length) {
/* 585 */       throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Invalid number of input or output arguments in XML message, expected " + args.length + " but found " + matches
/*     */           
/* 587 */           .size());
/*     */     }
/*     */     
/* 590 */     return matches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActionArgumentValue createValue(ActionArgument arg, String value) throws ActionException {
/*     */     try {
/* 600 */       return new ActionArgumentValue(arg, value);
/* 601 */     } catch (InvalidValueException ex) {
/* 602 */       throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Wrong type or invalid value for '" + arg
/*     */           
/* 604 */           .getName() + "': " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node findActionArgumentNode(List<Node> nodes, ActionArgument arg) {
/* 615 */     for (Node node : nodes) {
/* 616 */       if (arg.isNameOrAlias(getUnprefixedNodeName(node))) return node; 
/*     */     } 
/* 618 */     return null;
/*     */   }
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 622 */     log.warning(e.toString());
/*     */   }
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 626 */     throw e;
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 630 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\SOAPActionProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
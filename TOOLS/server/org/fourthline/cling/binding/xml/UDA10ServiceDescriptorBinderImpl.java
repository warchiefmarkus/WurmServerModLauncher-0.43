/*     */ package org.fourthline.cling.binding.xml;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.fourthline.cling.binding.staging.MutableAction;
/*     */ import org.fourthline.cling.binding.staging.MutableActionArgument;
/*     */ import org.fourthline.cling.binding.staging.MutableAllowedValueRange;
/*     */ import org.fourthline.cling.binding.staging.MutableService;
/*     */ import org.fourthline.cling.binding.staging.MutableStateVariable;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.XMLUtil;
/*     */ import org.fourthline.cling.model.meta.Action;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.meta.StateVariableEventDetails;
/*     */ import org.fourthline.cling.model.types.CustomDatatype;
/*     */ import org.fourthline.cling.model.types.Datatype;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UDA10ServiceDescriptorBinderImpl
/*     */   implements ServiceDescriptorBinder, ErrorHandler
/*     */ {
/*  63 */   private static Logger log = Logger.getLogger(ServiceDescriptorBinder.class.getName());
/*     */   
/*     */   public <S extends Service> S describe(S undescribedService, String descriptorXml) throws DescriptorBindingException, ValidationException {
/*  66 */     if (descriptorXml == null || descriptorXml.length() == 0) {
/*  67 */       throw new DescriptorBindingException("Null or empty descriptor");
/*     */     }
/*     */     
/*     */     try {
/*  71 */       log.fine("Populating service from XML descriptor: " + undescribedService);
/*     */       
/*  73 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  74 */       factory.setNamespaceAware(true);
/*  75 */       DocumentBuilder documentBuilder = factory.newDocumentBuilder();
/*  76 */       documentBuilder.setErrorHandler(this);
/*     */       
/*  78 */       Document d = documentBuilder.parse(new InputSource(new StringReader(descriptorXml
/*     */ 
/*     */               
/*  81 */               .trim())));
/*     */ 
/*     */ 
/*     */       
/*  85 */       return describe(undescribedService, d);
/*     */     }
/*  87 */     catch (ValidationException ex) {
/*  88 */       throw ex;
/*  89 */     } catch (Exception ex) {
/*  90 */       throw new DescriptorBindingException("Could not parse service descriptor: " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public <S extends Service> S describe(S undescribedService, Document dom) throws DescriptorBindingException, ValidationException {
/*     */     try {
/*  96 */       log.fine("Populating service from DOM: " + undescribedService);
/*     */ 
/*     */       
/*  99 */       MutableService descriptor = new MutableService();
/*     */       
/* 101 */       hydrateBasic(descriptor, (Service)undescribedService);
/*     */       
/* 103 */       Element rootElement = dom.getDocumentElement();
/* 104 */       hydrateRoot(descriptor, rootElement);
/*     */ 
/*     */       
/* 107 */       return buildInstance(undescribedService, descriptor);
/*     */     }
/* 109 */     catch (ValidationException ex) {
/* 110 */       throw ex;
/* 111 */     } catch (Exception ex) {
/* 112 */       throw new DescriptorBindingException("Could not parse service DOM: " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected <S extends Service> S buildInstance(S undescribedService, MutableService descriptor) throws ValidationException {
/* 117 */     return (S)descriptor.build(undescribedService.getDevice());
/*     */   }
/*     */   
/*     */   protected void hydrateBasic(MutableService descriptor, Service undescribedService) {
/* 121 */     descriptor.serviceId = undescribedService.getServiceId();
/* 122 */     descriptor.serviceType = undescribedService.getServiceType();
/* 123 */     if (undescribedService instanceof RemoteService) {
/* 124 */       RemoteService rs = (RemoteService)undescribedService;
/* 125 */       descriptor.controlURI = rs.getControlURI();
/* 126 */       descriptor.eventSubscriptionURI = rs.getEventSubscriptionURI();
/* 127 */       descriptor.descriptorURI = rs.getDescriptorURI();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void hydrateRoot(MutableService descriptor, Element rootElement) throws DescriptorBindingException {
/* 136 */     if (!Descriptor.Service.ELEMENT.scpd.equals(rootElement)) {
/* 137 */       throw new DescriptorBindingException("Root element name is not <scpd>: " + rootElement.getNodeName());
/*     */     }
/*     */     
/* 140 */     NodeList rootChildren = rootElement.getChildNodes();
/*     */     
/* 142 */     for (int i = 0; i < rootChildren.getLength(); i++) {
/* 143 */       Node rootChild = rootChildren.item(i);
/*     */       
/* 145 */       if (rootChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 148 */         if (!Descriptor.Service.ELEMENT.specVersion.equals(rootChild))
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 153 */           if (Descriptor.Service.ELEMENT.actionList.equals(rootChild)) {
/* 154 */             hydrateActionList(descriptor, rootChild);
/* 155 */           } else if (Descriptor.Service.ELEMENT.serviceStateTable.equals(rootChild)) {
/* 156 */             hydrateServiceStateTableList(descriptor, rootChild);
/*     */           } else {
/* 158 */             log.finer("Ignoring unknown element: " + rootChild.getNodeName());
/*     */           } 
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
/*     */   public void hydrateActionList(MutableService descriptor, Node actionListNode) throws DescriptorBindingException {
/* 187 */     NodeList actionListChildren = actionListNode.getChildNodes();
/* 188 */     for (int i = 0; i < actionListChildren.getLength(); i++) {
/* 189 */       Node actionListChild = actionListChildren.item(i);
/*     */       
/* 191 */       if (actionListChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 194 */         if (Descriptor.Service.ELEMENT.action.equals(actionListChild)) {
/* 195 */           MutableAction action = new MutableAction();
/* 196 */           hydrateAction(action, actionListChild);
/* 197 */           descriptor.actions.add(action);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hydrateAction(MutableAction action, Node actionNode) {
/* 204 */     NodeList actionNodeChildren = actionNode.getChildNodes();
/* 205 */     for (int i = 0; i < actionNodeChildren.getLength(); i++) {
/* 206 */       Node actionNodeChild = actionNodeChildren.item(i);
/*     */       
/* 208 */       if (actionNodeChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 211 */         if (Descriptor.Service.ELEMENT.name.equals(actionNodeChild)) {
/* 212 */           action.name = XMLUtil.getTextContent(actionNodeChild);
/* 213 */         } else if (Descriptor.Service.ELEMENT.argumentList.equals(actionNodeChild)) {
/*     */ 
/*     */           
/* 216 */           NodeList argumentChildren = actionNodeChild.getChildNodes();
/* 217 */           for (int j = 0; j < argumentChildren.getLength(); j++) {
/* 218 */             Node argumentChild = argumentChildren.item(j);
/*     */             
/* 220 */             if (argumentChild.getNodeType() == 1) {
/*     */ 
/*     */               
/* 223 */               MutableActionArgument actionArgument = new MutableActionArgument();
/* 224 */               hydrateActionArgument(actionArgument, argumentChild);
/* 225 */               action.arguments.add(actionArgument);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hydrateActionArgument(MutableActionArgument actionArgument, Node actionArgumentNode) {
/* 234 */     NodeList argumentNodeChildren = actionArgumentNode.getChildNodes();
/* 235 */     for (int i = 0; i < argumentNodeChildren.getLength(); i++) {
/* 236 */       Node argumentNodeChild = argumentNodeChildren.item(i);
/*     */       
/* 238 */       if (argumentNodeChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 241 */         if (Descriptor.Service.ELEMENT.name.equals(argumentNodeChild)) {
/* 242 */           actionArgument.name = XMLUtil.getTextContent(argumentNodeChild);
/* 243 */         } else if (Descriptor.Service.ELEMENT.direction.equals(argumentNodeChild)) {
/* 244 */           String directionString = XMLUtil.getTextContent(argumentNodeChild);
/*     */           try {
/* 246 */             actionArgument.direction = ActionArgument.Direction.valueOf(directionString.toUpperCase(Locale.ROOT));
/* 247 */           } catch (IllegalArgumentException ex) {
/*     */             
/* 249 */             log.warning("UPnP specification violation: Invalid action argument direction, assuming 'IN': " + directionString);
/* 250 */             actionArgument.direction = ActionArgument.Direction.IN;
/*     */           } 
/* 252 */         } else if (Descriptor.Service.ELEMENT.relatedStateVariable.equals(argumentNodeChild)) {
/* 253 */           actionArgument.relatedStateVariable = XMLUtil.getTextContent(argumentNodeChild);
/* 254 */         } else if (Descriptor.Service.ELEMENT.retval.equals(argumentNodeChild)) {
/* 255 */           actionArgument.retval = true;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hydrateServiceStateTableList(MutableService descriptor, Node serviceStateTableNode) {
/* 262 */     NodeList serviceStateTableChildren = serviceStateTableNode.getChildNodes();
/* 263 */     for (int i = 0; i < serviceStateTableChildren.getLength(); i++) {
/* 264 */       Node serviceStateTableChild = serviceStateTableChildren.item(i);
/*     */       
/* 266 */       if (serviceStateTableChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 269 */         if (Descriptor.Service.ELEMENT.stateVariable.equals(serviceStateTableChild)) {
/* 270 */           MutableStateVariable stateVariable = new MutableStateVariable();
/* 271 */           hydrateStateVariable(stateVariable, (Element)serviceStateTableChild);
/* 272 */           descriptor.stateVariables.add(stateVariable);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hydrateStateVariable(MutableStateVariable stateVariable, Element stateVariableElement) {
/* 279 */     stateVariable
/*     */       
/* 281 */       .eventDetails = new StateVariableEventDetails((stateVariableElement.getAttribute("sendEvents") != null && stateVariableElement.getAttribute(Descriptor.Service.ATTRIBUTE.sendEvents.toString()).toUpperCase(Locale.ROOT).equals("YES")));
/*     */ 
/*     */     
/* 284 */     NodeList stateVariableChildren = stateVariableElement.getChildNodes();
/* 285 */     for (int i = 0; i < stateVariableChildren.getLength(); i++) {
/* 286 */       Node stateVariableChild = stateVariableChildren.item(i);
/*     */       
/* 288 */       if (stateVariableChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 291 */         if (Descriptor.Service.ELEMENT.name.equals(stateVariableChild)) {
/* 292 */           stateVariable.name = XMLUtil.getTextContent(stateVariableChild);
/* 293 */         } else if (Descriptor.Service.ELEMENT.dataType.equals(stateVariableChild)) {
/* 294 */           String dtName = XMLUtil.getTextContent(stateVariableChild);
/* 295 */           Datatype.Builtin builtin = Datatype.Builtin.getByDescriptorName(dtName);
/* 296 */           stateVariable.dataType = (builtin != null) ? builtin.getDatatype() : (Datatype)new CustomDatatype(dtName);
/* 297 */         } else if (Descriptor.Service.ELEMENT.defaultValue.equals(stateVariableChild)) {
/* 298 */           stateVariable.defaultValue = XMLUtil.getTextContent(stateVariableChild);
/* 299 */         } else if (Descriptor.Service.ELEMENT.allowedValueList.equals(stateVariableChild)) {
/*     */           
/* 301 */           List<String> allowedValues = new ArrayList<>();
/*     */           
/* 303 */           NodeList allowedValueListChildren = stateVariableChild.getChildNodes();
/* 304 */           for (int j = 0; j < allowedValueListChildren.getLength(); j++) {
/* 305 */             Node allowedValueListChild = allowedValueListChildren.item(j);
/*     */             
/* 307 */             if (allowedValueListChild.getNodeType() == 1)
/*     */             {
/*     */               
/* 310 */               if (Descriptor.Service.ELEMENT.allowedValue.equals(allowedValueListChild))
/* 311 */                 allowedValues.add(XMLUtil.getTextContent(allowedValueListChild)); 
/*     */             }
/*     */           } 
/* 314 */           stateVariable.allowedValues = allowedValues;
/*     */         }
/* 316 */         else if (Descriptor.Service.ELEMENT.allowedValueRange.equals(stateVariableChild)) {
/*     */           
/* 318 */           MutableAllowedValueRange range = new MutableAllowedValueRange();
/*     */           
/* 320 */           NodeList allowedValueRangeChildren = stateVariableChild.getChildNodes();
/* 321 */           for (int j = 0; j < allowedValueRangeChildren.getLength(); j++) {
/* 322 */             Node allowedValueRangeChild = allowedValueRangeChildren.item(j);
/*     */             
/* 324 */             if (allowedValueRangeChild.getNodeType() == 1)
/*     */             {
/*     */               
/* 327 */               if (Descriptor.Service.ELEMENT.minimum.equals(allowedValueRangeChild)) {
/*     */                 try {
/* 329 */                   range.minimum = Long.valueOf(XMLUtil.getTextContent(allowedValueRangeChild));
/* 330 */                 } catch (Exception exception) {}
/*     */               }
/* 332 */               else if (Descriptor.Service.ELEMENT.maximum.equals(allowedValueRangeChild)) {
/*     */                 try {
/* 334 */                   range.maximum = Long.valueOf(XMLUtil.getTextContent(allowedValueRangeChild));
/* 335 */                 } catch (Exception exception) {}
/*     */               }
/* 337 */               else if (Descriptor.Service.ELEMENT.step.equals(allowedValueRangeChild)) {
/*     */                 try {
/* 339 */                   range.step = Long.valueOf(XMLUtil.getTextContent(allowedValueRangeChild));
/* 340 */                 } catch (Exception exception) {}
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 345 */           stateVariable.allowedValueRange = range;
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String generate(Service service) throws DescriptorBindingException {
/*     */     try {
/* 352 */       log.fine("Generating XML descriptor from service model: " + service);
/*     */       
/* 354 */       return XMLUtil.documentToString(buildDOM(service));
/*     */     }
/* 356 */     catch (Exception ex) {
/* 357 */       throw new DescriptorBindingException("Could not build DOM: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Document buildDOM(Service service) throws DescriptorBindingException {
/*     */     try {
/* 364 */       log.fine("Generating XML descriptor from service model: " + service);
/*     */       
/* 366 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 367 */       factory.setNamespaceAware(true);
/*     */       
/* 369 */       Document d = factory.newDocumentBuilder().newDocument();
/* 370 */       generateScpd(service, d);
/*     */       
/* 372 */       return d;
/*     */     }
/* 374 */     catch (Exception ex) {
/* 375 */       throw new DescriptorBindingException("Could not generate service descriptor: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateScpd(Service serviceModel, Document descriptor) {
/* 381 */     Element scpdElement = descriptor.createElementNS("urn:schemas-upnp-org:service-1-0", Descriptor.Service.ELEMENT.scpd.toString());
/* 382 */     descriptor.appendChild(scpdElement);
/*     */     
/* 384 */     generateSpecVersion(serviceModel, descriptor, scpdElement);
/* 385 */     if (serviceModel.hasActions()) {
/* 386 */       generateActionList(serviceModel, descriptor, scpdElement);
/*     */     }
/* 388 */     generateServiceStateTable(serviceModel, descriptor, scpdElement);
/*     */   }
/*     */   
/*     */   private void generateSpecVersion(Service serviceModel, Document descriptor, Element rootElement) {
/* 392 */     Element specVersionElement = XMLUtil.appendNewElement(descriptor, rootElement, Descriptor.Service.ELEMENT.specVersion);
/* 393 */     XMLUtil.appendNewElementIfNotNull(descriptor, specVersionElement, Descriptor.Service.ELEMENT.major, Integer.valueOf(serviceModel.getDevice().getVersion().getMajor()));
/* 394 */     XMLUtil.appendNewElementIfNotNull(descriptor, specVersionElement, Descriptor.Service.ELEMENT.minor, Integer.valueOf(serviceModel.getDevice().getVersion().getMinor()));
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateActionList(Service serviceModel, Document descriptor, Element scpdElement) {
/* 399 */     Element actionListElement = XMLUtil.appendNewElement(descriptor, scpdElement, Descriptor.Service.ELEMENT.actionList);
/*     */     
/* 401 */     for (Action action : serviceModel.getActions()) {
/* 402 */       if (!action.getName().equals("QueryStateVariable")) {
/* 403 */         generateAction(action, descriptor, actionListElement);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void generateAction(Action action, Document descriptor, Element actionListElement) {
/* 409 */     Element actionElement = XMLUtil.appendNewElement(descriptor, actionListElement, Descriptor.Service.ELEMENT.action);
/*     */     
/* 411 */     XMLUtil.appendNewElementIfNotNull(descriptor, actionElement, Descriptor.Service.ELEMENT.name, action.getName());
/*     */     
/* 413 */     if (action.hasArguments()) {
/* 414 */       Element argumentListElement = XMLUtil.appendNewElement(descriptor, actionElement, Descriptor.Service.ELEMENT.argumentList);
/* 415 */       for (ActionArgument actionArgument : action.getArguments()) {
/* 416 */         generateActionArgument(actionArgument, descriptor, argumentListElement);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateActionArgument(ActionArgument actionArgument, Document descriptor, Element actionElement) {
/* 423 */     Element actionArgumentElement = XMLUtil.appendNewElement(descriptor, actionElement, Descriptor.Service.ELEMENT.argument);
/*     */     
/* 425 */     XMLUtil.appendNewElementIfNotNull(descriptor, actionArgumentElement, Descriptor.Service.ELEMENT.name, actionArgument.getName());
/* 426 */     XMLUtil.appendNewElementIfNotNull(descriptor, actionArgumentElement, Descriptor.Service.ELEMENT.direction, actionArgument.getDirection().toString().toLowerCase(Locale.ROOT));
/* 427 */     if (actionArgument.isReturnValue())
/*     */     {
/* 429 */       log.warning("UPnP specification violation: Not producing <retval> element to be compatible with WMP12: " + actionArgument);
/*     */     }
/*     */     
/* 432 */     XMLUtil.appendNewElementIfNotNull(descriptor, actionArgumentElement, Descriptor.Service.ELEMENT.relatedStateVariable, actionArgument.getRelatedStateVariableName());
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateServiceStateTable(Service serviceModel, Document descriptor, Element scpdElement) {
/* 437 */     Element serviceStateTableElement = XMLUtil.appendNewElement(descriptor, scpdElement, Descriptor.Service.ELEMENT.serviceStateTable);
/*     */     
/* 439 */     for (StateVariable stateVariable : serviceModel.getStateVariables()) {
/* 440 */       generateStateVariable(stateVariable, descriptor, serviceStateTableElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateStateVariable(StateVariable stateVariable, Document descriptor, Element serviveStateTableElement) {
/* 446 */     Element stateVariableElement = XMLUtil.appendNewElement(descriptor, serviveStateTableElement, Descriptor.Service.ELEMENT.stateVariable);
/*     */     
/* 448 */     XMLUtil.appendNewElementIfNotNull(descriptor, stateVariableElement, Descriptor.Service.ELEMENT.name, stateVariable.getName());
/*     */     
/* 450 */     if (stateVariable.getTypeDetails().getDatatype() instanceof CustomDatatype) {
/* 451 */       XMLUtil.appendNewElementIfNotNull(descriptor, stateVariableElement, Descriptor.Service.ELEMENT.dataType, ((CustomDatatype)stateVariable
/* 452 */           .getTypeDetails().getDatatype()).getName());
/*     */     } else {
/* 454 */       XMLUtil.appendNewElementIfNotNull(descriptor, stateVariableElement, Descriptor.Service.ELEMENT.dataType, stateVariable
/* 455 */           .getTypeDetails().getDatatype().getBuiltin().getDescriptorName());
/*     */     } 
/*     */     
/* 458 */     XMLUtil.appendNewElementIfNotNull(descriptor, stateVariableElement, Descriptor.Service.ELEMENT.defaultValue, stateVariable
/* 459 */         .getTypeDetails().getDefaultValue());
/*     */ 
/*     */     
/* 462 */     if (stateVariable.getEventDetails().isSendEvents()) {
/* 463 */       stateVariableElement.setAttribute(Descriptor.Service.ATTRIBUTE.sendEvents.toString(), "yes");
/*     */     } else {
/* 465 */       stateVariableElement.setAttribute(Descriptor.Service.ATTRIBUTE.sendEvents.toString(), "no");
/*     */     } 
/*     */     
/* 468 */     if (stateVariable.getTypeDetails().getAllowedValues() != null) {
/* 469 */       Element allowedValueListElement = XMLUtil.appendNewElement(descriptor, stateVariableElement, Descriptor.Service.ELEMENT.allowedValueList);
/* 470 */       for (String allowedValue : stateVariable.getTypeDetails().getAllowedValues()) {
/* 471 */         XMLUtil.appendNewElementIfNotNull(descriptor, allowedValueListElement, Descriptor.Service.ELEMENT.allowedValue, allowedValue);
/*     */       }
/*     */     } 
/*     */     
/* 475 */     if (stateVariable.getTypeDetails().getAllowedValueRange() != null) {
/* 476 */       Element allowedValueRangeElement = XMLUtil.appendNewElement(descriptor, stateVariableElement, Descriptor.Service.ELEMENT.allowedValueRange);
/* 477 */       XMLUtil.appendNewElementIfNotNull(descriptor, allowedValueRangeElement, Descriptor.Service.ELEMENT.minimum, 
/* 478 */           Long.valueOf(stateVariable.getTypeDetails().getAllowedValueRange().getMinimum()));
/*     */       
/* 480 */       XMLUtil.appendNewElementIfNotNull(descriptor, allowedValueRangeElement, Descriptor.Service.ELEMENT.maximum, 
/* 481 */           Long.valueOf(stateVariable.getTypeDetails().getAllowedValueRange().getMaximum()));
/*     */       
/* 483 */       if (stateVariable.getTypeDetails().getAllowedValueRange().getStep() >= 1L) {
/* 484 */         XMLUtil.appendNewElementIfNotNull(descriptor, allowedValueRangeElement, Descriptor.Service.ELEMENT.step, 
/* 485 */             Long.valueOf(stateVariable.getTypeDetails().getAllowedValueRange().getStep()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 493 */     log.warning(e.toString());
/*     */   }
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 497 */     throw e;
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 501 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\UDA10ServiceDescriptorBinderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
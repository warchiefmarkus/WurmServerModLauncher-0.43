/*     */ package org.fourthline.cling.binding.xml;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.binding.staging.MutableAction;
/*     */ import org.fourthline.cling.binding.staging.MutableActionArgument;
/*     */ import org.fourthline.cling.binding.staging.MutableAllowedValueRange;
/*     */ import org.fourthline.cling.binding.staging.MutableService;
/*     */ import org.fourthline.cling.binding.staging.MutableStateVariable;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.meta.StateVariableEventDetails;
/*     */ import org.fourthline.cling.model.types.CustomDatatype;
/*     */ import org.fourthline.cling.model.types.Datatype;
/*     */ import org.seamless.xml.SAXParser;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class UDA10ServiceDescriptorBinderSAXImpl
/*     */   extends UDA10ServiceDescriptorBinderImpl
/*     */ {
/*  50 */   private static Logger log = Logger.getLogger(ServiceDescriptorBinder.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public <S extends Service> S describe(S undescribedService, String descriptorXml) throws DescriptorBindingException, ValidationException {
/*  55 */     if (descriptorXml == null || descriptorXml.length() == 0) {
/*  56 */       throw new DescriptorBindingException("Null or empty descriptor");
/*     */     }
/*     */     
/*     */     try {
/*  60 */       log.fine("Reading service from XML descriptor");
/*     */       
/*  62 */       SAXParser parser = new SAXParser();
/*     */       
/*  64 */       MutableService descriptor = new MutableService();
/*     */       
/*  66 */       hydrateBasic(descriptor, (Service)undescribedService);
/*     */       
/*  68 */       new RootHandler(descriptor, parser);
/*     */       
/*  70 */       parser.parse(new InputSource(new StringReader(descriptorXml
/*     */ 
/*     */               
/*  73 */               .trim())));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  78 */       return (S)descriptor.build(undescribedService.getDevice());
/*     */     }
/*  80 */     catch (ValidationException ex) {
/*  81 */       throw ex;
/*  82 */     } catch (Exception ex) {
/*  83 */       throw new DescriptorBindingException("Could not parse service descriptor: " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static class RootHandler
/*     */     extends ServiceDescriptorHandler<MutableService> {
/*     */     public RootHandler(MutableService instance, SAXParser parser) {
/*  90 */       super(instance, parser);
/*     */     }
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
/*     */     public void startElement(Descriptor.Service.ELEMENT element, Attributes attributes) throws SAXException {
/* 104 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.ActionListHandler.EL)) {
/* 105 */         List<MutableAction> actions = new ArrayList<>();
/* 106 */         ((MutableService)getInstance()).actions = actions;
/* 107 */         new UDA10ServiceDescriptorBinderSAXImpl.ActionListHandler(actions, this);
/*     */       } 
/*     */       
/* 110 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.StateVariableListHandler.EL)) {
/* 111 */         List<MutableStateVariable> stateVariables = new ArrayList<>();
/* 112 */         ((MutableService)getInstance()).stateVariables = stateVariables;
/* 113 */         new UDA10ServiceDescriptorBinderSAXImpl.StateVariableListHandler(stateVariables, this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ActionListHandler
/*     */     extends ServiceDescriptorHandler<List<MutableAction>>
/*     */   {
/* 149 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.actionList;
/*     */     
/*     */     public ActionListHandler(List<MutableAction> instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 152 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Service.ELEMENT element, Attributes attributes) throws SAXException {
/* 157 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.ActionHandler.EL)) {
/* 158 */         MutableAction action = new MutableAction();
/* 159 */         ((List<MutableAction>)getInstance()).add(action);
/* 160 */         new UDA10ServiceDescriptorBinderSAXImpl.ActionHandler(action, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 166 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ActionHandler
/*     */     extends ServiceDescriptorHandler<MutableAction> {
/* 172 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.action;
/*     */     
/*     */     public ActionHandler(MutableAction instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 175 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Service.ELEMENT element, Attributes attributes) throws SAXException {
/* 180 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.ActionArgumentListHandler.EL)) {
/* 181 */         List<MutableActionArgument> arguments = new ArrayList<>();
/* 182 */         ((MutableAction)getInstance()).arguments = arguments;
/* 183 */         new UDA10ServiceDescriptorBinderSAXImpl.ActionArgumentListHandler(arguments, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Service.ELEMENT element) throws SAXException {
/* 189 */       switch (element) {
/*     */         case name:
/* 191 */           ((MutableAction)getInstance()).name = getCharacters();
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 198 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ActionArgumentListHandler
/*     */     extends ServiceDescriptorHandler<List<MutableActionArgument>> {
/* 204 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.argumentList;
/*     */     
/*     */     public ActionArgumentListHandler(List<MutableActionArgument> instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 207 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Service.ELEMENT element, Attributes attributes) throws SAXException {
/* 212 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.ActionArgumentHandler.EL)) {
/* 213 */         MutableActionArgument argument = new MutableActionArgument();
/* 214 */         ((List<MutableActionArgument>)getInstance()).add(argument);
/* 215 */         new UDA10ServiceDescriptorBinderSAXImpl.ActionArgumentHandler(argument, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 221 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ActionArgumentHandler
/*     */     extends ServiceDescriptorHandler<MutableActionArgument> {
/* 227 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.argument;
/*     */     
/*     */     public ActionArgumentHandler(MutableActionArgument instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 230 */       super(instance, parent);
/*     */     }
/*     */     
/*     */     public void endElement(Descriptor.Service.ELEMENT element) throws SAXException {
/*     */       String directionString;
/* 235 */       switch (element) {
/*     */         case name:
/* 237 */           ((MutableActionArgument)getInstance()).name = getCharacters();
/*     */           break;
/*     */         case direction:
/* 240 */           directionString = getCharacters();
/*     */           try {
/* 242 */             ((MutableActionArgument)getInstance()).direction = ActionArgument.Direction.valueOf(directionString.toUpperCase(Locale.ROOT));
/* 243 */           } catch (IllegalArgumentException ex) {
/*     */             
/* 245 */             UDA10ServiceDescriptorBinderSAXImpl.log.warning("UPnP specification violation: Invalid action argument direction, assuming 'IN': " + directionString);
/* 246 */             ((MutableActionArgument)getInstance()).direction = ActionArgument.Direction.IN;
/*     */           } 
/*     */           break;
/*     */         case relatedStateVariable:
/* 250 */           ((MutableActionArgument)getInstance()).relatedStateVariable = getCharacters();
/*     */           break;
/*     */         case retval:
/* 253 */           ((MutableActionArgument)getInstance()).retval = true;
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 260 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class StateVariableListHandler
/*     */     extends ServiceDescriptorHandler<List<MutableStateVariable>> {
/* 266 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.serviceStateTable;
/*     */     
/*     */     public StateVariableListHandler(List<MutableStateVariable> instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 269 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Service.ELEMENT element, Attributes attributes) throws SAXException {
/* 274 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.StateVariableHandler.EL)) {
/* 275 */         MutableStateVariable stateVariable = new MutableStateVariable();
/*     */         
/* 277 */         String sendEventsAttributeValue = attributes.getValue(Descriptor.Service.ATTRIBUTE.sendEvents.toString());
/* 278 */         stateVariable
/* 279 */           .eventDetails = new StateVariableEventDetails((sendEventsAttributeValue != null && sendEventsAttributeValue.toUpperCase(Locale.ROOT).equals("YES")));
/*     */ 
/*     */         
/* 282 */         ((List<MutableStateVariable>)getInstance()).add(stateVariable);
/* 283 */         new UDA10ServiceDescriptorBinderSAXImpl.StateVariableHandler(stateVariable, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 289 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class StateVariableHandler
/*     */     extends ServiceDescriptorHandler<MutableStateVariable> {
/* 295 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.stateVariable;
/*     */     
/*     */     public StateVariableHandler(MutableStateVariable instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 298 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Service.ELEMENT element, Attributes attributes) throws SAXException {
/* 303 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.AllowedValueListHandler.EL)) {
/* 304 */         List<String> allowedValues = new ArrayList<>();
/* 305 */         ((MutableStateVariable)getInstance()).allowedValues = allowedValues;
/* 306 */         new UDA10ServiceDescriptorBinderSAXImpl.AllowedValueListHandler(allowedValues, this);
/*     */       } 
/*     */       
/* 309 */       if (element.equals(UDA10ServiceDescriptorBinderSAXImpl.AllowedValueRangeHandler.EL)) {
/* 310 */         MutableAllowedValueRange allowedValueRange = new MutableAllowedValueRange();
/* 311 */         ((MutableStateVariable)getInstance()).allowedValueRange = allowedValueRange;
/* 312 */         new UDA10ServiceDescriptorBinderSAXImpl.AllowedValueRangeHandler(allowedValueRange, this);
/*     */       } 
/*     */     }
/*     */     public void endElement(Descriptor.Service.ELEMENT element) throws SAXException {
/*     */       String dtName;
/*     */       Datatype.Builtin builtin;
/* 318 */       switch (element) {
/*     */         case name:
/* 320 */           ((MutableStateVariable)getInstance()).name = getCharacters();
/*     */           break;
/*     */         case dataType:
/* 323 */           dtName = getCharacters();
/* 324 */           builtin = Datatype.Builtin.getByDescriptorName(dtName);
/* 325 */           ((MutableStateVariable)getInstance()).dataType = (builtin != null) ? builtin.getDatatype() : (Datatype)new CustomDatatype(dtName);
/*     */           break;
/*     */         case defaultValue:
/* 328 */           ((MutableStateVariable)getInstance()).defaultValue = getCharacters();
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 335 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class AllowedValueListHandler
/*     */     extends ServiceDescriptorHandler<List<String>> {
/* 341 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.allowedValueList;
/*     */     
/*     */     public AllowedValueListHandler(List<String> instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 344 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Service.ELEMENT element) throws SAXException {
/* 349 */       switch (element) {
/*     */         case allowedValue:
/* 351 */           ((List<String>)getInstance()).add(getCharacters());
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 358 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class AllowedValueRangeHandler
/*     */     extends ServiceDescriptorHandler<MutableAllowedValueRange> {
/* 364 */     public static final Descriptor.Service.ELEMENT EL = Descriptor.Service.ELEMENT.allowedValueRange;
/*     */     
/*     */     public AllowedValueRangeHandler(MutableAllowedValueRange instance, UDA10ServiceDescriptorBinderSAXImpl.ServiceDescriptorHandler parent) {
/* 367 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Service.ELEMENT element) throws SAXException {
/*     */       try {
/* 373 */         switch (element) {
/*     */           case minimum:
/* 375 */             ((MutableAllowedValueRange)getInstance()).minimum = Long.valueOf(getCharacters());
/*     */             break;
/*     */           case maximum:
/* 378 */             ((MutableAllowedValueRange)getInstance()).maximum = Long.valueOf(getCharacters());
/*     */             break;
/*     */           case step:
/* 381 */             ((MutableAllowedValueRange)getInstance()).step = Long.valueOf(getCharacters());
/*     */             break;
/*     */         } 
/* 384 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 391 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ServiceDescriptorHandler<I>
/*     */     extends SAXParser.Handler<I> {
/*     */     public ServiceDescriptorHandler(I instance) {
/* 398 */       super(instance);
/*     */     }
/*     */     
/*     */     public ServiceDescriptorHandler(I instance, SAXParser parser) {
/* 402 */       super(instance, parser);
/*     */     }
/*     */     
/*     */     public ServiceDescriptorHandler(I instance, ServiceDescriptorHandler parent) {
/* 406 */       super(instance, parent);
/*     */     }
/*     */     
/*     */     public ServiceDescriptorHandler(I instance, SAXParser parser, ServiceDescriptorHandler parent) {
/* 410 */       super(instance, parser, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 415 */       super.startElement(uri, localName, qName, attributes);
/* 416 */       Descriptor.Service.ELEMENT el = Descriptor.Service.ELEMENT.valueOrNullOf(localName);
/* 417 */       if (el == null)
/* 418 */         return;  startElement(el, attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(String uri, String localName, String qName) throws SAXException {
/* 423 */       super.endElement(uri, localName, qName);
/* 424 */       Descriptor.Service.ELEMENT el = Descriptor.Service.ELEMENT.valueOrNullOf(localName);
/* 425 */       if (el == null)
/* 426 */         return;  endElement(el);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isLastElement(String uri, String localName, String qName) {
/* 431 */       Descriptor.Service.ELEMENT el = Descriptor.Service.ELEMENT.valueOrNullOf(localName);
/* 432 */       return (el != null && isLastElement(el));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Service.ELEMENT element, Attributes attributes) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Service.ELEMENT element) throws SAXException {}
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Service.ELEMENT element) {
/* 444 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\UDA10ServiceDescriptorBinderSAXImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.fourthline.cling.binding.xml;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.fourthline.cling.binding.staging.MutableDevice;
/*     */ import org.fourthline.cling.binding.staging.MutableIcon;
/*     */ import org.fourthline.cling.binding.staging.MutableService;
/*     */ import org.fourthline.cling.model.Namespace;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.XMLUtil;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.DeviceDetails;
/*     */ import org.fourthline.cling.model.meta.Icon;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.RemoteService;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.profile.RemoteClientInfo;
/*     */ import org.fourthline.cling.model.types.DLNACaps;
/*     */ import org.fourthline.cling.model.types.DLNADoc;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ import org.fourthline.cling.model.types.ServiceId;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.seamless.util.Exceptions;
/*     */ import org.seamless.util.MimeType;
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
/*     */ 
/*     */ public class UDA10DeviceDescriptorBinderImpl
/*     */   implements DeviceDescriptorBinder, ErrorHandler
/*     */ {
/*  69 */   private static Logger log = Logger.getLogger(DeviceDescriptorBinder.class.getName());
/*     */ 
/*     */   
/*     */   public <D extends Device> D describe(D undescribedDevice, String descriptorXml) throws DescriptorBindingException, ValidationException {
/*  73 */     if (descriptorXml == null || descriptorXml.length() == 0) {
/*  74 */       throw new DescriptorBindingException("Null or empty descriptor");
/*     */     }
/*     */     
/*     */     try {
/*  78 */       log.fine("Populating device from XML descriptor: " + undescribedDevice);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  87 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  88 */       factory.setNamespaceAware(true);
/*  89 */       DocumentBuilder documentBuilder = factory.newDocumentBuilder();
/*  90 */       documentBuilder.setErrorHandler(this);
/*     */       
/*  92 */       Document d = documentBuilder.parse(new InputSource(new StringReader(descriptorXml
/*     */ 
/*     */               
/*  95 */               .trim())));
/*     */ 
/*     */ 
/*     */       
/*  99 */       return describe(undescribedDevice, d);
/*     */     }
/* 101 */     catch (ValidationException ex) {
/* 102 */       throw ex;
/* 103 */     } catch (Exception ex) {
/* 104 */       throw new DescriptorBindingException("Could not parse device descriptor: " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public <D extends Device> D describe(D undescribedDevice, Document dom) throws DescriptorBindingException, ValidationException {
/*     */     try {
/* 110 */       log.fine("Populating device from DOM: " + undescribedDevice);
/*     */ 
/*     */       
/* 113 */       MutableDevice descriptor = new MutableDevice();
/* 114 */       Element rootElement = dom.getDocumentElement();
/* 115 */       hydrateRoot(descriptor, rootElement);
/*     */ 
/*     */       
/* 118 */       return buildInstance(undescribedDevice, descriptor);
/*     */     }
/* 120 */     catch (ValidationException ex) {
/* 121 */       throw ex;
/* 122 */     } catch (Exception ex) {
/* 123 */       throw new DescriptorBindingException("Could not parse device DOM: " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public <D extends Device> D buildInstance(D undescribedDevice, MutableDevice descriptor) throws ValidationException {
/* 128 */     return (D)descriptor.build((Device)undescribedDevice);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void hydrateRoot(MutableDevice descriptor, Element rootElement) throws DescriptorBindingException {
/* 133 */     if (rootElement.getNamespaceURI() == null || !rootElement.getNamespaceURI().equals("urn:schemas-upnp-org:device-1-0")) {
/* 134 */       log.warning("Wrong XML namespace declared on root element: " + rootElement.getNamespaceURI());
/*     */     }
/*     */     
/* 137 */     if (!rootElement.getNodeName().equals(Descriptor.Device.ELEMENT.root.name())) {
/* 138 */       throw new DescriptorBindingException("Root element name is not <root>: " + rootElement.getNodeName());
/*     */     }
/*     */     
/* 141 */     NodeList rootChildren = rootElement.getChildNodes();
/*     */     
/* 143 */     Node deviceNode = null;
/*     */     
/* 145 */     for (int i = 0; i < rootChildren.getLength(); i++) {
/* 146 */       Node rootChild = rootChildren.item(i);
/*     */       
/* 148 */       if (rootChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 151 */         if (Descriptor.Device.ELEMENT.specVersion.equals(rootChild)) {
/* 152 */           hydrateSpecVersion(descriptor, rootChild);
/* 153 */         } else if (Descriptor.Device.ELEMENT.URLBase.equals(rootChild)) {
/*     */           try {
/* 155 */             String urlString = XMLUtil.getTextContent(rootChild);
/* 156 */             if (urlString != null && urlString.length() > 0)
/*     */             {
/* 158 */               descriptor.baseURL = new URL(urlString);
/*     */             }
/* 160 */           } catch (Exception ex) {
/* 161 */             throw new DescriptorBindingException("Invalid URLBase: " + ex.getMessage());
/*     */           } 
/* 163 */         } else if (Descriptor.Device.ELEMENT.device.equals(rootChild)) {
/*     */           
/* 165 */           if (deviceNode != null)
/* 166 */             throw new DescriptorBindingException("Found multiple <device> elements in <root>"); 
/* 167 */           deviceNode = rootChild;
/*     */         } else {
/* 169 */           log.finer("Ignoring unknown element: " + rootChild.getNodeName());
/*     */         } 
/*     */       }
/*     */     } 
/* 173 */     if (deviceNode == null) {
/* 174 */       throw new DescriptorBindingException("No <device> element in <root>");
/*     */     }
/* 176 */     hydrateDevice(descriptor, deviceNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void hydrateSpecVersion(MutableDevice descriptor, Node specVersionNode) throws DescriptorBindingException {
/* 181 */     NodeList specVersionChildren = specVersionNode.getChildNodes();
/* 182 */     for (int i = 0; i < specVersionChildren.getLength(); i++) {
/* 183 */       Node specVersionChild = specVersionChildren.item(i);
/*     */       
/* 185 */       if (specVersionChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 188 */         if (Descriptor.Device.ELEMENT.major.equals(specVersionChild)) {
/* 189 */           String version = XMLUtil.getTextContent(specVersionChild).trim();
/* 190 */           if (!version.equals("1")) {
/* 191 */             log.warning("Unsupported UDA major version, ignoring: " + version);
/* 192 */             version = "1";
/*     */           } 
/* 194 */           descriptor.udaVersion.major = Integer.valueOf(version).intValue();
/* 195 */         } else if (Descriptor.Device.ELEMENT.minor.equals(specVersionChild)) {
/* 196 */           String version = XMLUtil.getTextContent(specVersionChild).trim();
/* 197 */           if (!version.equals("0")) {
/* 198 */             log.warning("Unsupported UDA minor version, ignoring: " + version);
/* 199 */             version = "0";
/*     */           } 
/* 201 */           descriptor.udaVersion.minor = Integer.valueOf(version).intValue();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void hydrateDevice(MutableDevice descriptor, Node deviceNode) throws DescriptorBindingException {
/* 210 */     NodeList deviceNodeChildren = deviceNode.getChildNodes();
/* 211 */     for (int i = 0; i < deviceNodeChildren.getLength(); i++) {
/* 212 */       Node deviceNodeChild = deviceNodeChildren.item(i);
/*     */       
/* 214 */       if (deviceNodeChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 217 */         if (Descriptor.Device.ELEMENT.deviceType.equals(deviceNodeChild)) {
/* 218 */           descriptor.deviceType = XMLUtil.getTextContent(deviceNodeChild);
/* 219 */         } else if (Descriptor.Device.ELEMENT.friendlyName.equals(deviceNodeChild)) {
/* 220 */           descriptor.friendlyName = XMLUtil.getTextContent(deviceNodeChild);
/* 221 */         } else if (Descriptor.Device.ELEMENT.manufacturer.equals(deviceNodeChild)) {
/* 222 */           descriptor.manufacturer = XMLUtil.getTextContent(deviceNodeChild);
/* 223 */         } else if (Descriptor.Device.ELEMENT.manufacturerURL.equals(deviceNodeChild)) {
/* 224 */           descriptor.manufacturerURI = parseURI(XMLUtil.getTextContent(deviceNodeChild));
/* 225 */         } else if (Descriptor.Device.ELEMENT.modelDescription.equals(deviceNodeChild)) {
/* 226 */           descriptor.modelDescription = XMLUtil.getTextContent(deviceNodeChild);
/* 227 */         } else if (Descriptor.Device.ELEMENT.modelName.equals(deviceNodeChild)) {
/* 228 */           descriptor.modelName = XMLUtil.getTextContent(deviceNodeChild);
/* 229 */         } else if (Descriptor.Device.ELEMENT.modelNumber.equals(deviceNodeChild)) {
/* 230 */           descriptor.modelNumber = XMLUtil.getTextContent(deviceNodeChild);
/* 231 */         } else if (Descriptor.Device.ELEMENT.modelURL.equals(deviceNodeChild)) {
/* 232 */           descriptor.modelURI = parseURI(XMLUtil.getTextContent(deviceNodeChild));
/* 233 */         } else if (Descriptor.Device.ELEMENT.presentationURL.equals(deviceNodeChild)) {
/* 234 */           descriptor.presentationURI = parseURI(XMLUtil.getTextContent(deviceNodeChild));
/* 235 */         } else if (Descriptor.Device.ELEMENT.UPC.equals(deviceNodeChild)) {
/* 236 */           descriptor.upc = XMLUtil.getTextContent(deviceNodeChild);
/* 237 */         } else if (Descriptor.Device.ELEMENT.serialNumber.equals(deviceNodeChild)) {
/* 238 */           descriptor.serialNumber = XMLUtil.getTextContent(deviceNodeChild);
/* 239 */         } else if (Descriptor.Device.ELEMENT.UDN.equals(deviceNodeChild)) {
/* 240 */           descriptor.udn = UDN.valueOf(XMLUtil.getTextContent(deviceNodeChild));
/* 241 */         } else if (Descriptor.Device.ELEMENT.iconList.equals(deviceNodeChild)) {
/* 242 */           hydrateIconList(descriptor, deviceNodeChild);
/* 243 */         } else if (Descriptor.Device.ELEMENT.serviceList.equals(deviceNodeChild)) {
/* 244 */           hydrateServiceList(descriptor, deviceNodeChild);
/* 245 */         } else if (Descriptor.Device.ELEMENT.deviceList.equals(deviceNodeChild)) {
/* 246 */           hydrateDeviceList(descriptor, deviceNodeChild);
/* 247 */         } else if (Descriptor.Device.ELEMENT.X_DLNADOC.equals(deviceNodeChild) && "dlna"
/* 248 */           .equals(deviceNodeChild.getPrefix())) {
/* 249 */           String txt = XMLUtil.getTextContent(deviceNodeChild);
/*     */           try {
/* 251 */             descriptor.dlnaDocs.add(DLNADoc.valueOf(txt));
/* 252 */           } catch (InvalidValueException ex) {
/* 253 */             log.info("Invalid X_DLNADOC value, ignoring value: " + txt);
/*     */           } 
/* 255 */         } else if (Descriptor.Device.ELEMENT.X_DLNACAP.equals(deviceNodeChild) && "dlna"
/* 256 */           .equals(deviceNodeChild.getPrefix())) {
/* 257 */           descriptor.dlnaCaps = DLNACaps.valueOf(XMLUtil.getTextContent(deviceNodeChild));
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hydrateIconList(MutableDevice descriptor, Node iconListNode) throws DescriptorBindingException {
/* 264 */     NodeList iconListNodeChildren = iconListNode.getChildNodes();
/* 265 */     for (int i = 0; i < iconListNodeChildren.getLength(); i++) {
/* 266 */       Node iconListNodeChild = iconListNodeChildren.item(i);
/*     */       
/* 268 */       if (iconListNodeChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 271 */         if (Descriptor.Device.ELEMENT.icon.equals(iconListNodeChild)) {
/*     */           
/* 273 */           MutableIcon icon = new MutableIcon();
/*     */           
/* 275 */           NodeList iconChildren = iconListNodeChild.getChildNodes();
/*     */           
/* 277 */           for (int x = 0; x < iconChildren.getLength(); x++) {
/* 278 */             Node iconChild = iconChildren.item(x);
/*     */             
/* 280 */             if (iconChild.getNodeType() == 1)
/*     */             {
/*     */               
/* 283 */               if (Descriptor.Device.ELEMENT.width.equals(iconChild)) {
/* 284 */                 icon.width = Integer.valueOf(XMLUtil.getTextContent(iconChild)).intValue();
/* 285 */               } else if (Descriptor.Device.ELEMENT.height.equals(iconChild)) {
/* 286 */                 icon.height = Integer.valueOf(XMLUtil.getTextContent(iconChild)).intValue();
/* 287 */               } else if (Descriptor.Device.ELEMENT.depth.equals(iconChild)) {
/* 288 */                 String depth = XMLUtil.getTextContent(iconChild);
/*     */                 try {
/* 290 */                   icon.depth = Integer.valueOf(depth).intValue();
/* 291 */                 } catch (NumberFormatException ex) {
/* 292 */                   log.warning("Invalid icon depth '" + depth + "', using 16 as default: " + ex);
/* 293 */                   icon.depth = 16;
/*     */                 } 
/* 295 */               } else if (Descriptor.Device.ELEMENT.url.equals(iconChild)) {
/* 296 */                 icon.uri = parseURI(XMLUtil.getTextContent(iconChild));
/* 297 */               } else if (Descriptor.Device.ELEMENT.mimetype.equals(iconChild)) {
/*     */                 try {
/* 299 */                   icon.mimeType = XMLUtil.getTextContent(iconChild);
/* 300 */                   MimeType.valueOf(icon.mimeType);
/* 301 */                 } catch (IllegalArgumentException ex) {
/* 302 */                   log.warning("Ignoring invalid icon mime type: " + icon.mimeType);
/* 303 */                   icon.mimeType = "";
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 309 */           descriptor.icons.add(icon);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hydrateServiceList(MutableDevice descriptor, Node serviceListNode) throws DescriptorBindingException {
/* 316 */     NodeList serviceListNodeChildren = serviceListNode.getChildNodes();
/* 317 */     for (int i = 0; i < serviceListNodeChildren.getLength(); i++) {
/* 318 */       Node serviceListNodeChild = serviceListNodeChildren.item(i);
/*     */       
/* 320 */       if (serviceListNodeChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 323 */         if (Descriptor.Device.ELEMENT.service.equals(serviceListNodeChild)) {
/*     */           
/* 325 */           NodeList serviceChildren = serviceListNodeChild.getChildNodes();
/*     */           
/*     */           try {
/* 328 */             MutableService service = new MutableService();
/*     */             
/* 330 */             for (int x = 0; x < serviceChildren.getLength(); x++) {
/* 331 */               Node serviceChild = serviceChildren.item(x);
/*     */               
/* 333 */               if (serviceChild.getNodeType() == 1)
/*     */               {
/*     */                 
/* 336 */                 if (Descriptor.Device.ELEMENT.serviceType.equals(serviceChild)) {
/* 337 */                   service.serviceType = ServiceType.valueOf(XMLUtil.getTextContent(serviceChild));
/* 338 */                 } else if (Descriptor.Device.ELEMENT.serviceId.equals(serviceChild)) {
/* 339 */                   service.serviceId = ServiceId.valueOf(XMLUtil.getTextContent(serviceChild));
/* 340 */                 } else if (Descriptor.Device.ELEMENT.SCPDURL.equals(serviceChild)) {
/* 341 */                   service.descriptorURI = parseURI(XMLUtil.getTextContent(serviceChild));
/* 342 */                 } else if (Descriptor.Device.ELEMENT.controlURL.equals(serviceChild)) {
/* 343 */                   service.controlURI = parseURI(XMLUtil.getTextContent(serviceChild));
/* 344 */                 } else if (Descriptor.Device.ELEMENT.eventSubURL.equals(serviceChild)) {
/* 345 */                   service.eventSubscriptionURI = parseURI(XMLUtil.getTextContent(serviceChild));
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/* 350 */             descriptor.services.add(service);
/* 351 */           } catch (InvalidValueException ex) {
/* 352 */             log.warning("UPnP specification violation, skipping invalid service declaration. " + ex
/* 353 */                 .getMessage());
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void hydrateDeviceList(MutableDevice descriptor, Node deviceListNode) throws DescriptorBindingException {
/* 362 */     NodeList deviceListNodeChildren = deviceListNode.getChildNodes();
/* 363 */     for (int i = 0; i < deviceListNodeChildren.getLength(); i++) {
/* 364 */       Node deviceListNodeChild = deviceListNodeChildren.item(i);
/*     */       
/* 366 */       if (deviceListNodeChild.getNodeType() == 1)
/*     */       {
/*     */         
/* 369 */         if (Descriptor.Device.ELEMENT.device.equals(deviceListNodeChild)) {
/* 370 */           MutableDevice embeddedDevice = new MutableDevice();
/* 371 */           embeddedDevice.parentDevice = descriptor;
/* 372 */           descriptor.embeddedDevices.add(embeddedDevice);
/* 373 */           hydrateDevice(embeddedDevice, deviceListNodeChild);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String generate(Device deviceModel, RemoteClientInfo info, Namespace namespace) throws DescriptorBindingException {
/*     */     try {
/* 381 */       log.fine("Generating XML descriptor from device model: " + deviceModel);
/*     */       
/* 383 */       return XMLUtil.documentToString(buildDOM(deviceModel, info, namespace));
/*     */     }
/* 385 */     catch (Exception ex) {
/* 386 */       throw new DescriptorBindingException("Could not build DOM: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Document buildDOM(Device deviceModel, RemoteClientInfo info, Namespace namespace) throws DescriptorBindingException {
/*     */     try {
/* 393 */       log.fine("Generating DOM from device model: " + deviceModel);
/*     */       
/* 395 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 396 */       factory.setNamespaceAware(true);
/*     */       
/* 398 */       Document d = factory.newDocumentBuilder().newDocument();
/* 399 */       generateRoot(namespace, deviceModel, d, info);
/*     */       
/* 401 */       return d;
/*     */     }
/* 403 */     catch (Exception ex) {
/* 404 */       throw new DescriptorBindingException("Could not generate device descriptor: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateRoot(Namespace namespace, Device deviceModel, Document descriptor, RemoteClientInfo info) {
/* 410 */     Element rootElement = descriptor.createElementNS("urn:schemas-upnp-org:device-1-0", Descriptor.Device.ELEMENT.root.toString());
/* 411 */     descriptor.appendChild(rootElement);
/*     */     
/* 413 */     generateSpecVersion(namespace, deviceModel, descriptor, rootElement);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 421 */     generateDevice(namespace, deviceModel, descriptor, rootElement, info);
/*     */   }
/*     */   
/*     */   protected void generateSpecVersion(Namespace namespace, Device deviceModel, Document descriptor, Element rootElement) {
/* 425 */     Element specVersionElement = XMLUtil.appendNewElement(descriptor, rootElement, Descriptor.Device.ELEMENT.specVersion);
/* 426 */     XMLUtil.appendNewElementIfNotNull(descriptor, specVersionElement, Descriptor.Device.ELEMENT.major, Integer.valueOf(deviceModel.getVersion().getMajor()));
/* 427 */     XMLUtil.appendNewElementIfNotNull(descriptor, specVersionElement, Descriptor.Device.ELEMENT.minor, Integer.valueOf(deviceModel.getVersion().getMinor()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateDevice(Namespace namespace, Device deviceModel, Document descriptor, Element rootElement, RemoteClientInfo info) {
/* 432 */     Element deviceElement = XMLUtil.appendNewElement(descriptor, rootElement, Descriptor.Device.ELEMENT.device);
/*     */     
/* 434 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.deviceType, deviceModel.getType());
/*     */     
/* 436 */     DeviceDetails deviceModelDetails = deviceModel.getDetails(info);
/* 437 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.friendlyName, deviceModelDetails
/*     */         
/* 439 */         .getFriendlyName());
/*     */     
/* 441 */     if (deviceModelDetails.getManufacturerDetails() != null) {
/* 442 */       XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.manufacturer, deviceModelDetails
/*     */           
/* 444 */           .getManufacturerDetails().getManufacturer());
/*     */       
/* 446 */       XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.manufacturerURL, deviceModelDetails
/*     */           
/* 448 */           .getManufacturerDetails().getManufacturerURI());
/*     */     } 
/*     */     
/* 451 */     if (deviceModelDetails.getModelDetails() != null) {
/* 452 */       XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.modelDescription, deviceModelDetails
/*     */           
/* 454 */           .getModelDetails().getModelDescription());
/*     */       
/* 456 */       XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.modelName, deviceModelDetails
/*     */           
/* 458 */           .getModelDetails().getModelName());
/*     */       
/* 460 */       XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.modelNumber, deviceModelDetails
/*     */           
/* 462 */           .getModelDetails().getModelNumber());
/*     */       
/* 464 */       XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.modelURL, deviceModelDetails
/*     */           
/* 466 */           .getModelDetails().getModelURI());
/*     */     } 
/*     */     
/* 469 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.serialNumber, deviceModelDetails
/*     */         
/* 471 */         .getSerialNumber());
/*     */     
/* 473 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.UDN, deviceModel.getIdentity().getUdn());
/* 474 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.presentationURL, deviceModelDetails
/*     */         
/* 476 */         .getPresentationURI());
/*     */     
/* 478 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, Descriptor.Device.ELEMENT.UPC, deviceModelDetails
/*     */         
/* 480 */         .getUpc());
/*     */ 
/*     */     
/* 483 */     if (deviceModelDetails.getDlnaDocs() != null) {
/* 484 */       for (DLNADoc dlnaDoc : deviceModelDetails.getDlnaDocs()) {
/* 485 */         XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, "dlna:" + Descriptor.Device.ELEMENT.X_DLNADOC, dlnaDoc, "urn:schemas-dlna-org:device-1-0");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 491 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, "dlna:" + Descriptor.Device.ELEMENT.X_DLNACAP, deviceModelDetails
/*     */         
/* 493 */         .getDlnaCaps(), "urn:schemas-dlna-org:device-1-0");
/*     */ 
/*     */     
/* 496 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, "sec:" + Descriptor.Device.ELEMENT.ProductCap, deviceModelDetails
/*     */         
/* 498 */         .getSecProductCaps(), "http://www.sec.co.kr/dlna");
/*     */ 
/*     */     
/* 501 */     XMLUtil.appendNewElementIfNotNull(descriptor, deviceElement, "sec:" + Descriptor.Device.ELEMENT.X_ProductCap, deviceModelDetails
/*     */         
/* 503 */         .getSecProductCaps(), "http://www.sec.co.kr/dlna");
/*     */ 
/*     */     
/* 506 */     generateIconList(namespace, deviceModel, descriptor, deviceElement);
/* 507 */     generateServiceList(namespace, deviceModel, descriptor, deviceElement);
/* 508 */     generateDeviceList(namespace, deviceModel, descriptor, deviceElement, info);
/*     */   }
/*     */   
/*     */   protected void generateIconList(Namespace namespace, Device deviceModel, Document descriptor, Element deviceElement) {
/* 512 */     if (!deviceModel.hasIcons())
/*     */       return; 
/* 514 */     Element iconListElement = XMLUtil.appendNewElement(descriptor, deviceElement, Descriptor.Device.ELEMENT.iconList);
/*     */     
/* 516 */     for (Icon icon : deviceModel.getIcons()) {
/* 517 */       Element iconElement = XMLUtil.appendNewElement(descriptor, iconListElement, Descriptor.Device.ELEMENT.icon);
/*     */       
/* 519 */       XMLUtil.appendNewElementIfNotNull(descriptor, iconElement, Descriptor.Device.ELEMENT.mimetype, icon.getMimeType());
/* 520 */       XMLUtil.appendNewElementIfNotNull(descriptor, iconElement, Descriptor.Device.ELEMENT.width, Integer.valueOf(icon.getWidth()));
/* 521 */       XMLUtil.appendNewElementIfNotNull(descriptor, iconElement, Descriptor.Device.ELEMENT.height, Integer.valueOf(icon.getHeight()));
/* 522 */       XMLUtil.appendNewElementIfNotNull(descriptor, iconElement, Descriptor.Device.ELEMENT.depth, Integer.valueOf(icon.getDepth()));
/* 523 */       if (deviceModel instanceof org.fourthline.cling.model.meta.RemoteDevice) {
/* 524 */         XMLUtil.appendNewElementIfNotNull(descriptor, iconElement, Descriptor.Device.ELEMENT.url, icon.getUri());
/* 525 */       } else if (deviceModel instanceof org.fourthline.cling.model.meta.LocalDevice) {
/* 526 */         XMLUtil.appendNewElementIfNotNull(descriptor, iconElement, Descriptor.Device.ELEMENT.url, namespace.getIconPath(icon));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateServiceList(Namespace namespace, Device deviceModel, Document descriptor, Element deviceElement) {
/* 532 */     if (!deviceModel.hasServices())
/*     */       return; 
/* 534 */     Element serviceListElement = XMLUtil.appendNewElement(descriptor, deviceElement, Descriptor.Device.ELEMENT.serviceList);
/*     */     
/* 536 */     for (Service service : deviceModel.getServices()) {
/* 537 */       Element serviceElement = XMLUtil.appendNewElement(descriptor, serviceListElement, Descriptor.Device.ELEMENT.service);
/*     */       
/* 539 */       XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.serviceType, service.getServiceType());
/* 540 */       XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.serviceId, service.getServiceId());
/* 541 */       if (service instanceof RemoteService) {
/* 542 */         RemoteService rs = (RemoteService)service;
/* 543 */         XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.SCPDURL, rs.getDescriptorURI());
/* 544 */         XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.controlURL, rs.getControlURI());
/* 545 */         XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.eventSubURL, rs.getEventSubscriptionURI());
/* 546 */       } else if (service instanceof LocalService) {
/* 547 */         LocalService ls = (LocalService)service;
/* 548 */         XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.SCPDURL, namespace.getDescriptorPath((Service)ls));
/* 549 */         XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.controlURL, namespace.getControlPath((Service)ls));
/* 550 */         XMLUtil.appendNewElementIfNotNull(descriptor, serviceElement, Descriptor.Device.ELEMENT.eventSubURL, namespace.getEventSubscriptionPath((Service)ls));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateDeviceList(Namespace namespace, Device deviceModel, Document descriptor, Element deviceElement, RemoteClientInfo info) {
/* 556 */     if (!deviceModel.hasEmbeddedDevices())
/*     */       return; 
/* 558 */     Element deviceListElement = XMLUtil.appendNewElement(descriptor, deviceElement, Descriptor.Device.ELEMENT.deviceList);
/*     */     
/* 560 */     for (Device device : deviceModel.getEmbeddedDevices()) {
/* 561 */       generateDevice(namespace, device, descriptor, deviceListElement, info);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 566 */     log.warning(e.toString());
/*     */   }
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 570 */     throw e;
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 574 */     throw e;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static URI parseURI(String uri) {
/* 580 */     if (uri.startsWith("www.")) {
/* 581 */       uri = "http://" + uri;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 586 */     if (uri.contains(" "))
/*     */     {
/*     */ 
/*     */       
/* 590 */       uri = uri.replaceAll(" ", "%20");
/*     */     }
/*     */     
/*     */     try {
/* 594 */       return URI.create(uri);
/* 595 */     } catch (Throwable ex) {
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
/* 607 */       log.fine("Illegal URI, trying with ./ prefix: " + Exceptions.unwrap(ex));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 618 */         return URI.create("./" + uri);
/* 619 */       } catch (IllegalArgumentException illegalArgumentException) {
/* 620 */         log.warning("Illegal URI '" + uri + "', ignoring value: " + Exceptions.unwrap(illegalArgumentException));
/*     */ 
/*     */         
/* 623 */         return null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\UDA10DeviceDescriptorBinderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
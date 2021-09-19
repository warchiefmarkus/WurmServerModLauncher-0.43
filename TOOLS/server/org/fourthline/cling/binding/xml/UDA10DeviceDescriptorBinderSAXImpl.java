/*     */ package org.fourthline.cling.binding.xml;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.binding.staging.MutableDevice;
/*     */ import org.fourthline.cling.binding.staging.MutableIcon;
/*     */ import org.fourthline.cling.binding.staging.MutableService;
/*     */ import org.fourthline.cling.binding.staging.MutableUDAVersion;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.types.DLNACaps;
/*     */ import org.fourthline.cling.model.types.DLNADoc;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ import org.fourthline.cling.model.types.ServiceId;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDN;
/*     */ import org.seamless.util.MimeType;
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
/*     */ public class UDA10DeviceDescriptorBinderSAXImpl
/*     */   extends UDA10DeviceDescriptorBinderImpl
/*     */ {
/*  53 */   private static Logger log = Logger.getLogger(DeviceDescriptorBinder.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public <D extends Device> D describe(D undescribedDevice, String descriptorXml) throws DescriptorBindingException, ValidationException {
/*  58 */     if (descriptorXml == null || descriptorXml.length() == 0) {
/*  59 */       throw new DescriptorBindingException("Null or empty descriptor");
/*     */     }
/*     */     
/*     */     try {
/*  63 */       log.fine("Populating device from XML descriptor: " + undescribedDevice);
/*     */ 
/*     */ 
/*     */       
/*  67 */       SAXParser parser = new SAXParser();
/*     */       
/*  69 */       MutableDevice descriptor = new MutableDevice();
/*  70 */       new RootHandler(descriptor, parser);
/*     */       
/*  72 */       parser.parse(new InputSource(new StringReader(descriptorXml
/*     */ 
/*     */               
/*  75 */               .trim())));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       return (D)descriptor.build((Device)undescribedDevice);
/*     */     }
/*  82 */     catch (ValidationException ex) {
/*  83 */       throw ex;
/*  84 */     } catch (Exception ex) {
/*  85 */       throw new DescriptorBindingException("Could not parse device descriptor: " + ex.toString(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static class RootHandler
/*     */     extends DeviceDescriptorHandler<MutableDevice> {
/*     */     public RootHandler(MutableDevice instance, SAXParser parser) {
/*  92 */       super(instance, parser);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Device.ELEMENT element, Attributes attributes) throws SAXException {
/*  98 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.SpecVersionHandler.EL)) {
/*  99 */         MutableUDAVersion udaVersion = new MutableUDAVersion();
/* 100 */         ((MutableDevice)getInstance()).udaVersion = udaVersion;
/* 101 */         new UDA10DeviceDescriptorBinderSAXImpl.SpecVersionHandler(udaVersion, this);
/*     */       } 
/*     */       
/* 104 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.DeviceHandler.EL)) {
/* 105 */         new UDA10DeviceDescriptorBinderSAXImpl.DeviceHandler((MutableDevice)getInstance(), this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Device.ELEMENT element) throws SAXException {
/* 112 */       switch (element) {
/*     */         case URLBase:
/*     */           try {
/* 115 */             String urlString = getCharacters();
/* 116 */             if (urlString != null && urlString.length() > 0)
/*     */             {
/* 118 */               ((MutableDevice)getInstance()).baseURL = new URL(urlString);
/*     */             }
/* 120 */           } catch (Exception ex) {
/* 121 */             throw new SAXException("Invalid URLBase: " + ex.toString());
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class SpecVersionHandler
/*     */     extends DeviceDescriptorHandler<MutableUDAVersion> {
/* 130 */     public static final Descriptor.Device.ELEMENT EL = Descriptor.Device.ELEMENT.specVersion;
/*     */     
/*     */     public SpecVersionHandler(MutableUDAVersion instance, UDA10DeviceDescriptorBinderSAXImpl.DeviceDescriptorHandler parent) {
/* 133 */       super(instance, parent);
/*     */     }
/*     */     public void endElement(Descriptor.Device.ELEMENT element) throws SAXException {
/*     */       String majorVersion;
/*     */       String minorVersion;
/* 138 */       switch (element) {
/*     */         case major:
/* 140 */           majorVersion = getCharacters().trim();
/* 141 */           if (!majorVersion.equals("1")) {
/* 142 */             UDA10DeviceDescriptorBinderSAXImpl.log.warning("Unsupported UDA major version, ignoring: " + majorVersion);
/* 143 */             majorVersion = "1";
/*     */           } 
/* 145 */           ((MutableUDAVersion)getInstance()).major = Integer.valueOf(majorVersion).intValue();
/*     */           break;
/*     */         case minor:
/* 148 */           minorVersion = getCharacters().trim();
/* 149 */           if (!minorVersion.equals("0")) {
/* 150 */             UDA10DeviceDescriptorBinderSAXImpl.log.warning("Unsupported UDA minor version, ignoring: " + minorVersion);
/* 151 */             minorVersion = "0";
/*     */           } 
/* 153 */           ((MutableUDAVersion)getInstance()).minor = Integer.valueOf(minorVersion).intValue();
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 160 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class DeviceHandler
/*     */     extends DeviceDescriptorHandler<MutableDevice> {
/* 166 */     public static final Descriptor.Device.ELEMENT EL = Descriptor.Device.ELEMENT.device;
/*     */     
/*     */     public DeviceHandler(MutableDevice instance, UDA10DeviceDescriptorBinderSAXImpl.DeviceDescriptorHandler parent) {
/* 169 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Device.ELEMENT element, Attributes attributes) throws SAXException {
/* 175 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.IconListHandler.EL)) {
/* 176 */         List<MutableIcon> icons = new ArrayList<>();
/* 177 */         ((MutableDevice)getInstance()).icons = icons;
/* 178 */         new UDA10DeviceDescriptorBinderSAXImpl.IconListHandler(icons, this);
/*     */       } 
/*     */       
/* 181 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.ServiceListHandler.EL)) {
/* 182 */         List<MutableService> services = new ArrayList<>();
/* 183 */         ((MutableDevice)getInstance()).services = services;
/* 184 */         new UDA10DeviceDescriptorBinderSAXImpl.ServiceListHandler(services, this);
/*     */       } 
/*     */       
/* 187 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.DeviceListHandler.EL)) {
/* 188 */         List<MutableDevice> devices = new ArrayList<>();
/* 189 */         ((MutableDevice)getInstance()).embeddedDevices = devices;
/* 190 */         new UDA10DeviceDescriptorBinderSAXImpl.DeviceListHandler(devices, this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void endElement(Descriptor.Device.ELEMENT element) throws SAXException {
/*     */       String txt;
/* 196 */       switch (element) {
/*     */         case deviceType:
/* 198 */           ((MutableDevice)getInstance()).deviceType = getCharacters();
/*     */           break;
/*     */         case friendlyName:
/* 201 */           ((MutableDevice)getInstance()).friendlyName = getCharacters();
/*     */           break;
/*     */         case manufacturer:
/* 204 */           ((MutableDevice)getInstance()).manufacturer = getCharacters();
/*     */           break;
/*     */         case manufacturerURL:
/* 207 */           ((MutableDevice)getInstance()).manufacturerURI = UDA10DeviceDescriptorBinderImpl.parseURI(getCharacters());
/*     */           break;
/*     */         case modelDescription:
/* 210 */           ((MutableDevice)getInstance()).modelDescription = getCharacters();
/*     */           break;
/*     */         case modelName:
/* 213 */           ((MutableDevice)getInstance()).modelName = getCharacters();
/*     */           break;
/*     */         case modelNumber:
/* 216 */           ((MutableDevice)getInstance()).modelNumber = getCharacters();
/*     */           break;
/*     */         case modelURL:
/* 219 */           ((MutableDevice)getInstance()).modelURI = UDA10DeviceDescriptorBinderImpl.parseURI(getCharacters());
/*     */           break;
/*     */         case presentationURL:
/* 222 */           ((MutableDevice)getInstance()).presentationURI = UDA10DeviceDescriptorBinderImpl.parseURI(getCharacters());
/*     */           break;
/*     */         case UPC:
/* 225 */           ((MutableDevice)getInstance()).upc = getCharacters();
/*     */           break;
/*     */         case serialNumber:
/* 228 */           ((MutableDevice)getInstance()).serialNumber = getCharacters();
/*     */           break;
/*     */         case UDN:
/* 231 */           ((MutableDevice)getInstance()).udn = UDN.valueOf(getCharacters());
/*     */           break;
/*     */         case X_DLNADOC:
/* 234 */           txt = getCharacters();
/*     */           try {
/* 236 */             ((MutableDevice)getInstance()).dlnaDocs.add(DLNADoc.valueOf(txt));
/* 237 */           } catch (InvalidValueException ex) {
/* 238 */             UDA10DeviceDescriptorBinderSAXImpl.log.info("Invalid X_DLNADOC value, ignoring value: " + txt);
/*     */           } 
/*     */           break;
/*     */         case X_DLNACAP:
/* 242 */           ((MutableDevice)getInstance()).dlnaCaps = DLNACaps.valueOf(getCharacters());
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 249 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class IconListHandler
/*     */     extends DeviceDescriptorHandler<List<MutableIcon>> {
/* 255 */     public static final Descriptor.Device.ELEMENT EL = Descriptor.Device.ELEMENT.iconList;
/*     */     
/*     */     public IconListHandler(List<MutableIcon> instance, UDA10DeviceDescriptorBinderSAXImpl.DeviceDescriptorHandler parent) {
/* 258 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Device.ELEMENT element, Attributes attributes) throws SAXException {
/* 263 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.IconHandler.EL)) {
/* 264 */         MutableIcon icon = new MutableIcon();
/* 265 */         ((List<MutableIcon>)getInstance()).add(icon);
/* 266 */         new UDA10DeviceDescriptorBinderSAXImpl.IconHandler(icon, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 272 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class IconHandler
/*     */     extends DeviceDescriptorHandler<MutableIcon> {
/* 278 */     public static final Descriptor.Device.ELEMENT EL = Descriptor.Device.ELEMENT.icon;
/*     */     
/*     */     public IconHandler(MutableIcon instance, UDA10DeviceDescriptorBinderSAXImpl.DeviceDescriptorHandler parent) {
/* 281 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Device.ELEMENT element) throws SAXException {
/* 286 */       switch (element) {
/*     */         case width:
/* 288 */           ((MutableIcon)getInstance()).width = Integer.valueOf(getCharacters()).intValue();
/*     */           break;
/*     */         case height:
/* 291 */           ((MutableIcon)getInstance()).height = Integer.valueOf(getCharacters()).intValue();
/*     */           break;
/*     */         case depth:
/*     */           try {
/* 295 */             ((MutableIcon)getInstance()).depth = Integer.valueOf(getCharacters()).intValue();
/* 296 */           } catch (NumberFormatException ex) {
/* 297 */             UDA10DeviceDescriptorBinderSAXImpl.log.warning("Invalid icon depth '" + getCharacters() + "', using 16 as default: " + ex);
/* 298 */             ((MutableIcon)getInstance()).depth = 16;
/*     */           } 
/*     */           break;
/*     */         case url:
/* 302 */           ((MutableIcon)getInstance()).uri = UDA10DeviceDescriptorBinderImpl.parseURI(getCharacters());
/*     */           break;
/*     */         case mimetype:
/*     */           try {
/* 306 */             ((MutableIcon)getInstance()).mimeType = getCharacters();
/* 307 */             MimeType.valueOf(((MutableIcon)getInstance()).mimeType);
/* 308 */           } catch (IllegalArgumentException ex) {
/* 309 */             UDA10DeviceDescriptorBinderSAXImpl.log.warning("Ignoring invalid icon mime type: " + ((MutableIcon)getInstance()).mimeType);
/* 310 */             ((MutableIcon)getInstance()).mimeType = "";
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 318 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ServiceListHandler
/*     */     extends DeviceDescriptorHandler<List<MutableService>> {
/* 324 */     public static final Descriptor.Device.ELEMENT EL = Descriptor.Device.ELEMENT.serviceList;
/*     */     
/*     */     public ServiceListHandler(List<MutableService> instance, UDA10DeviceDescriptorBinderSAXImpl.DeviceDescriptorHandler parent) {
/* 327 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Device.ELEMENT element, Attributes attributes) throws SAXException {
/* 332 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.ServiceHandler.EL)) {
/* 333 */         MutableService service = new MutableService();
/* 334 */         ((List<MutableService>)getInstance()).add(service);
/* 335 */         new UDA10DeviceDescriptorBinderSAXImpl.ServiceHandler(service, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 341 */       boolean last = element.equals(EL);
/* 342 */       if (last) {
/* 343 */         Iterator<MutableService> it = ((List<MutableService>)getInstance()).iterator();
/* 344 */         while (it.hasNext()) {
/* 345 */           MutableService service = it.next();
/* 346 */           if (service.serviceType == null || service.serviceId == null)
/* 347 */             it.remove(); 
/*     */         } 
/*     */       } 
/* 350 */       return last;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ServiceHandler
/*     */     extends DeviceDescriptorHandler<MutableService> {
/* 356 */     public static final Descriptor.Device.ELEMENT EL = Descriptor.Device.ELEMENT.service;
/*     */     
/*     */     public ServiceHandler(MutableService instance, UDA10DeviceDescriptorBinderSAXImpl.DeviceDescriptorHandler parent) {
/* 359 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Device.ELEMENT element) throws SAXException {
/*     */       try {
/* 365 */         switch (element) {
/*     */           case serviceType:
/* 367 */             ((MutableService)getInstance()).serviceType = ServiceType.valueOf(getCharacters());
/*     */             break;
/*     */           case serviceId:
/* 370 */             ((MutableService)getInstance()).serviceId = ServiceId.valueOf(getCharacters());
/*     */             break;
/*     */           case SCPDURL:
/* 373 */             ((MutableService)getInstance()).descriptorURI = UDA10DeviceDescriptorBinderImpl.parseURI(getCharacters());
/*     */             break;
/*     */           case controlURL:
/* 376 */             ((MutableService)getInstance()).controlURI = UDA10DeviceDescriptorBinderImpl.parseURI(getCharacters());
/*     */             break;
/*     */           case eventSubURL:
/* 379 */             ((MutableService)getInstance()).eventSubscriptionURI = UDA10DeviceDescriptorBinderImpl.parseURI(getCharacters());
/*     */             break;
/*     */         } 
/* 382 */       } catch (InvalidValueException ex) {
/* 383 */         UDA10DeviceDescriptorBinderSAXImpl.log.warning("UPnP specification violation, skipping invalid service declaration. " + ex
/* 384 */             .getMessage());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 391 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class DeviceListHandler
/*     */     extends DeviceDescriptorHandler<List<MutableDevice>> {
/* 397 */     public static final Descriptor.Device.ELEMENT EL = Descriptor.Device.ELEMENT.deviceList;
/*     */     
/*     */     public DeviceListHandler(List<MutableDevice> instance, UDA10DeviceDescriptorBinderSAXImpl.DeviceDescriptorHandler parent) {
/* 400 */       super(instance, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Device.ELEMENT element, Attributes attributes) throws SAXException {
/* 405 */       if (element.equals(UDA10DeviceDescriptorBinderSAXImpl.DeviceHandler.EL)) {
/* 406 */         MutableDevice device = new MutableDevice();
/* 407 */         ((List<MutableDevice>)getInstance()).add(device);
/* 408 */         new UDA10DeviceDescriptorBinderSAXImpl.DeviceHandler(device, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 414 */       return element.equals(EL);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class DeviceDescriptorHandler<I>
/*     */     extends SAXParser.Handler<I> {
/*     */     public DeviceDescriptorHandler(I instance) {
/* 421 */       super(instance);
/*     */     }
/*     */     
/*     */     public DeviceDescriptorHandler(I instance, SAXParser parser) {
/* 425 */       super(instance, parser);
/*     */     }
/*     */     
/*     */     public DeviceDescriptorHandler(I instance, DeviceDescriptorHandler parent) {
/* 429 */       super(instance, parent);
/*     */     }
/*     */     
/*     */     public DeviceDescriptorHandler(I instance, SAXParser parser, DeviceDescriptorHandler parent) {
/* 433 */       super(instance, parser, parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 438 */       super.startElement(uri, localName, qName, attributes);
/* 439 */       Descriptor.Device.ELEMENT el = Descriptor.Device.ELEMENT.valueOrNullOf(localName);
/* 440 */       if (el == null)
/* 441 */         return;  startElement(el, attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     public void endElement(String uri, String localName, String qName) throws SAXException {
/* 446 */       super.endElement(uri, localName, qName);
/* 447 */       Descriptor.Device.ELEMENT el = Descriptor.Device.ELEMENT.valueOrNullOf(localName);
/* 448 */       if (el == null)
/* 449 */         return;  endElement(el);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isLastElement(String uri, String localName, String qName) {
/* 454 */       Descriptor.Device.ELEMENT el = Descriptor.Device.ELEMENT.valueOrNullOf(localName);
/* 455 */       return (el != null && isLastElement(el));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(Descriptor.Device.ELEMENT element, Attributes attributes) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void endElement(Descriptor.Device.ELEMENT element) throws SAXException {}
/*     */ 
/*     */     
/*     */     public boolean isLastElement(Descriptor.Device.ELEMENT element) {
/* 467 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\UDA10DeviceDescriptorBinderSAXImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
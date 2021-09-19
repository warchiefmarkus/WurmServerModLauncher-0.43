/*     */ package org.fourthline.cling.binding.staging;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.DeviceDetails;
/*     */ import org.fourthline.cling.model.meta.Icon;
/*     */ import org.fourthline.cling.model.meta.ManufacturerDetails;
/*     */ import org.fourthline.cling.model.meta.ModelDetails;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.meta.UDAVersion;
/*     */ import org.fourthline.cling.model.types.DLNACaps;
/*     */ import org.fourthline.cling.model.types.DLNADoc;
/*     */ import org.fourthline.cling.model.types.DeviceType;
/*     */ import org.fourthline.cling.model.types.UDN;
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
/*     */ public class MutableDevice
/*     */ {
/*     */   public UDN udn;
/*  42 */   public MutableUDAVersion udaVersion = new MutableUDAVersion();
/*     */   public URL baseURL;
/*     */   public String deviceType;
/*     */   public String friendlyName;
/*     */   public String manufacturer;
/*     */   public URI manufacturerURI;
/*     */   public String modelName;
/*     */   public String modelDescription;
/*     */   public String modelNumber;
/*     */   public URI modelURI;
/*     */   public String serialNumber;
/*     */   public String upc;
/*     */   public URI presentationURI;
/*  55 */   public List<DLNADoc> dlnaDocs = new ArrayList<>();
/*     */   public DLNACaps dlnaCaps;
/*  57 */   public List<MutableIcon> icons = new ArrayList<>();
/*  58 */   public List<MutableService> services = new ArrayList<>();
/*  59 */   public List<MutableDevice> embeddedDevices = new ArrayList<>();
/*     */   
/*     */   public MutableDevice parentDevice;
/*     */   
/*     */   public Device build(Device prototype) throws ValidationException {
/*  64 */     return build(prototype, createDeviceVersion(), this.baseURL);
/*     */   }
/*     */ 
/*     */   
/*     */   public Device build(Device prototype, UDAVersion deviceVersion, URL baseURL) throws ValidationException {
/*  69 */     List<Device> embeddedDevicesList = new ArrayList<>();
/*  70 */     for (MutableDevice embeddedDevice : this.embeddedDevices) {
/*  71 */       embeddedDevicesList.add(embeddedDevice.build(prototype, deviceVersion, baseURL));
/*     */     }
/*  73 */     return prototype.newInstance(this.udn, deviceVersion, 
/*     */ 
/*     */         
/*  76 */         createDeviceType(), 
/*  77 */         createDeviceDetails(baseURL), 
/*  78 */         createIcons(), 
/*  79 */         createServices(prototype), embeddedDevicesList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UDAVersion createDeviceVersion() {
/*  85 */     return new UDAVersion(this.udaVersion.major, this.udaVersion.minor);
/*     */   }
/*     */   
/*     */   public DeviceType createDeviceType() {
/*  89 */     return DeviceType.valueOf(this.deviceType);
/*     */   }
/*     */   
/*     */   public DeviceDetails createDeviceDetails(URL baseURL) {
/*  93 */     return new DeviceDetails(baseURL, this.friendlyName, new ManufacturerDetails(this.manufacturer, this.manufacturerURI), new ModelDetails(this.modelName, this.modelDescription, this.modelNumber, this.modelURI), this.serialNumber, this.upc, this.presentationURI, this.dlnaDocs
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  98 */         .<DLNADoc>toArray(new DLNADoc[this.dlnaDocs.size()]), this.dlnaCaps);
/*     */   }
/*     */ 
/*     */   
/*     */   public Icon[] createIcons() {
/* 103 */     Icon[] iconArray = new Icon[this.icons.size()];
/* 104 */     int i = 0;
/* 105 */     for (MutableIcon icon : this.icons) {
/* 106 */       iconArray[i++] = icon.build();
/*     */     }
/* 108 */     return iconArray;
/*     */   }
/*     */   
/*     */   public Service[] createServices(Device prototype) throws ValidationException {
/* 112 */     Service[] services = prototype.newServiceArray(this.services.size());
/* 113 */     int i = 0;
/* 114 */     for (MutableService service : this.services) {
/* 115 */       services[i++] = service.build(prototype);
/*     */     }
/* 117 */     return services;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\staging\MutableDevice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
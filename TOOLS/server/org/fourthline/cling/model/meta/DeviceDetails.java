/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.Validatable;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.types.DLNACaps;
/*     */ import org.fourthline.cling.model.types.DLNADoc;
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
/*     */ public class DeviceDetails
/*     */   implements Validatable
/*     */ {
/*  36 */   private static final Logger log = Logger.getLogger(DeviceDetails.class.getName());
/*     */   
/*     */   private final URL baseURL;
/*     */   private final String friendlyName;
/*     */   private final ManufacturerDetails manufacturerDetails;
/*     */   private final ModelDetails modelDetails;
/*     */   private final String serialNumber;
/*     */   private final String upc;
/*     */   private final URI presentationURI;
/*     */   private final DLNADoc[] dlnaDocs;
/*     */   private final DLNACaps dlnaCaps;
/*     */   private final DLNACaps secProductCaps;
/*     */   
/*     */   public DeviceDetails(String friendlyName) {
/*  50 */     this((URL)null, friendlyName, (ManufacturerDetails)null, (ModelDetails)null, (String)null, (String)null, (URI)null);
/*     */   }
/*     */   
/*     */   public DeviceDetails(String friendlyName, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/*  54 */     this(null, friendlyName, null, null, null, null, null, dlnaDocs, dlnaCaps);
/*     */   }
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails) {
/*  58 */     this((URL)null, friendlyName, manufacturerDetails, (ModelDetails)null, (String)null, (String)null, (URI)null);
/*     */   }
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/*  62 */     this(null, friendlyName, manufacturerDetails, null, null, null, null, dlnaDocs, dlnaCaps);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails) {
/*  67 */     this((URL)null, friendlyName, manufacturerDetails, modelDetails, (String)null, (String)null, (URI)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/*  72 */     this(null, friendlyName, manufacturerDetails, modelDetails, null, null, null, dlnaDocs, dlnaCaps);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps, DLNACaps secProductCaps) {
/*  77 */     this(null, friendlyName, manufacturerDetails, modelDetails, null, null, null, dlnaDocs, dlnaCaps, secProductCaps);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc) {
/*  82 */     this((URL)null, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, (URI)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/*  87 */     this(null, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, null, dlnaDocs, dlnaCaps);
/*     */   }
/*     */   
/*     */   public DeviceDetails(String friendlyName, URI presentationURI) {
/*  91 */     this((URL)null, friendlyName, (ManufacturerDetails)null, (ModelDetails)null, (String)null, (String)null, presentationURI);
/*     */   }
/*     */   
/*     */   public DeviceDetails(String friendlyName, URI presentationURI, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/*  95 */     this(null, friendlyName, null, null, null, null, presentationURI, dlnaDocs, dlnaCaps);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, URI presentationURI) {
/* 100 */     this((URL)null, friendlyName, manufacturerDetails, modelDetails, (String)null, (String)null, presentationURI);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, URI presentationURI, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/* 105 */     this(null, friendlyName, manufacturerDetails, modelDetails, null, null, presentationURI, dlnaDocs, dlnaCaps);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, URI presentationURI) {
/* 110 */     this((URL)null, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, presentationURI);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, URI presentationURI, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/* 115 */     this(null, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, presentationURI, dlnaDocs, dlnaCaps);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, String presentationURI) throws IllegalArgumentException {
/* 121 */     this((URL)null, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, URI.create(presentationURI));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DeviceDetails(String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, String presentationURI, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) throws IllegalArgumentException {
/* 127 */     this(null, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, URI.create(presentationURI), dlnaDocs, dlnaCaps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeviceDetails(URL baseURL, String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, URI presentationURI) {
/* 134 */     this(baseURL, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, presentationURI, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeviceDetails(URL baseURL, String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, URI presentationURI, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps) {
/* 141 */     this(baseURL, friendlyName, manufacturerDetails, modelDetails, serialNumber, upc, presentationURI, dlnaDocs, dlnaCaps, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeviceDetails(URL baseURL, String friendlyName, ManufacturerDetails manufacturerDetails, ModelDetails modelDetails, String serialNumber, String upc, URI presentationURI, DLNADoc[] dlnaDocs, DLNACaps dlnaCaps, DLNACaps secProductCaps) {
/* 148 */     this.baseURL = baseURL;
/* 149 */     this.friendlyName = friendlyName;
/* 150 */     this.manufacturerDetails = (manufacturerDetails == null) ? new ManufacturerDetails() : manufacturerDetails;
/* 151 */     this.modelDetails = (modelDetails == null) ? new ModelDetails() : modelDetails;
/* 152 */     this.serialNumber = serialNumber;
/* 153 */     this.upc = upc;
/* 154 */     this.presentationURI = presentationURI;
/* 155 */     this.dlnaDocs = (dlnaDocs != null) ? dlnaDocs : new DLNADoc[0];
/* 156 */     this.dlnaCaps = dlnaCaps;
/* 157 */     this.secProductCaps = secProductCaps;
/*     */   }
/*     */   
/*     */   public URL getBaseURL() {
/* 161 */     return this.baseURL;
/*     */   }
/*     */   
/*     */   public String getFriendlyName() {
/* 165 */     return this.friendlyName;
/*     */   }
/*     */   
/*     */   public ManufacturerDetails getManufacturerDetails() {
/* 169 */     return this.manufacturerDetails;
/*     */   }
/*     */   
/*     */   public ModelDetails getModelDetails() {
/* 173 */     return this.modelDetails;
/*     */   }
/*     */   
/*     */   public String getSerialNumber() {
/* 177 */     return this.serialNumber;
/*     */   }
/*     */   
/*     */   public String getUpc() {
/* 181 */     return this.upc;
/*     */   }
/*     */   
/*     */   public URI getPresentationURI() {
/* 185 */     return this.presentationURI;
/*     */   }
/*     */   
/*     */   public DLNADoc[] getDlnaDocs() {
/* 189 */     return this.dlnaDocs;
/*     */   }
/*     */   
/*     */   public DLNACaps getDlnaCaps() {
/* 193 */     return this.dlnaCaps;
/*     */   }
/*     */   
/*     */   public DLNACaps getSecProductCaps() {
/* 197 */     return this.secProductCaps;
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/* 201 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/* 203 */     if (getUpc() != null)
/*     */     {
/* 205 */       if (getUpc().length() != 12) {
/* 206 */         log.fine("UPnP specification violation, UPC must be 12 digits: " + getUpc());
/*     */       } else {
/*     */         try {
/* 209 */           Long.parseLong(getUpc());
/* 210 */         } catch (NumberFormatException ex) {
/* 211 */           log.fine("UPnP specification violation, UPC must be 12 digits all-numeric: " + getUpc());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 216 */     return errors;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\DeviceDetails.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
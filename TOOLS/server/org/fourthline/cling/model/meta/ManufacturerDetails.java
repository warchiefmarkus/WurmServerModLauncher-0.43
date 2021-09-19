/*    */ package org.fourthline.cling.model.meta;
/*    */ 
/*    */ import java.net.URI;
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
/*    */ public class ManufacturerDetails
/*    */ {
/*    */   private String manufacturer;
/*    */   private URI manufacturerURI;
/*    */   
/*    */   ManufacturerDetails() {}
/*    */   
/*    */   public ManufacturerDetails(String manufacturer) {
/* 34 */     this.manufacturer = manufacturer;
/*    */   }
/*    */   
/*    */   public ManufacturerDetails(URI manufacturerURI) {
/* 38 */     this.manufacturerURI = manufacturerURI;
/*    */   }
/*    */   
/*    */   public ManufacturerDetails(String manufacturer, URI manufacturerURI) {
/* 42 */     this.manufacturer = manufacturer;
/* 43 */     this.manufacturerURI = manufacturerURI;
/*    */   }
/*    */   
/*    */   public ManufacturerDetails(String manufacturer, String manufacturerURI) throws IllegalArgumentException {
/* 47 */     this.manufacturer = manufacturer;
/* 48 */     this.manufacturerURI = URI.create(manufacturerURI);
/*    */   }
/*    */   
/*    */   public String getManufacturer() {
/* 52 */     return this.manufacturer;
/*    */   }
/*    */   
/*    */   public URI getManufacturerURI() {
/* 56 */     return this.manufacturerURI;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\ManufacturerDetails.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
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
/*    */ public class ModelDetails
/*    */ {
/*    */   private String modelName;
/*    */   private String modelDescription;
/*    */   private String modelNumber;
/*    */   private URI modelURI;
/*    */   
/*    */   ModelDetails() {}
/*    */   
/*    */   public ModelDetails(String modelName) {
/* 36 */     this.modelName = modelName;
/*    */   }
/*    */   
/*    */   public ModelDetails(String modelName, String modelDescription) {
/* 40 */     this.modelName = modelName;
/* 41 */     this.modelDescription = modelDescription;
/*    */   }
/*    */   
/*    */   public ModelDetails(String modelName, String modelDescription, String modelNumber) {
/* 45 */     this.modelName = modelName;
/* 46 */     this.modelDescription = modelDescription;
/* 47 */     this.modelNumber = modelNumber;
/*    */   }
/*    */   
/*    */   public ModelDetails(String modelName, String modelDescription, String modelNumber, URI modelURI) {
/* 51 */     this.modelName = modelName;
/* 52 */     this.modelDescription = modelDescription;
/* 53 */     this.modelNumber = modelNumber;
/* 54 */     this.modelURI = modelURI;
/*    */   }
/*    */   
/*    */   public ModelDetails(String modelName, String modelDescription, String modelNumber, String modelURI) throws IllegalArgumentException {
/* 58 */     this.modelName = modelName;
/* 59 */     this.modelDescription = modelDescription;
/* 60 */     this.modelNumber = modelNumber;
/* 61 */     this.modelURI = URI.create(modelURI);
/*    */   }
/*    */   
/*    */   public String getModelName() {
/* 65 */     return this.modelName;
/*    */   }
/*    */   
/*    */   public String getModelDescription() {
/* 69 */     return this.modelDescription;
/*    */   }
/*    */   
/*    */   public String getModelNumber() {
/* 73 */     return this.modelNumber;
/*    */   }
/*    */   
/*    */   public URI getModelURI() {
/* 77 */     return this.modelURI;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\ModelDetails.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
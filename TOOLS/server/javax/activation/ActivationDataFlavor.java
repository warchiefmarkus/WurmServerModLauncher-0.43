/*     */ package javax.activation;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActivationDataFlavor
/*     */   extends DataFlavor
/*     */ {
/*  70 */   private String mimeType = null;
/*  71 */   private MimeType mimeObject = null;
/*  72 */   private String humanPresentableName = null;
/*  73 */   private Class representationClass = null;
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
/*     */   public ActivationDataFlavor(Class representationClass, String mimeType, String humanPresentableName) {
/*  94 */     super(mimeType, humanPresentableName);
/*     */ 
/*     */     
/*  97 */     this.mimeType = mimeType;
/*  98 */     this.humanPresentableName = humanPresentableName;
/*  99 */     this.representationClass = representationClass;
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
/*     */   public ActivationDataFlavor(Class representationClass, String humanPresentableName) {
/* 121 */     super(representationClass, humanPresentableName);
/* 122 */     this.mimeType = super.getMimeType();
/* 123 */     this.representationClass = representationClass;
/* 124 */     this.humanPresentableName = humanPresentableName;
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
/*     */   public ActivationDataFlavor(String mimeType, String humanPresentableName) {
/* 143 */     super(mimeType, humanPresentableName);
/* 144 */     this.mimeType = mimeType;
/*     */     try {
/* 146 */       this.representationClass = Class.forName("java.io.InputStream");
/* 147 */     } catch (ClassNotFoundException ex) {}
/*     */ 
/*     */     
/* 150 */     this.humanPresentableName = humanPresentableName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMimeType() {
/* 159 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getRepresentationClass() {
/* 168 */     return this.representationClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHumanPresentableName() {
/* 177 */     return this.humanPresentableName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHumanPresentableName(String humanPresentableName) {
/* 186 */     this.humanPresentableName = humanPresentableName;
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
/*     */   public boolean equals(DataFlavor dataFlavor) {
/* 198 */     return (isMimeTypeEqual(dataFlavor) && dataFlavor.getRepresentationClass() == this.representationClass);
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
/*     */   public boolean isMimeTypeEqual(String mimeType) {
/* 215 */     MimeType mt = null;
/*     */     try {
/* 217 */       if (this.mimeObject == null)
/* 218 */         this.mimeObject = new MimeType(this.mimeType); 
/* 219 */       mt = new MimeType(mimeType);
/* 220 */     } catch (MimeTypeParseException e) {
/*     */       
/* 222 */       return this.mimeType.equalsIgnoreCase(mimeType);
/*     */     } 
/*     */     
/* 225 */     return this.mimeObject.match(mt);
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
/*     */   protected String normalizeMimeTypeParameter(String parameterName, String parameterValue) {
/* 245 */     return parameterValue;
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
/*     */   protected String normalizeMimeType(String mimeType) {
/* 261 */     return mimeType;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\ActivationDataFlavor.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */
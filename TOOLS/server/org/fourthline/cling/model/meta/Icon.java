/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.Validatable;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.types.BinHexDatatype;
/*     */ import org.seamless.util.MimeType;
/*     */ import org.seamless.util.URIUtil;
/*     */ import org.seamless.util.io.IO;
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
/*     */ public class Icon
/*     */   implements Validatable
/*     */ {
/*  50 */   private static final Logger log = Logger.getLogger(StateVariable.class.getName());
/*     */   
/*     */   private final MimeType mimeType;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int depth;
/*     */   
/*     */   private final URI uri;
/*     */   
/*     */   private final byte[] data;
/*     */   private Device device;
/*     */   
/*     */   public Icon(String mimeType, int width, int height, int depth, URI uri) {
/*  66 */     this((mimeType != null && mimeType.length() > 0) ? MimeType.valueOf(mimeType) : null, width, height, depth, uri, (byte[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon(String mimeType, int width, int height, int depth, URL url) throws IOException {
/*  76 */     this(mimeType, width, height, depth, new File(URIUtil.toURI(url)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon(String mimeType, int width, int height, int depth, File file) throws IOException {
/*  84 */     this(mimeType, width, height, depth, file.getName(), IO.readBytes(file));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon(String mimeType, int width, int height, int depth, String uniqueName, InputStream is) throws IOException {
/*  93 */     this(mimeType, width, height, depth, uniqueName, IO.readBytes(is));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon(String mimeType, int width, int height, int depth, String uniqueName, byte[] data) {
/* 102 */     this((mimeType != null && mimeType.length() > 0) ? MimeType.valueOf(mimeType) : null, width, height, depth, URI.create(uniqueName), data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon(String mimeType, int width, int height, int depth, String uniqueName, String binHexEncoded) {
/* 112 */     this(mimeType, width, height, depth, uniqueName, (binHexEncoded != null && 
/*     */         
/* 114 */         !binHexEncoded.equals("")) ? (new BinHexDatatype()).valueOf(binHexEncoded) : null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Icon(MimeType mimeType, int width, int height, int depth, URI uri, byte[] data) {
/* 119 */     this.mimeType = mimeType;
/* 120 */     this.width = width;
/* 121 */     this.height = height;
/* 122 */     this.depth = depth;
/* 123 */     this.uri = uri;
/* 124 */     this.data = data;
/*     */   }
/*     */   
/*     */   public MimeType getMimeType() {
/* 128 */     return this.mimeType;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 132 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 136 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getDepth() {
/* 140 */     return this.depth;
/*     */   }
/*     */   
/*     */   public URI getUri() {
/* 144 */     return this.uri;
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/* 148 */     return this.data;
/*     */   }
/*     */   
/*     */   public Device getDevice() {
/* 152 */     return this.device;
/*     */   }
/*     */   
/*     */   void setDevice(Device device) {
/* 156 */     if (this.device != null)
/* 157 */       throw new IllegalStateException("Final value has been set already, model is immutable"); 
/* 158 */     this.device = device;
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/* 162 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/* 164 */     if (getMimeType() == null) {
/* 165 */       log.warning("UPnP specification violation of: " + getDevice());
/* 166 */       log.warning("Invalid icon, missing mime type: " + this);
/*     */     } 
/* 168 */     if (getWidth() == 0) {
/* 169 */       log.warning("UPnP specification violation of: " + getDevice());
/* 170 */       log.warning("Invalid icon, missing width: " + this);
/*     */     } 
/* 172 */     if (getHeight() == 0) {
/* 173 */       log.warning("UPnP specification violation of: " + getDevice());
/* 174 */       log.warning("Invalid icon, missing height: " + this);
/*     */     } 
/* 176 */     if (getDepth() == 0) {
/* 177 */       log.warning("UPnP specification violation of: " + getDevice());
/* 178 */       log.warning("Invalid icon, missing bitmap depth: " + this);
/*     */     } 
/*     */     
/* 181 */     if (getUri() == null) {
/* 182 */       errors.add(new ValidationError(
/* 183 */             getClass(), "uri", "URL is required"));
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 189 */         URL testURI = getUri().toURL();
/* 190 */         if (testURI == null)
/* 191 */           throw new MalformedURLException(); 
/* 192 */       } catch (MalformedURLException ex) {
/* 193 */         errors.add(new ValidationError(
/* 194 */               getClass(), "uri", "URL must be valid: " + ex
/*     */               
/* 196 */               .getMessage()));
/*     */       }
/* 198 */       catch (IllegalArgumentException illegalArgumentException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 203 */     return errors;
/*     */   }
/*     */   
/*     */   public Icon deepCopy() {
/* 207 */     return new Icon(
/* 208 */         getMimeType(), 
/* 209 */         getWidth(), 
/* 210 */         getHeight(), 
/* 211 */         getDepth(), 
/* 212 */         getUri(), 
/* 213 */         getData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 219 */     return "Icon(" + getWidth() + "x" + getHeight() + ", MIME: " + getMimeType() + ") " + getUri();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\Icon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
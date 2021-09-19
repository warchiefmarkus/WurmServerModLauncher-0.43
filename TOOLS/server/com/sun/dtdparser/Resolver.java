/*     */ package com.sun.dtdparser;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Hashtable;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class Resolver
/*     */   implements EntityResolver
/*     */ {
/*     */   private boolean ignoringMIME;
/*     */   private Hashtable id2uri;
/*     */   private Hashtable id2resource;
/*     */   private Hashtable id2loader;
/*  92 */   private static final String[] types = new String[] { "application/xml", "text/xml", "text/plain", "text/html", "application/x-netcdf", "content/unknown" };
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
/*     */   public static InputSource createInputSource(String contentType, InputStream stream, boolean checkType, String scheme) throws IOException {
/* 143 */     String charset = null;
/*     */     
/* 145 */     if (contentType != null) {
/*     */ 
/*     */       
/* 148 */       contentType = contentType.toLowerCase();
/* 149 */       int index = contentType.indexOf(';');
/* 150 */       if (index != -1) {
/*     */ 
/*     */         
/* 153 */         String attributes = contentType.substring(index + 1);
/* 154 */         contentType = contentType.substring(0, index);
/*     */ 
/*     */         
/* 157 */         index = attributes.indexOf("charset");
/* 158 */         if (index != -1) {
/* 159 */           attributes = attributes.substring(index + 7);
/*     */           
/* 161 */           if ((index = attributes.indexOf(';')) != -1) {
/* 162 */             attributes = attributes.substring(0, index);
/*     */           }
/* 164 */           if ((index = attributes.indexOf('=')) != -1) {
/* 165 */             attributes = attributes.substring(index + 1);
/*     */             
/* 167 */             if ((index = attributes.indexOf('(')) != -1) {
/* 168 */               attributes = attributes.substring(0, index);
/*     */             }
/* 170 */             if ((index = attributes.indexOf('"')) != -1) {
/* 171 */               attributes = attributes.substring(index + 1);
/* 172 */               attributes = attributes.substring(0, attributes.indexOf('"'));
/*     */             } 
/*     */             
/* 175 */             charset = attributes.trim();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 184 */       if (checkType) {
/* 185 */         boolean isOK = false;
/* 186 */         for (int i = 0; i < types.length; i++) {
/* 187 */           if (types[i].equals(contentType)) {
/* 188 */             isOK = true; break;
/*     */           } 
/*     */         } 
/* 191 */         if (!isOK) {
/* 192 */           throw new IOException("Not XML: " + contentType);
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 201 */       if (charset == null) {
/* 202 */         contentType = contentType.trim();
/* 203 */         if (contentType.startsWith("text/") && 
/* 204 */           !"file".equalsIgnoreCase(scheme)) {
/* 205 */           charset = "US-ASCII";
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 211 */     InputSource retval = new InputSource(XmlReader.createReader(stream, charset));
/* 212 */     retval.setByteStream(stream);
/* 213 */     retval.setEncoding(charset);
/* 214 */     return retval;
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
/*     */   public static InputSource createInputSource(URL uri, boolean checkType) throws IOException {
/*     */     InputSource retval;
/* 228 */     URLConnection conn = uri.openConnection();
/*     */ 
/*     */     
/* 231 */     if (checkType) {
/* 232 */       String contentType = conn.getContentType();
/* 233 */       retval = createInputSource(contentType, conn.getInputStream(), false, uri.getProtocol());
/*     */     } else {
/*     */       
/* 236 */       retval = new InputSource(XmlReader.createReader(conn.getInputStream()));
/*     */     } 
/* 238 */     retval.setSystemId(conn.getURL().toString());
/* 239 */     return retval;
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
/*     */   public static InputSource createInputSource(File file) throws IOException {
/* 252 */     InputSource retval = new InputSource(XmlReader.createReader(new FileInputStream(file)));
/*     */ 
/*     */ 
/*     */     
/* 256 */     String path = file.getAbsolutePath();
/* 257 */     if (File.separatorChar != '/')
/* 258 */       path = path.replace(File.separatorChar, '/'); 
/* 259 */     if (!path.startsWith("/"))
/* 260 */       path = "/" + path; 
/* 261 */     if (!path.endsWith("/") && file.isDirectory()) {
/* 262 */       path = path + "/";
/*     */     }
/* 264 */     retval.setSystemId("file:" + path);
/* 265 */     return retval;
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
/*     */   public InputSource resolveEntity(String name, String uri) throws IOException {
/*     */     InputSource retval;
/* 290 */     String mappedURI = name2uri(name);
/*     */     
/*     */     InputStream stream;
/*     */     
/* 294 */     if (mappedURI == null && (stream = mapResource(name)) != null) {
/* 295 */       uri = "java:resource:" + (String)this.id2resource.get(name);
/* 296 */       retval = new InputSource(XmlReader.createReader(stream));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 303 */       if (mappedURI != null) {
/* 304 */         uri = mappedURI;
/* 305 */       } else if (uri == null) {
/* 306 */         return null;
/*     */       } 
/* 308 */       URL url = new URL(uri);
/* 309 */       URLConnection conn = url.openConnection();
/* 310 */       uri = conn.getURL().toString();
/*     */       
/* 312 */       if (this.ignoringMIME) {
/* 313 */         retval = new InputSource(XmlReader.createReader(conn.getInputStream()));
/*     */       } else {
/* 315 */         String contentType = conn.getContentType();
/* 316 */         retval = createInputSource(contentType, conn.getInputStream(), false, url.getProtocol());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 321 */     retval.setSystemId(uri);
/* 322 */     retval.setPublicId(name);
/* 323 */     return retval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoringMIME() {
/* 333 */     return this.ignoringMIME;
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
/*     */   public void setIgnoringMIME(boolean value) {
/* 347 */     this.ignoringMIME = value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String name2uri(String publicId) {
/* 353 */     if (publicId == null || this.id2uri == null)
/* 354 */       return null; 
/* 355 */     return (String)this.id2uri.get(publicId);
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
/*     */   public void registerCatalogEntry(String publicId, String uri) {
/* 371 */     if (this.id2uri == null)
/* 372 */       this.id2uri = new Hashtable(17); 
/* 373 */     this.id2uri.put(publicId, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputStream mapResource(String publicId) {
/* 380 */     if (publicId == null || this.id2resource == null) {
/* 381 */       return null;
/*     */     }
/* 383 */     String resourceName = (String)this.id2resource.get(publicId);
/* 384 */     ClassLoader loader = null;
/*     */     
/* 386 */     if (resourceName == null) {
/* 387 */       return null;
/*     */     }
/*     */     
/* 390 */     if (this.id2loader != null) {
/* 391 */       loader = (ClassLoader)this.id2loader.get(publicId);
/*     */     }
/* 393 */     if (loader == null)
/* 394 */       return ClassLoader.getSystemResourceAsStream(resourceName); 
/* 395 */     return loader.getResourceAsStream(resourceName);
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
/*     */   public void registerCatalogEntry(String publicId, String resourceName, ClassLoader loader) {
/* 417 */     if (this.id2resource == null)
/* 418 */       this.id2resource = new Hashtable(17); 
/* 419 */     this.id2resource.put(publicId, resourceName);
/*     */     
/* 421 */     if (loader != null) {
/* 422 */       if (this.id2loader == null)
/* 423 */         this.id2loader = new Hashtable(17); 
/* 424 */       this.id2loader.put(publicId, loader);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\dtdparser\Resolver.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */
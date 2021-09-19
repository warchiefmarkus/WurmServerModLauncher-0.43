/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import java.net.URL;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface LocatorEx
/*     */   extends Locator
/*     */ {
/*     */   ValidationEventLocator getLocation();
/*     */   
/*     */   public static final class Snapshot
/*     */     implements LocatorEx, ValidationEventLocator
/*     */   {
/*     */     private final int columnNumber;
/*     */     private final int lineNumber;
/*     */     private final int offset;
/*     */     private final String systemId;
/*     */     private final String publicId;
/*     */     private final URL url;
/*     */     private final Object object;
/*     */     private final Node node;
/*     */     
/*     */     public Snapshot(LocatorEx loc) {
/*  68 */       this.columnNumber = loc.getColumnNumber();
/*  69 */       this.lineNumber = loc.getLineNumber();
/*  70 */       this.systemId = loc.getSystemId();
/*  71 */       this.publicId = loc.getPublicId();
/*     */       
/*  73 */       ValidationEventLocator vel = loc.getLocation();
/*  74 */       this.offset = vel.getOffset();
/*  75 */       this.url = vel.getURL();
/*  76 */       this.object = vel.getObject();
/*  77 */       this.node = vel.getNode();
/*     */     }
/*     */     
/*     */     public Object getObject() {
/*  81 */       return this.object;
/*     */     }
/*     */     
/*     */     public Node getNode() {
/*  85 */       return this.node;
/*     */     }
/*     */     
/*     */     public int getOffset() {
/*  89 */       return this.offset;
/*     */     }
/*     */     
/*     */     public URL getURL() {
/*  93 */       return this.url;
/*     */     }
/*     */     
/*     */     public int getColumnNumber() {
/*  97 */       return this.columnNumber;
/*     */     }
/*     */     
/*     */     public int getLineNumber() {
/* 101 */       return this.lineNumber;
/*     */     }
/*     */     
/*     */     public String getSystemId() {
/* 105 */       return this.systemId;
/*     */     }
/*     */     
/*     */     public String getPublicId() {
/* 109 */       return this.publicId;
/*     */     }
/*     */     
/*     */     public ValidationEventLocator getLocation() {
/* 113 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\LocatorEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
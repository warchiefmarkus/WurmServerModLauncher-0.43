/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DescMeta<M>
/*     */ {
/*     */   protected String id;
/*     */   protected String type;
/*     */   protected URI nameSpace;
/*     */   protected M metadata;
/*     */   
/*     */   public DescMeta() {}
/*     */   
/*     */   public DescMeta(String id, String type, URI nameSpace, M metadata) {
/*  56 */     this.id = id;
/*  57 */     this.type = type;
/*  58 */     this.nameSpace = nameSpace;
/*  59 */     this.metadata = metadata;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  63 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  67 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  71 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  75 */     this.type = type;
/*     */   }
/*     */   
/*     */   public URI getNameSpace() {
/*  79 */     return this.nameSpace;
/*     */   }
/*     */   
/*     */   public void setNameSpace(URI nameSpace) {
/*  83 */     this.nameSpace = nameSpace;
/*     */   }
/*     */   
/*     */   public M getMetadata() {
/*  87 */     return this.metadata;
/*     */   }
/*     */   
/*     */   public void setMetadata(M metadata) {
/*  91 */     this.metadata = metadata;
/*     */   }
/*     */   
/*     */   public Document createMetadataDocument() {
/*     */     try {
/*  96 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  97 */       factory.setNamespaceAware(true);
/*  98 */       Document d = factory.newDocumentBuilder().newDocument();
/*  99 */       Element rootElement = d.createElementNS("urn:fourthline-org:cling:support:content-directory-desc-1-0", "desc-wrapper");
/* 100 */       d.appendChild(rootElement);
/* 101 */       return d;
/* 102 */     } catch (Exception ex) {
/* 103 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\DescMeta.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
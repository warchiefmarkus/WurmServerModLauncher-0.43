/*     */ package org.fourthline.cling.model.resource;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.model.ExpirationDetails;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Resource<M>
/*     */ {
/*     */   private URI pathQuery;
/*     */   private M model;
/*     */   
/*     */   public Resource(URI pathQuery, M model) {
/*     */     try {
/*  42 */       this.pathQuery = new URI(null, null, pathQuery.getPath(), pathQuery.getQuery(), null);
/*  43 */     } catch (URISyntaxException ex) {
/*  44 */       throw new RuntimeException(ex);
/*     */     } 
/*  46 */     this.model = model;
/*  47 */     if (model == null) {
/*  48 */       throw new IllegalArgumentException("Model instance must not be null");
/*     */     }
/*     */   }
/*     */   
/*     */   public URI getPathQuery() {
/*  53 */     return this.pathQuery;
/*     */   }
/*     */   
/*     */   public M getModel() {
/*  57 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(URI pathQuery) {
/*  66 */     return pathQuery.equals(getPathQuery());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void maintain(List<Runnable> pendingExecutions, ExpirationDetails expirationDetails) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  95 */     if (this == o) return true; 
/*  96 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/*  98 */     Resource resource = (Resource)o;
/*     */     
/* 100 */     if (!getPathQuery().equals(resource.getPathQuery())) return false;
/*     */     
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 107 */     return getPathQuery().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return "(" + getClass().getSimpleName() + ") URI: " + getPathQuery();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\resource\Resource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
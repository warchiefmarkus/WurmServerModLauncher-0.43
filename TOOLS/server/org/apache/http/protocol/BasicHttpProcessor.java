/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public final class BasicHttpProcessor
/*     */   implements HttpProcessor, HttpRequestInterceptorList, HttpResponseInterceptorList, Cloneable
/*     */ {
/*  55 */   protected final List<HttpRequestInterceptor> requestInterceptors = new ArrayList<HttpRequestInterceptor>();
/*  56 */   protected final List<HttpResponseInterceptor> responseInterceptors = new ArrayList<HttpResponseInterceptor>();
/*     */   
/*     */   public void addRequestInterceptor(HttpRequestInterceptor itcp) {
/*  59 */     if (itcp == null) {
/*     */       return;
/*     */     }
/*  62 */     this.requestInterceptors.add(itcp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
/*  67 */     if (itcp == null) {
/*     */       return;
/*     */     }
/*  70 */     this.requestInterceptors.add(index, itcp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
/*  75 */     if (itcp == null) {
/*     */       return;
/*     */     }
/*  78 */     this.responseInterceptors.add(index, itcp);
/*     */   }
/*     */   
/*     */   public void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
/*  82 */     Iterator<HttpRequestInterceptor> it = this.requestInterceptors.iterator();
/*  83 */     while (it.hasNext()) {
/*  84 */       Object request = it.next();
/*  85 */       if (request.getClass().equals(clazz)) {
/*  86 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
/*  92 */     Iterator<HttpResponseInterceptor> it = this.responseInterceptors.iterator();
/*  93 */     while (it.hasNext()) {
/*  94 */       Object request = it.next();
/*  95 */       if (request.getClass().equals(clazz)) {
/*  96 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpRequestInterceptor interceptor) {
/* 102 */     addRequestInterceptor(interceptor);
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpRequestInterceptor interceptor, int index) {
/* 106 */     addRequestInterceptor(interceptor, index);
/*     */   }
/*     */   
/*     */   public int getRequestInterceptorCount() {
/* 110 */     return this.requestInterceptors.size();
/*     */   }
/*     */   
/*     */   public HttpRequestInterceptor getRequestInterceptor(int index) {
/* 114 */     if (index < 0 || index >= this.requestInterceptors.size())
/* 115 */       return null; 
/* 116 */     return this.requestInterceptors.get(index);
/*     */   }
/*     */   
/*     */   public void clearRequestInterceptors() {
/* 120 */     this.requestInterceptors.clear();
/*     */   }
/*     */   
/*     */   public void addResponseInterceptor(HttpResponseInterceptor itcp) {
/* 124 */     if (itcp == null) {
/*     */       return;
/*     */     }
/* 127 */     this.responseInterceptors.add(itcp);
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpResponseInterceptor interceptor) {
/* 131 */     addResponseInterceptor(interceptor);
/*     */   }
/*     */   
/*     */   public final void addInterceptor(HttpResponseInterceptor interceptor, int index) {
/* 135 */     addResponseInterceptor(interceptor, index);
/*     */   }
/*     */   
/*     */   public int getResponseInterceptorCount() {
/* 139 */     return this.responseInterceptors.size();
/*     */   }
/*     */   
/*     */   public HttpResponseInterceptor getResponseInterceptor(int index) {
/* 143 */     if (index < 0 || index >= this.responseInterceptors.size())
/* 144 */       return null; 
/* 145 */     return this.responseInterceptors.get(index);
/*     */   }
/*     */   
/*     */   public void clearResponseInterceptors() {
/* 149 */     this.responseInterceptors.clear();
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
/*     */   public void setInterceptors(List<?> list) {
/* 170 */     if (list == null) {
/* 171 */       throw new IllegalArgumentException("List must not be null.");
/*     */     }
/* 173 */     this.requestInterceptors.clear();
/* 174 */     this.responseInterceptors.clear();
/* 175 */     for (int i = 0; i < list.size(); i++) {
/* 176 */       Object obj = list.get(i);
/* 177 */       if (obj instanceof HttpRequestInterceptor) {
/* 178 */         addInterceptor((HttpRequestInterceptor)obj);
/*     */       }
/* 180 */       if (obj instanceof HttpResponseInterceptor) {
/* 181 */         addInterceptor((HttpResponseInterceptor)obj);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearInterceptors() {
/* 190 */     clearRequestInterceptors();
/* 191 */     clearResponseInterceptors();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
/* 198 */     for (int i = 0; i < this.requestInterceptors.size(); i++) {
/* 199 */       HttpRequestInterceptor interceptor = this.requestInterceptors.get(i);
/*     */       
/* 201 */       interceptor.process(request, context);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
/* 209 */     for (int i = 0; i < this.responseInterceptors.size(); i++) {
/* 210 */       HttpResponseInterceptor interceptor = this.responseInterceptors.get(i);
/*     */       
/* 212 */       interceptor.process(response, context);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyInterceptors(BasicHttpProcessor target) {
/* 223 */     target.requestInterceptors.clear();
/* 224 */     target.requestInterceptors.addAll(this.requestInterceptors);
/* 225 */     target.responseInterceptors.clear();
/* 226 */     target.responseInterceptors.addAll(this.responseInterceptors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpProcessor copy() {
/* 235 */     BasicHttpProcessor clone = new BasicHttpProcessor();
/* 236 */     copyInterceptors(clone);
/* 237 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 242 */     BasicHttpProcessor clone = (BasicHttpProcessor)super.clone();
/* 243 */     copyInterceptors(clone);
/* 244 */     return clone;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\BasicHttpProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
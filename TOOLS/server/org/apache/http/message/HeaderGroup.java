/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HeaderGroup
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2608834160639271617L;
/*  60 */   private final List<Header> headers = new ArrayList<Header>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  67 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/*  77 */     if (header == null) {
/*     */       return;
/*     */     }
/*  80 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHeader(Header header) {
/*  89 */     if (header == null) {
/*     */       return;
/*     */     }
/*  92 */     this.headers.remove(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateHeader(Header header) {
/* 103 */     if (header == null) {
/*     */       return;
/*     */     }
/* 106 */     for (int i = 0; i < this.headers.size(); i++) {
/* 107 */       Header current = this.headers.get(i);
/* 108 */       if (current.getName().equalsIgnoreCase(header.getName())) {
/* 109 */         this.headers.set(i, header);
/*     */         return;
/*     */       } 
/*     */     } 
/* 113 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeaders(Header[] headers) {
/* 124 */     clear();
/* 125 */     if (headers == null) {
/*     */       return;
/*     */     }
/* 128 */     for (int i = 0; i < headers.length; i++) {
/* 129 */       this.headers.add(headers[i]);
/*     */     }
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
/*     */   public Header getCondensedHeader(String name) {
/* 145 */     Header[] headers = getHeaders(name);
/*     */     
/* 147 */     if (headers.length == 0)
/* 148 */       return null; 
/* 149 */     if (headers.length == 1) {
/* 150 */       return headers[0];
/*     */     }
/* 152 */     CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
/* 153 */     valueBuffer.append(headers[0].getValue());
/* 154 */     for (int i = 1; i < headers.length; i++) {
/* 155 */       valueBuffer.append(", ");
/* 156 */       valueBuffer.append(headers[i].getValue());
/*     */     } 
/*     */     
/* 159 */     return new BasicHeader(name.toLowerCase(Locale.ENGLISH), valueBuffer.toString());
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
/*     */   public Header[] getHeaders(String name) {
/* 174 */     List<Header> headersFound = new ArrayList<Header>();
/*     */     
/* 176 */     for (int i = 0; i < this.headers.size(); i++) {
/* 177 */       Header header = this.headers.get(i);
/* 178 */       if (header.getName().equalsIgnoreCase(name)) {
/* 179 */         headersFound.add(header);
/*     */       }
/*     */     } 
/*     */     
/* 183 */     return headersFound.<Header>toArray(new Header[headersFound.size()]);
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
/*     */   public Header getFirstHeader(String name) {
/* 195 */     for (int i = 0; i < this.headers.size(); i++) {
/* 196 */       Header header = this.headers.get(i);
/* 197 */       if (header.getName().equalsIgnoreCase(name)) {
/* 198 */         return header;
/*     */       }
/*     */     } 
/* 201 */     return null;
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
/*     */   public Header getLastHeader(String name) {
/* 214 */     for (int i = this.headers.size() - 1; i >= 0; i--) {
/* 215 */       Header header = this.headers.get(i);
/* 216 */       if (header.getName().equalsIgnoreCase(name)) {
/* 217 */         return header;
/*     */       }
/*     */     } 
/*     */     
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header[] getAllHeaders() {
/* 230 */     return this.headers.<Header>toArray(new Header[this.headers.size()]);
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
/*     */   public boolean containsHeader(String name) {
/* 243 */     for (int i = 0; i < this.headers.size(); i++) {
/* 244 */       Header header = this.headers.get(i);
/* 245 */       if (header.getName().equalsIgnoreCase(name)) {
/* 246 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderIterator iterator() {
/* 261 */     return new BasicListHeaderIterator(this.headers, null);
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
/*     */   public HeaderIterator iterator(String name) {
/* 275 */     return new BasicListHeaderIterator(this.headers, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderGroup copy() {
/* 286 */     HeaderGroup clone = new HeaderGroup();
/* 287 */     clone.headers.addAll(this.headers);
/* 288 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 293 */     return super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 298 */     return this.headers.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\HeaderGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
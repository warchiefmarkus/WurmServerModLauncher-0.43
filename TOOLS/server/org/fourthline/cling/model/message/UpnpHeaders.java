/*     */ package org.fourthline.cling.model.message;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.seamless.http.Headers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpnpHeaders
/*     */   extends Headers
/*     */ {
/*  36 */   private static final Logger log = Logger.getLogger(UpnpHeaders.class.getName());
/*     */   
/*     */   protected Map<UpnpHeader.Type, List<UpnpHeader>> parsedHeaders;
/*     */ 
/*     */   
/*     */   public UpnpHeaders() {}
/*     */   
/*     */   public UpnpHeaders(Map<String, List<String>> headers) {
/*  44 */     super(headers);
/*     */   }
/*     */   
/*     */   public UpnpHeaders(ByteArrayInputStream inputStream) {
/*  48 */     super(inputStream);
/*     */   }
/*     */   
/*     */   public UpnpHeaders(boolean normalizeHeaders) {
/*  52 */     super(normalizeHeaders);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseHeaders() {
/*  57 */     this.parsedHeaders = new LinkedHashMap<>();
/*  58 */     if (log.isLoggable(Level.FINE))
/*  59 */       log.fine("Parsing all HTTP headers for known UPnP headers: " + size()); 
/*  60 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)entrySet()) {
/*     */       
/*  62 */       if (entry.getKey() == null)
/*     */         continue; 
/*  64 */       UpnpHeader.Type type = UpnpHeader.Type.getByHttpName(entry.getKey());
/*  65 */       if (type == null) {
/*  66 */         if (log.isLoggable(Level.FINE)) {
/*  67 */           log.fine("Ignoring non-UPNP HTTP header: " + (String)entry.getKey());
/*     */         }
/*     */         continue;
/*     */       } 
/*  71 */       for (String value : entry.getValue()) {
/*  72 */         UpnpHeader upnpHeader = UpnpHeader.newInstance(type, value);
/*  73 */         if (upnpHeader == null || upnpHeader.getValue() == null) {
/*  74 */           if (log.isLoggable(Level.FINE)) {
/*  75 */             log.fine("Ignoring known but irrelevant header (value violates the UDA specification?) '" + type
/*     */                 
/*  77 */                 .getHttpName() + "': " + value);
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/*  82 */         addParsedValue(type, upnpHeader);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addParsedValue(UpnpHeader.Type type, UpnpHeader value) {
/*  89 */     if (log.isLoggable(Level.FINE))
/*  90 */       log.fine("Adding parsed header: " + value); 
/*  91 */     List<UpnpHeader> list = this.parsedHeaders.get(type);
/*  92 */     if (list == null) {
/*  93 */       list = new LinkedList<>();
/*  94 */       this.parsedHeaders.put(type, list);
/*     */     } 
/*  96 */     list.add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> put(String key, List<String> values) {
/* 101 */     this.parsedHeaders = null;
/* 102 */     return super.put(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String key, String value) {
/* 107 */     this.parsedHeaders = null;
/* 108 */     super.add(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> remove(Object key) {
/* 113 */     this.parsedHeaders = null;
/* 114 */     return super.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 119 */     this.parsedHeaders = null;
/* 120 */     super.clear();
/*     */   }
/*     */   
/*     */   public boolean containsKey(UpnpHeader.Type type) {
/* 124 */     if (this.parsedHeaders == null) parseHeaders(); 
/* 125 */     return this.parsedHeaders.containsKey(type);
/*     */   }
/*     */   
/*     */   public List<UpnpHeader> get(UpnpHeader.Type type) {
/* 129 */     if (this.parsedHeaders == null) parseHeaders(); 
/* 130 */     return this.parsedHeaders.get(type);
/*     */   }
/*     */   
/*     */   public void add(UpnpHeader.Type type, UpnpHeader value) {
/* 134 */     super.add(type.getHttpName(), value.getString());
/* 135 */     if (this.parsedHeaders != null)
/* 136 */       addParsedValue(type, value); 
/*     */   }
/*     */   
/*     */   public void remove(UpnpHeader.Type type) {
/* 140 */     super.remove(type.getHttpName());
/* 141 */     if (this.parsedHeaders != null)
/* 142 */       this.parsedHeaders.remove(type); 
/*     */   }
/*     */   
/*     */   public UpnpHeader[] getAsArray(UpnpHeader.Type type) {
/* 146 */     if (this.parsedHeaders == null) parseHeaders(); 
/* 147 */     return (this.parsedHeaders.get(type) != null) ? (UpnpHeader[])((List)this.parsedHeaders
/* 148 */       .get(type)).toArray((Object[])new UpnpHeader[((List)this.parsedHeaders.get(type)).size()]) : new UpnpHeader[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public UpnpHeader getFirstHeader(UpnpHeader.Type type) {
/* 153 */     return ((getAsArray(type)).length > 0) ? 
/* 154 */       getAsArray(type)[0] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public <H extends UpnpHeader> H getFirstHeader(UpnpHeader.Type type, Class<H> subtype) {
/* 159 */     UpnpHeader[] headers = getAsArray(type);
/* 160 */     if (headers.length == 0) return null;
/*     */     
/* 162 */     for (UpnpHeader header : headers) {
/* 163 */       if (subtype.isAssignableFrom(header.getClass())) {
/* 164 */         return (H)header;
/*     */       }
/*     */     } 
/* 167 */     return null;
/*     */   }
/*     */   
/*     */   public String getFirstHeaderString(UpnpHeader.Type type) {
/* 171 */     UpnpHeader header = getFirstHeader(type);
/* 172 */     return (header != null) ? header.getString() : null;
/*     */   }
/*     */   
/*     */   public void log() {
/* 176 */     if (log.isLoggable(Level.FINE)) {
/* 177 */       log.fine("############################ RAW HEADERS ###########################");
/* 178 */       for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)entrySet()) {
/* 179 */         log.fine("=== NAME : " + (String)entry.getKey());
/* 180 */         for (String v : entry.getValue()) {
/* 181 */           log.fine("VALUE: " + v);
/*     */         }
/*     */       } 
/* 184 */       if (this.parsedHeaders != null && this.parsedHeaders.size() > 0) {
/* 185 */         log.fine("########################## PARSED HEADERS ##########################");
/* 186 */         for (Map.Entry<UpnpHeader.Type, List<UpnpHeader>> entry : this.parsedHeaders.entrySet()) {
/* 187 */           log.fine("=== TYPE: " + entry.getKey());
/* 188 */           for (UpnpHeader upnpHeader : entry.getValue()) {
/* 189 */             log.fine("HEADER: " + upnpHeader);
/*     */           }
/*     */         } 
/*     */       } 
/* 193 */       log.fine("####################################################################");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\UpnpHeaders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
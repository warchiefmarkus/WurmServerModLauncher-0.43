/*     */ package org.fourthline.cling.support.model.dlna.message;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.support.model.dlna.message.header.DLNAHeader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DLNAHeaders
/*     */   extends UpnpHeaders
/*     */ {
/*  38 */   private static final Logger log = Logger.getLogger(DLNAHeaders.class.getName());
/*     */   
/*     */   protected Map<DLNAHeader.Type, List<UpnpHeader>> parsedDLNAHeaders;
/*     */ 
/*     */   
/*     */   public DLNAHeaders() {}
/*     */   
/*     */   public DLNAHeaders(Map<String, List<String>> headers) {
/*  46 */     super(headers);
/*     */   }
/*     */   
/*     */   public DLNAHeaders(ByteArrayInputStream inputStream) {
/*  50 */     super(inputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseHeaders() {
/*  55 */     if (this.parsedHeaders == null) super.parseHeaders();
/*     */ 
/*     */     
/*  58 */     this.parsedDLNAHeaders = new LinkedHashMap<>();
/*  59 */     log.log(Level.FINE, "Parsing all HTTP headers for known UPnP headers: {0}", Integer.valueOf(size()));
/*  60 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)entrySet()) {
/*     */       
/*  62 */       if (entry.getKey() == null)
/*     */         continue; 
/*  64 */       DLNAHeader.Type type = DLNAHeader.Type.getByHttpName(entry.getKey());
/*  65 */       if (type == null) {
/*  66 */         log.log(Level.FINE, "Ignoring non-UPNP HTTP header: {0}", entry.getKey());
/*     */         
/*     */         continue;
/*     */       } 
/*  70 */       for (String value : entry.getValue()) {
/*  71 */         DLNAHeader dLNAHeader = DLNAHeader.newInstance(type, value);
/*  72 */         if (dLNAHeader == null || dLNAHeader.getValue() == null) {
/*  73 */           log.log(Level.FINE, "Ignoring known but non-parsable header (value violates the UDA specification?) '{0}': {1}", new Object[] { type.getHttpName(), value }); continue;
/*     */         } 
/*  75 */         addParsedValue(type, (UpnpHeader)dLNAHeader);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addParsedValue(DLNAHeader.Type type, UpnpHeader value) {
/*  82 */     log.log(Level.FINE, "Adding parsed header: {0}", value);
/*  83 */     List<UpnpHeader> list = this.parsedDLNAHeaders.get(type);
/*  84 */     if (list == null) {
/*  85 */       list = new LinkedList<>();
/*  86 */       this.parsedDLNAHeaders.put(type, list);
/*     */     } 
/*  88 */     list.add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> put(String key, List<String> values) {
/*  93 */     this.parsedDLNAHeaders = null;
/*  94 */     return super.put(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String key, String value) {
/*  99 */     this.parsedDLNAHeaders = null;
/* 100 */     super.add(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> remove(Object key) {
/* 105 */     this.parsedDLNAHeaders = null;
/* 106 */     return super.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 111 */     this.parsedDLNAHeaders = null;
/* 112 */     super.clear();
/*     */   }
/*     */   
/*     */   public boolean containsKey(DLNAHeader.Type type) {
/* 116 */     if (this.parsedDLNAHeaders == null) parseHeaders(); 
/* 117 */     return this.parsedDLNAHeaders.containsKey(type);
/*     */   }
/*     */   
/*     */   public List<UpnpHeader> get(DLNAHeader.Type type) {
/* 121 */     if (this.parsedDLNAHeaders == null) parseHeaders(); 
/* 122 */     return this.parsedDLNAHeaders.get(type);
/*     */   }
/*     */   
/*     */   public void add(DLNAHeader.Type type, UpnpHeader value) {
/* 126 */     super.add(type.getHttpName(), value.getString());
/* 127 */     if (this.parsedDLNAHeaders != null)
/* 128 */       addParsedValue(type, value); 
/*     */   }
/*     */   
/*     */   public void remove(DLNAHeader.Type type) {
/* 132 */     super.remove(type.getHttpName());
/* 133 */     if (this.parsedDLNAHeaders != null)
/* 134 */       this.parsedDLNAHeaders.remove(type); 
/*     */   }
/*     */   
/*     */   public UpnpHeader[] getAsArray(DLNAHeader.Type type) {
/* 138 */     if (this.parsedDLNAHeaders == null) parseHeaders(); 
/* 139 */     return (this.parsedDLNAHeaders.get(type) != null) ? (UpnpHeader[])((List)this.parsedDLNAHeaders
/* 140 */       .get(type)).toArray((Object[])new UpnpHeader[((List)this.parsedDLNAHeaders.get(type)).size()]) : new UpnpHeader[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public UpnpHeader getFirstHeader(DLNAHeader.Type type) {
/* 145 */     return ((getAsArray(type)).length > 0) ? 
/* 146 */       getAsArray(type)[0] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public <H extends UpnpHeader> H getFirstHeader(DLNAHeader.Type type, Class<H> subtype) {
/* 151 */     UpnpHeader[] headers = getAsArray(type);
/* 152 */     if (headers.length == 0) return null;
/*     */     
/* 154 */     for (UpnpHeader header : headers) {
/* 155 */       if (subtype.isAssignableFrom(header.getClass())) {
/* 156 */         return (H)header;
/*     */       }
/*     */     } 
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void log() {
/* 164 */     if (log.isLoggable(Level.FINE)) {
/* 165 */       super.log();
/* 166 */       if (this.parsedDLNAHeaders != null && this.parsedDLNAHeaders.size() > 0) {
/* 167 */         log.fine("########################## PARSED DLNA HEADERS ##########################");
/* 168 */         for (Map.Entry<DLNAHeader.Type, List<UpnpHeader>> entry : this.parsedDLNAHeaders.entrySet()) {
/* 169 */           log.log(Level.FINE, "=== TYPE: {0}", entry.getKey());
/* 170 */           for (UpnpHeader upnpHeader : entry.getValue()) {
/* 171 */             log.log(Level.FINE, "HEADER: {0}", upnpHeader);
/*     */           }
/*     */         } 
/*     */       } 
/* 175 */       log.fine("####################################################################");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\DLNAHeaders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */